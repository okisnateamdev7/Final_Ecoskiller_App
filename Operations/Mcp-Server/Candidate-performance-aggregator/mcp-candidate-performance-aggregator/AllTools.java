package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.*;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.AggSecurityConfig;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// ─────────────────────────────────────────────────────────────────────────────
// AggregationTools — compute_aggregate, retry_aggregation,
//                    batch_compute_aggregates, get_aggregation_status
// ─────────────────────────────────────────────────────────────────────────────
class AggregationTools {

    private final AggRepository     repo   = AggRepository.getInstance();
    private final AggregationEngine engine = new AggregationEngine();

    public String computeAggregate(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_SERVICE_ACCOUNT, AggSecurityConfig.ROLE_ADMIN);
        String tenantId     = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId  = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId      = AggSecurityConfig.requireString(args, "cycle_id");
        boolean forceRecomp = AggSecurityConfig.getBool(args, "force_recompute", false);

        if (forceRecomp) AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);

        // Check idempotency — return cached if already COMPUTED and not forcing
        Optional<AggModels.AggregateRecord> existing = repo.getAggregate(tenantId, candidateId, cycleId);
        if (existing.isPresent() && existing.get().status == AggModels.AggStatus.COMPUTED && !forceRecomp) {
            Map<String,Object> data = new LinkedHashMap<>(existing.get().toMap());
            data.put("idempotent_return", true);
            return resp("OK", "Aggregate already computed (idempotent return)", data);
        }

        // Check for lock conflict
        if (AggSecurityConfig.isLocked(candidateId, cycleId))
            return resp("CONFLICT", "Aggregation already in progress. Lock held for " + AggSecurityConfig.getLockTtlSec() + "s",
                    Map.of("candidate_id",candidateId,"cycle_id",cycleId,"status","IN_PROGRESS"));

        try {
            AggModels.AggregateRecord record = engine.aggregate(tenantId, candidateId, cycleId, forceRecomp);
            return resp("OK", "Aggregate computed successfully", record.toMap());
        } catch (Exception e) {
            // Save FAILED state + DLQ
            AggModels.AggregateRecord failed = existing.orElse(new AggModels.AggregateRecord(candidateId, cycleId, tenantId));
            failed.status = AggModels.AggStatus.FAILED;
            failed.retryCount++;
            repo.saveAggregate(failed);
            repo.inc("aggregations_failed_total");

            if (failed.retryCount >= AggSecurityConfig.getMaxRetryCount()) {
                AggModels.DlqEntry dlqEntry = new AggModels.DlqEntry(candidateId, cycleId, tenantId,
                        AggSecurityConfig.generateId(), e.getMessage(), failed.retryCount, "aggregation_failed");
                repo.addDlqEntry(dlqEntry);
                Map<String,Object> dlqPayload = Map.of("candidate_id",candidateId,"cycle_id",cycleId,
                        "tenant_id",tenantId,"failure_reason",e.getMessage(),"dlq_id",dlqEntry.dlqId);
                repo.logKafkaEvent("candidate.performance.aggregated.dlq", dlqPayload);
                repo.logKafkaEvent("candidate.performance.aggregated.failed", dlqPayload);
                repo.audit("AGGREGATE_FAILED_DLQ", candidateId, cycleId, tenantId,
                        "retries=" + failed.retryCount + " reason=" + e.getMessage(), false);
                return resp("DLQ", "Aggregation failed after " + AggSecurityConfig.getMaxRetryCount()
                        + " retries — sent to DLQ", Map.of("dlq_id",dlqEntry.dlqId,"failure_reason",e.getMessage()));
            }

            repo.audit("AGGREGATE_FAILED", candidateId, cycleId, tenantId,
                    "attempt=" + failed.retryCount + " reason=" + e.getMessage(), false);
            return resp("FAILED", "Aggregation failed: " + e.getMessage(),
                    Map.of("candidate_id",candidateId,"cycle_id",cycleId,"retry_count",failed.retryCount));
        }
    }

    public String retryAggregation(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId     = AggSecurityConfig.requireString(args, "cycle_id");
        String reason      = AggSecurityConfig.sanitise(AggSecurityConfig.getString(args,"reason"),"reason");

        // Check lock (409 equivalent)
        if (AggSecurityConfig.isLocked(candidateId, cycleId))
            return resp("CONFLICT", "Aggregation already in progress — retry later",
                    Map.of("candidate_id",candidateId,"cycle_id",cycleId,"status","IN_PROGRESS"));

        Optional<AggModels.AggregateRecord> existing = repo.getAggregate(tenantId, candidateId, cycleId);
        int currentRetry = existing.map(r -> r.retryCount).orElse(0) + 1;

        repo.audit("AGGREGATE_RETRY_TRIGGERED", candidateId, cycleId, tenantId,
                "retry=" + currentRetry + " reason=" + reason + " by=" + claims.get("sub"), true);

        try {
            AggModels.AggregateRecord record = engine.aggregate(tenantId, candidateId, cycleId, true);
            record.retryCount = currentRetry;
            repo.saveAggregate(record);
            return resp("ACCEPTED", "Retry aggregation complete (attempt " + currentRetry + ")", record.toMap());
        } catch (Exception e) {
            repo.audit("AGGREGATE_RETRY_FAILED", candidateId, cycleId, tenantId, "reason=" + e.getMessage(), false);
            return resp("FAILED", "Retry failed: " + e.getMessage(),
                    Map.of("candidate_id",candidateId,"cycle_id",cycleId,"retry_attempt",currentRetry));
        }
    }

    public String batchCompute(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_SERVICE_ACCOUNT, AggSecurityConfig.ROLE_ADMIN);
        String tenantId      = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateCsv  = AggSecurityConfig.requireString(args, "candidate_ids");
        String cycleId       = AggSecurityConfig.requireString(args, "cycle_id");
        boolean forceRecomp  = AggSecurityConfig.getBool(args, "force_recompute", false);

        List<String> ids = Arrays.stream(candidateCsv.split(","))
                .map(String::trim).filter(s->!s.isEmpty()).collect(Collectors.toList());
        if (ids.size() > 50) throw new IllegalArgumentException("Batch limited to 50 candidates");

        int computed=0, skipped=0, failed=0;
        List<Map<String,Object>> results = new ArrayList<>();
        for (String cid : ids) {
            try {
                Optional<AggModels.AggregateRecord> ex = repo.getAggregate(tenantId, cid, cycleId);
                if (ex.isPresent() && ex.get().status == AggModels.AggStatus.COMPUTED && !forceRecomp) {
                    skipped++; results.add(Map.of("candidate_id",cid,"status","SKIPPED_ALREADY_COMPUTED"));
                } else {
                    AggModels.AggregateRecord r = engine.aggregate(tenantId, cid, cycleId, forceRecomp);
                    computed++; results.add(Map.of("candidate_id",cid,"status","COMPUTED",
                            "normalized_score",r.normalizedScore,"belt",r.beltEligibleLevel.name()));
                }
            } catch (Exception e) {
                failed++; results.add(Map.of("candidate_id",cid,"status","FAILED","error",e.getMessage()));
            }
        }
        return resp("OK", computed + "/" + ids.size() + " aggregated", Map.of(
                "total",ids.size(),"computed",computed,"skipped",skipped,"failed",failed,"results",results));
    }

    public String getAggregationStatus(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId     = AggSecurityConfig.requireString(args, "cycle_id");

        if (AggSecurityConfig.hasRole(claims, AggSecurityConfig.ROLE_CANDIDATE)
                && !AggSecurityConfig.hasRole(claims, AggSecurityConfig.ROLE_ADMIN)) {
            if (!candidateId.equals(claims.get("sub")))
                throw new SecurityException("CANDIDATE can only view own aggregation status");
        }

        Optional<AggModels.AggregateRecord> opt = repo.getAggregate(tenantId, candidateId, cycleId);
        if (opt.isEmpty()) {
            boolean locked = AggSecurityConfig.isLocked(candidateId, cycleId);
            return resp("NOT_FOUND", "No aggregation found for candidate+cycle",
                    Map.of("candidate_id",candidateId,"cycle_id",cycleId,
                            "status", locked ? "IN_PROGRESS" : "PENDING"));
        }
        return resp("OK", "Aggregation status retrieved", opt.get().toMap());
    }

    Map<String,Object> auth(Map<String,Object> a){ return AggSecurityConfig.validateToken(AggSecurityConfig.requireString(a,"auth_token")); }
    String resp(String s,String m,Object d){ Map<String,Object> r=new LinkedHashMap<>();r.put("status",s);r.put("message",m);r.put("data",d);r.put("service","candidate-performance-aggregator");return JsonUtil.toJson(r); }
}

