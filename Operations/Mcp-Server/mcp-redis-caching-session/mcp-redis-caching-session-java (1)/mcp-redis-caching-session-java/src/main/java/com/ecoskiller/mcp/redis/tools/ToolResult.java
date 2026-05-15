package com.ecoskiller.mcp.redis.tools;

/**
 * Result returned by every MCP tool execution.
 *
 * @param content  JSON or plain-text string to return to the MCP client
 * @param isError  true if this result represents a tool-level error
 */
public record ToolResult(String content, boolean isError) {

    /** Convenience factory for successful results. */
    public static ToolResult ok(String content) {
        return new ToolResult(content, false);
    }

    /** Convenience factory for error results. */
    public static ToolResult error(String message) {
        return new ToolResult("{\"error\":\"" + escape(message) + "\"}", true);
    }

    private static String escape(String s) {
        if (s == null) return "null";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
