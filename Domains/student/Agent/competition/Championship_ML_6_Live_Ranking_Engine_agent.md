# CHAMPIONSHIP ML (6) — LIVE RANKING ENGINE AGENT
## SKILL & COMPETITION CORE · ANTIGRAVITY MODULE

---

```
ECOSKILLER — CHAMPIONSHIP ML (6) LIVE RANKING ENGINE AGENT
Domain: SKILL & COMPETITION CORE → ANTIGRAVITY
Status: FINAL · LOCKED · SEALED · DETERMINISTIC
Mutation Policy: Add-only via version bump
Interpretation Authority: NONE
Execution Authority: Human declaration only
Agent Prompt Seal: ANTIGRAVITY-RANK-AGENT-v1.0
```

---

## SECTION A — AGENT IDENTITY & PURPOSE

```
Agent Name: Championship ML (6) Live Ranking Engine Agent
Agent Code: CML6-RANK-ANTIGRAVITY
Agent Type: Real-Time Competitive Intelligence Engine
Domain: SKILL & COMPETITION CORE → ANTIGRAVITY
Parent System: ECOSKILLER MASTER EXECUTION PROMPT v12.0+

Function:
  This agent computes, maintains, and broadcasts live competitive
  rankings across all AntiGravity Championship tiers in real time.
  It uses a Traditional ML ranking pipeline (R28-2 compliant),
  processing multi-signal participant event streams to produce
  deterministic, explainable, tamper-resistant rank positions
  at all competition levels simultaneously.

AntiGravity Definition:
  AntiGravity is the ECOSKILLER championship domain that rewards
  participants who defy conventional performance expectations —
  breakthrough performers, streak destroyers, hidden talent surges,
  and radical skill acceleration. It detects momentum, velocity,
  and trajectory beyond static scores.
```

---

## SECTION B — ANTIGRAVITY CHAMPIONSHIP TIER ARCHITECTURE

```
TIER 1 — INSTITUTION LEVEL (School / College / Coaching Center)
TIER 2 — DISTRICT LEVEL
TIER 3 — STATE LEVEL
TIER 4 — NATIONAL LEVEL
TIER 5 — CONTINENTAL LEVEL
TIER 6 — GLOBAL LEVEL (AntiGravity World Cup)

All 6 tiers run concurrently.
Rankings propagate bottom-up in real time.
No tier may claim rankings without lower-tier data availability.
```

---

## SECTION C — CORE EXECUTION RULE

```
Rule C1: All ranking computations are Traditional ML only.
  → No LLM may compute rank scores. (R28-2 enforced)
  → Rule engines handle eligibility, gating, and disqualification.
  → LLMs permitted only for human-readable rank explainability.

Rule C2: Identical input signals → Identical rank output.
  → Full determinism required.

Rule C3: Failure Mode = STOP → REPORT → NO PARTIAL RANKING PUBLISHED.
  → No incomplete or stale rankings may be broadcast.

Rule C4: Rankings must refresh at defined cadence intervals:
  → LIVE ROUND: Every 15 seconds
  → POST-ROUND: Every 60 seconds
  → LEADERBOARD: Every 5 minutes
  → ARCHIVAL SNAPSHOT: Every round completion event
```

---

## SECTION D — ANTIGRAVITY SIGNAL REGISTRY

