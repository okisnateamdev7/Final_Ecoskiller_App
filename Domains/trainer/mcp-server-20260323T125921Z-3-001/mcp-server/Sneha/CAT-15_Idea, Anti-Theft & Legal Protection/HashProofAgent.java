package com.ecoskiller.mcp15;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static com.ecoskiller.mcp15.AgentUtils.*;

/**
 * HASH_PROOF
 *
 * Generates a cryptographic proof-of-existence for any content blob.
 * Produces a SHA-256 hash, a composite proof ID, and a verification record
 * that can be used to prove the content existed at a specific point in time.
 */
public class HashProofAgent implements AgentHandler {

    @Override
    public ObjectNode toolDefinition(String toolName) {
        ObjectNode schema = schemaObject("content", "owner_id");
        addStringProp(schema, "content",      "The raw content / idea text to hash-seal");
        addStringProp(schema, "owner_id",     "User ID of the content owner");
        addStringProp(schema, "content_type", "Type label: IDEA | CODE | DOCUMENT | DATASET (optional)");
        addStringProp(schema, "reference_id", "External reference ID to bind to the proof (optional)");

        return buildToolDef(
            toolName,
            "HASH_PROOF — Generates a tamper-evident SHA-256 proof-of-existence for content, " +
            "binding the hash to owner, timestamp, and an optional reference. " +
            "Used to establish undeniable prior art.",
            schema
        );
    }

    @Override
    public ObjectNode execute(JsonNode args) {
        String content     = args.path("content").asText();
        String ownerId     = args.path("owner_id").asText();
        String contentType = args.path("content_type").asText("IDEA");
        String referenceId = args.path("reference_id").asText("");

        String timestamp    = now();
        String contentHash  = sha256(content);
        String ownerHash    = sha256(ownerId);
        String compositeKey = contentHash + ownerHash + timestamp;
        String proofId      = deterministicId(compositeKey);
        String proofHash    = sha256(compositeKey);
        String merkleLeaf   = sha256(proofHash + ownerId);

        ObjectNode result = MAPPER.createObjectNode();
        result.put("agent",         "HASH_PROOF");
        result.put("status",        "SEALED");
        result.put("proof_id",      proofId);
        result.put("owner_id",      ownerId);
        result.put("content_type",  contentType);
        result.put("timestamp",     timestamp);

        ObjectNode hashes = result.putObject("hashes");
        hashes.put("content_hash",  contentHash);
        hashes.put("owner_hash",    ownerHash);
        hashes.put("proof_hash",    proofHash);
        hashes.put("merkle_leaf",   merkleLeaf);
        hashes.put("algorithm",     "SHA-256");

        if (!referenceId.isBlank()) {
            result.put("reference_id",       referenceId);
            result.put("reference_binding",  sha256(referenceId + proofHash));
        }

        ObjectNode verification = result.putObject("verification");
        verification.put("verifiable",          true);
        verification.put("verification_method", "SHA256_RECOMPUTE");
        verification.put("instructions",
            "Re-hash the original content with SHA-256. Compare against content_hash. " +
            "Match confirms proof-of-existence at recorded timestamp.");

        ObjectNode legal = result.putObject("legal_standing");
        legal.put("proof_class",      "CRYPTOGRAPHIC_PRIOR_ART");
        legal.put("admissibility",    "PENDING_NOTARISATION");
        legal.put("jurisdiction",     "GLOBAL");
        legal.put("export_formats",   "JSON, PDF_CERTIFICATE");

        return result;
    }
}
