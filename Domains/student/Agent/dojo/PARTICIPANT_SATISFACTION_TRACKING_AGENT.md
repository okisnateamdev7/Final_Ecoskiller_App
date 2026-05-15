# 🔒 PARTICIPANT_SATISFACTION_TRACKING_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ANTIGRAVITY PLATFORM — SEALED PRODUCTION SPEC v1.0

---

```
STATUS             : SEALED · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY    : ADD-ONLY VIA VERSION BUMP
CREATIVE_INTERP    : FORBIDDEN
ASSUMPTION_FILLING : FORBIDDEN
DEFAULT_BEHAVIOR   : DENY
FAILURE_MODE       : STOP → REPORT → NO PARTIAL OUTPUT
INTERPRETATION_AUTH: NONE
EXECUTION_AUTH     : Human Declaration Only
AGENT_CLASS        : Autonomous Background Agent (Non-Interactive)
AGENT_CATEGORY     : Enterprise Optimization + Trust Infrastructure
PLATFORM_BINDING   : ANTIGRAVITY (ECOSKILLER + DOJO + CAMPUS PORTAL + GD ENGINE)
```

---

## SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

| Field | Value |
|---|---|
| **Agent Name** | PARTICIPANT_SATISFACTION_TRACKING_AGENT |
| **Agent ID** | PSTA-ENT-001 |
| **Tier** | Enterprise Optimization Layer |
| **Sub-Layer** | Trust Infrastructure |
| **Scope** | Platform-Wide (All Participant Types) |
| **Mode** | Passive Collection + Active Analysis + Alert Emission |
| **Bias Policy** | Structurally Eliminated via Protocol |
| **Human Judgment** | NOT PERMITTED in scoring |
| **AI Interpretation** | NOT PERMITTED in raw scoring |
| **Output Type** | Deterministic Score + Structured Report + Event Emission |

---

## SECTION 2 — PARTICIPANT TAXONOMY (LOCKED)

This agent tracks satisfaction signals across all registered participant classes in the Antigravity platform ecosystem.

```
PARTICIPANT_CLASSES = [
  STUDENT,
  TRAINER / MENTOR,
  EVALUATOR,
  INSTITUTE (College / University / School),
  ENTERPRISE (SME / Corporate),
  RECRUITER / HR,
  PARENT (Read-Only Trust Layer),
  ADMIN (Tenant / Platform / Compliance),
  GD_PARTICIPANT (Voice Group Discussion Engine),
  AUTOMATION_AGENT (Non-Human — excluded from satisfaction scoring)
]
```

**Rule:** AUTOMATION_AGENT class is EXCLUDED from satisfaction scoring.
All other classes MUST produce satisfaction signals.
Absence of signal after mandatory interaction = `SILENT_DISSATISFACTION` flag.

---

## SECTION 3 — SATISFACTION SIGNAL TAXONOMY

### 3.1 Explicit Signals (Direct Collection)

| Signal ID | Signal Name | Trigger Point | Participant Classes |
|---|---|---|---|
| ES-01 | Post-Session Micro-Rating | End of GD session / Interview round | STUDENT, GD_PARTICIPANT |
| ES-02 | Post-Assessment Feedback Form | OA / Technical Round completion | STUDENT, EVALUATOR |
| ES-03 | Offer Letter Receipt Confirmation | Blockchain-verified offer received | STUDENT, PARENT |
| ES-04 | Mentor Session Rating | Post-mentorship session | STUDENT, TRAINER |
| ES-05 | Platform Usability Score | Monthly auto-prompt (max 1/month) | ALL except AUTOMATION_AGENT |
| ES-06 | Institute Placement Cell Rating | Post-drive completion | INSTITUTE, RECRUITER |
| ES-07 | Company Trust Verification Feedback | Post-KYC verification | ENTERPRISE, RECRUITER |
| ES-08 | Parent Progress Report Acknowledgement | Monthly report email opened + rated | PARENT |
| ES-09 | GD System Fairness Confirmation | Post-GD deterministic score received | GD_PARTICIPANT, STUDENT |
| ES-10 | Bias Incident Dispute Resolution | Post-bias alert resolution | STUDENT, EVALUATOR |

