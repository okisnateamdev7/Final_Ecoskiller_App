# TRANSCRIPT_AGGREGATION_AGENT.md

```
╔══════════════════════════════════════════════════════════════════════════════════════╗
║         ECOSKILLER ANTIGRAVITY — TRANSCRIPT AGGREGATION AGENT                      ║
║                      SEALED · LOCKED · GOVERNED · DETERMINISTIC                    ║
║                                                                                      ║
║  Agent ID          : AGTAA-002                                                       ║
║  Mutation Policy   : Add-only via version bump                                       ║
║  Interpretation    : NONE PERMITTED                                                  ║
║  Execution Auth    : Human declaration only                                          ║
║  Classification    : CRITICAL INFRASTRUCTURE — ASSESSMENT INTEGRITY LAYER           ║
╚══════════════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — SEAL DECLARATION

This agent specification is **SEALED**.

> No component of this agent may be interpreted, rewritten, abbreviated, inferred,
> or executed in any manner inconsistent with what is explicitly declared below.
> Any ambiguity MUST halt execution and escalate to the Governance Authority.
> No silent assumptions are permitted at any layer.

**GOVERNANCE AUTHORITY  :** `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT`
**COMPLIANCE AUTHORITY  :** `AUDIT_COMPLIANCE_AGENT`
**SECURITY AUTHORITY    :** `ZERO_TRUST_AGENT`
**DATA AUTHORITY        :** `DATA_GOVERNANCE_AGENT`

---

## SECTION 1 — AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME          : TRANSCRIPT_AGGREGATION_AGENT
AGENT_ID            : AGTAA-002
SYSTEM_ROLE         : Multi-Source Session Transcript Collector, Normalizer,
                      Assembler, and Structured Evidence Producer
PRIMARY_DOMAIN      : Voice GD Sessions / Dojo Match Sessions / Interview Sessions /
                      Skill Assessment Sessions / Intelligence Test Sessions
EXECUTION_MODE      : Deterministic + Validated + Append-Only
DATA_SCOPE          : Session event logs, turn-level participation metadata,
                      structured behavioral signals, scoring inputs, timing records,
                      bot detection outcomes, attendance records, platform signals
                      — NO raw audio, NO biometric voice data, NO PII beyond session-bound ID
TENANT_SCOPE        : Strict Multi-Tenant Isolation
                      (row-level security + Kafka partition isolation by tenant_id)
FAILURE_POLICY      : HALT on ambiguity — STOP → LOG → ESCALATE — NO partial output
MUTATION_POLICY     : Add-only versioned — no retroactive modification of assembled transcripts
AUDIT_POLICY        : Append-only immutable audit trail per execution
SECURITY_MODEL      : Zero-trust — every source validated, every assembly step traced
VERSION             : v1.0.0
STATUS              : ACTIVE
```

---

## SECTION 2 — PURPOSE DECLARATION

### 2.1 Problem Statement

The Ecoskiller Antigravity platform operates multiple **parallel real-time session types**:

- Voice Group Discussion (GD) — recruiter-less, rule-driven, Jitsi-based
- Dojo Match Sessions — skill combat, role-bound, LiveKit-based
- Interview Sessions — scheduled, token-gated, structured
- Intelligence Test Sessions — 100-question adaptive assessment
- Skill Certification Sessions — mentor-confirmed, belt-gated

Each of these session types generates **fragmented event streams** across multiple services:

```
Redis         → GD state machine events (turn grants, mutes, timers)
PostgreSQL    → Participant roster, session metadata, scheduling records
Kafka         → Async domain events (joined, spoke, skipped, dropped)
SCORING_ENGINE → Per-turn scores, confidence values
BOT_DETECTION → Bot classification results per turn
ATTENDANCE    → Presence confirmations, late joins, early exits
WEBRTC_LAYER  → Network quality telemetry per participant
FEATURE_STORE → Behavioral baseline signals
```

**No single service holds a complete, coherent, legally defensible record of what happened in a session.**

> The `TRANSCRIPT_AGGREGATION_AGENT` is the **canonical assembly layer** that collects
> all fragmented signals from all upstream sources, normalizes them into a unified
> structured session transcript, validates completeness and integrity, and produces
> an immutable, auditable, machine-readable and human-readable evidence record
> that downstream agents — scoring, analytics, governance, legal, parent reporting —
> can trust unconditionally.

### 2.2 What This Agent Solves

| Problem | Resolution |
|---|---|
| Fragmented session data across 6+ services | Single canonical aggregation per session |
| Inconsistent timestamps across event sources | UTC normalization + clock drift correction |
| Missing turns due to network drops or bot vetoes | Gap detection and structured gap annotation |
| No single auditable source of truth per session | Immutable assembled transcript per session_id |
| Scoring engine receiving incomplete participation data | Contract-validated transcript as scoring input |
| Parent/admin report generation with no reliable source | Structured transcript as report foundation |
| Legal disputes requiring session evidence | WORM-archived transcript with full provenance chain |
| Multi-domain session types with different schemas | Schema normalization layer per session_type |

### 2.3 Input Consumed

- Redis GD state machine event log (turn-by-turn)
- Kafka event bus messages for the session (all domains)
- PostgreSQL session metadata (participant roster, schedule, topic, tenant config)
- `PHONE_BOT_VOICE_DETECTION_AGENT` output records per turn
- `SCORING_ENGINE` intermediate turn-level score records
- `GD_ATTENDANCE_TRACKING_AGENT` presence records
- WebRTC network telemetry per participant per turn
- `FEATURE_STORE_SERVICE` behavioral snapshots at session start

### 2.4 Output Produced

- `SESSION_TRANSCRIPT` — complete, normalized, structured record (primary output)
- `TRANSCRIPT_GAP_REPORT` — enumeration of missing/incomplete signals with annotations
- `TRANSCRIPT_INTEGRITY_CERTIFICATE` — hash-chain proof of completeness
- Trigger events to downstream consumers
- Audit record (immutable, append-only)

### 2.5 Upstream Agents (Feeds This Agent)

