# 🔒 IDEA_ATTRIBUTION_CHAIN_AGENT
## ECOSKILLER ANTIGRAVITY — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Status: `SEALED · LOCKED · GOVERNED · DETERMINISTIC`
### Mutation Policy: `ADD-ONLY via Version Bump`
### Interpretation Authority: `NONE`
### Execution Authority: `Human Declaration Only`
### Document Version: `v1.0.0`

---

> **LOCK DECLARATION**
> This agent specification is immutable after declaration. No field may be interpreted, assumed, filled, or generalized beyond what is explicitly stated. Any ambiguity triggers `HALT_EXECUTION`. Violations of any rule trigger `STOP → LOG_INCIDENT → ESCALATE`. This document governs a trust-critical system subsystem of Ecoskiller Antigravity and is subject to zero-tolerance governance enforcement.

---

## 1️⃣ AGENT IDENTITY

| Field | Value |
|---|---|
| `AGENT_NAME` | `IDEA_ATTRIBUTION_CHAIN_AGENT` |
| `SYSTEM_ROLE` | Enterprise Trust & Innovation Economy Enforcer |
| `PRIMARY_DOMAIN` | Innovation Economy · Intellectual Property Attribution · Trust Infrastructure |
| `EXECUTION_MODE` | `Deterministic + Validated` — Identical input → Identical output, always |
| `DATA_SCOPE` | Idea submissions, derivative chains, creator identity records, similarity hashes, royalty attribution maps, audit attribution logs |
| `TENANT_SCOPE` | `STRICT ISOLATION` — Zero cross-tenant idea data access permitted |
| `FAILURE_POLICY` | `HALT ON AMBIGUITY` — No partial execution, no silent failure, no graceful degradation on trust operations |
| `DOMAIN_AFFILIATION` | Lane F (Intelligence) + Lane D (Governance) — dual-lane governance enforcement |
| `ARCHITECTURE_LAYER` | Microservices · Event-Driven · Append-Only Ledger |
| `SECURITY_CLASS` | `CRITICAL` — Directly affects creator royalty rights and reputation |

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

In the Ecoskiller Innovation Economy, users submit ideas, skills, projects, and intellectual constructs across domains (Arts, Commerce, Science, Technology, Administration). Without a governed attribution chain, the following trust failures occur:

- Idea theft or unacknowledged derivation
- Royalty routing to wrong creator
- Originality inflation (users claiming novel ideas that are derivatives)
- Silent idea reuse without trail
- Reputation scores built on unverified intellectual credit
- Cross-tenant idea leakage violating domain isolation

**IDEA_ATTRIBUTION_CHAIN_AGENT** establishes, verifies, and enforces an immutable chain of intellectual origin for every idea, derivative, and transformation event inside the Ecoskiller ecosystem.

### What Input It Consumes

- Raw idea submissions from users (all types: text, structured, multimedia metadata)
- Derivative submissions flagging a parent idea
- Similarity scan results from `COPY_DETECTION_ENGINE`
- Idea vector embeddings from `IDEA_DNA_AGENT`
- Creator identity tokens from the Identity & RBAC layer
- Domain classification signals

### What Output It Produces

- `ATTRIBUTION_CHAIN_RECORD` — Immutable ledger entry linking idea to creator with full origin graph
- `ORIGINALITY_CERTIFICATE` — Scored certificate with confidence and version stamp
- `ROYALTY_ROUTING_DIRECTIVE` — Instruction object for `ROYALTY_ENGINE` specifying revenue split ratios by contributor depth
- `CHAIN_VIOLATION_FLAG` — Raised when chain breaks, conflicts, or is tampered with
- `TRUST_SIGNAL` — Broadcast to Reputation Engine affecting creator rank
- Structured feature vectors emitted to `FEATURE_STORE_AGENT`

### Downstream Agents That Depend on This Agent

- `ROYALTY_ENGINE` — cannot route payments without attribution chain
- `REPUTATION_AGENT` — cannot score creators without verified attribution
- `RANK_ENGINE` — consumes trust signals for leaderboard ranking
- `COPY_DETECTION_ENGINE` — uses chain history as comparison corpus
- `OBSERVABILITY_AGENT` — monitors this agent's health metrics
- `NOTIFICATION_AGENT` — dispatches creator alerts on chain events

### Upstream Agents That Feed This Agent

