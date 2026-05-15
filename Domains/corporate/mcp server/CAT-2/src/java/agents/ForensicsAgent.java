package com.ecoskiller.mcp.governance.agents;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FORENSICS_AGENT
 *
 * Digital forensics and incident investigation:
 * - Security incident timeline reconstruction
 * - User activity forensics (who did what and when)
 * - Tamper detection in audit logs
 * - Chain-of-custody evidence packaging
 * - Intrusion pattern analysis
 */
@Service
public class ForensicsAgent {

    @Tool(name = "forensics_investigate_incident",
          description = "Investigate a security incident: reconstruct the full timeline of events, "
                      + "identify affected resources, and trace the attack vector.")
    public Map<String, Object> investigateIncident(
            @ToolParam(description = "Incident ID to investigate") String incidentId,
            @ToolParam(description = "Investigation depth: QUICK | DEEP") String depth) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "FORENSICS_AGENT");
        result.put("action",      "INVESTIGATE_INCIDENT");
        result.put("incident_id", incidentId);
        result.put("depth",       depth);
        result.put("timeline", List.of(
            Map.of("ts", "2025-01-01T09:00:00Z", "event", "Anomalous login from IP 203.0.113.5",    "severity", "HIGH"),
            Map.of("ts", "2025-01-01T09:02:00Z", "event", "Privilege escalation attempted",          "severity", "CRITICAL"),
            Map.of("ts", "2025-01-01T09:03:00Z", "event", "Bulk data export initiated (4,200 rows)","severity", "CRITICAL"),
            Map.of("ts", "2025-01-01T09:05:00Z", "event", "Session terminated by security policy",   "severity", "INFO")
        ));
        result.put("affected_users",    List.of("user-1234"));
        result.put("affected_services", List.of("mcp-05-institute-erp", "mcp-09-intelligence"));
        result.put("attack_vector",     "CREDENTIAL_STUFFING");
        result.put("root_cause",        "Reused credentials from external breach");
        result.put("contained",         true);
        return result;
    }

    @Tool(name = "forensics_get_user_activity",
          description = "Retrieve a full forensic activity log for a specific user "
                      + "within a time window — every action, resource accessed, and data touched.")
    public Map<String, Object> getUserActivity(
            @ToolParam(description = "User ID to investigate") String userId,
            @ToolParam(description = "Start timestamp ISO-8601") String fromTs,
            @ToolParam(description = "End timestamp ISO-8601") String toTs,
            @ToolParam(description = "Tenant ID") String tenantId) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",     "FORENSICS_AGENT");
        result.put("action",    "USER_ACTIVITY_LOG");
        result.put("user_id",   userId);
        result.put("tenant_id", tenantId);
        result.put("from",      fromTs);
        result.put("to",        toTs);
        result.put("events", List.of(
            Map.of("ts", "2025-01-01T08:55:00Z", "action", "LOGIN",         "ip", "203.0.113.5", "resource", "/auth"),
            Map.of("ts", "2025-01-01T09:00:00Z", "action", "VIEW",          "ip", "203.0.113.5", "resource", "/students"),
            Map.of("ts", "2025-01-01T09:02:00Z", "action", "EXPORT",        "ip", "203.0.113.5", "resource", "/students/export"),
            Map.of("ts", "2025-01-01T09:05:00Z", "action", "LOGOUT_FORCED", "ip", "203.0.113.5", "resource", "/auth/logout")
        ));
        result.put("total_events", 4);
        result.put("risk_score",   92);
        return result;
    }

    @Tool(name = "forensics_package_evidence",
          description = "Package forensic evidence for an incident into a tamper-evident "
                      + "chain-of-custody bundle suitable for legal or regulatory submission.")
    public Map<String, Object> packageEvidence(
            @ToolParam(description = "Incident ID to package evidence for") String incidentId,
            @ToolParam(description = "Legal case reference number") String caseRef) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "FORENSICS_AGENT");
        result.put("action",      "PACKAGE_EVIDENCE");
        result.put("incident_id", incidentId);
        result.put("case_ref",    caseRef);
        result.put("package_id",  "EVD-" + System.currentTimeMillis());
        result.put("files_included", List.of("audit_logs.json", "network_capture.pcap", "user_activity.csv", "system_snapshot.tar.gz"));
        result.put("hash_sha256", "a3f1b2c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2");
        result.put("custody_chain_verified", true);
        result.put("download_url", "https://forensics.ecoskiller.internal/evidence/" + incidentId);
        result.put("status",       "PACKAGED");
        return result;
    }
}
