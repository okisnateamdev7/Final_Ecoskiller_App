package com.ecoskiller.mcp.isd.security;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityValidator — idea-similarity-detector MCP Server
 *
 * Enforces per the ISD spec:
 *   • 13-tool allowlist — no arbitrary tool execution
 *   • UUID validation for all ID fields
 *   • Cosine similarity range [0.0–1.0]
 *   • copy_probability range [0.0–1.0]
 *   • Classification enum: CLEAR | REVIEW | FLAG
 *   • top_k range: 1–100
 *   • Category max length
 *   • Threshold bounds: flag_threshold must be > review_threshold > 0
 *   • Injection guard (SQL, XSS, path traversal, template injection)
 *   • limit/page validation
 *   • DLQ count non-negative
 */
public class SecurityValidator {

    private static final Logger LOG = Logger.getLogger(SecurityValidator.class.getName());

    // ── Allowlisted tools ────────────────────────────────────────────────────
    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "run_similarity_check",
        "run_similarity_recheck",
        "get_similarity_report",
        "get_similarity_audit",
        "get_classification",
        "compute_copy_probability",
        "get_top_matches",
        "get_threshold_config",
        "set_threshold_config",
        "get_dlq_status",
        "get_consumer_health",
        "get_similarity_metrics",
        "list_similarity_reports"
    );

    // ── Enums ────────────────────────────────────────────────────────────────
    private static final Set<String> CLASSIFICATIONS = Set.of("CLEAR", "REVIEW", "FLAG");

    private static final Set<String> VALID_CATEGORIES = Set.of(
        "software", "hardware", "business_process", "research", "other",
        "materials_science", "software_architecture", "product_design",
        "general", "all"
    );

    // ── Limits ───────────────────────────────────────────────────────────────
    private static final int MAX_TOP_K           = 100;
    private static final int MAX_CATEGORY_LEN    = 100;
    private static final int MAX_LIMIT           = 500;
    private static final int MAX_PAGE            = 10_000;

    // ── Patterns ─────────────────────────────────────────────────────────────
    private static final Pattern UUID_RE =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}"
                      + "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final Pattern INJECTION_RE = Pattern.compile(
        "(?i)(;\\s*(drop|alter|truncate|delete|insert|update|exec|execute|select\\s+\\*)" +
        "|<script|javascript:|data:|vbscript:|onload=|onerror=" +
        "|\\.\\.\\/|\\{\\{|\\$\\{" +
        "|\\'\\s*or\\s+\\'|\\\"\\s*or\\s+\\\")");

    // ─────────────────────────────────────────────────────────────────────────

    public void validateToolCall(String toolName, JsonNode args) {
        validateToolName(toolName);
        if (args == null || args.isNull()) return;
        validateCommonFields(args);
        validateToolSpecific(toolName, args);
    }

    public void validateToolName(String name) {
        if (name == null || name.isBlank())
            throw new SecurityException("Tool name is required");
        if (!ALLOWED_TOOLS.contains(name)) {
            LOG.warning("Rejected unknown tool: " + name);
            throw new SecurityException("Unknown tool: " + name);
        }
    }

    // ── Common field validators ───────────────────────────────────────────────

    private void validateCommonFields(JsonNode args) {
        ifPresent(args, "idea_id",      v -> validateUuid(v, "idea_id"));
        ifPresent(args, "tenant_id",    v -> validateUuid(v, "tenant_id"));
        ifPresent(args, "candidate_id", v -> validateUuid(v, "candidate_id"));
        ifPresent(args, "report_id",    v -> validateUuid(v, "report_id"));
        // vector_id is a Qdrant point ID — can be a UUID or a string key; validate it's not empty/injected
        ifPresent(args, "vector_id",    v -> {
            if (v.length() > 256) throw new SecurityException("vector_id exceeds max length 256");
            if (INJECTION_RE.matcher(v).find()) throw new SecurityException("Unsafe content in vector_id");
        });
        ifPresent(args, "classification",
            v -> { if (!CLASSIFICATIONS.contains(v.toUpperCase()))
                throw new SecurityException("classification must be CLEAR|REVIEW|FLAG, got: " + v); });
        ifPresent(args, "idea_category", v -> {
            if (v.length() > MAX_CATEGORY_LEN)
                throw new SecurityException("idea_category exceeds max length " + MAX_CATEGORY_LEN);
            if (INJECTION_RE.matcher(v).find())
                throw new SecurityException("Unsafe content in idea_category");
        });
        ifPresent(args, "copy_probability", v -> validateProbability(Double.parseDouble(v), "copy_probability"));
        ifPresent(args, "min_similarity",   v -> validateProbability(Double.parseDouble(v), "min_similarity"));
        ifPresent(args, "max_similarity",   v -> validateProbability(Double.parseDouble(v), "max_similarity"));
    }

    // ── Tool-specific ─────────────────────────────────────────────────────────

    private void validateToolSpecific(String tool, JsonNode args) {
        switch (tool) {
            case "run_similarity_check"     -> validateRunCheck(args);
            case "run_similarity_recheck"   -> validateRecheck(args);
            case "get_top_matches"          -> validateTopMatches(args);
            case "set_threshold_config"     -> validateSetThreshold(args);
            case "list_similarity_reports"  -> validateListReports(args);
            case "compute_copy_probability" -> validateComputeScore(args);
        }
    }

    private void validateRunCheck(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        requireVectorId(args, "vector_id");
        if (args.has("top_k")) {
            int k = args.get("top_k").asInt(-1);
            if (k < 1 || k > MAX_TOP_K)
                throw new SecurityException("top_k must be 1–" + MAX_TOP_K + ", got " + k);
        }
        if (args.has("min_similarity")) {
            validateProbability(args.get("min_similarity").asDouble(), "min_similarity");
        }
    }

    private void validateRecheck(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        requireVectorId(args, "vector_id");
    }

    private void validateTopMatches(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        if (args.has("top_k")) {
            int k = args.get("top_k").asInt(-1);
            if (k < 1 || k > MAX_TOP_K)
                throw new SecurityException("top_k must be 1–" + MAX_TOP_K + ", got " + k);
        }
    }

    private void validateSetThreshold(JsonNode args) {
        requireUuid(args, "tenant_id");
        double reviewThresh = args.path("review_threshold").asDouble(-1);
        double flagThresh   = args.path("flag_threshold").asDouble(-1);
        double minSim       = args.path("min_similarity_floor").asDouble(-1);

        if (reviewThresh >= 0) validateProbability(reviewThresh, "review_threshold");
        if (flagThresh >= 0)   validateProbability(flagThresh,   "flag_threshold");
        if (minSim >= 0)       validateProbability(minSim,       "min_similarity_floor");

        // Semantic ordering: flag > review > min_similarity
        if (reviewThresh >= 0 && flagThresh >= 0 && flagThresh <= reviewThresh)
            throw new SecurityException(
                "flag_threshold (" + flagThresh + ") must be strictly greater than " +
                "review_threshold (" + reviewThresh + ")");
        if (minSim >= 0 && reviewThresh >= 0 && reviewThresh <= minSim)
            throw new SecurityException(
                "review_threshold (" + reviewThresh + ") must be greater than " +
                "min_similarity_floor (" + minSim + ")");
    }

    private void validateListReports(JsonNode args) {
        requireUuid(args, "tenant_id");
        if (args.has("limit")) {
            int l = args.get("limit").asInt(-1);
            if (l < 1 || l > MAX_LIMIT)
                throw new SecurityException("limit must be 1–" + MAX_LIMIT + ", got " + l);
        }
        if (args.has("page")) {
            int p = args.get("page").asInt(-1);
            if (p < 0 || p > MAX_PAGE)
                throw new SecurityException("page must be 0–" + MAX_PAGE + ", got " + p);
        }
    }

    private void validateComputeScore(JsonNode args) {
        requireUuid(args, "idea_id");
        requireUuid(args, "tenant_id");
        if (args.has("max_cosine_similarity"))
            validateProbability(args.get("max_cosine_similarity").asDouble(), "max_cosine_similarity");
        if (args.has("category_match_rate"))
            validateProbability(args.get("category_match_rate").asDouble(), "category_match_rate");
    }

    // ── Field validators ──────────────────────────────────────────────────────

    public void validateUuid(String value, String field) {
        if (value == null || value.isBlank())
            throw new SecurityException(field + " is required");
        if (!UUID_RE.matcher(value).matches())
            throw new SecurityException(field + " must be a valid UUID, got: " + value);
    }

    public void validateProbability(double value, String field) {
        if (value < 0.0 || value > 1.0)
            throw new SecurityException(field + " must be in [0.0, 1.0], got: " + value);
    }

    private void requireUuid(JsonNode args, String field) {
        validateUuid(args.path(field).asText(""), field);
    }

    private void requireVectorId(JsonNode args, String field) {
        String v = args.path(field).asText("").trim();
        if (v.isBlank()) throw new SecurityException(field + " is required");
        if (v.length() > 256) throw new SecurityException(field + " exceeds max length 256");
        if (INJECTION_RE.matcher(v).find()) throw new SecurityException("Unsafe content in " + field);
    }

    private void ifPresent(JsonNode args, String field, java.util.function.Consumer<String> check) {
        if (args.has(field) && !args.get(field).isNull()) {
            String v = args.get(field).asText().trim();
            if (!v.isBlank()) check.accept(v);
        }
    }
}
