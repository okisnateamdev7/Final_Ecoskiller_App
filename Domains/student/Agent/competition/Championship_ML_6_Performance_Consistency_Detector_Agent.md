# CHAMPIONSHIP ML (6) — PERFORMANCE CONSISTENCY DETECTOR AGENT
## SKILL & COMPETITION CORE · ANTIGRAVITY MODULE

---

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║  ECOSKILLER — CHAMPIONSHIP ML (6) PERFORMANCE CONSISTENCY DETECTOR AGENT     ║
║  Domain     : SKILL & COMPETITION CORE → ANTIGRAVITY                          ║
║  Agent Code : CML6-PCD-ANTIGRAVITY                                             ║
║  Seal ID    : ANTIGRAVITY-PCD-AGENT-v1.0                                       ║
║  Status     : FINAL · LOCKED · SEALED · DETERMINISTIC                         ║
║  Mutation   : Add-only via version bump                                        ║
║  Interpretation Authority : NONE                                               ║
║  Execution Authority      : Human declaration only                             ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION A — AGENT IDENTITY & CONSTITUTIONAL PURPOSE

```
AGENT NAME    : Championship ML (6) Performance Consistency Detector Agent
AGENT CODE    : CML6-PCD-ANTIGRAVITY
AGENT TYPE    : Real-Time & Longitudinal Performance Integrity Engine
DOMAIN        : SKILL & COMPETITION CORE → ANTIGRAVITY
PARENT SYSTEM : ECOSKILLER MASTER EXECUTION PROMPT v12.0+
DOJO PARENT   : DOJO SAAS LOCKED & SEALED PRODUCTION ARTIFACT SPEC v1.0

CONSTITUTIONAL MANDATE:
  This agent is the performance integrity and longitudinal consistency
  guardian of all AntiGravity Championship participants across all 6
  competition tiers. It continuously monitors, profiles, and classifies
  participant performance trajectories to detect instability, artificial
  consistency, unearned progression, and performance manipulation —
  distinguishing genuine skill development from manufactured improvement.

  Unlike the Anti-Cheat Agent (CML6-ANTICHEAT), which focuses on
  adversarial attacks, this agent focuses on statistical legitimacy
  of performance patterns — even those that appear technically clean.

  It does NOT disqualify participants autonomously.
  It does NOT modify scores, ranks, or belts directly.
  It produces consistency profiles, confidence scores, reliability
  indices, anomaly classifications, and audit evidence — all
  human-reviewable and system-consumable by peer agents.

ANTIGRAVITY CONSISTENCY CONTEXT:
  The AntiGravity domain rewards surge, velocity, and comeback.
  These same properties create a consistency measurement paradox:
  natural talent surges are indistinguishable from manufactured ones
  without a deep longitudinal behavioral consistency baseline.
  This agent resolves that paradox by building, maintaining, and
  validating individual multi-dimensional consistency profiles for
  every participant — making the legitimate extraordinary visible
  and the fabricated ordinary detectable.

PEER AGENT INTERFACES:
  → CML6-RANK-ANTIGRAVITY      (Live Ranking Engine)
      Receives: consistency_confidence_score to weight AGCRS signal
  → CML6-ANTICHEAT-ANTIGRAVITY (Anti-Cheat Behavioral Model)
      Sends   : consistency_breach_events for correlation
      Receives: fraud_hold_status to gate consistency updates
  → DOJO Belt Engine
      Sends   : consistency_gate_result (PASS / HOLD / FAIL)
      Purpose : Belt promotion must pass consistency gate

DOJO ALIGNMENT:
  Implements DOJO SAAS Section T1  — Skill Validity Framework Lock
  Implements DOJO SAAS Section T2  — Scoring Validity & Reliability Lock
  Implements DOJO SAAS Section T3  — Rater Calibration Lock
  Implements DOJO SAAS Section T4  — Scenario Difficulty Calibration Lock
  Implements DOJO SAAS Section T6  — Learning Effectiveness Loop Lock
  Implements DOJO SAAS Section T9  — Collusion & Manipulation Detection Lock
  Implements DOJO SAAS Section P10 — AI Analytics Governance Lock
  Implements ECOSKILLER R12        — AI Model Specification
  Implements ECOSKILLER R28        — Intelligence Cost Optimization Law
  Implements ECOSKILLER R38        — Master Bug & Failure Registry Law
  Implements ECOSKILLER R40        — Internal Admin & Ops Console Law
  Implements ECOSKILLER R60        — Long-Term Archival & Data History Law
  Implements ECOSKILLER R61        — Data Network Effect Analytics Law
  Implements ECOSKILLER R79        — Trust & Reputation Amplification Law
```

---

## SECTION B — PERFORMANCE CONSISTENCY FRAMEWORK

```
The Performance Consistency Detector operates on five measurement
dimensions. Each dimension produces an independent consistency score.
The five scores are combined into a Unified Consistency Index (UCI).

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DIMENSION 1 — INTRA-SESSION CONSISTENCY (ISC)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Definition : Measures whether a participant performs coherently
               within a single session across multiple challenges.
               A participant who excels at challenge 1 then collapses
               at challenge 3 (same difficulty) shows intra-session
               inconsistency — a red flag for assisted performance
               or fatigue-masking.

  Key Metrics:
    ISC-M-01  score_variance_within_session
                Coefficient of variation of scores across all
                challenges within one session (lower = more consistent)
    ISC-M-02  difficulty_normalized_score_range
                Score range after normalizing for challenge difficulty
    ISC-M-03  performance_drift_slope_within_session
                Linear regression slope of scores across session time
                (large negative = performance collapse mid-session)
    ISC-M-04  challenge_skip_pattern
                Whether participant skips hard challenges and
                returns to easy ones within same session
    ISC-M-05  error_clustering_coefficient
                Whether errors cluster (suggesting knowledge gaps)
                or distribute randomly (suggesting genuine attempts)

  Score Range : 0.0 – 1.0 (1.0 = perfectly consistent intra-session)
  Weight in UCI: 15%

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DIMENSION 2 — INTER-SESSION CONSISTENCY (XSEC)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Definition : Measures whether performance across multiple
               sessions over time follows a statistically coherent
               trajectory — neither suspiciously flat nor
               implausibly volatile.

  Key Metrics:
    XSEC-M-01 rolling_30day_score_std_dev
                Standard deviation of scores across last 30 days
    XSEC-M-02 session_to_session_delta_distribution
                Distribution shape of score changes between sessions
                (heavy tails = inconsistency indicator)
    XSEC-M-03 performance_plateau_detection
                Periods of suspiciously zero improvement over
                extended time followed by sudden jumps
    XSEC-M-04 ability_estimate_stability_index
                IRT (Item Response Theory) ability estimate
                variance over rolling 60-day window
    XSEC-M-05 recency_weighted_consistency_score
                Consistency score with exponential decay weighting
                (recent sessions matter more than old ones)
    XSEC-M-06 weekend_vs_weekday_performance_delta
                Significant delta may indicate proxy usage on
                specific day patterns

  Score Range : 0.0 – 1.0
  Weight in UCI: 20%

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DIMENSION 3 — DIFFICULTY RESPONSE CONSISTENCY (DRC)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Definition : Measures whether a participant's performance
               responds to difficulty changes in a way consistent
               with genuine skill. A genuine performer scores lower
               on harder challenges in a predictable curve.
               A coached/scripted performer may show no difficulty
               response — scoring equally on all difficulty levels.

  Key Metrics:
    DRC-M-01  difficulty_response_curve_fit_r2
                R² of polynomial fit between challenge difficulty
                and participant score (low R² = no difficulty response)
    DRC-M-02  easy_vs_hard_score_differential
                Expected differential between difficulty tier 1–3
                vs. 7–10 challenges (too small = suspicious)
    DRC-M-03  novel_scenario_score_drop_ratio
                Performance drop on first-seen scenarios vs.
                previously encountered ones (genuine learners drop less
                as skill grows; cheaters drop more on novel content)
    DRC-M-04  adaptive_difficulty_progression_rate
                Rate at which participant earns adaptive difficulty
                upgrades relative to score improvement
    DRC-M-05  cross_domain_difficulty_consistency
                Performance consistency when same difficulty level
                appears in different intelligence domains

  Score Range : 0.0 – 1.0
  Weight in UCI: 20%

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DIMENSION 4 — LONGITUDINAL GROWTH CONSISTENCY (LGC)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Definition : Measures whether a participant's performance
               improvement over time follows plausible learning
               curves — not too fast, not artificially stagnant.
               Genuine skill development follows recognisable
               psychometric growth patterns (power law, logarithmic).
               Fabricated growth deviates from these patterns.

  Key Metrics:
    LGC-M-01  learning_curve_model_fit_score
                Goodness of fit against power-law and logarithmic
                learning curve models (higher = more natural growth)
    LGC-M-02  growth_velocity_percentile
                Growth rate relative to same-cohort peers at
                same starting ability level
    LGC-M-03  retention_check_performance_delta
                Score on repeated exposure to previously mastered
                challenges (genuine learners maintain; fabricated
                performers degrade rapidly on retention checks)
    LGC-M-04  skill_transfer_evidence_score
                Evidence that learning in one domain transfers
                to adjacent domains (genuine learning indicator)
    LGC-M-05  plateau_break_legitimacy_score
                When a plateau breaks into a surge, this measures
                whether the surge aligns with identifiable
                external learning activities (course completion,
                practice drill completion, mentor sessions)
    LGC-M-06  regression_detection_score
                Detection of performance regression followed by
                implausible recovery (may indicate test-taking
                strategy gaming rather than genuine skill change)

  Score Range : 0.0 – 1.0
  Weight in UCI: 25%

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DIMENSION 5 — MULTI-DOMAIN CONSISTENCY (MDC)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Definition : Measures whether a participant's performance
               across multiple intelligence domains (as defined
               in the ECOSKILLER Multiple Intelligence System)
               is internally coherent. A genuine learner shows
               correlated growth across related domains. An
               artificially assisted participant shows implausibly
               high performance in isolated domains without
               corresponding foundational domain development.

  Intelligence Domains Tracked:
    DOM-1  Logical-Mathematical
    DOM-2  Linguistic
    DOM-3  Spatial
    DOM-4  Interpersonal
    DOM-5  Intrapersonal
    DOM-6  Naturalistic
    DOM-7  Musical
    DOM-8  Kinesthetic
    DOM-9  Entrepreneurial
    DOM-10 AI-Collaboration

  Key Metrics:
    MDC-M-01  inter_domain_correlation_matrix_score
                Pairwise correlation of domain scores over 90 days
                against expected correlation structure for same
                cognitive ability profile
    MDC-M-02  domain_specialisation_legitimacy_score
                Whether a participant's domain specialisation
                pattern aligns with known psychometric profiles
    MDC-M-03  cross_domain_volatility_differential
                Variance in one domain far exceeding others
                of similar cognitive load (suspicious isolation)
    MDC-M-04  foundational_domain_support_score
                Whether strong advanced domain performance is
                supported by adequate foundational domain scores
                (e.g., high Entrepreneurial score requires
                adequate Logical + Interpersonal base)
    MDC-M-05  domain_improvement_synchrony_score
                Whether multiple domains improve simultaneously
                after a learning activity — genuine transfer
                signal vs. isolated score injection

  Score Range : 0.0 – 1.0
  Weight in UCI: 20%

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
UCI WEIGHT VERIFICATION:
  ISC  (15%) + XSEC (20%) + DRC (20%) + LGC (25%) + MDC (20%) = 100%
  Weight mutation requires version bump + human declaration.
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## SECTION C — UNIFIED CONSISTENCY INDEX (UCI) ALGORITHM

```
ALGORITHM: Unified Consistency Index (UCI)

