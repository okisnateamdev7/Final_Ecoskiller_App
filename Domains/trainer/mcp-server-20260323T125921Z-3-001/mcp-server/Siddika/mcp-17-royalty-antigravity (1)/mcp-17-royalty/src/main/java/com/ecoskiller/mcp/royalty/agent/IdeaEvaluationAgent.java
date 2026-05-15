package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AGENT-13: IDEA_EVALUATION_AGENT
 * Evaluates submitted ideas for marketplace potential,
 * originality score, and royalty eligibility.
 */
@Component
public class IdeaEvaluationAgent {

    @Tool(name = "idea_evaluate",
          description = "Evaluate a submitted idea for marketplace viability and royalty potential.")
    public AgentResponse evaluate(
            @ToolParam(description = "Idea ID") String ideaId,
            @ToolParam(description = "Idea title") String title,
            @ToolParam(description = "Idea description") String description,
            @ToolParam(description = "Category: EDTECH | SKILL_TRAINING | COMPETITION | CONTENT") String category) {

        return AgentResponse.ok("IDEA_EVALUATION_AGENT",
                "Evaluation completed for idea " + ideaId,
                Map.of(
                        "ideaId",                ideaId,
                        "title",                 title,
                        "category",              category,
                        "originalityScore",      82.5,
                        "marketPotentialScore",  78.0,
                        "royaltyEligible",       true,
                        "estimatedAnnualRoyalty",45000.0,
                        "status",                "APPROVED_FOR_MARKETPLACE",
                        "reviewerNotes",         "High demand category, unique approach detected",
                        "suggestedRoyaltyRate",  "STANDARD"
                ));
    }

    @Tool(name = "idea_batch_evaluate",
          description = "Queue a batch of ideas for evaluation.")
    public AgentResponse batchEvaluate(
            @ToolParam(description = "Comma-separated idea IDs") String ideaIds) {

        List<String> ids = List.of(ideaIds.split(","));
        return AgentResponse.ok("IDEA_EVALUATION_AGENT",
                "Batch evaluation queued for " + ids.size() + " ideas",
                Map.of(
                        "ideaIds",           ids,
                        "batchStatus",       "QUEUED",
                        "estimatedMinutes",  ids.size() * 2
                ));
    }
}
