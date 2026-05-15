package com.ecoskiller.flyway;

import com.ecoskiller.flyway.security.SecurityManager;
import com.ecoskiller.flyway.agents.*;
import com.google.gson.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Flyway Service MCP Server — Test Suite
 * 26 tests covering all 18 agents + SecurityManager
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FlywayMcpServerTest {

    private final SecurityManager security = new SecurityManager();

    // ── Security Layer Tests ──────────────────────────────────────────────────

    @Test @Order(1) @DisplayName("Security: valid RPC structure accepted")
    void testValidRpc() {
        JsonObject req = new JsonObject();
        req.addProperty("jsonrpc", "2.0");
        req.addProperty("method",  "tools/call");
        req.addProperty("id", 1);
        assertTrue(security.validateRpcStructure(req));
    }

    @Test @Order(2) @DisplayName("Security: missing method rejected")
    void testMissingMethodRejected() {
        JsonObject req = new JsonObject();
        req.addProperty("jsonrpc", "2.0");
        assertFalse(security.validateRpcStructure(req));
    }

    @Test @Order(3) @DisplayName("Security: wrong jsonrpc version rejected")
    void testWrongVersionRejected() {
        JsonObject req = new JsonObject();
        req.addProperty("jsonrpc", "1.0");
        req.addProperty("method",  "tools/call");
        assertFalse(security.validateRpcStructure(req));
    }

    @Test @Order(4) @DisplayName("Security: tool allowlist — valid tools permitted")
    void testAllowlistPermit() {
        assertTrue(security.isAllowedTool("migration_apply"));
        assertTrue(security.isAllowedTool("dr_schema_consistency"));
        assertTrue(security.isAllowedTool("compliance_audit_reporter"));
    }

    @Test @Order(5) @DisplayName("Security: tool allowlist — unknown tools denied")
    void testAllowlistDeny() {
        assertFalse(security.isAllowedTool("DROP TABLE flyway_schema_history"));
        assertFalse(security.isAllowedTool("admin_override"));
        assertFalse(security.isAllowedTool(""));
        assertFalse(security.isAllowedTool(null));
    }

    @Test @Order(6) @DisplayName("Security: SQL injection blocked")
    void testSqlInjectionBlocked() {
        JsonObject args = new JsonObject();
        args.addProperty("schema", "core; DROP TABLE users; --");
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(7) @DisplayName("Security: path traversal blocked")
    void testPathTraversalBlocked() {
        JsonObject args = new JsonObject();
        args.addProperty("migration_file", "../../etc/passwd");
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(8) @DisplayName("Security: string length limit enforced")
    void testStringLengthLimit() {
        JsonObject args = new JsonObject();
        args.addProperty("schema", "a".repeat(3000));
        assertThrows(SecurityException.class, () -> security.sanitizeInputs(args));
    }

    @Test @Order(9) @DisplayName("Security: HMAC sign and verify")
    void testHmacSignVerify() {
        String payload   = "{\"schema\":\"core\",\"action\":\"migrate\"}";
        String signature = security.signPayload(payload);
        assertNotNull(signature);
        assertTrue(security.verifySignature(payload, signature));
        assertFalse(security.verifySignature(payload + "tampered", signature));
    }

    @Test @Order(10) @DisplayName("Security: rate limiting enforced")
    void testRateLimiting() {
        for (int i = 0; i < 120; i++) {
            assertDoesNotThrow(() -> security.checkRateLimit("test_rl_tool"));
        }
        assertThrows(SecurityException.class, () -> security.checkRateLimit("test_rl_tool"));
    }

    @Test @Order(11) @DisplayName("Security: schema name validation")
    void testSchemaValidation() {
        assertTrue(security.isKnownSchema("core"));
        assertTrue(security.isKnownSchema("phone_bridge"));
        assertTrue(security.isKnownSchema("royalty"));
        assertFalse(security.isKnownSchema("unknown_schema"));
        assertFalse(security.isKnownSchema(null));
    }

    @Test @Order(12) @DisplayName("Security: migration file name validation")
    void testMigrationFileValidation() {
        assertTrue(security.isValidMigrationFile("V1__init.sql"));
        assertTrue(security.isValidMigrationFile("V25__gd_topics_bank.sql"));
        assertFalse(security.isValidMigrationFile("V1_init.sql"));     // single underscore
        assertFalse(security.isValidMigrationFile("init.sql"));        // no version prefix
        assertFalse(security.isValidMigrationFile("../evil.sql"));     // path traversal
    }

    // ── Agent Tests ───────────────────────────────────────────────────────────

    @Test @Order(13) @DisplayName("Agent: migration_apply — core schema success")
    void testMigrationApply() {
        var agent = new MigrationApplyAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema",      "core");
        args.addProperty("environment", "prod");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("run_id"));
        assertEquals(0, r.get("exit_code").getAsInt());
        assertTrue(r.get("run_id").getAsString().startsWith("MIGRATE-"));
    }

    @Test @Order(14) @DisplayName("Agent: migration_apply — ALL schemas DR mode")
    void testMigrationApplyAllSchemas() {
        var agent = new MigrationApplyAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema",    "ALL");
        args.addProperty("environment","stage");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertEquals("MULTI_SCHEMA_DR", r.get("mode").getAsString());
        assertEquals("34/45", r.get("dr_runbook_step").getAsString());
    }

    @Test @Order(15) @DisplayName("Agent: migration_apply — unknown schema rejected")
    void testMigrationApplyUnknownSchema() {
        var agent = new MigrationApplyAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema", "evil_schema");
        JsonObject r = agent.execute(args);
        assertEquals("error", r.get("status").getAsString());
    }

    @Test @Order(16) @DisplayName("Agent: migration_status — returns migration table")
    void testMigrationStatus() {
        var agent = new MigrationStatusAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema", "core");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("migrations"));
        assertTrue(r.get("applied").getAsInt() > 0);
    }

    @Test @Order(17) @DisplayName("Agent: migration_validate — checksums all valid")
    void testMigrationValidate() {
        var agent = new MigrationValidateAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema", "core");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("all_valid").getAsBoolean());
        assertEquals(0, r.get("exit_code").getAsInt());
    }

    @Test @Order(18) @DisplayName("Agent: migration_validate — invalid file name rejected")
    void testMigrationValidateInvalidFile() {
        var agent = new MigrationValidateAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema",         "core");
        args.addProperty("migration_file", "badname.sql");
        JsonObject r = agent.execute(args);
        assertEquals("error", r.get("status").getAsString());
    }

    @Test @Order(19) @DisplayName("Agent: migration_repair — records repair action")
    void testMigrationRepair() {
        var agent = new MigrationRepairAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema",         "billing");
        args.addProperty("failed_version", "V12");
        args.addProperty("repair_reason",  "Constraint violation fixed in V12 SQL");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("repair_id"));
        assertEquals(1, r.get("rows_removed").getAsInt());
    }

    @Test @Order(20) @DisplayName("Agent: migration_baseline — prod requires confirmed=true")
    void testMigrationBaselineProdGate() {
        var agent = new MigrationBaselineAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema",           "legal");
        args.addProperty("baseline_version", "V1");
        args.addProperty("environment",      "prod");
        args.addProperty("confirmed",        false);
        JsonObject r = agent.execute(args);
        assertEquals("error", r.get("status").getAsString());
        assertTrue(r.get("error").getAsString().contains("confirmed=true"));
    }

    @Test @Order(21) @DisplayName("Agent: schema_drift_detector — no drift detected")
    void testSchemaDriftDetector() {
        var agent = new SchemaDriftDetectorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema", "core");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertFalse(r.get("overall_drift").getAsBoolean());
    }

    @Test @Order(22) @DisplayName("Agent: checksum_validator — all checksums valid")
    void testChecksumValidator() {
        var agent = new ChecksumValidatorAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema",       "core");
        args.addProperty("validate_all", true);
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.get("all_valid").getAsBoolean());
        assertEquals(0, r.get("mismatch_count").getAsInt());
    }

    @Test @Order(23) @DisplayName("Agent: helm_init_container_status — all completed")
    void testHelmInitContainerStatus() {
        var agent = new HelmInitContainerStatusAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("environment", "prod");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertEquals(0, r.get("failed").getAsInt());
        assertTrue(r.get("completed").getAsInt() > 0);
    }

    @Test @Order(24) @DisplayName("Agent: dr_schema_consistency — step 34 post-restore")
    void testDrSchemaConsistency() {
        var agent = new DrSchemaConsistencyAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("dr_step", "STEP_34_POST_RESTORE");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertEquals("DR Runbook Step 34", r.get("runbook_ref").getAsString());
        assertTrue(r.has("flyway_command"));
    }

    @Test @Order(25) @DisplayName("Agent: rls_policy_tracker — lists phone_bridge policies")
    void testRlsPolicyTracker() {
        var agent = new RlsPolicyTrackerAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("schema", "phone_bridge");
        args.addProperty("action", "LIST_POLICIES");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertTrue(r.has("rls_policies"));
        JsonArray policies = r.get("rls_policies").getAsJsonArray();
        assertTrue(policies.size() >= 3, "phone_bridge has 3 RLS tables");
    }

    @Test @Order(26) @DisplayName("Agent: compliance_audit_reporter — DPDPA 2023 compliant")
    void testComplianceAuditReporter() {
        var agent = new ComplianceAuditReporterAgent(security);
        JsonObject args = new JsonObject();
        args.addProperty("report_type", "DPDPA_2023");
        args.addProperty("schema",      "core");
        JsonObject r = agent.execute(args);
        assertEquals("success", r.get("status").getAsString());
        assertEquals("COMPLIANT", r.get("compliance_status").getAsString());
        assertTrue(r.has("report_id"));
    }

    @Test @Order(27) @DisplayName("All 18 agents have valid tool definitions")
    void testAllAgentsHaveToolDefinitions() {
        McpAgent[] agents = {
            new MigrationApplyAgent(security),
            new MigrationStatusAgent(security),
            new MigrationValidateAgent(security),
            new MigrationRepairAgent(security),
            new MigrationBaselineAgent(security),
            new MigrationInfoAgent(security),
            new SchemaDriftDetectorAgent(security),
            new SchemaHistoryQueryAgent(security),
            new ChecksumValidatorAgent(security),
            new MultiSchemaManagerAgent(security),
            new HelmInitContainerStatusAgent(security),
            new KubernetesSecretValidatorAgent(security),
            new GitlabPipelineTriggerAgent(security),
            new EnvironmentParityCheckerAgent(security),
            new DrSchemaConsistencyAgent(security),
            new DrRunbookExecutorAgent(security),
            new RlsPolicyTrackerAgent(security),
            new ComplianceAuditReporterAgent(security),
        };
        assertEquals(18, agents.length, "Must have exactly 18 agents");

        for (McpAgent agent : agents) {
            JsonObject def = agent.getToolDefinition();
            assertNotNull(def,  "Tool definition must not be null");
            assertTrue(def.has("name"),        "Tool must have name");
            assertTrue(def.has("description"), "Tool must have description");
            assertTrue(def.has("inputSchema"), "Tool must have inputSchema");
            assertFalse(def.get("name").getAsString().isEmpty(), "Tool name must not be empty");
        }
    }
}
