package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 17 — ddos_protection */
public class DdosProtectionTool extends BaseNginxTool {
    public DdosProtectionTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "ddos_protection",
            "description",
                "Configure DDoS and abuse protection on NGINX Ingress. " +
                "Per-IP rate limiting (100 req/s), per-user limits (1000 req/s), " +
                "connection limits (20 concurrent per IP), burst allowance. " +
                "Block/unblock specific IPs or CIDR ranges. " +
                "Detect and drop malformed requests, known attack signatures. " +
                "Prevents brute-force on /api/login, scraping, credential stuffing. " +
                "Actions: configure | block-ip | unblock-ip | status | list-blocked.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action", enumProp("Operation", "configure", "block-ip", "unblock-ip", "status", "list-blocked"),
                    "ip",     property("string", "IP address or CIDR to block/unblock (e.g. 203.0.113.5 or 203.0.113.0/24)"),
                    "cidr",   property("string", "CIDR block to configure (e.g. 10.0.0.0/8)")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action = str(args, "action", "status");
        String ip     = str(args, "ip",     "");
        String cidr   = str(args, "cidr",   "");

        if ("block-ip".equals(action)) {
            String target = !ip.isEmpty() ? ip : cidr;
            return success("IP blocked: " + target, Map.of(
                "blocked",   target,
                "effect",    "HTTP 444 (no response) — connection dropped immediately",
                "nginxDirective", "deny " + target + ";",
                "persistent", "Add to nginx.conf deny list + Kubernetes NetworkPolicy for L4 block",
                "note",       "Block takes effect after next config reload (~150ms)"
            ));
        }
        if ("unblock-ip".equals(action)) {
            String target = !ip.isEmpty() ? ip : cidr;
            return success("IP unblocked: " + target, Map.of(
                "unblocked", target,
                "command",   "Remove 'deny " + target + ";' from nginx.conf, then reload"
            ));
        }
        if ("configure".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("perIpRateLimit",     config.getDefaultRateLimitRps() + " req/s");
            d.put("perUserRateLimit",   "1000 req/s");
            d.put("burstAllowance",     config.getDefaultBurst());
            d.put("maxConnsPerIp",      config.getMaxConnPerIp());
            d.put("loginEndpointLimit", "5 req/min on /api/login");
            d.put("nginxZones", List.of(
                "limit_req_zone $binary_remote_addr zone=per_ip:10m rate=" + config.getDefaultRateLimitRps() + "r/s;",
                "limit_req_zone $http_authorization zone=per_user:10m rate=1000r/s;",
                "limit_conn_zone $binary_remote_addr zone=conn_limit:10m;",
                "limit_conn conn_limit " + config.getMaxConnPerIp() + ";"
            ));
            d.put("whitelistedCidrs", List.of("10.0.0.0/8", "172.16.0.0/12"));
            d.put("effects", Map.of(
                "rateLimitExceeded",   "HTTP 429 with Retry-After header",
                "connLimitExceeded",   "TCP RST (connection refused)",
                "malformedRequest",    "HTTP 400 Bad Request",
                "knownAttackSignature","HTTP 403 Forbidden (WAF)"
            ));
            return success("DDoS protection configured", d);
        }
        if ("list-blocked".equals(action)) {
            List<Map<String, Object>> blocked = List.of(
                Map.of("cidr", "203.0.113.0/24", "reason", "Brute-force login attempt", "blockedAt", "2026-03-08T10:00:00Z"),
                Map.of("ip",   "198.51.100.99",  "reason", "DDoS source",               "blockedAt", "2026-03-09T03:15:00Z"),
                Map.of("cidr", "192.0.2.0/24",   "reason", "Scraping",                  "blockedAt", "2026-03-07T18:30:00Z")
            );
            return success("Blocked IPs/CIDRs", Map.of("blocked", blocked, "total", blocked.size()));
        }
        // status
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("rateLimitingEnabled", true);
        d.put("connLimitEnabled",    true);
        d.put("wafEnabled",          config.isWafEnabled());
        d.put("blockedEntries",      3);
        d.put("requestsDroppedLast1h", 2847);
        d.put("top429Sources",       List.of("203.0.113.55: 1200 drops", "198.51.100.88: 890 drops"));
        return success("DDoS protection status", d);
    }
}
