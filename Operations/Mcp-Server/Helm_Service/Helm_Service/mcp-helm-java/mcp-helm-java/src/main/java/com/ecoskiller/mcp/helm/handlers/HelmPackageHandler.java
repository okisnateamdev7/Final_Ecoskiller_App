package com.ecoskiller.mcp.helm.handlers;

import com.ecoskiller.mcp.helm.security.SecurityValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * HelmPackageHandler
 *
 * Implements all 10 Helm K8s package manager tools.
 *
 * Ecoskiller uses Helm v3.x for:
 *   - Managing all 50 service deployments (13 custom + 37 infrastructure)
 *   - Environment-specific value overrides (dev/test/stage/prod)
 *   - Blue/green deploys and one-command rollbacks
 *   - CI/CD pipeline: GitLab CI → Harbor (self-hosted) → Helm → k3s
 *
 * Ecoskiller services deployed via Helm:
 *   auth-service, job-service, application-service, gd-orchestrator,
 *   interview-service, dojo-match-engine, scoring-engine, certification-engine,
 *   innovation-registry, royalty-management, billing-service, analytics-service,
 *   notification-service + 37 infrastructure charts
 */
public class HelmPackageHandler {

    private static final Logger LOG = Logger.getLogger(HelmPackageHandler.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityValidator validator;

    // In-memory release store (production: Helm state in k8s Secrets per namespace)
    // key = namespace:release_name
    private static final Map<String, ReleaseRecord> releaseStore = new ConcurrentHashMap<>();

    // Known Ecoskiller charts in Harbor registry
    private static final Set<String> KNOWN_CHARTS = Set.of(
        "ecoskiller/auth-service", "ecoskiller/job-service",
        "ecoskiller/application-service", "ecoskiller/gd-orchestrator",
        "ecoskiller/interview-service", "ecoskiller/dojo-match-engine",
        "ecoskiller/scoring-engine", "ecoskiller/certification-engine",
        "ecoskiller/innovation-registry", "ecoskiller/royalty-management",
        "ecoskiller/billing-service", "ecoskiller/analytics-service",
        "ecoskiller/notification-service", "ecoskiller/helm-service",
        "ecoskiller/idea-timestamp-service", "ecoskiller/idea-fingerprint-engine",
        "ecoskiller/idea-similarity-detector",
        "bitnami/postgresql", "bitnami/redis", "bitnami/kafka",
        "qdrant/qdrant", "minio/minio", "grafana/grafana", "prometheus/kube-prometheus-stack",
        "keycloak/keycloak", "opensearch/opensearch"
    );

    public HelmPackageHandler(SecurityValidator sv) {
        this.validator = sv;
        seedDemoReleases();
    }

    // ── helm_install ─────────────────────────────────────────────────────────

    public Object helmInstall(JsonNode args) {
        String releaseName  = req(args, "release_name");
        String namespace    = req(args, "namespace");
        String chartName    = req(args, "chart_name");
        String chartVersion = args.path("chart_version").asText("latest");
        String environment  = args.path("environment").asText("dev");
        String valuesYaml   = args.path("values_yaml").asText("");
        boolean atomic      = args.path("atomic").asBoolean(true);
        boolean wait        = args.path("wait").asBoolean(true);
        int timeout         = args.path("timeout_seconds").asInt(300);

        String key = storeKey(namespace, releaseName);
        if (releaseStore.containsKey(key)) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error",   "RELEASE_EXISTS");
            err.put("message", "Release '" + releaseName + "' already exists in namespace '" + namespace +
                "'. Use helm_upgrade instead.");
            return err;
        }

        String deployedAt = Instant.now().toString();
        ReleaseRecord rec = new ReleaseRecord(releaseName, namespace, chartName, chartVersion,
            environment, 1, "deployed", deployedAt, deployedAt, valuesYaml,
            new ArrayList<>(), 0);
        rec.history().add(new RevisionEntry(1, deployedAt, "deployed", chartName, chartVersion,
            "Install complete"));
        releaseStore.put(key, rec);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("status",         "success");
        resp.put("release_name",   releaseName);
        resp.put("namespace",      namespace);
        resp.put("chart",          chartName);
        resp.put("chart_version",  chartVersion);
        resp.put("revision",       1);
        resp.put("release_status", "deployed");
        resp.put("environment",    environment);
        resp.put("deployed_at",    deployedAt);
        resp.put("atomic",         atomic);
        resp.put("wait",           wait);
        resp.put("timeout_seconds",timeout);

        addHelmCommand(resp, "install", releaseName, chartName, namespace, chartVersion, environment, valuesYaml);
        addKafkaEvent(resp, "helm.release.installed", releaseName, namespace, "1");
        addCiCdNote(resp, "install", environment);

        LOG.info("helm_install: release=" + releaseName + " chart=" + chartName + " ns=" + namespace);
        return resp;
    }

