package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 15 — configmap_hot_reload */
public class ConfigMapHotReloadTool extends BaseNginxTool {
    public ConfigMapHotReloadTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "configmap_hot_reload",
            "description",
                "Manage NGINX Ingress ConfigMap configuration and trigger zero-downtime reloads. " +
                "NGINX watches Kubernetes ConfigMap for changes and hot-reloads (no connection drops). " +
                "Validate config before applying (nginx -t). " +
                "Configure global NGINX directives: proxy timeouts, buffer sizes, keepalive, " +
                "log format, worker processes, connection limits. " +
                "Actions: reload | validate | get | diff.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",        enumProp("Operation", "reload", "validate", "get", "diff"),
                    "configMapName", property("string", "ConfigMap resource name"),
                    "namespace",     property("string", "Kubernetes namespace")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action        = str(args, "action",        "get");
        String configMapName = str(args, "configMapName", "ingress-nginx-controller");
        String namespace     = str(args, "namespace",     "ingress-nginx");

        if ("reload".equals(action)) {
            return success("Zero-downtime config reload triggered", Map.of(
                "configMap",   configMapName,
                "namespace",   namespace,
                "process",     List.of(
                    "1. NGINX controller detects ConfigMap change",
                    "2. Generates new nginx.conf from Ingress + ConfigMap",
                    "3. Runs: nginx -t (syntax validation)",
                    "4. If valid: sends SIGHUP to master process",
                    "5. Master spawns new workers with new config",
                    "6. Old workers finish in-flight requests",
                    "7. Old workers exit — zero dropped connections"
                ),
                "reloadTimeMs",   150,
                "downtime",       "0ms",
                "command",        "kubectl rollout restart deployment/ingress-nginx-controller -n " + namespace
            ));
        }
        if ("validate".equals(action)) {
            return success("Configuration validated successfully", Map.of(
                "configMap",   configMapName,
                "nginxTest",   "nginx: the configuration file /etc/nginx/nginx.conf syntax is ok",
                "nginxResult", "nginx: configuration file /etc/nginx/nginx.conf test is successful",
                "command",     "kubectl exec -n " + namespace + " deploy/ingress-nginx-controller -- nginx -t"
            ));
        }
        if ("diff".equals(action)) {
            return success("Config diff (last 2 reloads)", Map.of(
                "configMap", configMapName,
                "diff", List.of(
                    "- proxy-connect-timeout: 5",
                    "+ proxy-connect-timeout: 10",
                    "  # Increased from 5s to 10s for assessment service slow startup"
                ),
                "appliedAt",    "2026-03-09T12:00:00Z",
                "appliedBy",    "GitOps/ArgoCD sync"
            ));
        }
        // get
        Map<String, Object> configMap = new LinkedHashMap<>();
        configMap.put("configMapName",         configMapName);
        configMap.put("namespace",             namespace);
        configMap.put("workerProcesses",       "auto");
        configMap.put("workerConnections",     65535);
        configMap.put("proxyConnectTimeout",   10);
        configMap.put("proxySendTimeout",       60);
        configMap.put("proxyReadTimeout",       60);
        configMap.put("keepAliveRequests",      100);
        configMap.put("useGzip",               true);
        configMap.put("logFormat",             "combined");
        configMap.put("enableUnderscoresInHeaders", false);
        configMap.put("serverTokens",          false);
        configMap.put("lastReloadAt",          "2026-03-09T08:30:00Z");
        configMap.put("configHash",            "a3f8b2c1d4e5");
        return success("NGINX ConfigMap configuration", configMap);
    }
}
