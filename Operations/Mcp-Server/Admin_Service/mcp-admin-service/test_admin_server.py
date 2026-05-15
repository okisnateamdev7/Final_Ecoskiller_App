#!/usr/bin/env python3
"""
Ecoskiller Admin MCP Server — Test Suite
Tests all 37 tools via JSON-RPC 2.0 over stdio.

Usage:
  python3 test_admin_server.py           # pass/fail summary
  python3 test_admin_server.py --verbose # full JSON output
"""

import json
import subprocess
import sys
import os

VERBOSE = "--verbose" in sys.argv

JAR = os.path.join(os.path.dirname(__file__),
                   "target", "mcp-admin-service-1.0.0.jar")

# ── Dev-mode JWT (no signature required when JWT_STRICT_SIGNATURE=false) ──────
DEV_ENV = {
    "JWT_STRICT_SIGNATURE": "false",
    "AUDIT_LOG_FILE": "/tmp/test-audit.log",
    "KEYCLOAK_ISSUER": "https://keycloak.ecoskiller.internal/realms/ecoskiller",
}

# Fake Bearer token (base64 payload with SUPER_ADMIN role, far-future exp)
# Payload: {"sub":"test-admin","realm_access":{"roles":["SUPER_ADMIN"]},"exp":9999999999,"iss":"https://keycloak.ecoskiller.internal/realms/ecoskiller","aud":"admin-service"}
FAKE_TOKEN = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJ0ZXN0LWFkbWluIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlNVUEVSX0FETUlOIl19LCJleHAiOjk5OTk5OTk5OTksImlzcyI6Imh0dHBzOi8va2V5Y2xvYWsuZWNvc2tpbGxlci5pbnRlcm5hbC9yZWFsbXMvZWNvc2tpbGxlciIsImF1ZCI6ImFkbWluLXNlcnZpY2UifQ."

def send(proc, request: dict) -> dict:
    line = json.dumps(request) + "\n"
    proc.stdin.write(line.encode())
    proc.stdin.flush()
    raw = proc.stdout.readline()
    return json.loads(raw)

def meta():
    return {"authorization": f"Bearer {FAKE_TOKEN}"}

