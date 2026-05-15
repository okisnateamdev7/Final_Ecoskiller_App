# 🔒 REPUTATION_AGENT.md
## SEALED & LOCKED AGENT PROMPT — ECOSKILLER PLATFORM
### EXECUTION_ENGINE = ANTIGRAVITY
### AGENT_CLASS = REPUTATION_TRUST_INTELLIGENCE

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║              🔐 ANTIGRAVITY EXECUTION SEAL — REPUTATION AGENT               ║
║                                                                              ║
║  EXECUTION_MODE         = LOCKED                                             ║
║  MUTATION_POLICY        = ADD_ONLY                                           ║
║  CREATIVE_INTERPRETATION= FORBIDDEN                                          ║
║  ASSUMPTION_FILLING     = FORBIDDEN                                          ║
║  DEFAULT_BEHAVIOR       = DENY                                               ║
║  FAILURE_MODE           = STOP_EXECUTION                                     ║
║  AGENT_TYPE             = REPUTATION_INTELLIGENCE_AGENT                     ║
║  AGENT_ID               = RA-001                                             ║
║  PLATFORM               = ECOSKILLER + DOJO ENGINE                          ║
║  STAGE_ALIGNMENT        = STAGE_2_INTELLIGENCE + STAGE_4_COMPLIANCE         ║
║  AI_ROLE                = ADVISE_ONLY — NEVER APPROVE / BLOCK / OVERRIDE    ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## ⚠️ AGENT AUTHORITY DECLARATION

This agent operates **exclusively** within the Ecoskiller multi-tenant enterprise SaaS platform under Antigravity's execution engine. It is a **non-human automation agent** governed by the platform's RBAC + ABAC authorization framework.

```
AGENT_SCOPE           = REPUTATION_TRUST_LOOP
AGENT_AUTHORITY       = READ + SCORE + ALERT + ADVISE
AGENT_PROHIBITION     = APPROVE | BLOCK | OVERRIDE | MUTATE_USER_DATA
TENANT_ISOLATION      = HARD
DOMAIN_ISOLATION      = HARD
COMPLIANCE_INHERITANCE= FULL (Auth · MFA · Session · Encryption · Audit)
```

> **⛔ ANY DEVIATION FROM THE ABOVE = STOP EXECUTION**

---

## 1️⃣ AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

### What This Agent Does
The `REPUTATION_AGENT` is a continuous intelligence subsystem responsible for:

- **Computing, updating, and propagating** reputation scores across all user roles
- **Detecting fraudulent, anomalous, or coordinated reputational attacks**
- **Surfacing trust signals** to the platform, recruiters, institutes, and end users
- **Triggering compliance escalations** when reputational thresholds are breached
- **Generating endorsement graphs** to model trust relationships across the ecosystem

### What This Agent Is NOT Allowed To Do
```
❌ Approve job applications based on reputation score alone
❌ Block any user without human governance review
❌ Override recruiter, trainer, or admin decisions
❌ Share reputation data across tenant boundaries
❌ Expose raw scoring internals to non-admin roles
❌ Generate or publish success stories autonomously (requires R80 pipeline)
❌ Merge domain reputations (Arts ≠ Commerce ≠ Science ≠ Technology ≠ Administration)
```

---

## 2️⃣ DOMAIN & TENANT ISOLATION (HARD LOCK)

```
RULE: Reputation scores are DOMAIN-SCOPED + TENANT-SCOPED
CROSS_DOMAIN_REPUTATION_TRANSFER = FORBIDDEN
CROSS_TENANT_REPUTATION_TRANSFER = FORBIDDEN
DOMAIN_TRACKS = Arts | Commerce | Science | Technology | Administration

Institute reputation ≠ Company reputation ≠ Platform reputation
Dojo reputation incidents ≠ Ecoskiller brand reputation
```

Each domain maintains an **independent reputation ledger**. A trainer's Technology domain score MUST NOT influence their Arts domain score.

---

## 3️⃣ USER ECOSYSTEM COVERAGE (ALL ROLES — STRICTLY ENFORCED)

The agent computes and maintains reputation records for the following user groups:

| Role | Reputation Scope |
|---|---|
| `STUDENT` | Academic integrity, Dojo match behavior, skill progression, peer rating |
| `TRAINER / MENTOR` | Course quality, placement success, student feedback, complaint ratio |
| `EVALUATOR` | Evaluation consistency, bias detection, timeliness |
| `INSTITUTE` | Student outcome quality, TPO reliability, compliance adherence |
| `ENTERPRISE / SME` | Job offer reliability, recruiter fairness, offer retraction rate |
| `RECRUITER / HR` | Interview conduct, candidate satisfaction rating, ghosting score |
| `ADMIN` | Audit compliance, governance response time |
| `PARENT` | READ-ONLY visibility. No reputation score generated. |
| `AUTOMATION / AI AGENTS` | This agent itself is logged, scored for accuracy, and auditable |

---

## 4️⃣ DATABASE SCHEMAS (MANDATORY — ABSENCE → STOP EXECUTION)

### 4.1 Core Reputation Tables

```sql
-- Primary reputation ledger
CREATE TABLE reputation_scores (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  entity_id           UUID NOT NULL,        -- user, institute, company
  entity_type         TEXT NOT NULL,        -- STUDENT | TRAINER | RECRUITER | INSTITUTE | ENTERPRISE
  domain_track        TEXT NOT NULL,        -- Arts | Commerce | Science | Technology | Administration
  score               DECIMAL(5,2) NOT NULL CHECK (score >= 0 AND score <= 100),
  tier                TEXT NOT NULL,        -- BRONZE | SILVER | GOLD | PLATINUM | ELITE
  computed_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version             INTEGER NOT NULL DEFAULT 1,
  is_visible          BOOLEAN DEFAULT TRUE,
  UNIQUE (tenant_id, entity_id, entity_type, domain_track)
);

-- Individual reputation events that feed score
CREATE TABLE reputation_events (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  entity_id           UUID NOT NULL,
  entity_type         TEXT NOT NULL,
  domain_track        TEXT NOT NULL,
  event_type          TEXT NOT NULL,        -- ENDORSEMENT | COMPLAINT | MILESTONE | FRAUD_FLAG | PEER_RATING | JOB_OUTCOME
  event_source        TEXT NOT NULL,        -- who triggered it
  weight              DECIMAL(4,3) NOT NULL,
  delta               DECIMAL(5,2) NOT NULL, -- positive or negative change
  metadata            JSONB,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_reversed         BOOLEAN DEFAULT FALSE,
  reversal_reason     TEXT
);

-- Endorsement graph
CREATE TABLE endorsement_graph (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  endorser_id         UUID NOT NULL,
  endorsed_id         UUID NOT NULL,
  domain_track        TEXT NOT NULL,
  skill_area          TEXT,
  strength            INTEGER CHECK (strength BETWEEN 1 AND 5),
  validated           BOOLEAN DEFAULT FALSE,
  validation_method   TEXT,               -- AI_CHECKED | ADMIN_VERIFIED | AUTO
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at          TIMESTAMPTZ,
  UNIQUE (tenant_id, endorser_id, endorsed_id, domain_track, skill_area)
);

-- Fraud and anomaly flags
CREATE TABLE fraud_flags (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  entity_id           UUID NOT NULL,
  flag_type           TEXT NOT NULL,       -- COORDINATED_BOOST | FAKE_ENDORSEMENT | REVIEW_BOMB | IMPERSONATION | ANOMALOUS_SPIKE
  severity            TEXT NOT NULL,       -- LOW | MEDIUM | HIGH | CRITICAL
  detected_by         TEXT NOT NULL,       -- REPUTATION_AGENT | ADMIN | USER_REPORT
  evidence            JSONB NOT NULL,
  status              TEXT DEFAULT 'OPEN', -- OPEN | UNDER_REVIEW | RESOLVED | DISMISSED
  assigned_to         UUID,
  resolved_at         TIMESTAMPTZ,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Trainer-specific reputation
CREATE TABLE trainer_reputation_scores (
  id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id               UUID NOT NULL,
  trainer_id              UUID NOT NULL,
  domain_track            TEXT NOT NULL,
  student_feedback_avg    DECIMAL(3,2),    -- 0–5 star avg
  course_completion_rate  DECIMAL(5,2),    -- %
  placement_success_rate  DECIMAL(5,2),    -- %
  content_quality_rating  DECIMAL(3,2),   -- AI + human assessed
  complaint_ratio_penalty DECIMAL(4,3),   -- subtracted from score
  influence_rank          INTEGER,         -- percentile among trainers in domain
  composite_score         DECIMAL(5,2),
  computed_at             TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE (tenant_id, trainer_id, domain_track)
);

-- Student reputation
CREATE TABLE student_reputation_scores (
  id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id               UUID NOT NULL,
  student_id              UUID NOT NULL,
  domain_track            TEXT NOT NULL,
  dojo_match_conduct      DECIMAL(3,2),   -- peer rating after Dojo matches
  skill_growth_rate       DECIMAL(5,2),
  peer_endorsement_count  INTEGER DEFAULT 0,
  integrity_score         DECIMAL(5,2),   -- based on anti-cheat data
  portfolio_quality       DECIMAL(3,2),
  composite_score         DECIMAL(5,2),
  computed_at             TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE (tenant_id, student_id, domain_track)
);

-- Recruiter / enterprise reputation
CREATE TABLE recruiter_reputation_scores (
  id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id                   UUID NOT NULL,
  recruiter_id                UUID NOT NULL,
  interview_satisfaction_avg  DECIMAL(3,2),
  offer_retraction_rate       DECIMAL(5,2),
  ghosting_rate               DECIMAL(5,2),
  sme_reliability_score       DECIMAL(5,2),
  compliance_score            DECIMAL(5,2),
  composite_score             DECIMAL(5,2),
  computed_at                 TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE (tenant_id, recruiter_id)
);

-- Reputation decay log
CREATE TABLE reputation_decay_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id       UUID NOT NULL,
  entity_id       UUID NOT NULL,
  entity_type     TEXT NOT NULL,
  decay_delta     DECIMAL(5,2) NOT NULL,
  decay_reason    TEXT NOT NULL,
  applied_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Audit trail (append-only, immutable)
CREATE TABLE reputation_audit_log (
  id              BIGSERIAL PRIMARY KEY,
  tenant_id       UUID NOT NULL,
  entity_id       UUID NOT NULL,
  action          TEXT NOT NULL,
  actor           UUID,                   -- agent UUID or admin UUID
  before_score    DECIMAL(5,2),
  after_score     DECIMAL(5,2),
  justification   TEXT,
  logged_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- NO DELETE, NO UPDATE PERMITTED ON THIS TABLE
```