Output Range   : 0.0 – 1.0
Model Class    : Weighted Ensemble of Traditional ML regressors (R28-2)
UCI Interpretation (LOCKED — v1.0):

  UCI_TIER     UCI RANGE    LABEL              ACTION
  ─────────────────────────────────────────────────────────────
  PLATINUM     0.85 – 1.00  HIGHLY CONSISTENT  Boost eligibility signal
  GOLD         0.70 – 0.84  CONSISTENT         Normal progression
  SILVER       0.55 – 0.69  MODERATE           Enhanced monitoring
  BRONZE       0.40 – 0.54  VARIABLE           Flag for review
  COPPER       0.20 – 0.39  INCONSISTENT       Hold belt progression
  FLAGGED      0.00 – 0.19  CRITICALLY LOW     Suspend competition score
                                                credits + audit trigger

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
UCI COMPUTATION FORMULA (v1.0):

  UCI = (ISC_score  × 0.15)
      + (XSEC_score × 0.20)
      + (DRC_score  × 0.20)
      + (LGC_score  × 0.25)
      + (MDC_score  × 0.20)

  Adjustments (applied after base UCI):

  ADJ-1 — MINIMUM SESSION GATE:
    IF total_scored_sessions < 10
    → UCI is PROVISIONAL
    → UCI displayed as "Insufficient Data"
    → No UCI-based gating actions applied until session threshold met

  ADJ-2 — FRAUD HOLD PROPAGATION:
    IF CML6-ANTICHEAT reports fraud_hold = TRUE for participant
    → UCI computation PAUSED
    → Last known UCI retained as FROZEN
    → UCI_status = "UNDER_REVIEW"

  ADJ-3 — ANTIGRAVITY SURGE LEGITIMACY WEIGHTING:
    IF rank_delta_24h ≥ +50 positions (AntiGravity surge event):
    → Apply surge_legitimacy_modifier:
        IF LGC_score ≥ 0.70 AND DRC_score ≥ 0.65:
            surge_legitimacy = CONFIRMED_NATURAL
            No UCI penalty applied
        IF LGC_score < 0.55 OR DRC_score < 0.50:
            surge_legitimacy = SUSPICIOUS
            UCI temporarily reduced by 0.10 pending surge audit
        IF LGC_score < 0.40 AND DRC_score < 0.40:
            surge_legitimacy = UNVERIFIED
            AntiGravity multiplier suspended
            Emit: surge_legitimacy_failed event

  ADJ-4 — BELT PROMOTION CONSISTENCY GATE:
    IF belt_promotion_pending = TRUE:
    → UCI ≥ 0.70 (GOLD or above): GATE_PASS
    → UCI 0.55 – 0.69 (SILVER): GATE_CONDITIONAL (mentor review required)
    → UCI < 0.55: GATE_HOLD (belt promotion blocked until UCI improves)
    DOJO Belt Engine receives gate result via event

  ADJ-5 — RETENTION CHECK MODIFIER:
    IF retention_check_challenge_attempted = TRUE in session:
    → LGC_score updated with LGC-M-03 (retention delta)
    → UCI recomputed immediately
    → Retention failure (score < 60% of established level):
        UCI_status = "RETENTION_CONCERN"
        Emit: retention_failure_detected event
```

---

## SECTION D — DETECTION MODEL ARCHITECTURE

```
COMPLIANCE DECLARATION:
  All models use Traditional ML only (R28-2 enforced).
  No LLM may compute consistency scores or gating decisions.
  LLMs permitted ONLY for human-readable consistency summaries.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL PC-M1 — INTRA-SESSION CONSISTENCY SCORER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Gaussian Process Regression
  Input Features : ISC-M-01 to ISC-M-05 (normalized per session)
  Output         : isc_score (float 0.0–1.0)
  Training Data  : Labeled historical sessions from verified
                   consistent performers + known inconsistency cases
  Retrain        : Weekly
  Min Performance: MAE ≤ 0.08 on held-out validation set

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL PC-M2 — INTER-SESSION TRAJECTORY MODELER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Prophet Time-Series Forecaster + Anomaly Detector
                   (Facebook Prophet — Traditional ML class)
  Input Features : XSEC-M-01 to XSEC-M-06 rolling time series
  Output         : xsec_score (float 0.0–1.0)
                   trajectory_anomaly_flag (boolean)
                   forecast_vs_actual_deviation (float)
  Training Data  : Historical score time series, 90-day windows
  Retrain        : Bi-weekly
  Min Performance: MAPE ≤ 12% on forecast vs. actual

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL PC-M3 — DIFFICULTY RESPONSE CURVE FITTER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Polynomial Regression + IRT (Item Response Theory)
                   2-Parameter Logistic Model
  Input Features : DRC-M-01 to DRC-M-05
                   Per-challenge: difficulty_level, participant_score,
                   attempt_count, time_on_challenge
  Output         : drc_score (float 0.0–1.0)
                   ability_estimate_theta (IRT theta parameter)
                   difficulty_response_class (NORMAL / FLAT / INVERTED)
  Training Data  : Item response patterns from verified participants
  Retrain        : Bi-weekly with new item calibration data
  Min Performance: IRT model fit χ² p-value > 0.05

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL PC-M4 — LONGITUDINAL GROWTH CURVE CLASSIFIER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Random Forest Classifier (Traditional ML)
                   + Power-Law / Log Curve Fitting
  Input Features : LGC-M-01 to LGC-M-06 + 90-day score series
  Output         : lgc_score (float 0.0–1.0)
                   growth_curve_class:
                     NATURAL_POWER_LAW
                     NATURAL_LOGARITHMIC
                     NATURAL_STEP         (course completion driven)
                     PLATEAU_BREAK        (audit required)
                     ARTIFICIAL_FLAT      (suspicious stagnation)
                     ARTIFICIAL_SPIKE     (fabricated surge)
                     REGRESSION_ANOMALY   (suspicious collapse-recovery)
  Training Data  : Labeled growth trajectories across 1M+ participants
  Retrain        : Weekly
  Min Performance: Weighted F1-score ≥ 0.87 across all curve classes

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL PC-M5 — MULTI-DOMAIN COHERENCE ANALYZER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Gaussian Mixture Model + Correlation Network
                   Analysis (Traditional ML)
  Input Features : MDC-M-01 to MDC-M-05
                   Per-domain scores for all 10 intelligence domains
                   over 90-day rolling window
  Output         : mdc_score (float 0.0–1.0)
                   domain_coherence_class (COHERENT / FRAGMENTED /
                     ISOLATED_SPIKE / PROFILE_MISMATCH)
                   anomalous_domain_list (which domains are outliers)
  Training Data  : Multi-domain performance profiles from verified
                   genuine learners + known isolated spike cases
  Retrain        : Bi-weekly
  Min Performance: GMM log-likelihood threshold ≥ defined baseline

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL PC-M6 — UCI ENSEMBLE COMBINER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Model Type     : Gradient Boosting Regressor (LightGBM)
  Input Features : PC-M1 through PC-M5 outputs +
                   participant_tier_level +
                   sessions_since_last_anomaly +
                   active_streak_length +
                   consecutive_consistent_sessions_count
  Output         : uci_score (float 0.0–1.0)
                   uci_tier (PLATINUM/GOLD/SILVER/BRONZE/COPPER/FLAGGED)
                   uci_confidence_interval_95 (lower, upper bounds)
  Retrain        : Weekly
  Min Performance: RMSE ≤ 0.07 on validation set

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
INFERENCE COST DECLARATION (R28 Compliance):
  PC-M1 Gaussian Process  : $0.0006 per 1,000 sessions
  PC-M2 Prophet TS        : $0.0012 per 1,000 participants (batch)
  PC-M3 IRT Fitting       : $0.0020 per 1,000 participants (batch)
  PC-M4 Random Forest     : $0.0009 per 1,000 participants
  PC-M5 GMM Analysis      : $0.0015 per 1,000 participants (batch)
  PC-M6 LightGBM          : $0.0008 per 1,000 participants
  TOTAL MONTHLY ESTIMATE  : ~$40–$70 at 1M active participants
