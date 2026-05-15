package io.ecoskiller.mcp.security;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * SecurityManager — Central security enforcement for Jitsi MCP Server.
 *
 * Features:
 *   1. JWT token validation (signature + expiry + claims)
 *   2. Role-Based Access Control (RBAC) per tool
 *   3. Rate limiting (per-client sliding window)
 *   4. Input sanitization helpers
 *   5. Audit logging (all requests + auth events)
 *   6. Replay attack prevention (jti nonce tracking)
 */
public class SecurityManager {

    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    // Rate limit: 100 calls / 60 seconds per client key
    private static final int    RATE_LIMIT_MAX      = 100;
    private static final long   RATE_LIMIT_WINDOW_MS = 60_000L;

    // JWT nonce cache (prevent replay attacks) — TTL = 1 hour
    private static final long NONCE_TTL_MS = 3_600_000L;

    // Secret key (in production, load from env / secrets manager)
    private static final String JWT_SECRET = System.getenv()
        .getOrDefault("JITSI_MCP_JWT_SECRET", "CHANGE_ME_IN_PRODUCTION_USE_ENV_VAR");

    // Allowed roles per tool category
    private static final Map<String, Set<String>> TOOL_ROLE_MAP = new HashMap<>() {{
        put("session_create",             Set.of("admin", "interviewer"));
        put("session_join",               Set.of("admin", "interviewer", "candidate"));
        put("session_leave",              Set.of("admin", "interviewer", "candidate"));
        put("session_status",             Set.of("admin", "interviewer", "candidate"));
        put("media_quality_get",          Set.of("admin", "interviewer", "candidate"));
        put("media_device_list",          Set.of("admin", "interviewer", "candidate"));
        put("participant_list",           Set.of("admin", "interviewer", "candidate"));
        put("participant_mute",           Set.of("admin", "interviewer"));
        put("participant_remove",         Set.of("admin"));
        put("recording_start",            Set.of("admin", "interviewer"));
        put("recording_stop",             Set.of("admin", "interviewer"));
        put("recording_status",           Set.of("admin", "interviewer", "candidate"));
        put("analytics_session_report",   Set.of("admin", "interviewer"));
        put("analytics_event_emit",       Set.of("admin", "interviewer", "candidate"));
        put("auth_token_validate",        Set.of("admin", "interviewer", "candidate"));
    }};

    // Sliding-window rate limiter: clientKey -> list of timestamps
    private final Map<String, Deque<Long>> rateLimitMap = new ConcurrentHashMap<>();

    // Seen nonces to prevent replay attacks
    private final Map<String, Long> seenNonces = new ConcurrentHashMap<>();

    // ─── JWT Validation ───────────────────────────────────────────────────────

    /**
     * Validates a JWT token and returns its claims.
     * Checks: structure, signature (HMAC-SHA256), expiry, required claims.
     */
    public JwtClaims validateToken(String token) throws SecurityException {
        if (token == null || token.isBlank()) {
            throw new SecurityException("Missing JWT token");
        }
        // Strip Bearer prefix if present
        if (token.startsWith("Bearer ")) token = token.substring(7).trim();

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new SecurityException("Malformed JWT: expected 3 parts");
        }

        // Decode header + payload (no external libraries needed)
        String headerJson;
        String payloadJson;
        try {
            headerJson  = new String(Base64.getUrlDecoder().decode(padBase64(parts[0])), StandardCharsets.UTF_8);
            payloadJson = new String(Base64.getUrlDecoder().decode(padBase64(parts[1])), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("JWT decode failed: " + e.getMessage());
        }

        // Verify algorithm is HS256
        if (!headerJson.contains("\"HS256\"")) {
            throw new SecurityException("Unsupported JWT algorithm — only HS256 is allowed");
        }

        // Verify signature
        String expectedSig = computeHmacSha256(parts[0] + "." + parts[1], JWT_SECRET);
        if (!constantTimeEquals(expectedSig, parts[2])) {
            auditLog("AUTH_FAILURE", "INVALID_SIGNATURE", null);
            throw new SecurityException("JWT signature verification failed");
        }

        // Parse claims from payload JSON (simple hand-rolled parser)
        JwtClaims claims = JwtClaims.fromJson(payloadJson);

        // Check expiry
        long nowSec = System.currentTimeMillis() / 1000;
        if (claims.getExp() > 0 && nowSec > claims.getExp()) {
            auditLog("AUTH_FAILURE", "TOKEN_EXPIRED", null);
            throw new SecurityException("JWT token expired");
        }

        // Replay attack check via jti (JWT ID / nonce)
        String jti = claims.getJti();
        if (jti != null && !jti.isBlank()) {
            if (seenNonces.containsKey(jti)) {
                auditLog("AUTH_FAILURE", "REPLAY_ATTACK jti=" + jti, null);
                throw new SecurityException("JWT replay attack detected");
            }
            seenNonces.put(jti, System.currentTimeMillis());
            pruneOldNonces();
        }

        auditLog("AUTH_SUCCESS", "userId=" + claims.getUserId() + " role=" + claims.getRole(), null);
        return claims;
    }

