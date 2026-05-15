# 🔒 GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Version: v1.0.0
### Platform: Ecoskiller Antigravity

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 1 — AGENT IDENTITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
AGENT_NAME            = GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
SYSTEM_ROLE           = Global Temporal Normalization, Event Clock & DST
                        Arbitration Engine
PRIMARY_DOMAIN        = time_event_normalization
EXECUTION_MODE        = Deterministic + Validated + Real-Time + Scheduled
DATA_SCOPE            = Platform-wide — all tenant-scoped time-sensitive events,
                        schedules, triggers, streaks, sessions, notifications,
                        leaderboard resets, and cron-dependent operations
TENANT_SCOPE          = Strict Isolation — tenant timezone policies enforced
                        independently; no cross-tenant time policy leakage
FAILURE_POLICY        = HALT on ambiguous time state → LOG_INCIDENT → ESCALATE
CLOCK_AUTHORITY       = UTC canonical source — all local time derived, never stored
AGENT_VERSION         = v1.0.0
DEPLOYED_ON           = Ecoskiller Antigravity Platform
LANE_DEPENDENCY       = Lane A (Foundation) — event_schema_ready required
                        Lane D (Governance) — governance_ready required
                        Lane F (Intelligence) — ai_ready for ML optimization
```

**Core Law: All time stored in UTC. All time displayed in user-local.
All event triggers fire in UTC. All normalizations are deterministic.
Ambiguous time states (DST gap / overlap) are HALTED and resolved
before execution proceeds. No time-based decision may be made without
this agent's canonical timestamp.**

This agent must NEVER assume a timezone if none is declared.
This agent must NEVER store local time as canonical time.
This agent must NEVER silently resolve a DST ambiguity — all ambiguities are logged.
This agent must NEVER allow a streak, session, or event to fire at an
incorrect local boundary due to timezone misconfiguration.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 2 — PURPOSE DECLARATION
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 2.1 The Problem This Agent Solves

Ecoskiller Antigravity operates at 10M–100M users across every global timezone,
spanning Dojo match rooms, live teaching sessions, streak engines, tournament
brackets, leaderboard resets, daily digest triggers, XP award windows, royalty
period calculations, reminder notifications, and academic calendar events.

Without a single authoritative temporal normalization layer, the following
failures become inevitable at scale:

- Streak reset fires at wrong local midnight → user loses earned streak unfairly
- Tournament bracket closes before user's local deadline → unfair elimination
- Leaderboard weekly reset fires at inconsistent UTC offsets per region
- Live session notification delivered 3 hours late due to DST transition
- Daily cron evaluates habit completion before user's local day ends
- Royalty calculation period starts/ends at misaligned timezone boundaries
- XP award window expires before user in UTC+14 completes qualifying action
- Reminder scheduler batches notifications in wrong user-local time window

The GLOBAL_TIME_EVENT_NORMALIZATION_AGENT is the single canonical resolution
authority for all time-sensitive operations across the platform. It guarantees
that every event, trigger, deadline, window, and schedule fires at the correct
temporal moment for every user in every region — deterministically, auditably,
and at scale.

### 2.2 Inputs Consumed

- Raw event registration payloads (time-sensitive events from all platform agents)
- User timezone profile (IANA timezone string from user settings)
- Institution timezone profile (from INSTITUTION_VERIFICATION_AGENT)
- Tenant timezone policy (from TENANT_CONFIG_SERVICE)
- IANA timezone database (system-level, versioned)
- NTP clock reference (platform NTP server — not system clock)
- DST transition calendar (pre-computed, refreshed on IANA DB update)
- Academic calendar events (locale-specific, from ACADEMIC_CALENDAR_SERVICE)
- Cron schedule definitions (from all dependent agents and workers)
- Live session and tournament schedule payloads (from DOJO_SESSION_AGENT,
  TRAINER_REVENUE_AGENT, TOURNAMENT_ENGINE)
- Streak evaluation triggers (from HABIT_STREAK_AGENT)
- Notification dispatch schedules (from NOTIFICATION_DISPATCH_AGENT)
- Leaderboard reset schedules (from ANTIGRAVITY_LEADERBOARD_AGENT)
- Royalty period boundaries (from ROYALTY_ENGINE)

### 2.3 Outputs Produced

- Canonical UTC timestamp for every registered time-sensitive event
- User-local display timestamp (formatted per locale, never stored)
- DST-safe event fire time (pre-computed before DST window)
- Normalized cron schedule payload (UTC, tenant-aware)
- Time window validation result (VALID / INVALID / AMBIGUOUS)
- Streak boundary resolution record (per user, per timezone)
- Event deadline normalization record
- Notification optimal send window (per user timezone and engagement profile)
- Audit trace per every normalization execution
- DST transition alert events (pre-emitted 7 days before transition)

### 2.4 Downstream Agents That Depend On This Agent

| Agent | Time Dependency |
|---|---|
| HABIT_STREAK_AGENT | Midnight boundary per user timezone for streak evaluation |
| NOTIFICATION_DISPATCH_AGENT | User-local optimal send window |
| ANTIGRAVITY_LEADERBOARD_AGENT | Weekly/monthly reset canonical UTC timestamp |
| ROYALTY_ENGINE | Period start/end canonical UTC boundaries |
| TOURNAMENT_ENGINE (Dojo) | Match window open/close in user-local time |
| DOJO_SESSION_AGENT | Live session start/end in user-local time |
| TRAINER_REVENUE_AGENT | Payout period UTC boundary |
| SCHOOL_GROWTH_FORECAST_AGENT | Forecast period boundary alignment |
| DAILY_DIGEST_AGENT | Digest dispatch window per user locale |
| XP_LEDGER_AGENT | XP award window UTC boundary |
| REFERRAL_ENGINE | Referral expiry window normalization |
| ROYALTY_DISPUTE_RESOLUTION_AGENT | Appeal window UTC expiry |
| AUDIT_LOG_SERVICE | Canonical UTC timestamp authority |
| OBSERVABILITY_AGENT | All metrics timestamps normalized |
| CRON_SCHEDULER_SERVICE | All cron definitions normalized to UTC |

### 2.5 Upstream Agents That Feed This Agent

| Agent | Input Provided |
|---|---|
| TENANT_CONFIG_SERVICE | Tenant timezone policy |
| USER_PROFILE_SERVICE | User IANA timezone string |
| INSTITUTION_VERIFICATION_AGENT | Institution timezone |
| IANA_TIMEZONE_DB_SERVICE | IANA tz database, versioned |
| NTP_REFERENCE_SERVICE | Platform canonical clock |
| ACADEMIC_CALENDAR_SERVICE | Academic locale calendar |
| DOJO_SESSION_AGENT | Scheduled live sessions |
| TOURNAMENT_ENGINE | Tournament bracket schedules |
| HABIT_STREAK_AGENT | Streak evaluation trigger requests |
| NOTIFICATION_DISPATCH_AGENT | Notification schedule requests |

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 3 — TIME NORMALIZATION TAXONOMY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 3.1 Time Event Categories

```
CATEGORY A — BOUNDARY EVENTS (midnight-sensitive):
  streak_reset_boundary          → fires at user's local midnight in their IANA tz
  daily_digest_window            → opens at user's local morning (configurable: default 07:00)
  daily_habit_evaluation         → fires at user's local 23:59:00
  leaderboard_daily_reset        → fires at 00:00:00 UTC (platform canonical)
  leaderboard_weekly_reset       → fires Sunday 00:00:00 UTC
  leaderboard_monthly_reset      → fires 1st of month 00:00:00 UTC
  royalty_period_boundary        → fires at 00:00:00 UTC first day of billing period

