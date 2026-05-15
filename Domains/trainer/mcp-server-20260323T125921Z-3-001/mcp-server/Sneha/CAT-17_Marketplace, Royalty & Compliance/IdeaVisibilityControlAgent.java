package com.ecoskiller.mcp17.agents;

import com.ecoskiller.mcp17.ToolDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * IDEA_VISIBILITY_CONTROL_AGENT_IVCA-v1.0.0
 * Controls who can view, licence, or fork an idea submission in the marketplace.
 */
public class IdeaVisibilityControlAgent extends ToolDefinition {

    @Override public String getName() { return "idea_visibility_control"; }

    @Override public String getDescription() {
        return "IDEA_VISIBILITY_CONTROL_AGENT_IVCA-v1.0.0 — Controls visibility, access tiers, " +
               "and disclosure permissions for idea submissions. Manages public/private toggles, " +
               "whitelisted viewers, embargo windows, and commercial exposure settings.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper m) {
        ObjectNode s = baseSchema(m, "idea_id", "action");
        addStringProp(s, "idea_id",       "Idea asset identifier");
        addStringProp(s, "action",        "Action: set_visibility | grant_access | revoke_access | get_policy");
        addStringProp(s, "visibility",    "Visibility level: private | invite_only | public | marketplace");
        addStringProp(s, "target_user_id","User to grant/revoke access to");
        addStringProp(s, "embargo_until", "ISO-8601 date until which idea stays hidden");
        addBoolProp  (s, "commercial_exposure", "Allow commercial entities to discover this idea");
        addArrayProp (s, "whitelist_roles","Roles that can view: evaluator | investor | partner", "string");
        return s;
    }

    @Override
    public ObjectNode execute(JsonNode args, ObjectMapper m) {
        String ideaId    = str(args, "idea_id",   "IDEA-UNKNOWN");
        String action    = str(args, "action",    "get_policy");
        String visibility= str(args, "visibility","private");

        ObjectNode policy = m.createObjectNode();
        policy.put("idea_id",    ideaId);
        policy.put("visibility", visibility);
        policy.put("commercial_exposure", bool(args,"commercial_exposure",false));
        policy.put("embargo_until",       str(args,"embargo_until","none"));

        ArrayNode whitelistRoles = policy.putArray("whitelist_roles");
        JsonNode wlNode = args.get("whitelist_roles");
        if (wlNode != null && wlNode.isArray()) {
            wlNode.forEach(n -> whitelistRoles.add(n.asText()));
        }

        ObjectNode data = m.createObjectNode();
        data.put("action_taken", action);
        data.set("policy",       policy);
        data.put("policy_version", "v" + System.currentTimeMillis());

        return successWithData(m, "Visibility policy updated for idea " + ideaId, data);
    }
}
