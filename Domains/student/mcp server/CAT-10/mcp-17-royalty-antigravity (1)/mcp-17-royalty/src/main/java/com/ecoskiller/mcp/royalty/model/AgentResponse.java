package com.ecoskiller.mcp.royalty.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

/**
 * Standard response wrapper returned by every agent tool.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentResponse {

    public String agentName;
    public String status;
    public String message;
    public Object data;
    public String timestamp = Instant.now().toString();

    public AgentResponse(String agentName, String status, String message, Object data) {
        this.agentName = agentName;
        this.status    = status;
        this.message   = message;
        this.data      = data;
    }

    public static AgentResponse ok(String agent, String msg, Object data) {
        return new AgentResponse(agent, "SUCCESS", msg, data);
    }

    public static AgentResponse fail(String agent, String msg) {
        return new AgentResponse(agent, "FAILURE", msg, null);
    }

    public static AgentResponse pending(String agent, String msg, Object data) {
        return new AgentResponse(agent, "PENDING", msg, data);
    }
}