```
The ranking engine ingests the following live event signals:

SIG-01  performance_score          Raw task/challenge score
SIG-02  time_to_completion         Speed of completion in seconds
SIG-03  streak_active              Active daily participation streak count
SIG-04  streak_delta               Change in streak in current session
SIG-05  skill_velocity             Rate of skill score change (points/day)
SIG-06  rank_delta_24h             Rank position change in last 24 hours
SIG-07  rank_delta_7d              Rank position change in last 7 days
SIG-08  challenge_difficulty_level Difficulty tier of completed task (1–10)
SIG-09  peer_comparison_score      Score vs same-tier peer cohort median
SIG-10  consistency_index          Variance in performance over 30 events
SIG-11  comeback_indicator         Boolean: inactive > 5 days then active
SIG-12  upset_score                Score over higher-ranked opponent
SIG-13  multi_intelligence_breadth Count of intelligence domains active
SIG-14  certification_recency      Days since last verified certification
SIG-15  engagement_depth           Avg session duration in minutes
SIG-16  social_endorsement_count   Peer endorsements in current cycle
SIG-17  referral_influence_score   Users activated by this participant
SIG-18  error_recovery_rate        Errors corrected per session
SIG-19  adaptive_difficulty_pass   Passed adaptive difficulty upgrade
SIG-20  live_round_participation   Participated in live championship round

All signals must be:
  ✔ Timestamped with UTC epoch
  ✔ Source-verified via contract gate
  ✔ Stored in RANKING_EVENT_LOG table before ingestion
```

---

## SECTION E — ANTIGRAVITY COMPOSITE SCORE ALGORITHM

```
ALGORITHM: AntiGravity Composite Ranking Score (AGCRS)

Model Class: Gradient Boosting Regressor (Traditional ML — R28-2)
Framework: scikit-learn / LightGBM
Training Target: Historical rank advancement velocity

AGCRS = weighted_sum([
  SIG-01 × W1,   // performance_score weight
  SIG-02 × W2,   // time_to_completion (inverse)
  SIG-05 × W3,   // skill_velocity (high weight)
  SIG-06 × W4,   // rank_delta_24h (surge detection)
  SIG-07 × W5,   // rank_delta_7d (momentum)
  SIG-08 × W6,   // challenge_difficulty_level
  SIG-09 × W7,   // peer_comparison_score
  SIG-11 × W8,   // comeback_indicator (binary amplifier)
  SIG-12 × W9,   // upset_score (high value)
  SIG-03 × W10,  // streak_active
  SIG-20 × W11   // live_round_participation
])

Weight Table (LOCKED — v1.0):
  W1  = 0.18   performance_score
  W2  = 0.08   time_to_completion
  W3  = 0.15   skill_velocity
  W4  = 0.10   rank_delta_24h
  W5  = 0.08   rank_delta_7d
  W6  = 0.09   challenge_difficulty_level
  W7  = 0.07   peer_comparison_score
  W8  = 0.05   comeback_indicator
  W9  = 0.09   upset_score
  W10 = 0.06   streak_active
  W11 = 0.05   live_round_participation

SUM(W) = 1.00 (enforced)

Weight mutation requires version bump and human declaration.
No runtime weight override permitted.
```

---

## SECTION F — FRAUD & ANOMALY DETECTION RULES

```
FRAUD RULE FR-01: Velocity Spike Threshold
  IF skill_velocity > (user_90day_avg × 4.0)
  → FLAG: anomaly_velocity_spike
  → HOLD rank update pending review
  → Emit event: fraud_flag_raised

FRAUD RULE FR-02: Consistency Collapse Detection
  IF consistency_index drops > 60% in 24h with performance_score rise
  → FLAG: inconsistency_detected
  → Emit event: fraud_flag_raised

FRAUD RULE FR-03: Multi-Account Correlation
  IF same IP + device_fingerprint → multiple participant IDs
  → FLAG: multi_account_suspected
  → Quarantine both rank records
  → Emit event: fraud_flag_raised

FRAUD RULE FR-04: Impossible Time Completion
  IF time_to_completion < (challenge_minimum_threshold × 0.5)
  → FLAG: impossible_timing
  → Invalidate score for that round
  → Emit event: fraud_flag_raised

FRAUD RULE FR-05: Endorsement Ring Detection
  IF social_endorsement_count > 50 within 1 hour from < 5 unique IPs
  → FLAG: endorsement_ring
  → Zero social_endorsement_count for scoring
  → Emit event: fraud_flag_raised

On any FRAUD FLAG:
  → Rank update PAUSED for participant
  → Moderation queue entry created
  → Admin Ops Console notified (R40)
  → Participant rank marked [UNDER REVIEW]
  → No disqualification without human moderation confirmation
```

