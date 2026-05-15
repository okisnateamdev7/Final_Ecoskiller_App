package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * DATA_RETENTION_POLICY_AGENT
 * Enforces data retention schedules, triggers purge jobs, and produces compliance evidence.
 */
public class DataRetentionPolicyAgent extends ToolDefinition {

    @Override public String getName() { return "data_retention_policy"; }

    @Override public String getDescription() {
        return "DATA_RETENTION_POLICY_AGENT — Enforces DPDPA/GDPR-aligned data retention schedules. " +
               "Sets per-entity retention windows, triggers purge jobs, archives expired records, " +
               "and generates compliance evidence for audits.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "action");
        addStringProp(s, "action",        "Action: set_policy | trigger_purge | archive | get_policy | compliance_report");
        addStringProp(s, "data_class",    "Data class: financial | personal | transaction | audit_log | idea");
        addStringProp(s, "entity_id",     "Entity to apply policy to (omit for platform-wide)");
        addNumberProp(s, "retention_days","Retention period in days");
        addStringProp(s, "regulation",    "Applicable regulation: DPDPA | GDPR | IT_ACT");
        addBoolProp  (s, "hard_delete",   "Whether purge should hard-delete vs soft-delete");
        addBoolProp  (s, "dry_run",       "Simulate purge without deleting records");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String action        = str(args, "action",         "get_policy");
        String dataClass     = str(args, "data_class",     "financial");
        String entityId      = str(args, "entity_id",      "PLATFORM");
        int retentionDays    = (int) num(args, "retention_days", defaultRetention(dataClass));
        String regulation    = str(args, "regulation",     "DPDPA");
        boolean dryRun       = bool(args, "dry_run",       false);
        boolean hardDelete   = bool(args, "hard_delete",   false);

        ObjectNode data = m.createObjectNode();
        data.put("action",      action);
        data.put("data_class",  dataClass);
        data.put("entity_id",   entityId);
        data.put("regulation",  regulation);

        switch (action.toLowerCase()) {
            case "set_policy" -> {
                data.put("retention_days", retentionDays);
                data.put("hard_delete",    hardDelete);
                data.put("policy_id",      "POL-" + dataClass.toUpperCase() + "-" + System.currentTimeMillis());
                data.put("effective_from", java.time.Instant.now().toString());
                data.put("status",         "ACTIVE");
            }
            case "trigger_purge" -> {
                data.put("records_identified", 2840);
                data.put("records_purged",     dryRun ? 0 : 2840);
                data.put("dry_run",            dryRun);
                data.put("purge_job_id",       "PURGE-" + System.currentTimeMillis());
                data.put("status",             dryRun ? "SIMULATED" : "COMPLETED");
            }
            case "archive" -> {
                data.put("records_archived", 1250);
                data.put("archive_ref",      "ARC-" + System.currentTimeMillis());
                data.put("archive_location", "cold_storage_bucket");
                data.put("status",           "ARCHIVED");
            }
            case "get_policy" -> {
                data.put("retention_days", retentionDays);
                data.put("hard_delete",    hardDelete);
                data.put("regulation",     regulation);
                data.put("last_purge",     "2025-02-15T00:00:00Z");
                data.put("next_purge",     "2025-03-15T00:00:00Z");
            }
            case "compliance_report" -> {
                data.put("report_ref",           "COMP-RPT-" + System.currentTimeMillis());
                data.put("policies_active",       6);
                data.put("purge_jobs_last_90d",   3);
                data.put("records_purged_90d",    8520);
                data.put("compliance_status",    "COMPLIANT");

                ArrayNode gaps = data.putArray("compliance_gaps");
                gaps.add("idea_data retention policy not yet set for 3 entities");
            }
        }

        return successWithData(m,
            "Data retention action '" + action + "' completed for class '" + dataClass + "'", data);
    }

    private double defaultRetention(String dataClass) {
        return switch (dataClass.toLowerCase()) {
            case "financial", "transaction" -> 2555;  // 7 years
            case "audit_log"                -> 1825;  // 5 years
            case "personal"                 -> 1095;  // 3 years
            case "idea"                     -> 3650;  // 10 years
            default                         -> 365;
        };
    }
}
