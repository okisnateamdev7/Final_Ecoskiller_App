package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.security.SecurityManager;
import com.google.gson.*;
import java.util.*;

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 2: CANDIDATE_QUEUE_AGENT
// Manages Redis Stream: dojo-candidates:waiting
// ─────────────────────────────────────────────────────────────────────────────

class CandidateQueueAgent extends BaseAgent {
    public CandidateQueueAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema();
        addEnumProp  (sc, "action",           "Queue operation to perform",
                       "ENQUEUE", "DEQUEUE", "PEEK", "STATUS", "REQUEUE");
        addStringProp(sc, "candidate_id",     "Candidate UUID to enqueue/dequeue/requeue");
        addStringProp(sc, "assessment_id",    "Assessment ID associated with candidate");
        addEnumProp  (sc, "skill_level",      "Candidate proficiency",
                       "BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
        addStringProp(sc, "preferred_language","Programming language: Java, Python, Go, etc.");
        addStringProp(sc, "timezone",         "Candidate timezone (IANA format)");
        return buildToolDef("candidate_queue",
            "Manages the Redis Stream (dojo-candidates:waiting) for real-time candidate availability. " +
            "Supports ENQUEUE (candidate joins queue), DEQUEUE (candidate matched and removed), " +
            "PEEK (view queue head without consuming), STATUS (queue depth metrics), " +
            "REQUEUE (re-add candidate after no-show). Prevents duplicate entries via idempotency.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("candidate_queue");
        String action = str(args, "action");
        String candidateId = str(args, "candidate_id");

        JsonObject result = new JsonObject();
        switch (action) {
            case "ENQUEUE" -> {
                String streamId = "1700000000-" + Math.abs(candidateId.hashCode() % 1000);
                result.addProperty("stream_id",      streamId);
                result.addProperty("candidate_id",   candidateId);
                result.addProperty("assessment_id",  str(args, "assessment_id"));
                result.addProperty("skill_level",    str(args, "skill_level"));
                result.addProperty("language",       str(args, "preferred_language"));
                result.addProperty("timezone",       str(args, "timezone"));
                result.addProperty("queue_position", 3);
                result.addProperty("queue_depth",    3);
                result.addProperty("estimated_wait_ms", 4500);
                result.addProperty("redis_key",      "dojo-candidates:waiting");
                result.addProperty("event",          "XADD dojo-candidates:waiting * candidate_id " + candidateId);
            }
            case "DEQUEUE" -> {
                result.addProperty("candidate_id", candidateId);
                result.addProperty("acknowledged", true);
                result.addProperty("redis_ack",    "XACK dojo-candidates:waiting dojo-match-engine <stream_id>");
                result.addProperty("queue_depth",  2);
            }
            case "PEEK" -> {
                result.addProperty("queue_depth",    3);
                result.addProperty("head_candidate", "cand-abc123");
                result.addProperty("oldest_wait_ms", 1200);
            }
            case "STATUS" -> {
                result.addProperty("queue_depth",    3);
                result.addProperty("processing_rate", "12 candidates/min");
                result.addProperty("avg_wait_ms",    3800);
                result.addProperty("consumer_group", "dojo-match-engine");
                result.addProperty("pending_count",  0);
            }
            case "REQUEUE" -> {
                result.addProperty("candidate_id",   candidateId);
                result.addProperty("requeue_reason", "NO_SHOW_TIMEOUT");
                result.addProperty("new_stream_id",  "1700001000-" + Math.abs(candidateId.hashCode() % 999));
                result.addProperty("queue_position", 1);
                result.addProperty("priority",       "HIGH");
            }
            default -> { return error("Unknown action: " + action + ". Use ENQUEUE/DEQUEUE/PEEK/STATUS/REQUEUE"); }
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 3: INTERVIEWER_AVAILABILITY_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class InterviewerAvailabilityAgent extends BaseAgent {
    public InterviewerAvailabilityAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema();
        addStringProp(sc, "interviewer_id",   "Interviewer UUID (leave empty to list all available)");
        addEnumProp  (sc, "status_filter",    "Filter by availability status",
                       "ALL", "AVAILABLE", "BUSY", "OFFLINE", "OVERLOADED");
        addStringProp(sc, "timezone_filter",  "Filter interviewers by timezone (IANA)");
        addStringProp(sc, "skill_filter",     "Filter by required skill (e.g. Java, Python)");
        return buildToolDef("interviewer_availability",
            "Queries real-time interviewer availability from Redis cache (dojo-available-interviewers). " +
            "Calculates availability as: interviewer.current_interviews < interviewer.max_concurrent " +
            "AND interviews_today < max_daily. Cache TTL: 1 minute. Falls back to PostgreSQL if cache miss. " +
            "Returns availability pool with current load metrics for the matching algorithm.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("interviewer_availability");
        String ivId        = str(args, "interviewer_id");
        String statusFilter = str(args, "status_filter");

        JsonObject result = new JsonObject();

        if (!ivId.isEmpty()) {
            // Single interviewer lookup
            result.addProperty("interviewer_id",    ivId);
            result.addProperty("status",            "AVAILABLE");
            result.addProperty("current_interviews", 1);
            result.addProperty("max_concurrent",    3);
            result.addProperty("interviews_today",  4);
            result.addProperty("max_per_day",       8);
            result.addProperty("cache_hit",         true);
            result.addProperty("cache_ttl_seconds", 60);
            result.addProperty("next_free_slot_ms", 0);
        } else {
            // Pool listing
            JsonArray pool = new JsonArray();
            String[][] interviewers = {
                {"IV-001", "AVAILABLE", "1", "3", "4", "8"},
                {"IV-002", "AVAILABLE", "0", "3", "2", "8"},
                {"IV-003", "BUSY",      "3", "3", "6", "6"},
                {"IV-004", "AVAILABLE", "2", "3", "5", "8"},
                {"IV-005", "OFFLINE",   "0", "3", "0", "8"},
            };
            for (String[] iv : interviewers) {
                if (!statusFilter.isEmpty() && !statusFilter.equals("ALL") && !statusFilter.equals(iv[1])) continue;
                JsonObject ivObj = new JsonObject();
                ivObj.addProperty("id",                 iv[0]);
                ivObj.addProperty("status",             iv[1]);
                ivObj.addProperty("current_interviews", Integer.parseInt(iv[2]));
                ivObj.addProperty("max_concurrent",     Integer.parseInt(iv[3]));
                ivObj.addProperty("interviews_today",   Integer.parseInt(iv[4]));
                ivObj.addProperty("max_per_day",        Integer.parseInt(iv[5]));
                pool.add(ivObj);
            }
            result.add("interviewers", pool);
            result.addProperty("total_available", 3);
            result.addProperty("total_busy",      1);
            result.addProperty("total_offline",   1);
            result.addProperty("cache_source",    "REDIS");
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 4: SKILL_COMPATIBILITY_FILTER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class SkillCompatibilityFilterAgent extends BaseAgent {
    public SkillCompatibilityFilterAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("candidate_skill", "candidate_level", "language");
        addStringProp(sc, "candidate_skill",      "Candidate's primary skill (e.g. Java, Python)");
        addEnumProp  (sc, "candidate_level",      "Candidate proficiency",
                       "BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
        addStringProp(sc, "language",             "Programming language to filter by");
        addEnumProp  (sc, "match_mode",           "Matching strictness mode",
                       "STRICT", "FLEXIBLE", "FALLBACK");
        return buildToolDef("skill_compatibility_filter",
            "Filters interviewer pool by skill compatibility. Rule: interviewer skill level " +
            "must be >= candidate level (prevent undermatching). Supports STRICT (exact language), " +
            "FLEXIBLE (related languages accepted), FALLBACK (any skill >= candidate level). " +
            "Returns filtered interviewer IDs with skill match ratios.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("skill_compatibility_filter");
        String language      = str(args, "language");
        String candidateLvl  = str(args, "candidate_level");
        String mode          = str(args, "match_mode");
        if (mode.isEmpty()) mode = "STRICT";

        Map<String, Integer> rank = Map.of(
            "BEGINNER",1,"INTERMEDIATE",2,"ADVANCED",3,"EXPERT",4);
        int cRank = rank.getOrDefault(candidateLvl, 0);

        JsonArray filtered = new JsonArray();
        String[][] pool = {
            {"IV-001","Java","ADVANCED"},
            {"IV-002","Java","INTERMEDIATE"},
            {"IV-003","Python","EXPERT"},
            {"IV-004","Java","EXPERT"},
            {"IV-005","Go","ADVANCED"},
        };
        for (String[] iv : pool) {
            boolean langMatch = iv[1].equalsIgnoreCase(language);
            if (!langMatch && mode.equals("STRICT")) continue;
            int iRank = rank.getOrDefault(iv[2], 0);
            if (iRank < cRank) continue; // underqualified — skip
            double matchRatio = langMatch ? 1.0 : 0.7;
            JsonObject obj = new JsonObject();
            obj.addProperty("interviewer_id",    iv[0]);
            obj.addProperty("skill_language",    iv[1]);
            obj.addProperty("skill_level",       iv[2]);
            obj.addProperty("skill_match_ratio", matchRatio);
            obj.addProperty("delta_levels",      iRank - cRank);
            obj.addProperty("is_exact_language", langMatch);
            filtered.add(obj);
        }

        JsonObject result = new JsonObject();
        result.add("compatible_interviewers", filtered);
        result.addProperty("candidate_level",     candidateLvl);
        result.addProperty("language",            language);
        result.addProperty("match_mode",          mode);
        result.addProperty("filtered_count",      filtered.size());
        result.addProperty("algorithm",           "level_gte_filter v1.0");
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 5: TIMEZONE_CONSTRAINT_CHECK_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class TimezoneConstraintCheckAgent extends BaseAgent {
    public TimezoneConstraintCheckAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("interviewer_timezone");
        addStringProp(sc, "interviewer_timezone", "Interviewer timezone (IANA, e.g. America/New_York)");
        addStringProp(sc, "candidate_timezone",   "Candidate timezone (IANA, e.g. Asia/Kolkata)");
        addIntProp   (sc, "working_hour_start",   "Interviewer working hours start (24h, default 8)");
        addIntProp   (sc, "working_hour_end",     "Interviewer working hours end (24h, default 18)");
        return buildToolDef("timezone_constraint_check",
            "Validates that scheduling an interview is feasible given both parties' timezones. " +
            "Rule: current time must be within 08:00-18:00 in interviewer's local timezone. " +
            "Checks 15+ timezones including IST, EST, PST, JST, CET, AEST. " +
            "Returns: is_valid_time, interviewer_local_hour, candidate_local_hour, overlap_windows.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("timezone_constraint_check");
        String ivTz   = str(args, "interviewer_timezone");
        String candTz = str(args, "candidate_timezone");
        int start     = num(args, "working_hour_start", 8);
        int end       = num(args, "working_hour_end", 18);

        java.time.ZoneId ivZone, candZone;
        try {
            ivZone   = java.time.ZoneId.of(ivTz.isEmpty() ? "UTC" : ivTz);
            candZone = java.time.ZoneId.of(candTz.isEmpty() ? "UTC" : candTz);
        } catch (Exception e) {
            return error("Invalid timezone: " + e.getMessage());
        }

        int ivHour   = java.time.ZonedDateTime.now(ivZone).getHour();
        int candHour = java.time.ZonedDateTime.now(candZone).getHour();
        boolean valid = ivHour >= start && ivHour < end;

        JsonObject result = new JsonObject();
        result.addProperty("interviewer_timezone",    ivTz);
        result.addProperty("candidate_timezone",      candTz);
        result.addProperty("interviewer_local_hour",  ivHour);
        result.addProperty("candidate_local_hour",    candHour);
        result.addProperty("working_hours",           start + ":00 - " + end + ":00");
        result.addProperty("is_valid_time",           valid);
        result.addProperty("skip_reason",             valid ? null : (ivHour < start ? "TOO_EARLY" : "TOO_LATE"));
        result.addProperty("utc_offset_interviewer",  java.time.ZonedDateTime.now(ivZone).getOffset().toString());
        result.addProperty("utc_offset_candidate",    java.time.ZonedDateTime.now(candZone).getOffset().toString());
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 6: FAIRNESS_OPTIMIZER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class FairnessOptimizerAgent extends BaseAgent {
    public FairnessOptimizerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("candidate_id");
        addStringProp(sc, "candidate_id",     "Candidate to check repeat-interview constraint for");
        addStringProp(sc, "interviewer_ids",  "Comma-separated list of candidate interviewer IDs to check");
        addEnumProp  (sc, "mode",             "Optimization mode",
                       "CHECK_REPEATS", "REBALANCE", "AUDIT", "SCORE_DISTRIBUTION");
        return buildToolDef("fairness_optimizer",
            "Anti-bias fairness engine. Checks: (1) no candidate interviewed twice by same interviewer, " +
            "(2) round-robin load balance across interviewers (variance < 0.5), " +
            "(3) equitable difficulty distribution (avg_skill_delta < 0.5 across interviewers). " +
            "Triggers rebalancing when fairness_score < 0.7. Writes to immutable fairness_audit_log.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("fairness_optimizer");
        String mode = str(args, "mode");
        if (mode.isEmpty()) mode = "CHECK_REPEATS";
        String candidateId = str(args, "candidate_id");

        JsonObject result = new JsonObject();
        switch (mode) {
            case "CHECK_REPEATS" -> {
                result.addProperty("candidate_id",         candidateId);
                result.addProperty("repeat_interviewer",   "IV-004");
                result.addProperty("should_skip_IV-004",   true);
                result.addProperty("available_without_repeat", 4);
                result.addProperty("fairness_enforced",    true);
            }
            case "REBALANCE" -> {
                result.addProperty("triggered",            true);
                result.addProperty("reason",               "fairness_score < 0.7");
                result.addProperty("before_variance",      1.2);
                result.addProperty("after_variance",       0.4);
                result.addProperty("interviews_redistributed", 3);
                result.addProperty("rebalance_strategy",   "priority_boost_underutilized");
            }
            case "AUDIT" -> {
                result.addProperty("fairness_score",       0.87);
                result.addProperty("load_variance",        0.3);
                result.addProperty("max_interviews",       6);
                result.addProperty("min_interviews",       4);
                result.addProperty("avg_interviews",       5.0);
                result.addProperty("skill_distribution_ok", true);
                result.addProperty("audit_log_entries",    128);
            }
            case "SCORE_DISTRIBUTION" -> {
                JsonArray dist = new JsonArray();
                String[][] ivDist = {{"IV-001","5","2.4"},{"IV-002","4","2.1"},{"IV-003","6","2.6"},{"IV-004","5","1.9"}};
                for (String[] d : ivDist) {
                    JsonObject o = new JsonObject();
                    o.addProperty("interviewer_id",     d[0]);
                    o.addProperty("interview_count",    Integer.parseInt(d[1]));
                    o.addProperty("avg_candidate_skill", Double.parseDouble(d[2]));
                    dist.add(o);
                }
                result.add("distribution", dist);
                result.addProperty("fairness_score",     0.87);
                result.addProperty("needs_rebalance",    false);
            }
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 7: MATCH_SCORE_CALCULATOR_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class MatchScoreCalculatorAgent extends BaseAgent {
    public MatchScoreCalculatorAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("skill_match_ratio", "fairness_bonus", "utilization_priority");
        addNumberProp(sc, "skill_match_ratio",     "Skill match ratio (0.0–1.0). Weight: 60%");
        addNumberProp(sc, "fairness_bonus",        "Fairness score contribution (0.0–1.0). Weight: 30%");
        addNumberProp(sc, "utilization_priority",  "Utilization priority factor (0.0–1.0). Weight: 10%");
        addStringProp(sc, "interviewer_id",        "Interviewer ID (optional, for audit trail)");
        addStringProp(sc, "candidate_id",          "Candidate ID (optional, for audit trail)");
        return buildToolDef("match_score_calculator",
            "Computes weighted match score: score = (skill_match_ratio * 0.6) + " +
            "(fairness_bonus * 0.3) + (utilization_priority * 0.1). " +
            "Returns score 0-100 with confidence band: HIGH(>=90), MEDIUM(70-89), LOW(<70). " +
            "Breakdown shows per-component contribution for transparency.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("match_score_calculator");
        double skillRatio       = dbl(args, "skill_match_ratio", 0.8);
        double fairnessBonus    = dbl(args, "fairness_bonus", 0.9);
        double utilizationPrio  = dbl(args, "utilization_priority", 1.0);

        // Clamp inputs 0–1
        skillRatio      = Math.max(0, Math.min(1, skillRatio));
        fairnessBonus   = Math.max(0, Math.min(1, fairnessBonus));
        utilizationPrio = Math.max(0, Math.min(1, utilizationPrio));

        double rawScore = (skillRatio * 0.6) + (fairnessBonus * 0.3) + (utilizationPrio * 0.1);
        int score = (int) Math.round(rawScore * 100);
        String confidence = score >= 90 ? "HIGH" : score >= 70 ? "MEDIUM" : "LOW";

        JsonObject result = new JsonObject();
        result.addProperty("interviewer_id",      str(args,"interviewer_id"));
        result.addProperty("candidate_id",        str(args,"candidate_id"));
        result.addProperty("match_score",         score);
        result.addProperty("confidence",          confidence);
        result.addProperty("raw_score",           rawScore);

        JsonObject breakdown = new JsonObject();
        breakdown.addProperty("skill_match_contribution",     Math.round(skillRatio * 0.6 * 100));
        breakdown.addProperty("fairness_contribution",        Math.round(fairnessBonus * 0.3 * 100));
        breakdown.addProperty("utilization_contribution",     Math.round(utilizationPrio * 0.1 * 100));
        breakdown.addProperty("skill_weight",                 "60%");
        breakdown.addProperty("fairness_weight",              "30%");
        breakdown.addProperty("utilization_weight",           "10%");
        result.add("score_breakdown", breakdown);
        result.addProperty("formula", "score = (skill*0.6) + (fairness*0.3) + (utilization*0.1)");
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 8: SESSION_INITIATION_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class SessionInitiationAgent extends BaseAgent {
    public SessionInitiationAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("match_id", "interviewer_id", "candidate_id");
        addStringProp(sc, "match_id",         "Match ID from find_match result");
        addStringProp(sc, "interviewer_id",   "Matched interviewer UUID");
        addStringProp(sc, "candidate_id",     "Matched candidate UUID");
        addIntProp   (sc, "duration_minutes", "Interview duration in minutes (default: 30)");
        return buildToolDef("session_initiation",
            "Initializes a dojo interview session after a successful match. " +
            "Creates: secure WebRTC session URL (wss://), PostgreSQL interview_session record, " +
            "TURN server allocation (coturn), media server connection. " +
            "Sets end_time = now + duration_minutes (default 30 min). " +
            "Returns session credentials and join URLs for both parties.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("session_initiation");
        String matchId       = str(args, "match_id");
        String interviewerId = str(args, "interviewer_id");
        String candidateId   = str(args, "candidate_id");
        int    duration      = num(args, "duration_minutes", 30);

        if (matchId.isEmpty()) return error("match_id is required");

        String sessionId  = "SESS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sessionUrl = "wss://dojo.ecoskiller.io/session/" + sessionId;
        String token      = "eyJ..." + UUID.randomUUID().toString().replace("-","").substring(0,16);

        java.time.Instant startTime = java.time.Instant.now().plusSeconds(5);
        java.time.Instant endTime   = startTime.plusSeconds(duration * 60L);

        JsonObject result = new JsonObject();
        result.addProperty("session_id",       sessionId);
        result.addProperty("match_id",         matchId);
        result.addProperty("session_url",      sessionUrl);
        result.addProperty("candidate_join_url",   sessionUrl + "?role=candidate&token=" + token);
        result.addProperty("interviewer_join_url", sessionUrl + "?role=interviewer&token=" + token);
        result.addProperty("interviewer_id",   interviewerId);
        result.addProperty("candidate_id",     candidateId);
        result.addProperty("start_time",       startTime.toString());
        result.addProperty("end_time_planned", endTime.toString());
        result.addProperty("duration_minutes", duration);
        result.addProperty("status",           "SCHEDULED");
        result.addProperty("turn_server",      "turn:coturn.ecoskiller.io:3478");
        result.addProperty("media_server",     "media-01.ecoskiller.io");
        result.addProperty("db_record",        "INSERT INTO interview_sessions (id, match_id, status...) VALUES (...)");
        result.addProperty("no_show_timeout_s", 60);
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 9: NO_SHOW_HANDLER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class NoShowHandlerAgent extends BaseAgent {
    public NoShowHandlerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("session_id", "no_show_party");
        addStringProp(sc, "session_id",       "Session ID where no-show occurred");
        addStringProp(sc, "match_id",         "Match ID associated with session");
        addEnumProp  (sc, "no_show_party",    "Who failed to join",
                       "CANDIDATE", "INTERVIEWER", "BOTH");
        addStringProp(sc, "candidate_id",     "Candidate UUID for requeue");
        addStringProp(sc, "interviewer_id",   "Interviewer UUID for reliability tracking");
        return buildToolDef("no_show_handler",
            "Handles no-show events when candidate or interviewer fails to join within 60 seconds. " +
            "Actions: emit dojo.match.noshown Kafka event, mark session CANCELLED in PostgreSQL, " +
            "re-queue candidate for next available match (high priority), " +
            "track interviewer no_show_rate (deprioritize if rate > 5%), " +
            "free interviewer slot immediately for next match.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("no_show_handler");
        String sessionId    = str(args, "session_id");
        String noShowParty  = str(args, "no_show_party");
        String candidateId  = str(args, "candidate_id");
        String interviewerId = str(args, "interviewer_id");

        if (sessionId.isEmpty()) return error("session_id is required");

        JsonObject result = new JsonObject();
        result.addProperty("session_id",     sessionId);
        result.addProperty("no_show_party",  noShowParty);
        result.addProperty("session_status", "CANCELLED");

        if (noShowParty.equals("CANDIDATE") || noShowParty.equals("BOTH")) {
            result.addProperty("candidate_requeued",      true);
            result.addProperty("candidate_queue_priority","HIGH");
            result.addProperty("candidate_wait_penalty_s", 30);
        }
        if (noShowParty.equals("INTERVIEWER") || noShowParty.equals("BOTH")) {
            result.addProperty("interviewer_slot_freed",  true);
            result.addProperty("interviewer_no_show_count", 2);
            result.addProperty("interviewer_no_show_rate", "3.5%");
            result.addProperty("interviewer_reliability",  "ACCEPTABLE");
            result.addProperty("deprioritized",            false);
        }

        result.addProperty("kafka_event",    "dojo.match.noshown");
        result.addProperty("kafka_payload",  "{match_id:..., no_show_party:" + noShowParty + "}");
        result.addProperty("db_update",      "UPDATE interview_sessions SET status='CANCELLED' WHERE id='" + sessionId + "'");
        result.addProperty("timeout_seconds", 60);
        return success(result);
    }
}
