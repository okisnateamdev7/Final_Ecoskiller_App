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
        // CAT-17: Marketplace, Royalty & Compliance (18 Agents)
        agents.put("tax_compliance", new TaxComplianceAgent());
        agents.put("school_auto_creation", new SchoolAutoCreationAgent());
        agents.put("royalty_wallet", new RoyaltyWalletAgent());
        agents.put("royalty_system_unified", new RoyaltySystemUnifiedAgent());
        agents.put("royalty_escrow", new RoyaltyEscrowAgent());
        agents.put("royalty_distribution", new RoyaltyDistributionAgent());
        agents.put("royalty_calculation", new RoyaltyCalculationAgent());
        agents.put("revenue_ingestion", new RevenueIngestionAgent());
        agents.put("parent_dashboard", new ParentDashboardAgent());
        agents.put("market_demand_prediction", new MarketDemandPredictionAgent());
        agents.put("license_generation", new LicenseGenerationAgent());
        agents.put("idea_visibility_control", new IdeaVisibilityControlAgent());
        agents.put("idea_evaluation", new IdeaEvaluationAgent());
        agents.put("data_retention_policy", new DataRetentionPolicyAgent());
        agents.put("contract_lifecycle", new ContractLifecycleAgent());
        agents.put("competition_engine", new CompetitionEngineAgent());
        agents.put("business_matching", new BusinessMatchingAgent());
        agents.put("audit_verification", new AuditVerificationAgent());
        
        logger.info("✅ Initialized {} agents for CAT-17", agents.size());
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
        serverInfo.put("name", "mcp-17-royalty");
        serverInfo.put("version", "1.0.0");
        serverInfo.put("description", "EcoSkiller CAT-17 — Marketplace, Royalty & Compliance");
        
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

        // Parse tool name to agent mapping (format: agent_name/tool_name)
        String agentName = extractAgentName(toolName);
        Agent agent = agents.get(agentName);

        if (agent == null) {
            return buildErrorResponse(id, -32602, "Unknown agent: " + agentName);
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

    private String extractAgentName(String toolName) {
        // Simple extraction: assumes format like "tax_compliance" maps to "tax_compliance" agent
        // More complex mappings can be added as needed
        return toolName.split("/")[0];
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
