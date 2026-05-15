package com.ecoskiller.mcp.webrtc.model;

import org.json.JSONObject;

/**
 * Standardised result envelope returned by every agent.
 */
public class AgentResult {

    public enum Status { SUCCESS, ERROR, PARTIAL }

    private final Status status;
    private final String agent;
    private final JSONObject data;
    private final String message;
    private final long timestampMs;

    private AgentResult(Builder b) {
        this.status      = b.status;
        this.agent       = b.agent;
        this.data        = b.data != null ? b.data : new JSONObject();
        this.message     = b.message != null ? b.message : "";
        this.timestampMs = System.currentTimeMillis();
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("status",       status.name().toLowerCase())
            .put("agent",        agent)
            .put("message",      message)
            .put("data",         data)
            .put("timestamp_ms", timestampMs);
    }

    public static Builder success(String agent) {
        return new Builder(Status.SUCCESS, agent);
    }

    public static Builder error(String agent, String message) {
        return new Builder(Status.ERROR, agent).message(message);
    }

    public static class Builder {
        private final Status status;
        private final String agent;
        private JSONObject data;
        private String message;

        private Builder(Status s, String a) { this.status = s; this.agent = a; }

        public Builder data(JSONObject d)    { this.data = d;    return this; }
        public Builder message(String m)     { this.message = m; return this; }
        public AgentResult build()           { return new AgentResult(this); }
    }
}
