# CLOUD_DATA_STORAGE_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Ecoskiller Platform — Antigravity Edition
### Classification: SEALED · LOCKED · PRODUCTION-GRADE

---

> **AGENT IDENTITY**
> This agent is not a database wrapper.
> It is not a file uploader.
> It is the **permanent memory of the Ecoskiller ecosystem** —
> the layer that ensures every byte stored is governed, versioned, tenant-isolated, immutable where required, and auditable without exception.
> It enforces trust at the infrastructure level, not the application level.

---

## 0. AGENT PURPOSE — LOCKED DEFINITION

The `CLOUD_DATA_STORAGE_AGENT` is a sovereign infrastructure agent responsible for:

1. **Unified data placement** — deciding WHERE each artifact class lives (object store, block, DB, WORM archive)
2. **Tenant isolation enforcement** — zero cross-tenant data bleed at the storage layer
3. **Immutability governance** — WORM locks for legal, billing, scoring, and GD audit records
4. **Lifecycle orchestration** — hot → warm → cold → glacier → destruction pipelines
5. **Trust chain maintenance** — cryptographic integrity verification for every stored artifact
6. **Compliance posture** — DPDP (India), GDPR, SOC2-aligned retention and deletion controls
7. **Disaster recovery contract** — enforced RPO/RTO per data class
8. **Anti-tamper audit trail** — immutable event log for every write, update, delete

This agent operates **below** application microservices. It exposes a governed API surface. No service writes to storage directly without passing through this agent's policy layer.

---

## 1. SCOPE — WHAT THIS AGENT GOVERNS

| Data Class | Examples | Storage Tier |
|---|---|---|
| **Identity Artifacts** | Profile photos, KYC docs, government ID scans | MinIO — encrypted, tenant-prefixed |
| **Resume & Portfolio** | Resume PDFs, certificates, portfolio files | MinIO — versioned, WORM-eligible |
| **GD Session Artifacts** | Session metadata, mute logs, turn logs, scores | PostgreSQL + ClickHouse + MinIO (logs) |
| **Interview Artifacts** | Slot records, tokens, recruiter notes | PostgreSQL |
| **Dojo Artifacts** | Match states, scenario files, belt records | PostgreSQL + MinIO |
| **Billing & Invoice** | Invoice PDFs, payment receipts, GST records | MinIO — WORM-locked, 7-year retention |
| **Intelligence DNA** | 8-vector profiles, test results, trajectories | PostgreSQL + ClickHouse |
| **Innovation Submissions** | Idea text, drawings, attachments, timestamps | MinIO + Vector DB + Immutable Archive |
| **Royalty Ledger** | Double-entry ledger, revenue ingestion records | PostgreSQL — append-only |
| **Legal Documents** | Contracts, consent forms, digital signatures | Immutable Archive — 15-year WORM |
| **Audit Logs** | Every agent action, every API mutation | Loki + MinIO — append-only |
| **ML Features & Models** | Feature store exports, model weights, embeddings | MinIO + Model Registry |
| **System Backups** | Kubernetes state, DB snapshots, volume backups | Velero → Object Store |
| **Email/SMS Artifacts** | Notification templates, delivery receipts | PostgreSQL + MinIO |

---

## 2. STORAGE LAYER ARCHITECTURE — LOCKED

```
┌─────────────────────────────────────────────────────────────────┐
│                   CLOUD_DATA_STORAGE_AGENT                       │
│                   Policy · Routing · Governance                  │
└──────────┬──────────────┬───────────────┬────────────┬──────────┘
           │              │               │            │
    ┌──────▼──────┐ ┌─────▼──────┐ ┌────▼─────┐ ┌───▼────────┐
    │   MinIO     │ │ PostgreSQL │ │ClickHouse│ │  Immutable │
    │ Object Store│ │ Transact.  │ │Analytics │ │  Archive   │
    │ (Primary)   │ │ Primary DB │ │  Store   │ │  (WORM)    │
    └──────┬──────┘ └─────┬──────┘ └────┬─────┘ └───┬────────┘
           │              │               │            │
    ┌──────▼──────────────▼───────────────▼────────────▼────────┐
    │                    etcd  (Coordination)                     │
    │              Redis (State / Locks / Timers)                 │
    │              Velero (Backup Orchestration)                   │
    └─────────────────────────────────────────────────────────────┘
```

