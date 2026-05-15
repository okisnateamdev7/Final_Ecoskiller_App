package com.ecoskiller.mcp.agents;
import java.util.*;
public class CallCostCalculationAgent extends BaseAgent {
    @Override public String toolName()    { return "call_cost_calculation"; }
    @Override public String description() { return "Calculate billable cost for a phone call based on duration, carrier tier, and tenant plan."; }
    @Override public Map<String,Object> inputSchema() { return schema("session_id","string","duration_seconds","number","carrier_tier","string"); }
    @Override public String execute(Map<String,Object> a) {
        String sid = str(a,"session_id","sess-?");
        int dur = intVal(a,"duration_seconds",60);
        String tier = str(a,"carrier_tier","standard");
        double ratePerMin = "premium".equals(tier)?0.08:"economy".equals(tier)?0.02:0.04;
        double cost = (dur/60.0)*ratePerMin;
        return result("CALL_COST_CALCULATION_AGENT","OK",
            "session_id="+sid,"duration_sec="+dur,"carrier_tier="+tier,
            String.format("rate_per_min=$%.4f",ratePerMin),
            String.format("billable_cost=$%.4f",cost),"timestamp="+new java.util.Date());
    }
}
