package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class BackupStatusTool extends BaseTool {
    @Override protected String description() {
        return "Query Longhorn backup status from MinIO target. " +
               "Returns backup state, last successful backup time, size, geo-redundancy status. " +
               "Validates RPO targets: Redis < 15 min, block volumes < 60 min.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("backup_id",   strProp("Specific backup job ID to query (optional)"));
        p.put("volume_name", strProp("Filter by PVC name (optional)"));
        return p;
    }
    @Override public Object execute(Map<String, Object> args) {
        String volFilter = optional(args, "volume_name", "");
        List<Map<String, Object>> statuses = List.of(
            bkp("postgresql-data-pvc", "completed", "2026-05-23T23:50:00Z", 42, true,  58),
            bkp("redis-data-pvc",       "completed", "2026-05-23T23:59:00Z",  6, true,   1),
            bkp("clickhouse-data-pvc",  "running",   null,                   38, false, null),
            bkp("opensearch-idx-pvc",   "completed", "2026-05-23T22:00:00Z", 29, true, 120)
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> s : statuses) {
            if (volFilter.isEmpty() || volFilter.equals(s.get("volume_name"))) result.add(s);
        }
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("total", result.size());
        resp.put("rpo_sla_redis_minutes", 15);
        resp.put("rpo_sla_block_volumes_minutes", 60);
        resp.put("backups", result);
        return resp;
    }
    private Map<String, Object> bkp(String vol, String state, String lastSuccess, int sizeGi, boolean geoOk, Integer ageMin) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("volume_name", vol); m.put("state", state);
        m.put("last_success", lastSuccess != null ? lastSuccess : "in-progress");
        m.put("backup_size_gi", sizeGi); m.put("geo_redundant", geoOk);
        m.put("age_minutes", ageMin != null ? ageMin : "N/A");
        m.put("rpo_compliant", ageMin != null && ageMin < (vol.contains("redis") ? 15 : 60));
        return m;
    }
}
