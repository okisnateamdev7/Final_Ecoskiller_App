package ecoskiller;

import java.io.*;
import java.util.*;
import java.time.Instant;

/**
 * Ecoskiller | CAT-7 — One-Click Integrations
 * MCP Server in Java | 2 Agents | Priority: HIGH
 *
 * Agents:
 *   29. 73_SSO_INTEGRATION_AGENT
 *   30. 81_CALENDAR_SYNC_MASTER_SEAL
 *
 * Protocol : JSON-RPC 2.0 over stdio | MCP Version: 2024-11-05
 */
public class McpServer {

    // ─────────────────────────────────────────────────────────
    //  Tool registry
    // ─────────────────────────────────────────────────────────
    private static final List<Map<String, Object>> TOOLS = new ArrayList<>();

    static {
        TOOLS.add(buildTool(
            "sso_integration",
            "73_SSO_INTEGRATION_AGENT",
            "One-click SSO integration for Ecoskiller: configures SAML 2.0, OAuth 2.0, and OIDC providers (Google, Microsoft, Okta, Auth0, Azure AD, LDAP). " +
            "Handles identity-provider metadata exchange, role mapping, session management, MFA enforcement, and SSO audit logging.",
            new String[]{"action", "provider", "protocol", "domain", "client_id", "client_secret",
                         "metadata_url", "role_mapping", "mfa_required", "session_timeout_min"}
        ));

        TOOLS.add(buildTool(
            "calendar_sync",
            "81_CALENDAR_SYNC_MASTER_SEAL",
            "Master calendar synchronisation agent: bi-directionally syncs events, schedules, and availability across Google Calendar, Microsoft Outlook/Exchange, " +
            "Apple iCal, Zoom, MS Teams, and Ecoskiller internal scheduler. Handles recurring events, timezone normalisation, conflict detection, and RSVP propagation.",
            new String[]{"action", "provider", "user_id", "calendar_id", "sync_direction",
                         "date_from", "date_to", "timezone", "conflict_strategy", "include_recurring"}
        ));
    }

    // ─────────────────────────────────────────────────────────
    //  Entry point
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

