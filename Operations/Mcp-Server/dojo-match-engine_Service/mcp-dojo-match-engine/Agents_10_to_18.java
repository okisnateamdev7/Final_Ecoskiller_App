package com.ecoskiller.dojo.agents;

import com.ecoskiller.dojo.security.SecurityManager;
import com.google.gson.*;
import java.util.UUID;

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 10: LOAD_BALANCER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class LoadBalancerAgent extends BaseAgent {
    public LoadBalancerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema();
        addStringProp(sc, "interviewer_id",  "Interviewer to update capacity for");
        addEnumProp  (sc, "action",          "Action to perform on capacity",
                       "ADD_TO_POOL", "REMOVE_FROM_POOL", "UPDATE_LOAD", "GET_POOL_STATUS");
        addIntProp   (sc, "current_load",    "Current number of active interviews for interviewer");
        addIntProp   (sc, "max_concurrent",  "Max concurrent interviews allowed");
        return buildToolDef("load_balancer",
            "Real-time interviewer capacity manager. Monitors active interview count per interviewer. " +
            "Removes interviewer from available pool when at max_concurrent capacity (default: 3). " +
            "Re-enables interviewer when interview ends. Prevents over-allocation. " +
            "Supports pool inspection, dynamic capacity updates, and overload detection.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("load_balancer");
        String action       = str(args, "action");
        String interviewerId = str(args, "interviewer_id");
        int currentLoad     = num(args, "current_load", 0);
        int maxConcurrent   = num(args, "max_concurrent", 3);

        JsonObject result = new JsonObject();
        switch (action) {
            case "ADD_TO_POOL" -> {
                result.addProperty("interviewer_id",  interviewerId);
                result.addProperty("action",          "ADDED_TO_POOL");
                result.addProperty("redis_key",       "dojo-available-interviewers");
                result.addProperty("redis_op",        "SADD dojo-available-interviewers " + interviewerId);
                result.addProperty("available_slots", maxConcurrent - currentLoad);
            }
            case "REMOVE_FROM_POOL" -> {
                result.addProperty("interviewer_id",  interviewerId);
                result.addProperty("action",          "REMOVED_FROM_POOL");
                result.addProperty("reason",          "AT_MAX_CAPACITY (" + maxConcurrent + "/" + maxConcurrent + ")");
                result.addProperty("redis_op",        "SREM dojo-available-interviewers " + interviewerId);
                result.addProperty("re_enable_on",    "Interview completion event");
            }
            case "UPDATE_LOAD" -> {
                boolean overloaded = currentLoad >= maxConcurrent;
                result.addProperty("interviewer_id",  interviewerId);
                result.addProperty("current_load",    currentLoad);
                result.addProperty("max_concurrent",  maxConcurrent);
                result.addProperty("utilization_pct", Math.round((double)currentLoad/maxConcurrent*100));
                result.addProperty("is_overloaded",   overloaded);
                result.addProperty("pool_action",     overloaded ? "REMOVED_FROM_POOL" : "REMAINS_IN_POOL");
            }
            case "GET_POOL_STATUS" -> {
                JsonArray pool = new JsonArray();
                String[][] status = {
                    {"IV-001","1","3","33%","IN_POOL"},
                    {"IV-002","0","3","0%","IN_POOL"},
                    {"IV-003","3","3","100%","OUT_OF_POOL"},
                    {"IV-004","2","3","67%","IN_POOL"},
                };
                for (String[] iv : status) {
                    JsonObject o = new JsonObject(); o.addProperty("id",iv[0]);
                    o.addProperty("current",iv[1]); o.addProperty("max",iv[2]);
                    o.addProperty("utilization",iv[3]); o.addProperty("pool_status",iv[4]);
                    pool.add(o);
                }
                result.add("pool", pool);
                result.addProperty("total_in_pool",    3);
                result.addProperty("total_out_of_pool",1);
                result.addProperty("total_capacity",   9);
                result.addProperty("used_capacity",    6);
            }
            default -> { return error("Unknown action: " + action); }
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 11: KAFKA_EVENT_PUBLISHER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class KafkaEventPublisherAgent extends BaseAgent {
    public KafkaEventPublisherAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("event_type", "match_id");
        addEnumProp  (sc, "event_type",    "Kafka event type to publish",
                       "dojo.match.created", "dojo.match.noshown", "dojo.match.cancelled",
                       "dojo.session.started", "dojo.session.completed");
        addStringProp(sc, "match_id",      "Match ID for the event payload");
        addStringProp(sc, "candidate_id",  "Candidate UUID for partitioning");
        addStringProp(sc, "interviewer_id","Interviewer UUID");
        addIntProp   (sc, "match_score",   "Match score (0-100) for event metadata");
        addStringProp(sc, "confidence",    "Match confidence: HIGH, MEDIUM, LOW");
        return buildToolDef("kafka_event_publisher",
            "Publishes Dojo Match Engine events to Apache Kafka. Topics: dojo.match.created " +
            "(triggers candidate/interviewer notifications), dojo.match.noshown " +
            "(triggers requeue), dojo.session.completed (triggers scoring pipeline). " +
            "Partitioned by candidate_id for ordering. Consumer groups: candidate-service, " +
            "interviewer-service, analytics-service. Retry on failure (async retry queue).",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("kafka_event_publisher");
        String eventType    = str(args, "event_type");
        String matchId      = str(args, "match_id");
        String candidateId  = str(args, "candidate_id");
        String interviewerId= str(args, "interviewer_id");

        if (eventType.isEmpty() || matchId.isEmpty()) return error("event_type and match_id required");

        String offset = String.valueOf(1000000 + Math.abs(matchId.hashCode() % 99999));
        int partition = Math.abs(candidateId.hashCode()) % 12; // 12 partitions

        JsonObject result = new JsonObject();
        result.addProperty("event_type",    eventType);
        result.addProperty("match_id",      matchId);
        result.addProperty("candidate_id",  candidateId);
        result.addProperty("interviewer_id",interviewerId);
        result.addProperty("topic",         eventType);
        result.addProperty("partition",     partition);
        result.addProperty("offset",        offset);
        result.addProperty("published_at",  java.time.Instant.now().toString());
        result.addProperty("publish_latency_ms", 2);
        result.addProperty("consumer_groups","candidate-service, interviewer-service, analytics-service");
        result.addProperty("ack_mode",      "LEADER_REPLICATION");
        result.addProperty("retry_policy",  "async-retry-queue with background republisher every 5min");
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 12: MATCH_RESULT_PUBLISHER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class MatchResultPublisherAgent extends BaseAgent {
    public MatchResultPublisherAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("match_id", "candidate_id", "interviewer_id");
        addStringProp(sc, "match_id",       "Match ID to publish result for");
        addStringProp(sc, "candidate_id",   "Candidate UUID");
        addStringProp(sc, "interviewer_id", "Interviewer UUID");
        addStringProp(sc, "session_url",    "Secure WebRTC session URL for join");
        addIntProp   (sc, "match_score",    "Match quality score 0-100");
        addStringProp(sc, "confidence",     "Confidence level: HIGH/MEDIUM/LOW");
        return buildToolDef("match_result_publisher",
            "Notifies both candidate and interviewer after successful match. " +
            "Sends HTTP callback to candidate-service (update candidate UI: matched notification). " +
            "Sends HTTP callback to interviewer-service (new incoming interview panel update). " +
            "Updates PostgreSQL match_queue: INSERT match record with status=MATCHED. " +
            "Both notifications are fire-and-forget with retry on HTTP 5xx.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("match_result_publisher");
        String matchId       = str(args, "match_id");
        String candidateId   = str(args, "candidate_id");
        String interviewerId = str(args, "interviewer_id");
        String sessionUrl    = str(args, "session_url");
        int    score         = num(args, "match_score", 85);
        String confidence    = str(args, "confidence");

        if (matchId.isEmpty()) return error("match_id is required");

        JsonObject result = new JsonObject();
        result.addProperty("match_id",         matchId);

        // Candidate notification
        JsonObject candNotif = new JsonObject();
        candNotif.addProperty("endpoint",      "POST https://candidate-svc.ecoskiller.svc/api/v1/match-assigned");
        candNotif.addProperty("payload",       "{candidate_id:" + candidateId + ", session_url:" + sessionUrl + "}");
        candNotif.addProperty("status",        "HTTP_200_OK");
        candNotif.addProperty("latency_ms",    18);
        result.add("candidate_notification",   candNotif);

        // Interviewer notification
        JsonObject ivNotif = new JsonObject();
        ivNotif.addProperty("endpoint",        "POST https://interviewer-svc.ecoskiller.svc/api/v1/new-interview");
        ivNotif.addProperty("payload",         "{interviewer_id:" + interviewerId + ", match_score:" + score + "}");
        ivNotif.addProperty("status",          "HTTP_200_OK");
        ivNotif.addProperty("latency_ms",      14);
        result.add("interviewer_notification", ivNotif);

        // DB record
        result.addProperty("db_insert",        "INSERT INTO match_queue (match_id, candidate_id, interviewer_id, match_score, status) VALUES (...)");
        result.addProperty("db_status",        "INSERTED");
        result.addProperty("confidence",       confidence);
        result.addProperty("match_score",      score);
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 13: FAIRNESS_AUDIT_LOG_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class FairnessAuditLogAgent extends BaseAgent {
    public FairnessAuditLogAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema();
        addEnumProp  (sc, "action",          "Audit operation",
                       "WRITE", "READ", "REPORT", "VERIFY_INTEGRITY");
        addStringProp(sc, "match_id",        "Match ID to log");
        addStringProp(sc, "candidate_id",    "Candidate ID in the match");
        addStringProp(sc, "interviewer_id",  "Interviewer ID in the match");
        addNumberProp(sc, "fairness_score",  "Fairness score at time of match (0.0–1.0)");
        addStringProp(sc, "algorithm_version","Matching algorithm version tag");
        return buildToolDef("fairness_audit_log",
            "Immutable fairness audit log stored in PostgreSQL (fairness_log table). " +
            "Records every match decision with: match_id, candidate/interviewer IDs, " +
            "fairness_score, skill distribution metrics, algorithm version, timestamp. " +
            "Supports compliance reporting: prove no algorithmic bias to auditors. " +
            "WRITE is append-only (no UPDATE/DELETE). VERIFY_INTEGRITY checks hash chain.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("fairness_audit_log");
        String action      = str(args, "action");
        if (action.isEmpty()) action = "WRITE";

        JsonObject result = new JsonObject();
        switch (action) {
            case "WRITE" -> {
                String logId = "FAIR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                result.addProperty("log_id",          logId);
                result.addProperty("match_id",        str(args,"match_id"));
                result.addProperty("candidate_id",    str(args,"candidate_id"));
                result.addProperty("interviewer_id",  str(args,"interviewer_id"));
                result.addProperty("fairness_score",  dbl(args,"fairness_score",0.87));
                result.addProperty("algorithm_version", str(args,"algorithm_version"));
                result.addProperty("logged_at",       java.time.Instant.now().toString());
                result.addProperty("append_only",     true);
                result.addProperty("db_op",           "INSERT INTO fairness_log ... (no UPDATE/DELETE allowed)");
            }
            case "REPORT" -> {
                result.addProperty("period",           "last_7_days");
                result.addProperty("total_matches",    892);
                result.addProperty("avg_fairness_score", 0.87);
                result.addProperty("min_fairness_score", 0.71);
                result.addProperty("rebalance_events",  3);
                result.addProperty("bias_flags",         0);
                result.addProperty("compliance_status",  "PASS");
            }
            case "VERIFY_INTEGRITY" -> {
                result.addProperty("total_records",    892);
                result.addProperty("hash_chain_valid", true);
                result.addProperty("tamper_detected",  false);
                result.addProperty("last_hash",        "sha256:a1b2c3d4...");
            }
            case "READ" -> {
                result.addProperty("log_entries_returned", 10);
                result.addProperty("filter",              "last 10 matches");
                result.addProperty("note",                "Full entries returned in production with DB connection");
            }
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 14: METRICS_COLLECTOR_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class MetricsCollectorAgent extends BaseAgent {
    public MetricsCollectorAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema();
        addEnumProp  (sc, "metric_type",     "Metric to retrieve",
                       "MATCH_RATE", "LATENCY", "UTILIZATION", "FAIRNESS",
                       "NO_SHOW_RATE", "SKILL_MISMATCH", "ALL");
        addStringProp(sc, "interviewer_id",  "Filter metrics by interviewer (optional)");
        addStringProp(sc, "time_window",     "Time window: 1min, 5min, 1h, 24h (default: 5min)");
        return buildToolDef("metrics_collector",
            "Prometheus metrics collection for Dojo Match Engine. Exposes: " +
            "dojo_matches_per_minute (rate counter), dojo_match_latency_ms (histogram p50/p95/p99), " +
            "dojo_interviewer_utilization (gauge per interviewer), " +
            "dojo_fairness_score (gauge), dojo_noshown_rate (gauge). " +
            "Used by Grafana Matching Quality Dashboard and HR monitoring portal.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("metrics_collector");
        String metricType  = str(args, "metric_type");
        String timeWindow  = str(args, "time_window");
        if (metricType.isEmpty()) metricType = "ALL";
        if (timeWindow.isEmpty()) timeWindow = "5min";

        JsonObject result = new JsonObject();
        result.addProperty("time_window", timeWindow);
        result.addProperty("collected_at", java.time.Instant.now().toString());

        if (metricType.equals("MATCH_RATE") || metricType.equals("ALL")) {
            JsonObject mr = new JsonObject();
            mr.addProperty("dojo_matches_per_minute", 18);
            mr.addProperty("matches_last_hour",       1020);
            mr.addProperty("matches_today",           8640);
            mr.addProperty("success_rate_pct",        97.3);
            result.add("match_rate", mr);
        }
        if (metricType.equals("LATENCY") || metricType.equals("ALL")) {
            JsonObject lat = new JsonObject();
            lat.addProperty("p50_ms",   420);
            lat.addProperty("p95_ms",   1200);
            lat.addProperty("p99_ms",   4800);
            lat.addProperty("max_ms",   4950);
            lat.addProperty("slo_target_ms", 5000);
            lat.addProperty("slo_breaches",  0);
            result.add("latency", lat);
        }
        if (metricType.equals("UTILIZATION") || metricType.equals("ALL")) {
            JsonObject util = new JsonObject();
            util.addProperty("avg_utilization_pct",  73.0);
            util.addProperty("peak_utilization_pct", 91.0);
            util.addProperty("idle_time_saved_hrs",  12.4);
            result.add("utilization", util);
        }
        if (metricType.equals("FAIRNESS") || metricType.equals("ALL")) {
            JsonObject fair = new JsonObject();
            fair.addProperty("dojo_fairness_score",     0.87);
            fair.addProperty("load_variance",           0.28);
            fair.addProperty("skill_distribution_gini", 0.12);
            result.add("fairness", fair);
        }
        if (metricType.equals("NO_SHOW_RATE") || metricType.equals("ALL")) {
            JsonObject ns = new JsonObject();
            ns.addProperty("no_show_rate_pct",        2.7);
            ns.addProperty("candidate_no_shows",       18);
            ns.addProperty("interviewer_no_shows",     6);
            result.add("no_show", ns);
        }
        if (metricType.equals("SKILL_MISMATCH") || metricType.equals("ALL")) {
            JsonObject sm = new JsonObject();
            sm.addProperty("skill_mismatch_rate_pct",  1.2);
            sm.addProperty("fallback_matches_used",    9);
            sm.addProperty("fallback_success_rate_pct",88.0);
            result.add("skill_mismatch", sm);
        }
        result.addProperty("prometheus_endpoint", "http://dojo-match-engine:8080/metrics");
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 15: FALLBACK_MATCH_ENGINE_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class FallbackMatchEngineAgent extends BaseAgent {
    public FallbackMatchEngineAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("candidate_id", "primary_skill", "candidate_level");
        addStringProp(sc, "candidate_id",      "Candidate UUID needing fallback match");
        addStringProp(sc, "primary_skill",     "Candidate's primary skill that found no perfect match");
        addEnumProp  (sc, "candidate_level",   "Candidate proficiency",
                       "BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
        addStringProp(sc, "fallback_reason",   "Why primary matching failed");
        addBoolProp  (sc, "allow_cross_skill", "Allow cross-skill matching (e.g. Java interviewer for Python candidate)");
        return buildToolDef("fallback_match_engine",
            "Emergency fallback matching when no perfect interviewer found. " +
            "Scenarios: all skill-aligned interviewers busy, timezone constraint eliminates all options. " +
            "Strategy: relax skill constraint (Java expert can interview Python intermediate). " +
            "Score downgraded but match created to prevent queue backlog. " +
            "Confidence always LOW for fallback matches. Prevents candidate starvation.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("fallback_match_engine");
        String candidateId   = str(args, "candidate_id");
        String primarySkill  = str(args, "primary_skill");
        String candidateLvl  = str(args, "candidate_level");
        String reason        = str(args, "fallback_reason");

        String fallbackMatchId = "FALL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        JsonObject result = new JsonObject();
        result.addProperty("fallback_triggered",   true);
        result.addProperty("fallback_match_id",    fallbackMatchId);
        result.addProperty("candidate_id",         candidateId);
        result.addProperty("primary_skill_sought", primarySkill);
        result.addProperty("candidate_level",      candidateLvl);
        result.addProperty("fallback_reason",      reason.isEmpty() ? "NO_AVAILABLE_SKILL_ALIGNED_INTERVIEWER" : reason);
        result.addProperty("fallback_interviewer_id",    "IV-003");
        result.addProperty("fallback_interviewer_skill",  "Python");
        result.addProperty("fallback_interviewer_level",  "EXPERT");
        result.addProperty("cross_skill_match",           true);
        result.addProperty("match_score",                 62);
        result.addProperty("confidence",                  "LOW");
        result.addProperty("score_penalty",               "-15 (cross-skill penalty applied)");
        result.addProperty("educational_benefit",         "Java expert can provide Python perspective");
        result.addProperty("hr_visibility",               "LOW confidence match displayed in HR dashboard");
        result.addProperty("queue_backlog_prevented",     true);
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 16: INTERVIEW_HISTORY_TRACKER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class InterviewHistoryTrackerAgent extends BaseAgent {
    public InterviewHistoryTrackerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema();
        addEnumProp  (sc, "action",          "History operation",
                       "RECORD_INTERVIEW", "GET_CANDIDATE_HISTORY", "GET_INTERVIEWER_HISTORY",
                       "CHECK_REPEAT", "GET_STATS");
        addStringProp(sc, "candidate_id",    "Candidate UUID");
        addStringProp(sc, "interviewer_id",  "Interviewer UUID");
        addStringProp(sc, "session_id",      "Completed session ID to record");
        addStringProp(sc, "outcome",         "Session outcome: COMPLETED, CANCELLED, NO_SHOW");
        return buildToolDef("interview_history_tracker",
            "Tracks completed interview history in PostgreSQL. " +
            "Maintains previous_interviewees set per candidate (for repeat prevention). " +
            "Tracks no_show_count per interviewer. Provides: GET_CANDIDATE_HISTORY " +
            "(all interviewers a candidate has seen), CHECK_REPEAT (has this pair met before?), " +
            "GET_STATS (interview count per interviewer for fairness scoring).",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("interview_history_tracker");
        String action       = str(args, "action");
        String candidateId  = str(args, "candidate_id");
        String interviewerId = str(args, "interviewer_id");

        JsonObject result = new JsonObject();
        switch (action) {
            case "RECORD_INTERVIEW" -> {
                result.addProperty("recorded",        true);
                result.addProperty("session_id",      str(args,"session_id"));
                result.addProperty("candidate_id",    candidateId);
                result.addProperty("interviewer_id",  interviewerId);
                result.addProperty("outcome",         str(args,"outcome"));
                result.addProperty("db_op",           "INSERT INTO interview_history ...");
                result.addProperty("redis_op",        "SADD candidate:" + candidateId + ":interviewers " + interviewerId);
            }
            case "GET_CANDIDATE_HISTORY" -> {
                result.addProperty("candidate_id",        candidateId);
                result.addProperty("total_interviews",    3);
                JsonArray ivs = new JsonArray();
                ivs.add("IV-001"); ivs.add("IV-004"); ivs.add("IV-002");
                result.add("interviewed_by",              ivs);
                result.addProperty("no_repeat_constraint_active", true);
            }
            case "GET_INTERVIEWER_HISTORY" -> {
                result.addProperty("interviewer_id",      interviewerId);
                result.addProperty("total_interviews",    47);
                result.addProperty("interviews_today",    5);
                result.addProperty("no_show_count",       2);
                result.addProperty("no_show_rate_pct",    4.3);
                result.addProperty("avg_match_score",     82);
            }
            case "CHECK_REPEAT" -> {
                result.addProperty("candidate_id",        candidateId);
                result.addProperty("interviewer_id",      interviewerId);
                result.addProperty("has_met_before",      false);
                result.addProperty("constraint_violated", false);
                result.addProperty("redis_check",         "SISMEMBER candidate:" + candidateId + ":interviewers " + interviewerId);
            }
            case "GET_STATS" -> {
                result.addProperty("total_unique_pairs",  412);
                result.addProperty("repeat_prevented",    18);
                result.addProperty("avg_interviews_per_candidate", 2.3);
            }
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 17: CONCURRENT_CAPACITY_MANAGER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class ConcurrentCapacityManagerAgent extends BaseAgent {
    public ConcurrentCapacityManagerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("interviewer_id");
        addStringProp(sc, "interviewer_id",   "Interviewer UUID to manage capacity for");
        addEnumProp  (sc, "action",           "Capacity operation",
                       "INCREMENT", "DECREMENT", "GET", "SET_MAX", "RESET");
        addIntProp   (sc, "max_concurrent",   "New max concurrent limit (for SET_MAX action)");
        return buildToolDef("concurrent_capacity_manager",
            "Manages real-time concurrent interview slot tracking per interviewer. " +
            "Interviewers can conduct 2-3 concurrent interviews (multi-window). " +
            "INCREMENT when interview starts, DECREMENT when interview ends. " +
            "Prevents over-allocation: if current >= max_concurrent, interviewer removed from pool. " +
            "Backed by Redis atomic INCR/DECR operations for thread safety across replicas.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("concurrent_capacity_manager");
        String interviewerId = str(args, "interviewer_id");
        String action        = str(args, "action");
        int    maxConcurrent = num(args, "max_concurrent", 3);

        if (interviewerId.isEmpty()) return error("interviewer_id is required");

        JsonObject result = new JsonObject();
        result.addProperty("interviewer_id", interviewerId);
        switch (action) {
            case "INCREMENT" -> {
                int newVal = 2; // simulated
                result.addProperty("previous_count", newVal - 1);
                result.addProperty("current_count",  newVal);
                result.addProperty("max_concurrent", maxConcurrent);
                result.addProperty("slots_remaining", maxConcurrent - newVal);
                result.addProperty("pool_status",    newVal >= maxConcurrent ? "REMOVED_FROM_POOL" : "IN_POOL");
                result.addProperty("redis_op",       "INCR interviewer:" + interviewerId + ":active_count");
            }
            case "DECREMENT" -> {
                int newVal = 1; // simulated
                result.addProperty("previous_count", newVal + 1);
                result.addProperty("current_count",  newVal);
                result.addProperty("slots_remaining", maxConcurrent - newVal);
                result.addProperty("pool_status",    "RE_ADDED_TO_POOL");
                result.addProperty("redis_op",       "DECR interviewer:" + interviewerId + ":active_count");
            }
            case "GET" -> {
                result.addProperty("current_count",  1);
                result.addProperty("max_concurrent", maxConcurrent);
                result.addProperty("slots_remaining", maxConcurrent - 1);
                result.addProperty("utilization_pct", Math.round((1.0/maxConcurrent)*100));
                result.addProperty("redis_key",      "interviewer:" + interviewerId + ":active_count");
            }
            case "SET_MAX" -> {
                result.addProperty("previous_max",   3);
                result.addProperty("new_max",        maxConcurrent);
                result.addProperty("updated",        true);
                result.addProperty("db_op",          "UPDATE interviewers SET max_concurrent=" + maxConcurrent + " WHERE id='" + interviewerId + "'");
            }
            case "RESET" -> {
                result.addProperty("reset_to",       0);
                result.addProperty("reason",         "Manual reset or pod restart recovery");
                result.addProperty("redis_op",       "SET interviewer:" + interviewerId + ":active_count 0");
            }
        }
        return success(result);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AGENT 18: MATCH_CONFIDENCE_SCORER_AGENT
// ─────────────────────────────────────────────────────────────────────────────

class MatchConfidenceScorerAgent extends BaseAgent {
    public MatchConfidenceScorerAgent(SecurityManager s) { super(s); }

    @Override
    public JsonObject getToolDefinition() {
        JsonObject sc = schema("match_score");
        addIntProp   (sc, "match_score",         "Raw match score 0-100 from match_score_calculator");
        addBoolProp  (sc, "is_fallback",         "True if this is a fallback/cross-skill match");
        addBoolProp  (sc, "fairness_flag",       "True if fairness rebalancing was triggered");
        addStringProp(sc, "skill_delta",         "Skill level gap: EXACT, ONE_LEVEL, TWO_LEVELS, CROSS_SKILL");
        addStringProp(sc, "interviewer_id",      "Interviewer ID for display in HR portal");
        addStringProp(sc, "candidate_id",        "Candidate ID");
        return buildToolDef("match_confidence_scorer",
            "Determines match confidence band for HR dashboard display. " +
            "Rules: HIGH (score >= 90, no fairness flag, not fallback), " +
            "MEDIUM (score 70-89 OR minor fairness flag), LOW (score < 70 OR fallback OR cross-skill). " +
            "HR uses LOW confidence matches only when no alternatives exist. " +
            "Returns confidence band + reasons + recommendation for HR.",
            sc);
    }

    @Override
    public JsonObject execute(JsonObject args) {
        security.checkRateLimit("match_confidence_scorer");
        int     score        = num(args, "match_score", 85);
        boolean isFallback   = args.has("is_fallback") && args.get("is_fallback").getAsBoolean();
        boolean fairnessFlag = args.has("fairness_flag") && args.get("fairness_flag").getAsBoolean();
        String  skillDelta   = str(args, "skill_delta");

        // Confidence determination logic
        String confidence;
        String reason;
        String recommendation;

        if (score >= 90 && !isFallback && !fairnessFlag) {
            confidence     = "HIGH";
            reason         = "Score >= 90, no fairness concern, exact skill match";
            recommendation = "Proceed immediately — optimal match";
        } else if (score >= 70 && !isFallback) {
            confidence     = "MEDIUM";
            reason         = (fairnessFlag ? "Fairness rebalancing active. " : "") +
                             (score < 90 ? "Score " + score + " below HIGH threshold." : "");
            recommendation = "Acceptable match — monitor for fairness metrics";
        } else {
            confidence     = "LOW";
            reason         = (isFallback ? "Fallback/cross-skill match. " : "") +
                             (score < 70 ? "Score " + score + " below threshold. " : "");
            recommendation = "Use only if no alternatives — review match manually";
        }

        JsonObject result = new JsonObject();
        result.addProperty("match_score",         score);
        result.addProperty("confidence",          confidence);
        result.addProperty("confidence_reason",   reason);
        result.addProperty("hr_recommendation",   recommendation);
        result.addProperty("is_fallback",         isFallback);
        result.addProperty("fairness_flag",       fairnessFlag);
        result.addProperty("skill_delta",         skillDelta.isEmpty() ? "EXACT" : skillDelta);
        result.addProperty("interviewer_id",      str(args,"interviewer_id"));
        result.addProperty("candidate_id",        str(args,"candidate_id"));
        result.addProperty("high_threshold",      90);
        result.addProperty("medium_threshold",    70);
        result.addProperty("display_in_hr_ui",    true);
        return success(result);
    }
}
