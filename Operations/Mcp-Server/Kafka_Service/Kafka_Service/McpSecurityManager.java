package com.ecoskiller.mcp.kafka.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Security layer for the Ecoskiller Kafka MCP Server.
 *
 * Responsibilities:
 *  - API token validation (Bearer token, HMAC-SHA256 signed)
 *  - Rate limiting per token (sliding window)
 *  - Audit logging of every tool invocation
 *  - Input sanitisation (injection prevention)
 *  - Kafka SASL/SSL credential management from environment
 */
public class McpSecurityManager {

    private static final Logger log = LoggerFactory.getLogger(McpSecurityManager.class);
    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");

    // ── Rate Limiting ────────────────────────────────────────────────────────
    private static final int MAX_REQUESTS_PER_MINUTE = 120;
    private final Map<String, Deque<Long>> rateLimitWindows = new ConcurrentHashMap<>();

    // ── Valid Tokens (loaded from env / k8s secret) ──────────────────────────
    private final Set<String> validTokenHashes;

    // ── Allowed topic name pattern (Ecoskiller naming convention) ────────────
    // Matches: user.created, gd.completed, invoice.generated.dlq, dev.gd.completed …
    private static final java.util.regex.Pattern SAFE_TOPIC_PATTERN =
        java.util.regex.Pattern.compile("^[a-z0-9][a-z0-9._\\-]{0,248}$");

    // ── Allowed consumer group pattern ───────────────────────────────────────
    private static final java.util.regex.Pattern SAFE_CONSUMER_GROUP_PATTERN =
        java.util.regex.Pattern.compile("^[a-z0-9][a-z0-9._\\-]{0,248}$");

