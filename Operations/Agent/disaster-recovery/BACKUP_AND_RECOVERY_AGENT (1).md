# BACKUP_AND_RECOVERY_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER PLATFORM — ANTIGRAVITY SUBSYSTEM

---

```
DOCUMENT CLASS     : ENTERPRISE INFRASTRUCTURE AGENT SPEC
AGENT ID           : BRA-001
VERSION            : v1.0.0 — SEALED
STATUS             : LOCKED · GOVERNED · DETERMINISTIC
MUTATION POLICY    : ADD-ONLY VIA VERSION BUMP
EXECUTION AUTHORITY: HUMAN DECLARATION ONLY
INTERPRETATION     : NONE PERMITTED
FAILURE MODE       : STOP → REPORT → NO PARTIAL EXECUTION
CLASSIFICATION     : TRUST INFRASTRUCTURE — NON-OPTIONAL
```

---

## SECTION 0 — AGENT DECLARATION

This document defines the **BACKUP_AND_RECOVERY_AGENT (BRA)** — a fully deterministic,
protocol-governed backup and recovery system for the Ecoskiller platform operating under
the ANTIGRAVITY Enterprise Optimization + Trust Infrastructure layer.

**The BRA is not optional. It is not advisory. It is enforcement infrastructure.**

Absence of BRA implementation → `STOP EXECUTION`  
Partial BRA implementation → `TRUST INFRASTRUCTURE FAILURE`  
BRA deviation from this spec → `COMPLIANCE VIOLATION`

---

## SECTION 1 — SCOPE DECLARATION (NON-NEGOTIABLE)

The BRA governs backup and recovery across every data layer, service, and stateful component
of the Ecoskiller platform. No data category is excluded. No service is exempt.

### 1.1 Protected Data Domains

| Domain | Data Assets | Criticality |
|---|---|---|
| Identity & Auth | Users, sessions, RBAC, MFA state, OAuth tokens | CRITICAL |
| Job Portal | Job listings, applications, pipeline stages, offers | CRITICAL |
| Voice GD | Session metadata, turn logs, scores, participant maps | CRITICAL |
| Dojo System | Match records, scenarios, belts, ratings, replays | CRITICAL |
| Intelligence Engine | Intelligence DNA vectors, test history, ML models | CRITICAL |
| Innovation Engine | Idea registry, fingerprints, similarity scores, IP timestamps | CRITICAL |
| Royalty & Licensing | Contracts, royalty ledger, wallet balances, revenue ingestion | LEGAL-CRITICAL |
| Billing & Subscriptions | Plans, invoices, refunds, GST/VAT records | LEGAL-CRITICAL |
| Tenant Configuration | Tenant bindings, row-level security policies, domain maps | CRITICAL |
| Audit Logs | Immutable event history, compliance trails | IMMUTABLE |
| Media & Files | Resumes, certificates, invoices, audit files (MinIO) | HIGH |
| Notification History | Email, SMS, push delivery logs | MEDIUM |
| Analytics Data | GD metrics, scoring distributions, funnel data (ClickHouse) | HIGH |
| Search Indices | Candidate discovery, job search, recruiter filters (OpenSearch) | HIGH |
| Kubernetes State | Cluster configuration, Helm releases, secrets | CRITICAL |
| Legal Documents | Contracts, digital signatures, WORM-archived docs | LEGAL-CRITICAL |

---

## SECTION 2 — AGENT IDENTITY AND PLACEMENT

### 2.1 Architectural Position

```
┌─────────────────────────────────────────────────────────────────┐
│              ENTERPRISE OPTIMIZATION + TRUST LAYER              │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────────────────┐ │
│  │  Audit Trail │  │  Compliance  │  │  BACKUP_AND_RECOVERY  │ │
│  │  Engine      │  │  Engine      │  │  AGENT (BRA-001)      │ │
│  └──────────────┘  └──────────────┘  └───────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                            │
         ┌──────────────────┼──────────────────┐
         ▼                  ▼                  ▼
   Data Layer         Media Layer        Cluster Layer
  (PostgreSQL,      (MinIO, Legal       (Kubernetes,
   Redis, CH,        Archive,            etcd, Helm,
   OpenSearch,       Signatures)         Secrets)
   ClickHouse)
```

