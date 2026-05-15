package com.ecoskiller.dojo.server;

import com.ecoskiller.dojo.agents.*;
import com.ecoskiller.dojo.models.AgentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ecoskiller Dojo MCP Server — Unit Tests (18 Agents)")
class DojoMcpServerTest {

    private DojoMcpServer server;
    private ObjectMapper  mapper;

    @BeforeEach void setUp() {
        server = new DojoMcpServer();
        mapper = new ObjectMapper();
    }

    // ─── Protocol ─────────────────────────────────────────────────────────

    @Test @DisplayName("initialize → serverInfo = mcp-16-dojo")
    void testInitialize() {
        String resp = server.handleMessage(
            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\",\"params\":{}}");
        assertTrue(resp.contains("mcp-16-dojo"));
        assertTrue(resp.contains("2024-11-05"));
        assertFalse(resp.contains("\"error\""));
    }

    @Test @DisplayName("tools/list → all 18 agents' tools present")
    void testToolsList() {
        String resp = server.handleMessage(
            "{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\",\"params\":{}}");
        assertTrue(resp.contains("dojo_award_xp"));
        assertTrue(resp.contains("dojo_calculate_teacher_incentive"));
        assertTrue(resp.contains("dojo_record_streak_activity"));
        assertTrue(resp.contains("dojo_generate_share_card"));
        assertTrue(resp.contains("dojo_get_school_performance_report"));
        assertTrue(resp.contains("dojo_compute_reputation_score"));
        assertTrue(resp.contains("dojo_compute_rank"));
        assertTrue(resp.contains("dojo_score_submission"));
        assertTrue(resp.contains("dojo_get_passport_timeline"));
        assertTrue(resp.contains("dojo_aggregate_passports"));
        assertTrue(resp.contains("dojo_get_level_status"));
        assertTrue(resp.contains("dojo_generate_invite_link"));
        assertTrue(resp.contains("dojo_score_innovation_submission"));
        assertTrue(resp.contains("dojo_schedule_gd_session"));
        assertTrue(resp.contains("dojo_analyze_gd_session"));
        assertTrue(resp.contains("dojo_mark_gd_attendance"));
        assertTrue(resp.contains("dojo_get_recommended_difficulty"));
        assertTrue(resp.contains("dojo_generate_daily_missions"));
    }

    @Test @DisplayName("18 agents registered")
    void testAgentCount() {
        assertEquals(18, server.registry.count());
    }

    @Test @DisplayName("ping → success")
    void testPing() {
        String resp = server.handleMessage("{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"ping\",\"params\":{}}");
        assertTrue(resp.contains("\"result\""));
    }

    @Test @DisplayName("unknown method → -32601")
    void testUnknownMethod() {
        String resp = server.handleMessage("{\"jsonrpc\":\"2.0\",\"id\":4,\"method\":\"fake\",\"params\":{}}");
        assertTrue(resp.contains("-32601"));
    }

    @Test @DisplayName("malformed JSON → -32700")
    void testMalformed() {
        String resp = server.handleMessage("{bad}");
        assertTrue(resp.contains("-32700"));
    }

    // ─── Agent 1: XP_ENGINE ───────────────────────────────────────────────

