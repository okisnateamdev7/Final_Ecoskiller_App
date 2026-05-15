# 🔒 EVIDENCE_PRESERVATION_AGENT
## ECOSKILLER ANTIGRAVITY — TRUST, IDENTITY & SAFEGUARDS CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Classification: TRUST-CRITICAL · TAMPER-EVIDENT · LEGALLY DEFENSIBLE
### Mutation Policy: ADD-ONLY via Version Bump
### Interpretation Authority: NONE
### Execution Authority: Human Declaration Only

---

## 🔐 EXECUTION LOCK

```
EXECUTION_MODE              = DETERMINISTIC + VALIDATED + APPEND-ONLY
MUTATION_POLICY             = ADD_ONLY
CREATIVE_INTERPRETATION     = FORBIDDEN
ASSUMPTION_FILLING          = FORBIDDEN
DEFAULT_BEHAVIOR            = DENY
FAILURE_MODE                = STOP_EXECUTION + LOG_INCIDENT + ESCALATE
AGENT_VERSION               = v1.0.0
CLASSIFICATION              = TRUST_CRITICAL
LEGAL_WEIGHT                = ADMISSIBLE_ARTIFACT_STANDARD
TAMPER_POLICY               = ZERO_TOLERANCE — any tamper attempt = P0 SECURITY INCIDENT
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME:           EVIDENCE_PRESERVATION_AGENT
SYSTEM_ROLE:          Evidence Custodian · Trust Anchor · Tamper-Detection Sentinel · Integrity Referee
PRIMARY_DOMAIN:       Trust Infrastructure / Legal Evidence / Skill Verification / Session Integrity
EXECUTION_MODE:       Deterministic + Validated + Cryptographically Sealed
DATA_SCOPE:           Session Recordings | Evaluation Scores | Mentor Decisions | Project Milestones |
                      Skill Certifications | Offer Letters | Portfolio Artifacts | Dispute Records |
                      Anti-Cheat Forensics | Consent Captures | Identity Verification Events
TENANT_SCOPE:         STRICT ISOLATION — No cross-tenant evidence queries permitted under any condition
FAILURE_POLICY:       HALT on ambiguity | HALT on hash mismatch | HALT on chain break |
                      ESCALATE to GOVERNANCE_AGENT + SECURITY_AGENT immediately
DOMAIN_LOCK:          Arts | Commerce | Science | Technology | Administration
                      Evidence from one domain NEVER accessible by another domain without explicit
                      GOVERNANCE_AGENT override + audit record
PLATFORM_CONTEXT:     Ecoskiller Antigravity — Enterprise Multi-Tenant SaaS at 10M–100M user scale
LEGAL_CONTEXT:        Evidence must meet admissibility standards for: Academic dispute resolution |
                      Employment offer disputes | Skill fraud claims | Regulatory compliance audits |
                      GDPR / India DPDP data subject requests | Parental visibility obligations
```

This agent is the **immutable trust backbone** of Ecoskiller Antigravity. It seals, chains, timestamps, verifies, and custodies every piece of evidence generated across all platform engines — Dojo sessions, project milestones, evaluations, certifications, hiring events, anti-cheat records, and consent captures. It operates write-once, read-controlled. It never deletes. It never modifies. It never silently fails.

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 Problem Solved

The EVIDENCE_PRESERVATION_AGENT resolves the following critical trust and safeguard gaps in Ecoskiller Antigravity:

- **Score Tampering Risk**: Dojo and evaluation scores modified post-session without detection
- **Certification Fraud**: Skill certificates issued without verifiable evidence chain
- **Offer Letter Disputes**: Employment offers contested with no tamper-proof record
- **Project Milestone Fraud**: Milestone completion claimed without evidence-linked verification
- **Consent Gaps**: Recording and data-use consent not cryptographically captured, creating legal exposure
- **Anti-Cheat Evidence Loss**: Cheating events detected but forensic evidence not preserved for dispute
- **Replay Integrity Risk**: Session replays modified or selectively deleted to protect bad actors
- **Identity Substitution**: User impersonation during live evaluations not detectable post-session
- **Parent Visibility Trust Gap**: Parents unable to verify child's performance records are authentic
- **Regulatory Non-Compliance**: No auditable data lineage for GDPR / India DPDP subject access requests

### 2.2 Input Sources
- `DOJO_ENGINE_AGENT` — Live session events, scoring events, mentor override commands
- `SCORING_ENGINE_AGENT` — Raw score submissions, peer scores, mentor scores, final merged scores
- `PROJECT_EXECUTION_ENGINE_AGENT` — Milestone completion events, evaluator sign-off records
- `CERTIFICATION_ENGINE_AGENT` — Certificate issuance decisions, belt promotion records
- `JOB_PORTAL_ENGINE_AGENT` — Offer letter events, application decisions, recruiter actions
- `ANTI_CHEAT_ENGINE_AGENT` — Anomaly events, forensic snapshots, violation classifications
- `IDENTITY_AGENT` — Identity verification events, MFA completions, device session records
- `CONSENT_ENGINE_AGENT` — Recording consent captures, TOS acceptance, data-use acknowledgements
- `PASSIVE_INTELLIGENCE_AGENT` — Behavioral anomaly signals for forensic enrichment
- `MEDIA_ENGINE_AGENT` — Recording finalization events, replay generation confirmations

### 2.3 Output Produced
- Sealed Evidence Packages (cryptographically chained, timestamped, tenant-isolated)
- Integrity Verification Responses (on-demand: valid / tampered / chain-broken)
- Evidence Manifests (for regulatory audits, legal disputes, parent visibility)
- Anti-Cheat Forensic Archives (immutable, time-limited access, role-gated)
- Consent Proof Records (legally defensible consent audit trail)
- Certificate Evidence Chains (complete lineage from training → evaluation → issuance)
- Dispute Evidence Bundles (structured packages for dispute resolution agents)
- Replay Integrity Seals (hash-verified replay access certificates)

### 2.4 Downstream Agents
- `GOVERNANCE_AGENT` — receives integrity breach alerts and compliance audit packages
- `DISPUTE_RESOLUTION_AGENT` — receives structured evidence bundles for dispute proceedings
- `CERTIFICATION_ENGINE_AGENT` — receives evidence chain validation before certificate issuance
- `SECURITY_AGENT` — receives tamper detection alerts and anti-cheat forensic escalations
- `PARENT_VISIBILITY_AGENT` — receives read-only verified evidence summaries for parent trust layer
- `OBSERVABILITY_AGENT` — receives chain health metrics and preservation system telemetry
- `ROYALTY_ENGINE` — receives idea-evidence chains for originality verification
- `COMPLIANCE_AGENT` — receives data subject access packages for GDPR / DPDP requests

