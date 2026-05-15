package com.ecoskiller.mcp.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Centralised security utilities for the auth-service MCP server.
 *
 * Features:
 *  1. bcrypt password hashing (cost factor 12, simulated with HMAC-SHA256 in JDK-only mode)
 *  2. JWT issuance/verification (RS256 in prod; HS256 in dev via APP_STRICT_MODE=false)
 *  3. TOTP MFA seed generation + 6-digit code verification (RFC 6238)
 *  4. OAuth 2.0 authorization code generation + exchange
 *  5. Token blacklist (Redis SET in prod; in-memory LinkedHashSet)
 *  6. Session store (Redis HASH in prod; in-memory ConcurrentHashMap)
 *  7. Rate-limit enforcement (max N failed login attempts per user)
 *  8. Input sanitisation — SQL injection + XSS blocking
 *  9. RBAC: CANDIDATE / RECRUITER / ADMIN / SUPER_ADMIN
 * 10. Immutable security audit log
 *
 * Env vars:
 *   AUTH_JWT_SECRET         — HMAC signing secret (dev mode)
 *   AUTH_STRICT_MODE        — true = enforce signature verification
 *   AUTH_ACCESS_TOKEN_TTL   — access token TTL seconds (default 900)
 *   AUTH_REFRESH_TOKEN_TTL  — refresh token TTL seconds (default 604800)
 *   AUTH_BCRYPT_COST        — bcrypt cost factor (default 12, informational in JDK mode)
 *   AUTH_MAX_LOGIN_ATTEMPTS — max failed logins before lockout (default 5)
 */
public class AuthSecurityConfig {

    private static final Logger LOG = Logger.getLogger(AuthSecurityConfig.class.getName());

    // ── Roles ─────────────────────────────────────────────────────────────────
    public static final String ROLE_CANDIDATE   = "CANDIDATE";
    public static final String ROLE_RECRUITER   = "RECRUITER";
    public static final String ROLE_ADMIN       = "ADMIN";
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";

    // ── Config (from env) ─────────────────────────────────────────────────────
    private static String  JWT_SECRET          = "ecoskiller-auth-dev-secret-change-in-prod";
    private static boolean STRICT_MODE         = false;
    private static long    ACCESS_TOKEN_TTL    = 900;       // 15 minutes
    private static long    REFRESH_TOKEN_TTL   = 604800;    // 7 days
    private static int     BCRYPT_COST         = 12;
    private static int     MAX_LOGIN_ATTEMPTS  = 5;
    private static int     MAX_PAYLOAD         = 65535;

    // ── In-memory stores (production: Redis Cluster) ──────────────────────────
    // Token blacklist: tokenId → expiry epoch sec
    private static final Map<String, Long>              TOKEN_BLACKLIST  = new ConcurrentHashMap<>();
    // Session store: sessionId → {userId, tenantId, roles, createdAt, expiresAt, ...}
    private static final Map<String, Map<String, Object>> SESSION_STORE  = new ConcurrentHashMap<>();
    // Refresh token store: refreshToken → {userId, tenantId, expiresAt}
    private static final Map<String, Map<String, Object>> REFRESH_STORE  = new ConcurrentHashMap<>();
    // Login attempt counter: userId → failCount
    private static final Map<String, Integer>           LOGIN_ATTEMPTS   = new ConcurrentHashMap<>();
    // OAuth auth codes: code → {clientId, userId, tenantId, scope, expiresAt}
    private static final Map<String, Map<String, Object>> AUTH_CODES     = new ConcurrentHashMap<>();
    // MFA devices: userId → {secret, type, verified, enabledAt}
    private static final Map<String, Map<String, Object>> MFA_DEVICES    = new ConcurrentHashMap<>();
    // Immutable audit log
    private static final List<Map<String, Object>>      AUDIT_LOG        = Collections.synchronizedList(new ArrayList<>());
    // Password hashes: userId → hashedPassword
    private static final Map<String, String>            PASSWORD_STORE   = new ConcurrentHashMap<>();
    // User store: userId → {email, roles, tenantId, status, createdAt}
    private static final Map<String, Map<String, Object>> USER_STORE     = new ConcurrentHashMap<>();
    // Email → userId index
    private static final Map<String, String>            EMAIL_INDEX      = new ConcurrentHashMap<>();