```

---

## SECTION E — CONSISTENCY ANOMALY TAXONOMY

```
The following anomaly classes are actively detected and classified.
Each anomaly class triggers specific downstream actions.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ANOMALY CLASS A — SURGE ANOMALIES (AntiGravity-Specific)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CA-A-01 | UNGROUNDED SURGE
    Definition : Large positive rank delta with no corresponding
                 improvement in LGC scores or verifiable
                 learning activity.
    LGC Class  : ARTIFICIAL_SPIKE
    Severity   : HIGH
    Action     : surge_legitimacy = UNVERIFIED
                 AntiGravity multiplier suspended pending audit

  CA-A-02 | DOMAIN-ISOLATED SURGE
    Definition : Score surge confined to a single intelligence
                 domain with no correlated movement in related
                 domains — inconsistent with genuine learning.
    MDC Class  : ISOLATED_SPIKE
    Severity   : HIGH
    Action     : Emit: domain_isolated_surge_detected
                 Flag for multi-domain coherence review

  CA-A-03 | CYCLICALLY TIMED SURGE
    Definition : Surge events that occur at statistically regular
                 intervals suggesting engineered timing rather
                 than organic performance peaks.
    XSEC Class : Trajectory anomaly with periodic signature
    Severity   : HIGH
    Action     : Forward to CML6-ANTICHEAT for farming correlation

  CA-A-04 | SURGE WITHOUT DIFFICULTY RESPONSE
    Definition : Participant achieves surge-level scores on
                 high-difficulty challenges without showing
                 expected performance gradient.
    DRC Class  : FLAT or INVERTED
    Severity   : CRITICAL
    Action     : Immediate UCI reduction + ops alert

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ANOMALY CLASS B — PLATEAU ANOMALIES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CA-B-01 | ARTIFICIAL PLATEAU
    Definition : Extended period of perfectly stable scores
                 with near-zero variance — statistically
                 improbable for genuine human performance.
    XSEC Class : ARTIFICIAL_FLAT
    Severity   : MEDIUM
    Action     : SILVER or below UCI assignment
                 Enhanced monitoring flag

  CA-B-02 | STRATEGIC PLATEAU BREAK
    Definition : After a plateau, a sudden break that
                 correlates with no identifiable learning
                 intervention but coincides with a competition
                 tier change or prize milestone.
    LGC Class  : PLATEAU_BREAK (unsubstantiated)
    Severity   : HIGH
    Action     : Audit trigger for LGC-M-05 verification

  CA-B-03 | STAIRCASE PLATEAU
    Definition : Performance that advances in discrete equal
                 steps rather than organic curves — suggesting
                 programmatic score increments.
    LGC Fit    : Low R² on both power-law and log curves
    Severity   : HIGH
    Action     : Emit: staircase_pattern_detected
                 Forward to CML6-ANTICHEAT

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ANOMALY CLASS C — REGRESSION ANOMALIES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CA-C-01 | IMPLAUSIBLE RECOVERY
    Definition : Performance collapses significantly then
                 recovers to above-previous-peak within
                 an implausibly short window for genuine
                 skill rebuilding.
    LGC Class  : REGRESSION_ANOMALY
    Severity   : HIGH
    Action     : Emit: implausible_recovery_detected
                 Check for AntiGravity multiplier farming

  CA-C-02 | SELECTIVE REGRESSION
    Definition : Performance regresses only in domains or
                 challenge types that are low-stakes for
                 current competition tier — strategic
                 sandbagging.
    MDC Class  : FRAGMENTED
    Severity   : MEDIUM
    Action     : Note in consistency profile
                 Forward to Anti-Cheat for floor-suppression check

  CA-C-03 | RETENTION FAILURE CASCADE
    Definition : Systematic failure on retention checks across
                 multiple previously mastered skill domains —
                 indicating skills were never genuinely acquired.
    LGC-M-03   : Retention delta strongly negative
    Severity   : HIGH
    Action     : UCI_status = RETENTION_CONCERN
                 Belt promotion gate HOLD

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ANOMALY CLASS D — MULTI-DOMAIN ANOMALIES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CA-D-01 | PROFILE MISMATCH
    Definition : A participant's multi-domain performance profile
                 does not match any known cognitive ability cluster
                 from the psychometric reference population.
    MDC Class  : PROFILE_MISMATCH
    Severity   : MEDIUM
    Action     : Flag for human review
                 Intelligence measurement review recommended

  CA-D-02 | FOUNDATIONAL GAP
    Definition : High advanced domain scores without adequate
                 foundational domain support — impossible in
                 genuine skill development.
    MDC-M-04   : foundational_domain_support_score < 0.40
    Severity   : HIGH
    Action     : Emit: foundational_gap_detected
                 Block advanced belt claim for affected domain

  CA-D-03 | SYNCHRONOUS MULTI-DOMAIN SPIKE
    Definition : Multiple unrelated domains all spike simultaneously
                 — correlated improvement that exceeds expected
                 general-ability lift and suggests data injection.
    MDC-M-05   : domain_improvement_synchrony_score anomalously high
    Severity   : CRITICAL
    Action     : Immediate FLAGGED UCI tier
                 Forward to CML6-ANTICHEAT

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ANOMALY CLASS E — CALIBRATION ANOMALIES (DOJO T2/T3)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CA-E-01 | RATER INFLATION SIGNATURE
    Definition : Participant's scores are consistently higher
                 when scored by a specific subset of mentors —
                 beyond calibration tolerance.
    Source     : BS-S-07, BS-S-08 from CML6-ANTICHEAT
    DOJO T3    : Mentor calibration violation
    Severity   : HIGH
    Action     : Flag mentor + participant pair
                 Emit: rater_inflation_detected

  CA-E-02 | SCENARIO EXPLOITATION SIGNATURE
    Definition : Participant consistently outperforms on specific
                 scenario IDs beyond what difficulty calibration
                 predicts — memorisation or advance access.
    DRC-M-03   : Novel scenario drop far exceeds familiar drop
    Severity   : HIGH
    Action     : Emit: scenario_exploitation_detected
                 Flag specific scenario IDs for review (DOJO T4)

  CA-E-03 | INCONSISTENT SELF-ASSESSMENT
    Definition : Systematic divergence between self-assessment
                 scores and peer/mentor scores beyond normal
                 calibration range.
    DOJO T2    : Inter-rater reliability violation
    Severity   : MEDIUM
    Action     : Flag for self-assessment calibration review

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
TOTAL ANOMALY CLASSES : 5
TOTAL ANOMALY TYPES   : 15
CRITICAL SEVERITY     : 3
HIGH SEVERITY         : 9
MEDIUM SEVERITY       : 3
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## SECTION F — DETECTION PIPELINE SPECIFICATION

```
PIPELINE EXECUTION MODES:

  MODE RT  — REAL-TIME (per session completion event)
    Trigger  : Session ends + scores submitted
    Latency  : < 500ms end-to-end
    Models   : PC-M1, PC-M6 (session-level update)
    Output   : Updated ISC score + UCI recomputed

  MODE H1  — HOURLY BATCH (rolling time-series update)
    Trigger  : Cron — every 60 minutes
    Models   : PC-M2 (trajectory), PC-M4 (growth curve)
    Output   : Updated XSEC, LGC scores + trajectory anomaly flags

  MODE H6  — 6-HOURLY BATCH (domain coherence + difficulty response)
    Trigger  : Cron — every 6 hours
    Models   : PC-M3 (IRT), PC-M5 (GMM multi-domain)
    Output   : Updated DRC, MDC scores

  MODE EVT — EVENT-DRIVEN (triggered by specific conditions)
    Trigger  : AntiGravity surge event, belt promotion request,
               retention check completion, anomaly cross-signal
    Models   : All PC-M1 through PC-M6 (full recompute)
    Output   : Full UCI refresh + surge legitimacy classification

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PIPELINE STAGES (MODE RT — REAL-TIME):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  STAGE PCD-1 — SESSION RESULT INGESTION
    Source   : Session completion event from match/challenge service
    Action   : Validate schema → persist to SESSION_PERFORMANCE_LOG
    Failure  : DROP + DEAD_LETTER_QUEUE + alert

  STAGE PCD-2 — METRIC EXTRACTION
    Action   : Compute ISC-M-01 to ISC-M-05 from session data
    Output   : Normalized ISC metric vector
    Failure  : STOP STAGE → emit: metric_extraction_failure

  STAGE PCD-3 — FRAUD HOLD CHECK
    Action   : Query CML6-ANTICHEAT for fraud_hold status
    IF fraud_hold = TRUE → FREEZE UCI → skip to STAGE PCD-8
    IF fraud_hold = FALSE → continue

  STAGE PCD-4 — PC-M1 INFERENCE (ISC Scoring)
    Action   : Run Gaussian Process Regression for ISC score
    Output   : isc_score (float 0.0–1.0)
    Timeout  : 200ms hard limit

  STAGE PCD-5 — MINIMUM SESSION GATE CHECK
    IF total_scored_sessions < 10 → UCI = PROVISIONAL → skip to PCD-8
    ELSE → continue to PCD-6

  STAGE PCD-6 — PC-M6 UCI RECOMPUTE
    Input    : isc_score (new) + cached XSEC, DRC, LGC, MDC scores
    Output   : Updated uci_score + uci_tier + confidence_interval
    Action   : Persist to PARTICIPANT_CONSISTENCY_PROFILE table

  STAGE PCD-7 — ANOMALY PATTERN CHECK
    Action   : Apply anomaly taxonomy rules (Section E)
    Output   : List of triggered anomaly classes
    IF anomaly detected → emit: consistency_anomaly_detected event

  STAGE PCD-8 — DOWNSTREAM BROADCAST
    Outputs:
      → Ranking Engine (CML6-RANK):
          POST consistency_confidence_score for AGCRS weighting
      → If UCI_TIER = FLAGGED:
          Emit: uci_flagged event → ops console alert (R40)
      → If belt_promotion_pending:
          Emit: belt_gate_result (PASS / CONDITIONAL / HOLD)
      → Update PARTICIPANT_CONSISTENCY_PROFILE

  STAGE PCD-9 — CONSISTENCY REPORT GENERATION
    On UCI_TIER change (any direction):
    → Generate human-readable consistency summary (LLM — R28-3)
    → Store to CONSISTENCY_EXPLAINABILITY_LOG
    → Available via API for participant self-view (masked detail)
```

---

## SECTION G — CONSISTENCY SIGNAL REGISTRY