### 2.5 Upstream Agents
- `DOJO_ENGINE_AGENT`
- `SCORING_ENGINE_AGENT`
- `PROJECT_EXECUTION_ENGINE_AGENT`
- `CERTIFICATION_ENGINE_AGENT`
- `JOB_PORTAL_ENGINE_AGENT`
- `ANTI_CHEAT_ENGINE_AGENT`
- `IDENTITY_AGENT`
- `CONSENT_ENGINE_AGENT`
- `MEDIA_ENGINE_AGENT`
- `PASSIVE_INTELLIGENCE_AGENT`

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "tenant_id",
    "user_id",
    "evidence_type",
    "source_agent",
    "source_event_id",
    "event_timestamp_utc",
    "domain_tag",
    "user_role",
    "payload_hash",
    "payload_data"
  ],
  "optional_fields": [
    "session_id",
    "match_id",
    "project_id",
    "milestone_id",
    "certificate_id",
    "offer_id",
    "dispute_id",
    "anti_cheat_event_id",
    "consent_token",
    "evaluator_id",
    "mentor_id",
    "parent_id",
    "replay_id",
    "recording_url_signed",
    "identity_verification_id",
    "idea_id",
    "forensic_snapshot_url",
    "witness_agent_ids"
  ],
  "validation_rules": [
    "tenant_id must match active tenant registry — reject if unknown",
    "user_id must belong to tenant_id — reject cross-tenant reference IMMEDIATELY",
    "evidence_type must be in EVIDENCE_TYPE_REGISTRY — reject unknown types",
    "source_agent must be in APPROVED_SOURCE_AGENT_LIST — reject unauthorized emitters",
    "event_timestamp_utc must be ISO 8601 UTC — reject malformed or future-dated > 60 seconds",
    "domain_tag must be in [Arts, Commerce, Science, Technology, Administration] — reject unknown",
    "user_role must be in [STUDENT, TRAINER, EVALUATOR, INSTITUTE, ENTERPRISE, RECRUITER, ADMIN, PARENT, AUTOMATION] — reject unknown",
    "payload_hash must equal SHA-256(payload_data) — reject if hash does not match",
    "payload_data must not exceed 50MB per event — reject oversized payloads",
    "recording_url_signed if present must be a signed URL from MEDIA_ENGINE — reject unsigned URLs",
    "consent_token if required by evidence_type must be present and valid — reject missing consent on consent-required types",
    "evaluator_id if present must be a verified EVALUATOR role user in tenant — reject unverified evaluators",
    "witness_agent_ids if present must all be in APPROVED_SOURCE_AGENT_LIST"
  ],
  "security_checks": [
    "Validate tenant JWT signature before accepting any event — hard block on invalid JWT",
    "Reject any event where user_id.tenant_id != JWT.tenant_id",
    "Enforce domain isolation — domain_tag must match user's registered domain scope",
    "All inputs encrypted in transit (TLS 1.3 minimum)",
    "Payload hash verified client-side and re-verified on receipt — double-hash validation",
    "Source agent identity verified via agent-to-agent signed token",
    "Rate limit: max 10,000 evidence events per tenant per minute — queue excess; never drop"
  ],
  "domain_checks": [
    "Evidence from domain A may not be accessed by users in domain B without GOVERNANCE_AGENT override",
    "Institute evidence namespace isolated from Corporate evidence namespace",
    "Parent access restricted to own child's evidence only — enforced at query layer"
  ]
}
```

**Evidence Type Registry (LOCKED — Add-Only):**
```yaml
EVIDENCE_TYPE_REGISTRY:
  - DOJO_SESSION_RECORDING        # Live match session recording
  - DOJO_SCORE_SUBMISSION         # Raw score from peer/mentor/self
  - DOJO_SCORE_FINAL              # Merged and weighted final score
  - MENTOR_OVERRIDE_COMMAND       # Mentor score override with reason
  - ANTI_CHEAT_VIOLATION          # Detected cheating event + forensic snapshot
  - ANTI_CHEAT_BEHAVIORAL_LOG     # Behavioral anomaly stream during session
  - PROJECT_MILESTONE_COMPLETION  # Evaluator-signed milestone record
  - PROJECT_SUBMISSION_ARTIFACT   # Student-submitted project deliverable
  - CERTIFICATION_ISSUANCE        # Belt/certification issuance decision record
  - CERTIFICATION_EVIDENCE_CHAIN  # Complete chain: training → eval → issuance
  - OFFER_LETTER_ISSUED           # Employer-issued offer event
  - OFFER_LETTER_ACCEPTED         # Candidate acceptance event
  - OFFER_LETTER_REVOKED          # Revocation event with reason
  - CONSENT_CAPTURE               # Recording/TOS/data-use consent with timestamp
  - IDENTITY_VERIFICATION_EVENT   # MFA completion, biometric check, device binding
  - REPLAY_INTEGRITY_SEAL         # Hash-verified replay access certificate
  - DISPUTE_EVIDENCE_BUNDLE       # Structured evidence package for dispute
  - IDEA_ORIGINALITY_PROOF        # Timestamped idea submission for IP protection
  - PARENT_VISIBILITY_RECORD      # Verified summary record for parent trust layer
  - REGULATORY_AUDIT_PACKAGE      # Data subject access / compliance export
```

**Null Tolerance Policy:**
- `tenant_id`, `user_id`, `evidence_type`, `source_agent`, `payload_hash`, `payload_data` — ZERO null tolerance. Reject immediately. Log rejection.
- All rejections appended to `VALIDATION_FAILURE_LOG` with full input hash. No silent drops. No exceptions.

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "evidence_id": "UUID v4 — globally unique, tenant-scoped",
    "evidence_type": "string — from EVIDENCE_TYPE_REGISTRY",
    "tenant_id": "string",
    "user_id": "string",
    "domain_tag": "string",
    "preservation_timestamp_utc": "ISO8601 — moment of sealing",
    "chain_sequence_number": "integer — monotonically increasing per tenant chain",
    "chain_hash": "string — SHA-256(previous_chain_hash + payload_hash + preservation_timestamp_utc)",
    "payload_hash": "string — SHA-256 of raw payload",
    "payload_encryption_key_ref": "string — Vault key reference (never the key itself)",
    "source_agent": "string",
    "source_event_id": "string — original event ID from source agent",
    "integrity_status": "SEALED | VERIFIED | TAMPERED | CHAIN_BROKEN",
    "legal_admissibility_score": "decimal 0.0–1.0",
    "access_policy": {
      "read_roles": ["role list authorized to read this evidence"],
      "expiry_utc": "ISO8601 or null for permanent",
      "requires_consent_token": "boolean",
      "parent_visible": "boolean"
    },
    "witness_agent_ids": ["agent IDs that co-signed this evidence"],
    "next_chain_anchor": "string — placeholder for next event's chain_hash computation"
  },
  "confidence_score": "decimal 0.0–1.0",
  "model_version": "string — e.g. evidence-engine-v1.0.0",
  "audit_reference": "UUID v4",
  "next_trigger_events": [
    "EVIDENCE_SEALED",
    "CHAIN_UPDATED",
    "INTEGRITY_VERIFIED",
    "TAMPER_DETECTED",
    "DISPUTE_BUNDLE_READY",
    "CONSENT_PROOF_SEALED",
    "CERTIFICATION_CHAIN_VALIDATED",
    "ANTI_CHEAT_ARCHIVE_SEALED",
    "REGULATORY_PACKAGE_READY"
  ]
}
```

