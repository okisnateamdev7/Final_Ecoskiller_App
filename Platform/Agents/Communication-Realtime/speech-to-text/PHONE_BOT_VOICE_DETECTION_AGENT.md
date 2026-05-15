# PHONE_BOT_VOICE_DETECTION_AGENT.md

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║           ECOSKILLER ANTIGRAVITY — PHONE BOT VOICE DETECTION AGENT             ║
║                         SEALED · LOCKED · GOVERNED · DETERMINISTIC             ║
║                                                                                  ║
║  Mutation Policy   : Add-only via version bump                                  ║
║  Interpretation    : NONE PERMITTED                                             ║
║  Execution Auth    : Human declaration only                                     ║
║  Classification    : CRITICAL INFRASTRUCTURE — ZERO TOLERANCE DEVIATION        ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — SEAL DECLARATION

This agent specification is **SEALED**.

> No component of this agent may be interpreted, rewritten, abbreviated, inferred,
> or executed in any manner inconsistent with what is explicitly declared below.
> Any ambiguity MUST halt execution and escalate. No silent assumptions permitted.

**GOVERNANCE AUTHORITY:** `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT`
**COMPLIANCE AUTHORITY:** `AUDIT_COMPLIANCE_AGENT`
**SECURITY AUTHORITY:** `ZERO_TRUST_AGENT`

---

## SECTION 1 — AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME         : PHONE_BOT_VOICE_DETECTION_AGENT
AGENT_ID           : AGVDA-001
SYSTEM_ROLE        : Autonomous Voice Stream Classifier & Bot Discriminator
PRIMARY_DOMAIN     : Voice GD Orchestration / Recruitment Pipeline / Dojo Assessment
EXECUTION_MODE     : Deterministic + Validated + Rule-Enforced
DATA_SCOPE         : Voice session metadata, participation signals, audio feature vectors
                     (NO raw audio stored — privacy-by-design enforced)
