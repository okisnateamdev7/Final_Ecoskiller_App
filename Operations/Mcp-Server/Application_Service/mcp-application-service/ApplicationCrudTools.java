package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Core CRUD tools for Application-Service.
 *
 * Tools:
 *   submit_application             — POST /api/v1/applications
 *   get_application                — GET  /api/v1/applications/{id}
 *   list_applications              — GET  /api/v1/applications
 *   update_application_status      — PATCH /api/v1/applications/{id}
 *   withdraw_application           — DELETE /api/v1/applications/{id}
 *   validate_application_eligibility
 *   check_duplicate_application
 */
public class ApplicationCrudTools {

    private final ApplicationRepository repo = ApplicationRepository.getInstance();
    private final KafkaEventPublisher   kafka = KafkaEventPublisher.getInstance();

    // =========================================================================
    // submit_application
    // =========================================================================

    public String submitApplication(Map<String, Object> args) {
        // 1. Authenticate & authorise
        JwtClaims claims = authenticate(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_CANDIDATE, SecurityConfig.ROLE_ADMIN);
        String tenantId = SecurityConfig.effectiveTenantId(claims, args);

        // 2. Validate inputs
        String jobId          = SecurityConfig.requireString(args, "job_id");
        String candidateId    = SecurityConfig.requireString(args, "candidate_id");
        String idempotencyKey = SecurityConfig.requireString(args, "idempotency_key");
        String coverNote      = SecurityConfig.sanitise(SecurityConfig.getString(args, "cover_note"), "cover_note");

        // CANDIDATE can only submit for themselves
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE) && !claims.getUserId().equals(candidateId)) {
            throw new SecurityException("CANDIDATE can only submit applications for their own user ID");
        }

        // 3. Idempotency check
        if (!SecurityConfig.checkIdempotency(tenantId + ":" + idempotencyKey)) {
            return JsonUtil.toJson(response("DUPLICATE",
                    "Application already submitted with this idempotency key",
                    Map.of("idempotency_key", idempotencyKey)));
        }

        // 4. Duplicate check (same candidate + job)
        if (repo.existsByJobAndCandidate(tenantId, jobId, candidateId)) {
            return JsonUtil.toJson(response("CONFLICT",
                    "Candidate has already applied to this job",
                    Map.of("job_id", jobId, "candidate_id", candidateId)));
        }

        // 5. Assessment type
        String assessmentTypeStr = SecurityConfig.getString(args, "assessment_type");
        Application.AssessmentType assessmentType;
        try {
            assessmentType = assessmentTypeStr != null
                    ? Application.AssessmentType.valueOf(assessmentTypeStr.toUpperCase())
                    : Application.AssessmentType.NONE;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid assessment_type: " + assessmentTypeStr);
        }

        // 6. Create & persist
        // recruiter_id comes from job-service lookup in prod; optionally passed by caller (ADMIN)
        String recruiterId = SecurityConfig.getString(args, "recruiter_id");
        if (recruiterId == null || recruiterId.isBlank()) recruiterId = "unassigned";
        String appId = SecurityConfig.generateId();
        Application app = new Application(appId, tenantId, jobId, candidateId,
                recruiterId, idempotencyKey);
        app.setAssessmentType(assessmentType);
        if (coverNote != null) app.addAudit(candidateId, "Cover note: " + coverNote);
        repo.save(app);

        // 7. Publish Kafka event
        kafka.publishJobApplied(app);

        // 8. Response
        return JsonUtil.toJson(response("CREATED", "Application submitted successfully", app.toMap()));
    }

    // =========================================================================
    // get_application
    // =========================================================================

    public String getApplication(Map<String, Object> args) {
        JwtClaims claims    = authenticate(args);
        String tenantId     = SecurityConfig.effectiveTenantId(claims, args);
        String appId        = SecurityConfig.requireString(args, "application_id");

        Application app = repo.findById(tenantId, appId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + appId));

        // CANDIDATE: own applications only
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE)
                && !claims.getUserId().equals(app.getCandidateId())) {
            throw new SecurityException("CANDIDATE can only view their own applications");
        }
        // RECRUITER: only applications for their jobs (recruiter_id check)
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER)
                && !claims.getUserId().equals(app.getRecruiterId())) {
            throw new SecurityException("RECRUITER can only view applications for their own job postings");
        }

        return JsonUtil.toJson(response("OK", "Application retrieved", app.toMap()));
    }

    // =========================================================================
    // list_applications
    // =========================================================================

    public String listApplications(Map<String, Object> args) {
        JwtClaims claims = authenticate(args);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        int pageSize     = Math.min(JsonUtil.getInt(args, "page_size", 20), 100);

        // Build filter
        ApplicationRepository.ApplicationFilter filter = new ApplicationRepository.ApplicationFilter();
        filter.status         = SecurityConfig.getString(args, "status");
        filter.assessmentType = SecurityConfig.getString(args, "assessment_type");
        filter.minScore       = parseDouble(SecurityConfig.getString(args, "min_score"));
        filter.maxScore       = parseDouble(SecurityConfig.getString(args, "max_score"));

        // RECRUITER: automatically scope to their jobs
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)) {
            filter.recruiterId = claims.getUserId();
        } else {
            filter.recruiterId = SecurityConfig.getString(args, "recruiter_id");
        }

        List<Application> all = repo.findAll(tenantId, filter);

        // Cursor-based pagination (simple: cursor = index offset)
        String cursor = SecurityConfig.getString(args, "cursor");
        int offset = 0;
        if (cursor != null) { try { offset = Integer.parseInt(cursor); } catch (NumberFormatException ignored) {} }
        List<Application> page  = all.stream().skip(offset).limit(pageSize).collect(Collectors.toList());
        String nextCursor = (offset + pageSize) < all.size() ? String.valueOf(offset + pageSize) : null;

        List<Map<String, Object>> items = page.stream().map(Application::toMap).collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("total",       all.size());
        data.put("page_size",   pageSize);
        data.put("next_cursor", nextCursor);
        data.put("items",       items);

        return JsonUtil.toJson(response("OK", "Applications retrieved", data));
    }

    // =========================================================================
    // update_application_status
    // =========================================================================

    public String updateStatus(Map<String, Object> args) {
        JwtClaims claims = authenticate(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_ADMIN);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String appId     = SecurityConfig.requireString(args, "application_id");
        String newStatus = SecurityConfig.requireString(args, "new_status");
        String reason    = SecurityConfig.sanitise(SecurityConfig.getString(args, "reason"), "reason");

        Application app = repo.findById(tenantId, appId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + appId));

        // RECRUITER scope check (skip if recruiter_id is unassigned — job not yet linked)
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)
                && !"unassigned".equals(app.getRecruiterId())
                && !claims.getUserId().equals(app.getRecruiterId())) {
            throw new SecurityException("RECRUITER can only update applications for their own job postings");
        }

        Application.Status target;
        try {
            target = Application.Status.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        app.transitionTo(target, claims.getUserId(), reason);
        repo.save(app);

        // Publish Kafka event per transition
        switch (target) {
            case SCREENED:  kafka.publishApplicationScreened(app); break;
            case REJECTED:  kafka.publishApplicationRejected(app); break;
            case HIRED:     kafka.publishApplicationHired(app);    break;
            default: break;
        }

        return JsonUtil.toJson(response("OK", "Status updated to " + target, app.toMap()));
    }

    // =========================================================================
    // withdraw_application (soft-delete)
    // =========================================================================

    public String withdrawApplication(Map<String, Object> args) {
        JwtClaims claims = authenticate(args);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String appId     = SecurityConfig.requireString(args, "application_id");

        Application app = repo.findById(tenantId, appId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + appId));

        // CANDIDATE: own application only
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE)
                && !claims.getUserId().equals(app.getCandidateId())) {
            throw new SecurityException("CANDIDATE can only withdraw their own application");
        }

        app.softDelete(claims.getUserId());
        repo.save(app);

        return JsonUtil.toJson(response("OK", "Application withdrawn", app.toMap()));
    }

    // =========================================================================
    // validate_application_eligibility
    // =========================================================================

    public String validateEligibility(Map<String, Object> args) {
        JwtClaims claims  = authenticate(args);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String jobId      = SecurityConfig.requireString(args, "job_id");
        String candidateId = SecurityConfig.requireString(args, "candidate_id");

        List<String> issues = new ArrayList<>();

        // Check: no active application already
        if (repo.existsByJobAndCandidate(tenantId, jobId, candidateId)) {
            issues.add("Candidate has already applied to job " + jobId);
        }

        // Candidate application count (max 10 active per tenant — configurable)
        List<Application> candidateApps = repo.findByCandidateId(tenantId, candidateId);
        long activeCount = candidateApps.stream()
                .filter(a -> a.getStatus() != Application.Status.WITHDRAWN
                          && a.getStatus() != Application.Status.REJECTED)
                .count();
        if (activeCount >= 10) {
            issues.add("Candidate has reached the maximum of 10 active applications");
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("eligible",      issues.isEmpty());
        data.put("job_id",        jobId);
        data.put("candidate_id",  candidateId);
        data.put("issues",        issues);
        data.put("active_applications", activeCount);

        return JsonUtil.toJson(response("OK",
                issues.isEmpty() ? "Candidate is eligible" : "Candidate is ineligible",
                data));
    }

    // =========================================================================
    // check_duplicate_application
    // =========================================================================

    public String checkDuplicate(Map<String, Object> args) {
        JwtClaims claims   = authenticate(args);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String jobId       = SecurityConfig.requireString(args, "job_id");
        String candidateId = SecurityConfig.requireString(args, "candidate_id");

        boolean duplicate = repo.existsByJobAndCandidate(tenantId, jobId, candidateId);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("duplicate",    duplicate);
        data.put("job_id",       jobId);
        data.put("candidate_id", candidateId);

        return JsonUtil.toJson(response("OK",
                duplicate ? "Duplicate application found" : "No duplicate found", data));
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private JwtClaims authenticate(Map<String, Object> args) {
        String token = SecurityConfig.requireString(args, "auth_token");
        return SecurityConfig.validateToken(token);
    }

    private Double parseDouble(String s) {
        if (s == null) return null;
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return null; }
    }

    private Map<String, Object> response(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status",  status);
        r.put("message", message);
        r.put("data",    data);
        r.put("service", "application-service");
        return r;
    }
}
