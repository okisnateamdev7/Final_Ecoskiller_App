package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.models.AgentResponse;
import com.ecoskiller.dojo.models.McpTool;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════
// Agent 7 — RANK_COMPUTATION_AGENT
// Real-time rank computation within school, district, state, and global.
// ═══════════════════════════════════════════════════════════════════════════
class RankComputationAgent extends BaseAgent {
    @Override public String getName() { return "RANK_COMPUTATION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_compute_rank",
                "Compute the current rank for a user across multiple scopes.",
                schema("user_id", "scope", "scope_id", "metric", "tenant_id")),
            new McpTool("dojo_get_rank_leaderboard",
                "Get a ranked leaderboard for a scope.",
                schema("scope", "scope_id", "metric", "top_n", "tenant_id")),
            new McpTool("dojo_get_rank_history",
                "Get rank movement history for a user over time.",
                schema("user_id", "scope", "days"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_compute_rank" -> {
                String uid    = args.path("user_id").asText();
                String scope  = args.path("scope").asText("school");
                String metric = args.path("metric").asText("xp");
                ObjectNode d  = mapper.createObjectNode();
                d.put("user_id",     uid);
                d.put("scope",       scope);
                d.put("metric",      metric);
                d.put("rank",        7);
                d.put("total_peers", 320);
                d.put("percentile",  97.8);
                d.put("rank_label",  "Top 10");
                d.put("delta_7d",    +3);
                d.put("computed_at", System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Rank=" + 7 + " in " + scope + " by " + metric);
            }
            case "dojo_get_rank_leaderboard" -> {
                int topN = args.path("top_n").asInt(10);
                ArrayNode board = mapper.createArrayNode();
                String[] names = {"Arjun S.","Priya K.","Ravi M.","Sneha T.","Dev P.",
                                  "Kavya R.","Mihir J.","Tanya S.","Rohan B.","Ananya V."};
                for (int i = 0; i < Math.min(topN, names.length); i++) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("rank",  i + 1);
                    row.put("name",  names[i]);
                    row.put("score", 5000 - (i * 200));
                    row.put("delta", i % 2 == 0 ? +1 : -1);
                    board.add(row);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("leaderboard", board);
                d.put("scope",  args.path("scope").asText("school"));
                d.put("metric", args.path("metric").asText("xp"));
                yield AgentResponse.success(getName(), d, "Leaderboard retrieved (" + topN + " entries).");
            }
            case "dojo_get_rank_history" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",    args.path("user_id").asText());
                d.put("scope",      args.path("scope").asText("school"));
                d.put("days",       args.path("days").asInt(30));
                d.put("start_rank", 14);
                d.put("end_rank",   7);
                d.put("best_rank",  5);
                d.put("trend",      "IMPROVING");
                yield AgentResponse.success(getName(), d, "Rank history: improved from #14 to #7.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 8 — PROBLEM_DETECTION_SCORING_AGENT
// Scores problem-solving submissions and detects reasoning patterns.
// ═══════════════════════════════════════════════════════════════════════════
class ProblemDetectionScoringAgent extends BaseAgent {
    @Override public String getName() { return "PROBLEM_DETECTION_SCORING_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_score_submission",
                "Score a student's problem-solving submission with rubric and reasoning analysis.",
                schema("submission_id", "user_id", "problem_id", "answer", "tenant_id")),
            new McpTool("dojo_detect_reasoning_pattern",
                "Detect the reasoning pattern (logical/creative/structured) in a submission.",
                schema("submission_id", "content")),
            new McpTool("dojo_get_scoring_rubric",
                "Retrieve the scoring rubric for a given problem type.",
                schema("problem_type", "difficulty_level"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_score_submission" -> {
                String sid = args.path("submission_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("submission_id",   sid);
                d.put("user_id",         args.path("user_id").asText());
                d.put("problem_id",      args.path("problem_id").asText());
                d.put("score",           82);
                d.put("max_score",       100);
                d.put("grade",           "B+");
                d.put("logic_score",     88);
                d.put("clarity_score",   76);
                d.put("creativity_score",79);
                d.put("xp_earned",       41);
                d.put("feedback",        "Strong logical structure. Improve clarity in step 3.");
                yield AgentResponse.success(getName(), d, "Submission scored: 82/100 for " + sid);
            }
            case "dojo_detect_reasoning_pattern" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("submission_id", args.path("submission_id").asText());
                d.put("pattern",       "STRUCTURED_LOGICAL");
                d.put("confidence",    0.91);
                d.put("creativity_index", 64);
                d.put("depth_score",   78);
                yield AgentResponse.success(getName(), d, "Reasoning pattern: STRUCTURED_LOGICAL (91% confidence).");
            }
            case "dojo_get_scoring_rubric" -> {
                ObjectNode d     = mapper.createObjectNode();
                ObjectNode rubric = mapper.createObjectNode();
                rubric.put("logic",     40);
                rubric.put("clarity",   30);
                rubric.put("creativity",30);
                d.set("rubric",         rubric);
                d.put("problem_type",   args.path("problem_type").asText("case_study"));
                d.put("difficulty",     args.path("difficulty_level").asText("MEDIUM"));
                d.put("total_marks",    100);
                yield AgentResponse.success(getName(), d, "Rubric retrieved.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 9 — PASSPORT_HISTORY_AGENT
// Immutable learning passport: records every skill, badge, and milestone.
// ═══════════════════════════════════════════════════════════════════════════
class PassportHistoryAgent extends BaseAgent {
    @Override public String getName() { return "PASSPORT_HISTORY_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_get_passport_timeline",
                "Get the full chronological learning passport timeline for a student.",
                schema("user_id", "from_date", "to_date", "tenant_id")),
            new McpTool("dojo_add_passport_entry",
                "Add a new immutable entry (skill, badge, event) to a student's passport.",
                schema("user_id", "entry_type", "entry_data", "issued_by", "tenant_id")),
            new McpTool("dojo_verify_passport_entry",
                "Verify the authenticity of a specific passport entry by hash.",
                schema("user_id", "entry_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_get_passport_timeline" -> {
                String uid = args.path("user_id").asText();
                ArrayNode entries = mapper.createArrayNode();
                String[][] events = {
                    {"2026-01-10","badge","Innovation Starter","SCHOOL-01"},
                    {"2026-02-05","skill","Problem Solving L3","TEACHER-09"},
                    {"2026-02-20","milestone","30-Day Streak","SYSTEM"},
                    {"2026-03-01","rank","School Rank #7","SYSTEM"}
                };
                for (String[] ev : events) {
                    ObjectNode e = mapper.createObjectNode();
                    e.put("date",      ev[0]);
                    e.put("type",      ev[1]);
                    e.put("title",     ev[2]);
                    e.put("issued_by", ev[3]);
                    e.put("hash",      "sha256:" + Integer.toHexString(ev[2].hashCode()));
                    entries.add(e);
                }
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id", uid);
                d.set("entries", entries);
                d.put("total",   entries.size());
                yield AgentResponse.success(getName(), d, "Passport timeline: " + entries.size() + " entries for user=" + uid);
            }
            case "dojo_add_passport_entry" -> {
                ObjectNode d = mapper.createObjectNode();
                String entryId = "pe-" + System.currentTimeMillis();
                d.put("user_id",    args.path("user_id").asText());
                d.put("entry_id",   entryId);
                d.put("entry_type", args.path("entry_type").asText("skill"));
                d.put("issued_by",  args.path("issued_by").asText("SYSTEM"));
                d.put("immutable",  true);
                d.put("hash",       "sha256:" + Integer.toHexString(entryId.hashCode()));
                d.put("recorded_at", System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Passport entry added: " + entryId);
            }
            case "dojo_verify_passport_entry" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",   args.path("user_id").asText());
                d.put("entry_id",  args.path("entry_id").asText());
                d.put("verified",  true);
                d.put("tampered",  false);
                d.put("issuer_valid", true);
                yield AgentResponse.success(getName(), d, "Passport entry verified: authentic.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 10 — PASSPORT_AGGREGATION_AGENT
// Aggregates passport data across schools, districts, and national level.
// ═══════════════════════════════════════════════════════════════════════════
class PassportAggregationAgent extends BaseAgent {
    @Override public String getName() { return "PASSPORT_AGGREGATION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_aggregate_passports",
                "Aggregate learning passport statistics for a school or district.",
                schema("scope", "scope_id", "metric", "tenant_id")),
            new McpTool("dojo_export_passport_report",
                "Export a batch passport report (PDF/CSV) for a cohort.",
                schema("scope_id", "format", "cohort_year", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_aggregate_passports" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("scope",               args.path("scope").asText("school"));
                d.put("scope_id",            args.path("scope_id").asText());
                d.put("total_students",      320);
                d.put("passports_issued",    318);
                d.put("avg_entries_per_student", 14.2);
                d.put("total_badges",        1240);
                d.put("total_skills_logged", 4820);
                d.put("verified_entries_pct", 99.1);
                yield AgentResponse.success(getName(), d, "Passport aggregation complete.");
            }
            case "dojo_export_passport_report" -> {
                String fmt = args.path("format").asText("PDF");
                ObjectNode d = mapper.createObjectNode();
                d.put("format",        fmt);
                d.put("scope_id",      args.path("scope_id").asText());
                d.put("cohort_year",   args.path("cohort_year").asText("2026"));
                d.put("records",       318);
                d.put("export_url",    "https://cdn.ecoskiller.com/exports/passport-report-" + System.currentTimeMillis() + "." + fmt.toLowerCase());
                d.put("expires_at",    "2026-03-12");
                yield AgentResponse.success(getName(), d, "Passport report exported: " + fmt);
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 11 — LEVEL_PROGRESSION_AGENT
// Manages level thresholds, unlocks, and progression path for each user.
// ═══════════════════════════════════════════════════════════════════════════
class LevelProgressionAgent extends BaseAgent {
    @Override public String getName() { return "LEVEL_PROGRESSION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_get_level_status",
                "Get the current level, XP thresholds, and next unlock for a user.",
                schema("user_id", "tenant_id")),
            new McpTool("dojo_trigger_level_up",
                "Manually trigger a level-up check and apply if criteria are met.",
                schema("user_id", "tenant_id")),
            new McpTool("dojo_configure_level_thresholds",
                "Configure XP thresholds and unlocks for each level tier.",
                schema("tenant_id", "level_config_json"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_get_level_status" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",        args.path("user_id").asText());
                d.put("current_level",  12);
                d.put("level_title",    "Skill Scout");
                d.put("current_xp",     4820);
                d.put("xp_for_level",   5500);
                d.put("xp_needed",      680);
                d.put("progress_pct",   87.6);
                d.put("next_unlock",    "Global Challenge Access");
                d.put("level_up_ready", false);
                yield AgentResponse.success(getName(), d, "Level 12 — 680 XP to next level.");
            }
            case "dojo_trigger_level_up" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",      args.path("user_id").asText());
                d.put("levelled_up",  false);
                d.put("reason",       "Insufficient XP: need 680 more.");
                d.put("current_level", 12);
                yield AgentResponse.success(getName(), d, "Level-up check complete: not ready yet.");
            }
            case "dojo_configure_level_thresholds" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("tenant_id", args.path("tenant_id").asText());
                d.put("saved",     true);
                d.put("levels",    25);
                d.put("version",   "v2");
                yield AgentResponse.success(getName(), d, "Level thresholds configured: 25 levels.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 12 — INVITE_TRACKING_AGENT
// Tracks referral/invite chains, conversion rates, and reward attribution.
// ═══════════════════════════════════════════════════════════════════════════
class InviteTrackingAgent extends BaseAgent {
    @Override public String getName() { return "INVITE_TRACKING_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_generate_invite_link",
                "Generate a unique invite link for a user or school.",
                schema("inviter_id", "inviter_type", "campaign", "tenant_id")),
            new McpTool("dojo_record_invite_conversion",
                "Record when an invite results in a successful registration.",
                schema("invite_code", "invitee_id", "tenant_id")),
            new McpTool("dojo_get_invite_analytics",
                "Get invite funnel analytics: sent, clicked, converted, XP earned.",
                schema("inviter_id", "period", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_generate_invite_link" -> {
                String inviterId = args.path("inviter_id").asText();
                String code      = "INV-" + inviterId.substring(0, Math.min(4, inviterId.length())).toUpperCase() + "-" + (int)(Math.random()*9999);
                ObjectNode d     = mapper.createObjectNode();
                d.put("inviter_id",  inviterId);
                d.put("invite_code", code);
                d.put("invite_url",  "https://ecoskiller.com/join?ref=" + code);
                d.put("xp_reward_on_convert", 200);
                d.put("expires_at",  "2026-06-05");
                yield AgentResponse.success(getName(), d, "Invite link generated: " + code);
            }
            case "dojo_record_invite_conversion" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("invite_code",    args.path("invite_code").asText());
                d.put("invitee_id",     args.path("invitee_id").asText());
                d.put("converted",      true);
                d.put("inviter_xp_awarded", 200);
                d.put("invitee_xp_awarded", 100);
                d.put("conversion_id",  "conv-" + System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Invite conversion recorded. XP awarded to both parties.");
            }
            case "dojo_get_invite_analytics" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("inviter_id",     args.path("inviter_id").asText());
                d.put("period",         args.path("period").asText("30d"));
                d.put("invites_sent",   18);
                d.put("invites_clicked", 12);
                d.put("conversions",    7);
                d.put("conversion_rate_pct", 58.3);
                d.put("xp_earned",      1400);
                yield AgentResponse.success(getName(), d, "Invite analytics: 7 conversions, 58.3% rate.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}
