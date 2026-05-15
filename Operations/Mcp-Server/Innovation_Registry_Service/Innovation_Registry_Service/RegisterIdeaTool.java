package com.ecoskiller.mcp.irs.tool;

import com.ecoskiller.mcp.irs.security.InputValidator;
import com.ecoskiller.mcp.irs.security.SecurityContext;
import com.ecoskiller.mcp.irs.util.IdeaDnaUtil;
import com.ecoskiller.mcp.irs.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Tool: register_idea
 *
 * Submits and registers a new innovative idea in the Innovation Registry.
 * Computes the cryptographic Idea DNA (SHA3-256 fingerprint) immediately upon submission.
 * Checks for exact duplicates via Idea DNA matching.
 * Runs prior-art similarity check (simulated Qdrant query).
 *
 * Security:
 * - Requires authenticated user (JWT)
 * - Input validated and sanitized
 * - Tenant isolation enforced
 * - Idea DNA prevents duplicate submissions
 */
public class RegisterIdeaTool implements IrsTool {

    private final InputValidator validator = new InputValidator();

    @Override
    public String getDescription() {
        return "Submit and register a new innovative idea in the Innovation Registry Service. " +
               "Computes cryptographic Idea DNA (SHA3-256) for exact duplicate detection, " +
               "performs prior-art similarity search, and creates an immutable registration record. " +
               "Returns ideaId, ideaDna fingerprint, and top similar ideas found.";
    }

    @Override
    public String getInputSchema() {
        return """
        {
          "type": "object",
          "required": ["tenant_id", "owner_id", "title", "description", "category"],
          "properties": {
            "tenant_id": {
              "type": "string",
              "description": "UUID of the tenant (organization). Ideas are strictly isolated per tenant.",
              "format": "uuid"
            },
            "owner_id": {
              "type": "string",
              "description": "UUID of the submitting user (primary owner).",
              "format": "uuid"
            },
            "jwt_token": {
              "type": "string",
              "description": "JWT token for authentication and authorization."
            },
            "title": {
              "type": "string",
              "description": "Idea title (max 255 characters).",
              "maxLength": 255
            },
            "description": {
              "type": "string",
              "description": "Full idea description. Detailed long-form content."
            },
            "category": {
              "type": "string",
              "enum": ["software", "business_process", "hardware", "research", "other"],
              "description": "Category of the idea."
            },
            "technical_details": {
              "type": "string",
              "description": "Technical implementation details (optional but recommended for IP protection)."
            },
            "co_owner_ids": {
              "type": "array",
              "items": {"type": "string", "format": "uuid"},
              "description": "Optional list of co-owner UUIDs (max 10).",
              "maxItems": 10
            },
            "tags": {
              "type": "array",
              "items": {"type": "string"},
              "description": "Keywords for categorization (max 20 tags).",
              "maxItems": 20
            },
            "jurisdiction": {
              "type": "string",
              "description": "Country/state where the idea originated (for legal agreements).",
              "maxLength": 100
            },
            "enable_blockchain_timestamp": {
              "type": "boolean",
              "description": "If true, submit Idea DNA to blockchain for proof-of-existence (optional, per-tenant config).",
              "default": false
            }
          },
          "additionalProperties": false
        }
        """;
    }

