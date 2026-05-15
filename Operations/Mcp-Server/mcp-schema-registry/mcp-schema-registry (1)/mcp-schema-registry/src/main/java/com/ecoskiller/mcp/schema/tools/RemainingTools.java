package com.ecoskiller.mcp.schema.tools;

import com.ecoskiller.mcp.schema.client.ApicurioClient;
import com.ecoskiller.mcp.schema.client.ApicurioClient.ApicurioResponse;
import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.ecoskiller.mcp.schema.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Remaining tools grouped by category:
 *
 *  Schema Validation (2):
 *   10. schema_validate_content — validate a record against a schema
 *   11. schema_lint             — parse and lint schema for best-practices
 *
 *  Schema Lifecycle & Governance (4):
 *   12. schema_update_state     — set state: ENABLED, DISABLED, DEPRECATED
 *   13. schema_update_meta      — update name, description, labels
 *   14. schema_delete           — delete a schema or specific version
 *   15. schema_delete_version   — delete a single version
 *
 *  Schema Discovery & Search (3):
 *   16. schema_search           — full-text search across all schemas
 *   17. schema_get_meta         — get full artifact metadata
 *   18. schema_export           — export all schemas in a group (backup)
 *
 *  Multi-Tenant / Group Management (2):
 *   19. group_create            — create a tenant namespace (group)
 *   20. group_list              — list all groups (tenants)
 *
 *  References (1):
 *   21. schema_get_references   — get schemas that reference this artifact
 *
 *  Health & Monitoring (1):
 *   22. registry_health         — system info, version, health check
 */

// ═════════════════════════════════════════════════════════════════════════════
// SCHEMA VALIDATION
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 10. schema_validate_content
// ─────────────────────────────────────────────────────────────────────────────
class SchemaValidateContentTool extends BaseTool {

    SchemaValidateContentTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_validate_content"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("version",     versionProp());
        p.set("group",       groupProp());
        p.set("record",      strProp("JSON record to validate against the schema"));
        return buildSchema(name(),
            "Validate a JSON record against a registered schema before publishing to Kafka. " +
            "Prevents runtime deserialization failures. Returns valid=true/false with error details.",
            p, "artifact_id", "record");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String record     = requireString(args, "record");
        String version    = optString(args, "version", "latest");
        String group      = resolveGroup(args);

        // Parse record to ensure it's valid JSON first
        try {
            json.readTree(record);
        } catch (Exception e) {
            ObjectNode out = json.createObjectNode();
            out.put("valid",       false);
            out.put("artifact_id", artifactId);
            out.put("reason",      "Record is not valid JSON: " + e.getMessage());
            return ToolResult.ok(json.writeValueAsString(out));
        }

        ApicurioResponse r = client.validateContent(group, artifactId, version, record);

        ObjectNode out = json.createObjectNode();
        out.put("artifact_id", artifactId);
        out.put("group",       group);
        out.put("version",     version);

