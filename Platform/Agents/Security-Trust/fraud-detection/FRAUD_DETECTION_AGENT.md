# FRAUD_DETECTION_AGENT.md
**System:** ECOSKILLER — Unified Job + Skill + Project + Education + Marketplace SaaS  
**Agent Name:** Fraud Detection Engine (FDE)  
**Execution Target:** Google Antigravity Tool — Production SaaS Generator  
**Artifact Class:** Production System Blueprint  
**Status:** FINAL · LOCKED · GOVERNED · SEALED  
**Version:** FDE-v1.0  
**Mutation Policy:** Add-only via version bump. No deletions. No rewrites.  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  

---

## 🔒 SEAL DECLARATION

```
FRAUD_DETECTION_AGENT = ACTIVE
EXECUTION_ENGINE = ANTIGRAVITY
STACK = LOCKED
MUTATION = ADD-ONLY
DEFAULT_POLICY = DENY
AUDIT_MODE = IMMUTABLE
ANTI-GRAVITY_SAFE = TRUE
INTERPRETATION = FORBIDDEN
```

> Antigravity MUST NOT reinterpret detection logic, scoring weights, escalation thresholds,
> or enforcement flows defined in this document.
> Antigravity MUST generate only what is declared.
> Absence of any declared component → STOP EXECUTION → REPORT FDE-INCOMPLETE

---

## SECTION FDE-A — AGENT IDENTITY & SCOPE

### A1 — Purpose

The Fraud Detection Engine is a cross-domain, always-on, rule-and-ML-hybrid agent
that protects the Ecoskiller platform from all categories of fraud, abuse, and
financial misrepresentation across every surface: identity, content, GD sessions,
Dojo matches, royalty and revenue reporting, billing, certifications, and referrals.

### A2 — Domains Covered

| Domain | Fraud Vectors |
|---|---|
| Identity & Registration | Fake accounts, multi-account farming, impersonation |
| Voice GD Sessions | Session manipulation, silent farming, interrupt abuse |
| Dojo Matches | Score manipulation, collusion, belt farming, fake tournaments |
| Revenue & Royalty | Fake revenue submissions, underreporting, round-number bias |
| Billing & Subscriptions | Refund abuse, chargeback fraud, plan gaming |
| Certifications & Belts | Auto-promotion attempts, mentor collusion, eligibility bypass |
| Referral System | Self-referral loops, device-spoof referrals, reward farming |
| Content & Social | Spam, duplicate flooding, copy-paste abuse, hashtag stuffing |
| API & Infrastructure | Velocity abuse, token replay, rate-limit evasion |
| Accounting & Financial | Anomalous entries, late-period spikes, restatement fraud |

### A3 — Non-Scope (Hard Boundary)

The FDE does NOT duplicate:
- Authentication rules (covered under AUTH compliance seal)
- Password security (covered under PASS-SEC-v1)
- Session management (covered under SESSION compliance seal)
- Authorization / RBAC (covered under AUTHORIZATION compliance seal)
- Incident Response Command (covered under IRC sections)

FDE consumes outputs from the above systems as signals. It does not govern them.

---

## SECTION FDE-B — ARCHITECTURE POSITION

### B1 — Placement in System

```
Candidate / User Request
        ↓
   API Gateway (Kong)
        ↓
 WAF + Rate Limiter (ModSecurity + Envoy)
        ↓
 FDE Signal Interceptor ← (passive tap on all traffic classes)
        ↓
  Business Service (Auth / GD / Dojo / Billing / Revenue)
        ↓
  Event Bus (Kafka)
        ↓
 FDE Async Signal Processor
        ↓
 FDE Scoring Engine
        ↓
 FDE Action Dispatcher
        ↓
  Immutable Audit Log (MinIO + PostgreSQL)
```

### B2 — Traffic Class Coverage

| Traffic Class | FDE Layer |
|---|---|
| HTTP APIs (CRUD) | Inline interceptor + async event |
| WebSocket (GD / Dojo / Interviews) | Real-time session monitor |
| Media (Jitsi / LiveKit) | Behavioral metadata only — no audio/video access |
| Async Events (Kafka) | Stream processor |
| Billing Webhooks | Dedicated financial fraud listener |
| Revenue Reports (API) | Revenue Ingestion Gateway fraud gate |

