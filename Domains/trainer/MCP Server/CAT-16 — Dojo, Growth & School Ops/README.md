# Ecoskiller Dojo MCP Server
## `mcp-16-dojo` | CAT-16 — Dojo, Growth & School Ops

---

### Overview

Java MCP Server (JSON-RPC 2.0 over **stdio**) hosting all **18 Dojo agents** for the Ecoskiller platform.

| Property | Value |
|---|---|
| MCP Server ID | `mcp-16-dojo` |
| Namespace | `realtime` |
| Priority | HIGH (Sprint 2) |
| Agents | 18 |
| Java | 21+ |
| Transport | stdio (JSON-RPC 2.0) |

---

### Agents & Tools

| # | Agent | Tools |
|---|---|---|
| 1 | `XP_ENGINE_AGENT` | `award_xp`, `get_xp_balance`, `apply_xp_multiplier`, `decay_xp` |
| 2 | `TEACHER_INCENTIVE_AGENT` | `calculate_teacher_incentive`, `get_teacher_kpi_dashboard`, `set_incentive_rules` |
| 3 | `STREAK_ENGINE_AGENT` | `record_streak_activity`, `get_streak_status`, `apply_streak_freeze`, `get_streak_leaderboard` |
| 4 | `SHARE_CARD_GENERATOR_AGENT` | `generate_share_card`, `get_share_card_templates`, `track_share_event` |
| 5 | `SCHOOL_PERFORMANCE_ANALYTICS_AGENT` | `get_school_performance_report`, `compare_schools`, `get_subject_gap_analysis` |
| 6 | `REPUTATION_SCORE_AGENT` | `compute_reputation_score`, `get_reputation_history`, `flag_reputation_event` |
| 7 | `RANK_COMPUTATION_AGENT` | `compute_rank`, `get_rank_leaderboard`, `get_rank_history` |
| 8 | `PROBLEM_DETECTION_SCORING_AGENT` | `score_submission`, `detect_reasoning_pattern`, `get_scoring_rubric` |
| 9 | `PASSPORT_HISTORY_AGENT` | `get_passport_timeline`, `add_passport_entry`, `verify_passport_entry` |
| 10 | `PASSPORT_AGGREGATION_AGENT` | `aggregate_passports`, `export_passport_report` |
| 11 | `LEVEL_PROGRESSION_AGENT` | `get_level_status`, `trigger_level_up`, `configure_level_thresholds` |
| 12 | `INVITE_TRACKING_AGENT` | `generate_invite_link`, `record_invite_conversion`, `get_invite_analytics` |
| 13 | `INNOVATION_SKILL_SCORING_AGENT` | `score_innovation_submission`, `get_innovation_skill_profile`, `rank_innovation_submissions` |
| 14 | `GD_SESSION_SCHEDULER_AGENT` | `schedule_gd_session`, `enroll_in_gd_session`, `get_upcoming_gd_sessions`, `cancel_gd_session` |
| 15 | `GD_POST_SESSION_ANALYTICS_AGENT` | `analyze_gd_session`, `get_gd_participant_scores`, `get_gd_session_summary` |
| 16 | `GD_ATTENDANCE_TRACKING_AGENT` | `mark_gd_attendance`, `get_gd_attendance_report`, `get_user_gd_attendance_history` |
| 17 | `DIFFICULTY_ADAPTATION_AGENT` | `get_recommended_difficulty`, `record_performance_signal`, `get_adaptation_history` |
| 18 | `DAILY_MISSION_AGENT` | `generate_daily_missions`, `complete_mission`, `get_mission_status`, `get_mission_completion_stats` |

**Total tools: 57**

---

### Build & Run

```bash
# Build fat JAR
mvn clean package -q

# Run as MCP server (reads stdin, writes stdout)
java -jar target/mcp-16-dojo-1.0.0.jar

# Run tests (40 test cases)
mvn test
```

---

### Example Calls

```json
// Award XP for lesson completion
{"jsonrpc":"2.0","id":1,"method":"tools/call",
 "params":{"name":"dojo_award_xp",
           "arguments":{"user_id":"USR-001","activity_type":"lesson_complete","tenant_id":"T1"}}}

// Schedule a GD session
{"jsonrpc":"2.0","id":2,"method":"tools/call",
 "params":{"name":"dojo_schedule_gd_session",
           "arguments":{"school_id":"SCH-001","topic":"AI in Education",
                        "date":"2026-03-10","time":"10:00","duration_min":"45",
                        "max_participants":"8","tenant_id":"T1"}}}

// Generate daily missions for a student
{"jsonrpc":"2.0","id":3,"method":"tools/call",
 "params":{"name":"dojo_generate_daily_missions",
           "arguments":{"user_id":"USR-001","date":"2026-03-05","tenant_id":"T1"}}}
```

---

### Project Structure

```
mcp-16-dojo/
├── pom.xml
├── mcp-client-config.json
└── src/
    ├── main/java/com/ecoskiller/dojo/
    │   ├── server/
    │   │   ├── DojoMcpServer.java        ← Entry point + MCP protocol
    │   │   └── AgentRegistry.java
    │   ├── agents/
    │   │   ├── BaseAgent.java
    │   │   ├── AgentsGroup1.java         ← Agents 1–6  (XP→Reputation)
    │   │   ├── AgentsGroup2.java         ← Agents 7–12 (Rank→Invite)
    │   │   └── AgentsGroup3.java         ← Agents 13–18 (Innovation→Daily Mission)
    │   └── models/
    │       ├── McpTool.java
    │       └── AgentResponse.java
    └── test/
        └── DojoMcpServerTest.java        ← 40 test cases
```

---

*Ecoskiller Engineering | mcp-16-dojo v1.0.0 | Java 21 | MCP 2024-11-05*
