package com.ecoskiller.mcp.jicofo.security;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

// ─────────────────────────────────────────────────────────────────────────────
// RateLimiter — sliding window counter per callerId
// Max `limit` requests per `windowMs` milliseconds
// ─────────────────────────────────────────────────────────────────────────────
public class RateLimiter {

    private final int    limit;
    private final long   windowMs;
    private final ConcurrentHashMap<String, Deque<Long>> windows = new ConcurrentHashMap<>();

    public RateLimiter(int limit, long windowMs) {
        this.limit    = limit;
        this.windowMs = windowMs;
    }

    /**
     * Returns true if the caller is allowed (within rate limit).
     * Thread-safe sliding window implementation.
     */
    public synchronized boolean allow(String callerId) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = windows.computeIfAbsent(callerId, k -> new ArrayDeque<>());

        // Evict entries outside the window
        while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > windowMs) {
            timestamps.pollFirst();
        }

        if (timestamps.size() >= limit) {
            return false; // Rate limit exceeded
        }

        timestamps.addLast(now);
        return true;
    }

    /** Returns remaining quota for a caller */
    public synchronized int remaining(String callerId) {
        long now = System.currentTimeMillis();
        Deque<Long> ts = windows.getOrDefault(callerId, new ArrayDeque<>());
        long count = ts.stream().filter(t -> (now - t) <= windowMs).count();
        return (int) Math.max(0, limit - count);
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// AuditLogger — structured JSON audit log to stderr
//
// Every tool invocation is logged with:
//   timestamp, tool, status, caller, sanitized_args
// Compliant with Ecoskiller's Grafana Loki structured logging format.
// ─────────────────────────────────────────────────────────────────────────────
public class AuditLogger {

    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT;

    /**
     * Log a tool invocation audit event.
     * @param tool     Tool name
     * @param status   INVOKED | SUCCESS | ERROR | DENIED | RATE_LIMITED | NOT_FOUND | EXCEPTION
     * @param caller   Caller identity (sub from JWT)
     * @param args     Input arguments (PII-stripped for security)
     */
    public void log(String tool, String status, String caller, JsonNode args) {
        String timestamp = ISO.format(Instant.now().atZone(ZoneOffset.UTC));
        String sanitizedArgs = sanitizeForLog(args);

        // Structured JSON log line — Grafana Loki compatible
        String logLine = String.format(
            "{\"timestamp\":\"%s\",\"service\":\"jicofo-mcp\",\"event\":\"tool_call\"," +
            "\"tool\":\"%s\",\"status\":\"%s\",\"caller\":\"%s\",\"args\":%s}",
            timestamp, tool, status, caller, sanitizedArgs
        );

        // Log to stderr (stdout is reserved for MCP JSON-RPC responses)
        System.err.println(logLine);
    }

    /** Strip sensitive values from args before logging */
    private String sanitizeForLog(JsonNode args) {
        if (args == null) return "{}";
        try {
            // Clone and redact sensitive fields
            String raw = args.toString();
            // Redact JWT values in log
            raw = raw.replaceAll("\"caller_jwt\"\\s*:\\s*\"[^\"]+\"",
                                 "\"caller_jwt\":\"[REDACTED]\"");
            raw = raw.replaceAll("\"jwt_token\"\\s*:\\s*\"[^\"]+\"",
                                 "\"jwt_token\":\"[REDACTED]\"");
            raw = raw.replaceAll("\"token\"\\s*:\\s*\"[^\"]+\"",
                                 "\"token\":\"[REDACTED]\"");
            // Truncate very long values
            if (raw.length() > 512) {
                raw = raw.substring(0, 509) + "...";
            }
            return raw;
        } catch (Exception e) {
            return "{\"error\":\"log_sanitization_failed\"}";
        }
    }
}
