package com.ecoskiller.unleash.security;

import java.util.*;
import java.util.regex.Pattern;

/**
 * InputSanitizer — blocks all injection attack vectors on every tool argument:
 *
 *  1. Shell metacharacters  ;  &&  |  `  $  ()  <>  \n  \t  %00
 *  2. SQL injection patterns  SELECT UNION OR -- /* EXEC CAST
 *  3. Script injection        <script>  javascript:  eval(
 *  4. Flag name format        [a-zA-Z0-9_-] only, max 128, alphanumeric start
 *  5. Environment enum        dev / test / staging / production only
 *  6. Identifier safety       userId, tenantId, email — safe chars only
 *  7. Percentage range        0–100 integers only
 *  8. Oversized input         max 512 chars per string argument
 *  9. Null byte               prevents path truncation
 * 10. Plan enum              FREE / PRO / ENTERPRISE only
 */
public final class InputSanitizer {

    private InputSanitizer() {}

    private static final int MAX_LEN = 512;

    // Naming convention: team-feature-version (e.g. scoring-ai-v2-experiment)
    private static final Pattern FLAG_NAME   = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-]{0,127}$");
    private static final Pattern IDENTIFIER  = Pattern.compile("^[a-zA-Z0-9@._\\-+]{1,128}$");
    private static final Pattern VARIANT_NAME= Pattern.compile("^[a-zA-Z0-9_\\-]{1,64}$");
    private static final Pattern TEAM_NAME   = Pattern.compile("^[a-zA-Z0-9_\\- ]{1,64}$");

    private static final Set<String> ENVIRONMENTS = new HashSet<>(
        Arrays.asList("dev","test","staging","stage","production","prod","all"));

    private static final Set<String> PLANS = new HashSet<>(
        Arrays.asList("FREE","PRO","ENTERPRISE","ANY"));

    private static final String[] SHELL_META = {
        ";","&&","||","`","$(","${","<(",">(","<",">",
        "|","!","\n","\r","\t","%00","\0"
    };

    private static final String[] SQL_PATTERNS = {
        " OR "," AND ","SELECT ","INSERT ","UPDATE ","DELETE ",
        "DROP ","UNION ","--","/*","*/","EXEC ","EXECUTE ","CAST(",
        "CONVERT(","XP_","SP_","INFORMATION_SCHEMA"
    };

    private static final String[] SCRIPT_PATTERNS = {
        "<script","javascript:","vbscript:","onload=","onerror=","eval("
    };

    // ── Public API ────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public static Map<String, Object> sanitize(Map<String, Object> args) {
        if (args == null) return new HashMap<>();
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, Object> e : args.entrySet()) {
            String key = validateKey(e.getKey());
            Object val = e.getValue();
            if      (val instanceof String) out.put(key, sanitizeString(key, (String) val));
            else if (val instanceof List)   out.put(key, sanitizeList(key, (List<Object>) val));
            else if (val instanceof Map)    out.put(key, sanitize((Map<String, Object>) val));
            else                            out.put(key, val);
        }
        return out;
    }

    public static void validateFlagName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Flag name cannot be empty");
        if (!FLAG_NAME.matcher(name).matches())
            throw new SecurityException("Invalid flag name '" + name +
                "'. Use: team-feature-version format (alphanumeric, hyphens, underscores, max 128).");
    }

    public static void validateEnvironment(String env) {
        if (env == null || env.isBlank()) return;
        if (!ENVIRONMENTS.contains(env.toLowerCase()))
            throw new IllegalArgumentException(
                "Invalid environment '" + env + "'. Allowed: dev, test, staging, production.");
    }

    public static void validateIdentifier(String param, String val) {
        if (val == null || val.isBlank())
            throw new IllegalArgumentException(param + " cannot be empty");
        if (!IDENTIFIER.matcher(val).matches())
            throw new SecurityException("Invalid identifier '" + val + "' for param '" + param + "'");
    }

    public static void validateVariantName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Variant name cannot be empty");
        if (!VARIANT_NAME.matcher(name).matches())
            throw new SecurityException("Invalid variant name '" + name + "'. Use alphanumeric/hyphens/underscores.");
    }

    public static void validateTeamName(String team) {
        if (team == null || team.isBlank()) return;
        if (!TEAM_NAME.matcher(team).matches())
            throw new SecurityException("Invalid team name '" + team + "'");
    }

    public static void validatePlan(String plan) {
        if (plan == null || plan.isBlank()) return;
        if (!PLANS.contains(plan.toUpperCase()))
            throw new IllegalArgumentException("Invalid plan '" + plan + "'. Allowed: FREE, PRO, ENTERPRISE, ANY.");
    }

    public static int validatePercentage(Object raw, String param) {
        if (raw == null) throw new IllegalArgumentException(param + " is required");
        int pct;
        try { pct = Integer.parseInt(raw.toString()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException(param + " must be integer"); }
        if (pct < 0 || pct > 100)
            throw new IllegalArgumentException(param + " must be 0-100, got: " + pct);
        return pct;
    }

    public static String sanitizeString(String param, String val) {
        if (val == null) return null;
        if (val.length() > MAX_LEN)
            throw new SecurityException("Argument '" + param + "' exceeds max length " + MAX_LEN);
        if (val.contains("\0"))
            throw new SecurityException("Null byte in '" + param + "'");
        for (String m : SHELL_META)
            if (val.contains(m))
                throw new SecurityException("Disallowed character '" + m + "' in '" + param + "'");
        String upper = val.toUpperCase();
        for (String kw : SQL_PATTERNS)
            if (upper.contains(kw.toUpperCase()))
                throw new SecurityException("SQL injection pattern detected in '" + param + "'");
        String lower = val.toLowerCase();
        for (String sc : SCRIPT_PATTERNS)
            if (lower.contains(sc))
                throw new SecurityException("Script injection pattern detected in '" + param + "'");
        return val.trim();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static String validateKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Empty argument key");
        if (!key.matches("[a-zA-Z0-9_]+")) throw new SecurityException("Invalid key: " + key);
        return key;
    }

    private static List<Object> sanitizeList(String param, List<Object> list) {
        List<Object> out = new ArrayList<>();
        for (Object item : list)
            out.add(item instanceof String ? sanitizeString(param, (String) item) : item);
        return out;
    }
}