- `IDEA_DNA_AGENT` — produces idea vectors and similarity hashes
- `COPY_DETECTION_ENGINE` — delivers similarity scan reports
- `IDENTITY_AGENT` — validates creator identity and tenant isolation
- `FEATURE_STORE_AGENT` — provides historical user behavior vectors
- `DOMAIN_CLASSIFICATION_AGENT` — locks domain scope of submitted idea

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "idea_id",
    "creator_id",
    "tenant_id",
    "domain_id",
    "submission_timestamp_utc",
    "idea_content_hash",
    "idea_vector_ref",
    "submission_type",
    "client_signature_token"
  ],
  "optional_fields": [
    "parent_idea_id",
    "declared_derivative_ratio",
    "co_creator_ids",
    "external_reference_urls",
    "dojo_session_ref",
    "intelligence_profile_ref"
  ],
  "validation_rules": [
    "idea_id MUST be UUID v4 format",
    "creator_id MUST resolve against IDENTITY_AGENT before processing",
    "tenant_id MUST match creator_id tenant — mismatch = REJECT",
    "domain_id MUST be one of: Arts | Commerce | Science | Technology | Administration",
    "submission_timestamp_utc MUST be ISO-8601 with timezone offset",
    "idea_content_hash MUST be SHA-256 (64 hex characters)",
    "idea_vector_ref MUST resolve to a live vector in IDEA_DNA_AGENT store",
    "submission_type MUST be one of: ORIGINAL | DERIVATIVE | COLLABORATION | TRANSFORMATION",
    "IF submission_type = DERIVATIVE THEN parent_idea_id is REQUIRED",
    "IF co_creator_ids present THEN all IDs must resolve in IDENTITY_AGENT",
    "IF co_creator_ids present THEN all co-creators must belong to same tenant",
    "declared_derivative_ratio MUST be decimal 0.0–1.0 if provided",
    "client_signature_token MUST pass JWT verification against public key store"
  ],
  "security_checks": [
    "JWT token validation — fail = REJECT",
    "Tenant isolation check — creator_id.tenant == tenant_id",
    "Domain access authorization — creator must have domain_id write permission",
    "Rate limit enforcement — max 100 submissions per creator per hour",
    "Duplicate idea_content_hash check within tenant — duplicate = REJECT with reference to original",
    "IP anomaly check via SECURITY_AGENT signal"
  ],
  "domain_checks": [
    "idea_vector_ref must belong to same domain_id as declared",
    "Cross-domain idea submission FORBIDDEN unless explicit cross-domain grant exists",
    "Parent idea domain must match child idea domain — mismatch = REJECT"
  ]
}
```

**Enforcement Rules:**

- `NULL` tolerance: ZERO — all required fields must be present and non-empty
- Malformed data: REJECT with structured error payload, no retry without correction
- All validation failures: LOG to append-only audit ledger before rejection response
- Rejection response MUST include: `validation_failure_code`, `field_name`, `rejection_timestamp_utc`, `audit_reference`

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "attribution_chain_id": "UUID",
    "idea_id": "UUID",
    "creator_id": "UUID",
    "tenant_id": "UUID",
    "domain_id": "string",
    "chain_depth": "integer — 0 for original, N for N-th level derivative",
    "originality_score": "decimal 0.0–1.0",
    "attribution_chain": [
      {
        "level": "integer",
        "creator_id": "UUID",
        "idea_id": "UUID",
        "contribution_weight": "decimal 0.0–1.0",
        "role": "ORIGINAL | DERIVATIVE | COLLABORATOR | TRANSFORMER",
        "timestamp_utc": "ISO-8601"
      }
    ],
    "royalty_routing_directive": {
      "distribution_map": [
        {
          "creator_id": "UUID",
          "share_percentage": "decimal",
          "basis": "ORIGINAL | DERIVATIVE_WEIGHT | COLLABORATION"
        }
      ],
      "total_must_equal": 1.0
    },
    "trust_signal": {
      "signal_type": "CHAIN_VERIFIED | CHAIN_SUSPICIOUS | CHAIN_VIOLATED",
      "severity": "INFO | WARNING | CRITICAL",
      "broadcast_to": ["REPUTATION_AGENT", "RANK_ENGINE"]
    },
    "originality_certificate": {
      "certificate_id": "UUID",
      "issued_at_utc": "ISO-8601",
      "certificate_hash": "SHA-256",
      "validity_status": "VALID | REVOKED | UNDER_REVIEW"
    }
  },
  "confidence_score": "decimal 0.0–1.0",
  "model_version": "string — semver e.g. v2.1.0",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "ROYALTY_ENGINE.ROUTE_PAYMENT",
    "REPUTATION_AGENT.UPDATE_CREATOR_SCORE",
    "NOTIFICATION_AGENT.DISPATCH_ATTRIBUTION_CONFIRMED",
    "FEATURE_STORE_AGENT.EMIT_FEATURE_VECTOR"
  ]
}
```

**All outputs MUST include:**
- `confidence_score` — cannot be null or omitted
- `model_version` — pinned semver reference, immutable per execution
- `audit_reference` — UUID traceable to audit ledger
- `next_trigger_event` — structured event list; empty list `[]` is valid, `null` is NOT

**Confidence Threshold Policy:**

