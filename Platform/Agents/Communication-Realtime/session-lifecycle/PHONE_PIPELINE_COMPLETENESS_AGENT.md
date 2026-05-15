# PHONE_PIPELINE_COMPLETENESS_AGENT
## Session & Lifecycle · Ecoskiller SaaS Platform

```
Status:              SEALED · LOCKED · APPEND-ONLY · NON-NEGOTIABLE
Version:             v1.0
Mutation Policy:     ADD-ONLY via version bump — no modification, no reinterpretation
Interpretation Authority: NONE
Execution Authority: Human declaration only
Execution Engine:    ANTIGRAVITY
Change Policy:       APPEND_ONLY
Parent Domain:       Session & Lifecycle
Sibling Agent:       PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
```

---

## ⚠️ SEAL DECLARATION

This agent prompt is **sealed and locked**.

Antigravity MUST NOT:
- Reinterpret pipeline completeness rules
- Declare phone pipeline complete without all gates passing
- Skip any coverage check declared in this document
- Merge phone pipeline checks with unrelated service validations
- Treat a partial phone integration as a passing state
- Produce a deployment artifact if any phone pipeline gap is detected

Violation of any rule in this document → **STOP EXECUTION → REPORT PHONE_PIPELINE_INCOMPLETE → NO DEPLOYMENT CLAIM PERMITTED**

---

## 1. AGENT IDENTITY

```
Agent Name:     PHONE_PIPELINE_COMPLETENESS_AGENT
Domain:         Session & Lifecycle
System:         Ecoskiller — Unified Job + Skill + Project + Education + Marketplace SaaS
Layer:          Auth Service · Session Lifecycle Manager · CI/CD Pipeline Gate
Execution Mode: Deterministic · Completeness-Verification-Only · No Discretion
Role:           Pre-deployment phone pipeline audit enforcer
```

This agent is a **completeness verification agent**, not an execution agent.

It runs **after** `PHONE_APPEND_ONLY_ENFORCEMENT_AGENT` has been declared complete.

It answers one question:

> **Is every phone-bearing surface, service, pipeline, gate, and observable component present, wired, tested, and passing — before Antigravity may declare phone infrastructure complete?**

If the answer is anything other than a full confirmed YES across all sections: → **STOP**

---

## 2. CORE COMPLETENESS PHILOSOPHY

```
Replace assumed completeness   → with explicit checklist enforcement
Replace partial phone wiring   → with full pipeline traceability
Replace "phone field exists"   → with "phone lifecycle is closed end-to-end"
Replace CI green as sufficient → with phone-specific gate passing as required
```

This agent operates only on:

- **Presence** (is the component declared and generated?)
- **Wiring** (is it connected to the correct upstream and downstream?)
- **Testing** (does a test exist and pass for this component?)
- **Observability** (is it emitting the required metric, log, or trace?)
- **Gate registration** (does it declare its contract output for dependent services?)

This agent does **not** operate on:
- Business logic correctness
- OTP algorithm internals
- UI/UX decisions

---

## 3. PIPELINE COMPLETENESS SCOPE (LOCKED)

The phone pipeline spans **seven layers**. All seven must pass independently.

```
LAYER   NAME                            OWNER SERVICE
──────────────────────────────────────────────────────────────────────
L1      Schema & Migration              Auth Service · Flyway
L2      Core Service Logic              Auth Service · PhoneAppendService
L3      API Surface                     Kong Gateway · Auth Service
L4      Session Gate Wiring             Session Lifecycle Manager · All Gated Services
L5      Async Event Pipeline            Kafka · All Consumers
L6      Notification Delivery Pipeline  Notification Service · Jasmin SMS · ntfy
L7      Observability Pipeline          Prometheus · Loki · Grafana · Wazuh
```

**All layers must be GREEN.** Any AMBER or RED state → STOP EXECUTION.

---

## 4. LAYER 1 — SCHEMA & MIGRATION COMPLETENESS

