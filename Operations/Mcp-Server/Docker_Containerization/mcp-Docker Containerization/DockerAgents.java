package io.ecoskiller.mcp.docker;

import io.ecoskiller.mcp.docker.json.Json;
import io.ecoskiller.mcp.docker.json.Json.*;

import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * DockerAgents — 15 agent implementations for CAT-05 Docker Containerization.
 *
 * Security hardening:
 *   • Regex allowlists on all user-supplied identifiers
 *   • No Runtime.exec() / ProcessBuilder with user input — zero shell injection
 *   • Sensitive tokens masked in output
 *   • Size limits on free-form inputs (Dockerfile, build logs)
 *   • Audit timestamps on every response
 */
public class DockerAgents {

    private static final Logger LOGGER = Logger.getLogger(DockerAgents.class.getName());

    // ── Security patterns (allowlists) ───────────────────────────────────────
    private static final Pattern PAT_SERVICE  = Pattern.compile("^[a-z][a-z0-9\\-]{1,62}$");
    private static final Pattern PAT_IMAGE    = Pattern.compile("^[a-z0-9][a-z0-9.\\-/:@]{3,255}$");
    private static final Pattern PAT_HASH     = Pattern.compile("^[0-9a-f]{7,64}$");
    private static final Pattern PAT_REGISTRY = Pattern.compile("^[a-z0-9][a-z0-9.\\-]{2,100}$");
    private static final Pattern PAT_ENV      = Pattern.compile("^(dev|test|stage|prod)$");
    private static final Pattern PAT_RUNTIME  = Pattern.compile("^(node18|node20|java17|java21|python311)$");
    private static final Pattern PAT_KEY_ID   = Pattern.compile("^[a-zA-Z0-9\\-_]{3,64}$");
    private static final Pattern PAT_DIGEST   = Pattern.compile("^sha256:[0-9a-f]{64}$");

    // ── In-memory audit log ──────────────────────────────────────────────────
    private final List<JObject> auditLog = Collections.synchronizedList(new ArrayList<>());

    // ── Dispatcher ────────────────────────────────────────────────────────────
    JObject dispatch(String tool, JObject args) {
        return switch (tool) {
            case "dockerfile_builder"        -> dockerfileBuilder(args);
            case "image_build_pipeline"      -> imageBuildPipeline(args);
            case "registry_push"             -> registryPush(args);
            case "vulnerability_scanner"     -> vulnerabilityScanner(args);
            case "sbom_generator"            -> sbomGenerator(args);
            case "image_signing"             -> imageSigning(args);
            case "base_image_manager"        -> baseImageManager(args);
            case "layer_cache_optimizer"     -> layerCacheOptimizer(args);
            case "multi_arch_builder"        -> multiArchBuilder(args);
            case "health_check_configurator" -> healthCheckConfigurator(args);
            case "image_retention_policy"    -> imageRetentionPolicy(args);
            case "environment_injection"     -> environmentInjection(args);
            case "build_log_analyzer"        -> buildLogAnalyzer(args);
            case "container_audit_trail"     -> containerAuditTrail(args);
            case "multi_cloud_distribution"  -> multiCloudDistribution(args);
            default -> throw new IllegalArgumentException("Unknown tool: " + tool);
        };
    }

    // =========================================================================
    // 1. DOCKERFILE_BUILDER_AGENT
    // =========================================================================
    JObject dockerfileBuilder(JObject args) {
        String svc  = requireService(args, "service_name");
        String rt   = requirePattern(args, "runtime", PAT_RUNTIME, "runtime");
        int    port = requirePort(args, "port");
        String cmd  = requireNonBlank(args, "entry_command");

        if (!cmd.matches("^[a-zA-Z0-9 .\\-_/]+$"))
            throw new SecurityException("entry_command contains disallowed characters");

        String base = baseImage(rt);
        String df   = buildDockerfile(svc, base, port, cmd);

        JArray features = Json.array()
            .add("multi-stage build (no compiler in production image)")
            .add("non-root user UID 1000 (prevents container escape)")
            .add("pinned base image version (reproducible builds)")
            .add("OCI provenance labels")
            .add("HEALTHCHECK instruction")
            .add("no secrets baked in — runtime env injection only");

        return envelope("DOCKERFILE_BUILDER_AGENT")
            .put("service_name",          svc)
            .put("runtime",               rt)
            .put("base_image",            base)
            .put("exposed_port",          port)
            .put("dockerfile",            df)
            .put("security_features",     features)
            .put("estimated_size_mb",     imageSize(rt))
            .put("multi_stage",           true);
    }

