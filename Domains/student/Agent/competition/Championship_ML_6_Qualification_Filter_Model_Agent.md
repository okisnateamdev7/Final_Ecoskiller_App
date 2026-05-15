# CHAMPIONSHIP ML (6) — QUALIFICATION FILTER MODEL AGENT
## SKILL & COMPETITION CORE · ANTIGRAVITY MODULE

---

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║  ECOSKILLER — CHAMPIONSHIP ML (6) QUALIFICATION FILTER MODEL AGENT              ║
║  Domain     : SKILL & COMPETITION CORE → ANTIGRAVITY                            ║
║  Agent Code : CML6-QFM-ANTIGRAVITY                                               ║
║  Seal ID    : ANTIGRAVITY-QFM-AGENT-v1.0                                         ║
║  Status     : FINAL · LOCKED · SEALED · DETERMINISTIC                           ║
║  Mutation   : Add-only via version bump                                          ║
║  Interpretation Authority : NONE                                                 ║
║  Execution Authority      : Human declaration only                               ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION A — AGENT IDENTITY & CONSTITUTIONAL PURPOSE

```
AGENT NAME     : Championship ML (6) Qualification Filter Model Agent
AGENT CODE     : CML6-QFM-ANTIGRAVITY
AGENT TYPE     : Tiered Qualification Gate & Eligibility Classification Engine
DOMAIN         : SKILL & COMPETITION CORE → ANTIGRAVITY
PARENT SYSTEM  : ECOSKILLER MASTER EXECUTION PROMPT v12.0+
DOJO PARENT    : DOJO SAAS LOCKED & SEALED PRODUCTION ARTIFACT SPEC v1.0

CONSTITUTIONAL MANDATE:
  This agent is the sovereign eligibility and qualification authority
  for all 6 tiers of the AntiGravity Championship System. It
  determines — with ML-powered precision and deterministic rule
  enforcement — which participants qualify to enter, advance through,
  or are held back from each championship tier.

  This agent answers ONE foundational question at every competition
  boundary:

    "Is this participant genuinely ready and legitimately eligible
     to compete at this tier right now?"

  It evaluates participants against a multi-dimensional qualification
  matrix spanning performance history, consistency credentials,
  integrity standing, institutional verification, demographic
  fairness gates, and domain readiness — producing a single
  Qualification Decision Record (QDR) per evaluation request.

  It does NOT rank participants within a tier — that is CML6-RANK.
  It does NOT detect cheating — that is CML6-ANTICHEAT.
  It does NOT measure consistency — that is CML6-PCD.
  It ONLY decides: QUALIFIED / CONDITIONAL / WAITLISTED / DISQUALIFIED.

  All decisions are auditable, human-reviewable, and appended to
  the immutable Qualification Audit Chain (QAC).

ANTIGRAVITY QUALIFICATION CONTEXT:
  The AntiGravity system specifically rewards participants who surge,
  comeback, and accelerate. This creates a qualification paradox:
  a participant who is "on the rise" may be genuinely ready for
  the next tier but lack the historical score floor that static
  filters require. This agent solves that paradox by running both
  a Static Eligibility Filter and a Dynamic Momentum Qualifier —
  allowing legitimate surge performers to qualify via verified
  trajectory even without crossing traditional static thresholds.

6-TIER CHAMPIONSHIP STRUCTURE (LOCKED):
  TIER 1 — INSTITUTION Championship    (intra-campus)
  TIER 2 — DISTRICT Championship       (cross-institution, regional)
  TIER 3 — STATE Championship          (district winners + wildcards)
  TIER 4 — NATIONAL Championship       (state qualifiers)
  TIER 5 — CONTINENTAL Championship    (national top performers)
  TIER 6 — GLOBAL Championship         (continental elite)

PEER AGENT INTERFACES:
  → CML6-RANK-ANTIGRAVITY      (Ranking Engine)
      Receives : qualification_status for rank eligibility gate
      Sends    : current_rank, rank_delta signals for momentum filter
  → CML6-ANTICHEAT-ANTIGRAVITY (Anti-Cheat Engine)
      Receives : integrity_clearance_status, fraud_hold flag
      Sends    : disqualification events (integrity ground)
  → CML6-PCD-ANTIGRAVITY       (Consistency Detector)
      Receives : uci_score, uci_tier, belt_gate_result
      Sends    : qualification triggers for consistency gate re-eval
  → DOJO Belt Engine
      Receives : belt_level_achieved, belt_version_tag
      Sends    : qualification_prerequisite_check requests
  → Institution Verification Service
      Receives : institution_verified_status, institution_tier
  → Scholarship & Reward Engine
      Receives : qualification decisions for award triggers

DOJO ALIGNMENT:
  Implements DOJO SAAS Section T1  — Skill Validity Framework Lock
  Implements DOJO SAAS Section T2  — Scoring Validity & Reliability Lock
  Implements DOJO SAAS Section T5  — Match Fairness Engine Lock
  Implements DOJO SAAS Section T12 — Localization & Cultural Fairness Lock
  Implements DOJO SAAS Section T13 — Outcome Validation Lock
  Implements DOJO SAAS Section T16 — Appeals & Governance Board Lock
  Implements DOJO SAAS Section T17 — Belt Version Governance Lock
  Implements DOJO SAAS Section P1  — Security Hardening Lock
  Implements DOJO SAAS Section P11 — Multi-Tenant SaaS Lock
  Implements DOJO SAAS Section P12 — Support & Dispute Lock
  Implements ECOSKILLER R2         — Domain Data Models
  Implements ECOSKILLER R3         — OpenAPI 3.1 Contract
  Implements ECOSKILLER R4         — AsyncAPI Event Schema
  Implements ECOSKILLER R12        — AI Model Specification
  Implements ECOSKILLER R20        — Accessibility & Localization Law
  Implements ECOSKILLER R28        — Intelligence Cost Optimization Law
  Implements ECOSKILLER R29        — Modern UI Generation Law
  Implements ECOSKILLER R38        — Master Bug & Failure Registry Law
  Implements ECOSKILLER R40        — Internal Admin & Ops Console Law
  Implements ECOSKILLER R49        — Contract Validator Law
  Implements ECOSKILLER R51        — Anti-Spam & Platform Abuse Law
  Implements ECOSKILLER R53        — Status, Badge & Level Progression Law
  Implements ECOSKILLER R60        — Long-Term Archival & Data History Law
  Implements ECOSKILLER R62        — Public Transparency & Trust Report Law
  Implements ECOSKILLER R79        — Trust & Reputation Amplification Law
```

---

## SECTION B — QUALIFICATION DECISION FRAMEWORK