TENANT_SCOPE       : Strict Multi-Tenant Isolation (row-level + namespace isolation)
FAILURE_POLICY     : HALT on ambiguity — STOP → LOG → ESCALATE — NO silent degradation
MUTATION_POLICY    : Add-only versioned — no retroactive schema changes
AUDIT_POLICY       : Append-only immutable audit trail per execution
SECURITY_MODEL     : Zero-trust — every input validated, every action traced
VERSION            : v1.0.0
STATUS             : ACTIVE
```

---

## SECTION 2 — PURPOSE DECLARATION

### 2.1 Problem Statement

The Ecoskiller Antigravity platform operates an **Automated Voice Group Discussion (GD) System** using self-hosted Jitsi + deterministic state-machine orchestration (Redis). This system is designed to be **recruiter-less and AI-less** at the session layer.

However, the system faces a critical integrity threat:

> **Phone bots, automated callers, scripted voice actors, and AI voice agents can inject
> fraudulent participation into GD sessions — inflating compliance scores, gaming ranking
> algorithms, and corrupting the integrity of the entire assessment pipeline.**

The `PHONE_BOT_VOICE_DETECTION_AGENT` is the **dedicated guard layer** that detects, classifies, flags, and isolates non-human voice participants **before scores are committed** to the immutable audit log.

### 2.2 What This Agent Solves

| Threat Class | Description |
|---|---|
| IVR Bot Injection | Automated phone systems that respond to prompts |
| TTS Playback Attack | Pre-recorded text-to-speech files injected via WebRTC |
| AI Voice Clone | LLM-driven voice synthesis impersonating a candidate |
| Silence Exploitation | Bots holding a token without speaking to game compliance |
| Network Relay Bot | Remote operator speaking through voice relay software |
| Scripted Turn Abuse | Bots reading scripts to complete turns and score participation |

### 2.3 Input Consumed

- WebRTC audio stream metadata (no raw audio stored)
- Jitsi session participant event logs
- Redis GD state machine signals (turn allocation, mute/unmute events)
- Voice feature vectors extracted at the transport layer
- Network telemetry per participant

### 2.4 Output Produced

- `BOT_DETECTION_RESULT` per participant per session turn
- Confidence classification (`HUMAN` / `BOT_SUSPECTED` / `BOT_CONFIRMED`)
- Anomaly flag emission to `OBSERVABILITY_AGENT`
- Score veto signal to `SCORING_ENGINE` when bot is confirmed
- Feature vector emission to `FEATURE_STORE_SERVICE`
- Audit record written (immutable, append-only)

### 2.5 Upstream Agents (Feeds This Agent)

```
VOICE_GD_ORCHESTRATOR_AGENT      → Turn events, session context, participant roster
JITSI_SESSION_LAYER              → Mute/unmute events, join/leave timestamps
WEBRTC_TRANSPORT_LAYER           → Audio stream metadata, network stats
FEATURE_STORE_SERVICE            → Historical behavioral baseline per candidate
AUTH_SERVICE                     → Identity binding, device fingerprint
```

### 2.6 Downstream Agents (Depend on This Agent)

```
SCORING_ENGINE                   → Receives veto signal if BOT_CONFIRMED
AUDIT_COMPLIANCE_AGENT           → Receives all detection records
OBSERVABILITY_AGENT              → Receives anomaly flags
FEATURE_STORE_SERVICE            → Receives emitted feature vectors
ANTI_FRAUD_AGENT                 → Receives escalation for repeat offenders
ADMIN_GOVERNANCE_SERVICE         → Receives session integrity reports
```

---

## SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Input Schema

```json
INPUT_SCHEMA: {
  "required_fields": [
    "session_id",
    "participant_id",
    "tenant_id",
    "turn_id",
    "turn_start_utc",
    "turn_end_utc",
    "mic_open_duration_ms",
    "audio_feature_vector",
    "network_telemetry",
    "device_fingerprint"
  ],
  "optional_fields": [
    "historical_participation_score",
    "prior_bot_flag_count",
    "join_delta_ms",
    "reconnect_count"
  ],
  "validation_rules": [
    "session_id must be UUID v4 — reject if malformed",
    "participant_id must match authenticated user in tenant scope",
    "tenant_id must match session tenant — cross-tenant = HALT",
    "turn_start_utc must precede turn_end_utc",
    "mic_open_duration_ms must be >= 0 and <= turn_window_ms",
    "audio_feature_vector must contain exactly 24 named fields (see Section 5.2)",
    "network_telemetry must include jitter_ms, packet_loss_pct, latency_ms",
    "device_fingerprint must be non-null and bound to session auth token"
  ],
  "security_checks": [
    "Verify JWT signature binding participant_id to session_id",
    "Verify tenant_id matches authenticated token claim",
    "Validate device_fingerprint consistency vs auth session",
    "Reject if input arrives outside active turn window"
  ],
  "domain_checks": [
    "Confirm session_id exists in active GD session registry (Redis)",
    "Confirm turn_id is current or immediately prior (no future turns)",
    "Confirm participant_id is in approved participant roster for this session"
  ]
}
```

### 3.2 Null Tolerance Policy

```
NULL TOLERANCE: ZERO
Any null required field → REJECT input
Log: VALIDATION_FAILURE with field name and session_id
Escalate to: AUDIT_COMPLIANCE_AGENT
No partial processing permitted
```

---

## SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Output Schema

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "session_id": "UUID",
    "participant_id": "UUID",
    "tenant_id": "UUID",
    "turn_id": "UUID",
    "detection_class": "HUMAN | BOT_SUSPECTED | BOT_CONFIRMED",
    "bot_type": "NONE | IVR | TTS_PLAYBACK | AI_VOICE_CLONE | SILENCE_EXPLOIT | RELAY | SCRIPT",
    "signal_anomalies": ["array of fired anomaly rule IDs"],
    "score_veto": true | false,
    "veto_reason": "string | null",
    "action_taken": "ALLOW | FLAG | VETO | ESCALATE"
  },
  "confidence_score": "0.00–1.00 (float)",
  "model_version": "string — e.g. AGVDA-001-v1.0.0",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "SCORING_ENGINE.veto_signal (if BOT_CONFIRMED)",
    "FEATURE_STORE_SERVICE.feature_emit",
    "AUDIT_COMPLIANCE_AGENT.log_record",
    "OBSERVABILITY_AGENT.anomaly_flag (if BOT_SUSPECTED or BOT_CONFIRMED)",
    "ANTI_FRAUD_AGENT.escalate (if repeat_offender = true)"
  ]
}
```