```
All raw signals collected per session and per participant.
These feed the ML models (Section D) and anomaly rules (Section E).

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
SESSION-LEVEL SIGNALS (collected per session completion):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CS-SES-01  session_score_mean            Mean score across all challenges
  CS-SES-02  session_score_std_dev         Std dev of scores within session
  CS-SES-03  session_score_min             Minimum score in session
  CS-SES-04  session_score_max             Maximum score in session
  CS-SES-05  session_score_cv              Coefficient of variation (std/mean)
  CS-SES-06  performance_drift_slope       Linear slope of scores over session
  CS-SES-07  challenge_difficulty_mean     Mean difficulty of challenges faced
  CS-SES-08  difficulty_normalized_score   Score adjusted for difficulty level
  CS-SES-09  error_clustering_coeff        Spatial clustering of errors
  CS-SES-10  skip_return_pattern_flag      Boolean: skipped then returned
  CS-SES-11  total_challenges_attempted    Count of challenges in session
  CS-SES-12  session_duration_minutes      Total session duration
  CS-SES-13  focus_time_ratio              Active engagement / total time
  CS-SES-14  novel_challenge_count         Challenges seen for first time
  CS-SES-15  retention_check_count         Challenges from past mastered pool

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
LONGITUDINAL SIGNALS (rolling windows, updated per session):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CS-LNG-01  rolling_7d_score_mean         7-day rolling mean score
  CS-LNG-02  rolling_30d_score_mean        30-day rolling mean score
  CS-LNG-03  rolling_30d_score_std_dev     30-day rolling std dev
  CS-LNG-04  rolling_90d_score_mean        90-day rolling mean score
  CS-LNG-05  rolling_90d_score_std_dev     90-day rolling std dev
  CS-LNG-06  score_trend_7d_slope          Linear slope over last 7 days
  CS-LNG-07  score_trend_30d_slope         Linear slope over last 30 days
  CS-LNG-08  score_trend_90d_slope         Linear slope over last 90 days
  CS-LNG-09  ability_estimate_theta_7d     IRT theta estimate over 7 days
  CS-LNG-10  ability_estimate_theta_30d    IRT theta estimate over 30 days
  CS-LNG-11  ability_estimate_theta_90d    IRT theta estimate over 90 days
  CS-LNG-12  ability_estimate_stability    Variance of theta over 90 days
  CS-LNG-13  consecutive_consistent_sess   Count of sessions without anomaly
  CS-LNG-14  consecutive_anomaly_sessions  Count of consecutive anomaly sessions
  CS-LNG-15  learning_curve_r2_power       R² of power-law fit (90-day)
  CS-LNG-16  learning_curve_r2_log         R² of logarithmic fit (90-day)
  CS-LNG-17  plateau_days_count            Days since last meaningful improvement
  CS-LNG-18  retention_check_pass_rate     % of retention checks passed (90-day)
  CS-LNG-19  weekend_score_delta           Weekend vs weekday performance gap

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DOMAIN-LEVEL SIGNALS (per intelligence domain, per 30-day window):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  CS-DOM-01  domain_score_mean_{dom}       Per-domain mean score
  CS-DOM-02  domain_score_trend_{dom}      Per-domain score slope (30d)
  CS-DOM-03  domain_vs_profile_delta_{dom} Deviation from participant's
                                           own expected domain profile
  CS-DOM-04  inter_domain_correlation_mat  10×10 pairwise correlation matrix
  CS-DOM-05  foundational_support_ratio    Advanced/foundational domain ratio
  CS-DOM-06  domain_synchrony_score        Multi-domain simultaneous change

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
TOTAL CONSISTENCY SIGNALS: 46 (15 session + 19 longitudinal + 12 domain)
All signals stored in: CONSISTENCY_SIGNAL_LOG (Section H schema)
All signals anonymized: participant_id only — no PII in features
```

---

## SECTION H — DATABASE SCHEMA

```sql
-- ════════════════════════════════════════════════════════════════
-- PERFORMANCE CONSISTENCY DETECTOR — COMPLETE DATABASE SCHEMA
-- ════════════════════════════════════════════════════════════════

-- Raw session performance data
TABLE session_performance_log (
  session_id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  tier_level           INT         NOT NULL CHECK (tier_level BETWEEN 1 AND 6),
  session_score_mean   DECIMAL(10,4) NOT NULL,
  session_score_std    DECIMAL(10,4) NOT NULL,
  session_score_min    DECIMAL(10,4) NOT NULL,
  session_score_max    DECIMAL(10,4) NOT NULL,
  session_score_cv     DECIMAL(10,6) NOT NULL,
  drift_slope          DECIMAL(10,6),
  difficulty_mean      DECIMAL(6,3) NOT NULL,
  diff_normalized_score DECIMAL(10,4),
  error_clustering_coeff DECIMAL(6,4),
  skip_return_flag     BOOLEAN     DEFAULT FALSE,
  total_challenges     INT         NOT NULL,
  novel_challenge_count INT        DEFAULT 0,
  retention_check_count INT        DEFAULT 0,
  session_duration_min DECIMAL(8,2),
  focus_time_ratio     DECIMAL(6,4),
  completed_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Participant consistency profile (master record)
TABLE participant_consistency_profile (
  participant_id         UUID        PRIMARY KEY REFERENCES users(id),
  championship_id        UUID        NOT NULL,
  tier_level             INT         NOT NULL,
  -- Dimension scores
  isc_score              DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  xsec_score             DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  drc_score              DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  lgc_score              DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  mdc_score              DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  -- UCI
  uci_score              DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  uci_tier               TEXT        NOT NULL DEFAULT 'PROVISIONAL',
                                     -- PLATINUM/GOLD/SILVER/BRONZE/COPPER/FLAGGED/PROVISIONAL
  uci_confidence_lower   DECIMAL(6,4),
  uci_confidence_upper   DECIMAL(6,4),
  uci_status             TEXT        NOT NULL DEFAULT 'ACTIVE',
                                     -- ACTIVE/PROVISIONAL/FROZEN/UNDER_REVIEW/RETENTION_CONCERN
  -- Growth curve
  growth_curve_class     TEXT,       -- NATURAL_POWER_LAW/LOGARITHMIC/STEP/PLATEAU_BREAK/etc.
  -- Surge legitimacy (AntiGravity)
  surge_legitimacy       TEXT,       -- CONFIRMED_NATURAL/SUSPICIOUS/UNVERIFIED/NULL
  -- IRT
  ability_theta_current  DECIMAL(8,4),
  ability_theta_30d_std  DECIMAL(8,4),
  -- Belt gate
  belt_gate_result       TEXT,       -- PASS/CONDITIONAL/HOLD/NULL
  belt_gate_evaluated_at TIMESTAMPTZ,
  -- Meta
  total_scored_sessions  INT         NOT NULL DEFAULT 0,
  consecutive_consistent INT         NOT NULL DEFAULT 0,
  active_anomaly_count   INT         NOT NULL DEFAULT 0,
  last_computed_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_at             TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Longitudinal signal rolling cache (updated per session)
TABLE consistency_signal_log (
  log_id               UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  signal_code          TEXT        NOT NULL,  -- CS-SES-XX / CS-LNG-XX / CS-DOM-XX
  signal_value         DECIMAL(18,6) NOT NULL,
  window_days          INT,                   -- 7 / 30 / 90 / NULL for session
  domain_code          TEXT,                  -- DOM-1 to DOM-10 if applicable
  computed_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Anomaly detection events
TABLE consistency_anomaly_log (
  anomaly_id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  anomaly_class        TEXT        NOT NULL,  -- CA-A-XX / CA-B-XX / etc.
  anomaly_description  TEXT        NOT NULL,
  severity             TEXT        NOT NULL,  -- CRITICAL / HIGH / MEDIUM
  triggering_signals   JSONB       NOT NULL,
  uci_at_detection     DECIMAL(6,4) NOT NULL,
  detected_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  forwarded_to_anticheat BOOLEAN   DEFAULT FALSE,
  resolved_at          TIMESTAMPTZ,
  resolution           TEXT        -- CLEARED / CONFIRMED / ESCALATED
);

-- Surge legitimacy audit trail
TABLE surge_legitimacy_log (
  surge_id             UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  rank_delta_24h       INT         NOT NULL,
  lgc_score_at_surge   DECIMAL(6,4) NOT NULL,
  drc_score_at_surge   DECIMAL(6,4) NOT NULL,
  surge_legitimacy     TEXT        NOT NULL,  -- CONFIRMED_NATURAL/SUSPICIOUS/UNVERIFIED
  uci_modifier_applied DECIMAL(6,4) DEFAULT 0.0,
  multiplier_suspended BOOLEAN     DEFAULT FALSE,
  detected_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  reviewed_by          UUID        REFERENCES users(id),
  review_outcome       TEXT
);

-- Belt gate decisions
TABLE belt_gate_decisions (
  gate_id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  belt_level_requested TEXT        NOT NULL,
  uci_score_at_gate    DECIMAL(6,4) NOT NULL,
  uci_tier_at_gate     TEXT        NOT NULL,
  gate_result          TEXT        NOT NULL,  -- PASS / CONDITIONAL / HOLD
  conditional_reason   TEXT,       -- If CONDITIONAL: what mentor must verify
  hold_reason          TEXT,       -- If HOLD: what must improve
  evaluated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  mentor_review_by     UUID        REFERENCES users(id),
  mentor_decision      TEXT,       -- APPROVED / DEFERRED
  finalized_at         TIMESTAMPTZ
);

-- IRT ability estimates history
TABLE irt_ability_estimates (
  estimate_id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  ability_theta        DECIMAL(8,4) NOT NULL,
  standard_error       DECIMAL(8,4) NOT NULL,
  window_days          INT         NOT NULL,  -- 7 / 30 / 90
  challenge_count      INT         NOT NULL,
  estimated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Consistency explainability
TABLE consistency_explainability_log (
  explain_id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id       UUID        NOT NULL REFERENCES users(id),
  championship_id      UUID        NOT NULL,
  uci_score            DECIMAL(6,4) NOT NULL,
  uci_tier             TEXT        NOT NULL,
  dimension_breakdown  JSONB       NOT NULL,  -- ISC/XSEC/DRC/LGC/MDC scores
  top_signals          JSONB       NOT NULL,  -- Top contributing signals
  anomalies_active     TEXT[]      NOT NULL,  -- Active anomaly class codes
  human_summary        TEXT,                  -- LLM generated (R28-3)
  generated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Model performance audit
TABLE pcd_model_performance_audit (
  audit_id             UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  model_code           TEXT        NOT NULL,  -- PC-M1 to PC-M6
  model_version        TEXT        NOT NULL,
  evaluation_metric    TEXT        NOT NULL,
  metric_value         DECIMAL(10,6) NOT NULL,
  threshold_passed     BOOLEAN     NOT NULL,
  fairness_pass        BOOLEAN     NOT NULL,
  evaluated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  evaluated_by         UUID        REFERENCES users(id),
  deployment_approved  BOOLEAN     DEFAULT FALSE,
  approved_by          UUID        REFERENCES users(id),
  approved_at          TIMESTAMPTZ
);

-- ═══════════════ INDEXES ═══════════════
CREATE INDEX idx_pcp_uci_tier           ON participant_consistency_profile (uci_tier);
CREATE INDEX idx_pcp_championship       ON participant_consistency_profile (championship_id);
CREATE INDEX idx_pcp_belt_gate          ON participant_consistency_profile (belt_gate_result);
CREATE INDEX idx_session_log_participant ON session_performance_log (participant_id, completed_at);
CREATE INDEX idx_anomaly_participant    ON consistency_anomaly_log (participant_id, detected_at);
CREATE INDEX idx_anomaly_class          ON consistency_anomaly_log (anomaly_class, severity);
CREATE INDEX idx_surge_participant      ON surge_legitimacy_log (participant_id, detected_at);
CREATE INDEX idx_belt_gate_participant  ON belt_gate_decisions (participant_id, evaluated_at);
CREATE INDEX idx_signal_log_code        ON consistency_signal_log (signal_code, computed_at);
CREATE INDEX idx_irt_participant_window ON irt_ability_estimates (participant_id, window_days);
```

