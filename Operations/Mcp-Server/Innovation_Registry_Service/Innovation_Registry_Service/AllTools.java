package com.ecoskiller.mcp.irs.tool;

import com.ecoskiller.mcp.irs.security.InputValidator;
import com.ecoskiller.mcp.irs.util.IdeaDnaUtil;
import com.ecoskiller.mcp.irs.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

// ════════════════════════════════════════════════════════════════════════════
// Tool 2: Get Idea
// ════════════════════════════════════════════════════════════════════════════
class GetIdeaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Retrieve full idea details including metadata, ownership, Idea DNA, " +
               "current status, attachments list, and recent audit events. " +
               "Enforces tenant isolation — ideas are only accessible within their tenant.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid","description":"UUID of the idea to retrieve."},
           "tenant_id":{"type":"string","format":"uuid","description":"Tenant UUID for isolation."},
           "jwt_token":{"type":"string"},
           "include_audit_log":{"type":"boolean","default":false,"description":"Include full audit event history."},
           "include_similarity_cache":{"type":"boolean","default":false,"description":"Include cached similarity results."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("")   : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",   "Invalid idea_id UUID format", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id UUID format", m);

        boolean inclAudit = p.has("include_audit_log") && p.get("include_audit_log").asBoolean(false);

        // Production: SELECT * FROM ideas WHERE id = ? AND tenant_id = ?
        ObjectNode idea = m.createObjectNode();
        idea.put("id", ideaId);
        idea.put("tenantId", tenantId);
        idea.put("title", "Example Idea Title");
        idea.put("description", "Idea description would be fetched from PostgreSQL.");
        idea.put("category", "software");
        idea.put("ideaDna", "a7b3c8f2e1d9" + "0".repeat(52));
        idea.put("ideaDnaAlgorithm", "SHA3-256");
        idea.put("status", "DRAFT");
        idea.put("primaryOwnerId", JsonUtil.newId());
        idea.put("version", 1);
        idea.put("isLicensed", false);
        idea.put("licenseCount", 0);
        idea.put("totalRoyalties", 0.0);
        idea.put("createdAt", "2026-03-10T00:00:00Z");
        idea.put("updatedAt", "2026-03-10T00:00:00Z");
        idea.put("note", "Production: fetched from PostgreSQL with RLS tenant isolation.");

        if (inclAudit) {
            ArrayNode auditLog = m.createArrayNode();
            ObjectNode evt = m.createObjectNode();
            evt.put("event", "IDEA_CREATED"); evt.put("timestamp", "2026-03-10T00:00:00Z");
            auditLog.add(evt);
            idea.set("auditLog", auditLog);
        }
        return ToolResult.success(idea);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 3: Update Idea
// ════════════════════════════════════════════════════════════════════════════
class UpdateIdeaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Update idea metadata (title, description, technical_details, tags). " +
               "Only the idea owner can update. Each update creates a new version and recomputes Idea DNA. " +
               "Updates to title/description after SUBMITTED status require legal team re-review.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","owner_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "owner_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "title":{"type":"string","maxLength":255},
           "description":{"type":"string"},
           "technical_details":{"type":"string"},
           "tags":{"type":"array","items":{"type":"string"},"maxItems":20},
           "references":{"type":"array","items":{"type":"string"},"maxItems":50}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("")   : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        String ownerId  = p.has("owner_id")  ? p.get("owner_id").asText("")  : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",   "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);
        if (!v.isValidUuid(ownerId))  return err("INVALID_OWNER_ID",  "Invalid owner_id", m);

        // Validate updated fields
        if (p.has("title")) {
            String err = v.validateTitle(p.get("title").asText(""));
            if (err != null) return ToolResult.error(JsonUtil.errorResponse(m, "INVALID_TITLE", err));
        }
        if (p.has("description")) {
            String err = v.validateDescription(p.get("description").asText(""));
            if (err != null) return ToolResult.error(JsonUtil.errorResponse(m, "INVALID_DESCRIPTION", err));
        }

        // Recompute Idea DNA if content changed
        String newDna = null;
        if (p.has("title") || p.has("description") || p.has("technical_details")) {
            String title   = p.has("title")            ? p.get("title").asText("") : "existing";
            String desc    = p.has("description")      ? p.get("description").asText("") : "existing desc";
            String tech    = p.has("technical_details") ? p.get("technical_details").asText("") : "";
            newDna = IdeaDnaUtil.compute(title, desc, tech, "software");
        }

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("status", "updated");
        result.put("newVersion", 2);
        if (newDna != null) {
            result.put("newIdeaDna", newDna);
            result.put("dnaChanged", true);
        }
        result.put("updatedAt", JsonUtil.now());
        result.put("note", "Production: UPDATE ideas SET ... WHERE id=? AND tenant_id=? AND primary_owner_id=?");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 4: Search Similar Ideas (Prior-Art Detection)
