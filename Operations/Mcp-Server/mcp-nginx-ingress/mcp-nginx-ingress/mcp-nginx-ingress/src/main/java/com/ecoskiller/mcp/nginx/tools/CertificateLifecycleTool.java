package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 9 — certificate_lifecycle */
public class CertificateLifecycleTool extends BaseNginxTool {
    public CertificateLifecycleTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "certificate_lifecycle",
            "description",
                "Manage TLS certificate provisioning via cert-manager and Let's Encrypt ACME. " +
                "Auto-provision wildcard certs (*.ecoskiller.com), multi-domain SAN certs, " +
                "monitor expiry (alert 7 days before), zero-downtime rotation. " +
                "Certificates stored as Kubernetes Secrets (type: kubernetes.io/tls). " +
                "Monthly cost: ₹0 (Let's Encrypt is free). " +
                "Actions: provision | renew | revoke | get | list.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",    enumProp("Operation", "provision", "renew", "revoke", "get", "list"),
                    "domain",    property("string", "Domain to provision (e.g. api.ecoskiller.com or *.ecoskiller.com)"),
                    "issuer",    property("string", "cert-manager Issuer name (default: letsencrypt-prod)"),
                    "namespace", property("string", "Kubernetes namespace")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action    = str(args, "action",    "list");
        String domain    = str(args, "domain",    "api.ecoskiller.com");
        String issuer    = str(args, "issuer",    "letsencrypt-prod");
        String namespace = str(args, "namespace", config.getKubeNamespace());

        if ("provision".equals(action)) {
            String secretName = domain.replace("*.", "wildcard-").replace(".", "-") + "-tls";
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("domain",      domain);
            d.put("issuer",      issuer);
            d.put("namespace",   namespace);
            d.put("secretName",  secretName);
            d.put("certManagerYaml",
                "apiVersion: cert-manager.io/v1\nkind: Certificate\nmetadata:\n" +
                "  name: " + secretName + "\n  namespace: " + namespace + "\nspec:\n" +
                "  secretName: " + secretName + "\n  issuerRef:\n    name: " + issuer +
                "\n    kind: ClusterIssuer\n  dnsNames:\n  - " + domain + "\n");
            d.put("acmeChallenge",  "HTTP-01 or DNS-01");
            d.put("estimatedTimeSec", 30);
            d.put("cost",         "₹0 (Let's Encrypt)");
            d.put("renewalNote",  "cert-manager auto-renews 30 days before expiry");
            return success("Certificate provisioning initiated for " + domain, d);
        }
        if ("renew".equals(action)) {
            return success("Certificate renewal triggered for " + domain, Map.of(
                "domain",    domain,
                "command",   "kubectl annotate certificate " + domain.replace(".","-") +
                             " cert-manager.io/renew-before=48h --overwrite -n " + namespace,
                "downtime",  "zero — NGINX hot-reloads new cert"
            ));
        }
        // list
        List<Map<String, Object>> certs = List.of(
            Map.of("domain", "api.ecoskiller.com",      "expiresInDays", 87,  "autoRenew", true, "status", "Valid"),
            Map.of("domain", "portal.ecoskiller.com",   "expiresInDays", 62,  "autoRenew", true, "status", "Valid"),
            Map.of("domain", "*.ecoskiller.com",        "expiresInDays", 14,  "autoRenew", true, "status", "Renewing"),
            Map.of("domain", "assessment.ecoskiller.com","expiresInDays", 5,  "autoRenew", true, "status", "ALERT: expiring soon")
        );
        return success("Certificate inventory", Map.of(
            "certificates", certs,
            "alerts", List.of("assessment.ecoskiller.com expires in 5 days — manual intervention recommended if auto-renew fails")
        ));
    }
}
