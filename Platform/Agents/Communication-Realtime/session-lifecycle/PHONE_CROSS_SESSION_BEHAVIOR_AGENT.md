# PHONE_CROSS_SESSION_BEHAVIOR_AGENT
## ECOSKILLER PLATFORM — SEALED EXECUTION PROMPT

```
PROMPT_CLASS         = PHONE_CROSS_SESSION_BEHAVIOR_AGENT
EXECUTION_ENGINE     = ANTIGRAVITY
ENGINEERING_GRADE    = PRINCIPAL_ENGINEER
DOCUMENT_VERSION     = v1.0.0
STATUS               = FINAL · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY      = ADD_ONLY VIA VERSION BUMP
ASSUMPTION_POLICY    = FORBIDDEN
IMPLICIT_BEHAVIOR    = FORBIDDEN
FAILURE_POLICY       = HARD_STOP → REPORT → NO PARTIAL OUTPUT
INTERPRETATION_AUTH  = NONE
EXECUTION_AUTHORITY  = HUMAN DECLARATION ONLY
```

---

## SECTION 0 — AGENT IDENTITY & SCOPE

```
AGENT_NAME           = PHONE_CROSS_SESSION_BEHAVIOR_AGENT
AGENT_TYPE           = Cross-Session Behavioral Aggregator ·
                       Longitudinal Profile Builder ·
                       Retention & Churn Engine ·
                       Lifecycle State Machine Governor ·
                       ERP Behavioral Intelligence Producer
AGENT_SCOPE          = ALL user roles across ALL platform domains.
                       Spans every session boundary from account creation
                       to career stage evolution, skill obsolescence, alumni
                       longitudinal impact, and permanent lifetime records.
PARENT_CONSTITUTION  = ECOSKILLER MASTER EXECUTION PROMPT v12.0
SIBLING_AGENTS       = MODEL_GOVERNANCE_REGISTRY_AGENT
                       PHONE_EXTERNAL_WEBHOOK_AGENT
                       PHONE_FEATURE_VECTOR_EMISSION_AGENT
CONTRACT_GATE_ROLE   = PRODUCES: cross_session_behavior_ready
                       BLOCKS: Career Lifecycle Engine · Churn Prediction Model ·
                       Re-engagement Automation · Longitudinal Impact Service ·
                       ERP Retention Dashboards · Skill Obsolescence Detector ·
                       Growth Loop Analytics · User Segment Engine ·
                       Lifetime Skill Passport Writer · Success Story Generator
                       — until ALL cross-session schemas, aggregation windows,
                       retention rules, and lifecycle contracts are registered
                       and validated by this agent.
```

This agent governs the complete lifecycle of behavioral intelligence that
**crosses individual session boundaries**. Where the
`PHONE_FEATURE_VECTOR_EMISSION_AGENT` captures what happens *inside* a single
session, this agent governs what those sessions mean *together over time* —
patterns, trajectories, decay, reactivation, and eventual life-stage transitions.

Specific responsibilities:

- **Cross-session aggregation windows** — rolling 7 / 14 / 30 / 90 / 365-day
  behavioral summaries per user per domain
- **Career Stage State Machine** (R71) — deterministic stage transitions from
  Student → Job Seeker → Professional → Mentor, governed by inactivity rules and
  platform-verified life events
- **Streak & Daily Habit Engine** (R57 / R95) — cross-session streak counters,
  freeze tokens, leaderboards, and daily action logs
- **Skill obsolescence detection** (R73) — last-used timestamps per skill,
  inactivity thresholds, re-engagement triggers
- **Churn risk scoring** — multi-factor dormancy model flagging users before they
  lapse; feeds re-engagement notification pipeline
- **Longitudinal Impact Service** (Society domain) — alumni income uplift
  tracking, skill heatmap across cohort cohorts over years
- **User Segment Engine** — real-time RFM segmentation (Recency · Frequency ·
  Milestone) powering ERP dashboards and Growth Loop Orchestrator (R70)
- **Lifetime Skill Passport** (R72) — immutable append-only record written at
  every skill milestone, verification link generation
- **Success Story Detection** (R80) — milestone event listening across sessions
  to trigger auto-narrative generation pipeline
- **Growth Funnel Tracker** (R70) — cross-session activation scoring, loop
  performance statistics, organic funnel attribution

```
ABSENCE OF THIS AGENT'S CONTRACTS          → STOP EXECUTION
CROSS-SESSION AGGREGATE WITHOUT SCHEMA     → HARD_STOP
                                             REPORT CSB_UNREGISTERED_AGGREGATE:{name}
CAREER STAGE CHANGED BY ML/AI              → HARD_STOP
                                             REPORT CSB_ML_CAREER_STAGE_VIOLATION
                                             (R71: deterministic rule-engine ONLY)
LIFETIME SKILL RECORD DELETED OR UPDATED   → HARD_STOP
                                             REPORT CSB_PASSPORT_IMMUTABILITY_VIOLATION
PII IN ANY AGGREGATE OR SEGMENT PAYLOAD    → HARD_STOP
                                             REPORT CSB_PII_IN_AGGREGATE:{field}
```

---

## SECTION 1 — SYSTEM CONTEXT (READ-ONLY)

Antigravity must treat the following as immutable platform reality.

### 1.1 Platform Identity
```
PLATFORM_NAME            = ECOSKILLER
CLIENT_PRIMARY           = Flutter (Android · iOS · Desktop)
CLIENT_WEB_SEO           = Next.js (read-only SEO clone)
PRIMARY_DB               = PostgreSQL 15 (cross-session state · career stage · streaks)
ANALYTICS_DB             = ClickHouse (append-only aggregation tables)
STREAM_BACKBONE          = Apache Kafka 3.7.0
WORKFLOW_ENGINE          = Apache Airflow 2.9.0 (nightly + hourly aggregation jobs)
DURABLE_WORKFLOW         = Temporal (payout flows, franchise termination, dispute
                           resolution — durable state machines per society spec)
CACHE_LAYER              = Redis 7 (hot segment cache · streak state · churn scores)
VECTOR_STORE             = Qdrant (alumni similarity scoring · skill portfolio embeddings)
SELF_HOSTED_MANDATE      = TRUE — no Mixpanel, Amplitude, Segment, or any paid
                           behavioral analytics SaaS
TENANT_MODEL             = Multi-tenant · Hard RLS on tenant_id on ALL tables
STAGE_AUTHORITY          = Four-Stage Development Model is LOCKED:
                           STAGE_1 (Foundation) → STAGE_2 (Intelligence) →
                           STAGE_3 (Ecosystem) → STAGE_4 (Compliance & Scale)
```

### 1.2 Law Inheritance (All rules below are derived from these master laws)
```
R57   Daily Habit & Streak Engine Law
R60   Long-Term Archival & Data History Law
R61   Data Network Effect Analytics Law
R66   Daily Habit Formation Automation Law
R70   Zero-Marketing Organic Growth Governance Law
R71   Career Lifecycle Capture System Law
R72   Lifetime Skill Passport Law
R73   Career Gap & Skill Obsolescence Detection Law
R79   Trust & Reputation Amplification Law
R80   Continuous Success Story Generation Law
R95   Student Habit, Streak & Retention Loop Law
SOCIETY_ARCH longitudinal-impact-service (Alumni tracking, income uplift, skill heatmap)
```

### 1.3 Upstream Gate Dependency
```
REQUIRES_GATE            = feature_vector_ready (from PHONE_FEATURE_VECTOR_EMISSION_AGENT)
                           db_ready (from MODEL_GOVERNANCE_REGISTRY_AGENT)
                           event_schema_ready (from MODEL_GOVERNANCE_REGISTRY_AGENT)
RATIONALE                = Cross-session aggregation consumes session-level signals
                           registered by the Emission Agent. ClickHouse tables and
                           Kafka topics must already exist.
```

---

## SECTION 2 — CROSS-SESSION AGGREGATION ARCHITECTURE

### 2.1 Aggregation Pipeline (LOCKED)
```
SOURCE_1: ClickHouse (raw signal tables from PHONE_FEATURE_VECTOR_EMISSION_AGENT)
SOURCE_2: PostgreSQL (job/application lifecycle · skill records · career stage)
SOURCE_3: Kafka (real-time domain events: career_stage_changed, skill_record_added,
          billing events, payout events, society events)
          ↓
AGGREGATION_WORKERS (Apache Airflow DAGs — scheduled)
          ↓
  ├──→ PostgreSQL cross_session schema  (mutable aggregates — current state)
  ├──→ ClickHouse ch_cross_session_*   (immutable time-series history)
  └──→ Redis                           (hot-path segment cache · streak state)
          ↓
CONSUMERS:
  ├── Career Lifecycle Engine  (career stage transitions)
  ├── Churn Risk Scorer        (dormancy + re-engagement triggers)
  ├── Skill Obsolescence Detector
  ├── User Segment Engine      (RFM segments → ERP dashboards)
  ├── Growth Loop Orchestrator (R70 activation scoring)
  ├── Longitudinal Impact Service (society domain alumni)
  ├── Lifetime Skill Passport Writer
  └── Success Story Detector   (milestone → narrative trigger)
```

