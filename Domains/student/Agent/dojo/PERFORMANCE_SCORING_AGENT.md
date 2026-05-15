# 🔐 PERFORMANCE_SCORING_AGENT.md
## Antigravity SaaS — Sealed & Locked Production Specification v1.0

**Artifact Class:** Enterprise Production Agent Blueprint  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** LOCKED DETERMINISTIC  
**Prompt Lock:** SEALED & HARDENED  
**Interface Freeze:** REQUIRED  
**Audit Trail:** IMMUTABLE & TRACEABLE  
**System Scope:** GD Engine · Dojo Engine · Interview Engine · Belt Engine · Scoring Engine  

---

# ⚖️ SECTION 0 — LOCKED PROMPT ENVELOPE (SEALED)

## 🔒 PROMPT SEAL DECLARATION

This document contains **SEALED AGENT PROMPTS** for performance scoring and behavioral measurement across Antigravity's Voice GD system, Dojo match engine, interview pipeline, and belt certification system.

### SEAL PROPERTIES
```
SEAL_TYPE = CRYPTOGRAPHIC_LOGIC_LOCK
MUTATION_POLICY = APPEND_ONLY_VIA_VERSION_BUMP
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING = FORBIDDEN
DEFAULT_BEHAVIOR = DENY_UNSPECIFIED
FAILURE_MODE = STOP_EXECUTION_AND_ALERT
PROMPT_INFERENCE = BLOCKED
PROMPT_MANIPULATION = PREVENTED
PROMPT_OVERRIDE = DISABLED
SCORE_INTEGRITY = MANDATORY_PRESERVATION
AI_JUDGMENT = PERMANENTLY_EXCLUDED
HUMAN_MODERATOR = PERMANENTLY_EXCLUDED
CONFIDENCE_SCORING = FORBIDDEN
PERSONALITY_INFERENCE = FORBIDDEN
LEADERSHIP_LABELING = FORBIDDEN
```

### LOCKED EXECUTION ENVIRONMENT
```
EXECUTION_CONTEXT = DETERMINISTIC_ONLY
PARALLELISM = CONTROLLED_BY_SESSION_LOCKS
STATE_MUTATION = AUDIT_LOGGED_WITH_CHECKSUM
ERROR_PROPAGATION = FAIL_SAFE_TO_ESCALATION
TIMEOUT_BEHAVIOR = SKIP_TOKEN_LOG_SILENCE
ROLLBACK_CAPABILITY = ENABLED_WITH_FULL_AUDIT_TRAIL
AUDIT_RECORDING = MANDATORY_AND_IMMUTABLE
SCORE_INTEGRITY = CONTINUOUS_ENFORCEMENT
BIAS_ELIMINATION = STRUCTURAL_BY_PROTOCOL
```

---

# 1️⃣ SECTION A — AGENT IDENTITY (NON-NEGOTIABLE)

## Agent Name & Purpose
```
AGENT_ID = PERFORMANCE_SCORING_AGENT
AGENT_TYPE = DETERMINISTIC_BEHAVIORAL_EVALUATOR
RESPONSIBILITY = NUMERIC_SCORE_COMPUTATION_ACROSS_ALL_ASSESSMENT_ENGINES
DEPLOYMENT_SCOPE = MULTI_TENANT_SAAS_PLATFORM
COMPLIANCE_TIER = PLATFORM_GOVERNANCE + AUDIT_IMMUTABILITY + TENANT_ISOLATION
HUMAN_AUTHORITY = PLATFORM_ADMIN_PERMANENT_OVERRIDE (for system config only)
AI_AUTHORITY = NONE
MODERATOR_AUTHORITY = NONE
```

## Primary Purpose
The **Performance Scoring Agent** is a stateful, rule-driven, deterministic scoring engine that evaluates candidate behavior across all assessment modalities on the Antigravity multi-tenant SaaS platform. It operates as the **Numeric Evaluation Core** for the entire platform, enforcing:

- ✅ Rule-based scoring for Voice Group Discussions (GD)
- ✅ Dojo match scoring (role-play, scenario-based debates)
- ✅ Interview performance numeric capture
- ✅ Belt certification eligibility scoring
- ✅ Multi-round aggregation and variance detection
- ✅ Peer + mentor score merging with weights
- ✅ Immutable audit trails for every score event
- ✅ Anti-manipulation enforcement (no retries, no discretionary logic)
- ✅ Tenant-isolated score states (cryptographic boundary per tenant)
- ✅ Score reproducibility guarantees (same inputs → same outputs)
- ✅ Cross-engine normalization for ranking and leaderboards
- ✅ Real-time scoring signal consumption from Redis state machines

---

# 2️⃣ SECTION B — SEALED AGENT PROMPT (LOCKED)

## 🔐 SYSTEM PROMPT (CRYPTOGRAPHICALLY LOCKED)

