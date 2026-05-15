package com.ecoskiller.mcp.k3s.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Security layer for the k3s MCP Server.
 *
 * Responsibilities:
 *  1. Raw input length / character validation
 *  2. JSON-RPC method name whitelist
 *  3. Tool name whitelist + regex guard
 *  4. Argument injection prevention (shell, path traversal, YAML injection)
 *  5. Rate limiting per tool (token-bucket)
 *  6. Secrets redaction from logs
 */
public class SecurityValidator {

    private static final Logger LOGGER = Logger.getLogger(SecurityValidator.class.getName());

    // ── Config ────────────────────────────────────────────────────────────────
    private static final int MAX_RAW_BYTES     = 128 * 1024;  // 128 KB
    private static final int MAX_ARG_LENGTH    = 4096;
    private static final int RATE_LIMIT_PER_MIN = 60;

    // ── Whitelists ────────────────────────────────────────────────────────────
    private static final Set<String> ALLOWED_METHODS = Set.of(
        "initialize", "ping", "tools/list", "tools/call"
    );

    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "k3s_cluster_status",     "k3s_node_management",   "k3s_pod_lifecycle",
        "k3s_deployment_manager", "k3s_service_discovery", "k3s_ingress_controller",
        "k3s_horizontal_autoscaler","k3s_persistent_volume","k3s_config_secrets",
        "k3s_rbac_policy",        "k3s_network_policy",    "k3s_rolling_update",
        "k3s_etcd_backup",        "k3s_observability",     "k3s_gitops_argocd",
        "k3s_multicloud_failover","k3s_security_scanner",  "k3s_disaster_recovery",
        "k3s_compliance_audit",   "k3s_cluster_upgrade"
    );

    // ── Injection patterns ────────────────────────────────────────────────────
    private static final List<Pattern> INJECTION_PATTERNS = List.of(
        // Shell meta-characters
        Pattern.compile("[;&|`$(){}<>]"),
        // Null bytes
        Pattern.compile("\\x00"),
        // Path traversal
        Pattern.compile("\\.\\./|\\.\\.[/\\\\]"),
        // YAML injection anchors
        Pattern.compile("!!python|!!java|!!ruby"),
        // Script injection
        Pattern.compile("<script|javascript:|vbscript:", Pattern.CASE_INSENSITIVE),
        // SQL injection keywords (basic guard)
        Pattern.compile("(?i)(union\\s+select|drop\\s+table|;\\s*delete|exec\\s*\\()")
    );

    // Kubernetes-safe identifier pattern
    private static final Pattern K8S_NAME_PATTERN =
        Pattern.compile("^[a-z0-9]([a-z0-9\\-\\.]{0,251}[a-z0-9])?$");

    // Namespace pattern
    private static final Pattern NAMESPACE_PATTERN =
        Pattern.compile("^[a-z0-9][a-z0-9\\-]{0,62}[a-z0-9]$|^[a-z0-9]$");

    // ── Rate limiting ─────────────────────────────────────────────────────────
    private final ConcurrentHashMap<String, AtomicInteger> callCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long>          windowStart = new ConcurrentHashMap<>();

    // ─────────────────────────────────────────────────────────────────────────

    public void validateRawInput(String raw) {
        if (raw == null || raw.isEmpty()) {
            throw new SecurityException("Empty input");
        }
        if (raw.getBytes().length > MAX_RAW_BYTES) {
            throw new SecurityException("Input too large: " + raw.getBytes().length + " bytes");
        }
        // No null bytes in raw stream
        if (raw.contains("\0")) {
            throw new SecurityException("Null byte in input stream");
        }
    }

    public boolean validateMethodName(String method) {
        return method != null && ALLOWED_METHODS.contains(method);
    }

    public boolean validateToolName(String toolName) {
        if (toolName == null || toolName.isBlank()) return false;
        return ALLOWED_TOOLS.contains(toolName);
    }

    public boolean validateToolCallParams(JsonNode params) {
        if (params == null || !params.isObject()) return false;
        if (!params.has("name")) return false;
        // Max depth guard
        return params.size() <= 10;
    }

    /**
     * Sanitize all argument values.
     * Returns null if any value fails validation (signals rejection).
     */
    public Map<String, String> sanitizeArguments(JsonNode argsNode) {
        if (argsNode == null || !argsNode.isObject()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = argsNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key   = entry.getKey();
            String value = entry.getValue().asText();

            // Key validation
            if (!isValidKey(key)) {
                LOGGER.warning("[SECURITY] Invalid argument key: " + key);
                return null;
            }
            // Value length
            if (value.length() > MAX_ARG_LENGTH) {
                LOGGER.warning("[SECURITY] Argument too long: " + key);
                return null;
            }
            // Injection check
            if (containsInjection(value)) {
                LOGGER.warning("[SECURITY] Injection pattern in arg: " + key);
                return null;
            }
            result.put(key, value);
        }
        return result;
    }

    public boolean checkRateLimit(String toolName) {
        long now = System.currentTimeMillis();
        windowStart.putIfAbsent(toolName, now);
        callCounts.putIfAbsent(toolName, new AtomicInteger(0));

        long window = windowStart.get(toolName);
        if (now - window > 60_000L) {
            // Reset window
            windowStart.put(toolName, now);
            callCounts.get(toolName).set(0);
        }
        int count = callCounts.get(toolName).incrementAndGet();
        return count <= RATE_LIMIT_PER_MIN;
    }

    /**
     * Validate a Kubernetes resource name.
     */
    public boolean validateK8sName(String name) {
        if (name == null || name.isBlank()) return true; // optional
        return K8S_NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Validate a Kubernetes namespace.
     */
    public boolean validateNamespace(String ns) {
        if (ns == null || ns.isBlank()) return true;
        return NAMESPACE_PATTERN.matcher(ns).matches();
    }

    /**
     * Redact secrets from log strings.
     */
    public String redactSecrets(String input) {
        if (input == null) return "";
        return input
            .replaceAll("(?i)(password|secret|token|key|credential)\\s*[=:]\\s*\\S+",
                        "$1=***REDACTED***")
            .replaceAll("[A-Za-z0-9+/]{40,}={0,2}", "***BASE64_REDACTED***");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    private boolean isValidKey(String key) {
        if (key == null || key.isBlank() || key.length() > 64) return false;
        return key.matches("[a-zA-Z_][a-zA-Z0-9_\\-]{0,63}");
    }

    private boolean containsInjection(String value) {
        for (Pattern p : INJECTION_PATTERNS) {
            if (p.matcher(value).find()) return true;
        }
        return false;
    }
}