---

## 5️⃣ SCORING ALGORITHMS (DETERMINISTIC — NO CREATIVE DEVIATION)

### 5.1 Trainer Reputation Score Formula

```
TrainerReputationScore = (
  (student_feedback_avg         × 0.30) +
  (course_completion_rate       × 0.20) +
  (placement_success_rate       × 0.25) +
  (content_quality_rating       × 0.15) +
  (complaint_ratio_penalty      × -0.10)   ← SUBTRACTED
) × domain_weight_multiplier

domain_weight_multiplier = 1.0 (default, configurable per tenant)

InfluenceRank = PERCENTILE_RANK(composite_score) OVER (PARTITION BY tenant_id, domain_track)
```

### 5.2 Student Reputation Score Formula

```
StudentReputationScore = (
  (dojo_match_conduct            × 0.25) +
  (skill_growth_rate             × 0.20) +
  (peer_endorsement_count_norm   × 0.20) +
  (integrity_score               × 0.25) +
  (portfolio_quality             × 0.10)
)

peer_endorsement_count_norm = MIN(peer_endorsement_count / 50, 1.0) × 100
```

### 5.3 Recruiter Reliability Score Formula

```
RecruiterReputationScore = (
  (interview_satisfaction_avg   × 0.30) +
  (100 - offer_retraction_rate  × 0.25) +
  (100 - ghosting_rate          × 0.25) +
  (sme_reliability_score        × 0.10) +
  (compliance_score             × 0.10)
)
```

### 5.4 Reputation Tier Thresholds (All Roles)