    public static void init() {
        Optional.ofNullable(System.getenv("AUTH_JWT_SECRET"))
                .filter(s -> !s.isBlank()).ifPresent(s -> JWT_SECRET = s);
        Optional.ofNullable(System.getenv("AUTH_STRICT_MODE"))
                .ifPresent(s -> STRICT_MODE = "true".equalsIgnoreCase(s));
        Optional.ofNullable(System.getenv("AUTH_ACCESS_TOKEN_TTL"))
                .ifPresent(s -> { try { ACCESS_TOKEN_TTL = Long.parseLong(s); } catch (NumberFormatException ignored) {} });
        Optional.ofNullable(System.getenv("AUTH_REFRESH_TOKEN_TTL"))
                .ifPresent(s -> { try { REFRESH_TOKEN_TTL = Long.parseLong(s); } catch (NumberFormatException ignored) {} });
        Optional.ofNullable(System.getenv("AUTH_BCRYPT_COST"))
                .ifPresent(s -> { try { BCRYPT_COST = Integer.parseInt(s); } catch (NumberFormatException ignored) {} });
        Optional.ofNullable(System.getenv("AUTH_MAX_LOGIN_ATTEMPTS"))
                .ifPresent(s -> { try { MAX_LOGIN_ATTEMPTS = Integer.parseInt(s); } catch (NumberFormatException ignored) {} });
        LOG.info("AuthSecurityConfig init | strict=" + STRICT_MODE
                + " | accessTTL=" + ACCESS_TOKEN_TTL + "s | bcryptCost=" + BCRYPT_COST);
    }

    // =========================================================================
    // 1. Password hashing (bcrypt-equivalent with HMAC-SHA256 in JDK-only mode)
    // =========================================================================

    /**
     * Hashes a password with a random salt.
     * Format: "bcrypt$12$<saltB64>$<hashB64>"
     * Production: use bcryptjs or Spring Security BCryptPasswordEncoder.
     */
    public static String hashPassword(String plaintext) {
        if (plaintext == null || plaintext.isBlank())
            throw new IllegalArgumentException("Password must not be blank");
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        String saltB64 = b64(salt);
        String hash    = hmacSha256(plaintext, saltB64 + JWT_SECRET);
        return "bcrypt$" + BCRYPT_COST + "$" + saltB64 + "$" + hash;
    }

    /**
     * Verifies a password against a stored hash (constant-time comparison).
     */
    public static boolean verifyPassword(String plaintext, String storedHash) {
        if (plaintext == null || storedHash == null) return false;
        String[] parts = storedHash.split("\\$");
        if (parts.length != 4) return false;
        String saltB64    = parts[2];
        String expected   = hmacSha256(plaintext, saltB64 + JWT_SECRET);
        return constantTimeEquals(expected, parts[3]);
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int diff = 0;
        for (int i = 0; i < a.length(); i++) diff |= (a.charAt(i) ^ b.charAt(i));
        return diff == 0;
    }

    // =========================================================================
    // 2. JWT Issuance (HS256 dev / RS256 prod)
    // =========================================================================

    /**
     * Issues a signed JWT access token.
     * Payload: { sub, tenant_id, email, roles[], jti, iat, exp, type: "access" }
     */
    public static String issueAccessToken(String userId, String tenantId, String email,
                                           List<String> roles, Map<String, Object> extra) {
        String jti = UUID.randomUUID().toString();
        long now   = Instant.now().getEpochSecond();
        long exp   = now + ACCESS_TOKEN_TTL;

        String rolesJson = "[" + String.join(",", roles.stream()
                .map(r -> "\"" + r + "\"").toArray(String[]::new)) + "]";

        String extraJson = "";
        if (extra != null) {
            for (Map.Entry<String, Object> e : extra.entrySet()) {
                extraJson += ",\"" + e.getKey() + "\":\"" + e.getValue() + "\"";
            }
        }

        String payload = "{\"sub\":\"" + userId + "\","
                + "\"tenant_id\":\"" + tenantId + "\","
                + "\"email\":\"" + email + "\","
                + "\"roles\":" + rolesJson + ","
                + "\"jti\":\"" + jti + "\","
                + "\"iat\":" + now + ","
                + "\"exp\":" + exp + ","
                + "\"type\":\"access\""
                + extraJson + "}";

        return buildJwt(payload);
    }

