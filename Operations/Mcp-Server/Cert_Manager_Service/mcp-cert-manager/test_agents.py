#!/usr/bin/env python3
"""
test_agents.py — Smoke-test for EcoSkiller Cert Manager MCP Server.
Sends JSON-RPC messages over stdin and reads responses from stdout.

Usage:
    python3 test_agents.py           # pass/fail summary
    python3 test_agents.py --verbose # with full JSON output
"""

import json, subprocess, sys, os, uuid

VERBOSE = "--verbose" in sys.argv

# ── Test cases (25 total) ─────────────────────────────────────────────────────

VALID_CERT_ID  = str(uuid.uuid4())
VALID_CERT_ID2 = str(uuid.uuid4())

TESTS = [
    # ── Protocol ──────────────────────────────────────────────────────────
    {
        "name": "initialize",
        "request": {"jsonrpc":"2.0","id":1,"method":"initialize",
                    "params":{"protocolVersion":"2024-11-05","clientInfo":{"name":"test"}}},
        "expect_result": True,
    },
    {
        "name": "ping",
        "request": {"jsonrpc":"2.0","id":2,"method":"ping","params":{}},
        "expect_result": True,
    },
    {
        "name": "tools/list — must return 12 tools",
        "request": {"jsonrpc":"2.0","id":3,"method":"tools/list","params":{}},
        "expect_result": True,
        "check": lambda r: len(r.get("result",{}).get("tools",[])) == 12,
        "check_msg": "Expected 12 tools",
    },

    # ── Agent 1: certificate_issue ─────────────────────────────────────────
    {
        "name": "certificate_issue — Gold belt (score 88)",
        "request": {"jsonrpc":"2.0","id":10,"method":"tools/call","params":{
            "name": "certificate_issue",
            "arguments": {"candidate_id":"cand-001","tenant_id":"tenant-acme",
                          "overall_score":88.5,"assessment_type":"group_discussion"}}},
        "expect_result": True,
    },
    {
        "name": "certificate_issue — score below Bronze threshold",
        "request": {"jsonrpc":"2.0","id":11,"method":"tools/call","params":{
            "name": "certificate_issue",
            "arguments": {"candidate_id":"cand-002","tenant_id":"tenant-acme",
                          "overall_score":45.0}}},
        "expect_result": True,
        "check": lambda r: "NOT issued" in json.dumps(r),
        "check_msg": "Expected 'NOT issued' in response for below-threshold score",
    },

    # ── Agent 2: certificate_verify ────────────────────────────────────────
    {
        "name": "certificate_verify — valid UUID",
        "request": {"jsonrpc":"2.0","id":12,"method":"tools/call","params":{
            "name": "certificate_verify",
            "arguments": {"certificate_id": VALID_CERT_ID, "verifier_ip":"203.0.113.5"}}},
        "expect_result": True,
    },

    # ── Agent 3: certificate_revoke ────────────────────────────────────────
    {
        "name": "certificate_revoke — misconduct",
        "request": {"jsonrpc":"2.0","id":13,"method":"tools/call","params":{
            "name": "certificate_revoke",
            "arguments": {"certificate_id": VALID_CERT_ID,
                          "revocation_reason":"misconduct",
                          "admin_user_id":"admin-raj",
                          "tenant_id":"tenant-acme"}}},
        "expect_result": True,
    },

    # ── Agent 4: certificate_renew ─────────────────────────────────────────
    {
        "name": "certificate_renew",
        "request": {"jsonrpc":"2.0","id":14,"method":"tools/call","params":{
            "name": "certificate_renew",
            "arguments": {"certificate_id": VALID_CERT_ID2,
                          "candidate_id":"cand-003",
                          "tenant_id":"tenant-acme",
                          "renewal_reason":"re_assessment"}}},
        "expect_result": True,
    },

    # ── Agent 5: belt_eligibility_check ────────────────────────────────────
    {
        "name": "belt_eligibility_check — Silver candidate",
        "request": {"jsonrpc":"2.0","id":15,"method":"tools/call","params":{
            "name": "belt_eligibility_check",
            "arguments": {"candidate_id":"cand-004","overall_score":78.0,
                          "communication_score":80.0,"technical_score":76.0,
                          "problem_solving_score":78.0}}},
        "expect_result": True,
    },

    # ── Agent 6: blockchain_anchor ─────────────────────────────────────────
    {
        "name": "blockchain_anchor — hyperledger-fabric",
        "request": {"jsonrpc":"2.0","id":16,"method":"tools/call","params":{
            "name": "blockchain_anchor",
            "arguments": {"certificate_id": VALID_CERT_ID2,
                          "blockchain_network":"hyperledger-fabric"}}},
        "expect_result": True,
    },
    {
        "name": "blockchain_anchor — network=none (skip)",
        "request": {"jsonrpc":"2.0","id":17,"method":"tools/call","params":{
            "name": "blockchain_anchor",
            "arguments": {"certificate_id": VALID_CERT_ID2, "blockchain_network":"none"}}},
        "expect_result": True,
    },

    # ── Agent 7: certificate_share ─────────────────────────────────────────
    {
        "name": "certificate_share — LinkedIn with consent",
        "request": {"jsonrpc":"2.0","id":18,"method":"tools/call","params":{
            "name": "certificate_share",
            "arguments": {"certificate_id": VALID_CERT_ID2, "candidate_id":"cand-003",
                          "channel":"linkedin","candidate_consent":True}}},
        "expect_result": True,
    },
    {
        "name": "certificate_share — no consent is rejected",
        "request": {"jsonrpc":"2.0","id":19,"method":"tools/call","params":{
            "name": "certificate_share",
            "arguments": {"certificate_id": VALID_CERT_ID2, "candidate_id":"cand-003",
                          "channel":"twitter","candidate_consent":False}}},
        "expect_result": True,
        "check": lambda r: "consent" in json.dumps(r).lower(),
        "check_msg": "Expected consent error message",
    },

    # ── Agent 8: certificate_template_manage ───────────────────────────────
    {
        "name": "certificate_template_manage — list",
        "request": {"jsonrpc":"2.0","id":20,"method":"tools/call","params":{
            "name": "certificate_template_manage",
            "arguments": {"action":"list","tenant_id":"tenant-acme"}}},
        "expect_result": True,
    },

    # ── Agent 9: key_management ─────────────────────────────────────────────
    {
        "name": "key_management — status",
        "request": {"jsonrpc":"2.0","id":21,"method":"tools/call","params":{
            "name": "key_management",
            "arguments": {"action":"status","tenant_id":"tenant-acme"}}},
        "expect_result": True,
    },
    {
        "name": "key_management — rotate ES256",
        "request": {"jsonrpc":"2.0","id":22,"method":"tools/call","params":{
            "name": "key_management",
            "arguments": {"action":"rotate","tenant_id":"tenant-acme","algorithm":"ES256"}}},
        "expect_result": True,
    },

    # ── Agent 10: credential_status_list ───────────────────────────────────
    {
        "name": "credential_status_list — stats",
        "request": {"jsonrpc":"2.0","id":23,"method":"tools/call","params":{
            "name": "credential_status_list",
            "arguments": {"action":"stats","tenant_id":"tenant-acme"}}},
        "expect_result": True,
    },
    {
        "name": "credential_status_list — query specific cert",
        "request": {"jsonrpc":"2.0","id":24,"method":"tools/call","params":{
            "name": "credential_status_list",
            "arguments": {"action":"query","tenant_id":"tenant-acme",
                          "certificate_id": VALID_CERT_ID2}}},
        "expect_result": True,
    },

    # ── Agent 11: certificate_audit_log ────────────────────────────────────
    {
        "name": "certificate_audit_log — tenant all events",
        "request": {"jsonrpc":"2.0","id":25,"method":"tools/call","params":{
            "name": "certificate_audit_log",
            "arguments": {"tenant_id":"tenant-acme","limit":10}}},
        "expect_result": True,
    },

    # ── Agent 12: certificate_analytics ────────────────────────────────────
    {
        "name": "certificate_analytics — summary",
        "request": {"jsonrpc":"2.0","id":26,"method":"tools/call","params":{
            "name": "certificate_analytics",
            "arguments": {"tenant_id":"tenant-acme","report_type":"summary"}}},
        "expect_result": True,
    },
    {
        "name": "certificate_analytics — anomaly_detection",
        "request": {"jsonrpc":"2.0","id":27,"method":"tools/call","params":{
            "name": "certificate_analytics",
            "arguments": {"tenant_id":"tenant-acme","report_type":"anomaly_detection"}}},
        "expect_result": True,
    },

    # ── Security tests ─────────────────────────────────────────────────────
    {
        "name": "SECURITY: shell injection in candidate_id blocked",
        "request": {"jsonrpc":"2.0","id":30,"method":"tools/call","params":{
            "name": "certificate_issue",
            "arguments": {"candidate_id":"cand; rm -rf /","tenant_id":"tenant-acme",
                          "overall_score":88.0}}},
        "expect_error": True,
    },
    {
        "name": "SECURITY: invalid belt level blocked",
        "request": {"jsonrpc":"2.0","id":31,"method":"tools/call","params":{
            "name": "certificate_issue",
            "arguments": {"candidate_id":"cand-001","tenant_id":"tenant-acme",
                          "overall_score":88.0,"belt_level":"super-diamond-hack"}}},
        "expect_error": True,
    },
    {
        "name": "SECURITY: malformed UUID certificate_id blocked",
        "request": {"jsonrpc":"2.0","id":32,"method":"tools/call","params":{
            "name": "certificate_verify",
            "arguments": {"certificate_id":"../../../etc/passwd"}}},
        "expect_error": True,
    },
    {
        "name": "SECURITY: invalid blockchain network blocked",
        "request": {"jsonrpc":"2.0","id":33,"method":"tools/call","params":{
            "name": "blockchain_anchor",
            "arguments": {"certificate_id": VALID_CERT_ID2,
                          "blockchain_network":"bitcoin-hack"}}},
        "expect_error": True,
    },
    {
        "name": "SECURITY: invalid revocation reason blocked",
        "request": {"jsonrpc":"2.0","id":34,"method":"tools/call","params":{
            "name": "certificate_revoke",
            "arguments": {"certificate_id": VALID_CERT_ID,
                          "revocation_reason":"because_i_said_so",
                          "admin_user_id":"admin-raj",
                          "tenant_id":"tenant-acme"}}},
        "expect_error": True,
    },
    {
        "name": "SECURITY: unknown method returns error",
        "request": {"jsonrpc":"2.0","id":35,"method":"vault/dump_keys","params":{}},
        "expect_error": True,
    },
]

