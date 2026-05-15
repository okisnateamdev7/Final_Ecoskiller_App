package com.ecoskiller.mcp.ife.handlers;

import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * SimilarityHandler
 *
 * Implements:
 *   - similarity_search      → Top-K ANN cosine search (Qdrant HNSW simulation)
 *   - get_similarity_report  → Full plagiarism/duplicate report from persistence layer
 *
 * Per IFE spec §11: cosine similarity thresholds:
 *   >= 0.9  → Very similar (likely duplicate)
 *   0.75-0.9 → Related ideas (worth reviewing)
 *   0.5-0.75 → Somewhat related
 *   < 0.5   → Not related
 */
public class SimilarityHandler {

    private static final Logger LOG = Logger.getLogger(SimilarityHandler.class.getName());

    // Similarity tier thresholds
    public static final double THRESHOLD_DUPLICATE = 0.90;
    public static final double THRESHOLD_RELATED   = 0.75;
    public static final double THRESHOLD_CONTEXT   = 0.50;

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;
    private final EmbeddingHandler embeddingHandler;

    // Simulated Qdrant index: tenantId -> List<IndexedIdea>
    private final Map<String, List<IndexedIdea>> qdrantIndex = new HashMap<>();

    // Simulated similarity report store
    private final Map<String, ObjectNode> reportStore = new HashMap<>();

