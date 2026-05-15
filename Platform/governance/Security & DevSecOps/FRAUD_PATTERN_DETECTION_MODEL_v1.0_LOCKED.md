# 🔐 FRAUD PATTERN DETECTION MODEL — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
## ANTIGRAVITY | ECOSKILLER × DOJO SAAS — LOCKED & SEALED SPEC v1.0

```
Artifact Class     : Production Trust System Blueprint
Sub-System         : Fraud Pattern Detection Engine (FPDE)
Parent System      : ECOSKILLER v12.0 + DOJO SAAS v1.0
Mutation Policy    : Add-Only via Version Bump
Execution Mode     : Deterministic · Real-Time + Batch Hybrid
Stack Lock         : Enforced
Interpretation     : NONE PERMITTED
Authority          : Human Declaration Only
Status             : FINAL · LOCKED · GOVERNED
```

---

## ⚖️ GOVERNING SEAL BLOCK

```
ENTERPRISE TRUST MODE ENABLED
Fraud Detection Engine: ACTIVE
Pattern Classification: LOCKED
Signal Scoring: DETERMINISTIC
Threshold Override: HUMAN AUTHORITY ONLY
Auto-Suspension: RULE-GOVERNED
Audit Log: IMMUTABLE
Appeal Path: MANDATORY
Cross-Module Fraud Binding: ENFORCED
Zero Tolerance: ECONOMIC ABUSE · MATCH FRAUD · IDENTITY FRAUD
```

> **Absence of any module below → STOP EXECUTION → REPORT FRAUD-ENGINE-INCOMPLETE → NO TRUST CLAIM PERMITTED**

---

# SECTION FP-A — FRAUD TAXONOMY REGISTRY (MASTER CLASSIFICATION)

All fraud types that the FPDE must detect are classified into **7 Primary Fraud Families**.
No fraud family may be excluded. Detection coverage is mandatory across all families.

---

## Family 1 — IDENTITY FRAUD

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| ID-F01 | Multi-Account Farming | Same device fingerprint, IP cluster, or behavioral biometric across >1 account |
| ID-F02 | Fake Profile Creation | Profile age < 24h + rapid credential submission + no organic session behavior |
| ID-F03 | Stolen Identity Injection | Institute email domain valid but ownership unverifiable; Aadhaar/passport mismatch |
| ID-F04 | Ghost Account Network | Accounts with zero organic activity but high match participation |
| ID-F05 | Role Impersonation | Non-certified user assuming mentor/recruiter API role via token replay |
| ID-F06 | Verification Bypass | Forced institution domain match without email OTP confirmation |

**Detection Authority:** Identity Engine + RBAC Layer + Behavioral Biometric Module

---

## Family 2 — MATCH & SCORING FRAUD

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| MS-F01 | Match Farming | Same pair of users producing >3 matches within 24h with identical high scores |
| MS-F02 | Reciprocal High Scoring | User A scores User B high → User B scores User A high across ≥2 consecutive matches |
| MS-F03 | Peer Score Clustering Anomaly | Peer score variance < 0.5 SD across all participants in a match (statistical impossibility) |
| MS-F04 | Rating Inflation Cluster | Cohort of ≥3 users whose ratings increase >15% in 7 days through inter-matches only |
| MS-F05 | Scenario Abandonment Exploit | User exits scenario post-scoring-window but before completion to lock favorable partial score |
| MS-F06 | Score Override Abuse | Mentor triggers override on >20% of their match pool in 30 days |
| MS-F07 | First-Speaker Exploit | Same user wins 100% of matches where they hold first-speaker advantage over 10+ matches |
| MS-F08 | Coordinated Throw | User deliberately scores minimum to lose, enabling opponent belt advancement |
| MS-F09 | Time Manipulation | Match timestamp vs WebRTC session timestamp divergence >5 minutes |
| MS-F10 | Replay Injection | Replay submitted does not match live WebRTC recording hash |

**Detection Authority:** Scoring Engine + Match Engine + Analytics Engine + Audit Log

---

