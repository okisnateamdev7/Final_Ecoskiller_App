# 🔴 EMERGENCY_PLATFORM_LOCKDOWN_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED PRODUCTION ARTIFACT
### Document Class: Critical Infrastructure Agent Specification
### Mutation Policy: ADD-ONLY via signed version bump
### Execution Mode: DETERMINISTIC + VALIDATED
### Interpretation Authority: NONE
### Architecture Authority: LOCKED
### Version: v1.0.0-SEALED
### Seal Date: 2026-02-28T00:00:00Z
### Seal Authority: PLATFORM GOVERNANCE BOARD

---

> ⚠️ **THIS DOCUMENT IS SEALED.**
> No agent, human, or AI system may alter the logic, scope, thresholds, or execution policy of this agent without a formal version bump, governance board sign-off, and append-only changelog entry.
> Silent modification = SECURITY BREACH.
> Retroactive alteration of any execution record = COMPLIANCE VIOLATION.

---

## 🔒 SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:         EMERGENCY_PLATFORM_LOCKDOWN_AGENT
AGENT_ID:           EPLA-ANTIGRAVITY-001
SYSTEM_ROLE:        Platform Emergency Response & Lockdown Controller
PRIMARY_DOMAIN:     Platform Security · Tenant Integrity · Compliance Enforcement
EXECUTION_MODE:     Deterministic + Validated + Human-Override-Required
DATA_SCOPE:         Platform-Wide (All Tenants, All Domains, All Services)
TENANT_SCOPE:       STRICT ISOLATION — Cross-tenant action forbidden except platform-level kill
FAILURE_POLICY:     HALT ON AMBIGUITY → LOG INCIDENT → ESCALATE → NO PARTIAL ACTION
CREATIVE_INTERPRETATION: FORBIDDEN
ASSUMPTION_FILLING: FORBIDDEN
DEFAULT_BEHAVIOR:   DENY
DOMAIN_LOCK:        Arts | Commerce | Science | Technology | Administration
STACK_LOCK:         Flutter (Operational) + React Next.js (SEO Layer)
```

This agent must never assume missing specifications. If any required parameter is absent from an incoming trigger, execution halts immediately and the incident is logged with severity CRITICAL.

---

## 🔒 SECTION 2 — PURPOSE DECLARATION

### Problem Solved
The EMERGENCY_PLATFORM_LOCKDOWN_AGENT (EPLA) is the last line of defense for the Ecoskiller Antigravity SaaS platform. It is invoked when a critical threat, catastrophic failure, compliance violation, or coordinated attack is detected that cannot be resolved through normal operational recovery paths.

It is designed to:
- Instantly freeze all or scoped platform operations without data loss
- Preserve audit trail integrity under attack conditions
- Prevent cross-tenant data leakage during incident windows
- Protect billing, certification, scoring, and ranking integrity
- Maintain append-only log continuity even during shutdown
- Trigger coordinated downstream agent suspension
- Enable controlled, validated, staged recovery post-lockdown

### What Input It Consumes
- Threat signals from: `OBSERVABILITY_AGENT`, `SECURITY_AGENT`, `COMPLIANCE_AGENT`, `ANOMALY_DETECTION_AGENT`
- Manual emergency declarations from: `PLATFORM_GOVERNANCE_BOARD`, `PLATFORM_ADMIN`
- Automated breach signals from: `ZERO_TRUST_GATEWAY`, `WAF_LAYER`, `RATE_LIMIT_ENFORCER`
- Domain integrity violation signals from: `TENANT_ISOLATION_MONITOR`
- Scoring/certification fraud signals from: `COLLUSION_DETECTION_AGENT`, `SCORING_GOVERNANCE_AGENT`

### What Output It Produces
- `LOCKDOWN_STATE_RECORD` — Immutable incident snapshot
- `TENANT_SUSPENSION_EVENT` — Per-tenant or platform-wide
- `SERVICE_FREEZE_MANIFEST` — List of frozen services with timestamps
- `AUDIT_PRESERVATION_COMMAND` — Lock logs against deletion
- `RECOVERY_READINESS_TOKEN` — Issued only after validated checklist
- `ESCALATION_NOTICE` — Human operator + governance board alert

### Upstream Agent Dependencies
```
OBSERVABILITY_AGENT
SECURITY_AGENT
COMPLIANCE_AGENT
ANOMALY_DETECTION_AGENT
ZERO_TRUST_GATEWAY
WAF_LAYER
TENANT_ISOLATION_MONITOR
COLLUSION_DETECTION_AGENT
BILLING_INTEGRITY_AGENT
FEATURE_STORE_AGENT
SCORING_GOVERNANCE_AGENT
```

### Downstream Agents Controlled (Suspended on Lockdown)
```
MATCH_ENGINE
SCORING_ENGINE
RATING_ENGINE
BELT_ENGINE
TOURNAMENT_ENGINE
NOTIFICATION_SERVICE
BILLING_SERVICE
REPLAY_ENGINE
JOB_PORTAL_ENGINE
SKILL_DEVELOPMENT_ENGINE
PROJECT_EXECUTION_ENGINE
DOJO_ENGINE
MARKETPLACE_ENGINE
AI_MATCHING_ENGINE
RANK_UPDATE_AGENT
XP_UPDATE_AGENT
IDEA_DNA_AGENT
ROYALTY_ENGINE
```

---

## 🔒 SECTION 3 — INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "trigger_id",
    "trigger_source_agent",
    "trigger_timestamp_utc",
    "threat_class",
    "threat_severity",
    "affected_scope",
    "requesting_actor_id",
    "requesting_actor_role",
    "tenant_id_list",
    "domain_list",
    "evidence_reference_hash"
  ],
  "optional_fields": [
    "affected_match_ids",
    "affected_user_ids",
    "collateral_agent_list",
    "override_reason_text",
    "human_operator_id"
  ],
  "validation_rules": [
    "trigger_id must be UUID v4",
    "trigger_timestamp_utc must be ISO 8601 UTC",
    "threat_severity must be one of [LOW, MEDIUM, HIGH, CRITICAL, CATASTROPHIC]",
    "affected_scope must be one of [TENANT, DOMAIN, SERVICE, PLATFORM]",
    "requesting_actor_role must be one of [PLATFORM_ADMIN, GOVERNANCE_BOARD, SECURITY_AGENT, COMPLIANCE_AGENT]",
    "evidence_reference_hash must be SHA-256",
    "No null values in required fields",
    "tenant_id_list must be non-empty array when scope != PLATFORM",
    "All fields must pass schema type validation before processing"
  ],
  "security_checks": [
    "Caller must present valid signed JWT with EMERGENCY_TRIGGER scope",
    "JWT must not be expired (max 60s validity for emergency tokens)",
    "Caller IP must match registered internal service registry",
    "RBAC: Role must have LOCKDOWN_EXECUTE permission",
    "Request signature validated against platform signing key",
    "Replay protection: trigger_id must not exist in deduplication store"
  ],
  "domain_checks": [
    "If scope=DOMAIN: domain_list must be valid registered platform domains",
    "If scope=TENANT: tenant_id_list must reference existing, active tenants",
    "Cross-domain contamination check must pass before scoped lockdown proceeds"
  ]
}
```