        if (r.statusCode == 200) {
            out.put("valid",   true);
            out.put("message", "Record is valid against schema");
        } else if (r.statusCode == 400 || r.statusCode == 422) {
            out.put("valid",   false);
            out.put("message", "Record validation failed");
            if (r.body().has("message"))   out.put("reason",  r.body().get("message").asText());
            if (r.body().has("causes"))    out.set("causes",  r.body().get("causes"));
        } else {
            return apicurioError(r.statusCode(), r.body(), "schema_validate_content");
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 11. schema_lint — parse and validate schema structure
// ─────────────────────────────────────────────────────────────────────────────
class SchemaLintTool extends BaseTool {

    SchemaLintTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_lint"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",       apiKeyProp());
        p.set("schema_content",strProp("Schema content to lint"));
        p.set("artifact_type", enumProp("Schema format", "AVRO","JSON","PROTOBUF","OPENAPI","ASYNCAPI"));
        return buildSchema(name(),
            "Lint and validate a schema definition for structural correctness and best practices " +
            "BEFORE registering it. Checks: valid JSON/Avro structure, required fields, " +
            "naming conventions, PII field presence.",
            p, "schema_content", "artifact_type");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String content = requireString(args, "schema_content");
        String type    = validateType(requireString(args, "artifact_type"));

        ArrayNode warnings = json.createArrayNode();
        ArrayNode errors   = json.createArrayNode();

        JsonNode parsed = null;

        // 1. JSON parse check
        try {
            parsed = json.readTree(content);
        } catch (Exception e) {
            errors.add("Schema is not valid JSON: " + e.getMessage());
        }

        if (parsed != null) {
            // 2. Avro-specific checks
            if ("AVRO".equals(type)) {
                if (!parsed.has("type")) errors.add("Avro schema missing required 'type' field");
                if (!parsed.has("name")) errors.add("Avro schema missing required 'name' field");
                if (!parsed.has("namespace")) warnings.add("Avro schema missing 'namespace' — recommended for cross-service compatibility");
                if (!parsed.has("doc")) warnings.add("Avro schema missing 'doc' field — add description for discoverability");

                // Check fields
                if (parsed.has("fields") && parsed.get("fields").isArray()) {
                    for (JsonNode field : parsed.get("fields")) {
                        String fname = field.path("name").asText("");
                        if (!field.has("doc")) {
                            warnings.add("Field '" + fname + "' missing 'doc' documentation");
                        }
                        // PII detection
                        if (fname.toLowerCase().contains("email") || fname.toLowerCase().contains("phone")
                            || fname.toLowerCase().contains("ssn") || fname.toLowerCase().contains("nid")) {
                            warnings.add("Field '" + fname + "' may contain PII — ensure schema is tagged appropriately");
                        }
                        // Naming convention check (camelCase)
                        if (fname.contains("-") || fname.contains(" ")) {
                            warnings.add("Field '" + fname + "' uses dashes/spaces — camelCase recommended");
                        }
                    }
                } else if ("record".equals(parsed.path("type").asText())) {
                    warnings.add("Avro record schema has no 'fields' array");
                }

                String name = parsed.path("name").asText("");
                if (!name.isEmpty() && !name.equals(name.substring(0,1).toUpperCase() + name.substring(1))) {
                    warnings.add("Avro schema name '" + name + "' should start with uppercase (PascalCase)");
                }
            }

            // 3. JSON Schema checks
            if ("JSON".equals(type)) {
                if (!parsed.has("$schema")) warnings.add("JSON Schema missing '$schema' declaration");
                if (!parsed.has("title"))   warnings.add("JSON Schema missing 'title' field");
                if (!parsed.has("description")) warnings.add("JSON Schema missing 'description'");
            }
        }

        ObjectNode out = json.createObjectNode();
        out.put("artifact_type",  type);
        out.put("parseable",      parsed != null);
        out.put("valid",          errors.size() == 0);
        out.put("error_count",    errors.size());
        out.put("warning_count",  warnings.size());
        out.set("errors",         errors);
        out.set("warnings",       warnings);
        out.put("message", errors.size() == 0
                ? (warnings.size() == 0 ? "Schema looks good!" : "Schema valid but has " + warnings.size() + " warning(s)")
                : "Schema has " + errors.size() + " error(s) — fix before registering");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// SCHEMA LIFECYCLE & GOVERNANCE
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 12. schema_update_state
// ─────────────────────────────────────────────────────────────────────────────
class SchemaUpdateStateTool extends BaseTool {

    SchemaUpdateStateTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_update_state"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("state",       enumProp(
            "New state:\n" +
            "  ENABLED   — Active, producers can use (default)\n" +
            "  DISABLED  — Blocked for new use (emergency rollback)\n" +
            "  DEPRECATED — Marked for removal; consumers should migrate",
            "ENABLED","DISABLED","DEPRECATED"));
        p.set("group", groupProp());
        p.set("reason",strProp("Reason for state change (for audit trail)"));
        return buildSchema(name(),
            "Update the lifecycle state of a schema. " +
            "DEPRECATED: producers detect this and fall back to prior version. " +
            "DISABLED: emergency rollback — blocks new registrations immediately.",
            p, "artifact_id", "state");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String state      = requireString(args, "state").toUpperCase();
        String group      = resolveGroup(args);
        String reason     = optString(args, "reason", "");