CATEGORY B — SESSION & LIVE EVENTS (exact-time-sensitive):
  live_session_start             → exact UTC timestamp derived from host local time
  live_session_end               → exact UTC timestamp
  match_window_open              → UTC timestamp from tournament config
  match_window_close             → UTC timestamp with DST-safe buffer
  tournament_registration_deadline → UTC deadline derived from declared local deadline
  mentor_booking_slot            → UTC block derived from mentor IANA tz availability

CATEGORY C — WINDOW EVENTS (duration-sensitive):
  xp_award_window                → UTC open + close, per user region
  referral_expiry_window         → UTC deadline
  dispute_appeal_window          → 72h from verdict_issued_at_utc (pure UTC offset, no DST)
  content_submission_deadline    → UTC deadline derived from declared local deadline
  course_enrollment_window       → UTC open/close

CATEGORY D — NOTIFICATION WINDOWS (engagement-optimized):
  push_notification_send_window  → user-local optimal send time (ML-derived)
  email_digest_send_window       → user-local morning window
  reminder_dispatch_window       → user-local time per reminder_prefs
  sms_compliance_window          → user-local TCPA/DND compliant window

CATEGORY E — CRON & SCHEDULED JOBS:
  all platform cron definitions   → normalized to UTC before registration
  nightly_ml_retraining_trigger   → UTC scheduled
  weekly_forecast_refresh         → UTC scheduled
  daily_audit_integrity_check     → UTC scheduled
```

### 3.2 DST Handling Protocol

```
DST_TRANSITION_STATES:
  STANDARD        → No DST active. UTC offset is standard offset.
  DST_ACTIVE      → DST in effect. UTC offset is DST offset.
  PRE_TRANSITION  → Within 7-day DST transition window. Pre-compute post-DST times.
  GAP_AMBIGUITY   → Clock springs forward. Local times in gap (e.g. 02:00–03:00)
                    do not exist. Events targeting these times must be rescheduled.
  OVERLAP_AMBIGUITY → Clock falls back. Local times in overlap occur twice.
                    Events must declare FIRST or SECOND occurrence explicitly.

DST_RESOLUTION_RULES:
  GAP_AMBIGUITY:
    → DETECT: event_local_time falls within gap window
    → ACTION: reschedule to post-gap equivalent UTC time
    → LOG: DST_GAP_RESCHEDULE event to audit trail
    → NOTIFY: affected downstream agent of rescheduled UTC time

  OVERLAP_AMBIGUITY:
    → DETECT: event_local_time falls within overlap window
    → DEFAULT: use first occurrence (pre-fall-back)
    → OVERRIDE: if fold=SECOND declared in event payload, use second occurrence
    → LOG: DST_OVERLAP_RESOLVED event to audit trail

  PRE_TRANSITION:
    → 7 days before transition: emit DST_TRANSITION_ALERT to all affected agents
    → Pre-compute all scheduled event UTC timestamps post-transition
    → Validate no boundary events fall in gap
    → Update normalized cron schedules

IANA_DB_VERSION_POLICY:
  → IANA timezone database version tracked and stored per normalization
  → On IANA DB update: re-compute all affected future scheduled events
  → Version mismatch between stored normalization and current IANA DB:
     → FLAG affected events → recompute → re-emit → LOG
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 4 — INPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
INPUT_SCHEMA — TIME_EVENT_REGISTRATION: {
  "required_fields": [
    "event_normalization_id",
    "tenant_id",
    "requesting_agent",
    "event_category",
    "event_type",
    "reference_entity_id",
    "reference_entity_type",
    "declared_local_time",
    "declared_timezone",
    "normalization_purpose"
  ],
  "optional_fields": [
    "user_id",
    "institution_id",
    "recurrence_rule",
    "window_duration_seconds",
    "deadline_flag",
    "pre_compute_days_ahead",
    "dst_fold_preference",
    "locale_code",
    "notification_context",
    "force_recompute_flag"
  ],
  "validation_rules": [
    "event_normalization_id must be unique UUID — REJECT if duplicate",
    "tenant_id must match authenticated caller tenant — REJECT if mismatch",
    "requesting_agent must be registered in AGENT_REGISTRY — REJECT if unknown",
    "event_category must be ENUM['BOUNDARY','SESSION_LIVE','WINDOW',
       'NOTIFICATION','CRON'] — REJECT otherwise",
    "event_type must be a declared type within event_category — REJECT if undeclared",
    "declared_local_time must be ISO-8601 with no UTC offset (local time literal)",
    "declared_timezone must be valid IANA timezone string — REJECT if invalid",
    "declared_timezone must not be a fixed UTC offset like 'UTC+5' —
       MUST be named IANA zone like 'Asia/Kolkata' — REJECT fixed offsets",
    "normalization_purpose must be non-empty string",
    "recurrence_rule if present must be valid RFC-5545 RRULE string",
    "dst_fold_preference if present must be ENUM['FIRST','SECOND']",
    "locale_code if present must be valid BCP-47 locale string"
  ],
  "security_checks": [
    "Validate requesting_agent has time_event:register scope",
    "Validate tenant_id matches requesting agent's tenant context",
    "Reject anonymous or unregistered agent requests",
    "Rate limit: 10,000 normalization requests per agent per minute",
    "Validate reference_entity_id belongs to declared tenant"
  ],
  "domain_checks": [
    "Validate declared_timezone exists in current IANA DB version",
    "Validate declared_local_time is not in DST gap — HALT if gap detected",
    "Validate declared_local_time is not in DST overlap without fold declared",
    "Validate event_type is registered in EVENT_SCHEMA_REGISTRY"
  ]
}
```

**Rules — Non-Negotiable:**
- Named IANA timezone strings only — never accept raw UTC offsets like UTC+5:30
- No null timezone tolerance — every event must carry an explicit IANA zone
- DST gap times: HALT immediately, do not silently reschedule without log
- Fixed offsets (UTC+5, GMT+8): REJECT — they do not account for DST transitions
- No silent coercion of malformed timestamps — REJECT with structured error

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 5 — OUTPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
OUTPUT_SCHEMA — NORMALIZED_TIME_EVENT: {
  "result_object": {
    "event_normalization_id":       "UUID (echoed from input)",
    "tenant_id":                    "UUID",
    "requesting_agent":             "string",
    "event_category":               "ENUM string",
    "event_type":                   "string",
    "reference_entity_id":          "UUID",
    "reference_entity_type":        "string",
    "canonical_utc_timestamp":      "ISO-8601 UTC (authoritative fire time)",
    "canonical_utc_window_end":     "ISO-8601 UTC | null (if window event)",
    "user_display_timestamp":       "ISO-8601 with local offset (display only)",
    "user_display_timezone":        "IANA string",
    "user_display_formatted":       "locale-formatted string (e.g. 'Mon, 3 Mar 2026, 9:00 AM IST')",
    "dst_state_at_event":           "ENUM['STANDARD','DST_ACTIVE','PRE_TRANSITION']",
    "dst_transition_alert":         "boolean",
    "dst_gap_detected":             "boolean",
    "dst_overlap_detected":         "boolean",
    "dst_resolution_applied":       "ENUM['NONE','GAP_RESCHEDULED','OVERLAP_FIRST',
                                       'OVERLAP_SECOND'] | null",
    "iana_db_version":              "string (e.g. 2025b)",
    "recurrence_next_utc":          "ISO-8601 UTC | null (if recurrence defined)",
    "recurrence_sequence":          ["ISO-8601 UTC"] (next N occurrences, N configurable)",
    "normalization_confidence":     "ENUM['HIGH','MEDIUM','LOW']",
    "normalization_warnings":       ["string"],
    "anomaly_flags":                ["string"]
  },
  "confidence_score":               "float [0.0–1.0]",
  "model_version":                  "string (e.g. gtena-v1.0.0)",
  "audit_reference":                "UUID",
  "next_trigger_event": [
    "TIME_EVENT_NORMALIZED",
    "DST_TRANSITION_ALERT (conditional: pre_transition window)",
    "DST_GAP_RESCHEDULE_APPLIED (conditional)",
    "CRON_SCHEDULE_UPDATED (conditional: cron events)",
    "IANA_DB_VERSION_MISMATCH (conditional: version drift)"
  ]
}
```

