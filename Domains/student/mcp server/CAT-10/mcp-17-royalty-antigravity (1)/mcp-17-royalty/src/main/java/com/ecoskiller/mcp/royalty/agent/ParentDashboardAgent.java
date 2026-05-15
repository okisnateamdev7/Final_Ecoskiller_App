package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AGENT-09: PARENT_DASHBOARD_AGENT
 * Royalty and earnings summary dashboard for parents and franchise owners.
 */
@Component
public class ParentDashboardAgent {

    @Tool(name = "parent_dashboard_summary",
          description = "Get royalty and revenue summary for parent/franchise dashboard.")
    public AgentResponse getDashboardSummary(
            @ToolParam(description = "Parent or franchise user ID") String parentId,
            @ToolParam(description = "Period: TODAY | THIS_WEEK | THIS_MONTH | THIS_YEAR") String period) {

        return AgentResponse.ok("PARENT_DASHBOARD_AGENT",
                "Dashboard summary for " + period,
                Map.of(
                        "parentId",             parentId,
                        "period",               period,
                        "totalRevenue",         85000.0,
                        "royaltyEarned",        51000.0,
                        "pendingPayout",        12000.0,
                        "totalStudents",        430,
                        "activeSchools",        5,
                        "topContent",           "Advanced Mathematics Course",
                        "growthRate",           "18%"
                ));
    }

    @Tool(name = "parent_child_performance",
          description = "Get children skill performance linked to royalty-generating content.")
    public AgentResponse getChildPerformance(
            @ToolParam(description = "Parent user ID") String parentId) {

        return AgentResponse.ok("PARENT_DASHBOARD_AGENT",
                "Child performance data fetched",
                Map.of(
                        "parentId", parentId,
                        "children", List.of(
                                Map.of("childId","CHD-001","name","Riya", "rank",12,"skillScore",87.5),
                                Map.of("childId","CHD-002","name","Aryan","rank",34,"skillScore",74.2)
                        )
                ));
    }
}
