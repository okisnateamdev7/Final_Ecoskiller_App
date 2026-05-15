package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.models.AgentResponse;
import com.ecoskiller.dojo.models.McpTool;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════
// Agent 13 — INNOVATION_SKILL_SCORING_AGENT
// Evaluates innovation and entrepreneurial skill scores.
// ═══════════════════════════════════════════════════════════════════════════
class InnovationSkillScoringAgent extends BaseAgent {
    @Override public String getName() { return "INNOVATION_SKILL_SCORING_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_score_innovation_submission",
                "Score an innovation or entrepreneurial idea submission.",
                schema("submission_id", "user_id", "category", "content", "tenant_id")),
            new McpTool("dojo_get_innovation_skill_profile",
                "Get a detailed innovation skill profile for a student.",
                schema("user_id", "tenant_id")),
            new McpTool("dojo_rank_innovation_submissions",
                "Rank all innovation submissions for a school competition round.",
                schema("school_id", "round_id", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_score_innovation_submission" -> {
                String sid = args.path("submission_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("submission_id",      sid);
                d.put("user_id",            args.path("user_id").asText());
                d.put("category",           args.path("category").asText("sustainability"));
                d.put("originality_score",  84);
                d.put("feasibility_score",  71);
                d.put("impact_score",       90);
                d.put("presentation_score", 78);
                d.put("total_score",        81);
                d.put("grade",              "A-");
                d.put("xp_earned",          120);
                d.put("feedback",           "Excellent impact vision. Strengthen the feasibility plan.");
                yield AgentResponse.success(getName(), d, "Innovation submission scored: 81/100 for " + sid);
            }
            case "dojo_get_innovation_skill_profile" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",             args.path("user_id").asText());
                d.put("innovation_level",    "ADVANCED");
                d.put("originality_index",   82);
                d.put("risk_appetite",       74);
                d.put("problem_framing",     88);
                d.put("solution_generation", 79);
                d.put("pitching_score",      71);
                d.put("submissions_total",   6);
                d.put("top_category",        "sustainability");
                yield AgentResponse.success(getName(), d, "Innovation skill profile retrieved.");
            }
            case "dojo_rank_innovation_submissions" -> {
                ArrayNode ranked = mapper.createArrayNode();
                String[] users  = {"USR-001","USR-007","USR-014","USR-022","USR-031"};
                int[] scores    = {94, 88, 83, 79, 74};
                for (int i = 0; i < users.length; i++) {
                    ObjectNode row = mapper.createObjectNode();
                    row.put("rank",    i + 1);
                    row.put("user_id", users[i]);
                    row.put("score",   scores[i]);
                    row.put("grade",   i < 2 ? "A" : i < 4 ? "B+" : "B");
                    ranked.add(row);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("rankings",  ranked);
                d.put("school_id", args.path("school_id").asText());
                d.put("round_id",  args.path("round_id").asText());
                d.put("total",     5);
                yield AgentResponse.success(getName(), d, "Innovation submissions ranked.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 14 — GD_SESSION_SCHEDULER_AGENT
// Schedules Group Discussion (GD) sessions: slots, rooms, participants.
// ═══════════════════════════════════════════════════════════════════════════
class GdSessionSchedulerAgent extends BaseAgent {
    @Override public String getName() { return "GD_SESSION_SCHEDULER_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_schedule_gd_session",
                "Schedule a new Group Discussion session for a school.",
                schema("school_id", "topic", "date", "time", "duration_min", "max_participants", "tenant_id")),
            new McpTool("dojo_enroll_in_gd_session",
                "Enroll a student in an upcoming GD session.",
                schema("session_id", "user_id", "tenant_id")),
            new McpTool("dojo_get_upcoming_gd_sessions",
                "Get all upcoming GD sessions for a school or student.",
                schema("scope", "scope_id", "days_ahead", "tenant_id")),
            new McpTool("dojo_cancel_gd_session",
                "Cancel a GD session with notification to enrolled participants.",
                schema("session_id", "reason", "reschedule_date", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_schedule_gd_session" -> {
                String sessionId = "GD-" + System.currentTimeMillis();
                ObjectNode d = mapper.createObjectNode();
                d.put("session_id",      sessionId);
                d.put("school_id",       args.path("school_id").asText());
                d.put("topic",           args.path("topic").asText("Climate Change Solutions"));
                d.put("date",            args.path("date").asText("2026-03-10"));
                d.put("time",            args.path("time").asText("10:00"));
                d.put("duration_min",    args.path("duration_min").asInt(45));
                d.put("max_participants",args.path("max_participants").asInt(8));
                d.put("enrolled",        0);
                d.put("status",          "SCHEDULED");
                d.put("meet_link",       "https://meet.ecoskiller.com/gd/" + sessionId);
                yield AgentResponse.success(getName(), d, "GD session scheduled: " + sessionId);
            }
            case "dojo_enroll_in_gd_session" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("session_id",   args.path("session_id").asText());
                d.put("user_id",      args.path("user_id").asText());
                d.put("enrolled",     true);
                d.put("seat_number",  5);
                d.put("confirmation", "GD-CONF-" + System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Student enrolled in GD session.");
            }
            case "dojo_get_upcoming_gd_sessions" -> {
                ArrayNode sessions = mapper.createArrayNode();
                String[][] gds = {
                    {"GD-001","Climate Change Solutions","2026-03-10","10:00","6/8"},
                    {"GD-002","AI in Education","2026-03-12","14:00","3/8"},
                    {"GD-003","Rural Entrepreneurship","2026-03-15","11:00","8/8"}
                };
                for (String[] gd : gds) {
                    ObjectNode s = mapper.createObjectNode();
                    s.put("session_id",   gd[0]);
                    s.put("topic",        gd[1]);
                    s.put("date",         gd[2]);
                    s.put("time",         gd[3]);
                    s.put("enrollment",   gd[4]);
                    s.put("open",         !gd[4].startsWith("8"));
                    sessions.add(s);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("sessions", sessions);
                d.put("total",    sessions.size());
                yield AgentResponse.success(getName(), d, "Upcoming GD sessions: " + sessions.size());
            }
            case "dojo_cancel_gd_session" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("session_id",    args.path("session_id").asText());
                d.put("cancelled",     true);
                d.put("reason",        args.path("reason").asText("Unspecified"));
                d.put("notified",      true);
                d.put("reschedule_date", args.path("reschedule_date").asText("TBD"));
                yield AgentResponse.success(getName(), d, "GD session cancelled. Participants notified.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 15 — GD_POST_SESSION_ANALYTICS_AGENT
// Analyses GD session quality: participation balance, topic coverage, scores.
// ═══════════════════════════════════════════════════════════════════════════
class GdPostSessionAnalyticsAgent extends BaseAgent {
    @Override public String getName() { return "GD_POST_SESSION_ANALYTICS_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_analyze_gd_session",
                "Run post-session analytics for a completed GD session.",
                schema("session_id", "transcript_url", "tenant_id")),
            new McpTool("dojo_get_gd_participant_scores",
                "Get individual participant scores from a completed GD session.",
                schema("session_id", "tenant_id")),
            new McpTool("dojo_get_gd_session_summary",
                "Get a summary report of all GD sessions for a school in a period.",
                schema("school_id", "period", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_analyze_gd_session" -> {
                String sessionId = args.path("session_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("session_id",           sessionId);
                d.put("duration_actual_min",  47);
                d.put("participation_balance", 0.84);
                d.put("topic_coverage_pct",   91);
                d.put("avg_score",            76.4);
                d.put("best_performer",       "USR-007");
                d.put("avg_speaking_time_sec", 180);
                d.put("interruptions",        4);
                d.put("quality_grade",        "A-");
                yield AgentResponse.success(getName(), d, "Post-session analytics for " + sessionId);
            }
            case "dojo_get_gd_participant_scores" -> {
                ArrayNode scores = mapper.createArrayNode();
                String[] users  = {"USR-001","USR-007","USR-014","USR-022","USR-031","USR-044"};
                int[] pts       = {82, 91, 74, 79, 68, 72};
                for (int i = 0; i < users.length; i++) {
                    ObjectNode p = mapper.createObjectNode();
                    p.put("user_id",    users[i]);
                    p.put("score",      pts[i]);
                    p.put("xp_earned",  pts[i] / 2);
                    p.put("grade",      pts[i] >= 85 ? "A" : pts[i] >= 75 ? "B+" : "B");
                    scores.add(p);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("participants", scores);
                d.put("session_id",   args.path("session_id").asText());
                yield AgentResponse.success(getName(), d, "GD participant scores retrieved.");
            }
            case "dojo_get_gd_session_summary" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("school_id",          args.path("school_id").asText());
                d.put("period",             args.path("period").asText("2026-Q1"));
                d.put("sessions_total",     14);
                d.put("sessions_completed", 12);
                d.put("avg_attendance",     6.8);
                d.put("avg_quality_grade",  "B+");
                d.put("best_session_topic", "AI in Education");
                d.put("total_xp_distributed", 5040);
                yield AgentResponse.success(getName(), d, "GD session summary for school.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 16 — GD_ATTENDANCE_TRACKING_AGENT
// Tracks attendance for GD sessions with late/absent/present states.
// ═══════════════════════════════════════════════════════════════════════════
class GdAttendanceTrackingAgent extends BaseAgent {
    @Override public String getName() { return "GD_ATTENDANCE_TRACKING_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_mark_gd_attendance",
                "Mark a participant's attendance for a GD session.",
                schema("session_id", "user_id", "status", "join_time", "tenant_id")),
            new McpTool("dojo_get_gd_attendance_report",
                "Get the full attendance report for a GD session.",
                schema("session_id", "tenant_id")),
            new McpTool("dojo_get_user_gd_attendance_history",
                "Get the GD attendance history for a specific student.",
                schema("user_id", "days", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_mark_gd_attendance" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("session_id",   args.path("session_id").asText());
                d.put("user_id",      args.path("user_id").asText());
                d.put("status",       args.path("status").asText("PRESENT"));
                d.put("join_time",    args.path("join_time").asText("10:02"));
                d.put("late",         false);
                d.put("recorded",     true);
                d.put("xp_attendance_bonus", 15);
                yield AgentResponse.success(getName(), d, "Attendance marked: PRESENT.");
            }
            case "dojo_get_gd_attendance_report" -> {
                ArrayNode att = mapper.createArrayNode();
                String[][] data = {
                    {"USR-001","PRESENT","10:00"},
                    {"USR-007","PRESENT","10:01"},
                    {"USR-014","LATE",   "10:08"},
                    {"USR-022","PRESENT","09:59"},
                    {"USR-031","ABSENT", "—"},
                    {"USR-044","PRESENT","10:03"}
                };
                for (String[] row : data) {
                    ObjectNode e = mapper.createObjectNode();
                    e.put("user_id",   row[0]);
                    e.put("status",    row[1]);
                    e.put("join_time", row[2]);
                    att.add(e);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("attendance",  att);
                d.put("present",     4);
                d.put("late",        1);
                d.put("absent",      1);
                d.put("attendance_rate_pct", 83.3);
                yield AgentResponse.success(getName(), d, "GD attendance report: 83.3% present.");
            }
            case "dojo_get_user_gd_attendance_history" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",            args.path("user_id").asText());
                d.put("days",               args.path("days").asInt(90));
                d.put("sessions_scheduled", 10);
                d.put("sessions_attended",  9);
                d.put("sessions_late",      1);
                d.put("sessions_absent",    1);
                d.put("attendance_rate_pct", 90.0);
                yield AgentResponse.success(getName(), d, "GD attendance history: 90% rate.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 17 — DIFFICULTY_ADAPTATION_AGENT
// Dynamically adjusts content difficulty based on learner performance.
// ═══════════════════════════════════════════════════════════════════════════
class DifficultyAdaptationAgent extends BaseAgent {
    @Override public String getName() { return "DIFFICULTY_ADAPTATION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_get_recommended_difficulty",
                "Get the recommended difficulty level for the next content item for a user.",
                schema("user_id", "subject", "tenant_id")),
            new McpTool("dojo_record_performance_signal",
                "Record a performance signal (pass/fail/struggle) to update difficulty model.",
                schema("user_id", "subject", "result", "time_taken_sec", "tenant_id")),
            new McpTool("dojo_get_adaptation_history",
                "Get the difficulty adaptation history for a user in a subject.",
                schema("user_id", "subject", "days"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_get_recommended_difficulty" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",          args.path("user_id").asText());
                d.put("subject",          args.path("subject").asText("math"));
                d.put("current_level",    "MEDIUM");
                d.put("recommended",      "MEDIUM_HARD");
                d.put("confidence",       0.87);
                d.put("reason",           "3 consecutive correct answers at MEDIUM level");
                d.put("success_rate_pct", 82.4);
                yield AgentResponse.success(getName(), d, "Recommended: MEDIUM_HARD for next content.");
            }
            case "dojo_record_performance_signal" -> {
                String result = args.path("result").asText("pass");
                ObjectNode d  = mapper.createObjectNode();
                d.put("user_id",     args.path("user_id").asText());
                d.put("subject",     args.path("subject").asText("math"));
                d.put("result",      result);
                d.put("signal_id",   "sig-" + System.currentTimeMillis());
                d.put("model_updated", true);
                d.put("new_recommended", result.equals("fail") ? "MEDIUM" : "MEDIUM_HARD");
                yield AgentResponse.success(getName(), d, "Performance signal recorded. Model updated.");
            }
            case "dojo_get_adaptation_history" -> {
                ArrayNode hist = mapper.createArrayNode();
                String[][] rows = {
                    {"2026-02-20","EASY",     "pass"},
                    {"2026-02-25","MEDIUM",   "pass"},
                    {"2026-03-01","MEDIUM",   "pass"},
                    {"2026-03-04","MEDIUM_HARD","fail"},
                    {"2026-03-05","MEDIUM",   "pass"}
                };
                for (String[] r : rows) {
                    ObjectNode h = mapper.createObjectNode();
                    h.put("date",       r[0]);
                    h.put("difficulty", r[1]);
                    h.put("result",     r[2]);
                    hist.add(h);
                }
                ObjectNode d = mapper.createObjectNode();
                d.set("history", hist);
                d.put("trend",   "PROGRESSING");
                yield AgentResponse.success(getName(), d, "Adaptation history: progressing trend.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Agent 18 — DAILY_MISSION_AGENT
// Generates and tracks personalized daily missions for each student.
// ═══════════════════════════════════════════════════════════════════════════
class DailyMissionAgent extends BaseAgent {
    @Override public String getName() { return "DAILY_MISSION_AGENT"; }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            new McpTool("dojo_generate_daily_missions",
                "Generate personalized daily missions for a student based on profile.",
                schema("user_id", "date", "tenant_id")),
            new McpTool("dojo_complete_mission",
                "Mark a daily mission as complete and award XP.",
                schema("user_id", "mission_id", "proof", "tenant_id")),
            new McpTool("dojo_get_mission_status",
                "Get the status of all missions for a user on a given date.",
                schema("user_id", "date", "tenant_id")),
            new McpTool("dojo_get_mission_completion_stats",
                "Get mission completion statistics for a school or tenant.",
                schema("scope", "scope_id", "period", "tenant_id"))
        );
    }

    @Override
    public AgentResponse execute(String toolName, ObjectNode args) {
        return switch (toolName) {
            case "dojo_generate_daily_missions" -> {
                String uid  = args.path("user_id").asText();
                String date = args.path("date").asText("2026-03-05");
                ArrayNode missions = mapper.createArrayNode();
                Object[][] mData = {
                    {"M1","Complete 1 Math Quiz","quiz","50 XP",false},
                    {"M2","Maintain your streak","streak","30 XP",true},
                    {"M3","Share a learning insight","social","20 XP",false},
                    {"M4","Score 80%+ in any subject","score","100 XP",false}
                };
                for (Object[] m : mData) {
                    ObjectNode mission = mapper.createObjectNode();
                    mission.put("mission_id",  (String) m[0]);
                    mission.put("title",       (String) m[1]);
                    mission.put("type",        (String) m[2]);
                    mission.put("xp_reward",   (String) m[3]);
                    mission.put("completed",   (Boolean) m[4]);
                    missions.add(mission);
                }
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",         uid);
                d.put("date",            date);
                d.set("missions",        missions);
                d.put("total",           4);
                d.put("completed",       1);
                d.put("total_xp_available", 200);
                yield AgentResponse.success(getName(), d, "4 daily missions generated for " + uid + " on " + date);
            }
            case "dojo_complete_mission" -> {
                String mid = args.path("mission_id").asText();
                ObjectNode d = mapper.createObjectNode();
                d.put("mission_id",   mid);
                d.put("user_id",      args.path("user_id").asText());
                d.put("completed",    true);
                d.put("xp_awarded",   50);
                d.put("streak_bonus", false);
                d.put("completion_id","MC-" + System.currentTimeMillis());
                yield AgentResponse.success(getName(), d, "Mission " + mid + " completed! 50 XP awarded.");
            }
            case "dojo_get_mission_status" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("user_id",         args.path("user_id").asText());
                d.put("date",            args.path("date").asText("2026-03-05"));
                d.put("total_missions",  4);
                d.put("completed",       2);
                d.put("remaining",       2);
                d.put("xp_earned_today", 80);
                d.put("xp_remaining",    120);
                d.put("completion_pct",  50.0);
                yield AgentResponse.success(getName(), d, "Mission status: 2/4 completed (50%).");
            }
            case "dojo_get_mission_completion_stats" -> {
                ObjectNode d = mapper.createObjectNode();
                d.put("scope",              args.path("scope").asText("school"));
                d.put("scope_id",           args.path("scope_id").asText());
                d.put("period",             args.path("period").asText("7d"));
                d.put("avg_completion_pct", 67.4);
                d.put("most_completed_type","quiz");
                d.put("least_completed_type","social");
                d.put("total_xp_distributed", 48_200);
                d.put("students_perfect_day",  42);
                yield AgentResponse.success(getName(), d, "Mission completion stats: 67.4% avg.");
            }
            default -> AgentResponse.error(getName(), "Unknown tool: " + toolName);
        };
    }
}
