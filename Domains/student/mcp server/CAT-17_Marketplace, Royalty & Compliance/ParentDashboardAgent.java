package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * PARENT_DASHBOARD_AGENT_SPEC
 * Aggregates child progress, skill scores, earnings, and competition results for parent view.
 */
public class ParentDashboardAgent extends ToolDefinition {

    @Override public String getName() { return "parent_dashboard"; }

    @Override public String getDescription() {
        return "PARENT_DASHBOARD_AGENT_SPEC — Aggregates child skill progress, competition results, " +
               "earnings, royalty credits, and certificate status into a unified parent dashboard payload.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "parent_id");
        addStringProp(s, "parent_id",  "Parent user ID");
        addStringProp(s, "child_id",   "Optional: filter to a specific child");
        addStringProp(s, "period",     "Reporting period: week | month | quarter | all_time");
        addBoolProp  (s, "include_financials", "Include royalty/earnings data");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String parentId          = str(args, "parent_id",          "PARENT-UNKNOWN");
        String childId           = str(args, "child_id",           "all");
        String period            = str(args, "period",             "month");
        boolean includeFinancials= bool(args,"include_financials",  true);

        ObjectNode child = m.createObjectNode();
        child.put("child_id",           childId.equals("all") ? "CHILD-DEMO-001" : childId);
        child.put("name",               "Demo Child");
        child.put("skills_completed",   7);
        child.put("competitions_entered",3);
        child.put("competitions_won",    1);
        child.put("certificates_earned", 2);
        child.put("current_rank",       "Silver");
        child.put("skill_score",        82.5);

        ObjectNode data = m.createObjectNode();
        data.put("parent_id", parentId);
        data.put("period",    period);
        data.set("child_summary", child);

        if (includeFinancials) {
            ObjectNode fin = m.createObjectNode();
            fin.put("total_royalties_earned_inr", 1250.00);
            fin.put("pending_payout_inr",          450.00);
            fin.put("wallet_balance_inr",           800.00);
            data.set("financials", fin);
        }

        return successWithData(m, "Dashboard data fetched for parent " + parentId, data);
    }
}
