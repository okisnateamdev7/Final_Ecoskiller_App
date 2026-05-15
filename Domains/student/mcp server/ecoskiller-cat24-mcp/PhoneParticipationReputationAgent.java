package com.ecoskiller.mcp.agents;
import java.util.*;
public class PhoneParticipationReputationAgent extends BaseAgent {
    @Override public String toolName()    { return "phone_participation_reputation"; }
    @Override public String description() { return "Compute and update a long-term participation reputation score for each phone session learner."; }
    @Override public Map<String,Object> inputSchema() { return schema("learner_id","string","sessions_completed","number","avg_score","number","disputes_raised","number"); }
    @Override public String execute(Map<String,Object> a) {
        String lid = str(a,"learner_id","l-?");
        int sessions = intVal(a,"sessions_completed",0);
        int avg = intVal(a,"avg_score",0);
        int disputes = intVal(a,"disputes_raised",0);
        int rep = Math.max(0, Math.min(100, avg + sessions - disputes*5));
        String tier = rep>=80?"GOLD":rep>=60?"SILVER":rep>=40?"BRONZE":"STARTER";
        return result("PHONE_PARTICIPATION_REPUTATION_AGENT","UPDATED",
            "learner_id="+lid,"sessions_completed="+sessions,
            "avg_score="+avg,"disputes_raised="+disputes,
            "reputation_score="+rep,"tier="+tier,"timestamp="+new java.util.Date());
    }
}
