# HISTORICAL_DATA_ARCHIVE_AGENT
## Enterprise Optimization + Trust Infrastructure
### Antigravity Platform Layer — Ecoskiller / Dojo SaaS

---

```
ARTIFACT CLASS      : PRODUCTION SYSTEM BLUEPRINT
STATUS              : SEALED · LOCKED · GOVERNED
MUTATION POLICY     : ADD-ONLY VIA VERSION BUMP
INTERPRETATION AUTH : NONE
EXECUTION AUTHORITY : HUMAN DECLARATION ONLY
CREATIVE DEVIATION  : FORBIDDEN
ASSUMPTION FILLING  : FORBIDDEN
DEFAULT BEHAVIOR    : DENY
FAILURE MODE        : STOP → REPORT → NO PARTIAL OUTPUT
VERSION             : 1.0.0 — FINAL
```

---

## SECTION 0 — AGENT DECLARATION

This agent is the **sovereign, immutable, long-term archival backbone** of the Ecoskiller Antigravity Platform. It operates as an independent microservice below all business domains, enforcing append-only persistence, cryptographic integrity, regulatory compliance retention, legal hold authority, and trust chain verification.

**No microservice writes directly to historical storage. Every archival operation is mediated exclusively through this agent.**

The agent does not interpret. It enforces. It does not negotiate retention. It executes policy. It does not delete on demand. It applies law.

---

## SECTION 1 — AGENT IDENTITY CONTRACT

| Property | Value |
|---|---|
| Agent Name | HISTORICAL_DATA_ARCHIVE_AGENT |
| Namespace | `ecoskiller-archive` |
| Platform Layer | Antigravity — Below all domain services |
| Agent Class | Compliance Infrastructure |
| Access Model | API-only, no direct storage access by external services |
| Trust Model | Zero-trust + Cryptographic chain verification |
| Retention Authority | Agent holds final override on all deletion requests |
| Regulatory Frameworks | GDPR, India DPDP, SOX, ISO 27001, Education Sector, Minor Data Protection |
| Mutation Policy | IMMUTABLE after write for COMPLIANCE class; append-only for GOVERNANCE class |
| Failure Behavior | STOP → QUARANTINE → ALERT → ESCALATE |

---

## SECTION 2 — ARCHITECTURAL POSITION (ANTIGRAVITY LAYER)

```
┌─────────────────────────────────────────────────────────────┐
│                    ECOSKILLER SAAS PLATFORM                 │
│  ┌──────────┐ ┌───────────┐ ┌──────────┐ ┌──────────────┐  │
│  │  Auth    │ │  Billing  │ │   GD     │ │  Innovation  │  │
│  │  Service │ │  Service  │ │Orchestr. │ │  Engine      │  │
│  └────┬─────┘ └─────┬─────┘ └────┬─────┘ └──────┬───────┘  │
│       │             │            │               │          │
│       └─────────────┴────────────┴───────────────┘          │
│                              │                              │
│                    ┌─────────▼──────────┐                   │
│                    │   KAFKA EVENT BUS  │                   │
│                    └─────────┬──────────┘                   │
│                              │                              │
│  ════════════════════════════════════════════════════════   │
│            A N T I G R A V I T Y   L A Y E R               │
│  ════════════════════════════════════════════════════════   │
│                              │                              │
│         ┌────────────────────▼────────────────────┐         │
│         │   HISTORICAL_DATA_ARCHIVE_AGENT (THIS)  │         │
│         │   ┌──────────┐  ┌──────────┐            │         │
│         │   │  Trust   │  │ Policy   │            │         │
│         │   │  Chain   │  │ Engine   │            │         │
│         │   │  Engine  │  │ (OPA)    │            │         │
│         │   └────┬─────┘  └────┬─────┘            │         │
│         └────────┼─────────────┼──────────────────┘         │
│                  │             │                            │
│    ┌─────────────▼─────────────▼──────────────────┐         │
│    │            STORAGE CONTRACT                   │         │
│    │  MinIO WORM │ PostgreSQL │ ClickHouse          │         │
│    │  (Immutable)│ (Metadata) │ (Analytics)         │         │
│    └────────────────────────────────────────────────┘        │
└──────────────────────────────────────────────────────────────┘
```

**Critical Principle:** The archive agent is the ONLY path to historical storage. All 15+ microservices emit events to Kafka. The archive agent consumes, classifies, seals, and stores. No service bypasses this path. Violation = STOP EXECUTION.

---

## SECTION 3 — DATA CLASSIFICATION MATRIX (SEALED)

All data entering the archive system is assigned exactly one classification at write time. Classification is immutable. Reclassification requires dual-approval governance workflow with full audit trail.

### 3.1 Classification Registry

