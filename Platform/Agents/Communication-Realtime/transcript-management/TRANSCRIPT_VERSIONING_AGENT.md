# TRANSCRIPT_VERSIONING_AGENT.md

```
╔══════════════════════════════════════════════════════════════════════════════════════════╗
║           ECOSKILLER ANTIGRAVITY — TRANSCRIPT VERSIONING AGENT                         ║
║                        SEALED · LOCKED · GOVERNED · DETERMINISTIC                      ║
║                                                                                          ║
║  Agent ID          : AGTVA-003                                                           ║
║  Mutation Policy   : Add-only via version bump                                           ║
║  Interpretation    : NONE PERMITTED                                                      ║
║  Execution Auth    : Human declaration only                                              ║
║  Classification    : CRITICAL INFRASTRUCTURE — LEGAL INTEGRITY + EVIDENCE CHAIN LAYER  ║
╚══════════════════════════════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — SEAL DECLARATION

This agent specification is **SEALED**.

> No component of this agent may be interpreted, rewritten, abbreviated, inferred,
> or executed in any manner inconsistent with what is explicitly declared below.
> Any ambiguity MUST halt execution and escalate to the Governance Authority.
> No silent assumptions are permitted at any layer, at any time, under any circumstance.

**GOVERNANCE AUTHORITY  :** `ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT`
**COMPLIANCE AUTHORITY  :** `AUDIT_COMPLIANCE_AGENT`
**SECURITY AUTHORITY    :** `ZERO_TRUST_AGENT`
**DATA AUTHORITY        :** `DATA_GOVERNANCE_AGENT`
**LEGAL AUTHORITY       :** `LEGAL_POLICY_AGENT`
**VERSION AUTHORITY     :** `AGENT_VERSION_COMPATIBILITY_AGENT`
**POLICY AUTHORITY      :** `POLICY_VERSION_CONTROL_AGENT`

---

## SECTION 1 — AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME            : TRANSCRIPT_VERSIONING_AGENT
AGENT_ID              : AGTVA-003
SYSTEM_ROLE           : Immutable Transcript Version Controller, Diff Engine,
                        Legal Evidence Chain Manager, Dispute-Safe Amendment Recorder,
                        and Cross-Agent Version Compatibility Enforcer
PRIMARY_DOMAIN        : Session Assessment Integrity / Legal Evidence Management /
                        Compliance Audit Trail / Dispute Resolution Infrastructure /
                        Scoring Pipeline Version Safety
EXECUTION_MODE        : Deterministic + Validated + Append-Only + Cryptographically Sealed
DATA_SCOPE            : Assembled session transcripts (all versions), version metadata,
                        amendment records, diff payloads, provenance chains,
                        dispute resolution attachments, version compatibility matrices
                        — NO raw audio, NO PII beyond session-bound hashed identifiers
TENANT_SCOPE          : Strict Multi-Tenant Isolation
                        (row-level + namespace + vault path isolation by tenant_id)
FAILURE_POLICY        : HALT on ambiguity — STOP → LOG → ESCALATE — NO partial versioning
MUTATION_POLICY       : Add-only — no overwrite, no delete, no retroactive patching
                        of any version record under ANY circumstance
AUDIT_POLICY          : Append-only immutable audit trail per version operation
SECURITY_MODEL        : Zero-trust — every version write cryptographically signed,
                        every version read access-logged, tamper detection on every operation
VERSION               : v1.0.0
STATUS                : ACTIVE
```

---

## SECTION 2 — PURPOSE DECLARATION

### 2.1 The Problem This Agent Solves

The `TRANSCRIPT_AGGREGATION_AGENT` (AGTAA-002) produces a `SESSION_TRANSCRIPT` — a complete, normalized, integrity-certified record of what happened in a session. That transcript is the **single source of truth** for:

- Score calculation (`SCORING_ENGINE`)
- Intelligence dimension scoring (`INTELLIGENCE_SCORING_ML_AGENT`)
- Parent reports (`PARENT_LLM_INSIGHT_NARRATIVE_AGENT`)
- Skill Passport updates (`PASSPORT_AGGREGATION_AGENT`)
- Legal evidence packaging (`LEGAL_DOCUMENT_GENERATION_SERVICE`)
- Dispute resolution (`ADMIN_GOVERNANCE_SERVICE`, `ROYALTY_DISPUTE_RESOLUTION_AGENT`)
- Compliance verification (`AUDIT_COMPLIANCE_AGENT`)

However, the platform operates at **10M–100M users** across **multiple session types** with the following post-assembly realities:

```
REALITY 1 — DISPUTE CORRECTION
  A candidate raises a dispute. The admin confirms a bot detection
  mis-classification. The transcript must be corrected.
  But the original must never be destroyed. Both must coexist.

REALITY 2 — ADMIN AMENDMENT
  A mentor or admin identifies a data entry error in session metadata
  (wrong topic_id, wrong round duration config). The transcript must
  be amended. The original must be preserved with a clear diff record.

REALITY 3 — DOWNSTREAM RE-SCORING
  The SCORING_ENGINE is retrained. New scores are generated from the
  same session events. The transcript must record both score sets
  (original and recomputed) without overwriting either.

REALITY 4 — LEGAL CHALLENGE
  A student challenges their GD exclusion in a campus placement process.
  The institution requires a complete, unbroken, chronologically ordered
  chain of all transcript versions — original + every amendment —
  with cryptographic proof that each version is authentic and unmodified.

REALITY 5 — SCHEMA EVOLUTION
  AGTAA-002 is upgraded to v1.1.0 with new fields. Existing transcripts
  assembled under v1.0.0 must remain readable and valid. Downstream agents
  consuming these transcripts must not break due to schema drift.

REALITY 6 — MULTI-TENANT AUDIT
  A regulatory body requests the complete version history of all transcripts
  for a specific tenant over a 90-day window — with proof that no version
  was deleted, modified, or tampered with after creation.
```

> The `TRANSCRIPT_VERSIONING_AGENT` is the **immutable version control and legal chain layer**
> that governs all lifecycle changes to assembled transcripts — from first commit through
> every amendment, dispute correction, recomputation, and eventual archival.
> It ensures that downstream agents always know which version they are operating on,
> that no version is ever silently superseded, and that the complete chronological
> evidence chain is cryptographically provable at all times.

### 2.2 Core Design Principles

```
PRINCIPLE 1 — IMMUTABILITY
  No version is ever modified after commit. Every change creates a new version.

PRINCIPLE 2 — APPEND-ONLY
  The version log is strictly append-only. No deletions, no truncations,
  no reordering, no retroactive insertions.

PRINCIPLE 3 — CRYPTOGRAPHIC CHAIN
  Each version contains a cryptographic reference to the prior version,
  forming a tamper-evident hash chain (analogous to blockchain block linkage).

PRINCIPLE 4 — DIFF TRANSPARENCY
  Every version transition is accompanied by a structured, machine-readable
  diff record — exactly what changed, why, who authorized it, and when.

PRINCIPLE 5 — CONSUMER SAFETY
  Downstream agents always declare which version they consumed. The versioning
  agent validates compatibility before any downstream consumption is permitted.

PRINCIPLE 6 — DISPUTE NEUTRALITY
  Dispute-driven amendments create a new version with DISPUTE_RESOLUTION status.
  The agent does not adjudicate disputes — it records outcomes with full provenance.

PRINCIPLE 7 — SCHEMA STABILITY
  All versions, regardless of the assembling agent version that produced them,
  must remain readable by all authorized downstream consumers.
```

### 2.3 What This Agent Does NOT Do

```
✗  Does not reassemble transcripts — that is AGTAA-002's responsibility
✗  Does not adjudicate disputes — that is ADMIN_GOVERNANCE_SERVICE's responsibility
✗  Does not recalculate scores — that is SCORING_ENGINE's responsibility
✗  Does not generate legal documents — that is LEGAL_DOCUMENT_GENERATION_SERVICE
✗  Does not validate session events — that is AGTAA-002's responsibility
✗  Does not archive to WORM — that is IMMUTABLE_ARCHIVE_SERVICE's responsibility
   (this agent INSTRUCTS the archive; it does not write to it directly)
```

### 2.4 Upstream Agents (Feed This Agent)

```
TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)     → Initial transcript commit trigger
ADMIN_GOVERNANCE_SERVICE                     → Amendment authorization + dispute outcomes
SCORING_ENGINE                               → Score recomputation version attachments
AUDIT_COMPLIANCE_AGENT                       → Compliance-driven version requests
LEGAL_POLICY_AGENT                           → Legal hold instructions + retention overrides
DATA_GOVERNANCE_AGENT                        → Schema evolution notifications
AGENT_VERSION_COMPATIBILITY_AGENT            → Consumer compatibility declarations
FORENSICS_AGENT                              → Evidence preservation requests
```

