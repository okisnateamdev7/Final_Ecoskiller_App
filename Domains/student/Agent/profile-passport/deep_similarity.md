# 🔒 SEALED & LOCKED AGENT PROMPT
## DEEP_SIMILARITY_AGENT
### ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE

---

```
EXECUTION_MODE          = LOCKED
MUTATION_POLICY         = ADD_ONLY
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING      = FORBIDDEN
DEFAULT_BEHAVIOR        = DENY
FAILURE_MODE            = STOP_EXECUTION
VERSION                 = 1.0.0
SEAL_STATUS             = IMMUTABLE
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```
AGENT_NAME        = DEEP_SIMILARITY_AGENT
SYSTEM_ROLE       = Content & Concept Similarity Detection Engine
PRIMARY_DOMAIN    = Idea Integrity | Plagiarism Detection | Originality Scoring
EXECUTION_MODE    = Deterministic + Validated
DATA_SCOPE        = Cross-tenant anonymized comparison index (read-only)
                    Per-tenant origination store (isolated write)
TENANT_SCOPE      = STRICT ISOLATION — no cross-tenant raw data exposure
FAILURE_POLICY    = HALT on ambiguity | LOG_INCIDENT | ESCALATE_TO = GOVERNANCE_AGENT
```

> This agent **NEVER** exposes content from one tenant to another.
> Similarity scoring uses only anonymized, hashed, vectorized representations.
> Raw content **NEVER** crosses tenant boundary lines.

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved
Ecoskiller Antigravity hosts 300+ user types — students, trainers, course creators, assessment designers, project submitters, idea contributors, and GD (Dojo) participants — all producing original intellectual content at scale. Without a deep similarity engine, the platform cannot:

- Detect copied or paraphrased assessments, projects, and course content
- Score originality of ideas submitted to the Innovation Economy
- Protect legitimate creators from having their work replicated
- Enforce academic and professional integrity at 10M–100M user scale

### Input Consumed
- Text content (submissions, assessments, course modules, ideas, GD transcripts, project reports, resumes, proposals)
- Structured metadata (user_id, tenant_id, domain_track, content_type, submission_timestamp)
- Embed vectors from the **EMBEDDING_AGENT** (upstream dependency)
- Historical corpus index from the **FEATURE_STORE_AGENT**

### Output Produced
- `similarity_score` (0.00–1.00): how similar this content is to the closest known match
- `originality_score` (0.00–1.00): inverse measure of novelty
- `match_references[]`: anonymized references to top-k matching content (tenant-isolated)
- `plagiarism_flag` (boolean): threshold-triggered hard flag
- `idea_vector`: compressed semantic vector for Innovation Economy routing
- `similarity_hash`: deterministic content fingerprint
- `audit_reference` (UUID): immutable trace ID

### Downstream Agents Depending on This Agent
- `IDEA_DNA_AGENT` — receives `idea_vector` + `originality_score`
- `ROYALTY_ENGINE` — uses originality to determine royalty eligibility
- `COPY_DETECTION_ENGINE` — triggers enforcement on `plagiarism_flag = TRUE`
- `RANK_UPDATE_AGENT` — penalizes or rewards based on originality
- `ASSESSMENT_INTEGRITY_AGENT` — uses score to validate exam/quiz submissions
- `OBSERVABILITY_AGENT` — receives performance metrics

### Upstream Agents Feeding This Agent
- `EMBEDDING_AGENT` — provides normalized semantic vectors
- `CONTENT_INGEST_AGENT` — delivers pre-validated raw content
- `FEATURE_STORE_AGENT` — provides historical vector index for comparison
- `IDENTITY_AGENT` — provides verified user_id + tenant_id for isolation enforcement

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "submission_id",
    "tenant_id",
    "user_id",
    "domain_track",
    "content_type",
    "content_text",
    "embed_vector",
    "submission_timestamp_utc"
  ],
  "optional_fields": [
    "content_title",
    "parent_content_id",
    "reference_urls",
    "declared_sources"
  ],
  "validation_rules": [
    "submission_id must be UUID v4",
    "tenant_id must match authenticated session context",
    "user_id must belong to tenant_id — cross-tenant mismatch = REJECT",
    "domain_track must be one of: Arts | Commerce | Science | Technology | Administration",
    "content_type must be one of: assessment | course_module | project_report | idea_submission | gd_transcript | resume | proposal",
    "content_text length: minimum 50 chars, maximum 500,000 chars",
    "embed_vector must be float32[], dimension = 1536 (enforced by schema)",
    "submission_timestamp_utc must be ISO 8601 format",
    "No null values in required_fields without explicit null_policy = PERMITTED in caller contract"
  ],
  "security_checks": [
    "Tenant isolation: tenant_id must match JWT claim",
    "Role authorization: only roles [student, trainer, course_creator, assessment_creator, admin] may submit",
    "Input sanitization: strip all HTML, scripts, executable content before processing",
    "Content encryption: content_text in transit must be TLS 1.3 minimum",
    "Rate limit: max 100 submissions/minute per tenant, 10 per user"
  ],
  "domain_checks": [
    "domain_track must match user's enrolled or authorized domain",
    "Cross-domain submissions FORBIDDEN unless role = admin or global_trainer"
  ]
}
```