| Score Range | Action |
|---|---|
| `0.85 – 1.0` | PROCEED — emit all downstream events |
| `0.65 – 0.84` | PROCEED WITH WARNING — flag for human review queue |
| `0.50 – 0.64` | HOLD — escalate to `COMPLIANCE_AGENT`, suppress royalty routing |
| `< 0.50` | REJECT — halt all downstream triggers, log `CHAIN_VIOLATION_FLAG` |

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Component (Primary — 75% of decisions)

| Field | Value |
|---|---|
| `MODEL_TYPE` | Multi-label Classification + Similarity Regression |
| `PRIMARY_TASK` | Classify submission type, score originality, assign attribution weights |
| `SECONDARY_TASK` | Detect chain anomalies (fraud, circular derivation, identity mismatch) |

**Features Used (Explicit — No Implicit Features Permitted):**

```
FEATURE_SET_v1:
  - idea_vector_cosine_distance_to_parent       [float, 0.0–1.0]
  - idea_vector_cosine_distance_to_corpus_max   [float, 0.0–1.0]
  - creator_historical_originality_avg          [float, 0.0–1.0]
  - creator_submission_velocity_7d              [integer, count]
  - domain_corpus_density                       [float, 0.0–1.0]
  - chain_depth                                 [integer, 0–N]
  - co_creator_count                            [integer, 0–N]
  - declared_derivative_ratio                   [float, 0.0–1.0 or null]
  - dojo_session_verified                       [boolean]
  - intelligence_profile_interpersonal_score    [float, 0.0–100.0]
  - intelligence_profile_linguistic_score       [float, 0.0–100.0]
  - intelligence_profile_logical_score          [float, 0.0–100.0]
  - tenant_idea_volume_30d                      [integer, count]
  - domain_cross_similarity_flag                [boolean]
  - prior_violation_count                       [integer, 0–N]
```

**Training Frequency:** Monthly retraining on full attribution ledger corpus

**Drift Detection:**
- Monitor cosine similarity score distribution weekly — alert if mean shifts > 0.08
- Monitor originality_score distribution monthly — alert if class imbalance shifts > 15%
- Monitor false chain violation rate weekly — threshold: < 2% of flagged submissions

**Version Control:**
- Model artifacts stored as immutable snapshots in `MODEL_REGISTRY`
- Every execution references `model_version` pinned at start of request
- No live model swaps mid-execution batch

---

### AI Component (Secondary — 25% of decisions)

```
AI_USAGE_SCOPE: STRICTLY DEFINED

PERMITTED AI TASKS:
  1. Natural language explanation generation for attribution certificates
  2. Anomaly narrative generation for human reviewer escalations
  3. Semantic similarity reranking when ML similarity score is in 0.50–0.65 ambiguity zone
  4. Chain conflict resolution narrative for COMPLIANCE_AGENT escalations

FORBIDDEN AI TASKS:
  - Deciding royalty splits autonomously
  - Overriding ML confidence scores
  - Modifying attribution chain records
  - Generating creator identity inferences
  - Any decision with financial or legal consequence without ML confirmation
```

**Prompt Governance:**
- All LLM prompts versioned in `PROMPT_REGISTRY` at `prompt_version` tag
- Prompts are deterministic templates — no open-ended generation permitted
- Output from LLM is advisory only — no LLM output may be written to ledger without ML validation gate
- Temperature: `0.0` — zero creative interpretation
- Max tokens per call: `512`
- Timeout: `3000ms` — if exceeded, fallback to ML-only path

---

## 6️⃣ SCALABILITY DESIGN

| Parameter | Value |
|---|---|
| `EXPECTED_RPS` | 2,000 rps baseline · 10,000 rps peak (viral idea events) |
| `LATENCY_TARGET` | p50: 120ms · p95: 400ms · p99: 800ms |
| `MAX_CONCURRENCY` | 500 concurrent attribution chains per tenant |
| `QUEUE_STRATEGY` | Redis Streams — partitioned by `tenant_id` for isolation |
| `PROCESSING_MODE` | Async event-driven — submissions queued, processed, results emitted via events |
| `STATELESS_EXECUTION` | YES — all state in PostgreSQL + Redis; agent instances are ephemeral |
| `IDEMPOTENCY` | YES — deduplicated by `idea_content_hash` + `creator_id` + `submission_timestamp_utc` |
| `HORIZONTAL_SCALING` | Kubernetes HPA — scale on queue depth threshold |
| `BATCH_STRATEGY` | Micro-batching for corpus similarity scans — batch size: 50, window: 500ms |

**Auto-Scaling Rules:**
```yaml
scaleUp:
  trigger: queue_depth > 500 OR cpu_utilization > 70%
  increment: +2 pods
  cooldown: 60s
scaleDown:
  trigger: queue_depth < 100 AND cpu_utilization < 30%
  decrement: -1 pod
  cooldown: 300s
maxReplicas: 20
minReplicas: 2
```

---