    @Test @DisplayName("XP_ENGINE: award_xp lesson_complete = 50 XP")
    void testAwardXp() {
        String resp = callTool("dojo_award_xp",
            "{\"user_id\":\"USR-001\",\"role\":\"student\",\"activity_type\":\"lesson_complete\",\"activity_id\":\"L1\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"xp_awarded\":50"));
        assertTrue(resp.contains("XP_ENGINE_AGENT"));
    }

    @Test @DisplayName("XP_ENGINE: get_xp_balance returns level and rank_title")
    void testGetXpBalance() {
        String resp = callTool("dojo_get_xp_balance", "{\"user_id\":\"USR-001\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("Skill Scout"));
        assertTrue(resp.contains("\"level\":12"));
    }

    @Test @DisplayName("XP_ENGINE: decay_xp affects 47 users")
    void testDecayXp() {
        String resp = callTool("dojo_decay_xp",
            "{\"tenant_id\":\"T1\",\"decay_rate_pct\":\"5.0\",\"inactivity_days\":\"14\"}");
        assertTrue(resp.contains("users_affected"));
    }

    // ─── Agent 2: TEACHER_INCENTIVE ───────────────────────────────────────

    @Test @DisplayName("TEACHER_INCENTIVE: calculate incentive = 7500 INR")
    void testTeacherIncentive() {
        String resp = callTool("dojo_calculate_teacher_incentive",
            "{\"teacher_id\":\"TCH-01\",\"period\":\"2026-02\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"total_incentive\":7500"));
        assertTrue(resp.contains("INR"));
    }

    // ─── Agent 3: STREAK_ENGINE ───────────────────────────────────────────

    @Test @DisplayName("STREAK_ENGINE: record activity extends streak to 14")
    void testStreakRecord() {
        String resp = callTool("dojo_record_streak_activity",
            "{\"user_id\":\"USR-001\",\"activity_date\":\"2026-03-05\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"current_streak\":14"));
    }

    @Test @DisplayName("STREAK_ENGINE: leaderboard returns top 5")
    void testStreakLeaderboard() {
        String resp = callTool("dojo_get_streak_leaderboard",
            "{\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"top_n\":\"5\"}");
        assertTrue(resp.contains("Arjun S."));
        assertTrue(resp.contains("\"streak\":92"));
    }

    // ─── Agent 4: SHARE_CARD ─────────────────────────────────────────────

    @Test @DisplayName("SHARE_CARD: generate card has image_url and og_url")
    void testShareCard() {
        String resp = callTool("dojo_generate_share_card",
            "{\"user_id\":\"USR-001\",\"card_type\":\"level_up\",\"achievement_id\":\"A1\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("image_url"));
        assertTrue(resp.contains("og_url"));
        assertTrue(resp.contains("ecoskiller.com"));
    }

    // ─── Agent 5: SCHOOL_PERFORMANCE ─────────────────────────────────────

    @Test @DisplayName("SCHOOL_PERFORMANCE: report contains pass_rate and top_subject")
    void testSchoolPerformance() {
        String resp = callTool("dojo_get_school_performance_report",
            "{\"school_id\":\"SCH-001\",\"period\":\"2026-Q1\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("pass_rate_pct"));
        assertTrue(resp.contains("Mathematics"));
    }

    @Test @DisplayName("SCHOOL_PERFORMANCE: subject gap shows 3 subjects")
    void testSubjectGap() {
        String resp = callTool("dojo_get_subject_gap_analysis",
            "{\"school_id\":\"SCH-001\",\"grade\":\"8\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("English Grammar"));
        assertTrue(resp.contains("HIGH"));
    }

    // ─── Agent 6: REPUTATION ─────────────────────────────────────────────

    @Test @DisplayName("REPUTATION: score = 847, grade = A")
    void testReputation() {
        String resp = callTool("dojo_compute_reputation_score",
            "{\"entity_id\":\"USR-001\",\"entity_type\":\"student\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"reputation_score\":847"));
        assertTrue(resp.contains("\"grade\":\"A\""));
    }

    // ─── Agent 7: RANK ────────────────────────────────────────────────────

    @Test @DisplayName("RANK: compute rank = 7, percentile = 97.8")
    void testRank() {
        String resp = callTool("dojo_compute_rank",
            "{\"user_id\":\"USR-001\",\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"metric\":\"xp\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"rank\":7"));
        assertTrue(resp.contains("97.8"));
    }

    @Test @DisplayName("RANK: leaderboard top 10 includes Arjun")
    void testLeaderboard() {
        String resp = callTool("dojo_get_rank_leaderboard",
            "{\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"metric\":\"xp\",\"top_n\":\"10\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("Arjun S."));
    }

    // ─── Agent 8: PROBLEM_DETECTION ──────────────────────────────────────

    @Test @DisplayName("PROBLEM_DETECTION: score 82/100 grade B+")
    void testProblemScore() {
        String resp = callTool("dojo_score_submission",
            "{\"submission_id\":\"SUB-001\",\"user_id\":\"USR-001\",\"problem_id\":\"P1\",\"answer\":\"...\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"score\":82"));
        assertTrue(resp.contains("\"grade\":\"B+\""));
        assertTrue(resp.contains("xp_earned"));
    }

    // ─── Agent 9: PASSPORT_HISTORY ───────────────────────────────────────

    @Test @DisplayName("PASSPORT_HISTORY: timeline has 4 entries")
    void testPassportTimeline() {
        String resp = callTool("dojo_get_passport_timeline",
            "{\"user_id\":\"USR-001\",\"from_date\":\"2026-01-01\",\"to_date\":\"2026-03-05\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"total\":4"));
        assertTrue(resp.contains("Innovation Starter"));
    }

    @Test @DisplayName("PASSPORT_HISTORY: add entry returns immutable=true")
    void testAddPassportEntry() {
        String resp = callTool("dojo_add_passport_entry",
            "{\"user_id\":\"USR-001\",\"entry_type\":\"badge\",\"entry_data\":\"{}\",\"issued_by\":\"TEACHER-01\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"immutable\":true"));
        assertTrue(resp.contains("hash"));
    }

    // ─── Agent 10: PASSPORT_AGGREGATION ──────────────────────────────────

    @Test @DisplayName("PASSPORT_AGGREGATION: 320 students, 99.1% verified")
    void testPassportAggregation() {
        String resp = callTool("dojo_aggregate_passports",
            "{\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"metric\":\"all\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"total_students\":320"));
        assertTrue(resp.contains("99.1"));
    }

    // ─── Agent 11: LEVEL_PROGRESSION ─────────────────────────────────────

    @Test @DisplayName("LEVEL_PROGRESSION: level 12, needs 680 XP")
    void testLevelStatus() {
        String resp = callTool("dojo_get_level_status",
            "{\"user_id\":\"USR-001\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"current_level\":12"));
        assertTrue(resp.contains("\"xp_needed\":680"));
    }

    // ─── Agent 12: INVITE_TRACKING ───────────────────────────────────────

    @Test @DisplayName("INVITE_TRACKING: generate link has xp_reward 200")
    void testInviteLink() {
        String resp = callTool("dojo_generate_invite_link",
            "{\"inviter_id\":\"USR-001\",\"inviter_type\":\"student\",\"campaign\":\"spring2026\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("invite_url"));
        assertTrue(resp.contains("\"xp_reward_on_convert\":200"));
    }

    @Test @DisplayName("INVITE_TRACKING: conversion awards XP to both")
    void testInviteConversion() {
        String resp = callTool("dojo_record_invite_conversion",
            "{\"invite_code\":\"INV-USR-1234\",\"invitee_id\":\"USR-999\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("inviter_xp_awarded"));
        assertTrue(resp.contains("invitee_xp_awarded"));
    }

    // ─── Agent 13: INNOVATION_SKILL ───────────────────────────────────────

    @Test @DisplayName("INNOVATION_SKILL: score 81/100 grade A-")
    void testInnovationScore() {
        String resp = callTool("dojo_score_innovation_submission",
            "{\"submission_id\":\"IS-001\",\"user_id\":\"USR-001\",\"category\":\"sustainability\",\"content\":\"...\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"total_score\":81"));
        assertTrue(resp.contains("\"grade\":\"A-\""));
    }

    // ─── Agent 14: GD_SCHEDULER ───────────────────────────────────────────

    @Test @DisplayName("GD_SCHEDULER: schedule session returns meet_link")
    void testGdSchedule() {
        String resp = callTool("dojo_schedule_gd_session",
            "{\"school_id\":\"SCH-001\",\"topic\":\"AI in Education\",\"date\":\"2026-03-10\",\"time\":\"10:00\",\"duration_min\":\"45\",\"max_participants\":\"8\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("meet.ecoskiller.com"));
        assertTrue(resp.contains("SCHEDULED"));
    }

    @Test @DisplayName("GD_SCHEDULER: upcoming sessions returns 3")
    void testUpcomingSessions() {
        String resp = callTool("dojo_get_upcoming_gd_sessions",
            "{\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"days_ahead\":\"14\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"total\":3"));
        assertTrue(resp.contains("Climate Change"));
    }

    // ─── Agent 15: GD_POST_ANALYTICS ─────────────────────────────────────

    @Test @DisplayName("GD_POST_ANALYTICS: quality_grade = A-")
    void testGdAnalytics() {
        String resp = callTool("dojo_analyze_gd_session",
            "{\"session_id\":\"GD-001\",\"transcript_url\":\"https://...\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"quality_grade\":\"A-\""));
        assertTrue(resp.contains("participation_balance"));
    }

    @Test @DisplayName("GD_POST_ANALYTICS: participant scores for 6 users")
    void testGdParticipantScores() {
        String resp = callTool("dojo_get_gd_participant_scores",
            "{\"session_id\":\"GD-001\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("USR-007"));
        assertTrue(resp.contains("xp_earned"));
    }

    // ─── Agent 16: GD_ATTENDANCE ──────────────────────────────────────────

    @Test @DisplayName("GD_ATTENDANCE: mark present awards attendance bonus")
    void testGdAttendanceMark() {
        String resp = callTool("dojo_mark_gd_attendance",
            "{\"session_id\":\"GD-001\",\"user_id\":\"USR-001\",\"status\":\"PRESENT\",\"join_time\":\"10:00\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"xp_attendance_bonus\":15"));
    }

    @Test @DisplayName("GD_ATTENDANCE: report shows 83.3% attendance")
    void testGdAttendanceReport() {
        String resp = callTool("dojo_get_gd_attendance_report",
            "{\"session_id\":\"GD-001\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("83.3"));
        assertTrue(resp.contains("ABSENT"));
    }

    // ─── Agent 17: DIFFICULTY_ADAPTATION ─────────────────────────────────

    @Test @DisplayName("DIFFICULTY_ADAPTATION: recommends MEDIUM_HARD")
    void testDifficultyRecommend() {
        String resp = callTool("dojo_get_recommended_difficulty",
            "{\"user_id\":\"USR-001\",\"subject\":\"math\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("MEDIUM_HARD"));
        assertTrue(resp.contains("confidence"));
    }

    @Test @DisplayName("DIFFICULTY_ADAPTATION: history trend = PROGRESSING")
    void testAdaptationHistory() {
        String resp = callTool("dojo_get_adaptation_history",
            "{\"user_id\":\"USR-001\",\"subject\":\"math\",\"days\":\"30\"}");
        assertTrue(resp.contains("PROGRESSING"));
    }

    // ─── Agent 18: DAILY_MISSION ──────────────────────────────────────────

    @Test @DisplayName("DAILY_MISSION: generate 4 missions, 200 XP available")
    void testDailyMissions() {
        String resp = callTool("dojo_generate_daily_missions",
            "{\"user_id\":\"USR-001\",\"date\":\"2026-03-05\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"total\":4"));
        assertTrue(resp.contains("\"total_xp_available\":200"));
    }

    @Test @DisplayName("DAILY_MISSION: complete mission awards 50 XP")
    void testCompleteMission() {
        String resp = callTool("dojo_complete_mission",
            "{\"user_id\":\"USR-001\",\"mission_id\":\"M1\",\"proof\":\"screenshot\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"xp_awarded\":50"));
        assertTrue(resp.contains("\"completed\":true"));
    }

    @Test @DisplayName("DAILY_MISSION: status 2/4 = 50% complete")
    void testMissionStatus() {
        String resp = callTool("dojo_get_mission_status",
            "{\"user_id\":\"USR-001\",\"date\":\"2026-03-05\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("\"completed\":2"));
        assertTrue(resp.contains("50.0"));
    }

    @Test @DisplayName("DAILY_MISSION: stats show 67.4% avg completion")
    void testMissionStats() {
        String resp = callTool("dojo_get_mission_completion_stats",
            "{\"scope\":\"school\",\"scope_id\":\"SCH-001\",\"period\":\"7d\",\"tenant_id\":\"T1\"}");
        assertTrue(resp.contains("67.4"));
    }

    // ─── Edge cases ───────────────────────────────────────────────────────

    @Test @DisplayName("unknown tool → -32602")
    void testUnknownTool() {
        String resp = callTool("dojo_nonexistent_tool", "{}");
        assertTrue(resp.contains("-32602") || resp.contains("Tool not found"));
    }

    // ─── Helper ───────────────────────────────────────────────────────────

    private String callTool(String toolName, String argsJson) {
        return server.handleMessage(
            "{\"jsonrpc\":\"2.0\",\"id\":99,\"method\":\"tools/call\"," +
            "\"params\":{\"name\":\"" + toolName + "\",\"arguments\":" + argsJson + "}}");
    }
}
