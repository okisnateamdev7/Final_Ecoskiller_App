package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * IDEA_EVALUATION_AGENT_SPEC
 * Scores idea submissions across novelty, feasibility, market potential, and IP clarity.
 */
public class IdeaEvaluationAgent extends ToolDefinition {

    @Override public String getName() { return "idea_evaluation"; }

    @Override public String getDescription() {
        return "IDEA_EVALUATION_AGENT_SPEC — Scores idea submissions across novelty, feasibility, " +
               "market potential, and IP clarity. Generates an evaluation report, " +
               "shortlist recommendation, and royalty eligibility flag.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "idea_id", "title", "description");
        addStringProp(s, "idea_id",     "Unique idea identifier");
        addStringProp(s, "title",       "Idea title");
        addStringProp(s, "description", "Full idea description");
        addStringProp(s, "category",    "Skill/domain category");
        addStringProp(s, "submitted_by","Submitter user ID");
        addBoolProp  (s, "fast_track",  "Fast-track evaluation (reduced criteria)");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String ideaId   = str(args, "idea_id",     "IDEA-UNKNOWN");
        String title    = str(args, "title",        "Untitled Idea");
        String category = str(args, "category",     "general");
        boolean fast    = bool(args, "fast_track",  false);

        // Deterministic demo scores
        int novelty     = 74;
        int feasibility = 68;
        int market      = 81;
        int ipClarity   = 72;
        double composite= (novelty + feasibility + market + ipClarity) / 4.0;

        ObjectNode scores = m.createObjectNode();
        scores.put("novelty",          novelty);
        scores.put("feasibility",      feasibility);
        scores.put("market_potential", market);
        scores.put("ip_clarity",       ipClarity);
        scores.put("composite",        composite);

        ObjectNode data = m.createObjectNode();
        data.put("idea_id",              ideaId);
        data.put("title",                title);
        data.put("category",             category);
        data.put("fast_track",           fast);
        data.set("scores",               scores);
        data.put("shortlist_recommended", composite >= 70.0);
        data.put("royalty_eligible",      composite >= 65.0);
        data.put("evaluation_ref",        "EVAL-" + ideaId + "-" + System.currentTimeMillis());

        return successWithData(m,
            "Idea '" + title + "' evaluated: composite score " + composite, data);
    }
}