### 2.2 Namespace Assignment

```
kubernetes namespace: ops
service name:         backup-recovery-agent
deployment type:      CronJob + Daemon
sidecar:              velero
state store:          Redis (backup state machine)
audit sink:           PostgreSQL (immutable backup_audit_logs table)
alert channel:        Prometheus → Grafana → ntfy
```

### 2.3 Non-Negotiable Dependencies

```
Velero            — Kubernetes cluster + PV backup and restore
MinIO             — Primary object storage for all backup archives
PostgreSQL        — Source + metadata destination
Redis             — Backup orchestration state machine
ClickHouse        — Analytics data snapshot target
OpenSearch        — Index snapshot target
HashiCorp Vault   — Encryption key management for all backup archives
Prometheus        — Backup job metric emission
Grafana           — Backup health dashboards
Wazuh             — Intrusion detection on backup access
Apache Airflow    — Scheduled backup workflow orchestration
```

---

## SECTION 3 — BACKUP CLASSIFICATION MATRIX (LOCKED)

### 3.1 Recovery Tier Definitions

| Tier | Name | RTO | RPO | Use Case |
|---|---|---|---|---|
| T0 | HOT — Continuous | < 1 min | 0 sec | Active GD sessions, wallet balances |
| T1 | WARM — Near Real-Time | < 15 min | < 5 min | Core transactional databases |
| T2 | COLD — Scheduled | < 4 hours | < 1 hour | Analytics, search indices, media |
| T3 | ARCHIVE — Long-Term | < 24 hours | < 24 hours | Legal docs, audit logs, royalty ledger |
| T4 | DR — Disaster Recovery | < 48 hours | < 1 hour | Full cluster rebuild |

### 3.2 Data Store → Tier Assignment

| Data Store | Tier | Method | Retention |
|---|---|---|---|
| PostgreSQL (core) | T1 | WAL streaming + pg_basebackup | 90 days hot + 7 years cold |
| PostgreSQL (billing) | T1 | WAL streaming | 7 years (legal) |
| PostgreSQL (royalty ledger) | T0 | Continuous WAL + logical replication | 15 years (legal) |
| Redis (GD state machines) | T0 | AOF + RDB snapshot | 7 days |
| Redis (timers + OTPs) | T1 | RDB snapshot | 3 days |
| ClickHouse | T2 | Native incremental snapshot | 2 years |
| OpenSearch | T2 | Index snapshot API | 90 days |
| MinIO (user files) | T2 | Bucket versioning + replication | 7 years |
| MinIO (legal archive) | T3 | WORM + immutable lock | 15+ years |
| etcd (cluster state) | T1 | etcdctl snapshot | 30 days |
| Kubernetes manifests | T1 | Velero + Helm values | 365 days |
| Vault secrets | T1 | Vault snapshot + seal wrapping | 365 days |
| ML models (registry) | T2 | MinIO versioned bucket | Per model version |
| Vector embeddings | T2 | Snapshot + export | 90 days |

---

## SECTION 4 — BACKUP EXECUTION PROTOCOLS

### 4.1 PostgreSQL Backup Protocol

**Method:** WAL-G with continuous archiving to MinIO

```
BACKUP_TOOL        = WAL-G
ARCHIVE_TARGET     = MinIO bucket: ecoskiller-pg-wal-{env}
COMPRESSION        = LZ4 (speed) + ZSTD (archive)
ENCRYPTION         = AES-256 via Vault-managed key
FULL_BACKUP_SCHEDULE = Daily at 02:00 UTC
INCREMENTAL        = WAL segments every 5 minutes
RETENTION_HOT      = 90 days full backups
RETENTION_COLD     = 7 years (billing, royalty)
VERIFY_ON_WRITE    = MANDATORY — checksum validated
```

**Multi-Tenant Isolation Rule:**
```
Each tenant's data lives in isolated PostgreSQL schema.
Backup metadata must tag: tenant_id, schema_version, data_class.
Cross-tenant restore is FORBIDDEN without dual-admin approval.
```