### 2.5 Downstream Agents (Depend on This Agent)

```
SCORING_ENGINE                               → Must declare version before scoring
GD_POST_SESSION_ANALYTICS_AGENT             → Must declare version before analysis
INTELLIGENCE_SCORING_ML_AGENT               → Must declare version before intelligence scoring
PARENT_LLM_INSIGHT_NARRATIVE_AGENT          → Must use latest STABLE version for reports
PASSPORT_AGGREGATION_AGENT                  → Must use latest STABLE version for passport update
LEGAL_DOCUMENT_GENERATION_SERVICE           → Receives complete version chain for evidence package
AUDIT_COMPLIANCE_AGENT                      → Receives version audit trail for compliance review
ADMIN_GOVERNANCE_SERVICE                    → Reads version history for dispute resolution
IMMUTABLE_ARCHIVE_SERVICE                   → Receives version commit instructions
OBSERVABILITY_AGENT                         → Receives version operation metrics
AGENT_VERSION_COMPATIBILITY_AGENT           → Receives schema evolution notifications
```

---

## SECTION 3 — INPUT CONTRACT (STRICT)

### 3.1 Trigger Events

This agent responds to exactly **five trigger event types**:

```yaml
TRIGGER_TYPE_1 — INITIAL_COMMIT:
  event       : transcript.assembled
  source      : TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)
  payload     : {transcript_id, session_id, tenant_id, assembled_at_utc,
                 transcript_body_hash, model_version, integrity_certificate}
  action      : Create VERSION_1 (v1) for this transcript

TRIGGER_TYPE_2 — ADMIN_AMENDMENT:
  event       : transcript.amendment.authorized
  source      : ADMIN_GOVERNANCE_SERVICE
  payload     : {transcript_id, amendment_id, authorized_by, authorization_utc,
                 amendment_type, amendment_payload, dispute_reference_id (if applicable)}
  action      : Create VERSION_N+1 with AMENDMENT diff record

TRIGGER_TYPE_3 — SCORE_RECOMPUTATION:
  event       : transcript.score.recomputed
  source      : SCORING_ENGINE
  payload     : {transcript_id, base_version, recomputed_score_payload,
                 scoring_model_version, recomputation_reason}
  action      : Create VERSION_N+1 with SCORE_UPDATE diff record

TRIGGER_TYPE_4 — LEGAL_HOLD:
  event       : transcript.legal_hold.placed
  source      : LEGAL_POLICY_AGENT
  payload     : {transcript_id, hold_id, hold_reason, hold_placed_by,
                 hold_start_utc, hold_end_utc (if known), retention_override_years}
  action      : Apply LEGAL_HOLD flag to all versions — block archival transitions

TRIGGER_TYPE_5 — VERSION_QUERY:
  event       : transcript.version.requested
  source      : Any authorized downstream agent
  payload     : {transcript_id, tenant_id, requested_by_agent,
                 requested_version (specific | LATEST_STABLE | ALL),
                 purpose: SCORING | ANALYTICS | LEGAL | AUDIT | REPORT | DISPUTE}
  action      : Validate authorization → return version record(s) with access log
```

### 3.2 Input Validation Schema (All Triggers)

```json
INPUT_VALIDATION: {
  "required_fields_all_triggers": [
    "transcript_id",
    "tenant_id",
    "trigger_type",
    "trigger_source_agent",
    "trigger_timestamp_utc",
    "request_signature"
  ],
  "trigger_specific_required": {
    "INITIAL_COMMIT"      : ["transcript_body_hash", "model_version", "integrity_certificate"],
    "ADMIN_AMENDMENT"     : ["amendment_id", "authorized_by", "authorization_utc",
                             "amendment_type", "amendment_payload"],
    "SCORE_RECOMPUTATION" : ["base_version", "recomputed_score_payload", "scoring_model_version"],
    "LEGAL_HOLD"          : ["hold_id", "hold_reason", "hold_placed_by", "hold_start_utc"],
    "VERSION_QUERY"       : ["requested_by_agent", "purpose"]
  },
  "validation_rules": [
    "transcript_id must be UUID v4 — reject if malformed",
    "tenant_id must match authenticated JWT claim of trigger_source_agent",
    "trigger_source_agent must be in AUTHORIZED_TRIGGER_SOURCES whitelist",
    "request_signature must be verified (HMAC-SHA256 with agent shared secret from Vault)",
    "trigger_timestamp_utc must be within 60 seconds of server receipt time (replay prevention)",
    "amendment_type must be one of declared AMENDMENT_TYPES (see Section 5.2)",
    "base_version in SCORE_RECOMPUTATION must reference an existing committed version",
    "For VERSION_QUERY: requested_by_agent must be in AUTHORIZED_READERS for declared purpose"
  ],
  "security_checks": [
    "Verify JWT signature of trigger source agent",
    "Verify tenant_id is consistent — no cross-tenant version operations",
    "Verify request_signature (HMAC) — reject replayed or tampered requests",
    "Check transcript_id exists in version registry for all triggers except INITIAL_COMMIT",
    "For INITIAL_COMMIT: verify transcript_id does NOT already exist (idempotency guard)"
  ],
  "domain_checks": [
    "For ADMIN_AMENDMENT: verify amendment_id does not already exist (duplicate prevention)",
    "For SCORE_RECOMPUTATION: verify base_version is a COMMITTED (not PENDING) version",
    "For LEGAL_HOLD: verify hold_id does not conflict with an existing active hold",
    "For VERSION_QUERY: verify purpose is compatible with agent's declared data access scope"
  ]
}
```

### 3.3 Null Tolerance Policy

```
NULL TOLERANCE: ZERO for all required fields
NULL in required field            → REJECT + LOG + ESCALATE
Cross-tenant data in any trigger  → HALT IMMEDIATELY + SECURITY_ALERT
Unrecognized trigger_source_agent → REJECT + LOG + ALERT ZERO_TRUST_AGENT
Expired or replayed request_signature → REJECT + LOG + ALERT ZERO_TRUST_AGENT
```

---

## SECTION 4 — OUTPUT CONTRACT (STRICT)

### 4.1 Version Record Schema (Core Output for Every Version)

```json
VERSION_RECORD: {
  "version_record_id"       : "UUID v4 — unique per version",
  "transcript_id"           : "UUID — references parent transcript",
  "tenant_id"               : "UUID",
  "version_number"          : "integer — monotonically increasing, starts at 1",
  "version_label"           : "string — e.g. v1, v2, v3",
  "version_status"          : "INITIAL | AMENDED | SCORE_UPDATED | DISPUTE_RESOLVED |
                               LEGAL_HOLD | DEPRECATED | ARCHIVED",
  "version_class"           : "STABLE | PROVISIONAL | LEGAL_HOLD | SUPERSEDED",
  "created_at_utc"          : "ISO-8601",
  "created_by_agent"        : "string — agent_id of trigger source",
  "authorized_by"           : "string — human actor_id for amendments | agent_id for auto",
  "trigger_type"            : "INITIAL_COMMIT | ADMIN_AMENDMENT | SCORE_RECOMPUTATION |
                               LEGAL_HOLD | SCHEMA_MIGRATION",
  "trigger_reference_id"    : "UUID — amendment_id, dispute_id, hold_id, or session_id",

  "transcript_snapshot_ref" : "UUID — reference to IMMUTABLE_ARCHIVE_SERVICE object",
  "transcript_body_hash"    : "SHA-256 of transcript body at this version",
  "prior_version_hash"      : "SHA-256 of prior VERSION_RECORD (null for v1)",
  "chain_hash"              : "SHA-256(transcript_body_hash + prior_version_hash + version_number)",

  "diff_record"             : {
    "diff_id"               : "UUID",
    "diff_type"             : "NONE | FIELD_UPDATE | SCORE_UPDATE | STATUS_CHANGE |
                               PARTICIPANT_CORRECTION | BOT_RECLASSIFICATION |
                               GAP_RESOLUTION | METADATA_CORRECTION | LEGAL_ANNOTATION",
    "diff_summary"          : "string — human-readable one-line description",
    "changed_fields"        : ["array of changed field paths — e.g. participant_records[0].bot_classification"],
    "prior_values"          : {},
    "new_values"            : {},
    "diff_payload_hash"     : "SHA-256 of diff payload",
    "diff_generated_by"     : "string — agent_id",
    "diff_authorized_by"    : "string — actor_id (human) or agent_id (auto)"
  },

  "legal_hold_status"       : {
    "is_under_hold"         : "boolean",
    "hold_id"               : "UUID | null",
    "hold_reason"           : "string | null",
    "archival_blocked"      : "boolean"
  },

  "schema_metadata"         : {
    "assembling_agent_version" : "string — AGTAA-002 version that produced this transcript",
    "versioning_agent_version" : "string — AGTVA-003 version that processed this record",
    "schema_version"           : "string — transcript schema version (e.g. TS-SCHEMA-v1.0.0)"
  },

  "audit_reference"         : "UUID — references immutable audit record for this operation",
  "model_version"           : "AGTVA-003-v1.0.0"
}
```

