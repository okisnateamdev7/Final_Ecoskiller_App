package com.ecoskiller.mcp.ips.tools;

import com.ecoskiller.mcp.ips.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import java.time.Instant;
import java.util.*;

/**
 * IntelligenceProfileTools
 *
 * Handles the 5 core profile-read tools:
 *   - get_intelligence_profile
 *   - get_skill_vectors
 *   - search_candidates
 *   - get_peer_benchmarks
 *   - get_risk_assessment
 *
 * In a production deployment these tools call the actual PostgreSQL (via Prisma/JDBC),
 * Redis cache, and Qdrant vector DB. This implementation provides the full
 * MCP interface with realistic mock data shaped exactly after the IPS data model.
 */
public class IntelligenceProfileTools {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator security;

    public IntelligenceProfileTools(SecurityValidator security) {
        this.security = security;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  1. get_intelligence_profile
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode getIntelligenceProfile(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId   = requireString(args, "userId");
        String tenantId = requireString(args, "tenantId");
        String include  = args.path("include").asText("all");

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.put("version",  23);
        resp.put("lastUpdatedAt", Instant.now().toString());

        // 8-type Intelligence DNA vector (normalized [0,1])
        ObjectNode iv = resp.putObject("intelligenceVectors");
        iv.put("cognitive",       0.92);  // problem-solving, coding, logic
        iv.put("behavioral",      0.78);  // collaboration, communication, leadership
        iv.put("domainExpertise", 0.85);  // tech, management, design depth
        iv.put("learningAgility", 0.71);  // acquiring new skills
        iv.put("personality",     0.68);  // Big Five traits
        iv.put("trajectory",      0.81);  // career progression
        iv.put("risk",            0.12);  // churn probability, red flags
        iv.put("uniqueness",      0.73);  // differentiation from peer group

        if (include.contains("skills") || include.equals("all")) {
            ArrayNode skills = resp.putArray("skills");
            addSkill(skills, "Python",     5, 0.92, 87, 8);
            addSkill(skills, "Kubernetes", 4, 0.78, 72, 3);
            addSkill(skills, "AWS",        4, 0.81, 68, 5);
            addSkill(skills, "Docker",     4, 0.76, 65, 4);
        }

        if (include.contains("peer-benchmarks") || include.equals("all")) {
            ObjectNode pb = resp.putObject("peerBenchmarks");
            ObjectNode overall = pb.putObject("overall");
            addPercentiles(overall, 0.45, 0.55, 0.65, 0.75, 0.88);
            ObjectNode python = pb.putObject("python");
            addPercentiles(python, 0.62, 0.71, 0.79, 0.85, 0.94);
        }

        if (include.contains("risk-assessment") || include.equals("all")) {
            ObjectNode ri = resp.putObject("riskIndicators");
            ri.put("churnProbability",      0.12);
            ri.put("offerAcceptanceProbability", 0.78);
            ri.put("retention12MonthProbability", 0.89);
            ri.put("trainingDurationEstimate", "6 weeks");
            ri.put("anomalyScore",          0.05);
            ri.putArray("fairnessFlags");  // empty = no flags
        }

        resp.put("cacheSource", "redis");
        resp.put("retrievalTimeMs", 47);

        return successResponse("get_intelligence_profile", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  2. get_skill_vectors
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode getSkillVectors(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId     = requireString(args, "userId");
        String tenantId   = requireString(args, "tenantId");
        String skillFilter = args.path("skillFilter").asText(null);

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        List<String[]> allSkills = Arrays.asList(
            new String[]{"Python",     "5", "0.92", "87"},
            new String[]{"Kubernetes", "4", "0.78", "72"},
            new String[]{"AWS",        "4", "0.81", "68"},
            new String[]{"Docker",     "4", "0.76", "65"},
            new String[]{"Java",       "3", "0.61", "54"},
            new String[]{"Go",         "2", "0.45", "38"}
        );

        ArrayNode skillsArray = mapper.createArrayNode();
        for (String[] s : allSkills) {
            if (skillFilter != null && !s[0].equalsIgnoreCase(skillFilter)) continue;
            ObjectNode sv = mapper.createObjectNode();
            sv.put("skillName",        s[0]);
            sv.put("proficiencyLevel", Integer.parseInt(s[1]));
            sv.put("confidenceScore",  Double.parseDouble(s[2]));
            sv.put("peerPercentile",   Integer.parseInt(s[3]));
            sv.put("taxonomyPath",     "Technical > Programming Languages > " + s[0]);
            // Sentence-BERT 384-dim embedding placeholder (first 5 dims shown)
            ArrayNode emb = sv.putArray("embeddingPreview");
            emb.add(0.231); emb.add(-0.154); emb.add(0.489); emb.add(0.102); emb.add(-0.322);
            sv.put("embeddingDimensions", 384);
            sv.put("assessmentCount",  8);
            sv.put("lastAssessedAt",   "2025-03-05T10:30:00Z");
            skillsArray.add(sv);
        }

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);
        resp.set("skills", skillsArray);
        resp.put("totalSkills", skillsArray.size());

        return successResponse("get_skill_vectors", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  3. search_candidates  (Qdrant HNSW similarity search)
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode searchCandidates(JsonNode args, SecurityValidator.JwtClaims claims) {
        String tenantId       = requireString(args, "tenantId");
        JsonNode skillsNode   = args.path("skills");
        double minSimilarity  = args.path("skillMinSimilarity").asDouble(0.75);
        double cognitiveMin   = args.path("cognitiveMin").asDouble(0.0);
        double behavioralMin  = args.path("behavioralMin").asDouble(0.0);
        int limit             = Math.min(args.path("limit").asInt(50), 200);

        validateUUID(tenantId, "tenantId");
        if (!skillsNode.isArray() || skillsNode.size() == 0) {
            throw new IllegalArgumentException("skills array is required and must not be empty");
        }

        // Collect requested skills
        List<String> requestedSkills = new ArrayList<>();
        skillsNode.forEach(s -> requestedSkills.add(SecurityValidator.sanitize(s.asText())));

        // Simulated Qdrant HNSW results (production: call Qdrant REST API)
        ArrayNode results = mapper.createArrayNode();
        String[] sampleNames = {"Alice Chen", "Bob Kumar", "Sarah Okoye", "Dev Patel", "Maria Santos"};
        double[] scores = {0.94, 0.91, 0.88, 0.85, 0.82};

        for (int i = 0; i < Math.min(sampleNames.length, limit); i++) {
            double matchScore = scores[i];
            if (matchScore < minSimilarity) break;

            ObjectNode candidate = mapper.createObjectNode();
            candidate.put("userId",    UUID.randomUUID().toString());
            candidate.put("name",      sampleNames[i]);
            candidate.put("matchScore", matchScore);
            candidate.put("tenantId",  tenantId);

            ObjectNode vectors = candidate.putObject("intelligenceVectors");
            vectors.put("cognitive",  0.90 - i * 0.02);
            vectors.put("behavioral", 0.80 - i * 0.01);

            ArrayNode skillMatches = candidate.putArray("skillMatches");
            for (String skill : requestedSkills) {
                ObjectNode sm = mapper.createObjectNode();
                sm.put("skill",       skill);
                sm.put("similarity",  0.98 - i * 0.03);
                sm.put("proficiency", 5 - i);
                skillMatches.add(sm);
            }
            results.add(candidate);
        }

        ObjectNode resp = mapper.createObjectNode();
        resp.set("results", results);
        resp.put("totalMatches",     147);
        resp.put("returnedCount",    results.size());
        resp.put("executionTimeMs",  87);
        resp.put("searchAlgorithm",  "HNSW (Qdrant)");
        resp.put("tenantId",         tenantId);

        return successResponse("search_candidates", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  4. get_peer_benchmarks
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode getPeerBenchmarks(JsonNode args, SecurityValidator.JwtClaims claims) {
        String tenantId  = requireString(args, "tenantId");
        String skillName = args.path("skillName").asText(null);

        validateUUID(tenantId, "tenantId");

        ObjectNode resp = mapper.createObjectNode();
        resp.put("tenantId",   tenantId);
        resp.put("peerGroupSize", 2847);

        if (skillName != null) {
            String safe = SecurityValidator.sanitize(skillName);
            ObjectNode skillBench = resp.putObject("skillBenchmark");
            skillBench.put("skillName", safe);
            addPercentiles(skillBench, 0.38, 0.52, 0.67, 0.81, 0.94);
            skillBench.put("assessmentCount", 1243);
        } else {
            ObjectNode overall = resp.putObject("overallProfile");
            addPercentiles(overall, 0.40, 0.55, 0.66, 0.78, 0.91);

            ArrayNode topSkills = resp.putArray("topSkillBenchmarks");
            for (String sk : new String[]{"Python", "Java", "AWS", "Docker", "React"}) {
                ObjectNode b = mapper.createObjectNode();
                b.put("skill", sk);
                addPercentiles(b, 0.35 + Math.random() * 0.1, 0.50 + Math.random() * 0.1,
                    0.65 + Math.random() * 0.1, 0.78 + Math.random() * 0.1, 0.90 + Math.random() * 0.05);
                topSkills.add(b);
            }
        }

        resp.put("computedAt", Instant.now().toString());
        return successResponse("get_peer_benchmarks", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  5. get_risk_assessment  (Recruiter/Admin only — enforced by RBAC)
    // ─────────────────────────────────────────────────────────────────────

    public JsonNode getRiskAssessment(JsonNode args, SecurityValidator.JwtClaims claims) {
        String userId   = requireString(args, "userId");
        String tenantId = requireString(args, "tenantId");

        validateUUID(userId, "userId");
        validateUUID(tenantId, "tenantId");

        ObjectNode resp = mapper.createObjectNode();
        resp.put("userId",   userId);
        resp.put("tenantId", tenantId);

        ObjectNode churn = resp.putObject("churnRisk");
        churn.put("probability",   0.12);
        churn.put("riskLevel",     "LOW");
        churn.put("drivingFactors", "High learning-agility, strong domain growth trajectory");

        ObjectNode retention = resp.putObject("retentionPrediction");
        retention.put("probability12Month",  0.89);
        retention.put("probability24Month",  0.74);
        retention.put("confidenceInterval",  "±0.08");

        ObjectNode offer = resp.putObject("offerAcceptance");
        offer.put("probability",    0.78);
        offer.put("salaryBandFit",  "HIGH");
        offer.put("cultureAlignScore", 0.82);

        ObjectNode training = resp.putObject("trainingRequirements");
        training.put("estimatedRampUpWeeks", 6);
        training.put("skillGaps", "Docker advanced networking, Go concurrency");

        ObjectNode fairness = resp.putObject("fairnessIndicators");
        fairness.put("disparateImpactRatio",      1.02);  // >0.8 is passing
        fairness.put("equalOpportunityDifference", 0.01);
        fairness.put("status",  "PASS");
        fairness.putArray("flags");  // empty = no violations

        ObjectNode anomaly = resp.putObject("anomalyIndicators");
        anomaly.put("anomalyScore",   0.05);
        anomaly.put("status",         "CLEAN");
        anomaly.putArray("flaggedFields");

        resp.put("reportGeneratedAt", Instant.now().toString());
        resp.put("modelVersion",      "lightgbm-v3.2.1");

        return successResponse("get_risk_assessment", resp);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────────

    private void addSkill(ArrayNode arr, String name, int prof, double conf, int pct, int count) {
        ObjectNode sk = mapper.createObjectNode();
        sk.put("skillName",        name);
        sk.put("proficiencyLevel", prof);
        sk.put("confidenceScore",  conf);
        sk.put("peerPercentile",   pct);
        sk.put("assessmentCount",  count);
        sk.put("lastAssessedAt",   "2025-03-05T10:30:00Z");
        arr.add(sk);
    }

    private void addPercentiles(ObjectNode n, double p10, double p25, double p50, double p75, double p90) {
        n.put("percentile_10", round2(p10));
        n.put("percentile_25", round2(p25));
        n.put("percentile_50", round2(p50));
        n.put("percentile_75", round2(p75));
        n.put("percentile_90", round2(p90));
    }

    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    private String requireString(JsonNode args, String field) {
        String v = args.path(field).asText(null);
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return SecurityValidator.sanitize(v);
    }

    private void validateUUID(String val, String field) {
        if (!SecurityValidator.isValidUUID(val))
            throw new IllegalArgumentException("Field '" + field + "' must be a valid UUID, got: " + val);
    }

    private ObjectNode successResponse(String tool, ObjectNode data) {
        ObjectNode r = mapper.createObjectNode();
        r.put("tool",      tool);
        r.put("status",    "SUCCESS");
        r.put("timestamp", Instant.now().toString());
        r.set("data",      data);
        return r;
    }
}
