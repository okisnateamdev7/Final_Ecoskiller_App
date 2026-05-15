package com.ecoskiller.mcp.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ecoskiller.mcp.agents.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private final Map<String, Agent> agents;
    private final ToolRegistry toolRegistry;

    public MessageHandler() {
        this.agents = new HashMap<>();
        this.toolRegistry = new ToolRegistry();
        initializeAgents();
    }

    private void initializeAgents() {
        // CAT-18: Governance & Core Control (11 Agents)
        agents.put("analytics_connect", new AnalyticsConnectAgent());
        agents.put("automation_connect", new AutomationConnectAgent());
        agents.put("data_warehouse", new DataWarehouseAgent());
        agents.put("deployment_checklist", new DeploymentChecklistAgent());
        agents.put("digilocker", new DigilockAgent());
        agents.put("esign_connect", new ESignConnectAgent());
        agents.put("implementation_guide", new ImplementationGuideAgent());
        agents.put("llm_connect", new LLMConnectAgent());
        agents.put("marketing_connect", new MarketingConnectAgent());
        agents.put("payment_connect", new PaymentConnectAgent());
        agents.put("video_connect", new VideoConnectAgent());
        
        logger.info("✅ Initialized {} agents for CAT-18", agents.size());
    }

    public JsonNode handle(JsonNode request) {
        try {
            String method = request.path("method").asText();
            Object id = request.path("id").isMissingNode() ? null : request.path("id").asText();
            JsonNode params = request.path("params");

            logger.debug("📩 Request - method: {}, id: {}", method, id);

            return switch (method) {
                case "initialize" -> handleInitialize(id);
                case "tools/list" -> handleToolsList(id);
                case "tools/call" -> handleToolCall(id, params);
                case "ping" -> handlePing(id);
                default -> buildErrorResponse(id, -32601, "Method not found");
            };
        } catch (Exception e) {
            logger.error("❌ Handler error: {}", e.getMessage());
            return buildErrorResponse(null, -32603, "Internal error");
        }
    }

    private JsonNode handleInitialize(Object id) {
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        
        ObjectNode serverInfo = JsonNodeFactory.instance.objectNode();
        serverInfo.put("name", "mcp-18-core-control");
        serverInfo.put("version", "1.0.0");
        serverInfo.put("description", "EcoSkiller CAT-18 — Governance & Core Control");
        
        result.set("serverInfo", serverInfo);
        
        ObjectNode protocolVersion = JsonNodeFactory.instance.objectNode();
        protocolVersion.put("version", "2024-11-05");
        result.set("protocolVersion", protocolVersion);
        
        result.set("capabilities", JsonNodeFactory.instance.objectNode());
        
        return buildResponse(id, result);
    }

    private JsonNode handleToolsList(Object id) {
        return buildResponse(id, toolRegistry.getToolsList());
    }

    private JsonNode handleToolCall(Object id, JsonNode params) {
        String toolName = params.path("name").asText();
        JsonNode arguments = params.path("arguments");

        logger.info("🔨 Calling tool: {}", toolName);

        Agent agent = agents.get(toolName);

        if (agent == null) {
            return buildErrorResponse(id, -32602, "Unknown tool: " + toolName);
        }

        try {
            JsonNode result = agent.execute(toolName, arguments);
            return buildResponse(id, result);
        } catch (Exception e) {
            logger.error("❌ Agent execution failed: {}", e.getMessage());
            return buildErrorResponse(id, -32603, "Agent execution error: " + e.getMessage());
        }
    }

    private JsonNode handlePing(Object id) {
        ObjectNode result = JsonNodeFactory.instance.objectNode();
        result.put("status", "alive");
        result.put("timestamp", System.currentTimeMillis());
        return buildResponse(id, result);
    }

    private JsonNode buildResponse(Object id, JsonNode result) {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("jsonrpc", "2.0");
        response.set("result", result);
        
        if (id != null) {
            response.put("id", id.toString());
        }
        
        return response;
    }

    private JsonNode buildErrorResponse(Object id, int code, String message) {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("jsonrpc", "2.0");
        
        ObjectNode errorNode = JsonNodeFactory.instance.objectNode();
        errorNode.put("code", code);
        errorNode.put("message", message);
        
        response.set("error", errorNode);
        
        if (id != null) {
            response.put("id", id.toString());
        }
        
        return response;
    }
}
