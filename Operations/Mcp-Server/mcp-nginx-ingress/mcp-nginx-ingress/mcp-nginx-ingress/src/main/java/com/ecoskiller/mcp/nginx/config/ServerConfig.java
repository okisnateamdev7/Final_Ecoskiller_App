package com.ecoskiller.mcp.nginx.config;

import java.util.Map;
import java.util.HashMap;

/**
 * Runtime configuration for the NGINX Ingress MCP server.
 *
 * Values are loaded from environment variables (12-factor style).
 * Sensitive values (API keys, tokens) are read-once and never stored as
 * plain strings in field variables that could be serialised or logged.
 */
public class ServerConfig {

    // ── Kubernetes / cluster ────────────────────────────────────────────
    private final String k8sApiServer;
    private final String kubeNamespace;
    private final String ingressClassName;

    // ── NGINX defaults ───────────────────────────────────────────────────
    private final int    defaultRateLimitRps;
    private final int    defaultBurst;
    private final int    maxConnPerIp;
    private final int    sslSessionCacheMb;
    private final String tlsProtocols;

    // ── Monitoring ───────────────────────────────────────────────────────
    private final String prometheusEndpoint;
    private final int    healthCheckIntervalSec;
    private final int    healthCheckTimeoutSec;
    private final int    unhealthyThreshold;

    // ── Security ─────────────────────────────────────────────────────────
    private final boolean wafEnabled;
    private final boolean mtlsEnabled;
    private final boolean hstsPreaload;
    private final long    hstsMaxAgeSec;

    // ── Cloud ────────────────────────────────────────────────────────────
    private final String primaryCloud;   // gcp | aws
    private final String primaryRegion;
    private final String secondaryCloud;
    private final String secondaryRegion;

    // ── Logging ─────────────────────────────────────────────────────────
    private final String logLevel;       // DEBUG | INFO | WARN | ERROR

    // ── Constructor (private — use load()) ──────────────────────────────

    private ServerConfig(
            String k8sApiServer, String kubeNamespace, String ingressClassName,
            int defaultRateLimitRps, int defaultBurst, int maxConnPerIp,
            int sslSessionCacheMb, String tlsProtocols,
            String prometheusEndpoint, int healthCheckIntervalSec,
            int healthCheckTimeoutSec, int unhealthyThreshold,
            boolean wafEnabled, boolean mtlsEnabled, boolean hstsPreload, long hstsMaxAgeSec,
            String primaryCloud, String primaryRegion, String secondaryCloud, String secondaryRegion,
            String logLevel) {
        this.k8sApiServer           = k8sApiServer;
        this.kubeNamespace          = kubeNamespace;
        this.ingressClassName       = ingressClassName;
        this.defaultRateLimitRps    = defaultRateLimitRps;
        this.defaultBurst           = defaultBurst;
        this.maxConnPerIp           = maxConnPerIp;
        this.sslSessionCacheMb      = sslSessionCacheMb;
        this.tlsProtocols           = tlsProtocols;
        this.prometheusEndpoint     = prometheusEndpoint;
        this.healthCheckIntervalSec = healthCheckIntervalSec;
        this.healthCheckTimeoutSec  = healthCheckTimeoutSec;
        this.unhealthyThreshold     = unhealthyThreshold;
        this.wafEnabled             = wafEnabled;
        this.mtlsEnabled            = mtlsEnabled;
        this.hstsPreaload           = hstsPreload;
        this.hstsMaxAgeSec          = hstsMaxAgeSec;
        this.primaryCloud           = primaryCloud;
        this.primaryRegion          = primaryRegion;
        this.secondaryCloud         = secondaryCloud;
        this.secondaryRegion        = secondaryRegion;
        this.logLevel               = logLevel;
    }

    // ── Factory ──────────────────────────────────────────────────────────

