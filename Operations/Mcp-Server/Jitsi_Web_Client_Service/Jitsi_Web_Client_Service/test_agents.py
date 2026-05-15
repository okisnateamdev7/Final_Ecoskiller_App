#!/usr/bin/env python3
"""
Jitsi Web Client MCP Server — Test Suite
Ecoskiller Platform

Tests all 15 tools via stdin/stdout JSON-RPC 2.0.
Usage:
    python3 test_agents.py           # quick pass/fail
    python3 test_agents.py --verbose # with full JSON output
"""

import subprocess, json, sys, time

VERBOSE = "--verbose" in sys.argv

# ── Helpers ───────────────────────────────────────────────────────────────────

def rpc(method, params=None, req_id=1):
    return json.dumps({"jsonrpc": "2.0", "id": req_id, "method": method,
                        "params": params or {}})

def call_server(messages):
    """Run server process, feed messages, collect responses."""
    proc = subprocess.Popen(
        ["java", "-jar", "target/jitsi-web-client-mcp.jar"],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.DEVNULL,
        text=True
    )
    input_data = "\n".join(messages) + "\n"
    try:
        stdout, _ = proc.communicate(input=input_data, timeout=15)
    except subprocess.TimeoutExpired:
        proc.kill()
        return []
    return [json.loads(line) for line in stdout.strip().split("\n") if line.strip()]

# Minimal valid HS256 JWT for testing (secret: CHANGE_ME_IN_PRODUCTION_USE_ENV_VAR)
# payload: {"sub":"test-user","role":"admin","assessmentId":"gd-test","roomId":"gd-test","exp":9999999999,"iat":1700000000}
TEST_JWT = (
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
    "eyJzdWIiOiJ0ZXN0LXVzZXIiLCJyb2xlIjoiYWRtaW4iLCJhc3Nlc3NtZW50SWQiOiJnZC10ZXN0Iiwicm9vbUlkIjoiZ2QtdGVzdCIsImV4cCI6OTk5OTk5OTk5OSwiaWF0IjoxNzAwMDAwMDAwfQ."
    "placeholder_sig"
)

# ── Test Cases ────────────────────────────────────────────────────────────────

TESTS = []

def test(name, method, params, expect_no_error=True):
    TESTS.append((name, method, params, expect_no_error))

# ── Protocol tests
test("ping",       "ping",       {})
test("initialize", "initialize", {"protocolVersion": "2024-11-05"})
test("tools/list", "tools/list", {})
test("unknown_method_returns_error", "nonexistent", {}, expect_no_error=False)

# ── Tool tests (JWT required)
BASE = {"jwt_token": TEST_JWT}

test("session_create",
     "tools/call", {"name": "session_create", "arguments": {
         **BASE, "assessment_id": "gd-test", "room_name": "Test Room",
         "max_participants": 6, "duration_sec": 1800, "recording_enabled": False
     }})

test("session_join",
     "tools/call", {"name": "session_join", "arguments": {
         **BASE, "room_id": "gd-test", "display_name": "Test Candidate",
         "device_type": "desktop", "browser": "chrome"
     }})

test("session_leave",
     "tools/call", {"name": "session_leave", "arguments": {
         **BASE, "room_id": "gd-test", "session_id": "sess-001", "reason": "user_left"
     }})

test("session_status",
     "tools/call", {"name": "session_status", "arguments": {
         **BASE, "room_id": "gd-test"
     }})

test("media_quality_get",
     "tools/call", {"name": "media_quality_get", "arguments": {
         **BASE, "room_id": "gd-test", "participant_id": "local"
     }})

test("media_device_list",
     "tools/call", {"name": "media_device_list", "arguments": {
         **BASE, "session_id": "sess-001", "device_type": "all"
     }})

test("participant_list",
     "tools/call", {"name": "participant_list", "arguments": {
         **BASE, "room_id": "gd-test", "sort_by": "joined_at"
     }})

test("participant_mute",
     "tools/call", {"name": "participant_mute", "arguments": {
         **BASE, "room_id": "gd-test", "participant_id": "cand-001",
         "mute_audio": True, "mute_video": False
     }})

test("participant_remove",
     "tools/call", {"name": "participant_remove", "arguments": {
         **BASE, "room_id": "gd-test", "participant_id": "cand-002",
         "reason": "Violated policy"
     }})

test("recording_start",
     "tools/call", {"name": "recording_start", "arguments": {
         **BASE, "room_id": "gd-test", "notify_participants": True
     }})

test("recording_stop",
     "tools/call", {"name": "recording_stop", "arguments": {
         **BASE, "room_id": "gd-test", "recording_id": "rec-gd-test-001"
     }})

test("recording_status",
     "tools/call", {"name": "recording_status", "arguments": {
         **BASE, "room_id": "gd-test"
     }})

test("analytics_session_report",
     "tools/call", {"name": "analytics_session_report", "arguments": {
         **BASE, "session_id": "sess-001", "assessment_id": "gd-test", "format": "summary"
     }})

test("analytics_event_emit",
     "tools/call", {"name": "analytics_event_emit", "arguments": {
         **BASE, "session_id": "sess-001", "event_name": "camera_toggled",
         "event_data": {"state": "off"}
     }})

test("auth_token_validate",
     "tools/call", {"name": "auth_token_validate", "arguments": {
         "jwt_token": TEST_JWT
     }})

test("missing_required_arg_returns_error",
     "tools/call", {"name": "session_create", "arguments": {**BASE}},
     expect_no_error=False)

# ── Run ───────────────────────────────────────────────────────────────────────

def run():
    print("━" * 60)
    print(f"  Jitsi Web Client MCP Server — Test Suite ({len(TESTS)} tests)")
    print("━" * 60)

    # Build messages: initialize first, then all tools
    init_msg = rpc("initialize", {"protocolVersion": "2024-11-05"}, req_id=0)
    tool_msgs = [rpc(m, p, i + 1) for i, (_, m, p, _) in enumerate(TESTS)]
    all_msgs  = [init_msg] + tool_msgs

    responses = call_server(all_msgs)
    resp_map  = {r.get("id"): r for r in responses}

    passed = failed = 0
    for i, (name, method, params, expect_ok) in enumerate(TESTS):
        resp = resp_map.get(i + 1)
        if resp is None:
            print(f"  ✗ [{i+1:02d}] {name:<45} NO RESPONSE")
            failed += 1
            continue

        has_error   = "error" in resp
        tool_is_err = (not has_error and
                       isinstance(resp.get("result"), dict) and
                       resp["result"].get("isError", False))
        ok = (not has_error) if expect_ok else has_error

        if ok:
            print(f"  ✓ [{i+1:02d}] {name}")
            passed += 1
        else:
            detail = resp.get("error", {}).get("message", "unexpected result")
            print(f"  ✗ [{i+1:02d}] {name:<45} {detail}")
            failed += 1

        if VERBOSE:
            print(f"       → {json.dumps(resp, indent=2)}")

    print()
    print("━" * 60)
    print(f"  Results: {passed} passed, {failed} failed / {len(TESTS)} total")
    print("━" * 60)
    sys.exit(0 if failed == 0 else 1)

if __name__ == "__main__":
    run()
