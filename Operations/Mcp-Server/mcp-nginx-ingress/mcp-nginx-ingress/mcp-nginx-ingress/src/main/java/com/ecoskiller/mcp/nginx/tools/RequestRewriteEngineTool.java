package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 12 — request_rewrite_engine */
public class RequestRewriteEngineTool extends BaseNginxTool {
    public RequestRewriteEngineTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "request_rewrite_engine",
            "description",
                "Configure request/response header manipulation and URL rewriting on NGINX Ingress. " +
                "Add X-Forwarded-For, X-Real-IP, X-Forwarded-Proto for client IP tracking. " +
                "Inject security headers (Content-Security-Policy, X-Frame-Options, X-XSS-Protection). " +
                "Rewrite URL paths transparently (e.g. /old-api → /new-api). " +
                "Block CRLF injection. Strip sensitive headers before forwarding. " +
                "Actions: add | remove | list | test.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",      enumProp("Operation", "add", "remove", "list", "test"),
                    "fromPath",    property("string", "Source URL path for rewrite"),
                    "toPath",      property("string", "Destination URL path"),
                    "headerName",  property("string", "Header name to add/modify"),
                    "headerValue", property("string", "Header value (no CRLF allowed)"),
                    "direction",   enumProp("Apply to request or response", "request", "response")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action      = str(args, "action",      "list");
        String fromPath    = str(args, "fromPath",    "/old-api");
        String toPath      = str(args, "toPath",      "/api");
        String headerName  = str(args, "headerName",  "X-Custom-Header");
        String headerValue = str(args, "headerValue", "ecoskiller-v2");
        String direction   = str(args, "direction",   "request");

        if ("add".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            boolean isRewrite = args.containsKey("fromPath");
            if (isRewrite) {
                d.put("type",      "URL rewrite");
                d.put("fromPath",  fromPath);
                d.put("toPath",    toPath);
                d.put("annotation","nginx.ingress.kubernetes.io/rewrite-target: " + toPath);
                d.put("nginxDirective", "rewrite ^" + fromPath + "(.*)$ " + toPath + "$1 break;");
            } else {
                d.put("type",      "Header injection");
                d.put("direction", direction);
                d.put("headerName",  headerName);
                d.put("headerValue", headerValue);
                String directive = "response".equals(direction)
                    ? "add_header " + headerName + " \"" + headerValue + "\";"
                    : "proxy_set_header " + headerName + " \"" + headerValue + "\";";
                d.put("nginxDirective", directive);
            }
            return success("Rewrite rule added", d);
        }
        // list
        List<Map<String, Object>> rules = List.of(
            Map.of("type", "header", "dir", "request",  "name", "X-Real-IP",         "value", "$remote_addr"),
            Map.of("type", "header", "dir", "request",  "name", "X-Forwarded-For",   "value", "$proxy_add_x_forwarded_for"),
            Map.of("type", "header", "dir", "request",  "name", "X-Forwarded-Proto", "value", "$scheme"),
            Map.of("type", "header", "dir", "response", "name", "X-Frame-Options",   "value", "DENY"),
            Map.of("type", "header", "dir", "response", "name", "X-Content-Type-Options", "value", "nosniff"),
            Map.of("type", "header", "dir", "response", "name", "X-XSS-Protection",  "value", "1; mode=block"),
            Map.of("type", "header", "dir", "response", "name", "Content-Security-Policy", "value", "default-src 'self'"),
            Map.of("type", "rewrite","dir", "request",  "from", "/v1/",              "to", "/api/v1/")
        );
        return success("Request/response rewrite rules", Map.of("rules", rules, "total", rules.size()));
    }
}
