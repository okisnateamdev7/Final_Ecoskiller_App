# PHONE_PARTICIPATION_REPUTATION_AGENT
## Ecoskiller Platform — Sealed & Locked Production Agent Specification
### Agent Class: Analytics & ERP Intelligence Layer
### Execution Engine: ANTIGRAVITY (Google)

---

```
ARTIFACT_CLASS        = PRODUCTION_AGENT_SPEC
AGENT_ID              = PHONE_PARTICIPATION_REPUTATION_AGENT
VERSION               = v1.0.0
STATUS                = FINAL · SEALED · LOCKED · GOVERNED
MUTATION_POLICY       = ADD-ONLY VIA VERSION BUMP
CREATIVE_INTERP       = FORBIDDEN
ASSUMPTION_FILLING    = FORBIDDEN
EXECUTION_ENGINE      = ANTIGRAVITY
DEFAULT_BEHAVIOR      = DENY
FAILURE_MODE          = STOP_EXECUTION → REPORT → NO_PARTIAL_OUTPUT
INTERPRETATION_AUTH   = NONE
EXECUTION_AUTH        = HUMAN DECLARATION ONLY
```

---

## ⚠️ ANTIGRAVITY BINDING DIRECTIVE

```
⚠️ Antigravity MUST NOT reinterpret this agent's architecture, scope, or workflows.
⚠️ Antigravity MUST NOT add fields, rename tables, or alter scoring formulas.
⚠️ Antigravity MUST NOT skip any section marked MANDATORY or NON-NEGOTIABLE.
⚠️ Antigravity MUST generate all outputs within the explicit module boundaries below.
⚠️ Antigravity MUST treat every STOP_EXECUTION directive as a hard halt — no partial output is permitted.
⚠️ Absence of any required component → STOP EXECUTION → REPORT INCOMPLETE → NO DEPLOYMENT CLAIM PERMITTED.
```

---

## SECTION 1 — AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

### 1.1 Agent Name
`PHONE_PARTICIPATION_REPUTATION_AGENT`

### 1.2 Platform Context
This agent operates inside the **Ecoskiller Unified SaaS Platform** — a multi-tenant enterprise system covering:
- Job Portal Engine
- Skill Development Engine
- Group Discussion (GD) Dojo Engine
- Project Execution Engine
- ERP Layer (Institutes, Corporates, SMEs, Recruiters)

### 1.3 Agent Role
This agent is the **single authority** responsible for:

1. **Phone Verification** — Enforcing verified phone identity at trust-sensitive events
2. **Participation Tracking** — Capturing structured behavioral participation signals across all platform surfaces
3. **Reputation Computation** — Computing deterministic, rule-based, auditable reputation scores per user role
4. **ERP Analytics Emission** — Feeding verified participation and reputation data into the ERP analytics layer
5. **Anti-Fraud Defense** — Detecting anomalous participation, fake phone registration, and score manipulation

### 1.4 Agent Classification
```
AGENT_TYPE            = Analytics Intelligence + ERP Data Emitter
SCOPE                 = Platform-Wide (All Tenants, All Roles)
DATA_CLASS            = Behavioral · Identity · Reputation · Compliance
AUDIT_CLASS           = Immutable
OUTPUT_CLASS          = ERP-consumable metrics + Scoring signals + Trust gates
```

---

## SECTION 2 — SCOPE BOUNDARIES (HARD LOCK)

### 2.1 In Scope (LOCKED — ANTIGRAVITY MUST IMPLEMENT ALL)

| Domain | Coverage |
|---|---|
| Phone Verification | OTP issuance, verification, re-verification triggers, phone change governance |
| GD Participation | Turn compliance, mic usage, silence, interrupt attempts, early exit, network drops |
| Job Application Participation | Application completeness, follow-through, no-show detection |
| Interview Participation | Slot lock, attendance, dropout, rescheduling pattern |
| Dojo Match Participation | Match join, scenario completion, dropout, rematch behavior |
| Course/Skill Participation | Enrollment, module completion, assessment submission |
| Reputation Score Computation | Weighted formula per role: Student, Trainer, Recruiter, Institute, SME |
| ERP Analytics Emission | ClickHouse write events for all participation and reputation signals |
| Trust Score Gateway | Block/gate actions based on reputation floor thresholds |
| Audit Trail | Immutable log of every reputation event and phone verification action |

### 2.2 Explicitly Out of Scope (FORBIDDEN — ANTIGRAVITY MUST NOT IMPLEMENT)

```
❌ Voice/audio content analysis
❌ Sentiment or tone detection
❌ Facial recognition or biometrics
❌ Personality trait inference
❌ Leadership or confidence scoring
❌ AI-subjective evaluation
❌ Recording or storing raw audio
❌ Cross-tenant data reads
❌ Manual override without audit trail
```

---

## SECTION 3 — SYSTEM ARCHITECTURE (NON-NEGOTIABLE)

### 3.1 Service Identity
```
Service Name:    phone-participation-reputation-service
Namespace:       core (Kubernetes)
Language:        Node.js (Spring Boot alternative permitted)
Protocol:        REST + Event Bus (Kafka)
State Store:     Redis (deterministic state machines)
Primary DB:      PostgreSQL (reputation records, audit logs)
Analytics Sink:  ClickHouse (ERP dashboards)
Auth Boundary:   Keycloak JWT (RBAC enforced)
Secret Store:    HashiCorp Vault
```