### B3 — FDE Does NOT Touch Raw Media

The Fraud Detection Engine:
- Does NOT access audio streams
- Does NOT access video streams
- Does NOT record or decode WebRTC content
- Only consumes behavioral metadata: mic-open duration, turn compliance,
  join timing, interruption counts, connection drops

---

## SECTION FDE-C — SIGNAL TAXONOMY

### C1 — Identity Signals

```
SIG-ID-01   Multiple accounts from same device fingerprint
SIG-ID-02   Multiple accounts from same IP subnet (/24)
SIG-ID-03   Email domain mismatch (student → non-institutional domain)
SIG-ID-04   VPN / proxy / Tor exit node registration
SIG-ID-05   Account creation velocity > 3/hour from same device
SIG-ID-06   Profile completeness below threshold at privileged action
SIG-ID-07   Phone number reuse across accounts
SIG-ID-08   Institution domain claimed but unverifiable
SIG-ID-09   Rapid name/email change post-onboarding
SIG-ID-10   Login from anomalous geo after known device session
```

### C2 — Voice GD Session Signals

```
SIG-GD-01   Zero mic-open duration during assigned speaking token
SIG-GD-02   Interrupt attempts > threshold during open discussion round
SIG-GD-03   Network drop pattern correlating with turn assignment
SIG-GD-04   Rejoin attempt after forced exit (policy: denied + logged)
SIG-GD-05   Repeated session abandonment before score commit
SIG-GD-06   Batch participation without any active contribution across N sessions
SIG-GD-07   Device-clock skew suggesting automated client behavior
SIG-GD-08   Score outlier: all sessions max score with zero turns used
SIG-GD-09   Late join attempt (spectator-only slot) used to gain score credit
SIG-GD-10   Same IP batch: >1 participant in same GD session
```

### C3 — Dojo Match Signals

```
SIG-DJ-01   Score submitted without match session metadata
SIG-DJ-02   Peer score variance beyond acceptable deviation threshold
SIG-DJ-03   Mentor score perfectly aligning with losing-side benefit
SIG-DJ-04   Belt promotion triggered without required match count
SIG-DJ-05   Same mentor certifying same student > N times consecutively
SIG-DJ-06   Tournament loop: same pair re-matched > threshold in short window
SIG-DJ-07   Scenario skip rate > threshold (avoidance of hard scenarios)
SIG-DJ-08   Match duration anomaly (too short / too fast for scenario type)
SIG-DJ-09   Replay manipulation: replay viewed by scorer before scoring
SIG-DJ-10   Certification chain: mentor certified by certifier they previously certified
```

### C4 — Revenue & Royalty Signals

```
SIG-RR-01   Revenue report submitted with round-number values (e.g., 100000.00 exactly)
SIG-RR-02   Late-period spike: revenue spike in final days of reporting window
SIG-RR-03   Revenue YoY growth > anomaly threshold without corresponding activity data
SIG-RR-04   Revenue report amended > N times in single period
SIG-RR-05   Business submitting revenue but royalty wallet showing no disbursement
SIG-RR-06   Royalty calculation input hash mismatch (tampered ledger entry)
SIG-RR-07   Revenue submission from IP/device not matching authorized business account
SIG-RR-08   Zero revenue reported for licensed product with confirmed market activity
SIG-RR-09   Duplicate revenue report submission (same period, same business_id)
SIG-RR-10   Revenue data structure anomaly (missing mandatory fields, injected nulls)
```

### C5 — Billing & Subscription Signals

```
SIG-BL-01   Refund request immediately following high-value feature usage
SIG-BL-02   Chargeback rate for tenant > platform threshold
SIG-BL-03   Plan downgrade + immediate re-upgrade within cooldown window
SIG-BL-04   Trial account cycling: repeated new-account trial abuse from same device
SIG-BL-05   Promo code reuse beyond claimed limit
SIG-BL-06   Invoice total manipulation attempt (API-level field injection)
SIG-BL-07   Feature usage exceeding plan entitlement without billing trigger
SIG-BL-08   Billing event replay: same billing_event_id submitted twice
SIG-BL-09   GST/VAT registration mismatch (claimed exemption vs jurisdiction data)
SIG-BL-10   Bulk seat purchase + immediate mass deactivation for refund
```

