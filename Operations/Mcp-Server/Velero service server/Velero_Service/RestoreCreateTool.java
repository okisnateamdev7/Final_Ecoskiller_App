package com.ecoskiller.mcp.velero.tools;

import com.ecoskiller.mcp.velero.model.ToolResult;
import com.ecoskiller.mcp.velero.security.SecurityManager;
import com.ecoskiller.mcp.velero.util.CommandRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

/**
 * AGENT 6 — RESTORE_CREATE_AGENT
 *
 * Creates a Velero restore from a backup snapshot.
 * Supports full cluster restore and selective namespace restore.
 *
 * EcoSkiller DR context:
 *   - Full cluster restore: < 1 hour RTO
 *   - Single namespace restore: < 30 min RTO
 *   - Used in quarterly full cluster DR drills and real incidents
 *
 * SECURITY:
 *   - Rate-limited (destructive op — overwrites live cluster resources)
 *   - Requires confirm=true for full cluster restores (no namespace filter)
 *   - All namespace params validated against allow-list
 */
public class RestoreCreateTool extends BaseTool {

    public RestoreCreateTool(SecurityManager security) { super(security); }

    @Override public String getName()        { return "restore_create"; }
    @Override public String getDescription() {
        return "Create a Velero restore operation from a backup snapshot. "
             + "Supports full cluster restore (< 1hr RTO) or selective namespace restore "
             + "(--include-namespaces core,realtime etc, < 30min RTO). "
             + "Used during quarterly DR drills and real disaster recovery operations. "
             + "Full restore requires confirm=true as a safety gate.";
    }

    @Override
    public ObjectNode getInputSchema() {
        ObjectNode s = schema(mapper);
        addStringProp(s, "backup_name",
            "Name of the source backup snapshot to restore from.", true);
        addStringProp(s, "restore_name",
            "Name for this restore operation. Auto-generated from backup_name if blank.", false);
        addStringProp(s, "include_namespaces",
            "Comma-separated namespaces to restore. Allowed: core,realtime,billing,analytics,admin,ops,media. "
            + "Leave empty for full cluster restore (requires confirm=true).", false);
        addStringProp(s, "exclude_resources",
            "Comma-separated Kubernetes resource types to exclude from restore. E.g. 'persistentvolumeclaims'.", false);
        addBoolProp(s, "wait",
            "Wait for restore to complete before returning. Recommended for DR drills. Default: false.");
        addBoolProp(s, "confirm",
            "Required to be true for full-cluster restore (no include_namespaces set). "
            + "Safety gate against accidental full cluster overwrite.");
        ((com.fasterxml.jackson.databind.node.ArrayNode) s.get("required")).add("confirm");
        return s;
    }

    @Override
    public ToolResult execute(JsonNode args) {
        try {
            String backupName   = security.validateName(getStr(args, "backup_name"));
            String restoreNameR = getStr(args, "restore_name");
            String nsRaw        = getStr(args, "include_namespaces");
            boolean wait        = getBool(args, "wait", false);
            boolean confirm     = getBool(args, "confirm", false);
            boolean fullCluster = nsRaw.isBlank();

            // Full cluster restore requires explicit confirmation
            if (fullCluster && !confirm) {
                return ToolResult.error(
                    "FULL CLUSTER RESTORE requires confirm=true. "
                    + "This will overwrite all live cluster resources from backup '"
                    + backupName + "'. Set confirm=true to proceed.");
            }

            // Rate-limit restore as a destructive operation
            security.checkDestructiveRateLimit("restore_create");

            String restoreName = restoreNameR.isBlank()
                ? backupName + "-restore-" + System.currentTimeMillis()
                : security.validateName(restoreNameR);

            List<String> cmd = new ArrayList<>(List.of(
                "restore", "create", restoreName,
                "--from-backup", backupName
            ));

            if (!fullCluster) {
                List<String> namespaces = security.validateNamespaceList(nsRaw);
                cmd.addAll(List.of("--include-namespaces", String.join(",", namespaces)));
            }

            String excludeRes = getStr(args, "exclude_resources");
            if (!excludeRes.isBlank()) {
                String safeExclude = security.sanitiseFreeText(excludeRes);
                cmd.addAll(List.of("--exclude-resources", safeExclude));
            }

            if (wait) cmd.add("--wait");

            CommandRunner.Result result = runner.run(cmd);

            String scope = fullCluster ? "FULL CLUSTER" : "Namespaces: " + nsRaw;
            if (result.success()) {
                return ToolResult.ok(
                    "♻️ Restore '" + restoreName + "' started.\n"
                    + "Source backup : " + backupName + "\n"
                    + "Scope         : " + scope + "\n"
                    + "Target RTO    : " + (fullCluster ? "< 1 hour" : "< 30 minutes") + "\n\n"
                    + "Resources restored in order: Namespaces → RBAC → ConfigMaps/Secrets → Deployments\n\n"
                    + result.summary()
                );
            } else {
                return ToolResult.error("Restore failed.\n" + result.summary());
            }
        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