| Class ID | Data Type | Domain | Retention | Lock Type | Encryption | Legal Hold Eligible |
|---|---|---|---|---|---|---|
| `ARC-001` | GD Session Logs (metadata only) | Dojo / GD | 5 years | GOVERNANCE | AES-256 | Yes |
| `ARC-002` | GD Scoring Records | Dojo / GD | 7 years | COMPLIANCE | AES-256 | Yes |
| `ARC-003` | GD Integrity Audit Trails | Dojo / GD | Permanent | COMPLIANCE | AES-256 | Yes |
| `ARC-004` | Belt / Certification Records | Dojo / Arts | Permanent | COMPLIANCE | AES-256 | Yes |
| `ARC-005` | Royalty Ledger (double-entry) | Innovation | 15 years | COMPLIANCE | AES-256 + HSM | Yes |
| `ARC-006` | Licensing Contracts | Innovation | 15 years | COMPLIANCE | AES-256 + HSM | Yes |
| `ARC-007` | Invention Priority Timestamps | Innovation | Permanent | COMPLIANCE | AES-256 + HSM | Yes |
| `ARC-008` | Revenue Ingestion Reports | Innovation | 10 years | COMPLIANCE | AES-256 | Yes |
| `ARC-009` | Parent Consent Records | Trust Layer | 18 years | COMPLIANCE | AES-256 | Yes |
| `ARC-010` | KYC / Identity Verification | Trust Layer | 7 years post-deletion | GOVERNANCE | AES-256 | Yes |
| `ARC-011` | Invoices / Billing Records | Billing | 7 years (GST mandate) | COMPLIANCE | AES-256 | Yes |
| `ARC-012` | Legal Contract Documents | Legal | 15 years | COMPLIANCE | AES-256 + HSM | Yes |
| `ARC-013` | Safety / Complaint Evidence | Governance | Indefinite (hold-gated) | LEGAL_HOLD | AES-256 | Mandatory |
| `ARC-014` | Government Audit Trails | Compliance | Permanent | COMPLIANCE | AES-256 | Yes |
| `ARC-015` | AI Decision Logs | Intelligence | 7 years | GOVERNANCE | AES-256 | Yes |
| `ARC-016` | Vendor / Partner Contracts | Operations | 10 years post-termination | COMPLIANCE | AES-256 | Yes |
| `ARC-017` | Security Incident Records | Security | 10 years | COMPLIANCE | AES-256 | Yes |
| `ARC-018` | DPIA Records | Privacy | Permanent | COMPLIANCE | AES-256 | Yes |
| `ARC-019` | RCA / Incident Postmortems | Operations | 7 years | GOVERNANCE | AES-256 | No |
| `ARC-020` | Ownership Transfer Records | Innovation | Permanent | COMPLIANCE | AES-256 + HSM | Yes |
| `ARC-021` | Student Academic Records | Education | Permanent | COMPLIANCE | AES-256 | Yes |
| `ARC-022` | Minor Data Records (U18) | Trust Layer | Until age 25 minimum | COMPLIANCE | AES-256 + HSM | Yes |

### 3.2 Classification Rules (NON-NEGOTIABLE)

```
RULE C1: GD audio is NEVER archived. Data class registry REJECTS any audio payload.
         Structural prevention — not policy. Code enforces this at ingestion.

RULE C2: COMPLIANCE-locked artifacts are IMMUTABLE after write.
         No update. No delete. No overwrite. WORM enforced at storage layer.

RULE C3: GOVERNANCE-locked artifacts are APPEND-ONLY.
         New versions append. Old versions preserved. Deletion requires legal authority.

RULE C4: LEGAL_HOLD classification suspends all lifecycle automation.
         Agent freezes artifact. All expiry timers halted. Hold release = dual-approval only.

RULE C5: Minor data (ARC-022) receives maximum protection tier regardless of other rules.
         Parent consent required for any access. MFA mandatory for retrieval.

RULE C6: Cross-tenant classification leakage = SECURITY FAILURE → STOP EXECUTION.
         Tenant tag is cryptographically embedded in Trust Envelope at write time.
```

---

## SECTION 4 — STORAGE TIER ARCHITECTURE

### 4.1 Tier Definitions

| Tier | Name | Storage Backend | Access Pattern | Compression | Cost Target |
|---|---|---|---|---|---|
| T1 | Hot Operational | MinIO Standard | < 24hr access | None | $0.023/GB/mo |
| T2 | Warm Archive | MinIO Infrequent | 90 days – 3 years | LZ4 | $0.0125/GB/mo |
| T3 | Cold Archive | MinIO Glacier-class | 3 – 10 years | ZSTD | $0.004/GB/mo |
| T4 | Deep Immutable Archive | MinIO WORM | 10+ years / Permanent | ZSTD + dedup | $0.00099/GB/mo |
| T5 | Legal Hold Vault | MinIO WORM (isolated) | Court-ordered access | ZSTD | Compliance cost |
| T6 | Audit Vault | MinIO WORM (isolated) | Permanent access | LZ4 | Compliance cost |

### 4.2 Lifecycle Transition Rules (AUTOMATED VIA AIRFLOW)

```
HOT  →  WARM  :  After 90 days (configurable per classification, min 30 days)
WARM →  COLD  :  After 1 year from creation (configurable per classification)
COLD →  ARCHIVE:  After 3 years from creation (configurable per classification)
ARCHIVE: No further transition. Permanent tier for COMPLIANCE class.

EXCEPTIONS (NO AUTOMATIC TRANSITION):
  - LEGAL_HOLD artifacts: Frozen at current tier until hold release
  - Permanent classification (ARC-004, ARC-007, ARC-014, ARC-018, ARC-021): Never transition beyond ARCHIVE
  - Active litigation artifacts: Frozen regardless of age

COMPRESSION SCHEDULE:
  - T2 (Warm): LZ4 on transition (fast, lightweight, 3-5x ratio for logs)
  - T3+ (Cold/Archive): ZSTD level 19 (maximum compression, 5-10x ratio for text/logs)
  - Binary artifacts (certs, documents): ZSTD level 6 (1.5-2x ratio)

DEDUPLICATION:
  - Content-addressable storage applied at T2+ transition
  - Estimated 20-40% savings for templates, certificates, standard documents
  - SHA-256 content hash used as dedup key (also serves integrity verification)
```

### 4.3 Bucket Schema (TENANT ISOLATION ENFORCED)

```
MinIO Bucket Naming Convention:
  ecoskiller-archive-{tenant_id}-{classification_class}-{tier}

Examples:
  ecoskiller-archive-tenant001-arc001-warm
  ecoskiller-archive-tenant002-arc005-immutable
  ecoskiller-archive-platform-arc014-audit

RULES:
  - Separate bucket per tenant per classification per tier
  - Bucket creation is automated at tenant provisioning
  - Bucket policy: WORM for COMPLIANCE class, GOVERNANCE for GOVERNANCE class
  - Cross-bucket access: DENIED by MinIO policy + OPA enforcement
  - Bucket deletion: FORBIDDEN for active tenants
  - Tenant offboard: Bucket export → grace period → cryptographic deletion
```