## Family 3 — BELT & CERTIFICATION FRAUD

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| BC-F01 | Belt Laundering | Belt awarded on matches flagged by ≥2 fraud signals without governance review |
| BC-F02 | Mentor Collusion — Certification | Mentor certifies >80% of students from single institution in <30 days |
| BC-F03 | Auto-Promotion Bypass | Belt promotion triggered without completing all 5 mandatory gates |
| BC-F04 | Rubric Version Mismatch | Certification issued referencing a retired or unreleased rubric version tag |
| BC-F05 | Low-Confidence Belt Push | Belt awarded on match with confidence_score < 0.65 without mentor board review |
| BC-F06 | Calibration Drift Abuse | Mentor who has failed calibration tolerance still issues certifications |
| BC-F07 | Belt Resale Signal | Certified user shares credential access with uncertified user (device + session pattern) |

**Detection Authority:** Belt Engine + Certification Engine + Rater Calibration Module

---

## Family 4 — ECONOMIC & BILLING FRAUD

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| EC-F01 | Refund Abuse | Same user initiates ≥3 refund requests within 60 days across subscription cycles |
| EC-F02 | Mentor Collusion Billing | Mentor charges for sessions with students who are ghost accounts |
| EC-F03 | Fake Tournament Loop | User enters tournament → exits after prize eligibility window → re-enters using second account |
| EC-F04 | Coupon Stacking Abuse | Single user redeems >1 coupon per billing cycle through multi-account coupon relay |
| EC-F05 | Seat Farming | Enterprise seat purchased and distributed to non-employee accounts |
| EC-F06 | Certification Fee Laundering | Certification fee paid via third-party wallet, then disputed after certificate issuance |
| EC-F07 | GST/VAT Phantom Entity | Billing address country does not match IP geolocation or institute registration |
| EC-F08 | Affiliate Fraud | Referral rewards claimed using self-referral via multi-account or spoofed referral chain |

**Detection Authority:** Billing Engine + Payment Abstraction Layer + Fraud Signal Bus

---

## Family 5 — MENTOR & INSTITUTIONAL FRAUD

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| MF-F01 | Mentor Favoritism Pattern | Single mentor awards scores >2 SD above platform mean to ≥5 consecutive students from same institution |
| MF-F02 | Mentor Score Drift | Mentor calibration delta > ±15% from gold-standard across 3 calibration cycles |
| MF-F03 | Ghost Mentor Operation | Mentor license active but zero live match supervision in >30 days |
| MF-F04 | Institutional Leaderboard Manipulation | Institution submits bulk match results in final 24h of ranking cycle |
| MF-F05 | Fake Institute Registration | Institute registered without verifiable domain + GST/accreditation record |
| MF-F06 | Uncertified Mentor in Ranked Match | Mentor without valid certification authority presiding over belt or ranked match |

**Detection Authority:** Mentor Control Engine + Rater Calibration Module + Institutional Validator

---

## Family 6 — INTEGRATION & DATA FRAUD (Ecoskiller Integration Engine)

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| IF-F01 | Synthetic Work Data Injection | GitHub commit timestamps inconsistent with account creation date; bulk commits in <1hr |
| IF-F02 | Credential Stuffing via OAuth | OAuth token reused across multiple Ecoskiller accounts within same session window |
| IF-F03 | Fake Skill Extraction Amplification | Tool integration returns skill signals that contradict all other platform data for user |
| IF-F04 | Migration Data Poisoning | Migrated records contain uniform score fields (all identical values across 100+ records) |
| IF-F05 | Webhook Replay Attack | Same webhook event_id received >1 time from external tool within 60-second dedup window |
| IF-F06 | API Credential Sharing | Single API key used from >3 distinct IP subnets in <1 hour |
| IF-F07 | Phantom Tool Connection | Tool connected via OAuth but zero data sync events recorded after 72h |
| IF-F08 | UWDF Manipulation | Universal Work Data Format record manually edited post-normalization before skill extraction |

**Detection Authority:** Ecoskiller Integration Engine (EIE) + Data Normalization AI + Webhook Validator