### 3.2 Implicit Signals (Behavioral Collection)

| Signal ID | Signal Name | Source | Weight |
|---|---|---|---|
| IS-01 | Platform Return Rate | Session logs (within 48h after event) | HIGH |
| IS-02 | Feature Re-Engagement | Re-use of flagged feature post-improvement | HIGH |
| IS-03 | Offer Acceptance Rate | Blockchain offer acceptance ledger | CRITICAL |
| IS-04 | Profile Completion Velocity | Post-onboarding 7-day delta | MEDIUM |
| IS-05 | Support Ticket Absence | 30-day window post-interaction | MEDIUM |
| IS-06 | GD Room Completion Rate | Session completed vs abandoned | HIGH |
| IS-07 | Mock Interview Re-Attempt Rate | AI Mock re-engagement | MEDIUM |
| IS-08 | Alumni Referral Initiation | Referral generated from satisfied user | HIGH |
| IS-09 | Notification Open Rate | Push / Email engagement ratio | LOW |
| IS-10 | Reporting Silence (Negative) | No complaint after positive signal expected | MEDIUM |

### 3.3 Systemic Signals (Infrastructure-Derived)

| Signal ID | Signal Name | Source System | Interpretation |
|---|---|---|---|
| SS-01 | GD Turn Completion Compliance | GD Orchestrator State Machine | Full compliance = satisfaction proxy |
| SS-02 | Network Drop Recovery Rate | WebRTC / Jitsi connection logs | Low recovery = frustration risk |
| SS-03 | Blockchain Offer Verification Latency | Blockchain ledger timestamps | >2s = trust friction event |
| SS-04 | AI Match Score Acceptance Rate | Job application post-recommendation | Accepted = AI trust signal |
| SS-05 | Bias Alert Resolution Time | Trust Infrastructure audit log | >72h = escalation trigger |
| SS-06 | Salary Truth Validation Disputes | CTC vs. reality alumni feedback | Any dispute = CRITICAL flag |
| SS-07 | GDPR Consent Re-Withdrawal Rate | Consent management system | Rate >2%/month = trust erosion |
| SS-08 | Profile Delete Rate | Account management logs | Rate >1%/month = satisfaction failure |

---

## SECTION 4 — SCORING ENGINE (DETERMINISTIC · RULE-BASED · AUDITABLE)

### 4.1 Composite Satisfaction Score (CSS)

```
CSS = (
    (Σ Explicit_Signals × W_explicit)
  + (Σ Implicit_Signals × W_implicit)
  + (Σ Systemic_Signals × W_systemic)
) / Total_Weighted_Signals

Range: 0 – 100
Precision: 2 decimal places
Refresh Cycle: 24 hours (rolling)
```

### 4.2 Weight Table (LOCKED)

| Signal Class | Weight (W) | Rationale |
|---|---|---|
| Explicit Signals | 0.40 | Direct participant voice — highest intentional input |
| Implicit Signals | 0.35 | Behavioral truth — cannot be gamed by social desirability |
| Systemic Signals | 0.25 | Infrastructure reliability as proxy for trust |

### 4.3 Participant-Class-Specific Scoring Modifiers

```
STUDENT       : CSS × 1.00   (baseline — highest volume, primary beneficiary)
PARENT        : CSS × 0.60   (read-only — limited touchpoints)
INSTITUTE     : CSS × 0.90   (placement cell — critical trust node)
ENTERPRISE    : CSS × 0.85   (recruiter trust — revenue-linked)
TRAINER       : CSS × 0.80   (content quality — curriculum satisfaction)
GD_PARTICIPANT: CSS × 1.00   (deterministic system — full accountability)
EVALUATOR     : CSS × 0.75   (assessment fairness — bias-risk class)
ADMIN         : CSS × 0.50   (platform operator — internal user)
RECRUITER_HR  : CSS × 0.85   (aligned with ENTERPRISE)
```

### 4.4 Penalty Clauses (HARD ENFORCEMENT)

