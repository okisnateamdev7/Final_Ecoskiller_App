package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * CHILD_PROTECTION_EVIDENCE_AGENT
 *
 * Manages evidence collection and reporting for child safety incidents.
 * Ensures compliance with child protection regulations.
 * Works closely with CONSENT_AND_PARENT_PERMISSION_AGENT.
 *
 * Actions:
 *   - FLAG_INCIDENT        : Flag a child safety concern
 *   - COLLECT_EVIDENCE     : Attach evidence to an incident
 *   - GENERATE_REPORT      : Generate a formal child protection report
 *   - ESCALATE             : Escalate to authorities/guardians
 *   - CHECK_MINOR_STATUS   : Verify if user is a minor
 */
@Component
public class ChildProtectionEvidenceAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "CHILD_PROTECTION_EVIDENCE_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "FLAG_INCIDENT" -> {
                String incidentType = str(payload, "incidentType");
                String subjectId    = str(payload, "subjectUserId");
                String caseId       = "CP-CASE-" + System.currentTimeMillis();
                yield result(
                    "caseId",       caseId,
                    "incidentType", incidentType,
                    "subjectUserId",subjectId,
                    "status",       "FLAGGED",
                    "priority",     "HIGH",
                    "flaggedBy",    userId,
                    "flaggedAt",    java.time.Instant.now().toString(),
                    "nextAction",   "EVIDENCE_COLLECTION_REQUIRED"
                );
            }

            case "COLLECT_EVIDENCE" -> {
                String caseId       = str(payload, "caseId");
                String evidenceType = str(payload, "evidenceType");
                String evidenceRef  = str(payload, "evidenceRef");
                yield result(
                    "caseId",        caseId,
                    "evidenceId",    "EVD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase(),
                    "evidenceType",  evidenceType,
                    "evidenceRef",   evidenceRef,
                    "hashSHA256",    "sha256:mock-hash-" + System.currentTimeMillis(),
                    "preserved",     true,
                    "collectedBy",   userId,
                    "collectedAt",   java.time.Instant.now().toString(),
                    "chainOfCustody","INTACT"
                );
            }

            case "GENERATE_REPORT" -> {
                String caseId = str(payload, "caseId");
                yield result(
                    "caseId",      caseId,
                    "reportId",    "RPT-" + System.currentTimeMillis(),
                    "reportType",  "CHILD_PROTECTION_INCIDENT_REPORT",
                    "format",      "PDF",
                    "generatedBy", userId,
                    "generatedAt", java.time.Instant.now().toString(),
                    "status",      "GENERATED",
                    "classification","CONFIDENTIAL"
                );
            }

            case "ESCALATE" -> {
                String caseId       = str(payload, "caseId");
                String escalateTo   = str(payload, "escalateTo");
                yield result(
                    "caseId",       caseId,
                    "escalatedTo",  escalateTo.isEmpty() ? "PARENT_GUARDIAN" : escalateTo,
                    "escalatedBy",  userId,
                    "escalatedAt",  java.time.Instant.now().toString(),
                    "notified",     true,
                    "status",       "ESCALATED"
                );
            }

            case "CHECK_MINOR_STATUS" -> {
                String subjectId = str(payload, "subjectUserId");
                int    age       = payload != null ? (int) payload.getOrDefault("age", 0) : 0;
                boolean isMinor  = age > 0 && age < 18;
                yield result(
                    "subjectUserId", subjectId,
                    "isMinor",       isMinor,
                    "age",           age,
                    "ageGroup",      age < 13 ? "CHILD" : age < 18 ? "TEEN" : "ADULT",
                    "restrictionsApplied", isMinor
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("FLAG_INCIDENT","COLLECT_EVIDENCE","GENERATE_REPORT",
                                   "ESCALATE","CHECK_MINOR_STATUS")
            );
        };
    }
}