---

## Family 7 — BEHAVIORAL & SAFETY FRAUD

| Code | Pattern | Trigger Signal |
|------|---------|---------------|
| BF-F01 | Intimidation-Driven Score Suppression | User with harassment flag shows statistically lower peer scores from intimidated opponents |
| BF-F02 | Coordinated Harassment Ring | ≥3 users filing concurrent harassment reports against same target within 48h |
| BF-F03 | Fake Abuse Report Weaponization | User files ≥5 abuse reports in 30 days; all dismissed after review |
| BF-F04 | Match Exit Manipulation | User triggers forced match exit after opponent has scored but before own scoring window |
| BF-F05 | Session Recording Consent Bypass | Recording commenced without capturing consent event in audit log |
| BF-F06 | Spectator Collusion Signal | Spectator account logged into same network as competitor during live championship match |

**Detection Authority:** Behavioral Safety Engine + Audit Log + Mentor Emergency Override

---

# SECTION FP-B — SIGNAL SCORING MATRIX

Every fraud detection event produces a **Fraud Signal Score (FSS)** between 0.00 – 1.00.
Scores above threshold trigger automated actions. No score may be manually suppressed without governance log entry.

```
FSS Range       → Status
0.00 – 0.39     → MONITOR (log only, no action)
0.40 – 0.59     → ALERT   (notify trust team, flag in dashboard)
0.60 – 0.79     → HOLD    (freeze scoring output, matches pending)
0.80 – 0.94     → SUSPEND (account restricted, appeal path opened)
0.95 – 1.00     → BLOCK   (full access revoked, escalation mandatory)
```

### Signal Weight Table

| Signal Category | Base Weight |
|----------------|-------------|
| Device fingerprint match (multi-account) | 0.85 |
| Reciprocal high score (≥2 matches) | 0.70 |
| Calibration drift (mentor, 3 cycles) | 0.75 |
| Refund abuse (≥3 in 60 days) | 0.65 |
| Replay hash mismatch | 0.95 |
| OAuth token reuse (multi-account) | 0.90 |
| Bulk match result injection (24h window) | 0.80 |
| Fake abuse report weaponization | 0.55 |
| Synthetic GitHub commit cluster | 0.72 |
| Belt issued on failed gate | 1.00 |

### Composite Score Rule

```
FSS_composite = max(individual signals) + Σ(supporting signals × 0.10)
Cap at 1.00
```

---

# SECTION FP-C — DETECTION ENGINE ARCHITECTURE

## Core Components (All Mandatory)

```
Fraud Pattern Detection Engine (FPDE)
│
├── Real-Time Signal Processor (RSP)
│   ├── WebSocket Event Listener
│   ├── Match Event Interceptor
│   ├── Score Submit Validator
│   └── OAuth Event Monitor
│
├── Batch Analysis Engine (BAE)
│   ├── Daily Rating Trend Scanner
│   ├── Weekly Peer Score Variance Analyzer
│   ├── Monthly Billing Pattern Auditor
│   └── Quarterly Mentor Calibration Reviewer
│
├── Behavioral Biometric Module (BBM)
│   ├── Device Fingerprint Resolver
│   ├── Session Velocity Profiler
│   ├── Interaction Pattern Classifier
│   └── Network Topology Mapper
│
├── AI Pattern Classifier (APC)
│   ├── Reciprocal Scoring Graph Detector
│   ├── Cluster Anomaly Model
│   ├── Time-Series Drift Detector
│   └── NLP Dispute Signal Extractor
│
├── Fraud Signal Bus (FSB)
│   ├── Event-driven signal routing
│   ├── Signal deduplication (60s window)
│   ├── Cross-module signal aggregation
│   └── FSS computation layer
│
└── Governance Response Dispatcher (GRD)
    ├── MONITOR → Audit log write
    ├── ALERT   → Trust dashboard notification
    ├── HOLD    → Score freeze command to Scoring Engine
    ├── SUSPEND → Account restriction + appeal ticket open
    └── BLOCK   → Full revoke + human escalation ping
```

