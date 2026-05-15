package com.ecoskiller.mcp.schema.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Schema Registry MCP server configuration.
 * All values sourced from environment variables — zero hardcoded secrets.
 *
 * Environment variables:
 *   APICURIO_BASE_URL       — Apicurio Registry REST base URL
 *                             (default: http://localhost:8080/apis/registry/v2)
 *   APICURIO_AUTH_TYPE      — none | basic | bearer  (default: none)
 *   APICURIO_USERNAME       — Basic auth username (if auth_type=basic)
 *   APICURIO_PASSWORD       — Basic auth password (if auth_type=basic)
 *   APICURIO_TOKEN          — Bearer token (if auth_type=bearer)
 *   APICURIO_TLS_VERIFY     — true/false — verify TLS cert (default: true)
 *   APICURIO_TIMEOUT_MS     — HTTP request timeout ms (default: 10000)
 *   APICURIO_DEFAULT_GROUP  — Default artifact group (default: ecoskiller)
 *   MCP_API_KEY             — Secret key required in every tool call
 *   MCP_API_KEY_2           — Optional rotation key
 *   DEFAULT_COMPATIBILITY   — BACKWARD|FORWARD|FULL|NONE (default: BACKWARD)
 *   MAX_VERSIONS_WARN       — Warn if subject versions exceed this (default: 100)
 */
public class SchemaRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(SchemaRegistryConfig.class);

    public final String   apicurioBaseUrl;
    public final String   authType;         // none | basic | bearer
    public final String   username;
    public final String   password;
    public final String   bearerToken;
    public final boolean  tlsVerify;
    public final int      timeoutMs;
    public final String   defaultGroup;
    public final String   defaultCompatibility;
    public final int      maxVersionsWarn;
    public final String[] validApiKeys;

    private SchemaRegistryConfig(String apicurioBaseUrl, String authType, String username,
                                  String password, String bearerToken, boolean tlsVerify,
                                  int timeoutMs, String defaultGroup, String defaultCompatibility,
                                  int maxVersionsWarn, String[] validApiKeys) {
        this.apicurioBaseUrl      = apicurioBaseUrl;
        this.authType             = authType;
        this.username             = username;
        this.password             = password;
        this.bearerToken          = bearerToken;
        this.tlsVerify            = tlsVerify;
        this.timeoutMs            = timeoutMs;
        this.defaultGroup         = defaultGroup;
        this.defaultCompatibility = defaultCompatibility;
        this.maxVersionsWarn      = maxVersionsWarn;
        this.validApiKeys         = validApiKeys;
    }

    public static SchemaRegistryConfig fromEnvironment() {
        String baseUrl   = env("APICURIO_BASE_URL",
                               "http://localhost:8080/apis/registry/v2");
        String authType  = env("APICURIO_AUTH_TYPE", "none").toLowerCase();
        String username  = env("APICURIO_USERNAME",  "");
        String password  = env("APICURIO_PASSWORD",  "");
        String token     = env("APICURIO_TOKEN",     "");
        boolean tlsVerify= !"false".equalsIgnoreCase(env("APICURIO_TLS_VERIFY", "true"));
        int timeout      = Integer.parseInt(env("APICURIO_TIMEOUT_MS", "10000"));
        String group     = env("APICURIO_DEFAULT_GROUP", "ecoskiller");
        String compat    = env("DEFAULT_COMPATIBILITY", "BACKWARD");
        int maxVer       = Integer.parseInt(env("MAX_VERSIONS_WARN", "100"));

        // API keys for MCP auth
        String k1 = env("MCP_API_KEY",   "");
        String k2 = env("MCP_API_KEY_2", "");
        String[] keys = java.util.Arrays.stream(new String[]{k1, k2})
                            .filter(k -> !k.isEmpty()).toArray(String[]::new);

        if (keys.length == 0) {
            log.warn("WARNING: MCP_API_KEY not set — all tool calls will be rejected");
        }

        // Production security checks
        if (!tlsVerify) {
            log.warn("SECURITY WARNING: TLS verification disabled (APICURIO_TLS_VERIFY=false)");
        }
        if ("basic".equals(authType) && (username.isEmpty() || password.isEmpty())) {
            log.warn("SECURITY WARNING: auth_type=basic but username/password not fully set");
        }

        // Validate compatibility mode
        if (!java.util.Set.of("BACKWARD","FORWARD","FULL","NONE",
                              "BACKWARD_TRANSITIVE","FORWARD_TRANSITIVE","FULL_TRANSITIVE")
                          .contains(compat.toUpperCase())) {
            log.warn("Unknown DEFAULT_COMPATIBILITY '{}' — defaulting to BACKWARD", compat);
            compat = "BACKWARD";
        }

        log.info("Apicurio config: url={}, auth={}, tlsVerify={}, group={}, compat={}",
                 baseUrl, authType, tlsVerify, group, compat);

        return new SchemaRegistryConfig(baseUrl, authType, username, password, token,
                                        tlsVerify, timeout, group, compat, maxVer, keys);
    }

    private static String env(String name, String def) {
        String v = System.getenv(name);
        return (v != null && !v.isEmpty()) ? v : def;
    }
}
