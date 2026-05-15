package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.AssessmentOrchestratorMCPServer;
import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: get_session_audit_log, get_speaking_time_stats, get_service_health, configure_track_plan */
public class AuditAnalyticsTools {

    private final OrchestratorRepository repo = OrchestratorRepository.getInstance();

    // Track plan store (PostgreSQL ecoskiller.assessment_cycle_config in prod)
    private static final Map<String, Map<String, Object>> TRACK_PLANS = new LinkedHashMap<>();

    // ─────────────────────────────────────────────────────────────────────────
    // get_session_audit_log  (ClickHouse ecoskiller.session_audit_log)
    // ─────────────────────────────────────────────────────────────────────────

    public String getSessionAuditLog(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_MODERATOR,
                SecurityConfig.ROLE_ADMIN);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId = SecurityConfig.requireString(args, "session_id");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        List<Map<String, Object>> clickhouseRecords = repo.getAuditForSession(sessionId);

        // If no ClickHouse record yet (session still active), return live telemetry
        Map<String, Object> telemetry = clickhouseRecords.isEmpty()
                ? session.toTelemetryMap()
                : clickhouseRecords.get(clickhouseRecords.size() - 1);

        // Compute summary statistics
        Map<String, Long> speakingTime = session.getSpeakingTimeMap();
        OptionalDouble avgSpeakingTime = speakingTime.values().stream().mapToLong(Long::longValue).average();
        long maxSpeaker = speakingTime.values().stream().mapToLong(Long::longValue).max().orElse(0);
        long minSpeaker = speakingTime.values().stream().mapToLong(Long::longValue).min().orElse(0);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total_state_transitions", session.getStateTransitions().size());
        summary.put("participant_count",        session.getParticipantCount());
        summary.put("drop_event_count",         telemetry.getOrDefault("drop_events", List.of()) instanceof List ?
                ((List<?>) telemetry.getOrDefault("drop_events", List.of())).size() : 0);
        summary.put("avg_speaking_time_sec",    avgSpeakingTime.isPresent() ? Math.round(avgSpeakingTime.getAsDouble()) : 0);
        summary.put("max_speaking_time_sec",    maxSpeaker);
        summary.put("min_speaking_time_sec",    minSpeaker);
        summary.put("speaking_time_fairness",   maxSpeaker > 0 ? String.format("%.1f%%", 100.0 * minSpeaker / maxSpeaker) : "N/A");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id",       sessionId);
        data.put("track_type",       session.getTrackType().name());
        data.put("tenant_id",        tenantId);
        data.put("telemetry",        telemetry);
        data.put("summary",          summary);
        data.put("clickhouse_table", "ecoskiller.session_audit_log");
        data.put("compliance_note",  "Retained 3 years per DPDPA 2023. Supports candidate audit challenge response.");

