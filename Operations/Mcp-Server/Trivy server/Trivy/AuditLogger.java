package com.ecoskiller.trivy.mcp.security;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Structured audit logger for the Trivy MCP server.
 *
 * Every security-relevant event is written as a JSON line to:
 *   - stderr (captured by Kubernetes log agent / Loki / ELK)
 *   - optionally a file   (TRIVY_AUDIT_LOG_FILE env var)
 *
 * Fields: ts, svc, client, action, outcome, msg
 *
 * In production, these logs flow into the security.trivy_scans table
 * in ClickHouse (as described in §11.5 of the Trivy service spec).
 */
public class AuditLogger {

    private static final Logger LOG = Logger.getLogger(AuditLogger.class.getName());
    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
    private static final String FMT =
            "{\"ts\":\"%s\",\"svc\":\"trivy-mcp\",\"client\":\"%s\","
          + "\"action\":\"%s\",\"outcome\":\"%s\",\"msg\":\"%s\"}";

    private final PrintWriter fileWriter;

    public AuditLogger() {
        String path = System.getenv("TRIVY_AUDIT_LOG_FILE");
        PrintWriter fw = null;
        if (path != null && !path.isBlank()) {
            try {
                fw = new PrintWriter(new FileWriter(path, true), true);
                LOG.info("[AUDIT] Writing audit log to: " + path);
            } catch (IOException e) {
                LOG.warning("[AUDIT] Cannot open log file " + path + ": " + e.getMessage());
            }
        }
        this.fileWriter = fw;
    }

    public void info (String client, String action, String msg) { log(client, action, "INFO",  msg); }
    public void warn (String client, String action, String msg) { log(client, action, "WARN",  msg); }
    public void error(String client, String action, String msg) { log(client, action, "ERROR", msg); }

    private void log(String client, String action, String outcome, String msg) {
        String line = String.format(FMT,
                ISO.format(Instant.now()),
                sanitize(client),
                sanitize(action),
                outcome,
                sanitize(msg));
        System.err.println(line);
        if (fileWriter != null) fileWriter.println(line);
    }

    /** Prevent log injection. */
    private String sanitize(String s) {
        if (s == null) return "null";
        return s.replace("\\", "\\\\").replace("\"", "'").replace("\n", " ").replace("\r", "");
    }
}
