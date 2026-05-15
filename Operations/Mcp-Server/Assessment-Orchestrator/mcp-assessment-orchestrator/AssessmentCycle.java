package com.ecoskiller.mcp.models;

import java.time.Instant;
import java.util.*;

/**
 * Assessment Cycle — top-level grouping that spans one or more tracks.
 *
 * Tracks are executed in order per the tenant-configured execution plan:
 *   Default: GD → INTERVIEW → DOJO
 *   Technical role: GD → DOJO
 *   Management role: GD → INTERVIEW
 *
 * PostgreSQL table: ecoskiller.assessment_cycles
 */
public class AssessmentCycle {

    public enum CycleStatus { INITIALIZING, ACTIVE, COMPLETED, CANCELLED }
    public enum TrackType    { GD, INTERVIEW, DOJO }

    private String       cycleId;
    private String       tenantId;
    private String       jobId;
    private String       recruiterId;
    private CycleStatus  status;
    private List<TrackType> tracks;        // ordered execution plan
    private int          completedTracks;
    private int          totalCandidates;
    private Instant      createdAt;
    private Instant      scheduledAt;
    private Instant      completedAt;
    private String       idempotencyKey;
    private List<String> sessionIds;       // session IDs in creation order
    private String       cancellationReason;

    public AssessmentCycle(String cycleId, String tenantId, String jobId,
                           String recruiterId, List<TrackType> tracks, Instant scheduledAt) {
        this.cycleId       = cycleId;
        this.tenantId      = tenantId;
        this.jobId         = jobId;
        this.recruiterId   = recruiterId;
        this.tracks        = new ArrayList<>(tracks);
        this.status        = CycleStatus.INITIALIZING;
        this.completedTracks = 0;
        this.totalCandidates = 0;
        this.createdAt     = Instant.now();
        this.scheduledAt   = scheduledAt != null ? scheduledAt : Instant.now().plusSeconds(3600);
        this.idempotencyKey = "cycle_" + cycleId + "_init";
        this.sessionIds    = new ArrayList<>();
    }

    public void activate()  { this.status = CycleStatus.ACTIVE; }
    public void complete()  { this.status = CycleStatus.COMPLETED; this.completedAt = Instant.now(); }
    public void cancel(String reason) {
        this.status = CycleStatus.CANCELLED;
        this.cancellationReason = reason;
        this.completedAt = Instant.now();
    }
    public void trackCompleted()      { this.completedTracks++; }
    public void addSession(String id) { this.sessionIds.add(id); }

    public boolean allTracksComplete() { return completedTracks >= tracks.size(); }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("cycle_id",            cycleId);
        m.put("tenant_id",           tenantId);
        m.put("job_id",              jobId);
        m.put("recruiter_id",        recruiterId);
        m.put("status",              status.name());
        m.put("tracks",              tracks.stream().map(Enum::name).collect(java.util.stream.Collectors.toList()));
        m.put("completed_tracks",    completedTracks);
        m.put("total_tracks",        tracks.size());
        m.put("total_candidates",    totalCandidates);
        m.put("session_ids",         sessionIds);
        m.put("created_at",          createdAt.toString());
        m.put("scheduled_at",        scheduledAt.toString());
        m.put("completed_at",        completedAt != null ? completedAt.toString() : null);
        m.put("idempotency_key",     idempotencyKey);
        m.put("cancellation_reason", cancellationReason);
        return m;
    }

    // Getters
    public String      getCycleId()    { return cycleId; }
    public String      getTenantId()   { return tenantId; }
    public String      getJobId()      { return jobId; }
    public String      getRecruiterId(){ return recruiterId; }
    public CycleStatus getStatus()     { return status; }
    public List<TrackType> getTracks() { return tracks; }
    public Instant     getScheduledAt(){ return scheduledAt; }
    public List<String> getSessionIds(){ return sessionIds; }
    public void setScheduledAt(Instant t) { this.scheduledAt = t; }
    public void setTotalCandidates(int n) { this.totalCandidates = n; }
}
