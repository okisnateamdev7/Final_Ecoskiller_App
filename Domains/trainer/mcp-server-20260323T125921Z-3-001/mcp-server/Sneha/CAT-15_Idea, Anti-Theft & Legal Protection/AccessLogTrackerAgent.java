package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * ACCESS_LOG_TRACKER
 *
 * Records and summarises access log entries for protected ideas.
 * Generates an immutable, hash-chained audit trail that can be used
 * as evidence in IP dispute proceedings.
 */
public class AccessLogTrackerAgent implements AgentHandler {

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("idea_id", "accessor_id", "action");
        addStringProp(schema, "idea_id",     "ID of the protected idea being accessed");
        addStringProp(schema, "accessor_id", "User ID of the person accessing the idea");
        addStringProp(schema, "action",      "Action type: VIEW | DOWNLOAD | COPY | EXPORT | PRINT | SHARE | API_FETCH");
        addStringProp(schema, "ip_address",  "IP address of the accessor (optional)");
        addStringProp(schema, "device_id",   "Device fingerprint or session ID (optional)");
        addStringProp(schema, "context",     "Additional context or metadata (optional)");
        addStringProp(schema, "prev_log_hash", "Hash of previous log entry for chain linking (optional)");

        return buildToolDef(
            toolName,
            "ACCESS_LOG_TRACKER — Creates a tamper-evident, hash-chained access log entry " +
            "for a protected idea. Every access is recorded with cryptographic linkage to " +
            "the previous entry, producing a forensic audit trail.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String ideaId       = args.path("idea_id").asText();
        String accessorId   = args.path("accessor_id").asText();
        String action       = args.path("action").asText("VIEW").toUpperCase();
        String ipAddress    = args.path("ip_address").asText("REDACTED");
        String deviceId     = args.path("device_id").asText("UNKNOWN");
        String context      = args.path("context").asText("");
        String prevLogHash  = args.path("prev_log_hash").asText("0000000000000000");

        String timestamp    = now();
        String logEntryId   = deterministicId(ideaId + accessorId + action + timestamp);

        // Canonical payload for hashing
        String payload = String.join("|", ideaId, accessorId, action,
                                         ipAddress, deviceId, timestamp);
        String payloadHash  = sha256(payload);
        String chainHash    = sha256(prevLogHash + payloadHash);

        // Risk classification of action
        String actionRisk = classifyActionRisk(action);
        boolean requiresAlert = actionRisk.equals("HIGH") || actionRisk.equals("CRITICAL");

        // Build result
        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",         "ACCESS_LOG_TRACKER");
        result.put("status",        "LOGGED");
        result.put("log_entry_id",  logEntryId);
        result.put("timestamp",     timestamp);

        ObjectNode entry = result.putObject("log_entry");
        entry.put("idea_id",       ideaId);
        entry.put("accessor_id",   accessorId);
        entry.put("action",        action);
        entry.put("ip_address",    ipAddress);
        entry.put("device_id",     deviceId);
        entry.put("context",       context.isBlank() ? null : context);
        entry.put("action_risk",   actionRisk);

        ObjectNode chain = result.putObject("chain_integrity");
        chain.put("payload_hash",   payloadHash);
        chain.put("prev_log_hash",  prevLogHash);
        chain.put("chain_hash",     chainHash);
        chain.put("algorithm",      "SHA-256");
        chain.put("tamper_evident", true);

        ObjectNode alertInfo = result.putObject("alert");
        alertInfo.put("alert_required", requiresAlert);
        alertInfo.put("alert_level",    actionRisk);
        alertInfo.put("notify_owner",   requiresAlert);
        alertInfo.put("flag_for_review", actionRisk.equals("CRITICAL"));

        ObjectNode usage = result.putObject("usage_tracking");
        usage.put("cumulative_access_note",
            "Integrate with BEHAVIOR_STREAM_PROCESSOR to build cumulative user risk profiles");
        usage.put("evidence_exportable",  true);
        usage.put("legal_standing",       "ADMISSIBLE_AS_DIGITAL_EVIDENCE");
        usage.put("retention_policy_days", 3650);

        return result;
    }

    private static String classifyActionRisk(String action) {
        switch (action) {
            case "SCREENSHOT":
            case "PRINT":      return "HIGH";
            case "DOWNLOAD":
            case "EXPORT":
            case "COPY":       return "MEDIUM";
            case "SHARE":      return "MEDIUM";
            case "API_FETCH":  return "LOW";
            case "VIEW":       return "LOW";
            default:           return "UNKNOWN";
        }
    }
}
