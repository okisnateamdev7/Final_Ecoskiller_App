package io.ecoskiller.scoring.config;

import java.util.logging.Logger;

/**
 * All configuration from environment variables / Kubernetes Secrets.
 *
 * Required:
 *   SCORING_JWT_SECRET            — HMAC-SHA256 Keycloak key
 *
 * Optional:
 *   SCORING_DB_HOST               — PostgreSQL host (analytics namespace)
 *   SCORING_REDIS_ENABLED         — default: true
 *   SCORING_AUDIT_ENABLED         — default: true
 *   SCORING_RATE_PER_SEC          — default: 100
 *   SCORING_RATE_PER_MIN          — default: 1000
 *   SCORING_SLA_MINUTES           — default: 15 (scoring SLA window)
 *   SCORING_CACHE_TTL_SECONDS     — default: 3600 (1 hour)
 *   SCORING_CONFIDENCE_THRESHOLD  — default: 0.6
 *   SCORING_WEIGHT_COMMUNICATION  — default: 35 (%)
 *   SCORING_WEIGHT_TECHNICAL      — default: 40 (%)
 *   SCORING_WEIGHT_CULTURAL_FIT   — default: 25 (%)
 *   SCORING_FAIRNESS_THRESHOLD    — default: 0.8 (disparate impact ratio)
 *   SCORING_BIAS_VARIANCE_MAX     — default: 5 (% max score variance by demographic)
 *   SCORING_MODEL_PRODUCTION_VER  — default: v1
 *   SCORING_MODEL_CANDIDATE_VER   — default: v2
 *   SCORING_CANDIDATE_TRAFFIC_PCT — default: 10 (% traffic to candidate model)
 *   SCORING_MAX_RETRIES           — default: 3
 *   SCORING_NAMESPACE             — default: analytics
 */
public final class ServerConfig {

    private static final Logger LOG = Logger.getLogger(ServerConfig.class.getName());

    private final String jwtSecret;
    private final String dbHost;
    private final boolean redisEnabled;
    private final boolean auditEnabled;
    private final int rateLimitPerSec;
    private final int rateLimitPerMin;
    private final int slaMinutes;
    private final int cacheTtlSeconds;
    private final double confidenceThreshold;
    private final double weightCommunication;
    private final double weightTechnical;
    private final double weightCulturalFit;
    private final double fairnessThreshold;
    private final double biasVarianceMax;
    private final String modelProductionVer;
    private final String modelCandidateVer;
    private final int candidateTrafficPct;
    private final int maxRetries;
    private final String namespace;

    private ServerConfig(Builder b) {
        this.jwtSecret = b.jwtSecret; this.dbHost = b.dbHost;
        this.redisEnabled = b.redisEnabled; this.auditEnabled = b.auditEnabled;
        this.rateLimitPerSec = b.rateLimitPerSec; this.rateLimitPerMin = b.rateLimitPerMin;
        this.slaMinutes = b.slaMinutes; this.cacheTtlSeconds = b.cacheTtlSeconds;
        this.confidenceThreshold = b.confidenceThreshold;
        this.weightCommunication = b.weightCommunication;
        this.weightTechnical = b.weightTechnical;
        this.weightCulturalFit = b.weightCulturalFit;
        this.fairnessThreshold = b.fairnessThreshold; this.biasVarianceMax = b.biasVarianceMax;
        this.modelProductionVer = b.modelProductionVer; this.modelCandidateVer = b.modelCandidateVer;
        this.candidateTrafficPct = b.candidateTrafficPct; this.maxRetries = b.maxRetries;
        this.namespace = b.namespace;
    }

