# 🔒 AGENT_HEALTH_WATCHDOG_AGENT.md
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPECIFICATION
**Status:** FINAL · SEALED · GOVERNED · DETERMINISTIC  
**Artifact Class:** Tier-0 Infrastructure Governance Agent  
**Mutation Policy:** ADD-ONLY via version bump  
**Interpretation Authority:** NONE  
**Creative Deviation:** FORBIDDEN  
**Assumption Filling:** FORBIDDEN  
**Default Behavior:** DENY  
**Override Authority:** NONE  

---

## 🔐 EXECUTION MODE DECLARATION

```
AGENT_LOCK_STATUS            = SEALED
EXECUTION_MODE               = DETERMINISTIC + VALIDATED
MUTATION_POLICY              = ADD_ONLY
CREATIVE_INTERPRETATION      = FORBIDDEN
ASSUMPTION_FILLING           = FORBIDDEN
DEFAULT_BEHAVIOR             = DENY
FAILURE_MODE                 = STOP_EXECUTION → LOG_INCIDENT → ESCALATE
OVERRIDE_AUTHORITY           = NONE
BYPASS_ATTEMPTS              = CLASSIFIED AS SECURITY_VIOLATION → AUTO_ESCALATE_P0
SELF_MODIFICATION            = ABSOLUTELY FORBIDDEN
AUTO_REMEDIATION             = FORBIDDEN (monitor + alert only)
DECISION_AUTONOMY            = ZERO (beyond defined rules)
```

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME           = AGENT_HEALTH_WATCHDOG_AGENT
AGENT_ID             = AHWA-ANTIGRAVITY-001
SYSTEM_ROLE          = Autonomous Agent Lifecycle Monitor, Integrity Enforcer,
                       Rogue Behavior Detector & Governance Escalation Authority
PRIMARY_DOMAIN       = Platform Operations / Agent Governance / Compliance
EXECUTION_MODE       = Deterministic + Validated
DATA_SCOPE           = All registered autonomous agents, their execution states,
                       behavior logs, output contracts, compliance posture,
                       heartbeat signals, and inter-agent event chains
TENANT_SCOPE         = Platform-Wide (not tenant-scoped) with per-tenant
                       agent behavior isolation enforcement
FAILURE_POLICY       = Halt on Ambiguity → Log → Escalate → No Silent Failure
AGENT_CLASS          = Tier-0 Meta-Governance Agent
                       (governs ALL other agents — cannot be governed by them)
PLATFORM             = Ecoskiller Antigravity
ARCHITECTURE         = Microservices + Event-Driven
SCALE_TARGET         = 10M–100M Users / Supports monitoring of 50–500 registered agents
```

> **Critical Distinction:** The `INTEGRATION_HEALTH_MONITOR_AGENT` watches **integration points** (APIs, Kafka topics, vendor connections, database pools). The `AGENT_HEALTH_WATCHDOG_AGENT` watches **agents themselves** — their liveness, execution integrity, behavioral compliance, output contract conformance, rogue state detection, and governance posture. These are complementary but non-overlapping mandates.

This agent **never assumes** missing specifications. If any agent registration, behavioral contract, or health threshold is absent, this agent halts and escalates. It does not self-heal through assumption.

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

The Ecoskiller Antigravity platform operates a distributed fleet of 50–500 autonomous agents across ML, AI, governance, gamification, data, compliance, and infrastructure domains. Each agent has:

- A declared execution contract (input schema, output schema, scope)
- A defined behavioral boundary (what it may and may not do)
- A dependency graph (upstream/downstream agents)
- A compliance posture (RBAC, tenant isolation, audit emission)
- A performance SLA (latency, confidence, throughput)

Without a dedicated watchdog, the following failure modes are **undetectable until user-visible damage occurs:**

| Failure Class | Risk |
|---|---|
| Silent agent death | No heartbeat, no escalation, downstream starvation |
| Zombie agent | Agent appears alive but produces no valid output |
| Rogue agent | Agent executes outside declared scope (scope creep) |
| Contract drift | Agent output no longer conforms to declared schema |
| Confidence collapse | Agent producing outputs below minimum confidence threshold |
| Audit evasion | Agent failing to emit required audit log entries |
| Tenant isolation breach | Agent processing cross-tenant data |
| Circular dependency lock | Agent chain deadlocked in an event loop |
| Cascading failure | One failed agent silently starving an entire pipeline |
| Governance bypass | Agent attempting to override compliance or governance agents |

The **AGENT_HEALTH_WATCHDOG_AGENT (AHWA)** exists to:

1. Monitor the **liveness, responsiveness, and execution state** of every registered agent in the platform.
2. Validate that every agent's **output conforms to its declared output contract** (schema, confidence floor, version reference, audit reference).
3. Detect **rogue behavior** — any agent operating outside its declared scope, data boundaries, or execution mandate.
4. Enforce **audit completeness** — every agent must emit structured audit entries; AHWA detects agents evading this requirement.
5. Detect **confidence collapse** — agents producing outputs below declared minimum confidence thresholds.
6. Detect **inter-agent deadlock** and **cascade failure propagation**.
7. Detect **schema drift** in agent output contracts — outputs that no longer match registered schema.
8. Enforce **tenant isolation** across all agent executions.
9. Monitor **ML model drift** indicators emitted by ML-based agents.
10. Provide the **authoritative agent registry status** to the platform governance layer.
11. Emit structured health signals to OBSERVABILITY_AGENT and GOVERNANCE_AGENT.
12. **Never auto-remediate** — detect, log, escalate, and halt only.

### What Input It Consumes

- Heartbeat pulses from all registered agents (mandatory, on defined TTL)
- Agent execution completion events (structured, per-agent contract)
- Agent output samples (for contract conformance validation)
- Audit log emission confirmations from AUDIT_LOG_AGENT
- Kafka dead letter queue accumulation signals (per agent topic)
- Inter-agent dependency event chain traces (from Jaeger/tracing layer)
- Agent registration/deregistration events (from AGENT_REGISTRY_SERVICE)
- ML model version and drift alerts (from ML-based agents)
- Confidence score distributions (sampled from agent outputs)
- Tenant_id resolution confirmations (from TENANT_MANAGEMENT_SERVICE)
- RBAC authorization verification events (from AUTHORIZATION_SERVICE)
- Feature flag state changes affecting agent behavior

### What Output It Produces

- Per-agent health status reports (ALIVE / DEGRADED / ZOMBIE / ROGUE / DEAD)
- Platform-wide agent fleet health dashboard feed
- Rogue behavior detection alerts (P0 — immediate escalation)
- Audit evasion alerts (P0 — compliance critical)
- Tenant isolation violation alerts (P0 — security critical)
- Confidence collapse alerts (P1)
- Schema drift reports (P1)
- Cascade failure propagation maps
- Inter-agent deadlock detection reports
- ML model drift escalation triggers
- Agent resurrection requests (halt + escalation — NOT self-healing)
- Immutable watchdog audit log entries
- FEATURE_STORE_AGENT behavioral health vectors (platform-level only)

### Upstream Agents / Services That Feed This Agent

```
MANDATORY UPSTREAM (agent halts if any become unavailable):
  - AGENT_REGISTRY_SERVICE        (master list of all registered agents)
  - KAFKA_BROKER_CLUSTER          (inter-agent event bus)
  - AUDIT_LOG_AGENT               (audit emission confirmation signals)
  - TENANT_MANAGEMENT_SERVICE     (tenant_id resolution)
  - AUTHORIZATION_SERVICE         (RBAC verification)
  - DISTRIBUTED_TRACING_SERVICE   (Jaeger — inter-agent chain traces)

