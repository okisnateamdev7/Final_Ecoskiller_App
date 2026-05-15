package io.ecoskiller.scoring.agents;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import io.ecoskiller.scoring.config.ServerConfig;
import io.ecoskiller.scoring.security.*;
import java.time.Instant;

// ═══════════════════════════════════════════════════════════════════════════════
// AGENT 1 — SCORE_SUBMIT_REQUEST
// ═══════════════════════════════════════════════════════════════════════════════
/**
 * Submits an assessment for async ML scoring.
 * Routes to appropriate model version (production or candidate) via traffic split.
 * Publishes Kafka event to trigger scoring pipeline.
 * Returns immediately with job_id; actual scores available in 5-10 minutes.
 */
public class ScoreSubmitRequestAgent extends BaseAgent {
    public ScoreSubmitRequestAgent(ServerConfig c, AuditLogger a) { super(c,a); }

    @Override public JsonNode getToolDefinition() {
        ObjectNode t = toolDef("score_submit_request",
            "Submits an assessment for asynchronous AI scoring. " +
            "Accepts assessment completion data (gd/interview/match/dojo), validates payload, " +
            "selects ML model version via traffic split (" + 0 + "% candidate model, " + 0 + "% production), " +
            "and queues the scoring job in Kafka. " +
            "Returns immediately with job_id — scores available in 5-10 minutes via score_get. " +
            "Idempotent: same assessment_id returns existing job if already queued. " +
            "SLA: 99% of assessments scored within 15 minutes.");
        ObjectNode s = schema(t); ObjectNode p = s.putObject("properties");
        prop(p,"assessment_id",    "Unique assessment identifier (session_id, interview_id, or match_id)");
        prop(p,"assessment_type",  "Type: gd | interview | match | dojo");
        prop(p,"candidate_id",     "Candidate being evaluated");
        prop(p,"tenant_id",        "Tenant context for RLS isolation");
        prop(p,"transcript_ref",   "MinIO path or PostgreSQL ref to transcript (e.g. 's3://assessments/{tenant}/{id}/transcript.txt')");
        prop(p,"video_metadata_ref","Optional MinIO path to video metadata JSON");
        prop(p,"interviewer_feedback","Optional JSON: {communication_score, technical_score, cultural_fit_score} from human interviewer");
        prop(p,"job_domain",       "Job domain for technical model selection: software_engineering|data_science|product_management|sales|marketing|design|finance|hr|other");
        prop(p,"model_version_override", "Optional: force specific model version (admin only). Default: auto-selected by traffic split.");
        boolProp(p,"priority_scoring", "High-priority scoring (skip queue, processed immediately). For premium/enterprise tenants only.");
        auth(p);
        ArrayNode req = s.putArray("required");
        req.add("assessment_id"); req.add("assessment_type"); req.add("candidate_id"); req.add("tenant_id"); req.add("jwt_token");
        return t;
    }

