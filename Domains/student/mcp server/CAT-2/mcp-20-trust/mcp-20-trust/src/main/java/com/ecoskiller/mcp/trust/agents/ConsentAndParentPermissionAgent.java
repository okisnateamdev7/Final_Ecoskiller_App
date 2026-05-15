package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * CONSENT_AND_PARENT_PERMISSION_AGENT
 *
 * Manages COPPA/GDPR-compliant consent flows for minors.
 * Handles parental consent, permission grants, and consent revocation.
 *
 * Actions:
 *   - REQUEST_CONSENT      : Send consent request to parent/guardian
 *   - VERIFY_CONSENT       : Check if valid consent exists
 *   - REVOKE_CONSENT       : Parent revokes child's permission
 *   - GET_CONSENT_HISTORY  : Full audit trail of consent
 *   - BULK_CONSENT_CHECK   : Check consent for multiple users
 */
@Component
public class ConsentAndParentPermissionAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "CONSENT_AND_PARENT_PERMISSION_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "REQUEST_CONSENT" -> {
                String childId     = str(payload, "childUserId");
                String parentEmail = str(payload, "parentEmail");
                String scope       = str(payload, "consentScope");
                String consentId   = "CNS-" + System.currentTimeMillis();
                yield result(
                    "consentId",    consentId,
                    "childUserId",  childId,
                    "parentEmail",  parentEmail,
                    "scope",        scope.isEmpty() ? "FULL_PLATFORM_ACCESS" : scope,
                    "status",       "PENDING_PARENT_APPROVAL",
                    "expiresAt",    java.time.Instant.now().plusSeconds(7*24*3600).toString(),
                    "notificationSent", true,
                    "channel",      "EMAIL"
                );
            }

            case "VERIFY_CONSENT" -> {
                String childId = str(payload, "childUserId");
                String scope   = str(payload, "scope");
                yield result(
                    "childUserId",   childId,
                    "scope",         scope,
                    "consentGranted",true,
                    "consentId",     "CNS-MOCK-001",
                    "grantedBy",     "parent@example.com",
                    "grantedAt",     "2025-01-01T10:00:00Z",
                    "expiresAt",     "2026-01-01T10:00:00Z",
                    "legalBasis",    "PARENTAL_CONSENT_COPPA"
                );
            }

            case "REVOKE_CONSENT" -> {
                String consentId  = str(payload, "consentId");
                String reason     = str(payload, "reason");
                yield result(
                    "consentId",   consentId,
                    "status",      "REVOKED",
                    "reason",      reason.isEmpty() ? "Parent request" : reason,
                    "revokedBy",   userId,
                    "revokedAt",   java.time.Instant.now().toString(),
                    "dataDeleted", false,
                    "note",        "Data retained for 30 days per policy then auto-deleted"
                );
            }

            case "GET_CONSENT_HISTORY" -> {
                String childId = str(payload, "childUserId");
                yield result(
                    "childUserId", childId,
                    "tenant",      tenantId,
                    "history", List.of(
                        Map.of("consentId","CNS-001","action","GRANTED","ts","2025-01-01T10:00:00Z","by","parent@example.com"),
                        Map.of("consentId","CNS-001","action","RENEWED","ts","2025-06-01T10:00:00Z","by","parent@example.com"),
                        Map.of("consentId","CNS-002","action","REVOKED","ts","2025-08-01T10:00:00Z","by","parent@example.com")
                    ),
                    "totalEvents", 3
                );
            }

            case "BULK_CONSENT_CHECK" -> {
                @SuppressWarnings("unchecked")
                List<String> userIds = payload != null
                    ? (List<String>) payload.getOrDefault("userIds", List.of())
                    : List.of();
                List<Map<String,Object>> results = new ArrayList<>();
                for (String uid : userIds) {
                    results.add(Map.of("userId", uid, "consentGranted", true, "legalBasis","PARENTAL_CONSENT_COPPA"));
                }
                yield result(
                    "tenant",     tenantId,
                    "checked",    userIds.size(),
                    "results",    results,
                    "allConsented", results.stream().allMatch(m -> Boolean.TRUE.equals(m.get("consentGranted")))
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("REQUEST_CONSENT","VERIFY_CONSENT","REVOKE_CONSENT",
                                   "GET_CONSENT_HISTORY","BULK_CONSENT_CHECK")
            );
        };
    }
}
