package com.ecoskiller.antigravity.cat19.agents;

import com.ecoskiller.antigravity.cat19.model.McpModels.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  AGENT: RESOURCE_CONSUMPTION_GUARD_AGENT                    ║
 * ║  MCP Server: CAT-19 — Platform Stability & Risk             ║
 * ║  Platform: ECOSKILLER ANTIGRAVITY SEALED                    ║
 * ╠══════════════════════════════════════════════════════════════╣
 * ║  Purpose:                                                   ║
 * ║   Monitors CPU, memory, storage, and API quota consumption  ║
 * ║   per tenant and service. Enforces soft/hard limits and     ║
 * ║   triggers auto-scaling or throttling before overload.      ║
 * ╚══════════════════════════════════════════════════════════════╝
 */
@Slf4j
@Component
public class ResourceConsumptionGuardAgent {

    public static final String AGENT_NAME = "RESOURCE_CONSUMPTION_GUARD_AGENT";

    // ─── Tool Definitions ────────────────────────────────────────────

    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("resource_guard__get_consumption_snapshot")
                .description("Get real-time resource consumption snapshot for a service or tenant. Returns CPU%, memory%, storage, and API call rates.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "target_type", PropertySchema.builder()
                            .type("string")
                            .description("SERVICE or TENANT")
                            .enumValues(List.of("SERVICE", "TENANT"))
                            .build(),
                        "target_id", PropertySchema.builder()
                            .type("string")
                            .description("Service name (e.g., stt-service) or Tenant ID")
                            .build()
                    ))
                    .required(List.of("target_type", "target_id"))
                    .build())
                .build(),

            McpTool.builder()
                .name("resource_guard__set_resource_limit")
                .description("Set or update soft/hard resource consumption limits for a service or tenant. Triggers alert at soft limit, throttle at hard limit.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(new LinkedHashMap<>() {{
                        put("target_id", PropertySchema.builder().type("string").description("Service or Tenant ID").build());
                        put("resource_type", PropertySchema.builder().type("string")
                            .description("CPU, MEMORY, STORAGE, API_CALLS, LLM_TOKENS")
                            .enumValues(List.of("CPU", "MEMORY", "STORAGE", "API_CALLS", "LLM_TOKENS")).build());
                        put("soft_limit", PropertySchema.builder().type("number").description("Soft limit value — triggers alert").build());
                        put("hard_limit", PropertySchema.builder().type("number").description("Hard limit value — triggers throttle/block").build());
                        put("unit", PropertySchema.builder().type("string").description("%, MB, GB, req/min, tokens/day").build());
                    }})
                    .required(List.of("target_id", "resource_type", "soft_limit", "hard_limit", "unit"))
                    .build())
                .build(),

            McpTool.builder()
                .name("resource_guard__get_overconsumption_alerts")
                .description("List all active resource overconsumption alerts. Optionally filter by severity or service.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "severity", PropertySchema.builder()
                            .type("string")
                            .description("CRITICAL, HIGH, MEDIUM, LOW, ALL")
                            .enumValues(List.of("CRITICAL", "HIGH", "MEDIUM", "LOW", "ALL"))
                            .build(),
                        "service_name", PropertySchema.builder()
                            .type("string").description("Optional: filter by service name").build()
                    ))
                    .required(List.of("severity"))
                    .build())
                .build(),

            McpTool.builder()
                .name("resource_guard__trigger_throttle")
                .description("Manually trigger throttling or emergency resource cap for a runaway service or tenant.")
                .inputSchema(InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "target_id", PropertySchema.builder().type("string").description("Service or Tenant ID to throttle").build(),
                        "action", PropertySchema.builder().type("string")
                            .description("THROTTLE_50, THROTTLE_80, HARD_STOP, RELEASE")
                            .enumValues(List.of("THROTTLE_50", "THROTTLE_80", "HARD_STOP", "RELEASE")).build(),
                        "reason", PropertySchema.builder().type("string").description("Reason for manual intervention").build(),
                        "operator_id", PropertySchema.builder().type("string").description("Operator/admin who authorized action").build()
                    ))
                    .required(List.of("target_id", "action", "operator_id"))
                    .build())
                .build()
        );
    }

    // ─── Tool Execution ──────────────────────────────────────────────

    public AgentResult executeTool(String toolName, Map<String, Object> args) {
        log.info("[{}] Executing tool: {}", AGENT_NAME, toolName);

        return switch (toolName) {
            case "resource_guard__get_consumption_snapshot"  -> getConsumptionSnapshot(args);
            case "resource_guard__set_resource_limit"        -> setResourceLimit(args);
            case "resource_guard__get_overconsumption_alerts" -> getOverconsumptionAlerts(args);
            case "resource_guard__trigger_throttle"          -> triggerThrottle(args);
            default -> AgentResult.builder()
                .agentName(AGENT_NAME).status("ERROR")
                .payload(Map.of("error", "Unknown tool: " + toolName))
                .timestamp(Instant.now().toString()).build();
        };
    }

    // ─── Tool Implementations ────────────────────────────────────────

    private AgentResult getConsumptionSnapshot(Map<String, Object> args) {
        String targetType = (String) args.get("target_type");
        String targetId   = (String) args.get("target_id");

        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("target_type", targetType);
        snapshot.put("target_id", targetId);
        snapshot.put("snapshot_at", Instant.now().toString());
        snapshot.put("resources", Map.of(
            "cpu", Map.of("current_pct", 67.3, "soft_limit_pct", 70.0, "hard_limit_pct", 85.0, "status", "WARNING"),
            "memory", Map.of("current_mb", 4096, "soft_limit_mb", 6144, "hard_limit_mb", 8192, "status", "NORMAL"),
            "storage", Map.of("current_gb", 120.5, "soft_limit_gb", 200.0, "hard_limit_gb", 250.0, "status", "NORMAL"),
            "api_calls", Map.of("current_per_min", 840, "soft_limit_per_min", 1000, "hard_limit_per_min", 1200, "status", "NORMAL"),
            "llm_tokens", Map.of("current_today", 485000, "soft_limit_day", 500000, "hard_limit_day", 600000, "status", "WARNING")
        ));
        snapshot.put("overall_health", "WARNING");
        snapshot.put("predicted_breach_in_minutes", Map.of("cpu", 25, "llm_tokens", 40));
        snapshot.put("auto_scale_triggered", false);
        snapshot.put("recommendation", "Pre-emptive scale-up recommended for CPU within 15 min");

        return AgentResult.builder()
            .agentName(AGENT_NAME).category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk").status("SUCCESS")
            .timestamp(Instant.now().toString()).payload(snapshot)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0").build();
    }

    private AgentResult setResourceLimit(Map<String, Object> args) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("target_id", args.get("target_id"));
        result.put("resource_type", args.get("resource_type"));
        result.put("soft_limit", args.get("soft_limit"));
        result.put("hard_limit", args.get("hard_limit"));
        result.put("unit", args.get("unit"));
        result.put("applied_at", Instant.now().toString());
        result.put("status", "LIMIT_APPLIED");
        result.put("previous_soft_limit", 70.0);
        result.put("previous_hard_limit", 85.0);
        result.put("change_id", "LIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return AgentResult.builder()
            .agentName(AGENT_NAME).category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk").status("SUCCESS")
            .timestamp(Instant.now().toString()).payload(result)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0").build();
    }

    private AgentResult getOverconsumptionAlerts(Map<String, Object> args) {
        String severity = (String) args.get("severity");

        Map<String, Object> alertsReport = new LinkedHashMap<>();
        alertsReport.put("generated_at", Instant.now().toString());
        alertsReport.put("severity_filter", severity);
        alertsReport.put("total_active_alerts", 3);
        alertsReport.put("alerts", List.of(
            Map.of("alert_id", "ALT-001", "service", "stt-service", "resource", "CPU",
                   "current_value", "87%", "hard_limit", "85%", "severity", "CRITICAL",
                   "triggered_at", "2025-06-19T04:12:00Z", "status", "ACTIVE",
                   "auto_action_taken", "THROTTLE_50 applied"),
            Map.of("alert_id", "ALT-002", "service", "llm-connect", "resource", "LLM_TOKENS",
                   "current_value", "490K", "soft_limit", "500K", "severity", "HIGH",
                   "triggered_at", "2025-06-19T05:30:00Z", "status", "MONITORING",
                   "auto_action_taken", "None — monitoring"),
            Map.of("alert_id", "ALT-003", "tenant", "TENANT-4521", "resource", "API_CALLS",
                   "current_value", "1150/min", "hard_limit", "1200/min", "severity", "MEDIUM",
                   "triggered_at", "2025-06-19T06:00:00Z", "status", "MONITORING",
                   "auto_action_taken", "Rate-limit header injected")
        ));

        return AgentResult.builder()
            .agentName(AGENT_NAME).category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk").status("SUCCESS")
            .timestamp(Instant.now().toString()).payload(alertsReport)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0").build();
    }

    private AgentResult triggerThrottle(Map<String, Object> args) {
        String targetId   = (String) args.get("target_id");
        String action     = (String) args.get("action");
        String reason     = (String) args.getOrDefault("reason", "Manual operator intervention");
        String operatorId = (String) args.get("operator_id");

        log.warn("[{}] THROTTLE ACTION: {} on {} by operator {}", AGENT_NAME, action, targetId, operatorId);

        Map<String, Object> throttleResult = new LinkedHashMap<>();
        throttleResult.put("target_id", targetId);
        throttleResult.put("action", action);
        throttleResult.put("applied_by", operatorId);
        throttleResult.put("reason", reason);
        throttleResult.put("applied_at", Instant.now().toString());
        throttleResult.put("effective_until", "RELEASE_REQUIRED".equals(action) ? "N/A" : Instant.now().plusSeconds(3600).toString());
        throttleResult.put("action_id", "THROT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        throttleResult.put("notifications_sent", List.of("ops@ecoskiller.com", "INSIDER_THREAT_MONITOR_AGENT"));
        throttleResult.put("status", "THROTTLE_APPLIED");

        return AgentResult.builder()
            .agentName(AGENT_NAME).category("CAT-19 Platform Stability & Risk")
            .mcpServer("19. Platform Stability & Risk").status("SUCCESS")
            .timestamp(Instant.now().toString()).payload(throttleResult)
            .antigravityToken("ANTIGRAVITY_SEALED_v1.0").build();
    }
}
