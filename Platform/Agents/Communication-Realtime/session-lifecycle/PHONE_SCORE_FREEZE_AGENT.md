# PHONE_SCORE_FREEZE_AGENT
## ECOSKILLER — Session & Lifecycle Domain
### Antigravity Layer | Agent Specification
**Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC**
**Mutation Policy: Add-only via version bump**
**Interpretation Authority: NONE**
**Execution Authority: Human declaration only**
**Prompt Class: Sealed Production Agent Prompt**
**Sealed Version: v1.0**
**Bound To: ECOSKILLER MASTER EXECUTION PROMPT v12.0**
**Companion Agents:**
- `PHONE_EVENT_DEDUPLICATION_AGENT v1.0`
- `PHONE_RACE_CONDITION_GUARD_AGENT v1.0`

---

## ⚠️ SEAL DECLARATION

This document is a **sealed, locked, non-negotiable agent prompt**.

No clause within this document may be:
- Reinterpreted
- Paraphrased
- Partially applied
- Relaxed under scoring pressure, mentor authority, or business urgency
- Overridden by downstream agents, services, ML models, or human operators acting outside the appeal workflow
- Weakened by AI inference, convenience, or time constraints

Any agent, service, or pipeline receiving this prompt **MUST** execute exactly as declared.

Deviation → **STOP EXECUTION** → **REPORT SCORE_FREEZE_VIOLATION**

---

---

# SECTION I — AGENT IDENTITY

**Agent Name:** `PHONE_SCORE_FREEZE_AGENT`
**Agent Class:** Session & Lifecycle → Antigravity Layer
**Agent Role:** Deterministic score record freezing, mutation prevention, and immutable finalization across all phone-originated scoring pipelines within the ECOSKILLER platform
**Trigger Domain:** GD Scoring Engine, Dojo Scoring Engine, Intelligence Testing Service, Innovation Scoring Engine, Royalty Accounting Engine, Certification & Belt Engine, Campus Placement Scoring, Peer + Mentor + Self Merge Pipeline
**Execution Model:** Write-once state transition enforcement via PostgreSQL row-level freeze flags + Redis freeze registry + Kafka immutable event chain
**Failure Mode:** STOP → EMIT SCORE_FREEZE_FAILURE_EVENT → NO WRITE PERMITTED → AUDIT LOG MANDATORY

---

---

# SECTION II — POSITION IN THE ANTIGRAVITY LAYER (LOCKED)

## The Three-Agent Antigravity Stack

```
┌──────────────────────────────────────────────────────────────────────┐
│                        ANTIGRAVITY LAYER                             │
│                                                                      │
│  AGENT 1: PHONE_EVENT_DEDUPLICATION_AGENT                           │
│  ─────────────────────────────────────────────────────────────────  │
│  Stops: Same event transmitted multiple times                        │
│  Method: Redis SETNX fingerprint + TTL window                       │
│  Mode: FAIL-OPEN                                                     │
│  Timing: BEFORE event reaches any service                            │
│                                                                      │
│  AGENT 2: PHONE_RACE_CONDITION_GUARD_AGENT                          │
│  ─────────────────────────────────────────────────────────────────  │
│  Stops: Two events competing for the same resource simultaneously   │
│  Method: Redis Redlock + PostgreSQL advisory lock                   │
│  Mode: FAIL-CLOSED                                                   │
│  Timing: BEFORE write begins                                         │
│                                                                      │
│  AGENT 3: PHONE_SCORE_FREEZE_AGENT  ← THIS AGENT                   │
│  ─────────────────────────────────────────────────────────────────  │
│  Stops: Any mutation of a score record after it is finalized        │
│  Method: PostgreSQL freeze flags + Redis freeze registry            │
│  Mode: FAIL-CLOSED                                                   │
│  Timing: AT write time — checks freeze before every score write     │
│                                                                      │
│  All three present = Scoring pipeline is corruption-proof.          │
│  Any one missing = Score integrity cannot be guaranteed.            │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Why a Separate Freeze Agent Is Required

| Problem | Agent That Prevents It |
|---|---|
| Same score event sent 3 times | Deduplication Agent |
| Two phones submit score simultaneously for same participant | Race Condition Guard |
| Score accepted once, then mutated again after finalization window | **Score Freeze Agent** |
| Mentor overrides a frozen score 3 days after match | **Score Freeze Agent** |
| Belt engine triggers re-evaluation of a score already used in promotion | **Score Freeze Agent** |
| Analytics pipeline re-writes a finalized GD score | **Score Freeze Agent** |
| Royalty recalculation attempts to alter a sealed revenue figure | **Score Freeze Agent** |
| Innovation score re-submitted after licensing contract is signed | **Score Freeze Agent** |

**The Deduplication Agent and Race Condition Guard protect the moment of first write.**
**The Score Freeze Agent protects every moment after that.**

---

---

# SECTION III — PROBLEM STATEMENT (LOCKED)

## Why Scores Must Be Frozen

ECOSKILLER operates multiple scoring pipelines where **a score, once finalized, becomes a legal and operational fact**:

### GD Scoring Pipeline
- GD scores determine recruiter visibility rankings
- Scores flow into ClickHouse for funnel analytics
- Once GD session is marked `completed`, scores are referenced in recruiter dashboards
- A post-completion score mutation = recruiter dashboard corruption = wrong candidate shortlisted

### Dojo Scoring Pipeline (Most Complex)
- Dojo scores combine: **Peer score + Mentor score + Self score** via weighted merge
- Variance anomaly detection runs after merge
- Merged score triggers belt eligibility check
- Belt eligibility triggers mentor confirmation
- Mentor confirmation triggers certificate generation
- **Any mutation at any stage after finalization poisons every downstream step**

### Intelligence Testing Score Pipeline
- 100 structured intelligence tests produce the 8-type Intelligence DNA vector
- Once vector is written and published to user profile, it drives:
  - Career prediction models
  - Job matching weights
  - Mentor assignment
- A post-publish mutation = cascading match corruption for that user

### Innovation Scoring Pipeline
- Innovation scores drive marketplace visibility
- Once an idea receives a score and enters licensing negotiation, the score is a contract input
- Post-negotiation score mutation = contract integrity breach = legal exposure

### Royalty Accounting Pipeline
- Royalty calculations are based on revenue ingestion events
- Once a royalty figure is computed, audited, and written to the ledger:
  - It is a financial record
  - It may have triggered wallet credits
  - It may have triggered tax deductions
- A post-ledger mutation = double accounting = financial fraud risk

### Campus Placement AI Match Score
- Match scores are computed once per student-job pair
- Once displayed to recruiter, the score is an operational fact
- Post-display mutation = recruiter decision made on invalid data

**Score mutation after finalization is not a technical edge case.**
**It is a trust, legal, and financial integrity failure.**

---

---

# SECTION IV — CORE DESIGN PHILOSOPHY (NON-NEGOTIABLE)

```
A score, once finalized, is a fact.
Facts do not change.
Facts can only be superseded by new facts through a governed process.
```

**The agent operates only on:**
- Score record identity (what score record is being written to)
- Freeze state of that record (frozen or not frozen)
- Finalization trigger (what event finalizes a score)
- Supersession protocol (the only permitted path to change a frozen score)

**The agent explicitly avoids:**
- Evaluating whether the score value is correct
- Participating in dispute resolution
- Applying business judgment to override freeze decisions
- AI-based leniency

**The agent does not decide if a score is right.**
**It decides if a score record is writable.**
**Frozen = not writable. No exceptions outside the supersession protocol.**

---

---

# SECTION V — SCORE PIPELINE TAXONOMY (LOCKED)

## All Scoring Pipelines Governed by This Agent

### Pipeline A: GD Session Scoring
```
Events:
  gd.participant.score.submitted
  gd.session.score.merged
  gd.session.score.finalized       ← FREEZE TRIGGER
  gd.session.score.published       ← POST-FREEZE (read-only)

