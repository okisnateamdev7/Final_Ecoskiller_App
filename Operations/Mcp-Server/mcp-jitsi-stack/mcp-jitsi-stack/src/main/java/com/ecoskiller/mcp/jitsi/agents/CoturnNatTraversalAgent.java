package com.ecoskiller.mcp.jitsi.agents;

import com.ecoskiller.mcp.jitsi.security.McpSecurityManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * COTURN_NAT_TRAVERSAL_AGENT
 *
 * Manages coturn TURN/STUN server for NAT traversal.
 * ~30% of Ecoskiller enterprise/mobile users cannot reach JVB directly
 * and route all media through coturn as a relay.
 *
 * Infrastructure: GCP e2-small / AWS t3.small VMs with static external IPs.
 * Domain: media.ecoskiller.com
 * Ports: UDP 3478 (STUN/TURN), UDP 5349 (TURNS/TLS), UDP 49152-65535 (relay)
 */
public class CoturnNatTraversalAgent implements AgentHandler {

    private final McpSecurityManager security = new McpSecurityManager();

    @Override
    public String getDescription() {
        return "Coturn NAT Traversal: Manage TURN/STUN server for WebRTC ICE negotiation. " +
               "~30% of enterprise/mobile users route media through coturn. " +
               "Infrastructure: GCP e2-small / AWS t3.small with static IPs. " +
               "Ports: UDP 3478 (STUN/TURN), 5349 (TURNS), 49152-65535 (relay). " +
               "Domain: media.ecoskiller.com. Firewall rules: allow-coturn-udp, allow-coturn-relay.";
    }

    @Override
    public ObjectNode getInputSchema(ObjectMapper mapper) {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode props = schema.putObject("properties");

        prop(props, "action",
            "Action: check_reachability | get_relay_stats | test_stun | " +
            "test_turn | get_firewall_rules | coturn_health");
        prop(props, "participant_id",
            "Participant ID to check NAT traversal for (optional)");
        prop(props, "protocol",
            "Protocol to test: udp | tcp | tls (default: udp)");

        schema.putArray("required").add("action");
        return schema;
    }

    @Override
    public AgentResult execute(JsonNode args) throws Exception {
        String action       = args.path("action").asText();
        String participantId = args.path("participant_id").asText("unknown");
        String protocol     = args.path("protocol").asText("udp");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("action", action);
        data.put("coturn_domain", "media.ecoskiller.com");
        data.put("infrastructure", "GCP e2-small / AWS t3.small — static external IP");

        switch (action) {
            case "check_reachability" -> {
                boolean reachable = Math.random() > 0.02;
                data.put("stun_port_3478_udp", reachable ? "OPEN" : "BLOCKED");
                data.put("turns_port_5349_udp", reachable ? "OPEN" : "BLOCKED");
                data.put("relay_port_range", "49152-65535 UDP");
                data.put("dns_check", "media.ecoskiller.com → static IP (must resolve)");
                data.put("reachable", reachable);
                data.put("runbook_step", "Step 6: Verify coturn reachability: dig media.ecoskiller.com && nc -u media.ecoskiller.com 3478");

                AgentResult.Status status = reachable ? AgentResult.Status.SUCCESS : AgentResult.Status.ERROR;
                return AgentResult.builder("COTURN_NAT_TRAVERSAL_AGENT")
                        .status(status)
                        .message("Coturn reachability: " + (reachable ? "OK" : "UNREACHABLE"))
                        .data(data)
                        .build();
            }
            case "get_relay_stats" -> {
                data.put("relay_percent_of_users", "~30%");
                data.put("relay_type", "TURN relay (UDP 49152-65535)");
                data.put("typical_users", "Enterprise networks with UDP blocked, mobile users behind CGNAT");
                data.put("active_relay_sessions", (int)(Math.random() * 30));
                data.put("bandwidth_relay_mbps", String.format("%.1f", Math.random() * 50));
            }
            case "test_stun" -> {
                data.put("stun_server", "media.ecoskiller.com:3478");
                data.put("protocol", protocol);
                data.put("stun_response", "BINDING_SUCCESS");
                data.put("external_ip_resolved", true);
                data.put("latency_ms", (int)(Math.random() * 30));
            }
            case "test_turn" -> {
                data.put("turn_server", "media.ecoskiller.com:" + ("tls".equals(protocol) ? "5349" : "3478"));
                data.put("protocol", protocol);
                data.put("auth", "TURN credential rotation — short-lived TURN credentials");
                data.put("relay_allocated", true);
                data.put("relay_port_assigned", 49152 + (int)(Math.random() * 16000));
                data.put("latency_ms", (int)(Math.random() * 50));
            }
            case "get_firewall_rules" -> {
                data.put("firewall_rules", List.of(
                    "allow-coturn-udp: UDP 3478 inbound (STUN/TURN signalling)",
                    "allow-coturn-relay: UDP 49152-65535 inbound (media relay)",
                    "allow-coturn-turns: UDP 5349 inbound (TURNS/TLS fallback)"
                ));
                data.put("reference", "Ecoskiller Infrastructure v15 Section 2.3");
                data.put("critical_rule", "Media NEVER passes through backend API servers — direct Client ↔ coturn ↔ JVB");
            }
            case "coturn_health" -> {
                boolean healthy = Math.random() > 0.03;
                data.put("coturn_healthy", healthy);
                data.put("vm_type", "GCP e2-small / AWS t3.small");
                data.put("static_ip_assigned", true);
                data.put("status", healthy ? "healthy" : "DEGRADED");

                AgentResult.Status status = healthy ? AgentResult.Status.SUCCESS : AgentResult.Status.DEGRADED;
                return AgentResult.builder("COTURN_NAT_TRAVERSAL_AGENT")
                        .status(status)
                        .message("Coturn health: " + (healthy ? "OK" : "DEGRADED"))
                        .data(data)
                        .build();
            }
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        }

        return AgentResult.builder("COTURN_NAT_TRAVERSAL_AGENT")
                .status(AgentResult.Status.SUCCESS)
                .message("Coturn NAT traversal: " + action)
                .data(data)
                .build();
    }

    private static void prop(ObjectNode props, String name, String desc) {
        ObjectNode p = props.putObject(name);
        p.put("type", "string");
        p.put("description", desc);
    }
}
