package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Base interface for all CAT-06 Corporate ERP agent tool implementations.
 */
public interface AgentTool {

    String execute(JsonNode args) throws Exception;

    default String getString(JsonNode args, String key) {
        return args.path(key).asText("");
    }

    default boolean getBool(JsonNode args, String key, boolean def) {
        JsonNode n = args.path(key);
        return n.isMissingNode() ? def : n.asBoolean(def);
    }

    default double getDouble(JsonNode args, String key) {
        return args.path(key).asDouble(0);
    }

    default int getInt(JsonNode args, String key, int def) {
        return args.path(key).asInt(def);
    }

    default String ok(String action, String detail) {
        return String.format("✅  %s%n────────────────────────────────────────%n%s", action, detail);
    }

    default String queued(String action, String jobId, String extra) {
        return String.format(
            "⏳  %s — queued%n────────────────────────────────────────%njob_id : %s%n%s%nUse action=get_status to poll.",
            action, jobId, extra);
    }

    default String dryRun(String action, String summary) {
        return String.format(
            "🔍  DRY RUN — %s%n────────────────────────────────────────%n%s%nNo changes committed.", action, summary);
    }

    default String sealed(String reason) {
        return "[ANTIGRAVITY_SEALED] " + reason;
    }
}
