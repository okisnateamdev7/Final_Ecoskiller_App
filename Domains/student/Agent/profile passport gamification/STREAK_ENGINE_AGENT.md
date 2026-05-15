# 🔒 STREAK_ENGINE_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED PRODUCTION ARTIFACT
### Domains: Dojo · Growth Engine · School Ops
### Intelligence Layer: Passive Intelligence Engine (PIE) Compatible

---

```
AGENT_SEAL: LOCKED
ARTIFACT_CLASS: PRODUCTION SYSTEM BLUEPRINT
MUTATION_POLICY: ADD-ONLY VIA VERSION BUMP
EXECUTION_MODE: DETERMINISTIC + VALIDATED
CREATIVE_INTERPRETATION: FORBIDDEN
ASSUMPTION_FILLING: FORBIDDEN
DEFAULT_BEHAVIOR: DENY
FAILURE_MODE: HALT_ON_AMBIGUITY
VERSION: 1.0.0
AUTHORED_FOR: ECOSKILLER ANTIGRAVITY PLATFORM
COMPANION_AGENT: PROBLEM_DETECTION_SCORING_AGENT v1.0.0
INTELLIGENCE_LAYER: EIE (Ecoskiller Intelligence Engine) Compatible
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME: STREAK_ENGINE_AGENT
AGENT_ID: ECSK-AGENT-SEA-002
SYSTEM_ROLE: >
  Authoritative, deterministic engine responsible for the complete lifecycle
  of all streak types across Dojo, Growth Engine, and School Ops domains.
  Manages streak creation, increment, validation, freeze, break, recovery,
  multiplier application, reward emission, abuse detection, and audit
  traceability — across all user types (Students, Trainers, Mentors,
  School Ops Owners, Institute Admins) within strict tenant and domain
  isolation. Also feeds passive intelligence signals to the Ecoskiller
  Intelligence Engine (EIE) based on streak behavior patterns.

PRIMARY_DOMAIN:
  - Dojo (Match Streak · Drill Streak · Practice Streak · Belt Progression Streak · Mentor Evaluation Streak)
  - Growth Engine (Daily Login Streak · XP Streak · Referral Streak · Challenge Streak · Win Streak · Consistency Streak)
  - School Ops (Student Attendance Streak · Cohort Engagement Streak · TPO Activity Streak · Placement Drive Streak · Compliance Streak)

EXECUTION_MODE: Deterministic + Validated
DATA_SCOPE: >
  Tenant-scoped only. Reads from: streak_registry, user_activity_log,
  match_events, xp_ledger, challenge_completion_log, attendance_records,
  cohort_engagement_log, tpo_activity_log, placement_drive_log,
  compliance_snapshot_store, feature_store, freeze_token_registry.
  Writes to: streak_registry, streak_audit_log, reward_emission_queue,
  xp_pending_queue, feature_store (streak intelligence features),
  abuse_flag_store, notification_trigger_queue.

TENANT_SCOPE: Strict Isolation — No cross-tenant streak data access permitted
FAILURE_POLICY: HALT_ON_AMBIGUITY — Log incident, emit escalation event, preserve streak state
AGENT_CLASS: ML-Primary (70%) + Rule Engine (20%) + AI-Assist (10%)
ARCHITECTURE: Stateless · Event-Driven · Horizontally Scalable · Idempotent
```

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Streaks are one of the most powerful behavioral retention mechanisms on Ecoskiller Antigravity. Across three domains — competitive Dojo practice, growth gamification, and school operational compliance — streak mechanics drive daily engagement, learning consistency, mentor accountability, and institutional performance visibility. Without a dedicated, authoritative streak agent, these mechanics become fragmented: a daily login streak might conflict with a Dojo match streak's XP multiplier, a cohort attendance streak might silently break without notification, or a win streak multiplier might be applied to an artificially farmed match sequence.

The `STREAK_ENGINE_AGENT` is the single source of truth for all streak state across the platform. It enforces streak integrity against fraud and manipulation, applies mathematically correct multipliers, coordinates reward emission downstream, and feeds behavioral consistency signals into the Ecoskiller Intelligence Engine (EIE) — enabling Passive Intelligence Measurement (PIM) to detect intrapersonal intelligence signals (goal-setting consistency, habit formation, self-discipline) without requiring explicit intelligence tests. This is a critical node in the platform's intelligence architecture, not just a gamification utility.

### What It Consumes

- Event streams from: Auth Service (login events), Match Engine (match completion), Challenge Engine (challenge completions), Attendance Service (student check-ins), XP Engine (XP award confirmations), Cohort Analytics (engagement snapshots), TPO Dashboard Service (TPO activity events)
- Batch triggers from: Streak Expiry Scheduler (daily 00:00 UTC per tenant timezone), Freeze Token Expiry Monitor (hourly), Compliance Snapshot Scheduler (daily)
- API calls from: Freeze Token Purchase Service (billing layer), Growth Engine (streak status queries), Notification Dispatch Agent (streak state queries for push messages)
- Hold release events from: PROBLEM_DETECTION_SCORING_AGENT (clears frozen streaks pending abuse review)

### What It Produces

- Updated `STREAK_RECORD` objects in `streak_registry`
- `REWARD_EMISSION_EVENT` to reward pipeline for XP, badges, multipliers
- `STREAK_FEATURE_VECTOR` to `FEATURE_STORE_AGENT` for EIE passive intelligence
- `STREAK_NOTIFICATION_TRIGGER` to `NOTIFICATION_DISPATCH_AGENT`
- `STREAK_AUDIT_LOG_ENTRY` to append-only audit store (every state change)
- `STREAK_ABUSE_FLAG` to `PROBLEM_DETECTION_SCORING_AGENT` when anomalies detected
- `RANK_UPDATE_TRIGGER` to `RANK_UPDATE_AGENT` when milestone streaks are achieved
- `XP_MULTIPLIER_TOKEN` to `XP_ENGINE_AGENT` when active multiplier streaks qualify

### Upstream Agents (Feed This Agent)

| Agent / Service | Data Provided |
|---|---|
| `AUTH_SERVICE` | Login event stream (daily login streak source) |
| `MATCH_ENGINE_AGENT` | Match completion events (Dojo match streak source) |
| `CHALLENGE_ENGINE_AGENT` | Challenge completion events (challenge streak source) |
| `DRILL_ENGINE_AGENT` | Drill completion events (practice streak source) |
| `ATTENDANCE_SERVICE` | Student check-in events (School Ops attendance streak) |
| `COHORT_ANALYTICS_AGENT` | Engagement snapshot events (cohort streak source) |
| `TPO_DASHBOARD_SERVICE` | TPO activity events (School Ops TPO streak source) |
| `BILLING_SERVICE` | Freeze token purchase confirmations |
| `PROBLEM_DETECTION_SCORING_AGENT` | Hold flags, abuse clearance signals |
| `XP_ENGINE_AGENT` | XP award confirmation (for XP consistency streak) |

### Downstream Agents (Depend On This Agent)

| Agent | What They Receive |
|---|---|
| `REWARD_EMISSION_AGENT` | `REWARD_EMISSION_EVENT` for XP, badges, multipliers |
| `XP_ENGINE_AGENT` | `XP_MULTIPLIER_TOKEN` for active win/consistency streaks |
| `RANK_UPDATE_AGENT` | `RANK_UPDATE_TRIGGER` for milestone streak achievements |
| `NOTIFICATION_DISPATCH_AGENT` | `STREAK_NOTIFICATION_TRIGGER` (at-risk, achieved, broken, milestone) |
| `FEATURE_STORE_AGENT` | `STREAK_FEATURE_VECTOR` for EIE passive intelligence |
| `PROBLEM_DETECTION_SCORING_AGENT` | `STREAK_ABUSE_FLAG` for anomaly review |
| `BELT_GOVERNANCE_AGENT` | Streak eligibility signal for belt promotion gate |
| `OBSERVABILITY_AGENT` | Streak metrics (active count, break rate, multiplier utilization) |
| `GROWTH_ENGINE_AGENT` | Streak state queries, leaderboard streak data |

---

## 3️⃣ STREAK TYPE TAXONOMY (LOCKED)

All streak types are registered. No streak may operate outside this taxonomy. New types require version bump.

### Domain: DOJO

