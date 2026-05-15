package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AGENT-17: BUSINESS_MATCHING_AGENT
 * Matches content creators with franchise partners, investors,
 * and school operators in the Ecoskiller marketplace.
 */
@Component
public class BusinessMatchingAgent {

    @Tool(name = "business_match_find",
          description = "Find business matches for a creator or franchise in the marketplace.")
    public AgentResponse findMatches(
            @ToolParam(description = "User ID seeking matches") String userId,
            @ToolParam(description = "Type: FRANCHISE_PARTNER | INVESTOR | SCHOOL_OPERATOR | CONTENT_CREATOR") String matchType,
            @ToolParam(description = "Preferred region") String region,
            @ToolParam(description = "Skill domain") String skillDomain) {

        return AgentResponse.ok("BUSINESS_MATCHING_AGENT",
                "Business matches found for " + userId,
                Map.of(
                        "userId",      userId,
                        "matchType",   matchType,
                        "region",      region,
                        "skillDomain", skillDomain,
                        "matches", List.of(
                                Map.of("matchId","USR-4421","name","Sharma Edu Franchise", "score",94.2,"city","Pune"),
                                Map.of("matchId","USR-8834","name","Digital Skill Academy","score",89.7,"city","Mumbai"),
                                Map.of("matchId","USR-2290","name","NextGen Learning Pvt", "score",85.1,"city","Nagpur")
                        ),
                        "totalMatches", 3
                ));
    }

    @Tool(name = "business_partnership_initiate",
          description = "Initiate a business partnership request between two parties.")
    public AgentResponse initiatePartnership(
            @ToolParam(description = "Requester user ID") String requesterId,
            @ToolParam(description = "Target user/entity ID") String targetId,
            @ToolParam(description = "Partnership type") String partnershipType) {

        return AgentResponse.ok("BUSINESS_MATCHING_AGENT",
                "Partnership request initiated",
                Map.of(
                        "requestId",       "PART-" + System.currentTimeMillis(),
                        "requesterId",     requesterId,
                        "targetId",        targetId,
                        "partnershipType", partnershipType,
                        "status",          "PENDING_ACCEPTANCE"
                ));
    }
}
