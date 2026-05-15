package com.ecoskiller.mcp.jitsi.agents;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

/**
 * Standard response envelope for all Jitsi MCP agents.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentResult {

    public enum Status { SUCCESS, ERROR, WARNING, DEGRADED }

    private final Status status;
    private final String agent;
    private final String message;
    private final Map<String, Object> data;
    private final String timestamp;

    private AgentResult(Builder b) {
        this.status    = b.status;
        this.agent     = b.agent;
        this.message   = b.message;
        this.data      = b.data;
        this.timestamp = Instant.now().toString();
    }

    public Status getStatus()              { return status;    }
    public String getAgent()               { return agent;     }
    public String getMessage()             { return message;   }
    public Map<String, Object> getData()   { return data;      }
    public String getTimestamp()           { return timestamp; }

    public static Builder builder(String agent) { return new Builder(agent); }

    public static class Builder {
        private final String agent;
        private Status status = Status.SUCCESS;
        private String message;
        private Map<String, Object> data;

        private Builder(String agent) { this.agent = agent; }

        public Builder status(Status s)             { this.status = s;  return this; }
        public Builder message(String m)            { this.message = m; return this; }
        public Builder data(Map<String, Object> d)  { this.data = d;    return this; }

        public AgentResult build() {
            if (agent == null || agent.isBlank()) throw new IllegalStateException("agent name required");
            return new AgentResult(this);
        }
    }
}