```
VOICE_GD_ORCHESTRATOR_AGENT        → Turn event stream, session lifecycle events
PHONE_BOT_VOICE_DETECTION_AGENT    → Bot classification per participant per turn
GD_ATTENDANCE_TRACKING_AGENT       → Presence, join delta, late/absent records
SCORING_ENGINE                     → Intermediate per-turn scores (pre-commit)
FEATURE_STORE_SERVICE              → Behavioral baseline snapshot at session start
WEBRTC_TRANSPORT_LAYER             → Network telemetry per participant per turn
AUTH_SERVICE                       → Identity verification binding per participant
DOJO_SESSION_ORCHESTRATION_AGENT   → Dojo match event stream (when session_type=DOJO)
INTERVIEW_SERVICE                  → Interview lifecycle events (when session_type=INTERVIEW)
```

### 2.6 Downstream Agents (Depend on This Agent)

```
SCORING_ENGINE                     → Consumes finalized transcript as definitive scoring input
GD_POST_SESSION_ANALYTICS_AGENT    → Consumes transcript for behavioral analytics
AUDIT_COMPLIANCE_AGENT             → Consumes transcript for compliance verification
ADMIN_GOVERNANCE_SERVICE           → Consumes transcript for moderation + dispute resolution
PARENT_LLM_INSIGHT_NARRATIVE_AGENT → Consumes transcript for parent-facing reports
INTELLIGENCE_SCORING_ML_AGENT      → Consumes transcript for intelligence dimension scoring
LEGAL_DOCUMENT_GENERATION_SERVICE  → Consumes transcript for evidence package generation
IMMUTABLE_ARCHIVE_SERVICE          → Receives final transcript for WORM storage
BEHAVIOR_ANALYTICS_AGENT           → Consumes feature vectors emitted from transcript
PASSPORT_AGGREGATION_AGENT         → Consumes session outcome to update Skill Passport
```

---

## SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Trigger Conditions

This agent is triggered by ONE of:

```yaml
TRIGGER_A : gd.session.completed          (Voice GD session ended by orchestrator)
TRIGGER_B : dojo.match.completed          (Dojo match session concluded)
TRIGGER_C : interview.session.completed   (Interview lifecycle closed)
TRIGGER_D : assessment.session.completed  (Intelligence test session finalized)
TRIGGER_E : ADMIN_GOVERNANCE_SERVICE.manual_transcript_request (dispute resolution path)
```

On trigger, agent receives a `session_id` and initiates multi-source collection.

### 3.2 Source Collection Contract

Each source is collected with an explicit **SLA window** and a **fallback policy**.

```yaml
SOURCE_COLLECTION_CONTRACT:

  SOURCE_1 — REDIS_GD_STATE_LOG:
    data          : Complete turn-by-turn state machine event log
    format        : JSON array of state transition objects
    sla_window_ms : 500ms after session.completed event
    fallback      : If unavailable within SLA → mark as SOURCE_UNAVAILABLE
                    → log incident → proceed with gap annotation
    required      : YES for GD sessions / NO for Dojo/Interview

  SOURCE_2 — KAFKA_SESSION_EVENTS:
    data          : All domain events tagged with this session_id
    format        : Ordered Kafka message batch (partitioned by tenant_id)
    sla_window_ms : 2000ms (allow event bus propagation lag)
    fallback      : Replay from Kafka retention if initial fetch incomplete
    required      : YES — all session types

  SOURCE_3 — POSTGRESQL_SESSION_METADATA:
    data          : Participant roster, session config, topic, tenant, schedule
    format        : Relational query result (session + participants JOIN)
    sla_window_ms : 300ms
    fallback      : HALT — metadata is mandatory, no transcript without it
    required      : YES — all session types

  SOURCE_4 — BOT_DETECTION_RECORDS:
    data          : PHONE_BOT_VOICE_DETECTION_AGENT output per participant per turn
    format        : JSON array indexed by (participant_id, turn_id)
    sla_window_ms : 1000ms
    fallback      : Mark turns as BOT_STATUS_UNKNOWN if unavailable
                    → flag in TRANSCRIPT_GAP_REPORT
    required      : YES for GD sessions / Optional for others

  SOURCE_5 — SCORING_INTERMEDIATE_RECORDS:
    data          : Per-turn numeric scores (pre-commit, unvalidated)
    format        : JSON array of score_record objects
    sla_window_ms : 1500ms
    fallback      : Omit score data → SCORING_ENGINE will regenerate from transcript
    required      : Optional (scoring can reconstruct from assembled events)

  SOURCE_6 — ATTENDANCE_RECORDS:
    data          : Join timestamps, leave timestamps, presence classification
    format        : JSON attendance_record per participant
    sla_window_ms : 500ms
    fallback      : Derive from Kafka join/leave events if attendance service down
    required      : YES — all session types

  SOURCE_7 — WEBRTC_NETWORK_TELEMETRY:
    data          : Jitter, packet loss, latency per participant per turn
    format        : TimeSeries JSON array
    sla_window_ms : 1000ms
    fallback      : Mark as TELEMETRY_UNAVAILABLE — non-blocking
    required      : NO (enrichment only)

  SOURCE_8 — FEATURE_STORE_BASELINE_SNAPSHOT:
    data          : Behavioral baseline at session start for each participant
    format        : Feature vector JSON per participant_id
    sla_window_ms : 1000ms
    fallback      : Mark as BASELINE_UNAVAILABLE — proceed without
    required      : NO (enrichment only)
```

### 3.3 Input Validation Schema

```json
INPUT_VALIDATION: {
  "required_fields": [
    "session_id",
    "session_type",
    "tenant_id",
    "session_start_utc",
    "session_end_utc",
    "participant_roster",
    "topic_id",
    "scheduled_turn_count",
    "actual_turn_count"
  ],
  "optional_fields": [
    "batch_id",
    "recruiter_id",
    "institute_id",
    "job_id",
    "dojo_match_id",
    "interview_slot_id",
    "admin_override_reason"
  ],
  "validation_rules": [
    "session_id must be UUID v4",
    "session_type must be one of: GD | DOJO | INTERVIEW | INTELLIGENCE_TEST | CERTIFICATION",
    "tenant_id must match authenticated service token claim",
    "session_end_utc must be after session_start_utc",
    "participant_roster must contain >= 1 participant (warn if < 3 for GD)",
    "scheduled_turn_count must be integer > 0",
    "actual_turn_count must be <= scheduled_turn_count (excess = anomaly)",
    "All participant_ids in roster must resolve to valid authenticated users in tenant"
  ],
  "security_checks": [
    "Verify service JWT — agent only accepts calls from whitelisted upstream agents",
    "Verify tenant_id is consistent across ALL collected sources",
    "Reject if any source returns data with a different tenant_id",
    "Validate session_id existence in PostgreSQL before proceeding"
  ],
  "domain_checks": [
    "session_type determines which sources are REQUIRED vs OPTIONAL",
    "GD sessions: Sources 1,2,3,4,6 are REQUIRED",
    "DOJO sessions: Sources 2,3,6 are REQUIRED; Source 1 replaced by dojo event log",
    "INTERVIEW sessions: Sources 2,3,6 are REQUIRED",
    "INTELLIGENCE_TEST: Sources 2,3,6 are REQUIRED"
  ]
}
```