def run_tests():
    if not os.path.exists(JAR):
        print(f"❌  JAR not found: {JAR}")
        print("    Run: mvn package -DskipTests")
        sys.exit(1)

    env = {**os.environ, **DEV_ENV}
    proc = subprocess.Popen(
        ["java", "-jar", JAR],
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.DEVNULL,
        env=env,
    )

    passed = failed = 0

    def test(name, request):
        nonlocal passed, failed
        resp = send(proc, request)
        ok = "result" in resp and "error" not in resp
        status = "✅" if ok else "❌"
        print(f"  {status}  {name}")
        if VERBOSE:
            print(json.dumps(resp, indent=2))
        if ok:
            passed += 1
        else:
            failed += 1
            if not VERBOSE:
                print(f"       Error: {resp.get('error')}")

    print("\n── Ecoskiller Admin MCP Server Tests ──────────────────────\n")

    # initialize
    test("initialize", {
        "jsonrpc": "2.0", "id": 1, "method": "initialize",
        "params": {"protocolVersion": "2024-11-05"},
        "_meta": meta()
    })

    # ping
    test("ping", {"jsonrpc": "2.0", "id": 2, "method": "ping", "_meta": meta()})

    # tools/list
    test("tools/list", {"jsonrpc": "2.0", "id": 3, "method": "tools/list", "params": {}, "_meta": meta()})

    # ── Recruiter tools ───────────────────────────────────────────────────────
    print("\n  [ Recruiter Management ]")
    for tool, args in [
        ("recruiter_create",       {"email":"test@co.com","company":"TestCo","tier":"STARTER"}),
        ("recruiter_list",         {"tier":"ALL","page":1,"page_size":10}),
        ("recruiter_get",          {"recruiter_id":"rec_test001"}),
        ("recruiter_update",       {"recruiter_id":"rec_test001","tier":"PROFESSIONAL","reason":"upgrade"}),
        ("recruiter_suspend",      {"recruiter_id":"rec_test001","reason":"policy violation"}),
        ("recruiter_delete",       {"recruiter_id":"rec_test001","reason":"account closure"}),
        ("recruiter_quota_adjust", {"recruiter_id":"rec_test001","max_jobs":20,"reason":"tier upgrade"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":10,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── DPDPA / Compliance ────────────────────────────────────────────────────
    print("\n  [ DPDPA / Compliance ]")
    for tool, args in [
        ("dsar_create",    {"subject_id":"cand_001","requester_email":"user@example.com","request_type":"DSAR"}),
        ("dsar_list",      {"status":"PENDING"}),
        ("dsar_get",       {"dsar_id":"dsar_test001"}),
        ("dsar_complete",  {"dsar_id":"dsar_test001","email_sent":True}),
        ("erasure_create", {"candidate_id":"cand_001","erasure_type":"FULL","legal_basis":"DPDPA Art.13"}),
        ("erasure_list",   {"status":"ALL"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":20,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── Audit Log ─────────────────────────────────────────────────────────────
    print("\n  [ Audit Log ]")
    test("audit_log_query", {"jsonrpc":"2.0","id":30,"method":"tools/call",
        "params":{"name":"audit_log_query","arguments":{"action":"recruiter_create","page":1}},"_meta":meta()})

    # ── Questions ─────────────────────────────────────────────────────────────
    print("\n  [ Questions & Content ]")
    for tool, args in [
        ("question_list",          {"category":"SWE","difficulty":3}),
        ("question_create",        {"question_text":"Describe SOLID principles","category":"SWE","difficulty":3}),
        ("question_update",        {"question_id":"q_test001","difficulty":4,"reason":"difficulty calibration"}),
        ("question_delete",        {"question_id":"q_test001","reason":"outdated"}),
        ("question_ab_test_create",{"question_id":"q_test001","variant_b_text":"Explain OOP principles","test_duration_days":14}),
        ("question_analytics",     {"question_id":"q_test001"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":40,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── Scoring Models ────────────────────────────────────────────────────────
    print("\n  [ Scoring Models ]")
    for tool, args in [
        ("model_list",     {"status":"ALL"}),
        ("model_metrics",  {"model_id":"model_v2"}),
        ("model_promote",  {"model_id":"model_v2","target_stage":"STAGING","traffic_pct":10,"reason":"accuracy improved"}),
        ("model_rollback", {"model_id":"model_v2","reason":"bias detected"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":50,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── Score Override ────────────────────────────────────────────────────────
    print("\n  [ Score Override ]")
    test("score_override", {"jsonrpc":"2.0","id":60,"method":"tools/call",
        "params":{"name":"score_override","arguments":{"score_id":"scr_001","original_score":72,"new_score":80,"reason":"GLITCHY_AUDIO"}},"_meta":meta()})

    # ── System Health ─────────────────────────────────────────────────────────
    print("\n  [ System Health ]")
    for tool, args in [
        ("health_services",  {}),
        ("health_kafka",     {}),
        ("health_database",  {}),
        ("health_kubernetes",{"namespace":"ops"}),
        ("service_logs",     {"service_name":"scoring-engine","lines":50,"level":"ERROR"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":70,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── Alerts ────────────────────────────────────────────────────────────────
    print("\n  [ Alerts & Configuration ]")
    for tool, args in [
        ("alert_list",        {"severity":"ALL"}),
        ("alert_rules_list",  {}),
        ("alert_rule_create", {"metric":"kafka_lag","operator":"GT","threshold":10000,"action":"PAGERDUTY","severity":"P1"}),
        ("alert_rule_update", {"rule_id":"rule_001","threshold":15000,"reason":"adjusted threshold"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":80,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── Reports & Exports ─────────────────────────────────────────────────────
    print("\n  [ Reports & Exports ]")
    for tool, args in [
        ("report_generate",              {"report_type":"REVENUE","format":"CSV","from_date":"2026-01-01","to_date":"2026-03-31"}),
        ("report_recruiter_performance", {"from_date":"2026-01-01","to_date":"2026-03-31"}),
        ("report_assessment_metrics",    {"from_date":"2026-01-01","to_date":"2026-03-31"}),
        ("report_compliance",            {"from_date":"2026-01-01","to_date":"2026-03-31"}),
        ("data_export",                  {"entity_type":"AUDIT_LOG","format":"JSON","legal_reason":"Legal discovery"}),
    ]:
        test(tool, {"jsonrpc":"2.0","id":90,"method":"tools/call",
                    "params":{"name":tool,"arguments":args},"_meta":meta()})

    # ── Billing ───────────────────────────────────────────────────────────────
    print("\n  [ Billing ]")
    test("invoice_adjust", {"jsonrpc":"2.0","id":100,"method":"tools/call",
        "params":{"name":"invoice_adjust","arguments":{"invoice_id":"inv_001","adjustment_type":"REFUND","amount_inr":500000,"reason":"service downtime credit"}},"_meta":meta()})

    # ── Security test: bad token ──────────────────────────────────────────────
    print("\n  [ Security ]")
    bad_resp = send(proc, {"jsonrpc":"2.0","id":200,"method":"tools/call",
        "params":{"name":"recruiter_list","arguments":{}},
        "_meta":{"authorization":"Bearer invalid.token.here"}})
    blocked = "error" in bad_resp
    status = "✅" if blocked else "❌"
    print(f"  {status}  Invalid JWT rejected")
    if blocked: passed += 1
    else: failed += 1

    proc.stdin.close()
    proc.wait()

    print(f"\n──────────────────────────────────────────────────────────")
    print(f"  Results: {passed} passed, {failed} failed  |  Total: {passed+failed}")
    print(f"──────────────────────────────────────────────────────────\n")
    sys.exit(0 if failed == 0 else 1)

if __name__ == "__main__":
    run_tests()
