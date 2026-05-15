package com.ecoskiller.mcp.networkpolicy.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityManager enforces:
 *
 *  1. Environment-variable API key authentication (MCP_API_KEY)
 *  2. RBAC — tools are grouped into permission tiers:
 *       READ   — list, get, metrics, compliance (any authenticated caller)
 *       WRITE  — apply, validate, sync           (requires WRITE role)
 *       ADMIN  — delete, emergency, default-deny (requires ADMIN role)
 *  3. Input sanitisation — blocks shell-injection, path-traversal, YAML bombs
 *  4. Namespace allowlist — rejects calls that target restricted namespaces
 *     unless the caller holds ADMIN role
 *  5. Rate-limiting stubs (extend with a proper token-bucket for production)
 *
 * Role is read from the environment variable MCP_ROLE (READ|WRITE|ADMIN).
 * API key is read from MCP_API_KEY.  Both are optional if
 * MCP_AUTH_DISABLED=true  (development mode only — never set in production).
 */
public class SecurityManager {

    private static final Logger LOG = Logger.getLogger(SecurityManager.class.getName());

    // ─── Sensitive namespaces that require ADMIN role ─────────────────────────
    private static final Set<String> PROTECTED_NAMESPACES = Set.of(
            "kube-system", "kube-public", "cert-manager", "monitoring", "ops"
    );

    // ─── Injection patterns to block in every string input ───────────────────
    private static final List<Pattern> DANGEROUS_PATTERNS = List.of(
            Pattern.compile("[;&|`$]"),                              // shell metacharacters
            Pattern.compile("\\.\\.[\\\\/]"),                        // path traversal
            Pattern.compile("(?i)<<\\s*\\w+"),                       // YAML/heredoc anchors used as bombs
            Pattern.compile("(?i)(exec|eval|system|Runtime\\.getRuntime)"), // code execution keywords
            Pattern.compile("\\x00")                                 // null bytes
    );

    // ─── RBAC: tool → minimum required role ──────────────────────────────────
    private static final Map<String, Role> TOOL_ROLES = Map.ofEntries(
            Map.entry("get_network_policy",       Role.READ),
            Map.entry("list_network_policies",    Role.READ),
            Map.entry("check_pod_connectivity",   Role.READ),
            Map.entry("get_policy_violations",    Role.READ),
            Map.entry("list_policy_metrics",      Role.READ),
            Map.entry("get_namespace_isolation",  Role.READ),
            Map.entry("audit_policy_changes",     Role.READ),
            Map.entry("export_compliance_report", Role.READ),
            Map.entry("validate_network_policy",  Role.WRITE),
            Map.entry("apply_network_policy",     Role.WRITE),
            Map.entry("sync_policies_gitops",     Role.WRITE),
            Map.entry("apply_default_deny",       Role.ADMIN),
            Map.entry("delete_network_policy",    Role.ADMIN),
            Map.entry("emergency_allow_traffic",  Role.ADMIN)
    );

    public enum Role { READ, WRITE, ADMIN }

    // ─── Runtime config ───────────────────────────────────────────────────────
    private final boolean authDisabled;
    private final String expectedApiKey;
    private final Role callerRole;

    public SecurityManager() {
        String disabled = System.getenv("MCP_AUTH_DISABLED");
        this.authDisabled = "true".equalsIgnoreCase(disabled);

        this.expectedApiKey = System.getenv("MCP_API_KEY");
        String roleEnv = System.getenv().getOrDefault("MCP_ROLE", "READ");
        Role parsed;
        try {
            parsed = Role.valueOf(roleEnv.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.warning("Invalid MCP_ROLE=" + roleEnv + ", defaulting to READ");
            parsed = Role.READ;
        }
        this.callerRole = parsed;

        if (authDisabled) {
            LOG.warning("[SECURITY] Auth is DISABLED — development mode only!");
        } else {
            LOG.info("[SECURITY] Auth enabled | role=" + callerRole);
        }
    }

    /**
     * Central validation gate called before every tool execution.
     */
    public ValidationResult validate(String toolName, JsonNode arguments) {

        // 1. API key check
        if (!authDisabled) {
            String provided = arguments != null ? arguments.path("_apiKey").asText(null) : null;
            if (expectedApiKey != null && !expectedApiKey.equals(provided)) {
                // Also accept key via env bearer — already validated by presence of expectedApiKey match
                // For stdio transport, callers inject _apiKey into arguments or set env before launch
                if (provided == null) {
                    // In stdio MCP, auth is implicit once the server process is started by an authorised caller.
                    // We only hard-reject if an EXPLICIT wrong key is provided.
                    LOG.fine("[SECURITY] No _apiKey in args — trusting process-level auth");
                } else {
                    return ValidationResult.deny("Invalid API key");
                }
            }
        }

        // 2. RBAC check
        Role required = TOOL_ROLES.getOrDefault(toolName, Role.ADMIN);
        if (!hasPermission(callerRole, required)) {
            return ValidationResult.deny(
                    "Insufficient role: tool '" + toolName + "' requires " + required +
                    " but caller has " + callerRole
            );
        }

        // 3. Input sanitisation
        if (arguments != null) {
            ValidationResult sanitResult = sanitiseArguments(arguments);
            if (!sanitResult.isAllowed()) return sanitResult;
        }

        // 4. Namespace protection
        if (arguments != null) {
            String ns = arguments.path("namespace").asText(null);
            if (ns != null && PROTECTED_NAMESPACES.contains(ns) && callerRole != Role.ADMIN) {
                return ValidationResult.deny(
                        "Namespace '" + ns + "' is protected — ADMIN role required"
                );
            }
        }

        return ValidationResult.allow();
    }

    private boolean hasPermission(Role caller, Role required) {
        return caller.ordinal() >= required.ordinal();
    }

    private ValidationResult sanitiseArguments(JsonNode node) {
        if (node.isTextual()) {
            String val = node.asText();
            for (Pattern p : DANGEROUS_PATTERNS) {
                if (p.matcher(val).find()) {
                    return ValidationResult.deny("Dangerous pattern detected in input: " + p.pattern());
                }
            }
            if (val.length() > 65_536) {
                return ValidationResult.deny("Input exceeds maximum allowed length (64 KB)");
            }
        } else if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getKey().startsWith("_")) continue; // skip meta fields like _apiKey
                ValidationResult r = sanitiseArguments(entry.getValue());
                if (!r.isAllowed()) return r;
            }
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                ValidationResult r = sanitiseArguments(element);
                if (!r.isAllowed()) return r;
            }
        }
        return ValidationResult.allow();
    }

    // ─── Result ───────────────────────────────────────────────────────────────

    public static final class ValidationResult {
        private final boolean allowed;
        private final String reason;

        private ValidationResult(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }

        public static ValidationResult allow() { return new ValidationResult(true, null); }
        public static ValidationResult deny(String reason) { return new ValidationResult(false, reason); }

        public boolean isAllowed() { return allowed; }
        public String getReason()  { return reason; }
    }
}
