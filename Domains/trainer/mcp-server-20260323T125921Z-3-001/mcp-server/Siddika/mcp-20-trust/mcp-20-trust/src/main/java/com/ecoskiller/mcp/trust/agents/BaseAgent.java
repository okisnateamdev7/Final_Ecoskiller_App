package com.ecoskiller.mcp.trust.agents;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * Base class for all CAT-20 Trust agents.
 * Every agent exposes: POST /invoke and GET /health
 */
public abstract class BaseAgent {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract String agentName();

    /** Main invocation — subclasses implement their business logic here */
    public abstract Map<String, Object> invoke(String action, Map<String, Object> payload,
                                               String tenantId, String userId);

    /** Standard health check response */
    public Map<String, Object> health() {
        Map<String, Object> h = new LinkedHashMap<>();
        h.put("agent",  agentName());
        h.put("status", "UP");
        h.put("server", "mcp-20-trust");
        h.put("ts",     Instant.now().toString());
        return h;
    }

    /** Utility: build a result map quickly */
    protected Map<String, Object> result(Object... kvPairs) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kvPairs.length; i += 2) {
            m.put(String.valueOf(kvPairs[i]), kvPairs[i + 1]);
        }
        return m;
    }

    protected String str(Map<String, Object> payload, String key) {
        return payload == null ? null : String.valueOf(payload.getOrDefault(key, ""));
    }

    protected Object get(Map<String, Object> payload, String key) {
        return payload == null ? null : payload.get(key);
    }
}
