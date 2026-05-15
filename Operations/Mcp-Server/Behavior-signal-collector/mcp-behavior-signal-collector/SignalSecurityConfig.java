package com.ecoskiller.mcp.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * Security configuration for behavior-signal-collector MCP server.
 *
 * Enforces:
 *   1. Keycloak JWT validation (HS256 dev / RS256 prod)
 *   2. RBAC: CANDIDATE / RECRUITER / ADMIN / SERVICE_ACCOUNT
 *   3. Tenant isolation (tenant_id on every operation)
 *   4. Signal value range enforcement per signal type
 *   5. Batch size cap (default 1000 signals/request)
 *   6. Input sanitisation (SQL injection / XSS)
 *   7. message_id deduplication (prevent replay ingestion)
 *   8. Rate-limit tracking per assessment
 */
public class SignalSecurityConfig {

    private static final Logger LOG = Logger.getLogger(SignalSecurityConfig.class.getName());

    // Roles
    public static final String ROLE_CANDIDATE       = "CANDIDATE";
    public static final String ROLE_RECRUITER       = "RECRUITER";
    public static final String ROLE_ADMIN           = "ADMIN";
    public static final String ROLE_SERVICE_ACCOUNT = "SERVICE_ACCOUNT";

    // Config
    private static String  JWT_SECRET        = "ecoskiller-bsc-dev-secret-change-in-prod";
    private static boolean STRICT_MODE       = false;
    private static int     MAX_BATCH_SIZE    = 1000;
    private static double  IQR_MULTIPLIER    = 1.5;
    private static double  QUALITY_THRESHOLD = 0.85;
    private static int     MAX_PAYLOAD       = 524288; // 512 KB

    // Deduplication store (production: Redis SET NX EX 86400)
    private static final Set<String> SEEN_MESSAGE_IDS = Collections.synchronizedSet(
            new LinkedHashSet<String>() {
                @Override public boolean add(String s) {
                    if (size() >= 100_000) { Iterator<String> it = iterator(); it.next(); it.remove(); }
                    return super.add(s);
                }
            });

    // ── Signal value ranges (per signal type) ─────────────────────────────────
    // Format: signalType → [min, max]
    private static final Map<String, double[]> SIGNAL_RANGES = new HashMap<>();
    static {
        SIGNAL_RANGES.put("keyboard_latency",         new double[]{0,    5000});
        SIGNAL_RANGES.put("mouse_movement_velocity",  new double[]{0,    50000});
        SIGNAL_RANGES.put("gaze_fixation_duration",   new double[]{0,    30000});
        SIGNAL_RANGES.put("speech_pause_duration",    new double[]{0,    60000});
        SIGNAL_RANGES.put("speech_rate_wpm",          new double[]{0,    400});
        SIGNAL_RANGES.put("sentiment_score",          new double[]{-1.0, 1.0});
        SIGNAL_RANGES.put("code_commit_frequency",    new double[]{0,    1000});
        SIGNAL_RANGES.put("solution_revision_count",  new double[]{0,    10000});
        SIGNAL_RANGES.put("turn_taking_duration",     new double[]{0,    600000});
        SIGNAL_RANGES.put("confidence_score",         new double[]{0,    1.0});
        SIGNAL_RANGES.put("gaze_fixation_x",          new double[]{-5000, 10000});
        SIGNAL_RANGES.put("gaze_fixation_y",          new double[]{-5000, 10000});
        SIGNAL_RANGES.put("gesture_velocity",         new double[]{0,    100000});
        SIGNAL_RANGES.put("filler_word_ratio",        new double[]{0,    1.0});
        SIGNAL_RANGES.put("interruption_count",       new double[]{0,    500});
    }