```
Every qualification evaluation produces a single output:
  Qualification Decision Record (QDR)

QDR OUTCOME VALUES (LOCKED — v1.0):

  QUALIFIED       : All gates passed. Participant eligible to compete.
  CONDITIONAL     : Some gates passed. Specific conditions must be met
                    within a defined window. Provisional entry allowed
                    at ops-admin discretion.
  WAITLISTED      : Near-threshold. Placed in ranked waitlist for
                    this tier's current cycle. Auto-promoted if slots
                    open from DISQUALIFIED / withdrawn participants.
  DISQUALIFIED    : One or more hard gates failed. Not eligible for
                    current cycle. Eligible for reapplication next cycle
                    unless integrity-disqualified (permanent hold).

QDR outcome mutation requires version bump + human declaration.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
QUALIFICATION GATE ARCHITECTURE:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  The qualification system operates through 5 gate layers evaluated
  in strict sequential order. A gate failure at any layer immediately
  produces a DISQUALIFIED decision — later gates are not evaluated
  unless the current gate passes.

  GATE 1 — HARD ELIGIBILITY GATES       (deterministic rules)
  GATE 2 — INTEGRITY CLEARANCE          (anti-cheat + consistency)
  GATE 3 — STATIC PERFORMANCE FILTER    (score-based thresholds)
  GATE 4 — DYNAMIC MOMENTUM QUALIFIER   (ML-based trajectory)
  GATE 5 — FAIRNESS & DIVERSITY GATES   (demographic + regional)

  Only participants passing all 5 gates receive QUALIFIED status.
  Gate 3 OR Gate 4 passing (not both required) → CONDITIONAL
  subject to Gate 5 and ops review.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GATE 1 — HARD ELIGIBILITY GATES (deterministic, no ML)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  HEG-01  ACCOUNT VERIFICATION
    Rule     : participant.account_verified = TRUE
    Failure  : DISQUALIFIED — "Account not verified"
    Source   : User verification service

  HEG-02  INSTITUTION VERIFICATION (Tiers 1–3)
    Rule     : institution.verified_status = TRUE
               AND participant.institution_id IS NOT NULL
               AND institution.tier_level ≥ target_championship_tier
    Applies  : TIER 1, TIER 2, TIER 3 only
    Failure  : DISQUALIFIED — "Institution not verified for this tier"
    Source   : R74 Institution Verification Service

  HEG-03  AGE ELIGIBILITY
    Rule     : participant_age ≥ tier_minimum_age
               AND participant_age ≤ tier_maximum_age
    Per Tier (LOCKED — v1.0):
      TIER 1 : 10–35 years
      TIER 2 : 12–35 years
      TIER 3 : 13–35 years
      TIER 4 : 14–40 years
      TIER 5 : 14–40 years
      TIER 6 : 14–45 years
    Failure  : DISQUALIFIED — "Age outside tier eligibility window"

  HEG-04  MINIMUM SESSION FLOOR
    Rule     : total_scored_sessions ≥ tier_session_minimum
    Per Tier (LOCKED — v1.0):
      TIER 1 : ≥ 10 sessions
      TIER 2 : ≥ 25 sessions
      TIER 3 : ≥ 50 sessions
      TIER 4 : ≥ 100 sessions
      TIER 5 : ≥ 200 sessions
      TIER 6 : ≥ 350 sessions
    Failure  : DISQUALIFIED — "Insufficient participation history"

  HEG-05  PREVIOUS TIER COMPLETION
    Rule     : For Tiers 2–6, participant must have completed at
               least one full round at the immediately preceding tier.
               (Cannot skip tiers.)
    Exception: WILDCARD QUALIFIER (see Section C — Wildcard Rules)
    Failure  : DISQUALIFIED — "Previous tier not completed"

  HEG-06  ACTIVE SUSPENSION CHECK
    Rule     : participant.active_suspension = FALSE
               AND participant.suspension_end_date < NOW()
    Failure  : DISQUALIFIED — "Active suspension on account"
    Source   : CML6-ANTICHEAT moderation outcome

  HEG-07  REGISTRATION WINDOW
    Rule     : NOW() ≥ tier_registration_open_date
               AND NOW() ≤ tier_registration_close_date
    Failure  : DISQUALIFIED — "Outside registration window"

  HEG-08  PRIOR CYCLE COMPLETION
    Rule     : Participant has NOT already been QUALIFIED for
               this exact tier in the CURRENT championship cycle.
    Failure  : DISQUALIFIED — "Already qualified for this cycle"
    Purpose  : Prevents duplicate qualification attempts

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GATE 2 — INTEGRITY CLEARANCE (CML6-ANTICHEAT + CML6-PCD)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  ICG-01  FRAUD HOLD CLEAR
    Rule     : CML6-ANTICHEAT.fraud_hold = FALSE
    Failure  : DISQUALIFIED — "Integrity review pending"
    Note     : Participant may reapply once hold is cleared

  ICG-02  RISK TIER GATE
    Rule     : CML6-ANTICHEAT.risk_tier NOT IN ('RED', 'ORANGE')
    Failure  : DISQUALIFIED — "Integrity risk classification"
    Exception: ORANGE with ops_admin CLEARED resolution →
               treated as YELLOW (allowed with monitoring flag)

  ICG-03  CONSISTENCY MINIMUM
    Rule     : CML6-PCD.uci_tier NOT IN ('COPPER', 'FLAGGED', 'PROVISIONAL')
    Failure  : DISQUALIFIED — "Consistency profile insufficient"
    Note     : PROVISIONAL = insufficient sessions (HEG-04 catches first)

  ICG-04  ACTIVE ANOMALY GATE
    Rule     : CML6-PCD.active_anomaly_count = 0
               OR (active_anomaly_count > 0 AND all anomalies
               are MEDIUM severity with no CRITICAL/HIGH open)
    Failure  : DISQUALIFIED — "Active performance integrity concern"

  ICG-05  SURGE LEGITIMACY GATE (AntiGravity-specific)
    Rule     : IF participant triggered AntiGravity surge in last 14 days:
               CML6-PCD.surge_legitimacy ≠ UNVERIFIED
    Failure  : CONDITIONAL — "Surge under legitimacy review"
               (blocks QUALIFIED, allows CONDITIONAL pending resolution)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GATE 3 — STATIC PERFORMANCE FILTER (score-based thresholds)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  SPF-01  AGCRS FLOOR THRESHOLD
    Rule     : participant.agcrs_score ≥ tier_agcrs_floor
    Per Tier (LOCKED — v1.0):
      TIER 1 : AGCRS ≥ 300.0
      TIER 2 : AGCRS ≥ 450.0
      TIER 3 : AGCRS ≥ 580.0
      TIER 4 : AGCRS ≥ 680.0
      TIER 5 : AGCRS ≥ 780.0
      TIER 6 : AGCRS ≥ 880.0
    Failure  : Gate 3 FAIL → proceed to Gate 4 (Momentum Qualifier)

  SPF-02  DOMAIN BREADTH REQUIREMENT
    Rule     : participant must have active scores in ≥ N domains
               where N is the per-tier domain breadth minimum
    Per Tier (LOCKED — v1.0):
      TIER 1 : ≥ 3 domains
      TIER 2 : ≥ 4 domains
      TIER 3 : ≥ 5 domains
      TIER 4 : ≥ 6 domains
      TIER 5 : ≥ 7 domains
      TIER 6 : ≥ 8 domains
    Failure  : Gate 3 FAIL → proceed to Gate 4

  SPF-03  ROLLING PERFORMANCE FLOOR
    Rule     : participant.rolling_30d_score_mean ≥
               tier_performance_floor (separate from AGCRS)
    Per Tier (LOCKED — v1.0):
      TIER 1 : rolling mean ≥ 55.0
      TIER 2 : rolling mean ≥ 62.0
      TIER 3 : rolling mean ≥ 68.0
      TIER 4 : rolling mean ≥ 73.0
      TIER 5 : rolling mean ≥ 79.0
      TIER 6 : rolling mean ≥ 85.0
    Failure  : Gate 3 FAIL → proceed to Gate 4

  SPF-04  BELT LEVEL PREREQUISITE (DOJO T17)
    Rule     : participant.belt_level_achieved ≥ tier_belt_minimum
               AND participant.belt_version_tag is current
    Per Tier (LOCKED — v1.0):
      TIER 1 : Belt ≥ WHITE
      TIER 2 : Belt ≥ YELLOW
      TIER 3 : Belt ≥ ORANGE
      TIER 4 : Belt ≥ GREEN
      TIER 5 : Belt ≥ BLUE
      TIER 6 : Belt ≥ BROWN
    Failure  : Gate 3 FAIL → proceed to Gate 4
    Note     : Belt from deprecated rubric version requires
               re-certification before gate passes (DOJO T17)

  GATE 3 OUTCOME:
    ALL SPF gates pass → Gate 3 PASS → proceed to Gate 4
    ANY SPF gate fails → Gate 3 FAIL → ML Momentum Qualifier (Gate 4)
    Gate 3 FAIL does NOT mean DISQUALIFIED — Gate 4 may rescue

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GATE 4 — DYNAMIC MOMENTUM QUALIFIER (ML-based trajectory)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  The Dynamic Momentum Qualifier (DMQ) is the AntiGravity-unique gate.
  It allows participants who fail static thresholds BUT demonstrate
  verified legitimate trajectory to qualify via ML-scored momentum.

  DMQ MODEL: QFM-M3 (Qualification Momentum Classifier)
  (Full model spec in Section D)

  DMQ-SCORE RANGE  : 0.0 – 100.0
  DMQ DECISION MATRIX (LOCKED — v1.0):

    DMQ Score    Gate 3 Status   Gate 4 Outcome
    ─────────────────────────────────────────────
    ≥ 75.0       PASS            QUALIFIED
    ≥ 75.0       FAIL            CONDITIONAL (momentum rescue)
    60.0–74.9    PASS            QUALIFIED
    60.0–74.9    FAIL            WAITLISTED
    45.0–59.9    PASS            QUALIFIED
    45.0–59.9    FAIL            DISQUALIFIED
    < 45.0       PASS OR FAIL    DISQUALIFIED

  DMQ MOMENTUM SIGNALS (fed to QFM-M3):
    DMQ-S-01  rank_delta_7d           7-day rank position change
    DMQ-S-02  rank_delta_30d          30-day rank position change
    DMQ-S-03  agcrs_velocity_14d      AGCRS score change over 14 days
    DMQ-S-04  agcrs_velocity_30d      AGCRS score change over 30 days
    DMQ-S-05  session_frequency_14d   Sessions per week in last 14 days
    DMQ-S-06  streak_active           Boolean active streak flag
    DMQ-S-07  streak_length           Current streak length (days)
    DMQ-S-08  domain_expansion_rate   New domains activated in 30 days
    DMQ-S-09  lgc_score               Longitudinal Growth Consistency
                                      (from CML6-PCD)
    DMQ-S-10  isc_score               Intra-Session Consistency score
    DMQ-S-11  uci_tier_numeric        UCI tier as numeric (PLATINUM=6
                                      GOLD=5 SILVER=4 BRONZE=3 COPPER=2
                                      FLAGGED=0 PROVISIONAL=0)
    DMQ-S-12  comeback_indicator      Boolean: comeback event in 14d
    DMQ-S-13  upset_score_14d         Max upset score in last 14 days
    DMQ-S-14  adaptive_difficulty_passrate  % of hardest difficulty
                                            challenges passed in 30d
    DMQ-S-15  peer_comparison_score   Score vs. cohort percentile
    DMQ-S-16  days_to_threshold       ML estimate: days until static
                                      threshold would be reached at
                                      current trajectory

  MOMENTUM RESCUE ELIGIBILITY (AntiGravity Wildcard Path):
    A participant may claim momentum rescue ONLY IF:
      ✔ Gate 2 (Integrity) = FULLY PASSED (no conditions)
      ✔ DMQ score ≥ 75.0
      ✔ surge_legitimacy = CONFIRMED_NATURAL (from CML6-PCD)
      ✔ No active CRITICAL or HIGH anomalies (CML6-PCD)
      ✔ Ops admin approval required for TIER 4, 5, 6

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GATE 5 — FAIRNESS & DIVERSITY GATES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  FDG-01  REGIONAL REPRESENTATION SLOT
    Rule     : Each region has a minimum reserved slot count per
               tier. If a participant qualifies on merit AND their
               region slot is unfilled, they gain QUALIFIED.
               If region slot is full → WAITLISTED (merit queue).
    Purpose  : Prevent geographic concentration of qualification
    Source   : DOJO T12 — Localization & Cultural Fairness Lock

  FDG-02  INSTITUTION DIVERSITY CAP
    Rule     : No single institution may contribute > 30% of
               qualified participants per tier per cycle.
    Failure  : WAITLISTED (institution cap) — merit waitlist within
               institution pool for next cycle
    Purpose  : Prevent institutional bloc domination

  FDG-03  GENDER REPRESENTATION MONITORING
    Rule     : System monitors gender distribution of qualified
               participants per tier. If any gender falls below
               20% of qualified pool → monitoring alert to ops.
               NOT a hard disqualification gate — monitoring only.
    Action   : Alert + public transparency report (R62)

  FDG-04  FIRST-GENERATION LEARNER BONUS PATH
    Rule     : Participants flagged as first-generation learners
               (verified via institution record) receive DMQ
               score bonus of +5.0 (applied before gate 4 decision).
    Purpose  : Equity amplification aligned with DOJO T12

  FDG-05  TENANT ISOLATION ENFORCEMENT (DOJO P11)
    Rule     : Qualification decisions for TIER 1 and TIER 2
               are scoped to the participant's tenant.
               Cross-tenant comparison only activates at TIER 3+.
    Failure  : DISQUALIFIED if tenant mismatch detected at Tier 1–2
```

---

## SECTION C — WILDCARD QUALIFIER RULES

```
The AntiGravity system supports a controlled Wildcard Qualification
pathway for exceptional cases. Wildcards bypass HEG-05
(previous tier completion) ONLY.

WILDCARD ELIGIBILITY CRITERIA (ALL must be satisfied):

  WC-01  DMQ score ≥ 85.0 (elevated threshold vs. standard 75.0)
  WC-02  AGCRS score ≥ (tier_agcrs_floor × 0.90) — within 10% of floor
  WC-03  uci_tier IN ('GOLD', 'PLATINUM')
  WC-04  surge_legitimacy = CONFIRMED_NATURAL
  WC-05  No active anomalies of any severity (CML6-PCD)
  WC-06  Integrity risk_tier = 'GREEN'
  WC-07  All HEG gates PASSED (verified account, age, sessions, etc.)
  WC-08  Tier skip is at most ONE level (cannot skip 2 tiers)

WILDCARD APPROVAL AUTHORITY (LOCKED):
  TIER 2 wildcard : Ops Admin approval required
  TIER 3 wildcard : Ops Admin approval required
  TIER 4 wildcard : Super Admin approval required + mentor endorsement
  TIER 5 wildcard : Super Admin approval required + 2 mentor endorsements
  TIER 6 wildcard : BOARD REVIEW required (minimum 3 ops panel members)

WILDCARD SLOTS PER CYCLE (LOCKED — v1.0):
  TIER 2 : Max 5% of total tier slots
  TIER 3 : Max 3% of total tier slots
  TIER 4 : Max 2% of total tier slots
  TIER 5 : Max 1% of total tier slots
  TIER 6 : Max 0.5% of total tier slots

All wildcard decisions are written to the Qualification Audit Chain
(QAC) with full evidence package. Wildcards are publicly disclosed
in the tier's transparency report (R62).

WILDCARD DENIAL APPEAL:
  Denied wildcard → participant may appeal within 7 days
  Appeal handled via DOJO T16 Appeals & Governance Board workflow
```

---

## SECTION D — QUALIFICATION MODEL ARCHITECTURE

