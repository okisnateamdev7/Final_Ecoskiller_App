package com.ecoskiller.mcp.wazuh.security;

import java.time.Instant;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

/**
 * SecurityManager — EcoSkiller Wazuh MCP
 *
 * Responsibilities:
 *  1. Validate and sanitise ALL user-supplied parameters (prevent injection).
 *  2. Block shell metacharacters, SSRF-enabling URLs, path traversal.
 *  3. Enforce allow-lists for severity levels, compliance frameworks, namespaces.
 *  4. Produce an immutable audit log for every tool invocation.
 *  5. Rate-limit destructive / high-privilege operations.
 */
public class SecurityManager {

    private static final Logger AUDIT  = Logger.getLogger("WAZUH_AUDIT");
    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    // ── Allow-lists ────────────────────────────────────────────────────────
    public static final Set<String> ALLOWED_SEVERITIES = Set.of(
        "critical", "high", "medium", "low", "info"
    );

    public static final Set<String> ALLOWED_COMPLIANCE_FRAMEWORKS = Set.of(
        "dpdpa", "gdpr", "pci-dss", "soc2", "cis", "hipaa", "iso27001"
    );

    public static final Set<String> ALLOWED_NAMESPACES = Set.of(
        "core", "realtime", "billing", "analytics", "admin", "ops",
        "media", "velero", "security", "default"
    );

    public static final Set<String> ALLOWED_AGENT_STATUSES = Set.of(
        "active", "disconnected", "pending", "never_connected"
    );

    public static final Set<String> ALLOWED_RULE_GROUPS = Set.of(
        "authentication", "privilege_escalation", "malware", "network",
        "file_integrity", "policy_violation", "kubernetes", "compliance",
        "brute_force", "data_exfiltration", "custom_ecoskiller"
    );

