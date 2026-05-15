package com.ecoskiller.mcp.nat.agents;

import com.ecoskiller.mcp.nat.model.ToolResult;
import com.ecoskiller.mcp.nat.security.SecurityManager;

import java.util.*;

/**
 * SNAT_MASQUERADE_AGENT
 *
 * Simulates Source NAT translation mechanics.
 * GCP public IP: 172.16.0.1 | AWS public IP: 10.0.0.1
 * Pod CIDR: GCP 10.244.0.0/16, AWS 10.245.0.0/16
 */
public class SnatMasqueradeAgent implements NatAgent {

    @Override public String toolName() { return "snat_masquerade"; }

    @Override
    public Map<String, Object> toolDefinition() {
        return Map.of(
            "name", toolName(),
            "description", "Simulate SNAT/masquerade translation: translates pod private IP:port to gateway public IP:port. " +
                "Returns the iptables POSTROUTING rule, conntrack entry, and translated packet details. " +
                "Use to verify NAT translation logic for GCP (172.16.0.1) or AWS (10.0.0.1) gateways.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "pod_ip",          Map.of("type", "string", "description", "Source pod IP (e.g. 10.244.1.5)"),
                    "pod_port",        Map.of("type", "integer", "description", "Source pod ephemeral port (1024-65535)"),
                    "dest_ip",         Map.of("type", "string", "description", "External destination IP"),
                    "dest_port",       Map.of("type", "integer", "description", "Destination port (e.g. 443)"),
                    "protocol",        Map.of("type", "string", "description", "Protocol: tcp, udp, icmp"),
                    "cloud",           Map.of("type", "string", "description", "Cloud provider: gcp or aws")
                ),
                "required", List.of("pod_ip", "pod_port", "dest_ip", "dest_port")
            )
        );
    }

    @Override
    public ToolResult execute(Map<String, Object> args, SecurityManager sec) {
        sec.checkRateLimit(toolName());

        String podIp   = (String) args.getOrDefault("pod_ip",   "10.244.1.5");
        int    podPort = toInt(args.get("pod_port"), 50000);
        String destIp  = (String) args.getOrDefault("dest_ip",  "1.2.3.4");
        int    destPort= toInt(args.get("dest_port"), 443);
        String proto   = (String) args.getOrDefault("protocol", "tcp");
        String cloud   = (String) args.getOrDefault("cloud",    "gcp");

        sec.requireValidIp(podIp,  "pod_ip");
        sec.requireValidIp(destIp, "dest_ip");
        sec.requireValidPort(podPort,  "pod_port");
        sec.requireValidPort(destPort, "dest_port");

        String gatewayIp = "aws".equalsIgnoreCase(cloud) ? "10.0.0.1" : "172.16.0.1";
        int    gwPort    = 40000 + (podPort % 20000); // simulated ephemeral port assignment

        String iptablesRule = "iptables -t nat -A POSTROUTING -s " + getCidr(cloud, podIp)
            + " -o eth0 -j SNAT --to-source " + gatewayIp;

        String conntrackEntry = String.format(
            "proto=%s src=%s dst=%s sport=%d dport=%d state=ESTABLISHED\n"
            + "  → reply: src=%s dst=%s sport=%d dport=%d ttl=432000",
            proto, podIp, destIp, podPort, destPort,
            destIp, gatewayIp, destPort, gwPort
        );

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cloud",           cloud.toUpperCase());
        data.put("gateway_ip",      gatewayIp);
        data.put("original_packet", Map.of("src", podIp+":"+podPort, "dst", destIp+":"+destPort, "proto", proto));
        data.put("snat_packet",     Map.of("src", gatewayIp+":"+gwPort, "dst", destIp+":"+destPort, "proto", proto));
        data.put("iptables_rule",   iptablesRule);
        data.put("conntrack_entry", conntrackEntry);
        data.put("translation_note","Source IP rewritten: "+podIp+"→"+gatewayIp+", Port: "+podPort+"→"+gwPort);
        data.put("reverse_translation","Reply "+destIp+":"+destPort+" → "+gatewayIp+":"+gwPort+" mapped back to "+podIp+":"+podPort);

        return ToolResult.success("SNAT translation computed for " + podIp + ":" + podPort
            + " → " + gatewayIp + ":" + gwPort, data);
    }

    private String getCidr(String cloud, String podIp) {
        if ("aws".equalsIgnoreCase(cloud)) return "10.245.0.0/16";
        return "10.244.0.0/16";
    }

    private int toInt(Object v, int def) {
        if (v instanceof Number n) return n.intValue();
        try { return Integer.parseInt(String.valueOf(v)); } catch (Exception e) { return def; }
    }
}