### 2.2 Aggregation Standards
```
AGGREGATION_CLOCK        = UTC (all windows are UTC-aligned)
WINDOW_TYPES             = rolling_7d · rolling_14d · rolling_30d · rolling_90d · rolling_365d
                           + lifetime (since account_created_at)
SCHEDULE_CADENCES:
  real-time    = Kafka Streams consumer (streak state, career stage triggers)
  hourly       = Airflow DAG: churn_score_refresh
  nightly      = Airflow DAG: behavioral_aggregate_refresh (00:30 UTC)
  weekly       = Airflow DAG: longitudinal_impact_refresh (Sunday 01:00 UTC)
  monthly      = Airflow DAG: skill_passport_health_scan + cohort_analysis_refresh
IDEMPOTENCY              = all Airflow DAGs use idempotent SQL upserts
                           (INSERT ... ON CONFLICT DO UPDATE)
TENANT_ISOLATION         = WHERE tenant_id = ? enforced on ALL aggregation queries
MISSING_DAG_SCHEDULE     → STOP EXECUTION → REPORT CSB_AIRFLOW_DAG_MISSING:{dag_id}
```

---

## SECTION 3 — BEHAVIORAL AGGREGATE SCHEMA REGISTRY

All cross-session aggregate tables live in the `cross_session` PostgreSQL schema.
Every table has: `tenant_id UUID NOT NULL`, `user_id UUID NOT NULL`, soft-delete
(`is_active BOOLEAN DEFAULT TRUE`), `created_at`, `updated_at`.
Hard DELETE is FORBIDDEN on all tables in this section.

---

### 3.1 USER ENGAGEMENT AGGREGATES

#### CSB_AGG_001: user_engagement_summary
```sql
CREATE TABLE cross_session.user_engagement_summary (
  id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id                 UUID NOT NULL,
  user_id                   UUID NOT NULL,
  role                      VARCHAR(50) NOT NULL,
  domain_track              VARCHAR(50),

  -- Rolling windows (refreshed nightly by Airflow)
  sessions_7d               SMALLINT  DEFAULT 0,
  sessions_14d              SMALLINT  DEFAULT 0,
  sessions_30d              SMALLINT  DEFAULT 0,
  sessions_90d              SMALLINT  DEFAULT 0,

  avg_session_duration_7d_seconds   INTEGER DEFAULT 0,
  avg_session_duration_30d_seconds  INTEGER DEFAULT 0,

  screens_visited_7d        SMALLINT  DEFAULT 0,
  screens_visited_30d       SMALLINT  DEFAULT 0,

  actions_taken_7d          SMALLINT  DEFAULT 0,
  actions_taken_30d         SMALLINT  DEFAULT 0,

  -- Lifetime totals
  total_sessions_lifetime   INTEGER   DEFAULT 0,
  total_actions_lifetime    INTEGER   DEFAULT 0,
  first_session_at          TIMESTAMPTZ,
  last_session_at           TIMESTAMPTZ,

  -- Derived engagement tier (computed, not ML)
  engagement_tier           VARCHAR(20) DEFAULT 'NEW',
  -- Values: NEW · ACTIVE · POWER · AT_RISK · DORMANT · CHURNED

  -- Churn risk
  churn_risk_score          FLOAT     DEFAULT 0.0,
  -- 0.0–1.0; refreshed hourly; threshold rules in Section 5
  churn_risk_band           VARCHAR(20) DEFAULT 'LOW',
  -- Values: LOW · MEDIUM · HIGH · CRITICAL
  churn_score_refreshed_at  TIMESTAMPTZ,

  days_since_last_session   INTEGER   GENERATED ALWAYS AS
    (EXTRACT(DAY FROM NOW() - last_session_at)::INTEGER) STORED,

  created_at                TIMESTAMPTZ DEFAULT NOW(),
  updated_at                TIMESTAMPTZ DEFAULT NOW(),
  is_active                 BOOLEAN   DEFAULT TRUE,

  CONSTRAINT uq_user_engagement UNIQUE (tenant_id, user_id)
);
CREATE INDEX idx_ueng_tenant_churn ON cross_session.user_engagement_summary
  (tenant_id, churn_risk_band, last_session_at);
CREATE INDEX idx_ueng_tenant_tier ON cross_session.user_engagement_summary
  (tenant_id, engagement_tier);
```

```
REFRESH_DAG        = behavioral_aggregate_refresh (nightly)
CHURN_SCORE_DAG    = churn_score_refresh (hourly)
ENGAGEMENT_TIER_RULES:
  NEW      → last_session_at IS NULL OR sessions_lifetime < 3
  ACTIVE   → sessions_7d >= 2
  POWER    → sessions_7d >= 5 AND actions_taken_7d >= 20
  AT_RISK  → sessions_7d = 0 AND sessions_14d >= 1
  DORMANT  → sessions_30d = 0 AND sessions_90d >= 1
  CHURNED  → sessions_90d = 0
TIER_CHANGE_EMITS  = Kafka: user.engagement_tier.changed → feeds re-engagement pipeline
```

---

#### CSB_AGG_002: domain_activity_summary
```sql
CREATE TABLE cross_session.domain_activity_summary (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  user_id             UUID NOT NULL,
  domain_module       VARCHAR(50) NOT NULL,
  -- Values: job_portal · skill_development · dojo_gd · intelligence ·
  --         innovation · royalty · billing · society · erp

  actions_7d          SMALLINT  DEFAULT 0,
  actions_30d         SMALLINT  DEFAULT 0,
  actions_90d         SMALLINT  DEFAULT 0,
  actions_lifetime    INTEGER   DEFAULT 0,

  last_active_at      TIMESTAMPTZ,
  days_since_active   INTEGER   GENERATED ALWAYS AS
    (EXTRACT(DAY FROM NOW() - last_active_at)::INTEGER) STORED,

  -- Module-specific depth metric (varies per domain)
  depth_score_30d     FLOAT     DEFAULT 0.0,
  -- Job Portal: applications sent / jobs viewed ratio
  -- Skill: modules completed / modules started
  -- Dojo/GD: turns fully utilized / turns received
  -- Intelligence: test items answered / items served
  -- Innovation: ideas submitted / drafts started
  depth_score_definition VARCHAR(120),  -- human-readable formula label

  created_at          TIMESTAMPTZ DEFAULT NOW(),
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  is_active           BOOLEAN DEFAULT TRUE,

  CONSTRAINT uq_domain_activity UNIQUE (tenant_id, user_id, domain_module)
);
CREATE INDEX idx_dom_act_tenant_module ON cross_session.domain_activity_summary
  (tenant_id, domain_module, last_active_at);
```

```
REFRESH_DAG   = behavioral_aggregate_refresh (nightly)
USE_CASES:
  - Platform admin: which domains have highest drop-off per tenant
  - Institute ERP: student engagement depth per module
  - Churn scorer: domain-specific dormancy weighting
```

---

### 3.2 STREAK & DAILY HABIT TABLES (R57 · R95)

#### CSB_STR_001: streak_tracker
```sql
CREATE TABLE cross_session.streak_tracker (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL,
  user_id               UUID NOT NULL,
  streak_type           VARCHAR(50) NOT NULL,
  -- Registered types: login_daily · skill_module · gd_session · dojo_match ·
  --                   job_application · intelligence_test · society_attendance

  current_streak        SMALLINT  DEFAULT 0,
  longest_streak        SMALLINT  DEFAULT 0,
  streak_started_at     DATE,
  last_activity_date    DATE,

  -- Freeze token balance (R57)
  freeze_tokens_balance SMALLINT  DEFAULT 0,
  freeze_tokens_used    SMALLINT  DEFAULT 0,
  last_freeze_used_date DATE,

  -- Milestone tracking
  milestone_7d_achieved  BOOLEAN DEFAULT FALSE,
  milestone_30d_achieved BOOLEAN DEFAULT FALSE,
  milestone_100d_achieved BOOLEAN DEFAULT FALSE,

  -- Reward state
  pending_reward_box    BOOLEAN DEFAULT FALSE,
  last_reward_granted_at TIMESTAMPTZ,

  created_at            TIMESTAMPTZ DEFAULT NOW(),
  updated_at            TIMESTAMPTZ DEFAULT NOW(),
  is_active             BOOLEAN DEFAULT TRUE,

  CONSTRAINT uq_streak UNIQUE (tenant_id, user_id, streak_type)
);
CREATE INDEX idx_streak_tenant_type ON cross_session.streak_tracker
  (tenant_id, streak_type, current_streak DESC);
```

