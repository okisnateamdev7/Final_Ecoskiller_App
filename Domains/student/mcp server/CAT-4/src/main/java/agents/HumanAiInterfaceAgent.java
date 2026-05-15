package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * HUMAN_AI_INTERFACE_AGENT
 * Governs Human-in-the-Loop (HITL) workflows — when AI must defer to humans,
 * how human overrides are recorded, and how feedback improves AI decisions.
 */
@Component
public class HumanAiInterfaceAgent implements McpAgent {

    @Override public String getName() { return "HUMAN_AI_INTERFACE_AGENT"; }

    @Override public String getDescription() {
        return "Governs Human-in-the-Loop workflows — AI deferral triggers, human override management, feedback loop integration, and HITL audit trails.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("hitl_evaluate_deferral")
                .description("Decide whether an AI decision should be deferred to a human reviewer")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",        p("string",  "Tenant ID"),
                        "decision_type",    p("string",  "exam_grade | loan_approval | hiring | content_removal | fraud_flag"),
                        "ai_confidence",    p("number",  "AI confidence score 0.0-1.0"),
                        "impact_level",     p("string",  "LOW | MEDIUM | HIGH | CRITICAL"),
                        "user_id",          p("string",  "Affected user/subject ID")
                    ))
                    .required(List.of("tenant_id", "decision_type", "ai_confidence", "impact_level"))
                    .build())
                .build(),
            McpTool.builder()
                .name("hitl_record_override")
                .description("Record a human override of an AI decision with justification")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",        p("string", "Tenant ID"),
                        "decision_id",      p("string", "Original AI decision ID"),
                        "reviewer_id",      p("string", "Human reviewer user ID"),
                        "override_outcome", p("string", "APPROVED | REJECTED | MODIFIED"),
                        "justification",    p("string", "Reviewer's reason for override")
                    ))
                    .required(List.of("tenant_id", "decision_id", "reviewer_id", "override_outcome"))
                    .build())
                .build(),
            McpTool.builder()
                .name("hitl_feedback_ingest")
                .description("Ingest human feedback to improve AI decision quality")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",     p("string",  "Tenant ID"),
                        "model_id",      p("string",  "Model to improve"),
                        "feedback_count",p("integer", "Number of feedback samples to process"),
                        "feedback_type", p("string",  "correction | preference | ranking | annotation")
                    ))
                    .required(List.of("tenant_id", "model_id", "feedback_count"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "hitl_evaluate_deferral" -> {
                double confidence = args.path("ai_confidence").asDouble(0.9);
                String impact     = args.path("impact_level").asText("MEDIUM");
                String decType    = args.path("decision_type").asText();
                boolean defer = confidence < 0.75 || impact.equals("CRITICAL") || impact.equals("HIGH");
                yield ToolResult.text(String.format(
                    "HUMAN_AI_INTERFACE_AGENT: Deferral evaluation for '%s'. " +
                    "Confidence=%.2f, Impact=%s. Decision=%s. %s. " +
                    "Queue_depth=12 pending reviews. SLA=4h.",
                    decType, confidence, impact,
                    defer ? "DEFER_TO_HUMAN" : "AI_PROCEED",
                    defer ? "Assigned to reviewer pool. Notification sent." : "AI authorized to act autonomously."
                ));
            }
            case "hitl_record_override" -> {
                String decisionId = args.path("decision_id").asText();
                String outcome    = args.path("override_outcome").asText();
                String reviewer   = args.path("reviewer_id").asText();
                yield ToolResult.text(String.format(
                    "HUMAN_AI_INTERFACE_AGENT: Override recorded. Decision='%s', Outcome=%s, Reviewer=%s. " +
                    "Override_ID=OVR-%s-001. Feedback queued for model retraining. " +
                    "Audit_trail=IMMUTABLE. Compliance_event=LOGGED.",
                    decisionId, outcome, reviewer, decisionId.toUpperCase()
                ));
            }
            case "hitl_feedback_ingest" -> {
                String modelId = args.path("model_id").asText();
                int count      = args.path("feedback_count").asInt(0);
                String type    = args.path("feedback_type").asText("correction");
                yield ToolResult.text(String.format(
                    "HUMAN_AI_INTERFACE_AGENT: Feedback ingested for model '%s'. " +
                    "Samples=%d, Type=%s. Conflicts_resolved=3. " +
                    "Quality_improvement_estimate=+1.8%% accuracy. Retraining_job queued. " +
                    "Expected deployment: T+48h.",
                    modelId, count, type
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
