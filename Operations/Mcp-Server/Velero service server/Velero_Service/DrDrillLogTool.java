package com.ecoskiller.mcp.velero.tools;

import com.ecoskiller.mcp.velero.model.ToolResult;
import com.ecoskiller.mcp.velero.security.SecurityManager;
import com.ecoskiller.mcp.velero.util.CommandRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.util.*;

/**
 * AGENT 15 — DR_DRILL_LOG_AGENT
 *
 * Documents and validates quarterly DR drill results per the EcoSkiller DR rules:
 *
 *   "Every DR drill result must be documented in a shared DR log
 *    (ClickHouse audit table or Wiki.js). If any drill fails or exceeds RTO,
 *    a P2 incident must be raised and the runbook updated before the next sprint ends."
 *
 * Drill types (from EcoSkiller DR Drill Schedule):
 *   - Full cluster restore    : Quarterly | Stage | RTO < 1 hour
 *   - Namespace restore drill : Monthly   | Stage | RTO < 30 min
 *   - Restore-verify script   : Weekly    | Stage (auto)
 *   - Backup integrity check  : Weekly    | Automated
 */
public class DrDrillLogTool extends BaseTool {

    private static final long FULL_RESTORE_RTO_MINUTES    = 60;
    private static final long NAMESPACE_RESTORE_RTO_MINUTES = 30;

    public DrDrillLogTool(SecurityManager security) { super(security); }

    @Override public String getName()        { return "dr_drill_log"; }
    @Override public String getDescription() {
        return "Record and validate an EcoSkiller DR drill result. "
             + "Checks if the restore met the RTO target, formats a compliance report, "
             + "and emits a P2 incident notice if RTO was exceeded or drill failed. "
             + "Mandatory per EcoSkiller DR rules — results must be logged to ClickHouse/Wiki.js. "
             + "Drill types: full_cluster (quarterly, RTO<1hr), namespace (monthly, RTO<30min), "
             + "restore_verify (weekly auto), integrity_check (weekly auto).";
    }

    @Override
    public ObjectNode getInputSchema() {
        ObjectNode s = schema(mapper);
        addStringProp(s, "drill_type",
            "Type of DR drill: 'full_cluster' | 'namespace' | 'restore_verify' | 'integrity_check'.", true);
        addStringProp(s, "restore_name",
            "Name of the Velero restore operation that was executed in the drill.", true);
        addStringProp(s, "backup_name",
            "Name of the backup snapshot used in the drill.", true);
        addStringProp(s, "start_time",
            "Drill start time as ISO-8601 string. E.g. '2026-03-20T02:00:00Z'.", true);
        addStringProp(s, "end_time",
            "Drill completion time as ISO-8601 string. E.g. '2026-03-20T02:45:00Z'.", true);
        addStringProp(s, "outcome",
            "Drill outcome: 'passed' | 'failed' | 'partial'.", true);
        addStringProp(s, "namespaces_restored",
            "Comma-separated list of namespaces restored in the drill.", false);
        addStringProp(s, "notes",
            "Free-text notes on drill findings, blockers, or runbook update requirements.", false);
        return s;
    }

    @Override
    public ToolResult execute(JsonNode args) {
        try {
            String drillType    = security.sanitiseFreeText(getStr(args, "drill_type")).toLowerCase();
            String restoreName  = security.validateName(getStr(args, "restore_name"));
            String backupName   = security.validateName(getStr(args, "backup_name"));
            String startTimeStr = security.sanitiseFreeText(getStr(args, "start_time"));
            String endTimeStr   = security.sanitiseFreeText(getStr(args, "end_time"));
            String outcome      = security.sanitiseFreeText(getStr(args, "outcome")).toLowerCase();
            String nsRestored   = security.sanitiseFreeText(getStr(args, "namespaces_restored"));
            String notes        = security.sanitiseFreeText(getStr(args, "notes"));

            // Validate drill type
            Set<String> validTypes = Set.of("full_cluster", "namespace", "restore_verify", "integrity_check");
            if (!validTypes.contains(drillType)) {
                return ToolResult.error("Invalid drill_type '" + drillType + "'. Use: " + validTypes);
            }

            // Validate outcome
            if (!Set.of("passed", "failed", "partial").contains(outcome)) {
                return ToolResult.error("Invalid outcome '" + outcome + "'. Use: passed | failed | partial");
            }

            // Calculate elapsed time
            long elapsedMinutes = -1;
            try {
                Instant start = Instant.parse(startTimeStr);
                Instant end   = Instant.parse(endTimeStr);
                elapsedMinutes = java.time.Duration.between(start, end).toMinutes();
            } catch (Exception e) {
                // Non-fatal — still produce report
            }

            // Determine RTO target
            long rtoTarget = drillType.equals("full_cluster") ? FULL_RESTORE_RTO_MINUTES : NAMESPACE_RESTORE_RTO_MINUTES;
            boolean rtoMet = elapsedMinutes >= 0 && elapsedMinutes <= rtoTarget;
            boolean p2Required = !rtoMet || "failed".equals(outcome) || "partial".equals(outcome);

            // Get restore details from Velero
            CommandRunner.Result restoreDesc = runner.run(List.of("restore", "describe", restoreName));

            // Build compliance report
            StringBuilder report = new StringBuilder();
            report.append("📋 EcoSkiller DR Drill Compliance Report\n");
            report.append("═".repeat(55)).append("\n");
            report.append("Drill Type       : ").append(drillType.toUpperCase()).append("\n");
            report.append("Restore Name     : ").append(restoreName).append("\n");
            report.append("Backup Used      : ").append(backupName).append("\n");
            report.append("Start Time       : ").append(startTimeStr).append("\n");
            report.append("End Time         : ").append(endTimeStr).append("\n");
            report.append("Elapsed          : ").append(elapsedMinutes >= 0 ? elapsedMinutes + " min" : "N/A").append("\n");
            report.append("RTO Target       : ").append(rtoTarget).append(" min\n");
            report.append("RTO Met          : ").append(rtoMet ? "✅ YES" : "❌ NO").append("\n");
            report.append("Outcome          : ").append(outcome.toUpperCase()).append("\n");
            if (!nsRestored.isBlank())
                report.append("Namespaces       : ").append(nsRestored).append("\n");
            if (!notes.isBlank())
                report.append("Notes            : ").append(notes).append("\n");
            report.append("\n── Velero Restore Details ──\n");
            report.append(restoreDesc.summary()).append("\n");

            report.append("\n── Compliance Actions ──\n");
            if (p2Required) {
                report.append("⚠️  P2 INCIDENT REQUIRED:\n");
                if (!rtoMet)    report.append("  • RTO exceeded (").append(elapsedMinutes).append(" min > ").append(rtoTarget).append(" min target)\n");
                if (!"passed".equals(outcome)) report.append("  • Drill outcome was ").append(outcome).append(" (not passed)\n");
                report.append("  • Raise P2 incident and update DR runbook before next sprint ends.\n");
            } else {
                report.append("✅ Drill PASSED all compliance checks.\n");
            }
            report.append("\n📌 ACTION: Log this report to ClickHouse DR audit table or Wiki.js.\n");
            report.append("   (Mandatory per EcoSkiller DR policy)\n");

            return p2Required
                ? ToolResult.error(report.toString())
                : ToolResult.ok(report.toString());

        } catch (SecurityException e) {
            security.logViolation(getName(), e.getMessage());
            return ToolResult.error("Security violation: " + e.getMessage());
        }
    }
}
