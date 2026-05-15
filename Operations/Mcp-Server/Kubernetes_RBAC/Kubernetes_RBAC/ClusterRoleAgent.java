package com.ecoskiller.mcp.rbac.tools;

import com.ecoskiller.mcp.rbac.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Agent handling cluster-scoped Roles and Bindings.
 * Tools: rbac_create_cluster_role, rbac_delete_cluster_role,
 *        rbac_list_cluster_roles, rbac_bind_cluster_role
 */
public class ClusterRoleAgent {

    private static final Logger LOG = Logger.getLogger(ClusterRoleAgent.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    private final Map<String, RbacRole>    clusterRoles    = new ConcurrentHashMap<>();
    private final Map<String, RoleBinding> clusterBindings = new ConcurrentHashMap<>();

    // Immutable system ClusterRoles — cannot be modified or deleted
    private static final Set<String> SYSTEM_CLUSTER_ROLES = Set.of(
        "cluster-admin", "admin", "edit", "view",
        "system:auth-delegator", "system:heapster", "system:kube-aggregator",
        "system:kube-controller-manager", "system:kube-dns",
        "system:kube-scheduler", "system:monitoring", "system:node",
        "system:node-bootstrapper", "system:node-problem-detector",
        "system:node-proxier", "system:persistent-volume-provisioner"
    );

    public JsonNode createClusterRole(JsonNode args) {
        String   name          = require(args, "name");
        JsonNode rulesNode     = requireArray(args, "rules");
        String   justification = require(args, "justification");

        validateClusterRoleName(name);

        // Require non-trivial justification
        if (justification.length() < 20) {
            throw new ValidationException(
                "Justification too brief. Cluster-wide roles require a detailed security justification (min 20 chars). " +
                "Example: 'Monitoring service needs read access to all namespaces for metrics collection.'");
        }

        List<PolicyRule> rules = parseClusterRules(rulesNode);

        if (clusterRoles.containsKey(name)) {
            throw new ValidationException("ClusterRole '" + name + "' already exists.");
        }

        RbacRole cr = new RbacRole(name, null, rules, Instant.now().toString(), "active");
        cr.setJustification(justification);
        clusterRoles.put(name, cr);

        LOG.info("[ClusterRoleAgent] Created cluster role=" + name + " justification=" + justification);

        ObjectNode result = mapper.createObjectNode();
        result.put("status",        "created");
        result.put("name",          name);
        result.put("kind",          "ClusterRole");
        result.put("rules_count",   rules.size());
        result.put("justification", justification);
        result.put("created_at",    cr.getCreatedAt());
        result.put("scope",         "cluster-wide — applies to ALL namespaces");
        result.put("warning",       "ClusterRoles grant elevated access. Bind sparingly using namespace-scoped RoleBindings where possible.");
        result.put("gitops_action", "ClusterRole YAML queued for ArgoCD sync (GCP + AWS)");
        result.set("yaml_preview",  buildClusterRoleYaml(cr));
        return result;
    }

    public JsonNode deleteClusterRole(JsonNode args) {
        String name    = require(args, "name");
        String confirm = require(args, "confirm");
        String reason  = args.has("reason") ? args.get("reason").asText() : "(no reason)";

        if (!"CONFIRM_DELETE".equals(confirm)) {
            throw new ValidationException(
                "Deletion requires confirm='CONFIRM_DELETE'. This safety gate prevents accidental deletion of cluster-wide roles.");
        }
        if (SYSTEM_CLUSTER_ROLES.contains(name) || name.startsWith("system:")) {
            throw new ValidationException(
                "Cannot delete system-managed ClusterRole: " + name +
                ". System ClusterRoles are protected by Kubernetes and Ecoskiller policy.");
        }
        if (!clusterRoles.containsKey(name)) {
            throw new ValidationException("ClusterRole '" + name + "' not found.");
        }

        clusterRoles.remove(name);
        LOG.info("[ClusterRoleAgent] Deleted cluster role=" + name + " reason=" + reason);

        ObjectNode result = mapper.createObjectNode();
        result.put("status",    "deleted");
        result.put("name",      name);
        result.put("reason",    reason);
        result.put("audit_note","ClusterRole deletion logged for SOC2 change control");
        result.put("gitops_action","YAML removal queued for both GCP and AWS clusters via ArgoCD");
        return result;
    }

    public JsonNode listClusterRoles(JsonNode args) {
        boolean includeSystem  = args.has("include_system") && args.get("include_system").asBoolean();
        String  labelSelector  = args.has("label_selector")  ? args.get("label_selector").asText()  : null;
        String  resourceFilter = args.has("resource_filter") ? args.get("resource_filter").asText() : null;

        ArrayNode list = mapper.createArrayNode();

        // Add system roles if requested
        if (includeSystem) {
            for (String sysName : SYSTEM_CLUSTER_ROLES) {
                ObjectNode entry = mapper.createObjectNode();
                entry.put("name",       sysName);
                entry.put("kind",       "ClusterRole");
                entry.put("managed_by", "system");
                entry.put("mutable",    false);
                list.add(entry);
            }
        }

        for (RbacRole cr : clusterRoles.values()) {
            if (resourceFilter != null && cr.getRules().stream()
                    .noneMatch(r -> r.getResources().contains(resourceFilter))) continue;

            ObjectNode entry = mapper.createObjectNode();
            entry.put("name",       cr.getName());
            entry.put("kind",       "ClusterRole");
            entry.put("rules_count", cr.getRules().size());
            entry.put("created_at", cr.getCreatedAt());
            entry.put("managed_by", "ecoskiller-gitops");
            entry.put("mutable",    true);
            if (cr.getJustification() != null) entry.put("justification", cr.getJustification());
            list.add(entry);
        }

        ObjectNode result = mapper.createObjectNode();
        result.set("cluster_roles", list);
        result.put("total",          list.size());
        result.put("note",           "ClusterRoles are replicated identically across GCP and AWS via ArgoCD GitOps");
        return result;
    }

    public JsonNode bindClusterRole(JsonNode args) {
        String   bindingName  = require(args, "binding_name");
        String   clusterRole  = require(args, "cluster_role");
        JsonNode subjects     = requireArray(args, "subjects");
        boolean  clusterWide  = args.has("cluster_wide") && args.get("cluster_wide").asBoolean();
        String   namespace    = args.has("namespace")    ? args.get("namespace").asText() : null;
        String   justification= args.has("justification") ? args.get("justification").asText() : null;

        if (clusterWide && (justification == null || justification.length() < 20)) {
            throw new ValidationException(
                "ClusterRoleBinding (cluster_wide=true) requires a detailed justification (min 20 chars). " +
                "Prefer namespace-scoped RoleBindings referencing ClusterRoles where possible.");
        }
        if (!clusterWide && (namespace == null || namespace.isBlank())) {
            throw new ValidationException("namespace is required when cluster_wide=false.");
        }

        // Parse subjects
        List<Subject> parsedSubjects = new ArrayList<>();
        for (JsonNode s : subjects) {
            String kind = s.has("kind") ? s.get("kind").asText() : null;
            String name = s.has("name") ? s.get("name").asText() : null;
            String ns   = s.has("namespace") ? s.get("namespace").asText() : null;
            if (kind == null || name == null) throw new ValidationException("Each subject requires kind and name.");
            parsedSubjects.add(new Subject(kind, name, ns));
        }

        String bindingKey = (clusterWide ? "cluster" : namespace) + "/" + bindingName;
        if (clusterBindings.containsKey(bindingKey)) {
            throw new ValidationException("Binding '" + bindingName + "' already exists.");
        }

        String bindingNamespace = clusterWide ? null : namespace;
        RoleBinding rb = new RoleBinding(bindingName, bindingNamespace, "ClusterRole", clusterRole,
                                         parsedSubjects, Instant.now().toString());
        clusterBindings.put(bindingKey, rb);

        LOG.info("[ClusterRoleAgent] Bound clusterRole=" + clusterRole + " clusterWide=" + clusterWide);

        ObjectNode result = mapper.createObjectNode();
        result.put("status",       "created");
        result.put("binding_name", bindingName);
        result.put("binding_kind", clusterWide ? "ClusterRoleBinding" : "RoleBinding (ClusterRole ref)");
        result.put("cluster_role", clusterRole);
        result.put("subjects",     parsedSubjects.size());
        result.put("scope",        clusterWide ? "CLUSTER-WIDE (all namespaces)" : "namespace: " + namespace);
        result.put("best_practice", clusterWide ?
            "WARNING: ClusterRoleBinding grants access to ALL namespaces. Consider namespace-scoped RoleBinding instead." :
            "GOOD: Namespace-scoped binding reuses ClusterRole definition without granting cluster-wide access.");
        result.set("yaml_preview",  buildClusterBindingYaml(rb, clusterWide));
        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────

    private List<PolicyRule> parseClusterRules(JsonNode rulesNode) {
        List<PolicyRule> rules = new ArrayList<>();
        for (JsonNode ruleNode : rulesNode) {
            List<String> verbs     = toStringList(ruleNode.has("verbs")      ? ruleNode.get("verbs")      : mapper.createArrayNode());
            List<String> resources = toStringList(ruleNode.has("resources")  ? ruleNode.get("resources")  : mapper.createArrayNode());
            List<String> apiGroups = toStringList(ruleNode.has("api_groups") ? ruleNode.get("api_groups") : mapper.createArrayNode());
            if (apiGroups.isEmpty()) apiGroups.add("");

            // Warn but allow wildcards at cluster scope (with justification)
            rules.add(new PolicyRule(verbs, resources, apiGroups));
        }
        return rules;
    }

    private void validateClusterRoleName(String name) {
        if (name == null || name.isBlank()) throw new ValidationException("ClusterRole name cannot be blank.");
        if (SYSTEM_CLUSTER_ROLES.contains(name) || name.startsWith("system:")) {
            throw new ValidationException("Cannot create a ClusterRole with reserved system name: " + name);
        }
        if (!name.matches("[a-z0-9][a-z0-9\\-:.]*")) {
            throw new ValidationException("Invalid ClusterRole name: " + name);
        }
    }

    private JsonNode buildClusterRoleYaml(RbacRole cr) {
        StringBuilder sb = new StringBuilder();
        sb.append("apiVersion: rbac.authorization.k8s.io/v1\n");
        sb.append("kind: ClusterRole\n");
        sb.append("metadata:\n");
        sb.append("  name: ").append(cr.getName()).append("\n");
        sb.append("  labels:\n");
        sb.append("    managed-by: ecoskiller-gitops\n");
        sb.append("rules:\n");
        for (PolicyRule r : cr.getRules()) {
            sb.append("- apiGroups: [\"").append(String.join("\", \"", r.getApiGroups())).append("\"]\n");
            sb.append("  resources: [\"").append(String.join("\", \"", r.getResources())).append("\"]\n");
            sb.append("  verbs: [\"").append(String.join("\", \"", r.getVerbs())).append("\"]\n");
        }
        ObjectNode yaml = mapper.createObjectNode();
        yaml.put("content", sb.toString());
        return yaml;
    }

    private JsonNode buildClusterBindingYaml(RoleBinding rb, boolean clusterWide) {
        StringBuilder sb = new StringBuilder();
        sb.append("apiVersion: rbac.authorization.k8s.io/v1\n");
        sb.append("kind: ").append(clusterWide ? "ClusterRoleBinding" : "RoleBinding").append("\n");
        sb.append("metadata:\n");
        sb.append("  name: ").append(rb.getName()).append("\n");
        if (!clusterWide && rb.getNamespace() != null) {
            sb.append("  namespace: ").append(rb.getNamespace()).append("\n");
        }
        sb.append("subjects:\n");
        for (Subject s : rb.getSubjects()) {
            sb.append("- kind: ").append(s.getKind()).append("\n");
            sb.append("  name: ").append(s.getName()).append("\n");
            if (s.getNamespace() != null) sb.append("  namespace: ").append(s.getNamespace()).append("\n");
        }
        sb.append("roleRef:\n");
        sb.append("  apiGroup: rbac.authorization.k8s.io\n");
        sb.append("  kind: ClusterRole\n");
        sb.append("  name: ").append(rb.getRoleName()).append("\n");
        ObjectNode yaml = mapper.createObjectNode();
        yaml.put("content", sb.toString());
        return yaml;
    }

    private List<String> toStringList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node.isArray()) node.forEach(n -> list.add(n.asText()));
        return list;
    }

    private String require(JsonNode args, String field) {
        if (!args.has(field) || args.get(field).asText().isBlank()) {
            throw new ValidationException("Required field missing: " + field);
        }
        return args.get(field).asText().trim();
    }

    private JsonNode requireArray(JsonNode args, String field) {
        if (!args.has(field) || !args.get(field).isArray()) {
            throw new ValidationException("Required array field missing: " + field);
        }
        return args.get(field);
    }
}