**Royalty Ledger Special Rule:**
```
Royalty database backup uses double-entry audit tagging.
Every backup archive references the last verified ledger hash.
Archive is WORM-locked after 24 hours.
Retention: 15 years minimum (legal obligation per licensing contract terms).
```

### 4.2 Redis Backup Protocol

**GD Orchestration State (T0 — Critical Path):**
```
MODE               = AOF + RDB hybrid
AOF_FSYNC          = everysec
RDB_SCHEDULE       = Every 60 seconds during active GD sessions
SNAPSHOT_TARGET    = MinIO bucket: ecoskiller-redis-gd-{env}
REPLICATION        = Redis Sentinel (3 nodes minimum)
FAILOVER_TIMEOUT   = 10 seconds
GD_STATE_FLUSH     = On session completion → PostgreSQL permanent store
```

**General Redis (T1):**
```
MODE               = RDB
RDB_SCHEDULE       = Every 15 minutes
RETENTION          = 7 days
TARGET             = MinIO bucket: ecoskiller-redis-state-{env}
```

**Recovery Guarantee for GD:**
```
If Redis fails mid-session:
  1. Backend reads last committed state from PostgreSQL checkpoint
  2. Session is NOT restarted — current round is abandoned
  3. Completed turns are preserved from PostgreSQL log
  4. Participants are notified of session termination
  5. Partial scores are flagged as INCOMPLETE — not discarded
  Rationale: GD protocol is deterministic. Incomplete sessions
  produce partial-but-valid numeric data.
```

### 4.3 MinIO Backup Protocol

**Bucket Versioning (All Buckets — Mandatory):**
```
VERSIONING         = ENABLED on all buckets
DELETE_PROTECTION  = Object lock on legal and audit buckets
REPLICATION        = Cross-region replication to secondary MinIO
ENCRYPTION         = SSE-S3 with Vault-managed keys
INTEGRITY_CHECK    = MD5 + SHA-256 checksum on every object write
```

**Bucket Classification:**

| Bucket | Lock Policy | Retention |
|---|---|---|
| ecoskiller-resumes | Soft delete + versioning | 7 years |
| ecoskiller-certificates | Immutable after issue | 15 years |
| ecoskiller-invoices | WORM (legal) | 7 years |
| ecoskiller-audit-files | WORM (compliance) | 7 years |
| ecoskiller-legal-archive | WORM + dual-key encryption | 15+ years |
| ecoskiller-idea-registry | Immutable after timestamp | 15 years |
| ecoskiller-ml-models | Versioned | Per version lifecycle |
| ecoskiller-pg-wal | Rolling | 90 days hot |
| ecoskiller-velero | Rolling | 365 days |

**Idea Registry Special Rule:**
```
Innovation ideas are timestamped at submission.
MinIO object receives:
  - creation timestamp (immutable)
  - idea_fingerprint hash
  - similarity_score_at_submission
  - WORM lock: applied immediately on submission
  - Retention: 15 years (IP protection obligation)
This guarantees anti-theft evidence chain.
```

### 4.4 ClickHouse Backup Protocol

```
METHOD             = Native ClickHouse BACKUP command
SCHEDULE           = Daily at 03:00 UTC (incremental), Weekly full
TARGET             = MinIO bucket: ecoskiller-clickhouse-{env}
COMPRESSION        = LZ4
RETENTION          = 2 years
DATA_SCOPE         :
  - GD metrics (speaking time, scoring distributions)
  - Interview no-show analytics
  - Dojo performance data
  - Billing funnel analytics
  - Intelligence score progression
RESTORE_TEST       = Monthly automated restore-and-diff
```

### 4.5 OpenSearch Backup Protocol

```
METHOD             = Snapshot API → MinIO repository plugin
SCHEDULE           = Every 6 hours incremental, daily full
TARGET             = MinIO bucket: ecoskiller-opensearch-{env}
RETENTION          = 90 days
INDICES            :
  - candidates
  - jobs
  - recruiters
  - dojo-scenarios
  - innovation-ideas
RESTORE_TEST       = Weekly automated restore to staging namespace
```

### 4.6 Kubernetes Cluster Backup Protocol

