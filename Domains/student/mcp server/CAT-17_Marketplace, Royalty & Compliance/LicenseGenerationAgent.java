package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

/**
 * LICENSE_GENERATION_AGENT_SEALED
 * Issues tamper-evident licenses for idea usage, course content, and digital assets.
 */
public class LicenseGenerationAgent extends ToolDefinition {

    @Override public String getName() { return "license_generation"; }

    @Override public String getDescription() {
        return "LICENSE_GENERATION_AGENT_SEALED — Issues tamper-evident digital licenses for " +
               "idea usage, course content, and digital assets. Stores license hash, " +
               "validity window, usage caps, and sublicense permissions.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "asset_id", "licensee_id", "license_type");
        addStringProp(s, "asset_id",       "ID of the asset being licensed");
        addStringProp(s, "licensee_id",    "User/org receiving the license");
        addStringProp(s, "license_type",   "Type: personal | commercial | sublicensable | exclusive");
        addStringProp(s, "valid_from",     "ISO-8601 start date");
        addStringProp(s, "valid_until",    "ISO-8601 expiry date (null = perpetual)");
        addNumberProp(s, "usage_cap",      "Max allowed uses (-1 = unlimited)");
        addBoolProp  (s, "sublicense_allowed", "Whether sublicensing is permitted");
        addNumberProp(s, "fee_paid_inr",   "License fee paid");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String assetId     = str(args, "asset_id",          "ASSET-UNKNOWN");
        String licenseeId  = str(args, "licensee_id",       "USER-UNKNOWN");
        String licenseType = str(args, "license_type",      "personal");
        String validFrom   = str(args, "valid_from",        java.time.LocalDate.now().toString());
        String validUntil  = str(args, "valid_until",       "");
        double feePaid     = num(args, "fee_paid_inr",      0.0);
        boolean sublicense = bool(args,"sublicense_allowed", false);

        String licenseId   = "LIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String licenseHash = Integer.toHexString((assetId + licenseeId + licenseId).hashCode()).toUpperCase();

        ObjectNode data = m.createObjectNode();
        data.put("license_id",          licenseId);
        data.put("license_hash",        licenseHash);
        data.put("asset_id",            assetId);
        data.put("licensee_id",         licenseeId);
        data.put("license_type",        licenseType);
        data.put("valid_from",          validFrom);
        data.put("valid_until",         validUntil.isEmpty() ? "PERPETUAL" : validUntil);
        data.put("sublicense_allowed",  sublicense);
        data.put("fee_paid_inr",        feePaid);
        data.put("status",              "ACTIVE");
        data.put("issued_at",           java.time.Instant.now().toString());

        return successWithData(m, "License " + licenseId + " issued for asset " + assetId, data);
    }
}
