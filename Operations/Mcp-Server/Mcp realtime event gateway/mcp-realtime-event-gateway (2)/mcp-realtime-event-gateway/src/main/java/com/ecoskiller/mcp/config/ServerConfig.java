package com.ecoskiller.mcp.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Environment-variable-first configuration.
 * Fallback: config.properties (classpath).
 * No secrets in code — all sensitive values via Kubernetes Secrets → env vars.
 */
public class ServerConfig {

    private static final Logger LOGGER = Logger.getLogger(ServerConfig.class.getName());

    private String  environment;
    private boolean jwtValidationEnabled;
    private String  keycloakIssuer;
    private int     rateLimitPerMinute;
    private int     maxConnectionsPerPod;
    private int     maxEventsPerSecGlobal;
    private int     maxEventsPerSecClient;
    private int     ringBufferCapacity;
    private int     bufferReplayWindowMin;
    private int     circuitBreakerThreshold;
    private Level   logLevel;

    private ServerConfig() {}

    public static ServerConfig load() {
        ServerConfig c  = new ServerConfig();
        Properties   p  = new Properties();
        try (InputStream is = ServerConfig.class.getResourceAsStream("/config.properties")) {
            if (is != null) p.load(is);
        } catch (Exception e) { /* ok — env vars take over */ }

        c.environment            = env("MCP_ENV",                 p, "ENV",                    "dev");
        c.jwtValidationEnabled   = Boolean.parseBoolean(
                env("MCP_JWT_VALIDATION_ENABLED",  p, "JWT_VALIDATION_ENABLED",  "false"));
        c.keycloakIssuer         = env("MCP_KEYCLOAK_ISSUER",     p, "KEYCLOAK_ISSUER",
                "https://auth.ecoskiller.com/realms/");
        c.rateLimitPerMinute     = Integer.parseInt(
                env("MCP_RATE_LIMIT_PER_MINUTE",   p, "RATE_LIMIT_PER_MINUTE",   "120"));
        c.maxConnectionsPerPod   = Integer.parseInt(
                env("MCP_MAX_CONNECTIONS",         p, "MAX_CONNECTIONS",          "10000"));
        c.maxEventsPerSecGlobal  = Integer.parseInt(
                env("MCP_MAX_EVENTS_SEC_GLOBAL",   p, "MAX_EVENTS_SEC_GLOBAL",    "50000"));
        c.maxEventsPerSecClient  = Integer.parseInt(
                env("MCP_MAX_EVENTS_SEC_CLIENT",   p, "MAX_EVENTS_SEC_CLIENT",    "100"));
        c.ringBufferCapacity     = Integer.parseInt(
                env("MCP_RING_BUFFER_CAPACITY",    p, "RING_BUFFER_CAPACITY",     "100"));
        c.bufferReplayWindowMin  = Integer.parseInt(
                env("MCP_BUFFER_REPLAY_WINDOW_MIN",p, "BUFFER_REPLAY_WINDOW_MIN", "5"));
        c.circuitBreakerThreshold= Integer.parseInt(
                env("MCP_CIRCUIT_BREAKER_THRESHOLD",p,"CIRCUIT_BREAKER_THRESHOLD","50000"));
        String ll = env("MCP_LOG_LEVEL", p, "LOG_LEVEL",
                c.environment.equals("prod") ? "WARNING" : "INFO");
        c.logLevel = parseLevel(ll);

        LOGGER.info("ServerConfig: env=" + c.environment +
                ", jwtValidation=" + c.jwtValidationEnabled +
                ", rateLimit=" + c.rateLimitPerMinute + "/min" +
                ", maxConn=" + c.maxConnectionsPerPod);
        return c;
    }

    private static String env(String envKey, Properties p, String propKey, String def) {
        String v = System.getenv(envKey);
        return (v != null && !v.isBlank()) ? v : p.getProperty(propKey, def);
    }
    private static Level parseLevel(String s) {
        return switch (s.toUpperCase()) {
            case "DEBUG","FINE" -> Level.FINE;
            case "INFO"         -> Level.INFO;
            case "WARN","WARNING"->Level.WARNING;
            case "ERROR","SEVERE"->Level.SEVERE;
            default             -> Level.INFO;
        };
    }

    public String  getEnvironment()            { return environment; }
    public boolean isJwtValidationEnabled()    { return jwtValidationEnabled; }
    public String  getKeycloakIssuer()         { return keycloakIssuer; }
    public int     getRateLimitPerMinute()      { return rateLimitPerMinute; }
    public int     getMaxConnectionsPerPod()    { return maxConnectionsPerPod; }
    public int     getMaxEventsPerSecGlobal()   { return maxEventsPerSecGlobal; }
    public int     getMaxEventsPerSecClient()   { return maxEventsPerSecClient; }
    public int     getRingBufferCapacity()      { return ringBufferCapacity; }
    public int     getBufferReplayWindowMin()   { return bufferReplayWindowMin; }
    public int     getCircuitBreakerThreshold() { return circuitBreakerThreshold; }
    public Level   getLogLevel()               { return logLevel; }
}
