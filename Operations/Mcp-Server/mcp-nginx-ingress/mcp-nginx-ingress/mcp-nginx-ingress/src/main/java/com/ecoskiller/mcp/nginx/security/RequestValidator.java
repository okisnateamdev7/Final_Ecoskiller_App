package com.ecoskiller.mcp.nginx.security;

import com.ecoskiller.mcp.nginx.config.ServerConfig;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Security gate for every inbound request and every tool argument.
 *
 * Design principles:
 *  • Deny by default — anything that doesn't match an allowlist is rejected.
 *  • No secrets ever logged — validation messages scrubbed of token values.
 *  • Injection prevention — hostname, path, CIDR, YAML inputs sanitised
 *    against known injection patterns before they reach any tool.
 *  • Depth limiting — maps/lists truncated to prevent stack-overflow via
 *    deeply-nested JSON.
 */
public class RequestValidator {

    // ── Compiled injection patterns (compiled once, thread-safe) ─────────

    /** Blocks shell metacharacters that could be injected into config templates */
    private static final Pattern SHELL_INJECTION = Pattern.compile(
        "[;&|`$<>\\\\]|\\.\\./"
    );

    /** RFC-1123 hostname validation (allows wildcards like *.ecoskiller.com) */
    private static final Pattern HOSTNAME = Pattern.compile(
        "^(\\*\\.)?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$"
    );

    /** URL path — alphanumeric, slashes, hyphens, underscores, tildes, curly braces for regex  */
    private static final Pattern URL_PATH = Pattern.compile(
        "^[/a-zA-Z0-9_.\\-~{}*^$()\\[\\]|+?]+$"
    );

    /** CIDR notation */
    private static final Pattern CIDR = Pattern.compile(
        "^(\\d{1,3}\\.){3}\\d{1,3}/\\d{1,2}$"
    );

    /** Kubernetes resource name */
    private static final Pattern K8S_NAME = Pattern.compile(
        "^[a-z0-9][a-z0-9\\-.]{0,252}[a-z0-9]?$"
    );

    /** Kubernetes namespace */
    private static final Pattern K8S_NAMESPACE = Pattern.compile(
        "^[a-z0-9][a-z0-9\\-]{0,62}[a-z0-9]?$"
    );

    /** Port number */
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;

    /** Max string length for any single argument value */
    private static final int MAX_VALUE_LEN = 4096;

    /** Max nesting depth for argument maps */
    private static final int MAX_DEPTH = 5;

    /** Allowed cloud providers */
    private static final Set<String> ALLOWED_CLOUDS = Set.of("gcp", "aws", "azure", "on-prem");

    /** Allowed load-balancing algorithms */
    private static final Set<String> ALLOWED_LB_ALGOS = Set.of(
        "round-robin", "least_conn", "ip_hash", "random", "weighted"
    );

    /** Allowed TLS protocol strings */
    private static final Set<String> ALLOWED_TLS = Set.of(
        "TLSv1.2", "TLSv1.3", "TLSv1.2 TLSv1.3"
    );

    /** Allowed auth types */
    private static final Set<String> ALLOWED_AUTH_TYPES = Set.of(
        "oauth2", "oidc", "jwt", "mtls", "api_key", "basic", "none"
    );

    /** Allowed log levels */
    private static final Set<String> ALLOWED_LOG_LEVELS = Set.of(
        "debug", "info", "notice", "warn", "error", "crit", "alert", "emerg"
    );

    /** Allowed path types */
    private static final Set<String> ALLOWED_PATH_TYPES = Set.of(
        "Prefix", "Exact", "ImplementationSpecific"
    );

    @SuppressWarnings("unused")
    private final ServerConfig config;

    public RequestValidator(ServerConfig config) {
        this.config = config;
    }

    // ── JSON-RPC envelope validation ─────────────────────────────────────

    public boolean validateJsonRpc(Map<String, Object> req) {
        if (req == null) return false;
        if (!"2.0".equals(req.get("jsonrpc"))) return false;
        if (!(req.get("method") instanceof String)) return false;
        String method = (String) req.get("method");
        if (method.isBlank() || method.length() > 128) return false;
        // Prevent method injection
        if (!method.matches("[a-zA-Z0-9_/\\-]+")) return false;
        return true;
    }

    // ── Per-tool argument validation ─────────────────────────────────────