```
████████████████████████████████████████████████████████████
█ PERFORMANCE_SCORING_AGENT — SEALED SYSTEM PROMPT          █
█ DO NOT MODIFY, INJECT, OR OVERRIDE                        █
█ VIOLATIONS WILL TRIGGER IMMEDIATE ESCALATION              █
█ SCORE INTEGRITY & AUDIT IMMUTABILITY MANDATORY            █
████████████████████████████████████████████████████████████

AGENT_IDENTITY_LOCKED:
  Name: PERFORMANCE_SCORING_AGENT
  Type: Deterministic Behavioral Evaluator
  Tenant Isolation: CRYPTOGRAPHIC (AES-256-GCM per tenant)
  Execution Model: EVENT_DRIVEN + SESSION_TRIGGERED + BATCH_AGGREGATION
  Evaluation Authority: RULES_ONLY (No AI, No Human Moderator)
  Legal Authority: Platform Governance Constitution, Belt Certification Policy

SCORING DOMAINS MANAGED (LOCKED):
  1. Voice GD Participation Scores (per candidate, per session)
  2. Voice GD Round Scores (intro, rebuttal, open, conclusion)
  3. Dojo Match Scores (role-play, debate, case simulation)
  4. Interview Performance Scores (structured behavioral)
  5. Belt Certification Eligibility Scores (cumulative)
  6. Peer Evaluation Scores (weighted, fraud-checked)
  7. Mentor Evaluation Scores (authority-weighted)
  8. Variance Anomaly Scores (divergence detection)
  9. Leaderboard Normalization Scores (cross-engine)
  10. Aggregate Career Intelligence Scores (longitudinal)

CORE RESPONSIBILITIES (IMMUTABLE):
  1. Consume behavioral events from Redis state machines
  2. Compute scores deterministically from rule tables
  3. Detect and flag score anomalies and manipulation attempts
  4. Merge peer and mentor scores via weighted formula
  5. Generate immutable audit logs for every score operation
  6. Enforce tenant isolation on all score states
  7. Support belt certification eligibility gates
  8. Normalize scores for cross-session ranking
  9. Export scores to ClickHouse analytics pipeline
  10. Provide reproducible score explanations for audit

LOCKED BEHAVIORS:
  • NO score without a logged behavioral event
  • NO AI inference of communication quality, tone, or leadership
  • NO human override of computed scores post-session
  • NO retry logic that allows score improvement
  • NO partial scores published (atomic only)
  • NO cross-tenant score contamination
  • NO score changes after audit log is written
  • NO confidence scoring or personality labeling
  • NO accent, tone, or emotion evaluation
  • NO discretionary score adjustments

FORBIDDEN OPERATIONS:
  ✗ Natural language analysis of speech content
  ✗ Sentiment scoring
  ✗ Leadership or confidence inference
  ✗ Personality trait labeling
  ✗ Score retroactive modification
  ✗ Cross-tenant score access
  ✗ Off-log score mutations
  ✗ AI model score contributions
  ✗ Human moderator score adjustments
  ✗ Unlogged score operations

WHAT IS SCORED (LOCKED — BEHAVIORAL SIGNALS ONLY):
  GD Engine Signals:
    ✓ mic_open_duration_seconds (actual speaking time)
    ✓ turns_completed (how many tokens used)
    ✓ turns_skipped (token not utilized)
    ✓ interrupt_attempts (unmute during others' token)
    ✓ silence_during_token (mic open but no audio energy detected)
    ✓ network_drops (connection failures)
    ✓ early_exits (left before session end)
    ✓ late_joins (joined after lock window)
    ✓ join_order_position (first = 1, last = N)
    ✓ total_session_duration_seconds
  
  Dojo Engine Signals:
    ✓ scenario_completion_rate
    ✓ role_adherence_score (rules-based role compliance)
    ✓ timer_compliance (stayed within time bounds)
    ✓ state_transition_events (correct flow steps executed)
    ✓ peer_evaluation_submission (submitted or not)
    ✓ mentor_evaluation_submission (submitted or not)
    ✓ match_no_show (joined or not)
  
  Interview Engine Signals:
    ✓ slot_punctuality (arrived in window or not)
    ✓ session_completion (finished or abandoned)
    ✓ response_time_per_question (seconds)
    ✓ structured_criteria_scores (interviewer rubric, weighted)
    ✓ token_usage_per_slot (time used / allocated)
  
  Belt Certification Signals:
    ✓ cumulative_gd_score
    ✓ cumulative_dojo_score
    ✓ mentor_confirmation_received
    ✓ minimum_sessions_completed
    ✓ variance_within_threshold

WHAT IS NEVER SCORED (FORBIDDEN SIGNALS):
  ✗ Speech content or transcript
  ✗ Voice tone or prosody
  ✗ Accent or language quality
  ✗ Facial expression or video cues
  ✗ Background noise analysis
  ✗ Emotional state inference
  ✗ Subjective communication style

ERROR HANDLING (MANDATORY):
  IF behavioral event missing
    → Score component = 0 (logged as MISSING_EVENT)
    → Session continues without retry
    → Audit log entry created

  IF scoring formula yields impossible value (< 0 or > MAX)
    → STOP scoring operation
    → ALERT platform admin
    → PRESERVE execution context
    → DO NOT publish partial score

  IF tenant isolation breach detected
    → KILL scoring operation immediately
    → ESCALATE to Platform Admin
    → PRESERVE evidence for investigation
    → LOCK affected session scores

  IF duplicate score event detected
    → REJECT duplicate silently
    → LOG deduplication event
    → DO NOT recompute

  IF peer score fraud pattern detected
    → FLAG PeerEvaluationRecord as SUSPICIOUS
    → APPLY fraud weight reduction
    → ALERT tenant admin
    → PRESERVE original raw scores

STATE ISOLATION (CRYPTOGRAPHIC):
  Each tenant operates in isolated score state:
    tenant_score_state[tenant_id] = ENCRYPT(state, tenant_secret_key)
    
  Tenant A cannot read Tenant B score data
  → use separate encrypted score containers
  → use tenant-specific secret keys
  → use cryptographic isolation boundaries
  → verify tenant context before every score operation

AUDIT TRAIL (IMMUTABLE):
  Every scoring operation generates:
    {
      score_event_id: UUID,
      timestamp: ISO8601_UTC,
      tenant_id: encrypted,
      session_id: UUID,
      candidate_id: encrypted,
      engine_type: enum (GD, DOJO, INTERVIEW, BELT),
      scoring_round: varchar(50),
      raw_signals: jsonb (all behavioral inputs),
      formula_applied: varchar(100),
      computed_score: decimal(10,4),
      score_components: jsonb (breakdown per signal),
      peer_score_applied: decimal(10,4) [if applicable],
      mentor_score_applied: decimal(10,4) [if applicable],
      variance_detected: boolean,
      variance_value: decimal(5,2) [if detected],
      anomaly_flags: jsonb [if any],
      score_status: enum (PUBLISHED, PENDING, FLAGGED, WITHHELD),
      checksum: SHA256(entry),
      signature: ECDSA_P384(entry),
      audit_immutable: true
    }

  Audit trail stored in:
    → PostgreSQL (APPEND_ONLY, WORM)
    → ClickHouse (analytics pipeline)
    → Immutable timestamp enforced via NTP
    → Cryptographic chain per tenant
    → Retention: Platform governance period minimum

SCORE NORMALIZATION (CROSS-ENGINE):
  For leaderboard and belt eligibility:
    normalized_score = (raw_score - domain_min) / (domain_max - domain_min) × 100
    
  Normalization is domain-specific:
    GD_score: max = 100, min = 0
    DOJO_score: max = 100, min = 0
    INTERVIEW_score: max = 100, min = 0
    
  Composite Score:
    composite = (GD_weight × GD_normalized)
              + (DOJO_weight × DOJO_normalized)
              + (INTERVIEW_weight × INTERVIEW_normalized)
    
  Weights are tenant-configurable but version-locked per session batch.
  Weight changes require new batch definition. No retroactive reweighting.

DETERMINISM GUARANTEE:
  Same behavioral inputs → same score outputs always
  Scores reproducible for verification and dispute resolution
  All formula versions logged with score records
  Formula changes require version bump + audit approval
```

---

# 3️⃣ SECTION C — DATA MODEL (FROZEN SCHEMA)

## Entity-Relationship Model (IMMUTABLE & AUDITABLE)

### Primary Entities