| Penalty Event | Score Deduction | Escalation |
|---|---|---|
| `SILENT_DISSATISFACTION` (no signal after interaction) | −5 points to domain CSS | Log + Alert |
| `BIAS_INCIDENT_UNRESOLVED` (>72h) | −10 points to platform CSS | CRITICAL alert to Admin |
| `GD_ROOM_ABANDONED` (mid-session exit, non-network) | −3 points to GD-module CSS | Log |
| `OFFER_DISPUTE_ACTIVE` (salary truth conflict) | −15 points to ENTERPRISE CSS | Escalate to Compliance |
| `GDPR_CONSENT_WITHDRAWN` | −8 points per withdrawal to Platform Trust Score | Legal notification |
| `PROFILE_DELETION` | −6 points to Onboarding CSS | Root cause analysis trigger |
| `BLOCKCHAIN_VERIFY_FAILURE` | −20 points to ENTERPRISE Trust Score | STOP — manual review required |
| `SUPPORT_TICKET_UNRESOLVED` (>48h) | −7 points to Support CSS | SLA breach alert |

---

## SECTION 5 — TRUST INFRASTRUCTURE INTEGRATION

### 5.1 Blockchain Layer Binding

```
AGENT_READS_FROM: Blockchain Offer Ledger
READ_EVENTS:
  - offer_issued_timestamp
  - offer_verified_timestamp
  - offer_acceptance_status
  - offer_dispute_flag
  - qr_scan_count (proxy for parent verification engagement)

AGENT_WRITES_TO: Satisfaction Audit Trail (Immutable Append-Only Log)
WRITE_EVENTS:
  - css_snapshot (daily)
  - penalty_event_log
  - escalation_record
  - resolution_confirmation

Tamper protection: All writes include SHA-256 hash of input state.
```

### 5.2 GD Engine (Voice Group Discussion) Binding

```
READS_FROM: GD Session Orchestrator State Machine
DATA_POINTS:
  - participant_id
  - turns_completed / turns_skipped
  - mic_open_duration_seconds
  - interrupt_attempts_count
  - network_drop_count
  - session_completion_status (COMPLETED | ABANDONED | NETWORK_FAILURE)
  - final_deterministic_score

SATISFACTION_MAPPING:
  turns_completed == total_turns    → positive_implicit_signal
  interrupt_attempts > 3            → frustration_proxy → flag for UX review
  session_completion = ABANDONED    → SILENT_DISSATISFACTION trigger
  network_drop_count > 2            → infrastructure_friction_event
```

### 5.3 AI Match Engine Binding

```
READS_FROM: Smart Job Recommendation Engine
DATA_POINTS:
  - match_score_presented
  - application_made (Y/N)
  - outcome_status (shortlisted | rejected | offer)
  - ai_recommendation_vs_outcome_delta

TRUST_SIGNAL:
  If recommendation_accepted AND outcome = offer → AI Trust +1
  If recommendation_accepted AND outcome = rejected → AI Trust −0.5
  If recommendation_ignored → neutral (no inference)
```

### 5.4 Company KYC & Trust Score Binding

```
READS_FROM: Company KYC Verification System
DATA_POINTS:
  - company_trust_score (1–100)
  - offer_rollout_rate
  - average_employee_rating
  - fraud_complaint_count
  - salary_truth_dispute_flag

SATISFACTION_CROSS_REFERENCE:
  company_trust_score < 60 → NOT PERMITTED to affect student CSS positively
  offer_rollout_rate < 80% → flag ENTERPRISE dissatisfaction risk
  salary_truth_dispute_flag = TRUE → CRITICAL escalation
```

### 5.5 Psychological Support Layer Binding

```
READS_FROM: Mental Health & Wellbeing Module
MONITORS:
  - counseling_session_requests (rate per cohort)
  - helpline_activations (anonymized count, no PII)
  - peer_support_forum_activity_delta
  - rejection_handling_path_completions

RULE:
  Counseling_request_rate > 15% per cohort within 7 days
  → TRIGGER: Platform_Stress_Surge_Alert
  → ESCALATE to: Institute TPO + Platform Admin
  → SUPPRESS: Public reporting (privacy protection)
  → LOG: Internal audit only
```

