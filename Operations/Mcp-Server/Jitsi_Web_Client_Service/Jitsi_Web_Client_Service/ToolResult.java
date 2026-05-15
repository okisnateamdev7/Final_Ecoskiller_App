package io.ecoskiller.mcp.tools;

import io.ecoskiller.mcp.utils.JsonUtils;
import java.util.Map;

/**
 * Represents the result of a tool invocation.
 */
public class ToolResult {
    private final Map<String, Object> data;
    private final boolean error;

    private ToolResult(Map<String, Object> data, boolean error) {
        this.data  = data;
        this.error = error;
    }

    public static ToolResult ok(Map<String, Object> data) {
        return new ToolResult(data, false);
    }

    public static ToolResult error(Map<String, Object> data) {
        return new ToolResult(data, true);
    }

    public boolean isError() { return error; }

    public String toJson() {
        return JsonUtils.toJson(data);
    }
}

// ────────────────────────────────────────────────────────────────────────────

class ToolNotFoundException extends RuntimeException {
    public ToolNotFoundException(String name) {
        super("Tool not found: " + name);
    }
}

class ToolValidationException extends RuntimeException {
    public ToolValidationException(String message) {
        super(message);
    }
}