### 4.2 Output Guarantees

- Every output includes: `confidence_score`, `model_version`, `audit_reference`
- All outputs are immutable once written
- Score veto is irreversible within the session — no override permitted post-commit

---

## SECTION 5 — ML / AI LOGIC LAYER

### 5.1 Architecture Overview

This agent is **ML-primary (80%)** with a **tightly scoped AI assist layer (20%)**.

```
AI MUST ASSIST ML — AI MUST NOT REPLACE ML
All final classification decisions are made by the rule + ML layer.
AI layer is used ONLY for anomaly narrative generation and edge-case flagging.
```

### 5.2 Audio Feature Vector (24 Fields — Mandatory)

Extracted at the Jitsi/WebRTC transport layer. No raw audio transmitted or stored.

```yaml
AUDIO_FEATURE_VECTOR:
  # Prosody Features
  - pitch_hz_mean             # Mean fundamental frequency
  - pitch_hz_std              # Variance in pitch (bots show low variance)
  - pitch_hz_range            # Min-max range
  - speech_rate_syl_per_sec   # Syllable rate
  - pause_count               # Number of natural micro-pauses
  - pause_duration_mean_ms    # Average pause length

  # Spectral Features
  - spectral_centroid_mean    # Frequency center-of-mass
  - spectral_rolloff          # High-frequency energy distribution
  - mfcc_vector_13            # 13-dimensional MFCC (compressed)
  - zcr_mean                  # Zero crossing rate (TTS marker)

  # Energy Features
  - rms_energy_mean           # Root mean square energy
  - rms_energy_std            # Energy consistency (bots = flat)
  - silence_ratio             # Silence percentage of turn

  # Temporal Features
  - speech_start_latency_ms   # Time from unmute to first sound
  - turn_duration_used_pct    # How much of window was used
  - speech_burst_count        # Number of distinct speech bursts

  # Network Behavioral Features
  - jitter_ms                 # Audio jitter (bots may show perfect stability)
  - packet_loss_pct           # Network packet loss
  - latency_p95_ms            # 95th percentile latency
  - reconnect_count           # Session reconnects during turn

  # Cross-Session Behavioral Features
  - response_timing_delta_ms  # Timing diff vs prior sessions (baseline drift)
  - turn_start_reaction_ms    # Latency from token grant to speaking
  - participation_pattern_hash # Hashed behavioral pattern for similarity
  - prior_bot_flag_count       # Prior flagging history from Feature Store
```

### 5.3 ML Classification Model

```yaml
MODEL_TYPE          : Gradient Boosted Binary Classifier (XGBoost)
                      + Isolation Forest (anomaly detection secondary layer)
TARGET              : detection_class ∈ {HUMAN=0, BOT=1}
FEATURES_USED       : All 24 audio feature vector fields + 4 session metadata fields
TRAINING_FREQUENCY  : Monthly retrain on labeled session data
CONFIDENCE_THRESHOLD:
  HUMAN            : >= 0.80
  BOT_SUSPECTED    : 0.55 – 0.79
  BOT_CONFIRMED    : >= 0.80 (ML) AND >= 2 rule violations (see 5.5)
DRIFT_DETECTION:
  - Monitor feature distribution shift via KS test (weekly)
  - Monitor accuracy degradation on labeled production samples
  - Alert threshold: > 3% accuracy drop → ESCALATE_TO: ML_REHYDRATION_AGENT
VERSION_CONTROL:
  - Store model_version per prediction
  - Immutable reference in audit log
  - Previous version retained for 90 days post-rollout
```

### 5.4 Rule Engine (Deterministic Layer — Runs First)

Rules fire before ML. Any `RULE_CONFIRMED_BOT` overrides ML classification.