**Rules:**
- No null tolerance without explicit policy declaration
- Malformed requests are rejected with `INVALID_INPUT` log event
- All validation failures are logged to append-only incident store
- No partial execution on partial input — fail completely or succeed completely

---

## 🔒 SECTION 4 — OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "lockdown_record": {
    "lockdown_id": "UUID",
    "lockdown_timestamp_utc": "ISO 8601",
    "lockdown_scope": "TENANT | DOMAIN | SERVICE | PLATFORM",
    "affected_tenants": ["tenant_id_list"],
    "affected_domains": ["domain_list"],
    "suspended_services": ["service_name_list"],
    "frozen_audit_log_pointer": "UUID",
    "lockdown_reason_code": "ENUM",
    "threat_class": "string",
    "threat_severity": "ENUM"
  },
  "confidence_score": "0.0 - 1.0",
  "model_version": "string (e.g. EPLA-v1.0.0)",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "LOCKDOWN_CONFIRMED",
    "ESCALATION_DISPATCHED",
    "RECOVERY_GATE_OPEN",
    "PARTIAL_RESTORE_PERMITTED"
  ],
  "recovery_readiness_token": "UUID (issued only post-validated checklist)",
  "operator_alert": {
    "channel": ["EMAIL", "SMS", "INTERNAL_ALERT_BUS"],
    "severity": "CRITICAL",
    "recipients": ["PLATFORM_ADMIN", "GOVERNANCE_BOARD", "SECURITY_LEAD"]
  }
}
```

All outputs must include: Confidence score, Version reference, Traceability UUID, and Escalation targets.
Outputs with confidence below `0.85` for PLATFORM-scope lockdowns require human confirmation before full execution.

---

## 🔒 SECTION 5 — THREAT CLASSIFICATION TAXONOMY

The agent recognizes the following threat classes. Each class maps to a lockdown tier.

| Threat Class | Code | Lockdown Tier | Auto-Execute | Human Required |
|---|---|---|---|---|
| Security Breach (Active) | SEC-001 | PLATFORM | YES (immediate) | Post-facto confirmation |
| Data Exfiltration Detected | SEC-002 | PLATFORM | YES | Post-facto confirmation |
| Cross-Tenant Data Leak | SEC-003 | AFFECTED TENANTS | YES | YES within 5 min |
| Scoring Manipulation (Confirmed) | INT-001 | DOMAIN + TENANT | YES | YES within 10 min |
| Belt/Certification Fraud (Confirmed) | INT-002 | DOMAIN | YES | YES |
| Billing System Compromise | BIL-001 | PLATFORM | YES | Post-facto confirmation |
| Identity/Auth System Compromise | AUTH-001 | PLATFORM | YES | Post-facto confirmation |
| DDoS / Abuse (Threshold Breach) | ABS-001 | SERVICE | YES | YES within 15 min |
| AI Model Drift (Critical) | ML-001 | SERVICE (AI Layer) | NO | YES |
| Collusion Network (Confirmed >5 users) | FRD-001 | TENANT | YES | YES |
| Match Farming (Confirmed) | FRD-002 | TENANT | YES | YES |
| Mentor Certification Abuse | FRD-003 | MENTOR SCOPE | YES | YES |
| Compliance Audit Trigger | CMP-001 | READ-ONLY LOCK | NO | YES |
| Data Corruption Detected | DAT-001 | AFFECTED SERVICE | YES | YES |
| Ransomware/Malware Signal | MAL-001 | PLATFORM | YES (immediate) | Post-facto confirmation |
| Governance Override Attempt | GOV-001 | PLATFORM | YES (immediate) | Post-facto confirmation |

---

## 🔒 SECTION 6 — LOCKDOWN EXECUTION PHASES

Lockdown does not execute as a single atomic action. It follows a strictly ordered, irreversible-forward phase model.

### PHASE 0: SIGNAL VALIDATION (< 500ms)
```
- Validate trigger input schema
- Validate caller JWT + signature
- Deduplicate trigger_id
- Classify threat
- Determine lockdown scope
- If validation fails → REJECT + LOG + ALERT
```

### PHASE 1: AUDIT PRESERVATION (< 1 second)
```
- Issue FREEZE command to audit log stores
- Create point-in-time snapshot reference
- Lock all append-only logs against deletion or mutation
- Emit AUDIT_PRESERVED event to Observability Agent
- Confirm log seal via cryptographic hash
```
**→ PHASE 1 MUST COMPLETE BEFORE ANY SERVICE SUSPENSION BEGINS**

### PHASE 2: SESSION INVALIDATION (< 2 seconds)
```
- Invalidate all active JWT tokens platform-wide (or scoped)
- Revoke all active video room tokens (LiveKit)
- Force-close all active WebSocket connections
- Push "PLATFORM UNDER MAINTENANCE" state to Flutter apps
- Push maintenance banner to React SEO web layer
- Clear all cached session states
```

### PHASE 3: SERVICE SUSPENSION (< 5 seconds)
```
- Emit SUSPEND_EVENT to all downstream agents (see Section 2)
- Stop all active Dojo match rooms
- Freeze scoring writes
- Freeze belt/certification writes
- Freeze billing charge operations
- Freeze ranking and XP updates
- Freeze AI/ML model inference writes
- Set all service health probes to LOCKED state
- Kubernetes: Scale suspicious pods to 0 (scoped or platform-wide)
```

### PHASE 4: TENANT ISOLATION ENFORCEMENT (< 8 seconds)
```
- Enable hard row-level security override on all DB connections
- Force-flush cross-tenant query caches
- Close any open cross-tenant event streams
- Validate tenant isolation is confirmed per active connection audit
- Log isolation confirmation hash per tenant
```

### PHASE 5: ESCALATION DISPATCH (< 10 seconds)
```
- Send CRITICAL alert to PLATFORM_ADMIN (SMS + Email + Internal Bus)
- Send alert to GOVERNANCE_BOARD
- Send alert to SECURITY_LEAD
- Attach: lockdown_id, threat_class, affected_scope, audit_reference
- Start human response timer (per threat class SLA defined in Section 5)
```

### PHASE 6: LOCKDOWN STATE PERSISTENCE
```
- Write immutable LOCKDOWN_STATE_RECORD to append-only store
- Record: all phase completion timestamps
- Record: all service freeze confirmations
- Record: all session invalidation confirmations
- Record: all escalation dispatch confirmations
- Generate LOCKDOWN_AUDIT_SEAL (SHA-256 of full record)
```

---

## 🔒 SECTION 7 — ML / AI LOGIC LAYER

This agent is **ML-GOVERNED** with **AI-ASSISTED** anomaly correlation.

### ML Components (70–80% of signal processing)

```yaml
MODEL_TYPE: Anomaly Detection + Multi-Class Threat Classification
FEATURES_USED:
  - request_rate_delta_per_tenant
  - failed_auth_burst_count
  - cross_tenant_query_attempt_count
  - scoring_variance_spike_score
  - session_token_anomaly_flag
  - billing_charge_pattern_deviation
  - user_behavior_entropy_score
  - match_farming_index
  - mentor_collusion_graph_density
  - cert_approval_rate_spike
