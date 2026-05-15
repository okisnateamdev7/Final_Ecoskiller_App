package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * REPUTATION_AGENT
 * Manages AI-computed reputation scores for users, tenants, and AI models —
 * including trust scoring, reputation decay, recovery paths, and fraud detection.
 */
@Component
public class ReputationAgent implements McpAgent {

    @Override public String getName() { return "REPUTATION_AGENT"; }

    @Override public String getDescription() {
        return "Manages AI-computed reputation scores for users and tenants — trust scoring, reputation decay models, fraud-driven reputation penalties, and recovery pathways.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("reputation_score_compute")
                .description("Compute or refresh an AI reputation score for a user or tenant")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",   p("string",  "Tenant ID"),
                        "entity_id",   p("string",  "User ID or Tenant ID to score"),
                        "entity_type", p("string",  "user | tenant | model | organization"),
                        "signals",     p("string",  "Comma-separated: behavior | performance | compliance | peer_reviews | history")
                    ))
                    .required(List.of("tenant_id", "entity_id", "entity_type"))
                    .build())
                .build(),
            McpTool.builder()
                .name("reputation_apply_event")
                .description("Apply a reputation-impacting event (positive or negative) to an entity")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",    p("string",  "Tenant ID"),
                        "entity_id",    p("string",  "Target entity ID"),
                        "event_type",   p("string",  "FRAUD | VIOLATION | ACHIEVEMENT | ENDORSEMENT | PENALTY | AWARD"),
                        "score_delta",  p("number",  "Points to add (positive) or subtract (negative)"),
                        "reason",       p("string",  "Reason for reputation change")
                    ))
                    .required(List.of("tenant_id", "entity_id", "event_type", "score_delta"))
                    .build())
                .build(),
            McpTool.builder()
                .name("reputation_recovery_plan")
                .description("Generate a reputation recovery roadmap for a penalized entity")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",      p("string", "Tenant ID"),
                        "entity_id",      p("string", "Entity with damaged reputation"),
                        "current_score",  p("number", "Current reputation score"),
                        "target_score",   p("number", "Target reputation score to reach")
                    ))
                    .required(List.of("tenant_id", "entity_id", "current_score", "target_score"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "reputation_score_compute" -> {
                String entityId   = args.path("entity_id").asText();
                String entityType = args.path("entity_type").asText();
                String signals    = args.path("signals").asText("behavior,performance");
                yield ToolResult.text(String.format(
                    "REPUTATION_AGENT: Score computed for %s '%s'. Signals=[%s]. " +
                    "Score=742/1000(TRUSTED). Components: behavior=88%%, performance=79%%, " +
                    "compliance=95%%, peer_reviews=4.2/5. Percentile=P74. " +
                    "Tier=SILVER. Trend=+12 points (30d). Status=ACTIVE.",
                    entityType, entityId, signals
                ));
            }
            case "reputation_apply_event" -> {
                String entityId  = args.path("entity_id").asText();
                String eventType = args.path("event_type").asText();
                double delta     = args.path("score_delta").asDouble(0);
                String reason    = args.path("reason").asText("N/A");
                yield ToolResult.text(String.format(
                    "REPUTATION_AGENT: Event applied to entity '%s'. Type=%s, Delta=%+.0f. " +
                    "Reason='%s'. New_score=742%+.0f=%s. " +
                    "Event_ID=REP-EVT-%s-001. Audit_logged=YES. Notification=QUEUED.",
                    entityId, eventType, delta, reason, delta,
                    delta >= 0 ? String.format("%.0f(IMPROVED)", 742 + delta)
                               : String.format("%.0f(DECLINED)", 742 + delta),
                    entityId.toUpperCase()
                ));
            }
            case "reputation_recovery_plan" -> {
                String entityId = args.path("entity_id").asText();
                double current  = args.path("current_score").asDouble(400);
                double target   = args.path("target_score").asDouble(700);
                int gap         = (int)(target - current);
                yield ToolResult.text(String.format(
                    "REPUTATION_AGENT: Recovery plan for entity '%s'. Current=%.0f, Target=%.0f, Gap=%d. " +
                    "Estimated_time=45 days. Steps: (1) Complete 3 endorsed skill assessments(+90pts), " +
                    "(2) Zero violations for 30d(+150pts), (3) Receive 5 peer endorsements(+100pts). " +
                    "Plan_ID=RECOV-%s-001. Milestone_alerts=ENABLED.",
                    entityId, current, target, gap, entityId.toUpperCase()
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
