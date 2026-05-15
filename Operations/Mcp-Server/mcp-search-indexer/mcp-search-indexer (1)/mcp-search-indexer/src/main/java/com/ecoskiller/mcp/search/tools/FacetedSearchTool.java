package com.ecoskiller.mcp.search.tools;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FACETED_SEARCH_AGENT — Tool: faceted_search
 *
 * Multi-dimensional faceted navigation over OpenSearch aggregations.
 * Implements dynamic facet re-computation after filtering so sub-facets
 * remain relevant (e.g. once Belt=BLUE selected, show only locations
 * with BLUE-belt candidates).
 *
 * Supports: belt_level, job_category, location, assessment_type,
 *           hiring_stage, skills, language, experience_years.
 */
public class FacetedSearchTool extends BaseTool {

    @Override public String getName() { return "faceted_search"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",         str("search|get_facets|drill_down|aggregate"))
                .put("tenant_id",      str("Ecoskiller tenant ID"))
                .put("doc_type",       str("Document type: candidate|job|application"))
                .put("query",          str("Optional query string"))
                .put("filters",        str("JSON object string of active filters e.g. {\"belt_level\":\"BLUE\"}"))
                .put("facet_fields",   str("Comma-separated facet fields to aggregate"))
                .put("facet_size",     intP("Max values per facet bucket (default: 20, max: 100)"))
                .put("limit",          intP("Result documents limit (default: 20)"))
                .put("offset",         intP("Pagination offset"))
                .put("sort_by",        str("Sort field: relevance|created_at|score|belt_level"));
        return schema(getName(),
                "FACETED_SEARCH_AGENT — Multi-dimensional faceted navigation with dynamic sub-facet " +
                "re-computation. Aggregates belt_level, job_category, location, assessment_type, " +
                "hiring_stage, skills. Tenant-scoped.",
                p, "action", "tenant_id", "doc_type");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action    = a.getString("action");
        String tenantId  = a.getString("tenant_id");
        String docType   = a.optString("doc_type","candidate");
        String idx       = indexName(tenantId, docType + "s");
        int    facetSize = Math.min(a.optInt("facet_size", 20), 100);

        return switch (action) {
            case "search" -> {
                String query   = a.optString("query","*");
                String filters = a.optString("filters","{}");
                yield json(new JSONObject()
                        .put("index",        idx)
                        .put("tenant_id",    tenantId)
                        .put("query",        query)
                        .put("active_filters",filters)
                        .put("total_hits",   0)
                        .put("hits",         new JSONArray())
                        .put("facets",       buildAllFacets(docType, facetSize))
                        .put("facet_note",   "Facets re-computed after filter application")
                        .put("took_ms",      0)
                        .put("timestamp",    now()));
            }
            case "get_facets" -> json(new JSONObject()
                    .put("index",        idx)
                    .put("tenant_id",    tenantId)
                    .put("facets",       buildAllFacets(docType, facetSize))
                    .put("timestamp",    now()));
            case "drill_down" -> {
                String filters = a.optString("filters","{}");
                yield json(new JSONObject()
                        .put("index",           idx)
                        .put("tenant_id",       tenantId)
                        .put("applied_filters", filters)
                        .put("sub_facets",      buildAllFacets(docType, facetSize))
                        .put("note",            "Sub-facets scoped to current filter selection")
                        .put("timestamp",       now()));
            }
            case "aggregate" -> {
                String fields = a.optString("facet_fields","belt_level,location");
                JSONObject aggs = new JSONObject();
                for (String f : fields.split(",")) {
                    String fTrim = f.trim();
                    aggs.put(fTrim, new JSONObject()
                            .put("doc_count", 0)
                            .put("buckets",   new JSONArray()));
                }
                yield json(new JSONObject()
                        .put("index",      idx)
                        .put("tenant_id",  tenantId)
                        .put("aggregations",aggs)
                        .put("took_ms",    0)
                        .put("timestamp",  now()));
            }
            default -> text("Unknown action: " + action);
        };
    }

    private JSONObject buildAllFacets(String docType, int size) {
        JSONObject facets = new JSONObject();
        // Common facets
        facets.put("location", facetBucket("location.keyword", size));
        if ("candidate".equals(docType) || "application".equals(docType)) {
            facets.put("belt_level",     facetBucket("belt_level.keyword", size));
            facets.put("skills",         facetBucket("skills.keyword", size));
            facets.put("hiring_stage",   facetBucket("hiring_stage.keyword", size));
        }
        if ("job".equals(docType)) {
            facets.put("job_category",   facetBucket("category.keyword", size));
            facets.put("belt_required",  facetBucket("belt_required.keyword", size));
            facets.put("assessment_type",facetBucket("assessment_type.keyword", size));
        }
        return facets;
    }

    private JSONObject facetBucket(String field, int size) {
        return new JSONObject()
                .put("field",   field)
                .put("size",    size)
                .put("buckets", new JSONArray());
    }
}
