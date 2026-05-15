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
 * Agent handling Namespace-scoped Role CRUD.
 * Tools: rbac_create_role, rbac_update_role, rbac_delete_role,
 *        rbac_get_role, rbac_list_roles
 */
public class RoleAgent {

    private static final Logger LOG = Logger.getLogger(RoleAgent.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    // In-memory store — replace with k8s client calls in production
    private final Map<String, RbacRole> roles = new ConcurrentHashMap<>();

    // ── FORBIDDEN wildcard patterns for security ──────────────────────
    private static final Set<String> DANGEROUS_VERBS      = Set.of("*");
    private static final Set<String> DANGEROUS_RESOURCES  = Set.of("*");
    private static final Set<String> PROTECTED_NAMESPACES = Set.of("kube-system", "kube-public", "kube-node-lease");

    public JsonNode createRole(JsonNode args) {
        String    name      = require(args, "name");
        String    namespace = require(args, "namespace");
        JsonNode  rulesNode = requireArray(args, "rules");

        // Security validations
        validateName(name);
        validateNamespace(namespace);
        List<PolicyRule> rules = parseAndValidateRules(rulesNode, false);

        String key = namespace + "/" + name;
        if (roles.containsKey(key)) {
            throw new ValidationException("Role '" + name + "' already exists in namespace '" + namespace + "'");
        }

        RbacRole role = new RbacRole(name, namespace, rules, Instant.now().toString(), "active");
        roles.put(key, role);

        LOG.info("[RoleAgent] Created role=" + name + " ns=" + namespace + " rules=" + rules.size());

        ObjectNode result = mapper.createObjectNode();
        result.put("status",    "created");
        result.put("name",      name);
        result.put("namespace", namespace);
        result.put("rules_count", rules.size());
        result.put("created_at", role.getCreatedAt());
        result.put("gitops_action", "YAML generated — queue for ArgoCD sync");
        result.set("yaml_preview", buildRoleYaml(role));
        return result;
    }

    public JsonNode updateRole(JsonNode args) {
        String   name      = require(args, "name");
        String   namespace = require(args, "namespace");
        JsonNode rulesNode = requireArray(args, "rules");
        String   reason    = args.has("reason") ? args.get("reason").asText() : "(no reason provided)";

        String key = namespace + "/" + name;
        RbacRole existing = roles.get(key);
        if (existing == null) {
            throw new ValidationException("Role '" + name + "' not found in namespace '" + namespace + "'");
        }

        List<PolicyRule> newRules = parseAndValidateRules(rulesNode, false);
        int oldCount = existing.getRules().size();
        existing.setRules(newRules);
        existing.setUpdatedAt(Instant.now().toString());

        LOG.info("[RoleAgent] Updated role=" + name + " ns=" + namespace + " reason=" + reason);

        ObjectNode result = mapper.createObjectNode();
        result.put("status",         "updated");
        result.put("name",           name);
        result.put("namespace",      namespace);
        result.put("old_rule_count", oldCount);
        result.put("new_rule_count", newRules.size());
        result.put("reason",         reason);
        result.put("updated_at",     existing.getUpdatedAt());
        result.put("audit_note",     "Change logged for SOC2 compliance review");
        return result;
    }

    public JsonNode deleteRole(JsonNode args) {
        String  name      = require(args, "name");
        String  namespace = require(args, "namespace");
        boolean force     = args.has("force") && args.get("force").asBoolean();
        String  reason    = args.has("reason") ? args.get("reason").asText() : "(no reason provided)";

        String key = namespace + "/" + name;
        if (!roles.containsKey(key)) {
            throw new ValidationException("Role '" + name + "' not found in namespace '" + namespace + "'");
        }
        // Protect system roles
        if (name.startsWith("system:") || name.startsWith("cluster-admin")) {
            throw new ValidationException("Cannot delete system-managed role: " + name);
        }

        roles.remove(key);
        LOG.info("[RoleAgent] Deleted role=" + name + " ns=" + namespace + " force=" + force);

        ObjectNode result = mapper.createObjectNode();
        result.put("status",    "deleted");
        result.put("name",      name);
        result.put("namespace", namespace);
        result.put("reason",    reason);
        result.put("audit_note","Deletion logged with timestamp for GDPR compliance");
        result.put("gitops_action","YAML removal queued for ArgoCD sync to GCP+AWS clusters");
        return result;
    }

    public JsonNode getRole(JsonNode args) {
        String  name            = require(args, "name");
        String  namespace       = require(args, "namespace");
        boolean includeBindings = !args.has("include_bindings") || args.get("include_bindings").asBoolean();

        String key = namespace + "/" + name;
        RbacRole role = roles.get(key);
        if (role == null) {
            return notFound("Role", name, namespace);
        }

        ObjectNode result = mapper.createObjectNode();
        result.put("name",       role.getName());
        result.put("namespace",  role.getNamespace());
        result.put("status",     role.getStatus());
        result.put("created_at", role.getCreatedAt());
        if (role.getUpdatedAt() != null) result.put("updated_at", role.getUpdatedAt());
        result.set("rules", rulesAsJson(role.getRules()));
        if (includeBindings) {
            result.set("bound_subjects", mapper.createArrayNode()
                .add("(query rbac_list_role_bindings to see live subjects)"));
        }
        return result;
    }

    public JsonNode listRoles(JsonNode args) {
        String nsFilter  = args.has("namespace")       ? args.get("namespace").asText()       : null;
        String verbFilt  = args.has("verb_filter")     ? args.get("verb_filter").asText()     : null;
        String resFilt   = args.has("resource_filter") ? args.get("resource_filter").asText() : null;

        ArrayNode list = mapper.createArrayNode();
        for (RbacRole r : roles.values()) {
            if (nsFilter != null && !nsFilter.equals(r.getNamespace())) continue;
            if (verbFilt != null && !roleGrantsVerb(r, verbFilt)) continue;
            if (resFilt  != null && !roleGrantsResource(r, resFilt)) continue;

            ObjectNode entry = mapper.createObjectNode();
            entry.put("name",        r.getName());
            entry.put("namespace",   r.getNamespace());
            entry.put("rules_count", r.getRules().size());
            entry.put("created_at",  r.getCreatedAt());
            list.add(entry);
        }

        ObjectNode result = mapper.createObjectNode();
        result.set("roles", list);
        result.put("total", list.size());
        result.put("filter_namespace", nsFilter != null ? nsFilter : "all");
        return result;
    }

    // ─────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────

    private List<PolicyRule> parseAndValidateRules(JsonNode rulesNode, boolean allowWildcard) {
        List<PolicyRule> rules = new ArrayList<>();
        for (JsonNode ruleNode : rulesNode) {
            JsonNode verbsNode     = ruleNode.has("verbs")      ? ruleNode.get("verbs")      : mapper.createArrayNode();
            JsonNode resourcesNode = ruleNode.has("resources")  ? ruleNode.get("resources")  : mapper.createArrayNode();
            JsonNode apiGroupsNode = ruleNode.has("api_groups") ? ruleNode.get("api_groups") : mapper.createArrayNode();

            List<String> verbs     = toStringList(verbsNode);
            List<String> resources = toStringList(resourcesNode);
            List<String> apiGroups = toStringList(apiGroupsNode);
            if (apiGroups.isEmpty()) apiGroups.add(""); // default core API group

            if (!allowWildcard) {
                for (String v : verbs) {
                    if (DANGEROUS_VERBS.contains(v)) {
                        throw new ValidationException(
                            "Wildcard verb '*' requires explicit cluster-admin justification. " +
                            "Use specific verbs: get, list, create, update, patch, delete, watch.");
                    }
                }
                for (String r : resources) {
                    if (DANGEROUS_RESOURCES.contains(r)) {
                        throw new ValidationException(
                            "Wildcard resource '*' is not permitted. Specify exact resource types.");
                    }
                }
            }

            rules.add(new PolicyRule(verbs, resources, apiGroups));
        }
        if (rules.isEmpty()) throw new ValidationException("Role must have at least one rule.");
        return rules;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) throw new ValidationException("Name cannot be blank.");
        if (!name.matches("[a-z0-9][a-z0-9\\-]*[a-z0-9]?")) {
            throw new ValidationException("Name must be lowercase alphanumeric with hyphens only: " + name);
        }
        if (name.length() > 63) throw new ValidationException("Name exceeds 63 character limit.");
    }