**Rules:**
- No null tolerance on required fields without explicit NULL_POLICY contract
- Malformed embed_vector → REJECT immediately, log validation failure
- All validation failures → append-only log entry, no silent discard

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "submission_id": "UUID",
    "tenant_id": "string",
    "user_id": "string",
    "domain_track": "string",
    "content_type": "string",
    "similarity_score": "float [0.00–1.00]",
    "originality_score": "float [0.00–1.00]",
    "plagiarism_flag": "boolean",
    "plagiarism_severity": "NONE | LOW | MEDIUM | HIGH | CRITICAL",
    "match_references": [
      {
        "match_id": "UUID (anonymized)",
        "similarity_pct": "float",
        "content_type": "string",
        "domain_track": "string",
        "tenant_visible": false
      }
    ],
    "idea_vector": "float32[] (1536-dim)",
    "similarity_hash": "SHA-256 deterministic fingerprint",
    "processing_time_ms": "integer"
  },
  "confidence_score": "float [0.00–1.00]",
  "model_version": "string",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "IDEA_DNA_VECTOR_EMIT",
    "COPY_DETECTION_REVIEW (if plagiarism_flag = true)",
    "RANK_PENALTY_EVENT (if plagiarism_severity = HIGH | CRITICAL)",
    "ROYALTY_ELIGIBILITY_CHECK (if originality_score >= 0.80)"
  ]
}
```

**All outputs must include:**
- `confidence_score` — model certainty, never omitted
- `model_version` — for audit reproducibility
- `audit_reference` — immutable UUID, linked to append-only log
- `next_trigger_event[]` — downstream event cascade is always declared

---

## 5️⃣ ML / AI LOGIC LAYER

### Model Architecture (Primary — 80% ML)

```
MODEL_TYPE          = Semantic Similarity + Clustering
PRIMARY_ALGORITHM   = Approximate Nearest Neighbour Search (ANN)
                      Cosine Similarity on Dense Embeddings
SECONDARY_ALGORITHM = TF-IDF fingerprinting for structural matches
                      MinHash LSH for near-duplicate detection
ENSEMBLE_STRATEGY   = Weighted ensemble: ANN (60%) + MinHash (25%) + TF-IDF (15%)

FEATURES_USED:
  - embed_vector (1536-dim semantic embedding from EMBEDDING_AGENT)
  - content_length_normalized
  - domain_track_encoded (one-hot)
  - content_type_encoded (one-hot)
  - structural_n_gram_hash
  - minhash_signature (128 permutations)
  - tf_idf_vector (top 300 features)
  - sentence_boundary_pattern

TRAINING_FREQUENCY:
  - ANN index: Incremental daily update (append-only corpus additions)
  - TF-IDF vocabulary: Weekly rebuild
  - MinHash bands: Rebuilt on schema version change only
  - Threshold calibration: Monthly using labeled flagged/cleared samples

DRIFT_DETECTION:
  - Monitor: cosine similarity score distribution (weekly percentile shift > 5% = alert)
  - Monitor: plagiarism_flag rate per domain_track (spike > 3σ = alert)
  - Monitor: false positive rate from COPY_DETECTION_ENGINE feedback loop
  - Monitor: embedding distribution shift (KL divergence from baseline)

VERSION_CONTROL:
  - model_version stored as immutable string on every output
  - No model replaces previous — versioned side-by-side
  - ANN index versioned by build_date + corpus_size hash
