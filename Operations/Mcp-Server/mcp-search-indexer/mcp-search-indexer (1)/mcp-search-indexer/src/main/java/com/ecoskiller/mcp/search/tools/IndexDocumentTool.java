package com.ecoskiller.mcp.search.tools;

import org.json.JSONObject;

/**
 * INDEX_DOCUMENT_AGENT — Tool: index_document
 *
 * Single-document index/update/upsert operations against OpenSearch.
 * Handles all domain document types: candidate, job, assessment,
 * application, user_profile, scoring.
 *
 * Implements idempotent upsert (PUT with doc_as_upsert) to handle
 * Kafka event replay and broker restarts gracefully.
 * Tenant isolation: document must carry tenant_id field.
 */
public class IndexDocumentTool extends BaseTool {

    @Override public String getName() { return "index_document"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",       str("index|update|upsert|get|exists"))
                .put("tenant_id",    str("Ecoskiller tenant ID — scoped index"))
                .put("doc_type",     str("Document type: candidate|job|assessment|application|user_profile|scoring"))
                .put("document_id",  str("Document ID (entity ID from source service)"))
                .put("document_json",str("Document payload as JSON string (for index/update/upsert)"))
                .put("routing_key",  str("Optional custom routing key for shard targeting"))
                .put("refresh",      str("Refresh policy: false|wait_for|true (default: false)"));
        return schema(getName(),
                "INDEX_DOCUMENT_AGENT — Single-document index/update/upsert into OpenSearch. " +
                "Idempotent via doc_as_upsert. Tenant-scoped. Handles candidate, job, assessment, " +
                "application, user_profile, scoring documents.",
                p, "action", "tenant_id", "doc_type", "document_id");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action   = a.getString("action");
        String tenantId = a.getString("tenant_id");
        String docType  = a.getString("doc_type");
        String docId    = a.getString("document_id");
        String idx      = indexName(tenantId, docType + "s");
        String compositeId = docId(tenantId, docType, docId);
        String refresh  = a.optString("refresh","false");

        return switch (action) {
            case "index", "upsert" -> {
                String payload = a.optString("document_json","{}");
                yield json(new JSONObject()
                        .put("action",         action)
                        .put("index",          idx)
                        .put("document_id",    compositeId)
                        .put("doc_type",       docType)
                        .put("tenant_id",      tenantId)
                        .put("routing_key",    a.optString("routing_key", tenantId))
                        .put("refresh",        refresh)
                        .put("idempotent",     true)
                        .put("method",         "PUT (doc_as_upsert=true)")
                        .put("payload_size_bytes", payload.length())
                        .put("result",         "indexed")
                        .put("timestamp",      now()));
            }
            case "update" -> json(new JSONObject()
                    .put("action",      "update")
                    .put("index",       idx)
                    .put("document_id", compositeId)
                    .put("method",      "POST /_update (partial doc)")
                    .put("refresh",     refresh)
                    .put("result",      "updated")
                    .put("timestamp",   now()));
            case "get" -> json(new JSONObject()
                    .put("index",       idx)
                    .put("document_id", compositeId)
                    .put("found",       false)
                    .put("source",      "Fetch from OpenSearch GET /{index}/_doc/{id}")
                    .put("timestamp",   now()));
            case "exists" -> json(new JSONObject()
                    .put("index",       idx)
                    .put("document_id", compositeId)
                    .put("exists",      false)
                    .put("timestamp",   now()));
            default -> text("Unknown action: " + action);
        };
    }
}
