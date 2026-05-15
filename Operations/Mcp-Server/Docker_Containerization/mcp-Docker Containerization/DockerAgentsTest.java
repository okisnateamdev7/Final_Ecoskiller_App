package io.ecoskiller.mcp.docker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all 15 Docker Containerization MCP agents.
 * Covers happy paths, security validation, and edge cases.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DockerAgentsTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private DockerAgents agents;

    @BeforeEach
    void setUp() { agents = new DockerAgents(); }

    // ── 1. Dockerfile Builder ────────────────────────────────────────────────

    @Test @Order(1)
    @DisplayName("dockerfileBuilder: valid node18 service")
    void testDockerfileBuilderValid() {
        ObjectNode args = args(
            "service_name", "scoring-engine",
            "runtime",      "node18",
            "port",         "8080",
            "entry_command","npm start"
        );
        ObjectNode result = agents.dockerfileBuilder(args);
        assertEquals("DOCKERFILE_BUILDER_AGENT", result.get("agent").asText());
        assertTrue(result.get("dockerfile").asText().contains("node:18.15-alpine"));
        assertTrue(result.get("dockerfile").asText().contains("USER appuser"), "Should have non-root user");
        assertTrue(result.get("dockerfile").asText().contains("HEALTHCHECK"), "Should have healthcheck");
        assertTrue(result.get("multi_stage").asBoolean(), "Should be multi-stage");
    }

    @Test @Order(2)
    @DisplayName("dockerfileBuilder: security — rejects injection in entry_command")
    void testDockerfileBuilderCommandInjection() {
        ObjectNode args = args(
            "service_name", "test-svc",
            "runtime",      "node18",
            "port",         "8080",
            "entry_command","npm start; rm -rf /"
        );
        assertThrows(SecurityException.class, () -> agents.dockerfileBuilder(args));
    }

    @Test @Order(3)
    @DisplayName("dockerfileBuilder: security — rejects invalid service name")
    void testDockerfileBuilderInvalidServiceName() {
        ObjectNode args = args(
            "service_name", "INVALID NAME!",
            "runtime",      "node18",
            "port",         "8080",
            "entry_command","npm start"
        );
        assertThrows(SecurityException.class, () -> agents.dockerfileBuilder(args));
    }

    // ── 2. Image Build Pipeline ──────────────────────────────────────────────

    @Test @Order(4)
    @DisplayName("imageBuildPipeline: valid build")
    void testImageBuildPipelineValid() {
        ObjectNode args = args(
            "service_name",  "scoring-engine",
            "git_commit",    "abc123def456",
            "registry_host", "harbor.ecoskiller.io"
        );
        ObjectNode result = agents.imageBuildPipeline(args);
        assertEquals("IMAGE_BUILD_PIPELINE_AGENT", result.get("agent").asText());
        assertTrue(result.get("image_ref").asText().contains("harbor.ecoskiller.io/scoring-engine"));
        assertEquals("Docker BuildKit", result.get("build_engine").asText());
    }

    @Test @Order(5)
    @DisplayName("imageBuildPipeline: security — rejects invalid git hash")
    void testImageBuildPipelineInvalidHash() {
        ObjectNode args = args(
            "service_name",  "scoring-engine",
            "git_commit",    "not-a-git-hash!!",
            "registry_host", "harbor.ecoskiller.io"
        );
        assertThrows(IllegalArgumentException.class, () -> agents.imageBuildPipeline(args));
    }

    // ── 3. Registry Push ─────────────────────────────────────────────────────

    @Test @Order(6)
    @DisplayName("registryPush: valid push with retention")
    void testRegistryPushValid() {
        ObjectNode args = args(
            "image_ref",       "harbor.ecoskiller.io/scoring-engine:abc123",
            "auth_token",      "dXNlcjpwYXNz",
            "retention_policy","10"
        );
        ObjectNode result = agents.registryPush(args);
        assertEquals("REGISTRY_PUSH_AGENT", result.get("agent").asText());
        // Security: token must be masked
        String maskedToken = result.get("auth_token_masked").asText();
        assertFalse(maskedToken.equals("dXNlcjpwYXNz"), "Token should be masked in output");
        assertTrue(maskedToken.contains("****"), "Mask should contain asterisks");
    }

    @Test @Order(7)
    @DisplayName("registryPush: security — rejects invalid retention")
    void testRegistryPushInvalidRetention() {
        ObjectNode args = args(
            "image_ref",       "harbor.ecoskiller.io/svc:latest",
            "auth_token",      "dXNlcjpwYXNz",
            "retention_policy","999"
        );
        assertThrows(IllegalArgumentException.class, () -> agents.registryPush(args));
    }

    // ── 4. Vulnerability Scanner ─────────────────────────────────────────────

    @Test @Order(8)
    @DisplayName("vulnerabilityScanner: returns scan report")
    void testVulnerabilityScanner() {
        ObjectNode args = args(
            "image_ref",          "harbor.ecoskiller.io/scoring-engine:abc123",
            "severity_threshold", "HIGH",
            "ignore_unfixed",     "false"
        );
        ObjectNode result = agents.vulnerabilityScanner(args);
        assertEquals("VULNERABILITY_SCANNER_AGENT", result.get("agent").asText());
        assertNotNull(result.get("vulnerability_summary"));
        assertNotNull(result.get("deployment_blocked"));
        assertEquals("Trivy 0.48.x", result.get("scanner").asText());
    }

    @Test @Order(9)
    @DisplayName("vulnerabilityScanner: security — rejects invalid severity")
    void testVulnerabilityScannerInvalidSeverity() {
        ObjectNode args = args(
            "image_ref",          "harbor.ecoskiller.io/svc:latest",
            "severity_threshold", "EXTREME",
            "ignore_unfixed",     "false"
        );
        assertThrows(IllegalArgumentException.class, () -> agents.vulnerabilityScanner(args));
    }

    // ── 5. SBOM Generator ────────────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("sbomGenerator: generates CycloneDX SBOM")
    void testSbomGenerator() {
        ObjectNode args = args(
            "image_ref",     "harbor.ecoskiller.io/scoring-engine:abc123",
            "output_format", "cyclonedx-json",
            "service_name",  "scoring-engine"
        );
        ObjectNode result = agents.sbomGenerator(args);
        assertEquals("SBOM_GENERATOR_AGENT", result.get("agent").asText());
        assertEquals("cyclonedx-json", result.get("format").asText());
        assertNotNull(result.get("components_sample"));
    }

    // ── 6. Image Signing ─────────────────────────────────────────────────────

    @Test @Order(11)
    @DisplayName("imageSigning: signs image with Cosign")
    void testImageSigning() {
        ObjectNode args = args(
            "image_ref", "harbor.ecoskiller.io/scoring-engine@sha256:abc123def456",
            "key_id",    "ecoskiller-prod-key"
        );
        ObjectNode result = agents.imageSigning(args);
        assertEquals("IMAGE_SIGNING_AGENT", result.get("agent").asText());
        assertTrue(result.get("signed").asBoolean());
        assertEquals("Cosign (Sigstore)", result.get("tool").asText());
    }

    @Test @Order(12)
    @DisplayName("imageSigning: security — rejects dangerous key_id")
    void testImageSigningInvalidKeyId() {
        ObjectNode args = args(
            "image_ref", "harbor.ecoskiller.io/svc:latest",
            "key_id",    "../../etc/passwd"
        );
        assertThrows(SecurityException.class, () -> agents.imageSigning(args));
    }

    // ── 7. Base Image Manager ────────────────────────────────────────────────

    @Test @Order(13)
    @DisplayName("baseImageManager: analyzes base image")
    void testBaseImageManager() {
        ObjectNode args = args(
            "current_base",    "node:18.15-alpine",
            "check_updates",   "true",
            "services_affected","scoring-engine,interview-service"
        );
        ObjectNode result = agents.baseImageManager(args);
        assertEquals("BASE_IMAGE_MANAGER_AGENT", result.get("agent").asText());
        assertTrue(result.get("is_pinned").asBoolean());
        assertNotNull(result.get("analysis"));
    }

    // ── 8. Layer Cache Optimizer ──────────────────────────────────────────────

    @Test @Order(14)
    @DisplayName("layerCacheOptimizer: detects COPY . . anti-pattern")
    void testLayerCacheOptimizerAntiPattern() {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("dockerfile_content", "FROM node:18-alpine\nCOPY . .\nRUN npm install");
        args.put("service_name",       "test-svc");
        args.put("optimization_level", "basic");

        ObjectNode result = agents.layerCacheOptimizer(args);
        assertEquals("LAYER_CACHE_OPTIMIZER_AGENT", result.get("agent").asText());
        // Should flag the COPY . . issue
        assertTrue(result.get("cache_issues").size() > 0, "Should detect cache issues");
    }

    @Test @Order(15)
    @DisplayName("layerCacheOptimizer: security — rejects oversized dockerfile")
    void testLayerCacheOptimizerOversized() {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("dockerfile_content", "x".repeat(60_000));
        args.put("service_name",       "test-svc");
        args.put("optimization_level", "basic");
        assertThrows(SecurityException.class, () -> agents.layerCacheOptimizer(args));
    }

    // ── 9. Multi-Arch Builder ─────────────────────────────────────────────────

    @Test @Order(16)
    @DisplayName("multiArchBuilder: configures amd64+arm64 build")
    void testMultiArchBuilder() {
        ObjectNode args = args(
            "service_name", "scoring-engine",
            "platforms",    "linux/amd64,linux/arm64",
            "image_ref",    "harbor.ecoskiller.io/scoring-engine:v1.2.3"
        );
        ObjectNode result = agents.multiArchBuilder(args);
        assertEquals("MULTI_ARCH_BUILDER_AGENT", result.get("agent").asText());
        assertEquals("docker buildx", result.get("tool").asText());
        assertNotNull(result.get("build_commands"));
    }

    @Test @Order(17)
    @DisplayName("multiArchBuilder: security — rejects invalid platform")
    void testMultiArchBuilderInvalidPlatform() {
        ObjectNode args = args(
            "service_name", "test-svc",
            "platforms",    "windows/amd64",
            "image_ref",    "harbor.ecoskiller.io/svc:latest"
        );
        assertThrows(IllegalArgumentException.class, () -> agents.multiArchBuilder(args));
    }

    // ── 10. Health Check Configurator ─────────────────────────────────────────

    @Test @Order(18)
    @DisplayName("healthCheckConfigurator: generates HEALTHCHECK and k8s probe")
    void testHealthCheckConfigurator() {
        ObjectNode args = args(
            "service_name",    "scoring-engine",
            "health_endpoint", "/health",
            "interval_seconds","30",
            "timeout_seconds", "5"
        );
        ObjectNode result = agents.healthCheckConfigurator(args);
        assertEquals("HEALTH_CHECK_CONFIGURATOR_AGENT", result.get("agent").asText());
        assertTrue(result.get("dockerfile_healthcheck").asText().contains("HEALTHCHECK"));
        assertNotNull(result.get("kubernetes_probe"));
    }

    @Test @Order(19)
    @DisplayName("healthCheckConfigurator: security — rejects path traversal endpoint")
    void testHealthCheckConfiguratorPathTraversal() {
        ObjectNode args = args(
            "service_name",    "test-svc",
            "health_endpoint", "/../../../etc/passwd",
            "interval_seconds","30",
            "timeout_seconds", "5"
        );
        assertThrows(SecurityException.class, () -> agents.healthCheckConfigurator(args));
    }

    // ── 11. Image Retention Policy ────────────────────────────────────────────

    @Test @Order(20)
    @DisplayName("imageRetentionPolicy: dry run")
    void testImageRetentionPolicyDryRun() {
        ObjectNode args = args(
            "service_name", "scoring-engine",
            "keep_last_n",  "10",
            "dry_run",      "true"
        );
        ObjectNode result = agents.imageRetentionPolicy(args);
        assertEquals("IMAGE_RETENTION_POLICY_AGENT", result.get("agent").asText());
        assertTrue(result.get("policy").get("images_to_delete").asText().contains("DRY RUN"));
    }

    // ── 12. Environment Injection ─────────────────────────────────────────────

    @Test @Order(21)
    @DisplayName("environmentInjection: validates env vars")
    void testEnvironmentInjection() {
        ObjectNode args = args(
            "service_name", "scoring-engine",
            "environment",  "prod",
            "vars",         "DATABASE_URL,KAFKA_BROKERS,JWT_SECRET"
        );
        ObjectNode result = agents.environmentInjection(args);
        assertEquals("ENVIRONMENT_INJECTION_AGENT", result.get("agent").asText());
        assertEquals(3, result.get("required_vars").size());
    }

    @Test @Order(22)
    @DisplayName("environmentInjection: security — rejects invalid environment")
    void testEnvironmentInjectionInvalidEnv() {
        ObjectNode args = args(
            "service_name", "test-svc",
            "environment",  "production-hacked",
            "vars",         "DB_URL"
        );
        assertThrows(IllegalArgumentException.class, () -> agents.environmentInjection(args));
    }

    // ── 13. Build Log Analyzer ────────────────────────────────────────────────

    @Test @Order(23)
    @DisplayName("buildLogAnalyzer: parses log successfully")
    void testBuildLogAnalyzer() {
        String sampleLog = "Step 1/10 : FROM node:18-alpine\n => CACHED\nStep 2/10 : RUN npm install\n => 45s";
        ObjectNode args = MAPPER.createObjectNode();
        args.put("build_log",                   sampleLog);
        args.put("service_name",                "scoring-engine");
        args.put("flag_slow_threshold_seconds", 30);

        ObjectNode result = agents.buildLogAnalyzer(args);
        assertEquals("BUILD_LOG_ANALYZER_AGENT", result.get("agent").asText());
        assertTrue(result.get("diagnosis").get("cache_hits_detected").asBoolean());
    }

    @Test @Order(24)
    @DisplayName("buildLogAnalyzer: security — rejects oversized log")
    void testBuildLogAnalyzerOversized() {
        ObjectNode args = MAPPER.createObjectNode();
        args.put("build_log",    "x".repeat(600_000));
        args.put("service_name", "test-svc");
        assertThrows(SecurityException.class, () -> agents.buildLogAnalyzer(args));
    }

    // ── 14. Container Audit Trail ─────────────────────────────────────────────

    @Test @Order(25)
    @DisplayName("containerAuditTrail: record then query")
    void testContainerAuditTrailRecordQuery() {
        ObjectNode recordArgs = args(
            "action",       "record",
            "service_name", "scoring-engine",
            "image_digest", "sha256:" + "a".repeat(64),
            "git_commit",   "abc123"
        );
        ObjectNode recorded = agents.containerAuditTrail(recordArgs);
        assertTrue(recorded.get("recorded").asBoolean());

        ObjectNode queryArgs = args(
            "action",       "query",
            "service_name", "scoring-engine",
            "image_digest", "",
            "git_commit",   ""
        );
        ObjectNode queried = agents.containerAuditTrail(queryArgs);
        assertTrue(queried.get("total_found").asInt() >= 1);
    }

    @Test @Order(26)
    @DisplayName("containerAuditTrail: security — rejects invalid digest format")
    void testContainerAuditTrailInvalidDigest() {
        ObjectNode args = args(
            "action",       "record",
            "service_name", "test-svc",
            "image_digest", "not-a-real-digest",
            "git_commit",   "abc123"
        );
        assertThrows(SecurityException.class, () -> agents.containerAuditTrail(args));
    }

    // ── 15. Multi-Cloud Distribution ──────────────────────────────────────────

    @Test @Order(27)
    @DisplayName("multiCloudDistribution: both clouds with mirror")
    void testMultiCloudDistribution() {
        ObjectNode args = args(
            "image_ref",             "harbor.ecoskiller.io/scoring-engine:v1.2.3",
            "target_clouds",         "both",
            "create_regional_mirror","true"
        );
        ObjectNode result = agents.multiCloudDistribution(args);
        assertEquals("MULTI_CLOUD_DISTRIBUTION_AGENT", result.get("agent").asText());
        assertNotNull(result.get("regional_mirrors"));
        assertNotNull(result.get("regional_mirrors").get("gcp_gcr"));
        assertNotNull(result.get("regional_mirrors").get("aws_ecr"));
    }

    @Test @Order(28)
    @DisplayName("multiCloudDistribution: security — rejects invalid target_clouds")
    void testMultiCloudDistributionInvalid() {
        ObjectNode args = args(
            "image_ref",             "harbor.ecoskiller.io/svc:latest",
            "target_clouds",         "azure",
            "create_regional_mirror","false"
        );
        assertThrows(IllegalArgumentException.class, () -> agents.multiCloudDistribution(args));
    }

    // ── Sanitize helper ───────────────────────────────────────────────────────

    @Test @Order(29)
    @DisplayName("sanitize: strips control characters")
    void testSanitize() {
        String malicious = "hello\nworld\r\n\0injection";
        String result = DockerMCPServer.sanitize(malicious);
        assertFalse(result.contains("\n"), "Should strip newlines");
        assertFalse(result.contains("\r"), "Should strip carriage returns");
        assertFalse(result.contains("\0"), "Should strip null bytes");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Build ObjectNode args from String key-value pairs. */
    private ObjectNode args(String... keyValues) {
        ObjectNode n = MAPPER.createObjectNode();
        for (int i = 0; i < keyValues.length - 1; i += 2) {
            String k = keyValues[i];
            String v = keyValues[i + 1];
            // Auto-detect booleans and integers
            if (v.equals("true") || v.equals("false")) {
                n.put(k, Boolean.parseBoolean(v));
            } else {
                try {
                    n.put(k, Integer.parseInt(v));
                } catch (NumberFormatException e) {
                    n.put(k, v);
                }
            }
        }
        return n;
    }
}