### 3.4 Null Tolerance Policy

```
NULL TOLERANCE: ZERO for required fields
NULL in optional field → annotate in transcript, proceed
NULL in required field → REJECT + LOG + ESCALATE
Cross-tenant data contamination → HALT IMMEDIATELY + SECURITY ALERT
```

---

## SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Primary Output — SESSION_TRANSCRIPT

```json
SESSION_TRANSCRIPT: {
  "transcript_id"             : "UUID v4 — generated at assembly time",
  "session_id"                : "UUID",
  "session_type"              : "GD | DOJO | INTERVIEW | INTELLIGENCE_TEST | CERTIFICATION",
  "tenant_id"                 : "UUID",
  "assembled_at_utc"          : "ISO-8601",
  "model_version"             : "AGTAA-002-v1.0.0",
  "audit_reference"           : "UUID",

  "session_metadata": {
    "topic_id"                : "UUID",
    "topic_text"              : "string",
    "scheduled_start_utc"     : "ISO-8601",
    "actual_start_utc"        : "ISO-8601",
    "actual_end_utc"          : "ISO-8601",
    "session_duration_seconds": "integer",
    "batch_id"                : "UUID | null",
    "round_structure"         : ["INTRO", "REBUTTAL", "OPEN", "CONCLUSION"],
    "turn_duration_config"    : {"INTRO": 60, "REBUTTAL": 30, "OPEN": null, "CONCLUSION": 45}
  },

  "participant_records": [
    {
      "participant_id"          : "UUID",
      "display_name_hash"       : "SHA-256 of display name (PII protection)",
      "join_timestamp_utc"      : "ISO-8601",
      "leave_timestamp_utc"     : "ISO-8601 | null",
      "join_delta_seconds"      : "integer (0 = on-time, positive = late)",
      "attendance_class"        : "PRESENT | LATE | SPECTATOR | ABSENT | DROPPED",
      "bot_classification"      : "HUMAN | BOT_SUSPECTED | BOT_CONFIRMED | BOT_STATUS_UNKNOWN",
      "bot_type"                : "NONE | IVR | TTS_PLAYBACK | AI_VOICE_CLONE | SILENCE_EXPLOIT | RELAY | SCRIPT",
      "score_eligible"          : "boolean (false if BOT_CONFIRMED or ABSENT)",
      "device_fingerprint_hash" : "SHA-256 of device fingerprint",
      "network_quality_class"   : "EXCELLENT | GOOD | DEGRADED | UNSTABLE | UNKNOWN",
      "turn_records"            : ["array — see turn_record schema below"]
    }
  ],

  "turn_records": [
    {
      "turn_id"                 : "UUID",
      "participant_id"          : "UUID",
      "round_type"              : "INTRO | REBUTTAL | OPEN | CONCLUSION",
      "turn_sequence"           : "integer (order within session)",
      "token_granted_utc"       : "ISO-8601",
      "token_expired_utc"       : "ISO-8601",
      "allocated_duration_ms"   : "integer",
      "mic_open_duration_ms"    : "integer",
      "speech_start_latency_ms" : "integer",
      "silence_ratio"           : "float 0.0–1.0",
      "interrupt_attempts"      : "integer",
      "turn_outcome"            : "COMPLETED | SKIPPED | DROPPED | VETOED | TIMEOUT",
      "veto_reason"             : "BOT_CONFIRMED | null",
      "bot_detection_confidence": "float 0.0–1.0 | null",
      "network_jitter_ms"       : "float | null",
      "network_packet_loss_pct" : "float | null",
      "turn_score_raw"          : "float | null (pre-commit from SCORING_ENGINE)",
      "turn_score_eligible"     : "boolean",
      "anomaly_flags"           : ["array of anomaly rule IDs"]
    }
  ],

  "session_summary": {
    "total_participants"        : "integer",
    "present_participants"      : "integer",
    "bot_confirmed_count"       : "integer",
    "bot_suspected_count"       : "integer",
    "total_turns_scheduled"     : "integer",
    "total_turns_completed"     : "integer",
    "total_turns_skipped"       : "integer",
    "total_turns_vetoed"        : "integer",
    "session_integrity_class"   : "CLEAN | DEGRADED | COMPROMISED",
    "integrity_degradation_reason" : "string | null"
  },

  "provenance_chain": {
    "sources_collected"         : ["SOURCE_1", "SOURCE_2", "SOURCE_3", "SOURCE_4", "SOURCE_6"],
    "sources_unavailable"       : ["SOURCE_7"],
    "source_hashes"             : {"SOURCE_1": "SHA-256", "SOURCE_2": "SHA-256", "SOURCE_3": "SHA-256"},
    "collection_timestamps_utc" : {"SOURCE_1": "ISO-8601", "SOURCE_2": "ISO-8601"},
    "clock_drift_corrections"   : [{"source": "SOURCE_1", "delta_ms": 12, "method": "NTP_ALIGN"}]
  },

  "integrity_certificate": {
    "transcript_hash"           : "SHA-256 of assembled transcript body",
    "hash_algorithm"            : "SHA-256",
    "signed_at_utc"             : "ISO-8601",
    "signing_agent"             : "AGTAA-002",
    "gap_count"                 : "integer",
    "is_complete"               : "boolean"
  }
}
```

### 4.2 Secondary Output — TRANSCRIPT_GAP_REPORT

```json
TRANSCRIPT_GAP_REPORT: {
  "transcript_id"     : "UUID",
  "session_id"        : "UUID",
  "gap_count"         : "integer",
  "gaps": [
    {
      "gap_id"          : "UUID",
      "gap_type"        : "MISSING_TURN | SOURCE_UNAVAILABLE | CLOCK_INCONSISTENCY |
                           BOT_STATUS_UNKNOWN | TELEMETRY_GAP | ATTENDANCE_AMBIGUITY",
      "affected_entity" : "turn_id or participant_id",
      "severity"        : "CRITICAL | WARNING | INFO",
      "description"     : "string",
      "resolution"      : "ANNOTATED | ESCALATED | INFERRED | IRRESOLVABLE"
    }
  ]
}
```

