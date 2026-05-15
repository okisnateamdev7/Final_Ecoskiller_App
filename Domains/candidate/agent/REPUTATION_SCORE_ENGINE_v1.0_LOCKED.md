# 🏆 REPUTATION SCORE ENGINE — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
## ANTIGRAVITY | ECOSKILLER × DOJO SAAS — LOCKED & SEALED SPEC v1.0

```
Artifact Class     : Production Trust System Blueprint
Sub-System         : Reputation Score Engine (RSE)
Parent System      : ECOSKILLER v12.0 + DOJO SAAS v1.0
Mutation Policy    : Add-Only via Version Bump
Execution Mode     : Deterministic · Real-Time + Batch Hybrid
Stack Lock         : Enforced
Interpretation     : NONE PERMITTED
Authority          : Human Declaration Only
Status             : FINAL · LOCKED · GOVERNED
Version Tag        : RSE-v1.0
```

---

## ⚖️ GOVERNING SEAL BLOCK

```
ENTERPRISE TRUST MODE ENABLED
Reputation Score Engine         : ACTIVE
Score Dimensionality            : MULTI-AXIS (7 Dimensions)
Computation Mode                : DETERMINISTIC · WEIGHTED · VERSIONED
Score Override                  : HUMAN GOVERNANCE AUTHORITY ONLY
Auto-Decay                      : RULE-GOVERNED · SCHEDULED
Audit Log                       : IMMUTABLE
Appeal Path                     : MANDATORY FOR SCORE DEDUCTIONS > 100 pts
Cross-Module Signal Binding     : ENFORCED
Reputation Gating               : ACTIVE ACROSS ALL PLATFORM ACCESS LAYERS
Zero Score Floor                : 0 (No negative reputation)
```

> **Absence of any section below → STOP EXECUTION → REPORT RSE-INCOMPLETE → NO TRUST CLAIM PERMITTED**

---

# SECTION RSE-A — SYSTEM IDENTITY & PURPOSE

## What the Reputation Score Engine Does

The Reputation Score Engine (RSE) is the **unified trust quantification layer** of the Antigravity / Ecoskiller + Dojo SaaS platform. It produces a **single, multi-dimensional, auditable Reputation Score** for every entity in the system — users, mentors, institutions, recruiters, integrations, and content.

The RSE is not a vanity metric. It is an **operational gate** — reputation determines:

- Match eligibility and matchmaking band
- Belt and certification access
- Marketplace visibility and hiring priority
- Integration data trust weight
- Mentor authority scope
- Tournament entry eligibility
- Enterprise API access tier
- Recruiter search ranking priority

The RSE aggregates signals from all 9 core engines, the Fraud Pattern Detection Engine (FPDE), the Billing Engine, the Integration Engine, and all behavioral event streams into a **single versioned, append-only reputation ledger** per entity.

---

## Entity Types Covered

| Entity Type | Reputation Scope |
|-------------|-----------------|
| Student / User | Skill trust, match integrity, behavioral safety |
| Mentor | Calibration authority, scoring fairness, certification validity |
| Institution | Student outcome quality, placement track record, content governance |
| Recruiter | Hiring quality, offer follow-through, candidate fairness |
| Integration Connection | Data authenticity, sync reliability, AI normalization accuracy |
| Content (Scenario / Rubric) | Scenario fairness, completion rate, score distribution health |

---

# SECTION RSE-B — REPUTATION SCORE ARCHITECTURE

## Score Structure

Every entity receives a **Composite Reputation Score (CRS)** computed from **7 Reputation Dimensions**.

```
Composite Reputation Score (CRS)
│
├── D1 — Skill Integrity Score        (SIS)    Weight: 20%
├── D2 — Behavioral Safety Score      (BSS)    Weight: 15%
├── D3 — Scoring Fairness Score       (SFS)    Weight: 20%
├── D4 — Institutional Trust Score    (ITS)    Weight: 15%
├── D5 — Economic Reliability Score   (ERS)    Weight: 10%
├── D6 — Integration Authenticity Score (IAS)  Weight: 10%
└── D7 — Engagement Quality Score     (EQS)    Weight: 10%
```

### CRS Formula (Locked)

```
CRS = (SIS × 0.20) + (BSS × 0.15) + (SFS × 0.20) + (ITS × 0.15)
      + (ERS × 0.10) + (IAS × 0.10) + (EQS × 0.10)

CRS Range       : 0 – 1000
Precision       : Integer (floor after computation)
Recalculation   : Real-time on signal event + nightly full batch recompute
```

**Formula is immutable. Change requires version bump to RSE-v2.0 with governance board approval.**

---

## Score Bands (Operational Thresholds)

```
BAND              RANGE         STATUS          ACCESS LEVEL
───────────────────────────────────────────────────────────────────
ELITE             900 – 1000    Verified Elite  Full access + priority ranking
TRUSTED           750 – 899     Trusted         Full access
STANDARD          550 – 749     Standard        Standard access
MONITORED         350 – 549     Under Review    Restricted: no ranked matches
RESTRICTED        150 – 349     Restricted      Read-only: no match entry
BLOCKED           0   – 149     Blocked         Access revoked pending human review
```

---

# SECTION RSE-C — DIMENSION SPECIFICATIONS (ALL MANDATORY)

---

## D1 — Skill Integrity Score (SIS) | Weight: 20%

**Purpose:** Measures the authenticity and consistency of skill demonstration across all match and certification events.

### Input Signals