### C6 — Certification & Belt Signals

```
SIG-CB-01   Belt eligibility check bypassed (direct DB write without engine call)
SIG-CB-02   Auto-promotion attempt: no mentor confirmation in certification chain
SIG-CB-03   Certification issued to account with open fraud flag
SIG-CB-04   Belt version mismatch: certificate version not matching current criteria
SIG-CB-05   Parent consent record missing for minor certification
SIG-CB-06   Ownership transfer before age-18 threshold (royalty / idea systems)
SIG-CB-07   Certification chain gap: belts skipped without exception record
SIG-CB-08   Mentor certifying student in domain where mentor has no active belt
```

### C7 — Referral Signals

```
SIG-RF-01   Self-referral: referrer_id == referred_id (direct or alias)
SIG-RF-02   Device-loop: referrer and referred share fingerprint within 24h
SIG-RF-03   IP-loop: referrer and referred share IP within join window
SIG-RF-04   Reward farming: referred account inactive within 30 days of claim
SIG-RF-05   Referral velocity: single code generating > N accepts per hour
SIG-RF-06   Code sharing on blacklisted platforms (scraped link patterns)
SIG-RF-07   Referral chain depth > allowed tier (pyramid pattern)
SIG-RF-08   Referral reward claimed before verification gate passed
```

### C8 — Content & Social Signals

```
SIG-CS-01   Duplicate post: hash match within sliding window
SIG-CS-02   Flood-post: > N posts within T seconds from same account
SIG-CS-03   Mass-DM: identical message body to > N recipients
SIG-CS-04   External link from non-whitelisted domain by new account
SIG-CS-05   Hashtag count > platform limit per post
SIG-CS-06   Mention spam: > N unique mentions in single content item
SIG-CS-07   Image anomaly: image metadata stripped (anti-forensic signal)
SIG-CS-08   Hidden text injection (white-on-white, zero-size font)
SIG-CS-09   False report pattern: reporter submitting > N dismissed reports
SIG-CS-10   Attachment type mismatch: declared MIME ≠ actual MIME
```

### C9 — API & Infrastructure Signals

```
SIG-AP-01   Request velocity > rate limit after evasion (header rotation)
SIG-AP-02   JWT token replay: same jti used from different IP
SIG-AP-03   CSRF token absence on state-changing request
SIG-AP-04   SQL injection pattern in API parameters
SIG-AP-05   Path traversal attempt in file upload endpoint
SIG-AP-06   Scoring endpoint called without valid session context
SIG-AP-07   Admin endpoint probing from non-admin role
SIG-AP-08   Bulk data extraction pattern (paginated scraping signature)
SIG-AP-09   WebSocket command injection attempt (mute/unmute outside token)
SIG-AP-10   TLS fingerprint anomaly (non-browser client on browser-only endpoint)
```

---

## SECTION FDE-D — SCORING MODEL

### D1 — Model Architecture

```
Model Type:         Rule-Based Primary + Gradient Boosting Secondary
Framework:          Traditional ML only (per R28-2 mandate)
LLM Involvement:    FORBIDDEN for scoring decisions
Explainability:     Required on every score output
Audit:              Every score immutably logged
```

> No LLM, confidence NLP, personality inference, or emotional signal is
> permitted in fraud scoring. This is non-negotiable.

### D2 — Fraud Score Formula

```
fraud_score = Σ (signal_weight[i] × signal_fired[i]) × domain_multiplier
             + velocity_bonus
             + pattern_recurrence_multiplier
             - trust_score_offset
```

| Parameter | Description |
|---|---|
| signal_weight[i] | Per-signal risk weight (0.1 – 10.0, declared in registry) |
| signal_fired[i] | Binary: 1 = triggered, 0 = not |
| domain_multiplier | Financial domain = 1.5x, Identity = 1.3x, Content = 1.0x |
| velocity_bonus | +N points if same signal fires > threshold in sliding window |
| pattern_recurrence_multiplier | Multiplier if signal appeared in prior sessions |
| trust_score_offset | Reduces score for high-trust, verified, long-standing accounts |

### D3 — Fraud Score Thresholds

