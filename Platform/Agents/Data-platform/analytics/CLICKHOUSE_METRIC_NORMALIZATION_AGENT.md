# CLICKHOUSE_METRIC_NORMALIZATION_AGENT
## Ecoskiller Platform — Sealed & Locked Production Agent Specification
### Agent Class: Analytics & ERP Intelligence Layer — ClickHouse Normalization Engine
### Execution Engine: ANTIGRAVITY (Google)

---

```
ARTIFACT_CLASS        = PRODUCTION_AGENT_SPEC
AGENT_ID              = CLICKHOUSE_METRIC_NORMALIZATION_AGENT
VERSION               = v1.0.0
STATUS                = FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY       = ADD-ONLY VIA VERSION BUMP
CREATIVE_INTERP       = FORBIDDEN
ASSUMPTION_FILLING    = FORBIDDEN
EXECUTION_ENGINE      = ANTIGRAVITY
DEFAULT_BEHAVIOR      = DENY
FAILURE_MODE          = STOP_EXECUTION → REPORT → NO_PARTIAL_OUTPUT
INTERPRETATION_AUTH   = NONE
EXECUTION_AUTH        = HUMAN DECLARATION ONLY
DETERMINISM_RULE      = IDENTICAL INPUT → IDENTICAL NORMALIZED OUTPUT — ALWAYS
```

---

## ⚠️ ANTIGRAVITY BINDING DIRECTIVE (READ BEFORE ANY EXECUTION)

```
⚠️ Antigravity MUST NOT reinterpret this agent's normalization formulas, table schemas,
   or pipeline topology under any circumstance.

⚠️ Antigravity MUST NOT rename ClickHouse tables, alter partition keys, change ORDER BY
   keys, or merge MergeTree engine types without a version bump.

⚠️ Antigravity MUST NOT skip any section marked MANDATORY, NON-NEGOTIABLE, or LOCKED.

⚠️ Antigravity MUST NOT introduce AI/ML-based metric normalization — only rule-based,
   deterministic, auditable formulas are permitted under R28 Intelligence Cost Law.

⚠️ Antigravity MUST NOT allow cross-tenant metric reads — RLS and tenant partitioning
   are absolute requirements enforced at storage, query, and API layers.

⚠️ Antigravity MUST NOT produce partial outputs. Every section below is an atomic
   requirement. Absence of any component:
   → STOP EXECUTION
   → REPORT NORMALIZATION-AGENT-INCOMPLETE
   → NO DEPLOYMENT CLAIM PERMITTED
```

---

## SECTION 1 — AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

### 1.1 Agent Name
`CLICKHOUSE_METRIC_NORMALIZATION_AGENT`

### 1.2 Platform Context
This agent operates as a core analytics infrastructure component of the **Ecoskiller Unified
SaaS Platform** — a multi-tenant enterprise system spanning:

- **Job Portal Engine** — applications, offers, hiring funnels
- **Skill Development Engine** — courses, assessments, streaks
- **GD (Group Discussion) Dojo Engine** — voice sessions, turn compliance, scoring
- **Dojo Match Engine** — live matches, belt progression, mentor scoring
- **ERP Layer** — institute, corporate, SME, recruiter analytics and governance
- **Intelligence Engine** — reputation, trust, prediction, placement analytics
- **Billing & Subscription Engine** — plan usage, invoice generation, GST compliance

### 1.3 Agent Role
This agent is the **single authority** responsible for:

1. **Raw Event Ingestion** — consuming all Kafka events from every platform domain into ClickHouse
2. **Schema Normalization** — transforming heterogeneous raw event shapes into canonical metric structures
3. **Cross-Domain Metric Alignment** — ensuring GD, Dojo, Interview, Job, Billing, and Reputation metrics share normalized dimensional keys (tenant_id, user_id, surface, date)
4. **Aggregation Pipeline Orchestration** — running Airflow DAGs to materialize hourly, daily, and monthly aggregated views for ERP dashboards
5. **Metric Validation & Quality Gates** — detecting and quarantining malformed, duplicate, out-of-range, or stale events before they corrupt ERP reports
6. **Normalization Audit Trail** — logging every transformation, rejection, and anomaly for compliance and reproducibility
7. **ERP Metric Surface Exposure** — serving normalized, tenant-isolated metric APIs consumed by Grafana, internal ERP dashboards, and platform analytics screens
8. **Cross-Metric Ratio Computation** — computing derived metrics (rates, ratios, percentiles, rank positions) deterministically from normalized base metrics

### 1.4 Agent Classification

```
AGENT_TYPE            = Analytics Infrastructure — Normalization + ERP Pipeline
SCOPE                 = Platform-Wide (All Tenants, All Domains, All Roles)
DATA_CLASS            = Behavioral · Operational · Financial · Compliance · Performance
STORAGE_ENGINE        = ClickHouse (MergeTree family — engine-specific per table class)
PIPELINE_ENGINE       = Apache Airflow (scheduled DAGs — no manual cron)
SINK_CONSUMERS        = Grafana · Internal ERP Dashboards · Flutter Analytics Screens · Public Data API (R61)
AUDIT_CLASS           = Append-only · Immutable · Reproducible
INTELLIGENCE_CLASS    = Rule-based only (R28 enforced — no ML in normalization layer)
```

---

## SECTION 2 — SCOPE BOUNDARIES (HARD LOCK)

### 2.1 In Scope — ALL REQUIRED — ANTIGRAVITY MUST IMPLEMENT ALL

| Domain | Metric Surface | Normalization Requirement |
|---|---|---|
| GD Sessions | Speaking time, turns, interrupt rate, silence rate, completion rate | Per-session + per-user + per-tenant daily aggregation |
| Interview | Attendance, no-show, dropout, rescheduling, feedback score | Per-recruiter + per-institute + per-tenant daily aggregation |
| Job Application | Submission rate, ghost rate, offer acceptance rate, funnel conversion | Per-recruiter + per-job + per-tenant daily + monthly |
| Dojo Matches | Join rate, completion rate, scenario pass rate, belt attempt rate | Per-user + per-skill + per-tenant daily aggregation |
| Course/Skill | Enrollment, completion, assessment pass rate, streak health | Per-course + per-trainer + per-tenant daily aggregation |
| Reputation | Score band distribution, gate block rate, decay event rate, anomaly rate | Per-role + per-tenant daily snapshot |
| Billing | Invoice generated, payment success, overdue rate, plan utilization | Per-tenant daily + monthly |
| Phone Verification | OTP success rate, gate block rate, VoIP detection rate | Per-tenant daily |
| Trainer Performance | Feedback avg, placement success rate, complaint ratio | Per-trainer + per-tenant monthly |
| Institute Performance | Placement rate, GD batch completion, TPO engagement | Per-institute + per-tenant monthly |
| Recruiter Reliability | Offer genuine rate, ghost offer count, JD accuracy | Per-recruiter + per-tenant monthly |
| Skill Demand | Posting velocity, application volume, salary range | Platform-wide weekly (R61 public analytics) |
| Platform Funnel | User registration → profile complete → first application → offer | Platform-wide daily (ERP executive summary) |

### 2.2 Explicitly Out of Scope — FORBIDDEN — ANTIGRAVITY MUST NOT IMPLEMENT

```
❌ Raw audio or video data ingestion into ClickHouse
❌ PII fields in any ClickHouse table (no name, email, phone — use hashed user_id only)
❌ ML model training pipelines (Feature Store is separate — see AI & Data Infra Services)
❌ Real-time sub-second metric queries (this is batch + micro-batch — not OLTP)
❌ Direct ClickHouse writes from frontend clients
❌ Cross-tenant aggregation without explicit platform-admin authentication
❌ Metric formula modification without version bump + human sign-off
❌ Deletion of ClickHouse records (append-only — use TTL policies for expiry only)
❌ ClickHouse as primary operational database (PostgreSQL handles transactional data)
❌ AI/ML inference in normalization pipeline (R28: deterministic rules only)
```

---

## SECTION 3 — SYSTEM ARCHITECTURE (NON-NEGOTIABLE)

### 3.1 Service Identity

```
Service Name:         clickhouse-metric-normalization-service
Namespace:            analytics (Kubernetes)
Language:             Python 3.11 (FastAPI for metric API surface)
Pipeline Orchestrator: Apache Airflow (dedicated analytics namespace)
Storage Engine:       ClickHouse (self-hosted, analytics namespace)
Event Source:         Apache Kafka (all platform event topics)
Staging Store:        PostgreSQL (normalization audit log, quarantine table)
Secret Store:         HashiCorp Vault
Monitoring:           Prometheus + Grafana + Loki
Auth Boundary:        Keycloak JWT (ERP_ADMIN, PLATFORM_ADMIN, ANALYTICS_READER roles only)
```

