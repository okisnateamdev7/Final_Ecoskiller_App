package com.ecoskiller.mcp.ife.handlers;

import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.function.Function;

/**
 * ToolRegistry
 *
 * Central registry for all 14 IFE MCP tools.
 * Provides:
 *   - getToolDefinitions() → JSON array for tools/list response
 *   - call(name, args)     → dispatches to the correct handler
 */
public class ToolRegistry {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Function<JsonNode, Object>> handlers;

    public ToolRegistry(SecurityValidator validator) {
        FingerprintHandler   fp  = new FingerprintHandler(validator);
        EmbeddingHandler     emb = new EmbeddingHandler(validator);
        SimilarityHandler    sim = new SimilarityHandler(validator);
        PlagiarismHandler    plg = new PlagiarismHandler(validator);
        IndexHandler         idx = new IndexHandler(validator);
        BatchHandler         bat = new BatchHandler(validator);
        ModelHandler         mod = new ModelHandler();

        this.handlers = Map.ofEntries(
            Map.entry("compute_fingerprint",       fp::computeFingerprint),
            Map.entry("compute_fingerprint_batch", bat::computeBatch),
            Map.entry("generate_embedding",        emb::generateEmbedding),
            Map.entry("similarity_search",         sim::similaritySearch),
            Map.entry("verify_fingerprint",        fp::verifyFingerprint),
            Map.entry("get_fingerprint",           fp::getFingerprint),
            Map.entry("get_similarity_report",     sim::getSimilarityReport),
            Map.entry("detect_plagiarism",         plg::detectPlagiarism),
            Map.entry("index_embedding",           idx::indexEmbedding),
            Map.entry("delete_embedding",          idx::deleteEmbedding),
            Map.entry("reindex_tenant",            idx::reindexTenant),
            Map.entry("get_batch_job_status",      bat::getBatchJobStatus),
            Map.entry("list_embedding_models",     mod::listModels),
            Map.entry("compute_risk_score",        plg::computeRiskScore)
        );
    }

