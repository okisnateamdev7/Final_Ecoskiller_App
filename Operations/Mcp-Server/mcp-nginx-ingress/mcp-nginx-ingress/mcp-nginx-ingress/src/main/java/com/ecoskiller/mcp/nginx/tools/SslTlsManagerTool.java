package com.ecoskiller.mcp.nginx.tools;

import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/**
 * Tool 2 — ssl_tls_manager
 *
 * Manages SSL/TLS configuration: TLS versions, cipher suites, HSTS,
 * cert secrets, OCSP stapling, session cache.
 */
public class SslTlsManagerTool extends BaseNginxTool {

    public SslTlsManagerTool(ServerConfig config) { super(config); }

    @Override
    public Map<String, Object> descriptor() {
        return Map.of(
            "name", "ssl_tls_manager",
            "description",
                "Manage SSL/TLS configuration on the NGINX Ingress Controller. " +
                "Configure TLS protocols (TLS 1.2/1.3), cipher suites, HSTS headers, " +
                "OCSP stapling, SSL session cache, and link TLS secrets to Ingress resources. " +
                "Disable weak protocols (TLS 1.0/1.1, SSL 3.0). " +
                "Actions: configure | rotate | get | list | disable.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",     enumProp("Operation", "configure", "rotate", "get", "list", "disable"),
                    "domain",     property("string", "Target domain (e.g. api.ecoskiller.com)"),
                    "protocols",  enumProp("TLS versions to enable", "TLSv1.2", "TLSv1.3", "TLSv1.2 TLSv1.3"),
                    "secretName", property("string", "Kubernetes TLS secret name"),
                    "namespace",  property("string", "Kubernetes namespace")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override
    public Object execute(Map<String, Object> args) {
        String action    = str(args, "action",    "get");
        String domain    = str(args, "domain",    "api.ecoskiller.com");
        String protocols = str(args, "protocols", config.getTlsProtocols());
        String secretName = str(args, "secretName", "ecoskiller-tls");

        switch (action) {
            case "configure": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("domain",         domain);
                d.put("protocols",      protocols);
                d.put("ciphers",        List.of(
                    "TLS_AES_256_GCM_SHA384",
                    "TLS_CHACHA20_POLY1305_SHA256",
                    "ECDHE-RSA-AES128-GCM-SHA256",
                    "ECDHE-RSA-CHACHA20-POLY1305"
                ));
                d.put("disabledCiphers", List.of("MD5", "DES", "3DES", "NULL"));
                d.put("hsts",           Map.of(
                    "enabled",       true,
                    "maxAge",        config.getHstsMaxAgeSec(),
                    "includeSubDomains", true,
                    "preload",       config.isHstsPreload()
                ));
                d.put("ocspStapling",   true);
                d.put("sessionCache",   Map.of("sizeMb", config.getSslSessionCacheMb(), "timeoutSec", 86400));
                d.put("secretName",     secretName);
                d.put("nginxDirectives", List.of(
                    "ssl_protocols " + protocols + ";",
                    "ssl_ciphers HIGH:!aNULL:!MD5;",
                    "ssl_session_cache shared:SSL:" + config.getSslSessionCacheMb() + "m;",
                    "ssl_session_timeout 1d;",
                    "ssl_stapling on;",
                    "ssl_stapling_verify on;",
                    "add_header Strict-Transport-Security \"max-age=" + config.getHstsMaxAgeSec() +
                        "; includeSubDomains; preload\";"
                ));
                return success("TLS configured for " + domain, d);
            }
            case "rotate": {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("domain",      domain);
                d.put("secretName",  secretName);
                d.put("method",      "cert-manager ACME (Let's Encrypt)");
                d.put("command",     "kubectl annotate certificate " + domain.replace(".", "-") +
                                     " cert-manager.io/renew-before=1h --overwrite");
                d.put("expectedOutcome", "New certificate issued within 60 seconds, zero-downtime reload");
                d.put("renewalLog",  "Check: kubectl logs -n cert-manager deploy/cert-manager");
                return success("Certificate rotation initiated for " + domain, d);
            }
            case "get":
            default: {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("domain",          domain);
                d.put("protocols",       protocols);
                d.put("secretName",      secretName);
                d.put("expiresInDays",   87);
                d.put("issuer",          "Let's Encrypt");
                d.put("autoRenewal",     true);
                d.put("hstsEnabled",     true);
                d.put("ocspStapling",    true);
                return success("TLS status for " + domain, d);
            }
        }
    }
}
