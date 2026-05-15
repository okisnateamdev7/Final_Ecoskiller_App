package com.ecoskiller.mcp.rbac.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Central registry for all 18 RBAC tool agents.
 * Each tool is registered with:
 *  - name       (MCP tool identifier)
 *  - description
 *  - inputSchema (JSON Schema)
 *  - handler    (Java lambda)
 */
public class ToolRegistry {

    private static final Logger LOG = Logger.getLogger(ToolRegistry.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    private final Map<String, ToolDefinition>               defs     = new HashMap<>();
    private final Map<String, Function<JsonNode, JsonNode>> handlers = new HashMap<>();

    // ── Delegate tool-groups to dedicated agent classes ──────────────
    private final RoleAgent          roleAgent   = new RoleAgent();
    private final RoleBindingAgent   bindAgent   = new RoleBindingAgent();
    private final ClusterRoleAgent   clusterAgent = new ClusterRoleAgent();
    private final AccessCheckAgent   accessAgent = new AccessCheckAgent();
    private final AuditQueryAgent    auditAgent  = new AuditQueryAgent();
    private final PolicyAgent        policyAgent = new PolicyAgent();
    private final GitOpsAgent        gitOpsAgent = new GitOpsAgent();

    public void registerAll() {
        // ── 1. rbac_check_access ─────────────────────────────────────
        register("rbac_check_access",
            "Check whether a user/service account is authorized to perform a verb on a resource " +
            "in a given namespace. Returns allow/deny with reason. Implements Kubernetes RBAC " +
            "authorization decision logic (kubectl auth can-i equivalent).",
            schema()
                .req("user",      "string",  "User identity (e.g. alice@ecoskiller.com) or service account (system:serviceaccount:ns:sa-name)")
                .req("verb",      "string",  "Kubernetes verb: get | list | create | update | patch | delete | watch | exec | port-forward")
                .req("resource",  "string",  "Kubernetes resource: pods | deployments | services | secrets | configmaps | etc.")
                .req("namespace", "string",  "Target namespace (e.g. production, tenant-a-prod)")
                .opt("api_group", "string",  "API group (default: v1). Use 'apps' for deployments, 'batch' for jobs.")
                .opt("sub_resource","string","Sub-resource (e.g. 'log' for pods/log, 'exec' for pods/exec)"),
            accessAgent::checkAccess);

        // ── 2. rbac_create_role ──────────────────────────────────────
        register("rbac_create_role",
            "Create a new Kubernetes Role in a specific namespace with defined permission rules. " +
            "Enforces least-privilege: validates rules don't grant wildcard admin access unless " +
            "explicitly approved. Stores definition for GitOps sync.",
            schema()
                .req("name",      "string",  "Role name (e.g. pod-reader, deployment-manager, log-reader)")
                .req("namespace", "string",  "Namespace scope for this role")
                .req("rules",     "array",   "Array of PolicyRule objects: [{verbs:[...], resources:[...], api_groups:[...]}]")
                .opt("labels",    "object",  "Kubernetes labels as key-value pairs")
                .opt("annotations","object", "Kubernetes annotations"),
            roleAgent::createRole);

        // ── 3. rbac_update_role ──────────────────────────────────────
        register("rbac_update_role",
            "Update an existing Role's permission rules in a namespace. All changes are validated " +
            "for privilege escalation before applying. Audit logged with before/after diff.",
            schema()
                .req("name",      "string", "Existing role name")
                .req("namespace", "string", "Namespace of the role")
                .req("rules",     "array",  "New complete set of PolicyRule objects")
                .opt("reason",    "string", "Change reason for audit log (recommended)"),
            roleAgent::updateRole);

        // ── 4. rbac_delete_role ──────────────────────────────────────
        register("rbac_delete_role",
            "Delete a Role from a namespace. Checks for active RoleBindings referencing this role " +
            "before deletion. Fails safe: refuses deletion if active bindings exist unless force=true.",
            schema()
                .req("name",      "string",  "Role name to delete")
                .req("namespace", "string",  "Namespace of the role")
                .opt("force",     "boolean", "Force delete even if active bindings exist (default: false)")
                .opt("reason",    "string",  "Deletion reason for audit log"),
            roleAgent::deleteRole);

        // ── 5. rbac_get_role ─────────────────────────────────────────
        register("rbac_get_role",
            "Retrieve full definition of a Role including all permission rules, labels, " +
            "annotations, creation timestamp, and currently bound subjects.",
            schema()
                .req("name",       "string",  "Role name")
                .req("namespace",  "string",  "Namespace")
                .opt("include_bindings", "boolean", "Also return subjects bound to this role (default: true)"),
            roleAgent::getRole);

        // ── 6. rbac_list_roles ───────────────────────────────────────
        register("rbac_list_roles",
            "List all Roles in a namespace or across all namespaces. Supports filtering by " +
            "label selector, verb, or resource. Used for RBAC audits and permission reviews.",
            schema()
                .opt("namespace",      "string", "Filter by namespace (omit for all namespaces)")
                .opt("label_selector", "string", "Label selector (e.g. team=frontend)")
                .opt("verb_filter",    "string", "Only return roles granting this verb")
                .opt("resource_filter","string", "Only return roles for this resource type"),
            roleAgent::listRoles);

        // ── 7. rbac_create_role_binding ──────────────────────────────
        register("rbac_create_role_binding",
            "Bind a Role to one or more subjects (Users, Groups, ServiceAccounts) in a namespace. " +
            "Validates cross-tenant access: prevents Tenant A subjects from being bound in Tenant B " +
            "namespaces. Enforces namespace isolation per Ecoskiller multi-tenancy policy.",
            schema()
                .req("name",       "string", "RoleBinding name")
                .req("namespace",  "string", "Namespace where binding applies")
                .req("role_name",  "string", "Role to bind (must exist in same namespace or be a ClusterRole)")
                .req("subjects",   "array",  "Subjects: [{kind: User|Group|ServiceAccount, name: '...', namespace: '...'}]")
                .opt("role_kind",  "string", "Role | ClusterRole (default: Role)"),
            bindAgent::createRoleBinding);

        // ── 8. rbac_delete_role_binding ──────────────────────────────
        register("rbac_delete_role_binding",
            "Remove a RoleBinding, revoking the bound role from all subjects in the namespace. " +
            "Audit logged with full subject list for compliance. Cannot delete system-critical bindings.",
            schema()
                .req("name",      "string", "RoleBinding name to delete")
                .req("namespace", "string", "Namespace of the binding")
                .opt("reason",    "string", "Revocation reason for SOC2/GDPR audit trail"),
            bindAgent::deleteRoleBinding);

        // ── 9. rbac_list_role_bindings ───────────────────────────────
        register("rbac_list_role_bindings",
            "List all RoleBindings in a namespace or cluster-wide. Filter by subject to find all " +
            "permissions granted to a specific user or service account. Used for quarterly access reviews.",
            schema()
                .opt("namespace",       "string",  "Namespace filter (omit for all namespaces)")
                .opt("subject_name",    "string",  "Filter bindings for a specific user/SA/group")
                .opt("subject_kind",    "string",  "User | Group | ServiceAccount")
                .opt("role_filter",     "string",  "Only bindings referencing this role name")
                .opt("include_cluster_bindings", "boolean", "Also include ClusterRoleBindings (default: false)"),
            bindAgent::listRoleBindings);

        // ── 10. rbac_create_cluster_role ─────────────────────────────
        register("rbac_create_cluster_role",
            "Create a ClusterRole with cluster-wide permissions. These are elevated and apply across " +
            "all namespaces. Requires explicit security justification. Validates against Ecoskiller " +
            "privilege escalation rules — cannot create roles exceeding creator's own permissions.",
            schema()
                .req("name",       "string", "ClusterRole name (e.g. cluster-readonly, node-manager)")
                .req("rules",      "array",  "PolicyRule array with verbs, resources, api_groups, resource_names")
                .req("justification","string","Security justification required for cluster-scoped roles")
                .opt("labels",     "object", "Labels for GitOps tracking")
                .opt("aggregation_rule","object","AggregationRule to combine other ClusterRoles"),
            clusterAgent::createClusterRole);

        // ── 11. rbac_delete_cluster_role ─────────────────────────────
        register("rbac_delete_cluster_role",
            "Delete a ClusterRole. Refuses deletion of system ClusterRoles (prefixed 'system:'). " +
            "Checks active ClusterRoleBindings. Requires explicit confirmation string for safety.",
            schema()
                .req("name",       "string", "ClusterRole name")
                .req("confirm",    "string", "Must be 'CONFIRM_DELETE' to proceed")
                .opt("reason",     "string", "Deletion justification for audit"),
            clusterAgent::deleteClusterRole);

        // ── 12. rbac_list_cluster_roles ──────────────────────────────
        register("rbac_list_cluster_roles",
            "List all ClusterRoles, including system-managed ones. Shows permission summary, " +
            "bound subjects count, and last modification. Useful for privilege escalation reviews.",
            schema()
                .opt("include_system",  "boolean", "Include system: prefixed roles (default: false)")
                .opt("label_selector",  "string",  "Label selector filter")
                .opt("resource_filter", "string",  "Only roles granting access to this resource"),
            clusterAgent::listClusterRoles);

        // ── 13. rbac_bind_cluster_role ───────────────────────────────
        register("rbac_bind_cluster_role",
            "Create a ClusterRoleBinding granting cluster-wide access, or a namespaced RoleBinding " +
            "referencing a ClusterRole. The latter is the preferred pattern for scoped reuse of " +
            "shared ClusterRole definitions per Ecoskiller RBAC design principles.",
            schema()
                .req("binding_name",    "string",  "Name for the ClusterRoleBinding")
                .req("cluster_role",    "string",  "ClusterRole name to bind")
                .req("subjects",        "array",   "Subjects: [{kind, name, namespace}]")
                .req("cluster_wide",    "boolean", "true = ClusterRoleBinding; false = namespace-scoped RoleBinding referencing ClusterRole")
                .opt("namespace",       "string",  "Required when cluster_wide=false")
                .opt("justification",   "string",  "Required for cluster_wide=true"),
            clusterAgent::bindClusterRole);

        // ── 14. rbac_list_service_accounts ───────────────────────────
        register("rbac_list_service_accounts",
            "List all ServiceAccounts in a namespace with their bound roles and permissions summary. " +
            "Identifies over-privileged service accounts — a key Kubernetes security hardening check. " +
            "Flags SAs with wildcard permissions or cluster-admin binding.",
            schema()
                .req("namespace",         "string",  "Namespace to inspect")
                .opt("flag_overprivileged","boolean", "Highlight SAs with excessive permissions (default: true)")
                .opt("include_default",   "boolean", "Include 'default' SA (default: true)"),
            accessAgent::listServiceAccounts);

        // ── 15. rbac_get_user_permissions ────────────────────────────
        register("rbac_get_user_permissions",
            "Get the complete effective permission set for a user or service account across all " +
            "namespaces. Aggregates all RoleBindings + ClusterRoleBindings. Equivalent to " +
            "'kubectl auth can-i --list --as=USER'. Used for quarterly access reviews and " +
            "SOC2/HIPAA/GDPR compliance reporting.",
            schema()
                .req("user",             "string",  "User email or service account (system:serviceaccount:ns:name)")
                .opt("namespace",        "string",  "Scope to specific namespace (omit for cluster-wide summary)")
                .opt("format",           "string",  "summary | detailed | compliance_report (default: summary)")
                .opt("include_groups",   "boolean", "Include group-inherited permissions (default: true)"),
            accessAgent::getUserPermissions);

        // ── 16. rbac_audit_log_query ─────────────────────────────────
        register("rbac_audit_log_query",
            "Query Kubernetes RBAC audit logs for authorization events. Supports forensic analysis: " +
            "'show all denied API calls in prod', 'who accessed secrets in last 24h', 'show all " +
            "role modifications'. Essential for SOC2, HIPAA, GDPR incident response and compliance audits.",
            schema()
                .opt("user_filter",      "string",  "Filter by specific user/SA")
                .opt("verb_filter",      "string",  "Filter by action verb (get|create|delete|...)")
                .opt("resource_filter",  "string",  "Filter by resource type")
                .opt("namespace_filter", "string",  "Filter by namespace")
                .opt("result_filter",    "string",  "allow | deny | all (default: all)")
                .opt("start_time",       "string",  "ISO 8601 start timestamp (e.g. 2025-03-01T00:00:00Z)")
                .opt("end_time",         "string",  "ISO 8601 end timestamp")
                .opt("limit",            "integer", "Max results (default: 100, max: 1000)")
                .opt("include_reasons",  "boolean", "Include denial reasons (default: true)"),
            auditAgent::queryAuditLog);

        // ── 17. rbac_validate_policy ─────────────────────────────────
        register("rbac_validate_policy",
            "Validate RBAC YAML policy before applying to cluster. Checks: syntax correctness, " +
            "privilege escalation detection, cross-tenant access risks, naming convention compliance, " +
            "and wildcard permission warnings. CI/CD pre-commit gate — runs in GitLab CI pipeline.",
            schema()
                .req("yaml_content",     "string",  "Raw YAML content of Role/ClusterRole/RoleBinding/ClusterRoleBinding")
                .opt("strict_mode",      "boolean", "Fail on warnings, not just errors (default: false)")
                .opt("tenant_context",   "string",  "Tenant ID for cross-tenant access validation")
                .opt("environment",      "string",  "dev | staging | prod — prod applies strictest rules"),
            policyAgent::validatePolicy);

        // ── 18. rbac_sync_gitops_status ──────────────────────────────
        register("rbac_sync_gitops_status",
            "Check GitOps sync status of RBAC resources across GCP and AWS clusters. Detects drift " +
            "between Git (source of truth) and live cluster state. Returns per-resource sync status, " +
            "last sync timestamp, and any divergence details. Integrates with ArgoCD sync state.",
            schema()
                .opt("cluster",          "string",  "gcp | aws | both (default: both)")
                .opt("namespace",        "string",  "Filter by namespace")
                .opt("resource_type",    "string",  "Role | RoleBinding | ClusterRole | ClusterRoleBinding | all")
                .opt("only_drifted",     "boolean", "Only return out-of-sync resources (default: false)"),
            gitOpsAgent::syncStatus);

        LOG.info("[ToolRegistry] Registered " + defs.size() + " RBAC tools.");
    }

    // ─────────────────────────────────────────────────────────────────
    //  Dispatch
    // ─────────────────────────────────────────────────────────────────

    public JsonNode call(String name, JsonNode args) throws ToolNotFoundException {
        Function<JsonNode, JsonNode> handler = handlers.get(name);
        if (handler == null) throw new ToolNotFoundException(name);
        return handler.apply(args);
    }

    public ArrayNode getToolDefinitions() throws Exception {
        ArrayNode arr = mapper.createArrayNode();
        for (ToolDefinition td : defs.values()) arr.add(td.toJson(mapper));
        return arr;
    }

    // ─────────────────────────────────────────────────────────────────
    //  Registration helpers
    // ─────────────────────────────────────────────────────────────────

    private void register(String name, String description, SchemaBuilder schema,
                          Function<JsonNode, JsonNode> handler) {
        defs.put(name, new ToolDefinition(name, description, schema.build()));
        handlers.put(name, handler);
    }

    private SchemaBuilder schema() { return new SchemaBuilder(mapper); }
}