### 2.1 MinIO — Primary Object Store

**Role:** Stores all binary artifacts — resumes, invoices, certificates, idea attachments, ML models, logs.

**Configuration (Non-Negotiable):**
- Erasure coding: minimum 4+2 (4 data shards, 2 parity)
- Server-side encryption: AES-256 per object
- Bucket naming: `ecoskiller-{tenant_id}-{data_class}` (strict schema)
- Versioning: ENABLED on all buckets
- Object locking: ENABLED on legal, billing, royalty, GD audit buckets
- Retention mode: GOVERNANCE (admin override with MFA) or COMPLIANCE (no override) per class
- Access: IAM roles only — no root credentials in application layer
- Lifecycle rules: automated per data class (see Section 6)

**Bucket Schema — Locked:**

```
ecoskiller-{tenant}-identity/          → KYC, photos
ecoskiller-{tenant}-resumes/           → resume versions
ecoskiller-{tenant}-certificates/      → belt certs, completion docs
ecoskiller-{tenant}-invoices/          → billing PDFs (COMPLIANCE lock)
ecoskiller-{tenant}-gd-logs/           → session logs (GOVERNANCE lock)
ecoskiller-{tenant}-idea-assets/       → innovation attachments
ecoskiller-{tenant}-legal/             → contracts, consents (COMPLIANCE lock)
ecoskiller-{tenant}-ml-models/         → model weights, embeddings
ecoskiller-{tenant}-backups/           → system backups
ecoskiller-platform-audit/             → cross-tenant audit trail (COMPLIANCE)
```

### 2.2 PostgreSQL — Transactional Primary

**Role:** All structured, relational, ACID-required data. The source of truth for every entity in the system.

**Configuration:**
- Row-Level Security (RLS): ENFORCED — every table has `tenant_id` predicate
- Connection pooling: PgBouncer (transaction mode)
- Replication: streaming replication with at least 1 hot standby
- WAL archiving: continuous to MinIO (point-in-time recovery)
- Schema migrations: Flyway — versioned, audited, no manual DDL in prod
- Encryption at rest: filesystem-level AES-256
- Encryption in transit: TLS 1.3 enforced

**Critical Tables — Append-Only Enforcement:**

| Table | Policy |
|---|---|
| `gd_session_logs` | INSERT only — no UPDATE, no DELETE |
| `royalty_ledger` | INSERT only — double-entry, never mutated |
| `invoice_records` | INSERT only — amendments via new rows |
| `scoring_audit` | INSERT only — immutable after write |
| `legal_consent_log` | INSERT only — WORM semantics in DB |
| `belt_issuance_log` | INSERT only — belt grants never revoked via mutation |

### 2.3 ClickHouse — Analytics Store

**Role:** High-volume, append-only analytical data. GD metrics, speaking time distributions, scoring analytics, dojo performance, funnel data, royalty computations.

**Configuration:**
- Sharding: by `tenant_id` + time partition
- Replication: ZooKeeper-backed, 2 replicas minimum
- Compression: LZ4 (default) + ZSTD for cold partitions
- TTL policies: per table, aligned with retention schedule
- No UPDATE / DELETE in production pipelines — use `ReplacingMergeTree` for corrections
- Access: read-only for dashboards, insert-only for pipeline writers

### 2.4 Immutable Archive — Legal/WORM Tier

**Role:** Long-term WORM storage for legal documents, royalty agreements, innovation timestamps, consent records. 15+ year retention guaranteed.

**Implementation Options (choose per deployment):**
- MinIO in COMPLIANCE lock mode (preferred for self-hosted)
- AWS S3 Object Lock (COMPLIANCE) for cloud deployments
- Wasabi S3-compatible (cost-optimized)

