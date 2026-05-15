package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

/**
 * INSIDER_THREAT_MONITOR_AGENT
 *
 * Detects anomalous behaviour from internal users, admins, and service
 * accounts that may indicate data exfiltration, privilege abuse, or
 * sabotage. Cross-correlates access logs, role changes, unusual hours,
 * and bulk-export signals to produce a risk score and recommended actions.
 */
public class InsiderThreatMonitorAgent implements AgentHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String description() {
        return "Detects anomalous behaviour from internal users, admins, and service accounts " +
               "indicating data exfiltration, privilege abuse, or sabotage. Produces risk scores " +
               "and actionable recommendations from access-log and role-change signals.";
    }

    @Override
    public ObjectNode inputSchema() {
        ObjectNode schema = MAPPER.createObjectNode();
        schema.put("type", "object");

        ObjectNode props = MAPPER.createObjectNode();

        ObjectNode userId = MAPPER.createObjectNode();
        userId.put("type", "string");
        userId.put("description", "User ID, admin ID, or service-account ID to evaluate ('*' for sweep)");
        props.set("user_id", userId);

        ObjectNode windowHours = MAPPER.createObjectNode();
        windowHours.put("type", "integer");
        windowHours.put("description", "Analysis window in hours (default 24)");
        windowHours.put("default", 24);
        props.set("window_hours", windowHours);

        ObjectNode signals = MAPPER.createObjectNode();
        signals.put("type", "array");
        signals.put("description", "Signal types to evaluate: bulk_export | off_hours_access | privilege_escalation | failed_auth | data_delete | all");
        ObjectNode items = MAPPER.createObjectNode();
        items.put("type", "string");
        signals.set("items", items);
        props.set("signals", signals);

        ObjectNode riskThreshold = MAPPER.createObjectNode();
        riskThreshold.put("type", "number");
        riskThreshold.put("description", "Risk score threshold (0-100) to trigger an alert (default 65)");
        riskThreshold.put("default", 65);
        props.set("risk_threshold", riskThreshold);

        schema.set("properties", props);

        com.fasterxml.jackson.databind.node.ArrayNode required = MAPPER.createArrayNode();
        required.add("user_id");
        schema.set("required", required);

        return schema;
    }

    @Override
    public JsonNode execute(JsonNode args) {
        String userId       = args.path("user_id").asText("*");
        int    windowHours  = args.path("window_hours").asInt(24);
        double riskThreshold = args.path("risk_threshold").asDouble(65.0);

        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",           "INSIDER_THREAT_MONITOR_AGENT");
        result.put("category",        "CAT-19 — Platform Stability & Risk");
        result.put("timestamp",       Instant.now().toString());
        result.put("evaluated_user",  userId);
        result.put("window_hours",    windowHours);
        result.put("risk_threshold",  riskThreshold);

        // --- Simulated threat analysis ---
        ObjectNode analysis = MAPPER.createObjectNode();
        double riskScore = 18.5;  // low risk in simulation
        analysis.put("risk_score",       riskScore);
        analysis.put("risk_level",       classifyRisk(riskScore));
        analysis.put("alert_triggered",  riskScore >= riskThreshold);

        com.fasterxml.jackson.databind.node.ArrayNode detectedSignals = MAPPER.createArrayNode();
        // No anomalies in baseline simulation
        analysis.set("detected_signals", detectedSignals);

        com.fasterxml.jackson.databind.node.ArrayNode accessSummary = MAPPER.createArrayNode();
        ObjectNode entry = MAPPER.createObjectNode();
        entry.put("resource",    "platform/reports");
        entry.put("action",      "READ");
        entry.put("count",       12);
        entry.put("first_seen",  Instant.now().minusSeconds(3600 * windowHours).toString());
        entry.put("last_seen",   Instant.now().minusSeconds(300).toString());
        entry.put("off_hours",   false);
        accessSummary.add(entry);
        analysis.set("access_summary", accessSummary);

        result.set("threat_analysis", analysis);

        // Recommendations
        com.fasterxml.jackson.databind.node.ArrayNode recs = MAPPER.createArrayNode();
        if (riskScore < riskThreshold) {
            recs.add("No immediate action required. Continue periodic monitoring.");
        } else {
            recs.add("ALERT: Risk score exceeds threshold — review access logs immediately.");
            recs.add("Notify security team and suspend privileged sessions.");
            recs.add("Preserve audit trail and initiate forensic review via FORENSICS_AGENT.");
        }
        result.set("recommendations", recs);

        return result;
    }

    private String classifyRisk(double score) {
        if (score < 30)  return "LOW";
        if (score < 60)  return "MEDIUM";
        if (score < 80)  return "HIGH";
        return "CRITICAL";
    }
}
