package com.ecoskiller.mcp.longhorn;

import com.ecoskiller.mcp.longhorn.security.InputValidator;
import com.ecoskiller.mcp.longhorn.security.RateLimiter;
import com.ecoskiller.mcp.longhorn.server.JsonParser;
import com.ecoskiller.mcp.longhorn.server.JsonRpc;
import com.ecoskiller.mcp.longhorn.tools.*;

import java.util.*;

/**
 * Self-contained test runner — no external testing framework needed.
 * Run: java -cp . com.ecoskiller.mcp.longhorn.LonghornMcpTests
 */
public class LonghornMcpTests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║   Ecoskiller Longhorn MCP Server — Test Suite        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        testJsonParser();
        testJsonRpc();
        testInputValidator();
        testRateLimiter();
        testVolumeProvision();
        testVolumeList();
        testSnapshotCreate();
        testSnapshotDelete();
        testBackupTrigger();
        testBackupStatus();
        testReplicaHealth();
        testStorageMetrics();

        System.out.println("\n══════════════════════════════════════");
        System.out.printf("  Results: %d passed, %d failed%n", passed, failed);
        System.out.println("══════════════════════════════════════");

        if (failed > 0) System.exit(1);
    }

    // ─── JSON Parser ─────────────────────────────────────────

    static void testJsonParser() {
        section("JsonParser");

        Map<String, Object> obj = JsonParser.parse("{\"method\":\"tools/list\",\"id\":1}");
        assertEqual("method", "tools/list", obj.get("method"));
        assertEqual("id long", 1L, obj.get("id"));

        Map<String, Object> nested = JsonParser.parse("{\"params\":{\"name\":\"volume_list\",\"arguments\":{}}}");
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) nested.get("params");
        assertEqual("nested name", "volume_list", params.get("name"));

        assertThrows("non-object root", () -> JsonParser.parse("[1,2,3]"));
    }

    // ─── JsonRpc Builder ─────────────────────────────────────

    static void testJsonRpc() {
        section("JsonRpc");

        String r = JsonRpc.result("1", JsonRpc.obj("status", "ok"));
        assertTrue("result contains jsonrpc", r.contains("\"jsonrpc\":\"2.0\""));
        assertTrue("result contains id",      r.contains("\"id\":1"));

        String e = JsonRpc.error("2", -32601, "Method not found");
        assertTrue("error code",    e.contains("-32601"));
        assertTrue("error message", e.contains("Method not found"));

        String toolErr = JsonRpc.toolError("3", "Rate limit exceeded");
        assertTrue("toolError isError", toolErr.contains("\"isError\":true"));
    }

    // ─── Input Validator ─────────────────────────────────────

    static void testInputValidator() {
        section("InputValidator");

        InputValidator v = new InputValidator();

        assertTrue("valid tool name",   v.isValidToolName("volume_list"));
        assertTrue("valid with nums",   v.isValidToolName("snapshot_create"));
        assertFalse("empty name",       v.isValidToolName(""));
        assertFalse("null name",        v.isValidToolName(null));
        assertFalse("path traversal",   v.isValidToolName("../evil"));
        assertFalse("shell inject",     v.isValidToolName("tool;ls"));
        assertFalse("uppercase",        v.isValidToolName("Volume_List"));

        // Valid args
        Map<String, Object> good = Map.of("volume_name", "redis-data-pvc", "storage_gi", 10);
        assertNoThrow("valid args", () -> v.validateArguments(good));

        // Forbidden chars in value
        Map<String, Object> bad = new HashMap<>();
        bad.put("volume_name", "pvc; rm -rf /");
        assertThrows("shell injection in value", () -> v.validateArguments(bad));

        // Path traversal in value
        Map<String, Object> bad2 = new HashMap<>();
        bad2.put("namespace", "../ops");
        assertThrows("path traversal in value", () -> v.validateArguments(bad2));

        // Too long value
        Map<String, Object> bad3 = new HashMap<>();
        bad3.put("volume_name", "x".repeat(2000));
        assertThrows("value too long", () -> v.validateArguments(bad3));
    }

    // ─── Rate Limiter ─────────────────────────────────────────

    static void testRateLimiter() {
        section("RateLimiter");

        RateLimiter limiter = new RateLimiter(5, 1000);
        for (int i = 0; i < 5; i++) {
            assertTrue("request " + (i + 1) + " within limit", limiter.allowRequest("test_tool"));
        }
        assertFalse("6th request exceeds limit", limiter.allowRequest("test_tool"));

        // Different tool has own window
        assertTrue("different tool not rate limited", limiter.allowRequest("other_tool"));
    }

    // ─── Volume Provision ─────────────────────────────────────

    static void testVolumeProvision() {
        section("VolumeProvisionTool");

        VolumeProvisionTool tool = new VolumeProvisionTool();

        // Basic provision
        Map<String, Object> args = new HashMap<>();
        args.put("volume_name", "test-pvc");
        args.put("namespace",   "default");
        args.put("storage_gi",  20);
        args.put("consumer",    "redis");
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(args);
        assertEqual("status", "success", result.get("status"));
        assertEqual("storage_class", "longhorn", result.get("storage_class"));

        // OpenSearch requires encryption
        Map<String, Object> osArgs = new HashMap<>();
        osArgs.put("volume_name", "os-pvc");
        osArgs.put("namespace",   "default");
        osArgs.put("storage_gi",  50);
        osArgs.put("consumer",    "opensearch");
        osArgs.put("encrypted",   false);
        assertThrows("opensearch requires encryption", () -> tool.execute(osArgs));

        // Over 100Gi on single cloud
        Map<String, Object> bigArgs = new HashMap<>();
        bigArgs.put("volume_name", "big-pvc");
        bigArgs.put("namespace",   "default");
        bigArgs.put("storage_gi",  200);
        bigArgs.put("consumer",    "minio");
        bigArgs.put("cloud_target","gcp");
        assertThrows("single cloud 100Gi cap", () -> tool.execute(bigArgs));
    }

    // ─── Volume List ──────────────────────────────────────────

    static void testVolumeList() {
        section("VolumeListTool");

        VolumeListTool tool = new VolumeListTool();

        @SuppressWarnings("unchecked")
        Map<String, Object> all = (Map<String, Object>) tool.execute(Collections.emptyMap());
        assertTrue("returns volumes", (int) all.get("total_volumes") > 0);

        Map<String, Object> filterArgs = Map.of("cloud_target", "gcp");
        @SuppressWarnings("unchecked")
        Map<String, Object> gcpOnly = (Map<String, Object>) tool.execute(filterArgs);
        assertTrue("gcp filter works", (int) gcpOnly.get("total_volumes") > 0);
    }

    // ─── Snapshot Create ──────────────────────────────────────

    static void testSnapshotCreate() {
        section("SnapshotCreateTool");

        SnapshotCreateTool tool = new SnapshotCreateTool();

        Map<String, Object> args = Map.of(
            "volume_name", "postgresql-data-pvc",
            "namespace",   "default",
            "retain_days", 7
        );
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(args);
        assertEqual("status", "success", result.get("status"));
        assertTrue("snapshot_name set", result.get("snapshot_name") != null);
        assertEqual("velero_eligible", true, result.get("velero_eligible"));
    }

    // ─── Snapshot Delete ──────────────────────────────────────

    static void testSnapshotDelete() {
        section("SnapshotDeleteTool");

        SnapshotDeleteTool tool = new SnapshotDeleteTool();

        // Missing confirmation
        Map<String, Object> bad = new HashMap<>();
        bad.put("snapshot_name",  "some-snap");
        bad.put("namespace",      "default");
        bad.put("confirm_delete", "yes");
        assertThrows("requires exact confirm token", () -> tool.execute(bad));

        // Correct confirmation
        Map<String, Object> good = new HashMap<>();
        good.put("snapshot_name",  "some-snap");
        good.put("namespace",      "default");
        good.put("confirm_delete", "CONFIRM_SNAPSHOT_DELETE");
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(good);
        assertEqual("status", "success", result.get("status"));
    }

    // ─── Backup Trigger ───────────────────────────────────────

    static void testBackupTrigger() {
        section("BackupTriggerTool");

        BackupTriggerTool tool = new BackupTriggerTool();

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(Collections.emptyMap());
        assertEqual("status", "success", result.get("status"));
        assertTrue("backup_id set", result.get("backup_id") != null);
        assertTrue("primary_target set", result.get("primary_target") != null);
    }

    // ─── Backup Status ────────────────────────────────────────

    static void testBackupStatus() {
        section("BackupStatusTool");

        BackupStatusTool tool = new BackupStatusTool();

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(Collections.emptyMap());
        assertTrue("returns backups", (int) result.get("total") > 0);
        assertEqual("rpo redis", 15, result.get("rpo_sla_redis_minutes"));
        assertEqual("rpo block", 60, result.get("rpo_sla_block_volumes_minutes"));
    }

    // ─── Replica Health ───────────────────────────────────────

    static void testReplicaHealth() {
        section("ReplicaHealthTool");

        ReplicaHealthTool tool = new ReplicaHealthTool();

        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(Collections.emptyMap());
        assertTrue("has alert field", result.containsKey("alert"));
        assertTrue("degraded_count >= 0", (long) result.get("degraded_count") >= 0);

        Map<String, Object> degradedOnly = Map.of("degraded_only", true);
        @SuppressWarnings("unchecked")
        Map<String, Object> degraded = (Map<String, Object>) tool.execute(degradedOnly);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> vols = (List<Map<String, Object>>) degraded.get("volumes");
        for (Map<String, Object> v : vols) {
            assertEqual("only degraded returned", "degraded", v.get("health"));
        }
    }

    // ─── Storage Metrics ─────────────────────────────────────

    static void testStorageMetrics() {
        section("StorageMetricsTool");

        StorageMetricsTool tool = new StorageMetricsTool();

        Map<String, Object> args = Map.of("cloud_target", "gcp", "include_cost", true);
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) tool.execute(args);
        assertTrue("capacity > 0",        (int) result.get("total_capacity_gi") > 0);
        assertTrue("used > 0",            (int) result.get("total_used_gi") > 0);
        assertEqual("zero saas cost",     0, result.get("saas_storage_cost_usd"));
    }

    // ─── Test helpers ─────────────────────────────────────────

    static void section(String name) {
        System.out.println("\n▶ " + name);
    }

    static void assertEqual(String label, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("  ✓ " + label);
            passed++;
        } else {
            System.out.println("  ✗ " + label + " — expected: " + expected + ", got: " + actual);
            failed++;
        }
    }

    static void assertTrue(String label, boolean condition) {
        if (condition) {
            System.out.println("  ✓ " + label);
            passed++;
        } else {
            System.out.println("  ✗ " + label + " — condition was false");
            failed++;
        }
    }

    static void assertFalse(String label, boolean condition) {
        assertTrue(label, !condition);
    }

    static void assertThrows(String label, Runnable r) {
        try {
            r.run();
            System.out.println("  ✗ " + label + " — expected exception but none thrown");
            failed++;
        } catch (Exception e) {
            System.out.println("  ✓ " + label + " (threw: " + e.getClass().getSimpleName() + ")");
            passed++;
        }
    }

    static void assertNoThrow(String label, Runnable r) {
        try {
            r.run();
            System.out.println("  ✓ " + label);
            passed++;
        } catch (Exception e) {
            System.out.println("  ✗ " + label + " — unexpected exception: " + e.getMessage());
            failed++;
        }
    }
}