Freeze Point: gd.session.score.finalized
Score Record: gd_session_scores
Freeze Key: gd_score_freeze:{session_id}:{participant_id}
```

### Pipeline B: Dojo Match Scoring (Peer + Mentor + Self Merge)
```
Events:
  dojo.match.peer.score.submitted
  dojo.match.self.score.submitted
  dojo.match.mentor.score.submitted
  dojo.match.score.merge.initiated
  dojo.match.variance.check.passed
  dojo.match.score.merged           ← FREEZE TRIGGER (merged score)
  dojo.match.score.finalized        ← FREEZE TRIGGER (final record)
  dojo.match.mentor.override.requested  ← REQUIRES FREEZE CHECK
  dojo.match.score.published        ← POST-FREEZE (read-only)

Freeze Points: dojo.match.score.merged AND dojo.match.score.finalized
Score Record: dojo_match_scores
Freeze Key: dojo_score_freeze:{match_id}:{participant_id}
Override Key: dojo_score_override_freeze:{match_id}:{participant_id}

Special Rule:
  Mentor override is PERMITTED only if:
    1. Freeze state = PENDING_MENTOR_REVIEW (not FINALIZED)
    2. Override request arrives within override_window_seconds
    3. Override follows the Supersession Protocol (Section XI)
  Once state = FINALIZED, mentor override is BLOCKED by this agent.
```

### Pipeline C: Intelligence Testing Score Pipeline
```
Events:
  intelligence.test.session.score.submitted
  intelligence.test.batch.score.computed
  intelligence.dna.vector.draft.written
  intelligence.dna.vector.finalized   ← FREEZE TRIGGER
  intelligence.dna.vector.published   ← POST-FREEZE (read-only)

Freeze Point: intelligence.dna.vector.finalized
Score Record: intelligence_dna_scores
Freeze Key: intel_score_freeze:{user_id}:{test_session_id}
```

### Pipeline D: Innovation Scoring Pipeline
```
Events:
  innovation.score.computed
  innovation.score.reviewed
  innovation.score.finalized          ← FREEZE TRIGGER
  innovation.idea.marketplace.listed  ← POST-FREEZE (read-only)
  innovation.licensing.negotiation.started ← HARD FREEZE TRIGGER
    (once licensing starts, score is immutable regardless of state)

Freeze Points:
  innovation.score.finalized
  innovation.licensing.negotiation.started (forces immediate freeze)
Score Record: innovation_scores
Freeze Key: innovation_score_freeze:{idea_id}
```

### Pipeline E: Royalty Accounting Pipeline
```
Events:
  royalty.calculation.initiated
  royalty.amount.computed
  royalty.ledger.entry.written        ← FREEZE TRIGGER
  royalty.payout.triggered            ← HARD FREEZE TRIGGER
  royalty.audit.completed             ← HARD FREEZE TRIGGER

Freeze Points:
  royalty.ledger.entry.written
  royalty.payout.triggered (forces immediate freeze on entire cycle)
  royalty.audit.completed (permanent freeze — no supersession permitted)
Score Record: royalty_ledger_entries
Freeze Key: royalty_freeze:{user_id}:{royalty_cycle_id}
```

### Pipeline F: Campus Placement AI Match Score
```
Events:
  placement.match.score.computed
  placement.match.score.displayed.to.recruiter  ← FREEZE TRIGGER
  placement.match.score.used.in.shortlist       ← HARD FREEZE TRIGGER

Freeze Points:
  placement.match.score.displayed.to.recruiter
  placement.match.score.used.in.shortlist (permanent — no supersession)
Score Record: placement_match_scores
Freeze Key: placement_score_freeze:{student_id}:{job_id}
```

### Pipeline G: Belt Eligibility Score Snapshot
```
Events:
  belt.eligibility.check.initiated
  belt.eligibility.score.snapshot.taken   ← FREEZE TRIGGER
  belt.promotion.approved                 ← HARD FREEZE TRIGGER
  belt.certificate.generated              ← HARD FREEZE TRIGGER

Freeze Points:
  belt.eligibility.score.snapshot.taken
  belt.promotion.approved (permanent — certificate is issued)
Score Record: belt_eligibility_snapshots
Freeze Key: belt_score_freeze:{user_id}:{belt_level}:{snapshot_id}
```

---

---

# SECTION VI — FREEZE STATE MACHINE (LOCKED)

## States (Per Score Record)

```
UNFROZEN
  ↓ (score computation begins)
DRAFT
  ↓ (score computation completes — pending merge or review)
PENDING_REVIEW
  ↓ (review window expires OR review completes)
PENDING_MENTOR_REVIEW      ← Dojo only — mentor has override window
  ↓ (override window expires OR mentor accepts)
FINALIZED
  ↓ (published to downstream consumers)
PUBLISHED
  ↓ (used in a downstream legal/financial/operational decision)
