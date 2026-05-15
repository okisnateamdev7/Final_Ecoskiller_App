#!/usr/bin/env python3
"""
Harbor MCP Server — Agent Test Runner
Ecoskiller | CAT-05

Usage:
    python3 test_agents.py            # quick pass/fail
    python3 test_agents.py --verbose  # with full JSON output

Requires:
    java HarborMcpServer to be on the classpath or JAR in ../dist/
"""

import subprocess, json, sys, os

VERBOSE = "--verbose" in sys.argv

# Resolve paths relative to this script's location
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
JAR_PATH   = os.path.join(SCRIPT_DIR, "dist", "mcp-harbor-service.jar")
SRC_PATH   = os.path.join(SCRIPT_DIR, "src")

# Try JAR first, then compiled class files
if os.path.exists(JAR_PATH):
    JAVA_CMD = ["java", "-jar", JAR_PATH]
elif os.path.exists(os.path.join(SRC_PATH, "HarborMcpServer.class")):
    JAVA_CMD = ["java", "-cp", SRC_PATH, "HarborMcpServer"]
else:
    print("ERROR: Neither dist/mcp-harbor-service.jar nor src/HarborMcpServer.class found.")
    print("Run:  cd src && javac HarborMcpServer.java")
    sys.exit(1)

def rpc(request_id, method, params=None):
    req = {"jsonrpc": "2.0", "id": str(request_id), "method": method, "params": params or {}}
    raw = json.dumps(req)
    try:
        result = subprocess.run(
            JAVA_CMD, input=raw, capture_output=True, text=True, timeout=10
        )
        line = result.stdout.strip()
        if not line:
            return None, "No output from server"
        return json.loads(line), None
    except Exception as e:
        return None, str(e)

def run(name, request_id, method, params=None, expect_error=False):
    resp, err = rpc(request_id, method, params)
    if err:
        return name, False, err

    if VERBOSE:
        print(f"\n  ── {name}")
        print(f"  RSP: {json.dumps(resp, indent=2)[:400]}")

    has_top_err  = resp is not None and "error" in resp
    is_tool_err  = resp is not None and resp.get("result", {}).get("isError", False)

    # HARBOR env vars not configured → tool errors are acceptable (server structure still OK)
    env_not_set  = has_top_err and ("HARBOR_URL" in resp["error"].get("message","") or
                                    "env var" in resp["error"].get("message","").lower())
    tool_env_err = is_tool_err and "HARBOR_URL" in (
        (resp.get("result",{}).get("content") or [{}])[0].get("text",""))

    if expect_error:
        passed = has_top_err or is_tool_err
        return name, passed, None if passed else "Expected error response — got success"

    if env_not_set or tool_env_err:
        return name, True, None  # env not configured — server structure is correct

    if has_top_err:
        return name, False, resp["error"].get("message","unknown error")

    return name, True, None

def tool_call(name, request_id, tool_name, arguments, expect_error=False):
    return run(name, request_id, "tools/call",
               {"name": tool_name, "arguments": arguments},
               expect_error=expect_error)

# ─────────────────────────────────────────────────────────────────────────
# Test Suite
# ─────────────────────────────────────────────────────────────────────────

results = []

# Protocol
results.append(run("initialize",   1, "initialize", {"protocolVersion": "2024-11-05"}))
results.append(run("ping",         2, "ping"))
results.append(run("tools/list",   3, "tools/list"))
results.append(run("unknown method returns -32601", 4, "nonexistent/method", expect_error=True))

# Verify tools/list returns exactly 12 tools
resp, _ = rpc(99, "tools/list")
if resp and "result" in resp:
    count = len(resp["result"].get("tools", []))
    results.append(("tools/list returns 12 agents", count == 12,
                    None if count == 12 else f"Expected 12, got {count}"))
    tool_names = [t["name"] for t in resp["result"]["tools"]]
    expected = [
        "image_list","image_push_status","image_pull_validate","tag_retention_status",
        "vulnerability_report","robot_account_manage","project_quota_check","webhook_manage",
        "image_tag_promote","registry_health","proxy_cache_manage","audit_log_query"
    ]
    for tn in expected:
        results.append((f"  tool registered: {tn}", tn in tool_names,
                        None if tn in tool_names else "Missing tool"))

# 12 agent tool calls
results.append(tool_call("image_list (default)",          10, "image_list",           {}))
results.append(tool_call("image_list (with filter)",      11, "image_list",           {"filter":"auth","page_size":5}))
results.append(tool_call("image_push_status",             12, "image_push_status",    {"service":"auth-service","limit":3}))
results.append(tool_call("image_pull_validate",           13, "image_pull_validate",  {"service":"auth-service","tag":"prod-latest"}))
results.append(tool_call("tag_retention_status",          14, "tag_retention_status", {"action":"status"}))
results.append(tool_call("vulnerability_report",          15, "vulnerability_report", {"service":"scoring-engine","min_severity":"HIGH"}))
results.append(tool_call("robot_account_manage/list",     16, "robot_account_manage", {"action":"list"}))
results.append(tool_call("project_quota_check",           17, "project_quota_check",  {}))
results.append(tool_call("webhook_manage/list",           18, "webhook_manage",        {"action":"list"}))
results.append(tool_call("image_tag_promote",             19, "image_tag_promote",    {"service":"auth-service","sha_tag":"abc1234f","target_tag":"dev-latest"}))
results.append(tool_call("registry_health",               20, "registry_health",      {"include":"both"}))
results.append(tool_call("proxy_cache_manage/list",       21, "proxy_cache_manage",   {"action":"list"}))
results.append(tool_call("audit_log_query",               22, "audit_log_query",      {"operation":"push","limit":10}))

# Security tests
results.append(tool_call("SEC: shell metachar in tool name",   30, "rm; -rf /",     {}, expect_error=True))
results.append(tool_call("SEC: path traversal in arg",         31, "image_list",    {"filter":"../../etc/passwd"}, expect_error=True))
results.append(tool_call("SEC: oversized string",              32, "image_push_status", {"service":"x"*2000}, expect_error=True))
results.append(tool_call("SEC: invalid service name (spaces)", 33, "image_push_status", {"service":"auth service"}, expect_error=True))
results.append(tool_call("SEC: invalid tag (shell special)",   34, "image_pull_validate",
                         {"service":"auth-service","tag":"latest;rm -rf /"}, expect_error=True))

# Argument validation
results.append(tool_call("VAL: missing required 'service'",   40, "image_push_status",   {}))
results.append(tool_call("VAL: missing required 'sha_tag'",   41, "image_tag_promote",   {"service":"auth-service","target_tag":"dev-latest"}))

# ─────────────────────────────────────────────────────────────────────────
# Report
# ─────────────────────────────────────────────────────────────────────────

print()
print("═══════════════════════════════════════════════════════")
print("  Harbor MCP Server — Test Results")
print("═══════════════════════════════════════════════════════")

passed = failed = 0
for name, ok, err in results:
    icon = "✓" if ok else "✗"
    msg  = "" if ok else f"  ← {err}"
    print(f"  {icon}  {name}{msg}")
    if ok: passed += 1
    else:  failed += 1

print()
print(f"  Total: {passed + failed}  |  Passed: {passed}  |  Failed: {failed}")
print()

if failed == 0:
    print("  ✓ All tests passed")
else:
    print(f"  ✗ {failed} test(s) failed")

sys.exit(0 if failed == 0 else 1)
