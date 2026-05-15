#!/usr/bin/env python3
"""
test_agents.py — Test all 18 NAT Gateway MCP agents
Ecoskiller | mcp-nat-gateway

Usage:
    python3 test_agents.py            # quick pass/fail
    python3 test_agents.py --verbose  # full JSON output
"""

import subprocess
import json
import sys
import os

VERBOSE = "--verbose" in sys.argv

JAR = os.path.join(os.path.dirname(__file__), "target", "mcp-nat-gateway.jar")

TESTS = [
    # (test_name, method, params)
    ("initialize",            "initialize",   {"protocolVersion":"2024-11-05","capabilities":{}}),
    ("tools_list",            "tools/list",   {}),
    ("ping",                  "ping",         {}),

    # Agent calls
    ("snat_masquerade",       "tools/call",   {"name":"snat_masquerade",       "arguments":{"pod_ip":"10.244.1.5","pod_port":50001,"dest_ip":"35.192.0.5","dest_port":443,"protocol":"tcp","cloud":"gcp"}}),
    ("conntrack_stats",       "tools/call",   {"name":"conntrack_manager",      "arguments":{"action":"stats"}}),
    ("conntrack_lookup",      "tools/call",   {"name":"conntrack_manager",      "arguments":{"action":"lookup","pod_ip":"10.244.1.5"}}),
    ("egress_policy_list",    "tools/call",   {"name":"egress_policy",          "arguments":{"action":"list"}}),
    ("egress_policy_check",   "tools/call",   {"name":"egress_policy",          "arguments":{"action":"check","dest_ip":"35.192.0.5","confirm":True}}),
    ("vrrp_status_gcp",       "tools/call",   {"name":"vrrp_failover",          "arguments":{"action":"status","cloud":"gcp"}}),
    ("vrrp_status_aws",       "tools/call",   {"name":"vrrp_failover",          "arguments":{"action":"status","cloud":"aws"}}),
    ("vrrp_failover_sim",     "tools/call",   {"name":"vrrp_failover",          "arguments":{"action":"simulate_failover","cloud":"gcp"}}),
    ("performance_medium",    "tools/call",   {"name":"performance_tuning",     "arguments":{"target_connections":100000,"gateways":2}}),
    ("traffic_top_pods",      "tools/call",   {"name":"traffic_monitor",        "arguments":{"view":"top_pods"}}),
    ("traffic_bandwidth",     "tools/call",   {"name":"traffic_monitor",        "arguments":{"view":"bandwidth"}}),
    ("iptables_ruleset",      "tools/call",   {"name":"iptables_rules",         "arguments":{"action":"full_ruleset","cloud":"gcp","confirm":True}}),
    ("packet_diag_hangs",     "tools/call",   {"name":"packet_flow_diagnostics","arguments":{"symptom":"hangs","pod_ip":"10.244.1.5","dest_ip":"1.2.3.4"}}),
    ("packet_diag_connfull",  "tools/call",   {"name":"packet_flow_diagnostics","arguments":{"symptom":"conntrack_full"}}),
    ("nat_timeout_get",       "tools/call",   {"name":"nat_session_timeout",    "arguments":{"action":"get"}}),
    ("nat_timeout_set",       "tools/call",   {"name":"nat_session_timeout",    "arguments":{"action":"set","tcp_established_sec":300,"confirm":True}}),
    ("mtu_show",              "tools/call",   {"name":"mtu_fragmentation",      "arguments":{"action":"show","cloud":"gcp"}}),
    ("mtu_clamp",             "tools/call",   {"name":"mtu_fragmentation",      "arguments":{"action":"clamp_mss"}}),
    ("audit_recent",          "tools/call",   {"name":"audit_trail",            "arguments":{"query":"recent"}}),
    ("audit_iptables",        "tools/call",   {"name":"audit_trail",            "arguments":{"query":"iptables_changes"}}),
    ("prometheus_current",    "tools/call",   {"name":"prometheus_metrics",     "arguments":{"action":"current"}}),
    ("prometheus_alerts",     "tools/call",   {"name":"prometheus_metrics",     "arguments":{"action":"alerting_rules"}}),
    ("health_check_gcp",      "tools/call",   {"name":"health_check",           "arguments":{"cloud":"gcp","detail":True}}),
    ("keepalived_primary",    "tools/call",   {"name":"keepalived_config",      "arguments":{"role":"primary","cloud":"gcp"}}),
    ("keepalived_secondary",  "tools/call",   {"name":"keepalived_config",      "arguments":{"role":"secondary","cloud":"aws"}}),
    ("whitelist_list",        "tools/call",   {"name":"egress_whitelist",       "arguments":{"action":"list"}}),
    ("whitelist_check",       "tools/call",   {"name":"egress_whitelist",       "arguments":{"action":"check_coverage"}}),
    ("rate_limiter_show",     "tools/call",   {"name":"connection_rate_limiter","arguments":{"action":"show"}}),
    ("sysctl_generate",       "tools/call",   {"name":"sysctl_parameters",      "arguments":{"action":"generate_config","profile":"large"}}),
    ("gateway_status_all",    "tools/call",   {"name":"gateway_status",         "arguments":{"cloud":"all"}}),
    ("gateway_status_gcp",    "tools/call",   {"name":"gateway_status",         "arguments":{"cloud":"gcp"}}),

    # Security tests
    ("sec_shell_inject",      "tools/call",   {"name":"snat_masquerade",        "arguments":{"pod_ip":"10.244.1.5; rm -rf /","pod_port":443,"dest_ip":"1.2.3.4","dest_port":443}}),
    ("sec_missing_confirm",   "tools/call",   {"name":"iptables_rules",         "arguments":{"action":"full_ruleset","cloud":"gcp"}}),
    ("sec_invalid_ip",        "tools/call",   {"name":"snat_masquerade",        "arguments":{"pod_ip":"999.999.999.999","pod_port":443,"dest_ip":"1.2.3.4","dest_port":443}}),
    ("sec_invalid_jsonrpc",   "initialize",   None),  # will be sent without jsonrpc field
]

