package com.ecoskiller.mcp.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Registry of all available tools in the MCP-17 Royalty Server
 */
public class ToolRegistry {
    
    public JsonNode getToolsList() {
        ArrayNode tools = JsonNodeFactory.instance.arrayNode();
        
        // CAT-17: Marketplace, Royalty & Compliance Agents (18 total)
        
        // 1. TAX_COMPLIANCE
        tools.add(createTool("tax_compliance", "TAX_COMPLIANCE_AGENT",
            "Handles tax compliance, filing, and regulatory requirements",
            new String[]{"entity_id", "tax_year", "compliance_level"}));
        
        // 2. SCHOOL_AUTO_CREATION
        tools.add(createTool("school_auto_creation", "SCHOOL_AUTO_CREATION_AGENT",
            "Automatically creates school entities and provisions infrastructure",
            new String[]{"school_name", "region", "capacity"}));
        
        // 3. ROYALTY_WALLET
        tools.add(createTool("royalty_wallet", "ROYALTY_WALLET_AGENT",
            "Manages royalty wallet balance and transactions",
            new String[]{"wallet_id", "balance", "operation"}));
        
        // 4. ROYALTY_SYSTEM_UNIFIED
        tools.add(createTool("royalty_system_unified", "ROYALTY_SYSTEM_UNIFIED_AGENT",
            "Unified royalty system orchestration and coordination",
            new String[]{"action", "entity_id"}));
        
        // 5. ROYALTY_ESCROW
        tools.add(createTool("royalty_escrow", "ROYALTY_ESCROW_AGENT",
            "Manages escrow accounts and fund holds for royalty transactions",
            new String[]{"escrow_id", "amount", "release_condition"}));
        
        // 6. ROYALTY_DISTRIBUTION
        tools.add(createTool("royalty_distribution", "ROYALTY_DISTRIBUTION_AGENT",
            "Distributes royalties to beneficiaries based on agreements",
            new String[]{"distribution_id", "beneficiary_list", "amount"}));
        
        // 7. ROYALTY_CALCULATION
        tools.add(createTool("royalty_calculation", "ROYALTY_CALCULATION_AGENT",
            "Calculates royalties based on revenue and agreements",
            new String[]{"revenue", "royalty_percentage", "agreement_id"}));
        
        // 8. REVENUE_INGESTION
        tools.add(createTool("revenue_ingestion", "REVENUE_INGESTION_AGENT",
            "Ingests revenue data from multiple sources",
            new String[]{"source", "amount", "transaction_date"}));
        
        // 9. PARENT_DASHBOARD
        tools.add(createTool("parent_dashboard", "PARENT_DASHBOARD_AGENT",
            "Provides parent dashboard data and insights",
            new String[]{"parent_id", "time_period", "metrics"}));
        
        // 10. MARKET_DEMAND_PREDICTION
        tools.add(createTool("market_demand_prediction", "MARKET_DEMAND_PREDICTION_AGENT",
            "Predicts market demand and trend forecasting",
            new String[]{"region", "time_horizon", "skill_category"}));
        
        // 11. LICENSE_GENERATION
        tools.add(createTool("license_generation", "LICENSE_GENERATION_AGENT",
            "Generates licenses and manages licensing agreements",
            new String[]{"license_type", "beneficiary_id", "duration"}));
        
        // 12. IDEA_VISIBILITY_CONTROL
        tools.add(createTool("idea_visibility_control", "IDEA_VISIBILITY_CONTROL_AGENT",
            "Controls visibility and access to ideas and content",
            new String[]{"idea_id", "visibility_level", "access_list"}));
        
        // 13. IDEA_EVALUATION
        tools.add(createTool("idea_evaluation", "IDEA_EVALUATION_AGENT",
            "Evaluates ideas for quality, feasibility, and impact",
            new String[]{"idea_id", "evaluation_criteria", "reviewer_id"}));
        
        // 14. DATA_RETENTION_POLICY
        tools.add(createTool("data_retention_policy", "DATA_RETENTION_POLICY_AGENT",
            "Manages data retention policies and archival",
            new String[]{"policy_type", "retention_period", "data_classification"}));
        
        // 15. CONTRACT_LIFECYCLE
        tools.add(createTool("contract_lifecycle", "CONTRACT_LIFECYCLE_AGENT",
            "Manages contract creation, execution, and lifecycle",
            new String[]{"contract_id", "stage", "parties"}));
        
        // 16. COMPETITION_ENGINE
        tools.add(createTool("competition_engine", "COMPETITION_ENGINE_AGENT",
            "Manages competitions, rankings, and leaderboards",
            new String[]{"competition_id", "action", "participant_id"}));
        
        // 17. BUSINESS_MATCHING
        tools.add(createTool("business_matching", "BUSINESS_MATCHING_AGENT",
            "Matches businesses and partners based on criteria",
            new String[]{"business_id", "match_criteria", "limit"}));
        
        // 18. AUDIT_VERIFICATION
        tools.add(createTool("audit_verification", "AUDIT_VERIFICATION_AGENT",
            "Performs audits and verification of transactions",
            new String[]{"audit_id", "scope", "transaction_list"}));
        
        return tools;
    }

    private ObjectNode createTool(String name, String agentName, String description, String[] inputSchema) {
        ObjectNode tool = JsonNodeFactory.instance.objectNode();
        
        tool.put("name", name);
        tool.put("description", description);
        tool.put("inputSchema", createInputSchema(inputSchema));
        
        ObjectNode meta = JsonNodeFactory.instance.objectNode();
        meta.put("agent", agentName);
        meta.put("category", "CAT-17");
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
