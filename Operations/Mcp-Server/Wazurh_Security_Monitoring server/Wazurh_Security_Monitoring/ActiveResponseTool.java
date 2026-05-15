package com.ecoskiller.mcp.wazuh.tools;

import com.ecoskiller.mcp.wazuh.model.ToolResult;
import com.ecoskiller.mcp.wazuh.security.SecurityManager;
import com.ecoskiller.mcp.wazuh.util.WazuhApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Set;

/**
 * AGENT 12 — ACTIVE_RESPONSE_AGENT
 *
 * Trigger Wazuh active-response playbooks.
 * PUT /active-response
 *
 * EcoSkiller playbooks (from specification):
 *   block_ip           — Add IP to firewall blocklist (iptables -A INPUT -s IP -j DROP)
 *   quarantine_pod     — Remove pod from LB, revoke SA token, block egress
 *   revoke_sa_token    — Revoke ServiceAccount token (unauthorized PII access)
 *   restart_pod        — Force pod restart via kubectl
 *   isolate_container  — Apply NetworkPolicy deny-all to container
 *
 * SECURITY:
 *   - confirm=true required for ALL actions
 *   - Rate-limited per action type (3s cooldown)
 *   - Audit logged with full parameters
 *   - IP block validates IPv4 before execution
 *   - Pod/container names validated (no shell injection)
 */
public class ActiveResponseTool extends BaseTool {

    private static final Set<String> ALLOWED_ACTIONS = Set.of(
        "block_ip", "quarantine_pod", "revoke_sa_token", "restart_pod", "isolate_container"
    );

    public ActiveResponseTool(SecurityManager security, WazuhApiClient api) { super(security, api); }

    @Override public String getName()        { return "active_response_trigger"; }
    @Override public String getDescription() {
        return "Trigger a Wazuh active-response playbook for incident remediation. "
             + "Actions: block_ip (firewall DROP rule), quarantine_pod (remove from LB + revoke SA token), "
             + "revoke_sa_token (unauthorized PII access response), "
             + "restart_pod (force pod restart), isolate_container (NetworkPolicy deny-all). "
             + "ALL actions require confirm=true. Rate-limited per action. "
             + "Full audit trail logged. Execution target: < 10 seconds per EcoSkiller MTTR target.";
    }

    @Override public ObjectNode getInputSchema() {
        ObjectNode s = schema();
        addStr(s, "action",
            "Playbook to execute: block_ip | quarantine_pod | revoke_sa_token | restart_pod | isolate_container.", true);
        addStr(s, "agent_id",
            "Target Wazuh agent ID where action is executed.", true);
        addStr(s, "target_ip",
            "IP address to block (required for action=block_ip).", false);
        addStr(s, "target_pod",
            "Pod name to quarantine/restart/isolate (required for pod actions).", false);
        addStr(s, "target_namespace",
            "Kubernetes namespace of the target pod (required for pod actions).", false);
        addStr(s, "reason",
            "Reason for triggering active response (for audit log). Free text.", true);
        addBool(s, "confirm",
            "Must be true to execute. Safety gate — prevents accidental automation.");
        ((com.fasterxml.jackson.databind.node.ArrayNode) s.get("required")).add("confirm");
        return s;
    }