**All outputs must include:**
- Cryptographic chain hash (linking to previous event — chain continuity mandatory)
- Preservation timestamp (server-side UTC — client timestamps not trusted)
- Evidence ID (immutable, globally unique within tenant scope)
- Integrity status (computed on every read, not stored as mutable state)
- Legal admissibility score (ML-computed, based on evidence completeness and chain integrity)
- Access policy (role-gated, consent-aware, parent-visibility-aware)

---

## 5️⃣ ML / AI LOGIC LAYER

### 5.1 ML Layer (Primary — 78% of intelligence)

```yaml
MODEL_TYPE:
  - Classification:
      - Evidence completeness classifier (Complete / Incomplete / At-Risk)
      - Tamper risk classifier (Low / Medium / High / Critical)
      - Legal admissibility scorer (0.0–1.0)
      - Anti-cheat behavioral pattern classifier
  - Anomaly Detection:
      - Score variance anomaly (peer vs mentor vs self divergence)
      - Session behavioral anomaly (keypress patterns, inactivity gaps, identity signals)
      - Chain continuity anomaly (unexpected gaps, out-of-sequence events)
      - Access pattern anomaly (unusual read frequency, off-hours access, bulk exports)
  - Time-Series:
      - Evidence ingestion rate monitoring (detect spikes = possible bulk fraud)
      - Chain health trending

FEATURES_USED:
  # Evidence completeness features
  - evidence_type
  - payload_size_bytes
  - consent_token_present
  - evaluator_id_present
  - witness_agent_count
  - source_agent_trust_score
  # Tamper risk features
  - time_delta_event_to_preservation_ms
  - hash_verification_result
  - chain_sequence_gap
  - access_count_since_sealing
  - read_actor_roles
  # Anti-cheat behavioral features
  - session_keystroke_variance
  - focus_event_count_per_minute
  - webcam_presence_signal (if applicable)
  - ip_consistency_score
  - device_fingerprint_match_score
  - behavioral_profile_distance (from PASSIVE_INTELLIGENCE_AGENT baseline)
  # Legal admissibility features
  - chain_continuity_score
  - consent_capture_present
  - evaluator_verified_flag
  - timestamp_server_verified_flag
  - replay_integrity_seal_present

TRAINING_FREQUENCY:
  - Tamper risk classifier: Weekly — adversarial patterns evolve fast
  - Admissibility scorer: Monthly
  - Anti-cheat behavioral classifier: Weekly
  - Anomaly models: Continuous online learning with weekly checkpoint

DRIFT_DETECTION:
  - Monitor PSI on: behavioral feature distributions, evidence type mix, chain gap frequency
  - Drift threshold: PSI > 0.15 triggers DRIFT_ALERT to OBSERVABILITY_AGENT
  - Accuracy degradation: F1 drop > 10% on tamper classifier triggers MODEL_FREEZE + escalation

VERSION_CONTROL:
  - Every model version immutably linked to agent version at deployment
  - Rollback to n-1 if new model F1 < 95% of previous on held-out evaluation set
  - All model metadata stored append-only in ML_MODEL_REGISTRY
```

### 5.2 AI Layer (Supporting — 22% of intelligence)

```yaml
AI_USAGE_SCOPE:
  - Natural language generation of dispute evidence summaries for DISPUTE_RESOLUTION_AGENT
  - Plain-language explanation of evidence chain integrity status for admin dashboards
  - Regulatory audit package narrative generation (GDPR / DPDP subject access summaries)
  - Anti-cheat violation narrative: translate ML behavioral anomaly vectors into human-readable
    incident reports for GOVERNANCE_AGENT and legal review

PROMPT_GOVERNANCE:
  - All prompts versioned in PROMPT_REGISTRY (semantic version)
  - Prompts are deterministic — no creative interpretation permitted
  - AI output is supplementary narrative only — never primary evidence
  - AI never determines integrity status — that is cryptographic + ML domain exclusively
  - AI timeout: 10 seconds max — fallback to ML-only output with narrative_unavailable flag
  - AI may not access raw payload data — inputs to AI are ML-computed summary vectors only

AI_FORBIDDEN_ACTIONS:
  - AI may NOT determine whether evidence is valid or tampered
  - AI may NOT generate legal conclusions or admissibility decisions
  - AI may NOT access raw session recordings or unanonymized behavioral logs
  - AI may NOT produce parent-facing evidence reports without ML admissibility gate
  - AI may NOT generate forensic conclusions independently — forensics is ML + human domain
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  - 10M users:   8,000 evidence events/second (ingestion) | 3,000 reads/second
  - 100M users:  80,000 evidence events/second (ingestion) | 30,000 reads/second

LATENCY_TARGET:
  - Evidence sealing (write):    P95 < 300ms | P99 < 800ms
  - Integrity verification (read): P95 < 150ms | P99 < 400ms
  - Dispute bundle assembly:     P95 < 2000ms (async job — not real-time path)

MAX_CONCURRENCY:    600 worker pods (Kubernetes HPA — min 15 / max 600 replicas)

QUEUE_STRATEGY:
  - Redis Streams — per-tenant isolated consumer groups for evidence ingestion
  - Priority lanes: ANTI_CHEAT events → P0 lane (immediate processing, no queuing)
                    CERTIFICATION events → P1 lane
                    SESSION_RECORDING events → P2 lane (async, bulk-tolerant)
  - Dead-letter queue: failed events retained 90 days — never purged automatically

STATELESS_EXECUTION:
  - All agent workers stateless — chain state persisted in PostgreSQL (append-only partition)
  - Chain hash computed from database state — never from worker memory

ASYNC_PROCESSING:
  - Evidence sealing: synchronous (returns evidence_id immediately on success)
  - Dispute bundle assembly: async job — status polled by DISPUTE_RESOLUTION_AGENT
  - Regulatory package generation: async job — SLA 24 hours for GDPR/DPDP requests

IDEMPOTENCY:
  - Idempotency key = SHA-256(tenant_id + source_event_id + source_agent + evidence_type)
  - Duplicate events silently deduplicated — log deduplication event — return existing evidence_id

STORAGE_ARCHITECTURE:
  - Evidence metadata: PostgreSQL 15 (append-only partition, write-once enforced at DB role level)
  - Evidence payloads: Object storage (S3-compatible) — encrypted, versioned, lifecycle-managed
  - Chain state: PostgreSQL (dedicated chain_state table — row-level security per tenant)
  - Forensic archives: Cold storage tier — immutable, access-logged, 7-year retention minimum
  - Replay integrity seals: PostgreSQL — hash-linked to media storage object ETag

HORIZONTAL_SCALING:
  - Ingestion workers: scale independently from verification workers
  - Chain computation: single-writer per tenant (serialized) — parallel across tenants
  - Read workers: fully parallel — no chain write contention
```

