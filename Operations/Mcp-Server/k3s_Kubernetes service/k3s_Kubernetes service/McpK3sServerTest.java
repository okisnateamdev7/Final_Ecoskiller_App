package com.ecoskiller.mcp.k3s;

import com.ecoskiller.mcp.k3s.agents.*;
import com.ecoskiller.mcp.k3s.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for all 20 k3s MCP agents and security layer.
 */
@DisplayName("Ecoskiller k3s MCP Server Tests")
class McpK3sServerTest {

    private final ObjectMapper        mapper    = new ObjectMapper();
    private final SecurityValidator   security  = new SecurityValidator();
    private final Map<String, K3sAgent> agents  = new LinkedHashMap<>();

    @BeforeEach
    void setup() {
        agents.put("k3s_cluster_status",        new ClusterStatusAgent());
        agents.put("k3s_node_management",        new NodeManagementAgent());
        agents.put("k3s_pod_lifecycle",          new PodLifecycleAgent());
        agents.put("k3s_deployment_manager",     new DeploymentManagerAgent());
        agents.put("k3s_service_discovery",      new ServiceDiscoveryAgent());
        agents.put("k3s_ingress_controller",     new IngressControllerAgent());
        agents.put("k3s_horizontal_autoscaler",  new HorizontalAutoscalerAgent());
        agents.put("k3s_persistent_volume",      new PersistentVolumeAgent());
        agents.put("k3s_config_secrets",         new ConfigSecretsAgent());
        agents.put("k3s_rbac_policy",            new RbacPolicyAgent());
        agents.put("k3s_network_policy",         new NetworkPolicyAgent());
        agents.put("k3s_rolling_update",         new RollingUpdateAgent());
        agents.put("k3s_etcd_backup",            new EtcdBackupAgent());
        agents.put("k3s_observability",          new ObservabilityAgent());
        agents.put("k3s_gitops_argocd",          new GitopsArgoCDAgent());
        agents.put("k3s_multicloud_failover",    new MulticloudFailoverAgent());
        agents.put("k3s_security_scanner",       new SecurityScannerAgent());
        agents.put("k3s_disaster_recovery",      new DisasterRecoveryAgent());
        agents.put("k3s_compliance_audit",       new ComplianceAuditAgent());
        agents.put("k3s_cluster_upgrade",        new ClusterUpgradeAgent());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Agent Count
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Should have exactly 20 registered agents")
    void testAgentCount() {
        assertEquals(20, agents.size(), "Expected exactly 20 k3s agents");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Agent Contract
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Every agent should have non-empty description")
    void testAgentDescriptions() {
        agents.forEach((name, agent) -> {
            assertNotNull(agent.getDescription(), name + ": description is null");
            assertFalse(agent.getDescription().isBlank(), name + ": description is blank");
        });
    }

    @Test
    @DisplayName("Every agent should have a parameter map")
    void testAgentParameters() {
        agents.forEach((name, agent) -> {
            assertNotNull(agent.getParameters(), name + ": parameters is null");
        });
    }

    @Test
    @DisplayName("Every agent execute() should return non-null output")
    void testAgentExecution() {
        Map<String, String> minArgs = Map.of(
            "cluster", "gcp", "action", "list", "namespace", "default"
        );
        agents.forEach((name, agent) -> {
            String result = agent.execute(minArgs);
            assertNotNull(result,   name + ": execute returned null");
            assertFalse(result.isBlank(), name + ": execute returned blank");
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Specific Agent Tests
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("ClusterStatusAgent should include both GCP and AWS info")
    void testClusterStatus() {
        String result = agents.get("k3s_cluster_status").execute(Map.of("cluster", "all"));
        assertTrue(result.contains("GCP"),  "Should include GCP cluster info");
        assertTrue(result.contains("AWS"),  "Should include AWS cluster info");
        assertTrue(result.contains("HEALTHY"), "Should include health status");
    }

    @Test
    @DisplayName("RollingUpdateAgent should show zero downtime")
    void testRollingUpdate() {
        String result = agents.get("k3s_rolling_update").execute(
            Map.of("deployment", "scoring-engine", "action", "status",
                   "image", "harbor.ecoskiller.io/scoring-engine:v2.3.1")
        );
        assertTrue(result.contains("0s"), "Should show zero downtime");
        assertTrue(result.contains("RollingUpdate"), "Should mention rolling strategy");
    }

    @Test
    @DisplayName("DisasterRecoveryAgent should require CONFIRM for destructive actions")
    void testDisasterRecoveryConfirmGuard() {
        String result = agents.get("k3s_disaster_recovery").execute(
            Map.of("action", "restore-cluster", "cluster", "gcp")
        );
        assertTrue(result.contains("CONFIRMATION"), "Should require CONFIRM for restore");
        assertFalse(result.contains("Step 1"), "Should NOT execute restore without confirm");
    }

    @Test
    @DisplayName("ClusterUpgradeAgent should require CONFIRM before upgrading masters")
    void testClusterUpgradeConfirmGuard() {
        String result = agents.get("k3s_cluster_upgrade").execute(
            Map.of("cluster", "gcp", "action", "execute-master")
        );
        assertTrue(result.contains("CONFIRMATION"), "Should require CONFIRM");
    }

    @Test
    @DisplayName("ConfigSecretsAgent should never expose secret values")
    void testSecretsNotExposed() {
        String result = agents.get("k3s_config_secrets").execute(
            Map.of("namespace", "prod", "action", "list-secrets")
        );
        assertFalse(result.toLowerCase().contains("password="), "Should not expose passwords");
        assertFalse(result.toLowerCase().contains("token=abc"), "Should not expose tokens");
        assertTrue(result.contains("NEVER exposed"), "Should state values are never exposed");
    }

    @Test
    @DisplayName("SecurityScannerAgent should confirm 0 CRITICAL CVEs")
    void testSecurityScannerNoCritical() {
        String result = agents.get("k3s_security_scanner").execute(
            Map.of("action", "trivy-scan")
        );
        assertTrue(result.contains("0 CRITICAL"), "Should report 0 critical CVEs");
        assertTrue(result.contains("pass deployment gate"), "Should confirm deployment gate passes");
    }

    @Test
    @DisplayName("MulticloudFailoverAgent should show RTO < 5 minutes")
    void testMulticloudRto() {
        String result = agents.get("k3s_multicloud_failover").execute(
            Map.of("action", "status")
        );
        assertTrue(result.contains("RTO"), "Should include RTO metric");
        assertTrue(result.contains("< 5min"), "RTO should be within 5min target");
    }

    @Test
    @DisplayName("NetworkPolicyAgent should confirm default-deny baseline")
    void testNetworkPolicyDefaultDeny() {
        String result = agents.get("k3s_network_policy").execute(
            Map.of("action", "verify-default-deny")
        );
        assertTrue(result.contains("default-deny-all"), "Should confirm default-deny policy");
        assertTrue(result.contains("eBPF"), "Should mention Cilium eBPF enforcement");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Security Validator Tests
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("SecurityValidator: should reject oversized raw input")
    void testRawInputSizeLimit() {
        String oversized = "A".repeat(200_000);
        assertThrows(SecurityException.class,
            () -> security.validateRawInput(oversized),
            "Should throw on oversized input");
    }

    @Test
    @DisplayName("SecurityValidator: should reject null-byte injection")
    void testNullByteRejection() {
        assertThrows(SecurityException.class,
            () -> security.validateRawInput("normal\0payload"),
            "Should throw on null byte");
    }

    @Test
    @DisplayName("SecurityValidator: should allow valid method names")
    void testValidMethods() {
        assertTrue(security.validateMethodName("initialize"));
        assertTrue(security.validateMethodName("tools/list"));
        assertTrue(security.validateMethodName("tools/call"));
        assertTrue(security.validateMethodName("ping"));
    }

    @Test
    @DisplayName("SecurityValidator: should block unknown method names")
    void testInvalidMethodBlocked() {
        assertFalse(security.validateMethodName("system/exec"));
        assertFalse(security.validateMethodName("../../../etc/passwd"));
        assertFalse(security.validateMethodName(""));
        assertFalse(security.validateMethodName(null));
    }

    @Test
    @DisplayName("SecurityValidator: should block shell injection in arguments")
    void testShellInjectionBlocked() {
        ObjectNode args = mapper.createObjectNode();
        args.put("namespace", "default; rm -rf /");
        Map<String, String> result = security.sanitizeArguments(args);
        assertNull(result, "Shell injection should return null (reject)");
    }

    @Test
    @DisplayName("SecurityValidator: should block path traversal in arguments")
    void testPathTraversalBlocked() {
        ObjectNode args = mapper.createObjectNode();
        args.put("cluster", "../../etc/shadow");
        Map<String, String> result = security.sanitizeArguments(args);
        assertNull(result, "Path traversal should return null (reject)");
    }

    @Test
    @DisplayName("SecurityValidator: should block YAML/script injection")
    void testYamlInjectionBlocked() {
        ObjectNode args = mapper.createObjectNode();
        args.put("manifest", "!!python/object/apply:os.system ['rm -rf /']");
        Map<String, String> result = security.sanitizeArguments(args);
        assertNull(result, "YAML injection should be blocked");
    }

    @Test
    @DisplayName("SecurityValidator: should pass clean arguments")
    void testCleanArgumentsPass() {
        ObjectNode args = mapper.createObjectNode();
        args.put("cluster",    "gcp");
        args.put("namespace",  "default");
        args.put("action",     "list");
        Map<String, String> result = security.sanitizeArguments(args);
        assertNotNull(result, "Clean args should pass validation");
        assertEquals("gcp",     result.get("cluster"));
        assertEquals("default", result.get("namespace"));
        assertEquals("list",    result.get("action"));
    }

    @Test
    @DisplayName("SecurityValidator: should enforce rate limiting")
    void testRateLimiting() {
        String tool = "k3s_cluster_status";
        // Consume all rate limit tokens
        for (int i = 0; i < 60; i++) {
            security.checkRateLimit(tool);
        }
        // 61st call should be rate-limited
        assertFalse(security.checkRateLimit(tool),
            "61st call in 1 minute should be rate-limited");
    }

    @Test
    @DisplayName("SecurityValidator: should validate k8s resource names")
    void testK8sNameValidation() {
        assertTrue(security.validateK8sName("scoring-engine"));
        assertTrue(security.validateK8sName("scoring-engine-v2"));
        assertFalse(security.validateK8sName("UPPER_CASE"));
        assertFalse(security.validateK8sName("-starts-with-dash"));
    }

    @Test
    @DisplayName("SecurityValidator: should redact secrets from log strings")
    void testSecretsRedaction() {
        String input  = "password=supersecret123 and token=abc.def.ghi";
        String result = security.redactSecrets(input);
        assertFalse(result.contains("supersecret123"), "Password value should be redacted");
        assertTrue(result.contains("***REDACTED***"), "Should show redaction marker");
    }

    @Test
    @DisplayName("SecurityValidator: should validate tool names against whitelist")
    void testToolNameWhitelist() {
        assertTrue(security.validateToolName("k3s_cluster_status"));
        assertTrue(security.validateToolName("k3s_security_scanner"));
        assertFalse(security.validateToolName("rm_everything"));
        assertFalse(security.validateToolName("../../../bin/sh"));
        assertFalse(security.validateToolName(""));
    }
}
