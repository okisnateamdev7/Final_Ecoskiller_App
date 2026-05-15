package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;

/**
 * Tool: cache_get
 * Retrieve a value from the Redis cache by namespace + key.
 * Returns the value and remaining TTL.
 */
public class CacheGetTool extends BaseTool {

    public CacheGetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }

    @Override public String name() { return "cache_get"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",   apiKeyProp());
        props.set("namespace", strProp("Cache namespace (e.g. candidate, assessment, config)"));
        props.set("key",       strProp("Cache key within the namespace (e.g. cand-123)"));
        return buildSchema(name(),
                "Retrieve a cached value from Redis. Returns the value and remaining TTL. " +
                "Returns null if key does not exist (cache miss).",
                props, "namespace", "key");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String ns  = requireString(args, "namespace");
        String k   = requireString(args, "key");
        String fqk = key(ns, k);

        try (Jedis j = conn()) {
            String value = j.get(fqk);
            long   ttl   = j.ttl(fqk);

            ObjectNode result = json.createObjectNode();
            if (value == null) {
                result.put("hit",       false);
                result.put("value",     (String) null);
                result.put("ttl_secs",  -2);
                result.put("message",   "cache miss");
            } else {
                result.put("hit",      true);
                result.put("value",    value);
                result.put("ttl_secs", ttl);
                result.put("message",  "cache hit");
            }
            result.put("key", fqk);
            return ToolResult.ok(json.writeValueAsString(result));
        }
    }
}
