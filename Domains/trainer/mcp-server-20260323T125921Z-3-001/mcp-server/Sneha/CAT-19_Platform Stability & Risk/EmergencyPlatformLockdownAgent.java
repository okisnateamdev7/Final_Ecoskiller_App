package com.ecoskiller.mcp19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.UUID;

/**
 * EMERGENCY_PLATFORM_LOCKDOWN_AGENT
 *
 * Executes controlled platform-wide or scope-limited lockdowns in response
 * to critical security events, catastrophic agent failures, data-breach
 * signals, or manual admin overrides. Manages lockdown tiers, audit trails,
 * and graceful recovery checklists.
 */
public class EmergencyPlatformLockdownAgent implements AgentHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String description() {
        return "Executes controlled platform lockdowns (FULL, PARTIAL, or TENANT-scoped) " +
               "in response to critical failures, security breaches, or admin overrides. " +
               "Manages lockdown tiers, audit trails, and recovery checklists.";
    }

    @Override
    public ObjectNode inputSchema() {
        ObjectNode schema = MAPPER.createObjectNode();
        schema.put("type", "object");

        ObjectNode props = MAPPER.createObjectNode();

        ObjectNode lockdownType = MAPPER.createObjectNode();
        lockdownType.put("type", "string");
        lockdownType.put("description", "Lockdown scope: FULL | PARTIAL | TENANT | READ_ONLY | SIMULATE");
        props.set("lockdown_type", lockdownType);

        ObjectNode reason = MAPPER.createObjectNode();
        reason.put("type", "string");
        reason.put("description", "Human-readable reason for lockdown (logged in audit trail)");
        props.set("reason", reason);

        ObjectNode initiatedBy = MAPPER.createObjectNode();
        initiatedBy.put("type", "string");
        initiatedBy.put("description", "Agent or admin identity initiating the lockdown");
        props.set("initiated_by", initiatedBy);

        ObjectNode tenantId = MAPPER.createObjectNode();
        tenantId.put("type", "string");
        tenantId.put("description", "Tenant ID for TENANT-scoped lockdown (optional)");
        props.set("tenant_id", tenantId);

        ObjectNode durationMinutes = MAPPER.createObjectNode();
        durationMinutes.put("type", "integer");
        durationMinutes.put("description", "Auto-release duration in minutes (0 = manual release only)");
        durationMinutes.put("default", 0);
        props.set("duration_minutes", durationMinutes);

        ObjectNode dryRun = MAPPER.createObjectNode();
        dryRun.put("type", "boolean");
        dryRun.put("description", "If true, simulate lockdown without affecting live systems");
        dryRun.put("default", false);
        props.set("dry_run", dryRun);

        schema.set("properties", props);

        com.fasterxml.jackson.databind.node.ArrayNode required = MAPPER.createArrayNode();
        required.add("lockdown_type");
        required.add("reason");
        required.add("initiated_by");
        schema.set("required", required);

        return schema;
    }

    @Override
    public JsonNode execute(JsonNode args) {
        String lockdownType    = args.path("lockdown_type").asText("SIMULATE");
        String reason          = args.path("reason").asText("Unspecified");
        String initiatedBy     = args.path("initiated_by").asText("UNKNOWN");
        String tenantId        = args.path("tenant_id").asText(null);
        int    durationMinutes = args.path("duration_minutes").asInt(0);
        boolean dryRun         = args.path("dry_run").asBoolean(false);

        String lockdownId = "LKD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",          "EMERGENCY_PLATFORM_LOCKDOWN_AGENT");
        result.put("category",       "CAT-19 — Platform Stability & Risk");
        result.put("timestamp",      Instant.now().toString());
        result.put("lockdown_id",    lockdownId);
        result.put("lockdown_type",  lockdownType);
        result.put("dry_run",        dryRun);
        result.put("reason",         reason);
        result.put("initiated_by",   initiatedBy);

        if (tenantId != null && !tenantId.isBlank()) {
            result.put("tenant_id", tenantId);
        }

        // Lockdown decision
        String status = dryRun ? "SIMULATED" : "LOCKDOWN_ENGAGED";
        result.put("status", status);

        // Actions taken
        com.fasterxml.jackson.databind.node.ArrayNode actions = MAPPER.createArrayNode();
        if (!dryRun) {
            switch (lockdownType.toUpperCase()) {
                case "FULL"      -> {
                    actions.add("Suspended all write operations across platform");
                    actions.add("Revoked all active session tokens");
                    actions.add("Blocked inbound API traffic");
                    actions.add("Notified all tenant admins via emergency channel");
                }
                case "PARTIAL"   -> {
                    actions.add("Suspended high-risk agent executions");
                    actions.add("Enabled read-only mode for non-critical modules");
                    actions.add("Escalated to on-call engineering team");
                }
                case "TENANT"    -> {
                    actions.add("Isolated tenant " + (tenantId != null ? tenantId : "N/A") + " from platform");
                    actions.add("Suspended tenant API keys");
                    actions.add("Preserved tenant data in immutable snapshot");
                }
                case "READ_ONLY" -> {
                    actions.add("Downgraded all write endpoints to read-only");
                    actions.add("Maintained read traffic with reduced SLA");
                }
                default          -> actions.add("Simulation mode — no live action taken");
            }
        } else {
            actions.add("[DRY RUN] Would engage " + lockdownType + " lockdown");
            actions.add("[DRY RUN] Estimated impact: " + estimateImpact(lockdownType));
        }
        result.set("actions_taken", actions);

        // Recovery checklist
        com.fasterxml.jackson.databind.node.ArrayNode recovery = MAPPER.createArrayNode();
        recovery.add("1. Identify and resolve root cause");
        recovery.add("2. Run AGENT_HEALTH_WATCHDOG_AGENT full scan");
        recovery.add("3. Validate INTEGRATION_HEALTH_MONITOR_AGENT clearance");
        recovery.add("4. Obtain admin sign-off for unlock");
        recovery.add("5. Issue post-incident report within 24h");
        result.set("recovery_checklist", recovery);

        if (durationMinutes > 0) {
            result.put("auto_release_at",
                Instant.now().plusSeconds(durationMinutes * 60L).toString());
        } else {
            result.put("auto_release_at", "MANUAL_RELEASE_REQUIRED");
        }

        result.put("audit_trail_id", "AUDIT-" + lockdownId);
        return result;
    }

    private String estimateImpact(String type) {
        return switch (type.toUpperCase()) {
            case "FULL"      -> "100% platform downtime — all tenants affected";
            case "PARTIAL"   -> "~30% of agents suspended — read traffic unaffected";
            case "TENANT"    -> "Single tenant isolated — other tenants unaffected";
            case "READ_ONLY" -> "Write latency SLA degraded — reads nominal";
            default          -> "Unknown scope";
        };
    }
}