### 3.2 Architecture Topology (LOCKED)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        KAFKA EVENT BUS                                   │
│  (All domain topics: gd.completed, interview.completed, job.applied,    │
│   match.scored, reputation.signal.applied, invoice.generated, ...)      │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │ Kafka Consumer (per-topic partitioned)
                               ▼
┌─────────────────────────────────────────────────────────────────────────┐
│              RAW EVENT INGESTOR (Kafka → ClickHouse Raw Layer)          │
│  - Schema validation against Event Schema Registry                       │
│  - Malformed events → Quarantine table (PostgreSQL)                     │
│  - Valid events → ClickHouse raw_events table (append-only)             │
│  - Deduplication: event_id idempotency check (Redis Bloom Filter)       │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │ Airflow DAG Triggers (6-min, 1h, 24h)
                               ▼
┌─────────────────────────────────────────────────────────────────────────┐
│         NORMALIZATION PIPELINE (Airflow DAG: metric_normalization)      │
│                                                                          │
│  Stage 1: Raw → Canonical (field mapping, type coercion, unit align)    │
│  Stage 2: Enrichment (tenant metadata, role lookup, surface tagging)    │
│  Stage 3: Deduplication (event_id + session_id + recorded_at window)    │
│  Stage 4: Range Validation (value bounds per metric type)               │
│  Stage 5: Derived Metric Computation (rates, ratios, percentiles)       │
│  Stage 6: Aggregation Materialization (hourly → daily → monthly)        │
│  Stage 7: Normalization Audit Write (PostgreSQL normalization_audit_log)│
└──────────────────────────────┬──────────────────────────────────────────┘
                               │ Normalized + Aggregated Writes
                               ▼
┌─────────────────────────────────────────────────────────────────────────┐
│              CLICKHOUSE NORMALIZED METRIC TABLES                        │
│  (MergeTree family, partitioned by tenant + date, ORDER BY locked)      │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │ Read (tenant-isolated queries)
                               ▼
┌────────────────────────────────────────────────────────────────────────┐
│              METRIC API SURFACE (FastAPI — analytics namespace)        │
│  - Grafana datasource endpoints                                        │
│  - ERP Dashboard API (Flutter + Web)                                   │
│  - Public Data API (R61 — anonymized, aggregated only)                 │
│  - Admin Export API (ERP_ADMIN role, tenant-scoped)                    │
└────────────────────────────────────────────────────────────────────────┘
```

### 3.3 Architecture Principles (HARD LOCK)

```
PRINCIPLE-A-01: ClickHouse is WRITE-ONCE — no UPDATE, no DELETE (TTL-only expiry)
PRINCIPLE-A-02: All normalization is deterministic — same raw event = same normalized output
PRINCIPLE-A-03: Every raw event is idempotency-checked before insertion (Bloom Filter + event_id)
PRINCIPLE-A-04: Tenant isolation is enforced at partition key, query WHERE clause, AND API layer
PRINCIPLE-A-05: No PII enters ClickHouse — user_id is UUID only, no name/email/phone
PRINCIPLE-A-06: Normalization failures do not silently discard — all rejections are quarantined + logged
PRINCIPLE-A-07: Aggregated views are materialized by Airflow — not computed at query time for ERP dashboards
PRINCIPLE-A-08: Formula changes require version bump — old formula results are preserved under prior version tag
PRINCIPLE-A-09: All ClickHouse queries from API surface use parameterized statements — no string interpolation
PRINCIPLE-A-10: Pipeline SLA: raw event → normalized metric available in ERP dashboard ≤ 10 minutes (micro-batch)
```

---

## SECTION 4 — KAFKA EVENT CONSUMPTION MAP (ALL REQUIRED — ANTIGRAVITY MUST IMPLEMENT ALL)

### 4.1 Event Topic → Normalization Target Map (LOCKED)

```
Kafka Topic                           Source Service                    Target ClickHouse Scope
─────────────────────────────────────────────────────────────────────────────────────────────────
gd.completed                          Voice GD Orchestrator             raw_events + gd_session_metrics
gd.participation.signal               Voice GD Orchestrator             raw_events + gd_participation_signals
participation.dropout.detected        Phone/Participation Agent         raw_events + dropout_events
interview.completed                   Interview Service                 raw_events + interview_metrics
interview.no_show.detected            Interview Service                 raw_events + interview_metrics
job.applied                           Application Service               raw_events + job_funnel_events
job.offer.issued                      Recruiter Service                 raw_events + job_funnel_events
job.offer.accepted                    Application Service               raw_events + job_funnel_events
job.offer.ghosted                     Application Service               raw_events + job_funnel_events
match.scored                          Dojo Match Engine                 raw_events + dojo_match_metrics
match.completed                       Dojo Match Engine                 raw_events + dojo_match_metrics
belt.eligible                         Belt Engine                       raw_events + belt_progression_events
reputation.signal.applied             Reputation Engine                 raw_events + reputation_metric_stream
reputation.gate.blocked               Reputation Engine                 raw_events + reputation_gate_events
reputation.decay.applied              Reputation Engine                 raw_events + reputation_metric_stream
phone.verified                        Phone Verification Engine         raw_events + phone_verification_metrics
phone.gate.blocked                    Phone Verification Engine         raw_events + phone_verification_metrics
invoice.generated                     Billing Service                   raw_events + billing_metrics
invoice.paid                          Billing Service                   raw_events + billing_metrics
invoice.overdue                       Billing Service                   raw_events + billing_metrics
subscription.created                  Billing Service                   raw_events + subscription_metrics
subscription.cancelled                Billing Service                   raw_events + subscription_metrics
course.module.completed               Skill/LMS Service                 raw_events + course_participation_metrics
course.assessment.passed              Skill/LMS Service                 raw_events + course_participation_metrics
course.enrollment.abandoned           Skill/LMS Service                 raw_events + course_participation_metrics
trainer.feedback.submitted            Trainer Service                   raw_events + trainer_performance_metrics
user.created                          Auth/User Service                 raw_events + platform_funnel_events
user.profile.completed                User Service                      raw_events + platform_funnel_events
security.reputation.tamper.attempt    Reputation Engine                 raw_events + security_anomaly_events
erp.analytics.snapshot.ready          Phone/Participation Agent         pipeline_trigger_only (no storage)
```

### 4.2 Kafka Consumer Configuration (LOCKED)

```
Consumer Group:         clickhouse-normalization-consumer
Auto Offset Reset:      earliest (no event loss on restart)
Max Poll Records:       500 per batch
Session Timeout:        30s
Heartbeat Interval:     10s
Max Poll Interval:      300s
Enable Auto Commit:     FALSE (manual commit after ClickHouse write confirmed)
Commit Strategy:        Commit only after successful idempotency check + raw insert
Failure Strategy:       On write failure → retain offset → retry with exponential backoff (max 5)
Dead Letter Queue:      Kafka topic: clickhouse.normalization.dlq (after 5 retries → DLQ + quarantine)
```

---

## SECTION 5 — CLICKHOUSE TABLE SCHEMAS (ALL REQUIRED — FULLY LOCKED)

### 5.1 Raw Events Table — Universal Intake Layer

```sql
-- ALL raw Kafka events land here first — append-only, no normalization at write time
CREATE TABLE raw_events (
    event_id            UUID,
    tenant_id           UUID,
    user_id             UUID,
    source_service      LowCardinality(String),     -- e.g. 'voice_gd_orchestrator'
    topic               LowCardinality(String),     -- Kafka topic name
    event_type          LowCardinality(String),     -- e.g. 'gd.completed'
    payload             String,                     -- JSON blob (raw, no schema enforcement here)
    schema_version      LowCardinality(String),     -- e.g. 'v1.0'
    ingested_at         DateTime64(3),
    event_date          Date MATERIALIZED toDate(ingested_at)
)
ENGINE = MergeTree()
PARTITION BY (tenant_id, event_date)
ORDER BY (tenant_id, event_type, ingested_at)
TTL event_date + INTERVAL 90 DAY DELETE
SETTINGS index_granularity = 8192;

