package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 4 — load_balancer_config */
public class LoadBalancerConfigTool extends BaseNginxTool {
    public LoadBalancerConfigTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "load_balancer_config",
            "description",
                "Configure NGINX upstream load balancing algorithms and upstream pools. " +
                "Supports round-robin (stateless APIs), least_conn (WebSocket/Jitsi), " +
                "ip_hash (session affinity), weighted (heterogeneous pods). " +
                "Also configures keepalive connections, max-fails, fail-timeout. " +
                "Actions: configure | get | list | reset.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",    enumProp("Operation", "configure", "get", "list", "reset"),
                    "service",   property("string", "Kubernetes service name to configure"),
                    "algorithm", enumProp("LB algorithm", "round-robin", "least_conn", "ip_hash", "random", "weighted"),
                    "keepalive", property("integer", "Idle keepalive connections to backends"),
                    "maxFails",  property("integer", "Failures before marking backend unhealthy"),
                    "failTimeout",property("integer", "Seconds to wait after max-fails")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action    = str(args, "action",    "list");
        String service   = str(args, "service",   "assessment-service");
        String algorithm = str(args, "algorithm", "round-robin");
        int    keepalive = intArg(args, "keepalive", 32);
        int    maxFails  = intArg(args, "maxFails",  3);
        int    failTimeout = intArg(args, "failTimeout", 10);

        if ("configure".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("service",   service);
            d.put("algorithm", algorithm);
            d.put("keepalive", keepalive);
            d.put("maxFails",  maxFails);
            d.put("failTimeoutSec", failTimeout);
            String lbDirective = "least_conn".equals(algorithm) ? "least_conn;" :
                                 "ip_hash".equals(algorithm)    ? "ip_hash;"    : "";
            d.put("nginxUpstream",
                "upstream " + service.replace("-", "_") + "_pool {\n" +
                (lbDirective.isEmpty() ? "" : "  " + lbDirective + "\n") +
                "  server $POD_IP:8080 max_fails=" + maxFails + " fail_timeout=" + failTimeout + "s;\n" +
                "  keepalive " + keepalive + ";\n}");
            d.put("useCase", recommendedUseCase(algorithm));
            return success("Load balancer configured: " + algorithm + " for " + service, d);
        }
        // list
        List<Map<String, Object>> upstreams = List.of(
            Map.of("service", "assessment-service",  "algorithm", "round-robin",  "backends", 3, "healthy", 3),
            Map.of("service", "candidate-service",   "algorithm", "round-robin",  "backends", 2, "healthy", 2),
            Map.of("service", "interview-engine",    "algorithm", "least_conn",   "backends", 4, "healthy", 4),
            Map.of("service", "jitsi-signaling",     "algorithm", "least_conn",   "backends", 3, "healthy", 3),
            Map.of("service", "session-manager",     "algorithm", "ip_hash",      "backends", 2, "healthy", 2)
        );
        return success("Upstream pool list", Map.of("upstreams", upstreams));
    }

    private String recommendedUseCase(String algo) {
        switch (algo) {
            case "round-robin": return "Stateless services (assessment submission, candidate lookup)";
            case "least_conn":  return "Long-lived connections (WebSocket, Jitsi signaling, streaming)";
            case "ip_hash":     return "Client session affinity (stateful backend, legacy services)";
            case "weighted":    return "Heterogeneous pods (different CPU/memory allocations)";
            default:            return "General purpose";
        }
    }
}