    public static ServerConfig load() {
        return new ServerConfig(
            env("K8S_API_SERVER",            "https://kubernetes.default.svc"),
            env("KUBE_NAMESPACE",            "default"),
            env("INGRESS_CLASS_NAME",        "nginx"),
            envInt("RATE_LIMIT_RPS",         100),
            envInt("RATE_LIMIT_BURST",       200),
            envInt("MAX_CONN_PER_IP",        20),
            envInt("SSL_SESSION_CACHE_MB",   50),
            env("TLS_PROTOCOLS",             "TLSv1.2 TLSv1.3"),
            env("PROMETHEUS_ENDPOINT",       "http://prometheus:9090"),
            envInt("HEALTH_CHECK_INTERVAL",  10),
            envInt("HEALTH_CHECK_TIMEOUT",   5),
            envInt("UNHEALTHY_THRESHOLD",    3),
            envBool("WAF_ENABLED",           false),
            envBool("MTLS_ENABLED",          false),
            envBool("HSTS_PRELOAD",          true),
            envLong("HSTS_MAX_AGE",          31536000L),
            env("PRIMARY_CLOUD",             "gcp"),
            env("PRIMARY_REGION",            "asia-south1"),
            env("SECONDARY_CLOUD",           "aws"),
            env("SECONDARY_REGION",          "ap-southeast-1"),
            env("LOG_LEVEL",                 "INFO")
        );
    }

    // ── Private helpers ──────────────────────────────────────────────────

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }
    private static int envInt(String key, int def) {
        try { String v = System.getenv(key); return (v != null) ? Integer.parseInt(v.trim()) : def; }
        catch (NumberFormatException e) { return def; }
    }
    private static long envLong(String key, long def) {
        try { String v = System.getenv(key); return (v != null) ? Long.parseLong(v.trim()) : def; }
        catch (NumberFormatException e) { return def; }
    }
    private static boolean envBool(String key, boolean def) {
        String v = System.getenv(key);
        if (v == null) return def;
        return "true".equalsIgnoreCase(v.trim()) || "1".equals(v.trim());
    }

    // ── Getters ──────────────────────────────────────────────────────────

    public String  getK8sApiServer()           { return k8sApiServer; }
    public String  getKubeNamespace()          { return kubeNamespace; }
    public String  getIngressClassName()       { return ingressClassName; }
    public int     getDefaultRateLimitRps()    { return defaultRateLimitRps; }
    public int     getDefaultBurst()           { return defaultBurst; }
    public int     getMaxConnPerIp()           { return maxConnPerIp; }
    public int     getSslSessionCacheMb()      { return sslSessionCacheMb; }
    public String  getTlsProtocols()           { return tlsProtocols; }
    public String  getPrometheusEndpoint()     { return prometheusEndpoint; }
    public int     getHealthCheckIntervalSec() { return healthCheckIntervalSec; }
    public int     getHealthCheckTimeoutSec()  { return healthCheckTimeoutSec; }
    public int     getUnhealthyThreshold()     { return unhealthyThreshold; }
    public boolean isWafEnabled()              { return wafEnabled; }
    public boolean isMtlsEnabled()             { return mtlsEnabled; }
    public boolean isHstsPreload()             { return hstsPreaload; }
    public long    getHstsMaxAgeSec()          { return hstsMaxAgeSec; }
    public String  getPrimaryCloud()           { return primaryCloud; }
    public String  getPrimaryRegion()          { return primaryRegion; }
    public String  getSecondaryCloud()         { return secondaryCloud; }
    public String  getSecondaryRegion()        { return secondaryRegion; }
    public String  getLogLevel()               { return logLevel; }

    /** Safe summary — never prints sensitive values */
    @Override
    public String toString() {
        return "ServerConfig{namespace=" + kubeNamespace
             + ", ingressClass=" + ingressClassName
             + ", rateLimitRps=" + defaultRateLimitRps
             + ", waf=" + wafEnabled
             + ", mtls=" + mtlsEnabled
             + ", primaryCloud=" + primaryCloud + "/" + primaryRegion + "}";
    }
}