---

## SECTION 5 — TRUST CHAIN ARCHITECTURE (CRYPTOGRAPHIC INTEGRITY)

Every artifact written to the archive receives a **Trust Envelope** at write time. The Trust Envelope is immutable, co-stored with the artifact, and verified on every read.

### 5.1 Trust Envelope Schema

```json
{
  "trust_envelope_version": "1.0",
  "artifact_id": "uuid-v4-globally-unique",
  "tenant_id": "tenant_cryptographic_tag",
  "classification": "ARC-005",
  "domain": "innovation",
  "write_timestamp": "ISO-8601-UTC",
  "timestamp_authority": "RFC3161-TSA-response",
  "content_hash": "SHA-256-hex",
  "hmac_signature": "HMAC-SHA256-hex (Vault-managed key)",
  "hmac_key_version": "vault-key-id-rotation-ref",
  "chain_hash": "SHA-256(prev_chain_hash + content_hash + timestamp)",
  "chain_position": 4821,
  "merkle_leaf_hash": "SHA-256-merkle-position",
  "encryption_key_ref": "vault-tenant-key-id",
  "storage_tier": "immutable",
  "lifecycle_class": "COMPLIANCE",
  "retention_expiry": "ISO-8601-UTC or PERMANENT",
  "legal_hold_status": false,
  "schema_version": "ecoskiller-archive-schema-v1"
}
```

### 5.2 Chain Hashing Protocol

Critical classification classes (`ARC-005`, `ARC-006`, `ARC-007`, `ARC-012`, `ARC-020`) use **blockchain-style chain hashing** for tamper detection:

```
chain_hash[n] = SHA-256(chain_hash[n-1] || content_hash[n] || timestamp[n])

Properties:
  - Any modification to any prior record invalidates all subsequent chain hashes
  - Chain is append-only (matches WORM storage semantics)
  - Chain root hash stored in separate Merkle tree
  - Merkle tree root published daily to append-only audit log
  - Cross-verification: chain_hash vs Merkle leaf vs content_hash must all agree

Failure behavior:
  - Hash mismatch → QUARANTINE artifact
  - Trigger INTEGRITY_FAILURE event to Kafka
  - Alert Wazuh security monitoring
  - Attempt repair from replica (cross-region)
  - If repair fails → ESCALATE to governance → MARK_UNRECOVERABLE
  - Never serve corrupted artifact. Ever.
```

### 5.3 Integrity Verification Schedule (AIRFLOW DAGs)

```
COMPLIANCE class  : Daily verification sweep (Airflow DAG: archive_integrity_compliance)
GOVERNANCE class  : Weekly verification sweep (Airflow DAG: archive_integrity_governance)
Legal Hold class  : Continuous (triggered on any access attempt)
Audit Vault       : Real-time (every write triggers immediate chain verification)

Anti-Entropy Process:
  - DAG runs full Merkle tree reconstruction
  - Compares reconstructed root vs stored root
  - Discrepancy → quarantine all artifacts in affected subtree
  - Repair from cross-region replica
  - Report → governance dashboard within 5 minutes of detection
```

### 5.4 Key Management (Vault)

```
Key Types:
  ├── Archive Encryption Keys (per tenant, AES-256-GCM)
  │     Rotation: Every 180 days (automatic, zero-downtime)
  │     Old keys retained for decryption of old artifacts (no re-encryption required)
  │
  ├── HMAC Signing Keys (per tenant)
  │     Rotation: Every 90 days (automatic)
  │     Previous version retained for verification of existing Trust Envelopes
  │
  ├── Legal Hold Authorization Keys (platform-level, MFA-protected)
  │     Rotation: Every 60 days (manual, dual-approval)
  │     Usage logged to tamper-proof audit vault
  │
  └── HSM-Backed Keys (for ARC-005, ARC-007, ARC-012, ARC-020, ARC-022)
        Hardware Security Module backing for highest-sensitivity classifications
        Never extractable from HSM. All crypto operations performed inside HSM.

Vault Auth Method: Kubernetes Service Account (per namespace)
Vault Policy: Least-privilege per agent component
```

---

## SECTION 6 — AGENT API CONTRACT

All APIs enforce: JWT tenant isolation, OPA policy check, purpose limitation, consent alignment, rate limiting, and full audit logging of every call.

### 6.1 API Surface

```
BASE PATH: /api/v1/archive

POST   /write
       Body: { classification, artifact_payload, tenant_id, metadata }
       Action: Classify → Encrypt → Generate Trust Envelope → Write to MinIO → Register metadata
       Returns: { artifact_id, trust_envelope_hash, storage_tier, retention_expiry }
       Idempotency: Content-addressable (duplicate content returns existing artifact_id)

GET    /read/{artifact_id}
       Headers: Authorization, X-Purpose, X-Requester-Role
       Action: Validate access → Verify Trust Envelope → Decrypt → Return artifact
       Returns: artifact_payload + trust_envelope
       Side Effect: Write to audit trail (mandatory, synchronous)

POST   /legal-hold
       Body: { artifact_ids[], hold_reason, legal_authority, authority_ref, scope }
       Auth: MFA required + Legal Hold Key
       Action: Freeze artifacts → Suspend lifecycle → Log to audit vault
       Returns: { hold_id, frozen_count, hold_timestamp }

DELETE /legal-hold/{hold_id}
       Auth: Dual-approval (two governance admins) + MFA both
       Action: Release freeze → Resume lifecycle from hold point → Log release
       Returns: { released_count, lifecycle_resume_timestamp }

GET    /verify/{artifact_id}
       Action: Retrieve Trust Envelope → Recompute content hash → Verify chain hash → Verify HMAC
       Returns: { verified: bool, chain_position, last_verified, anomalies[] }

GET    /audit-trail/{artifact_id}
       Returns: Complete access history (who accessed, when, why, what was served)

POST   /restore/{artifact_id}
       Body: { target_tier, restore_reason, requester }
       Action: Promote artifact from cold/archive to warm for operational access
       Returns: { restore_job_id, estimated_completion }

POST   /offboard/{tenant_id}
       Auth: Dual-approval + Platform Admin + MFA
       Action: Execute tenant data export → Grace period → Cryptographic deletion workflow
       Returns: { export_job_id, artifact_count, export_ready_timestamp }

GET    /health
       Returns: Agent health, storage connectivity, key rotation status, integrity sweep status

GET    /metrics
       Returns: Prometheus-format metrics (consumed by Prometheus scraper)
```