| Signal | Source Engine | Effect on SIS |
|--------|--------------|---------------|
| Peer score variance within match | Scoring Engine | Low variance → +SIS |
| Score consistency across scenarios (same skill) | Analytics Engine | High consistency → +SIS |
| Replay hash validation pass | Replay Engine | Pass → +SIS |
| Confidence score on match result | Scoring Engine | High confidence → +SIS |
| Belt earned on clean match history | Belt Engine | Clean history → +SIS |
| Match result disputed and upheld | Appeals System | Upheld dispute → -SIS |
| FPDE signal: MS-F01 to MS-F10 active | FPDE | Active signal → -SIS (scaled by FSS) |
| Calibration scenario pass rate | Scenario Engine | High pass rate → +SIS |
| Skill track completion rate | Analytics Engine | Completion → +SIS |
| Inter-rater reliability score (mentor) | Scoring Engine | High IRS → +SIS (mentor only) |

### SIS Computation

```
SIS_base = 500

SIS = SIS_base
    + (peer_variance_score × 80)           // 0–1 scale → 0–80 pts
    + (score_consistency_index × 100)      // 0–1 scale → 0–100 pts
    + (replay_integrity_rate × 60)         // 0–1 scale → 0–60 pts
    + (match_confidence_avg × 80)          // 0–1 scale → 0–80 pts
    - (active_fpde_signals × 50 × fss_weight)
    - (upheld_disputes × 30)
    - (failed_gate_attempts × 20)

SIS = clamp(SIS, 0, 1000)
```

---

## D2 — Behavioral Safety Score (BSS) | Weight: 15%

**Purpose:** Measures the entity's conduct quality across all live interactions — matches, mentorship sessions, tournaments, and community features.

### Input Signals

| Signal | Source | Effect on BSS |
|--------|--------|---------------|
| Harassment reports filed against entity | Safety Engine | Filed → -BSS |
| Harassment reports dismissed (after review) | Trust Team | Dismissed → +BSS (partial restore) |
| Abuse flag tags on match | Match Engine | Tag → -BSS |
| Mentor emergency intervention triggered | Mentor Control Engine | Triggered → -BSS |
| Cooldown enforcement events | Safety Engine | Enforcement → -BSS |
| Clean match history (30d, no flags) | Match Engine | Clean → +BSS |
| Positive mentor behavioral endorsement | Mentor Control Engine | Endorsement → +BSS |
| FPDE signal: BF-F01 to BF-F06 active | FPDE | Active → -BSS (scaled) |
| Forced match exit events | Safety Engine | Event → -BSS |
| Session recording consent given | Security Layer | Consent captured → +BSS |

### BSS Computation

```
BSS_base = 600

BSS = BSS_base
    + (clean_match_streak_30d × 5)          // max +150 at 30 clean matches
    + (mentor_endorsements × 20)            // max +100
    - (harassment_reports_active × 80)
    - (abuse_flags × 40)
    - (emergency_interventions × 60)
    - (cooldown_events × 30)
    - (forced_exits × 25)
    - (active_bf_fpde_signals × 50 × fss_weight)

BSS = clamp(BSS, 0, 1000)
```

---

## D3 — Scoring Fairness Score (SFS) | Weight: 20%

**Purpose:** Measures whether the entity (user as peer scorer, or mentor as evaluator) scores others fairly, consistently, and without bias.

### Input Signals

| Signal | Source | Effect on SFS |
|--------|--------|---------------|
| Peer score given vs. platform mean delta | Scoring Engine | Low delta → +SFS |
| Inter-rater reliability with peer panel | Scoring Engine | High IRL → +SFS |
| Reciprocal scoring pattern detected | FPDE: MS-F02 | Detected → -SFS |
| Score variance with calibration gold standard | Rater Calibration | Low variance → +SFS |
| Mentor override rate (30d) | Mentor Control Engine | High rate → -SFS |
| Mentor calibration pass | Rater Calibration Module | Pass → +SFS |
| Score override with valid audit reason | Audit Log | Valid reason → neutral |
| Score override without audit reason | Audit Log | No reason → -SFS |
| FPDE signal: MS-F06, MF-F01, MF-F02 | FPDE | Active → -SFS (scaled) |
| Appeal upheld (scorer was unfair) | Appeals System | Upheld → -SFS |

### SFS Computation

```
SFS_base = 550

SFS = SFS_base
    + (irl_score × 120)                    // inter-rater reliability 0–1 → 0–120 pts
    + (calibration_pass_streak × 30)       // consecutive passes
    - (peer_score_delta_excess × 60)       // deviation from platform mean
    - (reciprocal_pattern_count × 100)
    - (override_rate_excess × 80)          // override_rate > 20% triggers
    - (appeals_upheld_against × 50)
    - (active_scoring_fpde_signals × 60 × fss_weight)

SFS = clamp(SFS, 0, 1000)
```

---

## D4 — Institutional Trust Score (ITS) | Weight: 15%

**Purpose:** Measures the credibility and outcome quality of institutions, and for users, the verified quality of their institutional affiliation.

### Input Signals (Institution Entity)

| Signal | Source | Effect on ITS |
|--------|--------|---------------|
| Verified domain + accreditation record | Institutional Validator | Verified → +ITS |
| Student placement rate (outcome validation) | T13 Outcome Validator | High rate → +ITS |
| Employer feedback signals (positive) | T13 Outcome Validator | Positive → +ITS |
| Belt earned per enrolled student ratio | Belt Engine | High ratio → +ITS |
| Leaderboard rank manipulation detected | FPDE: MF-F04 | Detected → -ITS |
| Fake institution registration flag | FPDE: MF-F05 | Flagged → -ITS |
| Content governance compliance | T8 Content Governance | Compliant → +ITS |
| Student behavioral flags per institution | Safety Engine | High flags → -ITS |
| Mentor certification rate (within institution) | Certification Engine | High rate → +ITS |
| Dispute rate (match disputes per enrollment) | Dispute System | Low rate → +ITS |