### 3.2 Architecture Principles (LOCKED)
```
1. Stateless service + stateful Redis state machines
2. All scoring is deterministic: identical input → identical output
3. No AI/ML in scoring — only rule-based weighted formulas
4. Every score mutation emits an immutable audit event
5. Tenant isolation enforced at DB (RLS on tenant_id)
6. Phone verification is a hard gate for trust-sensitive actions
7. Reputation score never decreases silently — every penalty is logged
8. ERP analytics feed is append-only (no deletion)
```

### 3.3 Traffic Classes
```
Class 1: HTTP REST APIs     → CRUD, verification triggers, score queries
Class 2: WebSocket events   → Real-time GD participation capture
Class 3: Kafka events       → Async reputation mutations, ERP writes
Class 4: Redis state        → GD session state, OTP timers, locks
```

---

## SECTION 4 — PHONE VERIFICATION ENGINE (MANDATORY)

### 4.1 Purpose
Phone verification is a **trust gate**. It is required before participation in:
- GD sessions (Voice Group Discussion)
- Interview slot booking
- Job offer acceptance
- Billing-linked actions
- Dispute filing
- Mentor certification

### 4.2 OTP Flow (DETERMINISTIC — NON-NEGOTIABLE)

```
STEP 1: User requests phone verification
        → Agent validates: phone format (E.164), not already verified, not rate-limited
        → Agent calls SMS Gateway (Jasmin SMS Gateway)
        → OTP generated: 6-digit, cryptographically random
        → OTP stored in Redis with TTL = 300 seconds
        → Attempt counter initialized in Redis: max_attempts = 3

STEP 2: User submits OTP
        → Agent fetches OTP from Redis
        → Compares hash (HMAC-SHA256) — never plaintext comparison
        → On match: phone_verified = TRUE, Redis key deleted, audit event emitted
        → On mismatch: attempt_counter++
        → On max_attempts exceeded: account flagged, cooldown = 900 seconds

STEP 3: Verified status stored in PostgreSQL (users.phone_verified = TRUE)
        → Kafka event: phone.verified emitted
        → Reputation event: PHONE_VERIFIED_SIGNAL (+score)
```

### 4.3 Re-verification Triggers (LOCKED — ALL REQUIRED)
```
Trigger                         Action
─────────────────────────────────────────────────────
Phone number change             Force re-verification before save
Suspicious login from new IP    Force phone OTP confirmation
Billing action above threshold  Phone OTP gate enforced
Account recovery flow           Phone OTP mandatory
GD session join (first time)    Phone verified check (gate)
Interview slot lock             Phone verified check (gate)
```

### 4.4 Phone Governance Rules (HARD LOCK)
```
RULE-PH-01: One verified phone per active account (no duplicates across tenants)
RULE-PH-02: Phone change requires current phone OTP + new phone OTP (both verified)
RULE-PH-03: Unverified phone accounts cannot join GD sessions — hard gate
RULE-PH-04: Unverified phone accounts cannot lock interview slots — hard gate
RULE-PH-05: VoIP / virtual numbers flagged for manual review — not auto-approved
RULE-PH-06: Phone verification status cannot be set by API — only by OTP flow
RULE-PH-07: Every OTP issuance logged with: timestamp, IP, device_fingerprint
RULE-PH-08: All verification events are immutable — no deletion permitted
```

### 4.5 PostgreSQL Schema — Phone Verification (LOCKED)

```sql
-- Phone Verification Log (IMMUTABLE — NO DELETE PERMITTED)
CREATE TABLE phone_verification_log (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    phone           TEXT NOT NULL,                          -- E.164 format
    event_type      TEXT NOT NULL,                          -- OTP_ISSUED | OTP_VERIFIED | OTP_FAILED | OTP_EXPIRED | PHONE_CHANGED | GATE_BLOCKED
    ip_address      TEXT,
    device_fp       TEXT,                                   -- device fingerprint hash
    attempt_count   INT DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Row-Level Security
ALTER TABLE phone_verification_log ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON phone_verification_log
    USING (tenant_id = current_setting('app.tenant_id')::UUID);

-- Indexes
CREATE INDEX idx_pvl_user ON phone_verification_log(user_id);
CREATE INDEX idx_pvl_phone ON phone_verification_log(phone);
CREATE INDEX idx_pvl_event ON phone_verification_log(event_type);
CREATE INDEX idx_pvl_created ON phone_verification_log(created_at);
```

---

## SECTION 5 — PARTICIPATION TRACKING ENGINE (MANDATORY)

### 5.1 Participation Surfaces (ALL REQUIRED — ANTIGRAVITY MUST IMPLEMENT ALL)

#### 5.1.A — GD (Group Discussion) Participation
```
Signal                      Capture Method          Storage
────────────────────────────────────────────────────────────
mic_open_duration_sec       WebSocket + Redis        PostgreSQL + ClickHouse
turns_completed             State machine            PostgreSQL + ClickHouse
turns_skipped               State machine            PostgreSQL + ClickHouse
interrupt_attempts          WebSocket event          PostgreSQL + ClickHouse
silence_during_token        Timer vs mic open        PostgreSQL + ClickHouse
network_drops               WebSocket disconnect     PostgreSQL + ClickHouse
early_exit                  Session state            PostgreSQL + ClickHouse
late_join (spectator flag)  Join timestamp check     PostgreSQL
rejoin_attempt_blocked      State machine            PostgreSQL
```

#### 5.1.B — Interview Participation
```
Signal                      Capture Method          Storage
────────────────────────────────────────────────────────────
slot_booked                 API event               PostgreSQL
slot_cancelled              API event               PostgreSQL
no_show                     Timeout + status check  PostgreSQL + ClickHouse
reschedule_count            Counter                 PostgreSQL
interview_completed         Lifecycle event         PostgreSQL + ClickHouse
dropout_midway              Session state           PostgreSQL + ClickHouse
feedback_submitted          API event               PostgreSQL
```