// ─────────────────────────────────────────────────────────────────────────────
// RubricConfigTools — configure_rubric, get_rubric, configure_cycle, get_cycle_config
// ─────────────────────────────────────────────────────────────────────────────
class RubricConfigTools {

    private final AggRepository repo = AggRepository.getInstance();
    private int rubricVersionCounter = 1;

    public String configureRubric(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String jobRoleId   = AggSecurityConfig.getString(args, "job_role_id");
        if (jobRoleId == null || jobRoleId.isBlank()) jobRoleId = "default";

        double gdW   = AggSecurityConfig.getDouble(args, "gd_weight",          AggSecurityConfig.getDefaultGdWeight());
        double intW  = AggSecurityConfig.getDouble(args, "interview_weight",    AggSecurityConfig.getDefaultIntWeight());
        double dojoW = AggSecurityConfig.getDouble(args, "dojo_weight",         AggSecurityConfig.getDefaultDojoWeight());
        AggSecurityConfig.validateWeights(gdW, intW, dojoW);

        double bronze   = AggSecurityConfig.getDouble(args, "bronze_threshold",   0);
        double silver   = AggSecurityConfig.getDouble(args, "silver_threshold",   50);
        double gold     = AggSecurityConfig.getDouble(args, "gold_threshold",     70);
        double platinum = AggSecurityConfig.getDouble(args, "platinum_threshold", 85);

        if (!(bronze < silver && silver < gold && gold < platinum))
            throw new IllegalArgumentException("Thresholds must be increasing: bronze < silver < gold < platinum");

        String version = "v" + (rubricVersionCounter++);
        AggModels.ScoringRubric rubric = new AggModels.ScoringRubric(tenantId, jobRoleId, gdW, intW, dojoW,
                bronze, silver, gold, platinum, version);
        repo.saveRubric(rubric);
        repo.audit("RUBRIC_CONFIGURED", "RUBRIC", tenantId, tenantId,
                "role=" + jobRoleId + " gd=" + gdW + " int=" + intW + " dojo=" + dojoW + " v=" + version, true);
        return resp("CREATED", "Scoring rubric configured", rubric.toMap());
    }