### Input Signals (User Entity — Institutional Affiliation)

| Signal | Source | Effect on ITS for User |
|--------|--------|------------------------|
| Institution email verified | Auth Layer | Verified → +ITS |
| Institution's own ITS score | Institution RSE | High ITS → +ITS (inherited partial) |
| Institution verification revoked | Auth Layer | Revoked → -ITS |

### ITS Computation

```
ITS_base = 500

// For Institution:
ITS = ITS_base
    + (placement_rate × 150)               // 0–1 → 0–150 pts
    + (employer_feedback_score × 100)      // 0–1 → 0–100 pts
    + (accreditation_verified × 100)       // boolean → 0 or 100
    + (content_governance_compliance × 80)
    - (leaderboard_manipulation_flags × 120)
    - (fake_registration_flags × 200)
    - (student_behavioral_flags_rate × 60)

// For User (partial inheritance):
ITS_user = (institution.ITS × 0.30) + (email_verified × 100) + user_own_signals

ITS = clamp(ITS, 0, 1000)
```

---

## D5 — Economic Reliability Score (ERS) | Weight: 10%

**Purpose:** Measures the financial trustworthiness of the entity — payment history, refund behavior, billing legitimacy, and economic conduct.

### Input Signals

| Signal | Source | Effect on ERS |
|--------|--------|---------------|
| On-time subscription payment history | Billing Engine | On-time → +ERS |
| Refund request rate | Billing Engine | Low rate → +ERS |
| Refund abuse flag (≥3 in 60d) | FPDE: EC-F01 | Flagged → -ERS |
| Coupon stacking abuse | FPDE: EC-F04 | Flagged → -ERS |
| Tournament entry fee paid and completed | Tournament Engine | Completed → +ERS |
| Fake tournament loop detected | FPDE: EC-F03 | Detected → -ERS |
| Seat farming detected | FPDE: EC-F05 | Detected → -ERS |
| Invoice dispute rate | Billing Engine | Low rate → +ERS |
| GST/VAT phantom entity flag | FPDE: EC-F07 | Flagged → -ERS |
| Affiliate fraud detected | FPDE: EC-F08 | Detected → -ERS |
| Mentor billing with ghost account | FPDE: EC-F02 | Detected → -ERS |

### ERS Computation

```
ERS_base = 600

ERS = ERS_base
    + (payment_on_time_streak × 10)        // months consecutive → max +150
    + (low_refund_rate_bonus × 80)         // refund_rate < 2% → +80
    - (refund_abuse_flags × 150)
    - (coupon_abuse_flags × 100)
    - (economic_fpde_signals × 80 × fss_weight)
    - (invoice_dispute_rate_excess × 60)
    - (seat_farming_flags × 200)

ERS = clamp(ERS, 0, 1000)
```

---

## D6 — Integration Authenticity Score (IAS) | Weight: 10%

**Purpose:** Measures the trustworthiness of data flowing into Ecoskiller from external tool integrations — GitHub, Jira, Salesforce, HR systems, and all 100 connected tools.

### Input Signals

| Signal | Source | Effect on IAS |
|--------|--------|---------------|
| Tool connection verified + active sync | EIE | Verified → +IAS |
| Skill extraction data matches manual profile | AI Normalization | Match → +IAS |
| Synthetic data injection detected | FPDE: IF-F01 | Detected → -IAS |
| OAuth token reuse (multi-account) | FPDE: IF-F02 | Detected → -IAS |
| Skill amplification anomaly | FPDE: IF-F03 | Detected → -IAS |
| Migration data validated clean | Migration Engine | Clean → +IAS |
| Migration data poisoning detected | FPDE: IF-F04 | Detected → -IAS |
| Webhook replay attack detected | FPDE: IF-F05 | Detected → -IAS |
| API credential sharing detected | FPDE: IF-F06 | Detected → -IAS |
| Phantom tool connection | FPDE: IF-F07 | Detected → -IAS |
| UWDF manipulation detected | FPDE: IF-F08 | Detected → -IAS |
| Sync reliability rate (7d) | EIE | High rate → +IAS |

### IAS Computation

```
IAS_base = 500

IAS = IAS_base
    + (verified_active_integrations × 30)  // per tool, max +150
    + (skill_data_alignment_score × 100)   // 0–1 → 0–100 pts
    + (sync_reliability_7d × 80)           // 0–1 → 0–80 pts
    - (synthetic_data_flags × 150)
    - (oauth_reuse_flags × 120)
    - (migration_poison_flags × 180)
    - (integration_fpde_signals × 70 × fss_weight)

IAS = clamp(IAS, 0, 1000)
```

---

## D7 — Engagement Quality Score (EQS) | Weight: 10%

**Purpose:** Measures the depth, consistency, and quality of platform engagement — not raw activity volume, but meaningful contribution to the ecosystem.

### Input Signals

| Signal | Source | Effect on EQS |
|--------|--------|---------------|
| Skill track completion rate | Analytics Engine | High → +EQS |
| Drill engagement rate | Analytics Engine | High → +EQS |
| Daily streak maintained | Streak Tracker | Maintained → +EQS |
| Study room activity (peer learning) | Peer Learning Engine | Active → +EQS |
| Peer endorsements given (genuine) | Endorsement System | Given → +EQS |
| Peer endorsements received (verified) | Endorsement System | Received → +EQS |
| Post quality (campus feed) | Moderation Engine | High quality → +EQS |
| Content flagged for low quality | Moderation Engine | Flagged → -EQS |
| Challenge participation rate | Challenge Engine | High → +EQS |
| Referral quality (active referred users) | Growth Engine | High quality → +EQS |
| Match dropout rate | Match Engine | High dropout → -EQS |
| Social post flagged for violation | Moderation Engine | Flagged → -EQS |