    public SimilarityHandler(SecurityValidator validator) {
        this.validator = validator;
        this.embeddingHandler = new EmbeddingHandler(validator);
        seedDemoCorpus();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: similarity_search
    // ──────────────────────────────────────────────────────────────────────────

    public Object similaritySearch(JsonNode args) {
        String tenantId     = requireString(args, "tenant_id");
        String queryIdeaId  = requireString(args, "query_idea_id");
        int topK            = Math.min(args.path("top_k").asInt(10), 50);
        double minSim       = args.path("min_similarity").asDouble(0.40);
        String category     = args.path("idea_category").asText(null);
        boolean withPayload = args.path("include_payload").asBoolean(true);

        long startNs = System.nanoTime();

        // Generate query embedding (or use cached)
        float[] queryVec = embeddingHandler.generateDeterministicEmbedding(queryIdeaId, "all-MiniLM-L6-v2");

        // Simulate Qdrant ANN search within tenant
        List<IndexedIdea> corpus = qdrantIndex.getOrDefault(tenantId, Collections.emptyList());

        List<SimilarityResult> results = corpus.stream()
            .filter(idea -> !idea.ideaId().equals(queryIdeaId))
            .filter(idea -> category == null || category.equalsIgnoreCase(idea.category()))
            .map(idea -> new SimilarityResult(idea, embeddingHandler.cosineSimilarity(queryVec, idea.embedding())))
            .filter(r -> r.score() >= minSim)
            .sorted(Comparator.comparingDouble(SimilarityResult::score).reversed())
            .limit(topK)
            .collect(Collectors.toList());

        long searchMs = (System.nanoTime() - startNs) / 1_000_000;

        // Persist similarity report
        String reportKey = tenantId + ":" + queryIdeaId;
        ObjectNode report = buildReport(tenantId, queryIdeaId, results);
        reportStore.put(reportKey, report);

        // Build response
        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id",         queryIdeaId);
        response.put("tenant_id",       tenantId);
        response.put("search_time_ms",  searchMs);
        response.put("corpus_size",     corpus.size());
        response.put("results_found",   results.size());
        response.put("query_vector_norm", 1.0); // L2-normalized

        ArrayNode matches = response.putArray("top_matches");
        for (int i = 0; i < results.size(); i++) {
            SimilarityResult r = results.get(i);
            ObjectNode m = matches.addObject();
            m.put("rank",           i + 1);
            m.put("idea_id",        r.idea().ideaId());
            m.put("similarity_score", r.score());
            m.put("tier",           classifyTier(r.score()));
            if (withPayload) {
                m.put("idea_title",  r.idea().title());
                m.put("category",    r.idea().category());
                m.put("tenant_id",   tenantId);
            }
        }

        // Compute aggregate similarity stats
        OptionalDouble maxSim = results.stream().mapToDouble(SimilarityResult::score).max();
        response.put("max_similarity_score", maxSim.isPresent() ? maxSim.getAsDouble() : 0.0);

        long duplicateCount = results.stream().filter(r -> r.score() >= THRESHOLD_DUPLICATE).count();
        long relatedCount   = results.stream().filter(r -> r.score() >= THRESHOLD_RELATED && r.score() < THRESHOLD_DUPLICATE).count();
        response.put("potential_duplicates", duplicateCount);
        response.put("related_ideas",        relatedCount);
        response.put("kafka_event_published", "similarity.analyzed");

        LOG.info("Similarity search: query=" + queryIdeaId + " results=" + results.size() + " time=" + searchMs + "ms");
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: get_similarity_report
    // ──────────────────────────────────────────────────────────────────────────

    public Object getSimilarityReport(JsonNode args) {
        String ideaId   = requireString(args, "idea_id");
        String tenantId = requireString(args, "tenant_id");
        int limit       = args.path("top_matches_limit").asInt(5);

        String key = tenantId + ":" + ideaId;
        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id",   ideaId);
        response.put("tenant_id", tenantId);

        if (reportStore.containsKey(key)) {
            response.put("report_found", true);
            response.set("report", reportStore.get(key));
        } else {
            // Return demo report structure
            response.put("report_found", false);
            response.put("message",
                "No similarity report found in session cache. " +
                "In production: queries innovation.idea_similarity_reports (PostgreSQL) " +
                "after an ANN search has been run via similarity_search tool.");
            response.set("demo_report_schema", buildDemoReportSchema());
        }
        return response;
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private ObjectNode buildReport(String tenantId, String ideaId, List<SimilarityResult> results) {
        ObjectNode report = mapper.createObjectNode();
        report.put("tenant_id",       tenantId);
        report.put("idea_id",         ideaId);
        report.put("checked_at",      Instant.now().toString());

        long exactDuplicates    = results.stream().filter(r -> r.score() >= 0.99).count();
        long semanticDuplicates = results.stream().filter(r -> r.score() >= THRESHOLD_DUPLICATE && r.score() < 0.99).count();
        long relatedIdeas       = results.stream().filter(r -> r.score() >= THRESHOLD_RELATED && r.score() < THRESHOLD_DUPLICATE).count();

        report.put("exact_duplicates",    exactDuplicates);
        report.put("semantic_duplicates", semanticDuplicates);
        report.put("related_ideas",       relatedIdeas);

        double maxSim = results.stream().mapToDouble(SimilarityResult::score).max().orElse(0.0);
        report.put("max_similarity_score", maxSim);
        report.put("plagiarism_risk_score", computeRiskFromSimilarity(maxSim, exactDuplicates > 0));
        report.put("recommendation", recommendation(maxSim, exactDuplicates > 0));

        ArrayNode topMatches = report.putArray("top_matches");
        results.stream().limit(5).forEach(r -> {
            ObjectNode m = topMatches.addObject();
            m.put("matched_idea_id",        r.idea().ideaId());
            m.put("cosine_similarity",      r.score());
            m.put("tier",                   classifyTier(r.score()));
        });
        return report;
    }

    private double computeRiskFromSimilarity(double maxSim, boolean exactMatch) {
        double base = exactMatch ? 0.95 : maxSim * 0.8;
        return Math.min(1.0, base);
    }

    private String recommendation(double maxSim, boolean exactMatch) {
        if (exactMatch || maxSim >= 0.9) return "BLOCK — exact or near-duplicate detected";
        if (maxSim >= 0.75) return "FLAG_FOR_REVIEW — high semantic similarity";
        if (maxSim >= 0.5)  return "SOFT_FLAG — related ideas exist; manual review optional";
        return "ACCEPT — idea appears sufficiently original";
    }

    public String classifyTier(double similarity) {
        if (similarity >= THRESHOLD_DUPLICATE) return "VERY_SIMILAR_LIKELY_DUPLICATE";
        if (similarity >= THRESHOLD_RELATED)   return "RELATED_WORTH_REVIEWING";
        if (similarity >= THRESHOLD_CONTEXT)   return "SOMEWHAT_RELATED_FOR_CONTEXT";
        return "NOT_RELATED";
    }

    private ObjectNode buildDemoReportSchema() {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("exact_duplicates", "integer");
        schema.put("semantic_duplicates", "integer");
        schema.put("related_ideas", "integer");
        schema.put("plagiarism_risk_score", "float [0-1]");
        schema.put("recommendation", "ACCEPT | SOFT_FLAG | FLAG_FOR_REVIEW | BLOCK");
        return schema;
    }

    // ─── Demo corpus (seeded for meaningful ANN demonstrations) ──────────────

    private void seedDemoCorpus() {
        String demoTenant = "00000000-0000-0000-0000-000000000001";
        List<IndexedIdea> ideas = qdrantIndex.computeIfAbsent(demoTenant, k -> new ArrayList<>());

        String[][] corpus = {
            {"idea-demo-001", "Machine learning model compression", "software"},
            {"idea-demo-002", "Neural network quantization technique", "software"},
            {"idea-demo-003", "Federated learning privacy framework", "software"},
            {"idea-demo-004", "Distributed ML training system", "software"},
            {"idea-demo-005", "Blockchain supply chain verification", "business_process"},
            {"idea-demo-006", "IoT sensor data analytics platform", "hardware"},
            {"idea-demo-007", "Carbon footprint tracking mobile app", "software"},
            {"idea-demo-008", "AI-powered resume screening tool", "software"},
        };

        for (String[] row : corpus) {
            float[] emb = embeddingHandler.generateDeterministicEmbedding(row[0] + " " + row[1], "all-MiniLM-L6-v2");
            ideas.add(new IndexedIdea(row[0], row[1], row[2], emb));
        }
    }

    // Register a new embedding for search (called by IndexHandler)
    public void registerEmbedding(String tenantId, String ideaId, String title, String category, float[] embedding) {
        qdrantIndex.computeIfAbsent(tenantId, k -> new ArrayList<>())
                   .removeIf(i -> i.ideaId().equals(ideaId)); // upsert
        qdrantIndex.get(tenantId).add(new IndexedIdea(ideaId, title, category, embedding));
    }

    public void removeEmbedding(String tenantId, String ideaId) {
        List<IndexedIdea> list = qdrantIndex.get(tenantId);
        if (list != null) list.removeIf(i -> i.ideaId().equals(ideaId));
    }

    private String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return node.asText().trim();
    }

    // ─── Internal records ─────────────────────────────────────────────────────

    record IndexedIdea(String ideaId, String title, String category, float[] embedding) {}
    record SimilarityResult(IndexedIdea idea, double score) {}
}