**Requirements:**
- Object lock: COMPLIANCE mode (no override, not even admin)
- Cryptographic hash stored separately from object (SHA-256 in PostgreSQL)
- Annual integrity verification job (automated via Airflow)
- Offline backup copy: required for >5-year retention class

### 2.5 Redis — State & Coordination Layer

**Role:** Ephemeral but critical — GD state machines, interview timers, OTP TTLs, slot locks, rate limit counters, feature flag state.

**Configuration:**
- Persistence: RDB + AOF (both enabled for crash safety)
- Eviction: `allkeys-lru` for cache namespaces; `noeviction` for GD/interview state namespaces
- Key namespacing (strict):
  ```
  gd:{session_id}:state
  gd:{session_id}:turn
  interview:{slot_id}:lock
  otp:{user_id}:{purpose}
  ratelimit:{ip}:{endpoint}
  feature:{tenant_id}:{flag}
  ```
- Sentinel or Cluster: mandatory for production
- TLS: enforced in-transit

### 2.6 etcd — Distributed Coordination

**Role:** Strong-consistency distributed locking for slot booking, GD room creation, billing state transitions, migration locks.

**Rules:**
- Never used for application data
- Lease-based locks only — all leases have TTL
- Accessed only via Storage Agent internal layer, never directly by microservices

---

## 3. TENANT ISOLATION — ZERO-BLEED CONTRACT

Every storage operation in the system passes through the following isolation enforcement:

```
REQUEST
  │
  ▼
[Storage Agent Policy Layer]
  │
  ├── Extract tenant_id from JWT (verified)
  ├── Validate tenant_id matches resource path
  ├── Inject RLS predicate into DB query
  ├── Validate MinIO bucket prefix matches tenant_id
  ├── Reject any cross-tenant path traversal attempt
  ├── Log enforcement decision to audit trail
  │
  ▼
STORAGE OPERATION EXECUTED
```

**Non-negotiable rules:**
- No wildcard bucket access — all MinIO policies are tenant-scoped
- No shared database tables without `tenant_id` column + RLS policy
- No cross-tenant query possible even with admin service tokens
- Tenant deletion: cascaded across all storage tiers via agent-orchestrated pipeline (not manual)
- Tenant suspension: read-only mode enforced at agent level

---

## 4. DATA CLASSIFICATION MATRIX — LOCKED

| Class | Encryption | Versioning | WORM Lock | Retention | Backup Frequency |
|---|---|---|---|---|---|
| KYC / Identity | AES-256 + field-level | Yes | No | 7 years post-deletion | Daily |
| Resumes | AES-256 | Yes | No | Duration of account + 2 years | Daily |
| GD Session Logs | AES-256 | Yes | GOVERNANCE | 5 years | Hourly |
| GD Scores | AES-256 | Immutable row | GOVERNANCE | 5 years | Continuous |
| Interview Records | AES-256 | Yes | No | 3 years | Daily |
| Dojo Match Logs | AES-256 | Yes | No | 3 years | Daily |
| Belt Records | AES-256 | Yes | COMPLIANCE | Permanent | Continuous |
| Invoices | AES-256 | Yes | COMPLIANCE | 7 years (GST mandate) | Continuous |
| Royalty Ledger | AES-256 | Append-only | COMPLIANCE | 15 years | Continuous |
| Innovation Submissions | AES-256 | Yes | COMPLIANCE | 15 years | Daily |
| Legal Contracts | AES-256 | Yes | COMPLIANCE | 15 years | Daily |
| Consent Records | AES-256 | Append-only | COMPLIANCE | 18 years (minor protection) | Daily |
| ML Models | AES-256 | Yes | No | Version-controlled | Weekly |
| Audit Logs | AES-256 | Append-only | COMPLIANCE | 7 years | Continuous |
| System Backups | AES-256 | Yes | No | 90 days rolling | Per schedule |

---

## 5. TRUST CHAIN — CRYPTOGRAPHIC INTEGRITY

Every artifact written through the Storage Agent receives a **Trust Envelope**:

```json
{
  "artifact_id": "uuid-v4",
  "tenant_id": "tenant_xyz",
  "data_class": "invoice",
  "storage_path": "ecoskiller-tenant_xyz-invoices/2025/06/inv_abc.pdf",
  "sha256_hash": "e3b0c44298fc1c149afb...",
  "written_at": "2025-06-15T10:30:00Z",
  "written_by_service": "billing-service",
  "written_by_user": "system",
  "lock_mode": "COMPLIANCE",
  "retention_until": "2032-06-15",
  "integrity_verified_at": null,
  "trust_signature": "HMAC-SHA256(secret, artifact_id + sha256_hash + written_at)"
}
```

This envelope is stored in PostgreSQL (`artifact_trust_registry` table — append-only).

**Integrity Verification Pipeline (Airflow — Scheduled):**
- Daily: verify hashes for WORM-locked objects modified in last 24h
- Weekly: spot-check 5% of all active objects
- Monthly: full integrity sweep of legal/royalty/compliance tier
- On anomaly: immediate alert to Wazuh SIEM + freeze affected bucket

---

## 6. LIFECYCLE MANAGEMENT — AUTOMATED

### Object Lifecycle Rules (MinIO / S3)

| Bucket Class | Hot (0–90d) | Warm (90–365d) | Cold (1–3y) | Archive (3y+) | Deletion |
|---|---|---|---|---|---|
| Resumes | Standard | IA tier | Glacier | N/A | On account deletion + 2y |
| GD Logs | Standard | IA tier | Glacier | N/A | 5y hard delete |
| Invoices | Standard | Standard | IA tier | WORM Archive | Never (7y COMPLIANCE) |
| Legal Docs | Standard | Standard | IA tier | WORM Archive | Never (15y COMPLIANCE) |
| ML Models | Standard | IA tier | Delete old | N/A | On model deprecation |
| Backups | Standard | Delete after 90d | — | — | 90d rolling |

### Lifecycle Orchestration (Apache Airflow DAGs — Locked)

```
DAG: storage_lifecycle_manager
  ├── daily_hot_to_warm_transition
  ├── weekly_warm_to_cold_transition  
  ├── monthly_cold_to_archive_transition
  ├── daily_integrity_verification
  ├── daily_backup_validation
  ├── weekly_orphan_object_detection
  └── monthly_retention_policy_audit
```

---

## 7. BACKUP & DISASTER RECOVERY CONTRACT

### RPO / RTO Targets — Non-Negotiable

| Data Class | RPO (Recovery Point) | RTO (Recovery Time) | Method |
|---|---|---|---|
| PostgreSQL (all tenants) | 5 minutes | 30 minutes | WAL streaming + Velero |
| Redis (GD/interview state) | 1 minute | 10 minutes | RDB+AOF restore |
| MinIO (active objects) | 1 hour | 2 hours | Cross-region replication |
| ClickHouse (analytics) | 1 hour | 4 hours | Snapshot restore |
| Immutable Archive | 24 hours | 48 hours | Offline backup |
| etcd (cluster state) | 5 minutes | 15 minutes | Velero snapshot |

### Backup Architecture (Velero-Orchestrated)

```
PRODUCTION CLUSTER
  │
  ▼
Velero Backup Controller
  ├── PostgreSQL: WAL continuous → MinIO backup bucket
  ├── Redis: RDB snapshot every 60s → MinIO backup bucket  
  ├── MinIO volumes: Velero PV snapshot → secondary region
  ├── etcd: Velero cluster snapshot every 5m
  └── ClickHouse: Native backup → MinIO backup bucket
        │
        ▼
  Cross-Region / Secondary Site
  (Oracle Cloud secondary region or separate VPC)
```

### DR Runbook (Sealed)

1. Detect: Prometheus alert → PagerDuty escalation
2. Assess: Wazuh incident classification (data loss vs. corruption vs. outage)
3. Isolate: Affected tenant namespaces quarantined via Storage Agent policy
4. Restore: Velero restore from last verified snapshot
5. Validate: Integrity check against `artifact_trust_registry`
6. Resume: Gradual traffic re-admission per tenant
7. Post-mortem: Immutable incident report written to audit archive