### 4.3 Output Schema Requirements

```
Every output must include:
  - transcript_id (UUID)
  - model_version
  - audit_reference
  - integrity_certificate.transcript_hash
  - provenance_chain (source list + hashes)

Outputs are IMMUTABLE once written to IMMUTABLE_ARCHIVE_SERVICE.
No downstream agent may modify an assembled transcript.
Dispute resolution creates a NEW transcript version — it does not mutate the original.
```

---

## SECTION 5 — ML / AI LOGIC LAYER

### 5.1 Architecture Overview

```
AI MUST ASSIST ML — AI MUST NOT REPLACE ML
All assembly, validation, and gap-classification decisions are ML/rule primary.
AI layer is used ONLY for narrative generation and ambiguity classification.
```

### 5.2 ML Layer — Clock Drift Correction Model

Session events arrive from multiple services with different clock sources. Timestamps must be normalized to a single UTC reference.

```yaml
MODEL_TYPE          : Linear regression drift correction (per-source per-session)
INPUT_FEATURES      :
  - Event order sequence number
  - Expected inter-event delta (from Redis state machine config)
  - Observed inter-event delta (from raw timestamps)
  - Source system clock identifier
OUTPUT              : corrected_timestamp_utc per event
DRIFT_THRESHOLD     : Alert if any source shows > 500ms drift vs NTP reference
TRAINING_FREQUENCY  : Continuous online model per session (no offline train needed)
VERSION_CONTROL     : Drift correction parameters stored in transcript provenance_chain
```

### 5.3 ML Layer — Gap Severity Classifier

When a signal is missing (gap detected), this model classifies its severity and impact on downstream scoring.

```yaml
MODEL_TYPE          : Gradient Boosted Multi-Class Classifier
CLASSES             : CRITICAL | WARNING | INFO
FEATURES_USED       :
  - gap_type (categorical)
  - affected_turn_count
  - affected_participant_count
  - session_type
  - missing_source_id
  - bot_confirmed_participants (count)
  - turn_veto_count
  - session_integrity_class_prior
TRAINING_FREQUENCY  : Monthly retrain on labeled historical gap records
CONFIDENCE_THRESHOLD: >= 0.75 to accept ML classification; below → rule fallback
```

### 5.4 ML Layer — Session Integrity Classifier

Classifies the overall session as `CLEAN`, `DEGRADED`, or `COMPROMISED`.

```yaml
MODEL_TYPE          : Rule-first → XGBoost binary stacking
RULE_LAYER (fires first):
  COMPROMISED if:
    - bot_confirmed_count / total_participants > 0.40
    - source_unavailable_count >= 2 CRITICAL sources
    - transcript_gap_count > (total_turns * 0.30)
  DEGRADED if:
    - bot_confirmed_count > 0 AND < threshold
    - source_unavailable_count == 1 CRITICAL source
    - clock_drift_corrections with delta > 1000ms detected
  CLEAN if:
    - All required sources collected
    - bot_confirmed_count == 0
    - gap_count == 0 or gap_severity == INFO only

ML_LAYER (applied after rule layer if ambiguous):
  Probability-weighted classification
  Features: all summary-level session signals
```

### 5.5 AI Assist Layer (Scoped — 20%)

```yaml
AI_USAGE_SCOPE:
  - Generate human-readable session narrative summary for PARENT_LLM_INSIGHT_NARRATIVE_AGENT
  - Classify ambiguous gap types that fall outside defined rule categories
  - Produce anomaly description text for ADMIN_GOVERNANCE_SERVICE review queue
  - Summarize session integrity report for dispute resolution packaging

PROMPT_GOVERNANCE:
  - All prompts versioned (AGTAA-AI-PROMPT-vX.Y.Z)
  - AI output is advisory and narrative only
  - AI output NEVER modifies structured transcript fields
  - AI output tagged with ai_generated: true in output metadata
  - Prompt version stored in audit_reference

AI MUST NOT:
  - Reclassify bot detection results
  - Modify turn outcome fields
  - Override session_integrity_class
  - Access cross-tenant session data
  - Generate participant identity narratives using PII
  - Autonomously trigger downstream events
```

---

## SECTION 6 — ASSEMBLY PIPELINE (DETERMINISTIC EXECUTION ORDER)

The assembly pipeline is **strictly ordered**. No step may begin before the prior step completes and passes its gate check.