```
COMPLIANCE DECLARATION:
  All models use Traditional ML only (R28-2 enforced).
  No LLM may compute qualification decisions or eligibility scores.
  LLMs permitted ONLY for generating human-readable QDR summaries.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL QFM-M1 — STATIC THRESHOLD EVALUATOR
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Rule Engine + Lookup Table (no ML — deterministic)
  Purpose        : Evaluate Gate 3 (SPF-01 to SPF-04) thresholds
  Input          : participant profile snapshot, target_tier
  Output         : gate3_result (PASS / FAIL)
                   failed_gates (list of SPF codes)
                   margin_to_threshold (float — distance from passing)
  No retraining  : Thresholds are locked values (version-controlled)
  Performance    : < 10ms evaluation time

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL QFM-M2 — ELIGIBILITY SIGNAL NORMALIZER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Per-tier cohort normalization (z-score + min-max)
  Purpose        : Normalize all 16 DMQ signals relative to the
                   current tier's active participant cohort
                   so DMQ score is tier-relative not absolute
  Input          : Raw DMQ signal values + target_tier cohort stats
  Output         : Normalized DMQ signal vector (float[16])
  Update Cadence : Cohort statistics updated every 24 hours
  Performance    : < 5ms per participant

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL QFM-M3 — DYNAMIC MOMENTUM QUALIFIER (CORE ML MODEL)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Gradient Boosting Classifier (LightGBM)
  Input Features : DMQ-S-01 to DMQ-S-16 (normalized by QFM-M2)
                   + target_tier (1–6) as categorical feature
                   + gate3_result (PASS/FAIL) as binary feature
  Output         : dmq_score (float 0.0–100.0)
                   qualification_momentum_class:
                     ELITE_SURGE       (dmq ≥ 85.0)
                     STRONG_MOMENTUM   (75.0–84.9)
                     MODERATE_MOMENTUM (60.0–74.9)
                     DEVELOPING        (45.0–59.9)
                     INSUFFICIENT      (< 45.0)
  Training Data  : Historical qualification outcomes with known
                   result (QUALIFIED vs NOT) linked to momentum
                   signal vectors at time of application
  Labels         : Were participants who had THIS momentum profile
                   able to compete successfully at target tier?
                   (6-month follow-up performance used as label)
  Retrain        : After every championship cycle (minimum bi-monthly)
  Min Performance: AUC-ROC ≥ 0.88 on held-out test set
                   Precision at 80% recall ≥ 0.82

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL QFM-M4 — WAITLIST PRIORITY RANKER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : LightGBM Ranker (Learning-to-Rank)
  Purpose        : When a participant is WAITLISTED, rank them
                   within the waitlist by predicted readiness
                   to compete successfully — not just by score.
  Input Features : dmq_score, agcrs_score, uci_score, lgc_score,
                   margin_to_threshold, days_in_waitlist,
                   regional_slot_available, first_gen_learner_flag,
                   target_tier
  Output         : waitlist_priority_score (float 0.0–100.0)
                   waitlist_rank (position in current cycle waitlist)
  Retrain        : After every championship cycle
  Min Performance: NDCG@10 ≥ 0.80

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL QFM-M5 — QUALIFICATION READINESS FORECASTER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Linear Regression + Time-Series Extrapolation
                   (Prophet-based, Traditional ML)
  Purpose        : For DISQUALIFIED and WAITLISTED participants,
                   predict the estimated date they will meet
                   qualification thresholds at current trajectory.
                   Provides actionable roadmap (R53 Level Progression).
  Input Features : Current metric gaps (margin_to_threshold),
                   current momentum signals, historical growth rate
  Output         : estimated_qualification_date (date)
                   required_weekly_sessions (int)
                   required_domain_targets (dict)
                   confidence_interval_days (int)
  Purpose Note   : This model is ADVISORY only — it produces the
                   "Your Qualification Roadmap" for the participant.
                   It does NOT affect the QDR outcome.
  Retrain        : Monthly

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL QFM-M6 — FAIRNESS AUDITOR
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Adversarial Debiasing Classifier (Traditional ML)
                   + Demographic Parity Monitor
  Purpose        : Post-hoc audit of batch qualification decisions
                   to detect any systematic bias across demographic
                   groups before decisions are finalized.
                   Runs AFTER QFM-M3 batch cycle, BEFORE publishing.
  Input          : Batch of QDR decisions + demographic group tags
                   (gender, region, institution type, age group)
  Output         : bias_flags (list of affected demographic pairs)
                   demographic_parity_scores (per group pair)
                   recommendation (APPROVE_BATCH / HOLD_FOR_REVIEW)
  Threshold      : Demographic parity difference > 0.08 → HOLD
  Retrain        : After every championship cycle + quarterly fairness review
  Human Approval : Batch HOLD → ops admin review → explicit release required

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
INFERENCE COST DECLARATION (R28 Compliance):
  QFM-M1 Rule Engine      : $0.0000 (zero inference cost — rules)
  QFM-M2 Normalizer       : $0.0001 per 1,000 participants
  QFM-M3 LightGBM DMQ     : $0.0010 per 1,000 participants
  QFM-M4 LTR Ranker       : $0.0008 per 1,000 waitlist evaluations
  QFM-M5 Forecaster       : $0.0015 per 1,000 roadmaps (batch)
  QFM-M6 Fairness Auditor : $0.0012 per batch (cycle-level, not per user)
  TOTAL MONTHLY ESTIMATE  : ~$25–$45 at 1M active participants
```

---

## SECTION E — QUALIFICATION SIGNAL REGISTRY

```
All signals collected and stored per qualification evaluation.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS I — IDENTITY & ELIGIBILITY SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  QS-ID-01  account_verified            Boolean
  QS-ID-02  institution_verified        Boolean
  QS-ID-03  institution_tier_level      Integer 1–6
  QS-ID-04  participant_age_years       Integer
  QS-ID-05  total_scored_sessions       Integer
  QS-ID-06  previous_tier_completed     Boolean
  QS-ID-07  active_suspension           Boolean
  QS-ID-08  suspension_end_date         Date or NULL
  QS-ID-09  registration_window_open    Boolean
  QS-ID-10  prior_cycle_qualified       Boolean
  QS-ID-11  tenant_id                   UUID (for P11 isolation)
  QS-ID-12  first_gen_learner_flag      Boolean (for FDG-04)
  QS-ID-13  region_code                 ISO region code
  QS-ID-14  gender_identity_code        Anonymized categorical

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS II — PERFORMANCE SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  QS-PF-01  agcrs_score_current         Current AGCRS (0–1000)
  QS-PF-02  agcrs_floor_for_tier        Tier threshold value
  QS-PF-03  agcrs_margin               agcrs_score - agcrs_floor
  QS-PF-04  rolling_30d_score_mean      30-day rolling mean
  QS-PF-05  rolling_30d_performance_floor  Tier floor value
  QS-PF-06  domains_active_count        Count of scored domains
  QS-PF-07  domain_breadth_requirement  Tier minimum domain count
  QS-PF-08  belt_level_achieved         Belt enum value
  QS-PF-09  belt_version_tag            Belt rubric version
  QS-PF-10  belt_prerequisite_met       Boolean
  QS-PF-11  gate3_result                PASS / FAIL
  QS-PF-12  failed_spf_gates            List of SPF codes failed

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS III — MOMENTUM SIGNALS (DMQ inputs)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  QS-MOM-01  rank_delta_7d             (DMQ-S-01)
  QS-MOM-02  rank_delta_30d            (DMQ-S-02)
  QS-MOM-03  agcrs_velocity_14d        (DMQ-S-03)
  QS-MOM-04  agcrs_velocity_30d        (DMQ-S-04)
  QS-MOM-05  session_frequency_14d     (DMQ-S-05)
  QS-MOM-06  streak_active             (DMQ-S-06)
  QS-MOM-07  streak_length             (DMQ-S-07)
  QS-MOM-08  domain_expansion_rate     (DMQ-S-08)
  QS-MOM-09  lgc_score                 (DMQ-S-09 — from CML6-PCD)
  QS-MOM-10  isc_score                 (DMQ-S-10 — from CML6-PCD)
  QS-MOM-11  uci_tier_numeric          (DMQ-S-11 — from CML6-PCD)
  QS-MOM-12  comeback_indicator        (DMQ-S-12)
  QS-MOM-13  upset_score_14d           (DMQ-S-13)
  QS-MOM-14  adaptive_diff_passrate    (DMQ-S-14)
  QS-MOM-15  peer_comparison_score     (DMQ-S-15)
  QS-MOM-16  days_to_threshold_est     (DMQ-S-16 — QFM-M5 output)
  QS-MOM-17  dmq_score                 (QFM-M3 output)
  QS-MOM-18  qualification_momentum_class  (QFM-M3 class output)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS IV — INTEGRITY SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  QS-INT-01  fraud_hold                 Boolean (from CML6-ANTICHEAT)
  QS-INT-02  risk_tier                  GREEN/YELLOW/ORANGE/RED
  QS-INT-03  uci_tier                   PLATINUM/.../FLAGGED/PROVISIONAL
  QS-INT-04  active_anomaly_count       Integer
  QS-INT-05  max_active_anomaly_severity  CRITICAL/HIGH/MEDIUM/NONE
  QS-INT-06  surge_legitimacy           CONFIRMED_NATURAL/SUSPICIOUS/UNVERIFIED
  QS-INT-07  gate2_result               PASS / CONDITIONAL / FAIL
  QS-INT-08  failed_icg_gates           List of ICG codes failed

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS V — FAIRNESS & STRUCTURAL SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  QS-FDG-01  region_slot_available      Boolean
  QS-FDG-02  region_current_fill_pct    Float 0.0–1.0
  QS-FDG-03  institution_current_fill_pct Float 0.0–1.0
  QS-FDG-04  institution_cap_hit        Boolean
  QS-FDG-05  first_gen_dmq_bonus_applied Float (0.0 or 5.0)
  QS-FDG-06  fairness_audit_result      APPROVED / HOLD
  QS-FDG-07  waitlist_priority_score    Float (if WAITLISTED)
  QS-FDG-08  waitlist_rank              Integer (if WAITLISTED)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
TOTAL QUALIFICATION SIGNALS: 56 across 5 classes
All signals stored immutably in QUALIFICATION_SIGNAL_SNAPSHOT
at time of each QDR generation.
```

---

## SECTION F — QUALIFICATION PIPELINE SPECIFICATION

