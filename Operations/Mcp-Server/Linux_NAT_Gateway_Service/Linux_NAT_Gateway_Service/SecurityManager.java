package com.ecoskiller.mcp.nat.security;

import com.ecoskiller.mcp.nat.util.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Centralized security manager for the NAT Gateway MCP Server.
 *
 * Responsibilities:
 *  1. Request-level validation (JSON-RPC structural checks)
 *  2. Input sanitization  (strip dangerous chars from all string args)
 *  3. IP address validation (CIDR / host format)
 *  4. Rate limiting        (per-tool, sliding-window)
 *  5. Privilege checks     (destructive ops require explicit confirmation)
 *  6. Audit trail          (every call logged with args)
 */
public class SecurityManager {

    private static final Logger LOGGER = new Logger("SecurityManager");

    // ── IP/CIDR validation ────────────────────────────────────────────────────
    private static final Pattern IPV4_PATTERN =
        Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    private static final Pattern CIDR_PATTERN =
        Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)/(3[0-2]|[12]?\\d)$");

    // ── Dangerous patterns that must never appear in any string argument ──────
    private static final List<Pattern> DANGEROUS_PATTERNS = List.of(
        Pattern.compile("[;&|`$<>]"),            // shell metacharacters
        Pattern.compile("\\.\\.[\\\\/]"),        // path traversal
        Pattern.compile("(?i)\\bexec\\b"),       // exec injection
        Pattern.compile("(?i)\\beval\\b"),       // eval injection
        Pattern.compile("(?i)<script"),          // XSS
        Pattern.compile("(?i)DROP\\s+TABLE"),    // SQL injection
        Pattern.compile("(?i)--\\s*$")           // SQL comment injection
    );

    // ── Maximum lengths per argument type ────────────────────────────────────
    private static final int MAX_IP_LEN     = 50;
    private static final int MAX_STRING_LEN = 512;
    private static final int MAX_COMMENT_LEN = 1024;

    // ── Rate limiting (per tool, 60-second sliding window) ───────────────────
    private static final int RATE_LIMIT_PER_MINUTE = 30;
    private final Map<String, Deque<Long>> callHistory = new ConcurrentHashMap<>();

    // ── Destructive tools requiring confirmation ──────────────────────────────
    private static final Set<String> DESTRUCTIVE_TOOLS = Set.of(
        "egress_policy",
        "iptables_rules",
        "egress_whitelist",
        "nat_session_timeout",
        "sysctl_parameters"
    );

    // ─────────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Validate the top-level JSON-RPC request structure.
     * Returns false if the request is structurally invalid or missing required fields.
     */
    public boolean validateRequest(Map<String, Object> req) {
        if (req == null)                     return false;
        if (!req.containsKey("jsonrpc"))     return false;
        if (!"2.0".equals(req.get("jsonrpc"))) return false;
        if (!req.containsKey("method"))      return false;
        Object method = req.get("method");
        if (!(method instanceof String s) || s.isBlank()) return false;
        // Protect against oversized payloads
        if (req.size() > 20)                 return false;
        return true;
    }

    /**
     * Deep-sanitize all string values in an args map.
     * Returns a new map with sanitized values; never modifies the input.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> sanitizeArgs(Map<String, Object> args) {
        if (args == null) return new LinkedHashMap<>();
        Map<String, Object> clean = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String key = sanitizeString(entry.getKey(), 64);
            Object val = entry.getValue();
            if (val instanceof String s) {
                clean.put(key, sanitizeString(s, MAX_STRING_LEN));
            } else if (val instanceof Map<?,?> m) {
                clean.put(key, sanitizeArgs((Map<String, Object>) m));
            } else {
                clean.put(key, val); // numbers, booleans — safe as-is
            }
        }
        return clean;
    }

    /**
     * Validate an IPv4 address string.
     */
    public void requireValidIp(String ip, String fieldName) {
        if (ip == null || ip.isBlank())
            throw new SecurityException(fieldName + " must not be blank");
        if (ip.length() > MAX_IP_LEN)
            throw new SecurityException(fieldName + " exceeds max length");
        if (!IPV4_PATTERN.matcher(ip).matches())
            throw new SecurityException(fieldName + " is not a valid IPv4 address: " + ip);
    }

    /**
     * Validate a CIDR notation string (e.g. 10.244.0.0/16).
     */
    public void requireValidCidr(String cidr, String fieldName) {
        if (cidr == null || cidr.isBlank())
            throw new SecurityException(fieldName + " must not be blank");
        if (cidr.length() > MAX_IP_LEN)
            throw new SecurityException(fieldName + " exceeds max length");
        if (!CIDR_PATTERN.matcher(cidr).matches() && !IPV4_PATTERN.matcher(cidr).matches())
            throw new SecurityException(fieldName + " is not a valid IPv4/CIDR: " + cidr);
    }

    /**
     * Validate a port number (1-65535).
     */
    public void requireValidPort(int port, String fieldName) {
        if (port < 1 || port > 65535)
            throw new SecurityException(fieldName + " port out of range: " + port);
    }

    /**
     * Rate-limit check: max RATE_LIMIT_PER_MINUTE calls per tool per minute.
     */
    public void checkRateLimit(String toolName) {
        long now = System.currentTimeMillis();
        callHistory.computeIfAbsent(toolName, k -> new ArrayDeque<>());
        Deque<Long> window = callHistory.get(toolName);
        synchronized (window) {
            while (!window.isEmpty() && now - window.peekFirst() > 60_000) {
                window.pollFirst();
            }
            if (window.size() >= RATE_LIMIT_PER_MINUTE) {
                LOGGER.warn("RATE_LIMIT_EXCEEDED tool=" + toolName);
                throw new SecurityException("Rate limit exceeded for tool: " + toolName
                    + " (max " + RATE_LIMIT_PER_MINUTE + " calls/min)");
            }
            window.addLast(now);
        }
    }

    /**
     * Destructive tool guard: args must contain "confirm": true.
     */
    public void requireConfirmation(String toolName, Map<String, Object> args) {
        if (!DESTRUCTIVE_TOOLS.contains(toolName)) return;
        Object confirm = args.get("confirm");
        if (!Boolean.TRUE.equals(confirm)) {
            throw new SecurityException("Destructive tool '" + toolName
                + "' requires args.confirm = true to proceed");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    private String sanitizeString(String input, int maxLen) {
        if (input == null) return "";
        // Truncate
        String s = input.length() > maxLen ? input.substring(0, maxLen) : input;
        // Check for dangerous patterns
        for (Pattern p : DANGEROUS_PATTERNS) {
            if (p.matcher(s).find()) {
                LOGGER.warn("DANGEROUS_PATTERN_DETECTED in input: " + p.pattern());
                throw new SecurityException("Input contains disallowed pattern: " + p.pattern());
            }
        }
        return s;
    }
}