| Score Range | Classification | Auto-Action |
|---|---|---|
| 0 – 19 | CLEAN | None |
| 20 – 39 | WATCH | Log + passive monitor |
| 40 – 59 | SUSPICIOUS | Rate limit + flag for review |
| 60 – 79 | HIGH RISK | Feature restrict + alert ops |
| 80 – 94 | CONFIRMED FRAUD | Action dispatch + account freeze |
| 95 – 100 | CRITICAL | Immediate isolation + IRC escalation |

### D4 — Score Immutability

- Fraud scores are written once per detection event
- Scores cannot be mutated after commit
- Score correction requires new event with override_reason and dual approval
- Override audit trail permanent

---

## SECTION FDE-E — DETECTION ENGINES

### E1 — Real-Time Interceptor (Inline)

**Triggers on:** Every HTTP API request and WebSocket command  
**Latency budget:** < 5ms p99 (non-blocking pass-through)  
**Checks:**
- Signal registry lookup (cached in Redis)
- JWT context validation
- Rate limit breach check
- Injection pattern match (WAF integration)

**Output:** ALLOW / FLAG / BLOCK decision injected into request context

### E2 — GD Session Monitor

**Transport:** WebSocket listener (backend orchestrator tap)  
**Runs during:** Active GD session lifecycle only  
**Tracks per participant:**
```
mic_open_ms          per token
interrupt_count      per session
silence_tokens       count
network_drops        count and timing
turn_completion_rate per session
```
**Emits:** `gd.behavior.event` to Kafka on each anomaly  
**Score commit:** After session termination, not during

### E3 — Dojo Match Monitor

**Transport:** Game event WebSocket tap  
**Tracks:**
```
score_submission_context     (valid session required)
peer_score_vector            (variance analysis)
mentor_score_delta           (bias detection)
scenario_skip_pattern        (avoidance detection)
match_duration_vs_scenario   (timing anomaly)
```
**Emits:** `dojo.fraud.signal` to Kafka

### E4 — Revenue Ingestion Fraud Gate

**Transport:** REST API (Revenue Ingestion Gateway)  
**Runs:** Synchronously before revenue record is written  
**Checks:**
```
round_number_detector        (modulo analysis)
late_period_spike_detector   (period-end timing)
duplicate_submission_check   (idempotency hash)
hash_integrity_check         (payload vs signature)
authorized_submitter_check   (IP + device + account)
field_completeness_check     (required fields)
```
**On flag:** Revenue held in PENDING state — not written to ledger  
**On pass:** Revenue committed with fraud_cleared_at timestamp

### E5 — Async Stream Processor

**Transport:** Kafka consumer (dedicated FDE consumer group)  
**Topics consumed:**
```
user.created
gd.completed
dojo.match.scored
belt.eligible
invoice.generated
referral.accepted
revenue.submitted
content.posted
auth.anomaly
```
**Processing:** Signal enrichment + fraud score computation + action dispatch

### E6 — Periodic Batch Detector

**Runs via:** Apache Airflow (scheduled)  
**Jobs:**
```
daily:    multi_account_cluster_scan
daily:    dormant_referral_reward_audit
weekly:   mentor_collusion_graph_scan
weekly:   revenue_trend_anomaly_scan
monthly:  certification_chain_integrity_audit
monthly:  royalty_ledger_double_entry_audit
```

---

## SECTION FDE-F — ACTION DISPATCHER

### F1 — Action Registry

| Action Code | Description | Reversible |
|---|---|---|
| ACT-LOG | Write fraud signal to audit log | N/A |
| ACT-WATCH | Add account to passive watch list | Yes |
| ACT-RATE | Apply elevated rate limiting | Yes |
| ACT-FLAG | Mark entity with fraud_flag in DB | Yes (dual approval) |
| ACT-RESTRICT | Disable specific feature(s) for account | Yes (ops review) |
| ACT-FREEZE | Suspend account (login blocked) | Yes (human review) |
| ACT-HOLD | Place transaction / revenue in PENDING | Yes (compliance review) |
| ACT-ESCALATE | Trigger IRC incident creation | No (must be reviewed) |
| ACT-TERMINATE | Force-end active session (GD / Dojo) | No |
| ACT-DENY | Block operation at gateway level | Per-request |
| ACT-CLAWBACK | Reverse credited reward (referral / royalty) | Yes (audit required) |
| ACT-ISOLATE | Tenant-level isolation (critical fraud) | Yes (senior approval) |

