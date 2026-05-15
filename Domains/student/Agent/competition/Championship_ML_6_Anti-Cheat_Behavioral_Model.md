# CHAMPIONSHIP ML (6) — ANTI-CHEAT BEHAVIORAL MODEL
## SKILL & COMPETITION CORE · ANTIGRAVITY MODULE

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  ECOSKILLER — CHAMPIONSHIP ML (6) ANTI-CHEAT BEHAVIORAL MODEL AGENT         ║
║  Domain    : SKILL & COMPETITION CORE → ANTIGRAVITY                          ║
║  Agent Code: CML6-ANTICHEAT-ANTIGRAVITY                                      ║
║  Seal ID   : ANTIGRAVITY-ANTICHEAT-AGENT-v1.0                                ║
║  Status    : FINAL · LOCKED · SEALED · DETERMINISTIC                         ║
║  Mutation  : Add-only via version bump                                        ║
║  Interpretation Authority : NONE                                              ║
║  Execution Authority      : Human declaration only                            ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION A — AGENT IDENTITY & CONSTITUTIONAL PURPOSE

```
AGENT NAME    : Championship ML (6) Anti-Cheat Behavioral Model Agent
AGENT CODE    : CML6-ANTICHEAT-ANTIGRAVITY
AGENT TYPE    : Adversarial Integrity & Behavioral Anomaly Detection Engine
DOMAIN        : SKILL & COMPETITION CORE → ANTIGRAVITY
PARENT SYSTEM : ECOSKILLER MASTER EXECUTION PROMPT v12.0+
DOJO PARENT   : DOJO SAAS LOCKED & SEALED PRODUCTION ARTIFACT SPEC v1.0

CONSTITUTIONAL MANDATE:
  This agent is the behavioral integrity guardian of all AntiGravity
  Championship events. It operates as a continuous, real-time and
  batch-mode behavioral surveillance system that detects, classifies,
  flags, quarantines, and escalates cheating, manipulation, collusion,
  impersonation, automation-abuse, and performance-fraud attempts
  across all 6 championship tiers simultaneously.

  It does NOT disqualify participants autonomously.
  It does NOT modify scores or ranks directly.
  It ONLY produces behavioral signals, confidence-rated flags,
  evidence packages, and audit trails — all human-reviewable.

ANTIGRAVITY INTEGRITY CONTEXT:
  The AntiGravity domain specifically rewards momentum surges,
  comeback performance, streak breaks, and rank velocity. These
  exact properties are the most exploitable attack surfaces:
  artificially engineered surges, fake streaks, bot-driven
  performance spikes, and collusion rings that manufacture upsets.
  This agent is purpose-built to neutralise these exact attack vectors.

DOJO ALIGNMENT:
  Implements DOJO SAAS Section T9 — Collusion & Manipulation Detection Lock
  Implements DOJO SAAS Section T10 — Behavioral Safety Lock
  Implements DOJO SAAS Section T15 — Economic Abuse Controls Lock
  Implements DOJO SAAS Section P1  — Security Hardening Lock
  Implements ECOSKILLER R51        — Anti-Spam & Platform Abuse Prevention Law
  Implements ECOSKILLER R38        — Master Bug & Failure Registry Law
  Implements ECOSKILLER R40        — Internal Admin & Ops Console Law
  Implements ECOSKILLER R60        — Long-Term Archival & Data History Law
  Implements ECOSKILLER R28        — Intelligence Cost Optimization Law
```

---

## SECTION B — ANTI-CHEAT THREAT TAXONOMY

```
The following threat classes are officially registered and actively
detected by this agent. Each class has a THREAT-ID, description,
severity tier, and detection method class.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
THREAT CLASS 1 — SCORE MANIPULATION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  THR-SM-01 | Impossible Score Spike
    Description : Single-session score rise exceeding 4× user's
                  own 90-day rolling performance average.
    Severity    : CRITICAL
    Detection   : Statistical anomaly (Z-score > 3.5 threshold)

  THR-SM-02 | Score Precision Injection
    Description : Submitted scores arriving at exact floating-point
                  values (e.g., 999.9999) with < 50ms latency from
                  challenge completion — indicating scripted injection.
    Severity    : CRITICAL
    Detection   : Latency fingerprint + value distribution analysis

  THR-SM-03 | Replay-Attack Score Submission
    Description : Re-submitting a previously accepted score payload
                  with a forged timestamp to claim double-credit.
    Severity    : CRITICAL
    Detection   : Nonce validation + idempotency hash check

  THR-SM-04 | Score Suppression (Deliberate Under-Performance)
    Description : A participant deliberately scoring below their
                  established floor to manipulate tier seeding,
                  then surging in later rounds for AntiGravity bonus.
    Severity    : HIGH
    Detection   : Floor-suppression pattern + subsequent surge delta

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
THREAT CLASS 2 — IDENTITY & IMPERSONATION FRAUD
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  THR-ID-01 | Account Substitution (Human Proxy)
    Description : A skilled third party completes challenges on
                  behalf of a registered participant.
    Severity    : CRITICAL
    Detection   : Behavioral biometric deviation (keystroke, timing,
                  navigation pattern shift)

  THR-ID-02 | Multi-Account Farming
    Description : One individual operates multiple accounts to
                  accumulate referral bonuses, endorsements, or to
                  inflate one primary account's rank delta.
    Severity    : HIGH
    Detection   : Device fingerprint + IP subnet correlation +
                  behavioral twin analysis

  THR-ID-03 | Account Harvesting
    Description : Acquiring dormant legitimate accounts and activating
                  them to manufacture artificial platform presence.
    Severity    : HIGH
    Detection   : Account age vs. sudden activity delta + geolocation shift

  THR-ID-04 | VPN / Proxy Region Hop
    Description : Switching regions mid-competition to exploit
                  regional tier differentials or weaker opponent pools.
    Severity    : MEDIUM
    Detection   : IP geo-delta + VPN/proxy ASN database match

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
THREAT CLASS 3 — AUTOMATION & BOT ABUSE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  THR-BOT-01 | Script-Driven Challenge Completion
    Description : Automated scripts or bots completing challenges
                  at machine-speed with inhuman precision.
    Severity    : CRITICAL
    Detection   : Completion time distribution vs. human baseline +
                  interaction entropy analysis

  THR-BOT-02 | Automated Streak Maintenance
    Description : Bots logging micro-activity events at exact
                  midnight UTC windows to prevent streak breaks
                  without genuine human engagement.
    Severity    : HIGH
    Detection   : Activity timestamp clustering at reset boundaries +
                  session depth analysis (< 30 seconds = suspicious)

  THR-BOT-03 | Mass Endorsement Bot Ring
    Description : Coordinated bot accounts issuing endorsements
                  to one participant to inflate social_endorsement_count
                  signal used in AGCRS (ranking engine).
    Severity    : HIGH
    Detection   : Endorsement velocity > 50/hour from < 5 unique IPs +
                  new-account endorser age < 72 hours

  THR-BOT-04 | API Scraping & Replay Injection
    Description : Scraping opponent answers via API timing attacks
                  then injecting improved responses before deadline.
    Severity    : CRITICAL
    Detection   : API call pattern analysis + answer submission
                  latency fingerprint

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
THREAT CLASS 4 — COLLUSION & RING ATTACKS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  THR-COL-01 | Reciprocal High-Score Pairing
    Description : Two or more participants systematically awarding
                  each other maximum peer scores in every shared match
                  to mutually inflate AGCRS signals.
    Severity    : HIGH
    Detection   : Peer scoring reciprocity matrix + chi-squared
                  test vs. expected distribution

  THR-COL-02 | Deliberate Match Throw
    Description : A participant intentionally performing poorly in a
                  match to allow a colluding partner to gain an
                  upset_score bonus — exploiting AntiGravity multiplier.
    Severity    : HIGH
    Detection   : Below-floor performance in round + subsequent
                  partner AntiGravity multiplier activation correlation

  THR-COL-03 | Tournament Bracket Manipulation
    Description : Coordinated group of participants arranging losses
                  and wins to place a designated member on the optimal
                  bracket path to the finals.
    Severity    : HIGH
    Detection   : Win/loss pattern graph analysis + bracket
                  statistical improbability score

  THR-COL-04 | Mentor Favoritism Collusion
    Description : A mentor systematically awarding score overrides
                  to a specific participant outside statistical norms
                  (DOJO T9 + T3 violation).
    Severity    : CRITICAL
    Detection   : Mentor override frequency analysis per participant +
                  score delta distribution vs. peer mentors

  THR-COL-05 | Rating Inflation Ring
    Description : A closed group of participants playing only each
                  other in low-risk matches to inflate ratings without
                  external competitive pressure.
    Severity    : MEDIUM
    Detection   : Match opponent diversity index < threshold +
                  closed-graph detection on match history

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
THREAT CLASS 5 — BEHAVIORAL SPOOFING
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  THR-BEH-01 | Biometric Profile Spoofing
    Description : A participant deliberately mimics their own
                  historical interaction patterns (timing, navigation)
                  to mask account substitution.
    Severity    : HIGH
    Detection   : Deep behavioral variance analysis across
                  multiple session dimensions simultaneously

  THR-BEH-02 | Calibrated Slow Play
    Description : Deliberately adding artificial delay before
                  submissions to appear "human-speed" while using
                  bot-generated answers.
    Severity    : HIGH
    Detection   : Answer quality vs. time-taken correlation anomaly
                  (high quality + artificially uniform timing)

  THR-BEH-03 | Pattern Replay Attack
    Description : Participant studies recordings of previous top
                  performers and mimics exact response patterns to
                  game scoring rubrics without genuine understanding.
    Severity    : MEDIUM
    Detection   : Cross-session answer fingerprint similarity +
                  novel scenario performance drop

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
THREAT CLASS 6 — ECONOMIC & PLATFORM ABUSE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  THR-ECO-01 | Referral Loop Farming
    Description : Creating fake referred accounts that immediately
                  refer back to manufacture circular referral rewards.
    Severity    : HIGH
    Detection   : Referral graph cycle detection +
                  account engagement depth after registration

  THR-ECO-02 | Scholarship Farming
    Description : Gaming the intelligence scoring system to
                  qualify for scholarships using coordinated
                  multi-account score inflation.
    Severity    : HIGH
    Detection   : Scholarship-qualifier cohort behavioral audit +
                  cross-account correlation

  THR-ECO-03 | AntiGravity Multiplier Farming
    Description : Deliberately tanking rank for 7+ days then
                  engineering a rapid surge to repeatedly trigger
                  the AntiGravity ×1.15 multiplier bonus.
    Severity    : CRITICAL
    Detection   : Cyclic suppression-surge pattern detection
                  (period analysis on rank_delta time series)

  THR-ECO-04 | Fake Tournament Entry Loops
    Description : Registering for tournaments with no intent to
                  compete — forfeiting strategically to manipulate
                  bracket structures for colluding partners.
    Severity    : MEDIUM
    Detection   : Forfeiture rate > 40% over 5+ tournaments +
                  correlation with partner advancement

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

TOTAL REGISTERED THREATS: 20
CRITICAL SEVERITY        : 8
HIGH SEVERITY            : 9
MEDIUM SEVERITY          : 3

All 20 threats are actively monitored.
New threats may only be added via version bump with human declaration.
```