#### 5.1.C — Job Application Participation
```
Signal                      Capture Method          Storage
────────────────────────────────────────────────────────────
applications_submitted      API event               PostgreSQL + ClickHouse
incomplete_applications     Schema validation       PostgreSQL
ghost_applications          Offer issued, no resp.  PostgreSQL + ClickHouse
offer_accepted              Lifecycle event         PostgreSQL + ClickHouse
offer_declined              Lifecycle event         PostgreSQL
offer_ghosted               Timeout check           PostgreSQL + ClickHouse
```

#### 5.1.D — Dojo Match Participation
```
Signal                      Capture Method          Storage
────────────────────────────────────────────────────────────
match_joined                Session event           PostgreSQL + ClickHouse
scenario_completed          Match engine event      PostgreSQL + ClickHouse
dropout                     Session state           PostgreSQL + ClickHouse
rematch_requests            Counter                 PostgreSQL
belt_attempt_count          Belt engine             PostgreSQL
mentor_interaction          Match event             PostgreSQL
```

#### 5.1.E — Course & Skill Participation
```
Signal                      Capture Method          Storage
────────────────────────────────────────────────────────────
modules_completed           LMS event               PostgreSQL + ClickHouse
assessment_submitted        API event               PostgreSQL + ClickHouse
assessment_passed           Score engine            PostgreSQL + ClickHouse
streak_maintained           Daily activity log      Redis + PostgreSQL
enrollment_abandoned        Timeout check           PostgreSQL + ClickHouse
```

### 5.2 PostgreSQL Schema — Participation Events (LOCKED)

```sql
-- Participation Event Log (IMMUTABLE — APPEND-ONLY)
CREATE TABLE participation_events (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    session_id      UUID,                                   -- GD/Interview/Match/Course session
    surface         TEXT NOT NULL,                          -- GD | INTERVIEW | JOB_APP | DOJO | COURSE
    event_type      TEXT NOT NULL,                          -- per surface signal codes
    value_int       BIGINT,                                 -- numeric value (e.g. seconds)
    value_bool      BOOLEAN,                                -- boolean signal
    value_text      TEXT,                                   -- text annotation (e.g. exit reason)
    metadata        JSONB,                                  -- additional context (round, topic, etc.)
    recorded_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Row-Level Security
ALTER TABLE participation_events ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON participation_events
    USING (tenant_id = current_setting('app.tenant_id')::UUID);

-- Indexes
CREATE INDEX idx_pe_user ON participation_events(user_id);
CREATE INDEX idx_pe_session ON participation_events(session_id);
CREATE INDEX idx_pe_surface ON participation_events(surface);
CREATE INDEX idx_pe_event ON participation_events(event_type);
CREATE INDEX idx_pe_recorded ON participation_events(recorded_at);
```

---

## SECTION 6 — REPUTATION COMPUTATION ENGINE (SEALED — NON-NEGOTIABLE)

### 6.1 Core Reputation Principles (HARD LOCK)
```
PRINCIPLE-R-01: Reputation is always computed from verifiable behavioral signals only
PRINCIPLE-R-02: No AI inference, no personality scoring, no confidence metrics
PRINCIPLE-R-03: Formula is deterministic — same input = same output — always
PRINCIPLE-R-04: Every score mutation is logged immutably before the score updates
PRINCIPLE-R-05: Scores are reproducible and auditable — every component traceable
PRINCIPLE-R-06: Reputation gates enforce floor thresholds — no bypass permitted
PRINCIPLE-R-07: Score decay applies on inactivity — governed by decay schedule only
PRINCIPLE-R-08: Manual admin override of score requires L3 authority + audit entry
```

### 6.2 Reputation Score Formula — STUDENT (LOCKED)

```
STUDENT_REPUTATION_SCORE = (

  -- PARTICIPATION LAYER (40 points max)
    (gd_turns_completed_rate       * 10)   -- % of turns taken in all GD sessions
  + (gd_speak_time_utilization     * 8)    -- avg % of speaking token time used
  + (interview_attendance_rate     * 10)   -- % of booked interviews attended
  + (course_completion_rate        * 7)    -- % of enrolled courses completed
  + (dojo_match_completion_rate    * 5)    -- % of joined matches completed

  -- QUALITY LAYER (30 points max)
  + (assessment_pass_rate          * 10)   -- % of assessments passed
  + (peer_feedback_score           * 8)    -- avg peer score from GD/Dojo (0–10 scaled)
  + (streak_score                  * 7)    -- daily activity streak (capped at 7 days = max)
  + (application_quality_score     * 5)    -- profile completeness × application completeness

  -- COMPLIANCE LAYER (20 points max)
  + (phone_verified                * 5)    -- binary: verified = 5, unverified = 0
  + (no_ghost_penalty              * 5)    -- deduct 5 per ghost application/offer (floor = 0)
  + (no_interrupt_penalty          * 5)    -- deduct 2 per GD interrupt attempt (floor = 0)
  + (no_early_exit_penalty         * 5)    -- deduct 3 per early GD exit (floor = 0)

  -- TRUST LAYER (10 points max)
  + (account_age_score             * 3)    -- graduated: 0-30d=1, 31-90d=2, 90d+=3
  + (verified_badge_score          * 4)    -- identity verification tier (0–4)
  + (no_misconduct_score           * 3)    -- 3 if zero misconduct flags, else scaled down

)

FLOOR = 0
CEILING = 100
GRANULARITY = 2 decimal places
```

### 6.3 Reputation Score Formula — TRAINER / MENTOR (LOCKED)