**Velero Configuration (Mandatory):**
```
VELERO_PROVIDER    = MinIO (S3-compatible)
BACKUP_TARGET      = MinIO bucket: ecoskiller-velero-{env}
SCHEDULE           = Every 6 hours (namespaced resources)
FULL_CLUSTER       = Daily at 01:00 UTC
NAMESPACES         :
  - core
  - realtime
  - media
  - billing
  - analytics
  - admin
  - ops
INCLUDE_PV         = true
INCLUDE_SECRETS    = true (encrypted at rest via Vault)
RETENTION          = 365 days
```

**etcd Backup:**
```
METHOD             = etcdctl snapshot save
SCHEDULE           = Every 30 minutes
TARGET             = MinIO bucket: ecoskiller-etcd-{env}
ENCRYPTION         = Vault-wrapped AES-256
RETENTION          = 30 days
RESTORE_TEST       = Monthly
```

**Helm State Backup:**
```
All Helm values files are version-controlled in Git (GitLab).
GitLab CI enforces: no production deploy without Helm values committed.
Additional snapshot: Helm state exported to MinIO daily.
```

### 4.7 HashiCorp Vault Backup Protocol

```
METHOD             = vault operator raft snapshot save
SCHEDULE           = Every 4 hours
TARGET             = MinIO bucket: ecoskiller-vault-{env}
ENCRYPTION         = Shamir secret shares (5 keys, 3 required)
SEAL_TYPE          = Auto-unseal via cloud KMS (emergency fallback)
RETENTION          = 365 days
AUDIT_LOG          = Every snapshot operation logged to immutable audit table
CRITICAL_RULE      : Vault backup keys must be held by 3 separate humans.
                    No single operator holds restore capability alone.
```

---

## SECTION 5 — RECOVERY EXECUTION PROTOCOLS

### 5.1 Recovery Classification

| Failure Class | Trigger | Protocol |
|---|---|---|
| REC-01 | Single service crash | Auto-restart via Kubernetes (no BRA involvement) |
| REC-02 | Data corruption (single table) | Table-level restore from WAL |
| REC-03 | Database server failure | Warm standby promotion + WAL replay |
| REC-04 | Tenant data loss | Isolated tenant schema restore |
| REC-05 | Full namespace failure | Velero namespace restore |
| REC-06 | Cluster total failure | Full DR protocol |
| REC-07 | Legal data loss (royalty, contracts) | WORM archive restore + legal notification |
| REC-08 | Backup store compromise | Vault key rotation + fresh backup from replica |
| REC-09 | Active GD session failure | GD-specific recovery (Section 5.5) |
| REC-10 | Intelligence DNA corruption | Point-in-time restore + recalculation trigger |

### 5.2 PostgreSQL Recovery Protocol

**Single Table Recovery (REC-02):**
```
Step 1: Identify corruption timestamp from audit log
Step 2: Locate WAL segment containing last valid state
Step 3: Restore to isolated recovery namespace
Step 4: Verify row count + checksum against backup metadata
Step 5: Apply logical dump to production schema
Step 6: Run integrity assertion tests
Step 7: Log recovery event to immutable audit table
Step 8: Notify platform admins
```

**Server Failure Recovery (REC-03):**
```
Step 1: Promote PostgreSQL warm standby replica (< 60 sec automatic)
Step 2: Verify replication lag was < 5 minutes at failure
Step 3: If lag > 5 minutes: initiate WAL replay from last valid checkpoint
Step 4: Run full integrity check on promoted primary
Step 5: Update connection pool (PgBouncer) to new primary
Step 6: Trigger service health checks across all microservices
Step 7: Alert operations team
RTO Target: < 15 minutes
```

**Royalty Ledger Recovery (Special Protocol):**
```
Step 1: Restore from WORM-locked archive (immutable)
Step 2: Verify double-entry ledger balance (debit = credit)
Step 3: Cross-reference against revenue ingestion gateway logs
Step 4: Generate ledger reconciliation report
Step 5: Legal team notification mandatory within 1 hour of royalty data incident
Step 6: Regulatory compliance flag raised if data was inaccessible > 4 hours
```

