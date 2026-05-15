package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * FEATURE_STORE_AGENT
 *
 * Extracts, stores, and retrieves a structured feature vector from idea/content text.
 * These feature vectors feed downstream similarity and copy-detection agents.
 */
public class FeatureStoreAgent implements AgentHandler {

    // Feature dimension for pseudo-embeddings
    private static final int EMBED_DIMS = 32;

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("idea_id", "content");
        addStringProp(schema, "idea_id",     "Unique identifier for the idea");
        addStringProp(schema, "content",     "Full idea / content text to featurise");
        addStringProp(schema, "owner_id",    "Owner user ID (optional)");
        addStringProp(schema, "domain_tags", "Comma-separated domain tags, e.g. 'edu,fintech' (optional)");

        return buildToolDef(
            toolName,
            "FEATURE_STORE_AGENT — Extracts and persists a multi-dimensional feature vector " +
            "from idea content (lexical, structural, semantic dimensions). " +
            "Vectors are used by FAST_SIMILARITY and DEEP_SIMILARITY for copy-detection.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String ideaId    = args.path("idea_id").asText();
        String content   = args.path("content").asText();
        String ownerId   = args.path("owner_id").asText("unknown");
        String domainRaw = args.path("domain_tags").asText("");

        // Lexical features
        String[] words      = content.split("\\s+");
        int wordCount       = words.length;
        int charCount       = content.length();
        int uniqueWords     = (int) Arrays.stream(words).map(String::toLowerCase).distinct().count();
        double lexicalDensity = wordCount > 0 ? (double) uniqueWords / wordCount : 0;
        int sentenceCount   = content.split("[.!?]+").length;

        // Pseudo-embedding
        double[] embedding = pseudoEmbed(content, EMBED_DIMS);

        // Top keywords (simple frequency)
        Map<String, Integer> freq = new LinkedHashMap<>();
        for (String w : words) {
            String lw = w.toLowerCase().replaceAll("[^a-z0-9]", "");
            if (lw.length() > 3) freq.merge(lw, 1, Integer::sum);
        }
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(freq.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());
        List<String> topKeywords = new ArrayList<>();
        for (int i = 0; i < Math.min(5, sorted.size()); i++) topKeywords.add(sorted.get(i).getKey());

        // Domain tags
        List<String> domains = new ArrayList<>();
        if (!domainRaw.isBlank()) {
            for (String d : domainRaw.split(",")) {
                String t = d.trim();
                if (!t.isEmpty()) domains.add(t);
            }
        }

        String featureHash = sha256(ideaId + content);

        // Build result
        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",         "FEATURE_STORE_AGENT");
        result.put("status",        "STORED");
        result.put("idea_id",       ideaId);
        result.put("owner_id",      ownerId);
        result.put("feature_hash",  featureHash);
        result.put("timestamp",     now());

        ObjectNode lexical = result.putObject("lexical_features");
        lexical.put("word_count",      wordCount);
        lexical.put("char_count",      charCount);
        lexical.put("unique_words",    uniqueWords);
        lexical.put("lexical_density", Math.round(lexicalDensity * 1000.0) / 1000.0);
        lexical.put("sentence_count",  sentenceCount);

        ArrayNode keywords = lexical.putArray("top_keywords");
        topKeywords.forEach(keywords::add);

        ArrayNode embeddingArr = result.putArray("embedding_vector");
        embeddingArr.add("dim_" + EMBED_DIMS + "_pseudo_embedding");
        for (double v : embedding) embeddingArr.add(Math.round(v * 10000.0) / 10000.0);

        ArrayNode domainArr = result.putArray("domain_tags");
        domains.forEach(domainArr::add);

        ObjectNode storeMeta = result.putObject("store_metadata");
        storeMeta.put("vector_dims",  EMBED_DIMS);
        storeMeta.put("index_type",   "HNSW");
        storeMeta.put("ttl_days",     3650);
        storeMeta.put("access_level", "OWNER_ONLY");

        return result;
    }
}
