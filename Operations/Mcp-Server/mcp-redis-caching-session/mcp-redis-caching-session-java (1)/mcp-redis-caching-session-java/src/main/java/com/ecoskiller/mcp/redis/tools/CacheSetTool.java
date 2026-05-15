package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * Tool: cache_set
 * Store a value in the Redis cache with optional TTL.
 * Supports NX (only set if not exists) and XX (only set if exists) flags.
 */
public class CacheSetTool extends BaseTool {

    private static final long MAX_TTL = 86400 * 30L; // 30 days max

    public CacheSetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }

    @Override public String name() { return "cache_set"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",   apiKeyProp());
        props.set("namespace", strProp("Cache namespace (e.g. candidate, assessment, scoring)"));
        props.set("key",       strProp("Cache key within the namespace"));
        props.set("value",     strProp("Value to cache (string or JSON string)"));
        props.set("ttl_secs",  intProp("TTL in seconds (default: 3600 = 1 hour, max: 2592000 = 30 days)"));
        props.set("mode",      enumProp("Set mode: always (default), nx (only if absent), xx (only if present)",
                                        "always", "nx", "xx"));
        return buildSchema(name(),
                "Store a value in the Redis cache. Supports TTL and conditional set modes. " +
                "After a candidate profile is fetched from PostgreSQL, cache it here for sub-ms retrieval.",
                props, "namespace", "key", "value");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String ns    = requireString(args, "namespace");
        String k     = requireString(args, "key");
        String value = requireString(args, "value");
        long   ttl   = optLong(args, "ttl_secs", 3600L);
        String mode  = optString(args, "mode", "always");

        // Safety: cap TTL
        if (ttl < 1 || ttl > MAX_TTL) {
            ttl = 3600L;
        }

        String fqk = key(ns, k);

        SetParams params = new SetParams().ex(ttl);
        switch (mode) {
            case "nx" -> params.nx();
            case "xx" -> params.xx();
            // "always" — no extra flag
        }

        try (Jedis j = conn()) {
            String reply = j.set(fqk, value, params);

            ObjectNode result = json.createObjectNode();
            result.put("success",  reply != null && reply.equals("OK"));
            result.put("key",      fqk);
            result.put("ttl_secs", ttl);
            result.put("mode",     mode);
            result.put("reply",    reply != null ? reply : "nil");
            return ToolResult.ok(json.writeValueAsString(result));
        }
    }
}