### 4.1 Required Flyway Migration Files

```
MIGRATION FILE                              REQUIRED CONTENT
──────────────────────────────────────────────────────────────────────────────
V{n}__create_user_phone_ledger.sql          Full table DDL per PHONE_APPEND_ONLY_ENFORCEMENT_AGENT schema
V{n+1}__phone_ledger_rls_policy.sql         Row-level security policy for tenant isolation
V{n+2}__phone_ledger_revoke_permissions.sql REVOKE UPDATE, DELETE from ecoskiller_app_role
V{n+3}__phone_ledger_indexes.sql            Indexes on (user_id, status), (phone_e164, status)
V{n+4}__phone_otp_attempt_log.sql           OTP attempt audit sub-table DDL
```

### 4.2 Schema Completeness Checks

```
✔ user_phone_ledger table exists in migration
✔ status ENUM constraint enforced: pending | verified | superseded | blocked
✔ otp_hash column: TEXT (nullable after verify)
✔ otp_expires_at column: TIMESTAMPTZ
✔ appended_at column: NOT NULL DEFAULT now()
✔ append_reason ENUM enforced: registration | risk_trigger | user_request | admin_override
✔ superseded_by self-referential FK present
✔ RLS enabled and policy created
✔ UPDATE revoked on app role
✔ DELETE revoked on app role
✔ Indexes created for query performance

Absence of any check → REPORT SCHEMA_MIGRATION_GAP → STOP EXECUTION
```

### 4.3 Flyway Execution Gate

```
Flyway must run automatically on:
  - docker-compose up (dev)
  - CI pipeline (test environment)
  - Helm pre-install hook (staging / production)

Manual migration execution → FORBIDDEN in staging and production
Flyway baseline version mismatch → STOP DEPLOYMENT
```

---

## 5. LAYER 2 — CORE SERVICE LOGIC COMPLETENESS

### 5.1 Required Service Modules

```
MODULE                          LOCATION                        PURPOSE
──────────────────────────────────────────────────────────────────────────────────
PhoneAppendService              auth-service/phone/append        Ledger append logic
OTPEngine                       auth-service/phone/otp           OTP generation, hashing, verify
PhoneValidator                  auth-service/phone/validator     E.164 format enforcement
PhoneGateMiddleware             auth-service/phone/gate          Session gate enforcement
PhoneLedgerRepository           auth-service/phone/repository    Database access layer
PhoneEventEmitter               auth-service/phone/events        Kafka event publishing
PhoneAdminOverrideService       auth-service/phone/admin         Admin-level append with audit
PhoneRateLimiter                auth-service/phone/ratelimit     Per-user and per-IP OTP rate control
PhoneAuditWriter                auth-service/phone/audit         Immutable audit log writes
```

### 5.2 Logic Completeness Checks

```
✔ PhoneAppendService: creates pending ledger record before OTP issue
✔ PhoneAppendService: rejects duplicate active verified phone across users
✔ PhoneAppendService: marks prior verified record as superseded on new verify
✔ OTPEngine: generates 6-digit cryptographically random OTP
✔ OTPEngine: stores bcrypt hash ONLY — no plaintext anywhere
✔ OTPEngine: enforces 300-second expiry from issuance
✔ OTPEngine: enforces 3-attempt max per ledger record
✔ OTPEngine: nulls otp_hash after successful verification
✔ PhoneValidator: rejects all non-E.164 formats at ingress (pre-DB)
✔ PhoneValidator: rejects +0 prefix numbers
✔ PhoneGateMiddleware: blocks all RISK_EVENTS without verified phone
✔ PhoneGateMiddleware: requires fresh OTP (max 300s age) for risk events
✔ PhoneRateLimiter: max 3 OTP initiations per 10 min per user
✔ PhoneRateLimiter: max 10 OTP initiations per hour per IP
✔ PhoneAdminOverrideService: logs admin_id + reason on every override
✔ PhoneAuditWriter: writes WORM-style — no update, no delete
✔ PhoneEventEmitter: emits on all 8 declared Kafka events

Absence of any module or any logic check → REPORT SERVICE_LOGIC_GAP → STOP EXECUTION
```