    // ─── RBAC ─────────────────────────────────────────────────────────────────

    /**
     * Checks if a role is allowed to call a tool.
     */
    public void checkToolPermission(String toolName, String role) throws SecurityException {
        Set<String> allowed = TOOL_ROLE_MAP.get(toolName);
        if (allowed == null) {
            throw new SecurityException("Unknown tool: " + toolName);
        }
        if (!allowed.contains(role)) {
            auditLog("AUTHZ_FAILURE", "tool=" + toolName + " role=" + role, null);
            throw new SecurityException("Role '" + role + "' is not authorized to call tool '" + toolName + "'");
        }
    }

    // ─── Rate Limiting ────────────────────────────────────────────────────────

    /**
     * Sliding-window rate limiter. Returns true if request is allowed.
     */
    public boolean checkRateLimit(String clientKey) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = rateLimitMap.computeIfAbsent(clientKey, k -> new ArrayDeque<>());
        synchronized (timestamps) {
            // Evict old timestamps outside the window
            while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > RATE_LIMIT_WINDOW_MS) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= RATE_LIMIT_MAX) {
                LOGGER.warning("[Security] Rate limit hit for client: " + clientKey);
                return false;
            }
            timestamps.addLast(now);
        }
        return true;
    }

    // ─── Input Sanitization ───────────────────────────────────────────────────

    /**
     * Sanitizes a string to prevent injection attacks.
     * Allows alphanumerics, hyphens, underscores, dots, spaces.
     */
    public String sanitize(String input, int maxLen) {
        if (input == null) return "";
        String cleaned = input.replaceAll("[^a-zA-Z0-9\\-_.@: /]", "");
        return cleaned.substring(0, Math.min(cleaned.length(), maxLen));
    }

    /**
     * Validates an assessment/room ID format: gd-XXXXX or [a-z0-9-]{3,64}
     */
    public void validateRoomId(String roomId) throws IllegalArgumentException {
        if (roomId == null || !roomId.matches("[a-zA-Z0-9\\-]{3,64}")) {
            throw new IllegalArgumentException("Invalid room ID format: " + roomId);
        }
    }

    /**
     * Validates a userId format.
     */
    public void validateUserId(String userId) throws IllegalArgumentException {
        if (userId == null || !userId.matches("[a-zA-Z0-9\\-_]{3,64}")) {
            throw new IllegalArgumentException("Invalid user ID format");
        }
    }

    // ─── Audit Logging ────────────────────────────────────────────────────────

    /**
     * Structured audit log entry — emitted to stderr (separate from JSON-RPC stdout).
     */
    public void auditLog(String event, String detail, Object requestId) {
        String entry = String.format("[AUDIT] ts=%d event=%s detail=%s reqId=%s",
            System.currentTimeMillis(), event,
            detail != null ? detail.replaceAll("\\s+", "_") : "none",
            requestId != null ? requestId.toString() : "none");
        LOGGER.info(entry);
    }

    // ─── Crypto helpers ───────────────────────────────────────────────────────

    /**
     * Computes HMAC-SHA256 and returns Base64url-encoded result.
     * Uses only java.crypto (no external dependencies).
     */
    private String computeHmacSha256(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec keySpec =
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] result = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(result);
        } catch (Exception e) {
            throw new RuntimeException("HMAC computation failed", e);
        }
    }

    /**
     * Constant-time string comparison to prevent timing attacks.
     */
    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] ab = a.getBytes(StandardCharsets.UTF_8);
        byte[] bb = b.getBytes(StandardCharsets.UTF_8);
        if (ab.length != bb.length) return false;
        int result = 0;
        for (int i = 0; i < ab.length; i++) result |= (ab[i] ^ bb[i]);
        return result == 0;
    }

    private String padBase64(String s) {
        int pad = s.length() % 4;
        if (pad == 2) return s + "==";
        if (pad == 3) return s + "=";
        return s;
    }

    private void pruneOldNonces() {
        long cutoff = System.currentTimeMillis() - NONCE_TTL_MS;
        seenNonces.entrySet().removeIf(e -> e.getValue() < cutoff);
    }
}
