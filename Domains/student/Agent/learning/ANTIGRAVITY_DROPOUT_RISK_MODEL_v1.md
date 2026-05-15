# 🔴 ANTIGRAVITY — DROPOUT RISK MODEL (DRM) v1.0
## ECOSKILLER CHAMPIONSHIP ADVANCED + PARENT PREDICTIVE AI ML LAYER
### AGENT SPECIFICATION · SEALED · LOCKED · GOVERNED · DETERMINISTIC

```
╔══════════════════════════════════════════════════════════════════════════╗
║          ANTIGRAVITY DROPOUT RISK MODEL — DOCUMENT SEAL                 ║
╠══════════════════════════════════════════════════════════════════════════╣
║  DOCUMENT CLASS          : SEALED PRODUCTION ARTIFACT                   ║
║  ARTIFACT TYPE           : AGENT SYSTEM SPECIFICATION                   ║
║  EXECUTION ENGINE        : ANTIGRAVITY                                   ║
║  MUTATION POLICY         : ADD-ONLY VIA VERSION BUMP                    ║
║  EXECUTION MODE          : DETERMINISTIC                                 ║
║  INTERPRETATION          : FORBIDDEN                                     ║
║  CREATIVE DEVIATION      : FORBIDDEN                                     ║
║  ASSUMPTION FILLING      : FORBIDDEN                                     ║
║  DEFAULT BEHAVIOR        : DENY                                          ║
║  FAILURE MODE            : STOP → REPORT → NO PARTIAL OUTPUT            ║
║  ANTIGRAVITY SAFE        : CONFIRMED                                     ║
║  HUMAN OVERRIDE          : REQUIRED FOR ALL CRITICAL INTERVENTIONS      ║
║  AI AUTONOMY CEILING     : COMPUTATION + PREDICTION + ADVISORY ONLY     ║
║  CLINICAL DETERMINATION  : FORBIDDEN — DRM IS NOT A DIAGNOSTIC TOOL     ║
║  DROPOUT LABEL EXPOSURE  : FORBIDDEN IN STUDENT-FACING UI               ║
║  SHAME LANGUAGE          : FORBIDDEN — CARE + ENGAGEMENT FRAMING ONLY   ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

## ⚡ WHAT IS THE DROPOUT RISK MODEL

The **Dropout Risk Model (DRM)** is a sealed Antigravity agent that **continuously detects, scores, classifies, predicts, and triggers graduated intervention responses** for students at risk of disengaging from or permanently leaving the Ecoskiller platform.

Dropout is not a single event. It is a **staged deterioration** — a compounding sequence of micro-disengagements that, left unaddressed, terminates in permanent platform exit. The DRM monitors this deterioration in real time across seven behavioural, performance, social, and lifecycle signal dimensions, computes a composite Dropout Risk Score (DRS) per student, predicts 14-day and 30-day dropout probability, and fires the correct graduated intervention — from a gentle in-app re-engagement nudge to a Championship bracket hold to a high-urgency parent alert to an institute admin cohort escalation.

The DRM also operates as the **retention intelligence engine** for Championship Advanced. A student who drops mid-tournament, abandons qualification rounds, or stops practicing before a championship window is not just a retention problem — they are a bracket integrity problem, a parent trust problem, and a platform health problem simultaneously. The DRM treats all three consequences.

**Three laws the DRM never violates:**

1. **The word "dropout" never appears in any student-facing output.** The DRM's student UI outputs are framed entirely as re-engagement invitations and care signals.
2. **The DRM does not make clinical or psychological determinations.** It detects behavioural patterns and fires human escalation. Humans diagnose. The DRM observes and signals.
3. **Mentor authority supersedes DRM output.** Mentors may override any DRM classification with an audit-logged reason. DRM advises. Humans decide.

---

## 🔒 SECTION DRM-0 — MASTER GOVERNANCE SEAL

```
DRM_ENGINE_STATUS                   : ACTIVE · ENFORCED · LOCKED
PARENT_ML_LAYER                     : ACTIVE · PREDICTIVE · ADVISORY ONLY
CHAMPIONSHIP_ADVANCED_LAYER         : ACTIVE · TOURNAMENT + BRACKET AWARE
HUMAN_OVERRIDE_AUTHORITY            : REQUIRED FOR ALL CRITICAL INTERVENTIONS
AI_AUTONOMY_CEILING                 : COMPUTATION + PREDICTION + ADVISORY ONLY
CLINICAL_DETERMINATION              : FORBIDDEN — NOT A DIAGNOSTIC TOOL
DROPOUT_LABEL_IN_STUDENT_UI         : FORBIDDEN — CARE FRAMING MANDATORY
SHAME_OR_PRESSURE_LANGUAGE          : FORBIDDEN — ALL OUTPUT TONES GOVERNED
DROPOUT_DATA_CROSS_TENANT           : FORBIDDEN — TENANT ISOLATED
MINOR_EXTRA_PROTECTION              : ACTIVE — STAGED SAFEGUARDS FOR UNDER-18
MENTOR_OVERRIDE_SUPREMACY           : ACTIVE — MENTOR DECISION SUPERSEDES DRM ALWAYS
RECRUITER_DROPOUT_DATA_ACCESS       : FORBIDDEN — ZERO EXPOSURE
PARENT_DROPOUT_DATA_FRAMING         : CARE-FIRST · NON-ALARMING · SOLUTION-PAIRED
STREAK_SYSTEM_INTEGRATION           : ACTIVE — R95 STREAK ENGINE DATA CONSUMED
GAMIFICATION_INTEGRATION            : ACTIVE — RE-ENGAGEMENT TRIGGERS PUBLISHED
SOCIAL_LAYER_INTEGRATION            : ACTIVE — R92/R93/R94 PEER SIGNALS CONSUMED
CHAMPIONSHIP_HOLD_AUTHORITY         : ADVISORY — MENTOR BOARD CONFIRMATION REQUIRED
INTERPRETATION_AUTHORITY            : NONE
ARCHITECTURE_AUTHORITY              : LOCKED
```

**Absence of any section below → STOP EXECUTION**

---

## 🔒 SECTION DRM-1 — DROPOUT SIGNAL TAXONOMY (IMMUTABLE)

### DRM-1.1 — Signal Dimension Registry (LOCKED)

Seven signal dimensions. Immutable. Addition requires version bump.

| Dim ID | Dimension Name | Description | Primary Data Sources | Weight |
|---|---|---|---|---|
| DS-001 | Session Engagement Collapse | Drop in active session count, session duration, or daily login frequency | Analytics Engine, DailyActivity (R95) | 22% |
| DS-002 | Streak Break Severity | Streak break pattern — frequency, duration, and post-break recovery rate | StreakTracker (R95), Habit Engine | 15% |
| DS-003 | Performance Deterioration | Sustained decline in match scores, belt assessment outcomes, or drill completion | Scoring Engine, Belt Engine, Skill Engine | 18% |
| DS-004 | Social Disengagement | Withdrawal from study rooms, peer quizzes, campus groups, social feed, endorsements | R92/R93/R94 Social Layer, Peer Comparison Engine | 12% |
| DS-005 | Championship Abandonment | Pre-match exits, qualification round skips, tournament abandonment, bracket forfeit | Championship Engine, Dojo Match Engine | 16% |
| DS-006 | Economic Disengagement | Subscription cancellation signals, payment failures, benefit non-utilisation, marketplace inactivity | Billing Engine, Subscription Service | 7% |
| DS-007 | Lifecycle Transition Signal | Account state changes, profile incompleteness, onboarding abandonment, career-path drift | Identity Service, Analytics Engine | 10% |

### DRM-1.2 — Dropout Stage Classification (LOCKED)

```
STAGE-0: HEALTHY
  Definition   : No active dropout signals detected
  DRS Range    : 0–19
  Action       : Monitor only — no intervention
  Parent Alert : None
  AG Alert     : None

STAGE-1: EARLY DISENGAGEMENT
  Definition   : 1–2 signal dimensions showing mild deterioration
  DRS Range    : 20–39
  Duration Gate: Signal active ≥ 5 days
  Action       : In-app re-engagement nudge (automated)
               : Streak reminder push notification
               : Achievement suggestion surfaced
  Parent Alert : None
  AG Alert     : None — internal log only

STAGE-2: ACTIVE RISK
  Definition   : 2–3 signal dimensions with moderate deterioration OR 1 dimension
                 with severe deterioration
  DRS Range    : 40–59
  Duration Gate: Signal active ≥ 7 days
  Action       : Personalised re-engagement campaign (automated)
               : Mentor notification (advisory — no mandatory action)
               : Peer connection suggestion
               : Championship incentive surface (if championship upcoming)
  Parent Alert : Soft advisory card in dashboard (no push)
  AG Alert     : AG-LOW (logged — no escalation)

STAGE-3: HIGH RISK
  Definition   : 3–4 signal dimensions deteriorating OR championship abandonment
                 pattern + social disengagement combined
  DRS Range    : 60–74
  Duration Gate: Signal active ≥ 10 days
  Action       : Mentor intervention required (not optional)
               : Personalised recovery plan generated
               : Belt hold advisory (mentor must confirm)
               : Parent notification push (care-framed)
               : Re-engagement reward unlocked (gamification)
  Parent Alert : AG-MED push + advisory card with parent action
  AG Alert     : AG-MED (mentor action required within 48 hours)