### 6.2 API Security Rules (NON-NEGOTIABLE)

```
RULE A1: Every /read call writes to audit trail BEFORE returning artifact.
         If audit write fails → DO NOT serve artifact → Return 503.

RULE A2: Every /write call generates Trust Envelope BEFORE confirming success.
         If Trust Envelope generation fails → ROLLBACK write → Return 500.

RULE A3: Legal hold operations require MFA verification in the same HTTP session.
         Cached MFA tokens NOT accepted. Real-time challenge required.

RULE A4: /offboard requires dual-approval. Both approvals must arrive within 1-hour window.
         Expired single approval invalidates the workflow. Start again.

RULE A5: Presigned URLs used for all artifact payloads > 10MB.
         Direct streaming through agent API for large artifacts is FORBIDDEN.
         Presigned URL expiry: 15 minutes maximum. Single-use enforcement.

RULE A6: Rate limiting enforced per tenant:
         /write: 1000 req/min (burst: 5000/min)
         /read: 500 req/min (burst: 2000/min)
         /legal-hold: 10 req/min (burst: 50/min) — deliberate throttle
         Exceeding limits → 429 → Logged → Alert if sustained
```

---

## SECTION 7 — GD-SPECIFIC ARCHIVAL CONTRACT

The Voice GD system (Automated Voice Group Discussion, Jitsi + Redis state machine) produces archival events. This section defines the exact contract between GD Orchestrator and the archive agent.

### 7.1 GD Data Classification Map

| GD Data Type | Classification | Retention | Notes |
|---|---|---|---|
| Session metadata (batch, participants, topic, timestamps) | `ARC-001` | 5 years | Behavioral metrics only |
| Scoring records (mic duration, turns, interrupt attempts) | `ARC-002` | 7 years | Rule-based scores only |
| Turn-by-turn audit log (who spoke, when, duration) | `ARC-003` | Permanent | Integrity audit chain |
| Network drop events | `ARC-001` | 5 years | Part of session metadata |
| GD audio streams | **REJECTED** | **NEVER** | Structural prevention at ingestion |
| Speech content / transcripts | **REJECTED** | **NEVER** | No NLP, no content capture |
| Tone / emotion / accent data | **REJECTED** | **NEVER** | Zero AI judgment in GD |

### 7.2 GD Audio Rejection Protocol

```
RULE GD-1: The archive agent's ingestion handler maintains a DATA_CLASS_REGISTRY.
           ARC-AUDIO class does not exist in the registry.
           Any payload with mime_type containing "audio/*" is:
             1. REJECTED (HTTP 422 — Unprocessable Entity)
             2. Rejection logged to security audit trail
             3. Wazuh alert triggered if repeated rejections from same source

RULE GD-2: This is structural, not policy-based.
           Policy can be changed. Struct cannot. Audio handling code does not exist.

RULE GD-3: GD Orchestrator emits only behavioral events to Kafka:
           { participant_id, session_id, mic_open_seconds, turns_completed,
             turns_skipped, interrupt_attempts, silence_events, network_drops,
             session_exit_reason }
           No voice data. No text data. No sentiment data.
```

### 7.3 GD Scoring Archival (Immutable Audit Trail)

```
Scoring formula stored alongside score record:
{
  "artifact_id": "...",
  "session_id": "...",
  "participant_id": "...",
  "score_components": {
    "turns_used": { "value": 10, "weight": 1.0, "earned": 10 },
    "full_time_spoke": { "value": 10, "weight": 1.0, "earned": 10 },
    "interrupt_attempts": { "value": -2, "count": 1, "deducted": 2 }
  },
  "final_score": 18,
  "formula_version": "gd-scoring-v1.0",
  "formula_hash": "SHA-256-of-formula-definition",
  "certification_linked": false,
  "computed_by": "scoring-engine-v1.2.3",
  "computed_at": "ISO-8601-UTC"
}

IMMUTABILITY: Score records are COMPLIANCE-locked on write.
              No score correction without legal governance workflow.
              Score disputes → create DISPUTE record as APPEND, never modify original.
```

---

## SECTION 8 — INNOVATION & ROYALTY ARCHIVAL CONTRACT

The Innovation Engine (Idea Registry, Royalty Accounting, Licensing) produces the highest-sensitivity financial and legal artifacts in the platform.

### 8.1 Invention Priority Timestamp Chain

```
Each idea submission generates:
  1. Content hash of idea DNA fingerprint
  2. RFC3161 timestamp authority seal (external TSA)
  3. Chain hash linked to platform's invention priority chain
  4. HSM-backed signature (non-extractable key)
  5. Permanent storage in ARC-007 (COMPLIANCE, permanent)

Chain property: Invention timestamp chain is globally ordered.
                Claim of "I invented first" is cryptographically provable from chain position.
                Chain cannot be backdated. Insertion order is sealed at TSA timestamp.
```

### 8.2 Royalty Ledger Architecture