---

## 8. SECURITY POSTURE — STORAGE LAYER

### Encryption

| Layer | Standard |
|---|---|
| In-transit | TLS 1.3 (all storage endpoints) |
| At-rest (MinIO) | AES-256 SSE-S3 per object |
| At-rest (PostgreSQL) | Filesystem encryption + pgcrypto for PII fields |
| At-rest (ClickHouse) | Filesystem encryption |
| At-rest (Redis) | Filesystem encryption + TLS |
| Key management | HashiCorp Vault — storage encryption keys, rotated quarterly |

### Access Control

```
HashiCorp Vault
  │
  ├── Issues short-lived MinIO credentials (15-minute TTL)
  ├── Issues PostgreSQL role tokens per service
  ├── Rotates Redis AUTH passwords (weekly)
  └── Never exposes master keys to application layer

Open Policy Agent
  │
  ├── Storage access policies evaluated per request
  ├── Tenant-scope enforcement at policy layer
  ├── Audit log of every policy evaluation
  └── Policy updates via GitOps only (no live admin edits)
```

### Threat Detection (Wazuh Integration)

Wazuh monitors:
- Unusual object access volume per tenant
- Cross-tenant path probe attempts
- Failed IAM auth on MinIO endpoints
- Database RLS bypass attempts (logged as violation)
- Sudden spike in deletion events
- WORM object modification attempts (impossible — but logged if probed)

All anomalies → immediate Wazuh alert → Storage Agent policy freeze → incident log entry.

---

## 9. AGENT API SURFACE — SEALED CONTRACT

The Storage Agent exposes the following internal API surface to platform microservices. No service accesses storage backends directly.

### 9.1 Write API

```
POST /storage/artifacts
  Body: { tenant_id, data_class, content_type, metadata, payload_ref }
  Returns: { artifact_id, storage_path, sha256_hash, trust_envelope_id }
  
  Rules:
    - tenant_id must match JWT claim
    - data_class must be in approved registry
    - payload routed to correct backend automatically
    - Trust Envelope generated and stored
    - Lifecycle policy applied immediately
```

### 9.2 Read API

```
GET /storage/artifacts/{artifact_id}
  Returns: { presigned_url (TTL: 15min), trust_envelope, integrity_status }
  
  Rules:
    - Tenant isolation enforced
    - Presigned URL — never raw credentials
    - Every read logged to audit trail
    - WORM objects: read-only, no override
```

### 9.3 Delete API

```
DELETE /storage/artifacts/{artifact_id}
  Returns: { scheduled_deletion_at, compliance_hold_active }
  
  Rules:
    - Soft delete only — object marked, not removed
    - COMPLIANCE-locked objects: delete request rejected with 409
    - GOVERNANCE-locked objects: require MFA-elevated token
    - Actual deletion only via lifecycle pipeline
    - Deletion event written to immutable audit log
```

### 9.4 Integrity Verification API

```
GET /storage/artifacts/{artifact_id}/verify
  Returns: { hash_match: bool, worm_status, last_verified_at, chain_intact: bool }
  
  Use: Legal teams, compliance audits, dispute resolution
```

### 9.5 Tenant Offboarding API

```
POST /storage/tenants/{tenant_id}/offboard
  Body: { reason, initiated_by, legal_hold: bool }
  Returns: { pipeline_id, estimated_completion, compliance_hold_list }
  
  Rules:
    - Cascades across all storage tiers
    - COMPLIANCE-locked objects: placed in legal hold, not deleted
    - Full audit trail written
    - Irreversible after confirmation step
```

---

## 10. OBSERVABILITY — STORAGE AGENT

### Prometheus Metrics (Mandatory)

```
storage_write_duration_ms{data_class, tenant_id}
storage_read_duration_ms{data_class, tenant_id}
storage_object_count{bucket, tenant_id}
storage_bytes_total{bucket, tenant_id}
storage_integrity_failures_total{data_class}
storage_worm_probe_attempts_total{bucket}
storage_lifecycle_transitions_total{from_tier, to_tier}
storage_backup_lag_seconds{component}
storage_rpo_breach_total{component}
```