TRAINING_FREQUENCY: Weekly (rolling 90-day window)
DRIFT_DETECTION:
  - Monitor feature distribution shift (KL divergence threshold: 0.15)
  - Monitor precision/recall degradation (alert if F1 < 0.90)
  - Automatic retraining trigger on drift confirmation
VERSION_CONTROL:
  - All model versions immutable
  - model_version stored in every output
  - Rollback requires governance board approval
CONFIDENCE_THRESHOLD:
  - PLATFORM scope lockdown: >= 0.92 (or human confirmation required)
  - TENANT scope: >= 0.85
  - SERVICE scope: >= 0.75
```

### AI Components (20–30% — Semantic Correlation Only)

```yaml
AI_USAGE_SCOPE:
  - Correlate multi-signal threat narratives
  - Generate human-readable incident summary
  - Surface related historical incident patterns
  - Identify non-obvious cross-tenant threat linkage
RESTRICTIONS:
  - AI CANNOT trigger lockdown autonomously
  - AI CANNOT modify lockdown scope
  - AI CANNOT override ML confidence scores
  - AI assists in explanation only — decision authority stays with ML + Human
PROMPT_GOVERNANCE:
  - Versioned prompt templates only
  - No dynamic prompt construction
  - No creative interpretation
  - Deterministic structure enforced
