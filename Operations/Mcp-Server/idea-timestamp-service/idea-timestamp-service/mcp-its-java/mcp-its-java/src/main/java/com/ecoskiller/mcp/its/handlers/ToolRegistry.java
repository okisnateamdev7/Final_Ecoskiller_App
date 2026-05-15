package com.ecoskiller.mcp.its.handlers;

import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.function.Function;

/**
 * ToolRegistry
 *
 * Wires all 12 idea-timestamp-service MCP tools:
 *   getToolDefinitions() → JSON array for tools/list
 *   call(name, args)     → dispatches to handler
 */
public class ToolRegistry {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Function<JsonNode, Object>> handlers;

    public ToolRegistry(SecurityValidator sv) {
        SubmissionHandler  sub = new SubmissionHandler(sv);
        TimestampHandler   ts  = new TimestampHandler(sv);
        IntegrityHandler   int_ = new IntegrityHandler(sv);
        LifecycleHandler   lc  = new LifecycleHandler(sv);
        AuditHandler       aud = new AuditHandler(sv);

        this.handlers = Map.ofEntries(
            Map.entry("submit_idea",            sub::submitIdea),
            Map.entry("revise_idea",            sub::reviseIdea),
            Map.entry("get_timestamp_proof",    ts::getTimestampProof),
            Map.entry("list_idea_versions",     ts::listIdeaVersions),
            Map.entry("verify_idea_integrity",  int_::verifyIdeaIntegrity),
            Map.entry("get_submission_status",  lc::getSubmissionStatus),
            Map.entry("set_visibility",         lc::setVisibility),
            Map.entry("archive_idea",           lc::archiveIdea),
            Map.entry("check_rate_limit",       aud::checkRateLimit),
            Map.entry("get_submission_audit",   aud::getSubmissionAudit),
            Map.entry("verify_minio_object",    int_::verifyMinioObject),
            Map.entry("get_idea_ownership",     ts::getIdeaOwnership)
        );
    }

    public Object call(String toolName, JsonNode arguments) {
        Function<JsonNode, Object> h = handlers.get(toolName);
        if (h == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "Unknown tool: " + toolName);
            return err;
        }
        try {
            return h.apply(arguments);
        } catch (IllegalArgumentException | SecurityException e) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", e.getMessage());
            return err;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool definitions (MCP JSON Schema)
    // ─────────────────────────────────────────────────────────────────────────

