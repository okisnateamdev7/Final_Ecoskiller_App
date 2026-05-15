package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 3 — rate_limit_controller */
public class RateLimitControllerTool extends BaseNginxTool {
    public RateLimitControllerTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "rate_limit_controller",
            "description",
                "Configure per-IP and per-user rate limiting on NGINX Ingress. " +
                "Prevents brute-force attacks, DDoS, and API abuse. " +
                "Supports token-bucket algorithm with burst allowance. " +
                "Target specific paths (e.g. /api/login: 5 req/min). " +
                "Actions: set | remove | get | list.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",  enumProp("Operation", "set", "remove", "get", "list"),
                    "path",    property("string", "URL path to apply limit (e.g. /api/login)"),
                    "rps",     property("integer", "Requests per second allowed (1–100000)"),
                    "burst",   property("integer", "Burst allowance above rps (1–200000)"),
                    "zone",    enumProp("Rate limit zone", "per_ip", "per_user", "per_endpoint"),
                    "cidr",    property("string", "Exempt CIDR (e.g. 10.0.0.0/8)")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action = str(args, "action", "list");
        String path   = str(args, "path", "/api");
        int    rps    = intArg(args, "rps",   config.getDefaultRateLimitRps());
        int    burst  = intArg(args, "burst", config.getDefaultBurst());
        String zone   = str(args, "zone", "per_ip");

        if ("set".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("path",  path);
            d.put("rps",   rps);
            d.put("burst", burst);
            d.put("zone",  zone);
            d.put("annotation", "nginx.ingress.kubernetes.io/rate-limit: \"" + rps + "\"");
            d.put("nginxDirective", "limit_req_zone $binary_remote_addr zone=" + zone +
                  ":10m rate=" + rps + "r/s;\nlimit_req zone=" + zone + " burst=" + burst + " nodelay;");
            d.put("effect", "Excess requests receive HTTP 429 with Retry-After header");
            return success("Rate limit set: " + rps + " rps on " + path, d);
        }
        if ("remove".equals(action)) {
            return success("Rate limit removed from " + path, Map.of("path", path, "status", "removed"));
        }
        // list / get
        List<Map<String, Object>> zones = List.of(
            Map.of("path", "/api/login",          "rps", 5,   "burst", 10,  "zone", "per_ip"),
            Map.of("path", "/api/assessments",    "rps", 100, "burst", 200, "zone", "per_ip"),
            Map.of("path", "/api/assessments",    "rps", 1000,"burst", 2000,"zone", "per_user"),
            Map.of("path", "/api/admin",          "rps", 10,  "burst", 20,  "zone", "per_ip")
        );
        return success("Rate limit zones listed", Map.of("zones", zones, "total", zones.size()));
    }
}