---

## SECTION G — LIVE RANKING PIPELINE SPECIFICATION

```
PIPELINE STAGE 1 — EVENT INGESTION
  Source: Redis Streams (R1 stack)
  Consumer Group: ranking_engine_consumers
  Input: Raw event objects per SIG-01 to SIG-20
  Action: Validate schema → persist to RANKING_EVENT_LOG
  Failure: DROP event → log to DEAD_LETTER_QUEUE → alert

PIPELINE STAGE 2 — SIGNAL NORMALIZATION
  Action: Min-Max scale each signal per tier cohort
  Cohort: Same championship tier + age group
  Output: Normalized signal vector per participant per round
  Failure: STOP STAGE → emit: normalization_failure

PIPELINE STAGE 3 — FRAUD GATE (FR-01 to FR-05)
  Action: Apply all fraud rules before scoring
  Output: PASS or HOLD per participant
  HOLD participants: excluded from current ranking cycle

PIPELINE STAGE 4 — AGCRS COMPUTATION
  Action: Apply AGCRS algorithm (Section E)
  Output: Float score 0.0 – 1000.0 per participant
  Precision: 4 decimal places
  Failure: STOP → emit: scoring_failure

PIPELINE STAGE 5 — TIER RANK COMPUTATION
  Action: Sort AGCRS descending per tier
  Assign: rank_position integer (1 = highest)
  Compute: rank_delta vs previous snapshot
  Failure: STOP → emit: rank_assignment_failure

PIPELINE STAGE 6 — ANTIGRAVITY MULTIPLIER
  Action: Apply AntiGravity Amplifier if:
    rank_delta_24h >= +50 positions
    OR comeback_indicator = TRUE
    OR upset_score > 80th percentile
  Multiplier: AGCRS × 1.15 (one-time per event cycle)
  Purpose: Rewards extraordinary momentum bursts
  Fraud gate re-applied after multiplier

PIPELINE STAGE 7 — RANK BROADCAST
  Output: Push to:
    → LEADERBOARD_SNAPSHOT table
    → Redis pub/sub channel: rankings.antigravity.{tier}
    → WebSocket broadcast to connected clients
    → SEO ranking page ISR trigger (R32)
  Cadence: Per Section C — Rule C4

PIPELINE STAGE 8 — ARCHIVAL SNAPSHOT
  On: round_completion event
  Action: Write immutable rank record to RANK_ARCHIVE_LOG
  Hash: SHA-256 of full rank state
  Purpose: Audit compliance (R60 enforced)
```

---

## SECTION H — DATABASE SCHEMA