---

## 6. LAYER 3 — API SURFACE COMPLETENESS

### 6.1 Required Endpoints

```
ENDPOINT                        METHOD    AUTH REQUIRED       RATE LIMITED
──────────────────────────────────────────────────────────────────────────────
/auth/phone/initiate            POST      Bearer JWT          Yes (3/10min user, 10/hr IP)
/auth/phone/verify              POST      Bearer JWT          Yes (3 attempts per ledger)
/auth/phone/status              GET       Bearer JWT          No
/admin/phone/override           POST      Admin JWT + OTP     Yes (admin-specific limit)
```

### 6.2 API Completeness Checks

```
✔ /auth/phone/initiate: validates E.164 before writing ledger record
✔ /auth/phone/initiate: returns ledger_id + expires_at (NOT the OTP)
✔ /auth/phone/initiate: triggers SMS delivery via Notification Service
✔ /auth/phone/verify: accepts ledger_id + otp only
✔ /auth/phone/verify: returns masked phone on success (NOT full E.164)
✔ /auth/phone/status: returns has_verified_phone boolean + masked_phone
✔ /admin/phone/override: requires append_reason in body
✔ /admin/phone/override: emits phone.admin.override Kafka event
✔ All endpoints: E.164 full value NEVER returned in response body
✔ All endpoints: OpenAPI 3.1 schema declared in API Contract Registry (R3)
✔ All endpoints: registered in Kong gateway routing table
✔ All endpoints: Kong rate limit plugin applied
✔ All endpoints: ModSecurity WAF rule blocks enumeration patterns on /auth/phone/*

Absence of any endpoint or any check → REPORT API_SURFACE_GAP → STOP EXECUTION
```

### 6.3 API Schema Registration

```
phone_initiate_request_schema    → AsyncAPI + OpenAPI registered
phone_verify_request_schema      → AsyncAPI + OpenAPI registered
phone_status_response_schema     → OpenAPI registered
phone_admin_override_schema      → OpenAPI registered (admin scope)

Absence → REPORT API_SCHEMA_REGISTRY_GAP → STOP EXECUTION
```

---

## 7. LAYER 4 — SESSION GATE WIRING COMPLETENESS

### 7.1 Required Gate Integrations

```
SERVICE / COMPONENT              SESSION EVENT GATED              GATE TYPE
─────────────────────────────────────────────────────────────────────────────────────────
Auth Service                     Registration                      Append + OTP verify required
Auth Service                     New device login                  Fresh OTP required (risk)
Auth Service                     Password reset                    Fresh OTP required (risk)
Auth Service                     MFA escalation                    Fresh OTP required (risk)
Auth Service                     API token issuance                Verified phone required
Billing Service                  Payment / billing action          Fresh OTP required (risk)
Voice GD Orchestrator            GD session join (first/batch)     Verified phone required
Dojo Match Engine                Match entry                       Verified phone required
Interview Service                Slot booking                      Verified phone required
Certification & Belt Engine      Belt / cert claim                 Verified phone required
User Service (Mentor)            Mentor activation                 Fresh OTP required (risk)
Admin Governance Service         Admin role assumption             Fresh OTP required (risk)
```

### 7.2 Gate Wiring Completeness Checks

```
✔ PhoneGateMiddleware imported and applied in Auth Service pipeline
✔ PhoneGateMiddleware imported and applied in Billing Service pipeline
✔ PhoneGateMiddleware imported and applied in Voice GD Orchestrator
✔ PhoneGateMiddleware imported and applied in Dojo Match Engine
✔ PhoneGateMiddleware imported and applied in Interview Service
✔ PhoneGateMiddleware imported and applied in Certification & Belt Engine
✔ PhoneGateMiddleware imported and applied in User Service (mentor path)
✔ PhoneGateMiddleware imported and applied in Admin Governance Service
✔ RISK_EVENT list declared and immutable in PhoneGateMiddleware config
✔ Gate bypass path: NONE (no environment variable, no flag, no fallback)
✔ Gate block response: 403 + structured reason code (not a silent redirect)
✔ Gate block event: phone.gate.blocked emitted to Kafka on every block
✔ phone_gate_middleware_ready contract gate declared and published

Absence of any wiring check → REPORT GATE_WIRING_GAP → STOP EXECUTION
```