```
DOJO-STREAK-001 | Match Streak
  Definition:    Consecutive days with at least 1 completed Dojo match
  Qualifier:     Match must be ranked or evaluation type (practice-only excluded)
  Break Rule:    Missed calendar day (tenant timezone) breaks streak
  Freeze Eligible: YES — freeze token applies
  XP Multiplier:  YES — activates at day 3, 7, 14, 30
  Belt Signal:    YES — sustained streak contributes to belt promotion eligibility
  Abuse Risk:     HIGH — monitor for match farming patterns

DOJO-STREAK-002 | Win Streak
  Definition:    Consecutive match wins (unbroken — a loss resets to 0)
  Qualifier:     Ranked matches only. Opponent rating band must be valid.
  Break Rule:    Any loss or draw resets counter
  Freeze Eligible: NO — win streaks cannot be frozen (performance-based)
  XP Multiplier:  YES — activated at 3, 5, 10, 20 consecutive wins
  Belt Signal:    YES — win streaks above threshold contribute to belt promotion
  Abuse Risk:     CRITICAL — monitor for collusion, smurf accounts, arranged losses

DOJO-STREAK-003 | Drill Practice Streak
  Definition:    Consecutive days completing at least 1 drill
  Qualifier:     Any drill type counts (practice-only eligible)
  Break Rule:    Missed calendar day breaks streak
  Freeze Eligible: YES
  XP Multiplier:  YES — activates at day 7, 21
  Belt Signal:    NO
  Abuse Risk:     LOW

DOJO-STREAK-004 | Mentor Evaluation Streak
  Definition:    Consecutive days a Mentor completes at least 1 evaluation
  Qualifier:     Evaluation must be submitted and not flagged for override abuse
  Break Rule:    Missed calendar day breaks streak
  Freeze Eligible: YES
  XP Multiplier:  YES — activates at day 5, 14
  Belt Signal:    N/A (Mentor role)
  Abuse Risk:     MEDIUM — monitor for batch evaluation spamming

DOJO-STREAK-005 | Scenario Attempt Streak
  Definition:    Consecutive days attempting at least 1 scenario
  Qualifier:     Scenario attempt must be started AND completed (abandon = no count)
  Break Rule:    Missed calendar day breaks streak
  Freeze Eligible: YES
  XP Multiplier:  NO
  Belt Signal:    YES (scenario breadth signal)
  Abuse Risk:     LOW
```

### Domain: GROWTH ENGINE

```
GROWTH-STREAK-001 | Daily Login Streak
  Definition:    Consecutive calendar days with at least 1 authenticated session
  Qualifier:     Session must be > 60 seconds (anti-bot threshold)
  Break Rule:    Missed calendar day (tenant timezone) breaks streak
  Freeze Eligible: YES — 1 freeze token per 30-day window maximum
  XP Multiplier:  YES — activates at day 3, 7, 14, 30, 60, 100, 365
  Belt Signal:    NO
  Abuse Risk:     MEDIUM — monitor for bot login patterns

GROWTH-STREAK-002 | XP Consistency Streak
  Definition:    Consecutive days earning XP through any qualifying activity
  Qualifier:     Minimum XP threshold per day must be met (configurable per tenant)
  Break Rule:    Missed day or XP below threshold breaks streak
  Freeze Eligible: YES
  XP Multiplier:  YES — multiplier stacks with login streak (capped)
  Belt Signal:    NO
  Abuse Risk:     MEDIUM — monitor for XP farming patterns

GROWTH-STREAK-003 | Referral Streak
  Definition:    Consecutive weeks with at least 1 successful referral completion
  Qualifier:     Referral must be confirmed (referred user completed Day 1 activity)
  Break Rule:    Missed week breaks streak
  Freeze Eligible: NO — referral streaks are action-gated
  XP Multiplier:  NO — referrals have separate reward track
  Belt Signal:    NO
  Abuse Risk:     HIGH — monitor for multi-account referral farming

GROWTH-STREAK-004 | Weekly Challenge Streak
  Definition:    Consecutive weeks completing the platform weekly challenge
  Qualifier:     Challenge must be the official weekly challenge (not custom)
  Break Rule:    Missed week breaks streak
  Freeze Eligible: YES — 1 freeze per quarter maximum
  XP Multiplier:  YES — activates at week 4, 8, 12
  Belt Signal:    NO
  Abuse Risk:     LOW

GROWTH-STREAK-005 | Win Streak Multiplier Streak
  Definition:    Consecutive days with an active Dojo Win Streak above threshold
  Qualifier:     DOJO-STREAK-002 must be active AND above configured threshold
  Break Rule:    Win streak drop or loss breaks this streak
  Freeze Eligible: NO
  XP Multiplier:  YES — bonus multiplier on top of Win Streak XP
  Belt Signal:    YES
  Abuse Risk:     CRITICAL — inherits Win Streak abuse risk

GROWTH-STREAK-006 | Innovation Activity Streak
  Definition:    Consecutive days completing at least 1 Innovation Engine activity
  Qualifier:     Innovation Engine (EIE-Innovation) activity must be logged
  Break Rule:    Missed calendar day breaks streak
  Freeze Eligible: YES
  XP Multiplier:  YES — activates at day 7, 30
  Belt Signal:    NO
  Abuse Risk:     LOW
  Intelligence Signal: YES — feeds Intrapersonal + Logical intelligence passive signals
```

### Domain: SCHOOL OPS

```
SCHOOL-STREAK-001 | Student Attendance Streak
  Definition:    Consecutive school days with recorded attendance
  Qualifier:     Attendance must be confirmed by institute system (not self-reported alone)
  Break Rule:    Unexcused absence breaks streak. Approved leave = FREEZE (automatic)
  Freeze Eligible: YES — automatic on approved leave record, no token required
  XP Multiplier:  YES — activates at day 10, 20, 30 of school term
  Belt Signal:    NO
  Abuse Risk:     MEDIUM — monitor for proxy attendance patterns

SCHOOL-STREAK-002 | Cohort Engagement Streak
  Definition:    Consecutive weeks where cohort engagement rate stays above baseline
  Qualifier:     Cohort-level metric, not individual (owned by School Ops Owner / TPO)
  Break Rule:    Week where engagement drops below baseline breaks streak
  Freeze Eligible: NO — cohort metric, not personal
  XP Multiplier:  NO — feeds into institute-level achievement system
  Belt Signal:    NO
  Abuse Risk:     LOW
  Escalation:     Cohort streak break triggers PROBLEM_DETECTION_SCORING_AGENT

SCHOOL-STREAK-003 | TPO Activity Streak
  Definition:    Consecutive weeks where TPO Officer completes required platform activities
  Qualifier:     Activities: update placement records, review cohort dashboard, log at least 1 recruiter contact
  Break Rule:    Missed week or incomplete required activities breaks streak
  Freeze Eligible: YES
  XP Multiplier:  NO — feeds into institute ranking
  Belt Signal:    NO
  Abuse Risk:     LOW

SCHOOL-STREAK-004 | Placement Drive Streak
  Definition:    Consecutive months with at least 1 active placement drive recorded
  Qualifier:     Drive must have at least 1 registered student and 1 participating recruiter
  Break Rule:    Missed month breaks streak
  Freeze Eligible: YES — 1 freeze per academic year
  XP Multiplier:  NO — feeds into institute performance score
  Belt Signal:    NO
  Abuse Risk:     MEDIUM — validate recruiter legitimacy

SCHOOL-STREAK-005 | Student Learning Streak
  Definition:    Consecutive days where student completes at least 1 learning activity (course, drill, scenario)
  Qualifier:     Activity must be in their enrolled skill track
  Break Rule:    Missed calendar day breaks streak
  Freeze Eligible: YES
  XP Multiplier:  YES — activates at day 7, 21, 60
  Belt Signal:    YES (learning consistency contributes to belt eligibility)
  Abuse Risk:     LOW
  Intelligence Signal: YES — feeds Intrapersonal intelligence passive signal (consistency, self-discipline)

SCHOOL-STREAK-006 | Compliance Reporting Streak
  Definition:    Consecutive months with on-time compliance snapshot submission by institute
  Qualifier:     Snapshot must be submitted within SLA window
  Break Rule:    Late or missed submission breaks streak
  Freeze Eligible: NO — compliance is non-negotiable
  XP Multiplier:  NO — feeds into institute trust score
  Belt Signal:    NO
  Abuse Risk:     LOW
  Escalation:     Streak break triggers PROBLEM_DETECTION_SCORING_AGENT (PDSA-SCHOOL-003)
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "event_id",
    "tenant_id",
    "user_id",
    "streak_type_code",
    "event_type",
    "event_timestamp_utc",
    "qualifying_entity_id",
    "qualifying_entity_type"
  ],
  "optional_fields": [
    "freeze_token_id",
    "approved_leave_reference",
    "match_id",
    "challenge_id",
    "cohort_id",
    "institute_id",
    "tpo_officer_id",
    "correlation_id",
    "prior_streak_ref"
  ],
  "validation_rules": [
    "event_id must be UUID v4",
    "tenant_id must exist in tenant_registry — reject unknown tenants",
    "user_id must exist and be active within the tenant — reject unknown or suspended users",
    "streak_type_code must exist in registered streak taxonomy — reject unknown codes",
    "event_type must be one of: [STREAK_QUALIFY, STREAK_FREEZE_REQUEST, STREAK_FREEZE_AUTO, STREAK_BREAK_FORCE, STREAK_QUERY, FREEZE_TOKEN_APPLY] — reject unknown types",
    "event_timestamp_utc must be ISO8601 and not in the future — reject future timestamps",
    "qualifying_entity_type must match allowed entity types for the streak_type_code — reject mismatches",
    "event_id must be unique within 48-hour deduplication window (idempotency)",
    "Freeze token ID, if provided, must exist in freeze_token_registry with UNUSED status",
    "For SCHOOL domain streaks: institute_id is required when streak owner is not individual user"
  ],
  "security_checks": [
    "Validate JWT caller identity — reject unsigned or expired tokens",
    "Validate caller tenant_id matches payload tenant_id — reject cross-tenant injections",
    "Validate source_agent is in registered agent whitelist",
    "Validate qualifying_entity_id belongs to the user_id within the tenant — prevent entity impersonation",
    "Validate payload HMAC signature — reject tampered events"
  ],
  "domain_checks": [
    "DOJO streak types may only be triggered by MATCH_ENGINE_AGENT, DRILL_ENGINE_AGENT, CHALLENGE_ENGINE_AGENT, MENTOR_CONTROL_AGENT",
    "GROWTH streak types may only be triggered by AUTH_SERVICE, XP_ENGINE_AGENT, REFERRAL_SERVICE, CHALLENGE_ENGINE_AGENT",
    "SCHOOL_OPS streak types may only be triggered by ATTENDANCE_SERVICE, COHORT_ANALYTICS_AGENT, TPO_DASHBOARD_SERVICE, COMPLIANCE_AUDIT_AGENT",
    "Cross-domain streak triggering is FORBIDDEN — source domain must match streak_type_code domain prefix"
  ]
}
```