```

---

## 🔒 SECTION 8 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:          Not applicable (emergency agent — low frequency, high priority)
LATENCY_TARGET:        Phase 0–5 complete in < 10 seconds end-to-end
MAX_CONCURRENCY:       1 active PLATFORM lockdown at a time (serialized by lockdown_id)
                       Up to 50 concurrent TENANT-scoped lockdowns
QUEUE_STRATEGY:        Priority queue — CATASTROPHIC > CRITICAL > HIGH
                       Deduplicated by trigger_id
EXECUTION_MODEL:       Stateless execution per trigger
                       State stored in append-only lockdown store
EVENT_TRIGGERS:
  - LOCKDOWN_INITIATED
  - AUDIT_PRESERVED
  - SESSION_INVALIDATED
  - SERVICES_SUSPENDED
  - TENANT_ISOLATED
  - ESCALATION_DISPATCHED
  - LOCKDOWN_SEALED
  - RECOVERY_GATE_OPEN
IDEMPOTENCY:           Guaranteed — duplicate trigger_id is rejected silently after first execution
```

---

## 🔒 SECTION 9 — SECURITY ENFORCEMENT

All controls below are mandatory. None are optional.

```
✅ Tenant isolation validation — enforced at Phase 4
✅ Domain isolation validation — enforced at Phase 4
✅ Role-based authorization — LOCKDOWN_EXECUTE permission required
✅ Signed JWT with EMERGENCY_TRIGGER scope — validated at Phase 0
✅ Replay protection — trigger_id deduplication enforced
✅ Encryption enforcement — all lockdown records AES-256 encrypted at rest
✅ Audit logging — append-only, cryptographically sealed
✅ Access log tracking — all lockdown API access logged
✅ WAF enforcement — lockdown trigger endpoint behind WAF
✅ Rate limiting — max 3 PLATFORM lockdown triggers per 5-minute window (abuse prevention)
✅ Secret management — no plaintext credentials in any lockdown config
✅ PII protection — PII fields masked in all lockdown log outputs
✅ Cross-tenant query ban — absolute, no exception during lockdown
```