```
Score 0–39   → BRONZE
Score 40–59  → SILVER
Score 60–74  → GOLD
Score 75–89  → PLATINUM
Score 90–100 → ELITE
```

### 5.5 Reputation Decay Rules

```
DECAY_TRIGGER = 30 days of inactivity
DECAY_RATE    = -0.5 points per inactive week (capped at -15 total)
DECAY_EXEMPT  = ELITE tier entities (score ≥ 90)
DECAY_FLOOR   = entity cannot drop below 10 from decay alone
```

### 5.6 Graph-Based Trust Propagation

```
Algorithm: Weighted PageRank variant on endorsement_graph
Direction: endorser → endorsed (directed)
Weight: endorsement.strength × endorsement.validated (1 | 0.5 if unvalidated)
Dampening_factor: 0.85
Max_iterations: 100
Convergence_threshold: 0.0001

Trust_propagation operates WITHIN domain_track boundaries ONLY
Cross-domain propagation = FORBIDDEN
```

### 5.7 Anomaly Detection Rules

```
Trigger FRAUD_FLAG if:
  - Endorsement count spikes > 3σ in 24h
  - Score increases > 20 points in 48h without corresponding events
  - Endorsements from > 80% newly created accounts
  - IP cluster concentration > 70% in endorsement batch
  - Mutual endorsement loops (A→B→A) with no prior interaction history
  - Review-bomb pattern: ≥10 low ratings from same tenant cohort in <6h
```

---

## 6️⃣ BACKEND SERVICES (MANDATORY — ABSENCE → STOP EXECUTION)

### 6.1 Service Architecture

```
REPUTATION_AGENT (RA-001)
├── ReputationScoreEngine         → Computes all composite scores
├── EndorsementGraphBuilder       → Builds + validates directed trust graph
├── FraudScoreDetector            → ML-based anomaly & fraud detection
├── ReputationDecayScheduler      → Cron-driven decay engine (30d cycle)
├── ReputationEventIngester       → Kafka consumer for upstream activity events
├── TrustPropagationEngine        → Graph-based trust score amplifier
├── ReputationAlertPublisher      → Kafka producer for compliance alerts
└── ReputationAuditWriter         → Append-only audit trail committer
```

### 6.2 Event Ingestion (Kafka Topics — Consumed)

```
Topic                             | Source Service         | Triggers
---------------------------------|------------------------|------------------------
ecoskiller.dojo.match.completed  | Dojo Engine            | Student conduct scoring
ecoskiller.job.offer.retracted   | Job Portal Engine      | Recruiter penalty
ecoskiller.interview.rated       | Interview Service      | Recruiter satisfaction
ecoskiller.course.completed      | Skill Dev Engine       | Trainer completion rate
ecoskiller.placement.confirmed   | ERP Layer              | Trainer placement score
ecoskiller.complaint.filed       | Governance Service     | Complaint ratio update
ecoskiller.endorsement.created   | User Service           | Endorsement graph update
ecoskiller.anti_cheat.violation  | Dojo Anti-Cheat        | Integrity score penalty
ecoskiller.bug_report.validated  | Admin Governance       | Positive reputation event
```

### 6.3 Event Publication (Kafka Topics — Produced)

```
Topic                                    | Consumer                  | Purpose
----------------------------------------|---------------------------|---------------------------
ecoskiller.reputation.score.updated     | Notification Service      | Inform user of score change
ecoskiller.reputation.fraud.detected    | Admin Governance Service  | Trigger human review
ecoskiller.reputation.threshold.crossed | Compliance Engine         | Escalation workflow
ecoskiller.reputation.tier.changed      | Badge Service             | Badge assignment trigger
```

---

## 7️⃣ API CONTRACTS (SEALED — NO CREATIVE DEVIATION)

### Reputation Read APIs

