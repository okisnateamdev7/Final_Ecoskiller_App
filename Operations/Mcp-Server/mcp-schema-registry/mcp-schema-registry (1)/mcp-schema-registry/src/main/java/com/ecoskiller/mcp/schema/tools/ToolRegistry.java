package com.ecoskiller.mcp.schema.tools;

import com.ecoskiller.mcp.schema.client.ApicurioClient;
import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.ecoskiller.mcp.schema.security.AuditLogger;

import java.util.*;

/**
 * Central registry for all 22 Apicurio Schema Registry MCP tools.
 *
 * Tool categories:
 *   Schema Registration & Versioning  (5)  : schema_register, schema_get, schema_get_by_id,
 *                                            schema_list_versions, schema_list_subjects
 *   Compatibility Management          (4)  : compatibility_get, compatibility_set,
 *                                            compatibility_test, global_rule_set
 *   Schema Validation                 (2)  : schema_validate_content, schema_lint
 *   Schema Lifecycle & Governance     (4)  : schema_update_state, schema_update_meta,
 *                                            schema_delete, schema_delete_version
 *   Schema Discovery & Search         (3)  : schema_search, schema_get_meta, schema_export
 *   Multi-Tenant Group Management     (2)  : group_create, group_list
 *   Schema References                 (1)  : schema_get_references
 *   Health & Monitoring               (1)  : registry_health
 *                                   ─────
 *   Total                            22 tools
 */
public class ToolRegistry {

    private final Map<String, McpTool> tools = new LinkedHashMap<>();

    public ToolRegistry(ApicurioClient client, SchemaRegistryConfig cfg, AuditLogger audit) {

        // ── Schema Registration & Versioning ──────────────────────────────────
        register(new SchemaRegisterTool(client, cfg, audit));
        register(new SchemaGetTool(client, cfg, audit));
        register(new SchemaGetByIdTool(client, cfg, audit));
        register(new SchemaListVersionsTool(client, cfg, audit));
        register(new SchemaListSubjectsTool(client, cfg, audit));

        // ── Compatibility Management ──────────────────────────────────────────
        register(new CompatibilityGetTool(client, cfg, audit));
        register(new CompatibilitySetTool(client, cfg, audit));
        register(new CompatibilityTestTool(client, cfg, audit));
        register(new GlobalRuleSetTool(client, cfg, audit));

        // ── Schema Validation ─────────────────────────────────────────────────
        register(new SchemaValidateContentTool(client, cfg, audit));
        register(new SchemaLintTool(client, cfg, audit));

        // ── Schema Lifecycle & Governance ─────────────────────────────────────
        register(new SchemaUpdateStateTool(client, cfg, audit));
        register(new SchemaUpdateMetaTool(client, cfg, audit));
        register(new SchemaDeleteTool(client, cfg, audit));
        register(new SchemaDeleteVersionTool(client, cfg, audit));

        // ── Schema Discovery & Search ─────────────────────────────────────────
        register(new SchemaSearchTool(client, cfg, audit));
        register(new SchemaGetMetaTool(client, cfg, audit));
        register(new SchemaExportTool(client, cfg, audit));

        // ── Multi-Tenant Group Management ─────────────────────────────────────
        register(new GroupCreateTool(client, cfg, audit));
        register(new GroupListTool(client, cfg, audit));

        // ── Schema References ─────────────────────────────────────────────────
        register(new SchemaGetReferencesTool(client, cfg, audit));

        // ── Health & Monitoring ───────────────────────────────────────────────
        register(new RegistryHealthTool(client, cfg, audit));
    }

    private void register(McpTool t) { tools.put(t.name(), t); }

    public McpTool find(String name)             { return tools.get(name); }
    public Collection<McpTool> allTools()        { return Collections.unmodifiableCollection(tools.values()); }
    public int size()                            { return tools.size(); }
}