---

## 7️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every ingestion event validated against tenant JWT before chain insertion
  - PostgreSQL Row-Level Security enforced — no cross-tenant query possible at DB layer
  - Object storage: tenant-prefixed bucket namespaces with IAM policy isolation
  - Chain hash computed within tenant scope — cross-tenant chain linking FORBIDDEN

DOMAIN_ISOLATION:
  - domain_tag validated against user's registered domain scope on every event
  - Evidence tagged Arts is invisible to users in Technology domain at query layer
  - Cross-domain access = SECURITY FAILURE → HALT + LOG_SECURITY_INCIDENT (P0)

ROLE-BASED ACCESS CONTROL ON EVIDENCE:
  - STUDENT:    Read own evidence only (no forensic archives, no anti-cheat details)
  - TRAINER / MENTOR:  Read session evidence for own sessions only
  - EVALUATOR:  Read project / certification evidence for assigned evaluations only
  - INSTITUTE ADMIN:  Read all evidence within own tenant
  - ENTERPRISE ADMIN: Read hiring-related evidence within own tenant
  - RECRUITER:  Read offer letter evidence for own job postings only
  - PARENT:     Read child's academic evidence only (parent_visible=true records)
                Parent may NOT access anti-cheat forensic details (privacy protection for minors)
  - PLATFORM ADMIN:   Read across tenants (anonymized aggregate only — never raw evidence)
  - GOVERNANCE_AGENT: Read all evidence for compliance audit (logged access)
  - DISPUTE_RESOLUTION_AGENT: Read dispute bundle evidence only (scoped access token)
  - AUTOMATION / AI AGENTS:   Read ML-computed summary vectors only — no raw payload access

ENCRYPTION:
  - All evidence payloads: AES-256-GCM encryption at rest
  - Encryption keys: Managed by Hashicorp Vault — key reference stored, never key value
  - Key rotation: 90-day automatic rotation — old keys retained for decryption, never new encryption
  - All inter-agent communication: TLS 1.3 minimum
  - Signed URLs for recording / forensic archive access: max 15-minute validity — no permanent URLs

ANTI-TAMPERING CONTROLS:
  - Chain hash computed server-side — client cannot influence hash
  - Any evidence record modification attempt at DB layer: trigger TAMPER_DETECTED → P0 incident
  - Object storage: Object Lock (WORM mode) — immutable after seal
  - Scheduled chain integrity audits: every 6 hours — full chain re-verification per tenant
  - PostgreSQL audit trigger on evidence tables: any UPDATE or DELETE → immediate security incident

CONSENT ENFORCEMENT:
  - Evidence types requiring consent (DOJO_SESSION_RECORDING, ANTI_CHEAT_BEHAVIORAL_LOG):
    MUST have valid consent_token — reject without it
  - Consent tokens signed by CONSENT_ENGINE_AGENT and verified on ingest
  - Consent withdrawal: does not delete evidence — triggers access_policy update:
    recording marked consent_withdrawn=true, access_restricted to GOVERNANCE_AGENT + LEGAL only

ACCESS_LOG_TRACKING:
  - Every read of evidence record logged: actor_id, actor_role, evidence_id, timestamp_utc, access_reason
  - Access logs immutable — append-only, same chain architecture as evidence
  - Unusual access patterns flagged to OBSERVABILITY_AGENT (ML anomaly model)
```

---

## 8️⃣ AUDIT & TRACEABILITY

Every evidence sealing and verification operation logs the following immutable record:

```json
AUDIT_LOG_ENTRY: {
  "audit_id": "UUID v4",
  "operation_type": "SEAL | VERIFY | ACCESS | DISPUTE_BUNDLE | REGULATORY_EXPORT | TAMPER_DETECTED",
  "timestamp_utc": "ISO8601",
  "actor_id": "string — agent_id or human user_id",
  "actor_role": "string",
  "tenant_id": "string",
  "evidence_id": "string — UUID of evidence record",
  "evidence_type": "string",
  "input_hash": "SHA-256 of raw input payload",
  "output_hash": "SHA-256 of sealed evidence package",
  "chain_sequence_number": "integer",
  "chain_hash_before": "string — chain hash before this operation",
  "chain_hash_after": "string — chain hash after this operation",
  "integrity_status": "SEALED | VERIFIED | TAMPERED | CHAIN_BROKEN",
  "model_version": "string",
  "decision_path": [
    "INPUT_RECEIVED",
    "JWT_VALIDATED",
    "TENANT_ISOLATION_CONFIRMED",
    "DOMAIN_ISOLATION_CONFIRMED",
    "PAYLOAD_HASH_VERIFIED",
    "CONSENT_TOKEN_VALIDATED",
    "SOURCE_AGENT_VERIFIED",
    "CHAIN_HASH_COMPUTED",
    "EVIDENCE_SEALED",
    "CHAIN_UPDATED",
    "ACCESS_POLICY_APPLIED",
    "DOWNSTREAM_EVENTS_EMITTED"
  ],
  "confidence_score": "decimal",
  "anomaly_flags": ["string array"],
  "legal_admissibility_score": "decimal",
  "processing_latency_ms": "integer"
}
```

**Chain Integrity Architecture:**
```
chain_hash[N] = SHA-256(
  chain_hash[N-1]              +   ← previous event hash (chain continuity)
  evidence_id[N]               +   ← current evidence UUID
  payload_hash[N]              +   ← current payload hash
  preservation_timestamp_utc[N]+   ← server-side timestamp
  tenant_id                        ← tenant scope binding
)
```

- Genesis block per tenant: `chain_hash[0]` = SHA-256(tenant_id + tenant_creation_timestamp_utc)
- Any break in chain sequence = immediate CHAIN_BROKEN alert → P0 incident
- Chain re-verification schedule: every 6 hours (full) + on every read (spot check last 10 blocks)

**Immutability Enforcement:**
- Evidence table: PostgreSQL write-once enforced at DB role level (INSERT only — no UPDATE, no DELETE)
- Object storage: S3 Object Lock (WORM) — retention lock minimum 7 years for legal compliance
- Audit log table: same write-once enforcement — separate PostgreSQL partition
- Any attempt to modify, delete, or truncate evidence tables = immediate security incident + human escalation

---

## 9️⃣ FAILURE POLICY

### Failure Scenarios & Responses

| Failure Type | Severity | Response |
|---|---|---|
| Invalid input (schema violation) | P2 | STOP_EXECUTION → LOG_VALIDATION_FAILURE → REJECT_EVENT → RETURN_ERROR_CODE |
| Payload hash mismatch | P1 | STOP_EXECUTION → LOG_HASH_FAILURE → QUARANTINE_EVENT → ALERT_SECURITY |
| JWT invalid or expired | P1 | STOP_EXECUTION → LOG_AUTH_FAILURE → REJECT → ALERT_SECURITY |
| Chain sequence break detected | P0 | STOP_CHAIN_WRITES → LOG_CHAIN_BREAK → HALT_TENANT_CHAIN → ESCALATE immediately |
| Tamper detected (DB audit trigger) | P0 | HALT_ALL_WRITES → LOG_TAMPER_INCIDENT → ESCALATE_TO: SECURITY_AGENT + GOVERNANCE_AGENT + HUMAN |
| Consent token missing (required type) | P1 | STOP_EXECUTION → LOG_CONSENT_FAILURE → REJECT_EVENT → NOTIFY_SOURCE_AGENT |
| Vault unavailable (encryption key) | P1 | STOP_EVIDENCE_SEALING → QUEUE_EVENT → ALERT_OPS → retry on Vault recovery |
| Object storage write failure | P1 | STOP_EXECUTION → LOG_STORAGE_FAILURE → RETRY (3x exponential) → DEAD_LETTER on exhaustion |
| ML model unavailable | P2 | CONTINUE with rule-based admissibility score → SET ml_unavailable=true → LOG |
| AI annotation timeout | P3 | CONTINUE without annotation → SET narrative_unavailable=true → LOG |
| Chain integrity audit failure | P0 | HALT_TENANT_CHAIN → LOG_INTEGRITY_FAILURE → ESCALATE → FREEZE_TENANT_EVIDENCE_ACCESS |
| Cross-tenant query attempt | P0 | IMMEDIATE_HALT → LOG_SECURITY_INCIDENT → ALERT_SECURITY_AGENT + GOVERNANCE_AGENT |

```yaml
RETRY_POLICY:
  - Transient failures (storage, vault, network): Exponential backoff — 1s, 3s, 9s — max 3 retries
  - After 3 retries: DEAD_LETTER_QUEUE — retained 90 days — alert OPS + GOVERNANCE_AGENT
  - P0 failures: NO RETRY — immediate halt + human escalation — no automated recovery attempt