---

## SECTION C — BEHAVIORAL SIGNAL CORPUS

```
The following behavioral signals are continuously collected per
participant session. All signals feed the ML detection models.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS I — TIMING SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  BS-T-01  challenge_response_latency_ms
             Time from challenge display to first input event (ms)
  BS-T-02  submission_latency_ms
             Time from last edit to submission event (ms)
  BS-T-03  inter_keystroke_interval_avg_ms
             Average time between consecutive keystrokes
  BS-T-04  inter_keystroke_interval_std_ms
             Standard deviation of keystroke intervals
  BS-T-05  page_focus_duration_ms
             Total time the challenge page had active focus
  BS-T-06  idle_gap_count
             Number of idle gaps > 30 seconds within session
  BS-T-07  submission_uniformity_score
             Variance of submission timing across 20+ sessions
             (low variance = suspicious automation pattern)
  BS-T-08  activity_boundary_proximity_score
             Distance of login/activity from streak-reset UTC
             boundary (0–100: 100 = exact boundary login)
  BS-T-09  challenge_completion_time_vs_cohort_percentile
             User completion time vs. same-difficulty cohort median

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS II — INTERACTION PATTERN SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  BS-I-01  mouse_movement_entropy
             Entropy of mouse path during session (low = robotic)
  BS-I-02  copy_paste_event_count
             Number of clipboard paste events per session
  BS-I-03  scroll_pattern_regularity
             Regularity index of scroll events (high = scripted)
  BS-I-04  tab_switch_count
             Number of tab/window switches during challenge
  BS-I-05  navigation_path_deviation
             Deviation from user's own historical navigation path
  BS-I-06  form_field_fill_pattern
             Whether fields filled sequentially vs. random access
  BS-I-07  right_click_frequency
             Unusually high right-click = context menu scraping
  BS-I-08  focus_loss_during_challenge_count
             Window focus losses during active challenge window
  BS-I-09  answer_edit_cycle_count
             Number of edit-delete-retype cycles per answer field
  BS-I-10  screen_resolution_consistency
             Change in reported screen resolution across sessions

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS III — NETWORK & DEVICE SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  BS-N-01  ip_address_hash
             Hashed IP per session (no PII stored in raw form)
  BS-N-02  ip_asn_category
             ISP / Hosting / VPN / Proxy / Tor classification
  BS-N-03  ip_geo_country_code
             Country code of session IP
  BS-N-04  ip_geo_delta_km
             Geographic distance between consecutive sessions
  BS-N-05  device_fingerprint_hash
             Hashed device fingerprint (browser/OS/hardware)
  BS-N-06  device_fingerprint_change_flag
             Boolean: fingerprint changed since last session
  BS-N-07  user_agent_consistency_score
             Consistency of user agent string across sessions
  BS-N-08  concurrent_session_count
             Number of simultaneous active sessions for this account
  BS-N-09  api_request_rate_per_minute
             API requests per minute — elevated = bot indicator
  BS-N-10  tls_fingerprint_hash
             TLS client fingerprint (JA3 hash) per session

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS IV — PERFORMANCE SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  BS-P-01  score_z_score_vs_90day
             Z-score of current score vs. user's 90-day distribution
  BS-P-02  score_z_score_vs_cohort
             Z-score of current score vs. same-tier cohort
  BS-P-03  answer_similarity_to_previous_sessions
             Cosine similarity of current answers to past sessions
             (extremely high similarity = replay attack indicator)
  BS-P-04  novel_scenario_performance_delta
             Score on new (never seen) scenarios vs. familiar ones
             (large negative delta = pattern memorisation cheating)
  BS-P-05  error_rate_per_session
             Errors per session — too low can indicate scripting
  BS-P-06  performance_floor_breach_count
             Times participant scored below established floor
  BS-P-07  performance_ceiling_breakthrough_count
             Times participant exceeded own ceiling by > 2 std devs
  BS-P-08  consecutive_perfect_score_count
             Consecutive rounds with maximum possible score

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS V — SOCIAL & RELATIONAL SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  BS-S-01  peer_scoring_reciprocity_index
             Measure of mutual high-scoring between two participants
  BS-S-02  endorsement_source_diversity_score
             Unique IP/device sources for received endorsements
  BS-S-03  endorsement_source_account_age_avg_days
             Average age of accounts that endorsed this participant
  BS-S-04  match_opponent_diversity_index
             Diversity of opponents faced over last 30 matches
  BS-S-05  closed_group_match_ratio
             % of matches played within a closed set of participants
  BS-S-06  referral_graph_cycle_depth
             Depth of cycles detected in referral chain graph
  BS-S-07  mentor_override_received_frequency
             How often this participant receives mentor overrides
  BS-S-08  mentor_id_concentration_score
             % of overrides from a single mentor vs. many mentors

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
CLASS VI — ANTIGRAVITY-SPECIFIC SIGNALS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  BS-AG-01 rank_suppression_duration_days
              Consecutive days rank was held below personal 60-day
              average before current surge began
  BS-AG-02 surge_to_suppression_cycle_count
              Number of full suppression-then-surge cycles detected
              (> 2 = AntiGravity Farming pattern)
  BS-AG-03 multiplier_activation_frequency
              How many times AntiGravity ×1.15 multiplier triggered
              in last 90 days for this participant
  BS-AG-04 upset_score_legitimacy_index
              Cross-validated legitimacy score of upset events
              (verified by independent performance signal correlation)
  BS-AG-05 comeback_trigger_pattern_regularity
              Regularity interval of comeback events
              (high regularity = engineered, not natural)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

TOTAL BEHAVIORAL SIGNALS: 52
All signals stored in: BEHAVIORAL_SIGNAL_LOG (Section H schema)
All signals anonymized: participant_id only (no PII in model features)
Signal collection cadence: Real-time per event + batch 5-minute rollup
```

---

## SECTION D — DETECTION MODEL ARCHITECTURE