## Transport Lock

```
Fraud Signals     → Internal Event Bus (NOT exposed to API)
Trust Dashboard   → WebSocket (read-only trust team feed)
Governance Actions→ REST API with dual-key authorization
Audit Writes      → Immutable append-only log (no update/delete)
```

---

# SECTION FP-D — DATABASE SCHEMA (MANDATORY)

All entities below are required. None optional. Absence → STOP EXECUTION.

```sql
-- Fraud Signal Registry
FraudSignal (
  signal_id        UUID PRIMARY KEY,
  detected_at      TIMESTAMPTZ NOT NULL,
  fraud_family     VARCHAR(10) NOT NULL,   -- ID, MS, BC, EC, MF, IF, BF
  fraud_code       VARCHAR(10) NOT NULL,   -- e.g. MS-F01
  entity_type      VARCHAR(30) NOT NULL,   -- user | match | mentor | institution | integration
  entity_id        UUID NOT NULL,
  raw_evidence     JSONB NOT NULL,
  fss_score        DECIMAL(4,2) NOT NULL,
  signal_status    VARCHAR(10) NOT NULL,   -- MONITOR | ALERT | HOLD | SUSPEND | BLOCK
  created_by       VARCHAR(30) DEFAULT 'FPDE-AUTO'
)

-- Fraud Case (aggregated multi-signal event)
FraudCase (
  case_id          UUID PRIMARY KEY,
  opened_at        TIMESTAMPTZ NOT NULL,
  case_status      VARCHAR(20) NOT NULL,   -- OPEN | UNDER_REVIEW | CLOSED_CONFIRMED | CLOSED_DISMISSED
  entity_type      VARCHAR(30) NOT NULL,
  entity_id        UUID NOT NULL,
  composite_fss    DECIMAL(4,2) NOT NULL,
  assigned_to      UUID,                  -- Trust team member
  resolution_note  TEXT,
  resolved_at      TIMESTAMPTZ,
  appeal_eligible  BOOLEAN DEFAULT TRUE
)

-- Signal → Case Mapping
FraudSignalCase (
  signal_id   UUID REFERENCES FraudSignal,
  case_id     UUID REFERENCES FraudCase,
  PRIMARY KEY (signal_id, case_id)
)

-- Governance Action Log (IMMUTABLE)
FraudActionLog (
  action_id        UUID PRIMARY KEY,
  case_id          UUID REFERENCES FraudCase,
  action_type      VARCHAR(20) NOT NULL,   -- SUSPEND | BLOCK | REINSTATE | ESCALATE
  executed_by      UUID NOT NULL,          -- human authority only for BLOCK/REINSTATE
  executed_at      TIMESTAMPTZ NOT NULL,
  rationale        TEXT NOT NULL,
  version_tag      VARCHAR(20) NOT NULL    -- ties to system version at time of action
)

-- Match Fraud Flag (linked to Match entity)
MatchFraudFlag (
  flag_id          UUID PRIMARY KEY,
  match_id         UUID NOT NULL,          -- FK to Match entity
  fraud_codes      VARCHAR(10)[] NOT NULL,
  flagged_at       TIMESTAMPTZ NOT NULL,
  belt_hold        BOOLEAN DEFAULT TRUE,   -- flagged matches cannot count toward belt
  cleared_at       TIMESTAMPTZ,
  cleared_by       UUID
)

-- Mentor Fraud Profile
MentorFraudProfile (
  mentor_id             UUID PRIMARY KEY,
  calibration_failures  INTEGER DEFAULT 0,
  favoritism_score      DECIMAL(4,2) DEFAULT 0,
  override_rate_30d     DECIMAL(5,2) DEFAULT 0,
  last_reviewed_at      TIMESTAMPTZ,
  authority_suspended   BOOLEAN DEFAULT FALSE
)

-- Integration Fraud Log
IntegrationFraudLog (
  log_id           UUID PRIMARY KEY,
  integration_id   UUID NOT NULL,
  tool_name        VARCHAR(50) NOT NULL,
  fraud_code       VARCHAR(10) NOT NULL,
  detected_at      TIMESTAMPTZ NOT NULL,
  data_snapshot    JSONB NOT NULL,
  action_taken     VARCHAR(20) NOT NULL    -- QUARANTINE | REJECT | FLAG
)
```