```
Double-Entry Ledger (ARC-005):
  Every royalty record has:
    - DEBIT entry (business account)
    - CREDIT entry (innovator wallet)
    - Balance verification hash (debit + credit = zero sum check)
    - Revenue report reference (ARC-008)
    - Licensing contract reference (ARC-006)
    - Computed royalty: revenue × royalty_rate (0.01% – 0.05%)
    - Blockchain-style chain hash across all ledger entries

Immutability:
  Royalty entries are APPEND-ONLY.
  Corrections create new ADJUSTMENT entry with reference to corrected entry.
  Original entry never modified. Audit trail shows correction history.
  15-year COMPLIANCE retention. HSM-backed encryption.

Royalty Wallet (child innovators):
  Balance derived from ledger — never stored separately.
  Payout records append to ledger.
  Parent consent required for payout authorization.
  Ownership transfer at age 18 triggers ARC-020 record + governance workflow.
```

### 8.3 Revenue Ingestion Archive

```
Revenue reports from businesses (ARC-008):
  - Cryptographically signed by submitting business (digital signature)
  - Agent verifies signature before archiving
  - Fraud Detection Engine signal stored alongside report
  - Anomaly flags preserved in immutable record
  - 10-year retention, COMPLIANCE lock
  - Discrepancy between reports and royalty calculations → ALERT → Legal hold eligible
```

---

## SECTION 9 — TENANT ISOLATION CONTRACT (ZERO-BLEED)

### 9.1 Isolation Mechanisms

```
Layer 1 — Storage Isolation:
  Separate MinIO buckets per tenant per classification
  Bucket ACL: tenant service account only
  No shared bucket policies across tenants

Layer 2 — Metadata Isolation:
  PostgreSQL Row-Level Security (RLS) enforced
  All queries automatically filtered by tenant_id from JWT claims
  Application-level tenant filter is DEFENSE-IN-DEPTH, not primary control
  Primary control = RLS (database-enforced)

Layer 3 — Encryption Isolation:
  Separate AES-256 key per tenant (Vault-managed)
  Separate HMAC key per tenant (Vault-managed)
  Cross-tenant key access: IMPOSSIBLE by Vault policy

Layer 4 — Network Isolation:
  OPA policy enforces tenant claim in JWT against requested artifact's tenant tag
  Kubernetes NetworkPolicy: archive namespace → storage namespace only
  No inter-tenant routing permitted

Layer 5 — Audit Isolation:
  Per-tenant audit log stream in Loki
  Cross-tenant audit leakage = SECURITY FAILURE
```

### 9.2 Domain Isolation (Arts / Commerce / Science / Technology / Administration)

```
Domain separation mirrors tenant isolation at classification level:
  - Domain tag cryptographically embedded in Trust Envelope
  - OPA policy verifies domain claim in JWT matches artifact domain
  - Cross-domain access requires explicit grant in RBAC (double approval)
  - Violation → SECURITY_EVENT → Wazuh alert → STOP serving request

Dojo Arts domain:
  - Belt/certification records isolated from other domains
  - Arts performance data never shared with commerce/science domains
  - Parent-visible data scoped to child's enrolled domain only
```

---

## SECTION 10 — LEGAL HOLD WORKFLOW (COURT-DEFENSIBLE)

### 10.1 Hold Initiation Sequence

```
Step 1: Legal authority submits POST /legal-hold with:
          - Legal authority identifier (court order ref / regulatory ref)
          - Scope definition (tenant, classification, date range, or specific artifact_ids)
          - Hold reason (litigation / regulatory / investigation)
          - Requester identity (MFA-verified in session)

Step 2: Agent validates:
          - Authority ref format (structural validation)
          - Scope doesn't exceed platform bounds
          - Requester has LEGAL_HOLD role in RBAC
          - MFA token is fresh (< 5 minutes)

Step 3: Agent executes:
          - All matching artifacts → frozen flag set in metadata DB
          - Lifecycle automation: DAG excludes frozen artifacts from transition
          - Trust Envelope updated: legal_hold_status = true, hold_ref appended
          - Hold record written to Audit Vault (permanent, tamper-proof)

Step 4: Notification:
          - governance@platform and legal@platform notified immediately
          - Hold ID returned to requesting authority
          - Frozen artifact count confirmed

Step 5: Access during hold:
          - Read access permitted to authorized parties only
          - Every read logged with enhanced detail (requester, purpose, IP, timestamp)
          - Modification attempts → DENIED → ALERT
```

### 10.2 Hold Release Sequence

```
Release requires:
  1. Governance Admin A authenticates (MFA)
  2. Governance Admin B authenticates (MFA) — different person, different session
  3. Legal confirmation document uploaded (PDF, archived as ARC-012)
  4. Both approvals within 4-hour window
  5. Agent executes release:
       - Frozen flag cleared from metadata
       - Lifecycle resumes from hold point (age counter continues from pre-hold state)
       - Release record appended to Audit Vault
       - Hold history PERMANENTLY retained (release does not erase hold history)
```

---

## SECTION 11 — TENANT OFFBOARDING PROTOCOL

```
Trigger: Tenant admin + Platform admin dual-approval POST /offboard/{tenant_id}

Phase 1 — Export (Days 1-7):
  Agent generates complete data export across all tiers and classifications
  Export encrypted with tenant-provided public key (agent holds no copy of private key)
  Export package stored in MinIO with 90-day access window
  Checksum manifest provided to tenant

Phase 2 — Confirmation (Days 8-30):
  Tenant downloads and confirms receipt (digital acknowledgment required)
  Platform provides confirmation receipt (archived as ARC-012)

Phase 3 — Soft Deletion (Day 30):
  Metadata marked deleted — data inaccessible to all services
  Physical data untouched (30-day grace for recovery request)

Phase 4 — Physical Purge (Day 60):
  All tenant MinIO buckets purged
  Cryptographic deletion verification (content hash no longer resolvable)
  Deletion certificate generated (court-defensible, archived in platform audit vault)
  Tenant encryption keys destroyed in Vault (HSM-backed destruction where applicable)

Phase 5 — Audit Retention:
  All audit logs PERMANENTLY retained (tenant-anonymized)
  Deletion certificate accessible to authorized parties indefinitely
  Compliance record preserved for regulatory inquiry

EXCEPTION: Artifacts under active legal hold cannot be deleted.
           Offboarding pauses for held artifacts.
           Resolution required before final purge.
```