### F2 — Action Trigger Matrix

| Score Range | Minimum Actions Required |
|---|---|
| WATCH (20–39) | ACT-LOG + ACT-WATCH |
| SUSPICIOUS (40–59) | ACT-LOG + ACT-WATCH + ACT-RATE + ACT-FLAG |
| HIGH RISK (60–79) | All above + ACT-RESTRICT + ACT-HOLD (if financial) |
| CONFIRMED (80–94) | All above + ACT-FREEZE + ACT-ESCALATE |
| CRITICAL (95–100) | All above + ACT-TERMINATE + ACT-ISOLATE |

### F3 — Action Constraints

- No single-actor resolution for financial actions (dual control required)
- ACT-FREEZE requires ops console confirmation within 1 hour or auto-reversal
- ACT-ISOLATE requires senior admin approval (not automated)
- ACT-CLAWBACK requires immutable ledger entry before execution
- All actions write to fraud_action_log before execution, not after

---

## SECTION FDE-G — DATABASE SCHEMA (REQUIRED ENTITIES)

### G1 — Core Tables

```sql
fraud_signals
  id                UUID PK
  signal_code       VARCHAR NOT NULL  -- e.g. SIG-GD-01
  entity_type       ENUM(user, session, match, invoice, revenue_report, content)
  entity_id         UUID NOT NULL
  tenant_id         UUID NOT NULL
  fired_at          TIMESTAMPTZ NOT NULL
  signal_weight     DECIMAL(4,2) NOT NULL
  context_json      JSONB           -- raw signal context (immutable)
  created_at        TIMESTAMPTZ DEFAULT NOW()

fraud_scores
  id                UUID PK
  entity_type       ENUM(user, session, match, invoice, revenue_report)
  entity_id         UUID NOT NULL
  tenant_id         UUID NOT NULL
  score             DECIMAL(5,2) NOT NULL
  classification    ENUM(CLEAN, WATCH, SUSPICIOUS, HIGH_RISK, CONFIRMED, CRITICAL)
  signal_ids        UUID[]          -- array of contributing signal IDs
  scored_at         TIMESTAMPTZ NOT NULL
  model_version     VARCHAR NOT NULL
  explanation_json  JSONB           -- required explainability output

fraud_flags
  id                UUID PK
  entity_type       ENUM(user, account, business, mentor, content)
  entity_id         UUID NOT NULL
  tenant_id         UUID NOT NULL
  flag_reason       TEXT NOT NULL
  flag_source       ENUM(auto, ops, compliance, legal)
  severity          ENUM(LOW, MEDIUM, HIGH, CRITICAL)
  status            ENUM(OPEN, REVIEWING, RESOLVED, APPEALED)
  raised_at         TIMESTAMPTZ NOT NULL
  resolved_at       TIMESTAMPTZ
  resolution_note   TEXT
  raised_by         UUID            -- system or user ID

fraud_actions
  id                UUID PK
  action_code       VARCHAR NOT NULL -- ACT-* codes
  entity_type       ENUM(user, session, match, invoice, revenue_report, tenant)
  entity_id         UUID NOT NULL
  tenant_id         UUID NOT NULL
  fraud_score_id    UUID FK → fraud_scores
  triggered_by      ENUM(auto, ops, compliance)
  triggered_at      TIMESTAMPTZ NOT NULL
  executed_at       TIMESTAMPTZ     -- null = pending
  reversed_at       TIMESTAMPTZ     -- null = not reversed
  reversal_reason   TEXT
  approver_id       UUID            -- required for financial actions

referral_fraud_flags
  id                UUID PK
  referral_code     VARCHAR NOT NULL
  referrer_id       UUID FK → users
  referred_id       UUID FK → users
  flag_reason       ENUM(self_referral, device_loop, ip_loop, reward_farming, velocity)
  device_fingerprint VARCHAR
  ip_address        INET
  flagged_at        TIMESTAMPTZ NOT NULL
  reward_blocked    BOOLEAN DEFAULT TRUE

revenue_fraud_holds
  id                UUID PK
  revenue_report_id UUID NOT NULL
  business_id       UUID NOT NULL
  hold_reason       VARCHAR NOT NULL
  signals_fired     VARCHAR[]       -- signal codes
  fraud_score       DECIMAL(5,2)
  held_at           TIMESTAMPTZ NOT NULL
  reviewed_at       TIMESTAMPTZ
  disposition       ENUM(CLEARED, CONFIRMED_FRAUD, PARTIAL_ACCEPT)
  reviewer_id       UUID
```

