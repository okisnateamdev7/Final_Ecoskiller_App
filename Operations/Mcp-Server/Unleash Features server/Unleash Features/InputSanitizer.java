package com.ecoskiller.unleash.security;

import java.util.*;
import java.util.regex.Pattern;

/**
 * InputSanitizer — multi-layer injection defence applied to ALL tool arguments:
 *
 *   1. Shell metacharacter blocking  (;  &&  |  `  $  ()  <>  \n  etc.)
 *   2. SQL injection keyword blocking (SELECT, UNION, OR 1=1, --, etc.)
 *   3. Flag name validation           (only [a-zA-Z0-9_-], max 128 chars)
 *   4. Environment enum enforcement   (dev / test / staging / production)
 *   5. Identifier validation          (user/org/team ids — safe chars only)
 *   6. Percentage range check         (0–100 integers only)
 *   7. Oversized input rejection      (max 512 chars per string field)
 *   8. Null-byte detection            (prevents path truncation tricks)
 *
 * Called on ALL incoming args before any tool logic executes.
 */
public final class InputSanitizer {

    private InputSanitizer() {}

    private static final int MAX_LEN = 512;

    /** Flag names: alphanumeric + hyphen + underscore, 1-128 chars, must start with alphanumeric. */
    private static final Pattern FLAG_NAME = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-]{0,127}$");

    /** Safe identifier (userId, orgId, teamName, email-like). */
    private static final Pattern IDENTIFIER = Pattern.compile("^[a-zA-Z0-9@._\\-]{1,128}$");

    /** Allowed environment names. */
    private static final Set<String> ENVIRONMENTS = new HashSet<>(
        Arrays.asList("dev", "test", "staging", "stage", "production", "prod")
    );

    /** Shell metacharacters — any of these in a string → SecurityException. */
    private static final String[] SHELL_META = {
        ";", "&&", "||", "`", "$", "$(", ")", "{", "}", "<", ">",
        "|", "!", "\\", "\n", "\r", "\t", "%00", "\0"
    };

    /** SQL injection indicators. */
    private static final String[] SQL_PATTERNS = {
        " OR ", " AND ", "SELECT ", "INSERT ", "UPDATE ", "DELETE ",
        "DROP ", "UNION ", "--", "/*", "*/", "EXEC ", "EXECUTE ",
        "CAST(", "CONVERT(", "XP_"
    };

    // ── Public API ────────────────────────────────────────────────────────────

    /** Recursively sanitize all string values in an args map. */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> sanitize(Map<String, Object> args) {
        if (args == null) return new HashMap<>();
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : args.entrySet()) {
            String key = validateKey(e.getKey());
            Object val = e.getValue();
            if      (val instanceof String)  out.put(key, sanitizeString(key, (String) val));
            else if (val instanceof List)    out.put(key, sanitizeList(key, (List<Object>) val));
            else if (val instanceof Map)     out.put(key, sanitize((Map<String, Object>) val));
            else                             out.put(key, val);  // booleans, numbers pass through
        }
        return out;
    }

    /** Validate a feature flag name. Throws SecurityException if invalid. */
    public static void validateFlagName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Flag name cannot be empty");
        if (!FLAG_NAME.matcher(name).matches())
            throw new SecurityException(
                "Invalid flag name '" + name + "'. Use alphanumeric + hyphens/underscores (max 128 chars).");
    }

    /** Validate an environment string. */
    public static void validateEnvironment(String env) {
        if (env == null || env.isBlank()) return;
        if (!ENVIRONMENTS.contains(env.toLowerCase()))
            throw new IllegalArgumentException(
                "Invalid environment '" + env + "'. Allowed: dev, test, staging, production.");
    }

    /** Validate an identifier (userId, orgId, teamName). */
    public static void validateIdentifier(String param, String val) {
        if (val == null || val.isBlank())
            throw new IllegalArgumentException(param + " cannot be empty");
        if (!IDENTIFIER.matcher(val).matches())
            throw new SecurityException("Invalid identifier in '" + param + "': " + val);
    }

    /** Parse and validate a percentage integer 0-100. */
    public static int validatePercentage(Object raw, String param) {
        if (raw == null) throw new IllegalArgumentException(param + " is required");
        int pct;
        try { pct = Integer.parseInt(raw.toString()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(param + " must be an integer"); }
        if (pct < 0 || pct > 100)
            throw new IllegalArgumentException(param + " must be 0-100, got: " + pct);
        return pct;
    }

    /** Sanitize a single string value. */
    public static String sanitizeString(String param, String val) {
        if (val == null) return null;
        // Length guard
        if (val.length() > MAX_LEN)
            throw new SecurityException("Argument '" + param + "' exceeds max length " + MAX_LEN);
        // Null-byte check
        if (val.contains("\0"))
            throw new SecurityException("Null byte detected in argument '" + param + "'");
        // Shell metacharacters
        for (String m : SHELL_META)
            if (val.contains(m))
                throw new SecurityException("Disallowed character '" + m + "' in '" + param + "'");
        // SQL injection
        String upper = val.toUpperCase();
        for (String kw : SQL_PATTERNS)
            if (upper.contains(kw.toUpperCase()))
                throw new SecurityException("SQL injection pattern detected in '" + param + "'");
        return val.trim();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static String validateKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Empty argument key");
        if (!key.matches("[a-zA-Z0-9_]+")) throw new SecurityException("Invalid argument key: " + key);
        return key;
    }

    private static List<Object> sanitizeList(String param, List<Object> list) {
        List<Object> out = new ArrayList<>();
        for (Object item : list)
            out.add(item instanceof String ? sanitizeString(param, (String) item) : item);
        return out;
    }
}