**Row-Level Security: Enforced per tenant. Cross-tenant fraud data access forbidden.**

---

# SECTION FP-E — API CONTRACTS (MANDATORY)

All routes below are required. None optional.

```
Internal (Trust Team Only):
GET    /fraud/signals                 → List signals with filter by family/status/date
GET    /fraud/signals/{signal_id}     → Signal detail + evidence
GET    /fraud/cases                   → Case queue for trust team
GET    /fraud/cases/{case_id}         → Full case detail with signal chain
PATCH  /fraud/cases/{case_id}/resolve → Human resolution (requires dual-key auth)
POST   /fraud/cases/{case_id}/escalate→ Escalate to governance board

Match-Linked:
GET    /matches/{match_id}/fraud-flags → Flags on specific match
POST   /matches/{match_id}/clear-flag  → Human clearance with rationale (immutable logged)

Mentor Trust:
GET    /mentors/{id}/fraud-profile     → Mentor calibration + favoritism profile
POST   /mentors/{id}/suspend-authority → Suspend certification authority (human only)

Billing Fraud:
GET    /billing/fraud/signals          → Billing-specific fraud signals
POST   /billing/accounts/{id}/freeze   → Freeze billing account pending review

Integration Fraud:
GET    /integrations/{id}/fraud-log    → Tool-specific fraud events
POST   /integrations/{id}/quarantine   → Quarantine synced data pending review

Appeals:
POST   /fraud/cases/{case_id}/appeal   → User submits appeal
GET    /fraud/cases/{case_id}/appeal   → Appeal status
PATCH  /fraud/appeals/{id}/resolve     → Governance board resolution (immutable)
```

**All endpoints: JWT auth + per-route RBAC enforced. No public access.**

---

# SECTION FP-F — DETECTION ALGORITHMS (LOCKED LOGIC)

## Algorithm 1 — Reciprocal High Scoring Detector

```
INPUT: match_id, peer_score_matrix

FOR each unique user_pair (A, B) in last 90 days:
  matches_AB = matches where A scored B AND B scored A
  IF len(matches_AB) >= 2:
    FOR each match in matches_AB:
      score_A_by_B = peer_score[match][B→A]
      score_B_by_A = peer_score[match][A→B]
      IF score_A_by_B > platform_mean_peer_score + 1.5 SD
      AND score_B_by_A > platform_mean_peer_score + 1.5 SD:
        reciprocal_count++

IF reciprocal_count >= 2:
  EMIT signal(MS-F02, FSS=0.70, entity=[A,B])
```

## Algorithm 2 — Rating Inflation Cluster Detector

```
INPUT: user_cohort, rating_snapshots, time_window=7d

FOR each cohort C of size >= 3:
  rating_delta = rating_now - rating_7d_ago
  match_sources = sources of matches contributing to delta

  IF rating_delta > 15% of prior rating
  AND match_sources contain only intra-cohort matches:
    inflation_score = rating_delta / expected_delta_by_platform_norm
    IF inflation_score > 2.0:
      EMIT signal(MS-F04, FSS=0.75 + min(0.15, inflation_score/10), entity=cohort)
```

## Algorithm 3 — Multi-Account Device Resolver

```
INPUT: login_event stream

FOR each login_event E:
  fingerprint = hash(device_id + browser_ua + screen_res + timezone + font_set)
  existing_accounts = lookup(fingerprint, time_window=30d)

  IF len(existing_accounts) > 1 AND accounts have different user_ids:
    shared_ip_subnets = count_shared_ip_subnet(accounts)
    IF shared_ip_subnets >= 1:
      EMIT signal(ID-F01, FSS=0.85, entity=all_matching_accounts)
```

## Algorithm 4 — Mentor Favoritism Detector