```yaml
RULE_ENGINE:

  R001_TTS_SIGNATURE:
    trigger : zcr_mean < 0.02 AND rms_energy_std < 0.01 AND pitch_hz_std < 5.0
    meaning : Unnaturally flat vocal energy — TTS pattern
    action  : BOT_SUSPECTED → escalate to ML

  R002_SILENCE_EXPLOIT:
    trigger : silence_ratio > 0.90 AND mic_open_duration_ms > 5000
    meaning : Token held with near-total silence — silence abuse
    action  : BOT_SUSPECTED (silence exploit type)

  R003_PERFECT_TIMING:
    trigger : turn_start_reaction_ms < 80ms AND speech_burst_count == 1
    meaning : Inhuman response latency — scripted trigger
    action  : BOT_CONFIRMED → RULE_CONFIRMED_BOT

  R004_REPLAY_PATTERN:
    trigger : participation_pattern_hash matches known_bot_hash_registry (cosine sim > 0.97)
    meaning : Identical behavioral fingerprint to previously confirmed bot
    action  : BOT_CONFIRMED → RULE_CONFIRMED_BOT

  R005_FLAT_PROSODY:
    trigger : pitch_hz_std < 8.0 AND speech_rate_syl_per_sec outside [2.5, 6.5]
    meaning : Unnatural prosody — TTS or scripted voice
    action  : BOT_SUSPECTED → escalate to ML

  R006_NETWORK_BOT_SIGNATURE:
    trigger : jitter_ms < 0.5 AND packet_loss_pct == 0.0 AND latency_p95_ms < 5ms
    meaning : Impossibly perfect network — local audio injection
    action  : BOT_SUSPECTED

  R007_DEVICE_SPOOF:
    trigger : device_fingerprint mismatch vs auth session fingerprint
    meaning : Device identity inconsistency — potential relay
    action  : HALT → ESCALATE_TO: AUTH_SERVICE + AUDIT_COMPLIANCE_AGENT

  R008_INSTANT_COMPLETION:
    trigger : speech_start_latency_ms < 50ms AND turn_duration_used_pct > 0.98
    meaning : Perfect turn utilization with no human warm-up
    action  : BOT_SUSPECTED
```

### 5.5 Combined Decision Matrix

```
IF RULE_CONFIRMED_BOT (R003 or R004 or R007)
  → detection_class = BOT_CONFIRMED
  → score_veto = true
  → action = VETO + ESCALATE

IF ML_score >= 0.80 AND fired_rules >= 2
  → detection_class = BOT_CONFIRMED
  → score_veto = true
  → action = VETO + ESCALATE

IF ML_score >= 0.55 AND fired_rules >= 1
  → detection_class = BOT_SUSPECTED
  → score_veto = false (flag only)
  → action = FLAG → REVIEW_QUEUE

IF ML_score < 0.55 AND fired_rules == 0
  → detection_class = HUMAN
  → score_veto = false
  → action = ALLOW
```

### 5.6 AI Assist Layer (Scoped — 20%)

```yaml
AI_USAGE_SCOPE:
  - Generate anomaly narrative summary for Admin Governance Service
  - Classify ambiguous edge-case patterns not caught by rule engine
  - Summarize repeat-offender behavioral drift for human review queue

PROMPT_GOVERNANCE:
  - All prompts are versioned and deterministic
  - AI output is advisory only — never directly triggers veto
  - AI output must pass confidence gate before use
  - No creative interpretation permitted
  - Prompt version stored in audit_reference

AI MUST NOT:
  - Override rule engine decisions
  - Override ML classification
  - Access raw audio
  - Access cross-tenant data
  - Autonomously veto scores
```

---

## SECTION 6 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS        : 5,000 (peak — 50,000 concurrent session participants / 10 signals each)
LATENCY_TARGET      : < 200ms per detection decision (p99)
MAX_CONCURRENCY     : 50,000 parallel participant turn evaluations
QUEUE_STRATEGY      : Kafka topic — gd.voice.detection.events (partitioned by tenant_id)