---

## SECTION I — API CONTRACT REGISTRY

```yaml
openapi: 3.1.0
info:
  title: Championship ML (6) Performance Consistency Detector API — AntiGravity
  version: 1.0.0

paths:

  /consistency/antigravity/profile/{participant_id}:
    get:
      summary: Get full consistency profile for participant
      security: [{bearerAuth: []}]
      parameters:
        - name: participant_id
          in: path
          required: true
          schema: {type: string, format: uuid}
        - name: championship_id
          in: query
          required: true
          schema: {type: string, format: uuid}
      responses:
        "200":
          description: Full consistency profile with all dimension scores
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ConsistencyProfile'
        "403": {description: Insufficient permissions}
        "404": {description: Profile not found}

  /consistency/antigravity/profile/{participant_id}/summary:
    get:
      summary: Get participant-visible consistency summary (own only)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: UCI tier + human-readable summary (no raw signals)

  /consistency/antigravity/session/ingest:
    post:
      summary: Ingest session completion result (internal service only)
      security: [{internalServiceAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SessionCompletionPayload'
      responses:
        "202": {description: Session accepted and queued}
        "400": {description: Schema validation failure}

  /consistency/antigravity/surge/evaluate:
    post:
      summary: Evaluate surge legitimacy for AntiGravity event (internal)
      security: [{internalServiceAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [participant_id, championship_id, rank_delta_24h]
              properties:
                participant_id: {type: string, format: uuid}
                championship_id: {type: string, format: uuid}
                rank_delta_24h: {type: integer}
      responses:
        "200":
          description: Surge legitimacy classification
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/SurgeLegitimacyResult'

  /consistency/antigravity/belt-gate/evaluate:
    post:
      summary: Evaluate consistency gate for belt promotion (internal)
      security: [{internalServiceAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [participant_id, championship_id, belt_level_requested]
              properties:
                participant_id: {type: string, format: uuid}
                championship_id: {type: string, format: uuid}
                belt_level_requested: {type: string}
      responses:
        "200":
          description: Belt gate result (PASS / CONDITIONAL / HOLD)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/BeltGateResult'

  /consistency/antigravity/anomalies/active:
    get:
      summary: List active consistency anomalies (ops admin only)
      security: [{bearerAuth: []}]
      parameters:
        - name: severity
          in: query
          schema: {type: string, enum: [CRITICAL, HIGH, MEDIUM, ALL]}
        - name: championship_id
          in: query
          schema: {type: string, format: uuid}
      responses:
        "200":
          description: Active anomaly list

  /consistency/antigravity/leaderboard/consistency:
    get:
      summary: Get consistency leaderboard (PLATINUM/GOLD tiers)
      security: [{bearerAuth: []}]
      parameters:
        - name: tier_level
          in: query
          schema: {type: integer, minimum: 1, maximum: 6}
        - name: uci_tier_filter
          in: query
          schema: {type: string, enum: [PLATINUM, GOLD]}
      responses:
        "200":
          description: Consistency leaderboard

  /consistency/antigravity/analytics/cohort:
    get:
      summary: Cohort consistency analytics (ops admin only)
      security: [{bearerAuth: []}]
      parameters:
        - name: championship_id
          in: query
          required: true
          schema: {type: string, format: uuid}
      responses:
        "200":
          description: Cohort-level UCI distribution and anomaly summary

components:
  schemas:
    ConsistencyProfile:
      type: object
      properties:
        participant_id:        {type: string, format: uuid}
        uci_score:             {type: number, format: float}
        uci_tier:              {type: string}
        isc_score:             {type: number, format: float}
        xsec_score:            {type: number, format: float}
        drc_score:             {type: number, format: float}
        lgc_score:             {type: number, format: float}
        mdc_score:             {type: number, format: float}
        growth_curve_class:    {type: string}
        uci_status:            {type: string}
        belt_gate_result:      {type: string}
        active_anomaly_count:  {type: integer}
        last_computed_at:      {type: string, format: date-time}

    SessionCompletionPayload:
      type: object
      required: [session_id, participant_id, championship_id,
                 tier_level, challenges, session_metadata]
      properties:
        session_id:       {type: string, format: uuid}
        participant_id:   {type: string, format: uuid}
        championship_id:  {type: string, format: uuid}
        tier_level:       {type: integer, minimum: 1, maximum: 6}
        challenges:
          type: array
          items:
            type: object
            properties:
              challenge_id:       {type: string, format: uuid}
              difficulty_level:   {type: integer, minimum: 1, maximum: 10}
              score:              {type: number}
              time_on_task_ms:    {type: integer}
              is_novel:           {type: boolean}
              is_retention_check: {type: boolean}
        session_metadata:
          type: object
          properties:
            duration_minutes: {type: number}
            focus_time_ratio: {type: number}

    SurgeLegitimacyResult:
      type: object
      properties:
        participant_id:      {type: string, format: uuid}
        surge_legitimacy:    {type: string,
                              enum: [CONFIRMED_NATURAL, SUSPICIOUS, UNVERIFIED]}
        lgc_score:           {type: number}
        drc_score:           {type: number}
        uci_modifier_applied:{type: number}
        multiplier_suspended:{type: boolean}
        audit_required:      {type: boolean}

    BeltGateResult:
      type: object
      properties:
        participant_id:    {type: string, format: uuid}
        gate_result:       {type: string, enum: [PASS, CONDITIONAL, HOLD]}
        uci_score:         {type: number}
        uci_tier:          {type: string}
        conditional_reason:{type: string}
        hold_reason:       {type: string}
        mentor_review_required: {type: boolean}

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
    internalServiceAuth:
      type: apiKey
      in: header
      name: X-Internal-Service-Key

security:
  - bearerAuth: []
```

---

## SECTION J — EVENT SCHEMA REGISTRY (AsyncAPI)

```yaml
asyncapi: 2.6.0
info:
  title: AntiGravity Performance Consistency Detector Events
  version: 1.0.0

channels:

  consistency.uci.updated:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            championship_id:  {type: string}
            uci_score:        {type: number}
            uci_tier:         {type: string}
            uci_tier_previous:{type: string}
            updated_at:       {type: string, format: date-time}

  consistency.anomaly.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            championship_id:  {type: string}
            anomaly_class:    {type: string}
            severity:         {type: string}
            uci_at_detection: {type: number}
            detected_at:      {type: string, format: date-time}

  consistency.surge.legitimacy.evaluated:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:      {type: string}
            surge_legitimacy:    {type: string}
            multiplier_suspended:{type: boolean}
            evaluated_at:        {type: string, format: date-time}

  consistency.belt.gate.result:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:    {type: string}
            belt_level:        {type: string}
            gate_result:       {type: string}
            uci_tier:          {type: string}
            evaluated_at:      {type: string, format: date-time}

  consistency.retention.failure.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            championship_id:  {type: string}
            retention_score:  {type: number}
            threshold:        {type: number}
            detected_at:      {type: string, format: date-time}

  consistency.foundational.gap.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            affected_domain:  {type: string}
            support_score:    {type: number}
            detected_at:      {type: string, format: date-time}

  consistency.uci.flagged:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:   {type: string}
            championship_id:  {type: string}
            uci_score:        {type: number}
            active_anomalies: {type: array, items: {type: string}}
            flagged_at:       {type: string, format: date-time}
```

---

## SECTION K — MICROSERVICE ARCHITECTURE