### 5.3 GD Session Recovery Protocol (REC-09)

The Voice GD system is deterministic and stateful. Recovery must preserve this determinism.

```
SCENARIO: Redis GD state machine failure during active session

State Recovery Priority:
  1. PostgreSQL checkpoint (written every completed turn) — PRIMARY SOURCE
  2. Redis AOF log (if partially intact) — SECONDARY
  3. WebSocket command log (timestamped) — TERTIARY

Recovery Steps:
  Step 1: Detect failure via session health monitor (heartbeat timeout = 5s)
  Step 2: Flag session as DISRUPTED in PostgreSQL
  Step 3: Broadcast forced-mute to ALL participants via WebSocket
  Step 4: Read last committed turn state from PostgreSQL
  Step 5: Determine if session can resume:
    - If completed turns ≥ 50%: resume from next turn
    - If completed turns < 50%: reschedule session
  Step 6: If resuming: rebuild Redis state from PostgreSQL checkpoint
  Step 7: Re-broadcast session state to frontend
  Step 8: Log disruption event with turn-level audit detail

DATA GUARANTEE:
  All completed turns are preserved regardless of session outcome.
  Interrupted sessions are marked PARTIAL — never INVALID.
  Scores from completed turns are final and immutable.
  No candidate loses earned score due to infrastructure failure.
```

### 5.4 Dojo Match Recovery Protocol

```
SCENARIO: LiveKit / Jitsi failure during active Dojo match

Step 1: Match state is checkpointed to PostgreSQL every 30 seconds
Step 2: On failure detection: save last checkpoint to immutable match log
Step 3: Determine failure type:
  - Media failure only: reconnect participants to new room (state preserved)
  - Backend failure: restore from PostgreSQL checkpoint
Step 4: Belt and rating events in progress are held in pending state
Step 5: Mentors are notified of disruption
Step 6: Scoring Engine re-evaluates from last valid checkpoint on resume
Step 7: If match cannot resume: both participants receive neutral outcome
  (no rating penalty for infrastructure failure — rule enforced)
```

### 5.5 Disaster Recovery — Full Cluster (REC-06)

```
TRIGGER: Total Kubernetes cluster loss

DR EXECUTION ORDER (STRICT SEQUENCE):
Phase 1 — Infrastructure (0–30 min)
  1.1  Provision fresh cluster (Terraform — reproducible infra)
  1.2  Apply base namespaces via Helm
  1.3  Restore HashiCorp Vault from snapshot (requires 3 key holders)
  1.4  Verify Vault unseal and secret accessibility

Phase 2 — Data Layer (30–90 min)
  2.1  Restore etcd state from latest snapshot
  2.2  Restore PostgreSQL primary from last full backup + WAL replay
  2.3  Restore Redis from RDB snapshot
  2.4  Restore MinIO buckets (priority: legal-archive, invoices, audit-files)
  2.5  Restore OpenSearch indices from snapshot
  2.6  Restore ClickHouse from last daily backup

Phase 3 — Services (90–150 min)
  3.1  Restore Kubernetes namespaces via Velero (core → realtime → media)
  3.2  Validate all service health checks
  3.3  Restore Kafka topics and consumer group offsets
  3.4  Verify event bus connectivity (publish test event per topic)

Phase 4 — Validation (150–180 min)
  4.1  Run full integration test suite against restored cluster
  4.2  Verify GD orchestration state machine is functional
  4.3  Verify billing ledger integrity
  4.4  Verify royalty ledger balance
  4.5  Verify tenant isolation (cross-tenant access test)
  4.6  External health check (DNS, NGINX, Kong gateway)

Phase 5 — Trust Restoration (180–240 min)
  5.1  Generate disaster recovery audit report
  5.2  Notify affected tenants (legally required)
  5.3  Legal review if downtime > 4 hours
  5.4  Publish incident timeline to admin governance dashboard

RTO TARGET: < 4 hours (Tier T4 commitment)
RPO TARGET: < 1 hour data loss maximum
```

---

## SECTION 6 — BACKUP VERIFICATION PROTOCOL (MANDATORY)

**All backups must be verified. Unverified backups are treated as non-existent.**