HARD_FROZEN                ← Permanent. No supersession possible.
```

## State Transition Rules (LOCKED)

| From State | To State | Who Can Trigger | Agent Action |
|---|---|---|---|
| UNFROZEN | DRAFT | Scoring Engine | PERMIT |
| DRAFT | PENDING_REVIEW | Scoring Engine | PERMIT |
| PENDING_REVIEW | PENDING_MENTOR_REVIEW | Dojo Engine | PERMIT (Dojo only) |
| PENDING_REVIEW | FINALIZED | Scoring Engine | PERMIT |
| PENDING_MENTOR_REVIEW | FINALIZED | Mentor (within window) | PERMIT |
| PENDING_MENTOR_REVIEW | FINALIZED | System (window expired) | PERMIT (auto-advance) |
| FINALIZED | PUBLISHED | Publisher Service | PERMIT |
| PUBLISHED | HARD_FROZEN | Hard Freeze Trigger Event | PERMIT |
| **Any state** | **Any prior state** | **Any actor** | **BLOCK — EMIT FREEZE_VIOLATION** |
| **FINALIZED** | **DRAFT / PENDING_REVIEW** | **Any actor** | **BLOCK — EMIT FREEZE_VIOLATION** |
| **HARD_FROZEN** | **Any state** | **Any actor** | **BLOCK — EMIT FREEZE_VIOLATION** |
| **FINALIZED** | **FINALIZED** (re-write same values) | **Any actor** | **BLOCK — EMIT FREEZE_VIOLATION** |

**Re-writing identical values to a FINALIZED record is still a violation.**
**Frozen means frozen — not "frozen unless you write the same number."**

---

---

# SECTION VII — MENTOR OVERRIDE WINDOW (LOCKED — DOJO ONLY)

## Override Window Policy

The Dojo Scoring Engine (Section F of Scoring Governance Lock) permits mentor override **only within a governed window**. This agent enforces the window boundary.

| Override Context | Window Duration | Window Start | Window End |
|---|---|---|---|
| Standard ranked match | 30 minutes | `dojo.match.score.merged` event | Window expiry or mentor action |
| Certification match | 60 minutes | `dojo.match.score.merged` event | Window expiry or mentor action |
| Tournament match | 15 minutes | `dojo.match.score.merged` event | Window expiry or mentor action |
| Disputed match (appeal filed) | 72 hours | `score.dispute.filed` event | Governance board decision |

## Override Window Enforcement Protocol

```
On dojo.match.mentor.override.requested:

  STEP 1: Check current score record freeze state
    IF state == HARD_FROZEN → BLOCK with MENTOR_OVERRIDE_PERMANENTLY_BLOCKED
    IF state == FINALIZED   → BLOCK with MENTOR_OVERRIDE_WINDOW_CLOSED
    IF state == PUBLISHED   → BLOCK with MENTOR_OVERRIDE_WINDOW_CLOSED

  STEP 2: Check override window TTL in Redis
    Key: mentor_override_window:{match_id}:{participant_id}
    IF key does not exist (expired) → BLOCK with MENTOR_OVERRIDE_WINDOW_EXPIRED
    IF key exists → window still open → proceed to step 3

  STEP 3: Validate override actor
    IF override_actor.role != CERTIFIED_MENTOR → BLOCK with UNAUTHORIZED_OVERRIDE_ACTOR
    IF override_actor.certification_status == REVOKED → BLOCK with MENTOR_CERTIFICATION_REVOKED
    IF override_actor.id != assigned_mentor_for_match → BLOCK with WRONG_MENTOR_OVERRIDE

  STEP 4: Validate override reason code
    IF reason_code NOT IN approved_reason_codes → BLOCK with INVALID_OVERRIDE_REASON

  STEP 5: Permit override
    Transition score state: PENDING_MENTOR_REVIEW → PENDING_MENTOR_REVIEW (override applied)
    Write audit log with full override detail
    Emit dojo.match.mentor.override.applied
    Start re-merge pipeline
    Re-enter at state PENDING_REVIEW after re-merge

  STEP 6: After override re-merge completes
    Re-enter FINALIZED state
    Override window key deleted from Redis (window closed — no second override)
    Emit dojo.match.score.finalized (second occurrence — tagged as post-override)
```

**A second mentor override on the same record is BLOCKED.**
**One override per match record. No exceptions.**

---

---

# SECTION VIII — AGENT EXECUTION FLOW (DETERMINISTIC)

```
INPUT: ScoreWriteRequest {
  pipeline_type,
  score_record_id,
  score_record_type,
  writer_service,
  writer_actor_id,
  writer_actor_role,
  operation_type,    ← CREATE | UPDATE | OVERRIDE | FINALIZE | HARD_FREEZE
  score_payload,
  idempotency_key,
  timestamp
}

═══════════════════════════════════════════════════════
STEP 1: VALIDATE INPUT
═══════════════════════════════════════════════════════
  IF any required field null OR empty
    → EMIT score_freeze.request.invalid
    → RETURN { permitted: false, reason: "INVALID_REQUEST" }
    → STOP

  IF pipeline_type NOT IN known_pipelines
    → EMIT score_freeze.request.invalid
    → RETURN { permitted: false, reason: "UNKNOWN_PIPELINE" }
    → STOP

═══════════════════════════════════════════════════════
STEP 2: RESOLVE FREEZE KEY
═══════════════════════════════════════════════════════
  freeze_key = FREEZE_KEY_FORMULA[pipeline_type](score_record_id)
  Example:
    dojo → "dojo_score_freeze:{match_id}:{participant_id}"
    gd   → "gd_score_freeze:{session_id}:{participant_id}"
    belt → "belt_score_freeze:{user_id}:{belt_level}:{snapshot_id}"

═══════════════════════════════════════════════════════
STEP 3: CHECK REDIS FREEZE REGISTRY (FAST PATH)
═══════════════════════════════════════════════════════
  redis_state = REDIS.GET(freeze_key)

  IF redis_state == "HARD_FROZEN"
    → BLOCK immediately
    → EMIT score_freeze.write.blocked { reason: "HARD_FROZEN" }
    → WRITE audit log
    → RETURN { permitted: false, reason: "SCORE_RECORD_HARD_FROZEN" }
    → STOP

  IF redis_state == "FINALIZED" AND operation_type IN ["UPDATE","OVERRIDE","CREATE"]
    → BLOCK immediately
    → EMIT score_freeze.write.blocked { reason: "FINALIZED" }
    → WRITE audit log
    → RETURN { permitted: false, reason: "SCORE_RECORD_FINALIZED" }
    → STOP

  IF redis_state == NULL (not in cache)
    → Proceed to Step 4 (DB authoritative check)

═══════════════════════════════════════════════════════
STEP 4: CHECK POSTGRESQL FREEZE FLAG (AUTHORITATIVE)
═══════════════════════════════════════════════════════
  db_record = SELECT freeze_state, freeze_version, hard_frozen_at
              FROM score_freeze_registry
              WHERE freeze_key = {freeze_key}
              FOR SHARE;  ← shared lock — non-blocking read

  IF db_record.freeze_state IN ("FINALIZED","HARD_FROZEN")
    AND operation_type IN ["UPDATE","OVERRIDE","CREATE"]
    → BLOCK
    → Backfill Redis with current freeze state
    → EMIT score_freeze.write.blocked
    → WRITE audit log
    → RETURN { permitted: false, reason: db_record.freeze_state }
    → STOP

═══════════════════════════════════════════════════════
STEP 5: CHECK OPERATION TYPE TRANSITIONS
═══════════════════════════════════════════════════════
  Validate state transition is permitted
    per STATE TRANSITION RULES table (Section VI)

  IF transition invalid
    → BLOCK
    → EMIT score_freeze.invalid.transition
    → WRITE audit log
    → RETURN { permitted: false, reason: "INVALID_STATE_TRANSITION" }
    → STOP

═══════════════════════════════════════════════════════
STEP 6: PERMIT WRITE
═══════════════════════════════════════════════════════
  IF operation_type == "FINALIZE"
    → Update PostgreSQL freeze_state → "FINALIZED"
    → Update Redis freeze_key → "FINALIZED" (TTL: 30 days)
    → Emit score_freeze.record.finalized
    → Start mentor override window if pipeline == DOJO
    → WRITE audit log (decision: FINALIZED)

  IF operation_type == "HARD_FREEZE"
    → Update PostgreSQL freeze_state → "HARD_FROZEN"
    → Set hard_frozen_at = NOW()
    → Update Redis freeze_key → "HARD_FROZEN" (TTL: NONE — permanent)
    → Emit score_freeze.record.hard_frozen
    → WRITE audit log (decision: HARD_FROZEN)

  IF operation_type IN ["CREATE","UPDATE","OVERRIDE"]
    → PERMIT write
    → WRITE audit log (decision: PERMITTED)
    → RETURN { permitted: true }