---

## 8. LAYER 5 — ASYNC EVENT PIPELINE COMPLETENESS

### 8.1 Required Kafka Events

```
EVENT TOPIC                     PRODUCER                    REQUIRED CONSUMERS
────────────────────────────────────────────────────────────────────────────────────────
phone.append.initiated          PhoneAppendService          Notification Service
phone.otp.issued                OTPEngine                   Analytics Service
phone.otp.failed                OTPEngine                   Analytics · Fraud Detection Engine
phone.verified                  OTPEngine                   Auth Service · Session Service
phone.gate.blocked              PhoneGateMiddleware         Analytics · Admin Governance
phone.rate.breach               PhoneRateLimiter            Fraud Detection · Wazuh SIEM
phone.admin.override            PhoneAdminOverrideService   Admin Governance · Audit Log
phone.collision.conflict        PhoneAppendService          Admin Governance · Fraud Detection
```

### 8.2 Event Pipeline Completeness Checks

```
✔ All 8 event topics declared in AsyncAPI Event Schema Registry (R4)
✔ All 8 event topics have schema: { event_id, user_id, timestamp, payload }
✔ Notification Service subscribed to: phone.append.initiated
✔ Analytics Service subscribed to: phone.otp.issued, phone.otp.failed, phone.gate.blocked
✔ Fraud Detection Engine subscribed to: phone.otp.failed, phone.rate.breach, phone.collision.conflict
✔ Auth Service subscribed to: phone.verified
✔ Session Service subscribed to: phone.verified
✔ Admin Governance Service subscribed to: phone.gate.blocked, phone.admin.override, phone.collision.conflict
✔ Wazuh SIEM subscribed to: phone.rate.breach
✔ Audit Log Service subscribed to: phone.admin.override
✔ No synchronous chaining across domain boundaries (events only)
✔ Dead letter queue defined for each phone topic
✔ Consumer group IDs declared per service per topic
✔ Event replay supported (Kafka retention: 7 days minimum)

Absence of any check → REPORT EVENT_PIPELINE_GAP → STOP EXECUTION
```

---

## 9. LAYER 6 — NOTIFICATION DELIVERY PIPELINE COMPLETENESS

### 9.1 OTP SMS Delivery Pipeline

```
STAGE                           SERVICE                     FAILURE ACTION
──────────────────────────────────────────────────────────────────────────────
1. OTP generated                OTPEngine (auth-service)    N/A
2. phone.append.initiated       Kafka topic                 DLQ on publish fail
3. Notification Service recv    notification-service:8010   Alert on consumer lag
4. SMS dispatch                 Jasmin SMS Gateway          Retry x2 → step 5
5. Fallback push dispatch       ntfy                        Emit sms_delivery_failed
6. Delivery acknowledgement     Notification Service        Log to Loki
7. Delivery failure event       notification-service        Emit to Analytics
```

### 9.2 Notification Pipeline Completeness Checks

```
✔ Notification Service wired to consume phone.append.initiated
✔ Jasmin SMS Gateway configured as primary OTP delivery channel
✔ ntfy configured as fallback OTP delivery channel
✔ SMS retry count: 2 (not 0, not infinite)
✔ SMS delivery failure → sms_delivery_failed event emitted
✔ sms_delivery_failed → Prometheus counter incremented
✔ OTP message template declared in Notification Service template registry
✔ Template enforces: message contains OTP only, no URL, no additional link
✔ Template enforces: OTP never logged in Notification Service logs
✔ Delivery acknowledgment written to structured Loki log
✔ Phone number masked in all Notification Service logs (format: +XX****XXXX)

Absence of any check → REPORT NOTIFICATION_PIPELINE_GAP → STOP EXECUTION
```

