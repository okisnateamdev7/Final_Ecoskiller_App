package com.ecoskiller.mcp.models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory repository for Application records.
 *
 * Production replacement: PostgreSQL 15 with row-level security (RLS).
 * Every query method takes tenant_id as a mandatory parameter to enforce
 * tenant isolation — matching the RLS behaviour of PostgreSQL.
 *
 * Key-value structure:  tenantId → (applicationId → Application)
 */
public class ApplicationRepository {

    // Singleton (tools share the same in-memory store)
    private static final ApplicationRepository INSTANCE = new ApplicationRepository();
    public static ApplicationRepository getInstance() { return INSTANCE; }

    // Storage: tenant_id → { application_id → Application }
    private final Map<String, Map<String, Application>> store = new ConcurrentHashMap<>();

    private ApplicationRepository() {}

    // -------------------------------------------------------------------------
    // Write
    // -------------------------------------------------------------------------

    public Application save(Application app) {
        store.computeIfAbsent(app.getTenantId(), k -> new ConcurrentHashMap<>())
             .put(app.getApplicationId(), app);
        return app;
    }

    // -------------------------------------------------------------------------
    // Read
    // -------------------------------------------------------------------------

    /** Tenant-scoped find by ID. */
    public Optional<Application> findById(String tenantId, String applicationId) {
        Map<String, Application> tenantStore = store.getOrDefault(tenantId, Collections.emptyMap());
        Application app = tenantStore.get(applicationId);
        if (app == null || app.isDeleted()) return Optional.empty();
        return Optional.of(app);
    }

    /** Find including soft-deleted (for admin/compliance). */
    public Optional<Application> findByIdIncludeDeleted(String tenantId, String applicationId) {
        return Optional.ofNullable(store.getOrDefault(tenantId, Collections.emptyMap()).get(applicationId));
    }

    /** List all non-deleted applications for a tenant, with optional filters. */
    public List<Application> findAll(String tenantId, ApplicationFilter filter) {
        Map<String, Application> tenantStore = store.getOrDefault(tenantId, Collections.emptyMap());
        return tenantStore.values().stream()
                .filter(a -> !a.isDeleted())
                .filter(a -> filter.matches(a))
                .sorted(Comparator.comparing(Application::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /** Find by candidate_id (for self-service candidate view). */
    public List<Application> findByCandidateId(String tenantId, String candidateId) {
        return store.getOrDefault(tenantId, Collections.emptyMap()).values().stream()
                .filter(a -> !a.isDeleted() && candidateId.equals(a.getCandidateId()))
                .collect(Collectors.toList());
    }

    /** Find by recruiter_id (scoped recruiter view). */
    public List<Application> findByRecruiterId(String tenantId, String recruiterId) {
        return store.getOrDefault(tenantId, Collections.emptyMap()).values().stream()
                .filter(a -> !a.isDeleted() && recruiterId.equals(a.getRecruiterId()))
                .collect(Collectors.toList());
    }

    /** Check whether a candidate has already applied to a given job (idempotency). */
    public boolean existsByJobAndCandidate(String tenantId, String jobId, String candidateId) {
        return store.getOrDefault(tenantId, Collections.emptyMap()).values().stream()
                .anyMatch(a -> !a.isDeleted()
                        && jobId.equals(a.getJobId())
                        && candidateId.equals(a.getCandidateId()));
    }

    /** Count applications per status for funnel analytics. */
    public Map<String, Long> countByStatus(String tenantId) {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (Application.Status s : Application.Status.values()) {
            long c = store.getOrDefault(tenantId, Collections.emptyMap()).values().stream()
                     .filter(a -> !a.isDeleted() && a.getStatus() == s)
                     .count();
            counts.put(s.name(), c);
        }
        return counts;
    }

    /** Full-text search across candidate notes and job IDs (stub for OpenSearch integration). */
    public List<Application> search(String tenantId, String query) {
        String q = query.toLowerCase();
        return store.getOrDefault(tenantId, Collections.emptyMap()).values().stream()
                .filter(a -> !a.isDeleted())
                .filter(a -> a.getJobId().toLowerCase().contains(q)
                        || a.getCandidateId().toLowerCase().contains(q)
                        || (a.getStatus().name().toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Filter DSL
    // -------------------------------------------------------------------------

    public static class ApplicationFilter {
        public String            status;
        public String            assessmentType;
        public String            recruiterId;
        public Double            minScore;
        public Double            maxScore;

        public boolean matches(Application a) {
            if (status != null && !a.getStatus().name().equalsIgnoreCase(status)) return false;
            if (assessmentType != null && !a.getAssessmentType().name().equalsIgnoreCase(assessmentType)) return false;
            if (recruiterId != null && !recruiterId.equals(a.getRecruiterId())) return false;
            if (minScore != null && (a.getOverallScore() == null || a.getOverallScore() < minScore)) return false;
            if (maxScore != null && (a.getOverallScore() == null || a.getOverallScore() > maxScore)) return false;
            return true;
        }
    }
}
