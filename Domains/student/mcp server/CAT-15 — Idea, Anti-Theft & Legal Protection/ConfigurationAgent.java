package com.ecoskiller.antigravity.agents;

import com.ecoskiller.antigravity.models.AgentResponse;
import com.ecoskiller.antigravity.models.McpTool;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * ANTIGRAVITY_CONFIGURATION_AGENT
 * Manages platform-wide configuration: feature flags, env profiles, runtime overrides.
 */
public class ConfigurationAgent extends BaseAgent {

    @Override public String getName() { return "ANTIGRAVITY_CONFIGURATION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool(
                "antigravity_get_config",
                "Retrieve the current Antigravity platform configuration for a given environment.",
                schema("environment", "namespace")
            ),
            new McpTool(
                "antigravity_set_config",
                "Update a configuration key-value pair for the specified environment.",
                schema("environment", "key", "value", "reason")
            ),
            new McpTool(
                "antigravity_list_feature_flags",
                "List all feature flags and their current states.",
                schema("environment")
            ),
            new McpTool(
                "antigravity_toggle_feature_flag",
                "Enable or disable a specific feature flag.",
                schema("flag_name", "enabled", "environment")
            )
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "antigravity_get_config" -> {
                String env = args.path("environment").asText("production");
                String ns  = args.path("namespace").asText("core");
                ObjectNode data = mapper.createObjectNode();
                data.put("environment",     env);
                data.put("namespace",       ns);
                data.put("version",         "v15.4.1");
                data.put("log_level",       "INFO");
                data.put("max_agents",      461);
                data.put("mcp_server",      "mcp-01-platform");
                data.put("hot_reload",      true);
                data.put("config_source",   "vault://ecoskiller/antigravity/" + env);
                yield AgentResponse.success(getName(), data, "Configuration loaded for env=" + env);
            }
            case "antigravity_set_config" -> {
                String key   = args.path("key").asText();
                String value = args.path("value").asText();
                String reason = args.path("reason").asText("no reason provided");
                ObjectNode data = mapper.createObjectNode();
                data.put("key",     key);
                data.put("value",   value);
                data.put("reason",  reason);
                data.put("applied", true);
                data.put("audit_id", "cfg-" + System.currentTimeMillis());
                yield AgentResponse.success(getName(), data, "Config key '" + key + "' updated successfully.");
            }
            case "antigravity_list_feature_flags" -> {
                ObjectNode data  = mapper.createObjectNode();
                ObjectNode flags = mapper.createObjectNode();
                flags.put("ENABLE_ANTIGRAVITY_ORCHESTRATOR", true);
                flags.put("ENABLE_TRUTH_ENGINE",             true);
                flags.put("ENABLE_SCHEMA_VALIDATION",        true);
                flags.put("ENABLE_TECH_DEBT_SCANNER",        false);
                flags.put("ENABLE_UI_AGENT",                 true);
                flags.put("ENABLE_API_VERSIONING",           true);
                data.set("flags", flags);
                data.put("total", 6);
                yield AgentResponse.success(getName(), data, "Feature flags retrieved.");
            }
            case "antigravity_toggle_feature_flag" -> {
                String  flag    = args.path("flag_name").asText();
                boolean enabled = args.path("enabled").asBoolean(true);
                ObjectNode data = mapper.createObjectNode();
                data.put("flag",        flag);
                data.put("enabled",     enabled);
                data.put("changed_by",  "ANTIGRAVITY_CONFIGURATION_AGENT");
                data.put("timestamp",   System.currentTimeMillis());
                yield AgentResponse.success(getName(), data, "Flag '" + flag + "' set to " + enabled);
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}
