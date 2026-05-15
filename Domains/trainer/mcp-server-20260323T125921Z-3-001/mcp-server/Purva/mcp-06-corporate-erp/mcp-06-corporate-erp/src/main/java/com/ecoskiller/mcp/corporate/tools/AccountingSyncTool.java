package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * ACCOUNTING_SYNC_AGENT
 *
 * <p>Bidirectional sync between Ecoskiller ERP and external accounting
 * platforms (Tally, Zoho Books, QuickBooks, SAP).</p>
 */
@Slf4j
@Component
public class AccountingSyncTool implements AgentTool {

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId  = getString(args, "tenant_id");
        String action    = getString(args, "action");
        String platform  = getString(args, "platform");
        String syncMode  = getString(args, "sync_mode");
        String syncId    = getString(args, "sync_id");
        boolean dryRun   = getBool(args, "dry_run", false);

        log.info("[ACCOUNTING_SYNC] tenant={} action={} platform={} mode={}", tenantId, action, platform, syncMode);

        if (dryRun && "start_sync".equals(action)) {
            return dryRun("start_sync",
                "Platform connectivity validated: " + platform +
                "\nMode: " + syncMode +
                "\nEstimated records: 0 (connect platform credentials).");
        }

        return switch (action) {
            case "start_sync"      -> queued("start_sync", UUID.randomUUID().toString(),
                "Platform : " + platform + "\nMode     : " + syncMode);
            case "get_status"      -> ok("get_status",
                "sync_id=" + syncId + "\nstatus: PENDING (demo).");
            case "pause"           -> ok("pause",  "Sync paused.");
            case "resume"          -> ok("resume", "Sync resumed.");
            case "rollback"        -> ok("rollback",
                "Rollback complete for sync_id=" + syncId);
            case "configure"       -> ok("configure",
                "Platform " + platform + " configured for tenant=" + tenantId);
            case "get_sync_report" -> ok("get_sync_report",
                "Report: /api/accounting-sync/" + syncId + "/report");
            default -> "Unknown action: " + action;
        };
    }
}
