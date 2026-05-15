package com.ecoskiller.mcp.webrtc.agents;

import com.ecoskiller.mcp.webrtc.model.AgentResult;
import com.ecoskiller.mcp.webrtc.security.SecurityValidator;
import org.json.JSONObject;

/**
 * Abstract base for all WebRTC MCP agents.
 * Each agent exposes a tool definition (for tools/list) and an execute() method.
 */
public abstract class BaseAgent {

    protected final SecurityValidator security;

    protected BaseAgent(SecurityValidator security) {
        this.security = security;
    }

    /** Returns the MCP tool definition JSONObject (name, description, inputSchema). */
    public abstract JSONObject getToolDefinition();

    /** Executes the agent with validated arguments. May throw SecurityException. */
    public abstract AgentResult execute(JSONObject args) throws Exception;

    // ── Shared schema helpers ────────────────────────────────────────────

    protected JSONObject stringProp(String description) {
        return new JSONObject().put("type", "string").put("description", description);
    }

    protected JSONObject intProp(String description, int min, int max) {
        return new JSONObject()
            .put("type", "integer")
            .put("description", description)
            .put("minimum", min)
            .put("maximum", max);
    }

    protected JSONObject boolProp(String description) {
        return new JSONObject().put("type", "boolean").put("description", description);
    }

    protected JSONObject schema(JSONObject properties, String... required) {
        org.json.JSONArray req = new org.json.JSONArray();
        for (String r : required) req.put(r);
        return new JSONObject()
            .put("type", "object")
            .put("properties", properties)
            .put("required", req);
    }

    protected JSONObject tool(String name, String description, JSONObject inputSchema) {
        return new JSONObject()
            .put("name", name)
            .put("description", description)
            .put("inputSchema", inputSchema);
    }
}