### EQS Computation

```
EQS_base = 400

EQS = EQS_base
    + (skill_completion_rate × 100)        // 0–1 → 0–100 pts
    + (drill_engagement_rate × 60)
    + (streak_bonus × 5 per day, max 100)
    + (peer_endorsements_received × 10, max 80)
    + (peer_endorsements_given × 5, max 40)
    + (challenge_participation_rate × 60)
    + (quality_referrals × 15, max 90)
    - (match_dropout_rate × 80)
    - (content_violations × 50)
    - (low_quality_flags × 30)

EQS = clamp(EQS, 0, 1000)
```

---

# SECTION RSE-D — REPUTATION LEDGER (DATABASE SCHEMA)

All entities below are mandatory. Absence → STOP EXECUTION.

```sql
-- Master Reputation Score Record
ReputationScore (
  score_id          UUID PRIMARY KEY,
  entity_type       VARCHAR(30) NOT NULL,   -- user | mentor | institution | recruiter | integration | content
  entity_id         UUID NOT NULL,
  crs               INTEGER NOT NULL,        -- Composite Reputation Score 0–1000
  sis               INTEGER NOT NULL,        -- D1 Skill Integrity Score
  bss               INTEGER NOT NULL,        -- D2 Behavioral Safety Score
  sfs               INTEGER NOT NULL,        -- D3 Scoring Fairness Score
  its               INTEGER NOT NULL,        -- D4 Institutional Trust Score
  ers               INTEGER NOT NULL,        -- D5 Economic Reliability Score
  ias               INTEGER NOT NULL,        -- D6 Integration Authenticity Score
  eqs               INTEGER NOT NULL,        -- D7 Engagement Quality Score
  band              VARCHAR(15) NOT NULL,    -- ELITE | TRUSTED | STANDARD | MONITORED | RESTRICTED | BLOCKED
  computed_at       TIMESTAMPTZ NOT NULL,
  version_tag       VARCHAR(20) NOT NULL,    -- RSE version at time of computation
  UNIQUE(entity_type, entity_id)
)

-- Reputation Signal Event (Append-Only)
ReputationSignalEvent (
  event_id          UUID PRIMARY KEY,
  entity_type       VARCHAR(30) NOT NULL,
  entity_id         UUID NOT NULL,
  dimension_code    VARCHAR(5) NOT NULL,    -- D1–D7
  signal_type       VARCHAR(50) NOT NULL,
  signal_source     VARCHAR(50) NOT NULL,  -- engine that emitted signal
  delta_value       INTEGER NOT NULL,       -- positive = gain, negative = loss
  evidence_ref      JSONB,                  -- pointer to source record (match_id, case_id, etc.)
  detected_at       TIMESTAMPTZ NOT NULL,
  applied_at        TIMESTAMPTZ,
  applied           BOOLEAN DEFAULT FALSE
)

-- Reputation Score History (Time-Series)
ReputationScoreHistory (
  history_id        UUID PRIMARY KEY,
  entity_type       VARCHAR(30) NOT NULL,
  entity_id         UUID NOT NULL,
  crs_snapshot      INTEGER NOT NULL,
  band_snapshot     VARCHAR(15) NOT NULL,
  snapshot_date     DATE NOT NULL,
  trigger_event_id  UUID REFERENCES ReputationSignalEvent
)

-- Reputation Decay Schedule
ReputationDecayRule (
  rule_id           UUID PRIMARY KEY,
  dimension_code    VARCHAR(5) NOT NULL,
  inactivity_days   INTEGER NOT NULL,       -- days of inactivity before decay triggers
  decay_rate        DECIMAL(5,2) NOT NULL,  -- percentage decay per day after threshold
  floor_value       INTEGER NOT NULL,       -- minimum value for this dimension
  applies_to        VARCHAR(30) NOT NULL    -- entity_type or 'ALL'
)

-- Reputation Gate Record (Access Control Link)
ReputationGate (
  gate_id           UUID PRIMARY KEY,
  feature_code      VARCHAR(50) NOT NULL,   -- e.g. RANKED_MATCH, BELT_CHECK, TOURNAMENT_ENTRY
  entity_type       VARCHAR(30) NOT NULL,
  minimum_crs       INTEGER NOT NULL,
  minimum_band      VARCHAR(15) NOT NULL,
  dimension_overrides JSONB,               -- per-dimension minimums if needed
  active            BOOLEAN DEFAULT TRUE
)

-- Reputation Appeal Record
ReputationAppeal (
  appeal_id         UUID PRIMARY KEY,
  entity_id         UUID NOT NULL,
  entity_type       VARCHAR(30) NOT NULL,
  dimension_code    VARCHAR(5),             -- NULL = whole CRS appeal
  appealed_signal   UUID REFERENCES ReputationSignalEvent,
  evidence_text     TEXT NOT NULL,
  status            VARCHAR(20) NOT NULL,  -- OPEN | UNDER_REVIEW | UPHELD | DISMISSED
  opened_at         TIMESTAMPTZ NOT NULL,
  resolved_at       TIMESTAMPTZ,
  resolved_by       UUID,
  crs_delta_applied INTEGER                 -- adjustment if upheld
)

-- Dimension Weight Config (Versioned, Locked)
ReputationWeightConfig (
  config_id         UUID PRIMARY KEY,
  version_tag       VARCHAR(20) NOT NULL UNIQUE,
  d1_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.20,
  d2_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.15,
  d3_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.20,
  d4_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.15,
  d5_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.10,
  d6_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.10,
  d7_weight         DECIMAL(4,2) NOT NULL DEFAULT 0.10,
  active            BOOLEAN DEFAULT FALSE,  -- only one config active at a time
  activated_at      TIMESTAMPTZ,
  activated_by      UUID                    -- human authority required
)
```

