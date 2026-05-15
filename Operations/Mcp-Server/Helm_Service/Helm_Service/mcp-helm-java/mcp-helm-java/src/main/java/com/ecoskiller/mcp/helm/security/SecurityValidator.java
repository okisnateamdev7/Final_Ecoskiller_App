package com.ecoskiller.mcp.helm.security;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * SecurityValidator — Helm MCP Server
 *
 * Enforces:
 *   • 15-tool strict allowlist
 *   • Kubernetes resource name validation (RFC 1123 subdomain)
 *   • Namespace allowlist (dev / test / stage / prod / core / analytics / billing)
 *   • Environment enum validation
 *   • Chart name / version sanitization
 *   • Release name length and character constraints
 *   • Revision number: positive integer 1–9999
 *   • Session ID: UUID format
 *   • Participant ID: UUID format
 *   • Duration values: positive integers within bounds
 *   • Injection guard (SQL, shell command, YAML injection, path traversal)
 *   • Values YAML size limit (64 KB)
 *   • Rollback protection: cannot roll back to revision 0
 */
public class SecurityValidator {

    private static final Logger LOG = Logger.getLogger(SecurityValidator.class.getName());

    // ── Tool allowlist ────────────────────────────────────────────────────────
    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "helm_install",
        "helm_upgrade",
        "helm_rollback",
        "helm_uninstall",
        "helm_list_releases",
        "helm_get_status",
        "helm_get_values",
        "helm_get_history",
        "helm_lint_chart",
        "helm_diff_upgrade",
        "session_create",
        "session_join",
        "session_get_status",
        "session_advance_turn",
        "session_get_metrics"
    );

    // ── Kubernetes namespaces allowed in Ecoskiller platform ─────────────────
    private static final Set<String> ALLOWED_NAMESPACES = Set.of(
        "default", "core", "analytics", "billing",
        "dev", "test", "stage", "prod",
        "monitoring", "infrastructure", "kube-system"
    );

    // ── Valid Ecoskiller environments ─────────────────────────────────────────
    private static final Set<String> VALID_ENVIRONMENTS = Set.of(
        "dev", "test", "stage", "prod"
    );

    // ── Valid session states ──────────────────────────────────────────────────
    private static final Set<String> VALID_SESSION_STATES = Set.of(
        "pending", "active", "completed", "archived", "failed", "paused"
    );

    // ── Limits ────────────────────────────────────────────────────────────────
    private static final int MAX_RELEASE_NAME_LEN   = 53;  // Kubernetes limit
    private static final int MAX_CHART_NAME_LEN     = 100;
    private static final int MAX_VALUES_YAML_BYTES  = 65_536; // 64 KB
    private static final int MAX_REVISION           = 9_999;
    private static final int MAX_SESSION_NOTES_LEN  = 500;
    private static final int MAX_PARTICIPANTS        = 20;
    private static final int MAX_INTRO_DURATION_S   = 300;  // 5 min
    private static final int MAX_REBUTTAL_DURATION_S = 120;
    private static final int MAX_CONCLUSION_DURATION_S = 180;

    // ── Patterns ─────────────────────────────────────────────────────────────
    /** RFC 1123: lowercase alphanumeric and hyphens, start/end with alphanumeric */
    private static final Pattern K8S_NAME_RE =
        Pattern.compile("^[a-z0-9]([a-z0-9\\-]{0,51}[a-z0-9])?$");

    private static final Pattern UUID_RE =
        Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}"
                      + "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    /** Chart version: SemVer-like (1.2.3 or 1.2.3-alpha.1) */
    private static final Pattern CHART_VERSION_RE =
        Pattern.compile("^[0-9]+\\.[0-9]+(\\.[0-9]+)?([-+][a-zA-Z0-9._-]+)?$");

    /** Shell / command injection guard */
    private static final Pattern INJECTION_RE = Pattern.compile(
        "(?i)(;\\s*(drop|alter|truncate|delete|exec|execute|select\\s+\\*)" +
        "|<script|javascript:|data:|vbscript:|onload=|onerror=" +
        "|\\.\\.\\/|\\$\\(|`|\\{\\{|\\$\\{" +
        "|&&|\\|\\|" +          // shell chaining
        "|>\\s*/etc|>\\s*/proc" + // path traversal
        "|rm\\s+-rf|chmod\\s+777)"); // dangerous shell

    // ─────────────────────────────────────────────────────────────────────────

    public void validateToolCall(String toolName, JsonNode args) {
        validateToolName(toolName);
        if (args == null || args.isNull()) return;
        validateCommonFields(args);
        validateToolSpecific(toolName, args);
    }

    public void validateToolName(String name) {
        if (name == null || name.isBlank())
            throw new SecurityException("Tool name is required");
        if (!ALLOWED_TOOLS.contains(name)) {
            LOG.warning("Rejected unknown tool: " + name);
            throw new SecurityException("Unknown tool: " + name);
        }
    }

    private void validateCommonFields(JsonNode args) {
        ifPresent(args, "release_name",  v -> validateReleaseName(v));
        ifPresent(args, "namespace",     v -> validateNamespace(v));
        ifPresent(args, "environment",   v -> validateEnvironment(v));
        ifPresent(args, "chart_version", v -> validateChartVersion(v));
        ifPresent(args, "session_id",    v -> validateUuid(v, "session_id"));
        ifPresent(args, "participant_id",v -> validateUuid(v, "participant_id"));
        ifPresent(args, "session_state", v -> {
            if (!VALID_SESSION_STATES.contains(v.toLowerCase()))
                throw new SecurityException("Invalid session_state: " + v);
        });
    }

    private void validateToolSpecific(String tool, JsonNode args) {
        switch (tool) {
            case "helm_install"       -> validateHelmInstall(args);
            case "helm_upgrade"       -> validateHelmUpgrade(args);
            case "helm_rollback"      -> validateHelmRollback(args);
            case "helm_uninstall"     -> validateHelmUninstall(args);
            case "helm_diff_upgrade"  -> validateHelmDiffUpgrade(args);
            case "helm_lint_chart"    -> validateLintChart(args);
            case "session_create"     -> validateSessionCreate(args);
            case "session_join"       -> validateSessionJoin(args);
            case "session_advance_turn" -> validateAdvanceTurn(args);
        }
    }

    // ── Helm-specific validators ──────────────────────────────────────────────

    private void validateHelmInstall(JsonNode args) {
        requireReleaseName(args);
        requireNamespace(args);
        requireChartName(args);
        validateValuesYaml(args);
    }

    private void validateHelmUpgrade(JsonNode args) {
        requireReleaseName(args);
        requireNamespace(args);
        validateValuesYaml(args);
    }

    private void validateHelmRollback(JsonNode args) {
        requireReleaseName(args);
        requireNamespace(args);
        if (args.has("revision")) {
            int rev = args.get("revision").asInt(-1);
            if (rev < 1 || rev > MAX_REVISION)
                throw new SecurityException(
                    "revision must be 1–" + MAX_REVISION + ", got " + rev);
        }
    }

    private void validateHelmUninstall(JsonNode args) {
        requireReleaseName(args);
        requireNamespace(args);
        // Require explicit confirmation to prevent accidental uninstall
        boolean confirmed = args.path("confirm_uninstall").asBoolean(false);
        if (!confirmed)
            throw new SecurityException(
                "confirm_uninstall must be true to proceed with uninstall");
    }

    private void validateHelmDiffUpgrade(JsonNode args) {
        requireReleaseName(args);
        requireNamespace(args);
        validateValuesYaml(args);
    }

    private void validateLintChart(JsonNode args) {
        if (args.has("chart_name")) {
            String cn = args.get("chart_name").asText();
            if (cn.isBlank() || cn.length() > MAX_CHART_NAME_LEN)
                throw new SecurityException("chart_name must be 1–" + MAX_CHART_NAME_LEN + " chars");
            if (INJECTION_RE.matcher(cn).find())
                throw new SecurityException("Unsafe content in chart_name");
        }
        validateValuesYaml(args);
    }

    private void validateValuesYaml(JsonNode args) {
        if (args.has("values_yaml")) {
            String yaml = args.get("values_yaml").asText();
            if (yaml.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > MAX_VALUES_YAML_BYTES)
                throw new SecurityException("values_yaml exceeds 64 KB limit");
            if (INJECTION_RE.matcher(yaml).find())
                throw new SecurityException("Unsafe content in values_yaml");
        }
    }

    // ── Session validators ────────────────────────────────────────────────────

    private void validateSessionCreate(JsonNode args) {
        if (args.has("max_participants")) {
            int mp = args.get("max_participants").asInt(-1);
            if (mp < 2 || mp > MAX_PARTICIPANTS)
                throw new SecurityException("max_participants must be 2–" + MAX_PARTICIPANTS + ", got " + mp);
        }
        if (args.has("intro_duration_seconds")) {
            int d = args.get("intro_duration_seconds").asInt(-1);
            if (d < 10 || d > MAX_INTRO_DURATION_S)
                throw new SecurityException("intro_duration_seconds must be 10–" + MAX_INTRO_DURATION_S);
        }
        if (args.has("rebuttal_duration_seconds")) {
            int d = args.get("rebuttal_duration_seconds").asInt(-1);
            if (d < 10 || d > MAX_REBUTTAL_DURATION_S)
                throw new SecurityException("rebuttal_duration_seconds must be 10–" + MAX_REBUTTAL_DURATION_S);
        }
        if (args.has("conclusion_duration_seconds")) {
            int d = args.get("conclusion_duration_seconds").asInt(-1);
            if (d < 10 || d > MAX_CONCLUSION_DURATION_S)
                throw new SecurityException("conclusion_duration_seconds must be 10–" + MAX_CONCLUSION_DURATION_S);
        }
        if (args.has("notes")) {
            String n = args.get("notes").asText();
            if (n.length() > MAX_SESSION_NOTES_LEN)
                throw new SecurityException("notes exceeds " + MAX_SESSION_NOTES_LEN + " chars");
            if (INJECTION_RE.matcher(n).find())
                throw new SecurityException("Unsafe content in notes");
        }
    }

    private void validateSessionJoin(JsonNode args) {
        if (!args.has("session_id") || args.get("session_id").asText().isBlank())
            throw new SecurityException("session_id is required");
        if (!args.has("participant_id") || args.get("participant_id").asText().isBlank())
            throw new SecurityException("participant_id is required");
        // consent_given must be true (DPDP 2023 compliance)
        boolean consent = args.path("consent_given").asBoolean(false);
        if (!consent)
            throw new SecurityException(
                "consent_given must be true — DPDP 2023 requires explicit participant consent");
    }

    private void validateAdvanceTurn(JsonNode args) {
        if (!args.has("session_id") || args.get("session_id").asText().isBlank())
            throw new SecurityException("session_id is required");
        if (!args.has("current_speaker_id") || args.get("current_speaker_id").asText().isBlank())
            throw new SecurityException("current_speaker_id is required to advance turn");
    }

    // ── Field validators ──────────────────────────────────────────────────────

    public void validateReleaseName(String name) {
        if (name == null || name.isBlank())
            throw new SecurityException("release_name is required");
        if (name.length() > MAX_RELEASE_NAME_LEN)
            throw new SecurityException("release_name exceeds Kubernetes limit of 53 chars");
        if (!K8S_NAME_RE.matcher(name).matches())
            throw new SecurityException(
                "release_name must be RFC 1123: lowercase alphanumeric and hyphens, " +
                "start/end with alphanumeric, got: " + name);
    }

    public void validateNamespace(String ns) {
        if (ns == null || ns.isBlank())
            throw new SecurityException("namespace is required");
        if (!ALLOWED_NAMESPACES.contains(ns.toLowerCase()))
            throw new SecurityException(
                "namespace '" + ns + "' not in allowed list: " + ALLOWED_NAMESPACES);
    }

    public void validateEnvironment(String env) {
        if (env != null && !env.isBlank() && !VALID_ENVIRONMENTS.contains(env.toLowerCase()))
            throw new SecurityException(
                "environment must be one of: " + VALID_ENVIRONMENTS + ", got: " + env);
    }

    public void validateChartVersion(String ver) {
        if (ver != null && !ver.isBlank() && !CHART_VERSION_RE.matcher(ver).matches())
            throw new SecurityException(
                "chart_version must be SemVer format (e.g. 1.2.3 or 1.2.3-alpha.1), got: " + ver);
    }

    public void validateUuid(String value, String field) {
        if (value == null || value.isBlank())
            throw new SecurityException(field + " is required");
        if (!UUID_RE.matcher(value).matches())
            throw new SecurityException(field + " must be a valid UUID, got: " + value);
    }

    private void requireReleaseName(JsonNode args) {
        validateReleaseName(args.path("release_name").asText(""));
    }

    private void requireNamespace(JsonNode args) {
        validateNamespace(args.path("namespace").asText(""));
    }

    private void requireChartName(JsonNode args) {
        String cn = args.path("chart_name").asText("").trim();
        if (cn.isBlank()) throw new SecurityException("chart_name is required for install");
        if (cn.length() > MAX_CHART_NAME_LEN)
            throw new SecurityException("chart_name exceeds " + MAX_CHART_NAME_LEN + " chars");
        if (INJECTION_RE.matcher(cn).find())
            throw new SecurityException("Unsafe content in chart_name");
    }

    private void ifPresent(JsonNode args, String field, java.util.function.Consumer<String> check) {
        if (args.has(field) && !args.get(field).isNull()) {
            String v = args.get(field).asText().trim();
            if (!v.isBlank()) check.accept(v);
        }
    }
}