### 4.2 Version Chain Index Schema

One index record per transcript — maintained as a running list of all versions:

```json
VERSION_CHAIN_INDEX: {
  "transcript_id"           : "UUID",
  "tenant_id"               : "UUID",
  "session_id"              : "UUID",
  "session_type"            : "GD | DOJO | INTERVIEW | INTELLIGENCE_TEST | CERTIFICATION",
  "total_versions"          : "integer",
  "latest_version_number"   : "integer",
  "latest_stable_version"   : "integer — last version with class = STABLE",
  "latest_version_hash"     : "SHA-256 — chain_hash of most recent VERSION_RECORD",
  "is_under_legal_hold"     : "boolean",
  "legal_hold_id"           : "UUID | null",
  "chain_integrity_valid"   : "boolean — result of last chain verification",
  "chain_last_verified_utc" : "ISO-8601",
  "version_history"         : [
    {
      "version_number"      : "integer",
      "version_label"       : "string",
      "version_status"      : "string",
      "version_class"       : "string",
      "created_at_utc"      : "ISO-8601",
      "chain_hash"          : "SHA-256",
      "trigger_type"        : "string",
      "trigger_reference_id": "UUID"
    }
  ]
}
```

### 4.3 Version Query Response Schema

```json
VERSION_QUERY_RESPONSE: {
  "query_id"                : "UUID",
  "transcript_id"           : "UUID",
  "tenant_id"               : "UUID",
  "requested_by_agent"      : "string",
  "purpose"                 : "string",
  "served_at_utc"           : "ISO-8601",
  "versions_returned"       : "integer",
  "version_records"         : ["array of VERSION_RECORD objects"],
  "chain_integrity_valid"   : "boolean",
  "access_log_reference"    : "UUID — immutable access log entry",
  "model_version"           : "AGTVA-003-v1.0.0"
}
```

### 4.4 Output Guarantees

```
Every version write produces an immutable VERSION_RECORD
Every version read produces an immutable ACCESS_LOG entry
Every diff is stored as a permanent, machine-readable, structured record
No version is ever deleted, truncated, or superseded in-place
chain_hash forms an unbreakable cryptographic sequence from v1 to vN
All outputs include: model_version, audit_reference, chain_hash
```

---

## SECTION 5 — VERSION LIFECYCLE ENGINE

### 5.1 Version State Machine

```
                    ┌─────────────┐
                    │   PENDING   │ ← version write in progress
                    └──────┬──────┘
                           │ commit success + audit write success
                           ▼
                    ┌─────────────┐
         ┌──────────│  COMMITTED  │──────────┐
         │          └──────┬──────┘          │
         │                 │ new version      │ legal hold placed
         │                 │ created          ▼
         │          ┌──────▼──────┐   ┌──────────────┐
         │          │ SUPERSEDED  │   │  LEGAL_HOLD  │
         │          └─────────────┘   └──────┬───────┘
         │                                   │ hold released
         │                                   ▼
         │                            ┌──────────────┐
         │                            │   COMMITTED  │ (restored)
         │                            └──────────────┘
         │
         │ retention period expired + no legal hold
         ▼
  ┌──────────────┐
  │   ARCHIVED   │ ← WORM archive, access via IMMUTABLE_ARCHIVE_SERVICE only
  └──────────────┘

STATES:
  PENDING    — Write in progress, not yet committed
  COMMITTED  — Cryptographically sealed, immutable, active
  SUPERSEDED — Replaced by a newer version, still readable, not deleted
  LEGAL_HOLD — Cannot transition to ARCHIVED; all reads create access log
  ARCHIVED   — In WORM storage, readable but not modifiable
```

### 5.2 Amendment Type Registry (LOCKED)

All amendments must declare one of the following types. Undeclared amendment types are **rejected**.

```yaml
AMENDMENT_TYPES:

  BOT_RECLASSIFICATION:
    description : Change of bot_classification for one or more participants
                  following admin review of PHONE_BOT_VOICE_DETECTION_AGENT output
    authorized_by: ADMIN_GOVERNANCE_SERVICE + human admin actor_id
    affects      : participant_records[N].bot_classification,
                   participant_records[N].score_eligible,
                   session_summary.bot_confirmed_count,
                   session_summary.session_integrity_class
    downstream_impact : SCORING_ENGINE must recompute affected participants
    downstream_trigger: transcript.score.recomputation_required

  PARTICIPANT_CORRECTION:
    description : Correction of a participant record due to identity binding error,
                  attendance misclassification, or roster error
    authorized_by: ADMIN_GOVERNANCE_SERVICE + human admin actor_id
    affects      : participant_records[N].attendance_class,
                   participant_records[N].join_timestamp_utc,
                   participant_records[N].score_eligible
    downstream_impact: SCORING_ENGINE must recompute if score_eligible changed

  METADATA_CORRECTION:
    description : Correction of session metadata fields
                  (wrong topic_id, wrong round_structure, wrong turn_duration_config)
    authorized_by: ADMIN_GOVERNANCE_SERVICE + human admin actor_id
    affects      : session_metadata fields only
    downstream_impact: ANALYTICS agents must invalidate cached analyses

  SCORE_UPDATE:
    description : Attachment of recomputed scores from SCORING_ENGINE
                  (new model version, corrected feature input, post-dispute rescore)
    authorized_by: SCORING_ENGINE (automatic) — subject to audit
    affects      : turn_records[N].turn_score_raw,
                   session_summary (aggregate score fields)
    downstream_impact: PASSPORT_AGGREGATION_AGENT, RANK_ENGINE must update

  GAP_RESOLUTION:
    description : Resolution of a previously annotated gap
                  (late-arriving source data fills a gap marked in TRANSCRIPT_GAP_REPORT)
    authorized_by: TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002) — automatic
    affects      : turn_records where turn_outcome was GAP_ANNOTATED
    downstream_impact: SCORING_ENGINE may need recomputation

  LEGAL_ANNOTATION:
    description : Addition of a legal reference, hold notice, or compliance annotation
                  to a transcript version — does not change session data
    authorized_by: LEGAL_POLICY_AGENT or AUDIT_COMPLIANCE_AGENT
    affects      : legal_hold_status fields only
    downstream_impact: NONE to scoring — legal metadata only

  SCHEMA_MIGRATION:
    description : Non-semantic field additions due to schema version upgrade
                  (new optional fields added — no existing field changed)
    authorized_by: DATA_GOVERNANCE_AGENT + AGENT_VERSION_COMPATIBILITY_AGENT
    affects      : schema_metadata fields + new optional fields added
    downstream_impact: Consumers must validate against new schema version
```

### 5.3 Version Creation Algorithm (Deterministic)