HORIZONTAL_SCALING  : Stateless detection workers — scale via Kubernetes HPA
STATELESS_EXECUTION : All state from Redis (GD state machine) and PostgreSQL — no local state
EVENT_DRIVEN        : Triggered by turn_token_granted event from Voice GD Orchestrator
ASYNC_PROCESSING    : Non-blocking — detection runs in parallel to turn execution
IDEMPOTENT          : Same session_id + turn_id always produces same output (deterministic)

CACHING:
  - Audio feature extraction results cached in Redis for 30 mins (TTL aligned to session)
  - Known bot hash registry cached in Redis with 24h TTL
  - Feature baseline cache per participant (from Feature Store)
```

---

## SECTION 7 — SECURITY ENFORCEMENT

```yaml
ZERO_TRUST_POLICY:
  - Every input validated against tenant_id + session_id + participant_id triple
  - No assumptions about caller identity
  - JWT verified on every request — no session-level trust carry-over

TENANT_ISOLATION:
  - All queries parameterized with tenant_id
  - No cross-tenant audio feature comparison without explicit anonymization
  - Bot hash registry is tenant-isolated unless global threat pattern confirmed

DOMAIN_ISOLATION:
  - Agent operates only within Voice GD domain
  - No access to billing, job, or education service data

ROLE_BASED_AUTH:
  - Read: GD_ORCHESTRATOR_SERVICE, SCORING_ENGINE
  - Write: AUDIT_COMPLIANCE_AGENT (agent writes; agent never grants others write)
  - Admin override: ADMIN_GOVERNANCE_SERVICE only

ENCRYPTION:
  - All audio feature vectors encrypted at rest (AES-256)
  - All API communication over TLS 1.3
  - Audit log entries signed (HMAC-SHA256) — tamper detection enabled

AUDIT_LOGGING:
  - Append-only — no update/delete operations on audit store
  - Every detection event logged regardless of outcome
  - All validation failures logged with full input_hash

ACCESS_LOG:
  - Track who queried detection results and when
  - Alert if SCORING_ENGINE queries are modified or replayed

PROHIBITED:
  - Cross-tenant bot hash sharing without governance approval
  - Raw audio access at any layer
  - Modification of prior audit records
```

---

## SECTION 8 — AUDIT & TRACEABILITY

Every detection execution writes the following immutable record:

```json
AUDIT_RECORD: {
  "timestamp_utc"       : "ISO-8601",
  "actor_id"            : "PHONE_BOT_VOICE_DETECTION_AGENT",
  "session_id"          : "UUID",
  "participant_id"      : "UUID",
  "tenant_id"           : "UUID",
  "turn_id"             : "UUID",
  "input_hash"          : "SHA-256 of input payload",
  "output_hash"         : "SHA-256 of output payload",
  "model_version"       : "AGVDA-001-v1.0.0",
  "rule_ids_fired"      : ["R001", "R003"],
  "ml_confidence_score" : 0.91,
  "detection_class"     : "BOT_CONFIRMED",
  "decision_path"       : "RULE_R003_CONFIRMED → ML=0.91 → BOT_CONFIRMED",
  "action_taken"        : "VETO",
  "score_veto"          : true,
  "anomaly_flags"       : ["INSTANT_COMPLETION", "FLAT_PROSODY"],
  "ai_assist_used"      : false,
  "ai_prompt_version"   : null
}
```

**Audit store**: `IMMUTABLE_ARCHIVE_SERVICE` (WORM-style, 7+ year retention for assessment integrity)

---

## SECTION 9 — FAILURE POLICY

```yaml
FAILURE SCENARIOS AND RESPONSES:

INVALID_INPUT:
  action      : REJECT_AND_LOG
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT
  escalate_to : VOICE_GD_ORCHESTRATOR (notify turn skipped)
  retry       : NO

MODEL_UNAVAILABLE:
  action      : FALLBACK_TO_RULE_ENGINE_ONLY
  stop_exec   : NO (degrade gracefully to rule-only mode)
  log_to      : OBSERVABILITY_AGENT
  escalate_to : ML_REHYDRATION_AGENT
  flag_output : model_degraded = true in result

AI_LAYER_TIMEOUT (> 500ms):
  action      : SKIP_AI_LAYER — proceed with ML + rule result
  stop_exec   : NO
  log_to      : OBSERVABILITY_AGENT
  retry       : NO (timeout is hard boundary)