---

## SECTION FDE-H — API CONTRACTS

### H1 — Internal APIs (Backend-to-FDE)

```
POST   /internal/fde/signals              Ingest a fraud signal
POST   /internal/fde/score                Request a fraud score for entity
GET    /internal/fde/score/{entity_id}    Get current fraud score
POST   /internal/fde/action               Trigger a fraud action
GET    /internal/fde/flags/{entity_id}    Get fraud flags for entity
POST   /internal/fde/revenue/gate         Revenue ingestion fraud gate check
```

All internal FDE endpoints:
- mTLS required
- Not exposed through Kong public gateway
- JWT with `service_role: fde_consumer` required

### H2 — Ops Console APIs (Admin-Facing)

```
GET    /ops/fraud/queue                   Pending review queue
PATCH  /ops/fraud/flags/{id}              Update flag status
POST   /ops/fraud/actions/{id}/approve    Approve pending action
POST   /ops/fraud/actions/{id}/reverse    Reverse executed action
GET    /ops/fraud/scores/{entity_id}      Full score history
GET    /ops/fraud/revenue/holds           Revenue hold queue
POST   /ops/fraud/revenue/holds/{id}/disposition   Clear or confirm hold
```

All ops endpoints:
- RBAC: `ops.fraud.reviewer` or `compliance.lead` role required
- MFA gate on all PATCH / POST operations
- Full audit log on every state change

---

## SECTION FDE-I — OBSERVABILITY (NON-OPTIONAL)

### I1 — Metrics (Prometheus)

```
fde_signals_fired_total{signal_code, domain}
fde_scores_distribution{classification}
fde_actions_dispatched_total{action_code}
fde_revenue_holds_active
fde_flags_open{severity}
fde_inline_latency_ms{p50, p95, p99}
fde_false_positive_rate{domain}         -- tracked via appeal outcomes
fde_ml_model_drift_score                -- monthly batch output
```

### I2 — Dashboards (Grafana)

Required dashboards:
- Fraud Signal Volume (real-time, by domain)
- Score Distribution Heatmap
- Action Dispatch Timeline
- Revenue Hold Queue Status
- GD Session Anomaly Rate
- Dojo Match Fraud Rate
- Referral Fraud Rate
- False Positive Tracker

### I3 — Alerts

| Alert | Threshold | Severity |
|---|---|---|
| CRITICAL fraud score spike | >5 CRITICAL scores/hour | P0 |
| Revenue hold queue > N | > 20 items pending > 24h | P1 |
| Inline interceptor latency | p99 > 10ms | P1 |
| Signal registry cache miss | > 1% | P2 |
| ML model score drift | > 0.05 delta | P2 |
| False positive rate | > 5% | P2 |

---

## SECTION FDE-J — APPEAL WORKFLOW

### J1 — Appeal Eligibility

Users may appeal:
- Account freeze (ACT-FREEZE)
- Feature restriction (ACT-RESTRICT)
- Reward clawback (ACT-CLAWBACK)
- Revenue hold (ACT-HOLD)
- Fraud flag (ACT-FLAG)

Users may NOT appeal:
- Audit log entries (immutable by design)
- Session terminations (GD/Dojo — enforced by protocol)
- ACT-LOG (no operational impact)

### J2 — Appeal Flow

```
User submits appeal
        ↓
Appeal validation (entity_id + fraud_flag_id required)
        ↓
Ops queue assignment (SLA: 48h business hours)
        ↓
Reviewer examines fraud_scores + signal_ids + context_json
        ↓
Outcome: UPHELD / OVERTURNED / PARTIAL
        ↓
If OVERTURNED:
  → ACT-REVERSE executed
  → fraud_flag.status = RESOLVED
  → trust_score_offset increased (false positive credit)
  → false_positive_rate metric updated
If UPHELD:
  → Existing actions remain
  → Appeal outcome logged
  → Further escalation path offered (compliance board)
```