```
ALGORITHM: CREATE_VERSION(trigger_type, trigger_payload)

STEP 1 — VALIDATE INPUT
  Run full input validation (Section 3.2)
  Fail → REJECT + LOG + ESCALATE

STEP 2 — ACQUIRE IDEMPOTENCY LOCK
  Redis distributed lock: lock_key = "version_lock:{transcript_id}"
  TTL: 30 seconds
  If lock not acquired within 5 seconds → HALT + retry once → then ESCALATE

STEP 3 — LOAD CURRENT CHAIN STATE
  Fetch VERSION_CHAIN_INDEX for transcript_id
  Record latest_version_number (N)
  Record latest_version_hash (prior chain hash)

STEP 4 — COMPUTE VERSION NUMBER
  new_version_number = N + 1
  (For INITIAL_COMMIT: new_version_number = 1, prior_version_hash = NULL)

STEP 5 — GENERATE DIFF RECORD
  For INITIAL_COMMIT: diff_type = NONE
  For ADMIN_AMENDMENT: compute structured diff between prior transcript and amended payload
    - Field-level diff (prior_values vs new_values)
    - Validate amendment_type against AMENDMENT_TYPES registry
    - Verify all changed_fields are within amendment_type's declared affect scope
    - Reject if any changed field is outside declared scope
  For SCORE_RECOMPUTATION: diff_type = SCORE_UPDATE
  For LEGAL_HOLD: diff_type = LEGAL_ANNOTATION

STEP 6 — COMPUTE CHAIN HASH
  chain_hash = SHA-256(
    transcript_body_hash
    + prior_version_hash    (NULL → "0000...0000" for v1)
    + new_version_number    (as string)
    + created_at_utc
  )

STEP 7 — ASSEMBLE VERSION_RECORD
  Populate all VERSION_RECORD fields (Section 4.1)
  Set version_status and version_class per state machine (Section 5.1)

STEP 8 — WRITE VERSION_RECORD (PENDING state)
  Write to PostgreSQL (versioned_transcripts table)
  Tenant-isolated, row-level security enforced

STEP 9 — WRITE AUDIT RECORD (MANDATORY GATE)
  Write immutable audit record (Section 8)
  If audit write fails → HALT + ROLLBACK STEP 8 + ESCALATE
  Audit write must succeed before version is promoted from PENDING

STEP 10 — PROMOTE VERSION TO COMMITTED
  Update version_status = COMMITTED
  Update VERSION_CHAIN_INDEX (atomic update: total_versions, latest hashes)

STEP 11 — PRIOR VERSION STATUS UPDATE
  If new_version_number > 1:
    Update prior version's version_class = SUPERSEDED
    (Prior version remains readable — status change only, data immutable)

STEP 12 — INSTRUCT ARCHIVE SERVICE
  Emit: transcript.version.archive_instruction
  Payload: {version_record_id, transcript_snapshot_ref, tenant_id, retention_policy}
  IMMUTABLE_ARCHIVE_SERVICE handles WORM write asynchronously

STEP 13 — EMIT DOWNSTREAM EVENTS
  Emit relevant next_trigger_events (see Section 11)

STEP 14 — RELEASE IDEMPOTENCY LOCK
  Release Redis lock: "version_lock:{transcript_id}"

STEP 15 — CHAIN INTEGRITY VERIFICATION (POST-COMMIT)
  Asynchronously verify full chain from v1 to vN
  Log result to OBSERVABILITY_AGENT
  Alert if chain_integrity_valid = false
```

---

## SECTION 6 — CRYPTOGRAPHIC CHAIN ENGINE

### 6.1 Chain Architecture

The version chain is a **tamper-evident linked sequence** where each version's `chain_hash` is computed from its own content plus the previous version's `chain_hash`. This makes silent modification of any version detectable by any subsequent chain verification.

```
VERSION 1 (v1):
  transcript_body_hash : SHA-256(transcript_body_v1)
  prior_version_hash   : "0000000000000000000000000000000000000000000000000000000000000000"
  chain_hash_v1        : SHA-256(transcript_body_hash_v1 + "000...0" + "1" + created_at_utc)

VERSION 2 (v2):
  transcript_body_hash : SHA-256(transcript_body_v2)
  prior_version_hash   : chain_hash_v1
  chain_hash_v2        : SHA-256(transcript_body_hash_v2 + chain_hash_v1 + "2" + created_at_utc)

VERSION N (vN):
  transcript_body_hash : SHA-256(transcript_body_vN)
  prior_version_hash   : chain_hash_v(N-1)
  chain_hash_vN        : SHA-256(transcript_body_hash_vN + chain_hash_v(N-1) + "N" + created_at_utc)

TAMPER DETECTION:
  If any VERSION_RECORD is modified after commit:
  → Its chain_hash will no longer match the prior_version_hash of the next version
  → Chain verification fails immediately
  → FORENSICS_AGENT is alerted
```

### 6.2 Chain Verification Protocol

```yaml
CHAIN_VERIFICATION:
  trigger         : Scheduled (every 6 hours via Airflow) +
                    On every VERSION_QUERY request with purpose = LEGAL or AUDIT +
                    On-demand by FORENSICS_AGENT
  algorithm       :
    FOR version in chain (v1 → vN):
      recompute chain_hash from stored fields
      compare with stored chain_hash
      verify prior_version_hash == chain_hash of version N-1
      if mismatch → CHAIN_BROKEN flag → FORENSICS_AGENT alert
  output          : chain_integrity_valid (boolean) per transcript
  storage         : Verification result written to VERSION_CHAIN_INDEX (not to version record)
  on_failure      : HALT all version operations for this transcript
                    ALERT: FORENSICS_AGENT, AUDIT_COMPLIANCE_AGENT, ADMIN_GOVERNANCE_SERVICE
                    LEGAL_HOLD placed automatically
```

---

## SECTION 7 — DIFF ENGINE

### 7.1 Diff Computation Rules

```yaml
DIFF_RULES:

  FIELD_LEVEL_DIFF:
    method  : Deep recursive JSON diff (field path level)
    format  : changed_fields = ["path.to.field"],
              prior_values   = {"path.to.field": old_value},
              new_values     = {"path.to.field": new_value}
    exclude : Fields not in amendment_type's declared affect scope
              (attempting to change excluded fields → REJECT)

  ARRAY_ELEMENT_DIFF:
    method  : Diff by indexed position AND by element ID (participant_id or turn_id)
    format  : changed_fields = ["participant_records[participant_id=UUID].bot_classification"]
    purpose : Avoids ambiguity when array order changes

  HASH_DIFF:
    Every diff_record has a diff_payload_hash (SHA-256 of the diff payload)
    This prevents retroactive diff record modification

  NULL_DIFF:
    If amendment produces no actual change to any field:
    → REJECT amendment (no-op amendments are not permitted)
    → LOG: AMENDMENT_REJECTED_NO_DIFF
    → Escalate to amendment source for review
```

### 7.2 Diff Scope Enforcement

```yaml
SCOPE_ENFORCEMENT:
  Each amendment_type has a declared affect scope (Section 5.2)
  The diff engine enforces this scope strictly:

  IF diff contains changed_fields outside declared scope:
    → REJECT VERSION_CREATION
    → LOG: SCOPE_VIOLATION with exact out-of-scope field paths
    → ESCALATE to ADMIN_GOVERNANCE_SERVICE + AUDIT_COMPLIANCE_AGENT

  This prevents:
    - Bot reclassification silently altering session metadata
    - Score updates silently altering participant records
    - Legal annotations silently altering session events
```

---

## SECTION 8 — ML / AI LOGIC LAYER

### 8.1 Architecture Overview

```
AI MUST ASSIST ML — AI MUST NOT REPLACE ML
All version lifecycle decisions are rule-primary.
ML layer handles anomaly detection and amendment pattern scoring.
AI layer is used ONLY for narrative generation in audit and dispute contexts.
```

### 8.2 ML Layer — Amendment Anomaly Detector

Detects suspicious patterns in amendment requests that could indicate fraud or governance bypass.

```yaml
MODEL_TYPE          : Isolation Forest (unsupervised anomaly detection)
FEATURES_USED       :
  - amendment_frequency_per_transcript   (# amendments on same transcript_id)
  - amendment_frequency_per_actor        (# amendments by same authorized_by actor)
  - amendment_time_delta_hours           (time since initial commit)
  - amendment_type_entropy               (variety of amendment types on same transcript)
  - affected_field_count                 (breadth of change in single amendment)
  - bot_reclassification_rate_per_session (tenant-level)
  - score_update_frequency_per_session   (tenant-level)
  - prior_anomaly_score_actor            (actor's historical anomaly score)

OUTPUT              : anomaly_score (0.0–1.0), anomaly_class (NORMAL | SUSPICIOUS | ALERT)
THRESHOLD:
  ALERT             : anomaly_score >= 0.80 → FLAG + ESCALATE to FORENSICS_AGENT
  SUSPICIOUS        : anomaly_score 0.55–0.79 → FLAG + LOG
  NORMAL            : anomaly_score < 0.55 → PROCEED

TRAINING_FREQUENCY  : Monthly retrain on labeled amendment history
DRIFT_DETECTION     : Monitor feature distribution weekly; alert if KS stat > 0.08
VERSION_CONTROL     : Model version stored in every version audit record
```

### 8.3 ML Layer — Version Consumer Compatibility Checker

Before any downstream agent consumes a version, this model predicts compatibility risk.

```yaml
MODEL_TYPE          : Rule-first compatibility matrix + Gradient Boosted classifier
RULE_LAYER:
  COMPATIBLE if:
    consumer_declared_schema_version is within transcript schema_version range
    AND consumer_agent_version is in AGENT_VERSION_COMPATIBILITY_AGENT's approved list
  INCOMPATIBLE if:
    consumer_declared_schema_version < minimum supported transcript schema version
    OR consumer_agent_version is deprecated

ML_LAYER (ambiguous cases):
  Features: schema_version delta, field set diff, agent version history
  Output: compatibility_confidence (0.0–1.0)
  Threshold: < 0.85 → block consumption + alert DATA_GOVERNANCE_AGENT

TRAINING_FREQUENCY  : On every schema evolution event
```