### Grafana Dashboards

- **Storage Health Overview** — bytes, objects, write/read latency per tier
- **Tenant Storage Distribution** — per-tenant usage, growth rate, quota status
- **Integrity & Compliance** — WORM status, hash verification pass/fail rate
- **Backup & DR Status** — last successful backup per component, RPO adherence
- **Security Events** — access anomalies, policy violations, probe attempts

### Loki Log Streams

```
{agent="cloud-data-storage-agent", event="write"}
{agent="cloud-data-storage-agent", event="read"}
{agent="cloud-data-storage-agent", event="delete"}
{agent="cloud-data-storage-agent", event="integrity_check"}
{agent="cloud-data-storage-agent", event="policy_violation"}
{agent="cloud-data-storage-agent", event="worm_probe"}
{agent="cloud-data-storage-agent", event="backup"}
{agent="cloud-data-storage-agent", event="lifecycle_transition"}
```

---

## 11. GD-SPECIFIC STORAGE CONTRACT

The Voice GD System generates high-frequency, forensic-grade storage requirements. This section seals those contracts.

### What is Stored (Non-Negotiable)

```
gd_sessions
  session_id          UUID
  tenant_id           UUID
  topic_id            UUID
  batch_id            UUID
  room_name           TEXT
  scheduled_at        TIMESTAMPTZ
  started_at          TIMESTAMPTZ
  ended_at            TIMESTAMPTZ
  participant_count   INT
  status              ENUM(scheduled, active, completed, failed)

gd_participants
  session_id          UUID (FK)
  user_id             UUID
  join_order          INT
  join_time           TIMESTAMPTZ
  exit_time           TIMESTAMPTZ
  spectator           BOOLEAN

gd_turn_logs          ← APPEND-ONLY, GOVERNANCE LOCK
  session_id          UUID
  user_id             UUID
  round_type          ENUM(intro, rebuttal, open, conclusion)
  turn_index          INT
  token_granted_at    TIMESTAMPTZ
  token_expired_at    TIMESTAMPTZ
  mic_open_seconds    DECIMAL
  spoke              BOOLEAN
  silence_detected    BOOLEAN
  interrupt_attempts  INT
  network_drops       INT

gd_scores             ← APPEND-ONLY, GOVERNANCE LOCK
  session_id          UUID
  user_id             UUID
  turns_completed     INT
  turns_skipped       INT
  total_mic_seconds   DECIMAL
  interrupt_penalty   INT
  participation_score DECIMAL
  computed_at         TIMESTAMPTZ
  formula_version     TEXT
```

### What is Never Stored

- Voice recordings
- Audio waveforms
- Speech content
- Transcriptions
- Tone, emotion, accent data

This is structurally enforced: the GD Orchestrator has no write path to audio storage. The Storage Agent's data class registry rejects any `audio/*` content type from the GD namespace.

---

## 12. INNOVATION & ROYALTY STORAGE CONTRACT

### Idea Immutability (Sealed)

Every submitted idea receives:
1. SHA-256 hash of content at submission time
2. Timestamp written to PostgreSQL (append-only)
3. Attachment files written to MinIO with COMPLIANCE lock
4. Trust Envelope generated
5. Blockchain-style chain hash: `hash(idea_hash + previous_chain_hash)` stored in `innovation_chain_log`

This creates an **unforgeable invention priority record** — critical for royalty and anti-theft claims.

### Royalty Ledger (Double-Entry, Append-Only)

```
royalty_ledger          ← APPEND-ONLY, COMPLIANCE LOCK
  entry_id            UUID
  tenant_id           UUID (licensor business)
  innovator_id        UUID
  entry_type          ENUM(revenue_ingestion, royalty_accrual, royalty_payout, adjustment)
  debit_account       TEXT
  credit_account      TEXT
  amount              DECIMAL(15,4)
  currency            TEXT
  reference_id        UUID
  period_start        DATE
  period_end          DATE
  created_at          TIMESTAMPTZ
  created_by_service  TEXT
```