**Law: canonical_utc_timestamp is the ONLY timestamp that may be used
to trigger platform events. user_display_timestamp is for UI rendering only
and must NEVER be stored as a trigger timestamp in any database.**

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 6 — ML / AI LOGIC LAYER
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 6.1 ML Layer (PRIMARY — 70% of execution weight)

```
MODEL_TYPE:
  Model A:   Regression — Optimal notification send time per user
             (predicts engagement-maximizing local time window)
  Model B:   Classification — DST ambiguity risk scoring
             (scores probability of DST-related event collision)
  Model C:   Time-Series — User activity window detection
             (identifies user's active hours per timezone for notification
             batching and streak evaluation prioritization)

FEATURES_USED:
  Notification optimization model (A):
    - user_timezone_iana
    - user_historical_open_rate_by_hour
    - user_historical_click_rate_by_hour
    - user_device_type
    - user_locale_code
    - notification_category
    - day_of_week
    - is_dst_active
    - platform_send_volume_by_hour (congestion avoidance)

  DST risk scoring model (B):
    - timezone_iana
    - days_until_dst_transition
    - event_type
    - event_local_hour
    - dst_gap_overlap_proximity_minutes
    - historical_dst_collision_rate_for_timezone

  Activity window model (C):
    - user_session_start_times_local (rolling 30d)
    - user_session_end_times_local
    - user_streak_activity_local_hour
    - user_content_interaction_local_hour

TRAINING_FREQUENCY:
  Notification optimization (A):  Weekly (user behavior shifts)
  DST risk scoring (B):            Monthly + on IANA DB update
  Activity window (C):             Bi-weekly

DRIFT_DETECTION:
  - Monitor notification open rate degradation > 10% from model baseline
  - Monitor DST collision rate increase vs predicted
  - Monitor activity window prediction accuracy < 0.75
  - Action: PAUSE model → FLAG to OBSERVABILITY_AGENT → revert version

VERSION_CONTROL:
  Format: gtena-[model-id]-v[MAJOR].[MINOR].[PATCH]
  All models immutably versioned — no in-place overwrites
  Rollback to prior version on drift event
```

### 6.2 AI Layer (ASSIST ONLY — 30% of execution weight)

```
AI_USAGE_SCOPE:
  - Generate human-readable DST transition alerts for admin notifications
  - Summarize timezone normalization anomalies for ops review
  - Generate locale-aware display format strings for edge-case locales
  - Assist in detecting patterns in DST-related event failures across regions
  - AI must NOT override ML-computed canonical_utc_timestamp
  - AI must NOT decide DST resolution policy autonomously
  - AI must NOT generate timezone conversion logic — only Python zoneinfo / IANA

PROMPT_GOVERNANCE:
  - All prompts versioned: gtena-prompt-v[VERSION]
  - Temperature: 0.1 (precision-critical — no creative variance)
  - Max tokens: 300 per DST alert message
  - Prompt inputs: structured normalization metadata only — no raw user data
  - All prompts logged with prompt_version and input_hash

AI MUST NOT:
  - Override the IANA database as source of truth
  - Use LLM-generated timezone rules (LLMs hallucinate timezone data)
  - Generate UTC offsets from memory — always derived from IANA DB
  - Access user PII for optimization context
```

**Law: IANA database is absolute timezone authority.
ML optimizes delivery windows. AI explains anomalies.
Neither overrides canonical UTC computation.**

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 7 — NORMALIZATION EXECUTION ENGINE
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 7.1 Core Normalization Pipeline