def send_rpc(process, method, params, id_):
    msg = {"jsonrpc": "2.0", "id": id_, "method": method}
    if params:
        msg["params"] = params
    line = json.dumps(msg) + "\n"
    process.stdin.write(line.encode())
    process.stdin.flush()
    response_line = process.stdout.readline()
    return json.loads(response_line)

def run_tests():
    if not os.path.exists(JAR):
        print(f"❌ JAR not found: {JAR}")
        print("   Run ./build.sh first")
        sys.exit(1)

    proc = subprocess.Popen(
        ["java", "--enable-preview", "-jar", JAR],
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.DEVNULL
    )

    passed = 0
    failed = 0

    print(f"\n{'─'*60}")
    print(f"  mcp-nat-gateway — Agent Tests ({len(TESTS)} tests)")
    print(f"{'─'*60}\n")

    for i, (name, method, params) in enumerate(TESTS, 1):
        try:
            resp = send_rpc(proc, method, params, i)
            has_result = "result" in resp
            has_error  = "error" in resp

            # Security tests expect errors
            is_security_test = name.startswith("sec_")
            if is_security_test:
                if has_error:
                    status = "✅ PASS (security blocked)"
                    passed += 1
                else:
                    status = "❌ FAIL (security should have blocked)"
                    failed += 1
            else:
                if has_result:
                    status = "✅ PASS"
                    passed += 1
                else:
                    status = f"❌ FAIL — {resp.get('error',{}).get('message','unknown error')}"
                    failed += 1

            print(f"  [{i:02d}] {name:<35} {status}")

            if VERBOSE:
                print(f"       {json.dumps(resp, indent=2)[:400]}\n")

        except Exception as e:
            print(f"  [{i:02d}] {name:<35} ❌ EXCEPTION: {e}")
            failed += 1

    proc.terminate()

    print(f"\n{'─'*60}")
    print(f"  Results: {passed} passed, {failed} failed / {len(TESTS)} total")
    print(f"{'─'*60}\n")

    sys.exit(0 if failed == 0 else 1)

if __name__ == "__main__":
    run_tests()