```
TRAINER_REPUTATION_SCORE = (

  -- DELIVERY LAYER (35 points max)
    (student_feedback_avg          * 10)   -- avg student rating across all courses (0–10 → 0–10 pts)
  + (course_completion_rate        * 10)   -- % of students completing trainer's courses
  + (placement_success_rate        * 10)   -- % of students placed after trainer's course
  + (content_quality_rating        * 5)    -- admin-reviewed content quality (0–5)

  -- ENGAGEMENT LAYER (25 points max)
  + (dojo_sessions_hosted          * 8)    -- count of Dojo sessions conducted (capped)
  + (gd_mentor_participation       * 7)    -- GD evaluation sessions completed
  + (mentor_response_rate          * 5)    -- % of student queries responded to
  + (milestone_completion_rate     * 5)    -- % of assigned student milestones completed

  -- COMPLIANCE LAYER (25 points max)
  + (phone_verified                * 5)    -- binary gate
  + (identity_verified             * 10)   -- tiered: basic=5, govt-id=10
  + (complaint_ratio_penalty       * 10)   -- deduct 2 per sustained complaint (floor = 0)

  -- TRUST LAYER (15 points max)
  + (endorsement_score             * 8)    -- validated peer endorsements (capped at 4)
  + (tenure_score                  * 4)    -- account age on platform
  + (no_misconduct_score           * 3)    -- clean record

)

FLOOR = 0
CEILING = 100
```

### 6.4 Reputation Score Formula — RECRUITER (LOCKED)

```
RECRUITER_REPUTATION_SCORE = (

  -- RELIABILITY LAYER (40 points max)
    (offer_genuine_rate            * 15)   -- % of offers that were real and accepted
  + (no_ghost_offer_penalty        * 10)   -- deduct 5 per ghost offer (unaccepted, no reason)
  + (interview_conduct_score       * 10)   -- avg candidate post-interview rating of recruiter
  + (jd_accuracy_score             * 5)    -- how accurately JD reflected actual role

  -- ENGAGEMENT LAYER (30 points max)
  + (response_rate                 * 10)   -- % of applications responded to within SLA
  + (feedback_provided_rate        * 10)   -- % of rejected candidates given feedback
  + (gd_session_attendance         * 5)    -- attendance at scheduled GD batches
  + (posting_quality_score         * 5)    -- completeness of job postings

  -- COMPLIANCE LAYER (20 points max)
  + (company_verified              * 10)   -- domain + GST / company ID verified
  + (phone_verified                * 5)    -- binary gate
  + (billing_compliance            * 5)    -- no overdue billing disputes

  -- TRUST LAYER (10 points max)
  + (tenure_score                  * 4)
  + (no_misconduct_score           * 6)

)

FLOOR = 0
CEILING = 100
```

### 6.5 Reputation Score Formula — INSTITUTE (ERP LAYER — LOCKED)

```
INSTITUTE_REPUTATION_SCORE = (

  -- PLACEMENT LAYER (40 points max)
    (placement_rate                * 20)   -- % of eligible students placed via platform
  + (avg_package_score             * 10)   -- avg offer package vs industry benchmark
  + (recruiter_return_rate         * 10)   -- % of recruiters returning to campus

  -- PARTICIPATION LAYER (30 points max)
  + (gd_batch_completion_rate      * 10)   -- % of scheduled GD batches completed
  + (student_profile_completeness  * 10)   -- avg student profile completeness across institute
  + (tpo_engagement_score          * 10)   -- TPO activity, response, moderation actions

  -- COMPLIANCE LAYER (20 points max)
  + (domain_verified               * 10)   -- institute domain verified
  + (student_data_accuracy         * 5)    -- % of student records verified (CGPA, degree)
  + (billing_compliance            * 5)    -- subscription payment compliance

  -- TRUST LAYER (10 points max)
  + (audit_clean_score             * 5)
  + (no_misconduct_score           * 5)

)

FLOOR = 0
CEILING = 100
```

### 6.6 Score Decay Schedule (LOCKED — NON-NEGOTIABLE)

```
DECAY_TRIGGER         = Inactivity (no platform events in rolling window)
DECAY_SCHEDULE:
  30 days  inactive   → score × 0.98   (−2%)
  60 days  inactive   → score × 0.95   (−5% cumulative)
  90 days  inactive   → score × 0.90   (−10% cumulative)
  180 days inactive   → score × 0.75   (−25% cumulative, floor = 20)
  365 days inactive   → score FROZEN at 20 (floor enforced)

DECAY_EXCEPTIONS:
  - Verified identity freeze: score cannot decay below 30 if identity_verified = TRUE
  - Active subscription: decay halted while subscription is active
  - Admin grace period: L3 authority can freeze decay for 30 days max (audit required)

DECAY_AUDIT:
  Every decay event emits: user_id, old_score, new_score, decay_reason, applied_at
```

---

## SECTION 7 — REPUTATION GATE THRESHOLDS (HARD LOCK)

### 7.1 Action Gates by Score Floor

```
ACTION                              ROLE        MIN_REPUTATION    GATE_TYPE
────────────────────────────────────────────────────────────────────────────────
Join GD Session                     STUDENT     20                HARD (block)
Book Interview Slot                 STUDENT     25                HARD (block)
Apply to Job                        STUDENT     10                SOFT (warn)
Submit Dojo Belt Application        STUDENT     50                HARD (block)
Post Course Content                 TRAINER     40                HARD (block)
Access Recruiter Talent Pool        RECRUITER   30                HARD (block)
Post Job Listing                    RECRUITER   20                HARD (block)
Issue Offer Letter                  RECRUITER   40                HARD (block)
Run GD Batch (Institute)            INSTITUTE   30                HARD (block)
Access ERP Analytics Dashboard      INSTITUTE   25                HARD (block)
```

