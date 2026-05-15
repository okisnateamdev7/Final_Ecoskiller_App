package com.ecoskiller.mcp.nginx.tools;

import com.ecoskiller.mcp.nginx.NginxTool;
import com.ecoskiller.mcp.nginx.config.ServerConfig;

import java.time.Instant;
import java.util.*;

/**
 * Shared helpers for all NGINX Ingress MCP tools.
 */
public abstract class BaseNginxTool implements NginxTool {

    protected final ServerConfig config;

    protected BaseNginxTool(ServerConfig config) {
        this.config = config;
    }

    // ─── Schema helpers ────────────────────────────────────────────────

    protected Map<String, Object> property(String type, String description) {
        return Map.of("type", type, "description", description);
    }

    protected Map<String, Object> enumProp(String description, String... values) {
        return Map.of("type", "string", "description", description, "enum", List.of(values));
    }

    // ─── Response builders ─────────────────────────────────────────────

    protected Map<String, Object> success(String message, Map<String, Object> data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status",    "success");
        r.put("message",   message);
        r.put("timestamp", Instant.now().toString());
        r.put("server",    "mcp-nginx-ingress");
        if (data != null) r.putAll(data);
        return r;
    }

    protected Map<String, Object> success(String message) {
        return success(message, null);
    }

    protected Map<String, Object> withDefaults(Map<String, Object> r) {
        r.put("namespace",    config.getKubeNamespace());
        r.put("ingressClass", config.getIngressClassName());
        return r;
    }

    // ─── Argument helpers ──────────────────────────────────────────────

    protected String str(Map<String, Object> args, String key, String def) {
        Object v = args.get(key);
        return (v instanceof String) ? (String) v : (v != null ? v.toString() : def);
    }

    protected int intArg(Map<String, Object> args, String key, int def) {
        Object v = args.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        if (v != null) try { return Integer.parseInt(v.toString()); } catch (NumberFormatException ignored) {}
        return def;
    }

    protected boolean boolArg(Map<String, Object> args, String key, boolean def) {
        Object v = args.get(key);
        if (v instanceof Boolean) return (Boolean) v;
        if (v instanceof String) return "true".equalsIgnoreCase((String) v);
        return def;
    }

    // ─── Kubernetes YAML generation helpers ────────────────────────────

    protected String ingressYaml(String name, String namespace, String host,
                                  String path, String service, int port, String certSecret) {
        return "apiVersion: networking.k8s.io/v1\n" +
               "kind: Ingress\n" +
               "metadata:\n" +
               "  name: " + name + "\n" +
               "  namespace: " + namespace + "\n" +
               "  annotations:\n" +
               "    cert-manager.io/issuer: \"letsencrypt-prod\"\n" +
               "    nginx.ingress.kubernetes.io/rate-limit: \"" + config.getDefaultRateLimitRps() + "\"\n" +
               "    nginx.ingress.kubernetes.io/ssl-redirect: \"true\"\n" +
               "spec:\n" +
               "  ingressClassName: " + config.getIngressClassName() + "\n" +
               (certSecret != null ?
                "  tls:\n  - hosts:\n    - " + host + "\n    secretName: " + certSecret + "\n" : "") +
               "  rules:\n" +
               "  - host: " + host + "\n" +
               "    http:\n" +
               "      paths:\n" +
               "      - path: " + path + "\n" +
               "        pathType: Prefix\n" +
               "        backend:\n" +
               "          service:\n" +
               "            name: " + service + "\n" +
               "            port:\n" +
               "              number: " + port + "\n";
    }
}