    public static ServerConfig load() {
        Builder b = new Builder();
        String secret = System.getenv("SCORING_JWT_SECRET");
        if (secret == null || secret.isBlank()) {
            LOG.warning("⚠ SCORING_JWT_SECRET not set — using insecure dev default. DO NOT USE IN PRODUCTION.");
            secret = "dev-insecure-scoring-engine-secret-do-not-use";
        }
        b.jwtSecret             = secret;
        b.dbHost                = env("SCORING_DB_HOST", "postgres.analytics.svc.cluster.local");
        b.redisEnabled          = !"false".equalsIgnoreCase(env("SCORING_REDIS_ENABLED", "true"));
        b.auditEnabled          = !"false".equalsIgnoreCase(env("SCORING_AUDIT_ENABLED", "true"));
        b.rateLimitPerSec       = envInt("SCORING_RATE_PER_SEC", 100);
        b.rateLimitPerMin       = envInt("SCORING_RATE_PER_MIN", 1000);
        b.slaMinutes            = envInt("SCORING_SLA_MINUTES", 15);
        b.cacheTtlSeconds       = envInt("SCORING_CACHE_TTL_SECONDS", 3600);
        b.confidenceThreshold   = envDbl("SCORING_CONFIDENCE_THRESHOLD", 0.6);
        b.weightCommunication   = envDbl("SCORING_WEIGHT_COMMUNICATION", 35.0);
        b.weightTechnical       = envDbl("SCORING_WEIGHT_TECHNICAL", 40.0);
        b.weightCulturalFit     = envDbl("SCORING_WEIGHT_CULTURAL_FIT", 25.0);
        b.fairnessThreshold     = envDbl("SCORING_FAIRNESS_THRESHOLD", 0.8);
        b.biasVarianceMax       = envDbl("SCORING_BIAS_VARIANCE_MAX", 5.0);
        b.modelProductionVer    = env("SCORING_MODEL_PRODUCTION_VER", "v1");
        b.modelCandidateVer     = env("SCORING_MODEL_CANDIDATE_VER", "v2");
        b.candidateTrafficPct   = envInt("SCORING_CANDIDATE_TRAFFIC_PCT", 10);
        b.maxRetries            = envInt("SCORING_MAX_RETRIES", 3);
        b.namespace             = env("SCORING_NAMESPACE", "analytics");

        // Validate weights sum to 100%
        double wsum = b.weightCommunication + b.weightTechnical + b.weightCulturalFit;
        if (Math.abs(wsum - 100.0) > 0.01) {
            LOG.warning("⚠ Scoring weights sum=" + wsum + " (should be 100). Using defaults 35/40/25.");
            b.weightCommunication = 35.0; b.weightTechnical = 40.0; b.weightCulturalFit = 25.0;
        }

        LOG.info("ScoringConfig: namespace=" + b.namespace + " sla=" + b.slaMinutes + "min"
            + " weights=comm" + b.weightCommunication + "/tech" + b.weightTechnical
            + "/cult" + b.weightCulturalFit + " model=" + b.modelProductionVer);
        return new ServerConfig(b);
    }

    private static String env(String k, String d) { String v = System.getenv(k); return (v==null||v.isBlank())?d:v; }
    private static int envInt(String k, int d) { try{String v=System.getenv(k);return(v==null||v.isBlank())?d:Integer.parseInt(v.trim());}catch(Exception e){return d;} }
    private static double envDbl(String k, double d) { try{String v=System.getenv(k);return(v==null||v.isBlank())?d:Double.parseDouble(v.trim());}catch(Exception e){return d;} }

    public String getJwtSecret()          { return jwtSecret; }
    public String getDbHost()             { return dbHost; }
    public boolean isRedisEnabled()       { return redisEnabled; }
    public boolean isAuditEnabled()       { return auditEnabled; }
    public int getRateLimitPerSec()       { return rateLimitPerSec; }
    public int getRateLimitPerMin()       { return rateLimitPerMin; }
    public int getSlaMinutes()            { return slaMinutes; }
    public int getCacheTtlSeconds()       { return cacheTtlSeconds; }
    public double getConfidenceThreshold(){ return confidenceThreshold; }
    public double getWeightCommunication(){ return weightCommunication; }
    public double getWeightTechnical()    { return weightTechnical; }
    public double getWeightCulturalFit()  { return weightCulturalFit; }
    public double getFairnessThreshold()  { return fairnessThreshold; }
    public double getBiasVarianceMax()    { return biasVarianceMax; }
    public String getModelProductionVer() { return modelProductionVer; }
    public String getModelCandidateVer()  { return modelCandidateVer; }
    public int getCandidateTrafficPct()   { return candidateTrafficPct; }
    public int getMaxRetries()            { return maxRetries; }
    public String getNamespace()          { return namespace; }

    /** Compute overall score using configured dimension weights. */
    public double computeOverallScore(double comm, double tech, double cult) {
        return Math.round(
            (weightCommunication / 100.0 * comm
            + weightTechnical    / 100.0 * tech
            + weightCulturalFit  / 100.0 * cult) * 100.0) / 100.0;
    }

    /** Determine model version for this assessment (traffic splitting by hash). */
    public String selectModelVersion(String assessmentId) {
        int hash = Math.abs(assessmentId.hashCode()) % 100;
        return hash < candidateTrafficPct ? modelCandidateVer : modelProductionVer;
    }

    private static final class Builder {
        String jwtSecret, dbHost, modelProductionVer, modelCandidateVer, namespace;
        boolean redisEnabled, auditEnabled;
        int rateLimitPerSec, rateLimitPerMin, slaMinutes, cacheTtlSeconds, candidateTrafficPct, maxRetries;
        double confidenceThreshold, weightCommunication, weightTechnical, weightCulturalFit, fairnessThreshold, biasVarianceMax;
    }
}