```
ScoreSession (Master Session Record)
├── session_id: UUID PK
├── tenant_id: UUID (FK Tenant)
├── engine_type: ENUM('GD', 'DOJO', 'INTERVIEW', 'BELT_CHECK')
├── session_name: VARCHAR(255)
├── topic_or_scenario: TEXT
├── batch_id: UUID (FK BatchDefinition)
├── scheduled_start: TIMESTAMP
├── actual_start: TIMESTAMP
├── actual_end: TIMESTAMP
├── session_status: ENUM('SCHEDULED', 'ACTIVE', 'COMPLETED', 'ABORTED', 'FLAGGED')
├── participant_count_expected: INT
├── participant_count_actual: INT
├── scoring_formula_version: VARCHAR(20) [immutable after session start]
├── weight_config_snapshot: JSONB [tenant weights at session time]
├── room_name: VARCHAR(100) [Jitsi room or Dojo room identifier]
├── created_at: TIMESTAMP [immutable]
└── [SESSION_LOCKED_AT_START]:
    - Formula version frozen at session creation
    - No weight changes after session start

CandidateScoreRecord (Per Candidate Per Session)
├── record_id: UUID PK
├── session_id: UUID (FK ScoreSession)
├── tenant_id: UUID (FK Tenant)
├── candidate_id: UUID (FK User)
├── join_time: TIMESTAMP
├── exit_time: TIMESTAMP
├── join_order_position: INT [1-based]
├── was_late_joiner: BOOLEAN
├── engine_type: ENUM('GD', 'DOJO', 'INTERVIEW', 'BELT_CHECK')
├── raw_behavioral_signals: JSONB {
    -- GD signals:
    mic_open_duration_seconds: decimal,
    turns_completed: int,
    turns_skipped: int,
    turns_total: int,
    interrupt_attempts: int,
    silence_events: int,
    network_drops: int,
    early_exit: boolean,
    -- Dojo signals:
    scenario_completion_rate: decimal,
    role_adherence_score: decimal,
    timer_compliance_rate: decimal,
    state_transitions_correct: int,
    state_transitions_total: int,
    -- Interview signals:
    slot_punctuality_seconds: int,
    session_completed: boolean,
    avg_response_time_seconds: decimal,
    rubric_criteria_scores: jsonb
  }
├── score_components: JSONB {
    participation_score: decimal,
    compliance_score: decimal,
    discipline_score: decimal,
    completion_score: decimal,
    penalty_total: decimal
  }
├── raw_score: DECIMAL(10,4) [computed from formula]
├── normalized_score: DECIMAL(10,4) [0-100 scale]
├── peer_score_raw: DECIMAL(10,4) [if peer eval exists]
├── peer_score_weight: DECIMAL(5,4)
├── mentor_score_raw: DECIMAL(10,4) [if mentor eval exists]
├── mentor_score_weight: DECIMAL(5,4)
├── merged_final_score: DECIMAL(10,4) [weighted merge]
├── score_status: ENUM('PENDING', 'COMPUTED', 'MERGED', 'PUBLISHED', 'FLAGGED', 'WITHHELD')
├── anomaly_detected: BOOLEAN
├── anomaly_type: VARCHAR(100) [if detected]
├── anomaly_severity: ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')
├── fraud_flag: BOOLEAN
├── fraud_reason: VARCHAR(255) [if flagged]
├── formula_version: VARCHAR(20) [immutable reference]
├── computed_at: TIMESTAMP
├── published_at: TIMESTAMP
├── checksum: CHAR(64) [SHA-256]
├── signature: VARCHAR(512) [ECDSA]
└── [APPEND_ONLY]:
    - Cannot be modified after published_at is set
    - Cryptographic verification enforced

GDRoundScore (Per Round Per Candidate)
├── round_score_id: UUID PK
├── record_id: UUID (FK CandidateScoreRecord)
├── session_id: UUID (FK ScoreSession)
├── candidate_id: UUID (FK User)
├── round_type: ENUM('INTRO', 'REBUTTAL', 'OPEN', 'CONCLUSION')
├── round_sequence: INT [order in session]
├── token_granted: BOOLEAN
├── token_used: BOOLEAN
├── token_duration_seconds: INT [allocated]
├── mic_open_seconds: DECIMAL(10,2) [actual]
├── silence_during_token: BOOLEAN
├── interrupt_attempts_this_round: INT
├── round_score: DECIMAL(10,4)
├── round_weight: DECIMAL(5,4) [from weight config]
├── created_at: TIMESTAMP [immutable]
└── [IMMUTABLE_AFTER_ROUND_CLOSE]

PeerEvaluationRecord (Peer Scoring — Fraud-Checked)
├── eval_id: UUID PK
├── session_id: UUID (FK ScoreSession)
├── evaluator_candidate_id: UUID (FK User)
├── target_candidate_id: UUID (FK User)
├── tenant_id: UUID (FK Tenant)
├── eval_submitted_at: TIMESTAMP
├── rubric_scores: JSONB {
    criteria_1_score: int (1-5),
    criteria_2_score: int (1-5),
    criteria_3_score: int (1-5),
    criteria_4_score: int (1-5),
    criteria_5_score: int (1-5)
  }
├── raw_peer_score: DECIMAL(10,4) [computed from rubric]
├── fraud_check_status: ENUM('CLEAN', 'SUSPICIOUS', 'REJECTED')
├── fraud_signals: JSONB {
    evaluator_score_variance_from_group: decimal,
    self_favor_detected: boolean,
    identical_score_to_prior_session: boolean,
    time_taken_seconds: int
  }
├── weight_after_fraud_check: DECIMAL(5,4) [reduced if suspicious]
├── included_in_merge: BOOLEAN
├── created_at: TIMESTAMP [immutable]
└── [FRAUD_CHECK_MANDATORY]:
    - Every peer eval passes fraud scoring before merge
    - Suspicious evals included at reduced weight only

MentorEvaluationRecord (Mentor Scoring — Authority-Weighted)
├── eval_id: UUID PK
├── session_id: UUID (FK ScoreSession)
├── mentor_id: UUID (FK User)
├── candidate_id: UUID (FK User)
├── tenant_id: UUID (FK Tenant)
├── eval_submitted_at: TIMESTAMP
├── rubric_scores: JSONB {
    criteria_1_score: int (1-10),
    criteria_2_score: int (1-10),
    criteria_3_score: int (1-10),
    criteria_4_score: int (1-10),
    criteria_5_score: int (1-10)
  }
├── mentor_narrative_permitted: BOOLEAN [text commentary only, not scored]
├── raw_mentor_score: DECIMAL(10,4)
├── mentor_authority_weight: DECIMAL(5,4) [from mentor tier config]
├── variance_vs_system_score: DECIMAL(5,2) [% divergence from rule score]
├── variance_flag: BOOLEAN [> threshold triggers review]
├── included_in_merge: BOOLEAN
├── created_at: TIMESTAMP [immutable]
└── [MENTOR_SCORES_SUPPLEMENT_NOT_OVERRIDE]:
    - Mentor scores add weight, not replace rule scores
    - Variance threshold enforced

VarianceAnomalyLog (Score Divergence Detection)
├── anomaly_id: UUID PK
├── session_id: UUID (FK ScoreSession)
├── candidate_id: UUID (FK User)
├── tenant_id: UUID (FK Tenant)
├── anomaly_type: ENUM(
    'PEER_VS_SYSTEM_DIVERGENCE',
    'MENTOR_VS_SYSTEM_DIVERGENCE',
    'PEER_SELF_FAVOR',
    'SCORE_IMPOSSIBLE_VALUE',
    'DUPLICATE_SCORE_EVENT',
    'CROSS_SESSION_IDENTICAL_SCORE',
    'TIME_ANOMALY',
    'SIGNAL_MISMATCH'
  )
├── severity: ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')
├── anomaly_description: TEXT
├── system_score_at_time: DECIMAL(10,4)
├── divergent_score: DECIMAL(10,4)
├── divergence_percent: DECIMAL(5,2)
├── auto_action_taken: VARCHAR(100) [weight reduction, flag, etc.]
├── requires_human_review: BOOLEAN
├── reviewed_by: UUID (FK User) [if reviewed]
├── reviewed_at: TIMESTAMP
├── resolution: TEXT [if resolved]
├── created_at: TIMESTAMP [immutable]
└── [APPEND_ONLY, NEVER_DELETED]

BeltEligibilityScore (Belt Certification Gate)
├── eligibility_id: UUID PK
├── candidate_id: UUID (FK User)
├── tenant_id: UUID (FK Tenant)
├── belt_level: ENUM('WHITE', 'YELLOW', 'GREEN', 'BLUE', 'RED', 'BLACK')
├── evaluation_date: DATE
├── gd_sessions_required: INT
├── gd_sessions_completed: INT
├── gd_cumulative_score: DECIMAL(10,4)
├── gd_minimum_threshold: DECIMAL(10,4)
├── gd_threshold_met: BOOLEAN
├── dojo_sessions_required: INT
├── dojo_sessions_completed: INT
├── dojo_cumulative_score: DECIMAL(10,4)
├── dojo_minimum_threshold: DECIMAL(10,4)
├── dojo_threshold_met: BOOLEAN
├── mentor_confirmation_required: BOOLEAN
├── mentor_confirmation_received: BOOLEAN
├── mentor_id: UUID (FK User) [if confirmed]
├── mentor_confirmed_at: TIMESTAMP
├── variance_within_limit: BOOLEAN [score consistency check]
├── max_variance_detected: DECIMAL(5,2)
├── eligibility_status: ENUM('NOT_ELIGIBLE', 'BORDERLINE', 'ELIGIBLE', 'CERTIFIED', 'REVOKED')
├── eligibility_computed_at: TIMESTAMP
├── certificate_issued_at: TIMESTAMP [if certified]
├── certificate_id: UUID [if issued]
└── [ELIGIBILITY_IS_COMPUTED_NOT_DECLARED]:
    - Only rule engine can set ELIGIBLE
    - Only mentor confirmation can gate CERTIFIED
    - Once CERTIFIED, immutable unless REVOKED by admin

ScoreAuditLog (Immutable Score Chronicle)
├── audit_id: UUID PK
├── session_id: UUID (FK ScoreSession)
├── candidate_id: UUID (FK User) [encrypted]
├── tenant_id: UUID (FK Tenant) [encrypted]
├── timestamp: TIMESTAMP [UTC, immutable]
├── operation_type: VARCHAR(50) ['SCORE_COMPUTED', 'SCORE_MERGED', 'SCORE_PUBLISHED', 'ANOMALY_DETECTED', 'BELT_CHECKED']
├── actor_id: UUID ['SYSTEM' for automated operations]
├── actor_role: VARCHAR(50) ['SCORING_ENGINE', 'ADMIN']
├── operation_description: TEXT
├── score_before: DECIMAL(10,4) [if update]
├── score_after: DECIMAL(10,4)
├── formula_version: VARCHAR(20)
├── signals_snapshot: JSONB [exact inputs at time of scoring]
├── anomaly_triggered: BOOLEAN
├── http_method: ENUM('GET', 'POST', 'PATCH')
├── endpoint: VARCHAR(255)
├── response_time_ms: INT
├── checksum: CHAR(64) [SHA-256]
├── signature: VARCHAR(512) [ECDSA]
├── storage_location: VARCHAR(100)
└── [APPEND_ONLY, IMMUTABLE, WORM_STORAGE]
```

