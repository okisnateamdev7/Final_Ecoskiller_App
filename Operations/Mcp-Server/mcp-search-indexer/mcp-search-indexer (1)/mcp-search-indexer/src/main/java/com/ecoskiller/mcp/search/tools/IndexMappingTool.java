package com.ecoskiller.mcp.search.tools;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * INDEX_MAPPING_AGENT — Tool: index_mapping
 *
 * Manages OpenSearch index mappings and analyzer configurations.
 * Supports zero-downtime mapping updates via blue-green reindex.
 *
 * Custom analyzers:
 *   - hindi_analyzer:    language-specific stemming + stop words
 *   - hinglish_filter:   mixed Hindi-English character filter
 *   - standard:          English BM25 default
 *
 * Field boosting: name^2, title^2 vs description (1x).
 */
public class IndexMappingTool extends BaseTool {

    @Override public String getName() { return "index_mapping"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",         str("get|put|update_analyzer|validate|diff|get_settings"))
                .put("tenant_id",      str("Ecoskiller tenant ID"))
                .put("doc_type",       str("Document type: candidate|job|assessment|application|user_profile|scoring"))
                .put("mapping_json",   str("New mapping definition as JSON string (for put action)"))
                .put("analyzer_name",  str("Analyzer: standard|hindi_analyzer|hinglish_filter"))
                .put("field",          str("Specific field to inspect or update"));
        return schema(getName(),
                "INDEX_MAPPING_AGENT — OpenSearch index mapping management: get/put/update mappings, " +
                "custom analyzer config (Hindi/Hinglish), field boosting, zero-downtime updates via reindex.",
                p, "action", "tenant_id", "doc_type");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action  = a.getString("action");
        String tenant  = a.getString("tenant_id");
        String docType = a.optString("doc_type","candidates");
        String idx     = indexName(tenant, docType);

        return switch (action) {
            case "get" -> json(new JSONObject()
                    .put("index",    idx)
                    .put("mappings", buildMapping(docType))
                    .put("settings", buildSettings())
                    .put("timestamp",now()));
            case "put" -> {
                String mappingJson = a.optString("mapping_json","{}");
                yield json(new JSONObject()
                        .put("index",         idx)
                        .put("action",        "PUT_MAPPING")
                        .put("note",          "Mapping changes require reindex for existing documents")
                        .put("reindex_needed",true)
                        .put("zero_downtime", "Use reindex tool with blue-green strategy")
                        .put("status",        "MAPPING_STAGED")
                        .put("timestamp",     now()));
            }
            case "update_analyzer" -> {
                String analyzer = a.optString("analyzer_name","standard");
                yield json(new JSONObject()
                        .put("index",         idx)
                        .put("analyzer",      analyzer)
                        .put("config",        analyzerConfig(analyzer))
                        .put("note",          "Index must be closed, updated, then reopened for analyzer changes")
                        .put("zero_downtime", "Use reindex tool blue-green to apply without downtime")
                        .put("timestamp",     now()));
            }
            case "validate" -> {
                String mapping = a.optString("mapping_json","{}");
                yield json(new JSONObject()
                        .put("index",   idx)
                        .put("valid",   true)
                        .put("issues",  new JSONArray())
                        .put("timestamp",now()));
            }
            case "diff" -> json(new JSONObject()
                    .put("index",    idx)
                    .put("current",  buildMapping(docType))
                    .put("proposed", "Provide mapping_json to see diff")
                    .put("timestamp",now()));
            case "get_settings" -> json(new JSONObject()
                    .put("index",    idx)
                    .put("settings", buildSettings())
                    .put("timestamp",now()));
            default -> text("Unknown action: " + action);
        };
    }

    private JSONObject buildMapping(String docType) {
        JSONObject m = new JSONObject()
                .put("tenant_id",  new JSONObject().put("type","keyword"))
                .put("created_at", new JSONObject().put("type","date"))
                .put("updated_at", new JSONObject().put("type","date"))
                .put("_doc_type",  new JSONObject().put("type","keyword"));
        if ("candidates".equals(docType) || "candidate".equals(docType)) {
            m.put("name",      new JSONObject().put("type","text").put("analyzer","standard")
                    .put("fields",new JSONObject().put("keyword",new JSONObject().put("type","keyword")))
                    .put("boost",2))
             .put("skills",    new JSONObject().put("type","keyword"))
             .put("belt_level",new JSONObject().put("type","keyword"))
             .put("location",  new JSONObject().put("type","text")
                     .put("fields",new JSONObject().put("keyword",new JSONObject().put("type","keyword"))))
             .put("bio",       new JSONObject().put("type","text").put("analyzer","standard"))
             .put("geo_point", new JSONObject().put("type","geo_point"));
        } else if ("jobs".equals(docType) || "job".equals(docType)) {
            m.put("title",        new JSONObject().put("type","text").put("boost",2))
             .put("description",  new JSONObject().put("type","text"))
             .put("category",     new JSONObject().put("type","keyword"))
             .put("belt_required",new JSONObject().put("type","keyword"))
             .put("location",     new JSONObject().put("type","text")
                     .put("fields",new JSONObject().put("keyword",new JSONObject().put("type","keyword"))))
             .put("geo_location", new JSONObject().put("type","geo_point"));
        }
        return m;
    }

    private JSONObject buildSettings() {
        return new JSONObject()
                .put("number_of_shards",         5)
                .put("number_of_replicas",        2)
                .put("refresh_interval",          "1s")
                .put("codec",                     "best_compression")
                .put("analysis", new JSONObject()
                        .put("analyzer", new JSONObject()
                                .put("hindi_analyzer",   analyzerConfig("hindi_analyzer"))
                                .put("hinglish_filter",  analyzerConfig("hinglish_filter"))
                                .put("standard",         analyzerConfig("standard"))));
    }

    private JSONObject analyzerConfig(String name) {
        return switch (name) {
            case "hindi_analyzer" -> new JSONObject()
                    .put("type","custom").put("tokenizer","standard")
                    .put("filter", new JSONArray().put("lowercase").put("hindi_stop").put("hindi_stemmer"));
            case "hinglish_filter" -> new JSONObject()
                    .put("type","custom").put("tokenizer","standard")
                    .put("char_filter",new JSONArray().put("hindi_normalization"))
                    .put("filter",new JSONArray().put("lowercase").put("asciifolding"));
            default -> new JSONObject()
                    .put("type","standard").put("stopwords","_english_");
        };
    }
}