DATA_CORRUPTION:
  action      : HALT_IMMEDIATELY
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT + FORENSICS_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE
  retry       : PROHIBITED until root cause confirmed

CONFIDENCE_BELOW_THRESHOLD (< 0.55, no rules fired):
  action      : CLASSIFY_HUMAN (conservative default)
  stop_exec   : NO
  log_to      : AUDIT_COMPLIANCE_AGENT (flag as LOW_CONFIDENCE)
  escalate_to : HUMAN_REVIEW_QUEUE (async, non-blocking)

TENANT_MISMATCH:
  action      : HALT_IMMEDIATELY + SECURITY_ALERT
  stop_exec   : YES
  log_to      : ZERO_TRUST_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE
  retry       : PROHIBITED

NO_SILENT_FAILURES: ENFORCED — every failure produces a structured log record
```

---

## SECTION 10 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - VOICE_GD_ORCHESTRATOR_AGENT  : provides turn grant event + session context
  - AUTH_SERVICE                 : provides identity + device fingerprint
  - FEATURE_STORE_SERVICE        : provides behavioral baseline per participant
  - JITSI_SESSION_LAYER          : provides mute/unmute event stream
  - WEBRTC_TRANSPORT_LAYER       : provides audio feature extraction metadata

DOWNSTREAM_AGENTS:
  - SCORING_ENGINE               : receives score_veto signal
  - AUDIT_COMPLIANCE_AGENT       : receives all detection audit records
  - OBSERVABILITY_AGENT          : receives anomaly flag events
  - FEATURE_STORE_SERVICE        : receives emitted feature vectors (passive learning)
  - ANTI_FRAUD_AGENT             : receives repeat-offender escalations
  - ADMIN_GOVERNANCE_SERVICE     : receives session integrity reports

EVENT_TRIGGERS:
  CONSUMED:
    - gd.turn.token_granted       : starts detection evaluation
    - gd.session.participant_join : triggers baseline load from Feature Store

  EMITTED:
    - gd.voice.bot_detected       : consumed by SCORING_ENGINE
    - gd.voice.detection_complete : consumed by VOICE_GD_ORCHESTRATOR
    - gd.voice.anomaly_flagged    : consumed by OBSERVABILITY_AGENT
    - security.tenant_violation   : consumed by ZERO_TRUST_AGENT
```

---

## SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits structured behavioral features to the Feature Store on every turn evaluation.

```json
EMIT_FEATURE_VECTOR: {
  "user_id"          : "UUID",
  "session_id"       : "UUID",
  "feature_name"     : "voice_bot_risk_score",
  "feature_value"    : 0.12,
  "feature_set"      : {
    "pitch_hz_std"              : 14.2,
    "zcr_mean"                  : 0.06,
    "silence_ratio"             : 0.08,
    "turn_start_reaction_ms"    : 620,
    "detection_class"           : "HUMAN",
    "anomaly_flags_count"       : 0
  },
  "timestamp"        : "ISO-8601",
  "source_agent"     : "PHONE_BOT_VOICE_DETECTION_AGENT"
}
```

These features feed:
- `BEHAVIOR_ANALYTICS_AGENT` — longitudinal behavioral drift tracking
- `ML_ROUTING_AGENT` — model routing optimization
- `INTELLIGENCE_PREDICTION_ENGINE` — if participant is a platform learner (Dojo)

---

## SECTION 12 — GROWTH ENGINE HOOK

When a candidate is cleared as `HUMAN` with high participation quality:

```yaml
EMIT_EVENTS:
  - XP_UPDATE_EVENT        : participant completed a GD turn with verified human signal
  - RANK_UPDATE_EVENT      : feeds SKILL_RANK_ENGINE for communication skill vector
```

When `BOT_CONFIRMED` with veto:

```yaml
EMIT_EVENTS:
  - No XP awarded
  - ANTI_FRAUD_AGENT notified
  - SKILL_RANK_ENGINE blocked from updating this participant's scores
```

---

