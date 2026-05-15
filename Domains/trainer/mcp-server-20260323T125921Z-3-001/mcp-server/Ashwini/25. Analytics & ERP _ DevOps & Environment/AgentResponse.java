package com.ecoskiller.analytics.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AgentResponse {
    private final String agentName, status, message;
    private final ObjectNode data;
    private final boolean error;

    private AgentResponse(String a, String s, ObjectNode d, String m, boolean e) {
        agentName = a; status = s; data = d; message = m; error = e;
    }
    public static AgentResponse success(String a, ObjectNode d, String m) { return new AgentResponse(a,"SUCCESS",d,m,false); }
    public static AgentResponse warning(String a, ObjectNode d, String m) { return new AgentResponse(a,"WARNING",d,m,false); }
    public static AgentResponse error(String a, String m)                  { return new AgentResponse(a,"ERROR",null,m,true); }
    public boolean isError() { return error; }
    public String toJson(ObjectMapper mapper) {
        ObjectNode r = mapper.createObjectNode();
        r.put("agent", agentName); r.put("status", status); r.put("message", message);
        if (data != null) r.set("data", data);
        return r.toString();
    }
}