ESCALATION_TARGETS:
  - SECURITY_AGENT:          Tamper events, cross-tenant violations, auth failures
  - GOVERNANCE_AGENT:        Chain breaks, admissibility failures, regulatory triggers
  - OBSERVABILITY_AGENT:     Performance failures, model failures, storage failures
  - HUMAN_SECURITY_TEAM:     All P0 incidents — mandatory human review before chain resume
  - LEGAL_REVIEW_TRIGGER:    Any TAMPER_DETECTED event involving certification or offer evidence

NO_SILENT_FAILURES:   ABSOLUTE RULE — every failure produces an immutable log entry. No exceptions.
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - DOJO_ENGINE_AGENT:                Session events, score submissions, mentor commands
  - SCORING_ENGINE_AGENT:             Raw + merged score records
  - PROJECT_EXECUTION_ENGINE_AGENT:   Milestone completions, evaluator sign-offs
  - CERTIFICATION_ENGINE_AGENT:       Issuance decisions, belt promotion records
  - JOB_PORTAL_ENGINE_AGENT:          Offer letter events, hiring decisions
  - ANTI_CHEAT_ENGINE_AGENT:          Violation events, forensic snapshots
  - IDENTITY_AGENT:                   Verification events, MFA records, device sessions
  - CONSENT_ENGINE_AGENT:             Consent capture events, withdrawal events
  - MEDIA_ENGINE_AGENT:               Recording finalization, replay generation
  - PASSIVE_INTELLIGENCE_AGENT:       Behavioral baseline vectors for forensic enrichment

DOWNSTREAM_AGENTS:
  - GOVERNANCE_AGENT:                 Integrity breach alerts, compliance packages
  - DISPUTE_RESOLUTION_AGENT:         Structured evidence bundles on dispute trigger
  - CERTIFICATION_ENGINE_AGENT:       Evidence chain validation gate before issuance
  - SECURITY_AGENT:                   Tamper alerts, anti-cheat forensic escalations
  - PARENT_VISIBILITY_AGENT:          Read-only verified summaries (parent_visible=true)
  - OBSERVABILITY_AGENT:              Chain health metrics, system telemetry
  - ROYALTY_ENGINE:                   Idea originality proof chains
  - COMPLIANCE_AGENT:                 GDPR / DPDP data subject access packages

EVENT_TRIGGERS_EMITTED:
  - EVIDENCE_SEALED:                  After every successful evidence sealing
  - CHAIN_UPDATED:                    After chain hash computed and stored
  - INTEGRITY_VERIFIED:               After successful on-demand integrity check
  - TAMPER_DETECTED:                  Immediate P0 — any hash mismatch or DB trigger
  - CHAIN_BROKEN:                     Immediate P0 — sequence gap or chain hash failure
  - DISPUTE_BUNDLE_READY:             After async dispute bundle assembly completes
  - CONSENT_PROOF_SEALED:             After consent capture evidence sealed
  - CERTIFICATION_CHAIN_VALIDATED:    Gate event — allows CERTIFICATION_ENGINE to proceed
  - ANTI_CHEAT_ARCHIVE_SEALED:        After forensic archive sealed and access-controlled
  - REGULATORY_PACKAGE_READY:         After GDPR/DPDP audit package assembled
  - REPLAY_INTEGRITY_SEAL_ISSUED:     After replay hash verified and seal issued
  - CHAIN_AUDIT_PASSED:               Scheduled 6-hour chain integrity audit result
  - CHAIN_AUDIT_FAILED:               P0 — scheduled audit detected integrity failure

EVENT_TRIGGERS_CONSUMED:
  - DOJO_SESSION_CLOSED:              Trigger session recording + score evidence sealing
  - SCORE_FINALIZED:                  Trigger final score evidence sealing
  - MENTOR_OVERRIDE_EXECUTED:         Trigger mentor override evidence sealing immediately
  - MILESTONE_SIGNED_OFF:             Trigger milestone evidence sealing
  - CERTIFICATE_ISSUANCE_REQUESTED:   Trigger evidence chain validation gate
  - OFFER_LETTER_ISSUED:              Trigger offer letter evidence sealing
  - ANTI_CHEAT_VIOLATION_DETECTED:    Trigger forensic archive sealing (P0 priority lane)
  - CONSENT_CAPTURED:                 Trigger consent proof sealing
  - IDENTITY_VERIFIED:                Trigger identity verification event sealing
  - DISPUTE_OPENED:                   Trigger dispute bundle assembly job
  - DATA_SUBJECT_REQUEST_RECEIVED:    Trigger regulatory package assembly
  - REPLAY_GENERATED:                 Trigger replay integrity seal computation
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent emits behavioral forensic feature vectors to FEATURE_STORE_AGENT for enrichment of the behavioral baseline used in anti-cheat and identity verification:

