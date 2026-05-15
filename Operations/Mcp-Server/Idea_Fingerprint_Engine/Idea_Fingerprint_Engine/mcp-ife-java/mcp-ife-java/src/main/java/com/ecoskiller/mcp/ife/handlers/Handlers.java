package com.ecoskiller.mcp.ife.handlers;

import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

// ══════════════════════════════════════════════════════════════════════════════
// IndexHandler — Qdrant embedding index management
// ══════════════════════════════════════════════════════════════════════════════

class IndexHandler {

    private static final Logger LOG = Logger.getLogger(IndexHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;
    private final SimilarityHandler similarityHandler;
    private final EmbeddingHandler embeddingHandler;

    IndexHandler(SecurityValidator validator) {
        this.validator = validator;
        this.embeddingHandler = new EmbeddingHandler(validator);
        this.similarityHandler = new SimilarityHandler(validator);
    }

    // ── Tool: index_embedding ─────────────────────────────────────────────────

    public Object indexEmbedding(JsonNode args) {
        String ideaId       = requireString(args, "idea_id");
        String tenantId     = requireString(args, "tenant_id");
        String model        = requireString(args, "embedding_model");
        String category     = args.path("idea_category").asText("general");
        String title        = args.path("idea_title").asText("Untitled");

        // Parse embedding from args
        float[] embedding;
        if (args.has("embedding") && args.get("embedding").isArray()) {
            ArrayNode arr = (ArrayNode) args.get("embedding");
            embedding = new float[arr.size()];
            for (int i = 0; i < arr.size(); i++) embedding[i] = (float) arr.get(i).asDouble();
        } else {
            // Generate if not provided
            embedding = embeddingHandler.generateDeterministicEmbedding(ideaId + title, model);
        }

        // Register in simulated Qdrant
        similarityHandler.registerEmbedding(tenantId, ideaId, title, category, embedding);

        ObjectNode response = mapper.createObjectNode();
        response.put("status",           "INDEXED");
        response.put("idea_id",          ideaId);
        response.put("tenant_id",        tenantId);
        response.put("embedding_model",  model);
        response.put("vector_id",        UUID.randomUUID().toString()); // Qdrant point ID
        response.put("indexed_at",       Instant.now().toString());
        response.put("searchable_within_seconds", 10);
        response.put("collection",       "idea-embeddings");
        response.put("payload_fields",   "idea_id, tenant_id, idea_title, idea_category, embedding_model, timestamp");
        response.put("kafka_note",       "embedding.computed event already published by generate_embedding tool");
        LOG.info("Embedding indexed: idea_id=" + ideaId + " tenant_id=" + tenantId + " model=" + model);
        return response;
    }

    // ── Tool: delete_embedding ────────────────────────────────────────────────

    public Object deleteEmbedding(JsonNode args) {
        String ideaId   = requireString(args, "idea_id");
        String tenantId = requireString(args, "tenant_id");
        String reason   = args.path("reason").asText("NOT_SPECIFIED");

        similarityHandler.removeEmbedding(tenantId, ideaId);

        ObjectNode response = mapper.createObjectNode();
        response.put("status",   "DELETED");
        response.put("idea_id",  ideaId);
        response.put("tenant_id", tenantId);
        response.put("reason",   reason);
        response.put("deleted_at", Instant.now().toString());
        response.put("qdrant_operation", "DELETE (tombstone; compacted during next index maintenance)");
        response.put("gdpr_note",
            reason.contains("GDPR") || reason.contains("ERASURE")
                ? "Embedding removed per GDPR Right to Erasure. Fingerprint archived (anonymized) per 7-year IP retention policy."
                : "Embedding removed from Qdrant index.");
        LOG.info("Embedding deleted: idea_id=" + ideaId + " reason=" + reason);
        return response;
    }

    // ── Tool: reindex_tenant ──────────────────────────────────────────────────

    public Object reindexTenant(JsonNode args) {
        String tenantId = requireString(args, "tenant_id");
        String model    = args.path("embedding_model").asText("all-MiniLM-L6-v2");
        boolean force   = args.path("force").asBoolean(false);

        String jobId = UUID.randomUUID().toString();

        ObjectNode response = mapper.createObjectNode();
        response.put("status",       "QUEUED");
        response.put("job_id",       jobId);
        response.put("tenant_id",    tenantId);
        response.put("operation",    "FULL_TENANT_REINDEX");
        response.put("embedding_model", model);
        response.put("force",        force);
        response.put("queued_at",    Instant.now().toString());
        response.put("estimated_completion_time",
            "Scheduled nightly at 02:00 UTC (Sun) for weekly maintenance. " +
            "Force mode dispatches immediately to batch worker pool (parallelism=30).");
        response.put("polling_hint", "Use get_batch_job_status with job_id: " + jobId);
        response.put("qdrant_ops",   "DELETE all tenant embeddings → re-generate → re-insert (HNSW rebuild)");
        LOG.info("Reindex queued: tenant_id=" + tenantId + " job_id=" + jobId + " force=" + force);
        return response;
    }

    private String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return node.asText().trim();
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// BatchHandler — async batch fingerprinting jobs
// ══════════════════════════════════════════════════════════════════════════════

class BatchHandler {

    private static final Logger LOG = Logger.getLogger(BatchHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;
    private final FingerprintHandler fingerprintHandler;

    // Simulated job store
    private final Map<String, ObjectNode> jobStore = new ConcurrentHashMap<>();

    BatchHandler(SecurityValidator validator) {
        this.validator = validator;
        this.fingerprintHandler = new FingerprintHandler(validator);
    }

    // ── Tool: compute_fingerprint_batch ───────────────────────────────────────

    public Object computeBatch(JsonNode args) {
        String tenantId = requireString(args, "tenant_id");
        String priority = args.path("priority").asText("NORMAL");
        JsonNode ideas  = args.path("ideas");

        int count = ideas.isArray() ? ideas.size() : 0;
        String jobId = UUID.randomUUID().toString();

        // Simulate immediate processing for small batches in demo mode
        int processed = 0;
        int failed = 0;
        if (ideas.isArray()) {
            for (JsonNode idea : ideas) {
                try {
                    // Validate each idea's required fields
                    if (idea.has("idea_id") && idea.has("title") && idea.has("description")) {
                        processed++;
                    } else {
                        failed++;
                    }
                } catch (Exception e) {
                    failed++;
                }
            }
        }

        // Persist job record
        ObjectNode job = mapper.createObjectNode();
        job.put("job_id",          jobId);
        job.put("tenant_id",       tenantId);
        job.put("status",          count <= 10 ? "COMPLETED" : "QUEUED");
        job.put("priority",        priority);
        job.put("ideas_total",     count);
        job.put("ideas_processed", count <= 10 ? processed : 0);
        job.put("ideas_failed",    count <= 10 ? failed : 0);
        job.put("created_at",      Instant.now().toString());
        job.put("estimated_completion_time",
            count <= 10
                ? "Completed synchronously (small batch)"
                : "~" + (count / 5000 + 1) + " hour(s) — dispatched to nightly batch workers (parallelism=20)");
        job.put("batch_size",      count);
        job.put("worker_note",
            "Production: Celery workers with Redis queue. " +
            "Each worker processes 100-idea minibatches for GPU efficiency. " +
            "APScheduler triggers nightly job at 02:00 UTC.");
        jobStore.put(jobId, job);

        ObjectNode response = mapper.createObjectNode();
        response.put("status", count <= 10 ? "COMPLETED" : "ACCEPTED");
        response.put("job_id", jobId);
        response.set("job", job);
        response.put("polling_hint", "Use get_batch_job_status with job_id: " + jobId);
        LOG.info("Batch job created: job_id=" + jobId + " count=" + count + " priority=" + priority);
        return response;
    }

    // ── Tool: get_batch_job_status ─────────────────────────────────────────────

    public Object getBatchJobStatus(JsonNode args) {
        String jobId = requireString(args, "job_id");

        ObjectNode response = mapper.createObjectNode();
        response.put("job_id", jobId);

        if (jobStore.containsKey(jobId)) {
            ObjectNode job = jobStore.get(jobId);
            response.put("found", true);
            response.set("job", job);

            // Simulate progress for QUEUED jobs on successive polls
            String status = job.path("status").asText();
            if ("QUEUED".equals(status) || "RUNNING".equals(status)) {
                int total     = job.path("ideas_total").asInt(0);
                int processed = job.path("ideas_processed").asInt(0);
                int newProcessed = Math.min(total, processed + (total / 4 + 1));
                job.put("ideas_processed", newProcessed);
                job.put("status", newProcessed >= total ? "COMPLETED" : "RUNNING");
                job.put("progress_pct", total > 0 ? (newProcessed * 100 / total) : 100);
            }
        } else {
            response.put("found", false);
            response.put("message",
                "Job not found in session cache. " +
                "In production: queries Redis job store and PostgreSQL batch_jobs table.");
        }
        return response;
    }

    private String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return node.asText().trim();
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// ModelHandler — embedding model registry
// ══════════════════════════════════════════════════════════════════════════════

class ModelHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    // ── Tool: list_embedding_models ───────────────────────────────────────────

    public Object listModels(JsonNode args) {
        boolean inclDeprecated = args != null && args.path("include_deprecated").asBoolean(false);

        ArrayNode models = mapper.createArrayNode();

        // Model 1: Fast baseline
        ObjectNode m1 = models.addObject();
        m1.put("name",           "all-MiniLM-L6-v2");
        m1.put("version",        "v2.0");
        m1.put("status",         "ACTIVE");
        m1.put("dimensions",     384);
        m1.put("latency_cpu_ms", 50);
        m1.put("latency_gpu_ms", 10);
        m1.put("use_case",       "Real-time API queries — balanced speed and quality for general ideas");
        m1.put("training_data",  "General English text corpus (SBERT base)");
        m1.put("ab_testing",     false);
        m1.put("default",        true);

        // Model 2: Domain-specific (technical/software)
        ObjectNode m2 = models.addObject();
        m2.put("name",           "domain-specific-v2");
        m2.put("version",        "v2.2");
        m2.put("status",         "ACTIVE");
        m2.put("dimensions",     384);
        m2.put("latency_cpu_ms", 100);
        m2.put("latency_gpu_ms", 20);
        m2.put("use_case",       "Technical/software/code ideas — fine-tuned on engineering and research content");
        m2.put("training_data",  "Stack Overflow, arXiv CS/EE papers, GitHub READMEs");
        m2.put("ab_testing",     false);
        m2.put("default",        false);

        // Model 3: Domain-agnostic balanced
        ObjectNode m3 = models.addObject();
        m3.put("name",           "domain-agnostic-v3");
        m3.put("version",        "v3.1");
        m3.put("status",         "ACTIVE");
        m3.put("dimensions",     384);
        m3.put("latency_cpu_ms", 75);
        m3.put("latency_gpu_ms", 15);
        m3.put("use_case",       "Multi-domain ideas (business, hardware, research, software) — balanced precision/recall");
        m3.put("training_data",  "Multi-domain corpus: patents, academic papers, business proposals");
        m3.put("ab_testing",     true);
        m3.put("ab_note",        "Currently A/B tested against domain-specific-v2 on new submissions");
        m3.put("default",        false);

        if (inclDeprecated) {
            ObjectNode m0 = models.addObject();
            m0.put("name",    "all-MiniLM-L6-v1");
            m0.put("version", "v1.0");
            m0.put("status",  "DEPRECATED");
            m0.put("dimensions", 384);
            m0.put("latency_cpu_ms", 60);
            m0.put("deprecation_reason", "Replaced by v2.0 with improved multilingual recall");
            m0.put("end_of_life", "2025-06-01");
        }

        ObjectNode response = mapper.createObjectNode();
        response.put("model_count", models.size());
        response.set("models", models);
        response.put("note",
            "In production, model weights are loaded from /models/sentence-bert/ " +
            "volume mounted into the IFE Kubernetes pod. " +
            "GPU inference enabled for batch jobs (Tesla T4, CUDA 12).");
        return response;
    }
}