        if (!VALID_STATES.contains(state)) {
            return ToolResult.error("Invalid state: " + state + ". Must be one of: " + VALID_STATES);
        }

        ApicurioResponse r = client.updateState(group, artifactId, state);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_update_state");

        ObjectNode out = json.createObjectNode();
        out.put("success",     true);
        out.put("artifact_id", artifactId);
        out.put("group",       group);
        out.put("new_state",   state);
        out.put("reason",      reason);
        out.put("message",     "Schema state changed to " + state);
        if ("DEPRECATED".equals(state)) {
            out.put("note", "Producers will detect DEPRECATED state and use previous version. Notify consumers to migrate.");
        }
        if ("DISABLED".equals(state)) {
            out.put("note", "DISABLED state blocks all new use. Use for emergency rollbacks only.");
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 13. schema_update_meta
// ─────────────────────────────────────────────────────────────────────────────
class SchemaUpdateMetaTool extends BaseTool {

    SchemaUpdateMetaTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_update_meta"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",      apiKeyProp());
        p.set("artifact_id",  artifactIdProp());
        p.set("group",        groupProp());
        p.set("name",         strProp("Human-readable display name"));
        p.set("description",  strProp("Schema description — purpose, domain, owner team"));
        p.set("owner_team",   strProp("Owning team (e.g. scoring-team, assessment-team)"));
        p.set("domain",       strProp("Business domain tag (e.g. hiring, assessment, scoring, notification)"));
        p.set("pii",          strProp("'true' if schema contains PII fields — triggers data governance tagging"));
        return buildSchema(name(),
            "Update schema metadata — name, description, owner, domain tags, PII flag. " +
            "Does NOT create a new schema version. Used for governance and discoverability.",
            p, "artifact_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String group      = resolveGroup(args);

        ObjectNode meta = json.createObjectNode();
        String name  = optString(args, "name", "");
        String desc  = optString(args, "description", "");
        String team  = optString(args, "owner_team", "");
        String domain= optString(args, "domain", "");
        String pii   = optString(args, "pii", "");

        if (!name.isEmpty())   meta.put("name", name);
        if (!desc.isEmpty())   meta.put("description", desc);

        // Build labels for governance tags
        ObjectNode labels = json.createObjectNode();
        if (!team.isEmpty())   labels.put("owner_team", team);
        if (!domain.isEmpty()) labels.put("domain", domain);
        if (!pii.isEmpty())    labels.put("pii", pii);
        if (labels.size() > 0) meta.set("labels", labels);

        if (meta.size() == 0) {
            return ToolResult.error("No metadata fields provided to update");
        }

        ApicurioResponse r = client.updateMeta(group, artifactId, json.writeValueAsString(meta));
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_update_meta");

        ObjectNode out = json.createObjectNode();
        out.put("success",     true);
        out.put("artifact_id", artifactId);
        out.put("group",       group);
        out.set("updated",     meta);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 14. schema_delete (all versions)
// ─────────────────────────────────────────────────────────────────────────────
class SchemaDeleteTool extends BaseTool {

    SchemaDeleteTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_delete"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("group",       groupProp());
        p.set("confirm",     strProp("Must be 'CONFIRM' — safety guard against accidental deletion"));
        return buildSchema(name(),
            "Permanently delete a schema and ALL its versions. " +
            "Irreversible — use schema_update_state(DEPRECATED) for soft deprecation instead. " +
            "Requires confirm=CONFIRM.",
            p, "artifact_id", "confirm");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String confirm    = requireString(args, "confirm");
        String group      = resolveGroup(args);

        if (!"CONFIRM".equals(confirm)) {
            return ToolResult.error("Safety: set confirm=CONFIRM to delete a schema. " +
                                    "Consider schema_update_state(DEPRECATED) instead.");
        }

        ApicurioResponse r = client.deleteSchema(group, artifactId);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_delete");

        ObjectNode out = json.createObjectNode();
        out.put("deleted",     true);
        out.put("artifact_id", artifactId);
        out.put("group",       group);
        out.put("message",     "Schema and all versions permanently deleted");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 15. schema_delete_version
// ─────────────────────────────────────────────────────────────────────────────
class SchemaDeleteVersionTool extends BaseTool {

    SchemaDeleteVersionTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_delete_version"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("version",     strProp("Version number to delete (cannot delete 'latest')"));
        p.set("group",       groupProp());
        return buildSchema(name(),
            "Delete a specific version of a schema. Use to remove buggy schema versions. " +
            "Other versions remain intact. Cannot delete 'latest' directly — use version number.",
            p, "artifact_id", "version");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String version    = requireString(args, "version");
        String group      = resolveGroup(args);

        if ("latest".equalsIgnoreCase(version)) {
            return ToolResult.error("Cannot delete version 'latest' — specify the exact version number");
        }

        ApicurioResponse r = client.deleteVersion(group, artifactId, version);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_delete_version");

        ObjectNode out = json.createObjectNode();
        out.put("deleted",     true);
        out.put("artifact_id", artifactId);
        out.put("version",     version);
        out.put("group",       group);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// SCHEMA DISCOVERY & SEARCH
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 16. schema_search
// ─────────────────────────────────────────────────────────────────────────────
class SchemaSearchTool extends BaseTool {

    SchemaSearchTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_search"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",       apiKeyProp());
        p.set("query",         strProp("Search query — matches artifact ID, name, description"));
        p.set("group",         groupProp());
        p.set("artifact_type", enumProp("Filter by schema type (optional)", "AVRO","JSON","PROTOBUF","OPENAPI","ASYNCAPI","ANY"));
        p.set("limit",         intProp("Maximum results to return (default: 20, max: 100)"));
        p.set("offset",        intProp("Pagination offset (default: 0)"));
        return buildSchema(name(),
            "Search for schemas by name, description, or artifact ID. " +
            "Supports filtering by format type and tenant group. " +
            "Use for schema discovery, impact analysis, and onboarding new teams.",
            p, "query");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String query = requireString(args, "query");
        String group = optString(args, "group", "");
        String type  = optString(args, "artifact_type", "");
        int limit    = Math.min(optInt(args, "limit", 20), 100);
        int offset   = Math.max(optInt(args, "offset", 0), 0);

        if ("ANY".equalsIgnoreCase(type)) type = "";

        ApicurioResponse r = client.searchArtifacts(query, group, type, limit, offset);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_search");

        JsonNode artifacts = r.body().has("artifacts") ? r.body().get("artifacts") : r.body();
        int total = r.body().path("count").asInt(artifacts.isArray() ? artifacts.size() : 0);

        ObjectNode out = json.createObjectNode();
        out.put("query",          query);
        out.put("total_found",    total);
        out.put("returned",       artifacts.isArray() ? artifacts.size() : 0);
        out.put("offset",         offset);
        out.put("limit",          limit);
        out.set("results",        artifacts);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 17. schema_get_meta
// ─────────────────────────────────────────────────────────────────────────────
class SchemaGetMetaTool extends BaseTool {

    SchemaGetMetaTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_get_meta"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("group",       groupProp());
        return buildSchema(name(),
            "Get full artifact metadata: name, description, state, type, labels, owner, timestamps. " +
            "Does NOT return the schema content — use schema_get for that.",
            p, "artifact_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String group      = resolveGroup(args);

        ApicurioResponse r = client.getArtifactMeta(group, artifactId);
        return wrapSuccess(r, "schema_get_meta");
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 18. schema_export — export all schemas in a group (backup / migration)
// ─────────────────────────────────────────────────────────────────────────────
class SchemaExportTool extends BaseTool {

    private static final int MAX_EXPORT = 200;

    SchemaExportTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_export"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("group",       groupProp());
        p.set("include_content", strProp("'true' to include schema content (default: false — metadata only)"));
        return buildSchema(name(),
            "Export a summary of all schemas in a group. Use for backup inventory, " +
            "migration between dev/test/prod, or impact analysis. " +
            "Set include_content=true to get full schema definitions (slower).",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String  group          = resolveGroup(args);
        boolean includeContent = "true".equalsIgnoreCase(optString(args, "include_content", "false"));

        // List all subjects in the group
        ApicurioResponse listR = client.listSubjects(group);
        if (!listR.isSuccess()) return apicurioError(listR.statusCode(), listR.body(), "schema_export");

        JsonNode subjects = listR.body().has("artifacts")
                          ? listR.body().get("artifacts") : listR.body();

        ArrayNode exported = json.createArrayNode();
        int count = 0;

        if (subjects.isArray()) {
            for (JsonNode subj : subjects) {
                if (count >= MAX_EXPORT) {
                    break; // Safety cap
                }
                String id = subj.has("id") ? subj.get("id").asText() : subj.asText();
                ObjectNode entry = json.createObjectNode();
                entry.put("artifact_id", id);
                entry.set("meta",        subj);

                if (includeContent) {
                    ApicurioResponse sr = client.getSchema(group, id, "latest");
                    if (sr.isSuccess()) entry.set("schema", sr.body());
                    else entry.put("schema_error", "HTTP " + sr.statusCode());
                }
                exported.add(entry);
                count++;
            }
        }

        ObjectNode out = json.createObjectNode();
        out.put("group",            group);
        out.put("exported_count",   count);
        out.put("include_content",  includeContent);
        out.set("schemas",          exported);
        if (count >= MAX_EXPORT) {
            out.put("truncated", true);
            out.put("note", "Export truncated at " + MAX_EXPORT + " schemas. Use API pagination for full export.");
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// MULTI-TENANT GROUP MANAGEMENT
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 19. group_create
// ─────────────────────────────────────────────────────────────────────────────
class GroupCreateTool extends BaseTool {

    GroupCreateTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "group_create"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("group_id",    strProp("Unique group/tenant identifier (e.g. tenant-001, acme-corp)"));
        p.set("description", strProp("Group description — tenant name, purpose"));
        return buildSchema(name(),
            "Create a new schema group (tenant namespace). Each enterprise customer in Ecoskiller " +
            "gets an isolated group to manage schemas independently. " +
            "Schemas in one group are invisible to other groups — enforcing multi-tenant isolation.",
            p, "group_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String groupId = requireString(args, "group_id");
        String desc    = optString(args, "description", "");

        // Security: validate group ID
        if (groupId.contains("..") || groupId.contains("/") || groupId.contains("\\")) {
            return ToolResult.error("group_id contains illegal characters");
        }

        ApicurioResponse r = client.createGroup(groupId, desc);
        if (!r.isSuccess() && !r.isConflict()) {
            return apicurioError(r.statusCode(), r.body(), "group_create");
        }

        ObjectNode out = json.createObjectNode();
        out.put("success",     !r.isConflict());
        out.put("group_id",    groupId);
        out.put("description", desc);
        out.put("message", r.isConflict()
                ? "Group already exists"
                : "Group created — schemas in this group are isolated from other tenants");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 20. group_list
// ─────────────────────────────────────────────────────────────────────────────
class GroupListTool extends BaseTool {

    GroupListTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "group_list"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key", apiKeyProp());
        return buildSchema(name(),
            "List all schema groups (tenant namespaces) in the registry. " +
            "Returns group IDs and descriptions for all tenants.",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        ApicurioResponse r = client.listGroups();
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "group_list");

        JsonNode groups = r.body().has("groups") ? r.body().get("groups") : r.body();
        int count = groups.isArray() ? groups.size() : 0;

        ObjectNode out = json.createObjectNode();
        out.put("group_count", count);
        out.set("groups",      groups);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// SCHEMA REFERENCES
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 21. schema_get_references
// ─────────────────────────────────────────────────────────────────────────────
class SchemaGetReferencesTool extends BaseTool {

    SchemaGetReferencesTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_get_references"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("version",     versionProp());
        p.set("group",       groupProp());
        return buildSchema(name(),
            "Get all schemas that reference (depend on) this schema. " +
            "Critical for impact analysis — before deprecating a shared type " +
            "(e.g. CandidateProfile, Score), identify all schemas that include it.",
            p, "artifact_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String version    = optString(args, "version", "latest");
        String group      = resolveGroup(args);

        // Fetch outbound references (schemas that THIS schema depends on)
        String path = "/groups/" + group + "/artifacts/" + artifactId + "/versions/" + version + "/references";
        ApicurioResponse outR = client.get(path);

        // Also fetch who references this artifact (inbound — referredBy)
        String inPath = "/groups/" + group + "/artifacts/" + artifactId + "/versions/" + version
                      + "/references?refType=INBOUND";
        ApicurioResponse inR = client.get(inPath);

        ObjectNode out = json.createObjectNode();
        out.put("artifact_id", artifactId);
        out.put("group",       group);
        out.put("version",     version);

        if (outR.isSuccess()) {
            out.set("outbound_references", outR.body()); // schemas this one depends on
            out.put("outbound_count", outR.body().isArray() ? outR.body().size() : 0);
        } else {
            out.put("outbound_note", "Could not fetch outbound references (HTTP " + outR.statusCode() + ")");
        }

        if (inR.isSuccess()) {
            out.set("inbound_references", inR.body()); // schemas that depend on this one
            out.put("inbound_count", inR.body().isArray() ? inR.body().size() : 0);
        } else {
            out.put("inbound_note", "Could not fetch inbound references — check Apicurio version supports INBOUND ref type");
        }

        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ═════════════════════════════════════════════════════════════════════════════
// HEALTH & MONITORING
// ═════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// 22. registry_health
// ─────────────────────────────────────────────────────────────────────────────
class RegistryHealthTool extends BaseTool {

    RegistryHealthTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "registry_health"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key", apiKeyProp());
        return buildSchema(name(),
            "Check Apicurio Schema Registry health: version, uptime, storage backend, connectivity. " +
            "Also validates MCP server → Apicurio connection. Returns overall healthy=true/false.",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        long start = System.currentTimeMillis();
        ApicurioResponse sysInfo = client.health();
        long latencyMs = System.currentTimeMillis() - start;

        ObjectNode out = json.createObjectNode();

        if (!sysInfo.isSuccess()) {
            out.put("healthy",          false);
            out.put("connection",       false);
            out.put("apicurio_url",     cfg.apicurioBaseUrl);
            out.put("error",            "Cannot reach Apicurio — HTTP " + sysInfo.statusCode());
            out.put("latency_ms",       latencyMs);
            return ToolResult.ok(json.writeValueAsString(out));
        }

        JsonNode info = sysInfo.body();
        out.put("healthy",          true);
        out.put("connection",       true);
        out.put("apicurio_url",     cfg.apicurioBaseUrl);
        out.put("latency_ms",       latencyMs);
        out.put("apicurio_version", info.path("version").asText(info.path("name").asText("unknown")));
        out.put("build_time",       info.path("builtOn").asText(""));
        out.put("default_group",    cfg.defaultGroup);
        out.put("default_compat",   cfg.defaultCompatibility);
        out.put("auth_type",        cfg.authType);
        out.put("tls_verify",       cfg.tlsVerify);

        // Alert thresholds (from spec section 10.3)
        ObjectNode alerts = json.createObjectNode();
        alerts.put("high_latency", latencyMs > 50);
        out.set("alerts", alerts);
        out.put("message", latencyMs > 50
                ? "Registry reachable but latency " + latencyMs + "ms exceeds 50ms threshold"
                : "Registry healthy — " + latencyMs + "ms");

        return ToolResult.ok(json.writeValueAsString(out));
    }
}