**Row-Level Security: Enforced per tenant. Cross-tenant reputation data access: FORBIDDEN.**

---

# SECTION RSE-E — REPUTATION SCORE COMPUTATION ENGINE

## Computation Modes

```
MODE 1 — Real-Time Trigger Computation
  Trigger     : Signal event emitted by any source engine
  Scope       : Single entity, single dimension update
  Latency     : < 500ms
  Method      : Delta application to current score
  Output      : Updated ReputationScore + new ReputationScoreHistory entry

MODE 2 — Nightly Full Batch Recompute
  Trigger     : Scheduled cron (02:00 UTC daily)
  Scope       : All entities platform-wide
  Latency     : Batch (SLA: complete within 4h window)
  Method      : Full recompute from all signals in rolling 90-day window
  Output      : ReputationScore refresh + decay application

MODE 3 — On-Demand Governance Recompute
  Trigger     : Human governance authority request
  Scope       : Single entity or cohort
  Latency     : < 5 minutes
  Method      : Full recompute + manual signal override capability
  Output      : Updated score + governance audit entry
```

## Computation Pipeline (Locked Logic)

```
STEP 1 — Signal Ingestion
  Receive signal event from source engine
  Validate: entity_id exists, dimension_code valid, delta_value within bounds
  Write to ReputationSignalEvent (applied=false)

STEP 2 — Dimension Score Update
  Fetch current dimension score for entity
  Apply delta: new_score = current_score + delta_value
  Apply dimension floor/ceiling: clamp(new_score, 0, 1000)
  Write updated dimension score

STEP 3 — Decay Check
  For each dimension: check inactivity since last positive signal
  If inactivity_days > ReputationDecayRule.inactivity_days:
    Apply: dimension_score = dimension_score × (1 - decay_rate/100) per day over threshold
    Apply floor: dimension_score = max(dimension_score, floor_value)

STEP 4 — CRS Recompute
  Fetch active ReputationWeightConfig
  CRS = (SIS × d1_weight) + (BSS × d2_weight) + (SFS × d3_weight)
      + (ITS × d4_weight) + (ERS × d5_weight) + (IAS × d6_weight)
      + (EQS × d7_weight)
  CRS = floor(CRS)
  CRS = clamp(CRS, 0, 1000)

STEP 5 — Band Assignment
  IF CRS >= 900 → band = ELITE
  IF CRS >= 750 → band = TRUSTED
  IF CRS >= 550 → band = STANDARD
  IF CRS >= 350 → band = MONITORED
  IF CRS >= 150 → band = RESTRICTED
  ELSE           → band = BLOCKED

STEP 6 — Gate Evaluation
  For each ReputationGate where entity_type matches:
    IF entity.crs < gate.minimum_crs OR entity.band < gate.minimum_band:
      RESTRICT feature for entity
      EMIT gate_restriction_event

STEP 7 — Persist + Audit
  Update ReputationScore record
  Append ReputationScoreHistory snapshot
  Mark ReputationSignalEvent.applied = true
  If band changed → EMIT band_change_event to notification engine
```

---

# SECTION RSE-F — DECAY MODEL (LOCKED)

Reputation is not static. Inactive entities decay toward a floor value to prevent stale high-reputation accounts from maintaining platform trust without ongoing participation.

## Decay Rules (Default Config)

| Dimension | Inactivity Threshold | Decay Rate | Floor Value |
|-----------|---------------------|------------|-------------|
| D1 — Skill Integrity | 60 days | 0.5% / day | 300 |
| D2 — Behavioral Safety | 90 days | 0.2% / day | 400 |
| D3 — Scoring Fairness | 45 days | 0.5% / day | 300 |
| D4 — Institutional Trust | 120 days | 0.3% / day | 350 |
| D5 — Economic Reliability | No decay | — | 400 |
| D6 — Integration Authenticity | 30 days | 1.0% / day | 200 |
| D7 — Engagement Quality | 14 days | 1.5% / day | 100 |

## Decay Rules

```
Decay applies ONLY when:
  - No positive signal received for the dimension within inactivity_threshold
  - Entity status is not BLOCKED (blocked entities do not decay further)

Decay does NOT apply when:
  - Entity has active fraud case open (score frozen pending resolution)
  - Entity is on governance hold
  - Entity has submitted appeal pending review

Decay exemptions (entity types):
  - Institution: D4 decay rate halved (institutional trust changes slowly)
  - Mentor (certified): D3 decay begins after 90 days (not 45)
```

---

# SECTION RSE-G — REPUTATION GATE MAP (ACCESS CONTROL)

Every platform feature that involves trust is gated by minimum CRS and band.

## Gate Registry (Mandatory — None Optional)