```
PIPELINE:

STEP 1 — TRIGGER VALIDATION
  Input  : session_id, session_type, tenant_id
  Action : Validate session exists, is closed, and belongs to tenant
  Gate   : session.status == COMPLETED | TIMED_OUT
  Fail   : HALT → LOG → ESCALATE

STEP 2 — ROSTER LOCK
  Input  : PostgreSQL participant roster query
  Action : Lock participant list — no additions after this step
  Gate   : >= 1 participant resolved
  Fail   : HALT (no participants = no transcript)

STEP 3 — SOURCE COLLECTION (PARALLEL)
  Action : Simultaneously collect all sources with SLA windows
  Timeout: 3000ms total collection window
  Gate   : All REQUIRED sources for session_type collected
  Fail   : Missing required source → proceed with gap annotation (see Section 3.2)

STEP 4 — CLOCK NORMALIZATION
  Action : Apply drift correction model to all event timestamps
  Method : NTP-aligned UTC normalization per source
  Gate   : All events in provenance_chain have corrected_timestamp_utc
  Fail   : If drift > 5000ms on any source → COMPROMISED flag + escalate

STEP 5 — EVENT STREAM MERGE
  Action : Merge all source event streams into unified chronological sequence
  Method : Merge-sort by corrected_timestamp_utc; tie-break by source_priority
  Source Priority (descending): REDIS > KAFKA > POSTGRES > BOT_DETECTION > SCORING > ATTENDANCE > WEBRTC
  Gate   : Merged stream contains events for all turns in scheduled_turn_count
           OR gap annotations for missing turns
  Fail   : HALT if merged stream is empty (no events at all)

STEP 6 — TURN RECORD ASSEMBLY
  Action : For each turn_id in session, assemble complete turn_record
           from merged event stream
  Fields : All fields defined in turn_record schema (Section 4.1)
  Missing fields → annotate as null with gap_type = MISSING_TURN
  Veto  : Apply BOT_DETECTION veto signals — mark turn as VETOED if BOT_CONFIRMED

STEP 7 — PARTICIPANT RECORD ASSEMBLY
  Action : For each participant_id in roster, assemble participant_record
           from turn records + attendance + bot detection
  Compute : score_eligible (false if BOT_CONFIRMED or ABSENT)
  Compute : network_quality_class from WebRTC telemetry (if available)

STEP 8 — GAP DETECTION
  Action : Enumerate all expected signals and compare to assembled records
  Produce : TRANSCRIPT_GAP_REPORT (see Section 4.2)
  Classify: Each gap by severity using ML Gap Severity Classifier (Section 5.3)

STEP 9 — SESSION SUMMARY COMPUTATION
  Action : Compute all session_summary fields (counts, integrity class)
  Integrity: Apply Session Integrity Classifier (Section 5.4)

STEP 10 — PROVENANCE CHAIN ASSEMBLY
  Action : Record all source_hashes, collection_timestamps, drift_corrections
  Purpose : Proof of data origin and completeness for legal/audit use

STEP 11 — INTEGRITY CERTIFICATE GENERATION
  Action : SHA-256 hash of assembled transcript body (Steps 1–10 output)
  Store  : hash in integrity_certificate.transcript_hash
  Sign   : With agent identity (AGTAA-002) and model_version

STEP 12 — AUDIT RECORD WRITE
  Action : Write immutable audit record (see Section 8)
  Store  : AUDIT_COMPLIANCE_AGENT → IMMUTABLE_ARCHIVE_SERVICE
  Gate   : Audit write must succeed before transcript is released to consumers

STEP 13 — TRANSCRIPT RELEASE
  Action : Emit next_trigger_events to downstream consumers
  Gate   : Integrity certificate must be present
  Method : Event-driven (Kafka) — never synchronous push

STEP 14 — WORM ARCHIVE
  Action : Write final transcript to IMMUTABLE_ARCHIVE_SERVICE
  Retention: 7 years minimum (15 years for legal dispute sessions)
  Format : JSON + human-readable Markdown companion (generated by AI layer)
```

---

## SECTION 7 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS          : 2,000 transcript assembly jobs per hour (peak — 50K daily sessions)
LATENCY_TARGET        : < 5 seconds end-to-end assembly (p95) after session.completed trigger
                        < 15 seconds for COMPROMISED sessions requiring deep gap analysis
MAX_CONCURRENCY       : 10,000 parallel assembly jobs (tenant-partitioned)
QUEUE_STRATEGY        : Kafka topic — transcript.assembly.queue (partitioned by tenant_id)

HORIZONTAL_SCALING    : Stateless assembly workers — Kubernetes HPA on queue depth
STATELESS_EXECUTION   : All state from PostgreSQL + Redis — no local state in workers
EVENT_DRIVEN          : Triggered by session.completed event — never polled
ASYNC_PROCESSING      : Non-blocking — downstream consumers receive transcript via event
IDEMPOTENT            : Same session_id + trigger event always produces same transcript
                        (deterministic merge-sort + versioned model ensures reproducibility)

CACHING:
  - Participant roster cached in Redis (TTL: session lifetime + 10 min)
  - Source collection results cached (TTL: 30 min — for retry within SLA window)
  - Assembled transcripts NOT cached — served from IMMUTABLE_ARCHIVE_SERVICE directly

BATCH_PROCESSING:
  - Scheduled batch reconciliation runs every 6 hours via Apache Airflow
  - Reconciliation identifies sessions with TRANSCRIPT_STATUS = PENDING > 1 hour
  - Forces gap-annotated assembly with COMPROMISED classification for orphaned sessions
```

---

## SECTION 8 — SECURITY ENFORCEMENT

```yaml
ZERO_TRUST_POLICY:
  - Every collection request validated against session_id + tenant_id + caller_identity
  - No implicit trust from prior steps in the same pipeline execution
  - JWT verified for every source collection call — no session-level trust carryover

TENANT_ISOLATION:
  - All source queries parameterized with tenant_id (row-level security in PostgreSQL)
  - Kafka consumer group is tenant-namespaced
  - Zero cross-tenant data merge under any circumstance
  - If cross-tenant contamination detected at any step → HALT IMMEDIATELY + SECURITY_ALERT

DOMAIN_ISOLATION:
  - Agent operates only within declared session domains
  - No access to billing, marketplace, or idea economy service data
  - Participant identity exposed only as display_name_hash (SHA-256) — raw names not stored
  - Raw audio: NEVER accessed, NEVER stored, NEVER referenced at any step

ROLE_BASED_AUTH:
  - Write (transcript): TRANSCRIPT_AGGREGATION_AGENT only
  - Read (transcript): SCORING_ENGINE, GD_POST_SESSION_ANALYTICS_AGENT,
                       AUDIT_COMPLIANCE_AGENT, ADMIN_GOVERNANCE_SERVICE,
                       PARENT_LLM_INSIGHT_NARRATIVE_AGENT, LEGAL_DOCUMENT_GENERATION_SERVICE
  - Admin access: ADMIN_GOVERNANCE_SERVICE (with full audit trail)
  - Dispute read: FORENSICS_AGENT (restricted, time-limited, logged)

ENCRYPTION:
  - All transcript data encrypted at rest (AES-256)
  - All source collection calls over TLS 1.3
  - Integrity certificate uses HMAC-SHA256 — tamper detection on every read
  - WORM archive entries are cryptographically sealed

ACCESS_LOG:
  - Every read of an assembled transcript creates an access record
  - Access records are append-only
  - Alert if transcript is read by an agent not in the AUTHORIZED_READERS list

PII_POLICY:
  - participant display names are NEVER stored in transcript (hash only)
  - Job IDs, institute IDs, tenant IDs are stored (not PII)
  - Bot detection results are stored at participant_id level (not identity level for reports)
  - Parent-facing reports generated by AI layer use anonymized participant roles (P1, P2...)
