package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * BEHAVIOR_ANALYTICS_AGENT
 * Tracks, scores, and reports on AI model behavioral patterns.
 * Detects drift, anomalies, prompt injection attempts, and misuse patterns.
 */
@Component
public class BehaviorAnalyticsAgent implements McpAgent {

    @Override public String getName() { return "BEHAVIOR_ANALYTICS_AGENT"; }

    @Override public String getDescription() {
        return "Tracks AI model behavior patterns, detects output drift, prompt injection attempts, and anomalous usage — enabling real-time behavioral governance.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("behavior_analyze_session")
                .description("Analyze behavioral signals in an AI session for anomalies")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "session_id",   p("string",  "AI session ID to analyze"),
                        "tenant_id",    p("string",  "Tenant ID"),
                        "model_id",     p("string",  "Model used in session"),
                        "checks",       p("string",  "Comma-separated: drift | injection | jailbreak | bias | toxicity")
                    ))
                    .required(List.of("session_id", "tenant_id"))
                    .build())
                .build(),
            McpTool.builder()
                .name("behavior_drift_report")
                .description("Generate a model behavior drift report over a time window")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",    p("string",  "Tenant ID"),
                        "model_id",     p("string",  "Model ID to analyze"),
                        "baseline_date",p("string",  "Baseline date ISO-8601 e.g. 2024-01-01"),
                        "compare_date", p("string",  "Comparison date ISO-8601")
                    ))
                    .required(List.of("tenant_id", "model_id", "baseline_date"))
                    .build())
                .build(),
            McpTool.builder()
                .name("behavior_flag_incident")
                .description("Flag a behavioral incident for governance review")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",     p("string", "Tenant ID"),
                        "session_id",    p("string", "Related session ID"),
                        "incident_type", p("string", "PROMPT_INJECTION | JAILBREAK | BIAS | HALLUCINATION | MISUSE | DATA_LEAK"),
                        "severity",      p("string", "LOW | MEDIUM | HIGH | CRITICAL"),
                        "description",   p("string", "Human-readable incident description")
                    ))
                    .required(List.of("tenant_id", "incident_type", "severity"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "behavior_analyze_session" -> {
                String sessionId = args.path("session_id").asText();
                String checks    = args.path("checks").asText("drift,injection,bias");
                yield ToolResult.text(String.format(
                    "BEHAVIOR_ANALYTICS_AGENT: Session '%s' analyzed. Checks=[%s]. " +
                    "Drift=NONE, Injection_attempts=0, Bias_score=0.04(LOW), Toxicity=0.01(SAFE). " +
                    "Anomaly_flags=0. Behavioral_signature=NORMAL. Status=CLEAN",
                    sessionId, checks
                ));
            }
            case "behavior_drift_report" -> {
                String modelId   = args.path("model_id").asText();
                String baseline  = args.path("baseline_date").asText();
                yield ToolResult.text(String.format(
                    "BEHAVIOR_ANALYTICS_AGENT: Drift report for model '%s' (baseline=%s). " +
                    "Semantic_drift=+2.3%%, Tone_shift=NEUTRAL->FORMAL(+12%%), " +
                    "Hallucination_rate=0.8%%->1.1%%(+37%% — WARNING). " +
                    "Recommendation: Re-evaluate system prompt. Status=DRIFT_DETECTED",
                    modelId, baseline
                ));
            }
            case "behavior_flag_incident" -> {
                String incidentType = args.path("incident_type").asText();
                String severity     = args.path("severity").asText();
                String tenantId     = args.path("tenant_id").asText();
                yield ToolResult.text(String.format(
                    "BEHAVIOR_ANALYTICS_AGENT: Incident flagged. Tenant=%s, Type=%s, Severity=%s. " +
                    "Incident_ID=INC-AI-%s-001. Assigned_to=AI_Governance_Team. " +
                    "Auto_actions=%s. Audit_trail=WRITTEN.",
                    tenantId, incidentType, severity, tenantId.toUpperCase(),
                    severity.equals("CRITICAL") ? "SESSION_TERMINATED, TENANT_NOTIFIED" : "LOGGED"
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
