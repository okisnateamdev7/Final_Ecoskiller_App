package com.ecoskiller.mcp.wazuh.tools;

import com.ecoskiller.mcp.wazuh.model.ToolResult;
import com.ecoskiller.mcp.wazuh.security.SecurityManager;
import com.ecoskiller.mcp.wazuh.util.WazuhApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

/**
 * AGENT 13 — INCIDENT_RUNBOOK_AGENT
 *
 * Retrieve and execute EcoSkiller incident response runbooks.
 * Runbooks are defined in the Wazuh Security Monitoring Service Specification
 * and cover the 5 documented incident types:
 *
 *   RB-01: Suspicious Process in Production Pod (CRITICAL, severity 10)
 *   RB-02: Unauthorized Candidate Data Access (CRITICAL, severity 9)
 *   RB-03: Brute-Force Attack on Auth Service (HIGH, severity 8)
 *   RB-04: Wazuh Agent Offline (MEDIUM, severity 5)
 *   RB-05: False Positive Alert Tuning
 */
public class IncidentRunbookTool extends BaseTool {

    private static final Map<String, String> RUNBOOKS = new LinkedHashMap<>();

    static {
        RUNBOOKS.put("RB-01",
            "═══ RUNBOOK RB-01: Suspicious Process in Production Pod ═══\n"
            + "Trigger   : Wazuh alert 'kubectl exec as root in production pod'\n"
            + "Severity  : 10 (CRITICAL) | Rule: 5910 (Kubernetes-specific)\n"
            + "MITRE     : T1609 (Container Administration Command)\n\n"
            + "── Automated Response (< 10 seconds) ──\n"
            + "1. Pod killed immediately (response-automation-service)\n"
            + "2. Kafka event: security.wazuh_alert {severity:10, pod_name, timestamp}\n"
            + "3. Mattermost #security-incidents notified\n\n"
            + "── Manual Investigation Steps ──\n"
            + "1. Open Wazuh Dashboard → search pod_name in alerts\n"
            + "2. kubectl logs -n <namespace> <pod> — inspect container output\n"
            + "3. Review network connections: kubectl exec -n <ns> <pod> -- netstat -an\n"
            + "4. Check file changes: use tool 'fim_events' for affected agent\n"
            + "5. Copy suspicious files to MinIO forensics bucket for malware analysis\n\n"
            + "── Remediation ──\n"
            + "• Update PodSecurityPolicy to deny privileged exec\n"
            + "• Restart deployment with clean image from Harbor\n"
            + "• Scan image with Trivy: kubectl -n ops exec trivy -- trivy image <image>\n"
            + "• Update RBAC: remove exec permission from ServiceAccount\n");

        RUNBOOKS.put("RB-02",
            "═══ RUNBOOK RB-02: Unauthorized Candidate Data Access ═══\n"
            + "Trigger   : Wazuh alert 'ServiceAccount accessed candidate table without authorization flow'\n"
            + "Severity  : 9 (CRITICAL) | Rule: 100002 (custom EcoSkiller)\n"
            + "MITRE     : T1078 (Valid Accounts)\n\n"
            + "── Automated Response (< 10 seconds) ──\n"
            + "1. ServiceAccount token revoked (response-automation-service)\n"
            + "2. Kafka event published to security.wazuh_alert\n"
            + "3. Security team paged via PagerDuty + Mattermost\n\n"
            + "── Manual Investigation Steps ──\n"
            + "1. Determine WHAT data was accessed:\n"
            + "   → tool 'forensic_query' — query ClickHouse security.wazuh_events\n"
            + "   → \"SELECT * FROM security.wazuh_events WHERE pod=<pod> AND timestamp BETWEEN ...\"\n"
            + "2. Determine HOW LONG access was active\n"
            + "3. Determine if data was exfiltrated: check network connections\n\n"
            + "── DPDPA Compliance Actions ──\n"
            + "• If PII breach confirmed: notify PDPB (Personal Data Protection Board) within 72 hours\n"
            + "• Identify affected candidate IDs from forensic query\n"
            + "• Send breach notification to affected candidates\n"
            + "• Document incident in ClickHouse DR audit table\n");

        RUNBOOKS.put("RB-03",
            "═══ RUNBOOK RB-03: Brute-Force Attack on Auth Service ═══\n"
            + "Trigger   : Wazuh alert '50+ failed auth attempts from single IP in 5 minutes'\n"
            + "Severity  : 8 (HIGH) | Rule: 100001 (custom EcoSkiller)\n"
            + "MITRE     : T1110.001 (Brute Force: Password Guessing)\n\n"
            + "── Automated Response (< 5 seconds) ──\n"
            + "1. IP blocked in GCP Firewall: iptables -A INPUT -s <IP> -j DROP\n"
            + "2. Kafka event: security.wazuh_alert {rule_id:100001, severity:8}\n"
            + "3. Mattermost #security-incidents + PagerDuty paged\n\n"
            + "── Manual Investigation Steps ──\n"
            + "1. Check if internal IP (misconfigured client): tool 'alert_query' source_ip=<ip>\n"
            + "2. If internal: whitelist IP, investigate why client is hammering login\n"
            + "3. Check threat intel: tool 'threat_intelligence_check' check_type=ip, ip_address=<ip>\n"
            + "4. Verify credentials not compromised: force password reset for targeted accounts\n"
            + "5. Extend block if confirmed attack: tool 'active_response_trigger' action=block_ip\n\n"
            + "── Remediation ──\n"
            + "• Implement CAPTCHA / rate-limiting on auth-service /login endpoint\n"
            + "• Enable Keycloak brute-force protection (Keycloak admin console)\n"
            + "• Review auth-service rate limit settings\n");

        RUNBOOKS.put("RB-04",
            "═══ RUNBOOK RB-04: Wazuh Agent Offline ═══\n"
            + "Trigger   : Wazuh Manager reports agent offline > 10 minutes\n"
            + "Severity  : 5 (MEDIUM) | Rule: Agent Heartbeat\n\n"
            + "── Investigation Steps ──\n"
            + "1. Check agent status: tool 'agent_status' agent_id=<id>\n"
            + "2. Check pod logs: kubectl logs -n ops daemonset/wazuh-agent (on affected node)\n"
            + "3. Check node health: kubectl get nodes | grep <nodeName>\n"
            + "4. Check resource usage: kubectl top node <nodeName>\n\n"
            + "── Resolution ──\n"
            + "• DaemonSet pod crash: tool 'agent_restart' agent_id=<id>\n"
            + "• Or: kubectl rollout restart daemonset/wazuh-agent -n ops\n"
            + "• If recurring: kubectl edit daemonset wazuh-agent -n ops → increase resource limits\n"
            + "• Node failure: cordon node → kubectl drain → investigate hardware\n\n"
            + "── Impact ──\n"
            + "Offline agent = security blind spot on that node.\n"
            + "All events from that node are NOT monitored until agent reconnects.\n");

        RUNBOOKS.put("RB-05",
            "═══ RUNBOOK RB-05: False Positive Alert Tuning ═══\n"
            + "Trigger   : Rule fires consistently but is NOT a real threat\n"
            + "Example   : Deployment pipeline triggers 'suspicious file creation' every 2 hours\n\n"
            + "── Resolution Options ──\n"
            + "Option A — Create Exception:\n"
            + "  tool 'false_positive_exception' action=create, rule_id=<id>, reason=<explanation>\n"
            + "  Add rule_id to rule's exceptions field in ConfigMap\n"
            + "  Document in Wiki.js: why false positive, when exception expires (re-evaluate monthly)\n\n"
            + "Option B — Tune Rule Threshold:\n"
            + "  Example: change from 5 failed logins/min to 10/min\n"
            + "  Update ConfigMap wazuh-rules → modify frequency/timeframe\n"
            + "  Test in staging before production deploy\n\n"
            + "── Alert Fatigue Warning ──\n"
            + "High false-positive rate → security team ignores alerts → real threats missed.\n"
            + "Target: < 0.1% false positive rate. Review monthly.\n");
    }