```

---

## SECTION 9 — AUDIT & TRACEABILITY

Every assembly execution writes the following immutable record:

```json
AUDIT_RECORD: {
  "timestamp_utc"            : "ISO-8601",
  "actor_id"                 : "TRANSCRIPT_AGGREGATION_AGENT",
  "agent_version"            : "AGTAA-002-v1.0.0",
  "session_id"               : "UUID",
  "transcript_id"            : "UUID",
  "tenant_id"                : "UUID",
  "session_type"             : "GD | DOJO | INTERVIEW | ...",
  "trigger_event"            : "gd.session.completed",
  "sources_collected"        : ["SOURCE_1", "SOURCE_2", "SOURCE_3", "SOURCE_4", "SOURCE_6"],
  "sources_unavailable"      : ["SOURCE_7"],
  "input_hash"               : "SHA-256 of collected source bundle",
  "output_hash"              : "SHA-256 of assembled transcript",
  "model_version"            : "AGTAA-002-v1.0.0",
  "clock_drift_max_ms"       : 12,
  "gap_count"                : 1,
  "gap_severity_max"         : "WARNING",
  "session_integrity_class"  : "CLEAN",
  "bot_confirmed_count"      : 0,
  "bot_suspected_count"      : 1,
  "assembly_duration_ms"     : 1842,
  "pipeline_steps_completed" : 14,
  "pipeline_steps_failed"    : 0,
  "ai_assist_used"           : true,
  "ai_prompt_version"        : "AGTAA-AI-PROMPT-v1.0.0",
  "anomaly_flags"            : [],
  "worm_archive_reference"   : "UUID"
}
```

**Audit store**: `IMMUTABLE_ARCHIVE_SERVICE` — append-only, WORM-style, 7-year minimum retention
**Integrity**: Every audit record is HMAC-signed. Tamper detection on read.

---

## SECTION 10 — FAILURE POLICY

```yaml
FAILURE SCENARIOS AND RESPONSES:

TRIGGER_WITHOUT_SESSION_RECORD:
  action      : HALT_AND_REJECT
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT
  escalate_to : VOICE_GD_ORCHESTRATOR (notify session record missing)
  retry       : After 30 seconds, once. Permanent fail → human review queue.

REQUIRED_SOURCE_UNAVAILABLE (within SLA window):
  action      : PROCEED_WITH_GAP_ANNOTATION
  stop_exec   : NO (degrade gracefully)
  log_to      : OBSERVABILITY_AGENT + TRANSCRIPT_GAP_REPORT
  flag_output : session_integrity_class = DEGRADED or COMPROMISED (per Section 5.4)
  retry       : Single retry after 2 seconds before gap annotation

ALL_REQUIRED_SOURCES_UNAVAILABLE:
  action      : HALT_IMMEDIATELY
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT + OBSERVABILITY_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE
  retry       : PROHIBITED — manual review required

CLOCK_DRIFT_EXCEEDS_5000MS:
  action      : FLAG_COMPROMISED + ESCALATE
  stop_exec   : NO (produce transcript with COMPROMISED integrity class)
  log_to      : AUDIT_COMPLIANCE_AGENT + FORENSICS_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE (dispute resolution queue)

CROSS_TENANT_DATA_DETECTED:
  action      : HALT_IMMEDIATELY + SECURITY_ALERT
  stop_exec   : YES
  log_to      : ZERO_TRUST_AGENT + AUDIT_COMPLIANCE_AGENT + FORENSICS_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE + DATA_GOVERNANCE_AGENT
  retry       : PROHIBITED until security review complete

AUDIT_WRITE_FAILURE:
  action      : HALT_TRANSCRIPT_RELEASE
  stop_exec   : YES (transcript must not be released without audit record)
  log_to      : OBSERVABILITY_AGENT
  escalate_to : AUDIT_COMPLIANCE_AGENT
  retry       : 3 retries with exponential backoff (500ms, 1000ms, 2000ms)
               After 3 failures → HALT + human escalation

INTEGRITY_CERTIFICATE_GENERATION_FAILURE:
  action      : HALT_TRANSCRIPT_RELEASE
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE
  retry       : NO — investigate root cause

AI_LAYER_TIMEOUT (> 3000ms for narrative generation):
  action      : SKIP_AI_NARRATIVE — release transcript without narrative
  stop_exec   : NO
  log_to      : OBSERVABILITY_AGENT
  flag_output : narrative_generated = false

NO_SILENT_FAILURES: ENFORCED — every failure path produces a structured log record
```

---

## SECTION 11 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - VOICE_GD_ORCHESTRATOR_AGENT        : session lifecycle trigger + turn event log
  - PHONE_BOT_VOICE_DETECTION_AGENT    : bot classification records per turn
  - GD_ATTENDANCE_TRACKING_AGENT       : presence and join records
  - SCORING_ENGINE                     : intermediate per-turn scores
  - FEATURE_STORE_SERVICE              : behavioral baseline snapshot
  - WEBRTC_TRANSPORT_LAYER             : network telemetry
  - AUTH_SERVICE                       : identity binding validation
  - DOJO_SESSION_ORCHESTRATION_AGENT   : dojo match event stream
  - INTERVIEW_SERVICE                  : interview lifecycle events

DOWNSTREAM_AGENTS:
  - SCORING_ENGINE                     : receives finalized transcript as scoring input
  - GD_POST_SESSION_ANALYTICS_AGENT    : behavioral analytics consumer
  - AUDIT_COMPLIANCE_AGENT             : compliance verification consumer
  - ADMIN_GOVERNANCE_SERVICE           : moderation + dispute consumer
  - PARENT_LLM_INSIGHT_NARRATIVE_AGENT : parent report consumer
  - INTELLIGENCE_SCORING_ML_AGENT      : intelligence dimension input
  - LEGAL_DOCUMENT_GENERATION_SERVICE  : evidence package consumer
  - IMMUTABLE_ARCHIVE_SERVICE          : WORM storage receiver
  - BEHAVIOR_ANALYTICS_AGENT           : feature vector consumer
  - PASSPORT_AGGREGATION_AGENT         : skill passport update consumer

EVENT_TRIGGERS:
  CONSUMED:
    - gd.session.completed             : primary trigger
    - dojo.match.completed             : primary trigger (DOJO)
    - interview.session.completed      : primary trigger (INTERVIEW)
    - assessment.session.completed     : primary trigger (INTELLIGENCE_TEST)
    - admin.transcript.manual_request  : dispute resolution trigger

  EMITTED:
    - transcript.assembled             : consumed by SCORING_ENGINE, ANALYTICS, GOVERNANCE
    - transcript.assembly_failed       : consumed by OBSERVABILITY_AGENT, ADMIN
    - transcript.integrity_compromised : consumed by FORENSICS_AGENT, ADMIN_GOVERNANCE
    - transcript.gap_detected          : consumed by AUDIT_COMPLIANCE_AGENT
    - security.cross_tenant_violation  : consumed by ZERO_TRUST_AGENT
```

