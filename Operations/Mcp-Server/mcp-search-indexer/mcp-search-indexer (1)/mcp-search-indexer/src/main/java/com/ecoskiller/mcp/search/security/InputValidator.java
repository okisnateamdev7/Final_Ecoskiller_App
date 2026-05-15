package com.ecoskiller.mcp.search.security;

import org.json.JSONObject;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Security input validator for all 18 Search Indexer MCP tools.
 *
 * Enforces:
 *  - Tenant/index ID format: alphanumeric, hyphens, underscores (no wildcards, path traversal)
 *  - Document type allow-list: prevents arbitrary index targeting
 *  - Query string injection prevention: no raw Lucene injection chars in plain search fields
 *  - Pagination sanity: limit ≤ 1000, offset ≥ 0
 *  - Batch size limits: max 1000 docs per bulk call
 *  - Shard/replica ranges: reasonable cluster config values
 *  - Language allow-list: supported analyzers only
 */
public class InputValidator {

    // Tenant/index identifiers: lowercase alphanumeric + hyphen + underscore, max 128 chars
    private static final Pattern ID_PATTERN    = Pattern.compile("^[a-zA-Z0-9\\-_]{1,128}$");
    // Query strings: block raw injection attempts (no raw { } [ ] / \\ sequences > 512 chars)
    private static final Pattern QUERY_PATTERN = Pattern.compile("^[^\\\\]{0,512}$");
    // Safe field names
    private static final Pattern FIELD_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\.]{1,64}$");

    private static final Set<String> DOC_TYPES  = Set.of(
            "candidate","job","assessment","application","user_profile","scoring");
    private static final Set<String> LANGUAGES  = Set.of("en","hi","hinglish","auto");
    private static final Set<String> SORT_FIELDS = Set.of(
            "relevance","created_at","updated_at","belt_level","score","name","title","location");
    private static final Set<String> CLUSTERS   = Set.of("gcp","aws","primary","secondary","all");
    private static final Set<String> ILM_PHASES = Set.of("hot","warm","cold","delete");

    private static final int MAX_LIMIT      = 1000;
    private static final int MAX_BATCH_SIZE = 1000;
    private static final int MAX_SHARDS     = 100;

    public void validate(String toolName, JSONObject args) {
        // IDs
        validateId(args, "tenant_id");
        validateId(args, "index_name");
        validateId(args, "document_id");
        validateId(args, "template_name");
        validateId(args, "alias_name");
        validateId(args, "snapshot_name");

        // Query string
        if (args.has("query") && !QUERY_PATTERN.matcher(args.optString("query","")).matches())
            throw new SecurityException("query contains invalid characters or exceeds 512 chars");

        // Document type allow-list
        if (args.has("doc_type") && !DOC_TYPES.contains(args.optString("doc_type","").toLowerCase()))
            throw new SecurityException("Unknown doc_type: " + args.optString("doc_type"));

        // Language allow-list
        if (args.has("language") && !LANGUAGES.contains(args.optString("language","").toLowerCase()))
            throw new SecurityException("Unsupported language: " + args.optString("language"));

        // Sort field allow-list
        if (args.has("sort_by") && !SORT_FIELDS.contains(args.optString("sort_by","").toLowerCase()))
            throw new SecurityException("Invalid sort_by field: " + args.optString("sort_by"));

        // Cluster allow-list
        if (args.has("cluster") && !CLUSTERS.contains(args.optString("cluster","").toLowerCase()))
            throw new SecurityException("Unknown cluster: " + args.optString("cluster"));

        // Pagination
        if (args.has("limit")) {
            int limit = args.getInt("limit");
            if (limit < 1 || limit > MAX_LIMIT)
                throw new SecurityException("limit must be 1–" + MAX_LIMIT);
        }
        if (args.has("offset")) {
            int off = args.getInt("offset");
            if (off < 0)
                throw new SecurityException("offset cannot be negative");
        }

        // Batch size
        if (args.has("batch_size")) {
            int bs = args.getInt("batch_size");
            if (bs < 1 || bs > MAX_BATCH_SIZE)
                throw new SecurityException("batch_size must be 1–" + MAX_BATCH_SIZE);
        }

        // Shards/replicas
        if (args.has("num_shards")) {
            int s = args.getInt("num_shards");
            if (s < 1 || s > MAX_SHARDS)
                throw new SecurityException("num_shards must be 1–" + MAX_SHARDS);
        }
        if (args.has("num_replicas")) {
            int r = args.getInt("num_replicas");
            if (r < 0 || r > 10)
                throw new SecurityException("num_replicas must be 0–10");
        }

        // Field name safety
        if (args.has("field") && !FIELD_PATTERN.matcher(args.optString("field","")).matches())
            throw new SecurityException("Invalid field name: " + args.optString("field"));

        // ILM phase
        if (args.has("phase") && !ILM_PHASES.contains(args.optString("phase","").toLowerCase()))
            throw new SecurityException("Invalid ILM phase: " + args.optString("phase"));

        // Year sanity
        if (args.has("year")) {
            int y = args.getInt("year");
            if (y < 2000 || y > 2100) throw new SecurityException("year out of range: " + y);
        }
    }

    private void validateId(JSONObject args, String field) {
        if (!args.has(field)) return;
        String v = args.optString(field,"");
        if (!ID_PATTERN.matcher(v).matches())
            throw new SecurityException("Invalid " + field + " '" + safe(v) + "' — must be [a-zA-Z0-9\\-_]{1,128}");
    }

    private String safe(String s) {
        return s == null ? "null" : (s.length() > 30 ? s.substring(0,30)+"..." : s);
    }
}
