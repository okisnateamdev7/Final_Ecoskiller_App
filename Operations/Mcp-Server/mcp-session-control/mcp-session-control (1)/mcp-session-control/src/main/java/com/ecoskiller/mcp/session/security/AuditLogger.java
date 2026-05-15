package com.ecoskiller.mcp.session.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;

/**
 * Structured audit log — every MCP tool call, result, and security event.
 * Output: JSON lines → Wazuh SIEM via stderr.
 * Secrets (api_key, token, jwt) always masked before logging.
 */
public class AuditLogger {

    private static final Logger audit = LoggerFactory.getLogger("AUDIT");
    private final ObjectMapper  json  = new ObjectMapper();

    public void logCall(String tool, JsonNode arguments) {
        try {
            ObjectNode safe = (ObjectNode) json.readTree(arguments.toString());
            mask(safe, "api_key"); mask(safe, "token"); mask(safe, "jwt");
            mask(safe, "participantToken"); mask(safe, "idempotencyKey");
            audit.info("{\"event\":\"TOOL_CALL\",\"tool\":\"{}\",\"ts\":\"{}\",\"args\":{}}", tool, now(), safe);
        } catch (Exception e) {
            audit.info("{\"event\":\"TOOL_CALL\",\"tool\":\"{}\",\"ts\":\"{}\"}", tool, now());
        }
    }

    public void logResult(String tool, boolean isError) {
        audit.info("{\"event\":\"TOOL_RESULT\",\"tool\":\"{}\",\"ts\":\"{}\",\"error\":{}}", tool, now(), isError);
    }

    public void logUnauthorised(String tool, String apiKey) {
        String safe = (apiKey != null && apiKey.length() > 4) ? apiKey.substring(0, 4) + "***" : "***";
        audit.warn("{\"event\":\"UNAUTHORISED\",\"tool\":\"{}\",\"ts\":\"{}\",\"key_prefix\":\"{}\"}", tool, now(), safe);
    }

    public void logError(String tool, String message) {
        audit.error("{\"event\":\"TOOL_ERROR\",\"tool\":\"{}\",\"ts\":\"{}\",\"message\":\"{}\"}", tool, now(), clean(message));
    }

    private void mask(ObjectNode n, String f) { if (n.has(f)) n.put(f, "***"); }
    private String now()         { return Instant.now().toString(); }
    private String clean(String s){ return s == null ? "" : s.replace("\n"," ").replace("\r"," "); }
}