```

### AI Layer (Supplementary — 20% LLM)

```
AI_USAGE_SCOPE:
  - Paraphrase detection: LLM detects structural rewrites that ANN misses
  - Borderline adjudication: When 0.65 < similarity_score < 0.75 (grey zone)
  - Explanation generation: Human-readable similarity reason for transparency report
  - Novel idea classification: Assist IDEA_DNA_AGENT with semantic novelty framing

PROMPT_GOVERNANCE:
  - All LLM prompts are versioned (prompt_version: string)
  - Prompts are deterministic — no open-ended generation
  - LLM produces only structured JSON output — no free text in decision path
  - LLM is advisory only — NEVER overrides ML similarity score
  - LLM reasoning must be logged as audit_ai_reasoning (append-only)

AI must ASSIST ML. AI must NEVER replace or override ML similarity score.
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS        = 5,000 submissions/second (peak — 100M user scale)
LATENCY_TARGET      = p95 < 200ms | p99 < 500ms
MAX_CONCURRENCY     = 2,000 parallel comparisons per node
QUEUE_STRATEGY      = Kafka topic: similarity.submission.queue
                      Partitioned by tenant_id for isolation
                      Consumer group: similarity_workers (auto-scaled)
SCALING_MODEL       = Horizontal pod autoscaling (HPA) on Kubernetes
                      Stateless execution — no local state
                      ANN index served by dedicated vector-search pods (Milvus/Qdrant)
IDEMPOTENCY         = submission_id-based deduplication (Redis TTL: 24h)
ASYNC_PROCESSING    = Non-blocking — results emitted via Kafka event
                      Sync endpoint available for low-latency assessment use cases (< 10KB content only)
```

---

## 7️⃣ SECURITY ENFORCEMENT

```
TENANT_ISOLATION:
  - tenant_id validated on every request against JWT claim
  - ANN index partitioned by tenant_id — no cross-partition queries
  - Match references returned with tenant_visible = false ALWAYS
  - Cross-tenant raw content access = HARD BLOCK + SECURITY_INCIDENT log

DOMAIN_ISOLATION:
  - Comparisons restricted to domain_track of submission
  - domain_track mismatch between content and user → REJECT

ROLE-BASED AUTHORIZATION:
  - Submit: student | trainer | course_creator | assessment_creator | admin
  - View full match details: admin | compliance_admin | assessment_evaluator only
  - View own score: submission owner only
  - Cross-tenant analytics: platform_super_admin only

ENCRYPTION:
  - content_text: TLS 1.3 in transit, AES-256 at rest
  - embed_vector: AES-256 at rest
  - similarity_hash: stored as SHA-256, never reversed

AUDIT LOGGING (APPEND-ONLY):
  - Every execution logged immediately, no buffering
  - Log records are immutable — no DELETE, no UPDATE ever
  - Stored in append-only audit store (separate from operational DB)

ACCESS LOG TRACKING:
  - Every read of match_references logged with actor_id + timestamp
  - Anomalous access patterns → FRAUD_DETECTION_AGENT alert
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every execution emits one immutable audit record:

```json
{
  "timestamp_utc": "ISO 8601",
  "actor_id": "user_id:tenant_id",
  "submission_id": "UUID",
  "input_hash": "SHA-256 of full input payload",
  "output_hash": "SHA-256 of full output payload",
  "model_version": "string",
  "prompt_version": "string (if LLM used)",
  "decision_path": [
    "ANN_SEARCH_COMPLETE",
    "MINHASH_COMPARISON_COMPLETE",
    "TFIDF_COMPARISON_COMPLETE",
    "ENSEMBLE_WEIGHTED_SCORE",
    "THRESHOLD_EVALUATION",
    "FLAG_DECISION",
    "EVENT_EMITTED"
  ],
  "confidence_score": "float",
  "anomaly_flags": ["string"],
  "ai_reasoning_ref": "UUID (if LLM invoked, links to ai_reasoning log)"
}
```

- Logs are **WRITE-ONCE** — stored in append-only event store
- Logs are **NEVER** deleted by any agent, including this one
- Logs must be accessible to `COMPLIANCE_ADMIN`, `PLATFORM_SUPER_ADMIN`, `AUDIT_ADMIN`

---

## 9️⃣ FAILURE POLICY

