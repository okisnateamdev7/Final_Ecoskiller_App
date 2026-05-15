package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.UUID;

/**
 * Distributed lock tools — Redis-based mutex (simplified Redlock pattern).
 *
 * Key pattern:  ecoskiller:lock:{resource}
 * Value:        Unique lock token (UUID) — prevents accidental release by non-owner
 *
 * Ecoskiller use cases:
 *  - Prevent duplicate royalty payouts (lock on royalty:{calculation_id})
 *  - Serialise concurrent assessment submissions
 *  - Guard idempotent operations in the competition engine
 *  - Prevent double-billing in the marketplace
 *
 * Implementation notes:
 *  - SET key token NX EX ttl — atomic acquire (no SETNX + EXPIRE race)
 *  - Lua script for release — atomic check-and-delete (no token mismatch race)
 *  - Lock token is returned to caller — must supply on release
 *  - TTL is mandatory — locks auto-expire to prevent deadlocks
 */

// ─────────────────────────────────────────────────────────────────────────────
// lock_acquire
// ─────────────────────────────────────────────────────────────────────────────

class LockAcquireTool extends BaseTool {

    private static final int MAX_TTL = 300; // 5 minutes max lock duration

    LockAcquireTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "lock_acquire"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",   apiKeyProp());
        props.set("resource",  strProp("Resource name to lock (e.g. royalty:calc-123, assessment:submit:cand-456)"));
        props.set("ttl_secs",  intProp("Lock TTL in seconds — auto-expires to prevent deadlocks (default: 30, max: 300)"));
        props.set("retry",     intProp("Number of retry attempts if lock is held (default: 0 = no retry)"));
        props.set("retry_delay_ms", intProp("Milliseconds to wait between retries (default: 100)"));
        return buildSchema(name(),
                "Acquire a distributed lock on a named resource. Returns a lock_token — store this " +
                "and pass it to lock_release when done. Lock auto-expires after ttl_secs to prevent deadlocks. " +
                "Returns acquired=false if resource is already locked.",
                props, "resource");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String resource = validateKey(requireString(args, "resource"), "resource");
        int    ttl      = Math.min(optInt(args, "ttl_secs", 30), MAX_TTL);
        int    retries  = Math.min(optInt(args, "retry", 0), 10);
        long   retryMs  = Math.min(optLong(args, "retry_delay_ms", 100L), 2000L);

        String fqk   = key("lock", resource);
        String token = UUID.randomUUID().toString();

        try (Jedis j = conn()) {
            String reply = null;
            int attempt  = 0;

            do {
                reply = j.set(fqk, token, new SetParams().nx().ex(ttl));
                if ("OK".equals(reply)) break;

                if (attempt < retries) {
                    attempt++;
                    Thread.sleep(retryMs);
                } else {
                    break;
                }
            } while (true);

            boolean acquired = "OK".equals(reply);
            long    ttlRemaining = acquired ? ttl : j.ttl(fqk);

            ObjectNode r = json.createObjectNode();
            r.put("acquired",   acquired);
            r.put("resource",   resource);
            r.put("lock_token", acquired ? token : (String) null);
            r.put("ttl_secs",   ttlRemaining);
            r.put("attempts",   attempt + 1);
            r.put("message",    acquired
                    ? "lock acquired — release with lock_release + lock_token"
                    : "lock not acquired — resource held by another process (retry_after ~" + ttlRemaining + "s)");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// lock_release — atomic token-checked release via Lua
// ─────────────────────────────────────────────────────────────────────────────

class LockReleaseTool extends BaseTool {

    /**
     * Lua script: only delete the key if the stored value matches our token.
     * Prevents a process from accidentally releasing a lock it doesn't own
     * (e.g., after lock expiry + re-acquisition by another process).
     */
    private static final String LUA_RELEASE =
        "if redis.call('get', KEYS[1]) == ARGV[1] then\n" +
        "  return redis.call('del', KEYS[1])\n" +
        "else\n" +
        "  return 0\n" +
        "end";

    LockReleaseTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "lock_release"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",    apiKeyProp());
        props.set("resource",   strProp("Resource name (must match the value used in lock_acquire)"));
        props.set("lock_token", strProp("Lock token returned by lock_acquire — required to prevent accidental release"));
        return buildSchema(name(),
                "Release a distributed lock. Requires the lock_token from lock_acquire — prevents accidental " +
                "release of a lock owned by another process. Returns released=false if token doesn't match " +
                "(lock expired and re-acquired by someone else).",
                props, "resource", "lock_token");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String resource = validateKey(requireString(args, "resource"), "resource");
        String token    = requireString(args, "lock_token");

        String fqk = key("lock", resource);

        try (Jedis j = conn()) {
            Object result = j.eval(LUA_RELEASE, List.of(fqk), List.of(token));
            long released = result instanceof Long ? (Long) result : 0L;

            ObjectNode r = json.createObjectNode();
            r.put("released", released == 1);
            r.put("resource", resource);
            r.put("message",  released == 1
                    ? "lock released successfully"
                    : "lock not released — token mismatch (lock may have expired and been re-acquired)");
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}