-- Deduplication guard: ReplacingMergeTree shadow table for idempotency
CREATE TABLE raw_events_dedup_guard (
    event_id            UUID,
    tenant_id           UUID,
    ingested_at         DateTime64(3),
    event_date          Date MATERIALIZED toDate(ingested_at)
)
ENGINE = ReplacingMergeTree()
PARTITION BY (tenant_id, event_date)
ORDER BY (tenant_id, event_id)
TTL event_date + INTERVAL 90 DAY DELETE;
```

### 5.2 GD Session Metrics Table (LOCKED)

```sql
CREATE TABLE gd_session_metrics (
    metric_id               UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    session_id              UUID,
    user_id                 UUID,
    batch_id                UUID,
    topic_id                UUID,
    surface                 LowCardinality(String) DEFAULT 'GD',

    -- Participation signals (raw counts)
    turns_completed         UInt16,
    turns_skipped           UInt16,
    turns_total             UInt16,
    mic_open_seconds        UInt32,
    token_window_seconds    UInt32,          -- total speaking token time allocated
    interrupt_attempts      UInt16,
    silence_events          UInt16,
    network_drops           UInt16,
    early_exit              UInt8,           -- 0 or 1

    -- Derived / normalized rates (computed by normalization pipeline — NOT at write time)
    turn_completion_rate    Float32,         -- turns_completed / turns_total
    mic_utilization_rate    Float32,         -- mic_open_seconds / token_window_seconds
    interrupt_rate          Float32,         -- interrupt_attempts / turns_total
    silence_rate            Float32,         -- silence_events / turns_total

    -- Scoring output (rule-based, from GD Scoring Engine)
    gd_score                Float32,
    score_band              LowCardinality(String),

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    session_date            Date,
    recorded_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(recorded_at)
PARTITION BY (tenant_id, toYYYYMM(session_date))
ORDER BY (tenant_id, session_id, user_id)
TTL session_date + INTERVAL 2 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.3 Interview Metrics Table (LOCKED)

```sql
CREATE TABLE interview_metrics (
    metric_id               UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    session_id              UUID,
    user_id                 UUID,           -- candidate
    recruiter_id            UUID,
    institute_id            UUID,
    surface                 LowCardinality(String) DEFAULT 'INTERVIEW',

    -- Participation signals
    slot_booked             UInt8,
    attended                UInt8,
    no_show                 UInt8,
    dropout_midway          UInt8,
    reschedule_count        UInt8,
    feedback_submitted      UInt8,
    feedback_score          Float32,        -- recruiter-rated candidate score (0–10)
    candidate_rated_recruiter Float32,      -- candidate-rated recruiter score (0–10)

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    interview_date          Date,
    recorded_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(recorded_at)
PARTITION BY (tenant_id, toYYYYMM(interview_date))
ORDER BY (tenant_id, session_id, user_id)
TTL interview_date + INTERVAL 3 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.4 Job Funnel Events Table (LOCKED)

```sql
CREATE TABLE job_funnel_events (
    event_id                UUID,
    tenant_id               UUID,
    user_id                 UUID,           -- candidate (hashed UUID only — no PII)
    recruiter_id            UUID,
    job_id                  UUID,
    surface                 LowCardinality(String) DEFAULT 'JOB_APP',

    -- Funnel stage flags
    application_submitted   UInt8,
    application_complete    UInt8,          -- profile + docs fully attached
    shortlisted             UInt8,
    interview_invited       UInt8,
    offer_issued            UInt8,
    offer_accepted          UInt8,
    offer_declined          UInt8,
    offer_ghosted           UInt8,          -- offer issued, no response in SLA window
    ghost_application       UInt8,          -- candidate applied, zero follow-through

    -- Funnel conversion rates (computed by normalization pipeline)
    app_to_shortlist_rate   Float32,        -- populated at job-level daily aggregation
    shortlist_to_offer_rate Float32,
    offer_acceptance_rate   Float32,

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    event_date              Date,
    recorded_at             DateTime64(3)
)
ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_date))
ORDER BY (tenant_id, job_id, user_id, event_date)
TTL event_date + INTERVAL 3 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.5 Dojo Match Metrics Table (LOCKED)

```sql
CREATE TABLE dojo_match_metrics (
    metric_id               UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    match_id                UUID,
    user_id                 UUID,
    skill_id                UUID,
    mentor_id               UUID,
    surface                 LowCardinality(String) DEFAULT 'DOJO',

    -- Match participation
    match_joined            UInt8,
    scenario_completed      UInt8,
    dropout                 UInt8,
    rematch_requested       UInt8,
    belt_attempt            UInt8,
    belt_awarded            UInt8,

    -- Scoring
    match_score             Float32,
    peer_score              Float32,
    mentor_score            Float32,
    weighted_final_score    Float32,        -- from Scoring Engine (locked formula)

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    match_date              Date,
    recorded_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(recorded_at)
PARTITION BY (tenant_id, toYYYYMM(match_date))
ORDER BY (tenant_id, match_id, user_id)
TTL match_date + INTERVAL 2 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.6 Course Participation Metrics Table (LOCKED)

```sql
CREATE TABLE course_participation_metrics (
    metric_id               UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    user_id                 UUID,
    course_id               UUID,
    trainer_id              UUID,
    surface                 LowCardinality(String) DEFAULT 'COURSE',

    -- Participation
    modules_completed       UInt16,
    modules_total           UInt16,
    assessments_submitted   UInt16,
    assessments_passed      UInt16,
    streak_days             UInt16,
    enrollment_abandoned    UInt8,

    -- Derived rates
    module_completion_rate  Float32,        -- modules_completed / modules_total
    assessment_pass_rate    Float32,        -- assessments_passed / assessments_submitted
    streak_health_score     Float32,        -- streak_days capped at 30 / 30 (normalized 0–1)

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    activity_date           Date,
    recorded_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(recorded_at)
PARTITION BY (tenant_id, toYYYYMM(activity_date))
ORDER BY (tenant_id, course_id, user_id)
TTL activity_date + INTERVAL 2 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.7 Reputation Metric Stream Table (LOCKED)

```sql
CREATE TABLE reputation_metric_stream (
    event_id                UUID,
    tenant_id               UUID,
    user_id                 UUID,
    user_role               LowCardinality(String),     -- STUDENT|TRAINER|RECRUITER|INSTITUTE
    surface                 LowCardinality(String),
    signal_code             LowCardinality(String),

    -- Score deltas
    delta_value             Float32,
    old_score               Float32,
    new_score               Float32,
    score_band_before       LowCardinality(String),
    score_band_after        LowCardinality(String),

    -- Event classification
    event_class             LowCardinality(String),     -- SIGNAL|DECAY|GATE_BLOCK|ADMIN_OVERRIDE|ANOMALY_FLAG
    gate_action             LowCardinality(String),     -- populated only on gate events

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    event_date              Date,
    recorded_at             DateTime64(3)
)
ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_date))
ORDER BY (tenant_id, user_role, signal_code, event_date)
TTL event_date + INTERVAL 3 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.8 Billing Metrics Table (LOCKED)

```sql
CREATE TABLE billing_metrics (
    metric_id               UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    invoice_id              UUID,
    subscription_id         UUID,
    plan_id                 LowCardinality(String),
    surface                 LowCardinality(String) DEFAULT 'BILLING',

    -- Invoice lifecycle
    invoice_generated       UInt8,
    invoice_paid            UInt8,
    invoice_overdue         UInt8,
    payment_attempts        UInt8,
    amount_due              Decimal(12, 2),
    amount_paid             Decimal(12, 2),
    gst_amount              Decimal(12, 2),
    currency                LowCardinality(String) DEFAULT 'INR',

    -- Subscription
    subscription_active     UInt8,
    subscription_cancelled  UInt8,
    plan_utilization_pct    Float32,        -- usage / plan_limit (0–1, can exceed 1 for overage)

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    billing_date            Date,
    recorded_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(recorded_at)
PARTITION BY (tenant_id, toYYYYMM(billing_date))
ORDER BY (tenant_id, invoice_id)
TTL billing_date + INTERVAL 7 YEAR DELETE     -- financial retention: 7 years
SETTINGS index_granularity = 8192;
```

### 5.9 Platform Funnel Events Table (LOCKED)

```sql
-- Tracks the user journey from registration to first successful outcome
CREATE TABLE platform_funnel_events (
    event_id                UUID,
    tenant_id               UUID,
    user_id                 UUID,
    user_role               LowCardinality(String),
    surface                 LowCardinality(String) DEFAULT 'PLATFORM_FUNNEL',

    -- Funnel milestone flags
    registered              UInt8,
    email_verified          UInt8,
    phone_verified          UInt8,
    profile_completed       UInt8,
    first_gd_joined         UInt8,
    first_application       UInt8,
    first_interview         UInt8,
    first_offer_received    UInt8,
    first_offer_accepted    UInt8,
    first_dojo_match        UInt8,
    first_belt_awarded      UInt8,
    first_course_completed  UInt8,

    -- Time-to-milestone (seconds from registration)
    ttm_profile_completed   Int64,          -- -1 if not yet reached
    ttm_first_application   Int64,
    ttm_first_offer         Int64,

    -- Normalization metadata
    normalization_version   LowCardinality(String) DEFAULT 'v1.0',
    pipeline_run_id         UUID,
    event_date              Date,
    recorded_at             DateTime64(3)
)
ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_date))
ORDER BY (tenant_id, user_role, event_date)
TTL event_date + INTERVAL 3 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.10 ERP Aggregated Daily Snapshots Table (LOCKED)

