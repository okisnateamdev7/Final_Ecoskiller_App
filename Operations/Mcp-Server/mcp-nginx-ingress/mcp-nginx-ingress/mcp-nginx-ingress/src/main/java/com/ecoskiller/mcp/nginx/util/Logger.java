package com.ecoskiller.mcp.nginx.util;

import java.time.Instant;

/**
 * Minimal structured logger.
 * All output goes to stderr (stdout is reserved for MCP JSON-RPC).
 *
 * Security: secrets must never be passed to log methods.
 *           Callers are responsible for scrubbing sensitive data.
 */
public class Logger {

    public enum Level { DEBUG, INFO, WARN, ERROR }

    private final Level threshold;

    public Logger(String levelStr) {
        Level l;
        try { l = Level.valueOf(levelStr.toUpperCase()); }
        catch (Exception e) { l = Level.INFO; }
        this.threshold = l;
    }

    public void debug(String msg) { log(Level.DEBUG, msg); }
    public void info (String msg) { log(Level.INFO,  msg); }
    public void warn (String msg) { log(Level.WARN,  msg); }
    public void error(String msg) { log(Level.ERROR, msg); }

    private void log(Level level, String msg) {
        if (level.ordinal() >= threshold.ordinal()) {
            System.err.println(Instant.now() + " [" + level + "] " + msg);
        }
    }
}
