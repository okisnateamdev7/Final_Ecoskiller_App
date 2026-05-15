package com.ecoskiller.mcp.economics.agents;

import com.ecoskiller.mcp.economics.model.McpProtocol.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/** Contract every CAT-08 agent must satisfy */
public interface AgentTool {
    String getName();
    String getDescription();
    InputSchema getInputSchema();
    ToolCallResult execute(Map<String, Object> arguments);
}

/** Shared helpers — extend this in every agent */
abstract class BaseAgentTool implements AgentTool {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    protected ToolCallResult success(String text) {
        return ToolCallResult.builder().isError(false)
                .content(List.of(ContentBlock.builder().type("text").text(text).build())).build();
    }

    protected ToolCallResult success(Object payload) {
        try {
            return success(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(payload));
        } catch (JsonProcessingException e) { return error("Serialization failed: " + e.getMessage()); }
    }

    protected ToolCallResult error(String message) {
        log.warn("[{}] {}", getName(), message);
        return ToolCallResult.builder().isError(true)
                .content(List.of(ContentBlock.builder().type("text").text("ERROR: " + message).build())).build();
    }

    protected ToolCallResult missingParam(String p) { return error("Required parameter missing: '" + p + "'"); }

    protected String str(Map<String, Object> a, String k) {
        Object v = a.get(k); return v != null ? v.toString() : null;
    }
    protected String req(Map<String, Object> a, String k) {
        String v = str(a, k);
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Required parameter missing: " + k);
        return v;
    }
    protected int intVal(Map<String, Object> a, String k, int def) {
        Object v = a.get(k);
        if (v == null) return def;
        if (v instanceof Number n) return n.intValue();
        try { return Integer.parseInt(v.toString()); } catch (NumberFormatException e) { return def; }
    }
    protected ContentBlock txt(String t) { return ContentBlock.builder().type("text").text(t).build(); }

    @Override
    public final ToolCallResult execute(Map<String, Object> arguments) {
        try {
            log.info("[{}] args={}", getName(), arguments);
            return doExecute(arguments != null ? arguments : Map.of());
        } catch (IllegalArgumentException e) { return error(e.getMessage());
        } catch (Exception e) {
            log.error("[{}] unexpected error", getName(), e);
            return error("Internal error: " + e.getMessage());
        }
    }

    protected abstract ToolCallResult doExecute(Map<String, Object> arguments);
}
