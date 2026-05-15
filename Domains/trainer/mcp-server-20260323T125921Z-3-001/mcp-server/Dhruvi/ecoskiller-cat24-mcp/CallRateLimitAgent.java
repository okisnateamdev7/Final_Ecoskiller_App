package com.ecoskiller.mcp.agents;
import java.util.*;
public class CallRateLimitAgent extends BaseAgent {
    @Override public String toolName()    { return "call_rate_limit"; }
    @Override public String description() { return "Enforce per-tenant call-rate limits and block excess calls during quota windows."; }
    @Override public Map<String,Object> inputSchema() { return schema("tenant_id","string","calls_in_window","number","limit","number"); }
    @Override public String execute(Map<String,Object> a) {
        String tid = str(a,"tenant_id","t-?");
        int calls = intVal(a,"calls_in_window",0);
        int limit = intVal(a,"limit",100);
        boolean exceeded = calls >= limit;
        return result("CALL_RATE_LIMIT_AGENT", exceeded?"LIMIT_EXCEEDED":"OK",
            "tenant_id="+tid,"calls_in_window="+calls,"limit="+limit,
            "action="+(exceeded?"Excess calls blocked; 429 returned":"Calls permitted"),
            "timestamp="+new java.util.Date());
    }
}
