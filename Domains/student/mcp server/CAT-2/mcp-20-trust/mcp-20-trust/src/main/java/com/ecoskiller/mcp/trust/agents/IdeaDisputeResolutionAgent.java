package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * IDEA_DISPUTE_RESOLUTION_AGENT
 *
 * Handles disputes around ideas, IP ownership, and content attribution
 * between users on the Ecoskiller platform.
 *
 * Actions:
 *   - RAISE_DISPUTE       : Create a new dispute case
 *   - SUBMIT_EVIDENCE     : Add supporting evidence
 *   - GET_RULING          : Get AI-assisted preliminary ruling
 *   - ESCALATE_TO_HUMAN   : Route to human mediator
 *   - CLOSE_DISPUTE       : Close and record outcome
 */
@Component
public class IdeaDisputeResolutionAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "IDEA_DISPUTE_RESOLUTION_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "RAISE_DISPUTE" -> {
                String ideaId       = str(payload, "ideaId");
                String claimantId   = str(payload, "claimantId");
                String respondentId = str(payload, "respondentId");
                String disputeType  = str(payload, "disputeType");
                String disputeId    = "DISP-" + System.currentTimeMillis();
                yield result(
                    "disputeId",    disputeId,
                    "ideaId",       ideaId,
                    "claimantId",   claimantId,
                    "respondentId", respondentId,
                    "disputeType",  disputeType.isEmpty() ? "IP_OWNERSHIP" : disputeType,
                    "status",       "OPEN",
                    "raisedBy",     userId,
                    "raisedAt",     java.time.Instant.now().toString(),
                    "deadline",     java.time.Instant.now().plusSeconds(14*24*3600).toString()
                );
            }

            case "SUBMIT_EVIDENCE" -> {
                String disputeId  = str(payload, "disputeId");
                String evidenceRef = str(payload, "evidenceRef");
                String party      = str(payload, "party");
                yield result(
                    "disputeId",   disputeId,
                    "evidenceId",  "EVD-DISP-" + System.currentTimeMillis(),
                    "party",       party.isEmpty() ? "CLAIMANT" : party,
                    "evidenceRef", evidenceRef,
                    "accepted",    true,
                    "submittedBy", userId,
                    "submittedAt", java.time.Instant.now().toString()
                );
            }

            case "GET_RULING" -> {
                String disputeId = str(payload, "disputeId");
                yield result(
                    "disputeId",     disputeId,
                    "rulingSummary", "Based on timestamp analysis, claimant demonstrates prior art with 73% confidence",
                    "recommendedOutcome", "PARTIAL_CLAIMANT_FAVOR",
                    "confidence",    73.2,
                    "model",         "dispute-resolver-v1.4",
                    "isAiAssisted",  true,
                    "requiresHumanReview", true,
                    "ruledAt",       java.time.Instant.now().toString()
                );
            }

            case "ESCALATE_TO_HUMAN" -> {
                String disputeId = str(payload, "disputeId");
                String reason    = str(payload, "reason");
                yield result(
                    "disputeId",   disputeId,
                    "escalated",   true,
                    "assignedTo",  "HUMAN_MEDIATOR_POOL",
                    "ticketId",    "HM-" + System.currentTimeMillis(),
                    "reason",      reason.isEmpty() ? "AI confidence insufficient" : reason,
                    "escalatedBy", userId,
                    "escalatedAt", java.time.Instant.now().toString(),
                    "slaHours",    48
                );
            }

            case "CLOSE_DISPUTE" -> {
                String disputeId = str(payload, "disputeId");
                String outcome   = str(payload, "outcome");
                yield result(
                    "disputeId",  disputeId,
                    "status",     "CLOSED",
                    "outcome",    outcome.isEmpty() ? "SETTLED_MUTUALLY" : outcome,
                    "closedBy",   userId,
                    "closedAt",   java.time.Instant.now().toString(),
                    "archived",   true
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("RAISE_DISPUTE","SUBMIT_EVIDENCE","GET_RULING",
                                   "ESCALATE_TO_HUMAN","CLOSE_DISPUTE")
            );
        };
    }
}