#### CSB_STR_002: daily_activity_log
```sql
CREATE TABLE cross_session.daily_activity_log (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID NOT NULL,
  user_id           UUID NOT NULL,
  activity_date     DATE NOT NULL,           -- UTC date
  streak_type       VARCHAR(50) NOT NULL,
  qualifying_action VARCHAR(80) NOT NULL,    -- which action satisfied the streak rule
  session_id        UUID,                    -- session in which action occurred
  created_at        TIMESTAMPTZ DEFAULT NOW(),

  CONSTRAINT uq_daily_log UNIQUE (tenant_id, user_id, activity_date, streak_type)
);
-- No soft-delete: daily_activity_log is append-only
-- No UPDATE or DELETE permitted after insert
CREATE INDEX idx_dal_tenant_user_date ON cross_session.daily_activity_log
  (tenant_id, user_id, activity_date DESC);
```

```
STREAK_EVALUATION_JOB = Airflow DAG: streak_midnight_evaluator (runs 00:05 UTC daily)
ALGORITHM (R57 / R95):
  FOR each user × streak_type:
    IF daily_activity_log entry exists for yesterday (UTC):
      streak_tracker.current_streak += 1
      IF current_streak > longest_streak: longest_streak = current_streak
      UPDATE streak_started_at if current_streak == 1
    ELSE IF freeze_tokens_balance > 0:
      freeze_tokens_balance -= 1
      freeze_tokens_used += 1
      last_freeze_used_date = yesterday
      (streak preserved, no increment)
    ELSE:
      current_streak = 0
      milestone_* = FALSE (milestones require unbroken streak)
    EVALUATE milestone thresholds:
      if current_streak >= 7  → milestone_7d_achieved = TRUE
                               → pending_reward_box = TRUE
                               → emit: streak.milestone.achieved (Kafka)
      if current_streak >= 30 → milestone_30d_achieved = TRUE → emit milestone event
      if current_streak >= 100 → milestone_100d_achieved = TRUE → emit milestone event

STREAK_TYPE_QUALIFYING_ACTION_MAP:
  login_daily          → any session_start signal (SIG_002)
  skill_module         → course_module_completed (SIG_021) or skill_assessment_submitted (SIG_022)
  gd_session           → gd_turn_speaking_ended (SIG_032) with utilization_pct > 0
  dojo_match           → dojo_peer_evaluation_submitted (SIG_041)
  job_application      → job_applied (SIG_012)
  intelligence_test    → intelligence_test_answer_submitted (SIG_050), min 5 items
  society_attendance   → society_attendance_marked (SIG_090) with attendance_method != NULL

FREEZE_TOKEN_GRANT_RULE:
  Every 30-day streak milestone → grant +1 freeze token (max balance: 3)
  Freeze tokens DO NOT roll over past 3; excess is discarded
```

#### CSB_STR_003: streak_freeze_token_ledger
```sql
CREATE TABLE cross_session.streak_freeze_token_ledger (
  id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id     UUID NOT NULL,
  user_id       UUID NOT NULL,
  streak_type   VARCHAR(50) NOT NULL,
  event_type    VARCHAR(20) NOT NULL,  -- GRANTED | CONSUMED | EXPIRED
  tokens_delta  SMALLINT NOT NULL,     -- +1 (GRANTED) or -1 (CONSUMED) or -N (EXPIRED)
  reason        VARCHAR(120),          -- e.g. "30-day milestone" or "streak gap on 2025-03-15"
  created_at    TIMESTAMPTZ DEFAULT NOW()
  -- Append-only: no UPDATE or DELETE
);
```

---

### 3.3 CAREER STAGE STATE MACHINE (R71)

#### CSB_CAR_001: career_stage
```sql
CREATE TABLE cross_session.career_stage (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  user_id             UUID NOT NULL,
  current_stage       VARCHAR(30) NOT NULL,
  -- ALLOWED STAGES (exactly):
  -- STUDENT · JOB_SEEKER · EARLY_PROFESSIONAL · MID_PROFESSIONAL ·
  -- SENIOR_PROFESSIONAL · MENTOR · RETIRED · INACTIVE
  stage_since_date    DATE NOT NULL,
  auto_transitioned   BOOLEAN DEFAULT FALSE,
  transition_reason   VARCHAR(200),  -- deterministic rule text (not ML reason)
  next_review_date    DATE,

  created_at          TIMESTAMPTZ DEFAULT NOW(),
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  is_active           BOOLEAN DEFAULT TRUE,

  CONSTRAINT uq_career_stage UNIQUE (tenant_id, user_id)
);
```

#### CSB_CAR_002: career_stage_history
```sql
CREATE TABLE cross_session.career_stage_history (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID NOT NULL,
  user_id           UUID NOT NULL,
  from_stage        VARCHAR(30),   -- NULL for initial assignment
  to_stage          VARCHAR(30) NOT NULL,
  transitioned_at   TIMESTAMPTZ DEFAULT NOW(),
  transition_source VARCHAR(30) NOT NULL,
  -- Values: SYSTEM_AUTO · USER_DECLARED · ADMIN_OVERRIDE
  rule_triggered    VARCHAR(120) NOT NULL,  -- the exact rule code that fired
  -- Append-only: no UPDATE or DELETE
);
CREATE INDEX idx_csh_tenant_user ON cross_session.career_stage_history
  (tenant_id, user_id, transitioned_at DESC);
```

```
CAREER_STAGE_DAG = career_stage_evaluator (Airflow, runs daily 01:00 UTC)

TRANSITION_RULES (R71 — DETERMINISTIC ONLY — NO ML):

RULE_CS_01: STUDENT → JOB_SEEKER
  CONDITION: profile.education_status = COMPLETED
             AND career_stage.stage_since_date > 30 days
             AND (job_applications_lifetime > 0 OR user_explicitly_declared)

RULE_CS_02: JOB_SEEKER → EARLY_PROFESSIONAL
  CONDITION: job_application hired event EXISTS (job.application.hired Kafka topic)
             AND employment_record.start_date is set

RULE_CS_03: EARLY_PROFESSIONAL → MID_PROFESSIONAL
  CONDITION: employment_record.years_experience >= 3
             AND (skill_passport.record_count >= 5 OR certifications_count >= 2)

RULE_CS_04: MID_PROFESSIONAL → SENIOR_PROFESSIONAL
  CONDITION: employment_record.years_experience >= 7

RULE_CS_05: *_PROFESSIONAL → MENTOR
  CONDITION: user_explicitly_declared = TRUE
             AND mentor_profile.verified = TRUE
             AND total_mentee_sessions >= 5

RULE_CS_06: ANY_STAGE → INACTIVE
  CONDITION: days_since_last_session >= 365
             AND no Kafka events from user in 365 days
  REVERSIBLE: TRUE (re-engagement restores prior stage)

RULE_CS_07: INACTIVE → [prior stage]
  CONDITION: session_start event received after INACTIVE assignment
  AUTO_RESTORE: TRUE
  EMITS: career_stage_changed (Kafka) with transition_source = SYSTEM_AUTO

ON_TRANSITION:
  1. INSERT INTO career_stage_history
  2. UPDATE career_stage.current_stage
  3. EMIT: career.stage.changed (Kafka)
  4. TRIGGER RBAC update (auth-service subscribes to this topic)
  5. EMIT: notification trigger for STAGE_TRANSITION_NOTIFICATION_MODAL (Flutter)

ML_PROHIBITION: No ML model may trigger or influence career stage transitions.
                Rule violations → HARD_STOP → REPORT CSB_ML_CAREER_STAGE_VIOLATION
```

---

### 3.4 SKILL OBSOLESCENCE TRACKING (R73)

#### CSB_SKL_001: skill_last_used
```sql
CREATE TABLE cross_session.skill_last_used (
  id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id                 UUID NOT NULL,
  user_id                   UUID NOT NULL,
  skill_id                  UUID NOT NULL,
  skill_category            VARCHAR(80),
  domain_track              VARCHAR(50),

  last_used_at              TIMESTAMPTZ,
  last_assessed_at          TIMESTAMPTZ,
  last_assessment_score     FLOAT,       -- 0.0–1.0, most recent
  times_assessed_lifetime   SMALLINT DEFAULT 0,
  times_used_in_projects    SMALLINT DEFAULT 0,

  days_since_last_used      INTEGER GENERATED ALWAYS AS
    (EXTRACT(DAY FROM NOW() - last_used_at)::INTEGER) STORED,

  -- Obsolescence risk
  obsolescence_risk_score   FLOAT DEFAULT 0.0,
  -- 0.0–1.0; computed by skill_obsolescence_scorer DAG
  -- Uses traditional ML classification (R28-2 — R73)
  obsolescence_risk_band    VARCHAR(20) DEFAULT 'NONE',
  -- NONE · LOW · MEDIUM · HIGH · CRITICAL
  relearning_recommended    BOOLEAN DEFAULT FALSE,
  recommendation_sent_at    TIMESTAMPTZ,

  created_at                TIMESTAMPTZ DEFAULT NOW(),
  updated_at                TIMESTAMPTZ DEFAULT NOW(),
  is_active                 BOOLEAN DEFAULT TRUE,

  CONSTRAINT uq_skill_last_used UNIQUE (tenant_id, user_id, skill_id)
);
CREATE INDEX idx_slu_tenant_risk ON cross_session.skill_last_used
  (tenant_id, obsolescence_risk_band, days_since_last_used DESC);
```