---

## SECTION 12 — PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits structured behavioral feature vectors to the Feature Store from every assembled transcript.

```json
EMIT_FEATURE_VECTOR (per participant): {
  "user_id"          : "UUID",
  "session_id"       : "UUID",
  "feature_name"     : "gd_participation_profile",
  "feature_value"    : 0.78,
  "feature_set"      : {
    "turns_completed_ratio"       : 0.83,
    "silence_ratio_mean"          : 0.12,
    "interrupt_attempts_total"    : 1,
    "speech_start_latency_mean_ms": 620,
    "turn_time_utilization_mean"  : 0.87,
    "attendance_class"            : "PRESENT",
    "bot_classification"          : "HUMAN",
    "network_quality_class"       : "GOOD",
    "session_integrity_class"     : "CLEAN"
  },
  "timestamp"        : "ISO-8601",
  "source_agent"     : "TRANSCRIPT_AGGREGATION_AGENT"
}
```

These vectors feed:
- `BEHAVIOR_ANALYTICS_AGENT` — longitudinal participation drift
- `INTELLIGENCE_SCORING_ML_AGENT` — interpersonal + linguistic intelligence scoring
- `SKILL_RANK_ENGINE` — communication skill vector update
- `CAREER_PROBABILITY_MODEL` — GD performance as career predictor input
- `PASSPORT_AGGREGATION_AGENT` — Skill Passport update for GD competency

---

## SECTION 13 — GROWTH ENGINE HOOK

```yaml
ON transcript.assembled WITH session_integrity_class = CLEAN:
  EMIT:
    - XP_UPDATE_EVENT      : per participant (based on turns_completed + time_utilized)
    - RANK_UPDATE_EVENT    : feeds SKILL_RANK_ENGINE for communication domain
    - SHARE_TRIGGER_EVENT  : if participant score qualifies for leaderboard

ON transcript.assembled WITH session_integrity_class = COMPROMISED:
  EMIT:
    - XP_UPDATE_EVENT      : BLOCKED for bot_confirmed participants
    - RANK_UPDATE_EVENT    : BLOCKED — no rank update from compromised session
    - ADMIN_REVIEW_TRIGGER : session flagged for manual review before any rank update

ON transcript.assembled WITH bot_confirmed_count > 0:
  EMIT:
    - ANTI_FRAUD_AGENT escalation per confirmed bot participant
    - SCORING_ENGINE veto enforcement confirmation
```

---

## SECTION 14 — INNOVATION ECONOMY COMPATIBILITY

Not primary scope of this agent. However:

```yaml
IF session_type = INTELLIGENCE_TEST AND transcript is assembled:
  EMIT to IDEA_DNA_AGENT:
    - IDEA_VECTOR           : semantic summary of test response patterns (anonymized)
    - For internal IP tracking of novel test session architectures only

IF novel assembly algorithm is developed internally:
  EMIT to ROYALTY_ENGINE:
    - ORIGINALITY_SCORE
    - SIMILARITY_HASH
    - For internal IP protection and licensing tracking
```

---

## SECTION 15 — PERFORMANCE MONITORING

```yaml
METRICS (via Prometheus → Grafana):

  assembly_success_rate_pct          : % of triggered sessions successfully assembled
  assembly_latency_p50_ms            : Median assembly time
  assembly_latency_p95_ms            : 95th percentile (target < 5000ms)
  assembly_latency_p99_ms            : 99th percentile (alert if > 15000ms)
  source_collection_failure_rate_pct : % of collection attempts that missed SLA
  gap_detection_rate_pct             : % of transcripts with at least one gap
  integrity_compromised_rate_pct     : % of sessions classified COMPROMISED
  integrity_degraded_rate_pct        : % of sessions classified DEGRADED
  clock_drift_correction_count       : Total drift corrections applied per hour
  audit_write_failure_count          : Total audit write failures (target = 0)
  worm_archive_lag_seconds           : Time between assembly and WORM write
  cross_tenant_violation_count       : Must be 0 always — alert immediately if > 0

DASHBOARDS (Grafana):
  - Transcript Assembly Pipeline Health (per tenant, per day)
  - Source Collection SLA Compliance Heatmap
  - Session Integrity Class Distribution (CLEAN / DEGRADED / COMPROMISED)
  - Gap Type Breakdown (by gap_type, by severity)
  - Assembly Latency Percentile Timeline
  - Bot Detection Integration Accuracy (from downstream scoring feedback)
  - Cross-Tenant Violation Monitor (must always show zero)

ALERTS:
  - assembly_success_rate < 99% in any 1-hour window → PAGE ADMIN
  - assembly_latency_p99 > 20000ms → ALERT + investigate queue depth
  - integrity_compromised_rate > 3% in any day → ALERT DATA_GOVERNANCE_AGENT
  - audit_write_failure_count > 0 → PAGE IMMEDIATELY
  - cross_tenant_violation_count > 0 → CRITICAL PAGE + HALT NEW ASSEMBLIES
  - worm_archive_lag > 60 seconds → ALERT IMMUTABLE_ARCHIVE_SERVICE
```

---

## SECTION 16 — VERSIONING POLICY

```yaml
VERSION_FORMAT      : AGTAA-002-vMAJOR.MINOR.PATCH
MUTATION_POLICY     : Add-only — no field removal from SESSION_TRANSCRIPT schema
BACKWARD_COMPAT     : All prior transcript schemas must remain readable
ROLLBACK_SAFETY     : Previous version retained 90 days post-rollout
MIGRATION_DOC       : Required for every MINOR or MAJOR version bump
CHANGELOG           : Append-only CHANGELOG.md co-versioned with agent spec
TRANSCRIPT_VERSION  : Stored in every transcript (model_version field)
GAP_REPORT_VERSION  : Versioned separately (AGTAA-GAP-vX.Y.Z)
AI_PROMPT_VERSION   : Versioned separately (AGTAA-AI-PROMPT-vX.Y.Z)
SCHEMA_REGISTRY     : All output schemas registered in ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
```

---