    @Override public ToolResult execute(JsonNode args) {
        try {
            String action    = security.sanitiseFreeText(getStr(args, "action")).toLowerCase();
            String agentId   = security.validateId(getStr(args, "agent_id"));
            String reason    = security.sanitiseFreeText(getStr(args, "reason"));
            boolean confirm  = getBool(args, "confirm", false);

            if (!confirm)
                return ToolResult.error("Active response requires confirm=true. Action '" + action + "' NOT executed.");

            if (!ALLOWED_ACTIONS.contains(action))
                return ToolResult.error("Unknown action '" + action + "'. Allowed: " + ALLOWED_ACTIONS);

            if (reason.isBlank())
                return ToolResult.error("reason is required for audit purposes.");

            // Rate-limit each action independently
            security.checkRateLimit("active_response:" + action);

            // Build the active-response payload and execute
            return switch (action) {
                case "block_ip"          -> blockIp(args, agentId, reason);
                case "quarantine_pod"    -> quarantinePod(args, agentId, reason);
                case "revoke_sa_token"   -> revokeSaToken(args, agentId, reason);
                case "restart_pod"       -> restartPod(args, agentId, reason);
                case "isolate_container" -> isolateContainer(args, agentId, reason);
                default -> ToolResult.error("Unknown action: " + action);
            };

        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }

    private ToolResult blockIp(JsonNode args, String agentId, String reason) {
        String ip = security.validateIpAddress(getStr(args, "target_ip"));
        String payload = String.format(
            "{\"command\":\"firewall-drop\",\"arguments\":[\"add\",\"%s\"],\"alert\":{\"data\":{\"srcip\":\"%s\"}}}",
            ip, ip);
        WazuhApiClient.ApiResponse resp = api.put("/active-response", payload);
        if (resp.success) {
            return ToolResult.ok("🚫 IP BLOCKED: " + ip + "\n"
                + "Rule: iptables -A INPUT -s " + ip + " -j DROP\n"
                + "Agent: " + agentId + " | Reason: " + reason + "\n"
                + "Runbook: Brute-Force Attack Response\n"
                + "Next: Verify if legitimate IP → whitelist if misconfigured client\n\n"
                + resp.body);
        }
        return ToolResult.error("block_ip failed (HTTP " + resp.statusCode + "):\n" + resp.body);
    }

    private ToolResult quarantinePod(JsonNode args, String agentId, String reason) {
        String pod = security.validateId(getStr(args, "target_pod"));
        String ns  = security.validateNamespace(getStr(args, "target_namespace"));
        String payload = String.format(
            "{\"command\":\"quarantine-pod\",\"arguments\":[\"%s\",\"%s\"]}",
            pod, ns);
        WazuhApiClient.ApiResponse resp = api.put("/active-response", payload);
        if (resp.success) {
            return ToolResult.ok("🔒 POD QUARANTINED: " + pod + " (namespace: " + ns + ")\n"
                + "Actions: removed from LB, SA token revoked, egress blocked\n"
                + "Agent: " + agentId + " | Reason: " + reason + "\n"
                + "Runbook: Suspicious Process in Production Pod\n"
                + "Next: kubectl logs -n " + ns + " " + pod + " → forensic analysis → clean image redeploy\n\n"
                + resp.body);
        }
        return ToolResult.error("quarantine_pod failed (HTTP " + resp.statusCode + "):\n" + resp.body);
    }

    private ToolResult revokeSaToken(JsonNode args, String agentId, String reason) {
        String pod = security.validateId(getStr(args, "target_pod"));
        String ns  = security.validateNamespace(getStr(args, "target_namespace"));
        String payload = String.format(
            "{\"command\":\"revoke-sa-token\",\"arguments\":[\"%s\",\"%s\"]}", pod, ns);
        WazuhApiClient.ApiResponse resp = api.put("/active-response", payload);
        if (resp.success) {
            return ToolResult.ok("🔑 SA TOKEN REVOKED: " + pod + " (namespace: " + ns + ")\n"
                + "Agent: " + agentId + " | Reason: " + reason + "\n"
                + "Runbook: Unauthorized Candidate Data Access\n"
                + "DPDPA: If PII breach confirmed → notify PDPB within 72 hours\n\n"
                + resp.body);
        }
        return ToolResult.error("revoke_sa_token failed (HTTP " + resp.statusCode + "):\n" + resp.body);
    }

    private ToolResult restartPod(JsonNode args, String agentId, String reason) {
        String pod = security.validateId(getStr(args, "target_pod"));
        String ns  = security.validateNamespace(getStr(args, "target_namespace"));
        String payload = String.format(
            "{\"command\":\"restart-pod\",\"arguments\":[\"%s\",\"%s\"]}", pod, ns);
        WazuhApiClient.ApiResponse resp = api.put("/active-response", payload);
        if (resp.success) {
            return ToolResult.ok("♻️ POD RESTARTED: " + pod + " (namespace: " + ns + ")\n"
                + "Agent: " + agentId + " | Reason: " + reason + "\n\n" + resp.body);
        }
        return ToolResult.error("restart_pod failed (HTTP " + resp.statusCode + "):\n" + resp.body);
    }

    private ToolResult isolateContainer(JsonNode args, String agentId, String reason) {
        String pod = security.validateId(getStr(args, "target_pod"));
        String ns  = security.validateNamespace(getStr(args, "target_namespace"));
        String payload = String.format(
            "{\"command\":\"network-isolation\",\"arguments\":[\"%s\",\"%s\",\"deny-all\"]}", pod, ns);
        WazuhApiClient.ApiResponse resp = api.put("/active-response", payload);
        if (resp.success) {
            return ToolResult.ok("🔌 CONTAINER ISOLATED: " + pod + " (namespace: " + ns + ")\n"
                + "Action: NetworkPolicy deny-all applied\n"
                + "Agent: " + agentId + " | Reason: " + reason + "\n"
                + "Note: Pod can be accessed via kubectl exec for forensics but all network traffic blocked\n\n"
                + resp.body);
        }
        return ToolResult.error("isolate_container failed (HTTP " + resp.statusCode + "):\n" + resp.body);
    }
}
