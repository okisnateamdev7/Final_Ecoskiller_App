package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AGENT-10: MARKET_DEMAND_PREDICTION_AGENT
 * Predicts market demand for skills and content categories
 * to guide royalty pricing and marketplace decisions.
 */
@Component
public class MarketDemandPredictionAgent {

    @Tool(name = "market_demand_predict",
          description = "Predict market demand for a skill/content category over next N months.")
    public AgentResponse predictDemand(
            @ToolParam(description = "Skill or content category e.g. Python, Robotics, Chess") String category,
            @ToolParam(description = "Region: Maharashtra | PAN_INDIA | Global") String region,
            @ToolParam(description = "Forecast months 1-12") int forecastMonths) {

        return AgentResponse.ok("MARKET_DEMAND_PREDICTION_AGENT",
                "Demand prediction for " + category + " in " + region,
                Map.of(
                        "category",            category,
                        "region",              region,
                        "forecastMonths",      forecastMonths,
                        "demandScore",         87.3,
                        "demandTrend",         "RISING",
                        "predictedGrowth",     "22%",
                        "recommendedRate",     "PREMIUM",
                        "competitors",         List.of("AI/ML","Data Science","Coding"),
                        "confidenceScore",     0.82
                ));
    }

    @Tool(name = "trending_skills_get",
          description = "Get top N trending skills in the marketplace by demand score.")
    public AgentResponse getTrendingSkills(
            @ToolParam(description = "Region filter") String region,
            @ToolParam(description = "Top N results (max 10)") int topN) {

        List<Map<String, Object>> all = List.of(
                Map.of("rank",1,"skill","AI & Robotics",      "demandScore",95.2),
                Map.of("rank",2,"skill","Competitive Coding", "demandScore",91.0),
                Map.of("rank",3,"skill","Chess & Strategy",   "demandScore",88.5),
                Map.of("rank",4,"skill","Creative Writing",   "demandScore",82.1),
                Map.of("rank",5,"skill","Financial Literacy", "demandScore",79.8)
        );

        return AgentResponse.ok("MARKET_DEMAND_PREDICTION_AGENT",
                "Top " + topN + " trending skills",
                Map.of("region", region, "skills", all.subList(0, Math.min(topN, all.size()))));
    }
}