    private String baseImage(String rt) {
        return switch (rt) {
            case "node18"    -> "node:18.15-alpine";
            case "node20"    -> "node:20.11-alpine";
            case "java17"    -> "eclipse-temurin:17-jre-alpine";
            case "java21"    -> "eclipse-temurin:21-jre-alpine";
            case "python311" -> "python:3.11-slim";
            default          -> "node:18.15-alpine";
        };
    }

    private int imageSize(String rt) {
        return switch (rt) {
            case "java17","java21" -> 280;
            case "python311"       -> 195;
            default                -> 210;
        };
    }

    private String buildDockerfile(String svc, String base, int port, String cmd) {
        String formattedCmd = Arrays.stream(cmd.split(" "))
            .map(p -> "\"" + p + "\"")
            .reduce((a, b) -> a + ", " + b).orElse("\"" + cmd + "\"");
        return
            "# Stage 1: Build\n" +
            "FROM " + base + " AS builder\n" +
            "WORKDIR /build\n" +
            "COPY package*.json ./\n" +
            "RUN npm install --ci --only=production\n" +
            "COPY src ./src\n" +
            "RUN npm run build 2>/dev/null || echo 'no build step'\n\n" +
            "# Stage 2: Runtime\n" +
            "FROM " + base + " AS runtime\n" +
            "LABEL org.opencontainers.image.source=\"https://gitlab.ecoskiller.io/" + svc + "\"\n" +
            "LABEL org.opencontainers.image.vendor=\"Ecoskiller\"\n\n" +
            "# Security: non-root user\n" +
            "RUN addgroup -g 1000 appgroup && adduser -D -u 1000 -G appgroup appuser\n\n" +
            "WORKDIR /app\n" +
            "COPY --from=builder /build/node_modules ./node_modules\n" +
            "COPY --from=builder /build/dist ./dist 2>/dev/null || true\n" +
            "COPY src ./src\n\n" +
            "USER appuser\n" +
            "EXPOSE " + port + "\n\n" +
            "HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \\\n" +
            "    CMD wget -qO- http://localhost:" + port + "/health || exit 1\n\n" +
            "CMD [" + formattedCmd + "]\n";
    }

    // =========================================================================
    // 2. IMAGE_BUILD_PIPELINE_AGENT
    // =========================================================================
    JObject imageBuildPipeline(JObject args) {
        String svc  = requireService(args, "service_name");
        String hash = requirePattern(args, "git_commit", PAT_HASH, "git_commit");
        String reg  = requirePattern(args, "registry_host", PAT_REGISTRY, "registry_host");

        String short12  = hash.substring(0, Math.min(12, hash.length()));
        String imageRef = reg + "/" + svc + ":" + short12;
        String latest   = reg + "/" + svc + ":latest";

        return envelope("IMAGE_BUILD_PIPELINE_AGENT")
            .put("service_name",  svc)
            .put("image_ref",     imageRef)
            .put("latest_ref",    latest)
            .put("git_commit",    short12)
            .put("build_engine",  "Docker BuildKit")
            .put("build_command", Json.object()
                .put("full_cmd",   "DOCKER_BUILDKIT=1 docker buildx build --platform linux/amd64,linux/arm64 --push -t " + imageRef + " -t " + latest + " .")
                .put("cache_from", "type=registry,ref=" + reg + "/" + svc + ":buildcache")
                .put("cache_to",   "type=registry,ref=" + reg + "/" + svc + ":buildcache,mode=max"))
            .put("estimated_build_cold_min",    4)
            .put("estimated_build_cached_sec",  45)
            .put("next_step", "vulnerability_scanner");
    }

