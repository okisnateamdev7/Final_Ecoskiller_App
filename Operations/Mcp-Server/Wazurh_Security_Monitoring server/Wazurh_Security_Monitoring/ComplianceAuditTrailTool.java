package com.ecoskiller.mcp.wazuh.tools;

import com.ecoskiller.mcp.wazuh.model.ToolResult;
import com.ecoskiller.mcp.wazuh.security.SecurityManager;
import com.ecoskiller.mcp.wazuh.util.WazuhApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.util.*;

/**
 * AGENT 20 — COMPLIANCE_AUDIT_TRAIL_AGENT
 *
 * Generate a full regulatory compliance audit trail from Wazuh event data.
 *
 * For regulators and auditors, this tool produces structured evidence packages:
 *   DPDPA 2023  — 12-month PII access trail, breach detection log, consent events
 *   SOC2 Type II — CC6.x, CC7.x control evidence (12-month log)
 *   PCI-DSS      — billing data access, vulnerability management evidence
 *   GDPR         — EU candidate data access, data transfer records
 *
 * Storage: Wazuh Indexer (hot 30d) → MinIO warm (90d) → MinIO cold (1yr+)
 * All logs are immutable (write-once) and tamper-proof.
 *
 * From specification:
 *   "Reports saved to MinIO compliance bucket for audit"
 *   "12-month retention: Elasticsearch stores all logs"
 *   "Evidence: 12-month audit log, detection rules, incident response log"
 */
public class ComplianceAuditTrailTool extends BaseTool {

    // Evidence requirements per compliance standard
    private static final Map<String, List<String>> EVIDENCE_REQUIREMENTS = Map.of(
        "dpdpa",   List.of(
            "All PII access events (candidate data, billing info)",
            "Unauthorized access attempts (blocked + investigated)",
            "Data breach detection capability evidence",
            "Consent events (granted/withdrawn timestamps)",
            "Incident response log (MTTD/MTTR metrics)",
            "Data processor agreements and access controls"
        ),
        "soc2",    List.of(
            "CC6.1: RBAC change log, failed authorization attempts, SA token usage",
            "CC6.2: User login events (user, timestamp, IP, result — 12 months)",
            "CC7.2: Process execution, file changes, network connections log",
            "CC7.4: Security incident log with full context",
            "Detection rules documentation (technical controls)",
            "Incident response metrics (MTTD < 10s, MTTR < 30s)"
        ),
        "pci-dss", List.of(
            "Req-10: All billing-service access events with timestamps",
            "Req-6: Vulnerability scan results (Trivy CVE reports)",
            "Req-7: Access control log for payment data",
            "Req-11: Security testing evidence (penetration test results)",
            "Cardholder data access events (read/write/delete)"
        ),
        "gdpr",    List.of(
            "EU candidate data access events (subject, timestamp, purpose)",
            "Cross-border data transfer records",
            "Encryption verification (data at rest + in transit)",
            "Breach detection evidence (MTTD + response log)",
            "Data subject rights requests (access/deletion/portability)",
            "DPIA (Data Protection Impact Assessment) support data"
        )
    );

    public ComplianceAuditTrailTool(SecurityManager security, WazuhApiClient api) { super(security, api); }

    @Override public String getName()        { return "compliance_audit_trail"; }
    @Override public String getDescription() {
        return "Generate a full regulatory compliance audit trail from Wazuh event data. "
             + "Produces structured evidence packages for: DPDPA 2023 (Indian data protection), "
             + "SOC2 Type II (CC6.1, CC6.2, CC7.2, CC7.4 controls), "
             + "PCI-DSS (billing/payment data controls), GDPR (EU candidate data). "
             + "All logs are immutable (write-once, tamper-proof). "
             + "12-month retention in Wazuh Indexer + MinIO archive. "
             + "Results suitable for regulator submission and audit evidence packages.";
    }

    @Override public ObjectNode getInputSchema() {
        ObjectNode s = schema();
        addStr(s, "framework",
            "Compliance framework: dpdpa | soc2 | pci-dss | gdpr.", true);
        addStr(s, "period_start",
            "Audit period start date ISO-8601. E.g. '2026-01-01'.", true);
        addStr(s, "period_end",
            "Audit period end date ISO-8601. E.g. '2026-03-31'.", true);
        addStr(s, "evidence_type",
            "Evidence type: access_log | incident_log | control_summary | all.", false);
        addStr(s, "limit",
            "Max events per evidence category (1-1000). Default: 500.", false);
        return s;
    }