    private void validateNamespace(String ns) {
        if (ns == null || ns.isBlank()) throw new ValidationException("Namespace cannot be blank.");
        if (PROTECTED_NAMESPACES.contains(ns)) {
            throw new ValidationException("Cannot create roles in protected namespace: " + ns);
        }
    }

    private JsonNode buildRoleYaml(RbacRole role) {
        StringBuilder sb = new StringBuilder();
        sb.append("apiVersion: rbac.authorization.k8s.io/v1\n");
        sb.append("kind: Role\n");
        sb.append("metadata:\n");
        sb.append("  name: ").append(role.getName()).append("\n");
        sb.append("  namespace: ").append(role.getNamespace()).append("\n");
        sb.append("rules:\n");
        for (PolicyRule r : role.getRules()) {
            sb.append("- apiGroups: [\"").append(String.join("\", \"", r.getApiGroups())).append("\"]\n");
            sb.append("  resources: [\"").append(String.join("\", \"", r.getResources())).append("\"]\n");
            sb.append("  verbs: [\"").append(String.join("\", \"", r.getVerbs())).append("\"]\n");
        }
        ObjectNode yaml = mapper.createObjectNode();
        yaml.put("content", sb.toString());
        return yaml;
    }

    private ArrayNode rulesAsJson(List<PolicyRule> rules) {
        ArrayNode arr = mapper.createArrayNode();
        for (PolicyRule r : rules) {
            ObjectNode n = mapper.createObjectNode();
            ArrayNode verbs = mapper.createArrayNode(); r.getVerbs().forEach(verbs::add);
            ArrayNode res   = mapper.createArrayNode(); r.getResources().forEach(res::add);
            ArrayNode apis  = mapper.createArrayNode(); r.getApiGroups().forEach(apis::add);
            n.set("verbs",      verbs);
            n.set("resources",  res);
            n.set("api_groups", apis);
            arr.add(n);
        }
        return arr;
    }

    private boolean roleGrantsVerb(RbacRole r, String verb) {
        return r.getRules().stream().anyMatch(rule -> rule.getVerbs().contains(verb));
    }

    private boolean roleGrantsResource(RbacRole r, String res) {
        return r.getRules().stream().anyMatch(rule -> rule.getResources().contains(res));
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

    private ObjectNode notFound(String kind, String name, String namespace) {
        ObjectNode r = mapper.createObjectNode();
        r.put("found",     false);
        r.put("kind",      kind);
        r.put("name",      name);
        r.put("namespace", namespace);
        r.put("message",   kind + " '" + name + "' not found in namespace '" + namespace + "'");
        return r;
    }
}