```
PIPELINE EXECUTION MODES:

  MODE ON-DEMAND : Triggered per participant registration request
    Latency : < 800ms end-to-end (sync response to participant)
    Runs    : All gates 1–5, all models QFM-M1 to QFM-M5

  MODE BATCH-CYCLE : End-of-registration-window processing
    Trigger : Registration window closes
    Runs    : QFM-M6 Fairness Auditor on all pending QDRs
               Waitlist ranking (QFM-M4) for all WAITLISTED
               Final QDR publishing + notification dispatch

  MODE CYCLE-RECHECK : Mid-cycle qualification re-evaluation
    Trigger : Ops admin manual trigger OR significant event
               (fraud_hold cleared, anomaly cleared, appeal resolved)
    Runs    : Full re-evaluation for specific participants

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PIPELINE STAGES (MODE ON-DEMAND — primary path):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  STAGE QF-1 — REGISTRATION REQUEST VALIDATION
    Source   : Participant registration API call
    Action   : Validate request schema, check participant exists,
               confirm target_tier in range (1–6)
    Failure  : 400 Bad Request — schema error
               409 Conflict — duplicate registration attempt

  STAGE QF-2 — SIGNAL COLLECTION
    Action   : Fan out to all source services in parallel:
               → CML6-RANK: fetch AGCRS, rank_delta, peer_comparison
               → CML6-ANTICHEAT: fetch fraud_hold, risk_tier
               → CML6-PCD: fetch UCI, anomalies, surge_legitimacy
               → DOJO Belt Engine: fetch belt_level, belt_version
               → User Service: fetch age, verified status, sessions
               → Institution Service: fetch institution verification
               → Cohort Stats Cache: fetch tier cohort statistics
    Timeout  : 500ms parallel fetch hard limit
    Failure  : If any critical service times out → QDR deferred
               Emit: qualification_signal_fetch_failure

  STAGE QF-3 — GATE 1 EVALUATION (Hard Eligibility)
    Action   : Evaluate HEG-01 to HEG-08 (deterministic rules)
    IF any HEG fails → QDR = DISQUALIFIED → skip to QF-8
    IF all pass → proceed to QF-4

  STAGE QF-4 — GATE 2 EVALUATION (Integrity Clearance)
    Action   : Evaluate ICG-01 to ICG-05
    IF ICG-01 or ICG-02 or ICG-03 or ICG-04 fails → DISQUALIFIED
    IF ICG-05 fails → note CONDITIONAL flag, continue
    Proceed to QF-5

  STAGE QF-5 — GATE 3 EVALUATION (Static Performance)
    Action   : Run QFM-M1 (rule engine) for SPF-01 to SPF-04
    Output   : gate3_result, failed_spf_gates, margin_to_threshold
    IF Gate 3 PASS → set gate3_pass = TRUE
    IF Gate 3 FAIL → set gate3_pass = FALSE
    Proceed to QF-6 (always — Gate 3 fail is not disqualifying alone)

  STAGE QF-6 — GATE 4 EVALUATION (Dynamic Momentum)
    Action   : Run QFM-M2 (normalize DMQ signals)
               Run QFM-M3 (compute dmq_score + class)
               Run QFM-M5 (generate qualification roadmap)
               Apply FDG-04 first-gen bonus if applicable
    Output   : dmq_score, qualification_momentum_class
    Apply Gate 4 Decision Matrix (Section B)
    Derive preliminary QDR outcome

  STAGE QF-7 — GATE 5 EVALUATION (Fairness)
    Action   : Evaluate FDG-01 to FDG-05
    FDG-01 regional slot → adjust outcome if region slot available
    FDG-02 institution cap → downgrade QUALIFIED to WAITLISTED if capped
    FDG-03 gender monitoring → log only, no outcome change
    FDG-05 tenant isolation → enforce tenant scope

  STAGE QF-8 — QDR GENERATION
    Action   : Compile final QDR record with:
               All gate results, all signal snapshots,
               dmq_score, momentum_class, preliminary outcome,
               roadmap data (QFM-M5), human summary (LLM — R28-3)
               SHA-256 hash of QDR payload
    Store    : QUALIFICATION_DECISION_RECORDS (immutable)
    Emit     : qualification_decision_made event

  STAGE QF-9 — DOWNSTREAM NOTIFICATIONS & PROPAGATION
    Action   :
      → Notify participant (email + in-app) with QDR outcome
        and qualification roadmap (friendly, coaching tone — R29)
      → If QUALIFIED: notify CML6-RANK to add to tier competition pool
      → If DISQUALIFIED/WAITLISTED: no rank pool update
      → If WAITLISTED: add to waitlist with QFM-M4 priority score
      → Update participant qualification badge (R53)
      → Emit: qualification_published event

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
BATCH CYCLE FINALIZATION STAGES:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  STAGE QF-BC-1 — FAIRNESS AUDIT
    Action   : Run QFM-M6 on all pending QDRs in cycle batch
    Output   : bias_flags, demographic_parity_scores, recommendation
    IF HOLD_FOR_REVIEW → pause publishing → ops console alert

  STAGE QF-BC-2 — WAITLIST RANKING
    Action   : Run QFM-M4 on all WAITLISTED participants
    Update   : waitlist_priority_score and waitlist_rank

  STAGE QF-BC-3 — WILDCARD SLOT ALLOCATION
    Action   : Review approved wildcard applications
               (Super Admin / Board approval already captured)
               Allocate within per-tier wildcard slot caps

  STAGE QF-BC-4 — FINAL BATCH PUBLISH
    IF QFM-M6 = APPROVE_BATCH: finalize and publish all QDRs
    IF QFM-M6 = HOLD_FOR_REVIEW: ops admin must manually release
    On release: emit batch_qualification_published event
    Append to QAC (Qualification Audit Chain)

  STAGE QF-BC-5 — CYCLE TRANSPARENCY REPORT
    Action   : Generate per-tier qualification summary for R62
    Content  : Total applicants, QUALIFIED count, WAITLISTED,
               DISQUALIFIED breakdown by gate, wildcard count,
               demographic distribution (anonymized)
    Publish  : Public transparency page (R62)
```

---

## SECTION G — QUALIFICATION AUDIT CHAIN (QAC)

```
The Qualification Audit Chain is an append-only, SHA-256
chained log of every QDR generated by this agent.

QAC PURPOSE:
  Provides cryptographic proof that qualification decisions
  were not altered after issuance. Any modification to a
  historical QDR is detectable via chain hash mismatch.

QAC RECORD STRUCTURE:
  qac_id           : UUID
  qdr_id           : UUID (reference to QDR)
  participant_id   : UUID
  championship_id  : UUID
  target_tier      : Integer 1–6
  qdr_outcome      : QUALIFIED/CONDITIONAL/WAITLISTED/DISQUALIFIED
  qdr_hash         : SHA-256 of full QDR payload
  previous_qac_hash: SHA-256 of previous QAC record (chain link)
  qac_hash         : SHA-256(qdr_hash + previous_qac_hash + qac_id)
  created_at       : TIMESTAMPTZ

CHAIN INTEGRITY VALIDATION:
  System runs daily QAC integrity check job
  Any mismatch → CRITICAL alert → ops console + PagerDuty
  Hash mismatch = potential tampering event

RETENTION: 10 years minimum (R60 compliance, championship legal hold)
ACCESS   : Super Admin read-only via ops console
           No modification capability — not even Super Admin

APPEALS TRAIL:
  Appeal decisions (Section C + DOJO T16) are added as
  amendment QAC records referencing the original qac_id.
  Original record never modified — amendment chain only.
```

---

## SECTION H — DATABASE SCHEMA

```sql
-- ═══════════════════════════════════════════════════════════════════
-- QUALIFICATION FILTER MODEL — COMPLETE DATABASE SCHEMA
-- ═══════════════════════════════════════════════════════════════════

-- Master qualification decision record (immutable after creation)
TABLE qualification_decision_records (
  qdr_id                   UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id           UUID        NOT NULL REFERENCES users(id),
  championship_id          UUID        NOT NULL,
  target_tier              INT         NOT NULL CHECK (target_tier BETWEEN 1 AND 6),
  cycle_id                 UUID        NOT NULL,
  -- Gate results
  gate1_result             TEXT        NOT NULL,   -- PASS / FAIL
  gate1_failed_gates       TEXT[],                 -- HEG codes
  gate2_result             TEXT        NOT NULL,   -- PASS / CONDITIONAL / FAIL
  gate2_failed_gates       TEXT[],                 -- ICG codes
  gate3_result             TEXT        NOT NULL,   -- PASS / FAIL
  gate3_failed_gates       TEXT[],                 -- SPF codes
  gate3_margins            JSONB,                  -- distance from each threshold
  gate4_dmq_score          DECIMAL(7,4),
  gate4_momentum_class     TEXT,
  gate5_result             TEXT        NOT NULL,   -- PASS / ADJUSTED / FAIL
  gate5_flags              TEXT[],                 -- FDG codes triggered
  -- Final outcome
  qdr_outcome              TEXT        NOT NULL,   -- QUALIFIED/CONDITIONAL/WAITLISTED/DISQUALIFIED
  outcome_reasons          TEXT[]      NOT NULL,   -- Human-readable reason codes
  is_wildcard              BOOLEAN     DEFAULT FALSE,
  wildcard_approved_by     UUID        REFERENCES users(id),
  -- Roadmap
  estimated_qual_date      DATE,
  required_weekly_sessions INT,
  roadmap_domain_targets   JSONB,
  -- Human summary
  human_summary            TEXT,                   -- LLM generated (R28-3)
  -- Integrity
  qdr_hash                 TEXT        NOT NULL,   -- SHA-256
  qac_id                   UUID,                   -- FK to qualification_audit_chain
  created_at               TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_immutable             BOOLEAN     NOT NULL DEFAULT TRUE,
  -- Review / appeals
  reviewed_by              UUID        REFERENCES users(id),
  review_outcome           TEXT,
  review_notes             TEXT,
  review_completed_at      TIMESTAMPTZ
);

-- Qualification signal snapshots (immutable, one per QDR)
TABLE qualification_signal_snapshot (
  snapshot_id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  qdr_id                   UUID        NOT NULL REFERENCES qualification_decision_records(qdr_id),
  participant_id           UUID        NOT NULL REFERENCES users(id),
  championship_id          UUID        NOT NULL,
  target_tier              INT         NOT NULL,
  -- All 56 signals stored as JSON for future auditability
  identity_signals         JSONB       NOT NULL,   -- QS-ID-01 to QS-ID-14
  performance_signals      JSONB       NOT NULL,   -- QS-PF-01 to QS-PF-12
  momentum_signals         JSONB       NOT NULL,   -- QS-MOM-01 to QS-MOM-18
  integrity_signals        JSONB       NOT NULL,   -- QS-INT-01 to QS-INT-08
  fairness_signals         JSONB       NOT NULL,   -- QS-FDG-01 to QS-FDG-08
  captured_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Championship cycle registry
TABLE championship_cycles (
  cycle_id                 UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  championship_id          UUID        NOT NULL,
  target_tier              INT         NOT NULL,
  cycle_name               TEXT        NOT NULL,
  registration_open_date   TIMESTAMPTZ NOT NULL,
  registration_close_date  TIMESTAMPTZ NOT NULL,
  competition_start_date   TIMESTAMPTZ NOT NULL,
  competition_end_date     TIMESTAMPTZ NOT NULL,
  total_slots              INT         NOT NULL,
  wildcard_slots           INT         NOT NULL,
  status                   TEXT        NOT NULL DEFAULT 'UPCOMING',
                                       -- UPCOMING/REGISTRATION/COMPETING/CLOSED
  created_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Per-tier slot tracking
TABLE tier_slot_tracker (
  tracker_id               UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  cycle_id                 UUID        NOT NULL REFERENCES championship_cycles(cycle_id),
  target_tier              INT         NOT NULL,
  region_code              TEXT        NOT NULL,
  institution_id           UUID,
  total_allocated_slots    INT         NOT NULL,
  filled_slots             INT         NOT NULL DEFAULT 0,
  wildcard_slots_used      INT         NOT NULL DEFAULT 0,
  updated_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Waitlist registry
TABLE qualification_waitlist (
  waitlist_id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  qdr_id                   UUID        NOT NULL REFERENCES qualification_decision_records(qdr_id),
  participant_id           UUID        NOT NULL REFERENCES users(id),
  cycle_id                 UUID        NOT NULL REFERENCES championship_cycles(cycle_id),
  target_tier              INT         NOT NULL,
  waitlist_priority_score  DECIMAL(7,4) NOT NULL,
  waitlist_rank            INT         NOT NULL,
  status                   TEXT        NOT NULL DEFAULT 'WAITING',
                                       -- WAITING/PROMOTED/EXPIRED/WITHDRAWN
  added_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  promoted_at              TIMESTAMPTZ,
  expired_at               TIMESTAMPTZ
);

-- Qualification audit chain (immutable append-only)
TABLE qualification_audit_chain (
  qac_id                   UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  qdr_id                   UUID        NOT NULL,
  participant_id           UUID        NOT NULL,
  championship_id          UUID        NOT NULL,
  target_tier              INT         NOT NULL,
  qdr_outcome              TEXT        NOT NULL,
  qdr_hash                 TEXT        NOT NULL,
  previous_qac_hash        TEXT,                   -- NULL for first record
  qac_hash                 TEXT        NOT NULL,   -- SHA-256 chain hash
  record_type              TEXT        NOT NULL DEFAULT 'ORIGINAL',
                                       -- ORIGINAL / AMENDMENT / APPEAL_RESULT
  predecessor_qac_id       UUID,                   -- for amendments
  created_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Wildcard application registry
TABLE wildcard_applications (
  wildcard_id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id           UUID        NOT NULL REFERENCES users(id),
  championship_id          UUID        NOT NULL,
  cycle_id                 UUID        NOT NULL,
  target_tier              INT         NOT NULL,
  dmq_score_at_application DECIMAL(7,4) NOT NULL,
  agcrs_at_application     DECIMAL(10,4) NOT NULL,
  wc_criteria_results      JSONB       NOT NULL,   -- WC-01 to WC-08 results
  application_status       TEXT        NOT NULL DEFAULT 'PENDING',
                                       -- PENDING/APPROVED/DENIED/BOARD_REVIEW
  level1_approver_id       UUID        REFERENCES users(id),
  level1_approved_at       TIMESTAMPTZ,
  level2_approver_id       UUID        REFERENCES users(id),
  level2_approved_at       TIMESTAMPTZ,
  board_decision           TEXT,
  board_decision_at        TIMESTAMPTZ,
  board_notes              TEXT,
  applied_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Fairness audit batch results
TABLE qualification_fairness_audit (
  audit_id                 UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  cycle_id                 UUID        NOT NULL,
  target_tier              INT         NOT NULL,
  bias_flags               JSONB       NOT NULL,   -- flags per demographic pair
  parity_scores            JSONB       NOT NULL,   -- per group pair
  recommendation           TEXT        NOT NULL,   -- APPROVE_BATCH/HOLD_FOR_REVIEW
  batch_released           BOOLEAN     DEFAULT FALSE,
  released_by              UUID        REFERENCES users(id),
  released_at              TIMESTAMPTZ,
  audited_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Appeals registry (DOJO T16)
TABLE qualification_appeals (
  appeal_id                UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  qdr_id                   UUID        NOT NULL REFERENCES qualification_decision_records(qdr_id),
  participant_id           UUID        NOT NULL REFERENCES users(id),
  cycle_id                 UUID        NOT NULL,
  target_tier              INT         NOT NULL,
  appeal_reason            TEXT        NOT NULL,
  evidence_submitted       JSONB,
  appeal_status            TEXT        NOT NULL DEFAULT 'SUBMITTED',
                                       -- SUBMITTED/IN_REVIEW/RESOLVED/DISMISSED
  assigned_to              UUID        REFERENCES users(id),
  board_decision           TEXT,       -- UPHELD / OVERTURNED / PARTIAL
  board_notes              TEXT,
  submitted_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  resolved_at              TIMESTAMPTZ,
  new_qdr_id               UUID        REFERENCES qualification_decision_records(qdr_id)
);

-- Model performance audit
TABLE qfm_model_performance_audit (
  audit_id                 UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  model_code               TEXT        NOT NULL,   -- QFM-M1 to QFM-M6
  model_version            TEXT        NOT NULL,
  evaluation_metric        TEXT        NOT NULL,
  metric_value             DECIMAL(10,6) NOT NULL,
  threshold_passed         BOOLEAN     NOT NULL,
  fairness_pass            BOOLEAN     NOT NULL,
  evaluated_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  evaluated_by             UUID        REFERENCES users(id),
  deployment_approved      BOOLEAN     DEFAULT FALSE,
  approved_by              UUID        REFERENCES users(id),
  approved_at              TIMESTAMPTZ
);

-- ════════════ INDEXES ════════════
CREATE INDEX idx_qdr_participant      ON qualification_decision_records (participant_id, created_at);
CREATE INDEX idx_qdr_cycle_tier       ON qualification_decision_records (cycle_id, target_tier);
CREATE INDEX idx_qdr_outcome          ON qualification_decision_records (qdr_outcome);
CREATE INDEX idx_qdr_wildcard         ON qualification_decision_records (is_wildcard) WHERE is_wildcard = TRUE;
CREATE INDEX idx_waitlist_cycle       ON qualification_waitlist (cycle_id, target_tier, status);
CREATE INDEX idx_waitlist_rank        ON qualification_waitlist (waitlist_rank);
CREATE INDEX idx_qac_participant      ON qualification_audit_chain (participant_id, created_at);
CREATE INDEX idx_qac_chain            ON qualification_audit_chain (previous_qac_hash);
CREATE INDEX idx_slots_cycle_region   ON tier_slot_tracker (cycle_id, region_code);
CREATE INDEX idx_wildcard_status      ON wildcard_applications (application_status);
CREATE INDEX idx_appeals_status       ON qualification_appeals (appeal_status);
CREATE INDEX idx_signal_qdr           ON qualification_signal_snapshot (qdr_id);
```

