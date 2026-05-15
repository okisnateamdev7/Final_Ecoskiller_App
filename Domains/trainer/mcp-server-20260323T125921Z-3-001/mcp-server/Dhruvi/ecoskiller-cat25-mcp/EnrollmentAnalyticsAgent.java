package com.ecoskiller.mcp.agents;
import java.util.*;
public class EnrollmentAnalyticsAgent extends BaseAgent {
    @Override public String toolName()    { return "enrollment_analytics"; }
    @Override public String description() { return "Compute enrollment trends, cohort sizes, and drop-off rates for institutional ERP reporting."; }
    @Override public Map<String,Object> inputSchema() { return schema("institution_id","string","cohort","string","enrolled","number","active","number","completed","number"); }
    @Override public String execute(Map<String,Object> a) {
        String iid    = str(a,"institution_id","inst-?");
        String cohort = str(a,"cohort","2025-Q1");
        int enrolled  = intVal(a,"enrolled",0);
        int active    = intVal(a,"active",0);
        int completed = intVal(a,"completed",0);
        int dropped   = Math.max(0,enrolled-active-completed);
        double compRate= enrolled>0?(completed*100.0/enrolled):0;
        double dropRate= enrolled>0?(dropped*100.0/enrolled):0;
        return result("ENROLLMENT_ANALYTICS_AGENT","OK",
            "institution_id="+iid,"cohort="+cohort,
            "enrolled="+enrolled,"active="+active,
            "completed="+completed,"dropped="+dropped,
            String.format("completion_rate=%.1f%%",compRate),
            String.format("drop_rate=%.1f%%",dropRate),
            "timestamp="+new java.util.Date());
    }
}