| Failure Condition               | Response                                                                 |
|---------------------------------|--------------------------------------------------------------------------|
| Invalid input (schema violation) | STOP_EXECUTION → LOG_INCIDENT → Return structured error to caller       |
| ANN index unavailable           | HALT → LOG_INCIDENT → ESCALATE_TO = PLATFORM_DEVOPS_AGENT → RETRY x3 with exponential backoff |
| LLM timeout (> 5s)              | SKIP LLM layer → Complete with ML-only output → Flag `ai_skipped = true` in output |
| Embedding dimension mismatch    | REJECT input → LOG_INCIDENT → Notify EMBEDDING_AGENT of schema drift     |
| Confidence score < 0.50         | Return result with `low_confidence = true` → ESCALATE_TO = HUMAN_IN_THE_LOOP_REVIEWER |
| Data corruption detected        | STOP_EXECUTION → LOG_INCIDENT → QUARANTINE submission → ESCALATE_TO = DATA_PROTECTION_OFFICER |
| Kafka publish failure           | Retry x5 → Dead-letter queue → ALERT INCIDENT_RESPONSE_MANAGER           |
| Cross-tenant breach detected    | IMMEDIATE STOP → SECURITY_INCIDENT_EVENT → ESCALATE_TO = CISO + SECURITY_ADMIN |

```
RETRY_POLICY    = Exponential backoff: 100ms → 200ms → 400ms → 800ms → 1600ms → DLQ
SILENT_FAILURE  = FORBIDDEN — every failure must produce a log entry
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  - EMBEDDING_AGENT          → provides embed_vector (1536-dim)
  - CONTENT_INGEST_AGENT     → delivers sanitized content_text + metadata
  - FEATURE_STORE_AGENT      → provides historical vector index
  - IDENTITY_AGENT           → validates user_id + tenant_id per request

DOWNSTREAM_AGENTS:
  - IDEA_DNA_AGENT           → receives idea_vector + originality_score
  - ROYALTY_ENGINE           → receives originality_score + submission_id for royalty calc
  - COPY_DETECTION_ENGINE    → receives plagiarism_flag + match_references for enforcement
  - RANK_UPDATE_AGENT        → receives originality-based XP/rank signal
  - ASSESSMENT_INTEGRITY_AGENT → receives similarity_score for exam validation
  - OBSERVABILITY_AGENT      → receives latency, error rate, drift metrics
  - HUMAN_IN_THE_LOOP_REVIEWER → receives low-confidence cases

EVENT_TRIGGERS (Kafka Topics Emitted):
  - similarity.result.complete         → all downstream agents listen
  - similarity.plagiarism.flagged      → COPY_DETECTION_ENGINE + COMPLIANCE_ADMIN
  - similarity.idea.vector.ready       → IDEA_DNA_AGENT
  - similarity.royalty.eligible        → ROYALTY_ENGINE
  - similarity.rank.signal             → RANK_UPDATE_AGENT
  - similarity.low.confidence          → HUMAN_IN_THE_LOOP_REVIEWER
  - similarity.security.breach         → SECURITY_ADMIN + CISO
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user behavior and must emit to the feature store:

```json
EMIT_FEATURE_VECTOR: {
  "user_id": "string",
  "tenant_id": "string",
  "feature_name": "similarity_originality_score",
  "feature_value": "float [0.00–1.00]",
  "timestamp": "ISO 8601",
  "source_agent": "DEEP_SIMILARITY_AGENT"
}

EMIT_FEATURE_VECTOR: {
  "user_id": "string",
  "tenant_id": "string",
  "feature_name": "plagiarism_flag_count_30d",
  "feature_value": "integer",
  "timestamp": "ISO 8601",
  "source_agent": "DEEP_SIMILARITY_AGENT"
}

EMIT_FEATURE_VECTOR: {
  "user_id": "string",
  "tenant_id": "string",
  "feature_name": "avg_originality_score_90d",
  "feature_value": "float",
  "timestamp": "ISO 8601",
  "source_agent": "DEEP_SIMILARITY_AGENT"
}
```

All feature vectors → `FEATURE_STORE_AGENT` (Kafka topic: `feature.store.ingest`)

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

This agent is a primary feeder to the Innovation Economy.

```json
EMIT ON EVERY idea_submission content_type:

{
  "submission_id": "UUID",
  "user_id": "string",
  "tenant_id": "string",
  "IDEA_VECTOR": "float32[] (1536-dim)",
  "SIMILARITY_HASH": "SHA-256",
  "ORIGINALITY_SCORE": "float [0.00–1.00]",
  "timestamp_utc": "ISO 8601"
}
```

Compatible with:
- `IDEA_DNA_AGENT` — receives IDEA_VECTOR for lineage mapping
- `ROYALTY_ENGINE` — uses ORIGINALITY_SCORE to gate royalty eligibility (threshold: >= 0.80)
- `COPY_DETECTION_ENGINE` — uses SIMILARITY_HASH for forensic matching

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

Originality and integrity achievements drive the Growth Engine:

```
IF originality_score >= 0.90 AND content_type IN [idea_submission, course_module, project_report]:
  EMIT → RANK_UPDATE_EVENT   (XP bonus for original contribution)
  EMIT → XP_UPDATE_EVENT     (originality XP: configurable per domain)
  EMIT → SHARE_TRIGGER_EVENT (if user opts in: "You created something truly original")

IF plagiarism_flag = TRUE AND plagiarism_severity IN [HIGH, CRITICAL]:
  EMIT → RANK_PENALTY_EVENT  (XP deduction per governance policy)
  DO NOT EMIT SHARE_TRIGGER_EVENT
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```
METRICS (emitted to OBSERVABILITY_AGENT every 60s):

  similarity_agent_success_rate          → target: >= 99.5%
  similarity_agent_error_rate            → alert threshold: > 0.5%
  similarity_agent_p95_latency_ms        → target: < 200ms
  similarity_agent_p99_latency_ms        → target: < 500ms
  similarity_agent_plagiarism_flag_rate  → anomaly: > 3σ from 30d avg
  similarity_agent_low_confidence_rate   → alert: > 5% of submissions
  similarity_agent_ai_skip_rate          → alert: > 10% (indicates LLM instability)
  similarity_agent_drift_indicator       → KL divergence from baseline embedding dist
  similarity_agent_anomaly_frequency     → count per hour

INTEGRATION: Kafka topic → similarity.metrics.stream → OBSERVABILITY_AGENT → Prometheus → Grafana
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```
ALL_CHANGES       = ADD_ONLY
BACKWARD_COMPAT   = MANDATORY — no breaking schema changes
MIGRATION_DOCS    = Required before any schema version increment
ROLLBACK_SAFE     = Every version must support safe rollback to version N-1
MODEL_VERSIONING  = Side-by-side deployment — new model runs parallel before cutover
PROMPT_VERSIONING = All LLM prompts stored with version tag in prompt_registry
SCHEMA_VERSION    = Semver (MAJOR.MINOR.PATCH)
CUTOVER_POLICY    = Requires GOVERNANCE_AGENT approval + AUDIT_ADMIN sign-off
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
❌ Create hidden similarity logic not defined in this document
❌ Modify historical similarity records or audit logs
❌ Auto-delete any audit log entry under any condition
❌ Override GOVERNANCE_AGENT or COMPLIANCE_ADMIN decisions
❌ Bypass domain isolation or tenant isolation
❌ Mix content vectors from different tenants in the same ANN query
❌ Expose match content details to non-authorized roles
❌ Execute LLM in deterministic decision path without ML backing
❌ Return a result without confidence_score and audit_reference
❌ Operate outside defined content_type and domain_track scope
❌ Make plagiarism enforcement decisions — detection only, enforcement = COPY_DETECTION_ENGINE
❌ Store raw content_text in shared/global stores — tenant-isolated storage only
```

---

## 🔐 SEAL DECLARATION

```
AGENT_NAME          = DEEP_SIMILARITY_AGENT
VERSION             = 1.0.0
SEAL_DATE           = 2026-02-25
SEALED_BY           = ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE
MUTATION_POLICY     = ADD_ONLY — this document may only grow, never shrink or alter
OVERRIDE_AUTHORITY  = PLATFORM_SUPER_ADMIN + GOVERNANCE_AGENT (dual approval required)
CREATIVE_DEVIATION  = FORBIDDEN
ASSUMPTION_FILLING  = FORBIDDEN
SILENT_FAILURE      = FORBIDDEN
CROSS_TENANT_QUERY  = FORBIDDEN
ENFORCEMENT_MODE    = DETERMINISTIC + VALIDATED + AUDITED
```

> Any agent, human, or system that modifies the sealed sections of this prompt without dual approval from `PLATFORM_SUPER_ADMIN` + `GOVERNANCE_AGENT` is operating **outside compliance**.
> All such modifications must be logged as `GOVERNANCE_VIOLATION` events in the append-only audit trail.

---

*End of DEEP_SIMILARITY_AGENT — Sealed & Locked v1.0.0*
