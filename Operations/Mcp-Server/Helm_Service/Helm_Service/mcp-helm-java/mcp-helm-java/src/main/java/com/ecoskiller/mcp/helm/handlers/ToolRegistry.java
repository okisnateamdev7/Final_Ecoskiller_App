package com.ecoskiller.mcp.helm.handlers;

import com.ecoskiller.mcp.helm.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.function.Function;

/**
 * ToolRegistry — 15-tool registry for Helm Service MCP server.
 */
public class ToolRegistry {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Function<JsonNode, Object>> handlers;

    public ToolRegistry(SecurityValidator sv) {
        HelmPackageHandler  helm    = new HelmPackageHandler(sv);
        SessionHandler      session = new SessionHandler(sv);

        this.handlers = Map.ofEntries(
            Map.entry("helm_install",           helm::helmInstall),
            Map.entry("helm_upgrade",           helm::helmUpgrade),
            Map.entry("helm_rollback",          helm::helmRollback),
            Map.entry("helm_uninstall",         helm::helmUninstall),
            Map.entry("helm_list_releases",     helm::helmListReleases),
            Map.entry("helm_get_status",        helm::helmGetStatus),
            Map.entry("helm_get_values",        helm::helmGetValues),
            Map.entry("helm_get_history",       helm::helmGetHistory),
            Map.entry("helm_lint_chart",        helm::helmLintChart),
            Map.entry("helm_diff_upgrade",      helm::helmDiffUpgrade),
            Map.entry("session_create",         session::sessionCreate),
            Map.entry("session_join",           session::sessionJoin),
            Map.entry("session_get_status",     session::sessionGetStatus),
            Map.entry("session_advance_turn",   session::sessionAdvanceTurn),
            Map.entry("session_get_metrics",    session::sessionGetMetrics)
        );
    }

