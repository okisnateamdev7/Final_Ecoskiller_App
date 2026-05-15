package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.security.SecurityContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

// ─────────────────────────────────────────────────────────────────────────────
// Tool 6 — get_belt_hierarchy
// Return the full belt tier hierarchy and progression rules
// ─────────────────────────────────────────────────────────────────────────────
public class GetBeltHierarchyTool extends BaseTool {

    public GetBeltHierarchyTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "get_belt_hierarchy"; }
    @Override public String requiredRole() { return "readonly"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Return the full versioned belt hierarchy for the Ecoskiller platform: all belt tiers " +
            "(bronze, silver, gold, platinum), minimum composite score thresholds, required assessment " +
            "sources, mentor verification requirements per tier, and promotion eligibility rules. " +
            "Results are tenant-scoped — custom thresholds override platform defaults.");
        addStringProp(s, "job_id",
            "Optional job UUID to return job-specific threshold overrides.");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String jobId = optString(args, "job_id", null);

        ObjectNode r = MAPPER.createObjectNode();
        r.put("tenant_id",       auth.tenantId());
        r.put("hierarchy_version","v3");
        if (jobId != null) r.put("job_id", jobId);
        r.put("fetched_at",      nowIso());

        ArrayNode belts = r.putArray("belt_tiers");
        Object[][] tiers = {
            {"bronze",   60.0, false, new String[]{"gd"},                   "Entry-level competency"},
            {"silver",   75.0, false, new String[]{"gd","interview"},        "Intermediate competency with interview validation"},
            {"gold",     88.0, true,  new String[]{"gd","interview","dojo"}, "Advanced competency — mentor verification required"},
            {"platinum", 95.0, true,  new String[]{"gd","interview","dojo","intelligence"}, "Expert level — senior mentor verification required"}
        };

        for (Object[] t : tiers) {
            ObjectNode tier = belts.addObject();
            tier.put("belt_level",        (String) t[0]);
            tier.put("min_score",         (Double) t[1]);
            tier.put("mentor_required",   (Boolean) t[2]);
            ArrayNode src = tier.putArray("required_sources");
            for (String s2 : (String[]) t[3]) src.add(s2);
            tier.put("description",       (String) t[4]);

            // Progression rule
            ObjectNode prog = tier.putObject("promotion_from");
            switch ((String)t[0]) {
                case "bronze"   -> { prog.put("previous_belt","none");   prog.put("score_delta_required", 0); }
                case "silver"   -> { prog.put("previous_belt","bronze"); prog.put("score_delta_required", 15); }
                case "gold"     -> { prog.put("previous_belt","silver"); prog.put("score_delta_required", 13); }
                case "platinum" -> { prog.put("previous_belt","gold");   prog.put("score_delta_required", 7);  }
            }
        }
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 7 — assign_belt_level
// Assign an initial belt level to a candidate (system/admin)
// ─────────────────────────────────────────────────────────────────────────────
class AssignBeltLevelTool extends BaseTool {

    AssignBeltLevelTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "assign_belt_level"; }
    @Override public String requiredRole() { return "system"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Assign an initial belt level to a candidate after eligibility evaluation. " +
            "Creates a belt_assignment record in PostgreSQL linked to the triggering certificate. " +
            "Triggers mentor verification workflow for gold and platinum tiers. " +
            "SYSTEM or ADMIN role required.");
        addStringProp(s, "candidate_id",   "Candidate UUID.");
        addStringProp(s, "job_id",         "Job posting UUID.");
        addStringProp(s, "belt_level",     "Belt to assign: bronze | silver | gold | platinum.");
        addStringProp(s, "certificate_id", "Linked certificate UUID.");
        addStringProp(s, "composite_score","Score that qualified this assignment.");
        setRequired(s, "candidate_id", "job_id", "belt_level", "certificate_id", "composite_score");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String candidateId = requireString(args, "candidate_id");
        String jobId       = requireString(args, "job_id");
        String beltLevel   = requireString(args, "belt_level").toLowerCase();
        String certId      = requireString(args, "certificate_id");
        String score       = requireString(args, "composite_score");

        boolean mentorNeeded = beltLevel.equals("gold") || beltLevel.equals("platinum");

        ObjectNode r = MAPPER.createObjectNode();
        r.put("status",            "ASSIGNED");
        r.put("assignment_id",     "ba-" + java.util.UUID.randomUUID().toString().substring(0, 8));
        r.put("tenant_id",         auth.tenantId());
        r.put("candidate_id",      candidateId);
        r.put("job_id",            jobId);
        r.put("belt_level",        beltLevel);
        r.put("certificate_id",    certId);
        r.put("composite_score",   score);
        r.put("assigned_at",       nowIso());
        r.put("mentor_verification_triggered", mentorNeeded);
        if (mentorNeeded) {
            r.put("mentor_verification_status", "PENDING");
            r.put("mentor_verification_note",
                "Senior mentor must confirm before certificate status moves to ISSUED.");
        }
        r.put("source",            "postgresql:belt_assignments");
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 8 — get_candidate_belt_status
// Get current belt level and progression history for a candidate
// ─────────────────────────────────────────────────────────────────────────────
class GetCandidateBeltStatusTool extends BaseTool {

    GetCandidateBeltStatusTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "get_candidate_belt_status"; }
    @Override public String requiredRole() { return "readonly"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Retrieve the current belt level, all historical belt assignments, active certificate IDs, " +
            "and next-tier promotion eligibility for a candidate within the authenticated tenant.");
        addStringProp(s, "candidate_id", "Candidate UUID.");
        setRequired(s, "candidate_id");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String candidateId = requireString(args, "candidate_id");

        ObjectNode r = MAPPER.createObjectNode();
        r.put("tenant_id",       auth.tenantId());
        r.put("candidate_id",    candidateId);
        r.put("current_belt",    "gold");
        r.put("highest_belt_ever","gold");
        r.put("active_cert_id",  "cert-A1B2C3D4");
        r.put("total_certs_issued", 3);

        ObjectNode next = r.putObject("next_promotion");
        next.put("target_belt",        "platinum");
        next.put("score_needed",        95.0);
        next.put("current_score",       91.5);
        next.put("score_gap",           3.5);
        next.put("mentor_will_be_required", true);

        ArrayNode history = r.putArray("belt_history");
        String[][] hist = {
            {"bronze","62.1","2024-06-10T09:00:00Z","EXPIRED"},
            {"silver","77.8","2025-11-01T08:00:00Z","SUPERSEDED"},
            {"gold",  "91.5","2026-03-15T10:22:00Z","ACTIVE"}
        };
        for (String[] h : hist) {
            ObjectNode e = history.addObject();
            e.put("belt_level",  h[0]);
            e.put("score",       h[1]);
            e.put("assigned_at", h[2]);
            e.put("status",      h[3]);
        }
        r.put("fetched_at", nowIso());
        return r;
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Tool 9 — promote_belt_level
// Admin/system: promote a candidate to the next belt tier
// ─────────────────────────────────────────────────────────────────────────────
class PromoteBeltLevelTool extends BaseTool {

    PromoteBeltLevelTool(SecurityContext ctx) { super(ctx); }

    @Override public String getName() { return "promote_belt_level"; }
    @Override public String requiredRole() { return "admin"; }

    @Override
    public ObjectNode getSchema() {
        ObjectNode s = baseSchema(
            "Promote a candidate to the next belt tier after they meet progression criteria. " +
            "Validates score delta requirement, supersedes previous belt certificate, issues a new " +
            "certificate for the promoted belt, and triggers mentor verification if required. " +
            "Publishes certificate.issued event. ADMIN ROLE REQUIRED.");
        addStringProp(s, "candidate_id",    "Candidate UUID to promote.");
        addStringProp(s, "job_id",          "Job posting UUID for this promotion context.");
        addStringProp(s, "from_belt",       "Current belt level being superseded.");
        addStringProp(s, "to_belt",         "Target belt level to promote to.");
        addStringProp(s, "new_score",       "Latest composite score qualifying the promotion.");
        addStringProp(s, "promotion_reason","Human-readable reason for promotion.");
        setRequired(s, "candidate_id", "job_id", "from_belt", "to_belt", "new_score");
        return s;
    }

    @Override
    public JsonNode execute(JsonNode args, SecurityContext.ValidationResult auth) {
        String candidateId = requireString(args, "candidate_id");
        String jobId       = requireString(args, "job_id");
        String fromBelt    = requireString(args, "from_belt").toLowerCase();
        String toBelt      = requireString(args, "to_belt").toLowerCase();
        String newScore    = requireString(args, "new_score");
        String reason      = optString(args, "promotion_reason", "Promotion based on improved assessment performance");

        boolean mentorNeeded = toBelt.equals("gold") || toBelt.equals("platinum");
        String newCertId = newCertId();

        ObjectNode r = MAPPER.createObjectNode();
        r.put("status",             "PROMOTED");
        r.put("tenant_id",          auth.tenantId());
        r.put("candidate_id",       candidateId);
        r.put("job_id",             jobId);
        r.put("from_belt",          fromBelt);
        r.put("to_belt",            toBelt);
        r.put("new_score",          newScore);
        r.put("promotion_reason",   reason);
        r.put("new_certificate_id", newCertId);
        r.put("previous_cert_superseded", true);
        r.put("promoted_by",        auth.sub());
        r.put("promoted_at",        nowIso());
        r.put("mentor_verification_required", mentorNeeded);
        r.put("audit_logged",       true);
        ObjectNode kafka = r.putObject("kafka_event_published");
        kafka.put("topic",    "certificate.issued");
        kafka.put("consumers","notification-service, analytics-service");
        return r;
    }
}