```
OBSOLESCENCE_DAG     = skill_obsolescence_scorer (Airflow, monthly)
INACTIVITY_THRESHOLDS:
  LOW      → days_since_last_used > 90  AND domain average demand unchanged
  MEDIUM   → days_since_last_used > 180
  HIGH     → days_since_last_used > 270 AND market demand trending UP (from R61 stats)
  CRITICAL → days_since_last_used > 365

ON_RELEARNING_RECOMMENDED:
  1. SET relearning_recommended = TRUE
  2. EMIT: skill.obsolete.detected (Kafka) → notification pipeline
  3. EMIT: relearning.recommended (Kafka) → recommendation engine
  4. Notification channel: PUSH + EMAIL (HIGH+ priority per PHONE_EXTERNAL_WEBHOOK_AGENT)
  5. Recommendation contains: courses addressing the gap, NOT a raw skill score

MARKET_DEMAND_SOURCE = ch_skill_demand_stats (R61 Skill_Demand_Stats ClickHouse table)
                       market demand is read from public analytics — NOT from other
                       users' private behavioral data
```

---

### 3.5 LIFETIME SKILL PASSPORT (R72)

#### CSB_PSP_001: skill_passport
```sql
CREATE TABLE cross_session.skill_passport (
  passport_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id       UUID NOT NULL,
  user_id         UUID NOT NULL,
  created_at      TIMESTAMPTZ DEFAULT NOW(),
  is_public       BOOLEAN DEFAULT TRUE,
  public_slug     VARCHAR(80) UNIQUE,  -- SEO-friendly URL slug
  verification_url VARCHAR(255),

  CONSTRAINT uq_passport UNIQUE (tenant_id, user_id)
);
```

#### CSB_PSP_002: skill_passport_records
```sql
CREATE TABLE cross_session.skill_passport_records (
  record_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  passport_id        UUID NOT NULL REFERENCES cross_session.skill_passport(passport_id),
  tenant_id          UUID NOT NULL,
  user_id            UUID NOT NULL,

  record_type        VARCHAR(30) NOT NULL,
  -- COURSE_COMPLETED · CERTIFICATION_EARNED · PROJECT_DELIVERED ·
  -- JOB_EXPERIENCE · SKILL_ASSESSED · DOJO_BELT_AWARDED · GD_SCORE_PERCENTILE

  skill_id           UUID,
  reference_entity_id UUID NOT NULL,  -- course_id, cert_id, job_id, etc.
  level              VARCHAR(30),     -- BEGINNER · INTERMEDIATE · ADVANCED · EXPERT
  verifier           VARCHAR(50),     -- PLATFORM · INSTITUTION · EMPLOYER · PEER
  score              FLOAT,           -- normalized 0.0–1.0 if applicable

  -- Immutability fields
  hash_signature     VARCHAR(64) NOT NULL,  -- SHA-256 of record content
  superseded_by      UUID,                  -- if NULL → current record
  recorded_at        TIMESTAMPTZ DEFAULT NOW(),

  -- No UPDATE or DELETE: records are APPEND-ONLY (R72: "never deleted; only superseded")
  CONSTRAINT chk_no_delete CHECK (TRUE)  -- enforced via DB trigger
);
CREATE INDEX idx_spr_passport ON cross_session.skill_passport_records
  (passport_id, recorded_at DESC);
CREATE INDEX idx_spr_tenant_user ON cross_session.skill_passport_records
  (tenant_id, user_id, record_type);
```

```
PASSPORT_WRITER_DAG = skill_passport_writer (Airflow, triggered by Kafka events)
TRIGGER_EVENTS (writes a new passport record on each):
  course_module_completed (SIG_021)    → if score > 0.7 AND module_type = 'certification'
  certificate.generated (Kafka)        → CERTIFICATION_EARNED record
  dojo.belt.awarded (Kafka)            → DOJO_BELT_AWARDED record
  job.application.hired (Kafka)        → JOB_EXPERIENCE record (start event)
  skill_assessment_submitted (SIG_022) → SKILL_ASSESSED record if score > 0.75
  gd.score.finalized (Kafka)           → GD_SCORE_PERCENTILE if score >= 70th percentile

HASH_SIGNATURE_ALGORITHM:
  SHA-256 of: (passport_id + record_type + reference_entity_id + score + recorded_at)
  stored as hex string (64 chars)
  Used by public verification endpoint to confirm tamper-free record

PUBLIC_VERIFICATION_ENDPOINT = GET /v1/passport/verify/{hash_signature}
  → returns: record_type, level, verifier, recorded_at (NO PII)

IMMUTABILITY_ENFORCEMENT:
  PostgreSQL trigger on skill_passport_records:
    BEFORE UPDATE OR DELETE → RAISE EXCEPTION 'Passport records are immutable'
  Application layer MUST NOT call UPDATE or DELETE on this table
  Violation → HARD_STOP → REPORT CSB_PASSPORT_IMMUTABILITY_VIOLATION
```

---

### 3.6 CHURN RISK & RE-ENGAGEMENT ENGINE

#### CSB_CHN_001: churn_risk_event_log
```sql
CREATE TABLE cross_session.churn_risk_event_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL,
  user_id               UUID NOT NULL,
  event_type            VARCHAR(40) NOT NULL,
  -- AT_RISK_DETECTED · DORMANT_DETECTED · CHURNED_DETECTED ·
  -- REENGAGEMENT_TRIGGERED · REENGAGEMENT_RESPONDED · REENGAGEMENT_FAILED
  risk_band_at_event    VARCHAR(20),
  days_since_session    INTEGER,
  trigger_source        VARCHAR(50),  -- HOURLY_JOB · REALTIME_KAFKA · MANUAL_ADMIN
  action_taken          VARCHAR(100), -- description of re-engagement action
  created_at            TIMESTAMPTZ DEFAULT NOW()
  -- Append-only
);
CREATE INDEX idx_churn_tenant_type ON cross_session.churn_risk_event_log
  (tenant_id, event_type, created_at DESC);
```

```
CHURN_SCORE_ALGORITHM (multi-factor, traditional ML R28-2, advisory output only):

  INPUT FEATURES (from CSB_AGG_001 + domain_activity_summary):
    days_since_last_session         weight: 0.35
    sessions_14d                    weight: 0.20
    domain_activity_any_30d         weight: 0.15
    streak_current_login_daily      weight: 0.10
    skill_passport_records_30d      weight: 0.05
    billing_plan_tier               weight: 0.05
    career_stage                    weight: 0.05
    gd_sessions_attended_30d        weight: 0.05

  RISK_BAND_THRESHOLDS:
    LOW      score < 0.30
    MEDIUM   0.30 ≤ score < 0.55
    HIGH     0.55 ≤ score < 0.75
    CRITICAL score ≥ 0.75

  RE-ENGAGEMENT_TRIGGER_RULES:
    MEDIUM  → send 1× personalized digest notification (in-app + push)
    HIGH    → send streak recovery reminder (push + email) + suggest top matched job
    CRITICAL → send win-back push + email + optional discount notification
               (discount eligibility: HUMAN DECISION via admin panel, not automated)

  RE-ENGAGEMENT IS ADVISORY: AI produces the risk score and notification trigger.
  The content of win-back offers REQUIRES human admin approval.
  No automated discount or subscription change without admin sign-off.

  CHURN_DAG = churn_score_refresh (Airflow, hourly)
  REAL_TIME_TRIGGER = Kafka consumer on session_end events: if days_since_last_session
                      crosses threshold after session_end, emit risk event immediately
```

---

### 3.7 USER SEGMENT ENGINE (RFM)

#### CSB_SEG_001: user_segment_assignment
```sql
CREATE TABLE cross_session.user_segment_assignment (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  user_id             UUID NOT NULL,
  role                VARCHAR(50),

  -- RFM dimensions (Recency · Frequency · Milestone)
  recency_score       SMALLINT DEFAULT 0,   -- 1–5 (5 = most recent)
  frequency_score     SMALLINT DEFAULT 0,   -- 1–5 (5 = most frequent)
  milestone_score     SMALLINT DEFAULT 0,   -- 1–5 (5 = highest achievement level)

  rfm_segment         VARCHAR(40),
  -- Registered segment codes:
  -- CHAMPIONS · LOYAL · POTENTIAL_LOYALIST · NEW_RISING · AT_RISK ·
  -- NEED_ATTENTION · ABOUT_TO_SLEEP · HIBERNATING · LOST

  rfm_segment_since   DATE,
  previous_segment    VARCHAR(40),
  segment_changed_at  TIMESTAMPTZ,

  -- ERP dashboard fields
  ltv_band            VARCHAR(20),   -- LOW · MEDIUM · HIGH · PLATINUM
  activation_score    FLOAT,         -- 0.0–1.0 (R70 Growth Loop input)

  computed_at         TIMESTAMPTZ DEFAULT NOW(),
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  is_active           BOOLEAN DEFAULT TRUE,

  CONSTRAINT uq_user_segment UNIQUE (tenant_id, user_id)
);
CREATE INDEX idx_seg_tenant_segment ON cross_session.user_segment_assignment
  (tenant_id, rfm_segment, computed_at DESC);
```