```
COMPLIANCE DECLARATION:
  All detection models use Traditional ML only (R28-2 enforced).
  No LLM may compute fraud decisions or risk scores.
  LLMs permitted ONLY for generating human-readable audit summaries.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL M1 — INDIVIDUAL BEHAVIORAL ANOMALY DETECTOR
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  Model Type    : Isolation Forest (unsupervised anomaly detection)
  Input Features: BS-T-01 to BS-T-09, BS-I-01 to BS-I-10,
                  BS-P-01 to BS-P-08
  Output        : anomaly_score (float, 0.0–1.0)
                  anomaly_flag (boolean, threshold: 0.72)
  Training Data : Historical clean session behavioral vectors
  Purpose       : Detect individual participants whose current
                  session behavior deviates from their own
                  historical baseline
  Retrain Schedule: Weekly
  Minimum Performance: Precision ≥ 0.85 at 1% false-positive rate

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL M2 — BOT CLASSIFICATION DETECTOR
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  Model Type    : Gradient Boosting Classifier (LightGBM)
  Input Features: BS-T-01 to BS-T-09, BS-I-01 to BS-I-10,
                  BS-N-07, BS-N-09, BS-P-05, BS-P-08
  Output        : bot_probability (float, 0.0–1.0)
                  bot_class (HUMAN / SUSPECTED_BOT / CONFIRMED_BOT)
  Training Data : Labeled bot vs. human interaction dataset
  Purpose       : Binary classification of session as bot-driven
                  or human-driven
  Retrain Schedule: Weekly with new labeled examples
  Minimum Performance: F1-score ≥ 0.92, AUC-ROC ≥ 0.96

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL M3 — COLLUSION GRAPH DETECTOR
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  Model Type    : Graph Neural Network on participant interaction graph
                  (PyTorch Geometric — Traditional ML variant)
  Input Features: BS-S-01 to BS-S-06, match history edges,
                  peer scoring matrices
  Output        : collusion_ring_membership_score (float, 0.0–1.0)
                  ring_id (group identifier for connected colluders)
  Training Data : Synthetic collusion ring graphs + confirmed cases
  Purpose       : Identify clusters of participants engaged in
                  coordinated mutual-benefit manipulation
  Retrain Schedule: Bi-weekly
  Minimum Performance: Collusion ring recall ≥ 0.80

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL M4 — IDENTITY CONTINUITY VERIFIER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  Model Type    : One-Class SVM (novelty detection per participant)
  Input Features: BS-T-03, BS-T-04, BS-I-01, BS-I-05, BS-I-09,
                  BS-N-05, BS-N-06, BS-N-07, BS-N-10
  Output        : identity_continuity_score (float, 0.0–1.0)
                  identity_break_flag (boolean, threshold: 0.65)
  Training Data : Per-participant baseline behavioral fingerprint
                  (minimum 20 clean sessions required to activate)
  Purpose       : Detect account substitution — when a different
                  human or bot takes over an account mid-competition
  Retrain Schedule: Continuous per-participant rolling update
  Minimum Performance: True-positive rate ≥ 0.78 on known subs

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL M5 — ANTIGRAVITY FARMING PATTERN DETECTOR
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  Model Type    : Time-Series Classifier (LSTM — Traditional ML)
  Input Features: BS-AG-01 to BS-AG-05, rank_delta time series
                  (90-day window), multiplier_activation history
  Output        : farming_pattern_score (float, 0.0–1.0)
                  farming_pattern_class (NATURAL / SUSPICIOUS / FARMING)
  Training Data : Historical rank trajectory labeled dataset
  Purpose       : Specifically detect the cyclic suppression-surge
                  pattern used to farm the AntiGravity ×1.15
                  multiplier bonus repeatedly
  Retrain Schedule: Weekly
  Minimum Performance: FARMING class precision ≥ 0.88

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODEL M6 — COMPOSITE FRAUD RISK SCORER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  Model Type    : Logistic Regression Ensemble (Traditional ML)
  Input Features: Outputs of M1 through M5 + rule engine signals
  Output        : composite_fraud_risk_score (float, 0.0–1.0)
                  risk_tier (GREEN / YELLOW / ORANGE / RED)
  Purpose       : Unified single fraud risk signal that combines
                  all model outputs into one actionable score
  Risk Tier Thresholds (LOCKED — v1.0):
    GREEN  : 0.00 – 0.29  → No action required
    YELLOW : 0.30 – 0.49  → Flag for monitoring, no rank hold
    ORANGE : 0.50 – 0.74  → Rank hold + moderation queue entry
    RED    : 0.75 – 1.00  → Immediate rank quarantine + ops alert

  Threshold mutation requires version bump + human declaration.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
INFERENCE COST DECLARATION (R28 Compliance):
  M1 Isolation Forest  : $0.0004 per 1,000 sessions
  M2 LightGBM          : $0.0008 per 1,000 sessions
  M3 Graph NN          : $0.003  per 1,000 participant graphs (batch)
  M4 One-Class SVM     : $0.0002 per 1,000 sessions
  M5 LSTM              : $0.0015 per 1,000 participants (hourly batch)
  M6 Logistic Ensemble : $0.0001 per 1,000 participants
  TOTAL MONTHLY EST.   : ~$35–$60 at 1M active participants
```

---

## SECTION E — DETECTION PIPELINE SPECIFICATION

```
PIPELINE EXECUTION MODES:

  MODE A — REAL-TIME (per session event)
    Trigger : Any behavioral event from BS corpus
    Latency : < 200ms end-to-end
    Models  : M1, M2, M4, M6 (fast models only)
    Output  : risk_tier update per participant

  MODE B — BATCH HOURLY (cohort analysis)
    Trigger : Cron job every 60 minutes
    Models  : M3 (graph analysis), M5 (time-series)
    Output  : collusion rings, farming patterns updated

  MODE C — DEEP AUDIT (triggered investigation)
    Trigger : Ops Admin manual trigger OR RED risk_tier
    Models  : All M1–M6 + full rule engine
    Output  : Complete evidence package (Section F)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PIPELINE STAGES (MODE A — REAL-TIME):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  STAGE AC-1 — EVENT INGESTION
    Source   : WebSocket event bus + API gateway hook
    Action   : Receive behavioral event → validate schema
    Store    : Persist to BEHAVIORAL_SIGNAL_LOG (Section H)
    Failure  : DROP + DEAD_LETTER_QUEUE + alert

  STAGE AC-2 — SIGNAL EXTRACTION
    Action   : Map raw event to BS signal vector
    Output   : Normalized feature vector per session
    Failure  : STOP STAGE → emit: signal_extraction_failure

  STAGE AC-3 — RULE ENGINE PRE-FILTER
    Action   : Apply deterministic hard rules (Section G)
    Hard Rule Violations → IMMEDIATE FLAG regardless of ML
    Output   : PASS (continue) / IMMEDIATE_FLAG (escalate now)

  STAGE AC-4 — REAL-TIME ML INFERENCE
    Models   : M1, M2, M4 run in parallel
    Output   : anomaly_score, bot_probability,
               identity_continuity_score
    Timeout  : 150ms hard limit (timeout = fallback to rule engine)

  STAGE AC-5 — COMPOSITE RISK SCORING (M6)
    Input    : M1, M2, M4 outputs + rule engine signals
    Output   : composite_fraud_risk_score + risk_tier
    Action   : Store to PARTICIPANT_RISK_PROFILE table

  STAGE AC-6 — RISK TIER ENFORCEMENT
    GREEN  → No action
    YELLOW → Write to MONITORING_WATCHLIST
    ORANGE → Set fraud_hold = TRUE on participant_agcrs
             Create moderation queue entry
             Notify ranking engine (CML6-RANK) to display [UNDER REVIEW]
    RED    → Immediate rank quarantine
             Emit: fraud_critical_alert event
             Page Ops Console (R40)
             Lock further score submissions pending review

  STAGE AC-7 — EVIDENCE PACKAGING
    On ORANGE or RED: Auto-generate Evidence Package (Section F)
    Store to: FRAUD_EVIDENCE_PACKAGES table

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PIPELINE STAGES (MODE B — BATCH HOURLY):
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  STAGE BC-1 — GRAPH CONSTRUCTION
    Build: Participant interaction graph (match edges, peer edges,
           endorsement edges, referral edges)

  STAGE BC-2 — COLLUSION DETECTION (M3)
    Run  : Graph Neural Network over full participant graph
    Flag : All participants with collusion_ring_membership_score ≥ 0.70
    Tag  : ring_id for grouped investigation

  STAGE BC-3 — FARMING PATTERN DETECTION (M5)
    Run  : LSTM over 90-day rank trajectory per participant
    Flag : All participants with farming_pattern_class = FARMING

  STAGE BC-4 — COMPOSITE UPDATE
    Update: All flagged participants through M6
    Re-evaluate risk_tier for each flagged participant

  STAGE BC-5 — BATCH REPORT GENERATION
    Output : Hourly anomaly summary to BATCH_ANOMALY_REPORT table
    Notify : Ops Console (R40) if new ORANGE/RED participants found
```

---

## SECTION F — EVIDENCE PACKAGE SPECIFICATION

