package com.ecoskiller.mcp.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * Security configuration for assessment-orchestrator MCP Server.
 *
 * Roles (mirror Keycloak realm roles):
 *   CANDIDATE        — join sessions, raise hand, heartbeat
 *   RECRUITER        — schedule sessions, view roster, basic control
 *   MODERATOR        — full session control (FORCE_MUTE, ADVANCE_TURN, END_SESSION)
 *   ADMIN            — cross-tenant, reschedule, cycle cancellation
 *   SERVICE_ACCOUNT  — internal service-to-service (Kafka event injection)
 *
 * Jitsi JWT:
 *   Issued by this service using HS256 with JITSI_JWT_SECRET env var.
 *   Room = session_id. TTL = 5 minutes. Roles: moderator | attendee.
 *
 * Env vars:
 *   APP_JWT_SECRET        — Keycloak HMAC secret (HS256 dev mode)
 *   APP_STRICT_MODE       — true = enforce signature verification
 *   APP_MAX_PAYLOAD_SIZE  — max string field length (default 65535)
 *   JITSI_JWT_SECRET      — Jitsi HS256 signing secret
 *   JITSI_APP_ID          — Jitsi application ID claim
 *   JITSI_DOMAIN          — Jitsi server domain
 */
public class SecurityConfig {

    private static final Logger LOG = Logger.getLogger(SecurityConfig.class.getName());

    public static final String ROLE_CANDIDATE       = "CANDIDATE";
    public static final String ROLE_RECRUITER       = "RECRUITER";
    public static final String ROLE_MODERATOR       = "MODERATOR";
    public static final String ROLE_ADMIN           = "ADMIN";
    public static final String ROLE_SERVICE_ACCOUNT = "SERVICE_ACCOUNT";

    private static int     MAX_PAYLOAD    = 65535;
    private static String  JWT_SECRET     = "ecoskiller-dev-secret-change-in-prod";
    private static boolean STRICT_MODE    = false;
    private static String  JITSI_SECRET   = "jitsi-dev-secret-change-in-prod";
    private static String  JITSI_APP_ID   = "ecoskiller";
    private static String  JITSI_DOMAIN   = "meet.ecoskiller.internal";

    // Idempotency store (production: Redis SET NX EX)
    private static final Set<String> IDEMPOTENCY_STORE = Collections.synchronizedSet(new LinkedHashSet<>() {
        @Override public boolean add(String s) {
            if (size() >= 10_000) { Iterator<String> it = iterator(); it.next(); it.remove(); }
            return super.add(s);
        }
    });

    // Distributed lock store (production: Redis SET NX EX 30)
    private static final Map<String, Long> LOCK_STORE = Collections.synchronizedMap(new LinkedHashMap<>());

    public static void init() {
        Optional.ofNullable(System.getenv("APP_JWT_SECRET")).filter(s -> !s.isBlank()).ifPresent(s -> JWT_SECRET = s);
        Optional.ofNullable(System.getenv("JITSI_JWT_SECRET")).filter(s -> !s.isBlank()).ifPresent(s -> JITSI_SECRET = s);
        Optional.ofNullable(System.getenv("JITSI_APP_ID")).filter(s -> !s.isBlank()).ifPresent(s -> JITSI_APP_ID = s);
        Optional.ofNullable(System.getenv("JITSI_DOMAIN")).filter(s -> !s.isBlank()).ifPresent(s -> JITSI_DOMAIN = s);
        Optional.ofNullable(System.getenv("APP_STRICT_MODE")).ifPresent(s -> STRICT_MODE = "true".equalsIgnoreCase(s));
        Optional.ofNullable(System.getenv("APP_MAX_PAYLOAD_SIZE")).ifPresent(s -> {
            try { MAX_PAYLOAD = Integer.parseInt(s); } catch (NumberFormatException ignored) {}
        });
        LOG.info("SecurityConfig init | strict=" + STRICT_MODE + " | jitsiDomain=" + JITSI_DOMAIN);
    }

    // ─── JWT Validation ────────────────────────────────────────────────────────

