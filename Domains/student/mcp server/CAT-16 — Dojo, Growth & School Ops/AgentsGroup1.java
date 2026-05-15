package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.models.AgentResponse;
import com.ecoskiller.dojo.models.McpTool;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════
// Agent 1 — XP_ENGINE_AGENT
// Manages XP award, decay, multipliers, and balance per student/teacher.
// ═══════════════════════════════════════════════════════════════════════════
class XpEngineAgent extends BaseAgent {
    @Override public String getName() { return "XP_ENGINE_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_award_xp",
                "Award XP points to a student or teacher for a completed activity.",
                schema("user_id", "role", "activity_type", "activity_id", "tenant_id")),
            new McpTool("dojo_get_xp_balance",
                "Get the current XP balance, level, and breakdown for a user.",
                schema("user_id", "tenant_id")),
            new McpTool("dojo_apply_xp_multiplier",
                "Apply a time-limited XP multiplier for a user or school.",
                schema("target_id", "target_type", "multiplier", "duration_hours", "reason")),
            new McpTool("dojo_decay_xp",
                "Run the scheduled XP decay pass for inactive users.",
                schema("tenant_id", "decay_rate_pct", "inactivity_days"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_award_xp" -> {
                String userId   = args.path("user_id").asText();
                String activity = args.path("activity_type").asText("task_complete");
                int    base     = activityXp(activity);
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",        userId);
                d.put("activity_type",  activity);
                d.put("xp_awarded",     base);
                d.put("multiplier",     1.0);
                d.put("total_awarded",  base);
                d.put("new_balance",    4820 + base);
                d.put("transaction_id", "xp-" + System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Awarded " + base + " XP to user=" + userId);
            }
            case "dojo_get_xp_balance" -> {
                String userId = args.path("user_id").asText();
                ObjectNode d  = mapper.createObjectNode();
                d.put("user_id",       userId);
                d.put("total_xp",      4820);
                d.put("level",         12);
                d.put("xp_to_next",    680);
                d.put("rank_title",    "Skill Scout");
                d.put("streak_bonus",  150);
                d.put("last_earned",   "2026-03-04T14:30:00Z");
                yield AgentResponse.success(getName(), d, "XP balance for user=" + userId);
            }
            case "dojo_apply_xp_multiplier" -> {
                double mult = args.path("multiplier").asDouble(2.0);
                ObjectNode d = mapper.createObjectNode();
                d.put("target_id",      args.path("target_id").asText());
                d.put("multiplier",     mult);
                d.put("duration_hours", args.path("duration_hours").asInt(24));
                d.put("active",         true);
                d.put("expires_at",     "2026-03-06T12:00:00Z");
                yield AgentResponse.success(getName(), d, mult + "x XP multiplier applied.");
            }
            case "dojo_decay_xp" -> {
                double rate = args.path("decay_rate_pct").asDouble(5.0);
                ObjectNode d = mapper.createObjectNode();
                d.put("tenant_id",      args.path("tenant_id").asText("default"));
                d.put("users_affected", 47);
                d.put("decay_rate_pct", rate);
                d.put("total_xp_decayed", 2340);
                d.put("run_at",         System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "XP decay pass complete. " + 47 + " users affected.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }

    private int activityXp(String type) {
        return switch (type) {
            case "lesson_complete"  -> 50;
            case "quiz_pass"        -> 30;
            case "streak_milestone" -> 100;
            case "gd_session"       -> 75;
            case "task_complete"    -> 20;
            default                 -> 10;
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 2 — TEACHER_INCENTIVE_AGENT
// Tracks teacher performance metrics and computes incentive payouts.
// ═══════════════════════════════════════════════════════════════════════════
class TeacherIncentiveAgent extends BaseAgent {
    @Override public String getName() { return "TEACHER_INCENTIVE_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_calculate_teacher_incentive",
                "Calculate incentive payout for a teacher based on performance KPIs.",
                schema("teacher_id", "period", "tenant_id")),
            new McpTool("dojo_get_teacher_kpi_dashboard",
                "Get a full KPI dashboard for a teacher: attendance, ratings, student outcomes.",
                schema("teacher_id", "tenant_id")),
            new McpTool("dojo_set_incentive_rules",
                "Configure the incentive rule set for a school or tenant.",
                schema("tenant_id", "school_id", "rules_json"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_calculate_teacher_incentive" -> {
                String tid    = args.path("teacher_id").asText();
                String period = args.path("period").asText("2026-02");
                ObjectNode d  = mapper.createObjectNode();
                d.put("teacher_id",          tid);
                d.put("period",              period);
                d.put("base_incentive",      5000);
                d.put("attendance_bonus",    800);
                d.put("student_outcome_bonus", 1200);
                d.put("rating_bonus",        500);
                d.put("total_incentive",     7500);
                d.put("currency",            "INR");
                d.put("status",              "PENDING_APPROVAL");
                yield AgentResponse.success(getName(), d, "Incentive calculated for teacher=" + tid + ", period=" + period);
            }
            case "dojo_get_teacher_kpi_dashboard" -> {
                String tid = args.path("teacher_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("teacher_id",         tid);
                d.put("classes_taken",       42);
                d.put("avg_student_rating",  4.7);
                d.put("attendance_rate_pct", 96.2);
                d.put("students_improved",   28);
                d.put("students_total",      35);
                d.put("improvement_pct",     80.0);
                d.put("gd_sessions_led",     8);
                d.put("rank_in_school",      2);
                yield AgentResponse.success(getName(), d, "KPI dashboard for teacher=" + tid);
            }
            case "dojo_set_incentive_rules" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("tenant_id",   args.path("tenant_id").asText());
                d.put("school_id",   args.path("school_id").asText());
                d.put("rules_saved", true);
                d.put("version",     "v3");
                d.put("effective",   "2026-04-01");
                yield AgentResponse.success(getName(), d, "Incentive rules saved.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 3 — STREAK_ENGINE_AGENT
// Tracks daily/weekly streaks, milestone rewards, and streak protection.
// ═══════════════════════════════════════════════════════════════════════════
class StreakEngineAgent extends BaseAgent {
    @Override public String getName() { return "STREAK_ENGINE_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_record_streak_activity",
                "Record a user's daily activity to maintain or extend their streak.",
                schema("user_id", "activity_date", "tenant_id")),
            new McpTool("dojo_get_streak_status",
                "Get current streak length, longest streak, and freeze status for a user.",
                schema("user_id", "tenant_id")),
            new McpTool("dojo_apply_streak_freeze",
                "Apply a streak freeze shield to protect a user's streak for N days.",
                schema("user_id", "freeze_days", "reason", "tenant_id")),
            new McpTool("dojo_get_streak_leaderboard",
                "Get the top streak holders for a school or tenant.",
                schema("scope", "scope_id", "top_n"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_record_streak_activity" -> {
                String uid = args.path("user_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",         uid);
                d.put("date",            args.path("activity_date").asText("2026-03-05"));
                d.put("streak_extended", true);
                d.put("current_streak",  14);
                d.put("milestone_hit",   false);
                d.put("xp_streak_bonus", 30);
                yield AgentResponse.success(getName(), d, "Streak extended to 14 days for user=" + uid);
            }
            case "dojo_get_streak_status" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",         args.path("user_id").asText());
                d.put("current_streak",  14);
                d.put("longest_streak",  42);
                d.put("frozen",          false);
                d.put("freeze_shields",  1);
                d.put("next_milestone",  30);
                d.put("at_risk",         false);
                yield AgentResponse.success(getName(), d, "Streak status retrieved.");
            }
            case "dojo_apply_streak_freeze" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",     args.path("user_id").asText());
                d.put("freeze_days", args.path("freeze_days").asInt(1));
                d.put("applied",     true);
                d.put("protected_until", "2026-03-06");
                yield AgentResponse.success(getName(), d, "Streak freeze applied.");
            }
            case "dojo_get_streak_leaderboard" -> {
                ArrayNode  board = mapper.createArrayNode();
                String[] names   = {"Arjun S.", "Priya K.", "Ravi M.", "Sneha T.", "Dev P."};
                int[]    streaks = {92, 87, 71, 65, 58};
                for (int i = 0; i < names.length; i++) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("rank",   i + 1);
                    row.put("name",   names[i]);
                    row.put("streak", streaks[i]);
                    board.add(row);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("leaderboard", board);
                d.put("scope", args.path("scope").asText("school"));
                yield AgentResponse.success(getName(), d, "Streak leaderboard retrieved.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 4 — SHARE_CARD_GENERATOR_AGENT
// Generates shareable achievement cards for social/WhatsApp sharing.
// ═══════════════════════════════════════════════════════════════════════════
class ShareCardGeneratorAgent extends BaseAgent {
    @Override public String getName() { return "SHARE_CARD_GENERATOR_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_generate_share_card",
                "Generate a shareable achievement card image URL for a user milestone.",
                schema("user_id", "card_type", "achievement_id", "tenant_id")),
            new McpTool("dojo_get_share_card_templates",
                "List all available share card templates for a tenant.",
                schema("tenant_id")),
            new McpTool("dojo_track_share_event",
                "Record when and where a user shared their achievement card.",
                schema("user_id", "card_id", "platform", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_generate_share_card" -> {
                String uid      = args.path("user_id").asText();
                String cardType = args.path("card_type").asText("level_up");
                String cardId   = "card-" + System.currentTimeMillis();
                ObjectNode d    = mapper.createObjectNode();
                d.put("user_id",   uid);
                d.put("card_id",   cardId);
                d.put("card_type", cardType);
                d.put("image_url", "https://cdn.ecoskiller.com/share-cards/" + cardId + ".png");
                d.put("og_url",    "https://ecoskiller.com/achievements/" + uid + "/" + cardId);
                d.put("whatsapp_text", "I just levelled up on Ecoskiller! 🎉");
                d.put("expires_at",    "2026-06-05");
                yield AgentResponse.success(getName(), d, "Share card generated: " + cardType);
            }
            case "dojo_get_share_card_templates" -> {
                ArrayNode tmpl = mapper.createArrayNode();
                for (String t : new String[]{"level_up","streak_milestone","rank_achieved","gd_winner","school_topper"})
                    tmpl.add(t);
                ObjectNode d = mapper.createObjectNode();
                d.set("templates", tmpl);
                d.put("count", 5);
                yield AgentResponse.success(getName(), d, "Templates listed.");
            }
            case "dojo_track_share_event" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",    args.path("user_id").asText());
                d.put("card_id",    args.path("card_id").asText());
                d.put("platform",   args.path("platform").asText("whatsapp"));
                d.put("tracked",    true);
                d.put("share_count_today", 3);
                yield AgentResponse.success(getName(), d, "Share event tracked.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 5 — SCHOOL_PERFORMANCE_ANALYTICS_AGENT
// School-level analytics: pass rates, XP totals, subject gaps.
// ═══════════════════════════════════════════════════════════════════════════
class SchoolPerformanceAnalyticsAgent extends BaseAgent {
    @Override public String getName() { return "SCHOOL_PERFORMANCE_ANALYTICS_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_get_school_performance_report",
                "Generate a comprehensive performance report for a school.",
                schema("school_id", "period", "tenant_id")),
            new McpTool("dojo_compare_schools",
                "Compare KPIs between two or more schools.",
                schema("school_ids_csv", "metric", "tenant_id")),
            new McpTool("dojo_get_subject_gap_analysis",
                "Identify subjects with the largest skill gaps for a school.",
                schema("school_id", "grade", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_get_school_performance_report" -> {
                String sid = args.path("school_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("school_id",            sid);
                d.put("period",               args.path("period").asText("2026-Q1"));
                d.put("total_students",       320);
                d.put("active_students",      298);
                d.put("avg_xp_per_student",   3420);
                d.put("avg_streak",           9.4);
                d.put("pass_rate_pct",        87.5);
                d.put("gd_sessions_total",    44);
                d.put("top_subject",          "Mathematics");
                d.put("weakest_subject",      "English Grammar");
                d.put("school_rank_district", 3);
                yield AgentResponse.success(getName(), d, "Performance report for school=" + sid);
            }
            case "dojo_compare_schools" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("metric",   args.path("metric").asText("avg_xp"));
                d.put("schools",  args.path("school_ids_csv").asText());
                ArrayNode results = mapper.createArrayNode();
                String[] sids  = {"SCH-001","SCH-002","SCH-003"};
                int[] xpAvgs   = {3420, 3180, 2950};
                for (int i = 0; i < sids.length; i++) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("school_id", sids[i]);
                    row.put("value",     xpAvgs[i]);
                    row.put("rank",      i + 1);
                    results.add(row);
                }
                d.set("comparison", results);
                yield AgentResponse.success(getName(), d, "School comparison complete.");
            }
            case "dojo_get_subject_gap_analysis" -> {
                ObjectNode d    = mapper.createObjectNode();
                ArrayNode  gaps = mapper.createArrayNode();
                String[] subs = {"English Grammar","Science","Social Studies"};
                double[] gaps_ = {18.3, 12.7, 9.1};
                for (int i = 0; i < subs.length; i++) {
                    ObjectNode g = mapper.createObjectNode();
                    g.put("subject",  subs[i]);
                    g.put("gap_pct",  gaps_[i]);
                    g.put("priority", i == 0 ? "HIGH" : "MEDIUM");
                    gaps.add(g);
                }
                d.set("gaps",      gaps);
                d.put("school_id", args.path("school_id").asText());
                yield AgentResponse.success(getName(), d, "Subject gap analysis complete.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 6 — REPUTATION_SCORE_AGENT
// Computes multi-factor reputation scores for students, teachers, schools.
// ═══════════════════════════════════════════════════════════════════════════
class ReputationScoreAgent extends BaseAgent {
    @Override public String getName() { return "REPUTATION_SCORE_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_compute_reputation_score",
                "Compute the multi-factor reputation score for a user or institution.",
                schema("entity_id", "entity_type", "tenant_id")),
            new McpTool("dojo_get_reputation_history",
                "Get the reputation score history with change events.",
                schema("entity_id", "entity_type", "days")),
            new McpTool("dojo_flag_reputation_event",
                "Flag a positive or negative reputation event for review.",
                schema("entity_id", "event_type", "impact", "evidence_url"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_compute_reputation_score" -> {
                String eid  = args.path("entity_id").asText();
                String type = args.path("entity_type").asText("student");
                ObjectNode d = mapper.createObjectNode();
                d.put("entity_id",        eid);
                d.put("entity_type",      type);
                d.put("reputation_score", 847);
                d.put("grade",            "A");
                d.put("consistency",      92.4);
                d.put("peer_rating",      4.6);
                d.put("integrity_flag",   false);
                d.put("percentile",       89);
                d.put("computed_at",      System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Reputation score: 847 (Grade A) for " + type + "=" + eid);
            }
            case "dojo_get_reputation_history" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("entity_id",  args.path("entity_id").asText());
                d.put("days",       args.path("days").asInt(30));
                d.put("start_score", 791);
                d.put("end_score",   847);
                d.put("delta",       +56);
                d.put("events",      12);
                yield AgentResponse.success(getName(), d, "Reputation history retrieved.");
            }
            case "dojo_flag_reputation_event" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("entity_id",  args.path("entity_id").asText());
                d.put("event_type", args.path("event_type").asText());
                d.put("impact",     args.path("impact").asText("POSITIVE"));
                d.put("flagged",    true);
                d.put("review_id",  "rev-" + System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Reputation event flagged for review.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}