```
When a participant reaches ORANGE or RED risk tier, an immutable
Evidence Package is automatically assembled and stored.

EVIDENCE PACKAGE CONTENTS:

  EP-01  participant_id               UUID (no PII)
  EP-02  risk_tier                    ORANGE / RED
  EP-03  composite_fraud_risk_score   Float 0.0–1.0
  EP-04  triggered_threat_ids         List of THR-XX-XX codes
  EP-05  model_scores_breakdown       {M1: score, M2: score ... M6: score}
  EP-06  top_contributing_signals     Top 10 BS signals with values
  EP-07  rule_violations_list         All hard rules triggered (Section G)
  EP-08  timeline_of_events           Last 50 behavioral events JSON
  EP-09  session_comparison_matrix    Current session vs. 90-day baseline
  EP-10  peer_interaction_subgraph    Subgraph of related participants
  EP-11  rank_trajectory_chart_data   90-day rank_delta time series
  EP-12  antigravity_multiplier_history  All past multiplier activations
  EP-13  mentor_override_history      All overrides received (if any)
  EP-14  device_network_summary       IP/device/geo summary (hashed)
  EP-15  human_readable_summary       LLM-generated plain English
                                      explanation (R28-3 — text only)
  EP-16  package_hash                 SHA-256 of EP-01 to EP-15 payload
  EP-17  created_at                   UTC timestamp
  EP-18  is_immutable                 TRUE (write-once)

Evidence packages may NEVER be modified after creation.
Amendments require creating a NEW package referencing the original.
All packages stored in FRAUD_EVIDENCE_PACKAGES table (Section H).
Retention: Minimum 7 years (R60 compliance).
```

---

## SECTION G — HARD RULE ENGINE

```
These rules execute BEFORE any ML model.
A hard rule violation triggers an immediate flag regardless of ML output.
Hard rules are deterministic. No ML involved.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

RULE HR-01 | IMPOSSIBLE COMPLETION TIME
  CONDITION : time_to_completion_ms < challenge_minimum_viable_time_ms × 0.40
  ACTION    : IMMEDIATE RED flag + score invalidated for round
  RATIONALE : No human can complete this challenge this fast

RULE HR-02 | DUPLICATE SCORE SUBMISSION NONCE
  CONDITION : score_submission_nonce already exists in NONCE_REGISTRY
  ACTION    : IMMEDIATE RED flag + submission rejected (idempotent)
  RATIONALE : Replay attack detected

RULE HR-03 | CONCURRENT SESSION VIOLATION
  CONDITION : concurrent_session_count > 1 for same account
  ACTION    : IMMEDIATE ORANGE flag + secondary session terminated
  RATIONALE : One human cannot be in two sessions simultaneously

RULE HR-04 | VPN / TOR / HOSTING ASN DETECTION
  CONDITION : ip_asn_category IN ('VPN', 'TOR', 'DATACENTER', 'PROXY')
  ACTION    : YELLOW flag + challenge session flagged for review
  EXCEPTION : Pre-registered VPN users (privacy exemption flow)
  RATIONALE : Anonymizing infrastructure hides identity attacks

RULE HR-05 | MASS ENDORSEMENT VELOCITY
  CONDITION : endorsements_received_in_1h > 50
              AND unique_endorser_ips < 5
  ACTION    : ORANGE flag + endorsement signal zeroed for scoring cycle
  RATIONALE : Endorsement bot ring detected

RULE HR-06 | API CALL RATE BURST
  CONDITION : api_request_rate_per_minute > 300
  ACTION    : ORANGE flag + rate limit enforced + session paused
  RATIONALE : Automated scraping or injection attempt

RULE HR-07 | ANTIGRAVITY FARMING CYCLE LOCK
  CONDITION : surge_to_suppression_cycle_count > 2
              AND multiplier_activation_frequency > 4 in 90 days
  ACTION    : ORANGE flag + AntiGravity multiplier suspended
              pending review
  RATIONALE : Repeated cyclic exploitation of multiplier

RULE HR-08 | ZERO ERROR PERFECTION STREAK
  CONDITION : consecutive_perfect_score_count > 10
              AND error_rate_per_session = 0.00 for all 10
  ACTION    : YELLOW flag + deep audit triggered
  RATIONALE : Statistically improbable human performance

RULE HR-09 | DEVICE FINGERPRINT SUBSTITUTION
  CONDITION : device_fingerprint_change_flag = TRUE
              AND identity_continuity_score < 0.50
  ACTION    : ORANGE flag + session paused
  RATIONALE : Possible account handover to different person/device

RULE HR-10 | COLLUSION THROW DETECTION
  CONDITION : performance_floor_breach_count > 3 in last 5 matches
              AND partner's upset_score > 80th percentile in same matches
  ACTION    : ORANGE flag for BOTH participants
  RATIONALE : Coordinated throw + upset score farming

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

TOTAL HARD RULES: 10
All rules evaluate in < 5ms (deterministic, no ML).
Hard rules take precedence over all ML model outputs.
Rule mutation requires version bump + human declaration.
```

---

## SECTION H — DATABASE SCHEMA

```sql
-- ════════════════════════════════════════════════════════════════
-- ANTI-CHEAT BEHAVIORAL MODEL — COMPLETE SCHEMA
-- ════════════════════════════════════════════════════════════════

-- Behavioral signal collection
TABLE behavioral_signal_log (
  signal_id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id      UUID        NOT NULL REFERENCES users(id),
  championship_id     UUID        NOT NULL,
  session_id          UUID        NOT NULL,
  signal_class        TEXT        NOT NULL,  -- BS-T / BS-I / BS-N / BS-P / BS-S / BS-AG
  signal_code         TEXT        NOT NULL,  -- e.g. BS-T-01
  signal_value        DECIMAL(18,6) NOT NULL,
  recorded_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  source_service      TEXT        NOT NULL,
  is_processed        BOOLEAN     DEFAULT FALSE
);

-- Per-participant rolling risk profile
TABLE participant_risk_profile (
  participant_id          UUID        PRIMARY KEY REFERENCES users(id),
  championship_id         UUID        NOT NULL,
  risk_tier               TEXT        NOT NULL DEFAULT 'GREEN',
                                      -- GREEN / YELLOW / ORANGE / RED
  composite_fraud_risk    DECIMAL(6,4) NOT NULL DEFAULT 0.0,
  m1_anomaly_score        DECIMAL(6,4),
  m2_bot_probability      DECIMAL(6,4),
  m3_collusion_score      DECIMAL(6,4),
  m4_identity_score       DECIMAL(6,4),
  m5_farming_score        DECIMAL(6,4),
  collusion_ring_id       UUID,       -- FK to collusion_ring_registry
  farming_pattern_class   TEXT,       -- NATURAL / SUSPICIOUS / FARMING
  last_evaluated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  under_review            BOOLEAN     DEFAULT FALSE,
  review_locked_at        TIMESTAMPTZ
);

-- Hard rule violation log
TABLE hard_rule_violation_log (
  violation_id        UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id      UUID        NOT NULL REFERENCES users(id),
  rule_code           TEXT        NOT NULL,  -- HR-01 to HR-10
  session_id          UUID        NOT NULL,
  evidence_payload    JSONB       NOT NULL,
  triggered_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  action_taken        TEXT        NOT NULL,  -- YELLOW / ORANGE / RED / SCORE_INVALID
  score_invalidated   BOOLEAN     DEFAULT FALSE,
  reviewed_by         UUID,       -- Ops admin UUID after review
  review_outcome      TEXT        -- CLEARED / CONFIRMED / ESCALATED
);

-- Collusion ring registry
TABLE collusion_ring_registry (
  ring_id             UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  ring_members        UUID[]      NOT NULL,  -- Array of participant_ids
  ring_score          DECIMAL(6,4) NOT NULL,
  threat_class        TEXT        NOT NULL,  -- THR-COL-XX
  detected_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  status              TEXT        DEFAULT 'ACTIVE',
                                  -- ACTIVE / UNDER_REVIEW / CLEARED / CONFIRMED
  ops_assigned_to     UUID        REFERENCES users(id),
  resolved_at         TIMESTAMPTZ
);

-- Nonce registry for replay attack prevention (HR-02)
TABLE submission_nonce_registry (
  nonce               TEXT        PRIMARY KEY,
  participant_id      UUID        NOT NULL REFERENCES users(id),
  championship_id     UUID        NOT NULL,
  registered_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at          TIMESTAMPTZ NOT NULL  -- TTL: challenge deadline + 1h
);

-- Immutable evidence packages
TABLE fraud_evidence_packages (
  package_id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id      UUID        NOT NULL REFERENCES users(id),
  championship_id     UUID        NOT NULL,
  risk_tier           TEXT        NOT NULL,
  triggered_threats   TEXT[]      NOT NULL,  -- Array of THR-XX-XX codes
  model_scores        JSONB       NOT NULL,
  top_signals         JSONB       NOT NULL,
  rule_violations     TEXT[]      NOT NULL,
  event_timeline      JSONB       NOT NULL,
  session_comparison  JSONB       NOT NULL,
  peer_subgraph       JSONB       NOT NULL,
  rank_trajectory     JSONB       NOT NULL,
  multiplier_history  JSONB       NOT NULL,
  mentor_overrides    JSONB       NOT NULL,
  device_summary      JSONB       NOT NULL,
  human_summary       TEXT,                  -- LLM generated (R28-3)
  package_hash        TEXT        NOT NULL,  -- SHA-256
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_immutable        BOOLEAN     NOT NULL DEFAULT TRUE,
  predecessor_id      UUID        REFERENCES fraud_evidence_packages(package_id)
);

-- Moderation workflow tracker
TABLE moderation_queue (
  queue_id            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id      UUID        NOT NULL REFERENCES users(id),
  championship_id     UUID        NOT NULL,
  package_id          UUID        REFERENCES fraud_evidence_packages(package_id),
  priority            TEXT        NOT NULL DEFAULT 'NORMAL',
                                  -- LOW / NORMAL / HIGH / CRITICAL
  risk_tier           TEXT        NOT NULL,
  assigned_to         UUID        REFERENCES users(id),
  status              TEXT        NOT NULL DEFAULT 'PENDING',
                                  -- PENDING / IN_REVIEW / RESOLVED
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  resolved_at         TIMESTAMPTZ,
  resolution          TEXT,       -- CLEARED / WARNING / DISQUALIFIED / ESCALATED
  resolution_notes    TEXT,
  resolution_hash     TEXT        -- Immutable after resolution
);

-- Batch anomaly reports
TABLE batch_anomaly_report (
  report_id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  report_type         TEXT        NOT NULL,  -- HOURLY / DAILY / WEEKLY
  championship_id     UUID        NOT NULL,
  new_orange_count    INT         DEFAULT 0,
  new_red_count       INT         DEFAULT 0,
  new_collusion_rings INT         DEFAULT 0,
  new_farming_flags   INT         DEFAULT 0,
  total_under_review  INT         DEFAULT 0,
  report_payload      JSONB       NOT NULL,
  generated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Model performance audit trail
TABLE model_performance_audit (
  audit_id            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  model_code          TEXT        NOT NULL,  -- M1 to M6
  model_version       TEXT        NOT NULL,
  evaluation_metric   TEXT        NOT NULL,
  metric_value        DECIMAL(8,6) NOT NULL,
  threshold_passed    BOOLEAN     NOT NULL,
  evaluated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  evaluated_by        UUID        REFERENCES users(id),
  deployment_approved BOOLEAN     DEFAULT FALSE
);

-- Indexes
CREATE INDEX idx_risk_profile_tier         ON participant_risk_profile (risk_tier);
CREATE INDEX idx_risk_profile_championship ON participant_risk_profile (championship_id);
CREATE INDEX idx_signal_log_participant    ON behavioral_signal_log (participant_id, recorded_at);
CREATE INDEX idx_signal_log_session        ON behavioral_signal_log (session_id);
CREATE INDEX idx_hr_violations_participant ON hard_rule_violation_log (participant_id, triggered_at);
CREATE INDEX idx_moderation_queue_status   ON moderation_queue (status, priority);
CREATE INDEX idx_nonce_expiry              ON submission_nonce_registry (expires_at);
CREATE INDEX idx_evidence_participant      ON fraud_evidence_packages (participant_id, created_at);
```

