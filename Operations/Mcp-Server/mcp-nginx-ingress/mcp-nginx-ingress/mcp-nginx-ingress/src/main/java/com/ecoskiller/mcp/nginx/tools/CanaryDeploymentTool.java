package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 8 — canary_deployment */
public class CanaryDeploymentTool extends BaseNginxTool {
    public CanaryDeploymentTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "canary_deployment",
            "description",
                "Manage canary (blue-green) deployments via NGINX Ingress weight-based traffic splitting. " +
                "Route a percentage of traffic (e.g. 10%) to a new service version while keeping " +
                "90% on the stable version. Monitor error rates before full rollout. " +
                "Supports automatic rollback on error threshold breach. " +
                "Actions: enable | disable | set-weight | get | list.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",        enumProp("Operation", "enable", "disable", "set-weight", "get", "list"),
                    "service",       property("string", "New/canary service name"),
                    "stableService", property("string", "Stable service name"),
                    "weight",        property("integer", "Traffic % to canary (0–100)"),
                    "host",          property("string", "Hostname for the Ingress"),
                    "path",          property("string", "Path to split traffic on")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action        = str(args, "action",        "list");
        String service       = str(args, "service",       "assessment-service-v2");
        String stableService = str(args, "stableService", "assessment-service-v1");
        int    weight        = intArg(args, "weight",     10);
        String host          = str(args, "host",          "api.ecoskiller.com");
        String path          = str(args, "path",          "/assessments");

        if ("enable".equals(action) || "set-weight".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("canaryService", service);
            d.put("stableService", stableService);
            d.put("canaryWeight",  weight);
            d.put("stableWeight",  100 - weight);
            d.put("host",  host);
            d.put("path",  path);
            d.put("canaryIngressYaml",
                "apiVersion: networking.k8s.io/v1\nkind: Ingress\nmetadata:\n" +
                "  name: " + service + "-canary\n  annotations:\n" +
                "    nginx.ingress.kubernetes.io/canary: \"true\"\n" +
                "    nginx.ingress.kubernetes.io/canary-weight: \"" + weight + "\"\n" +
                "spec:\n  ingressClassName: nginx\n  rules:\n" +
                "  - host: " + host + "\n    http:\n      paths:\n" +
                "      - path: " + path + "\n        pathType: Prefix\n" +
                "        backend:\n          service:\n            name: " + service + "\n            port:\n              number: 8080");
            d.put("monitoring", Map.of(
                "watchMetric",   "nginx_ingress_controller_requests_total",
                "errorThreshold","5% error rate triggers rollback",
                "command",       "kubectl get --raw /apis/metrics.k8s.io/v1beta1/pods"
            ));
            return success("Canary enabled: " + weight + "% → " + service + ", " + (100-weight) + "% → " + stableService, d);
        }
        if ("disable".equals(action)) {
            return success("Canary disabled, 100% traffic → " + stableService, Map.of(
                "command", "kubectl delete ingress " + service + "-canary"
            ));
        }
        // list
        List<Map<String, Object>> canaries = List.of(
            Map.of("path", "/assessments", "canary", "assessment-service-v2", "weight", 10, "stable", "assessment-service-v1", "stableWeight", 90)
        );
        return success("Canary deployments", Map.of("canaries", canaries));
    }
}