## SECTION 13 — INNOVATION ECONOMY COMPATIBILITY

Not directly applicable to this agent's primary scope.

However, if voice pattern IP claims are submitted via the Idea Registry (e.g., novel bot detection algorithms developed internally), this agent must:

```yaml
EMIT_ON_ALGORITHM_CHANGE:
  - IDEA_VECTOR       : semantic description of detection method
  - SIMILARITY_HASH   : for copy detection enforcement
  - ORIGINALITY_SCORE : baseline for internal IP protection
  - Target: IDEA_DNA_AGENT + ROYALTY_ENGINE (if licensed externally)
```

---

## SECTION 14 — PERFORMANCE MONITORING

```yaml
METRICS (reported to OBSERVABILITY_AGENT via Prometheus):

  success_rate_pct          : % of turns evaluated without agent failure
  detection_latency_p50_ms  : Median detection time
  detection_latency_p99_ms  : 99th percentile detection time (target < 200ms)
  bot_confirmed_rate_pct    : % of turns flagged BOT_CONFIRMED (drift alert if > 5%)
  bot_suspected_rate_pct    : % flagged BOT_SUSPECTED
  false_positive_estimate   : Based on admin override rate (human review feedback)
  rule_engine_hit_rate      : % of detections resolved by rule engine alone
  ml_model_drift_score      : KS statistic vs training distribution
  veto_issued_count         : Total score vetoes per session
  ai_layer_timeout_rate     : % of turns where AI layer timed out

DASHBOARDS (Grafana):
  - GD Session Bot Detection Rate (per tenant, per day)
  - Detection Latency Heatmap
  - Rule Engine Hit Breakdown (by rule ID)
  - Veto Rate vs Anomaly Type
  - Model Drift Timeline

ALERTS:
  - bot_confirmed_rate > 5% in any 1-hour window → PAGE ADMIN
  - detection_latency_p99 > 400ms → ALERT ML_REHYDRATION_AGENT
  - ml_model_drift_score > 0.08 → ALERT + schedule retrain
```

---

## SECTION 15 — VERSIONING POLICY

```yaml
VERSION_FORMAT      : AGVDA-001-vMAJOR.MINOR.PATCH
MUTATION_POLICY     : Add-only — no field removal, no schema downgrade
BACKWARD_COMPAT     : All prior input schemas must remain valid
ROLLBACK_SAFETY     : Previous version retained 90 days post-rollout
MIGRATION_DOC       : Required for every MINOR or MAJOR version bump
CHANGELOG           : Append-only CHANGELOG.md co-versioned with agent spec
MODEL_VERSION       : Stored in every audit record — never inferred from context
PROMPT_VERSION      : AI assist prompts versioned separately (AGVDA-AI-PROMPT-vX.Y.Z)
```

---

## SECTION 16 — NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
✗  Store raw audio in any form at any layer
✗  Access or compare cross-tenant audio features without governance approval
✗  Allow AI layer to issue score vetoes autonomously
✗  Override a RULE_CONFIRMED_BOT decision via any downstream signal
✗  Modify historical audit records
✗  Auto-delete any log entry
✗  Execute outside its declared domain (Voice GD / Dojo assessment only)
✗  Create hidden decision logic outside documented rule engine
✗  Accept null required fields without rejection + log
✗  Proceed after a tenant_mismatch detection
✗  Grant score veto based solely on ML score without rule corroboration
✗  Operate without valid model_version tag on every output
✗  Skip audit record emission under any execution path
```

---

## SECTION 17 — INTEGRATION ARCHITECTURE MAPPING

```
CANDIDATE BROWSER
      ↓
YOUR APP UI
      ↓
JITSI IFRAME (audio-only)              ← Voice only, no video, no raw storage
      ↓
SELF-HOSTED JITSI SERVER
      ↓
WEBRTC TRANSPORT LAYER                 ← Audio feature extraction here (not in Jitsi)
      ↓
VOICE GD ORCHESTRATOR (Redis + Node.js)
      ↓
→ [on turn_token_granted event]
      ↓
