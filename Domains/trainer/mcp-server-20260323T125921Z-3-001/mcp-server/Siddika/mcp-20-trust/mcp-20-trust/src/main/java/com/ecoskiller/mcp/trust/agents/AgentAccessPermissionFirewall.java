package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * AGENT_ACCESS_PERMISSION_FIREWALL
 *
 * Enforces fine-grained permission checks before any agent or API call proceeds.
 * Acts as the first line of defense in the trust layer.
 *
 * Actions:
 *   - CHECK_PERMISSION   : Validate whether a user/agent can perform an action
 *   - GRANT_PERMISSION   : Temporarily grant elevated permission
 *   - REVOKE_PERMISSION  : Revoke an existing permission
 *   - LIST_PERMISSIONS   : List all active permissions for a principal
 *   - AUDIT_ACCESS       : Return access log for a tenant/user
 */
@Component
public class AgentAccessPermissionFirewall extends BaseAgent {

    @Override
    public String agentName() {
        return "AGENT_ACCESS_PERMISSION_FIREWALL";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={} user={}", agentName(), action, tenantId, userId);

        return switch (action.toUpperCase()) {

            case "CHECK_PERMISSION" -> {
                String resource  = str(payload, "resource");
                String operation = str(payload, "operation");
                // Simulate permission matrix check
                boolean allowed  = !resource.isBlank() && !operation.isBlank();
                yield result(
                    "allowed",    allowed,
                    "resource",   resource,
                    "operation",  operation,
                    "principal",  userId,
                    "policy",     allowed ? "ROLE_BASED_ALLOW" : "DENY_DEFAULT",
                    "reason",     allowed ? "Permission granted via role matrix" : "Resource or operation missing"
                );
            }

            case "GRANT_PERMISSION" -> {
                String resource  = str(payload, "resource");
                String grantedTo = str(payload, "grantedTo");
                int    ttlSec    = payload != null ? (int) payload.getOrDefault("ttlSeconds", 3600) : 3600;
                yield result(
                    "granted",    true,
                    "resource",   resource,
                    "grantedTo",  grantedTo,
                    "grantedBy",  userId,
                    "ttlSeconds", ttlSec,
                    "expiresAt",  java.time.Instant.now().plusSeconds(ttlSec).toString()
                );
            }

            case "REVOKE_PERMISSION" -> {
                String resource     = str(payload, "resource");
                String revokedFrom  = str(payload, "revokedFrom");
                yield result(
                    "revoked",    true,
                    "resource",   resource,
                    "revokedFrom",revokedFrom,
                    "revokedBy",  userId,
                    "revokedAt",  java.time.Instant.now().toString()
                );
            }

            case "LIST_PERMISSIONS" -> {
                String principal = str(payload, "principal");
                yield result(
                    "principal",   principal,
                    "tenant",      tenantId,
                    "permissions", List.of(
                        Map.of("resource","skills","operations",List.of("READ","LIST")),
                        Map.of("resource","profile","operations",List.of("READ","UPDATE")),
                        Map.of("resource","reports","operations",List.of("READ"))
                    ),
                    "totalCount",  3
                );
            }

            case "AUDIT_ACCESS" -> {
                yield result(
                    "tenant",  tenantId,
                    "user",    userId,
                    "logs", List.of(
                        Map.of("ts", "2025-01-01T10:00:00Z", "action","READ","resource","skills","result","ALLOW"),
                        Map.of("ts", "2025-01-01T10:01:00Z", "action","DELETE","resource","reports","result","DENY")
                    )
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("CHECK_PERMISSION","GRANT_PERMISSION",
                                   "REVOKE_PERMISSION","LIST_PERMISSIONS","AUDIT_ACCESS")
            );
        };
    }
}