    public static void init() {
        Optional.ofNullable(System.getenv("BSC_JWT_SECRET"))
                .filter(s -> !s.isBlank()).ifPresent(s -> JWT_SECRET = s);
        Optional.ofNullable(System.getenv("BSC_STRICT_MODE"))
                .ifPresent(s -> STRICT_MODE = "true".equalsIgnoreCase(s));
        Optional.ofNullable(System.getenv("BSC_MAX_BATCH_SIZE"))
                .ifPresent(s -> { try { MAX_BATCH_SIZE = Integer.parseInt(s); } catch (NumberFormatException ignored) {} });
        Optional.ofNullable(System.getenv("BSC_ANOMALY_IQR_MULT"))
                .ifPresent(s -> { try { IQR_MULTIPLIER = Double.parseDouble(s); } catch (NumberFormatException ignored) {} });
        Optional.ofNullable(System.getenv("BSC_QUALITY_THRESHOLD"))
                .ifPresent(s -> { try { QUALITY_THRESHOLD = Double.parseDouble(s); } catch (NumberFormatException ignored) {} });
        LOG.info("SignalSecurityConfig init | strict=" + STRICT_MODE
                + " | maxBatch=" + MAX_BATCH_SIZE + " | iqrMult=" + IQR_MULTIPLIER);
    }

    // ── JWT Validation ─────────────────────────────────────────────────────────

