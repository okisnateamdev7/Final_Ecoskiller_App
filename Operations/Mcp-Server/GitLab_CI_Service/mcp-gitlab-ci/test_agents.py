#!/usr/bin/env python3
"""
test_agents.py — Quick smoke-test for the EcoSkiller GitLab CI MCP Server.

Sends JSON-RPC requests over stdin and reads responses from stdout.
Usage:
    python3 test_agents.py           # pass/fail summary
    python3 test_agents.py --verbose # full JSON output
"""

import json
import subprocess
import sys
import os

VERBOSE = "--verbose" in sys.argv

# ── Test cases ────────────────────────────────────────────────────────────────

TESTS = [
    {
        "name": "initialize",
        "request": {
            "jsonrpc": "2.0", "id": 1, "method": "initialize",
            "params": {"protocolVersion": "2024-11-05", "clientInfo": {"name": "test"}}
        },
        "expect_result": True,
    },
    {
        "name": "ping",
        "request": {"jsonrpc": "2.0", "id": 2, "method": "ping", "params": {}},
        "expect_result": True,
    },
    {
        "name": "tools/list — must return 13 tools",
        "request": {"jsonrpc": "2.0", "id": 3, "method": "tools/list", "params": {}},
        "expect_result": True,
        "check": lambda r: len(r.get("result", {}).get("tools", [])) == 13,
        "check_msg": "Expected 13 tools",
    },
    {
        "name": "pipeline_trigger — feature branch → dev",
        "request": {
            "jsonrpc": "2.0", "id": 4, "method": "tools/call",
            "params": {"name": "pipeline_trigger", "arguments": {
                "service_name": "auth-service", "branch": "feature/login-fix"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "pipeline_status",
        "request": {
            "jsonrpc": "2.0", "id": 5, "method": "tools/call",
            "params": {"name": "pipeline_status", "arguments": {
                "pipeline_id": "pl-123456", "service_name": "auth-service"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "container_vulnerability_scan",
        "request": {
            "jsonrpc": "2.0", "id": 6, "method": "tools/call",
            "params": {"name": "container_vulnerability_scan", "arguments": {
                "image": "auth-service", "tag": "sha-abc1234", "environment": "dev"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "helm_deploy",
        "request": {
            "jsonrpc": "2.0", "id": 7, "method": "tools/call",
            "params": {"name": "helm_deploy", "arguments": {
                "service_name": "auth-service", "environment": "dev", "git_sha": "abc1234"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "helm_rollback",
        "request": {
            "jsonrpc": "2.0", "id": 8, "method": "tools/call",
            "params": {"name": "helm_rollback", "arguments": {
                "service_name": "auth-service", "environment": "test",
                "justification": "Regression in login flow detected"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "kubernetes_health_check",
        "request": {
            "jsonrpc": "2.0", "id": 9, "method": "tools/call",
            "params": {"name": "kubernetes_health_check", "arguments": {
                "service_name": "scoring-engine", "environment": "prod"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "merge_request_gate",
        "request": {
            "jsonrpc": "2.0", "id": 10, "method": "tools/call",
            "params": {"name": "merge_request_gate", "arguments": {
                "mr_id": "MR-42", "service_name": "billing-service",
                "source_branch": "feature/gst-fix"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "deployment_audit_log",
        "request": {
            "jsonrpc": "2.0", "id": 11, "method": "tools/call",
            "params": {"name": "deployment_audit_log", "arguments": {
                "environment": "prod", "limit": 5
            }}
        },
        "expect_result": True,
    },
    {
        "name": "harbor_registry_manage — list",
        "request": {
            "jsonrpc": "2.0", "id": 12, "method": "tools/call",
            "params": {"name": "harbor_registry_manage", "arguments": {"action": "list"}}
        },
        "expect_result": True,
    },
    {
        "name": "slack_notify — p0 triggers PagerDuty",
        "request": {
            "jsonrpc": "2.0", "id": 13, "method": "tools/call",
            "params": {"name": "slack_notify", "arguments": {
                "message": "PROD rollback triggered for auth-service",
                "channel": "#deployments", "severity": "p0"
            }}
        },
        "expect_result": True,
    },
    {
        "name": "environment_promote — dev → test",
        "request": {
            "jsonrpc": "2.0", "id": 14, "method": "tools/call",
            "params": {"name": "environment_promote", "arguments": {
                "service_name": "scoring-engine", "git_sha": "abc1234",
                "from_environment": "dev", "to_environment": "test"
            }}
        },
        "expect_result": True,
    },
    # ── Security tests ────────────────────────────────────────────────────
    {
        "name": "SECURITY: shell injection blocked",
        "request": {
            "jsonrpc": "2.0", "id": 20, "method": "tools/call",
            "params": {"name": "pipeline_trigger", "arguments": {
                "service_name": "auth-service; rm -rf /", "branch": "feature/x"
            }}
        },
        "expect_error": True,
    },
    {
        "name": "SECURITY: invalid environment blocked",
        "request": {
            "jsonrpc": "2.0", "id": 21, "method": "tools/call",
            "params": {"name": "helm_deploy", "arguments": {
                "service_name": "auth-service", "environment": "production-hack",
                "git_sha": "abc1234"
            }}
        },
        "expect_error": True,
    },
    {
        "name": "SECURITY: stage skip blocked (dev → prod)",
        "request": {
            "jsonrpc": "2.0", "id": 22, "method": "tools/call",
            "params": {"name": "environment_promote", "arguments": {
                "service_name": "auth-service", "git_sha": "abc1234",
                "from_environment": "dev", "to_environment": "prod"
            }}
        },
        "expect_result": True,   # returns ToolResult.error (not JSON-RPC error)
        "check": lambda r: "not allowed" in json.dumps(r).lower() or "invalid" in json.dumps(r).lower(),
        "check_msg": "Expected stage-skip error message",
    },
    {
        "name": "SECURITY: unknown method returns error",
        "request": {"jsonrpc": "2.0", "id": 23, "method": "admin/delete_all", "params": {}},
        "expect_error": True,
    },
]

# ── Runner ────────────────────────────────────────────────────────────────────

def run_tests_via_process(jar_path):
    """Run all tests by piping JSON-RPC messages into the JAR process."""
    inputs = [json.dumps(t["request"]) for t in TESTS]
    stdin_data = "\n".join(inputs) + "\n"

    proc = subprocess.run(
        ["java", "-jar", jar_path],
        input=stdin_data.encode(),
        capture_output=True,
        timeout=30
    )

    lines = [l for l in proc.stdout.decode().splitlines() if l.strip()]
    return lines


def run_tests_inline():
    """Run tests by importing the server logic directly via stdin simulation."""
    # Simulate via subprocess calling the test helper
    print("ℹ  JAR not found — printing test payloads for manual verification\n")
    for i, test in enumerate(TESTS, 1):
        print(f"Test {i:02d}: {test['name']}")
        print("  Request:", json.dumps(test["request"], separators=(",", ":")))
        print()


def evaluate(test, response_str):
    try:
        resp = json.loads(response_str)
    except Exception:
        return False, "Could not parse response JSON"

    if VERBOSE:
        print(f"  Response: {json.dumps(resp, indent=2)}")

    if test.get("expect_error"):
        if "error" in resp:
            return True, "Got expected error"
        return False, f"Expected error, got: {resp}"

    if test.get("expect_result"):
        if "result" not in resp:
            return False, f"Expected result, got: {resp}"

    custom_check = test.get("check")
    if custom_check:
        if not custom_check(resp):
            return False, test.get("check_msg", "Custom check failed")

    return True, "OK"


def main():
    jar_path = "target/mcp-gitlab-ci.jar"
    passed = failed = 0

    if not os.path.exists(jar_path):
        print(f"⚠  JAR not found at {jar_path}")
        print("   Run: mvn package -q")
        print("   Then: python3 test_agents.py\n")
        run_tests_inline()
        return

    print(f"Running {len(TESTS)} tests against {jar_path}\n")
    responses = run_tests_via_process(jar_path)

    for i, (test, resp_line) in enumerate(zip(TESTS, responses)):
        ok, msg = evaluate(test, resp_line)
        status = "✅ PASS" if ok else "❌ FAIL"
        print(f"  {status} [{i+1:02d}] {test['name']}")
        if not ok:
            print(f"       → {msg}")
            failed += 1
        else:
            passed += 1

    print(f"\n{'─'*50}")
    print(f"Results: {passed} passed / {failed} failed / {len(TESTS)} total")
    sys.exit(0 if failed == 0 else 1)


if __name__ == "__main__":
    main()