```
STEP 1 — CLOCK AUTHORITY CHECK
  → Fetch current time from NTP_REFERENCE_SERVICE (not system clock)
  → Validate NTP drift < 100ms — HALT if > 100ms until NTP sync confirmed
  → Record ntp_reference_utc for audit trail

STEP 2 — IANA DB VERSION CHECK
  → Load current IANA timezone database version
  → Compare against version used in prior normalizations for this entity
  → If version differs: FLAG IANA_DB_VERSION_MISMATCH → recompute all
    affected future events for this entity

STEP 3 — TIMEZONE VALIDATION
  → Validate declared_timezone against IANA DB
  → Reject fixed offsets (UTC+N, GMT±N)
  → Resolve timezone alias if applicable (e.g. "US/Eastern" → "America/New_York")
  → Store canonical IANA zone name

STEP 4 — DST STATE EVALUATION
  → Compute DST state for declared_timezone at declared_local_time
  → Determine UTC offset (standard or DST)
  → Check for gap: does declared_local_time fall in a DST spring-forward gap?
    → YES: HALT → log DST_GAP_DETECTED → apply GAP_RESCHEDULE rule → re-evaluate
  → Check for overlap: does declared_local_time fall in a DST fall-back overlap?
    → YES: apply dst_fold_preference (default FIRST) → log DST_OVERLAP_RESOLVED
  → Check pre-transition window (within 7 days of DST change):
    → YES: emit DST_TRANSITION_ALERT → pre-compute post-transition equivalent

STEP 5 — UTC CANONICALIZATION
  → Convert declared_local_time to canonical_utc_timestamp using
    Python 3.11 zoneinfo + IANA DB (deterministic, not offset arithmetic)
  → Store: canonical_utc_timestamp (ISO-8601 UTC, Z suffix mandatory)
  → Store: iana_db_version used
  → Store: dst_state_at_event

STEP 6 — RECURRENCE COMPUTATION (if recurrence_rule present)
  → Parse RFC-5545 RRULE
  → Compute next N occurrences in UTC accounting for future DST transitions
    in the declared_timezone (each occurrence independently evaluated for DST)
  → N default: 52 (one year of weekly events) — configurable
  → Store: recurrence_sequence as UTC timestamp array

STEP 7 — DISPLAY FORMAT GENERATION (for UI layer only)
  → Format user_display_timestamp using locale_code (BCP-47)
  → Apply locale-specific date/time formatting conventions
  → Flag as DISPLAY_ONLY — never persist this value as event trigger

STEP 8 — ML OPTIMIZATION (for NOTIFICATION category events)
  → Invoke notification optimization model (Model A)
  → Compute optimal_send_utc_window: {open: UTC, close: UTC}
  → If ML model unavailable: use rule-based fallback (user local 09:00–21:00)

STEP 9 — OUTPUT ASSEMBLY AND AUDIT
  → Assemble OUTPUT_SCHEMA payload
  → Compute output_hash
  → Write audit log entry (immutable)
  → Emit TIME_EVENT_NORMALIZED event to Redis Streams
  → Return normalized payload to requesting agent
```

### 7.2 Streak Boundary Normalization (Critical Path)

```
STREAK_BOUNDARY_NORMALIZATION (Category A — highest priority):

  Problem: "Today" for a streak is user's local calendar day, not UTC calendar day.
  A user in UTC+14 (Line Islands) has a different "today" than a user in UTC-12.
  The platform streak evaluation cron runs at UTC midnight — incorrect without
  per-user midnight normalization.

  RESOLUTION PROTOCOL:
    For each user with an active streak:
      → Fetch user IANA timezone
      → Compute user's local midnight in UTC: user_local_midnight_utc
      → Register streak evaluation trigger at: user_local_midnight_utc
      → This creates a per-user, timezone-aware streak evaluation queue

  SCALE DESIGN:
    At 10M active users spanning all timezones:
      → 10M individual streak evaluation UTC timestamps computed and queued
      → Grouped by UTC minute: users sharing the same local midnight are batched
      → Batch size: up to 50,000 per UTC minute (spread across 1440 minutes/day)
      → Redis Sorted Set used: score = canonical_utc_epoch_seconds

  STREAK_EVALUATION_QUEUE_SCHEMA:
    {
      "user_id":                  "UUID",
      "tenant_id":                "UUID",
      "user_timezone_iana":       "string",
      "streak_eval_utc":          "epoch_seconds (canonical UTC midnight for user)",
      "iana_db_version":          "string",
      "dst_state":                "ENUM string",
      "audit_reference":          "UUID"
    }

  LAW: No streak may be evaluated before user's local midnight UTC equivalent.
  LAW: No streak may be evaluated more than 120 seconds after user's local midnight UTC.
  SLA BREACH on streak evaluation > 120s late: LOG_INCIDENT → ALERT OPS.
```

### 7.3 Tournament & Session Time Normalization