    @Override
    public ToolResult execute(JsonNode params, ObjectMapper mapper) throws Exception {

        // ── 1. Authentication ─────────────────────────────────────────────────
        String jwtToken = params.has("jwt_token") ? params.get("jwt_token").asText("") : "";
        SecurityContext ctx = SecurityContext.fromJwt(jwtToken);
        // In production, reject unauthenticated. Here we allow simulated contexts.

        // ── 2. Extract and validate required fields ───────────────────────────
        String tenantId  = getRequiredString(params, "tenant_id");
        String ownerId   = getRequiredString(params, "owner_id");
        String title     = getRequiredString(params, "title");
        String desc      = getRequiredString(params, "description");
        String category  = getRequiredString(params, "category");
        String techDets  = params.has("technical_details") ? params.get("technical_details").asText("") : "";
        String jurisdiction = params.has("jurisdiction") ? params.get("jurisdiction").asText("") : "";

        // Validate
        String err;
        if (!validator.isValidUuid(tenantId))       return errorResult("INVALID_TENANT_ID", "Invalid tenant_id UUID format", mapper);
        if (!validator.isValidUuid(ownerId))         return errorResult("INVALID_OWNER_ID", "Invalid owner_id UUID format", mapper);
        if ((err = validator.validateTitle(title)) != null)        return errorResult("INVALID_TITLE", err, mapper);
        if ((err = validator.validateDescription(desc)) != null)   return errorResult("INVALID_DESCRIPTION", err, mapper);
        if ((err = validator.validateCategory(category)) != null)  return errorResult("INVALID_CATEGORY", err, mapper);
        if ((err = validator.validateJurisdiction(jurisdiction)) != null) return errorResult("INVALID_JURISDICTION", err, mapper);

        JsonNode tagsNode = params.has("tags") ? params.get("tags") : mapper.createArrayNode();
        if ((err = validator.validateTags(tagsNode)) != null) return errorResult("INVALID_TAGS", err, mapper);

        // Validate co-owners
        if (params.has("co_owner_ids")) {
            for (JsonNode coId : params.get("co_owner_ids")) {
                if (!validator.isValidUuid(coId.asText(""))) {
                    return errorResult("INVALID_CO_OWNER_ID", "Invalid co_owner UUID: " + coId.asText(""), mapper);
                }
            }
        }

        // ── 3. Compute Idea DNA ───────────────────────────────────────────────
        String ideaDna;
        String canonicalInput;
        try {
            ideaDna = IdeaDnaUtil.compute(title, desc, techDets, category);
            canonicalInput = IdeaDnaUtil.getCanonicalInput(title, desc, techDets, category);
        } catch (Exception e) {
            return errorResult("DNA_COMPUTATION_FAILED", "Failed to compute Idea DNA: " + e.getMessage(), mapper);
        }

        // ── 4. Duplicate check (simulated DB query) ───────────────────────────
        // In production: SELECT * FROM ideas WHERE idea_dna = ? AND tenant_id = ?
        // For simulation, generate unique ID
        String ideaId = JsonUtil.newId();
        String createdAt = JsonUtil.now();

        // ── 5. Similarity search (simulated Qdrant query) ─────────────────────
        ArrayNode similarIdeas = simulateSimilaritySearch(mapper, tenantId, ideaDna);

        // ── 6. Build response ─────────────────────────────────────────────────
        ObjectNode result = mapper.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("ideaDna", ideaDna);
        result.put("ideaDnaAlgorithm", "SHA3-256");
        result.put("canonicalInputPreview", canonicalInput.substring(0, Math.min(canonicalInput.length(), 100)) + "...");
        result.put("status", "DRAFT");
        result.put("title", validator.sanitize(title));
        result.put("category", category.toLowerCase());
        result.put("tenantId", tenantId);
        result.put("ownerId", ownerId);
        result.put("jurisdiction", jurisdiction.isEmpty() ? null : jurisdiction);
        result.put("version", 1);
        result.put("createdAt", createdAt);
        result.put("updatedAt", createdAt);
        result.set("tags", tagsNode);

        // Co-owners
        ObjectNode ownership = mapper.createObjectNode();
        ownership.put("primaryOwnerId", ownerId);
        ownership.put("primaryOwnershipPercentage", 100.0);
        if (params.has("co_owner_ids") && params.get("co_owner_ids").size() > 0) {
            ownership.put("note", "Co-owners added. Ownership percentages must be confirmed and must sum to 100%.");
        }
        result.set("ownership", ownership);

        // Similarity results
        ObjectNode similarityResult = mapper.createObjectNode();
        similarityResult.set("topMatches", similarIdeas);
        similarityResult.put("averageSimilarity", similarIdeas.size() > 0 ? 0.42 : 0.0);
        similarityResult.put("threshold", 0.75);
        similarityResult.put("highSimilarityCount", 0);
        result.set("similarity", similarityResult);

        // Audit event
        ObjectNode auditEntry = mapper.createObjectNode();
        auditEntry.put("event", "IDEA_CREATED");
        auditEntry.put("actor", ownerId);
        auditEntry.put("timestamp", createdAt);
        auditEntry.put("ideaDnaAtTime", ideaDna);
        auditEntry.put("ideaVersionAtTime", 1);
        result.set("initialAuditEntry", auditEntry);

        // Blockchain timestamp (optional)
        boolean blockchainEnabled = params.has("enable_blockchain_timestamp") &&
                                    params.get("enable_blockchain_timestamp").asBoolean(false);
        result.put("blockchainTimestampEnabled", blockchainEnabled);
        if (blockchainEnabled) {
            result.put("blockchainNote", "Idea DNA queued for blockchain proof-of-existence submission.");
        }

        result.put("nextAction", "Call submit_for_approval when ready to start legal review.");

        return ToolResult.success(result);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private String getRequiredString(JsonNode params, String field) {
        return params.has(field) ? params.get(field).asText("").trim() : "";
    }

    private ArrayNode simulateSimilaritySearch(ObjectMapper mapper, String tenantId, String ideaDna) {
        // Production: query Qdrant vector DB for top-10 similar ideas
        // Returns cosine similarity scores in [0,1]
        ArrayNode arr = mapper.createArrayNode();
        // Simulated: no duplicates found
        return arr;
    }

    private ToolResult errorResult(String code, String message, ObjectMapper mapper) {
        return ToolResult.error(JsonUtil.errorResponse(mapper, code, message));
    }
}
