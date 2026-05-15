#!/usr/bin/env python3
"""
Ecoskiller Analytics MCP Server — Test Suite
Tests all 32 tools via JSON-RPC 2.0 over stdio.

Usage:
  python3 test_analytics_server.py           # pass/fail summary
  python3 test_analytics_server.py --verbose # full JSON output
"""

import json, subprocess, sys, os

VERBOSE = "--verbose" in sys.argv
JAR = os.path.join(os.path.dirname(__file__), "target", "mcp-analytics-service-1.0.0.jar")

DEV_ENV = {
    "JWT_STRICT_SIGNATURE": "false",
    "AUDIT_LOG_FILE": "/tmp/analytics-test-audit.log",
}

# Fake JWT with SUPER_ADMIN + tenant_id
# Payload: {"sub":"test-user","realm_access":{"roles":["SUPER_ADMIN"]},"exp":9999999999,
#           "iss":"https://keycloak.ecoskiller.internal/realms/ecoskiller",
#           "aud":"analytics-service","tenant_id":"test-tenant"}
FAKE_TOKEN = (
    "eyJhbGciOiJub25lIn0"
    ".eyJzdWIiOiJ0ZXN0LXVzZXIiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiU1VQRVJfQURNSU4iXX0s"
    "ImV4cCI6OTk5OTk5OTk5OSwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5lY29za2lsbGVyLmludGVybmFsL3"
    "JlYWxtcy9lY29za2lsbGVyIiwiYXVkIjoiYW5hbHl0aWNzLXNlcnZpY2UiLCJ0ZW5hbnRfaWQiOiJ0ZXN0"
    "LXRlbmFudCJ9."
)

def send(proc, req):
    proc.stdin.write((json.dumps(req)+"\n").encode())
    proc.stdin.flush()
    return json.loads(proc.stdout.readline())

def meta(): return {"authorization": f"Bearer {FAKE_TOKEN}"}