    public String getRubric(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_RECRUITER, AggSecurityConfig.ROLE_ADMIN);
        String tenantId   = AggSecurityConfig.effectiveTenant(claims, args);
        String jobRoleId  = AggSecurityConfig.getString(args, "job_role_id");
        if (jobRoleId == null || jobRoleId.isBlank()) jobRoleId = "default";

        Optional<AggModels.ScoringRubric> opt = repo.getRubric(tenantId, jobRoleId);
        if (opt.isEmpty()) {
            Map<String,Object> defaults = new LinkedHashMap<>();
            defaults.put("source","DEFAULT (no rubric configured — platform defaults applied)");
            defaults.put("tenant_id",tenantId); defaults.put("job_role_id",jobRoleId);
            defaults.put("weights",Map.of("gd",AggSecurityConfig.getDefaultGdWeight(),
                    "interview",AggSecurityConfig.getDefaultIntWeight(),"dojo",AggSecurityConfig.getDefaultDojoWeight()));
            defaults.put("thresholds",Map.of("bronze",0,"silver",50,"gold",70,"platinum",85));
            return resp("OK", "Default rubric (no tenant config)", defaults);
        }
        return resp("OK", "Rubric retrieved", opt.get().toMap());
    }

    public String configureCycle(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String cycleId     = AggSecurityConfig.requireString(args, "cycle_id");
        String jobRoleId   = AggSecurityConfig.getString(args, "job_role_id");
        String tracksStr   = AggSecurityConfig.getString(args, "tracks");
        String status      = AggSecurityConfig.getString(args, "status");

        // Parse tracks
        List<AggModels.AssessmentTrack> tracks = new ArrayList<>();
        if (tracksStr != null && !tracksStr.isBlank()) {
            for (String t : tracksStr.split(",")) {
                try { tracks.add(AggModels.AssessmentTrack.valueOf(t.trim().toUpperCase())); }
                catch (Exception e) { throw new IllegalArgumentException("Invalid track: " + t); }
            }
        } else {
            tracks = Arrays.asList(AggModels.AssessmentTrack.values());
        }

        // Parse optional weight overrides
        Map<String,Double> overrides = new LinkedHashMap<>();
        String gdOv  = AggSecurityConfig.getString(args, "gd_weight");
        String intOv = AggSecurityConfig.getString(args, "interview_weight");
        String djOv  = AggSecurityConfig.getString(args, "dojo_weight");
        if (gdOv != null)  overrides.put("GD",        Double.parseDouble(gdOv));
        if (intOv != null) overrides.put("INTERVIEW",  Double.parseDouble(intOv));
        if (djOv != null)  overrides.put("DOJO",       Double.parseDouble(djOv));
        if (!overrides.isEmpty()) {
            double s = overrides.values().stream().mapToDouble(Double::doubleValue).sum();
            if (Math.abs(s - 1.0) > 0.001) throw new IllegalArgumentException("Cycle weight overrides must sum to 1.0");
        }

        AggModels.CycleConfig config = new AggModels.CycleConfig(cycleId, tenantId, jobRoleId, tracks, overrides, status);
        repo.saveCycleConfig(config);
        repo.audit("CYCLE_CONFIGURED", "CYCLE:" + cycleId, cycleId, tenantId,
                "tracks=" + tracks.size() + " overrides=" + overrides.size(), true);
        return resp("CREATED", "Cycle configuration saved (Redis cycle_config + PostgreSQL)", config.toMap());
    }

    public String getCycleConfig(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_RECRUITER, AggSecurityConfig.ROLE_ADMIN);
        String cycleId = AggSecurityConfig.requireString(args, "cycle_id");

        Optional<AggModels.CycleConfig> opt = repo.getCycleConfig(cycleId);
        if (opt.isEmpty()) return resp("NOT_FOUND", "No config found for cycle: " + cycleId,
                Map.of("cycle_id",cycleId));
        return resp("OK", "Cycle config retrieved", opt.get().toMap());
    }

    Map<String,Object> auth(Map<String,Object> a){ return AggSecurityConfig.validateToken(AggSecurityConfig.requireString(a,"auth_token")); }
    String resp(String s,String m,Object d){ Map<String,Object> r=new LinkedHashMap<>();r.put("status",s);r.put("message",m);r.put("data",d);r.put("service","candidate-performance-aggregator");return JsonUtil.toJson(r); }
}