---

## SECTION 6 — DATA ISOLATION & PRIVACY CONTRACT

```
PII_IN_SCORES        : FORBIDDEN
PARTICIPANT_SCORES   : Anonymized by default
PARENT_ACCESS        : Aggregate only (no individual peer comparison)
RECRUITER_ACCESS     : Own interaction CSS only
ADMIN_ACCESS         : Full platform CSS + domain breakdown
STUDENT_ACCESS       : Own CSS + percentile (no peer identification)
EXTERNAL_ACCESS      : DENIED (no third-party data sharing)
```

### 6.1 GDPR Compliance Rules

| Rule | Enforcement |
|---|---|
| Data minimization | Only satisfaction-relevant signals collected |
| Right to erasure | Profile delete triggers full CSS history purge within 30 days |
| Consent gating | Explicit signals collected ONLY with active consent |
| Data portability | Student CSS history exportable (JSON) on request |
| Purpose limitation | Satisfaction data CANNOT be used for hiring decisions |

**HARD RULE:** Satisfaction scores are NEVER exposed to Recruiters or Enterprises for individual student evaluation. Violation = STOP EXECUTION + Security Incident.

---

## SECTION 7 — ALERT & ESCALATION PROTOCOL

### 7.1 Alert Severity Matrix

| Level | Threshold | Label | Recipient | SLA |
|---|---|---|---|---|
| L1 — INFO | CSS 70–84 | NOMINAL | No alert | — |
| L2 — WATCH | CSS 60–69 | DECLINING | Module Owner | 72h review |
| L3 — WARNING | CSS 50–59 | AT_RISK | Platform Admin | 48h action |
| L4 — CRITICAL | CSS 30–49 | TRUST_BREACH | Admin + Compliance | 24h escalation |
| L5 — EMERGENCY | CSS < 30 | SYSTEM_FAILURE | CTO + Legal + Admin | Immediate |

### 7.2 Domain-Specific Escalation Paths

```
GD_MODULE CSS < 50:
  → Escalate to: GD System Architect + QA
  → Action: Audit last 50 sessions for protocol drift
  → Freeze: New GD cohort creation until resolution

ENTERPRISE_TRUST CSS < 60:
  → Escalate to: Compliance Officer + Platform Admin
  → Action: Suspend company job posting access
  → Notify: Affected student cohort (anonymized)

STUDENT_PLACEMENT_CSS < 55:
  → Escalate to: TPO + Institute Admin
  → Action: Trigger counseling surge protocol
  → Report: Parent notification (aggregate, no PII)

BLOCKCHAIN_VERIFY CSS < 70:
  → Escalate to: Infrastructure Lead
  → Action: Halt offer issuance until node health restored
  → SLA: 4-hour resolution window

AI_MATCH_TRUST CSS < 65:
  → Escalate to: AI/ML Lead + Product Owner
  → Action: Force human review of recommendation pipeline
  → Log: All affected recommendations for audit
```

---

## SECTION 8 — REPORTING CONTRACTS

### 8.1 Report Types (LOCKED)

| Report ID | Name | Frequency | Recipient | Format |
|---|---|---|---|---|
| RPT-01 | Platform CSS Summary | Daily | Platform Admin | JSON + Dashboard |
| RPT-02 | Module-Level Satisfaction Breakdown | Weekly | Module Owners | PDF + Dashboard |
| RPT-03 | Trust Infrastructure Health Report | Weekly | Admin + Compliance | PDF |
| RPT-04 | GD System Fairness & Satisfaction | Per-batch | GD Operator + TPO | JSON |
| RPT-05 | Student Placement Journey Satisfaction | Monthly | Institute + Admin | PDF (GDPR-safe) |
| RPT-06 | Parent Engagement Trust Report | Monthly | Platform Admin | Dashboard |
| RPT-07 | Company KYC Trust Correlation Report | Monthly | Compliance + Admin | PDF |
| RPT-08 | Bias Incident Resolution Audit | On-demand | Compliance + Legal | PDF (Audit-grade) |
| RPT-09 | Mental Health Surge Report | Triggered (L4+) | Admin + TPO | CONFIDENTIAL PDF |
| RPT-10 | Annual Trust Infrastructure Audit | Annual | Board + Legal | Full Audit Package |

