package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-15: CONTRACT_LIFECYCLE_AGENT
 * Manages full contract lifecycle: Draft → eSign → Active → Renew / Terminate
 */
@Component
public class ContractLifecycleAgent {

    @Tool(name = "contract_create",
          description = "Create a new royalty contract between creator and platform.")
    public AgentResponse createContract(
            @ToolParam(description = "Creator user ID") String creatorId,
            @ToolParam(description = "Contract type: CONTENT_ROYALTY | FRANCHISE | LICENSE") String contractType,
            @ToolParam(description = "Contract duration in days") int durationDays,
            @ToolParam(description = "Agreed royalty rate as decimal e.g. 0.55") double royaltyRate) {

        String contractId = "CON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return AgentResponse.ok("CONTRACT_LIFECYCLE_AGENT",
                contractType + " contract created for " + creatorId,
                Map.of(
                        "contractId",    contractId,
                        "creatorId",     creatorId,
                        "contractType",  contractType,
                        "royaltyRate",   royaltyRate,
                        "status",        "DRAFT",
                        "createdAt",     Instant.now().toString(),
                        "expiresAt",     Instant.now().plus(durationDays, ChronoUnit.DAYS).toString(),
                        "nextStep",      "AWAITING_ESIGN"
                ));
    }

    @Tool(name = "contract_sign",
          description = "Mark a contract as signed after e-signature is received.")
    public AgentResponse signContract(
            @ToolParam(description = "Contract ID") String contractId,
            @ToolParam(description = "Signatory user ID") String signatoryId,
            @ToolParam(description = "eSign reference ID from DigiLocker/eSign service") String eSignRef) {

        return AgentResponse.ok("CONTRACT_LIFECYCLE_AGENT",
                "Contract " + contractId + " signed",
                Map.of(
                        "contractId",   contractId,
                        "signatoryId",  signatoryId,
                        "eSignRef",     eSignRef,
                        "status",       "ACTIVE",
                        "signedAt",     Instant.now().toString()
                ));
    }

    @Tool(name = "contract_terminate",
          description = "Terminate a contract with a given reason.")
    public AgentResponse terminateContract(
            @ToolParam(description = "Contract ID") String contractId,
            @ToolParam(description = "Termination reason") String reason) {

        return AgentResponse.ok("CONTRACT_LIFECYCLE_AGENT",
                "Contract " + contractId + " terminated",
                Map.of(
                        "contractId",   contractId,
                        "status",       "TERMINATED",
                        "reason",       reason,
                        "terminatedAt", Instant.now().toString()
                ));
    }
}