**Null Tolerance Policy:**
- `tenant_id` = zero null tolerance — reject immediately
- `user_id` = zero null tolerance — reject immediately (system-level cohort streaks use cohort_id, not null user_id)
- `streak_type_code` = zero null tolerance — reject immediately
- `freeze_token_id` = nullable (only required for STREAK_FREEZE_REQUEST events)
- `approved_leave_reference` = nullable (required only for SCHOOL-STREAK-001 auto-freeze)

**On Validation Failure:**
1. `STOP_EXECUTION` for this event
2. Write `VALIDATION_FAILURE_LOG` to append-only streak audit store
3. Emit `INPUT_REJECTION_EVENT` to Kafka `ecsk.streak.rejection.v1`
4. Return structured rejection response to caller with rejection code + reason
5. Preserve existing streak state — no side effects from rejected events

---

## 5️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "streak_record_id": "UUID v4",
    "tenant_id": "string",
    "user_id": "string",
    "streak_type_code": "string — from registered taxonomy",
    "streak_domain": "DOJO | GROWTH | SCHOOL_OPS",
    "previous_streak_count": "int",
    "new_streak_count": "int",
    "streak_status": "ACTIVE | FROZEN | BROKEN | MILESTONE | AT_RISK | HOLD",
    "streak_start_date_utc": "ISO8601",
    "last_qualifying_event_utc": "ISO8601",
    "next_required_event_deadline_utc": "ISO8601",
    "freeze_tokens_remaining": "int",
    "active_freeze_expiry_utc": "ISO8601 | null",
    "current_multiplier": "float — 1.0 base, multiplied at milestone thresholds",
    "milestone_achieved": "boolean",
    "milestone_threshold_crossed": "int | null",
    "reward_emission_triggered": "boolean",
    "abuse_flag_raised": "boolean",
    "intelligence_signal_emitted": "boolean",
    "hold_active": "boolean"
  },
  "confidence_score": "float 0.0–1.0 (qualification confidence for edge-case events)",
  "model_version": "string — semver e.g. sea-ml-v1.2.0",
  "audit_reference": "UUID — links to append-only audit log entry",
  "next_trigger_event": [
    "ecsk.reward.v1 → REWARD_EMISSION_AGENT (if reward triggered)",
    "ecsk.xp.multiplier.v1 → XP_ENGINE_AGENT (if multiplier active)",
    "ecsk.notification.streak.v1 → NOTIFICATION_DISPATCH_AGENT",
    "ecsk.feature.v1 → FEATURE_STORE_AGENT (if intelligence signal)",
    "ecsk.streak.abuse.v1 → PROBLEM_DETECTION_SCORING_AGENT (if abuse flag)",
    "ecsk.rank.trigger.v1 → RANK_UPDATE_AGENT (if milestone)"
  ]
}
```

**Output Rules:**
- Streak state must never be written without confirmed audit log entry first
- `current_multiplier` must be computed by the locked multiplier formula — never freeform
- If `abuse_flag_raised = true`, streak status must be set to `HOLD` until cleared by `PROBLEM_DETECTION_SCORING_AGENT`
- `confidence_score < 0.70` on qualifying event → flag for REQUIRES_REVIEW but still count streak (conservative — don't punish user for system ambiguity). Log event for review.
- All reward emissions are signals only — actual reward logic lives in `REWARD_EMISSION_AGENT`

---

## 6️⃣ ML / AI LOGIC LAYER

### ML Layer (70% of Processing Logic)

```yaml
MODEL_TYPE:
  Primary: Binary Classification (qualifying event valid/invalid per streak type)
  Secondary: Anomaly Detection (abuse pattern detection across streak sequences)
  Tertiary: Time-Series Forecasting (streak at-risk prediction, churn prevention)

FEATURES_USED:
  Qualification Classifier:
    - event_source_agent_id (categorical)
    - qualifying_entity_type (categorical)
    - event_timestamp_hour_utc (int — detect off-hours bot activity)
    - user_activity_gap_since_last_event_seconds (int)
    - qualifying_entity_domain_match (boolean)
    - user_account_age_days (int)
    - session_duration_seconds (int — for login streak bot detection)
    - match_opponent_rating_delta (float — for win streak collusion)
    - match_same_opponent_repeat_count_7d (int — for farming detection)
    - drill_completion_time_seconds (int — speed anomaly for drill streak)

  Abuse Detection Model:
    - win_streak_opponent_overlap_ratio_7d (float)
    - login_event_ip_diversity_score (float)
    - streak_increment_regularity_score (float — too-regular = bot)
    - xp_source_diversity_score (float)
    - referral_referred_user_activation_rate (float)
    - account_pair_mutual_streak_correlation (float — colluding accounts)
    - event_burst_rate_per_hour (int — flash farming)

  At-Risk Forecasting:
    - hours_since_last_qualifying_event (int)
    - historical_streak_break_time_of_day (float)
    - user_timezone_adjusted_deadline_proximity (float)
    - notification_open_rate_last_7d (float — responsiveness to nudges)
    - day_of_week_activity_pattern_match (float)
    - freeze_token_balance (int)

TRAINING_FREQUENCY:
  Qualification Classifier: Weekly retrain on 90-day rolling window
  Abuse Detection: Continuous online update (mini-batch every 6 hours)
  At-Risk Forecasting: Daily retrain on 30-day rolling window

DRIFT_DETECTION:
  - Monitor PSI on all features weekly
  - Trigger DRIFT_ALERT if PSI > 0.20 on abuse detection features
  - Trigger MODEL_REFIT_REQUEST if abuse false-positive rate > 2%
  - All drift events logged and emitted to OBSERVABILITY_AGENT

VERSION_CONTROL:
  - All model artifacts semver tagged, immutable in model registry
  - Model version stored in every streak_record and audit entry
  - Rollback to N-1 supported without data migration