### 7.2 Gate Enforcement Rules
```
GATE-G-01: Hard gates cannot be bypassed — no exception without L3 override + audit
GATE-G-02: Soft gates warn user and log the event but do not block
GATE-G-03: Gate threshold changes require version bump + admin sign-off
GATE-G-04: Every gate check emits a Kafka event: reputation.gate.checked
GATE-G-05: Every gate block emits: reputation.gate.blocked (immutable log)
GATE-G-06: User must be shown: current score, threshold, gap, and path to unlock
```

---

## SECTION 8 — ERP ANALYTICS EMISSION (MANDATORY)

### 8.1 ClickHouse Sink Tables (ALL REQUIRED — ANTIGRAVITY MUST GENERATE ALL)

```sql
-- ERP: Phone Verification Analytics
CREATE TABLE erp_phone_verification_metrics (
    tenant_id           UUID,
    date                Date,
    total_otp_issued    UInt64,
    total_verified      UInt64,
    total_failed        UInt64,
    total_expired       UInt64,
    verification_rate   Float64,   -- verified / issued
    avg_attempts        Float64,
    gate_blocks         UInt64
) ENGINE = MergeTree()
ORDER BY (tenant_id, date);

-- ERP: Participation Analytics
CREATE TABLE erp_participation_metrics (
    tenant_id           UUID,
    user_id             UUID,
    surface             String,    -- GD | INTERVIEW | JOB_APP | DOJO | COURSE
    date                Date,
    sessions_joined     UInt64,
    sessions_completed  UInt64,
    dropout_count       UInt64,
    no_show_count       UInt64,
    total_mic_seconds   UInt64,    -- GD only
    turns_taken         UInt64,    -- GD only
    interrupt_attempts  UInt64,    -- GD only
    completion_rate     Float64
) ENGINE = MergeTree()
ORDER BY (tenant_id, surface, date);

-- ERP: Reputation Score Snapshots
CREATE TABLE erp_reputation_snapshots (
    tenant_id           UUID,
    user_id             UUID,
    user_role           String,    -- STUDENT | TRAINER | RECRUITER | INSTITUTE
    snapshot_date       Date,
    reputation_score    Float64,
    score_band          String,    -- CRITICAL(<20) | LOW(20-39) | MEDIUM(40-59) | HIGH(60-79) | ELITE(80-100)
    participation_sub   Float64,
    quality_sub         Float64,
    compliance_sub      Float64,
    trust_sub           Float64,
    gate_blocks_30d     UInt64,
    decay_applied       UInt8      -- 0 or 1
) ENGINE = MergeTree()
ORDER BY (tenant_id, user_role, snapshot_date);

-- ERP: Reputation Event Stream
CREATE TABLE erp_reputation_event_stream (
    event_id            UUID,
    tenant_id           UUID,
    user_id             UUID,
    event_type          String,    -- SCORE_INIT | SIGNAL_APPLIED | DECAY_APPLIED | GATE_BLOCKED | ADMIN_OVERRIDE
    signal_code         String,    -- e.g. GD_TURN_COMPLETED | PHONE_VERIFIED | GHOST_PENALTY
    delta_value         Float64,   -- score change (+/-)
    old_score           Float64,
    new_score           Float64,
    surface             String,
    session_id          UUID,
    applied_at          DateTime64(3)
) ENGINE = MergeTree()
ORDER BY (tenant_id, user_id, applied_at);
```

### 8.2 Kafka Events Emitted by This Agent (LOCKED — ALL REQUIRED)

```
Event Topic                         Payload Summary
──────────────────────────────────────────────────────────────────────────────
phone.otp.issued                    user_id, phone_hash, ip, device_fp, ts
phone.verified                      user_id, tenant_id, ts
phone.verification.failed           user_id, attempt_count, ts
phone.gate.blocked                  user_id, action_attempted, ts
participation.signal.captured       user_id, surface, event_type, value, ts
participation.session.completed     user_id, session_id, surface, ts
participation.dropout.detected      user_id, session_id, surface, reason, ts
reputation.score.initialized        user_id, role, initial_score, ts
reputation.signal.applied           user_id, signal_code, delta, new_score, ts
reputation.gate.checked             user_id, action, required_score, actual_score, ts
reputation.gate.blocked             user_id, action, gap, ts
reputation.decay.applied            user_id, old_score, new_score, decay_pct, ts
reputation.admin.override           user_id, admin_id, old_score, new_score, reason, ts
erp.analytics.snapshot.ready        tenant_id, snapshot_date, record_count, ts
```

---

## SECTION 9 — ANTI-FRAUD & ANOMALY DETECTION (MANDATORY)

### 9.1 Fraud Signals (ALL REQUIRED)

```
FRAUD-F-01: Multiple accounts sharing same verified phone
            → Flag both accounts, raise admin review event
FRAUD-F-02: OTP requests from VoIP / disposable number ranges
            → Block OTP issuance, log IP + device fingerprint
FRAUD-F-03: Participation signal manipulation (impossible values)
            → e.g. mic_open_duration > session_duration → INVALID → discard + flag
FRAUD-F-04: Rapid reputation gain (>30 points in 24 hours)
            → Anomaly flag, score frozen pending review
FRAUD-F-05: Same IP submitting participation events for multiple users in same session
            → Cross-session collusion flag, all users flagged
FRAUD-F-06: GD participation events arriving after session_end_timestamp
            → Invalid — discard + log
FRAUD-F-07: Reputation score reset attempts via API (not through legitimate formula)
            → DENY + ALERT + Kafka: security.reputation.tamper.attempt
FRAUD-F-08: Phone verification bypassed via direct DB write (detected via audit gap)
            → Integrity violation — immediate admin alert
```