// ════════════════════════════════════════════════════════════════════════════
class SearchSimilarIdeasTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Find semantically similar ideas using vector embeddings (Qdrant HNSW). " +
               "Computes cosine similarity between idea embeddings. " +
               "Thresholds: >0.9 = nearly identical (flag), 0.75-0.9 = very similar (review), " +
               "0.5-0.75 = related. Used for prior-art detection before submitting ideas.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid","description":"Idea to find similar ideas for."},
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "top_k":{"type":"integer","minimum":1,"maximum":50,"default":10,
                    "description":"Number of similar ideas to return (max 50)."},
           "similarity_threshold":{"type":"number","minimum":0.0,"maximum":1.0,"default":0.75,
                                   "description":"Minimum cosine similarity score (0.0-1.0)."},
           "include_archived":{"type":"boolean","default":false,
                               "description":"Include archived ideas in similarity search."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("")   : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",   "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);

        int topK = p.has("top_k") ? p.get("top_k").asInt(10) : 10;
        if (topK < 1 || topK > 50) return err("INVALID_TOP_K", "top_k must be 1-50", m);

        double threshold = p.has("similarity_threshold") ? p.get("similarity_threshold").asDouble(0.75) : 0.75;
        String threshErr = v.validateSimilarityThreshold(threshold);
        if (threshErr != null) return err("INVALID_THRESHOLD", threshErr, m);

        // Production: query Qdrant HNSW index
        // 1. Load 384-dim embedding for idea_id from PostgreSQL/Qdrant
        // 2. Query Qdrant: {filter: {tenant_id}, top: topK, vector: embedding, score_threshold: threshold}
        // 3. Return scored results

        ArrayNode matches = m.createArrayNode();
        ObjectNode meta = m.createObjectNode();
        meta.put("ideaId", ideaId);
        meta.put("tenantId", tenantId);
        meta.put("threshold", threshold);
        meta.put("topK", topK);
        meta.set("matches", matches);
        meta.put("searchLatencyMs", 47);
        meta.put("totalIdeasSearched", 1000);
        meta.put("algorithm", "Qdrant HNSW cosine similarity");
        meta.put("note", "Production: Qdrant vector search across up to 1M tenant ideas in <100ms.");
        meta.put("thresholds", ">0.9=nearlyIdentical, 0.75-0.9=verySimilar, 0.5-0.75=related");
        return ToolResult.success(meta);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 5: Add Co-Owner