    public IncidentRunbookTool(SecurityManager security, WazuhApiClient api) { super(security, api); }

    @Override public String getName()        { return "incident_runbook"; }
    @Override public String getDescription() {
        return "Retrieve EcoSkiller Wazuh incident response runbooks. "
             + "Available: RB-01 (Suspicious Process/CRITICAL), RB-02 (Unauthorized PII Access/CRITICAL), "
             + "RB-03 (Brute-Force on Auth Service/HIGH), RB-04 (Agent Offline/MEDIUM), "
             + "RB-05 (False Positive Tuning). "
             + "Returns step-by-step investigation, automated response actions, "
             + "MITRE ATT&CK mapping, and DPDPA compliance requirements.";
    }

    @Override public ObjectNode getInputSchema() {
        ObjectNode s = schema();
        addStr(s, "runbook_id",
            "Runbook ID: RB-01 | RB-02 | RB-03 | RB-04 | RB-05. "
            + "Leave blank to list all available runbooks.", false);
        return s;
    }

    @Override public ToolResult execute(JsonNode args) {
        try {
            String runbookId = security.sanitiseFreeText(getStr(args, "runbook_id")).toUpperCase();

            if (runbookId.isBlank()) {
                StringBuilder list = new StringBuilder("📚 EcoSkiller Wazuh Incident Runbooks\n\n");
                list.append("ID    | Severity | Trigger\n");
                list.append("──────┼──────────┼──────────────────────────────────────────\n");
                list.append("RB-01 | CRITICAL  | Suspicious process / kubectl exec as root\n");
                list.append("RB-02 | CRITICAL  | Unauthorized candidate PII data access\n");
                list.append("RB-03 | HIGH      | Brute-force attack on auth-service\n");
                list.append("RB-04 | MEDIUM    | Wazuh agent offline > 10 minutes\n");
                list.append("RB-05 | INFO      | False positive alert tuning\n\n");
                list.append("Use runbook_id parameter to retrieve full runbook.\n");
                return ToolResult.ok(list.toString());
            }

            if (!RUNBOOKS.containsKey(runbookId))
                return ToolResult.error("Unknown runbook '" + runbookId + "'. Use: RB-01 | RB-02 | RB-03 | RB-04 | RB-05");

            return ToolResult.ok(RUNBOOKS.get(runbookId));

        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
