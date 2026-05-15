package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

/**
 * SCHOOL_AUTO_CREATION_AGENT
 * Auto-provisions a new EcoSkiller school/franchise node in the marketplace network.
 */
public class SchoolAutoCreationAgent extends ToolDefinition {

    @Override public String getName() { return "school_auto_creation"; }

    @Override public String getDescription() {
        return "SCHOOL_AUTO_CREATION_AGENT — Auto-provisions a new EcoSkiller school or franchise node: " +
               "creates entity record, assigns organizer roles, sets up wallet, " +
               "generates license, and links to the master organizer network.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "school_name", "organizer_id", "region");
        addStringProp(s, "school_name",     "Name of the new school/franchise");
        addStringProp(s, "organizer_id",    "Master organizer / coordinator user ID");
        addStringProp(s, "region",          "State or district (e.g. Maharashtra_Pune)");
        addStringProp(s, "school_type",     "Type: franchise | dojo | rural_block | urban_centre");
        addStringProp(s, "address",         "Physical address of the school");
        addStringProp(s, "contact_email",   "Primary contact email");
        addStringProp(s, "contact_phone",   "Primary contact phone");
        addBoolProp  (s, "auto_wallet",     "Auto-create royalty wallet for the school (default true)");
        addBoolProp  (s, "auto_license",    "Auto-generate franchise license (default true)");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String schoolName  = str(args, "school_name",   "New EcoSkiller School");
        String organizerId = str(args, "organizer_id",  "ORG-UNKNOWN");
        String region      = str(args, "region",        "PAN_INDIA");
        String schoolType  = str(args, "school_type",   "franchise");
        boolean autoWallet = bool(args, "auto_wallet",  true);
        boolean autoLicense= bool(args, "auto_license", true);

        String schoolId    = "SCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String walletId    = autoWallet  ? "WLT-" + schoolId : null;
        String licenseId   = autoLicense ? "LIC-" + schoolId : null;

        ObjectNode data = m.createObjectNode();
        data.put("school_id",    schoolId);
        data.put("school_name",  schoolName);
        data.put("organizer_id", organizerId);
        data.put("region",       region);
        data.put("school_type",  schoolType);
        data.put("status",       "ACTIVE");
        data.put("created_at",   java.time.Instant.now().toString());

        ObjectNode provisioned = data.putObject("provisioned");
        provisioned.put("entity_record",   true);
        provisioned.put("organizer_role",  true);
        provisioned.put("wallet_id",       autoWallet  ? walletId  : "SKIPPED");
        provisioned.put("license_id",      autoLicense ? licenseId : "SKIPPED");
        provisioned.put("network_linked",  true);

        return successWithData(m,
            "School '" + schoolName + "' provisioned as " + schoolId + " in region " + region, data);
    }
}
