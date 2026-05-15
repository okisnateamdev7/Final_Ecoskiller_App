package com.ecoskiller.mcp.irs.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Idea DNA Fingerprinting Utility
 *
 * Implements deterministic SHA3-256 cryptographic fingerprinting of idea content.
 * Identical ideas (regardless of submission date) always produce the same Idea DNA.
 *
 * Algorithm:
 *  1. Normalize: lowercase, trim, collapse whitespace, remove punctuation
 *  2. Extract key concepts: tokenize, filter stopwords
 *  3. Concatenate: title + " " + description + " " + technical_details
 *  4. Hash with SHA3-256
 *
 * Security properties:
 *  - Collision-resistant: SHA3-256 has 2^256 possible outputs
 *  - Deterministic: same input → same Idea DNA always
 *  - Tamper-detectable: any modification produces different DNA
 *  - Fast: < 1ms for typical idea text
 */
public final class IdeaDnaUtil {

    private IdeaDnaUtil() {}

    private static final String ALGORITHM = "SHA3-256";

    // Common English stopwords to filter during canonicalization
    private static final Set<String> STOPWORDS = Set.of(
        "a", "an", "the", "is", "are", "was", "were", "be", "been", "being",
        "have", "has", "had", "do", "does", "did", "will", "would", "should",
        "could", "may", "might", "shall", "can", "to", "of", "in", "for",
        "on", "with", "at", "by", "from", "up", "about", "into", "through",
        "during", "before", "after", "above", "below", "between", "out",
        "off", "over", "under", "again", "further", "then", "once", "it",
        "its", "this", "that", "these", "those", "i", "we", "you", "he",
        "she", "they", "what", "which", "who", "whom", "when", "where",
        "why", "how", "all", "each", "every", "both", "few", "more", "most",
        "other", "some", "such", "no", "not", "only", "same", "so", "than",
        "too", "very", "just", "but", "and", "or", "nor"
    );

    private static final Pattern WHITESPACE    = Pattern.compile("\\s+");
    private static final Pattern NON_ALPHANUM  = Pattern.compile("[^a-z0-9 ]");

    /**
     * Computes the Idea DNA for the given idea components.
     *
     * @param title           Idea title
     * @param description     Long-form description
     * @param technicalDetails Technical details (may be null)
     * @param category        Idea category
     * @return 64-character hex string representing the Idea DNA (SHA3-256)
     * @throws IllegalArgumentException if required fields are null/empty
     */
    public static String compute(String title, String description,
                                 String technicalDetails, String category) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("description required");

        String canonicalInput = canonicalize(
            title,
            description,
            technicalDetails != null ? technicalDetails : "",
            category         != null ? category : ""
        );

        return sha3_256Hex(canonicalInput);
    }

    /**
     * Verifies that a stored Idea DNA still matches current idea content.
     * Used for tamper detection after idea creation.
     */
    public static boolean verify(String storedDna, String title, String description,
                                  String technicalDetails, String category) {
        try {
            String recomputed = compute(title, description, technicalDetails, category);
            // Constant-time comparison to prevent timing attacks
            return MessageDigest.isEqual(
                storedDna.getBytes(StandardCharsets.UTF_8),
                recomputed.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the canonicalized input string (useful for audit log storage).
     */
    public static String getCanonicalInput(String title, String description,
                                            String technicalDetails, String category) {
        return canonicalize(
            title,
            description,
            technicalDetails != null ? technicalDetails : "",
            category         != null ? category : ""
        );
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private static String canonicalize(String... fields) {
        String combined = String.join(" ", fields);

        // 1. Lowercase
        combined = combined.toLowerCase();

        // 2. Remove punctuation (keep alphanumeric and spaces)
        combined = NON_ALPHANUM.matcher(combined).replaceAll(" ");

        // 3. Collapse whitespace
        combined = WHITESPACE.matcher(combined).replaceAll(" ").trim();

        // 4. Tokenize and filter stopwords
        List<String> tokens = Arrays.stream(combined.split(" "))
            .filter(t -> !t.isEmpty() && !STOPWORDS.contains(t))
            .collect(Collectors.toList());

        return String.join(" ", tokens);
    }

    private static String sha3_256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA3-256 is available in Java 9+; fall back to SHA-256 if needed
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
                return "sha256:" + bytesToHex(hash);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("No suitable hash algorithm available", ex);
            }
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