    // ── Patterns ───────────────────────────────────────────────────────────
    private static final Pattern SAFE_ID          = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-]{0,63}$");
    private static final Pattern SAFE_RULE_ID     = Pattern.compile("^\\d{1,6}$");
    private static final Pattern SAFE_IP          = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
    private static final Pattern SAFE_CIDR        = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}/\\d{1,2}$");
    private static final Pattern SAFE_SHA256      = Pattern.compile("^[a-f0-9]{64}$");
    private static final Pattern SAFE_DATE        = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}Z?)?$");
    private static final Pattern SAFE_INT_RANGE   = Pattern.compile("^\\d{1,6}$");
    private static final Pattern SAFE_AGENT_NAME  = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-\\.]{0,63}$");
    private static final Pattern SAFE_WAZUH_URL   = Pattern.compile("^https://[a-zA-Z0-9.\\-]+(:\\d{2,5})?(/[a-zA-Z0-9/\\-_]*)?$");

    // Shell metacharacters — never allowed in any field
    private static final Pattern DANGEROUS_CHARS  = Pattern.compile("[;|&`$(){}\\[\\]<>!\\\\'\"]");
    private static final Pattern PATH_TRAVERSAL   = Pattern.compile("\\.\\./");
    private static final Pattern NULL_BYTES        = Pattern.compile("\\x00");

    // ── Rate-limiting for destructive operations ───────────────────────────
    private final Map<String, Long> lastDestructiveOp = new HashMap<>();
    private static final long RATE_LIMIT_MS = 3_000L; // 3 seconds

    // ── Validation API ─────────────────────────────────────────────────────

    /** Validate a generic resource ID (agent IDs, alert IDs, etc.) */
    public String validateId(String id) {
        if (id == null || id.isBlank()) throw new SecurityException("ID must not be blank");
        if (NULL_BYTES.matcher(id).find())   throw new SecurityException("Null bytes in ID");
        if (DANGEROUS_CHARS.matcher(id).find()) throw new SecurityException("Illegal characters in ID: " + id);
        if (!SAFE_ID.matcher(id.trim()).matches()) throw new SecurityException("ID does not match safe pattern: " + id);
        return id.trim();
    }

    /** Validate a Wazuh numeric rule ID (e.g. "5502", "100001") */
    public String validateRuleId(String ruleId) {
        if (ruleId == null || ruleId.isBlank()) throw new SecurityException("Rule ID must not be blank");
        String trimmed = ruleId.trim();
        if (!SAFE_RULE_ID.matcher(trimmed).matches())
            throw new SecurityException("Invalid rule ID format: " + ruleId + " (must be 1-6 digits)");
        return trimmed;
    }

    /** Validate an IPv4 address */
    public String validateIpAddress(String ip) {
        if (ip == null || ip.isBlank()) throw new SecurityException("IP address must not be blank");
        String trimmed = ip.trim();
        if (!SAFE_IP.matcher(trimmed).matches())
            throw new SecurityException("Invalid IP address format: " + ip);
        // Validate each octet
        String[] parts = trimmed.split("\\.");
        for (String part : parts) {
            int val = Integer.parseInt(part);
            if (val < 0 || val > 255) throw new SecurityException("IP octet out of range: " + ip);
        }
        return trimmed;
    }

    /** Validate an IPv4 CIDR range (e.g. "192.168.1.0/24") */
    public String validateCidr(String cidr) {
        if (cidr == null || cidr.isBlank()) throw new SecurityException("CIDR must not be blank");
        String trimmed = cidr.trim();
        if (!SAFE_CIDR.matcher(trimmed).matches())
            throw new SecurityException("Invalid CIDR format: " + cidr);
        return trimmed;
    }

    /** Validate a severity level against the Wazuh allow-list */
    public String validateSeverity(String severity) {
        if (severity == null || severity.isBlank()) throw new SecurityException("Severity must not be blank");
        String lower = severity.trim().toLowerCase();
        if (!ALLOWED_SEVERITIES.contains(lower))
            throw new SecurityException("Severity not in allow-list: " + severity + ". Allowed: " + ALLOWED_SEVERITIES);
        return lower;
    }

    /** Validate a compliance framework against the allow-list */
    public String validateComplianceFramework(String framework) {
        if (framework == null || framework.isBlank()) throw new SecurityException("Framework must not be blank");
        String lower = framework.trim().toLowerCase();
        if (!ALLOWED_COMPLIANCE_FRAMEWORKS.contains(lower))
            throw new SecurityException("Compliance framework not in allow-list: " + framework
                + ". Allowed: " + ALLOWED_COMPLIANCE_FRAMEWORKS);
        return lower;
    }

    /** Validate a Kubernetes namespace against the allow-list */
    public String validateNamespace(String ns) {
        if (ns == null || ns.isBlank()) throw new SecurityException("Namespace must not be blank");
        String lower = ns.trim().toLowerCase();
        if (!ALLOWED_NAMESPACES.contains(lower))
            throw new SecurityException("Namespace not in allow-list: " + lower + ". Allowed: " + ALLOWED_NAMESPACES);
        return lower;
    }

    /** Validate an agent status against the allow-list */
    public String validateAgentStatus(String status) {
        if (status == null || status.isBlank()) throw new SecurityException("Status must not be blank");
        String lower = status.trim().toLowerCase();
        if (!ALLOWED_AGENT_STATUSES.contains(lower))
            throw new SecurityException("Agent status not in allow-list: " + lower);
        return lower;
    }

    /** Validate a Wazuh agent name (alphanumeric, dash, dot, underscore) */
    public String validateAgentName(String name) {
        if (name == null || name.isBlank()) throw new SecurityException("Agent name must not be blank");
        if (DANGEROUS_CHARS.matcher(name).find()) throw new SecurityException("Illegal chars in agent name");
        if (!SAFE_AGENT_NAME.matcher(name.trim()).matches())
            throw new SecurityException("Agent name does not match safe pattern: " + name);
        return name.trim();
    }

    /** Validate a rule group against the allow-list */
    public String validateRuleGroup(String group) {
        if (group == null || group.isBlank()) throw new SecurityException("Rule group must not be blank");
        String lower = group.trim().toLowerCase();
        if (!ALLOWED_RULE_GROUPS.contains(lower))
            throw new SecurityException("Rule group not in allow-list: " + lower);
        return lower;
    }

    /** Validate a SHA-256 hash */
    public String validateSha256(String hash) {
        if (hash == null || hash.isBlank()) throw new SecurityException("Hash must not be blank");
        String lower = hash.trim().toLowerCase();
        if (!SAFE_SHA256.matcher(lower).matches()) throw new SecurityException("Invalid SHA-256 hash: " + hash);
        return lower;
    }

    /** Validate an ISO-8601 date or datetime string */
    public String validateDate(String date) {
        if (date == null || date.isBlank()) throw new SecurityException("Date must not be blank");
        String trimmed = date.trim();
        if (!SAFE_DATE.matcher(trimmed).matches()) throw new SecurityException("Invalid date format: " + date);
        return trimmed;
    }

    /** Validate a positive integer string (count, limit, etc.) */
    public int validatePositiveInt(String val, int maxVal) {
        if (val == null || val.isBlank()) throw new SecurityException("Integer value must not be blank");
        String trimmed = val.trim();
        if (!SAFE_INT_RANGE.matcher(trimmed).matches())
            throw new SecurityException("Invalid integer value: " + val);
        int parsed = Integer.parseInt(trimmed);
        if (parsed <= 0 || parsed > maxVal)
            throw new SecurityException("Integer out of range: " + parsed + " (max " + maxVal + ")");
        return parsed;
    }

    /**
     * Validate the Wazuh Manager REST API base URL.
     * Must be HTTPS to prevent credential leakage over plain HTTP.
     */
    public String validateWazuhUrl(String url) {
        if (url == null || url.isBlank()) throw new SecurityException("Wazuh URL must not be blank");
        String trimmed = url.trim();
        if (!SAFE_WAZUH_URL.matcher(trimmed).matches())
            throw new SecurityException("Invalid or insecure Wazuh URL: " + url + " (must be https://)");
        return trimmed;
    }

    /**
     * Sanitise free-text fields (descriptions, notes, queries).
     * Strips dangerous characters; truncates at 500 chars.
     */
    public String sanitiseFreeText(String text) {
        if (text == null) return "";
        String cleaned = DANGEROUS_CHARS.matcher(text).replaceAll("_");
        cleaned = PATH_TRAVERSAL.matcher(cleaned).replaceAll("__");
        return cleaned.length() > 500 ? cleaned.substring(0, 500) : cleaned;
    }

    /** Rate-limit protection for destructive/high-privilege operations. */
    public void checkRateLimit(String operationKey) {
        long now = System.currentTimeMillis();
        Long last = lastDestructiveOp.get(operationKey);
        if (last != null && (now - last) < RATE_LIMIT_MS)
            throw new SecurityException("Rate limit: operation '" + operationKey
                + "' called too quickly. Wait " + RATE_LIMIT_MS / 1000 + "s.");
        lastDestructiveOp.put(operationKey, now);
    }

    // ── Audit logging ──────────────────────────────────────────────────────

    public void auditLog(String event, String tool, String args) {
        String entry = String.format("[AUDIT] %s | %s | tool=%s | args=%s",
            Instant.now(), event, tool,
            args != null && args.length() > 512 ? args.substring(0, 512) + "…" : args);
        AUDIT.info(entry);
    }

    public void logViolation(String tool, String reason) {
        AUDIT.warning(String.format("[SECURITY_VIOLATION] %s | tool=%s | reason=%s",
            Instant.now(), tool, reason));
    }
}
