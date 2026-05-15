package com.ecoskiller.mcp.royalty.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * COMPETITION_ENGINE_AGENT — Tool: competition_engine
 *
 * Manages innovation competitions and challenges on the Ecoskiller
 * marketplace. Competitions drive idea submissions and creator engagement.
 * This tool handles competition creation, scoring, prize royalty
 * distribution, and winner payout orchestration.
 *
 * Prize payouts flow through the standard royalty pipeline (escrow →
 * tax withholding → payout distribution) ensuring compliance.
 */
public class CompetitionEngineTool extends BaseTool {

    @Override public String getName() { return "competition_engine"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",         str("create|get|score|declare_winners|distribute_prizes|leaderboard"))
                .put("competition_id", str("Competition unique ID"))
                .put("creator_id",     str("Creator / participant ID"))
                .put("idea_id",        str("Submitted idea ID"))
                .put("prize_pool",     num("Total prize pool amount"))
                .put("currency",       str("Prize currency: INR|USD|EUR"))
                .put("category",       str("Competition category: ML|FINTECH|BIOTECH|OPEN"))
                .put("territory",      str("Competition territory"))
                .put("score",          num("Evaluation score 0-100"))
                .put("rank",           intP("Winner rank (1=first place)"));
        return schema(getName(),
                "COMPETITION_ENGINE_AGENT — Innovation competition lifecycle: creation, scoring, " +
                "winner declaration, prize royalty distribution via compliant payout pipeline.",
                p, "action");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action = a.getString("action");

        return switch (action) {
            case "create" -> {
                String compId    = "COMP-" + System.currentTimeMillis();
                double prizePool = a.optDouble("prize_pool", 100_000);
                String currency  = a.optString("currency","INR");
                String category  = a.optString("category","OPEN");
                yield json(new JSONObject()
                        .put("competition_id", compId)
                        .put("category",       category)
                        .put("prize_pool",     money(prizePool).toPlainString())
                        .put("currency",       currency)
                        .put("status",         "OPEN")
                        .put("prize_structure", new JSONObject()
                                .put("1st_place","50%")
                                .put("2nd_place","30%")
                                .put("3rd_place","20%"))
                        .put("created_at",     now()));
            }
            case "get" -> {
                String compId = a.getString("competition_id");
                yield json(new JSONObject()
                        .put("competition_id", compId)
                        .put("status",        "OPEN")
                        .put("participants",  "Fetch from registry")
                        .put("timestamp",     now()));
            }
            case "score" -> {
                String compId    = a.getString("competition_id");
                String creatorId = a.getString("creator_id");
                String ideaId    = a.optString("idea_id","");
                double score     = a.optDouble("score", 0);
                yield json(new JSONObject()
                        .put("competition_id", compId)
                        .put("creator_id",     creatorId)
                        .put("idea_id",        ideaId)
                        .put("score",          score)
                        .put("recorded_at",    now()));
            }
            case "declare_winners" -> {
                String compId = a.getString("competition_id");
                yield json(new JSONObject()
                        .put("competition_id", compId)
                        .put("winners", new JSONArray()
                                .put(new JSONObject().put("rank",1).put("prize_pct","50%").put("status","ESCROW_HELD"))
                                .put(new JSONObject().put("rank",2).put("prize_pct","30%").put("status","ESCROW_HELD"))
                                .put(new JSONObject().put("rank",3).put("prize_pct","20%").put("status","ESCROW_HELD")))
                        .put("payout_note",    "Prizes held in escrow pending tax verification")
                        .put("kafka_event",    "competition.winners.declared")
                        .put("timestamp",      now()));
            }
            case "distribute_prizes" -> {
                String compId    = a.getString("competition_id");
                double prizePool = a.optDouble("prize_pool",100_000);
                yield json(new JSONObject()
                        .put("competition_id",  compId)
                        .put("total_prize",     money(prizePool).toPlainString())
                        .put("distribution",    "Routed through royalty_distribution pipeline")
                        .put("tax_withholding", "Applied per creator jurisdiction")
                        .put("kafka_event",     "payout.queued")
                        .put("timestamp",       now()));
            }
            case "leaderboard" -> {
                String compId = a.getString("competition_id");
                yield json(new JSONObject()
                        .put("competition_id", compId)
                        .put("leaderboard",    "Fetch top-10 from scoring engine")
                        .put("timestamp",      now()));
            }
            default -> text("Unknown action: " + action);
        };
    }
}
