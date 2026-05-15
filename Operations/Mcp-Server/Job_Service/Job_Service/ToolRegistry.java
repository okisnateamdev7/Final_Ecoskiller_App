package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.json.JsonValue;
import com.ecoskiller.mcp.json.JsonValue.*;
import com.ecoskiller.mcp.security.SecurityManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * ToolRegistry — registers all 16 Job Service tools and routes calls.
 */
public class ToolRegistry {

    private final Map<String, ToolEntry> tools = new LinkedHashMap<>();
    private final JobToolHandlers        handlers;

    public ToolRegistry(SecurityManager security) {
        this.handlers = new JobToolHandlers(security);
        registerAll();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Registration
    // ─────────────────────────────────────────────────────────────────────
    private void registerAll() {

        reg("job_create",
            "Create a new job posting (DRAFT). Recruiter role required. Emits job.created Kafka event.",
            schema(
                req("title",        "string",  "Job title, max 200 chars"),
                req("domain",       "string",  "Arts|Commerce|Science|Technology|Administration"),
                req("description",  "string",  "Full job description"),
                req("tenant_id",    "string",  "Tenant UUID"),
                req("recruiter_id", "string",  "Recruiter UUID"),
                opt("skills",       "array",   "Required skill tags"),
                opt("salary_min",   "integer", "Min monthly salary (INR)"),
                opt("salary_max",   "integer", "Max monthly salary (INR)"),
                opt("location",     "string",  "Location or 'Remote'"),
                opt("visibility",   "string",  "PUBLIC|TENANT_ONLY|RECRUITER"),
                req("bearer_token", "string",  "Keycloak JWT")
            ), handlers::jobCreate);

        reg("job_get",
            "Get a single job by ID. Enforces domain + tenant isolation. Candidate role and above.",
            schema(
                req("job_id",       "string", "Job UUID"),
                req("tenant_id",    "string", "Tenant UUID"),
                req("domain",       "string", "Domain scope"),
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobGet);

        reg("job_update",
            "Update job fields with optimistic concurrency (CAS on version). Recruiter role.",
            schema(
                req("job_id",       "string",  "Job UUID"),
                req("tenant_id",    "string",  "Tenant UUID"),
                req("version",      "integer", "Optimistic lock version"),
                opt("title",        "string",  "Updated title"),
                opt("description",  "string",  "Updated description"),
                opt("skills",       "array",   "Updated skills"),
                opt("salary_min",   "integer", "Updated min salary"),
                opt("salary_max",   "integer", "Updated max salary"),
                opt("location",     "string",  "Updated location"),
                req("bearer_token", "string",  "Keycloak JWT")
            ), handlers::jobUpdate);

        reg("job_deactivate",
            "Soft-deactivate a job (CLOSED). Emits job.deleted Kafka event. Recruiter role.",
            schema(
                req("job_id",       "string", "Job UUID"),
                req("tenant_id",    "string", "Tenant UUID"),
                req("reason",       "string", "Deactivation reason"),
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobDeactivate);

        reg("job_list",
            "List jobs with domain/status/pagination filters. Candidate role and above.",
            schema(
                req("tenant_id",    "string",  "Tenant UUID"),
                req("domain",       "string",  "Domain filter"),
                opt("status",       "string",  "DRAFT|PUBLISHED|CLOSED|ARCHIVED"),
                opt("page",         "integer", "Page number (0-based)"),
                opt("page_size",    "integer", "Page size (max 100)"),
                opt("recruiter_id", "string",  "Filter by recruiter UUID"),
                req("bearer_token", "string",  "Keycloak JWT")
            ), handlers::jobList);

        reg("job_bulk_import",
            "Atomic bulk import: all succeed or all rollback. Recruiter role.",
            schema(
                req("tenant_id",    "string", "Tenant UUID"),
                req("domain",       "string", "Domain for all jobs"),
                req("jobs",         "array",  "Array of job objects (max 500)"),
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobBulkImport);

        reg("job_moderate",
            "Approve or reject a MODERATION_REQUIRED job. ADMIN ONLY.",
            schema(
                req("job_id",           "string", "Job UUID"),
                req("action",           "string", "APPROVE|REJECT"),
                req("moderator_id",     "string", "Admin user UUID"),
                opt("rejection_reason", "string", "Required when action=REJECT"),
                req("bearer_token",     "string", "Keycloak JWT")
            ), handlers::jobModerate);

        reg("job_visibility_update",
            "Update job visibility scope. Recruiter role.",
            schema(
                req("job_id",       "string", "Job UUID"),
                req("tenant_id",    "string", "Tenant UUID"),
                req("visibility",   "string", "PUBLIC|TENANT_ONLY|RECRUITER"),
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobVisibilityUpdate);

        reg("job_salary_band_validate",
            "Validate salary against domain market-rate band. Admin role.",
            schema(
                req("domain",       "string",  "Domain to check"),
                req("salary_min",   "integer", "Minimum salary to validate"),
                req("salary_max",   "integer", "Maximum salary to validate"),
                req("bearer_token", "string",  "Keycloak JWT")
            ), handlers::jobSalaryBandValidate);

        reg("job_seo_generate",
            "Generate SEO metadata (title/description/keywords/OpenGraph) for React web listing. Recruiter role.",
            schema(
                req("job_id",       "string", "Job UUID"),
                req("tenant_id",    "string", "Tenant UUID"),
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobSeoGenerate);

        reg("job_search_fulltext",
            "Full-text search across PUBLISHED jobs (OpenSearch-backed in production). Candidate role.",
            schema(
                req("query",        "string",  "Search query"),
                req("tenant_id",    "string",  "Tenant UUID"),
                opt("domain",       "string",  "Optional domain filter"),
                opt("page",         "integer", "Page number"),
                opt("page_size",    "integer", "Page size (max 100)"),
                req("bearer_token", "string",  "Keycloak JWT")
            ), handlers::jobSearchFulltext);

        reg("job_status_transition",
            "Transition job status through the enforced state machine. Recruiter role.",
            schema(
                req("job_id",        "string", "Job UUID"),
                req("tenant_id",     "string", "Tenant UUID"),
                req("target_status", "string", "Target: PUBLISHED|CLOSED|ARCHIVED|MODERATION_REQUIRED"),
                opt("reason",        "string", "Transition reason"),
                req("bearer_token",  "string", "Keycloak JWT")
            ), handlers::jobStatusTransition);

        reg("job_audit_trail",
            "Retrieve immutable audit log for a job. ADMIN ONLY.",
            schema(
                req("job_id",       "string",  "Job UUID"),
                req("tenant_id",    "string",  "Tenant UUID"),
                opt("page",         "integer", "Page number"),
                opt("page_size",    "integer", "Page size (max 50)"),
                req("bearer_token", "string",  "Keycloak JWT")
            ), handlers::jobAuditTrail);

        reg("job_domain_stats",
            "Aggregate job counts per domain and status. ADMIN ONLY.",
            schema(
                req("tenant_id",    "string", "Tenant UUID"),
                opt("domain",       "string", "Optional domain filter"),
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobDomainStats);

        reg("job_health",
            "Health check for all dependencies: PostgreSQL, Redis, Kafka, OpenSearch. Service role.",
            schema(
                req("bearer_token", "string", "Keycloak JWT (service account)")
            ), handlers::jobHealth);

        reg("job_schema",
            "Return JSON schema + state machine reference for a Job object. Candidate role.",
            schema(
                req("bearer_token", "string", "Keycloak JWT")
            ), handlers::jobSchema);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Public interface
    // ─────────────────────────────────────────────────────────────────────

    public JsonArray getToolSchemas() {
        JsonArray arr = JsonValue.array();
        for (ToolEntry e : tools.values()) arr.add(e.schema());
        return arr;
    }

    public JsonObject call(String name, JsonObject args) {
        ToolEntry e = tools.get(name);
        if (e == null) throw new IllegalArgumentException("Unknown tool: " + name);
        return e.handler().apply(name, args);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Schema DSL
    // ─────────────────────────────────────────────────────────────────────

    private void reg(String name, String desc, JsonObject schema,
                     BiFunction<String, JsonObject, JsonObject> handler) {
        JsonObject tool = JsonValue.object()
            .put("name", name)
            .put("description", desc)
            .set("inputSchema", schema);
        tools.put(name, new ToolEntry(tool, handler));
    }

    private JsonObject schema(PropDef... props) {
        JsonObject properties = JsonValue.object();
        JsonArray  required   = JsonValue.array();
        for (PropDef p : props) {
            JsonObject prop = JsonValue.object()
                .put("type", p.type())
                .put("description", p.desc());
            properties.set(p.name(), prop);
            if (p.required()) required.add(p.name());
        }
        return JsonValue.object()
            .put("type", "object")
            .set("properties", properties)
            .set("required", required);
    }

    private PropDef req(String n, String t, String d) { return new PropDef(n, t, d, true); }
    private PropDef opt(String n, String t, String d) { return new PropDef(n, t, d, false); }

    private record PropDef(String name, String type, String desc, boolean required) {}
    private record ToolEntry(JsonObject schema, BiFunction<String, JsonObject, JsonObject> handler) {}
}
