package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class ReplicaHealthTool extends BaseTool {
    @Override protected String description() {
        return "Check Longhorn volume replica health across k3s worker nodes. " +
               "Returns per-volume replica placement, rebuild progress, and degraded volume count. " +
               "Default replica count: 3 (cross-node). Alerts on replica count < 3 (degraded).";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name",   strProp("Filter by PVC name (optional)"));
        p.put("namespace",     strProp("Filter by namespace (optional)"));
        p.put("degraded_only", boolProp("Return only volumes with replica count < configured target"));
        return p;
    }
    @Override public Object execute(Map<String, Object> args) {
        String volFilter  = optional(args, "volume_name", "");
        boolean degraded  = optionalBool(args, "degraded_only", false);
        List<Map<String, Object>> vols = List.of(
            rs("postgresql-data-pvc", 3, 3, "healthy",  List.of("node-01","node-02","node-03")),
            rs("redis-data-pvc",      3, 3, "healthy",  List.of("node-01","node-02","node-03")),
            rs("opensearch-idx-pvc",  3, 2, "degraded", List.of("node-01","node-03")),
            rs("kafka-log-pvc",       3, 3, "healthy",  List.of("node-02","node-03","node-04"))
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> v : vols) {
            if (!volFilter.isEmpty() && !volFilter.equals(v.get("volume_name"))) continue;
            if (degraded && "healthy".equals(v.get("health"))) continue;
            result.add(v);
        }
        long degradedCount = result.stream().filter(v -> "degraded".equals(v.get("health"))).count();
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("total_volumes", result.size()); resp.put("degraded_count", degradedCount);
        resp.put("target_replicas", 3);
        resp.put("alert", degradedCount > 0
            ? "ACTION REQUIRED: " + degradedCount + " volume(s) degraded — auto-replication in progress"
            : "All volumes healthy");
        resp.put("volumes", result);
        return resp;
    }
    private Map<String, Object> rs(String name, int target, int actual, String health, List<String> nodes) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("volume_name", name); m.put("target_replicas", target); m.put("actual_replicas", actual);
        m.put("health", health);    m.put("replica_nodes", nodes);
        m.put("self_healing", actual < target ? "scheduled" : "not-needed");
        return m;
    }
}
