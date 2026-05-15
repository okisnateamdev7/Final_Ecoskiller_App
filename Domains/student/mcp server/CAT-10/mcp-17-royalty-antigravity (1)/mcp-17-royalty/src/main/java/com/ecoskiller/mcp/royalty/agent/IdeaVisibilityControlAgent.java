package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * AGENT-12: IDEA_VISIBILITY_CONTROL_AGENT
 * Controls who can see submitted ideas in the marketplace.
 * Supports: PUBLIC | PRIVATE | INVITED_ONLY
 */
@Component
public class IdeaVisibilityControlAgent {

    @Tool(name = "idea_visibility_set",
          description = "Set visibility of an idea submission: PUBLIC | PRIVATE | INVITED_ONLY.")
    public AgentResponse setVisibility(
            @ToolParam(description = "Idea ID") String ideaId,
            @ToolParam(description = "Visibility: PUBLIC | PRIVATE | INVITED_ONLY") String visibility,
            @ToolParam(description = "Owner user ID") String ownerId) {

        return AgentResponse.ok("IDEA_VISIBILITY_CONTROL_AGENT",
                "Visibility set to " + visibility + " for idea " + ideaId,
                Map.of(
                        "ideaId",     ideaId,
                        "visibility", visibility,
                        "ownerId",    ownerId,
                        "updatedAt",  Instant.now().toString()
                ));
    }

    @Tool(name = "idea_visibility_get",
          description = "Get current visibility setting and view count of an idea.")
    public AgentResponse getVisibility(
            @ToolParam(description = "Idea ID") String ideaId) {

        return AgentResponse.ok("IDEA_VISIBILITY_CONTROL_AGENT",
                "Visibility fetched for idea " + ideaId,
                Map.of(
                        "ideaId",     ideaId,
                        "visibility", "PUBLIC",
                        "viewCount",  128,
                        "likeCount",  34
                ));
    }
}
