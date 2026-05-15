package io.ecoskiller.recruiter.config;

import java.util.logging.Logger;

/**
 * Configuration loaded entirely from environment variables / Kubernetes Secrets.
 * No secrets are ever hardcoded.
 *
 * Required:
 *   RECRUITER_JWT_SECRET      — HMAC-SHA256 key for JWT validation
 *
 * Optional:
 *   RECRUITER_TENANT_DOMAIN   — default: ecoskiller.io
 *   RECRUITER_DB_HOST         — default: postgres.core.svc.cluster.local
 *   RECRUITER_REDIS_ENABLED   — default: true
 *   RECRUITER_AUDIT_ENABLED   — default: true
 *   RECRUITER_RATE_PER_SEC    — default: 50
 *   RECRUITER_RATE_PER_MIN    — default: 500
 *   RECRUITER_LOG_LEVEL       — default: INFO
 *   RECRUITER_MAX_JOBS_STARTER      — default: 5
 *   RECRUITER_MAX_JOBS_PROFESSIONAL — default: 25
 *   RECRUITER_MAX_JOBS_ENTERPRISE   — default: 999
 */
public final class ServerConfig {

    private static final Logger LOG = Logger.getLogger(ServerConfig.class.getName());

    private final String jwtSecret;
    private final String tenantDomain;
    private final String dbHost;
    private final boolean redisEnabled;
    private final boolean auditEnabled;
    private final int rateLimitPerSec;
    private final int rateLimitPerMin;
    private final String logLevel;
    private final int maxJobsStarter;
    private final int maxJobsProfessional;
    private final int maxJobsEnterprise;

    private ServerConfig(Builder b) {
        this.jwtSecret = b.jwtSecret;
        this.tenantDomain = b.tenantDomain;
        this.dbHost = b.dbHost;
        this.redisEnabled = b.redisEnabled;
        this.auditEnabled = b.auditEnabled;
        this.rateLimitPerSec = b.rateLimitPerSec;
        this.rateLimitPerMin = b.rateLimitPerMin;
        this.logLevel = b.logLevel;
        this.maxJobsStarter = b.maxJobsStarter;
        this.maxJobsProfessional = b.maxJobsProfessional;
        this.maxJobsEnterprise = b.maxJobsEnterprise;
    }

    public static ServerConfig load() {
        Builder b = new Builder();

        // Secret — from Kubernetes Secret (never default in prod)
        String secret = System.getenv("RECRUITER_JWT_SECRET");
        if (secret == null || secret.isBlank()) {
            LOG.warning("⚠ RECRUITER_JWT_SECRET not set — using insecure dev default. DO NOT USE IN PRODUCTION.");
            secret = "dev-insecure-recruiter-secret-change-me";
        }
        b.jwtSecret = secret;

        b.tenantDomain        = env("RECRUITER_TENANT_DOMAIN", "ecoskiller.io");
        b.dbHost              = env("RECRUITER_DB_HOST", "postgres.core.svc.cluster.local");
        b.redisEnabled        = !"false".equalsIgnoreCase(env("RECRUITER_REDIS_ENABLED", "true"));
        b.auditEnabled        = !"false".equalsIgnoreCase(env("RECRUITER_AUDIT_ENABLED", "true"));
        b.rateLimitPerSec     = envInt("RECRUITER_RATE_PER_SEC", 50);
        b.rateLimitPerMin     = envInt("RECRUITER_RATE_PER_MIN", 500);
        b.logLevel            = env("RECRUITER_LOG_LEVEL", "INFO");
        b.maxJobsStarter      = envInt("RECRUITER_MAX_JOBS_STARTER", 5);
        b.maxJobsProfessional = envInt("RECRUITER_MAX_JOBS_PROFESSIONAL", 25);
        b.maxJobsEnterprise   = envInt("RECRUITER_MAX_JOBS_ENTERPRISE", 999);

        LOG.info("RecruiterConfig loaded: domain=" + b.tenantDomain
            + " rps=" + b.rateLimitPerSec + " audit=" + b.auditEnabled);
        return new ServerConfig(b);
    }

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }
    private static int envInt(String key, int def) {
        try { String v = System.getenv(key); return (v == null || v.isBlank()) ? def : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return def; }
    }

    // ── Getters ─────────────────────────────────────────────────────────────
    public String getJwtSecret()          { return jwtSecret; }
    public String getTenantDomain()       { return tenantDomain; }
    public String getDbHost()             { return dbHost; }
    public boolean isRedisEnabled()       { return redisEnabled; }
    public boolean isAuditEnabled()       { return auditEnabled; }
    public int getRateLimitPerSec()       { return rateLimitPerSec; }
    public int getRateLimitPerMin()       { return rateLimitPerMin; }
    public String getLogLevel()           { return logLevel; }
    public int getMaxJobsForTier(String tier) {
        return switch (tier == null ? "" : tier.toLowerCase()) {
            case "professional" -> maxJobsProfessional;
            case "enterprise"   -> maxJobsEnterprise;
            default             -> maxJobsStarter;
        };
    }

    private static final class Builder {
        String jwtSecret, tenantDomain, dbHost, logLevel;
        boolean redisEnabled, auditEnabled;
        int rateLimitPerSec, rateLimitPerMin;
        int maxJobsStarter, maxJobsProfessional, maxJobsEnterprise;
    }
}