---

## SECTION 12 — BACKUP & DISASTER RECOVERY CONTRACT

### 12.1 RPO / RTO Targets

| Storage Class | RPO | RTO | Mechanism |
|---|---|---|---|
| Audit Vault | 5 minutes | 10 minutes | Synchronous cross-region replication |
| Legal Hold Vault | 0 (synchronous) | 30 minutes | Sync replication + hot standby |
| COMPLIANCE Archive | 24 hours | 48 hours | Async replication, daily sync |
| GOVERNANCE Archive | 24 hours | 72 hours | Async replication, daily sync |
| Metadata DB (PostgreSQL) | 5 minutes | 15 minutes | Streaming replication + Velero |

### 12.2 Backup Architecture

```
MinIO → MinIO replication (same-protocol, self-hosted, cross-region):
  Primary: GCP region (asia-south1)
  Secondary: AWS region (ap-south-1)
  Both MinIO instances: self-hosted, open source, zero paid cloud storage APIs

Velero: Kubernetes cluster backup
  Schedule: Every 6 hours for ops metadata, Daily for archive metadata
  Target: Cross-region MinIO bucket (not cloud provider backup service)
  Encryption: AES-256 before storage

PostgreSQL:
  Streaming replication to standby (same cluster)
  WAL archiving to MinIO (cross-region)
  Point-in-time recovery capability (7-day window for metadata DB)

ClickHouse:
  Replicated tables (native ClickHouse replication)
  Daily snapshots to MinIO

Recovery Test:
  Monthly DR drill (automated Airflow DAG)
  Recovery validation: artifact read + integrity verify post-restore
  RTO breach → PagerDuty alert → Engineering escalation
```

---

## SECTION 13 — OBSERVABILITY CONTRACT

### 13.1 Prometheus Metrics (Mandatory)

```
# Write operations
archive_write_total{tenant, classification, tier, status}
archive_write_duration_seconds{tenant, classification} [histogram: p50, p90, p99]
archive_write_bytes_total{tenant, classification}

# Read operations
archive_read_total{tenant, classification, purpose, status}
archive_read_duration_seconds{tenant, classification} [histogram]

# Integrity
archive_integrity_checks_total{classification, result}
archive_integrity_failures_total{classification, failure_type}
archive_chain_hash_verifications_total{classification, result}

# Legal hold
archive_legal_hold_active{tenant}
archive_legal_hold_operations_total{operation, status}

# Lifecycle transitions
archive_lifecycle_transitions_total{from_tier, to_tier, classification}
archive_lifecycle_errors_total{classification, error_type}

# Backup / DR
archive_backup_lag_seconds{region, storage_class}
archive_rpo_breach_total{region, storage_class}
archive_replica_sync_status{primary_region, secondary_region}

# Cost attribution
archive_storage_bytes{tenant, classification, tier}
archive_estimated_cost_dollars{tenant, classification}

# Security
archive_policy_violations_total{violation_type, tenant}
archive_unauthorized_access_attempts_total{tenant, endpoint}
```

### 13.2 Loki Log Streams

```
archive.write       : artifact_id, tenant, classification, tier, size_bytes, duration_ms, trust_envelope_hash
archive.read        : artifact_id, tenant, requester_role, purpose, duration_ms, integrity_verified
archive.legal_hold  : hold_id, artifact_count, authority_ref, action, admin_1, admin_2, timestamp
archive.integrity   : artifact_id, classification, check_type, result, action_taken, repair_attempted
archive.lifecycle   : artifact_id, classification, from_tier, to_tier, trigger, compression_ratio
archive.violation   : violation_type, tenant, artifact_id, endpoint, action, wazuh_alert_id
archive.offboard    : tenant_id, phase, artifact_count, status, timestamp
archive.backup      : region, storage_class, lag_seconds, sync_status, rpo_status
```

### 13.3 Grafana Dashboards

```
Dashboard 1: Archive Health
  - Write/read rates (real-time)
  - Error rates by classification
  - P99 latency heatmap
  - Trust Envelope generation success rate

Dashboard 2: Compliance Status
  - WORM integrity sweep results (daily/weekly)
  - Legal hold active count by tenant
  - Retention expiry calendar (next 90 days)
  - Chain hash verification pass rate

Dashboard 3: Tenant Distribution
  - Storage by tenant, classification, tier (treemap)
  - Growth trend (30-day, 90-day, 1-year)
  - Cost attribution by tenant

Dashboard 4: Backup / DR Status
  - RPO adherence (green/amber/red)
  - Replication lag (per region)
  - Last successful DR test result
  - RTO confidence meter

Dashboard 5: Security Events
  - Policy violations timeline
  - Unauthorized access heatmap (by tenant, by endpoint)
  - Legal hold operations log
  - Wazuh alert feed (archive-tagged events)

Dashboard 6: Cost Analytics
  - Storage cost per classification tier
  - Compression savings ($)
  - Deduplication savings ($)
  - Projected growth vs budget
```

---

## SECTION 14 — SECURITY POSTURE (HARDENED)