```
INPUT: mentor_id, score_history, time_window=30d

scores_by_mentor = mentor_scores WHERE mentor_id = mentor_id AND period = 30d
platform_mean    = global_mean_mentor_score(period=30d)
platform_sd      = global_sd_mentor_score(period=30d)

institution_groups = group_by(student.institution_id, scores_by_mentor)

FOR each group G:
  IF len(G) >= 5:
    group_mean = mean(G.scores)
    z_score    = (group_mean - platform_mean) / platform_sd
    IF z_score > 2.0:
      EMIT signal(MF-F01, FSS=min(0.95, 0.55 + z_score * 0.08), entity=mentor_id)
```

## Algorithm 5 — Replay Hash Integrity Validator

```
INPUT: replay_id, submitted_replay_hash, match_id

live_recording_hash = fetch_recording_hash(match_id, source=LiveKit)
submitted_hash      = replay.hash

IF submitted_hash != live_recording_hash:
  EMIT signal(MS-F10, FSS=0.95, entity=match_id)
  SET MatchFraudFlag.belt_hold = TRUE
  FREEZE belt computation for match_id
```

## Algorithm 6 — Synthetic Integration Data Detector

```
INPUT: integration_event, tool=GitHub

commits = fetch_commits(user_id, tool=GitHub, time_window=30d)

IF len(commits) > 50 AND time_span(commits) < 3600 seconds:
  commit_messages_entropy = shannon_entropy(commit_messages)
  IF commit_messages_entropy < 2.5:  // near-identical messages
    EMIT signal(IF-F01, FSS=0.72, entity=integration_id)
    QUARANTINE skill_extraction_output for review

account_age = days_since_github_account_creation
ecoskiller_age = days_since_ecoskiller_account_creation

IF account_age < 7 AND commit_count > 200:
  EMIT signal(IF-F01, FSS=0.85, entity=integration_id)
```

---

# SECTION FP-G — AUTOMATED RESPONSE PROTOCOL

All responses are deterministic. No partial response permitted.

```
FSS MONITOR (0.00–0.39):
  → WRITE to FraudSignal (status=MONITOR)
  → No user-facing action
  → Aggregate for trend analysis

FSS ALERT (0.40–0.59):
  → WRITE to FraudSignal (status=ALERT)
  → NOTIFY trust team dashboard (real-time)
  → ATTACH signal to user/entity fraud profile
  → FLAG in Trust Score calculation

FSS HOLD (0.60–0.79):
  → WRITE to FraudSignal (status=HOLD)
  → FREEZE: scoring output pending for entity
  → FREEZE: belt eligibility for related matches
  → OPEN FraudCase (status=OPEN)
  → ASSIGN to trust team queue
  → NOTIFY user: "Your account is under standard trust review"

FSS SUSPEND (0.80–0.94):
  → WRITE to FraudSignal (status=SUSPEND)
  → RESTRICT: account access (read-only, no match entry)
  → FREEZE: all pending payments/certifications
  → OPEN FraudCase + set appeal_eligible=TRUE
  → OPEN appeal ticket automatically
  → NOTIFY user: formal notice with appeal instructions
  → REQUIRE human review within 72h

FSS BLOCK (0.95–1.00):
  → WRITE to FraudSignal (status=BLOCK)
  → REVOKE: full access
  → REVOKE: all active sessions (device registry clear)
  → FREEZE: all financial instruments
  → ESCALATE: to Governance Board
  → REQUIRE: senior human authority for any reinstatement
  → NOTIFY user: formal block notice + escalated appeal path
  → LOG: FraudActionLog with version_tag
```

**Auto-reinstatement: FORBIDDEN. All reinstatements require human authority + dual-key authorization.**

---

# SECTION FP-H — CROSS-ENGINE INTEGRATION MAP

Fraud detection is not a standalone module. It is event-driven and wired to all core engines.

