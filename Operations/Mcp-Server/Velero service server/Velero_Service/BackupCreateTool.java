package com.ecoskiller.mcp.velero.tools;

import com.ecoskiller.mcp.velero.model.ToolResult;
import com.ecoskiller.mcp.velero.security.SecurityManager;
import com.ecoskiller.mcp.velero.util.CommandRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * AGENT 1 — BACKUP_CREATE_AGENT
 *
 * Creates a Velero backup (manual on-demand or named pre-change snapshot).
 * Ecoskiller namespaces: core, realtime, billing, analytics, admin, ops, media.
 * Used before risky Helm upgrades and K8s version bumps.
 */
public class BackupCreateTool extends BaseTool {

    public BackupCreateTool(SecurityManager security) { super(security); }

    @Override public String getName()        { return "backup_create"; }
    @Override public String getDescription() {
        return "Create a Velero backup of the EcoSkiller k3s cluster. "
             + "Supports all-namespace daily backups and named pre-change snapshots "
             + "(e.g. pre-migration-2026-03-20). Database PVCs (PostgreSQL, Redis, ClickHouse) "
             + "are always excluded per the EcoSkiller backup policy.";
    }

    @Override
    public ObjectNode getInputSchema() {
        ObjectNode s = schema(mapper);
        addStringProp(s, "backup_name",
            "Backup name (alphanumeric, dash, underscore). E.g. 'pre-migration-2026-03-20' or 'ecoskiller-manual'.",
            true);
        addStringProp(s, "include_namespaces",
            "Comma-separated namespaces to include. Allowed: core,realtime,billing,analytics,admin,ops,media. "
            + "Leave empty for all namespaces.",
            false);
        addStringProp(s, "ttl",
            "Backup retention TTL. Format: 168h (7 days), 720h (30 days). Default: 168h.",
            false);
        addBoolProp(s, "wait",
            "Wait for backup completion before returning. Default: false.");
        return s;
    }

    @Override
    public ToolResult execute(JsonNode args) {
        try {
            String backupName = security.validateName(getStr(args, "backup_name"));
            String ttlRaw     = getStr(args, "ttl");
            String ttl        = ttlRaw.isBlank() ? "168h" : security.validateTtl(ttlRaw);
            boolean wait      = getBool(args, "wait", false);

            List<String> cmd = new ArrayList<>(List.of(
                "backup", "create", backupName,
                "--ttl", ttl,
                "--storage-location", "default"
            ));

            // Namespace scoping (optional)
            String nsRaw = getStr(args, "include_namespaces");
            if (!nsRaw.isBlank()) {
                List<String> namespaces = security.validateNamespaceList(nsRaw);
                cmd.add("--include-namespaces");
                cmd.add(String.join(",", namespaces));
            }

            // Always exclude database PVCs per EcoSkiller backup policy
            cmd.addAll(List.of(
                "--exclude-resources", "persistentvolumeclaims",
                "--labels", "managed-by=velero-mcp,ecoskiller=true"
            ));

            if (wait) cmd.add("--wait");

            CommandRunner.Result result = runner.run(cmd);

            if (result.success()) {
                return ToolResult.ok(
                    "✅ Backup '" + backupName + "' created successfully.\n"
                    + "TTL: " + ttl + " | Storage: default (MinIO GCP us-central1)\n"
                    + "Note: Database PVCs (PostgreSQL, Redis, ClickHouse) excluded per policy.\n\n"
                    + result.summary()
                );
            } else {
                return ToolResult.error("Backup creation failed.\n" + result.summary());
            }
        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
