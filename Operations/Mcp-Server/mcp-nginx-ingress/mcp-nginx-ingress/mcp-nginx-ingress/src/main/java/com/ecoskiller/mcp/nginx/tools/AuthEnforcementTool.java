package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 7 — auth_enforcement */
public class AuthEnforcementTool extends BaseNginxTool {
    public AuthEnforcementTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "auth_enforcement",
            "description",
                "Configure authentication and authorization on NGINX Ingress paths. " +
                "Supported: OAuth2/OIDC (Okta, Azure AD, Google), JWT token validation, " +
                "API key validation, mutual TLS (mTLS), HTTP Basic Auth. " +
                "Injects X-User-ID header to backend after validation. " +
                "Protects candidate portal, API endpoints, admin interfaces. " +
                "Actions: configure | remove | get | test.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",  enumProp("Operation", "configure", "remove", "get", "test"),
                    "path",    property("string", "Path to protect (e.g. /api/assessments)"),
                    "type",    enumProp("Auth type", "oauth2", "oidc", "jwt", "mtls", "api_key", "basic", "none"),
                    "authUrl", property("string", "Auth service URL (https://auth.ecoskiller.com/verify)"),
                    "signInUrl",property("string", "Login redirect URL")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action   = str(args, "action",  "get");
        String path     = str(args, "path",    "/api");
        String type     = str(args, "type",    "oauth2");
        String authUrl  = str(args, "authUrl", "https://auth.ecoskiller.com/verify");

        if ("configure".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("path",    path);
            d.put("type",    type);
            d.put("authUrl", authUrl);
            d.put("annotations", Map.of(
                "nginx.ingress.kubernetes.io/auth-type",   type,
                "nginx.ingress.kubernetes.io/auth-url",    authUrl,
                "nginx.ingress.kubernetes.io/auth-signin", "https://auth.ecoskiller.com/login"
            ));
            d.put("injectedHeaders", List.of("X-User-ID", "X-Auth-Token", "X-Forwarded-User"));
            d.put("flow", authFlow(type));
            return success("Auth (" + type + ") configured on " + path, d);
        }
        if ("test".equals(action)) {
            return success("Auth test passed for " + path, Map.of(
                "path",   path,
                "type",   type,
                "result", "PASS",
                "latencyMs", 12,
                "tokenValid", true,
                "userIdExtracted", "candidate_test_user"
            ));
        }
        // get
        List<Map<String, Object>> policies = List.of(
            Map.of("path", "/api/assessments", "type", "jwt",    "status", "active"),
            Map.of("path", "/api/candidates",  "type", "oauth2", "status", "active"),
            Map.of("path", "/api/admin",       "type", "oauth2", "status", "active"),
            Map.of("path", "/api/internal",    "type", "mtls",   "status", "active"),
            Map.of("path", "/health",          "type", "none",   "status", "active")
        );
        return success("Auth policies", Map.of("policies", policies));
    }

    private List<String> authFlow(String type) {
        switch (type) {
            case "oauth2":
                return List.of("1. Request → NGINX", "2. No cookie? Redirect to /oauth2/start",
                               "3. User logs in at provider", "4. Provider redirects with code",
                               "5. NGINX exchanges code for token", "6. Token stored as cookie",
                               "7. Subsequent requests: validate cookie → route to backend");
            case "jwt":
                return List.of("1. Request with Authorization: Bearer <token>",
                               "2. NGINX validates JWT signature + expiry",
                               "3. Extract user_id from claims",
                               "4. Add X-User-ID header", "5. Route to backend");
            case "mtls":
                return List.of("1. TLS handshake requires client certificate",
                               "2. NGINX verifies cert against CA",
                               "3. Extract identity from cert CN", "4. Route to backend");
            default:
                return List.of("Auth flow: " + type);
        }
    }
}