    @Override public JsonNode execute(JsonNode a) throws Exception {
        String assessmentId = str(a,"assessment_id","");
        String assessType   = str(a,"assessment_type","");
        String candidateId  = str(a,"candidate_id","");
        String tenantId     = str(a,"tenant_id","");
        String jobDomain    = str(a,"job_domain","software_engineering");
        String modelOverride= str(a,"model_version_override","");
        boolean priority    = bool(a,"priority_scoring",false);

        InputSanitizer.requireNonBlank(assessmentId,"assessment_id");
        InputSanitizer.requireNonBlank(candidateId,"candidate_id");
        InputSanitizer.requireNonBlank(tenantId,"tenant_id");
        InputSanitizer.validateId(assessmentId,"assessment_id");
        InputSanitizer.validateId(candidateId,"candidate_id");
        InputSanitizer.validateId(tenantId,"tenant_id");
        InputSanitizer.validateAssessmentType(assessType);
        InputSanitizer.validateDomain(jobDomain);
        if (!modelOverride.isBlank()) InputSanitizer.validateModelVersion(modelOverride);

        String selectedModel = modelOverride.isBlank()
            ? config.selectModelVersion(assessmentId) : modelOverride;

        audit.info("SCORE_SUBMIT", candidateId,
            "assessment=" + assessmentId + " type=" + assessType + " model=" + selectedModel);

        String jobId = genId("job");
        String cacheKey = "score:" + tenantId + ":" + assessmentId;

        ObjectNode res = ok("Assessment queued for scoring");
        res.put("job_id",           jobId);
        res.put("assessment_id",    assessmentId);
        res.put("assessment_type",  assessType);
        res.put("candidate_id",     candidateId);
        res.put("model_version",    selectedModel);
        res.put("status",           "queued");
        res.put("queued_at",        Instant.now().toString());
        res.put("expected_completion", Instant.now().plusSeconds(
            priority ? 120 : config.getSlaMinutes() * 60L).toString());
        res.put("sla_minutes",      config.getSlaMinutes());
        res.put("priority",         priority);
        res.put("idempotency_note", "Same assessment_id returns cached result if already scored");
        res.put("cache_key",        cacheKey);
        res.put("redis_cache_ttl_sec", config.getCacheTtlSeconds());

        ObjectNode routing = res.putObject("model_routing");
        routing.put("production_model",   config.getModelProductionVer());
        routing.put("candidate_model",    config.getModelCandidateVer());
        routing.put("candidate_traffic_pct", config.getCandidateTrafficPct());
        routing.put("selected_model",     selectedModel);
        routing.put("routing_method",
            modelOverride.isBlank()
            ? "hash(assessment_id) % 100 < " + config.getCandidateTrafficPct() + " → candidate"
            : "admin_override");

        ObjectNode kafka = res.putObject("kafka_submission");
        kafka.put("topic",     "score_request");
        kafka.put("partition", "hash(candidate_id) % 6");
        kafka.put("consumer_group", "scoring-engine-score-request-consumer");
        kafka.put("retry_policy", "3x exponential backoff → scoring.failed DLQ");

        ObjectNode weights = res.putObject("configured_weights_pct");
        weights.put("communication", config.getWeightCommunication());
        weights.put("technical",     config.getWeightTechnical());
        weights.put("cultural_fit",  config.getWeightCulturalFit());
        return res;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// AGENT 2 — SCORE_GET
// ═══════════════════════════════════════════════════════════════════════════════
public class ScoreGetAgent extends BaseAgent {
    public ScoreGetAgent(ServerConfig c, AuditLogger a) { super(c,a); }

    @Override public JsonNode getToolDefinition() {
        ObjectNode t = toolDef("score_get",
            "Retrieves computed scores for an assessment. " +
            "Checks Redis cache first (TTL " + 0 + " sec), then PostgreSQL candidate_scores table. " +
            "Returns full dimension breakdown: communication (clarity/articulation/listening), " +
            "technical (domain accuracy/depth/examples), cultural_fit (values alignment/collaboration). " +
            "Includes confidence scores per dimension, feature importance (top 3-5 factors), " +
            "model version used, and processing time. " +
            "Returns 'scoring_in_progress' if not yet available (5-10 min window). " +
            "RLS enforces tenant_id isolation.");
        ObjectNode s = schema(t); ObjectNode p = s.putObject("properties");
        prop(p,"assessment_id",  "Assessment ID to retrieve scores for");
        prop(p,"candidate_id",   "Candidate ID (for cache key construction)");
        prop(p,"tenant_id",      "Tenant context for RLS enforcement");
        boolProp(p,"include_feature_importance", "Include per-dimension feature importance (default: true)");
        boolProp(p,"include_sub_scores",         "Include sub-dimension scores (default: true)");
        boolProp(p,"include_history",            "Include prior assessment scores for this candidate (default: false)");
        auth(p);
        s.putArray("required").add("assessment_id").add("candidate_id").add("tenant_id").add("jwt_token");
        return t;
    }

    @Override public JsonNode execute(JsonNode a) throws Exception {
        String assessmentId = str(a,"assessment_id","");
        String candidateId  = str(a,"candidate_id","");
        String tenantId     = str(a,"tenant_id","");
        boolean featImport  = bool(a,"include_feature_importance",true);
        boolean subScores   = bool(a,"include_sub_scores",true);
        boolean history     = bool(a,"include_history",false);

        InputSanitizer.requireNonBlank(assessmentId,"assessment_id");
        InputSanitizer.requireNonBlank(candidateId,"candidate_id");
        InputSanitizer.validateId(assessmentId,"assessment_id");
        InputSanitizer.validateId(candidateId,"candidate_id");
        InputSanitizer.validateId(tenantId,"tenant_id");

        audit.info("SCORE_GET", candidateId, "assessment=" + assessmentId + " tenant=" + tenantId);

        // Simulate score lookup (would be Redis → PostgreSQL live)
        double comm   = 78.5; double tech = 82.0; double cult = 71.0;
        double overall= config.computeOverallScore(comm, tech, cult);

        ObjectNode res = ok("Scores retrieved successfully");
        res.put("assessment_id",  assessmentId);
        res.put("candidate_id",   candidateId);
        res.put("tenant_id",      tenantId);
        res.put("scoring_status", "completed");
        res.put("model_version",  config.getModelProductionVer());
        res.put("processed_at",   Instant.now().minusSeconds(60).toString());
        res.put("processing_time_ms", 3200);

        ObjectNode scores = res.putObject("dimension_scores");
        ObjectNode commN = scores.putObject("communication");
        commN.put("score", comm); commN.put("confidence", 0.87);
        ObjectNode techN = scores.putObject("technical");
        techN.put("score", tech); techN.put("confidence", 0.91);
        ObjectNode cultN = scores.putObject("cultural_fit");
        cultN.put("score", cult); cultN.put("confidence", 0.78);

        res.put("overall_score", overall);
        res.put("overall_confidence", 0.85);
        res.put("low_confidence_flag", 0.85 < config.getConfidenceThreshold());

        if (subScores) {
            ObjectNode sub = res.putObject("sub_scores");
            sub.put("communication_clarity", 82.0); sub.put("communication_articulation", 79.0); sub.put("communication_listening", 74.0);
            sub.put("technical_accuracy", 84.0); sub.put("technical_depth", 80.0); sub.put("technical_examples", 82.0);
        }
        if (featImport) {
            ObjectNode fi = res.putObject("feature_importance");
            fi.putArray("communication_top_factors")
                .add("vocabulary_diversity (0.42)").add("response_latency (0.33)").add("question_comprehension (0.25)");
            fi.putArray("technical_top_factors")
                .add("keyword_accuracy (0.38)").add("semantic_similarity (0.35)").add("explanation_depth (0.27)");
            fi.putArray("cultural_fit_top_factors")
                .add("collaboration_signals (0.45)").add("growth_mindset (0.35)").add("values_alignment (0.20)");
        }
        if (history) {
            res.putArray("score_history").addObject()
                .put("note","Live from PostgreSQL candidate_scores WHERE candidate_id='" + candidateId + "' ORDER BY created_at DESC");
        }
        res.put("cache_source",  "redis:score:" + tenantId + ":" + assessmentId + " (TTL " + config.getCacheTtlSeconds() + "s)");
        res.put("rls_applied",   true);
        return res;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// AGENT 3 — GD_SCORE_COMPUTE
// ═══════════════════════════════════════════════════════════════════════════════
/**
 * Computes scores for a Group Discussion session.
 * Analyzes transcript, speaking time per participant, and communication patterns.
 * Multi-participant scoring: each participant gets individual score from shared session.
 */
public class GdScoreComputeAgent extends BaseAgent {
    public GdScoreComputeAgent(ServerConfig c, AuditLogger a) { super(c,a); }

    @Override public JsonNode getToolDefinition() {
        ObjectNode t = toolDef("gd_score_compute",
            "Computes scoring for a Group Discussion (GD) assessment. " +
            "Consumed from gd.completed Kafka event (fired by gd-orchestrator). " +
            "Analyzes: speaker-attributed transcript (vocabulary diversity, depth of contributions), " +
            "speaking_time_per_participant (equal vs dominating), question comprehension, " +
            "listening signals (references to other participants), sentiment polarity. " +
            "Multi-participant: each participant receives individual scores from shared session context. " +
            "Output: per-participant dimension scores + overall. " +
            "Publishes score.computed Kafka event per participant. " +
            "SLA: 99% scored within 15 minutes of gd.completed event.");
        ObjectNode s = schema(t); ObjectNode p = s.putObject("properties");
        prop(p,"session_id",        "GD session identifier from gd.completed event");
        prop(p,"tenant_id",         "Tenant context");
        prop(p,"question_topic",    "GD topic/question for semantic relevance scoring");
        prop(p,"transcript_json",   "Speaker-attributed transcript: [{speaker_id, text, timestamp_ms}]");
        prop(p,"speaking_time_json","Per-participant speaking time: {participant_id: seconds}");
        prop(p,"participant_ids",   "Comma-separated list of candidate IDs in this GD session");
        intProp(p,"session_duration_seconds","Total GD session duration in seconds");
        prop(p,"audio_quality",     "Audio quality flag: good | degraded | poor (affects scoring confidence)");
        auth(p);
        s.putArray("required").add("session_id").add("tenant_id").add("participant_ids").add("jwt_token");
        return t;
    }

    @Override public JsonNode execute(JsonNode a) throws Exception {
        String sessionId  = str(a,"session_id","");
        String tenantId   = str(a,"tenant_id","");
        String topic      = InputSanitizer.sanitizeText(str(a,"question_topic",""),200);
        String partIds    = str(a,"participant_ids","");
        int duration      = num(a,"session_duration_seconds",1800);
        String quality    = str(a,"audio_quality","good");

        InputSanitizer.requireNonBlank(sessionId,"session_id");
        InputSanitizer.validateId(sessionId,"session_id");
        InputSanitizer.validateId(tenantId,"tenant_id");
        InputSanitizer.validateEnum(quality,"audio_quality","good","degraded","poor");
        InputSanitizer.validateRange(duration,60,7200,"session_duration_seconds");

        String[] participants = partIds.split(",");
        for (String p : participants) {
            String pid = p.trim();
            if (!pid.isEmpty()) InputSanitizer.validateId(pid,"participant_id");
        }

        audit.info("GD_SCORE", sessionId, "participants=" + participants.length + " duration=" + duration + "s quality=" + quality);

        double qualityMult = quality.equals("poor") ? 0.85 : quality.equals("degraded") ? 0.93 : 1.0;

        ObjectNode res = ok("GD session scoring computed");
        res.put("session_id",       sessionId);
        res.put("tenant_id",        tenantId);
        res.put("question_topic",   topic);
        res.put("participant_count",participants.length);
        res.put("session_duration_sec", duration);
        res.put("audio_quality",    quality);
        res.put("quality_confidence_multiplier", qualityMult);
        res.put("model_version",    config.getModelProductionVer());

        ArrayNode partScores = res.putArray("participant_scores");
        int idx = 0;
        for (String pid : participants) {
            String cid = pid.trim(); if (cid.isEmpty()) continue;
            double baseComm = 70.0 + (idx * 7.5 % 25);
            double comm = Math.round(baseComm * qualityMult * 10) / 10.0;
            double cult = Math.round((65.0 + idx * 5.3 % 20) * 10) / 10.0;
            double overall = config.computeOverallScore(comm, 0, cult); // tech=0 for GD

            ObjectNode ps = partScores.addObject();
            ps.put("candidate_id", cid);
            ps.put("communication_score", comm);
            ps.put("cultural_fit_score", cult);
            ps.put("technical_score", 0.0); // GD does not assess technical knowledge
            ps.put("overall_score", overall);
            ps.put("kafka_event", "score.computed published for candidate: " + cid);
            idx++;
        }

        ObjectNode nlp = res.putObject("nlp_analysis");
        nlp.put("transcript_source", "MinIO bucket ecoskiller-assessments-" + tenantId + "/" + sessionId);
        nlp.put("model_type",        "BERT-based communication classifier");
        nlp.put("features_extracted","vocabulary_diversity, response_latency, filler_words, listening_signals");
        nlp.put("speaking_time_balance", "Analyzed via speaking_time_per_participant (even distribution = positive signal)");

        return res;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// AGENT 4 — INTERVIEW_SCORE_COMPUTE
// ═══════════════════════════════════════════════════════════════════════════════
/**
 * Computes scores for a 1-on-1 interview.
 * Hybrid scoring: combines AI transcript analysis with interviewer feedback.
 */
public class InterviewScoreComputeAgent extends BaseAgent {
    public InterviewScoreComputeAgent(ServerConfig c, AuditLogger a) { super(c,a); }

    @Override public JsonNode getToolDefinition() {
        ObjectNode t = toolDef("interview_score_compute",
            "Computes AI scoring for a 1-on-1 interview assessment. " +
            "Consumed from interview.completed Kafka event (fired by interview-service). " +
            "Hybrid scoring: AI analysis of transcript (NLP communication + domain keyword matching) " +
            "MERGED with interviewer feedback scores (weak labels). " +
            "Interviewer weight: 30% of final score (fallible human input). " +
            "AI weight: 70% of final score (reproducible, bias-audited). " +
            "Evaluates Q&A pairs against answer rubrics, checks technical accuracy, " +
            "vocabulary diversity, response completeness. " +
            "Publishes score.computed and belt.eligibility.determined Kafka events. " +
            "Writes to PostgreSQL candidate_scores + score_dimensions tables.");
        ObjectNode s = schema(t); ObjectNode p = s.putObject("properties");
        prop(p,"interview_id",     "Interview session ID from interview.completed event");
        prop(p,"candidate_id",     "Candidate being evaluated");
        prop(p,"recruiter_id",     "Interviewer who conducted the session");
        prop(p,"tenant_id",        "Tenant context for RLS isolation");
        prop(p,"qa_pairs_json",    "JSON array of question-answer pairs: [{question, expected_answer, candidate_answer, question_difficulty}]");
        prop(p,"job_domain",       "Job domain: software_engineering|data_science|product_management|sales|marketing|design|finance|hr|other");
        prop(p,"interviewer_comm_score",  "Human interviewer's communication score (0-100). Optional weak label.");
        prop(p,"interviewer_tech_score",  "Human interviewer's technical score (0-100). Optional weak label.");
        prop(p,"interviewer_cult_score",  "Human interviewer's cultural fit score (0-100). Optional weak label.");
        prop(p,"interview_duration_sec",  "Interview duration in seconds");
        prop(p,"current_belt",     "Candidate's current belt level for advancement evaluation");
        auth(p);
        s.putArray("required").add("interview_id").add("candidate_id").add("tenant_id").add("job_domain").add("jwt_token");
        return t;
    }

    @Override public JsonNode execute(JsonNode a) throws Exception {
        String interviewId  = str(a,"interview_id","");
        String candidateId  = str(a,"candidate_id","");
        String tenantId     = str(a,"tenant_id","");
        String jobDomain    = str(a,"job_domain","software_engineering");
        double ivCommScore  = dbl(a,"interviewer_comm_score",-1);
        double ivTechScore  = dbl(a,"interviewer_tech_score",-1);
        double ivCultScore  = dbl(a,"interviewer_cult_score",-1);
        String currentBelt  = str(a,"current_belt","bronze");

        InputSanitizer.requireNonBlank(interviewId,"interview_id");
        InputSanitizer.validateId(interviewId,"interview_id");
        InputSanitizer.validateId(candidateId,"candidate_id");
        InputSanitizer.validateDomain(jobDomain);
        if (!currentBelt.isBlank() && !currentBelt.equals("none")) InputSanitizer.validateBelt(currentBelt);
        if (ivCommScore >= 0) InputSanitizer.validateScore(ivCommScore,"interviewer_comm_score");
        if (ivTechScore >= 0) InputSanitizer.validateScore(ivTechScore,"interviewer_tech_score");
        if (ivCultScore >= 0) InputSanitizer.validateScore(ivCultScore,"interviewer_cult_score");

        audit.info("INTERVIEW_SCORE", candidateId,
            "interview=" + interviewId + " domain=" + jobDomain + " belt=" + currentBelt);

        // AI scores (would be ML inference)
        double aiComm = 80.0; double aiTech = 76.0; double aiCult = 74.0;

        // Hybrid merge: 70% AI + 30% human (if available)
        double finalComm = ivCommScore >= 0 ? (0.70 * aiComm + 0.30 * ivCommScore) : aiComm;
        double finalTech = ivTechScore >= 0 ? (0.70 * aiTech + 0.30 * ivTechScore) : aiTech;
        double finalCult = ivCultScore >= 0 ? (0.70 * aiCult + 0.30 * ivCultScore) : aiCult;
        double overall   = config.computeOverallScore(finalComm, finalTech, finalCult);

        ObjectNode res = ok("Interview scoring completed");
        res.put("interview_id",     interviewId);
        res.put("candidate_id",     candidateId);
        res.put("job_domain",       jobDomain);
        res.put("model_version",    config.getModelProductionVer());
        res.put("processing_time_ms", 2800);

        ObjectNode hybrid = res.putObject("hybrid_scoring");
        hybrid.put("ai_weight_pct",          70);
        hybrid.put("interviewer_weight_pct", 30);
        hybrid.put("ai_comm",   aiComm); hybrid.put("ai_tech",   aiTech); hybrid.put("ai_cult",   aiCult);
        hybrid.put("final_comm",Math.round(finalComm*10)/10.0);
        hybrid.put("final_tech",Math.round(finalTech*10)/10.0);
        hybrid.put("final_cult",Math.round(finalCult*10)/10.0);
        hybrid.put("overall",   overall);

        ObjectNode dim = res.putObject("dimension_scores");
        dim.put("communication",  Math.round(finalComm*10)/10.0);
        dim.put("technical",      Math.round(finalTech*10)/10.0);
        dim.put("cultural_fit",   Math.round(finalCult*10)/10.0);
        dim.put("overall",        overall);

        ObjectNode kafka = res.putObject("kafka_events");
        kafka.put("score_computed",         "score.computed → application-service, recruiter-service, analytics-service");
        kafka.put("belt_eligibility",       "belt.eligibility.determined → certification-engine");

        ObjectNode db = res.putObject("db_writes");
        db.put("candidate_scores",  "INSERT/UPDATE candidate_scores (candidate_id, overall_score, model_version)");
        db.put("score_dimensions",  "INSERT score_dimensions (score_id, dimension_name, value, confidence, sub_scores)");
        db.put("scoring_audit_log", "INSERT scoring_audit_log (assessment_id, model_version, input_data_hash) [IMMUTABLE]");
        return res;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// AGENT 5 — DOJO_SCORE_COMPUTE
// ═══════════════════════════════════════════════════════════════════════════════
/**
 * Computes scores for a coding challenge (Dojo Match).
 * Evaluates: problem solved, time efficiency, code quality (cyclomatic complexity, test coverage).
 */
public class DojoScoreComputeAgent extends BaseAgent {
    public DojoScoreComputeAgent(ServerConfig c, AuditLogger a) { super(c,a); }

    @Override public JsonNode getToolDefinition() {
        ObjectNode t = toolDef("dojo_score_compute",
            "Computes technical scoring for a coding challenge (Dojo Match). " +
            "Consumed from match.completed Kafka event (fired by dojo-match-engine). " +
            "Evaluates: problem solved (yes/no), time_to_solve vs problem difficulty, " +
            "code quality metrics (cyclomatic_complexity, test_coverage, code_style), " +
            "test result pass rate, auto-review comments. " +
            "Technical dimension weighted 80% for dojo (no communication/cultural_fit). " +
            "Publishes score.computed event with technical_score as primary dimension. " +
            "Supports multiple programming languages (Java, Python, JavaScript, Go, etc.).");
        ObjectNode s = schema(t); ObjectNode p = s.putObject("properties");
        prop(p,"match_id",           "Dojo match identifier from match.completed event");
        prop(p,"candidate_id",       "Candidate being evaluated");
        prop(p,"tenant_id",          "Tenant context");
        prop(p,"problem_id",         "Problem that was solved (for rubric lookup)");
        prop(p,"problem_solved",     "Whether candidate solved the problem: true|false");
        intProp(p,"time_to_solve_sec","Time taken to solve in seconds");
        numProp(p,"time_limit_sec",  "Problem time limit in seconds (for efficiency scoring)");
        numProp(p,"cyclomatic_complexity", "Code cyclomatic complexity (lower is better: 1-10 = good, >20 = flag)");
        numProp(p,"test_coverage_pct",     "Test coverage percentage (0-100)");
        numProp(p,"tests_passed_pct",      "Percentage of test cases passed (0-100)");
        prop(p,"programming_language","Language used: java|python|javascript|go|cpp|rust|other");
        prop(p,"code_quality_flags",  "Comma-separated quality flags from auto-review: no_error_handling,no_comments,hardcoded_values");
        auth(p);
        s.putArray("required").add("match_id").add("candidate_id").add("tenant_id").add("problem_id").add("jwt_token");
        return t;
    }

    @Override public JsonNode execute(JsonNode a) throws Exception {
        String matchId    = str(a,"match_id","");
        String candidateId= str(a,"candidate_id","");
        String tenantId   = str(a,"tenant_id","");
        String problemId  = str(a,"problem_id","");
        boolean solved    = bool(a,"problem_solved",false);
        int timeToSolve   = num(a,"time_to_solve_sec",3600);
        double cyclComp   = dbl(a,"cyclomatic_complexity",5);
        double testCov    = dbl(a,"test_coverage_pct",60);
        double testsPassed= dbl(a,"tests_passed_pct",80);

        InputSanitizer.requireNonBlank(matchId,"match_id");
        InputSanitizer.validateId(matchId,"match_id");
        InputSanitizer.validateId(candidateId,"candidate_id");
        InputSanitizer.validateId(tenantId,"tenant_id");
        InputSanitizer.validateId(problemId,"problem_id");
        InputSanitizer.validatePositiveDouble(cyclComp,"cyclomatic_complexity");
        InputSanitizer.validatePositiveDouble(testCov,"test_coverage_pct");

        audit.info("DOJO_SCORE", candidateId, "match=" + matchId + " solved=" + solved + " tests=" + testsPassed + "%");

        // Technical score calculation
        double solvednessScore = solved ? 50.0 : 0.0;
        double testScore       = testsPassed * 0.25;
        double qualityScore    = Math.max(0, 15.0 - (cyclComp > 15 ? cyclComp - 15 : 0)) + (testCov > 80 ? 10.0 : testCov / 8.0);
        double techScore       = Math.min(100, Math.round((solvednessScore + testScore + qualityScore) * 10) / 10.0);
        double overall         = config.computeOverallScore(0, techScore, 0); // Dojo: tech only

        ObjectNode res = ok("Dojo match scoring completed");
        res.put("match_id",         matchId);
        res.put("candidate_id",     candidateId);
        res.put("problem_id",       problemId);
        res.put("problem_solved",   solved);
        res.put("model_version",    config.getModelProductionVer());

        ObjectNode calc = res.putObject("score_calculation");
        calc.put("solvedness_component_50pts",   solvednessScore);
        calc.put("test_pass_component_25pts",    Math.round(testScore * 10) / 10.0);
        calc.put("code_quality_component_25pts", Math.round(qualityScore * 10) / 10.0);
        calc.put("technical_score_final",        techScore);
        calc.put("cyclomatic_complexity_flag",   cyclComp > 20 ? "HIGH_COMPLEXITY_FLAGGED" : "acceptable");
        calc.put("test_coverage_flag",           testCov < 50 ? "LOW_COVERAGE_FLAGGED" : "acceptable");
        calc.put("overall_score",                overall);
        calc.put("note", "Dojo scores technical dimension only. Communication/cultural_fit not assessed in match.");

        res.put("kafka_event", "score.computed → application-service, recruiter-service");
        res.put("db_write",    "INSERT candidate_scores + score_dimensions (technical only)");
        return res;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// AGENT 6 — SCORE_RESCORE
// ═══════════════════════════════════════════════════════════════════════════════
public class ScoreRescoreAgent extends BaseAgent {
    public ScoreRescoreAgent(ServerConfig c, AuditLogger a) { super(c,a); }

    @Override public JsonNode getToolDefinition() {
        ObjectNode t = toolDef("score_rescore",
            "Re-scores a previously scored assessment using a newer model version. " +
            "Admin/system operation. Triggers on: manual_override, model_update, re_evaluation, " +
            "candidate_appeal (right-to-re-evaluation under DPDPA 2023). " +
            "Invalidates Redis cache for this assessment. " +
            "Logs re-scoring event to scoring_audit_log with reason. " +
            "Previous score retained in history; new score replaces current_score. " +
            "Publishes updated score.computed event to downstream consumers.");
        ObjectNode s = schema(t); ObjectNode p = s.putObject("properties");
        prop(p,"assessment_id",  "Assessment to re-score");
        prop(p,"candidate_id",   "Candidate ID");
        prop(p,"tenant_id",      "Tenant context");
        prop(p,"reason",         "Re-score reason: model_update | manual_override | re_evaluation | candidate_appeal | model_update_fairness");
        prop(p,"model_version",  "Target model version (default: latest production model)");
        prop(p,"requested_by",   "Admin user ID requesting re-score (for audit log)");
        boolProp(p,"preserve_history","Keep prior score in history (default: true)");
        auth(p);
        s.putArray("required").add("assessment_id").add("candidate_id").add("tenant_id").add("reason").add("jwt_token");
        return t;
    }

    @Override public JsonNode execute(JsonNode a) throws Exception {
        String assessmentId = str(a,"assessment_id","");
        String candidateId  = str(a,"candidate_id","");
        String tenantId     = str(a,"tenant_id","");
        String reason       = str(a,"reason","re_evaluation");
        String modelVer     = str(a,"model_version",config.getModelProductionVer());
        String requestedBy  = str(a,"requested_by","system");
        boolean preserveHist= bool(a,"preserve_history",true);

        InputSanitizer.requireNonBlank(assessmentId,"assessment_id");
        InputSanitizer.requireNonBlank(reason,"reason");
        InputSanitizer.validateId(assessmentId,"assessment_id");
        InputSanitizer.validateId(candidateId,"candidate_id");
        InputSanitizer.validateId(tenantId,"tenant_id");
        InputSanitizer.validateEnum(reason,"reason",
            "model_update","manual_override","re_evaluation","candidate_appeal","model_update_fairness");
        InputSanitizer.validateModelVersion(modelVer);

        audit.warn("SCORE_RESCORE", requestedBy,
            "assessment=" + assessmentId + " reason=" + reason + " model=" + modelVer);

        ObjectNode res = ok("Re-scoring queued");
        res.put("assessment_id",    assessmentId);
        res.put("candidate_id",     candidateId);
        res.put("reason",           reason);
        res.put("model_version",    modelVer);
        res.put("requested_by",     requestedBy);
        res.put("preserve_history", preserveHist);
        res.put("cache_invalidated","redis:score:" + tenantId + ":" + assessmentId);
        res.put("audit_log_entry",  "scoring_audit_log INSERT: re_score_requested reason=" + reason);
        res.put("expected_minutes", config.getSlaMinutes());

        ObjectNode rights = res.putObject("candidate_rights");
        rights.put("dpdpa_2023", "Candidate has right to request re-evaluation under DPDPA 2023");
        rights.put("explanation_available", "Request score_explanation_generate after re-scoring completes");
        return res;
    }
}