```sql
-- Materialized daily: primary source for ERP dashboards — never computed at query time
CREATE TABLE erp_daily_snapshots (
    snapshot_id             UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    snapshot_date           Date,
    domain                  LowCardinality(String),  -- GD|INTERVIEW|JOB|DOJO|COURSE|REPUTATION|BILLING|FUNNEL

    -- Universal dimensional keys
    entity_type             LowCardinality(String),  -- PLATFORM|INSTITUTE|RECRUITER|TRAINER|USER
    entity_id               UUID,                    -- platform-wide snapshots use nil UUID

    -- Metric payload (JSONB-like in ClickHouse via Map type)
    metrics                 Map(String, Float64),    -- normalized metric key → value
    metric_version          LowCardinality(String) DEFAULT 'v1.0',

    -- Snapshot quality metadata
    input_event_count       UInt64,                  -- raw events that fed this snapshot
    quarantine_event_count  UInt64,                  -- events rejected before this snapshot
    pipeline_run_id         UUID,
    computed_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(computed_at)
PARTITION BY (tenant_id, toYYYYMM(snapshot_date))
ORDER BY (tenant_id, domain, entity_type, entity_id, snapshot_date)
TTL snapshot_date + INTERVAL 5 YEAR DELETE
SETTINGS index_granularity = 8192;
```

### 5.11 Normalization Quarantine Table — PostgreSQL (LOCKED)

```sql
-- Lives in PostgreSQL (not ClickHouse) — for human review + reprocessing
CREATE TABLE normalization_quarantine (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID,
    event_id            UUID,
    kafka_topic         TEXT NOT NULL,
    kafka_offset        BIGINT,
    kafka_partition     INT,
    raw_payload         JSONB NOT NULL,
    rejection_reason    TEXT NOT NULL,
    rejection_code      TEXT NOT NULL,    -- SCHEMA_INVALID | DUPLICATE | OUT_OF_RANGE | STALE | UNKNOWN_SURFACE
    normalization_stage TEXT NOT NULL,    -- INGEST | VALIDATE | ENRICH | COMPUTE | AGGREGATE
    pipeline_run_id     UUID,
    reprocess_eligible  BOOLEAN DEFAULT TRUE,
    reprocessed_at      TIMESTAMPTZ,
    reviewed_by         TEXT,            -- admin user_id who cleared this
    quarantined_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_nq_tenant ON normalization_quarantine(tenant_id);
CREATE INDEX idx_nq_topic ON normalization_quarantine(kafka_topic);
CREATE INDEX idx_nq_code ON normalization_quarantine(rejection_code);
CREATE INDEX idx_nq_eligible ON normalization_quarantine(reprocess_eligible) WHERE reprocess_eligible = TRUE;
```

---

## SECTION 6 — NORMALIZATION PIPELINE SPECIFICATION (SEALED)

### 6.1 Normalization Stages (ALL STAGES REQUIRED — ANTIGRAVITY MUST IMPLEMENT ALL)

#### STAGE 1 — Raw Event Validation (LOCKED)

```
INPUT:   Raw Kafka event (JSON payload)
PROCESS:
  1. Validate event_id is valid UUID → reject if malformed (SCHEMA_INVALID)
  2. Validate tenant_id is valid UUID and exists in tenants table → reject if unknown
  3. Validate event_type is in known Event Schema Registry → reject if unregistered
  4. Validate schema_version matches registered version for event_type → reject if mismatch
  5. Validate required fields present per schema → reject if missing mandatory fields
  6. Check event_id against Redis Bloom Filter → reject if duplicate (DUPLICATE)
  7. Check ingested_at vs NOW() → reject if event is > 48 hours stale (STALE)
OUTPUT:  Valid event forwarded to Stage 2 | Invalid → Quarantine table + Kafka DLQ

REJECTION CODES:
  SCHEMA_INVALID      → missing fields, wrong types, unknown event_type
  DUPLICATE           → event_id already seen (Bloom Filter hit)
  STALE               → event timestamp > 48 hours behind current time
  UNKNOWN_TENANT      → tenant_id not in active tenant registry
  SCHEMA_VERSION_MISMATCH → event schema version deprecated or unknown
```

#### STAGE 2 — Canonical Field Mapping (LOCKED)

```
INPUT:   Validated raw event
PROCESS:
  1. Map source-service-specific field names → canonical metric field names
     (per Event Schema Registry field map — no creative re-mapping permitted)
  2. Coerce all numeric fields to canonical types:
     Durations → seconds (Int64)
     Rates → Float32 (0.0–1.0)
     Counts → UInt32
     Scores → Float32 (0.0–100.0)
     Amounts → Decimal(12,2)
  3. Normalize surface tag:
     All GD events → surface = 'GD'
     All interview events → surface = 'INTERVIEW'
     etc. (canonical surface registry locked below)
  4. Attach pipeline_run_id to all records in this batch
OUTPUT:  Canonical event record forwarded to Stage 3
```

**Canonical Surface Registry (LOCKED — ADD-ONLY)**
```
GD          → Voice Group Discussion sessions
INTERVIEW   → Scheduled recruiter interviews
JOB_APP     → Job application funnel
DOJO        → Dojo match sessions
COURSE      → Course / skill participation
REPUTATION  → Reputation signal events
BILLING     → Invoice and subscription events
FUNNEL      → Platform-wide user journey events
SECURITY    → Anomaly and tamper events
PHONE       → Phone verification events
```

#### STAGE 3 — Enrichment (LOCKED)

```
INPUT:   Canonical event record
PROCESS:
  1. Enrich user_role: lookup role from users table (PostgreSQL read replica)
     → If role not found: tag as UNKNOWN_ROLE, still process (do not quarantine)
  2. Enrich entity_type: derive from event_type + surface
     (e.g., gd.completed + GD + user_role=STUDENT → entity_type=STUDENT_GD_PARTICIPANT)
  3. Attach tenant metadata: tenant_type (INSTITUTE|CORPORATE|SME|PLATFORM)
  4. Attach domain tag: Arts|Commerce|Science|Technology|Administration
     → Derive from institute/company domain profile
     → If unresolvable: tag as CROSS_DOMAIN
  5. Attach normalization_version = current active formula version
OUTPUT:  Enriched canonical record forwarded to Stage 4
```

#### STAGE 4 — Range Validation & Bounds Enforcement (LOCKED)

```
INPUT:   Enriched canonical record
PROCESS:
  Validate all numeric fields against locked bounds per metric type:

  METRIC TYPE                     MIN     MAX     ON VIOLATION
  ─────────────────────────────────────────────────────────────────────────
  mic_open_seconds                0       3600    CLAMP + flag (not reject)
  turns_completed                 0       100     CLAMP + flag
  interrupt_attempts              0       500     CLAMP + flag
  gd_score                        0       100     OUT_OF_RANGE → quarantine
  reputation_score                0       100     OUT_OF_RANGE → quarantine
  feedback_score                  0       10      OUT_OF_RANGE → quarantine
  match_score                     0       100     OUT_OF_RANGE → quarantine
  module_completion_rate          0.0     1.0     CLAMP + flag
  mic_utilization_rate            0.0     1.0     CLAMP + flag (can exceed in error)
  amount_due (billing)            0       10000000  OUT_OF_RANGE → quarantine + alert
  plan_utilization_pct            0.0     10.0    CLAMP at 10.0 (overage allowed)
  turns_total                     1       100     If 0 → quarantine (division guard)
  assessments_submitted           0       10000   CLAMP + flag

CLAMPING RULE:
  When a value is clamped: original_value stored in metadata JSONB, clamped_value used in metric,
  anomaly_flag = TRUE on that record, Kafka event emitted: normalization.value.clamped

OUT_OF_RANGE quarantine: event goes to normalization_quarantine with code OUT_OF_RANGE
OUTPUT:  Range-validated record forwarded to Stage 5
```

#### STAGE 5 — Derived Metric Computation (LOCKED — FORMULAS SEALED)

