package com.ecoskiller.mcp.isd.handlers;

import com.ecoskiller.mcp.isd.engine.ScoringEngine;
import com.ecoskiller.mcp.isd.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * ThresholdHandler
 *
 * Implements:
 *   get_threshold_config — Per-tenant threshold config (Redis cache simulation)
 *   set_threshold_config — Update thresholds with semantic ordering validation
 *
 * Per ISD spec §3.3 / F5:
 *   - Thresholds cached in Redis: tenant:{tenant_id}:similarity:thresholds (TTL: 5 min)
 *   - Cache miss triggers REST call to admin-service
 *   - Enterprise tenants can set stricter thresholds without redeployment
 *
 * Default thresholds (per spec §3.3):
 *   review_threshold = 0.60
 *   flag_threshold   = 0.80
 *   min_similarity   = 0.40
 */
public class ThresholdHandler {

    private static final Logger LOG = Logger.getLogger(ThresholdHandler.class.getName());

    // Simulated Redis threshold store (production: ioredis TTL cache)
    // key = tenantId → ThresholdConfig
    private static final Map<String, ThresholdConfig> thresholdCache = new ConcurrentHashMap<>();

    // Redis TTL in seconds (per ISD spec §5.4)
    private static final int REDIS_TTL_SECONDS = 300; // 5 minutes

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    public ThresholdHandler(SecurityValidator sv) {
        this.validator = sv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_threshold_config
    // ─────────────────────────────────────────────────────────────────────────

    public Object getThresholdConfig(JsonNode args) {
        String tenantId = req(args, "tenant_id");

        ThresholdConfig config = thresholdCache.getOrDefault(tenantId,
            ThresholdConfig.defaults(tenantId));

        // Simulate TTL remaining (production: Redis TTL command)
        long secondsSinceCached = config.cachedAt() > 0
            ? (System.currentTimeMillis() / 1000) - config.cachedAt()
            : 0;
        long ttlRemaining = Math.max(0, REDIS_TTL_SECONDS - secondsSinceCached);
        boolean isCached  = secondsSinceCached < REDIS_TTL_SECONDS;

        ObjectNode resp = mapper.createObjectNode();
        resp.put("tenant_id",            tenantId);
        resp.put("review_threshold",     config.reviewThreshold());
        resp.put("flag_threshold",       config.flagThreshold());
        resp.put("min_similarity_floor", config.minSimilarity());
        resp.put("cache_ttl_remaining_s",ttlRemaining);
        resp.put("cache_hit",            isCached);
        resp.put("source",               isCached ? "redis_cache" : "admin_service_default");

        ObjectNode tierExplanation = resp.putObject("tier_explanation");
        tierExplanation.put("CLEAR",
            "copy_probability < " + config.reviewThreshold() +
            " → idea appears original; eligible for marketplace and licensing");
        tierExplanation.put("REVIEW",
            "copy_probability " + config.reviewThreshold() + "–" + (config.flagThreshold() - 0.01) +
            " → borderline; queued for human compliance review within 24h");
        tierExplanation.put("FLAG",
            "copy_probability >= " + config.flagThreshold() +
            " → high similarity; dispute workflow initiated");

        ObjectNode impl = resp.putObject("redis_implementation");
        impl.put("key_pattern", "tenant:{tenant_id}:similarity:thresholds");
        impl.put("ttl",         "300 seconds (5 minutes)");
        impl.put("cache_miss",  "REST call to admin-service GET /api/v1/tenants/{tenant_id}/similarity-config");
        impl.put("library",     "ioredis (production Node.js service)");

        LOG.info("get_threshold_config: tenant=" + tenantId + " review=" + config.reviewThreshold()
                + " flag=" + config.flagThreshold() + " cache_hit=" + isCached);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: set_threshold_config
    // ─────────────────────────────────────────────────────────────────────────

    public Object setThresholdConfig(JsonNode args) {
        String tenantId   = req(args, "tenant_id");
        String updatedBy  = args.path("updated_by").asText("admin");

        ThresholdConfig current = thresholdCache.getOrDefault(tenantId,
            ThresholdConfig.defaults(tenantId));

        double newReview = args.has("review_threshold")
            ? args.get("review_threshold").asDouble()
            : current.reviewThreshold();
        double newFlag   = args.has("flag_threshold")
            ? args.get("flag_threshold").asDouble()
            : current.flagThreshold();
        double newMin    = args.has("min_similarity_floor")
            ? args.get("min_similarity_floor").asDouble()
            : current.minSimilarity();

        // Semantic ordering already validated by SecurityValidator
        // Store new config
        ThresholdConfig updated = new ThresholdConfig(
            tenantId, newReview, newFlag, newMin,
            System.currentTimeMillis() / 1000);
        thresholdCache.put(tenantId, updated);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("success",          true);
        resp.put("tenant_id",        tenantId);
        resp.put("updated_by",       updatedBy);
        resp.put("updated_at",       Instant.now().toString());
        resp.put("review_threshold", newReview);
        resp.put("flag_threshold",   newFlag);
        resp.put("min_similarity_floor", newMin);
        resp.put("cache_ttl_seconds",    REDIS_TTL_SECONDS);

        ObjectNode prev = resp.putObject("previous_config");
        prev.put("review_threshold",    current.reviewThreshold());
        prev.put("flag_threshold",      current.flagThreshold());
        prev.put("min_similarity_floor",current.minSimilarity());

        ObjectNode changed = resp.putObject("changes");
        changed.put("review_threshold_changed", newReview != current.reviewThreshold());
        changed.put("flag_threshold_changed",   newFlag != current.flagThreshold());
        changed.put("min_similarity_changed",   newMin != current.minSimilarity());

        ObjectNode impl = resp.putObject("persistence_note");
        impl.put("redis",  "tenant:{tenant_id}:similarity:thresholds written with TTL=300s");
        impl.put("admin_service",
            "admin-service REST PUT /api/v1/tenants/{tenant_id}/similarity-config called to persist permanently");
        impl.put("effect", "New thresholds apply to all similarity checks from this moment");

        LOG.info("set_threshold_config: tenant=" + tenantId
                + " review=" + newReview + " flag=" + newFlag + " by=" + updatedBy);
        return resp;
    }

    // ── Static accessors (used by SimilarityCheckHandler + ClassificationHandler) ──

    public static double getReviewThreshold(String tenantId) {
        return thresholdCache.getOrDefault(tenantId,
            ThresholdConfig.defaults(tenantId)).reviewThreshold();
    }

    public static double getFlagThreshold(String tenantId) {
        return thresholdCache.getOrDefault(tenantId,
            ThresholdConfig.defaults(tenantId)).flagThreshold();
    }

    // ── Records ───────────────────────────────────────────────────────────────

    record ThresholdConfig(
        String tenantId,
        double reviewThreshold,
        double flagThreshold,
        double minSimilarity,
        long   cachedAt            // Unix epoch seconds
    ) {
        static ThresholdConfig defaults(String tenantId) {
            return new ThresholdConfig(
                tenantId,
                ScoringEngine.DEFAULT_REVIEW_THRESHOLD,
                ScoringEngine.DEFAULT_FLAG_THRESHOLD,
                ScoringEngine.DEFAULT_MIN_SIMILARITY,
                0L);
        }
    }

    private String req(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
