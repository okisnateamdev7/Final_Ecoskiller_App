package com.ecoskiller.mcp.agents;
import java.util.*;
public class CrossNodeTimeDriftMonitorAgent extends BaseAgent {
    @Override public String toolName()    { return "cross_node_time_drift_monitor"; }
    @Override public String description() { return "Detect and alert on clock skew between distributed phone processing nodes to prevent event ordering bugs."; }
    @Override public Map<String,Object> inputSchema() { return schema("node_a","string","node_b","string","epoch_a_ms","number","epoch_b_ms","number"); }
    @Override public String execute(Map<String,Object> a) {
        String na    = str(a,"node_a","node-1");
        String nb    = str(a,"node_b","node-2");
        long   ea    = Long.parseLong(str(a,"epoch_a_ms","0"));
        long   eb    = Long.parseLong(str(a,"epoch_b_ms","0"));
        long   skew  = Math.abs(ea-eb);
        String status= skew<=100?"OK":skew<=1000?"WARNING":"CRITICAL";
        return result("CROSS_NODE_TIME_DRIFT_MONITOR_AGENT",status,
            "node_a="+na+" ("+ea+"ms)","node_b="+nb+" ("+eb+"ms)",
            "clock_skew_ms="+skew,
            "ordering_risk="+(skew>100?"true":"false"),
            "action="+(skew>1000?"Halt event processing; trigger NTP resync on both nodes":
                       skew>100?"Alert infra; schedule resync within 5 minutes":"No action"),
            "timestamp="+new java.util.Date());
    }
}
