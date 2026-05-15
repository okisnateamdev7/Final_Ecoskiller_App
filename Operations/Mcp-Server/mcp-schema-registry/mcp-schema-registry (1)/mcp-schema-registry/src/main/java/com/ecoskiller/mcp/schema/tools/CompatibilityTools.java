package com.ecoskiller.mcp.schema.tools;

import com.ecoskiller.mcp.schema.client.ApicurioClient;
import com.ecoskiller.mcp.schema.client.ApicurioClient.ApicurioResponse;
import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.ecoskiller.mcp.schema.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Compatibility Management Tools (4)
 *
 *  6.  compatibility_get      — Get the compatibility mode for a schema
 *  7.  compatibility_set      — Set the compatibility mode for a schema
 *  8.  compatibility_test     — Dry-run test: is this new schema compatible?
 *  9.  global_rule_set        — Set a global compatibility rule (all schemas)
 */

// ─────────────────────────────────────────────────────────────────────────────
// 6. compatibility_get
// ─────────────────────────────────────────────────────────────────────────────
class CompatibilityGetTool extends BaseTool {

    CompatibilityGetTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "compatibility_get"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("group",       groupProp());
        return buildSchema(name(),
            "Get the current compatibility mode configured for a schema subject. " +
            "Returns BACKWARD, FORWARD, FULL, BACKWARD_TRANSITIVE, FORWARD_TRANSITIVE, FULL_TRANSITIVE, or NONE.",
            p, "artifact_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String group      = resolveGroup(args);

        ApicurioResponse r = client.getCompatibilityRule(group, artifactId);

        ObjectNode out = json.createObjectNode();
        out.put("artifact_id", artifactId);
        out.put("group",       group);

        if (r.isNotFound()) {
            // No artifact-level rule set — global default applies
            out.put("compatibility",    cfg.defaultCompatibility);
            out.put("level",            "global_default");
            out.put("note", "No per-artifact rule found. Global default '" + cfg.defaultCompatibility + "' applies.");
        } else if (!r.isSuccess()) {
            return apicurioError(r.statusCode(), r.body(), "compatibility_get");
        } else {
            out.put("compatibility", r.body().path("config").asText(cfg.defaultCompatibility));
            out.put("level",         "artifact");
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 7. compatibility_set
// ─────────────────────────────────────────────────────────────────────────────
class CompatibilitySetTool extends BaseTool {

    CompatibilitySetTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "compatibility_set"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",       apiKeyProp());
        p.set("artifact_id",   artifactIdProp());
        p.set("compatibility", enumProp(
            "Compatibility mode to enforce:\n" +
            "  BACKWARD — new schema readable by consumers of old schema (safe producer evolution)\n" +
            "  FORWARD — old schema readable by consumers of new schema (safe consumer evolution)\n" +
            "  FULL — both BACKWARD and FORWARD\n" +
            "  BACKWARD_TRANSITIVE / FORWARD_TRANSITIVE / FULL_TRANSITIVE — check all prior versions\n" +
            "  NONE — no compatibility check",
            "BACKWARD","FORWARD","FULL","BACKWARD_TRANSITIVE","FORWARD_TRANSITIVE","FULL_TRANSITIVE","NONE"));
        p.set("group", groupProp());
        return buildSchema(name(),
            "Set the compatibility mode for a specific schema subject. " +
            "Ecoskiller default is BACKWARD — ensures all consumers can still read new messages. " +
            "Use FULL for maximum flexibility in teams evolving independently.",
            p, "artifact_id", "compatibility");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String compat     = validateCompat(requireString(args, "compatibility"));
        String group      = resolveGroup(args);

        ApicurioResponse r = client.setCompatibilityRule(group, artifactId, compat);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "compatibility_set");

        ObjectNode out = json.createObjectNode();
        out.put("success",       true);
        out.put("artifact_id",   artifactId);
        out.put("group",         group);
        out.put("compatibility", compat);
        out.put("message",       "Compatibility mode set to " + compat + " for " + artifactId);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 8. compatibility_test — dry-run compatibility check
// ─────────────────────────────────────────────────────────────────────────────
class CompatibilityTestTool extends BaseTool {

    CompatibilityTestTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "compatibility_test"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("artifact_id",    artifactIdProp());
        p.set("new_schema",     strProp("Proposed new schema content to test for compatibility"));
        p.set("artifact_type",  enumProp("Schema format", "AVRO","JSON","PROTOBUF","OPENAPI","ASYNCAPI"));
        p.set("against_version",strProp("Version to test against (default: latest)"));
        p.set("group",          groupProp());
        return buildSchema(name(),
            "Dry-run compatibility test: check if a proposed new schema is compatible with existing versions " +
            "WITHOUT registering it. Use before deploying a producer with a schema change. " +
            "Returns compatible=true/false with detailed incompatibility reasons.",
            p, "artifact_id", "new_schema", "artifact_type");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String newSchema  = requireString(args, "new_schema");
        String type       = validateType(requireString(args, "artifact_type"));
        String version    = optString(args, "against_version", "latest");
        String group      = resolveGroup(args);

        ApicurioResponse r = client.testCompatibility(group, artifactId, version, newSchema, type);

        ObjectNode out = json.createObjectNode();
        out.put("artifact_id",      artifactId);
        out.put("group",            group);
        out.put("against_version",  version);
        out.put("artifact_type",    type);

        if (r.statusCode == 200) {
            out.put("compatible", true);
            out.put("message",    "Schema is compatible — safe to register");
        } else if (r.statusCode == 409) {
            out.put("compatible", false);
            out.put("message",    "Schema is NOT compatible — registration would be blocked");
            if (r.body().has("causes")) out.set("incompatibility_reasons", r.body().get("causes"));
            if (r.body().has("message")) out.put("detail", r.body().get("message").asText());
        } else {
            return apicurioError(r.statusCode(), r.body(), "compatibility_test");
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 9. global_rule_set — set the global compatibility rule (applies to all schemas)
// ─────────────────────────────────────────────────────────────────────────────
class GlobalRuleSetTool extends BaseTool {

    GlobalRuleSetTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "global_rule_set"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",       apiKeyProp());
        p.set("rule_type",     enumProp("Rule type to configure", "COMPATIBILITY","VALIDITY","INTEGRITY"));
        p.set("config",        strProp("Rule configuration value (e.g. BACKWARD, FULL, SYNTAX_ONLY, FULL_SCHEMA)"));
        return buildSchema(name(),
            "Set a global registry rule that applies to ALL schemas by default. " +
            "Per-artifact rules override global rules. Admin operation — requires elevated privileges.",
            p, "rule_type", "config");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String ruleType = requireString(args, "rule_type").toUpperCase();
        String config   = requireString(args, "config").toUpperCase();

        // Validate COMPATIBILITY config values
        if ("COMPATIBILITY".equals(ruleType) && !VALID_COMPAT.contains(config)) {
            return ToolResult.error("Invalid COMPATIBILITY config: " + config + ". Must be one of: " + VALID_COMPAT);
        }

        ApicurioResponse r = client.setGlobalRule(ruleType, config);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "global_rule_set");

        ObjectNode out = json.createObjectNode();
        out.put("success",   true);
        out.put("rule_type", ruleType);
        out.put("config",    config);
        out.put("scope",     "global — applies to all schemas without per-artifact overrides");
        return ToolResult.ok(json.writeValueAsString(out));
    }
}