```
SERVICE: consistency-detector-service
  Language   : Python 3.11 (R1 compliant)
  Framework  : FastAPI
  Port       : 8032
  Stack      : Redis 7, PostgreSQL 15, scikit-learn, LightGBM,
               Prophet (Meta), PyTorch (LSTM/GMM components)

RESPONSIBILITIES:
  1. Ingest session completion events
  2. Extract and store 46 consistency signals
  3. Run PC-M1 through PC-M6 in respective modes (RT/H1/H6/EVT)
  4. Compute and maintain UCI per participant
  5. Detect and classify all 15 anomaly types (Section E)
  6. Evaluate surge legitimacy for AntiGravity events
  7. Evaluate belt gate for DOJO belt promotion requests
  8. Broadcast consistency events (Section J)
  9. Expose REST API (Section I)
  10. Provide consistency confidence score to CML6-RANK

INTERNAL INTERFACES:
  → anticheat-behavioral-service:8031
      GET  /anticheat/antigravity/risk/{id}  (fraud_hold check)
      POST /anticheat/events/ingest          (forward anomalies)
  → ranking-engine-service:8030
      POST /rankings/antigravity/consistency-score (UCI → AGCRS weight)
  → dojo-belt-engine-service (port TBD per DOJO deployment)
      POST /belt/gate/result                 (belt gate decisions)
  → notification-service:8010
      POST /notify                           (ops alerts on FLAGGED)
  → ops-console-service:9000
      POST /ops/alerts                       (anomaly queue push)

HEALTH CHECK:
  GET /health
  Returns: {status, models_loaded, last_batch_run, queue_depth}

DOCKER BUILD:
  File: /backend/services/consistency_detector_service/Dockerfile
  Base: python:3.11-slim
  CMD : uvicorn main:app --host 0.0.0.0 --port 8032

KUBERNETES:
  Name        : consistency-detector-service
  Internal URL: http://consistency-detector-service:8032
  Namespace   : ecoskiller-{env}
  Replicas    : Min 2, Max 8 (HPA on CPU > 65%)

CRON JOBS (in-pod schedulers):
  H1 Batch : Every 60 minutes → runs PC-M2, PC-M4
  H6 Batch : Every 6 hours   → runs PC-M3, PC-M5
```

---

## SECTION L — UI SCREENS SPECIFICATION

```
SCREEN PCD-01: PARTICIPANT — MY CONSISTENCY DASHBOARD
  Route   : /app/championship/antigravity/my-consistency
  Access  : Authenticated participant (own data only)
  Purpose : Transparent, motivating consistency display
  Components:
    - UCI tier badge (PLATINUM/GOLD/SILVER/BRONZE/COPPER)
      with colour-coded ring and tier label
    - UCI score gauge (0.0–1.0 dial, no raw numbers shown)
    - "Your Consistency Story" — LLM-generated human summary
    - Five dimension score bars:
        Intra-Session Consistency
        Session-to-Session Stability
        Difficulty Response
        Growth Pattern
        Multi-Domain Coherence
      (each shown as a labelled progress bar, no raw floats)
    - Growth Curve type badge (NATURAL / DEVELOPING / NEEDS WORK)
    - Retention Health indicator (STRONG / MONITOR / AT RISK)
    - "What can I improve?" recommendations section
    - Belt Gate Status chip (if promotion pending):
        PASS ✓ / MENTOR REVIEW NEEDED / IMPROVING REQUIRED
    - 90-day UCI trend sparkline
  Notes   : No anomaly class codes shown to participant.
             No raw signal values shown.
             Positive framing: this is a coaching tool, not a court.

SCREEN PCD-02: PARTICIPANT — CONSISTENCY GROWTH TIMELINE
  Route   : /app/championship/antigravity/my-consistency/timeline
  Access  : Authenticated participant (own data only)
  Components:
    - 90-day UCI score line chart
    - Milestone markers: belt gates, surge events, course completions
    - Dimension score breakdown per week (stacked area chart)
    - "Key moments" annotations (auto-generated from growth events)
    - Learning activity correlation overlay (if consented)

SCREEN PCD-03: OPS ADMIN — CONSISTENCY ANOMALY DASHBOARD
  Route   : /ops/championship/antigravity/consistency/anomalies
  Access  : Ops Admin + Super Admin (MFA enforced — R40)
  Purpose : Monitor and action consistency integrity issues
  Components:
    - Anomaly class filter tabs
      (ALL / SURGE ANOMALIES / PLATEAU / REGRESSION / DOMAIN / CALIBRATION)
    - Severity filter (CRITICAL / HIGH / MEDIUM)
    - Participant anomaly cards:
        participant_id (masked display)
        uci_tier + uci_score
        active anomaly class list
        days active
        "Send to Anti-Cheat" action (forwards to CML6-ANTICHEAT)
        "View Full Profile" action → opens PCD-04
    - Real-time count: Active CRITICAL / HIGH / MEDIUM anomalies
    - Cohort UCI distribution pie chart

SCREEN PCD-04: OPS ADMIN — FULL CONSISTENCY PROFILE VIEWER
  Route   : /ops/championship/antigravity/consistency/profile/{id}
  Access  : Ops Admin + Super Admin only
  Purpose : Deep-dive individual participant consistency review
  Components:
    - UCI score + tier + confidence interval
    - All 5 dimension scores with mini-charts
    - Growth curve classification + curve fit visualization
    - IRT ability theta trend (7d / 30d / 90d)
    - Active anomaly list with triggering signal values
    - Surge legitimacy history (all past surge events)
    - Belt gate decision history
    - Retention check performance table
    - Domain coherence heatmap (10×10 correlation matrix)
    - Session performance table (last 20 sessions)
    - Human-readable consistency summary (LLM — R28-3)
    - "Escalate to Anti-Cheat" action button
    - "Flag for Manual Review" action button
    - Full audit log of all actions on this profile

SCREEN PCD-05: OPS ADMIN — SURGE LEGITIMACY REVIEW QUEUE
  Route   : /ops/championship/antigravity/consistency/surges
  Access  : Ops Admin only
  Purpose : Review AntiGravity surge events requiring legitimacy audit
  Components:
    - Pending surge review list:
        participant_id, rank_delta, surge_legitimacy classification,
        LGC score, DRC score, multiplier suspended flag
    - "View Evidence" → opens surge detail modal
    - "Clear — Natural Surge" action
    - "Escalate to Anti-Cheat" action
    - Surge legitimacy history (rolling 90-day)

SCREEN PCD-06: OPS ADMIN — BELT GATE REVIEW PANEL
  Route   : /ops/championship/antigravity/consistency/belt-gates
  Access  : Ops Admin + Mentor (belt-gate-specific permission)
  Purpose : Review CONDITIONAL belt gate cases requiring mentor sign-off
  Components:
    - CONDITIONAL gate queue:
        participant_id, belt_level requested, uci_tier, uci_score,
        conditional_reason, days waiting
    - Link to full consistency profile (PCD-04)
    - Mentor "Approve" / "Defer" decision with mandatory notes
    - Decision audit log (immutable)

SCREEN PCD-07: ANALYTICS — COHORT CONSISTENCY DASHBOARD
  Route   : /ops/championship/antigravity/consistency/analytics
  Access  : Ops Admin only
  Purpose : Platform-level consistency health monitoring (R61)
  Components:
    - UCI tier distribution by championship tier (stacked bar)
    - Anomaly class frequency trend (30-day rolling)
    - Growth curve class distribution (pie)
    - Average IRT theta by tier level (line chart)
    - Top 10 most improved participants (by LGC score delta)
    - Retention check pass rate trend
    - Belt gate pass/hold/conditional ratios
    - Export to CSV
```

---

## SECTION M — PERMISSION & ROLE MATRIX

```
ROLE              │OWN UCI  │OWN       │ALL       │FULL      │RESOLVE   │BELT GATE
                  │SUMMARY  │TIMELINE  │ANOMALIES │PROFILES  │ANOMALIES │REVIEW
──────────────────┼─────────┼──────────┼──────────┼──────────┼──────────┼─────────
participant       │YES      │YES       │NO        │NO        │NO        │NO
recruiter         │NO       │NO        │NO        │YES*      │NO        │NO
mentor            │NO       │NO        │NO        │YES*      │NO        │YES
institute_admin   │NO       │NO        │own inst  │own inst  │NO        │NO
ops_admin         │YES      │YES       │YES       │YES       │YES       │YES
super_admin       │YES      │YES       │YES       │YES       │YES       │YES
internal_service  │ingest   │ingest    │NO        │NO        │NO        │NO

*recruiter and mentor: view UCI tier + growth class ONLY (no raw scores,
 no signal values, no anomaly codes). UCI summary card only.

NOTE: internal_service has ONLY session ingest + surge/belt gate query rights.
      All other reads and writes require bearerAuth with appropriate role.
```

---

## SECTION N — INTERN EXECUTION GUIDE

```
OBJECTIVE: Run consistency detector service locally.
INTERN ROLE: Python Backend Developer
SKILL LEVEL: Intermediate Python + basic ML familiarity

REQUIRED TOOLS:
  Python 3.11+, PostgreSQL 15, Redis 7, Docker

STEP-BY-STEP:

Step 1 — Navigate to service:
  cd /backend/services/consistency_detector_service/

Step 2 — Install dependencies:
  pip install -r requirements.txt
  (Includes: fastapi, uvicorn, lightgbm, scikit-learn,
   prophet, alembic, redis, asyncpg, torch, scipy)

Step 3 — Configure environment:
  cp .env.example .env
  Fill in:
    DB_HOST, REDIS_HOST
    ANTICHEAT_SERVICE_URL=http://localhost:8031
    RANKING_ENGINE_URL=http://localhost:8030
    MODEL_PATH=/models/consistency/

Step 4 — Load dev ML models:
  python scripts/load_models.py --env dev
  Expected output (one line per model):
    "PC-M1 loaded: gp_isc_scorer_v1.joblib"
    "PC-M2 loaded: prophet_trajectory_v1.pkl"
    "PC-M3 loaded: irt_drc_fitter_v1.joblib"
    "PC-M4 loaded: rf_growth_classifier_v1.joblib"
    "PC-M5 loaded: gmm_mdc_analyzer_v1.joblib"
    "PC-M6 loaded: lgbm_uci_combiner_v1.joblib"

Step 5 — Run DB migrations:
  alembic upgrade head
  Expected: All consistency tables created with indexes.

Step 6 — Start service:
  uvicorn main:app --reload --port 8032
  Expected: "Uvicorn running on http://127.0.0.1:8032"

Step 7 — Verify health:
  curl http://127.0.0.1:8032/health
  Expected: {"status": "healthy", "models_loaded": 6,
             "last_batch_run": null, "queue_depth": 0}

Step 8 — Test session ingestion:
  POST http://127.0.0.1:8032/consistency/antigravity/session/ingest
  Body: (see test_payloads/session_test_01.json in repo)
  Expected: {"status": "accepted", "session_id": "..."}

Step 9 — Check profile update:
  GET http://127.0.0.1:8032/consistency/antigravity/profile/{test_participant_id}?championship_id={test_id}
  Expected: Consistency profile JSON with uci_tier = PROVISIONAL
             (< 10 sessions, so provisional — correct behavior)

SUCCESS CONDITIONS:
  ✔ Service running on port 8032
  ✔ All 6 models loaded
  ✔ DB tables created
  ✔ Health endpoint returns 200 with models_loaded = 6
  ✔ Session ingest endpoint responds 202
  ✔ Profile endpoint responds 200
  ✔ No runtime exceptions in logs

FAILURE → STOP EXECUTION
```

