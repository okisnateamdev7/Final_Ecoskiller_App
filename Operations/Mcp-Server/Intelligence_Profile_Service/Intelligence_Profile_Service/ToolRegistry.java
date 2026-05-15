package com.ecoskiller.mcp.ips.tools;

import com.ecoskiller.mcp.ips.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * ToolRegistry
 *
 * Central registry of all 15 IPS MCP tools.
 * Each entry stores: JSON Schema definition + handler function.
 */
public class ToolRegistry {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator security;

    /* tool-name → handler */
    private final Map<String, BiFunction<JsonNode, SecurityValidator.JwtClaims, JsonNode>> handlers
        = new HashMap<>();

    /* ordered list of tool definitions (JSON Schema) */
    private final ArrayNode toolDefs = mapper.createArrayNode();

    public ToolRegistry(SecurityValidator security) {
        this.security = security;
        register();
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Public API
    // ─────────────────────────────────────────────────────────────────────

    public ArrayNode getToolDefinitions() { return toolDefs; }

    public JsonNode dispatch(String toolName, JsonNode arguments) {
        // Authorize via RBAC (throws SecurityException on denial)
        SecurityValidator.JwtClaims claims = security.authorizeToolCall(toolName, arguments);

        BiFunction<JsonNode, SecurityValidator.JwtClaims, JsonNode> handler = handlers.get(toolName);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown tool: " + toolName);
        }
        return handler.apply(arguments, claims);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Tool registration
    // ─────────────────────────────────────────────────────────────────────

    private void register() {
        IntelligenceProfileTools ips = new IntelligenceProfileTools(security);
        EventProcessingTools     evt = new EventProcessingTools(security);
        AnalyticsTools           ana = new AnalyticsTools(security);
        AdminTools               adm = new AdminTools(security);

        // ── Profile read tools ──────────────────────────────────────────
        addTool("get_intelligence_profile",
            "Retrieve complete Intelligence DNA profile for a user — 8-type vector " +
            "(cognitive, behavioral, domain, learning_agility, personality, trajectory, risk, uniqueness), " +
            "skill vectors, peer benchmarks, and risk indicators. Returns cached result <50ms.",
            profileQuerySchema(),
            ips::getIntelligenceProfile);

        addTool("get_skill_vectors",
            "List skill vectors with proficiency levels, confidence scores, Sentence-BERT embeddings, " +
            "and peer percentile ranks for a user. Supports optional skill-name filter.",
            skillQuerySchema(),
            ips::getSkillVectors);

        addTool("search_candidates",
            "Skill-based candidate search using HNSW vector similarity (Qdrant). " +
            "Find top-N candidates matching required skills with similarity threshold, " +
            "optional cognitive/behavioral minimum filters. Returns match scores.",
            searchSchema(),
            ips::searchCandidates);

        addTool("get_peer_benchmarks",
            "Get anonymized peer percentile distributions for a skill or overall profile. " +
            "Returns 10th/25th/50th/75th/90th percentiles across tenant peer group.",
            peerBenchmarkSchema(),
            ips::getPeerBenchmarks);

        addTool("get_risk_assessment",
            "[Recruiter/Admin only] Retrieve risk indicators: churn probability, " +
            "training duration estimate, fairness flags, anomaly score, " +
            "offer acceptance probability, 12-month retention prediction.",
            riskSchema(),
            ips::getRiskAssessment);

        // ── Event processing tools ──────────────────────────────────────
        addTool("process_assessment_event",
            "[ml_system/admin] Ingest assessment.completed Kafka event — coding challenge scores, " +
            "interview ratings, problem-solving metrics. Updates cognitive and domain intelligence vectors.",
            assessmentEventSchema(),
            evt::processAssessmentEvent);

        addTool("process_skill_assessment",
            "[ml_system/admin] Ingest skill.assessment.result event — skill name, proficiency level, " +
            "confidence score. Triggers skill embedding recomputation via Sentence-BERT.",
            skillAssessmentSchema(),
            evt::processSkillAssessment);

        addTool("process_gd_discussion",
            "[ml_system/admin] Ingest gd.discussion.completed event — group discussion metrics: " +
            "speech_count, collaboration_score, idea_quality, sentiment. Updates behavioral vector.",
            gdDiscussionSchema(),
            evt::processGdDiscussion);

        addTool("process_learning_interaction",
            "[ml_system/admin] Ingest learning.resource.interaction event — course engagement, " +
            "time_spent, completion percentage, quiz scores. Updates learning_agility vector.",
            learningSchema(),
            evt::processLearningInteraction);

        // ── Analytics tools ─────────────────────────────────────────────
        addTool("compute_intelligence_dna",
            "[ml_system/admin] Trigger synchronous recomputation of the 8-type Intelligence DNA vector " +
            "for a user using LightGBM/XGBoost models. Applies time-decay weighting. " +
            "Returns updated vector with version number.",
            computeDnaSchema(),
            ana::computeIntelligenceDna);

        addTool("detect_profile_anomaly",
            "Run anomaly detection on a user profile — sudden score spikes, inconsistent assessments, " +
            "outlier scoring patterns. Returns anomaly score and flagged fields.",
            anomalySchema(),
            ana::detectProfileAnomaly);

        addTool("get_profile_history",
            "Retrieve immutable event-sourced version history of a user profile. " +
            "Supports temporal queries (profile state at a specific timestamp). " +
            "Useful for audit and post-hire performance correlation.",
            historySchema(),
            ana::getProfileHistory);

        // ── Admin tools ─────────────────────────────────────────────────
        addTool("recalculate_vectors",
            "[admin only] Force recomputation of all intelligence vectors for a single user " +
            "from the full event log. Use after data corrections or model updates.",
            singleUserSchema("Recalculate all intelligence vectors for a specific user"),
            adm::recalculateVectors);

        addTool("get_fairness_audit",
            "[admin only] Generate fairness audit report — Disparate Impact Ratio, " +
            "Equal Opportunity Difference across demographics. " +
            "Flags systematic bias patterns in profile distributions.",
            fairnessSchema(),
            adm::getFairnessAudit);

        addTool("rebuild_profiles",
            "[admin only] Full rebuild of ALL profiles from Kafka event log. " +
            "Disaster recovery operation. Returns job_id for async tracking.",
            rebuildSchema(),
            adm::rebuildProfiles);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Schema builders (JSON Schema for each tool)
    // ─────────────────────────────────────────────────────────────────────

    private ObjectNode authSchema() {
        ObjectNode auth = mapper.createObjectNode();
        auth.put("type", "object");
        ObjectNode authProps = auth.putObject("properties");
        authProps.putObject("token")
            .put("type", "string")
            .put("description", "JWT bearer token (issued by Ecoskiller auth-service / Keycloak)");
        auth.putArray("required").add("token");
        auth.put("description", "Authentication block — required on every tool call");
        return auth;
    }

    private ObjectNode profileQuerySchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type", "object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string").put("description","UUID of the user");
        p.putObject("tenantId").put("type","string").put("description","UUID of the tenant/hiring company");
        p.putObject("include").put("type","string")
            .put("description","Comma-separated fields: skills,risk-assessment,peer-benchmarks,embeddings")
            .put("default","all");
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode skillQuerySchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type", "object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("skillFilter").put("type","string")
            .put("description","Optional skill name filter (e.g. 'Python')");
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode searchSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type", "object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("tenantId").put("type","string");
        ObjectNode skills = p.putObject("skills");
        skills.put("type","array");
        skills.putObject("items").put("type","string");
        skills.put("description","Required skills (e.g. ['Python','Kubernetes','AWS'])");
        p.putObject("skillMinSimilarity").put("type","number").put("default",0.75)
            .put("description","Minimum cosine similarity threshold [0,1]");
        p.putObject("cognitiveMin").put("type","number").put("default",0.0);
        p.putObject("behavioralMin").put("type","number").put("default",0.0);
        p.putObject("limit").put("type","integer").put("default",50).put("maximum",200);
        s.putArray("required").add("_auth").add("tenantId").add("skills");
        return s;
    }

    private ObjectNode peerBenchmarkSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type", "object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("tenantId").put("type","string");
        p.putObject("skillName").put("type","string")
            .put("description","Skill to benchmark (e.g. 'Python'). Omit for overall profile.");
        s.putArray("required").add("_auth").add("tenantId");
        return s;
    }

