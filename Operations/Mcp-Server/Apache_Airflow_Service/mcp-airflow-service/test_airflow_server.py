#!/usr/bin/env python3
"""
Ecoskiller Apache Airflow MCP Server — Test Suite
Tests all 28 tools via JSON-RPC 2.0 over stdio.

Usage:
  python3 test_airflow_server.py           # pass/fail
  python3 test_airflow_server.py --verbose # full JSON
"""

import json, subprocess, sys, os

VERBOSE = "--verbose" in sys.argv
JAR = os.path.join(os.path.dirname(__file__), "target", "mcp-airflow-service-1.0.0.jar")

DEV_ENV = {
    "JWT_STRICT_SIGNATURE": "false",
    "AUDIT_LOG_FILE": "/tmp/airflow-test-audit.log",
}

# Fake JWT: SUPER_ADMIN + OPS_LEAD, far-future expiry, correct audience
FAKE_TOKEN = (
    "eyJhbGciOiJub25lIn0"
    ".eyJzdWIiOiJ0ZXN0LW9wcyIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJTVVBFUl9BRE1JTiIsIk9QU19MRUFEI"
    "l19LCJleHAiOjk5OTk5OTk5OTksImlzcyI6Imh0dHBzOi8va2V5Y2xvYWsuZWNvc2tpbGxlci5pbnRlcm5hbC9yZW"
    "FsbXMvZWNvc2tpbGxlciIsImF1ZCI6ImFpcmZsb3ctc2VydmljZSJ9."
)

def send(proc, req):
    proc.stdin.write((json.dumps(req)+"\n").encode())
    proc.stdin.flush()
    return json.loads(proc.stdout.readline())

def meta(): return {"authorization": f"Bearer {FAKE_TOKEN}"}

def call(tool, args={}):
    return {"jsonrpc":"2.0","id":1,"method":"tools/call",
            "params":{"name":tool,"arguments":args},"_meta":meta()}

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

    print("\n── Ecoskiller Airflow MCP Server Tests ─────────────────────\n")

    test("initialize", {"jsonrpc":"2.0","id":1,"method":"initialize",
        "params":{"protocolVersion":"2024-11-05"},"_meta":meta()})
    test("ping", {"jsonrpc":"2.0","id":2,"method":"ping","_meta":meta()})
    test("tools/list", {"jsonrpc":"2.0","id":3,"method":"tools/list","params":{},"_meta":meta()})

    print("\n  [ DAG Management ]")
    for t,a in [
        ("dag_list",    {"search":"ecoskiller","only_active":True}),
        ("dag_get",     {"dag_id":"ecoskiller_billing_cycle_monthly"}),
        ("dag_pause",   {"dag_id":"ecoskiller_analytics_report_weekly","reason":"maintenance window"}),
        ("dag_unpause", {"dag_id":"ecoskiller_analytics_report_weekly","reason":"maintenance complete"}),
        ("dag_trigger", {"dag_id":"ecoskiller_analytics_report_weekly","reason":"manual test run"}),
        ("dag_delete",  {"dag_id":"test_obsolete_dag","reason":"cleanup old test DAG"}),
    ]: test(t, call(t,a))

    print("\n  [ DAG Runs ]")
    for t,a in [
        ("dag_run_list", {"dag_id":"ecoskiller_billing_cycle_monthly","state":"success","limit":10}),
        ("dag_run_get",  {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"scheduled__2026-03-01T00:00:00+00:00"}),
        ("dag_run_clear",{"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"scheduled__2026-03-01T00:00:00+00:00","reason":"retry failed tasks"}),
    ]: test(t, call(t,a))

    print("\n  [ Task Instances ]")
    for t,a in [
        ("task_instance_list",     {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","state":"all"}),
        ("task_instance_get",      {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","task_id":"publish_kafka_billing_cycle_started"}),
        ("task_instance_log",      {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","task_id":"read_tenant_usage_clickhouse","lines":50}),
        ("task_instance_clear",    {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","task_id":"publish_kafka_billing_cycle_started","reason":"Kafka connection failed, retrying"}),
        ("task_instance_set_state",{"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","task_id":"call_billing_service_api","new_state":"success","reason":"Manually confirmed success via API logs"}),
    ]: test(t, call(t,a))

    print("\n  [ XCom ]")
    for t,a in [
        ("xcom_get",  {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","task_id":"read_tenant_usage_clickhouse","xcom_key":"cycle_id"}),
        ("xcom_list", {"dag_id":"ecoskiller_billing_cycle_monthly","run_id":"run_001","task_id":"read_tenant_usage_clickhouse"}),
    ]: test(t, call(t,a))

    print("\n  [ Ecoskiller Scheduled DAGs ]")
    for t,a in [
        ("billing_cycle_trigger",          {"reason":"Manual billing cycle — missed auto-trigger","billing_month":"2026-03","dry_run":True}),
        ("analytics_report_weekly_trigger",{"reason":"Re-run after ClickHouse recovery","week_start":"2026-03-17"}),
        ("certificate_expiry_check_trigger",{"reason":"Manual check after batch upload","expiry_window_days":30}),
        ("data_retention_cleanup_trigger", {"reason":"Monthly DPDPA cleanup","dry_run":True}),
        ("database_maintenance_trigger",   {"reason":"Emergency vacuuum after bloat alert","include_postgres":True,"include_clickhouse":True}),
    ]: test(t, call(t,a))

    print("\n  [ Kafka Events ]")
    for t,a in [
        ("kafka_event_status",  {"dag_id":"ecoskiller_billing_cycle_monthly"}),
        ("kafka_event_publish", {"kafka_topic":"billing.cycle.started","payload":'{"cycle_id":"cyc_001","month":"2026-03"}',
                                  "dag_id":"ecoskiller_billing_cycle_monthly","reason":"replay test"}),
    ]: test(t, call(t,a))

    print("\n  [ Observability ]")
    for t,a in [
        ("airflow_health",         {}),
        ("dag_metrics",            {"dag_id":"ecoskiller_billing_cycle_monthly"}),
        ("kubernetes_pod_status",  {"pod_state":"Running"}),
    ]: test(t, call(t,a))

    print("\n  [ Connections & Variables ]")
    for t,a in [
        ("connection_list", {}),
        ("variable_get",    {"key":"ecoskiller_billing_retry_count"}),
        ("variable_get",    {"key":"billing_api_secret_key"}),  # should be masked
    ]: test(t, call(t,a))

    # Security: invalid token must be rejected
    print("\n  [ Security ]")
    bad = send(proc, {"jsonrpc":"2.0","id":99,"method":"tools/call",
        "params":{"name":"airflow_health","arguments":{}},
        "_meta":{"authorization":"Bearer invalid.token.here"}})
    blocked = "error" in bad
    print(f"  {'✅' if blocked else '❌'}  Invalid JWT rejected")
    if blocked: passed+=1
    else: failed+=1

    proc.stdin.close(); proc.wait()
    print(f"\n{'─'*60}")
    print(f"  Results: {passed} passed, {failed} failed  |  Total: {passed+failed}")
    print(f"{'─'*60}\n")
    sys.exit(0 if failed==0 else 1)

if __name__ == "__main__":
    run()
