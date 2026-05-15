package com.ecoskiller.mcp.its.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityValidator — idea-timestamp-service MCP Server
 *
 * Enforces per the ITS spec:
 *   • Tool-name allowlist (reject anything not in the 12-tool set)
 *   • UUID format validation for all ID fields
 *   • Field length limits (title 200 chars, description 10 000 chars,
 *     attachments max 5 files × 10 MB each — enforced at ingestion layer)
 *   • Visibility enum validation: PRIVATE | INTERNAL | MARKETPLACE
 *   • Status enum validation: PENDING_FINGERPRINT | VERIFIED_ORIGINAL |
 *                             UNDER_REVIEW | FLAGGED | LICENSED | ARCHIVED
 *   • Injection guard (SQL, XSS, path traversal)
 *   • Version number: 1–1000 (positive int)
 *   • Content hash: 64-char lowercase hex (SHA-256 output)
 *   • Idea category: no injection, max 100 chars
 *   • Audit event type: enum check
 */
public class SecurityValidator {

    private static final Logger LOG = Logger.getLogger(SecurityValidator.class.getName());

    // ── Allowlisted tools ────────────────────────────────────────────────────

    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "submit_idea",
        "revise_idea",
        "get_timestamp_proof",
        "list_idea_versions",
        "verify_idea_integrity",
        "get_submission_status",
        "set_visibility",
        "archive_idea",
        "check_rate_limit",
        "get_submission_audit",
        "verify_minio_object",
        "get_idea_ownership"
    );

    // ── Enums ────────────────────────────────────────────────────────────────

    private static final Set<String> VISIBILITY_LEVELS = Set.of(
        "PRIVATE", "INTERNAL", "MARKETPLACE"
    );

    private static final Set<String> IDEA_STATUSES = Set.of(
        "PENDING_FINGERPRINT", "VERIFIED_ORIGINAL",
        "UNDER_REVIEW", "FLAGGED", "LICENSED", "ARCHIVED"
    );

    private static final Set<String> AUDIT_EVENT_TYPES = Set.of(
        "CREATED", "SUBMITTED", "REVISED", "APPROVED", "FLAGGED",
        "LICENSED", "ARCHIVED", "VISIBILITY_CHANGED", "INTEGRITY_VERIFIED",
        "TAMPER_DETECTED", "RATE_LIMIT_HIT", "OWNERSHIP_QUERIED"
    );

    // ── Size limits (per ITS spec §5) ────────────────────────────────────────

    private static final int MAX_TITLE_LEN       = 200;
    private static final int MAX_DESCRIPTION_LEN = 10_000;
    private static final int MAX_CATEGORY_LEN    = 100;
    private static final int MAX_REASON_LEN      = 2_000;
    private static final int MAX_VERSION_NUM     = 1_000;

    // ── Patterns ─────────────────────────────────────────────────────────────

    private static final Pattern UUID_RE =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}"
                      + "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /** SHA-256 hex output: exactly 64 lowercase hex chars */
    private static final Pattern SHA256_RE =
        Pattern.compile("^[0-9a-f]{64}$");

    /** MinIO ETag: MD5 hex, 32 chars (no quotes) */
    private static final Pattern ETAG_RE =
        Pattern.compile("^[0-9a-fA-F]{32}$");

    /** Injection guard: SQL, XSS, path traversal, template injection */
    private static final Pattern INJECTION_RE = Pattern.compile(
        "(?i)(;\\s*(drop|alter|truncate|delete|insert|update|exec|execute|select\\s+\\*)" +
        "|<script|javascript:|data:|vbscript:|onload=|onerror=" +
        "|\\.\\./" +                          // path traversal
        "|\\{\\{|\\$\\{" +                   // template injection
        "|\\'\\s*or\\s+\\'|\\\"\\s*or\\s+\\\")");  // SQL OR bypass

    // ─────────────────────────────────────────────────────────────────────────

    /** Entry point: called before every tool dispatch. */
    public void validateToolCall(String toolName, JsonNode args) {
        validateToolName(toolName);
        if (args == null || args.isNull()) return;
        validateCommonFields(args);
        validateToolSpecific(toolName, args);
    }

    // ── Tool name ─────────────────────────────────────────────────────────────

    public void validateToolName(String name) {
        if (name == null || name.isBlank())
            throw new SecurityException("Tool name is required");
        if (!ALLOWED_TOOLS.contains(name)) {
            LOG.warning("Rejected unknown tool: " + name);
            throw new SecurityException("Unknown tool: " + name);
        }
    }

    // ── Common fields present across multiple tools ───────────────────────────

    private void validateCommonFields(JsonNode args) {
        ifPresent(args, "idea_id",      v -> validateUuid(v, "idea_id"));
        ifPresent(args, "tenant_id",    v -> validateUuid(v, "tenant_id"));
        ifPresent(args, "candidate_id", v -> validateUuid(v, "candidate_id"));
        ifPresent(args, "version_id",   v -> validateUuid(v, "version_id"));
        ifPresent(args, "title",        v -> validateText(v, "title",       MAX_TITLE_LEN));
        ifPresent(args, "description",  v -> validateText(v, "description", MAX_DESCRIPTION_LEN));
        ifPresent(args, "idea_category",v -> validateText(v, "idea_category", MAX_CATEGORY_LEN));
        ifPresent(args, "revision_reason", v -> validateText(v, "revision_reason", MAX_REASON_LEN));
        ifPresent(args, "visibility_level", v -> validateVisibility(v));
        ifPresent(args, "status",       v -> validateStatus(v));
        ifPresent(args, "content_hash", v -> validateSha256(v));
    }

    // ── Tool-specific validation ──────────────────────────────────────────────

    private void validateToolSpecific(String tool, JsonNode args) {
        switch (tool) {
            case "submit_idea"          -> validateSubmitIdea(args);
            case "revise_idea"          -> validateReviseIdea(args);
            case "verify_idea_integrity"-> validateVerifyIntegrity(args);
            case "get_submission_audit" -> validateAuditQuery(args);
            case "verify_minio_object"  -> validateMinioVerify(args);
        }
    }

    private void validateSubmitIdea(JsonNode args) {
        requireUuid(args, "tenant_id");
        requireUuid(args, "candidate_id");
        requireText(args, "title", MAX_TITLE_LEN);
        requireText(args, "description", MAX_DESCRIPTION_LEN);
        // visibility_level required
        String vis = args.path("visibility_level").asText("");
        if (vis.isBlank()) throw new SecurityException("visibility_level is required");
        validateVisibility(vis);
        // attachment_count: if present, 0-5
        if (args.has("attachment_count")) {
            int cnt = args.get("attachment_count").asInt(-1);
            if (cnt < 0 || cnt > 5)
                throw new SecurityException("attachment_count must be 0-5, got " + cnt);
        }
    }

    private void validateReviseIdea(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        requireUuid(args, "candidate_id");
        requireText(args, "title", MAX_TITLE_LEN);
        requireText(args, "description", MAX_DESCRIPTION_LEN);
        // version number if present
        if (args.has("version_number")) {
            int v = args.get("version_number").asInt(-1);
            if (v < 1 || v > MAX_VERSION_NUM)
                throw new SecurityException("version_number out of range: " + v);
        }
    }

    private void validateVerifyIntegrity(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        requireText(args, "title", MAX_TITLE_LEN);
        requireText(args, "description", MAX_DESCRIPTION_LEN);
        // stored_hash optional — but if given must be valid SHA-256
        if (args.has("stored_hash")) {
            validateSha256(args.get("stored_hash").asText());
        }
    }

    private void validateAuditQuery(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        if (args.has("event_type_filter")) {
            String et = args.get("event_type_filter").asText();
            if (!et.isBlank() && !AUDIT_EVENT_TYPES.contains(et.toUpperCase()))
                throw new SecurityException("Unknown audit event_type_filter: " + et);
        }
    }

    private void validateMinioVerify(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        if (args.has("expected_etag")) {
            String etag = args.get("expected_etag").asText("");
            if (!etag.isBlank() && !ETAG_RE.matcher(etag).matches())
                throw new SecurityException("expected_etag must be 32-char hex (MD5), got: " + etag);
        }
    }

    // ── Field validators ─────────────────────────────────────────────────────

    public void validateUuid(String value, String field) {
        if (value == null || value.isBlank())
            throw new SecurityException(field + " is required and must be a UUID");
        if (!UUID_RE.matcher(value).matches())
            throw new SecurityException(field + " must be a valid UUID, got: " + value);
    }

    public void validateText(String text, String field, int maxLen) {
        if (text == null) return;
        if (text.length() > maxLen)
            throw new SecurityException(field + " exceeds max length " + maxLen + " (got " + text.length() + ")");
        if (INJECTION_RE.matcher(text).find()) {
            LOG.warning("Injection pattern in field: " + field);
            throw new SecurityException("Potentially unsafe content in field: " + field);
        }
    }

    public void validateVisibility(String v) {
        if (!VISIBILITY_LEVELS.contains(v.toUpperCase()))
            throw new SecurityException("visibility_level must be PRIVATE|INTERNAL|MARKETPLACE, got: " + v);
    }

    public void validateStatus(String s) {
        if (!IDEA_STATUSES.contains(s.toUpperCase()))
            throw new SecurityException("status must be one of " + IDEA_STATUSES + ", got: " + s);
    }

    public void validateSha256(String hash) {
        if (hash == null || !SHA256_RE.matcher(hash).matches())
            throw new SecurityException("content_hash must be 64-char lowercase hex (SHA-256), got: " + hash);
    }

    // ── Require helpers ───────────────────────────────────────────────────────

    private void requireUuid(JsonNode args, String field) {
        validateUuid(args.path(field).asText(""), field);
    }

    private void requireText(JsonNode args, String field, int maxLen) {
        String v = args.path(field).asText("");
        if (v.isBlank()) throw new SecurityException(field + " is required");
        validateText(v, field, maxLen);
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    private void ifPresent(JsonNode args, String field, java.util.function.Consumer<String> check) {
        if (args.has(field) && !args.get(field).isNull()) {
            String v = args.get(field).asText().trim();
            if (!v.isBlank()) check.accept(v);
        }
    }
}
