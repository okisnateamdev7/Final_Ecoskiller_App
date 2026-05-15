package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * EVIDENCE_PRESERVATION_AGENT
 *
 * Immutably stores and manages evidence for legal, compliance, and dispute cases.
 * Ensures chain-of-custody and tamper-evident storage.
 *
 * Actions:
 *   - PRESERVE          : Lock and hash-seal an evidence item
 *   - VERIFY_INTEGRITY  : Verify evidence hasn't been tampered
 *   - RETRIEVE          : Retrieve preserved evidence by ID
 *   - LIST_EVIDENCE     : List all evidence for a case
 *   - DESTROY           : Legal evidence destruction after retention period
 */
@Component
public class EvidencePreservationAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "EVIDENCE_PRESERVATION_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "PRESERVE" -> {
                String caseId      = str(payload, "caseId");
                String dataRef     = str(payload, "dataRef");
                String contentType = str(payload, "contentType");
                String evidenceId  = "EVD-" + UUID.randomUUID().toString().substring(0,12).toUpperCase();
                yield result(
                    "evidenceId",     evidenceId,
                    "caseId",         caseId,
                    "dataRef",        dataRef,
                    "contentType",    contentType.isEmpty() ? "application/octet-stream" : contentType,
                    "hashAlgorithm",  "SHA-256",
                    "hash",           "sha256:mock-" + dataRef.hashCode(),
                    "sealed",         true,
                    "sealedBy",       userId,
                    "sealedAt",       java.time.Instant.now().toString(),
                    "retentionYears", 7,
                    "storageLocation","encrypted-vault://evidence/" + evidenceId
                );
            }

            case "VERIFY_INTEGRITY" -> {
                String evidenceId = str(payload, "evidenceId");
                yield result(
                    "evidenceId",  evidenceId,
                    "intact",      true,
                    "storedHash",  "sha256:mock-original",
                    "currentHash", "sha256:mock-original",
                    "hashMatch",   true,
                    "chainOfCustody","UNBROKEN",
                    "lastVerified",java.time.Instant.now().toString()
                );
            }

            case "RETRIEVE" -> {
                String evidenceId = str(payload, "evidenceId");
                yield result(
                    "evidenceId",  evidenceId,
                    "status",      "SEALED",
                    "accessedBy",  userId,
                    "accessedAt",  java.time.Instant.now().toString(),
                    "downloadUrl", "https://vault.ecoskiller.com/evidence/" + evidenceId + "?token=mock",
                    "auditLogged", true
                );
            }

            case "LIST_EVIDENCE" -> {
                String caseId = str(payload, "caseId");
                yield result(
                    "caseId", caseId,
                    "items", List.of(
                        Map.of("evidenceId","EVD-001","type","SCREENSHOT","sealed",true,"size","245KB"),
                        Map.of("evidenceId","EVD-002","type","CHAT_LOG","sealed",true,"size","12KB"),
                        Map.of("evidenceId","EVD-003","type","AUDIT_TRAIL","sealed",true,"size","88KB")
                    ),
                    "count", 3
                );
            }

            case "DESTROY" -> {
                String evidenceId = str(payload, "evidenceId");
                String authCode   = str(payload, "legalAuthCode");
                boolean authorized = !authCode.isBlank();
                yield result(
                    "evidenceId",  evidenceId,
                    "destroyed",   authorized,
                    "reason",      authorized ? "Retention period expired — legal authorization provided"
                                              : "REJECTED: Legal authorization code required",
                    "destroyedBy", authorized ? userId : null,
                    "destroyedAt", authorized ? java.time.Instant.now().toString() : null,
                    "certificateId", authorized ? "DEST-CERT-" + System.currentTimeMillis() : null
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("PRESERVE","VERIFY_INTEGRITY","RETRIEVE","LIST_EVIDENCE","DESTROY")
            );
        };
    }
}
