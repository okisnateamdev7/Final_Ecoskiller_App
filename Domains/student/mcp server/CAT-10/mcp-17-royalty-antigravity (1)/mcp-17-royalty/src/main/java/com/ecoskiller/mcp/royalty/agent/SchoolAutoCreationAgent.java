package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-02: SCHOOL_AUTO_CREATION_AGENT
 * Auto-creates school/institute record in marketplace when a license is purchased.
 */
@Component
public class SchoolAutoCreationAgent {

    @Tool(name = "school_auto_create",
          description = "Automatically create a school/institute record in marketplace after license purchase.")
    public AgentResponse autoCreate(
            @ToolParam(description = "License ID that triggered this creation") String licenseId,
            @ToolParam(description = "School name") String schoolName,
            @ToolParam(description = "Owner user ID") String ownerId,
            @ToolParam(description = "City") String city,
            @ToolParam(description = "State") String state) {

        String schoolId = "SCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return AgentResponse.ok("SCHOOL_AUTO_CREATION_AGENT",
                "School created from license " + licenseId,
                Map.of(
                        "schoolId",          schoolId,
                        "licenseId",         licenseId,
                        "schoolName",        schoolName,
                        "ownerId",           ownerId,
                        "city",              city,
                        "state",             state,
                        "status",            "ACTIVE",
                        "onboardingStatus",  "PENDING_KYC",
                        "marketplaceVisible",false
                ));
    }

    @Tool(name = "school_marketplace_publish",
          description = "Publish school to Ecoskiller marketplace after KYC approval.")
    public AgentResponse publishToMarketplace(
            @ToolParam(description = "School ID to publish") String schoolId) {

        return AgentResponse.ok("SCHOOL_AUTO_CREATION_AGENT",
                "School " + schoolId + " published to marketplace",
                Map.of(
                        "schoolId",          schoolId,
                        "marketplaceVisible",true,
                        "marketplaceUrl",    "https://marketplace.ecoskiller.com/schools/" + schoolId,
                        "publishedAt",       Instant.now().toString()
                ));
    }
}