    // ── helm_upgrade ─────────────────────────────────────────────────────────

    public Object helmUpgrade(JsonNode args) {
        String releaseName  = req(args, "release_name");
        String namespace    = req(args, "namespace");
        String chartName    = args.path("chart_name").asText("");
        String chartVersion = args.path("chart_version").asText("latest");
        String environment  = args.path("environment").asText("dev");
        String valuesYaml   = args.path("values_yaml").asText("");
        boolean install     = args.path("install").asBoolean(false);
        boolean atomic      = args.path("atomic").asBoolean(true);
        boolean force       = args.path("force").asBoolean(false);
        int timeout         = args.path("timeout_seconds").asInt(300);

        String key = storeKey(namespace, releaseName);
        ReleaseRecord existing = releaseStore.get(key);

        if (existing == null && !install) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error",   "RELEASE_NOT_FOUND");
            err.put("message", "Release '" + releaseName + "' not found in namespace '" + namespace +
                "'. Use helm_install or set install=true.");
            return err;
        }

        int prevRevision = (existing != null) ? existing.currentRevision() : 0;
        int newRevision  = prevRevision + 1;
        String effectiveChart = chartName.isBlank() && existing != null
            ? existing.chartName() : chartName;
        String deployedAt = Instant.now().toString();

        ReleaseRecord updated = new ReleaseRecord(
            releaseName, namespace, effectiveChart, chartVersion,
            environment, newRevision, "deployed",
            existing != null ? existing.firstDeployedAt() : deployedAt,
            deployedAt, valuesYaml,
            existing != null ? existing.history() : new ArrayList<>(), 0);
        updated.history().add(new RevisionEntry(newRevision, deployedAt, "deployed",
            effectiveChart, chartVersion, "Upgrade complete"));
        releaseStore.put(key, updated);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("status",             "success");
        resp.put("release_name",       releaseName);
        resp.put("namespace",          namespace);
        resp.put("chart",              effectiveChart);
        resp.put("chart_version",      chartVersion);
        resp.put("previous_revision",  prevRevision);
        resp.put("new_revision",       newRevision);
        resp.put("release_status",     "deployed");
        resp.put("environment",        environment);
        resp.put("upgraded_at",        deployedAt);
        resp.put("atomic",             atomic);
        resp.put("force",              force);
        resp.put("was_install",        existing == null);

        addHelmCommand(resp, "upgrade", releaseName, effectiveChart, namespace, chartVersion, environment, valuesYaml);
        addKafkaEvent(resp, "helm.release.upgraded", releaseName, namespace, String.valueOf(newRevision));
        addCiCdNote(resp, "upgrade", environment);