    public Object call(String toolName, JsonNode arguments) {
        Function<JsonNode, Object> h = handlers.get(toolName);
        if (h == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", "Unknown tool: " + toolName);
            return err;
        }
        try {
            return h.apply(arguments);
        } catch (IllegalArgumentException | SecurityException e) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error", e.getMessage());
            return err;
        }
    }

    // ─── Tool definitions ────────────────────────────────────────────────────

    public ArrayNode getToolDefinitions() {
        ArrayNode tools = mapper.createArrayNode();

        // ── Helm K8s Package Manager ──────────────────────────────────────

        tools.add(tool("helm_install",
            "Deploy a new Helm release from a chart onto the Ecoskiller k3s cluster.\n" +
            "Used in the GitLab CI pipeline: lint → test → Trivy scan → docker-build → harbor-push → helm-deploy.\n" +
            "Supports environment-specific value overrides (dev/test/stage/prod).\n" +
            "Returns: release_name, namespace, chart, chart_version, revision=1, status, deployed_at.",
            schema()
                .required("release_name", "namespace", "chart_name")
                .string("release_name",    "Helm release name (RFC 1123: lowercase alphanumeric + hyphens, max 53 chars)")
                .string("namespace",       "Kubernetes namespace: dev | test | stage | prod | core | analytics | billing")
                .string("chart_name",      "Chart name from Harbor registry, e.g. ecoskiller/helm-service")
                .string("chart_version",   "Chart version (SemVer, e.g. 1.2.3). Defaults to latest.")
                .string("environment",     "Target environment: dev | test | stage | prod")
                .string("values_yaml",     "Inline values.yaml overrides (max 64 KB). Environment-specific config.")
                .string("values_file",     "Path to values file, e.g. values/prod.yaml")
                .bool("atomic",            "If true, rolls back on failure (default: true)")
                .bool("wait",              "Wait for resources to be ready (default: true)")
                .integer("timeout_seconds","Deployment timeout in seconds (default: 300)")
                .build()));

        tools.add(tool("helm_upgrade",
            "Upgrade an existing Helm release (or install if not present with --install flag).\n" +
            "Used for blue/green deploys, rolling updates, and environment-specific value overrides.\n" +
            "Each upgrade increments the revision number. Enables one-command rollback.\n" +
            "Returns: release_name, namespace, previous_revision, new_revision, status, upgraded_at.",
            schema()
                .required("release_name", "namespace")
                .string("release_name",    "Existing release to upgrade")
                .string("namespace",       "Kubernetes namespace of the release")
                .string("chart_name",      "Chart name (optional — uses current chart if omitted)")
                .string("chart_version",   "Chart version to upgrade to (optional)")
                .string("environment",     "Target environment: dev | test | stage | prod")
                .string("values_yaml",     "Inline values.yaml overrides (max 64 KB)")
                .bool("install",           "Install if release does not exist (--install flag, default: false)")
                .bool("atomic",            "Roll back on failure (default: true)")
                .bool("force",             "Force resource recreation (default: false)")
                .integer("timeout_seconds","Upgrade timeout in seconds (default: 300)")
                .build()));

        tools.add(tool("helm_rollback",
            "Roll back a Helm release to a specific previous revision.\n" +
            "Enables one-command rollbacks per Ecoskiller infrastructure spec.\n" +
            "If revision=0 or omitted, rolls back to the immediately previous revision.\n" +
            "Returns: release_name, namespace, rolled_back_from_revision, rolled_back_to_revision, status.",
            schema()
                .required("release_name", "namespace")
                .string("release_name", "Release to roll back")
                .string("namespace",    "Kubernetes namespace")
                .integer("revision",    "Target revision number (1-based, positive integer). Omit for previous.")
                .bool("wait",           "Wait for rollback to complete (default: true)")
                .integer("timeout_seconds", "Rollback timeout (default: 300)")
                .build()));

        tools.add(tool("helm_uninstall",
            "Remove a Helm release and all its Kubernetes resources.\n" +
            "DANGEROUS: requires confirm_uninstall=true to prevent accidental removal.\n" +
            "Purges release history from the cluster (use helm_get_history first to review).\n" +
            "Returns: release_name, namespace, uninstalled_at, resources_deleted.",
            schema()
                .required("release_name", "namespace", "confirm_uninstall")
                .string("release_name",    "Release to uninstall")
                .string("namespace",       "Kubernetes namespace")
                .bool("confirm_uninstall", "REQUIRED: must be true. Prevents accidental deletion.")
                .bool("keep_history",      "Preserve release history for audit (default: false)")
                .integer("timeout_seconds","Uninstall timeout (default: 60)")
                .build()));

        tools.add(tool("helm_list_releases",
            "List all Helm releases across the Ecoskiller k3s cluster.\n" +
            "Supports filtering by namespace, environment, and status.\n" +
            "Returns: list of releases with name, namespace, chart, chart_version, revision, status, last_deployed.",
            schema()
                .string("namespace",   "Filter by namespace (optional — all namespaces if omitted)")
                .string("environment", "Filter by environment: dev | test | stage | prod")
                .string("status",      "Filter by status: deployed | failed | pending-install | pending-upgrade | uninstalling")
                .bool("all_namespaces","List across all namespaces (default: true)")
                .build()));

        tools.add(tool("helm_get_status",
            "Get the detailed status of a Helm release.\n" +
            "Returns: release_name, namespace, status, chart, chart_version, revision, " +
            "last_deployed, description, resources (pods, services, configmaps, secrets count).",
            schema()
                .required("release_name", "namespace")
                .string("release_name", "Release name")
                .string("namespace",    "Kubernetes namespace")
                .build()));

        tools.add(tool("helm_get_values",
            "Retrieve the effective values (computed from chart defaults + overrides) for a deployed release.\n" +
            "Returns all values: deployed overrides and chart defaults. Useful for debugging config drift.\n" +
            "Returns: release_name, namespace, values (key-value map), user_supplied_values.",
            schema()
                .required("release_name", "namespace")
                .string("release_name",  "Release name")
                .string("namespace",     "Kubernetes namespace")
                .bool("all_values",      "Return all values including chart defaults (default: false = user-supplied only)")
                .build()));

        tools.add(tool("helm_get_history",
            "Retrieve the full revision history of a Helm release.\n" +
            "Each revision entry: revision, updated, status, chart, app_version, description.\n" +
            "Use before helm_rollback to identify the target revision.",
            schema()
                .required("release_name", "namespace")
                .string("release_name", "Release name")
                .string("namespace",    "Kubernetes namespace")
                .integer("max",         "Max revisions to return (default: 20)")
                .build()));

        tools.add(tool("helm_lint_chart",
            "Validate a Helm chart for syntax errors, missing required values, and best practices.\n" +
            "Run in GitLab CI pipeline (lint stage) before build/deploy.\n" +
            "Returns: chart_name, lint_passed, errors (list), warnings (list), suggestions (list).",
            schema()
                .string("chart_name",  "Chart name to lint")
                .string("chart_path",  "Local path to chart directory (optional, e.g. ./charts/helm-service)")
                .string("values_yaml", "Values YAML to lint against (max 64 KB)")
                .string("environment", "Environment context for linting: dev | test | stage | prod")
                .bool("strict",        "Treat warnings as errors (default: false)")
                .build()));

        tools.add(tool("helm_diff_upgrade",
            "Preview the changes that would be applied by a helm_upgrade, without executing.\n" +
            "Equivalent to helm-diff plugin: shows added/modified/removed Kubernetes resources.\n" +
            "Returns: release_name, namespace, added_resources, modified_resources, removed_resources, " +
            "config_changes (values diff).",
            schema()
                .required("release_name", "namespace")
                .string("release_name", "Release to diff against")
                .string("namespace",    "Kubernetes namespace")
                .string("chart_name",   "Chart for the proposed upgrade")
                .string("chart_version","Proposed chart version")
                .string("values_yaml",  "Proposed values overrides (max 64 KB)")
                .string("environment",  "Target environment")
                .build()));

        // ── Helm Session Orchestrator (GD Sessions) ───────────────────────

        tools.add(tool("session_create",
            "Create a new Group Discussion (GD) assessment session room.\n" +
            "Helm orchestrates the complete session lifecycle: deterministic speaking order, " +
            "enforced turn timing, mute/unmute commands, compliance logging.\n" +
            "Emits Kafka event: gd-sessions.created.\n" +
            "Returns: session_id, room_name, join_url, speaking_protocol, state=pending.",
            schema()
                .required("topic")
                .string("topic",                  "GD discussion topic (required)")
                .integer("max_participants",       "Maximum participants: 2–20 (default: 6)")
                .integer("intro_duration_seconds", "Intro round duration: 10–300s (default: 60)")
                .integer("rebuttal_duration_seconds","Rebuttal round: 10–120s (default: 30)")
                .integer("conclusion_duration_seconds","Conclusion round: 10–180s (default: 45)")
                .string("language",               "Session language: en | hi | regional (default: en)")
                .string("tenant_id",              "Tenant UUID for multi-tenant isolation")
                .string("notes",                  "Internal session notes (max 500 chars, optional)")
                .build()));

        tools.add(tool("session_join",
            "Register a participant joining a GD session.\n" +
            "Computes deterministic speaking position by join time, then candidate ID.\n" +
            "Requires explicit DPDP 2023 consent (consent_given=true).\n" +
            "Emits Kafka event: participant.joined.\n" +
            "Returns: session_id, participant_id, speaking_position, mic_state=muted, join_url.",
            schema()
                .required("session_id", "participant_id", "consent_given")
                .string("session_id",    "UUID of the session to join")
                .string("participant_id","UUID of the joining participant (candidate)")
                .bool("consent_given",   "REQUIRED: true — DPDP 2023 explicit participant consent")
                .string("candidate_name","Display name for IVR prompts (optional)")
                .build()));

        tools.add(tool("session_get_status",
            "Get the current state of a GD session.\n" +
            "Returns: session_id, state (pending|active|completed|archived), current_speaker_id, " +
            "speaking_queue (ordered list), current_round, time_remaining_seconds, " +
            "participant_count, joined_participants.",
            schema()
                .required("session_id")
                .string("session_id", "UUID of the session")
                .build()));

        tools.add(tool("session_advance_turn",
            "Mark the current speaker's turn as complete and advance to the next speaker.\n" +
            "Enforces deterministic order: mutes current speaker, unmutes next in queue.\n" +
            "Auto-advances on timeout (no manual call needed — Helm handles enforcement).\n" +
            "Emits Kafka events: speaking-turn.completed, speaking-turn.started.\n" +
            "Returns: previous_speaker_id, next_speaker_id, round, time_remaining_seconds.",
            schema()
                .required("session_id", "current_speaker_id")
                .string("session_id",        "UUID of the session")
                .string("current_speaker_id","UUID of the speaker completing their turn")
                .bool("force_advance",       "Force advance even if time remaining (admin only, default: false)")
                .build()));

        tools.add(tool("session_get_metrics",
            "Retrieve aggregated behavioral metrics after session completion.\n" +
            "Metrics are reproducible from the event log. Zero audio stored.\n" +
            "Per participant: speaking_duration_seconds, turns_completed, interrupt_attempts, " +
            "silence_events, network_disruptions, compliance_violations.\n" +
            "Emits Kafka event: session-metrics.published (consumed by Evaluation Engine).",
            schema()
                .required("session_id")
                .string("session_id",   "UUID of the completed session")
                .string("tenant_id",    "Tenant UUID for RLS-scoped query (optional)")
                .bool("include_events", "Include raw event log (default: false)")
                .build()));

        return tools;
    }

    // ── DSL helpers ───────────────────────────────────────────────────────────

    private ObjectNode tool(String name, String description, ObjectNode inputSchema) {
        ObjectNode t = mapper.createObjectNode();
        t.put("name",        name);
        t.put("description", description);
        t.set("inputSchema", inputSchema);
        return t;
    }

    private SchemaBuilder schema() { return new SchemaBuilder(mapper); }

    private static class SchemaBuilder {
        private final ObjectMapper mapper;
        private final ObjectNode   schema;
        private final ObjectNode   props;
        private final ArrayNode    required;

        SchemaBuilder(ObjectMapper m) {
            this.mapper   = m; this.schema = m.createObjectNode();
            this.props    = m.createObjectNode(); this.required = m.createArrayNode();
            schema.put("type", "object"); schema.set("properties", props);
        }
        SchemaBuilder required(String... f) { for (String s : f) required.add(s); return this; }
        SchemaBuilder string(String n, String d) {
            ObjectNode p = mapper.createObjectNode(); p.put("type","string"); p.put("description",d);
            props.set(n, p); return this; }
        SchemaBuilder integer(String n, String d) {
            ObjectNode p = mapper.createObjectNode(); p.put("type","integer"); p.put("description",d);
            props.set(n, p); return this; }
        SchemaBuilder bool(String n, String d) {
            ObjectNode p = mapper.createObjectNode(); p.put("type","boolean"); p.put("description",d);
            props.set(n, p); return this; }
        ObjectNode build() { if (required.size() > 0) schema.set("required", required); return schema; }
    }
}