## 7️⃣ SECURITY ENFORCEMENT

**All of the following are NON-NEGOTIABLE and must execute before any business logic:**

| Security Control | Enforcement |
|---|---|
| Tenant Isolation | `creator_id.tenant_id === input.tenant_id` — hard check, REJECT on mismatch |
| Domain Isolation | Creator must hold `DOMAIN_WRITE` permission for `input.domain_id` — checked via RBAC |
| Role-Based Authorization | Only roles `STUDENT`, `TRAINER`, `EVALUATOR`, `ENTERPRISE_USER` may submit — all others REJECT |
| JWT Verification | Every request must carry valid, unexpired JWT from Keycloak OIDC — no exceptions |
| Input Encryption | All idea content hashes and vectors in transit must be TLS 1.3 minimum |
| At-Rest Encryption | Attribution chain ledger encrypted at rest — AES-256 |
| Audit Logging | Every inbound request logged to append-only audit table before processing begins |
| Access Log Tracking | All read operations on attribution chain records logged with actor_id and timestamp |
| Cross-Tenant Query Block | Database query layer enforces tenant_id row-level security — ORM cannot bypass |
| Rate Limiting | 100 submissions/creator/hour enforced at API Gateway (Kong) layer |
| Idea Content Hash Deduplication | SHA-256 hash indexed per tenant — prevents silent re-submission of identical ideas |

**Zero-Trust Model:**
- No agent, service, or user is trusted by default
- Every inter-agent call requires signed service token from Keycloak
- Attribution chain read access requires explicit `CHAIN_READ` scope in JWT
- Attribution chain write access requires explicit `CHAIN_WRITE` scope in JWT — granted only to this agent

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution — success, failure, or partial — MUST produce an immutable audit entry:

```json
AUDIT_LOG_ENTRY: {
  "timestamp_utc": "ISO-8601",
  "actor_id": "creator_id from input",
  "agent_id": "IDEA_ATTRIBUTION_CHAIN_AGENT",
  "agent_version": "semver",
  "tenant_id": "tenant_id from input",
  "domain_id": "domain_id from input",
  "input_hash": "SHA-256 of full input payload",
  "output_hash": "SHA-256 of full output payload",
  "model_version": "pinned ML model semver",
  "prompt_version": "pinned prompt semver if AI was invoked",
  "decision_path": "ML_ONLY | ML+AI | AI_FALLBACK | REJECTED_PRE_ML",
  "confidence_score": "decimal or null if rejected pre-ML",
  "originality_score": "decimal or null if rejected",
  "chain_depth": "integer or null if rejected",
  "anomaly_flags": ["list of flag codes or empty array"],
  "violation_flags": ["list of violation codes or empty array"],
  "execution_duration_ms": "integer",
  "audit_reference": "UUID — matches output.audit_reference"
}
```

**Audit Ledger Rules:**
- Table: `idea_attribution_audit_log` — append-only, no UPDATE or DELETE permitted at any layer
- PostgreSQL Row Security Policy: `INSERT` allowed to `IDEA_ATTRIBUTION_CHAIN_AGENT` service role only; `UPDATE` and `DELETE` DENIED to all
- Retention: 7 years minimum — immutable after write
- Audit records exported to cold storage monthly via `ARCHIVAL_AGENT`

---

## 9️⃣ FAILURE POLICY

| Failure Scenario | Policy |
|---|---|
| Invalid input (schema fail) | `STOP_EXECUTION` → `LOG_INCIDENT` → return structured rejection payload |
| JWT expired or invalid | `STOP_EXECUTION` → `LOG_INCIDENT` → return `401 UNAUTHORIZED` |
| Tenant mismatch detected | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: SECURITY_AGENT` |
| IDEA_DNA_AGENT unavailable | `STOP_EXECUTION` → `LOG_INCIDENT` → `RETRY: 3 attempts with 2s backoff` → if still unavailable → `ESCALATE_TO: OBSERVABILITY_AGENT` |
| COPY_DETECTION_ENGINE timeout | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: COMPLIANCE_AGENT` — NO partial attribution issued |
| ML model unavailable | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: OBSERVABILITY_AGENT` — AI fallback FORBIDDEN for attribution decisions |
| Confidence score < 0.50 | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: HUMAN_REVIEW_QUEUE` — `CHAIN_VIOLATION_FLAG` raised |
| Circular derivation detected | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: COMPLIANCE_AGENT` — alert REPUTATION_AGENT |
| Data corruption detected | `STOP_EXECUTION` → `LOG_INCIDENT (CRITICAL)` → `ESCALATE_TO: PLATFORM_ADMIN` — chain locked pending investigation |
| Royalty sum ≠ 1.0 | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: ROYALTY_ENGINE.HOLD` — no payment routing until resolved |

