package com.ecoskiller.mcp.scale.agent;

import com.ecoskiller.mcp.scale.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-07: ROYALTY_DISPUTE_RESOLUTION_AGENT
 * Handles royalty payment disputes raised by creators, franchise partners, or schools.
 * Manages dispute intake, evidence collection, adjudication, and resolution.
 */
@Component
public class RoyaltyDisputeResolutionAgent {

    @Tool(name = "dispute_raise",
          description = "Raise a royalty dispute for a specific transaction or distribution.")
    public AgentResponse raiseDispute(
            @ToolParam(description = "Claimant user ID") String claimantId,
            @ToolParam(description = "Transaction or distribution ID under dispute") String referenceId,
            @ToolParam(description = "Dispute type: UNDERPAYMENT | NON_PAYMENT | WRONG_RATE | CALCULATION_ERROR") String disputeType,
            @ToolParam(description = "Amount claimed in INR") double claimedAmount,
            @ToolParam(description = "Description of the dispute") String description) {

        String disputeId = "DISP-" + System.currentTimeMillis();

        return AgentResponse.ok("ROYALTY_DISPUTE_RESOLUTION_AGENT",
                "Dispute raised: " + disputeId,
                Map.of(
                        "disputeId",     disputeId,
                        "claimantId",    claimantId,
                        "referenceId",   referenceId,
                        "disputeType",   disputeType,
                        "claimedAmount", claimedAmount,
                        "description",   description,
                        "status",        "OPEN",
                        "slaDeadline",   Instant.now().plusSeconds(7 * 24 * 3600).toString(),
                        "raisedAt",      Instant.now().toString()
                ));
    }

    @Tool(name = "dispute_adjudicate",
          description = "Adjudicate an open dispute after evidence review and issue a resolution ruling.")
    public AgentResponse adjudicateDispute(
            @ToolParam(description = "Dispute ID") String disputeId,
            @ToolParam(description = "Ruling: UPHELD | PARTIALLY_UPHELD | REJECTED") String ruling,
            @ToolParam(description = "Awarded amount in INR (0 if rejected)") double awardedAmount,
            @ToolParam(description = "Resolution notes from adjudicator") String notes) {

        return AgentResponse.ok("ROYALTY_DISPUTE_RESOLUTION_AGENT",
                "Dispute " + disputeId + " adjudicated: " + ruling,
                Map.of(
                        "disputeId",     disputeId,
                        "ruling",        ruling,
                        "awardedAmount", awardedAmount,
                        "notes",         notes,
                        "status",        "RESOLVED",
                        "nextStep",      awardedAmount > 0 ? "CREDIT_TO_WALLET" : "CLOSE_DISPUTE",
                        "resolvedAt",    Instant.now().toString()
                ));
    }

    @Tool(name = "dispute_list",
          description = "List disputes for a user or tenant with status filtering.")
    public AgentResponse listDisputes(
            @ToolParam(description = "User or tenant ID") String entityId,
            @ToolParam(description = "Status filter: OPEN | RESOLVED | ALL") String statusFilter) {

        return AgentResponse.ok("ROYALTY_DISPUTE_RESOLUTION_AGENT",
                "Disputes fetched for " + entityId,
                Map.of(
                        "entityId",     entityId,
                        "statusFilter", statusFilter,
                        "disputes", List.of(
                                Map.of("disputeId","DISP-1001","type","UNDERPAYMENT","claimedAmount",5000.0,"status","OPEN",     "raisedAt","2025-05-10T10:00:00Z"),
                                Map.of("disputeId","DISP-1002","type","WRONG_RATE",  "claimedAmount",2200.0,"status","RESOLVED", "raisedAt","2025-04-22T08:00:00Z")
                        ),
                        "total", 2
                ));
    }
}