        return resp("OK", "Session audit log retrieved", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // get_speaking_time_stats  (ClickHouse ecoskiller.gd_speaking_time_distributions)
    // ─────────────────────────────────────────────────────────────────────────

    public String getSpeakingTimeStats(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_MODERATOR,
                SecurityConfig.ROLE_ADMIN);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId = SecurityConfig.requireString(args, "session_id");

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        if (session.getTrackType() != AssessmentCycle.TrackType.GD)
            throw new IllegalArgumentException("Speaking time stats only available for GD sessions (track_type=GD)");

        Map<String, Long> speakingTime = session.getSpeakingTimeMap();
        long totalSpeakingTime = speakingTime.values().stream().mapToLong(Long::longValue).sum();

        // Fairness analysis
        int candidateCount = session.getCandidateIds().size();
        double idealShare  = candidateCount > 0 ? 100.0 / candidateCount : 0;

        List<Map<String, Object>> distribution = new ArrayList<>();
        for (String cid : session.getCandidateIds()) {
            long secs  = speakingTime.getOrDefault(cid, 0L);
            double pct = totalSpeakingTime > 0 ? (secs * 100.0 / totalSpeakingTime) : 0;
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("candidate_id",    cid);
            entry.put("speaking_seconds",secs);
            entry.put("speaking_pct",    String.format("%.1f%%", pct));
            entry.put("vs_ideal",        String.format("%+.1f%%", pct - idealShare));
            entry.put("turns_taken",     session.getStateTransitions().stream()
                    .filter(t -> ("SPEAKING:" + cid).equals(t.get("to"))).count());
            distribution.add(entry);
        }

        // Sort by speaking time descending
        distribution.sort((a, b) -> Long.compare(
                (Long) b.get("speaking_seconds"), (Long) a.get("speaking_seconds")));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id",            sessionId);
        data.put("track_type",            "GD");
        data.put("total_speaking_seconds",totalSpeakingTime);
        data.put("candidate_count",       candidateCount);
        data.put("ideal_share_pct",       String.format("%.1f%%", idealShare));
        data.put("distribution",          distribution);
        data.put("fairness_score",        computeFairnessScore(distribution, idealShare));
        data.put("clickhouse_table",      "ecoskiller.gd_speaking_time_distributions");

        return resp("OK", "Speaking time distribution retrieved", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // get_service_health  (mirrors GET /health)
    // ─────────────────────────────────────────────────────────────────────────

    public String getServiceHealth(Map<String, Object> args) {
        String tenantId = "global";
        try {
            JwtClaims claims = SecurityConfig.validateToken(SecurityConfig.getString(args, "auth_token"));
            tenantId = claims.getTenantId();
        } catch (Exception ignored) { /* allow unauthenticated for k8s probe */ }

        Map<String, Object> infra = new LinkedHashMap<>();
        infra.put("redis",   Map.of("status", "ok", "note", "state machine store — production: Redis 7.x"));
        infra.put("kafka",   Map.of("status", "ok", "note", "event bus — production: Kafka 3.7.0"));
        infra.put("postgres",Map.of("status", "ok", "note", "persistent store — production: PostgreSQL 15"));
        infra.put("keycloak",Map.of("status", "ok", "note", "JWT validation — production: Keycloak 24.0"));
        infra.put("clickhouse",Map.of("status", "ok","note", "audit log — production: ClickHouse latest"));

        Map<String, Object> metrics = repo.getMetrics(tenantId);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status",              "ok");
        data.put("server",              AssessmentOrchestratorMCPServer.SERVER_NAME);
        data.put("version",             AssessmentOrchestratorMCPServer.SERVER_VERSION);
        data.put("namespace",           "realtime");
        data.put("infrastructure",      infra);
        data.put("metrics",             metrics);
        data.put("gd_phase_config",     Map.of(
                "intro_sec",            System.getenv().getOrDefault("GD_INTRO_DURATION_SECONDS", "60"),
                "speaking_sec",         System.getenv().getOrDefault("GD_SPEAKING_DURATION_SECONDS", "90"),
                "open_discussion_sec",  System.getenv().getOrDefault("GD_OPEN_DISCUSSION_DURATION_SECONDS", "300"),
                "closing_sec",          System.getenv().getOrDefault("GD_CLOSING_DURATION_SECONDS", "120")));
        data.put("checked_at",          Instant.now().toString());

        return resp("OK", "Service healthy", data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // configure_track_plan  (ecoskiller.assessment_cycle_config)
    // ─────────────────────────────────────────────────────────────────────────

    public String configureTrackPlan(Map<String, Object> args) {
        JwtClaims claims  = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_ADMIN);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String roleType   = SecurityConfig.requireString(args, "role_type").toUpperCase();
        String tracksRaw  = SecurityConfig.requireString(args, "tracks");
        String gdSpeakStr = SecurityConfig.getString(args, "gd_speaking_seconds");
        String gdOpenStr  = SecurityConfig.getString(args, "gd_open_discussion_seconds");
        String quorumStr  = SecurityConfig.getString(args, "quorum_required");

        // Validate tracks
        List<String> tracks = new ArrayList<>();
        for (String t : tracksRaw.split(",")) {
            String u = t.trim().toUpperCase();
            if (!u.equals("GD") && !u.equals("INTERVIEW") && !u.equals("DOJO"))
                throw new IllegalArgumentException("Invalid track: " + t.trim() + ". Valid: GD, INTERVIEW, DOJO");
            tracks.add(u);
        }
        if (tracks.isEmpty()) throw new IllegalArgumentException("tracks must not be empty");

        // Build config
        Map<String, Object> plan = new LinkedHashMap<>();
        plan.put("tenant_id",                tenantId);
        plan.put("role_type",                roleType);
        plan.put("tracks",                   tracks);
        plan.put("gd_speaking_seconds",      gdSpeakStr != null ? Integer.parseInt(gdSpeakStr) : 90);
        plan.put("gd_open_discussion_seconds",gdOpenStr != null ? Integer.parseInt(gdOpenStr) : 300);
        plan.put("quorum_required",          quorumStr != null ? Integer.parseInt(quorumStr) : 3);
        plan.put("configured_by",            claims.getUserId());
        plan.put("configured_at",            Instant.now().toString());
        plan.put("redis_cache_ttl_sec",      600);
        plan.put("pg_table",                 "ecoskiller.assessment_cycle_config");

        // Store plan (Redis cache key: cycle_config:{tenantId}:{roleType} in prod)
        String planKey = tenantId + ":" + roleType;
        TRACK_PLANS.put(planKey, plan);

        return resp("OK", "Track execution plan configured for role: " + roleType, plan);
    }

    // ─ helpers ───────────────────────────────────────────────────────────────

    private String computeFairnessScore(List<Map<String, Object>> dist, double idealShare) {
        if (dist.isEmpty()) return "N/A";
        double totalDeviation = dist.stream()
                .mapToDouble(e -> {
                    String pctStr = (String) e.get("speaking_pct");
                    double pct = Double.parseDouble(pctStr.replace("%", ""));
                    return Math.abs(pct - idealShare);
                }).sum();
        double score = Math.max(0, 100 - totalDeviation);
        if (score >= 80) return String.format("%.0f/100 — EXCELLENT", score);
        if (score >= 60) return String.format("%.0f/100 — GOOD", score);
        if (score >= 40) return String.format("%.0f/100 — FAIR", score);
        return String.format("%.0f/100 — POOR (significant imbalance)", score);
    }

    private JwtClaims auth(Map<String, Object> args) {
        return SecurityConfig.validateToken(SecurityConfig.requireString(args, "auth_token"));
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "assessment-orchestrator");
        return JsonUtil.toJson(r);
    }
}
