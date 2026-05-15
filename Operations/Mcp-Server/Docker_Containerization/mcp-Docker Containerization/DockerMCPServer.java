package io.ecoskiller.mcp.docker;

import io.ecoskiller.mcp.docker.json.Json;
import io.ecoskiller.mcp.docker.json.Json.*;

import java.io.*;
import java.util.logging.*;

/**
 * Ecoskiller | CAT-05 — Docker Containerization MCP Server
 * =========================================================
 * Pure Java — ZERO external dependencies.
 * Transport : stdio  (JSON-RPC 2.0)
 * MCP Version: 2024-11-05
 * Security   : Input validation, sanitization, audit logging, size limits
 */
public class DockerMCPServer {

    static final String SERVER_NAME    = "mcp-05-docker";
    static final String SERVER_VERSION = "1.0.0";
    static final String MCP_VERSION    = "2024-11-05";
    static final Logger LOGGER         = Logger.getLogger(DockerMCPServer.class.getName());

    private static final DockerAgents AGENTS = new DockerAgents();

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) throws Exception {
        setupLogger();
        LOGGER.info("[MCP] " + SERVER_NAME + " v" + SERVER_VERSION + " starting on stdio");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,  "UTF-8"));
        PrintStream    writer = new PrintStream(System.out, true, "UTF-8");

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                String response = handleMessage(line);
                if (response != null) { writer.println(response); writer.flush(); }
            } catch (Exception e) {
                LOGGER.severe("[MCP] Fatal: " + e.getMessage());
                writer.println(errorResponse(null, -32700, "Parse error"));
                writer.flush();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    private static String handleMessage(String raw) {
        JValue req;
        try { req = Json.parse(raw); }
        catch (Exception e) { return errorResponse(null, -32700, "Parse error"); }

        if (!(req instanceof JObject obj))
            return errorResponse(null, -32600, "Invalid Request");
        if (!obj.has("jsonrpc") || !obj.has("method"))
            return errorResponse(null, -32600, "Missing jsonrpc or method");

        String method = obj.get("method").asText();
        JValue id     = obj.has("id") ? obj.get("id") : null;
        JValue params = obj.has("params") ? obj.get("params") : Json.object();

        LOGGER.info("[MCP] method=" + sanitize(method));

        return switch (method) {
            case "initialize"                -> handleInitialize(id);
            case "ping"                      -> successResponse(id, Json.object());
            case "tools/list"                -> handleToolsList(id);
            case "tools/call"                -> handleToolCall(id, params);
            case "notifications/initialized" -> null;
            default -> errorResponse(id, -32601, "Method not found: " + sanitize(method));
        };
    }

    // ── initialize ────────────────────────────────────────────────────────────
    private static String handleInitialize(JValue id) {
        JObject result = Json.object()
            .put("protocolVersion", MCP_VERSION)
            .put("capabilities",    Json.object()
                    .put("tools", Json.object().put("listChanged", false)))
            .put("serverInfo",      Json.object()
                    .put("name",    SERVER_NAME)
                    .put("version", SERVER_VERSION));
        return successResponse(id, result);
    }

    // ── tools/list ────────────────────────────────────────────────────────────
    private static String handleToolsList(JValue id) {
        JArray tools = Json.array();

        tools.add(tool("dockerfile_builder",
            "Generates secure multi-stage Dockerfile: non-root user (UID 1000), HEALTHCHECK, pinned Alpine base, " +
            "OCI provenance labels, BuildKit-optimised layers. Runtimes: node18/node20/java17/java21/python311.",
            schema(p("service_name","string","Microservice name e.g. scoring-engine"),
                   p("runtime","string","node18 | node20 | java17 | java21 | python311"),
                   p("port","integer","Exposed service port 1-65535"),
                   p("entry_command","string","Container start command e.g. npm start"))));

        tools.add(tool("image_build_pipeline",
            "Docker BuildKit CI pipeline: git-commit tag, registry layer cache, multi-arch build manifest.",
            schema(p("service_name","string","Service to build"),
                   p("git_commit","string","Git commit hash for tagging (min 7 hex chars)"),
                   p("registry_host","string","Harbor host e.g. harbor.ecoskiller.io"))));

        tools.add(tool("registry_push",
            "Push to Harbor with RBAC validation, Trivy auto-scan on push, content trust, retention policy.",
            schema(p("image_ref","string","Full image ref registry/service:tag"),
                   p("auth_token","string","Base64 registry credentials (masked in output)"),
                   p("retention_policy","string","Keep last N images: 10 | 20 | 30"))));

        tools.add(tool("vulnerability_scanner",
            "Trivy 0.48.x CVE scan — blocks deployment on CRITICAL/HIGH CVEs. Full report with fix recommendations.",
            schema(p("image_ref","string","Image to scan"),
                   p("severity_threshold","string","Block on: CRITICAL | HIGH | MEDIUM"),
                   p("ignore_unfixed","boolean","Skip CVEs with no available fix"))));

        tools.add(tool("sbom_generator",
            "Generate SBOM with syft: CycloneDX/SPDX/syft-json. Lists all deps, versions, licenses for SOC2.",
            schema(p("image_ref","string","Image reference to inspect"),
                   p("output_format","string","cyclonedx-json | spdx-json | syft-json"),
                   p("service_name","string","Service name for SBOM labeling"))));

        tools.add(tool("image_signing",
            "Sign images with Cosign (Sigstore). Kubernetes admission controller verifies signatures. Prevents MitM injection.",
            schema(p("image_ref","string","Image digest ref registry/service@sha256:..."),
                   p("key_id","string","Signing key identifier from Kubernetes secrets"))));

        tools.add(tool("base_image_manager",
            "Track Alpine base image CVEs, recommend upgrades, validate pinned versions, list affected services.",
            schema(p("current_base","string","Current base image e.g. node:18.15-alpine"),
                   p("check_updates","boolean","Fetch latest security patch info"),
                   p("services_affected","string","Comma-separated services using this base"))));

        tools.add(tool("layer_cache_optimizer",
            "Analyse Dockerfile layer order for BuildKit cache efficiency. Detects anti-patterns. Up to 80% build time reduction.",
            schema(p("dockerfile_content","string","Full Dockerfile content (max 50KB)"),
                   p("service_name","string","Service name for context"),
                   p("optimization_level","string","basic | aggressive | full"))));

        tools.add(tool("multi_arch_builder",
            "docker buildx for linux/amd64 + linux/arm64 OCI manifest lists. Kubernetes auto-selects correct arch.",
            schema(p("service_name","string","Service to build"),
                   p("platforms","string","Target platforms e.g. linux/amd64,linux/arm64"),
                   p("image_ref","string","Target Harbor image reference"))));

        tools.add(tool("health_check_configurator",
            "Generate Dockerfile HEALTHCHECK instruction + Kubernetes liveness/readiness probe YAML.",
            schema(p("service_name","string","Microservice name"),
                   p("health_endpoint","string","HTTP path e.g. /health"),
                   p("interval_seconds","integer","Check interval 5-300"),
                   p("timeout_seconds","integer","Probe timeout 1-60"))));

        tools.add(tool("image_retention_policy",
            "Harbor retention rules: keep last N images per service, delete older to save storage (~$100/TB/month).",
            schema(p("service_name","string","Service to apply retention to"),
                   p("keep_last_n","integer","Images to retain (1-100)"),
                   p("dry_run","boolean","Preview deletions without executing"))));

        tools.add(tool("environment_injection",
            "Validate 12-factor env var injection. No secrets baked in images. Generates Kubernetes Secret manifest.",
            schema(p("service_name","string","Microservice name"),
                   p("environment","string","dev | test | stage | prod"),
                   p("vars","string","Comma-separated required UPPER_CASE env var names"))));

        tools.add(tool("build_log_analyzer",
            "Parse Docker build logs to identify slow steps, missing cache hits, and BuildKit configuration issues.",
            schema(p("build_log","string","Raw Docker build log (max 500KB)"),
                   p("service_name","string","Service that was built"),
                   p("flag_slow_threshold_seconds","integer","Flag steps slower than N seconds"))));

        tools.add(tool("container_audit_trail",
            "SOC2 image provenance: record/query/export Dockerfile → git commit → image digest → build time trail.",
            schema(p("action","string","record | query | export"),
                   p("service_name","string","Service name"),
                   p("image_digest","string","sha256:<64-hex> image digest"),
                   p("git_commit","string","Source git commit hash"))));

        tools.add(tool("multi_cloud_distribution",
            "Harbor → GCP asia-south1 + AWS ap-south-1 image distribution. Optional regional GCR/ECR mirrors.",
            schema(p("image_ref","string","Source Harbor image reference"),
                   p("target_clouds","string","gcp | aws | both"),
                   p("create_regional_mirror","boolean","Mirror to GCR and ECR for faster regional pulls"))));

        return successResponse(null, Json.object().put("tools", tools));
    }

    // ── tools/call ────────────────────────────────────────────────────────────
    private static String handleToolCall(JValue id, JValue paramsVal) {
        if (!(paramsVal instanceof JObject params))
            return errorResponse(id, -32602, "params must be object");
        if (!params.has("name"))
            return errorResponse(id, -32602, "Missing tool name");

        String toolName = params.get("name").asText();
        if (!toolName.matches("[a-z_]{3,50}"))
            return errorResponse(id, -32602, "Invalid tool name format");

        JValue argsVal = params.has("arguments") ? params.get("arguments") : Json.object();
        if (!(argsVal instanceof JObject args))
            return errorResponse(id, -32602, "arguments must be object");

        LOGGER.info("[TOOL] " + toolName);

        try {
            JObject agentResult = AGENTS.dispatch(toolName, args);
            JObject result = Json.object()
                .put("content", Json.array().add(
                    Json.object().put("type","text").put("text", agentResult.toJson())))
                .put("isError", false);
            return successResponse(id, result);

        } catch (SecurityException se) {
            LOGGER.warning("[SECURITY] " + sanitize(se.getMessage()));
            return errorResponse(id, -32003, "Security violation: " + sanitize(se.getMessage()));
        } catch (IllegalArgumentException iae) {
            return errorResponse(id, -32602, "Invalid arguments: " + sanitize(iae.getMessage()));
        } catch (Exception e) {
            LOGGER.severe("[TOOL ERROR] " + sanitize(e.getMessage()));
            return errorResponse(id, -32000, "Tool error: " + sanitize(e.getMessage()));
        }
    }

    // ── JSON-RPC helpers ──────────────────────────────────────────────────────
    static String successResponse(JValue id, JObject result) {
        JObject r = Json.object().put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        r.put("result", result);
        return r.toJson();
    }

    static String errorResponse(JValue id, int code, String message) {
        JObject r = Json.object().put("jsonrpc", "2.0");
        if (id != null) r.put("id", id);
        r.put("error", Json.object().put("code", code).put("message", message));
        return r.toJson();
    }

    // ── Schema helpers ────────────────────────────────────────────────────────
    private static JObject tool(String name, String desc, JObject schema) {
        return Json.object().put("name", name).put("description", desc).put("inputSchema", schema);
    }

    private static JObject schema(JObject... props) {
        JObject properties = Json.object();
        JArray  required   = Json.array();
        for (JObject prop : props) {
            String nm = prop.get("_n").asText();
            properties.put(nm, Json.object()
                .put("type",        prop.get("type").asText())
                .put("description", prop.get("desc").asText()));
            required.add(nm);
        }
        return Json.object().put("type","object").put("properties", properties).put("required", required);
    }

    private static JObject p(String name, String type, String desc) {
        return Json.object().put("_n", name).put("type", type).put("desc", desc);
    }

    // ── Security helpers ──────────────────────────────────────────────────────
    public static String sanitize(String input) {
        if (input == null) return "null";
        String clean = input.replaceAll("[\\p{Cntrl}]", "");
        return clean.length() > 200 ? clean.substring(0, 200) : clean;
    }

    // ── Logger ────────────────────────────────────────────────────────────────
    private static void setupLogger() {
        Logger root = Logger.getLogger("");
        for (var h : root.getHandlers()) root.removeHandler(h);
        StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
        sh.setLevel(Level.ALL);
        root.addHandler(sh);
        root.setLevel(Level.INFO);
    }
}