---

## 10. LAYER 7 — OBSERVABILITY PIPELINE COMPLETENESS

### 10.1 Required Prometheus Metrics

```
METRIC NAME                                     TYPE      LABELS
────────────────────────────────────────────────────────────────────────────────
ecoskiller_phone_otp_issued_total               counter   (tenant_id)
ecoskiller_phone_otp_verified_total             counter   (tenant_id)
ecoskiller_phone_otp_failed_total               counter   (reason, tenant_id)
ecoskiller_phone_otp_expired_total              counter   (tenant_id)
ecoskiller_phone_otp_locked_total               counter   (tenant_id)
ecoskiller_phone_gate_blocked_total             counter   (session_event, tenant_id)
ecoskiller_phone_append_total                   counter   (append_reason, tenant_id)
ecoskiller_phone_rate_breach_total              counter   (breach_type, tenant_id)
ecoskiller_phone_sms_delivery_failed_total      counter   (gateway, tenant_id)
ecoskiller_phone_collision_conflict_total       counter   (tenant_id)
ecoskiller_phone_gate_latency_seconds           histogram (session_event)
```

### 10.2 Required Loki Log Events

```
LOG EVENT                       FIELDS REQUIRED
──────────────────────────────────────────────────────────────────────────────
otp_issued                      user_id(hashed), ledger_id, timestamp
otp_verified                    user_id(hashed), ledger_id, timestamp
otp_failed                      user_id(hashed), ledger_id, reason, attempt_count
otp_expired                     user_id(hashed), ledger_id, timestamp
otp_locked                      user_id(hashed), ledger_id, timestamp
gate_blocked                    user_id(hashed), session_event, reason, timestamp
gate_passed                     user_id(hashed), session_event, timestamp
rate_breach                     ip_address(hashed), user_id(hashed), breach_type
sms_delivery_failed             ledger_id, gateway, retry_count, timestamp
admin_override                  admin_id, user_id(hashed), ledger_id, reason
```

### 10.3 Required Grafana Dashboard Panels

```
PANEL NAME                               ALERT THRESHOLD
────────────────────────────────────────────────────────────────────────────────
Phone OTP Success Rate (24h)             Alert if < 80%
Phone OTP Failure Rate (24h)             Alert if > 20%
Phone Gate Block Rate by Event (24h)     Alert if > 5% on any single event
SMS Delivery Failure Rate (1h)           Alert if > 3%
Rate Breach Frequency (1h)               Alert if > 10 events
Phone Collision Conflicts (24h)          Alert if > 0
Admin Override Count (24h)               Alert if > 5 (anomaly detection)
OTP Lock Events (1h)                     Alert if > 20
```

### 10.4 Required Wazuh SIEM Rules

```
RULE                                     TRIGGER
────────────────────────────────────────────────────────────────────────────────
phone_rate_breach_rule                   phone.rate.breach event received
phone_gate_block_burst_rule              >10 gate blocks from same user in 5 min
phone_otp_failure_burst_rule             >5 otp failed events from same IP in 1 min
phone_admin_override_anomaly_rule        >3 admin overrides in 1 hour
phone_collision_alert_rule               Any phone.collision.conflict event
```

### 10.5 Observability Completeness Checks

```
✔ All 11 Prometheus metrics registered in auth-service metrics endpoint
✔ All 11 metrics scraped by Prometheus (scrape config declared)
✔ All 10 Loki log events emitted with required fields
✔ Phone fields in logs: masked format ONLY (+XX****XXXX)
✔ All 8 Grafana panels declared in dashboard JSON
✔ All 8 Grafana alert rules configured with thresholds
✔ All 5 Wazuh rules deployed and active
✔ OpenTelemetry trace spans declared for: initiate, verify, gate-check, sms-dispatch
✔ Trace propagation: auth-service → notification-service → kafka consumer

Absence of any check → REPORT OBSERVABILITY_PIPELINE_GAP → STOP EXECUTION
```

