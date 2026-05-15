package com.ecoskiller.mcp.institute.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base interface for all CAT-05 agent tool implementations.
 *
 * <p>Each implementation maps to exactly one Antigravity agent .md file.
 * The {@link #execute} method receives the parsed {@code arguments} node from
 * the MCP {@code tools/call} request and returns a plain-text response string
 * that is wrapped into a {@code ContentBlock} by the dispatcher.</p>
 */
public interface AgentTool {

    /**
     * Execute the tool with the given arguments.
     *
     * @param args  parsed JSON arguments from the MCP tools/call request
     * @return      human-readable or structured text response
     * @throws Exception on validation or execution error
     */
    String execute(JsonNode args) throws Exception;

    // ── Shared helpers ────────────────────────────────────────────────────────

    default String getString(JsonNode args, String key) {
        return args.path(key).asText("");
    }

    default boolean getBool(JsonNode args, String key, boolean def) {
        JsonNode n = args.path(key);
        return n.isMissingNode() ? def : n.asBoolean(def);
    }

    default String ok(String action, String detail) {
        return String.format("""
            ✅  %s
            ─────────────────────────────────────
            %s
            """, action, detail);
    }

    default String queued(String action, String jobId) {
        return String.format("""
            ⏳  %s — queued
            ─────────────────────────────────────
            job_id : %s
            status : PENDING
            Use the same tool with action=get_status to poll progress.
            """, action, jobId);
    }

    default String dryRun(String action, String summary) {
        return String.format("""
            🔍  DRY RUN — %s
            ─────────────────────────────────────
            %s
            No changes committed.
            """, action, summary);
    }
}
