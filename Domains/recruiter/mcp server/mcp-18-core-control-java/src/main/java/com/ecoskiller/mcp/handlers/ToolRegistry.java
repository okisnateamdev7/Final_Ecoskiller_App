package com.ecoskiller.mcp.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ToolRegistry {
    
    public JsonNode getToolsList() {
        ArrayNode tools = JsonNodeFactory.instance.arrayNode();
        
        // CAT-18: Governance & Core Control (11 agents)
        
        tools.add(createTool("analytics_connect", "ANALYTICS_CONNECT_AGENT",
            "Connect analytics platforms and data pipelines",
            new String[]{"platform", "api_key", "region"}));
        
        tools.add(createTool("automation_connect", "AUTOMATION_CONNECT_AGENT",
            "Orchestrate automation workflows and integrations",
            new String[]{"workflow_id", "trigger_type", "actions"}));
        
        tools.add(createTool("data_warehouse", "DATA_WAREHOUSE_AGENT",
            "Manage data warehouse operations and queries",
            new String[]{"query", "database", "parameters"}));
        
        tools.add(createTool("deployment_checklist", "DEPLOYMENT_CHECKLIST_AGENT",
            "Manage deployment checklists and release processes",
            new String[]{"deployment_id", "environment", "checklist"}));
        
        tools.add(createTool("digilock", "DIGILOCK_AGENT",
            "Handle digital document locking and signing",
            new String[]{"document_id", "lock_type", "permissions"}));
        
        tools.add(createTool("esign_connect", "ESIGN_CONNECT_AGENT",
            "Integrate e-signature services and signing workflows",
            new String[]{"signing_id", "signatories", "document"}));
        
        tools.add(createTool("implementation_guide", "IMPLEMENTATION_GUIDE_AGENT",
            "Provide implementation guides and best practices",
            new String[]{"guide_id", "topic", "skill_level"}));
        
        tools.add(createTool("llm_connect", "LLM_CONNECT_AGENT",
            "Connect and manage LLM integrations",
            new String[]{"model", "api_key", "parameters"}));
        
        tools.add(createTool("marketing_connect", "MARKETING_CONNECT_AGENT",
            "Integrate marketing platforms and campaigns",
            new String[]{"campaign_id", "platform", "channels"}));
        
        tools.add(createTool("payment_connect", "PAYMENT_CONNECT_AGENT",
            "Manage payment gateway integrations",
            new String[]{"amount", "currency", "gateway"}));
        
        tools.add(createTool("video_connect", "VIDEO_CONNECT_AGENT",
            "Integrate video processing and streaming services",
            new String[]{"video_id", "quality", "format"}));
        
        return tools;
    }

    private ObjectNode createTool(String name, String agentName, String description, String[] inputSchema) {
        ObjectNode tool = JsonNodeFactory.instance.objectNode();
        
        tool.put("name", name);
        tool.put("description", description);
        tool.put("inputSchema", createInputSchema(inputSchema));
        
        ObjectNode meta = JsonNodeFactory.instance.objectNode();
        meta.put("agent", agentName);
        meta.put("category", "CAT-18");
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