```

### Rule Engine (20% — Hard Deterministic Rules)

```yaml
RULE_ENGINE_RESPONSIBILITIES:
  - Calendar day boundary enforcement (per tenant timezone — not UTC)
  - Streak break on missed day (no ML involved — deterministic)
  - Freeze token application (deterministic — valid token = freeze applied)
  - Multiplier threshold crossing (deterministic — count reaches N = multiplier applied)
  - Compliance streak break (no ML — SLA window miss is binary)
  - School Ops auto-freeze on approved leave (deterministic — leave record = freeze)
  - Duplicate event rejection via idempotency cache (deterministic)
  - Win streak reset on loss (deterministic — loss event = counter reset)

RULE_EXECUTION_ORDER:
  1. Input validation (schema + security)
  2. Idempotency check (reject duplicates)
  3. Freeze state check (is streak frozen? → skip break logic)
  4. Hold state check (is streak on hold? → queue event, do not process)
  5. Calendar day boundary check (has qualifying window expired?)
  6. ML qualification classifier (is this event a valid qualifier?)
  7. Abuse detection model (is there an anomaly pattern?)
  8. Streak counter update
  9. Multiplier computation
  10. Milestone check
  11. Reward emission trigger
  12. Intelligence signal emission
  13. Audit log write
  14. Output event emission
```

### AI Layer (10% — Assist Only)

```yaml
AI_USAGE_SCOPE:
  - Structured human-readable streak milestone messages for notification payloads
  - Edge-case disambiguation for novel event patterns with no ML training data
  - Natural language summary in streak analytics reports for School Ops Owners
  - At-risk streak notification copy personalisation (tone, urgency level)

AI_BOUNDARY_RULES:
  - AI may NEVER determine if an event is a valid qualifier
  - AI may NEVER decide to apply or deny a freeze token
  - AI may NEVER set streak counts, multipliers, or milestone flags
  - AI may NEVER trigger reward emissions or abuse flags
  - AI output is used ONLY for human-readable text fields
  - AI timeout = 2000ms. Fallback = use registered template from notification taxonomy.

PROMPT_GOVERNANCE:
  - All AI prompts version-controlled alongside agent codebase
  - Prompt version stored in audit log for every AI-assisted output
  - Prompts are deterministic templates with variable injection only
  - Zero creative interpretation permitted
```

---

## 7️⃣ STREAK STATE MACHINE (LOCKED)

All streaks operate as deterministic state machines. States and transitions are fixed.

```
STATES:
  INACTIVE    → Streak not yet started for this user/type
  ACTIVE      → Streak is running, within qualifying window
  AT_RISK     → Qualifying window expires in < 4 hours (notification zone)
  FROZEN      → Freeze token applied, window paused (no break possible)
  MILESTONE   → Streak has crossed a milestone threshold (transient — resolves to ACTIVE)
  HOLD        → Abuse flag raised by PROBLEM_DETECTION_SCORING_AGENT — no increments
  BROKEN      → Qualifying window expired without event — counter reset to 0
  ARCHIVED    → Historical record only (after user account deactivation or explicit reset)

TRANSITIONS (LOCKED):
  INACTIVE → ACTIVE:      First qualifying event received and validated
  ACTIVE → AT_RISK:       System detects window expires < 4 hours
  AT_RISK → ACTIVE:       Qualifying event received before window expiry
  AT_RISK → BROKEN:       Window expired without qualifying event
  ACTIVE → FROZEN:        Valid freeze token applied
  FROZEN → ACTIVE:        Freeze token expires — window reset to standard duration
  ACTIVE → MILESTONE:     Streak count crosses milestone threshold
  MILESTONE → ACTIVE:     Reward emitted — streak continues
  ACTIVE → HOLD:          Abuse flag raised by PROBLEM_DETECTION_SCORING_AGENT
  HOLD → ACTIVE:          Hold cleared by PROBLEM_DETECTION_SCORING_AGENT after review
  HOLD → BROKEN:          Hold cleared AND qualifying window found to have expired during hold
  ACTIVE → BROKEN:        Qualifying window expired without event
  BROKEN → ACTIVE:        New qualifying event after break — counter resets to 1
  ANY → ARCHIVED:         User deactivation or admin-initiated reset with audit

INVALID TRANSITIONS (FORBIDDEN):
  BROKEN → FROZEN         (cannot freeze an already broken streak)
  FROZEN → MILESTONE      (frozen streaks cannot cross milestones)
  HOLD → FROZEN           (cannot freeze a held streak — hold takes precedence)
  ARCHIVED → ANY          (archived is terminal)
```

---

## 8️⃣ FREEZE TOKEN SYSTEM (LOCKED)

Freeze tokens allow users to pause their streak window — protecting a streak during unavoidable inactivity.

```yaml
FREEZE_TOKEN_RULES:
  Acquisition:
    - Tokens purchased via billing layer (Stripe — provider abstracted)
    - Tokens may be earned via achievement milestones (max 1 per milestone event)
    - Tokens may be gifted by institute admin to students (School Ops only)
    - Tokens are non-transferable between users or tenants

  Validity:
    - Each token has a TTL: 90 days from acquisition — expires unused if not applied
    - Applied tokens freeze the streak for exactly 24 hours (standard)
    - School Ops auto-freeze (on approved leave): Duration = leave duration + 1 day grace period
    - Token application is idempotent — applying same token twice is a no-op

  Limits by Streak Type:
    - GROWTH-STREAK-001 (Daily Login): max 1 token per 30-day window
    - GROWTH-STREAK-004 (Weekly Challenge): max 1 token per quarter
    - DOJO-STREAK-001 (Match Streak): max 2 tokens per 30-day window
    - SCHOOL-STREAK-005 (Student Learning): max 1 token per 14-day window
    - WIN STREAKS: FREEZE FORBIDDEN — no token applies to performance-based streaks
    - COMPLIANCE STREAKS: FREEZE FORBIDDEN — compliance is non-negotiable

  Storage:
    - `freeze_token_registry` table: {token_id, user_id, tenant_id, acquired_at, expires_at, status [UNUSED|APPLIED|EXPIRED], applied_to_streak_id, applied_at}
    - Status transitions: UNUSED → APPLIED (one-way), UNUSED → EXPIRED (on TTL), APPLIED is terminal
    - No token deletion — only status transitions (append-only model)

  Audit:
    - Every token application writes to streak_audit_log
    - Token balance visible to user in Growth Dashboard
    - Admin can view but not modify token registry (read-only for admins)
```

---

## 9️⃣ XP MULTIPLIER SYSTEM (LOCKED)

Streaks power the XP multiplier economy. Multipliers are deterministic — no ML or AI involvement.

```yaml
MULTIPLIER_FORMULA:
  base_multiplier: 1.0
  milestone_multiplier: computed per streak type and threshold crossing (see table below)
  stacking_policy: CAPPED — max combined multiplier = 3.0x across all active streaks
  stacking_rule: Sum of all active multipliers, capped at 3.0x

MULTIPLIER_TABLE (LOCKED — version-controlled):

  DOJO-STREAK-001 (Match Streak):
    Day 3:  1.1x
    Day 7:  1.2x
    Day 14: 1.3x
    Day 30: 1.5x

  DOJO-STREAK-002 (Win Streak):
    3 wins:  1.15x
    5 wins:  1.25x
    10 wins: 1.40x
    20 wins: 1.75x

  DOJO-STREAK-003 (Drill Practice Streak):
    Day 7:  1.1x
    Day 21: 1.2x

  DOJO-STREAK-004 (Mentor Evaluation Streak):
    Day 5:  1.1x
    Day 14: 1.2x

  GROWTH-STREAK-001 (Daily Login Streak):
    Day 3:   1.05x
    Day 7:   1.10x
    Day 14:  1.15x
    Day 30:  1.20x
    Day 60:  1.25x
    Day 100: 1.30x
    Day 365: 1.50x

  GROWTH-STREAK-002 (XP Consistency Streak):
    Day 7:  1.10x
    Day 21: 1.20x

  GROWTH-STREAK-004 (Weekly Challenge Streak):
    Week 4:  1.10x
    Week 8:  1.20x
    Week 12: 1.30x

  GROWTH-STREAK-006 (Innovation Activity Streak):
    Day 7:  1.10x
    Day 30: 1.20x

  SCHOOL-STREAK-005 (Student Learning Streak):
    Day 7:  1.10x
    Day 21: 1.20x
    Day 60: 1.30x

  WIN STREAK MULTIPLIER STACKING NOTE:
    GROWTH-STREAK-005 (Win Streak Multiplier Streak) adds +0.10x per day
    it is active ON TOP of DOJO-STREAK-002 multiplier.
    Combined Win Streak cap: 2.0x before global cap applied.

