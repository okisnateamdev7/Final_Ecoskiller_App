package com.ecoskiller.mcp.agents;
import java.util.*;
public class AttendanceBehaviorAnalyticsAgent extends BaseAgent {
    @Override public String toolName()    { return "attendance_behavior_analytics"; }
    @Override public String description() { return "Analyse learner attendance patterns and behavioural signals across phone and digital sessions."; }
    @Override public Map<String,Object> inputSchema() { return schema("learner_id","string","sessions_total","number","sessions_attended","number","late_joins","number"); }
    @Override public String execute(Map<String,Object> a) {
        String lid    = str(a,"learner_id","l-?");
        int total    = intVal(a,"sessions_total",1);
        int attended = intVal(a,"sessions_attended",0);
        int late     = intVal(a,"late_joins",0);
        double rate  = total>0?(attended*100.0/total):0;
        String band  = rate>=90?"EXCELLENT":rate>=75?"GOOD":rate>=50?"AT_RISK":"CRITICAL";
        return result("ATTENDANCE_BEHAVIOR_ANALYTICS_AGENT",band,
            "learner_id="+lid,
            String.format("attendance_rate=%.1f%%",rate),
            "sessions_total="+total,"sessions_attended="+attended,
            "late_joins="+late,"risk_band="+band,
            "recommendation="+(rate<75?"Schedule counselling session":"No intervention needed"),
            "timestamp="+new java.util.Date());
    }
}