    public void validateToolArguments(String toolName, Map<String, Object> args)
            throws SecurityException {
        if (args == null) return;
        checkDepth(args, 0);
        switch (toolName) {
            case "ingress_route_manager":    validateIngressRoute(args);     break;
            case "ssl_tls_manager":          validateSslTls(args);           break;
            case "rate_limit_controller":    validateRateLimit(args);        break;
            case "load_balancer_config":     validateLoadBalancer(args);     break;
            case "health_check_monitor":     validateHealthCheck(args);      break;
            case "waf_security_manager":     validateWaf(args);              break;
            case "auth_enforcement":         validateAuth(args);             break;
            case "canary_deployment":        validateCanary(args);           break;
            case "certificate_lifecycle":    validateCertificate(args);      break;
            case "traffic_shaping":          validateTrafficShaping(args);   break;
            case "websocket_proxy":          validateWebSocket(args);        break;
            case "request_rewrite_engine":   validateRewrite(args);          break;
            case "observability_exporter":   validateObservability(args);    break;
            case "backend_upstream_manager": validateUpstream(args);         break;
            case "configmap_hot_reload":     validateConfigMap(args);        break;
            case "multi_cloud_ingress":      validateMultiCloud(args);       break;
            case "ddos_protection":          validateDdos(args);             break;
            case "audit_compliance":         validateAudit(args);            break;
            default:
                // Unknown tool — already rejected by caller, but be safe
                throw new SecurityException("Unknown tool: " + sanitiseName(toolName));
        }
    }

    // ── Per-tool validators ───────────────────────────────────────────────

    private void validateIngressRoute(Map<String, Object> args) {
        requireString(args, "action",   Set.of("create", "update", "delete", "get", "list"));
        if (args.containsKey("host"))       validateHostname(str(args, "host"));
        if (args.containsKey("path"))       validatePath(str(args, "path"));
        if (args.containsKey("pathType"))   requireInSet(str(args, "pathType"), ALLOWED_PATH_TYPES, "pathType");
        if (args.containsKey("service"))    validateK8sName(str(args, "service"), "service");
        if (args.containsKey("namespace"))  validateK8sNamespace(str(args, "namespace"));
        if (args.containsKey("port"))       validatePort(intVal(args, "port"));
    }

    private void validateSslTls(Map<String, Object> args) {
        requireString(args, "action", Set.of("configure", "rotate", "get", "list", "disable"));
        if (args.containsKey("domain"))    validateHostname(str(args, "domain"));
        if (args.containsKey("protocols")) requireInSet(str(args, "protocols"), ALLOWED_TLS, "protocols");
        if (args.containsKey("secretName")) validateK8sName(str(args, "secretName"), "secretName");
    }

    private void validateRateLimit(Map<String, Object> args) {
        requireString(args, "action", Set.of("set", "remove", "get", "list"));
        if (args.containsKey("rps"))   validatePositiveInt(intVal(args, "rps"),   1, 100_000, "rps");
        if (args.containsKey("burst")) validatePositiveInt(intVal(args, "burst"), 1, 200_000, "burst");
        if (args.containsKey("path"))  validatePath(str(args, "path"));
        if (args.containsKey("cidr"))  validateCidr(str(args, "cidr"));
    }

    private void validateLoadBalancer(Map<String, Object> args) {
        requireString(args, "action", Set.of("configure", "get", "list", "reset"));
        if (args.containsKey("algorithm")) requireInSet(str(args, "algorithm"), ALLOWED_LB_ALGOS, "algorithm");
        if (args.containsKey("service"))   validateK8sName(str(args, "service"), "service");
    }

    private void validateHealthCheck(Map<String, Object> args) {
        requireString(args, "action", Set.of("configure", "get", "force-check", "list"));
        if (args.containsKey("path"))     validatePath(str(args, "path"));
        if (args.containsKey("service"))  validateK8sName(str(args, "service"), "service");
        if (args.containsKey("interval")) validatePositiveInt(intVal(args, "interval"), 1, 3600, "interval");
        if (args.containsKey("timeout"))  validatePositiveInt(intVal(args, "timeout"),  1, 300,  "timeout");
    }

    private void validateWaf(Map<String, Object> args) {
        requireString(args, "action", Set.of("enable", "disable", "add-rule", "list-rules", "test"));
        if (args.containsKey("ruleId")) validatePositiveInt(intVal(args, "ruleId"), 1000, 99999, "ruleId");
        // rule body validated for injection
        if (args.containsKey("ruleBody")) {
            String body = str(args, "ruleBody");
            checkNoShellInjection(body, "ruleBody");
            checkLength(body, "ruleBody", 2048);
        }
    }

