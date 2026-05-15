import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.regex.*;
import java.util.logging.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * Ecoskiller | mcp-harbor-service
 * CAT-05 — Container Registry  |  Custom Build  |  Priority: MEDIUM
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 12 Agents:
 *  01. image_list            — List images in ecoskiller/* project
 *  02. image_push_status     — Check latest SHA tags / push timestamps
 *  03. image_pull_validate   — Validate a tag exists before Helm deploy
 *  04. tag_retention_status  — Inspect retention policy; trigger GC
 *  05. vulnerability_report  — Trivy scan results per image
 *  06. robot_account_manage  — Manage Harbor robot accounts
 *  07. project_quota_check   — Storage quota for ecoskiller project
 *  08. webhook_manage        — Configure Harbor webhooks
 *  09. image_tag_promote     — Promote SHA → env-latest or semver tag
 *  10. registry_health       — Harbor /health + Prometheus metrics
 *  11. proxy_cache_manage    — DockerHub/GHCR proxy cache rules
 *  12. audit_log_query       — Push/pull event audit logs
 *
 * Requirements : Java 17+  (no external packages)
 * Transport    : stdio  (stdin/stdout  JSON-RPC 2.0)
 * MCP Version  : 2024-11-05
 *
 * Run:
 *   javac HarborMcpServer.java && java HarborMcpServer
 *
 * Env vars (required):
 *   HARBOR_URL      https://harbor.ecoskiller.internal
 *   HARBOR_USER     robot$gitlab-ci-push
 *   HARBOR_PASSWORD <secret — stored as Kubernetes Secret in ops namespace>
 *
 * Env vars (optional):
 *   HARBOR_PROJECT          default: ecoskiller
 *   HARBOR_API_TIMEOUT_MS   default: 10000
 *   HARBOR_TLS_VERIFY       default: true  (set "false" for local dev only)
 *   HARBOR_MCP_AUDIT_LOG    default: /var/log/ecoskiller/harbor-mcp-audit.log
 */
public class HarborMcpServer {

    // ── Constants ─────────────────────────────────────────────────────────
    static final String MCP_VERSION  = "2024-11-05";
    static final String SERVER_NAME  = "mcp-harbor-service";
    static final String SERVER_VER   = "1.0.0";
    static final String REGISTRY     = "harbor.ecoskiller.internal";
    static final int    RETAIN_DAYS  = 90;
    static final int    RETAIN_COUNT = 10;
    static final String[] ENV_TAGS   = {"dev-latest","test-latest","stage-latest","prod-latest"};

    // ── Security patterns ─────────────────────────────────────────────────
    static final Pattern P_TOOL      = Pattern.compile("^[a-z][a-z0-9_]{0,63}$");
    static final Pattern P_SHELL     = Pattern.compile("[;&|`$<>(){}!\\\\]");
    static final Pattern P_TRAVERSAL = Pattern.compile("\\.\\.");
    static final Pattern P_SHA_TAG   = Pattern.compile(
        "^([a-f0-9]{7,40}|latest|dev-latest|test-latest|stage-latest|prod-latest|v\\d+\\.\\d+\\.\\d+)$");
    static final Pattern P_SERVICE   = Pattern.compile("^[a-z][a-z0-9\\-]{0,63}$");

    // ── Rate limiter (100 req/min) ─────────────────────────────────────────
    static final AtomicInteger reqCount   = new AtomicInteger(0);
    static volatile long        windowStart = System.currentTimeMillis();
    static final int            RATE_LIMIT = 100;

    // ── Audit log ─────────────────────────────────────────────────────────
    static PrintWriter auditWriter = null;
    static {
        String logPath = env("HARBOR_MCP_AUDIT_LOG", "/var/log/ecoskiller/harbor-mcp-audit.log");
        try {
            File logDir = new File(logPath).getParentFile();
            if (logDir != null && (logDir.exists() || logDir.mkdirs())) {
                auditWriter = new PrintWriter(new FileWriter(logPath, true), true);
            }
        } catch (Exception ignored) {}
    }

    // ═════════════════════════════════════════════════════════════════════
    // ENTRY POINT
    // ═════════════════════════════════════════════════════════════════════

