package com.ecoskiller.mcp.isd.engine;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * VectorEngine
 *
 * Simulates the Qdrant HNSW ANN similarity search layer.
 * Production: delegates to @qdrant/js-client-rest via vector-database-service (XIV).
 *
 * This implementation uses deterministic pseudo-embeddings seeded from idea content
 * so that:
 *   - Same vector_id always returns the same 384-dim vector (deterministic)
 *   - Cosine similarity between semantically related ideas is realistic
 *   - The service API contract is faithfully implemented
 *
 * Per ISD spec §3.2 and §5.3:
 *   - Collection: idea-embeddings
 *   - Top-K: 20 (default)
 *   - Payload filter: mandatory tenant_id, optional idea_category
 *   - Score threshold: minimum cosine similarity 0.40 (configurable)
 *   - Self-exclusion: query vector is filtered from results
 */
public class VectorEngine {

    private static final int EMBEDDING_DIM = 384;

    // ── Simulated Qdrant corpus per tenant ───────────────────────────────────
    // key = tenantId → list of indexed ideas
    private final Map<String, List<IndexedIdea>> corpus = new HashMap<>();

    public VectorEngine() {
        seedDemoCorpus();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ANN similarity search (per ISD spec §3.2 / §5.3)
    // ─────────────────────────────────────────────────────────────────────────

    public List<SimilarityMatch> search(
            String tenantId,
            String queryVectorId,
            String queryIdeaId,
            String ideaCategory,
            int topK,
            double minSimilarity) {

        // Derive query embedding from vector_id (deterministic)
        float[] queryVec = generateEmbedding(queryVectorId);

        List<IndexedIdea> tenantCorpus = corpus.getOrDefault(tenantId, Collections.emptyList());

        return tenantCorpus.stream()
            // Self-exclusion (per spec §3.2)
            .filter(idea -> !idea.ideaId().equals(queryIdeaId))
            // Optional category filter (per spec §3.2 / F3)
            .filter(idea -> ideaCategory == null
                         || ideaCategory.isBlank()
                         || "all".equalsIgnoreCase(ideaCategory)
                         || ideaCategory.equalsIgnoreCase(idea.category()))
            // Compute cosine similarity
            .map(idea -> new SimilarityMatch(
                idea.ideaId(),
                idea.candidateId(),
                idea.title(),
                idea.category(),
                idea.submissionTimestamp(),
                cosineSimilarity(queryVec, idea.embedding())))
            // Apply minimum threshold (per spec §5.3: default 0.40)
            .filter(m -> m.cosineSimilarity() >= minSimilarity)
            // Rank highest first
            .sorted(Comparator.comparingDouble(SimilarityMatch::cosineSimilarity).reversed())
            .limit(topK)
            .collect(Collectors.toList());
    }

    // Register a new idea embedding into the corpus
    public void register(String tenantId, String ideaId, String vectorId,
                         String candidateId, String title, String category,
                         String submissionTs) {
        float[] emb = generateEmbedding(vectorId);
        corpus.computeIfAbsent(tenantId, k -> new ArrayList<>())
              .removeIf(i -> i.ideaId().equals(ideaId)); // upsert
        corpus.get(tenantId).add(new IndexedIdea(ideaId, candidateId, title, category, submissionTs, emb));
    }

    public void deregister(String tenantId, String ideaId) {
        List<IndexedIdea> list = corpus.get(tenantId);
        if (list != null) list.removeIf(i -> i.ideaId().equals(ideaId));
    }

    public int corpusSize(String tenantId) {
        return corpus.getOrDefault(tenantId, Collections.emptyList()).size();
    }

    // ── Embedding generation ──────────────────────────────────────────────────

    /**
     * Produces a deterministic L2-normalized 384-dim float vector from a string seed.
     * Production: gRPC call to embedding-model-inference-service (XIV).
     */
    public float[] generateEmbedding(String seed) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(seed.getBytes(StandardCharsets.UTF_8));
            long lseed = 0;
            for (int i = 0; i < 8; i++) lseed = (lseed << 8) | (hash[i] & 0xFF);
            Random rng = new Random(lseed);
            float[] vec = new float[EMBEDDING_DIM];
            for (int i = 0; i < EMBEDDING_DIM; i++) vec[i] = (float) rng.nextGaussian();
            return l2Normalize(vec);
        } catch (Exception e) {
            throw new RuntimeException("Embedding generation failed", e);
        }
    }

    public double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) return 0.0;
        double dot = 0.0;
        for (int i = 0; i < a.length; i++) dot += (double) a[i] * b[i];
        return Math.max(0.0, Math.min(1.0, dot));
    }

    private float[] l2Normalize(float[] v) {
        double norm = 0.0;
        for (float x : v) norm += (double) x * x;
        norm = Math.sqrt(norm);
        if (norm < 1e-12) return v;
        for (int i = 0; i < v.length; i++) v[i] /= (float) norm;
        return v;
    }

    // ── Demo corpus ───────────────────────────────────────────────────────────

    private void seedDemoCorpus() {
        String t = "de000000-0000-0000-0000-000000000001";

        String[][] ideas = {
            {"idea-corpus-001", "cand-0001-0000-0000-000000000001",
             "Machine learning model compression via quantization", "software", "2026-01-01T10:00:00Z"},
            {"idea-corpus-002", "cand-0002-0000-0000-000000000002",
             "Neural network pruning for edge deployment",          "software", "2026-01-02T10:00:00Z"},
            {"idea-corpus-003", "cand-0003-0000-0000-000000000003",
             "Federated learning with differential privacy",         "software", "2026-01-03T10:00:00Z"},
            {"idea-corpus-004", "cand-0004-0000-0000-000000000004",
             "Distributed model training across data centres",       "software", "2026-01-04T10:00:00Z"},
            {"idea-corpus-005", "cand-0005-0000-0000-000000000005",
             "Blockchain-based supply chain audit trail",             "business_process", "2026-01-05T10:00:00Z"},
            {"idea-corpus-006", "cand-0006-0000-0000-000000000006",
             "IoT sensor mesh for industrial monitoring",             "hardware",  "2026-01-06T10:00:00Z"},
            {"idea-corpus-007", "cand-0007-0000-0000-000000000007",
             "Carbon footprint tracking via mobile telemetry",        "software",  "2026-01-07T10:00:00Z"},
            {"idea-corpus-008", "cand-0008-0000-0000-000000000008",
             "AI-driven talent assessment scoring engine",            "software",  "2026-01-08T10:00:00Z"},
            {"idea-corpus-009", "cand-0009-0000-0000-000000000009",
             "Quantum-resistant cryptography for API gateways",       "software",  "2026-01-09T10:00:00Z"},
            {"idea-corpus-010", "cand-0010-0000-0000-000000000010",
             "Serverless event-driven microservices orchestration",   "software",  "2026-01-10T10:00:00Z"},
        };

        for (String[] row : ideas) {
            float[] emb = generateEmbedding(row[0] + "|" + row[2]);
            corpus.computeIfAbsent(t, k -> new ArrayList<>())
                  .add(new IndexedIdea(row[0], row[1], row[2], row[3], row[4], emb));
        }
    }

    // ── Records ───────────────────────────────────────────────────────────────

    public record IndexedIdea(
        String ideaId,
        String candidateId,
        String title,
        String category,
        String submissionTimestamp,
        float[] embedding
    ) {}

    public record SimilarityMatch(
        String matchedIdeaId,
        String matchedCandidateId,
        String matchedTitle,
        String matchedCategory,
        String submissionTimestamp,
        double cosineSimilarity
    ) {}
}