### 9.2 Anomaly Response Protocol

```
SEVERITY    TRIGGER                             RESPONSE
────────────────────────────────────────────────────────────────────────
LOW         Suspicious but within plausible     Log + flag for review
            bounds
MEDIUM      Pattern matches known fraud         Soft hold on score mutation
            signatures                          + admin notification
HIGH        Clear integrity violation           Score frozen + gate all
                                                actions + admin alert emitted
CRITICAL    Multi-user collusion or DB bypass   Account suspended +
                                                Kafka: security.critical.event
                                                + L3 authority paged
```

---

## SECTION 10 — DATA MODEL LOCK (POSTGRESQL — COMPLETE)

### 10.1 Reputation Score Table (LOCKED)

```sql
CREATE TABLE reputation_scores (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL REFERENCES users(id),
    tenant_id           UUID NOT NULL REFERENCES tenants(id),
    user_role           TEXT NOT NULL,                   -- STUDENT | TRAINER | RECRUITER | INSTITUTE
    current_score       NUMERIC(5,2) NOT NULL DEFAULT 0,
    score_band          TEXT NOT NULL DEFAULT 'CRITICAL', -- CRITICAL|LOW|MEDIUM|HIGH|ELITE
    participation_sub   NUMERIC(5,2) DEFAULT 0,
    quality_sub         NUMERIC(5,2) DEFAULT 0,
    compliance_sub      NUMERIC(5,2) DEFAULT 0,
    trust_sub           NUMERIC(5,2) DEFAULT 0,
    phone_verified      BOOLEAN DEFAULT FALSE,
    last_activity_at    TIMESTAMPTZ,
    last_decay_at       TIMESTAMPTZ,
    decay_frozen        BOOLEAN DEFAULT FALSE,
    decay_freeze_until  TIMESTAMPTZ,
    anomaly_flag        BOOLEAN DEFAULT FALSE,
    anomaly_reason      TEXT,
    computed_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

ALTER TABLE reputation_scores ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON reputation_scores
    USING (tenant_id = current_setting('app.tenant_id')::UUID);

CREATE UNIQUE INDEX idx_rs_user_role ON reputation_scores(user_id, user_role);
CREATE INDEX idx_rs_tenant ON reputation_scores(tenant_id);
CREATE INDEX idx_rs_band ON reputation_scores(score_band);
CREATE INDEX idx_rs_anomaly ON reputation_scores(anomaly_flag) WHERE anomaly_flag = TRUE;
```

### 10.2 Reputation Audit Log (IMMUTABLE — NO DELETE, NO UPDATE)

```sql
CREATE TABLE reputation_audit_log (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    event_type      TEXT NOT NULL,
    signal_code     TEXT NOT NULL,
    surface         TEXT,
    session_id      UUID,
    old_score       NUMERIC(5,2),
    delta_value     NUMERIC(5,2),
    new_score       NUMERIC(5,2),
    applied_by      TEXT DEFAULT 'SYSTEM',              -- SYSTEM | admin_user_id
    override_reason TEXT,                               -- populated on admin override only
    metadata        JSONB,
    applied_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- IMMUTABILITY ENFORCEMENT
CREATE RULE no_update_reputation_audit AS ON UPDATE TO reputation_audit_log DO INSTEAD NOTHING;
CREATE RULE no_delete_reputation_audit AS ON DELETE TO reputation_audit_log DO INSTEAD NOTHING;

ALTER TABLE reputation_audit_log ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON reputation_audit_log
    USING (tenant_id = current_setting('app.tenant_id')::UUID);

CREATE INDEX idx_ral_user ON reputation_audit_log(user_id);
CREATE INDEX idx_ral_signal ON reputation_audit_log(signal_code);
CREATE INDEX idx_ral_applied ON reputation_audit_log(applied_at);
```

---

## SECTION 11 — API CONTRACTS (LOCKED — ALL REQUIRED)

### 11.1 Phone Verification APIs

```
POST   /api/v1/phone/request-otp
       Body: { phone: string (E.164) }
       Auth: JWT (any authenticated user)
       Returns: { otp_reference_id, expires_in_seconds: 300 }

POST   /api/v1/phone/verify-otp
       Body: { otp_reference_id: string, otp: string }
       Auth: JWT
       Returns: { verified: boolean, reputation_signal_applied: boolean }

GET    /api/v1/phone/status
       Auth: JWT
       Returns: { phone_verified: boolean, phone_masked: string, verified_at: timestamp }
```

### 11.2 Participation APIs

```
POST   /api/v1/participation/signal
       Body: { session_id, surface, event_type, value_int?, value_bool?, metadata? }
       Auth: JWT (internal service token acceptable)
       Returns: { received: true, event_id }

GET    /api/v1/participation/summary/{user_id}
       Auth: JWT (self | admin | ERP role)
       Query: ?surface=GD&from=date&to=date
       Returns: { surface, sessions_joined, completion_rate, key_signals[] }
```

### 11.3 Reputation APIs