### 6.1 Automated Verification Schedule

| Target | Verification Method | Frequency |
|---|---|---|
| PostgreSQL WAL | pg_restore to isolated instance + row count diff | Daily |
| Redis RDB | Redis RESTORE to shadow instance + key count | Daily |
| MinIO critical buckets | Checksum verification of all objects | Daily |
| Velero cluster backup | Restore to staging namespace + pod health check | Weekly |
| ClickHouse | Restore + query result diff against production | Weekly |
| OpenSearch | Restore to isolated index + document count | Weekly |
| Vault snapshot | vault operator raft snapshot restore (dry-run) | Monthly |
| Full DR simulation | Full cluster restore to isolated DR environment | Quarterly |

### 6.2 Verification Output Requirements

Every verification run must produce:
```
backup_id         : unique identifier
backup_timestamp  : UTC ISO-8601
data_store        : target system
backup_size_bytes : compressed archive size
checksum_sha256   : archive hash
restore_status    : SUCCESS | FAIL | PARTIAL
restore_duration  : seconds
rows_verified     : count (databases)
objects_verified  : count (MinIO)
discrepancy_count : 0 = PASS, > 0 = ALERT
verified_by       : automated | human
verified_at       : UTC timestamp
```

These records are written to:  
`PostgreSQL table: backup_verification_audit_log`  
`Policy: IMMUTABLE — no UPDATE or DELETE permitted`

---

## SECTION 7 — ENCRYPTION AND KEY MANAGEMENT (TRUST LAYER)

### 7.1 Encryption Mandate

```
ALL backup archives: encrypted at rest (AES-256)
ALL backup transfers: encrypted in transit (TLS 1.3 minimum)
Key management: HashiCorp Vault ONLY
No plaintext backups: EVER — ZERO EXCEPTIONS
```

### 7.2 Key Rotation Policy

| Key Type | Rotation Frequency | Trigger for Emergency Rotation |
|---|---|---|
| Database encryption keys | 90 days | Suspected compromise |
| MinIO SSE keys | 180 days | Personnel change |
| Vault unseal keys | Annual | Security incident |
| Backup signing keys | 180 days | Audit failure |

### 7.3 Dual-Control Requirements

```
Royalty/legal archive restore: requires 2 admin approvals
Vault key recovery: requires 3 of 5 Shamir key holders
Cross-tenant restore: requires tenant admin + platform admin approval
Audit log access for external audit: requires compliance officer sign-off
```

### 7.4 Tenant Data Isolation in Backups

```
RULE: No backup archive contains data from multiple tenants.
ENFORCEMENT:
  - PostgreSQL: schema-per-tenant, backup by schema
  - MinIO: bucket prefix per tenant_id
  - All backup metadata tags: tenant_id (mandatory field)
  - Restore operations validate tenant_id match before execution
VIOLATION: Cross-tenant data in backup = SECURITY INCIDENT → Wazuh alert
```

---

## SECTION 8 — MONITORING AND ALERTING (NON-OPTIONAL)

### 8.1 Prometheus Metrics (All Required)

```
bra_backup_job_last_success_timestamp{store, env, tier}
bra_backup_job_duration_seconds{store, env}
bra_backup_size_bytes{store, env}
bra_restore_test_last_success_timestamp{store, env}
bra_restore_test_duration_seconds{store, env}
bra_backup_failure_total{store, env, reason}
bra_replication_lag_seconds{store, env}
bra_vault_key_age_days{key_type}
bra_worm_lock_violations_total{bucket}
```

### 8.2 Grafana Dashboards (Required)

```
Dashboard: BRA-OVERVIEW
  - Backup success rate (24h rolling) per store
  - Last successful backup timestamp per critical store
  - Replication lag heatmap
  - Restore test pass rate

Dashboard: BRA-COMPLIANCE
  - WORM lock integrity status
  - Key rotation due dates
  - Audit log write rate
  - Cross-tenant isolation violations

Dashboard: BRA-RECOVERY
  - Active recovery operations
  - Recovery duration vs. RTO targets
  - Open incidents by classification
```

### 8.3 Alert Definitions (Mandatory)