---

## SECTION I — API CONTRACT REGISTRY

```yaml
openapi: 3.1.0
info:
  title: Championship ML (6) Qualification Filter Model API — AntiGravity
  version: 1.0.0

paths:

  /qualification/antigravity/register:
    post:
      summary: Submit qualification registration for a participant
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [participant_id, championship_id, target_tier, cycle_id]
              properties:
                participant_id:  {type: string, format: uuid}
                championship_id: {type: string, format: uuid}
                target_tier:     {type: integer, minimum: 1, maximum: 6}
                cycle_id:        {type: string, format: uuid}
      responses:
        "202":
          description: Registration accepted — evaluation in progress
          content:
            application/json:
              schema:
                type: object
                properties:
                  request_id: {type: string, format: uuid}
                  estimated_completion_ms: {type: integer}
        "409": {description: Duplicate registration or cycle closed}
        "422": {description: Registration window closed for this tier}

  /qualification/antigravity/decision/{participant_id}:
    get:
      summary: Get qualification decision record for participant
      security: [{bearerAuth: []}]
      parameters:
        - name: participant_id
          in: path
          required: true
          schema: {type: string, format: uuid}
        - name: cycle_id
          in: query
          required: true
          schema: {type: string, format: uuid}
        - name: target_tier
          in: query
          required: true
          schema: {type: integer}
      responses:
        "200":
          description: Qualification Decision Record
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/QualificationDecisionRecord'
        "404": {description: No decision found for this cycle}

  /qualification/antigravity/decision/{participant_id}/summary:
    get:
      summary: Get participant-visible QDR summary (coaching view)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Coaching-friendly summary (no raw gate codes)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/QDRParticipantSummary'

  /qualification/antigravity/decision/{participant_id}/roadmap:
    get:
      summary: Get qualification roadmap (DISQUALIFIED / WAITLISTED only)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Personalised readiness roadmap from QFM-M5

  /qualification/antigravity/cycle/{cycle_id}/qualified:
    get:
      summary: Get list of qualified participants for a cycle (ops admin)
      security: [{bearerAuth: []}]
      parameters:
        - name: cycle_id
          in: path
          required: true
          schema: {type: string, format: uuid}
        - name: tier
          in: query
          schema: {type: integer}
      responses:
        "200":
          description: Qualified participant list for this cycle

  /qualification/antigravity/cycle/{cycle_id}/waitlist:
    get:
      summary: Get ranked waitlist for a cycle (ops admin)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Ranked waitlist

  /qualification/antigravity/cycle/{cycle_id}/fairness-audit:
    get:
      summary: Get fairness audit result for a batch cycle (ops admin)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Fairness audit result + parity scores

  /qualification/antigravity/cycle/{cycle_id}/fairness-audit/release:
    post:
      summary: Release fairness-held batch (ops admin — MFA required)
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [release_notes]
              properties:
                release_notes: {type: string, minLength: 30}
      responses:
        "200": {description: Batch released and published}
        "403": {description: MFA required or insufficient role}

  /qualification/antigravity/wildcard/apply:
    post:
      summary: Submit wildcard qualification application
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [participant_id, championship_id, cycle_id, target_tier]
              properties:
                participant_id:  {type: string, format: uuid}
                championship_id: {type: string, format: uuid}
                cycle_id:        {type: string, format: uuid}
                target_tier:     {type: integer}
      responses:
        "202": {description: Wildcard application submitted}
        "409": {description: Wildcard slots exhausted for this tier}

  /qualification/antigravity/wildcard/{wildcard_id}/approve:
    post:
      summary: Approve wildcard application (ops admin / super admin)
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [approval_notes]
              properties:
                approval_notes: {type: string, minLength: 20}
      responses:
        "200": {description: Wildcard approved}
        "403": {description: Insufficient approval authority for this tier}

  /qualification/antigravity/appeals/submit:
    post:
      summary: Submit qualification appeal (participant)
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [qdr_id, appeal_reason]
              properties:
                qdr_id:        {type: string, format: uuid}
                appeal_reason: {type: string, minLength: 50}
                evidence:      {type: object}
      responses:
        "202": {description: Appeal submitted}
        "409": {description: Appeal window closed (> 7 days from QDR)}

  /qualification/antigravity/appeals/{appeal_id}/resolve:
    post:
      summary: Resolve qualification appeal (ops admin / board)
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [board_decision, board_notes]
              properties:
                board_decision: {type: string, enum: [UPHELD, OVERTURNED, PARTIAL]}
                board_notes:    {type: string, minLength: 30}
      responses:
        "200": {description: Appeal resolved — new QDR issued if OVERTURNED}

  /qualification/antigravity/audit-chain/{championship_id}:
    get:
      summary: Get qualification audit chain (super admin only)
      security: [{bearerAuth: []}]
      responses:
        "200": {description: Qualification Audit Chain records}

  /qualification/antigravity/transparency/{championship_id}/{target_tier}:
    get:
      summary: Get public transparency report for a tier's cycle (public)
      security: []
      responses:
        "200":
          description: Anonymized qualification statistics (R62)

components:
  schemas:
    QualificationDecisionRecord:
      type: object
      properties:
        qdr_id:           {type: string, format: uuid}
        participant_id:   {type: string, format: uuid}
        target_tier:      {type: integer}
        qdr_outcome:      {type: string, enum: [QUALIFIED, CONDITIONAL, WAITLISTED, DISQUALIFIED]}
        gate1_result:     {type: string}
        gate2_result:     {type: string}
        gate3_result:     {type: string}
        gate4_dmq_score:  {type: number}
        gate5_result:     {type: string}
        outcome_reasons:  {type: array, items: {type: string}}
        is_wildcard:      {type: boolean}
        human_summary:    {type: string}
        created_at:       {type: string, format: date-time}

    QDRParticipantSummary:
      type: object
      properties:
        qdr_outcome:       {type: string}
        next_steps:        {type: string}
        estimated_ready:   {type: string, format: date}
        roadmap_actions:   {type: array, items: {type: string}}
        coaching_message:  {type: string}

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

security:
  - bearerAuth: []
```

---

## SECTION J — EVENT SCHEMA REGISTRY (AsyncAPI)

```yaml
asyncapi: 2.6.0
info:
  title: AntiGravity Qualification Filter Model Events
  version: 1.0.0

channels:

  qualification.decision.made:
    publish:
      message:
        payload:
          type: object
          properties:
            qdr_id:           {type: string}
            participant_id:   {type: string}
            championship_id:  {type: string}
            target_tier:      {type: integer}
            qdr_outcome:      {type: string}
            is_wildcard:      {type: boolean}
            decided_at:       {type: string, format: date-time}

  qualification.batch.published:
    publish:
      message:
        payload:
          type: object
          properties:
            cycle_id:         {type: string}
            target_tier:      {type: integer}
            qualified_count:  {type: integer}
            waitlisted_count: {type: integer}
            disqualified_count: {type: integer}
            published_at:     {type: string, format: date-time}

  qualification.waitlist.promoted:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            cycle_id:         {type: string}
            target_tier:      {type: integer}
            previous_rank:    {type: integer}
            promoted_at:      {type: string, format: date-time}

  qualification.wildcard.approved:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            cycle_id:         {type: string}
            target_tier:      {type: integer}
            approved_by:      {type: string}
            approved_at:      {type: string, format: date-time}

  qualification.appeal.resolved:
    publish:
      message:
        payload:
          type: object
          properties:
            appeal_id:        {type: string}
            participant_id:   {type: string}
            board_decision:   {type: string}
            new_qdr_id:       {type: string}
            resolved_at:      {type: string, format: date-time}

  qualification.fairness.hold.raised:
    publish:
      message:
        payload:
          type: object
          properties:
            cycle_id:         {type: string}
            target_tier:      {type: integer}
            bias_flags_count: {type: integer}
            raised_at:        {type: string, format: date-time}

  qualification.signal.fetch.failure:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            failed_service:   {type: string}
            cycle_id:         {type: string}
            failed_at:        {type: string, format: date-time}

  qualification.audit.chain.integrity.mismatch:
    publish:
      message:
        payload:
          type: object
          properties:
            championship_id:  {type: string}
            mismatched_qac_id:{type: string}
            detected_at:      {type: string, format: date-time}
```

