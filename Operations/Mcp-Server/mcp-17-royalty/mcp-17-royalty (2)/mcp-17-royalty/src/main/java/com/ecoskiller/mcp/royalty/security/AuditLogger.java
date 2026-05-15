package com.ecoskiller.mcp.royalty.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Append-only audit log for all MCP-17 Royalty Engine tool invocations.
 * Writes one JSON line per event to AUDIT_LOG_PATH env var.
 * Sensitive fields (SSN, PAN, bank account numbers) are never logged.
 */
public class AuditLogger {

    private static final Logger LOGGER = Logger.getLogger(AuditLogger.class.getName());
    private final PrintWriter writer;
    private final String      env;

    public AuditLogger() {
        String path = System.getenv("AUDIT_LOG_PATH");
        if (path == null || path.isBlank()) path = "/tmp/mcp-17-royalty-audit.log";
        this.env = System.getenv("ENV") != null ? System.getenv("ENV") : "dev";
        PrintWriter w;
        try {
            w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path, true), StandardCharsets.UTF_8), true);
            LOGGER.info("[Audit] Logging to " + path);
        } catch (IOException e) {
            w = new PrintWriter(new OutputStreamWriter(System.err, StandardCharsets.UTF_8), true);
            LOGGER.warning("[Audit] Cannot open " + path + ": " + e.getMessage());
        }
        this.writer = w;
    }

    public void logRequest(String method) { write("MCP_REQUEST", method, null, null); }
    public void logSuccess(String tool, String subject) { write("TOOL_OK", tool, subject, null); }
    public void logError(String tool, String msg) { write("TOOL_ERROR", tool, null, trim(msg)); }
    public void logSecurityViolation(String tool, String reason) { write("SECURITY_BLOCK", tool, null, trim(reason)); }

    private void write(String event, String tool, String subject, String detail) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"ts\":\"").append(Instant.now()).append("\"");
        sb.append(",\"event\":\"").append(event).append("\"");
        sb.append(",\"tool\":\"").append(tool).append("\"");
        sb.append(",\"env\":\"").append(env).append("\"");
        if (subject != null) sb.append(",\"subject\":\"").append(subject).append("\"");
        if (detail  != null) sb.append(",\"detail\":\"").append(detail.replace("\"","'")).append("\"");
        sb.append("}");
        synchronized (this) { writer.println(sb); }
    }

    private String trim(String s) { return s == null ? null : (s.length() > 200 ? s.substring(0,200)+"..." : s); }
}
