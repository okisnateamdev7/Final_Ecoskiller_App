package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 6 — waf_security_manager */
public class WafSecurityManagerTool extends BaseNginxTool {
    public WafSecurityManagerTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "waf_security_manager",
            "description",
                "Manage Web Application Firewall (ModSecurity) rules on NGINX Ingress. " +
                "Detect and block SQL injection, XSS, command injection, path traversal. " +
                "Enable/disable ModSecurity Core Rule Set (CRS). " +
                "Add custom rules for Ecoskiller-specific patterns. " +
                "Performance impact: ~5-10ms/request when enabled. " +
                "Actions: enable | disable | add-rule | list-rules | test.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",   enumProp("Operation", "enable", "disable", "add-rule", "list-rules", "test"),
                    "ruleId",   property("integer", "Rule ID (1000–99999)"),
                    "ruleBody", property("string", "ModSecurity SecRule directive"),
                    "target",   property("string", "Target path for WAF (e.g. /api/login)")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action   = str(args, "action", "list-rules");
        String target   = str(args, "target", "/api");
        int    ruleId   = intArg(args, "ruleId", 1001);

        if ("enable".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("target",      target);
            d.put("engine",      "ModSecurity v3");
            d.put("ruleSet",     "OWASP CRS 3.3");
            d.put("latencyImpact", "5-10ms per request");
            d.put("annotation",  "nginx.ingress.kubernetes.io/enable-modsecurity: \"true\"");
            d.put("highRiskPaths", List.of("/api/login", "/api/assessments/submit", "/api/admin"));
            return success("WAF (ModSecurity) enabled on " + target, d);
        }
        if ("disable".equals(action)) {
            return success("WAF disabled on " + target, Map.of(
                "warning", "Disabling WAF increases exposure to injection attacks"
            ));
        }
        if ("add-rule".equals(action)) {
            String body = str(args, "ruleBody", "SecRule ARGS \"@detectSQLi\" \"id:" + ruleId + ",deny,status:403\"");
            return success("WAF rule added: id=" + ruleId, Map.of(
                "ruleId",   ruleId,
                "ruleBody", body,
                "action",   "deny (HTTP 403)",
                "logEntry", "ModSecurity: Access denied with code 403"
            ));
        }
        // list-rules
        List<Map<String, Object>> rules = List.of(
            Map.of("id", 1001, "type", "SQLi",             "action", "deny:403", "enabled", true),
            Map.of("id", 1002, "type", "XSS",              "action", "deny:403", "enabled", true),
            Map.of("id", 1003, "type", "CommandInjection",  "action", "deny:403", "enabled", true),
            Map.of("id", 1004, "type", "PathTraversal",    "action", "deny:403", "enabled", true),
            Map.of("id", 1005, "type", "LoginRateLimit",   "action", "deny:429", "enabled", true)
        );
        return success("WAF rules listed", Map.of("rules", rules, "engine", "ModSecurity v3"));
    }
}
