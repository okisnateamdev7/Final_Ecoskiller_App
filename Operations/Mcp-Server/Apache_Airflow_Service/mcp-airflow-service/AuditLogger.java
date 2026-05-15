package com.ecoskiller.airflow.mcp.security;

import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Immutable append-only audit logger for the Airflow MCP Server.
 *
 * All DAG triggers, task state changes, and data retention operations
 * are logged for DPDPA 2023 compliance and operational audit trails.
 *
 * Outputs:
 *  1. stderr (JSON) — Grafana Loki ingestion
 *  2. audit.log file — local append-only fallback
 *
 * Production: forward via Fluent Bit → ClickHouse admin_action_log
 */
public class AuditLogger {

    private static final Logger LOG      = Logger.getLogger(AuditLogger.class.getName());
    private static final String LOG_FILE = System.getenv().getOrDefault("AUDIT_LOG_FILE", "airflow-audit.log");

    private final Path logPath;

    public AuditLogger() {
        logPath = Paths.get(LOG_FILE);
        try { if (!Files.exists(logPath)) Files.createFile(logPath); }
        catch (IOException e) { LOG.warning("[AuditLogger] Cannot create file: " + e.getMessage()); }
    }

    public void logToolCall(String userId, String tool, JSONObject args, JSONObject result) {
        append(new JSONObject()
            .put("event_type",   "TOOL_CALL")
            .put("entry_id",     UUID.randomUUID().toString())
            .put("timestamp",    Instant.now().toString())
            .put("user_id",      userId)
            .put("tool",         tool)
            .put("dag_id",       args.optString("dag_id", "-"))
            .put("result_status",result.optString("status", "ok"))
            .put("service",      "airflow-mcp-server"));
    }

    /** Critical: log all DAG triggers and state changes separately (operational audit). */
    public void logDagOperation(String userId, String operation, String dagId,
                                 String runId, String reason) {
        append(new JSONObject()
            .put("event_type",  "DAG_OPERATION")
            .put("entry_id",    UUID.randomUUID().toString())
            .put("timestamp",   Instant.now().toString())
            .put("user_id",     userId)
            .put("operation",   operation)
            .put("dag_id",      dagId)
            .put("run_id",      runId != null ? runId : "-")
            .put("reason",      reason != null ? reason : "-")
            .put("service",     "airflow-mcp-server"));
    }

    /** Log compliance-sensitive operations (data retention, DPDPA cleanups). */
    public void logComplianceOperation(String userId, String operation,
                                        String dagId, String detail) {
        append(new JSONObject()
            .put("event_type",  "COMPLIANCE_OPERATION")
            .put("entry_id",    UUID.randomUUID().toString())
            .put("timestamp",   Instant.now().toString())
            .put("user_id",     userId)
            .put("operation",   operation)
            .put("dag_id",      dagId)
            .put("detail",      detail)
            .put("service",     "airflow-mcp-server"));
    }

    public void logSecurityEvent(String type, String context, String detail) {
        JSONObject entry = new JSONObject()
            .put("event_type", "SECURITY_" + type)
            .put("entry_id",   UUID.randomUUID().toString())
            .put("timestamp",  Instant.now().toString())
            .put("context",    context)
            .put("detail",     detail)
            .put("service",    "airflow-mcp-server");
        append(entry);
        System.err.println("[SECURITY] " + entry);
    }

    private void append(JSONObject entry) {
        String line = entry.toString() + "\n";
        System.err.println("[AUDIT] " + entry);
        try (FileWriter fw = new FileWriter(logPath.toFile(), true)) { fw.write(line); }
        catch (IOException e) { LOG.warning("[AuditLogger] Write failed: " + e.getMessage()); }
    }
}