---

## SECTION O — ML MODEL SPECIFICATION & GOVERNANCE

```
MODEL PERFORMANCE THRESHOLDS (LOCKED — v1.0):

  PC-M1  Gaussian Process (ISC)
    Metric    : MAE ≤ 0.08 on validation set
    Below     : REJECT deployment

  PC-M2  Prophet TS (XSEC)
    Metric    : MAPE ≤ 12% forecast vs. actual
    Below     : REJECT deployment

  PC-M3  IRT 2PL (DRC)
    Metric    : Model fit χ² p-value > 0.05
    Below     : REJECT deployment → recalibrate items

  PC-M4  Random Forest (LGC)
    Metric    : Weighted F1-score ≥ 0.87 across all curve classes
    Below     : REJECT deployment

  PC-M5  GMM (MDC)
    Metric    : Log-likelihood ≥ defined cohort baseline (set at v1 launch)
    Below     : REJECT deployment

  PC-M6  LightGBM UCI (Ensemble)
    Metric    : RMSE ≤ 0.07 on validation set
    Below     : REJECT deployment

FAIRNESS REQUIREMENTS (R51 Compliance):
  All models before production deployment:
    ✔ Gender demographic parity difference < 0.05
    ✔ Region demographic parity difference < 0.05
    ✔ Age group demographic parity difference < 0.05
    ✔ Institution type parity difference < 0.05
  Failure in any check → REJECT model → alert ML team

HUMAN APPROVAL REQUIRED (M5 — AI Model Reality Law):
  ✔ Performance threshold passed
  ✔ Fairness checks passed
  ✔ ML team sign-off in pcd_model_performance_audit table
  ✔ Ops admin deployment approval in same table
  Both entries required before production deployment.

RETRAIN SCHEDULES:
  PC-M1  Weekly (Sunday 01:00 UTC)
  PC-M2  Bi-weekly (2nd and 16th, 02:00 UTC)
  PC-M3  Bi-weekly (with scenario difficulty recalibration — DOJO T4)
  PC-M4  Weekly (Sunday 01:30 UTC)
  PC-M5  Bi-weekly (2nd and 16th, 02:30 UTC)
  PC-M6  Weekly (Monday 01:00 UTC)

EXPLAINABILITY (R28-3 + DOJO P10):
  Method   : SHAP values for PC-M4, PC-M6
             GP posterior variance for PC-M1
             Prophet component decomposition for PC-M2
             IRT item information functions for PC-M3
             GMM component weights for PC-M5
  LLM Role : Text generation ONLY for human_summary in
             consistency_explainability_log and participant screens
             LLM NEVER computes UCI scores or gate decisions

IRT ITEM CALIBRATION (DOJO T4):
  Challenge difficulty parameters re-estimated bi-weekly
  from participant response data
  Difficulty labels must be data-derived (not author-declared)
  Scenario exploitation detection (CA-E-02) cross-references
  IRT item statistics to identify over-familiar items

MONTHLY COST ESTIMATE (R28 Compliance):
  All 6 models at 1M active participants: ~$40–$70/month
  IRT calibration infrastructure (bi-weekly): ~$5–$10/month
```

---

## SECTION P — OBSERVABILITY & MONITORING

```
METRICS (Prometheus — from port 8032):

  pcd_sessions_ingested_total              (counter)
  pcd_uci_tier_distribution                (gauge, labels: uci_tier)
  pcd_anomalies_detected_total             (counter, labels: anomaly_class)
  pcd_surge_legitimacy_distribution        (gauge, labels: legitimacy)
  pcd_belt_gate_results_total              (counter, labels: gate_result)
  pcd_model_inference_latency_ms           (histogram, labels: model_code)
  pcd_rt_pipeline_latency_ms               (histogram)
  pcd_batch_h1_duration_ms                 (histogram)
  pcd_batch_h6_duration_ms                 (histogram)
  pcd_pipeline_errors_total                (counter, labels: stage)
  pcd_retention_failures_total             (counter)
  pcd_provisonal_profiles_count            (gauge, sessions < 10)
  pcd_uci_flagged_active_count             (gauge)

GRAFANA ALERTS:
  ALERT: pcd_uci_flagged_active_count > 30
         → Slack: High flagged UCI count — review required
  ALERT: pcd_anomalies_detected_total CRITICAL > 10 in 1h
         → PagerDuty: Critical consistency anomaly wave
  ALERT: pcd_pipeline_errors_total > 5 in 5m
         → Slack: Consistency pipeline failure
  ALERT: pcd_belt_gate_results_total HOLD > 50 in 1 day
         → Slack: Belt gate hold rate elevated
  ALERT: pcd_batch_h6_duration_ms > 600000 (10 min)
         → Slack: H6 batch overrun — scale check needed

LOGGING (Loki):
  Log level     : INFO in prod, DEBUG in dev
  PII policy    : participant_id only in logs
  Retention     : 90 days hot, 7 years cold (R60)

TRACING (Jaeger):
  Full trace per session ingestion through all pipeline stages
  Trace IDs correlated with anomaly log entries
  Belt gate decisions include full trace for audit
```

---

## SECTION Q — DOJO SAAS INTEGRATION COMPLIANCE

```
DOJO T1 — Skill Validity Framework Lock
  ✔ DRC dimension validates that performance response to difficulty
    matches construct definition of genuine skill
  ✔ MDC dimension validates foundational domain support for
    advanced domain claims (foundational_domain_support_score)
  ✔ LGC dimension validates skill transfer evidence across domains
  ✔ UCI GOLD required for belt gate (maps to "construct mapping" gate)

DOJO T2 — Scoring Validity & Reliability Lock
  ✔ Inter-rater consistency tracked via XSEC dimension
  ✔ Scorer variance measurement via ISC-M-01 and XSEC-M-01
  ✔ Confidence intervals on UCI score (uci_confidence_interval_95)
  ✔ Low UCI (COPPER/FLAGGED) blocks belt promotion (ADJ-4 gate)
  ✔ CONDITIONAL gate triggers mentor board review (DOJO: low
    confidence scores cannot auto-promote)

DOJO T3 — Rater Calibration Lock
  ✔ CA-E-01 Rater Inflation Signature — detects mentor favoritism
  ✔ BS-S-07/08 signals from CML6-ANTICHEAT correlated with LGC
  ✔ Inconsistent self-assessment (CA-E-03) flagged for calibration
  ✔ Mentor drift detection via anomaly forwarding to ops console

DOJO T4 — Scenario Difficulty Calibration Lock
  ✔ PC-M3 (IRT) re-estimates difficulty parameters bi-weekly
  ✔ Difficulty labels are data-derived from DRC-M-01 fit
  ✔ CA-E-02 detects scenario exploitation before difficulty update
  ✔ Novel scenario performance tracked (DRC-M-03, CS-SES-14)

DOJO T6 — Learning Effectiveness Loop Lock
  ✔ LGC dimension measures pre/post learning delta (LGC-M-01 to M-06)
  ✔ LGC-M-03 retention check performance tracks retention
  ✔ LGC-M-04 skill transfer evidence score
  ✔ Retention failure cascade (CA-C-03) triggers curriculum review flag
  ✔ Skill tracks with flat LGC scores → flagged for curriculum review

DOJO T9 — Collusion & Manipulation Detection Lock
  ✔ Surge anomalies (CA-A) forwarded to CML6-ANTICHEAT
  ✔ Staircase plateau (CA-B-03) forwarded to CML6-ANTICHEAT
  ✔ Domain-isolated surge (CA-A-02) cross-checked with score injection
  ✔ Strategic regression (CA-C-02) linked to floor-suppression detection

DOJO P10 — AI Analytics Governance Lock
  ✔ All model versions tagged in pcd_model_performance_audit
  ✔ UCI computation logged with full signal breakdown
  ✔ Human override rights: ops admin can flag any profile for review
  ✔ Bias review sampling via quarterly demographic parity checks
  ✔ Explainability notes on every UCI score (SHAP + human summary)
  ✔ Belt decisions require mentor confirmation (AI cannot award belts)
```

---

## SECTION R — ENFORCEMENT RULES