```
Transport Security:
  TLS 1.3 mandatory for all inter-service communication
  mTLS between archive agent and storage backends
  Certificate rotation: Cert-Manager automated (Let's Encrypt or internal CA)

Storage Security:
  AES-256-GCM at-rest encryption (all tiers)
  HSM-backed keys for highest-sensitivity classifications
  MinIO server-side encryption (SSE-S3 compatible)

Access Control:
  OPA policy engine: all access decisions
  RBAC enforced at JWT claim level (Keycloak)
  IP allowlisting for legal hold operations (Wazuh-monitored deviation)
  MFA mandatory for: legal hold operations, tenant offboarding, key rotation

Threat Detection:
  Wazuh SIEM integration: all archive events forwarded
  Anomaly detection: unusual read volumes, off-hours access, bulk read patterns
  File integrity monitoring on agent binaries (Wazuh FIM)
  Automated block on repeated policy violations (OPA + Kong rate limit)

Audit Logging:
  Every operation logged before execution (write-ahead audit log)
  Audit logs stored in separate WORM bucket (ARC-AUDIT class)
  Audit logs: never deleted, never modified, never accessible to application services
  Audit queries: read-only replica, separate service account
```

---

## SECTION 15 — DEPLOYMENT SPECIFICATION

### 15.1 Kubernetes Deployment

```yaml
Namespace: ecoskiller-archive

Deployments:
  archive-api-server:
    replicas: 3 (min) → 12 (max, HPA)
    resources:
      requests: cpu=500m, memory=1Gi
      limits: cpu=4, memory=8Gi
    affinity: anti-affinity across nodes

  archive-integrity-worker:
    replicas: 2 (min) → 6 (max)
    resources:
      requests: cpu=1, memory=2Gi
      limits: cpu=8, memory=16Gi

  archive-lifecycle-worker:
    replicas: 2 (min) → 4 (max)
    resources:
      requests: cpu=500m, memory=1Gi

  archive-legal-hold-service:
    replicas: 2 (fixed, no autoscaling)
    resources:
      requests: cpu=250m, memory=512Mi
    note: Fixed replicas — legal hold must be deterministic

NetworkPolicy:
  Ingress: From kong gateway namespace only
  Egress: To minio, postgresql, vault, kafka, clickhouse namespaces only
  All other ingress/egress: DENIED
```

### 15.2 Version Pins (LOCKED)

```
MinIO           : Latest LTS with object locking (WORM support confirmed)
PostgreSQL      : 15+ (RLS, logical replication, partitioning)
ClickHouse      : 23+ (replicated tables, native analytics)
Apache Airflow  : 2.7+ (DAG-level locking, task isolation)
HashiCorp Vault : 1.15+ (PKI, KV v2, HSM integration)
OPA             : Latest stable (Rego policy engine)
Velero          : Latest stable (MinIO backend support confirmed)
Wazuh           : 4.7+ (SIEM, FIM, anomaly detection)
Prometheus      : 2.45+
Grafana         : 10+
Loki            : 2.9+
```

---

## SECTION 16 — FAILURE MODE REGISTRY

| Failure Event | Agent Response | Escalation |
|---|---|---|
| MinIO write timeout | Queue to Kafka, retry with exponential backoff (3x), alert at 15min lag | PagerDuty if 30min unresolved |
| MinIO outage | Kafka buffer all writes, serve reads from metadata (no artifact payload), switch to DR replica | Engineering on-call |
| PostgreSQL failover | Read-only mode, metadata cached Redis (5min TTL), auto replica promotion | DBA on-call |
| Integrity hash mismatch | Quarantine artifact, serve INTEGRITY_FAILURE error, attempt repair from replica | Governance + Security |
| WORM write attempt after seal | DENY, log SECURITY_EVENT to Wazuh, escalate to security team | Security team immediate |
| Legal hold key expired | Block all legal hold operations, alert governance, emergency rotation workflow | Legal + Governance |
| Cross-tenant access attempt | DENY (409 Forbidden), Wazuh alert, tenant flagged, access suspended | Security + Account team |
| Kafka consumer lag | Scale integrity/lifecycle workers, alert if lag > 10min | DevOps autoscaling |
| Trust Envelope generation failure | ROLLBACK write, return 500 to caller, no partial writes ever | Engineering alert |
| DR replica desync beyond RPO | Alert immediately, throttle non-critical writes, force sync | Engineering + DevOps |
| Airflow DAG failure (integrity sweep) | Retry 3x, alert DevOps, manual sweep required | DevOps on-call |
| Vault connection loss | Serve reads from decrypted cache (60 second TTL), block new writes, alert | Engineering immediate |
| Offboarding dual-approval timeout | Invalidate single approval, require restart, log warning | Governance team |

---

## SECTION 17 — INTEGRATION MAP

```
PRODUCES EVENTS TO:
  Kafka topic: archive.artifact.written    → Analytics Service, Compliance Dashboard
  Kafka topic: archive.integrity.checked   → Governance Dashboard, Wazuh
  Kafka topic: archive.legal.hold.changed  → Governance, Legal, Notification Service
  Kafka topic: archive.policy.violation    → Security, Wazuh, Governance
  Kafka topic: archive.lifecycle.transited → Analytics, Cost Dashboard

CONSUMES EVENTS FROM:
  Kafka topic: *.completed (all domain services)  → Archival trigger
  Kafka topic: billing.invoice.generated          → ARC-011 archival
  Kafka topic: gd.session.completed               → ARC-001, ARC-002, ARC-003
  Kafka topic: innovation.idea.submitted          → ARC-007 (invention timestamp)
  Kafka topic: royalty.ledger.entry.created       → ARC-005
  Kafka topic: certification.belt.granted         → ARC-004
  Kafka topic: legal.contract.executed            → ARC-006, ARC-012
  Kafka topic: user.consent.captured              → ARC-009
  Kafka topic: security.incident.detected         → ARC-017
  Kafka topic: compliance.dpia.completed          → ARC-018

CALLS (SYNCHRONOUS):
  Vault     : Key retrieval, HMAC signing, HSM operations
  OPA       : Policy enforcement (every API call)
  Keycloak  : JWT verification (every API call)
  MinIO     : Artifact read/write/verify
  PostgreSQL: Metadata read/write, audit trail
  ClickHouse: Analytics write (async, non-blocking)

CALLED BY:
  Kong API Gateway  : Routes external requests to archive API
  All domain services: Via Kafka events (no direct HTTP calls to archive from domains)
  Airflow DAGs      : Lifecycle and integrity sweep orchestration
  Governance Service: Legal hold operations (via archive API with elevated credentials)
```

