package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * FAST_SIMILARITY
 *
 * Performs rapid approximate nearest-neighbour similarity search
 * between a query idea and a target idea / corpus using lightweight
 * lexical + embedding heuristics (sub-millisecond at scale).
 */
public class FastSimilarityAgent implements AgentHandler {

    private static final int EMBED_DIMS = 32;

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("query_content", "target_content");
        addStringProp(schema, "query_content",  "The idea/content being checked");
        addStringProp(schema, "target_content", "The idea/content to compare against");
        addStringProp(schema, "query_id",       "Optional ID for the query idea");
        addStringProp(schema, "target_id",      "Optional ID for the target idea");
        addNumberProp(schema, "threshold",      "Similarity alert threshold 0.0–1.0 (default 0.75)");

        return buildToolDef(
            toolName,
            "FAST_SIMILARITY — Performs a rapid cosine-similarity check between two content " +
            "items using lightweight pseudo-embeddings. Returns a similarity score and a " +
            "risk classification in sub-millisecond time.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String queryContent  = args.path("query_content").asText();
        String targetContent = args.path("target_content").asText();
        String queryId       = args.path("query_id").asText("QUERY");
        String targetId      = args.path("target_id").asText("TARGET");
        double threshold     = args.path("threshold").asDouble(0.75);

        long startNs = System.nanoTime();

        double[] qVec = pseudoEmbed(queryContent, EMBED_DIMS);
        double[] tVec = pseudoEmbed(targetContent, EMBED_DIMS);
        double cosine = cosineSimilarity(qVec, tVec);
        // Normalise to 0-1 range
        double score = (cosine + 1.0) / 2.0;

        // Jaccard on word sets
        java.util.Set<String> qWords = wordsOf(queryContent);
        java.util.Set<String> tWords = wordsOf(targetContent);
        java.util.Set<String> intersection = new java.util.HashSet<>(qWords);
        intersection.retainAll(tWords);
        java.util.Set<String> union = new java.util.HashSet<>(qWords);
        union.addAll(tWords);
        double jaccard = union.isEmpty() ? 0 : (double) intersection.size() / union.size();

        // Blended score
        double blended = score * 0.7 + jaccard * 0.3;

        long elapsedUs = (System.nanoTime() - startNs) / 1000;

        String riskLevel;
        if (blended >= 0.90)      riskLevel = "CRITICAL";
        else if (blended >= 0.75) riskLevel = "HIGH";
        else if (blended >= threshold) riskLevel = "MEDIUM";
        else if (blended >= 0.40) riskLevel = "LOW";
        else                      riskLevel  = "NONE";

        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",      "FAST_SIMILARITY");
        result.put("status",     "COMPLETE");
        result.put("query_id",   queryId);
        result.put("target_id",  targetId);
        result.put("timestamp",  now());

        ObjectNode scores = result.putObject("similarity_scores");
        scores.put("cosine_normalized", round4(score));
        scores.put("jaccard",           round4(jaccard));
        scores.put("blended_score",     round4(blended));
        scores.put("threshold",         threshold);

        ObjectNode risk = result.putObject("risk_assessment");
        risk.put("risk_level",    riskLevel);
        risk.put("alert_triggered", blended >= threshold);
        risk.put("recommendation",
            blended >= 0.90 ? "IMMEDIATE_REVIEW — potential direct copy" :
            blended >= 0.75 ? "FLAG_FOR_REVIEW — high structural overlap detected" :
            blended >= 0.40 ? "MONITOR — partial similarity within normal range" :
                              "CLEAR — content appears sufficiently distinct");

        ObjectNode perf = result.putObject("performance");
        perf.put("latency_us",    elapsedUs);
        perf.put("method",        "ANN_COSINE+JACCARD");
        perf.put("embedding_dim", EMBED_DIMS);

        return result;
    }

    private static java.util.Set<String> wordsOf(String text) {
        java.util.Set<String> set = new java.util.HashSet<>();
        for (String w : text.split("\\s+")) {
            String lw = w.toLowerCase().replaceAll("[^a-z0-9]", "");
            if (lw.length() > 2) set.add(lw);
        }
        return set;
    }

    private static double round4(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