```
INPUT:   Range-validated canonical record
PROCESS:
  Compute all derived rates and ratios using only the locked formulas below.
  No creative formula variation permitted.
  Division guard: denominator = 0 → derived metric = NULL (not 0.0)

LOCKED DERIVED METRIC FORMULAS:

  GD Session Derived Metrics:
  ─────────────────────────────────────────────────────────────
  turn_completion_rate    = turns_completed / turns_total
  mic_utilization_rate    = mic_open_seconds / token_window_seconds
  interrupt_rate          = interrupt_attempts / turns_total
  silence_rate            = silence_events / turns_total
  early_exit_score        = 1.0 if early_exit = 0, else 0.0

  Interview Derived Metrics:
  ─────────────────────────────────────────────────────────────
  attendance_rate         = attended / slot_booked  (per session: binary)
  reschedule_burden       = reschedule_count / 1  (raw count, no normalization)
  feedback_quality_flag   = 1 if feedback_submitted = 1 AND feedback_score > 0 else 0

  Job Funnel Derived Metrics (job-level daily aggregation):
  ─────────────────────────────────────────────────────────────
  app_to_shortlist_rate   = SUM(shortlisted) / SUM(application_submitted)
  shortlist_to_offer_rate = SUM(offer_issued) / SUM(shortlisted)
  offer_acceptance_rate   = SUM(offer_accepted) / SUM(offer_issued)
  ghost_application_rate  = SUM(ghost_application) / SUM(application_submitted)
  ghost_offer_rate        = SUM(offer_ghosted) / SUM(offer_issued)

  Course Participation Derived Metrics:
  ─────────────────────────────────────────────────────────────
  module_completion_rate  = modules_completed / modules_total
  assessment_pass_rate    = assessments_passed / assessments_submitted
  streak_health_score     = MIN(streak_days, 30) / 30.0  (caps at 30 days = 1.0)

  Dojo Match Derived Metrics:
  ─────────────────────────────────────────────────────────────
  match_completion_rate   = scenario_completed / match_joined (per user, daily agg)
  belt_conversion_rate    = belt_awarded / belt_attempt (per user, monthly agg)

  Billing Derived Metrics (tenant daily):
  ─────────────────────────────────────────────────────────────
  payment_success_rate    = SUM(invoice_paid) / SUM(invoice_generated)
  overdue_rate            = SUM(invoice_overdue) / SUM(invoice_generated)
  avg_payment_attempts    = SUM(payment_attempts) / SUM(invoice_generated)

  Platform Funnel Conversion Rates (platform daily):
  ─────────────────────────────────────────────────────────────
  registration_to_profile = SUM(profile_completed) / SUM(registered)
  profile_to_application  = SUM(first_application) / SUM(profile_completed)
  application_to_offer    = SUM(first_offer_received) / SUM(first_application)
  offer_to_acceptance     = SUM(first_offer_accepted) / SUM(first_offer_received)

OUTPUT:  Fully enriched + derived record forwarded to Stage 6
```

#### STAGE 6 — Aggregation Materialization (LOCKED)

```
INPUT:   Fully derived per-event records (written to base metric tables in Stage 5)
PROCESS: Airflow DAG tasks compute and write aggregations at three cadences:

  MICRO-BATCH (every 6 minutes):
    → Re-aggregate last 30 minutes of base metric tables
    → Write to erp_daily_snapshots (ReplacingMergeTree — updates in place)
    → Target: ERP dashboard latency ≤ 10 minutes

  HOURLY AGGREGATION (every 1 hour):
    → Full re-aggregation of current day
    → Overwrite today's erp_daily_snapshots entries
    → Compute all derived rates from aggregated counts (not from per-event rates)

  DAILY FINALIZATION (at 02:00 UTC — previous day):
    → Final aggregation of yesterday
    → Write to erp_daily_snapshots with finalized=TRUE flag
    → Compute monthly rolling aggregations
    → Emit Kafka: erp.daily.snapshot.finalized

  MONTHLY AGGREGATION (1st of month at 03:00 UTC — previous month):
    → Aggregate all daily snapshots for prior month
    → Write to erp_monthly_snapshots (separate table)
    → Compute R61 public analytics figures (skill demand, hiring velocity, salary benchmarks)
    → Emit Kafka: erp.monthly.snapshot.finalized

AGGREGATION RULES:
  - Rates computed from SUM(counts) — never AVG(rates) to avoid ratio-of-ratios error
  - NULL values excluded from denominator (not treated as 0)
  - Minimum event count threshold for rate validity: ≥ 5 events per period (else rate = NULL)
  - Percentile computation: P50, P75, P90, P95 of all score distributions
```

#### STAGE 7 — Normalization Audit Write (LOCKED — MANDATORY)

```
INPUT:   Pipeline run metadata + stage outcome summary
PROCESS:
  After every pipeline run (micro-batch, hourly, daily, monthly):
  Write one record to normalization_audit_log (PostgreSQL):
    - pipeline_run_id
    - run_type: MICRO_BATCH | HOURLY | DAILY | MONTHLY
    - events_ingested
    - events_valid
    - events_quarantined
    - events_clamped
    - tables_written: array of ClickHouse table names
    - rows_inserted
    - rows_replaced (ReplacingMergeTree deduplication count)
    - duration_seconds
    - status: SUCCESS | PARTIAL_FAILURE | FAILURE
    - failure_reason (if applicable)
    - started_at
    - completed_at

  Emit Kafka: normalization.pipeline.run.completed

OUTPUT:  Audit record written → pipeline run complete
```

---

## SECTION 7 — AIRFLOW DAG SPECIFICATIONS (ALL REQUIRED)

### 7.1 DAG: `metric_normalization_micro_batch`

```python
# DAG Identity
dag_id           = 'metric_normalization_micro_batch'
schedule         = '@every 6 minutes'    # via timedelta(minutes=6)
catchup          = False
max_active_runs  = 1                     # no concurrent runs — prevents double-write
tags             = ['normalization', 'analytics', 'micro_batch']

# Task Chain (LOCKED ORDER)
Task 1: validate_kafka_consumer_lag     → Alert if consumer lag > 50,000 events
Task 2: ingest_raw_events_batch         → Kafka → raw_events (ClickHouse)
Task 3: run_dedup_guard                 → Redis Bloom Filter + raw_events_dedup_guard
Task 4: stage1_schema_validation        → Validate + quarantine invalids
Task 5: stage2_canonical_mapping        → Field normalization + type coercion
Task 6: stage3_enrichment               → Role + tenant + domain enrichment
Task 7: stage4_range_validation         → Bounds check + clamping
Task 8: stage5_derived_computation      → Compute all derived metrics
Task 9: write_base_metric_tables        → Write to domain-specific ClickHouse tables
Task 10: stage6_micro_agg               → Re-aggregate last 30 min → erp_daily_snapshots
Task 11: stage7_audit_write             → Write normalization_audit_log
Task 12: emit_pipeline_completed_event  → Kafka: normalization.pipeline.run.completed

On failure at any task:
  → Mark run as PARTIAL_FAILURE or FAILURE
  → Write audit record with failure_reason
  → Emit: normalization.pipeline.failed
  → Alert: Prometheus counter normalization_pipeline_failure_total++
  → DO NOT retry automatically (prevent double-write corruption)
  → Human review required before next run attempt
```

### 7.2 DAG: `metric_normalization_daily_finalization`

```python
dag_id           = 'metric_normalization_daily_finalization'
schedule         = '0 2 * * *'           # 02:00 UTC daily
catchup          = False
max_active_runs  = 1

# Task Chain
Task 1: validate_daily_completeness     → Check micro-batch coverage for prior day
Task 2: daily_aggregation_all_domains   → Full aggregation per tenant × domain
Task 3: compute_monthly_rolling         → 7-day, 30-day rolling averages
Task 4: compute_percentiles             → P50/P75/P90/P95 score distributions
Task 5: finalize_daily_snapshot         → Write finalized=TRUE to erp_daily_snapshots
Task 6: r61_public_analytics_refresh    → Update Skill_Demand_Stats, Salary_Benchmark_Stats (anonymized)
Task 7: audit_write                     → normalization_audit_log
Task 8: emit_finalized_event            → Kafka: erp.daily.snapshot.finalized
```

### 7.3 DAG: `metric_normalization_monthly`

```python
dag_id           = 'metric_normalization_monthly'
schedule         = '0 3 1 * *'           # 03:00 UTC on 1st of each month
catchup          = False
max_active_runs  = 1

# Task Chain
Task 1: validate_month_completeness     → All daily snapshots finalized for prior month
Task 2: aggregate_monthly_all_domains   → Sum/avg daily snapshots per tenant × domain
Task 3: compute_trainer_monthly_perf    → TrainerReputationScore monthly update inputs
Task 4: compute_institute_monthly_perf  → Institute placement rate, GD batch rate
Task 5: compute_recruiter_monthly_rel   → Recruiter reliability index
Task 6: write_erp_monthly_snapshots     → erp_monthly_snapshots ClickHouse table
Task 7: write_r61_public_monthly        → Anonymized public analytics (R61)
Task 8: audit_write                     → normalization_audit_log
Task 9: emit_monthly_finalized_event    → Kafka: erp.monthly.snapshot.finalized
```

