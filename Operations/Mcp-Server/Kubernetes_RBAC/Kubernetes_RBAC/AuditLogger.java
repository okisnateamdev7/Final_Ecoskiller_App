package com.ecoskiller.mcp.rbac.audit;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * SOC2 / GDPR / HIPAA compliant audit logger.
 *
 * Every RBAC operation is logged with:
 *  - ISO 8601 timestamp
 *  - Event type
 *  - Tool name
 *  - Context (truncated for safety)
 *
 * Logs written to:
 *  1. Java Logger (stderr) — captured by ELK / Datadog in production
 *  2. Append-only local file (rbac-audit.log) for durable record
 */
public class AuditLogger {

    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());
    private static final String AUDIT_FILE = "rbac-audit.log";
    private static final int    MAX_CONTEXT_LENGTH = 500;
    private static final DateTimeFormatter ISO = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .withZone(ZoneOffset.UTC);

    private final Path auditPath;

    public AuditLogger() {
        this.auditPath = Paths.get(AUDIT_FILE);
        try {
            // Create file if not exists — append-only
            if (!Files.exists(auditPath)) {
                Files.createFile(auditPath);
            }
            LOG.info("[AuditLogger] Audit log initialized at: " + auditPath.toAbsolutePath());
        } catch (IOException e) {
            LOG.warning("[AuditLogger] Cannot create audit file: " + e.getMessage() + " — logging to stderr only.");
        }
    }

    /**
     * Write an audit entry.
     *
     * @param eventType  e.g. TOOL_CALL, TOOL_RESULT, VALIDATION_FAIL, TOOL_ERROR
     * @param toolName   MCP tool name
     * @param context    Additional context (truncated for safety)
     */
    public void log(String eventType, String toolName, String context) {
        String timestamp = ISO.format(Instant.now());
        String ctx = context != null
            ? context.substring(0, Math.min(context.length(), MAX_CONTEXT_LENGTH))
            : "";

        // Sanitize for log injection: strip newlines and pipe chars
        ctx = ctx.replace('\n', ' ').replace('\r', ' ').replace('|', ' ');
        toolName = toolName != null ? toolName.replace('\n', '_') : "unknown";

        String entry = String.format("[RBAC-AUDIT] timestamp=%s event=%s tool=%s context=%s",
            timestamp, eventType, toolName, ctx);

        // Write to Java logger (goes to stderr via configured handler)
        LOG.info(entry);

        // Append to audit file for durable record
        appendToFile(entry);
    }

    private void appendToFile(String entry) {
        try (BufferedWriter w = Files.newBufferedWriter(
                auditPath,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            w.write(entry);
            w.newLine();
        } catch (IOException e) {
            LOG.warning("[AuditLogger] Failed to write audit entry: " + e.getMessage());
        }
    }
}
