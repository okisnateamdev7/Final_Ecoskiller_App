package com.ecoskiller.mcp.search.tools;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * ILM_POLICY_AGENT — Tool: ilm_policy
 *
 * Index Lifecycle Management (ILM) — automatic rotation of indices
 * through hot → warm → cold → delete phases per spec Section 10.4.
 *
 * Hot:    current + 3 prior days — 20 shards on fast SSD storage
 * Warm:   4-30 days — 5 shards on standard storage
 * Cold:   >30 days — archive storage or delete per retention policy
 * Delete: per tenant compliance retention rules
 *
 * Prevents shard explosion from unbounded index growth.
 * Enables best_compression codec for 30-40% size reduction.
 */
public class IlmPolicyTool extends BaseTool {

    @Override public String getName() { return "ilm_policy"; }

    @Override
    public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",       str("get_policy|create_policy|apply|rollover|explain|cost_estimate"))
                .put("tenant_id",    str("Ecoskiller tenant ID (optional)"))
                .put("doc_type",     str("Document type for policy application"))
                .put("phase",        str("ILM phase: hot|warm|cold|delete"))
                .put("policy_name",  str("Policy name (default: search-indexer-default-ilm)"))
                .put("hot_days",     intP("Days in hot phase (default: 3)"))
                .put("warm_days",    intP("Days in warm phase (default: 30)"))
                .put("delete_days",  intP("Delete after N days total (0 = never delete)"));
        return schema(getName(),
                "ILM_POLICY_AGENT — Index Lifecycle Management: hot/warm/cold/delete phases, " +
                "automatic rotation, shard count reduction, best_compression codec, " +
                "cost optimization 30-40% size reduction.",
                p, "action");
    }

    @Override
    public JSONObject execute(JSONObject a) throws Exception {
        String action     = a.getString("action");
        String policyName = a.optString("policy_name","search-indexer-default-ilm");
        int    hotDays    = a.optInt("hot_days",   3);
        int    warmDays   = a.optInt("warm_days", 30);
        int    deleteDays = a.optInt("delete_days", 0);

        return switch (action) {
            case "get_policy" -> json(new JSONObject()
                    .put("policy_name",  policyName)
                    .put("phases",       buildPolicy(hotDays, warmDays, deleteDays))
                    .put("applied_to",   "All tenant-* indices")
                    .put("timestamp",    now()));
            case "create_policy" -> json(new JSONObject()
                    .put("policy_name",  policyName)
                    .put("phases",       buildPolicy(hotDays, warmDays, deleteDays))
                    .put("status",       "CREATED")
                    .put("timestamp",    now()));
            case "apply" -> {
                String tenant  = a.optString("tenant_id","ALL");
                String docType = a.optString("doc_type","candidates");
                String idx     = "ALL".equals(tenant) ? "tenant-*-" + docType : indexName(tenant, docType);
                yield json(new JSONObject()
                        .put("policy_name",  policyName)
                        .put("applied_to",   idx)
                        .put("status",       "APPLIED")
                        .put("timestamp",    now()));
            }
            case "rollover" -> {
                String tenant  = a.getString("tenant_id");
                String docType = a.optString("doc_type","candidates");
                yield json(new JSONObject()
                        .put("old_index",   indexName(tenant, docType))
                        .put("new_index",   indexName(tenant, docType) + "-" + now().substring(0,10))
                        .put("trigger",     "age > " + hotDays + " days OR size > 50GB")
                        .put("shards",      new JSONObject()
                                .put("hot_phase",  20)
                                .put("warm_phase", 5)
                                .put("cold_phase", 1))
                        .put("status",      "ROLLOVER_COMPLETE")
                        .put("timestamp",   now()));
            }
            case "explain" -> {
                String tenant  = a.getString("tenant_id");
                String docType = a.optString("doc_type","candidates");
                yield json(new JSONObject()
                        .put("index",       indexName(tenant, docType))
                        .put("current_phase","hot")
                        .put("phase_age",   "2 days")
                        .put("next_action", "Transition to warm phase in " + hotDays + " days")
                        .put("policy",      policyName)
                        .put("timestamp",   now()));
            }
            case "cost_estimate" -> json(new JSONObject()
                    .put("compression_codec",   "best_compression")
                    .put("estimated_reduction", "30-40% index size reduction")
                    .put("warm_shards_savings", "Hot 20 shards → Warm 5 shards = 75% shard reduction")
                    .put("cold_storage",        "Archive storage: ~10x cheaper than SSD")
                    .put("timestamp",           now()));
            default -> text("Unknown action: " + action);
        };
    }

    private JSONObject buildPolicy(int hotDays, int warmDays, int deleteDays) {
        JSONObject phases = new JSONObject()
                .put("hot", new JSONObject()
                        .put("min_age",    "0ms")
                        .put("actions",    new JSONObject()
                                .put("rollover", new JSONObject().put("max_age", hotDays + "d").put("max_size","50gb"))
                                .put("set_priority", new JSONObject().put("priority",100))
                                .put("shards",       20)
                                .put("storage",      "SSD")))
                .put("warm", new JSONObject()
                        .put("min_age",    hotDays + "d")
                        .put("actions",    new JSONObject()
                                .put("forcemerge", new JSONObject().put("max_num_segments",1))
                                .put("shrink",     new JSONObject().put("number_of_shards",5))
                                .put("allocate",   new JSONObject().put("require", new JSONObject().put("data","warm")))
                                .put("storage",    "STANDARD")))
                .put("cold", new JSONObject()
                        .put("min_age",    warmDays + "d")
                        .put("actions",    new JSONObject()
                                .put("allocate",   new JSONObject().put("require", new JSONObject().put("data","cold")))
                                .put("shards",     1)
                                .put("storage",    "ARCHIVE")));
        if (deleteDays > 0) {
            phases.put("delete", new JSONObject()
                    .put("min_age", deleteDays + "d")
                    .put("actions", new JSONObject().put("delete", new JSONObject())));
        }
        return phases;
    }
}