---

## SECTION 8 — METRIC NORMALIZATION RULES REGISTRY (HARD LOCK)

### 8.1 Rate-of-Rates Prevention Rule (NON-NEGOTIABLE)

```
RULE-NR-01: ALL rates for ERP dashboards MUST be computed from SUM(numerator) / SUM(denominator)
            across the aggregation window.

            FORBIDDEN PATTERN:
              avg_completion_rate = AVG(completion_rate_per_event)
              → This produces ratio-of-ratios bias — STRICTLY FORBIDDEN

            REQUIRED PATTERN:
              completion_rate = SUM(sessions_completed) / SUM(sessions_joined)
              → Always aggregate raw counts first, then compute rate once

            Antigravity MUST enforce this in every aggregation query generated.
            Violation → STOP EXECUTION → REPORT RATIO_OF_RATIOS_VIOLATION
```

### 8.2 Minimum Sample Threshold Rule (NON-NEGOTIABLE)

```
RULE-NR-02: Any rate or ratio computed from fewer than 5 events in the aggregation window
            MUST be returned as NULL — not 0.0, not estimated, not interpolated.

            NULL is surfaced in ERP dashboards as "Insufficient data" — not as 0%.
            Antigravity MUST generate NULL-check logic in all aggregation queries.
```

### 8.3 Score Band Normalization (LOCKED)

```
RULE-NR-03: ALL score bands are assigned using this locked mapping.
            No custom banding permitted without version bump.

            SCORE BAND MAPPING (applies to: gd_score, reputation_score, match_score, weighted_final_score):

            Score Range     Band Label    ERP Color Token
            ─────────────────────────────────────────────
            0.0 – 19.99     CRITICAL      #E53E3E (red)
            20.0 – 39.99    LOW           #DD6B20 (orange)
            40.0 – 59.99    MEDIUM        #D69E2E (amber)
            60.0 – 79.99    HIGH          #38A169 (green)
            80.0 – 100.0    ELITE         #3182CE (blue)
```

### 8.4 Time-Series Smoothing Rule (LOCKED — R61 Compliance)

```
RULE-NR-04: Public analytics (R61 screens: Skill Heatmap, Hiring Trends, Salary Explorer)
            MUST use 7-day rolling average smoothing on all time-series metrics.

            Formula:
              smoothed_value(t) = SUM(raw_value(t-6) to raw_value(t)) / 7

            Where raw_value(t) for any day with NULL or < 5 events is excluded
            from both numerator and denominator (not treated as 0).

            All Skill_Demand_Stats and Job_Velocity_Stats writes must include
            both: raw_daily_value AND smoothed_7d_value columns.
```

### 8.5 Cross-Tenant Isolation Rule (NON-NEGOTIABLE)

```
RULE-NR-05: EVERY ClickHouse query generated by this agent MUST include:
              WHERE tenant_id = {tenant_id_param}
            as the FIRST filter in the WHERE clause.

            Platform-level aggregations (ERP executive summary, R61 public analytics)
            are the ONLY exception and require PLATFORM_ADMIN JWT role.

            Absence of tenant_id filter → QUERY REJECTED by API layer middleware.
            Antigravity MUST generate this middleware enforcement gate.
```

### 8.6 Normalization Version Binding Rule (NON-NEGOTIABLE)

```
RULE-NR-06: All ClickHouse metric records include normalization_version.
            When a formula changes, the new version tag is incremented.
            Old records retain their version tag permanently.
            ERP dashboard queries MUST specify which normalization_version to use.
            Default: latest version.
            Cross-version comparison requires explicit version params — never mixed silently.
```

---

## SECTION 9 — ERP METRIC API SURFACE (ALL REQUIRED)

### 9.1 API Identity

```
Service:        clickhouse-metric-api
Namespace:      analytics
Framework:      FastAPI (Python 3.11)
Auth:           Keycloak JWT — roles: ERP_ADMIN | PLATFORM_ADMIN | ANALYTICS_READER | GRAFANA_DATASOURCE
Rate Limit:     100 req/min per tenant (ERP_ADMIN) | 500 req/min (Grafana service account)
Response Format: JSON (application/json)
```

### 9.2 Metric Query APIs (ALL REQUIRED — LOCKED)

```
GET  /api/v1/metrics/gd/summary
     Auth: ERP_ADMIN (tenant-scoped)
     Query: ?tenant_id=&from=date&to=date&entity_type=PLATFORM|INSTITUTE|USER
     Returns: turn_completion_rate, mic_utilization_rate, interrupt_rate,
              completion_rate, dropout_rate, score_distribution (P50/P75/P90/P95)

GET  /api/v1/metrics/interview/summary
     Auth: ERP_ADMIN
     Query: ?tenant_id=&from=&to=&recruiter_id=&institute_id=
     Returns: attendance_rate, no_show_rate, dropout_rate, feedback_score_avg,
              reschedule_burden, candidate_satisfaction_avg

GET  /api/v1/metrics/job-funnel/summary
     Auth: ERP_ADMIN
     Query: ?tenant_id=&from=&to=&recruiter_id=&job_id=
     Returns: app_to_shortlist_rate, shortlist_to_offer_rate, offer_acceptance_rate,
              ghost_application_rate, ghost_offer_rate, total_applications

GET  /api/v1/metrics/dojo/summary
     Auth: ERP_ADMIN
     Query: ?tenant_id=&from=&to=&skill_id=
     Returns: match_completion_rate, dropout_rate, belt_conversion_rate,
              score_distribution, mentor_score_avg

GET  /api/v1/metrics/course/summary
     Auth: ERP_ADMIN
     Query: ?tenant_id=&from=&to=&trainer_id=&course_id=
     Returns: module_completion_rate, assessment_pass_rate, streak_health_avg,
              abandonment_rate, enrollment_count

GET  /api/v1/metrics/reputation/distribution
     Auth: ERP_ADMIN
     Query: ?tenant_id=&snapshot_date=&user_role=
     Returns: score_band_distribution (counts per band), avg_score, gate_block_rate,
              decay_event_count, anomaly_flag_count

GET  /api/v1/metrics/billing/summary
     Auth: ERP_ADMIN (finance scope)
     Query: ?tenant_id=&from=&to=
     Returns: payment_success_rate, overdue_rate, avg_payment_attempts,
              total_revenue, gst_total, subscription_active_count

GET  /api/v1/metrics/funnel/platform
     Auth: PLATFORM_ADMIN only
     Query: ?from=&to=&user_role=
     Returns: registration_to_profile, profile_to_application, application_to_offer,
              offer_to_acceptance, time_to_milestone_p50/p90

GET  /api/v1/metrics/public/skill-demand
     Auth: PUBLIC (no JWT required — R61)
     Query: ?skill=&region=&from=&to=
     Returns: Anonymized, aggregated, 7-day smoothed demand trend (no tenant data exposed)

GET  /api/v1/metrics/normalization/audit
     Auth: PLATFORM_ADMIN only
     Query: ?from=&to=&run_type=&status=
     Returns: pipeline run history, quarantine counts, formula versions active

POST /api/v1/metrics/normalization/quarantine/{event_id}/reprocess
     Auth: PLATFORM_ADMIN only
     Body: { reason: string }
     Returns: { reprocess_queued: boolean, pipeline_run_id }

GET  /api/v1/metrics/normalization/quarantine
     Auth: PLATFORM_ADMIN only
     Query: ?rejection_code=&from=&to=&reprocess_eligible=
     Returns: quarantined events list (no raw PII — UUIDs only)
```

---

## SECTION 10 — DEDUPLICATION STRATEGY (NON-NEGOTIABLE)

### 10.1 Three-Layer Deduplication (ALL LAYERS REQUIRED)

```
LAYER 1 — Redis Bloom Filter (at ingest, BEFORE ClickHouse write)
  Key:        bloomfilter:events:dedup (tenant-partitioned)
  Contains:   event_id hashes
  TTL:        72 hours (events older than 72h cannot be re-ingested anyway)
  False positive rate: 0.01% (acceptable — quarantine reviewed by human)
  Action:     Bloom hit → quarantine with DUPLICATE code + skip write

LAYER 2 — ClickHouse ReplacingMergeTree (at storage layer)
  Table:      raw_events_dedup_guard
  Engine:     ReplacingMergeTree()
  ORDER BY:   (tenant_id, event_id)
  Action:     Duplicate event_id writes are deduplicated by ClickHouse engine
              during background merge (eventual deduplication guarantee)

LAYER 3 — Pipeline Idempotency Guard (at aggregation stage)
  Method:     All Airflow aggregation tasks use REPLACE semantics on erp_daily_snapshots
              (ReplacingMergeTree ensures last-write-wins for same PK)
  Guard:      pipeline_run_id tracked — re-running a failed DAG overwrites partial results
              without creating double-counted aggregates
```