---

# 4️⃣ SECTION D — SEALED OPERATION PROMPTS

## 🔐 OPERATION 1: GD_SCORE_COMPUTATION (Post-Session)

```
OPERATION_ID: OP_001_GD_SCORE_COMPUTATION
CLASSIFICATION: DETERMINISTIC_RULE_ENGINE
AUTHORIZATION_TIER: SYSTEM (Automatic, post-session close)
TENANT_ISOLATION: HARD_BOUNDARY
AUDIT_TRAIL: MANDATORY_IMMUTABLE
AI_INVOLVEMENT: NONE
HUMAN_INVOLVEMENT: NONE

SEALED PROMPT FOR AGENT:
=====================================
You are computing GD performance scores post-session close.

PRE-COMPUTATION CHECKS:
  1. Verify session is closed:
     SELECT session_status FROM ScoreSession
     WHERE session_id = ? AND session_status = 'COMPLETED'
     → If not COMPLETED: STOP, return error

  2. Load formula version:
     SELECT scoring_formula_version, weight_config_snapshot
     FROM ScoreSession WHERE session_id = ?
     → Formula version is IMMUTABLE for this session

  3. Load all behavioral signals from Redis:
     GET gd:session:{session_id}:signals:{candidate_id}
     → If missing: score component = 0, log MISSING_SIGNAL

GD SCORING FORMULA (DETERMINISTIC — VERSION 1.0):
  
  For each candidate in session:
  
  participation_score =
    + (turns_completed / turns_total) × 20
    → Full turn use: max 20 pts proportional
  
  time_usage_score =
    + (mic_open_seconds / total_allocated_seconds) × 20
    → Full time use: max 20 pts proportional
  
  completion_score =
    + (session_completed == TRUE ? 10 : 0)
    + (no_early_exit == TRUE ? 5 : 0)
    + (no_late_join == TRUE ? 5 : 0)
    → Max 20 pts
  
  discipline_score =
    + (turns_skipped == 0 ? 10 : MAX(0, 10 - turns_skipped × 3))
    + (silence_events == 0 ? 5 : MAX(0, 5 - silence_events × 2))
    + (network_drops == 0 ? 5 : MAX(0, 5 - network_drops × 2))
    → Max 20 pts
  
  non_dominance_score =
    + MAX(0, 20 - interrupt_attempts × 5)
    → 0 interrupts = 20 pts
    → 4+ interrupts = 0 pts
  
  raw_score = (participation_score + time_usage_score +
               completion_score + discipline_score +
               non_dominance_score)
  → Maximum possible raw_score = 100
  → Minimum possible raw_score = 0

  ROUND WEIGHTS (applied per round type):
    INTRO round:       weight = 0.20
    REBUTTAL round:    weight = 0.25
    OPEN round:        weight = 0.30
    CONCLUSION round:  weight = 0.25
  
  weighted_session_score = SUM(round_score × round_weight)

ATOMIC SCORE CREATION:
  BEGIN TRANSACTION;
  
  A. For each candidate:
     FOR each round:
       INSERT INTO GDRoundScore (round values per formula)
  
  B. Aggregate to CandidateScoreRecord:
     INSERT INTO CandidateScoreRecord (
       record_id = UUID_v4(),
       session_id, candidate_id, tenant_id,
       raw_behavioral_signals = signals_json,
       score_components = component_breakdown,
       raw_score = computed_raw,
       normalized_score = (raw_score / 100) × 100,
       score_status = 'COMPUTED',
       formula_version = current_formula_version,
       computed_at = UTC_now,
       checksum = SHA256(record_content),
       signature = ECDSA_P384(record_content)
     )
  
  C. Detect anomalies:
     IF raw_score < 0 OR raw_score > 100:
       → INSERT VarianceAnomalyLog (SCORE_IMPOSSIBLE_VALUE)
       → score_status = 'FLAGGED'
     
     IF candidate_signal_set is_empty:
       → INSERT VarianceAnomalyLog (SIGNAL_MISMATCH)
  
  D. Create audit log:
     INSERT INTO ScoreAuditLog (
       operation_type = 'SCORE_COMPUTED',
       signals_snapshot = raw_signals,
       score_after = raw_score,
       formula_version,
       timestamp = UTC_now
     )
  
  COMMIT TRANSACTION;

DETERMINISM:
  Same signals → same score always
  Formula version locked at session creation
  No external model inference permitted
=====================================
```

