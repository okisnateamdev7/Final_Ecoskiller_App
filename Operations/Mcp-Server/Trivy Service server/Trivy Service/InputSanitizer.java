package com.ecoskiller.trivy.security;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * InputSanitizer — Defense layer against:
 *   - Command injection (shell metacharacters in CLI args)
 *   - Path traversal (../ sequences in file paths)
 *   - CVE ID forgery (only accepts valid CVE-YYYY-NNNNN format)
 *   - Oversized inputs (DoS prevention)
 *   - Image name injection (Docker tag/registry manipulation)
 *
 * Called on ALL tool arguments before they reach any tool.
 */
public final class InputSanitizer {

    private InputSanitizer() {}

    // Max allowed length for any single string argument
    private static final int MAX_STRING_LENGTH = 1024;

    // Allowed characters in filesystem paths (no shell metacharacters)
    private static final Pattern SAFE_PATH_CHARS = Pattern.compile("^[a-zA-Z0-9_.\\-/: ]+$");

    // Docker image name: registry/namespace/image:tag or image@sha256:...
    private static final Pattern SAFE_IMAGE_NAME = Pattern.compile(
        "^[a-zA-Z0-9_.\\-/:]+(:[a-zA-Z0-9_.\\-]+)?(@sha256:[a-f0-9]{64})?$"
    );

    // CVE ID format: CVE-YYYY-NNNNN (4-digit year, 4-7 digit number)
    private static final Pattern CVE_ID_PATTERN = Pattern.compile(
        "^CVE-\\d{4}-\\d{4,7}$", Pattern.CASE_INSENSITIVE
    );

    // Shell metacharacters to block in all string inputs
    private static final String[] SHELL_METACHARACTERS = {
        ";", "&&", "||", "`", "$", "(", ")", "{", "}", "<", ">",
        "|", "!", "\\", "\"", "'", "\n", "\r", "\t", "%00"
    };

    /**
     * Sanitizes all string values in the arguments map.
     * Returns a new map with sanitized values.
     *
     * @throws SecurityException if any value fails security validation
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> sanitize(Map<String, Object> args) {
        if (args == null) return new HashMap<>();

        Map<String, Object> sanitized = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String key = sanitizeKey(entry.getKey());
            Object value = entry.getValue();

            if (value instanceof String) {
                sanitized.put(key, sanitizeString(key, (String) value));
            } else if (value instanceof List) {
                sanitized.put(key, sanitizeList(key, (List<Object>) value));
            } else if (value instanceof Map) {
                sanitized.put(key, sanitize((Map<String, Object>) value));
            } else {
                // Booleans, numbers pass through as-is
                sanitized.put(key, value);
            }
        }
        return sanitized;
    }

    private static String sanitizeKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Empty argument key");
        if (!key.matches("[a-zA-Z0-9_]+")) {
            throw new SecurityException("Invalid argument key format: " + key);
        }
        return key;
    }

    private static String sanitizeString(String paramName, String value) {
        if (value == null) return null;

        // Length check
        if (value.length() > MAX_STRING_LENGTH) {
            throw new SecurityException("Argument '" + paramName + "' exceeds max length " + MAX_STRING_LENGTH);
        }

        // Check for null bytes
        if (value.contains("\0")) {
            throw new SecurityException("Null byte detected in argument '" + paramName + "'");
        }

        // Shell metacharacter check for all inputs
        for (String meta : SHELL_METACHARACTERS) {
            if (value.contains(meta)) {
                throw new SecurityException(
                    "Shell metacharacter '" + meta + "' not allowed in argument '" + paramName + "'");
            }
        }

        // Field-specific validation
        if (paramName.toLowerCase().contains("path") || paramName.equalsIgnoreCase("input")) {
            validatePath(paramName, value);
        }

        if (paramName.equalsIgnoreCase("image") || paramName.equalsIgnoreCase("image_ref")) {
            validateImageName(paramName, value);
        }

        if (paramName.equalsIgnoreCase("cve_id")) {
            validateCveId(value);
        }

        return value.trim();
    }

    /**
     * Validates a filesystem path:
     *   - No path traversal (..)
     *   - Must resolve within /tmp, /home, /workspace, /var, /mnt (allowed roots)
     *   - Only safe characters allowed
     */
    public static void validatePath(String paramName, String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path '" + paramName + "' cannot be empty");
        }

        // Block path traversal
        String normalized = path.replace("\\", "/");
        if (normalized.contains("../") || normalized.contains("/..") || normalized.equals("..")) {
            throw new SecurityException("Path traversal attempt detected in '" + paramName + "'");
        }

        // Safe character set
        if (!SAFE_PATH_CHARS.matcher(path).matches()) {
            throw new SecurityException("Unsafe characters in path '" + paramName + "': " + path);
        }

        // Resolve and check against allowed roots
        try {
            Path resolved = Paths.get(path).normalize().toAbsolutePath();
            String resolvedStr = resolved.toString();

            List<String> allowedRoots = Arrays.asList(
                "/tmp", "/home", "/workspace", "/var/tmp", "/mnt",
                "/builds", "/gitlab-runner"
            );
            boolean inAllowedRoot = allowedRoots.stream().anyMatch(resolvedStr::startsWith);

            if (!inAllowedRoot) {
                throw new SecurityException(
                    "Path '" + paramName + "' resolves outside allowed directories: " + resolvedStr);
            }
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new SecurityException("Invalid path '" + paramName + "': " + e.getMessage());
        }
    }

    public static void validateImageName(String paramName, String image) {
        if (!SAFE_IMAGE_NAME.matcher(image).matches()) {
            throw new SecurityException(
                "Invalid Docker image name in '" + paramName + "': " + image);
        }
    }

    public static void validateCveId(String cveId) {
        if (!CVE_ID_PATTERN.matcher(cveId).matches()) {
            throw new IllegalArgumentException(
                "Invalid CVE ID format: '" + cveId + "'. Expected format: CVE-YYYY-NNNNN");
        }
    }

    private static List<Object> sanitizeList(String paramName, List<Object> list) {
        List<Object> sanitized = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof String) {
                sanitized.add(sanitizeString(paramName, (String) item));
            } else {
                sanitized.add(item);
            }
        }
        return sanitized;
    }
}
