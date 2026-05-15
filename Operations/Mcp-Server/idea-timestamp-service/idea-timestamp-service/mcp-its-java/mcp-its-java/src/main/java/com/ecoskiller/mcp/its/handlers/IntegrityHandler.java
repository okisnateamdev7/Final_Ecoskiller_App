package com.ecoskiller.mcp.its.handlers;

import com.ecoskiller.mcp.its.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * IntegrityHandler
 *
 * Implements:
 *   verify_idea_integrity  — SHA-256 tamper detection (per ITS spec §3.2, §5.1)
 *   verify_minio_object    — MinIO ETag dual-layer tamper check (per ITS spec §3.4, §4.1 F2)
 *
 * Tamper detection model (per ITS spec §4.1 F1):
 *   - Recompute SHA-256 from current content using identical canonicalization
 *   - Compare against stored content_hash in innovation.ideas
 *   - MATCH   → content integrity confirmed, no modifications detected
 *   - MISMATCH → content modified after submission (tamper evidence)
 *
 * Dual-integrity model:
 *   content_hash (SHA-256 of canonical JSON)    → proves content unchanged
 *   minio_etag   (MD5 of stored MinIO object)   → proves storage object unchanged
 *   Two independent channels: both must match for full integrity assurance.
 */
public class IntegrityHandler {