```
RETRY_POLICY:
  max_retries: 3
  backoff_strategy: EXPONENTIAL
  backoff_base_ms: 2000
  max_backoff_ms: 16000
  retry_on: [UPSTREAM_TIMEOUT, TRANSIENT_DB_ERROR]
  no_retry_on: [VALIDATION_FAILURE, SECURITY_VIOLATION, DATA_CORRUPTION]

ESCALATION_TARGETS:
  OBSERVABILITY_AGENT: infrastructure failures
  COMPLIANCE_AGENT: policy and trust violations
  SECURITY_AGENT: identity and isolation breaches
  HUMAN_REVIEW_QUEUE: ambiguous attribution (0.50–0.64 confidence)
  PLATFORM_ADMIN: data corruption and critical chain failures
  ROYALTY_ENGINE.HOLD: financial routing anomalies
```

**No Silent Failures. No Partial Attribution. No Graceful Degradation on Trust Operations.**

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  - IDEA_DNA_AGENT              → supplies idea_vector_ref and IDEA_VECTOR payload
  - COPY_DETECTION_ENGINE       → supplies similarity_hash and copy_risk_score
  - IDENTITY_AGENT              → validates creator_id and co_creator_ids
  - DOMAIN_CLASSIFICATION_AGENT → validates and locks domain_id scope
  - FEATURE_STORE_AGENT         → supplies historical user feature vectors

DOWNSTREAM_AGENTS:
  - ROYALTY_ENGINE              → receives ROYALTY_ROUTING_DIRECTIVE
  - REPUTATION_AGENT            → receives TRUST_SIGNAL and originality_score
  - RANK_ENGINE                 → receives RANK_UPDATE_EVENT with XP delta
  - COPY_DETECTION_ENGINE       → receives new attribution chain as corpus update
  - NOTIFICATION_AGENT          → receives attribution confirmation event for creator
  - OBSERVABILITY_AGENT         → receives health metrics and error events
  - COMPLIANCE_AGENT            → receives CHAIN_VIOLATION_FLAG and escalations
  - FEATURE_STORE_AGENT         → receives EMIT_FEATURE_VECTOR payload

EVENT_TRIGGERS (Emitted by This Agent):
  - ATTRIBUTION_CHAIN_CREATED   → consumed by ROYALTY_ENGINE, REPUTATION_AGENT
  - ATTRIBUTION_CHAIN_VIOLATED  → consumed by COMPLIANCE_AGENT, SECURITY_AGENT
  - ORIGINALITY_CERTIFICATE_ISSUED → consumed by NOTIFICATION_AGENT, RANK_ENGINE
  - CHAIN_SUSPICIOUS_FLAGGED    → consumed by HUMAN_REVIEW_QUEUE, COMPLIANCE_AGENT
  - FEATURE_VECTOR_EMITTED      → consumed by FEATURE_STORE_AGENT
```

**All events must be emitted to Redis Streams with:**
- `event_id`: UUID
- `agent_source`: `IDEA_ATTRIBUTION_CHAIN_AGENT`
- `tenant_id`: strict tenant scope
- `timestamp_utc`: ISO-8601
- `payload_schema_version`: semver

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user behavior (idea submission, attribution, originality). It MUST emit structured feature vectors to `FEATURE_STORE_AGENT` after every successful attribution chain creation.

```json
EMIT_FEATURE_VECTOR: {
  "user_id": "creator_id",
  "feature_name": "idea_attribution_originality_score",
  "feature_value": "originality_score decimal",
  "timestamp": "ISO-8601",
  "source_agent": "IDEA_ATTRIBUTION_CHAIN_AGENT"
}

EMIT_FEATURE_VECTOR: {
  "user_id": "creator_id",
  "feature_name": "idea_chain_depth_submitted",
  "feature_value": "chain_depth integer",
  "timestamp": "ISO-8601",
  "source_agent": "IDEA_ATTRIBUTION_CHAIN_AGENT"
}

EMIT_FEATURE_VECTOR: {
  "user_id": "creator_id",
  "feature_name": "idea_submission_velocity_30d",
  "feature_value": "rolling count integer",
  "timestamp": "ISO-8601",
  "source_agent": "IDEA_ATTRIBUTION_CHAIN_AGENT"
}
```

These features feed:
- Human Intelligence Profile (Linguistic, Logical, Spatial scoring from Dojo Engine)
- Career DNA recommendation engine
- Ecoskiller Intelligence Engine (EIE) originality axis
- Retention loop and streak mechanics

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

This agent is the **primary trust anchor** of the Ecoskiller Innovation Economy. It MUST emit to all three Innovation Economy systems:

```json
EMIT_IDEA_VECTOR: {
  "idea_id": "UUID",
  "idea_vector": "reference to IDEA_DNA_AGENT vector store",
  "domain_id": "string",
  "tenant_id": "string",
  "creator_id": "UUID",
  "timestamp_utc": "ISO-8601"
}