### 8.2 Parent Report Rules (STRICT)

```
PARENT_REPORT_REQUIRES: Student consent (opt-in only)
PARENT_REPORT_CONTAINS:
  - Activity summary (applications, interviews, skill courses)
  - Performance metrics (non-comparative)
  - Placement probability (aggregate model only)
  - Mentor feedback summary
  - Next steps
PARENT_REPORT_FORBIDS:
  - CSS scores of other students
  - Peer ranking
  - Mental health incident details
  - Interview content (confidential)
```

---

## SECTION 9 — FAILURE HANDLING (NON-NEGOTIABLE)

| Failure Scenario | Agent Action | Platform Action |
|---|---|---|
| Signal source unavailable | Log gap; use last known value; flag as STALE | Alert Infrastructure team |
| CSS computation error | STOP; emit NULL; do not publish partial score | Halt dashboard refresh |
| Blockchain read timeout | Retry ×3 (5s interval); on failure → STOP + alert | Infrastructure escalation |
| PII detected in signal payload | REJECT signal; QUARANTINE; alert Security team | Security incident trigger |
| Consent not verified for explicit signal | REJECT signal; do not store | GDPR compliance log |
| Score manipulation attempt detected | STOP agent; LOCK module; alert Admin + Legal | Emergency security protocol |
| Agent crash / restart | Resume from last persisted checkpoint | No data loss guaranteed |

---

## SECTION 10 — INTEGRATION CONTRACTS

### 10.1 Required Upstream Contracts

```
CONTRACT_REQUIRED_FROM:
  ✅ GD_ORCHESTRATOR            → session_completion_payload (JSON schema v2.1+)
  ✅ BLOCKCHAIN_OFFER_LEDGER    → offer_event_stream (signed webhook)
  ✅ AI_MATCH_ENGINE            → recommendation_outcome_callback
  ✅ COMPANY_KYC_SYSTEM         → trust_score_updated_event
  ✅ MENTAL_HEALTH_MODULE       → anonymized_engagement_metrics
  ✅ ACCOUNT_MANAGEMENT         → profile_deletion_event
  ✅ CONSENT_MANAGEMENT         → consent_change_event_stream
  ✅ SUPPORT_TICKET_SYSTEM      → ticket_status_change_event
  ✅ NOTIFICATION_SERVICE       → delivery_confirmation_stream

Absent contract → STOP_LANE + REPORT_MISSING_CONTRACT
```

### 10.2 Downstream Output Contracts

```
AGENT_EMITS_TO:
  ✅ DASHBOARD_SERVICE          → css_snapshot (24h refresh)
  ✅ ALERT_NOTIFICATION_ENGINE  → escalation_events
  ✅ AUDIT_LOG_STORE            → immutable_event_log (append-only)
  ✅ REPORT_GENERATOR           → structured report payloads
  ✅ ADMIN_DASHBOARD            → trust_health_summary
  ✅ COMPLIANCE_MODULE          → bias + gdpr incident feed
```

### 10.3 Event Schema Registry (Required Before Deployment)

```
REQUIRED_SCHEMAS:
  - satisfaction_signal_v1.json
  - css_snapshot_v1.json
  - penalty_event_v1.json
  - escalation_record_v1.json
  - parent_report_payload_v1.json
  - bias_audit_event_v1.json

Absence of any schema → STOP_EXECUTION
```

---

## SECTION 11 — DEPLOYMENT SPECIFICATION

### 11.1 Infrastructure Requirements

```
RUNTIME        : Node.js 20 LTS (microservice)
STATE_STORE    : Redis (TTL-managed signal cache; persistent CSS store)
DB             : PostgreSQL (audit log; satisfaction history; report archive)
MESSAGE_BUS    : Kafka (event stream ingestion from all signal sources)
SCHEDULER      : Cron-based (24h rolling CSS computation)
HOSTING        : Kubernetes namespace: antigravity-trust-infra
SCALING_POLICY : Horizontal pod autoscaling (min 2 replicas; max 10)
ENVIRONMENT    : DEV · TEST · STAGING · PRODUCTION (isolated per R96 law)
```