MULTIPLIER_EMISSION:
  - `XP_MULTIPLIER_TOKEN` emitted to XP_ENGINE_AGENT when user earns XP
  - Token contains: {user_id, tenant_id, multiplier_value, valid_until_utc, streak_refs}
  - XP_ENGINE_AGENT applies multiplier to next XP award within valid window
  - Multiplier does NOT retroactively apply to past XP
  - Multiplier token expires at end of current qualifying window
```

---

## 🔟 STREAK MILESTONE REWARD TAXONOMY (LOCKED)

```yaml
MILESTONE_REWARDS_BY_STREAK_TYPE:

  DOJO-STREAK-001 (Match Streak) Milestones:
    Day 7:   Badge: "Week Warrior" + 500 XP bonus
    Day 14:  Badge: "Fortnight Fighter" + 1000 XP + rank signal
    Day 30:  Badge: "Month Marathoner" + 2500 XP + SHARE_TRIGGER_EVENT
    Day 90:  Badge: "Seasonal Samurai" + 5000 XP + belt eligibility signal
    Day 365: Badge: "Year Yogi" + 10000 XP + Certification acknowledgement

  DOJO-STREAK-002 (Win Streak) Milestones:
    3 wins:  Badge: "Hat Trick" + 300 XP
    5 wins:  Badge: "On Fire" + 750 XP + SHARE_TRIGGER_EVENT
    10 wins: Badge: "Unstoppable" + 2000 XP + rank signal
    20 wins: Badge: "Legend Run" + 5000 XP + belt eligibility signal + SHARE_TRIGGER_EVENT

  GROWTH-STREAK-001 (Daily Login Streak) Milestones:
    Day 7:   Badge: "7-Day Spark" + 200 XP
    Day 30:  Badge: "Monthly Master" + 750 XP + SHARE_TRIGGER_EVENT
    Day 100: Badge: "Century Clubber" + 2000 XP + SHARE_TRIGGER_EVENT
    Day 365: Badge: "Year-Long Legend" + 10000 XP + premium benefit unlock + SHARE_TRIGGER_EVENT

  SCHOOL-STREAK-001 (Student Attendance Streak) Milestones:
    10 days: Badge: "Consistent Learner" + 200 XP
    30 days: Badge: "Perfect Month" + 500 XP + parent notification trigger
    Full Term: Badge: "Term Champion" + institute award signal

  SCHOOL-STREAK-005 (Student Learning Streak) Milestones:
    Day 7:   Badge: "Learning Ignited" + 300 XP
    Day 21:  Badge: "Three Week Thinker" + 750 XP
    Day 60:  Badge: "Learning Machine" + 2000 XP + intelligence certificate signal

  INNOVATION-STREAK-001 (Innovation Activity Streak) Milestones:
    Day 7:   Badge: "Problem Spotter" + 400 XP + Intelligence signal (Logical ↑)
    Day 30:  Badge: "Innovation Habit" + 1500 XP + Intelligence signal (Intrapersonal ↑)

REWARD_EMISSION_CONTRACT:
  - Milestones emit REWARD_EMISSION_EVENT to REWARD_EMISSION_AGENT
  - This agent does NOT process rewards — it signals only
  - SHARE_TRIGGER_EVENT emitted to NOTIFICATION_DISPATCH_AGENT for social sharing prompts
  - Parent notification trigger emitted to NOTIFICATION_DISPATCH_AGENT (School Ops, read-only parent)
  - All reward emissions logged to streak_audit_log before sending
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE ENGINE (PIE) INTEGRATION

The `STREAK_ENGINE_AGENT` is a primary data source for the Ecoskiller Intelligence Engine (EIE). Behavioral consistency measured by streaks is a direct signal for **Intrapersonal Intelligence** (self-discipline, goal-setting, habit tracking) as defined by the Howard Gardner model integrated into Ecoskiller's architecture.

```yaml
INTELLIGENCE_SIGNAL_MAPPING:

  Student Learning Streak (SCHOOL-STREAK-005):
    Intelligence Type: Intrapersonal (Self Intelligence)
    Signal: daily_learning_consistency_score
    Weight: 20% of Intrapersonal passive score
    Trigger: Every 7-day milestone or streak break

  Innovation Activity Streak (GROWTH-STREAK-006):
    Intelligence Types: Intrapersonal + Logical
    Signal: innovation_habit_consistency_score
    Weight: 15% of Intrapersonal + 10% of Logical passive score
    Trigger: Every qualifying event

  Drill Practice Streak (DOJO-STREAK-003):
    Intelligence Type: Intrapersonal + domain-specific (Logical for coding drills, Spatial for design drills)
    Signal: skill_practice_consistency_score
    Weight: 10% of Intrapersonal passive score + domain-specific signal
    Trigger: Every 7-day milestone

  Match Streak (DOJO-STREAK-001):
    Intelligence Type: Interpersonal (competitive social engagement consistency)
    Signal: competitive_engagement_consistency_score
    Weight: 10% of Interpersonal passive score
    Trigger: Every milestone

  Win Streak (DOJO-STREAK-002):
    Intelligence Type: Logical (strategic thinking consistency)
    Signal: strategic_performance_consistency_score
    Weight: 10% of Logical passive score
    Trigger: Every milestone

  Attendance Streak (SCHOOL-STREAK-001):
    Intelligence Type: Intrapersonal
    Signal: physical_consistency_score
    Weight: 10% of Intrapersonal passive score
    Trigger: Every 10-day milestone or break

EMIT_FEATURE_VECTOR_FORMAT:
{
  "user_id": "string",
  "tenant_id": "string",
  "entity_id": "streak_record_id",
  "entity_type": "STREAK",
  "feature_name": "string — from intelligence signal taxonomy above",
  "feature_value": "float 0.0–1.0 (normalized streak health score)",
  "feature_domain": "DOJO | GROWTH | SCHOOL_OPS",
  "feature_category": "INTELLIGENCE_SIGNAL",
  "intelligence_type": "INTRAPERSONAL | INTERPERSONAL | LOGICAL | SPATIAL",
  "timestamp": "ISO8601",
  "source_agent": "ECSK-AGENT-SEA-002",
  "model_version": "string"
}

INTELLIGENCE_SIGNAL_RULES:
  - Signals must only be emitted on milestone crossings or break events (not on every qualifying event — reduces noise)
  - Break events emit a negative signal (feature_value decreases)
  - Milestone events emit a positive signal
  - Intelligence signals from streaks are advisory — they feed the EIE but do not override explicit test results
  - AI Mentor Engine may reference streak-derived intelligence signals when advising users
```

---

## 1️⃣2️⃣ STREAK ABUSE DETECTION (LOCKED)

```yaml
ABUSE_PATTERNS_MONITORED:

  PATTERN-ABUSE-001 | Win Streak Collusion
    Description: Two or more accounts repeatedly playing each other with one always winning
    Detection: match_same_opponent_repeat_count_7d > threshold + mutual win_loss_alternation detected
    Severity: CRITICAL
    Action: Raise STREAK_ABUSE_FLAG → PROBLEM_DETECTION_SCORING_AGENT (PDSA-DOJO-002)
    Streak Action: Set HOLD status on both accounts' win streaks

  PATTERN-ABUSE-002 | Login Bot Farming
    Description: Login events from single IP or device with < 60 second sessions
    Detection: session_duration_seconds < 60 AND ip_diversity_score < threshold
    Severity: HIGH
    Action: Raise STREAK_ABUSE_FLAG → PROBLEM_DETECTION_SCORING_AGENT
    Streak Action: Set HOLD status, flag for human review

  PATTERN-ABUSE-003 | Referral Chain Farming
    Description: Multiple referrals from same IP or device cluster with no real activation
    Detection: referred_user_activation_rate < 0.20 AND referral_source_ip_cluster detected
    Severity: HIGH
    Action: Raise STREAK_ABUSE_FLAG → PROBLEM_DETECTION_SCORING_AGENT (PDSA-GROWTH-008)
    Streak Action: Freeze referral streak rewards pending review (not break)

  PATTERN-ABUSE-004 | XP Farming via Streak Stacking
    Description: User artificially maintaining maximum streak count across all types to exploit multiplier cap
    Detection: active_streak_count = ALL_TYPES AND xp_multiplier = MAX AND xp_source_diversity_score < threshold
    Severity: MEDIUM
    Action: Raise STREAK_ABUSE_FLAG for review — do not auto-break
    Streak Action: No state change — log only. Human review decides.

  PATTERN-ABUSE-005 | Drill Speed Anomaly
    Description: Drill completions consistently below minimum realistic time threshold
    Detection: drill_completion_time_seconds < configurable_minimum_per_drill_type
    Severity: MEDIUM
    Action: Reject qualifying event (do not increment drill streak)
    Streak Action: Log rejection. If pattern repeats 3+ times in 24h → STREAK_ABUSE_FLAG

  PATTERN-ABUSE-006 | Proxy Attendance
    Description: Attendance events recorded from same device/IP for multiple students simultaneously
    Detection: ip_or_device_collision_count > 1 within same timestamp window
    Severity: HIGH
    Action: Reject attendance event for all colliding records
    Streak Action: Do not increment attendance streaks for flagged records

  ABUSE_RESOLUTION_POLICY:
    - This agent applies HOLD — it does not resolve abuse
    - Resolution authority: PROBLEM_DETECTION_SCORING_AGENT → GOVERNANCE_BOARD
    - On clearance: HOLD lifted, streak state evaluated at clearance time
    - If qualifying window expired during HOLD: streak BROKEN (user not penalized retroactively for system hold)
    - If qualifying window still valid at clearance: streak CONTINUES from last valid count
```