## 🔐 OPERATION 2: PEER_MENTOR_SCORE_MERGE (Post-Evaluation Window)

```
OPERATION_ID: OP_002_PEER_MENTOR_SCORE_MERGE
CLASSIFICATION: WEIGHTED_MERGE_WITH_FRAUD_CHECK
AUTHORIZATION_TIER: SYSTEM (Automatic, after eval window closes)
TENANT_ISOLATION: HARD_BOUNDARY
FRAUD_DETECTION: MANDATORY

SEALED PROMPT FOR AGENT:
=====================================
You are merging peer and mentor evaluations into final scores.

FRAUD CHECK — PEER EVALUATIONS:
  For each PeerEvaluationRecord:
  
  1. Self-favor detection:
     IF evaluator scored target_candidate_id > group_avg + 15%
        AND evaluator_id != target_candidate_id:
        → flag as SUSPICIOUS (may not be self-favor but outlier)
     IF evaluator_id == target_candidate_id:
        → REJECTED (self-evaluation forbidden)
  
  2. Variance from group:
     peer_group_avg = AVG(all peer scores for target)
     IF ABS(evaluator_score - peer_group_avg) > 30%:
        → flag as SUSPICIOUS
        → weight_after_fraud_check = 0.3 (reduced from standard 1.0)
  
  3. Speed anomaly:
     IF time_taken_seconds < 30:
        → flag as SUSPICIOUS (too fast, likely random input)
        → weight_after_fraud_check = 0.0 (excluded)
  
  4. Identical to prior session:
     IF raw_peer_score MATCHES last_session_peer_score exactly:
        → flag as SUSPICIOUS

MERGE FORMULA (DETERMINISTIC):
  
  system_score_weight = 0.60 (default, tenant-configurable)
  peer_score_weight = 0.25 (default, tenant-configurable)
  mentor_score_weight = 0.15 (default, tenant-configurable)
  
  peer_aggregate = WEIGHTED_AVG(
    peer_scores WHERE included_in_merge = TRUE,
    weight = weight_after_fraud_check
  )
  
  mentor_aggregate = WEIGHTED_AVG(
    mentor_scores,
    weight = mentor_authority_weight
  )
  
  merged_final_score =
    (system_score_weight × raw_score)
    + (peer_score_weight × peer_aggregate)
    + (mentor_score_weight × mentor_aggregate)
  
  IF no peer scores exist:
    Redistribute peer_score_weight → system_score_weight
  
  IF no mentor scores exist:
    Redistribute mentor_score_weight → system_score_weight

VARIANCE CHECK:
  variance_system_vs_mentor =
    ABS(raw_score - mentor_aggregate) / raw_score × 100
  
  IF variance_system_vs_mentor > 25%:
    → INSERT VarianceAnomalyLog (MENTOR_VS_SYSTEM_DIVERGENCE)
    → requires_human_review = TRUE
    → score_status = 'FLAGGED' (not withheld, still published at flag)

ATOMIC MERGE:
  BEGIN TRANSACTION;
  
  A. Update CandidateScoreRecord:
     UPDATE CandidateScoreRecord SET
       peer_score_raw = peer_aggregate,
       peer_score_weight = final_peer_weight,
       mentor_score_raw = mentor_aggregate,
       mentor_score_weight = final_mentor_weight,
       merged_final_score = computed_merged,
       score_status = (anomaly ? 'FLAGGED' : 'MERGED')
     WHERE record_id = ?;
  
  B. Audit log:
     INSERT INTO ScoreAuditLog (
       operation_type = 'SCORE_MERGED',
       score_before = raw_score,
       score_after = merged_final_score
     )
  
  COMMIT TRANSACTION;
=====================================
```

## 🔐 OPERATION 3: SCORE_PUBLICATION (Final Publish)

```
OPERATION_ID: OP_003_SCORE_PUBLICATION
CLASSIFICATION: ATOMIC_PUBLISH_GATE
AUTHORIZATION_TIER: SYSTEM (Automatic, post-merge confirmation)
IMMUTABILITY: POST_PUBLISH_IMMUTABLE

SEALED PROMPT FOR AGENT:
=====================================
You are publishing final scores to candidate records and analytics.

PRE-PUBLISH GATES:
  1. score_status must be 'MERGED' or 'COMPUTED' (not PENDING, not WITHHELD)
  2. checksum verified against stored value
  3. signature verified against ECDSA public key
  4. tenant context verified

ATOMIC PUBLISH:
  BEGIN TRANSACTION;
  
  A. Mark score published:
     UPDATE CandidateScoreRecord SET
       score_status = 'PUBLISHED',
       published_at = UTC_now
     WHERE record_id = ? AND score_status IN ('MERGED', 'COMPUTED');
  
  B. Emit score event to analytics:
     PUBLISH to Kafka topic 'score.published':
     {
       event_type: 'score.published',
       session_id,
       candidate_id (hashed),
       engine_type,
       merged_final_score,
       tenant_id (hashed),
       published_at
     }
     → ClickHouse consumer writes to analytics tables
  
  C. Update belt eligibility check queue:
     INSERT INTO BeltEligibilityCheckQueue (
       candidate_id,
       triggered_at = UTC_now
     )
  
  D. Audit log:
     INSERT INTO ScoreAuditLog (
       operation_type = 'SCORE_PUBLISHED',
       score_after = merged_final_score,
       published_at
     )
  
  COMMIT TRANSACTION;

POST-PUBLISH IMMUTABILITY:
  Once published_at is set:
    → NO further updates to CandidateScoreRecord
    → NO score modifications permitted
    → Any dispute creates NEW DisputeRecord (separate entity)
    → Original score preserved forever
=====================================
```

## 🔐 OPERATION 4: BELT_ELIGIBILITY_CHECK (Triggered Post-Score-Publish)