```
Match Engine          → emits match_completed event
                         → FPDE validates scoring integrity (MS-F01 to MS-F10)

Scoring Engine        → emits score_submitted event
                         → FPDE runs peer variance check (MS-F03)

Belt Engine           → emits belt_check_initiated event
                         → FPDE validates: no active MatchFraudFlag on contributing matches
                         → Belt held if any flag active

Certification Engine  → emits certification_requested event
                         → FPDE validates: mentor authority status (MF-F06, BC-F06)
                         → FPDE validates: all gate completions (BC-F03)

Billing Engine        → emits payment_event, refund_requested events
                         → FPDE checks refund pattern (EC-F01)
                         → FPDE checks coupon abuse (EC-F04)

Integration Engine    → emits tool_sync_completed event
                         → FPDE runs data authenticity check (IF-F01 to IF-F08)

Replay Engine         → emits replay_finalized event
                         → FPDE validates replay hash (MS-F10)

Tournament Engine     → emits tournament_result event
                         → FPDE checks fake tournament loop (EC-F03)

Mentor Control Engine → emits override_executed event
                         → FPDE checks override rate (MS-F06, MF-F01)
```

**All integrations: Event-driven only. No manual sync permitted.**

---

# SECTION FP-I — TRUST SCORE MODEL

Every user, mentor, institution, and integration connection maintains a **Trust Score**.

```
Trust Score Range: 0 – 1000

Starting Score:
  Unverified user      : 300
  Verified user        : 500
  Verified institution : 600
  Certified mentor     : 650

Modifiers:
  +50   Clean match history (30d, no flags)
  +30   Calibration pass (mentor)
  +20   Successful certification issued (mentor)
  -100  FSS ALERT signal
  -200  FSS HOLD signal
  -350  FSS SUSPEND signal
  -500  FSS BLOCK signal
  -150  Dismissed appeal (bad faith)
  +200  Appeal upheld (wrongful flag)

Thresholds:
  < 200  → Cannot enter ranked matches
  < 100  → Cannot issue certifications (mentor)
  < 50   → Cannot participate in tournaments
  0      → Full platform restriction
```

**Trust Score updates: Real-time on signal, batch recalculated nightly.**

---

# SECTION FP-J — APPEALS & GOVERNANCE PROTOCOL

Every automated action at FSS ≥ 0.80 must include an appeal path.

## Appeal Workflow

```
Step 1: User receives SUSPEND or BLOCK notification
Step 2: System auto-generates appeal ticket (FraudCase.appeal_eligible = TRUE)
Step 3: User submits evidence via /fraud/cases/{id}/appeal
Step 4: Trust team reviews within:
          SUSPEND: 72 hours
          BLOCK:   24 hours (escalated)
Step 5: Governance Board decision if unresolved in Step 4
Step 6: Decision logged immutably in FraudActionLog
Step 7: If upheld → reinstate + Trust Score +200
        If dismissed → log reason + Trust Score -150
```

## Governance Board Authority

Governance Board has authority over:
- All BLOCK decisions
- All belt/certification revocations
- Mentor authority revocations
- Institutional fraud cases
- Appeals dismissed by trust team but contested

**Governance Board decisions: Versioned, logged, non-overridable by any single individual.**

---

# SECTION FP-K — OBSERVABILITY & ALERTING LOCK

Fraud system must be fully observable in production.

## Required Dashboards

```
Fraud Signal Volume (by family, hourly/daily)
FSS Distribution (heatmap by fraud code)
Trust Score Distribution (platform-wide)
Case Resolution Time (SLA tracking)
Mentor Fraud Profile Overview
Integration Health + Anomaly Rate
Belt Hold Queue (pending clearance)
Active Suspensions + Block Count
```

## Required Alerts

```
CRITICAL: FSS 1.00 signal detected                → immediate ping: trust team lead
CRITICAL: Belt awarded on unflagged match post-hold→ immediate ping: scoring authority
HIGH:     Mentor calibration failure (3rd cycle)   → 1h response SLA
HIGH:     Refund pattern spike >20% in 24h         → billing team alert
MEDIUM:   Integration sync anomaly cluster         → trust team queue
MEDIUM:   SUSPEND count >10 in 1 hour              → possible false-positive storm alert
LOW:      Monitor signal volume spike              → weekly digest
```