---

## 1️⃣3️⃣ NOTIFICATION STRATEGY (LOCKED)

```yaml
NOTIFICATION_TRIGGERS:

  STREAK_AT_RISK:
    Condition: qualifying window expires in < 4 hours
    Target: user (primary), parent (School Ops students, read-only)
    Channel: Push notification (Flutter app) + in-app banner
    Frequency: Maximum 2 notifications per at-risk window (at 4h and at 1h)
    Priority: HIGH
    Message Type: AI-generated from template (urgency: medium)

  STREAK_BROKEN:
    Condition: qualifying window expired — streak reset to 0
    Target: user
    Channel: Push notification + in-app
    Frequency: 1 per break event
    Priority: MEDIUM
    Message Type: Template (supportive tone, encourages restart)
    Suppression: If user has broken this streak > 3 times in 7 days → suppress to avoid annoyance

  STREAK_MILESTONE:
    Condition: streak count crosses milestone threshold
    Target: user + social share prompt
    Channel: Push notification + in-app celebration UI + SHARE_TRIGGER_EVENT
    Frequency: 1 per milestone
    Priority: HIGH
    Message Type: AI-generated celebratory message from template

  STREAK_FROZEN:
    Condition: freeze token applied
    Target: user
    Channel: In-app confirmation only (no push — user initiated this)
    Frequency: 1 per freeze application
    Priority: LOW

  FREEZE_TOKEN_EXPIRING:
    Condition: unused freeze token TTL < 7 days
    Target: user
    Channel: Push notification
    Frequency: 1 reminder
    Priority: LOW

  STREAK_HOLD_APPLIED:
    Condition: abuse flag raised, streak set to HOLD
    Target: user (generic hold message — do not reveal abuse detection details)
    Channel: In-app only (no push — sensitive)
    Message: "Your streak is temporarily paused for a system review. No action needed."
    Priority: MEDIUM

  PARENT_MILESTONE_NOTIFICATION (School Ops):
    Condition: SCHOOL-STREAK-001 Day 30 milestone or SCHOOL-STREAK-005 Day 21 milestone
    Target: Parent (read-only role — notification only)
    Channel: Push notification to parent app
    Message: Template (student name + achievement — no grade data)
    Priority: MEDIUM

NOTIFICATION_RULES:
  - All notifications emitted as STREAK_NOTIFICATION_TRIGGER to NOTIFICATION_DISPATCH_AGENT
  - This agent does NOT send notifications — it triggers only
  - Notification content templates are version-controlled in notification taxonomy
  - AI-generated content is used for personalization of template fields only
  - Notification suppression logic is enforced here (frequency caps)
```

---

## 1️⃣4️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS: 8,000 streak events/second (peak: school term start + Dojo tournament + login burst)
LATENCY_TARGET:
  p50: < 100ms (event → streak state update → audit write)
  p95: < 350ms
  p99: < 800ms
MAX_CONCURRENCY: 800 parallel event processing workers
QUEUE_STRATEGY:
  - Kafka topic per domain: ecsk.streak.dojo.v1 | ecsk.streak.growth.v1 | ecsk.streak.school.v1
  - Priority lane: WIN_STREAK events get dedicated high-priority partition (tournament contexts)
  - Dead letter queue per domain: ecsk.streak.dlq.{domain}.v1
  - Freeze token events: dedicated topic ecsk.streak.freeze.v1 (lower volume, high precision)
IDEMPOTENCY:
  - Redis deduplication cache per event_id (TTL 48 hours)
  - Streak state updates wrapped in optimistic locking (version field on streak_record)
  - Concurrent increments for same user+streak_type: last-write-wins with audit trail
SCALING_POLICY:
  - Stateless workers — all state in streak_registry + Redis cache
  - Horizontal pod autoscaling on Kafka consumer lag metric
  - Streak expiry scheduler: distributed lock (Redis) — only one scheduler worker per tenant timezone per window
TIMEZONE_HANDLING:
  - All streak windows computed in TENANT_TIMEZONE (stored in tenant_registry)
  - Streak day boundary = midnight in tenant timezone, not UTC
  - UTC timestamps stored in all records for audit; timezone-adjusted for business logic
```

---

## 1️⃣5️⃣ SECURITY ENFORCEMENT

```yaml
SECURITY_CONTROLS:
  Tenant Isolation:
    - All DB queries scope to tenant_id via row-level security (enforced at DB layer)
    - Cross-tenant streak query = IMMEDIATE HALT + SECURITY_INCIDENT_LOG
    - tenant_id from JWT claim validated against payload on every event

  User Isolation:
    - Streak records are user-scoped — no user can read or influence another user's streak
    - Admin read access to streak registry is read-only, scoped to their tenant

  Domain Isolation:
    - DOJO streak events must come from DOJO-registered source agents only
    - SCHOOL_OPS streak events must be verified against institute_id + tenant_id

  Freeze Token Security:
    - Token IDs are UUIDs — non-guessable
    - Token application requires matching user_id + tenant_id — no token lending
    - Applied tokens are immediately marked APPLIED in registry — prevent replay attacks

  Encryption:
    - user_id, student_id in streak records encrypted at rest via platform key vault
    - Inter-agent communication over TLS 1.3 minimum
    - No plaintext secrets in agent configuration

  Audit:
    - Every streak state change writes to append-only audit log before state commit
    - Audit log immutable — no deletes, no updates
    - Failed audit write = STOP_EXECUTION (streak state not committed)
