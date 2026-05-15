package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * ERP_ANALYTICS_AGENT
 *
 * <p>Cross-module corporate analytics — revenue, cost, headcount,
 * procurement, and custom KPI dashboards via ClickHouse.</p>
 */
@Slf4j
@Component
public class ErpAnalyticsTool implements AgentTool {

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId   = getString(args, "tenant_id");
        String action     = getString(args, "action");
        String reportType = getString(args, "report_type");
        String dateFrom   = getString(args, "date_from");
        String dateTo     = getString(args, "date_to");
        String format     = getString(args, "format");

        log.info("[ERP_ANALYTICS] tenant={} action={} type={} [{} → {}]",
                tenantId, action, reportType, dateFrom, dateTo);

        return switch (action) {
            case "run_report"             -> queued("run_report", UUID.randomUUID().toString(),
                "Type  : " + reportType + "\nRange : " + dateFrom + " → " + dateTo);
            case "schedule_report"        -> ok("schedule_report",
                "Report scheduled.\nschedule_id: " + UUID.randomUUID());
            case "get_kpi_dashboard"      -> ok("get_kpi_dashboard",
                "Dashboard URL: /api/erp-analytics/dashboard/" + tenantId);
            case "create_custom_kpi"      -> ok("create_custom_kpi",
                "Custom KPI created.\nkpi_id: " + UUID.randomUUID());
            case "get_cost_centre_breakdown" -> queued("get_cost_centre_breakdown",
                UUID.randomUUID().toString(),
                "Period: " + dateFrom + " → " + dateTo);
            case "export_data"            -> queued("export_data", UUID.randomUUID().toString(),
                "Format: " + format);
            case "get_drill_down"         -> ok("get_drill_down",
                "Drill-down data returned. 0 rows (connect ClickHouse).");
            default -> "Unknown action: " + action;
        };
    }
}