---

## SECTION K — MICROSERVICE ARCHITECTURE

```
SERVICE: qualification-filter-service
  Language   : Python 3.11 (R1 compliant)
  Framework  : FastAPI
  Port       : 8033
  Stack      : Redis 7, PostgreSQL 15, LightGBM, Prophet, scikit-learn

RESPONSIBILITIES:
  1. Accept and validate participant qualification registrations
  2. Fan out signal collection to peer services in parallel
  3. Execute all 5 gate layers (deterministic + ML)
  4. Generate QDR with SHA-256 hash + append to QAC
  5. Run batch fairness audit (QFM-M6) at cycle close
  6. Maintain and rank waitlist (QFM-M4)
  7. Generate qualification roadmaps (QFM-M5)
  8. Process wildcard applications and approval workflow
  9. Handle appeals (DOJO T16) workflow
  10. Broadcast events (Section J)
  11. Expose REST API (Section I)
  12. Publish public transparency report (R62)

INTERNAL INTERFACES:
  → ranking-engine-service:8030
      GET  /rankings/antigravity/participant/{id}   (AGCRS, rank signals)
      POST /rankings/antigravity/tier-pool/add      (add QUALIFIED to pool)
  → anticheat-behavioral-service:8031
      GET  /anticheat/antigravity/risk/{id}         (integrity signals)
  → consistency-detector-service:8032
      GET  /consistency/antigravity/profile/{id}    (UCI, anomaly signals)
  → dojo-belt-engine-service
      GET  /belt/participant/{id}/current            (belt level + version)
  → user-service
      GET  /users/{id}/profile                      (age, sessions, verified)
  → institution-service
      GET  /institution/{id}/verification           (institution verification)
  → notification-service:8010
      POST /notify                                  (QDR outcome notifications)
  → ops-console-service:9000
      POST /ops/alerts                              (fairness hold, QAC mismatch)

HEALTH CHECK:
  GET /health
  Returns: {status, models_loaded, active_cycles, queue_depth,
            qac_integrity_last_check, last_fairness_audit}

DOCKER BUILD:
  File: /backend/services/qualification_filter_service/Dockerfile
  Base: python:3.11-slim
  CMD : uvicorn main:app --host 0.0.0.0 --port 8033

KUBERNETES:
  Name        : qualification-filter-service
  Internal URL: http://qualification-filter-service:8033
  Namespace   : ecoskiller-{env}
  Replicas    : Min 2, Max 12 (HPA on CPU > 60%)
               (Burst capacity for registration window open events)

CRON JOBS:
  Daily 01:00 UTC  → QAC integrity validation
  Daily 02:00 UTC  → QFM-M2 cohort statistics refresh
  End of cycle     → Batch fairness audit + waitlist ranking
```

---

## SECTION L — UI SCREENS SPECIFICATION

```
SCREEN QF-01: PARTICIPANT — QUALIFICATION STATUS HUB
  Route   : /app/championship/antigravity/qualify
  Access  : Authenticated participant
  Purpose : Central qualification management screen
  Components:
    - Active championship tiers (cards per tier):
        Tier name + registration window countdown
        "Check Eligibility" preview button (pre-registration)
        "Register" CTA (if window open)
        Current status chip: NOT REGISTERED / IN REVIEW /
          QUALIFIED ✓ / WAITLISTED / DISQUALIFIED
    - My Qualification Journey (timeline):
        Past tier qualifications with dates and outcomes
        Current cycle status
    - Qualification achievement badges (R53):
        "First Qualification", "Tier 3 Achiever", etc.
    - "View My Roadmap" CTA → QF-02 (if WAITLISTED/DISQUALIFIED)

SCREEN QF-02: PARTICIPANT — QUALIFICATION DECISION DETAIL
  Route   : /app/championship/antigravity/qualify/{cycle_id}/result
  Access  : Authenticated participant (own data only)
  Purpose : Show QDR outcome in coaching-friendly format
  Components:
    - Outcome banner: QUALIFIED 🏆 / WAITLISTED ⏳ /
                      WORKING TOWARD IT 💪 / KEEP GOING 🌱
      (DISQUALIFIED never shown as "disqualified" — reframed)
    - Human-readable summary (LLM — R28-3):
        "What this means for you"
        "What you did well"
        "What to focus on next"
    - Gate status summary (friendly labels, NOT codes):
        ✓ Your account is verified
        ✓ You have enough sessions
        ⚡ Keep building your score (if SPF failed)
    - Momentum badge: ELITE SURGE / STRONG MOMENTUM / etc.
    - Roadmap section (if not QUALIFIED):
        "Your estimated ready date: [date]"
        "Weekly session target: [N] per week"
        "Domain goals: [list]"
        Progress bars showing current vs. target per metric
    - Appeal button (if within 7-day window)
    - "Continue Training" CTA → skill training dashboard

SCREEN QF-03: PARTICIPANT — APPEAL SUBMISSION
  Route   : /app/championship/antigravity/qualify/{cycle_id}/appeal
  Access  : Authenticated participant (own QDR only)
  Components:
    - Original decision summary (read only)
    - Appeal reason text field (min 50 characters)
    - Evidence upload (optional — document / screenshot)
    - DOJO T16 process explanation (timeline, what happens next)
    - Submit button → confirmation + case_id
    - Appeal status tracker (after submission)

SCREEN QF-04: OPS ADMIN — CYCLE MANAGEMENT DASHBOARD
  Route   : /ops/championship/antigravity/qualification/cycles
  Access  : Ops Admin + Super Admin (MFA enforced — R40)
  Purpose : Manage all active championship cycles and qualification
  Components:
    - Cycle list (per tier): status, dates, slot counts
    - Per cycle summary cards:
        Qualified / Waitlisted / Disqualified / Pending counts
        Fairness audit status badge
        Wildcard applications pending count
    - "Open Registration" / "Close Registration" cycle controls
    - "Publish Batch" button (if fairness audit = APPROVE_BATCH)
    - "View Qualified List" → QF-06
    - "View Waitlist" → QF-07
    - "Create New Cycle" action (per tier)

SCREEN QF-05: OPS ADMIN — INDIVIDUAL QDR REVIEWER
  Route   : /ops/championship/antigravity/qualification/qdr/{qdr_id}
  Access  : Ops Admin + Super Admin
  Purpose : Full QDR review for edge cases and appeals support
  Components:
    - All 5 gate results with detailed signal values
    - DMQ score + momentum class + SHAP breakdown (QFM-M3)
    - Roadmap prediction (QFM-M5)
    - Signal snapshot table (all 56 signals at decision time)
    - Peer service signals (ANTICHEAT + PCD links)
    - Human-readable QDR summary (LLM)
    - QAC hash verification status
    - "Override Decision" action (Super Admin only — requires
       reason ≥ 50 chars + creates QAC amendment record)
    - Appeal history (if any)

SCREEN QF-06: OPS ADMIN — QUALIFIED PARTICIPANTS LIST
  Route   : /ops/championship/antigravity/qualification/cycle/{id}/qualified
  Access  : Ops Admin
  Components:
    - Filterable table: participant, tier, gate path (static/momentum),
      wildcard flag, DMQ score, AGCRS, UCI tier
    - Region distribution chart (FDG-01 monitoring)
    - Institution distribution chart (FDG-02 monitoring)
    - Gender distribution chart (FDG-03 monitoring)
    - Export to CSV

SCREEN QF-07: OPS ADMIN — WAITLIST MANAGER
  Route   : /ops/championship/antigravity/qualification/cycle/{id}/waitlist
  Access  : Ops Admin
  Components:
    - Ranked waitlist table:
        waitlist_rank, participant_id, priority_score,
        dmq_score, agcrs, uci_tier, region, institution cap status
    - "Promote to Qualified" action (if slot opens)
    - Days on waitlist indicator
    - Auto-promotion threshold indicator

SCREEN QF-08: OPS ADMIN — WILDCARD REVIEW PANEL
  Route   : /ops/championship/antigravity/qualification/wildcards
  Access  : Ops Admin / Super Admin (tier-dependent)
  Components:
    - Pending wildcard applications by tier
    - Per application: all WC-01 to WC-08 criteria results
    - DMQ score + signals breakdown
    - Approval authority indicator (tier-appropriate)
    - "Approve" / "Deny" actions with mandatory notes
    - Board review queue (TIER 6 applications)
    - Wildcard slot counter per tier (used / total)

SCREEN QF-09: OPS ADMIN — APPEALS GOVERNANCE BOARD
  Route   : /ops/championship/antigravity/qualification/appeals
  Access  : Ops Admin + Super Admin
  Purpose : DOJO T16 Appeals & Governance Board workflow
  Components:
    - Appeal queue (SUBMITTED / IN_REVIEW / RESOLVED)
    - Per appeal: original QDR, reason, evidence, timeline
    - Assign to board member action
    - Board decision panel: UPHELD / OVERTURNED / PARTIAL
    - Notes field (min 30 chars required)
    - On OVERTURNED: auto-trigger QDR reissuance + QAC amendment
    - Full appeal audit log

SCREEN QF-10: PUBLIC — QUALIFICATION TRANSPARENCY REPORT
  Route   : /championship/antigravity/{championship_id}/transparency
  Access  : Public (no auth required — R62)
  Purpose : Build institutional trust via public transparency
  Components:
    - Per-tier qualification statistics (anonymized):
        Total applicants, qualified count, % qualified
        Wildcard count and % of total
        Regional distribution (anonymized)
        Gate failure distribution (how many failed at each gate)
        Fairness audit summary
    - Historic cycle comparison (trend chart)
    - "How Qualification Works" explainer section
    - Download data (CSV) option
```

---

## SECTION M — PERMISSION & ROLE MATRIX

```
ROLE             │OWN QDR │OWN      │ALL     │FULL    │OVERRIDE│APPROVE  │APPEAL
                 │SUMMARY │ROADMAP  │QDRs    │SIGNALS │QDR     │WILDCARD │RESOLVE
─────────────────┼─────────┼─────────┼────────┼────────┼────────┼─────────┼───────
participant      │YES      │YES      │NO      │NO      │NO      │NO       │YES*
recruiter        │NO       │NO       │NO      │NO      │NO      │NO       │NO
mentor           │NO       │NO       │NO      │NO      │NO      │YES T1-3 │YES*
institute_admin  │NO       │NO       │own†    │NO      │NO      │NO       │NO
ops_admin        │YES      │YES      │YES     │YES     │NO      │YES T2-4 │YES
super_admin      │YES      │YES      │YES     │YES     │YES     │YES T1-6 │YES
board_member     │NO       │NO       │NO      │YES‡    │NO      │YES T6   │YES
internal_service │ingest   │ingest   │NO      │NO      │NO      │NO       │NO

* participants and mentors: appeal SUBMIT only, not resolve
† institute_admin: their institution's participants only
‡ board_member: evidence and signal read for appeal review only

WILDCARD APPROVAL AUTHORITY (enforced in API — from Section C):
  TIER 2-3 wildcard : ops_admin
  TIER 4-5 wildcard : super_admin
  TIER 6 wildcard   : board review (min 3 board_member confirmations)
```

---

## SECTION N — INTERN EXECUTION GUIDE