### J3 — Appeal Anti-Abuse

- Maximum 3 active appeals per account at any time
- Frivolous appeal penalty: ACT-WATCH added
- Appeal misuse pattern → ACT-RESTRICT on appeal feature

---

## SECTION FDE-K — COMPLIANCE & LEGAL ALIGNMENT

### K1 — Data Retention

| Data Type | Retention Period | Storage |
|---|---|---|
| fraud_signals | 7 years | PostgreSQL + MinIO cold |
| fraud_scores | 7 years | PostgreSQL + MinIO cold |
| fraud_flags | 7 years (or legal hold) | PostgreSQL |
| fraud_actions | Permanent (immutable) | PostgreSQL + WORM archive |
| revenue_fraud_holds | 10 years | MinIO WORM |
| referral_fraud_flags | 3 years | PostgreSQL |

### K2 — Privacy Controls

- Fraud signals contain behavioral metadata only
- No audio, video, or speech content ever stored
- PII in context_json encrypted at rest (AES-256)
- PII access requires fraud.reviewer role + audit log entry
- GDPR/DPDP subject access: fraud signal data exportable in anonymized form
- Right to erasure: PII fields erasable; event records retained in anonymized form

### K3 — Minor (Under-18) Protections

- Fraud flags on minor accounts require parent/guardian notification
- ACT-FREEZE on minor accounts requires compliance lead approval
- Royalty and idea ownership fraud involving minors escalated to legal team
- Innovation Trust Governance Service notified on all minor-related fraud events

### K4 — Cross-Border Handling

- Jurisdiction tag required on all fraud events (IN / EU / US / OTHER)
- GDPR-jurisdiction fraud events: DPO notified on CRITICAL severity
- COPPA-jurisdiction: Parental consent chain verified before any minor fraud action
- Cross-border revenue fraud: Legal team auto-notified

---

## SECTION FDE-L — INTEGRATION WIRING

### L1 — Services FDE Consumes Signals From

| Service | Signal Type |
|---|---|
| Auth Service | Login anomaly, device mismatch, MFA bypass attempt |
| Voice GD Orchestrator | Turn compliance, mic duration, interrupt counts |
| Dojo Match Engine | Score submissions, session context, scenario patterns |
| Revenue Ingestion Gateway | Revenue report payloads |
| Billing & Subscription Service | Refund events, chargeback webhooks, plan changes |
| Certification & Belt Engine | Promotion requests, mentor records |
| Referral Engine | Code acceptance events, reward triggers |
| Content Service | Post events, report events |
| User Service | Profile changes, account linking |
| Notification Service | Delivery failure patterns (OTP, email) |

### L2 — Services FDE Dispatches Actions To

| Action Target | Action Types |
|---|---|
| Auth Service | Session revocation, account freeze |
| API Gateway (Kong) | Rate limit overrides, IP block |
| Billing Service | Invoice hold, refund block |
| Revenue Ingestion Gateway | Revenue hold |
| Notification Service | Fraud alert to user / ops |
| Admin Governance Service | Escalation queue entry |
| Analytics Service | Fraud metric events |
| Royalty Wallet Service | Clawback initiation |
| Certification Engine | Promotion block |

### L3 — Event Bus Contracts

**FDE publishes to Kafka:**
```
fraud.signal.fired          { signal_code, entity_id, tenant_id, score_delta }
fraud.score.computed        { entity_id, score, classification, model_version }
fraud.action.dispatched     { action_code, entity_id, triggered_at }
fraud.flag.raised           { flag_id, entity_id, severity }
fraud.revenue.held          { hold_id, business_id, signals[] }
fraud.appeal.resolved       { appeal_id, outcome, entity_id }
```

**FDE consumes from Kafka:**
```
user.created
gd.completed
dojo.match.scored
belt.eligible
invoice.generated
referral.accepted
revenue.submitted
content.posted
auth.anomaly
billing.refund.requested
billing.chargeback.received
```

---

## SECTION FDE-M — TECHNOLOGY BINDING

All technology choices below are LOCKED. No substitution without version bump.

