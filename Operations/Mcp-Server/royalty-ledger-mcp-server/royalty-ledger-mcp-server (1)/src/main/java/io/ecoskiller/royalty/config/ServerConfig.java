package io.ecoskiller.royalty.config;

import java.util.logging.Logger;

/**
 * Configuration loaded entirely from environment variables / Kubernetes Secrets.
 *
 * Required:
 *   ROYALTY_JWT_SECRET         — HMAC-SHA256 key (Keycloak)
 *
 * Optional:
 *   ROYALTY_DOMAIN             — default: ecoskiller.io
 *   ROYALTY_DB_HOST            — default: postgres.revenue.svc.cluster.local
 *   ROYALTY_CLICKHOUSE_HOST    — default: clickhouse.revenue.svc.cluster.local
 *   ROYALTY_REDIS_ENABLED      — default: true
 *   ROYALTY_AUDIT_ENABLED      — default: true
 *   ROYALTY_RATE_PER_SEC       — default: 100
 *   ROYALTY_RATE_PER_MIN       — default: 1000
 *   ROYALTY_PAYOUT_THRESHOLD   — default: 5000   (₹5,000 minimum)
 *   ROYALTY_TDS_RATE_INDIVIDUAL— default: 10     (%)
 *   ROYALTY_TDS_RATE_FOREIGN   — default: 30     (%)
 *   ROYALTY_GST_RATE           — default: 18     (%)
 *   ROYALTY_FRAUD_VELOCITY_LIMIT — default: 100  (submissions/min threshold)
 *   ROYALTY_MAX_DAILY_SUBMISSIONS — default: 50
 */
public final class ServerConfig {

    private static final Logger LOG = Logger.getLogger(ServerConfig.class.getName());

    private final String jwtSecret;
    private final String domain;
    private final String dbHost;
    private final String clickhouseHost;
    private final boolean redisEnabled;
    private final boolean auditEnabled;
    private final int rateLimitPerSec;
    private final int rateLimitPerMin;
    private final double payoutThreshold;
    private final double tdsRateIndividual;
    private final double tdsRateForeign;
    private final double gstRate;
    private final int fraudVelocityLimit;
    private final int maxDailySubmissions;

    private ServerConfig(Builder b) {
        this.jwtSecret = b.jwtSecret;
        this.domain = b.domain;
        this.dbHost = b.dbHost;
        this.clickhouseHost = b.clickhouseHost;
        this.redisEnabled = b.redisEnabled;
        this.auditEnabled = b.auditEnabled;
        this.rateLimitPerSec = b.rateLimitPerSec;
        this.rateLimitPerMin = b.rateLimitPerMin;
        this.payoutThreshold = b.payoutThreshold;
        this.tdsRateIndividual = b.tdsRateIndividual;
        this.tdsRateForeign = b.tdsRateForeign;
        this.gstRate = b.gstRate;
        this.fraudVelocityLimit = b.fraudVelocityLimit;
        this.maxDailySubmissions = b.maxDailySubmissions;
    }

    public static ServerConfig load() {
        Builder b = new Builder();

        String secret = System.getenv("ROYALTY_JWT_SECRET");
        if (secret == null || secret.isBlank()) {
            LOG.warning("⚠ ROYALTY_JWT_SECRET not set — using insecure dev default. DO NOT USE IN PRODUCTION.");
            secret = "dev-insecure-royalty-ledger-secret-change-me";
        }
        b.jwtSecret = secret;

        b.domain             = env("ROYALTY_DOMAIN", "ecoskiller.io");
        b.dbHost             = env("ROYALTY_DB_HOST", "postgres.revenue.svc.cluster.local");
        b.clickhouseHost     = env("ROYALTY_CLICKHOUSE_HOST", "clickhouse.revenue.svc.cluster.local");
        b.redisEnabled       = !"false".equalsIgnoreCase(env("ROYALTY_REDIS_ENABLED", "true"));
        b.auditEnabled       = !"false".equalsIgnoreCase(env("ROYALTY_AUDIT_ENABLED", "true"));
        b.rateLimitPerSec    = envInt("ROYALTY_RATE_PER_SEC", 100);
        b.rateLimitPerMin    = envInt("ROYALTY_RATE_PER_MIN", 1000);
        b.payoutThreshold    = envDouble("ROYALTY_PAYOUT_THRESHOLD", 5000.0);
        b.tdsRateIndividual  = envDouble("ROYALTY_TDS_RATE_INDIVIDUAL", 10.0);
        b.tdsRateForeign     = envDouble("ROYALTY_TDS_RATE_FOREIGN", 30.0);
        b.gstRate            = envDouble("ROYALTY_GST_RATE", 18.0);
        b.fraudVelocityLimit = envInt("ROYALTY_FRAUD_VELOCITY_LIMIT", 100);
        b.maxDailySubmissions= envInt("ROYALTY_MAX_DAILY_SUBMISSIONS", 50);

        LOG.info("RoyaltyConfig loaded: domain=" + b.domain
            + " payoutThreshold=₹" + b.payoutThreshold
            + " tds=" + b.tdsRateIndividual + "%");
        return new ServerConfig(b);
    }

    private static String env(String k, String d) { String v = System.getenv(k); return (v==null||v.isBlank()) ? d : v; }
    private static int envInt(String k, int d) { try { String v=System.getenv(k); return (v==null||v.isBlank())?d:Integer.parseInt(v.trim()); } catch(Exception e){return d;} }
    private static double envDouble(String k, double d) { try { String v=System.getenv(k); return (v==null||v.isBlank())?d:Double.parseDouble(v.trim()); } catch(Exception e){return d;} }

    public String getJwtSecret()           { return jwtSecret; }
    public String getDomain()              { return domain; }
    public String getDbHost()              { return dbHost; }
    public String getClickhouseHost()      { return clickhouseHost; }
    public boolean isRedisEnabled()        { return redisEnabled; }
    public boolean isAuditEnabled()        { return auditEnabled; }
    public int getRateLimitPerSec()        { return rateLimitPerSec; }
    public int getRateLimitPerMin()        { return rateLimitPerMin; }
    public double getPayoutThreshold()     { return payoutThreshold; }
    public double getTdsRateIndividual()   { return tdsRateIndividual; }
    public double getTdsRateForeign()      { return tdsRateForeign; }
    public double getGstRate()             { return gstRate; }
    public int getFraudVelocityLimit()     { return fraudVelocityLimit; }
    public int getMaxDailySubmissions()    { return maxDailySubmissions; }

    private static final class Builder {
        String jwtSecret, domain, dbHost, clickhouseHost;
        boolean redisEnabled, auditEnabled;
        int rateLimitPerSec, rateLimitPerMin, fraudVelocityLimit, maxDailySubmissions;
        double payoutThreshold, tdsRateIndividual, tdsRateForeign, gstRate;
    }
}