---

## SECTION I — API CONTRACT REGISTRY

```yaml
openapi: 3.1.0
info:
  title: Championship ML (6) Anti-Cheat Behavioral Model API — AntiGravity
  version: 1.0.0

paths:

  /anticheat/antigravity/risk/{participant_id}:
    get:
      summary: Get current risk profile for a participant
      security: [{bearerAuth: []}]
      parameters:
        - name: participant_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      responses:
        "200":
          description: Participant risk profile
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ParticipantRiskProfile'
        "403":
          description: Insufficient permissions

  /anticheat/antigravity/events/ingest:
    post:
      summary: Ingest behavioral signal event (internal service only)
      security: [{internalServiceAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/BehavioralSignalEvent'
      responses:
        "202":
          description: Event accepted and queued for processing
        "400":
          description: Schema validation failure — event dropped

  /anticheat/antigravity/hard-rules/check:
    post:
      summary: Run hard rule engine against a session payload (sync)
      security: [{internalServiceAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SessionPayloadForRuleCheck'
      responses:
        "200":
          description: Rule check result
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RuleCheckResult'

  /anticheat/antigravity/flags/active:
    get:
      summary: List all active ORANGE and RED flags (ops admin only)
      security: [{bearerAuth: []}]
      parameters:
        - name: risk_tier
          in: query
          schema:
            type: string
            enum: [ORANGE, RED, ALL]
            default: ALL
        - name: championship_id
          in: query
          schema: {type: string, format: uuid}
      responses:
        "200":
          description: Active flag list with evidence package references

  /anticheat/antigravity/moderation/queue:
    get:
      summary: Retrieve moderation queue (ops admin only)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Moderation queue entries

  /anticheat/antigravity/moderation/{queue_id}/resolve:
    post:
      summary: Resolve a moderation queue item (ops admin only)
      security: [{bearerAuth: []}]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [resolution, resolution_notes]
              properties:
                resolution:
                  type: string
                  enum: [CLEARED, WARNING, DISQUALIFIED, ESCALATED]
                resolution_notes:
                  type: string
                  minLength: 20
      responses:
        "200":
          description: Resolution recorded and actions executed
        "409":
          description: Queue item already resolved

  /anticheat/antigravity/evidence/{package_id}:
    get:
      summary: Retrieve a specific evidence package (ops admin only)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Full evidence package
        "404":
          description: Package not found

  /anticheat/antigravity/collusion/rings/active:
    get:
      summary: List active collusion rings (ops admin only)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Active collusion ring registry

  /anticheat/antigravity/batch/report/latest:
    get:
      summary: Get latest batch anomaly report (ops admin only)
      security: [{bearerAuth: []}]
      responses:
        "200":
          description: Latest batch anomaly report

components:
  schemas:
    ParticipantRiskProfile:
      type: object
      properties:
        participant_id: {type: string, format: uuid}
        risk_tier: {type: string, enum: [GREEN, YELLOW, ORANGE, RED]}
        composite_fraud_risk: {type: number, format: float}
        under_review: {type: boolean}
        farming_pattern_class: {type: string}
        last_evaluated_at: {type: string, format: date-time}

    BehavioralSignalEvent:
      type: object
      required: [participant_id, session_id, championship_id,
                 signal_code, signal_value, source_service]
      properties:
        participant_id: {type: string, format: uuid}
        session_id: {type: string, format: uuid}
        championship_id: {type: string, format: uuid}
        signal_code:
          type: string
          pattern: "^BS-(T|I|N|P|S|AG)-[0-9]{2}$"
        signal_value: {type: number}
        source_service: {type: string}

    SessionPayloadForRuleCheck:
      type: object
      required: [participant_id, session_id, championship_id, signals]
      properties:
        participant_id: {type: string, format: uuid}
        session_id: {type: string, format: uuid}
        championship_id: {type: string, format: uuid}
        submission_nonce: {type: string}
        signals:
          type: object
          description: Map of signal_code to signal_value

    RuleCheckResult:
      type: object
      properties:
        all_rules_passed: {type: boolean}
        violations:
          type: array
          items:
            type: object
            properties:
              rule_code: {type: string}
              action: {type: string}
              score_invalidated: {type: boolean}

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
  title: AntiGravity Anti-Cheat Behavioral Model Events
  version: 1.0.0

channels:

  anticheat.flag.raised:
    publish:
      message:
        payload:
          type: object
          required: [participant_id, risk_tier, threat_ids, flagged_at]
          properties:
            participant_id:    {type: string}
            championship_id:   {type: string}
            risk_tier:         {type: string}
            threat_ids:        {type: array, items: {type: string}}
            composite_score:   {type: number}
            flagged_at:        {type: string, format: date-time}

  anticheat.flag.cleared:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:    {type: string}
            queue_id:          {type: string}
            cleared_by:        {type: string}
            cleared_at:        {type: string, format: date-time}

  anticheat.flag.disqualified:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:    {type: string}
            championship_id:   {type: string}
            package_id:        {type: string}
            disqualified_by:   {type: string}
            disqualified_at:   {type: string, format: date-time}

  anticheat.collusion.ring.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            ring_id:           {type: string}
            member_count:      {type: integer}
            ring_score:        {type: number}
            detected_at:       {type: string, format: date-time}

  anticheat.hard.rule.violated:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:    {type: string}
            session_id:        {type: string}
            rule_code:         {type: string}
            action_taken:      {type: string}
            triggered_at:      {type: string, format: date-time}

  anticheat.farming.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id:    {type: string}
            farming_score:     {type: number}
            cycle_count:       {type: integer}
            detected_at:       {type: string, format: date-time}

  anticheat.evidence.package.created:
    publish:
      message:
        payload:
          type: object
          properties:
            package_id:        {type: string}
            participant_id:    {type: string}
            risk_tier:         {type: string}
            package_hash:      {type: string}
            created_at:        {type: string, format: date-time}
```