```
OBJECTIVE: Run qualification filter service locally.
INTERN ROLE: Python Backend Developer
SKILL LEVEL: Intermediate Python + database comfort

REQUIRED TOOLS:
  Python 3.11+, PostgreSQL 15, Redis 7, Docker

STEP-BY-STEP:

Step 1 — Navigate to service:
  cd /backend/services/qualification_filter_service/

Step 2 — Install dependencies:
  pip install -r requirements.txt
  (Includes: fastapi, uvicorn, lightgbm, prophet, scikit-learn,
   alembic, redis, asyncpg, httpx, hashlib)

Step 3 — Configure environment:
  cp .env.example .env
  Fill in:
    DB_HOST, REDIS_HOST
    RANKING_ENGINE_URL=http://localhost:8030
    ANTICHEAT_SERVICE_URL=http://localhost:8031
    CONSISTENCY_SERVICE_URL=http://localhost:8032
    BELT_ENGINE_URL=http://localhost:[dojo_port]
    USER_SERVICE_URL=http://localhost:[user_port]
    MODEL_PATH=/models/qualification/

Step 4 — Load dev ML models:
  python scripts/load_models.py --env dev
  Expected:
    "QFM-M1 loaded: rule engine initialized"
    "QFM-M2 loaded: normalizer_v1.joblib"
    "QFM-M3 loaded: dmq_classifier_v1.joblib"
    "QFM-M4 loaded: waitlist_ranker_v1.joblib"
    "QFM-M5 loaded: readiness_forecaster_v1.pkl"
    "QFM-M6 loaded: fairness_auditor_v1.joblib"

Step 5 — Run DB migrations:
  alembic upgrade head
  Expected: All qualification tables created with indexes.

Step 6 — Seed test data:
  python scripts/seed_test_cycle.py --env dev
  Expected: 1 test championship cycle created per tier (6 cycles)
             Test slot counts seeded per tier

Step 7 — Start service:
  uvicorn main:app --reload --port 8033
  Expected: "Uvicorn running on http://127.0.0.1:8033"

Step 8 — Verify health:
  curl http://127.0.0.1:8033/health
  Expected: {
    "status": "healthy",
    "models_loaded": 6,
    "active_cycles": 6,
    "qac_integrity_last_check": null,
    "queue_depth": 0
  }

Step 9 — Test qualification registration:
  POST http://127.0.0.1:8033/qualification/antigravity/register
  Body: (see test_payloads/qual_register_tier1.json in repo)
  Expected: {"request_id": "...", "estimated_completion_ms": 800}

Step 10 — Check decision:
  GET http://127.0.0.1:8033/qualification/antigravity/decision/{participant_id}
      ?cycle_id={test_cycle_id}&target_tier=1
  Expected: QDR JSON with gate results and outcome

Step 11 — Verify QAC integrity:
  python scripts/validate_qac.py --env dev
  Expected: "QAC integrity check: 1 records validated, 0 mismatches"

SUCCESS CONDITIONS:
  ✔ Service running on port 8033
  ✔ All 6 models loaded (including rule engine)
  ✔ 6 test cycles created (one per tier)
  ✔ Health endpoint returns 200 with models_loaded: 6
  ✔ Registration endpoint returns 202
  ✔ QDR endpoint returns 200 with gate results
  ✔ QAC validation script reports 0 mismatches
  ✔ No runtime exceptions in logs

FAILURE → STOP EXECUTION
```

---

## SECTION O — ML MODEL SPECIFICATION & GOVERNANCE

```
MODEL PERFORMANCE THRESHOLDS (LOCKED — v1.0):

  QFM-M1  Rule Engine (Static Threshold)
    Metric    : 100% deterministic — no probabilistic threshold
    Audit     : Threshold values locked in version-controlled config
    Mutation  : Version bump required to change any threshold value

  QFM-M2  Signal Normalizer
    Metric    : Cohort statistics freshness < 24 hours
    Below     : STALE_COHORT warning → use previous valid stats

  QFM-M3  DMQ Classifier (LightGBM)
    Metric    : AUC-ROC ≥ 0.88, Precision at 80% recall ≥ 0.82
    Below     : REJECT deployment → retain previous version
    Post-hoc  : 6-month follow-up performance validation required
                to confirm qualified participants competed successfully

  QFM-M4  Waitlist Ranker (LightGBM LTR)
    Metric    : NDCG@10 ≥ 0.80
    Below     : REJECT deployment

  QFM-M5  Readiness Forecaster (Prophet)
    Metric    : MAE on estimated_qualification_date ≤ 14 days
               on held-out validation participants
    Below     : REJECT deployment
    Note      : Advisory only — does not affect QDR outcome

  QFM-M6  Fairness Auditor
    Metric    : Demographic parity detection recall ≥ 0.90
               (ability to catch known bias patterns in test data)
    Below     : REJECT deployment → all batches require manual review

FAIRNESS REQUIREMENTS (R51 + DOJO T12):
  Before any model deployment:
    ✔ Gender demographic parity < 0.05 in qualification rate
    ✔ Region demographic parity < 0.05 in qualification rate
    ✔ Institution type parity < 0.08 (higher tolerance for structural reasons)
    ✔ Age group parity < 0.05
    ✔ First-generation learner parity monitored (not gating)
  Failure → REJECT model → alert ML team + fairness reviewer

HUMAN APPROVAL REQUIRED (M5 — AI Model Reality Law):
  ✔ Performance threshold passed
  ✔ Fairness checks passed
  ✔ ML team sign-off in qfm_model_performance_audit table
  ✔ Ops admin deployment approval in same table
  ✔ For QFM-M3 specifically: outcome validation on prior cycle required
    before deployment on next cycle

RETRAIN SCHEDULES:
  QFM-M2  Daily cohort stats refresh (not full retrain)
  QFM-M3  After every championship cycle (labeled outcome data)
  QFM-M4  After every championship cycle
  QFM-M5  Monthly (new readiness tracking data)
  QFM-M6  After every championship cycle + quarterly fairness review

EXPLAINABILITY (R28-3 + DOJO P10):
  QFM-M3  SHAP values → top 5 momentum signals driving DMQ score
  QFM-M4  SHAP values → waitlist ranking explanation
  QFM-M5  Prophet component decomposition → roadmap narrative
  QFM-M6  Parity report → which demographic pairs triggered hold
  LLM Role: Text generation ONLY for:
    1. QDR human_summary (participant-friendly coaching text)
    2. QDR participant roadmap narrative
    3. Appeal pre-assessment summary for board review
    LLM NEVER computes scores or gate decisions.

OUTCOME VALIDATION (DOJO T13):
  6 months after each cycle closes:
  → Compare QUALIFIED participants' actual competition performance
    to their QFM-M3 predicted momentum class
  → Measure: did ELITE_SURGE performers outperform DEVELOPING at
    target tier?
  → Results feed next retrain of QFM-M3
  → Published in annual platform transparency report (R62)
```

---

## SECTION P — OBSERVABILITY & MONITORING

```
METRICS (Prometheus — from port 8033):

  qfm_registrations_total              (counter, labels: target_tier)
  qfm_decisions_by_outcome             (counter, labels: outcome, tier)
  qfm_gate_failures_total              (counter, labels: gate_layer, gate_code)
  qfm_dmq_score_distribution          (histogram, labels: tier)
  qfm_pipeline_latency_ms             (histogram)
  qfm_signal_fetch_failures_total      (counter, labels: service)
  qfm_waitlist_current_size            (gauge, labels: tier)
  qfm_wildcard_applications_pending    (gauge, labels: tier)
  qfm_appeals_open_count               (gauge)
  qfm_fairness_holds_active            (gauge)
  qfm_qac_records_total               (counter)
  qfm_qac_integrity_mismatches_total  (counter)
  qfm_qualification_rate_by_tier      (gauge, labels: tier)

GRAFANA ALERTS:
  ALERT: qfm_qac_integrity_mismatches_total > 0
         → PagerDuty: CRITICAL — QAC integrity breach detected
  ALERT: qfm_fairness_holds_active > 0 for > 2 hours
         → Slack: Fairness hold not released — ops review needed
  ALERT: qfm_signal_fetch_failures_total > 20 in 5m
         → Slack: Signal collection degradation — peer service issue
  ALERT: qfm_pipeline_latency_ms p95 > 1500ms
         → Slack: Qualification pipeline latency elevated
  ALERT: qfm_qualification_rate_by_tier drops > 20% vs. last cycle
         → Slack: Significant qualification rate drop — review thresholds

LOGGING (Loki):
  Log level     : INFO in prod, DEBUG in dev
  PII policy    : participant_id only in logs
  Retention     : 90 days hot, 10 years cold (QAC legal retention R60)

TRACING (Jaeger):
  Full trace per qualification pipeline execution
  Signal fan-out spans per peer service
  Gate evaluation spans with pass/fail annotation
  QAC append spans with hash verification
```

---

## SECTION Q — DOJO SAAS INTEGRATION COMPLIANCE

```
DOJO T1 — Skill Validity Framework Lock
  ✔ SPF-04 Belt prerequisite gate enforces construct-linked
    skill certification before tier advancement
  ✔ SPF-02 Domain breadth requirement enforces multi-construct
    coverage — not just single-domain specialisation
  ✔ ICG-03 UCI gate enforces consistency validity before qualification

DOJO T2 — Scoring Validity & Reliability Lock
  ✔ ICG-03 blocks PROVISIONAL UCI (insufficient data for valid decision)
  ✔ Gate 3 margin_to_threshold shows confidence in static decision
  ✔ QFM-M3 provides confidence in momentum decision (AUC-ROC ≥ 0.88)
  ✔ QDR confidence not applied below data minimums

DOJO T5 — Match Fairness Engine Lock
  ✔ FDG-02 institution diversity cap prevents bloc dominance
  ✔ FDG-01 regional slot reservation ensures geographic fairness
  ✔ QFM-M6 fairness auditor catches systematic bias pre-publish
  ✔ Wildcard slot caps prevent elite institution advantages

DOJO T12 — Localization & Cultural Fairness Lock
  ✔ FDG-01 regional representation slots (geographic equity)
  ✔ FDG-04 first-generation learner DMQ bonus (+5.0)
  ✔ QFM-M6 monitors regional parity in qualification outcomes
  ✔ Transparency report (R62 / QF-10) shows regional distribution

DOJO T13 — Outcome Validation Lock
  ✔ QFM-M3 outcome validation: 6-month post-cycle performance check
  ✔ Validated results feed next model retrain cycle
  ✔ Published in annual platform transparency report
  ✔ Belt prerequisite predictive validity tracked via DOJO T17

DOJO T16 — Appeals & Governance Board Lock
  ✔ 7-day appeal window after QDR issuance
  ✔ Structured appeal workflow (QF-03, QF-09)
  ✔ Board decision: UPHELD / OVERTURNED / PARTIAL
  ✔ Overturned decisions auto-issue new QDR + QAC amendment
  ✔ All appeal decisions versioned and logged (qualification_appeals table)

DOJO T17 — Belt Version Governance Lock
  ✔ SPF-04 checks belt_version_tag matches current rubric version
  ✔ Deprecated belt versions trigger re-certification requirement
  ✔ Belt version validation via DOJO Belt Engine interface
  ✔ Belt promotion confirmation required before tier advancement

DOJO P1 — Security Hardening Lock
  ✔ QAC SHA-256 hash chain prevents record tampering
  ✔ QAC integrity daily validation job (CRITICAL alert on mismatch)
  ✔ Immutable QDR records (no modification after creation)
  ✔ Super Admin MFA required for QDR override
  ✔ Wildcard approval authority hierarchy enforced in API

DOJO P11 — Multi-Tenant SaaS Lock
  ✔ FDG-05 tenant isolation: TIER 1/2 scoped to participant's tenant
  ✔ Cross-tenant comparison activates only at TIER 3+
  ✔ tenant_id in all QDR records for tenant-scoped admin access
  ✔ institute_admin access scoped to own institution QDRs only

DOJO P12 — Support & Dispute Lock
  ✔ Full appeal workflow (DOJO T16) for qualification disputes
  ✔ Wildcard denial appeal → DOJO T16 governance board
  ✔ QDR override by Super Admin creates auditable amendment record
  ✔ All dispute outcomes tied to qdr_id + qac chain
```

---

## SECTION R — ENFORCEMENT RULES

