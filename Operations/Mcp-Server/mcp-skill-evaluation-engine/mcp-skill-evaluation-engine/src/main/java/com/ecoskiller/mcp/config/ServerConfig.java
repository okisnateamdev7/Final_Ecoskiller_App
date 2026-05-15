package com.ecoskiller.mcp.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server configuration loaded from system environment variables and
 * fallback config.properties.
 *
 * Security principle: No secrets in code. All sensitive values come from
 * environment variables (injected by Kubernetes Secrets).
 */
public class ServerConfig {

    private static final Logger LOGGER = Logger.getLogger(ServerConfig.class.getName());

    private boolean jwtValidationEnabled;
    private String  keycloakIssuer;
    private int     rateLimitPerMinute;
    private Level   logLevel;
    private String  environment;

    private ServerConfig() {}

    public static ServerConfig load() {
        ServerConfig config = new ServerConfig();
        Properties props = new Properties();

        // Try loading from classpath config.properties
        try (InputStream is = ServerConfig.class.getResourceAsStream("/config.properties")) {
            if (is != null) props.load(is);
        } catch (Exception e) {
            LOGGER.fine("No config.properties found, using environment variables only");
        }

        // Environment variables take precedence (Kubernetes Secrets pattern)
        config.environment          = getEnv("MCP_ENV", props, "ENV", "dev");
        config.jwtValidationEnabled = Boolean.parseBoolean(
                getEnv("MCP_JWT_VALIDATION_ENABLED", props, "JWT_VALIDATION_ENABLED", "false"));
        config.keycloakIssuer       = getEnv("MCP_KEYCLOAK_ISSUER", props, "KEYCLOAK_ISSUER",
                "https://auth.ecoskiller.com/realms/");
        config.rateLimitPerMinute   = Integer.parseInt(
                getEnv("MCP_RATE_LIMIT_PER_MINUTE", props, "RATE_LIMIT_PER_MINUTE", "100"));

        String logLevelStr = getEnv("MCP_LOG_LEVEL", props, "LOG_LEVEL",
                config.environment.equals("prod") ? "WARNING" : "INFO");
        config.logLevel = parseLevel(logLevelStr);

        LOGGER.info("ServerConfig loaded: env=" + config.environment +
                ", jwtValidation=" + config.jwtValidationEnabled +
                ", rateLimit=" + config.rateLimitPerMinute + "/min");

        return config;
    }

    private static String getEnv(String envKey, Properties props, String propKey, String defaultValue) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isBlank()) return envVal;
        return props.getProperty(propKey, defaultValue);
    }

    private static Level parseLevel(String level) {
        return switch (level.toUpperCase()) {
            case "DEBUG", "FINE"  -> Level.FINE;
            case "INFO"           -> Level.INFO;
            case "WARNING", "WARN"-> Level.WARNING;
            case "ERROR", "SEVERE"-> Level.SEVERE;
            default               -> Level.INFO;
        };
    }

    public boolean isJwtValidationEnabled() { return jwtValidationEnabled; }
    public String  getKeycloakIssuer()      { return keycloakIssuer; }
    public int     getRateLimitPerMinute()   { return rateLimitPerMinute; }
    public Level   getLogLevel()             { return logLevel; }
    public String  getEnvironment()          { return environment; }
}
