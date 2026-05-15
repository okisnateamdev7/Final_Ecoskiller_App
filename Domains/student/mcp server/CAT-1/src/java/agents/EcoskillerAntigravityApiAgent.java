package com.ecoskiller.mcp.platform.agents;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ECOSKILLER_ANTIGRAVITY_API_AGENT
 *
 * Manages the Ecoskiller public and internal API layer:
 * - API version lifecycle management
 * - Endpoint rate limit configuration
 * - API key provisioning and revocation
 * - OpenAPI spec generation and publishing
 */
@Service
public class EcoskillerAntigravityApiAgent {

    @Tool(name = "ecoskiller_api_list_endpoints",
          description = "List all registered API endpoints for a given service with their version, "
                      + "method, path, rate limits, and deprecation status.")
    public Map<String, Object> listEndpoints(
            @ToolParam(description = "Service name") String serviceName,
            @ToolParam(description = "API version filter, e.g. 'v1' | 'v2' | 'ALL'") String version) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",   "ECOSKILLER_ANTIGRAVITY_API_AGENT");
        result.put("service", serviceName);
        result.put("version", version);
        result.put("endpoints", List.of(
            Map.of("method", "GET",  "path", "/api/v2/students",      "rps_limit", 100, "deprecated", false),
            Map.of("method", "POST", "path", "/api/v2/students",      "rps_limit",  50, "deprecated", false),
            Map.of("method", "GET",  "path", "/api/v1/students",      "rps_limit",  50, "deprecated", true)
        ));
        result.put("total", 3);
        return result;
    }

    @Tool(name = "ecoskiller_api_provision_key",
          description = "Provision a new API key for a tenant or internal service. "
                      + "Returns the key ID and the secret (shown only once).")
    public Map<String, Object> provisionApiKey(
            @ToolParam(description = "Tenant ID or service name requesting the key") String ownerId,
            @ToolParam(description = "Key scope: READ | WRITE | ADMIN") String scope,
            @ToolParam(description = "Expiry in days, 0 for no expiry") int expiryDays) {

        String keyId = "KEY-" + System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "ECOSKILLER_ANTIGRAVITY_API_AGENT");
        result.put("action",      "PROVISION_KEY");
        result.put("owner_id",    ownerId);
        result.put("scope",       scope);
        result.put("expiry_days", expiryDays);
        result.put("key_id",      keyId);
        result.put("secret",      "esk_" + keyId + "_SECRET_SHOWN_ONCE");
        result.put("status",      "ACTIVE");
        return result;
    }

    @Tool(name = "ecoskiller_api_deprecate_version",
          description = "Mark an API version as deprecated with a sunset date, "
                      + "and configure redirect to the new version.")
    public Map<String, Object> deprecateVersion(
            @ToolParam(description = "Service name") String serviceName,
            @ToolParam(description = "API version to deprecate, e.g. 'v1'") String version,
            @ToolParam(description = "Sunset date ISO-8601") String sunsetDate,
            @ToolParam(description = "New version to redirect consumers to") String newVersion) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "ECOSKILLER_ANTIGRAVITY_API_AGENT");
        result.put("action",      "DEPRECATE_VERSION");
        result.put("service",     serviceName);
        result.put("version",     version);
        result.put("sunset_date", sunsetDate);
        result.put("redirect_to", newVersion);
        result.put("status",      "DEPRECATED");
        return result;
    }

    @Tool(name = "ecoskiller_api_generate_openapi_spec",
          description = "Generate and return the OpenAPI 3.0 specification for a service. "
                      + "Can also publish it to the developer portal.")
    public Map<String, Object> generateOpenApiSpec(
            @ToolParam(description = "Service name") String serviceName,
            @ToolParam(description = "Publish to developer portal: true | false") boolean publishToPortal) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "ECOSKILLER_ANTIGRAVITY_API_AGENT");
        result.put("action",      "GENERATE_OPENAPI_SPEC");
        result.put("service",     serviceName);
        result.put("spec_version","3.0.3");
        result.put("endpoints",   24);
        result.put("published",   publishToPortal);
        result.put("spec_url",    "https://developer.ecoskiller.com/api/" + serviceName + "/openapi.json");
        result.put("status",      "GENERATED");
        return result;
    }
}