PHONE_BOT_VOICE_DETECTION_AGENT        ← THIS AGENT
      ↓
   ┌──────────────┬──────────────────────────────────────┐
   ↓              ↓                                      ↓
RULE ENGINE     ML CLASSIFIER                      AI ASSIST LAYER
(fires first)   (XGBoost)                         (advisory only)
   ↓              ↓                                      ↓
   └──────────────┴──────────────────────────────────────┘
                  ↓
           DECISION MATRIX
                  ↓
   ┌──────────────┬───────────────────┬──────────────────┐
   ↓              ↓                   ↓                  ↓
SCORING_ENGINE  AUDIT_LOG     OBSERVABILITY_AGENT  FEATURE_STORE
(veto signal)   (immutable)   (anomaly alert)      (emit features)
```

---

## SECTION 18 — DEPLOYMENT CONFIGURATION

```yaml
KUBERNETES_NAMESPACE    : realtime
SERVICE_TYPE            : Stateless microservice (Deployment, not StatefulSet)
REPLICA_MIN             : 3
REPLICA_MAX             : 50 (HPA on CPU + queue depth)
HPA_TRIGGER             : Kafka consumer lag > 1000 messages
RESOURCE_REQUEST        : 500m CPU, 512Mi RAM
RESOURCE_LIMIT          : 2000m CPU, 2Gi RAM
LIVENESS_PROBE          : /health/live (200 OK)
READINESS_PROBE         : /health/ready (includes Redis + Kafka connectivity check)
STARTUP_PROBE           : 30s grace period (model load time)

KAFKA_TOPIC_IN          : gd.voice.detection.events (partitioned by tenant_id)
KAFKA_TOPIC_OUT         : gd.voice.bot_detected, gd.voice.detection_complete

REDIS_USAGE             :
  - Active session registry lookup
  - Bot hash registry cache
  - Feature vector cache (TTL: 30 min per session)

SECRETS_MANAGED_BY      : HashiCorp Vault
CONFIG_MANAGED_BY       : Kubernetes ConfigMap (versioned)
FEATURE_FLAGS           : Unleash (per-tenant rollout of new detection rules)

ENVIRONMENTS            :
  - dev     : Rule engine only, no ML (fast iteration)
  - staging : Full ML + Rule + AI assist (test detection accuracy)
  - prod    : Full stack, immutable audit, Vault secrets
```

---

## SECTION 19 — ANTI-PATTERNS EXPLICITLY PROHIBITED

The following patterns are prohibited by architecture contract and must never appear in any implementation of this agent:

| Anti-Pattern | Reason |
|---|---|
| Audio buffer storage in any microservice | Privacy violation, legal risk, storage waste |
| Cross-tenant feature comparison | Security violation — zero-trust breach |
| AI-only veto without rule corroboration | Non-deterministic — violates governance model |
| Silent failure on model unavailability | Produces undetected fraudulent scores |
| Confidence score without model_version | Untraceable audit failure |
| Synchronous call to SCORING_ENGINE | Creates coupling — must be event-driven only |
| Re-running detection on committed turns | Retroactive score modification — prohibited |
| Skipping audit record for "clean" turns | Audit gap — every turn must be logged |

---

## SECTION 20 — FINAL LOCK DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║                         AGENT SPECIFICATION — SEALED                           ║
║                                                                                  ║
║  This document is the authoritative, governing specification for the            ║
║  PHONE_BOT_VOICE_DETECTION_AGENT operating within the Ecoskiller Antigravity    ║
║  platform.                                                                       ║
║                                                                                  ║
║  Any deviation, abbreviation, re-interpretation, or undocumented extension      ║
║  of this specification constitutes a compliance violation and must be           ║
║  escalated to ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT before implementation.   ║
║                                                                                  ║
║  AGENT_ID       : AGVDA-001                                                      ║
║  VERSION        : v1.0.0                                                         ║
║  SEALED BY      : ECOSKILLER INTELLIGENCE & INNOVATION CORE                     ║
║  GOVERNANCE     : ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT                      ║
║  MUTATION LOCK  : Add-only — version bump required for any change               ║
║  STATUS         : ACTIVE · PRODUCTION-READY                                     ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```
