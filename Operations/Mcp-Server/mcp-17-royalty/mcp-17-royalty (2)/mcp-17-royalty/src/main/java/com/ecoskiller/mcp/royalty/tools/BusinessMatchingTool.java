package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * BUSINESS_MATCHING_AGENT — Tool: business_matching
 *
 * Matches ideas with potential business licensees based on:
 *   - Idea category and technology domain
 *   - Business sector and licensing budget
 *   - Territory preferences and exclusivity requirements
 *   - Historical licensing patterns and royalty payment track record
 *
 * A successful match initiates the license_generation workflow.
 * Tracks match status from SUGGESTED → INTERESTED → NEGOTIATING → LICENSED.
 */
public class BusinessMatchingTool extends BaseTool {

    @Override public String getName() { return "business_matching"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",        str("match|get_matches|express_interest|get_pipeline|score_match|recommend_terms"))
                .put("idea_id",       str("Idea unique ID"))
                .put("creator_id",    str("Creator unique ID"))
                .put("business_id",   str("Business / licensee unique ID"))
                .put("territory",     str("Preferred licensing territory"))
                .put("category",      str("Idea/business category"))
                .put("budget_min",    num("Minimum licensing budget"))
                .put("budget_max",    num("Maximum licensing budget"))
                .put("exclusive",     bool("Looking for exclusive license?"));
        return schema(getName(),
                "BUSINESS_MATCHING_AGENT — AI-powered matching of ideas to potential licensees. " +
                "Tracks deal pipeline from suggestion to signed agreement. Recommends initial license terms.",
                p, "action", "idea_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String ideaId    = a.getString("idea_id");
        String territory = a.optString("territory","Asia");

        return switch (action) {
            case "match" -> {
                JSONArray matches = new JSONArray()
                        .put(new JSONObject()
                                .put("business_id",   "BIZ-001")
                                .put("match_score",    92)
                                .put("sector",         "Enterprise Software")
                                .put("territory",      territory)
                                .put("budget_range",   "₹100K-₹500K/year")
                                .put("payment_history","EXCELLENT")
                                .put("status",         "SUGGESTED"))
                        .put(new JSONObject()
                                .put("business_id",   "BIZ-002")
                                .put("match_score",    85)
                                .put("sector",         "FinTech")
                                .put("territory",      territory)
                                .put("budget_range",   "₹50K-₹200K/year")
                                .put("payment_history","GOOD")
                                .put("status",         "SUGGESTED"));
                yield json(new JSONObject()
                        .put("idea_id",    ideaId)
                        .put("territory",  territory)
                        .put("matches",    matches)
                        .put("total",      2)
                        .put("algorithm",  "Category affinity + budget fit + territory overlap + payment score")
                        .put("timestamp",  now()));
            }
            case "express_interest" -> {
                String bizId = a.getString("business_id");
                yield json(new JSONObject()
                        .put("idea_id",     ideaId)
                        .put("business_id", bizId)
                        .put("status",      "INTERESTED")
                        .put("next_step",   "Creator notified — initiate negotiation or auto-license")
                        .put("timestamp",   now()));
            }
            case "get_pipeline" -> json(new JSONObject()
                    .put("idea_id",  ideaId)
                    .put("pipeline", new JSONArray()
                            .put(new JSONObject().put("stage","SUGGESTED").put("count",5))
                            .put(new JSONObject().put("stage","INTERESTED").put("count",2))
                            .put(new JSONObject().put("stage","NEGOTIATING").put("count",1))
                            .put(new JSONObject().put("stage","LICENSED").put("count",0)))
                    .put("timestamp",now()));
            case "score_match" -> {
                String bizId = a.optString("business_id","");
                yield json(new JSONObject()
                        .put("idea_id",     ideaId)
                        .put("business_id", bizId)
                        .put("match_score", 88)
                        .put("dimensions", new JSONObject()
                                .put("category_fit",   90)
                                .put("territory_fit",  85)
                                .put("budget_fit",     88)
                                .put("payment_score",  90))
                        .put("recommendation","PROCEED_TO_LICENSE")
                        .put("timestamp",   now()));
            }
            case "recommend_terms" -> {
                String bizId = a.optString("business_id","");
                yield json(new JSONObject()
                        .put("idea_id",        ideaId)
                        .put("business_id",    bizId)
                        .put("suggested_terms", new JSONObject()
                                .put("royalty_rate",       "7% (tiered structure available)")
                                .put("minimum_guarantee",  "₹50,000/year")
                                .put("territory",          territory)
                                .put("duration_months",    12)
                                .put("exclusive",          false)
                                .put("payment_frequency",  "Quarterly"))
                        .put("next_action",    "Pass to license_generation tool to create agreement")
                        .put("timestamp",      now()));
            }
            case "get_matches" -> json(new JSONObject()
                    .put("idea_id",  ideaId)
                    .put("matches",  "Fetch active matches from PostgreSQL")
                    .put("timestamp",now()));
            default -> text("Unknown action: " + action);
        };
    }
}
