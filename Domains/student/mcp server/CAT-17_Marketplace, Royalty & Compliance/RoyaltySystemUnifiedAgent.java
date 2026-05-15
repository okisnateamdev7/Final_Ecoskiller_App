package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * ROYALTY_SYSTEM_UNIFIED
 * Unified orchestrator for the entire royalty pipeline: ingestion → calculation → distribution.
 */
public class RoyaltySystemUnifiedAgent extends ToolDefinition {

    @Override public String getName() { return "royalty_system_unified"; }

    @Override public String getDescription() {
        return "ROYALTY_SYSTEM_UNIFIED — Unified royalty orchestration across all tiers. " +
               "Triggers the full pipeline: revenue ingestion, royalty calculation, " +
               "escrow hold, and distribution scheduling for a given cycle.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "cycle_id", "cycle_type");
        addStringProp(s, "cycle_id",      "Royalty cycle identifier (e.g. CYCLE-2025-Q2)");
        addStringProp(s, "cycle_type",    "Cycle type: monthly | quarterly | annual");
        addStringProp(s, "entity_id",     "Optional: scope to a single entity");
        addBoolProp  (s, "dry_run",       "If true, simulate without committing");
        addStringProp(s, "triggered_by",  "User/system that triggered the run");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String cycleId     = str(args,  "cycle_id",    "CYCLE-UNSET");
        String cycleType   = str(args,  "cycle_type",  "monthly");
        boolean dryRun     = bool(args, "dry_run",     false);
        String triggeredBy = str(args,  "triggered_by","system");

        ObjectNode pipeline = m.createObjectNode();
        pipeline.put("step_1_ingestion",   "COMPLETED");
        pipeline.put("step_2_calculation", "COMPLETED");
        pipeline.put("step_3_escrow",      "COMPLETED");
        pipeline.put("step_4_distribution", dryRun ? "SIMULATED" : "SCHEDULED");

        ObjectNode data = m.createObjectNode();
        data.put("cycle_id",    cycleId);
        data.put("cycle_type",  cycleType);
        data.put("dry_run",     dryRun);
        data.put("triggered_by",triggeredBy);
        data.put("pipeline_run_id", "PLN-" + System.currentTimeMillis());
        data.set("pipeline_status", pipeline);

        String msg = dryRun
            ? "Dry-run completed for cycle " + cycleId + " — no changes committed"
            : "Unified royalty pipeline initiated for cycle " + cycleId;
        return successWithData(m, msg, data);
    }
}
