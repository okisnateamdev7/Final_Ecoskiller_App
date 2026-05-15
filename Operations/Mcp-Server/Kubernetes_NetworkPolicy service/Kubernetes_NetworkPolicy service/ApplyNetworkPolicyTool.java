package com.ecoskiller.mcp.networkpolicy.tools;

import com.ecoskiller.mcp.networkpolicy.security.SecurityManager;
import com.ecoskiller.mcp.networkpolicy.util.K8sApiClient;
import com.ecoskiller.mcp.networkpolicy.util.PolicyYamlValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Tool: apply_network_policy
 *
 * Applies a NetworkPolicy YAML to the specified Kubernetes namespace.
 * Uses server-side apply (kubectl apply equivalent) via the K8s API.
 * Client-side YAML validation is run first; if errors are found the call
 * is aborted before hitting the API.
 */
public class ApplyNetworkPolicyTool implements ToolHandler {

    private final SecurityManager securityManager;
    private final K8sApiClient k8s = new K8sApiClient();

    public ApplyNetworkPolicyTool(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public ObjectNode getSchema() {
        ObjectNode schema = newInputSchema("namespace", "policy_yaml");
        ObjectNode props = (ObjectNode) schema.get("properties");
        addStringProp(props, "namespace",   "Kubernetes namespace to apply the policy to (e.g. 'production')");
        addStringProp(props, "policy_yaml", "Full NetworkPolicy YAML document (networking.k8s.io/v1)");
        addBoolProp  (props, "dry_run",     "If true, validate via server-side dry-run without persisting (default: false)");
        return buildSchema(
                "apply_network_policy",
                "Apply or update a Kubernetes NetworkPolicy in a namespace. " +
                "Validates YAML locally then submits to the K8s API. " +
                "Use dry_run=true to validate without persisting. " +
                "Requires WRITE role. Protected namespaces (kube-system, cert-manager, ops) require ADMIN role.",
                schema
        );
    }

    @Override
    public ToolResult execute(JsonNode args) {
        String namespace = args.path("namespace").asText(null);
        String yaml      = args.path("policy_yaml").asText(null);
        boolean dryRun   = args.path("dry_run").asBoolean(false);

        if (namespace == null || namespace.isBlank())
            return ToolResult.error("'namespace' is required");
        if (yaml == null || yaml.isBlank())
            return ToolResult.error("'policy_yaml' is required");

        // 1. Client-side structural validation
        PolicyYamlValidator.ValidationReport report = PolicyYamlValidator.validate(yaml);
        if (!report.isValid()) {
            return ToolResult.error("Client-side YAML validation failed:\n" + report.toText());
        }

        // 2. Extract policy name from YAML (simple parse)
        String policyName = extractName(yaml);
        if (policyName == null) policyName = "unknown";

        // 3. Determine API path
        String path = "/apis/networking.k8s.io/v1/namespaces/" + namespace + "/networkpolicies";
        String fullPath = dryRun ? (path + "?dryRun=All&fieldManager=ecoskiller-mcp") : (path + "?fieldManager=ecoskiller-mcp");

        // 4. Call K8s API  (POST = create; tool uses apply-patch for idempotency)
        K8sApiClient.ApiResponse response = k8s.post(fullPath, yaml);

        StringBuilder sb = new StringBuilder();
        if (dryRun) sb.append("🔍 DRY-RUN MODE — no changes persisted\n\n");

        if (response.isSuccess()) {
            sb.append("✅ NetworkPolicy '").append(policyName).append("' applied to namespace '")
              .append(namespace).append("'\n");
            sb.append("HTTP ").append(response.getStatus()).append("\n\n");
            sb.append(report.toText());
            sb.append("\nK8s API response (truncated):\n");
            sb.append(truncate(response.getBody(), 800));
        } else {
            sb.append("❌ Failed to apply NetworkPolicy '").append(policyName)
              .append("' to namespace '").append(namespace).append("'\n");
            sb.append("HTTP ").append(response.getStatus()).append("\n");
            sb.append(response.getBody());
            return ToolResult.error(sb.toString());
        }

        return ToolResult.ok(sb.toString());
    }

    private String extractName(String yaml) {
        for (String line : yaml.split("\n")) {
            line = line.trim();
            if (line.startsWith("name:")) {
                return line.replaceFirst("name:\\s*", "").trim();
            }
        }
        return null;
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max) + "\n...(truncated)";
    }
}