```json
EMIT_FEATURE_VECTOR: {
  "user_id": "string",
  "feature_name": "evidence_behavioral_integrity_score",
  "feature_value": "decimal — computed from session behavioral anomaly model",
  "timestamp": "ISO8601 UTC",
  "source_agent": "EVIDENCE_PRESERVATION_AGENT",
  "model_version": "string",
  "confidence": "decimal"
}
```

**Feature vectors emitted:**
- `evidence_chain_integrity_score` — Per-user chain continuity health
- `evidence_behavioral_anomaly_score` — Session behavioral divergence from baseline
- `evidence_consent_compliance_score` — Completeness of consent capture per user
- `evidence_admissibility_score` — Legal admissibility score for most recent evidence chain
- `evidence_tamper_risk_score` — ML-computed tamper risk classification signal

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

When this agent seals idea-related evidence (IDEA_ORIGINALITY_PROOF):

```json
EMIT_IDEA_EVIDENCE: {
  "idea_id": "string",
  "evidence_id": "string — sealed evidence UUID",
  "idea_submission_timestamp_utc": "ISO8601",
  "payload_hash": "string — SHA-256 of idea submission",
  "chain_hash": "string — chain position of this idea evidence",
  "similarity_hash": "string — from IDEA_DNA_AGENT",
  "originality_score": "decimal 0-1",
  "legal_admissibility_score": "decimal",
  "source_agent": "EVIDENCE_PRESERVATION_AGENT"
}
```

**Compatible downstream systems:**
- `IDEA_DNA_AGENT` — timestamped proof of first submission for IP precedence
- `ROYALTY_ENGINE` — evidence chain for royalty dispute resolution
- `COPY_DETECTION_ENGINE` — originality proof for plagiarism defense

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

The EVIDENCE_PRESERVATION_AGENT contributes trust signals to the Growth Engine that govern achievement and rank legitimacy:

```yaml
CERTIFICATION_CHAIN_VALIDATED:
  trigger:   Emitted when evidence chain for a certification is complete and verified
  payload:   { user_id, certificate_id, evidence_completeness_score, chain_integrity_status,
               legal_admissibility_score, domain_tag }
  effect:    CERTIFICATION_ENGINE_AGENT may only issue certificate after receiving this event
             Growth Engine may only award certification XP after chain is validated
             Belt promotion blocked if CERTIFICATION_CHAIN_VALIDATED not received

RANK_TRUST_SCORE_UPDATED:
  trigger:   Emitted after each evidence sealing event for a user
  payload:   { user_id, rank_trust_score, evidence_count_sealed, tamper_events_count,
               behavioral_integrity_score }
  effect:    GROWTH_ENGINE_AGENT receives trust score to weight XP legitimacy
             Users with tamper_events_count > 0 have XP gated pending GOVERNANCE_AGENT review

ACHIEVEMENT_EVIDENCE_LOCKED:
  trigger:   Emitted after each project milestone or certification evidence is sealed
  payload:   { user_id, achievement_type, evidence_id, admissibility_score }
  effect:    Portfolio auto-generation may only include achievements with sealed evidence
             Public leaderboard entry requires ACHIEVEMENT_EVIDENCE_LOCKED event

HARD_RULE:
  No certificate, belt promotion, rank achievement, or portfolio entry may be published
  without a corresponding sealed evidence chain in EVIDENCE_PRESERVATION_AGENT.
  This is enforced by CONTRACT GATE — downstream agents check evidence_id before proceeding.
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  evidence_sealing_success_rate:
    definition:       Percentage of evidence events sealed without failure
    target:           >= 99.8%
    alert_threshold:  < 99.5% sustained over 3 minutes → P1 alert

  chain_integrity_audit_pass_rate:
    definition:       Percentage of 6-hour chain audits passing without anomaly
    target:           100% (any failure = P0)
    alert_threshold:  Any failure = immediate P0 escalation

  sealing_latency_p95:
    definition:       95th percentile evidence sealing latency
    target:           < 300ms
    alert_threshold:  > 500ms for 5 minutes → P2 alert

  verification_latency_p95:
    definition:       95th percentile integrity verification latency
    target:           < 150ms
    alert_threshold:  > 300ms for 5 minutes → P2 alert

  tamper_event_rate:
    definition:       Count of TAMPER_DETECTED events per 24-hour window per tenant
    target:           0 (any tamper event = P0)
    alert_threshold:  Any > 0 → immediate P0 escalation

  chain_break_rate:
    definition:       Count of CHAIN_BROKEN events per 24-hour window
    target:           0
    alert_threshold:  Any > 0 → immediate P0 escalation

  consent_coverage_rate:
    definition:       Percentage of consent-required evidence types with valid consent_token
    target:           100%
    alert_threshold:  < 100% → P1 alert + COMPLIANCE_AGENT notification

  queue_depth_p0_lane:
    definition:       Unprocessed events in anti-cheat P0 priority lane
    target:           0 (real-time processing required)
    alert_threshold:  > 10 unprocessed → immediate auto-scale + P1 alert

  dead_letter_queue_size:
    definition:       Events in dead-letter queue per tenant
    target:           0
    alert_threshold:  > 0 for > 30 minutes → P2 alert + OPS notification

  ml_admissibility_model_drift:
    definition:       PSI on key admissibility features
    target:           PSI < 0.15
    alert_threshold:  PSI > 0.15 → DRIFT_ALERT | PSI > 0.20 → MODEL_FREEZE

  regulatory_package_sla:
    definition:       Time to assemble GDPR/DPDP regulatory package from trigger
    target:           < 24 hours
    alert_threshold:  > 20 hours → P1 alert (buffer before SLA breach)
```

---

## 1️⃣5️⃣ EVIDENCE RETENTION & LIFECYCLE POLICY

