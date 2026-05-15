package com.ecoskiller.mcp.metallb.security;

import com.ecoskiller.mcp.metallb.json.Json;
import java.util.*;
import java.util.regex.Pattern;

public class SecurityManager {
    private static final Set<String> ALLOWED_VERSIONS = Set.of("2024-11-05","2025-03-26");
    private static final Set<String> ALLOWED_TOOLS = Set.of(
        "metallb_get_status","metallb_list_ip_pools","metallb_allocate_ip","metallb_release_ip",
        "metallb_check_pool_exhaustion","metallb_expand_pool","metallb_get_bgp_status",
        "metallb_configure_bgp","metallb_get_bgp_sessions","metallb_get_l2_status",
        "metallb_configure_l2","metallb_list_services","metallb_assign_service_ip",
        "metallb_check_node_health","metallb_simulate_failover","metallb_get_metrics",
        "metallb_get_config","metallb_validate_config","metallb_audit_log",
        "metallb_troubleshoot_service"
    );
    private static final Pattern IP_CIDR = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}(/\\d{1,2})?$");
    private static final Pattern ASN     = Pattern.compile("^\\d{1,10}$");
    private static final Pattern SAFE_NAME = Pattern.compile("^[a-zA-Z0-9_\\-.]{1,253}$");
    private static final Pattern NAMESPACE = Pattern.compile("^[a-z0-9_\\-]{1,63}$");
    private static final Pattern CLOUD     = Pattern.compile("^(gcp|aws|both)$");
    private static final Pattern MODE      = Pattern.compile("^(bgp|l2|both)$");
    private static final int MAX_STR = 512;

    public boolean validateRequest(Json.Node req) {
        if (!(req instanceof Json.Obj o)) return false;
        if (!o.has("jsonrpc") || !"2.0".equals(Json.strField(o,"jsonrpc",""))) return false;
        if (!o.has("method")) return false;
        String m = Json.strField(o,"method","");
        return !m.isBlank() && m.length() <= 64;
    }

    public boolean isAllowedProtocolVersion(String v) {
        return v != null && ALLOWED_VERSIONS.contains(v);
    }

    public boolean validateToolName(String name) {
        return name != null && !name.isBlank() && name.length() <= 64 && ALLOWED_TOOLS.contains(name);
    }

    public boolean sanitizeArguments(Json.Node args) {
        if (args == null || args instanceof Json.Null) return true;
        if (!(args instanceof Json.Obj o)) return false;
        for (var entry : ((Json.Obj)args).fields().entrySet()) {
            String key = entry.getKey();
            Json.Node val = entry.getValue();
            if (!SAFE_NAME.matcher(key).matches()) return false;
            if (val instanceof Json.Str s) {
                String text = s.value;
                if (text.length() > MAX_STR) return false;
                if (!isAllowedSlash(key, text) && (text.contains("..") || text.contains("/"))) return false;
                if (!validateField(key, text)) return false;
            } else if (val instanceof Json.Num n) {
                if (n.value < 0 || n.value > 4_294_967_295L) return false;
            } else if (!(val instanceof Json.Bool) && !(val instanceof Json.Null)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllowedSlash(String key, String val) {
        return (key.contains("ip")||key.contains("cidr")||key.contains("pool")||key.contains("range"))
            && IP_CIDR.matcher(val).matches();
    }

    private boolean validateField(String key, String val) {
        if (key.endsWith("_ip")||key.equals("ip")||key.contains("cidr")||key.contains("range"))
            return IP_CIDR.matcher(val).matches();
        if (key.equals("asn")||key.equals("peer_asn")||key.equals("local_asn"))
            return ASN.matcher(val).matches();
        if (key.equals("namespace")) return NAMESPACE.matcher(val).matches();
        if (key.equals("cloud"))     return CLOUD.matcher(val).matches();
        if (key.equals("mode"))      return MODE.matcher(val).matches();
        if (key.equals("service_name")||key.equals("pool_name")||key.equals("pool")||key.equals("node"))
            return SAFE_NAME.matcher(val).matches();
        return !val.matches(".*[;&|`$<>!].*");
    }

    public Set<String> getAllowedTools() { return ALLOWED_TOOLS; }
}
