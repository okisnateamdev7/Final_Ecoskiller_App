package io.ecoskiller.prosody.security;

import com.fasterxml.jackson.databind.JsonNode;
import io.ecoskiller.prosody.config.ServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates MCP request payloads for size and structural safety.
 */
public class RequestValidator {

    private static final int MAX_PAYLOAD_BYTES = 65536; // 64KB

    public boolean isValidToolName(String name) {
        return InputSanitizer.isValidToolName(name);
    }

    public List<String> validate(String toolName, JsonNode args) {
        List<String> errors = new ArrayList<>();
        if (args == null) return errors;

        String argsStr = args.toString();
        if (argsStr.length() > MAX_PAYLOAD_BYTES) {
            errors.add("Request payload (" + argsStr.length() + " bytes) exceeds " + MAX_PAYLOAD_BYTES + " byte limit");
        }

        // Check for null bytes (potential injection)
        if (argsStr.contains("\u0000")) {
            errors.add("Request contains null bytes — potential injection attack");
        }

        return errors;
    }
}