STAGE-4: CRITICAL RISK
  Definition   : 5+ signal dimensions deteriorating OR complete session absence
                 for 14+ days after prior high engagement OR championship
                 abandonment + performance collapse + social exit simultaneously
  DRS Range    : 75–89
  Duration Gate: Signal active ≥ 14 days (or immediate on 14-day absence)
  Action       : Mentor board escalation
               : Parent high-urgency alert (care-first, non-alarming)
               : Institute admin notification
               : Championship bracket advisory hold
               : Personal recovery roadmap generated
               : Re-entry onboarding sequence triggered
  Parent Alert : AG-HIGH push + care-framed escalation card + mentor contact detail
  AG Alert     : AG-HIGH (human intervention required within 24 hours)

STAGE-5: IMMINENT EXIT
  Definition   : All 7 signal dimensions in terminal deterioration OR account
                 deletion request in progress OR zero activity for 30+ days
                 with no re-engagement response to Stages 1–4 interventions
  DRS Range    : 90–100
  Duration Gate: Immediate on trigger
  Action       : Urgent mentor board intervention
               : Platform re-entry campaign (last-mile retention)
               : Parent direct escalation (with consent)
               : Institute admin alert
               : Championship removal from active roster (with audit)
               : Win-back sequence initiated
               : Exit survey triggered (optional, non-pressuring)
  Parent Alert : AG-HIGH (critical — immediate)
  AG Alert     : AG-CRIT (15-minute human response required)

STAGE-R: RECOVERED
  Definition   : DRS drops from ≥ STAGE-2 back to STAGE-0 and sustains
                 for 7+ consecutive days
  Action       : Achievement: "Welcome back" celebration trigger
               : Streak restoration bonus
               : Mentor positive feedback prompt
               : Parent positive update card
  AG Alert     : None — positive event published to Achievement Engine
```

### DRM-1.3 — Signal Evidence Rules (LOCKED)

```
EVIDENCE_WINDOW              : Rolling 21-day observation window per signal
EVIDENCE_THRESHOLD           : Signal confirmed after 3+ qualifying events in window
TRANSIENT_EXCLUSION          : Single event does not activate signal — logged only
SEVERITY_AMPLIFIER           : Championship window active → all signal weights ×1.3
RECOVERY_DEFINITION          : 3+ consecutive qualifying healthy events → signal deactivated
RECOVERY_GRACE_PERIOD        : 7 days of sustained recovery before DRS recalculated downward
CHRONIC_FLAG                 : Stage-3+ sustained > 30 days without recovery → CHRONIC
EXTERNAL_FACTOR_HOLD         : If RC-008 (external life factor) confirmed by mentor →
                               DRM signals logged but interventions paused for 14 days
                               (compassion hold — human decides pace of re-engagement)
```

---

## 🔒 SECTION DRM-2 — DROPOUT RISK SCORE (DRS) COMPUTATION MODEL

### DRM-2.1 — DRS Formula (LOCKED)

```
DROPOUT RISK SCORE (DRS) — 0 to 100 scale
Computed per student per computation cycle

DRS = Σ (dimension_score[i] × weight[i]) × context_amplifier × persistence_multiplier

Where:
  dimension_score[i] = normalised deterioration score per dimension DS-001 to DS-007
                       (0 = healthy, 100 = terminal deterioration)

  weight[i]          = DS-001:0.22, DS-002:0.15, DS-003:0.18, DS-004:0.12,
                       DS-005:0.16, DS-006:0.07, DS-007:0.10

  context_amplifier  = 1.0 (normal)
                       1.3 (championship window active — 14 days before tournament)
                       1.2 (belt promotion window active — within 21 days of eligibility)
                       1.15 (parent subscription renewal imminent — 30 days)
                       Amplifiers are additive up to maximum 1.5 combined

  persistence_multiplier = 1.0 + (days_at_current_stage / 30) × 0.2
                           Capped at 1.4 (prevents runaway scoring)

OUTPUT CLASSIFICATION:
  DRS 0–19   : STAGE-0 HEALTHY
  DRS 20–39  : STAGE-1 EARLY DISENGAGEMENT
  DRS 40–59  : STAGE-2 ACTIVE RISK
  DRS 60–74  : STAGE-3 HIGH RISK
  DRS 75–89  : STAGE-4 CRITICAL RISK
  DRS 90–100 : STAGE-5 IMMINENT EXIT

CONFIDENCE SCORE: Required alongside every DRS
  confidence_floor  = 60% — DRS suppressed below this (insufficient evidence)
  confidence_low    = 60–74% — flagged LOW CONFIDENCE in all outputs
  confidence_high   = 75–100% — standard display

DIMENSION SCORE COMPUTATION (per DS-00X):
  DS-001 (Session Engagement Collapse):
    base = (prior_30d_avg_sessions - current_7d_sessions) / prior_30d_avg_sessions × 100
    duration_penalty = IF avg_session_duration < 50% of prior → add 20 points
    login_gap_penalty = IF max_gap_between_logins > 5 days → add 15 points
    capped: 0–100

  DS-002 (Streak Break Severity):
    streak_loss = (max_streak - current_streak) / max_streak × 100
    recovery_rate = post_break_sessions_in_7d / 7 × 100 (inverse — low recovery = high score)
    chronic_break = IF streak_broken ≥ 3 times in 21 days → add 25 points
    capped: 0–100

  DS-003 (Performance Deterioration):
    match_score_delta = (prior_14d_avg_score - current_7d_avg_score) / prior_14d_avg_score × 100
    belt_fail_streak  = belt_fails_in_21d × 15 (max 45 addition)
    drill_completion  = (1 - drill_completion_rate_7d) × 30
    capped: 0–100

  DS-004 (Social Disengagement):
    study_room_exit   = IF study_rooms_left > study_rooms_joined in 14d → 30 points
    group_inactivity  = (1 - group_posts_7d / group_posts_prior_7d) × 30
    peer_withdraw     = IF peer_interactions_7d < 20% of prior_14d_avg → 40 points
    capped: 0–100

  DS-005 (Championship Abandonment):
    pre_match_exits   = pre_match_exit_count_21d × 20 (max 60)
    bracket_forfeits  = bracket_forfeits_21d × 30 (max 60)
    qual_skips        = qualification_skips_21d × 15 (max 45)
    capped: 0–100

  DS-006 (Economic Disengagement):
    sub_cancel_signal = IF subscription cancellation initiated → 80 points
    payment_fail      = payment_failures_21d × 25 (max 50)
    benefit_non_use   = IF entitled features unused for 21d → 30 points
    capped: 0–100

  DS-007 (Lifecycle Transition):
    profile_incomplete = (required_fields_missing / total_required) × 40
    career_path_drift  = IF career_path_last_changed < 7d AND prior_stability > 60d → 30
    account_state_flag = IF account restricted or payment_hold → 40 points
    capped: 0–100
```

### DRM-2.2 — Computation Cycle Schedule (LOCKED)

```
CYCLE-1: REAL-TIME STREAMING (Kafka Consumer)
  Trigger: Any qualifying event from DRM event registry
  Latency SLA: DRS updated within 60 seconds of event
  Scope: Affected user + relevant dimension score
  Output: Updated dimension score → recompute DRS → evaluate stage change

CYCLE-2: HOURLY MICRO-BATCH (Airflow)
  Scope: All users with DRS > 0 (active risk users)
  Output: Refresh dimension scores + DRS for all at-risk users
  Priority: STAGE-3 and above recomputed first

CYCLE-3: DAILY FULL RECOMPUTE (Airflow — 03:00 AM tenant timezone)
  Scope: All active users platform-wide
  Output: Full DRS recompute + persistence multiplier update + context amplifier apply
  Special: CHRONIC flag evaluation runs in this cycle

CYCLE-4: WEEKLY DEEP ANALYSIS (Airflow — Sunday 04:00 AM)
  Scope: All users — cohort-level dropout risk pattern analysis
  Output: Cohort dropout health score, structural dropout pattern detection
  Special: Institute admin reports generated, curriculum team signals fired if structural

CYCLE-5: PARENT PREDICTIVE ML BATCH (Airflow — Daily 05:00 AM)
  Scope: All students with active parent accounts
  Output: 14-day and 30-day dropout probability projections per child
  Model: DRM Predictive Layer (LSTM + Random Forest ensemble)
  Confidence: Required — suppressed below 60%
```

---

## 🔒 SECTION DRM-3 — AGENT ARCHITECTURE

### DRM-3.1 — Agent Identity (LOCKED)

```
AGENT_NAME              : DropoutRiskAgent (DRA)
AGENT_CLASS             : Always-On Background Compute + Event-Driven + Batch Agent
AGENT_TYPE              : Stateful Streaming + Scheduled Hybrid
AGENT_RUNTIME           : Kubernetes Deployment (streaming) + Airflow (batch)
AGENT_LANGUAGE          : Python 3.11 + FastAPI (serve layer)
AGENT_PERSISTENCE       : PostgreSQL (DRS registry + intervention log)
                        : ClickHouse (time-series DRS history + event evidence)
AGENT_CACHE             : Redis (hot DRS vectors per at-risk user — 30-min TTL)
AGENT_QUEUE             : Apache Kafka (event ingestion + intervention publishing)
AGENT_ORCHESTRATION     : Apache Airflow (batch compute + ML projection jobs)
AGENT_EXPLAINABILITY    : SHAP values mandatory — top 3 driving dimensions on all outputs
AGENT_TONE_GUARD        : Active — "dropout", "churn", "exit" forbidden in all output text
AGENT_PII_GUARD         : Active — anonymisation enforced before cross-role sharing
AGENT_MINOR_GUARD       : Active — staged protection rules for under-13, under-16, under-18
```

### DRM-3.2 — Detection + Intervention Pipeline (LOCKED)

```
STAGE-1: EVENT INGESTION
  Consume Kafka events from DRM event registry (Section DRM-13.1)
  Strip PII before processing
  Route event to appropriate dimension detector (DS-001 to DS-007)