```
SEGMENT_DAG = user_segment_refresh (Airflow, nightly)

RFM_SCORING_RULES:
  RECENCY_SCORE:
    5 → sessions_7d  > 0
    4 → sessions_14d > 0 AND sessions_7d  = 0
    3 → sessions_30d > 0 AND sessions_14d = 0
    2 → sessions_90d > 0 AND sessions_30d = 0
    1 → sessions_90d = 0

  FREQUENCY_SCORE:
    5 → sessions_30d >= 20
    4 → sessions_30d >= 10
    3 → sessions_30d >= 5
    2 → sessions_30d >= 2
    1 → sessions_30d < 2

  MILESTONE_SCORE:
    5 → skill_passport_records_lifetime >= 10 OR dojo_belt_level >= 4 OR certs >= 5
    4 → skill_passport_records_lifetime >= 5  OR dojo_belt_level >= 2
    3 → skill_passport_records_lifetime >= 2
    2 → skill_passport_records_lifetime >= 1
    1 → skill_passport_records_lifetime = 0

  SEGMENT_MATRIX (RFM → Segment label):
    R≥4 · F≥4 · M≥4  → CHAMPIONS
    R≥3 · F≥3 · M≥3  → LOYAL
    R≥3 · F≥2 · M≥2  → POTENTIAL_LOYALIST
    R=5 · F=1 · M=1  → NEW_RISING
    R=2 · F≥3        → AT_RISK
    R=2 · F=2        → NEED_ATTENTION
    R=1 · F≥2        → ABOUT_TO_SLEEP
    R=1 · F=1 · M≥2  → HIBERNATING
    R=1 · F=1 · M=1  → LOST

  SEGMENT_CHANGE_EMITS = Kafka: user.segment.changed
    → consumed by: Growth Loop Orchestrator · ERP segment dashboards
    → NOT consumed by pricing engine (no automated pricing based on segment)

LTV_BAND_RULES:
  PLATINUM → billing_plan = ENTERPRISE AND sessions_90d > 50
  HIGH     → billing_plan in (PROFESSIONAL, INSTITUTION) OR milestone_score >= 4
  MEDIUM   → billing_plan in (STARTER, BASIC)
  LOW      → billing_plan = FREE

ACTIVATION_SCORE (R70 input):
  activation_score = (recency_score × 0.4 + frequency_score × 0.35 + milestone_score × 0.25) / 5.0
```

---

### 3.8 LONGITUDINAL IMPACT SERVICE (Society Domain)

#### CSB_LON_001: alumni_impact_record
```sql
CREATE TABLE cross_session.alumni_impact_record (
  id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id               UUID NOT NULL,
  user_id                 UUID NOT NULL,
  society_id              UUID NOT NULL,        -- originating society unit
  cohort_id               UUID,                 -- training batch cohort

  -- Income uplift tracking (anonymized bands — no exact amounts)
  income_band_at_entry    VARCHAR(30),
  -- BELOW_POVERTY · LOW_INCOME · LOWER_MIDDLE · MIDDLE · UPPER_MIDDLE · HIGH
  income_band_current     VARCHAR(30),
  income_band_updated_at  DATE,
  income_uplift_levels    SMALLINT DEFAULT 0,   -- bands moved up (0 = no change)

  -- Skill heatmap
  skills_acquired_count   SMALLINT DEFAULT 0,
  top_skill_category      VARCHAR(80),
  skill_portfolio_vector_id UUID,               -- Qdrant vector ID for similarity scoring

  -- Employment outcome
  employment_status       VARCHAR(30),
  -- UNEMPLOYED · SELF_EMPLOYED · EMPLOYED · ENTREPRENEUR · STUDENT
  employment_updated_at   DATE,

  -- Platform continued engagement
  sessions_since_graduation_90d SMALLINT DEFAULT 0,
  is_active_on_platform   BOOLEAN DEFAULT TRUE,

  first_recorded_at       DATE NOT NULL,
  last_updated_at         DATE DEFAULT CURRENT_DATE,

  CONSTRAINT uq_alumni UNIQUE (tenant_id, user_id, society_id)
);
CREATE INDEX idx_alumni_society ON cross_session.alumni_impact_record
  (tenant_id, society_id, income_uplift_levels DESC);
```

#### CSB_LON_002: cohort_longitudinal_summary
```sql
CREATE TABLE cross_session.cohort_longitudinal_summary (
  id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id                 UUID NOT NULL,
  society_id                UUID NOT NULL,
  cohort_id                 UUID NOT NULL,
  cohort_start_date         DATE NOT NULL,
  cohort_end_date           DATE,
  cohort_size               SMALLINT,

  -- Aggregate outcomes (no individual PII)
  avg_income_uplift_levels  FLOAT DEFAULT 0.0,
  pct_employed_at_6_months  FLOAT,
  pct_employed_at_12_months FLOAT,
  pct_active_on_platform    FLOAT,

  top_skills_acquired       TEXT[],              -- top 5 skill categories
  gender_split_female_pct   FLOAT,

  skill_heatmap_snapshot    JSONB,
  -- {skill_category: frequency_count, ...} — aggregate only, no names

  last_refreshed_at         TIMESTAMPTZ DEFAULT NOW(),
  created_at                TIMESTAMPTZ DEFAULT NOW(),

  CONSTRAINT uq_cohort_summary UNIQUE (tenant_id, society_id, cohort_id)
);
```

```
LONGITUDINAL_DAG = longitudinal_impact_refresh (Airflow, weekly — Sunday 01:00 UTC)

DATA_SOURCES:
  - alumni_impact_record (above)
  - society_analytics Kafka events (attendance, payout, scheme_approved)
  - skill_passport_records for society alumni users
  - Qdrant vector similarity for alumni skill portfolio clustering

INCOME_BAND_UPDATE_RULE:
  Income band NEVER inferred from platform behavior.
  Income band ONLY updated by:
    (a) User explicit self-declaration in profile screen
    (b) Employment outcome verified by society admin
    (c) PMKVY/scheme approval event (carries employment outcome data)

PII_RULE: Individual income amounts NEVER stored or emitted.
          Only band label (LOW_INCOME, MIDDLE, etc.) is stored.
          Cohort summaries are always aggregate (min cohort_size: 10 for summary publish)

QDRANT_COLLECTION = alumni_skill_portfolios
  vector_dim: 384 (Sentence Transformer, same as idea DNA fingerprint)
  used_for: alumni similarity scoring · talent recommendation for society jobs
```

---

### 3.9 GROWTH LOOP TRACKER (R70)

#### CSB_GRW_001: growth_funnel_events
```sql
CREATE TABLE cross_session.growth_funnel_events (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID NOT NULL,
  user_id           UUID NOT NULL,
  funnel_stage      VARCHAR(40) NOT NULL,
  -- REGISTERED · PROFILE_COMPLETED · FIRST_ACTION · STREAK_7D ·
  -- FIRST_JOB_APPLY · FIRST_ASSESSMENT_PASS · FIRST_GD_SESSION ·
  -- FIRST_IDEA_SUBMIT · FIRST_CERTIFICATE · INVITED_PEER · CONVERTED_PAID
  achieved_at       TIMESTAMPTZ NOT NULL,
  days_to_achieve   INTEGER,   -- days from account creation to this funnel stage
  source_loop       VARCHAR(40),
  -- DIRECT · REFERRAL · SEO_ORGANIC · STREAK_LOOP · CHALLENGE_LOOP ·
  -- SOCIAL_SHARE · NETWORK_EFFECT
  session_id        UUID,
  created_at        TIMESTAMPTZ DEFAULT NOW()
  -- Append-only
);
CREATE INDEX idx_gfe_tenant_stage ON cross_session.growth_funnel_events
  (tenant_id, funnel_stage, achieved_at DESC);
```

