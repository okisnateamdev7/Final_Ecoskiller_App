package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class VolumeListTool extends BaseTool {

    private static final List<Map<String, Object>> REGISTRY = buildRegistry();

    @Override protected String description() {
        return "List all Longhorn PersistentVolumes across Ecoskiller's k3s clusters. " +
               "Returns volume name, namespace, size, replica count, health status, consumer service, and cloud target.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("namespace",    strProp("Filter by Kubernetes namespace (optional, omit for all)"));
        p.put("consumer",     strProp("Filter by consuming service (e.g., postgresql, redis)"));
        p.put("cloud_target", strPropEnum("Filter by cloud", "gcp", "aws", "both", "all"));
        p.put("health_only",  boolProp("If true, return only degraded/unhealthy volumes"));
        return p;
    }
    @Override public Object execute(Map<String, Object> args) {
        String nsFilter      = optional(args, "namespace",    "");
        String consumerFilter= optional(args, "consumer",     "");
        String cloudFilter   = optional(args, "cloud_target", "all");
        boolean healthOnly   = optionalBool(args, "health_only", false);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> vol : REGISTRY) {
            if (!nsFilter.isEmpty()       && !nsFilter.equals(vol.get("namespace")))    continue;
            if (!consumerFilter.isEmpty() && !consumerFilter.equals(vol.get("consumer"))) continue;
            if (!cloudFilter.equals("all") && !cloudFilter.equals(vol.get("cloud_target"))) continue;
            if (healthOnly && "healthy".equals(vol.get("health"))) continue;
            result.add(vol);
        }
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("total_volumes", result.size());
        resp.put("volumes", result);
        return resp;
    }
    private static List<Map<String, Object>> buildRegistry() {
        List<Map<String, Object>> v = new ArrayList<>();
        v.add(vol("postgresql-data-pvc", "default", 80,  "postgresql",    "gcp", "healthy",  3));
        v.add(vol("redis-data-pvc",      "default", 10,  "redis",         "gcp", "healthy",  3));
        v.add(vol("clickhouse-data-pvc", "default", 60,  "clickhouse",    "aws", "healthy",  3));
        v.add(vol("opensearch-idx-pvc",  "default", 50,  "opensearch",    "gcp", "degraded", 2));
        v.add(vol("kafka-log-pvc",       "default", 40,  "kafka",         "aws", "healthy",  3));
        v.add(vol("minio-data-pvc",      "default", 100, "minio",         "gcp", "healthy",  3));
        v.add(vol("grafana-tempo-pvc",   "ops",     50,  "grafana-tempo", "gcp", "healthy",  3));
        v.add(vol("prometheus-tsdb-pvc", "ops",     20,  "prometheus",    "gcp", "healthy",  3));
        v.add(vol("loki-chunks-pvc",     "ops",     30,  "loki",          "gcp", "healthy",  3));
        return Collections.unmodifiableList(v);
    }
    private static Map<String, Object> vol(String name, String ns, int gi, String consumer,
                                            String cloud, String health, int replicas) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("volume_name", name); m.put("namespace", ns); m.put("storage_gi", gi);
        m.put("consumer", consumer); m.put("cloud_target", cloud);
        m.put("health", health); m.put("replica_count", replicas);
        m.put("storage_class", "longhorn");
        return m;
    }
}