| Alert | Condition | Severity | Channel |
|---|---|---|---|
| BackupMissed | Last success > 2× schedule interval | CRITICAL | PagerDuty + ntfy |
| ReplicationLag | lag > 10 minutes | HIGH | ntfy + Slack |
| RestoreTestFailed | Verification != SUCCESS | CRITICAL | PagerDuty |
| VaultKeyExpiring | key age > 80% of rotation interval | MEDIUM | Email |
| WORMLockViolation | Any attempt to modify locked object | CRITICAL | Wazuh + PagerDuty |
| CrossTenantRestore | tenant_id mismatch in restore | CRITICAL | Wazuh + Legal |
| BackupStorageFull | MinIO bucket > 85% capacity | HIGH | ntfy |
| EncryptionKeyMissing | Backup missing key metadata | CRITICAL | PagerDuty |

---

## SECTION 9 — AIRFLOW ORCHESTRATION SPEC

### 9.1 Required DAGs

```
DAG: bra_daily_postgresql_backup
  Schedule: 0 2 * * *
  Tasks: full_backup → verify_checksum → upload_to_minio → update_audit_log
  SLA: Complete within 90 minutes

DAG: bra_daily_clickhouse_snapshot
  Schedule: 0 3 * * *
  Tasks: incremental_snapshot → compress → upload → verify

DAG: bra_velero_cluster_backup
  Schedule: 0 */6 * * *
  Tasks: velero_backup_trigger → wait_completion → validate_manifest_count

DAG: bra_restore_verification_weekly
  Schedule: 0 4 * * 0
  Tasks: restore_pg_to_staging → row_count_diff → restore_redis →
         restore_opensearch → generate_report → write_audit_log

DAG: bra_quarterly_dr_drill
  Schedule: 0 6 1 */3 *
  Tasks: provision_dr_env → full_restore_sequence → validation_suite →
         teardown → generate_dr_report → notify_stakeholders
```

### 9.2 Airflow Connection Requirements

```
Connections (must be pre-registered in Airflow):
  - postgres_primary (ecoskiller-prod)
  - redis_sentinel_cluster
  - minio_backup_store
  - kubernetes_cluster_prod
  - vault_api
  - wazuh_api (for security event injection)
  - pagerduty_webhook
  - ntfy_server
All credentials sourced from Vault. Never hardcoded. Never in DAG code.
```

---

## SECTION 10 — COMPLIANCE AND LEGAL OBLIGATIONS

### 10.1 Data Retention Legal Map

| Data Category | Legal Basis | Minimum Retention | Maximum Retention |
|---|---|---|---|
| Royalty contracts | Licensing agreement obligation | 15 years | Indefinite |
| Royalty ledger | Financial audit requirement | 15 years | Indefinite |
| Billing invoices | GST/VAT compliance | 7 years | Indefinite |
| Innovation ideas | IP protection (timestamped) | 15 years | Indefinite |
| Digital signatures | Legal enforceability | 15 years | Indefinite |
| Audit logs | Compliance + fraud detection | 7 years | Indefinite |
| User PII | GDPR/PDPA compliance | Duration of account + 2 years | 7 years |
| GD session metadata | Candidate record + dispute resolution | 3 years | 7 years |

### 10.2 Right to Erasure Protocol (GDPR Compliance)

```
RULE: User data deletion requests must be honored.
CONFLICT: WORM archives cannot be selectively deleted.

RESOLUTION PROTOCOL:
  1. PII is pseudonymized before entering WORM archive
  2. WORM archives contain: pseudonymized_user_id, NOT name/email/phone
  3. PII mapping table (pseudonym → real identity) is stored separately
  4. On erasure request: PII mapping is deleted
  5. WORM archive remains intact (legally required) but becomes de-linked
  6. Erasure is logged to compliance audit table
  7. User receives erasure confirmation with pseudonymization certificate

This satisfies both legal retention AND right-to-erasure simultaneously.
```

### 10.3 Incident Notification Obligations

