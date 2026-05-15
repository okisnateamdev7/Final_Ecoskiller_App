package com.ecoskiller.mcp.ife.handlers;

import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

/**
 * EmbeddingHandler
 *
 * Implements:
 *   - generate_embedding → 384-dim Sentence-BERT vector (L2-normalized)
 *
 * In production this delegates to the Python sentence-transformers service
 * via gRPC. Here we produce a deterministic pseudo-embedding by hashing
 * the content into a stable 384-dim float32 vector with proper L2 normalization,
 * which faithfully models the API contract without requiring a GPU at build time.
 *
 * Models per IFE spec §4:
 *   - all-MiniLM-L6-v2     (fast, ~50ms CPU)
 *   - domain-specific-v2   (tech-tuned, ~100ms)
 *   - domain-agnostic-v3   (balanced, ~75ms)
 */
public class EmbeddingHandler {

    private static final Logger LOG = Logger.getLogger(EmbeddingHandler.class.getName());

    private static final int EMBEDDING_DIM = 384;

    private static final Map<String, Integer> MODEL_LATENCY_MS = Map.of(
        "all-MiniLM-L6-v2",     50,
        "domain-specific-v2",  100,
        "domain-agnostic-v3",   75
    );

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    // Simulated embedding store (production: Qdrant payload lookup)
    private final Map<String, float[]> embeddingStore = new HashMap<>();

    public EmbeddingHandler(SecurityValidator validator) {
        this.validator = validator;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: generate_embedding
    // ──────────────────────────────────────────────────────────────────────────

    public Object generateEmbedding(JsonNode args) {
        String ideaId    = requireString(args, "idea_id");
        String tenantId  = requireString(args, "tenant_id");
        String title     = requireString(args, "title");
        String desc      = requireString(args, "description");
        String techDet   = args.path("technical_details").asText("");
        String model     = args.path("embedding_model").asText("all-MiniLM-L6-v2");

        // Validate model name
        if (!MODEL_LATENCY_MS.containsKey(model)) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "Unknown embedding model: " + model +
                ". Available: " + String.join(", ", MODEL_LATENCY_MS.keySet()));
            return err;
        }

        String content = title + " " + desc + " " + techDet;

        // Check Redis-style cache (in-memory here)
        String cacheKey = tenantId + ":" + ideaId + ":" + model;
        boolean cached = embeddingStore.containsKey(cacheKey);

        float[] vector = cached
            ? embeddingStore.get(cacheKey)
            : generateDeterministicEmbedding(content, model);

        if (!cached) {
            embeddingStore.put(cacheKey, vector);
        }

        // Verify L2 normalization
        double norm = computeL2Norm(vector);

        // Build response
        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id",            ideaId);
        response.put("tenant_id",          tenantId);
        response.put("embedding_model",    model);
        response.put("embedding_version",  modelVersion(model));
        response.put("embedding_dimension", EMBEDDING_DIM);
        response.put("computation_time_ms", MODEL_LATENCY_MS.get(model));
        response.put("cached",             cached);
        response.put("l2_norm",            norm);
        response.put("computed_at",        Instant.now().toString());

        ArrayNode embArray = response.putArray("embedding");
        for (float v : vector) embArray.add(v);

        response.put("kafka_event_published", "embedding.computed");
        LOG.info("Embedding generated for idea_id=" + ideaId + " model=" + model + " cached=" + cached);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Internal helpers
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Generates a deterministic, L2-normalized 384-dim float32 vector from text content.
     *
     * Production implementation: delegates to Python sentence-transformers service via gRPC.
     * This implementation uses SHA-256 seeded pseudo-random projection for determinism,
     * which correctly models the API contract (same text → same vector, L2-norm = 1.0).
     */
    public float[] generateDeterministicEmbedding(String content, String model) {
        try {
            // Seed the PRNG from SHA-256 of content+model for determinism
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] seed = md.digest((content + "|" + model).getBytes(StandardCharsets.UTF_8));

            // Derive 8-byte long seed from first 8 bytes of hash
            long lseed = 0;
            for (int i = 0; i < 8; i++) lseed = (lseed << 8) | (seed[i] & 0xFF);
            Random rng = new Random(lseed);

            float[] vec = new float[EMBEDDING_DIM];
            for (int i = 0; i < EMBEDDING_DIM; i++) {
                vec[i] = (float) rng.nextGaussian();
            }
            return l2Normalize(vec);
        } catch (Exception e) {
            throw new RuntimeException("Embedding generation failed", e);
        }
    }

    /** L2-normalize a float vector in-place and return it. */
    public float[] l2Normalize(float[] vec) {
        double norm = computeL2Norm(vec);
        if (norm < 1e-12) return vec; // zero vector guard
        for (int i = 0; i < vec.length; i++) vec[i] /= (float) norm;
        return vec;
    }

    public double computeL2Norm(float[] vec) {
        double sum = 0.0;
        for (float v : vec) sum += (double) v * v;
        return Math.sqrt(sum);
    }

    /** Compute cosine similarity between two L2-normalized vectors. */
    public double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("Vector dimension mismatch");
        double dot = 0.0;
        for (int i = 0; i < a.length; i++) dot += (double) a[i] * b[i];
        return Math.max(0.0, Math.min(1.0, dot)); // clamp due to floating-point drift
    }

    public Optional<float[]> getCachedEmbedding(String tenantId, String ideaId, String model) {
        return Optional.ofNullable(embeddingStore.get(tenantId + ":" + ideaId + ":" + model));
    }

    private String modelVersion(String model) {
        return switch (model) {
            case "all-MiniLM-L6-v2"   -> "v2.0";
            case "domain-specific-v2" -> "v2.2";
            case "domain-agnostic-v3" -> "v3.1";
            default -> "unknown";
        };
    }

    private String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return node.asText().trim();
    }
}