```
TOURNAMENT_TIME_NORMALIZATION:
  Input:  tournament host's declared local deadline + IANA timezone
  Output: canonical_utc_deadline + display timestamps for all participant locales

  For each registered participant:
    → Compute participant's local equivalent of the UTC deadline
    → Generate locale-formatted display string per participant locale
    → Emit PARTICIPANT_TIME_DISPLAY_READY event

  DST SAFETY BUFFER:
    → If tournament deadline falls within 24h of a DST transition
      in any participant's timezone:
    → Add 5-minute buffer to canonical_utc_deadline
    → Log: TOURNAMENT_DST_BUFFER_APPLIED
    → Notify TOURNAMENT_ENGINE of adjusted deadline

LIVE_SESSION_NORMALIZATION:
  Input:  trainer/mentor declared local start time + IANA timezone
  Output: canonical_utc_start, canonical_utc_end, join_window_utc_open

  join_window_utc_open = canonical_utc_start - 10 minutes (configurable)
  Early join lobby opens at join_window_utc_open

  Participant display: computed per each participant's IANA timezone
  Notification trigger: canonical_utc_start - notification_lead_time_minutes
  (notification_lead_time_minutes default: 15 — configurable per tenant)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 8 — SCALABILITY DESIGN
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
EXPECTED_RPS            = 5,000 peak (normalization requests from all agents)
LATENCY_TARGET          = p95 < 50ms (single event normalization — synchronous)
                          p95 < 200ms (recurrence sequence computation)
                          p95 < 500ms (streak boundary queue rebuild)
MAX_CONCURRENCY         = 10,000 simultaneous normalization operations
QUEUE_STRATEGY          = Redis Sorted Set for time-ordered event queues
                          Redis Streams for event emission
                          Priority: STREAK_BOUNDARY > SESSION_LIVE > BOUNDARY >
                                    NOTIFICATION > CRON > WINDOW

HORIZONTAL_SCALING:
  - Stateless execution — no in-process timezone state
  - IANA DB loaded into shared Redis cache on startup (read-only)
  - Kubernetes HPA: scale on CPU > 55% OR queue depth > 2,000
  - Min replicas: 3 | Max replicas: 30
  - Pod anti-affinity enforced across availability zones

CACHING STRATEGY:
  - IANA timezone data: cached in Redis (invalidated on IANA DB version update)
  - DST transition calendar: pre-computed 12 months ahead, cached in Redis
  - User timezone profiles: cached with 15-minute TTL (invalidated on user update)
  - Streak boundary queue: pre-computed nightly for next 24h, stored in Redis Sorted Set
  - NTP reference: re-fetched every 30 seconds — never cached longer

STREAK BOUNDARY SCALE:
  At 10M users:   10M UTC midnight timestamps computed daily
                  Batch processor: 30-minute pre-computation window before UTC midnight
                  Groups by timezone: ~600 distinct IANA zones active on platform
                  Per-zone batch: avg 16,700 users per zone
  At 100M users:  100M timestamps — horizontal worker scaling to 100 pods

DATA RETENTION:
  Normalization records:  30 days hot (most recent normalized timestamps)
  DST transition logs:    2 years (regulatory and audit)
  Streak boundary queue:  48 hours rolling (current + next 24h)
  Recurrence sequences:   12 months pre-computed, rolling refresh
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 9 — SECURITY ENFORCEMENT
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
SECURITY CHECKLIST (ALL MANDATORY — absence = STOP EXECUTION):

  ✅ Only registered platform agents may submit normalization requests
     (JWT scope: time_event:register validated via Keycloak)
  ✅ Tenant isolation: all normalization records tagged with tenant_id
  ✅ No cross-tenant timezone policy leakage
  ✅ Rate limiting: 10,000 requests per agent per minute via Kong Gateway
  ✅ NTP reference: platform-controlled NTP server only — no system clock drift
  ✅ IANA DB source: official IANA tz database only — no third-party timezone APIs
  ✅ All normalization outputs signed before dispatch to downstream agents
  ✅ Audit logging: every normalization execution, immutable append-only
  ✅ DST manipulation attack detection: flag if declared_timezone is submitted
     with known-incorrect IANA string that would shift event timing
  ✅ Timezone spoofing detection: if user_timezone declared in request differs
     from user's registered profile by > 2 hours, FLAG anomaly and require
     confirmation from USER_PROFILE_SERVICE before processing
  ✅ Encryption at rest: AES-256 on all stored normalization records
  ✅ Encryption in transit: TLS 1.3 minimum
  ✅ No local time ever stored as canonical time — enforced at ORM layer
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 10 — AUDIT & TRACEABILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Every normalization execution emits an immutable audit record:

```json
AUDIT_LOG_SCHEMA: {
  "audit_reference":            "UUID (primary key)",
  "timestamp_utc":              "ISO-8601 (from NTP_REFERENCE_SERVICE)",
  "ntp_reference_utc":          "ISO-8601 (raw NTP reading)",
  "ntp_drift_ms":               "integer",
  "tenant_id":                  "UUID",
  "requesting_agent":           "string",
  "event_normalization_id":     "UUID",
  "event_category":             "string",
  "event_type":                 "string",
  "reference_entity_id":        "UUID",
  "declared_local_time":        "ISO-8601 local literal",
  "declared_timezone":          "IANA string",
  "canonical_utc_output":       "ISO-8601 UTC",
  "iana_db_version":            "string",
  "dst_state":                  "ENUM string",
  "dst_resolution_applied":     "ENUM string | null",
  "dst_gap_detected":           "boolean",
  "dst_overlap_detected":       "boolean",
  "model_version":              "string",
  "normalization_confidence":   "ENUM string",
  "input_hash":                 "SHA-256",
  "output_hash":                "SHA-256",
  "prior_audit_hash":           "SHA-256 (hash chain)",
  "anomaly_flags":              ["string"],
  "execution_time_ms":          "integer"
}
```

**Immutability Law:**
- Append-only PostgreSQL partition — no UPDATE or DELETE
- Hash chain: every audit entry includes SHA-256 of prior entry
- Replicated to cold storage within 5 minutes
- IANA DB version stored per record — enables forensic reconstruction
  of any past normalization decision

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 11 — FAILURE POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
FAILURE_SCENARIOS AND RESPONSE:

┌──────────────────────────────────────────┬──────────────────────────────────────────────────┐
│ Failure Type                             │ Response                                         │
├──────────────────────────────────────────┼──────────────────────────────────────────────────┤
│ Invalid IANA timezone string             │ STOP → REJECT structured error → LOG             │
│ Fixed UTC offset submitted (UTC+5)       │ STOP → REJECT → LOG → ALERT requesting agent     │
│ NTP drift > 100ms                        │ HALT all normalizations → SYNC NTP → RESUME      │
│ NTP_REFERENCE_SERVICE unreachable        │ HALT → ESCALATE OPS → no normalizations issued   │
│ IANA DB unreachable / corrupted          │ HALT → serve from verified cache → FLAG OPS      │
│ DST gap detected in event time           │ HALT → apply GAP_RESCHEDULE → LOG → re-emit      │
│ DST overlap without fold declaration     │ Apply FIRST occurrence default → LOG warning     │
│ IANA DB version mismatch                 │ Recompute affected events → LOG → notify agents  │
│ Timezone spoofing detected               │ FLAG anomaly → require profile confirmation      │
│ Streak evaluation > 120s late            │ LOG_INCIDENT → ALERT OPS → force-evaluate now   │
│ Redis Sorted Set unavailable             │ FALLBACK to PostgreSQL queue → ALERT OPS         │
│ ML model unavailable (Model A)           │ FALLBACK to rule-based window → FLAG ai_down     │
│ Recurrence computation overflow (>1000)  │ CAP at 1000 → LOG_WARNING → return partial       │
│ Cross-tenant timezone policy collision   │ STOP → SECURITY_INCIDENT → ALERT                │
└──────────────────────────────────────────┴──────────────────────────────────────────────────┘

ESCALATION_TARGETS:
  OPS_TEAM:             ops@ecoskiller.internal
  ML_OPS_TEAM:          ml-ops@ecoskiller.internal
  OBSERVABILITY_AGENT:  emit AGENT_FAILURE_EVENT

RETRY_POLICY:
  Transient (Redis timeout, DB timeout):
    Attempt 1: immediate | 2: 1s | 3: 4s | 4: 16s
    Max retries: 4 → STOP → LOG_INCIDENT → ALERT
  NTP failure: no retry — HALT until NTP confirmed
  No silent failures under any condition
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 12 — INTER-AGENT DEPENDENCY MAP
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
UPSTREAM_AGENTS / SERVICES:
  - NTP_REFERENCE_SERVICE            → platform canonical clock
  - IANA_TIMEZONE_DB_SERVICE         → timezone rules, DST transitions
  - USER_PROFILE_SERVICE             → user IANA timezone
  - TENANT_CONFIG_SERVICE            → tenant timezone policy
  - INSTITUTION_VERIFICATION_AGENT   → institution timezone
  - ACADEMIC_CALENDAR_SERVICE        → academic locale calendar
  - DOJO_SESSION_AGENT               → live session schedules
  - TOURNAMENT_ENGINE                → tournament bracket schedules
  - HABIT_STREAK_AGENT               → streak evaluation triggers
  - NOTIFICATION_DISPATCH_AGENT      → notification schedule requests

DOWNSTREAM_AGENTS (consumers of canonical UTC timestamps):
  - HABIT_STREAK_AGENT               → streak midnight boundary per user
  - NOTIFICATION_DISPATCH_AGENT      → optimal send UTC window
  - ANTIGRAVITY_LEADERBOARD_AGENT    → reset canonical UTC
  - ROYALTY_ENGINE                   → period boundary UTC
  - TOURNAMENT_ENGINE                → match window UTC
  - DOJO_SESSION_AGENT               → session start/end UTC
  - TRAINER_REVENUE_AGENT            → payout period UTC
  - SCHOOL_GROWTH_FORECAST_AGENT     → forecast period alignment
  - DAILY_DIGEST_AGENT               → digest dispatch UTC window
  - XP_LEDGER_AGENT                  → award window UTC
  - REFERRAL_ENGINE                  → referral expiry UTC
  - ROYALTY_DISPUTE_RESOLUTION_AGENT → appeal window UTC expiry
  - AUDIT_LOG_SERVICE                → canonical timestamp authority
  - CRON_SCHEDULER_SERVICE           → all cron definitions normalized
  - OBSERVABILITY_AGENT              → all metrics timestamps

EVENT_TRIGGERS (emitted via Redis Streams):
  - TIME_EVENT_NORMALIZED
  - DST_TRANSITION_ALERT             (7 days pre-transition, per timezone)
  - DST_GAP_RESCHEDULE_APPLIED       (conditional)
  - DST_OVERLAP_RESOLVED             (conditional)
  - IANA_DB_VERSION_MISMATCH         (conditional)
  - IANA_DB_UPDATED                  (on database refresh)
  - STREAK_BOUNDARY_QUEUE_BUILT      (daily pre-computation complete)
  - NTP_DRIFT_ALERT                  (conditional: drift > 100ms)
  - CRON_SCHEDULE_UPDATED            (on recalibration)
  - AGENT_FAILURE_EVENT              (conditional)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 13 — PASSIVE INTELLIGENCE COMPATIBILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent emits timezone-behavioral feature vectors to FEATURE_STORE_AGENT:

```json
EMIT_FEATURE_VECTOR (per user, per ML model refresh): {
  "entity_type":        "user",
  "entity_id":          "user_id (UUID)",
  "tenant_id":          "UUID",
  "feature_name":       "optimal_notification_local_hour",
  "feature_value":      "float (e.g. 9.5 = 09:30 local)",
  "timestamp_utc":      "ISO-8601",
  "source_agent":       "GLOBAL_TIME_EVENT_NORMALIZATION_AGENT",
  "model_version":      "string"
}
```

Additional vectors emitted:

```
user_active_window_start_local_hour   → float
user_active_window_end_local_hour     → float
user_timezone_iana                    → string (canonical)
user_dst_transition_risk_next_30d     → float [0.0–1.0]
user_streak_eval_utc_epoch            → integer (daily)
```

These feed into NOTIFICATION_DISPATCH_AGENT for intelligent delivery
timing and HABIT_STREAK_AGENT for per-user streak boundary precision.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 14 — GROWTH ENGINE & ECONOMICS HOOK
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 14.1 Growth Mechanic Dependency

Every Ecoskiller Antigravity growth mechanic that depends on time
requires this agent's canonical UTC timestamp:

```
STREAK ENGINE (R57 / R95):
  → Incorrect streak reset = user trust loss = churn
  → Per-user timezone-correct midnight = zero unfair streak breaks
  → At 100M users: streaks are the #1 daily return driver
  → Time normalization accuracy directly impacts DAU