| Feature Code | Minimum CRS | Minimum Band | Dimension Override |
|-------------|-------------|--------------|-------------------|
| RANKED_MATCH_ENTRY | 550 | STANDARD | SIS ≥ 500 |
| BELT_ELIGIBILITY_CHECK | 600 | STANDARD | SFS ≥ 450 |
| CERTIFICATION_ISSUE (Mentor) | 700 | TRUSTED | SFS ≥ 600, D3 calibration pass |
| TOURNAMENT_ENTRY | 650 | STANDARD | BSS ≥ 500 |
| CHAMPIONSHIP_ENTRY | 750 | TRUSTED | SIS ≥ 650, BSS ≥ 600 |
| MARKETPLACE_VISIBILITY | 600 | STANDARD | ITS ≥ 500 |
| RECRUITER_SEARCH_PRIORITY | 800 | TRUSTED | All dimensions ≥ 550 |
| ENTERPRISE_API_ACCESS | 850 | TRUSTED | ERS ≥ 700, IAS ≥ 650 |
| INTEGRATION_SKILL_TRUST | 650 | STANDARD | IAS ≥ 600 |
| MENTOR_AUTHORITY_GRANT | 750 | TRUSTED | SFS ≥ 700, BSS ≥ 600 |
| CONTENT_PUBLISH_RIGHTS | 700 | TRUSTED | EQS ≥ 500 |
| PEER_ENDORSEMENT_GIVE | 400 | MONITORED | BSS ≥ 350 |
| REPLAY_EVIDENCE_ACCESS | 600 | STANDARD | ITS ≥ 500 |
| APPEAL_SUBMISSION | 150 | BLOCKED | None |
| LEADERBOARD_VISIBILITY | 500 | MONITORED | None |

**Gate logic: Evaluated in real-time on every feature access request. Gate failure returns HTTP 403 with reputation_insufficient reason code.**

---

# SECTION RSE-H — API CONTRACTS (MANDATORY)

All routes required. Absence → STOP EXECUTION.

```
Public (Authenticated User):
GET   /reputation/{entity_type}/{entity_id}         → Current CRS + band + dimension breakdown
GET   /reputation/{entity_type}/{entity_id}/history → Score history (90-day time series)
GET   /reputation/{entity_type}/{entity_id}/gates   → Which gates entity passes/fails
POST  /reputation/appeals                            → Submit reputation appeal
GET   /reputation/appeals/{appeal_id}               → Appeal status

Internal (Trust Team + Governance):
GET   /reputation/signals                            → Signal event log (filterable)
GET   /reputation/signals/{signal_id}               → Signal detail + evidence
POST  /reputation/{entity_type}/{entity_id}/recompute → On-demand recompute (human auth)
PATCH /reputation/signals/{signal_id}/override       → Signal override (governance only, logged)
GET   /reputation/decay/status                       → Decay schedule status
POST  /reputation/gates                              → Create new gate rule (versioned)
GET   /reputation/gates                              → List all active gates
PATCH /reputation/appeals/{appeal_id}/resolve        → Resolve appeal (trust team)

Platform Integration (Machine-to-Machine):
POST  /reputation/signals/emit                       → Source engine emits signal event
GET   /reputation/gates/{feature_code}/check         → Feature access gate check for entity
GET   /reputation/leaderboard/{scope}                → Reputation-ranked leaderboard

Admin:
GET   /reputation/config/weights                     → Active weight configuration
POST  /reputation/config/weights                     → Propose new weight config (version bump required)
PATCH /reputation/config/weights/{config_id}/activate → Activate config (governance board only)
GET   /reputation/analytics/distribution             → CRS distribution across platform
GET   /reputation/analytics/decay-report             → Decay report by dimension
```

**All endpoints: JWT auth. Per-route RBAC enforced. Governance-level endpoints: dual-key authorization.**

---

# SECTION RSE-I — CROSS-ENGINE SIGNAL WIRING

The RSE is fully event-driven. No manual signal injection permitted outside governance-authorized overrides.

```
Match Engine
  → match_completed          → D1 signal (SIS): peer variance, confidence score
  → match_flagged            → D1, D3 signal: score freeze, SFS penalty
  → match_dropout_recorded   → D7 signal (EQS): dropout rate increase

Scoring Engine
  → score_submitted          → D3 signal (SFS): peer score delta vs. platform mean
  → override_executed        → D3 signal (SFS): override rate tracking
  → irl_computed             → D3 signal (SFS): inter-rater reliability update

Belt Engine
  → belt_check_initiated     → D1 gate: SIS check before belt eligibility
  → belt_awarded             → D1 signal (SIS): positive signal on clean belt award
  → belt_held                → D1 signal (SFS): hold applied, no positive update

Certification Engine
  → certification_requested  → D3 gate: SFS + calibration check (mentor)
  → certification_issued     → D3 signal (SFS): positive for mentor
  → certification_revoked    → D3, D4 signal: penalty for mentor + institution

Rater Calibration Module
  → calibration_pass         → D3 signal (SFS): +SFS for mentor
  → calibration_fail         → D3 signal (SFS): -SFS for mentor
  → authority_suspended      → D3 gate: mentor loses CERTIFICATION_ISSUE gate

Analytics Engine
  → skill_completion_recorded → D1, D7 signals: +SIS, +EQS
  → drill_engagement_recorded → D7 signal: +EQS
  → drop_off_recorded         → D7 signal: -EQS

Tournament Engine
  → tournament_entry_paid    → D5 signal (ERS): +ERS
  → tournament_completed     → D5 signal (ERS): +ERS
  → tournament_exit_early    → D5, D7 signals: -ERS, -EQS

Billing Engine
  → payment_on_time          → D5 signal (ERS): +ERS
  → refund_requested         → D5 signal: neutral (monitored)
  → refund_abuse_flag        → D5 signal (ERS): -ERS (heavy)

FPDE (Fraud Pattern Detection Engine)
  → signal_emitted(FSS≥0.40) → All relevant dimensions: negative delta, FSS-weighted
  → case_closed_dismissed    → Reverse signal: dimension restore (partial)
  → case_closed_confirmed    → Permanent signal: dimension floor locked

EIE (Integration Engine)
  → tool_sync_verified       → D6 signal (IAS): +IAS
  → data_anomaly_detected    → D6 signal (IAS): -IAS
  → integration_quarantined  → D6 signal (IAS): heavy -IAS

Safety Engine
  → harassment_report_filed  → D2 signal (BSS): -BSS (pending review)
  → report_dismissed         → D2 signal (BSS): partial restore
  → cooldown_enforced        → D2 signal (BSS): -BSS

Endorsement System
  → endorsement_given        → D7 signal (EQS): +EQS (giver)
  → endorsement_received     → D7 signal (EQS): +EQS (receiver)

Growth Engine
  → referral_activated       → D7 signal (EQS): +EQS (referrer)
  → referral_fraud_detected  → D5, D7 signals: -ERS, -EQS
```