def run():
    if not os.path.exists(JAR):
        print(f"❌  JAR not found: {JAR}\n    Run: mvn package -DskipTests"); sys.exit(1)

    env = {**os.environ, **DEV_ENV}
    proc = subprocess.Popen(["java","-jar",JAR],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.DEVNULL, env=env)

    passed = failed = 0

    def test(name, req):
        nonlocal passed, failed
        resp = send(proc, req)
        ok = "result" in resp and "error" not in resp
        print(f"  {'✅' if ok else '❌'}  {name}")
        if VERBOSE: print(json.dumps(resp, indent=2))
        elif not ok: print(f"       {resp.get('error')}")
        if ok: passed += 1
        else:  failed += 1

    def call(tool, args={}):
        return {"jsonrpc":"2.0","id":1,"method":"tools/call",
                "params":{"name":tool,"arguments":args},"_meta":meta()}

    print("\n── Ecoskiller Analytics MCP Server Tests ──────────────────\n")

    test("initialize", {"jsonrpc":"2.0","id":1,"method":"initialize",
        "params":{"protocolVersion":"2024-11-05"},"_meta":meta()})
    test("ping", {"jsonrpc":"2.0","id":2,"method":"ping","_meta":meta()})
    test("tools/list", {"jsonrpc":"2.0","id":3,"method":"tools/list","params":{},"_meta":meta()})

    print("\n  [ Dashboard ]")
    for t,a in [
        ("dashboard_get",       {"dashboard_id":"dash_001"}),
        ("dashboard_data",      {"dashboard_id":"dash_001","refresh":False}),
        ("dashboard_list",      {"type":"ALL"}),
        ("dashboard_create",    {"name":"My Dashboard","type":"RECRUITER"}),
    ]: test(t, call(t,a))

    print("\n  [ Funnel Analytics ]")
    for t,a in [
        ("funnel_job",       {"job_id":"job_001","from_date":"2026-01-01","to_date":"2026-03-31"}),
        ("funnel_platform",  {"period":"THIS_MONTH"}),
        ("funnel_cohort",    {"cohort_type":"TIME_PERIOD","periods":4,"period_unit":"MONTH"}),
    ]: test(t, call(t,a))

    print("\n  [ Recruiter Analytics ]")
    for t,a in [
        ("recruiter_performance",    {"recruiter_id":"rec_001","compare_to":"TEAM"}),
        ("recruiter_team_benchmark", {"metric":"HIRE_RATE","top_n":10}),
        ("recruiter_noshow_analysis",{"recruiter_id":"rec_001"}),
        ("recruiter_leaderboard",    {"top_n":20,"rank_by":"HIRE_RATE"}),
        ("recruiter_time_to_hire",   {"recruiter_id":"rec_001"}),
    ]: test(t, call(t,a))

    print("\n  [ Candidate Analytics ]")
    for t,a in [
        ("candidate_score_history",  {"candidate_id":"cand_001","assessment_type":"ALL"}),
        ("candidate_progress",       {"candidate_id":"cand_001"}),
        ("candidate_score_breakdown",{"candidate_id":"cand_001"}),
        ("candidate_belt_advancement",{"candidate_id":"cand_001"}),
    ]: test(t, call(t,a))

    print("\n  [ Platform Health & KPIs ]")
    for t,a in [
        ("platform_health",            {}),
        ("platform_kpi",               {"period":"THIS_MONTH","compare_previous":True}),
        ("platform_model_performance", {"from_date":"2026-01-01","to_date":"2026-03-31"}),
    ]: test(t, call(t,a))

    print("\n  [ Kafka / Event Ingestion ]")
    for t,a in [
        ("kafka_consumer_lag",       {}),
        ("event_ingestion_status",   {}),
        ("event_ingestion_history",  {"from_date":"2026-01-01","granularity":"DAY"}),
    ]: test(t, call(t,a))

    print("\n  [ Jobs ]")
    for t,a in [
        ("job_leaderboard",   {"top_n":10,"rank_by":"HIRES"}),
        ("job_funnel_daily",  {"job_id":"job_001","days":30}),
    ]: test(t, call(t,a))

    print("\n  [ Anomaly Detection & Alerts ]")
    for t,a in [
        ("anomaly_detect",    {"metric":"ALL","sensitivity":"MEDIUM"}),
        ("alert_rule_list",   {}),
        ("alert_rule_create", {"metric":"no_show_rate","operator":"GT","threshold":20,"channel":"EMAIL"}),
    ]: test(t, call(t,a))

    print("\n  [ Compliance & DPDPA ]")
    for t,a in [
        ("compliance_audit_log",   {"from_date":"2026-01-01","page":1,"page_size":20}),
        ("compliance_data_export", {"candidate_id":"cand_001","requester_email":"user@test.com"}),
        ("bias_monitoring",        {"dimension":"ALL","from_date":"2026-01-01"}),
    ]: test(t, call(t,a))

    print("\n  [ Reports & Metrics ]")
    for t,a in [
        ("report_export",       {"report_type":"FUNNEL","format":"CSV"}),
        ("metrics_prometheus",  {}),
    ]: test(t, call(t,a))

    # Security: bad token should be rejected
    print("\n  [ Security ]")
    bad = send(proc, {"jsonrpc":"2.0","id":99,"method":"tools/call",
        "params":{"name":"platform_health","arguments":{}},
        "_meta":{"authorization":"Bearer bad.token.here"}})
    blocked = "error" in bad
    print(f"  {'✅' if blocked else '❌'}  Invalid JWT rejected")
    if blocked: passed+=1
    else: failed+=1

    proc.stdin.close(); proc.wait()
    print(f"\n{'─'*58}")
    print(f"  Results: {passed} passed, {failed} failed  |  Total: {passed+failed}")
    print(f"{'─'*58}\n")
    sys.exit(0 if failed==0 else 1)

if __name__ == "__main__":
    run()
