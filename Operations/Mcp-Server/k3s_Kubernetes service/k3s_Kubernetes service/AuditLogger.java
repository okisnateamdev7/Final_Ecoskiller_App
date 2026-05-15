package com.ecoskiller.mcp.k3s.security;

import java.time.Instant;
import java.util.logging.Logger;

/**
 * Structured audit logger for k3s MCP Server.
 * All security events, tool calls, and violations are logged here.
 * Output goes to stderr (keeps stdout clean for JSON-RPC).
 *
 * Log format: [AUDIT][timestamp][category] message
 */
public class AuditLogger {

    private static final Logger LOGGER = Logger.getLogger(AuditLogger.class.getName());

    /** Log a normal operational event. */
    public void logEvent(String category, String message) {
        LOGGER.info(format("EVENT", category, message));
    }

    /** Log a security violation — always at WARNING level. */
    public void logSecurityViolation(String category, String detail) {
        LOGGER.warning(format("SECURITY_VIOLATION", category, detail));
    }

    /** Log a tool execution result. */
    public void logToolResult(String tool, boolean success, String summary) {
        String level = success ? "EVENT" : "ERROR";
        LOGGER.info(format(level, "TOOL_RESULT", tool + " success=" + success + " | " + summary));
    }

    private String format(String type, String category, String message) {
        return "[AUDIT][" + Instant.now() + "][" + type + "][" + category + "] " + message;
    }
}