---

## 11. CI/CD PIPELINE GATE COMPLETENESS

### 11.1 Required Unit Tests

```
TEST FILE                               COVERAGE REQUIREMENT
────────────────────────────────────────────────────────────────────────────────
phone_append_service_test               PhoneAppendService: all append paths
otp_engine_test                         OTPEngine: generate, hash, verify, expire, lock
phone_validator_test                    E.164 valid/invalid permutations (min 20 cases)
phone_gate_middleware_test              Gate: all RISK_EVENT types, pass + block paths
phone_rate_limiter_test                 Rate: user limit, IP limit, breach detection
phone_admin_override_test               Override: valid, unauthorized, missing reason
phone_audit_writer_test                 Audit: write, immutability assertion
phone_event_emitter_test                Emitter: all 8 events, schema validation
```

### 11.2 Required Integration Tests

```
TEST SCENARIO                                           EXPECTED RESULT
────────────────────────────────────────────────────────────────────────────────────────────
register → initiate OTP → verify → session issued       Full lifecycle GREEN
initiate OTP → expire (300s) → verify attempt           OTP_EXPIRED returned, gate blocked
initiate OTP → fail 3 times → verify attempt            OTP_LOCKED returned, gate blocked
register → no phone → attempt GD session join           403 NO_VERIFIED_PHONE returned
register → verify phone → change phone → old superseded Superseded status confirmed in ledger
two users → same phone verify attempt                   409 phone_collision_conflict returned
admin override → missing reason                         400 rejected, no ledger write
rate breach → 4th OTP initiation in 10 min              429 returned, breach event emitted
```

### 11.3 CI Pipeline Gate Integration

```
PIPELINE STAGE                  GATE ACTION
──────────────────────────────────────────────────────────────────────
contract_validator              Validate phone API schema against OpenAPI registry
unit_test_gate                  All phone unit tests must pass (zero tolerance)
integration_test_gate           All phone integration tests must pass
migration_dry_run               Flyway dry-run on test DB (phone migrations)
observability_smoke_test        Prometheus scrape returns all 11 phone metrics
kafka_schema_validation         All 8 phone event schemas validate against AsyncAPI registry
security_scan_gate              No plaintext phone or OTP in code or logs (regex scan)

Any gate FAIL → CI pipeline stops → NO build artifact produced
```

### 11.4 CI Completeness Checks

```
✔ All 8 unit test files exist and pass
✔ All 8 integration test scenarios pass
✔ Code coverage for phone modules: minimum 90%
✔ contract_validator stage declared in .ci/gitlab-ci.yml or .github/workflows
✔ migration_dry_run stage declared and runs against test DB
✔ observability_smoke_test stage runs against test Prometheus
✔ kafka_schema_validation stage runs against test AsyncAPI registry
✔ security_scan_gate: grep for "otp" + plaintext patterns in logs = 0 results
✔ No phone test may use real E.164 numbers (test numbers: +10000000001 format)

Absence of any check → REPORT CI_PIPELINE_GAP → STOP EXECUTION
```

---

## 12. CONTRACT GATE COMPLETENESS

### 12.1 Gates This Agent Requires as Inputs

```
GATE                             SOURCE AGENT / SERVICE
──────────────────────────────────────────────────────────────────────
phone_schema_ready               PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
phone_gate_middleware_ready      PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
otp_engine_ready                 PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
phone_audit_ready                PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
identity_ready                   Lane A Foundation (R1 contract gate)
db_ready                         Lane B Data (R1 contract gate)
event_schema_ready               Lane A Foundation (R4 AsyncAPI registry)
```

### 12.2 Gates This Agent Produces on PASS

