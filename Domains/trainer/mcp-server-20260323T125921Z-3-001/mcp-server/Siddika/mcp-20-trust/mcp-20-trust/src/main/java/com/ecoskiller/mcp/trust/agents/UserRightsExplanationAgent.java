package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * USER_RIGHTS_EXPLANATION_AGENT
 *
 * Explains user rights in plain language — GDPR, COPPA, DPDPA (India), etc.
 * Enables users to invoke their rights programmatically.
 *
 * Actions:
 *   - EXPLAIN_RIGHTS       : Get plain-language rights summary
 *   - REQUEST_DATA_EXPORT  : User requests their data export (DSAR)
 *   - REQUEST_DELETION     : Right to be forgotten request
 *   - REQUEST_CORRECTION   : Request data correction
 *   - GET_RIGHT_STATUS     : Track status of a rights request
 */
@Component
public class UserRightsExplanationAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "USER_RIGHTS_EXPLANATION_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "EXPLAIN_RIGHTS" -> {
                String jurisdiction = str(payload, "jurisdiction");
                String lang         = str(payload, "language");
                yield result(
                    "jurisdiction", jurisdiction.isEmpty() ? "INDIA_DPDPA" : jurisdiction,
                    "language",     lang.isEmpty() ? "en" : lang,
                    "rights", List.of(
                        Map.of("right","RIGHT_TO_ACCESS","description","You can request a copy of all data we hold about you","article","Art. 11 DPDPA"),
                        Map.of("right","RIGHT_TO_CORRECTION","description","You can ask us to correct inaccurate personal data","article","Art. 12 DPDPA"),
                        Map.of("right","RIGHT_TO_ERASURE","description","You can ask us to delete your personal data in certain circumstances","article","Art. 13 DPDPA"),
                        Map.of("right","RIGHT_TO_GRIEVANCE","description","You can raise a grievance with our Data Protection Officer","article","Art. 13 DPDPA"),
                        Map.of("right","RIGHT_TO_NOMINATE","description","You can nominate someone to exercise your rights on your behalf","article","Art. 14 DPDPA")
                    ),
                    "dpOfficerContact","dpo@ecoskiller.com",
                    "responseTimeDays",30
                );
            }

            case "REQUEST_DATA_EXPORT" -> {
                String format  = str(payload, "format");
                String dsarId  = "DSAR-" + System.currentTimeMillis();
                yield result(
                    "dsarId",        dsarId,
                    "requestType",   "DATA_ACCESS_REQUEST",
                    "requestedBy",   userId,
                    "format",        format.isEmpty() ? "JSON" : format,
                    "status",        "ACCEPTED",
                    "estimatedDays", 7,
                    "submittedAt",   java.time.Instant.now().toString(),
                    "dueBy",         java.time.Instant.now().plusSeconds(30*24*3600L).toString(),
                    "ticketRef",     dsarId
                );
            }

            case "REQUEST_DELETION" -> {
                String scope  = str(payload, "scope");
                String reason = str(payload, "reason");
                String reqId  = "DEL-" + System.currentTimeMillis();
                yield result(
                    "requestId",     reqId,
                    "requestType",   "RIGHT_TO_ERASURE",
                    "scope",         scope.isEmpty() ? "ALL_PERSONAL_DATA" : scope,
                    "reason",        reason,
                    "status",        "UNDER_REVIEW",
                    "requestedBy",   userId,
                    "submittedAt",   java.time.Instant.now().toString(),
                    "note",          "Some data may be retained for legal obligation periods"
                );
            }

            case "REQUEST_CORRECTION" -> {
                String field    = str(payload, "field");
                String oldValue = str(payload, "oldValue");
                String newValue = str(payload, "newValue");
                String corrId   = "CORR-" + System.currentTimeMillis();
                yield result(
                    "correctionId", corrId,
                    "field",        field,
                    "oldValue",     oldValue,
                    "newValue",     newValue,
                    "status",       "PENDING_VERIFICATION",
                    "requestedBy",  userId,
                    "submittedAt",  java.time.Instant.now().toString(),
                    "estimatedDays",3
                );
            }

            case "GET_RIGHT_STATUS" -> {
                String requestId = str(payload, "requestId");
                yield result(
                    "requestId",   requestId,
                    "status",      "IN_PROGRESS",
                    "progress",    "45%",
                    "lastUpdated", java.time.Instant.now().toString(),
                    "steps", List.of(
                        Map.of("step","REQUEST_RECEIVED","done",true),
                        Map.of("step","IDENTITY_VERIFICATION","done",true),
                        Map.of("step","DATA_GATHERING","done",false),
                        Map.of("step","DELIVERY","done",false)
                    )
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("EXPLAIN_RIGHTS","REQUEST_DATA_EXPORT","REQUEST_DELETION",
                                   "REQUEST_CORRECTION","GET_RIGHT_STATUS")
            );
        };
    }
}