    public static void main(String[] args) throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(System.in,  StandardCharsets.UTF_8));
        PrintWriter    out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(System.out, StandardCharsets.UTF_8)));
        audit("START", SERVER_NAME + " v" + SERVER_VER);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String resp = handle(line);
            if (resp != null) { out.println(resp); out.flush(); }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // JSON-RPC DISPATCHER
    // ═════════════════════════════════════════════════════════════════════

    static String handle(String raw) {
        Map<String,Object> req;
        try { req = parseObj(raw); }
        catch (Exception e) { return err(null, -32700, "Parse error: " + e.getMessage()); }

        String id     = strVal(req, "id");
        String method = strVal(req, "method");

        // Rate limit
        long now = System.currentTimeMillis();
        synchronized (HarborMcpServer.class) {
            if (now - windowStart >= 60_000L) { windowStart = now; reqCount.set(0); }
            if (reqCount.incrementAndGet() > RATE_LIMIT)
                return err(id, -32000, "Rate limit exceeded (100 req/min).");
        }

        audit("REQ", method + " id=" + id);

        Map<String,Object> params = objVal(req, "params");

        return switch (method != null ? method : "") {
            case "initialize"  -> handleInit(id, params);
            case "ping"        -> ok(id, obj());
            case "tools/list"  -> handleToolsList(id);
            case "tools/call"  -> handleToolCall(id, params);
            default            -> err(id, -32601, "Method not found: " + method);
        };
    }

    // ─── initialize ──────────────────────────────────────────────────────
    static String handleInit(String id, Map<String,Object> params) {
        return ok(id, obj(
            "protocolVersion", MCP_VERSION,
            "serverInfo",      obj("name", SERVER_NAME, "version", SERVER_VER),
            "capabilities",    obj("tools", obj())
        ));
    }

    // ─── tools/list ──────────────────────────────────────────────────────
    static String handleToolsList(String id) {
        List<Object> tools = new ArrayList<>();
        for (ToolDef t : TOOLS) tools.add(toolDef(t));
        return ok(id, obj("tools", tools));
    }

    // ─── tools/call ──────────────────────────────────────────────────────
    static String handleToolCall(String id, Map<String,Object> params) {
        String toolName = strVal(params, "name");
        Map<String,Object> args = objVal(params, "arguments");

        // Security validation
        try {
            validateToolName(toolName);
            validateArgs(args, 0);
        } catch (SecurityException se) {
            audit("SECURITY_VIOLATION", toolName + ": " + se.getMessage());
            return err(id, -32001, "Security violation: " + se.getMessage());
        }

        ToolDef tool = findTool(toolName);
        if (tool == null) return err(id, -32602, "Unknown tool: " + toolName);

        try {
            String result = tool.handler.execute(args);
            audit("TOOL_OK", toolName);
            return ok(id, obj(
                "content", List.of(obj("type","text","text",result)),
                "isError", false
            ));
        } catch (Exception e) {
            audit("TOOL_ERR", toolName + ": " + e.getMessage());
            return ok(id, obj(
                "content", List.of(obj("type","text","text","Error: " + e.getMessage())),
                "isError", true
            ));
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // TOOL REGISTRY
    // ═════════════════════════════════════════════════════════════════════

    interface ToolHandler { String execute(Map<String,Object> args) throws Exception; }

    record ToolDef(String name, String description, String schema, ToolHandler handler) {}

    static final ToolDef[] TOOLS = {
        new ToolDef("image_list",
            "List all container images in the ecoskiller/* Harbor project. Returns repository names, " +
            "artifact counts, pull counts, last push timestamps. " +
            "Registry: harbor.ecoskiller.internal  Project: ecoskiller",
            """
            {"type":"object","properties":{
              "page":{"type":"integer","description":"Page number (default 1)","default":1},
              "page_size":{"type":"integer","description":"Results per page (default 20, max 50)","default":20},
              "filter":{"type":"string","description":"Filter by service name substring"}
            }}""",
            HarborMcpServer::toolImageList),

        new ToolDef("image_push_status",
            "Check push status for an Ecoskiller service image. Returns all tags " +
            "(SHA, dev-latest/test-latest/stage-latest/prod-latest, semver), " +
            "push timestamp, digest, size, and Trivy scan status.",
            """
            {"type":"object","required":["service"],"properties":{
              "service":{"type":"string","description":"Service name e.g. auth-service, scoring-engine"},
              "limit":{"type":"integer","description":"Recent artifacts to return (default 5, max 20)","default":5}
            }}""",
            HarborMcpServer::toolImagePushStatus),

        new ToolDef("image_pull_validate",
            "Validate a specific image tag exists in Harbor before Helm deploy. " +
            "Checks SHA tags, env-latest, and semver tags. Returns digest, push time, size, Trivy pass/fail.",
            """
            {"type":"object","required":["service","tag"],"properties":{
              "service":{"type":"string","description":"Service name e.g. auth-service"},
              "tag":{"type":"string","description":"Tag: full SHA, short SHA, dev-latest, prod-latest, v1.2.3"}
            }}""",
            HarborMcpServer::toolImagePullValidate),

        new ToolDef("tag_retention_status",
            "Inspect and manage tag retention policy. Policy: retain last 10 SHAs per service per env, " +
            "auto-delete >90 days, protect env-latest and semver tags. " +
            "Actions: status, trigger_dry_run, trigger_gc, history.",
            """
            {"type":"object","properties":{
              "action":{"type":"string","enum":["status","trigger_dry_run","trigger_gc","history"],"default":"status"}
            }}""",
            HarborMcpServer::toolTagRetention),

        new ToolDef("vulnerability_report",
            "Fetch Trivy CVE scan report for an Ecoskiller image from Harbor. " +
            "Shows severity counts (CRITICAL/HIGH/MEDIUM/LOW), individual CVEs with fix versions, " +
            "and whether the CI Trivy gate (--exit-code 1, no CRITICAL/HIGH) would pass.",
            """
            {"type":"object","required":["service"],"properties":{
              "service":{"type":"string","description":"Service name e.g. auth-service"},
              "tag":{"type":"string","description":"Tag or digest (default: prod-latest)","default":"prod-latest"},
              "min_severity":{"type":"string","enum":["LOW","MEDIUM","HIGH","CRITICAL"],"default":"HIGH"}
            }}""",
            HarborMcpServer::toolVulnerabilityReport),

        new ToolDef("robot_account_manage",
            "Manage Harbor robot accounts. CI push: HARBOR_USER/HARBOR_PASSWORD (protected+masked GitLab CI vars). " +
            "k3s pull: imagePullSecret (read-only per namespace). " +
            "SECURITY: secrets returned once on creation — store immediately as K8s Secret or GitLab CI var.",
            """
            {"type":"object","required":["action"],"properties":{
              "action":{"type":"string","enum":["list","status","create","refresh"],"default":"list"},
              "robot_name":{"type":"string","description":"Robot account name (required for status/refresh)"},
              "permissions":{"type":"string","enum":["push","pull"],"description":"Permission level for create"},
              "expire_days":{"type":"integer","description":"Days until expiry (-1=never, default 365)","default":365}
            }}""",
            HarborMcpServer::toolRobotAccount),

        new ToolDef("project_quota_check",
            "Check storage quota and usage for the ecoskiller Harbor project. " +
            "Harbor image data is stored on Longhorn PVC (ops namespace, 3-replica). " +
            "Retention policy (90 days / last-10 SHAs) bounds storage growth.",
            """
            {"type":"object","properties":{}}""",
            HarborMcpServer::toolProjectQuota),

        new ToolDef("webhook_manage",
            "Configure Harbor webhooks for image push events. " +
            "Webhooks can notify GitLab CI (event-driven deploy trigger) or Mattermost #incidents. " +
            "Events: PUSH_ARTIFACT, DELETE_ARTIFACT, SCANNING_COMPLETED. Actions: list, create, delete, test.",
            """
            {"type":"object","required":["action"],"properties":{
              "action":{"type":"string","enum":["list","create","delete","test"],"default":"list"},
              "target_url":{"type":"string","description":"Webhook target URL (required for create)"},
              "name":{"type":"string","description":"Webhook name (required for create)"},
              "webhook_id":{"type":"integer","description":"Webhook policy ID (required for delete/test)"}
            }}""",
            HarborMcpServer::toolWebhook),

        new ToolDef("image_tag_promote",
            "Promote a SHA-tagged image to env-latest or semver tag. " +
            "Mirrors GitLab CI tag promotion: SHA → dev-latest/test-latest/stage-latest/prod-latest. " +
            "Also creates v1.2.3 semver tags for GitLab Releases.",
            """
            {"type":"object","required":["service","sha_tag","target_tag"],"properties":{
              "service":{"type":"string","description":"Service name e.g. auth-service"},
              "sha_tag":{"type":"string","description":"Source git commit SHA tag"},
              "target_tag":{"type":"string","description":"Target: dev-latest, prod-latest, v1.2.3"}
            }}""",
            HarborMcpServer::toolImageTagPromote),

        new ToolDef("registry_health",
            "Check Harbor registry health and Prometheus metrics. " +
            "/health confirms all components (core, jobservice, registry, database, redis, storage). " +
            "Prometheus metrics: registry_storage_used_bytes, request rates, latency histograms.",
            """
            {"type":"object","properties":{
              "include":{"type":"string","enum":["health","metrics","both"],"default":"both"}
            }}""",
            HarborMcpServer::toolRegistryHealth),

        new ToolDef("proxy_cache_manage",
            "Manage Harbor proxy cache for third-party registries (DockerHub, GHCR). " +
            "Caches infrastructure images (prom/prometheus, grafana/grafana, kafka, redis, postgres) " +
            "locally — eliminates DockerHub rate limits and external registry dependency. " +
            "Actions: list, create, delete.",
            """
            {"type":"object","required":["action"],"properties":{
              "action":{"type":"string","enum":["list","create","delete"],"default":"list"},
              "source_registry":{"type":"string","description":"Source registry URL (required for create)"},
              "cache_name":{"type":"string","description":"Cache project name in Harbor (required for create)"},
              "cache_id":{"type":"integer","description":"Cache project ID (required for delete)"}
            }}""",
            HarborMcpServer::toolProxyCache),

        new ToolDef("audit_log_query",
            "Query Harbor audit logs for image push, pull, delete and user events. " +
            "Compliance-grade audit trail: every operation linked to robot account or user, " +
            "with timestamps and resource details. Filter by operation, service, username.",
            """
            {"type":"object","properties":{
              "operation":{"type":"string","enum":["push","pull","delete","create","update","all"],"default":"all"},
              "service":{"type":"string","description":"Filter by service name"},
              "username":{"type":"string","description":"Filter by robot account or username"},
              "limit":{"type":"integer","description":"Entries to return (default 20, max 100)","default":20}
            }}""",
            HarborMcpServer::toolAuditLog)
    };

    static ToolDef findTool(String name) {
        if (name == null) return null;
        for (ToolDef t : TOOLS) if (t.name().equals(name)) return t;
        return null;
    }

    static Map<String,Object> toolDef(ToolDef t) {
        return obj("name", t.name(), "description", t.description(),
                   "inputSchema", parseObj(t.schema()));
    }

    // ═════════════════════════════════════════════════════════════════════
    // TOOL HANDLERS
    // ═════════════════════════════════════════════════════════════════════

    // ── 01. image_list ───────────────────────────────────────────────────
    static String toolImageList(Map<String,Object> args) throws Exception {
        int    page     = intVal(args, "page",      1);
        int    pageSize = intVal(args, "page_size", 20);
        String filter   = sanitize(strVal(args, "filter"));
        if (page < 1)      page = 1;
        if (pageSize < 1 || pageSize > 50) pageSize = 20;

        String path = "/projects/" + project() + "/repositories?page=" + page + "&page_size=" + pageSize;
        HarborResp resp = harborGet(path);
        resp.assertOk("image_list");

        List<Object> repos = new ArrayList<>();
        for (Map<String,Object> r : parseArr(resp.body)) {
            String name = strVal(r, "name");
            if (!filter.isEmpty() && !name.toLowerCase().contains(filter.toLowerCase())) continue;
            repos.add(obj(
                "name",           name,
                "artifact_count", r.getOrDefault("artifact_count", 0),
                "pull_count",     r.getOrDefault("pull_count",     0),
                "update_time",    strVal(r, "update_time"),
                "description",    strVal(r, "description")
            ));
        }

        return toJson(obj(
            "project",   project(),
            "registry",  REGISTRY,
            "page",      page,
            "page_size", pageSize,
            "count",     repos.size(),
            "filter",    filter.isEmpty() ? null : filter,
            "repositories", repos
        ));
    }

    // ── 02. image_push_status ─────────────────────────────────────────────
    static String toolImagePushStatus(Map<String,Object> args) throws Exception {
        String service = requireService(args);
        int    limit   = Math.min(intVal(args, "limit", 5), 20);

        String encodedRepo = (project() + "/" + service).replace("/", "%2F");
        String path = "/projects/" + project() + "/repositories/" + encodedRepo +
                      "/artifacts?with_tag=true&with_scan_overview=true&page_size=" + limit;

        HarborResp resp = harborGet(path);
        resp.assertOk("image_push_status");

        List<Object> artifacts = new ArrayList<>();
        List<Map<String,Object>> raw = parseArr(resp.body);
        Map<String,Boolean> envTagMap = new LinkedHashMap<>();
        for (String et : ENV_TAGS) envTagMap.put(et, false);

        for (Map<String,Object> a : raw) {
            List<String> tags = new ArrayList<>();
            for (Map<String,Object> tag : parseArr(toJson(a.getOrDefault("tags", List.of())))) {
                String tn = strVal(tag, "name");
                tags.add(tn);
                if (envTagMap.containsKey(tn)) envTagMap.put(tn, true);
            }
            artifacts.add(obj(
                "digest",     strVal(a, "digest"),
                "push_time",  strVal(a, "push_time"),
                "size_mb",    String.format("%.2f", toLong(a.getOrDefault("size", 0L)) / 1_048_576.0),
                "tags",       tags
            ));
        }

        return toJson(obj(
            "service",         service,
            "full_image",      REGISTRY + "/" + project() + "/" + service,
            "artifact_count",  artifacts.size(),
            "artifacts",       artifacts,
            "env_latest_tags", envTagMap
        ));
    }

    // ── 03. image_pull_validate ──────────────────────────────────────────
    static String toolImagePullValidate(Map<String,Object> args) throws Exception {
        String service = requireService(args);
        String tag     = requireTag(args, "tag");

        String encodedRepo = (project() + "/" + service).replace("/", "%2F");
        String path = "/projects/" + project() + "/repositories/" + encodedRepo +
                      "/artifacts/" + tag + "?with_tag=true&with_scan_overview=true";

        HarborResp resp = harborGet(path);
        String imageRef = REGISTRY + "/" + project() + "/" + service + ":" + tag;

        if (resp.status == 404) {
            return toJson(obj(
                "valid",       false,
                "image_ref",   imageRef,
                "error",       "Tag not found. Confirm Trivy scan passed and push:harbor CI stage completed.",
                "helm_safe",   false
            ));
        }
        resp.assertOk("image_pull_validate");

        Map<String,Object> a = parseObj(resp.body);
        boolean trivyOk = trivyPassed(a);

        Map<String,Object> result = obj(
            "valid",        true,
            "image_ref",    imageRef,
            "digest",       strVal(a, "digest"),
            "push_time",    strVal(a, "push_time"),
            "size_mb",      String.format("%.2f", toLong(a.getOrDefault("size", 0L)) / 1_048_576.0),
            "trivy_passed", trivyOk,
            "helm_safe",    trivyOk,
            "pull_policy",  "Always",
            "helm_set_arg", "--set image.repository=" + REGISTRY + "/" + project() + "/" + service +
                            " --set image.tag=" + tag
        );
        if (!trivyOk) result.put("warning",
            "Image exists but Trivy scan found CRITICAL/HIGH CVEs. " +
            "Do NOT deploy to stage/prod. Use vulnerability_report tool for details.");
        return toJson(result);
    }

    // ── 04. tag_retention_status ─────────────────────────────────────────
    static String toolTagRetention(Map<String,Object> args) throws Exception {
        String action = strVal(args, "action");
        if (action == null || action.isBlank()) action = "status";

        Map<String,Object> result = obj(
            "project",         project(),
            "registry",        REGISTRY,
            "retention_days",  RETAIN_DAYS,
            "retention_count", RETAIN_COUNT
        );

        switch (action) {
            case "status" -> {
                HarborResp r = harborGet("/projects/" + project());
                r.assertOk("tag_retention/project");
                Map<String,Object> proj = parseObj(r.body);
                result.put("project_id",  proj.getOrDefault("id",         0));
                result.put("repo_count",  proj.getOrDefault("repo_count", 0));
                result.put("policy", obj(
                    "rule_1", "Retain last " + RETAIN_COUNT + " SHA-tagged images per service per environment",
                    "rule_2", "Auto-delete images older than " + RETAIN_DAYS + " days",
                    "protected_tags", "prod-latest, stage-latest, test-latest, dev-latest, v{semver} — retained indefinitely",
                    "gc_trigger",     "Automatic after retention execution",
                    "config_location","Harbor UI → Projects → ecoskiller → Tag Retention"
                ));
            }
            case "trigger_dry_run" -> {
                String retId = getRetentionId();
                if (retId == null) { result.put("error","No retention policy configured. Set it in Harbor UI first."); break; }
                HarborResp r = harborPost("/retentions/" + retId + "/executions", "{\"dry_run\":true}");
                r.assertOk("tag_retention/dry_run");
                result.put("dry_run_triggered", true);
                result.put("note", "Dry run started. No images deleted. Check Harbor UI → Tag Retention → Executions.");
            }
            case "trigger_gc" -> {
                HarborResp r = harborPost("/system/gc/schedule",
                    "{\"parameters\":{\"delete_untagged\":true,\"workers\":1},\"schedule\":{\"type\":\"Manual\"}}");
                r.assertOk("tag_retention/gc");
                result.put("gc_triggered", true);
                result.put("note", "GC started. Orphaned layers will be deleted from Longhorn PVC. " +
                                   "Monitor: Harbor UI → Administration → Garbage Collection.");
            }
            case "history" -> {
                String retId = getRetentionId();
                if (retId == null) { result.put("error","No retention policy found."); break; }
                HarborResp r = harborGet("/retentions/" + retId + "/executions?page=1&page_size=10");
                r.assertOk("tag_retention/history");
                List<Object> hist = new ArrayList<>();
                for (Map<String,Object> e : parseArr(r.body)) {
                    hist.add(obj("id",strVal(e,"id"),"status",strVal(e,"status"),
                                 "start_time",strVal(e,"start_time"),"dry_run",e.getOrDefault("dry_run",false)));
                }
                result.put("execution_history", hist);
            }
            default -> result.put("error", "Unknown action: " + action);
        }
        return toJson(result);
    }

    // ── 05. vulnerability_report ─────────────────────────────────────────
    static String toolVulnerabilityReport(Map<String,Object> args) throws Exception {
        String service     = requireService(args);
        String tag         = sanitize(strVal(args, "tag")); if (tag.isEmpty()) tag = "prod-latest";
        String minSeverity = strVal(args, "min_severity"); if (minSeverity == null) minSeverity = "HIGH";
        int    minLevel    = sevLevel(minSeverity);

        String encodedRepo = (project() + "/" + service).replace("/", "%2F");
        HarborResp resp = harborGet("/projects/" + project() + "/repositories/" + encodedRepo +
                                    "/artifacts/" + tag + "/additions/vulnerabilities");

        Map<String,Object> result = obj("service", service, "tag", tag, "min_severity", minSeverity,
            "image_ref", REGISTRY + "/" + project() + "/" + service + ":" + tag);

        if (resp.status == 404) {
            result.put("error","Artifact not found or scan unavailable. Ensure Harbor scanner is enabled.");
            return toJson(result);
        }
        resp.assertOk("vuln_report");

        Map<String,Object> reportRoot = parseObj(resp.body);
        // Harbor wraps the report under a content-type key
        Map<String,Object> report = null;
        for (Map.Entry<String,Object> e : reportRoot.entrySet()) {
            if (e.getKey().contains("vulnerability") && e.getValue() instanceof Map) {
                @SuppressWarnings("unchecked") Map<String,Object> m = (Map<String,Object>) e.getValue();
                report = m;
                break;
            }
        }
        if (report == null) {
            result.put("status","no_scan_data");
            result.put("note","No vulnerability report for this artifact. Enable Harbor scanner.");
            return toJson(result);
        }

        Map<String,Object> summary = objVal(report, "summary");
        int critical = intVal(summary,"critical",0), high = intVal(summary,"high",0);
        result.put("scan_status",    strVal(report,"scan_status"));
        result.put("scanner",        "Trivy");
        result.put("generated_at",   strVal(report,"generated_at"));
        result.put("severity_counts", obj("critical",critical,"high",high,
            "medium",intVal(summary,"medium",0),"low",intVal(summary,"low",0),
            "fixable",intVal(summary,"fixable",0),"total",intVal(summary,"total",0)));
        result.put("ci_gate_passed", critical == 0 && high == 0);
        result.put("deploy_safe",    critical == 0 && high == 0);
        if (critical > 0 || high > 0)
            result.put("block_reason", critical + " CRITICAL + " + high +
                " HIGH CVEs. GitLab CI Trivy gate would block push. Update base image before deploy.");

        List<Object> vulns = new ArrayList<>();
        for (Map<String,Object> v : parseArr(toJson(report.getOrDefault("vulnerabilities", List.of())))) {
            if (sevLevel(strVal(v,"severity")) >= minLevel) {
                vulns.add(obj("id",strVal(v,"id"),"severity",strVal(v,"severity"),
                    "package",strVal(v,"package"),"version",strVal(v,"version"),
                    "fix_version",strVal(v,"fix_version"),
                    "description",truncate(strVal(v,"description"),200)));
            }
        }
        result.put("vulnerabilities", vulns);
        result.put("cve_count_shown", vulns.size());
        return toJson(result);
    }

    // ── 06. robot_account_manage ─────────────────────────────────────────
    static String toolRobotAccount(Map<String,Object> args) throws Exception {
        String action    = strVal(args,"action");    if(action==null) action="list";
        String robotName = sanitize(strVal(args,"robot_name"));
        String perms     = strVal(args,"permissions"); if(perms==null) perms="pull";
        int    expDays   = intVal(args,"expire_days",365);

        String basePath = "/projects/" + project() + "/robots";
        Map<String,Object> result = obj("project", project());

        switch (action) {
            case "list" -> {
                HarborResp r = harborGet(basePath + "?page=1&page_size=50");
                r.assertOk("robot_account/list");
                List<Object> list = new ArrayList<>();
                for (Map<String,Object> rob : parseArr(r.body)) {
                    list.add(obj("id",strVal(rob,"id"),"name",strVal(rob,"name"),
                        "disabled",rob.getOrDefault("disabled",false),
                        "expires_at",strVal(rob,"expires_at"),
                        "creation_time",strVal(rob,"creation_time")));
                }
                result.put("robot_accounts", list);
                result.put("count", list.size());
                result.put("note","CI push: HARBOR_USER/HARBOR_PASSWORD — protected+masked GitLab CI vars. " +
                                  "k3s pull: imagePullSecret per namespace.");
            }
            case "status" -> {
                if (robotName.isEmpty()) { result.put("error","robot_name required for status"); break; }
                HarborResp r = harborGet(basePath + "?page=1&page_size=50");
                r.assertOk("robot_account/status");
                boolean found = false;
                for (Map<String,Object> rob : parseArr(r.body)) {
                    String rn = strVal(rob,"name");
                    if (rn.equals(robotName) || rn.endsWith("+" + robotName)) {
                        long expEpoch = toLong(rob.getOrDefault("expires_at", -1L));
                        result.put("found",      true);
                        result.put("name",       rn);
                        result.put("disabled",   rob.getOrDefault("disabled",false));
                        if (expEpoch == -1) {
                            result.put("expires","never");
                        } else {
                            long daysLeft = (expEpoch - System.currentTimeMillis()/1000) / 86400;
                            result.put("days_until_expiry", daysLeft);
                            result.put("expired", daysLeft < 0);
                            if (daysLeft >= 0 && daysLeft < 30)
                                result.put("warning","Expires in " + daysLeft + " days. Rotate credentials soon.");
                        }
                        found = true; break;
                    }
                }
                if (!found) { result.put("found",false); result.put("error","Robot not found: " + robotName); }
            }
            case "create" -> {
                String name  = robotName.isEmpty() ? "ecoskiller-" + perms + "-robot" : robotName;
                long   expAt = expDays == -1 ? -1 : System.currentTimeMillis()/1000 + (long)expDays*86400;
                String accessJson = buildAccessJson(perms);
                String body = "{\"name\":\"" + name + "\",\"description\":\"" +
                              ("push".equals(perms)?"GitLab CI push account":"k3s read-only pull account") +
                              "\",\"expires_at\":" + expAt + ",\"permissions\":[" + accessJson + "]}";
                HarborResp r = harborPost(basePath, body);
                r.assertOk("robot_account/create");
                Map<String,Object> created = parseObj(r.body);
                result.put("created",    true);
                result.put("name",       strVal(created,"name"));
                result.put("id",         strVal(created,"id"));
                result.put("secret",     strVal(created,"secret"));
                result.put("IMPORTANT",  "push".equals(perms)
                    ? "Store as HARBOR_USER=<name> HARBOR_PASSWORD=<secret> in GitLab CI as protected+masked Variables"
                    : "kubectl create secret docker-registry harbor-pull-secret " +
                      "--docker-server=harbor.ecoskiller.internal --docker-username=<n> --docker-password=<secret> -n <ns>");
            }
            case "refresh" -> {
                if (robotName.isEmpty()) { result.put("error","robot_name required for refresh"); break; }
                // find ID
                HarborResp lr = harborGet(basePath + "?page=1&page_size=50");
                lr.assertOk("robot_account/refresh/list");
                String robotId = null;
                for (Map<String,Object> rob : parseArr(lr.body)) {
                    String rn = strVal(rob,"name");
                    if (rn.equals(robotName)||rn.endsWith("+"+robotName)) { robotId=strVal(rob,"id"); break; }
                }
                if (robotId == null) { result.put("error","Robot not found: " + robotName); break; }
                HarborResp r = harborPatch(basePath + "/" + robotId, "{}");
                r.assertOk("robot_account/refresh");
                Map<String,Object> refreshed = parseObj(r.body);
                result.put("refreshed", true);
                result.put("name",      strVal(refreshed,"name"));
                result.put("secret",    strVal(refreshed,"secret"));
                result.put("IMPORTANT", "Old credentials are INVALID. Update GitLab CI Variables and " +
                                        "Kubernetes imagePullSecret immediately to prevent deployment failures.");
            }
            default -> result.put("error","Unknown action: " + action);
        }
        return toJson(result);
    }

    // ── 07. project_quota_check ───────────────────────────────────────────
    static String toolProjectQuota(Map<String,Object> args) throws Exception {
        HarborResp pr = harborGet("/projects/" + project());
        pr.assertOk("quota/project");
        Map<String,Object> proj = parseObj(pr.body);
        String projectId = strVal(proj, "id");

        HarborResp qr = harborGet("/quotas?reference=project&reference_id=" + projectId);
        qr.assertOk("quota");

        Map<String,Object> result = obj("project", project(), "project_id", projectId,
            "storage_backend","Longhorn PVC — ops namespace, 3-replica replication",
            "retention_policy","Last " + RETAIN_COUNT + " SHAs/env + auto-delete >" + RETAIN_DAYS +
                               " days. Run tag_retention_status (action=trigger_gc) to reclaim space.");

        for (Map<String,Object> q : parseArr(qr.body)) {
            Map<String,Object> hard = objVal(q,"hard"), used = objVal(q,"used");
            long usedBytes  = toLong(used.getOrDefault("storage", 0L));
            long hardBytes  = toLong(hard.getOrDefault("storage", -1L));
            result.put("used_bytes", usedBytes);
            result.put("used_mb",    String.format("%.1f", usedBytes / 1_048_576.0));
            result.put("used_gb",    String.format("%.2f", usedBytes / 1_073_741_824.0));
            if (hardBytes == -1) {
                result.put("quota_limit","unlimited");
            } else {
                double pct = hardBytes > 0 ? (double)usedBytes/hardBytes*100 : 0;
                result.put("quota_gb",      String.format("%.2f", hardBytes / 1_073_741_824.0));
                result.put("usage_percent", String.format("%.1f%%", pct));
                if (pct > 80) result.put("alert","Usage above 80%. Trigger GC to free space.");
            }
            break; // only first quota entry
        }
        return toJson(result);
    }

    // ── 08. webhook_manage ───────────────────────────────────────────────
    static String toolWebhook(Map<String,Object> args) throws Exception {
        String action = strVal(args,"action"); if(action==null) action="list";
        String base   = "/projects/" + project() + "/webhook/policies";
        Map<String,Object> result = obj("project", project());

        switch (action) {
            case "list" -> {
                HarborResp r = harborGet(base);
                r.assertOk("webhook/list");
                List<Object> hooks = new ArrayList<>();
                for (Map<String,Object> h : parseArr(r.body)) {
                    List<String> targets = new ArrayList<>();
                    for (Map<String,Object> t : parseArr(toJson(h.getOrDefault("targets",List.of()))))
                        targets.add(strVal(t,"address"));
                    hooks.add(obj("id",strVal(h,"id"),"name",strVal(h,"name"),
                        "enabled",h.getOrDefault("enabled",false),"targets",targets));
                }
                result.put("webhooks", hooks); result.put("count", hooks.size());
            }
            case "create" -> {
                String url  = sanitize(strVal(args,"target_url"));
                String name = sanitize(strVal(args,"name"));
                if (url.isEmpty())  { result.put("error","target_url required"); break; }
                if (name.isEmpty()) name = "harbor-webhook";
                String body = "{\"name\":\""+name+"\",\"enabled\":true," +
                    "\"event_types\":[\"PUSH_ARTIFACT\",\"DELETE_ARTIFACT\",\"SCANNING_COMPLETED\"]," +
                    "\"targets\":[{\"address\":\""+url+"\",\"type\":\"http\",\"skip_cert_verify\":false}]}";
                HarborResp r = harborPost(base, body);
                r.assertOk("webhook/create");
                result.put("created",true); result.put("name",name); result.put("target",url);
            }
            case "delete" -> {
                int wid = intVal(args,"webhook_id",0);
                if (wid == 0) { result.put("error","webhook_id required"); break; }
                HarborResp r = harborDelete(base + "/" + wid);
                r.assertOk("webhook/delete");
                result.put("deleted",true); result.put("webhook_id",wid);
            }
            case "test" -> {
                int wid = intVal(args,"webhook_id",0);
                if (wid == 0) { result.put("error","webhook_id required"); break; }
                HarborResp r = harborPost(base + "/" + wid + "/executions", "{}");
                result.put("test_triggered", r.isOk()); result.put("webhook_id", wid);
            }
            default -> result.put("error","Unknown action: " + action);
        }
        return toJson(result);
    }

    // ── 09. image_tag_promote ────────────────────────────────────────────
    static String toolImageTagPromote(Map<String,Object> args) throws Exception {
        String service   = requireService(args);
        String shaTag    = requireTag(args, "sha_tag");
        String targetTag = requireTag(args, "target_tag");

        String encodedRepo = (project() + "/" + service).replace("/", "%2F");
        String path = "/projects/" + project() + "/repositories/" + encodedRepo +
                      "/artifacts/" + shaTag + "/tags";
        HarborResp r = harborPost(path, "{\"name\":\"" + targetTag + "\"}");

        String imageRef = REGISTRY + "/" + project() + "/" + service + ":" + targetTag;
        Map<String,Object> result = obj("service",service,"sha_tag",shaTag,"target_tag",targetTag,"image_ref",imageRef);

        if (r.status == 201 || r.status == 200) {
            result.put("promoted", true);
            result.put("note", "Tag '" + targetTag + "' now points to SHA " + shaTag +
                               ". Helm pullPolicy:Always will use it on next pod restart.");
            if ("prod-latest".equals(targetTag))
                result.put("rollback_cmd","helm upgrade --reuse-values --set image.tag=" + shaTag + " <release>");
        } else if (r.status == 409) {
            result.put("promoted", false);
            result.put("note","Tag already exists on this artifact (idempotent).");
        } else {
            result.put("promoted",false); result.put("http_status",r.status); result.put("error",r.body);
        }
        return toJson(result);
    }

    // ── 10. registry_health ───────────────────────────────────────────────
    static String toolRegistryHealth(Map<String,Object> args) throws Exception {
        String include = strVal(args,"include"); if(include==null) include="both";
        Map<String,Object> result = obj("registry",REGISTRY,"namespace","ops",
            "dr_posture","No failover needed — co-located in k3s cluster with pulling services",
            "grafana_dashboard","https://grafana.ecoskiller.internal/d/harbor",
            "prometheus_scrape","Prometheus scrapes /metrics every 30s from ops namespace");

        if ("health".equals(include) || "both".equals(include)) {
            HarborResp r = harborGet("/health");
            r.assertOk("registry_health");
            Map<String,Object> health = parseObj(r.body);
            String status = strVal(health,"status");
            result.put("overall_status", status);
            result.put("healthy", "healthy".equalsIgnoreCase(status));
            List<Object> comps = new ArrayList<>();
            for (Map<String,Object> c : parseArr(toJson(health.getOrDefault("components",List.of()))))
                comps.add(obj("name",strVal(c,"name"),"status",strVal(c,"status")));
            result.put("components", comps);
        }

        if ("metrics".equals(include) || "both".equals(include)) {
            HarborResp r = harborGet("/metrics");
            Map<String,Object> metrics = obj();
            if (r.isOk()) {
                metrics.put("storage_used_bytes",    extractMetric(r.body,"registry_storage_used_bytes"));
                metrics.put("http_requests_total",   extractMetric(r.body,"registry_http_requests_total"));
                metrics.put("request_duration_p99",  extractMetric(r.body,"registry_http_request_duration_seconds{quantile=\"0.99\"}"));
                metrics.put("note","Full metrics in Grafana — Harbor Storage & Health panel (ops namespace).");
            } else {
                metrics.put("error","Metrics unavailable (HTTP " + r.status + "). Enable /metrics in Harbor admin settings.");
            }
            result.put("prometheus_metrics", metrics);
        }
        return toJson(result);
    }

    // ── 11. proxy_cache_manage ───────────────────────────────────────────
    static String toolProxyCache(Map<String,Object> args) throws Exception {
        String action = strVal(args,"action"); if(action==null) action="list";
        Map<String,Object> result = obj("registry", REGISTRY);

        switch (action) {
            case "list" -> {
                HarborResp r = harborGet("/registries?page=1&page_size=50");
                r.assertOk("proxy_cache/list");
                List<Object> list = new ArrayList<>();
                for (Map<String,Object> reg : parseArr(r.body))
                    list.add(obj("id",strVal(reg,"id"),"name",strVal(reg,"name"),
                        "url",strVal(reg,"url"),"type",strVal(reg,"type"),"status",strVal(reg,"status")));
                result.put("proxy_cache_registries", list);
                result.put("count", list.size());
                result.put("benefit","Cached images eliminate DockerHub rate limits for infrastructure images (prom/prometheus, grafana, kafka, redis, postgres).");
            }
            case "create" -> {
                String src  = sanitize(strVal(args,"source_registry"));
                String name = sanitize(strVal(args,"cache_name"));
                if (src.isEmpty() || name.isEmpty()) { result.put("error","source_registry and cache_name required"); break; }
                String body = "{\"name\":\""+name+"\",\"url\":\""+src+"\",\"type\":\"docker-registry\",\"insecure\":false}";
                HarborResp r = harborPost("/registries", body);
                r.assertOk("proxy_cache/create");
                result.put("created",true); result.put("cache_name",name); result.put("source_registry",src);
                result.put("pull_usage","harbor.ecoskiller.internal/"+name+"/prom/prometheus:latest");
            }
            case "delete" -> {
                int cid = intVal(args,"cache_id",0);
                if (cid == 0) { result.put("error","cache_id required"); break; }
                HarborResp r = harborDelete("/registries/" + cid);
                r.assertOk("proxy_cache/delete");
                result.put("deleted",true); result.put("cache_id",cid);
            }
            default -> result.put("error","Unknown action: " + action);
        }
        return toJson(result);
    }

    // ── 12. audit_log_query ──────────────────────────────────────────────
    static String toolAuditLog(Map<String,Object> args) throws Exception {
        String operation = strVal(args,"operation"); if(operation==null) operation="all";
        String service   = sanitize(strVal(args,"service"));
        String username  = sanitize(strVal(args,"username"));
        int    limit     = Math.min(intVal(args,"limit",20), 100);

        StringBuilder q = new StringBuilder("?page=1&page_size=" + limit);
        if (!"all".equals(operation)) q.append("&q=operation%3D").append(operation);
        if (!service.isEmpty())  q.append("&q=resource~%3D").append(service);
        if (!username.isEmpty()) q.append("&q=username%3D").append(username);

        HarborResp r = harborGet("/audit-logs" + q);
        r.assertOk("audit_log");

        List<Object> entries = new ArrayList<>();
        for (Map<String,Object> log : parseArr(r.body))
            entries.add(obj("id",strVal(log,"id"),"username",strVal(log,"username"),
                "resource",strVal(log,"resource"),"resource_type",strVal(log,"resource_type"),
                "operation",strVal(log,"operation"),"op_time",strVal(log,"op_time")));

        return toJson(obj("project",project(),"filter_op",operation,"audit_logs",entries,"count",entries.size(),
            "compliance_note","Every push performed by GitLab CI robot account. " +
            "Each image push is linked to the CI pipeline run for compliance-grade traceability."));
    }

    // ═════════════════════════════════════════════════════════════════════
    // HARBOR HTTP CLIENT
    // ═════════════════════════════════════════════════════════════════════

    record HarborResp(int status, String body) {
        boolean isOk() { return status >= 200 && status < 300; }
        void assertOk(String ctx) {
            if (!isOk()) throw new RuntimeException(ctx + " failed — HTTP " + status + ": " + truncate(body,200));
        }
    }

    static HttpClient buildHttpClient() throws Exception {
        boolean verify = !"false".equalsIgnoreCase(env("HARBOR_TLS_VERIFY","true"));
        var builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(Integer.parseInt(env("HARBOR_API_TIMEOUT_MS","10000"))))
                .followRedirects(HttpClient.Redirect.NORMAL);
        if (!verify) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] c, String a) {}
                public void checkServerTrusted(X509Certificate[] c, String a) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }}, new java.security.SecureRandom());
            builder.sslContext(ctx);
            System.err.println("[HARBOR-MCP] WARNING: TLS verification DISABLED — dev only");
        }
        return builder.build();
    }

    static String basicAuth() {
        String creds = requireEnv("HARBOR_USER") + ":" + requireEnv("HARBOR_PASSWORD");
        return "Basic " + Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));
    }

    static String apiBase() {
        String url = requireEnv("HARBOR_URL");
        if (url.endsWith("/")) url = url.substring(0, url.length()-1);
        return url + "/api/v2.0";
    }

    static String project() { return env("HARBOR_PROJECT","ecoskiller"); }

    static HarborResp harborGet(String path) throws Exception {
        var req = HttpRequest.newBuilder().uri(URI.create(apiBase() + path))
                .header("Authorization", basicAuth()).header("Accept","application/json")
                .timeout(Duration.ofMillis(Integer.parseInt(env("HARBOR_API_TIMEOUT_MS","10000"))))
                .GET().build();
        return send(req);
    }

    static HarborResp harborPost(String path, String body) throws Exception {
        var req = HttpRequest.newBuilder().uri(URI.create(apiBase() + path))
                .header("Authorization", basicAuth()).header("Content-Type","application/json")
                .header("Accept","application/json")
                .timeout(Duration.ofMillis(Integer.parseInt(env("HARBOR_API_TIMEOUT_MS","10000"))))
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        return send(req);
    }

    static HarborResp harborPatch(String path, String body) throws Exception {
        var req = HttpRequest.newBuilder().uri(URI.create(apiBase() + path))
                .header("Authorization", basicAuth()).header("Content-Type","application/json")
                .header("Accept","application/json")
                .timeout(Duration.ofMillis(Integer.parseInt(env("HARBOR_API_TIMEOUT_MS","10000"))))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
        return send(req);
    }

    static HarborResp harborDelete(String path) throws Exception {
        var req = HttpRequest.newBuilder().uri(URI.create(apiBase() + path))
                .header("Authorization", basicAuth()).header("Accept","application/json")
                .timeout(Duration.ofMillis(Integer.parseInt(env("HARBOR_API_TIMEOUT_MS","10000"))))
                .DELETE().build();
        return send(req);
    }

    static HarborResp send(HttpRequest req) throws Exception {
        var resp = buildHttpClient().send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return new HarborResp(resp.statusCode(), resp.body());
    }

    // ═════════════════════════════════════════════════════════════════════
    // SECURITY
    // ═════════════════════════════════════════════════════════════════════

    static void validateToolName(String name) {
        if (name == null || name.isBlank()) throw new SecurityException("Tool name must not be blank");
        if (!P_TOOL.matcher(name).matches()) throw new SecurityException("Illegal tool name: " + name);
    }

    static void validateArgs(Map<String,Object> args, int depth) {
        if (args == null || depth > 5) return;
        if (depth > 5) throw new SecurityException("Arguments too deeply nested");
        for (Map.Entry<String,Object> e : args.entrySet()) {
            validateArgKey(e.getKey());
            validateArgValue(e.getValue(), depth);
        }
    }

    static void validateArgKey(String key) {
        if (key == null || key.isBlank()) throw new SecurityException("Arg key must not be blank");
        if (key.length() > 64) throw new SecurityException("Arg key too long: " + key);
        if (P_SHELL.matcher(key).find()) throw new SecurityException("Illegal char in arg key: " + key);
    }

    @SuppressWarnings("unchecked")
    static void validateArgValue(Object val, int depth) {
        if (val instanceof String s) {
            if (s.length() > 1024) throw new SecurityException("String argument exceeds 1024 chars");
            if (P_TRAVERSAL.matcher(s).find()) throw new SecurityException("Path traversal in arg: " + s);
            if (P_SHELL.matcher(s).find()) throw new SecurityException("Shell metachar in arg: " + s);
        } else if (val instanceof Map m) {
            validateArgs(m, depth + 1);
        } else if (val instanceof List<?> list) {
            if (list.size() > 50) throw new SecurityException("Array argument too large (max 50)");
            for (Object item : list) validateArgValue(item, depth + 1);
        } else if (val instanceof Number n) {
            long lv = n.longValue();
            if (lv < Integer.MIN_VALUE || lv > Integer.MAX_VALUE)
                throw new SecurityException("Numeric argument out of range");
        }
    }

    static String requireService(Map<String,Object> args) {
        String s = sanitize(strVal(args,"service"));
        if (s.isEmpty()) throw new IllegalArgumentException("'service' argument is required");
        if (!P_SERVICE.matcher(s).matches())
            throw new SecurityException("Invalid service name: " + s);
        return s;
    }

    static String requireTag(Map<String,Object> args, String key) {
        String t = sanitize(strVal(args,key));
        if (t.isEmpty()) throw new IllegalArgumentException("'" + key + "' argument is required");
        if (!P_SHA_TAG.matcher(t).matches())
            throw new SecurityException("Invalid tag format: " + t);
        return t;
    }

    static String sanitize(String s) {
        if (s == null) return "";
        return s.replaceAll("[^a-zA-Z0-9_./:@\\-]","").substring(0, Math.min(s.length(),256));
    }

    // ═════════════════════════════════════════════════════════════════════
    // MINIMAL SELF-CONTAINED JSON PARSER & BUILDER (no external deps)
    // ═════════════════════════════════════════════════════════════════════

    /** Parse a JSON object string into Map<String,Object>. */
    @SuppressWarnings("unchecked")
    static Map<String,Object> parseObj(String json) {
        if (json == null || json.isBlank()) return new LinkedHashMap<>();
        try {
            Object parsed = new JsonParser(json.trim()).parse();
            if (parsed instanceof Map m) return m;
            return new LinkedHashMap<>();
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    /** Parse a JSON array string into List<Map<String,Object>>. */
    @SuppressWarnings("unchecked")
    static List<Map<String,Object>> parseArr(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            Object parsed = new JsonParser(json.trim()).parse();
            if (parsed instanceof List<?> list) {
                List<Map<String,Object>> result = new ArrayList<>();
                for (Object item : list) {
                    if (item instanceof Map m) result.add(m);
                }
                return result;
            }
            return List.of();
        } catch (Exception e) {
            return List.of();
        }
    }

    /** Serialize an object graph to compact JSON. */
    @SuppressWarnings("unchecked")
    static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String s) return "\"" + jsonEscape(s) + "\"";
        if (obj instanceof Boolean || obj instanceof Integer || obj instanceof Long
                || obj instanceof Double) return obj.toString();
        if (obj instanceof Number n) return n.toString();
        if (obj instanceof Map m) {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Object key : m.keySet()) {
                Object val = m.get(key);
                if (val == null) continue; // omit null fields
                if (!first) sb.append(",");
                sb.append("\"").append(jsonEscape(key.toString())).append("\":").append(toJson(val));
                first = false;
            }
            return sb.append("}").toString();
        }
        if (obj instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            return sb.append("]").toString();
        }
        return "\"" + jsonEscape(obj.toString()) + "\"";
    }

    static String jsonEscape(String s) {
        if (s == null) return "";
        return s.replace("\\","\\\\").replace("\"","\\\"")
                .replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
    }

    /** Convenience: build a LinkedHashMap from alternating key/value pairs. */
    @SuppressWarnings({"unchecked","rawtypes"})
    static Map<String,Object> obj(Object... kvs) {
        Map<String,Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kvs.length; i += 2) {
            if (kvs[i] != null && kvs[i+1] != null) m.put(kvs[i].toString(), kvs[i+1]);
        }
        return m;
    }

    // Recursive descent JSON parser
    static class JsonParser {
        final String src;
        int pos;
        JsonParser(String src) { this.src = src; this.pos = 0; }

        Object parse() {
            skipWs();
            if (pos >= src.length()) return null;
            char c = src.charAt(pos);
            if (c == '{') return parseObject();
            if (c == '[') return parseArray();
            if (c == '"') return parseString();
            if (c == 't') { pos += 4; return Boolean.TRUE; }
            if (c == 'f') { pos += 5; return Boolean.FALSE; }
            if (c == 'n') { pos += 4; return null; }
            return parseNumber();
        }

        Map<String,Object> parseObject() {
            Map<String,Object> m = new LinkedHashMap<>();
            pos++; // {
            skipWs();
            if (pos < src.length() && src.charAt(pos) == '}') { pos++; return m; }
            while (pos < src.length()) {
                skipWs();
                String key = parseString();
                skipWs();
                if (pos < src.length() && src.charAt(pos) == ':') pos++;
                skipWs();
                Object val = parse();
                m.put(key, val);
                skipWs();
                if (pos < src.length() && src.charAt(pos) == ',') pos++;
                skipWs();
                if (pos < src.length() && src.charAt(pos) == '}') { pos++; break; }
            }
            return m;
        }

        List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            pos++; // [
            skipWs();
            if (pos < src.length() && src.charAt(pos) == ']') { pos++; return list; }
            while (pos < src.length()) {
                skipWs();
                list.add(parse());
                skipWs();
                if (pos < src.length() && src.charAt(pos) == ',') pos++;
                skipWs();
                if (pos < src.length() && src.charAt(pos) == ']') { pos++; break; }
            }
            return list;
        }

        String parseString() {
            if (pos < src.length() && src.charAt(pos) == '"') pos++;
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') break;
                if (c == '\\' && pos < src.length()) {
                    char esc = src.charAt(pos++);
                    switch (esc) {
                        case '"'  -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case '/'  -> sb.append('/');
                        case 'n'  -> sb.append('\n');
                        case 'r'  -> sb.append('\r');
                        case 't'  -> sb.append('\t');
                        case 'u'  -> {
                            if (pos + 4 <= src.length()) {
                                sb.append((char) Integer.parseInt(src.substring(pos, pos+4), 16));
                                pos += 4;
                            }
                        }
                        default   -> sb.append(esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        Object parseNumber() {
            int start = pos;
            while (pos < src.length() && "-0123456789.eE+".indexOf(src.charAt(pos)) >= 0) pos++;
            String num = src.substring(start, pos);
            try {
                if (num.contains(".") || num.contains("e") || num.contains("E"))
                    return Double.parseDouble(num);
                long lv = Long.parseLong(num);
                return (lv >= Integer.MIN_VALUE && lv <= Integer.MAX_VALUE) ? (int)lv : lv;
            } catch (NumberFormatException e) { return num; }
        }

        void skipWs() { while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++; }
    }

    // ═════════════════════════════════════════════════════════════════════
    // JSON-RPC RESPONSE BUILDERS
    // ═════════════════════════════════════════════════════════════════════

    static String ok(String id, Object result) {
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc","2.0");
        if (id != null) resp.put("id", id);
        resp.put("result", result);
        return toJson(resp);
    }

    static String err(String id, int code, String message) {
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("jsonrpc","2.0");
        if (id != null) resp.put("id", id);
        resp.put("error", obj("code", code, "message", message));
        return toJson(resp);
    }

    // ═════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════

    static String env(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }

    static String requireEnv(String key) {
        String v = System.getenv(key);
        if (v == null || v.isBlank())
            throw new IllegalStateException("Required env var not set: " + key +
                "\nSet it as a Kubernetes Secret in the ops namespace and mount as env var.");
        return v;
    }

    @SuppressWarnings("unchecked")
    static String strVal(Map<String,Object> m, String key) {
        if (m == null) return null;
        Object v = m.get(key);
        return v == null ? null : v.toString();
    }

    @SuppressWarnings("unchecked")
    static Map<String,Object> objVal(Map<String,Object> m, String key) {
        if (m == null) return new LinkedHashMap<>();
        Object v = m.get(key);
        if (v instanceof Map mv) return mv;
        return new LinkedHashMap<>();
    }

    static int intVal(Map<String,Object> m, String key, int def) {
        if (m == null) return def;
        Object v = m.get(key);
        if (v instanceof Number n) return n.intValue();
        if (v instanceof String s) { try { return Integer.parseInt(s); } catch (Exception ignored) {} }
        return def;
    }

    static long toLong(Object v) {
        if (v instanceof Number n) return n.longValue();
        if (v instanceof String s) { try { return Long.parseLong(s); } catch (Exception ignored) {} }
        return 0L;
    }

    @SuppressWarnings("unchecked")
    static String getRetentionId() {
        try {
            HarborResp r = harborGet("/projects/" + project() + "/metadatas");
            if (!r.isOk()) return null;
            Map<String,Object> meta = parseObj(r.body);
            String rid = strVal(meta,"retention_id");
            if (rid != null && !rid.isBlank()) return rid;
        } catch (Exception ignored) {}
        return null;
    }

    static boolean trivyPassed(Map<String,Object> artifact) {
        Object scanOverview = artifact.get("scan_overview");
        if (!(scanOverview instanceof Map)) return true;
        @SuppressWarnings("unchecked") Map<String,Object> soMap = (Map<String,Object>) scanOverview;
        for (Object v : soMap.values()) {
            if (v instanceof Map scanData) {
                @SuppressWarnings("unchecked") Map<String,Object> sd = (Map<String,Object>) scanData;
                String status = strVal(sd,"scan_status");
                Map<String,Object> summary = objVal(sd,"summary");
                int critical = intVal(summary,"critical",0), high = intVal(summary,"high",0);
                return "Success".equalsIgnoreCase(status) && critical == 0 && high == 0;
            }
        }
        return true;
    }

    static int sevLevel(String sev) {
        if (sev == null) return 0;
        return switch (sev.toUpperCase()) {
            case "CRITICAL" -> 4; case "HIGH" -> 3; case "MEDIUM" -> 2; case "LOW" -> 1; default -> 0;
        };
    }

    static String extractMetric(String body, String name) {
        if (body == null) return "N/A";
        for (String line : body.split("\n")) {
            if (line.startsWith(name+" ") || line.startsWith(name+"{")) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) return parts[parts.length-1];
            }
        }
        return "N/A";
    }

    static String buildAccessJson(String perms) {
        boolean push = "push".equals(perms);
        String actions = push
            ? "[\"pull\",\"push\",\"create\",\"delete\",\"update\"]"
            : "[\"pull\"]";
        return "{\"resource\":\"repository\",\"action\":" + actions + "}";
    }

    static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0,max) + "..." : s;
    }

    static void audit(String level, String detail) {
        String ts    = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        String clean = detail.replaceAll("[\\x00-\\x1F|]","_").substring(0,Math.min(detail.length(),256));
        String entry = ts + " | " + level + " | " + clean;
        System.err.println("[HARBOR-MCP] " + entry);
        if (auditWriter != null) auditWriter.println(entry);
    }
}