    public Object call(String toolName, JsonNode arguments) {
        Function<JsonNode, Object> handler = handlers.get(toolName);
        if (handler == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "Unknown tool: " + toolName);
            return err;
        }
        return handler.apply(arguments);
    }

    // ─── Tool Definitions (MCP schema) ───────────────────────────────────────

    public ArrayNode getToolDefinitions() {
        ArrayNode tools = mapper.createArrayNode();

        tools.add(tool("compute_fingerprint",
            "Compute SHA-3-256 deterministic fingerprint (Idea DNA) for a single idea. " +
            "Canonicalizes text (lowercase, whitespace, punctuation normalization), hashes with SHA-3-256, " +
            "and returns a 64-char hex fingerprint. Used for exact-duplicate detection and tamper evidence.",
            schema()
                .required("idea_id", "tenant_id", "title", "description")
                .string("idea_id", "UUID of the idea being fingerprinted")
                .string("tenant_id", "Tenant scope UUID")
                .string("title", "Idea title (max 500 chars)")
                .string("description", "Full idea description (max 50 KB)")
                .string("technical_details", "Technical details (optional, max 50 KB)")
                .string("model_version", "IFE model version string, e.g. ife_v2.1 (optional)")
                .build()));

        tools.add(tool("compute_fingerprint_batch",
            "Submit a batch fingerprinting job for up to 500 ideas. Returns a job_id for async tracking. " +
            "Jobs are processed by nightly batch workers (parallelism=20). " +
            "Poll get_batch_job_status with the returned job_id.",
            schema()
                .required("tenant_id", "ideas")
                .string("tenant_id", "Tenant scope UUID")
                .array("ideas", "Array of idea objects: [{idea_id, title, description, technical_details}]")
                .string("priority", "Job priority: LOW | NORMAL | HIGH (default NORMAL)")
                .build()));

        tools.add(tool("generate_embedding",
            "Generate a 384-dimensional Sentence-BERT semantic embedding for idea content. " +
            "Tokenizes up to 512 tokens, passes through the selected embedding model, and returns L2-normalized vector. " +
            "Supports fast (50ms), tuned (100ms), and domain-agnostic (75ms) models.",
            schema()
                .required("idea_id", "tenant_id", "title", "description")
                .string("idea_id", "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .string("title", "Idea title")
                .string("description", "Idea description")
                .string("technical_details", "Additional technical details (optional)")
                .string("embedding_model", "Model: all-MiniLM-L6-v2 | domain-specific-v2 | domain-agnostic-v3 (default: all-MiniLM-L6-v2)")
                .build()));

        tools.add(tool("similarity_search",
            "Execute sub-100ms ANN vector similarity search against the Qdrant idea-embeddings collection. " +
            "Returns top-K nearest neighbors scoped by tenant_id using HNSW cosine similarity. " +
            "Supports threshold filtering (min_similarity) and category-aware filtering.",
            schema()
                .required("tenant_id", "query_idea_id")
                .string("tenant_id", "Tenant scope UUID (search restricted within tenant)")
                .string("query_idea_id", "UUID of the query idea (its stored embedding is used)")
                .integer("top_k", "Number of similar ideas to return (1-50, default 10)")
                .number("min_similarity", "Minimum cosine similarity threshold [0.0-1.0], default 0.40")
                .string("idea_category", "Optional category filter to reduce cross-domain false positives")
                .bool("include_payload", "Include idea metadata in results (default true)")
                .build()));

        tools.add(tool("verify_fingerprint",
            "Verify a stored idea fingerprint matches the current content (tamper detection). " +
            "Recomputes SHA-3-256 from canonical content and compares against PostgreSQL record. " +
            "Returns match/mismatch with delta analysis. Publishes fingerprint.tamper_detected event on mismatch.",
            schema()
                .required("idea_id", "tenant_id", "title", "description")
                .string("idea_id", "UUID of the idea to verify")
                .string("tenant_id", "Tenant scope UUID")
                .string("title", "Current idea title")
                .string("description", "Current idea description")
                .string("technical_details", "Current technical details (optional)")
                .string("stored_fingerprint", "Expected fingerprint for comparison (optional, fetched from DB if omitted)")
                .build()));

        tools.add(tool("get_fingerprint",
            "Retrieve the stored fingerprint record and metadata for an idea by its UUID. " +
            "Returns fingerprint, algorithm, canonicalized_length, vocabulary_size, computed_at, and IFE version.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id", "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .bool("include_history", "Return version history of fingerprints (default false)")
                .build()));

        tools.add(tool("get_similarity_report",
            "Get a full duplicate/plagiarism report for an idea: exact duplicate count, " +
            "semantic duplicates, related ideas, plagiarism risk score, and recommendations. " +
            "Pulls from cached innovation.idea_similarity_reports table.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id", "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .integer("top_matches_limit", "Max similar ideas to return (default 5)")
                .build()));

        tools.add(tool("detect_plagiarism",
            "Run multi-level plagiarism detection pipeline: " +
            "(L1) exact fingerprint match, (L2) semantic near-duplicate (>0.9 similarity), " +
            "(L3) coordinated submission clustering, (L4) user-behavior anomaly detection. " +
            "Returns structured plagiarism report with risk level: CLEAR | REVIEW | FLAG.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id", "UUID of the idea to check")
                .string("tenant_id", "Tenant scope UUID")
                .string("candidate_id", "UUID of submitting candidate (for user behavior analysis)")
                .number("flag_threshold", "Score threshold for FLAG classification (default 0.80)")
                .number("review_threshold", "Score threshold for REVIEW classification (default 0.60)")
                .bool("cross_tenant_check", "Enable anonymized cross-tenant check (default false)")
                .build()));

        tools.add(tool("index_embedding",
            "Upsert an idea's 384-dim embedding into the Qdrant idea-embeddings collection. " +
            "Tenant-scoped: embedding stored with tenant_id payload filter. " +
            "New embeddings become searchable within 10 seconds (real-time indexing SLO).",
            schema()
                .required("idea_id", "tenant_id", "embedding", "embedding_model")
                .string("idea_id", "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .array("embedding", "384-dimensional float32 vector (L2-normalized)")
                .string("embedding_model", "Model name and version, e.g. all-MiniLM-L6-v2-v2.0")
                .string("idea_category", "Semantic category tag for category-aware filtering (optional)")
                .string("idea_title", "Idea title stored as Qdrant payload for retrieval (optional)")
                .build()));

        tools.add(tool("delete_embedding",
            "Remove an idea's embedding from the Qdrant index. " +
            "Called on idea archival or tenant deletion. " +
            "Uses tombstone deletion (Qdrant eventually removes from HNSW graph on compaction).",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id", "UUID of the idea whose embedding should be removed")
                .string("tenant_id", "Tenant scope UUID")
                .string("reason", "Deletion reason: ARCHIVED | TENANT_DELETED | GDPR_ERASURE | ADMIN (optional)")
                .build()));

        tools.add(tool("reindex_tenant",
            "Trigger a full tenant re-index of all idea embeddings in Qdrant. " +
            "Scheduled weekly for index maintenance (defragmentation, consistency check). " +
            "Returns a batch job_id for async tracking. High-priority admin operation.",
            schema()
                .required("tenant_id")
                .string("tenant_id", "Tenant scope UUID to re-index")
                .string("embedding_model", "Target embedding model for re-index (optional, uses current default)")
                .bool("force", "Force re-index even if last run was recent (default false)")
                .build()));

        tools.add(tool("get_batch_job_status",
            "Poll the status of an async batch fingerprinting or embedding job. " +
            "Returns: job_id, status (QUEUED|RUNNING|COMPLETED|FAILED), " +
            "ideas_total, ideas_processed, ideas_failed, estimated_completion_time.",
            schema()
                .required("job_id")
                .string("job_id", "UUID of the batch job returned from compute_fingerprint_batch or reindex_tenant")
                .build()));

        tools.add(tool("list_embedding_models",
            "List all available Sentence-BERT embedding models with their version, " +
            "inference latency (CPU/GPU), intended use case, and availability status. " +
            "Used by clients to select the appropriate model for compute_fingerprint/generate_embedding calls.",
            schema()
                .bool("include_deprecated", "Include deprecated model versions (default false)")
                .build()));

        tools.add(tool("compute_risk_score",
            "Compute the weighted plagiarism risk score for an idea submission. " +
            "Formula: 0.4*exact_match + 0.3*max_semantic_similarity + 0.15*coordinated_submission + " +
            "0.1*user_reputation + 0.05*content_flags. " +
            "Returns score [0-1] and recommended action: ACCEPT | SOFT_FLAG | FLAG_FOR_REVIEW | BLOCK.",
            schema()
                .required("idea_id", "tenant_id")
                .string("idea_id", "UUID of the idea")
                .string("tenant_id", "Tenant scope UUID")
                .bool("exact_match_flag", "Level-1: exact SHA-3 fingerprint match found (default false)")
                .number("max_semantic_similarity", "Level-2: highest cosine similarity score from ANN search [0-1]")
                .bool("coordinated_submission_flag", "Level-3: coordinated submission pattern detected (default false)")
                .number("user_reputation_score", "Level-4: submitting user trust score [0-1], higher = more trusted")
                .number("content_flags", "Level-4: content spam/keyword flag score [0-1]")
                .build()));

        return tools;
    }

    // ─── DSL helpers ─────────────────────────────────────────────────────────

    private ObjectNode tool(String name, String description, ObjectNode inputSchema) {
        ObjectNode t = mapper.createObjectNode();
        t.put("name", name);
        t.put("description", description);
        t.set("inputSchema", inputSchema);
        return t;
    }

    private SchemaBuilder schema() {
        return new SchemaBuilder(mapper);
    }

    /** Fluent JSON Schema builder for tool input schemas. */
    private static class SchemaBuilder {
        private final ObjectMapper mapper;
        private final ObjectNode schema;
        private final ObjectNode props;
        private final ArrayNode required;

        SchemaBuilder(ObjectMapper mapper) {
            this.mapper   = mapper;
            this.schema   = mapper.createObjectNode();
            this.props    = mapper.createObjectNode();
            this.required = mapper.createArrayNode();
            schema.put("type", "object");
            schema.set("properties", props);
        }

        SchemaBuilder required(String... fields) {
            for (String f : fields) required.add(f);
            return this;
        }

        SchemaBuilder string(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "string");
            p.put("description", desc);
            props.set(name, p);
            return this;
        }

        SchemaBuilder integer(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "integer");
            p.put("description", desc);
            props.set(name, p);
            return this;
        }

        SchemaBuilder number(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "number");
            p.put("description", desc);
            props.set(name, p);
            return this;
        }

        SchemaBuilder bool(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "boolean");
            p.put("description", desc);
            props.set(name, p);
            return this;
        }

        SchemaBuilder array(String name, String desc) {
            ObjectNode p = mapper.createObjectNode();
            p.put("type", "array");
            p.put("description", desc);
            props.set(name, p);
            return this;
        }

        ObjectNode build() {
            if (required.size() > 0) schema.set("required", required);
            return schema;
        }
    }
}