**Zero-trust enforcement:** Every internal service calling this agent must re-authenticate per invocation. Service mesh mutual TLS required on all lockdown-related calls.

---

## 🔒 SECTION 10 — AUDIT & TRACEABILITY

Every execution must produce an immutable audit record:

```json
{
  "timestamp_utc": "ISO 8601",
  "actor_id": "string (agent or human)",
  "actor_role": "string",
  "input_hash": "SHA-256 of raw input payload",
  "output_hash": "SHA-256 of output record",
  "model_version": "EPLA-v1.0.0",
  "decision_path": [
    "SIGNAL_VALIDATED",
    "THREAT_CLASSIFIED",
    "SCOPE_DETERMINED",
    "AUDIT_PRESERVED",
    "SESSION_INVALIDATED",
    "SERVICES_SUSPENDED",
    "TENANT_ISOLATED",
    "ESCALATION_DISPATCHED",
    "LOCKDOWN_SEALED"
  ],
  "confidence_score": "0.0-1.0",
  "anomaly_flags": ["list of triggered anomaly codes"],
  "phase_timestamps": {
    "phase_0_ms": "integer",
    "phase_1_ms": "integer",
    "phase_2_ms": "integer",
    "phase_3_ms": "integer",
    "phase_4_ms": "integer",
    "phase_5_ms": "integer",
    "phase_6_ms": "integer"
  },
  "lockdown_audit_seal": "SHA-256"
}
```

**Logs are immutable. Deletion is architecturally impossible. Modification triggers tamper alert.**

---

## 🔒 SECTION 11 — FAILURE POLICY

No silent failures. No partial executions without logging.

| Failure Condition | Response |
|---|---|
| Invalid input schema | REJECT → LOG INVALID_INPUT → ALERT Security Agent |
| Missing required fields | STOP_EXECUTION → LOG MISSING_FIELDS → ESCALATE |
| JWT invalid / expired | REJECT → LOG AUTH_FAILURE → ALERT Security Agent |
| ML model unavailable | HALT → LOG MODEL_UNAVAILABLE → ESCALATE_TO: PLATFORM_ADMIN |
| AI service timeout | Continue without AI layer → LOG AI_TIMEOUT → Flag in output |
| Data corruption detected | STOP_EXECUTION → LOG CORRUPTION → ESCALATE_TO: DATA_INTEGRITY_TEAM |
| Confidence below threshold | Require human confirmation → LOG CONFIDENCE_BELOW_THRESHOLD → HOLD |
| Audit preservation failure | ABORT ALL PHASES → LOG AUDIT_FAILURE → ESCALATE_TO: GOVERNANCE_BOARD |
| Phase timeout breach | HALT + PARTIAL_STATE_LOG → ESCALATE_TO: PLATFORM_ADMIN |
| Duplicate trigger_id | REJECT silently → LOG DUPLICATE_TRIGGER |
| Governance agent offline | STOP_EXECUTION → LOG GOV_UNAVAILABLE → ESCALATE |

```yaml
STOP_EXECUTION: true
LOG_INCIDENT: true (all cases)
RETRY_POLICY:
  - Transient network failures: 3 retries, exponential backoff (500ms, 1s, 2s)
  - All other failures: NO RETRY — human intervention required
ESCALATE_TO:
  - L1: PLATFORM_ADMIN
  - L2: GOVERNANCE_BOARD
  - L3: SECURITY_LEAD
  - L4: CTO (CATASTROPHIC class only)
```

---

## 🔒 SECTION 12 — RECOVERY GATE PROTOCOL

