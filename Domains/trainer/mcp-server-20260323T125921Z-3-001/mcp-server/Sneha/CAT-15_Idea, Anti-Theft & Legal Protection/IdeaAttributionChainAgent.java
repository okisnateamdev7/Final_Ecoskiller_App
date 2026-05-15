package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * IDEA_ATTRIBUTION_CHAIN_AGENT
 *
 * Builds an immutable, timestamped attribution chain for an idea —
 * linking creator → contributors → versions in a tamper-evident ledger.
 */
public class IdeaAttributionChainAgent implements AgentHandler {

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("idea_id", "creator_id", "idea_title", "idea_description");
        addStringProp(schema, "idea_id",          "Unique identifier for the idea");
        addStringProp(schema, "creator_id",       "User ID of the original creator");
        addStringProp(schema, "idea_title",       "Short title of the idea");
        addStringProp(schema, "idea_description", "Full description / abstract of the idea");
        addStringProp(schema, "contributor_ids",  "Comma-separated user IDs of contributors (optional)");
        addStringProp(schema, "parent_idea_id",   "Parent idea ID if this is a derivative (optional)");

        return buildToolDef(
            toolName,
            "IDEA_ATTRIBUTION_CHAIN_AGENT — Builds a cryptographically linked attribution chain " +
            "for an idea, recording the original creator, contributors, timestamps, and parent " +
            "derivation in a tamper-evident ledger entry.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String ideaId          = args.path("idea_id").asText();
        String creatorId       = args.path("creator_id").asText();
        String ideaTitle       = args.path("idea_title").asText();
        String ideaDescription = args.path("idea_description").asText();
        String contributorRaw  = args.path("contributor_ids").asText("");
        String parentIdeaId    = args.path("parent_idea_id").asText("");

        // Derive content hash
        String contentHash = sha256(ideaId + creatorId + ideaTitle + ideaDescription);

        // Build attribution chain entries
        ArrayNode chain = MAPPER.createArrayNode();

        // Genesis entry
        ObjectNode genesis = chain.addObject();
        genesis.put("entry_type",    "GENESIS");
        genesis.put("entry_id",      deterministicId(ideaId + "genesis"));
        genesis.put("idea_id",       ideaId);
        genesis.put("actor_id",      creatorId);
        genesis.put("role",          "ORIGINAL_CREATOR");
        genesis.put("timestamp",     now());
        genesis.put("content_hash",  contentHash);
        genesis.put("prev_hash",     "0000000000000000");
        genesis.put("chain_hash",    sha256("0000000000000000" + contentHash + creatorId));

        // Contributor entries
        List<String> contributors = new ArrayList<>();
        if (!contributorRaw.isBlank()) {
            for (String c : contributorRaw.split(",")) {
                c = c.trim();
                if (!c.isEmpty()) contributors.add(c);
            }
        }

        String prevChainHash = (String) genesis.get("chain_hash").asText();
        for (String contributor : contributors) {
            ObjectNode entry = chain.addObject();
            String contribHash = sha256(contributor + prevChainHash);
            entry.put("entry_type",   "CONTRIBUTION");
            entry.put("entry_id",     deterministicId(ideaId + contributor));
            entry.put("idea_id",      ideaId);
            entry.put("actor_id",     contributor);
            entry.put("role",         "CONTRIBUTOR");
            entry.put("timestamp",    now());
            entry.put("content_hash", contribHash);
            entry.put("prev_hash",    prevChainHash);
            entry.put("chain_hash",   sha256(prevChainHash + contribHash + contributor));
            prevChainHash = entry.get("chain_hash").asText();
        }

        // Derivation linkage
        ObjectNode derivation = MAPPER.createObjectNode();
        if (!parentIdeaId.isBlank()) {
            derivation.put("is_derivative",  true);
            derivation.put("parent_idea_id", parentIdeaId);
            derivation.put("derivation_hash", sha256(parentIdeaId + ideaId));
        } else {
            derivation.put("is_derivative", false);
        }

        // Assemble result
        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",          "IDEA_ATTRIBUTION_CHAIN_AGENT");
        result.put("status",         "SUCCESS");
        result.put("idea_id",        ideaId);
        result.put("idea_title",     ideaTitle);
        result.put("creator_id",     creatorId);
        result.put("content_hash",   contentHash);
        result.put("chain_length",   chain.size());
        result.put("timestamp",      now());
        result.set("attribution_chain", chain);
        result.set("derivation",        derivation);

        ObjectNode meta = result.putObject("legal_metadata");
        meta.put("protection_class", "IP_ATTRIBUTED");
        meta.put("jurisdiction",     "GLOBAL");
        meta.put("disclosure_level", "PRIVATE");
        meta.put("immutable",        true);

        return result;
    }
}
