package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;

/**
 * Atomic counter tools using Redis INCR / INCRBY / DECR / GET / DEL.
 *
 * Key pattern:  ecoskiller:counter:{scope}:{name}
 *
 * Ecoskiller use cases (from spec section 4 "Atomic Counter Operations"):
 *  - Total assessments completed: INCR assessments:completed:total
 *  - Candidates viewed per recruiter
 *  - Job applications received per job
 *  - Real-time dashboard metrics
 *
 * Atomic: Redis INCR is single-threaded — no race conditions even under
 * millions of concurrent requests.
 */

// ─────────────────────────────────────────────────────────────────────────────
// counter_increment
// ─────────────────────────────────────────────────────────────────────────────

class CounterIncrementTool extends BaseTool {

    CounterIncrementTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "counter_increment"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",  apiKeyProp());
        props.set("scope",    strProp("Counter scope (e.g. assessments, candidates, applications)"));
        props.set("name",     strProp("Counter name within scope (e.g. completed:total, viewed:recruiter-123)"));
        props.set("by",       intProp("Increment amount (default: 1, can be negative to decrement)"));
        props.set("ttl_secs", intProp("Optional TTL — if set, counter auto-expires after this many seconds"));
        return buildSchema(name(),
                "Atomically increment (or decrement) a named counter. Zero race conditions — Redis INCR " +
                "is atomic. Use for real-time metrics: assessments completed, candidates viewed, etc.",
                props, "scope", "name");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String scope = validateKey(requireString(args, "scope"), "scope");
        String name  = validateKey(requireString(args, "name"),  "name");
        long   by    = optLong(args, "by", 1L);
        long   ttl   = optLong(args, "ttl_secs", -1L);

        String fqk = key("counter", scope + ":" + name);

        try (Jedis j = conn()) {
            long newVal = (by == 1L) ? j.incr(fqk) : j.incrBy(fqk, by);
            if (ttl > 0) {
                // Only set TTL if key is brand new (TTL = -1 means no expiry set yet)
                if (j.ttl(fqk) == -1) {
                    j.expire(fqk, ttl);
                }
            }

            ObjectNode r = json.createObjectNode();
            r.put("scope",      scope);
            r.put("name",       name);
            r.put("value",      newVal);
            r.put("incremented_by", by);
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// counter_get
// ─────────────────────────────────────────────────────────────────────────────

class CounterGetTool extends BaseTool {

    CounterGetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "counter_get"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("scope",   strProp("Counter scope"));
        props.set("name",    strProp("Counter name within scope"));
        return buildSchema(name(),
                "Read the current value of an atomic counter without modifying it.",
                props, "scope", "name");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String scope = validateKey(requireString(args, "scope"), "scope");
        String name  = validateKey(requireString(args, "name"),  "name");
        String fqk   = key("counter", scope + ":" + name);

        try (Jedis j = conn()) {
            String raw = j.get(fqk);
            long   ttl = j.ttl(fqk);

            ObjectNode r = json.createObjectNode();
            r.put("scope",    scope);
            r.put("name",     name);
            r.put("value",    raw != null ? Long.parseLong(raw) : 0L);
            r.put("exists",   raw != null);
            r.put("ttl_secs", ttl);
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// counter_reset — delete a counter (set to 0 by deleting the key)
// ─────────────────────────────────────────────────────────────────────────────

class CounterResetTool extends BaseTool {

    CounterResetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "counter_reset"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("scope",   strProp("Counter scope"));
        props.set("name",    strProp("Counter name to reset to 0"));
        return buildSchema(name(),
                "Reset a counter to 0 by deleting its Redis key. " +
                "Next increment will start from 1.",
                props, "scope", "name");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String scope = validateKey(requireString(args, "scope"), "scope");
        String name  = validateKey(requireString(args, "name"),  "name");
        String fqk   = key("counter", scope + ":" + name);

        try (Jedis j = conn()) {
            long deleted = j.del(fqk);
            ObjectNode r = json.createObjectNode();
            r.put("scope",   scope);
            r.put("name",    name);
            r.put("reset",   deleted > 0);
            r.put("message", deleted > 0 ? "counter reset" : "counter did not exist");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}