Recovery from lockdown is **never automatic**. It requires a validated checklist and issued `RECOVERY_READINESS_TOKEN`.

### Pre-Recovery Checklist (ALL items must pass)

```
□ Threat root cause identified and documented
□ Affected scope fully mapped
□ Compromised sessions purged
□ Security patch applied (if applicable)
□ Tenant isolation confirmed clean
□ Scoring/certification integrity verified (if INT class threat)
□ Billing records audited (if BIL class threat)
□ Audit logs confirmed intact and unsealed for review
□ Human operator sign-off obtained
□ Governance board review completed (for PLATFORM scope)
□ Recovery scope defined (full vs. partial restore)
□ Staged recovery plan documented
□ Rollback plan documented
□ Communication plan for affected tenants prepared
```

### Recovery Stages

**STAGE 1 — Read-Only Restore**
- Re-enable read-only API access
- No write operations permitted
- Monitor for 15 minutes minimum

**STAGE 2 — Non-Critical Service Restore**
- Re-enable: Notifications, SEO web layer, public read endpoints
- Continue monitoring

**STAGE 3 — Core Service Restore (Human Authorization Required)**
- Re-enable: Match Engine, Scoring, Billing, Auth
- Requires `RECOVERY_READINESS_TOKEN` + human operator confirmation

**STAGE 4 — Full Restore**
- Re-enable all services
- Issue new session tokens to affected users
- Send tenant notifications
- Publish incident summary to platform status page

---

## 🔒 SECTION 13 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - OBSERVABILITY_AGENT (signal source)
  - SECURITY_AGENT (breach signal)
  - COMPLIANCE_AGENT (regulatory trigger)
  - ANOMALY_DETECTION_AGENT (ML signal)
  - TENANT_ISOLATION_MONITOR (isolation breach)
  - COLLUSION_DETECTION_AGENT (fraud signal)
  - BILLING_INTEGRITY_AGENT (billing signal)
  - ZERO_TRUST_GATEWAY (auth breach)
  - WAF_LAYER (attack signal)

DOWNSTREAM_AGENTS_CONTROLLED:
  - All engines listed in Section 2 (suspended on lockdown)

SIBLING_AGENTS_NOTIFIED:
  - FEATURE_STORE_AGENT (freeze feature writes)
  - IDEA_DNA_AGENT (freeze idea processing)
  - ROYALTY_ENGINE (freeze royalty calculation)
  - RANK_UPDATE_AGENT (freeze rank updates)
  - XP_UPDATE_AGENT (freeze XP updates)
  - COPY_DETECTION_ENGINE (freeze during lockdown)

EVENT_TRIGGERS_EMITTED:
  - LOCKDOWN_INITIATED
  - AUDIT_PRESERVED
  - SESSION_INVALIDATED
  - SERVICES_SUSPENDED
  - TENANT_ISOLATED
  - ESCALATION_DISPATCHED
  - LOCKDOWN_SEALED
  - RECOVERY_STAGE_1_OPEN
  - RECOVERY_STAGE_2_OPEN
  - RECOVERY_STAGE_3_OPEN
  - RECOVERY_STAGE_4_OPEN
  - LOCKDOWN_LIFTED
```

All events emitted as structured payloads with `lockdown_id`, `timestamp_utc`, `scope`, and `audit_reference`.

---

## 🔒 SECTION 14 — PASSIVE INTELLIGENCE COMPATIBILITY

When lockdown affects user behavior data, the following must be handled:

```yaml
EMIT_FEATURE_VECTOR:
  - FREEZE command to FEATURE_STORE_AGENT (suspend all feature writes during lockdown)
  - Post-recovery: Resume feature emission normally
  - Lockdown window: Flagged as data gap in feature store (do not impute)
  - Feature vector integrity audit required before ML models resume inference