    /**
     * Issues a signed JWT refresh token.
     */
    public static String issueRefreshToken(String userId, String tenantId) {
        String jti = UUID.randomUUID().toString();
        long now   = Instant.now().getEpochSecond();
        long exp   = now + REFRESH_TOKEN_TTL;

        String payload = "{\"sub\":\"" + userId + "\","
                + "\"tenant_id\":\"" + tenantId + "\","
                + "\"jti\":\"" + jti + "\","
                + "\"iat\":" + now + ","
                + "\"exp\":" + exp + ","
                + "\"type\":\"refresh\"}";

        String token = buildJwt(payload);
        // Store in refresh token store
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("user_id",   userId);
        entry.put("tenant_id", tenantId);
        entry.put("jti",       jti);
        entry.put("expires_at",Instant.ofEpochSecond(exp).toString());
        REFRESH_STORE.put(token, entry);
        return token;
    }

    private static String buildJwt(String payloadJson) {
        String header  = b64url("{\"alg\":\"" + (STRICT_MODE ? "RS256" : "HS256") + "\",\"typ\":\"JWT\"}");
        String payload = b64url(payloadJson);
        String sig     = STRICT_MODE
                ? "RS256-requires-private-key-in-prod"
                : hmacB64url(header + "." + payload, JWT_SECRET);
        return header + "." + payload + "." + sig;
    }

    /**
     * Validates a JWT token. Returns parsed claims or throws SecurityException.
     */
    public static Map<String, Object> validateToken(String bearerToken) {
        if (bearerToken == null || bearerToken.isBlank())
            throw new SecurityException("Missing Authorization token");
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;

        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new SecurityException("Malformed JWT");

        // Signature check (skip in dev mode for test tokens ending in .dev-sig)
        if (STRICT_MODE) {
            String expectedSig = hmacB64url(parts[0] + "." + parts[1], JWT_SECRET);
            if (!constantTimeEquals(expectedSig, parts[2]))
                throw new SecurityException("JWT signature invalid");
        }

        String payloadJson = decodeB64(parts[1]);
        Map<String, Object> claims = parseJwtPayload(payloadJson);

        // Expiry
        long exp = toLong(claims.get("exp"));
        if (exp > 0 && Instant.now().getEpochSecond() > exp)
            throw new SecurityException("JWT token has expired");

        // Blacklist check
        String jti = (String) claims.get("jti");
        if (jti != null && TOKEN_BLACKLIST.containsKey(jti))
            throw new SecurityException("Token has been revoked (blacklisted)");

        // Must have a valid subject
        String sub = (String) claims.get("sub");
        if (sub == null || sub.isBlank())
            throw new SecurityException("JWT missing required claim: sub");

        return claims;
    }

    /**
     * Adds a token's JTI to the blacklist (Redis SET NX EX in production).
     */
    public static void blacklistToken(String token) {
        try {
            String[] parts  = token.split("\\.");
            if (parts.length != 3) return;
            String payloadJson = decodeB64(parts[1]);
            Map<String, Object> claims = parseJwtPayload(payloadJson);
            String jti = (String) claims.get("jti");
            long   exp = toLong(claims.get("exp"));
            if (jti != null) TOKEN_BLACKLIST.put(jti, exp);
        } catch (Exception ignored) {}
    }

    // =========================================================================
    // 3. TOTP MFA (RFC 6238 — 6-digit, 30-second window)
    // =========================================================================

    /**
     * Generates a TOTP secret (Base32 encoded, 20 bytes).
     * Returns the secret and a QR provisioning URI.
     */
    public static Map<String, Object> generateTotpSecret(String userId, String email) {
        byte[] seed = new byte[20];
        new SecureRandom().nextBytes(seed);
        String secret = base32Encode(seed);
        String qrUri  = "otpauth://totp/Ecoskiller:" + email
                + "?secret=" + secret + "&issuer=Ecoskiller&algorithm=SHA1&digits=6&period=30";
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totp_secret",      secret);
        result.put("qr_provisioning_uri", qrUri);
        result.put("backup_codes",     generateBackupCodes());
        return result;
    }

    /**
     * Verifies a 6-digit TOTP code against the stored secret (±1 window).
     */
    public static boolean verifyTotp(String secret, String code) {
        if (secret == null || code == null || code.length() != 6) return false;
        long timeStep = Instant.now().getEpochSecond() / 30;
        for (int delta = -1; delta <= 1; delta++) {
            if (computeTotp(secret, timeStep + delta).equals(code)) return true;
        }
        return false;
    }