```
GET    /api/v1/reputation/{user_id}
       Auth: JWT (self | recruiter | admin | ERP)
       Returns: { current_score, score_band, sub_scores{}, phone_verified, last_updated }

GET    /api/v1/reputation/{user_id}/audit
       Auth: JWT (self | admin only)
       Query: ?from=date&to=date&limit=50
       Returns: { events: [{ signal_code, delta, old_score, new_score, applied_at }] }

POST   /api/v1/reputation/gate/check
       Body: { user_id, action_code }
       Auth: JWT (internal service token)
       Returns: { allowed: boolean, current_score, required_score, gap }

POST   /api/v1/reputation/admin/override
       Body: { user_id, new_score, reason }
       Auth: JWT (L3 ADMIN ROLE ONLY)
       Returns: { applied: boolean, audit_event_id }
       Note: Emits Kafka: reputation.admin.override — IMMUTABLE LOG REQUIRED
```

### 11.4 ERP Analytics APIs

```
GET    /api/v1/erp/analytics/reputation/snapshot
       Auth: JWT (ERP_ADMIN | PLATFORM_ADMIN)
       Query: ?tenant_id=&date=&role=
       Returns: ClickHouse-backed score distribution, band breakdown, gate block counts

GET    /api/v1/erp/analytics/participation/summary
       Auth: JWT (ERP_ADMIN | PLATFORM_ADMIN | INSTITUTE_ADMIN)
       Query: ?tenant_id=&surface=&from=&to=
       Returns: Completion rates, dropout rates, no-show rates per surface

GET    /api/v1/erp/analytics/phone/metrics
       Auth: JWT (ERP_ADMIN | PLATFORM_ADMIN)
       Query: ?tenant_id=&from=&to=
       Returns: Verification rates, failure patterns, gate block counts
```

---

## SECTION 12 — REDIS STATE MACHINES (LOCKED)

### 12.1 OTP State Machine

```
Key Pattern:   otp:{user_id}:{otp_reference_id}
TTL:           300 seconds
Fields:
  otp_hash     HMAC-SHA256 of OTP code
  phone_hash   SHA-256 of target phone
  attempts     integer (max 3)
  issued_at    epoch millis

State Transitions:
  INITIAL → PENDING (OTP issued)
  PENDING → VERIFIED (correct OTP, attempts < 3)
  PENDING → FAILED (incorrect OTP, attempts < 3)
  PENDING → LOCKED (attempts = 3)
  PENDING → EXPIRED (TTL elapsed)
  Any terminal state → key deleted from Redis
```

### 12.2 Reputation Score Signal Queue

```
Key Pattern:   reputation:queue:{user_id}
Type:          Redis List (LPUSH/RPOP)
TTL:           None (processed within 5 seconds SLA)
Entry Format:  { signal_code, delta, surface, session_id, ts }

Processing:
  Worker polls RPOP on queue
  Applies signal to current score (atomic Redis + PostgreSQL transaction)
  Emits Kafka event
  Writes to ClickHouse (async)
  Clears queue entry
```

---

## SECTION 13 — OBSERVABILITY (NON-OPTIONAL)

### 13.1 Prometheus Metrics (ALL REQUIRED)

```
phone_otp_issued_total{tenant, status}
phone_otp_verified_total{tenant}
phone_otp_failed_total{tenant, reason}
phone_gate_blocked_total{tenant, action}
participation_signal_captured_total{tenant, surface, event_type}
participation_dropout_total{tenant, surface}
reputation_signal_applied_total{tenant, role, signal_code}
reputation_gate_blocked_total{tenant, action}
reputation_decay_applied_total{tenant, role}
reputation_anomaly_flagged_total{tenant}
reputation_admin_override_total{tenant}
erp_analytics_snapshot_written_total{tenant}
```

### 13.2 Grafana Dashboards (ALL REQUIRED)

```
Dashboard 1: Phone Verification Health
  - OTP issuance rate (per hour)
  - Verification success rate (target: >85%)
  - Gate block rate by action
  - VoIP detection blocks

Dashboard 2: Participation Health
  - GD completion rate by tenant
  - Interview no-show rate by tenant
  - Dropout rate by surface
  - Average mic utilization % (GD)

Dashboard 3: Reputation Score Distribution
  - Score band distribution (CRITICAL/LOW/MEDIUM/HIGH/ELITE) by role
  - Score decay events per day
  - Gate block counts by action
  - Anomaly flags active

Dashboard 4: ERP Executive Summary
  - Tenant-level participation health score
  - Top 10 / bottom 10 institutes by reputation
  - Recruiter reliability index
  - Platform-wide trust score trend
```

### 13.3 Alert Rules (LOCKED — PAGERDUTY / OPSGENIE TARGETS)

```
ALERT: phone_verification_rate < 70%         → SEVERITY: HIGH
ALERT: reputation_anomaly_flagged > 50/hour  → SEVERITY: CRITICAL
ALERT: erp_snapshot_missed_2_hours           → SEVERITY: HIGH
ALERT: gate_block_rate > 30% on GD_JOIN      → SEVERITY: MEDIUM
ALERT: reputation_admin_override > 10/day    → SEVERITY: HIGH (unusual activity)
ALERT: otp_failure_rate > 40%                → SEVERITY: HIGH (possible attack)
```

---

## SECTION 14 — SECURITY BASELINE (NON-OPTIONAL)

```
SEC-01: All API routes require JWT (Keycloak-issued, RS256)
SEC-02: RBAC enforced via Open Policy Agent — no hardcoded role checks in code
SEC-03: Phone numbers stored as HMAC-SHA256 hashes in logs — never plaintext
SEC-04: OTP codes stored as HMAC-SHA256 — never reversible
SEC-05: All ClickHouse writes via internal service account — no direct external access
SEC-06: Reputation audit log protected by DB-level RULE (no UPDATE, no DELETE)
SEC-07: Admin override endpoint requires L3 role + MFA-confirmed session token
SEC-08: All Kafka event payloads exclude raw PII — use hashed or masked identifiers
SEC-09: Rate limiting: POST /phone/request-otp → 3 requests per 15 minutes per user
SEC-10: WAF rule active on /reputation/admin/override route (Wazuh + ModSecurity)
SEC-11: Tenant isolation enforced via PostgreSQL RLS (tenant_id on all tables)
SEC-12: Vault stores: SMS gateway API key, OTP HMAC secret, Kafka credentials
```

