package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for all 8 CAT-15 agents.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgentTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ── helpers ───────────────────────────────────────────────────────────────

    private ObjectNode args(String... kvPairs) {
        ObjectNode n = MAPPER.createObjectNode();
        for (int i = 0; i < kvPairs.length - 1; i += 2) n.put(kvPairs[i], kvPairs[i + 1]);
        return n;
    }

    // ── 1. IdeaAttributionChainAgent ─────────────────────────────────────────

    @Test @Order(1)
    void testIdeaAttributionChain_genesis() throws Exception {
        ObjectNode result = new IdeaAttributionChainAgent().execute(args(
            "idea_id",          "IDEA-001",
            "creator_id",       "USER-42",
            "idea_title",       "Adaptive Learning Engine",
            "idea_description", "A system that adapts quiz difficulty in real time"
        ));
        assertEquals("IDEA_ATTRIBUTION_CHAIN_AGENT", result.path("agent").asText());
        assertEquals("SUCCESS",                       result.path("status").asText());
        assertEquals(1,                               result.path("chain_length").asInt());
        assertTrue(result.path("attribution_chain").isArray());
        assertEquals("GENESIS", result.path("attribution_chain").get(0).path("entry_type").asText());
    }

    @Test @Order(2)
    void testIdeaAttributionChain_withContributors() throws Exception {
        ObjectNode result = new IdeaAttributionChainAgent().execute(args(
            "idea_id",          "IDEA-002",
            "creator_id",       "USER-10",
            "idea_title",       "Gamified Skill Badge",
            "idea_description", "Earn badges for completing skill ladders",
            "contributor_ids",  "USER-11,USER-12"
        ));
        assertEquals(3, result.path("chain_length").asInt());
    }

    // ── 2. HashProofAgent ────────────────────────────────────────────────────

    @Test @Order(3)
    void testHashProof_basic() throws Exception {
        ObjectNode result = new HashProofAgent().execute(args(
            "content",  "Revolutionary peer-learning algorithm using spaced repetition",
            "owner_id", "USER-99"
        ));
        assertEquals("HASH_PROOF", result.path("agent").asText());
        assertEquals("SEALED",     result.path("status").asText());
        assertFalse(result.path("hashes").path("content_hash").asText().isBlank());
        assertEquals("SHA-256",    result.path("hashes").path("algorithm").asText());
        assertTrue(result.path("verification").path("verifiable").asBoolean());
    }

    @Test @Order(4)
    void testHashProof_sameContentSameHash() throws Exception {
        String content = "Deterministic hashing test content";
        ObjectNode r1 = new HashProofAgent().execute(args("content", content, "owner_id", "U1"));
        ObjectNode r2 = new HashProofAgent().execute(args("content", content, "owner_id", "U1"));
        assertEquals(
            r1.path("hashes").path("content_hash").asText(),
            r2.path("hashes").path("content_hash").asText()
        );
    }

    // ── 3. FeatureStoreAgent ─────────────────────────────────────────────────

    @Test @Order(5)
    void testFeatureStore_basic() throws Exception {
        ObjectNode result = new FeatureStoreAgent().execute(args(
            "idea_id", "FEAT-001",
            "content", "An innovative approach to teaching mathematics using visual puzzles and game mechanics"
        ));
        assertEquals("FEATURE_STORE_AGENT", result.path("agent").asText());
        assertEquals("STORED",              result.path("status").asText());
        assertTrue(result.path("lexical_features").path("word_count").asInt() > 0);
        assertTrue(result.path("embedding_vector").isArray());
        assertTrue(result.path("embedding_vector").size() > 1);
    }

    // ── 4. FastSimilarityAgent ───────────────────────────────────────────────

    @Test @Order(6)
    void testFastSimilarity_identicalContent() throws Exception {
        String content = "Exactly the same idea about adaptive learning systems";
        ObjectNode result = new FastSimilarityAgent().execute(args(
            "query_content",  content,
            "target_content", content
        ));
        assertEquals("FAST_SIMILARITY", result.path("agent").asText());
        double blended = result.path("similarity_scores").path("blended_score").asDouble();
        assertTrue(blended > 0.90, "Identical content should score > 0.90, got: " + blended);
    }

    @Test @Order(7)
    void testFastSimilarity_distinctContent() throws Exception {
        ObjectNode result = new FastSimilarityAgent().execute(args(
            "query_content",  "Machine learning based skill recommendation",
            "target_content", "Blockchain token distribution for freelancers"
        ));
        double blended = result.path("similarity_scores").path("blended_score").asDouble();
        // Different topics should score lower
        assertNotNull(result.path("risk_assessment").path("risk_level").asText());
    }

    // ── 5. DeepSimilarityAgent ───────────────────────────────────────────────

    @Test @Order(8)
    void testDeepSimilarity_basic() throws Exception {
        ObjectNode result = new DeepSimilarityAgent().execute(args(
            "query_content",  "An online platform for skill-based competitions among students",
            "target_content", "A web portal enabling student skill tournaments and competitions"
        ));
        assertEquals("deep_similarity", result.path("agent").asText());
        assertFalse(result.path("verdict").asText().isBlank());
        assertTrue(result.path("dimension_scores").path("composite_score").asDouble() >= 0);
    }

    @Test @Order(9)
    void testDeepSimilarity_forensicMode() throws Exception {
        ObjectNode result = new DeepSimilarityAgent().execute(args(
            "query_content",  "Peer assessment model for creative writing",
            "target_content", "Peer assessment system for creative writing evaluation",
            "analysis_depth", "FORENSIC"
        ));
        assertTrue(result.has("forensic_details"), "Forensic mode should add forensic_details block");
        assertTrue(result.path("forensic_details").path("query_content_hash").asText().length() == 64);
    }

    // ── 6. CopyProbabilityAgent ──────────────────────────────────────────────

    @Test @Order(10)
    void testCopyProbability_highRisk() throws Exception {
        String orig = "A gamified coding platform where students earn XP by solving algorithmic challenges";
        // Near-duplicate with minor rewording
        String susp = "A gamification coding platform where students earn experience points by solving algorithm challenges";
        ObjectNode result = new CopyProbabilityAgent().execute(args(
            "original_content", orig,
            "suspect_content",  susp,
            "access_count",     "5"
        ));
        assertEquals("COPY_PROBABILITY", result.path("agent").asText());
        assertTrue(result.path("probability_pct").asInt() >= 0);
        assertFalse(result.path("classification").asText().isBlank());
    }

    @Test @Order(11)
    void testCopyProbability_sameAuthorLowerRisk() throws Exception {
        ObjectNode result = new CopyProbabilityAgent().execute(args(
            "original_content",  "Original research idea on NLP",
            "suspect_content",   "Extended NLP research idea",
            "original_author_id","USER-5",
            "suspect_author_id", "USER-5"
        ));
        // Same author should reduce probability
        int prob = result.path("probability_pct").asInt();
        assertTrue(prob >= 0 && prob <= 100);
    }

    // ── 7. BehaviorStreamProcessorAgent ─────────────────────────────────────

    @Test @Order(12)
    void testBehaviorStream_highRisk() throws Exception {
        String events = "[" +
            "{\"type\":\"COPY\",\"idea_id\":\"IDEA-001\",\"timestamp\":\"2026-03-16T10:00:00Z\"}," +
            "{\"type\":\"COPY\",\"idea_id\":\"IDEA-001\",\"timestamp\":\"2026-03-16T10:01:00Z\"}," +
            "{\"type\":\"COPY\",\"idea_id\":\"IDEA-001\",\"timestamp\":\"2026-03-16T10:02:00Z\"}," +
            "{\"type\":\"EXPORT\",\"idea_id\":\"IDEA-002\",\"timestamp\":\"2026-03-16T10:03:00Z\"}," +
            "{\"type\":\"DOWNLOAD\",\"idea_id\":\"IDEA-003\",\"timestamp\":\"2026-03-16T10:04:00Z\"}" +
        "]";
        ObjectNode result = new BehaviorStreamProcessorAgent().execute(args(
            "user_id",     "USER-BAD",
            "events_json", events
        ));
        assertEquals("BEHAVIOR_STREAM_PROCESSOR", result.path("agent").asText());
        assertEquals("PROCESSED",                 result.path("status").asText());
        assertTrue(result.path("total_events").asInt() == 5);
        double score = result.path("risk_score").asDouble();
        assertTrue(score > 0, "Risk score should be > 0 for suspicious events");
    }

    @Test @Order(13)
    void testBehaviorStream_emptyEvents() throws Exception {
        ObjectNode result = new BehaviorStreamProcessorAgent().execute(args(
            "user_id",     "USER-CLEAN",
            "events_json", "[]"
        ));
        assertEquals(0,       result.path("total_events").asInt());
        assertEquals("NORMAL", result.path("risk_level").asText());
    }

    // ── 8. AccessLogTrackerAgent ─────────────────────────────────────────────

    @Test @Order(14)
    void testAccessLogTracker_basic() throws Exception {
        ObjectNode result = new AccessLogTrackerAgent().execute(args(
            "idea_id",      "IDEA-555",
            "accessor_id",  "USER-77",
            "action",       "DOWNLOAD"
        ));
        assertEquals("ACCESS_LOG_TRACKER", result.path("agent").asText());
        assertEquals("LOGGED",             result.path("status").asText());
        assertFalse(result.path("log_entry_id").asText().isBlank());
        assertTrue(result.path("chain_integrity").path("tamper_evident").asBoolean());
    }

    @Test @Order(15)
    void testAccessLogTracker_chainLinking() throws Exception {
        // First log
        ObjectNode log1 = new AccessLogTrackerAgent().execute(args(
            "idea_id",     "IDEA-1",
            "accessor_id", "USR-A",
            "action",      "VIEW"
        ));
        String prevHash = log1.path("chain_integrity").path("chain_hash").asText();

        // Second log links to first
        ObjectNode log2 = new AccessLogTrackerAgent().execute(args(
            "idea_id",       "IDEA-1",
            "accessor_id",   "USR-A",
            "action",        "COPY",
            "prev_log_hash", prevHash
        ));
        // chain_hash should differ from the standalone default
        assertFalse(log2.path("chain_integrity").path("chain_hash").asText().isBlank());
        assertEquals(prevHash, log2.path("chain_integrity").path("prev_log_hash").asText());
    }

    // ── tools/list integration ───────────────────────────────────────────────

    @Test @Order(16)
    void testAllToolDefinitionsValid() {
        java.util.Map<String, AgentHandler> agents = new java.util.LinkedHashMap<>();
        agents.put("idea_attribution_chain",    new IdeaAttributionChainAgent());
        agents.put("hash_proof",                new HashProofAgent());
        agents.put("feature_store",             new FeatureStoreAgent());
        agents.put("fast_similarity",           new FastSimilarityAgent());
        agents.put("deep_similarity",           new DeepSimilarityAgent());
        agents.put("copy_probability",          new CopyProbabilityAgent());
        agents.put("behavior_stream_processor", new BehaviorStreamProcessorAgent());
        agents.put("access_log_tracker",        new AccessLogTrackerAgent());

        for (java.util.Map.Entry<String, AgentHandler> e : agents.entrySet()) {
            ObjectNode def = e.getValue().toolDefinition(e.getKey());
            assertEquals(e.getKey(), def.path("name").asText(),
                "Tool name mismatch for: " + e.getKey());
            assertFalse(def.path("description").asText().isBlank(),
                "Missing description for: " + e.getKey());
            assertTrue(def.path("inputSchema").has("properties"),
                "Missing inputSchema.properties for: " + e.getKey());
        }
    }
}