TOURNAMENT ENGINE (Dojo):
  → Unfair deadline = community complaint escalation
  → DST-safe tournament windows = fair global participation
  → Cross-timezone tournaments require per-participant local deadline display

NOTIFICATION ENGINE (R66):
  → Optimal send window prediction increases open rates by ~30%
  → Wrong-timezone notifications = unsubscribes = engagement decay
  → Time normalization is a direct economic lever on platform DAU

LEADERBOARD RESET:
  → Consistent weekly/monthly resets at canonical UTC = fair competition
  → Any drift in reset time creates exploitation vectors (late XP farming)
```

### 14.2 Economic Compatibility

```
ROYALTY_ENGINE period boundaries:
  → All royalty calculation periods start and end at canonical UTC midnight
    of the first and last day of the billing period
  → This agent provides: royalty_period_start_utc, royalty_period_end_utc
  → Incorrect period boundaries = incorrect royalty payments = dispute trigger

PAYOUT PERIOD NORMALIZATION:
  → Trainer payout periods (from TRAINER_REVENUE_AGENT) normalized to UTC
  → Ensures payout calculation always uses complete billing period data

REFERRAL EXPIRY:
  → Referral links expire at canonical_utc_deadline derived from
    declared_local_deadline + declarer's IANA timezone
  → DST-safe: expiry never falls in a DST gap

ECONOMICS SIGNAL emitted to ROYALTY_ENGINE:
  {
    "period_type":              "ENUM['monthly','weekly','custom']",
    "period_start_utc":         "ISO-8601 UTC",
    "period_end_utc":           "ISO-8601 UTC",
    "iana_db_version":          "string",
    "dst_transitions_in_period": "integer",
    "source_agent":             "GLOBAL_TIME_EVENT_NORMALIZATION_AGENT"
  }
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 15 — SCALE, COMPATIBILITY & GLOBAL REACH
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 15.1 Global Timezone Coverage

```
IANA TIMEZONE DATABASE COVERAGE:
  Total IANA zones: ~600 named zones (2025b database)
  Active zones at 10M users: ~200 distinct zones
  Active zones at 100M users: ~600 zones (near-full coverage)

DST-OBSERVING ZONES: ~140 zones observe DST
  → Annual DST transitions: ~280 events (spring + fall per zone)
  → This agent pre-computes all 280 transitions 12 months ahead

NON-DST ZONES: ~460 zones (e.g. Asia/Kolkata, Asia/Shanghai, Africa/*)
  → Standard UTC offset: constant, never changes
  → Still require IANA validation (some non-DST zones have had historical
    offset changes — IANA DB tracks these)

HALF-HOUR AND QUARTER-HOUR OFFSET ZONES:
  → Asia/Kolkata (UTC+5:30), Asia/Kathmandu (UTC+5:45),
    Australia/Lord_Howe (UTC+10:30/+11), Pacific/Chatham (UTC+12:45/+13:45)
  → All handled natively by IANA DB + Python zoneinfo
  → Never use float arithmetic for these — always IANA DB resolution

EXTREME OFFSET ZONES:
  → Pacific/Kiritimati (UTC+14): easternmost timezone
  → Pacific/Samoa (UTC-11): westernmost standard
  → Users here may be on calendar day N+1 or N-1 vs UTC
  → Streak boundary: these users have the most divergent local midnight UTC
  → Pre-computed and queued correctly by streak boundary normalization
```

### 15.2 Multi-Region Deployment Compatibility

```
DEPLOYMENT REGIONS:
  Region 1: Asia Pacific (primary: India, Southeast Asia, East Asia)
  Region 2: Europe / Middle East / Africa
  Region 3: Americas (North + South)

TIME NORMALIZATION AGENT DEPLOYMENT:
  → Deployed in all 3 regions
  → NTP reference: region-local NTP servers with cross-region sync validation
  → IANA DB: replicated to all regions — version synchronized every 6 hours
  → Redis Sorted Set: per-region streak boundary queues
  → Event emission: region-local Redis Streams with global event bus bridge

DATA RESIDENCY:
  → Normalization records tagged with user's data residency region
  → Streak boundary queue partitioned by tenant + region
  → Audit logs: stored in user's data residency region
  → Cross-region normalization requests: allowed for tournament/session events
    (host may be in different region than participants)
```

### 15.3 Compatibility Matrix