---

# SECTION RSE-J — ENTITY-SPECIFIC REPUTATION PROFILES

## User / Student Reputation Profile

```
Primary Dimensions     : D1 (Skill Integrity), D2 (Behavioral Safety), D7 (Engagement Quality)
Secondary Dimensions   : D3 (Scoring Fairness), D4 (Institutional Trust)
Optional Dimensions    : D5 (Economic), D6 (Integration — if tools connected)

Key Gates:
  CRS ≥ 550 (STANDARD) → Ranked match entry
  CRS ≥ 650 (STANDARD) → Tournament entry
  CRS ≥ 750 (TRUSTED)  → Championship entry
  CRS ≥ 800 (TRUSTED)  → Recruiter search priority
```

## Mentor Reputation Profile

```
Primary Dimensions     : D3 (Scoring Fairness), D2 (Behavioral Safety), D1 (Skill Integrity)
Secondary Dimensions   : D4 (Institutional Trust), D7 (Engagement Quality)

Key Gates:
  CRS ≥ 700 + D3 ≥ 600  → Certification issue authority
  CRS ≥ 750             → Ranked match supervision
  D3 < 400              → Automatic authority suspension
  3× calibration fail   → Mentor authority revoked (human review required)
```

## Institution Reputation Profile

```
Primary Dimensions     : D4 (Institutional Trust), D3 (Scoring Fairness — aggregate)
Secondary Dimensions   : D5 (Economic), D2 (Behavioral Safety — aggregate)

Key Gates:
  CRS ≥ 600  → Marketplace listing
  CRS ≥ 750  → Featured institution status
  CRS < 350  → Institution flagged for governance review
  CRS < 150  → Institution suspended from platform
```

## Recruiter Reputation Profile

```
Primary Dimensions     : D4 (Institutional Trust), D5 (Economic Reliability)
Secondary Dimensions   : D2 (Behavioral Safety), D7 (Engagement Quality)

Key Gates:
  CRS ≥ 600  → Standard candidate access
  CRS ≥ 800  → Priority search access + AI hiring recommendations
  CRS ≥ 850  → Enterprise API access
  CRS < 400  → Candidate access restricted
```

## Integration Connection Reputation Profile

```
Primary Dimensions     : D6 (Integration Authenticity), D5 (Economic Reliability)
Secondary Dimensions   : D1 (skill signal trust weight)

Key Gates:
  IAS ≥ 600  → Skill signals trusted for profile
  IAS ≥ 700  → Skill signals trusted for belt evidence
  IAS < 400  → All synced skill data quarantined
  IAS < 200  → Integration suspended + data invalidated
```

---

# SECTION RSE-K — OBSERVABILITY & ALERTING LOCK

## Required Dashboards

```
CRS Distribution (platform-wide heatmap by band)
Dimension Score Averages (D1–D7 per entity type, daily trend)
Band Transition Tracker (upgrades vs. downgrades per day)
Gate Restriction Volume (feature access denials by feature code)
Reputation Signal Volume (by source engine, by dimension)
Decay Report (entities approaching floor values)
Appeal Queue Status (open / resolved / SLA breach)
Top Score Gainers (7d) + Top Score Losers (7d)
Mentor Reputation Panel (SFS trend, calibration history)
Institution Reputation Leaderboard
```

## Required Alerts

```
CRITICAL: Entity CRS drops to BLOCKED band                → immediate: trust team + governance
CRITICAL: Mentor D3 (SFS) drops below 400                → immediate: revoke certification authority
HIGH:     >50 entities cross from TRUSTED to MONITORED in 24h → possible systemic issue
HIGH:     Batch recompute fails or exceeds 4h SLA         → devops escalation
MEDIUM:   Appeal SLA breach (>72h SUSPEND, >24h BLOCK)    → trust team SLA alert
MEDIUM:   Decay floor reached for >100 entities           → curriculum / engagement team alert
LOW:      Single entity band upgrade to ELITE             → notify entity (achievement)
LOW:      Single entity band downgrade (any)              → notify entity with reason
```

## Prometheus Metrics

```
rse_crs_gauge{entity_type, band}
rse_dimension_score_gauge{entity_type, dimension}
rse_signal_events_total{source_engine, dimension, direction}
rse_band_transition_total{from_band, to_band, entity_type}
rse_gate_denial_total{feature_code, entity_type}
rse_decay_applied_total{dimension, entity_type}
rse_appeal_resolution_seconds{outcome}
rse_batch_recompute_duration_seconds
rse_score_delta_histogram{entity_type, dimension}
```

---

# SECTION RSE-L — REPUTATION LEADERBOARD SYSTEM

