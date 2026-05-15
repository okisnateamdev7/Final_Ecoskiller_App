package com.ecoskiller.mcp.rbac.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

// ═══════════════════════════════════════════════════════════════════════════════
// AuditQueryAgent — Tool 16: rbac_audit_log_query
// ═══════════════════════════════════════════════════════════════════════════════
class AuditQueryAgent {

    private static final Logger LOG = Logger.getLogger(AuditQueryAgent.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    // Simulated in-memory audit log — replace with ELK/Datadog query in production
    private static final List<Map<String, String>> SAMPLE_LOG = buildSampleLog();

    public JsonNode queryAuditLog(JsonNode args) {
        String  userFilter  = args.has("user_filter")      ? args.get("user_filter").asText()      : null;
        String  verbFilter  = args.has("verb_filter")      ? args.get("verb_filter").asText()      : null;
        String  resFilter   = args.has("resource_filter")  ? args.get("resource_filter").asText()  : null;
        String  nsFilter    = args.has("namespace_filter") ? args.get("namespace_filter").asText() : null;
        String  resultFilt  = args.has("result_filter")    ? args.get("result_filter").asText()    : "all";
        int     limit       = args.has("limit")            ? Math.min(args.get("limit").asInt(), 1000) : 100;
        boolean inclReasons = !args.has("include_reasons") || args.get("include_reasons").asBoolean();

        LOG.info("[AuditQueryAgent] Query: user=" + userFilter + " verb=" + verbFilter + " result=" + resultFilt);

        ArrayNode entries = mapper.createArrayNode();
        int count = 0;
        for (Map<String, String> entry : SAMPLE_LOG) {
            if (count >= limit) break;
            if (userFilter != null && !entry.get("user").contains(userFilter)) continue;
            if (verbFilter != null && !verbFilter.equals(entry.get("verb"))) continue;
            if (resFilter  != null && !resFilter.equals(entry.get("resource"))) continue;
            if (nsFilter   != null && !nsFilter.equals(entry.get("namespace"))) continue;
            if (!"all".equals(resultFilt) && !resultFilt.equalsIgnoreCase(entry.get("result"))) continue;

            ObjectNode e = mapper.createObjectNode();
            e.put("timestamp", entry.get("timestamp"));
            e.put("user",      entry.get("user"));
            e.put("verb",      entry.get("verb"));
            e.put("resource",  entry.get("resource"));
            e.put("namespace", entry.get("namespace"));
            e.put("result",    entry.get("result"));
            if (inclReasons && "deny".equals(entry.get("result"))) {
                e.put("denial_reason", entry.getOrDefault("reason", "No matching RoleBinding"));
            }
            entries.add(e);
            count++;
        }

        // Aggregation stats
        ObjectNode stats = mapper.createObjectNode();
        long allows = 0, denies = 0;
        for (JsonNode e : entries) {
            if ("allow".equals(e.get("result").asText())) allows++;
            else denies++;
        }
        stats.put("total_returned", count);
        stats.put("allowed",        allows);
        stats.put("denied",         denies);
        stats.put("denial_rate_pct", count > 0 ? Math.round((double) denies / count * 100) : 0);

        ObjectNode result = mapper.createObjectNode();
        result.set("entries", entries);
        result.set("stats",   stats);
        result.put("query_time",   Instant.now().toString());
        result.put("data_source",  "Ecoskiller audit sink (ELK Stack / Datadog — simulated in this response)");
        result.put("retention",    "Audit logs retained 90 days (SOC2 requirement)");
        result.put("elk_query",    buildElkQuery(userFilter, verbFilter, resFilter, nsFilter, resultFilt));
        return result;
    }

    private String buildElkQuery(String user, String verb, String res, String ns, String result) {
        List<String> clauses = new ArrayList<>();
        if (user  != null) clauses.add("user:\"" + user + "\"");
        if (verb  != null) clauses.add("verb:\"" + verb + "\"");
        if (res   != null) clauses.add("resource:\"" + res + "\"");
        if (ns    != null) clauses.add("namespace:\"" + ns + "\"");
        if (!"all".equals(result)) clauses.add("result:\"" + result + "\"");
        return "GET /kubernetes-audit-*/_search { query: { match: { " + String.join(" AND ", clauses) + " } } }";
    }

    private static List<Map<String, String>> buildSampleLog() {
        List<Map<String, String>> log = new ArrayList<>();
        String[] users = {"alice@ecoskiller.com", "bob@ecoskiller.com",
                          "system:serviceaccount:production:ecoskiller-api",
                          "system:serviceaccount:production:cert-manager"};
        String[] verbs = {"get", "list", "create", "update", "delete", "watch"};
        String[] resources = {"pods", "deployments", "secrets", "services", "configmaps"};
        String[] namespaces = {"production", "staging", "tenant-a-prod", "tenant-b-prod"};
        String[] results = {"allow", "allow", "allow", "deny"};

        Random rng = new Random(42L);
        for (int i = 0; i < 200; i++) {
            String r = results[rng.nextInt(results.length)];
            Map<String, String> e = new HashMap<>();
            e.put("timestamp", "2025-03-0" + (1 + i % 9) + "T" + String.format("%02d", rng.nextInt(24)) + ":00:00Z");
            e.put("user",      users[rng.nextInt(users.length)]);
            e.put("verb",      verbs[rng.nextInt(verbs.length)]);
            e.put("resource",  resources[rng.nextInt(resources.length)]);
            e.put("namespace", namespaces[rng.nextInt(namespaces.length)]);
            e.put("result",    r);
            if ("deny".equals(r)) e.put("reason", "No matching RoleBinding for requested action");
            log.add(e);
        }
        return Collections.unmodifiableList(log);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// PolicyAgent — Tool 17: rbac_validate_policy
// ═══════════════════════════════════════════════════════════════════════════════
class PolicyAgent {

    private static final Logger LOG = Logger.getLogger(PolicyAgent.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode validatePolicy(JsonNode args) {
        String  yaml        = require(args, "yaml_content");
        boolean strictMode  = args.has("strict_mode")    && args.get("strict_mode").asBoolean();
        String  tenant      = args.has("tenant_context") ? args.get("tenant_context").asText() : null;
        String  environment = args.has("environment")    ? args.get("environment").asText()    : "dev";

        LOG.info("[PolicyAgent] Validating RBAC YAML env=" + environment + " strict=" + strictMode);

        List<String> errors   = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<String> passed   = new ArrayList<>();

        // ── Check 1: required fields ───────────────────────────────
        if (!yaml.contains("apiVersion:")) errors.add("Missing 'apiVersion' field.");
        else passed.add("apiVersion present");
        if (!yaml.contains("kind:")) errors.add("Missing 'kind' field.");
        else passed.add("kind present");
        if (!yaml.contains("metadata:")) errors.add("Missing 'metadata' field.");
        else passed.add("metadata present");

        // ── Check 2: API version ───────────────────────────────────
        if (yaml.contains("apiVersion: v1")) {
            errors.add("RBAC resources must use 'apiVersion: rbac.authorization.k8s.io/v1', not 'v1'.");
        } else if (yaml.contains("rbac.authorization.k8s.io/v1")) {
            passed.add("Correct RBAC apiVersion");
        }

        // ── Check 3: wildcard privilege escalation ─────────────────
        if (yaml.contains("\"*\"") || yaml.contains("'*'") || yaml.contains("- '*'") || yaml.contains("- \"*\"")) {
            if ("prod".equals(environment)) {
                errors.add("PRIVILEGE ESCALATION: Wildcard permissions ('*') are forbidden in production. Specify exact verbs and resources.");
            } else {
                warnings.add("Wildcard permissions detected. Acceptable in " + environment + " but must be restricted before promoting to prod.");
            }
        } else {
            passed.add("No wildcard permissions detected");
        }

        // ── Check 4: naming convention ────────────────────────────
        if (yaml.matches("(?s).*name:\\s+[A-Z].*")) {
            errors.add("Name contains uppercase characters. Kubernetes names must be lowercase.");
        } else {
            passed.add("Name follows lowercase convention");
        }

        // ── Check 5: secrets access without justification ─────────
        if (yaml.contains("secrets") && yaml.contains("create")) {
            warnings.add("Role grants 'create' on 'secrets'. Ensure this is intentional — secrets access should be minimal.");
        }

        // ── Check 6: cross-tenant check ───────────────────────────
        if (tenant != null && yaml.contains("namespace:")) {
            String nsLine = Arrays.stream(yaml.split("\n"))
                .filter(l -> l.trim().startsWith("namespace:"))
                .findFirst().orElse("");
            String nsVal = nsLine.replace("namespace:", "").trim();
            if (!nsVal.isBlank() && !nsVal.startsWith(tenant)) {
                warnings.add("Namespace '" + nsVal + "' may not belong to tenant '" + tenant +
                             "'. Cross-tenant access requires explicit platform-eng approval.");
            }
        }

        // ── Check 7: exec/port-forward in prod ────────────────────
        if ("prod".equals(environment) && (yaml.contains("exec") || yaml.contains("port-forward"))) {
            warnings.add("'exec' and 'port-forward' permissions are high-risk in production. Consider removing.");
        }

        // ── Check 8: cluster-admin binding ───────────────────────
        if (yaml.contains("cluster-admin") && yaml.contains("ClusterRoleBinding")) {
            errors.add("Binding to 'cluster-admin' is forbidden. Use a scoped ClusterRole instead.");
        }

        boolean hasErrors   = !errors.isEmpty();
        boolean hasWarnings = !warnings.isEmpty();
        boolean valid       = !hasErrors && !(strictMode && hasWarnings);

        ObjectNode result = mapper.createObjectNode();
        result.put("valid",       valid);
        result.put("environment", environment);

        ArrayNode errArr  = mapper.createArrayNode(); errors.forEach(errArr::add);
        ArrayNode warnArr = mapper.createArrayNode(); warnings.forEach(warnArr::add);
        ArrayNode passArr = mapper.createArrayNode(); passed.forEach(passArr::add);
        result.set("errors",   errArr);
        result.set("warnings", warnArr);
        result.set("passed",   passArr);
        result.put("error_count",   errors.size());
        result.put("warning_count", warnings.size());
        result.put("ci_gate_result", valid ? "PASS — safe to apply" : "FAIL — fix errors before merge");
        result.put("strict_mode",    strictMode);
        result.put("note",    "This validation runs as GitLab CI pre-commit gate via ArgoCD drift prevention.");
        return result;
    }

    private String require(JsonNode args, String field) {
        if (!args.has(field) || args.get(field).asText().isBlank()) {
            throw new ValidationException("Required field missing: " + field);
        }
        return args.get(field).asText();
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// GitOpsAgent — Tool 18: rbac_sync_gitops_status
// ═══════════════════════════════════════════════════════════════════════════════
class GitOpsAgent {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOG   = Logger.getLogger(GitOpsAgent.class.getName());

    public JsonNode syncStatus(JsonNode args) {
        String  cluster       = args.has("cluster")        ? args.get("cluster").asText()        : "both";
        String  namespace     = args.has("namespace")      ? args.get("namespace").asText()      : null;
        String  resourceType  = args.has("resource_type")  ? args.get("resource_type").asText()  : "all";
        boolean onlyDrifted   = args.has("only_drifted")   && args.get("only_drifted").asBoolean();

        LOG.info("[GitOpsAgent] Checking sync status cluster=" + cluster + " type=" + resourceType);

        List<String> clusters = "both".equals(cluster)
            ? List.of("gcp-asia-south1-k3s", "aws-ap-south-1-k3s")
            : List.of(cluster.contains("gcp") ? "gcp-asia-south1-k3s" : "aws-ap-south-1-k3s");

        ArrayNode clusterResults = mapper.createArrayNode();

        for (String cl : clusters) {
            ObjectNode clNode = mapper.createObjectNode();
            clNode.put("cluster", cl);
            clNode.put("argocd_app", "ecoskiller-rbac-" + (cl.startsWith("gcp") ? "gcp" : "aws"));
            clNode.put("last_sync",  "2025-03-09T10:00:00Z");

            ArrayNode resources = mapper.createArrayNode();
            List<Map<String, Object>> rbacResources = buildSampleResources(cl, namespace, resourceType);

            boolean hasDrift = false;
            for (Map<String, Object> res : rbacResources) {
                boolean drifted = (Boolean) res.get("drifted");
                if (onlyDrifted && !drifted) continue;
                hasDrift = hasDrift || drifted;

                ObjectNode r = mapper.createObjectNode();
                r.put("kind",       (String) res.get("kind"));
                r.put("name",       (String) res.get("name"));
                if (res.containsKey("namespace")) r.put("namespace", (String) res.get("namespace"));
                r.put("sync_status", drifted ? "OUT_OF_SYNC" : "SYNCED");
                r.put("git_hash",   (String) res.get("gitHash"));
                r.put("live_hash",  drifted ? "DRIFT_DETECTED" : (String) res.get("gitHash"));
                if (drifted) {
                    r.put("drift_detail", "Manual kubectl apply detected. ArgoCD will auto-correct on next sync cycle.");
                    r.put("auto_heal",    "true — ArgoCD self-healing enabled");
                }
                resources.add(r);
            }

            clNode.set("resources",    resources);
            clNode.put("total_resources", resources.size());
            clNode.put("cluster_status",  hasDrift ? "DRIFTED — auto-correction pending" : "IN_SYNC");
            clusterResults.add(clNode);
        }

        ObjectNode result = mapper.createObjectNode();
        result.set("clusters",     clusterResults);
        result.put("git_source",   "https://git.ecoskiller.internal/infra/rbac-policies");
        result.put("argocd_url",   "https://argocd.ecoskiller.internal");
        result.put("sync_policy",  "Automated — ArgoCD syncs every 3 minutes. Self-healing enabled.");
        result.put("multi_cloud",  "GCP (asia-south1) + AWS (ap-south-1) — identical RBAC enforced");
        result.put("last_checked", Instant.now().toString());
        return result;
    }

    private List<Map<String, Object>> buildSampleResources(String cluster, String ns, String type) {
        List<Map<String, Object>> list = new ArrayList<>();
        boolean gcpDrifted = cluster.startsWith("gcp");

        if ("all".equals(type) || "Role".equals(type)) {
            list.add(Map.of("kind", "Role", "name", "pod-reader",     "namespace", ns != null ? ns : "production",
                            "gitHash", "a1b2c3d", "drifted", false));
            list.add(Map.of("kind", "Role", "name", "deployment-manager", "namespace", ns != null ? ns : "production",
                            "gitHash", "e5f6a7b", "drifted", gcpDrifted));
        }
        if ("all".equals(type) || "RoleBinding".equals(type)) {
            list.add(Map.of("kind", "RoleBinding", "name", "alice-pod-reader", "namespace", ns != null ? ns : "production",
                            "gitHash", "c8d9e0f", "drifted", false));
        }
        if ("all".equals(type) || "ClusterRole".equals(type)) {
            list.add(Map.of("kind", "ClusterRole", "name", "ecoskiller-readonly",
                            "gitHash", "1a2b3c4", "drifted", false));
        }
        return list;
    }
}