STAGE-2: DIMENSION SCORE UPDATE
  Apply event to relevant dimension score formula
  Update Redis dimension score cache (TTL: 30 min)
  Increment evidence counter (TTL: 21-day rolling)
  If evidence_count ≥ 3 → confirm signal active
  If evidence_count < 3 → log as pending signal only

STAGE-3: DRS RECOMPUTE
  Retrieve all 7 dimension scores from cache/DB
  Apply context_amplifier (championship window / belt window / subscription window)
  Apply persistence_multiplier (days at current stage)
  Compute composite DRS
  Assign Dropout Stage (STAGE-0 through STAGE-5)
  Check: has stage changed from prior DRS? → fire stage_change event if yes

STAGE-4: INTERVENTION ROUTING
  If stage UNCHANGED: no new intervention (avoid notification fatigue)
  If stage ESCALATED: fire appropriate intervention per stage rules (Section DRM-4)
  If stage RECOVERED: fire recovery celebration event
  Apply frequency cap: max 2 re-engagement notifications per user per day
  Apply quiet hours: no push between 22:00–08:00 local time
  Apply compassion hold: if RC-008 external factor hold active → skip automated interventions

STAGE-5: PARENT ML PROJECTION
  Daily batch only
  Run LSTM + Random Forest ensemble on 30-day history
  Compute: P(dropout_in_14d), P(dropout_in_30d)
  Compute: P(recovery_with_intervention), P(recovery_without_intervention)
  Generate parent narrative (tone-filtered — see Section DRM-6.2)
  Write to wim_parent_projections table (shared schema with WIM)

STAGE-6: PERSISTENCE + PUBLISH
  Write to PostgreSQL: drm_risk_registry (upsert latest DRS per user)
  Write to ClickHouse: drm_risk_history (append time-series snapshot)
  Update Redis: user DRS hot cache (30-min TTL)
  Publish Kafka events:
    drm.stage.changed → Antigravity Risk Alert Engine + Notification Service
    drm.stage.escalated → Mentor Service + Parent Dashboard
    drm.stage.recovered → Achievement Engine (positive milestone)
    drm.structural.detected → Curriculum Service + AG-STRUCT
    drm.championship.hold.advisory → Championship Engine + Mentor Board
    drm.intervention.generated → Notification Service
    drm.parent.projection.ready → Parent Dashboard Service
```

---

## 🔒 SECTION DRM-4 — GRADUATED INTERVENTION REGISTRY

### DRM-4.1 — Automated Intervention Catalog (LOCKED)

```
AUTO-INT-001: STREAK RESTORATION NUDGE (STAGE-1)
  Trigger: DS-002 active (streak break detected)
  Channel: In-app push notification + home screen widget
  Message tone: Warm, encouraging — "Your learning streak is waiting for you"
  Action: Deep-link to daily activity screen
  Frequency cap: 1 per day maximum
  Student label: Never "you've been absent" — always "come back to your journey"
  Cooldown: 48 hours before next nudge on same signal

AUTO-INT-002: PERSONALISED RE-ENGAGEMENT CARD (STAGE-1 → STAGE-2)
  Trigger: DS-001 or DS-003 active for ≥ 5 days
  Channel: In-app notification + home screen card
  Content: Personalised content based on user's strongest dimension
           (e.g., "Your [top skill] is waiting — your last session was [N] days ago")
  Action: Surface most relevant pending drill or championship event
  Frequency cap: 1 per 3 days maximum
  Tone: Care-framed, not guilt-framed

AUTO-INT-003: PEER RECONNECTION SUGGESTION (STAGE-2)
  Trigger: DS-004 active (social disengagement)
  Channel: In-app notification
  Content: Suggest a specific study room or peer quiz based on prior activity
           ("3 of your peers are in a study room right now")
  Action: Deep-link to study room or peer connection
  Privacy rule: Peer names included only if both users have mutual visibility enabled

AUTO-INT-004: CHAMPIONSHIP INCENTIVE SURFACE (STAGE-2 — championship context)
  Trigger: DS-005 active + championship window within 21 days
  Channel: In-app notification + championship home card
  Content: Remind student of upcoming championship opportunity with their current position
           ("You're eligible for [bracket tier] — registration closes in [N] days")
  Action: Deep-link to championship registration or bracket view
  Tone: Opportunity-framing — never pressure framing

AUTO-INT-005: GAMIFICATION REWARD UNLOCK (STAGE-3)
  Trigger: DRS reaches STAGE-3
  Channel: In-app animated reward reveal
  Content: Unlock a bonus reward box (as per R95 RewardBox) for returning
           "We've saved your reward — come claim it"
  Action: Reward appears on next app open after 3-day absence
  Tone: Celebratory, playful — reward is unconditional on return, not conditional

AUTO-INT-006: PERSONALISED RECOVERY PLAN (STAGE-3 → STAGE-4)
  Trigger: DRS at STAGE-3 for ≥ 10 days, or escalated to STAGE-4
  Channel: In-app notification + dedicated recovery plan screen
  Content: Simplified 7-day re-engagement roadmap:
           - Day 1: Complete one drill (15 min)
           - Day 3: Rejoin one study room
           - Day 5: Take one practice match
           - Day 7: Streak restored — next reward unlocked
  Action: Recovery plan screen with daily checkpoint system
  Tone: Gentle, structured, achievable — no performance targets
  Mentor notification: Mentor informed when recovery plan issued

AUTO-INT-007: WIN-BACK SEQUENCE (STAGE-5)
  Trigger: DRS at STAGE-5 (imminent exit)
  Channel: Push + Email + In-app (all channels)
  Content: 5-step win-back sequence over 10 days:
           Day 1: "We miss you" warm message + key achievement reminder
           Day 3: Peer activity summary ("Your study group has been active")
           Day 5: Special re-entry reward offer
           Day 7: Mentor personal message prompt (mentor approves before send)
           Day 10: Final re-entry invitation with account preservation notice
  Frequency cap: Maximum 5 messages in 10-day window
  Opt-out: User can silence win-back sequence at any point
  Tone: Warm, non-pressuring, dignity-preserving
```

### DRM-4.2 — Human-Required Intervention Catalog (LOCKED)

```
HUMAN-INT-001: MENTOR ADVISORY NOTIFICATION (STAGE-2)
  Recipient: Assigned mentor
  Content: Student showing early disengagement signals — advisory awareness
  Required action: None mandatory at Stage-2 — advisory only
  Mentor panel update: Student flagged amber in mentor cohort panel
  Escalation if no mentor response within 7 days: Upgrade to HUMAN-INT-002

HUMAN-INT-002: MENTOR INTERVENTION REQUIRED (STAGE-3)
  Recipient: Assigned mentor
  Content: Student showing high dropout risk — intervention required within 48 hours
  Required action: Mentor must either:
    (a) Contact student via platform messaging
    (b) Schedule a check-in session
    (c) Override with logged reason (e.g. "student confirmed on planned break")
  Escalation if no mentor action within 48 hours: Notify mentor manager
  Mentor panel update: Student flagged red in mentor cohort panel
  Audit: All mentor actions on this record are immutable audit-logged

HUMAN-INT-003: MENTOR BOARD ESCALATION (STAGE-4)
  Recipient: Mentor board (mentor manager + senior mentor)
  Content: Student at critical dropout risk — board awareness required
  Required action: Board must review within 24 hours
  Options for board:
    (a) Assign rescue mentor
    (b) Approve championship bracket hold advisory
    (c) Approve parent escalation (if not already triggered by DRM)
    (d) Override with logged reason
  Audit: Board decision immutably logged

HUMAN-INT-004: INSTITUTE ADMIN NOTIFICATION (STAGE-4)
  Recipient: Institute admin (if student is institution-linked)
  Content: Cohort-level awareness — one or more students at critical dropout risk
  Content format: Anonymised in first notification — only student count, not names
  Student name: Revealed only if institute admin explicitly requests drill-down
  Required action: Advisory — no mandatory institute action
  Purpose: Enable institute to trigger institutional support if available

HUMAN-INT-005: CHAMPIONSHIP BRACKET HOLD ADVISORY (STAGE-3 + DS-005 active)
  Recipient: Championship Engine + Mentor Board
  Content: Student in active tournament showing abandonment signals
  Advisory: Recommend bracket hold pending mentor confirmation
  Authority: Mentor board must confirm hold — DRM cannot hold bracket autonomously
  Student notification: If hold confirmed — student notified warmly
                        ("Your bracket slot has been preserved — reach out to your mentor")
  Audit: Hold decision and mentor board confirmation immutably logged

HUMAN-INT-006: URGENT PARENT ESCALATION (STAGE-4 → STAGE-5)
  Recipient: Parent/guardian (if linked and consent active)
  Channel: Push notification + email + parent dashboard alert card
  Content: Care-framed concern + specific suggested action
           ("We want to make sure [child] feels supported — here is what you can do")
  Required action: Suggested parent action + mentor contact detail provided
  Tone rules: Non-alarming, care-first, empowering — parent as partner not passenger
  FORBIDDEN: Clinical language, dropout prediction probability shown to parent,
             comparison to other students, any language implying failure
  Audit: Parent notification delivery and read status logged