### 11.2 Environment Config Files Required

```
/config/environments/
  psta.dev.env
  psta.test.env
  psta.staging.env
  psta.production.env

Each must define:
  DB_CONNECTION
  REDIS_CONNECTION
  KAFKA_BROKER_URL
  BLOCKCHAIN_NODE_URL
  AUDIT_LOG_ENDPOINT
  ALERT_WEBHOOK_URL
  GDPR_CONSENT_SERVICE_URL
  AGENT_SECRET (rotated 90-day policy)
```

### 11.3 CI/CD Gate Requirements

```
PIPELINE_STAGES:
  1. Schema Contract Validator (all 6 schemas must pass)
  2. Signal Integration Tests (all 10 upstream contracts)
  3. Scoring Engine Unit Tests (determinism verification)
  4. Penalty Clause Enforcement Tests
  5. PII Leak Scanner (reject any test with PII in payload)
  6. Privacy Compliance Check (GDPR rules)
  7. Docker Build + Security Scan
  8. Staging Deploy + Smoke Test
  9. Production Gate (human approval required for L4+ logic changes)
```

---

## SECTION 12 — ANTI-PATTERNS (EXPLICITLY FORBIDDEN)

```
FORBIDDEN:
  ❌ Using CSS scores in hiring pipeline or recruiter visibility
  ❌ Comparing individual student satisfaction scores publicly
  ❌ Using implicit signals as sole basis for escalation (must combine with ≥1 other class)
  ❌ Storing raw audio/video for satisfaction analysis (privacy violation)
  ❌ Allowing Evaluator or Recruiter to view GD_PARTICIPANT satisfaction breakdown
  ❌ Modifying CSS formula without version bump + human declaration
  ❌ Inferring personality traits from satisfaction signals
  ❌ Assigning "satisfaction labels" (e.g., "unhappy student") — only numeric scores
  ❌ Exposing mental health module data in satisfaction reports (except L5 emergency)
  ❌ Cross-tenant satisfaction data access (hard tenant isolation enforced)
```

---

## SECTION 13 — AUDIT & COMPLIANCE TRAIL

```
EVERY AGENT ACTION MUST LOG:
  - agent_id: PSTA-ENT-001
  - action_type: [SIGNAL_RECEIVED | SCORE_COMPUTED | ALERT_EMITTED | REPORT_GENERATED | PENALTY_APPLIED]
  - timestamp: ISO 8601 UTC
  - participant_class: (anonymized ID)
  - input_hash: SHA-256 of input payload
  - output_hash: SHA-256 of output payload
  - rule_applied: (rule ID from this document)
  - environment: DEV | TEST | STAGING | PRODUCTION

Log Retention:
  PRODUCTION  : 7 years (legal compliance)
  STAGING     : 90 days
  TEST        : 30 days
  DEV         : 7 days

Log Immutability: APPEND-ONLY. No UPDATE or DELETE permitted on audit log.
```

---

## SECTION 14 — VERSION CONTROL POLICY

```
DOCUMENT_VERSION  : 1.0
STATUS            : SEALED
CHANGE_AUTHORITY  : Platform Architect + Compliance Officer (dual sign-off)
CHANGE_POLICY     : ADD-ONLY sections permitted; no deletion of existing rules
VERSION_FORMAT    : MAJOR.MINOR (1.0, 1.1, 2.0)
MAJOR_BUMP_TRIGGERS:
  - CSS formula change
  - New participant class added
  - Blockchain integration schema change
  - GDPR rule update
MINOR_BUMP_TRIGGERS:
  - New signal ID added
  - Alert threshold adjustment
  - Report type added
```

---

## SECTION 15 — SYSTEM DEFENSE STATEMENT

