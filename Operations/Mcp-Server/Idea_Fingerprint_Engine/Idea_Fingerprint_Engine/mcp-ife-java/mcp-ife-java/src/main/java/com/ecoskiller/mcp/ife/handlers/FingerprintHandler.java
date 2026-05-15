package com.ecoskiller.mcp.ife.handlers;

import com.ecoskiller.mcp.ife.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * FingerprintHandler
 *
 * Implements:
 *   - compute_fingerprint  → SHA-3-256 deterministic Idea DNA
 *   - verify_fingerprint   → Tamper detection
 *   - get_fingerprint      → Retrieve stored record (simulated)
 *
 * Algorithm (per IFE spec §10):
 *   1. Canonicalize: trim, lowercase, collapse whitespace, remove punctuation, NFC-normalize
 *   2. Concatenate: title + " " + description + " " + technical_details
 *   3. SHA3-256(UTF-8 bytes) → 64-char hex
 */
public class FingerprintHandler {

    private static final Logger LOG = Logger.getLogger(FingerprintHandler.class.getName());

    private static final String ALGO = "SHA3-256";
    private static final String IFE_VERSION = "ife_v2.1";

    // Punctuation characters to strip (per IFE spec §10)
    private static final Pattern PUNCT_PATTERN = Pattern.compile("[^a-z0-9\\s]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    // Simulated in-memory fingerprint store (production: PostgreSQL)
    private final Map<String, ObjectNode> fingerprintStore = new HashMap<>();

    public FingerprintHandler(SecurityValidator validator) {
        this.validator = validator;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: compute_fingerprint
    // ──────────────────────────────────────────────────────────────────────────

    public Object computeFingerprint(JsonNode args) {
        String ideaId    = requireString(args, "idea_id");
        String tenantId  = requireString(args, "tenant_id");
        String title     = requireString(args, "title");
        String desc      = requireString(args, "description");
        String techDet   = args.path("technical_details").asText("");
        String modelVer  = args.path("model_version").asText(IFE_VERSION);

        // Step 1-3: Canonicalize and hash
        String canonical = canonicalize(title + " " + desc + " " + techDet);
        String fingerprint = sha3_256(canonical);

        // Metadata extraction
        int originalLength     = (title + " " + desc + " " + techDet).length();
        int canonicalizedLength = canonical.length();
        long vocabularySize    = countUniqueWords(canonical);

        // Persist to simulated store
        ObjectNode record = mapper.createObjectNode();
        record.put("idea_id",                ideaId);
        record.put("tenant_id",              tenantId);
        record.put("fingerprint",            fingerprint);
        record.put("fingerprint_algorithm",  ALGO);
        record.put("content_hash",           fingerprint);
        record.put("original_length",        originalLength);
        record.put("canonicalized_length",   canonicalizedLength);
        record.put("vocabulary_size",        vocabularySize);
        record.put("language",               "en");
        record.put("computed_at",            Instant.now().toString());
        record.put("computed_version",       modelVer);
        record.put("duplicate_detected",     fingerprintExists(tenantId, fingerprint, ideaId));
        fingerprintStore.put(storeKey(tenantId, ideaId), record);

        ObjectNode response = mapper.createObjectNode();
        response.put("status", "success");
        response.set("fingerprint_record", record);
        response.put("kafka_event_published", "fingerprint.computed");
        LOG.info("Fingerprint computed for idea_id=" + ideaId + " tenant_id=" + tenantId);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: verify_fingerprint
    // ──────────────────────────────────────────────────────────────────────────

    public Object verifyFingerprint(JsonNode args) {
        String ideaId   = requireString(args, "idea_id");
        String tenantId = requireString(args, "tenant_id");
        String title    = requireString(args, "title");
        String desc     = requireString(args, "description");
        String techDet  = args.path("technical_details").asText("");

        String recomputed = sha3_256(canonicalize(title + " " + desc + " " + techDet));
        String key = storeKey(tenantId, ideaId);

        // Compare against stored fingerprint
        String storedFp = args.has("stored_fingerprint")
            ? args.get("stored_fingerprint").asText()
            : (fingerprintStore.containsKey(key) ? fingerprintStore.get(key).get("fingerprint").asText() : null);

        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id", ideaId);
        response.put("tenant_id", tenantId);
        response.put("recomputed_fingerprint", recomputed);

        if (storedFp == null) {
            response.put("status", "NO_STORED_FINGERPRINT");
            response.put("message", "No fingerprint found for this idea_id in the current session store.");
        } else if (storedFp.equals(recomputed)) {
            response.put("status", "MATCH");
            response.put("tamper_detected", false);
            response.put("message", "Idea content integrity verified — fingerprint matches stored record.");
        } else {
            response.put("status", "MISMATCH");
            response.put("tamper_detected", true);
            response.put("stored_fingerprint", storedFp);
            response.put("message", "TAMPER DETECTED: Content has been modified since fingerprinting.");
            response.put("kafka_event_published", "fingerprint.tamper_detected");
            LOG.warning("Tamper detected for idea_id=" + ideaId + " tenant_id=" + tenantId);
        }
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Tool: get_fingerprint
    // ──────────────────────────────────────────────────────────────────────────

    public Object getFingerprint(JsonNode args) {
        String ideaId        = requireString(args, "idea_id");
        String tenantId      = requireString(args, "tenant_id");
        boolean inclHistory  = args.path("include_history").asBoolean(false);

        String key = storeKey(tenantId, ideaId);
        ObjectNode response = mapper.createObjectNode();
        response.put("idea_id", ideaId);
        response.put("tenant_id", tenantId);

        if (fingerprintStore.containsKey(key)) {
            response.put("found", true);
            response.set("fingerprint_record", fingerprintStore.get(key));
        } else {
            response.put("found", false);
            response.put("message",
                "Fingerprint not found in session cache. " +
                "In production, this queries PostgreSQL innovation.fingerprints table.");
        }

        if (inclHistory) {
            response.put("version_history_note",
                "In production, returns array of {fingerprint, computed_at, model_version} " +
                "for all historical versions stored in PostgreSQL.");
        }
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Internal helpers
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Canonicalization per IFE spec §10:
     * 1. Trim leading/trailing whitespace
     * 2. Lowercase
     * 3. Remove punctuation (keep alphanumeric + spaces)
     * 4. Collapse multiple spaces to single space
     */
    private String canonicalize(String input) {
        if (input == null) return "";
        String s = input.trim();
        s = s.toLowerCase(Locale.ROOT);
        // Normalize unicode (basic approximation — full NFC requires ICU4J in production)
        s = s.replaceAll("[àáâãäå]", "a").replaceAll("[èéêë]", "e")
             .replaceAll("[ìíîï]", "i").replaceAll("[òóôõö]", "o")
             .replaceAll("[ùúûü]", "u").replaceAll("[ñ]", "n")
             .replaceAll("[ç]", "c");
        s = PUNCT_PATTERN.matcher(s).replaceAll("");
        s = WHITESPACE_PATTERN.matcher(s).replaceAll(" ").trim();
        return s;
    }

    /** SHA3-256 → 64-char lowercase hex string. */
    private String sha3_256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGO);
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(64);
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA3-256 not available in JVM", e);
        }
    }

    /** Count distinct words after canonicalization. */
    private long countUniqueWords(String canonical) {
        if (canonical.isBlank()) return 0;
        return Arrays.stream(canonical.split("\\s+"))
                     .filter(w -> !w.isBlank())
                     .distinct()
                     .count();
    }

    /** Check if a fingerprint already exists for a different idea (duplicate detection). */
    private boolean fingerprintExists(String tenantId, String fingerprint, String currentIdeaId) {
        return fingerprintStore.entrySet().stream()
            .anyMatch(e -> {
                JsonNode rec = e.getValue();
                return e.getKey().startsWith(tenantId + ":")
                    && fingerprint.equals(rec.path("fingerprint").asText())
                    && !currentIdeaId.equals(rec.path("idea_id").asText());
            });
    }

    private String storeKey(String tenantId, String ideaId) {
        return tenantId + ":" + ideaId;
    }

    private String requireString(JsonNode args, String field) {
        JsonNode node = args.get(field);
        if (node == null || node.isNull() || node.asText().isBlank()) {
            throw new IllegalArgumentException("Missing required field: " + field);
        }
        return node.asText().trim();
    }
}