HUMAN-INT-007: EXIT SURVEY (STAGE-5 — optional only)
  Trigger: DRS at STAGE-5 and win-back sequence has not produced re-engagement
  Channel: Single optional in-app prompt — non-mandatory
  Content: "We'd love to understand how we can do better for you"
           3 simple question survey — no performance or comparison questions
  Rules: Survey is entirely optional — no re-engagement conditional on completion
         Survey is anonymised before any analysis
         Survey cannot be shown more than once per user
         Survey disappears on any user re-engagement
```

---

## 🔒 SECTION DRM-5 — CHAMPIONSHIP ADVANCED DROPOUT INTEGRATION

### DRM-5.1 — Championship-Specific Dropout Signal Amplification (LOCKED)

```
CHAMP-DRM-001: TOURNAMENT WINDOW AMPLIFIER
  Definition: Championship event active or within 14 days
  DRS Amplifier: ×1.3 on all dimension scores during window
  Rationale: Mid-tournament dropout has greater platform, bracket, and trust consequences
  Deactivation: Amplifier removed 48 hours after tournament conclusion

CHAMP-DRM-002: PRE-MATCH EXIT CLUSTER SIGNAL
  Definition: ≥ 3 pre-match exits in any 14-day window
  DS-005 contribution: 60 points (maximum contribution for this sub-signal)
  Championship Action: Advisory to mentor — "student is avoiding match starts"
  Parent Signal: Advisory card (Stage-2 threshold still applies)
  Tone: "We've noticed [child] has been stepping away from matches lately"

CHAMP-DRM-003: BRACKET FORFEIT PATTERN
  Definition: ≥ 2 bracket forfeits in same tournament or consecutive tournaments
  DS-005 contribution: 60 points
  Championship Action: HUMAN-INT-005 (bracket hold advisory — mentor board required)
  Championship Consequence: If hold confirmed — slot preserved for 7 days pending
                             mentor re-engagement confirmation
  Parent Signal: AG-MED care alert with championship context

CHAMP-DRM-004: QUALIFICATION WINDOW SKIP
  Definition: Student eligible for qualification but did not register within 48 hours
               of opening AND no engagement signal in that period
  DS-005 contribution: 45 points (1 occurrence) / 60 points (2+ occurrences)
  Championship Action: Reminder notification (in-app + push) on day 1
                       Mentor notification on day 3 of no response
  Parent Signal: None at single occurrence — Stage-2 overall triggers parent if combined

CHAMP-DRM-005: POST-LOSS DISENGAGEMENT PATTERN
  Definition: Significant engagement drop (DS-001 > 40 points) within 72 hours after
               a championship match loss
  Cross-Reference: WIM WC-003 (pressure response) signal may co-occur
  Action: Personalised re-engagement card specific to post-competition context
          ("Every champion uses losses to train smarter — here is your next step")
  Mentor Alert: If post-loss disengagement persists beyond 7 days → HUMAN-INT-001
  Parent Signal: If combined DRS reaches Stage-3 → parent card with appropriate framing

CHAMP-DRM-006: HALL OF FAME PROXIMITY ABANDONMENT
  Definition: Student was within 10% of Hall of Fame threshold but disengaged
              (DRS escalates to Stage-2+) in the 30 days preceding threshold
  Signal Weight Boost: DS-001 and DS-003 dimension weights ×1.2 in this context
  Action: Personalised "you were so close" re-engagement with Hall of Fame milestone
          reminder ("You're [N] achievements from Hall of Fame status")
  Mentor Alert: HUMAN-INT-001 — context flag "Hall of Fame proximity"
```

---

## 🔒 SECTION DRM-6 — ROLE-SPECIFIC OUTPUT RULES

### DRM-6.1 — Student-Facing Dropout Output (LOCKED)

```
STUDENT_DRM_OUTPUT_RULES:
  Visibility: Student sees re-engagement content only — no DRS shown ever
  Language: Care-framing MANDATORY — engagement invitation only
  FORBIDDEN terms in student UI:
    - "dropout risk"
    - "churn"
    - "disengaged"
    - "falling behind"
    - "inactive"
    - Any reference to platform retention mechanics
  REQUIRED framing in student UI:
    - "We missed you"
    - "Your journey is waiting"
    - "Come back to your [streak / championship / belt goal]"
    - "Your peers are [activity]"
    - "You were close to [milestone]"
  DRS Score: NEVER shown to student
  Stage label: NEVER shown to student
  Intervention reason: NEVER shown to student (e.g., student does not see "You triggered a retention alert")
```

### DRM-6.2 — Parent-Facing Dropout Output (LOCKED)

```
PARENT_DRM_OUTPUT_RULES:
  Visibility: Parent sees care signals for own child only
  DRS Score: NEVER shown to parent
  Stage label: NEVER shown to parent
  Dropout probability: NEVER shown to parent (not even confidence-qualified)
  Dropout word: NEVER used in parent-facing text

PARENT DRM CARD FORMATS (by stage):

STAGE-1 → STAGE-2 (Advisory Card — no push):
  Title: "Checking in on [child's name]'s learning journey"
  Body: "We've noticed [child] has been a little less active on the platform lately.
         This is normal — here is a small idea that might help them reconnect."
  Action suggestion: Simple, specific, low-pressure parent action
  Tone: Warm, curious, empowering — not alarming

STAGE-3 (Advisory Card + soft push):
  Title: "We want to make sure [child] stays on track"
  Body: "We have noticed some changes in [child]'s activity recently.
         Their mentor has been informed and is keeping an eye on things.
         Here is something you can do to support them at home."
  Action: Specific suggested conversation starter or home activity
  Mentor status: "Mentor [first name] is aware and will reach out"
  Tone: Reassuring, collaborative — parent + mentor as team

STAGE-4 (Push + escalation card):
  Title: "We'd like to check in on [child]"
  Body: "We haven't seen [child] on the platform recently and we want to
         make sure everything is okay. Your involvement as a parent makes
         a real difference. Here is how you can help right now."
  Action: Specific — "Consider having a gentle conversation about what they are enjoying
           or finding challenging right now"
  Mentor contact: "[Mentor first name]'s contact is available — they would welcome
                   a brief update from you"
  Tone: Care-first, never alarm-first

STAGE-5 (Urgent care card + push + email):
  Title: "We're reaching out about [child]"
  Body: "We want to make sure [child] is doing well. The platform has not seen
         them for some time and we wanted you to know we are here to help.
         Please don't hesitate to reach out."
  Action: Specific — mentor contact + platform support contact
  Tone: Human, warm, open — no pressure language, no performance reference

FORBIDDEN IN ALL PARENT OUTPUT:
  - Dropout probability numbers
  - Comparison to other students
  - Clinical language
  - Any framing that implies the parent failed
  - Urgency language that triggers panic
  - Stage numbers or DRS values
```

### DRM-6.3 — Mentor-Facing Dropout Output (LOCKED)

```
MENTOR_DRM_OUTPUT_RULES:
  Visibility: Full DRS data for assigned students only
  Includes: DRS, stage, driving dimension scores, evidence summary, intervention history
  Authority: Mentor may override any DRM stage classification with reason (audit-logged)

MENTOR DRM PANEL FORMAT (per student):
  - DRS score (0–100) with confidence badge
  - Stage label with duration (e.g., "HIGH RISK — Day 12")
  - Top 3 driving signal dimensions with scores
  - Intervention history: what automated actions have already fired
  - Recommended human intervention type + urgency
  - Championship context flag (if applicable)
  - Chronic flag (if > 30 days at Stage-3+)
  - Quick actions: Message student / Schedule session / Override DRS / Escalate

MENTOR OVERRIDE RULE:
  Mentor may:
    - Downgrade stage with reason (e.g., "Student on planned family holiday")
    - Upgrade stage with reason (e.g., "Student confided personal difficulties")
    - Apply Compassion Hold (pauses automated interventions for up to 14 days)
    - Mark student as RECOVERED before DRM evidence threshold met
  All overrides: Immutable audit log entry with reason code
  Compassion Hold: Mentor-applied pause on all automated DRM interventions
                   (DRM continues computing DRS but does not fire interventions)
                   Maximum hold: 14 days — extendable by mentor board only
```

### DRM-6.4 — Institute Admin Dropout Output (LOCKED)

```
INSTITUTE_DRM_OUTPUT_RULES:
  Visibility: Cohort-level dropout risk data — anonymised by default
  Individual student: Accessible on drill-down with role permission

INSTITUTE DRM DASHBOARD:
  - Cohort dropout risk distribution: % of students at each stage
  - Stage-3+ students: count (not names by default — drill-down requires permission)
  - Top driving dimensions across cohort: which signals are most prevalent
  - Recovery rate: % of students who recovered within 30 days of Stage-2 detection
  - Chronic flag rate: % of students with > 30 days at Stage-3+
  - Championship abandonment rate: DS-005 prevalence across cohort
  - Comparison to prior semester: trend direction

STRUCTURAL DROPOUT ALERT:
  Trigger: > 20% of cohort at Stage-2+ simultaneously
  Action: AG-STRUCT alert → curriculum team + institute admin
  Analysis: Which dimensions are driving structural dropout? (scenario design? mentor issue?)
  Outcome: Human investigation required — DRM flags, humans diagnose cause