---

## SECTION K — MICROSERVICE ARCHITECTURE

```
SERVICE: anticheat-behavioral-service
  Language   : Python 3.11 (R1 compliant)
  Framework  : FastAPI
  Port       : 8031
  Stack      : ECOSKILLER R1 — Redis 7, PostgreSQL 15, LightGBM,
               PyTorch Geometric, scikit-learn

RESPONSIBILITIES:
  1. Ingest behavioral signals from API gateway hook + WebSocket bus
  2. Execute Hard Rule Engine (Section G) — synchronous
  3. Run real-time ML models M1, M2, M4 — asynchronous
  4. Run batch ML models M3, M5 — hourly cron
  5. Compute M6 composite risk score
  6. Enforce risk tier actions (rank hold, session lock)
  7. Generate Evidence Packages (Section F)
  8. Manage moderation queue
  9. Expose REST API (Section I)
  10. Emit events (Section J)
  11. Interface with ranking engine CML6-RANK (Section A)

INTERNAL INTERFACES:
  → ranking-engine-service:8030
      POST /rankings/antigravity/events/flag (set fraud_hold)
  → notification-service:8010
      POST /notify (ops alert on RED tier)
  → ops-console-service:9000
      POST /ops/alerts (moderation queue push)

HEALTH CHECK:
  GET /health
  Checks: DB reachable, Redis reachable,
          All ML models loaded and responsive

DOCKER BUILD:
  File: /backend/services/anticheat_behavioral_service/Dockerfile
  Base: python:3.11-slim
  CMD : uvicorn main:app --host 0.0.0.0 --port 8031

KUBERNETES:
  Name        : anticheat-behavioral-service
  Internal URL: http://anticheat-behavioral-service:8031
  Namespace   : ecoskiller-{env}
  Replicas    : Min 2, Max 10 (HPA on CPU > 60%)
```

---

## SECTION L — UI SCREENS SPECIFICATION

```
SCREEN AC-01: OPS ADMIN — FRAUD FLAG DASHBOARD
  Route   : /ops/championship/antigravity/anticheat/flags
  Access  : Super Admin + Ops Admin only (MFA enforced — R40)
  Purpose : Central moderation command center
  Components:
    - Risk tier filter tabs (ALL / RED / ORANGE / YELLOW)
    - Participant flag cards showing:
        participant_id (masked display name)
        risk_tier badge (color-coded)
        composite_fraud_risk gauge
        triggered threat IDs
        time since flagged
        assigned moderator
    - Bulk assign to moderator action
    - Priority escalation control
    - "View Evidence Package" CTA → opens AC-02
    - Real-time counter: Active RED / ORANGE / YELLOW
  Refresh : WebSocket live update on new flags

SCREEN AC-02: EVIDENCE PACKAGE VIEWER
  Route   : /ops/championship/antigravity/anticheat/evidence/{package_id}
  Access  : Super Admin + Ops Admin only
  Purpose : Full evidence review before moderation decision
  Components:
    - Package header: participant_id, risk_tier, package_hash
    - Model scores breakdown (M1–M6 bar chart)
    - Top 10 contributing behavioral signals (ranked list)
    - Hard rule violations list
    - 90-day rank trajectory chart
    - AntiGravity multiplier activation timeline
    - Event timeline (last 50 events, filterable)
    - Peer interaction subgraph visualization
    - Mentor override history table
    - Device/network summary (hashed, no raw PII)
    - Human-readable summary (LLM — R28-3 generated)
    - Resolution panel: CLEARED / WARNING / DISQUALIFIED / ESCALATED
    - Notes field (minimum 20 characters required)
    - "Confirm Resolution" button (one-time, irreversible)
    - Full audit log of all actions on this package

SCREEN AC-03: COLLUSION RING MONITOR
  Route   : /ops/championship/antigravity/anticheat/rings
  Access  : Super Admin only
  Purpose : Visualize and manage detected collusion rings
  Components:
    - Interactive participant graph visualization
    - Ring_id list with member count + ring_score
    - Member profiles (masked IDs) + their individual risk scores
    - Match history matrix between ring members
    - Peer scoring reciprocity heatmap
    - Status controls: ACTIVE / UNDER_REVIEW / CLEARED / CONFIRMED
    - Assign investigator per ring

SCREEN AC-04: ANTIGRAVITY FARMING MONITOR
  Route   : /ops/championship/antigravity/anticheat/farming
  Access  : Ops Admin only
  Purpose : Monitor AntiGravity multiplier exploitation patterns
  Components:
    - Participants sorted by BS-AG-03 (multiplier activation frequency)
    - Rank suppression-surge cycle chart per participant
    - BS-AG-05 regularity score indicator
    - "Suspend Multiplier" action per participant (pending review)
    - Farming pattern class badge (NATURAL / SUSPICIOUS / FARMING)

SCREEN AC-05: PARTICIPANT SELF-SERVICE — FLAG STATUS
  Route   : /app/championship/antigravity/my-status
  Access  : Authenticated participant (own data only)
  Purpose : Transparent communication to participant under review
  Components:
    - Review status banner: "Your account is under routine review"
      (No specific threat details disclosed to participant)
    - Estimated review timeline
    - Appeal submission button → links to appeals workflow (R40 — T16)
    - Activity restrictions during review period (if any)
    - Contact support link

SCREEN AC-06: BATCH ANOMALY REPORT VIEWER
  Route   : /ops/championship/antigravity/anticheat/reports
  Access  : Ops Admin only
  Components:
    - Report type selector (HOURLY / DAILY / WEEKLY)
    - Key metrics: new flags, rings, farming detections
    - Trend chart: flag volume over time
    - Model performance summary (last evaluation scores)
    - Export to CSV
```

---

## SECTION M — PERMISSION & ROLE MATRIX

```
ROLE              │ VIEW OWN  │ VIEW ALL  │ VIEW     │ RESOLVE  │ SUSPEND  │ VIEW
                  │ RISK TIER │ FLAGS     │ EVIDENCE │ QUEUE    │ MULTIPLR │ RINGS
──────────────────┼───────────┼───────────┼──────────┼──────────┼──────────┼──────
participant       │ YES       │ NO        │ NO       │ NO       │ NO       │ NO
recruiter         │ NO        │ NO        │ NO       │ NO       │ NO       │ NO
institute_admin   │ NO        │ own inst  │ NO       │ NO       │ NO       │ NO
ops_admin         │ YES       │ YES       │ YES      │ YES      │ YES      │ YES
super_admin       │ YES       │ YES       │ YES      │ YES      │ YES      │ YES
internal_service  │ NO        │ NO        │ NO       │ NO       │ NO       │ NO
                  │ (ingest)  │           │          │          │          │

NOTE: internal_service role has ONLY event ingest rights via
      X-Internal-Service-Key header authentication (Section I).
      No read rights for any moderation or evidence data.
```

---

## SECTION N — INTERN EXECUTION GUIDE

```
OBJECTIVE: Run anti-cheat behavioral service locally.
INTERN ROLE: Python Backend Developer
SKILL LEVEL: Intermediate Python, Basic ML

REQUIRED TOOLS:
  Python 3.11+, PostgreSQL 15, Redis 7, Docker

STEP-BY-STEP:

Step 1 — Navigate to service:
  cd /backend/services/anticheat_behavioral_service/

Step 2 — Install dependencies:
  pip install -r requirements.txt
  (Includes: fastapi, uvicorn, lightgbm, scikit-learn,
   torch-geometric, alembic, redis, asyncpg)

Step 3 — Configure environment:
  cp .env.example .env
  Fill in:
    DB_HOST, REDIS_HOST, MODEL_PATH=/models/anticheat/
    RANKING_ENGINE_URL=http://localhost:8030

Step 4 — Load dev ML models:
  python scripts/load_models.py --env dev
  Expected:
    "Model M1 loaded: isolation_forest_v1.joblib"
    "Model M2 loaded: bot_classifier_v1.joblib"
    "Model M3 loaded: collusion_gnn_v1.pt"
    "Model M4 loaded: identity_svm_v1.joblib"
    "Model M5 loaded: farming_lstm_v1.pt"
    "Model M6 loaded: composite_scorer_v1.joblib"

Step 5 — Run DB migrations:
  alembic upgrade head
  Expected: All anti-cheat tables created with indexes.

Step 6 — Start service:
  uvicorn main:app --reload --port 8031
  Expected: "Uvicorn running on http://127.0.0.1:8031"

Step 7 — Verify health:
  curl http://127.0.0.1:8031/health
  Expected: {"status": "healthy", "models_loaded": 6}

Step 8 — Test hard rule engine:
  POST http://127.0.0.1:8031/anticheat/antigravity/hard-rules/check
  Body: (see test_payloads/hr_test_01.json in repo)
  Expected: {"all_rules_passed": true, "violations": []}

SUCCESS CONDITIONS:
  ✔ Service running on port 8031
  ✔ All 6 models loaded
  ✔ DB tables created
  ✔ Health endpoint returns 200
  ✔ Hard rule check endpoint responds
  ✔ No runtime exceptions in logs

FAILURE → STOP EXECUTION
```

