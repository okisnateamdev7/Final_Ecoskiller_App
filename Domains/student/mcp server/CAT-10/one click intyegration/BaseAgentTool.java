package com.ecoskiller.mcp.integrations.agents;

import com.ecoskiller.mcp.integrations.model.McpProtocol.ContentBlock;
import com.ecoskiller.mcp.integrations.model.McpProtocol.ToolCallResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Abstract base with JSON serialization helpers and error builders
 * shared across all 18 CAT-07 agents.
 */
public abstract class BaseAgentTool implements AgentTool {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    // ─── Success helpers ──────────────────────────────────────

    protected ToolCallResult success(String text) {
        return ToolCallResult.builder()
                .isError(false)
                .content(List.of(textBlock(text)))
                .build();
    }

    protected ToolCallResult success(Object payload) {
        try {
            String json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            return success(json);
        } catch (JsonProcessingException e) {
            return error("Serialization failed: " + e.getMessage());
        }
    }

    // ─── Error helpers ────────────────────────────────────────

    protected ToolCallResult error(String message) {
        log.warn("[{}] Tool error: {}", getName(), message);
        return ToolCallResult.builder()
                .isError(true)
                .content(List.of(textBlock("ERROR: " + message)))
                .build();
    }

    protected ToolCallResult missingParam(String paramName) {
        return error("Required parameter missing: '" + paramName + "'");
    }

    // ─── Argument helpers ─────────────────────────────────────

    protected String getString(Map<String, Object> args, String key) {
        Object v = args.get(key);
        return v != null ? v.toString() : null;
    }

    protected String getRequiredString(Map<String, Object> args, String key) throws IllegalArgumentException {
        String v = getString(args, key);
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException("Required parameter missing: " + key);
        }
        return v;
    }

    protected boolean getBoolean(Map<String, Object> args, String key, boolean defaultValue) {
        Object v = args.get(key);
        if (v == null) return defaultValue;
        if (v instanceof Boolean b) return b;
        return Boolean.parseBoolean(v.toString());
    }

    protected int getInt(Map<String, Object> args, String key, int defaultValue) {
        Object v = args.get(key);
        if (v == null) return defaultValue;
        if (v instanceof Number n) return n.intValue();
        try { return Integer.parseInt(v.toString()); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    // ─── Content block builders ───────────────────────────────

    protected ContentBlock textBlock(String text) {
        return ContentBlock.builder().type("text").text(text).build();
    }

    /**
     * Wrap execute() so any uncaught exception becomes a proper MCP error response
     * instead of an HTTP 500. Override doExecute() in subclasses.
     */
    @Override
    public final ToolCallResult execute(Map<String, Object> arguments) {
        try {
            log.info("[{}] Executing with args: {}", getName(), arguments);
            return doExecute(arguments != null ? arguments : Map.of());
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            log.error("[{}] Unexpected error", getName(), e);
            return error("Internal error in agent '" + getName() + "': " + e.getMessage());
        }
    }

    /** Implement your tool logic here — exceptions are caught by execute() */
    protected abstract ToolCallResult doExecute(Map<String, Object> arguments);
}