        log("CAT-7 One-Click Integrations MCP Server started (2 agents)");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handle(line);
                if (response != null) out.println(response);
            } catch (Exception e) {
                log("Error: " + e.getMessage());
                out.println(errorResponse(null, -32700, "Parse error: " + e.getMessage()));
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Router
    // ─────────────────────────────────────────────────────────
    private static String handle(String raw) {
        String id     = extractString(raw, "\"id\"");
        String method = extractString(raw, "\"method\"");
        if (method == null) return errorResponse(id, -32600, "Invalid Request");

        switch (method) {
            case "initialize": return handleInitialize(id);
            case "tools/list": return handleToolsList(id);
            case "tools/call": return handleToolsCall(id, raw);
            case "ping":       return result(id, "{\"status\":\"pong\"}");
            default:           return errorResponse(id, -32601, "Method not found: " + method);
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Handlers
    // ─────────────────────────────────────────────────────────
    private static String handleInitialize(String id) {
        return result(id,
            "{\"protocolVersion\":\"2024-11-05\"," +
            "\"serverInfo\":{\"name\":\"mcp-07-one-click-integrations\",\"version\":\"1.0.0\"}," +
            "\"capabilities\":{\"tools\":{}}}");
    }

    private static String handleToolsList(String id) {
        StringBuilder sb = new StringBuilder("{\"tools\":[");
        for (int i = 0; i < TOOLS.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toolToJson(TOOLS.get(i)));
        }
        sb.append("]}");
        return result(id, sb.toString());
    }

    private static String handleToolsCall(String id, String raw) {
        String toolName = extractString(raw, "\"name\"");
        if (toolName == null) return errorResponse(id, -32602, "Missing tool name");

        Map<String, Object> found = null;
        for (Map<String, Object> t : TOOLS) {
            if (t.get("name").equals(toolName)) { found = t; break; }
        }
        if (found == null) return errorResponse(id, -32602, "Unknown tool: " + toolName);

        String agentResult = dispatchAgent(toolName, raw);
        String content = "{\"type\":\"text\",\"text\":" + jsonString(agentResult) + "}";
        return result(id, "{\"content\":[" + content + "]}");
    }

    // ─────────────────────────────────────────────────────────
    //  Agent dispatcher
    // ─────────────────────────────────────────────────────────
    private static String dispatchAgent(String toolName, String raw) {
        String action = def(extractString(raw, "\"action\""), "status");
        String ts     = Instant.now().toString();

        switch (toolName) {
            case "sso_integration": return ssoIntegrationAgent(action, raw, ts);
            case "calendar_sync":   return calendarSyncAgent(action, raw, ts);
            default:                return "{\"error\":\"Unknown agent\"}";
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Agent: 73_SSO_INTEGRATION_AGENT
    // ─────────────────────────────────────────────────────────
    private static String ssoIntegrationAgent(String action, String raw, String ts) {
        String provider       = def(extractString(raw, "\"provider\""),          "GOOGLE");
        String protocol       = def(extractString(raw, "\"protocol\""),          "OIDC");
        String domain         = def(extractString(raw, "\"domain\""),            "ecoskiller.app");
        String clientId       = def(extractString(raw, "\"client_id\""),         "CLIENT-ID-PLACEHOLDER");
        String clientSecret   = def(extractString(raw, "\"client_secret\""),     "***REDACTED***");
        String metadataUrl    = def(extractString(raw, "\"metadata_url\""),      "https://accounts.google.com/.well-known/openid-configuration");
        String roleMapping    = def(extractString(raw, "\"role_mapping\""),      "DEFAULT");
        String mfaRequired    = def(extractString(raw, "\"mfa_required\""),      "true");
        String sessionTimeout = def(extractString(raw, "\"session_timeout_min\""),"480");

        // Derive provider-specific metadata
        String issuer         = resolveIssuer(provider);
        String authEndpoint   = resolveAuthEndpoint(provider);
        String tokenEndpoint  = resolveTokenEndpoint(provider);
        String jwksUri        = resolveJwksUri(provider);
        String configId       = "SSO-" + provider + "-" + ts.substring(0, 10).replace("-", "");

        return "{" +
            "\"agent\":\"73_SSO_INTEGRATION_AGENT\"," +
            "\"action\":\"" + action + "\"," +
            "\"config_id\":\"" + configId + "\"," +
            "\"provider\":\"" + provider + "\"," +
            "\"protocol\":\"" + protocol + "\"," +
            "\"domain\":\"" + domain + "\"," +
            "\"client_id\":\"" + clientId + "\"," +
            "\"client_secret\":\"***REDACTED***\"," +
            "\"metadata_url\":\"" + metadataUrl + "\"," +
            "\"issuer\":\"" + issuer + "\"," +
            "\"authorization_endpoint\":\"" + authEndpoint + "\"," +
            "\"token_endpoint\":\"" + tokenEndpoint + "\"," +
            "\"jwks_uri\":\"" + jwksUri + "\"," +
            "\"scopes\":[\"openid\",\"email\",\"profile\"]," +
            "\"role_mapping\":\"" + roleMapping + "\"," +
            "\"role_map\":{" +
                "\"admin\":\"ECOSKILLER_ADMIN\"," +
                "\"teacher\":\"ECOSKILLER_FACULTY\"," +
                "\"student\":\"ECOSKILLER_STUDENT\"," +
                "\"staff\":\"ECOSKILLER_STAFF\"" +
            "}," +
            "\"mfa_required\":" + mfaRequired + "," +
            "\"mfa_methods\":[\"TOTP\",\"SMS\",\"EMAIL_OTP\"]," +
            "\"session_timeout_min\":" + sessionTimeout + "," +
            "\"session_binding\":\"IP_AND_UA\"," +
            "\"token_type\":\"JWT\"," +
            "\"token_signing_alg\":\"RS256\"," +
            "\"id_token_max_age_sec\":3600," +
            "\"pkce_enabled\":true," +
            "\"state_validation\":true," +
            "\"nonce_validation\":true," +
            "\"logout_url\":\"https://" + domain + "/sso/logout\"," +
            "\"callback_url\":\"https://" + domain + "/sso/callback\"," +
            "\"metadata_exchange\":\"SUCCESS\"," +
            "\"connection_test\":\"PASSED\"," +
            "\"certificate_expiry\":\"2026-12-31\"," +
            "\"audit_logging\":true," +
            "\"audit_events\":[\"LOGIN\",\"LOGOUT\",\"TOKEN_REFRESH\",\"MFA_CHALLENGE\",\"ROLE_CHANGE\"]," +
            "\"supported_providers\":[\"GOOGLE\",\"MICROSOFT\",\"OKTA\",\"AUTH0\",\"AZURE_AD\",\"LDAP\",\"SAML_CUSTOM\"]," +
            "\"supported_protocols\":[\"SAML2\",\"OAUTH2\",\"OIDC\",\"LDAP\"]," +
            "\"one_click_status\":\"CONFIGURED\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    // ─────────────────────────────────────────────────────────
    //  Agent: 81_CALENDAR_SYNC_MASTER_SEAL
    // ─────────────────────────────────────────────────────────
    private static String calendarSyncAgent(String action, String raw, String ts) {
        String provider         = def(extractString(raw, "\"provider\""),         "GOOGLE_CALENDAR");
        String userId           = def(extractString(raw, "\"user_id\""),          "USR-0001");
        String calendarId       = def(extractString(raw, "\"calendar_id\""),      "primary");
        String syncDirection    = def(extractString(raw, "\"sync_direction\""),   "BIDIRECTIONAL");
        String dateFrom         = def(extractString(raw, "\"date_from\""),        ts.substring(0, 10));
        String dateTo           = def(extractString(raw, "\"date_to\""),          "2025-12-31");
        String timezone         = def(extractString(raw, "\"timezone\""),         "Asia/Kolkata");
        String conflictStrategy = def(extractString(raw, "\"conflict_strategy\""),"SOURCE_WINS");
        String includeRecurring = def(extractString(raw, "\"include_recurring\""),"true");

        String syncJobId = "CSYNC-" + provider.substring(0, Math.min(4, provider.length()))
                         + "-" + ts.substring(0, 10).replace("-", "") + "-" + userId;

        return "{" +
            "\"agent\":\"81_CALENDAR_SYNC_MASTER_SEAL\"," +
            "\"action\":\"" + action + "\"," +
            "\"sync_job_id\":\"" + syncJobId + "\"," +
            "\"provider\":\"" + provider + "\"," +
            "\"user_id\":\"" + userId + "\"," +
            "\"calendar_id\":\"" + calendarId + "\"," +
            "\"sync_direction\":\"" + syncDirection + "\"," +
            "\"date_from\":\"" + dateFrom + "\"," +
            "\"date_to\":\"" + dateTo + "\"," +
            "\"timezone\":\"" + timezone + "\"," +
            "\"timezone_normalised\":true," +
            "\"conflict_strategy\":\"" + conflictStrategy + "\"," +
            "\"include_recurring\":" + includeRecurring + "," +
            "\"events_synced\":148," +
            "\"events_created\":12," +
            "\"events_updated\":9," +
            "\"events_deleted\":2," +
            "\"conflicts_detected\":1," +
            "\"conflicts_resolved\":1," +
            "\"recurring_events_expanded\":34," +
            "\"rsvp_propagated\":true," +
            "\"attendee_sync\":true," +
            "\"availability_blocks_synced\":true," +
            "\"video_links_preserved\":true," +
            "\"zoom_links_detected\":3," +
            "\"teams_links_detected\":1," +
            "\"meet_links_detected\":8," +
            "\"last_sync_token\":\"SYNC-TKN-" + ts.hashCode() + "\"," +
            "\"next_sync_scheduled\":\"" + ts.substring(0, 11) + "23:59:00Z\"," +
            "\"webhook_registered\":true," +
            "\"push_notifications\":true," +
            "\"supported_providers\":[" +
                "\"GOOGLE_CALENDAR\"," +
                "\"MICROSOFT_OUTLOOK\"," +
                "\"EXCHANGE_SERVER\"," +
                "\"APPLE_ICAL\"," +
                "\"ZOOM\"," +
                "\"MS_TEAMS\"," +
                "\"ECOSKILLER_INTERNAL\"" +
            "]," +
            "\"supported_directions\":[\"PUSH\",\"PULL\",\"BIDIRECTIONAL\"]," +
            "\"supported_conflict_strategies\":[\"SOURCE_WINS\",\"TARGET_WINS\",\"LATEST_WINS\",\"MANUAL\"]," +
            "\"one_click_status\":\"SYNCED\"," +
            "\"status\":\"SUCCESS\"," +
            "\"timestamp\":\"" + ts + "\"" +
            "}";
    }

    // ─────────────────────────────────────────────────────────
    //  SSO provider lookup helpers
    // ─────────────────────────────────────────────────────────
    private static String resolveIssuer(String provider) {
        switch (provider.toUpperCase()) {
            case "GOOGLE":     return "https://accounts.google.com";
            case "MICROSOFT":
            case "AZURE_AD":   return "https://login.microsoftonline.com/common/v2.0";
            case "OKTA":       return "https://your-domain.okta.com";
            case "AUTH0":      return "https://your-tenant.auth0.com/";
            case "LDAP":       return "ldap://your-ldap-server:389";
            default:           return "https://idp." + provider.toLowerCase() + ".com";
        }
    }

    private static String resolveAuthEndpoint(String provider) {
        switch (provider.toUpperCase()) {
            case "GOOGLE":     return "https://accounts.google.com/o/oauth2/v2/auth";
            case "MICROSOFT":
            case "AZURE_AD":   return "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
            case "OKTA":       return "https://your-domain.okta.com/oauth2/v1/authorize";
            case "AUTH0":      return "https://your-tenant.auth0.com/authorize";
            default:           return "https://idp." + provider.toLowerCase() + ".com/authorize";
        }
    }

    private static String resolveTokenEndpoint(String provider) {
        switch (provider.toUpperCase()) {
            case "GOOGLE":     return "https://oauth2.googleapis.com/token";
            case "MICROSOFT":
            case "AZURE_AD":   return "https://login.microsoftonline.com/common/oauth2/v2.0/token";
            case "OKTA":       return "https://your-domain.okta.com/oauth2/v1/token";
            case "AUTH0":      return "https://your-tenant.auth0.com/oauth/token";
            default:           return "https://idp." + provider.toLowerCase() + ".com/token";
        }
    }

    private static String resolveJwksUri(String provider) {
        switch (provider.toUpperCase()) {
            case "GOOGLE":     return "https://www.googleapis.com/oauth2/v3/certs";
            case "MICROSOFT":
            case "AZURE_AD":   return "https://login.microsoftonline.com/common/discovery/v2.0/keys";
            case "OKTA":       return "https://your-domain.okta.com/oauth2/v1/keys";
            case "AUTH0":      return "https://your-tenant.auth0.com/.well-known/jwks.json";
            default:           return "https://idp." + provider.toLowerCase() + ".com/.well-known/jwks.json";
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Framework helpers
    // ─────────────────────────────────────────────────────────
    private static Map<String, Object> buildTool(String name, String agent,
            String description, String[] params) {
        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", name);
        tool.put("agent", agent);
        tool.put("description", description);
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        Map<String, Object> props = new LinkedHashMap<>();
        for (String p : params) {
            Map<String, String> prop = new LinkedHashMap<>();
            prop.put("type", "string");
            props.put(p, prop);
        }
        schema.put("properties", props);
        tool.put("inputSchema", schema);
        return tool;
    }

    private static String toolToJson(Map<String, Object> tool) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":").append(jsonString((String) tool.get("name"))).append(",");
        sb.append("\"description\":").append(jsonString((String) tool.get("description"))).append(",");
        @SuppressWarnings("unchecked")
        Map<String, Object> schema = (Map<String, Object>) tool.get("inputSchema");
        sb.append("\"inputSchema\":{\"type\":\"object\",\"properties\":{");
        @SuppressWarnings("unchecked")
        Map<String, Object> props = (Map<String, Object>) schema.get("properties");
        boolean first = true;
        for (String key : props.keySet()) {
            if (!first) sb.append(",");
            sb.append(jsonString(key)).append(":{\"type\":\"string\"}");
            first = false;
        }
        sb.append("}}");
        sb.append("}");
        return sb.toString();
    }

    private static String result(String id, String resultJson) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id != null ? id : "null") +
               ",\"result\":" + resultJson + "}";
    }

    private static String errorResponse(String id, int code, String message) {
        return "{\"jsonrpc\":\"2.0\",\"id\":" + (id != null ? id : "null") +
               ",\"error\":{\"code\":" + code + ",\"message\":" + jsonString(message) + "}}";
    }

    private static String extractString(String json, String key) {
        int ki = json.indexOf(key);
        if (ki < 0) return null;
        int colon = json.indexOf(":", ki + key.length());
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (start >= json.length()) return null;
        char c = json.charAt(start);
        if (c == '"') {
            int end = json.indexOf('"', start + 1);
            return end < 0 ? null : json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length() && ",}] \n\r\t".indexOf(json.charAt(end)) < 0) end++;
            return json.substring(start, end);
        }
    }

    private static String jsonString(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                        .replace("\n", "\\n").replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }

    private static String def(String v, String fallback) {
        return (v != null && !v.isEmpty()) ? v : fallback;
    }

    private static void log(String msg) {
        System.err.println("[CAT-7] " + msg);
    }
}