```
ENFORCEMENT RULE QFE-01:
  Gate 1 (Hard Eligibility) is the absolute authority.
  No ML output from any gate may override a Gate 1 failure.
  Gate 1 failure always produces DISQUALIFIED — no exceptions.
  Override: Super Admin only via explicit QDR override (QF-05 screen)
  with mandatory audit trail in QAC.

ENFORCEMENT RULE QFE-02:
  No participant with fraud_hold = TRUE (ICG-01) may be
  QUALIFIED or placed as CONDITIONAL for any tier.
  fraud_hold overrides all other gate results.
  A fraud_hold cleared by CML6-ANTICHEAT triggers automatic
  re-evaluation (CYCLE-RECHECK pipeline mode).

ENFORCEMENT RULE QFE-03:
  Tier skipping (advancing more than 1 tier) is PROHIBITED
  except via approved Wildcard pathway (Section C).
  System enforces HEG-05 check. Wildcard bypass of HEG-05
  requires WC-01 to WC-08 ALL satisfied + appropriate
  approval authority for the target tier.

ENFORCEMENT RULE QFE-04:
  QDR records are immutable after creation.
  No field may be modified. Amendments require a new QDR
  record with predecessor reference + new QAC entry.
  Direct DB modification attempt → CRITICAL audit alert.

ENFORCEMENT RULE QFE-05:
  Batch cycle results MUST pass QFM-M6 fairness audit
  before batch_qualification_published event is emitted.
  Bypassing QFM-M6 → STOP EXECUTION → report: FAIRNESS_GATE_BYPASS.

ENFORCEMENT RULE QFE-06:
  Wildcard slots are hard-capped per tier per cycle (Section C).
  System enforces slot count before approving wildcard.
  Approval of wildcard when slots exhausted → STOP →
  report: WILDCARD_SLOT_OVERFLOW_ATTEMPT.

ENFORCEMENT RULE QFE-07:
  Belt version must be current for SPF-04 to pass.
  A participant with a deprecated-rubric belt MUST re-certify
  before qualifying for tiers requiring that belt level.
  No exceptions — DOJO T17 enforced.

ENFORCEMENT RULE QFE-08:
  QAC integrity check runs daily. Any hash mismatch
  → immediate PagerDuty alert → championship suspension
  pending investigation. Championship cannot resume
  until QAC integrity is verified clean.

ENFORCEMENT RULE QFE-09:
  Appeal window is exactly 7 calendar days from QDR creation.
  Appeals submitted after Day 7 → rejected (409 response).
  Exception: If QDR was demonstrably produced with a service
  error → Super Admin may re-open appeal window manually.

ENFORCEMENT RULE QFE-10:
  Public transparency report (QF-10) MUST be published
  within 72 hours of batch_qualification_published event.
  Absence → report: TRANSPARENCY_REPORT_OVERDUE → ops alert.
  R62 compliance required.

Violation of any enforcement rule above:
  → STOP EXECUTION
  → REPORT QFM_ENFORCEMENT_VIOLATION
  → NO CHAMPIONSHIP PARTICIPATION CLAIM PERMITTED
```

---

## SECTION S — CONTRACT GATE REQUIREMENTS (R49 Compliance)

```
Before deployment, all items must pass Contract Validator (R49):

  ✔ All QDR outcome values registered in schema registry
  ✔ All gate codes (HEG, ICG, SPF, FDG) registered
  ✔ All wildcard criteria (WC-01 to WC-08) registered
  ✔ API contract OpenAPI 3.1 validated and registered
  ✔ All AsyncAPI event channels registered
  ✔ Permission → Screen → Role matrix registered (Section M)
  ✔ All 6 ML models loaded with performance thresholds met
  ✔ Per-tier threshold tables seeded (Section B Gate 3 values)
  ✔ Per-tier slot tracker seeded for test cycles
  ✔ Integration: ranking-engine-service signal fetch tested
  ✔ Integration: anticheat-behavioral-service tested
  ✔ Integration: consistency-detector-service tested
  ✔ Integration: DOJO Belt Engine tested
  ✔ QAC hash chain integrity validated end-to-end
  ✔ Wildcard approval authority enforcement tested per tier
  ✔ Appeal 7-day window enforcement tested
  ✔ Fairness audit hold → release workflow tested
  ✔ R50 QA test suite generated and all tests passing

Absence of any item → STOP EXECUTION
```

---

## SECTION T — PRODUCTION READINESS CHECKLIST

```
Before agent is declared Production-Ready (L3 compliant):

INFRASTRUCTURE:
  ✔ qualification-filter-service pods STATUS = Running (min 2 replicas)
  ✔ Redis cache active (cohort stats)
  ✔ PostgreSQL qualification tables created with all indexes
  ✔ All 6 models loaded — /health returns models_loaded: 6
  ✔ Daily QAC integrity cron job active
  ✔ Daily cohort stats refresh cron job (QFM-M2) active
  ✔ Per-tier threshold config loaded and validated

ML MODELS:
  ✔ QFM-M1 deterministic thresholds version-locked in config
  ✔ QFM-M3 AUC-ROC ≥ 0.88, Precision at 80% recall ≥ 0.82
  ✔ QFM-M4 NDCG@10 ≥ 0.80
  ✔ QFM-M5 MAE ≤ 14 days
  ✔ QFM-M6 Detection recall ≥ 0.90
  ✔ All fairness checks passed (parity < 0.05 / 0.08)
  ✔ Human approval logs in qfm_model_performance_audit

INTEGRATIONS:
  ✔ CML6-RANK signal fetch live-tested
  ✔ CML6-ANTICHEAT signal fetch live-tested
  ✔ CML6-PCD signal fetch live-tested
  ✔ DOJO Belt Engine query live-tested
  ✔ Notification service QDR dispatch live-tested
  ✔ Ops console alert for fairness holds live-tested

SECURITY & INTEGRITY:
  ✔ QAC chain hash verification tested (tamper detection works)
  ✔ QDR immutability enforced (update attempt returns 405)
  ✔ Wildcard tier approval authority enforced (API rejects wrong role)
  ✔ Super Admin MFA enforced for QDR override
  ✔ Tenant isolation enforced for Tier 1/2 (Dojo P11)

GOVERNANCE:
  ✔ Public transparency endpoint (QF-10) live and accessible
  ✔ Appeal workflow end-to-end tested (submit → resolve → QAC amendment)
  ✔ Fairness audit → hold → release workflow tested
  ✔ Belt version validation tested (deprecated version = SPF-04 fail)

OBSERVABILITY:
  ✔ All Prometheus metrics exporting
  ✔ Grafana dashboards + QAC integrity alert configured
  ✔ Loki logs flowing (PII-safe)
  ✔ Jaeger traces active with gate span annotations

Until ALL conditions above are met:
  Agent Status = "ARTIFACTS GENERATED — NOT DEPLOYED"
  NOT: "Qualification Filter Operational"
```

---

## SECTION U — FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║  AGENT SEAL   : ANTIGRAVITY-QFM-AGENT-v1.0                                      ║
║  FILE         : Championship_ML_6_Qualification_Filter_Model_Agent.md            ║
║  DOMAIN       : SKILL & COMPETITION CORE → ANTIGRAVITY                          ║
║  PARENT       : ECOSKILLER MASTER EXECUTION PROMPT v12.0+                       ║
║                 DOJO SAAS LOCKED & SEALED SPEC v1.0                             ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  COMPLIANCE MAP:                                                                  ║
║  ✔ R2   Domain Data Models (9 tables, all indexes defined)                       ║
║  ✔ R3   OpenAPI 3.1 Contract (Section I — 13 endpoints)                          ║
║  ✔ R4   AsyncAPI Event Schema (Section J — 8 event channels)                     ║
║  ✔ R5   Workflow State Machine (pipeline modes + 9+5 stages)                     ║
║  ✔ R12  AI Model Specification (6 models, Section O)                             ║
║  ✔ R20  Accessibility & Localization Law (DOJO T12 + FDG gates)                  ║
║  ✔ R24  Execution Skill Alignment (intern guide Section N)                       ║
║  ✔ R26  Intern Line-Level Instructions (11 steps)                                ║
║  ✔ R28  Intelligence Cost Optimization (Traditional ML + LLM text only)          ║
║  ✔ R29  Modern UI Generation (Section L — 10 screens)                            ║
║  ✔ R38  Bug & Failure Registry (15 anomaly types, enforcement rules)             ║
║  ✔ R40  Internal Admin & Ops Console (7 ops screens + role matrix)               ║
║  ✔ R49  Contract Validator Compliance (Section S — 20 validation items)          ║
║  ✔ R51  Anti-Abuse (wildcard slot caps, duplicate registration prevention)       ║
║  ✔ R53  Status, Badge & Level Progression (qualification achievement badges)     ║
║  ✔ R60  Long-Term Archival (10-year QAC retention, immutable records)            ║
║  ✔ R62  Public Transparency & Trust Report (QF-10, Section F QF-BC-5)           ║
║  ✔ R79  Trust & Reputation Amplification (UCI + DMQ → recruiter signal)         ║
║  ✔ M5   AI Model Reality Law (human approval required, outcome validation)       ║
║  ✔ L2   AI Operational Restriction (no real cloud deployment claimed)            ║
║  ✔ DOJO T1   Skill Validity Framework Lock                                       ║
║  ✔ DOJO T2   Scoring Validity & Reliability Lock                                 ║
║  ✔ DOJO T5   Match Fairness Engine Lock                                          ║
║  ✔ DOJO T12  Localization & Cultural Fairness Lock                               ║
║  ✔ DOJO T13  Outcome Validation Lock                                             ║
║  ✔ DOJO T16  Appeals & Governance Board Lock                                     ║
║  ✔ DOJO T17  Belt Version Governance Lock                                        ║
║  ✔ DOJO P1   Security Hardening Lock                                             ║
║  ✔ DOJO P11  Multi-Tenant SaaS Lock                                              ║
║  ✔ DOJO P12  Support & Dispute Lock                                              ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  CONTENTS SUMMARY:                                                                ║
║  Section A  : Agent Identity, Purpose & 6-Tier Structure                        ║
║  Section B  : Qualification Decision Framework (5 gates, all thresholds locked) ║
║  Section C  : Wildcard Qualifier Rules (criteria, slots, approval hierarchy)     ║
║  Section D  : Qualification Model Architecture (6 models)                        ║
║  Section E  : Qualification Signal Registry (56 signals, 5 classes)              ║
║  Section F  : Qualification Pipeline (3 modes, 14 stages)                        ║
║  Section G  : Qualification Audit Chain (QAC — cryptographic integrity)          ║
║  Section H  : Database Schema (9 tables, full indexes)                           ║
║  Section I  : API Contract Registry (OpenAPI 3.1 — 13 endpoints)                ║
║  Section J  : Event Schema Registry (AsyncAPI — 8 channels)                      ║
║  Section K  : Microservice Architecture                                           ║
║  Section L  : UI Screens (10 screens — 3 participant + 7 ops/public)             ║
║  Section M  : Permission & Role Matrix                                            ║
║  Section N  : Intern Execution Guide (11 steps)                                  ║
║  Section O  : ML Model Specification & Governance                                 ║
║  Section P  : Observability & Monitoring                                          ║
║  Section Q  : DOJO SaaS Integration Compliance (9 DOJO sections mapped)          ║
║  Section R  : Enforcement Rules (10 rules)                                        ║
║  Section S  : Contract Gate Requirements (20 validation items)                    ║
║  Section T  : Production Readiness Checklist                                      ║
║  Section U  : Final Seal                                                          ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  STATUS    : LOCKED · SEALED · DETERMINISTIC                                     ║
║  MUTATION  : Add-only via version bump to ANTIGRAVITY-QFM-AGENT-v2.0            ║
║  INTERP.   : NONE                                                                ║
║  EXEC AUTH : Human declaration only                                              ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

*ECOSKILLER — Championship ML (6) Qualification Filter Model Agent · AntiGravity Module · v1.0 · SEALED*