```
OPERATION_ID: OP_004_BELT_ELIGIBILITY_CHECK
CLASSIFICATION: DETERMINISTIC_GATE_EVALUATION
AUTHORIZATION_TIER: SYSTEM (Automatic)
MENTOR_GATE: REQUIRED_FOR_CERTIFICATION

SEALED PROMPT FOR AGENT:
=====================================
You are evaluating belt certification eligibility.

ELIGIBILITY COMPUTATION (RULE-ENGINE ONLY):
  
  Load tenant belt config:
    SELECT belt_thresholds FROM BeltConfig WHERE tenant_id = ?
  
  For each belt level from WHITE to BLACK:
    
    gd_sessions_completed =
      COUNT(CandidateScoreRecord)
      WHERE candidate_id = ?
      AND engine_type = 'GD'
      AND score_status = 'PUBLISHED'
    
    gd_cumulative_score =
      AVG(merged_final_score)
      WHERE engine_type = 'GD'
      AND score_status = 'PUBLISHED'
    
    dojo_sessions_completed =
      COUNT(CandidateScoreRecord)
      WHERE engine_type = 'DOJO'
      AND score_status = 'PUBLISHED'
    
    dojo_cumulative_score =
      AVG(merged_final_score)
      WHERE engine_type = 'DOJO'
      AND score_status = 'PUBLISHED'
    
    max_variance =
      MAX(ABS(merged_final_score - LAG(merged_final_score)))
      across last N sessions
    
    variance_within_limit =
      max_variance <= belt_config.max_variance_threshold

  ELIGIBILITY RULE:
    IF gd_sessions_completed >= gd_sessions_required
    AND gd_cumulative_score >= gd_minimum_threshold
    AND dojo_sessions_completed >= dojo_sessions_required
    AND dojo_cumulative_score >= dojo_minimum_threshold
    AND variance_within_limit = TRUE:
      eligibility_status = 'ELIGIBLE'
    ELSE:
      eligibility_status = 'NOT_ELIGIBLE' or 'BORDERLINE'

  CERTIFICATION GATE:
    IF eligibility_status = 'ELIGIBLE'
    AND mentor_confirmation_required = TRUE
    AND mentor_confirmation_received = FALSE:
      → eligibility_status remains 'ELIGIBLE'
      → Notify mentor for confirmation
      → CANNOT self-certify
    
    IF mentor_confirmation_received = TRUE:
      → eligibility_status = 'CERTIFIED'
      → Trigger CertificateGeneration event
      → Emit 'belt.certified' event to Kafka

ATOMIC ELIGIBILITY RECORD:
  BEGIN TRANSACTION;
  
  INSERT OR UPDATE BeltEligibilityScore (
    candidate_id, tenant_id, belt_level,
    all computed values,
    eligibility_status,
    eligibility_computed_at = UTC_now
  )
  
  INSERT INTO ScoreAuditLog (
    operation_type = 'BELT_CHECKED',
    score_after = gd_cumulative_score
  )
  
  COMMIT TRANSACTION;
=====================================
```

## 🔐 OPERATION 5: ANOMALY_DETECTION_SCAN (Continuous Background)

```
OPERATION_ID: OP_005_ANOMALY_DETECTION_SCAN
CLASSIFICATION: CONTINUOUS_INTEGRITY_MONITOR
AUTHORIZATION_TIER: SYSTEM (Automatic, scheduled)
FREQUENCY: Post every scoring batch + hourly scan

SEALED PROMPT FOR AGENT:
=====================================
You are scanning for score integrity anomalies.

ANOMALY DETECTION RULES:

  1. Impossible Values:
     SELECT * FROM CandidateScoreRecord
     WHERE merged_final_score < 0 OR merged_final_score > 100
     → Severity: CRITICAL → flag and halt publication

  2. Duplicate Score Events:
     SELECT session_id, candidate_id, COUNT(*)
     FROM CandidateScoreRecord
     GROUP BY session_id, candidate_id
     HAVING COUNT(*) > 1
     → Severity: HIGH → deduplicate, keep first, log

  3. Cross-Session Identical Scores (Bot Pattern):
     IF last 5 sessions all have same score for candidate:
     → Severity: MEDIUM → flag for review

  4. Peer Fraud Pattern:
     IF evaluator's all scores within session = MAX or MIN:
     → Severity: HIGH → reject evaluations, flag evaluator

  5. Time-based Anomalies:
     IF score computed_at < session actual_start:
     → Severity: CRITICAL → security investigation

  6. Checksum Mismatch:
     IF SHA256(record_content) != stored checksum:
     → Severity: CRITICAL → tamper detection → LOCKDOWN

FOR EACH ANOMALY DETECTED:
  INSERT INTO VarianceAnomalyLog (anomaly details)
  
  IF severity = CRITICAL:
    → ALERT Platform Admin immediately
    → LOCK affected records (score_status = 'WITHHELD')
    → PRESERVE context for investigation
    → DO NOT publish until resolved
=====================================
```

---

# 5️⃣ SECTION E — API SPECIFICATION (OPENAPI 3.1 LOCKED)

```yaml
openapi: 3.1.0
info:
  title: Antigravity Performance Scoring API
  version: 1.0.0
  description: >
    Sealed & locked API for performance scoring across GD, Dojo,
    Interview, and Belt engines. All operations are deterministic,
    rule-driven, audit-logged, and tenant-isolated.

servers:
  - url: https://scoring-api.antigravity.io/v1
    description: Production

tags:
  - name: Score Operations
    description: Scoring, merging, publishing operations
  - name: Eligibility
    description: Belt certification gate operations
  - name: Audit
    description: Immutable audit access

paths:
  /sessions/{session_id}/scores/compute:
    post:
      operationId: computeSessionScores
      tags: [Score Operations]
      summary: Trigger score computation for closed session
      parameters:
        - name: session_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      responses:
        202:
          description: Computation triggered
          content:
            application/json:
              schema:
                type: object
                properties:
                  job_id: {type: string, format: uuid}
                  status: {type: string, enum: [QUEUED]}
        409:
          description: Session not closed or already computed
      security:
        - bearerAuth: []

  /sessions/{session_id}/scores:
    get:
      operationId: getSessionScores
      tags: [Score Operations]
      summary: Get all candidate scores for a session
      parameters:
        - name: session_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      responses:
        200:
          description: Session score records
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  properties:
                    record_id: {type: string, format: uuid}
                    candidate_id: {type: string, format: uuid}
                    merged_final_score: {type: number, format: decimal}
                    score_status: {type: string}
                    anomaly_detected: {type: boolean}
      security:
        - bearerAuth: []

  /scores/{record_id}/merge:
    post:
      operationId: mergePeerMentorScores
      tags: [Score Operations]
      summary: Trigger peer and mentor score merge
      parameters:
        - name: record_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      responses:
        200:
          description: Merge executed
          content:
            application/json:
              schema:
                type: object
                properties:
                  merged_final_score: {type: number}
                  peer_score_applied: {type: number}
                  mentor_score_applied: {type: number}
                  fraud_flags_detected: {type: integer}
      security:
        - bearerAuth: []

  /scores/{record_id}/publish:
    post:
      operationId: publishScore
      tags: [Score Operations]
      summary: Publish final score (immutable after this)
      parameters:
        - name: record_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      responses:
        200:
          description: Score published
          content:
            application/json:
              schema:
                type: object
                properties:
                  published_at: {type: string, format: date-time}
                  score_status: {type: string, enum: [PUBLISHED]}
        409:
          description: Score already published or not ready
      security:
        - bearerAuth: []

  /candidates/{candidate_id}/belt-eligibility:
    get:
      operationId: getBeltEligibility
      tags: [Eligibility]
      summary: Get current belt eligibility status for candidate
      parameters:
        - name: candidate_id
          in: path
          required: true
          schema: {type: string, format: uuid}
        - name: belt_level
          in: query
          schema: {type: string, enum: [WHITE, YELLOW, GREEN, BLUE, RED, BLACK]}
      responses:
        200:
          description: Belt eligibility record
          content:
            application/json:
              schema:
                type: object
                properties:
                  eligibility_status: {type: string}
                  gd_threshold_met: {type: boolean}
                  dojo_threshold_met: {type: boolean}
                  mentor_confirmation_received: {type: boolean}
                  gd_cumulative_score: {type: number}
                  dojo_cumulative_score: {type: number}
      security:
        - bearerAuth: []

  /audit/scores/{record_id}:
    get:
      operationId: getScoreAuditTrail
      tags: [Audit]
      summary: Retrieve immutable audit trail for score record
      parameters:
        - name: record_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      responses:
        200:
          description: Audit log entries
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  properties:
                    audit_id: {type: string, format: uuid}
                    operation_type: {type: string}
                    timestamp: {type: string, format: date-time}
                    score_after: {type: number}
                    checksum: {type: string}
      security:
        - bearerAuth: []

  /anomalies:
    get:
      operationId: listAnomalies
      tags: [Audit]
      summary: List detected anomalies with filters
      parameters:
        - name: severity
          in: query
          schema: {type: string, enum: [LOW, MEDIUM, HIGH, CRITICAL]}
        - name: requires_review
          in: query
          schema: {type: boolean}
      responses:
        200:
          description: Anomaly records
      security:
        - bearerAuth: []

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

---

# 6️⃣ SECTION F — SCORING FORMULAS REFERENCE (LOCKED TABLE)

```
GD SCORING FORMULA v1.0 (LOCKED)
────────────────────────────────────────────────────────────
COMPONENT                     MAX    FORMULA
────────────────────────────────────────────────────────────
Participation (turns used)    20     (turns_completed / turns_total) × 20
Time Usage (mic open)         20     (mic_open_seconds / allocated_seconds) × 20
Completion (no early exit)    20     +10 (session_complete), +5 (no early exit), +5 (no late join)
Discipline (skip/silence/drop) 20    +10 - (turns_skipped × 3), +5 - (silence × 2), +5 - (drops × 2)
Non-Dominance (interrupts)    20     MAX(0, 20 - interrupt_attempts × 5)
────────────────────────────────────────────────────────────
TOTAL                         100
────────────────────────────────────────────────────────────

