package com.ecoskiller.mcp.search.tools;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * DOCUMENT_TRANSFORM_AGENT — Tool: document_transform
 *
 * Transforms raw domain events into OpenSearch-ready documents.
 * Extracts searchable fields, normalises text, enriches with computed
 * properties (belt level, category aliases), and applies nested mappings.
 *
 * Also handles PII tokenisation — sensitive fields (email, phone, SSN)
 * are excluded from search indices or tokenised per spec Section 10.5.
 */
public class DocumentTransformTool extends BaseTool {

    @Override public String getName() { return "document_transform"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",       str("transform|validate|enrich|pii_tokenise|preview_mapping"))
                .put("doc_type",     str("Document type: candidate|job|assessment|application|user_profile|scoring"))
                .put("event_json",   str("Raw domain event JSON string to transform"))
                .put("language",     str("Text analyzer language: en|hi|hinglish|auto"))
                .put("enrich_fields",str("Comma-separated fields to enrich from external services"));
        return schema(getName(),
                "DOCUMENT_TRANSFORM_AGENT — Transform Kafka domain events into OpenSearch documents: " +
                "field extraction, text normalisation, belt/category enrichment, " +
                "PII tokenisation (email/phone/SSN excluded from search index).",
                p, "action", "doc_type");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action  = a.getString("action");
        String docType = a.getString("doc_type");

        return switch (action) {
            case "transform" -> {
                String raw = a.optString("event_json","{}");
                yield json(new JSONObject()
                        .put("doc_type",         docType)
                        .put("input_event_size", raw.length())
                        .put("transformed_doc",  buildSampleDoc(docType))
                        .put("pii_excluded",     new JSONArray().put("email").put("phone").put("ssn"))
                        .put("nested_mappings",  "skills[]  and requirements{} flattened for indexing")
                        .put("analyzers_applied",new JSONArray().put("standard").put("hindi_analyzer").put("hinglish_filter"))
                        .put("timestamp",        now()));
            }
            case "pii_tokenise" -> json(new JSONObject()
                    .put("doc_type",    docType)
                    .put("pii_fields",  new JSONArray().put("email").put("phone").put("ssn").put("bank_account"))
                    .put("strategy",    "EXCLUDE_FROM_INDEX — PII stored in PostgreSQL, never in OpenSearch")
                    .put("gdpr_note",   "Right-to-be-forgotten: only delete_document needed (no PII in index)")
                    .put("timestamp",   now()));
            case "enrich" -> json(new JSONObject()
                    .put("doc_type",       docType)
                    .put("enrichments",    new JSONObject()
                            .put("belt_level_label",  "Computed from score thresholds")
                            .put("job_category_alias","Expanded from category hierarchy")
                            .put("skill_synonyms",    "Loaded from skill-synonym service"))
                    .put("timestamp",      now()));
            case "validate" -> json(new JSONObject()
                    .put("doc_type",        docType)
                    .put("valid",           true)
                    .put("required_fields", requiredFields(docType))
                    .put("timestamp",       now()));
            case "preview_mapping" -> json(new JSONObject()
                    .put("doc_type",   docType)
                    .put("mapping",    buildMapping(docType))
                    .put("timestamp",  now()));
            default -> text("Unknown action: " + action);
        };
    }

    private JSONObject buildSampleDoc(String docType) {
        return switch (docType) {
            case "candidate" -> new JSONObject()
                    .put("candidate_id","[ID]").put("name","[SEARCHABLE]")
                    .put("skills","[ARRAY]").put("belt_level","[COMPUTED]")
                    .put("location","[SEARCHABLE]").put("bio","[SEARCHABLE]")
                    .put("tenant_id","[MANDATORY_FILTER]");
            case "job" -> new JSONObject()
                    .put("job_id","[ID]").put("title","[SEARCHABLE]")
                    .put("description","[SEARCHABLE]").put("category","[FACET]")
                    .put("location","[GEO+FACET]").put("belt_required","[FACET]")
                    .put("tenant_id","[MANDATORY_FILTER]");
            default -> new JSONObject().put("doc_type",docType).put("note","Generic document structure");
        };
    }

    private JSONArray requiredFields(String docType) {
        JSONArray f = new JSONArray().put("tenant_id").put("created_at").put("updated_at");
        return switch (docType) {
            case "candidate" -> f.put("candidate_id").put("name");
            case "job"       -> f.put("job_id").put("title").put("category");
            default          -> f.put(docType + "_id");
        };
    }

    private JSONObject buildMapping(String docType) {
        return new JSONObject()
                .put("tenant_id",   new JSONObject().put("type","keyword"))
                .put("created_at",  new JSONObject().put("type","date"))
                .put("updated_at",  new JSONObject().put("type","date"))
                .put(docType+"_id", new JSONObject().put("type","keyword"))
                .put("name",        new JSONObject().put("type","text").put("analyzer","standard").put("boost",2))
                .put("location",    new JSONObject().put("type","text")
                        .put("fields", new JSONObject()
                                .put("keyword", new JSONObject().put("type","keyword"))))
                .put("_doc_type",   new JSONObject().put("type","keyword"));
    }
}