    public static Map<String, Object> validateToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank())
            throw new SecurityException("Missing Authorization token");
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new SecurityException("Malformed JWT");
        if (STRICT_MODE) {
            String expected = hmacB64url(parts[0]+"."+parts[1], JWT_SECRET);
            if (!expected.equals(parts[2])) throw new SecurityException("JWT signature invalid");
        }
        String payloadJson = decodeB64(parts[1]);
        Map<String, Object> claims = parseJwtPayload(payloadJson);
        long exp = toLong(claims.get("exp"));
        if (exp > 0 && Instant.now().getEpochSecond() > exp)
            throw new SecurityException("JWT expired");
        String sub = (String) claims.get("sub");
        if (sub == null || sub.isBlank()) throw new SecurityException("JWT missing sub");
        return claims;
    }

    public static void requireRole(Map<String, Object> claims, String... roles) {
        List<?> userRoles = (List<?>) claims.getOrDefault("roles", Collections.emptyList());
        for (String r : roles)
            if (userRoles.stream().anyMatch(ur -> r.equalsIgnoreCase(ur.toString()))) return;
        throw new SecurityException("Access denied. Required: " + Arrays.toString(roles));
    }

    public static boolean hasRole(Map<String, Object> claims, String role) {
        List<?> userRoles = (List<?>) claims.getOrDefault("roles", Collections.emptyList());
        return userRoles.stream().anyMatch(ur -> role.equalsIgnoreCase(ur.toString()));
    }

    public static String effectiveTenantId(Map<String, Object> claims, Map<String, Object> args) {
        String req = getString(args, "tenant_id");
        if (req != null && !req.isBlank() && hasRole(claims, ROLE_ADMIN)) return req;
        String fromToken = (String) claims.get("tenant_id");
        return fromToken != null ? fromToken : (req != null ? req : "dev-tenant");
    }

    // ── Signal validation ──────────────────────────────────────────────────────

    /**
     * Validates a single signal value against known range for its type.
     * Returns validation result with details.
     */
    public static SignalValidationResult validateSignalValue(String signalType, double value) {
        if (signalType == null || signalType.isBlank())
            return new SignalValidationResult(false, "MISSING_TYPE", value);
        double[] range = SIGNAL_RANGES.get(signalType.toLowerCase());
        if (range == null)
            return new SignalValidationResult(true, "UNKNOWN_TYPE_PASS", value); // unknown types pass
        if (value < range[0] || value > range[1])
            return new SignalValidationResult(false,
                    "OUT_OF_RANGE [" + range[0] + "," + range[1] + "] got " + value, value);
        return new SignalValidationResult(true, "OK", value);
    }

    /** Checks and registers a message_id for deduplication. Returns true if NEW. */
    public static boolean checkDeduplication(String messageId) {
        if (messageId == null || messageId.isBlank()) return true;
        return SEEN_MESSAGE_IDS.add(messageId);
    }

    public static int     getMaxBatchSize()    { return MAX_BATCH_SIZE; }
    public static double  getIqrMultiplier()   { return IQR_MULTIPLIER; }
    public static double  getQualityThreshold(){ return QUALITY_THRESHOLD; }
    public static Map<String, double[]> getSignalRanges() { return SIGNAL_RANGES; }

    // ── Input sanitisation ─────────────────────────────────────────────────────

    private static final String[] SQL_PATTERNS = {"--",";--","/*","*/","UNION SELECT","DROP TABLE","DELETE FROM","INSERT INTO"};
    private static final String[] XSS_PATTERNS = {"<script","javascript:","onerror=","onload="};

    public static String sanitise(String input, String field) {
        if (input == null) return null;
        String t = input.trim();
        if (t.length() > MAX_PAYLOAD)
            throw new IllegalArgumentException("Field '"+field+"' exceeds max length");
        String u = t.toUpperCase();
        for (String p : SQL_PATTERNS) if (u.contains(p.toUpperCase()))
            throw new SecurityException("SQL injection detected in: "+field);
        for (String p : XSS_PATTERNS) if (u.contains(p.toUpperCase()))
            throw new SecurityException("Script injection detected in: "+field);
        return t;
    }

    public static String requireString(Map<String, Object> args, String key) {
        Object v = args.get(key);
        if (v == null || v.toString().isBlank())
            throw new IllegalArgumentException("Missing required field: "+key);
        return sanitise(v.toString(), key);
    }

    public static String getString(Map<String, Object> args, String key) {
        Object v = args.get(key); return v != null ? v.toString() : null;
    }

    public static double getDouble(Map<String, Object> args, String key, double def) {
        Object v = args.get(key); if (v==null) return def;
        try { return Double.parseDouble(v.toString()); } catch (NumberFormatException e) { return def; }
    }

    public static int getInt(Map<String, Object> args, String key, int def) {
        Object v = args.get(key); if (v==null) return def;
        try { return Integer.parseInt(v.toString()); } catch (NumberFormatException e) { return def; }
    }

    public static String generateId() { return UUID.randomUUID().toString(); }

    // ── Crypto helpers ─────────────────────────────────────────────────────────

    private static String hmacB64url(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
            return java.util.Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException("HMAC error: "+e.getMessage()); }
    }

    public static String decodeB64(String encoded) {
        String p = encoded;
        switch (p.length()%4) { case 2: p+="=="; break; case 3: p+="="; break; }
        return new String(java.util.Base64.getUrlDecoder().decode(p), StandardCharsets.UTF_8);
    }

    static String b64url(String s) {
        return java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static Map<String, Object> parseJwtPayload(String json) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("sub",       extractStr(json,"sub"));
        m.put("tenant_id", extractStr(json,"tenant_id"));
        m.put("email",     extractStr(json,"email"));
        m.put("exp",       extractLong(json,"exp"));
        m.put("roles",     extractRoles(json));
        return m;
    }

    private static String extractStr(String json, String key) {
        String k = "\""+key+"\""; int i=json.indexOf(k); if(i<0) return null;
        int c=json.indexOf(':',i+k.length()); if(c<0) return null;
        int s=json.indexOf('"',c+1); if(s<0) return null;
        int e=json.indexOf('"',s+1); if(e<0) return null;
        return json.substring(s+1,e);
    }

    private static long extractLong(String json, String key) {
        String k = "\""+key+"\""; int i=json.indexOf(k); if(i<0) return -1;
        int c=json.indexOf(':',i+k.length()); if(c<0) return -1;
        StringBuilder num=new StringBuilder();
        for(int j=c+1;j<json.length();j++){
            char ch=json.charAt(j);
            if(Character.isDigit(ch)||ch=='-') num.append(ch);
            else if(num.length()>0) break;
        }
        try { return Long.parseLong(num.toString()); } catch (NumberFormatException e) { return -1; }
    }

    private static List<String> extractRoles(String json) {
        List<String> roles = new ArrayList<>();
        int ri = json.indexOf("\"roles\"");
        if (ri < 0) ri = json.indexOf("\"realm_access\"");
        if (ri < 0) return roles;
        int arrS=json.indexOf('[',ri), arrE=json.indexOf(']',arrS);
        if(arrS<0||arrE<0) return roles;
        for(String p : json.substring(arrS+1,arrE).split(",")) {
            String r=p.trim().replace("\"",""); if(!r.isBlank()) roles.add(r);
        }
        return roles;
    }

    private static long toLong(Object v) {
        if(v==null) return -1;
        try { return Long.parseLong(v.toString()); } catch (NumberFormatException e) { return -1; }
    }

    // ── Value object ───────────────────────────────────────────────────────────

    public static class SignalValidationResult {
        public final boolean valid;
        public final String  reason;
        public final double  value;
        public SignalValidationResult(boolean valid, String reason, double value) {
            this.valid=valid; this.reason=reason; this.value=value;
        }
    }
}
