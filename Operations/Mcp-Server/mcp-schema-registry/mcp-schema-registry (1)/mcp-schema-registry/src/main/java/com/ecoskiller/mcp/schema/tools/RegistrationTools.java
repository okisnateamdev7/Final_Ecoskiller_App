package com.ecoskiller.mcp.schema.tools;

import com.ecoskiller.mcp.schema.client.ApicurioClient;
import com.ecoskiller.mcp.schema.client.ApicurioClient.ApicurioResponse;
import com.ecoskiller.mcp.schema.config.SchemaRegistryConfig;
import com.ecoskiller.mcp.schema.security.AuditLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Schema Registration & Versioning Tools (5)
 *
 *  1. schema_register       — Register a new schema / new version
 *  2. schema_get            — Retrieve schema content by artifact + version
 *  3. schema_get_by_id      — Retrieve schema by global content ID
 *  4. schema_list_versions  — List all versions of a schema subject
 *  5. schema_list_subjects  — List all registered subjects (artifact IDs)
 */

// ─────────────────────────────────────────────────────────────────────────────
// 1. schema_register
// ─────────────────────────────────────────────────────────────────────────────
class SchemaRegisterTool extends BaseTool {

    SchemaRegisterTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_register"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",        apiKeyProp());
        p.set("artifact_id",    artifactIdProp());
        p.set("schema_content", strProp("Schema definition — Avro (.avsc) JSON string, JSON Schema, or Protobuf text"));
        p.set("artifact_type",  enumProp("Schema format", "AVRO","JSON","PROTOBUF","OPENAPI","ASYNCAPI"));
        p.set("group",          groupProp());
        p.set("if_exists",      enumProp("Action if artifact already exists: FAIL (default), UPDATE, RETURN, RETURN_OR_UPDATE",
                                          "FAIL","UPDATE","RETURN","RETURN_OR_UPDATE"));
        p.set("name",           strProp("Human-readable display name for the schema"));
        p.set("description",    strProp("Schema description — purpose, domain, owner team"));
        return buildSchema(name(),
            "Register a new schema (artifact) or add a new version to an existing one. " +
            "Compatibility rules are enforced automatically — incompatible schemas are rejected. " +
            "Returns schema_id and version number on success.",
            p, "artifact_id", "schema_content", "artifact_type");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String content    = requireString(args, "schema_content");
        String type       = validateType(requireString(args, "artifact_type"));
        String group      = resolveGroup(args);
        String ifExists   = optString(args, "if_exists", "RETURN_OR_UPDATE").toUpperCase();

        // First: try to update meta if name/description provided
        String displayName = optString(args, "name", "");
        String description = optString(args, "description", "");

        ApicurioResponse r = client.registerSchema(group, artifactId, content, type, ifExists);

        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_register");

        ObjectNode out = json.createObjectNode();
        out.put("success",      true);
        out.put("artifact_id",  artifactId);
        out.put("group",        group);
        out.put("artifact_type",type);
        out.put("global_id",    r.body().path("globalId").asLong(-1));
        out.put("content_id",   r.body().path("contentId").asLong(-1));
        out.put("version",      r.body().path("version").asText("1"));
        out.put("state",        r.body().path("state").asText("ENABLED"));
        out.put("created_on",   r.body().path("createdOn").asText(""));
        if (!displayName.isEmpty() || !description.isEmpty()) {
            ObjectNode meta = json.createObjectNode();
            if (!displayName.isEmpty()) meta.put("name", displayName);
            if (!description.isEmpty()) meta.put("description", description);
            client.updateMeta(group, artifactId, json.writeValueAsString(meta));
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. schema_get
// ─────────────────────────────────────────────────────────────────────────────
class SchemaGetTool extends BaseTool {

    SchemaGetTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_get"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("version",     versionProp());
        p.set("group",       groupProp());
        p.set("include_meta",strProp("true to include metadata (name, description, state, timestamps)"));
        return buildSchema(name(),
            "Retrieve a schema definition by artifact ID and version. " +
            "Used by Kafka consumers to fetch schema for deserialization. Returns the raw schema content.",
            p, "artifact_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String version    = optString(args, "version", "latest");
        String group      = resolveGroup(args);
        boolean withMeta  = "true".equalsIgnoreCase(optString(args, "include_meta", "false"));

        ApicurioResponse r = client.getSchema(group, artifactId, version);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_get");

        ObjectNode out = json.createObjectNode();
        out.put("artifact_id", artifactId);
        out.put("group",       group);
        out.put("version",     version);
        out.set("schema",      r.body());

        if (withMeta) {
            ApicurioResponse meta = client.getArtifactMeta(group, artifactId);
            if (meta.isSuccess()) out.set("meta", meta.body());
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. schema_get_by_id — fetch schema by global content ID (used in Kafka headers)
// ─────────────────────────────────────────────────────────────────────────────
class SchemaGetByIdTool extends BaseTool {

    SchemaGetByIdTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_get_by_id"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",    apiKeyProp());
        p.set("content_id", intProp("Global content ID embedded in Kafka message headers by Avro serializer"));
        return buildSchema(name(),
            "Retrieve a schema by its global content ID — the ID embedded in Kafka message headers. " +
            "This is the primary lookup path for Kafka consumers during deserialization.",
            p, "content_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        long contentId = args.path("content_id").asLong(-1);
        if (contentId < 0) return ToolResult.error("content_id must be a positive integer");

        ApicurioResponse r = client.get("/ids/contentIds/" + contentId);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_get_by_id");

        ObjectNode out = json.createObjectNode();
        out.put("content_id", contentId);
        out.set("schema",     r.body());
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. schema_list_versions
// ─────────────────────────────────────────────────────────────────────────────
class SchemaListVersionsTool extends BaseTool {

    SchemaListVersionsTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_list_versions"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key",     apiKeyProp());
        p.set("artifact_id", artifactIdProp());
        p.set("group",       groupProp());
        return buildSchema(name(),
            "List all versions of a schema subject. Returns version numbers, global IDs, and state for each. " +
            "Use for schema lineage analysis and impact assessment.",
            p, "artifact_id");
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String artifactId = sanitiseArtifactId(requireString(args, "artifact_id"));
        String group      = resolveGroup(args);

        ApicurioResponse r = client.listVersions(group, artifactId);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_list_versions");

        int count = r.body().isArray() ? r.body().size() : 0;
        ObjectNode out = json.createObjectNode();
        out.put("artifact_id",     artifactId);
        out.put("group",           group);
        out.put("version_count",   count);
        out.put("warn_too_many",   count > cfg.maxVersionsWarn);
        out.set("versions",        r.body());
        if (count > cfg.maxVersionsWarn) {
            out.put("warning", "Version count (" + count + ") exceeds threshold (" + cfg.maxVersionsWarn + "). Consider schema cleanup.");
        }
        return ToolResult.ok(json.writeValueAsString(out));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. schema_list_subjects
// ─────────────────────────────────────────────────────────────────────────────
class SchemaListSubjectsTool extends BaseTool {

    SchemaListSubjectsTool(ApicurioClient c, SchemaRegistryConfig cfg, AuditLogger a) { super(c,cfg,a); }
    @Override public String name() { return "schema_list_subjects"; }

    @Override public ObjectNode schema() {
        ObjectNode p = json.createObjectNode();
        p.set("api_key", apiKeyProp());
        p.set("group",   groupProp());
        return buildSchema(name(),
            "List all registered schema subjects (artifact IDs) in a group/namespace. " +
            "Returns summary of all Kafka topic schemas registered in Ecoskiller.",
            p);
    }

    @Override public ToolResult execute(JsonNode args) throws Exception {
        String group = resolveGroup(args);

        ApicurioResponse r = client.listSubjects(group);
        if (!r.isSuccess()) return apicurioError(r.statusCode(), r.body(), "schema_list_subjects");

        // Apicurio returns { artifacts: [...] }
        JsonNode artifacts = r.body().has("artifacts") ? r.body().get("artifacts") : r.body();
        int count = artifacts.isArray() ? artifacts.size() : 0;

        ObjectNode out = json.createObjectNode();
        out.put("group",          group);
        out.put("subject_count",  count);
        out.set("subjects",       artifacts);
        return ToolResult.ok(json.writeValueAsString(out));
    }
}