```
ENFORCEMENT RULE PCE-01:
  No belt promotion may be finalized without a UCI GATE_PASS.
  CONDITIONAL gates require named mentor review and approval.
  HOLD gates block all promotion until UCI improves to GOLD or above.
  Promotion without gate clearance → STOP → report: BELT_GATE_BYPASS

ENFORCEMENT RULE PCE-02:
  The AntiGravity ×1.15 multiplier may not be applied to any
  participant with surge_legitimacy = UNVERIFIED.
  Multiplier application to UNVERIFIED surge
  → STOP → report: MULTIPLIER_LEGITIMACY_VIOLATION

ENFORCEMENT RULE PCE-03:
  UCI scores may not be computed for participants with
  fraud_hold = TRUE from CML6-ANTICHEAT.
  Last known UCI is frozen. No score updates until hold cleared.

ENFORCEMENT RULE PCE-04:
  UCI PROVISIONAL (< 10 sessions) may not be used for any
  gating decision — belt gate, multiplier, or tier advancement.
  Gating on PROVISIONAL UCI → STOP → report: PROVISIONAL_GATE_VIOLATION

ENFORCEMENT RULE PCE-05:
  All 6 ML models must be loaded and healthy before the service
  accepts session ingest events. Degraded mode (any model missing):
  → Service accepts ingestion only
  → All gate evaluations suspended
  → Emit: pcd_degraded_mode_active
  → Ops console alerted immediately

ENFORCEMENT RULE PCE-06:
  Anomaly class CA-D-03 (Synchronous Multi-Domain Spike) always
  triggers FLAGGED UCI tier immediately — no model override.
  This is a non-negotiable hard gate aligned with DOJO T9.

ENFORCEMENT RULE PCE-07:
  IRT ability estimate requires minimum 20 challenge responses
  before the ability_theta parameter is used in DRC scoring.
  Below 20 responses → DRC dimension uses fallback heuristic only.

ENFORCEMENT RULE PCE-08:
  Belt gate decisions are immutable after finalization.
  Amendment requires a NEW belt_gate_decisions record referencing
  the original. Direct modification of existing records
  → STOP → report: GATE_DECISION_TAMPERING_ATTEMPT

ENFORCEMENT RULE PCE-09:
  Participant-facing consistency screens (PCD-01, PCD-02) must
  NEVER display raw anomaly class codes, signal values, or
  model score details. Positive coaching framing required.
  Violation → STOP DEPLOYMENT → report: PII_DISPLAY_VIOLATION

ENFORCEMENT RULE PCE-10:
  Consistency signal data is retained for minimum 7 years (R60).
  Any deletion outside GDPR right-to-erasure workflow
  → STOP → report: ILLEGAL_DATA_DELETION_ATTEMPT

Violation of any enforcement rule above:
  → STOP EXECUTION
  → REPORT PCD_ENFORCEMENT_VIOLATION
  → NO CHAMPIONSHIP RESULT OR BELT CLAIM PERMITTED
```

---

## SECTION S — CONTRACT GATE REQUIREMENTS (R49 Compliance)

```
Before deployment, all items below must pass Contract Validator (R49):

  ✔ SessionCompletionPayload schema validated vs AsyncAPI registry
  ✔ All CS signal codes (CS-SES to CS-DOM) registered
  ✔ All anomaly class codes (CA-A to CA-E) registered
  ✔ API contract registered in API Contract Registry
  ✔ Permission → Screen matrix registered (Section M)
  ✔ Role → Widget matrix registered
  ✔ All 6 ML models loaded with performance thresholds met
  ✔ IRT calibration dataset loaded and item parameters estimated
  ✔ Integration with CML6-ANTICHEAT fraud_hold query tested
  ✔ Integration with CML6-RANK consistency_confidence_score tested
  ✔ Integration with DOJO Belt Engine gate result event tested
  ✔ Belt gate immutability constraint tested
  ✔ UCI PROVISIONAL gate enforcement tested
  ✔ Q/A test suite (R50) generated and all tests passing

Absence of any item → STOP EXECUTION
```

---

## SECTION T — PRODUCTION READINESS CHECKLIST

```
Before agent is declared Production-Ready (L3 compliant):

INFRASTRUCTURE:
  ✔ consistency-detector-service pods STATUS = Running (min 2 replicas)
  ✔ Redis Streams consumer active
  ✔ PostgreSQL consistency tables created with all indexes
  ✔ All 6 ML models loaded — /health returns models_loaded: 6
  ✔ H1 cron job (PC-M2, PC-M4) running and confirmed
  ✔ H6 cron job (PC-M3, PC-M5) running and confirmed

ML MODELS:
  ✔ PC-M1 MAE ≤ 0.08
  ✔ PC-M2 MAPE ≤ 12%
  ✔ PC-M3 IRT χ² p-value > 0.05
  ✔ PC-M4 Weighted F1 ≥ 0.87
  ✔ PC-M5 GMM log-likelihood ≥ baseline
  ✔ PC-M6 RMSE ≤ 0.07
  ✔ All fairness checks passed (all demographics < 0.05 parity)
  ✔ Human approval logs in pcd_model_performance_audit

CONTRACTS & INTEGRATIONS:
  ✔ R49 Contract Validator passed
  ✔ R50 QA Tests all passing
  ✔ CML6-ANTICHEAT fraud_hold interface live-tested
  ✔ CML6-RANK consistency_score interface live-tested
  ✔ DOJO Belt Engine gate event interface live-tested

SECURITY:
  ✔ Participant screens (PCD-01/02) show NO raw scores or anomaly codes
  ✔ Internal service auth enforced on all ingest/gate endpoints
  ✔ Ops admin MFA enforced (R40)
  ✔ Belt gate immutability constraint enforced in DB
  ✔ No PII in any ML model features or log payloads

OBSERVABILITY:
  ✔ All Prometheus metrics exporting
  ✔ Grafana dashboards + alerts deployed
  ✔ Loki logs flowing (PII-safe format)
  ✔ Jaeger traces active

Until all conditions above are met:
  Agent Status = "ARTIFACTS GENERATED — NOT DEPLOYED"
  NOT: "Performance Consistency Detector Operational"
```

---

## SECTION U — FINAL SEAL

```
╔═══════════════════════════════════════════════════════════════════════════════╗
║  AGENT SEAL   : ANTIGRAVITY-PCD-AGENT-v1.0                                    ║
║  FILE         : Championship_ML_6_Performance_Consistency_Detector_Agent.md   ║
║  DOMAIN       : SKILL & COMPETITION CORE → ANTIGRAVITY                        ║
║  PARENT       : ECOSKILLER MASTER EXECUTION PROMPT v12.0+                     ║
║                 DOJO SAAS LOCKED & SEALED SPEC v1.0                           ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  COMPLIANCE MAP:                                                               ║
║  ✔ R1   Technology Stack Lock (Python, FastAPI, Redis, PostgreSQL, LightGBM)  ║
║  ✔ R2   Domain Data Models (8 tables, all indexes defined)                    ║
║  ✔ R3   OpenAPI 3.1 Contract (Section I — 8 endpoints)                        ║
║  ✔ R4   AsyncAPI Event Schema (Section J — 7 event channels)                  ║
║  ✔ R5   Workflow State Machine (pipeline stages Section F — 9 stages)         ║
║  ✔ R12  AI Model Specification (Section O — 6 models fully governed)          ║
║  ✔ R24  Execution Skill Alignment (intern guide Section N)                    ║
║  ✔ R26  Intern Line-Level Execution Instructions (step-by-step)               ║
║  ✔ R28  Intelligence Cost Optimization (Traditional ML, LLM text-only)        ║
║  ✔ R29  Modern UI Screens (Section L — 7 screens fully specified)             ║
║  ✔ R38  Bug & Failure Registry (15 anomaly types, 10 enforcement rules)       ║
║  ✔ R39  Core Inbuilt Tools (consistency engine, IRT, growth classifier)       ║
║  ✔ R40  Internal Admin & Ops Console (Sections L, M)                          ║
║  ✔ R49  Contract Validator Compliance (Section S)                             ║
║  ✔ R51  Anti-Abuse (surge legitimacy, farming detection cross-signal)         ║
║  ✔ R60  Long-Term Archival (7-year retention, immutable gate decisions)       ║
║  ✔ R61  Data Network Effect Analytics (cohort consistency analytics L, PCD-07)║
║  ✔ R79  Trust & Reputation Amplification (UCI → recruiter trust signal)       ║
║  ✔ M5   AI Model Reality Law (human approval required, logged)                ║
║  ✔ L2   AI Operational Restriction (no real cloud deployment claimed)         ║
║  ✔ DOJO T1   Skill Validity Framework Lock                                    ║
║  ✔ DOJO T2   Scoring Validity & Reliability Lock                              ║
║  ✔ DOJO T3   Rater Calibration Lock                                           ║
║  ✔ DOJO T4   Scenario Difficulty Calibration Lock                             ║
║  ✔ DOJO T6   Learning Effectiveness Loop Lock                                 ║
║  ✔ DOJO T9   Collusion & Manipulation Detection Lock                          ║
║  ✔ DOJO P10  AI Analytics Governance Lock                                     ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  CONTENTS SUMMARY:                                                             ║
║  Section A  : Agent Identity & Constitutional Purpose                         ║
║  Section B  : Performance Consistency Framework (5 dimensions, 30 metrics)    ║
║  Section C  : UCI Algorithm (formula, 6 adjustments, tier thresholds)         ║
║  Section D  : Detection Model Architecture (6 ML models)                      ║
║  Section E  : Consistency Anomaly Taxonomy (5 classes, 15 anomaly types)      ║
║  Section F  : Detection Pipeline (4 modes, 9 stages)                          ║
║  Section G  : Consistency Signal Registry (46 signals, 3 classes)             ║
║  Section H  : Database Schema (8 tables, full indexes)                        ║
║  Section I  : API Contract Registry (OpenAPI 3.1, 8 endpoints)                ║
║  Section J  : Event Schema Registry (AsyncAPI, 7 channels)                    ║
║  Section K  : Microservice Architecture                                        ║
║  Section L  : UI Screens (7 screens fully specified)                          ║
║  Section M  : Permission & Role Matrix                                        ║
║  Section N  : Intern Execution Guide (step-by-step)                           ║
║  Section O  : ML Model Specification & Governance                             ║
║  Section P  : Observability & Monitoring                                      ║
║  Section Q  : DOJO SaaS Integration Compliance (7 sections mapped)            ║
║  Section R  : Enforcement Rules (10 rules)                                    ║
║  Section S  : Contract Gate Requirements (14 validation items)                ║
║  Section T  : Production Readiness Checklist                                  ║
║  Section U  : Final Seal                                                      ║
╠═══════════════════════════════════════════════════════════════════════════════╣
║  STATUS    : LOCKED · SEALED · DETERMINISTIC                                  ║
║  MUTATION  : Add-only via version bump to ANTIGRAVITY-PCD-AGENT-v2.0          ║
║  INTERP    : NONE                                                              ║
║  EXEC AUTH : Human declaration only                                           ║
╚═══════════════════════════════════════════════════════════════════════════════╝
```

---

*ECOSKILLER — Championship ML (6) Performance Consistency Detector Agent · AntiGravity Module · v1.0 · SEALED*