```
COMPATIBLE WITH:
  ✅ All time-sensitive Ecoskiller platform agents (see Section 12)
  ✅ Dojo For Arts — match rooms, tournaments, live sessions
  ✅ Antigravity Leaderboard (weekly/monthly reset normalization)
  ✅ Royalty & Billing Engine (period boundary normalization)
  ✅ Innovation Economy (submission deadline normalization)
  ✅ Notification Stack (optimal send window)
  ✅ Habit & Streak Engine (per-user midnight boundary)
  ✅ Academic Calendar (locale-aware term/exam period events)
  ✅ CQRS Event Store (canonical UTC timestamp authority)
  ✅ Redis Streams (event emission)
  ✅ Redis Sorted Set (streak boundary priority queue)
  ✅ PostgreSQL (normalization records + audit logs)
  ✅ Kubernetes autoscaling (stateless pods)
  ✅ Python 3.11 zoneinfo (IANA DB integration)
  ✅ RFC-5545 RRULE recurrence parsing
  ✅ Kong API Gateway (rate limiting + auth)
  ✅ Keycloak RBAC (JWT scope: time_event:register)
  ✅ CI/CD contract-gate system (Lane A dependency: event_schema_ready)
  ✅ Multi-region deployment (3 regions, IANA DB sync across regions)

NOT COMPATIBLE WITH (by design):
  ❌ Fixed UTC offset strings (UTC+5, GMT±N) — always rejected
  ❌ System clock as time authority — NTP reference only
  ❌ LLM-generated timezone rules — IANA DB only
  ❌ Local time storage as canonical — UTC only stored
  ❌ Third-party timezone APIs — IANA DB local replica only
  ❌ Cross-tenant streak queue access
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 16 — PERFORMANCE MONITORING
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
METRICS (all exposed to OBSERVABILITY_AGENT):

  normalization_request_rate:        requests per second (target headroom < 60% of RPS)
  normalization_success_rate:        % completing without error (target > 99.9%)
  normalization_p50_latency_ms:      target < 20ms
  normalization_p95_latency_ms:      target < 50ms
  normalization_p99_latency_ms:      target < 150ms
  dst_gap_events_detected:           rolling 30d count per timezone
  dst_overlap_events_detected:       rolling 30d count
  dst_reschedule_rate:               % of events requiring DST rescheduling
  iana_db_version_mismatch_count:    rolling 7d count
  ntp_drift_ms:                      current NTP drift (alert > 50ms, HALT > 100ms)
  streak_boundary_queue_build_time_s: daily pre-computation duration (alert > 300s)
  streak_eval_latency_p99_s:         target < 120s per SLA
  streak_eval_sla_breach_count:      rolling 24h
  notification_open_rate_by_timezone: model effectiveness proxy
  timezone_spoof_detection_count:    rolling 24h (security metric)
  recurrence_computation_p95_ms:     target < 200ms
  ml_model_drift_indicator:          0 = stable | 1 = drift

DASHBOARD:
  Grafana panel: GLOBAL_TIME_EVENT_NORMALIZATION_AGENT
  World-map visualization: active timezone zones, DST transition heat map
  Alerts: PagerDuty → OPS_TEAM, ML_OPS_TEAM
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 17 — VERSIONING POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
VERSIONING LAW:
  All changes: Add-only, versioned via semver, backward compatible,
               migration documented, rollback safe

  MAJOR version bump required for:
    - New event category added to taxonomy
    - Input schema breaking change
    - DST resolution protocol change
    - NTP authority provider change
    - IANA DB integration library change (e.g. switching zoneinfo backend)

  MINOR version bump required for:
    - New event type within existing category
    - New optional input/output field
    - New recurrence rule support
    - New ML model for notification optimization
    - New region deployment

  PATCH version bump required for:
    - Bug fixes in DST computation
    - Performance improvements in streak queue
    - IANA DB version update (automatic, no manual bump needed)
    - Prompt updates (minor)

  IANA DB updates are NOT version bumps — they are database refreshes.
  IANA DB updates must trigger: re-computation of all future events,
  emit IANA_DB_UPDATED event, log version change to audit trail.

  NO local time storage policy may be weakened under any version bump.
  NO NTP authority may be changed to system clock under any version bump.
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 18 — DATABASE SCHEMA (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Normalization records (immutable after write)
CREATE TABLE time_event_normalizations (
  id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_normalization_id      UUID NOT NULL UNIQUE,
  tenant_id                   UUID NOT NULL,
  requesting_agent            VARCHAR(100) NOT NULL,
  event_category              VARCHAR(20) NOT NULL,
  event_type                  VARCHAR(60) NOT NULL,
  reference_entity_id         UUID NOT NULL,
  reference_entity_type       VARCHAR(50) NOT NULL,
  declared_local_time         TEXT NOT NULL,               -- ISO-8601 local literal (stored for audit)
  declared_timezone           VARCHAR(60) NOT NULL,         -- IANA string
  canonical_utc_timestamp     TIMESTAMPTZ NOT NULL,         -- THE canonical trigger time
  canonical_utc_window_end    TIMESTAMPTZ,
  user_id                     UUID,
  institution_id              UUID,
  iana_db_version             VARCHAR(10) NOT NULL,
  dst_state                   VARCHAR(20) NOT NULL,
  dst_gap_detected            BOOLEAN NOT NULL DEFAULT FALSE,
  dst_overlap_detected        BOOLEAN NOT NULL DEFAULT FALSE,
  dst_resolution_applied      VARCHAR(30),
  normalization_confidence    VARCHAR(10) NOT NULL,
  ml_model_version            VARCHAR(50),
  input_hash                  CHAR(64) NOT NULL,
  output_hash                 CHAR(64) NOT NULL,
  audit_reference             UUID NOT NULL,
  anomaly_flags               TEXT[] DEFAULT '{}',
  created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- CONSTRAINT: canonical_utc_timestamp must have UTC timezone
ALTER TABLE time_event_normalizations
  ADD CONSTRAINT canonical_utc_check
  CHECK (EXTRACT(TIMEZONE FROM canonical_utc_timestamp) = 0);

-- Immutability enforcement
CREATE RULE no_update_normalizations
  AS ON UPDATE TO time_event_normalizations DO INSTEAD NOTHING;
CREATE RULE no_delete_normalizations
  AS ON DELETE TO time_event_normalizations DO INSTEAD NOTHING;

-- Streak boundary queue (rolling 48h, Redis Sorted Set is primary)
CREATE TABLE streak_boundary_queue (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id               UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  user_timezone_iana    VARCHAR(60) NOT NULL,
  streak_eval_utc       TIMESTAMPTZ NOT NULL,
  iana_db_version       VARCHAR(10) NOT NULL,
  dst_state             VARCHAR(20) NOT NULL,
  audit_reference       UUID NOT NULL,
  processed_at          TIMESTAMPTZ,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_sbq_eval_utc ON streak_boundary_queue (streak_eval_utc ASC)
  WHERE processed_at IS NULL;
CREATE INDEX idx_sbq_user ON streak_boundary_queue (user_id, tenant_id);

-- DST transition calendar (pre-computed, 12 months ahead)
CREATE TABLE dst_transition_calendar (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timezone_iana       VARCHAR(60) NOT NULL,
  transition_type     VARCHAR(10) NOT NULL,  -- 'SPRING_FORWARD' or 'FALL_BACK'
  transition_utc      TIMESTAMPTZ NOT NULL,
  gap_start_local     TEXT,  -- local time gap start (spring forward)
  gap_end_local       TEXT,  -- local time gap end
  overlap_start_local TEXT,  -- local time overlap start (fall back)
  overlap_end_local   TEXT,
  offset_before       INTEGER NOT NULL,  -- UTC offset in minutes before transition
  offset_after        INTEGER NOT NULL,  -- UTC offset in minutes after transition
  iana_db_version     VARCHAR(10) NOT NULL,
  computed_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_dtc_zone_utc ON dst_transition_calendar (timezone_iana, transition_utc ASC);
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 19 — API CONTRACT (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 reference (excerpt) — internal agent-to-agent API

/v1/time/normalize:
  post:
    summary: Normalize a time event to canonical UTC
    description: |
      Accepts a declared local time + IANA timezone.
      Returns canonical UTC timestamp, DST state, display format.
      Used by all platform agents requiring authoritative UTC timestamps.
    security:
      - BearerAuth: [time_event:register]
    requestBody:
      required: true
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/TimeEventRegistration'
    responses:
      200:
        description: Normalized canonical UTC timestamp
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/NormalizedTimeEvent'
      400:
        description: |
          Validation failure — structured error.
          Includes: INVALID_IANA_ZONE | FIXED_OFFSET_REJECTED |
                    DST_GAP_DETECTED | NULL_TIMEZONE
      429:
        description: Rate limit exceeded

/v1/time/normalize/batch:
  post:
    summary: Batch normalize multiple time events (max 500 per request)
    security:
      - BearerAuth: [time_event:register]
    responses:
      202: Accepted (async — results via event stream)
      400: Validation failure on batch payload

/v1/time/dst-calendar/{timezone_iana}:
  get:
    summary: Get pre-computed DST transition calendar for a timezone
    security:
      - BearerAuth: [time_event:read]
    parameters:
      - name: timezone_iana
        in: path
        required: true
      - name: months_ahead
        in: query
        schema:
          type: integer
          default: 12
          maximum: 24
    responses:
      200: DST transition schedule
      404: Timezone not found in IANA DB

/v1/time/streak-queue/status:
  get:
    summary: Get streak boundary queue build status (ops / monitoring)
    security:
      - BearerAuth: [platform_admin]
    responses:
      200: Queue build status, coverage, last build time
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 20 — NON-NEGOTIABLE RULES (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent MUST NOT under any condition:

```
❌ Accept fixed UTC offset strings (UTC+5, GMT+5:30, +0530) as timezone input
   — ONLY named IANA timezone strings accepted (e.g. Asia/Kolkata)
