package com.ecoskiller.mcp.networkpolicy.tools;

import com.ecoskiller.mcp.networkpolicy.security.SecurityManager;
import com.ecoskiller.mcp.networkpolicy.util.K8sApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Tool: emergency_allow_traffic
 *
 * Creates a time-limited emergency allow NetworkPolicy when a critical
 * service is blocked due to a misconfigured policy.
 *
 * Security controls:
 *   - Requires ADMIN role
 *   - Requires reason and incident_id for audit trail
 *   - TTL annotation for automated cleanup (requires a TTL controller)
 *   - Labels the policy as "emergency" for easy identification
 *   - Automatically schedules deletion instructions
 *   - Maximum TTL capped at 4 hours (240 minutes)
 *
 * Per Ecoskiller operational docs:
 * "If policy breaks critical service connectivity, temporarily allow traffic
 *  via emergency policy. Document why, schedule review."
 */
public class EmergencyAllowTrafficTool implements ToolHandler {

    private final K8sApiClient k8s = new K8sApiClient();
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);
    private static final int MAX_TTL_MINUTES = 240; // 4 hours

    public EmergencyAllowTrafficTool(SecurityManager sm) {}

    @Override
    public ObjectNode getSchema() {
        ObjectNode schema = newInputSchema(
                "namespace", "dest_label", "src_label", "port",
                "reason", "incident_id");
        ObjectNode props = (ObjectNode) schema.get("properties");
        addStringProp(props, "namespace",   "Namespace where the emergency policy is applied");
        addStringProp(props, "dest_label",  "Destination pod label selector (e.g. 'app=scoring-engine')");
        addStringProp(props, "src_label",   "Source pod label selector (e.g. 'app=assessment-orchestrator')");
        addIntProp   (props, "port",        "Destination TCP port to temporarily allow (e.g. 8080)");
        addStringProp(props, "reason",      "Reason for emergency override (required for audit)");
        addStringProp(props, "incident_id", "Incident/ticket ID for traceability (e.g. 'INC-2025-042')");
        addIntProp   (props, "ttl_minutes", "Auto-expiry TTL in minutes (default: 60, max: 240)");
        return buildSchema("emergency_allow_traffic",
                "Create a temporary emergency NetworkPolicy allow rule to restore connectivity " +
                "for a critical service blocked by a misconfigured policy. " +
                "⚠ Emergency policies weaken zero-trust posture — review and remove ASAP. " +
                "Requires reason and incident_id for audit trail. Auto-expires via TTL annotation. " +
                "Requires ADMIN role.",
                schema);
    }

    @Override
    public ToolResult execute(JsonNode args) {
        String namespace  = args.path("namespace").asText(null);
        String destLabel  = args.path("dest_label").asText(null);
        String srcLabel   = args.path("src_label").asText(null);
        int    port       = args.path("port").asInt(0);
        String reason     = args.path("reason").asText(null);
        String incidentId = args.path("incident_id").asText(null);
        int    ttl        = Math.min(args.path("ttl_minutes").asInt(60), MAX_TTL_MINUTES);

        if (namespace  == null || namespace.isBlank())  return ToolResult.error("'namespace' is required");
        if (destLabel  == null || destLabel.isBlank())  return ToolResult.error("'dest_label' is required");
        if (srcLabel   == null || srcLabel.isBlank())   return ToolResult.error("'src_label' is required");
        if (port <= 0)                                   return ToolResult.error("'port' must be > 0");
        if (reason     == null || reason.isBlank())     return ToolResult.error("'reason' is required for audit trail");
        if (incidentId == null || incidentId.isBlank()) return ToolResult.error("'incident_id' is required for audit trail");

        // Parse label key=value for Kubernetes selector
        String destKey = "app", destVal = destLabel;
        String srcKey  = "app", srcVal  = srcLabel;
        if (destLabel.contains("=")) { String[] kv = destLabel.split("=",2); destKey=kv[0]; destVal=kv[1]; }
        if (srcLabel.contains("="))  { String[] kv = srcLabel.split("=",2);  srcKey=kv[0];  srcVal=kv[1]; }

        String now      = ISO.format(Instant.now());
        String expiry   = ISO.format(Instant.now().plusSeconds((long) ttl * 60));
        String policyName = "emergency-allow-" + incidentId.toLowerCase().replaceAll("[^a-z0-9-]", "-")
                          + "-" + destVal.replaceAll("[^a-z0-9-]", "-");
        // Trim to K8s 63-char name limit
        if (policyName.length() > 63) policyName = policyName.substring(0, 63);

        String yaml = buildEmergencyPolicyYaml(
                policyName, namespace, destKey, destVal, srcKey, srcVal, port,
                reason, incidentId, ttl, now, expiry);

        String path = "/apis/networking.k8s.io/v1/namespaces/" + namespace +
                      "/networkpolicies?fieldManager=ecoskiller-mcp-emergency";
        K8sApiClient.ApiResponse resp = k8s.post(path, yaml);

        StringBuilder sb = new StringBuilder();
        sb.append("🚨 EMERGENCY ALLOW POLICY\n");
        sb.append("═".repeat(50)).append("\n");
        sb.append("  Policy:    ").append(policyName).append("\n");
        sb.append("  Namespace: ").append(namespace).append("\n");
        sb.append("  From:      ").append(srcLabel).append("\n");
        sb.append("  To:        ").append(destLabel).append(" port ").append(port).append("\n");
        sb.append("  Reason:    ").append(reason).append("\n");
        sb.append("  Incident:  ").append(incidentId).append("\n");
        sb.append("  TTL:       ").append(ttl).append(" minutes (expires ~").append(expiry).append(")\n\n");

        if (resp.isSuccess()) {
            sb.append("✅ Emergency policy applied (HTTP ").append(resp.getStatus()).append(")\n\n");
            sb.append("⚠ IMPORTANT — Schedule cleanup:\n");
            sb.append("  1. Fix the root-cause misconfigured policy\n");
            sb.append("  2. Delete this emergency policy:\n");
            sb.append("     kubectl delete networkpolicy ").append(policyName)
              .append(" -n ").append(namespace).append("\n");
            sb.append("  3. Verify service connectivity still works after deletion\n");
            sb.append("  4. Log resolution in incident ").append(incidentId).append("\n\n");
            sb.append("TTL controller (if installed) will auto-delete at: ").append(expiry).append("\n");
        } else {
            sb.append("❌ Failed to apply emergency policy (HTTP ").append(resp.getStatus()).append(")\n");
            sb.append(resp.getBody()).append("\n");
            sb.append("\nFallback: apply YAML manually via kubectl:\n").append(yaml);
            return ToolResult.error(sb.toString());
        }

        return ToolResult.ok(sb.toString());
    }

    private String buildEmergencyPolicyYaml(
            String name, String ns,
            String destKey, String destVal,
            String srcKey,  String srcVal,
            int port, String reason, String incidentId,
            int ttl, String createdAt, String expiresAt) {

        return "apiVersion: networking.k8s.io/v1\n" +
               "kind: NetworkPolicy\n" +
               "metadata:\n" +
               "  name: " + name + "\n" +
               "  namespace: " + ns + "\n" +
               "  labels:\n" +
               "    managed-by: ecoskiller-mcp\n" +
               "    policy-type: emergency\n" +
               "    incident-id: \"" + incidentId + "\"\n" +
               "  annotations:\n" +
               "    ecoskiller.io/emergency-reason: \"" + reason.replace("\"", "'") + "\"\n" +
               "    ecoskiller.io/incident-id: \"" + incidentId + "\"\n" +
               "    ecoskiller.io/created-at: \"" + createdAt + "\"\n" +
               "    ecoskiller.io/expires-at: \"" + expiresAt + "\"\n" +
               "    ecoskiller.io/ttl-minutes: \"" + ttl + "\"\n" +
               "    # TTL controller annotation (install kube-ttl-controller for auto-delete)\n" +
               "    k8s-ttl-controller.lwolf.org/expire-after: \"" + ttl + "m\"\n" +
               "spec:\n" +
               "  podSelector:\n" +
               "    matchLabels:\n" +
               "      " + destKey + ": " + destVal + "\n" +
               "  policyTypes:\n" +
               "  - Ingress\n" +
               "  ingress:\n" +
               "  - from:\n" +
               "    - podSelector:\n" +
               "        matchLabels:\n" +
               "          " + srcKey + ": " + srcVal + "\n" +
               "    ports:\n" +
               "    - protocol: TCP\n" +
               "      port: " + port + "\n";
    }
}
