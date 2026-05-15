package com.ecoskiller.mcp.session.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session-Control MCP Server configuration.
 * All values sourced from environment variables — zero hardcoded secrets.
 *
 * Environment variables:
 *   SESSION_SERVICE_BASE_URL    — Session-Control-Service REST base URL
 *                                 (default: http://localhost:3000)
 *   SESSION_SERVICE_AUTH_TYPE   — none | bearer (JWT from Keycloak)
 *   SESSION_SERVICE_TOKEN       — Bearer token / service account JWT
 *   SESSION_SERVICE_TLS_VERIFY  — true/false (default: true)
 *   SESSION_SERVICE_TIMEOUT_MS  — HTTP timeout ms (default: 10000)
 *   SESSION_DEFAULT_TENANT_ID   — Default tenant ID for scoped operations
 *   SESSION_MAX_DURATION_SECS   — Max allowed session duration (default: 14400 = 4h)
 *   SESSION_HEARTBEAT_TIMEOUT   — Seconds before participant eviction (default: 120)
 *   MCP_API_KEY                 — Secret key required in every tool call
 *   MCP_API_KEY_2               — Optional rotation key
 */
public class SessionControlConfig {

    private static final Logger log = LoggerFactory.getLogger(SessionControlConfig.class);

    public final String   baseUrl;
    public final String   authType;
    public final String   bearerToken;
    public final boolean  tlsVerify;
    public final int      timeoutMs;
    public final String   defaultTenantId;
    public final int      maxDurationSecs;
    public final int      heartbeatTimeoutSecs;
    public final String[] validApiKeys;

    private SessionControlConfig(String baseUrl, String authType, String bearerToken,
                                  boolean tlsVerify, int timeoutMs, String defaultTenantId,
                                  int maxDurationSecs, int heartbeatTimeoutSecs,
                                  String[] validApiKeys) {
        this.baseUrl              = baseUrl;
        this.authType             = authType;
        this.bearerToken          = bearerToken;
        this.tlsVerify            = tlsVerify;
        this.timeoutMs            = timeoutMs;
        this.defaultTenantId      = defaultTenantId;
        this.maxDurationSecs      = maxDurationSecs;
        this.heartbeatTimeoutSecs = heartbeatTimeoutSecs;
        this.validApiKeys         = validApiKeys;
    }

    public static SessionControlConfig fromEnvironment() {
        String  baseUrl        = env("SESSION_SERVICE_BASE_URL",   "http://localhost:3000");
        String  authType       = env("SESSION_SERVICE_AUTH_TYPE",  "none").toLowerCase();
        String  token          = env("SESSION_SERVICE_TOKEN",      "");
        boolean tlsVerify      = !"false".equalsIgnoreCase(env("SESSION_SERVICE_TLS_VERIFY", "true"));
        int     timeout        = Integer.parseInt(env("SESSION_SERVICE_TIMEOUT_MS", "10000"));
        String  tenantId       = env("SESSION_DEFAULT_TENANT_ID",  "");
        int     maxDur         = Integer.parseInt(env("SESSION_MAX_DURATION_SECS",  "14400"));
        int     hbTimeout      = Integer.parseInt(env("SESSION_HEARTBEAT_TIMEOUT",  "120"));

        String k1 = env("MCP_API_KEY",   "");
        String k2 = env("MCP_API_KEY_2", "");
        String[] keys = java.util.Arrays.stream(new String[]{k1, k2})
                            .filter(k -> !k.isEmpty()).toArray(String[]::new);

        if (keys.length == 0) log.warn("WARNING: MCP_API_KEY not set — all tool calls will be rejected");
        if (!tlsVerify)       log.warn("SECURITY WARNING: TLS verification is disabled");
        if (maxDur > 14400)   log.warn("SESSION_MAX_DURATION_SECS={} exceeds recommended 4h limit", maxDur);

        log.info("Session Control config: url={}, auth={}, tlsVerify={}, tenant={}",
                 baseUrl, authType, tlsVerify, tenantId);

        return new SessionControlConfig(baseUrl, authType, token, tlsVerify, timeout,
                                        tenantId, maxDur, hbTimeout, keys);
    }

    private static String env(String name, String def) {
        String v = System.getenv(name);
        return (v != null && !v.isEmpty()) ? v : def;
    }
}