    private ObjectNode riskSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type", "object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode assessmentEventSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("assessmentType").put("type","string")
            .put("description","coding_challenge | technical_interview | behavioral_interview");
        ObjectNode scores = p.putObject("scores");
        scores.put("type","object");
        ObjectNode sp = scores.putObject("properties");
        sp.putObject("cognitive").put("type","number");
        sp.putObject("domain").put("type","number");
        sp.putObject("speed").put("type","number");
        sp.putObject("correctness").put("type","number");
        p.putObject("durationSeconds").put("type","integer");
        p.putObject("idempotencyKey").put("type","string")
            .put("description","UUID to prevent duplicate event processing");
        s.putArray("required").add("_auth").add("userId").add("tenantId")
            .add("assessmentType").add("scores").add("idempotencyKey");
        return s;
    }

    private ObjectNode skillAssessmentSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("skillName").put("type","string");
        p.putObject("proficiencyLevel").put("type","integer").put("minimum",1).put("maximum",5);
        p.putObject("confidenceScore").put("type","number").put("minimum",0).put("maximum",1);
        p.putObject("idempotencyKey").put("type","string");
        s.putArray("required").add("_auth").add("userId").add("tenantId")
            .add("skillName").add("proficiencyLevel").add("confidenceScore").add("idempotencyKey");
        return s;
    }

    private ObjectNode gdDiscussionSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("sessionId").put("type","string");
        p.putObject("speechCount").put("type","integer");
        p.putObject("collaborationScore").put("type","number").put("minimum",0).put("maximum",1);
        p.putObject("ideaQuality").put("type","number").put("minimum",0).put("maximum",1);
        p.putObject("sentimentScore").put("type","number").put("minimum",-1).put("maximum",1);
        p.putObject("idempotencyKey").put("type","string");
        s.putArray("required").add("_auth").add("userId").add("tenantId")
            .add("sessionId").add("idempotencyKey");
        return s;
    }

    private ObjectNode learningSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("resourceId").put("type","string");
        p.putObject("resourceType").put("type","string").put("description","course | workshop | video");
        p.putObject("timeSpentMinutes").put("type","integer");
        p.putObject("completionPercentage").put("type","number").put("minimum",0).put("maximum",100);
        p.putObject("quizScore").put("type","number").put("minimum",0).put("maximum",1);
        p.putObject("idempotencyKey").put("type","string");
        s.putArray("required").add("_auth").add("userId").add("tenantId")
            .add("resourceId").add("idempotencyKey");
        return s;
    }

    private ObjectNode computeDnaSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("forceFullRecompute").put("type","boolean").put("default",false)
            .put("description","If true, recompute from full event history (slow). Default: incremental.");
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode anomalySchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("sensitivityThreshold").put("type","number").put("default",0.3)
            .put("description","Anomaly score above this triggers flag [0,1]");
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode historySchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        p.putObject("fromTimestamp").put("type","string").put("format","date-time");
        p.putObject("toTimestamp").put("type","string").put("format","date-time");
        p.putObject("limit").put("type","integer").put("default",20).put("maximum",100);
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode singleUserSchema(String description) {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        s.put("description", description);
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("userId").put("type","string");
        p.putObject("tenantId").put("type","string");
        s.putArray("required").add("_auth").add("userId").add("tenantId");
        return s;
    }

    private ObjectNode fairnessSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("tenantId").put("type","string");
        p.putObject("fromDate").put("type","string").put("format","date");
        p.putObject("toDate").put("type","string").put("format","date");
        p.putObject("demographic").put("type","string")
            .put("description","Filter: gender | ethnicity | age_group | geography");
        s.putArray("required").add("_auth").add("tenantId");
        return s;
    }

    private ObjectNode rebuildSchema() {
        ObjectNode s = mapper.createObjectNode();
        s.put("type","object");
        ObjectNode p = s.putObject("properties");
        p.set("_auth", authSchema());
        p.putObject("tenantId").put("type","string")
            .put("description","Scope rebuild to a single tenant (omit for global)");
        p.putObject("confirm").put("type","boolean")
            .put("description","Must be true to execute (safety gate)");
        s.putArray("required").add("_auth").add("confirm");
        return s;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Helper — register a single tool
    // ─────────────────────────────────────────────────────────────────────

    private void addTool(String name, String description, ObjectNode inputSchema,
                         BiFunction<JsonNode, SecurityValidator.JwtClaims, JsonNode> handler) {
        ObjectNode def = mapper.createObjectNode();
        def.put("name", name);
        def.put("description", description);
        def.set("inputSchema", inputSchema);
        toolDefs.add(def);
        handlers.put(name, handler);
    }
}