```
GET  /api/v1/reputation/{entity_type}/{entity_id}
     → Returns: score, tier, domain_track, computed_at
     → Auth: RBAC — role-scoped visibility (see §10)
     → Tenant isolation: ENFORCED via JWT claims

GET  /api/v1/reputation/{entity_type}/{entity_id}/history
     → Returns: paginated reputation_events[]
     → Max page size: 50

GET  /api/v1/reputation/{entity_type}/{entity_id}/endorsements
     → Returns: endorsement_graph nodes + strength

GET  /api/v1/reputation/leaderboard
     → Params: domain_track, entity_type, limit (max 100)
     → Tenant-scoped only

GET  /api/v1/reputation/trainer/{trainer_id}
     → Returns: trainer_reputation_scores + influence_rank

GET  /api/v1/reputation/fraud-alerts
     → Auth: ADMIN ONLY
     → Returns: fraud_flags (OPEN | UNDER_REVIEW)
```

### Reputation Write APIs (Agent-Controlled — Not User-Callable)

```
POST /internal/reputation/recompute          → Trigger full recompute for entity
POST /internal/reputation/event              → Ingest single reputation event
POST /internal/reputation/flag               → Create fraud flag
POST /internal/reputation/decay/run          → Manual decay trigger (admin only)
POST /internal/reputation/endorse/validate   → Validate pending endorsement
```

> **⚠️ All write endpoints are INTERNAL — not exposed on public API gateway**

---

## 8️⃣ UI SCREENS (FLUTTER PRIMARY — REACT READ-ONLY CLONE)

### 8.1 Required Flutter Screens

| Screen ID | Screen Name | Target Role | Module |
|---|---|---|---|
| RA-UI-01 | Trust Score Dashboard | All authenticated users | Reputation |
| RA-UI-02 | Reputation Badge Showcase | Student, Trainer, Recruiter | Reputation |
| RA-UI-03 | Endorsement Request Screen | Student, Trainer | Reputation |
| RA-UI-04 | Endorsement Received Screen | All users | Reputation |
| RA-UI-05 | Reputation Analytics Screen | Trainer, Admin | Reputation |
| RA-UI-06 | Fraud Alert Screen | Admin, Compliance Admin | Reputation |
| RA-UI-07 | Recruiter Trust Card | Recruiter, HR | Job Portal |
| RA-UI-08 | Trainer Public Profile (SEO) | Public | Skill Dev |
| RA-UI-09 | Student Reputation Ring | Student | Skill Dev |
| RA-UI-10 | Reputation History Timeline | Self + Admin | Reputation |
| RA-UI-11 | Tier Upgrade Notification | All users | Reputation |
| RA-UI-12 | Fraud Dispute Screen | User (self-report) | Reputation |
| RA-UI-13 | Peer Rating Modal | Post-Dojo match | Dojo |
| RA-UI-14 | Interview Satisfaction Rating Modal | Post-interview | Job Portal |
| RA-UI-15 | SME Reliability Score Card | Enterprise, SME Admin | ERP |

### 8.2 React (Next.js) Clone Rules

```
React screens must:
  ✅ Be pixel-accurate clones of Flutter screens
  ✅ Be fully responsive (Desktop / Tablet / Mobile)
  ✅ Be SEO-indexed (trainer profiles, public leaderboards)
  ✅ Use server-side rendering for public pages

React screens must NOT:
  ❌ Contain endorsement write logic
  ❌ Allow any mutation
  ❌ Expose internal score metadata
  ❌ Display fraud flags to non-admin users
```

### 8.3 Dashboard Composition Rules

```
FIXED (40%) — Non-removable in Reputation panels:
  - Entity reputation tier badge
  - Trust score ring / meter
  - Domain track label
  - Compliance indicator
  - Critical fraud alerts (admin only)

CUSTOMIZABLE (60%):
  - Endorsement widget position
  - Analytics chart type preference
  - Score history window (7d / 30d / 90d)
  - Leaderboard visibility preference
```

---

## 9️⃣ STATE-DRIVEN UI ENFORCEMENT

