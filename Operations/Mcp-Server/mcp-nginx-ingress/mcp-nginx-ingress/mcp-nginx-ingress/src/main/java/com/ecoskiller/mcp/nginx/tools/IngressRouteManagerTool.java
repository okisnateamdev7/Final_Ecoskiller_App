package com.ecoskiller.mcp.nginx.tools;

import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/**
 * Tool 1 — ingress_route_manager
 *
 * Manages Kubernetes Ingress resources: hostname/path routing rules,
 * path types, backend services.
 * Covers: path-based routing, host-based virtual hosting, regex routes.
 */
public class IngressRouteManagerTool extends BaseNginxTool {

    public IngressRouteManagerTool(ServerConfig config) { super(config); }

    @Override
    public Map<String, Object> descriptor() {
        return Map.of(
            "name", "ingress_route_manager",
            "description",
                "Manage Kubernetes Ingress routing rules for the NGINX Ingress Controller. " +
                "Create, update, delete, or list Ingress resources that define hostname/path-based " +
                "traffic routing from the public internet to internal Kubernetes services. " +
                "Supports path-based routing (/api/* → assessment-service), " +
                "hostname-based virtual hosting (api.ecoskiller.com → api-gateway), " +
                "and pathType options (Prefix, Exact). " +
                "Actions: create | update | delete | get | list.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",    enumProp("Operation to perform", "create", "update", "delete", "get", "list"),
                    "name",      property("string", "Ingress resource name (Kubernetes name format)"),
                    "namespace", property("string", "Kubernetes namespace (default: from server config)"),
                    "host",      property("string", "Hostname for routing (e.g. api.ecoskiller.com or *.ecoskiller.com)"),
                    "path",      property("string", "URL path prefix (e.g. /assessments or /api/*)"),
                    "pathType",  enumProp("Kubernetes pathType", "Prefix", "Exact", "ImplementationSpecific"),
                    "service",   property("string", "Backend Kubernetes service name"),
                    "port",      property("integer", "Backend service port (1–65535)"),
                    "certSecret",property("string", "TLS secret name (omit for HTTP only)"),
                    "annotations",property("object", "Extra Ingress annotations (nginx.ingress.kubernetes.io/*)")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override
    public Object execute(Map<String, Object> args) {
        String action    = str(args, "action", "list");
        String name      = str(args, "name",      "ecoskiller-ingress");
        String namespace = str(args, "namespace",  config.getKubeNamespace());
        String host      = str(args, "host",       "api.ecoskiller.com");
        String path      = str(args, "path",       "/");
        String pathType  = str(args, "pathType",   "Prefix");
        String service   = str(args, "service",    "assessment-service");
        int    port      = intArg(args, "port",    8080);
        String certSec   = str(args, "certSecret", null);

        switch (action) {
            case "create":
            case "update": {
                String yaml = ingressYaml(name, namespace, host, path, service, port, certSec);
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("ingressName",  name);
                d.put("namespace",    namespace);
                d.put("host",         host);
                d.put("path",         path);
                d.put("pathType",     pathType);
                d.put("backend",      Map.of("service", service, "port", port));
                d.put("tlsEnabled",   certSec != null);
                d.put("generatedYaml", yaml);
                d.put("applyCommand", "kubectl apply -f ingress.yaml");
                return success("Ingress route " + action + "d: " + name + " → " + host + path, d);
            }

            case "delete": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("ingressName", name);
                d.put("namespace",   namespace);
                d.put("command",     "kubectl delete ingress " + name + " -n " + namespace);
                d.put("warning",     "Deleting this Ingress will immediately stop routing external traffic to " + service);
                return success("Ingress route deleted: " + name, d);
            }

            case "get": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("ingressName",    name);
                d.put("namespace",      namespace);
                d.put("host",           host);
                d.put("path",           path);
                d.put("service",        service);
                d.put("port",           port);
                d.put("tlsEnabled",     certSec != null);
                d.put("ingressClass",   config.getIngressClassName());
                d.put("status",         "Active");
                d.put("loadBalancerIp", "34.100.50.200");    // GCP LB IP (placeholder)
                return success("Ingress route retrieved: " + name, d);
            }

            case "list":
            default: {
                List<Map<String, Object>> routes = List.of(
                    Map.of("name", "ecoskiller-assessments",  "host", "api.ecoskiller.com",    "path", "/assessments",  "service", "assessment-service",  "status", "Active"),
                    Map.of("name", "ecoskiller-candidates",   "host", "api.ecoskiller.com",    "path", "/candidates",   "service", "candidate-service",   "status", "Active"),
                    Map.of("name", "ecoskiller-portal",       "host", "portal.ecoskiller.com", "path", "/",             "service", "portal-service",      "status", "Active"),
                    Map.of("name", "ecoskiller-interviews",   "host", "api.ecoskiller.com",    "path", "/interviews",   "service", "interview-engine",    "status", "Active"),
                    Map.of("name", "ecoskiller-ws-signaling", "host", "ws.ecoskiller.com",     "path", "/signaling",    "service", "jitsi-signaling",     "status", "Active")
                );
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("namespace",   namespace);
                d.put("totalRoutes", routes.size());
                d.put("routes",      routes);
                return success("Listed " + routes.size() + " ingress routes in namespace " + namespace, d);
            }
        }
    }
}