```sql
-- Core Ranking Tables

TABLE ranking_event_log (
  event_id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id    UUID        NOT NULL REFERENCES users(id),
  championship_id   UUID        NOT NULL,
  tier_level        INT         NOT NULL CHECK (tier_level BETWEEN 1 AND 6),
  signal_type       TEXT        NOT NULL,  -- SIG-01 to SIG-20
  signal_value      DECIMAL(14,4) NOT NULL,
  recorded_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  source_service    TEXT        NOT NULL,
  is_validated      BOOLEAN     DEFAULT FALSE
);

TABLE participant_agcrs (
  participant_id    UUID        PRIMARY KEY REFERENCES users(id),
  championship_id   UUID        NOT NULL,
  tier_level        INT         NOT NULL,
  agcrs_score       DECIMAL(10,4) NOT NULL,
  rank_position     INT         NOT NULL,
  rank_delta_1h     INT         DEFAULT 0,
  rank_delta_24h    INT         DEFAULT 0,
  rank_delta_7d     INT         DEFAULT 0,
  antigravity_flag  BOOLEAN     DEFAULT FALSE,
  fraud_hold        BOOLEAN     DEFAULT FALSE,
  last_computed_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

TABLE rank_leaderboard_snapshot (
  snapshot_id       UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  championship_id   UUID        NOT NULL,
  tier_level        INT         NOT NULL,
  participant_id    UUID        NOT NULL,
  rank_position     INT         NOT NULL,
  agcrs_score       DECIMAL(10,4) NOT NULL,
  snapshot_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

TABLE rank_archive_log (
  archive_id        UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  championship_id   UUID        NOT NULL,
  round_id          UUID        NOT NULL,
  tier_level        INT         NOT NULL,
  full_rank_state   JSONB       NOT NULL,
  state_hash        TEXT        NOT NULL,  -- SHA-256
  archived_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_immutable      BOOLEAN     DEFAULT TRUE
);

TABLE fraud_flag_log (
  flag_id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id    UUID        NOT NULL,
  fraud_rule        TEXT        NOT NULL,  -- FR-01 to FR-05
  evidence_payload  JSONB       NOT NULL,
  flagged_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  status            TEXT        DEFAULT 'PENDING',  -- PENDING/CLEARED/CONFIRMED
  reviewed_by       UUID        REFERENCES users(id)
);

TABLE rank_explainability_log (
  explain_id        UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id    UUID        NOT NULL,
  championship_id   UUID        NOT NULL,
  signal_breakdown  JSONB       NOT NULL,
  human_summary     TEXT,       -- LLM generated (R28-3)
  generated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes (performance enforcement)
CREATE INDEX idx_agcrs_tier_rank     ON participant_agcrs (tier_level, rank_position);
CREATE INDEX idx_agcrs_championship  ON participant_agcrs (championship_id);
CREATE INDEX idx_event_log_participant ON ranking_event_log (participant_id, recorded_at);
CREATE INDEX idx_snapshot_time       ON rank_leaderboard_snapshot (snapshot_at, tier_level);
```

---

## SECTION I — API CONTRACT REGISTRY

```yaml
openapi: 3.1.0
info:
  title: Championship ML (6) Live Ranking Engine API — AntiGravity
  version: 1.0.0

paths:

  /rankings/antigravity/{tier}/live:
    get:
      summary: Get live leaderboard for a tier (1–6)
      parameters:
        - name: tier
          in: path
          required: true
          schema:
            type: integer
            minimum: 1
            maximum: 6
        - name: limit
          in: query
          schema:
            type: integer
            default: 50
        - name: page
          in: query
          schema:
            type: integer
            default: 1
      responses:
        "200":
          description: Live leaderboard data
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LiveLeaderboardResponse'
        "503":
          description: Ranking engine temporarily halted

  /rankings/antigravity/participant/{participant_id}:
    get:
      summary: Get individual participant live rank across all tiers
      responses:
        "200":
          description: Full rank profile per tier

  /rankings/antigravity/participant/{participant_id}/explain:
    get:
      summary: Get human-readable rank explanation (LLM R28-3)
      responses:
        "200":
          description: Explainability breakdown

  /rankings/antigravity/events/ingest:
    post:
      summary: Ingest a signal event (internal service only)
      security:
        - internalServiceAuth: []
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RankingEventPayload'
      responses:
        "202":
          description: Event accepted and queued
        "400":
          description: Schema validation failure
        "503":
          description: Pipeline stage failure

  /rankings/antigravity/fraud/flags:
    get:
      summary: Admin only — list active fraud flags
      security:
        - bearerAuth: []
      responses:
        "200":
          description: Fraud flag list

  /rankings/antigravity/snapshot/{championship_id}/{round_id}:
    get:
      summary: Retrieve archived round rank snapshot
      responses:
        "200":
          description: Immutable rank archive record

components:
  schemas:
    LiveLeaderboardResponse:
      type: object
      properties:
        tier_level: {type: integer}
        total_participants: {type: integer}
        last_updated: {type: string, format: date-time}
        rankings:
          type: array
          items:
            type: object
            properties:
              rank_position: {type: integer}
              participant_id: {type: string}
              display_name: {type: string}
              agcrs_score: {type: number}
              rank_delta_24h: {type: integer}
              antigravity_flag: {type: boolean}
              fraud_hold: {type: boolean}

    RankingEventPayload:
      type: object
      required: [participant_id, championship_id, tier_level, signal_type, signal_value]
      properties:
        participant_id: {type: string, format: uuid}
        championship_id: {type: string, format: uuid}
        tier_level: {type: integer, minimum: 1, maximum: 6}
        signal_type: {type: string, pattern: "^SIG-[0-9]{2}$"}
        signal_value: {type: number}
        source_service: {type: string}

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
  title: AntiGravity Ranking Engine Events
  version: 1.0.0

channels:

  rankings.antigravity.updated:
    publish:
      message:
        payload:
          type: object
          properties:
            championship_id: {type: string}
            tier_level: {type: integer}
            top_10_snapshot: {type: array}
            updated_at: {type: string}

  fraud.flag.raised:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id: {type: string}
            fraud_rule: {type: string}
            flagged_at: {type: string}

  rank.antigravity.achieved:
    publish:
      message:
        payload:
          type: object
          properties:
            participant_id: {type: string}
            tier_level: {type: integer}
            new_rank: {type: integer}
            rank_delta: {type: integer}
            antigravity_multiplier_applied: {type: boolean}

  round.snapshot.archived:
    publish:
      message:
        payload:
          type: object
          properties:
            championship_id: {type: string}
            round_id: {type: string}
            archive_id: {type: string}
            state_hash: {type: string}
```

