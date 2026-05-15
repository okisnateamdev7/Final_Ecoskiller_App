package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Rate limiting tools — sliding window counter backed by Redis INCR + EXPIRE.
 *
 * Key pattern:  ecoskiller:ratelimit:{scope}:{identifier}
 *
 * Ecoskiller use cases (from spec):
 *  - Assessment submission: max 5 per hour per candidate
 *  - Login attempts: max 10 per 15 minutes per IP
 *  - API calls: max 1000 per minute per tenant
 *  - DDoS protection on assessment endpoints
 *
 * Implementation: Lua script ensures atomic INCR + EXPIRE (no race condition).
 * This is the approach described in the technical spec section 3 "Rate Limiting (sliding window)".
 */

// ─────────────────────────────────────────────────────────────────────────────
// rate_limit_check — atomic increment and threshold check
// ─────────────────────────────────────────────────────────────────────────────

class RateLimitCheckTool extends BaseTool {

    /**
     * Lua script: atomically increment counter, set TTL on first increment,
     * and return [current_count, is_new_key].
     * This prevents the race condition between INCR and EXPIRE.
     */
    private static final String LUA_RATE_LIMIT =
        "local current = redis.call('INCR', KEYS[1])\n" +
        "if current == 1 then\n" +
        "  redis.call('EXPIRE', KEYS[1], ARGV[1])\n" +
        "  return {current, 1}\n" +
        "end\n" +
        "local ttl = redis.call('TTL', KEYS[1])\n" +
        "return {current, 0, ttl}";

    RateLimitCheckTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "rate_limit_check"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",    apiKeyProp());
        props.set("scope",      strProp("Rate limit scope (e.g. submit_assessment, login, api_call)"));
        props.set("identifier", strProp("Who to rate limit: user UUID, IP address, or tenant ID"));
        props.set("limit",      intProp("Maximum allowed requests in the window (e.g. 5)"));
        props.set("window_secs",intProp("Window duration in seconds (e.g. 3600 = 1 hour)"));
        return buildSchema(name(),
                "Check and increment a sliding window rate limit counter. Returns whether the request " +
                "is allowed or blocked. Uses a Lua script for atomic INCR + EXPIRE — no race conditions. " +
                "Returns 429 context when limit is exceeded.",
                props, "scope", "identifier", "limit", "window_secs");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String scope      = validateKey(requireString(args, "scope"),      "scope");
        String identifier = validateKey(requireString(args, "identifier"), "identifier");
        int    limit      = optInt(args, "limit",       10);
        int    windowSecs = optInt(args, "window_secs", 3600);

        // Clamp inputs
        if (limit < 1)       limit = 1;
        if (limit > 100_000) limit = 100_000;
        if (windowSecs < 1)  windowSecs = 1;

        String fqk = key("ratelimit", scope + ":" + identifier);

        try (Jedis j = conn()) {
            @SuppressWarnings("unchecked")
            List<Object> result = (List<Object>) j.eval(
                LUA_RATE_LIMIT,
                List.of(fqk),
                List.of(String.valueOf(windowSecs))
            );

            long current  = ((Number) result.get(0)).longValue();
            long ttlSecs  = result.size() > 2 ? ((Number) result.get(2)).longValue() : windowSecs;
            boolean allowed = current <= limit;

            ObjectNode r = json.createObjectNode();
            r.put("allowed",        allowed);
            r.put("current_count",  current);
            r.put("limit",          limit);
            r.put("remaining",      Math.max(0, limit - current));
            r.put("window_secs",    windowSecs);
            r.put("retry_after_secs", allowed ? 0 : ttlSecs);
            r.put("scope",          scope);
            r.put("identifier",     identifier);
            r.put("http_status",    allowed ? 200 : 429);
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// rate_limit_reset — clear a rate limit counter (admin action)
// ─────────────────────────────────────────────────────────────────────────────

class RateLimitResetTool extends BaseTool {

    RateLimitResetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "rate_limit_reset"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",    apiKeyProp());
        props.set("scope",      strProp("Rate limit scope to reset"));
        props.set("identifier", strProp("Specific identifier to reset (user UUID, IP, etc.)"));
        return buildSchema(name(),
                "Reset (clear) a rate limit counter for a specific identifier. " +
                "Admin tool — use when a user is incorrectly blocked or after manual review.",
                props, "scope", "identifier");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String scope      = validateKey(requireString(args, "scope"),      "scope");
        String identifier = validateKey(requireString(args, "identifier"), "identifier");
        String fqk        = key("ratelimit", scope + ":" + identifier);

        try (Jedis j = conn()) {
            long deleted = j.del(fqk);
            ObjectNode r = json.createObjectNode();
            r.put("reset",      deleted > 0);
            r.put("scope",      scope);
            r.put("identifier", identifier);
            r.put("message",    deleted > 0 ? "rate limit counter reset" : "counter not found");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}
