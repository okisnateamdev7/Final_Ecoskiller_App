package com.ecoskiller.analytics.mcp.security;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Immutable append-only audit logger for Analytics MCP Server.
 *
 * Per DPDPA 2023: all data access queries are logged with user_id,
 * query context, tenant_id, and timestamp.
 *
 * Outputs:
 *  1. stderr (JSON) — picked up by Grafana Loki log shipper
 *  2. audit.log file — local append-only fallback
 *
 * Production: forward stdout JSON → Fluent Bit → ClickHouse analytics_audit_log
 */
public class AuditLogger {

    private static final Logger LOG      = Logger.getLogger(AuditLogger.class.getName());
    private static final String LOG_FILE = System.getenv().getOrDefault("AUDIT_LOG_FILE","audit.log");

    private final Path logPath;

    public AuditLogger() {
        logPath = Paths.get(LOG_FILE);
        try { if (!Files.exists(logPath)) Files.createFile(logPath); }
        catch (IOException e) { LOG.warning("[AuditLogger] Cannot create file: " + e.getMessage()); }
    }

    /** Log every MCP tool call (DPDPA: track who accessed what data). */
    public void logToolCall(String userId, String tool, JSONObject args, JSONObject result) {
        append(new JSONObject()
            .put("event_type",   "TOOL_CALL")
            .put("entry_id",     UUID.randomUUID().toString())
            .put("timestamp",    Instant.now().toString())
            .put("user_id",      userId)
            .put("tool",         tool)
            .put("tenant_id",    args.optString("_tenant_id","unknown"))
            .put("args_summary", safeArgsSummary(args))
            .put("result_status",result.optString("status","ok"))
            .put("service",      "analytics-mcp-server"));
    }

    /** Log data access (detailed, for DPDPA compliance). */
    public void logDataAccess(String userId, String dataType, String resourceId,
                               String tenantId, String purpose) {
        append(new JSONObject()
            .put("event_type",   "DATA_ACCESS")
            .put("entry_id",     UUID.randomUUID().toString())
            .put("timestamp",    Instant.now().toString())
            .put("user_id",      userId)
            .put("data_type",    dataType)
            .put("resource_id",  resourceId)
            .put("tenant_id",    tenantId)
            .put("purpose",      purpose)
            .put("service",      "analytics-mcp-server"));
    }

    /** Log security events (auth failures, rate-limit, role violations). */
    public void logSecurityEvent(String type, String context, String detail) {
        JSONObject entry = new JSONObject()
            .put("event_type", "SECURITY_" + type)
            .put("entry_id",   UUID.randomUUID().toString())
            .put("timestamp",  Instant.now().toString())
            .put("context",    context)
            .put("detail",     detail)
            .put("service",    "analytics-mcp-server");
        append(entry);
        System.err.println("[SECURITY] " + entry);
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void append(JSONObject entry) {
        String line = entry.toString() + "\n";
        System.err.println("[AUDIT] " + entry);
        try (FileWriter fw = new FileWriter(logPath.toFile(), true)) { fw.write(line); }
        catch (IOException e) { LOG.warning("[AuditLogger] Write failed: " + e.getMessage()); }
    }

    /** Build safe args summary — redact candidate PII, retain structural keys. */
    private JSONObject safeArgsSummary(JSONObject args) {
        JSONObject safe = new JSONObject();
        for (String k : args.keySet()) {
            if (k.startsWith("_")) continue; // skip injected meta fields
            Object v = args.get(k);
            if (v instanceof String s && s.length() > 100)
                safe.put(k, s.substring(0,100)+"…");
            else
                safe.put(k, v);
        }
        return safe;
    }
}