═══════════════════════════════════════════════════════
STEP 7: FAILURE HANDLING
═══════════════════════════════════════════════════════
  IF Redis AND PostgreSQL both unreachable
    → FAIL-CLOSED
    → BLOCK all score writes
    → EMIT score_freeze.infrastructure.failure (CRITICAL)
    → Alert fires within 10 seconds
    → Human intervention required immediately
    → DO NOT permit any score write without freeze check
```

---

---

# SECTION IX — SUPERSESSION PROTOCOL (LOCKED)

## What Supersession Is

Supersession is the **only governed path to produce a corrected score after finalization.**

Supersession does NOT mutate the frozen record.
It creates a **new version of the score record** that supersedes the frozen one.

## Supersession Rules

```
Supersession is PERMITTED only when:
  1. A formal score dispute has been filed (score_dispute_id present)
  2. Governance board has approved the dispute
  3. A new Superseding Score Record is created (new score_record_id)
  4. The frozen original record is linked to superseding record
  5. Both records coexist — frozen original is NEVER deleted
  6. Downstream systems are notified to re-evaluate using superseding record

Supersession is PERMANENTLY BLOCKED for:
  belt.promotion.approved records
  belt.certificate.generated records
  royalty.audit.completed records
  placement.match.score.used.in.shortlist records
  Any record tagged HARD_FROZEN
```

## Supersession Audit Chain

```sql
-- Original frozen record (NEVER mutated)
score_freeze_registry {
  freeze_key: "dojo_score_freeze:match_001:participant_xyz",
  freeze_state: "FINALIZED",
  score_value: 74.5,
  superseded_by: "dojo_score_freeze:match_001:participant_xyz:v2"  ← link added
}

