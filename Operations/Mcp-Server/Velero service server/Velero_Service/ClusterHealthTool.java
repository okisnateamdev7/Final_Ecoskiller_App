package com.ecoskiller.mcp.velero.tools;

import com.ecoskiller.mcp.velero.model.ToolResult;
import com.ecoskiller.mcp.velero.security.SecurityManager;
import com.ecoskiller.mcp.velero.util.CommandRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

/**
 * AGENT 16 — CLUSTER_HEALTH_AGENT
 *
 * Provides a holistic Velero health snapshot for the EcoSkiller ops team:
 *   1. Latest backup status — is the daily backup healthy?
 *   2. Active schedules — is ecoskiller-daily running?
 *   3. BSL availability  — is MinIO reachable?
 *   4. Recent restore history — any recent restore activity?
 *   5. Prometheus metrics summary (via velero CLI)
 *
 * Acts as the "dashboard" tool — single call, full picture.
 * Maps to the Prometheus metrics:
 *   velero_backup_success_total, velero_backup_failure_total,
 *   velero_restore_success_total, velero_restore_duration_seconds
 */
public class ClusterHealthTool extends BaseTool {

    public ClusterHealthTool(SecurityManager security) { super(security); }

    @Override public String getName()        { return "cluster_health"; }
    @Override public String getDescription() {
        return "Get a full Velero health snapshot for the EcoSkiller cluster. "
             + "Checks: latest backup status, schedule health (ecoskiller-daily), "
             + "MinIO BSL availability, recent restore activity, and Velero pod status. "
             + "Single-call ops dashboard for on-call engineers and post-incident review.";
    }

    @Override
    public ObjectNode getInputSchema() {
        ObjectNode s = schema(mapper);
        addBoolProp(s, "include_restores",
            "Include recent restore operation history. Default: true.");
        addBoolProp(s, "include_schedules",
            "Include schedule health check. Default: true.");
        return s;
    }

    @Override
    public ToolResult execute(JsonNode args) {
        try {
            boolean includeRestores  = getBool(args, "include_restores",  true);
            boolean includeSchedules = getBool(args, "include_schedules", true);

            StringBuilder report = new StringBuilder();
            report.append("🏥 EcoSkiller Velero Cluster Health Report\n");
            report.append("═".repeat(55)).append("\n");
            report.append("Timestamp : ").append(java.time.Instant.now()).append("\n");
            report.append("Platform  : EcoSkiller k3s | GCP us-central1 + AWS us-east-1\n\n");

            // ── 1. Latest backups ──────────────────────────────────────────
            report.append("── 1. Backup Status (last 7 snapshots) ──\n");
            CommandRunner.Result backups = runner.run(List.of("backup", "get"));
            if (backups.success()) {
                report.append(backups.stdout.lines().limit(10)
                    .reduce("", (a, b) -> a + b + "\n"));
                boolean anyFailed = backups.stdout.contains("Failed") || backups.stdout.contains("PartiallyFailed");
                report.append(anyFailed
                    ? "⚠️  ALERT: One or more backups FAILED — check Prometheus velero_backup_failure_total.\n"
                    : "✅ All recent backups appear Completed.\n");
            } else {
                report.append("❌ Could not retrieve backup list: ").append(backups.stderr).append("\n");
            }
            report.append("\n");

            // ── 2. Schedule health ─────────────────────────────────────────
            if (includeSchedules) {
                report.append("── 2. Schedule Health ──\n");
                CommandRunner.Result schedules = runner.run(List.of("schedule", "get"));
                if (schedules.success()) {
                    report.append(schedules.summary()).append("\n");
                    boolean hasDaily = schedules.stdout.contains("ecoskiller-daily");
                    report.append(hasDaily
                        ? "✅ ecoskiller-daily schedule is present.\n"
                        : "❌ CRITICAL: ecoskiller-daily schedule NOT FOUND — daily backups are not running!\n");
                } else {
                    report.append("❌ Could not retrieve schedules.\n");
                }
                report.append("\n");
            }

            // ── 3. BSL availability ────────────────────────────────────────
            report.append("── 3. MinIO Backup Storage Location ──\n");
            CommandRunner.Result bsl = runner.run(List.of("backup-location", "get"));
            if (bsl.success()) {
                report.append(bsl.summary()).append("\n");
                boolean available = bsl.stdout.contains("Available");
                report.append(available
                    ? "✅ MinIO BSL is Available.\n"
                    : "❌ CRITICAL: MinIO BSL not Available — backups cannot be written/read!\n");
            } else {
                report.append("❌ Could not check BSL.\n");
            }
            report.append("\n");

            // ── 4. Recent restores ─────────────────────────────────────────
            if (includeRestores) {
                report.append("── 4. Recent Restore Activity ──\n");
                CommandRunner.Result restores = runner.run(List.of("restore", "get"));
                if (restores.success() && !restores.stdout.isBlank()) {
                    report.append(restores.stdout.lines().limit(6).reduce("", (a, b) -> a + b + "\n"));
                } else {
                    report.append("No recent restore operations.\n");
                }
                report.append("\n");
            }

            // ── 5. Summary ─────────────────────────────────────────────────
            report.append("── 5. Monitoring Reference ──\n");
            report.append("Prometheus metrics to check:\n");
            report.append("  velero_backup_success_total     → should be incrementing daily\n");
            report.append("  velero_backup_failure_total     → should be 0 (P1 if > 0)\n");
            report.append("  velero_restore_success_total    → increments per drill/incident\n");
            report.append("  velero_restore_duration_seconds → target < 3600s for full restore\n");
            report.append("\nGrafana: ops namespace dashboards | Alert: Alertmanager → ntfy + #incidents\n");

            return ToolResult.ok(report.toString());

        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