    public static JwtClaims validateToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank())
            throw new SecurityException("Missing Authorization token");
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new SecurityException("Malformed JWT: expected 3 parts");
        if (STRICT_MODE) verifyHmac(parts[0] + "." + parts[1], parts[2], JWT_SECRET);
        return extractClaims(decodeB64(parts[1]));
    }

    private static JwtClaims extractClaims(String payloadJson) {
        String sub      = extractStr(payloadJson, "sub");
        String tenantId = extractStr(payloadJson, "tenant_id");
        String email    = extractStr(payloadJson, "email");
        List<String> roles = extractRoles(payloadJson);
        long exp = extractLong(payloadJson, "exp");
        if (exp > 0 && Instant.now().getEpochSecond() > exp)
            throw new SecurityException("JWT token has expired");
        if (sub == null || sub.isBlank()) throw new SecurityException("JWT missing 'sub'");
        if (tenantId == null || tenantId.isBlank()) tenantId = STRICT_MODE ? null : "dev-tenant";
        if (tenantId == null) throw new SecurityException("JWT missing 'tenant_id'");
        return new JwtClaims(sub, tenantId, email, roles);
    }

    // ─── Jitsi JWT Issuance ────────────────────────────────────────────────────

    /**
     * Issues a Jitsi room JWT token (HS256, 5-min TTL).
     * Room name always = session_id for backend correlation.
     * Role: moderator (for RECRUITERs/MODERATORs), attendee (for CANDIDATEs).
     */
    public static Map<String, Object> issueJitsiToken(String sessionId, String userId,
                                                       String displayName, boolean isModerator) {
        long now = Instant.now().getEpochSecond();
        long exp = now + 300; // 5-minute TTL

        // Header
        String header = b64url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");

        // Payload
        String role = isModerator ? "moderator" : "attendee";
        String payloadJson =
            "{\"iss\":\"" + JITSI_APP_ID + "\"," +
            "\"sub\":\"" + JITSI_DOMAIN + "\"," +
            "\"aud\":\"jitsi\"," +
            "\"room\":\"" + sessionId + "\"," +
            "\"exp\":" + exp + "," +
            "\"iat\":" + now + "," +
            "\"context\":{" +
                "\"user\":{" +
                    "\"id\":\"" + userId + "\"," +
                    "\"name\":\"" + sanitise(displayName, "displayName") + "\"," +
                    "\"moderator\":" + isModerator +
                "}," +
                "\"features\":{\"recording\":false,\"livestreaming\":false,\"screen-sharing\":true}" +
            "}," +
            "\"moderator\":" + isModerator + "}";

        String payload = b64url(payloadJson);
        String sig;
        if (STRICT_MODE) {
            sig = computeHmacB64(header + "." + payload, JITSI_SECRET);
        } else {
            sig = "dev-jitsi-sig";
        }
        String jitsiToken = header + "." + payload + "." + sig;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("jitsi_token",     jitsiToken);
        result.put("jitsi_room_name", sessionId);
        result.put("ws_endpoint",     "wss://" + JITSI_DOMAIN + "/ws/session/" + sessionId);
        result.put("jitsi_domain",    JITSI_DOMAIN);
        result.put("expires_at",      Instant.ofEpochSecond(exp).toString());
        result.put("role",            role);
        return result;
    }

    // ─── RBAC ─────────────────────────────────────────────────────────────────

    public static void requireRole(JwtClaims claims, String... roles) {
        for (String r : roles) if (claims.hasRole(r)) return;
        throw new SecurityException("Access denied. Required: " + Arrays.toString(roles)
                + " | has: " + claims.getRoles());
    }

    // ─── Tenant isolation ─────────────────────────────────────────────────────

    public static String effectiveTenantId(JwtClaims claims, Map<String, Object> args) {
        String req = getString(args, "tenant_id");
        if (req != null && !req.isBlank() && claims.hasRole(ROLE_ADMIN)) return req;
        return claims.getTenantId();
    }

    public static void validateTenantAccess(JwtClaims claims, String targetTenantId) {
        if (!claims.hasRole(ROLE_ADMIN) && !claims.getTenantId().equals(targetTenantId))
            throw new SecurityException("Tenant mismatch: token=" + claims.getTenantId() + " target=" + targetTenantId);
    }

    // ─── Input sanitisation ───────────────────────────────────────────────────

    private static final String[] SQL_PATTERNS  = {"--",";--","/*","*/","xp_","UNION SELECT","DROP TABLE","INSERT INTO","DELETE FROM","UPDATE SET","EXEC("};
    private static final String[] XSS_PATTERNS  = {"<script","javascript:","onerror=","onload=","eval(","document.cookie"};

    public static String sanitise(String input, String field) {
        if (input == null) return null;
        String t = input.trim();
        if (t.length() > MAX_PAYLOAD)
            throw new IllegalArgumentException("Field '" + field + "' exceeds max length " + MAX_PAYLOAD);
        String u = t.toUpperCase();
        for (String p : SQL_PATTERNS) if (u.contains(p.toUpperCase()))
            throw new SecurityException("SQL injection detected in: " + field);
        for (String p : XSS_PATTERNS) if (u.contains(p.toUpperCase()))
            throw new SecurityException("Script injection detected in: " + field);
        return t;
    }

    public static String requireString(Map<String, Object> args, String key) {
        String v = getString(args, key);
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Missing required: " + key);
        return sanitise(v, key);
    }

    public static String getString(Map<String, Object> args, String key) {
        Object v = args.get(key); return v != null ? v.toString() : null;
    }

    public static int getInt(Map<String, Object> args, String key, int def) {
        Object v = args.get(key); if (v == null) return def;
        try { return Integer.parseInt(v.toString()); } catch (NumberFormatException e) { return def; }
    }

    // ─── Idempotency ──────────────────────────────────────────────────────────

    /** Returns true if this is a NEW request (should process). */
    public static boolean checkIdempotency(String key) {
        if (key == null || key.isBlank()) return true;
        return IDEMPOTENCY_STORE.add(key);
    }

    // ─── Distributed lock (Redis SET NX EX stub) ──────────────────────────────

    /** Acquire a distributed lock. Returns true if acquired. TTL in ms. */
    public static boolean acquireLock(String lockKey, long ttlMs) {
        long now = System.currentTimeMillis();
        // Expire old locks
        LOCK_STORE.entrySet().removeIf(e -> e.getValue() < now);
        return LOCK_STORE.putIfAbsent(lockKey, now + ttlMs) == null;
    }

    public static void releaseLock(String lockKey) { LOCK_STORE.remove(lockKey); }

    // ─── Crypto helpers ───────────────────────────────────────────────────────

    private static void verifyHmac(String data, String expectedSig, String secret) {
        String computed = computeHmacB64(data, secret);
        if (!computed.equals(expectedSig))
            throw new SecurityException("JWT signature verification failed");
    }

    private static String computeHmacB64(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return java.util.Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new SecurityException("Crypto error: " + e.getMessage()); }
    }

    public static String decodeB64(String encoded) {
        try {
            String padded = encoded;
            switch (padded.length() % 4) { case 2: padded+="=="; break; case 3: padded+="="; break; }
            return new String(java.util.Base64.getUrlDecoder().decode(padded), StandardCharsets.UTF_8);
        } catch (Exception e) { throw new SecurityException("B64 decode failed: " + e.getMessage()); }
    }

    static String b64url(String s) {
        return java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String extractStr(String json, String key) {
        String k = "\"" + key + "\""; int i = json.indexOf(k); if (i < 0) return null;
        int c = json.indexOf(':', i + k.length()); if (c < 0) return null;
        int s = json.indexOf('"', c + 1); if (s < 0) return null;
        int e = json.indexOf('"', s + 1); if (e < 0) return null;
        return json.substring(s + 1, e);
    }

    private static long extractLong(String json, String key) {
        String k = "\"" + key + "\""; int i = json.indexOf(k); if (i < 0) return -1;
        int c = json.indexOf(':', i + k.length()); if (c < 0) return -1;
        StringBuilder num = new StringBuilder();
        for (int j = c + 1; j < json.length(); j++) {
            char ch = json.charAt(j);
            if (Character.isDigit(ch) || ch == '-') num.append(ch);
            else if (num.length() > 0) break;
        }
        try { return Long.parseLong(num.toString()); } catch (NumberFormatException e) { return -1; }
    }

    private static List<String> extractRoles(String json) {
        List<String> roles = new ArrayList<>();
        int ri = json.indexOf("\"realm_access\"");
        if (ri < 0) ri = json.indexOf("\"roles\"");
        if (ri < 0) return roles;
        int arrS = json.indexOf('[', ri), arrE = json.indexOf(']', arrS);
        if (arrS < 0 || arrE < 0) return roles;
        for (String p : json.substring(arrS + 1, arrE).split(",")) {
            String r = p.trim().replace("\"", "");
            if (!r.isBlank()) roles.add(r.toUpperCase());
        }
        return roles;
    }

    public static String generateId() { return UUID.randomUUID().toString(); }
}
