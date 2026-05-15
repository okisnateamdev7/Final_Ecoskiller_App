package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-16: COMPETITION_ENGINE_AGENT
 * Manages competitions: entry fees, prize pool calculation,
 * royalty splits for organizers and platform.
 */
@Component
public class CompetitionEngineAgent {

    @Tool(name = "competition_create",
          description = "Create a competition with entry fee and prize pool configuration.")
    public AgentResponse createCompetition(
            @ToolParam(description = "Competition name") String name,
            @ToolParam(description = "Skill category") String skillCategory,
            @ToolParam(description = "Entry fee per participant in INR") double entryFee,
            @ToolParam(description = "Max participants") int maxParticipants,
            @ToolParam(description = "Creator/organizer user ID") String creatorId) {

        String compId  = "COMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double total   = entryFee * maxParticipants;

        return AgentResponse.ok("COMPETITION_ENGINE_AGENT",
                "Competition '" + name + "' created",
                Map.of(
                        "competitionId",     compId,
                        "name",              name,
                        "skillCategory",     skillCategory,
                        "entryFee",          entryFee,
                        "maxParticipants",   maxParticipants,
                        "estimatedPrizePool",total * 0.70,
                        "platformFee",       total * 0.20,
                        "creatorRoyalty",    total * 0.10,
                        "status",            "OPEN_FOR_REGISTRATION"
                ));
    }

    @Tool(name = "competition_prize_distribute",
          description = "Distribute prizes to winners and royalties to organizers after competition ends.")
    public AgentResponse distributePrizes(
            @ToolParam(description = "Competition ID") String competitionId,
            @ToolParam(description = "Total collected entry fees") double totalCollected) {

        return AgentResponse.ok("COMPETITION_ENGINE_AGENT",
                "Prize distribution completed for " + competitionId,
                Map.of(
                        "competitionId", competitionId,
                        "totalCollected",totalCollected,
                        "prizeDistribution", List.of(
                                Map.of("rank",1,"prize",totalCollected * 0.35),
                                Map.of("rank",2,"prize",totalCollected * 0.20),
                                Map.of("rank",3,"prize",totalCollected * 0.15)
                        ),
                        "creatorRoyalty",totalCollected * 0.10,
                        "platformFee",   totalCollected * 0.20,
                        "status",        "DISTRIBUTED"
                ));
    }
}
