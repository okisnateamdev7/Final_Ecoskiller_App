package com.ecoskiller.mcp.its.handlers;

import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * LifecycleHandler
 *
 * Implements:
 *   get_submission_status — Current lifecycle status of an idea
 *   set_visibility        — Update PRIVATE | INTERNAL | MARKETPLACE
 *   archive_idea          — Soft-archive (immutable record kept; 7-year IP retention)
 *
 * Status state machine (per ITS spec §3.1 and §6.1):
 *
 *   PENDING_FINGERPRINT
 *      ↓ (idea-dna-fingerprint-engine + idea-similarity-detector process idea.submitted)
 *   VERIFIED_ORIGINAL  ← CLEAR classification from similarity detector
 *   UNDER_REVIEW       ← REVIEW classification (0.60–0.79 copy probability)
 *   FLAGGED            ← FLAG classification (≥0.80 copy probability)
 *      ↓ (licensing-contract-service initiates contract)
 *   LICENSED
 *      ↓ (owner requests or admin archives)
 *   ARCHIVED           ← soft-delete; immutable; existing licenses grandfathered
 */
public class LifecycleHandler {

    private static final Logger LOG = Logger.getLogger(LifecycleHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    public LifecycleHandler(SecurityValidator sv) {
        this.validator = sv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: get_submission_status
    // ─────────────────────────────────────────────────────────────────────────

    public Object getSubmissionStatus(JsonNode args) {
        String ideaId   = require(args, "idea_id");
        String tenantId = require(args, "tenant_id");
        String key      = SubmissionHandler.storeKey(tenantId, ideaId);
        ObjectNode idea = SubmissionHandler.ideaStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);

        if (idea == null) {
            resp.put("found",   false);
            resp.put("message", "Idea not found. In production: SELECT status FROM innovation.ideas WHERE idea_id=? AND tenant_id=?");
            return resp;
        }

        String status = idea.path("status").asText("PENDING_FINGERPRINT");
        resp.put("found",            true);
        resp.put("status",           status);
        resp.put("visibility_level", idea.path("visibility_level").asText());
        resp.put("current_version",  idea.path("version").asInt(1));
        resp.put("submitted_at",     idea.path("submitted_at").asText());
        resp.put("idea_category",    idea.path("idea_category").asText());
        resp.put("candidate_id",     idea.path("candidate_id").asText());

        // Human-readable explanation + next steps
        ObjectNode stateInfo = resp.putObject("status_detail");
        switch (status) {
            case "PENDING_FINGERPRINT" -> {
                stateInfo.put("description",
                    "Kafka event idea.submitted published. " +
                    "Waiting for idea-dna-fingerprint-engine to compute embedding and similarity check.");
                stateInfo.put("next_service",   "idea-dna-fingerprint-engine → idea-similarity-detector");
                stateInfo.put("next_action",    "No action required. Status auto-updates on Kafka response.");
                stateInfo.put("typical_latency","< 60 seconds for similarity check completion");
            }
            case "VERIFIED_ORIGINAL" -> {
                stateInfo.put("description",
                    "Similarity check passed (CLEAR tier: copy_probability < 0.60). " +
                    "Idea is verified as sufficiently original.");
                stateInfo.put("eligible_for_licensing", true);
                stateInfo.put("marketplace_eligible",
                    "MARKETPLACE".equals(idea.path("visibility_level").asText()));
                stateInfo.put("next_action",
                    "Idea is active. Can be listed on marketplace (if MARKETPLACE visibility) " +
                    "or submitted for licensing via licensing-contract-service (Module XIII).");
            }
            case "UNDER_REVIEW" -> {
                stateInfo.put("description",
                    "Similarity check raised REVIEW tier (copy_probability 0.60–0.79). " +
                    "Queued for human review by compliance officer within 24h.");
                stateInfo.put("eligible_for_licensing", false);
                stateInfo.put("candidate_notification",
                    "Candidate notified: 'Submission under review due to similarity concerns.'");
                stateInfo.put("next_action",
                    "Await compliance officer decision. Status will change to " +
                    "VERIFIED_ORIGINAL (approved) or FLAGGED (rejected).");
            }
            case "FLAGGED" -> {
                stateInfo.put("description",
                    "Similarity check raised FLAG tier (copy_probability ≥ 0.80). " +
                    "Plagiarism investigation opened.");
                stateInfo.put("eligible_for_licensing", false);
                stateInfo.put("candidate_action",
                    "Submit an appeal via dispute-service with explanation. " +
                    "3 strikes policy applies. Compliance team reviews within 24h.");
                stateInfo.put("kafka_event", "idea.similarity.flagged published to compliance-audit-service");
            }
            case "LICENSED" -> {
                stateInfo.put("description",
                    "Active licensing contract exists for this idea via Module XIII.");
                stateInfo.put("worm_locked", true);
                stateInfo.put("worm_note",
                    "MinIO Object Lock (governance retention) applied by licensing-contract-service. " +
                    "Object cannot be deleted for contract duration.");
                stateInfo.put("next_action", "Royalty accounting is active in royalty-accounting-engine.");
            }
            case "ARCHIVED" -> {
                stateInfo.put("description",
                    "Idea soft-archived. Read-only; no new licenses accepted.");
                stateInfo.put("existing_licenses_status", "Grandfathered — not terminated");
                stateInfo.put("record_retention",
                    "Fingerprints and audit records retained 7 years (IP compliance). " +
                    "MinIO object preserved. PostgreSQL soft-archive only (no DELETE).");
                stateInfo.put("reactivation", "Contact platform admin to un-archive.");
            }
            default -> stateInfo.put("description", "Unknown status: " + status);
        }

        LOG.info("get_submission_status: idea_id=" + ideaId + " status=" + status);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: set_visibility
    // ─────────────────────────────────────────────────────────────────────────

    public Object setVisibility(JsonNode args) {
        String ideaId      = require(args, "idea_id");
        String tenantId    = require(args, "tenant_id");
        String candidateId = require(args, "candidate_id");
        String newVis      = require(args, "visibility_level").toUpperCase();

        String key      = SubmissionHandler.storeKey(tenantId, ideaId);
        ObjectNode idea = SubmissionHandler.ideaStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);

        if (idea == null) {
            resp.put("success", false);
            resp.put("error",   "IDEA_NOT_FOUND");
            resp.put("message", "No idea found for idea_id=" + ideaId);
            return resp;
        }

        // Ownership check
        String owner = idea.path("candidate_id").asText();
        if (!owner.equals(candidateId)) {
            resp.put("success", false);
            resp.put("error",   "UNAUTHORIZED");
            resp.put("message", "Only the original candidate (" + owner
                + ") may change visibility. Admin override requires admin JWT role.");
            return resp;
        }

        // Archive check
        if ("ARCHIVED".equals(idea.path("status").asText())) {
            resp.put("success", false);
            resp.put("error",   "IDEA_ARCHIVED");
            resp.put("message", "Cannot change visibility of an archived idea.");
            return resp;
        }

        String prevVis = idea.path("visibility_level").asText();
        idea.put("visibility_level", newVis);

        // Kafka + search indexer implications
        ObjectNode implications = resp.putObject("downstream_effects");
        if ("MARKETPLACE".equals(newVis) && !"MARKETPLACE".equals(prevVis)) {
            implications.put("search_indexer",
                "search-indexer will ADD idea to OpenSearch ecoskiller-" + tenantId +
                "-innovations index (MARKETPLACE ideas are recruiter-discoverable)");
            implications.put("kafka_consumers", "notification-service will send marketplace listing notification");
        } else if (!"MARKETPLACE".equals(newVis) && "MARKETPLACE".equals(prevVis)) {
            implications.put("search_indexer",
                "search-indexer will REMOVE idea from OpenSearch index (no longer marketplace-discoverable)");
        } else {
            implications.put("search_indexer", "No indexing change required");
        }
        implications.put("content_hash_unchanged", true);
        implications.put("submitted_at_unchanged", true);

        // Audit
        AuditHandler.appendAudit(tenantId, ideaId, "VISIBILITY_CHANGED", candidateId,
            "Visibility changed: " + prevVis + " → " + newVis, mapper);

        resp.put("success",        true);
        resp.put("previous_visibility", prevVis);
        resp.put("new_visibility", newVis);
        resp.put("changed_at",     Instant.now().toString());
        resp.put("immutable_note", "submitted_at and content_hash are unchanged by visibility updates");

        LOG.info("set_visibility: idea_id=" + ideaId + " " + prevVis + " → " + newVis);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: archive_idea
    // ─────────────────────────────────────────────────────────────────────────

    public Object archiveIdea(JsonNode args) {
        String ideaId      = require(args, "idea_id");
        String tenantId    = require(args, "tenant_id");
        String candidateId = require(args, "candidate_id");
        String reason      = args.path("archive_reason").asText("");

        String key      = SubmissionHandler.storeKey(tenantId, ideaId);
        ObjectNode idea = SubmissionHandler.ideaStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);

        if (idea == null) {
            resp.put("success", false);
            resp.put("error",   "IDEA_NOT_FOUND");
            return resp;
        }

        // Ownership check
        String owner = idea.path("candidate_id").asText();
        if (!owner.equals(candidateId)) {
            resp.put("success", false);
            resp.put("error",   "UNAUTHORIZED");
            resp.put("message", "Only the original candidate (" + owner + ") may archive this idea.");
            return resp;
        }

        // Already archived?
        if ("ARCHIVED".equals(idea.path("status").asText())) {
            resp.put("success", false);
            resp.put("error",   "ALREADY_ARCHIVED");
            resp.put("message", "Idea is already archived.");
            return resp;
        }

        String prevStatus = idea.path("status").asText();
        idea.put("status",      "ARCHIVED");
        idea.put("archived_at", Instant.now().toString());
        if (!reason.isBlank()) idea.put("archive_reason", reason);

        // Audit
        AuditHandler.appendAudit(tenantId, ideaId, "ARCHIVED", candidateId,
            "Idea archived. Previous status: " + prevStatus
            + (reason.isBlank() ? "" : ". Reason: " + reason), mapper);

        resp.put("success",           true);
        resp.put("new_status",        "ARCHIVED");
        resp.put("archived_at",       idea.path("archived_at").asText());
        resp.put("previous_status",   prevStatus);
        resp.put("archive_reason",    reason);

        ObjectNode retention = resp.putObject("retention_policy");
        retention.put("record_kept",     true);
        retention.put("delete_allowed",  false);
        retention.put("fingerprint_retention", "7 years (IP law compliance)");
        retention.put("audit_log_retention",   "3 years (platform compliance)");
        retention.put("minio_object",    "Preserved (not deleted — immutable archive)");
        retention.put("existing_licenses","Grandfathered — active contracts not terminated");
        retention.put("search_index",    "idea-timestamp-service removes idea from OpenSearch marketplace index");
        retention.put("note",
            "Ecoskiller never hard-deletes IP records. ARCHIVED = soft-delete. " +
            "PostgreSQL row remains; status=ARCHIVED prevents new licensing.");

        LOG.info("archive_idea: idea_id=" + ideaId + " prev=" + prevStatus);
        return resp;
    }

    private String require(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
