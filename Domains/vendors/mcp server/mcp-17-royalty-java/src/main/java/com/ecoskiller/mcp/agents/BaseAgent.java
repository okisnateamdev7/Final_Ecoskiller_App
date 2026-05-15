package com.ecoskiller.mcp.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base abstract class for CAT-17 agents
 */
public abstract class BaseAgent implements Agent {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected final Logger logger;
    
    public BaseAgent() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public JsonNode execute(String toolName, JsonNode arguments) throws Exception {
        logger.debug("🔧 {} executing: {}", getAgentName(), toolName);
        
        try {
            return executeToolLogic(toolName, arguments);
        } catch (Exception e) {
            logger.error("❌ {} tool {} failed: {}", getAgentName(), toolName, e.getMessage());
            throw e;
        }
    }

    /**
     * Subclasses implement specific tool logic here
     */
    protected abstract JsonNode executeToolLogic(String toolName, JsonNode arguments) throws Exception;

    /**
     * Helper: Create success response
     */
    protected ObjectNode createResponse(String status, JsonNode data) {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("status", status);
        response.set("data", data);
        response.put("timestamp", System.currentTimeMillis());
        response.put("agent", getAgentName());
        return response;
    }

    /**
     * Helper: Create error response
     */
    protected ObjectNode createErrorResponse(String message) {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("status", "error");
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        response.put("agent", getAgentName());
        return response;
    }

    /**
     * Helper: Get string parameter from arguments
     */
    protected String getStringParam(JsonNode arguments, String paramName, String defaultValue) {
        JsonNode value = arguments.path(paramName);
        return value.isMissingNode() ? defaultValue : value.asText();
    }

    /**
     * Helper: Get double parameter from arguments
     */
    protected double getDoubleParam(JsonNode arguments, String paramName, double defaultValue) {
        JsonNode value = arguments.path(paramName);
        return value.isMissingNode() ? defaultValue : value.asDouble();
    }

    /**
     * Helper: Get long parameter from arguments
     */
    protected long getLongParam(JsonNode arguments, String paramName, long defaultValue) {
        JsonNode value = arguments.path(paramName);
        return value.isMissingNode() ? defaultValue : value.asLong();
    }
}