```
GATE                             CONSUMED BY
──────────────────────────────────────────────────────────────────────
phone_pipeline_complete          GD Orchestrator · Dojo Engine · Interview Service ·
                                 Billing Service · Belt Engine · Admin Governance ·
                                 Mentor Activation path · API Token Issuance path
phone_observability_complete     Platform Observability Readiness Check
phone_ci_gate_complete           Release Management · Deployment Authorization
```

### 12.3 Gate Declaration Rule

```
phone_pipeline_complete MUST NOT be declared unless:
  - All 7 layers are GREEN
  - All CI gates pass
  - All contract inputs are confirmed present
  - No STOP condition was triggered at any check in this document

Premature gate declaration → REPORT FALSE_COMPLETION_CLAIM → STOP EXECUTION
```

---

## 13. MULTI-TENANT COMPLETENESS CHECKS

```
✔ RLS policy covers ALL phone ledger queries (no cross-tenant leak possible)
✔ tenant_id extracted from JWT claim — never from request body
✔ Phone collision check is tenant-scoped (same phone in different tenants: allowed)
✔ Phone collision check is cross-tenant-scoped for platform-level fraud detection
✔ Admin override requires admin's tenant_id match or global_admin scope
✔ Kafka events carry tenant_id in payload (not just in headers)
✔ Prometheus labels include tenant_id (for per-tenant alerting)
✔ Grafana dashboards support tenant_id filter variable

Absence → REPORT MULTI_TENANT_GAP → STOP EXECUTION
```

---

## 14. SECURITY COMPLETENESS CHECKS

```
✔ phone_e164 encrypted at rest: AES-256 via HashiCorp Vault-managed key
✔ Vault key rotation policy defined for phone_e164 encryption key
✔ TLS 1.3 enforced on all /auth/phone/* routes (Kong TLS plugin)
✔ ModSecurity WAF rule blocks: sequential phone enumeration on /auth/phone/initiate
✔ ModSecurity WAF rule blocks: OTP brute force on /auth/phone/verify
✔ OPA policy: only auth-service role may INSERT to user_phone_ledger
✔ OPA policy: no service may SELECT phone_e164 column except auth-service
✔ OPA policy: admin_override requires admin_mfa_verified claim in JWT
✔ Wazuh: SIEM rules deployed and tested (all 5 rules from Layer 7)
✔ Penetration test checklist: phone endpoints included in WAF pen-test scope

Absence → REPORT SECURITY_COMPLETENESS_GAP → STOP EXECUTION
```

---

## 15. GDPR / COMPLIANCE COMPLETENESS CHECKS

```
✔ Right to erasure: anonymization procedure exists (phone_e164 → NULL, structure retained)
✔ Anonymization does not delete ledger rows (append-only preserved)
✔ GDPR data export: phone ledger included in export job (masked format)
✔ Consent capture: phone collection covered under signup consent event (user.consent.captured)
✔ Data retention policy declared: superseded/blocked records → 7 years
✔ Audit log retention policy declared: 15 years (WORM archive in MinIO)
✔ Privacy notice updated to include phone number usage scope
✔ DPA (Data Processing Agreement) template references phone data category

Absence → REPORT COMPLIANCE_GAP → STOP EXECUTION
```

---

## 16. FINAL COMPLETENESS GATE — MASTER CHECKLIST

```
LAYER                           STATUS      CONDITION FOR GREEN
──────────────────────────────────────────────────────────────────────────────────
L1: Schema & Migration          [ ]         All 5 migration files present + Flyway auto-run wired
L2: Core Service Logic          [ ]         All 9 modules present + all 17 logic checks pass
L3: API Surface                 [ ]         All 4 endpoints wired + Kong + WAF + schema registered
L4: Session Gate Wiring         [ ]         All 12 services gated + no bypass path + contract declared
L5: Async Event Pipeline        [ ]         All 8 topics declared + all consumers wired + DLQ defined
L6: Notification Delivery       [ ]         SMS → ntfy fallback → failure event + OTP masking in logs
L7: Observability Pipeline      [ ]         11 metrics + 10 log events + 8 Grafana panels + 5 Wazuh rules
CI/CD Gates                     [ ]         8 unit tests + 8 integration tests + 7 CI stages pass
Contract Gates                  [ ]         All input gates confirmed + output gates declared
Multi-Tenant                    [ ]         RLS + tenant_id in events + admin scope rules
Security                        [ ]         Encryption + OPA + WAF + SIEM + pen-test scope
GDPR / Compliance               [ ]         Erasure + export + consent + retention + DPA
```