#### CSB_GRW_002: loop_performance_stats
```sql
CREATE TABLE cross_session.loop_performance_stats (
  id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id               UUID NOT NULL,
  loop_type               VARCHAR(40) NOT NULL,
  -- STREAK_LOOP · REFERRAL_LOOP · CHALLENGE_LOOP · SOCIAL_SHARE_LOOP ·
  -- SKILL_COMPLETION_LOOP · DOJO_BELT_LOOP · SOCIETY_TOURNAMENT_LOOP
  period_start            DATE NOT NULL,
  period_end              DATE NOT NULL,

  participants_count      INTEGER DEFAULT 0,
  completions_count       INTEGER DEFAULT 0,
  completion_rate         FLOAT,
  avg_days_to_complete    FLOAT,
  new_users_attributed    INTEGER DEFAULT 0,
  -- users whose first session came from this loop
  conversion_rate         FLOAT,
  -- new_users_attributed / participants who shared

  computed_at             TIMESTAMPTZ DEFAULT NOW(),
  CONSTRAINT uq_loop_stats UNIQUE (tenant_id, loop_type, period_start)
);
```

```
GROWTH_LOOP_DAG = growth_loop_analyzer (Airflow, weekly)

LOOP_ATTRIBUTION_RULES:
  STREAK_LOOP: user maintains streak_7d+ AND session source = app (not referral)
  REFERRAL_LOOP: new user session_start with referral_code → attribution to inviter loop
  CHALLENGE_LOOP: user joins challenge via peer invitation → attributed to CHALLENGE_LOOP
  SOCIAL_SHARE_LOOP: user arrives via share token (R77 share_tokens table)

FUNNEL_ACTIVATION_SCORING (R70):
  Activation Score = user_segment.activation_score (from CSB_SEG_001)
  Platform-level activation rate = users who reached FIRST_CERTIFICATE / REGISTERED
  Target: 15% activation rate within 90 days of registration (platform admin KPI)
  Below 10% → ALERT platform admin via ops dashboard
```

---

### 3.10 SUCCESS STORY DETECTOR (R80)

#### CSB_SUC_001: milestone_detection_log
```sql
CREATE TABLE cross_session.milestone_detection_log (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID NOT NULL,
  user_id           UUID NOT NULL,
  milestone_type    VARCHAR(60) NOT NULL,
  -- FIRST_JOB_PLACED · DOJO_BELT_LEVEL_5 · CERTIFICATION_TOP_10PCT ·
  -- IDEA_LICENSED · STREAK_100D · SKILL_PASSPORT_10_RECORDS ·
  -- SOCIETY_ALUMNI_INCOME_UPLIFT · PLACEMENT_AFTER_CAREER_GAP
  milestone_data    JSONB,
  -- Structured evidence for story generation (NO PII — use entity IDs only)
  story_generated   BOOLEAN DEFAULT FALSE,
  story_id          UUID,   -- FK to success_story_records once generated
  detected_at       TIMESTAMPTZ DEFAULT NOW()
  -- Append-only
);
```

```
MILESTONE_DETECTOR = Kafka consumer (always-on, not batched)
TRIGGER_EVENTS:
  job.application.hired              → FIRST_JOB_PLACED (if first hire for user)
  dojo.belt.awarded (belt_level=5)   → DOJO_BELT_LEVEL_5
  certificate.generated + top decile → CERTIFICATION_TOP_10PCT
  idea.licensed                      → IDEA_LICENSED
  streak milestone (100d)            → STREAK_100D
  skill_passport_records count >= 10 → SKILL_PASSPORT_10_RECORDS
  alumni_impact_record.income_uplift_levels > 0 → SOCIETY_ALUMNI_INCOME_UPLIFT
  job.application.hired WHERE career_stage_history
    shows INACTIVE gap > 180 days   → PLACEMENT_AFTER_CAREER_GAP

ON_MILESTONE_DETECTED:
  1. INSERT milestone_detection_log
  2. EMIT: career.milestone.detected (Kafka)
  3. Consumed by: success-story-service (R80) → uses LLM (R28-3 permitted)
                  to generate narrative for SEO page
  4. Story published ONLY after user OPT-IN consent
     (milestone detected ≠ story auto-published)
  5. User opt-in screen: "Your achievement unlocked! Share your story?" (Flutter)

PII_RULE: milestone_data JSONB contains ONLY entity IDs and scores.
          NO user name, phone, address, or Aadhaar in milestone_data.
          story-service fetches display name at render time (not stored in milestone log).
```

---

## SECTION 4 — CLICKHOUSE CROSS-SESSION TIME-SERIES TABLES

All ClickHouse tables in this section are append-only history tables.
They complement the PostgreSQL mutable-current-state tables in Section 3.

```sql
-- ch_engagement_tier_history: records every engagement tier change
CREATE TABLE ch_engagement_tier_history (
  snapshot_id       UUID,
  server_ts         DateTime64(3, 'UTC'),
  tenant_id         UUID,
  user_id           UUID,
  from_tier         LowCardinality(String),
  to_tier           LowCardinality(String),
  sessions_7d       UInt16,
  sessions_30d      UInt16,
  days_since_session UInt16
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, server_ts)
TTL server_ts + INTERVAL 3 YEAR;

-- ch_streak_history: daily streak snapshots for longitudinal analysis
CREATE TABLE ch_streak_history (
  snapshot_id       UUID,
  snapshot_date     Date,
  tenant_id         UUID,
  user_id           UUID,
  streak_type       LowCardinality(String),
  current_streak    UInt16,
  longest_streak    UInt16,
  freeze_used       Bool,
  milestone_hit     Nullable(UInt16)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(toDateTime(snapshot_date))
ORDER BY (tenant_id, user_id, streak_type, snapshot_date)
TTL toDateTime(snapshot_date) + INTERVAL 2 YEAR;

-- ch_career_stage_history_analytics: career stage changes for cohort analysis
CREATE TABLE ch_career_stage_history_analytics (
  event_id          UUID,
  server_ts         DateTime64(3, 'UTC'),
  tenant_id         UUID,
  from_stage        LowCardinality(String),
  to_stage          LowCardinality(String),
  transition_source LowCardinality(String),
  domain_track      LowCardinality(String),
  days_in_prev_stage UInt32
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, to_stage, server_ts)
TTL server_ts + INTERVAL 5 YEAR;
-- Note: no user_id in this table — cohort analytics only, not individual tracking

-- ch_rfm_segment_history: segment assignments over time for trend analysis
CREATE TABLE ch_rfm_segment_history (
  snapshot_id       UUID,
  snapshot_date     Date,
  tenant_id         UUID,
  rfm_segment       LowCardinality(String),
  segment_count     UInt32   -- count of users in this segment on this date
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(toDateTime(snapshot_date))
ORDER BY (tenant_id, rfm_segment, snapshot_date)
TTL toDateTime(snapshot_date) + INTERVAL 2 YEAR;
-- Note: aggregate counts only — no user_id in this table

-- ch_churn_event_history: churn risk band changes for model calibration
CREATE TABLE ch_churn_event_history (
  event_id          UUID,
  server_ts         DateTime64(3, 'UTC'),
  tenant_id         UUID,
  user_id           UUID,
  event_type        LowCardinality(String),
  from_band         LowCardinality(String),
  to_band           LowCardinality(String),
  days_since_session UInt16
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_growth_funnel_cohort: funnel stage achievement by registration cohort
CREATE TABLE ch_growth_funnel_cohort (
  cohort_month      Date,      -- month of user registration
  tenant_id         UUID,
  funnel_stage      LowCardinality(String),
  source_loop       LowCardinality(String),
  users_reached     UInt32,
  avg_days_to_reach Float32
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(toDateTime(cohort_month))
ORDER BY (tenant_id, cohort_month, funnel_stage)
TTL toDateTime(cohort_month) + INTERVAL 3 YEAR;
```

---

## SECTION 5 — AIRFLOW DAG REGISTRY

```
DAG_REGISTRY_LOCATION = /contracts/airflow/dags/{dag_id}/v{version}/dag.yaml
ALL DAGs MUST BE IDEMPOTENT (safe to re-run on same period without double-counting)
ALL DAGs MUST USE TENANT-ISOLATED QUERIES (WHERE tenant_id = ?)
MISSING DAG → STOP EXECUTION → REPORT CSB_AIRFLOW_DAG_MISSING:{dag_id}
```

| DAG ID | Schedule | Purpose | Source → Target |
|---|---|---|---|
| `behavioral_aggregate_refresh` | nightly 00:30 UTC | Refresh user_engagement_summary + domain_activity_summary | ClickHouse signal tables → PostgreSQL cross_session |
| `churn_score_refresh` | hourly | Recompute churn_risk_score + band per user | cross_session.user_engagement_summary → same table UPDATE |
| `streak_midnight_evaluator` | daily 00:05 UTC | Evaluate all streak types, apply freeze tokens, grant rewards | cross_session.daily_activity_log → streak_tracker |
| `career_stage_evaluator` | daily 01:00 UTC | Evaluate R71 deterministic transition rules | PostgreSQL (user profile + employment records) → career_stage |
| `skill_obsolescence_scorer` | monthly | Score skill obsolescence risk per user per skill | cross_session.skill_last_used + R61 market stats → obsolescence scores |
| `user_segment_refresh` | nightly | Compute RFM scores + segment assignment | cross_session.user_engagement_summary → user_segment_assignment |
| `skill_passport_writer` | event-driven (Kafka) | Write immutable skill passport records on qualifying events | Kafka milestone events → skill_passport_records |
| `longitudinal_impact_refresh` | weekly Sunday 01:00 UTC | Refresh alumni impact records + cohort summaries | PostgreSQL cross_session + society events → cohort_longitudinal_summary |
| `growth_loop_analyzer` | weekly | Compute loop performance stats + funnel cohort ClickHouse | growth_funnel_events → loop_performance_stats + ch_growth_funnel_cohort |
| `cross_session_ch_sync` | nightly 02:00 UTC | Sync engagement tier changes + segment changes to ClickHouse | PostgreSQL change log → ClickHouse history tables |

