package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.json.JsonValue;
import com.ecoskiller.mcp.json.JsonValue.*;
import com.ecoskiller.mcp.model.JobDomain;
import com.ecoskiller.mcp.model.JobDomain.*;
import com.ecoskiller.mcp.security.SecurityManager;

import java.time.Instant;
import java.util.*;

/**
 * JobToolHandlers — implements all 16 Job Service tools.
 *
 * In-memory stores (jobStore, auditStore) simulate the production stack:
 *   PostgreSQL  → jobStore (CRUD + audit_logs table)
 *   Redis       → cache invalidation (not simulated)
 *   Kafka       → kafka_event field in every response
 *   OpenSearch  → simple substring search in job_search_fulltext
 *
 * Replace each method body's data access with real DB/cache calls in production.
 */
public class JobToolHandlers {

    private final SecurityManager security;

    // Simulates PostgreSQL jobs table
    private final Map<String, JsonObject>        jobStore   = new LinkedHashMap<>();
    // Simulates PostgreSQL audit_logs table (immutable append-only)
    private final Map<String, List<JsonObject>>  auditStore = new LinkedHashMap<>();

    public JobToolHandlers(SecurityManager security) {
        this.security = security;
    }

    // ══════════════════════════════════════════════════════════════════════
    // 1 — job_create
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobCreate(String tool, JsonObject a) {
        String title       = security.sanitise(a.require("title"),       "title");
        String domain      = a.require("domain");
        String description = security.sanitise(a.require("description"), "description");
        String tenantId    = a.require("tenant_id");
        String recruiterId = security.sanitise(a.require("recruiter_id"),"recruiter_id");

        JobDomain.requireDomain(domain);
        if (title.length() > 200)
            throw new IllegalArgumentException("title exceeds 200 characters");

        long sMin = a.getLong("salary_min");
        long sMax = a.getLong("salary_max");
        if (sMin > 0 && sMax > 0) warnSalaryBand(domain, sMin, sMax);

        String vis = a.has("visibility") ? a.getString("visibility").toUpperCase() : "PUBLIC";
        validateVisibility(vis);

        String now   = Instant.now().toString();
        String jobId = UUID.randomUUID().toString();

        JsonObject job = JsonValue.object()
            .put("job_id",      jobId)
            .put("title",       title)
            .put("domain",      domain)
            .put("description", description)
            .put("tenant_id",   tenantId)
            .put("recruiter_id",recruiterId)
            .put("status",      Status.DRAFT.name())
            .put("visibility",  vis)
            .put("version",     1)
            .put("created_at",  now)
            .put("updated_at",  now);

        if (sMin > 0) job.put("salary_min", sMin);
        if (sMax > 0) job.put("salary_max", sMax);
        if (a.has("location")) job.put("location", security.sanitise(a.getString("location"),"location"));
        if (a.has("skills") && a.getArray("skills") != null) job.set("skills", a.getArray("skills"));

        jobStore.put(jobId, job);
        audit(jobId, tenantId, "CREATE", recruiterId, null, job);

        return JsonValue.object()
            .put("success",    true)
            .put("job_id",     jobId)
            .put("status",     Status.DRAFT.name())
            .put("kafka_event","job.created → jobs." + domain)
            .set("job",        clone(job));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2 — job_get
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobGet(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");
        String domain   = a.require("domain");
        JobDomain.requireDomain(domain);

        JsonObject job = jobStore.get(jobId);
        if (job == null
            || !domain.equals(job.getString("domain"))
            || !tenantId.equals(job.getString("tenant_id")))
            return notFound("job_id", jobId);

        return JsonValue.object().put("success", true).set("job", clone(job));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3 — job_update
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobUpdate(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");
        int    version  = a.getInt("version");

        JsonObject job = jobStore.get(jobId);
        if (job == null || !tenantId.equals(job.getString("tenant_id")))
            return notFound("job_id", jobId);

        if (job.getInt("version") != version)
            return JsonValue.object()
                .put("success", false)
                .put("error",   "Optimistic lock conflict: version mismatch — re-fetch and retry")
                .put("current_version", job.getInt("version"));

        String status = job.getString("status");
        if (!status.equals("DRAFT") && !status.equals("MODERATION_REQUIRED"))
            throw new IllegalArgumentException("Only DRAFT/MODERATION_REQUIRED jobs can be updated");

        JsonObject prev = clone(job);

        if (a.has("title"))       job.put("title",       security.sanitise(a.getString("title"),       "title"));
        if (a.has("description")) job.put("description", security.sanitise(a.getString("description"), "description"));
        if (a.has("skills") && a.getArray("skills") != null)
            job.set("skills",  a.getArray("skills"));
        if (a.getLong("salary_min") > 0) job.put("salary_min", a.getLong("salary_min"));
        if (a.getLong("salary_max") > 0) job.put("salary_max", a.getLong("salary_max"));
        if (a.has("location")) job.put("location", security.sanitise(a.getString("location"), "location"));

        int newVer = version + 1;
        job.put("version",    newVer);
        job.put("updated_at", Instant.now().toString());

        audit(jobId, tenantId, "UPDATE", "system", prev, job);

        return JsonValue.object()
            .put("success",     true)
            .put("job_id",      jobId)
            .put("new_version", newVer)
            .put("kafka_event", "job.updated → jobs." + job.getString("domain"))
            .set("job",         clone(job));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4 — job_deactivate
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobDeactivate(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");
        String reason   = security.sanitise(a.require("reason"), "reason");

        JsonObject job = jobStore.get(jobId);
        if (job == null || !tenantId.equals(job.getString("tenant_id")))
            return notFound("job_id", jobId);

        if ("ARCHIVED".equals(job.getString("status")))
            throw new IllegalArgumentException("Job is already in terminal ARCHIVED state");

        job.put("status",       "CLOSED")
           .put("closed_at",    Instant.now().toString())
           .put("close_reason", reason);

        audit(jobId, tenantId, "DEACTIVATE", "system", null, job);

        return JsonValue.object()
            .put("success",     true)
            .put("job_id",      jobId)
            .put("status",      "CLOSED")
            .put("kafka_event", "job.deleted → jobs." + job.getString("domain"));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 5 — job_list
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobList(String tool, JsonObject a) {
        String tenantId = a.require("tenant_id");
        String domain   = a.require("domain");
        JobDomain.requireDomain(domain);

        String statusF = a.has("status")       ? a.getString("status")       : "";
        String recF    = a.has("recruiter_id") ? a.getString("recruiter_id") : "";
        int    page    = a.getInt("page") > 0  ? a.getInt("page")            : 0;
        int    size    = a.getInt("page_size") > 0 ? Math.min(a.getInt("page_size"), 100) : 20;

        List<JsonObject> filtered = new ArrayList<>();
        for (JsonObject job : jobStore.values()) {
            if (!tenantId.equals(job.getString("tenant_id")))  continue;
            if (!domain.equals(job.getString("domain")))       continue;
            if (!statusF.isBlank() && !statusF.equals(job.getString("status"))) continue;
            if (!recF.isBlank()    && !recF.equals(job.getString("recruiter_id"))) continue;
            filtered.add(clone(job));
        }

        int total = filtered.size();
        int from  = Math.min(page * size, total);
        int to    = Math.min(from + size,  total);

        JsonArray items = JsonValue.array();
        for (JsonObject j : filtered.subList(from, to)) items.add(j);

        return JsonValue.object()
            .put("success",   true)
            .put("total",     total)
            .put("page",      page)
            .put("page_size", size)
            .set("jobs",      items);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 6 — job_bulk_import
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobBulkImport(String tool, JsonObject a) {
        String   tenantId = a.require("tenant_id");
        String   domain   = a.require("domain");
        JsonArray jobs    = a.getArray("jobs");
        JobDomain.requireDomain(domain);

        if (jobs == null || jobs.size() == 0)
            throw new IllegalArgumentException("'jobs' must be a non-empty array");
        if (jobs.size() > 500)
            throw new IllegalArgumentException("Bulk import limit is 500 jobs per request");

        // Validate all first (atomic: all-or-nothing)
        List<String> errors = new ArrayList<>();
        for (int i = 0; i < jobs.size(); i++) {
            JsonObject j = (JsonObject) jobs.get(i);
            String t = j.getString("title");
            if (t == null || t.isBlank()) errors.add("Job[" + i + "]: missing 'title'");
        }
        if (!errors.isEmpty()) {
            JsonArray ea = JsonValue.array();
            errors.forEach(ea::add);
            return JsonValue.object()
                .put("success", false)
                .put("error",   "Validation failed — atomic rollback, no jobs imported")
                .set("validation_errors", ea);
        }

        // Import all
        String now = Instant.now().toString();
        JsonArray ids = JsonValue.array();
        for (JsonValue jv : jobs.items()) {
            JsonObject j = (JsonObject) jv;
            String jobId = UUID.randomUUID().toString();
            JsonObject job = JsonValue.object()
                .put("job_id",      jobId)
                .put("title",       security.sanitise(j.getString("title"), "title"))
                .put("domain",      domain)
                .put("description", j.has("description") ? security.sanitise(j.getString("description"),"description") : "")
                .put("tenant_id",   tenantId)
                .put("recruiter_id",j.has("recruiter_id") ? j.getString("recruiter_id") : "bulk-import")
                .put("status",      Status.DRAFT.name())
                .put("visibility",  "TENANT_ONLY")
                .put("version",     1)
                .put("created_at",  now)
                .put("updated_at",  now)
                .put("bulk_import", true);
            jobStore.put(jobId, job);
            ids.add(jobId);
        }

        return JsonValue.object()
            .put("success",        true)
            .put("imported_count", ids.size())
            .put("kafka_event",    ids.size() + " × job.created → jobs." + domain)
            .set("job_ids",        ids);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 7 — job_moderate
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobModerate(String tool, JsonObject a) {
        String jobId       = a.require("job_id");
        String action      = a.require("action").toUpperCase();
        String moderatorId = security.sanitise(a.require("moderator_id"), "moderator_id");

        if (!action.equals("APPROVE") && !action.equals("REJECT"))
            throw new IllegalArgumentException("action must be APPROVE or REJECT");
        if (action.equals("REJECT")) {
            String rr = a.getString("rejection_reason");
            if (rr == null || rr.isBlank())
                throw new IllegalArgumentException("rejection_reason is required when action=REJECT");
        }

        JsonObject job = jobStore.get(jobId);
        if (job == null) return notFound("job_id", jobId);

        if (!"MODERATION_REQUIRED".equals(job.getString("status")))
            throw new IllegalArgumentException(
                "Job is not in MODERATION_REQUIRED state. Current: " + job.getString("status"));

        String newStatus = action.equals("APPROVE") ? "PUBLISHED" : "REJECTED";
        job.put("status",       newStatus)
           .put("moderated_by", moderatorId)
           .put("moderated_at", Instant.now().toString());
        if (action.equals("REJECT"))
            job.put("rejection_reason", security.sanitise(a.getString("rejection_reason"), "rejection_reason"));

        audit(jobId, job.getString("tenant_id"), "MODERATE_" + action, moderatorId, null, job);

        String event = action.equals("APPROVE") ? "job.approved" : "job.rejected";
        return JsonValue.object()
            .put("success",     true)
            .put("job_id",      jobId)
            .put("new_status",  newStatus)
            .put("kafka_event", event + " → jobs." + job.getString("domain"));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 8 — job_visibility_update
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobVisibilityUpdate(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");
        String vis      = a.require("visibility").toUpperCase();
        validateVisibility(vis);

        JsonObject job = jobStore.get(jobId);
        if (job == null || !tenantId.equals(job.getString("tenant_id")))
            return notFound("job_id", jobId);

        String old = job.getString("visibility");
        job.put("visibility", vis).put("updated_at", Instant.now().toString());

        return JsonValue.object()
            .put("success",        true)
            .put("job_id",         jobId)
            .put("old_visibility", old)
            .put("new_visibility", vis);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 9 — job_salary_band_validate
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobSalaryBandValidate(String tool, JsonObject a) {
        String domain = a.require("domain");
        long   sMin   = a.getLong("salary_min");
        long   sMax   = a.getLong("salary_max");

        JobDomain.requireDomain(domain);
        JobDomain.requirePositive(sMin, "salary_min");
        JobDomain.requirePositive(sMax, "salary_max");
        if (sMin >= sMax) throw new IllegalArgumentException("salary_min must be < salary_max");

        SalaryBand band    = SalaryBand.DEFAULTS.get(domain);
        boolean    minOk   = band.isWithin(sMin);
        boolean    maxOk   = band.isWithin(sMax);
        boolean    valid   = minOk && maxOk;

        JsonObject r = JsonValue.object()
            .put("success",         true)
            .put("domain",          domain)
            .put("salary_min",      sMin)
            .put("salary_max",      sMax)
            .put("valid",           valid)
            .put("min_within_band", minOk)
            .put("max_within_band", maxOk)
            .put("band_min",        band.min())
            .put("band_max",        band.max())
            .put("currency",        band.currency());
        if (!valid)
            r.put("action", "Job will be flagged MODERATION_REQUIRED — salary outside market-rate band");
        return r;
    }

    // ══════════════════════════════════════════════════════════════════════
    // 10 — job_seo_generate
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobSeoGenerate(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");

        JsonObject job = jobStore.get(jobId);
        if (job == null || !tenantId.equals(job.getString("tenant_id")))
            return notFound("job_id", jobId);

        String title  = job.getString("title");
        String domain = job.getString("domain");
        String desc   = job.has("description") ? job.getString("description") : "";
        String seoDesc = desc.length() > 160 ? desc.substring(0, 157) + "..." : desc;
        String url     = "https://jobs.ecoskiller.com/jobs/" + jobId;

        JsonObject seo = JsonValue.object()
            .put("title",         title + " | Ecoskiller Jobs")
            .put("description",   seoDesc)
            .put("keywords",      title + ", " + domain + " jobs, Ecoskiller, hiring")
            .put("canonical_url", url)
            .put("og_title",      title + " | Ecoskiller Jobs")
            .put("og_description",seoDesc)
            .put("og_url",        url)
            .put("og_type",       "website")
            .put("twitter_card",  "summary");

        return JsonValue.object()
            .put("success", true)
            .put("job_id",  jobId)
            .set("seo",     seo);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 11 — job_search_fulltext
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobSearchFulltext(String tool, JsonObject a) {
        String query    = security.sanitise(a.require("query"), "query");
        String tenantId = a.require("tenant_id");
        String domain   = a.has("domain") ? a.getString("domain") : "";
        int    page     = a.getInt("page");
        int    size     = a.getInt("page_size") > 0 ? Math.min(a.getInt("page_size"), 100) : 20;

        String ql = query.toLowerCase(Locale.ROOT);
        List<JsonObject> hits = new ArrayList<>();

        for (JsonObject job : jobStore.values()) {
            if (!tenantId.equals(job.getString("tenant_id"))) continue;
            if (!domain.isBlank() && !domain.equals(job.getString("domain"))) continue;
            if (!"PUBLISHED".equals(job.getString("status"))) continue;
            String text = (job.getString("title") + " " + job.getString("description")).toLowerCase(Locale.ROOT);
            if (!text.contains(ql)) continue;

            JsonObject hit = JsonValue.object()
                .put("job_id", job.getString("job_id"))
                .put("title",  job.getString("title"))
                .put("domain", job.getString("domain"))
                .put("status", job.getString("status"));
            if (job.has("location"))   hit.put("location",   job.getString("location"));
            if (job.getLong("salary_min") > 0) hit.put("salary_min", job.getLong("salary_min"));
            if (job.getLong("salary_max") > 0) hit.put("salary_max", job.getLong("salary_max"));
            hits.add(hit);
        }

        int total = hits.size();
        int from  = Math.min(page * size, total);
        int to    = Math.min(from + size, total);

        JsonArray results = JsonValue.array();
        for (JsonObject h : hits.subList(from, to)) results.add(h);

        return JsonValue.object()
            .put("success",   true)
            .put("query",     query)
            .put("total",     total)
            .put("page",      page)
            .put("page_size", size)
            .set("hits",      results);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 12 — job_status_transition
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobStatusTransition(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");
        String target   = a.require("target_status").toUpperCase();

        JsonObject job = jobStore.get(jobId);
        if (job == null || !tenantId.equals(job.getString("tenant_id")))
            return notFound("job_id", jobId);

        Status current, to;
        try { current = Status.valueOf(job.getString("status")); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Unknown current status: " + job.getString("status")); }
        try { to = Status.valueOf(target); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Invalid target status: " + target); }

        if (!Status.canTransition(current, to))
            throw new IllegalArgumentException("Invalid state transition: " + current + " → " + to);

        job.put("status",     to.name())
           .put("updated_at", Instant.now().toString());
        if (a.has("reason") && a.getString("reason") != null)
            job.put("status_reason", security.sanitise(a.getString("reason"), "reason"));

        audit(jobId, tenantId, "TRANSITION_" + to.name(), "system", null, job);

        return JsonValue.object()
            .put("success",         true)
            .put("job_id",          jobId)
            .put("previous_status", current.name())
            .put("new_status",      to.name())
            .put("kafka_event",     "job." + to.name().toLowerCase() + " → jobs." + job.getString("domain"));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 13 — job_audit_trail
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobAuditTrail(String tool, JsonObject a) {
        String jobId    = a.require("job_id");
        String tenantId = a.require("tenant_id");
        int    page     = a.getInt("page");
        int    size     = a.getInt("page_size") > 0 ? Math.min(a.getInt("page_size"), 50) : 50;

        List<JsonObject> logs = auditStore.getOrDefault(jobId, List.of());
        List<JsonObject> filtered = logs.stream()
            .filter(e -> tenantId.equals(e.getString("tenant_id")))
            .toList();

        int total = filtered.size();
        int from  = Math.min(page * size, total);
        int to    = Math.min(from + size, total);

        JsonArray entries = JsonValue.array();
        for (JsonObject e : filtered.subList(from, to)) entries.add(e);

        return JsonValue.object()
            .put("success",   true)
            .put("job_id",    jobId)
            .put("total",     total)
            .put("page",      page)
            .put("page_size", size)
            .set("entries",   entries);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 14 — job_domain_stats
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobDomainStats(String tool, JsonObject a) {
        String tenantId = a.require("tenant_id");
        String domainF  = a.has("domain") ? a.getString("domain") : "";

        Map<String, Map<String, Integer>> stats = new LinkedHashMap<>();
        for (JsonObject job : jobStore.values()) {
            if (!tenantId.equals(job.getString("tenant_id"))) continue;
            String d = job.getString("domain");
            if (!domainF.isBlank() && !domainF.equals(d)) continue;
            String s = job.getString("status");
            stats.computeIfAbsent(d, k -> new LinkedHashMap<>()).merge(s, 1, Integer::sum);
        }

        JsonObject domains = JsonValue.object();
        for (var e : stats.entrySet()) {
            JsonObject sc = JsonValue.object();
            e.getValue().forEach(sc::put);
            domains.set(e.getKey(), sc);
        }

        return JsonValue.object()
            .put("success",      true)
            .put("tenant_id",    tenantId)
            .set("domains",      domains)
            .put("generated_at", Instant.now().toString());
    }

    // ══════════════════════════════════════════════════════════════════════
    // 15 — job_health
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobHealth(String tool, JsonObject a) {
        // Production: ping PostgreSQL, Redis, Kafka, OpenSearch
        JsonObject checks = JsonValue.object()
            .put("postgresql", "healthy")
            .put("redis",      "healthy")
            .put("kafka",      "healthy")
            .put("opensearch", "healthy");

        return JsonValue.object()
            .put("service",       "ecoskiller-job-service")
            .put("status",        "healthy")
            .put("version",       "1.0.0")
            .put("jobs_in_store", jobStore.size())
            .put("timestamp",     Instant.now().toString())
            .set("dependencies",  checks);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 16 — job_schema
    // ══════════════════════════════════════════════════════════════════════
    public JsonObject jobSchema(String tool, JsonObject a) {
        JsonObject props = JsonValue.object();
        addProp(props, "job_id",       "string",  "UUID — auto-generated");
        addProp(props, "title",        "string",  "Job title, max 200 chars");
        addProp(props, "domain",       "string",  "Arts|Commerce|Science|Technology|Administration");
        addProp(props, "description",  "string",  "Full job description");
        addProp(props, "tenant_id",    "string",  "Tenant UUID — domain isolation key");
        addProp(props, "recruiter_id", "string",  "Recruiter UUID");
        addProp(props, "status",       "string",  "DRAFT|MODERATION_REQUIRED|PUBLISHED|CLOSED|ARCHIVED|REJECTED");
        addProp(props, "visibility",   "string",  "PUBLIC|TENANT_ONLY|RECRUITER");
        addProp(props, "version",      "integer", "Optimistic lock version");
        addProp(props, "skills",       "array",   "Required skill tags");
        addProp(props, "salary_min",   "integer", "Minimum monthly salary (INR)");
        addProp(props, "salary_max",   "integer", "Maximum monthly salary (INR)");
        addProp(props, "location",     "string",  "Location or 'Remote'");
        addProp(props, "created_at",   "string",  "ISO-8601 timestamp");
        addProp(props, "updated_at",   "string",  "ISO-8601 timestamp");

        JsonArray required = JsonValue.array()
            .add("job_id").add("title").add("domain")
            .add("tenant_id").add("recruiter_id").add("status");

        JsonObject schema = JsonValue.object()
            .put("type", "object")
            .set("properties", props)
            .set("required", required);

        JsonObject transitions = JsonValue.object()
            .put("DRAFT",               "→ PUBLISHED | MODERATION_REQUIRED")
            .put("MODERATION_REQUIRED", "→ PUBLISHED | REJECTED")
            .put("PUBLISHED",           "→ CLOSED")
            .put("CLOSED",              "→ ARCHIVED")
            .put("REJECTED",            "terminal")
            .put("ARCHIVED",            "terminal");

        return JsonValue.object()
            .put("success",       true)
            .set("schema",        schema)
            .set("status_machine",transitions)
            .put("kafka_topics",  "jobs.Arts | jobs.Commerce | jobs.Science | jobs.Technology | jobs.Administration")
            .put("events",        "job.created|job.updated|job.deleted|job.moderation_required|job.approved|job.rejected|job.closed");
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────

    private JsonObject notFound(String field, String value) {
        return JsonValue.object()
            .put("success", false)
            .put("error",   "Not found: " + field + "=" + value);
    }

    private void validateVisibility(String v) {
        try { Visibility.valueOf(v); }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("visibility must be PUBLIC|TENANT_ONLY|RECRUITER, got: " + v);
        }
    }

    private void warnSalaryBand(String domain, long min, long max) {
        SalaryBand band = SalaryBand.DEFAULTS.get(domain);
        if (band != null && (!band.isWithin(min) || !band.isWithin(max)))
            System.err.println("WARN: salary " + min + "-" + max + " outside " + domain + " band → will flag MODERATION_REQUIRED");
    }

    private void audit(String jobId, String tenantId, String action, String actor,
                       JsonObject before, JsonObject after) {
        JsonObject entry = JsonValue.object()
            .put("job_id",    jobId)
            .put("tenant_id", tenantId)
            .put("action",    action)
            .put("actor",     actor)
            .put("timestamp", Instant.now().toString());
        if (before != null) entry.set("before", before);
        if (after  != null) entry.set("after",  clone(after));
        auditStore.computeIfAbsent(jobId, k -> new ArrayList<>()).add(entry);
    }

    /** Deep clone a JsonObject via serialise → parse. */
    private JsonObject clone(JsonObject src) {
        return (JsonObject) JsonValue.parse(src.toJson());
    }

    private void addProp(JsonObject props, String name, String type, String desc) {
        props.set(name, JsonValue.object().put("type", type).put("description", desc));
    }
}
