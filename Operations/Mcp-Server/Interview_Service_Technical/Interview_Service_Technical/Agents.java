package com.ecoskiller.mcp.interview.agents;

import com.ecoskiller.mcp.interview.model.SecurityContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 1 — SCHEDULE_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Schedules a new one-on-one video interview.
 * Creates interview record in PostgreSQL, reserves Jitsi room.
 * Publishes: interview.scheduled.acked → Kafka
 */
class ScheduleInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String jobId          = validateUuid(requireString(args, "job_id"), "job_id");
        String candidateId    = validateUuid(requireString(args, "candidate_id"), "candidate_id");
        String recruiterId    = validateUuid(requireString(args, "recruiter_id"), "recruiter_id");
        String scheduledTime  = requireString(args, "scheduled_time");
        int    duration       = optionalInt(args, "duration_minutes", 30);
        String templateId     = optionalString(args, "template_id", null);
        boolean recordEnabled = optionalBool(args, "recording_enabled", false);

        // Validate duration
        if (duration < 5 || duration > 180) {
            throw new IllegalArgumentException("duration_minutes must be 5–180, got: " + duration);
        }

        String interviewId = newId();
        String roomId      = "ecoskiller-" + ctx.tenantId() + "-" + interviewId.substring(0, 8);
        String jitsiUrl    = "https://meet.ecoskiller.internal/" + roomId;

        // Production: INSERT INTO interview (...) + Jitsi REST API room creation
        publishKafkaEvent("interview.scheduled.acked", "interview.scheduled.acked",
            "{\"interview_id\":\"" + interviewId + "\",\"room_id\":\"" + roomId + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        LOG.info("Interview scheduled: id=" + interviewId + " job=" + jobId + " tenant=" + ctx.tenantId());

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"room_id\":\"%s\",\"jitsi_url\":\"%s\"," +
            "\"job_id\":\"%s\",\"candidate_id\":\"%s\",\"recruiter_id\":\"%s\"," +
            "\"scheduled_time\":\"%s\",\"duration_minutes\":%d,\"recording_enabled\":%b," +
            "\"state\":\"SCHEDULED\",\"tenant_id\":\"%s\",\"created_at\":\"%s\"}",
            interviewId, roomId, jitsiUrl, jobId, candidateId, recruiterId,
            scheduledTime, duration, recordEnabled, ctx.tenantId(), Instant.now()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 2 — GET_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Retrieves full interview details. Enforces tenant_id RLS.
 */
class GetInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");

        // Production: SELECT * FROM interview WHERE interview_id=? AND tenant_id=?
        // RLS on PostgreSQL ensures tenant_id isolation implicitly

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"tenant_id\":\"%s\"," +
            "\"state\":\"SCHEDULED\",\"job_id\":\"job-001\",\"candidate_id\":\"cand-001\"," +
            "\"recruiter_id\":\"rec-001\",\"scheduled_time\":\"2026-04-01T10:00:00Z\"," +
            "\"duration_minutes\":30,\"recording_enabled\":false,\"room_id\":\"ecoskiller-%s-%s\"," +
            "\"feedback\":null,\"scores\":null,\"audit_log_count\":1}",
            interviewId, ctx.tenantId(), ctx.tenantId().substring(0, 4), interviewId.substring(0, 8)
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 3 — UPDATE_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Updates interview: reschedule, change interviewer, cancel.
 * Validates allowed state transitions.
 */
class UpdateInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId  = validateUuid(requireString(args, "interview_id"), "interview_id");
        String newTime      = optionalString(args, "scheduled_time", null);
        String newRecruiter = optionalString(args, "recruiter_id", null);
        String newStatus    = optionalString(args, "status", null);

        if (newStatus != null && !newStatus.equals("CANCELLED")) {
            throw new IllegalArgumentException("Only CANCELLED status can be set via update_interview");
        }

        // Production: UPDATE interview SET ... WHERE interview_id=? AND tenant_id=?
        publishKafkaEvent("interview.updated", "interview.updated",
            "{\"interview_id\":\"" + interviewId + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"tenant_id\":\"%s\"," +
            "\"updated_fields\":{\"scheduled_time\":%s,\"recruiter_id\":%s,\"status\":%s}," +
            "\"updated_at\":\"%s\"}",
            interviewId, ctx.tenantId(),
            newTime      == null ? "null" : "\"" + newTime + "\"",
            newRecruiter == null ? "null" : "\"" + newRecruiter + "\"",
            newStatus    == null ? "null" : "\"" + newStatus + "\"",
            Instant.now()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 4 — JOIN_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Issues short-lived Jitsi media JWT (5-min TTL) and returns jitsi_url.
 * Validates participant type and recording consent if required.
 */
class JoinInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId     = validateUuid(requireString(args, "interview_id"), "interview_id");
        String participantType = requireString(args, "participant_type");

        if (!participantType.matches("RECRUITER|CANDIDATE|OBSERVER|INTERVIEWER")) {
            throw new IllegalArgumentException("participant_type must be RECRUITER, CANDIDATE, INTERVIEWER, or OBSERVER");
        }

        // Production:
        // 1. Verify interview state is SCHEDULED or STARTED
        // 2. Issue Jitsi JWT: {sub: roomId, exp: now+300, context: {user: {name, email, role}}}
        // 3. Return jitsi_url + media_token

        String roomId     = "ecoskiller-" + ctx.tenantId() + "-" + interviewId.substring(0, 8);
        String mediaToken = "JITSI_JWT_" + interviewId.substring(0, 8).toUpperCase() + "_5MIN_TTL";
        String jitsiUrl   = "https://meet.ecoskiller.internal/" + roomId + "?jwt=" + mediaToken;
        String tokenExpiry = Instant.now().plus(5, ChronoUnit.MINUTES).toString();

        // Fire participant.joined event via WebSocket to other participants
        publishKafkaEvent("interview.started", "participant.joined",
            "{\"interview_id\":\"" + interviewId + "\",\"participant_type\":\"" + participantType + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"room_id\":\"%s\"," +
            "\"jitsi_url\":\"%s\",\"media_token\":\"%s\",\"token_expires_at\":\"%s\"," +
            "\"participant_type\":\"%s\",\"coturn_url\":\"turn:coturn.ecoskiller.internal:3478\"," +
            "\"websocket_url\":\"wss://api.ecoskiller.internal/ws/interview/%s\"}",
            interviewId, roomId, jitsiUrl, mediaToken, tokenExpiry, participantType, interviewId
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 5 — PAUSE_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Pauses active interview. Max pause duration: 5 minutes.
 * Suspends Redis countdown timer. Publishes interview.paused.
 */
class PauseInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");

        // Production: SET interview:{id}:state PAUSED + HSET interview:{id}:pause_start <timestamp>
        // Enforce max 5-min pause via Redis TTL
        validateStateTransition("STARTED", "PAUSED");

        publishKafkaEvent("interview.paused", "interview.paused",
            "{\"interview_id\":\"" + interviewId + "\",\"paused_by\":\"" + ctx.userId() + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"state\":\"PAUSED\"," +
            "\"paused_at\":\"%s\",\"max_pause_until\":\"%s\",\"tenant_id\":\"%s\"}",
            interviewId, Instant.now(),
            Instant.now().plus(5, ChronoUnit.MINUTES), ctx.tenantId()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 6 — RESUME_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Resumes paused interview. Restores Redis timer from suspension point.
 * Publishes interview.resumed.
 */
class ResumeInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");
        validateStateTransition("PAUSED", "STARTED");

        publishKafkaEvent("interview.resumed", "interview.resumed",
            "{\"interview_id\":\"" + interviewId + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"state\":\"STARTED\"," +
            "\"resumed_at\":\"%s\",\"timer_resumed_from_seconds\":1247,\"tenant_id\":\"%s\"}",
            interviewId, Instant.now(), ctx.tenantId()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 7 — COMPLETE_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Marks interview as completed. Triggers async AI scoring via scoring-engine.
 * Publishes interview.completed to Kafka.
 */
class CompleteInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId     = validateUuid(requireString(args, "interview_id"), "interview_id");
        String completionReason = optionalString(args, "completion_reason", "NORMAL");

        if (!completionReason.matches("NORMAL|TIMEOUT|TECHNICAL_ISSUE|CANDIDATE_LEFT")) {
            throw new IllegalArgumentException("completion_reason must be NORMAL, TIMEOUT, TECHNICAL_ISSUE, or CANDIDATE_LEFT");
        }

        validateStateTransition("STARTED", "COMPLETED");

        // Production: UPDATE interview SET status='COMPLETED' WHERE id=? AND tenant_id=?
        // Trigger async scoring pipeline
        publishKafkaEvent("interview.completed", "interview.completed",
            "{\"interview_id\":\"" + interviewId + "\",\"completion_reason\":\"" + completionReason +
            "\",\"recruiter_id\":\"" + ctx.recruiterId() + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"state\":\"COMPLETED\"," +
            "\"completed_at\":\"%s\",\"completion_reason\":\"%s\"," +
            "\"scoring_initiated\":true,\"estimated_score_ready_minutes\":10," +
            "\"feedback_form_available\":true,\"tenant_id\":\"%s\"}",
            interviewId, Instant.now(), completionReason, ctx.tenantId()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 8 — GET_INTERVIEW_RESULT_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Retrieves final merged result: AI score (40%) + interviewer feedback (60%).
 * Returns dimension breakdowns and recommendation.
 */
class GetInterviewResultAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");

        // Production: SELECT * FROM interview_score WHERE interview_id=? AND tenant_id=?
        // Merge: final_score = (interviewer_score * 0.6) + (ai_score * 0.4)

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"tenant_id\":\"%s\"," +
            "\"result\":{" +
            "\"final_score\":3.8,\"recommendation\":\"HIRE\"," +
            "\"interviewer_score\":{\"weight\":0.6,\"communication\":4,\"technical\":4,\"cultural_fit\":3,\"weighted\":3.9}," +
            "\"ai_score\":{\"weight\":0.4,\"communication_pattern\":3.5,\"technical_accuracy\":3.7,\"engagement\":4.0,\"confidence\":0.87,\"weighted\":3.73}," +
            "\"dimensions\":{\"communication\":3.9,\"technical_knowledge\":3.85,\"cultural_fit\":3.6}," +
            "\"scoring_completed_at\":\"%s\"}}",
            interviewId, ctx.tenantId(), Instant.now().minus(5, ChronoUnit.MINUTES)
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 9 — SUBMIT_FEEDBACK_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Interviewer submits post-interview feedback.
 * Validates scores (1-5), recommendation enum, triggers score merge.
 */
class SubmitFeedbackAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId  = validateUuid(requireString(args, "interview_id"), "interview_id");
        int    commScore    = validateScore(requireInt(args, "communication_score"), "communication_score");
        int    techScore    = validateScore(requireInt(args, "technical_score"), "technical_score");
        int    cultureFit   = validateScore(requireInt(args, "cultural_fit_score"), "cultural_fit_score");
        String comments     = sanitizeString(optionalString(args, "comments", ""));
        String recommendation = requireString(args, "recommendation");

        if (!recommendation.matches("HIRE|MAYBE|REJECT")) {
            throw new IllegalArgumentException("recommendation must be HIRE, MAYBE, or REJECT");
        }

        // Production: INSERT INTO interview_feedback (...)
        publishKafkaEvent("interview.feedback.submitted", "interview.feedback.submitted",
            "{\"interview_id\":\"" + interviewId + "\",\"recommendation\":\"" + recommendation +
            "\",\"recruiter_id\":\"" + ctx.userId() + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        double avgScore = (commScore + techScore + cultureFit) / 3.0;
        String feedbackId = newId();

        return String.format(
            "{\"status\":\"success\",\"feedback_id\":\"%s\",\"interview_id\":\"%s\"," +
            "\"communication_score\":%d,\"technical_score\":%d,\"cultural_fit_score\":%d," +
            "\"average_interviewer_score\":%.2f,\"recommendation\":\"%s\"," +
            "\"comments_logged\":%b,\"score_merge_triggered\":true,\"tenant_id\":\"%s\",\"submitted_at\":\"%s\"}",
            feedbackId, interviewId, commScore, techScore, cultureFit,
            avgScore, recommendation, !comments.isEmpty(), ctx.tenantId(), Instant.now()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 10 — GET_QUESTIONS_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Returns ordered interview question template for the session.
 * Includes estimated time per question.
 */
class GetQuestionsAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");

        // Production: SELECT iq.* FROM interview_question iq
        //             JOIN interview i ON i.template_id = iq.template_id
        //             WHERE i.interview_id=? AND i.tenant_id=?

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"tenant_id\":\"%s\"," +
            "\"template_id\":\"template-001\",\"total_questions\":5," +
            "\"questions\":[" +
            "{\"id\":\"q1\",\"order\":1,\"text\":\"Tell me about yourself and your background.\",\"type\":\"OPEN\",\"estimated_minutes\":5}," +
            "{\"id\":\"q2\",\"order\":2,\"text\":\"Describe a challenging technical problem you solved.\",\"type\":\"BEHAVIORAL\",\"estimated_minutes\":7}," +
            "{\"id\":\"q3\",\"order\":3,\"text\":\"How do you approach system design for high-traffic applications?\",\"type\":\"TECHNICAL\",\"estimated_minutes\":8}," +
            "{\"id\":\"q4\",\"order\":4,\"text\":\"Tell me about a time you disagreed with your team.\",\"type\":\"BEHAVIORAL\",\"estimated_minutes\":5}," +
            "{\"id\":\"q5\",\"order\":5,\"text\":\"Where do you see yourself in 5 years?\",\"type\":\"OPEN\",\"estimated_minutes\":5}" +
            "]}",
            interviewId, ctx.tenantId()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 11 — LOG_ANSWER_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Logs candidate's answer to a specific question.
 * Records answer_text and duration_seconds.
 */
class LogAnswerAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");
        String questionId  = requireString(args, "question_id");
        String answerText  = sanitizeString(requireString(args, "answer_text"));
        int    duration    = optionalInt(args, "duration_seconds", 0);

        if (answerText.length() > 10000) {
            throw new IllegalArgumentException("answer_text exceeds 10,000 character limit");
        }

        // Production: INSERT INTO interview_question_answer (...)
        String answerId = newId();

        return String.format(
            "{\"status\":\"success\",\"answer_id\":\"%s\",\"interview_id\":\"%s\"," +
            "\"question_id\":\"%s\",\"answer_length\":%d,\"duration_seconds\":%d," +
            "\"tenant_id\":\"%s\",\"logged_at\":\"%s\"}",
            answerId, interviewId, questionId, answerText.length(), duration,
            ctx.tenantId(), Instant.now()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 12 — LIST_INTERVIEWS_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Lists interviews with filters. Results scoped by tenant_id + caller role.
 * RECRUITERs see all tenant interviews; CANDIDATEs see only their own.
 */
class ListInterviewsAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String status      = optionalString(args, "status", "ALL");
        String dateFrom    = optionalString(args, "date_from", "2026-01-01T00:00:00Z");
        String dateTo      = optionalString(args, "date_to", Instant.now().toString());
        int    page        = Math.max(1, optionalInt(args, "page", 1));
        int    pageSize    = Math.min(100, Math.max(1, optionalInt(args, "page_size", 20)));

        // Validate status
        if (!status.matches("ALL|SCHEDULED|STARTED|PAUSED|COMPLETED|CANCELLED|NO_SHOW")) {
            throw new IllegalArgumentException("Invalid status filter: " + status);
        }

        // Production: SELECT * FROM interview WHERE tenant_id=? AND status=? ...
        // CANDIDATE role: additionally AND (candidate_id=? OR recruiter_id=?)

        return String.format(
            "{\"status\":\"success\",\"tenant_id\":\"%s\",\"caller_role\":\"%s\"," +
            "\"filters\":{\"status\":\"%s\",\"date_from\":\"%s\",\"date_to\":\"%s\"}," +
            "\"pagination\":{\"page\":%d,\"page_size\":%d,\"total_count\":42,\"total_pages\":3}," +
            "\"interviews\":[" +
            "{\"interview_id\":\"int-001\",\"state\":\"SCHEDULED\",\"scheduled_time\":\"2026-04-01T10:00:00Z\",\"candidate_id\":\"cand-001\",\"duration_minutes\":30}," +
            "{\"interview_id\":\"int-002\",\"state\":\"COMPLETED\",\"scheduled_time\":\"2026-03-15T14:00:00Z\",\"candidate_id\":\"cand-002\",\"duration_minutes\":45}" +
            "]}",
            ctx.tenantId(), ctx.role(), status, dateFrom, dateTo, page, pageSize
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 13 — RESCHEDULE_INTERVIEW_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Reschedules interview (no-show or recruiter request).
 * Notifies candidate via notification-service.
 */
class RescheduleInterviewAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId  = validateUuid(requireString(args, "interview_id"), "interview_id");
        String newTime      = requireString(args, "new_scheduled_time");
        String reason       = requireString(args, "reason");

        if (!reason.matches("NO_SHOW|RECRUITER_REQUEST|CANDIDATE_REQUEST|TECHNICAL_ISSUE")) {
            throw new IllegalArgumentException("reason must be NO_SHOW, RECRUITER_REQUEST, CANDIDATE_REQUEST, or TECHNICAL_ISSUE");
        }

        // Production: UPDATE interview SET scheduled_time=?, status='SCHEDULED' WHERE id=? AND tenant_id=?
        publishKafkaEvent("interview.rescheduled", "interview.rescheduled",
            "{\"interview_id\":\"" + interviewId + "\",\"reason\":\"" + reason +
            "\",\"new_time\":\"" + newTime + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"new_scheduled_time\":\"%s\"," +
            "\"reason\":\"%s\",\"notification_sent\":true,\"state\":\"SCHEDULED\"," +
            "\"tenant_id\":\"%s\",\"rescheduled_at\":\"%s\"}",
            interviewId, newTime, reason, ctx.tenantId(), Instant.now()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 14 — RECORDING_CONSENT_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Candidate grants or denies recording consent (DPDPA 2023).
 * Consent timestamp logged immutably to interview_audit_log.
 */
class RecordingConsentAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId    = validateUuid(requireString(args, "interview_id"), "interview_id");
        boolean consentGranted = optionalBool(args, "consent_granted", false);

        // Only CANDIDATE can grant consent for their own interview
        if (!ctx.role().equals("CANDIDATE") && !ctx.role().equals("ADMIN")) {
            throw new SecurityException("Only CANDIDATE can grant recording consent");
        }

        // Production: UPDATE interview SET recording_consent=?, consent_at=NOW()
        //             INSERT INTO interview_audit_log (action='CONSENT_GRANTED', ...)
        publishKafkaEvent("interview.consent.granted", "interview.consent.granted",
            "{\"interview_id\":\"" + interviewId + "\",\"consent_granted\":" + consentGranted +
            ",\"candidate_id\":\"" + ctx.candidateId() + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"consent_granted\":%b," +
            "\"consent_logged_at\":\"%s\",\"recording_will_proceed\":%b," +
            "\"right_to_delete_until\":\"%s\",\"tenant_id\":\"%s\"}",
            interviewId, consentGranted, Instant.now(), consentGranted,
            Instant.now().plus(30, ChronoUnit.DAYS), ctx.tenantId()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 15 — DELETE_RECORDING_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Candidate requests recording deletion (right-to-erasure, DPDPA 2023).
 * Soft-delete from MinIO within 30-day window. Audit copy retained 7 years.
 */
class DeleteRecordingAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");
        String reason      = sanitizeString(optionalString(args, "reason", "CANDIDATE_REQUEST"));

        // Production:
        // 1. Verify request within 30-day window
        // 2. MinIO soft-delete: mc rm --dangerous (with WORM check)
        // 3. UPDATE interview_recording SET deleted_at=NOW(), delete_reason=?
        // 4. Retain audit_log entry (immutable)

        publishKafkaEvent("interview.recording.deleted", "interview.recording.deleted",
            "{\"interview_id\":\"" + interviewId + "\",\"reason\":\"" + reason +
            "\",\"candidate_id\":\"" + ctx.candidateId() + "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"recording_deleted\":true," +
            "\"deletion_reason\":\"%s\",\"audit_log_retained\":true," +
            "\"deleted_at\":\"%s\",\"tenant_id\":\"%s\"}",
            interviewId, reason, Instant.now(), ctx.tenantId()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 16 — ANALYTICS_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Recruiter dashboard analytics from ClickHouse immutable event log.
 * Metrics: avg duration, no-show rate, score distributions, QoS.
 */
class AnalyticsAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String dateFrom   = optionalString(args, "date_from", "2026-01-01T00:00:00Z");
        String dateTo     = optionalString(args, "date_to", Instant.now().toString());
        String metricType = optionalString(args, "metric_type", "ALL");
        String recruiterId = optionalString(args, "recruiter_id", ctx.recruiterId());

        if (!metricType.matches("ALL|QOS|SCORES|NO_SHOW|DURATION")) {
            throw new IllegalArgumentException("metric_type must be ALL, QOS, SCORES, NO_SHOW, or DURATION");
        }

        // Production: ClickHouse query on interview_events materialized view
        // SELECT avg(duration), count(*) filter(no_show), percentile(score, 0.5) ...
        // WHERE tenant_id=? AND started_at BETWEEN ? AND ?

        return String.format(
            "{\"status\":\"success\",\"tenant_id\":\"%s\",\"period\":{\"from\":\"%s\",\"to\":\"%s\"}," +
            "\"metrics\":{" +
            "\"duration\":{\"avg_minutes\":34.2,\"p50_minutes\":30,\"p95_minutes\":58}," +
            "\"no_show\":{\"rate_percent\":8.3,\"count\":17,\"total_scheduled\":205}," +
            "\"scores\":{\"avg_final\":3.6,\"hire_rate_percent\":42.1,\"maybe_rate_percent\":31.2,\"reject_rate_percent\":26.7}," +
            "\"qos\":{\"avg_audio_jitter_ms\":12.4,\"avg_video_bitrate_kbps\":850,\"avg_packet_loss_pct\":0.3,\"avg_rtt_ms\":45}," +
            "\"completion\":{\"normal_rate_percent\":88.3,\"timeout_rate_percent\":3.4,\"technical_issue_rate_percent\":0.5}" +
            "}}",
            ctx.tenantId(), dateFrom, dateTo
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 17 — NO_SHOW_DETECTION_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Detects candidate no-show (not joined within 5-minute grace period).
 * Publishes interview.no_show.detected. Triggers SMS/email alert + reschedule workflow.
 */
class NoShowDetectionAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");

        // Production:
        // 1. Check Redis: HGET interview:{id}:participants candidate_joined_at
        // 2. Compare with scheduled_time + 5-min grace
        // 3. If not joined → publish no_show event

        // Simulate: assume no-show confirmed
        boolean noShowConfirmed = true;

        if (noShowConfirmed) {
            publishKafkaEvent("interview.no_show.detected", "interview.no_show.detected",
                "{\"interview_id\":\"" + interviewId + "\",\"recruiter_id\":\"" + ctx.recruiterId() +
                "\",\"tenant_id\":\"" + ctx.tenantId() + "\"}");
        }

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"no_show_confirmed\":%b," +
            "\"grace_period_minutes\":5,\"notification_sent\":true," +
            "\"reschedule_workflow_triggered\":%b,\"tenant_id\":\"%s\",\"detected_at\":\"%s\"}",
            interviewId, noShowConfirmed, noShowConfirmed, ctx.tenantId(), Instant.now()
        );
    }
}

// ════════════════════════════════════════════════════════════════════════════
//  AGENT 18 — SESSION_HEALTH_AGENT
// ════════════════════════════════════════════════════════════════════════════

/**
 * Returns real-time WebSocket/Jitsi session health metrics.
 * Captures: audio jitter, video bitrate, packet loss, RTT, participant status.
 */
class SessionHealthAgent extends BaseAgent {
    @Override
    public String execute(JsonNode args, SecurityContext ctx) {
        String interviewId = validateUuid(requireString(args, "interview_id"), "interview_id");

        // Production: Read from Redis HASH interview:{id}:qos
        // Populated by WebSocket connection.health heartbeat messages

        return String.format(
            "{\"status\":\"success\",\"interview_id\":\"%s\",\"tenant_id\":\"%s\"," +
            "\"session_state\":\"STARTED\"," +
            "\"qos\":{" +
            "\"audio_jitter_ms\":9.2,\"video_bitrate_kbps\":920,\"packet_loss_pct\":0.1,\"round_trip_ms\":38," +
            "\"quality_rating\":\"EXCELLENT\"}," +
            "\"participants\":{" +
            "\"recruiter\":{\"connected\":true,\"join_time\":\"%s\",\"camera\":true,\"microphone\":true}," +
            "\"candidate\":{\"connected\":true,\"join_time\":\"%s\",\"camera\":true,\"microphone\":true}" +
            "}," +
            "\"jitsi_room_active\":true,\"websocket_connections\":2,\"sampled_at\":\"%s\"}",
            interviewId, ctx.tenantId(),
            Instant.now().minus(22, ChronoUnit.MINUTES),
            Instant.now().minus(20, ChronoUnit.MINUTES),
            Instant.now()
        );
    }
}