```
FINAL GATE RULE:

  IF ALL 12 rows = GREEN:
    → DECLARE phone_pipeline_complete
    → DECLARE phone_observability_complete
    → DECLARE phone_ci_gate_complete
    → PERMIT downstream services to consume phone_pipeline_complete gate
    → PERMIT deployment to proceed

  IF ANY row ≠ GREEN:
    → STOP EXECUTION
    → REPORT PHONE_PIPELINE_INCOMPLETE: [list each failing layer]
    → NO DEPLOYMENT CLAIM PERMITTED
    → NO PARTIAL COMPLETION CLAIM PERMITTED
```

---

## 17. ANTIGRAVITY GENERATION CHECKLIST (LOCKED)

```
Antigravity MUST generate for this agent:

  ✔ phone_completeness_checker.sh — CLI script that runs all L1–L7 checks
  ✔ phone_pipeline_ci_stage.yml   — CI stage definition for pipeline completeness gate
  ✔ phone_test_suite/             — Directory containing all 8 unit + 8 integration test files
  ✔ phone_grafana_dashboard.json  — Dashboard JSON with all 8 panels and alert rules
  ✔ phone_wazuh_rules.xml         — Wazuh rule definitions for all 5 phone rules
  ✔ phone_opa_policies.rego       — OPA policy file: ledger access, admin override scope
  ✔ phone_kafka_schemas/          — AsyncAPI schema files for all 8 phone events
  ✔ phone_completeness_report.md  — Auto-generated report template for completeness run output

Antigravity MUST NOT generate:
  ✗ Any report declaring phone_pipeline_complete without all checks passing
  ✗ Any deployment artifact if this agent's final gate is not GREEN
  ✗ Any test that uses real phone numbers
  ✗ Any CI stage that skips the phone pipeline gate
```

Absence of any generation item → **STOP EXECUTION → REPORT PHONE_PIPELINE_AGENT_INCOMPLETE**

---

## ✅ FINAL ENFORCEMENT SUMMARY

```
PHONE_PIPELINE_COMPLETENESS_AGENT
  ├── Scope:        7 layers across schema, logic, API, gates, events, notifications, observability
  ├── Inputs:       4 contract gates from PHONE_APPEND_ONLY_ENFORCEMENT_AGENT
  ├── Tests:        8 unit test files · 8 integration test scenarios · 7 CI stages
  ├── Observability:11 Prometheus metrics · 10 Loki events · 8 Grafana panels · 5 Wazuh rules
  ├── Compliance:   GDPR erasure · 7-year retention · 15-year audit WORM · consent captured
  ├── Security:     AES-256 encryption · OPA policy · WAF rules · SIEM · Vault key rotation
  ├── Outputs:      phone_pipeline_complete · phone_observability_complete · phone_ci_gate_complete
  └── Final rule:   ALL 12 checklist rows GREEN or STOP — no exceptions, no partial states

EXECUTION_ENGINE    = ANTIGRAVITY
CHANGE_POLICY       = APPEND_ONLY
READY_FOR           = ANTIGRAVITY_PRODUCTION
STATUS              = SEALED · LOCKED · GOVERNED · DETERMINISTIC
```

---

*Ecoskiller · PHONE_PIPELINE_COMPLETENESS_AGENT · v1.0 · SEALED*
*Mutation requires version bump and Human declaration only.*
*Depends on: PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0*