❌ Use the system clock as time authority — NTP_REFERENCE_SERVICE only
❌ Store local time as canonical time — UTC only stored in all databases
❌ Silently resolve a DST gap event — every gap must be logged and rescheduled
❌ Allow any downstream agent to fire an event using a non-canonical timestamp
❌ Use LLM-generated or memory-derived timezone offset values
❌ Accept timezone data from third-party APIs — IANA DB local replica only
❌ Allow NTP drift > 100ms to persist — HALT all normalizations until resolved
❌ Mix timezone policies across tenant boundaries
❌ Allow a streak evaluation to fire more than 120 seconds after user's local midnight
❌ Generate user_display_timestamp values that are stored as event triggers
❌ Modify historical normalization records — append-only
❌ Delete audit logs or DST transition calendar records
❌ Operate without confirmed IANA DB version loaded
❌ Process normalization requests from unregistered agents
❌ Produce partial outputs — HALT or COMPLETE, never partial
❌ Override IANA DB with any manually declared UTC offset rule
❌ Allow recurrence computation to proceed without per-occurrence DST validation
```

Violation of any rule above → STOP EXECUTION → LOG_INCIDENT → ESCALATE

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 21 — EXECUTION CHECKLIST (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
PRE-EXECUTION GATE (every normalization request):
  ✅ NTP_REFERENCE_SERVICE reachable and drift < 100ms
  ✅ IANA DB loaded and version confirmed
  ✅ requesting_agent registered in AGENT_REGISTRY
  ✅ JWT scope: time_event:register validated
  ✅ tenant_id matches requesting agent context
  ✅ declared_timezone is valid IANA string (not fixed offset)
  ✅ declared_local_time is valid ISO-8601 local literal
  ✅ DST gap check complete for declared_local_time + declared_timezone
  ✅ event_normalization_id unique (no duplicate)
  ✅ input_hash computed and logged

ON NORMALIZATION COMPLETE:
  ✅ canonical_utc_timestamp carries Z suffix (UTC confirmed)
  ✅ output_hash computed and logged
  ✅ Audit log entry written (immutable, hash chain updated)
  ✅ TIME_EVENT_NORMALIZED event emitted to Redis Streams
  ✅ DST_TRANSITION_ALERT emitted if pre-transition window active
  ✅ Feature vectors emitted to FEATURE_STORE_AGENT (for notification events)
  ✅ Canonical timestamp returned to requesting agent
  ✅ local display timestamp flagged as DISPLAY_ONLY

DAILY STREAK BOUNDARY QUEUE BUILD (nightly — 30 min before UTC midnight):
  ✅ All active users with streaks fetched
  ✅ Per-user local midnight UTC computed via IANA DB
  ✅ Redis Sorted Set populated with user_id + streak_eval_utc_epoch
  ✅ Build duration logged to OBSERVABILITY_AGENT
  ✅ DST transitions in next 24h checked — affected users recomputed
  ✅ STREAK_BOUNDARY_QUEUE_BUILT event emitted

ANY GATE FAILURE → HALT → LOG_INCIDENT → ESCALATE
```

---

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DOCUMENT STATUS:      SEALED
AGENT VERSION:        v1.0.0
PLATFORM:             Ecoskiller Antigravity
AUTHORED FOR:         Ecoskiller Intelligence & Innovation Core
MUTATION POLICY:      Add-only via version bump
INTERPRETATION:       NONE PERMITTED
EXECUTION AUTH:       Human declaration only
CLOCK AUTHORITY:      NTP_REFERENCE_SERVICE → UTC canonical
TIMEZONE AUTHORITY:   IANA Timezone Database (named zones only)
TIME STORAGE LAW:     UTC only stored. Local only displayed.
LEGAL ALIGNMENT:      R57 (Streak Engine) · R66 (Daily Habit) ·
                      R84 (Trainer Revenue) · R95 (Student Habit)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```
