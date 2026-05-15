package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.Instant;
import java.util.Map;

/**
 * Session management tools.
 *
 * Key pattern:  ecoskiller:session:{token}
 * Session data: stored as Redis hash fields (user_id, role, login_time, etc.)
 * Default TTL:  86400s (24 hours) — matches Keycloak JWT expiry
 *
 * Security:
 *  - Session tokens are accepted as-is — they are JWT/opaque tokens from Keycloak
 *  - No token generation here — Keycloak is the authority
 *  - Never log token values — only token prefixes for debugging
 */

// ─────────────────────────────────────────────────────────────────────────────
// session_create
// ─────────────────────────────────────────────────────────────────────────────

class SessionCreateTool extends BaseTool {

    private static final long SESSION_TTL = 86400L;     // 24 hours

    SessionCreateTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "session_create"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",    apiKeyProp());
        props.set("token",      strProp("JWT / opaque session token from Keycloak (used as Redis key)"));
        props.set("user_id",    strProp("Candidate or recruiter UUID from Keycloak"));
        props.set("role",       enumProp("User role", "candidate", "recruiter", "admin", "society_coordinator"));
        props.set("email",      strProp("User email address (stored for session context)"));
        props.set("tenant_id",  strProp("Tenant / organisation ID for multi-tenant isolation"));
        props.set("ttl_secs",   intProp("Session TTL in seconds (default: 86400 = 24 hours)"));
        return buildSchema(name(),
                "Create or overwrite a user session in Redis. Called after Keycloak login. " +
                "Stores session metadata as a Redis Hash for efficient field-level access.",
                props, "token", "user_id", "role");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String token    = requireString(args, "token");
        String userId   = requireString(args, "user_id");
        String role     = requireString(args, "role");
        String email    = optString(args, "email",     "");
        String tenantId = optString(args, "tenant_id", "default");
        long   ttl      = Math.min(optLong(args, "ttl_secs", SESSION_TTL), SESSION_TTL);

        String fqk = key("session", token);

        try (Jedis j = conn()) {
            // Use HSET to store all session fields atomically
            j.hset(fqk, Map.of(
                "user_id",    userId,
                "role",       role,
                "email",      email,
                "tenant_id",  tenantId,
                "login_time", Instant.now().toString(),
                "active",     "true"
            ));
            j.expire(fqk, ttl);

            ObjectNode r = json.createObjectNode();
            r.put("success",   true);
            r.put("user_id",   userId);
            r.put("role",      role);
            r.put("tenant_id", tenantId);
            r.put("ttl_secs",  ttl);
            r.put("expires_at", Instant.now().plusSeconds(ttl).toString());
            // Never echo the token back — just confirm creation
            r.put("message",   "session created");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// session_get
// ─────────────────────────────────────────────────────────────────────────────

class SessionGetTool extends BaseTool {

    SessionGetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "session_get"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("token",   strProp("Session token to look up"));
        return buildSchema(name(),
                "Retrieve session data from Redis by token. Returns all session fields and remaining TTL. " +
                "Returns {valid:false} if session does not exist or has expired.",
                props, "token");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String fqk = key("session", requireString(args, "token"));

        try (Jedis j = conn()) {
            Map<String, String> fields = j.hgetAll(fqk);
            long ttl = j.ttl(fqk);

            ObjectNode r = json.createObjectNode();
            if (fields == null || fields.isEmpty()) {
                r.put("valid",   false);
                r.put("message", "session not found or expired");
            } else {
                r.put("valid",      true);
                r.put("user_id",    fields.getOrDefault("user_id",   ""));
                r.put("role",       fields.getOrDefault("role",      ""));
                r.put("email",      fields.getOrDefault("email",     ""));
                r.put("tenant_id",  fields.getOrDefault("tenant_id", "default"));
                r.put("login_time", fields.getOrDefault("login_time",""));
                r.put("active",     "true".equals(fields.get("active")));
                r.put("ttl_secs",   ttl);
            }
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// session_refresh — extend TTL on active session (sliding expiry)
// ─────────────────────────────────────────────────────────────────────────────

class SessionRefreshTool extends BaseTool {

    SessionRefreshTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "session_refresh"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("token",   strProp("Session token to refresh"));
        props.set("ttl_secs",intProp("New TTL in seconds (default: 86400)"));
        return buildSchema(name(),
                "Extend a session's TTL (sliding expiry). Call on every authenticated request to " +
                "keep active users logged in without re-authentication.",
                props, "token");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String fqk = key("session", requireString(args, "token"));
        long   ttl  = Math.min(optLong(args, "ttl_secs", 86400L), 86400L);

        try (Jedis j = conn()) {
            long result = j.expire(fqk, ttl);
            ObjectNode r = json.createObjectNode();
            r.put("success",   result == 1);
            r.put("ttl_secs",  ttl);
            r.put("message",   result == 1 ? "session refreshed" : "session not found");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// session_destroy — logout / immediate invalidation
// ─────────────────────────────────────────────────────────────────────────────

class SessionDestroyTool extends BaseTool {

    SessionDestroyTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "session_destroy"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("token",   strProp("Session token to destroy (logout)"));
        return buildSchema(name(),
                "Immediately destroy a session (logout). Deletes the session key from Redis. " +
                "Call on explicit logout, password change, or security event.",
                props, "token");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String fqk = key("session", requireString(args, "token"));
        try (Jedis j = conn()) {
            long deleted = j.del(fqk);
            ObjectNode r = json.createObjectNode();
            r.put("destroyed", deleted > 0);
            r.put("message",   deleted > 0 ? "session destroyed" : "session not found");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}
