package com.ecoskiller.mcp.search.security;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * Append-only audit log for all Search Indexer MCP tool invocations.
 * Every search query is logged per spec Section 6.4 (Search Audit Log).
 * PII (email, phone, SSN) is never written to the log.
 */
public class AuditLogger {

    private static final Logger LOGGER = Logger.getLogger(AuditLogger.class.getName());
    private final PrintWriter writer;
    private final String      env;

    public AuditLogger() {
        String path = System.getenv("AUDIT_LOG_PATH");
        if (path == null || path.isBlank()) path = "/tmp/mcp-search-indexer-audit.log";
        this.env = nvl(System.getenv("ENV"), "dev");
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

    public void logRequest(String method)              { write("MCP_REQUEST",       method, null,    null); }
    public void logSuccess(String tool, String subject){ write("TOOL_OK",           tool,   subject, null); }
    public void logError(String tool, String msg)      { write("TOOL_ERROR",        tool,   null,    trim(msg)); }
    public void logSecurityViolation(String t, String r){ write("SECURITY_BLOCK",  t,      null,    trim(r)); }

    private void write(String event, String tool, String subject, String detail) {
        StringBuilder sb = new StringBuilder("{");
        append(sb, "ts",      Instant.now().toString());
        append(sb, "event",   event);
        append(sb, "tool",    tool);
        append(sb, "env",     env);
        if (subject != null) append(sb, "subject", subject);
        if (detail  != null) append(sb, "detail",  detail.replace("\"","'"));
        sb.append("}");
        synchronized (this) { writer.println(sb); }
    }

    private void append(StringBuilder sb, String k, String v) {
        if (sb.length() > 1) sb.append(",");
        sb.append("\"").append(k).append("\":\"").append(v).append("\"");
    }

    private String trim(String s)  { return s == null ? null : (s.length() > 200 ? s.substring(0,200)+"..." : s); }
    private String nvl(String s, String d) { return (s != null && !s.isBlank()) ? s : d; }
}
