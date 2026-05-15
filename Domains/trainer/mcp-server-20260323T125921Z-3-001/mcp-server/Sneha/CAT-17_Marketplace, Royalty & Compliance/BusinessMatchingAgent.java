package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * BUSINESS_MATCHING_AGENT_SEALED
 * Matches idea creators and skilled talent with investors, corporates, and hiring partners.
 */
public class BusinessMatchingAgent extends ToolDefinition {

    @Override public String getName() { return "business_matching"; }

    @Override public String getDescription() {
        return "BUSINESS_MATCHING_AGENT_SEALED — Matches idea creators and skilled talent with " +
               "investors, corporates, and hiring partners based on skill vectors, " +
               "idea categories, geography, and budget fit.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "entity_id", "match_type");
        addStringProp(s, "entity_id",    "Creator/candidate entity ID");
        addStringProp(s, "match_type",   "Match type: investor | corporate_hire | license_buyer | mentor");
        addStringProp(s, "skill_tags",   "Comma-separated skill tags");
        addStringProp(s, "region",       "Preferred region for match");
        addNumberProp(s, "min_budget_inr","Minimum budget / CTC for the opportunity");
        addNumberProp(s, "max_results",  "Max matches to return (default 5)");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String entityId  = str(args, "entity_id",  "ENTITY-UNKNOWN");
        String matchType = str(args, "match_type", "investor");
        int    maxRes    = (int) num(args, "max_results", 5.0);

        ArrayNode matches = m.createArrayNode();
        String[][] sampleMatches = {
            {"BIZ-001", "TechVentures India",    "95.2"},
            {"BIZ-002", "SkillBridge Corp",      "88.7"},
            {"BIZ-003", "FutureHire Solutions",  "83.1"},
        };
        int count = Math.min(maxRes, sampleMatches.length);
        for (int i = 0; i < count; i++) {
            ObjectNode match = matches.addObject();
            match.put("match_id",       sampleMatches[i][0]);
            match.put("name",           sampleMatches[i][1]);
            match.put("match_score",    Double.parseDouble(sampleMatches[i][2]));
            match.put("match_type",     matchType);
            match.put("contact_ref",    "CONTACT-" + sampleMatches[i][0]);
        }

        ObjectNode data = m.createObjectNode();
        data.put("entity_id",   entityId);
        data.put("match_type",  matchType);
        data.put("total_found", count);
        data.set("matches",     matches);

        return successWithData(m,
            count + " " + matchType + " matches found for entity " + entityId, data);
    }
}