    // ── Cleanup scheduler ────────────────────────────────────────────────────
    private final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "mcp-security-cleanup");
            t.setDaemon(true);
            return t;
        });

    public McpSecurityManager() {
        this.validTokenHashes = loadTokenHashesFromEnv();
        // Purge stale rate-limit entries every 2 minutes
        scheduler.scheduleAtFixedRate(this::purgeRateLimitCache, 2, 2, TimeUnit.MINUTES);
        log.info("McpSecurityManager initialised — {} token(s) registered", validTokenHashes.size());
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Validates the Bearer token supplied in the MCP request.
     * Returns false (and logs) if the token is missing, malformed, or unknown.
     */
    public boolean isAuthorised(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            auditLog.warn("AUTH_FAIL — no token presented");
            return false;
        }
        String stripped = rawToken.startsWith("Bearer ") ? rawToken.substring(7).trim() : rawToken.trim();
        if (stripped.length() < 32 || stripped.length() > 512) {
            auditLog.warn("AUTH_FAIL — token length out of bounds");
            return false;
        }
        String hash = sha256Hex(stripped);
        boolean ok = validTokenHashes.contains(hash);
        if (!ok) {
            auditLog.warn("AUTH_FAIL — unrecognised token hash={}", hash.substring(0, 8) + "…");
        }
        return ok;
    }

    /**
     * Sliding-window rate limiter.  Returns false when the caller has exceeded
     * MAX_REQUESTS_PER_MINUTE in the last 60 seconds.
     */
    public boolean checkRateLimit(String tokenIdentifier) {
        long now = System.currentTimeMillis();
        long windowStart = now - 60_000L;

        Deque<Long> hits = rateLimitWindows.computeIfAbsent(tokenIdentifier, k -> new ArrayDeque<>());
        synchronized (hits) {
            // Drop timestamps outside the window
            while (!hits.isEmpty() && hits.peekFirst() < windowStart) {
                hits.pollFirst();
            }
            if (hits.size() >= MAX_REQUESTS_PER_MINUTE) {
                auditLog.warn("RATE_LIMIT — token={} hits={}", tokenIdentifier.substring(0, 8) + "…", hits.size());
                return false;
            }
            hits.addLast(now);
        }
        return true;
    }

    /**
     * Writes a structured audit record for every tool call that passes auth.
     */
    public void auditToolCall(String toolName, String tokenIdentifier, Map<String, Object> params) {
        auditLog.info("TOOL_CALL tool={} caller={} params={}",
            toolName,
            tokenIdentifier.substring(0, Math.min(8, tokenIdentifier.length())) + "…",
            sanitiseForLog(params));
    }

    /**
     * Validates a Kafka topic name against the Ecoskiller naming convention.
     * Rejects anything that could be used for injection or path traversal.
     */
    public void validateTopicName(String topic) {
        if (topic == null || topic.isBlank()) {
            throw new SecurityException("Topic name must not be null or blank");
        }
        if (!SAFE_TOPIC_PATTERN.matcher(topic).matches()) {
            throw new SecurityException("Topic name contains illegal characters: " + sanitiseForLog(topic));
        }
    }

    /**
     * Validates a Kafka consumer group name.
     */
    public void validateConsumerGroup(String group) {
        if (group == null || group.isBlank()) {
            throw new SecurityException("Consumer group must not be null or blank");
        }
        if (!SAFE_CONSUMER_GROUP_PATTERN.matcher(group).matches()) {
            throw new SecurityException("Consumer group contains illegal characters: " + sanitiseForLog(group));
        }
    }

    /**
     * Sanitises a free-text string for safe inclusion in a JSON response
     * (prevents log injection / response injection).
     */
    public String sanitiseString(String input) {
        if (input == null) return "";
        // Remove control characters; truncate to 2 KB
        String clean = input.replaceAll("[\\x00-\\x1F\\x7F]", "").trim();
        return clean.length() > 2048 ? clean.substring(0, 2048) + " [TRUNCATED]" : clean;
    }

    /**
     * Returns a copy of the Kafka producer/consumer properties pre-loaded
     * with SASL/SSL credentials sourced from environment variables
     * (as mounted by Kubernetes Secrets).
     *
     * Environment variables expected (mapped from k8s secret/{env}/kafka):
     *   KAFKA_BOOTSTRAP_SERVERS   — e.g. kafka-0.kafka-headless.messaging.svc.cluster.local:9092
     *   KAFKA_SASL_USERNAME
     *   KAFKA_SASL_PASSWORD
     *   KAFKA_SECURITY_PROTOCOL   — SASL_SSL (prod) or PLAINTEXT (dev)
     *   KAFKA_SSL_TRUSTSTORE_PATH — optional, for custom CA
     *   KAFKA_SSL_TRUSTSTORE_PASS — optional
     */
    public Properties buildSecureKafkaProperties() {
        Properties props = new Properties();

        String bootstrapServers = requireEnv("KAFKA_BOOTSTRAP_SERVERS");
        String securityProtocol = getEnv("KAFKA_SECURITY_PROTOCOL", "SASL_SSL");

        props.put("bootstrap.servers", bootstrapServers);
        props.put("security.protocol", securityProtocol);

        if (securityProtocol.contains("SASL")) {
            String username = requireEnv("KAFKA_SASL_USERNAME");
            String password = requireEnv("KAFKA_SASL_PASSWORD");
            props.put("sasl.mechanism", getEnv("KAFKA_SASL_MECHANISM", "SCRAM-SHA-512"));
            props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.scram.ScramLoginModule required "
                + "username=\"" + username + "\" "
                + "password=\"" + password + "\";");
        }

        if (securityProtocol.contains("SSL")) {
            String truststore = getEnv("KAFKA_SSL_TRUSTSTORE_PATH", "");
            String truststorePass = getEnv("KAFKA_SSL_TRUSTSTORE_PASS", "");
            if (!truststore.isBlank()) {
                props.put("ssl.truststore.location", truststore);
                props.put("ssl.truststore.password", truststorePass);
            }
            props.put("ssl.endpoint.identification.algorithm", "https");
        }

        // Sensible client defaults
        props.put("request.timeout.ms", "30000");
        props.put("connections.max.idle.ms", "60000");
        props.put("reconnect.backoff.ms", "1000");
        props.put("reconnect.backoff.max.ms", "10000");

        log.info("Kafka secure properties built: bootstrap={} protocol={}", bootstrapServers, securityProtocol);
        return props;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Set<String> loadTokenHashesFromEnv() {
        Set<String> hashes = new HashSet<>();
        // Primary token
        String primary = System.getenv("MCP_API_TOKEN");
        if (primary != null && !primary.isBlank()) {
            hashes.add(sha256Hex(primary.trim()));
        }
        // Secondary (rotation support) — MCP_API_TOKEN_2, MCP_API_TOKEN_3 …
        for (int i = 2; i <= 5; i++) {
            String extra = System.getenv("MCP_API_TOKEN_" + i);
            if (extra != null && !extra.isBlank()) {
                hashes.add(sha256Hex(extra.trim()));
            }
        }
        if (hashes.isEmpty()) {
            log.warn("No MCP_API_TOKEN env var found — server will REJECT all requests. "
                   + "Set MCP_API_TOKEN in the environment or k8s secret.");
        }
        return Collections.unmodifiableSet(hashes);
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private static String requireEnv(String name) {
        String val = System.getenv(name);
        if (val == null || val.isBlank()) {
            throw new IllegalStateException("Required environment variable not set: " + name);
        }
        return val.trim();
    }

    private static String getEnv(String name, String defaultValue) {
        String val = System.getenv(name);
        return (val != null && !val.isBlank()) ? val.trim() : defaultValue;
    }

    private String sanitiseForLog(Object obj) {
        if (obj == null) return "null";
        String s = obj.toString();
        // Mask anything that looks like a password/token
        s = s.replaceAll("(?i)(password|token|secret|credential|jaas)[^,}\\]\"]*", "$1=***");
        return s.length() > 500 ? s.substring(0, 500) + "…" : s;
    }

    private void purgeRateLimitCache() {
        long cutoff = System.currentTimeMillis() - 120_000L;
        rateLimitWindows.entrySet().removeIf(e -> {
            synchronized (e.getValue()) {
                return e.getValue().isEmpty() || e.getValue().peekLast() < cutoff;
            }
        });
    }
}
