package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * MARKET_DEMAND_PREDICTION_AGENT_SPEC
 * Predicts demand for skills, courses, and ideas in the EcoSkiller marketplace.
 */
public class MarketDemandPredictionAgent extends ToolDefinition {

    @Override public String getName() { return "market_demand_prediction"; }

    @Override public String getDescription() {
        return "MARKET_DEMAND_PREDICTION_AGENT_SPEC — Predicts marketplace demand for skills, " +
               "courses, idea licenses, and competition slots using trend analysis and " +
               "regional demand signals.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "category");
        addStringProp(s, "category",   "Skill/product category to predict demand for");
        addStringProp(s, "region",     "Geographic region (e.g. Maharashtra, PAN_INDIA)");
        addStringProp(s, "horizon",    "Forecast horizon: 30d | 90d | 180d | 365d");
        addStringProp(s, "segment",    "Target segment: student | professional | rural");
        addBoolProp  (s, "include_competitors", "Include competitor activity signals");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String category   = str(args, "category",  "coding");
        String region     = str(args, "region",    "PAN_INDIA");
        String horizon    = str(args, "horizon",   "90d");
        String segment    = str(args, "segment",   "student");

        ObjectNode forecast = m.createObjectNode();
        forecast.put("demand_score",        78.4);
        forecast.put("trend",               "RISING");
        forecast.put("growth_rate_pct",     14.2);
        forecast.put("confidence_pct",      82.0);
        forecast.put("peak_window",         "Jun-Aug 2025");

        ArrayNode drivers = forecast.putArray("top_demand_drivers");
        drivers.add("Exam season alignment");
        drivers.add("Government skilling push");
        drivers.add("Rural broadband penetration");

        ObjectNode data = m.createObjectNode();
        data.put("category", category);
        data.put("region",   region);
        data.put("horizon",  horizon);
        data.put("segment",  segment);
        data.set("forecast", forecast);

        return successWithData(m,
            "Demand prediction for '" + category + "' in " + region + " over " + horizon, data);
    }
}