// ════════════════════════════════════════════════════════════════════════════
class AddCoOwnerTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Add a co-owner to an existing idea. Requires consent from the current primary owner. " +
               "Ownership percentages must sum to 100%. All co-owners must approve licensing decisions. " +
               "Tracked in immutable co-ownership ledger.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","requesting_owner_id","new_co_owner_id","new_owner_percentage"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "requesting_owner_id":{"type":"string","format":"uuid","description":"Must be existing owner."},
           "jwt_token":{"type":"string"},
           "new_co_owner_id":{"type":"string","format":"uuid","description":"User to add as co-owner."},
           "new_owner_percentage":{"type":"number","minimum":0.01,"maximum":99.99,
                                   "description":"Ownership % for new co-owner. Existing owners' shares reduced proportionally."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId  = p.has("idea_id")   ? p.get("idea_id").asText("") : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        String reqOwnerId   = p.has("requesting_owner_id")  ? p.get("requesting_owner_id").asText("") : "";
        String newCoOwnerId = p.has("new_co_owner_id")       ? p.get("new_co_owner_id").asText("") : "";
        double newPct = p.has("new_owner_percentage") ? p.get("new_owner_percentage").asDouble(0) : 0;

        if (!v.isValidUuid(ideaId))       return err("INVALID_IDEA_ID",  "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId))     return err("INVALID_TENANT",   "Invalid tenant_id", m);
        if (!v.isValidUuid(reqOwnerId))   return err("INVALID_OWNER",    "Invalid requesting_owner_id", m);
        if (!v.isValidUuid(newCoOwnerId)) return err("INVALID_CO_OWNER", "Invalid new_co_owner_id", m);
        String pctErr = v.validateOwnershipPercentage(newPct);
        if (pctErr != null) return err("INVALID_PERCENTAGE", pctErr, m);

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("newCoOwnerId", newCoOwnerId);
        result.put("newOwnershipPercentage", newPct);
        result.put("status", "CO_OWNER_ADDED");
        result.put("addedAt", JsonUtil.now());
        result.put("note", "All co-owners must approve future licensing decisions (multi-sig style).");
        result.put("auditEvent", "CO_OWNER_ADDED recorded in idea_audit_log.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 6: Transfer Ownership
// ════════════════════════════════════════════════════════════════════════════
class TransferOwnershipTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Initiate an ownership transfer for an idea. Requires approval from all current co-owners " +
               "(multi-sig style). Creates a pending transfer that expires after 7 days if not approved. " +
               "Publishes idea.ownership_transfer_initiated event to Kafka.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","current_owner_id","proposed_new_owner_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "current_owner_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "proposed_new_owner_id":{"type":"string","format":"uuid"},
           "ownership_percentage":{"type":"number","minimum":0.01,"maximum":100.0,
                                   "description":"Percentage being transferred."},
           "reason":{"type":"string","maxLength":500,"description":"Reason for transfer (optional)."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("") : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        String curOwner = p.has("current_owner_id")      ? p.get("current_owner_id").asText("") : "";
        String newOwner = p.has("proposed_new_owner_id") ? p.get("proposed_new_owner_id").asText("") : "";

        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",  "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT",   "Invalid tenant_id", m);
        if (!v.isValidUuid(curOwner)) return err("INVALID_OWNER",    "Invalid current_owner_id", m);
        if (!v.isValidUuid(newOwner)) return err("INVALID_NEW_OWNER","Invalid proposed_new_owner_id", m);

        String transferId = JsonUtil.newId();
        ObjectNode result = m.createObjectNode();
        result.put("transferId", transferId);
        result.put("ideaId", ideaId);
        result.put("currentOwnerId", curOwner);
        result.put("proposedNewOwnerId", newOwner);
        result.put("status", "PENDING_APPROVAL");
        result.put("initiatedAt", JsonUtil.now());
        result.put("expiresAt", "7 days from now");
        result.put("kafkaEvent", "idea.ownership_transfer_initiated published");
        result.put("note", "All co-owners must approve. Notification sent via notification-service.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 7: Search Ideas (Full-text)
// ════════════════════════════════════════════════════════════════════════════
class SearchIdeasTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Full-text search across the idea corpus for the tenant. " +
               "Searches title, description, tags. Results sorted by relevance. " +
               "Optional filters: category, status, dateRange. Returns paginated results.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["tenant_id","query"],
         "properties":{
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "query":{"type":"string","maxLength":500,"description":"Search query (keywords)."},
           "category":{"type":"string","enum":["software","business_process","hardware","research","other"]},
           "status":{"type":"string","enum":["DRAFT","SUBMITTED","LEGAL_REVIEW","APPROVED","LICENSED","ARCHIVED"]},
           "page":{"type":"integer","minimum":1,"default":1},
           "page_size":{"type":"integer","minimum":1,"maximum":100,"default":20},
           "sort_by":{"type":"string","enum":["relevance","created_at","title"],"default":"relevance"}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        String query    = p.has("query")     ? p.get("query").asText("").trim() : "";
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);
        String qErr = v.validateQuery(query);
        if (qErr != null) return err("INVALID_QUERY", qErr, m);

        int pageSize = p.has("page_size") ? p.get("page_size").asInt(20) : 20;
        if (pageSize < 1 || pageSize > 100) return err("INVALID_PAGE_SIZE", "page_size must be 1-100", m);

        // Production: Elasticsearch full-text search OR PostgreSQL full-text (tsvector)
        // Query: SELECT ... FROM ideas WHERE tenant_id=? AND to_tsvector(title||description) @@ plainto_tsquery(?)
        ObjectNode result = m.createObjectNode();
        result.put("query", v.sanitize(query));
        result.put("tenantId", tenantId);
        result.put("totalResults", 0);
        result.put("page", 1);
        result.put("pageSize", pageSize);
        result.set("results", m.createArrayNode());
        result.put("searchEngine", "Elasticsearch / PostgreSQL tsvector");
        result.put("note", "Production: full-text search across title, description, tags with relevance scoring.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 8: Bulk Analyze Ideas (Admin)
// ════════════════════════════════════════════════════════════════════════════
class BulkAnalyzeIdeasTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Admin tool: batch similarity analysis across multiple ideas. " +
               "Returns pairwise similarity matrix and cluster groups of related ideas. " +
               "Used to detect redundant ideas, coordinated spam submissions. " +
               "Results cached; scheduled weekly at 2 AM UTC.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["tenant_id","idea_ids"],
         "properties":{
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string","description":"Admin JWT required."},
           "idea_ids":{"type":"array","items":{"type":"string","format":"uuid"},
                       "minItems":2,"maxItems":500,"description":"List of idea IDs to analyze."},
           "similarity_threshold":{"type":"number","minimum":0.0,"maximum":1.0,"default":0.75}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);

        if (!p.has("idea_ids") || !p.get("idea_ids").isArray()) {
            return err("MISSING_IDEA_IDS", "idea_ids array required", m);
        }
        int count = p.get("idea_ids").size();
        if (count < 2 || count > 500) return err("INVALID_COUNT", "idea_ids must have 2-500 entries", m);

        // Production: Spark batch job computes pairwise cosine similarities
        ObjectNode result = m.createObjectNode();
        result.put("tenantId", tenantId);
        result.put("ideasAnalyzed", count);
        result.put("pairsComputed", (long) count * (count - 1) / 2);
        result.set("similarPairs", m.createArrayNode());
        result.set("clusterGroups", m.createArrayNode());
        result.put("exactDuplicates", 0);
        result.put("semanticDuplicates", 0);
        result.put("relatedIdeas", 0);
        result.put("analysisTimestamp", JsonUtil.now());
        result.put("note", "Production: Spark batch analysis. Scheduled nightly 2 AM UTC. Results cached 30 days.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 9: Get Audit Log
// ════════════════════════════════════════════════════════════════════════════
class GetAuditLogTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Retrieve the complete immutable audit event history for an idea. " +
               "Every event is cryptographically signed and timestamped. " +
               "Includes: CREATED, SUBMITTED, APPROVED, LICENSED, ownership transfers, similarity checks. " +
               "Enables compliance auditors to reconstruct the full idea lifecycle.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "page":{"type":"integer","minimum":1,"default":1},
           "page_size":{"type":"integer","minimum":1,"maximum":200,"default":50},
           "event_types":{"type":"array","items":{"type":"string"},
                          "description":"Filter by event types (optional)."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("")   : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",   "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);

        // Production: SELECT * FROM idea_audit_log WHERE idea_id=? AND tenant_id=? ORDER BY timestamp ASC
        ArrayNode events = m.createArrayNode();
        String[][] sampleEvents = {
            {"IDEA_CREATED", "Idea registered with Idea DNA"},
            {"SIMILARITY_CHECK", "Prior-art search performed, 0 similar ideas found"},
            {"IDEA_SUBMITTED_FOR_APPROVAL", "Submitted for legal review"}
        };
        for (String[] ev : sampleEvents) {
            ObjectNode evt = m.createObjectNode();
            evt.put("event", ev[0]);
            evt.put("description", ev[1]);
            evt.put("timestamp", "2026-03-10T00:00:00Z");
            evt.put("ideaDnaAtTime", "a7b3c8f2e1d9" + "0".repeat(52));
            evt.put("ideaVersionAtTime", 1);
            events.add(evt);
        }

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("tenantId", tenantId);
        result.put("totalEvents", events.size());
        result.set("events", events);
        result.put("storage", "WORM (Write-Once-Read-Many) — immutable, append-only");
        result.put("retention", "7-year legal hold per compliance requirements");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 10: Submit For Approval
// ════════════════════════════════════════════════════════════════════════════
class SubmitForApprovalTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Transition idea from DRAFT to SUBMITTED status and trigger the legal review workflow. " +
               "Automatically triggers legal-document-service to generate IP assignment agreement. " +
               "Publishes idea.submitted_for_approval event to Kafka. " +
               "SLA: 7 business days to APPROVED.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","owner_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "owner_id":{"type":"string","format":"uuid","description":"Must be primary owner."},
           "jwt_token":{"type":"string"},
           "confirm_novelty":{"type":"boolean","default":false,
                             "description":"Owner confirms reviewed similar ideas and confirms novelty."},
           "notes":{"type":"string","maxLength":2000,"description":"Optional notes to legal team."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("") : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        String ownerId  = p.has("owner_id")  ? p.get("owner_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",  "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT",   "Invalid tenant_id", m);
        if (!v.isValidUuid(ownerId))  return err("INVALID_OWNER_ID", "Invalid owner_id", m);

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("previousStatus", "DRAFT");
        result.put("newStatus", "SUBMITTED");
        result.put("submittedAt", JsonUtil.now());
        result.put("legalReviewSlaBusinessDays", 7);
        result.put("kafkaEvent", "idea.submitted_for_approval published");
        result.put("legalDocServiceTriggered", true);
        result.put("ipAgreementStatus", "GENERATION_QUEUED");
        result.put("note", "Legal team notified. IP agreement being auto-generated by legal-document-service.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 11: Approve Idea (Legal Team)
// ════════════════════════════════════════════════════════════════════════════
class ApproveIdeaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Legal team approves an idea after IP review. Transitions status from LEGAL_REVIEW to APPROVED. " +
               "Requires all owners to have signed the IP assignment agreement. " +
               "Publishes idea.approved event, enabling licensing/monetization.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","approver_id","legal_agreement_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "approver_id":{"type":"string","format":"uuid","description":"Legal team member ID."},
           "jwt_token":{"type":"string","description":"Requires LEGAL_TEAM or ADMIN role."},
           "legal_agreement_id":{"type":"string","format":"uuid",
                                 "description":"Signed IP agreement from legal-document-service."},
           "approval_notes":{"type":"string","maxLength":2000}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")             ? p.get("idea_id").asText("") : "";
        String tenantId = p.has("tenant_id")           ? p.get("tenant_id").asText("") : "";
        String approver = p.has("approver_id")         ? p.get("approver_id").asText("") : "";
        String legalId  = p.has("legal_agreement_id")  ? p.get("legal_agreement_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",   "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT",    "Invalid tenant_id", m);
        if (!v.isValidUuid(approver)) return err("INVALID_APPROVER",  "Invalid approver_id", m);
        if (!v.isValidUuid(legalId))  return err("INVALID_LEGAL_ID",  "Invalid legal_agreement_id", m);

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("previousStatus", "LEGAL_REVIEW");
        result.put("newStatus", "APPROVED");
        result.put("approvedAt", JsonUtil.now());
        result.put("approvedBy", approver);
        result.put("legalAgreementId", legalId);
        result.put("kafkaEvent", "idea.approved published → royalty-management-service notified");
        result.put("note", "Idea is now eligible for licensing and monetization.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 12: License Idea
// ════════════════════════════════════════════════════════════════════════════
class LicenseIdeaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Create a licensing agreement for an approved idea. " +
               "Supports EXCLUSIVE, NON_EXCLUSIVE, PERPETUAL, and TERM_LIMITED license types. " +
               "All co-owners must approve. Publishes idea.licensed event to Kafka. " +
               "Triggers royalty-management-service to begin royalty accrual.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","licensee_id","license_type","royalty_rate_percent"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "licensee_id":{"type":"string","format":"uuid","description":"Company/person licensing the idea."},
           "jwt_token":{"type":"string"},
           "license_type":{"type":"string","enum":["EXCLUSIVE","NON_EXCLUSIVE","PERPETUAL","TERM_LIMITED"]},
           "royalty_rate_percent":{"type":"number","minimum":0,"maximum":100,
                                   "description":"Royalty rate as percentage of licensee revenue."},
           "license_start_date":{"type":"string","format":"date"},
           "license_end_date":{"type":"string","format":"date","description":"Null for perpetual."},
           "territory":{"type":"string","description":"Geographic territory for license.","maxLength":100},
           "use_case":{"type":"string","description":"Intended use case.","maxLength":1000}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId     = p.has("idea_id")     ? p.get("idea_id").asText("") : "";
        String tenantId   = p.has("tenant_id")   ? p.get("tenant_id").asText("") : "";
        String licenseeId = p.has("licensee_id") ? p.get("licensee_id").asText("") : "";
        String licType    = p.has("license_type") ? p.get("license_type").asText("") : "";
        double royRate    = p.has("royalty_rate_percent") ? p.get("royalty_rate_percent").asDouble(0) : 0;

        if (!v.isValidUuid(ideaId))     return err("INVALID_IDEA_ID",   "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId))   return err("INVALID_TENANT",    "Invalid tenant_id", m);
        if (!v.isValidUuid(licenseeId)) return err("INVALID_LICENSEE",  "Invalid licensee_id", m);
        String ltErr = v.validateLicenseType(licType);
        if (ltErr != null) return err("INVALID_LICENSE_TYPE", ltErr, m);
        String rrErr = v.validateRoyaltyRate(royRate);
        if (rrErr != null) return err("INVALID_ROYALTY_RATE", rrErr, m);

        String licenseId = JsonUtil.newId();
        ObjectNode result = m.createObjectNode();
        result.put("licenseAgreementId", licenseId);
        result.put("ideaId", ideaId);
        result.put("licenseeId", licenseeId);
        result.put("licenseType", licType);
        result.put("royaltyRatePercent", royRate);
        result.put("status", "DRAFT");
        result.put("createdAt", JsonUtil.now());
        result.put("kafkaEvent", "idea.licensed published upon final approval");
        result.put("royaltyService", "royalty-management-service will begin accrual tracking");
        result.put("note", "All co-owners must approve. Agreement requires legal-document-service signature.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 13: Archive Idea
// ════════════════════════════════════════════════════════════════════════════
class ArchiveIdeaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Archive an idea (transitions to ARCHIVED status). Archived ideas are read-only — " +
               "no new licenses can be created, but existing licenses continue (grandfathered). " +
               "Archive does NOT delete: historical record preserved for compliance. " +
               "Publishes idea.archived event.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","owner_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "owner_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "archive_reason":{"type":"string","maxLength":1000,
                             "description":"Reason for archiving (required)."},
           "preserve_existing_licenses":{"type":"boolean","default":true,
                                         "description":"If true, existing licenses continue."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("") : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        String ownerId  = p.has("owner_id")  ? p.get("owner_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",  "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT",   "Invalid tenant_id", m);
        if (!v.isValidUuid(ownerId))  return err("INVALID_OWNER_ID", "Invalid owner_id", m);

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("newStatus", "ARCHIVED");
        result.put("archivedAt", JsonUtil.now());
        result.put("existingLicensesPreserved", true);
        result.put("kafkaEvent", "idea.archived published");
        result.put("note", "Idea archived. Historical record retained per compliance requirements (7-year hold).");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 14: Verify Idea DNA
// ════════════════════════════════════════════════════════════════════════════
class VerifyIdeaDnaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Verify the Idea DNA fingerprint of an idea to detect tampering. " +
               "Recomputes SHA3-256 hash from current content and compares to stored DNA. " +
               "Mismatch = tamper alert. Also supports historical novelty verification: " +
               "'Was this idea novel at a given date?'";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["tenant_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid","description":"ID of stored idea to verify."},
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "title":{"type":"string","description":"If provided, recompute DNA from this content."},
           "description":{"type":"string"},
           "technical_details":{"type":"string"},
           "category":{"type":"string"},
           "expected_dna":{"type":"string","description":"Expected 64-char hex DNA to compare against.",
                           "pattern":"^[0-9a-fA-F]{64}$"},
           "check_historical_novelty_before":{"type":"string","format":"date-time",
                                              "description":"Check if idea was novel before this timestamp."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);

        ObjectNode result = m.createObjectNode();

        // Compute DNA from provided content
        if (p.has("title") && p.has("description")) {
            String title   = p.get("title").asText("");
            String desc    = p.get("description").asText("");
            String tech    = p.has("technical_details") ? p.get("technical_details").asText("") : "";
            String cat     = p.has("category")          ? p.get("category").asText("other")     : "other";

            String errT = v.validateTitle(title);
            if (errT != null) return err("INVALID_TITLE", errT, m);
            String errD = v.validateDescription(desc);
            if (errD != null) return err("INVALID_DESCRIPTION", errD, m);

            String computed = IdeaDnaUtil.compute(title, desc, tech, cat);
            result.put("computedIdeaDna", computed);
            result.put("algorithm", "SHA3-256");

            if (p.has("expected_dna")) {
                String expected = p.get("expected_dna").asText("");
                boolean match = computed.equalsIgnoreCase(expected);
                result.put("matches", match);
                result.put("tamperDetected", !match);
                if (!match) {
                    result.put("alert", "TAMPER_ALERT: Idea DNA does not match stored fingerprint. Content may have been modified.");
                }
            }
        }

        if (p.has("check_historical_novelty_before")) {
            // Production: query audit_log WHERE idea_dna = ? AND timestamp < ?
            result.put("historicalNoveltyCheck", p.get("check_historical_novelty_before").asText());
            result.put("wasNovel", true);
            result.put("note", "Production: queries idea_audit_log for prior registrations of same DNA.");
        }

        result.put("tenantId", tenantId);
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 15: Get Licensing Status
// ════════════════════════════════════════════════════════════════════════════
class GetLicensingStatusTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Get the full licensing status and monetization summary for an idea. " +
               "Returns all active licenses, royalty earnings, licensing history, " +
               "and owner compensation breakdown per co-owner.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string"},
           "include_royalty_breakdown":{"type":"boolean","default":true},
           "include_license_history":{"type":"boolean","default":false}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId   = p.has("idea_id")   ? p.get("idea_id").asText("") : "";
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(ideaId))   return err("INVALID_IDEA_ID",  "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT", "Invalid tenant_id", m);

        // Production: JOIN ideas + idea_license_agreements + royalty data
        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("tenantId", tenantId);
        result.put("isLicensed", false);
        result.put("licenseCount", 0);
        result.put("totalRoyaltiesEarned", 0.0);
        result.put("currency", "USD");
        result.put("licensingStatus", "AVAILABLE_FOR_LICENSING");
        result.set("activeLicenses", m.createArrayNode());
        result.put("royaltyServiceUrl", "royalty-management-service/ideas/" + ideaId + "/royalties");
        result.put("note", "Production: fetches from idea_license_agreements table joined with royalty data.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 16: Flag Idea (Abuse/Plagiarism)
// ════════════════════════════════════════════════════════════════════════════
class FlagIdeaTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Flag an idea for abuse, plagiarism, or IP violation. Admin/compliance action. " +
               "Moves idea to FLAGGED state (read-only, 30-day investigation window). " +
               "Consumes idea.flagged.for_abuse Kafka event. Notifies all owners.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["idea_id","tenant_id","reporter_id","reason"],
         "properties":{
           "idea_id":{"type":"string","format":"uuid"},
           "tenant_id":{"type":"string","format":"uuid"},
           "reporter_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string","description":"Admin or compliance role required."},
           "reason":{"type":"string","enum":["PLAGIARISM","IP_VIOLATION","ABUSE","SPAM",
                                             "CONFIDENTIALITY_BREACH","HARASSMENT","OTHER"]},
           "evidence":{"type":"string","maxLength":5000,"description":"Evidence details."},
           "similar_idea_id":{"type":"string","format":"uuid",
                              "description":"ID of the idea this allegedly plagiarizes."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String ideaId     = p.has("idea_id")     ? p.get("idea_id").asText("") : "";
        String tenantId   = p.has("tenant_id")   ? p.get("tenant_id").asText("") : "";
        String reporterId = p.has("reporter_id") ? p.get("reporter_id").asText("") : "";
        String reason     = p.has("reason")      ? p.get("reason").asText("") : "";
        if (!v.isValidUuid(ideaId))     return err("INVALID_IDEA_ID",    "Invalid idea_id", m);
        if (!v.isValidUuid(tenantId))   return err("INVALID_TENANT",     "Invalid tenant_id", m);
        if (!v.isValidUuid(reporterId)) return err("INVALID_REPORTER",   "Invalid reporter_id", m);
        if (reason.isBlank())           return err("MISSING_REASON",     "reason is required", m);

        ObjectNode result = m.createObjectNode();
        result.put("ideaId", ideaId);
        result.put("newStatus", "FLAGGED");
        result.put("flaggedAt", JsonUtil.now());
        result.put("reason", reason);
        result.put("investigationWindowDays", 30);
        result.put("kafkaEvent", "idea.flagged.for_abuse consumed and processed");
        result.put("notificationSent", true);
        result.put("note", "Idea is read-only during investigation. Owners notified via notification-service.");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 17: Get Similarity Report
// ════════════════════════════════════════════════════════════════════════════
class GetSimilarityReportTool implements IrsTool {
    private final InputValidator v = new InputValidator();

    @Override public String getDescription() {
        return "Retrieve the latest cached similarity analysis report for a tenant. " +
               "Shows exact duplicates, semantic duplicates, and related idea clusters. " +
               "Reports generated nightly at 2 AM UTC by batch analysis job. " +
               "Useful for detecting spam, coordinated submissions, and idea quality metrics.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object","required":["tenant_id"],
         "properties":{
           "tenant_id":{"type":"string","format":"uuid"},
           "jwt_token":{"type":"string","description":"Admin JWT required."},
           "report_date":{"type":"string","format":"date",
                          "description":"Date of report to retrieve (defaults to latest)."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        String tenantId = p.has("tenant_id") ? p.get("tenant_id").asText("") : "";
        if (!v.isValidUuid(tenantId)) return err("INVALID_TENANT_ID", "Invalid tenant_id", m);

        // Production: SELECT * FROM idea_similarity_reports WHERE tenant_id=? ORDER BY report_timestamp DESC LIMIT 1
        ObjectNode result = m.createObjectNode();
        result.put("tenantId", tenantId);
        result.put("reportTimestamp", "2026-03-20T02:00:00Z");
        result.put("totalIdeasAnalyzed", 0);
        result.put("exactDuplicates", 0);
        result.put("semanticDuplicates", 0);
        result.put("relatedIdeas", 0);
        result.set("duplicatePairs", m.createArrayNode());
        result.put("nextScheduledReport", "2026-03-21T02:00:00Z");
        result.put("batchJobSchedule", "0 2 * * * (daily 2 AM UTC)");
        return ToolResult.success(result);
    }
    private ToolResult err(String code, String msg, ObjectMapper m) {
        return ToolResult.error(JsonUtil.errorResponse(m, code, msg));
    }
}

// ════════════════════════════════════════════════════════════════════════════
// Tool 18: Health Check
// ════════════════════════════════════════════════════════════════════════════
class HealthCheckTool implements IrsTool {
    @Override public String getDescription() {
        return "Service health and readiness check. Verifies connectivity to all dependencies: " +
               "PostgreSQL, Qdrant, Redis, Kafka, MinIO. Returns individual component status " +
               "and overall service health. Used by Kubernetes liveness/readiness probes.";
    }

    @Override public String getInputSchema() {
        return """
        {"type":"object",
         "properties":{
           "include_dependency_checks":{"type":"boolean","default":true,
                                        "description":"Check all downstream dependencies."}
         },"additionalProperties":false}
        """;
    }

    @Override public ToolResult execute(JsonNode p, ObjectMapper m) throws Exception {
        boolean checkDeps = !p.has("include_dependency_checks") ||
                            p.get("include_dependency_checks").asBoolean(true);

        ObjectNode result = m.createObjectNode();
        result.put("status", "healthy");
        result.put("service", "innovation-registry-service");
        result.put("version", "1.0.0");
        result.put("timestamp", JsonUtil.now());
        result.put("mcpProtocol", "2024-11-05");
        result.put("toolsRegistered", 18);

        if (checkDeps) {
            ObjectNode deps = m.createObjectNode();
            // Production: run actual health checks against each dependency
            deps.put("postgresql",    "SIMULATED - Production: SELECT 1 health query");
            deps.put("qdrant",        "SIMULATED - Production: GET /health on Qdrant endpoint");
            deps.put("redis",         "SIMULATED - Production: PING Redis cluster");
            deps.put("kafka",         "SIMULATED - Production: check broker connectivity");
            deps.put("minio",         "SIMULATED - Production: list buckets health check");
            deps.put("legalDocSvc",   "SIMULATED - Production: GET /health on legal-document-service");
            deps.put("royaltySvc",    "SIMULATED - Production: GET /health on royalty-management-service");
            result.set("dependencies", deps);
        }

        ObjectNode slos = m.createObjectNode();
        slos.put("ideaSubmission_p99_ms", 1000);
        slos.put("similaritySearch_p99_ms", 300);
        slos.put("ideaDnaComputation_p99_ms", 50);
        slos.put("availability_target", "99.9%");
        result.set("sloTargets", slos);

        return ToolResult.success(result);
    }
}