---

## SECTION K — MICROSERVICE ARCHITECTURE

```
SERVICE: ranking-engine-service
  Language: Python 3.11 (R1 compliant)
  Framework: FastAPI
  Port: 8030

RESPONSIBILITIES:
  1. Consume Redis Stream: ranking_events
  2. Execute AGCRS pipeline (Sections E–G)
  3. Persist to PostgreSQL ranking tables (Section H)
  4. Publish to Redis pub/sub: rankings.antigravity.{tier}
  5. Emit events (Section J)
  6. Expose REST API (Section I)
  7. Expose /health endpoint

DEPENDENCIES:
  → Redis 7 (event streaming + pub/sub)
  → PostgreSQL 15 (persistence)
  → ML Model Store (LightGBM model artifact)
  → Fraud Engine (internal module)
  → Explainability Service (LLM — R28-3)

HEALTH CHECK:
  GET /health → 200 OK
  Checks: DB reachable, Redis reachable, ML model loaded

DOCKER BUILD:
  File: /backend/services/ranking_engine_service/Dockerfile
  Base: python:3.11-slim
  CMD: uvicorn main:app --host 0.0.0.0 --port 8030

KUBERNETES SERVICE:
  Name: ranking-engine-service
  Internal URL: http://ranking-engine-service:8030
  Namespace: ecoskiller-{env}
```

---

## SECTION L — UI SCREENS SPECIFICATION