    private void validateAuth(Map<String, Object> args) {
        requireString(args, "action",   Set.of("configure", "remove", "get", "test"));
        if (args.containsKey("type"))   requireInSet(str(args, "type"), ALLOWED_AUTH_TYPES, "type");
        if (args.containsKey("path"))   validatePath(str(args, "path"));
        // Never log authUrl values
        if (args.containsKey("authUrl")) {
            String url = str(args, "authUrl");
            checkLength(url, "authUrl", 512);
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                throw new SecurityException("authUrl must be http(s) scheme");
            }
        }
    }

    private void validateCanary(Map<String, Object> args) {
        requireString(args, "action", Set.of("enable", "disable", "set-weight", "get", "list"));
        if (args.containsKey("weight")) validatePositiveInt(intVal(args, "weight"), 0, 100, "weight");
        if (args.containsKey("service")) validateK8sName(str(args, "service"), "service");
    }

    private void validateCertificate(Map<String, Object> args) {
        requireString(args, "action", Set.of("provision", "renew", "revoke", "get", "list"));
        if (args.containsKey("domain"))    validateHostname(str(args, "domain"));
        if (args.containsKey("issuer"))    validateK8sName(str(args, "issuer"), "issuer");
        if (args.containsKey("namespace")) validateK8sNamespace(str(args, "namespace"));
    }

    private void validateTrafficShaping(Map<String, Object> args) {
        requireString(args, "action", Set.of("configure", "reset", "get"));
        if (args.containsKey("maxConns")) validatePositiveInt(intVal(args, "maxConns"), 1, 10_000, "maxConns");
        if (args.containsKey("queueSize")) validatePositiveInt(intVal(args, "queueSize"), 0, 100_000, "queueSize");
        if (args.containsKey("service")) validateK8sName(str(args, "service"), "service");
    }

    private void validateWebSocket(Map<String, Object> args) {
        requireString(args, "action", Set.of("enable", "disable", "configure", "get"));
        if (args.containsKey("path"))    validatePath(str(args, "path"));
        if (args.containsKey("timeout")) validatePositiveInt(intVal(args, "timeout"), 1, 86400, "timeout");
    }

    private void validateRewrite(Map<String, Object> args) {
        requireString(args, "action", Set.of("add", "remove", "list", "test"));
        if (args.containsKey("fromPath")) validatePath(str(args, "fromPath"));
        if (args.containsKey("toPath"))   validatePath(str(args, "toPath"));
        // Prevent header injection
        if (args.containsKey("headerName")) {
            String h = str(args, "headerName");
            if (!h.matches("[a-zA-Z][a-zA-Z0-9\\-]{0,63}")) {
                throw new SecurityException("Invalid headerName format");
            }
        }
        if (args.containsKey("headerValue")) {
            String v = str(args, "headerValue");
            // Block CRLF injection
            if (v.contains("\r") || v.contains("\n")) {
                throw new SecurityException("Header value must not contain CRLF");
            }
            checkLength(v, "headerValue", 512);
        }
    }

    private void validateObservability(Map<String, Object> args) {
        requireString(args, "action", Set.of("metrics", "logs", "traces", "status", "dashboard"));
        if (args.containsKey("logLevel")) requireInSet(
            str(args, "logLevel").toLowerCase(), ALLOWED_LOG_LEVELS, "logLevel"
        );
        if (args.containsKey("service")) validateK8sName(str(args, "service"), "service");
    }

    private void validateUpstream(Map<String, Object> args) {
        requireString(args, "action", Set.of("add", "remove", "list", "drain", "restore"));
        if (args.containsKey("service"))   validateK8sName(str(args, "service"), "service");
        if (args.containsKey("namespace")) validateK8sNamespace(str(args, "namespace"));
        if (args.containsKey("port"))      validatePort(intVal(args, "port"));
    }

    private void validateConfigMap(Map<String, Object> args) {
        requireString(args, "action", Set.of("reload", "validate", "get", "diff"));
        if (args.containsKey("configMapName")) validateK8sName(str(args, "configMapName"), "configMapName");
        if (args.containsKey("namespace"))     validateK8sNamespace(str(args, "namespace"));
    }

    private void validateMultiCloud(Map<String, Object> args) {
        requireString(args, "action", Set.of("sync", "failover", "status", "configure"));
        if (args.containsKey("cloud"))  requireInSet(str(args, "cloud"), ALLOWED_CLOUDS, "cloud");
        if (args.containsKey("region")) {
            String r = str(args, "region");
            if (!r.matches("[a-z0-9\\-]+")) throw new SecurityException("Invalid region format");
        }
    }

    private void validateDdos(Map<String, Object> args) {
        requireString(args, "action", Set.of("configure", "block-ip", "unblock-ip", "status", "list-blocked"));
        if (args.containsKey("ip"))   validateIpOrCidr(str(args, "ip"));
        if (args.containsKey("cidr")) validateCidr(str(args, "cidr"));
    }

    private void validateAudit(Map<String, Object> args) {
        requireString(args, "action", Set.of("report", "export", "status", "configure"));
        if (args.containsKey("service"))   validateK8sName(str(args, "service"), "service");
        if (args.containsKey("namespace")) validateK8sNamespace(str(args, "namespace"));
    }

    // ── Low-level validators ──────────────────────────────────────────────

    public void validateHostname(String host) {
        if (host == null || host.isBlank())
            throw new SecurityException("hostname must not be empty");
        checkLength(host, "hostname", 253);
        if (!HOSTNAME.matcher(host).matches())
            throw new SecurityException("hostname contains invalid characters");
    }

    public void validatePath(String path) {
        if (path == null || path.isBlank())
            throw new SecurityException("path must not be empty");
        if (!path.startsWith("/"))
            throw new SecurityException("path must start with /");
        checkLength(path, "path", 512);
        if (!URL_PATH.matcher(path).matches())
            throw new SecurityException("path contains invalid characters");
        checkNoShellInjection(path, "path");
    }

    public void validateCidr(String cidr) {
        if (cidr == null || !CIDR.matcher(cidr).matches())
            throw new SecurityException("Invalid CIDR format: " + sanitiseName(cidr));
        // Validate each octet
        String[] parts = cidr.split("[./]");
        for (int i = 0; i < 4; i++) {
            int octet = Integer.parseInt(parts[i]);
            if (octet < 0 || octet > 255) throw new SecurityException("CIDR octet out of range");
        }
        int prefix = Integer.parseInt(parts[4]);
        if (prefix < 0 || prefix > 32) throw new SecurityException("CIDR prefix out of range");
    }

    public void validateIpOrCidr(String value) {
        if (value == null) throw new SecurityException("IP/CIDR must not be null");
        if (value.contains("/")) validateCidr(value);
        else validateCidr(value + "/32");  // reuse CIDR validator for single IP
    }

    public void validateK8sName(String name, String field) {
        if (name == null || name.isBlank())
            throw new SecurityException(field + " must not be empty");
        checkLength(name, field, 253);
        if (!K8S_NAME.matcher(name).matches())
            throw new SecurityException(field + " contains invalid characters for a Kubernetes name");
    }

    public void validateK8sNamespace(String ns) {
        if (ns == null || ns.isBlank())
            throw new SecurityException("namespace must not be empty");
        if (!K8S_NAMESPACE.matcher(ns).matches())
            throw new SecurityException("namespace contains invalid characters");
    }

    public void validatePort(int port) {
        if (port < MIN_PORT || port > MAX_PORT)
            throw new SecurityException("port out of range [1-65535]: " + port);
    }

    public void validatePositiveInt(int value, int min, int max, String field) {
        if (value < min || value > max)
            throw new SecurityException(field + " must be in [" + min + "," + max + "], got " + value);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void requireString(Map<String, Object> args, String field, Set<String> allowed) {
        Object v = args.get(field);
        if (v == null) throw new SecurityException("Required field missing: " + field);
        if (!(v instanceof String)) throw new SecurityException(field + " must be a string");
        if (!allowed.contains(v.toString()))
            throw new SecurityException(field + " value not allowed: " + sanitiseName(v.toString()));
    }

    private void requireInSet(String value, Set<String> allowed, String field) {
        if (!allowed.contains(value))
            throw new SecurityException(field + " value '" + sanitiseName(value) + "' not in allowed set");
    }

    private void checkNoShellInjection(String value, String field) {
        if (value != null && SHELL_INJECTION.matcher(value).find())
            throw new SecurityException(field + " contains disallowed shell characters");
    }

    private void checkLength(String value, String field, int max) {
        if (value != null && value.length() > max)
            throw new SecurityException(field + " exceeds max length " + max);
    }

    @SuppressWarnings("unchecked")
    private void checkDepth(Object obj, int depth) {
        if (depth > MAX_DEPTH)
            throw new SecurityException("Request nesting exceeds max depth " + MAX_DEPTH);
        if (obj instanceof Map) {
            Map<?, ?> m = (Map<?, ?>) obj;
            if (m.size() > 64) throw new SecurityException("Too many keys in argument map");
            for (Object v : m.values()) checkDepth(v, depth + 1);
        } else if (obj instanceof List) {
            List<?> l = (List<?>) obj;
            if (l.size() > 128) throw new SecurityException("Argument list too large");
            for (Object v : l) checkDepth(v, depth + 1);
        } else if (obj instanceof String) {
            checkLength((String) obj, "value", MAX_VALUE_LEN);
        }
    }

    private String str(Map<String, Object> args, String key) {
        Object v = args.get(key);
        return (v instanceof String) ? (String) v : String.valueOf(v);
    }

    private int intVal(Map<String, Object> args, String key) {
        Object v = args.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(String.valueOf(v)); }
        catch (NumberFormatException e) { throw new SecurityException(key + " must be an integer"); }
    }

    /** Truncate + remove non-printable chars before including in error messages */
    private String sanitiseName(String s) {
        if (s == null) return "(null)";
        return s.replaceAll("[^\\x20-\\x7E]", "?")
                .substring(0, Math.min(s.length(), 40));
    }
}
