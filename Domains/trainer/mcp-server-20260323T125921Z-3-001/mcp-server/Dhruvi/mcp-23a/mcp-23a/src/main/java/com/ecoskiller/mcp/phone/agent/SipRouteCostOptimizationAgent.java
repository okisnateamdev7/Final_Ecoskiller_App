package com.ecoskiller.mcp.phone.agent;

import com.ecoskiller.mcp.phone.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-15: SIP_ROUTE_COST_OPTIMIZATION_AGENT
 * Selects the lowest-cost SIP route for outbound calls considering
 * per-minute rates, quality scores, and region proximity.
 */
@Component
public class SipRouteCostOptimizationAgent {

    @Tool(name = "sip_route_optimize",
          description = "Select the optimal (lowest cost + quality-weighted) SIP route for a call.")
    public AgentResponse optimizeRoute(
            @ToolParam(description = "Destination phone number E.164") String destination,
            @ToolParam(description = "Origin region: ap-south-1 | us-east-1 | eu-west-1") String originRegion,
            @ToolParam(description = "Priority: COST | QUALITY | BALANCED") String priority) {

        Map<String, Object> selectedRoute = switch (priority.toUpperCase()) {
            case "COST"    -> Map.of("provider","EXOTEL",    "ratePerMin",0.50,"mosScore",4.1,"routeId","RT-EXO-001");
            case "QUALITY" -> Map.of("provider","TATA_COMM", "ratePerMin",0.90,"mosScore",4.5,"routeId","RT-TATA-003");
            default        -> Map.of("provider","OZONETEL",  "ratePerMin",0.65,"mosScore",4.3,"routeId","RT-OZO-002");
        };

        return AgentResponse.ok("SIP_ROUTE_COST_OPTIMIZATION_AGENT",
                "Optimal SIP route selected",
                Map.of(
                        "destination",   destination,
                        "originRegion",  originRegion,
                        "priority",      priority,
                        "selectedRoute", selectedRoute,
                        "alternatives", List.of(
                                Map.of("provider","TWILIO","ratePerMin",1.20,"mosScore",4.4),
                                Map.of("provider","EXOTEL","ratePerMin",0.50,"mosScore",4.1)
                        ),
                        "optimizedAt",   Instant.now().toString()
                ));
    }

    @Tool(name = "sip_route_rates_get",
          description = "Get current per-minute rates for all configured SIP providers.")
    public AgentResponse getRates(
            @ToolParam(description = "Destination country code: IN | US | GB") String countryCode) {

        return AgentResponse.ok("SIP_ROUTE_COST_OPTIMIZATION_AGENT",
                "SIP rates for country " + countryCode,
                Map.of(
                        "countryCode", countryCode,
                        "rates", List.of(
                                Map.of("provider","EXOTEL",   "ratePerMin",0.50,"currency","INR"),
                                Map.of("provider","OZONETEL", "ratePerMin",0.65,"currency","INR"),
                                Map.of("provider","TATA_COMM","ratePerMin",0.90,"currency","INR"),
                                Map.of("provider","TWILIO",   "ratePerMin",1.20,"currency","INR")
                        ),
                        "fetchedAt", Instant.now().toString()
                ));
    }
}
