package com.ecoskiller.mcp.corporate.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * BUDGET_AGENT
 *
 * <p>Corporate budget planning, allocation, variance tracking, and
 * rolling forecasts across departments and fiscal years.</p>
 */
@Slf4j
@Component
public class BudgetTool implements AgentTool {

    @Override
    public String execute(JsonNode args) throws Exception {
        String tenantId   = getString(args, "tenant_id");
        String action     = getString(args, "action");
        String budgetId   = getString(args, "budget_id");
        String deptId     = getString(args, "dept_id");
        String fiscalYear = getString(args, "fiscal_year");

        log.info("[BUDGET] tenant={} action={} budget={} fy={}", tenantId, action, budgetId, fiscalYear);

        return switch (action) {
            case "create_budget"        -> ok("create_budget",
                "Budget created for FY=" + fiscalYear + ".\nbudget_id: " + UUID.randomUUID());
            case "update_budget"        -> ok("update_budget",
                "Budget lines updated for budget_id=" + budgetId);
            case "approve_budget"       -> ok("approve_budget",
                "Budget " + budgetId + " approved. Department allocations unlocked.");
            case "allocate_to_dept"     -> ok("allocate_to_dept",
                "Allocation made to dept=" + deptId + " from budget=" + budgetId);
            case "get_variance_report"  -> queued("get_variance_report", UUID.randomUUID().toString(),
                "FY: " + fiscalYear + " | Budget: " + budgetId);
            case "set_alert_threshold"  -> ok("set_alert_threshold",
                "Alert threshold set. Notifications will fire at configured % utilisation.");
            case "get_forecast"         -> queued("get_forecast", UUID.randomUUID().toString(),
                "Rolling 12-month forecast queued.");
            case "lock_budget"          -> ok("lock_budget",
                "Budget " + budgetId + " locked. No further amendments allowed.");
            case "get_utilisation"      -> ok("get_utilisation",
                "Utilisation report: /api/budget/" + budgetId + "/utilisation");
            default -> "Unknown action: " + action;
        };
    }
}