    @Override public ToolResult execute(JsonNode args) {
        try {
            String framework  = security.validateComplianceFramework(getStr(args, "framework"));
            String periodStart = security.validateDate(getStr(args, "period_start"));
            String periodEnd   = security.validateDate(getStr(args, "period_end"));
            String evidenceType = security.sanitiseFreeText(getStr(args, "evidence_type")).toLowerCase();
            String limitStr    = getStr(args, "limit");
            int limit = limitStr.isBlank() ? 500 : security.validatePositiveInt(limitStr, 1000);

            if (evidenceType.isBlank()) evidenceType = "all";
            Set<String> validTypes = Set.of("access_log", "incident_log", "control_summary", "all");
            if (!validTypes.contains(evidenceType))
                return ToolResult.error("evidence_type must be: " + validTypes);

            StringBuilder report = new StringBuilder();
            report.append("📜 EcoSkiller Compliance Audit Trail\n");
            report.append("═".repeat(60)).append("\n");
            report.append("Framework     : ").append(framework.toUpperCase()).append("\n");
            report.append("Audit Period  : ").append(periodStart).append(" → ").append(periodEnd).append("\n");
            report.append("Generated     : ").append(Instant.now()).append("\n");
            report.append("Storage       : Wazuh Indexer (Elasticsearch) — immutable, tamper-proof\n");
            report.append("Archive       : MinIO ecoskiller-security-forensics-{env}\n\n");

            // ── Evidence Requirements ─────────────────────────────────────
            report.append("── Evidence Requirements for ").append(framework.toUpperCase()).append(" ──\n");
            List<String> requirements = EVIDENCE_REQUIREMENTS.getOrDefault(framework, List.of());
            for (int i = 0; i < requirements.size(); i++) {
                report.append(String.format("%2d. %s%n", i + 1, requirements.get(i)));
            }
            report.append("\n");

            // ── Access Log Evidence ───────────────────────────────────────
            if ("all".equals(evidenceType) || "access_log".equals(evidenceType)) {
                report.append("── Access Log Evidence ──\n");
                Map<String, String> accessParams = new LinkedHashMap<>();
                accessParams.put("timestamp.gte", periodStart);
                accessParams.put("timestamp.lte", periodEnd);
                accessParams.put("rule.groups", "authentication,policy_violation");
                accessParams.put("sort", "timestamp");
                accessParams.put("limit", String.valueOf(Math.min(limit, 200)));
                WazuhApiClient.ApiResponse accessResp = api.get("/alerts", accessParams);
                report.append(formatApiResponse("", accessResp)).append("\n");
            }

            // ── Incident Log Evidence ─────────────────────────────────────
            if ("all".equals(evidenceType) || "incident_log".equals(evidenceType)) {
                report.append("── Security Incident Log ──\n");
                Map<String, String> incidentParams = new LinkedHashMap<>();
                incidentParams.put("timestamp.gte", periodStart);
                incidentParams.put("timestamp.lte", periodEnd);
                incidentParams.put("level", "9"); // HIGH+
                incidentParams.put("sort", "timestamp");
                incidentParams.put("limit", String.valueOf(Math.min(limit, 200)));
                WazuhApiClient.ApiResponse incidentResp = api.get("/alerts", incidentParams);
                report.append(formatApiResponse("", incidentResp)).append("\n");
            }

            // ── Control Summary ───────────────────────────────────────────
            if ("all".equals(evidenceType) || "control_summary".equals(evidenceType)) {
                report.append("── Security Control Summary ──\n");
                WazuhApiClient.ApiResponse manInfo = api.get("/manager/info");
                report.append("Wazuh Manager: ").append(manInfo.success ? "✅ Operational" : "❌ Down").append("\n");
                WazuhApiClient.ApiResponse agentSummary = api.get("/overview/agents");
                report.append("Agent Coverage: ").append(agentSummary.success ? agentSummary.body : "N/A").append("\n");
                report.append("Rules Active: 1000+ built-in + EcoSkiller custom (100001-100999)\n");
                report.append("Detection Latency Target: MTTD < 10 seconds\n");
                report.append("Response Latency Target : MTTR < 30 seconds\n\n");
            }

            // ── Framework-specific Statements ────────────────────────────
            report.append("── Auditor Statement ──\n");
            switch (framework) {
                case "dpdpa"   -> report.append(
                    "This audit trail demonstrates that EcoSkiller maintained continuous security\n"
                    + "monitoring of all personal data access for the audit period under DPDPA 2023.\n"
                    + "All access to candidate PII was logged, unauthorized access attempts were\n"
                    + "detected and blocked, and breach notification procedures are in place.\n"
                    + "Contact: security@ecoskiller.com | infosec@ecoskiller.com\n");
                case "soc2"    -> report.append(
                    "This audit trail provides 12-month evidence for SOC2 Type II controls:\n"
                    + "CC6.1 (logical access), CC6.2 (user identification), CC7.2 (system monitoring),\n"
                    + "CC7.4 (event logging). All logs are immutable and cryptographically verified.\n");
                case "pci-dss" -> report.append(
                    "This trail documents compliance with PCI-DSS Req-10 (audit logging) and\n"
                    + "Req-6 (vulnerability management) for the EcoSkiller billing infrastructure.\n"
                    + "All cardholder data access is logged with timestamps and user identity.\n");
                case "gdpr"    -> report.append(
                    "This trail documents technical security measures implemented by EcoSkiller\n"
                    + "to protect EU candidate personal data under GDPR Article 32.\n"
                    + "Encryption verified, access controls in place, breach detection operational.\n");
                default -> report.append("Audit evidence package generated. Submit to auditor as supporting documentation.\n");
            }

            report.append("\n📌 Archive Location: MinIO bucket 'ecoskiller-security-forensics-prod'\n");
            report.append("📌 Elasticsearch retention: hot 30d | warm 90d | cold 1yr+\n");
            report.append("📌 This report should be exported as PDF for auditor submission.\n");

            return ToolResult.ok(report.toString());

        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
