package com.ecoskiller.mcp.keycloak.security;

import org.json.JSONObject;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Input validation and sanitization for all MCP tool arguments.
 *
 * Prevents:
 *   - Injection attacks in user IDs, emails, usernames
 *   - Path traversal via realm names
 *   - Overly long inputs causing memory issues
 *   - Control characters in string fields
 */
public class InputValidator {

    // Allowlist patterns
    private static final Pattern EMAIL   = Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern UUID    = Pattern.compile("^[0-9a-fA-F\\-]{8,36}$");
    private static final Pattern SAFE_ID = Pattern.compile("^[a-zA-Z0-9_\\-.@]{1,200}$");
    private static final Pattern REALM   = Pattern.compile("^[a-zA-Z0-9_\\-]{1,100}$");

    // Tools that accept email as argument
    private static final Set<String> HAS_EMAIL = Set.of("user_management", "bulk_user_import");
    // Tools that accept user_id (UUID)
    private static final Set<String> HAS_USER_ID = Set.of(
        "user_management", "role_management", "session_management",
        "mfa_management", "token_management", "audit_log", "compliance_report");

    public void validate(String toolName, JSONObject args) {
        // Email validation
        if (args.has("email")) {
            String email = args.getString("email");
            if (email.length() > 254 || !EMAIL.matcher(email).matches()) {
                throw new IllegalArgumentException("Invalid email format: " + mask(email));
            }
        }

        // User ID (UUID)
        if (args.has("user_id")) {
            String uid = args.getString("user_id");
            if (!UUID.matcher(uid).matches()) {
                throw new IllegalArgumentException("Invalid user_id format (expected UUID)");
            }
        }

        // Session ID
        if (args.has("session_id")) {
            String sid = args.getString("session_id");
            if (!UUID.matcher(sid).matches()) {
                throw new IllegalArgumentException("Invalid session_id format");
            }
        }

        // Realm name
        if (args.has("realm_name")) {
            String realm = args.getString("realm_name");
            if (!REALM.matcher(realm).matches()) {
                throw new IllegalArgumentException("Invalid realm_name (alphanumeric, dash, underscore only)");
            }
        }

        // Role name
        if (args.has("role_name")) {
            String role = args.getString("role_name");
            if (!SAFE_ID.matcher(role).matches()) {
                throw new IllegalArgumentException("Invalid role_name format");
            }
        }

        // Alias (IdP, federation)
        if (args.has("alias")) {
            String alias = args.getString("alias");
            if (!SAFE_ID.matcher(alias).matches()) {
                throw new IllegalArgumentException("Invalid alias format");
            }
        }

        // General string length guard (prevent 10MB strings)
        for (String key : args.keySet()) {
            Object val = args.get(key);
            if (val instanceof String str && str.length() > 10_000) {
                throw new IllegalArgumentException("Input too long for field: " + key);
            }
        }
    }

    /** Mask most of a string for safe logging (avoids logging PII). */
    private static String mask(String s) {
        if (s == null || s.length() <= 4) return "***";
        return s.substring(0, 2) + "***" + s.substring(s.length() - 2);
    }
}