No UPDATE. No DELETE. Corrections via new reversal entries only.

---

## 13. DEPLOYMENT SPECIFICATION

### Kubernetes Namespace: `storage-agent`

```yaml
Deployments:
  - cloud-data-storage-agent     # Main API + policy layer
  - storage-integrity-worker      # Async integrity verification
  - storage-lifecycle-worker      # Lifecycle transition executor
  - backup-validation-worker      # Backup health checks

ConfigMaps:
  - storage-policy-registry       # Data class definitions
  - lifecycle-rules-config        # Tier transition rules
  - retention-schedule-config     # Per-class retention

Secrets (via Vault):
  - minio-service-credentials
  - postgres-storage-agent-role
  - redis-auth-token
  - hmac-trust-signature-key
  - encryption-key-refs

Resource Limits (per pod):
  storage-agent:        500m CPU, 512Mi RAM
  integrity-worker:     250m CPU, 256Mi RAM
  lifecycle-worker:     250m CPU, 256Mi RAM
```

### Infrastructure Dependencies

| Component | Version Pin | Justification |
|---|---|---|
| MinIO | RELEASE.2024+ | Object locking + versioning stable |
| PostgreSQL | 16.x | RLS performance + logical replication |
| Redis | 7.x | ACL + TLS native |
| ClickHouse | 24.x | Materialized view performance |
| Velero | 1.13+ | PV snapshot stability |
| HashiCorp Vault | 1.15+ | KV v2 + dynamic secrets |
| Open Policy Agent | 0.68+ | Rego policy stability |

---

## 14. FAILURE MODES & AGENT RESPONSES

| Failure | Agent Response |
|---|---|
| MinIO unreachable | Queue write to Redis buffer (max 60s) → retry → alert |
| PostgreSQL primary down | Route to standby (read) → writes queued → failover alert |
| Redis unavailable | GD sessions frozen (safe state) → Orchestrator notified → resume on reconnect |
| Integrity hash mismatch | Object quarantined → Wazuh alert → manual review required |
| WORM probe attempt | Request rejected 409 → audit log → Wazuh critical alert |
| Backup lag > RPO | PagerDuty alert → backup pipeline restarted → incident opened |
| Tenant isolation breach attempt | Request rejected 403 → Wazuh critical → service token revoked |
| Vault unreachable | Storage Agent enters read-only safe mode → alert immediately |

---

## 15. FINAL SEAL — AGENT INVARIANTS

These invariants are **never violated under any circumstance**:

```
INVARIANT 1:  No microservice writes to any storage backend directly.
              All writes pass through Storage Agent policy layer.

INVARIANT 2:  No COMPLIANCE-locked object is ever mutated or deleted
              by any code path, including admin and emergency paths.

INVARIANT 3:  No cross-tenant data access is possible at the storage layer,
              regardless of service token scope.

INVARIANT 4:  Every write generates a Trust Envelope. No exception.

INVARIANT 5:  GD session logs are append-only from the moment of creation.
              No correction, update, or deletion is architecturally possible.

INVARIANT 6:  Royalty ledger entries are append-only and COMPLIANCE-locked.
              Double-entry structure enforced by schema constraints.

INVARIANT 7:  Audio data from GD sessions is never written to any storage tier.
              The Storage Agent data class registry structurally prevents it.

INVARIANT 8:  All encryption keys are held in Vault.
              No service ever holds a long-lived storage credential.

INVARIANT 9:  Backup validation runs on every backup.
              An unvalidated backup is treated as no backup.

INVARIANT 10: The Storage Agent itself is fully observable.
              Every policy decision, every write, every failure is logged
              to an append-only audit stream.
```

---

*CLOUD_DATA_STORAGE_AGENT · Ecoskiller Antigravity Platform*
*Enterprise Optimization + Trust Infrastructure*
*Document Status: SEALED · LOCKED · Version 1.0*
*Classification: Internal Architecture — Restricted Distribution*
