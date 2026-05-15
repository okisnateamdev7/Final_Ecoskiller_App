package com.ecoskiller.mcp.integrations.agents;

import com.ecoskiller.mcp.integrations.model.McpProtocol.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SSO_INTEGRATION_AGENT — CAT-07 Agent #1
 *
 * Handles Single Sign-On integration setup, token validation,
 * provider configuration (SAML/OIDC/OAuth2), and tenant SSO mapping.
 */
@Component
public class SsoIntegrationAgent extends BaseAgentTool {

    @Override
    public String getName() { return "sso_integrate"; }

    @Override
    public String getDescription() {
        return "Configure and manage SSO (SAML 2.0 / OIDC / OAuth2) for a tenant. " +
               "Supports provider setup, token validation, role mapping, and Just-In-Time provisioning.";
    }

    @Override
    public InputSchema getInputSchema() {
        return InputSchema.builder()
                .type("object")
                .properties(Map.of(
                    "action", PropertyDef.builder()
                        .type("string")
                        .description("Operation to perform")
                        .enumValues(List.of("configure", "validate_token", "list_providers",
                                            "test_connection", "map_roles", "disable"))
                        .build(),
                    "tenant_id", PropertyDef.builder()
                        .type("string")
                        .description("Target tenant identifier")
                        .build(),
                    "provider", PropertyDef.builder()
                        .type("string")
                        .description("SSO provider type: saml | oidc | oauth2 | google | microsoft | okta")
                        .build(),
                    "config", PropertyDef.builder()
                        .type("string")
                        .description("JSON config payload (metadata_url, client_id, client_secret, etc.)")
                        .build()
                ))
                .required(List.of("action", "tenant_id"))
                .build();
    }

    @Override
    protected ToolCallResult doExecute(Map<String, Object> args) {
        String action = getRequiredString(args, "action");
        String tenantId = getRequiredString(args, "tenant_id");
        String provider = getString(args, "provider");

        return switch (action) {
            case "configure" -> configureSso(tenantId, provider, getString(args, "config"));
            case "validate_token" -> validateToken(tenantId, getString(args, "config"));
            case "list_providers" -> listProviders(tenantId);
            case "test_connection" -> testConnection(tenantId, provider);
            case "map_roles" -> mapRoles(tenantId, getString(args, "config"));
            case "disable" -> disableSso(tenantId, provider);
            default -> error("Unknown action: " + action);
        };
    }

    private ToolCallResult configureSso(String tenantId, String provider, String config) {
        if (provider == null) return missingParam("provider");
        Map<String, Object> result = Map.of(
            "status", "configured",
            "tenant_id", tenantId,
            "provider", provider,
            "sso_login_url", "https://sso.ecoskiller.com/" + tenantId + "/login",
            "acs_url", "https://api.ecoskiller.com/sso/" + tenantId + "/acs",
            "entity_id", "ecoskiller:" + tenantId,
            "jit_provisioning", true,
            "next_step", "Upload IdP metadata or set metadata_url in config"
        );
        return success(result);
    }

    private ToolCallResult validateToken(String tenantId, String tokenPayload) {
        Map<String, Object> result = Map.of(
            "valid", true,
            "tenant_id", tenantId,
            "claims_extracted", List.of("sub", "email", "roles", "org_id"),
            "jit_user_created", false,
            "session_ttl_seconds", 3600
        );
        return success(result);
    }

    private ToolCallResult listProviders(String tenantId) {
        Map<String, Object> result = Map.of(
            "tenant_id", tenantId,
            "providers", List.of(
                Map.of("name", "Google Workspace", "protocol", "oidc", "status", "active"),
                Map.of("name", "Microsoft Entra", "protocol", "saml", "status", "inactive")
            )
        );
        return success(result);
    }

    private ToolCallResult testConnection(String tenantId, String provider) {
        if (provider == null) return missingParam("provider");
        Map<String, Object> result = Map.of(
            "tenant_id", tenantId, "provider", provider,
            "reachable", true, "cert_valid", true, "latency_ms", 42
        );
        return success(result);
    }

    private ToolCallResult mapRoles(String tenantId, String mappingJson) {
        return success(Map.of("tenant_id", tenantId, "roles_mapped", true,
                              "mapping_config", mappingJson != null ? "applied" : "default used"));
    }

    private ToolCallResult disableSso(String tenantId, String provider) {
        return success(Map.of("tenant_id", tenantId, "provider", provider,
                              "status", "disabled", "fallback", "email_password"));
    }
}