# ── Runner ────────────────────────────────────────────────────────────────────

def run_via_jar(jar_path):
    stdin_data = "\n".join(json.dumps(t["request"]) for t in TESTS) + "\n"
    proc = subprocess.run(
        ["java", "-jar", jar_path],
        input=stdin_data.encode(), capture_output=True, timeout=30)
    return [l for l in proc.stdout.decode().splitlines() if l.strip()]

def print_payloads():
    print("ℹ  JAR not found — printing test payloads for manual review\n")
    for i, t in enumerate(TESTS, 1):
        print(f"Test {i:02d}: {t['name']}")
        print("  Request:", json.dumps(t["request"], separators=(",", ":")))
        print()

def evaluate(test, resp_str):
    try:
        r = json.loads(resp_str)
    except Exception:
        return False, "Could not parse response JSON"
    if VERBOSE:
        print(f"  Response: {json.dumps(r, indent=2)}")
    if test.get("expect_error"):
        return ("error" in r, f"Expected error, got: {r}") if "error" not in r else (True, "OK")
    if test.get("expect_result") and "result" not in r:
        return False, f"Expected result, got: {r}"
    fn = test.get("check")
    if fn and not fn(r):
        return False, test.get("check_msg", "Custom check failed")
    return True, "OK"

def main():
    jar = "target/mcp-cert-manager.jar"
    if not os.path.exists(jar):
        print(f"⚠  JAR not found at {jar}\n   Run: mvn package -q\n")
        print_payloads()
        return

    print(f"Running {len(TESTS)} tests against {jar}\n")
    responses = run_via_jar(jar)
    passed = failed = 0

    for i, (test, resp) in enumerate(zip(TESTS, responses)):
        ok, msg = evaluate(test, resp)
        print(f"  {'✅ PASS' if ok else '❌ FAIL'} [{i+1:02d}] {test['name']}")
        if not ok:
            print(f"       → {msg}")
            failed += 1
        else:
            passed += 1

    print(f"\n{'─'*55}")
    print(f"Results: {passed} passed / {failed} failed / {len(TESTS)} total")
    sys.exit(0 if failed == 0 else 1)

if __name__ == "__main__":
    main()