    public ArrayNode getToolDefinitions() {
        ArrayNode tools = mapper.createArrayNode();

        // 1. submit_idea
        tools.add(tool("submit_idea",
            "Primary idea submission endpoint. Accepts a candidate's idea and performs:\n" +
            "(1) Keycloak JWT validation + per-tenant rate-limit check (Redis INCR/EXPIRE pattern).\n" +
            "(2) SHA-256 hash of canonical JSON payload (title + description + attachment bytes).\n" +
            "(3) Authoritative server-side UTC timestamp anchored to PostgreSQL transaction clock.\n" +
            "(4) MinIO object upload to ecoskiller-ideas-{tenant_id}/ideas/{idea_id}/v1/submission.json " +
            "with Object Versioning enabled.\n" +
            "(5) PostgreSQL write to innovation.ideas (immutable submitted_at, content_hash).\n" +
            "(6) Kafka publish: idea.submitted event → triggers idea-dna-fingerprint-engine.\n" +
            "Returns: idea_id, version, submitted_at (authoritative UTC), content_hash, " +
            "minio_object_key, minio_etag, visibility_level, status=PENDING_FINGERPRINT.",
            schema()
                .required("tenant_id", "candidate_id", "title", "description", "visibility_level")
                .string("tenant_id",        "Tenant scope UUID")
                .string("candidate_id",     "Submitting candidate UUID")
                .string("title",            "Idea title (max 200 chars)")
                .string("description",      "Full idea description (max 10 000 chars)")
                .string("idea_category",    "Semantic category tag: software | hardware | business_process | research | other (optional)")
                .string("visibility_level", "PRIVATE | INTERNAL | MARKETPLACE")
                .integer("attachment_count","Number of attachments (0–5, max 10 MB each, optional)")
                .string("technical_details","Additional technical context (optional, max 10 000 chars)")
                .build()));

        // 2. revise_idea
        tools.add(tool("revise_idea",
            "Submit a versioned revision of an existing idea.\n" +
            "Only the original candidate_id may revise their idea.\n" +
            "Each revision generates a NEW SHA-256 hash, a NEW submitted_at timestamp, and a NEW MinIO " +
            "object version (ideas/{idea_id}/v{n}/submission.json).\n" +
            "The original v1 PostgreSQL record remains IMMUTABLE; a new row is written to " +
            "innovation.idea_versions.\n" +
            "Emits Kafka event idea.submitted with version > 1 and is_revision=true.\n" +
            "Status is reset to PENDING_FINGERPRINT for the new version.",
            schema()
                .required("idea_id", "tenant_id", "candidate_id", "title", "description", "visibility_level")
                .string("idea_id",          "UUID of the existing idea to revise")
                .string("tenant_id",        "Tenant scope UUID")
                .string("candidate_id",     "Must match original submitter UUID")
                .string("title",            "Revised idea title (max 200 chars)")
                .string("description",      "Revised description (max 10 000 chars)")
                .string("visibility_level", "PRIVATE | INTERNAL | MARKETPLACE")
                .string("revision_reason",  "Candidate rationale for revision — stored and displayed in dispute review (optional, max 2 000 chars)")
                .string("idea_category",    "Updated category tag (optional)")
                .string("technical_details","Updated technical details (optional)")
                .build()));

        // 3. get_timestamp_proof
        tools.add(tool("get_timestamp_proof",
            "Retrieve the immutable timestamp proof record for an idea.\n" +
            "Returns: idea_id, submitted_at (authoritative UTC, immutable), content_hash (SHA-256), " +
            "minio_object_key, minio_etag (MD5 of uploaded object), version, visibility_level.\n" +
            "Used by dispute resolution, licensing workflows, and patent evidence generation.\n" +
            "The submitted_at value was generated from the PostgreSQL transaction clock " +
            "(monotonic server-side, NOT the client's reported time).",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",   "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .integer("version",  "Specific version number (optional — returns latest if omitted)")
                .build()));

        // 4. list_idea_versions
        tools.add(tool("list_idea_versions",
            "List all versions of an idea from innovation.idea_versions.\n" +
            "Returns: version_number, content_hash, submitted_at, minio_object_key, minio_etag, " +
            "revision_reason, status for each version.\n" +
            "The table is append-only (no UPDATE/DELETE permitted).\n" +
            "Includes the original v1 submission plus all revisions.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",   "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .build()));

        // 5. verify_idea_integrity
        tools.add(tool("verify_idea_integrity",
            "Tamper detection — recompute SHA-256 from provided content and compare against the " +
            "stored content_hash in PostgreSQL.\n" +
            "Uses identical canonicalization: deterministic JSON serialization of title + description + " +
            "attachment_bytes metadata.\n" +
            "Returns: MATCH (content integrity confirmed) or MISMATCH (content modified — tamper evidence).\n" +
            "Admin role required (per ITS spec §5.1). " +
            "MISMATCH triggers a TAMPER_DETECTED audit log entry.",
            schema()
                .required("idea_id", "tenant_id", "title", "description")
                .string("idea_id",     "UUID of the idea to verify")
                .string("tenant_id",   "Tenant scope UUID")
                .string("title",       "Current title to verify against stored hash")
                .string("description", "Current description to verify against stored hash")
                .string("technical_details", "Current technical details (optional)")
                .string("stored_hash", "Expected content_hash to compare against (64-char hex SHA-256). " +
                                       "If omitted, fetched from PostgreSQL.")
                .integer("version",    "Version to verify (optional — uses latest if omitted)")
                .build()));

        // 6. get_submission_status
        tools.add(tool("get_submission_status",
            "Return the current lifecycle status of an idea.\n" +
            "Status lifecycle: PENDING_FINGERPRINT → VERIFIED_ORIGINAL | UNDER_REVIEW | FLAGGED | LICENSED | ARCHIVED.\n" +
            "PENDING_FINGERPRINT: Kafka event sent; awaiting idea-dna-fingerprint-engine processing.\n" +
            "VERIFIED_ORIGINAL: Similarity check passed — idea cleared for marketplace/licensing.\n" +
            "UNDER_REVIEW: Similarity check raised REVIEW tier — human review pending.\n" +
            "FLAGGED: Similarity check raised FLAG tier — plagiarism investigation open.\n" +
            "LICENSED: Active licensing contract exists.\n" +
            "ARCHIVED: Soft-archived; immutable record retained for IP/compliance (7-year retention).",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",   "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .build()));

        // 7. set_visibility
        tools.add(tool("set_visibility",
            "Update the visibility level of an idea (only by the original candidate_id or admin).\n" +
            "PRIVATE: visible only to the submitting candidate.\n" +
            "INTERNAL: visible to recruiters within the submitting tenant.\n" +
            "MARKETPLACE: discoverable via the Idea Marketplace Service; triggers " +
            "search-indexer to add to OpenSearch ecoskiller-{tenant_id}-innovations index.\n" +
            "Emits audit log entry VISIBILITY_CHANGED. Does NOT change content_hash or submitted_at.",
            schema()
                .required("idea_id", "tenant_id", "candidate_id", "visibility_level")
                .string("idea_id",         "UUID of the idea")
                .string("tenant_id",       "Tenant scope UUID")
                .string("candidate_id",    "Must match original submitter for self-service change")
                .string("visibility_level","New visibility: PRIVATE | INTERNAL | MARKETPLACE")
                .build()));

        // 8. archive_idea
        tools.add(tool("archive_idea",
            "Soft-archive an idea. Sets status=ARCHIVED (read-only; no new licenses).\n" +
            "The record is NEVER deleted — PostgreSQL soft-archive only, MinIO object preserved.\n" +
            "Existing licenses are grandfathered (not terminated).\n" +
            "Marked in search results as archived. Emits audit log entry ARCHIVED.\n" +
            "Retention: fingerprints and audit records retained 7 years for IP compliance.",
            schema()
                .required("idea_id", "tenant_id", "candidate_id")
                .string("idea_id",      "UUID of the idea to archive")
                .string("tenant_id",    "Tenant scope UUID")
                .string("candidate_id", "Must match original submitter UUID")
                .string("archive_reason","Reason for archival (optional, max 2 000 chars)")
                .build()));

        // 9. check_rate_limit
        tools.add(tool("check_rate_limit",
            "Inspect the current Redis rate-limit counter for a candidate+tenant combination.\n" +
            "Key pattern: tenant:{tenant_id}:candidate:{candidate_id}:submissions:{date}.\n" +
            "Returns: current_count, daily_limit (from admin-service config), " +
            "remaining_submissions, reset_at (Redis key TTL expiry in UTC), rate_limited (bool).\n" +
            "Limit is configurable per tenant via admin-service. Exceeding the limit returns HTTP 429 " +
            "with a Retry-After header on the REST API.",
            schema()
                .required("tenant_id", "candidate_id")
                .string("tenant_id",    "Tenant scope UUID")
                .string("candidate_id", "Candidate UUID to check")
                .string("date",         "Date to check (YYYY-MM-DD UTC, optional — defaults to today)")
                .build()));

        // 10. get_submission_audit
        tools.add(tool("get_submission_audit",
            "Return the append-only audit log for a specific idea from innovation.idea_submission_audit.\n" +
            "Entries are immutable and include: event_type, actor_id, description, " +
            "request_metadata, rate_limit_state, submission_latency_ms, timestamp.\n" +
            "Used for: abuse detection, support escalations, IP dispute evidence, compliance audits.\n" +
            "Optional filter by event_type. Results ordered by timestamp ascending.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",           "UUID of the idea")
                .string("tenant_id",         "Tenant scope UUID")
                .string("event_type_filter", "Filter by event type: CREATED | SUBMITTED | REVISED | APPROVED | " +
                                             "FLAGGED | LICENSED | ARCHIVED | VISIBILITY_CHANGED | " +
                                             "INTEGRITY_VERIFIED | TAMPER_DETECTED (optional)")
                .integer("limit",            "Max events to return (1–500, default 100)")
                .build()));

        // 11. verify_minio_object
        tools.add(tool("verify_minio_object",
            "Re-fetch the MinIO object at the stored minio_object_key and compare its ETag (MD5) " +
            "against the minio_etag stored in PostgreSQL.\n" +
            "Provides dual-layer tamper detection: SHA-256 (content) + ETag (storage-layer MD5).\n" +
            "A mismatch indicates the MinIO object was overwritten or corrupted outside the service.\n" +
            "Returns: object_exists, etag_match, stored_etag, fetched_etag, object_version_count.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",       "UUID of the idea")
                .string("tenant_id",     "Tenant scope UUID")
                .integer("version",      "Version number (optional — checks latest if omitted)")
                .string("expected_etag", "ETag to compare against (32-char MD5 hex). " +
                                         "If omitted, fetched from PostgreSQL record.")
                .build()));

        // 12. get_idea_ownership
        tools.add(tool("get_idea_ownership",
            "Return the canonical ownership record for an idea.\n" +
            "Fields: idea_id, tenant_id, candidate_id, submitted_at (immutable), content_hash, " +
            "visibility_level, status, idea_category, version (latest), minio_object_key.\n" +
            "This is the source-of-truth record from innovation.ideas (PostgreSQL RLS enforced).\n" +
            "Used by licensing-contract-service (Module XIII) before initiating any license agreement.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id",   "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .build()));

        return tools;
    }

    // ── DSL helpers ───────────────────────────────────────────────────────────

    private ObjectNode tool(String name, String description, ObjectNode inputSchema) {
        ObjectNode t = mapper.createObjectNode();
        t.put("name",        name);
        t.put("description", description);
        t.set("inputSchema", inputSchema);
        return t;
    }

    private SchemaBuilder schema() { return new SchemaBuilder(mapper); }

    private static class SchemaBuilder {
        private final ObjectMapper mapper;
        private final ObjectNode   schema;
        private final ObjectNode   props;
        private final ArrayNode    required;

        SchemaBuilder(ObjectMapper m) {
            this.mapper   = m;
            this.schema   = m.createObjectNode();
            this.props    = m.createObjectNode();
            this.required = m.createArrayNode();
            schema.put("type", "object");
            schema.set("properties", props);
        }

        SchemaBuilder required(String... fields) {
            for (String f : fields) required.add(f);
            return this;
        }

        SchemaBuilder string(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "string"); p.put("description", desc);
            props.set(name, p); return this;
        }

        SchemaBuilder integer(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "integer"); p.put("description", desc);
            props.set(name, p); return this;
        }

        ObjectNode build() {
            if (required.size() > 0) schema.set("required", required);
            return schema;
        }
    }
}
