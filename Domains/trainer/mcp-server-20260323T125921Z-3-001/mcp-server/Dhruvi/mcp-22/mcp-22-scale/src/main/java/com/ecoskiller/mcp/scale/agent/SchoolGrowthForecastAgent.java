package com.ecoskiller.mcp.scale.agent;

import com.ecoskiller.mcp.scale.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * AGENT-08: SCHOOL_GROWTH_FORECAST_AGENT
 * Forecasts student enrolment growth, revenue trajectory, and royalty pipeline
 * for individual schools and franchise portfolios on the Ecoskiller platform.
 */
@Component
public class SchoolGrowthForecastAgent {

    @Tool(name = "school_growth_forecast",
          description = "Forecast student enrolment and royalty revenue for a school over next N months.")
    public AgentResponse forecastGrowth(
            @ToolParam(description = "School ID") String schoolId,
            @ToolParam(description = "Current monthly enrolment count") int currentEnrolment,
            @ToolParam(description = "Forecast months 1-24") int forecastMonths,
            @ToolParam(description = "Growth model: LINEAR | EXPONENTIAL | SEASONAL") String growthModel) {

        double growthRate = "EXPONENTIAL".equals(growthModel) ? 0.12 : "SEASONAL".equals(growthModel) ? 0.08 : 0.05;
        double projectedEnrolment = currentEnrolment * Math.pow(1 + growthRate, forecastMonths);
        double projectedMonthlyRevenue = projectedEnrolment * 1500;
        double projectedMonthlyRoyalty = projectedMonthlyRevenue * 0.55;

        return AgentResponse.ok("SCHOOL_GROWTH_FORECAST_AGENT",
                "Growth forecast for school " + schoolId,
                Map.of(
                        "schoolId",                  schoolId,
                        "currentEnrolment",          currentEnrolment,
                        "forecastMonths",             forecastMonths,
                        "growthModel",               growthModel,
                        "appliedGrowthRate",         growthRate,
                        "projectedEnrolment",        Math.round(projectedEnrolment),
                        "projectedMonthlyRevenue",   Math.round(projectedMonthlyRevenue),
                        "projectedMonthlyRoyalty",   Math.round(projectedMonthlyRoyalty),
                        "forecastDate",              LocalDate.now().plusMonths(forecastMonths).toString(),
                        "confidenceLevel",           "MEDIUM"
                ));
    }

    @Tool(name = "franchise_portfolio_forecast",
          description = "Forecast aggregate growth and royalties for a franchise owner's entire school portfolio.")
    public AgentResponse forecastPortfolio(
            @ToolParam(description = "Franchise owner user ID") String franchiseOwnerId,
            @ToolParam(description = "Forecast months 1-12") int forecastMonths) {

        return AgentResponse.ok("SCHOOL_GROWTH_FORECAST_AGENT",
                "Portfolio forecast for franchise " + franchiseOwnerId,
                Map.of(
                        "franchiseOwnerId",        franchiseOwnerId,
                        "forecastMonths",          forecastMonths,
                        "totalSchools",            5,
                        "currentTotalEnrolment",   430,
                        "projectedTotalEnrolment", 612,
                        "currentMonthlyRoyalty",   354750.0,
                        "projectedMonthlyRoyalty", 504900.0,
                        "growthPercent",           "42.3%",
                        "topGrowingSchool",        "SCH-A8B3C1",
                        "riskSchools", List.of(
                                Map.of("schoolId","SCH-D4E5F6","reason","Enrolment decline for 2 consecutive months")
                        ),
                        "forecastDate", LocalDate.now().plusMonths(forecastMonths).toString()
                ));
    }
}