---

## SECTION 6 — KAFKA EVENT CONTRACT (CROSS-SESSION TOPICS)

```
career.stage.changed
  schema: {user_id, tenant_id, from_stage, to_stage, rule_triggered,
           transition_source, transitioned_at}
  consumed_by: auth-service (RBAC update) · notification-service ·
               analytics-service · intelligence-profile-service

user.engagement_tier.changed
  schema: {user_id, tenant_id, from_tier, to_tier, days_since_session,
           sessions_7d, changed_at}
  consumed_by: analytics-service · re-engagement notification pipeline ·
               ERP segment dashboards

user.segment.changed
  schema: {user_id, tenant_id, from_segment, to_segment, rfm_scores,
           activation_score, changed_at}
  consumed_by: Growth Loop Orchestrator · ERP dashboards ·
               recommendation engine

streak.milestone.achieved
  schema: {user_id, tenant_id, streak_type, milestone_days,
           reward_box_granted, achieved_at}
  consumed_by: notification-service · reward-engine · leaderboard-service

streak.broken
  schema: {user_id, tenant_id, streak_type, broken_streak_length,
           freeze_used, broken_at}
  consumed_by: notification-service (recovery prompt) · analytics-service

skill.obsolete.detected
  schema: {user_id, tenant_id, skill_id, skill_category,
           obsolescence_risk_band, days_since_used, detected_at}
  consumed_by: notification-service · recommendation-engine

career.milestone.detected
  schema: {user_id, tenant_id, milestone_type, milestone_data_hash,
           milestone_log_id, detected_at}
  consumed_by: success-story-service · notification-service

user.reengagement.triggered
  schema: {user_id, tenant_id, risk_band, days_dormant,
           action_type, triggered_at}
  consumed_by: notification-service · analytics-service

skill.passport.record.added
  schema: {passport_id, tenant_id, record_type, record_id,
           hash_signature, recorded_at}
  consumed_by: analytics-service · public verification index

MISSING TOPIC → STOP EXECUTION → REPORT CSB_KAFKA_TOPIC_MISSING:{topic}
```

---

## SECTION 7 — ERP BEHAVIORAL INTELLIGENCE DASHBOARDS

```
DATA_SOURCE        = PostgreSQL cross_session schema + ClickHouse history tables
QUERY_RULE         = WHERE tenant_id = ? ENFORCED on ALL queries
CONSUMER_SERVICES  = analytics-service (reads) · ERP Flutter UI screens
```

### 7.1 Required ERP Dashboards (Cross-Session Layer)

| Dashboard ID | Consumer Role | Core Data Source | Refresh |
|---|---|---|---|
| CSBD_001: User Retention Waterfall | Platform Admin | ch_engagement_tier_history | daily |
| CSBD_002: Streak Engagement Heatmap | Institute ERP + Platform Admin | ch_streak_history | daily |
| CSBD_003: Career Stage Distribution | Institute ERP | ch_career_stage_history_analytics | weekly |
| CSBD_004: Churn Risk Band Monitor | Platform Admin + Tenant Admin | user_engagement_summary | hourly |
| CSBD_005: RFM Segment Trend | Platform Admin | ch_rfm_segment_history | daily |
| CSBD_006: Skill Obsolescence Heatmap | Institute ERP | skill_last_used aggregated | monthly |
| CSBD_007: Growth Funnel Cohort | Platform Admin | ch_growth_funnel_cohort | weekly |
| CSBD_008: Loop Performance Stats | Platform Admin | loop_performance_stats | weekly |
| CSBD_009: Alumni Income Uplift | Society ERP | cohort_longitudinal_summary | weekly |
| CSBD_010: Society Alumni Skill Heatmap | Society ERP | cohort_longitudinal_summary | weekly |
| CSBD_011: Lifetime Skill Passport Volume | Platform Admin | skill_passport_records | daily |
| CSBD_012: Success Story Milestone Rate | Platform Admin | milestone_detection_log | daily |
| CSBD_013: Re-engagement Campaign Response | Platform Admin | churn_risk_event_log | daily |
| CSBD_014: Domain Activity Depth | Institute ERP + Recruiter ERP | domain_activity_summary | daily |
| CSBD_015: Activation Score Distribution | Platform Admin | user_segment_assignment | daily |

```
DASHBOARD_WITHOUT_TENANT_FILTER → HARD_STOP → REPORT CSBD_CROSS_TENANT_QUERY:{id}
ALUMNI_DASHBOARD_MIN_COHORT_SIZE = 10 (do not display cohort summary < 10 members)
INCOME_BAND_IN_INDIVIDUAL_VIEW   → FORBIDDEN (only cohort aggregate view permitted)
```

---

## SECTION 8 — PII & PRIVACY RULES

```
RULE_CSB_PII_01: career_stage_history.transition_reason MUST contain ONLY deterministic
                 rule code text (e.g. "RULE_CS_02: hire event received").
                 NO user-written free text allowed.

RULE_CSB_PII_02: cohort_longitudinal_summary MUST aggregate a MINIMUM of 10 alumni
                 before publishing to any ERP dashboard.
                 Cohorts < 10 members → data suppressed.

RULE_CSB_PII_03: income_band is self-declared or admin-verified ONLY.
                 Platform behavioral signals MUST NOT infer income.
                 Income inference from engagement patterns → FORBIDDEN.

RULE_CSB_PII_04: skill_passport_records are immutable but the VERIFICATION endpoint
                 returns only: record_type · level · verifier · recorded_at.
                 User name, employer name, or salary NEVER returned by verification endpoint.

RULE_CSB_PII_05: ch_career_stage_history_analytics table contains NO user_id.
                 Career stage cohort analysis is aggregate ONLY.

RULE_CSB_PII_06: ch_rfm_segment_history contains NO user_id.
                 Segment counts are aggregate ONLY per tenant per date.

RULE_CSB_PII_07: churn_risk_event_log.action_taken field MUST NOT contain
                 user personal data. Allowed: enum code + job category hint only.

RULE_CSB_PII_08: milestone_detection_log.milestone_data JSONB MUST contain ONLY
                 entity IDs and scores. No names, no contact details.

RULE_CSB_PII_09: alumni_impact_record.skill_portfolio_vector_id references Qdrant.
                 The vector itself contains no PII — only skill embedding values.

ALL PII VIOLATIONS → HARD_STOP → REPORT CSB_PII_VIOLATION:{rule_id}:{table}:{field}
```

---

## SECTION 9 — TENANT & DOMAIN ISOLATION CONTRACT

```
RULE_CSB_ISO_01: ALL cross_session PostgreSQL tables have tenant_id UUID NOT NULL.
                 Any INSERT without tenant_id → rejected by DB constraint.

RULE_CSB_ISO_02: ALL Airflow DAG queries use parameterized tenant_id.
                 Cross-tenant joins in DAG SQL → FORBIDDEN.

RULE_CSB_ISO_03: society_id isolation: Society ERP dashboards filter by BOTH
                 tenant_id AND society_id. Society data leaking across societies
                 within same tenant → ISOLATION VIOLATION.

RULE_CSB_ISO_04: career_stage transitions for STUDENT role are scoped to
                 institution_id within tenant. A student cannot see another
                 institution's student career stage data.

RULE_CSB_ISO_05: Longitudinal impact service alumni similarity (Qdrant) uses
                 tenant-scoped collection namespacing:
                 collection name = alumni_skill_portfolios_{tenant_id}
                 Cross-tenant vector search → FORBIDDEN.

RULE_CSB_ISO_06: ch_career_stage_history_analytics and ch_rfm_segment_history
                 are populated with tenant_id. Platform admin sees all tenants
                 aggregated; tenant admin sees own tenant only.

ALL ISOLATION VIOLATIONS → HARD_STOP → REPORT CSB_ISO_VIOLATION:{rule_id}
```

---

## SECTION 10 — CI VALIDATION GATE