### 8.4 AI Assist Layer (Scoped — 20%)

```yaml
AI_USAGE_SCOPE:
  - Generate human-readable diff summary narrative for ADMIN_GOVERNANCE_SERVICE
  - Generate version history report for LEGAL_DOCUMENT_GENERATION_SERVICE
  - Summarize amendment trail for dispute resolution packages
  - Generate anomaly narrative for FORENSICS_AGENT review queue

PROMPT_GOVERNANCE:
  - All prompts versioned (AGTVA-AI-PROMPT-vX.Y.Z)
  - AI output is advisory and narrative only
  - AI output NEVER modifies any VERSION_RECORD field
  - AI output tagged with ai_generated: true in all outputs
  - Prompt version stored in audit_reference

AI MUST NOT:
  - Authorize or reject amendments
  - Compute diff records
  - Verify chain integrity
  - Access cross-tenant version data
  - Generate participant identity content using PII
  - Autonomously place or release legal holds
```

---

## SECTION 9 — CONSUMER VERSION PROTOCOL

### 9.1 Consumer Declaration Requirement

Every downstream agent that consumes a transcript version **MUST** declare:

```json
CONSUMER_DECLARATION: {
  "consumer_agent_id"           : "string",
  "consumer_agent_version"      : "string",
  "transcript_id"               : "UUID",
  "tenant_id"                   : "UUID",
  "declared_schema_version"     : "string — schema version consumer was built against",
  "requested_version"           : "integer | LATEST_STABLE",
  "purpose"                     : "SCORING | ANALYTICS | LEGAL | AUDIT | REPORT | DISPUTE",
  "consumption_timestamp_utc"   : "ISO-8601"
}
```

### 9.2 Version Selection Rules (by Purpose)

```yaml
PURPOSE_VERSION_RULES:

  SCORING:
    allowed_versions  : LATEST_STABLE only
    blocked_on        : LEGAL_HOLD (scoring blocked until hold assessed)
    notes             : SCORING_ENGINE must re-declare if version advances during scoring

  ANALYTICS:
    allowed_versions  : LATEST_STABLE or specific version (for historical analysis)
    blocked_on        : NONE
    notes             : Must record version_number used in every analytics output

  LEGAL:
    allowed_versions  : ALL versions (full chain)
    blocked_on        : NONE (legal access cannot be blocked by hold)
    notes             : Full VERSION_CHAIN_INDEX + all VERSION_RECORDs returned
                        Chain integrity verification run at time of legal query

  AUDIT:
    allowed_versions  : ALL versions
    blocked_on        : NONE
    notes             : Chain integrity verification run + result included in response

  REPORT (Parent/Student-facing):
    allowed_versions  : LATEST_STABLE only
    blocked_on        : LEGAL_HOLD (report blocked — return HOLD_NOTICE to requester)
    notes             : PII protection enforced — display_name_hash only in response

  DISPUTE:
    allowed_versions  : ALL versions
    blocked_on        : NONE
    notes             : Includes diff records for all versions
                        Diff narrative generated by AI layer
```

### 9.3 Consumer Access Log

Every version query — regardless of purpose — creates an immutable access log entry:

```json
ACCESS_LOG_ENTRY: {
  "access_log_id"         : "UUID",
  "transcript_id"         : "UUID",
  "tenant_id"             : "UUID",
  "accessed_by_agent"     : "string",
  "access_timestamp_utc"  : "ISO-8601",
  "purpose"               : "string",
  "versions_served"       : ["array of version_numbers"],
  "chain_integrity_valid" : "boolean",
  "legal_hold_active"     : "boolean"
}
```

---

## SECTION 10 — SCALABILITY DESIGN

```yaml
EXPECTED_WRITE_RPS    : 2,000 version writes/hour (aligned to session completion rate)
EXPECTED_READ_RPS     : 15,000 version reads/hour (scoring + analytics + reporting)
LATENCY_TARGET        :
  INITIAL_COMMIT      : < 500ms end-to-end (p95)
  ADMIN_AMENDMENT     : < 1000ms end-to-end (p95)
  VERSION_QUERY       : < 200ms (p95) for latest stable; < 1000ms for full chain
MAX_CONCURRENCY       : 10,000 parallel version operations (tenant-partitioned)
QUEUE_STRATEGY        :
  WRITES              : Kafka topic — transcript.versioning.write.queue
                        (partitioned by tenant_id, keyed by transcript_id — serial per transcript)
  READS               : Direct API (no queue — synchronous with circuit breaker)

HORIZONTAL_SCALING    : Stateless version workers — Kubernetes HPA on queue depth
STATELESS_EXECUTION   : All state in PostgreSQL + Redis — no local state in workers
IDEMPOTENT_WRITES     : Redis distributed lock per transcript_id ensures exactly-once version creation
SERIAL_PER_TRANSCRIPT : All version writes for a single transcript_id are serialized
                        (Kafka partition key = transcript_id ensures ordering)

CACHING:
  - VERSION_CHAIN_INDEX cached in Redis (TTL: 30 min, invalidated on every write)
  - LATEST_STABLE version number cached in Redis (TTL: 10 min)
  - Full VERSION_RECORD NOT cached — always served from PostgreSQL for integrity
  - Chain integrity result cached in Redis (TTL: 6 hours, invalidated on write)

BATCH_OPERATIONS:
  - Chain integrity verification: Airflow job, every 6 hours, tenant-partitioned batches
  - Archival transition job: Airflow job, daily, identifies COMMITTED versions past retention
  - Consumer compatibility scan: Airflow job, on schema evolution events
```

---

## SECTION 11 — SECURITY ENFORCEMENT

```yaml
ZERO_TRUST_POLICY:
  - Every write validated against: transcript_id + tenant_id + caller_identity + HMAC signature
  - Every read creates an immutable access log
  - No implicit trust from prior version operations
  - Request replay window: 60 seconds (timestamp + HMAC prevents replay attacks)

TENANT_ISOLATION:
  - All version records parameterized with tenant_id (PostgreSQL row-level security)
  - Kafka consumer groups are tenant-namespaced
  - Redis keys prefixed with tenant_id
  - Vault secret paths are tenant-scoped (HMAC keys per tenant per agent pair)
  - Zero cross-tenant version operations under any circumstance
  - Cross-tenant detection → HALT + SECURITY_ALERT + FORENSICS_AGENT

AMENDMENT_AUTHORIZATION:
  - ADMIN_AMENDMENT requires dual authorization:
      1. authorized_by = human admin actor_id (verified against Auth Service)
      2. trigger_source = ADMIN_GOVERNANCE_SERVICE (verified by JWT)
  - No self-authorization: an agent cannot authorize its own amendments
  - SCORE_RECOMPUTATION auto-authorized by SCORING_ENGINE (logged but no human needed)
  - LEGAL_HOLD requires LEGAL_POLICY_AGENT or AUDIT_COMPLIANCE_AGENT

ENCRYPTION:
  - All version records encrypted at rest (AES-256, keys in HashiCorp Vault)
  - All API communication TLS 1.3
  - chain_hash and transcript_body_hash stored as hex strings (collision-resistant)
  - HMAC keys rotated quarterly via Vault dynamic secrets

WRITE_PROTECTION:
  - VERSION_RECORD rows are INSERT-only in PostgreSQL (no UPDATE, no DELETE)
  - Enforced via PostgreSQL row-level security + trigger-based enforcement
  - Any attempt to UPDATE or DELETE a VERSION_RECORD → FORENSICS_AGENT alert

ACCESS_CONTROL:
  WRITE (version creation)   : TRANSCRIPT_AGGREGATION_AGENT, ADMIN_GOVERNANCE_SERVICE,
                                SCORING_ENGINE, LEGAL_POLICY_AGENT, DATA_GOVERNANCE_AGENT
  READ (any version)         : SCORING_ENGINE, GD_POST_SESSION_ANALYTICS_AGENT,
                                INTELLIGENCE_SCORING_ML_AGENT, PARENT_LLM_INSIGHT_NARRATIVE_AGENT,
                                PASSPORT_AGGREGATION_AGENT, LEGAL_DOCUMENT_GENERATION_SERVICE,
                                AUDIT_COMPLIANCE_AGENT, ADMIN_GOVERNANCE_SERVICE, FORENSICS_AGENT
  CHAIN VERIFY               : FORENSICS_AGENT, AUDIT_COMPLIANCE_AGENT (only)
  LEGAL HOLD PLACEMENT       : LEGAL_POLICY_AGENT, AUDIT_COMPLIANCE_AGENT (only)
  LEGAL HOLD RELEASE         : LEGAL_POLICY_AGENT (only — not AUDIT_COMPLIANCE_AGENT)
```