```
SCREEN L-01: LIVE ANTIGRAVITY LEADERBOARD
  Route: /app/championship/antigravity/{tier}/live
  Access: Authenticated participant or spectator
  Refresh: WebSocket live update every 15s
  Components:
    - Tier selector (1–6 tabs)
    - Rank table (position, name, score, delta, flag)
    - AntiGravity fire badge for multiplier-active participants
    - "Under Review" label for fraud_hold participants
    - My Rank floating chip (sticky if not in view)
    - Last updated timestamp
  Empty state: "Rankings loading — round starts in {countdown}"
  Error state: "Live rankings temporarily unavailable"

SCREEN L-02: PARTICIPANT RANK CARD
  Route: /app/championship/antigravity/me
  Components:
    - Current tier rank (all 6 tiers shown)
    - AGCRS score gauge
    - 24h rank delta indicator (↑/↓/—)
    - AntiGravity multiplier badge (if active)
    - Signal breakdown mini-chart (top 5 contributing signals)
    - "View Explanation" CTA → opens L-04
  Animations: Rank delta flash on update

SCREEN L-03: CHAMPIONSHIP ROUND RESULT
  Route: /app/championship/antigravity/round/{round_id}/result
  Components:
    - Final rank position
    - AGCRS breakdown
    - Tier progression status
    - Certificates earned
    - Share Card generator
  Unlock: On round_completion event

SCREEN L-04: RANK EXPLAINABILITY SCREEN
  Route: /app/championship/antigravity/explain/{participant_id}
  Components:
    - Human-readable rank explanation (LLM generated — R28-3)
    - Signal contribution bars (top contributing signals)
    - "What can I improve?" suggestions
    - Historical rank graph
  Note: LLM used ONLY for text generation. Rank itself is ML-computed.

SCREEN L-05: ADMIN FRAUD FLAG DASHBOARD (Ops Console — R40)
  Route: /ops/championship/antigravity/fraud
  Access: Super Admin only (MFA enforced)
  Components:
    - Active fraud flag list
    - Participant rank hold status
    - Fraud rule triggered
    - Evidence payload viewer
    - Approve (clear) / Confirm (disqualify) actions
    - Full audit log per action
```

---

## SECTION M — PERMISSION & ROLE MATRIX

```
ROLE              | VIEW LIVE RANKS | VIEW EXPLAIN | INGEST EVENTS | VIEW FRAUD FLAGS | CLEAR FLAGS
------------------+-----------------+--------------+---------------+------------------+------------
anonymous         | public tiers    | NO           | NO            | NO               | NO
participant       | all tiers       | own only     | NO            | NO               | NO
recruiter         | all tiers       | all          | NO            | NO               | NO
institute_admin   | own tier        | own students | NO            | NO               | NO
internal_service  | NO              | NO           | YES           | NO               | NO
ops_admin         | all             | all          | NO            | YES              | YES
super_admin       | all             | all          | NO            | YES              | YES
```

---

## SECTION N — INTERN EXECUTION GUIDE

```
OBJECTIVE: Run ranking engine service locally.

INTERN ROLE: Python Backend Developer

REQUIRED TOOLS:
  Python 3.11+, PostgreSQL, Redis, Docker

STEP-BY-STEP:

Step 1 — Navigate to service directory:
  cd /backend/services/ranking_engine_service/

Step 2 — Install dependencies:
  pip install -r requirements.txt

Step 3 — Configure environment:
  cp .env.example .env
  (Fill DB_HOST, REDIS_HOST, MODEL_PATH)

Step 4 — Run DB migrations:
  alembic upgrade head
  Expected: "Running upgrade -> head" with all ranking tables created.

Step 5 — Load ML model (dev mode uses dummy model):
  python scripts/load_model.py --env dev
  Expected: "Model loaded: antigravity_ranker_v1.joblib"

Step 6 — Start service:
  uvicorn main:app --reload --port 8030
  Expected: "Uvicorn running on http://127.0.0.1:8030"

Step 7 — Verify:
  Open: http://127.0.0.1:8030/docs
  ✔ Swagger UI loads
  ✔ /health returns 200 OK
  ✔ POST /rankings/antigravity/events/ingest accepts test payload

SUCCESS CONDITION:
  Service responds to API calls
  DB tables created and reachable
  Redis streams consume without error
  No runtime exceptions in logs

FAILURE → STOP EXECUTION
```

---

## SECTION O — ML MODEL SPECIFICATION