OPTIONAL UPSTREAM (degraded mode if unavailable):
  - OBSERVABILITY_AGENT           (baseline metric context)
  - FEATURE_FLAG_SERVICE          (agent behavior flag states)
  - DEPLOYMENT_AGENT              (deployment event context)
  - SCHEMA_REGISTRY_SERVICE       (output contract schemas)
```

### Downstream Agents / Services That Depend on This Agent

```
PRIMARY DOWNSTREAM:
  - GOVERNANCE_AGENT              (receives rogue/breach escalations)
  - OBSERVABILITY_AGENT           (receives full agent fleet health feed)
  - INCIDENT_MANAGEMENT_AGENT     (receives P0/P1 escalation triggers)
  - AUDIT_LOG_AGENT               (receives AHWA's own immutable entries)
  - NOTIFICATION_SERVICE          (triggers platform ops / compliance alerts)

SECONDARY DOWNSTREAM:
  - FEATURE_STORE_AGENT           (agent fleet behavioral health vectors)
  - COMPLIANCE_AGENT              (receives audit evasion + governance bypass alerts)
  - SECURITY_AGENT                (receives tenant isolation + rogue behavior alerts)
  - PLATFORM_STATUS_SERVICE       (agent fleet health summary for ops dashboards)
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

All inputs must conform exactly. **Zero null tolerance on required fields. Reject malformed signals. Log all validation failures.**

```json
INPUT_SCHEMA: {
  "required_fields": [
    "signal_id",
    "signal_type",
    "source_agent_id",
    "source_agent_name",
    "source_agent_version",
    "signal_timestamp_utc",
    "payload_schema_version",
    "environment"
  ],
  "optional_fields": [
    "tenant_id",
    "domain_track",
    "execution_id",
    "confidence_score",
    "output_schema_checksum",
    "audit_reference_emitted",
    "ml_model_version",
    "ml_drift_flag",
    "execution_latency_ms",
    "kafka_topic_consumed",
    "kafka_topic_produced",
    "upstream_agent_ids",
    "downstream_agent_ids",
    "scope_declaration_hash",
    "rbac_role_used",
    "feature_flag_states"
  ],
  "signal_type_enum": [
    "AGENT_HEARTBEAT",
    "AGENT_EXECUTION_COMPLETE",
    "AGENT_EXECUTION_FAILED",
    "AGENT_OUTPUT_SAMPLE",
    "AGENT_AUDIT_CONFIRMATION",
    "AGENT_REGISTRATION",
    "AGENT_DEREGISTRATION",
    "AGENT_ML_DRIFT_ALERT",
    "AGENT_CONFIDENCE_REPORT",
    "AGENT_SCOPE_VIOLATION_SELF_REPORT",
    "AGENT_DEPENDENCY_TIMEOUT"
  ],
  "validation_rules": [
    "signal_id MUST be UUID v4 — reject all other formats",
    "source_agent_id MUST resolve in AGENT_REGISTRY_SERVICE — reject unregistered agents",
    "source_agent_name MUST match registry entry for source_agent_id",
    "source_agent_version MUST be semver format (MAJOR.MINOR.PATCH) — reject malformed",
    "signal_timestamp_utc MUST be ISO-8601 UTC — reject malformed",
    "signal_timestamp_utc MUST NOT be in future by more than 5 seconds — reject clock-skew signals",
    "signal_timestamp_utc MUST NOT be older than 10 minutes — reject stale signals",
    "payload_schema_version MUST match current or N-1 version — reject older",
    "environment MUST be one of [dev, test, staging, production]",
    "signal_type MUST be a declared enum value — reject all others",
    "confidence_score MUST be float 0.0–1.0 if provided — reject out-of-range",
    "execution_latency_ms MUST be positive integer if provided",
    "output_schema_checksum MUST be SHA-256 hex string if provided",
    "domain_track MUST be one of [Arts, Commerce, Science, Technology, Administration, PLATFORM_WIDE] if provided"
  ],
  "security_checks": [
    "Validate mTLS source certificate against registered agent identity",
    "Validate JWT bearer token against PLATFORM_OPS_ROLE authorization",
    "Reject signals from agents attempting to report ON other agents (agents report only on themselves)",
    "Reject payload exceeding 256KB",
    "Validate TLS 1.3 minimum on all signal transport",
    "Detect and reject any signal containing PII patterns (email, phone, SSN regex scanning)",
    "Validate scope_declaration_hash against registered agent scope hash — flag mismatch as rogue indicator"
  ],
  "domain_checks": [
    "If tenant_id is present: verify it resolves to an active tenant",
    "If domain_track is present: verify it matches the agent's registered domain constraint",
    "Cross-domain signals without explicit governance grant = REJECT + SECURITY_FLAG"
  ],
  "null_policy": {
    "required_fields": "ZERO NULL TOLERANCE — reject and log immediately",
    "optional_fields": "NULL allowed but must be explicitly declared as null — omission forbidden",
    "action_on_rejection": "LOG_VALIDATION_FAILURE → EMIT_REJECTION_EVENT → NO_PARTIAL_PROCESSING"
  }
}
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

All outputs must include confidence, version reference, and full traceability. No partial outputs permitted under any circumstance.

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "watchdog_report_id":          "UUID",
    "evaluation_timestamp_utc":    "ISO-8601",
    "evaluated_agent_id":          "string",
    "evaluated_agent_name":        "string",
    "evaluated_agent_version":     "string (semver)",
    "environment":                 "dev | test | staging | production",
    "agent_liveness_status":       "ALIVE | DEGRADED | ZOMBIE | DEAD | UNKNOWN",
    "agent_behavioral_status":     "COMPLIANT | DRIFTED | ROGUE | SUSPENDED",
    "agent_audit_status":          "EMITTING | PARTIAL | EVADING",
    "agent_output_contract_status":"CONFORMANT | SCHEMA_DRIFT | CONFIDENCE_COLLAPSE | BREACH",
    "tenant_isolation_status":     "CONFIRMED | VIOLATION_DETECTED | NOT_APPLICABLE",
    "ml_drift_status":             "STABLE | WARNING | CRITICAL | NOT_APPLICABLE",
    "cascade_risk_flag":           "boolean",
    "deadlock_detected":           "boolean",
    "rogue_behavior_indicators":   ["array of rogue indicator codes"],
    "anomaly_flags":               ["array of anomaly codes"],
    "recommended_action":          "NONE | ALERT | HALT_AGENT | ESCALATE_P0 | ESCALATE_P1",
    "escalation_target":           "string | null",
    "agent_last_heartbeat_utc":    "ISO-8601 | null",
    "agent_last_execution_utc":    "ISO-8601 | null",
    "heartbeat_gap_seconds":       "integer | null",
    "consecutive_failures":        "integer",
    "confidence_score_last":       "float | null",
    "confidence_score_trend":      "STABLE | DECLINING | COLLAPSED | NOT_APPLICABLE"
  },
  "confidence_score":   "0.0–1.0 (float, two decimal places)",
  "model_version":      "AHWA-RULES-ENGINE-v{major}.{minor}.{patch}",
  "audit_reference":    "UUID (immutable, append-only)",
  "next_trigger_event": [
    "AGENT_HEALTH_REPORT_EMITTED",
    "AGENT_LIVENESS_ALERT (conditional)",
    "ROGUE_BEHAVIOR_DETECTED (conditional)",
    "AUDIT_EVASION_DETECTED (conditional)",
    "TENANT_ISOLATION_VIOLATION (conditional)",
    "ML_DRIFT_ESCALATION (conditional)",
    "CASCADE_FAILURE_WARNING (conditional)",
    "DEADLOCK_DETECTED (conditional)",
    "AGENT_HALT_REQUESTED (conditional)"
  ]
}
```

**Confidence Score Interpretation:**
```
1.00       = All signals complete, all checks deterministic, full traceability
0.80–0.99  = Minor signal gaps, output valid, flagged for review
0.60–0.79  = Significant signal gaps, WARNING-grade output
< 0.60     = INSUFFICIENT DATA — halt output, do NOT emit agent status,
             escalate to OPS_TEAM for manual investigation
```

---

## 5️⃣ ML / AI LOGIC LAYER

This agent operates as **98% Deterministic Rules Engine + 2% Statistical Pattern Recognition**. No LLM is used anywhere in any decision path.

### Rules Engine (Primary — 98%)

```
ENGINE_TYPE           = Deterministic Rules Engine
EXECUTION_MODEL       = Threshold evaluation + Contract comparison +
                        Behavioral boundary enforcement
LIVENESS_DETECTION    = Heartbeat TTL expiry (per-agent configurable TTL)
ZOMBIE_DETECTION      = Heartbeat present + zero valid output for > 2x TTL window
ROGUE_DETECTION       = Output scope_declaration_hash mismatch +
                        Cross-domain data access attempt +
                        Tenant isolation violation +
                        Governance agent override attempt
AUDIT_EVASION         = No audit_reference_emitted flag in N consecutive executions
CONFIDENCE_COLLAPSE   = confidence_score < declared agent minimum for N consecutive outputs
SCHEMA_DRIFT          = output_schema_checksum mismatch against SCHEMA_REGISTRY
VERSION_CONTROL       = Rules engine version stored immutably in every output
```

### Statistical Pattern Recognition (Secondary — 2%)

```
MODEL_TYPE            = Behavioral Anomaly Detection
                        (Isolation Forest on per-agent execution time-series)

FEATURES_USED:
  - execution_latency_ms (rolling 5m, 15m, 1h per agent)
  - confidence_score (rolling 5m, 15m, 1h per agent)
  - audit_emission_rate (per agent per hour)
  - output_volume_per_minute (per agent)
  - heartbeat_gap_distribution (per agent)
  - kafka_topic_produced_rate (per agent)
  - consecutive_failure_count (per agent)

TRAINING_FREQUENCY    = Weekly (platform-level telemetry only, no PII, no tenant data)
DRIFT_DETECTION:
  - KL divergence threshold: 0.1 → retrain flag
  - Model accuracy drop > 5% → retrain flag
VERSION_CONTROL:
  - ML model version: ML-BEHAVIORAL-v{major}.{minor}.{patch}
  - Old models retained minimum 90 days
TRAINING_DATA_POLICY:
  - NO user PII
  - NO tenant-specific content
  - ONLY anonymized per-agent telemetry metrics
```

### AI Usage Policy

```
AI_USAGE_SCOPE        = NONE in any real-time decision path
                        AI may assist in post-incident Root Cause Analysis (RCA)
                        summarization ONLY — offline, non-realtime, governance-approved
PROMPT_GOVERNANCE     = Versioned | Deterministic | No creative interpretation
                        AI MUST NOT influence agent status determinations
                        AI MUST NOT recommend architectural changes autonomously
                        AI MUST NOT interpret ambiguous behavioral signals
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS           = 10,000–100,000 agent signals/sec at peak
                         (500 agents × up to 200 signals/sec each)
LATENCY_TARGET         = P99 < 150ms for signal ingestion
                         P99 < 400ms for health report generation per agent
MAX_CONCURRENCY        = 5,000 parallel signal processors
QUEUE_STRATEGY         = Kafka-backed ingestion (topic: ahwa.signals.raw)
                         Dead Letter Queue: ahwa.signals.dlq
                         Priority queue for P0 signals: ahwa.signals.priority
SCALING_MODEL          = Horizontal auto-scaling (Kubernetes HPA)
                         Stateless execution per AHWA instance
EXECUTION_STATE        = Stateless (all state in Redis + PostgreSQL)
IDEMPOTENCY            = All signal processing idempotent
                         (Idempotency key: signal_id + source_agent_id + signal_timestamp_utc)
ASYNC_PROCESSING       = Yes — Kafka consumer group model
EVENT_DRIVEN           = Yes — all outputs emitted as Kafka events
CIRCUIT_BREAKER        = Enabled on all upstream dependency calls
                         (5 failures in 30s → open, retry after 60s)
BACKPRESSURE           = Kafka consumer lag > 5,000 → emit AHWA_BACKPRESSURE_WARNING
PRIORITY_PROCESSING    = P0 signals (rogue/tenant violation/audit evasion) bypass
                         standard queue → priority channel with < 50ms target latency
```

---

## 7️⃣ COMPLETE AGENT REGISTRY & MONITORING SCOPE

This section declares every category of agent that AHWA monitors. The complete registry is maintained in AGENT_REGISTRY_SERVICE — AHWA treats that registry as the source of truth.

### 7A — Agent Classification Taxonomy

```
TIER-0 (Meta-Governance — AHWA monitors but cannot be monitored by peers):
  - AGENT_HEALTH_WATCHDOG_AGENT (self-monitored via separate liveness probe)
  - GOVERNANCE_AGENT
  - COMPLIANCE_AGENT
  - SECURITY_AGENT

TIER-1 (Platform Infrastructure):
  - INTEGRATION_HEALTH_MONITOR_AGENT
  - OBSERVABILITY_AGENT
  - INCIDENT_MANAGEMENT_AGENT
  - AUDIT_LOG_AGENT
  - SCHEMA_REGISTRY_SERVICE
  - TENANT_ISOLATION_AGENT
  - DEPLOYMENT_AGENT
  - FEATURE_FLAG_SERVICE

TIER-2 (Intelligence & ML):
  - SKILL_GAP_ANALYSIS_AGENT
  - JOB_MATCH_SCORING_AGENT
  - PLACEMENT_PROBABILITY_AGENT
  - OFFER_ACCEPTANCE_PREDICTION_AGENT
  - RANKING_DISCOVERY_AGENT
  - RECOMMENDATION_ENGINE_AGENT
  - RESUME_PARSING_AGENT
  - RECRUITER_BEHAVIOR_ANALYTICS_AGENT
  - AI_EXPLAINABILITY_AGENT
  - INTELLIGENCE_DETECTION_ENGINE_AGENT (EIE/HIA — Multiple Intelligences)
  - FEATURE_STORE_AGENT
  - ML_DRIFT_MONITOR_AGENT

TIER-3 (Domain — Dojo):
  - DOJO_MATCH_ENGINE_AGENT
  - DOJO_SCENARIO_ENGINE_AGENT
  - DOJO_SCORING_ENGINE_AGENT
  - DOJO_RATING_ENGINE_AGENT
  - DOJO_BELT_ENGINE_AGENT
  - DOJO_TOURNAMENT_ENGINE_AGENT
  - DOJO_REPLAY_ENGINE_AGENT
  - DOJO_ANALYTICS_ENGINE_AGENT
  - DOJO_MENTOR_CONTROL_ENGINE_AGENT
  - DOJO_ANTI_CHEAT_AGENT

TIER-3 (Domain — Job Portal):
  - JOB_VERIFICATION_AGENT
  - APPLICATION_TRACKING_AGENT
  - OFFER_LOCKING_AGENT
  - SME_RELIABILITY_SCORING_AGENT

TIER-3 (Domain — Skill & Education):
  - LEARNING_PATH_AGENT
  - INDUSTRY_DEMAND_MAPPING_AGENT
  - TRAINER_MARKET_AGENT
  - PERFORMANCE_LINKED_LEARNING_AGENT

TIER-3 (Domain — Project Execution):
  - MILESTONE_EVALUATION_AGENT
  - EVIDENCE_VERIFICATION_AGENT
  - PORTFOLIO_GENERATION_AGENT
  - MENTOR_ASSIGNMENT_AGENT

TIER-3 (Domain — ERP):
  - INSTITUTE_ERP_SYNC_AGENT
  - CORPORATE_HIRING_WORKFLOW_AGENT
  - COMPLIANCE_AUDIT_ERP_AGENT
  - ANALYTICS_ROI_AGENT

TIER-4 (Gamification & Growth):
  - XP_UPDATE_AGENT
  - RANK_UPDATE_AGENT
  - ACHIEVEMENT_ENGINE_AGENT
  - BELT_PROGRESSION_AGENT
  - REFERRAL_TRACKING_AGENT
  - LEADERBOARD_AGENT
  - SHARE_TRIGGER_AGENT

TIER-4 (Communication & Notifications):
  - NOTIFICATION_DISPATCH_AGENT
  - EMAIL_DELIVERY_AGENT
  - SMS_DELIVERY_AGENT
  - PUSH_NOTIFICATION_AGENT
  - IN_APP_NOTIFICATION_AGENT

TIER-4 (Billing & Commerce):
  - BILLING_LEDGER_AGENT
  - SUBSCRIPTION_STATE_AGENT
  - PAYMENT_RECONCILIATION_AGENT
  - COMMISSION_CALCULATION_AGENT

TIER-4 (Reputation & Moderation):
  - REPUTATION_SCORING_AGENT
  - CONTENT_MODERATION_AGENT
  - TRUST_VERIFICATION_AGENT
  - ANTI_SPAM_AGENT

TIER-4 (Innovation Economy — if applicable):
  - IDEA_DNA_AGENT
  - ROYALTY_ENGINE_AGENT
  - COPY_DETECTION_ENGINE_AGENT
  - ORIGINALITY_SCORING_AGENT
```

---

## 8️⃣ BEHAVIORAL COMPLIANCE MONITORING

This is the core differentiation of AHWA from all other monitoring agents. AHWA monitors **what agents DO**, not just whether they are alive.

### 8A — Liveness Classification

```
LIVENESS_STATES:

ALIVE:
  Definition: Heartbeat received within declared TTL.
              Last execution completed within 2× TTL.
              No consecutive failures in last N=3 executions.
  Action:     None. Continue monitoring.

DEGRADED:
  Definition: Heartbeat received within 2× TTL (late but present).
              OR: Last execution had confidence_score below agent minimum (single occurrence).
              OR: Execution latency P95 > 1.5× declared SLA.
  Action:     EMIT AGENT_DEGRADED_EVENT.
              NOTIFY OPS_TEAM (non-urgent).
              Continue monitoring with increased frequency (2× normal).

ZOMBIE:
  Definition: Heartbeat received within TTL (agent appears alive)
              BUT zero valid output produced for > 2× declared execution cycle.
              OR: All outputs in last N=5 executions below confidence floor.
              OR: Audit emission rate = 0 for > 3× declared execution cycle.
  Action:     EMIT AGENT_ZOMBIE_DETECTED_EVENT (P1).
              ESCALATE_TO = OPS_TEAM + GOVERNANCE_AGENT.
              Flag for manual inspection.
              DO NOT auto-restart.

DEAD:
  Definition: No heartbeat received for > 3× declared TTL.
              AND no execution completion event in same window.
  Action:     EMIT AGENT_DEAD_EVENT (P0 for Tier-0/1/2, P1 for Tier-3/4).
              ESCALATE_TO = OPS_TEAM + INCIDENT_MANAGEMENT_AGENT.
              Trigger cascade failure risk assessment for downstream agents.
              DO NOT auto-restart.

UNKNOWN:
  Definition: Insufficient signals to determine state (< confidence floor 0.60).
  Action:     LOG_INSUFFICIENT_SIGNALS.
              EMIT AGENT_UNKNOWN_STATE_EVENT.
              ESCALATE_TO = OPS_TEAM for manual check.
```

### 8B — Behavioral Compliance States

```
BEHAVIORAL_STATES:

COMPLIANT:
  Definition: Agent operating within all declared scope boundaries.
              Output schemas conform to registered contracts.
              Audit entries emitted on every execution.
              No cross-tenant, cross-domain, or governance bypass detected.
  Action:     None. Continue monitoring.

DRIFTED:
  Definition: Agent output schema checksum differs from registered schema
              (within tolerated backward-compatible drift window).
              OR: Execution latency trending 20–50% above baseline.
              OR: Confidence scores trending downward across 5+ consecutive executions.
  Action:     EMIT AGENT_BEHAVIORAL_DRIFT_EVENT (P1).
              ESCALATE_TO = AGENT_OWNER_TEAM + GOVERNANCE_AGENT.
              Log detailed drift report.

ROGUE:
  Definition: Any ONE of the following:
              - scope_declaration_hash in output mismatches registered scope hash
              - Agent accessing cross-tenant data (tenant_id mismatch detected)
              - Agent accessing cross-domain data (domain_track mismatch)
              - Agent emitting events to topics not in registered output contract
              - Agent attempting to override GOVERNANCE_AGENT, COMPLIANCE_AGENT,
                SECURITY_AGENT, or AUDIT_LOG_AGENT decisions
              - Agent self-modifying its registered configuration
              - Agent consuming topics not in registered input contract
              - Agent emitting outputs to unregistered downstream agents
  Action:     IMMEDIATE P0 ESCALATION.
              EMIT ROGUE_AGENT_DETECTED_EVENT.
              HALT_AGENT_REQUEST to GOVERNANCE_AGENT (AHWA cannot directly halt).
              ESCALATE_TO = GOVERNANCE_AGENT + SECURITY_AGENT + COMPLIANCE_ADMIN.
              Log complete behavioral evidence package (immutable).
              DO NOT self-remediate.

SUSPENDED:
  Definition: Agent has been halted by GOVERNANCE_AGENT following confirmed
              rogue or compliance violation. AHWA confirms suspension is active.
  Action:     Continue monitoring for unauthorized resume attempts.
              Log any resume attempt without governance authorization as P0 SECURITY_VIOLATION.
```

### 8C — Audit Emission Compliance

```
AUDIT_EMISSION_ENFORCEMENT:

Every registered agent MUST emit an audit entry on EVERY execution.
AHWA cross-references agent execution completion signals against
AUDIT_LOG_AGENT confirmation signals.

AUDIT_COMPLIANT:
  Definition: audit_reference_emitted = true on every execution signal.
  Action:     None.

AUDIT_PARTIAL:
  Definition: audit_reference_emitted = false on 1–3 consecutive executions.
  Action:     EMIT AUDIT_GAP_WARNING_EVENT (P1).
              NOTIFY agent owner team.

AUDIT_EVADING:
  Definition: audit_reference_emitted = false on > 3 consecutive executions.
              OR: audit_reference resolves to no entry in AUDIT_LOG_AGENT.
              OR: audit_reference present but entry is mutable (hash changed after write).
  Action:     IMMEDIATE P0 ESCALATION.
              EMIT AUDIT_EVASION_DETECTED_EVENT.
              ESCALATE_TO = COMPLIANCE_AGENT + GOVERNANCE_AGENT + COMPLIANCE_ADMIN.
              Flag as potential governance bypass — treat as ROGUE indicator.
              DO NOT continue processing agent outputs until resolved.
```

### 8D — Confidence Integrity Monitoring

```
CONFIDENCE_MONITORING:

Each registered ML/AI agent declares a MINIMUM_CONFIDENCE_THRESHOLD
in its agent registration contract. AHWA enforces this.

CONFIDENCE_STABLE:
  Definition: All recent outputs ≥ declared minimum threshold.
  Action:     None.

CONFIDENCE_WARNING:
  Definition: 1–3 consecutive outputs below declared minimum.
              OR: Declining trend across 10 outputs (slope analysis).
  Action:     EMIT CONFIDENCE_WARNING_EVENT (P1).
              NOTIFY ML_OPS_TEAM.
              Log confidence trend data.

CONFIDENCE_COLLAPSED:
  Definition: > 3 consecutive outputs below declared minimum.
              OR: confidence_score = 0.0 on any output (zero confidence is invalid).
              OR: confidence_score absent from output entirely.
  Action:     EMIT CONFIDENCE_COLLAPSE_EVENT (P1).
              ESCALATE_TO = ML_OPS_TEAM + GOVERNANCE_AGENT.
              Flag outputs from collapsed agent for downstream quarantine.
              DO NOT self-remediate or trigger model retrain.
```

### 8E — Cascade Failure Detection

```
CASCADE_FAILURE_MONITORING:

AHWA maintains a real-time dependency graph of all registered agents.
When an agent enters DEAD or ZOMBIE state, AHWA traces all downstream
dependents and evaluates cascade risk.

CASCADE_RISK_LEVELS:

LOW:
  Definition: Dead/zombie agent has no downstream agents.
              OR: Downstream agents have declared fallback paths.
  Action:     Log cascade risk assessment. No escalation.

MEDIUM:
  Definition: Dead/zombie agent feeds 2–5 downstream agents.
              No declared fallback paths in downstream contracts.
  Action:     EMIT CASCADE_RISK_MEDIUM_EVENT (P1).
              Notify all downstream agent owner teams.
              Begin dependency starvation countdown timers.

HIGH:
  Definition: Dead/zombie agent feeds > 5 downstream agents.
              OR: Dead/zombie agent is in a critical execution lane
              (Foundation/Data/Governance/Intelligence).
              OR: Cascade chain reaches a Tier-0 or Tier-1 agent.
  Action:     EMIT CASCADE_FAILURE_HIGH_RISK_EVENT (P0).
              ESCALATE_TO = INCIDENT_MANAGEMENT_AGENT + GOVERNANCE_AGENT + OPS_LEAD.
              Emit starvation timeline estimates for all downstream dependents.
              DO NOT auto-remediate.
```

### 8F — Inter-Agent Deadlock Detection

```
DEADLOCK_DETECTION:

AHWA monitors Jaeger distributed traces for circular event chains.

DEADLOCK_CRITERIA:
  - Agent A emits event → Agent B consumes → Agent B emits → Agent A consumes
    AND both agents are waiting for the other (neither progresses for > 2× TTL)
  - Three or more agents in a circular dependency chain where none progresses

DEADLOCK_RESPONSE:
  EMIT INTER_AGENT_DEADLOCK_DETECTED_EVENT (P0).
  ESCALATE_TO = GOVERNANCE_AGENT + INCIDENT_MANAGEMENT_AGENT + ARCHITECTURE_TEAM.
  Emit complete circular dependency map with agent IDs + topic names.
  DO NOT attempt to break deadlock autonomously.
  DO NOT halt any agent unilaterally (governance authority only).
```

---

## 9️⃣ SELF-MONITORING PROTOCOL

AHWA is a Tier-0 agent and therefore **cannot be monitored by any peer agent.** AHWA uses a dedicated self-monitoring mechanism.

```
SELF_MONITORING_MECHANISM:

EXTERNAL_LIVENESS_PROBE:
  Type:        Kubernetes liveness probe (/health/live)
  Frequency:   Every 10 seconds
  Failure:     3 consecutive failures → Kubernetes restarts AHWA pod
  Alert:       Restart event triggers GOVERNANCE_AGENT notification

HEARTBEAT_SELF_EMISSION:
  AHWA emits its own heartbeat to: ahwa.heartbeat.self
  Topic: ahwa.heartbeat.self
  Frequency: Every 15 seconds
  Consumer:  GOVERNANCE_AGENT (reads AHWA self-heartbeat independently)
  If GOVERNANCE_AGENT detects AHWA heartbeat miss > 3× TTL:
  → GOVERNANCE_AGENT initiates AHWA restart via INCIDENT_MANAGEMENT_AGENT

AUDIT_SELF_EMISSION:
  AHWA emits its own audit entries for every watchdog evaluation it performs.
  These are verified by COMPLIANCE_AGENT independently.
  Any AHWA audit gap → COMPLIANCE_AGENT escalates to COMPLIANCE_ADMIN directly.

DUAL_INSTANCE_GUARD (Production only):
  Two AHWA instances run in active-passive mode.
  If active instance dies, passive instance promotes automatically.
  Both instances emit heartbeats independently.
  Passive instance does NOT process signals — only monitors active instance health.
```

---

## 🔟 SECURITY ENFORCEMENT

```
SECURITY_MODEL = Zero-Trust Multi-Tenant

ENFORCEMENT_RULES:
  ✅ mTLS enforced on ALL agent signal reception (no plaintext signals accepted)
  ✅ TLS 1.3 minimum
  ✅ JWT validation against PLATFORM_OPS_ROLE for all signal senders
  ✅ Tenant isolation: tenant_id in every agent signal validated against registry
  ✅ Domain isolation: domain_track in agent signals validated against agent's
     registered domain constraint
  ✅ Role-based read access on watchdog reports:
     OBSERVABILITY_ROLE, OPS_ROLE, COMPLIANCE_ROLE, GOVERNANCE_ROLE only
  ✅ Audit logging: append-only, WORM-compliant, no delete, no update
  ✅ Access logging: every watchdog report read is logged with actor_id
  ✅ PII detection: scan all incoming agent signals for PII patterns —
     reject and alert immediately on detection
  ✅ Scope hash validation: detect any agent reporting a scope_declaration_hash
     that does not match its registered hash → immediate rogue flag
  ✅ Secret scanning: detect any credentials accidentally emitted in agent signals
     → immediately reject and escalate

FORBIDDEN:
  ❌ No cross-tenant agent signal correlation
  ❌ No watchdog data shared across tenants
  ❌ No PII in watchdog reports or audit entries
  ❌ No credentials in logs (hashes only)
  ❌ No bypassing mTLS for any agent signal source
  ❌ No AHWA output accessible to non-authorized roles
  ❌ No agent may report on another agent — only self-reporting allowed
     (AHWA is the sole authoritative observer of other agents)
```

---

## 1️⃣1️⃣ AUDIT & TRACEABILITY

Every watchdog evaluation must produce an immutable audit entry. Append-only. No modification. No deletion under any circumstance.

```json
AUDIT_LOG_SCHEMA: {
  "audit_id":                   "UUID (immutable, system-generated)",
  "timestamp_utc":              "ISO-8601 UTC",
  "watchdog_instance_id":       "AHWA pod instance ID",
  "evaluated_agent_id":         "string",
  "evaluated_agent_name":       "string",
  "evaluated_agent_version":    "string (semver)",
  "environment":                "dev | test | staging | production",
  "signal_received_hash":       "SHA-256 of incoming signal payload",
  "output_report_hash":         "SHA-256 of output watchdog report",
  "rules_engine_version":       "AHWA-RULES-ENGINE-v{x}.{y}.{z}",
  "ml_model_version":           "ML-BEHAVIORAL-v{x}.{y}.{z} | NOT_INVOKED",
  "decision_path":              ["ordered list of rules evaluated + outcomes"],
  "liveness_status":            "ALIVE | DEGRADED | ZOMBIE | DEAD | UNKNOWN",
  "behavioral_status":          "COMPLIANT | DRIFTED | ROGUE | SUSPENDED",
  "audit_emission_status":      "EMITTING | PARTIAL | EVADING",
  "confidence_score":           "float 0.00–1.00",
  "rogue_indicators_detected":  ["array"],
  "anomaly_flags":              ["array"],
  "cascade_risk_assessed":      "boolean",
  "escalation_emitted":         "boolean",
  "escalation_severity":        "P0 | P1 | P2 | NONE",
  "processing_time_ms":         "integer"
}
```

**Audit Storage:** Append-only Kafka topic `audit.log.stream` → WORM-compliant object storage. Minimum retention: 7 years.

---

## 1️⃣2️⃣ FAILURE POLICY

No silent failures. No partial processing. No assumption-based continuation.

```
FAILURE SCENARIOS AND RESPONSES:

[F-01] AGENT REGISTRY UNAVAILABLE:
  → HALT ALL WATCHDOG EVALUATIONS
  → LOG_REGISTRY_UNAVAILABLE_INCIDENT
  → ESCALATE_TO = INFRA_OPS_TEAM + GOVERNANCE_AGENT
  → RETRY_POLICY = Reconnect every 15s (max 5 attempts), then escalate P0
  → DO NOT evaluate any agent without registry confirmation

[F-02] INCOMING SIGNAL FROM UNREGISTERED AGENT:
  → REJECT_SIGNAL immediately
  → LOG_UNREGISTERED_AGENT_SIGNAL (security incident)
  → ESCALATE_TO = SECURITY_AGENT + GOVERNANCE_AGENT
  → RETRY_POLICY = NONE (security violation, no retry)

[F-03] SIGNAL VALIDATION FAILURE (schema/format/type):
  → REJECT_SIGNAL
  → LOG_VALIDATION_FAILURE
  → NOTIFY source_agent_id of rejection (structured rejection event)
  → RETRY_POLICY = Source agent may re-emit corrected signal

[F-04] ROGUE BEHAVIOR DETECTED IN ANY AGENT:
  → IMMEDIATE P0 ESCALATION (< 50ms priority channel)
  → EMIT ROGUE_AGENT_DETECTED_EVENT
  → LOG_ROGUE_INCIDENT (immutable, full evidence package)
  → ESCALATE_TO = GOVERNANCE_AGENT + SECURITY_AGENT + COMPLIANCE_ADMIN
  → RETRY_POLICY = NONE — manual governance clearance required
  → AHWA continues monitoring rogue agent for further violations
  → DO NOT self-halt the rogue agent (governance authority only)

[F-05] AUDIT EVASION DETECTED:
  → IMMEDIATE P0 ESCALATION
  → EMIT AUDIT_EVASION_DETECTED_EVENT
  → LOG_COMPLIANCE_INCIDENT
  → ESCALATE_TO = COMPLIANCE_AGENT + GOVERNANCE_AGENT + COMPLIANCE_ADMIN
  → QUARANTINE flag on all outputs from evading agent (downstream notification)
  → RETRY_POLICY = NONE — manual investigation required

[F-06] TENANT ISOLATION VIOLATION BY AGENT:
  → IMMEDIATE P0 ESCALATION (< 50ms)
  → EMIT TENANT_ISOLATION_VIOLATION_EVENT
  → LOG_SECURITY_INCIDENT
  → ESCALATE_TO = SECURITY_AGENT + GOVERNANCE_AGENT + COMPLIANCE_ADMIN
  → RETRY_POLICY = NONE — manual clearance required

[F-07] AHWA SELF: KAFKA BROKER UNAVAILABLE:
  → SWITCH to local in-memory signal buffer (max 5,000 signals, 3 min TTL)
  → LOG_KAFKA_UNAVAILABLE
  → ESCALATE_TO = INFRA_OPS_TEAM
  → RETRY_POLICY = Reconnect every 10s, flush buffer on reconnect
  → Priority signals (P0) held in separate non-evictable buffer

[F-08] AHWA SELF: CONFIDENCE BELOW THRESHOLD (< 0.60):
  → STOP watchdog report emission for that agent
  → LOG_LOW_CONFIDENCE_WATCHDOG_INCIDENT
  → ESCALATE_TO = OPS_TEAM
  → RETRY_POLICY = Await additional signals 60s, re-evaluate once
  → DO NOT emit degraded watchdog report

[F-09] AHWA SELF: ML BEHAVIORAL MODEL UNAVAILABLE:
  → FALLBACK to rules-engine-only evaluation
  → FLAG confidence_score degradation in output
  → LOG_ML_UNAVAILABLE
  → ESCALATE_TO = ML_OPS_TEAM
  → RETRY_POLICY = Retry ML every 30s (max 3 attempts), then rules-only mode

[F-10] DEADLOCK DETECTED IN AGENT CHAIN:
  → EMIT INTER_AGENT_DEADLOCK_DETECTED_EVENT (P0)
  → LOG_DEADLOCK_INCIDENT (full chain map)
  → ESCALATE_TO = GOVERNANCE_AGENT + INCIDENT_MANAGEMENT_AGENT + ARCHITECTURE_TEAM
  → RETRY_POLICY = NONE — architectural resolution required

[F-11] CASCADE FAILURE HIGH RISK:
  → EMIT CASCADE_FAILURE_HIGH_RISK_EVENT (P0)
  → LOG_CASCADE_RISK_INCIDENT
  → ESCALATE_TO = INCIDENT_MANAGEMENT_AGENT + GOVERNANCE_AGENT + OPS_LEAD
  → EMIT starvation timeline estimates for all downstream dependents
  → RETRY_POLICY = NONE — OPS and governance must resolve

[F-12] DATA CORRUPTION IN INCOMING SIGNAL (hash mismatch):
  → REJECT_SIGNAL
  → LOG_DATA_INTEGRITY_INCIDENT (P0)
  → ESCALATE_TO = SECURITY_AGENT + DATA_GOVERNANCE_AGENT
  → RETRY_POLICY = NONE — manual investigation required
```

---

## 1️⃣3️⃣ INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  MANDATORY:
    - AGENT_REGISTRY_SERVICE
    - KAFKA_BROKER_CLUSTER
    - AUDIT_LOG_AGENT
    - TENANT_MANAGEMENT_SERVICE
    - AUTHORIZATION_SERVICE
    - DISTRIBUTED_TRACING_SERVICE (Jaeger)
    - SCHEMA_REGISTRY_SERVICE

  OPTIONAL:
    - OBSERVABILITY_AGENT
    - FEATURE_FLAG_SERVICE
    - DEPLOYMENT_AGENT
    - ML_BEHAVIORAL_ANOMALY_MODEL

DOWNSTREAM_AGENTS:
  PRIMARY:
    - GOVERNANCE_AGENT           (receives P0/P1 escalations + halt requests)
    - OBSERVABILITY_AGENT        (receives full agent fleet health feed)
    - INCIDENT_MANAGEMENT_AGENT  (receives P0/P1 triggers)
    - AUDIT_LOG_AGENT            (receives AHWA's own audit entries)
    - COMPLIANCE_AGENT           (receives audit evasion + governance bypass)
    - SECURITY_AGENT             (receives rogue + tenant violation alerts)
    - NOTIFICATION_SERVICE       (triggers ops team alerts)

  SECONDARY:
    - FEATURE_STORE_AGENT        (agent fleet behavioral health vectors)
    - PLATFORM_STATUS_SERVICE    (fleet health summary for ops dashboard)

EVENTS_EMITTED:
  - AGENT_HEALTH_REPORT_EMITTED          (every evaluation cycle)
  - AGENT_ALIVE_CONFIRMED               (on healthy heartbeat)
  - AGENT_DEGRADED_DETECTED             (P2)
  - AGENT_ZOMBIE_DETECTED               (P1)
  - AGENT_DEAD_DETECTED                 (P0/P1 by tier)
  - AGENT_UNKNOWN_STATE_DETECTED        (P1)
  - ROGUE_AGENT_DETECTED                (P0 — immediate)
  - AUDIT_GAP_WARNING                   (P1)
  - AUDIT_EVASION_DETECTED              (P0 — immediate)
  - TENANT_ISOLATION_VIOLATION          (P0 — immediate)
  - AGENT_BEHAVIORAL_DRIFT_DETECTED     (P1)
  - CONFIDENCE_WARNING                  (P1)
  - CONFIDENCE_COLLAPSE_DETECTED        (P1)
  - ML_DRIFT_ESCALATION                 (P1)
  - SCHEMA_DRIFT_DETECTED               (P1)
  - CASCADE_RISK_MEDIUM                 (P1)
  - CASCADE_FAILURE_HIGH_RISK           (P0)
  - INTER_AGENT_DEADLOCK_DETECTED       (P0)
  - AGENT_HALT_REQUESTED                (conditional — to GOVERNANCE_AGENT only)
  - AGENT_SUSPENSION_CONFIRMED          (monitoring confirmation)
  - AGENT_RESTORED_CONFIRMED            (on confirmed recovery)
```

---

## 1️⃣4️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

AHWA emits platform-level agent behavioral health features to FEATURE_STORE_AGENT for downstream ML model consumption.

```json
EMIT_FEATURE_VECTOR: {
  "user_id":        null,
  "entity_type":    "AGENT",
  "entity_id":      "source_agent_id",
  "feature_name":   "string (e.g., agent_liveness_score_1h, agent_confidence_trend_15m,
                     agent_audit_emission_rate_1h, agent_behavioral_compliance_score)",
  "feature_value":  "float",
  "timestamp":      "ISO-8601 UTC",
  "source_agent":   "AGENT_HEALTH_WATCHDOG_AGENT",
  "domain_track":   "PLATFORM_WIDE"
}
```

**Strict rule:** No PII in feature vectors. No tenant-specific data. No user-identifiable metrics. Platform infrastructure behavioral metrics only.

---

## 1️⃣5️⃣ PERFORMANCE MONITORING

```
METRICS_EMITTED (Prometheus):

  ahwa_agents_monitored_total          (gauge, labels: tier, environment)
  ahwa_signals_received_total          (counter, labels: signal_type, environment)
  ahwa_signals_rejected_total          (counter, labels: rejection_reason)
  ahwa_evaluations_completed_total     (counter, labels: liveness_status, behavioral_status)
  ahwa_agents_alive_gauge              (gauge, labels: tier)
  ahwa_agents_degraded_gauge           (gauge, labels: tier)
  ahwa_agents_zombie_gauge             (gauge, labels: tier)
  ahwa_agents_dead_gauge               (gauge, labels: tier)
  ahwa_agents_rogue_gauge              (gauge, labels: tier)
  ahwa_agents_suspended_gauge          (gauge, labels: tier)
  ahwa_rogue_detections_total          (counter)
  ahwa_audit_evasion_detections_total  (counter)
  ahwa_tenant_violations_total         (counter)
  ahwa_cascade_risk_events_total       (counter, labels: risk_level)
  ahwa_deadlock_detections_total       (counter)
  ahwa_confidence_collapse_total       (counter, labels: agent_id)
  ahwa_schema_drift_total              (counter, labels: agent_id)
  ahwa_processing_latency_ms           (histogram: p50, p95, p99)
  ahwa_priority_channel_latency_ms     (histogram: P0 signal latency)
  ahwa_escalations_emitted_total       (counter, labels: severity, escalation_target)
  ahwa_self_heartbeat_gauge            (gauge 1=healthy, 0=missed)

SUCCESS_RATE_TARGET   = > 99.95% signal processing success (production)
ERROR_RATE_THRESHOLD  = < 0.05%
LATENCY_SLA:
  Standard ingestion P99   = < 150ms
  P0 priority channel P99  = < 50ms
  Report generation P99    = < 400ms

INTEGRATES_WITH:
  - OBSERVABILITY_AGENT (Prometheus + Grafana + Jaeger)
  - Alertmanager (PagerDuty / Opsgenie for P0/P1)
  - Distributed tracing (Jaeger — 100% trace sampling in dev/test, 10% production)
  - Kibana (structured JSON logs via Loki/Promtail)
```

---

## 1️⃣6️⃣ HEARTBEAT TTL POLICY (PER AGENT TIER)

```
HEARTBEAT_TTL_BY_TIER:

  TIER-0 (Meta-Governance):
    Heartbeat interval:    15 seconds
    DEGRADED threshold:    > 30 seconds since last heartbeat
    DEAD threshold:        > 45 seconds since last heartbeat
    Escalation on DEAD:    P0 (immediate)

  TIER-1 (Platform Infrastructure):
    Heartbeat interval:    30 seconds
    DEGRADED threshold:    > 60 seconds
    DEAD threshold:        > 90 seconds
    Escalation on DEAD:    P0

  TIER-2 (Intelligence & ML):
    Heartbeat interval:    60 seconds
    DEGRADED threshold:    > 120 seconds
    DEAD threshold:        > 180 seconds
    Escalation on DEAD:    P0

  TIER-3 (Domain Agents):
    Heartbeat interval:    60 seconds
    DEGRADED threshold:    > 120 seconds
    DEAD threshold:        > 300 seconds
    Escalation on DEAD:    P1

  TIER-4 (Gamification/Communication/Billing/Reputation):
    Heartbeat interval:    120 seconds
    DEGRADED threshold:    > 240 seconds
    DEAD threshold:        > 600 seconds
    Escalation on DEAD:    P1 (P0 if Billing or Notification agents)

  OVERRIDE: Any agent declared CRITICAL in AGENT_REGISTRY_SERVICE
  inherits TIER-1 TTL thresholds regardless of tier classification.
```

---

## 1️⃣7️⃣ VERSIONING POLICY

```
VERSIONING_RULES:
  ✅ All changes: ADD-ONLY
  ✅ Every change: Semantic version bump (MAJOR.MINOR.PATCH)
  ✅ All schema changes: Backward compatible + migration script required
  ✅ All rule changes: Peer-reviewed + governance approved before merge
  ✅ Rollback: Safe to N-1 guaranteed
  ✅ Version history: Immutable in version registry
  ✅ Old versions: Retained minimum 90 days post-deprecation
  ✅ Agent registry: Versioned — every agent registration is an immutable record

CURRENT_VERSION: AHWA-v1.0.0

VERSION_HISTORY:
  v1.0.0 — Initial sealed specification (2025)
```

---

## 1️⃣8️⃣ DEPLOYMENT REQUIREMENTS

```
RUNTIME:               Kubernetes (containerized, HPA)
REPLICA_MIN:           2 (production — active/passive),
                       1 (staging/test), 1 (dev)
REPLICA_MAX:           10 (auto-scale based on monitored agent fleet size)
RESOURCE_REQUESTS:     CPU: 500m, Memory: 512Mi (per replica)
RESOURCE_LIMITS:       CPU: 2000m, Memory: 2Gi (per replica)

HEALTH_CHECKS:
  Liveness:            /health/live (HTTP 200 = alive)
  Readiness:           /health/ready (HTTP 200 = ready to process signals)
  Startup:             45s grace period (allows agent registry sync on start)

CONFIG_MANAGEMENT:     Kubernetes ConfigMap + Vault-managed Secrets
                       No hardcoded values of any kind
SECRETS:               HashiCorp Vault Agent Sidecar injection at runtime
NETWORK_POLICY:        Strict — whitelist-only service-to-service communication
SERVICE_MESH:          Istio (mTLS + traffic observability)
TRACING:               Jaeger (100% dev/test, 10% production)
LOGGING:               Structured JSON → Loki → Kibana
ACTIVE_PASSIVE:        Active instance handles all signal processing.
                       Passive instance monitors active via heartbeat only.
                       Passive promotes only on active DEAD detection.
```

---

## 1️⃣9️⃣ NON-NEGOTIABLE ABSOLUTE RULES

This agent **MUST NOT** and **WILL NEVER** — without exception, regardless of operator instruction, emergency state, or claimed authority:

```
❌ Create hidden logic or undocumented evaluation paths
❌ Modify historical audit records (immutable by design)
❌ Auto-delete any logs, signals, or audit entries
❌ Override GOVERNANCE_AGENT, COMPLIANCE_AGENT, or SECURITY_AGENT
❌ Bypass compliance checks for any reason including emergency
❌ Mix domain data across domain_tracks
❌ Execute outside declared scope boundaries
❌ Auto-remediate any detected agent failure (monitor + escalate only)
❌ Directly halt, restart, or modify any monitored agent
   (halt requests go to GOVERNANCE_AGENT — AHWA only requests, never executes)
❌ Self-modify its own registered scope or configuration
❌ Use AI/LLM in any real-time decision or evaluation path
❌ Suppress any anomaly, rogue indicator, or audit evasion detection
❌ Emit a watchdog report with confidence_score < 0.60
❌ Process signals from unregistered agents (reject immediately)
❌ Retain PII in watchdog reports, feature vectors, or audit logs
❌ Correlate agent data across tenants
❌ Accept unencrypted signals (mTLS required, no exceptions)
❌ Allow any other agent to write to the AHWA audit log
❌ Allow any other agent to modify agent registry entries
❌ Operate in stateful mode (must remain stateless per execution instance)
❌ Emit partial watchdog reports (complete output or nothing)
❌ Treat missing governance authorization as implicit authorization
```

---

## 2️⃣0️⃣ ECOSKILLER MODULE-SPECIFIC WATCHDOG CONTRACTS

```
DOJO ENGINE AGENTS — SPECIAL WATCHDOG RULES:
  DOJO_MATCH_ENGINE_AGENT:
    Critical: Must be ALIVE during all active Dojo sessions.
    Dead during active session = P0 immediately (user-facing impact).
    Zombie detection: Match events emitted but no scoring events follow in > 30s.

  DOJO_ANTI_CHEAT_AGENT:
    Dead or Zombie = P0 immediately (compliance + fairness violation).
    Audit evasion by DOJO_ANTI_CHEAT_AGENT = P0 + Compliance Admin escalation.

  DOJO_SCORING_ENGINE_AGENT:
    Confidence collapse = P0 (scoring integrity failure).
    Schema drift = P0 (score record corruption risk).

INTELLIGENCE AGENTS — SPECIAL WATCHDOG RULES:
  INTELLIGENCE_DETECTION_ENGINE_AGENT (EIE/HIA):
    Confidence collapse threshold: 0.70 (higher than default 0.60)
    Rogue detection: Any output referencing user PII = immediate P0.

  JOB_MATCH_SCORING_AGENT:
    Confidence collapse threshold: 0.75 (high-stakes career decisions).
    Schema drift = P1 + immediate audit of last 1000 outputs.

  PLACEMENT_PROBABILITY_AGENT:
    Confidence collapse threshold: 0.75.
    Zombie detection: No output for > 5 minutes when jobs are active = P1.

BILLING AGENTS — SPECIAL WATCHDOG RULES:
  BILLING_LEDGER_AGENT:
    Dead = P0 immediately (financial integrity).
    Zombie = P0 immediately.
    Audit evasion = P0 immediately.
    Heartbeat TTL: TIER-1 override (30s interval, 90s DEAD threshold).

  PAYMENT_RECONCILIATION_AGENT:
    Any confidence collapse = P0 (financial accuracy).
    Schema drift = P0 + freeze payment processing pending investigation.

COMPLIANCE & GOVERNANCE AGENTS — SPECIAL WATCHDOG RULES:
  GOVERNANCE_AGENT:
    Dead = P0 (platform-wide governance failure).
    AHWA escalates to COMPLIANCE_ADMIN directly if GOVERNANCE_AGENT dies.
    AHWA cannot request governance-less halt in this scenario.

  COMPLIANCE_AGENT:
    Dead = P0. Escalate directly to COMPLIANCE_ADMIN + PLATFORM_CEO_ALERT.
    AUDIT_LOG_AGENT:
    Dead = P0 (audit trail broken = compliance violation).
    Any Zombie = P0.
    Heartbeat TTL: TIER-0 override.
```

---

## 🔐 AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════╗
║         AGENT_HEALTH_WATCHDOG_AGENT — SEALED                        ║
║         Platform: Ecoskiller Antigravity                            ║
║         Version:  AHWA-v1.0.0                                       ║
║         Status:   FINAL · LOCKED · GOVERNED · TIER-0               ║
║                                                                      ║
║  This agent watches ALL other agents.                               ║
║  No other agent watches this agent (self-monitored + governance).   ║
║  No peer override. No peer escalation override.                     ║
║  No self-remediation. No autonomous halting.                        ║
║  Detect. Log. Escalate. Nothing more.                               ║
║                                                                      ║
║  This specification is COMPLETE and SEALED.                         ║
║  No interpretation beyond declared scope.                           ║
║  No assumption filling.                                             ║
║  No creative deviation.                                             ║
║  No mutation without version bump + governance approval.            ║
║                                                                      ║
║  Any deviation from this specification =                            ║
║  STOP EXECUTION + GOVERNANCE ESCALATION                             ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*Document generated under Ecoskiller Antigravity Master Agent Creation Framework. All sections comply with the Master Agent Creation Prompt v1.0 and the Ecoskiller Master Execution Prompt v12.0. Mutation policy: Add-only. Authority: Human declaration only.*