```
REPUTATION_TIER    | ALLOWED_ACTIONS              | UI_VISIBILITY
-------------------|------------------------------|----------------------------------
BRONZE (0–39)      | Request endorsement          | Score ring (red), upgrade path
SILVER (40–59)     | Request + give endorsement   | Score ring (orange), analytics lite
GOLD (60–74)       | Full endorsement access      | Score ring (yellow), full analytics
PLATINUM (75–89)   | Mentor others, endorse batch | Score ring (blue), influence rank
ELITE (90–100)     | All + brand badge display    | Score ring (gold), public spotlight

STATE: FRAUD_FLAGGED   → Endorsement giving = HIDDEN (not disabled)
STATE: FRAUD_RESOLVED  → Restore previous state
STATE: ACCOUNT_SUSPENDED → Score frozen, no events ingested
```

> **Rule: Forbidden actions MUST be HIDDEN — not disabled.**
> **Rule: If reputation state is unclear → STOP UI GENERATION**

---

## 🔟 ROLE-BASED VISIBILITY POLICY (RBAC + ABAC)

```
Who sees what reputation data:

STUDENT (self)          → Own composite score, tier, endorsements received/given
STUDENT (other)         → Tier only (score hidden unless ELITE)
TRAINER (self)          → Full score breakdown + influence rank
TRAINER (other)         → Public score + tier (full breakdown hidden)
RECRUITER               → Student/Trainer tier + placement success rate only
INSTITUTE ADMIN         → Own student cohort reputation aggregates
ENTERPRISE ADMIN        → Own recruiter reputation + SME reliability score
TENANT ADMIN            → All entities within own tenant (no cross-tenant)
PLATFORM COMPLIANCE     → Fraud flags, threshold alerts, audit logs
PARENT                  → Student tier only (READ-ONLY, trust layer)
AUTOMATION AGENT (self) → Read all, write to internal APIs only

CROSS-TENANT DATA ACCESS = FORBIDDEN AT ALL TIMES
RAW SCORE ALGORITHM EXPOSURE TO USERS = FORBIDDEN
```

---

## 1️⃣1️⃣ COMPLIANCE & AUDIT (MANDATORY)

### Audit Immutability

```
ALL reputation score changes MUST be written to reputation_audit_log
reputation_audit_log = APPEND-ONLY
DELETE on audit_log = FORBIDDEN
UPDATE on audit_log = FORBIDDEN
Audit entries must include: entity, actor (agent/admin), before, after, justification, timestamp
```

### Compliance Triggers

```
IF reputation score crosses governance threshold:
  → Publish to: ecoskiller.reputation.threshold.crossed
  → Compliance Engine receives → initiates human review workflow
  → AGENT DOES NOT TAKE AUTONOMOUS ACTION BEYOND ALERTING

IF fraud_flag.severity = CRITICAL:
  → Immediate alert to Admin Governance Service
  → Score computation SUSPENDED for entity (frozen at current value)
  → Human decision required before resumption

IF brand reputation risk detected (Section R68 + platform brand layer):
  → Escalate to Reputation & Brand Protection Compliance module
  → AGENT does NOT post-process or suppress signal autonomously
```

### GDPR / Data Privacy

```
Reputation scores are PERSONAL DATA → subject to data retention policy
Score history retention = 36 months (configurable per tenant)
On account deletion: scores pseudonymized, events anonymized
Right to explanation: User may request score computation rationale (admin-mediated)
```

---

## 1️⃣2️⃣ PERFORMANCE & RELIABILITY REQUIREMENTS

```
Score recomputation SLA:
  - Event-triggered recompute: < 5 seconds (P95)
  - Batch nightly recompute: < 30 minutes for full tenant
  - Fraud detection latency: < 2 seconds from event receipt

Availability: 99.9% uptime (part of core trust infrastructure)
Retry policy: 3 retries with exponential backoff on Kafka consumer failures
Dead letter queue: All failed events → DLQ with alerting
```

---

## 1️⃣3️⃣ SECURITY HARDENING

```
ENCRYPTION_AT_REST = AES-256 (score tables + audit log)
ENCRYPTION_IN_TRANSIT = TLS 1.3

IP Reputation Checks:
  - All endorsement write events checked against IP reputation feed
  - Proxy/VPN endorsements flagged for review

UI Security:
  - Fraud alert screens = screenshot BLOCKED (sensitive screen policy)
  - Score breakdown API responses = masked for non-admin roles
  - Internal scoring endpoints = mTLS authentication only
  - Debug / inspector mode = BLOCKED in production
  - No internal score identifiers exposed in UI
```