// ─────────────────────────────────────────────────────────────────────────────
// QueryDlqTools — get_aggregate_result, get_candidate_breakdown,
//                 get_aggregation_history, get_score_history,
//                 get_dlq_entries, resolve_dlq_entry,
//                 get_metrics, get_audit_log
// ─────────────────────────────────────────────────────────────────────────────
class QueryDlqTools {

    private final AggRepository     repo   = AggRepository.getInstance();
    private final AggregationEngine engine = new AggregationEngine();

    public String getAggregateResult(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId     = AggSecurityConfig.requireString(args, "cycle_id");

        enforceCandidateSelfAccess(claims, candidateId);

        Optional<AggModels.AggregateRecord> opt = repo.getAggregate(tenantId, candidateId, cycleId);
        if (opt.isEmpty()) return resp("NOT_FOUND", "No aggregate result yet for candidate+cycle",
                Map.of("candidate_id",candidateId,"cycle_id",cycleId,"hint","Call compute_aggregate first"));
        if (opt.get().status != AggModels.AggStatus.COMPUTED)
            return resp("PENDING", "Aggregation not yet complete. Status: " + opt.get().status.name(), opt.get().toMap());
        return resp("OK", "Aggregate result retrieved", opt.get().toMap());
    }

    public String getCandidateBreakdown(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.requireString(args, "candidate_id");
        String cycleId     = AggSecurityConfig.requireString(args, "cycle_id");

        enforceCandidateSelfAccess(claims, candidateId);

        Optional<AggModels.AggregateRecord> opt = repo.getAggregate(tenantId, candidateId, cycleId);
        if (opt.isEmpty()) return resp("NOT_FOUND", "No aggregate found",
                Map.of("candidate_id",candidateId,"cycle_id",cycleId));

        AggModels.AggregateRecord record = opt.get();
        List<Map<String,Object>> breakdown = record.perTrackBreakdown.stream()
                .map(AggModels.TrackBreakdown::toMap).collect(Collectors.toList());

        Map<String,Object> data = new LinkedHashMap<>();
        data.put("candidate_id",      candidateId);
        data.put("cycle_id",          cycleId);
        data.put("normalized_score",  record.normalizedScore);
        data.put("belt_eligible_level",record.beltEligibleLevel.name());
        data.put("per_track_breakdown",breakdown);
        data.put("tracks_participated",record.tracksParticipated.stream().map(Enum::name).collect(Collectors.toList()));
        data.put("weights_used",      Map.of("gd",record.weightsGd,"interview",record.weightsInterview,"dojo",record.weightsDojo));
        data.put("rubric_version",    record.rubricVersion);
        data.put("dpdpa_note",        "Score breakdown provided for transparency per DPDPA 2023 right to explanation");
        return resp("OK", "Score explainability breakdown retrieved", data);
    }