The RSE powers all leaderboards across the platform. Leaderboards are reputation-derived, not raw activity counts.

## Leaderboard Types (All Required)

```
PLATFORM GLOBAL        : Top 100 CRS across all entity types
STUDENT GLOBAL         : Top 100 students by CRS (cross-institution)
INSTITUTION GLOBAL     : Top 100 institutions by ITS + CRS composite
MENTOR GLOBAL          : Top 50 mentors by SFS + CRS
RECRUITER TRUST        : Top 25 recruiters by ERS + ITS
SKILL-SPECIFIC         : Top 50 users per skill by D1 (SIS)
INSTITUTION-SCOPED     : Top 100 students within institution
CHAMPIONSHIP ELIGIBLE  : Users with CRS ≥ 750 (TRUSTED band only)
```

## Leaderboard Rules

```
Eligibility:
  Minimum band: STANDARD (CRS ≥ 550) to appear on any leaderboard
  MONITORED, RESTRICTED, BLOCKED: excluded from all leaderboards

Update frequency: Nightly batch (aligned with full recompute)
Public visibility: STANDARD and above bands visible
Rank ties: Broken by longest continuous STANDARD+ band duration
Leaderboard manipulation: Detected by FPDE (MF-F04) → institution removed
```

---

# SECTION RSE-M — COMPLIANCE & DATA GOVERNANCE

```
Data Retention:
  ReputationScore (current)      → Retained while entity exists
  ReputationScoreHistory         → Retained 7 years
  ReputationSignalEvent          → Retained 7 years (immutable append-only)
  ReputationAppeal               → Retained permanently
  Dimension weight configs       → Retained permanently (all versions)

Privacy:
  CRS and band: user-visible (own entity)
  Dimension breakdown: user-visible (own entity)
  Signal evidence references: trust team only
  Cross-entity reputation queries: RBAC restricted
  GDPR erasure: score anonymized on entity deletion; signal events retained (de-identified)

Tenant Isolation:
  All reputation queries scoped to tenant_id
  Multi-tenant signal correlation: anonymized aggregates only
  Cross-tenant reputation inference: FORBIDDEN

Legal Hold:
  Entity under legal hold: score + all signals frozen, no decay, no deletion
  Export format: signed JSON archive with integrity hash
  Legal hold flag: stored in ReputationScore.legal_hold (boolean)
```

---

# SECTION RSE-N — VERSIONING & CHANGE GOVERNANCE

```
RSE Version Tag: RSE-v1.0

Allowed without version bump:
  Add new signal type to existing dimension
  Add new gate to ReputationGate registry
  Adjust decay floor ± 50 pts within existing dimension
  Add new leaderboard type
  Add new dashboard metric

Requires version bump (RSE-v1.x):
  Change dimension weight by any amount
  Add or remove dimension from CRS formula
  Change decay rate > ±0.3% per day
  Change band threshold boundaries
  Change gate minimum CRS / band
  Change computation mode behavior

Requires major version bump (RSE-v2.0):
  Remove a dimension
  Change CRS formula structure
  Change precision or range (0–1000)
  Change recompute mode architecture

Version bump requires:
  Human declaration
  Governance board review
  Backward compatibility impact assessment
  All active ReputationScore records flagged for recompute
  FraudActionLog-style entry with version_tag + rationale
```

---

# SECTION RSE-O — FINAL GOVERNANCE SEAL (MASTER PROMPT INSERT)

```
BEGIN LOCKED REPUTATION SCORE ENGINE ARTIFACT

System Role          : Reputation Score Engine (RSE)
Parent               : ECOSKILLER v12.0 + DOJO SAAS v1.0
Execution            : Deterministic · Real-Time + Nightly Batch + On-Demand
Score Architecture   : 7-Dimension Composite (D1–D7) — ALL ACTIVE — WEIGHTED LOCKED
CRS Range            : 0 – 1000 — IMMUTABLE SCALE
Bands                : 6 (ELITE → BLOCKED) — THRESHOLD LOCKED
Gate System          : ACTIVE — 15 Feature Gates — RBAC Enforced
Decay Model          : PER-DIMENSION — FLOOR-PROTECTED — SCHEDULE LOCKED
Entity Coverage      : User · Mentor · Institution · Recruiter · Integration · Content
Cross-Engine Wiring  : EVENT-DRIVEN — 12 Source Engines Bound
Leaderboard Engine   : REPUTATION-DERIVED — Band-Gated
Observability        : PROMETHEUS + GRAFANA — Full dashboard required
FPDE Integration     : BOUND — FSS signals feed all 7 dimensions
Appeals              : MANDATORY path for deductions > 100 pts
Governance Board     : ACTIVE — Weight config + Major version authority
Audit Log            : IMMUTABLE — 7-year signal retention
Compliance           : GDPR + Legal Hold + Tenant Isolation — ENFORCED
Interpretation       : NONE PERMITTED
Mutation             : ADD-ONLY VIA VERSION BUMP
Architecture Authority: LOCKED

REPUTATION SCORE ENGINE: ACTIVE
TRUST INFRASTRUCTURE: SEALED
ENTERPRISE OPTIMIZATION: ENABLED
ANTIGRAVITY DEPLOYMENT: READY

END LOCKED REPUTATION SCORE ENGINE ARTIFACT
```

---

*RSE v1.0 — Generated under ECOSKILLER Master Execution Prompt v12.0 + DOJO SAAS Locked Artifact Spec v1.0*
*Sealed for Antigravity Enterprise Trust Infrastructure*
*No interpretation. No deviation. Add-only evolution via version bump.*
*Companion system to FPDE v1.0 — both required for complete Trust Infrastructure.*