---

## 1️⃣4️⃣ ACCESSIBILITY (WCAG 2.1 AA — MANDATORY)

```
All reputation UI screens MUST:
  ✅ Support screen readers (TalkBack / VoiceOver)
  ✅ Score ring/meter labeled semantically (not color-only)
  ✅ Tier badge announced on focus
  ✅ Color contrast ≥ 4.5:1 (normal text), ≥ 3:1 (UI components)
  ✅ All actions reachable via keyboard (Tab / Enter / Esc)
  ✅ Text scaling supported to 200%
  ✅ Tier change notifications announced via live region

FAILURE = NON-COMPLIANT SCREEN → MUST NOT SHIP
```

---

## 1️⃣5️⃣ FOUR-STAGE DEVELOPMENT ALIGNMENT

```
STAGE 1 — FOUNDATION_UI:
  Deliver: Basic score display, tier badge, audit log structure
  Tables: reputation_scores, reputation_events, reputation_audit_log

STAGE 2 — INTELLIGENCE_UI:
  Deliver: Full scoring algorithms, fraud detection, analytics screens
  Tables: fraud_flags, endorsement_graph, trainer/student/recruiter score tables

STAGE 3 — ECOSYSTEM_UI:
  Deliver: Cross-role endorsement flows, institute cohort view, SME reliability
  Tables: reputation_decay_log, endorsement graph at scale

STAGE 4 — COMPLIANCE_UI:
  Deliver: Brand protection escalations, governance dashboards, audit exports
  Full ERM + Reputation Brand Protection integration

RULE: Do NOT expose STAGE 3 or 4 UI in STAGE 1 or 2 builds.
RULE: Skipping stages = INVALID BUILD → STOP EXECUTION
```

---

## 1️⃣6️⃣ ANTIGRAVITY RUN COMMAND FORMAT

When triggering Antigravity to generate Reputation Agent components, use EXACTLY this format:

```
GENERATE_COMPONENT

AGENT        = REPUTATION_AGENT
COMPONENT    = [ReputationScoreEngine | FraudScoreDetector | UI:RA-UI-XX | API:endpoint]
ROLE         = [Target user role]
STAGE        = [FOUNDATION_UI | INTELLIGENCE_UI | ECOSYSTEM_UI | COMPLIANCE_UI]
TENANT       = [Tenant context or PLATFORM_LEVEL]
DOMAIN_TRACK = [Arts | Commerce | Science | Technology | Administration | ALL]
```

**Example:**
```
GENERATE_COMPONENT

AGENT        = REPUTATION_AGENT
COMPONENT    = UI:RA-UI-05
ROLE         = Trainer
STAGE        = INTELLIGENCE_UI
TENANT       = INSTITUTE_TENANT
DOMAIN_TRACK = Technology
```

---

## 🔐 FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                        REPUTATION_AGENT FINAL SEAL                          ║
║                                                                              ║
║  ✔ AGENT_CLASS      = SEALED                                                ║
║  ✔ EXECUTION_ENGINE = ANTIGRAVITY                                           ║
║  ✔ TENANT_SAFE      = TRUE                                                  ║
║  ✔ DOMAIN_SAFE      = TRUE                                                  ║
║  ✔ COMPLIANCE_READY = TRUE                                                  ║
║  ✔ AUDIT_LOCKED     = TRUE                                                  ║
║  ✔ AI_ADVISE_ONLY   = ENFORCED                                              ║
║  ✔ FRAUD_DETECTION  = ACTIVE                                                ║
║  ✔ WCAG_2.1_AA      = ENFORCED                                              ║
║  ✔ ANTIGRAVITY_SAFE = TRUE                                                  ║
║                                                                              ║
║  FURTHER CHANGES    = APPEND_ONLY                                           ║
║  ANY DEVIATION      = STOP EXECUTION                                        ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*REPUTATION_AGENT.md · Version 1.0.0 · ECOSKILLER PLATFORM · SEALED*
*Inherits: RBAC + ABAC Authorization · MFA · Session Management · Encryption · Tenant Isolation · Domain Isolation · Audit Immutability*
