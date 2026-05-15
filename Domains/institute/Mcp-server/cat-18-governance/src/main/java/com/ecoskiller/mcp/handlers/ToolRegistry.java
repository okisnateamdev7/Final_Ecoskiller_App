package com.ecoskiller.mcp.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ToolRegistry {
    
    public JsonNode getToolsList(int totalAgents) {
        ArrayNode tools = JsonNodeFactory.instance.arrayNode();
        
        // Analytics & ERP (8 tools)
        tools.add(createTool("clickhouse_metric_normalization", "CLICKHOUSE_METRIC_NORMALIZATION", "Normalize ClickHouse metrics", "Analytics"));
        tools.add(createTool("erp_go_report_integration", "ERP_GO_REPORT_INTEGRATION", "Integrate ERP Go reports", "Analytics"));
        tools.add(createTool("phone_feature_vector_emission", "PHONE_FEATURE_VECTOR_EMISSION", "Emit feature vectors", "Analytics"));
        tools.add(createTool("attendance_behavior_analytics", "ATTENDANCE_BEHAVIOR_ANALYTICS", "Analyze attendance behavior", "Analytics"));
        tools.add(createTool("enrollment_analytics", "ENROLLMENT_ANALYTICS", "Analyze enrollment data", "Analytics"));
        tools.add(createTool("analytics_connect", "ANALYTICS_CONNECT", "Connect analytics platforms", "Analytics"));
        tools.add(createTool("data_warehouse_analytics", "DATA_WAREHOUSE_ANALYTICS", "Data warehouse analytics", "Analytics"));
        tools.add(createTool("reporting_engine", "REPORTING_ENGINE", "Generate reports", "Analytics"));
        
        // DevOps & Environment (8 tools)
        tools.add(createTool("multi_environment_config", "MULTI_ENV_CONFIG", "Multi-environment config", "DevOps"));
        tools.add(createTool("phone_backup_restore", "PHONE_BACKUP_RESTORE", "Backup and restore", "DevOps"));
        tools.add(createTool("phone_end_to_end_trace", "PHONE_E2E_TRACE", "End-to-end tracing", "DevOps"));
        tools.add(createTool("phone_external_webhook", "PHONE_EXTERNAL_WEBHOOK", "External webhooks", "DevOps"));
        tools.add(createTool("phone_infra_health", "PHONE_INFRA_HEALTH", "Infrastructure health", "DevOps"));
        tools.add(createTool("phone_monitoring_clock", "PHONE_MONITORING_CLOCK", "Monitoring and clock", "DevOps"));
        tools.add(createTool("cross_node_time_drift", "CROSS_NODE_TIME_DRIFT", "Cross-node time sync", "DevOps"));
        tools.add(createTool("model_governance_registry", "MODEL_GOVERNANCE_REGISTRY", "Model governance", "DevOps"));
        
        // Organizer & Network Management (10 tools)
        tools.add(createTool("user_registration", "USER_REGISTRATION", "User registration", "Network"));
        tools.add(createTool("kyc_verification", "KYC_VERIFICATION", "KYC verification", "Network"));
        tools.add(createTool("coordinator_onboarding", "COORDINATOR_ONBOARDING", "Coordinator onboarding", "Network"));
        tools.add(createTool("master_organizer_onboarding", "MASTER_ORGANIZER_ONBOARDING", "Master organizer setup", "Network"));
        tools.add(createTool("rural_block_onboarding", "RURAL_BLOCK_ONBOARDING", "Rural block setup", "Network"));
        tools.add(createTool("role_assignment", "ROLE_ASSIGNMENT", "Role assignment", "Network"));
        tools.add(createTool("household_id_linking", "HOUSEHOLD_ID_LINKING", "Household linking", "Network"));
        tools.add(createTool("society_mapping", "SOCIETY_MAPPING", "Society mapping", "Network"));
        tools.add(createTool("resource_allocation", "RESOURCE_ALLOCATION", "Resource allocation", "Network"));
        tools.add(createTool("network_management", "NETWORK_MANAGEMENT", "Network management", "Network"));
        
        // Scoring & Fairness (11 tools)
        tools.add(createTool("score_bias_audit", "SCORE_BIAS_AUDIT", "Score bias auditing", "Scoring"));
        tools.add(createTool("scoring_model_deprecation", "SCORING_DEPRECATION", "Model deprecation", "Scoring"));
        tools.add(createTool("phone_score_dispute", "PHONE_SCORE_DISPUTE", "Score disputes", "Scoring"));
        tools.add(createTool("phone_scoring_sanitizer", "PHONE_SCORING_SANITIZER", "Input sanitization", "Scoring"));
        tools.add(createTool("phone_speaking_time", "PHONE_SPEAKING_TIME", "Speaking time metrics", "Scoring"));
        tools.add(createTool("phone_min_participation", "PHONE_MIN_PARTICIPATION", "Minimum participation", "Scoring"));
        tools.add(createTool("offline_go_to_dojo_sync", "OFFLINE_DOJO_SYNC", "Offline sync", "Scoring"));
        tools.add(createTool("phone_ai_explainability", "PHONE_AI_EXPLAINABILITY", "AI explainability", "Scoring"));
        tools.add(createTool("phone_behavior_analytics", "PHONE_BEHAVIOR_ANALYTICS", "Behavior analytics", "Scoring"));
        tools.add(createTool("phone_participation_reputation", "PHONE_PARTICIPATION_REP", "Reputation management", "Scoring"));
        tools.add(createTool("cross_session_behavior", "CROSS_SESSION_BEHAVIOR", "Cross-session behavior", "Scoring"));
        
        // Security & Compliance (14 tools)
        tools.add(createTool("phone_tenant_boundary", "PHONE_TENANT_BOUNDARY", "Tenant isolation", "Security"));
        tools.add(createTool("phone_domain_isolation", "PHONE_DOMAIN_ISOLATION", "Domain isolation", "Security"));
        tools.add(createTool("tenant_audio_isolation", "TENANT_AUDIO_ISOLATION", "Audio isolation", "Security"));
        tools.add(createTool("tenant_transcript_encryption", "TENANT_ENCRYPTION", "Encryption", "Security"));
        tools.add(createTool("phone_permission_matrix", "PHONE_PERMISSIONS", "Permission matrix", "Security"));
        tools.add(createTool("phone_role_escalation", "PHONE_ROLE_ESCALATION", "Role escalation", "Security"));
        tools.add(createTool("phone_feature_gating", "PHONE_FEATURE_GATING", "Feature gating", "Security"));
        tools.add(createTool("phone_participant_identity", "PHONE_IDENTITY", "Identity management", "Security"));
        tools.add(createTool("short_lived_token_revocation", "TOKEN_REVOCATION", "Token management", "Security"));
        tools.add(createTool("human_override_audit", "HUMAN_OVERRIDE_AUDIT", "Override audit", "Security"));
        tools.add(createTool("phone_bot_detection", "PHONE_BOT_DETECTION", "Bot detection", "Security"));
        tools.add(createTool("phone_transparency_notification", "PHONE_TRANSPARENCY", "Transparency", "Security"));
        tools.add(createTool("media_session_security", "MEDIA_SESSION_SECURITY", "Session security", "Security"));
        tools.add(createTool("voice_impersonation_detection", "VOICE_IMPERSONATION", "Impersonation detection", "Security"));
        
        // Billing & Quota (7 tools)
        tools.add(createTool("call_cost_calculation", "CALL_COST_CALC", "Cost calculation", "Billing"));
        tools.add(createTool("call_rate_limit", "CALL_RATE_LIMIT", "Rate limiting", "Billing"));
        tools.add(createTool("high_usage_alert", "HIGH_USAGE_ALERT", "Usage alerts", "Billing"));
        tools.add(createTool("tenant_quota_enforcement", "TENANT_QUOTA", "Quota enforcement", "Billing"));
        tools.add(createTool("sms_segment_calculation", "SMS_SEGMENT_CALC", "SMS calculation", "Billing"));
        tools.add(createTool("telecom_usage_reconciliation", "TELECOM_RECONCILIATION", "Reconciliation", "Billing"));
        tools.add(createTool("phone_resource_quota", "PHONE_RESOURCE_QUOTA", "Resource quota", "Billing"));
        
        // Core Intelligence Architect (19 tools)
        tools.add(createTool("spatial_pattern_model", "SPATIAL_PATTERN_MODEL", "Spatial pattern analysis", "Intelligence"));
        tools.add(createTool("reflective_depth_analyzer", "REFLECTIVE_DEPTH_ANALYZER", "Reflective depth analysis", "Intelligence"));
        tools.add(createTool("population_percentile_engine", "POPULATION_PERCENTILE", "Population percentiles", "Intelligence"));
        tools.add(createTool("open_response_evaluation", "OPEN_RESPONSE_EVALUATION", "Open response evaluation", "Intelligence"));
        tools.add(createTool("naturalistic_classification", "NATURALISTIC_CLASSIFICATION", "Naturalistic classification", "Intelligence"));
        tools.add(createTool("musical_frequency_model", "MUSICAL_FREQUENCY", "Musical frequency analysis", "Intelligence"));
        tools.add(createTool("logical_scoring_model", "LOGICAL_SCORING", "Logical scoring", "Intelligence"));
        tools.add(createTool("linguistic_structural_analyzer", "LINGUISTIC_ANALYZER", "Linguistic analysis", "Intelligence"));
        tools.add(createTool("learning_velocity_model", "LEARNING_VELOCITY", "Learning velocity", "Intelligence"));
        tools.add(createTool("kinesthetic_motion_model", "KINESTHETIC_MOTION", "Kinesthetic motion", "Intelligence"));
        tools.add(createTool("intrapersonal_behavioral_model", "INTRAPERSONAL_BEHAVIORAL", "Intrapersonal behavior", "Intelligence"));
        tools.add(createTool("interpersonal_graph_model", "INTERPERSONAL_GRAPH", "Interpersonal graph", "Intelligence"));
        tools.add(createTool("intelligence_report_generator", "INTELLIGENCE_REPORT", "Intelligence reports", "Intelligence"));
        tools.add(createTool("intelligence_growth_timeseries", "INTELLIGENCE_GROWTH", "Growth time series", "Intelligence"));
        tools.add(createTool("entrepreneurial_risk_model", "ENTREPRENEURIAL_RISK", "Entrepreneurial risk", "Intelligence"));
        tools.add(createTool("debate_quality_analyzer", "DEBATE_QUALITY", "Debate quality analysis", "Intelligence"));
        tools.add(createTool("creativity_divergence_agent", "CREATIVITY_DIVERGENCE", "Creativity & divergence", "Intelligence"));
        tools.add(createTool("cognitive_stability_index", "COGNITIVE_STABILITY", "Cognitive stability", "Intelligence"));
        tools.add(createTool("ai_collaboration_efficiency", "AI_COLLABORATION", "AI collaboration", "Intelligence"));
        
        return tools;
    }

    private ObjectNode createTool(String name, String agentName, String description, String category) {
        ObjectNode tool = JsonNodeFactory.instance.objectNode();
        
        tool.put("name", name);
        tool.put("description", description);
        tool.put("inputSchema", createInputSchema(new String[]{"parameters", "options"}));
        
        ObjectNode meta = JsonNodeFactory.instance.objectNode();
        meta.put("agent", agentName);
        meta.put("category", "CAT-18");
        meta.put("subcategory", category);
        meta.put("priority", "HIGH");
        tool.set("_meta", meta);
        
        return tool;
    }

    private ObjectNode createInputSchema(String[] properties) {
        ObjectNode schema = JsonNodeFactory.instance.objectNode();
        schema.put("type", "object");
        
        ObjectNode props = JsonNodeFactory.instance.objectNode();
        for (String prop : properties) {
            ObjectNode propDef = JsonNodeFactory.instance.objectNode();
            propDef.put("type", "string");
            propDef.put("description", "Parameter: " + prop);
            props.set(prop, propDef);
        }
        
        schema.set("properties", props);
        schema.put("required", JsonNodeFactory.instance.arrayNode());
        
        return schema;
    }
}
