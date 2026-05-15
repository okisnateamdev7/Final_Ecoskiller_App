package com.ecoskiller.mcp.wazuh.tools;

import com.ecoskiller.mcp.wazuh.model.ToolResult;
import com.ecoskiller.mcp.wazuh.security.SecurityManager;
import com.ecoskiller.mcp.wazuh.util.WazuhApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

/**
 * AGENT 17 — FORENSIC_QUERY_AGENT
 *
 * Query the Wazuh forensic event archive for post-incident investigation.
 * All normalized Wazuh events (not just alerts) are streamed to:
 *   ClickHouse table: security.wazuh_events
 *
 * Enables forensic queries per specification:
 *   - "Find all network connections from pod X to IP Y in last 24 hours"
 *   - "Reconstruct attacker's command sequence"
 *   - "Identify which candidate data was accessed"
 *   - Breach scope determination for DPDPA 72-hour notification
 *
 * This tool constructs safe parameterized queries against the Wazuh
 * Indexer (Elasticsearch) API, NOT raw ClickHouse SQL (to avoid injection).
 * For direct ClickHouse queries, use the analytics-service API.
 */
public class ForensicQueryTool extends BaseTool {

    public ForensicQueryTool(SecurityManager security, WazuhApiClient api) { super(security, api); }

    @Override public String getName()        { return "forensic_query"; }
    @Override public String getDescription() {
        return "Query the Wazuh forensic event archive (Elasticsearch/ClickHouse) "
             + "for post-incident investigation. "
             + "Supports: timeline reconstruction (what happened during a breach), "
             + "network connection tracing (all connections from pod X to IP Y), "
             + "command sequence reconstruction (attacker's kubectl/exec commands), "
             + "PII data access audit (which candidate data was accessed — DPDPA evidence). "
             + "All events are immutable (write-once) and tamper-proof. "
             + "Results support DPDPA 72-hour breach notification and SOC2 audit evidence.";
    }

    @Override public ObjectNode getInputSchema() {
        ObjectNode s = schema();
        addStr(s, "query_type",
            "Query type: timeline | network_trace | command_sequence | pii_access | all_events.", true);
        addStr(s, "agent_id",   "Limit to events from a specific agent (node).", false);
        addStr(s, "source_ip",  "Source IP for network_trace queries.", false);
        addStr(s, "user",       "Username or ServiceAccount for user-activity queries.", false);
        addStr(s, "pod",        "Pod name for pod-specific forensic queries.", false);
        addStr(s, "from_date",  "Investigation window start (ISO-8601). Required.", true);
        addStr(s, "to_date",    "Investigation window end (ISO-8601). Required.", true);
        addStr(s, "limit",      "Max events to return (1-1000). Default: 200.", false);
        return s;
    }

    @Override public ToolResult execute(JsonNode args) {
        try {
            String queryType = security.sanitiseFreeText(getStr(args, "query_type")).toLowerCase();
            String from      = security.validateDate(getStr(args, "from_date"));
            String to        = security.validateDate(getStr(args, "to_date"));
            String limitStr  = getStr(args, "limit");
            int limit        = limitStr.isBlank() ? 200 : security.validatePositiveInt(limitStr, 1000);

            Set<String> validTypes = Set.of("timeline", "network_trace", "command_sequence", "pii_access", "all_events");
            if (!validTypes.contains(queryType))
                return ToolResult.error("query_type must be: " + validTypes);

            Map<String, String> params = new LinkedHashMap<>();
            params.put("timestamp.gte", from);
            params.put("timestamp.lte", to);
            params.put("limit", String.valueOf(limit));
            params.put("sort", "timestamp"); // chronological for forensics

            // Apply query-type specific filters
            switch (queryType) {
                case "network_trace" -> {
                    String ip = getStr(args, "source_ip");
                    if (!ip.isBlank()) params.put("data.srcip", security.validateIpAddress(ip));
                    params.put("rule.groups", "network");
                }
                case "command_sequence" -> {
                    String agentId = getStr(args, "agent_id");
                    if (!agentId.isBlank()) params.put("agent.id", security.validateId(agentId));
                    params.put("rule.groups", "system,authentication,privilege_escalation");
                }
                case "pii_access" -> {
                    String user = getStr(args, "user");
                    if (!user.isBlank()) params.put("data.dstuser", security.sanitiseFreeText(user));
                    params.put("rule.groups", "policy_violation,authentication");
                    // PII access is specifically billing-service and auth-service
                    params.put("data.srcip", "!127.0.0.1"); // exclude local
                }
                case "timeline" -> {
                    String pod = getStr(args, "pod");
                    if (!pod.isBlank()) params.put("agent.name", security.validateId(pod));
                    String agentId = getStr(args, "agent_id");
                    if (!agentId.isBlank()) params.put("agent.id", security.validateId(agentId));
                }
                // "all_events" — no additional filters
            }

            WazuhApiClient.ApiResponse resp = api.get("/alerts", params);

            StringBuilder report = new StringBuilder();
            report.append("🔬 Wazuh Forensic Investigation\n");
            report.append("═".repeat(55)).append("\n");
            report.append("Query Type    : ").append(queryType.toUpperCase()).append("\n");
            report.append("Time Window   : ").append(from).append(" → ").append(to).append("\n");
            report.append("Storage       : Wazuh Indexer (Elasticsearch) + ClickHouse security.wazuh_events\n");
            report.append("Integrity     : Immutable write-once logs (tamper-proof)\n\n");

            // DPDPA note for PII access queries
            if ("pii_access".equals(queryType)) {
                report.append("⚖️  DPDPA 2023 Note:\n");
                report.append("If PII breach confirmed: notify PDPB within 72 hours.\n");
                report.append("Identify affected candidate IDs from results below.\n\n");
            }

            report.append(formatApiResponse("── Forensic Events ──\n", resp));

            report.append("\n── ClickHouse Forensic Queries (for deeper analysis) ──\n");
            report.append("Connect to analytics-service and run:\n");
            switch (queryType) {
                case "pii_access" ->
                    report.append("SELECT user, resource, timestamp, result FROM security.wazuh_events\n"
                        + "WHERE timestamp BETWEEN '").append(from).append("' AND '").append(to).append("'\n"
                        + "AND rule_group IN ('policy_violation', 'authentication')\n"
                        + "ORDER BY timestamp;\n");
                case "network_trace" ->
                    report.append("SELECT src_ip, dst_ip, dst_port, timestamp, rule_id FROM security.wazuh_events\n"
                        + "WHERE timestamp BETWEEN '").append(from).append("' AND '").append(to).append("'\n"
                        + "AND rule_group = 'network' ORDER BY timestamp;\n");
                default ->
                    report.append("SELECT * FROM security.wazuh_events\n"
                        + "WHERE timestamp BETWEEN '").append(from).append("' AND '").append(to).append("'\n"
                        + "ORDER BY timestamp LIMIT ").append(limit).append(";\n");
            }

            return ToolResult.ok(report.toString());

        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
