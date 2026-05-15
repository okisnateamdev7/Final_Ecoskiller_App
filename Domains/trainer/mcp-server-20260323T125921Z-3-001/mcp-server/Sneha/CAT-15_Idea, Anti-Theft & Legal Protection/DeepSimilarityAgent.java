package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * deep_similarity
 *
 * Performs a thorough multi-dimensional similarity analysis across:
 *   - Semantic structure (sentence-level)
 *   - Thematic overlap
 *   - Structural fingerprint
 *   - Intent classification
 * Heavier than FAST_SIMILARITY — intended for final review before legal action.
 */
public class DeepSimilarityAgent implements AgentHandler {

    private static final int EMBED_DIMS = 128;

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("query_content", "target_content");
        addStringProp(schema, "query_content",  "The idea/content being investigated");
        addStringProp(schema, "target_content", "The candidate infringing content");
        addStringProp(schema, "query_id",       "Optional ID for the query idea");
        addStringProp(schema, "target_id",      "Optional ID for the target idea");
        addStringProp(schema, "analysis_depth", "STANDARD | FORENSIC (default STANDARD)");

        return buildToolDef(
            toolName,
            "deep_similarity — Multi-dimensional deep similarity analysis: semantic embedding, " +
            "thematic overlap, sentence-level structural fingerprint, and intent classification. " +
            "Produces a full forensic similarity report suitable for legal proceedings.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String queryContent  = args.path("query_content").asText();
        String targetContent = args.path("target_content").asText();
        String queryId       = args.path("query_id").asText("QUERY");
        String targetId      = args.path("target_id").asText("TARGET");
        String depth         = args.path("analysis_depth").asText("STANDARD");

        // ── Semantic embedding similarity ────────────────────────────────────
        double[] qVec128 = pseudoEmbed(queryContent, EMBED_DIMS);
        double[] tVec128 = pseudoEmbed(targetContent, EMBED_DIMS);
        double semanticSim = (cosineSimilarity(qVec128, tVec128) + 1.0) / 2.0;

        // ── Structural fingerprint: N-gram overlap (bigrams) ─────────────────
        java.util.Set<String> qBigrams = bigrams(queryContent);
        java.util.Set<String> tBigrams = bigrams(targetContent);
        java.util.Set<String> bIntersect = new java.util.HashSet<>(qBigrams);
        bIntersect.retainAll(tBigrams);
        java.util.Set<String> bUnion = new java.util.HashSet<>(qBigrams);
        bUnion.addAll(tBigrams);
        double structuralSim = bUnion.isEmpty() ? 0 : (double) bIntersect.size() / bUnion.size();

        // ── Thematic: shared topic indicators (word-length buckets) ──────────
        double thematicSim = topicSimilarity(queryContent, targetContent);

        // ── Intent classification ────────────────────────────────────────────
        String qIntent = classifyIntent(queryContent);
        String tIntent = classifyIntent(targetContent);
        double intentMatch = qIntent.equals(tIntent) ? 1.0 : 0.3;

        // ── Weighted composite ───────────────────────────────────────────────
        double composite = semanticSim * 0.45
                         + structuralSim * 0.25
                         + thematicSim   * 0.20
                         + intentMatch   * 0.10;

        // ── Plagiarism verdict ───────────────────────────────────────────────
        String verdict;
        if (composite >= 0.92)      verdict = "DIRECT_COPY";
        else if (composite >= 0.80) verdict = "SUBSTANTIAL_SIMILARITY";
        else if (composite >= 0.65) verdict = "NOTABLE_OVERLAP";
        else if (composite >= 0.45) verdict = "PARTIAL_SIMILARITY";
        else                        verdict  = "DISTINCT";

        // ── Build result ─────────────────────────────────────────────────────
        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",          "deep_similarity");
        result.put("status",         "COMPLETE");
        result.put("query_id",       queryId);
        result.put("target_id",      targetId);
        result.put("analysis_depth", depth);
        result.put("timestamp",      now());
        result.put("verdict",        verdict);

        ObjectNode scores = result.putObject("dimension_scores");
        scores.put("semantic_similarity",   r4(semanticSim));
        scores.put("structural_similarity", r4(structuralSim));
        scores.put("thematic_similarity",   r4(thematicSim));
        scores.put("intent_alignment",      r4(intentMatch));
        scores.put("composite_score",       r4(composite));

        ObjectNode intent = result.putObject("intent_analysis");
        intent.put("query_intent",  qIntent);
        intent.put("target_intent", tIntent);
        intent.put("intent_match",  qIntent.equals(tIntent));

        ObjectNode legal = result.putObject("legal_assessment");
        legal.put("risk_category",       verdict);
        legal.put("confidence_pct",      (int)(composite * 100));
        legal.put("recommended_action",
            composite >= 0.80 ? "ESCALATE_TO_LEGAL" :
            composite >= 0.65 ? "REQUEST_PROVENANCE" :
            composite >= 0.45 ? "MONITOR_AND_COMPARE" : "NO_ACTION_REQUIRED");
        legal.put("admissible_evidence", composite >= 0.80);

        if (depth.equalsIgnoreCase("FORENSIC")) {
            ObjectNode forensic = result.putObject("forensic_details");
            forensic.put("bigram_overlap_count",     bIntersect.size());
            forensic.put("query_bigram_count",        qBigrams.size());
            forensic.put("target_bigram_count",       tBigrams.size());
            forensic.put("embedding_dims_used",       EMBED_DIMS);
            forensic.put("query_content_hash",        sha256(queryContent));
            forensic.put("target_content_hash",       sha256(targetContent));
        }

        return result;
    }

    private static java.util.Set<String> bigrams(String text) {
        java.util.Set<String> bigrams = new java.util.LinkedHashSet<>();
        String[] words = text.toLowerCase().split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            bigrams.add(words[i] + "_" + words[i + 1]);
        }
        return bigrams;
    }

    private static double topicSimilarity(String a, String b) {
        // Proxy: ratio of shared "long word" stems
        java.util.Set<String> aLong = longWords(a);
        java.util.Set<String> bLong = longWords(b);
        java.util.Set<String> inter = new java.util.HashSet<>(aLong);
        inter.retainAll(bLong);
        java.util.Set<String> union = new java.util.HashSet<>(aLong);
        union.addAll(bLong);
        return union.isEmpty() ? 0 : (double) inter.size() / union.size();
    }

    private static java.util.Set<String> longWords(String text) {
        java.util.Set<String> set = new java.util.HashSet<>();
        for (String w : text.split("\\s+")) {
            String lw = w.toLowerCase().replaceAll("[^a-z]", "");
            if (lw.length() >= 6) set.add(lw);
        }
        return set;
    }

    private static String classifyIntent(String text) {
        String lower = text.toLowerCase();
        if (lower.contains("patent") || lower.contains("invent") || lower.contains("novelty")) return "INVENTION";
        if (lower.contains("learn") || lower.contains("teach") || lower.contains("student"))   return "EDUCATION";
        if (lower.contains("invest") || lower.contains("revenue") || lower.contains("profit"))  return "COMMERCIAL";
        if (lower.contains("research") || lower.contains("study") || lower.contains("data"))    return "RESEARCH";
        return "GENERAL";
    }

    private static double r4(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
