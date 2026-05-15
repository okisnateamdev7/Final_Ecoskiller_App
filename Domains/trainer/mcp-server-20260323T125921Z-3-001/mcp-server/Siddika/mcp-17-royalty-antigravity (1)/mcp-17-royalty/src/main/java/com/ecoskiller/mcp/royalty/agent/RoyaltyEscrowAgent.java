package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * AGENT-05: ROYALTY_ESCROW_AGENT
 * Holds royalty funds in escrow for a defined period.
 * Handles hold, release, and dispute triggers.
 */
@Component
public class RoyaltyEscrowAgent {

    @Tool(name = "escrow_create",
          description = "Place a royalty amount in escrow with a hold duration in days.")
    public AgentResponse createEscrow(
            @ToolParam(description = "Transaction ID") String transactionId,
            @ToolParam(description = "Beneficiary user ID") String beneficiaryId,
            @ToolParam(description = "Amount to hold in escrow (INR)") double amount,
            @ToolParam(description = "Hold duration in days (default 7)") int holdDays) {

        Instant releaseDate = Instant.now().plus(holdDays, ChronoUnit.DAYS);

        return AgentResponse.ok("ROYALTY_ESCROW_AGENT",
                "Escrow created ₹" + amount,
                Map.of(
                        "escrowId",         "ESC-" + System.currentTimeMillis(),
                        "transactionId",    transactionId,
                        "beneficiaryId",    beneficiaryId,
                        "amountHeld",       amount,
                        "holdDays",         holdDays,
                        "releaseDate",      releaseDate.toString(),
                        "escrowStatus",     "ACTIVE",
                        "releaseCondition", "AUTO_AFTER_HOLD_PERIOD"
                ));
    }

    @Tool(name = "escrow_release",
          description = "Release escrow funds to beneficiary wallet after hold period.")
    public AgentResponse releaseEscrow(
            @ToolParam(description = "Escrow ID to release") String escrowId) {

        return AgentResponse.ok("ROYALTY_ESCROW_AGENT",
                "Escrow " + escrowId + " released",
                Map.of(
                        "escrowId",      escrowId,
                        "escrowStatus",  "RELEASED",
                        "releasedAt",    Instant.now().toString(),
                        "walletCredited",true
                ));
    }

    @Tool(name = "escrow_dispute_hold",
          description = "Place a dispute hold on an active escrow pending review.")
    public AgentResponse disputeHold(
            @ToolParam(description = "Escrow ID") String escrowId,
            @ToolParam(description = "Reason for dispute") String reason) {

        return AgentResponse.ok("ROYALTY_ESCROW_AGENT",
                "Dispute hold placed on escrow " + escrowId,
                Map.of(
                        "escrowId",       escrowId,
                        "escrowStatus",   "DISPUTE_HOLD",
                        "disputeReason",  reason,
                        "openedAt",       Instant.now().toString(),
                        "reviewTeam",     "COMPLIANCE_TEAM"
                ));
    }
}
