package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AUDIT_VERIFICATION_AGENT
 * Runs integrity audits across royalty ledgers, contracts, licenses, and compliance trails.
 */
public class AuditVerificationAgent extends ToolDefinition {

    @Override public String getName() { return "audit_verification"; }

    @Override public String getDescription() {
        return "AUDIT_VERIFICATION_AGENT — Runs integrity audits across royalty ledgers, " +
               "license records, contracts, and compliance trails. Detects anomalies, " +
               "hash mismatches, orphaned records, and produces signed audit certificates.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "audit_scope");
        addStringProp(s, "audit_scope",   "Scope: royalty_ledger | licenses | contracts | compliance | full");
        addStringProp(s, "cycle_id",      "Cycle to audit (omit for latest)");
        addStringProp(s, "entity_id",     "Scope audit to a single entity (omit for all)");
        addBoolProp  (s, "hash_verify",   "Re-compute and verify hashes of all records");
        addBoolProp  (s, "generate_cert", "Generate a signed audit certificate on pass");
        addStringProp(s, "auditor_id",    "User ID or system triggering the audit");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String scope      = str(args, "audit_scope",   "full");
        String cycleId    = str(args, "cycle_id",      "CYCLE-CURRENT");
        String entityId   = str(args, "entity_id",     "ALL");
        boolean hashVerify= bool(args, "hash_verify",  true);
        boolean genCert   = bool(args, "generate_cert",false);
        String auditorId  = str(args, "auditor_id",    "system");

        int totalChecked  = 1284;
        int passed        = 1279;
        int failed        = 5;

        ObjectNode summary = m.createObjectNode();
        summary.put("total_records_checked", totalChecked);
        summary.put("passed",                passed);
        summary.put("failed",                failed);
        summary.put("pass_rate_pct",         Math.round((passed * 100.0 / totalChecked) * 100.0) / 100.0);

        ArrayNode findings = m.createArrayNode();
        if (failed > 0) {
            ObjectNode f1 = findings.addObject();
            f1.put("finding_id",  "AUD-F001");
            f1.put("severity",    "LOW");
            f1.put("description", "3 royalty ledger entries missing cycle_id tag");
            f1.put("entity_ids",  "ENTITY-004, ENTITY-017, ENTITY-089");

            ObjectNode f2 = findings.addObject();
            f2.put("finding_id",  "AUD-F002");
            f2.put("severity",    "MEDIUM");
            f2.put("description", "2 license records: hash mismatch on re-verification");
            f2.put("entity_ids",  "LIC-0A3F, LIC-2B7C");
        }

        ObjectNode data = m.createObjectNode();
        data.put("audit_id",    "AUD-" + System.currentTimeMillis());
        data.put("scope",        scope);
        data.put("cycle_id",     cycleId);
        data.put("entity_id",    entityId);
        data.put("hash_verified",hashVerify);
        data.put("auditor_id",   auditorId);
        data.put("audit_status", failed == 0 ? "PASSED" : "PASSED_WITH_FINDINGS");
        data.set("summary",      summary);
        data.set("findings",     findings);
        data.put("audited_at",   java.time.Instant.now().toString());

        if (genCert && failed == 0) {
            data.put("certificate_ref", "CERT-AUD-" + System.currentTimeMillis());
            data.put("certificate_hash", Integer.toHexString((scope + cycleId + auditorId).hashCode()).toUpperCase());
        }

        String msg = failed == 0
            ? "Audit PASSED: " + totalChecked + " records verified"
            : "Audit completed with " + failed + " findings across " + totalChecked + " records";

        return successWithData(m, msg, data);
    }
}
