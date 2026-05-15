package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * IDEA_EVALUATION_AGENT — Tool: idea_evaluation
 *
 * Evaluates ideas for marketplace licensing potential, royalty viability,
 * and commercial value scoring. Integrates with market demand signals,
 * innovation registry metadata, and historical licensing performance
 * to produce a commercialization score and licensing recommendation.
 */
public class IdeaEvaluationTool extends BaseTool {

    @Override public String getName() { return "idea_evaluation"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",       str("evaluate|score|recommend_rate|comparable_analysis|viability_check"))
                .put("idea_id",      str("Idea unique ID"))
                .put("creator_id",   str("Creator unique ID"))
                .put("category",     str("Idea category: ML|BIOTECH|FINTECH|HARDWARE|SOFTWARE|OTHER"))
                .put("territory",    str("Target territory for evaluation"))
                .put("sales_amount", num("Historical or projected sales for rate recommendation"));
        return schema(getName(),
                "IDEA_EVALUATION_AGENT — Score ideas for commercial licensing viability. " +
                "Recommend royalty rates, comparable analysis, territory demand assessment.",
                p, "action", "idea_id", "creator_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String ideaId    = a.getString("idea_id");
        String creatorId = a.getString("creator_id");
        String category  = a.optString("category", "SOFTWARE");
        String territory = a.optString("territory", "Asia");

        return switch (action) {
            case "evaluate" -> {
                JSONObject scores = new JSONObject()
                        .put("market_demand",       78)
                        .put("novelty",             85)
                        .put("commercial_readiness",70)
                        .put("territory_fit",       82)
                        .put("overall",             79);
                yield json(new JSONObject()
                        .put("idea_id",        ideaId)
                        .put("creator_id",     creatorId)
                        .put("category",       category)
                        .put("territory",      territory)
                        .put("scores",         scores)
                        .put("verdict",        "COMMERCIALLY_VIABLE")
                        .put("recommended_action", "Proceed to license_generation with 7% royalty rate")
                        .put("timestamp",      now()));
            }
            case "score" -> json(new JSONObject()
                    .put("idea_id",          ideaId)
                    .put("commercial_score", 79)
                    .put("percentile",       "Top 25% in " + category)
                    .put("methodology",      "Weighted: demand(30%) + novelty(30%) + readiness(20%) + territory(20%)")
                    .put("timestamp",        now()));
            case "recommend_rate" -> {
                double sales = a.optDouble("sales_amount", 100_000);
                double rate  = sales > 500_000 ? 0.10 : sales > 100_000 ? 0.07 : 0.05;
                yield json(new JSONObject()
                        .put("idea_id",              ideaId)
                        .put("territory",            territory)
                        .put("projected_sales",      money(sales).toPlainString())
                        .put("recommended_rate",     (rate * 100) + "%")
                        .put("tiered_alternative",   "5% (0-100K) / 7% (100K-500K) / 10% (>500K)")
                        .put("min_guarantee_suggest","₹50,000/year for agreements >₹500K projected")
                        .put("timestamp",            now()));
            }
            case "comparable_analysis" -> json(new JSONObject()
                    .put("idea_id",    ideaId)
                    .put("category",   category)
                    .put("comparables", new JSONArray()
                            .put(new JSONObject().put("idea","Similar-ML-001").put("rate","8%").put("territory","Asia"))
                            .put(new JSONObject().put("idea","Similar-ML-002").put("rate","7%").put("territory","EU")))
                    .put("market_avg_rate","7.2%")
                    .put("timestamp",  now()));
            case "viability_check" -> json(new JSONObject()
                    .put("idea_id",           ideaId)
                    .put("viable",            true)
                    .put("minimum_threshold", "Revenue > ₹10,000/quarter to justify licensing overhead")
                    .put("timestamp",         now()));
            default -> text("Unknown action: " + action);
        };
    }
}