        LOG.info("helm_upgrade: release=" + releaseName + " rev=" + prevRevision + "→" + newRevision + " ns=" + namespace);
        return resp;
    }

    // ── helm_rollback ────────────────────────────────────────────────────────

    public Object helmRollback(JsonNode args) {
        String releaseName = req(args, "release_name");
        String namespace   = req(args, "namespace");
        int targetRevision = args.path("revision").asInt(0);
        boolean wait       = args.path("wait").asBoolean(true);
        int timeout        = args.path("timeout_seconds").asInt(300);

        String key = storeKey(namespace, releaseName);
        ReleaseRecord existing = releaseStore.get(key);

        if (existing == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error",   "RELEASE_NOT_FOUND");
            err.put("message", "Release '" + releaseName + "' not found in '" + namespace + "'");
            return err;
        }

        int currentRev = existing.currentRevision();
        if (currentRev < 2) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error",   "NO_PREVIOUS_REVISION");
            err.put("message", "Cannot rollback — release '" + releaseName + "' only has revision 1");
            return err;
        }

        int rollbackTo  = targetRevision > 0 ? targetRevision : currentRev - 1;
        int newRevision = currentRev + 1;
        String rolledAt = Instant.now().toString();

        // Find target revision entry
        RevisionEntry targetEntry = existing.history().stream()
            .filter(r -> r.revision() == rollbackTo)
            .findFirst()
            .orElse(existing.history().get(0));

        ReleaseRecord rolled = new ReleaseRecord(
            releaseName, namespace, targetEntry.chart(), targetEntry.chartVersion(),
            existing.environment(), newRevision, "deployed",
            existing.firstDeployedAt(), rolledAt, existing.valuesYaml(),
            existing.history(), existing.failureCount());
        rolled.history().add(new RevisionEntry(newRevision, rolledAt, "deployed",
            targetEntry.chart(), targetEntry.chartVersion(),
            "Rollback to revision " + rollbackTo));
        releaseStore.put(key, rolled);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("status",                    "success");
        resp.put("release_name",              releaseName);
        resp.put("namespace",                 namespace);
        resp.put("rolled_back_from_revision", currentRev);
        resp.put("rolled_back_to_revision",   rollbackTo);
        resp.put("new_revision",              newRevision);
        resp.put("chart",                     targetEntry.chart());
        resp.put("chart_version",             targetEntry.chartVersion());
        resp.put("release_status",            "deployed");
        resp.put("rolled_back_at",            rolledAt);

        ObjectNode cmd = resp.putObject("helm_command");
        cmd.put("command", "helm rollback " + releaseName + " " + rollbackTo +
            " --namespace " + namespace + " --wait=" + wait +
            " --timeout " + timeout + "s");
        cmd.put("note", "Rollback applies previous values and manifests — no values_yaml needed");

        addKafkaEvent(resp, "helm.release.rolled-back", releaseName, namespace, String.valueOf(newRevision));

        LOG.info("helm_rollback: release=" + releaseName + " from_rev=" + currentRev + " to_rev=" + rollbackTo);
        return resp;
    }

    // ── helm_uninstall ───────────────────────────────────────────────────────

    public Object helmUninstall(JsonNode args) {
        String releaseName = req(args, "release_name");
        String namespace   = req(args, "namespace");
        boolean keepHist   = args.path("keep_history").asBoolean(false);
        int timeout        = args.path("timeout_seconds").asInt(60);

        String key = storeKey(namespace, releaseName);
        ReleaseRecord existing = releaseStore.get(key);

        if (existing == null) {
            ObjectNode err = mapper.createObjectNode();
            err.put("error",   "RELEASE_NOT_FOUND");
            err.put("message", "Release '" + releaseName + "' not found in namespace '" + namespace + "'");
            return err;
        }

        if (!keepHist) releaseStore.remove(key);
        else {
            // Mark as uninstalled but keep record
            ReleaseRecord uninstalled = new ReleaseRecord(
                existing.releaseName(), existing.namespace(), existing.chartName(),
                existing.chartVersion(), existing.environment(), existing.currentRevision(),
                "uninstalled", existing.firstDeployedAt(), Instant.now().toString(),
                existing.valuesYaml(), existing.history(), existing.failureCount());
            releaseStore.put(key, uninstalled);
        }

        ObjectNode resp = mapper.createObjectNode();
        resp.put("status",            "success");
        resp.put("release_name",      releaseName);
        resp.put("namespace",         namespace);
        resp.put("uninstalled_at",    Instant.now().toString());
        resp.put("history_preserved", keepHist);
        resp.put("chart",             existing.chartName());
        resp.put("final_revision",    existing.currentRevision());
        resp.put("resources_deleted", "Deployment, Service, ConfigMap, Secrets, HPA, NetworkPolicy");

        ObjectNode cmd = resp.putObject("helm_command");
        cmd.put("command", "helm uninstall " + releaseName + " --namespace " + namespace +
            (keepHist ? " --keep-history" : "") + " --timeout " + timeout + "s");
        cmd.put("warning", "All Kubernetes resources managed by this release have been deleted");

        addKafkaEvent(resp, "helm.release.uninstalled", releaseName, namespace,
            String.valueOf(existing.currentRevision()));

        LOG.info("helm_uninstall: release=" + releaseName + " ns=" + namespace);
        return resp;
    }

    // ── helm_list_releases ───────────────────────────────────────────────────

    public Object helmListReleases(JsonNode args) {
        String nsFilter  = args.path("namespace").asText("").toLowerCase();
        String envFilter = args.path("environment").asText("").toLowerCase();
        String statusFilt= args.path("status").asText("").toLowerCase();

        List<ReleaseRecord> all = new ArrayList<>(releaseStore.values());
        if (!nsFilter.isBlank())   all.removeIf(r -> !r.namespace().equals(nsFilter));
        if (!envFilter.isBlank())  all.removeIf(r -> !r.environment().equals(envFilter));
        if (!statusFilt.isBlank()) all.removeIf(r -> !r.status().equals(statusFilt));

        // Stats
        long deployed   = all.stream().filter(r -> "deployed".equals(r.status())).count();
        long failed     = all.stream().filter(r -> "failed".equals(r.status())).count();
        long uninstalled= all.stream().filter(r -> "uninstalled".equals(r.status())).count();

        ObjectNode resp = mapper.createObjectNode();
        resp.put("total_releases",        all.size());
        resp.put("namespace_filter",      nsFilter.isBlank()   ? "ALL" : nsFilter);
        resp.put("environment_filter",    envFilter.isBlank()  ? "ALL" : envFilter);
        resp.put("status_filter",         statusFilt.isBlank() ? "ALL" : statusFilt);

        ObjectNode stats = resp.putObject("status_summary");
        stats.put("deployed",    deployed);
        stats.put("failed",      failed);
        stats.put("uninstalled", uninstalled);

        ArrayNode list = resp.putArray("releases");
        for (ReleaseRecord r : all) {
            ObjectNode n = list.addObject();
            n.put("name",          r.releaseName());
            n.put("namespace",     r.namespace());
            n.put("revision",      r.currentRevision());
            n.put("status",        r.status());
            n.put("chart",         r.chartName());
            n.put("chart_version", r.chartVersion());
            n.put("environment",   r.environment());
            n.put("last_deployed", r.lastDeployedAt());
        }

        resp.put("helm_command", "helm list --all-namespaces" +
            (nsFilter.isBlank() ? "" : " --namespace " + nsFilter));
        return resp;
    }

    // ── helm_get_status ──────────────────────────────────────────────────────

    public Object helmGetStatus(JsonNode args) {
        String releaseName = req(args, "release_name");
        String namespace   = req(args, "namespace");
        String key = storeKey(namespace, releaseName);
        ReleaseRecord rec = releaseStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("release_name", releaseName);
        resp.put("namespace",    namespace);

        if (rec == null) {
            resp.put("found",   false);
            resp.put("message", "Release '" + releaseName + "' not found in '" + namespace + "'");
            return resp;
        }

        resp.put("found",            true);
        resp.put("status",           rec.status());
        resp.put("chart",            rec.chartName());
        resp.put("chart_version",    rec.chartVersion());
        resp.put("revision",         rec.currentRevision());
        resp.put("environment",      rec.environment());
        resp.put("first_deployed",   rec.firstDeployedAt());
        resp.put("last_deployed",    rec.lastDeployedAt());
        resp.put("description",
            "Release '" + releaseName + "' is " + rec.status() +
            " at revision " + rec.currentRevision());

        ObjectNode resources = resp.putObject("kubernetes_resources");
        resources.put("deployments",  1);
        resources.put("pods",         3);
        resources.put("services",     1);
        resources.put("configmaps",   2);
        resources.put("secrets",      2);
        resources.put("hpa",          1);
        resources.put("network_policies", 1);

        resp.put("helm_command", "helm status " + releaseName + " --namespace " + namespace);
        return resp;
    }

    // ── helm_get_values ──────────────────────────────────────────────────────

    public Object helmGetValues(JsonNode args) {
        String releaseName = req(args, "release_name");
        String namespace   = req(args, "namespace");
        boolean allValues  = args.path("all_values").asBoolean(false);
        String key = storeKey(namespace, releaseName);
        ReleaseRecord rec = releaseStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("release_name", releaseName);
        resp.put("namespace",    namespace);
        resp.put("all_values",   allValues);

        if (rec == null) {
            resp.put("found",   false);
            resp.put("message", "Release not found: " + releaseName);
            return resp;
        }

        resp.put("found", true);
        resp.put("user_supplied_values", rec.valuesYaml().isBlank() ? "(none)" : rec.valuesYaml());

        if (allValues) {
            ObjectNode defaults = resp.putObject("chart_defaults");
            defaults.put("replicaCount",        "2");
            defaults.put("image.pullPolicy",     "Always");
            defaults.put("image.repository",     "harbor.ecoskiller.internal/ecoskiller/" + releaseName);
            defaults.put("image.tag",            rec.chartVersion());
            defaults.put("resources.requests.cpu","200m");
            defaults.put("resources.limits.cpu", "800m");
            defaults.put("resources.requests.memory","256Mi");
            defaults.put("resources.limits.memory","768Mi");
            defaults.put("autoscaling.enabled",  "true");
            defaults.put("autoscaling.minReplicas","2");
            defaults.put("autoscaling.maxReplicas","6");
            defaults.put("service.type",         "ClusterIP");
            defaults.put("service.port",         "8080");
        }

        resp.put("helm_command", "helm get values " + releaseName + " --namespace " + namespace +
            (allValues ? " --all" : ""));
        return resp;
    }

    // ── helm_get_history ─────────────────────────────────────────────────────

    public Object helmGetHistory(JsonNode args) {
        String releaseName = req(args, "release_name");
        String namespace   = req(args, "namespace");
        int    maxRevs     = args.path("max").asInt(20);
        String key = storeKey(namespace, releaseName);
        ReleaseRecord rec = releaseStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("release_name", releaseName);
        resp.put("namespace",    namespace);

        if (rec == null) {
            resp.put("found",   false);
            resp.put("message", "Release not found: " + releaseName);
            return resp;
        }

        resp.put("found",           true);
        resp.put("total_revisions", rec.history().size());
        resp.put("current_revision",rec.currentRevision());

        ArrayNode history = resp.putArray("history");
        rec.history().stream()
            .sorted(Comparator.comparingInt(RevisionEntry::revision).reversed())
            .limit(maxRevs)
            .forEach(h -> {
                ObjectNode n = history.addObject();
                n.put("revision",      h.revision());
                n.put("updated",       h.updatedAt());
                n.put("status",        h.status());
                n.put("chart",         h.chart());
                n.put("chart_version", h.chartVersion());
                n.put("description",   h.description());
            });

        resp.put("helm_command", "helm history " + releaseName + " --namespace " + namespace + " --max " + maxRevs);
        resp.put("rollback_tip", "Use helm_rollback with a specific revision number to revert");
        return resp;
    }

    // ── helm_lint_chart ──────────────────────────────────────────────────────

    public Object helmLintChart(JsonNode args) {
        String chartName  = args.path("chart_name").asText("(not specified)");
        String chartPath  = args.path("chart_path").asText("./charts/" + chartName.replace("ecoskiller/",""));
        String valuesYaml = args.path("values_yaml").asText("");
        String environment= args.path("environment").asText("dev");
        boolean strict    = args.path("strict").asBoolean(false);

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        // Simulate lint checks
        if (valuesYaml.contains("password:") && !valuesYaml.contains("secretKeyRef"))
            warnings.add("Detected plain-text 'password' in values — use Kubernetes Secret reference instead");
        if (chartName.equals("(not specified)"))
            suggestions.add("Provide chart_name for more accurate lint feedback");
        if (!valuesYaml.contains("resources:"))
            suggestions.add("Consider specifying resource requests/limits for all containers");
        if (strict && !warnings.isEmpty())
            errors.addAll(warnings);

        boolean passed = errors.isEmpty();

        ObjectNode resp = mapper.createObjectNode();
        resp.put("chart_name",   chartName);
        resp.put("chart_path",   chartPath);
        resp.put("environment",  environment);
        resp.put("strict_mode",  strict);
        resp.put("lint_passed",  passed);
        resp.put("error_count",  errors.size());
        resp.put("warning_count",warnings.size());

        ArrayNode errArr  = resp.putArray("errors");
        errors.forEach(errArr::add);
        ArrayNode warnArr = resp.putArray("warnings");
        warnings.forEach(warnArr::add);
        ArrayNode suggArr = resp.putArray("suggestions");
        suggestions.forEach(suggArr::add);

        resp.put("helm_command", "helm lint " + chartPath +
            (valuesYaml.isBlank() ? "" : " -f values/" + environment + ".yaml") +
            (strict ? " --strict" : ""));
        resp.put("ci_stage_note",
            "This check runs in GitLab CI 'lint' stage before build/deploy. " +
            "Failures block the pipeline.");

        LOG.info("helm_lint: chart=" + chartName + " passed=" + passed);
        return resp;
    }

    // ── helm_diff_upgrade ────────────────────────────────────────────────────

    public Object helmDiffUpgrade(JsonNode args) {
        String releaseName  = req(args, "release_name");
        String namespace    = req(args, "namespace");
        String chartName    = args.path("chart_name").asText("");
        String chartVersion = args.path("chart_version").asText("latest");
        String valuesYaml   = args.path("values_yaml").asText("");
        String environment  = args.path("environment").asText("dev");

        String key = storeKey(namespace, releaseName);
        ReleaseRecord existing = releaseStore.get(key);

        ObjectNode resp = mapper.createObjectNode();
        resp.put("release_name",   releaseName);
        resp.put("namespace",      namespace);
        resp.put("environment",    environment);
        resp.put("current_chart",  existing != null ? existing.chartName() : "(not deployed)");
        resp.put("current_version",existing != null ? existing.chartVersion() : "N/A");
        resp.put("proposed_chart", chartName.isBlank() && existing != null ? existing.chartName() : chartName);
        resp.put("proposed_version", chartVersion);
        resp.put("diff_computed",  true);
        resp.put("changes_detected", existing != null && !chartVersion.equals(existing.chartVersion()));

        // Simulate diff output
        ArrayNode added    = resp.putArray("added_resources");
        ArrayNode modified = resp.putArray("modified_resources");
        ArrayNode removed  = resp.putArray("removed_resources");

        if (existing != null && !chartVersion.equals(existing.chartVersion())) {
            ObjectNode modDeploy = modified.addObject();
            modDeploy.put("kind",       "Deployment");
            modDeploy.put("name",       releaseName);
            modDeploy.put("change",     "image.tag: " + existing.chartVersion() + " → " + chartVersion);
            modDeploy.put("namespace",  namespace);
        } else if (existing == null) {
            ObjectNode addDeploy = added.addObject();
            addDeploy.put("kind",    "Deployment");
            addDeploy.put("name",    releaseName);
            addDeploy.put("namespace",namespace);
        }

        if (!valuesYaml.isBlank()) {
            ObjectNode valDiff = resp.putObject("config_changes");
            valDiff.put("values_yaml_changed", true);
            valDiff.put("note", "Values override provided — diff computed against current release values");
        }

        resp.put("helm_command",
            "helm diff upgrade " + releaseName + " " +
            (chartName.isBlank() ? (existing != null ? existing.chartName() : "CHART") : chartName) +
            " --namespace " + namespace + " --version " + chartVersion +
            (valuesYaml.isBlank() ? "" : " -f values/" + environment + ".yaml"));
        resp.put("plugin_note",
            "Requires helm-diff plugin: helm plugin install https://github.com/databus23/helm-diff");
        resp.put("safety_note",
            "Run helm_diff_upgrade before helm_upgrade to review changes in production");

        return resp;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void addHelmCommand(ObjectNode resp, String action, String releaseName,
                                 String chartName, String namespace, String chartVersion,
                                 String environment, String valuesYaml) {
        ObjectNode cmd = resp.putObject("helm_command");
        String base = "helm " + action + " " + releaseName + " " + chartName +
            " --namespace " + namespace +
            " --version " + chartVersion +
            " --atomic --wait --timeout 300s" +
            (valuesYaml.isBlank() ? "" : " -f values/" + environment + ".yaml");
        cmd.put("command", base);
        cmd.put("harbor_registry", "harbor.ecoskiller.internal");
        cmd.put("kubeconfig_env_var", "KUBECONFIG_" + environment.toUpperCase());
    }

    private void addKafkaEvent(ObjectNode resp, String eventType, String releaseName,
                                String namespace, String revision) {
        ObjectNode event = resp.putObject("kafka_event_published");
        event.put("topic",        "helm.events");
        event.put("event_type",   eventType);
        event.put("release_name", releaseName);
        event.put("namespace",    namespace);
        event.put("revision",     revision);
        event.put("timestamp",    Instant.now().toString());
    }

    private void addCiCdNote(ObjectNode resp, String action, String environment) {
        ObjectNode ci = resp.putObject("cicd_pipeline_context");
        ci.put("pipeline",  "GitLab CI (Self-Hosted)");
        ci.put("registry",  "Harbor (Self-Hosted) — harbor.ecoskiller.internal");
        ci.put("cluster",   "k3s self-managed GCP asia-south1 + AWS ap-south-1");
        ci.put("stage",     "helm-deploy (final pipeline stage after lint → test → trivy-scan → harbor-push)");
        ci.put("environment", environment);
        ci.put("kubeconfig", "KUBECONFIG_" + environment.toUpperCase() +
            " (least-privilege k3s kubeconfig per environment)");
    }

    private String storeKey(String ns, String name) { return ns + ":" + name; }

    private String req(JsonNode args, String field) {
        JsonNode n = args.get(field);
        if (n == null || n.isNull() || n.asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return n.asText().trim();
    }

    // ── Demo seed ─────────────────────────────────────────────────────────────

    private void seedDemoReleases() {
        String now = Instant.now().toString();
        String[][] seeds = {
            {"auth-service",          "prod", "ecoskiller/auth-service",           "1.4.2"},
            {"job-service",           "prod", "ecoskiller/job-service",            "2.1.0"},
            {"gd-orchestrator",       "prod", "ecoskiller/gd-orchestrator",        "1.3.1"},
            {"scoring-engine",        "prod", "ecoskiller/scoring-engine",         "1.2.0"},
            {"notification-service",  "prod", "ecoskiller/notification-service",   "1.1.3"},
            {"idea-timestamp-service","prod", "ecoskiller/idea-timestamp-service", "1.0.0"},
            {"idea-fingerprint-engine","prod","ecoskiller/idea-fingerprint-engine","1.0.0"},
            {"idea-similarity-detector","prod","ecoskiller/idea-similarity-detector","1.0.0"},
            {"postgresql",            "core", "bitnami/postgresql",                "12.5.0"},
            {"redis",                 "core", "bitnami/redis",                     "18.1.0"},
            {"kafka",                 "core", "bitnami/kafka",                     "26.4.0"},
            {"grafana",               "monitoring","grafana/grafana",              "7.0.0"},
        };
        for (String[] s : seeds) {
            String key = storeKey(s[1], s[0]);
            List<RevisionEntry> hist = new ArrayList<>();
            hist.add(new RevisionEntry(1, now, "deployed", s[2], s[3], "Initial install"));
            releaseStore.put(key, new ReleaseRecord(s[0], s[1], s[2], s[3], "prod",
                1, "deployed", now, now, "", hist, 0));
        }
    }

    // ── Records ───────────────────────────────────────────────────────────────

    static class ReleaseRecord {
        private final String releaseName, namespace, chartName, chartVersion, environment;
        private int currentRevision;
        private String status, firstDeployedAt, lastDeployedAt, valuesYaml;
        private final List<RevisionEntry> history;
        private final int failureCount;

        ReleaseRecord(String releaseName, String namespace, String chartName, String chartVersion,
                      String environment, int currentRevision, String status,
                      String firstDeployedAt, String lastDeployedAt, String valuesYaml,
                      List<RevisionEntry> history, int failureCount) {
            this.releaseName = releaseName; this.namespace = namespace;
            this.chartName = chartName; this.chartVersion = chartVersion;
            this.environment = environment; this.currentRevision = currentRevision;
            this.status = status; this.firstDeployedAt = firstDeployedAt;
            this.lastDeployedAt = lastDeployedAt; this.valuesYaml = valuesYaml;
            this.history = history; this.failureCount = failureCount;
        }

        String releaseName()     { return releaseName; }
        String namespace()       { return namespace; }
        String chartName()       { return chartName; }
        String chartVersion()    { return chartVersion; }
        String environment()     { return environment; }
        int    currentRevision() { return currentRevision; }
        String status()          { return status; }
        String firstDeployedAt() { return firstDeployedAt; }
        String lastDeployedAt()  { return lastDeployedAt; }
        String valuesYaml()      { return valuesYaml; }
        List<RevisionEntry> history() { return history; }
        int failureCount()       { return failureCount; }
    }

    record RevisionEntry(int revision, String updatedAt, String status,
                         String chart, String chartVersion, String description) {}
}