```
MODEL: AntiGravity Ranking Gradient Booster (ARGB-v1)

Type: LightGBM Gradient Boosting Regressor (Traditional ML — R28-2)
Target Variable: rank_advancement_velocity (historical)
Feature Set: SIG-01 to SIG-20 (normalized)
Training Frequency: Weekly retraining scheduled job
Evaluation Metric: NDCG@10 (Normalized Discounted Cumulative Gain)
Minimum NDCG@10 Threshold: 0.82 (below → reject model, retain previous)

INFERENCE COST: < $0.001 per 1,000 participants (rule-based pre-filter reduces calls)
MONTHLY COST ESTIMATE (1M participants): ~$15–$25

MODEL ARTIFACT STORAGE:
  Path: /models/antigravity_ranker_v1.joblib
  Registry: MinIO (R1 stack)
  Versioned: MAJOR.MINOR format

EXPLAINABILITY:
  Method: SHAP values per signal per participant
  Format: JSON signal breakdown → passed to LLM for human text (R28-3)
  LLM Role: Text generation ONLY — no ranking decisions

FAIRNESS CHECKS (R51 compliant):
  ✔ Gender bias check per training cycle
  ✔ Region bias check per training cycle
  ✔ Age group bias check
  Bias threshold: Demographic parity difference < 0.05
  Failure → reject model → alert ops team
```

---

## SECTION P — OBSERVABILITY & MONITORING HOOKS

```
METRICS (Prometheus):
  ranking_events_ingested_total       (counter)
  ranking_pipeline_stage_latency_ms   (histogram per stage)
  agcrs_computation_duration_ms       (histogram)
  fraud_flags_raised_total            (counter)
  rank_updates_broadcast_total        (counter)
  ranking_engine_errors_total         (counter by stage)
  model_inference_latency_ms          (histogram)

ALERTS (Grafana):
  ALERT: ranking_engine_errors_total > 10 in 5m  → PagerDuty
  ALERT: pipeline_stage_latency_ms > 2000         → Slack warning
  ALERT: fraud_flags_raised_total > 50 in 1h      → Ops console alert
  ALERT: model_inference_latency_ms > 500         → Auto-scaling trigger

LOGGING (Loki):
  Log level: INFO in prod, DEBUG in dev
  Every pipeline stage entry/exit logged with participant_id masked
  Every fraud flag logged in full

TRACING (Jaeger):
  Full trace per ranking cycle request end-to-end
  Trace ID injected into every event for correlation
```

---

## SECTION Q — CONTRACT GATE REQUIREMENTS (R49 Compliance)

```
Before any deployment of this agent the following contracts must be
validated by the Contract Validator CLI (R49):

  ✔ ranking_event_schema validated against AsyncAPI registry
  ✔ AGCRS signal weights sum = 1.00
  ✔ All SIG-01 to SIG-20 sources registered in Event Schema Registry
  ✔ API contract registered in API Contract Registry
  ✔ Permission → Screen matrix registered
  ✔ Role → Widget matrix registered
  ✔ ML model NDCG@10 ≥ 0.82 passed

Absence of any validation → STOP EXECUTION
```

---

## SECTION R — ENFORCEMENT RULES

```
ENFORCEMENT RULE R-01:
  No live ranking may be published to any tier unless all 8
  pipeline stages (Section G) complete without failure.
  Partial rankings are FORBIDDEN.
  Failure → STOP BROADCAST → emit: ranking_broadcast_failure

ENFORCEMENT RULE R-02:
  No ML model may be deployed to production without:
    ✔ NDCG@10 ≥ 0.82
    ✔ Bias check passed
    ✔ Human approval log entry (M5 — AI Model Reality Law)
  Failure → STOP DEPLOYMENT

ENFORCEMENT RULE R-03:
  No participant under fraud_hold may appear in any public
  leaderboard with a confirmed rank position.
  [UNDER REVIEW] label is the only permitted display.

ENFORCEMENT RULE R-04:
  All archival rank snapshots must include SHA-256 hash.
  Any snapshot with hash mismatch is INVALID and triggers
  an immediate audit event to the Ops Console (R40).

ENFORCEMENT RULE R-05:
  The AntiGravity Multiplier (Section G — Stage 6) may be
  applied at most ONCE per participant per championship round.
  Multiple applications in one round → REJECT → recompute.

ENFORCEMENT RULE R-06:
  Weight table (Section E) is LOCKED.
  Any modification without version bump and human declaration
  → STOP EXECUTION → REPORT WEIGHT_MUTATION_VIOLATION

Violation of any rule above:
  → STOP EXECUTION
  → REPORT RANKING_ENGINE_VIOLATION
  → NO CHAMPIONSHIP RESULT CLAIM PERMITTED
```