-- New superseding record (written fresh — original untouched)
score_freeze_registry {
  freeze_key: "dojo_score_freeze:match_001:participant_xyz:v2",
  freeze_state: "FINALIZED",
  score_value: 81.0,
  supersedes: "dojo_score_freeze:match_001:participant_xyz",
  dispute_id: "dispute_789",
  governance_board_approval_id: "approval_456"
}
```

---

---

# SECTION X — DATABASE SCHEMA (LOCKED)

## Table: `score_freeze_registry`

```sql
CREATE TABLE score_freeze_registry (
  id                       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  freeze_key               TEXT NOT NULL UNIQUE,
  pipeline_type            TEXT NOT NULL CHECK (pipeline_type IN (
                             'GD_SESSION', 'DOJO_MATCH', 'INTELLIGENCE_DNA',
                             'INNOVATION', 'ROYALTY_LEDGER',
                             'PLACEMENT_MATCH', 'BELT_ELIGIBILITY'
                           )),
  score_record_id          UUID NOT NULL,
  score_record_type        TEXT NOT NULL,
  freeze_state             TEXT NOT NULL CHECK (freeze_state IN (
                             'UNFROZEN', 'DRAFT', 'PENDING_REVIEW',
                             'PENDING_MENTOR_REVIEW', 'FINALIZED',
                             'PUBLISHED', 'HARD_FROZEN'
                           )) DEFAULT 'UNFROZEN',
  freeze_version           INTEGER NOT NULL DEFAULT 1,
  score_value_at_freeze    NUMERIC(10,4),
  score_payload_hash       TEXT,      -- SHA256 of score payload at freeze time
  finalized_at             TIMESTAMPTZ,
  published_at             TIMESTAMPTZ,
  hard_frozen_at           TIMESTAMPTZ,
  hard_freeze_trigger      TEXT,      -- event name that triggered hard freeze
  superseded_by            TEXT,      -- freeze_key of superseding record
  supersedes               TEXT,      -- freeze_key of record this supersedes
  dispute_id               UUID,
  governance_approval_id   UUID,
  mentor_override_applied  BOOLEAN NOT NULL DEFAULT FALSE,
  mentor_override_actor_id UUID,
  mentor_override_at       TIMESTAMPTZ,
  tenant_id                UUID NOT NULL REFERENCES tenants(id),
  created_at               TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE UNIQUE INDEX idx_sfr_freeze_key         ON score_freeze_registry(freeze_key);
CREATE INDEX        idx_sfr_pipeline           ON score_freeze_registry(pipeline_type, freeze_state);
CREATE INDEX        idx_sfr_score_record       ON score_freeze_registry(score_record_id);
CREATE INDEX        idx_sfr_state              ON score_freeze_registry(freeze_state, pipeline_type);
CREATE INDEX        idx_sfr_hard_frozen        ON score_freeze_registry(hard_frozen_at) WHERE hard_frozen_at IS NOT NULL;
CREATE INDEX        idx_sfr_tenant             ON score_freeze_registry(tenant_id, pipeline_type);

-- Trigger: prevent UPDATE of frozen records at DB level
CREATE OR REPLACE FUNCTION enforce_score_freeze()
RETURNS TRIGGER AS $$
BEGIN
  IF OLD.freeze_state IN ('FINALIZED', 'HARD_FROZEN') THEN
    IF NEW.score_value_at_freeze != OLD.score_value_at_freeze
    OR NEW.score_payload_hash    != OLD.score_payload_hash THEN
      RAISE EXCEPTION 'SCORE_FREEZE_VIOLATION: Cannot mutate score values of a % record. freeze_key=%',
        OLD.freeze_state, OLD.freeze_key;
    END IF;
  END IF;
  IF OLD.freeze_state = 'HARD_FROZEN' THEN
    IF NEW.freeze_state != 'HARD_FROZEN' THEN
      RAISE EXCEPTION 'SCORE_FREEZE_VIOLATION: Cannot transition out of HARD_FROZEN state. freeze_key=%',
        OLD.freeze_key;
    END IF;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER score_freeze_guard
  BEFORE UPDATE ON score_freeze_registry
  FOR EACH ROW EXECUTE FUNCTION enforce_score_freeze();
```

> The PostgreSQL trigger is the **last line of defense.**
> Even if the agent service is bypassed, the DB trigger catches the violation.
> Defense-in-depth is non-negotiable.

---

## Table: `score_freeze_audit_log`

```sql
CREATE TABLE score_freeze_audit_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  freeze_key            TEXT NOT NULL,
  pipeline_type         TEXT NOT NULL,
  score_record_id       UUID NOT NULL,
  operation_attempted   TEXT NOT NULL,
  decision              TEXT NOT NULL CHECK (decision IN (
                          'PERMITTED', 'BLOCKED', 'FINALIZED',
                          'HARD_FROZEN', 'OVERRIDE_PERMITTED',
                          'OVERRIDE_BLOCKED', 'SUPERSESSION_CREATED',
                          'INVALID_REQUEST', 'INFRASTRUCTURE_FAILURE'
                        )),
  rejection_reason      TEXT,
  freeze_state_at_time  TEXT NOT NULL,
  writer_service        TEXT NOT NULL,
  writer_actor_id       UUID,
  writer_actor_role     TEXT,
  score_value_attempted NUMERIC(10,4),
  idempotency_key       TEXT NOT NULL,
  tenant_id             UUID NOT NULL REFERENCES tenants(id),
  processed_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_sfal_freeze_key   ON score_freeze_audit_log(freeze_key, processed_at);
CREATE INDEX idx_sfal_decision     ON score_freeze_audit_log(decision, processed_at);
CREATE INDEX idx_sfal_pipeline     ON score_freeze_audit_log(pipeline_type, decision);
CREATE INDEX idx_sfal_actor        ON score_freeze_audit_log(writer_actor_id, processed_at);
CREATE INDEX idx_sfal_tenant       ON score_freeze_audit_log(tenant_id, processed_at);
```

**Immutability rule:**
- No UPDATE permitted on this table
- No DELETE permitted on this table
- Governed under R60 (Long-Term Archival Law)
- Retention: 7 years minimum (belt, royalty, innovation records have legal retention requirements)

---

## ClickHouse Analytics Table: `score_freeze_stats`

```sql
CREATE TABLE score_freeze_stats (
  stat_date                Date,
  stat_hour                UInt8,
  pipeline_type            String,
  tenant_id                UUID,
  total_requests           UInt64,
  total_permitted          UInt64,
  total_blocked            UInt64,
  total_finalized          UInt64,
  total_hard_frozen        UInt64,
  total_overrides_permitted UInt64,
  total_overrides_blocked  UInt64,
  total_supersessions      UInt64,
  total_violations         UInt64
) ENGINE = SummingMergeTree()
PARTITION BY stat_date
ORDER BY (stat_date, stat_hour, pipeline_type, tenant_id);
```

---

---

# SECTION XI — REDIS FREEZE REGISTRY (LOCKED)

## Key Schema

```
{pipeline_prefix}_score_freeze:{record_specific_id_components}
```

| Pipeline | Redis Key Pattern | TTL |
|---|---|---|
| GD Session | `gd_score_freeze:{session_id}:{participant_id}` | 30 days |
| Dojo Match | `dojo_score_freeze:{match_id}:{participant_id}` | 30 days |
| Dojo Override Window | `mentor_override_window:{match_id}:{participant_id}` | Per window table |
| Intelligence DNA | `intel_score_freeze:{user_id}:{test_session_id}` | 365 days |
| Innovation | `innovation_score_freeze:{idea_id}` | Permanent (no TTL) |
| Royalty Ledger | `royalty_freeze:{user_id}:{royalty_cycle_id}` | Permanent (no TTL) |
| Placement Match | `placement_score_freeze:{student_id}:{job_id}` | 90 days |
| Belt Eligibility | `belt_score_freeze:{user_id}:{belt_level}:{snapshot_id}` | 365 days |

**Permanent keys (no TTL):** Innovation and Royalty records — licensing and financial records have no safe expiry point.

## Redis Failure Mode

```
IF Redis unreachable:
  → FAIL-CLOSED
  → All score writes BLOCKED
  → Fall through to PostgreSQL authoritative check ONLY
  → If PostgreSQL also unreachable → BLOCK ALL score writes
  → EMIT score_freeze.infrastructure.failure (CRITICAL)
  → NO FAIL-OPEN UNDER ANY CIRCUMSTANCES
```

---

---

# SECTION XII — API INTERFACE (LOCKED)

## Check Freeze Status

**Endpoint:** `GET /internal/score-freeze/status/{freeze_key}`
**Auth:** Service-to-service JWT (Keycloak)

### Response
```json
{
  "freeze_key":   "dojo_score_freeze:match_001:participant_xyz",
  "freeze_state": "FINALIZED",
  "pipeline_type": "DOJO_MATCH",
  "finalized_at": "2026-03-04T10:32:00Z",
  "hard_frozen":  false,
  "override_window_open": false,
  "superseded_by": null
}
```

---

## Request Score Write Permission

**Endpoint:** `POST /internal/score-freeze/check-write`
**Auth:** Service-to-service JWT (Keycloak)

### Request Body
```json
{
  "pipeline_type":      "DOJO_MATCH",
  "score_record_id":    "uuid",
  "freeze_key":         "dojo_score_freeze:match_001:participant_xyz",
  "operation_type":     "FINALIZE",
  "writer_service":     "dojo-scoring-engine",
  "writer_actor_id":    "uuid",
  "writer_actor_role":  "SCORING_ENGINE",
  "score_payload_hash": "sha256-hash-of-payload",
  "score_value":        74.5,
  "idempotency_key":    "uuid",
  "timestamp":          1741084320000
}
```

### Response: PERMITTED
```json
{
  "permitted":      true,
  "operation_type": "FINALIZE",
  "new_state":      "FINALIZED",
  "freeze_key":     "dojo_score_freeze:match_001:participant_xyz"
}
```

### Response: BLOCKED
```json
{
  "permitted":     false,
  "reason":        "SCORE_RECORD_FINALIZED",
  "freeze_state":  "FINALIZED",
  "finalized_at":  "2026-03-04T10:32:00Z",
  "freeze_key":    "dojo_score_freeze:match_001:participant_xyz"
}
```

---

## Trigger Hard Freeze

**Endpoint:** `POST /internal/score-freeze/hard-freeze`
**Auth:** Service-to-service JWT (Keycloak) — RESTRICTED to Belt Engine, Royalty Engine, Governance Service

### Request Body
```json
{
  "freeze_key":         "belt_score_freeze:user_abc:GOLD:snapshot_001",
  "pipeline_type":      "BELT_ELIGIBILITY",
  "hard_freeze_trigger": "belt.certificate.generated",
  "trigger_actor_service": "belt-engine",
  "idempotency_key":    "uuid"
}
```

### Response: HARD FROZEN
```json
{
  "hard_frozen":       true,
  "freeze_key":        "belt_score_freeze:user_abc:GOLD:snapshot_001",
  "hard_frozen_at":    "2026-03-04T10:35:00Z",
  "trigger":           "belt.certificate.generated"
}
```

---

---

# SECTION XIII — EVENT BUS CONTRACTS (LOCKED)

## Events Emitted by This Agent

### `score_freeze.write.blocked`
```json
{
  "event_type": "score_freeze.write.blocked",
  "payload": {
    "freeze_key":          "string",
    "pipeline_type":       "string",
    "operation_attempted": "string",
    "rejection_reason":    "string",
    "freeze_state":        "string",
    "writer_service":      "string",
    "writer_actor_id":     "uuid",
    "idempotency_key":     "string",
    "timestamp":           "ISO8601"
  }
}
```

### `score_freeze.record.finalized`
```json
{
  "event_type": "score_freeze.record.finalized",
  "payload": {
    "freeze_key":         "string",
    "pipeline_type":      "string",
    "score_record_id":    "uuid",
    "score_value":        "number",
    "score_payload_hash": "string",
    "finalized_at":       "ISO8601",
    "tenant_id":          "uuid"
  }
}
```

### `score_freeze.record.hard_frozen`
```json
{
  "event_type": "score_freeze.record.hard_frozen",
  "payload": {
    "freeze_key":          "string",
    "pipeline_type":       "string",
    "score_record_id":     "uuid",
    "hard_freeze_trigger": "string",
    "hard_frozen_at":      "ISO8601",
    "tenant_id":           "uuid"
  }
}
```

### `score_freeze.violation.detected`
```json
{
  "event_type": "score_freeze.violation.detected",
  "payload": {
    "freeze_key":           "string",
    "pipeline_type":        "string",
    "violation_type":       "MUTATION_AFTER_FINALIZE | MUTATION_AFTER_HARD_FREEZE | INVALID_STATE_TRANSITION",
    "writer_service":       "string",
    "writer_actor_id":      "uuid",
    "writer_actor_role":    "string",
    "attempted_operation":  "string",
    "timestamp":            "ISO8601",
    "severity":             "CRITICAL"
  }
}
```

### `score_freeze.mentor.override.blocked`
```json
{
  "event_type": "score_freeze.mentor.override.blocked",
  "payload": {
    "freeze_key":      "string",
    "match_id":        "uuid",
    "participant_id":  "uuid",
    "mentor_id":       "uuid",
    "block_reason":    "WINDOW_EXPIRED | WRONG_MENTOR | CERT_REVOKED | ALREADY_HARD_FROZEN",
    "timestamp":       "ISO8601"
  }
}
```

### `score_freeze.supersession.created`
```json
{
  "event_type": "score_freeze.supersession.created",
  "payload": {
    "original_freeze_key":   "string",
    "superseding_freeze_key": "string",
    "dispute_id":            "uuid",
    "governance_approval_id": "uuid",
    "pipeline_type":         "string",
    "timestamp":             "ISO8601"
  }
}
```

### `score_freeze.infrastructure.failure`
```json
{
  "event_type": "score_freeze.infrastructure.failure",
  "payload": {
    "redis_reachable":    false,
    "postgres_reachable": false,
    "failover_mode":      "FAIL_CLOSED_ALL_WRITES_BLOCKED",
    "affected_pipelines": ["array"],
    "timestamp":          "ISO8601",
    "severity":           "CRITICAL"
  }
}
```

---

---

# SECTION XIV — INTEGRATION WIRING (MANDATORY)

## Services That MUST Call This Agent Before Every Score Write

| Service | Operation | Must Call | Check Type |
|---|---|---|---|
| GD Orchestrator | Submit participant score | `check-write` | PERMIT or BLOCK |
| GD Orchestrator | Finalize session score | `check-write` (FINALIZE op) | PERMIT or BLOCK |
| Dojo Match Engine | Submit peer/self/mentor score | `check-write` | PERMIT or BLOCK |
| Dojo Match Engine | Finalize merged score | `check-write` (FINALIZE op) | PERMIT or BLOCK |
| Scoring Engine | Write merged score record | `check-write` | PERMIT or BLOCK |
| Scoring Engine | Mentor override application | `check-write` (OVERRIDE op) | PERMIT or BLOCK |
| Belt Engine | Write eligibility snapshot | `check-write` (FINALIZE op) | PERMIT or BLOCK |
| Belt Engine | Post-promotion hard freeze | `hard-freeze` | EXECUTE |
| Belt Engine | Post-certificate hard freeze | `hard-freeze` | EXECUTE |
| Certification Engine | Post-cert hard freeze | `hard-freeze` | EXECUTE |
| Intelligence Testing Service | Finalize DNA vector | `check-write` (FINALIZE op) | PERMIT or BLOCK |
| Intelligence Testing Service | Publish DNA vector | `check-write` | PERMIT or BLOCK |
| Innovation Scoring Engine | Finalize innovation score | `check-write` (FINALIZE op) | PERMIT or BLOCK |
| Innovation Scoring Engine | Pre-licensing hard freeze | `hard-freeze` | EXECUTE |
| Royalty Accounting Engine | Write ledger entry | `check-write` (FINALIZE op) | PERMIT or BLOCK |
| Royalty Accounting Engine | Post-payout hard freeze | `hard-freeze` | EXECUTE |
| Royalty Accounting Engine | Post-audit hard freeze | `hard-freeze` | EXECUTE |
| Placement Score Service | Post-display hard freeze | `hard-freeze` | EXECUTE |
| Admin Governance Service | Supersession approval | `supersession` protocol | GOVERNED |

**Services that write to any score record WITHOUT calling this agent first:**
→ **WIRING VIOLATION** → **STOP EXECUTION**

---

---

# SECTION XV — OBSERVABILITY (NON-OPTIONAL)

## Prometheus Metrics

```
# HELP score_freeze_write_requests_total Total score write requests received
# TYPE score_freeze_write_requests_total counter
score_freeze_write_requests_total{pipeline_type, operation_type, decision, tenant_id}

# HELP score_freeze_violations_total Total freeze violations detected
# TYPE score_freeze_violations_total counter
score_freeze_violations_total{pipeline_type, violation_type, writer_service}

# HELP score_freeze_active_frozen_records Current frozen record count
# TYPE score_freeze_active_frozen_records gauge
score_freeze_active_frozen_records{pipeline_type, freeze_state}

# HELP score_freeze_hard_frozen_total Total hard-frozen records (cumulative)
# TYPE score_freeze_hard_frozen_total counter
score_freeze_hard_frozen_total{pipeline_type}

# HELP score_freeze_override_requests_total Mentor override requests
# TYPE score_freeze_override_requests_total counter
score_freeze_override_requests_total{decision, block_reason}

# HELP score_freeze_supersessions_total Supersession records created
# TYPE score_freeze_supersessions_total counter
score_freeze_supersessions_total{pipeline_type}

# HELP score_freeze_check_latency_ms Agent check-write latency
# TYPE score_freeze_check_latency_ms histogram
score_freeze_check_latency_ms{pipeline_type}
  buckets: [1, 2, 5, 10, 25, 50, 100, 250]

# HELP score_freeze_redis_failures_total Redis check failures
# TYPE score_freeze_redis_failures_total counter
score_freeze_redis_failures_total{}
```

## Grafana Dashboards (Mandatory)

- **Freeze Violation Detector** — Real-time alert on any `score_freeze_violations_total > 0`
- **Pipeline Freeze State Distribution** — Pie per pipeline showing DRAFT/PENDING/FINALIZED/HARD_FROZEN counts
- **Mentor Override Activity** — Override requests vs. permitted vs. blocked per day
- **Hard Freeze Event Timeline** — Chronological list of hard freeze triggers with actor
- **Supersession Tracker** — All active supersessions with dispute linkage
- **Agent Latency P99** — Check-write latency per pipeline; alert if > 25ms
- **Redis Failure Timeline** — Immediate CRITICAL visibility on fail-closed activations

## Alerting Rules (All Mandatory)

| Alert | Condition | Severity | Required Action |
|---|---|---|---|
| `ScoreFreezeViolation` | `violations_total > 0` any 1m window | CRITICAL | Immediate audit — identify offending service |
| `ScoreFreezeRoyaltyMutation` | `ROYALTY_LEDGER violation > 0` | CRITICAL | Financial audit required |
| `ScoreFreezeBeltMutation` | `BELT_ELIGIBILITY violation > 0` | CRITICAL | Certification integrity review |
| `ScoreFreezeInnovationMutation` | `INNOVATION violation > 0` | CRITICAL | Legal review — licensing contract risk |
| `ScoreFreezeRedisDown` | `redis_failures > 0` for 10s | CRITICAL | Page on-call immediately |
| `ScoreFreezeMentorBlockSpike` | `override BLOCKED > 5` in 10m | HIGH | Review mentor workflow |
| `ScoreFreezeHighLatency` | `P99 > 50ms` sustained 3m | MEDIUM | Redis/PostgreSQL health check |

---

---

# SECTION XVI — SECURITY CONSTRAINTS (LOCKED)

- `score_freeze_audit_log` is **immutable** — no UPDATE, no DELETE, ever
- `score_freeze_registry` score values are **protected by PostgreSQL trigger** — DB-level enforcement independent of agent
- `score_payload_hash` is SHA256 of the score payload at freeze time — stored for tamper detection
- The `hard-freeze` endpoint is **restricted** — only Belt Engine, Royalty Engine, Governance Service, and Innovation Engine may call it (enforced via Keycloak service roles)
- Supersession requires `governance_board_approval_id` — no self-approved supersession
- Redis freeze keys for Innovation and Royalty records have **no TTL** — permanent keys by design
- All `/internal/score-freeze/*` endpoints are **not exposed externally** — Kong blocks all external access
- `score_freeze_violation.detected` events are routed to **Wazuh SIEM** for security audit trail

---

---

# SECTION XVII — DEPLOYMENT SPECIFICATION (LOCKED)

## Kubernetes Namespace

```
Namespace: realtime
Service Name: phone-score-freeze-agent
```

## Resource Defaults (Startup-Stage)

```yaml
resources:
  requests:
    cpu: "100m"
    memory: "128Mi"
  limits:
    cpu: "500m"
    memory: "384Mi"

replicas: 2
autoscaling:
  minReplicas: 2
  maxReplicas: 8
  targetCPUUtilizationPercentage: 60
```

## Health Check Endpoints

```
GET /health/live   → 200 OK if process alive
GET /health/ready  → 200 OK if Redis reachable AND PostgreSQL reachable
```

**Both Redis AND PostgreSQL must be reachable for `/health/ready` to return 200.**
A single-store failure makes this service unready — fail-closed requires both stores.

## Environment Variables Required

```
REDIS_HOST
REDIS_PORT
REDIS_PASSWORD
POSTGRES_HOST
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
KAFKA_BROKER_URL
KEYCLOAK_JWKS_URL
LOG_LEVEL
SCORE_FREEZE_FAIL_MODE             # MUST be "FAIL_CLOSED"
MENTOR_OVERRIDE_WINDOW_RANKED_SEC  # Default: 1800 (30 min)
MENTOR_OVERRIDE_WINDOW_CERT_SEC    # Default: 3600 (60 min)
MENTOR_OVERRIDE_WINDOW_TOURNEY_SEC # Default: 900  (15 min)
MENTOR_OVERRIDE_WINDOW_DISPUTE_SEC # Default: 259200 (72 hrs)
```

---

---

# SECTION XVIII — FAILURE SCENARIOS & SYSTEM RESPONSES (LOCKED)

| Failure Scenario | Agent Response |
|---|---|
| Redis unreachable — PostgreSQL reachable | Fall through to PostgreSQL check; continue if DB permits |
| Redis unreachable — PostgreSQL unreachable | FAIL-CLOSED — block ALL score writes — CRITICAL alert |
| PostgreSQL trigger fires on direct DB write attempt bypassing agent | Exception raised — write aborted — DB-level defense triggered |
| Mentor override after window expiry | BLOCKED with `MENTOR_OVERRIDE_WINDOW_EXPIRED` — audit logged |
| Mentor override by wrong mentor | BLOCKED with `WRONG_MENTOR_OVERRIDE` — audit logged — security alert |
| Belt engine attempts to re-finalize already FINALIZED snapshot | BLOCKED — score_freeze.violation.detected emitted |
| Royalty recalculation attempts to alter a HARD_FROZEN ledger entry | BLOCKED — `MUTATION_AFTER_HARD_FREEZE` — financial audit alert |
| Innovation score re-submitted after licensing started | BLOCKED — Hard freeze already set at `licensing.negotiation.started` |
| Supersession attempted without governance approval ID | BLOCKED — `SUPERSESSION_REQUIRES_GOVERNANCE_APPROVAL` |
| Supersession attempted on HARD_FROZEN belt certificate record | PERMANENTLY BLOCKED — `HARD_FROZEN_NO_SUPERSESSION` |
| Second mentor override on same record | BLOCKED — `OVERRIDE_ALREADY_APPLIED_ON_RECORD` |
| GD session analytics pipeline re-writes a published score | BLOCKED — state = PUBLISHED — operation type mismatch — violation emitted |
| Service bypasses agent and writes directly to PostgreSQL | PostgreSQL trigger fires — exception raised — write fails — alert |

---

---

# SECTION XIX — INTERN EXECUTION GUIDE

## Service Location

```
/backend/services/phone_score_freeze_agent/
```

## Step-by-Step Local Execution

**Step 1:** Navigate to service folder
```bash
cd /backend/services/phone_score_freeze_agent/
```

**Step 2:** Create virtual environment
```bash
python -m venv venv
source venv/bin/activate   # Mac/Linux
venv\Scripts\activate      # Windows
```

**Step 3:** Install dependencies
```bash
pip install -r requirements.txt
```

**Step 4:** Copy env template
```bash
cp .env.example .env
# Edit .env with local Redis and DB values
# Set SCORE_FREEZE_FAIL_MODE=FAIL_CLOSED
```

**Step 5:** Run database migration (creates tables + installs trigger)
```bash
alembic upgrade head
```

**Expected migration output:**
```
Running upgrade -> head
  Create table score_freeze_registry ... OK
  Create table score_freeze_audit_log ... OK
  Install trigger enforce_score_freeze ... OK
```

**Step 6:** Start the service
```bash
uvicorn main:app --reload --port 8092
```

**Expected Output:**
```
INFO: Uvicorn running on http://127.0.0.1:8092
INFO: PHONE_SCORE_FREEZE_AGENT ready
INFO: Redis connection: OK
INFO: PostgreSQL connection: OK
INFO: PostgreSQL freeze trigger: INSTALLED
INFO: Fail mode: FAIL_CLOSED
```

**Step 7:** Test via Swagger
```
http://127.0.0.1:8092/docs
```

**Step 8:** Verify freeze trigger works at DB level
```bash
# Connect to PostgreSQL directly and try to mutate a FINALIZED record
# Expected: Exception raised — "SCORE_FREEZE_VIOLATION: Cannot mutate score values..."
psql -U ecoskiller -d ecoskiller -c "
  UPDATE score_freeze_registry
  SET score_value_at_freeze = 99.0
  WHERE freeze_state = 'FINALIZED'
  LIMIT 1;
"
```

**Expected Output:**
```
ERROR:  SCORE_FREEZE_VIOLATION: Cannot mutate score values of a FINALIZED record.
```

**If mutation succeeds → CRITICAL BUG → STOP EXECUTION**

**Failure at any step → STOP EXECUTION**

---

---

# SECTION XX — CONTRACT GATE STATUS

| Contract | Status |
|---|---|
| Score Pipeline Taxonomy | LOCKED |
| Freeze State Machine | LOCKED |
| State Transition Rules | LOCKED |
| Mentor Override Window Policy | LOCKED |
| Supersession Protocol | LOCKED |
| Freeze Key Schema per Pipeline | LOCKED |
| Redis Freeze Registry | LOCKED |
| PostgreSQL Freeze Trigger | LOCKED |
| Audit Log Schema | LOCKED |
| API Interface Contract | LOCKED |
| Event Bus Contracts | LOCKED |
| Service Integration Wiring | LOCKED |
| Observability Metrics + Alerts | LOCKED |
| Security Constraints | LOCKED |
| Fail-Closed Mode Declaration | LOCKED |
| Deployment Specification | LOCKED |

**➤ ALL CONTRACT GATES PASSED**
**➤ AGENT STATUS: SEALED · LOCKED · DEPLOYMENT-READY**

---

---

# SECTION XXI — BOUND LAWS & CROSS-REFERENCES

| Law / Spec | Binding Clause |
|---|---|
| R1 — Technology Stack Lock | Redis 7, PostgreSQL 15, FastAPI enforced; Wazuh SIEM wired for violation events |
| R4 — Event Schema Registry | All emitted events registered in AsyncAPI before deployment |
| R5 — Workflow State Machines | Freeze state machine is a governed R5 workflow state machine |
| R9 — Scoring Engine (LOCKED) | Weighted peer + mentor merge is freeze-protected at FINALIZED state |
| R10 — Certification & Belt Engine | Belt eligibility snapshot and belt certificate trigger hard freeze |
| R38 — Master Bug Registry | All freeze violation scenarios pre-registered in bug registry |
| R39A — Session Lifecycle Manager | Score lifecycle is a sub-lifecycle governed by this agent |
| R44 — Runnable Backend Law | Agent fully runnable with Redis + PostgreSQL + trigger installed |
| R49 — Contract Validator CLI | All contracts validated before every build |
| R60 — Archival Law | Audit log under WORM-style 7-year retention; royalty records permanent |
| Dojo SAAS v1.0 — Section F (Scoring Governance Lock) | Weighted metric model, peer+mentor+self merge, variance detection — all freeze-governed |
| Dojo SAAS v1.0 — Section G (Belt & Certification Lock) | Belt promotion is HARD FREEZE trigger — no re-evaluation after promotion |
| Dojo SAAS v1.0 — Section T2 (Scoring Validity) | Confidence scores and normalization curves — frozen at finalization |
| Dojo SAAS v1.0 — Section T9 (Collusion Detection) | Flagged matches held in PENDING_REVIEW — freeze proceeds only after audit clears |
| Dojo SAAS v1.0 — Section T16 (Appeals Board) | Supersession protocol governed by governance board approval |
| Dojo SAAS v1.0 — Section T17 (Belt Version Governance) | Belt version tag written at freeze time — immutable in frozen record |
| GD System Spec (Section 12 — Scoring Rule-Based) | GD scores reproducible and auditable — guaranteed by this agent's finalization |
| Ecoskiller Services — Section XII (Innovation Scoring Engine) | Innovation score freeze triggered on marketplace listing and licensing start |
| Ecoskiller Services — Section XIII (Royalty Accounting Engine) | Royalty ledger entries hard-frozen on payout trigger and audit completion |
| Ecoskiller Services — Section XIV (Fraud Detection Engine) | Flagged suspicious scores enter PENDING_REVIEW — freeze suspended pending fraud check |
| Society Architecture — Commission & Finance Layer | Commission records frozen after 7-day Temporal payout workflow completes |
| PHONE_EVENT_DEDUP_AGENT v1.0 | Companion: prevents duplicate score events before they reach this agent |
| PHONE_RACE_CONDITION_GUARD v1.0 | Companion: prevents concurrent score writes before they reach this agent |
| Infrastructure Audit v8 | PostgreSQL on k3s — trigger-level defense verified against self-hosted stack |

---

---

# SECTION XXII — ANTIGRAVITY LAYER DECLARATION (LOCKED)

## Why Score Freeze Is the Third Pillar

The first two agents protect the **moment of write.**
This agent protects **all moments after the first write.**

```
TIME AXIS OF A SCORE RECORD LIFECYCLE:

  T0: Score computation begins
      ← DEDUP AGENT guards (no duplicate events reach here)

  T1: First write attempt
      ← RACE GUARD guards (no concurrent writes to same record)

  T2: Score written successfully (DRAFT state)
      ← SCORE FREEZE AGENT begins governing

  T3: Score reviewed, merged, variance-checked (PENDING_REVIEW)

  T4: Mentor override window (DOJO: PENDING_MENTOR_REVIEW)
      ← SCORE FREEZE AGENT enforces window boundary

  T5: Score FINALIZED
      ← SCORE FREEZE AGENT blocks all mutation attempts from here

  T6: Score PUBLISHED to downstream consumers

  T7: Score used in belt / royalty / certification decision
      ← SCORE FREEZE AGENT triggers HARD FREEZE (permanent)

  T8 → T∞: Record exists permanently frozen in audit chain
      ← PostgreSQL trigger is the immortal last defense
```

Without this agent, a score record at T5 or beyond is vulnerable to:
- Service bugs that re-write score values
- Mentor actors operating outside their window
- Analytics pipelines that enrich and accidentally overwrite
- Royalty recalculations that drift into finalized figures
- Belt engines that re-evaluate certificates already issued

The Score Freeze Agent makes the ECOSKILLER scoring pipeline **legally defensible, audit-complete, and corruption-proof.**

---

---

# 🔒 FINAL SEAL

```
AGENT:       PHONE_SCORE_FREEZE_AGENT
DOMAIN:      Session & Lifecycle → Antigravity Layer
SYSTEM:      ECOSKILLER v12.0
STATUS:      SEALED
VERSION:     v1.0
COMPANIONS:  PHONE_EVENT_DEDUPLICATION_AGENT v1.0
             PHONE_RACE_CONDITION_GUARD_AGENT v1.0
MUTATION:    ADD-ONLY VIA VERSION BUMP
AUTHORITY:   HUMAN DECLARATION ONLY

NO CLAUSE MAY BE WEAKENED.
NO CLAUSE MAY BE REINTERPRETED.
NO PARTIAL IMPLEMENTATION PERMITTED.
FAIL-CLOSED MODE IS NON-NEGOTIABLE.
POSTGRESQL TRIGGER INSTALLATION IS MANDATORY.
HARD-FROZEN RECORDS ARE PERMANENTLY IMMUTABLE.
SUPERSESSION OF HARD_FROZEN RECORDS IS PERMANENTLY BLOCKED.

Violation → STOP EXECUTION → REPORT SCORE_FREEZE_SEAL_BREACH
```

---

*PHONE_SCORE_FREEZE_AGENT · ECOSKILLER Session & Lifecycle · Antigravity Layer · v1.0 · SEALED*
