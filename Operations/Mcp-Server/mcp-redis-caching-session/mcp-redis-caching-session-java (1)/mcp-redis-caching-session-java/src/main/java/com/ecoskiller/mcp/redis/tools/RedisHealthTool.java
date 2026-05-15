package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Tool: redis_health
 *
 * Comprehensive health and performance snapshot for the Redis cluster.
 * Maps to the metrics described in spec section 16 "Operational Monitoring & Management".
 *
 * Metrics returned:
 *  - Ping latency (ms)
 *  - Memory: used_memory, max_memory, fragmentation_ratio
 *  - Connections: connected_clients, blocked_clients
 *  - Stats: keyspace_hits, keyspace_misses, hit_rate
 *  - Evictions: evicted_keys
 *  - Replication: role, replication_lag
 *  - Server: redis_version, uptime_seconds
 *  - Alerts: pre-computed threshold checks (memory >80%, hit_rate <90%)
 */
public class RedisHealthTool extends BaseTool {

    public RedisHealthTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "redis_health"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key", apiKeyProp());
        props.set("section", enumProp(
                "Which INFO section to fetch (default: all)",
                "all", "memory", "stats", "replication", "clients", "server"));
        return buildSchema(name(),
                "Retrieve a comprehensive Redis health snapshot. Returns memory usage, hit rates, " +
                "eviction stats, replication lag, and pre-computed alert flags. " +
                "Maps to Ecoskiller spec section 16 monitoring requirements.",
                props);
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String section = optString(args, "section", "all");

        try (Jedis j = conn()) {
            // 1. Ping latency
            long pingStart = System.currentTimeMillis();
            String pong = j.ping();
            long pingMs = System.currentTimeMillis() - pingStart;

            // 2. INFO section
            String infoSection = "all".equals(section) ? "all" : section;
            String rawInfo = j.info(infoSection);
            Map<String, String> info = parseInfo(rawInfo);

            // 3. Keyspace info for hit rate
            String keyspaceRaw = "all".equals(section) ? rawInfo : j.info("all");
            Map<String, String> allInfo = parseInfo(keyspaceRaw);

            // Parse key metrics
            long   usedMemory     = parseLong(info, "used_memory", 0);
            long   maxMemory      = parseLong(info, "maxmemory", 0);
            double memFragRatio   = parseDouble(info, "mem_fragmentation_ratio", 1.0);
            long   connectedCli   = parseLong(info, "connected_clients", 0);
            long   blockedCli     = parseLong(info, "blocked_clients", 0);
            long   hits           = parseLong(allInfo, "keyspace_hits", 0);
            long   misses         = parseLong(allInfo, "keyspace_misses", 0);
            long   evictions      = parseLong(allInfo, "evicted_keys", 0);
            long   uptimeSecs     = parseLong(info, "uptime_in_seconds", 0);
            String role           = info.getOrDefault("role", "unknown");
            String redisVersion   = info.getOrDefault("redis_version", "unknown");
            long   replLag        = parseLong(info, "master_repl_offset", 0);
            String replState      = info.getOrDefault("master_link_status", "N/A");

            // Derived metrics
            double hitRate = (hits + misses) > 0
                             ? Math.round((double) hits / (hits + misses) * 10000.0) / 100.0
                             : 100.0;

            double memUsagePct = (maxMemory > 0)
                                 ? Math.round((double) usedMemory / maxMemory * 10000.0) / 100.0
                                 : -1.0;

            // Alert evaluation (per spec section 16)
            ObjectNode alerts = json.createObjectNode();
            alerts.put("memory_high",       memUsagePct > 80.0);
            alerts.put("hit_rate_low",       hitRate < 90.0);
            alerts.put("eviction_high",      evictions > 0);
            alerts.put("replication_lagging", "down".equals(replState));
            alerts.put("latency_high",       pingMs > 10);

            boolean healthy = !alerts.path("memory_high").asBoolean()
                           && !alerts.path("hit_rate_low").asBoolean()
                           && !alerts.path("replication_lagging").asBoolean()
                           && !alerts.path("latency_high").asBoolean();

            // Build response
            ObjectNode r = json.createObjectNode();
            r.put("healthy",      healthy);
            r.put("ping_ok",      "PONG".equals(pong));
            r.put("ping_ms",      pingMs);
            r.put("redis_version", redisVersion);
            r.put("role",         role);
            r.put("uptime_secs",  uptimeSecs);

            ObjectNode memory = json.createObjectNode();
            memory.put("used_bytes",          usedMemory);
            memory.put("used_human",          info.getOrDefault("used_memory_human", "?"));
            memory.put("max_bytes",           maxMemory);
            memory.put("usage_pct",           memUsagePct);
            memory.put("fragmentation_ratio", memFragRatio);
            r.set("memory", memory);

            ObjectNode clients = json.createObjectNode();
            clients.put("connected", connectedCli);
            clients.put("blocked",   blockedCli);
            r.set("clients", clients);

            ObjectNode stats = json.createObjectNode();
            stats.put("keyspace_hits",   hits);
            stats.put("keyspace_misses", misses);
            stats.put("hit_rate_pct",    hitRate);
            stats.put("evicted_keys",    evictions);
            r.set("stats", stats);

            ObjectNode replication = json.createObjectNode();
            replication.put("role",          role);
            replication.put("link_status",   replState);
            replication.put("repl_offset",   replLag);
            r.set("replication", replication);

            r.set("alerts", alerts);
            r.put("message", healthy ? "Redis is healthy" : "Redis has active alerts — check alerts section");

            return ToolResult.ok(json.writeValueAsString(r));
        }
    }

    /** Parse Redis INFO output into a key=value map. */
    private Map<String, String> parseInfo(String raw) {
        java.util.HashMap<String, String> map = new java.util.HashMap<>();
        for (String line : raw.split("\n")) {
            line = line.strip();
            if (line.startsWith("#") || line.isEmpty()) continue;
            int idx = line.indexOf(':');
            if (idx > 0) {
                map.put(line.substring(0, idx).strip(), line.substring(idx + 1).strip());
            }
        }
        return map;
    }

    private long parseLong(Map<String, String> m, String key, long def) {
        try { return Long.parseLong(m.getOrDefault(key, String.valueOf(def))); }
        catch (NumberFormatException e) { return def; }
    }

    private double parseDouble(Map<String, String> m, String key, double def) {
        try { return Double.parseDouble(m.getOrDefault(key, String.valueOf(def))); }
        catch (NumberFormatException e) { return def; }
    }
}
