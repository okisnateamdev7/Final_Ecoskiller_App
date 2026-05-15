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
        // CAT-18: Complete Governance & Core Control System
        // 7 Categories x 7-8 agents each = 50+ agents
        
        // Category 1: Analytics & ERP (8 agents)
        registerAgent("clickhouse_metric_normalization", new GenericAgent("CLICKHOUSE_METRIC_NORMALIZATION", "ClickHouse metrics"));
        registerAgent("erp_go_report_integration", new GenericAgent("ERP_GO_REPORT_INTEGRATION", "ERP reporting"));
        registerAgent("phone_feature_vector_emission", new GenericAgent("PHONE_FEATURE_VECTOR_EMISSION", "Feature vectors"));
        registerAgent("attendance_behavior_analytics", new GenericAgent("ATTENDANCE_BEHAVIOR_ANALYTICS", "Attendance analysis"));
        registerAgent("enrollment_analytics", new GenericAgent("ENROLLMENT_ANALYTICS", "Enrollment metrics"));
        registerAgent("analytics_connect", new GenericAgent("ANALYTICS_CONNECT", "Analytics integration"));
        registerAgent("data_warehouse_analytics", new GenericAgent("DATA_WAREHOUSE_ANALYTICS", "Data warehouse ops"));
        registerAgent("reporting_engine", new GenericAgent("REPORTING_ENGINE", "Report generation"));
        
        // Category 2: DevOps & Environment (8 agents)
        registerAgent("multi_environment_config", new GenericAgent("MULTI_ENV_CONFIG", "Environment config"));
        registerAgent("phone_backup_restore", new GenericAgent("PHONE_BACKUP_RESTORE", "Backup/restore"));
        registerAgent("phone_end_to_end_trace", new GenericAgent("PHONE_E2E_TRACE", "Tracing"));
        registerAgent("phone_external_webhook", new GenericAgent("PHONE_EXTERNAL_WEBHOOK", "Webhooks"));
        registerAgent("phone_infra_health", new GenericAgent("PHONE_INFRA_HEALTH", "Health checks"));
        registerAgent("phone_monitoring_clock", new GenericAgent("PHONE_MONITORING_CLOCK", "Monitoring"));
        registerAgent("cross_node_time_drift", new GenericAgent("CROSS_NODE_TIME_DRIFT", "Time sync"));
        registerAgent("model_governance_registry", new GenericAgent("MODEL_GOVERNANCE_REGISTRY", "Model governance"));
        
        // Category 3: Organizer & Network Management (10 agents)
        registerAgent("user_registration", new GenericAgent("USER_REGISTRATION", "User registration"));
        registerAgent("kyc_verification", new GenericAgent("KYC_VERIFICATION", "KYC verification"));
        registerAgent("coordinator_onboarding", new GenericAgent("COORDINATOR_ONBOARDING", "Coordinator setup"));
        registerAgent("master_organizer_onboarding", new GenericAgent("MASTER_ORGANIZER_ONBOARDING", "Master organizer"));
        registerAgent("rural_block_onboarding", new GenericAgent("RURAL_BLOCK_ONBOARDING", "Rural blocks"));
        registerAgent("role_assignment", new GenericAgent("ROLE_ASSIGNMENT", "Role management"));
        registerAgent("household_id_linking", new GenericAgent("HOUSEHOLD_ID_LINKING", "Household linking"));
        registerAgent("society_mapping", new GenericAgent("SOCIETY_MAPPING", "Society mapping"));
        registerAgent("resource_allocation", new GenericAgent("RESOURCE_ALLOCATION", "Resource alloc"));
        registerAgent("network_management", new GenericAgent("NETWORK_MANAGEMENT", "Network mgmt"));
        
        // Category 4: Scoring & Fairness (11 agents)
        registerAgent("score_bias_audit", new GenericAgent("SCORE_BIAS_AUDIT", "Bias auditing"));
        registerAgent("scoring_model_deprecation", new GenericAgent("SCORING_DEPRECATION", "Model deprecation"));
        registerAgent("phone_score_dispute", new GenericAgent("PHONE_SCORE_DISPUTE", "Score disputes"));
        registerAgent("phone_scoring_sanitizer", new GenericAgent("PHONE_SCORING_SANITIZER", "Input sanitization"));
        registerAgent("phone_speaking_time", new GenericAgent("PHONE_SPEAKING_TIME", "Speaking time"));
        registerAgent("phone_min_participation", new GenericAgent("PHONE_MIN_PARTICIPATION", "Minimum participation"));
        registerAgent("offline_go_to_dojo_sync", new GenericAgent("OFFLINE_DOJO_SYNC", "Offline sync"));
        registerAgent("phone_ai_explainability", new GenericAgent("PHONE_AI_EXPLAINABILITY", "AI explanation"));
        registerAgent("phone_behavior_analytics", new GenericAgent("PHONE_BEHAVIOR_ANALYTICS", "Behavior analysis"));
        registerAgent("phone_participation_reputation", new GenericAgent("PHONE_PARTICIPATION_REP", "Reputation"));
        registerAgent("cross_session_behavior", new GenericAgent("CROSS_SESSION_BEHAVIOR", "Session behavior"));
        
        // Category 5: Security & Compliance (14 agents)
        registerAgent("phone_tenant_boundary", new GenericAgent("PHONE_TENANT_BOUNDARY", "Tenant isolation"));
        registerAgent("phone_domain_isolation", new GenericAgent("PHONE_DOMAIN_ISOLATION", "Domain isolation"));
        registerAgent("tenant_audio_isolation", new GenericAgent("TENANT_AUDIO_ISOLATION", "Audio isolation"));
        registerAgent("tenant_transcript_encryption", new GenericAgent("TENANT_ENCRYPTION", "Encryption"));
        registerAgent("phone_permission_matrix", new GenericAgent("PHONE_PERMISSIONS", "Permissions"));
        registerAgent("phone_role_escalation", new GenericAgent("PHONE_ROLE_ESCALATION", "Role escalation"));
        registerAgent("phone_feature_gating", new GenericAgent("PHONE_FEATURE_GATING", "Feature gating"));
        registerAgent("phone_participant_identity", new GenericAgent("PHONE_IDENTITY", "Identity"));
        registerAgent("short_lived_token_revocation", new GenericAgent("TOKEN_REVOCATION", "Token mgmt"));
        registerAgent("human_override_audit", new GenericAgent("HUMAN_OVERRIDE_AUDIT", "Override audit"));
        registerAgent("phone_bot_detection", new GenericAgent("PHONE_BOT_DETECTION", "Bot detection"));
        registerAgent("phone_transparency_notification", new GenericAgent("PHONE_TRANSPARENCY", "Transparency"));
        registerAgent("media_session_security", new GenericAgent("MEDIA_SESSION_SECURITY", "Session security"));
        registerAgent("voice_impersonation_detection", new GenericAgent("VOICE_IMPERSONATION", "Impersonation"));
        
        // Category 6: Billing & Quota (7 agents)
        registerAgent("call_cost_calculation", new GenericAgent("CALL_COST_CALC", "Cost calculation"));
        registerAgent("call_rate_limit", new GenericAgent("CALL_RATE_LIMIT", "Rate limiting"));
        registerAgent("high_usage_alert", new GenericAgent("HIGH_USAGE_ALERT", "Usage alerts"));
        registerAgent("tenant_quota_enforcement", new GenericAgent("TENANT_QUOTA", "Quota enforcement"));
        registerAgent("sms_segment_calculation", new GenericAgent("SMS_SEGMENT_CALC", "SMS calc"));
        registerAgent("telecom_usage_reconciliation", new GenericAgent("TELECOM_RECONCILIATION", "Reconciliation"));
        registerAgent("phone_resource_quota", new GenericAgent("PHONE_RESOURCE_QUOTA", "Resource quota"));
        
        // Category 7: Core Intelligence Architect (19+ agents)
        registerAgent("spatial_pattern_model", new GenericAgent("SPATIAL_PATTERN_MODEL", "Spatial analysis"));
        registerAgent("reflective_depth_analyzer", new GenericAgent("REFLECTIVE_DEPTH_ANALYZER", "Depth analysis"));
        registerAgent("population_percentile_engine", new GenericAgent("POPULATION_PERCENTILE", "Percentile calc"));
        registerAgent("open_response_evaluation", new GenericAgent("OPEN_RESPONSE_EVALUATION", "Response eval"));
        registerAgent("naturalistic_classification", new GenericAgent("NATURALISTIC_CLASSIFICATION", "Classification"));
        registerAgent("musical_frequency_model", new GenericAgent("MUSICAL_FREQUENCY", "Frequency analysis"));
        registerAgent("logical_scoring_model", new GenericAgent("LOGICAL_SCORING", "Logic scoring"));
        registerAgent("linguistic_structural_analyzer", new GenericAgent("LINGUISTIC_ANALYZER", "Language analysis"));
        registerAgent("learning_velocity_model", new GenericAgent("LEARNING_VELOCITY", "Learning velocity"));
        registerAgent("kinesthetic_motion_model", new GenericAgent("KINESTHETIC_MOTION", "Motion analysis"));
        registerAgent("intrapersonal_behavioral_model", new GenericAgent("INTRAPERSONAL_BEHAVIORAL", "Behavior model"));
        registerAgent("interpersonal_graph_model", new GenericAgent("INTERPERSONAL_GRAPH", "Graph model"));
        registerAgent("intelligence_report_generator", new GenericAgent("INTELLIGENCE_REPORT", "Report gen"));
        registerAgent("intelligence_growth_timeseries", new GenericAgent("INTELLIGENCE_GROWTH", "Growth analysis"));
        registerAgent("entrepreneurial_risk_model", new GenericAgent("ENTREPRENEURIAL_RISK", "Risk model"));
        registerAgent("debate_quality_analyzer", new GenericAgent("DEBATE_QUALITY", "Debate analysis"));
        registerAgent("creativity_divergence_agent", new GenericAgent("CREATIVITY_DIVERGENCE", "Creativity analysis"));
        registerAgent("cognitive_stability_index", new GenericAgent("COGNITIVE_STABILITY", "Stability index"));
        registerAgent("ai_collaboration_efficiency", new GenericAgent("AI_COLLABORATION", "Collaboration"));
        
        logger.info("✅ Initialized {} agents for CAT-18 Complete", agents.size());
    }

    private void registerAgent(String toolName, Agent agent) {
        agents.put(toolName, agent);
    }

    public JsonNode handle(JsonNode request) {
        try {
            String method = request.path("method").asText();
            Object id = request.path("id").isMissingNode() ? null : request.path("id").asText();
            JsonNode params = request.path("params");

            logger.debug("📩 Request - method: {}, agents available: {}", method, agents.size());

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
        serverInfo.put("name", "mcp-18-complete");
        serverInfo.put("version", "1.0.0");
        serverInfo.put("description", "EcoSkiller CAT-18 Complete — Governance & Core Control");
        serverInfo.put("total_agents", agents.size());
        
        result.set("serverInfo", serverInfo);
        
        ObjectNode protocolVersion = JsonNodeFactory.instance.objectNode();
        protocolVersion.put("version", "2024-11-05");
        result.set("protocolVersion", protocolVersion);
        
        result.set("capabilities", JsonNodeFactory.instance.objectNode());
        
        return buildResponse(id, result);
    }

    private JsonNode handleToolsList(Object id) {
        return buildResponse(id, toolRegistry.getToolsList(agents.size()));
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
        result.put("agents_available", agents.size());
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