```

---

## 1️⃣6️⃣ AUDIT & TRACEABILITY

Every streak state change produces the following immutable audit entry:

```json
{
  "audit_id": "UUID v4",
  "timestamp_utc": "ISO8601",
  "agent_id": "ECSK-AGENT-SEA-002",
  "tenant_id": "string",
  "user_id": "string",
  "streak_record_id": "UUID",
  "streak_type_code": "string",
  "actor_id": "source_agent_id",
  "event_id": "UUID — source event",
  "input_hash": "SHA-256 of input payload",
  "previous_state": "INACTIVE | ACTIVE | AT_RISK | FROZEN | MILESTONE | HOLD | BROKEN",
  "new_state": "INACTIVE | ACTIVE | AT_RISK | FROZEN | MILESTONE | HOLD | BROKEN",
  "previous_count": "int",
  "new_count": "int",
  "state_transition": "string — e.g. ACTIVE→MILESTONE",
  "transition_reason": "QUALIFYING_EVENT | WINDOW_EXPIRY | FREEZE_APPLIED | ABUSE_FLAG | HOLD_CLEARED | MILESTONE_CROSSED",
  "multiplier_applied": "float",
  "reward_emitted": "boolean",
  "freeze_token_id": "UUID | null",
  "abuse_flag_raised": "boolean",
  "intelligence_signal_emitted": "boolean",
  "ml_confidence_score": "float",
  "model_version": "string",
  "rule_path": "string — rule engine path if rule-based decision",
  "execution_duration_ms": "int"
}
```

---

## 1️⃣7️⃣ FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  Invalid Input:
    action: STOP_EXECUTION — do not modify streak state
    log: VALIDATION_FAILURE to streak_audit_log
    emit: INPUT_REJECTION_EVENT to ecsk.streak.rejection.v1
    streak_state: PRESERVED — no side effects
    retry: NONE — caller must resubmit corrected event

  Streak State DB Write Failure:
    action: STOP_EXECUTION — do not emit downstream events without confirmed state write
    log: DB_WRITE_FAILURE_INCIDENT
    emit: INCIDENT_EVENT to OBSERVABILITY_AGENT
    retry: Exponential backoff 500ms/1s/2s — max 3 attempts
    escalate: After 3 failures → PLATFORM_OPS CRITICAL alert
    streak_state: ROLLBACK to last confirmed state

  Audit Log Write Failure:
    action: STOP_EXECUTION — do not commit streak state without audit trail
    log: AUDIT_WRITE_FAILURE to secondary backup audit store
    escalate: CRITICAL to GOVERNANCE_BOARD + PLATFORM_OPS immediately
    retry: 3 attempts at 500ms interval
    streak_state: NOT COMMITTED

  Freeze Token Registry Failure:
    action: STOP_EXECUTION for freeze request
    log: FREEZE_TOKEN_REGISTRY_FAILURE
    streak_state: PRESERVED — freeze not applied, streak continues
    emit: INCIDENT_EVENT to OBSERVABILITY_AGENT
    retry: Exponential backoff, max 3 attempts

  ML Model Unavailable:
    action: Fall back to rule engine for threshold-based decisions
    log: ML_MODEL_UNAVAILABLE
    escalate: OBSERVABILITY_AGENT HIGH alert
    streak_state: Conservative approach — do not break streak during ML unavailability (preserve)
    note: Qualifying events during ML outage are queued and processed on model recovery

  AI Assistant Timeout (> 2000ms):
    action: Use registered template for notification/description fields
    log: AI_TIMEOUT (non-blocking)
    streak_state: UNAFFECTED — AI is non-critical path

  Kafka Emit Failure (downstream events):
    action: Buffer in Redis retry queue (TTL 2 hours)
    log: KAFKA_EMIT_FAILURE
    retry: Exponential backoff, max 5 attempts
    escalate: After 5 failures → OBSERVABILITY_AGENT HIGH alert

  Streak Window Calculation Error (timezone):
    action: STOP_EXECUTION — do not apply break logic on ambiguous window
    log: TIMEZONE_CALCULATION_ERROR
    escalate: PLATFORM_OPS immediately
    streak_state: FROZEN temporarily until timezone confirmed

  Freeze Token Replay Attack Detected:
    action: STOP_EXECUTION — reject event
    log: SECURITY_INCIDENT to SECURITY_AGENT + audit log
    escalate: SECURITY_AGENT + GOVERNANCE_BOARD CRITICAL
    streak_state: PRESERVED

ESCALATE_TO_MAP:
  CRITICAL: [GOVERNANCE_BOARD, PLATFORM_OPS]
  HIGH: [PLATFORM_OPS, OBSERVABILITY_AGENT]
  SECURITY: [SECURITY_AGENT, GOVERNANCE_BOARD]
  STREAK_ABUSE: [PROBLEM_DETECTION_SCORING_AGENT]

RETRY_POLICY:
  DEFAULT: Exponential backoff 500ms, 1s, 2s — max 3 attempts
  KAFKA: 1s, 2s, 4s, 8s, 16s — max 5 attempts
  AUDIT: 500ms, 500ms, 500ms — max 3 attempts → then CRITICAL escalation
```

---

## 1️⃣8️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - AUTH_SERVICE               → Login events (GROWTH-STREAK-001)
  - MATCH_ENGINE_AGENT         → Match completion events (DOJO-STREAK-001, 002)
  - DRILL_ENGINE_AGENT         → Drill completion events (DOJO-STREAK-003, 005)
  - CHALLENGE_ENGINE_AGENT     → Challenge completion events (GROWTH-STREAK-004)
  - MENTOR_CONTROL_AGENT       → Evaluation completion events (DOJO-STREAK-004)
  - XP_ENGINE_AGENT            → XP award confirmation (GROWTH-STREAK-002)
  - REFERRAL_SERVICE           → Referral completion events (GROWTH-STREAK-003)
  - ATTENDANCE_SERVICE         → Student check-in events (SCHOOL-STREAK-001)
  - COHORT_ANALYTICS_AGENT     → Cohort engagement snapshots (SCHOOL-STREAK-002)
  - TPO_DASHBOARD_SERVICE      → TPO activity events (SCHOOL-STREAK-003, 004)
  - COMPLIANCE_AUDIT_AGENT     → Compliance snapshot events (SCHOOL-STREAK-006)
  - BILLING_SERVICE            → Freeze token purchase confirmations
  - PROBLEM_DETECTION_SCORING_AGENT → Hold flags, abuse clearance signals
  - INNOVATION_ENGINE_AGENT    → Innovation activity completions (GROWTH-STREAK-006)

DOWNSTREAM_AGENTS:
  - REWARD_EMISSION_AGENT          → REWARD_EMISSION_EVENT
  - XP_ENGINE_AGENT                → XP_MULTIPLIER_TOKEN
  - RANK_UPDATE_AGENT              → RANK_UPDATE_TRIGGER (milestone)
  - NOTIFICATION_DISPATCH_AGENT    → STREAK_NOTIFICATION_TRIGGER
  - FEATURE_STORE_AGENT            → STREAK_FEATURE_VECTOR (intelligence signals)
  - PROBLEM_DETECTION_SCORING_AGENT → STREAK_ABUSE_FLAG
  - BELT_GOVERNANCE_AGENT          → STREAK_ELIGIBILITY_SIGNAL
  - OBSERVABILITY_AGENT            → STREAK_METRICS

EVENT_TOPICS_EMITTED:
  ecsk.streak.state.v1          → REWARD_EMISSION_AGENT, RANK_UPDATE_AGENT, BELT_GOVERNANCE_AGENT
  ecsk.streak.notification.v1   → NOTIFICATION_DISPATCH_AGENT
  ecsk.streak.multiplier.v1     → XP_ENGINE_AGENT
  ecsk.streak.abuse.v1          → PROBLEM_DETECTION_SCORING_AGENT
  ecsk.streak.feature.v1        → FEATURE_STORE_AGENT
  ecsk.streak.rejection.v1      → OBSERVABILITY_AGENT, source caller
  ecsk.streak.metrics.v1        → OBSERVABILITY_AGENT
```

---

## 1️⃣9️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  active_streak_count_by_type:
    definition: Count of streaks in ACTIVE or AT_RISK state, per streak_type_code, per tenant
    purpose: Engagement health indicator per domain

  streak_break_rate:
    definition: Breaks in last 24h / total active streaks
    target: <= 15% (domain-dependent baseline)
    alert_threshold: > 30% in any 1-hour window (indicates systemic event processing issue)

  freeze_token_utilization_rate:
    definition: Tokens applied / tokens acquired (rolling 30d)
    purpose: Understand freeze demand vs supply

  multiplier_active_rate:
    definition: Users with any active multiplier / total active users
    target: 30–50% (engagement signal)
    alert_threshold: < 10% (engagement risk) or > 80% (potential farming)

  streak_qualification_rejection_rate:
    definition: Events rejected by ML/rule engine / total qualifying events received
    target: <= 2%
    alert_threshold: > 5% (indicates input quality issue or model drift)

  abuse_flag_rate:
    definition: STREAK_ABUSE_FLAGs raised / total streak increment events
    target: < 0.5%
    alert_threshold: > 2% (potential coordinated abuse campaign)

  intelligence_signal_emission_rate:
    definition: EIE signals emitted / total milestone events
    purpose: Confirm PIE integration health

  p95_processing_latency_ms:
    definition: 95th percentile from event receipt to audit write
    target: <= 350ms
    alert_threshold: > 600ms

  streak_scheduler_execution_success_rate:
    definition: Successful window expiry evaluations / total scheduled evaluations
    target: >= 99.9%
    alert_threshold: < 99.0%
```

---

## 2️⃣0️⃣ VERSIONING POLICY

