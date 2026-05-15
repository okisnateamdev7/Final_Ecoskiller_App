package com.ecoskiller.tie.mcp.security;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Structured audit logger — writes JSON lines to stderr (captured by Kubernetes log agent)
 * and optionally to a file (TIE_AUDIT_LOG_FILE env var).
 *
 * Every call that mutates state or accesses sensitive data is logged with:
 * - ISO-8601 timestamp
 * - client_id (from JWT sub)
 * - action (method:tool)
 * - outcome (OK / WARN / ERROR)
 * - message
 *
 * Log lines are shipped to the compliance-audit-service via Kafka in production.
 */
public class AuditLogger {

    private static final Logger LOG  = Logger.getLogger(AuditLogger.class.getName());
    private static final String FMT  = "{\"ts\":\"%s\",\"svc\":\"tie-mcp\",\"client\":\"%s\",\"action\":\"%s\",\"outcome\":\"%s\",\"msg\":\"%s\"}";
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    private final PrintWriter fileWriter;

    public AuditLogger() {
        String logFile = System.getenv("TIE_AUDIT_LOG_FILE");
        PrintWriter fw = null;
        if (logFile != null && !logFile.isBlank()) {
            try {
                fw = new PrintWriter(new FileWriter(logFile, true), true);
                LOG.info("[AUDIT] Writing audit log to: " + logFile);
            } catch (IOException e) {
                LOG.warning("[AUDIT] Cannot open log file " + logFile + ": " + e.getMessage());
            }
        }
        this.fileWriter = fw;
    }

    public void info(String client, String action, String msg) {
        log(client, action, "INFO", msg);
    }

    public void warn(String client, String action, String msg) {
        log(client, action, "WARN", msg);
    }

    public void error(String client, String action, String msg) {
        log(client, action, "ERROR", msg);
    }

    private void log(String client, String action, String outcome, String msg) {
        String ts   = ISO.format(Instant.now());
        String line = String.format(FMT, ts, sanitize(client), sanitize(action), outcome, sanitize(msg));
        System.err.println(line);
        if (fileWriter != null) {
            fileWriter.println(line);
        }
    }

    /** Prevent log injection by escaping special chars. */
    private String sanitize(String s) {
        if (s == null) return "null";
        return s.replace("\\", "\\\\").replace("\"", "'").replace("\n", " ").replace("\r", "");
    }
}