---

## SECTION S — PRODUCTION READINESS CHECKLIST

```
Before this agent may be declared Production-Ready (L3 compliant):

INFRASTRUCTURE:
  ✔ ranking-engine-service Kubernetes pod STATUS = Running
  ✔ Redis Streams consumer group active
  ✔ PostgreSQL ranking tables created and indexed
  ✔ ML model loaded and /health returning 200

CONTRACTS:
  ✔ R49 Contract Validator passed
  ✔ R50 QA Tests generated and passing
  ✔ API contract registered

ML:
  ✔ Model NDCG@10 ≥ 0.82
  ✔ Bias check passed
  ✔ Human training approval log exists (M5)

SECURITY:
  ✔ Internal service auth on event ingest endpoint
  ✔ Super Admin MFA for fraud flag access
  ✔ No participant PII in leaderboard broadcast payloads

OBSERVABILITY:
  ✔ Prometheus metrics exported
  ✔ Grafana alerts configured
  ✔ Loki logs flowing
  ✔ Jaeger traces active

Until all conditions above are met:
  Agent Status = "ARTIFACTS GENERATED — NOT DEPLOYED"
  NOT: "Live Ranking Engine Operational"
```

---

## SECTION T — FINAL SEAL

```
AGENT SEAL: ANTIGRAVITY-RANK-AGENT-v1.0
FILE: Championship_ML_6_Live_Ranking_Engine_agent.md
DOMAIN: SKILL & COMPETITION CORE → ANTIGRAVITY
PARENT: ECOSKILLER MASTER EXECUTION PROMPT v12.0+

COMPLIANCE:
  ✔ R1  — Technology Stack Lock (Python, FastAPI, Redis, PostgreSQL, LightGBM)
  ✔ R2  — Domain Data Models (ranking tables defined)
  ✔ R3  — OpenAPI 3.1 Contract (Section I)
  ✔ R4  — AsyncAPI Event Schema (Section J)
  ✔ R5  — Workflow State Machine (pipeline stages Section G)
  ✔ R12 — AI Model Specification (Section O)
  ✔ R24 — Execution Skill Alignment (intern guide Section N)
  ✔ R26 — Intern Line-Level Execution Instructions
  ✔ R28 — Intelligence Cost Optimization (Traditional ML, no LLM for ranking)
  ✔ R29 — Modern UI Screens (Section L, 5 screens defined)
  ✔ R38 — Bug & Failure Registry (fraud rules, enforcement rules)
  ✔ R39 — Core Inbuilt Tools (ranking engine, fraud engine, explainability)
  ✔ R40 — Admin Ops Console integration (fraud flag dashboard)
  ✔ R49 — Contract Validator compliance (Section Q)
  ✔ R51 — Anti-Spam & Abuse Prevention (fraud rules FR-01 to FR-05)
  ✔ R60 — Long-term Archival (immutable rank snapshots, hash chain)
  ✔ M5  — AI Model Reality Law (model spec, human approval required)
  ✔ L2  — AI Operational Restriction (no real cloud deployment claimed)

STATUS: LOCKED · SEALED · DETERMINISTIC
Mutation: Add-only via version bump to ANTIGRAVITY-RANK-AGENT-v2.0
Interpretation Authority: NONE
Execution Authority: Human declaration only
```

---

*ECOSKILLER — Championship ML (6) Live Ranking Engine Agent · AntiGravity Module · v1.0 · SEALED*
