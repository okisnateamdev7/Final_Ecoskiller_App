package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Recruiter-facing tools.
 *
 * get_recruiter_pipeline        — Dashboard view: applications grouped by status per recruiter
 * bulk_update_status            — Batch status transitions with Kafka events per application
 * get_assessment_pipeline_status — Full GD → Interview → Dojo pipeline view per application
 */
public class RecruiterTools {

    private final ApplicationRepository repo  = ApplicationRepository.getInstance();
    private final KafkaEventPublisher   kafka = KafkaEventPublisher.getInstance();

    // =========================================================================
    // get_recruiter_pipeline
    // =========================================================================

    public String getRecruiterPipeline(Map<String, Object> args) {
        JwtClaims claims   = authenticate(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_ADMIN);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String recruiterId = SecurityConfig.getString(args, "recruiter_id");
        String jobId       = SecurityConfig.getString(args, "job_id");

        // RECRUITERs can only see their own pipeline
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)) {
            if (recruiterId != null && !recruiterId.equals(claims.getUserId())) {
                throw new SecurityException("RECRUITER can only access their own pipeline");
            }
            recruiterId = claims.getUserId();
        }
        if (recruiterId == null) {
            throw new IllegalArgumentException("recruiter_id is required");
        }

        // Fetch all applications for this recruiter
        List<Application> apps = repo.findByRecruiterId(tenantId, recruiterId);

        // Filter by job if specified
        if (jobId != null && !jobId.isBlank()) {
            final String fJobId = jobId;
            apps = apps.stream().filter(a -> fJobId.equals(a.getJobId())).collect(Collectors.toList());
        }

        // Group by status
        Map<String, List<Map<String, Object>>> grouped = new LinkedHashMap<>();
        for (Application.Status status : Application.Status.values()) {
            final Application.Status s = status;
            List<Map<String, Object>> bucket = apps.stream()
                    .filter(a -> a.getStatus() == s)
                    .map(Application::toMap)
                    .collect(Collectors.toList());
            grouped.put(status.name(), bucket);
        }

        // Summary metrics
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total_applications",    apps.size());
        summary.put("pending_action",        countIn(apps, Application.Status.APPLIED, Application.Status.ASSESSED));
        summary.put("hired_this_period",     count(apps, Application.Status.HIRED));
        summary.put("avg_score",             avgScore(apps));
        summary.put("assessment_in_progress", count(apps, Application.Status.SCREENED));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("recruiter_id",  recruiterId);
        data.put("tenant_id",     tenantId);
        data.put("job_filter",    jobId);
        data.put("summary",       summary);
        data.put("pipeline",      grouped);

        return JsonUtil.toJson(response("OK", "Recruiter pipeline retrieved", data));
    }

    // =========================================================================
    // bulk_update_status
    // =========================================================================

    public String bulkUpdateStatus(Map<String, Object> args) {
        JwtClaims claims   = authenticate(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_ADMIN);
        String tenantId    = SecurityConfig.effectiveTenantId(claims, args);
        String idsRaw      = SecurityConfig.requireString(args, "application_ids");
        String newStatusStr = SecurityConfig.requireString(args, "new_status");
        String reason      = SecurityConfig.sanitise(SecurityConfig.getString(args, "reason"), "reason");

        // Parse & validate status
        Application.Status targetStatus;
        try {
            targetStatus = Application.Status.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + newStatusStr);
        }

        // Parse IDs
        String[] ids = idsRaw.split(",");
        if (ids.length == 0) throw new IllegalArgumentException("application_ids must not be empty");
        if (ids.length > 100) throw new IllegalArgumentException("Bulk update limited to 100 applications per call");

        List<Map<String, Object>> results = new ArrayList<>();
        int successCount = 0;
        int failCount    = 0;

        for (String rawId : ids) {
            String appId = rawId.trim();
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("application_id", appId);

            try {
                Application app = repo.findById(tenantId, appId)
                        .orElseThrow(() -> new IllegalArgumentException("Not found"));

                // RECRUITER scope check
                if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)
                        && !"unassigned".equals(app.getRecruiterId())
                        && !claims.getUserId().equals(app.getRecruiterId())) {
                    throw new SecurityException("Access denied for application " + appId);
                }

                app.transitionTo(targetStatus, claims.getUserId(), reason);
                repo.save(app);

                // Kafka events per application
                switch (targetStatus) {
                    case SCREENED:  kafka.publishApplicationScreened(app); break;
                    case REJECTED:  kafka.publishApplicationRejected(app); break;
                    case HIRED:     kafka.publishApplicationHired(app);    break;
                    default: break;
                }

                result.put("status",  "SUCCESS");
                result.put("new_status", targetStatus.name());
                successCount++;

            } catch (Exception e) {
                result.put("status",  "FAILED");
                result.put("error",   e.getMessage());
                failCount++;
            }
            results.add(result);
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("total",      ids.length);
        data.put("succeeded",  successCount);
        data.put("failed",     failCount);
        data.put("target_status", targetStatus.name());
        data.put("results",    results);

        return JsonUtil.toJson(response("OK",
                "Bulk update completed: " + successCount + " succeeded, " + failCount + " failed", data));
    }

    // =========================================================================
    // get_assessment_pipeline_status
    // =========================================================================

    public String getAssessmentPipelineStatus(Map<String, Object> args) {
        JwtClaims claims  = authenticate(args);
        String tenantId   = SecurityConfig.effectiveTenantId(claims, args);
        String appId      = SecurityConfig.requireString(args, "application_id");

        Application app = repo.findById(tenantId, appId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + appId));

        // Access control
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)
                && !claims.getUserId().equals(app.getCandidateId())) {
            throw new SecurityException("Access denied");
        }
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)
                && !claims.getUserId().equals(app.getRecruiterId())) {
            throw new SecurityException("Access denied");
        }

        // Build assessment pipeline stages
        // Multi-stage pipeline: GD → Interview → Dojo (configurable per job)
        List<Map<String, Object>> stages = new ArrayList<>();

        stages.add(buildStage("APPLICATION_REVIEW", "Application Review",
                app.getStatus().ordinal() >= Application.Status.SCREENED.ordinal(),
                app.getStatus() == Application.Status.APPLIED ? "PENDING" : "COMPLETE"));

        stages.add(buildStage("GROUP_DISCUSSION", "Group Discussion (GD)",
                app.getAssessmentType() == Application.AssessmentType.GD,
                resolveStageStatus(app, Application.Status.SCREENED)));

        stages.add(buildStage("INTERVIEW", "One-on-One Interview",
                app.getAssessmentType() == Application.AssessmentType.INTERVIEW,
                resolveStageStatus(app, Application.Status.ASSESSED)));

        stages.add(buildStage("DOJO_MATCH", "Coding Dojo Match",
                app.getAssessmentType() == Application.AssessmentType.DOJO,
                resolveStageStatus(app, Application.Status.ASSESSED)));

        stages.add(buildStage("FINAL_DECISION", "Final Hiring Decision",
                false,
                resolveFinalStatus(app)));

        // Overall progress
        long completed = stages.stream()
                .filter(s -> "COMPLETE".equals(s.get("stage_status")))
                .count();
        double progress = (completed * 100.0) / stages.size();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("application_id",   appId);
        data.put("current_status",   app.getStatus().name());
        data.put("assessment_type",  app.getAssessmentType().name());
        data.put("overall_score",    app.getOverallScore());
        data.put("progress_pct",     Math.round(progress));
        data.put("stages",           stages);
        data.put("pipeline_note",    "Full pipeline: APPLICATION_REVIEW → GD → INTERVIEW → DOJO → FINAL_DECISION");

        return JsonUtil.toJson(response("OK", "Assessment pipeline status retrieved", data));
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Map<String, Object> buildStage(String id, String label, boolean active, String status) {
        Map<String, Object> s = new LinkedHashMap<>();
        s.put("stage_id",     id);
        s.put("stage_label",  label);
        s.put("active",       active);
        s.put("stage_status", status);
        return s;
    }

    private String resolveStageStatus(Application app, Application.Status threshold) {
        int appOrd = app.getStatus().ordinal();
        int thrOrd = threshold.ordinal();
        if (appOrd > thrOrd) return "COMPLETE";
        if (appOrd == thrOrd) return "IN_PROGRESS";
        return "PENDING";
    }

    private String resolveFinalStatus(Application app) {
        if (app.getStatus() == Application.Status.HIRED)    return "COMPLETE";
        if (app.getStatus() == Application.Status.REJECTED) return "COMPLETE";
        return "PENDING";
    }

    private long count(List<Application> apps, Application.Status status) {
        return apps.stream().filter(a -> a.getStatus() == status).count();
    }

    private long countIn(List<Application> apps, Application.Status... statuses) {
        Set<Application.Status> set = new HashSet<>(Arrays.asList(statuses));
        return apps.stream().filter(a -> set.contains(a.getStatus())).count();
    }

    private String avgScore(List<Application> apps) {
        OptionalDouble avg = apps.stream()
                .filter(a -> a.getOverallScore() != null)
                .mapToDouble(Application::getOverallScore)
                .average();
        return avg.isPresent() ? String.format("%.1f", avg.getAsDouble()) : "N/A";
    }

    private JwtClaims authenticate(Map<String, Object> args) {
        return SecurityConfig.validateToken(SecurityConfig.requireString(args, "auth_token"));
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