---

## SECTION 11 — SECURITY & COMPLIANCE (NON-OPTIONAL)

### 11.1 Data Security Rules (HARD LOCK — ALL REQUIRED)

```
SEC-01: No PII in ClickHouse — user_id is UUID hash only; no name, email, phone, address
SEC-02: All ClickHouse queries from API surface use parameterized statements (no f-strings)
SEC-03: tenant_id filter enforcement at API middleware — missing → 400 FORBIDDEN
SEC-04: PLATFORM_ADMIN JWT required for cross-tenant aggregations — verified at middleware
SEC-05: ClickHouse credentials stored in HashiCorp Vault (path: secret/ecoskiller/clickhouse)
SEC-06: Airflow DAG connections use Vault-injected env vars — no plaintext credentials in DAG code
SEC-07: Normalization quarantine table (PostgreSQL) has RLS: tenant_id isolation enforced
SEC-08: Public analytics API (R61) returns only aggregated + anonymized data
        Minimum group size: 10 entities before any metric is published
        (e.g., salary benchmark only shown if ≥ 10 offer records exist for that skill/region)
SEC-09: Grafana datasource uses read-only service account with query-time tenant restriction
SEC-10: Wazuh alert rule active on: abnormally large ClickHouse write batches (>500K rows/min)
SEC-11: All normalization_audit_log records are immutable (PostgreSQL RULE: no UPDATE, no DELETE)
SEC-12: GDPR/DPDP compliance: user_id UUID mapping table lives only in PostgreSQL (not ClickHouse)
        On right-to-forget: UUID is rotated in PostgreSQL — ClickHouse data becomes permanently
        un-linkable to the deleted user (pseudonymization via UUID rotation)
```

---

## SECTION 12 — OBSERVABILITY (NON-OPTIONAL)

### 12.1 Prometheus Metrics (ALL REQUIRED)

```
normalization_events_ingested_total{tenant, event_type, status}
normalization_events_quarantined_total{tenant, rejection_code}
normalization_events_clamped_total{tenant, metric_type}
normalization_pipeline_run_duration_seconds{run_type}
normalization_pipeline_failure_total{run_type, stage}
normalization_kafka_consumer_lag{topic, partition}
normalization_clickhouse_write_duration_seconds{table}
normalization_clickhouse_write_rows_total{table}
normalization_dedup_bloom_hit_total{tenant}
normalization_null_rate_total{metric, reason}        -- tracks NULL due to insufficient sample
erp_dashboard_query_duration_seconds{endpoint, tenant_tier}
erp_snapshot_freshness_seconds                        -- NOW() - MAX(computed_at) in daily snapshots
```

### 12.2 Grafana Dashboards (ALL REQUIRED)

```
Dashboard 1: Normalization Pipeline Health
  - Kafka consumer lag (target: < 5,000 events)
  - Events ingested vs quarantined rate (target: quarantine rate < 0.5%)
  - Pipeline run duration (target: micro-batch < 3 min)
  - ClickHouse write throughput (rows/sec)
  - Deduplication hit rate
  - NULL rate by metric type

Dashboard 2: ERP Data Freshness
  - Snapshot freshness per domain (target: < 10 min lag for micro-batch)
  - Daily finalization status (green/red per tenant)
  - Monthly aggregation status
  - Quarantine backlog size (reprocess_eligible count)

Dashboard 3: Metric Quality Monitor
  - Value clamping events by metric type (anomaly signal)
  - Out-of-range rejection rate by surface
  - Schema version mismatch rate
  - Stale event arrival rate (events > 1 hour old)

Dashboard 4: ERP Executive Summary (Platform-wide)
  - Platform funnel conversion trend (30-day rolling)
  - GD completion rate trend by tenant tier
  - Interview no-show trend
  - Offer acceptance rate trend
  - Billing payment success rate
```

### 12.3 Alert Rules (LOCKED)

```
ALERT: kafka_consumer_lag > 50000 events           → SEVERITY: HIGH (pipeline falling behind)
ALERT: normalization_pipeline_failure              → SEVERITY: CRITICAL (any run failure)
ALERT: quarantine_rate > 2% of ingested            → SEVERITY: HIGH (data quality issue)
ALERT: erp_snapshot_freshness_seconds > 900        → SEVERITY: HIGH (15 min stale for ERP)
ALERT: clickhouse_write_duration > 120s            → SEVERITY: MEDIUM (write slowness)
ALERT: daily_finalization_missed                   → SEVERITY: HIGH (missing daily snapshot)
ALERT: monthly_aggregation_missed                  → SEVERITY: CRITICAL (ERP monthly report blocked)
ALERT: normalization_null_rate > 30% for metric    → SEVERITY: MEDIUM (data sparsity warning)
ALERT: dedup_bloom_hit_rate > 5%                   → SEVERITY: MEDIUM (possible event replay)
```

---

## SECTION 13 — R61 PUBLIC ANALYTICS INTEGRATION (LOCKED)

### 13.1 Required ClickHouse Tables for R61 (ALL REQUIRED)

```sql
-- Skill Demand Stats (R61 — public, anonymized, 7-day smoothed)
CREATE TABLE r61_skill_demand_stats (
    skill_id                UUID,
    skill_name              String,           -- denormalized for public API
    region                  LowCardinality(String),
    stat_week               Date,             -- Monday of stat week
    job_posting_count       UInt64,
    application_volume      UInt64,
    avg_salary_inr          Decimal(12, 2),
    p50_salary_inr          Decimal(12, 2),
    p90_salary_inr          Decimal(12, 2),
    demand_velocity_7d      Float32,          -- 7-day smoothed posting growth rate
    computed_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(computed_at)
PARTITION BY toYYYYMM(stat_week)
ORDER BY (skill_id, region, stat_week)
TTL stat_week + INTERVAL 3 YEAR DELETE;

-- College/Institute Ranking Stats (R61 — public, anonymized)
CREATE TABLE r61_institution_performance_stats (
    institution_id          UUID,             -- hashed, not linkable without PLATFORM_ADMIN
    institution_tier        LowCardinality(String),  -- TIER_1 | TIER_2 | TIER_3 (no name)
    domain                  LowCardinality(String),
    region                  LowCardinality(String),
    stat_month              Date,
    placement_rate          Float32,
    avg_offer_package_inr   Decimal(12, 2),
    gd_completion_rate      Float32,
    recruiter_return_rate   Float32,
    rank_position           UInt32,           -- Ranking normalization engine output
    normalized_rank_score   Float32,          -- 0.0–1.0 (R61 ranking normalization)
    computed_at             DateTime64(3)
)
ENGINE = ReplacingMergeTree(computed_at)
PARTITION BY toYYYYMM(stat_month)
ORDER BY (domain, region, stat_month, rank_position)
TTL stat_month + INTERVAL 3 YEAR DELETE;
```

### 13.2 Ranking Normalization Engine (R61 — LOCKED FORMULA)

```
PURPOSE: Produce a fair, comparable, normalized rank score across institutions
         of different sizes, domains, and regions (R61 requirement).

FORMULA:
  normalized_rank_score(i) = (
      (placement_rate(i)           × 0.40)
    + (recruiter_return_rate(i)    × 0.20)
    + (gd_completion_rate(i)       × 0.15)
    + (avg_offer_package_norm(i)   × 0.25)    -- min-max normalized within region+tier
  )

  Where avg_offer_package_norm(i) = (
    (avg_offer_package(i) - MIN(avg_offer_package in region+tier))
    /
    (MAX(avg_offer_package in region+tier) - MIN(avg_offer_package in region+tier))
  )

MINIMUM SAMPLE GATE:
  Institution must have ≥ 20 placement events in the month to receive a rank score.
  Institutions below threshold → rank_position = NULL, normalized_rank_score = NULL.

DISPLAY RULE (API layer):
  Public API exposes rank_position and normalized_rank_score only.
  institution_id is hashed before public exposure.
  Minimum 10 institutions per region+tier required before ranking is published.
```

---

## SECTION 14 — DEPLOYMENT SPEC (KUBERNETES — LOCKED)