---

## SECTION 12 — AUDIT & TRACEABILITY

Every version operation writes the following immutable audit record:

```json
AUDIT_RECORD: {
  "timestamp_utc"              : "ISO-8601",
  "actor_id"                   : "TRANSCRIPT_VERSIONING_AGENT",
  "agent_version"              : "AGTVA-003-v1.0.0",
  "operation_type"             : "VERSION_CREATE | VERSION_QUERY | CHAIN_VERIFY |
                                  LEGAL_HOLD_PLACE | LEGAL_HOLD_RELEASE | SCOPE_VIOLATION |
                                  COMPATIBILITY_CHECK | AMENDMENT_REJECTED",
  "transcript_id"              : "UUID",
  "tenant_id"                  : "UUID",
  "version_number"             : "integer",
  "version_record_id"          : "UUID",
  "trigger_type"               : "string",
  "trigger_source_agent"       : "string",
  "authorized_by"              : "string",
  "input_hash"                 : "SHA-256 of incoming trigger payload",
  "output_hash"                : "SHA-256 of produced VERSION_RECORD",
  "chain_hash_at_operation"    : "SHA-256 — chain hash of produced or queried version",
  "diff_type"                  : "string | null",
  "amendment_anomaly_score"    : "float | null",
  "amendment_anomaly_class"    : "string | null",
  "chain_integrity_valid"      : "boolean",
  "legal_hold_active"          : "boolean",
  "consumer_agent"             : "string | null (for VERSION_QUERY)",
  "consumer_declared_schema"   : "string | null",
  "compatibility_valid"        : "boolean | null",
  "ai_assist_used"             : "boolean",
  "ai_prompt_version"          : "string | null",
  "operation_duration_ms"      : "integer",
  "anomaly_flags"              : ["array"],
  "error_code"                 : "string | null"
}
```

**Audit store**: `IMMUTABLE_ARCHIVE_SERVICE` — append-only, WORM-style
**Retention**: 7 years minimum; 15 years for LEGAL_HOLD transcripts
**Tamper detection**: Every audit record HMAC-signed; verified on read

---

## SECTION 13 — FAILURE POLICY

```yaml
INVALID_INPUT (validation failure):
  action      : REJECT + LOG
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT
  escalate_to : trigger_source_agent (notify rejection reason)
  retry       : NO — fix input, resubmit

IDEMPOTENCY_LOCK_TIMEOUT (lock not acquired in 5s):
  action      : Single retry after 2 seconds
  stop_exec   : YES after retry fails
  log_to      : OBSERVABILITY_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE (if retry also fails)
  retry       : Once only

AUDIT_WRITE_FAILURE:
  action      : ROLLBACK version write (revert PENDING record)
  stop_exec   : YES — version not committed without audit record
  log_to      : OBSERVABILITY_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : AUDIT_COMPLIANCE_AGENT
  retry       : 3 retries with exponential backoff; then HALT + human escalation

SCOPE_VIOLATION_IN_AMENDMENT:
  action      : REJECT amendment, do not create version
  stop_exec   : YES
  log_to      : AUDIT_COMPLIANCE_AGENT (SCOPE_VIOLATION record)
  escalate_to : ADMIN_GOVERNANCE_SERVICE + trigger_source_agent
  retry       : Amendment must be corrected and resubmitted

CHAIN_INTEGRITY_FAILURE (detected during verification):
  action      : HALT all version operations for this transcript
  stop_exec   : YES (all writes and reads for this transcript_id blocked)
  log_to      : FORENSICS_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE + LEGAL_POLICY_AGENT
  automatic   : LEGAL_HOLD placed immediately
  retry       : PROHIBITED until forensic review complete

CROSS_TENANT_DETECTION:
  action      : HALT IMMEDIATELY + SECURITY_ALERT
  stop_exec   : YES
  log_to      : ZERO_TRUST_AGENT + FORENSICS_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE + DATA_GOVERNANCE_AGENT
  retry       : PROHIBITED

CONSUMER_INCOMPATIBILITY_DETECTED:
  action      : BLOCK consumption + notify consumer
  stop_exec   : YES (for this consumer request)
  log_to      : OBSERVABILITY_AGENT + DATA_GOVERNANCE_AGENT
  escalate_to : AGENT_VERSION_COMPATIBILITY_AGENT
  retry       : After consumer upgrades to compatible version

AMENDMENT_ANOMALY_ALERT (ML anomaly score >= 0.80):
  action      : FLAG amendment + LOG + ALERT — but do NOT auto-reject
  stop_exec   : NO (amendment proceeds with SUSPICIOUS flag)
  log_to      : FORENSICS_AGENT + AUDIT_COMPLIANCE_AGENT
  escalate_to : ADMIN_GOVERNANCE_SERVICE (human review required within 24h)
  note        : Human review outcome determines if version is CONFIRMED or REVERTED via new amendment

NO_SILENT_FAILURES: ENFORCED — every failure path produces a structured log record
```

---

## SECTION 14 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - TRANSCRIPT_AGGREGATION_AGENT     : initial commit trigger
  - ADMIN_GOVERNANCE_SERVICE         : amendment authorization + dispute outcomes
  - SCORING_ENGINE                   : score recomputation version attachments
  - LEGAL_POLICY_AGENT               : legal hold instructions
  - AUDIT_COMPLIANCE_AGENT           : compliance-driven version requests
  - DATA_GOVERNANCE_AGENT            : schema evolution notifications
  - AGENT_VERSION_COMPATIBILITY_AGENT: consumer compatibility declarations
  - FORENSICS_AGENT                  : evidence preservation requests

DOWNSTREAM_AGENTS:
  - SCORING_ENGINE                   : version-aware transcript consumption
  - GD_POST_SESSION_ANALYTICS_AGENT  : version-aware analysis
  - INTELLIGENCE_SCORING_ML_AGENT    : version-aware intelligence scoring
  - PARENT_LLM_INSIGHT_NARRATIVE_AGENT: latest stable version consumption
  - PASSPORT_AGGREGATION_AGENT       : latest stable version for passport update
  - LEGAL_DOCUMENT_GENERATION_SERVICE: full version chain for evidence packages
  - AUDIT_COMPLIANCE_AGENT           : version audit trail consumer
  - ADMIN_GOVERNANCE_SERVICE         : dispute resolution version history
  - IMMUTABLE_ARCHIVE_SERVICE        : receives version archive instructions
  - OBSERVABILITY_AGENT              : receives version operation metrics
  - AGENT_VERSION_COMPATIBILITY_AGENT: receives schema evolution notifications

EVENT_TRIGGERS:
  CONSUMED:
    - transcript.assembled                      : → INITIAL_COMMIT
    - transcript.amendment.authorized           : → ADMIN_AMENDMENT
    - transcript.score.recomputed               : → SCORE_RECOMPUTATION
    - transcript.legal_hold.placed              : → LEGAL_HOLD
    - transcript.version.requested              : → VERSION_QUERY

  EMITTED:
    - transcript.version.committed              : consumed by SCORING, ANALYTICS, GOVERNANCE
    - transcript.version.chain_verified         : consumed by AUDIT_COMPLIANCE_AGENT
    - transcript.version.chain_broken           : consumed by FORENSICS_AGENT, ADMIN, LEGAL
    - transcript.version.legal_hold_placed      : consumed by IMMUTABLE_ARCHIVE_SERVICE
    - transcript.version.consumer_incompatible  : consumed by DATA_GOVERNANCE_AGENT
    - transcript.version.amendment_anomaly      : consumed by FORENSICS_AGENT
    - transcript.version.archive_instruction    : consumed by IMMUTABLE_ARCHIVE_SERVICE
    - security.cross_tenant_violation           : consumed by ZERO_TRUST_AGENT
    - transcript.score.recomputation_required   : consumed by SCORING_ENGINE
                                                  (after BOT_RECLASSIFICATION amendment)