---

## SECTION O — ML MODEL SPECIFICATION & GOVERNANCE

```
MODEL PERFORMANCE THRESHOLDS (LOCKED — v1.0):

  M1 Isolation Forest
    Metric    : Precision at 1% FPR
    Threshold : ≥ 0.85
    Below     : REJECT deployment → retain previous version

  M2 Bot Classifier (LightGBM)
    Metrics   : F1-score ≥ 0.92 AND AUC-ROC ≥ 0.96
    Below     : REJECT deployment

  M3 Collusion GNN
    Metric    : Collusion recall ≥ 0.80
    Below     : REJECT deployment

  M4 Identity SVM
    Metric    : True-positive rate on known substitutions ≥ 0.78
    Below     : REJECT deployment

  M5 Farming LSTM
    Metric    : FARMING class precision ≥ 0.88
    Below     : REJECT deployment

  M6 Composite Scorer
    Metric    : Brier score ≤ 0.12
    Below     : REJECT deployment

FAIRNESS REQUIREMENTS (R51 Compliance):
  All models must pass before production deployment:
    ✔ Gender demographic parity difference < 0.05
    ✔ Region demographic parity difference < 0.05
    ✔ Age group demographic parity difference < 0.05
  Failure in any fairness check → REJECT model → alert ML team

HUMAN APPROVAL REQUIRED (M5 — AI Model Reality Law):
  No model may be deployed to production without:
    ✔ Performance threshold passed (above)
    ✔ Fairness check passed (above)
    ✔ ML team sign-off log entry
    ✔ Ops admin deployment approval log entry
  All approvals stored in: model_performance_audit table

RETRAIN SCHEDULES:
  M1  Weekly (Monday 02:00 UTC)
  M2  Weekly (Monday 02:00 UTC)
  M3  Bi-weekly (1st and 15th, 03:00 UTC)
  M4  Continuous rolling per-participant baseline update
  M5  Weekly (Tuesday 02:00 UTC)
  M6  Weekly (Wednesday 02:00 UTC)

EXPLAINABILITY (R28-3):
  Method   : SHAP values for M1, M2, M5, M6
             Subgraph attention weights for M3
             SVM support vector analysis for M4
  LLM Role : Text generation ONLY for human_readable_summary
             in Evidence Packages (EP-15)
             LLM NEVER computes risk scores or makes decisions

MONTHLY COST ESTIMATE (R28 Compliance):
  All 6 models at 1M active participants: ~$35–$60/month
  Human review infrastructure (Ops): separate operational cost
```

---

## SECTION P — OBSERVABILITY & MONITORING

```
METRICS (Prometheus — exported from port 8031):

  anticheat_events_ingested_total            (counter)
  anticheat_hard_rule_violations_total       (counter, labels: rule_code)
  anticheat_risk_tier_distribution           (gauge, labels: tier)
  anticheat_model_inference_latency_ms       (histogram, labels: model_id)
  anticheat_evidence_packages_created_total  (counter)
  anticheat_moderation_queue_depth           (gauge)
  anticheat_collusion_rings_active           (gauge)
  anticheat_farming_flags_active             (gauge)
  anticheat_false_positive_cleared_total     (counter)
  anticheat_disqualifications_total          (counter)
  anticheat_pipeline_errors_total            (counter, labels: stage)

GRAFANA ALERTS:
  ALERT: anticheat_risk_tier RED count > 20 in 1h
         → PagerDuty: Critical ops response required
  ALERT: anticheat_hard_rule_violations HR-01 > 50 in 30m
         → Slack: Potential bot wave attack
  ALERT: anticheat_pipeline_errors_total > 5 in 5m
         → Slack: Pipeline failure investigation
  ALERT: anticheat_moderation_queue_depth > 100
         → Slack: Moderation queue overflow — assign more reviewers
  ALERT: anticheat_collusion_rings_active > 10
         → PagerDuty: Mass collusion ring event

LOGGING (Loki):
  Log level     : INFO in prod, DEBUG in dev
  PII policy    : participant_id ONLY in logs (no names, emails)
  Retention     : 90 days hot, 7 years cold archive (R60)

TRACING (Jaeger):
  Full trace per behavioral event through all pipeline stages
  Trace IDs correlated with evidence package IDs
```

---

## SECTION Q — DOJO SAAS INTEGRATION COMPLIANCE

```
This agent directly implements the following DOJO SAAS locked sections:

DOJO T9 — Collusion & Manipulation Detection Lock
  ✔ THR-COL-01 to THR-COL-05 threat classes registered
  ✔ Model M3 (Graph NN) detects collusion rings
  ✔ Reciprocal high scoring detection (BS-S-01)
  ✔ Rating inflation ring detection (BS-S-05)
  ✔ Match farming pattern detection (BS-S-04)
  ✔ Mentor favoritism detection (BS-S-07, BS-S-08)
  ✔ Flagged matches halt belt counting (hard rule HR-10)

DOJO T10 — Behavioral Safety Lock
  ✔ Forced session exit capability via fraud_hold mechanism
  ✔ Emergency RED tier → immediate session lock
  ✔ Mentor intervention hook via anticheat.flag.raised event
  ✔ Cooldown enforcement via risk_tier = ORANGE score submission lock

DOJO T15 — Economic Abuse Controls Lock
  ✔ THR-ECO-01 to THR-ECO-04 threat classes registered
  ✔ Referral loop farming detection (BS-S-06)
  ✔ AntiGravity multiplier farming detection (THR-ECO-03, M5)
  ✔ Multi-account farming detection (THR-ID-02, M4)
  ✔ Fake tournament entry detection (THR-ECO-04)

DOJO P1 — Security Hardening Lock
  ✔ Submission nonce registry (HR-02 replay prevention)
  ✔ Concurrent session detection (HR-03)
  ✔ VPN/Proxy/TOR detection (HR-04)
  ✔ API rate limit burst detection (HR-06, BS-N-09)
  ✔ Device fingerprint change detection (HR-09, BS-N-05, BS-N-06)
  ✔ TLS fingerprint tracking (BS-N-10)

DOJO T3 — Rater Calibration Lock
  ✔ Mentor override frequency monitoring (BS-S-07)
  ✔ Mentor concentration detection (BS-S-08)
  ✔ Mentor favoritism threat class (THR-COL-04)
  ✔ Mentor-participant collusion detection via M3 graph model

DOJO SCORING GOVERNANCE (Section F)
  ✔ Variance anomaly detection (M1 Isolation Forest)
  ✔ Audit log on all overrides (moderation_queue + evidence packages)
  ✔ Score invalidation on hard rule violations (HR-01, HR-02)
```

---

## SECTION R — ENFORCEMENT RULES

```
ENFORCEMENT RULE ACE-01:
  No championship round result may be finalized for a participant
  with active risk_tier = ORANGE or RED.
  Round results remain provisional until moderation resolves.
  Failure → STOP ROUND FINALIZATION → emit: round_result_blocked

ENFORCEMENT RULE ACE-02:
  No ML model may be deployed to production without all performance
  thresholds passed AND human approval log recorded (Section O).
  Failure → STOP DEPLOYMENT → report: MODEL_DEPLOYMENT_BLOCKED

ENFORCEMENT RULE ACE-03:
  No evidence package may be modified after creation.
  Amendment requires new package with predecessor_id reference.
  Attempted modification → STOP → report: EVIDENCE_TAMPERING_ATTEMPT

ENFORCEMENT RULE ACE-04:
  Moderation resolution notes must be minimum 20 characters.
  Resolution without notes → REJECT API call → 400 Bad Request.
  Purpose: Prevent lazy "bulk clear" without review.

ENFORCEMENT RULE ACE-05:
  The AntiGravity ×1.15 multiplier is suspended for any participant
  with farming_pattern_class = FARMING regardless of risk_tier.
  Multiplier cannot be re-enabled without explicit ops admin action
  following a CLEARED moderation resolution.

ENFORCEMENT RULE ACE-06:
  All 6 ML models must be loaded and health-check responsive
  before the service may accept behavioral signal ingestion.
  Partial model availability → service enters DEGRADED mode →
  hard rule engine only → emit: anticheat_degraded_mode_active

ENFORCEMENT RULE ACE-07:
  Bot classification (M2) producing CONFIRMED_BOT result for a
  participant immediately triggers RED tier regardless of M6.
  M6 composite score does not override a CONFIRMED_BOT signal.

ENFORCEMENT RULE ACE-08:
  Collusion ring members (M3 ring_score ≥ 0.70) are all escalated
  to ORANGE minimum regardless of individual M6 scores.
  A ring is a collective finding — individual scores are insufficient
  to clear membership.

ENFORCEMENT RULE ACE-09:
  Behavioral signal data is retained for minimum 7 years (R60).
  Any deletion request outside of GDPR right-to-erasure workflow
  → STOP → report: ILLEGAL_DATA_DELETION_ATTEMPT

ENFORCEMENT RULE ACE-10:
  The hard rule engine (Section G) runs BEFORE all ML models.
  Any hard rule violation produces an immediate flag that cannot
  be overridden or suppressed by ML model outputs.
  Hard rules are the system's non-negotiable integrity floor.

Violation of any enforcement rule above:
  → STOP EXECUTION
  → REPORT ANTICHEAT_ENFORCEMENT_VIOLATION
  → NO CHAMPIONSHIP RESULT CLAIM PERMITTED
```

