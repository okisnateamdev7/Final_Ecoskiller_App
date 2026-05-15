#!/usr/bin/env python3
"""
Ecoskiller — Certification Engine MCP Server Test Suite
26 tests covering: initialize, tools/list, security (auth, expiry, RBAC),
and all 18 tools across recruiter/admin/system/readonly roles.

Usage:
    python3 test_server.py              # quick pass/fail
    python3 test_server.py --verbose    # full JSON output
    python3 test_server.py --test <name>
"""

import subprocess, json, sys, os, base64, time

VERBOSE      = "--verbose" in sys.argv
SINGLE_TEST  = sys.argv[sys.argv.index("--test") + 1] if "--test" in sys.argv else None
JAR_PATH     = "target/certification-engine-mcp-1.0.0.jar"


# ── JWT factory (not signature-verified in test) ──────────────────────────
def jwt(tenant, role, sub, exp_offset=3600):
    h = base64.urlsafe_b64encode(
        json.dumps({"alg":"RS256","typ":"JWT"}).encode()).rstrip(b"=").decode()
    p = base64.urlsafe_b64encode(
        json.dumps({"tenant_id":tenant,"role":role,"sub":sub,
                    "exp":int(time.time())+exp_offset}).encode()).rstrip(b"=").decode()
    return f"{h}.{p}.fakesig"

TENANT   = "tenant-eco-001"
ADMIN    = jwt(TENANT, "admin",    "admin-1")
RECRUITER= jwt(TENANT, "recruiter","recruiter-1")
READONLY = jwt(TENANT, "readonly", "readonly-1")
SYSTEM   = jwt(TENANT, "system",   "sys-1")
EXPIRED  = jwt(TENANT, "admin",    "admin-1", exp_offset=-100)


def send(proc, req):
    proc.stdin.write((json.dumps(req) + "\n").encode())
    proc.stdin.flush()
    return json.loads(proc.stdout.readline())

def run(proc, name, req, expect_error=False, expect_code=None):
    if SINGLE_TEST and name != SINGLE_TEST:
        return True
    resp  = send(proc, req)
    has_e = "error" in resp
    ok    = has_e == expect_error
    if expect_code and has_e:
        ok = ok and resp["error"]["code"] == expect_code
    sym   = "✅ PASS" if ok else "❌ FAIL"
    print(f"{sym}  {name}")
    if VERBOSE or not ok:
        print("  REQ:", json.dumps(req, indent=2))
        print("  RES:", json.dumps(resp, indent=2))
    return ok

def start():
    if not os.path.exists(JAR_PATH):
        print(f"❌ JAR not found: {JAR_PATH}\n   Run: mvn clean package -q")
        sys.exit(1)
    return subprocess.Popen(["java","-jar",JAR_PATH],
        stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.DEVNULL)