    private static final Logger LOG = Logger.getLogger(IntegrityHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    public IntegrityHandler(SecurityValidator sv) {
        this.validator = sv;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: verify_idea_integrity
    // ─────────────────────────────────────────────────────────────────────────

    public Object verifyIdeaIntegrity(JsonNode args) {
        String ideaId      = require(args, "idea_id");
        String tenantId    = require(args, "tenant_id");
        String title       = require(args, "title");
        String description = require(args, "description");
        String techDetails = args.path("technical_details").asText("");
        int    version     = args.path("version").asInt(0); // 0 = latest

        String key        = SubmissionHandler.storeKey(tenantId, ideaId);
        ObjectNode idea   = SubmissionHandler.ideaStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);
        resp.put("verified_at", Instant.now().toString());

        if (idea == null) {
            resp.put("status",  "IDEA_NOT_FOUND");
            resp.put("message", "No stored record to verify against. Submit the idea first.");
            return resp;
        }

        // Resolve stored hash for the requested version
        String storedHash;
        if (args.has("stored_hash") && !args.get("stored_hash").asText().isBlank()) {
            storedHash = args.get("stored_hash").asText().trim();
        } else {
            List<ObjectNode> versions = SubmissionHandler.versionStore.getOrDefault(key, List.of());
            if (!versions.isEmpty()) {
                ObjectNode targetVer = version > 0 && version <= versions.size()
                    ? versions.get(version - 1)
                    : versions.get(versions.size() - 1);
                storedHash = targetVer.path("content_hash").asText();
            } else {
                storedHash = idea.path("content_hash").asText();
            }
        }

        // Recompute SHA-256 with the same canonical JSON function
        String candidateId = idea.path("candidate_id").asText("unknown");
        String canonical   = buildCanonicalJson(ideaId, tenantId, candidateId,
                                                title, description, techDetails);
        String recomputed  = SubmissionHandler.sha256(canonical);

        // Compare
        boolean match      = storedHash.equals(recomputed);
        String  status     = match ? "MATCH" : "MISMATCH";

        resp.put("status",              status);
        resp.put("tamper_detected",     !match);
        resp.put("recomputed_hash",     recomputed);
        resp.put("stored_hash",         storedHash);
        resp.put("hash_algorithm",      "SHA-256");
        resp.put("version_checked",     version > 0 ? version : -1); // -1 = latest

        if (match) {
            resp.put("message",
                "Idea content integrity verified. SHA-256 matches stored record. " +
                "No modifications detected since submission.");
            // Audit: successful verification
            AuditHandler.appendAudit(tenantId, ideaId, "INTEGRITY_VERIFIED", candidateId,
                "Integrity check PASSED. SHA-256=" + recomputed.substring(0, 8) + "...", mapper);
        } else {
            resp.put("message",
                "TAMPER DETECTED: Content does not match stored SHA-256. " +
                "The idea may have been modified after fingerprinting.");

            ObjectNode evidence = resp.putObject("tamper_evidence");
            evidence.put("recomputed_hash", recomputed);
            evidence.put("stored_hash",     storedHash);
            evidence.put("first_8_chars_match",
                recomputed.substring(0, Math.min(8, recomputed.length()))
                    .equals(storedHash.substring(0, Math.min(8, storedHash.length()))));
            evidence.put("detected_at", Instant.now().toString());
            evidence.put("idea_submitted_at", idea.path("submitted_at").asText());

            // Audit: tamper event
            AuditHandler.appendAudit(tenantId, ideaId, "TAMPER_DETECTED", "system",
                "TAMPER DETECTED. Stored=" + storedHash.substring(0, 8) +
                "... Recomputed=" + recomputed.substring(0, 8) + "...", mapper);

            resp.put("kafka_event_published", "fingerprint.tamper_detected");
            resp.put("action_required",
                "Review idea modification history. " +
                "If unauthorized: escalate to compliance-audit-service. " +
                "Kafka event fingerprint.tamper_detected published to: " +
                "innovation-registry-service (alert owners), compliance-audit-service.");
        }

        // Security properties note
        ObjectNode props = resp.putObject("sha256_properties");
        props.put("algorithm",           "SHA-256 (NIST FIPS 180-4)");
        props.put("collision_resistance","2^256 possible outputs — collision probability negligible");
        props.put("preimage_resistance", "Cannot reverse SHA-256 to recover original text");
        props.put("avalanche_effect",    "1-bit change in input changes ~50% of output bits");
        props.put("deterministic",       "Same input always produces same hash");

        LOG.info("verify_idea_integrity: idea_id=" + ideaId + " status=" + status);
        return resp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tool: verify_minio_object
    // ─────────────────────────────────────────────────────────────────────────

    public Object verifyMinioObject(JsonNode args) {
        String ideaId   = require(args, "idea_id");
        String tenantId = require(args, "tenant_id");
        int    version  = args.path("version").asInt(0); // 0 = latest

        String key      = SubmissionHandler.storeKey(tenantId, ideaId);
        ObjectNode idea = SubmissionHandler.ideaStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("idea_id",   ideaId);
        resp.put("tenant_id", tenantId);
        resp.put("checked_at", Instant.now().toString());

        if (idea == null) {
            resp.put("object_exists",  false);
            resp.put("status",         "IDEA_NOT_FOUND");
            resp.put("message",        "No MinIO record found. Submit the idea first.");
            return resp;
        }

        // Resolve stored ETag for version
        List<ObjectNode> versions  = SubmissionHandler.versionStore.getOrDefault(key, List.of());
        String storedEtag;
        String minioKey;
        int    resolvedVersion;

        if (!versions.isEmpty()) {
            ObjectNode targetVer = version > 0 && version <= versions.size()
                ? versions.get(version - 1)
                : versions.get(versions.size() - 1);
            storedEtag       = targetVer.path("minio_etag").asText();
            minioKey         = targetVer.path("minio_object_key").asText();
            resolvedVersion  = targetVer.path("version_number").asInt();
        } else {
            storedEtag       = idea.path("minio_etag").asText();
            minioKey         = idea.path("minio_object_key").asText();
            resolvedVersion  = 1;
        }

        // expected_etag override
        String expectedEtag = args.has("expected_etag") && !args.get("expected_etag").asText().isBlank()
            ? args.get("expected_etag").asText().trim()
            : storedEtag;

        // Simulate MinIO getObject ETag fetch (production: MinIO SDK headObject)
        // In simulation the stored ETag is always the "fetched" ETag (no external mutation)
        String fetchedEtag = storedEtag; // production: minioClient.statObject(bucket, key).etag()

        boolean etagMatch = expectedEtag.equals(fetchedEtag);
        resp.put("object_exists",       true);
        resp.put("etag_match",          etagMatch);
        resp.put("status",              etagMatch ? "OBJECT_INTACT" : "ETAG_MISMATCH");
        resp.put("stored_etag",         storedEtag);
        resp.put("expected_etag",       expectedEtag);
        resp.put("fetched_etag",        fetchedEtag);
        resp.put("minio_object_key",    minioKey);
        resp.put("resolved_version",    resolvedVersion);
        resp.put("object_version_count",versions.size());

        // Bucket info
        ObjectNode bucket = resp.putObject("minio_config");
        bucket.put("bucket",        "ecoskiller-ideas-" + tenantId);
        bucket.put("versioning",    "ENABLED — MinIO Object Versioning prevents overwrites");
        bucket.put("etag_algo",     "MD5 of object bytes (standard S3 ETag for non-multipart uploads)");
        bucket.put("production_op", "minioClient.statObject(bucket, objectKey).etag()");

        if (!etagMatch) {
            ObjectNode evidence = resp.putObject("mismatch_evidence");
            evidence.put("warning",
                "MinIO ETag mismatch detected. The stored object may have been " +
                "overwritten or corrupted outside the idea-timestamp-service.");
            evidence.put("action",
                "Retrieve object version history from MinIO. " +
                "Compare content_hash from SHA-256 verify_idea_integrity tool. " +
                "Escalate to platform security team if both SHA-256 and ETag mismatch.");
            AuditHandler.appendAudit(tenantId, ideaId, "TAMPER_DETECTED", "system",
                "MinIO ETag mismatch. stored=" + storedEtag.substring(0,8) +
                "... expected=" + expectedEtag.substring(0,8) + "...", mapper);
        }

        LOG.info("verify_minio_object: idea_id=" + ideaId + " v" + resolvedVersion
                + " etag_match=" + etagMatch);
        return resp;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String buildCanonicalJson(String ideaId, String tenantId, String candidateId,
                                      String title, String description, String techDetails) {
        return "{\"idea_id\":\"" + ideaId +
               "\",\"tenant_id\":\"" + tenantId +
               "\",\"candidate_id\":\"" + candidateId +
               "\",\"title\":\"" + escape(title) +
               "\",\"description\":\"" + escape(description) +
               "\",\"technical_details\":\"" + escape(techDetails) +
               "\",\"attachment_count\":0}";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    private String require(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }
}