```
Data loss affecting legal data (royalty, contracts, billing):
  → Legal team notified within 1 hour
  → Affected tenants notified within 24 hours
  → Regulatory authority notified within 72 hours (if applicable)

Data breach affecting PII:
  → Security team notified immediately (< 15 minutes via Wazuh)
  → Legal + DPO notified within 1 hour
  → Affected users notified within 72 hours
  → Regulatory notification per applicable data protection law

All notifications logged to: compliance_incident_log (IMMUTABLE)
```

---

## SECTION 11 — FAILURE MODES AND HARD STOPS

### 11.1 BRA Hard Stops (Non-Negotiable)

Any of the following conditions must halt all deployment pipelines:

```
HARD STOP CONDITIONS:
  HS-01: Backup job failure for T0 or T1 store with no successful backup in last 2× schedule
  HS-02: Vault unavailable (secrets cannot be retrieved for encryption)
  HS-03: MinIO primary unreachable + secondary replication lag > 30 minutes
  HS-04: PostgreSQL WAL archiving failure for > 15 minutes
  HS-05: Restore verification failure on any T1 store for > 3 consecutive runs
  HS-06: WORM lock violation detected (any bucket)
  HS-07: Cross-tenant data found in any backup archive
  HS-08: Backup encryption key rotation overdue by > 30 days
  HS-09: Velero backup failure for production namespace for > 24 hours
  HS-10: etcd backup failure for > 1 hour

On HARD STOP:
  → CI/CD pipeline: BLOCKED
  → Deployment gate: CLOSED
  → Prometheus alert: FIRED (all channels)
  → Incident: AUTO-CREATED in admin governance dashboard
  → Resolution: Manual human sign-off required to resume
```

---

## SECTION 12 — IMPLEMENTATION CHECKLIST (LOCKED)

All items below must be completed and verified before BRA is declared operational.

```
[ ] Velero installed and configured in ops namespace
[ ] MinIO buckets created with correct versioning and lock policies
[ ] WAL-G configured for all PostgreSQL instances
[ ] Redis AOF enabled on GD state machine instances
[ ] ClickHouse native backup configured with MinIO target
[ ] OpenSearch snapshot repository registered
[ ] HashiCorp Vault snapshot schedule active
[ ] etcd backup CronJob deployed
[ ] Airflow DAGs deployed and validated in staging
[ ] All Prometheus metrics emitting correctly
[ ] Grafana dashboards imported and visible
[ ] All alert rules loaded and tested (fire test alerts)
[ ] Wazuh rules deployed for WORM violation detection
[ ] Tenant isolation validation test: PASS
[ ] Encryption key hierarchy documented in Vault
[ ] Dual-control procedures documented and tested
[ ] First DR drill completed in isolated environment
[ ] DR drill report signed off by platform admin
[ ] Compliance retention policies applied to all buckets
[ ] PII pseudonymization verified in WORM archives
[ ] Backup audit log table created (immutable constraints verified)
[ ] Legal team sign-off on royalty + contract backup SLAs
[ ] BRA operational declaration signed by: Platform Admin + Legal + DevOps Lead
```

---

## SECTION 13 — AGENT VERSION CONTROL

```
AGENT ID          : BRA-001
VERSION           : 1.0.0
SEALED BY         : Platform Architecture Authority
SEAL DATE         : [TO BE FILLED ON EXECUTION]
NEXT REVIEW       : 6 months post-seal
MUTATION POLICY   : All changes require version bump + re-seal
BREAKING CHANGE   : Requires Architecture Authority approval + Legal review
                    if change affects retention or encryption policy

VERSION HISTORY:
  v1.0.0 — Initial sealed specification
             Covers: PostgreSQL, Redis, MinIO, ClickHouse, OpenSearch,
                     etcd, Vault, Kubernetes/Velero, GD recovery, Dojo recovery,
                     Intelligence DNA, Innovation Registry, Royalty Ledger,
                     Legal Archive, Multi-tenant isolation, DR protocol,
                     GDPR compliance, WORM policy, Airflow orchestration
```

---

```
END OF SPECIFICATION

BACKUP_AND_RECOVERY_AGENT v1.0.0
STATUS: SEALED · LOCKED · GOVERNED
ECOSKILLER PLATFORM — ANTIGRAVITY SUBSYSTEM
ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE

"Infrastructure fails. Data does not."
```