```

### DRM-6.5 — Recruiter Dropout Data Policy (LOCKED)

```
RECRUITER_DROPOUT_DATA_POLICY:
  RECRUITERS NEVER SEE ANY DROPOUT DATA — EVER

  This includes:
    - DRS score
    - Stage classification
    - Signal dimension scores
    - Intervention history
    - Recovery history
    - Any derived label (inactive, disengaged, at-risk)

  What recruiters see instead:
    - Current engagement tier (ACTIVE / GROWING / BUILDING) — positive framing only
    - Recent skill progress (last 30 days activity level — no dropout context)
    - Championship participation status (active or not — no abandonment context)

  Technical enforcement: Kong API Gateway blocks all dropout data fields
                         from any response served to recruiter role tokens
```

---

## 🔒 SECTION DRM-7 — PARENT PREDICTIVE AI DROPOUT LAYER

### DRM-7.1 — Parent Dropout Prediction Model Architecture (LOCKED)

```
MODEL_NAME              : DropoutPredictiveModel (DPM)
MODEL_TYPE              : LSTM (temporal sequence) + Random Forest (feature importance)
                        : Ensemble: 60% LSTM weight + 40% Random Forest weight
INPUT_WINDOW            : 30-day rolling event history across all 7 signal dimensions
PREDICTION_HORIZON_1    : 14-day forward dropout probability
PREDICTION_HORIZON_2    : 30-day forward dropout probability
OUTPUT_1                : P(dropout_14d) — 0 to 1
OUTPUT_2                : P(dropout_30d) — 0 to 1
OUTPUT_3                : P(recovery_14d_with_intervention) — 0 to 1
OUTPUT_4                : P(recovery_14d_without_intervention) — 0 to 1
CONFIDENCE_FLOOR        : 60% — projections suppressed below this
EXPLAINABILITY          : SHAP values — top 3 contributing features mandatory
UPDATE_FREQUENCY        : Daily batch (Airflow 05:00 AM)
BIAS_AUDIT              : Quarterly — gender, region, age group, domain checked
RETRAIN_TRIGGER         : PSI drift > 0.2 OR false positive rate > 25% over 30 days
HUMAN_APPROVAL_GATE     : Required before production deployment of new model version
ROLLBACK_POLICY         : Prior model version retained 90 days post-retrain
PARENT_OUTPUT_FILTER    : All parent-facing projections run through tone guard
PREDICTION_LABELLING    : All parent projections framed as "engagement signals"
                          NEVER as "dropout probability" in parent-facing UI
```

### DRM-7.2 — Parent Prediction Output Cards (LOCKED)

```
PARENT-DRM-PRED-001: EARLY ENGAGEMENT SIGNAL CARD
  Trigger: P(dropout_14d) > 0.40 AND current DRS < Stage-3
  Card type: Gentle advisory — no push notification
  Parent narrative:
    "We are seeing some early signals that [child] may benefit from extra encouragement
     over the next couple of weeks. This is very common and easy to address."
  Recommended action: Specific, simple, home-based
  Confidence badge: "We're [X]% confident in this early signal"
  Top features: "This signal is mainly driven by [top feature in plain language]"
  FORBIDDEN: Probability number shown to parent ("40% chance of dropout" — NEVER)

PARENT-DRM-PRED-002: SUSTAINED RISK PROJECTION CARD
  Trigger: P(dropout_30d) > 0.55 OR current Stage ≥ 3 for > 7 days
  Card type: Advisory push + dashboard card
  Parent narrative:
    "We want to flag that [child] has been less active recently.
     With your support and their mentor's guidance, this is very addressable."
  Comparative framing: "Students who re-engage at this stage typically
                         return to full activity within 2–3 weeks with support"
  Action: Specific mentor engagement suggestion + parent conversation guide
  FORBIDDEN: Probability numbers, stage labels, comparison to dropout statistics

PARENT-DRM-PRED-003: RECOVERY MOMENTUM CARD
  Trigger: DRS dropping (student recovering) after Stage-2+
  Card type: Positive dashboard card + push
  Parent narrative:
    "[Child] is showing great signs of getting back into their rhythm.
     Here is how you can celebrate this with them."
  Celebration framing: Achievement acknowledgement
  Suggested parent action: Positive reinforcement at home

PARENT-DRM-PRED-004: INTERVENTION EFFECTIVENESS CARD
  Trigger: Active intervention running (any human or automated intervention active)
  Card type: Progress update card (weekly)
  Parent narrative:
    "Here is how [child]'s re-engagement plan is progressing"
  Simple progress indicator (no raw scores, no DRS)
  What parent can do this week: 1 specific action
```

---

## 🔒 SECTION DRM-8 — RETENTION LOOP INTEGRATION (R91–R95)

### DRM-8.1 — Streak Engine Integration (R95) (LOCKED)

```
STREAK_INTEGRATION_RULES:
  DRM CONSUMES:
    - StreakTracker.current_streak (real-time)
    - StreakTracker.max_streak (for DS-002 computation)
    - DailyActivity.activity_date (for session continuity)
    - RewardBox.granted_at (for reward cadence analysis)

  DRM PUBLISHES:
    - drm.streak.risk.signal → Streak Engine (triggers protective nudge)
    - drm.reward.unlock.recommend → Reward Engine (Stage-3 reward release)

  STREAK BREAK RESPONSE PROTOCOL:
    Day 1 of break  : Automated streak restoration nudge (AUTO-INT-001)
    Day 3 of break  : Personalised re-engagement card (AUTO-INT-002)
    Day 5 of break  : Gamification reward unlock (if DRS ≥ Stage-2)
    Day 7 of break  : Mentor advisory notification (HUMAN-INT-001)
    Day 10 of break : Mentor intervention required (HUMAN-INT-002) if DRS ≥ Stage-3
    Day 14 of break : Parent advisory card pushed
```

### DRM-8.2 — Social Layer Integration (R92/R93/R94) (LOCKED)

```
SOCIAL_INTEGRATION_RULES:
  DRM CONSUMES FROM R92 (Study Rooms + Peer Learning):
    - Study room membership changes (exits vs joins)
    - Peer quiz challenge acceptance rate
    - Shared note uploads
    → Feeds DS-004 (Social Disengagement)

  DRM CONSUMES FROM R93 (Social Feed + Campus Community):
    - Post creation rate per 7-day window
    - Reaction and comment activity rate
    - Group membership changes
    → Feeds DS-004 (Social Disengagement)

  DRM CONSUMES FROM R94 (Career + Peer Collaboration):
    - Peer project participation (joins vs exits)
    - Mock interview room activity
    - Resume share activity
    → Feeds DS-004 + DS-007 (Social + Lifecycle signals)

  SOCIAL RE-ENGAGEMENT TRIGGERS:
    - DS-004 active → AUTO-INT-003 (Peer Reconnection Suggestion)
    - Study room activity from peer network surfaced in notification
    - DRM publishes: drm.social.reengagement.suggest → Social Layer
      (triggers "Your study group is active" notification)

  PRIVACY RULE: Social signals used for DRS computation are aggregated
                No individual peer interactions exposed across users
```

### DRM-8.3 — Gamification Integration (LOCKED)

```
GAMIFICATION_INTEGRATION_RULES:
  DRM PUBLISHES:
    - drm.reward.unlock.recommend (Stage-3) → Achievement Engine
      Payload: { user_id, reward_type: "return_bonus", trigger: "drm_stage_3" }
    - drm.milestone.recovered → Achievement Engine
      Payload: { user_id, milestone: "comeback_champion", streak_restored: N }
    - drm.championship.proximity.remind → Championship Engine
      Payload: { user_id, championship_id, gap_to_threshold }

  GAMIFICATION ANTI-PATTERNS (FORBIDDEN):
    - DO NOT unlock rewards conditionally on engagement (reward must be unconditional
      — "We saved your reward" not "Complete this task to get your reward")
    - DO NOT surface competitive pressure as re-engagement tool
    - DO NOT show leaderboard position in dropout-context notifications
    - DO NOT use gamification to mask concern with artificial enthusiasm

  REWARD UNLOCK RULES (STAGE-3 AUTO-INT-005):
    - Reward box unlocked immediately at Stage-3 trigger
    - Reward appears on first app open (no conditions)
    - Reward type: RewardBox as defined in R95
    - Maximum 1 DRM-triggered reward per user per 30-day period
```

---

## 🔒 SECTION DRM-9 — DATABASE SCHEMA REQUIREMENTS

```sql
-- Core dropout risk registry (latest state per user)
CREATE TABLE drm_risk_registry (
  risk_id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                 UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  drs_score               NUMERIC(5,2) NOT NULL,        -- 0–100
  drs_confidence          NUMERIC(5,2) NOT NULL,        -- 0–100
  dropout_stage           INTEGER NOT NULL,             -- 0 through 5, or 'R' (recovered)
  stage_label             VARCHAR(20) NOT NULL,         -- HEALTHY, EARLY, ACTIVE_RISK, etc.
  days_at_current_stage   INTEGER NOT NULL DEFAULT 0,
  chronic_flag            BOOLEAN NOT NULL DEFAULT FALSE,
  compassion_hold         BOOLEAN NOT NULL DEFAULT FALSE,
  compassion_hold_until   TIMESTAMPTZ,
  compassion_hold_by      UUID,                         -- mentor who applied hold
  ds_001_score            NUMERIC(5,2),                 -- Session Engagement Collapse
  ds_002_score            NUMERIC(5,2),                 -- Streak Break Severity
  ds_003_score            NUMERIC(5,2),                 -- Performance Deterioration
  ds_004_score            NUMERIC(5,2),                 -- Social Disengagement
  ds_005_score            NUMERIC(5,2),                 -- Championship Abandonment
  ds_006_score            NUMERIC(5,2),                 -- Economic Disengagement
  ds_007_score            NUMERIC(5,2),                 -- Lifecycle Transition
  context_amplifier       NUMERIC(4,2) NOT NULL DEFAULT 1.0,
  persistence_multiplier  NUMERIC(4,2) NOT NULL DEFAULT 1.0,
  championship_window     BOOLEAN NOT NULL DEFAULT FALSE,
  is_recovered            BOOLEAN NOT NULL DEFAULT FALSE,
  recovery_date           TIMESTAMPTZ,
  mentor_override         BOOLEAN NOT NULL DEFAULT FALSE,
  mentor_override_reason  TEXT,
  mentor_override_by      UUID,
  mentor_override_at      TIMESTAMPTZ,
  last_intervention_at    TIMESTAMPTZ,
  last_intervention_type  VARCHAR(30),
  computed_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (user_id)
);

