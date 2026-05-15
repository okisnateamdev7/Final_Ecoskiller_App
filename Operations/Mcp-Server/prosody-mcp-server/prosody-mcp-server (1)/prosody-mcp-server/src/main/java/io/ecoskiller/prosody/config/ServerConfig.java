package io.ecoskiller.prosody.config;

import java.util.logging.Logger;

/**
 * Server configuration loaded from environment variables.
 *
 * All secrets (JWT key, DB password) come from Kubernetes Secrets mounted
 * as environment variables — never from code or config files.
 *
 * Required env vars:
 *   PROSODY_JWT_SECRET      — HMAC-SHA256 key for JWT validation
 *   PROSODY_XMPP_DOMAIN     — XMPP domain (default: ecoskiller.io)
 *   PROSODY_DB_HOST         — PostgreSQL host
 *
 * Optional env vars:
 *   PROSODY_RATE_LIMIT_PER_SEC   — default: 100
 *   PROSODY_RATE_LIMIT_PER_MIN   — default: 1000
 *   PROSODY_REDIS_ENABLED        — default: false
 *   PROSODY_AUDIT_ENABLED        — default: true
 *   PROSODY_LOG_LEVEL            — default: INFO
 */
public class ServerConfig {

    private static final Logger LOGGER = Logger.getLogger(ServerConfig.class.getName());

    private final String jwtSecret;
    private final String xmppDomain;
    private final String dbHost;
    private final int rateLimitPerSecond;
    private final int rateLimitPerMinute;
    private final boolean redisEnabled;
    private final boolean auditEnabled;
    private final String logLevel;

    private ServerConfig(Builder b) {
        this.jwtSecret = b.jwtSecret;
        this.xmppDomain = b.xmppDomain;
        this.dbHost = b.dbHost;
        this.rateLimitPerSecond = b.rateLimitPerSecond;
        this.rateLimitPerMinute = b.rateLimitPerMinute;
        this.redisEnabled = b.redisEnabled;
        this.auditEnabled = b.auditEnabled;
        this.logLevel = b.logLevel;
    }

    public static ServerConfig load() {
        Builder b = new Builder();

        // ── Secrets (from Kubernetes Secrets) ─────────────────────────────────
        String jwtSecret = System.getenv("PROSODY_JWT_SECRET");
        if (jwtSecret == null || jwtSecret.isBlank()) {
            LOGGER.warning("PROSODY_JWT_SECRET not set — using insecure default (dev only!)");
            jwtSecret = "dev-insecure-secret-do-not-use-in-production";
        }
        b.jwtSecret = jwtSecret;

        // ── Config values ─────────────────────────────────────────────────────
        b.xmppDomain = getEnv("PROSODY_XMPP_DOMAIN", "ecoskiller.io");
        b.dbHost = getEnv("PROSODY_DB_HOST", "postgres.default.svc.cluster.local");
        b.rateLimitPerSecond = getEnvInt("PROSODY_RATE_LIMIT_PER_SEC", 100);
        b.rateLimitPerMinute = getEnvInt("PROSODY_RATE_LIMIT_PER_MIN", 1000);
        b.redisEnabled = "true".equalsIgnoreCase(getEnv("PROSODY_REDIS_ENABLED", "false"));
        b.auditEnabled = !"false".equalsIgnoreCase(getEnv("PROSODY_AUDIT_ENABLED", "true"));
        b.logLevel = getEnv("PROSODY_LOG_LEVEL", "INFO");

        LOGGER.info("Config loaded: domain=" + b.xmppDomain +
            " rateLimit=" + b.rateLimitPerSecond + "/sec" +
            " redis=" + b.redisEnabled +
            " audit=" + b.auditEnabled);

        return new ServerConfig(b);
    }

    private static String getEnv(String key, String defaultVal) {
        String val = System.getenv(key);
        return (val == null || val.isBlank()) ? defaultVal : val;
    }

    private static int getEnvInt(String key, int defaultVal) {
        try {
            String val = System.getenv(key);
            return (val == null || val.isBlank()) ? defaultVal : Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getJwtSecret()         { return jwtSecret; }
    public String getXmppDomain()        { return xmppDomain; }
    public String getDbHost()            { return dbHost; }
    public int getRateLimitPerSecond()   { return rateLimitPerSecond; }
    public int getRateLimitPerMinute()   { return rateLimitPerMinute; }
    public boolean isRedisEnabled()      { return redisEnabled; }
    public boolean isAuditEnabled()      { return auditEnabled; }
    public String getLogLevel()          { return logLevel; }

    // ── Builder ────────────────────────────────────────────────────────────────

    private static class Builder {
        String jwtSecret;
        String xmppDomain;
        String dbHost;
        int rateLimitPerSecond = 100;
        int rateLimitPerMinute = 1000;
        boolean redisEnabled = false;
        boolean auditEnabled = true;
        String logLevel = "INFO";
    }
}