## SECTION 17 — NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
✗  Store or reference raw audio at any step
✗  Store participant display names (hash only)
✗  Merge data from different tenants in a single transcript
✗  Release a transcript without a valid integrity_certificate
✗  Release a transcript without a committed audit record
✗  Allow any downstream agent to modify an assembled transcript
✗  Skip STEP 12 (audit write) under any execution path
✗  Process a session that has not been marked COMPLETED or TIMED_OUT
✗  Allow AI layer to modify any structured transcript field
✗  Override BOT_CONFIRMED classification from PHONE_BOT_VOICE_DETECTION_AGENT
✗  Execute retroactive re-assembly that mutates prior transcript records
✗  Create hidden assembly logic outside the documented 14-step pipeline
✗  Accept null values for required session metadata fields
✗  Operate outside declared session domains
✗  Produce partial transcripts without gap annotation and integrity classification
✗  Write transcripts to any store other than IMMUTABLE_ARCHIVE_SERVICE as final destination
```

---

## SECTION 18 — DEPLOYMENT CONFIGURATION

```yaml
KUBERNETES_NAMESPACE    : realtime
SERVICE_TYPE            : Stateless microservice (Deployment)
REPLICA_MIN             : 3
REPLICA_MAX             : 100 (HPA on Kafka consumer lag)
HPA_TRIGGER             : transcript.assembly.queue lag > 500 messages
RESOURCE_REQUEST        : 500m CPU, 1Gi RAM
RESOURCE_LIMIT          : 4000m CPU, 4Gi RAM (source collection + merge-sort is memory-bound)
LIVENESS_PROBE          : /health/live
READINESS_PROBE         : /health/ready (checks Kafka, Redis, PostgreSQL connectivity)
STARTUP_PROBE           : 20s grace

KAFKA_TOPIC_IN          : transcript.assembly.queue (partitioned by tenant_id)
KAFKA_TOPIC_OUT         :
  - transcript.assembled
  - transcript.assembly_failed
  - transcript.integrity_compromised
  - transcript.gap_detected
  - security.cross_tenant_violation

POSTGRES_USAGE          : Session metadata, participant roster, audit logs
REDIS_USAGE             : Roster cache, source result cache, assembly lock (idempotency)
AIRFLOW_SCHEDULE        : Every 6 hours — orphaned session reconciliation job
SECRETS_MANAGED_BY      : HashiCorp Vault
CONFIG_MANAGED_BY       : Kubernetes ConfigMap (versioned)
FEATURE_FLAGS           : Unleash (per-tenant AI narrative generation rollout)
WORM_ARCHIVE            : IMMUTABLE_ARCHIVE_SERVICE (MinIO WORM policy + Velero backup)

ENVIRONMENTS            :
  - dev     : Mock source collection, no WORM archive, reduced SLA windows
  - staging : Full source collection, test WORM archive, full pipeline
  - prod    : Full stack, immutable audit, Vault secrets, 7-year WORM retention
```

---

## SECTION 19 — INTEGRATION ARCHITECTURE MAP

```
SESSION LIFECYCLE EVENTS (Kafka)
         ↓
gd.session.completed / dojo.match.completed / interview.session.completed
         ↓
TRANSCRIPT_AGGREGATION_AGENT (this agent)
         ↓
STEP 1–3: VALIDATE + LOCK ROSTER + COLLECT SOURCES (parallel)
  ┌──────────┬──────────┬──────────┬──────────┬──────────┬──────────┐
  ↓          ↓          ↓          ↓          ↓          ↓          ↓
REDIS      KAFKA    POSTGRES  BOT_DETECT  SCORING  ATTENDANCE  WEBRTC
         ↓
STEP 4–11: NORMALIZE → MERGE → ASSEMBLE → CERTIFY
         ↓
STEP 12: AUDIT RECORD WRITE (mandatory gate)
         ↓
STEP 13: TRANSCRIPT RELEASE (Kafka events)
  ┌──────────┬──────────┬──────────┬──────────┬──────────┐
  ↓          ↓          ↓          ↓          ↓          ↓
SCORING  ANALYTICS  GOVERNANCE  PARENT_AI  LEGAL  PASSPORT
ENGINE    AGENT      SERVICE    NARRATIVE   DOCS   AGENT
         ↓
STEP 14: WORM ARCHIVE
IMMUTABLE_ARCHIVE_SERVICE (MinIO, 7-year retention)
```

---

## SECTION 20 — ANTI-PATTERNS EXPLICITLY PROHIBITED

| Anti-Pattern | Reason |
|---|---|
| Releasing transcript before audit write | Creates untraced output — compliance violation |
| Merging sources without clock normalization | Produces causally inconsistent evidence record |
| Allowing downstream agents to mutate transcript | Destroys evidence integrity — legal risk |
| Skipping gap detection on missing sources | Produces incomplete transcript with no disclosure |
| Storing participant names in transcript | PII violation — display_name_hash only |
| Raw audio access at any layer | Privacy violation — architecture contract breach |
| Synchronous push to downstream consumers | Creates coupling — must be event-driven only |
| Re-running assembly on a committed transcript | Retroactive mutation — prohibited |
| Cross-tenant source collection in single assembly | Zero-trust and data isolation violation |
| AI layer classifying bot detection results | Non-deterministic — rule engine governs that layer |
| Omitting model_version from any output | Untraceable audit — compliance failure |
| Producing transcript without integrity_certificate | Unverifiable evidence — legally void |
| Assembling sessions still in ACTIVE state | Premature assembly on live session |

---

## SECTION 21 — FINAL LOCK DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════════════╗
║                          AGENT SPECIFICATION — SEALED                              ║
║                                                                                      ║
║  This document is the authoritative, governing specification for the                ║
║  TRANSCRIPT_AGGREGATION_AGENT operating within the Ecoskiller Antigravity           ║
║  platform.                                                                           ║
║                                                                                      ║
║  Any deviation, abbreviation, re-interpretation, or undocumented extension          ║
║  of this specification constitutes a compliance violation and must be               ║
║  escalated to ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT before implementation.       ║
║                                                                                      ║
║  AGENT_ID        : AGTAA-002                                                         ║
║  VERSION         : v1.0.0                                                            ║
║  SESSION_TYPES   : GD | DOJO | INTERVIEW | INTELLIGENCE_TEST | CERTIFICATION        ║
║  SEALED BY       : ECOSKILLER INTELLIGENCE & INNOVATION CORE                        ║
║  GOVERNANCE      : ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT                         ║
║  MUTATION LOCK   : Add-only — version bump required for any change                  ║
║  STATUS          : ACTIVE · PRODUCTION-READY                                        ║
╚══════════════════════════════════════════════════════════════════════════════════════╝
```
