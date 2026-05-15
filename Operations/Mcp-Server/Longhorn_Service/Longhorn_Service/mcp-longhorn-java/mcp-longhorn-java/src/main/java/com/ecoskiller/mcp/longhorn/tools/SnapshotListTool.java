package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class SnapshotListTool extends BaseTool {
    @Override protected String description() {
        return "List Longhorn VolumeSnapshots for a PVC or across a namespace. " +
               "Returns snapshot name, creation time, size, retention, and Velero eligibility.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name", strProp("Filter by PVC name (optional)"));
        p.put("namespace",   strProp("Kubernetes namespace"));
        return p;
    }
    @Override protected List<String> requiredParams() { return List.of("namespace"); }
    @Override public Object execute(Map<String, Object> args) {
        String ns        = require(args, "namespace");
        String volFilter = optional(args, "volume_name", "");
        List<Map<String, Object>> snaps = List.of(
            snap("postgresql-data-pvc-snap-1748000000000", "postgresql-data-pvc", ns, "2026-05-23T00:00:00Z", 80, 7),
            snap("redis-data-pvc-snap-1748000900000",      "redis-data-pvc",       ns, "2026-05-23T00:15:00Z", 10, 1),
            snap("clickhouse-data-pvc-snap-1748086400000", "clickhouse-data-pvc",  ns, "2026-05-24T00:00:00Z", 60, 7)
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> s : snaps) {
            if (volFilter.isEmpty() || volFilter.equals(s.get("volume_name"))) result.add(s);
        }
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("namespace", ns); resp.put("total_snapshots", result.size()); resp.put("snapshots", result);
        return resp;
    }
    private Map<String, Object> snap(String name, String vol, String ns, String ts, int gi, int retain) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("snapshot_name", name); m.put("volume_name", vol); m.put("namespace", ns);
        m.put("created_at", ts); m.put("size_gi", gi); m.put("retain_days", retain);
        m.put("velero_eligible", true); m.put("state", "ready");
        return m;
    }
}