```yaml
VERSIONING_RULES:
  - All changes to this agent spec: Add-only, version-bumped, migration documented
  - Streak type taxonomy: May be ADDED, never deleted or renamed (old codes remain valid)
  - Multiplier table: Frozen — changes require governance review + version bump + tenant notification
  - Freeze token limits: Frozen — changes require version bump
  - State machine transitions: Frozen — new states require version bump + full regression test
  - Milestone reward taxonomy: May be extended. Existing milestones cannot be retroactively removed.
  - ML models: Semver tagged, immutable artifacts
  - AI prompts: Versioned alongside codebase
  - Kafka topics: v1 is frozen. New versions use new topic names. Old consumers not broken.
  - Intelligence signal mapping: May be extended. Existing mappings version-locked.
  - Backward compatibility window: 2 versions supported simultaneously
  - Migration: Documented in MIGRATION.md per version bump
  - Rollback: Supported to N-1 version without data loss
```

---

## 2️⃣1️⃣ NON-NEGOTIABLE RULES (SEALED)

```
THIS AGENT MUST NOT:
  ❌ Create streak types not in the registered taxonomy
  ❌ Apply freeze tokens to WIN STREAKS or COMPLIANCE STREAKS — these are hardlocked
  ❌ Break a streak while it is in FROZEN or HOLD state
  ❌ Retroactively modify historical streak counts
  ❌ Delete or update audit log entries
  ❌ Apply multipliers beyond the 3.0x global cap
  ❌ Emit reward events without confirmed streak state write + audit log write
  ❌ Process events from source agents not in the registered whitelist
  ❌ Allow cross-tenant streak data reads or writes
  ❌ Resolve abuse holds — PROBLEM_DETECTION_SCORING_AGENT has sole authority
  ❌ Send notifications directly — all notifications go through NOTIFICATION_DISPATCH_AGENT
  ❌ Modify freeze token registry records except status transitions (UNUSED→APPLIED, UNUSED→EXPIRED)
  ❌ Use AI layer to determine streak qualification, multiplier, or milestone decisions
  ❌ Infer timezone from user device — use tenant_registry timezone as authoritative source
  ❌ Apply streak increments during active HOLD — queue events, do not process
  ❌ Suppress CRITICAL escalations for any reason
  ❌ Allow a streak to remain in AT_RISK for longer than the qualifying window duration
  ❌ Award multiplier tokens for streaks in HOLD state
  ❌ Emit parent notifications for users who have not consented to parent visibility (check parent_visibility_consent flag)
```

---

## 2️⃣2️⃣ SCHOOL OPS OWNER OPERATING RULES (DOMAIN-SPECIFIC)

```yaml
SCHOOL_OPS_OWNER_CAPABILITIES:
  - View: Cohort-level streak aggregates (not individual student data without RBAC grant)
  - View: Institute-level streak performance dashboards
  - Action: Approve student leave (triggers SCHOOL-STREAK-001 auto-freeze)
  - Action: Gift freeze tokens to students within allocated token budget
  - Action: View TPO Activity Streak (SCHOOL-STREAK-003) status for their institute

SCHOOL_OPS_OWNER_RESTRICTIONS:
  - Cannot modify individual student streak counts
  - Cannot manually break or reset student streaks
  - Cannot view other institutes' streak data (tenant isolation)
  - Cannot override compliance streak breaks (SCHOOL-STREAK-006 is non-negotiable)

COHORT_STREAK_REPORTING:
  - Cohort engagement streak (SCHOOL-STREAK-002) generates weekly report
  - Report emitted to COHORT_ANALYTICS_AGENT for TPO dashboard
  - Break event triggers PROBLEM_DETECTION_SCORING_AGENT (PDSA-SCHOOL-001)
  - School Ops Owner receives in-app notification on cohort streak break

PARENT_VISIBILITY_RULES:
  - Parents receive notifications ONLY for milestone events (not break events)
  - Parent notifications require parent_visibility_consent = TRUE on student record
  - Parent notifications never include granular activity data — milestone badge name only
  - Parent access to streak dashboard is read-only and filtered (no raw event data)
```

---

## 2️⃣3️⃣ DOJO OWNER / MENTOR OPERATING RULES (DOMAIN-SPECIFIC)

```yaml
MENTOR_STREAK_INTEGRATION:
  - Mentor Evaluation Streak (DOJO-STREAK-004) requires evaluations to pass override audit
  - If evaluation is flagged for override abuse (PDSA-DOJO-003), that evaluation is disqualified retroactively from the streak count
  - Mentor streak hold applies immediately when PROBLEM_DETECTION_SCORING_AGENT raises PDSA-DOJO-003
  - Mentor re-certification (SECTION T7 from Dojo spec) resets evaluation streak to 0 if failed

WIN_STREAK_INTEGRITY_ENFORCEMENT:
  - Win streaks that include matches later flagged by anti-cheat (PDSA-DOJO-014) are retroactively audited
  - Confirmed fraudulent wins are removed from win streak count (retroactive correction)
  - Retroactive corrections write audit entries with correction_reason field
  - User is notified of correction via in-app message (not push — sensitive)
  - Milestone rewards already emitted from fraudulent streaks are flagged for REWARD_EMISSION_AGENT review
```

---

## 2️⃣4️⃣ GROWTH ENGINE OWNER OPERATING RULES (DOMAIN-SPECIFIC)

```yaml
GROWTH_ENGINE_CONFIGURATION:
  - Minimum XP threshold for GROWTH-STREAK-002 is configurable per tenant (default: 50 XP/day)
  - Win Streak Multiplier Streak threshold is configurable (default: Win Streak >= 5)
  - Freeze token pricing is set by BILLING_SERVICE — this agent reads only, does not set prices
  - Multiplier table values are hardcoded — not configurable by tenant (prevent gaming)

INNOVATION_STREAK_INTELLIGENCE_PIPELINE:
  - GROWTH-STREAK-006 is the primary growth-domain intelligence signal source
  - Every 7-day milestone triggers intelligence signal emission to EIE
  - Signal weight increases with streak length (longer streak = higher confidence signal)
  - This enables the EIE to distinguish between users with genuine innovation habits vs one-time participation

LEADERBOARD_INTEGRATION:
  - Active streak counts (especially Win Streak and Daily Login Streak) feed leaderboard rankings
  - RANK_UPDATE_TRIGGER emitted on milestone streak achievements
  - Streak leaderboard is a separate ranked view from skill/XP leaderboard
  - Streak data visible on public leaderboard is: streak_type, current_count, milestone_badge_earned (no user_id unless user opts in)
```

---

## 2️⃣5️⃣ FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════╗
║      STREAK_ENGINE_AGENT — GOVERNANCE SEAL                   ║
║      ECOSKILLER ANTIGRAVITY PLATFORM                         ║
╠══════════════════════════════════════════════════════════════╣
║ Execution Mode           : DETERMINISTIC                     ║
║ Mutation Policy          : ADD-ONLY VERSIONED                ║
║ Tenant Isolation         : HARD ENFORCED                     ║
║ Domain Isolation         : HARD ENFORCED                     ║
║ Creative Interpretation  : FORBIDDEN                         ║
║ Assumption Filling       : FORBIDDEN                         ║
║ Silent Failures          : FORBIDDEN                         ║
║ AI Autonomy              : ASSIST-ONLY — NO DECISION RIGHTS  ║
║ Freeze on WIN STREAK      : PERMANENTLY FORBIDDEN            ║
║ Freeze on COMPLIANCE      : PERMANENTLY FORBIDDEN            ║
║ Audit Trail              : MANDATORY — IMMUTABLE             ║
║ Multiplier Cap           : 3.0x HARDCODED — NOT CONFIGURABLE ║
║ Security Model           : ZERO-TRUST MULTI-TENANT           ║
║ Intelligence Layer       : EIE PIE COMPATIBLE                ║
║ Domains Covered          : DOJO · GROWTH · SCHOOL OPS        ║
║ State Machine            : LOCKED — 8 STATES, FIXED TRANSITIONS ║
║ Streak Taxonomy          : LOCKED — 17 TYPES REGISTERED      ║
║ Companion Agent          : PROBLEM_DETECTION_SCORING_AGENT   ║
╠══════════════════════════════════════════════════════════════╣
║ Interpretation Authority  : NONE                             ║
║ Architecture Authority    : LOCKED                           ║
║ Version                   : 1.0.0                            ║
╚══════════════════════════════════════════════════════════════╝
```

---

*This artifact is sealed. Any modification requires a version bump, governance review, migration documentation, and backward compatibility validation. No silent edits permitted. No assumption filling allowed. System behavior must exactly follow this specification. Companion dependency: PROBLEM_DETECTION_SCORING_AGENT v1.0.0 must be deployed before this agent can process abuse flags or hold states.*