---

## SECTION S — CONTRACT GATE REQUIREMENTS (R49 Compliance)

```
Before deployment of this agent the following must be validated
by the Contract Validator CLI (R49):

  ✔ behavioral_signal_event schema validated vs AsyncAPI registry
  ✔ All BS signal codes (BS-T-01 to BS-AG-05) registered
  ✔ All THR-XX-XX threat IDs registered in Event Schema Registry
  ✔ API contract registered in API Contract Registry
  ✔ Permission → Screen matrix registered (Section M)
  ✔ Role → Widget matrix registered
  ✔ All 6 ML models loaded with performance thresholds met
  ✔ All 10 hard rules tested and passing (QA test suite R50)
  ✔ Integration with ranking engine (CML6-RANK) tested end-to-end
  ✔ Evidence package hash integrity test passing
  ✔ Nonce registry TTL cleanup job running

Absence of any validation → STOP EXECUTION
```

---

## SECTION T — PRODUCTION READINESS CHECKLIST

```
Before this agent may be declared Production-Ready (L3 compliant):

INFRASTRUCTURE:
  ✔ anticheat-behavioral-service pods STATUS = Running (min 2 replicas)
  ✔ Redis Streams consumer group active
  ✔ PostgreSQL anti-cheat tables created and indexed
  ✔ All 6 ML models loaded and /health returning 200
  ✔ Nonce registry TTL cleanup cron job active

ML MODELS:
  ✔ M1 Precision ≥ 0.85 at 1% FPR
  ✔ M2 F1-score ≥ 0.92, AUC-ROC ≥ 0.96
  ✔ M3 Collusion recall ≥ 0.80
  ✔ M4 True-positive rate ≥ 0.78
  ✔ M5 FARMING precision ≥ 0.88
  ✔ M6 Brier score ≤ 0.12
  ✔ All fairness checks passed (demographic parity < 0.05)
  ✔ Human approval logs in model_performance_audit table

CONTRACTS:
  ✔ R49 Contract Validator passed
  ✔ R50 QA Tests generated and all passing
  ✔ API contract registered

INTEGRATIONS:
  ✔ ranking-engine-service (CML6-RANK) fraud_hold interface tested
  ✔ notification-service ops alert interface tested
  ✔ ops-console-service moderation queue push tested

SECURITY:
  ✔ Internal service auth enforced on ingest endpoint
  ✔ Super Admin MFA enforced on ops screens
  ✔ No participant PII in any ML model features or log payloads
  ✔ Evidence package hash verification tested
  ✔ Nonce replay attack prevention tested

OBSERVABILITY:
  ✔ All Prometheus metrics exporting on port 8031
  ✔ Grafana dashboards deployed with all alerts configured
  ✔ Loki logs flowing with PII-safe format
  ✔ Jaeger traces active with stage correlation

Until all conditions above are met:
  Agent Status = "ARTIFACTS GENERATED — NOT DEPLOYED"
  NOT: "Anti-Cheat Engine Operational"
```

---

## SECTION U — FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  AGENT SEAL : ANTIGRAVITY-ANTICHEAT-AGENT-v1.0                               ║
║  FILE       : Championship_ML_6_Anti-Cheat_Behavioral_Model.md               ║
║  DOMAIN     : SKILL & COMPETITION CORE → ANTIGRAVITY                         ║
║  PARENT     : ECOSKILLER MASTER EXECUTION PROMPT v12.0+                      ║
║               DOJO SAAS LOCKED & SEALED SPEC v1.0                            ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  COMPLIANCE MAP:                                                               ║
║  ✔ R1   Technology Stack Lock (Python, FastAPI, Redis, PostgreSQL, LightGBM) ║
║  ✔ R2   Domain Data Models (8 tables, all indexes defined)                   ║
║  ✔ R3   OpenAPI 3.1 Contract (Section I — 9 endpoints)                       ║
║  ✔ R4   AsyncAPI Event Schema (Section J — 7 event channels)                 ║
║  ✔ R5   Workflow State Machines (pipeline stages Section E)                  ║
║  ✔ R12  AI Model Specification (Section O — 6 models, all governed)          ║
║  ✔ R24  Execution Skill Alignment (intern guide Section N)                   ║
║  ✔ R26  Intern Line-Level Execution Instructions                             ║
║  ✔ R28  Intelligence Cost Optimization (Traditional ML, LLM text-only)       ║
║  ✔ R29  Modern UI Screens (Section L — 6 screens)                            ║
║  ✔ R38  Bug & Failure Registry (20 threat classes, 10 hard rules)            ║
║  ✔ R39  Core Inbuilt Platform Tools (anti-cheat, fraud, moderation)          ║
║  ✔ R40  Internal Admin & Ops Console (Sections L, M)                         ║
║  ✔ R49  Contract Validator Compliance (Section S)                            ║
║  ✔ R51  Anti-Spam & Platform Abuse Prevention (52 behavioral signals)        ║
║  ✔ R60  Long-Term Archival (7-year retention, immutable evidence)            ║
║  ✔ M5   AI Model Reality Law (human approval required, logged)               ║
║  ✔ L2   AI Operational Restriction (no real cloud deployment claimed)        ║
║  ✔ DOJO T9   Collusion & Manipulation Detection Lock                         ║
║  ✔ DOJO T10  Behavioral Safety Lock                                          ║
║  ✔ DOJO T15  Economic Abuse Controls Lock                                    ║
║  ✔ DOJO P1   Security Hardening Lock                                         ║
║  ✔ DOJO T3   Rater Calibration Lock (mentor favoritism detection)            ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  CONTENTS SUMMARY:                                                            ║
║  Section A  : Agent Identity & Constitutional Purpose                        ║
║  Section B  : Anti-Cheat Threat Taxonomy (20 threats, 6 classes)             ║
║  Section C  : Behavioral Signal Corpus (52 signals, 6 classes)               ║
║  Section D  : Detection Model Architecture (6 ML models)                     ║
║  Section E  : Detection Pipeline (3 modes, 12 stages)                        ║
║  Section F  : Evidence Package Specification (18 fields, immutable)          ║
║  Section G  : Hard Rule Engine (10 rules, deterministic)                     ║
║  Section H  : Database Schema (8 tables, full indexes)                       ║
║  Section I  : API Contract Registry (OpenAPI 3.1, 9 endpoints)               ║
║  Section J  : Event Schema Registry (AsyncAPI, 7 channels)                   ║
║  Section K  : Microservice Architecture                                      ║
║  Section L  : UI Screens (6 screens fully specified)                         ║
║  Section M  : Permission & Role Matrix                                       ║
║  Section N  : Intern Execution Guide (step-by-step)                          ║
║  Section O  : ML Model Specification & Governance                            ║
║  Section P  : Observability & Monitoring                                     ║
║  Section Q  : DOJO SaaS Integration Compliance                               ║
║  Section R  : Enforcement Rules (10 rules)                                   ║
║  Section S  : Contract Gate Requirements                                     ║
║  Section T  : Production Readiness Checklist                                 ║
║  Section U  : Final Seal                                                     ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  STATUS    : LOCKED · SEALED · DETERMINISTIC                                 ║
║  MUTATION  : Add-only via version bump to ANTIGRAVITY-ANTICHEAT-AGENT-v2.0  ║
║  INTERP.   : NONE                                                            ║
║  EXEC AUTH : Human declaration only                                          ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*ECOSKILLER — Championship ML (6) Anti-Cheat Behavioral Model Agent · AntiGravity Module · v1.0 · SEALED*
