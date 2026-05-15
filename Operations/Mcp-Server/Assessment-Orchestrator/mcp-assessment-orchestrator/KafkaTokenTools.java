package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/** Tools: emit_completion_event, validate_jitsi_token, get_kafka_event_log */
public class KafkaTokenTools {

    private final OrchestratorRepository repo  = OrchestratorRepository.getInstance();
    private final KafkaEventPublisher    kafka = KafkaEventPublisher.getInstance();

    // ─────────────────────────────────────────────────────────────────────────
    // emit_completion_event  (idempotent)
    // ─────────────────────────────────────────────────────────────────────────

    public String emitCompletionEvent(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_MODERATOR, SecurityConfig.ROLE_ADMIN,
                SecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String sessionId = SecurityConfig.requireString(args, "session_id");
        boolean force    = "true".equalsIgnoreCase(SecurityConfig.getString(args, "force"));

        if (force) SecurityConfig.requireRole(claims, SecurityConfig.ROLE_ADMIN);

        AssessmentSession session = repo.findSession(tenantId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        if (session.getState() != AssessmentSession.SessionState.COMPLETED)
            throw new IllegalStateException("Session must be COMPLETED to emit completion event (state: " + session.getState() + ")");

        // Idempotency guard (Redis SET NX EX 86400 in production)
        String idempKey = session.getIdempotencyKey();
        if (!force && session.isIdempotencyKeyWritten()) {
            return resp("DUPLICATE", "Completion event already emitted — idempotency key: " + idempKey,
                    Map.of("session_id", sessionId, "idempotency_key", idempKey, "action", "NO_OP"));
        }
        if (!force && !SecurityConfig.checkIdempotency(idempKey)) {
            return resp("DUPLICATE", "Duplicate completion event blocked by idempotency store",
                    Map.of("session_id", sessionId, "idempotency_key", idempKey, "action", "NO_OP"));
        }

        // Write idempotency key BEFORE emitting (critical ordering)
        session.markIdempotencyKeyWritten();
        repo.saveSession(session);

        // Emit topic-specific completion event
        String topic;
        switch (session.getTrackType()) {
            case GD:
                kafka.publishGdCompleted(session);
                topic = "gd.completed"; break;
            case INTERVIEW:
                kafka.publishInterviewCompleted(session);
                topic = "interview.completed"; break;
            case DOJO:
                // Dojo uses match.scored — log synthetic event
                Map<String, Object> dojoPayload = new LinkedHashMap<>();
                dojoPayload.put("event_type",      "match.scored");
                dojoPayload.put("session_id",      sessionId);
                dojoPayload.put("tenant_id",       tenantId);
                dojoPayload.put("candidate_ids",   session.getCandidateIds());
                dojoPayload.put("idempotency_key", idempKey);
                dojoPayload.put("completed_at",    Instant.now().toString());
                repo.logKafkaEvent("match.scored", dojoPayload);
                topic = "match.scored"; break;
            default:
                topic = "unknown";
        }

        // Archive to ClickHouse (simulated)
        repo.writeAudit(session);

        // Check if parent cycle is fully complete
        Optional<AssessmentCycle> cycleOpt = repo.findCycle(tenantId, session.getCycleId());
        cycleOpt.ifPresent(cycle -> {
            List<AssessmentSession> siblings = repo.findSessionsByCycle(tenantId, cycle.getCycleId());
            boolean allDone = siblings.stream().allMatch(s ->
                    s.getState() == AssessmentSession.SessionState.COMPLETED ||
                    s.getState() == AssessmentSession.SessionState.CANCELLED);
            if (allDone) {
                cycle.complete();
                repo.saveCycle(cycle);
                kafka.publishCycleCompleted(cycle, siblings.stream()
                        .filter(s -> s.getState() == AssessmentSession.SessionState.COMPLETED)
                        .map(s -> s.getTrackType().name()).collect(Collectors.toList()));
            }
        });

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id",      sessionId);
        data.put("topic_emitted",   topic);
        data.put("idempotency_key", idempKey);
        data.put("track_type",      session.getTrackType().name());
        data.put("candidate_count", session.getCandidateIds().size());
        data.put("clickhouse_archived", true);
        data.put("emitted_at",      Instant.now().toString());
        return resp("OK", "Completion event emitted to " + topic, data);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // validate_jitsi_token
    // ─────────────────────────────────────────────────────────────────────────

    public String validateJitsiToken(Map<String, Object> args) {
        auth(args); // require any valid Keycloak JWT
        String sessionId  = SecurityConfig.requireString(args, "session_id");
        String jitsiToken = SecurityConfig.requireString(args, "jitsi_token");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id", sessionId);
        data.put("token_prefix", jitsiToken.substring(0, Math.min(20, jitsiToken.length())) + "...");

        try {
            String[] parts = jitsiToken.split("\\.");
            if (parts.length != 3) throw new SecurityException("Malformed JWT: expected 3 parts");

            String payloadJson = SecurityConfig.decodeB64(parts[1]);

            // Extract claims
            String room    = extractStr(payloadJson, "room");
            String sub     = extractStr(payloadJson, "sub");
            long   exp     = extractLong(payloadJson, "exp");
            boolean isMod  = payloadJson.contains("\"moderator\":true");

            // Validate room matches session_id
            if (!sessionId.equals(room))
                throw new SecurityException("Room mismatch: token.room=" + room + " expected=" + sessionId);

            // Validate expiry
            long now = Instant.now().getEpochSecond();
            boolean expired = exp > 0 && now > exp;

            data.put("valid",           !expired);
            data.put("room",            room);
            data.put("sub_domain",      sub);
            data.put("expires_at",      exp > 0 ? Instant.ofEpochSecond(exp).toString() : "never");
            data.put("expired",         expired);
            data.put("is_moderator",    isMod);
            data.put("seconds_remaining", expired ? 0 : (exp - now));

            return resp(expired ? "EXPIRED" : "VALID",
                    expired ? "Jitsi token has expired" : "Jitsi token is valid",
                    data);

        } catch (SecurityException e) {
            data.put("valid", false);
            data.put("error", e.getMessage());
            return resp("INVALID", "Jitsi token validation failed: " + e.getMessage(), data);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // get_kafka_event_log
    // ─────────────────────────────────────────────────────────────────────────

    public String getKafkaEventLog(Map<String, Object> args) {
        JwtClaims claims = auth(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_ADMIN, SecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String topic     = SecurityConfig.getString(args, "topic");
        String sessionId = SecurityConfig.getString(args, "session_id");
        int limit        = Math.min(SecurityConfig.getInt(args, "limit", 50), 200);

        List<Map<String, Object>> log = repo.getKafkaLog().stream()
                .filter(e -> tenantId.equals(e.get("tenant_id")))
                .filter(e -> topic == null || topic.equals(e.get("kafka_topic")))
                .filter(e -> sessionId == null || sessionId.equals(e.get("session_id")))
                .collect(Collectors.toList());

        // Return last N
        int from = Math.max(0, log.size() - limit);
        List<Map<String, Object>> page = log.subList(from, log.size());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("total_events",  log.size());
        data.put("limit",         limit);
        data.put("topic_filter",  topic);
        data.put("session_filter",sessionId);
        data.put("events",        page);

        return resp("OK", log.size() + " Kafka events retrieved", data);
    }

    // ─ helpers ───────────────────────────────────────────────────────────────

    private String extractStr(String json, String key) {
        String k = "\"" + key + "\""; int i = json.indexOf(k); if (i < 0) return null;
        int c = json.indexOf(':', i + k.length()); if (c < 0) return null;
        int s = json.indexOf('"', c + 1); if (s < 0) return null;
        int e = json.indexOf('"', s + 1); if (e < 0) return null;
        return json.substring(s + 1, e);
    }

    private long extractLong(String json, String key) {
        String k = "\"" + key + "\""; int i = json.indexOf(k); if (i < 0) return -1;
        int c = json.indexOf(':', i + k.length()); if (c < 0) return -1;
        StringBuilder num = new StringBuilder();
        for (int j = c + 1; j < json.length(); j++) {
            char ch = json.charAt(j);
            if (Character.isDigit(ch) || ch == '-') num.append(ch);
            else if (num.length() > 0) break;
        }
        try { return Long.parseLong(num.toString()); } catch (NumberFormatException e) { return -1; }
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