```
GATE_SIGNAL_PRODUCED     = cross_session_behavior_ready
GATE_CONSUMED_BY         = Career Lifecycle Engine · Churn Prediction Model ·
                           Re-engagement Automation · Longitudinal Impact Service ·
                           ERP Retention Dashboards · Skill Obsolescence Detector ·
                           Growth Loop Analytics · User Segment Engine ·
                           Lifetime Skill Passport Writer · Success Story Service

CI_VALIDATOR_SCRIPT      = /ci/validators/cross_session_behavior_gate_validator.sh
TRIGGER                  = Every push to test, staging, production branches

VALIDATION_STEPS:
  1.  Verify all PostgreSQL cross_session tables registered in Section 3 have
      Flyway migrations present in /db/migrations/
  2.  Verify all tables in cross_session schema have tenant_id NOT NULL constraint
  3.  Verify skill_passport_records has DB trigger blocking UPDATE and DELETE
  4.  Verify career_stage transition rules contain NO ML model references
  5.  Verify all Airflow DAG IDs in Section 5 have DAG Python files present
  6.  Run DAG dry-run validation (airflow dags test) on each registered DAG
  7.  Verify all Kafka topics in Section 6 are registered in Apicurio Schema Registry
  8.  Verify ClickHouse tables in Section 4 have CREATE TABLE DDL registered
  9.  Parse Section 7 dashboards — verify all reference registered data sources
  10. Verify PII rules in Section 8:
        ch_career_stage_history_analytics has NO user_id column (static analysis)
        ch_rfm_segment_history has NO user_id column (static analysis)
        milestone_detection_log has no PII-typed columns (type analysis)
  11. Verify alumni_impact_record has NO exact income columns — band columns only
  12. Verify cohort_longitudinal_summary query includes min-cohort-size guard (>=10)
  13. Scan Airflow DAG SQL for any literal cross-tenant joins (static analysis)
  14. Verify upstream gate dependency:
        feature_vector_ready signal exists in gate_check registry before proceeding

ON_FAILURE:  CI HARD_STOP → block merge → post failure report to ops Slack channel
ON_SUCCESS:  PRODUCES gate artifact:
             cross_session_behavior_gate_check.json (all 14 steps: PASSED)
             GATE SIGNAL: cross_session_behavior_ready
```

---

## SECTION 11 — TECHNOLOGY BINDINGS (NON-NEGOTIABLE)

```
CROSS_SESSION_STATE_DB   = PostgreSQL 15 (cross_session schema) — mutable current state
HISTORY_DB               = ClickHouse (append-only time-series) — immutable history
STREAM_PROCESSOR         = Apache Kafka Streams (real-time tier: career stage, streaks)
BATCH_ORCHESTRATOR       = Apache Airflow 2.9.0 (nightly/weekly aggregations)
DURABLE_WORKFLOWS        = Temporal (society payout flows, franchise termination)
                           — NOT used for behavioral aggregation (Airflow handles this)
VECTOR_STORE             = Qdrant (alumni skill portfolio embeddings) — self-hosted
CACHE_LAYER              = Redis 7 (hot-path: churn scores, streak state, segment cache)
SCHEMA_REGISTRY          = Apicurio Registry (Kafka topic schemas)
MIGRATIONS               = Flyway (ALL PostgreSQL schema changes)

FORBIDDEN TOOLS:
  - Mixpanel / Amplitude / Heap → FORBIDDEN (paid SaaS behavioral analytics)
  - Segment CDP               → FORBIDDEN (paid SaaS)
  - AWS Glue / GCP Dataflow   → FORBIDDEN (paid managed service)
  - Salesforce / HubSpot CRM  → FORBIDDEN as segment data target
  - Any tool that sends cross-session behavioral data outside the self-hosted stack
    → FORBIDDEN

VIOLATION → HARD_STOP → REPORT CSB_FORBIDDEN_TOOL_DETECTED:{tool}
```

---

## SECTION 12 — ANTIGRAVITY EXECUTION COMMANDS

```
# Register a new behavioral aggregate table
REGISTER_CROSS_SESSION_AGGREGATE
  TABLE_NAME    = user_engagement_summary
  SCHEMA        = cross_session
  REFRESH_DAG   = behavioral_aggregate_refresh
  FILL_TEMPLATE = SECTION_3_AGGREGATE_TABLE

# Register a new Airflow DAG
REGISTER_AIRFLOW_DAG
  DAG_ID        = streak_midnight_evaluator
  SCHEDULE      = daily_00:05_UTC
  PURPOSE       = streak evaluation and freeze token application
  FILL_TEMPLATE = SECTION_5_DAG_REGISTRY

# Register career stage transition rule
REGISTER_CAREER_RULE
  RULE_ID       = RULE_CS_02
  FROM_STAGE    = JOB_SEEKER
  TO_STAGE      = EARLY_PROFESSIONAL
  CONDITION     = job.application.hired Kafka event received
  ML_ALLOWED    = FALSE
  FILL_TEMPLATE = SECTION_3_CAREER_STAGE_RULES

# Register a new ClickHouse cross-session table
GENERATE_CH_CROSS_SESSION_TABLE
  TABLE_NAME    = ch_streak_history
  PURPOSE       = daily streak snapshots for longitudinal analysis
  FILL_TEMPLATE = SECTION_4_CLICKHOUSE_DDL

# Validate all cross-session gates
VALIDATE_CROSS_SESSION_GATES
  SCOPE         = ALL
  OUTPUT        = cross_session_behavior_gate_check.json
```

---

## SECTION 13 — FINAL SEAL

```
AGENT_STATUS             = COMPLETE · SEALED · LOCKED
CHANGE_POLICY            = APPEND_ONLY VIA VERSION BUMP
ANTIGRAVITY_CONFUSION    = IMPOSSIBLE
ASSUMPTION_PERMITTED     = NONE
IMPLICIT_BEHAVIOR        = NONE
QUALITY_SCORE            = 10 / 10

GATE PRODUCED BY THIS AGENT:
  ✔ cross_session_behavior_ready

SERVICES AND SUBSYSTEMS UNLOCKED:
  ✔ Career Lifecycle Engine (R71 transitions can execute)
  ✔ Churn Prediction Model (FS features available)
  ✔ Re-engagement Automation (churn_risk_event_log ready)
  ✔ Longitudinal Impact Service (alumni_impact_record schema ready)
  ✔ ERP Retention Dashboards (all 15 CSBD_* dashboards can be generated)
  ✔ Skill Obsolescence Detector (R73 — DAG + schema ready)
  ✔ Growth Loop Analytics (R70 — growth_funnel_events + loop_performance_stats)
  ✔ User Segment Engine (user_segment_assignment ready)
  ✔ Lifetime Skill Passport Writer (R72 — immutable schema + writer DAG)
  ✔ Success Story Service (R80 — milestone_detection_log + Kafka trigger)

SERVICES BLOCKED UNTIL GATE PASSES:
  ✗ Any ERP retention dashboard reading cross_session schema
  ✗ Career stage RBAC update automation
  ✗ Re-engagement notification triggers
  ✗ Alumni longitudinal impact reports for Society ERP
  ✗ Skill obsolescence recommendations
  ✗ Growth funnel attribution (organic vs referral)

⚠️ Antigravity MUST NOT:
   - Use ML or any model to trigger career stage transitions (R71: rule-engine ONLY)
   - DELETE or UPDATE skill_passport_records rows (append-only, DB trigger enforced)
   - Store exact income amounts (income_band VARCHAR only)
   - Include user_id in ch_career_stage_history_analytics or ch_rfm_segment_history
   - Publish cohort_longitudinal_summary for cohorts with fewer than 10 members
   - Build a cross-tenant query in any Airflow DAG
   - Infer income from behavioral patterns
   - Use Mixpanel, Amplitude, Segment, or any paid behavioral analytics SaaS
   - Auto-publish success stories without user opt-in consent (R80)
   - Automate win-back discounts without human admin approval

⚠️ This agent inherits and enforces:
   - R57   Daily Habit & Streak Engine Law
   - R60   Long-Term Archival & Data History Law
   - R61   Data Network Effect Analytics Law
   - R66   Daily Habit Formation Automation Law
   - R70   Zero-Marketing Organic Growth Governance Law
   - R71   Career Lifecycle Capture System Law (ML for stage changes = FORBIDDEN)
   - R72   Lifetime Skill Passport Law (immutability = absolute)
   - R73   Career Gap & Skill Obsolescence Detection Law
   - R79   Trust & Reputation Amplification Law
   - R80   Continuous Success Story Generation Law (opt-in only)
   - R95   Student Habit, Streak & Retention Loop Law
   - SOCIETY_ARCH longitudinal-impact-service spec
   - M5 AI Reality Law (behavioral data feeds models; models advise only)
   - Multi-tenant RLS law (tenant_id mandatory on all cross_session tables)
   - Four-Stage Development Model (cross-session features are STAGE_2+)
   - PHONE_FEATURE_VECTOR_EMISSION_AGENT as upstream dependency
     (feature_vector_ready gate must pass before this agent's gate is attempted)

DOCUMENT_END: PHONE_CROSS_SESSION_BEHAVIOR_AGENT v1.0.0
STATUS: FINAL · LOCKED · GOVERNED · DETERMINISTIC
```
