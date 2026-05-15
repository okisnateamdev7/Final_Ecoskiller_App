package com.ecoskiller.mcp.trust.config;

import com.ecoskiller.mcp.trust.agents.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * MCP-20 Main Router
 *
 * Routes:
 *   GET  /                              → Server info
 *   GET  /health                        → Health check
 *   GET  /agents                        → List all agents
 *   POST /agents/{agentName}/invoke     → Invoke a specific agent
 *   GET  /agents/{agentName}/health     → Agent health
 */
@RestController
@RequestMapping("/")
public class McpRouter {

    private static final Logger log = LoggerFactory.getLogger(McpRouter.class);

    @Autowired private AgentAccessPermissionFirewall   accessPermissionFirewall;
    @Autowired private AgentFailureRecoveryAgent       failureRecoveryAgent;
    @Autowired private ChildProtectionEvidenceAgent    childProtectionEvidenceAgent;
    @Autowired private ConsentAndParentPermissionAgent consentAndParentPermissionAgent;
    @Autowired private CrossPlatformTrustScoreAgent    crossPlatformTrustScoreAgent;
    @Autowired private EvidencePreservationAgent       evidencePreservationAgent;
    @Autowired private IdeaDisputeResolutionAgent      ideaDisputeResolutionAgent;
    @Autowired private IdentityAssuranceAgent          identityAssuranceAgent;
    @Autowired private UserRightsExplanationAgent      userRightsExplanationAgent;
    @Autowired private VendorRiskEvaluationAgent       vendorRiskEvaluationAgent;

    // ─── Routing Map ──────────────────────────────────────
    private Map<String, BaseAgent> agentMap() {
        Map<String, BaseAgent> m = new LinkedHashMap<>();
        m.put("AGENT_ACCESS_PERMISSION_FIREWALL",   accessPermissionFirewall);
        m.put("AGENT_FAILURE_RECOVERY_AGENT",       failureRecoveryAgent);
        m.put("CHILD_PROTECTION_EVIDENCE_AGENT",    childProtectionEvidenceAgent);
        m.put("CONSENT_AND_PARENT_PERMISSION_AGENT",consentAndParentPermissionAgent);
        m.put("CROSS_PLATFORM_TRUST_SCORE_AGENT",   crossPlatformTrustScoreAgent);
        m.put("EVIDENCE_PRESERVATION_AGENT",        evidencePreservationAgent);
        m.put("IDEA_DISPUTE_RESOLUTION_AGENT",      ideaDisputeResolutionAgent);
        m.put("IDENTITY_ASSURANCE_AGENT",           identityAssuranceAgent);
        m.put("USER_RIGHTS_EXPLANATION_AGENT",      userRightsExplanationAgent);
        m.put("VENDOR_RISK_EVALUATION_AGENT",       vendorRiskEvaluationAgent);
        return m;
    }

    // ─── GET / ────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<Map<String, Object>> serverInfo() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("serverId",    "mcp-20-trust");
        info.put("name",        "Trust, Identity & Safeguards");
        info.put("version",     "1.0.0");
        info.put("namespace",   "core");
        info.put("category",    "CAT-20");
        info.put("priority",    "HIGH");
        info.put("agentCount",  10);
        info.put("status",      "RUNNING");
        info.put("agents",      agentMap().keySet());
        info.put("timestamp",   Instant.now().toString());
        return ResponseEntity.ok(info);
    }

    // ─── GET /health ──────────────────────────────────────
    @GetMapping("health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> h = new LinkedHashMap<>();
        h.put("status",    "UP");
        h.put("server",    "mcp-20-trust");
        h.put("agents",    agentMap().size());
        h.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(h);
    }

    // ─── GET /agents ──────────────────────────────────────
    @GetMapping("agents")
    public ResponseEntity<Map<String, Object>> listAgents() {
        List<Map<String, Object>> agentList = new ArrayList<>();
        agentMap().forEach((name, agent) -> {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("name",   name);
            info.put("status", "UP");
            info.put("healthUrl", "/mcp-20-trust/agents/" + name + "/health");
            info.put("invokeUrl", "/mcp-20-trust/agents/" + name + "/invoke");
            agentList.add(info);
        });
        return ResponseEntity.ok(Map.of(
            "server", "mcp-20-trust",
            "count",  agentList.size(),
            "agents", agentList
        ));
    }

    // ─── GET /agents/{name}/health ────────────────────────
    @GetMapping("agents/{agentName}/health")
    public ResponseEntity<Map<String, Object>> agentHealth(@PathVariable String agentName) {
        BaseAgent agent = agentMap().get(agentName.toUpperCase());
        if (agent == null) return agentNotFound(agentName);
        return ResponseEntity.ok(agent.health());
    }

    // ─── POST /agents/{name}/invoke ───────────────────────
    @PostMapping("agents/{agentName}/invoke")
    public ResponseEntity<Map<String, Object>> invokeAgent(
            @PathVariable String agentName,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "X-Tenant-Id", defaultValue = "default") String tenantId,
            @RequestHeader(value = "X-User-Id",   defaultValue = "anonymous") String userId) {

        BaseAgent agent = agentMap().get(agentName.toUpperCase());
        if (agent == null) return agentNotFound(agentName);

        String action = String.valueOf(body.getOrDefault("action", ""));
        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) body.getOrDefault("payload", Map.of());
        String requestId = String.valueOf(body.getOrDefault("requestId", UUID.randomUUID().toString()));

        log.info("[MCP-20] Routing → {} | action={} | tenant={} | user={}", agentName, action, tenantId, userId);

        try {
            Map<String, Object> result = agent.invoke(action, payload, tenantId, userId);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("requestId",  requestId);
            response.put("agentName",  agentName.toUpperCase());
            response.put("status",     "SUCCESS");
            response.put("data",       result);
            response.put("timestamp",  Instant.now().toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[MCP-20] Error in agent {}: {}", agentName, e.getMessage(), e);
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("requestId", requestId);
            error.put("agentName", agentName.toUpperCase());
            error.put("status",    "FAILURE");
            error.put("message",   e.getMessage());
            error.put("timestamp", Instant.now().toString());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ─── Utility ──────────────────────────────────────────
    private ResponseEntity<Map<String, Object>> agentNotFound(String name) {
        return ResponseEntity.status(404).body(Map.of(
            "status",    "NOT_FOUND",
            "message",   "Agent not found: " + name,
            "available", agentMap().keySet()
        ));
    }
}
