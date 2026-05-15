package com.ecoskiller.mcp.redis.tools;

import com.ecoskiller.mcp.redis.config.RedisConfig;
import com.ecoskiller.mcp.redis.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.List;

/**
 * Leaderboard tools — backed by Redis Sorted Sets (ZADD / ZRANGE / ZRANK).
 *
 * Key pattern:  ecoskiller:leaderboard:{leaderboard_id}
 * Data:         Sorted Set — member=candidate_uuid, score=float score
 *
 * Ecoskiller use cases (from spec):
 *  - Assessment leaderboards: ranked by score
 *  - Hiring performance: recruiter completion rates
 *  - Competition engine rankings
 */

// ─────────────────────────────────────────────────────────────────────────────
// leaderboard_add — ZADD
// ─────────────────────────────────────────────────────────────────────────────

class LeaderboardAddTool extends BaseTool {

    LeaderboardAddTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "leaderboard_add"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",         apiKeyProp());
        props.set("leaderboard_id",  strProp("Leaderboard identifier (e.g. assessment:123, competition:456)"));
        props.set("member",          strProp("Member identifier — candidate UUID or recruiter UUID"));
        props.set("score",           numProp("Numeric score (e.g. 85.5). Higher = better rank."));
        props.set("mode",            enumProp("Update mode: always (overwrite), nx (only if new), gt (update only if score is greater)",
                                              "always", "nx", "gt"));
        return buildSchema(name(),
                "Add or update a member's score in a leaderboard (Redis Sorted Set / ZADD). " +
                "Automatically maintains ranking. Called when a candidate completes an assessment.",
                props, "leaderboard_id", "member", "score");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String lbId   = validateKey(requireString(args, "leaderboard_id"), "leaderboard_id");
        String member = requireString(args, "member");
        double score  = args.path("score").asDouble();
        String mode   = optString(args, "mode", "always");

        String fqk = key("leaderboard", lbId);

        try (Jedis j = conn()) {
            redis.clients.jedis.params.ZAddParams params = new redis.clients.jedis.params.ZAddParams();
            switch (mode) {
                case "nx" -> params.nx();
                case "gt" -> params.gt();
                // "always" — no extra flag
            }

            long added = j.zadd(fqk, score, member, params);

            // Get the new rank (0-indexed, ascending — we want descending rank)
            Long rank = j.zrevrank(fqk, member);
            long total = j.zcard(fqk);

            ObjectNode r = json.createObjectNode();
            r.put("success",        true);
            r.put("leaderboard_id", lbId);
            r.put("member",         member);
            r.put("score",          score);
            r.put("rank",           rank != null ? rank + 1 : -1); // 1-indexed
            r.put("total_members",  total);
            r.put("is_new_entry",   added > 0);
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// leaderboard_get — ZREVRANGE with scores (top-N)
// ─────────────────────────────────────────────────────────────────────────────

class LeaderboardGetTool extends BaseTool {

    private static final int MAX_LIMIT = 1000;

    LeaderboardGetTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "leaderboard_get"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",        apiKeyProp());
        props.set("leaderboard_id", strProp("Leaderboard identifier"));
        props.set("limit",          intProp("Number of top entries to return (default: 100, max: 1000)"));
        props.set("offset",         intProp("Offset for pagination (default: 0)"));
        return buildSchema(name(),
                "Retrieve the top-N entries from a leaderboard, ordered by score descending. " +
                "Returns rank, member ID, and score for each entry.",
                props, "leaderboard_id");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String lbId  = validateKey(requireString(args, "leaderboard_id"), "leaderboard_id");
        int    limit  = Math.min(optInt(args, "limit", 100), MAX_LIMIT);
        int    offset = Math.max(optInt(args, "offset", 0), 0);

        String fqk = key("leaderboard", lbId);

        try (Jedis j = conn()) {
            List<Tuple> entries = j.zrevrangeWithScores(fqk, offset, (long)offset + limit - 1);
            long total = j.zcard(fqk);

            ArrayNode entriesNode = json.createArrayNode();
            int rank = offset + 1;
            for (Tuple t : entries) {
                ObjectNode e = json.createObjectNode();
                e.put("rank",   rank++);
                e.put("member", t.getElement());
                e.put("score",  t.getScore());
                entriesNode.add(e);
            }

            ObjectNode r = json.createObjectNode();
            r.put("leaderboard_id", lbId);
            r.put("total_members",  total);
            r.put("offset",         offset);
            r.put("limit",          limit);
            r.set("entries",        entriesNode);
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// leaderboard_rank — ZREVRANK: get a single member's rank + score
// ─────────────────────────────────────────────────────────────────────────────

class LeaderboardRankTool extends BaseTool {

    LeaderboardRankTool(RedisConfig cfg, AuditLogger audit) { super(cfg, audit); }
    @Override public String name() { return "leaderboard_rank"; }

    @Override
    public ObjectNode schema() {
        ObjectNode props = json.createObjectNode();
        props.set("api_key",        apiKeyProp());
        props.set("leaderboard_id", strProp("Leaderboard identifier"));
        props.set("member",         strProp("Member UUID to look up"));
        return buildSchema(name(),
                "Look up a specific member's rank and score in a leaderboard. " +
                "O(log N) — very fast even for large leaderboards.",
                props, "leaderboard_id", "member");
    }

    @Override
    public ToolResult execute(JsonNode args) throws Exception {
        String lbId  = validateKey(requireString(args, "leaderboard_id"), "leaderboard_id");
        String member = requireString(args, "member");
        String fqk   = key("leaderboard", lbId);

        try (Jedis j = conn()) {
            Long   rank  = j.zrevrank(fqk, member);
            Double score = j.zscore(fqk, member);
            long   total = j.zcard(fqk);

            ObjectNode r = json.createObjectNode();
            r.put("leaderboard_id", lbId);
            r.put("member",         member);
            r.put("found",          rank != null);
            r.put("rank",           rank != null ? rank + 1 : -1); // 1-indexed
            r.put("score",          score != null ? score : 0.0);
            r.put("total_members",  total);
            r.put("percentile",     rank != null && total > 0
                                    ? Math.round((1.0 - (double) rank / total) * 100.0)
                                    : -1);
            return ToolResult.ok(json.writeValueAsString(r));
        }
    }
}