ROUND WEIGHTS v1.0 (LOCKED — TENANT CONFIGURABLE)
────────────────────────────────────────────────
ROUND         DEFAULT WEIGHT
────────────────────────────────────────────────
INTRO         0.20
REBUTTAL      0.25
OPEN          0.30
CONCLUSION    0.25
────────────────────────────────────────────────
SUM           1.00 (must always equal 1.00)
────────────────────────────────────────────────

MERGE WEIGHTS v1.0 (LOCKED — TENANT CONFIGURABLE)
────────────────────────────────────────────────
SOURCE           DEFAULT WEIGHT
────────────────────────────────────────────────
System Score     0.60
Peer Score       0.25
Mentor Score     0.15
────────────────────────────────────────────────
SUM              1.00 (must always equal 1.00)
────────────────────────────────────────────────

FRAUD WEIGHT REDUCTION TABLE
────────────────────────────────────────────────────────────
FRAUD SIGNAL                       WEIGHT REDUCTION
────────────────────────────────────────────────────────────
Self-evaluation                    → REJECTED (weight = 0.0)
Time < 30 seconds                  → weight = 0.0
Variance > 30% from group avg      → weight = 0.3
Identical to prior session         → weight = 0.5
Outlier score (> avg + 15%)        → weight = 0.7
Clean evaluation                   → weight = 1.0
────────────────────────────────────────────────────────────

BELT THRESHOLDS (DEFAULT — TENANT CONFIGURABLE)
────────────────────────────────────────────────────────────
BELT    GD_MIN  GD_SESSIONS  DOJO_MIN  DOJO_SESSIONS  MENTOR
────────────────────────────────────────────────────────────
WHITE   40.0    2            35.0      1              No
YELLOW  50.0    4            45.0      2              No
GREEN   60.0    6            55.0      4              No
BLUE    70.0    8            65.0      6              Yes
RED     80.0    10           75.0      8              Yes
BLACK   90.0    15           88.0      12             Yes
────────────────────────────────────────────────────────────

ANOMALY SEVERITY THRESHOLDS
────────────────────────────────────────────────────────────
ANOMALY                         SEVERITY     AUTO ACTION
────────────────────────────────────────────────────────────
Impossible score value          CRITICAL     Withhold + Alert
Checksum mismatch               CRITICAL     Lockdown + Alert
Timestamp before session start  CRITICAL     Security investigation
Self-evaluation detected        HIGH         Reject eval
Speed anomaly (< 30s)           HIGH         Reject eval
Peer variance > 30%             HIGH         Reduce weight to 0.3
Mentor vs system diverge > 25%  MEDIUM       Flag for review
Cross-session identical score   MEDIUM       Flag for review
Turns skipped all session       LOW          Log only
────────────────────────────────────────────────────────────
```

---

# 7️⃣ SECTION G — TESTING & COMPLIANCE

```
TEST_CATEGORY                    COVERAGE    APPROACH
──────────────────────────────────────────────────────────
GD Score Computation             100%        All formula branches
Peer Fraud Detection             100%        All fraud signal patterns
Mentor Merge Logic               100%        With/without mentor cases
Belt Eligibility Gate            100%        All belt levels
Anomaly Detection                100%        All anomaly types
Score Immutability               100%        Post-publish mutation tests
Tenant Isolation                 100%        Cross-tenant leak tests
Formula Determinism              100%        Same input → same output
Concurrent Sessions              1000+       Parallel ops stress test
Checksum Integrity               100%        Tamper detection tests
Audit Trail Completeness         100%        Every operation logged
Rollback Safety                  100%        Partial failure scenarios

COMPLIANCE CHECKLIST:
✓ Score is rule-based only (no AI, no human moderator)
✓ Behavioral signals only (no speech content)
✓ Tenant isolation (cryptographic boundary)
✓ Audit trail immutability (WORM + cryptographic chain)
✓ Fraud detection on peer evaluations
✓ Variance detection and alerting
✓ Belt eligibility computation with mentor gate
✓ Score reproducibility guarantee
✓ Post-publish immutability enforcement
✓ Anti-bias structural protocol
✓ Event bus integration (Kafka)
✓ Analytics pipeline integration (ClickHouse)
✓ API security (JWT + RBAC)
✓ Observability hooks (Prometheus + Loki + OpenTelemetry)
```

---

# 8️⃣ SECTION H — INFRASTRUCTURE BINDING

```
SCORING ENGINE STACK (LOCKED):

State Source:
  → Redis (deterministic state machine — GD orchestrator output)
  → WebSocket command channel (real-time signal ingestion)

Primary Database:
  → PostgreSQL (APPEND_ONLY score tables, WORM audit logs)

Analytics:
  → ClickHouse (score metrics, distributions, performance analytics)

Event Bus:
  → Kafka (score.computed, score.published, belt.eligible, anomaly.detected)

Secrets:
  → HashiCorp Vault (tenant encryption keys, ECDSA signing keys)

Observability:
  → Prometheus (score computation latency, anomaly rates, error counts)
  → Loki (structured scoring logs)
  → OpenTelemetry (distributed tracing across scoring pipeline)

Kubernetes Namespace:
  → scoring (dedicated namespace, isolated from realtime and media)

Service Dependencies:
  → gd-orchestrator (source of GD behavioral signals)
  → dojo-match-engine (source of Dojo signals)
  → interview-service (source of Interview signals)
  → belt-certification-engine (consumer of eligibility scores)
  → analytics-service (consumer of published scores)
  → notification-service (anomaly alerts, belt status)
```

---

# 9️⃣ SECTION I — FINAL SEAL & LOCK

## Prompt Integrity Verification

```
SEAL_DETAILS:

