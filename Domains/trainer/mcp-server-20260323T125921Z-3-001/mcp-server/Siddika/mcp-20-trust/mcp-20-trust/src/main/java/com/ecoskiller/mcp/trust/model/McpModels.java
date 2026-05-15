package com.ecoskiller.mcp.trust.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

// ─────────────────────────────────────────────
//  MCP Agent Request
// ─────────────────────────────────────────────
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class McpAgentRequest {
    public String requestId = UUID.randomUUID().toString();
    public String tenantId;
    public String userId;
    public String agentName;
    public String action;
    public Map<String, Object> payload;
    public String timestamp = Instant.now().toString();

    public McpAgentRequest() {}
}

// ─────────────────────────────────────────────
//  MCP Agent Response
// ─────────────────────────────────────────────
@JsonInclude(JsonInclude.Include.NON_NULL)
class McpAgentResponse {
    public String requestId;
    public String agentName;
    public String status;       // SUCCESS | FAILURE | PENDING
    public String message;
    public Map<String, Object> data;
    public String timestamp = Instant.now().toString();
    public String errorCode;

    public McpAgentResponse() {}

    public static McpAgentResponse success(String requestId, String agentName,
                                           String message, Map<String, Object> data) {
        McpAgentResponse r = new McpAgentResponse();
        r.requestId = requestId;
        r.agentName = agentName;
        r.status = "SUCCESS";
        r.message = message;
        r.data = data;
        return r;
    }

    public static McpAgentResponse failure(String requestId, String agentName,
                                           String message, String errorCode) {
        McpAgentResponse r = new McpAgentResponse();
        r.requestId = requestId;
        r.agentName = agentName;
        r.status = "FAILURE";
        r.message = message;
        r.errorCode = errorCode;
        return r;
    }
}

// ─────────────────────────────────────────────
//  MCP Server Info
// ─────────────────────────────────────────────
@JsonInclude(JsonInclude.Include.NON_NULL)
class McpServerInfo {
    public String serverId = "mcp-20-trust";
    public String name = "Trust, Identity & Safeguards";
    public String version = "1.0.0";
    public String namespace = "core";
    public String category = "CAT-20";
    public String priority = "HIGH";
    public int agentCount = 10;
    public String status = "RUNNING";
    public String startedAt = Instant.now().toString();
    public java.util.List<String> agents = java.util.List.of(
        "AGENT_ACCESS_PERMISSION_FIREWALL",
        "AGENT_FAILURE_RECOVERY_AGENT",
        "CHILD_PROTECTION_EVIDENCE_AGENT",
        "CONSENT_AND_PARENT_PERMISSION_AGENT",
        "CROSS_PLATFORM_TRUST_SCORE_AGENT",
        "EVIDENCE_PRESERVATION_AGENT",
        "IDEA_DISPUTE_RESOLUTION_AGENT",
        "IDENTITY_ASSURANCE_AGENT",
        "USER_RIGHTS_EXPLANATION_AGENT",
        "VENDOR_RISK_EVALUATION_AGENT"
    );
}

// ─────────────────────────────────────────────
//  Public Exports (package-level visible)
// ─────────────────────────────────────────────
public class McpModels {
    public static McpAgentRequest  newRequest()  { return new McpAgentRequest();  }
    public static McpAgentResponse success(String rId, String agent,
                                           String msg, Map<String,Object> data) {
        return McpAgentResponse.success(rId, agent, msg, data);
    }
    public static McpAgentResponse failure(String rId, String agent,
                                           String msg, String code) {
        return McpAgentResponse.failure(rId, agent, msg, code);
    }
    public static McpServerInfo serverInfo() { return new McpServerInfo(); }

    // Re-export types so controllers can use them
    public static Class<McpAgentRequest>  REQUEST_TYPE  = McpAgentRequest.class;
    public static Class<McpAgentResponse> RESPONSE_TYPE = McpAgentResponse.class;
    public static Class<McpServerInfo>    INFO_TYPE      = McpServerInfo.class;
}