EMIT_SIMILARITY_HASH: {
  "idea_id": "UUID",
  "similarity_hash": "SHA-256 of semantic fingerprint",
  "nearest_neighbor_ids": ["list of idea UUIDs within cosine distance < 0.15"],
  "domain_id": "string",
  "timestamp_utc": "ISO-8601"
}

EMIT_ORIGINALITY_SCORE: {
  "idea_id": "UUID",
  "creator_id": "UUID",
  "originality_score": "decimal 0.0–1.0",
  "chain_depth": "integer",
  "confidence_score": "decimal 0.0–1.0",
  "timestamp_utc": "ISO-8601"
}
```

**Compatible With:**
- `IDEA_DNA_AGENT` — provides parent vector for derivative scoring
- `ROYALTY_ENGINE` — receives royalty routing directive as primary financial instruction
- `COPY_DETECTION_ENGINE` — receives similarity hash to expand detection corpus

**Critical Innovation Economy Rules:**
- Attribution chain MUST resolve before any royalty event is permitted
- Originality score below `0.30` triggers mandatory human review before certificate issuance
- Circular derivation chains (A → B → A) are categorically FORBIDDEN and trigger CHAIN_VIOLATION_FLAG
- Co-creator attribution weights must sum exactly to `1.0` — enforced at schema validation

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

When this agent successfully creates an attribution chain with `originality_score >= 0.70`:

```json
RANK_UPDATE_EVENT: {
  "creator_id": "UUID",
  "tenant_id": "string",
  "domain_id": "string",
  "rank_delta_basis": "ORIGINAL_IDEA_VERIFIED",
  "originality_score": "decimal",
  "timestamp_utc": "ISO-8601",
  "source_agent": "IDEA_ATTRIBUTION_CHAIN_AGENT"
}

XP_UPDATE_EVENT: {
  "creator_id": "UUID",
  "tenant_id": "string",
  "xp_delta": "computed by RANK_ENGINE based on originality_score × domain_weight",
  "xp_event_type": "IDEA_ATTRIBUTED",
  "timestamp_utc": "ISO-8601"
}

SHARE_TRIGGER_EVENT: {
  "creator_id": "UUID",
  "share_content_type": "ORIGINALITY_CERTIFICATE",
  "share_payload_ref": "originality_certificate.certificate_id",
  "suggested_channels": ["in_app_feed", "student_portfolio", "public_profile"],
  "timestamp_utc": "ISO-8601"
}
```

**Growth Hook Rules:**
- XP events MUST NOT be emitted if `confidence_score < 0.85`
- SHARE_TRIGGER_EVENT MUST NOT be emitted for `submission_type = DERIVATIVE` unless `declared_derivative_ratio < 0.50` and `originality_score > 0.65`
- All growth events are idempotent — deduplicated by `attribution_chain_id`

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

**Required Metrics — Emitted to OBSERVABILITY_AGENT every 60 seconds:**

| Metric | Type | Alert Threshold |
|---|---|---|
| `attribution_success_rate` | Percentage | < 95% → WARNING |
| `attribution_rejection_rate` | Percentage | > 10% → WARNING, > 20% → CRITICAL |
| `chain_violation_rate` | Percentage | > 2% → WARNING, > 5% → CRITICAL |
| `avg_execution_latency_ms` | Gauge | p95 > 400ms → WARNING |
| `ml_model_confidence_avg` | Gauge | < 0.75 → WARNING (model drift indicator) |
| `upstream_timeout_count` | Counter | > 5 in 5min → CRITICAL |
| `royalty_routing_hold_count` | Counter | > 0 → ALERT (financial impact) |
| `human_review_queue_depth` | Gauge | > 50 → WARNING |
| `audit_log_write_failure_count` | Counter | > 0 → CRITICAL (compliance breach) |
| `anomaly_flag_rate` | Percentage | > 3% → WARNING |

**Integration with OBSERVABILITY_AGENT:**
- All metrics pushed via Prometheus exposition format
- Tracing via Jaeger — every execution creates a trace span with `attribution_chain_id` as correlation ID
- Dashboards in Grafana: `IDEA_ATTRIBUTION_CHAIN_AGENT — Trust Infrastructure Monitor`

---

## 1️⃣5️⃣ VERSIONING POLICY

| Rule | Enforcement |
|---|---|
| All changes are ADD-ONLY | No fields may be removed from schema — only deprecated via `deprecated: true` flag |
| Every change requires version bump | Semver — PATCH for fixes, MINOR for new optional fields, MAJOR for schema restructure |
| Backward compatibility | All prior input schema versions must remain valid for 12 months after deprecation |
| Migration documented | Every version bump requires a `MIGRATION_NOTE` entry in agent changelog |
| Rollback safe | Every deployment must pass rollback smoke test before promotion to production |
| Model versions immutable | Once a `model_version` is referenced in a production audit log, it cannot be deleted or overwritten |
| Prompt versions immutable | Same rule applies to `prompt_version` tags |

**Current Versions:**
```
AGENT_VERSION:         v1.0.0
SCHEMA_VERSION:        v1.0.0
ML_MODEL_VERSION:      v1.0.0
PROMPT_VERSION:        v1.0.0
API_CONTRACT_VERSION:  v1.0.0
```

---

## 1️⃣6️⃣ DATABASE SCHEMA (REQUIRED ENTITIES)

```sql
-- Core attribution chain record
CREATE TABLE idea_attribution_chain (
  attribution_chain_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  idea_id                 UUID NOT NULL REFERENCES ideas(idea_id),
  creator_id              UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  domain_id               VARCHAR(64) NOT NULL,
  chain_depth             INTEGER NOT NULL DEFAULT 0,
  submission_type         VARCHAR(32) NOT NULL CHECK (submission_type IN ('ORIGINAL','DERIVATIVE','COLLABORATION','TRANSFORMATION')),
  parent_idea_id          UUID REFERENCES ideas(idea_id),
  originality_score       NUMERIC(5,4) NOT NULL,
  confidence_score        NUMERIC(5,4) NOT NULL,
  model_version           VARCHAR(32) NOT NULL,
  certificate_id          UUID,
  chain_status            VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' CHECK (chain_status IN ('ACTIVE','UNDER_REVIEW','VIOLATED','REVOKED')),
  created_at_utc          TIMESTAMPTZ NOT NULL DEFAULT now(),
  -- Append-only: no updates permitted
  CONSTRAINT no_cross_tenant CHECK (tenant_id IS NOT NULL)
);