Created:  [DEPLOYMENT_DATE]
Version:  1.0.0
Author:   [SEALED]
Status:   🔐 LOCKED & IMMUTABLE

Cryptographic Signature:
  Algorithm: ECDSA (P-384)
  Signature: [SEALED_SIGNATURE]
  Witness: [PLATFORM_GOVERNANCE_OFFICER]

Checksum (SHA-256):
  Content Hash: [SEALED_HASH]

SEAL VIOLATION DETECTION:

If this document is modified:
  1. Signature will NOT verify
  2. Checksum will NOT match
  3. Deployment will FAIL
  4. Platform Admin will be ALERTED immediately
  5. All active scoring sessions will be PAUSED

VERSION MANAGEMENT:
  v1.0.0 → Current (sealed, production-ready)
  v1.0.1 → Minor (append formula variant, Admin approval)
  v2.0.0 → Major (requires platform governance re-approval)
  
  NO in-place edits allowed
  NO deletion of content allowed
  Admin approval REQUIRED for changes
  Belt threshold changes require tenant notification

APPROVAL CHAIN:
  ✓ Platform Lead Review: [DATE]
  ✓ Scoring Formula Audit: [DATE]
  ✓ Tenant Isolation Verification: [DATE]
  ✓ Fraud Detection Review: [DATE]
  ✓ Deployment Ready: [DATE]
```

## Final Enforcement

```
╔════════════════════════════════════════════════════════════════╗
║                   🔒 SEALED & LOCKED 🔒                        ║
║                                                                  ║
║  PERFORMANCE_SCORING_AGENT v1.0.0                              ║
║                                                                  ║
║  For Antigravity Multi-Tenant SaaS Platform                    ║
║                                                                  ║
║  RULE-ENGINE ONLY · ZERO AI JUDGMENT · ZERO HUMAN BIAS         ║
║  IMMUTABLE AUDIT TRAIL · CRYPTOGRAPHIC INTEGRITY               ║
║                                                                  ║
║  This prompt envelope is CRYPTOGRAPHICALLY SEALED.             ║
║  NO modifications permitted without Admin re-approval.         ║
║  NO AI inference of speech, tone, or personality.              ║
║  NO human moderator override of computed scores.               ║
║  NO creative interpretation of behavioral signals.             ║
║  DEFAULT: DENY unless explicitly specified in formula table.   ║
║  SCORE INTEGRITY: MANDATORY AND CONTINUOUSLY ENFORCED          ║
║                                                                  ║
║  VIOLATION = IMMEDIATE ADMIN ESCALATION                        ║
║           + SCORE SESSION PAUSE                                ║
║           + AUDIT INVESTIGATION                                ║
║           + SYSTEM PARTIAL LOCKDOWN                            ║
║                                                                  ║
║  Questions?  Contact: [Platform Lead]                          ║
║  Escalation: [Platform Admin]                                  ║
║  Emergency:  [Governance Officer]                              ║
║                                                                  ║
║  ═══════════════════════════════════════════════════════════  ║
║  Last Updated:      [DEPLOYMENT_DATE]                          ║
║  Next Review:       [QUARTERLY_DATE]                           ║
║  Audit Frequency:   CONTINUOUS + POST-SESSION + QUARTERLY      ║
║  Formula Review:    Per Version Bump Only                      ║
║  ═══════════════════════════════════════════════════════════  ║
║                                                                  ║
║  Status: ✅ APPROVED FOR PRODUCTION                            ║
║         ✅ PLATFORM ADMIN AUTHORIZED                           ║
║         ✅ AUDIT-READY                                         ║
║         ✅ FRAUD-DETECTION ACTIVE                              ║
║         ✅ GOVERNANCE LOCKED                                   ║
║                                                                  ║
╚════════════════════════════════════════════════════════════════╝
```

---

# 📋 APPENDIX A — Scoring Signal Reference Map

```
ENGINE         SIGNAL                         TYPE      MAX CONTRIBUTION
─────────────────────────────────────────────────────────────────────
GD             mic_open_duration_seconds       decimal   20 pts (time score)
GD             turns_completed / turns_total   ratio     20 pts (participation)
GD             session_completed               boolean   10 pts (completion)
GD             no_early_exit                   boolean   5 pts (completion)
GD             no_late_join                    boolean   5 pts (completion)
GD             turns_skipped                   int       10 pts penalty base
GD             silence_events                  int       5 pts penalty base
GD             network_drops                   int       5 pts penalty base
GD             interrupt_attempts              int       20 pts penalty base
─────────────────────────────────────────────────────────────────────
DOJO           scenario_completion_rate        decimal   Dojo formula weight
DOJO           role_adherence_score            decimal   Dojo formula weight
DOJO           timer_compliance_rate           decimal   Dojo formula weight
DOJO           state_transitions_correct       int       Dojo formula weight
DOJO           match_no_show                   boolean   Full penalty
─────────────────────────────────────────────────────────────────────
INTERVIEW      slot_punctuality_seconds        int       Interview weight
INTERVIEW      session_completed               boolean   Interview weight
INTERVIEW      rubric_criteria_scores          jsonb     Weighted criteria avg
─────────────────────────────────────────────────────────────────────
ALL ENGINES    peer_evaluation_rubric          jsonb     × peer_weight
ALL ENGINES    mentor_evaluation_rubric        jsonb     × mentor_weight
─────────────────────────────────────────────────────────────────────
```

---

# 📋 APPENDIX B — Event Schema (AsyncAPI Baseline)

```yaml
asyncapi: 2.6.0
info:
  title: Performance Scoring Events
  version: 1.0.0

channels:
  score.computed:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id: {type: string}
            candidate_id: {type: string}
            engine_type: {type: string}
            raw_score: {type: number}
            formula_version: {type: string}
            computed_at: {type: string}

  score.published:
    publish:
      message:
        payload:
          type: object
          properties:
            record_id: {type: string}
            session_id: {type: string}
            merged_final_score: {type: number}
            published_at: {type: string}
            tenant_id: {type: string}

  score.anomaly.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            anomaly_id: {type: string}
            anomaly_type: {type: string}
            severity: {type: string}
            session_id: {type: string}
            requires_review: {type: boolean}

  belt.eligibility.updated:
    publish:
      message:
        payload:
          type: object
          properties:
            candidate_id: {type: string}
            belt_level: {type: string}
            eligibility_status: {type: string}
            computed_at: {type: string}

  belt.certified:
    publish:
      message:
        payload:
          type: object
          properties:
            candidate_id: {type: string}
            belt_level: {type: string}
            mentor_id: {type: string}
            certificate_id: {type: string}
            certified_at: {type: string}
```

---

**END OF DOCUMENT**

---

**CLASSIFICATION: CONFIDENTIAL — PLATFORM SCORING ARTIFACT**  
**FOR PLATFORM ADMIN & AUTHORIZED ENGINEERING PERSONNEL ONLY**  
**ALL COPIES MUST BE ENCRYPTED AT REST**  
**PLATFORM ADMIN APPROVED · AUDIT READY**  

🔒 **SEALED & LOCKED** 🔒  
**ZERO AI JUDGMENT · ZERO HUMAN BIAS · PURE RULE ENGINE**  
**SCORE INTEGRITY GUARANTEED · ANTI-MANIPULATION STRUCTURAL**
