package com.ecoskiller.mcp.agents;
import java.util.*;
public class HighUsageAlertAgent extends BaseAgent {
    @Override public String toolName()    { return "high_usage_alert"; }
    @Override public String description() { return "Detect tenants exceeding 80% of their quota threshold and trigger proactive alerts."; }
    @Override public Map<String,Object> inputSchema() { return schema("tenant_id","string","usage_percent","number"); }
    @Override public String execute(Map<String,Object> a) {
        String tid = str(a,"tenant_id","t-?");
        int pct = intVal(a,"usage_percent",0);
        String level = pct>=100?"CRITICAL":pct>=90?"HIGH":pct>=80?"WARNING":"OK";
        return result("HIGH_USAGE_ALERT_AGENT",level,
            "tenant_id="+tid,"usage_percent="+pct+"%",
            "alert_level="+level,
            "action="+(pct>=80?"Notification queued for tenant admin":"No action"),
            "timestamp="+new java.util.Date());
    }
}