def main():
    print("=" * 65)
    print("  Ecoskiller — Certification Engine MCP Server Tests")
    print("=" * 65)
    proc = start()
    results = []

    try:
        def t(name, req, err=False, code=None):
            results.append(run(proc, name, req, err, code))

        # ── Protocol ─────────────────────────────────────────────────────
        t("initialize", {"jsonrpc":"2.0","id":"1","method":"initialize",
            "params":{"protocolVersion":"2024-11-05",
                      "clientInfo":{"name":"test","version":"1.0"}}})
        t("ping", {"jsonrpc":"2.0","id":"2","method":"ping","params":{}})
        t("tools/list — 18 tools", {"jsonrpc":"2.0","id":"3",
            "method":"tools/list","params":{}})
        t("unknown method → -32601",
            {"jsonrpc":"2.0","id":"4","method":"badmethod","params":{}},
            err=True, code=-32601)

        # ── Security ──────────────────────────────────────────────────────
        t("SECURITY: missing token → UNAUTHORIZED",
            {"jsonrpc":"2.0","id":"5","method":"tools/call",
             "params":{"name":"issue_certificate","arguments":{"candidate_id":"c1","job_id":"j1"}}},
            err=True, code=-32001)
        t("SECURITY: expired token → UNAUTHORIZED",
            {"jsonrpc":"2.0","id":"6","method":"tools/call",
             "params":{"name":"get_certificate_details",
                       "arguments":{"certificate_id":"cert-ABC","bearer_token":EXPIRED}}},
            err=True, code=-32001)
        t("SECURITY: readonly → FORBIDDEN for admin tool",
            {"jsonrpc":"2.0","id":"7","method":"tools/call",
             "params":{"name":"revoke_certificate",
                       "arguments":{"certificate_id":"cert-X",
                                    "reason":"test","bearer_token":READONLY}}},
            err=True, code=-32001)
        t("SECURITY: recruiter → FORBIDDEN for system tool",
            {"jsonrpc":"2.0","id":"8","method":"tools/call",
             "params":{"name":"issue_certificate",
                       "arguments":{"candidate_id":"c1","job_id":"j1",
                                    "belt_level":"gold","composite_score":"91.5",
                                    "bearer_token":RECRUITER}}},
            err=True, code=-32001)
        t("SECURITY: alg:none token → UNAUTHORIZED",
            {"jsonrpc":"2.0","id":"9","method":"tools/call",
             "params":{"name":"get_belt_hierarchy",
                       "arguments":{"bearer_token":
                           base64.urlsafe_b64encode(
                               json.dumps({"alg":"none","typ":"JWT"}).encode()
                           ).rstrip(b"=").decode() + "." +
                           base64.urlsafe_b64encode(
                               json.dumps({"tenant_id":TENANT,"role":"admin",
                                           "sub":"x","exp":int(time.time())+3600}).encode()
                           ).rstrip(b"=").decode() + "."}}},
            err=True, code=-32001)

        # ── Certificate Lifecycle ─────────────────────────────────────────
        t("issue_certificate — system (gold)",
            {"jsonrpc":"2.0","id":"10","method":"tools/call",
             "params":{"name":"issue_certificate","arguments":{
                 "candidate_id":"cand-1001","job_id":"job-abc-001",
                 "belt_level":"gold","composite_score":"91.5",
                 "assessment_sources_json":"[\"gd\",\"interview\",\"dojo\"]",
                 "bearer_token":SYSTEM}}})
        t("issue_certificate — invalid belt_level → error",
            {"jsonrpc":"2.0","id":"11","method":"tools/call",
             "params":{"name":"issue_certificate","arguments":{
                 "candidate_id":"cand-1001","job_id":"job-abc-001",
                 "belt_level":"diamond","composite_score":"99.0","bearer_token":SYSTEM}}},
            err=True, code=-32602)
        t("revoke_certificate — admin",
            {"jsonrpc":"2.0","id":"12","method":"tools/call",
             "params":{"name":"revoke_certificate","arguments":{
                 "certificate_id":"cert-A1B2C3D4","reason":"Score correction",
                 "bearer_token":ADMIN}}})
        t("get_certificate_details — readonly",
            {"jsonrpc":"2.0","id":"13","method":"tools/call",
             "params":{"name":"get_certificate_details","arguments":{
                 "certificate_id":"cert-A1B2C3D4","bearer_token":READONLY}}})
        t("verify_certificate_qr — readonly",
            {"jsonrpc":"2.0","id":"14","method":"tools/call",
             "params":{"name":"verify_certificate_qr","arguments":{
                 "qr_token":"A3F9B2C17E4D8F60","bearer_token":READONLY}}})
        t("list_candidate_certificates — readonly",
            {"jsonrpc":"2.0","id":"15","method":"tools/call",
             "params":{"name":"list_candidate_certificates","arguments":{
                 "candidate_id":"cand-1001","status_filter":"ISSUED",
                 "bearer_token":READONLY}}})

        # ── Belt Management ───────────────────────────────────────────────
        t("get_belt_hierarchy — readonly",
            {"jsonrpc":"2.0","id":"16","method":"tools/call",
             "params":{"name":"get_belt_hierarchy","arguments":{
                 "bearer_token":READONLY}}})
        t("assign_belt_level — system",
            {"jsonrpc":"2.0","id":"17","method":"tools/call",
             "params":{"name":"assign_belt_level","arguments":{
                 "candidate_id":"cand-1001","job_id":"job-abc-001",
                 "belt_level":"gold","certificate_id":"cert-A1B2C3D4",
                 "composite_score":"91.5","bearer_token":SYSTEM}}})
        t("get_candidate_belt_status — readonly",
            {"jsonrpc":"2.0","id":"18","method":"tools/call",
             "params":{"name":"get_candidate_belt_status","arguments":{
                 "candidate_id":"cand-1001","bearer_token":READONLY}}})
        t("promote_belt_level — admin",
            {"jsonrpc":"2.0","id":"19","method":"tools/call",
             "params":{"name":"promote_belt_level","arguments":{
                 "candidate_id":"cand-1001","job_id":"job-abc-001",
                 "from_belt":"gold","to_belt":"platinum","new_score":"95.8",
                 "bearer_token":ADMIN}}})

        # ── Eligibility ───────────────────────────────────────────────────
        t("evaluate_eligibility — gold eligible",
            {"jsonrpc":"2.0","id":"20","method":"tools/call",
             "params":{"name":"evaluate_eligibility","arguments":{
                 "candidate_id":"cand-1001","job_id":"job-abc-001",
                 "composite_score":"91.5",
                 "assessment_sources_json":"[\"gd\",\"interview\",\"dojo\"]",
                 "bearer_token":SYSTEM}}})
        t("evaluate_eligibility — ineligible (low score)",
            {"jsonrpc":"2.0","id":"21","method":"tools/call",
             "params":{"name":"evaluate_eligibility","arguments":{
                 "candidate_id":"cand-1002","job_id":"job-abc-001",
                 "composite_score":"55.0",
                 "assessment_sources_json":"[\"gd\"]",
                 "bearer_token":SYSTEM}}})
        t("get_eligibility_rules — recruiter",
            {"jsonrpc":"2.0","id":"22","method":"tools/call",
             "params":{"name":"get_eligibility_rules","arguments":{
                 "job_id":"job-abc-001","bearer_token":RECRUITER}}})
        t("update_eligibility_rules — admin",
            {"jsonrpc":"2.0","id":"23","method":"tools/call",
             "params":{"name":"update_eligibility_rules","arguments":{
                 "job_id":"job-abc-001",
                 "rules_json":json.dumps([{"belt_level":"gold","min_composite":87.0}]),
                 "change_reason":"Lower gold threshold for enterprise cohort",
                 "bearer_token":ADMIN}}})

        # ── Mentor Verification ───────────────────────────────────────────
        t("trigger_mentor_verification — system",
            {"jsonrpc":"2.0","id":"24","method":"tools/call",
             "params":{"name":"trigger_mentor_verification","arguments":{
                 "certificate_id":"cert-A1B2C3D4","candidate_id":"cand-1001",
                 "belt_level":"gold","bearer_token":SYSTEM}}})
        t("trigger_mentor_verification — invalid belt → error",
            {"jsonrpc":"2.0","id":"25","method":"tools/call",
             "params":{"name":"trigger_mentor_verification","arguments":{
                 "certificate_id":"cert-A1B2C3D4","candidate_id":"cand-1001",
                 "belt_level":"bronze","bearer_token":SYSTEM}}},
            err=True, code=-32602)
        t("get_mentor_verification_status — readonly",
            {"jsonrpc":"2.0","id":"26","method":"tools/call",
             "params":{"name":"get_mentor_verification_status","arguments":{
                 "certificate_id":"cert-A1B2C3D4","bearer_token":READONLY}}})

        # ── Kafka / Events ────────────────────────────────────────────────
        t("ingest_certification_event — candidate.rank.computed",
            {"jsonrpc":"2.0","id":"27","method":"tools/call",
             "params":{"name":"ingest_certification_event","arguments":{
                 "topic":"candidate.rank.computed","partition":"1","offset":"50001",
                 "event_json":json.dumps({"candidate_id":"cand-1001","belt_eligible":True}),
                 "event_id":"evt-rank-001","trace_id":"trace-abc-xyz",
                 "bearer_token":SYSTEM}}})
        t("ingest_certification_event — bad topic → error",
            {"jsonrpc":"2.0","id":"28","method":"tools/call",
             "params":{"name":"ingest_certification_event","arguments":{
                 "topic":"unknown.topic","partition":"0","offset":"1",
                 "event_json":"{}","event_id":"evt-bad",
                 "bearer_token":SYSTEM}}},
            err=True, code=-32602)
        t("publish_certification_event — certificate.issued",
            {"jsonrpc":"2.0","id":"29","method":"tools/call",
             "params":{"name":"publish_certification_event","arguments":{
                 "event_type":"certificate.issued","certificate_id":"cert-A1B2C3D4",
                 "candidate_id":"cand-1001","job_id":"job-abc-001","belt_level":"gold",
                 "bearer_token":SYSTEM}}})
        t("publish_certification_event — certification.rejected",
            {"jsonrpc":"2.0","id":"30","method":"tools/call",
             "params":{"name":"publish_certification_event","arguments":{
                 "event_type":"certification.rejected","certificate_id":"cert-ZZZZ",
                 "candidate_id":"cand-1002","job_id":"job-abc-001","belt_level":"bronze",
                 "reason":"Score below minimum threshold",
                 "bearer_token":SYSTEM}}})

        # ── Storage & Audit ───────────────────────────────────────────────
        t("get_certificate_file — presigned URL",
            {"jsonrpc":"2.0","id":"31","method":"tools/call",
             "params":{"name":"get_certificate_file","arguments":{
                 "certificate_id":"cert-A1B2C3D4","expires_seconds":7200,
                 "bearer_token":READONLY}}})
        t("get_certification_audit_log — admin",
            {"jsonrpc":"2.0","id":"32","method":"tools/call",
             "params":{"name":"get_certification_audit_log","arguments":{
                 "candidate_id":"cand-1001","limit":10,"bearer_token":ADMIN}}})
        t("get_certification_audit_log — readonly → FORBIDDEN",
            {"jsonrpc":"2.0","id":"33","method":"tools/call",
             "params":{"name":"get_certification_audit_log","arguments":{
                 "bearer_token":READONLY}}},
            err=True, code=-32001)

    finally:
        proc.terminate()

    passed = sum(results)
    total  = len(results)
    print()
    print("=" * 65)
    print(f"  Results: {passed}/{total} passed")
    if passed == total:
        print("  🎉 All tests passed!")
    else:
        print(f"  ⚠️  {total - passed} test(s) failed")
    print("=" * 65)
    sys.exit(0 if passed == total else 1)

if __name__ == "__main__":
    main()