```yaml
RETENTION_RULES (LOCKED — Add-Only):

  DOJO_SESSION_RECORDING:
    active_retention:    3 years (hot/warm storage)
    archive_retention:   7 years (cold storage, immutable)
    deletion_trigger:    Never auto-deleted — requires GOVERNANCE_AGENT + LEGAL explicit approval
    gdpr_handling:       On consent withdrawal → access_restricted; evidence preserved for legal

  ANTI_CHEAT_FORENSIC_ARCHIVE:
    active_retention:    5 years
    archive_retention:   10 years
    access_gating:       GOVERNANCE_AGENT + SECURITY_AGENT only after 6 months from event
    deletion_trigger:    Never — forensic evidence is legally permanent

  CERTIFICATION_EVIDENCE_CHAIN:
    active_retention:    Lifetime of certificate + 10 years
    archive_retention:   Permanent
    deletion_trigger:    Never — credentials must be verifiable indefinitely

  OFFER_LETTER_EVIDENCE:
    active_retention:    5 years from offer date
    archive_retention:   10 years
    deletion_trigger:    Never auto-deleted

  CONSENT_CAPTURE:
    active_retention:    Duration of consent + 5 years
    archive_retention:   10 years
    deletion_trigger:    Never — consent records are legal instruments

  PROJECT_MILESTONE_EVIDENCE:
    active_retention:    3 years
    archive_retention:   7 years
    deletion_trigger:    Never auto-deleted

  IDENTITY_VERIFICATION_EVENT:
    active_retention:    5 years
    archive_retention:   10 years
    deletion_trigger:    Never auto-deleted — regulatory requirement

MINIMUM_LEGAL_RETENTION:  7 years for all evidence types (override for types with longer policy)
AUTO_DELETION:            FORBIDDEN — no evidence type may be automatically deleted
GDPR_RIGHT_TO_ERASURE:    Does not apply to evidence preserved for legal proceedings,
                          fraud prevention, or regulatory compliance.
                          Erasure requests routed to COMPLIANCE_AGENT for legal review.
                          Evidence access restricted; record preserved with erasure_requested flag.
```

---

## 1️⃣6️⃣ TRUST FRAMEWORK — ECOSKILLER-SPECIFIC

### 16.1 Trust Pillars Enforced by This Agent

```yaml
TRUST_PILLAR_1 — SCORE INTEGRITY:
  What it protects:   Dojo GD scores, project evaluation scores, certification scores
  Mechanism:          Every score event sealed with chain hash immediately after SCORING_ENGINE emits
  Tamper detection:   Any post-seal modification triggers TAMPER_DETECTED (P0)
  Parent visibility:  Score evidence summaries available via PARENT_VISIBILITY_AGENT
  Legal weight:       Sealed scores are admissible in academic dispute proceedings

TRUST_PILLAR_2 — IDENTITY CONTINUITY:
  What it protects:   User identity consistency across session — anti-impersonation
  Mechanism:          Identity verification events sealed at session start and mid-session
                      Behavioral profile distance monitored via PASSIVE_INTELLIGENCE_AGENT
                      Device fingerprint consistency sealed as evidence
  Tamper detection:   Identity divergence mid-session triggers ANTI_CHEAT_VIOLATION evidence sealing
  Legal weight:       Admissible in fraud and impersonation disputes

TRUST_PILLAR_3 — CONSENT SANCTITY:
  What it protects:   User rights — recording consent, data use consent, TOS acceptance
  Mechanism:          Every consent event sealed before recording begins — no recording without seal
                      Consent withdrawal processed without evidence deletion (access-restricted)
  Jurisdiction:       GDPR pattern + India DPDP pattern — jurisdiction-appropriate consent models
  Legal weight:       Consent proof records are primary legal instruments

TRUST_PILLAR_4 — CERTIFICATION LEGITIMACY:
  What it protects:   Skill certificates, belt promotions, academic credentials
  Mechanism:          Certification chain = training evidence + evaluation evidence + issuance evidence
                      All three must be sealed and verified before certificate issued
                      Certificate contains evidence_chain_hash as verifiable fingerprint
  Tamper detection:   Certificate verification portal checks chain hash against live chain
  Legal weight:       Certificate + chain = verifiable credential for employers and institutions

TRUST_PILLAR_5 — HIRING EVENT INTEGRITY:
  What it protects:   Offer letters, application decisions, recruiter actions
  Mechanism:          Every hiring event sealed with employer + candidate identity binding
                      Offer letter includes preservation_timestamp and evidence_id
  Tamper detection:   Offer letter hash verifiable by candidate at any time
  Legal weight:       Sealed offer letters admissible in employment dispute proceedings

TRUST_PILLAR_6 — ANTI-CHEAT FINALITY:
  What it protects:   Fair evaluation environment — cheating cannot be denied post-session
  Mechanism:          Anti-cheat forensic archives sealed immediately on violation detection
                      Forensic data includes: behavioral logs, session state, ML classification,
                      timestamp chain, identity verification state at time of violation
  Access control:     Student may see violation classification — NOT raw forensic data
  Legal weight:       Forensic archive admissible in academic integrity proceedings
```

### 16.2 Parent Trust Layer

```yaml
PARENT_VISIBILITY_RULES:
  Accessible to parent:
    - Child's sealed score summaries (parent_visible=true records)
    - Certification evidence chain status (verified / pending / failed)
    - Attendance and session participation records
    - Project milestone completion evidence summaries
    - Identity verification status (was child present and verified in session?)

  NOT accessible to parent:
    - Anti-cheat forensic details (privacy protection for minors)
    - Peer evaluator identities
    - Raw session recordings (unless child consents + GOVERNANCE_AGENT approves)
    - Other students' records (no cross-user leakage)

  Access mechanism:
    - Parent must have verified PARENT role in tenant
    - Parent-child relationship must be verified in IDENTITY_AGENT
    - All parent reads logged in access_log with parent_id + child_id
```

---

## 1️⃣7️⃣ ANTI-CHEAT EVIDENCE PROTOCOL

```yaml
ANTI_CHEAT_EVIDENCE_PRIORITY:    P0 — highest priority lane — real-time processing, no queuing

TRIGGER_CONDITIONS:
  Source: ANTI_CHEAT_ENGINE_AGENT emits ANTI_CHEAT_VIOLATION_DETECTED event

SEALING_PROTOCOL:
  Step 1:   Receive violation event — validate hash and source agent identity
  Step 2:   Seal ANTI_CHEAT_BEHAVIORAL_LOG for the full session (not just violation moment)
  Step 3:   Seal forensic snapshot (device state, session state, identity state at violation)
  Step 4:   Seal ML classification output (behavioral model version + confidence + decision path)
  Step 5:   Chain all three seals in sequence — emit ANTI_CHEAT_ARCHIVE_SEALED
  Step 6:   Apply access policy: EVALUATOR + GOVERNANCE_AGENT + SECURITY_AGENT only
  Step 7:   Notify GOVERNANCE_AGENT + SCORING_ENGINE_AGENT (score hold pending review)

FORENSIC_ARCHIVE_CONTENTS:
  - behavioral_log_sealed: Keypress patterns, focus events, tab-switch events, idle periods
  - session_state_sealed:  Screen content snapshots (if proctoring enabled + consent captured)
  - identity_state_sealed: Device fingerprint, IP, biometric signal (if applicable)
  - ml_evidence_sealed:    Model version, feature values, anomaly score, classification
  - timeline_sealed:       Second-by-second event sequence for the violation window

STUDENT_NOTIFICATION_RULE:
  - Student MUST be notified of violation classification within 24 hours
  - Student receives: violation_type, session_id, timestamp — NOT raw forensic data
  - Student may trigger DISPUTE_RESOLUTION — which assembles evidence bundle for review

SCORE_HOLD_RULE:
  - Scores for sessions with unsealed anti-cheat violations are held — not published
  - Score release requires GOVERNANCE_AGENT explicit approval after forensic review
  - No auto-release of held scores
```

