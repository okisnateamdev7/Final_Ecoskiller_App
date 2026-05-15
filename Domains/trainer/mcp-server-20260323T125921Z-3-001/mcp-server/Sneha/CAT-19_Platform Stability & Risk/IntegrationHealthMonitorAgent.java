package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;

/**
 * INTEGRATION_HEALTH_MONITOR_AGENT
 *
 * Continuously monitors the health of all third-party and internal
 * integrations (payment gateways, SSO providers, LMS connectors,
 * WhatsApp, DigiLocker, etc.).  Tracks uptime, error rates, latency,
 * webhook delivery success, and schema-drift signals. Raises alerts
 * and suggests remediation when SLA thresholds are breached.
 */
public class IntegrationHealthMonitorAgent implements AgentHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String description() {
        return "Monitors health of all third-party and internal integrations (payment gateways, " +
               "SSO, LMS connectors, WhatsApp, DigiLocker, etc.). Tracks uptime, error rates, " +
               "latency, and webhook delivery. Raises alerts when SLA thresholds are breached.";
    }

    @Override
    public ObjectNode inputSchema() {
        ObjectNode schema = MAPPER.createObjectNode();
        schema.put("type", "object");

        ObjectNode props = MAPPER.createObjectNode();

        ObjectNode integrationId = MAPPER.createObjectNode();
        integrationId.put("type", "string");
        integrationId.put("description", "Integration ID or '*' to check all integrations");
        props.set("integration_id", integrationId);

        ObjectNode integrationTypes = MAPPER.createObjectNode();
        integrationTypes.put("type", "array");
        integrationTypes.put("description", "Filter by type: PAYMENT | SSO | LMS | WHATSAPP | DIGILOCKER | WEBHOOK | all");
        ObjectNode items = MAPPER.createObjectNode();
        items.put("type", "string");
        integrationTypes.set("items", items);
        props.set("integration_types", integrationTypes);

        ObjectNode windowMinutes = MAPPER.createObjectNode();
        windowMinutes.put("type", "integer");
        windowMinutes.put("description", "Health window in minutes (default 60)");
        windowMinutes.put("default", 60);
        props.set("window_minutes", windowMinutes);

        ObjectNode slaUptimePct = MAPPER.createObjectNode();
        slaUptimePct.put("type", "number");
        slaUptimePct.put("description", "Minimum acceptable uptime % before alert (default 99.5)");
        slaUptimePct.put("default", 99.5);
        props.set("sla_uptime_percent", slaUptimePct);

        schema.set("properties", props);

        com.fasterxml.jackson.databind.node.ArrayNode required = MAPPER.createArrayNode();
        required.add("integration_id");
        schema.set("required", required);

        return schema;
    }

    @Override
    public JsonNode execute(JsonNode args) {
        String integrationId  = args.path("integration_id").asText("*");
        int    windowMinutes  = args.path("window_minutes").asInt(60);
        double slaUptime      = args.path("sla_uptime_percent").asDouble(99.5);

        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",           "INTEGRATION_HEALTH_MONITOR_AGENT");
        result.put("category",        "CAT-19 — Platform Stability & Risk");
        result.put("timestamp",       Instant.now().toString());
        result.put("integration_id",  integrationId);
        result.put("window_minutes",  windowMinutes);
        result.put("sla_uptime_pct",  slaUptime);

        // --- Simulated integration health snapshot ---
        com.fasterxml.jackson.databind.node.ArrayNode integrations = MAPPER.createArrayNode();

        integrations.add(buildIntegration("PAYMENT-RAZORPAY",  "PAYMENT",   99.97, 210, 0.02, "HEALTHY"));
        integrations.add(buildIntegration("SSO-GOOGLE",        "SSO",       100.0, 95,  0.00, "HEALTHY"));
        integrations.add(buildIntegration("LMS-MOODLE",        "LMS",       98.80, 540, 1.20, "DEGRADED"));
        integrations.add(buildIntegration("WHATSAPP-META",     "WHATSAPP",  99.50, 180, 0.50, "HEALTHY"));
        integrations.add(buildIntegration("DIGILOCKER-GOV",    "DIGILOCKER",99.10, 320, 0.30, "HEALTHY"));

        result.set("integrations", integrations);

        // Summary
        ObjectNode summary = MAPPER.createObjectNode();
        summary.put("total_checked",   5);
        summary.put("healthy",         4);
        summary.put("degraded",        1);
        summary.put("down",            0);
        summary.put("overall_status",  "PARTIAL_DEGRADATION");
        result.set("summary", summary);

        // Alerts
        com.fasterxml.jackson.databind.node.ArrayNode alerts = MAPPER.createArrayNode();
        ObjectNode alert = MAPPER.createObjectNode();
        alert.put("integration",  "LMS-MOODLE");
        alert.put("severity",     "WARNING");
        alert.put("message",      "Uptime 98.80% below SLA threshold " + slaUptime + "%. High avg latency 540ms.");
        alert.put("action",       "Review Moodle server load; consider failover to LMS-CANVAS backup.");
        alerts.add(alert);
        result.set("alerts", alerts);

        return result;
    }

    private ObjectNode buildIntegration(String id, String type, double uptime,
                                        int avgLatencyMs, double errorRate, String status) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("integration_id",  id);
        node.put("type",            type);
        node.put("uptime_pct",      uptime);
        node.put("avg_latency_ms",  avgLatencyMs);
        node.put("error_rate_pct",  errorRate);
        node.put("status",          status);
        node.put("last_checked",    Instant.now().minusSeconds(30).toString());
        return node;
    }
}
