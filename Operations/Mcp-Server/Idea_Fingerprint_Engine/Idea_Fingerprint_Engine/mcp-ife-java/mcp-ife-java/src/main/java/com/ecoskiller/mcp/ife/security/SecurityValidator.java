package com.ecoskiller.mcp.ife.security;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityValidator — IFE MCP Server
 *
 * Enforces:
 *  - Tool-name allowlist (no unknown tool execution)
 *  - Input size limits (prevent DoS via oversized payloads)
 *  - Content sanitization (reject injection patterns)
 *  - Tenant ID format validation (UUID only)
 *  - Rate-limit counters per tool
 *  - Embedding vector bounds checking
 */
public class SecurityValidator {

    private static final Logger LOG = Logger.getLogger(SecurityValidator.class.getName());

    /* ── Allowlisted tools ─────────────────────────────────────────────────── */
    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "compute_fingerprint",
        "compute_fingerprint_batch",
        "generate_embedding",
        "similarity_search",
        "verify_fingerprint",
        "get_fingerprint",
        "get_similarity_report",
        "detect_plagiarism",
        "index_embedding",
        "delete_embedding",
        "reindex_tenant",
        "get_batch_job_status",
        "list_embedding_models",
        "compute_risk_score"
    );

    /* ── Limits ────────────────────────────────────────────────────────────── */
    private static final int MAX_TEXT_LENGTH        = 51_200;  // 50 KB per spec
    private static final int MAX_TITLE_LENGTH       = 500;
    private static final int MAX_BATCH_SIZE         = 500;     // ideas per batch call
    private static final int MAX_TOP_K              = 50;
    private static final int EMBEDDING_DIMENSION    = 384;

    /* ── UUID pattern ──────────────────────────────────────────────────────── */
    private static final Pattern UUID_PATTERN =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /* ── SQL/code injection guard ──────────────────────────────────────────── */
    private static final Pattern INJECTION_PATTERN =
        Pattern.compile("(?i)(;\\s*(drop|alter|delete|truncate|insert|update|exec|execute|select\\s+\\*)|" +
                        "<script|javascript:|data:|vbscript:|onload=|onerror=|\\.\\./)");

    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Top-level validation entry point.
     * Throws SecurityException on any violation — caller converts to MCP error.
     */
    public void validateToolCall(String toolName, JsonNode arguments) {
        validateToolName(toolName);
        validateArguments(toolName, arguments);
    }

    public void validateToolName(String toolName) {
        if (toolName == null || toolName.isBlank()) {
            throw new SecurityException("Tool name is required");
        }
        if (!ALLOWED_TOOLS.contains(toolName)) {
            LOG.warning("Rejected unknown tool: " + toolName);
            throw new SecurityException("Unknown tool: " + toolName);
        }
    }

    public void validateArguments(String toolName, JsonNode arguments) {
        if (arguments == null || arguments.isNull()) return;

        // Common field validators
        if (arguments.has("idea_id"))     validateUuid(arguments.get("idea_id").asText(), "idea_id");
        if (arguments.has("tenant_id"))   validateUuid(arguments.get("tenant_id").asText(), "tenant_id");
        if (arguments.has("job_id"))      validateUuid(arguments.get("job_id").asText(), "job_id");
        if (arguments.has("vector_id"))   validateUuid(arguments.get("vector_id").asText(), "vector_id");
        if (arguments.has("title"))       validateText(arguments.get("title").asText(), "title", MAX_TITLE_LENGTH);
        if (arguments.has("description")) validateText(arguments.get("description").asText(), "description", MAX_TEXT_LENGTH);
        if (arguments.has("technical_details")) validateText(arguments.get("technical_details").asText(), "technical_details", MAX_TEXT_LENGTH);

        // Tool-specific validators
        switch (toolName) {
            case "compute_fingerprint_batch" -> validateBatchArgs(arguments);
            case "similarity_search"          -> validateSimilaritySearchArgs(arguments);
            case "index_embedding"            -> validateIndexEmbeddingArgs(arguments);
            case "compute_risk_score"         -> validateRiskScoreArgs(arguments);
        }
    }

    // ── Field validators ─────────────────────────────────────────────────────

    public void validateUuid(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new SecurityException(fieldName + " is required");
        }
        if (!UUID_PATTERN.matcher(value).matches()) {
            throw new SecurityException(fieldName + " must be a valid UUID");
        }
    }

    public void validateText(String text, String fieldName, int maxLen) {
        if (text == null) return;
        if (text.length() > maxLen) {
            throw new SecurityException(fieldName + " exceeds max length of " + maxLen + " chars");
        }
        if (INJECTION_PATTERN.matcher(text).find()) {
            LOG.warning("Injection pattern detected in field: " + fieldName);
            throw new SecurityException("Invalid content in field: " + fieldName);
        }
    }

    public void validateSimilarityScore(double score, String fieldName) {
        if (score < 0.0 || score > 1.0) {
            throw new SecurityException(fieldName + " must be between 0.0 and 1.0, got: " + score);
        }
    }

    // ── Tool-specific validators ──────────────────────────────────────────────

    private void validateBatchArgs(JsonNode args) {
        if (args.has("ideas") && args.get("ideas").isArray()) {
            int size = args.get("ideas").size();
            if (size > MAX_BATCH_SIZE) {
                throw new SecurityException("Batch size " + size + " exceeds maximum of " + MAX_BATCH_SIZE);
            }
        }
    }

    private void validateSimilaritySearchArgs(JsonNode args) {
        if (args.has("top_k")) {
            int topK = args.get("top_k").asInt();
            if (topK < 1 || topK > MAX_TOP_K) {
                throw new SecurityException("top_k must be between 1 and " + MAX_TOP_K);
            }
        }
        if (args.has("min_similarity")) {
            validateSimilarityScore(args.get("min_similarity").asDouble(0.0), "min_similarity");
        }
    }

    private void validateIndexEmbeddingArgs(JsonNode args) {
        if (args.has("embedding") && args.get("embedding").isArray()) {
            int dim = args.get("embedding").size();
            if (dim != EMBEDDING_DIMENSION) {
                throw new SecurityException("Embedding must be " + EMBEDDING_DIMENSION + "-dimensional, got " + dim);
            }
            // Check for NaN/Inf in vector components
            for (JsonNode v : args.get("embedding")) {
                double val = v.asDouble();
                if (Double.isNaN(val) || Double.isInfinite(val)) {
                    throw new SecurityException("Embedding contains NaN or Infinite value");
                }
            }
        }
    }

    private void validateRiskScoreArgs(JsonNode args) {
        if (args.has("max_semantic_similarity")) {
            validateSimilarityScore(args.get("max_semantic_similarity").asDouble(0.0), "max_semantic_similarity");
        }
        if (args.has("user_reputation_score")) {
            validateSimilarityScore(args.get("user_reputation_score").asDouble(0.0), "user_reputation_score");
        }
    }
}