---

## 1️⃣8️⃣ DISPUTE RESOLUTION EVIDENCE PROTOCOL

```yaml
DISPUTE_TRIGGER:   DISPUTE_RESOLUTION_AGENT emits DISPUTE_OPENED event

EVIDENCE_BUNDLE_ASSEMBLY:
  Async job — SLA 2 hours from trigger for P1 disputes | 24 hours for P2 disputes

  Contents assembled:
    - All sealed evidence records related to dispute scope (session, score, offer, milestone)
    - Chain integrity verification results for each record
    - Access logs showing who accessed evidence records and when
    - Anti-cheat archives (if applicable, access-controlled)
    - Consent proof records
    - Identity verification records for all parties in dispute scope
    - ML admissibility score for each evidence item
    - AI-generated narrative summary (if admissibility > 0.80)

  Output format:
    - Structured JSON manifest (evidence_ids + integrity_status + admissibility_scores)
    - Encrypted ZIP archive of evidence payloads (key escrowed with GOVERNANCE_AGENT)
    - Chain integrity certificate (signed by EVIDENCE_PRESERVATION_AGENT)
    - Human-readable summary (AI-generated, ML-gated)

  Access control:
    - Bundle accessible to: DISPUTE_RESOLUTION_AGENT, GOVERNANCE_AGENT, LEGAL_REVIEW (human)
    - Parties in dispute: summary only — not raw forensic data unless GOVERNANCE_AGENT approves
    - Time-limited access: bundle access expires 30 days after dispute resolution

DISPUTE_OUTCOME_SEALING:
  - Dispute resolution outcome must be sealed as evidence: DISPUTE_EVIDENCE_BUNDLE type
  - Outcome record chains to original dispute evidence bundle
  - Immutable — dispute outcomes cannot be retroactively modified
```

---

## 1️⃣9️⃣ VERSIONING POLICY

```yaml
VERSION_FORMAT:              Semantic — MAJOR.MINOR.PATCH (e.g., v1.0.0)
CHANGE_POLICY:               ADD-ONLY — No field removal. No schema narrowing. No chain structure change.
CHAIN_STRUCTURE_IMMUTABILITY: The chain hash computation formula may NEVER change after genesis.
                              A new chain formula = a new chain = a governance decision.
BACKWARD_COMPATIBILITY:      All output schemas must remain compatible with n-2 consumer versions
MIGRATION_DOCUMENTATION:     Every version bump requires MIGRATION_NOTE.md in agent registry
ROLLBACK_SAFETY:             Rollback to n-1 executable — chain integrity preserved across versions
EVIDENCE_TYPE_REGISTRY:      Add-only — no type removal. Deprecated types marked but never removed.
RETENTION_POLICY_CHANGES:    May only increase retention duration — never decrease.
                             Any retention change requires GOVERNANCE_AGENT + LEGAL explicit approval.
MODEL_VERSIONING:            Every ML model version immutably linked to agent version
PROMPT_VERSIONING:           All AI prompts versioned in PROMPT_REGISTRY
```

---

## 2️⃣0️⃣ NON-NEGOTIABLE RULES

```
This agent MUST NOT:
  ❌ Delete any evidence record — ever, under any circumstance
  ❌ Modify any sealed evidence payload after sealing
  ❌ Allow client-side timestamp to influence preservation_timestamp_utc
  ❌ Issue signed URLs without access policy validation
  ❌ Allow recording access without valid consent proof or GOVERNANCE_AGENT override
  ❌ Publish chain hash computations to client — chain integrity is server-internal
  ❌ Issue certificates without sealed and verified evidence chain
  ❌ Allow ML model to determine integrity status — that is cryptographic domain only
  ❌ Allow AI to access raw evidence payloads
  ❌ Allow AI to produce admissibility decisions independently
  ❌ Allow cross-tenant evidence queries — hard block at every layer
  ❌ Allow parent access to anti-cheat forensic details
  ❌ Allow score publication without evidence seal
  ❌ Allow any achievement in Growth Engine without sealed evidence chain
  ❌ Auto-delete dead-letter queue events — human review required
  ❌ Process events from source agents not in APPROVED_SOURCE_AGENT_LIST
  ❌ Store encryption keys — key references only, keys in Vault exclusively
  ❌ Allow dispute bundles to be accessed by parties without GOVERNANCE_AGENT scope token
  ❌ Reduce retention duration for any evidence type
  ❌ Override GDPR/DPDP erasure decisions without COMPLIANCE_AGENT + LEGAL explicit gate
  ❌ Execute outside defined scope — any operation not in this document is FORBIDDEN
  ❌ Silent any failure — every failure produces an immutable log entry without exception
```

---

## 2️⃣1️⃣ AGENT SEAL

```
AGENT:              EVIDENCE_PRESERVATION_AGENT
PLATFORM:           ECOSKILLER ANTIGRAVITY
VERSION:            v1.0.0
DOMAIN:             TRUST, IDENTITY & SAFEGUARDS
STATUS:             SEALED + LOCKED + TRUST-CRITICAL
SEALED_BY:          ECOSKILLER ANTIGRAVITY — TRUST, IDENTITY & SAFEGUARDS CORE
MUTATION_POLICY:    ADD-ONLY — no deletions, no overwrites, no silent changes, no schema breaks
COMPLIANCE_MODEL:   Zero-Trust Multi-Tenant | Append-Only Audit | Domain-Isolated |
                    Cryptographic Chain | GDPR-Pattern | India DPDP-Pattern | WORM Storage
EXECUTION_LAW:      Deterministic — Identical input → Identical output
                    No partial output. No silent failure. No creative interpretation.
                    No evidence deletion. No chain modification. No score without evidence.
TRUST_LAW:          No certificate issued without sealed evidence chain.
                    No achievement published without sealed evidence.
                    No replay served without integrity seal.
                    No recording made without sealed consent proof.
                    No anti-cheat outcome finalized without sealed forensic archive.

VIOLATION OF ANY RULE IN THIS DOCUMENT:
→ STOP_EXECUTION
→ LOG_VIOLATION_INCIDENT (immutable)
→ ESCALATE_TO: GOVERNANCE_AGENT + SECURITY_AGENT + HUMAN_SECURITY_TEAM
→ HALT CHAIN WRITES FOR AFFECTED TENANT SCOPE
→ NO OUTPUT EMITTED
→ NO SILENT RECOVERY

This document is the single source of truth for the EVIDENCE_PRESERVATION_AGENT.
All implementations, tests, deployments, and audits must reference this version.
Deviation without versioned amendment = CONTRACT VIOLATION = TRUST FAILURE.
```

---

*Document Version: v1.0.0 | Platform: Ecoskiller Antigravity | Domain: Trust, Identity & Safeguards | Classification: Enterprise Internal — Trust-Critical — Restricted*