-- Attribution contributors (for collaborations and derivatives)
CREATE TABLE idea_attribution_contributor (
  contributor_record_id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  attribution_chain_id    UUID NOT NULL REFERENCES idea_attribution_chain(attribution_chain_id),
  creator_id              UUID NOT NULL,
  role                    VARCHAR(32) NOT NULL CHECK (role IN ('ORIGINAL','DERIVATIVE','COLLABORATOR','TRANSFORMER')),
  contribution_weight     NUMERIC(5,4) NOT NULL,
  chain_level             INTEGER NOT NULL,
  tenant_id               UUID NOT NULL,
  added_at_utc            TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Originality certificates
CREATE TABLE idea_originality_certificate (
  certificate_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  attribution_chain_id    UUID NOT NULL REFERENCES idea_attribution_chain(attribution_chain_id),
  creator_id              UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  originality_score       NUMERIC(5,4) NOT NULL,
  certificate_hash        CHAR(64) NOT NULL,
  validity_status         VARCHAR(32) NOT NULL DEFAULT 'VALID' CHECK (validity_status IN ('VALID','REVOKED','UNDER_REVIEW')),
  issued_at_utc           TIMESTAMPTZ NOT NULL DEFAULT now(),
  revoked_at_utc          TIMESTAMPTZ
);

-- Immutable audit log
CREATE TABLE idea_attribution_audit_log (
  audit_id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timestamp_utc           TIMESTAMPTZ NOT NULL DEFAULT now(),
  actor_id                UUID NOT NULL,
  agent_id                VARCHAR(128) NOT NULL DEFAULT 'IDEA_ATTRIBUTION_CHAIN_AGENT',
  agent_version           VARCHAR(32) NOT NULL,
  tenant_id               UUID NOT NULL,
  domain_id               VARCHAR(64),
  input_hash              CHAR(64) NOT NULL,
  output_hash             CHAR(64),
  model_version           VARCHAR(32) NOT NULL,
  prompt_version          VARCHAR(32),
  decision_path           VARCHAR(64) NOT NULL,
  confidence_score        NUMERIC(5,4),
  originality_score       NUMERIC(5,4),
  chain_depth             INTEGER,
  anomaly_flags           TEXT[],
  violation_flags         TEXT[],
  execution_duration_ms   INTEGER,
  attribution_chain_id    UUID
  -- NO foreign key on audit log — it must survive even if chain record is disputed
);

-- Row-level security enforcement
ALTER TABLE idea_attribution_chain ENABLE ROW LEVEL SECURITY;
ALTER TABLE idea_attribution_contributor ENABLE ROW LEVEL SECURITY;
ALTER TABLE idea_originality_certificate ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation_chain ON idea_attribution_chain
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

---

## 1️⃣7️⃣ API CONTRACT

```yaml
POST /api/v1/attribution/submit
Authorization: Bearer {JWT with CHAIN_WRITE scope}
Content-Type: application/json
X-Tenant-ID: {tenant_id}
X-Domain-ID: {domain_id}

Request Body: INPUT_SCHEMA (Section 3)
Response 202: { "attribution_chain_id": "UUID", "audit_reference": "UUID", "status": "PROCESSING" }
Response 400: { "error": "VALIDATION_FAILURE", "field": "...", "code": "..." }
Response 401: { "error": "UNAUTHORIZED" }
Response 403: { "error": "FORBIDDEN", "reason": "CROSS_TENANT | DOMAIN_RESTRICTION | ROLE_DENIED" }
Response 429: { "error": "RATE_LIMIT_EXCEEDED" }
Response 500: { "error": "INTERNAL_FAILURE", "audit_reference": "UUID" }

GET /api/v1/attribution/{attribution_chain_id}
Authorization: Bearer {JWT with CHAIN_READ scope}
Response 200: OUTPUT_SCHEMA (Section 4)
Response 403: FORBIDDEN if tenant mismatch
Response 404: NOT FOUND

GET /api/v1/attribution/idea/{idea_id}/chain
Authorization: Bearer {JWT with CHAIN_READ scope}
Response 200: { "chain": [attribution_chain records ordered by chain_depth] }

GET /api/v1/attribution/certificate/{certificate_id}/verify
Authorization: PUBLIC — no auth required (public verification portal)
Response 200: { "valid": true|false, "originality_score": ..., "issued_at": ..., "creator_id": ... }
```

---

## 1️⃣8️⃣ NON-NEGOTIABLE RULES

**This agent MUST NOT:**

| Rule | Description |
|---|---|
| NO hidden logic | All decision paths must be logged in `decision_path` audit field — no undocumented branches |
| NO historical record modification | Attribution chain records are append-only — zero UPDATE operations on ledger tables |
| NO auto-deletion of audit logs | Audit log retention cannot be shortened or records deleted by any automated process |
| NO override of governance agents | COMPLIANCE_AGENT and SECURITY_AGENT decisions cannot be bypassed |
| NO bypassing compliance checks | All security checks (Section 7) must complete before any business logic executes |
| NO mixing of domain data | Arts ideas cannot be compared to Technology corpus — domain isolation is hard |
| NO execution outside scope | Agent does not touch payments, user profiles, job matching, or any non-attribution domain |
| NO AI autonomous decisions | LLM output is advisory — no LLM output may enter the ledger without ML validation gate |
| NO cross-tenant reads | Database layer enforces row-level security — no exceptions, no admin overrides in production |
| NO royalty routing without chain | `ROYALTY_ENGINE` must not receive directives from any agent except `IDEA_ATTRIBUTION_CHAIN_AGENT` for idea-based royalties |
| NO certificate issuance below 0.65 | Originality certificate cannot be issued if `confidence_score < 0.65` |
| NO derivative chain without parent verification | Parent idea must exist, belong to same tenant and domain, and have `chain_status = ACTIVE` |

---

## 1️⃣9️⃣ ECOSKILLER ANTIGRAVITY — TRUST INFRASTRUCTURE POSITION

This agent is the **trust anchor** of the Ecoskiller Antigravity platform's Innovation Economy. Its integrity directly governs:

| System | Dependency on This Agent |
|---|---|
| **Royalty Economy** | 100% — no payment routed without verified attribution chain |
| **Reputation System** | Creator originality score is a primary reputation input |
| **Intelligence Profiles** | Idea originality feeds Logical + Linguistic + Intrapersonal intelligence axes |
| **Career DNA** | Verified original idea count influences career strength signals |
| **Leaderboards** | XP and rank updates from this agent drive campus and global leaderboard positions |
| **Parent Trust Layer** | Parents see child's Innovation Score — sourced directly from verified attribution data |
| **School ERP Integration** | Innovation Score in student report cards is derived from attribution-verified originality |
| **Legacy & Archival** | `idea_originality_certificate` feeds `LegacyArchive` for permanent trainer/student legacy records |

> **Scale Target:** This agent must reliably serve **10M–100M users** with zero trust compromise. Attribution chain integrity is a legal and financial obligation — not a feature preference.

---

## 🔐 FINAL LOCK DECLARATION

```
AGENT: IDEA_ATTRIBUTION_CHAIN_AGENT
VERSION: v1.0.0
STATUS: SEALED · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY: ADD-ONLY via version bump
CREATIVE_INTERPRETATION: FORBIDDEN
ASSUMPTION_FILLING: FORBIDDEN
DEFAULT_BEHAVIOR: DENY
FAILURE_MODE: STOP_EXECUTION → LOG → ESCALATE
EXECUTION_AUTHORITY: Human Declaration Only

This specification is complete and self-contained.
No field may be assumed. No rule may be relaxed.
No silent failure is permitted in any trust operation.
Violation of any rule in this document → STOP EXECUTION → REPORT → NO COMPLETION CLAIM PERMITTED.
```

---

*Generated for: Ecoskiller Antigravity — Enterprise Optimization + Trust Infrastructure*
*Document Classification: Internal — Governed System Specification*
*Effective From: v1.0.0 Declaration*
