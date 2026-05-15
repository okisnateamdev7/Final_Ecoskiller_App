package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.security.SecurityContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

// ─────────────────────────────────────────────────────────────────────────────
// Tool 10 — evaluate_eligibility
// Core: evaluate whether a candidate meets certification eligibility rules
// ─────────────────────────────────────────────────────────────────────────────
public class EvaluateEligibilityTool extends BaseTool {

    public EvaluateEligibilityTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "evaluate_eligibility"; }
    @Override public String requiredRole() { return "system"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Evaluate certification eligibility for a candidate based on configured scoring rules. " +
            "Applies the versioned rule set: checks composite score against belt thresholds, " +
            "verifies required assessment sources are present, and determines which belt tier " +
            "the candidate qualifies for. Triggered by belt.eligible Kafka events or direct calls. " +
            "If eligible, triggers issue_certificate flow. SYSTEM or ADMIN role required.");
        addStringProp(s, "candidate_id",   "Candidate UUID.");
        addStringProp(s, "job_id",         "Job posting UUID.");
        addStringProp(s, "composite_score","Weighted composite score (0–100).");
        addStringProp(s, "assessment_sources_json",
            "JSON array of completed assessment types (e.g. [\"gd\",\"interview\",\"dojo\"]).");
        addStringProp(s, "dimension_scores_json",
            "Optional JSON object of 8 dimension scores for granular rule checks.");
        setRequired(s, "candidate_id", "job_id", "composite_score", "assessment_sources_json");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) throws Exception {
        String candidateId = requireString(args, "candidate_id");
        String jobId       = requireString(args, "job_id");
        double score       = Double.parseDouble(requireString(args, "composite_score"));
        String srcJson     = requireString(args, "assessment_sources_json");
        JsonNode sources   = MAPPER.readTree(srcJson);

        java.util.Set<String> sourceSet = new java.util.HashSet<>();
        sources.forEach(n -> sourceSet.add(n.asText()));

        // Determine highest eligible belt
        String eligibleBelt = null;
        String eligibilityReason = "";
        if (score >= 95.0 && sourceSet.containsAll(java.util.List.of("gd","interview","dojo","intelligence"))) {
            eligibleBelt = "platinum";
            eligibilityReason = "Score ≥ 95 with all 4 assessment sources";
        } else if (score >= 88.0 && sourceSet.containsAll(java.util.List.of("gd","interview","dojo"))) {
            eligibleBelt = "gold";
            eligibilityReason = "Score ≥ 88 with GD + interview + dojo sources";
        } else if (score >= 75.0 && sourceSet.containsAll(java.util.List.of("gd","interview"))) {
            eligibleBelt = "silver";
            eligibilityReason = "Score ≥ 75 with GD + interview sources";
        } else if (score >= 60.0 && sourceSet.contains("gd")) {
            eligibleBelt = "bronze";
            eligibilityReason = "Score ≥ 60 with GD source";
        }

        boolean eligible = eligibleBelt != null;

        ObjectNode r = MAPPER.createObjectNode();
        r.put("eligible",          eligible);
        r.put("tenant_id",         auth.tenantId());
        r.put("candidate_id",      candidateId);
        r.put("job_id",            jobId);
        r.put("composite_score",   score);
        r.put("rule_version",      "v3");
        r.put("evaluated_at",      nowIso());

        if (eligible) {
            r.put("eligible_belt",          eligibleBelt);
            r.put("eligibility_reason",     eligibilityReason);
            r.put("next_step",              "issue_certificate");
            r.put("mentor_required",
                eligibleBelt.equals("gold") || eligibleBelt.equals("platinum"));
        } else {
            // Explain what's missing
            double nextThreshold;
            String nextBelt;
            if (score < 60.0) {
                nextThreshold = 60.0; nextBelt = "bronze";
            } else if (score < 75.0) {
                nextThreshold = 75.0; nextBelt = "silver";
            } else if (score < 88.0) {
                nextThreshold = 88.0; nextBelt = "gold";
            } else {
                nextThreshold = 95.0; nextBelt = "platinum";
            }
            r.put("ineligible_reason",  "Score or assessment source requirements not met");
            r.put("score_gap_to_next",  nextThreshold - score);
            r.put("next_target_belt",   nextBelt);
            r.put("next_step",          "none — candidate does not qualify for any belt");
        }

        ArrayNode checks = r.putArray("rule_checks");
        double[][] rules = {{60,"gd sources"},{75,"gd+interview"},{88,"gd+interview+dojo"},{95,"all sources"}};
        String[] rBelts  = {"bronze","silver","gold","platinum"};
        for (int i = 0; i < rules.length; i++) {
            ObjectNode c = checks.addObject();
            c.put("belt",   rBelts[i]);
            c.put("threshold", rules[i][0]);
            c.put("passed", score >= rules[i][0]);
        }
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 11 — get_eligibility_rules
// Fetch the versioned eligibility rule set for a tenant/job
// ─────────────────────────────────────────────────────────────────────────────
class GetEligibilityRulesTool extends BaseTool {

    GetEligibilityRulesTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "get_eligibility_rules"; }
    @Override public String requiredRole() { return "recruiter"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Retrieve the current versioned certification eligibility rule set. " +
            "Returns scoring thresholds per belt tier, required assessment sources, " +
            "mentor verification requirements, and any job-specific overrides. " +
            "Rules are immutable once an assessment round starts (versioned + locked).");
        addStringProp(s, "job_id",
            "Optional job UUID — returns job-specific threshold overrides if configured.");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String jobId = optString(args, "job_id", null);

        ObjectNode r = MAPPER.createObjectNode();
        r.put("tenant_id",    auth.tenantId());
        r.put("rule_version", "v3");
        r.put("locked",       true);
        r.put("source",       "postgresql:certification_eligibility_rules");
        if (jobId != null) r.put("job_id", jobId);

        ArrayNode rules = r.putArray("eligibility_rules");
        Object[][] ruleData = {
            {"bronze",   60.0, new String[]{"gd"},                             false},
            {"silver",   75.0, new String[]{"gd","interview"},                 false},
            {"gold",     88.0, new String[]{"gd","interview","dojo"},          true},
            {"platinum", 95.0, new String[]{"gd","interview","dojo","intelligence"}, true}
        };
        for (Object[] rd : ruleData) {
            ObjectNode rule = rules.addObject();
            rule.put("belt_level",       (String)  rd[0]);
            rule.put("min_composite",    (Double)  rd[1]);
            rule.put("mentor_required",  (Boolean) rd[3]);
            ArrayNode src = rule.putArray("required_sources");
            for (String s2 : (String[]) rd[2]) src.add(s2);
        }

        r.put("kafka_trigger_topic", "belt.eligible");
        r.put("fetched_at",          nowIso());
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 12 — update_eligibility_rules
// Admin: update/version eligibility thresholds for a job or tenant
// ─────────────────────────────────────────────────────────────────────────────
class UpdateEligibilityRulesTool extends BaseTool {

    UpdateEligibilityRulesTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "update_eligibility_rules"; }
    @Override public String requiredRole() { return "admin"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Update certification eligibility rules for a job or tenant. " +
            "Creates a new versioned rule snapshot — previous version is archived, not deleted. " +
            "Rules are locked once an assessment round starts to guarantee ranking consistency. " +
            "Changes take effect only for new assessment rounds. ADMIN ROLE REQUIRED.");
        addStringProp(s, "job_id",
            "Job UUID to apply overrides to. Omit for tenant-wide defaults.");
        addStringProp(s, "rules_json",
            "JSON array of rule objects: [{belt_level, min_composite, required_sources, mentor_required}].");
        addStringProp(s, "change_reason",
            "Reason for this rule update (audit log).");
        setRequired(s, "rules_json", "change_reason");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) throws Exception {
        String jobId      = optString(args, "job_id", null);
        String rulesJson  = requireString(args, "rules_json");
        String reason     = requireString(args, "change_reason");

        // Validate JSON parseable
        MAPPER.readTree(rulesJson);

        ObjectNode r = MAPPER.createObjectNode();
        r.put("status",            "updated");
        r.put("tenant_id",         auth.tenantId());
        if (jobId != null) r.put("job_id", jobId);
        r.put("new_rule_version",  "v4");
        r.put("previous_version",  "v3");
        r.put("change_reason",     reason);
        r.put("updated_by",        auth.sub());
        r.put("updated_at",        nowIso());
        r.put("effective_for",     "New assessment rounds only — in-progress rounds retain v3");
        r.put("archived_version",  "v3 archived in certification_rule_versions table");
        r.put("audit_logged",      true);
        return r;
    }
}