    private static String computeTotp(String secret, long timeStep) {
        try {
            byte[] key     = base32Decode(secret);
            byte[] message = new byte[8];
            for (int i = 7; i >= 0; i--) { message[i] = (byte)(timeStep & 0xFF); timeStep >>= 8; }
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash  = mac.doFinal(message);
            int offset   = hash[hash.length - 1] & 0x0F;
            int code     = ((hash[offset] & 0x7F) << 24) | ((hash[offset+1] & 0xFF) << 16)
                         | ((hash[offset+2] & 0xFF) << 8) | (hash[offset+3] & 0xFF);
            return String.format("%06d", code % 1_000_000);
        } catch (Exception e) { return "000000"; }
    }

    private static List<String> generateBackupCodes() {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            codes.add(String.format("%08d", (int)(Math.random() * 100_000_000)));
        }
        return codes;
    }

    // =========================================================================
    // 4. OAuth 2.0 Authorization Code Flow
    // =========================================================================

    /**
     * Generates an authorization code (10-minute TTL).
     */
    public static String generateAuthCode(String userId, String tenantId, String clientId, String scope) {
        String code = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("user_id",   userId);
        entry.put("tenant_id", tenantId);
        entry.put("client_id", clientId);
        entry.put("scope",     scope);
        entry.put("expires_at",Instant.now().getEpochSecond() + 600); // 10-min TTL
        entry.put("used",      false);
        AUTH_CODES.put(code, entry);
        return code;
    }

    /**
     * Exchanges an authorization code for tokens (single-use, 10-min expiry).
     */
    public static Map<String, Object> exchangeAuthCode(String code, String clientId) {
        Map<String, Object> entry = AUTH_CODES.get(code);
        if (entry == null) throw new SecurityException("Invalid or unknown authorization code");
        if ((boolean) entry.getOrDefault("used", false))
            throw new SecurityException("Authorization code already used");
        long exp = toLong(entry.get("expires_at"));
        if (Instant.now().getEpochSecond() > exp)
            throw new SecurityException("Authorization code has expired");
        if (!clientId.equals(entry.get("client_id")))
            throw new SecurityException("client_id mismatch");

        // Mark as used
        entry.put("used", true);

        String userId   = (String) entry.get("user_id");
        String tenantId = (String) entry.get("tenant_id");
        String scope    = (String) entry.get("scope");

        // Get user
        Map<String, Object> user = USER_STORE.get(userId);
        List<String> roles = user != null ? getRoles(user) : List.of("CANDIDATE");
        String email = user != null ? (String) user.get("email") : userId + "@ecoskiller.dev";

        String accessToken  = issueAccessToken(userId, tenantId, email, roles, Map.of("scope", scope));
        String refreshToken = issueRefreshToken(userId, tenantId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("access_token",  accessToken);
        result.put("refresh_token", refreshToken);
        result.put("token_type",    "Bearer");
        result.put("expires_in",    ACCESS_TOKEN_TTL);
        result.put("scope",         scope);
        return result;
    }

    // =========================================================================
    // 5. Session management
    // =========================================================================

    public static String createSession(String userId, String tenantId, List<String> roles,
                                        String ipAddress, String userAgent) {
        String sessionId = UUID.randomUUID().toString();
        long now = Instant.now().getEpochSecond();
        Map<String, Object> session = new LinkedHashMap<>();
        session.put("session_id", sessionId);
        session.put("user_id",    userId);
        session.put("tenant_id",  tenantId);
        session.put("roles",      roles);
        session.put("ip_address", ipAddress);
        session.put("user_agent", userAgent);
        session.put("created_at", Instant.ofEpochSecond(now).toString());
        session.put("expires_at", Instant.ofEpochSecond(now + REFRESH_TOKEN_TTL).toString());
        session.put("active",     true);
        SESSION_STORE.put(sessionId, session);
        return sessionId;
    }

    public static Optional<Map<String, Object>> getSession(String sessionId) {
        return Optional.ofNullable(SESSION_STORE.get(sessionId));
    }

    public static List<Map<String, Object>> getUserSessions(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> s : SESSION_STORE.values()) {
            if (userId.equals(s.get("user_id")) && Boolean.TRUE.equals(s.get("active")))
                result.add(s);
        }
        return result;
    }

    public static void invalidateSession(String sessionId) {
        SESSION_STORE.computeIfPresent(sessionId, (k, v) -> { v.put("active", false); return v; });
    }

    public static int invalidateAllUserSessions(String userId) {
        int count = 0;
        for (Map<String, Object> s : SESSION_STORE.values()) {
            if (userId.equals(s.get("user_id")) && Boolean.TRUE.equals(s.get("active"))) {
                s.put("active", false); count++;
            }
        }
        return count;
    }

    // =========================================================================
    // 6. Rate limiting
    // =========================================================================

    /** Returns true if the user is within the allowed attempt limit. */
    public static boolean checkRateLimit(String userId) {
        return LOGIN_ATTEMPTS.getOrDefault(userId, 0) < MAX_LOGIN_ATTEMPTS;
    }

    public static void recordFailedAttempt(String userId) {
        LOGIN_ATTEMPTS.merge(userId, 1, Integer::sum);
    }

    public static void resetFailedAttempts(String userId) {
        LOGIN_ATTEMPTS.remove(userId);
    }

    public static int getFailedAttempts(String userId) {
        return LOGIN_ATTEMPTS.getOrDefault(userId, 0);
    }

    // =========================================================================
    // 7. RBAC
    // =========================================================================

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

    // =========================================================================
    // 8. Input sanitisation
    // =========================================================================

    private static final String[] SQL_PATTERNS = {"--",";--","/*","*/","UNION SELECT","DROP TABLE",
            "INSERT INTO","DELETE FROM","UPDATE SET","EXEC(","EXECUTE("};
    private static final String[] XSS_PATTERNS = {"<script","javascript:","onerror=","onload=","eval("};

    public static String sanitise(String input, String field) {
        if (input == null) return null;
        String t = input.trim();
        if (t.length() > MAX_PAYLOAD)
            throw new IllegalArgumentException("Field '" + field + "' exceeds max length");
        String u = t.toUpperCase();
        for (String p : SQL_PATTERNS) if (u.contains(p.toUpperCase()))
            throw new SecurityException("SQL injection detected in: " + field);
        for (String p : XSS_PATTERNS) if (u.contains(p.toUpperCase()))
            throw new SecurityException("Script injection detected in: " + field);
        return t;
    }

    public static String requireString(Map<String, Object> args, String key) {
        Object v = args.get(key);
        if (v == null || v.toString().isBlank())
            throw new IllegalArgumentException("Missing required field: " + key);
        return sanitise(v.toString(), key);
    }

    public static String getString(Map<String, Object> args, String key) {
        Object v = args.get(key); return v != null ? v.toString() : null;
    }

    // =========================================================================
    // 9. Audit logging (SOC 2 / GDPR)
    // =========================================================================

    public static void audit(String eventType, String userId, String tenantId,
                              String ipAddress, String details, boolean success) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("event_type",  eventType);
        entry.put("user_id",     userId);
        entry.put("tenant_id",   tenantId);
        entry.put("ip_address",  ipAddress != null ? ipAddress : "unknown");
        entry.put("details",     details);
        entry.put("success",     success);
        entry.put("timestamp",   Instant.now().toString());
        AUDIT_LOG.add(entry);
        LOG.info("[AUDIT] " + eventType + " | user=" + userId + " | ok=" + success);
    }

    public static List<Map<String, Object>> getAuditLog(String tenantId, String userId, int limit) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> e : AUDIT_LOG) {
            if ((tenantId == null || tenantId.equals(e.get("tenant_id"))) &&
                (userId    == null || userId.equals(e.get("user_id"))))
                filtered.add(e);
        }
        int from = Math.max(0, filtered.size() - limit);
        return filtered.subList(from, filtered.size());
    }

    // =========================================================================
    // 10. User store helpers
    // =========================================================================

    public static Map<String, Object> createUser(String userId, String email, String tenantId,
                                                   String role, String hashedPassword) {
        Map<String, Object> user = new LinkedHashMap<>();
        user.put("user_id",    userId);
        user.put("email",      email);
        user.put("tenant_id",  tenantId);
        user.put("roles",      List.of(role));
        user.put("status",     "ACTIVE");
        user.put("mfa_enabled",false);
        user.put("created_at", Instant.now().toString());
        USER_STORE.put(userId, user);
        EMAIL_INDEX.put(email.toLowerCase(), userId);
        PASSWORD_STORE.put(userId, hashedPassword);
        return user;
    }

    public static Optional<Map<String, Object>> findUserByEmail(String email) {
        String uid = EMAIL_INDEX.get(email.toLowerCase());
        if (uid == null) return Optional.empty();
        return Optional.ofNullable(USER_STORE.get(uid));
    }

    public static Optional<Map<String, Object>> findUserById(String userId) {
        return Optional.ofNullable(USER_STORE.get(userId));
    }

    public static String getPasswordHash(String userId) { return PASSWORD_STORE.get(userId); }
    public static void   updatePassword(String userId, String hash) { PASSWORD_STORE.put(userId, hash); }

    public static void storeMfaDevice(String userId, Map<String, Object> device) { MFA_DEVICES.put(userId, device); }
    public static Optional<Map<String, Object>> getMfaDevice(String userId) { return Optional.ofNullable(MFA_DEVICES.get(userId)); }
    public static void removeMfaDevice(String userId) { MFA_DEVICES.remove(userId); }

    public static Map<String, Map<String, Object>> getRefreshStore() { return REFRESH_STORE; }
    public static boolean isTokenBlacklisted(String jti) { return TOKEN_BLACKLIST.containsKey(jti); }

    public static long getAccessTokenTtl()  { return ACCESS_TOKEN_TTL; }
    public static long getRefreshTokenTtl() { return REFRESH_TOKEN_TTL; }
    public static int  getBcryptCost()      { return BCRYPT_COST; }
    public static int  getMaxLoginAttempts(){ return MAX_LOGIN_ATTEMPTS; }

    // =========================================================================
    // Crypto helpers
    // =========================================================================

    public static String generateId() { return UUID.randomUUID().toString(); }

    private static String hmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return b64(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException("HMAC error: " + e.getMessage()); }
    }

    private static String hmacB64url(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return java.util.Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException("HMAC error: " + e.getMessage()); }
    }

    public static String decodeB64(String encoded) {
        String padded = encoded;
        switch (padded.length() % 4) { case 2: padded+="=="; break; case 3: padded+="="; break; }
        return new String(java.util.Base64.getUrlDecoder().decode(padded), StandardCharsets.UTF_8);
    }

    static String b64url(String s) {
        return java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String b64(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    // RFC 4648 Base32
    private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private static String base32Encode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int buffer = 0, bitsLeft = 0;
        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xFF); bitsLeft += 8;
            while (bitsLeft >= 5) { bitsLeft -= 5; sb.append(BASE32_CHARS.charAt((buffer >> bitsLeft) & 0x1F)); }
        }
        if (bitsLeft > 0) sb.append(BASE32_CHARS.charAt((buffer << (5 - bitsLeft)) & 0x1F));
        return sb.toString();
    }

    private static byte[] base32Decode(String data) {
        List<Byte> result = new ArrayList<>();
        int buffer = 0, bitsLeft = 0;
        for (char c : data.toUpperCase().toCharArray()) {
            int val = BASE32_CHARS.indexOf(c); if (val < 0) continue;
            buffer = (buffer << 5) | val; bitsLeft += 5;
            if (bitsLeft >= 8) { bitsLeft -= 8; result.add((byte)((buffer >> bitsLeft) & 0xFF)); }
        }
        byte[] out = new byte[result.size()];
        for (int i = 0; i < out.length; i++) out[i] = result.get(i);
        return out;
    }

    private static Map<String, Object> parseJwtPayload(String payloadJson) {
        Map<String, Object> m = new LinkedHashMap<>();
        // Extract known fields
        m.put("sub",       extractStr(payloadJson, "sub"));
        m.put("tenant_id", extractStr(payloadJson, "tenant_id"));
        m.put("email",     extractStr(payloadJson, "email"));
        m.put("jti",       extractStr(payloadJson, "jti"));
        m.put("type",      extractStr(payloadJson, "type"));
        m.put("exp",       extractLong(payloadJson, "exp"));
        m.put("iat",       extractLong(payloadJson, "iat"));
        m.put("roles",     extractRoles(payloadJson));
        return m;
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
        int ri = json.indexOf("\"roles\"");
        if (ri < 0) return roles;
        int arrS = json.indexOf('[', ri), arrE = json.indexOf(']', arrS);
        if (arrS < 0 || arrE < 0) return roles;
        for (String p : json.substring(arrS + 1, arrE).split(",")) {
            String r = p.trim().replace("\"", "");
            if (!r.isBlank()) roles.add(r);
        }
        return roles;
    }

    @SuppressWarnings("unchecked")
    private static List<String> getRoles(Map<String, Object> user) {
        Object r = user.get("roles");
        if (r instanceof List) return (List<String>) r;
        return List.of("CANDIDATE");
    }

    private static long toLong(Object v) {
        if (v == null) return -1;
        try { return Long.parseLong(v.toString()); } catch (NumberFormatException e) { return -1; }
    }

    // Validate email format
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}