-- Evidence event log (per signal per user)
CREATE TABLE drm_evidence_log (
  evidence_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                 UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  signal_dimension        VARCHAR(10) NOT NULL,         -- DS-001 through DS-007
  event_type              VARCHAR(80) NOT NULL,
  event_id                UUID,
  event_value             NUMERIC(10,4),
  baseline_value          NUMERIC(10,4),
  contribution_score      NUMERIC(5,2),
  evidence_window_count   INTEGER NOT NULL DEFAULT 1,
  detected_at             TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Intervention log (all automated + human interventions)
CREATE TABLE drm_intervention_log (
  intervention_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                 UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  intervention_type       VARCHAR(20) NOT NULL,         -- AUTO-INT-001 through HUMAN-INT-007
  dropout_stage_at_trigger INTEGER NOT NULL,
  drs_at_trigger          NUMERIC(5,2) NOT NULL,
  channel                 VARCHAR(20) NOT NULL,         -- push | email | in_app | mentor | parent
  delivered_at            TIMESTAMPTZ,
  opened_at               TIMESTAMPTZ,
  acted_on_at             TIMESTAMPTZ,
  effectiveness_signal    VARCHAR(20),                 -- re_engaged | no_response | opted_out
  triggered_by            VARCHAR(20) NOT NULL,         -- system | mentor | mentor_board
  notes                   TEXT,
  audit_hash              VARCHAR(64) NOT NULL
);

-- Dropout risk time-series (ClickHouse)
-- Table: drm_risk_history
-- Fields: risk_id, user_id, tenant_id, drs_score, dropout_stage,
--         ds_001_score through ds_007_score, context_amplifier,
--         persistence_multiplier, is_recovered, computed_at
-- Engine: MergeTree() ORDER BY (user_id, computed_at)

-- Parent projection snapshots
CREATE TABLE drm_parent_projections (
  projection_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  child_user_id             UUID NOT NULL,
  parent_user_id            UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  dropout_prob_14d          NUMERIC(5,4),               -- 0–1 (NEVER shown to parent)
  dropout_prob_30d          NUMERIC(5,4),               -- 0–1 (NEVER shown to parent)
  recovery_prob_with_int    NUMERIC(5,4),               -- 0–1 (used internally only)
  recovery_prob_without_int NUMERIC(5,4),               -- 0–1 (used internally only)
  confidence_score          NUMERIC(5,2),
  top_feature_1             VARCHAR(100),
  top_feature_2             VARCHAR(100),
  top_feature_3             VARCHAR(100),
  parent_card_type          VARCHAR(30),                -- EARLY_SIGNAL | SUSTAINED_RISK | RECOVERY | EFFECTIVENESS
  parent_narrative          TEXT,                       -- tone-filtered parent-safe text
  parent_action_suggestion  TEXT,
  computed_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Compassion hold registry
CREATE TABLE drm_compassion_holds (
  hold_id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                 UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  applied_by_mentor_id    UUID NOT NULL,
  applied_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at              TIMESTAMPTZ NOT NULL,
  reason_code             VARCHAR(20) NOT NULL,         -- PLANNED_BREAK | EXTERNAL_FACTOR | OTHER
  reason_note             TEXT,
  extended_by             UUID,
  extended_at             TIMESTAMPTZ,
  lifted_at               TIMESTAMPTZ,
  audit_hash              VARCHAR(64) NOT NULL
);

-- Cohort structural dropout audit
CREATE TABLE drm_structural_audits (
  audit_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id               UUID NOT NULL,
  affected_cohort_pct     NUMERIC(5,2) NOT NULL,
  top_dimension_id        VARCHAR(10) NOT NULL,
  stage_distribution      JSONB NOT NULL,
  escalated_at            TIMESTAMPTZ NOT NULL,
  curriculum_notified_at  TIMESTAMPTZ,
  ag_struct_alert_id      UUID,
  resolution_action       TEXT,
  resolved_at             TIMESTAMPTZ,
  audit_hash              VARCHAR(64) NOT NULL
);
```

---

## 🔒 SECTION DRM-10 — API CONTRACT REGISTRY

```
── STUDENT ENDPOINTS ──
GET    /drm/students/{id}/engagement-status          — Student engagement health (care-framed only)
GET    /drm/students/{id}/recovery-plan              — Active personalised recovery plan
POST   /drm/students/{id}/opt-out-winback            — Opt out of win-back sequence

── PARENT ENDPOINTS ──
GET    /drm/parents/{id}/child/{child_id}/care-cards — All parent care cards (tone-filtered)
GET    /drm/parents/{id}/child/{child_id}/progress   — Re-engagement progress cards
GET    /drm/parents/{id}/child/{child_id}/projection — Parent projection cards (tone-filtered)

── MENTOR ENDPOINTS ──
GET    /drm/mentors/{id}/student-risk-panel          — Full DRS panel for assigned students
GET    /drm/mentors/{id}/student/{student_id}/full   — Full dropout risk detail
PATCH  /drm/mentors/{id}/student/{student_id}/override — Stage override with reason
POST   /drm/mentors/{id}/student/{student_id}/compassion-hold — Apply compassion hold
POST   /drm/mentors/{id}/student/{student_id}/mark-recovered  — Mark as recovered early
GET    /drm/mentors/{id}/intervention-history/{student_id}    — All past interventions

── INSTITUTE ADMIN ENDPOINTS ──
GET    /drm/institutes/{id}/cohort-risk-dashboard    — Cohort dropout risk dashboard
GET    /drm/institutes/{id}/structural-alerts        — Structural dropout alerts
GET    /drm/institutes/{id}/stage-distribution       — Student count by stage
GET    /drm/institutes/{id}/recovery-rate            — 30-day recovery rate report

── PLATFORM ADMIN ENDPOINTS ──
GET    /drm/admin/model-health                       — DPM model health + PSI
GET    /drm/admin/pipeline-status                    — Detection pipeline latency
POST   /drm/admin/trigger-recompute/{user_id}        — Manual DRS recompute
GET    /drm/admin/intervention-effectiveness         — Platform-wide intervention data
GET    /drm/admin/compassion-holds-active            — All active compassion holds

── INTERNAL SERVICE ENDPOINTS ──
POST   /drm/internal/event                           — Ingest dropout signal event
GET    /drm/internal/drs-vector/{user_id}            — Raw DRS vector (internal only)
POST   /drm/internal/structural-flag                 — Flag structural cohort dropout
GET    /drm/internal/ag-signal-payload/{user_id}     — Generate AG alert payload

AUTHENTICATION: JWT required on all endpoints
AUTHORIZATION: Per-role RBAC enforced — cross-role dropout data forbidden
RATE LIMITING: 100 req/min per user / 1,000 req/min per service
AUDIT LOGGING: All read + write events logged with requestor_id + timestamp
RECRUITER BLOCK: Enforced at Kong API Gateway — zero dropout data in recruiter responses
```

---

## 🔒 SECTION DRM-11 — FLUTTER UI SCREENS REQUIRED

```
DRM-SCREEN-001: Student Re-Engagement Home Card (Student)
  Module: Skill_Development_UI (Home Dashboard)
  Role: Student
  Stage: INTELLIGENCE_UI (Stage 2)
  Trigger: DRS at STAGE-1+
  Components:
    - Warm welcome-back card (context-personalised)
    - Last achievement reminder
    - Next step CTA (one clear action — drill / match / study room)
    - Streak restoration indicator (visual — "day 1 of rebuilding your streak")
  Rules: No DRS, no stage label, no "we noticed you've been absent" phrasing
  Animation: Gentle — warm colour palette, inviting not alarming

DRM-SCREEN-002: Personalised Recovery Plan Screen (Student)
  Module: Skill_Development_UI
  Role: Student
  Stage: INTELLIGENCE_UI
  Trigger: STAGE-3 + HUMAN-INT-006 issued
  Components:
    - 7-day lightweight plan (Day 1 through Day 7)
    - Daily checkpoint system (tap to mark complete)
    - Progress bar (simple, visual)
    - "What happens next" milestone preview
    - Mentor connection CTA (non-mandatory — optional)
  Rules: No performance targets, no scores, no championship pressure
  Tone: Achievable, gentle, structured

DRM-SCREEN-003: Win-Back Re-Entry Screen (Student — STAGE-5)
  Module: Skill_Development_UI
  Role: Student
  Stage: INTELLIGENCE_UI
  Trigger: STAGE-5 win-back sequence Day 5 (special re-entry offer)
  Components:
    - Warm "we saved everything for you" message
    - Summary of preserved: streak history, belt progress, championship slot
    - Re-entry reward reveal animation
    - "Start fresh today" CTA → deep-links to daily activity
  Rules: Never shows how long they were absent, never shows platform statistics

DRM-SCREEN-004: Parent Care Dashboard (Parent)
  Module: ERP_UI (Parent sub-module)
  Role: Parent / Guardian
  Stage: INTELLIGENCE_UI
  Components:
    - Current engagement health indicator (visual — not DRS, not stage label)
    - Active care cards (tone-filtered per stage)
    - Projection card (engagement signal — not dropout probability)
    - Mentor status indicator
    - "How to support [child]" expandable action guide
  Rules: All content tone-filtered, DRS and stage never visible

DRM-SCREEN-005: Mentor Dropout Risk Panel (Mentor)
  Module: Group_Discussion_UI (Mentor sub-module)
  Role: Mentor
  Stage: INTELLIGENCE_UI
  Components:
    - Student risk table: DRS, stage, days at stage, top driving dimension
    - Traffic light colour coding: green / amber / red / critical
    - Sort by: DRS desc, stage, days at stage, championship flag
    - Quick action panel: Message / Schedule / Override / Compassion Hold
    - Chronic flag highlight: students > 30 days at Stage-3+ pinned top
    - Championship context flags: tournament window students highlighted

DRM-SCREEN-006: Institute Cohort Dropout Dashboard (Institute Admin)
  Module: ERP_UI (Institute Admin sub-module)
  Role: Institute Admin
  Stage: ECOSYSTEM_UI (Stage 3)
  Components:
    - Stage distribution donut chart: % at each stage
    - 30-day trend: is cohort improving or worsening?
    - Top dropout signal dimensions across cohort
    - Recovery rate gauge
    - Structural dropout alert banner (if SEVERITY-5 triggered)
    - Drill-down to individual student (own students only, role permission required)

DRM-SCREEN-007: Platform Admin Dropout Command Centre
  Module: ERP_UI (Platform Admin)
  Role: Platform Admin
  Stage: COMPLIANCE_UI (Stage 4)
  Components:
    - Real-time DRS pipeline health (Kafka lag, compute latency)
    - DPM model health (PSI, confidence distribution)
    - Platform-wide stage distribution trend
    - Intervention effectiveness heatmap (which interventions are working)
    - Active compassion holds registry
    - Structural dropout audit log
```

---

## 🔒 SECTION DRM-12 — OBSERVABILITY REQUIREMENTS

```
REQUIRED METRICS:
  drm_drs_distribution{stage, tenant}                — DRS distribution by stage
  drm_stage_transitions_total{from_stage, to_stage}  — Stage escalation + recovery volume
  drm_detection_latency_seconds                      — Streaming pipeline latency
  drm_intervention_delivery_total{type, channel}     — Intervention volume by type + channel
  drm_intervention_effectiveness_rate{type}          — Re-engagement rate per intervention type
  drm_compassion_holds_active                        — Active mentor compassion holds
  drm_chronic_flag_rate{tenant}                      — Chronic dropout rate per tenant
  drm_recovery_rate_30d                              — 30-day recovery rate platform-wide
  drm_model_confidence_avg                           — DPM average prediction confidence
  drm_model_psi_current                              — DPM population stability index
  drm_parent_projections_generated_total             — Parent ML batch volume
  drm_ag_signals_emitted_total{alert_code}           — AG signal volume
  drm_championship_hold_advisories_active            — Active bracket hold advisories
  drm_winback_sequence_completion_rate               — Win-back campaign effectiveness

REQUIRED DASHBOARDS:
  — DRM Agent Health Monitor (ops team)
  — Platform Dropout Risk Command Centre (platform admin)
  — Cohort Dropout Trend by Tenant (platform admin + institution admin)
  — Intervention Effectiveness Tracker (product team)
  — Parent Projection Quality Dashboard (ML ops)
  — Championship Dropout Correlation Dashboard (championship team)
  — Compassion Hold Registry (mentor board + compliance)

REQUIRED ALERTING:
  — Kafka consumer lag > 3 min → ops page
  — DPM confidence drops < 65% avg → ML ops alert
  — Platform-wide Stage-4+ rate exceeds 5% → governance alert
  — Structural dropout detected (> 20% cohort Stage-2+) → curriculum + governance alert
  — Compassion hold > 14 days without extension → mentor board alert
  — Win-back sequence completion < 10% for 30-day period → product team alert
  — Parent projection batch fails → ML team page
```

---

## 🔒 SECTION DRM-13 — EVENT INTEGRATION REGISTRY

### DRM-13.1 — Events DRM Consumes (Kafka)

```
Event Topic                              → DRM Signal
activity.logged (R95)                    → DS-001 update (session continuity)
session.completed                        → DS-001 update (session duration, frequency)
session.abandoned (early exit)           → DS-001 + DS-003 evidence
streak.broken (R95)                      → DS-002 activation
streak.restored (R95)                    → DS-002 recovery signal
match.scored                             → DS-003 performance check
match.pre_exit (pre-match abandon)       → DS-005 evidence + CHAMP-DRM-002
belt.promotion.failed                    → DS-003 evidence
championship.round.forfeited             → DS-005 evidence (heavy weight)
championship.qualification.window.skipped → DS-005 evidence
championship.round.completed             → DS-005 recovery signal
study_room.exited (R92)                  → DS-004 evidence
study_room.joined (R92)                  → DS-004 recovery signal
peer_quiz.declined (R92)                 → DS-004 evidence
social.post.created (R93)                → DS-004 recovery signal
group.membership.left (R93)              → DS-004 evidence
peer_project.exited (R94)                → DS-004 + DS-007 evidence
subscription.cancellation.initiated      → DS-006 evidence (critical weight)
payment.failed                           → DS-006 evidence
profile.completion.regressed             → DS-007 evidence
career_path.changed (within 7d)          → DS-007 evidence
account.restricted                       → DS-007 evidence
user.login (any login)                   → DS-001 recovery signal
user.created                             → DRS initialise at STAGE-0
mentor.compassion_hold.applied           → Pause automated interventions
mentor.compassion_hold.lifted            → Resume automated interventions
wim.weakness.detected                    → DS-003 cross-signal amplifier check
wim.weakness.chronic.flagged             → DS-003 heavy evidence
```

### DRM-13.2 — Events DRM Publishes (Kafka)

```
Event Topic                              → Consumed By
drm.stage.changed                        → Antigravity Risk Alert Engine + Notification Service
drm.stage.escalated                      → Mentor Service + Parent Dashboard
drm.stage.recovered                      → Achievement Engine + Parent Dashboard
drm.intervention.generated               → Notification Service
drm.structural.detected                  → Curriculum Service + AG-STRUCT
drm.parent.projection.ready              → Parent Dashboard Service
drm.championship.hold.advisory           → Championship Engine + Mentor Board Service
drm.reward.unlock.recommend              → Achievement Engine (gamification)
drm.social.reengagement.suggest          → Social Layer Service (study room suggestion)
drm.streak.risk.signal                   → Streak Engine (protective nudge)
drm.winback.sequence.start               → Notification Service (5-message sequence)
drm.exit.survey.trigger                  → Survey Service (optional — STAGE-5 only)
drm.mentor.override.logged               → Audit Service (immutable)
drm.compassion.hold.applied              → Notification Service (halt interventions)
```

---

## 🔒 SECTION DRM-14 — ANTIGRAVITY RISK ALERT ENGINE INTEGRATION

### DRM-14.1 — DRM → Antigravity Signal Map (LOCKED)

```
DRM Signal                                   → AG Alert Code        → Alert Class
Stage escalated to STAGE-2 (7+ days)         → PARENT_RISK_001      → AG-PRED
Stage escalated to STAGE-3                   → PARENT_RISK_006      → AG-MED
Championship abandonment pattern (DS-005)    → PARENT_RISK_003 feed → AG-MED
Stage escalated to STAGE-4                   → AG-HIGH              → AG-HIGH
STAGE-5 (imminent exit)                      → AG-CRIT              → AG-CRIT
Compassion hold: RC-008 external factor      → PARENT_RISK_008 feed → AG-HIGH
CHRONIC_FLAG triggered (30d at Stage-3+)     → AG-HIGH              → AG-HIGH
Structural dropout > 20% cohort              → AG-STRUCT            → AG-STRUCT
Win-back 10-day sequence with no response    → AG-HIGH              → AG-HIGH
Subscription cancellation initiated (DS-006) → ECONOMIC_RISK feed   → AG-HIGH
```

### DRM-14.2 — DRM → Antigravity Payload Format (LOCKED)

```json
{
  "drm_signal_id": "UUID",
  "signal_type": "stage_escalation | stage_recovery | structural | championship_hold",
  "user_id": "UUID",
  "tenant_id": "UUID",
  "dropout_stage": 3,
  "drs_score": 66.4,
  "drs_confidence": 81.2,
  "days_at_current_stage": 11,
  "chronic_flag": false,
  "compassion_hold": false,
  "top_dimension_1": { "id": "DS-001", "score": 74.2, "label": "Session Engagement Collapse" },
  "top_dimension_2": { "id": "DS-005", "score": 60.0, "label": "Championship Abandonment" },
  "top_dimension_3": { "id": "DS-003", "score": 48.1, "label": "Performance Deterioration" },
  "championship_window": true,
  "context_amplifier": 1.3,
  "parent_alert_level": "AG-MED",
  "active_intervention": "HUMAN-INT-002",
  "ag_alert_code": "PARENT_RISK_006",
  "timestamp": "ISO8601"
}
```

---

## 🔒 SECTION DRM-15 — SECURITY HARDENING

```
DROPOUT_DATA_ENCRYPTION          : AES-256 at rest (PostgreSQL + ClickHouse)
TRANSIT_ENCRYPTION               : TLS 1.3 on all DRM API endpoints
DROPOUT_DATA_CROSS_TENANT        : FORBIDDEN — row-level security enforced
RECRUITER_BLOCK                  : Hard enforcement at Kong API Gateway
                                   All recruiter tokens return zero dropout fields
STUDENT_LABEL_GUARD              : API serve layer maps DRS → care-framed content
                                   Raw stage labels stripped before student response
PARENT_NARRATIVE_CONTENT_FILTER  : All parent text validated against:
                                   (1) Forbidden terms list
                                   (2) Tone rule set
                                   Before delivery
MINOR_EXTRA_PROTECTION           : Under-13: no student-facing DRM content
                                   Under-16: no career-related DRM context
                                   Under-18: no economic disengagement shown to student
MENTOR_OVERRIDE_AUDIT            : Immutable SHA-256 hash on all override records
COMPASSION_HOLD_AUDIT            : Immutable SHA-256 hash on all hold records
INTERVENTION_AUDIT               : All intervention deliveries logged + hashed
CLINICAL_LANGUAGE_BLOCK          : Content filter active on all DRM text outputs
                                   Blocks: anxiety, depression, disorder, crisis, mental health
                                   (DRM flags these for human escalation, never self-diagnoses)
WINBACK_OPT_OUT                  : Hard enforced — opt-out silences all win-back immediately
DEBUG_MODE_BLOCK                 : DRM API never returns raw DRS vectors in production
TENANT_ISOLATION                 : Row-level security — zero cross-tenant exposure
```

---

## 🔒 SECTION DRM-16 — TEST REQUIREMENTS

```
UNIT TESTS:
  - DRS formula accuracy with known inputs for all 7 dimension scores
  - Context amplifier application (championship, belt, subscription windows)
  - Persistence multiplier boundary conditions
  - Stage assignment from DRS score boundary values
  - Evidence counter threshold enforcement (3-event rule)
  - Compassion hold: automated interventions suppressed during hold
  - Tone guard: all forbidden student-facing terms blocked
  - Parent tone guard: clinical + alarm language blocked
  - Recruiter block: zero dropout fields in recruiter API response
  - Minor protection: age-gated content enforcement
  - Streak integration: DS-002 score computation accuracy

INTEGRATION TESTS:
  - Kafka event → DRS update < 60 seconds
  - DRM → AG-CRIT signal on STAGE-5 escalation
  - DRM → Achievement Engine on STAGE-R recovery
  - DRM → Notification Service: intervention delivery
  - DRM → Championship Engine: bracket hold advisory
  - DRM → Streak Engine: protective nudge signal
  - DRM → Social Layer: study room suggestion
  - Compassion hold → all automated interventions paused
  - Cross-tenant isolation: zero leakage between tenants
  - RBAC enforcement: recruiter token returns zero dropout data

PREDICTIVE MODEL TESTS:
  - DPM prediction accuracy: precision/recall on held-out test set
  - False positive rate < 25% threshold enforcement
  - SHAP value generation for all projections
  - Confidence floor enforcement: < 60% suppressed
  - Parent projection: no probability numbers in parent output
  - Bias audit: gender, region, age group disparity analysis

LOAD TESTS:
  - 50,000 concurrent Kafka events → DRS update < 60s per event
  - Daily batch: 1M user recompute < 4 hours
  - Weekly deep analysis: cohort patterns < 6 hours
  - Parent projection batch: 500k projections < 4 hours
  - On-demand API: < 600ms at 10,000 concurrent requests

INTERVENTION EFFECTIVENESS TESTS:
  - AUTO-INT-001 through AUTO-INT-007: delivery confirmation rates
  - Frequency cap enforcement: max 2 per day per user
  - Quiet hours enforcement: no push 22:00–08:00
  - Win-back opt-out: immediate silence enforced
  - Championship context: bracket hold advisory requires mentor confirmation

SAFETY TESTS:
  - "Dropout" word: never appears in student or parent API response
  - Clinical terms: blocked in all output text
  - Parent probability numbers: never appear in parent response
  - Stage-5 compassion: dignity-preserving language verification
  - Minor under-13: no DRM content in student response
```

---

## 🔒 SECTION DRM-17 — FINAL GOVERNANCE SEAL BLOCK

```
╔══════════════════════════════════════════════════════════════════════════╗
║         ANTIGRAVITY DROPOUT RISK MODEL — FINAL MASTER SEAL              ║
╠══════════════════════════════════════════════════════════════════════════╣
║  ENGINE CLASS              : CHAMPIONSHIP ADVANCED + PARENT ML          ║
║  ARTIFACT TYPE             : AGENT SYSTEM SPECIFICATION                 ║
║  STATUS                    : LOCKED · SEALED · GOVERNED · FINAL         ║
║  MUTATION POLICY           : ADD-ONLY VIA VERSION BUMP                  ║
║  INTERPRETATION            : FORBIDDEN                                   ║
║  CREATIVE DEVIATION        : FORBIDDEN                                   ║
║  ASSUMPTION FILLING        : FORBIDDEN                                   ║
║  HUMAN OVERRIDE            : REQUIRED — MENTOR SUPERSEDES DRM ALWAYS    ║
║  AI AUTONOMY CEILING       : COMPUTATION + PREDICTION + ADVISORY        ║
║  CLINICAL DETERMINATION    : FORBIDDEN — NOT A DIAGNOSTIC TOOL          ║
║  "DROPOUT" IN STUDENT UI   : FORBIDDEN — ZERO TOLERANCE                 ║
║  "DROPOUT" IN PARENT UI    : FORBIDDEN — ZERO TOLERANCE                 ║
║  SHAME LANGUAGE            : FORBIDDEN — CARE FRAMING MANDATORY         ║
║  RECRUITER DATA ACCESS     : HARD BLOCK — GATEWAY ENFORCED              ║
║  MINOR PROTECTION          : STAGED AGE GATES ACTIVE                    ║
║  COMPASSION HOLD           : MENTOR-APPLIED — AUTOMATED PAUSE ACTIVE    ║
║  CHAMPIONSHIP INTEGRATION  : TOURNAMENT WINDOW AMPLIFIER ACTIVE         ║
║  GAMIFICATION INTEGRATION  : REWARD-FIRST, UNCONDITIONAL ONLY           ║
║  STREAK INTEGRATION        : R95 CONSUMED + PROTECTIVE SIGNALS FIRED    ║
║  SOCIAL INTEGRATION        : R92/R93/R94 SIGNALS CONSUMED               ║
║  STRUCTURAL DETECTION      : > 20% COHORT → AG-STRUCT MANDATORY         ║
║  ANTIGRAVITY INTEGRATION   : SIGNAL MAP LOCKED — 10 AG CODES MAPPED     ║
║  SECURITY MODE             : AES-256 + TLS 1.3 + RBAC + RLS            ║
║  OBSERVABILITY             : FULL TELEMETRY REQUIRED                    ║
║  TEST GATE                 : MANDATORY BEFORE PRODUCTION DEPLOY          ║
║  ABSENCE OF ANY SECTION    : STOP EXECUTION                             ║
╚══════════════════════════════════════════════════════════════════════════╝

ADD THIS BLOCK TO ECOSKILLER MASTER PROMPT:

ANTIGRAVITY DROPOUT RISK MODEL                 : ACTIVE
Championship Advanced DRM Layer                : ACTIVE
Parent Predictive ML Dropout Layer             : ACTIVE
7-Dimension Signal Registry                    : LOCKED
6-Stage Dropout Classification                 : LOCKED (including STAGE-R Recovery)
DRS Formula with Context Amplifier             : LOCKED
5-Cycle Computation Schedule                   : ACTIVE
7-Automated Intervention Catalog               : LOCKED
7-Human Intervention Catalog                   : LOCKED
Compassion Hold System                         : ACTIVE (Mentor-applied)
Tone Guard — Student UI                        : "DROPOUT" WORD FORBIDDEN
Tone Guard — Parent UI                         : CARE-FIRST MANDATORY
Probability Shield — Parent                    : NO PROBABILITY NUMBERS EVER
Recruiter Data Block                           : HARD GATEWAY ENFORCED
Minor Staged Protection                        : ACTIVE (Under-13/16/18)
Streak Engine Integration (R95)                : ACTIVE
Social Layer Integration (R92/R93/R94)         : ACTIVE
Gamification Integration                       : UNCONDITIONAL REWARD ONLY
Championship Window Amplifier (×1.3)           : ACTIVE
Structural Dropout Detection (> 20% Cohort)    : AG-STRUCT ENFORCED
Antigravity Signal Integration                 : 10 AG CODES LOCKED
Mentor Override Supremacy                      : ENFORCED + AUDITED
Clinical Language Block                        : ACTIVE
Cross-Tenant Isolation                         : HARD ENFORCED
Immutable Audit Chain                          : SHA-256 ACTIVE
Human Override Authority                       : ENFORCED
Interpretation Authority                       : NONE
Architecture Authority                         : LOCKED
```

---

*Dropout Risk Model (DRM) v1.0 — Antigravity Agent Specification*
*Ecoskiller Championship Advanced + Parent Predictive AI ML Layer*
*Sealed under DOJO SAAS PRODUCTION MODE + ECOSKILLER MASTER EXECUTION PROMPT v12.0*
*Add-only. No mutation without version bump. No interpretation. No assumption filling.*
*Antigravity Execution Engine — Deterministic. Governed. Locked.*
