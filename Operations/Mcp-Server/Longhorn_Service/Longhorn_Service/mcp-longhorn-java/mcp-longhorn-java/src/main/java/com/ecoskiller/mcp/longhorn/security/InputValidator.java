package com.ecoskiller.mcp.longhorn.security;

import java.util.*;
import java.util.regex.*;

/**
 * Security layer: input sanitisation and validation.
 *
 * All tool arguments pass through this validator before execution.
 * Prevents: injection attacks, excessively long inputs, path traversal,
 * and shell metacharacter injection.
 */
public class InputValidator {

    /** Allowed tool names — alphanumeric + underscores only */
    private static final Pattern TOOL_NAME_PATTERN = Pattern.compile("^[a-z][a-z0-9_]{1,63}$");

    /** Allowed Kubernetes resource name pattern (RFC 1123 subdomain) */
    private static final Pattern K8S_NAME_PATTERN = Pattern.compile("^[a-z0-9][a-z0-9\\-\\.]{0,252}[a-z0-9]$");

    /** Max string length for any argument value */
    private static final int MAX_VALUE_LENGTH = 1024;

    /** Max number of arguments per call */
    private static final int MAX_ARG_COUNT = 20;

    /** Characters forbidden in all string arguments */
    private static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\x00-\\x1f\\x7f`$\\\\|;&<>]");

    /** Path traversal detection */
    private static final Pattern PATH_TRAVERSAL = Pattern.compile("\\.{2}[/\\\\]|[/\\\\]\\.{2}");

    /**
     * Validates the tool name against the allowed pattern.
     * Rejects anything that could be a path, shell expression, or injection attempt.
     */
    public boolean isValidToolName(String toolName) {
        if (toolName == null || toolName.isEmpty()) return false;
        return TOOL_NAME_PATTERN.matcher(toolName).matches();
    }

    /**
     * Validates all arguments in the provided map.
     * Throws SecurityException with a safe message (no argument values leaked) on failure.
     */
    public void validateArguments(Map<String, Object> args) {
        if (args == null) return;

        if (args.size() > MAX_ARG_COUNT) {
            throw new SecurityException("Too many arguments (max " + MAX_ARG_COUNT + ")");
        }

        for (Map.Entry<String, Object> entry : args.entrySet()) {
            validateKey(entry.getKey());
            validateValue(entry.getKey(), entry.getValue());
        }
    }

    private void validateKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new SecurityException("Argument key must not be empty");
        }
        if (key.length() > 128) {
            throw new SecurityException("Argument key too long");
        }
        if (!key.matches("[a-zA-Z][a-zA-Z0-9_]{0,127}")) {
            throw new SecurityException("Argument key contains invalid characters");
        }
    }

    @SuppressWarnings("unchecked")
    private void validateValue(String key, Object value) {
        if (value == null) return;

        if (value instanceof String) {
            validateStringValue(key, (String) value);
        } else if (value instanceof Number) {
            // Numbers are inherently safe — no additional validation needed
        } else if (value instanceof Boolean) {
            // Booleans are inherently safe
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (list.size() > 100) {
                throw new SecurityException("List argument '" + key + "' exceeds max size (100)");
            }
            for (Object item : list) {
                validateValue(key, item);
            }
        } else if (value instanceof Map) {
            Map<String, Object> nested = (Map<String, Object>) value;
            validateArguments(nested);
        } else {
            // Unknown type — reject
            throw new SecurityException("Unsupported argument type for key '" + key + "'");
        }
    }

    private void validateStringValue(String key, String value) {
        if (value.length() > MAX_VALUE_LENGTH) {
            throw new SecurityException("Argument '" + key + "' exceeds maximum length (" + MAX_VALUE_LENGTH + ")");
        }

        if (FORBIDDEN_CHARS.matcher(value).find()) {
            throw new SecurityException("Argument '" + key + "' contains forbidden characters");
        }

        if (PATH_TRAVERSAL.matcher(value).find()) {
            throw new SecurityException("Argument '" + key + "' contains path traversal sequence");
        }
    }

    /**
     * Validate a Kubernetes resource name specifically.
     * Use in tool implementations for volume_name / namespace fields.
     */
    public void requireValidK8sName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        if (!K8S_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(
                fieldName + " '" + name + "' is not a valid Kubernetes resource name. " +
                "Use lowercase alphanumeric and hyphens only (RFC 1123)."
            );
        }
    }
}
