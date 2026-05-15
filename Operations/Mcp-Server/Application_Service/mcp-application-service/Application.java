package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;

/**
 * Domain model representing a candidate's job application.
 *
 * State machine (enforced by ApplicationStateMachine):
 *
 *   APPLIED ──► SCREENED ──► ASSESSED ──┬──► HIRED
 *      │            │                   └──► REJECTED
 *      └────────────┴──────────────────────► WITHDRAWN  (candidate-initiated)
 *
 * Invalid transitions (throws IllegalStateException):
 *   REJECTED → HIRED, HIRED → REJECTED, WITHDRAWN → anything
 */
public class Application {

    public enum Status {
        APPLIED, SCREENED, ASSESSED, HIRED, REJECTED, WITHDRAWN
    }

    public enum AssessmentType {
        GD, INTERVIEW, DOJO, AI_EVAL, NONE
    }

    private String         applicationId;
    private String         tenantId;
    private String         jobId;
    private String         candidateId;
    private String         recruiterId;
    private Status         status;
    private AssessmentType assessmentType;
    private Double         overallScore;
    private Map<String, Double> dimensionScores;
    private String         recruiterNotes;
    private String         rejectionReason;
    private boolean        deleted;      // soft-delete flag
    private Instant        createdAt;
    private Instant        updatedAt;
    private String         idempotencyKey;
    private List<AuditEntry> auditTrail;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Application(String applicationId, String tenantId, String jobId,
                       String candidateId, String recruiterId, String idempotencyKey) {
        this.applicationId  = applicationId;
        this.tenantId       = tenantId;
        this.jobId          = jobId;
        this.candidateId    = candidateId;
        this.recruiterId    = recruiterId;
        this.status         = Status.APPLIED;
        this.assessmentType = AssessmentType.NONE;
        this.dimensionScores = new LinkedHashMap<>();
        this.deleted        = false;
        this.createdAt      = Instant.now();
        this.updatedAt      = Instant.now();
        this.idempotencyKey = idempotencyKey;
        this.auditTrail     = new ArrayList<>();
        addAudit("SYSTEM", "Application created with status APPLIED");
    }

    // -------------------------------------------------------------------------
    // State machine
    // -------------------------------------------------------------------------

    public void transitionTo(Status newStatus, String actorId, String reason) {
        validateTransition(this.status, newStatus);
        Status old = this.status;
        this.status    = newStatus;
        this.updatedAt = Instant.now();
        if (reason != null && !reason.isBlank()) {
            if (newStatus == Status.REJECTED) this.rejectionReason = reason;
            if (newStatus == Status.SCREENED) this.recruiterNotes  = reason;
        }
        addAudit(actorId, old + " → " + newStatus + (reason != null ? " | " + reason : ""));
    }

    private void validateTransition(Status from, Status to) {
        if (from == Status.WITHDRAWN) {
            throw new IllegalStateException("Cannot transition from WITHDRAWN state");
        }
        if (from == Status.HIRED && to != Status.HIRED) {
            throw new IllegalStateException("Application is already HIRED; no further transitions allowed");
        }
        if (from == Status.REJECTED && to == Status.HIRED) {
            throw new IllegalStateException("Cannot move from REJECTED → HIRED");
        }
        if (from == Status.ASSESSED && to == Status.APPLIED) {
            throw new IllegalStateException("Cannot move backwards from ASSESSED → APPLIED");
        }
        // APPLIED can only go to SCREENED or WITHDRAWN
        if (from == Status.APPLIED && to != Status.SCREENED && to != Status.WITHDRAWN) {
            throw new IllegalStateException("APPLIED can only move to SCREENED or WITHDRAWN, not " + to);
        }
    }

    // -------------------------------------------------------------------------
    // Audit
    // -------------------------------------------------------------------------

    public void addAudit(String actorId, String event) {
        auditTrail.add(new AuditEntry(actorId, event, Instant.now()));
        this.updatedAt = Instant.now();
    }

    // -------------------------------------------------------------------------
    // Score update
    // -------------------------------------------------------------------------

    public void applyScore(double overall, Map<String, Double> dimensions, String sessionId) {
        this.overallScore    = overall;
        this.dimensionScores = dimensions != null ? dimensions : new LinkedHashMap<>();
        this.updatedAt       = Instant.now();
        addAudit("SCORING_ENGINE", "Score updated: overall=" + overall
                + " | session=" + sessionId + " | dims=" + this.dimensionScores);
    }

    // -------------------------------------------------------------------------
    // Soft-delete
    // -------------------------------------------------------------------------

    public void softDelete(String actorId) {
        if (this.status == Status.HIRED) {
            throw new IllegalStateException("Cannot withdraw a HIRED application");
        }
        this.deleted   = true;
        this.updatedAt = Instant.now();
        transitionTo(Status.WITHDRAWN, actorId, "Candidate withdrew application");
    }

    // -------------------------------------------------------------------------
    // Serialisation to Map (for JSON response)
    // -------------------------------------------------------------------------

    public Map<String, Object> toMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("application_id",   applicationId);
        m.put("tenant_id",        tenantId);
        m.put("job_id",           jobId);
        m.put("candidate_id",     candidateId);
        m.put("recruiter_id",     recruiterId);
        m.put("status",           status.name());
        m.put("assessment_type",  assessmentType.name());
        m.put("overall_score",    overallScore);
        m.put("dimension_scores", dimensionScores);
        m.put("recruiter_notes",  recruiterNotes);
        m.put("rejection_reason", rejectionReason);
        m.put("deleted",          deleted);
        m.put("created_at",       createdAt.toString());
        m.put("updated_at",       updatedAt.toString());
        m.put("idempotency_key",  idempotencyKey);
        return m;
    }

    public Map<String, Object> toMapWithAudit() {
        Map<String, Object> m = toMap();
        List<Map<String, Object>> trail = new ArrayList<>();
        for (AuditEntry e : auditTrail) trail.add(e.toMap());
        m.put("audit_trail", trail);
        return m;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String         getApplicationId()  { return applicationId; }
    public String         getTenantId()       { return tenantId; }
    public String         getJobId()          { return jobId; }
    public String         getCandidateId()    { return candidateId; }
    public String         getRecruiterId()    { return recruiterId; }
    public Status         getStatus()         { return status; }
    public AssessmentType getAssessmentType() { return assessmentType; }
    public Double         getOverallScore()   { return overallScore; }
    public boolean        isDeleted()         { return deleted; }
    public Instant        getCreatedAt()      { return createdAt; }

    public void setAssessmentType(AssessmentType t) { this.assessmentType = t; }
    public void setRecruiterNotes(String notes)     { this.recruiterNotes = notes; this.updatedAt = Instant.now(); }

    // -------------------------------------------------------------------------
    // Nested audit entry
    // -------------------------------------------------------------------------

    public static class AuditEntry {
        private final String  actorId;
        private final String  event;
        private final Instant timestamp;

        public AuditEntry(String actorId, String event, Instant timestamp) {
            this.actorId   = actorId;
            this.event     = event;
            this.timestamp = timestamp;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("actor_id",  actorId);
            m.put("event",     event);
            m.put("timestamp", timestamp.toString());
            return m;
        }
    }
}