```

---

## SECTION 15 — PASSIVE INTELLIGENCE COMPATIBILITY

```json
EMIT_FEATURE_VECTOR (per version operation): {
  "user_id"          : "SYSTEM (no user — system-level feature)",
  "session_id"       : "UUID",
  "feature_name"     : "transcript_version_health",
  "feature_value"    : 1.0,
  "feature_set"      : {
    "version_count_for_transcript"    : 3,
    "amendment_count"                 : 2,
    "bot_reclassification_count"      : 1,
    "chain_integrity_valid"           : true,
    "legal_hold_active"               : false,
    "amendment_anomaly_max_score"     : 0.22,
    "time_to_stable_version_hours"    : 0.5
  },
  "timestamp"        : "ISO-8601",
  "source_agent"     : "TRANSCRIPT_VERSIONING_AGENT"
}
```

These vectors feed:
- `BEHAVIOR_ANALYTICS_AGENT` — platform integrity health monitoring
- `FRAUD_DETECTION_ENGINE` — systemic amendment abuse patterns
- `MODEL_RISK_AGENT` — ML model quality via score recomputation frequency analysis

---

## SECTION 16 — SCHEMA EVOLUTION PROTOCOL

When `TRANSCRIPT_AGGREGATION_AGENT` (AGTAA-002) is upgraded and produces transcripts with a new schema version, this agent must:

```yaml
SCHEMA_EVOLUTION_STEPS:

  STEP 1 — RECEIVE NOTIFICATION:
    Source: DATA_GOVERNANCE_AGENT
    Event : transcript.schema.evolved
    Payload: {old_schema_version, new_schema_version, changed_fields, backward_compatible}

  STEP 2 — COMPATIBILITY ASSESSMENT:
    If backward_compatible = true:
      → Existing transcripts remain valid at their schema_version
      → New transcripts tagged with new_schema_version
      → No migration required
    If backward_compatible = false:
      → HALT all new version commits until migration plan is confirmed
      → Escalate to DATA_GOVERNANCE_AGENT + AGENT_VERSION_COMPATIBILITY_AGENT

  STEP 3 — CONSUMER NOTIFICATION:
    Emit: transcript.schema.evolution_notice
    Consumers must update CONSUMER_DECLARATION.declared_schema_version
    Old consumers blocked from consuming new-schema transcripts until updated

  STEP 4 — SCHEMA_MIGRATION VERSION:
    For each existing transcript that requires migration:
      Create new VERSION_RECORD with trigger_type = SCHEMA_MIGRATION
      diff_type = SCHEMA_MIGRATION
      diff shows: new optional fields added, no existing fields changed
    This creates a traceable migration audit trail

  STEP 5 — COMPATIBILITY MATRIX UPDATE:
    Update schema_version compatibility matrix in AGENT_VERSION_COMPATIBILITY_AGENT
    Publish: minimum_consumer_version per new schema
```

---

## SECTION 17 — LEGAL HOLD MANAGEMENT

```yaml
LEGAL_HOLD_RULES:

  PLACEMENT:
    Authorized by: LEGAL_POLICY_AGENT or AUDIT_COMPLIANCE_AGENT only
    Effect:
      - ALL versions of this transcript marked legal_hold_status.is_under_hold = true
      - Archival transition BLOCKED (transcript stays in hot storage)
      - All reads of any version create an access log entry (regardless of purpose)
      - Retention period extended to hold_end_utc or 15 years (whichever is greater)
    Notification: IMMUTABLE_ARCHIVE_SERVICE, ADMIN_GOVERNANCE_SERVICE, FORENSICS_AGENT

  RELEASE:
    Authorized by: LEGAL_POLICY_AGENT ONLY
    Effect:
      - All versions of this transcript marked legal_hold_status.is_under_hold = false
      - Archival transitions resume per standard retention policy
      - Access logging continues at standard rate
    Notification: IMMUTABLE_ARCHIVE_SERVICE, ADMIN_GOVERNANCE_SERVICE

  CONFLICT RESOLUTION:
    If multiple holds placed:
      ALL holds must be released before archival is permitted
      Hold release requires explicit hold_id — releases are not blanket

  SCORING DURING HOLD:
    Scoring is NOT blocked by legal hold unless ADMIN_GOVERNANCE_SERVICE explicitly orders it
    All scoring operations during a hold period are flagged in audit records

  REPORT GENERATION DURING HOLD:
    PARENT_LLM_INSIGHT_NARRATIVE_AGENT is blocked from generating reports
    Returns: LEGAL_HOLD_NOTICE to requesting consumer
```

---

## SECTION 18 — PERFORMANCE MONITORING

```yaml
METRICS (via Prometheus → Grafana):

  version_write_success_rate_pct      : % of version writes that commit successfully
  version_write_latency_p50_ms        : Median write latency
  version_write_latency_p95_ms        : 95th percentile (target < 500ms for INITIAL_COMMIT)
  version_read_latency_p95_ms         : 95th percentile (target < 200ms)
  chain_integrity_failure_count       : Must be 0 — immediate alert if > 0
  amendment_anomaly_alert_count       : Amendments flagged ALERT by ML model
  amendment_rejection_count           : Amendments rejected (scope violation + no-diff)
  legal_hold_active_count             : Total transcripts under legal hold
  consumer_incompatibility_count      : Blocked consumptions due to schema mismatch
  audit_write_failure_count           : Must be 0 — immediate alert if > 0
  cross_tenant_violation_count        : Must be 0 — critical alert if > 0
  avg_versions_per_transcript         : Healthy range: 1.0–1.5; alert if > 3.0
  idempotency_lock_timeout_rate_pct   : Rate of lock timeouts (target < 0.1%)

DASHBOARDS (Grafana):
  - Version Operation Pipeline Health (per tenant, per day)
  - Chain Integrity Status Map (per transcript cluster)
  - Amendment Type Distribution (by type, by tenant)
  - Legal Hold Timeline (active holds over time)
  - Consumer Version Compatibility Matrix (consumer vs schema version)
  - Amendment Anomaly Score Distribution
  - Schema Evolution Impact (consumer update lag)

ALERTS:
  - chain_integrity_failure_count > 0           → CRITICAL PAGE + auto LEGAL_HOLD
  - audit_write_failure_count > 0               → PAGE IMMEDIATELY
  - cross_tenant_violation_count > 0            → CRITICAL PAGE + HALT operations
  - avg_versions_per_transcript > 5.0 (1h)      → ALERT ADMIN_GOVERNANCE_SERVICE
  - amendment_anomaly_alert_count spike (>10/h) → ALERT FORENSICS_AGENT
  - legal_hold_active_count growth > 20% (24h)  → ALERT LEGAL_POLICY_AGENT
  - consumer_incompatibility_count > 0          → ALERT DATA_GOVERNANCE_AGENT
```

---

## SECTION 19 — VERSIONING POLICY (SELF-REFERENTIAL)

```yaml
THIS_AGENT_VERSION_FORMAT   : AGTVA-003-vMAJOR.MINOR.PATCH
MUTATION_POLICY             : Add-only — no field removal from any output schema
BACKWARD_COMPAT             : All prior VERSION_RECORD schemas must remain readable
ROLLBACK_SAFETY             : Previous agent version retained 90 days post-rollout
MIGRATION_DOC               : Required for every MINOR or MAJOR version bump
CHANGELOG                   : Append-only CHANGELOG.md co-versioned with this spec
VERSION_CHAIN_FORMAT        : AGTVA-003-CHAIN-vX — the chain hash algorithm is versioned
AI_PROMPT_VERSION           : AGTVA-AI-PROMPT-vX.Y.Z (versioned separately)
SCHEMA_REGISTRY             : All output schemas registered in
                              ECOSKILLER_ANTIGRAVITY_SCHEMA_COMPATIBILITY_AGENT
SELF_VERSION_IN_EVERY_OUTPUT: model_version field mandatory in every VERSION_RECORD
                              and AUDIT_RECORD
```

---

## SECTION 20 — NON-NEGOTIABLE RULES

This agent **MUST NOT**:

```
✗  Delete, update, or truncate any VERSION_RECORD after commit
✗  Allow any amendment that changes fields outside the amendment_type's declared scope
✗  Create a version without a corresponding committed audit record
✗  Release any version to a consumer without verifying consumer authorization
✗  Serve a transcript version without creating an access log entry
✗  Allow a cross-tenant version operation under any circumstance
✗  Release legal-hold transcripts to report generators without HOLD_NOTICE
✗  Allow the AI layer to modify any structured version field
✗  Allow chain hash computation to be skipped for any version
✗  Proceed with version creation if idempotency lock cannot be acquired
✗  Allow a no-diff amendment to succeed (no-op amendments are prohibited)
✗  Allow the INITIAL_COMMIT trigger if a version already exists for this transcript_id
✗  Allow version creation to proceed if audit write fails
✗  Allow chain_integrity_failure to go unescalated for more than 60 seconds
✗  Allow scoring to consume a version without consumer declaration
✗  Store participant display names (hashes only) in any version record
✗  Allow SCORE_RECOMPUTATION to modify participant_records fields
   (only score fields per amendment_type scope)