## Required Metrics (Prometheus Labels)

```
fpde_signal_total{family, code, status}
fpde_fss_score_histogram{entity_type}
fpde_case_resolution_seconds{outcome}
fpde_trust_score_gauge{entity_type, bucket}
fpde_belt_hold_active_count
fpde_integration_quarantine_count{tool_name}
```

---

# SECTION FP-L — COMPLIANCE & LEGAL LOCK

Fraud detection system must comply with:

```
Data Retention:
  FraudSignal records    → retain 7 years
  FraudActionLog         → retain permanently (immutable)
  Evidence JSONB payloads→ retain 3 years, encrypted at rest

Privacy:
  PII within fraud evidence encrypted at rest (AES-256)
  Access to evidence JSONB: restricted to trust team + governance board only
  Audit exports: anonymized unless under legal hold

Legal Hold Support:
  System must support: freeze + export of all fraud records for entity under legal hold
  Export format: signed JSON archive with integrity hash

GDPR / Data Portability:
  Users may request data export: excludes fraud evidence (law enforcement exception)
  Users may request deletion: deferred if active FraudCase is open

Tenant Isolation:
  Cross-tenant fraud signal access: FORBIDDEN
  Multi-tenant fraud correlation: only via anonymized aggregate signals
```

---

# SECTION FP-M — VERSIONING & CHANGE GOVERNANCE

```
Fraud Pattern Version Tag: FP-v1.0
Parent System Version:     ECOSKILLER v12.0 / DOJO v1.0

Allowed without version bump:
  Add new fraud code to existing family
  Adjust FSS weight within ±0.05 of current value
  Add new dashboard metric
  Add new alert rule

Requires version bump (FP-v1.x or FP-v2.0):
  Add new Fraud Family
  Change FSS threshold boundaries
  Change automated response action for any FSS range
  Change database schema entities
  Change appeal SLA timelines
  Change Trust Score model formula

Version bump requires:
  Human declaration
  Governance board review
  Backward compatibility impact note
  Audit log entry with version tag
```

---

# SECTION FP-N — FINAL GOVERNANCE SEAL (MASTER PROMPT INSERT)

```
BEGIN LOCKED FRAUD DETECTION ARTIFACT

System Role         : Fraud Pattern Detection Engine (FPDE)
Parent              : ECOSKILLER v12.0 + DOJO SAAS v1.0
Execution           : Deterministic · Real-Time + Batch Hybrid
Fraud Families      : 7 (ID · MS · BC · EC · MF · IF · BF) — ALL ACTIVE
Signal Scoring      : FSS 0.00–1.00 — LOCKED THRESHOLDS
Detection Algorithms: 6 Core — LOCKED LOGIC
Response Protocol   : 5-Level Automated — HUMAN AUTHORITY ABOVE FSS 0.94
Trust Score         : Entity-Level · Real-Time + Nightly Batch
Appeals             : MANDATORY path for SUSPEND + BLOCK
Governance Board    : ACTIVE — Final arbitration authority
Audit Log           : IMMUTABLE — 7-year retention minimum
Cross-Engine Wiring : EVENT-DRIVEN — All 9 core engines bound
Observability       : PROMETHEUS + GRAFANA — Full dashboard required
Compliance          : GDPR + Legal Hold + Tenant Isolation — ENFORCED
Interpretation      : NONE PERMITTED
Mutation            : ADD-ONLY VIA VERSION BUMP
Architecture Authority: LOCKED

FRAUD DETECTION MODE: ACTIVE
TRUST INFRASTRUCTURE: SEALED
ANTIGRAVITY ENTERPRISE OPTIMIZATION: ENABLED

END LOCKED FRAUD DETECTION ARTIFACT
```

---

*FPDE v1.0 — Generated under ECOSKILLER Master Execution Prompt v12.0 + DOJO SAAS Locked Artifact Spec v1.0*
*Sealed for Antigravity Enterprise Trust Infrastructure*
*No interpretation. No deviation. Add-only evolution via version bump.*