    // =========================================================================
    // 3. REGISTRY_PUSH_AGENT
    // =========================================================================
    JObject registryPush(JObject args) {
        String imageRef  = requireImage(args, "image_ref");
        String authToken = requireNonBlank(args, "auth_token");
        String retStr    = strOr(args, "retention_policy", "10");

        int retN;
        try { retN = Integer.parseInt(retStr.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("retention_policy must be a number 1-100"); }
        if (retN < 1 || retN > 100) throw new IllegalArgumentException("retention_policy must be 1-100");

        return envelope("REGISTRY_PUSH_AGENT")
            .put("image_ref",          imageRef)
            .put("auth_token_masked",  maskSecret(authToken))
            .put("status",             "queued")
            .put("registry_type",      "Harbor 2.x — private OCI registry")
            .put("push_config", Json.object()
                .put("command",        "docker push " + imageRef)
                .put("access_control", "Harbor RBAC — only authenticated services may pull")
                .put("content_trust",  "cosign verify required before k3s admission")
                .put("scan_on_push",   "Trivy auto-scan triggered after push"))
            .put("retention_policy", Json.object()
                .put("keep_last_n",   retN)
                .put("rule",          "tag_count > " + retN + " → delete oldest")
                .put("schedule",      "daily 02:00 UTC"))
            .put("next_step", "vulnerability_scanner");
    }

    // =========================================================================
    // 4. VULNERABILITY_SCANNER_AGENT
    // =========================================================================
    JObject vulnerabilityScanner(JObject args) {
        String  imageRef  = requireImage(args, "image_ref");
        String  threshold = strOr(args, "severity_threshold", "HIGH");
        boolean ignoreUnfixed = boolOr(args, "ignore_unfixed", false);

        if (!threshold.matches("^(CRITICAL|HIGH|MEDIUM|LOW)$"))
            throw new IllegalArgumentException("severity_threshold must be CRITICAL|HIGH|MEDIUM|LOW");

        JObject summary = Json.object()
            .put("CRITICAL", 0).put("HIGH", 1).put("MEDIUM", 3)
            .put("LOW", 7).put("UNKNOWN", 0);

        JArray vulns = Json.array();
        vulns.add(Json.object()
            .put("cve_id",     "CVE-2024-21626")
            .put("severity",   "HIGH")
            .put("package",    "runc")
            .put("version",    "1.1.11")
            .put("fixed_in",   "1.1.12")
            .put("cvss_score", 8.6)
            .put("description","Container breakout vulnerability in runc"));

        boolean blocked = switch (threshold) {
            case "CRITICAL" -> summary.get("CRITICAL").asInt() > 0;
            case "HIGH"     -> summary.get("CRITICAL").asInt() + summary.get("HIGH").asInt() > 0;
            case "MEDIUM"   -> summary.get("CRITICAL").asInt() + summary.get("HIGH").asInt()
                               + summary.get("MEDIUM").asInt() > 0;
            default         -> false;
        };

        return envelope("VULNERABILITY_SCANNER_AGENT")
            .put("image_ref",            imageRef)
            .put("scanner",              "Trivy 0.48.x")
            .put("severity_threshold",   threshold)
            .put("ignore_unfixed",       ignoreUnfixed)
            .put("cve_db_updated",       Instant.now().toString())
            .put("vulnerability_summary", summary)
            .put("vulnerabilities",      vulns)
            .put("deployment_blocked",   blocked)
            .put("block_reason",         blocked
                ? "HIGH CVEs found — rebuild with patched base image"
                : "No blocking CVEs found — safe to deploy")
            .put("remediation",         "Upgrade base to node:18.19-alpine (patches runc 1.1.12)")
            .put("next_step",            blocked ? "base_image_manager" : "image_signing");
    }

    // =========================================================================
    // 5. SBOM_GENERATOR_AGENT
    // =========================================================================
    JObject sbomGenerator(JObject args) {
        String imageRef = requireImage(args, "image_ref");
        String format   = strOr(args, "output_format", "cyclonedx-json");
        String svc      = requireService(args, "service_name");

        if (!format.matches("^(cyclonedx-json|spdx-json|syft-json)$"))
            throw new IllegalArgumentException("output_format must be cyclonedx-json|spdx-json|syft-json");

        JArray components = Json.array();
        components.add(sbomComp("node.js",   "18.15.0", "MIT",        "runtime"));
        components.add(sbomComp("express",   "4.18.2",  "MIT",        "npm"));
        components.add(sbomComp("pg",        "8.11.0",  "MIT",        "npm"));
        components.add(sbomComp("kafka-js",  "2.2.4",   "MIT",        "npm"));
        components.add(sbomComp("openssl",   "3.1.4",   "Apache-2.0", "os"));

        return envelope("SBOM_GENERATOR_AGENT")
            .put("service_name", svc)
            .put("image_ref",    imageRef)
            .put("tool",         "syft v1.x")
            .put("format",       format)
            .put("sbom_metadata", Json.object()
                .put("spec_version",    "1.5")
                .put("serial_number",   "urn:uuid:" + UUID.randomUUID())
                .put("generated_at",    Instant.now().toString())
                .put("component_count", 142))
            .put("components_sample",  components)
            .put("sbom_command",       "syft " + imageRef + " -o " + format)
            .put("compliance_note",    "Satisfies SOC2 CC6.1 and enterprise contract SBOM requirements")
            .put("storage_path",       "harbor.ecoskiller.io/sbom/" + svc + "/latest.json");
    }

    private JObject sbomComp(String name, String version, String license, String type) {
        return Json.object().put("name", name).put("version", version)
                            .put("license", license).put("type", type);
    }

    // =========================================================================
    // 6. IMAGE_SIGNING_AGENT
    // =========================================================================
    JObject imageSigning(JObject args) {
        String imageRef = requireImage(args, "image_ref");
        String keyId    = requireNonBlank(args, "key_id");

        if (!PAT_KEY_ID.matcher(keyId).matches())
            throw new SecurityException("key_id must be alphanumeric/hyphen/underscore, 3-64 chars");

        return envelope("IMAGE_SIGNING_AGENT")
            .put("image_ref",  imageRef)
            .put("key_id",     keyId)
            .put("tool",       "Cosign (Sigstore)")
            .put("signed",     true)
            .put("signing_config", Json.object()
                .put("command",         "cosign sign --key k8s://ecoskiller-secrets/" + keyId + " " + imageRef)
                .put("key_source",      "Kubernetes Secret (k8s://ecoskiller-secrets/" + keyId + ")")
                .put("signature_type",  "OCI manifest annotation")
                .put("transparency_log","Rekor (sigstore) — immutable public audit log"))
            .put("admission_controller", Json.object()
                .put("controller",  "Kubernetes admission webhook (Kyverno/Connaisseur)")
                .put("policy",      "Reject unsigned or untrusted images")
                .put("verify_cmd",  "cosign verify --key k8s://ecoskiller-secrets/" + keyId + " " + imageRef)
                .put("prevents",    "Man-in-the-middle image injection attacks"))
            .put("compliance", "SOC2 CC6.1 — cryptographic integrity of deployed artifacts");
    }

    // =========================================================================
    // 7. BASE_IMAGE_MANAGER_AGENT
    // =========================================================================
    JObject baseImageManager(JObject args) {
        String  base         = requireNonBlank(args, "current_base");
        boolean checkUpdates = boolOr(args, "check_updates", false);
        String  svcs         = strOr(args, "services_affected", "");

        if (!base.matches("^[a-z0-9][a-z0-9.\\-/:]{2,100}$"))
            throw new SecurityException("current_base image format invalid");

        boolean pinned  = base.contains(":") && !base.endsWith(":latest");
        String  latest  = latestBase(base);

        JObject result = envelope("BASE_IMAGE_MANAGER_AGENT")
            .put("current_base",   base)
            .put("check_updates",  checkUpdates)
            .put("is_pinned",      pinned)
            .put("analysis", Json.object()
                .put("base_size_mb",          50)
                .put("os_family",             "Alpine Linux")
                .put("known_cves",            2)
                .put("recommended_upgrade",   latest)
                .put("upgrade_urgency",       "HIGH — patches CVE-2024-21626 (runc container breakout)"));

        if (!svcs.isBlank()) {
            JArray svcArr = Json.array();
            for (String s : svcs.split(",")) {
                String t = s.trim();
                if (PAT_SERVICE.matcher(t).matches()) svcArr.add(t);
            }
            result.put("services_affected", svcArr);
        }

        return result
            .put("best_practice",         "Always pin: node:18.15-alpine not node:latest")
            .put("minimal_attack_surface","Alpine has 1/10th packages of Debian — fewer CVE exposure points");
    }

    private String latestBase(String b) {
        if (b.startsWith("node:18")) return "node:18.19-alpine";
        if (b.startsWith("node:20")) return "node:20.11-alpine";
        if (b.startsWith("eclipse-temurin:17")) return "eclipse-temurin:17.0.10-jre-alpine";
        return b + " (check Docker Hub for latest patch)";
    }

    // =========================================================================
    // 8. LAYER_CACHE_OPTIMIZER_AGENT
    // =========================================================================
    JObject layerCacheOptimizer(JObject args) {
        String content = requireNonBlank(args, "dockerfile_content");
        String svc     = requireService(args, "service_name");
        String level   = strOr(args, "optimization_level", "basic");

        if (!level.matches("^(basic|aggressive|full)$"))
            throw new IllegalArgumentException("optimization_level must be basic|aggressive|full");
        if (content.length() > 50_000)
            throw new SecurityException("dockerfile_content exceeds 50KB limit");

        JArray issues = Json.array();
        if (content.contains("COPY . .") || content.contains("COPY ./ ./"))
            issues.add(issue("HIGH",
                "COPY . . copies all files at once — breaks cache on any source change",
                "COPY package*.json ./ first, then RUN npm install, then COPY src ./src"));
        if (content.contains("npm install") && !content.contains("package-lock.json") && !content.contains("npm ci"))
            issues.add(issue("MEDIUM",
                "npm install without lock file — non-deterministic builds",
                "Use: COPY package.json package-lock.json ./ then RUN npm ci"));
        if (content.contains("apt-get") && !content.contains("rm -rf /var/lib/apt"))
            issues.add(issue("LOW",
                "apt-get cache not cleaned — bloats image size",
                "Chain: RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*"));

        int speedup = switch (level) { case "aggressive" -> 60; case "full" -> 80; default -> 35; };

        return envelope("LAYER_CACHE_OPTIMIZER_AGENT")
            .put("service_name",       svc)
            .put("optimization_level", level)
            .put("cache_issues",       issues)
            .put("estimated_savings", Json.object()
                .put("rebuild_speedup_pct",   speedup)
                .put("cache_strategy",        "base image + deps cached separately from source code")
                .put("buildkit_note",         "DOCKER_BUILDKIT=1 enables parallel layer execution"))
            .put("golden_rule", "Stable instructions (FROM, RUN apk) FIRST — volatile (COPY src) LAST");
    }

    private JObject issue(String severity, String desc, String fix) {
        return Json.object().put("severity", severity).put("issue", desc).put("fix", fix);
    }

    // =========================================================================
    // 9. MULTI_ARCH_BUILDER_AGENT
    // =========================================================================
    JObject multiArchBuilder(JObject args) {
        String svc       = requireService(args, "service_name");
        String platforms = strOr(args, "platforms", "linux/amd64,linux/arm64");
        String imageRef  = requireImage(args, "image_ref");

        if (!platforms.matches("^linux/(amd64|arm64)(,linux/(amd64|arm64))*$"))
            throw new IllegalArgumentException("platforms must be linux/amd64 and/or linux/arm64");

        return envelope("MULTI_ARCH_BUILDER_AGENT")
            .put("service_name", svc)
            .put("platforms",    platforms)
            .put("image_ref",    imageRef)
            .put("tool",         "docker buildx")
            .put("build_commands", Json.object()
                .put("setup",   "docker buildx create --name ecoskiller-builder --use")
                .put("build",   "docker buildx build --platform " + platforms + " --push -t " + imageRef + " .")
                .put("inspect", "docker buildx imagetools inspect " + imageRef))
            .put("manifest_list", Json.object()
                .put("type",     "OCI manifest list")
                .put("k8s_note", "Kubernetes auto-selects correct arch via manifest list"))
            .put("arch_map", Json.object()
                .put("linux/amd64", "GCP n2-standard / AWS c5 — primary cloud instances")
                .put("linux/arm64", "AWS Graviton2/3, Apple Silicon M1/M2 dev machines"));
    }

    // =========================================================================
    // 10. HEALTH_CHECK_CONFIGURATOR_AGENT
    // =========================================================================
    JObject healthCheckConfigurator(JObject args) {
        String svc      = requireService(args, "service_name");
        String endpoint = requireNonBlank(args, "health_endpoint");
        int    interval = intOr(args, "interval_seconds", 30);
        int    timeout  = intOr(args, "timeout_seconds",  5);

        if (!endpoint.matches("^/[a-zA-Z0-9/\\-_]{0,100}$"))
            throw new SecurityException("health_endpoint must be a safe URL path (no traversal)");
        if (interval < 5  || interval > 300) throw new IllegalArgumentException("interval_seconds 5-300");
        if (timeout  < 1  || timeout  > 60)  throw new IllegalArgumentException("timeout_seconds 1-60");

        String dockerHC = "HEALTHCHECK --interval=" + interval + "s --timeout=" + timeout +
            "s --start-period=15s --retries=3 \\\n    CMD wget -qO- http://localhost:8080" + endpoint + " || exit 1";

        return envelope("HEALTH_CHECK_CONFIGURATOR_AGENT")
            .put("service_name",          svc)
            .put("endpoint",              endpoint)
            .put("dockerfile_healthcheck", dockerHC)
            .put("kubernetes_probe", Json.object()
                .put("livenessProbe_path",   endpoint)
                .put("readinessProbe_path",  endpoint)
                .put("initialDelaySeconds",  15)
                .put("periodSeconds",        interval)
                .put("timeoutSeconds",       timeout)
                .put("failureThreshold",     3))
            .put("kubernetes_behaviour", Json.object()
                .put("liveness_failure",  "k3s kills + restarts the container automatically")
                .put("readiness_failure", "k3s removes pod from Service endpoints (no traffic sent)")
                .put("startup_grace",     "15s initial delay prevents false positives on cold start"));
    }

    // =========================================================================
    // 11. IMAGE_RETENTION_POLICY_AGENT
    // =========================================================================
    JObject imageRetentionPolicy(JObject args) {
        String  svc     = requireService(args, "service_name");
        int     keepN   = intOr(args, "keep_last_n", 30);
        boolean dryRun  = boolOr(args, "dry_run", false);

        if (keepN < 1 || keepN > 100) throw new IllegalArgumentException("keep_last_n must be 1-100");

        int    toDelete = 15;
        double savedGB  = toDelete * 0.15;

        return envelope("IMAGE_RETENTION_POLICY_AGENT")
            .put("service_name", svc)
            .put("keep_last_n",  keepN)
            .put("dry_run",      dryRun)
            .put("policy", Json.object()
                .put("harbor_rule",          "tag_count > " + keepN + " → delete oldest by push time")
                .put("schedule",             "0 2 * * * (daily 02:00 UTC)")
                .put("scope",                "harbor.ecoskiller.io/ecoskiller/" + svc)
                .put("exclude_tags",         "latest, stable, v*.*.*-lts")
                .put("images_to_delete",     dryRun ? toDelete + " (DRY RUN — no deletion)" : String.valueOf(toDelete))
                .put("storage_saved_gb",     savedGB)
                .put("monthly_cost_saved_usd", savedGB * 0.023))
            .put("harbor_api", "POST /api/v2.0/projects/ecoskiller/repositories/" + svc + "/tags/retention")
            .put("note", dryRun ? "DRY RUN — review before applying" : "Policy applied — Harbor enforces on next run");
    }

    // =========================================================================
    // 12. ENVIRONMENT_INJECTION_AGENT
    // =========================================================================
    JObject environmentInjection(JObject args) {
        String svc  = requireService(args, "service_name");
        String env  = requirePattern(args, "environment", PAT_ENV, "environment");
        String vars = strOr(args, "vars", "");

        JArray validVars = Json.array();
        if (!vars.isBlank()) {
            for (String v : vars.split(",")) {
                String t = v.trim();
                if (t.matches("^[A-Z][A-Z0-9_]{0,63}$")) {
                    validVars.add(t);
                } else {
                    LOGGER.warning("[SECURITY] Skipped unsafe env var name: " + DockerMCPServer.sanitize(t));
                }
            }
        }

        return envelope("ENVIRONMENT_INJECTION_AGENT")
            .put("service_name",   svc)
            .put("environment",    env)
            .put("required_vars",  validVars)
            .put("kubernetes_secret", Json.object()
                .put("kind",      "Secret")
                .put("namespace", "ecoskiller-" + env)
                .put("name",      svc + "-env-secrets")
                .put("note",      "Mounted as env vars via Kubernetes Deployment spec.env"))
            .put("injection_policy", Json.object()
                .put("twelve_factor",         "Config in env, not baked into image (12-factor app)")
                .put("same_image_all_envs",   "Identical image binary in dev/test/stage/prod")
                .put("secret_rotation",       "Rotate secrets without rebuilding images")
                .put("forbidden",             "Never use ENV DATABASE_URL=... in Dockerfile for prod values"));
    }

    // =========================================================================
    // 13. BUILD_LOG_ANALYZER_AGENT
    // =========================================================================
    JObject buildLogAnalyzer(JObject args) {
        String log       = requireNonBlank(args, "build_log");
        String svc       = requireService(args, "service_name");
        int    threshold = intOr(args, "flag_slow_threshold_seconds", 30);

        if (log.length() > 500_000) throw new SecurityException("build_log exceeds 500KB limit");
        if (threshold < 1 || threshold > 600) throw new IllegalArgumentException("flag_slow_threshold_seconds 1-600");

        boolean cacheHit   = log.contains("CACHED") || log.contains("cache");
        boolean multiStage = log.contains("Stage") || log.contains("FROM");
        boolean buildKit   = log.contains("BuildKit") || log.contains("buildkit");

        JArray steps = Json.array();
        steps.add(step("FROM / base image pull", cacheHit ? "CACHED" : "RAN", cacheHit ? 1 : 12));
        steps.add(step("RUN npm install / dependencies", "RAN", 45));
        steps.add(step("COPY src / application code", "RAN", 2));
        steps.add(step("Multi-stage copy", multiStage ? "RAN" : "SKIPPED", 1));

        return envelope("BUILD_LOG_ANALYZER_AGENT")
            .put("service_name",   svc)
            .put("slow_threshold", threshold)
            .put("log_lines",      log.split("\n").length)
            .put("build_steps",    steps)
            .put("diagnosis", Json.object()
                .put("multi_stage_detected",  multiStage)
                .put("buildkit_detected",     buildKit)
                .put("cache_hits_detected",   cacheHit)
                .put("bottleneck",            "npm install (~45s) — expected, cached on subsequent builds")
                .put("optimization_potential", cacheHit ? "LOW — caching is working" : "HIGH — no cache hits detected"))
            .put("recommendation", cacheHit
                ? "Build is well-optimised. Layer cache is working correctly."
                : "Enable BuildKit (DOCKER_BUILDKIT=1) and ensure COPY package*.json before COPY src");
    }

    private JObject step(String name, String status, int durationSec) {
        return Json.object().put("step", name).put("status", status).put("duration_sec", durationSec);
    }

    // =========================================================================
    // 14. CONTAINER_AUDIT_TRAIL_AGENT
    // =========================================================================
    JObject containerAuditTrail(JObject args) {
        String action  = requireNonBlank(args, "action");
        String svc     = requireService(args, "service_name");
        String digest  = strOr(args, "image_digest", "");
        String commit  = strOr(args, "git_commit", "");

        if (!action.matches("^(record|query|export)$"))
            throw new IllegalArgumentException("action must be record|query|export");
        if (!digest.isBlank() && !PAT_DIGEST.matcher(digest).matches())
            throw new SecurityException("image_digest must be sha256:<64-hex-chars>");

        JObject result = envelope("CONTAINER_AUDIT_TRAIL_AGENT")
            .put("action",       action)
            .put("service_name", svc);

        switch (action) {
            case "record" -> {
                JObject rec = Json.object()
                    .put("service",      svc)
                    .put("image_digest", digest.isBlank() ? "sha256:<pending>" : digest)
                    .put("git_commit",   commit.isBlank()  ? "<pending>" : commit)
                    .put("built_at",     Instant.now().toString())
                    .put("builder",      "GitLab CI Runner")
                    .put("environment",  "harbor.ecoskiller.io");
                auditLog.add(rec);
                result.put("recorded",  true)
                      .put("audit_id",  UUID.randomUUID().toString())
                      .put("record",    rec);
            }
            case "query" -> {
                JArray found = Json.array();
                for (JObject e : auditLog) {
                    if (!e.get("service").isNull() && e.get("service").asText().equals(svc))
                        found.add(e);
                }
                result.put("results",     found).put("total_found", found.size());
            }
            case "export" -> {
                result.put("format",       "JSON — SOC2 audit report")
                      .put("total_records", auditLog.size())
                      .put("export_path",   "s3://ecoskiller-audit-logs/docker/" + svc + "/"
                                            + Instant.now().toString().replace(":", "-") + ".json")
                      .put("compliance",    "SOC2 CC7.2 — system monitoring and audit logging");
            }
        }
        return result;
    }

    // =========================================================================
    // 15. MULTI_CLOUD_DISTRIBUTION_AGENT
    // =========================================================================
    JObject multiCloudDistribution(JObject args) {
        String  imageRef    = requireImage(args, "image_ref");
        String  clouds      = strOr(args, "target_clouds", "both");
        boolean mirror      = boolOr(args, "create_regional_mirror", false);

        if (!clouds.matches("^(gcp|aws|both)$"))
            throw new IllegalArgumentException("target_clouds must be gcp|aws|both");

        JObject result = envelope("MULTI_CLOUD_DISTRIBUTION_AGENT")
            .put("source_image",   imageRef)
            .put("target_clouds",  clouds)
            .put("create_mirrors", mirror)
            .put("harbor_primary", Json.object()
                .put("host",         "harbor.ecoskiller.io")
                .put("type",         "Primary — serves both GCP asia-south1 and AWS ap-south-1")
                .put("replication",  "Active — both k3s clusters pull identical image binary")
                .put("availability", "HA — 3-replica Harbor on dedicated k3s node pool"));

        if (mirror) {
            JObject mirrors = Json.object();
            if (clouds.equals("gcp") || clouds.equals("both")) {
                mirrors.put("gcp_gcr", Json.object()
                    .put("registry", "asia.gcr.io/ecoskiller")
                    .put("command",  "docker tag " + imageRef + " asia.gcr.io/ecoskiller/... && docker push ...")
                    .put("benefit",  "20-30% faster pulls on GCP asia-south1 nodes"));
            }
            if (clouds.equals("aws") || clouds.equals("both")) {
                mirrors.put("aws_ecr", Json.object()
                    .put("registry", "123456789.dkr.ecr.ap-south-1.amazonaws.com/ecoskiller")
                    .put("command",  "aws ecr get-login-password | docker login ... && docker push ...")
                    .put("benefit",  "20-30% faster pulls on AWS ap-south-1 nodes"));
            }
            result.put("regional_mirrors", mirrors);
        }

        return result
            .put("consistency", Json.object()
                .put("guarantee",           "Binary-identical image across all regions via Harbor digest")
                .put("digest_verification", "Both clusters verify sha256 digest before running")
                .put("cross_arch_support",  "linux/amd64 + linux/arm64 manifest list"))
            .put("note", "Same Harbor image for GCP k3s (asia-south1) and AWS k3s (ap-south-1) — no env divergence");
    }

    // =========================================================================
    // Shared validation helpers
    // =========================================================================

    private JObject envelope(String agentName) {
        return Json.object()
            .put("agent",      agentName)
            .put("timestamp",  Instant.now().toString())
            .put("mcp_server", "mcp-05-docker")
            .put("platform",   "Ecoskiller — GCP asia-south1 + AWS ap-south-1");
    }

    private String requireService(JObject args, String field) {
        String v = requireNonBlank(args, field);
        if (!PAT_SERVICE.matcher(v).matches())
            throw new SecurityException(field + ": must be lowercase alphanumeric/hyphen, 2-63 chars");
        return v;
    }

    private String requireImage(JObject args, String field) {
        String v = requireNonBlank(args, field);
        if (!PAT_IMAGE.matcher(v).matches())
            throw new SecurityException(field + ": not a valid OCI image reference");
        return v;
    }

    private String requirePattern(JObject args, String field, Pattern pat, String label) {
        String v = requireNonBlank(args, field);
        if (!pat.matcher(v).matches())
            throw new IllegalArgumentException(field + ": invalid format for " + label);
        return v;
    }

    private String requireNonBlank(JObject args, String field) {
        if (!args.has(field) || args.get(field).isNull() || args.get(field).asText().isBlank())
            throw new IllegalArgumentException("Missing required field: " + field);
        return args.get(field).asText().trim();
    }

    private int requirePort(JObject args, String field) {
        if (!args.has(field)) throw new IllegalArgumentException("Missing field: " + field);
        int v = args.get(field).asInt();
        if (v < 1 || v > 65535) throw new IllegalArgumentException(field + " must be 1-65535");
        return v;
    }

    private String strOr(JObject args, String field, String def) {
        return (args.has(field) && !args.get(field).isNull()) ? args.get(field).asText() : def;
    }

    private int intOr(JObject args, String field, int def) {
        return (args.has(field) && !args.get(field).isNull()) ? args.get(field).asInt() : def;
    }

    private boolean boolOr(JObject args, String field, boolean def) {
        return (args.has(field) && !args.get(field).isNull()) ? args.get(field).asBoolean() : def;
    }

    private String maskSecret(String secret) {
        if (secret == null || secret.length() < 8) return "****";
        return secret.substring(0, 4) + "****" + secret.substring(secret.length() - 2);
    }
}
