package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Analytics and search tools.
 *
 * get_application_statistics — Funnel analytics: counts, conversion rates, avg scores
 * search_applications        — Full-text search (OpenSearch delegate in production)
 */
public class AnalyticsTools {

    private final ApplicationRepository repo = ApplicationRepository.getInstance();

    // =========================================================================
    // get_application_statistics
    // =========================================================================

    public String getStatistics(Map<String, Object> args) {
        JwtClaims claims = authenticate(args);
        SecurityConfig.requireRole(claims, SecurityConfig.ROLE_RECRUITER, SecurityConfig.ROLE_ADMIN);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String recruiterId = SecurityConfig.getString(args, "recruiter_id");

        // Scope to recruiter if not admin
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)) {
            recruiterId = claims.getUserId();
        }

        // Build filter
        ApplicationRepository.ApplicationFilter filter = new ApplicationRepository.ApplicationFilter();
        filter.recruiterId = recruiterId;
        List<Application> all = repo.findAll(tenantId, filter);

        // Status counts
        Map<String, Long> statusCounts = repo.countByStatus(tenantId);

        // Conversion rates (funnel)
        long applied   = count(all, Application.Status.APPLIED);
        long screened  = count(all, Application.Status.SCREENED);
        long assessed  = count(all, Application.Status.ASSESSED);
        long hired     = count(all, Application.Status.HIRED);
        long rejected  = count(all, Application.Status.REJECTED);
        long withdrawn = count(all, Application.Status.WITHDRAWN);
        long total     = all.size();

        Map<String, Object> funnel = new LinkedHashMap<>();
        funnel.put("applied",         applied);
        funnel.put("screened",        screened);
        funnel.put("assessed",        assessed);
        funnel.put("hired",           hired);
        funnel.put("rejected",        rejected);
        funnel.put("withdrawn",       withdrawn);
        funnel.put("screen_rate",     rate(screened + assessed + hired + rejected, applied));
        funnel.put("assess_rate",     rate(assessed + hired + rejected, screened + assessed + hired + rejected));
        funnel.put("hire_rate",       rate(hired, assessed + hired + rejected));
        funnel.put("rejection_rate",  rate(rejected, assessed + hired + rejected));

        // Average scores by assessment type
        Map<String, Object> scoresByType = new LinkedHashMap<>();
        for (Application.AssessmentType type : Application.AssessmentType.values()) {
            if (type == Application.AssessmentType.NONE) continue;
            OptionalDouble avg = all.stream()
                    .filter(a -> a.getAssessmentType() == type && a.getOverallScore() != null)
                    .mapToDouble(Application::getOverallScore)
                    .average();
            scoresByType.put(type.name(), avg.isPresent() ? Math.round(avg.getAsDouble() * 10.0) / 10.0 : "N/A");
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("tenant_id",    tenantId);
        data.put("total",        total);
        data.put("funnel",       funnel);
        data.put("scores_by_assessment_type", scoresByType);
        data.put("status_counts", statusCounts);

        return JsonUtil.toJson(response("OK", "Statistics retrieved", data));
    }

    // =========================================================================
    // search_applications
    // =========================================================================

    public String searchApplications(Map<String, Object> args) {
        JwtClaims claims = authenticate(args);
        String tenantId  = SecurityConfig.effectiveTenantId(claims, args);
        String query     = SecurityConfig.requireString(args, "query");
        int pageSize     = Math.min(JsonUtil.getInt(args, "page_size", 10), 50);

        // Sanitise query
        query = SecurityConfig.sanitise(query, "query");

        List<Application> results = repo.search(tenantId, query);

        // CANDIDATE: own only
        if (claims.hasRole(SecurityConfig.ROLE_CANDIDATE) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)) {
            final String uid = claims.getUserId();
            results = results.stream().filter(a -> uid.equals(a.getCandidateId())).collect(Collectors.toList());
        }
        // RECRUITER: own jobs only
        if (claims.hasRole(SecurityConfig.ROLE_RECRUITER) && !claims.hasRole(SecurityConfig.ROLE_ADMIN)) {
            final String rid = claims.getUserId();
            results = results.stream().filter(a -> rid.equals(a.getRecruiterId())).collect(Collectors.toList());
        }

        List<Map<String, Object>> page = results.stream()
                .limit(pageSize)
                .map(Application::toMap)
                .collect(Collectors.toList());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("query",       query);
        data.put("total_hits",  results.size());
        data.put("page_size",   pageSize);
        data.put("results",     page);
        data.put("search_note", "Production: delegates to OpenSearch via search-indexer-service");

        return JsonUtil.toJson(response("OK", "Search completed", data));
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private long count(List<Application> apps, Application.Status status) {
        return apps.stream().filter(a -> a.getStatus() == status).count();
    }

    private String rate(long numerator, long denominator) {
        if (denominator == 0) return "0.00%";
        return String.format("%.2f%%", (numerator * 100.0 / denominator));
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
