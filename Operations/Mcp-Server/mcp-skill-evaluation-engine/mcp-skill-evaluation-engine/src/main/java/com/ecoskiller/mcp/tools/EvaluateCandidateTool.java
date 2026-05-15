package com.ecoskiller.mcp.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Tool: evaluate_candidate
 * Runs full multi-modal competency evaluation for a candidate,
 * aggregating signals from GD (35%), Interview (40%), Coding (20%), Test (5%).
 */
public class EvaluateCandidateTool extends BaseTool {

    @Override
    public JsonNode getSchema() {
        ObjectNode inputSchema = mapper.createObjectNode();
        inputSchema.put("type", "object");
        ObjectNode props = inputSchema.putObject("properties");

        addProp(props, "participant_id",    "string",  "Candidate UUID");
        addProp(props, "tenant_id",         "string",  "Tenant ID for RLS");
        addProp(props, "gd_score",          "number",  "Group discussion raw score (0-100). Weight: 35%");
        addProp(props, "interview_score",   "number",  "Interview raw score (0-100). Weight: 40%");
        addProp(props, "coding_score",      "number",  "Coding challenge raw score (0-100). Weight: 20%");
        addProp(props, "test_score",        "number",  "Competency test raw score (0-100). Weight: 5%");
        addProp(props, "include_breakdown", "boolean", "Include detailed competency dimension breakdown");

        inputSchema.putArray("required").add("participant_id").add("tenant_id");

        return buildSchema("evaluate_candidate",
                "Compute aggregated competency assessment for a candidate by combining scores " +
                "from all assessment types using configurable weights: GD 35%, Interview 40%, " +
                "Coding 20%, Test 5%. Returns normalized overall score (0-100), competency breakdown, " +
                "confidence level, and trend indicator. Feeds into belt-eligibility-service.",
                inputSchema);
    }

    @Override
    public JsonNode execute(JsonNode args) {
        String participantId   = requireString(args, "participant_id");
        String tenantId        = requireString(args, "tenant_id");
        double gdScore         = optDouble(args, "gd_score",        -1);
        double interviewScore  = optDouble(args, "interview_score",  -1);
        double codingScore     = optDouble(args, "coding_score",     -1);
        double testScore       = optDouble(args, "test_score",       -1);
        boolean includeBreakdown = args.path("include_breakdown").asBoolean(true);

        // Validate provided scores
        if (gdScore >= 0)       validateScore(gdScore,       "gd_score");
        if (interviewScore >= 0) validateScore(interviewScore, "interview_score");
        if (codingScore >= 0)   validateScore(codingScore,   "coding_score");
        if (testScore >= 0)     validateScore(testScore,     "test_score");

        // Weighted aggregation with only available scores
        double totalWeight = 0.0;
        double weightedSum = 0.0;
        if (gdScore >= 0)       { weightedSum += gdScore * 0.35;       totalWeight += 0.35; }
        if (interviewScore >= 0){ weightedSum += interviewScore * 0.40; totalWeight += 0.40; }
        if (codingScore >= 0)   { weightedSum += codingScore * 0.20;   totalWeight += 0.20; }
        if (testScore >= 0)     { weightedSum += testScore * 0.05;     totalWeight += 0.05; }

        double overallScore = totalWeight > 0 ? Math.round((weightedSum / totalWeight) * 100.0) / 100.0 : 0.0;
        double confidence   = Math.min(0.95, 0.5 + (totalWeight * 0.45));

        ObjectNode result = successResponse("Candidate evaluation completed");
        result.put("participant_id",   participantId);
        result.put("tenant_id",        tenantId);
        result.put("overall_score",    overallScore);
        result.put("confidence",       Math.round(confidence * 100.0) / 100.0);
        result.put("trend",            overallScore >= 70 ? "STRONG" : overallScore >= 50 ? "DEVELOPING" : "NEEDS_IMPROVEMENT");
        result.put("belt_eligible",    overallScore >= 75);
        result.put("assessment_coverage_pct", Math.round(totalWeight * 100));

        if (includeBreakdown) {
            ObjectNode breakdown = result.putObject("score_breakdown");
            if (gdScore >= 0)        breakdown.put("gd_weighted",        Math.round(gdScore * 0.35 * 100.0) / 100.0);
            if (interviewScore >= 0) breakdown.put("interview_weighted",  Math.round(interviewScore * 0.40 * 100.0) / 100.0);
            if (codingScore >= 0)    breakdown.put("coding_weighted",     Math.round(codingScore * 0.20 * 100.0) / 100.0);
            if (testScore >= 0)      breakdown.put("test_weighted",       Math.round(testScore * 0.05 * 100.0) / 100.0);
        }

        // Competency dimension estimates based on available scores
        ObjectNode competencies = result.putObject("competency_estimates");
        computeCompetencyEstimates(competencies, gdScore, interviewScore, codingScore, testScore);

        return result;
    }

    private void computeCompetencyEstimates(ObjectNode comp,
            double gd, double interview, double coding, double test) {
        // Technical Depth: 40% coding + 35% problem-solving (coding) + 25% interview
        double tech = blended(new double[]{coding, coding, interview},
                              new double[]{0.40,  0.35,  0.25});
        // Communication: 50% GD + 30% interview + 20% interview articulation
        double comm = blended(new double[]{gd, interview, interview},
                              new double[]{0.50, 0.30, 0.20});
        // Problem Solving: 50% coding + 30% coding creativity + 20% interview
        double prob = blended(new double[]{coding, coding, interview},
                              new double[]{0.50, 0.30, 0.20});
        // Collaboration: 50% GD speaking equity + 30% GD + 20% GD
        double collab = blended(new double[]{gd, gd, gd},
                                new double[]{0.50, 0.30, 0.20});
        // Leadership: 50% GD proposal rate + 30% GD influence + 20% interview
        double lead = blended(new double[]{gd, gd, interview},
                              new double[]{0.50, 0.30, 0.20});
        // Adaptability: 50% error recovery (coding) + 30% flexibility + 20% stress
        double adapt = blended(new double[]{coding, interview, interview},
                               new double[]{0.50, 0.30, 0.20});
        // Domain Knowledge: 60% terminology (interview) + 40% depth (test)
        double domain = blended(new double[]{interview, test},
                                new double[]{0.60, 0.40});
        // Innovation: 50% solution novelty (coding) + 50% improvement (interview)
        double innov = blended(new double[]{coding, interview},
                               new double[]{0.50, 0.50});

        comp.put("technical_depth",  round2(tech));
        comp.put("communication",    round2(comm));
        comp.put("problem_solving",  round2(prob));
        comp.put("collaboration",    round2(collab));
        comp.put("leadership",       round2(lead));
        comp.put("adaptability",     round2(adapt));
        comp.put("domain_knowledge", round2(domain));
        comp.put("innovation",       round2(innov));
    }

    private double blended(double[] scores, double[] weights) {
        double sum = 0, w = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] >= 0) { sum += scores[i] * weights[i]; w += weights[i]; }
        }
        return w > 0 ? sum / w : 0;
    }

    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }

    private void addProp(ObjectNode props, String name, String type, String description) {
        ObjectNode p = props.putObject(name);
        p.put("type", type);
        p.put("description", description);
    }
}
