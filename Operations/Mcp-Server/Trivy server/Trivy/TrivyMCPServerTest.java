package com.ecoskiller.trivy.mcp;

import com.ecoskiller.trivy.mcp.security.RateLimiter;
import com.ecoskiller.trivy.mcp.tools.ToolRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Trivy MCP Server — Full Test Suite (JUnit 5)
 *
 * 30 tests covering:
 *  - ToolRegistry completeness (14 tools)
 *  - Schema validity for all 14 tools
 *  - Happy-path execution for all 14 tools
 *  - Input validation (required field enforcement)
 *  - Security: RateLimiter per-client isolation
 *  - RBAC: role-based access control per tool
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TrivyMCPServerTest {

    private static final ObjectMapper M = new ObjectMapper();

    // ── Registry ──────────────────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("ToolRegistry must expose exactly 14 tools")
    void toolCount() {
        assertEquals(14, ToolRegistry.all().size());
    }

    @Test @Order(2)
    @DisplayName("All tool names are unique and snake_case")
    void toolNamesUniqueAndSnakeCase() {
        List<String> names = ToolRegistry.all().stream().map(MCPTool::getName).collect(Collectors.toList());
        assertEquals(names.size(), names.stream().distinct().count(), "Duplicate tool names");
        names.forEach(n -> assertTrue(n.matches("[a-z][a-z0-9_]+"), "Not snake_case: " + n));
    }

    // ── Schema validity ───────────────────────────────────────────────────────

    @ParameterizedTest(name = "Schema valid — {0}")
    @Order(3)
    @ValueSource(strings = {
        "scan_image", "scan_registry", "scan_filesystem", "scan_iac",
        "scan_k8s_manifests", "generate_sbom", "policy_check",
        "trivy_db_update", "exception_management", "export_sarif",
        "harbor_tag_metadata", "ci_pipeline_gate", "compliance_report", "scan_history"
    })
    void schemaValid(String name) {
        MCPTool tool = getTool(name);
        ObjectNode schema = tool.getInputSchema(M);
        assertNotNull(schema);
        assertEquals("object", schema.get("type").asText(), "Schema type must be 'object'");
        assertTrue(schema.has("properties"), "Schema missing 'properties'");

        ObjectNode full = tool.getSchema(M);
        assertFalse(full.get("name").asText().isBlank());
        assertFalse(full.get("description").asText().isBlank());
    }

    // ── Tool 1: scan_image ────────────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("scan_image — returns summary with all severity levels")
    void scanImage() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("image", "ecoskiller/auth-service:latest");
        args.put("environment", "prod");

        JsonNode r = getTool("scan_image").execute(args, M);
        assertTrue(r.has("summary"));
        assertTrue(r.get("summary").has("CRITICAL"));
        assertTrue(r.get("summary").has("HIGH"));
        assertTrue(r.has("vulnerabilities"));
        assertTrue(r.get("vulnerabilities").isArray());
        assertTrue(r.has("policy_result"));
        assertTrue(r.has("scan_id"));
    }

    @Test @Order(11)
    @DisplayName("scan_image — validation rejects missing image field")
    void scanImageMissingImage() {
        Optional<String> err = getTool("scan_image").validateArgs(M.createObjectNode());
        assertTrue(err.isPresent());
        assertTrue(err.get().contains("image"));
    }

    // ── Tool 2: scan_registry ─────────────────────────────────────────────────

    @Test @Order(12)
    @DisplayName("scan_registry — returns Harbor registry scan result")
    void scanRegistry() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("image_path", "ecoskiller/billing-service:v1.2.3");

        JsonNode r = getTool("scan_registry").execute(args, M);
        assertTrue(r.has("full_reference"));
        assertTrue(r.get("full_reference").asText().contains("harbor.ecoskiller.internal"));
        assertTrue(r.has("summary"));
        assertTrue(r.has("harbor_labels_updated"));
    }

    // ── Tool 3: scan_filesystem ───────────────────────────────────────────────

    @Test @Order(13)
    @DisplayName("scan_filesystem — returns detected_ecosystems and secret_scan")
    void scanFilesystem() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("path", "/workspace/ecoskiller-api");

        JsonNode r = getTool("scan_filesystem").execute(args, M);
        assertTrue(r.has("detected_ecosystems"));
        assertTrue(r.has("secret_scan"));
        assertTrue(r.get("secret_scan").has("secrets_found"));
        assertTrue(r.has("top_vulnerabilities"));
    }

    // ── Tool 4: scan_iac ──────────────────────────────────────────────────────

    @Test @Order(14)
    @DisplayName("scan_iac — returns IaC findings with locations")
    void scanIaC() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("path", "infrastructure/terraform");
        args.put("cloud_provider", "gcp");

        JsonNode r = getTool("scan_iac").execute(args, M);
        assertTrue(r.has("findings"));
        assertTrue(r.get("findings").isArray());
        assertTrue(r.get("findings").size() > 0);
        // Each finding must have check_id, severity, remediation
        JsonNode f1 = r.get("findings").get(0);
        assertTrue(f1.has("check_id"));
        assertTrue(f1.has("severity"));
        assertTrue(f1.has("remediation"));
        assertTrue(r.has("policy_result"));
    }

    // ── Tool 5: scan_k8s_manifests ────────────────────────────────────────────

    @Test @Order(15)
    @DisplayName("scan_k8s_manifests — returns K8s CIS findings with resource names")
    void scanK8s() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("path", "helm/charts");
        args.put("namespace", "ecoskiller");

        JsonNode r = getTool("scan_k8s_manifests").execute(args, M);
        assertTrue(r.has("findings"));
        assertTrue(r.get("findings").isArray());
        assertTrue(r.get("findings").size() > 0);
        assertTrue(r.has("cis_k8s_score"));
        // Each finding has resource field
        r.get("findings").forEach(f -> assertTrue(f.has("resource"), "Finding missing resource field"));
    }

    // ── Tool 6: generate_sbom ─────────────────────────────────────────────────

    @Test @Order(16)
    @DisplayName("generate_sbom — returns NTIA-compliant SBOM with MinIO path")
    void generateSbom() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("target",        "ecoskiller/auth-service:latest");
        args.put("service_name",  "auth-service");
        args.put("format",        "cyclonedx-json");
        args.put("environment",   "prod");
        args.put("version",       "v2.1.0");
        args.put("archive_minio", true);

        JsonNode r = getTool("generate_sbom").execute(args, M);
        assertTrue(r.has("component_stats"));
        assertTrue(r.get("component_stats").get("total_components").asInt() > 0);
        assertTrue(r.has("license_summary"));
        assertTrue(r.has("minio_path"));
        assertTrue(r.get("minio_path").asText().contains("auth-service"));
        assertTrue(r.get("ntia_compliant").asBoolean());
    }

    @Test @Order(17)
    @DisplayName("generate_sbom — no MinIO path when archive_minio=false")
    void generateSbomNoArchive() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("target",       "my-image:latest");
        args.put("service_name", "test-service");
        args.put("archive_minio",false);

        JsonNode r = getTool("generate_sbom").execute(args, M);
        assertFalse(r.has("minio_path"), "Should not have minio_path when archive_minio=false");
    }

    // ── Tool 7: policy_check ──────────────────────────────────────────────────

    @Test @Order(18)
    @DisplayName("policy_check — prod blocks on MEDIUM vulnerabilities")
    void policyCheckProdBlocksMedium() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("environment",   "prod");
        args.put("critical_count", 0);
        args.put("high_count",     0);
        args.put("medium_count",   3);

        JsonNode r = getTool("policy_check").execute(args, M);
        assertEquals("BLOCKED", r.get("decision").asText());
        assertEquals(1, r.get("exit_code").asInt());
    }

    @Test @Order(19)
    @DisplayName("policy_check — dev passes with CRITICAL vulnerabilities")
    void policyCheckDevPassesCritical() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("environment",    "dev");
        args.put("critical_count", 5);
        args.put("high_count",     3);
        args.put("medium_count",   8);

        JsonNode r = getTool("policy_check").execute(args, M);
        assertEquals("PASSED",  r.get("decision").asText());
        assertEquals(0, r.get("exit_code").asInt());
    }

    @Test @Order(20)
    @DisplayName("policy_check — stage blocks on HIGH but not MEDIUM")
    void policyCheckStageBlocksHigh() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("environment",    "stage");
        args.put("critical_count", 0);
        args.put("high_count",     2);
        args.put("medium_count",   5);

        JsonNode r = getTool("policy_check").execute(args, M);
        assertEquals("BLOCKED", r.get("decision").asText());
        assertTrue(r.has("blocking_reasons"));
    }

    // ── Tool 8: trivy_db_update ───────────────────────────────────────────────

    @Test @Order(21)
    @DisplayName("trivy_db_update — status returns DB version and CVE count")
    void trivyDbStatus() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("action", "status");

        JsonNode r = getTool("trivy_db_update").execute(args, M);
        assertTrue(r.has("db_version"));
        assertTrue(r.get("cve_count").asLong() > 0);
        assertTrue(r.get("offline_capable").asBoolean());
    }

    @Test @Order(22)
    @DisplayName("trivy_db_update — validation rejects invalid action")
    void trivyDbInvalidAction() {
        ObjectNode args = M.createObjectNode();
        args.put("action", "DELETE_ALL");
        assertTrue(getTool("trivy_db_update").validateArgs(args).isPresent());
    }

    // ── Tool 9: exception_management ─────────────────────────────────────────

    @Test @Order(23)
    @DisplayName("exception_management — add creates .trivyignore entry")
    void exceptionAdd() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("action",        "add");
        args.put("cve_id",        "CVE-2023-44487");
        args.put("justification", "HTTP/2 disabled in nginx — no exposure path");
        args.put("owner",         "security@ecoskiller.com");
        args.put("deadline",      "2026-06-30");

        JsonNode r = getTool("exception_management").execute(args, M);
        assertTrue(r.has("exception_id"));
        assertTrue(r.has("trivyignore_entry"));
        assertTrue(r.get("trivyignore_entry").asText().contains("CVE-2023-44487"));
        assertTrue(r.get("audit_logged").asBoolean());
    }

    @Test @Order(24)
    @DisplayName("exception_management — add rejects missing justification")
    void exceptionMissingJustification() {
        ObjectNode args = M.createObjectNode();
        args.put("action",   "add");
        args.put("cve_id",   "CVE-2023-44487");
        args.put("owner",    "dev@ecoskiller.com");
        args.put("deadline", "2026-06-30");
        // justification intentionally missing
        assertTrue(getTool("exception_management").validateArgs(args).isPresent());
    }

    // ── Tool 10: export_sarif ─────────────────────────────────────────────────

    @Test @Order(25)
    @DisplayName("export_sarif — returns SARIF 2.1.0 with GitLab compatibility flag")
    void exportSarif() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("output_path", "gl-container-scanning-report.sarif");
        args.put("scan_id",     "scan-abc12345");
        args.put("include_low", false);

        JsonNode r = getTool("export_sarif").execute(args, M);
        assertEquals("2.1.0",  r.get("sarif_version").asText());
        assertTrue(r.get("gitlab_sast_compatible").asBoolean());
        assertTrue(r.get("vscode_compatible").asBoolean());
        assertTrue(r.has("tool"));
    }

    // ── Tool 11: harbor_tag_metadata ──────────────────────────────────────────

    @Test @Order(26)
    @DisplayName("harbor_tag_metadata — writes all expected Harbor labels")
    void harborTagMetadata() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("image_path",     "ecoskiller/auth-service:v2.1.0");
        args.put("policy_result",  "PASSED");
        args.put("critical_count", 0);
        args.put("high_count",     2);
        args.put("scan_id",        "scan-abc12345");

        JsonNode r = getTool("harbor_tag_metadata").execute(args, M);
        assertTrue(r.get("labels_written").asBoolean());
        assertTrue(r.has("harbor_labels"));
        JsonNode labels = r.get("harbor_labels");
        assertTrue(labels.has("trivy-scan-date"));
        assertTrue(labels.has("trivy-critical-count"));
        assertTrue(labels.has("trivy-policy-result"));
        assertEquals("PASSED", labels.get("trivy-policy-result").asText());
    }

    // ── Tool 12: ci_pipeline_gate ─────────────────────────────────────────────

    @Test @Order(27)
    @DisplayName("ci_pipeline_gate — blocks when any scan is BLOCKED")
    void ciPipelineGateBlocks() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("pipeline_id",      "ci-12345");
        args.put("service_name",     "auth-service");
        args.put("environment",      "prod");
        args.put("image_scan_result","BLOCKED");
        args.put("iac_scan_result",  "PASSED");
        args.put("k8s_scan_result",  "PASSED");

        JsonNode r = getTool("ci_pipeline_gate").execute(args, M);
        assertEquals("BLOCKED", r.get("gate_decision").asText());
        assertEquals(1, r.get("exit_code").asInt());
        assertFalse(r.get("harbor_push_allowed").asBoolean());
        assertTrue(r.has("blocking_checks"));
    }

    @Test @Order(28)
    @DisplayName("ci_pipeline_gate — passes when all scans pass")
    void ciPipelineGatePasses() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("pipeline_id",      "ci-99999");
        args.put("service_name",     "gd-orchestrator");
        args.put("environment",      "prod");
        args.put("image_scan_result","PASSED");
        args.put("iac_scan_result",  "PASSED");
        args.put("k8s_scan_result",  "WARNING");

        JsonNode r = getTool("ci_pipeline_gate").execute(args, M);
        assertEquals("PASSED", r.get("gate_decision").asText());
        assertEquals(0, r.get("exit_code").asInt());
        assertTrue(r.get("harbor_push_allowed").asBoolean());
        assertTrue(r.get("harbor_tag").asText().contains("gd-orchestrator"));
    }

    // ── Tool 13: compliance_report ────────────────────────────────────────────

    @Test @Order(29)
    @DisplayName("compliance_report — full report has all 4 framework sections")
    void complianceReport() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("framework",    "all");
        args.put("period_start", "2026-01-01");
        args.put("period_end",   "2026-03-31");
        args.put("environment",  "prod");

        JsonNode r = getTool("compliance_report").execute(args, M);
        assertTrue(r.has("nist_csf"),        "Missing nist_csf section");
        assertTrue(r.has("owasp_top10"),     "Missing owasp_top10 section");
        assertTrue(r.has("dpdpa_2023"),      "Missing dpdpa_2023 section");
        assertTrue(r.has("pci_dss"),         "Missing pci_dss section");
        assertTrue(r.has("report_id"));
        assertTrue(r.get("clickhouse_archived").asBoolean());
    }

    // ── Tool 14: scan_history ─────────────────────────────────────────────────

    @Test @Order(30)
    @DisplayName("scan_history — trend returns 4 weeks of data")
    void scanHistoryTrend() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("query_type", "trend");
        args.put("environment","prod");
        args.put("days",       30);

        JsonNode r = getTool("scan_history").execute(args, M);
        assertTrue(r.has("weekly_trend"));
        assertEquals(4, r.get("weekly_trend").size(), "Expected 4 weeks of trend data");
        assertTrue(r.has("trend_direction"));
    }

    @Test @Order(31)
    @DisplayName("scan_history — mttd returns MTTD metrics")
    void scanHistoryMttd() throws Exception {
        ObjectNode args = M.createObjectNode();
        args.put("query_type", "mttd");

        JsonNode r = getTool("scan_history").execute(args, M);
        assertTrue(r.has("mean_time_to_detect_min"));
        assertTrue(r.get("mean_time_to_detect_min").asDouble() > 0);
        assertTrue(r.has("sla_critical_24h_compliance"));
    }

    // ── Security: RateLimiter ─────────────────────────────────────────────────

    @Test @Order(40)
    @DisplayName("RateLimiter — allows up to max requests in window")
    void rateLimiterAllows() {
        RateLimiter rl = new RateLimiter(5, 60_000);
        String client = "test-allow";
        for (int i = 0; i < 5; i++) assertTrue(rl.allow(client), "Request " + (i+1) + " should be allowed");
    }

    @Test @Order(41)
    @DisplayName("RateLimiter — blocks request exceeding max")
    void rateLimiterBlocks() {
        RateLimiter rl = new RateLimiter(3, 60_000);
        String client = "test-block";
        rl.allow(client); rl.allow(client); rl.allow(client);
        assertFalse(rl.allow(client), "4th request must be blocked");
    }

    @Test @Order(42)
    @DisplayName("RateLimiter — independent buckets per client")
    void rateLimiterIsolation() {
        RateLimiter rl = new RateLimiter(2, 60_000);
        rl.allow("client-A"); rl.allow("client-A");
        assertFalse(rl.allow("client-A"), "Client A exhausted");
        assertTrue(rl.allow("client-B"),  "Client B should be independent");
    }

    // ── Security: RBAC ────────────────────────────────────────────────────────

    @Test @Order(43)
    @DisplayName("RBAC — scan_image accessible to all roles")
    void rbacScanImageAllRoles() {
        MCPTool tool = getTool("scan_image");
        for (String role : List.of("admin","security_engineer","devops_engineer","developer","viewer"))
            assertTrue(tool.isAllowedRole(role), role + " should access scan_image");
    }

    @Test @Order(44)
    @DisplayName("RBAC — exception_management restricted to security_engineer and admin")
    void rbacExceptionManagement() {
        MCPTool tool = getTool("exception_management");
        assertTrue(tool.isAllowedRole("admin"),             "admin must have access");
        assertTrue(tool.isAllowedRole("security_engineer"), "security_engineer must have access");
        assertFalse(tool.isAllowedRole("devops_engineer"),  "devops_engineer must NOT have access");
        assertFalse(tool.isAllowedRole("developer"),        "developer must NOT have access");
        assertFalse(tool.isAllowedRole("viewer"),           "viewer must NOT have access");
    }

    @Test @Order(45)
    @DisplayName("RBAC — compliance_report restricted to security_engineer and admin")
    void rbacComplianceReport() {
        MCPTool tool = getTool("compliance_report");
        assertTrue(tool.isAllowedRole("admin"));
        assertTrue(tool.isAllowedRole("security_engineer"));
        assertFalse(tool.isAllowedRole("developer"));
        assertFalse(tool.isAllowedRole("viewer"));
    }

    @Test @Order(46)
    @DisplayName("RBAC — scan_registry restricted to sec_ops roles")
    void rbacScanRegistry() {
        MCPTool tool = getTool("scan_registry");
        assertTrue(tool.isAllowedRole("admin"));
        assertTrue(tool.isAllowedRole("security_engineer"));
        assertTrue(tool.isAllowedRole("devops_engineer"));
        assertFalse(tool.isAllowedRole("developer"), "developer should not access registry scan");
        assertFalse(tool.isAllowedRole("viewer"),    "viewer should not access registry scan");
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private MCPTool getTool(String name) {
        return ToolRegistry.all().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Tool not found: " + name));
    }

    private java.util.Optional<String> validateArgs(String toolName, ObjectNode args) {
        return getTool(toolName).validateArgs(args);
    }
}