> **"The PARTICIPANT_SATISFACTION_TRACKING_AGENT converts the traditionally subjective domain of satisfaction measurement into a deterministic, privacy-preserving, rule-governed protocol. Human interpretation is removed from scoring. AI inference is removed from scoring. Only signals, rules, weights, and enforcement remain. Every score is reproducible, every penalty is auditable, every report is GDPR-compliant. The agent serves the participant — not the recruiter, not the enterprise, not the platform's commercial interest."**

---

## APPENDIX A — SIGNAL QUICK REFERENCE

| ID | Type | Name | Weight Class | Participant |
|---|---|---|---|---|
| ES-01 | Explicit | Post-Session Micro-Rating | HIGH | STUDENT |
| ES-02 | Explicit | Post-Assessment Feedback | HIGH | STUDENT, EVALUATOR |
| ES-03 | Explicit | Offer Receipt Confirmation | CRITICAL | STUDENT, PARENT |
| ES-04 | Explicit | Mentor Session Rating | MEDIUM | STUDENT |
| ES-05 | Explicit | Platform Usability Score | MEDIUM | ALL |
| ES-06 | Explicit | Placement Cell Rating | HIGH | INSTITUTE |
| ES-07 | Explicit | Company Trust Feedback | HIGH | ENTERPRISE |
| ES-08 | Explicit | Parent Report Ack | MEDIUM | PARENT |
| ES-09 | Explicit | GD Fairness Confirmation | HIGH | GD_PARTICIPANT |
| ES-10 | Explicit | Bias Resolution Rating | CRITICAL | STUDENT |
| IS-01 | Implicit | Platform Return Rate | HIGH | ALL |
| IS-02 | Implicit | Feature Re-Engagement | HIGH | ALL |
| IS-03 | Implicit | Offer Acceptance Rate | CRITICAL | STUDENT |
| IS-04 | Implicit | Profile Completion Velocity | MEDIUM | STUDENT |
| IS-05 | Implicit | Support Ticket Absence | MEDIUM | ALL |
| IS-06 | Implicit | GD Room Completion Rate | HIGH | GD_PARTICIPANT |
| IS-07 | Implicit | Mock Interview Re-Attempt | MEDIUM | STUDENT |
| IS-08 | Implicit | Alumni Referral Initiation | HIGH | ALUMNI |
| IS-09 | Implicit | Notification Open Rate | LOW | ALL |
| IS-10 | Implicit | Complaint Silence (Negative) | MEDIUM | ALL |
| SS-01 | Systemic | GD Turn Compliance | HIGH | GD_PARTICIPANT |
| SS-02 | Systemic | Network Drop Recovery | MEDIUM | GD_PARTICIPANT |
| SS-03 | Systemic | Blockchain Verify Latency | HIGH | STUDENT, ENTERPRISE |
| SS-04 | Systemic | AI Match Acceptance Rate | HIGH | STUDENT |
| SS-05 | Systemic | Bias Resolution Time | CRITICAL | STUDENT |
| SS-06 | Systemic | Salary Truth Disputes | CRITICAL | STUDENT, ENTERPRISE |
| SS-07 | Systemic | GDPR Consent Re-Withdrawal | HIGH | ALL |
| SS-08 | Systemic | Profile Delete Rate | HIGH | ALL |

---

## APPENDIX B — DEPENDENCY GATE MAP

```
Lane A (Identity/RBAC) → identity_ready
  ↓
Lane B (Data) → db_ready + search_ready
  ↓
PSTA Signal Ingestion Engine → signal_schema_ready
  ↓
PSTA Scoring Engine → css_engine_ready
  ↓
PSTA Alert Engine → alert_contracts_ready
  ↓
PSTA Report Generator → report_contracts_ready
  ↓
PRODUCTION DEPLOY

Each gate requires automated CI pass.
No manual approvals between gates.
Human approval required ONLY for production deploy of scoring formula changes.
```

---

*ANTIGRAVITY Platform — Trust Infrastructure Division*
*Document ID: PSTA-ENT-001-v1.0*
*Sealed: March 2026*
*Next Review: September 2026 (mandatory)*
*Classification: INTERNAL — PLATFORM ARCHITECTURE*