```

No behavioral data collected during lockdown window may be used for belt progression, ranking, or certification decisions.

---

## 🔒 SECTION 15 — INNOVATION ECONOMY COMPATIBILITY

When lockdown affects the ideas/innovation layer:

```yaml
- IDEA_DNA_AGENT: Suspended during lockdown
- ROYALTY_ENGINE: Frozen — no new royalty calculations
- COPY_DETECTION_ENGINE: Frozen — queue held, not dropped
- SIMILARITY_HASH: All pending computations paused
- Post-recovery: Resume from last confirmed state (no data loss)
- All idea vectors held in queue with lockdown_id reference
```

---

## 🔒 SECTION 16 — GROWTH ENGINE HOOK (FREEZE PROTOCOL)

During lockdown, the following growth engine operations are frozen:

```yaml
RANK_UPDATE_EVENT: FROZEN
XP_UPDATE_EVENT: FROZEN
SHARE_TRIGGER_EVENT: FROZEN
BELT_PROMOTION: FORBIDDEN during lockdown
CERTIFICATION_ISSUE: FORBIDDEN during lockdown
TOURNAMENT_RESULT_WRITE: FORBIDDEN during lockdown
LEADERBOARD_UPDATE: FROZEN
```

Post-recovery, a retroactive audit of all pending events during the lockdown window must occur before any backfill is applied.

---

## 🔒 SECTION 17 — DOJO-SPECIFIC LOCKDOWN CONTROLS

Aligned with the Dojo SaaS production artifact:

```
Scoring Formula: FROZEN (no writes permitted)
Belt Engine: FROZEN (no promotions during lockdown)
Match Engine: ALL ACTIVE ROOMS FORCE-CLOSED
Video Rooms (LiveKit): ALL TOKENS REVOKED
WebSocket Connections: ALL FORCE-CLOSED
Mentor Commands: FROZEN (no override authority)
Replay Engine: READ-ONLY (no new replay jobs)
Tournament Engine: FROZEN (no result finalization)
Certification Engine: FROZEN (no certificate issuance)
Anti-Cheat Engine: ELEVATED ALERT MODE (remains active)
Behavioral Safety Engine: REMAINS ACTIVE (safety overrides lockdown restrictions)
```

**Safety overrides scoring, and safety override capability must remain available even during platform lockdown.**

---

## 🔒 SECTION 18 — PERFORMANCE MONITORING

```yaml
METRICS:
  - lockdown_trigger_success_rate
  - phase_completion_latency_p50_p95_p99
  - false_positive_lockdown_rate
  - mean_time_to_lockdown_seal (target: < 10 seconds)
  - mean_time_to_recovery_token_issue
  - escalation_response_time
  - audit_preservation_success_rate (target: 100%)
  - recovery_stage_completion_time

OBSERVABILITY_INTEGRATION:
  - All metrics streamed to OBSERVABILITY_AGENT
  - Grafana dashboard: ecoskiller.internal/lockdown-monitor
  - Prometheus metrics exported
  - Structured logs to centralized log aggregator

ALERTING:
  - Phase timeout breach → CRITICAL alert
  - Audit preservation failure → CATASTROPHIC alert
  - False positive rate > 2% → REVIEW trigger
  - Lockdown frequency anomaly → INVESTIGATE trigger
```

---

## 🔒 SECTION 19 — VERSIONING POLICY

```yaml
ALL CHANGES:
  - Add-only
  - Version-bumped
  - Backward compatible
  - Governance board reviewed
  - Migration documented
  - Rollback plan required before approval
  - Append-only changelog entry mandatory

VERSION FORMAT: EPLA-vMAJOR.MINOR.PATCH-SEALED
CURRENT VERSION: EPLA-v1.0.0-SEALED
NEXT VERSION GATE: Governance board vote + Security lead sign-off
```

---

## 🔒 SECTION 20 — NON-NEGOTIABLE RULES

This agent must NOT:

```
✗ Create hidden logic or undocumented execution paths
✗ Modify historical audit records (append-only is absolute)
✗ Auto-delete any audit logs under any condition
✗ Override governance agents
✗ Bypass compliance checks
✗ Mix domain data across tenant boundaries
✗ Execute outside declared scope
✗ Issue RECOVERY_READINESS_TOKEN without full checklist completion
✗ Proceed past Phase 1 if audit preservation fails
✗ Perform lockdown without valid authenticated trigger
✗ Allow AI components to make lockdown decisions autonomously
✗ Apply scoring/belt/cert backfill without post-lockdown audit
✗ Execute PLATFORM lockdown below 0.92 confidence without human confirmation
✗ Silence any failure — all failures logged and escalated
✗ Allow mentor override authority during active lockdown
```

---

## 🔒 SECTION 21 — COMMUNICATION PROTOCOL DURING LOCKDOWN

### User-Facing (Flutter App)
```
State: PLATFORM UNDER MAINTENANCE
Message: "We're performing an urgent platform update. Your progress is safe. We'll be back shortly."
Action: Block all write operations, allow session logout
```

### User-Facing (React SEO Web Layer)
```
State: Maintenance banner active
Public read pages: Remain accessible
Write endpoints: Disabled
```

### Tenant Admin Notification
```
Channel: Email + In-app notification
Content: Incident reference ID, affected scope, estimated timeline (if known), support contact
Timing: Within 5 minutes of lockdown seal
```

### Governance Board
```
Channel: Direct alert + Email + SMS
Content: Full lockdown record, confidence score, threat class, phases completed, recovery gate status
Timing: Immediate (within 10 seconds of LOCKDOWN_SEALED event)
```

---

## 🔒 SECTION 22 — COMPLIANCE & REGULATORY ALIGNMENT

```yaml
DATA_RESIDENCY: Lockdown records stored in region matching tenant data residency policy
GDPR_ALIGNMENT:
  - No additional PII collection during lockdown
  - PII fields masked in all lockdown logs
  - Right-to-access applies post-incident to affected users (via support workflow)
