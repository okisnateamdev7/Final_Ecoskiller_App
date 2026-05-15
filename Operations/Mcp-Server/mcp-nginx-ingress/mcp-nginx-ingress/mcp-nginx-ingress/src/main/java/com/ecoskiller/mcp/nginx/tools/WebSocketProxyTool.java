package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 11 — websocket_proxy */
public class WebSocketProxyTool extends BaseNginxTool {
    public WebSocketProxyTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "websocket_proxy",
            "description",
                "Configure WebSocket proxying on NGINX Ingress for real-time features. " +
                "Enables HTTP/1.1 Upgrade to WebSocket protocol for Jitsi SFU signaling, " +
                "live notifications, and IVR/voice/video streams. " +
                "Uses least_conn LB algorithm for persistent connections. " +
                "Maintains full-duplex TCP connections without timeout for GD sessions. " +
                "Actions: enable | disable | configure | get.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",  enumProp("Operation", "enable", "disable", "configure", "get"),
                    "path",    property("string", "WebSocket endpoint path (e.g. /signaling)"),
                    "service", property("string", "Backend service name"),
                    "timeout", property("integer", "Connection timeout in seconds (0 = no timeout)")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action  = str(args, "action",  "get");
        String path    = str(args, "path",    "/signaling");
        String service = str(args, "service", "jitsi-signaling");
        int    timeout = intArg(args, "timeout", 3600);

        if ("enable".equals(action) || "configure".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("path",    path);
            d.put("service", service);
            d.put("timeoutSec", timeout);
            d.put("annotation",    "nginx.ingress.kubernetes.io/proxy-read-timeout: \"" + timeout + "\"");
            d.put("nginxDirectives", List.of(
                "location " + path + " {",
                "  proxy_pass http://" + service + ";",
                "  proxy_http_version 1.1;",
                "  proxy_set_header Upgrade $http_upgrade;",
                "  proxy_set_header Connection \"upgrade\";",
                "  proxy_set_header Host $host;",
                "  proxy_read_timeout " + timeout + "s;",
                "  proxy_send_timeout " + timeout + "s;",
                "}"
            ));
            d.put("lbAlgorithm",    "least_conn (recommended for long-lived WS connections)");
            d.put("useCase",        "Jitsi SFU signaling, real-time candidate notifications, IVR streams");
            d.put("upgradeHeaders", Map.of(
                "Upgrade",    "websocket",
                "Connection", "Upgrade",
                "Sec-WebSocket-Version", "13"
            ));
            return success("WebSocket proxy configured on " + path + " → " + service, d);
        }
        if ("disable".equals(action)) {
            return success("WebSocket disabled on " + path, Map.of("warning", "Active WS connections will be dropped"));
        }
        // get
        List<Map<String, Object>> endpoints = List.of(
            Map.of("path", "/signaling",     "service", "jitsi-signaling",    "activeConns", 127, "timeout", 0),
            Map.of("path", "/notifications", "service", "notification-svc",   "activeConns", 43,  "timeout", 60),
            Map.of("path", "/ivr-stream",    "service", "ivr-gateway",        "activeConns", 8,   "timeout", 1800)
        );
        return success("WebSocket endpoints", Map.of("endpoints", endpoints));
    }
}
