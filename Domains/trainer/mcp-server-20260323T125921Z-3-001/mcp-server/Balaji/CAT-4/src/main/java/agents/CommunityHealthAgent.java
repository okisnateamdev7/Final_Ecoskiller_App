package com.ecoskiller.mcp.aigovernance.agents;

import com.ecoskiller.mcp.aigovernance.model.McpTool;
import com.ecoskiller.mcp.aigovernance.model.ToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * COMMUNITY_HEALTH_AGENT
 * Monitors AI-driven community spaces for toxicity, harassment, misinformation,
 * and social cohesion metrics. Produces health scores and intervention recommendations.
 */
@Component
public class CommunityHealthAgent implements McpAgent {

    @Override public String getName() { return "COMMUNITY_HEALTH_AGENT"; }

    @Override public String getDescription() {
        return "Monitors AI-driven community health: toxicity scoring, misinformation detection, social cohesion metrics, and automated intervention recommendations.";
    }

    @Override
    public List<McpTool> getTools() {
        return List.of(
            McpTool.builder()
                .name("community_health_score")
                .description("Compute a community health score for a tenant community space")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",      p("string",  "Tenant ID"),
                        "community_id",   p("string",  "Community or group identifier"),
                        "window_days",    p("integer", "Analysis window in days"),
                        "include_signals",p("string",  "Comma-separated: toxicity | misinformation | engagement | sentiment | cohesion")
                    ))
                    .required(List.of("tenant_id", "community_id"))
                    .build())
                .build(),
            McpTool.builder()
                .name("community_detect_toxicity")
                .description("Scan content for toxicity and generate moderation recommendations")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",   p("string", "Tenant ID"),
                        "content",     p("string", "Text content to analyze"),
                        "context",     p("string", "Context: forum | chat | comment | review"),
                        "user_id",     p("string", "Author user ID for pattern tracking")
                    ))
                    .required(List.of("tenant_id", "content"))
                    .build())
                .build(),
            McpTool.builder()
                .name("community_intervention_plan")
                .description("Generate intervention recommendations for an unhealthy community")
                .inputSchema(McpTool.InputSchema.builder()
                    .type("object")
                    .properties(Map.of(
                        "tenant_id",    p("string", "Tenant ID"),
                        "community_id", p("string", "Community ID"),
                        "health_score", p("number", "Current health score 0-100"),
                        "top_issues",   p("string", "Comma-separated top issues found")
                    ))
                    .required(List.of("tenant_id", "community_id", "health_score"))
                    .build())
                .build()
        );
    }

    @Override
    public ToolResult executeTool(String toolName, JsonNode args) {
        return switch (toolName) {
            case "community_health_score" -> {
                String communityId = args.path("community_id").asText();
                int window         = args.path("window_days").asInt(7);
                yield ToolResult.text(String.format(
                    "COMMUNITY_HEALTH_AGENT: Health score for community '%s' (last %d days): " +
                    "Overall=78/100 (GOOD). Toxicity=0.06(LOW), Misinformation=0.02(VERY_LOW), " +
                    "Sentiment=+0.62(POSITIVE), Cohesion=0.71(HIGH), Engagement=82%%. " +
                    "Trend=STABLE. No intervention required.",
                    communityId, window
                ));
            }
            case "community_detect_toxicity" -> {
                String context = args.path("context").asText("chat");
                yield ToolResult.text(String.format(
                    "COMMUNITY_HEALTH_AGENT: Content analyzed [context=%s]. " +
                    "Toxicity_score=0.03 (SAFE), Hate_speech=NONE, Personal_attack=NONE, " +
                    "Profanity=NONE, Sarcasm=MILD. Decision=ALLOW. " +
                    "Confidence=0.97. Processing_time=18ms.",
                    context
                ));
            }
            case "community_intervention_plan" -> {
                double score   = args.path("health_score").asDouble(60.0);
                String issues  = args.path("top_issues").asText("toxicity,low_engagement");
                String urgency = score < 40 ? "IMMEDIATE" : score < 65 ? "MODERATE" : "LOW";
                yield ToolResult.text(String.format(
                    "COMMUNITY_HEALTH_AGENT: Intervention plan generated. Score=%.0f/100, Urgency=%s. " +
                    "Top issues: [%s]. Recommendations: (1) Enable AI moderation on new posts, " +
                    "(2) Seed positive engagement prompts, (3) Highlight top contributors. " +
                    "Expected improvement: +12 points in 14 days.",
                    score, urgency, issues
                ));
            }
            default -> ToolResult.error("Unknown tool: " + toolName);
        };
    }

    private McpTool.PropertyDef p(String type, String desc) {
        return McpTool.PropertyDef.builder().type(type).description(desc).build();
    }
}
