package com.ecoskiller.mcp.jitsi.agents;

import com.ecoskiller.mcp.jitsi.security.McpSecurityManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * JITSI_WEB_SESSION_AGENT
 *
 * Manages Jitsi Web browser client sessions.
 * In Ecoskiller, Jitsi Web serves the JitsiMeetExternalAPI bundle embedded in
 * the Next.js frontend and Flutter mobile app. Also provides fallback standalone access.
 *
 * Key config overrides for GD sessions:
 *   startWithAudioMuted: true
 *   startWithVideoMuted: true  (GD = audio-only)
 *   disableDeepLinking: true
 *   prejoinPageEnabled: false  (CRITICAL: speaking clock starts at room entry)
 */
public class JitsiWebSessionAgent implements AgentHandler {

    private final McpSecurityManager security = new McpSecurityManager();

    // Environment domain mapping per Ecoskiller spec
    private static final Map<String, String> ENV_DOMAINS = Map.of(
        "dev",   "localhost:8443",
        "test",  "test-media.ecoskiller.com",
        "stage", "stage-media.ecoskiller.com",
        "prod",  "media.ecoskiller.com"
    );

    @Override
    public String getDescription() {
        return "Jitsi Web Session: Manage Jitsi Web browser client sessions and JitsiMeetExternalAPI " +
               "configuration for Next.js and Flutter frontends. Serves HTTPS on port 443/8443. " +
               "Applies GD audio-only config overrides (startWithVideoMuted: true, " +
               "prejoinPageEnabled: false). Supports all 4 environments (dev/test/stage/prod).";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper mapper) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");

        prop(props, "session_id",   "Session/room ID to join or configure");
        prop(props, "action",
            "Action: build_session_config | get_api_bundle_url | create_embed_config | " +
            "get_environment_domain | validate_config | flutter_sdk_config");
        prop(props, "session_type",  "gd | interview | dojo — determines config overrides");
        prop(props, "environment",   "dev | test | stage | prod");
        prop(props, "platform",      "web | flutter — determines SDK integration type");

        schema.putArray("required").add("session_id").add("action");
        return schema;
    }

    @Override
    public AgentResult execute(JsonNode args) throws Exception {
        String sessionId    = args.path("session_id").asText();
        String action       = args.path("action").asText();
        String sessionType  = args.path("session_type").asText("gd");
        String env          = args.path("environment").asText("prod");
        String platform     = args.path("platform").asText("web");

        security.validateSessionId(sessionId);

        String domain = ENV_DOMAINS.getOrDefault(env, ENV_DOMAINS.get("prod"));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("session_id", sessionId);
        data.put("action", action);
        data.put("environment", env);
        data.put("jitsi_domain", domain);
        data.put("jitsi_web_version", "stable-9364");

        switch (action) {
            case "build_session_config" -> {
                Map<String, Object> config = buildConfigOverwrite(sessionType);
                data.put("room_name", sessionId);
                data.put("domain", domain);
                data.put("config_overwrite", config);
                data.put("interface_config_overwrite", Map.of(
                    "SHOW_JITSI_WATERMARK", false,
                    "SHOW_WATERMARK_FOR_GUESTS", false,
                    "DEFAULT_BACKGROUND", "#ecoskiller-dark"
                ));
                data.put("status", "config_built");
            }
            case "get_api_bundle_url" -> {
                data.put("external_api_url", "https://" + domain + "/external_api.js");
                data.put("load_via", "<script src='https://" + domain + "/external_api.js'></script>");
                data.put("next_js_env_var", "NEXT_PUBLIC_JITSI_DOMAIN=" + domain);
                data.put("status", "url_fetched");
            }
            case "create_embed_config" -> {
                data.put("platform", platform);
                if ("flutter".equals(platform)) {
                    data.put("sdk", "jitsi_meet_flutter_sdk ^10.0.0");
                    data.put("dart_define", "--dart-define=JITSI_DOMAIN=" + domain);
                    data.put("build_time_injection", true);
                } else {
                    data.put("sdk", "JitsiMeetExternalAPI");
                    data.put("embed_code", buildEmbedCode(sessionId, domain, sessionType));
                }
                data.put("status", "embed_config_created");
            }
            case "get_environment_domain" -> {
                data.put("all_domains", ENV_DOMAINS);
                data.put("current_domain", domain);
                data.put("port_https", "443");
                data.put("port_https_dev", "8443");
                data.put("status", "domain_resolved");
            }
            case "validate_config" -> {
                Map<String, Object> config = buildConfigOverwrite(sessionType);
                boolean valid = config.containsKey("startWithAudioMuted");
                // Validate critical rule: prejoinPageEnabled must be false for GD
                boolean prejoinOff = !((Boolean) config.getOrDefault("prejoinPageEnabled", true));
                data.put("config_valid", valid && prejoinOff);
                data.put("critical_check_prejoin_disabled", prejoinOff);
                data.put("reason", prejoinOff ? "OK" : "FAIL: prejoinPageEnabled=true breaks GD speaking clock");
                data.put("config", config);

                AgentResult.Status status = (valid && prejoinOff) ? AgentResult.Status.SUCCESS : AgentResult.Status.ERROR;
                return AgentResult.builder("JITSI_WEB_SESSION_AGENT")
                        .status(status)
                        .message("Config validation: " + (valid && prejoinOff ? "PASS" : "FAIL"))
                        .data(data)
                        .build();
            }
            case "flutter_sdk_config" -> {
                data.put("package", "jitsi_meet_flutter_sdk ^10.0.0");
                data.put("jitsi_domain", domain);
                data.put("dart_define", "--dart-define=JITSI_DOMAIN=" + domain);
                data.put("feature_flags", buildConfigOverwrite(sessionType));
                data.put("room_name_rule", "Always = session_id for ClickHouse correlation");
                data.put("status", "flutter_config_built");
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }

        return AgentResult.builder("JITSI_WEB_SESSION_AGENT")
                .status(AgentResult.Status.SUCCESS)
                .message("Jitsi Web session: " + action + " for session " + sessionId)
                .data(data)
                .build();
    }

    private Map<String, Object> buildConfigOverwrite(String sessionType) {
        Map<String, Object> cfg = new LinkedHashMap<>();
        cfg.put("startWithAudioMuted", true);
        cfg.put("startWithVideoMuted", "gd".equals(sessionType)); // audio-only for GD
        cfg.put("disableDeepLinking", true);
        cfg.put("prejoinPageEnabled", false); // CRITICAL: speaking clock starts at room entry
        if ("gd".equals(sessionType)) {
            cfg.put("disableVideo", true);
            cfg.put("enableNoisyMicDetection", true);
        }
        return cfg;
    }

    private Map<String, Object> buildEmbedCode(String sessionId, String domain, String sessionType) {
        Map<String, Object> code = new LinkedHashMap<>();
        code.put("domain", domain);
        code.put("roomName", sessionId);
        code.put("configOverwrite", buildConfigOverwrite(sessionType));
        code.put("parentNode", "document.querySelector('#jitsi-container')");
        return code;
    }

    private static void prop(ObjectNode props, String name, String desc) {
        ObjectNode p = props.putObject(name);
        p.put("type", "string");
        p.put("description", desc);
    }
}