```yaml
# Namespace: analytics

# Service 1: Kafka Consumer + Normalization Worker
name: metric-normalization-worker
replicas: 3 (min) → 8 (max via HPA on Kafka consumer lag metric)
resources:
  requests: { cpu: 500m, memory: 512Mi }
  limits:   { cpu: 2000m, memory: 1Gi }

# Service 2: Metric API (FastAPI)
name: metric-normalization-api
replicas: 2 (min) → 5 (max via HPA on CPU)
resources:
  requests: { cpu: 250m, memory: 256Mi }
  limits:   { cpu: 1000m, memory: 512Mi }

# Airflow (existing analytics namespace deployment)
dags_to_register:
  - metric_normalization_micro_batch
  - metric_normalization_daily_finalization
  - metric_normalization_monthly

# ClickHouse (self-hosted, analytics namespace)
storage: Longhorn (replication factor: 3)
retention_enforced_by: TTL policies per table (defined in schemas above)

# ConfigMap keys (no secrets)
  KAFKA_BOOTSTRAP: kafka.core.svc.cluster.local:9092
  CLICKHOUSE_HOST: clickhouse.analytics.svc.cluster.local
  CLICKHOUSE_PORT: 8123
  REDIS_HOST: redis.core.svc.cluster.local
  POSTGRES_HOST: postgres.core.svc.cluster.local
  NORMALIZATION_FORMULA_VERSION: v1.0
  MICRO_BATCH_INTERVAL_MINUTES: 6
  DAILY_FINALIZATION_UTC_HOUR: 2
  MONTHLY_AGGREGATION_UTC_HOUR: 3
  MIN_SAMPLE_THRESHOLD: 5
  PUBLIC_ANALYTICS_MIN_GROUP_SIZE: 10

# Vault secret paths
  secret/ecoskiller/clickhouse-credentials
  secret/ecoskiller/kafka-credentials
  secret/ecoskiller/postgres-credentials
  secret/ecoskiller/redis-credentials
  secret/ecoskiller/airflow-fernet-key
```

---

## SECTION 15 — INTERN EXECUTION CHECKLIST (REQUIRED — ALL ITEMS MANDATORY)

```
☐ 1.  All 11 ClickHouse tables created with correct MergeTree engine, partition key, ORDER BY, TTL
☐ 2.  ReplacingMergeTree tables confirmed: raw_events_dedup_guard, gd_session_metrics, interview_metrics,
       dojo_match_metrics, course_participation_metrics, billing_metrics, erp_daily_snapshots
☐ 3.  normalization_quarantine PostgreSQL table created with correct indexes
☐ 4.  normalization_audit_log PostgreSQL table created with immutability RULE (no UPDATE, no DELETE)
☐ 5.  Redis Bloom Filter initialized with correct TTL (72 hours) and false-positive rate (0.01%)
☐ 6.  Kafka consumer group registered with manual commit strategy confirmed
☐ 7.  Dead letter queue topic created: clickhouse.normalization.dlq
☐ 8.  All 3 Airflow DAGs registered, tested on staging, verified with sample event payloads
☐ 9.  Stage 1–7 normalization pipeline unit tested: each stage tested independently with valid + invalid inputs
☐ 10. Rate-of-ratios violation test: any AVG(rate) query → test fails → pipeline stops (RULE-NR-01)
☐ 11. NULL rate logic: denominator < 5 → NULL returned (not 0.0) — confirmed via API response
☐ 12. Score band mapping applied correctly across all 5 bands — tested with boundary values (19.99, 20.0, 79.99, 80.0)
☐ 13. 7-day smoothing formula applied to all R61 Skill_Demand_Stats entries — tested with sparse data (some days NULL)
☐ 14. Cross-tenant isolation: API request without tenant_id → 400 response (middleware gate active)
☐ 15. PLATFORM_ADMIN cross-tenant query confirmed working
☐ 16. Public analytics API (R61 skill-demand): confirmed no tenant_id in response, group size gate active (≥10)
☐ 17. Ranking normalization engine tested: institutions with < 20 events return NULL rank
☐ 18. All 12 Prometheus metrics registered and scraped by Prometheus
☐ 19. All 4 Grafana dashboards created, imported, tested with live data
☐ 20. All 9 alert rules configured in Prometheus Alertmanager with correct routing
☐ 21. Vault secret injection confirmed for all 5 secret paths — no plaintext credentials in any config
☐ 22. GDPR UUID rotation tested: rotate user_id in PostgreSQL → ClickHouse records no longer linkable
☐ 23. ClickHouse parameterized query enforcement confirmed: string interpolation attempt → test fails
☐ 24. HPA on Kafka consumer lag metric active and tested (scale up at lag > 10,000)
☐ 25. Wazuh alert rule active on ClickHouse write batch anomaly (> 500K rows/min)
☐ 26. erp_snapshot_freshness alert tested: simulate pipeline stop → alert fires within 15 min
☐ 27. Monthly aggregation DAG tested on staging with 30 days of synthetic data
☐ 28. normalization_version binding tested: old records not affected by formula version bump
☐ 29. OpenAPI 3.1 schema generated and contract validator passes on all 11 API endpoints
☐ 30. Flyway migration files created for all PostgreSQL tables with versioned migration IDs

Absence of any checkbox → STOP EXECUTION → REPORT NORMALIZATION-AGENT-CHECKLIST-INCOMPLETE
```

---

## SECTION 16 — ANTIGRAVITY RUN COMMAND (REQUIRED)

```
ANTIGRAVITY_AGENT_SPEC      = CLICKHOUSE_METRIC_NORMALIZATION_AGENT
ANTIGRAVITY_VERSION         = v1.0.0
ANTIGRAVITY_MODE            = PRODUCTION
ANTIGRAVITY_STACK           = Python 3.11 | FastAPI | ClickHouse | Apache Airflow |
                              Kafka | Redis | PostgreSQL | HashiCorp Vault |
                              Prometheus | Grafana | Loki | Kubernetes (analytics namespace)

ANTIGRAVITY_OUTPUTS_REQUIRED =
  [1]  Kafka consumer service (Python) — all 35 topic subscriptions
  [2]  Stage 1–7 normalization pipeline (Python) — all stages, all rules
  [3]  All 11 ClickHouse CREATE TABLE statements (with TTL, engine, partition, ORDER BY)
  [4]  PostgreSQL migration files (Flyway) — quarantine + audit tables
  [5]  Redis Bloom Filter integration (idempotency guard)
  [6]  3 Airflow DAGs (micro_batch, daily_finalization, monthly)
  [7]  FastAPI metric API — all 11 endpoints with tenant isolation middleware
  [8]  Rate-of-ratios enforcement (RULE-NR-01) — validated in all aggregation queries
  [9]  Minimum sample threshold guard (RULE-NR-02) — NULL logic in all rate computations
  [10] Score band mapping (RULE-NR-03) — applied across all metric tables
  [11] 7-day smoothing (RULE-NR-04) — R61 public analytics tables
  [12] Ranking normalization engine (R61 formula, minimum sample gate)
  [13] All 12 Prometheus metrics registration
  [14] All 4 Grafana dashboard JSON exports
  [15] All 9 Alertmanager alert rules
  [16] Helm chart (analytics namespace, all 2 services + Airflow DAG registration)
  [17] Intern-executable deployment checklist (30 items)

ANTIGRAVITY_FORBIDDEN =
  ❌ AI/ML in normalization pipeline (R28 enforced — rule-based only)
  ❌ PII fields in any ClickHouse table
  ❌ AVG(rate) aggregations — only SUM(count)/SUM(count) permitted (RULE-NR-01)
  ❌ Cross-tenant queries without PLATFORM_ADMIN role
  ❌ DELETE or UPDATE in ClickHouse tables
  ❌ String interpolation in ClickHouse query construction
  ❌ Raw event direct write from frontend
  ❌ Formula modification without normalization_version bump
  ❌ Public R61 API exposing < 10 entity groups
  ❌ Skipping normalization_audit_log write on any pipeline run
  ❌ Silent event discard — all rejections must go to quarantine
  ❌ Creative reinterpretation of any locked formula in Section 6

READY_FOR = ANTIGRAVITY_PRODUCTION
✔ ANTIGRAVITY_SAFE
✔ ANTIGRAVITY COMPATIBLE
```

---

```
═══════════════════════════════════════════════════════════════════════════════
AGENT SEAL: CLICKHOUSE_METRIC_NORMALIZATION_AGENT v1.0.0
STATUS:     FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC
PLATFORM:   ECOSKILLER
ENGINE:     ANTIGRAVITY
MUTATION:   ADD-ONLY VIA VERSION BUMP
AUTHORITY:  HUMAN DECLARATION ONLY
RULE:       IDENTICAL INPUT → IDENTICAL NORMALIZED OUTPUT — ALWAYS
═══════════════════════════════════════════════════════════════════════════════
```