✗  Place or release legal holds without authorized agent identity verification
✗  Execute outside declared session domains
✗  Operate without model_version tag on every output
```

---

## SECTION 21 — DEPLOYMENT CONFIGURATION

```yaml
KUBERNETES_NAMESPACE    : realtime
SERVICE_TYPE            : Stateless microservice (Deployment)
REPLICA_MIN             : 3
REPLICA_MAX             : 50 (HPA on Kafka write queue depth + read RPS)
HPA_TRIGGER             : write queue lag > 200 messages OR read RPS > 8,000
RESOURCE_REQUEST        : 500m CPU, 512Mi RAM
RESOURCE_LIMIT          : 2000m CPU, 2Gi RAM
LIVENESS_PROBE          : /health/live
READINESS_PROBE         : /health/ready (checks Kafka, Redis, PostgreSQL, Vault connectivity)
STARTUP_PROBE           : 20s grace

KAFKA_TOPIC_IN          :
  - transcript.versioning.write.queue     (partitioned by tenant_id, keyed by transcript_id)
KAFKA_TOPIC_OUT         :
  - transcript.version.committed
  - transcript.version.chain_broken
  - transcript.version.legal_hold_placed
  - transcript.version.consumer_incompatible
  - transcript.version.amendment_anomaly
  - transcript.version.archive_instruction
  - transcript.score.recomputation_required
  - security.cross_tenant_violation

POSTGRES_USAGE          :
  - versioned_transcripts (INSERT-only, row-level security)
  - version_chain_index   (read/update for chain state)
  - version_access_log    (INSERT-only)
  - version_audit_records (INSERT-only)

REDIS_USAGE             :
  - Idempotency locks: "version_lock:{transcript_id}" (TTL 30s)
  - Chain index cache: "version_chain:{transcript_id}" (TTL 30m)
  - Latest stable cache: "version_latest_stable:{transcript_id}" (TTL 10m)
  - Chain integrity cache: "version_integrity:{transcript_id}" (TTL 6h)

SECRETS_MANAGED_BY      : HashiCorp Vault
  - Per-tenant HMAC signing keys
  - AES-256 encryption keys for version records
  - Agent identity certificates

CONFIG_MANAGED_BY       : Kubernetes ConfigMap (versioned)
FEATURE_FLAGS           : Unleash (per-tenant legal hold automation, AI narrative rollout)
WORM_ARCHIVE            : IMMUTABLE_ARCHIVE_SERVICE (instructed via event, not direct write)
AIRFLOW_JOBS            :
  - Chain integrity verification: every 6 hours, tenant-partitioned
  - Archival transition scan: daily, identifies COMMITTED versions past retention
  - Consumer compatibility scan: on schema evolution events
  - Orphaned PENDING version cleanup: every 15 minutes (PENDING > 5 min = anomaly)

ENVIRONMENTS            :
  - dev     : In-memory chain (no WORM), mock legal hold, no Vault
  - staging : Full chain + WORM test, Vault dev mode, full pipeline
  - prod    : Full stack, Vault prod, WORM retention enforced, 7-year minimum
```

---

## SECTION 22 — INTEGRATION ARCHITECTURE MAP

```
TRANSCRIPT_AGGREGATION_AGENT (AGTAA-002)
         │ transcript.assembled
         ▼
TRANSCRIPT_VERSIONING_AGENT (this agent — AGTVA-003)
         │
   ┌─────▼──────────────────────────────────────────────────────────┐
   │  VERSION CREATION PIPELINE (14 steps — Section 5.3)            │
   │  STEP 1  Validate trigger                                       │
   │  STEP 2  Acquire idempotency lock (Redis)                       │
   │  STEP 3  Load chain state (PostgreSQL + Redis cache)            │
   │  STEP 4  Compute version number                                 │
   │  STEP 5  Generate diff record (scope-enforced)                  │
   │  STEP 6  Compute chain_hash (SHA-256 linked)                    │
   │  STEP 7  Assemble VERSION_RECORD                                │
   │  STEP 8  Write VERSION_RECORD PENDING (PostgreSQL INSERT-only)  │
   │  STEP 9  Write AUDIT_RECORD (mandatory gate)                    │
   │  STEP 10 Promote to COMMITTED + Update chain index              │
   │  STEP 11 Mark prior version SUPERSEDED                          │
   │  STEP 12 Emit archive instruction → IMMUTABLE_ARCHIVE_SERVICE   │
   │  STEP 13 Emit downstream events (Kafka)                         │
   │  STEP 14 Release idempotency lock + verify chain                │
   └────────────────────────────────────────────────────────────────┘
         │
   ┌─────┴─────────────────────────────────────────────────────┐
   │ DOWNSTREAM CONSUMERS (version-declared consumption)        │
   ├──────────────────┬──────────────────┬──────────────────────┤
   ↓                  ↓                  ↓                      ↓
SCORING_ENGINE    ANALYTICS        LEGAL/AUDIT           PARENT REPORT
(LATEST_STABLE)   (version-pinned)  (FULL CHAIN)         (LATEST_STABLE)
         │
   ┌─────▼──────────────────────────┐
   │ AMENDMENT LIFECYCLE            │
   │ ADMIN_GOVERNANCE_SERVICE       │
   │ → authorized amendment         │
   │ → diff scope check             │
   │ → new version created          │
   │ → downstream recompute trigger │
   └────────────────────────────────┘
         │
   ┌─────▼──────────────────────────┐
   │ LEGAL HOLD LIFECYCLE           │
   │ LEGAL_POLICY_AGENT             │
   │ → hold placed on all versions  │
   │ → archival blocked             │
   │ → enhanced access logging      │
   │ → hold released by LEGAL only  │
   └────────────────────────────────┘
         │
   IMMUTABLE_ARCHIVE_SERVICE
   (WORM — 7-year minimum retention)
```

---

## SECTION 23 — ANTI-PATTERNS EXPLICITLY PROHIBITED

| Anti-Pattern | Why Prohibited |
|---|---|
| In-place update of any VERSION_RECORD | Destroys immutability — legal and compliance violation |
| Deleting a VERSION_RECORD for any reason | Chain breaks — evidence destruction |
| Allowing amendment without scope enforcement | Silent data drift — undetectable in audit |
| Skipping audit write before committing version | Untraced state change — compliance failure |
| Serving version to consumer without access log | Invisible data consumption — audit gap |
| Blanket legal hold release (without hold_id) | Could inadvertently release contested evidence |
| Chain hash skipped for "initial only" commits | Creates unverifiable root — entire chain is unsound |
| Parallel version writes without idempotency lock | Race condition → duplicate versions → chain corruption |
| Consumer consuming version without declaration | Undeclared schema dependency → silent breakage |
| AI layer classifying amendment types | Non-deterministic — rule engine governs amendment typing |
| Scoring during legal hold without audit flag | Produces untraced score from contested evidence |
| SCHEMA_MIGRATION that removes existing fields | Breaking change — all consumers break silently |
| Combining two amendment types in one version | Scope ambiguity — each version must have one diff_type |
| No-op amendments (zero diff) accepted | Pollutes version history — blocked by diff engine |

---

## SECTION 24 — FINAL LOCK DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════════════════╗
║                          AGENT SPECIFICATION — SEALED                                   ║
║                                                                                          ║
║  This document is the authoritative, governing specification for the                    ║
║  TRANSCRIPT_VERSIONING_AGENT operating within the Ecoskiller Antigravity platform.      ║
║                                                                                          ║
║  Any deviation, abbreviation, re-interpretation, or undocumented extension of this      ║
║  specification constitutes a compliance violation and MUST be escalated to              ║
║  ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT before any implementation proceeds.           ║
║                                                                                          ║
║  AGENT_ID         : AGTVA-003                                                            ║
║  VERSION          : v1.0.0                                                               ║
║  CHAIN ALGORITHM  : AGTVA-003-CHAIN-v1                                                   ║
║  SESSION_TYPES    : GD | DOJO | INTERVIEW | INTELLIGENCE_TEST | CERTIFICATION            ║
║  SEALED BY        : ECOSKILLER INTELLIGENCE & INNOVATION CORE                            ║
║  GOVERNANCE       : ANTIGRAVITY_ORCHESTRATION_GOVERNOR_AGENT                             ║
║  MUTATION LOCK    : Add-only — version bump required for any change                      ║
║  LEGAL AUTHORITY  : LEGAL_POLICY_AGENT                                                   ║
║  STATUS           : ACTIVE · PRODUCTION-READY                                            ║
╚══════════════════════════════════════════════════════════════════════════════════════════╝
```