    public String getAggregationHistory(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_RECRUITER, AggSecurityConfig.ROLE_ADMIN);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.getString(args, "candidate_id");
        String cycleIdFilt = AggSecurityConfig.getString(args, "cycle_id");
        String beltFilter  = AggSecurityConfig.getString(args, "belt_level");
        int limit          = Math.min(AggSecurityConfig.getInt(args,"limit",50), 500);

        List<AggModels.AggregateRecord> all = repo.getAggregates(tenantId);
        if (candidateId != null) all = all.stream().filter(a -> candidateId.equals(a.candidateId)).collect(Collectors.toList());
        if (cycleIdFilt != null) all = all.stream().filter(a -> cycleIdFilt.equals(a.cycleId)).collect(Collectors.toList());
        if (beltFilter != null && !beltFilter.isBlank()) {
            try { AggModels.BeltLevel bf = AggModels.BeltLevel.valueOf(beltFilter.toUpperCase());
                  all = all.stream().filter(a -> a.beltEligibleLevel == bf).collect(Collectors.toList()); }
            catch (Exception ignored) {}
        }

        List<Map<String,Object>> items = all.stream().limit(limit).map(AggModels.AggregateRecord::toMap).collect(Collectors.toList());
        return resp("OK", items.size() + " aggregation records", Map.of("total",items.size(),"records",items));
    }

    public String getScoreHistory(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.requireString(args, "candidate_id");
        String trackFilter = AggSecurityConfig.getString(args, "track");
        int limit          = Math.min(AggSecurityConfig.getInt(args,"limit",50), 500);

        enforceCandidateSelfAccess(claims, candidateId);

        // All track scores for this candidate in this tenant
        List<AggModels.TrackScore> all = new ArrayList<>();
        // Pull from all cycles for this candidate
        for (AggModels.AggregateRecord agg : repo.getAggregates(tenantId)) {
            if (agg.candidateId.equals(candidateId)) {
                all.addAll(repo.getTrackScores(tenantId, candidateId, agg.cycleId));
            }
        }
        // Unique by score_id
        Map<String,AggModels.TrackScore> unique = new LinkedHashMap<>();
        for (AggModels.TrackScore s : all) unique.put(s.scoreId, s);

        List<AggModels.TrackScore> filtered = new ArrayList<>(unique.values());
        if (trackFilter != null && !trackFilter.isBlank() && !"ALL".equalsIgnoreCase(trackFilter)) {
            AggModels.AssessmentTrack tf = AggModels.AssessmentTrack.fromString(trackFilter);
            filtered = filtered.stream().filter(s -> s.track == tf).collect(Collectors.toList());
        }
        filtered.sort(Comparator.comparing((AggModels.TrackScore s) -> s.scoredAt).reversed());
        List<Map<String,Object>> items = filtered.stream().limit(limit).map(AggModels.TrackScore::toMap).collect(Collectors.toList());
        return resp("OK", items.size() + " track scores", Map.of("candidate_id",candidateId,"total",items.size(),"scores",items));
    }

    public String getDlqEntries(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);
        String tenantId   = AggSecurityConfig.effectiveTenant(claims, args);
        String statusFilt = AggSecurityConfig.getString(args, "status");
        if (statusFilt == null) statusFilt = "PENDING";

        List<AggModels.DlqEntry> entries = repo.getDlqEntries(tenantId);
        final String sf = statusFilt;
        if (!"ALL".equalsIgnoreCase(sf))
            entries = entries.stream().filter(e -> sf.equalsIgnoreCase(e.status)).collect(Collectors.toList());

        List<Map<String,Object>> items = entries.stream().map(AggModels.DlqEntry::toMap).collect(Collectors.toList());
        return resp("OK", items.size() + " DLQ entries (status=" + statusFilt + ")",
                Map.of("total",items.size(),"status_filter",statusFilt,"entries",items));
    }

    public String resolveDlqEntry(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);
        String tenantId = AggSecurityConfig.effectiveTenant(claims, args);
        String dlqId    = AggSecurityConfig.requireString(args, "dlq_id");
        String action   = AggSecurityConfig.requireString(args, "action").toUpperCase();
        String notes    = AggSecurityConfig.sanitise(AggSecurityConfig.getString(args,"notes"),"notes");

        if (!action.equals("RESOLVED") && !action.equals("REQUEUED"))
            throw new IllegalArgumentException("action must be RESOLVED or REQUEUED");

        Optional<AggModels.DlqEntry> opt = repo.getDlqEntry(dlqId);
        if (opt.isEmpty()) throw new IllegalArgumentException("DLQ entry not found: " + dlqId);
        AggModels.DlqEntry entry = opt.get();

        if (!tenantId.equals(entry.tenantId)) throw new SecurityException("Tenant mismatch for DLQ entry");

        repo.updateDlqStatus(dlqId, action);

        Map<String,Object> result = new LinkedHashMap<>(entry.toMap());
        result.put("action",     action);
        result.put("resolved_by",claims.get("sub"));
        result.put("resolved_at",Instant.now().toString());
        result.put("notes",      notes);

        if ("REQUEUED".equals(action)) {
            // Trigger retry
            try {
                AggregationEngine eng = new AggregationEngine();
                AggModels.AggregateRecord r = eng.aggregate(entry.tenantId, entry.candidateId, entry.cycleId, true);
                result.put("requeue_result", Map.of("status","COMPUTED","normalized_score",r.normalizedScore));
            } catch (Exception e) {
                result.put("requeue_result", Map.of("status","FAILED","error",e.getMessage()));
            }
        }

        repo.audit("DLQ_RESOLVED", entry.candidateId, entry.cycleId, tenantId,
                "dlq_id=" + dlqId + " action=" + action + " by=" + claims.get("sub"), true);
        repo.logKafkaEvent("candidate.performance.aggregated.dlq.resolved",
                Map.of("dlq_id",dlqId,"action",action,"candidate_id",entry.candidateId,"tenant_id",tenantId));

        return resp("OK", "DLQ entry " + action.toLowerCase(), result);
    }

    public String getMetrics(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN, AggSecurityConfig.ROLE_SERVICE_ACCOUNT);
        String tenantId = AggSecurityConfig.effectiveTenant(claims, args);

        Map<String,Object> metrics = repo.getMetrics(tenantId);
        metrics.put("server",      com.ecoskiller.mcp.CandidatePerformanceAggregatorServer.SERVER_NAME);
        metrics.put("version",     com.ecoskiller.mcp.CandidatePerformanceAggregatorServer.SERVER_VERSION);
        metrics.put("namespace",   "ecoskiller/prod");
        metrics.put("health",      Map.of("kafka","UP","postgres","UP","redis","UP","clickhouse","UP"));
        metrics.put("checked_at",  Instant.now().toString());
        return resp("OK", "Prometheus metrics retrieved", metrics);
    }

    public String getAuditLog(Map<String,Object> args) {
        Map<String,Object> claims = auth(args);
        AggSecurityConfig.requireRole(claims, AggSecurityConfig.ROLE_ADMIN);
        String tenantId    = AggSecurityConfig.effectiveTenant(claims, args);
        String candidateId = AggSecurityConfig.getString(args, "candidate_id");
        int limit          = Math.min(AggSecurityConfig.getInt(args,"limit",100), 1000);

        List<Map<String,Object>> log = repo.getAuditLog(tenantId, candidateId, limit);
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("total",         log.size());
        data.put("limit",         limit);
        data.put("candidate_id",  candidateId != null ? candidateId : "ALL");
        data.put("entries",       log);
        data.put("persistence",   "ClickHouse ecoskiller.perf_agg_audit_log — DPDPA 2023 3-year retention");
        return resp("OK", log.size() + " audit entries", data);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void enforceCandidateSelfAccess(Map<String,Object> claims, String candidateId) {
        if (AggSecurityConfig.hasRole(claims, AggSecurityConfig.ROLE_CANDIDATE)
                && !AggSecurityConfig.hasRole(claims, AggSecurityConfig.ROLE_ADMIN)) {
            if (!candidateId.equals(claims.get("sub")))
                throw new SecurityException("CANDIDATE can only view own data");
        }
    }

    Map<String,Object> auth(Map<String,Object> a){ return AggSecurityConfig.validateToken(AggSecurityConfig.requireString(a,"auth_token")); }
    String resp(String s,String m,Object d){ Map<String,Object> r=new LinkedHashMap<>();r.put("status",s);r.put("message",m);r.put("data",d);r.put("service","candidate-performance-aggregator");return JsonUtil.toJson(r); }
}