| Layer | Technology | Purpose |
|---|---|---|
| Signal Cache | Redis | Real-time signal weight lookup + dedup |
| Score Storage | PostgreSQL | Immutable fraud score records |
| Event Bus | Kafka | Async signal ingestion + action dispatch |
| Batch Processing | Apache Airflow | Scheduled fraud scan jobs |
| ML Model Serving | Traditional ML (Gradient Boosting) | Fraud score computation |
| Model Registry | Model Registry Service | Version control of FDE models |
| Feature Store | Feature Store Service | Centralized ML feature access |
| Audit Archive | MinIO (WORM policy) | Long-term evidence storage |
| Vector Similarity | Vector Database Service | Idea duplication / referral loop detection |
| Observability | Prometheus + Grafana + Loki | Metrics, dashboards, logs |
| Tracing | OpenTelemetry | Distributed fraud signal tracing |
| Secrets | HashiCorp Vault | FDE service credentials |
| Policy Enforcement | Open Policy Agent | Cross-service action authorization |
| SIEM | Wazuh | Security event correlation with FDE signals |

---

## SECTION FDE-N — INTERN IMPLEMENTATION GUIDE

### N1 — Service Bootstrap

```bash
# Namespace
kubectl create namespace ecoskiller-fde

# Apply manifests
kubectl apply -f infra/k8s/fde/

# Verify
kubectl get pods -n ecoskiller-fde
# Expected: fde-interceptor, fde-stream-processor, fde-batch-worker, fde-api = Running
```

### N2 — Required File Outputs (AI Must Generate)

```
/services/fraud-detection/
  src/
    signals/         -- one file per signal domain (identity, gd, dojo, revenue, etc.)
    scoring/         -- FraudScoringEngine.py (Gradient Boosting wrapper)
    actions/         -- ActionDispatcher.py
    gates/           -- RevenueIngesionGate.py, GDSessionMonitor.py
    api/             -- Internal API routes
    consumers/       -- Kafka consumer handlers
    jobs/            -- Airflow DAGs for batch detection
  tests/
    unit/
    integration/
  migrations/
    V001__create_fraud_tables.sql
  k8s/
    deployment.yaml
    service.yaml
    configmap.yaml
    hpa.yaml
```

### N3 — Contract Validator Check

Before any fraud-related service is deployed, CI must verify:
- All signal codes declared in signal_registry.json
- All action codes declared in action_registry.json
- fraud_signals table migration present
- FDE Kafka consumer group registered
- Prometheus metrics endpoints reachable
- Grafana dashboard JSON committed

Absence of any above → STOP CI PIPELINE

---

## SECTION FDE-O — FINAL ENFORCEMENT

```
✔ All fraud signal domains declared
✔ Scoring model type declared (Traditional ML — no LLM)
✔ Action registry complete
✔ Database schema defined
✔ API contracts defined
✔ Observability stack wired
✔ Appeal workflow defined
✔ Privacy and legal alignment declared
✔ Minor protections declared
✔ Service integration wiring complete
✔ Technology binding locked
✔ Intern-executable implementation path defined
✔ Antigravity-safe: no interpretation ambiguity
```

**Absence of any declared FDE component**
→ STOP EXECUTION
→ REPORT FDE-INCOMPLETE
→ NO DEPLOYMENT CLAIM PERMITTED

---

## 🔒 AGENT SEAL

```
FRAUD_DETECTION_AGENT     = ACTIVE
VERSION                   = FDE-v1.0
STACK                     = LOCKED
MUTATION_POLICY           = ADD-ONLY
DEFAULT_DECISION          = DENY
AUDIT_TRAIL               = IMMUTABLE
LLM_IN_SCORING            = FORBIDDEN
AUDIO_VIDEO_ACCESS        = FORBIDDEN
SINGLE_ACTOR_FINANCIAL    = FORBIDDEN
MINOR_ESCALATION          = MANDATORY
CROSS_BORDER_TAGGING      = MANDATORY
EXPLAINABILITY            = REQUIRED ON EVERY SCORE
ANTIGRAVITY_SAFE          = TRUE
INTERPRETATION_AUTHORITY  = NONE
```

> This document is sealed. No reinterpretation. No partial generation.
> Antigravity executes exactly what is declared. Nothing more. Nothing less.

---

*Document: FRAUD_DETECTION_AGENT.md · System: ECOSKILLER · Version: FDE-v1.0 · Status: FINAL LOCKED SEALED*