AUDIT_EXPORT: Available to compliance team post-incident in structured JSON format
RETENTION_POLICY: Lockdown audit records retained minimum 7 years (immutable)
REGULATORY_TRIGGER: If lockdown involves suspected data breach → GDPR Article 33 notification workflow triggered within 72 hours
```

---

## 🔒 SECTION 23 — ENVIRONMENT-SPECIFIC BEHAVIOR

Aligned with Ecoskiller Multi-Environment law:

| Environment | Lockdown Behavior |
|---|---|
| DEV | Full lockdown simulation available — no real service impact |
| TEST | CI gate: Lockdown simulation must pass in every build |
| STAGING | Full lockdown drill — real service suspension, no real user impact |
| PRODUCTION | Full live lockdown — all phases execute against live infrastructure |

Staging lockdown drills must be executed quarterly. Results documented and reviewed by governance board.

---

## 🔒 SECTION 24 — FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════╗
║     EMERGENCY_PLATFORM_LOCKDOWN_AGENT — GOVERNANCE SEAL      ║
╠══════════════════════════════════════════════════════════════╣
║ Version:           EPLA-v1.0.0-SEALED                        ║
║ Seal Date:         2026-02-28T00:00:00Z                      ║
║ Execution Mode:    DETERMINISTIC + VALIDATED                 ║
║ Mutation Policy:   ADD-ONLY VIA SIGNED VERSION BUMP          ║
║ Interpretation:    FORBIDDEN                                 ║
║ Default Behavior:  DENY                                      ║
║ Failure Mode:      HALT + LOG + ESCALATE                     ║
║ Audit Policy:      IMMUTABLE APPEND-ONLY                     ║
║ Tenant Isolation:  ABSOLUTE                                  ║
║ AI Decision Auth:  NONE (Assist only)                        ║
║ Auto-Recovery:     FORBIDDEN                                 ║
║ Silent Failure:    FORBIDDEN                                 ║
║ Human Override:    REQUIRED for PLATFORM scope               ║
║ Dojo Alignment:    LOCKED (Scoring/Belt/Cert frozen)         ║
║ Growth Engine:     FROZEN during lockdown                    ║
║ Safety Override:   ALWAYS ACTIVE (cannot be locked)         ║
╠══════════════════════════════════════════════════════════════╣
║  ECOSKILLER ANTIGRAVITY — EMERGENCY LOCKDOWN MODE ENABLED    ║
║  PLATFORM INTEGRITY: PROTECTED                               ║
║  TENANT ISOLATION: ENFORCED                                  ║
║  AUDIT TRAIL: SEALED                                         ║
║  GOVERNANCE BOARD: ACTIVE                                    ║
║  RECOVERY: HUMAN-GATED                                       ║
╚══════════════════════════════════════════════════════════════╝
```

---

*This document is a sealed production artifact for the Ecoskiller Antigravity platform. Any modification without a formal version bump, governance board approval, and append-only changelog entry constitutes a compliance violation and will be treated as an unauthorized mutation of critical infrastructure.*

**END OF SEALED DOCUMENT — EPLA-v1.0.0**