---

## SECTION 18 — THE 10 SEALED INVARIANTS

```
INVARIANT 1 — NO DIRECT STORAGE ACCESS
  No microservice, no intern, no admin, no script may write directly to MinIO
  archive buckets. All writes are mediated by this agent. Violation detected by
  MinIO bucket policy → DENY + alert. Zero exceptions.

INVARIANT 2 — COMPLIANCE ARTIFACTS ARE IMMUTABLE AFTER WRITE
  WORM storage enforced at MinIO object level. Agent policy enforces this.
  Storage policy enforces this. Two layers. Neither can be bypassed individually.

INVARIANT 3 — ZERO CROSS-TENANT DATA LEAKAGE
  Cryptographic isolation (separate keys) + storage isolation (separate buckets)
  + RLS isolation (database) + OPA isolation (policy). All four must be compromised
  simultaneously for leakage. This is treated as impossible by design.

INVARIANT 4 — GD AUDIO IS NEVER ARCHIVED
  The data class registry does not contain an audio class. Ingestion handler
  structurally rejects audio payloads. This is code, not config. Cannot be
  toggled by configuration or feature flag.

INVARIANT 5 — EVERY WRITE GENERATES A TRUST ENVELOPE
  No artifact exists in the archive without a Trust Envelope. If Trust Envelope
  generation fails, the write is rolled back. Partial writes do not exist.

INVARIANT 6 — LEGAL HOLD OVERRIDES ALL RETENTION POLICIES
  Legal hold is the highest authority in the retention hierarchy. No expiry timer,
  no lifecycle DAG, no tenant offboarding can delete a held artifact. Court authority
  supersedes platform policy.

INVARIANT 7 — TENANT OFFBOARDING REQUIRES DUAL-APPROVAL
  One admin cannot offboard a tenant. The system requires two distinct authenticated
  governance administrators with independent MFA challenges. This is enforced in code.

INVARIANT 8 — INTEGRITY FAILURES TRIGGER QUARANTINE
  A hash mismatch never results in serving the artifact. Ever. The artifact is
  quarantined, repair is attempted from replica, and governance is notified.
  If repair fails, the artifact is marked UNRECOVERABLE and governance escalates.

INVARIANT 9 — AUDIT LOGS ARE PERMANENT AND TAMPER-PROOF
  Audit logs live in a separate WORM bucket. They have no expiry. They are not
  included in tenant offboarding deletion. They are verified by the same integrity
  chain as all other COMPLIANCE artifacts.

INVARIANT 10 — ARCHIVE TIER DATA IS RECOVERABLE WITHIN RTO
  The agent maintains backup health monitoring. If RPO/RTO SLA breach is detected,
  non-critical writes are throttled and engineering is paged. The promise to recover
  within RTO is not aspirational — it is monitored and enforced by automated alerting.
```

---

## SECTION 19 — COMPLIANCE ALIGNMENT

| Framework | Coverage |
|---|---|
| **GDPR** | Right to erasure (offboarding workflow), data portability (export phase), retention limits (classification matrix), consent records (ARC-009) |
| **India DPDP** | Data localization (GCP asia-south1 primary), consent management (ARC-009), processing limitation, retention schedules aligned |
| **SOX** | Financial record immutability (ARC-005, ARC-011), audit trail preservation (ARC-003, ARC-014), double-entry ledger (royalty) |
| **ISO 27001** | Information security controls, evidence retention (ARC-017, ARC-018), key management (Vault), access logging |
| **Minor Data Protection** | ARC-022 maximum protection tier, HSM backing, parental consent required for access, minimum retention until age 25, ownership transfer at 18 |
| **Education Sector** | Student record permanence (ARC-021), institutional isolation, belt/certification permanence (ARC-004) |
| **GST Mandate** | 7-year invoice retention (ARC-011), financial record immutability, audit trail for tax authority |

---

## SECTION 20 — ANTI-PATTERNS EXPLICITLY PREVENTED

```
❌ Microservices writing directly to MinIO archive buckets
❌ GD audio storage in any form, any tier, any bucket
❌ Cross-tenant metadata in shared database tables without RLS
❌ Mutable audit logs
❌ Unencrypted backups (including cross-region replicas)
❌ Single-region COMPLIANCE data (regulatory DR failure risk)
❌ Unlimited retention without classification (cost + privacy risk)
❌ Silent integrity failures (data corruption served as valid)
❌ Single-admin legal hold operations (authority abuse risk)
❌ Feature-flag-controlled immutability (must be structural, not policy)
❌ Shared encryption keys across tenants
❌ Presigned URLs with expiry > 15 minutes
❌ Cache-based MFA acceptance for legal hold operations
❌ Partial write success (Trust Envelope failure = full rollback)
❌ Tenant offboarding without export confirmation from tenant
```

---

```
═══════════════════════════════════════════════════════════════
DOCUMENT STATUS      : SEALED
MUTATION AUTHORITY   : VERSION BUMP ONLY
LAST SEALED BY       : ECOSKILLER PLATFORM ARCHITECTURE
VERSION              : 1.0.0
INVARIANTS LOCKED    : 10/10
API SURFACE LOCKED   : YES
TRUST CHAIN LOCKED   : YES
COMPLIANCE MAPPED    : YES
ANTI-PATTERNS SEALED : YES
═══════════════════════════════════════════════════════════════

HISTORICAL_DATA_ARCHIVE_AGENT v1.0.0 — ANTIGRAVITY LAYER
ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
ECOSKILLER / DOJO SAAS — PRODUCTION BLUEPRINT
STATUS: FINAL · LOCKED · GOVERNED · DETERMINISTIC
═══════════════════════════════════════════════════════════════
```