---

## SECTION 15 — DEPLOYMENT SPEC (KUBERNETES — LOCKED)

```yaml
# Namespace
namespace: core

# Deployment
name: phone-participation-reputation-service
replicas: 2 (min) → 5 (max via HPA)
resources:
  requests: { cpu: 250m, memory: 256Mi }
  limits:   { cpu: 1000m, memory: 512Mi }

# HPA
metric: CPU utilization > 70%
min: 2 | max: 5

# Dependencies (all must be healthy for readiness probe to pass)
  - PostgreSQL (core schema)
  - Redis (state + OTP)
  - Kafka (event bus)
  - ClickHouse (analytics sink)
  - Keycloak (JWT validation)
  - Vault (secrets)

# ConfigMap keys (no secrets in ConfigMap)
  POSTGRES_SCHEMA: ecoskiller_core
  REDIS_DB: 2
  KAFKA_TOPIC_PREFIX: ecoskiller
  CLICKHOUSE_DB: erp_analytics
  OTP_TTL_SECONDS: 300
  OTP_MAX_ATTEMPTS: 3
  REPUTATION_SNAPSHOT_INTERVAL_HOURS: 6

# Vault secret paths
  secret/ecoskiller/sms-gateway-key
  secret/ecoskiller/otp-hmac-secret
  secret/ecoskiller/kafka-credentials
  secret/ecoskiller/clickhouse-credentials
  secret/ecoskiller/postgres-credentials
```

---

## SECTION 16 — INTERN EXECUTION CHECKLIST (REQUIRED)

```
☐ 1. All PostgreSQL tables created with RLS enabled and tenant_id policy enforced
☐ 2. Immutability RULE applied to reputation_audit_log (no UPDATE, no DELETE)
☐ 3. Redis OTP state machine tested: issue → verify → expire → lock flow
☐ 4. All Kafka event topics created with correct partition count
☐ 5. ClickHouse sink tables created and tested with sample data
☐ 6. All 14 Prometheus metrics exposed and scraped by Prometheus
☐ 7. All 4 Grafana dashboards created and tested
☐ 8. All 6 alert rules configured and routed
☐ 9. OTP HMAC-SHA256 hashing implemented — no plaintext OTP storage
☐ 10. Phone number masking confirmed in all API responses and log entries
☐ 11. Admin override endpoint tested: requires L3 role, emits audit event
☐ 12. Gate enforcement tested: hard gate blocks confirmed for all 9 action types
☐ 13. Fraud detection rules F-01 through F-08 implemented and tested
☐ 14. Decay schedule implemented and tested with simulated 30/60/90/180/365d gaps
☐ 15. ERP snapshot Airflow DAG created, tested, scheduled at 6-hour intervals
☐ 16. Vault secret injection confirmed (no secrets in env files)
☐ 17. All 13 Kafka topics auto-created on first publish (confirm broker config)
☐ 18. HPA configured and tested under load simulation
☐ 19. Wazuh alert rule active on /reputation/admin/override route
☐ 20. Contract validator passes on all API endpoints (OpenAPI 3.1 schema)

Absence of any checkbox → STOP EXECUTION → REPORT CHECKLIST INCOMPLETE
```

---

## SECTION 17 — ANTIGRAVITY RUN COMMAND (REQUIRED)

```
ANTIGRAVITY_AGENT_SPEC   = PHONE_PARTICIPATION_REPUTATION_AGENT
ANTIGRAVITY_VERSION      = v1.0.0
ANTIGRAVITY_MODE         = PRODUCTION
ANTIGRAVITY_STACK        = Node.js | PostgreSQL | Redis | Kafka | ClickHouse | Keycloak | Vault
ANTIGRAVITY_NAMESPACE    = core
ANTIGRAVITY_OUTPUTS      =
  [1] Full service implementation (Node.js)
  [2] PostgreSQL migration files (Flyway)
  [3] Redis state machine implementation
  [4] Kafka producer/consumer implementation
  [5] ClickHouse sink writer
  [6] All API route handlers (OpenAPI 3.1 compliant)
  [7] Prometheus metrics registration
  [8] Grafana dashboard JSON exports
  [9] Helm chart (core namespace)
  [10] Intern-executable deployment checklist

ANTIGRAVITY_FORBIDDEN =
  - No AI/ML scoring
  - No audio capture
  - No plaintext OTP or phone storage
  - No cross-tenant reads
  - No mutation of reputation_audit_log
  - No creative interpretation of scoring formulas
  - No skipping of fraud detection rules
  - No skipping of ERP analytics emission

READY_FOR = ANTIGRAVITY_PRODUCTION
✔ ANTIGRAVITY_SAFE
✔ ANTIGRAVITY COMPATIBLE
```

---

```
═══════════════════════════════════════════════════════════════════
AGENT SEAL: PHONE_PARTICIPATION_REPUTATION_AGENT v1.0.0
STATUS: FINAL · LOCKED · GOVERNED · DETERMINISTIC
PLATFORM: ECOSKILLER
ENGINE: ANTIGRAVITY
MUTATION: ADD-ONLY VIA VERSION BUMP
AUTHORITY: HUMAN DECLARATION ONLY
═══════════════════════════════════════════════════════════════════
```
