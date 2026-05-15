package com.ecoskiller.admin.mcp.security;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Immutable audit logger for the Ecoskiller Admin MCP Server.
 *
 * Writes append-only JSONL audit entries to:
 *   1. stdout (structured JSON for Grafana Loki ingestion)
 *   2. audit.log file (local fallback, append-only)
 *
 * Every admin action is logged: who, what, when, from where, with reason.
 * Retained 7+ years per DPDPA 2023 compliance requirement.
 *
 * In production, forward stdout JSON logs to ClickHouse via a log shipper
 * (Fluent Bit → ClickHouse admin_action_log table).
 */
public class AuditLogger {

    private static final Logger LOG       = Logger.getLogger(AuditLogger.class.getName());
    private static final String LOG_FILE  = System.getenv().getOrDefault(
        "AUDIT_LOG_FILE", "audit.log");

    private final Path logPath;

    public AuditLogger() {
        logPath = Paths.get(LOG_FILE);
        // Ensure file exists (append mode)
        try {
            if (!Files.exists(logPath)) Files.createFile(logPath);
        } catch (IOException e) {
            LOG.warning("[AuditLogger] Cannot create audit log file: " + e.getMessage());
        }
    }

    /**
     * Log a tool call (all admin operations).
     */
    public void logToolCall(String adminId, String toolName, JSONObject args, JSONObject result) {
        // Redact sensitive fields before logging
        JSONObject safeArgs = redact(args);

        JSONObject entry = new JSONObject()
            .put("event_type",   "TOOL_CALL")
            .put("entry_id",     UUID.randomUUID().toString())
            .put("timestamp",    Instant.now().toString())
            .put("admin_id",     adminId)
            .put("tool",         toolName)
            .put("args",         safeArgs)
            .put("result_status", result.optString("status", "ok"))
            .put("service",      "admin-mcp-server");

        append(entry);
    }

    /**
     * Log a security event (auth failure, rate limit, role violation).
     */
    public void logSecurityEvent(String eventType, String context, String detail) {
        JSONObject entry = new JSONObject()
            .put("event_type",  "SECURITY_" + eventType)
            .put("entry_id",    UUID.randomUUID().toString())
            .put("timestamp",   Instant.now().toString())
            .put("context",     context)
            .put("detail",      detail)
            .put("service",     "admin-mcp-server");

        append(entry);
        // Also log to stderr for immediate ops visibility
        System.err.println("[SECURITY] " + entry);
    }

    /**
     * Log a compliance-specific action (DSAR, erasure, score override).
     */
    public void logComplianceAction(String adminId, String action,
                                    String resourceType, String resourceId,
                                    Object oldValue, Object newValue, String reason) {
        JSONObject entry = new JSONObject()
            .put("event_type",    "COMPLIANCE_ACTION")
            .put("entry_id",      UUID.randomUUID().toString())
            .put("timestamp",     Instant.now().toString())
            .put("admin_id",      adminId)
            .put("action",        action)
            .put("resource_type", resourceType)
            .put("resource_id",   resourceId)
            .put("old_value",     oldValue != null ? oldValue.toString() : JSONObject.NULL)
            .put("new_value",     newValue != null ? newValue.toString() : JSONObject.NULL)
            .put("reason",        reason)
            .put("service",       "admin-mcp-server");

        append(entry);
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void append(JSONObject entry) {
        String line = entry.toString() + "\n";
        // 1. Structured stdout (Loki picks this up)
        System.err.println("[AUDIT] " + line.trim());
        // 2. Append-only file
        try (FileWriter fw = new FileWriter(logPath.toFile(), true)) {
            fw.write(line);
        } catch (IOException e) {
            LOG.warning("[AuditLogger] Write failed: " + e.getMessage());
        }
    }

    /** Redact sensitive fields (passwords, tokens, PII) before logging. */
    private JSONObject redact(JSONObject args) {
        JSONObject copy = new JSONObject(args.toMap());
        for (String sensitive : new String[]{"password","token","secret","ssn","dob","card_number"}) {
            if (copy.has(sensitive)) copy.put(sensitive, "[REDACTED]");
        }
        return copy;
    }
}
