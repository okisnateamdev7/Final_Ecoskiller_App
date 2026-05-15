# NETWORK_SHUTDOWN_PROTOCOL_AGENT (NSPA)
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Platform: ANTIGRAVITY — Powered by ECOSKILLER Architecture
---

```
Document Class   : SEALED PRODUCTION ARTIFACT
Version          : v1.0 — FINAL
Mutation Policy  : ADD-ONLY via version bump
Execution Mode   : DETERMINISTIC
Interpretation   : FORBIDDEN
Assumption Fill  : FORBIDDEN
Default Behavior : DENY
Failure Mode     : STOP → REPORT → ESCALATE → NO PARTIAL EXECUTION
Authority        : Human Declaration Only
Status           : LOCKED · GOVERNED · ENFORCED
```

---

## PREAMBLE — DESIGN LAW

The NETWORK_SHUTDOWN_PROTOCOL_AGENT (NSPA) is a **non-negotiable, autonomous enforcement agent** embedded inside ANTIGRAVITY's Enterprise Optimization + Trust Infrastructure layer.

It does not ask permission.
It does not negotiate.
It does not interpret context.
It executes pre-declared protocol under pre-declared triggers — nothing more.

The NSPA converts all network integrity threats, abuse cascades, infrastructure degradation events, and trust violations into a **deterministic, auditable, tenant-safe shutdown sequence** — without human latency, without AI inference, and without partial states.

> **Core Law:**
> A partial shutdown is more dangerous than a full shutdown.
> An unlogged shutdown never happened.
> A non-reversible shutdown without a verified recovery contract is forbidden.

---

## SECTION I — SYSTEM IDENTITY

| Field | Value |
|---|---|
| Agent Name | NETWORK_SHUTDOWN_PROTOCOL_AGENT |
| Agent Class | Infrastructure Enforcement Agent |
| Layer | Enterprise Optimization + Trust Infrastructure |
| Platform | ANTIGRAVITY |
| Architecture Base | ECOSKILLER v8 — Microservices on Kubernetes |
| Execution Target | All namespaces: `core`, `realtime`, `media`, `billing`, `analytics`, `admin`, `ops` |
| Trigger Authority | Pre-declared trigger registry only |
| Scope | Multi-tenant, cross-namespace, cross-service |
| Reversibility | Controlled — requires verified Recovery Contract |
| Audit Log | Immutable — written to MinIO WORM + ClickHouse |
| Data Sovereignty | Per-tenant data sealed on shutdown, never co-mingled |

---

## SECTION II — AGENT POSITION IN ANTIGRAVITY TRUST STACK

```
┌─────────────────────────────────────────────────────────────────────┐
│                  ANTIGRAVITY TRUST INFRASTRUCTURE                   │
│                                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │  WAF / Kong  │  │   Keycloak   │  │  Open Policy Agent (OPA) │  │
│  │  (Ingress)   │  │   (AuthN)    │  │  (AuthZ + Policy)        │  │
│  └──────┬───────┘  └──────┬───────┘  └────────────┬─────────────┘  │
│         │                 │                        │                │
│  ┌──────▼─────────────────▼────────────────────────▼─────────────┐  │
│  │              ENTERPRISE OPTIMIZATION LAYER                     │  │
│  │   Prometheus · Grafana · Loki · OpenTelemetry · Wazuh · SIEM  │  │
│  └──────────────────────────────┬──────────────────────────────┘  │
│                                 │                                  │
│  ┌──────────────────────────────▼──────────────────────────────┐  │
│  │         ██ NETWORK_SHUTDOWN_PROTOCOL_AGENT (NSPA) ██         │  │
│  │                                                              │  │
│  │   Trigger Registry → State Machine → Execution Phases →     │  │
│  │   Audit Ledger → Recovery Contract Enforcement              │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                 │                                  │
│  ┌──────────────────────────────▼──────────────────────────────┐  │
│  │  Target Infrastructure Layer                                  │  │
│  │  Docker · Kubernetes · Redis · PostgreSQL · Kafka · Jitsi   │  │
│  │  LiveKit · WebRTC · MinIO · Vault · Envoy · Velero           │  │
│  └──────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

The NSPA sits **below** the observability layer and **above** raw infrastructure. It receives signals, validates against the trigger registry, and issues deterministic commands downward. It never communicates upward to users or recruiters — only to the Admin Governance Service and Immutable Audit Log.

---

## SECTION III — TRIGGER REGISTRY (SEALED)

The following are the **only valid triggers** for NSPA activation. Triggers outside this registry are silently discarded and logged as `UNAUTHORIZED_TRIGGER_ATTEMPT`.

### Class A — Network Integrity Triggers

| Trigger ID | Event | Threshold | Severity |
|---|---|---|---|
| `NIT-001` | Inbound DDoS burst detected (Wazuh + WAF consensus) | >50,000 rps sustained 10s | CRITICAL |
| `NIT-002` | TURN/STUN amplification attack on coturn | >5,000 spoofed UDP packets/s | CRITICAL |
| `NIT-003` | WebRTC ICE candidate flood from single IP range | >1,000 ICE requests/min per room | HIGH |
| `NIT-004` | Jitsi SFU bandwidth saturation | >95% uplink for >60s | HIGH |
| `NIT-005` | TLS certificate validation failure on ingress (NGINX/Kong) | Any prod cert failure | CRITICAL |
| `NIT-006` | DNS poisoning or unexpected CNAME resolution change | Any deviation from registered DNS state | CRITICAL |
| `NIT-007` | Cross-tenant network bleed detected (inter-namespace unauthorized traffic) | Any event | CRITICAL |

### Class B — Authentication + Trust Triggers

| Trigger ID | Event | Threshold | Severity |
|---|---|---|---|
| `ATT-001` | Keycloak token forge attempt detected (invalid signature cluster) | >500 failed JWT validations/min | CRITICAL |
| `ATT-002` | OAuth token replay attack (same JTI used across sessions) | Any JTI reuse | CRITICAL |
| `ATT-003` | MFA bypass attempt via brute-force on OTP endpoint | >20 consecutive failures per user | HIGH |
| `ATT-004` | Vault seal breach attempt (unauthorized unseal key usage) | Any unauthorized unseal event | CRITICAL |
| `ATT-005` | Service-to-service credential leak (Vault secret accessed by unknown ServiceAccount) | Any unauthorized accessor | CRITICAL |
| `ATT-006` | RBAC privilege escalation attempt via OPA policy override | Any policy mutation not via declared change pipeline | CRITICAL |
| `ATT-007` | Parent-role account accessing student live session data (trust boundary breach) | Any unauthorized read | HIGH |

### Class C — GD / Dojo / Realtime Session Triggers

| Trigger ID | Event | Threshold | Severity |
|---|---|---|---|
| `GDT-001` | Voice GD Orchestrator Redis state machine corruption | Any non-deterministic state transition | CRITICAL |
| `GDT-002` | Forced mute/unmute command pipeline broken (WebSocket channel failure) | >5s command latency in active GD session | HIGH |
| `GDT-003` | GD session score tamper attempt detected (score field mutation outside Scoring Engine) | Any direct DB write to score table | CRITICAL |
| `GDT-004` | Dojo Match Engine state rollback without declared Recovery Contract | Any unauthorized state reversal | CRITICAL |
| `GDT-005` | LiveKit SFU room token reuse across distinct match IDs | Any token/room mismatch | HIGH |
| `GDT-006` | Interrupt attempt spike in GD session (coordinated disruption pattern) | >50 interrupt_attempt logs in single session | MEDIUM |

### Class D — Billing + Revenue Integrity Triggers

| Trigger ID | Event | Threshold | Severity |
|---|---|---|---|
| `BIT-001` | Invoice generation without verified subscription entitlement | Any orphaned invoice | CRITICAL |
| `BIT-002` | Revenue ingestion gateway receiving unsigned payload | Any missing HMAC signature | CRITICAL |
| `BIT-003` | Royalty accounting ledger double-entry imbalance detected | Any ledger delta ≠ 0 | CRITICAL |
| `BIT-004` | GST/VAT calculation engine returning null on taxable transaction | Any null tax on non-exempt transaction | HIGH |
| `BIT-005` | Feature gating middleware bypassed (paid feature accessed on free plan) | Any unauthorized feature flag flip | HIGH |
| `BIT-006` | Fraud Detection Engine flagging suspicious revenue submission | Confidence score >0.85 | HIGH |

### Class E — Infrastructure Degradation Triggers

| Trigger ID | Event | Threshold | Severity |
|---|---|---|---|
| `IDT-001` | Kubernetes pod crash loop in `core` namespace | >3 restarts in 5 minutes | HIGH |
| `IDT-002` | PostgreSQL primary node unreachable | >30s timeout | CRITICAL |
| `IDT-003` | Redis cluster quorum loss | Majority nodes unreachable | CRITICAL |
| `IDT-004` | Kafka consumer lag beyond recovery threshold | >500,000 unprocessed events | HIGH |
| `IDT-005` | MinIO storage node failure with no quorum replica | Any quorum loss | CRITICAL |
| `IDT-006` | Velero backup verification failure before maintenance window | Any failed pre-window backup check | HIGH |
| `IDT-007` | etcd distributed lock store split-brain detected | Any split-brain event | CRITICAL |

### Class F — Compliance + Legal Triggers

| Trigger ID | Event | Threshold | Severity |
|---|---|---|---|
| `CLT-001` | PII data exfiltration attempt (unencrypted PII detected in egress traffic) | Any event | CRITICAL |
| `CLT-002` | Immutable audit log tampering attempt (write to WORM storage after finalization) | Any unauthorized write | CRITICAL |
| `CLT-003` | Consent record missing on active recording session | Any session without verified consent | CRITICAL |
| `CLT-004` | Underage user (parent-linked minor) accessing adult-classified content | Any access event | CRITICAL |
| `CLT-005` | Digital signature validation failure on licensing agreement | Any signature mismatch | HIGH |
| `CLT-006` | Legal document retention period breach (deletion before 15-year WORM window) | Any early deletion attempt | CRITICAL |

---

## SECTION IV — SHUTDOWN SEVERITY CLASSIFICATION

```
LEVEL 0  —  OBSERVE ONLY         (no action, log, monitor for escalation)
LEVEL 1  —  SOFT RATE THROTTLE   (reduce traffic, warn tenant admins)
LEVEL 2  —  SERVICE ISOLATION    (isolate affected namespace, keep others running)
LEVEL 3  —  TENANT SUSPENSION    (suspend triggering tenant, preserve data)
LEVEL 4  —  DOMAIN FREEZE        (freeze affected domain track: Arts/Science/etc.)
LEVEL 5  —  PARTIAL SHUTDOWN     (shut down affected service cluster + media)
LEVEL 6  —  FULL PLATFORM HALT   (all namespaces — only authorized by L6 law below)
```

### Trigger → Level Mapping

| Trigger Class | Minimum Level | Maximum Level | Escalation Allowed |
|---|---|---|---|
| Class A — Network Integrity | LEVEL 2 | LEVEL 6 | YES — auto-escalate after 60s without mitigation |
| Class B — Auth + Trust | LEVEL 3 | LEVEL 6 | YES — auto-escalate after 30s |
| Class C — GD / Realtime | LEVEL 2 | LEVEL 5 | YES — after 120s |
| Class D — Billing | LEVEL 1 | LEVEL 5 | NO — hold at declared level, alert human |
| Class E — Infrastructure | LEVEL 1 | LEVEL 6 | YES — after 45s |
| Class F — Compliance | LEVEL 3 | LEVEL 6 | NO — hold at LEVEL 3, require human L6 declaration |

> **L6 LAW — FULL PLATFORM HALT:**
> LEVEL 6 may only be declared by:
> - A registered human Admin (Admin Governance Service — verified JWT + MFA)
> - OR automatic escalation from LEVEL 5 after sustained breach >120 seconds
> - LEVEL 6 is never triggered by Class D or Class F alone
> - LEVEL 6 always writes `FULL_HALT_DECLARATION` to immutable audit before execution

---

## SECTION V — STATE MACHINE (DETERMINISTIC)

```
State Diagram — NSPA Execution States

  [IDLE]
    │
    ├── trigger_received()
    │
  [TRIGGER_VALIDATION]
    │
    ├── trigger NOT in registry → [UNAUTHORIZED_DISCARD] → log → [IDLE]
    ├── trigger valid → classify severity level
    │
  [SEVERITY_CLASSIFICATION]
    │
    ├── LEVEL 0 → [OBSERVE_LOG] → [IDLE]
    ├── LEVEL 1 → [THROTTLE_EXECUTE] → [RECOVERY_WATCH] → [IDLE]
    ├── LEVEL 2–5 → [PRE_SHUTDOWN_SEAL]
    ├── LEVEL 6 → [FULL_HALT_DECLARATION] → [PRE_SHUTDOWN_SEAL]
    │
  [PRE_SHUTDOWN_SEAL]
    │
    ├── snapshot_active_sessions()
    ├── write_tenant_state_to_redis()
    ├── flush_kafka_events()
    ├── seal_billing_ledger()
    ├── write_pre_shutdown_audit_record()
    │
    ├── all seals confirmed → [SHUTDOWN_EXECUTION]
    ├── any seal failed → STOP → [SEAL_FAILURE_ALERT] → retry once → ESCALATE
    │
  [SHUTDOWN_EXECUTION]
    │
    ├── execute phased namespace shutdown (see Section VI)
    ├── write shutdown_complete audit record
    │
  [SHUTDOWN_CONFIRMED]
    │
    ├── Recovery Contract generated (see Section VII)
    ├── notify Admin Governance Service
    ├── → [RECOVERY_WATCH]
    │
  [RECOVERY_WATCH]
    │
    ├── contract verified + human declaration → [RECOVERY_EXECUTION]
    ├── no contract within SLA window → ESCALATE severity level
    │
  [RECOVERY_EXECUTION]
    │
    ├── reverse phases in declared order
    ├── validate each phase before proceeding
    ├── write recovery_complete audit record
    │
    └── → [IDLE]
```

---

## SECTION VI — SHUTDOWN EXECUTION PHASES

Phases are always executed **in this exact order**. Skipping a phase is a protocol violation and triggers `PHASE_SKIP_VIOLATION` log + immediate LEVEL 6 escalation.

### Phase 1 — Media Layer Isolation

Target: `media` namespace — Jitsi, LiveKit, coturn, WebRTC sessions

```
Actions:
  1.1  Issue force-mute ALL active GD participants via WebSocket command channel
  1.2  Set all Jitsi rooms to READ_ONLY state via Jicofo API
  1.3  Drain LiveKit SFU rooms — complete in-flight audio frames only
  1.4  Terminate coturn TURN relay allocations (graceful 5s window)
  1.5  Set room_state = SUSPENDED in Redis GD state machine
  1.6  Write GD session snapshots to PostgreSQL (session_metadata, participant_map, score_at_suspension)
  1.7  Revoke all active Jitsi / LiveKit short-lived tokens via Keycloak token revocation endpoint
  1.8  Scale Jitsi Videobridge replicas to 0 in Kubernetes

Completion Condition:
  All rooms_active = 0 in Redis
  All Jicofo participants = 0
  All LiveKit rooms = empty
  PostgreSQL snapshots confirmed written

Timeout: 30 seconds
Timeout Action: Force-kill media namespace pods — no grace
```

### Phase 2 — Realtime Channel Shutdown

Target: `realtime` namespace — WebSocket command channels, GD Orchestrator, Dojo Match Engine

```
Actions:
  2.1  Drain all active WebSocket connections (send SHUTDOWN_SIGNAL frame)
  2.2  Flush all pending Redis commands (MULTI/EXEC flush)
  2.3  Serialize GD Orchestrator state machine snapshot to MinIO
  2.4  Serialize Dojo Match Engine active match states to PostgreSQL
  2.5  Disable Kafka consumer groups in realtime namespace (pause, not delete)
  2.6  Set realtime_namespace_status = FROZEN in etcd distributed lock

Completion Condition:
  All WebSocket connections = 0
  Redis pipeline = empty
  etcd lock confirmed

Timeout: 20 seconds
Timeout Action: Force namespace cordon in Kubernetes
```

### Phase 3 — API Gateway + Auth Suspension

Target: Kong API Gateway, Keycloak, OPA

```
Actions:
  3.1  Switch Kong to maintenance mode (return HTTP 503 on all routes)
  3.2  Revoke all active JWT access tokens (write to Keycloak revocation list)
  3.3  Preserve all refresh tokens — do NOT revoke (required for recovery handshake)
  3.4  Disable all OAuth client grants for affected tenant scope
  3.5  Set OPA policy to DENY_ALL for affected namespaces
  3.6  Write token_revocation_snapshot to Vault (timestamped, immutable)
  3.7  HashiCorp Vault: set affected secret paths to READ_ONLY

Completion Condition:
  Kong returns 503 on >99.9% of probe requests
  OPA policy = DENY_ALL confirmed
  Vault secrets = READ_ONLY confirmed

Timeout: 15 seconds
Timeout Action: Kill Kong pods, force-seal Vault path
```

### Phase 4 — Core Service Suspension

Target: `core` namespace — Auth, User, Job, Application, Interview, Recruiter, Notification services

```
Actions:
  4.1  Scale all core namespace deployments to 0 replicas (Kubernetes rolling stop)
  4.2  Disable all Kafka producers in core namespace (set producer.enable = false)
  4.3  Flush pending notification queue (RabbitMQ drain — do not delete messages)
  4.4  Seal billing ledger: write LEDGER_SEALED event to ClickHouse
  4.5  Write tenant_state snapshot to PostgreSQL (all active sessions, pipeline states)
  4.6  Disable all Airflow DAGs (pause scheduled billing, analytics, report jobs)

Completion Condition:
  Kubernetes reports 0 running pods in core namespace
  Kafka producer = disabled
  RabbitMQ queue = drained (messages retained)
  Airflow DAGs = paused

Timeout: 45 seconds
Timeout Action: Force-evict all core namespace pods
```

### Phase 5 — Data Layer Protection

Target: PostgreSQL, Redis, OpenSearch, ClickHouse, MinIO, etcd

```
Actions:
  5.1  PostgreSQL: SET default_transaction_read_only = on (block all writes)
  5.2  Redis: CONFIG SET bind-source-addr restricted (block new connections from app services)
  5.3  OpenSearch: set index.blocks.write = true on all tenant indices
  5.4  ClickHouse: set readonly = 2 (reads allowed, writes blocked)
  5.5  MinIO: set bucket policy = READ_ONLY for all non-ops buckets
  5.6  etcd: revoke all non-ops leases
  5.7  Flyway: set migration lock = HARD_LOCK (no schema changes permitted)

Completion Condition:
  PostgreSQL write_probe returns ERROR (read-only confirmed)
  Redis new_connection_probe returns REFUSED (from app namespace)
  MinIO write_probe returns 403 (read-only confirmed)

Timeout: 30 seconds
Timeout Action: Restart data layer pods in read-only init mode
```

### Phase 6 — Observability Freeze + Audit Seal

Target: Prometheus, Grafana, Loki, OpenTelemetry, Wazuh

```
Actions:
  6.1  Prometheus: set global_evaluation_interval = paused (no new alerts during shutdown)
  6.2  Loki: flush all in-memory log buffers to persistent volume
  6.3  OpenTelemetry collector: drain all spans, flush to Tempo
  6.4  Wazuh: set alert_mode = LISTEN_ONLY (no automated response during shutdown)
  6.5  Write SHUTDOWN_AUDIT_SEAL record to MinIO WORM bucket with:
       - timestamp (UTC)
       - trigger_id
       - severity_level
       - phases_completed[]
       - tenant_scope[]
       - executing_agent: NSPA
       - human_override: true/false
       - recovery_contract_id (generated in Phase 7)

Completion Condition:
  All log buffers flushed
  WORM audit record write confirmed (HTTP 200 from MinIO)
  Wazuh = LISTEN_ONLY

Timeout: 20 seconds
Timeout Action: Write emergency audit to ClickHouse fallback
```

### Phase 7 — Recovery Contract Generation (Mandatory)

```
Actions:
  7.1  Generate Recovery Contract with:
       - contract_id: UUID v4
       - shutdown_trigger_id: <from trigger registry>
       - shutdown_severity: <LEVEL N>
       - shutdown_timestamp_utc: <ISO 8601>
       - tenant_scope: [tenant_id...]
       - namespace_scope: [namespace...]
       - data_integrity_hashes:
           postgres_snapshot_hash: <SHA-256>
           redis_snapshot_hash: <SHA-256>
           minio_worm_record_hash: <SHA-256>
       - required_human_declaration: true/false (see L6 Law)
       - recovery_sla_minutes: <from trigger class SLA table>
       - recovery_authorization_required: [admin_role, tenant_owner]
       - recovery_steps_locked: <ordered list from Section VII>
       - contract_status: PENDING_AUTHORIZATION

  7.2  Persist Recovery Contract to PostgreSQL (ops schema, contracts table)
  7.3  Persist Recovery Contract to MinIO (immutable copy)
  7.4  Dispatch contract_id via Admin Governance Service notification
       → Email (Postfix)
       → SMS (Jasmin SMS Gateway)
       → Push (ntfy)
  7.5  Set Recovery Contract expiry = now + recovery_sla_minutes
       → If expiry passes without authorization → auto-escalate severity level

Completion Condition:
  Recovery Contract written to both PostgreSQL and MinIO
  Notification dispatched on all 3 channels
  contract_status = PENDING_AUTHORIZATION

Timeout: 10 seconds
Timeout Action: CRITICAL ALERT — escalate to LEVEL 6 with human wake-up
```

---

## SECTION VII — RECOVERY CONTRACT PROTOCOL

Recovery is **not automatic**. It requires a verified contract execution. The NSPA does not assume the shutdown cause is resolved — it requires proof.

### Recovery Authorization Requirements

| Shutdown Level | Authorization Required |
|---|---|
| LEVEL 1 | Automatic (NSPA self-authorizes after mitigation signal) |
| LEVEL 2 | Tenant Admin JWT + MFA confirmation |
| LEVEL 3 | Tenant Owner + Platform Admin co-signature |
| LEVEL 4 | Platform Admin + Compliance Admin co-signature |
| LEVEL 5 | Platform Admin + Security Admin + Velero backup verification |
| LEVEL 6 | Full Admin Council (3 of 5 registered L6 admins) + fresh Velero restore verification |

### Recovery Execution Phases

Recovery phases execute in **reverse order** to shutdown phases — with one additional rule: **each phase must be validated before the next begins**.

```
Phase R1 — Observability Unfreeze
  → Prometheus re-enable, Loki confirm log continuity, Wazuh re-enable active response
  → Validation: Prometheus targets healthy, Grafana dashboards loading

Phase R2 — Data Layer Unlock
  → Remove PostgreSQL read-only, Redis connection unlock, MinIO write-enable, etcd leases restore
  → Validation: write_probe success on all data stores

Phase R3 — Core Service Restart
  → Scale core namespace deployments to declared replica counts
  → Re-enable Kafka producers, RabbitMQ drain resume, Airflow DAGs unpause
  → Validation: all pods Running, Kafka lag = 0 on critical topics

Phase R4 — Auth + Gateway Restore
  → Kong exit maintenance mode, Keycloak access token re-issuance enabled
  → OPA policy restored to pre-shutdown state
  → Vault secret paths restored to READ_WRITE
  → Validation: OAuth token issuance probe success, OPA policy test pass

Phase R5 — Realtime Restore
  → WebSocket server restart, GD Orchestrator state machine reload from Redis snapshot
  → Dojo Match Engine state reload from PostgreSQL snapshot
  → Kafka consumer groups re-enabled
  → Validation: WebSocket connection probe success, GD state machine = ACTIVE

Phase R6 — Media Layer Restore
  → Jitsi Videobridge scale-up, LiveKit SFU rooms re-enabled
  → coturn TURN relay re-enabled
  → Token issuance re-enabled for new sessions
  → Suspended GD sessions: candidates notified (cannot resume — new session required)
  → Validation: Jitsi room creation probe success, LiveKit token validation probe success

Phase R7 — Recovery Audit Seal
  → Write RECOVERY_COMPLETE record to MinIO WORM
  → Update Recovery Contract status = COMPLETED
  → Write recovery timeline delta to ClickHouse
  → Validate all data_integrity_hashes against pre-shutdown snapshots
  → If hash mismatch → STOP → trigger CLT-002 → do not declare recovery complete
```

---

## SECTION VIII — DATA INTEGRITY GUARANTEES DURING SHUTDOWN

### Tenant Data Isolation Law

> During any shutdown at any level, tenant data is **never co-mingled, never exposed to other tenants, and never partially persisted**.

Enforcement mechanisms:

- **PostgreSQL row-level security** remains active at all times — enforced at DB level, not application level
- **MinIO bucket policies** are per-tenant — shutdown does not merge buckets
- **Redis key namespacing** uses `tenant:{tenant_id}:*` — isolation is structural, not policy-based
- **Kafka topic partitioning** uses tenant_id as partition key — no cross-tenant message bleed
- **etcd key space** uses `/antigravity/tenant/{tenant_id}/` prefix — fenced by lease scope

### Active Session Handling

| Session Type | Shutdown Behavior |
|---|---|
| Voice GD (active speaking turn) | Force-mute → snapshot score at suspension → write participant_map → session suspended |
| Dojo Match (live round) | Pause match timer → snapshot match state → write to PostgreSQL → match suspended |
| Interview (live video) | Issue graceful_disconnect signal → 10s drain window → disconnect |
| Billing transaction (in-flight) | Complete if within 5s → abort and refund-queue if >5s |
| OTP validation (active) | Invalidate → log → user must re-request post-recovery |
| Certificate generation (in-progress) | Serialize state → resume post-recovery (not restart) |

### Score Integrity Law

> No score may be altered, committed, or rolled back during shutdown.
> The last confirmed score before `PRE_SHUTDOWN_SEAL` timestamp is the authoritative score.
> Post-recovery score reconciliation requires Scoring Engine validation — not manual entry.

---

## SECTION IX — FAILURE HANDLING WITHIN NSPA

The NSPA itself may fail. These rules govern NSPA self-failure.

| Failure Scenario | NSPA Response |
|---|---|
| Trigger validation service unreachable | DENY ALL triggers → alert → retry 3x → escalate to LEVEL 6 |
| Redis state machine unavailable during shutdown | Use PostgreSQL fallback state store → log `REDIS_FALLBACK_ACTIVATED` |
| MinIO WORM write failure (audit seal) | Write to ClickHouse emergency audit → alert → DO NOT proceed to recovery without audit |
| Recovery Contract generation failure | HALT recovery → escalate → human must manually generate contract via Admin Governance |
| Phase execution timeout exceeded | Force-kill affected namespace → log `PHASE_TIMEOUT_KILL` → escalate to next severity level |
| NSPA pod crash during active shutdown | Kubernetes liveness probe restarts pod → NSPA resumes from last confirmed phase via etcd checkpoint |
| Admin notification failure (all 3 channels) | Write to Grafana alert channel (ops channel, always last-resort) → log `NOTIFICATION_TOTAL_FAILURE` |

### NSPA Self-Checkpoint Protocol

Every phase completion writes a checkpoint to etcd:

```
/antigravity/nspa/shutdown/{contract_id}/phase/{phase_number}/status = COMPLETED
/antigravity/nspa/shutdown/{contract_id}/phase/{phase_number}/timestamp = <UTC>
/antigravity/nspa/shutdown/{contract_id}/phase/{phase_number}/hash = <SHA-256 of phase state>
```

On NSPA restart, it reads etcd checkpoints and **resumes from the first INCOMPLETE phase** — never re-executes completed phases.

---

## SECTION X — ANTIGRAVITY-SPECIFIC ENFORCEMENT RULES

These rules are specific to the ANTIGRAVITY platform's business and trust model layered on top of ECOSKILLER architecture.

### Rule AG-01 — Parent-Trust Boundary Lock

> During any NSPA-triggered shutdown involving Class B (Auth/Trust) triggers:
> All parent-role access tokens are revoked FIRST — before student tokens.
> Parent notification is sent FIRST — before recruiter or enterprise notifications.
> Parent-linked minor data enters ELEVATED_PROTECTION state — read access restricted to platform compliance admins only until recovery is verified.

### Rule AG-02 — Royalty Ledger Immutability

> During shutdown, the Royalty Accounting Engine is sealed before the Billing Service.
> No royalty calculation may be partially committed.
> If Royalty Audit & Compliance Service detects a mid-calculation state at shutdown:
> → Roll back to last confirmed ledger checkpoint
> → Log `ROYALTY_ROLLBACK_ACTIVATED`
> → Write to Immutable Archive Service before proceeding to Phase 4

### Rule AG-03 — Innovation Anti-Theft Freeze

> All idea submissions in-flight (Idea Registry Service) at shutdown are:
> → Timestamped with shutdown trigger ID (not system clock — prevents backdating)
> → Written to Vector Database with FROZEN status
> → Anti-theft fingerprint hash computed and sealed before Phase 4

### Rule AG-04 — GD Score Non-Replayability

> A GD session that is shutdown mid-session is NOT replayed or resumed.
> A new session must be scheduled.
> The suspended session's data (turns completed, mic open duration, interrupt attempts) is preserved for the candidate's record but no score is finalized.
> Recruiters receive a `SESSION_SUSPENDED` notification with the Recovery Contract ID.

### Rule AG-05 — Intelligence DNA Snapshot

> The Intelligence Profile Service writes a full DNA snapshot for all active users during Phase 4.
> Passive Intelligence Engine signal processing is halted — no partial signal may update an intelligence score during shutdown.
> Intelligence scores are immutable between `PRE_SHUTDOWN_SEAL` and `RECOVERY_COMPLETE`.

### Rule AG-06 — Belt & Certification Freeze

> No belt promotion or certification may be finalized during NSPA-active state.
> Any certification in the mentor_confirmation stage is preserved in PENDING state.
> Auto-promotion is always forbidden — doubly forbidden during NSPA-active state.

---

## SECTION XI — OBSERVABILITY INTEGRATION

The NSPA emits the following standardized metrics and logs into the ANTIGRAVITY observability stack.

### Prometheus Metrics (exposed at `/metrics`)

```
nspa_trigger_received_total{class, trigger_id, severity}
nspa_shutdown_phase_duration_seconds{phase, contract_id}
nspa_shutdown_level{contract_id, level}
nspa_recovery_duration_seconds{contract_id}
nspa_phase_timeout_total{phase}
nspa_unauthorized_trigger_total
nspa_audit_write_failures_total
nspa_active_contracts_count
```

### Grafana Dashboard (ops namespace)

Required panels:

| Panel Name | Data Source | Alert Threshold |
|---|---|---|
| Active NSPA Contracts | Prometheus | >0 = WARN |
| Shutdown Level History | ClickHouse | Any LEVEL 5+ = CRITICAL alert |
| Phase Execution Timeline | ClickHouse | Phase duration >timeout = RED |
| Trigger Class Distribution | ClickHouse | Class A/B spike = WARN |
| Recovery SLA Compliance | ClickHouse | Miss >10% = ALERT |
| Unauthorized Trigger Attempts | Prometheus | Any = CRITICAL |
| Audit Write Failure Rate | Prometheus | Any = CRITICAL |

### Loki Log Labels

All NSPA logs must carry:

```
{
  "agent": "NSPA",
  "contract_id": "<UUID>",
  "phase": "<N>",
  "severity": "LEVEL_<N>",
  "tenant_scope": "<tenant_id or PLATFORM>",
  "trigger_id": "<trigger_id>",
  "event_type": "<state_machine_event>"
}
```

### OpenTelemetry Trace

Every NSPA shutdown execution creates a root span:

```
span: nspa.shutdown.execute
  child: nspa.phase.1.media_isolation
  child: nspa.phase.2.realtime_shutdown
  child: nspa.phase.3.auth_suspension
  child: nspa.phase.4.core_suspension
  child: nspa.phase.5.data_protection
  child: nspa.phase.6.audit_seal
  child: nspa.phase.7.recovery_contract
```

Traces are exported via OpenTelemetry collector → Grafana Tempo.

---

## SECTION XII — DEPLOYMENT SPECIFICATION

### Kubernetes Namespace

```yaml
namespace: ops
deployment: nspa
replicas: 2  # Active-Passive HA — passive takes over on liveness failure
```

### Resource Constraints

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "2000m"
    memory: "2Gi"
```

### Dependencies (All must be healthy before NSPA starts)

```
Redis (state machine store + fallback checkpoint)
PostgreSQL (ops schema — contracts, audit)
etcd (phase checkpoints, distributed lock)
MinIO (WORM audit bucket — ops-audit)
Keycloak (token revocation endpoint)
Kong Admin API (maintenance mode toggle)
Wazuh API (alert mode control)
Admin Governance Service (notification dispatch)
```

### Environment Variables (No hardcoded values — all from HashiCorp Vault)

```
NSPA_REDIS_URL               → vault:secret/ops/redis/url
NSPA_PG_URL                  → vault:secret/ops/postgresql/url
NSPA_ETCD_ENDPOINTS          → vault:secret/ops/etcd/endpoints
NSPA_MINIO_ENDPOINT          → vault:secret/ops/minio/endpoint
NSPA_MINIO_WORM_BUCKET       → vault:secret/ops/minio/worm_bucket
NSPA_KEYCLOAK_ADMIN_URL      → vault:secret/ops/keycloak/admin_url
NSPA_KONG_ADMIN_URL          → vault:secret/ops/kong/admin_url
NSPA_WAZUH_API_URL           → vault:secret/ops/wazuh/api_url
NSPA_ADMIN_GOV_SERVICE_URL   → vault:secret/ops/admin_governance/url
NSPA_JITSI_ADMIN_URL         → vault:secret/media/jitsi/admin_url
NSPA_LIVEKIT_API_KEY         → vault:secret/media/livekit/api_key
NSPA_NOTIFICATION_SECRET     → vault:secret/ops/notifications/hmac_secret
```

### CI/CD Gate (Forgejo Actions / GitLab CI)

```yaml
NSPA deployment gate requires:
  - unit_tests: PASS
  - integration_tests: PASS (against staging NSPA instance)
  - security_scan: PASS (Trivy image scan — zero CRITICAL CVEs)
  - phase_simulation_test: PASS (all 7 phases executed against mock infra)
  - recovery_contract_test: PASS (contract generation + authorization flow)
  - chaos_test: PASS (NSPA pod kill mid-shutdown — verify etcd resume)
```

---

## SECTION XIII — WAZUH + SIEM INTEGRATION

The NSPA operates as a **consumer** of Wazuh SIEM alerts and as a **controller** of Wazuh response mode.

### NSPA as Wazuh Consumer

Wazuh alert rules that map to NSPA trigger classes:

| Wazuh Rule Group | NSPA Trigger Class | Action |
|---|---|---|
| `web_attack_*` | Class A (NIT-001) | Forward to NSPA trigger endpoint |
| `authentication_failed_*` | Class B (ATT-001, ATT-003) | Forward with count aggregation |
| `vault_*` | Class B (ATT-004, ATT-005) | Forward immediately (no aggregation) |
| `postgresql_write_*` (unexpected) | Class C (GDT-003) | Forward immediately |
| `pii_egress_*` | Class F (CLT-001) | Forward immediately — highest priority |

### NSPA as Wazuh Controller

- During Phase 6 (Audit Seal): NSPA sets Wazuh to `LISTEN_ONLY` — prevents automated firewall changes during shutdown that could interfere with audit writes
- During Recovery Phase R1: NSPA restores Wazuh to `ACTIVE_RESPONSE`
- NSPA never permanently disables Wazuh — only mode switches with audit trail

---

## SECTION XIV — ANTI-PATTERNS (FORBIDDEN)

The following behaviors are **explicitly forbidden** within the NSPA and any system that interfaces with it.

```
FORBIDDEN-01  Triggering shutdown based on unregistered event type
FORBIDDEN-02  Skipping any phase in the ordered shutdown sequence
FORBIDDEN-03  Executing partial phase (phase must be atomic — all or rollback)
FORBIDDEN-04  Initiating recovery without a valid Recovery Contract
FORBIDDEN-05  Manual database writes to any data store during NSPA-active state
FORBIDDEN-06  Bypassing Admin Governance Service for LEVEL 6 authorization
FORBIDDEN-07  Finalizing GD scores during shutdown
FORBIDDEN-08  Deleting rather than suspending Kafka consumer groups
FORBIDDEN-09  Restarting Jitsi rooms to "fix" a suspended session
FORBIDDEN-10  Writing to MinIO WORM bucket outside of NSPA audit writes
FORBIDDEN-11  Clearing etcd NSPA checkpoints manually
FORBIDDEN-12  Running Flyway migrations during NSPA-active state
FORBIDDEN-13  Issuing new JWT access tokens while Keycloak revocation list is active
FORBIDDEN-14  Sending user-facing "everything is fine" notifications while NSPA is active
FORBIDDEN-15  Auto-promoting any user belt or certification during shutdown state
```

Any system that violates a FORBIDDEN rule while NSPA is active triggers `FORBIDDEN_VIOLATION_DETECTED` — which is itself a trigger class event — escalating the current shutdown level by +1.

---

## SECTION XV — COMPLIANCE RECORD

### Regulatory Alignment

| Regulation | NSPA Enforcement Mechanism |
|---|---|
| GDPR Article 32 (Security of Processing) | PII encryption at rest maintained during shutdown; no plaintext egress permitted |
| GDPR Article 17 (Right to Erasure) | Erasure requests queued during shutdown, executed post-recovery with audit |
| Indian IT Act 2000 (Data Protection) | Tenant data isolation laws enforced at infrastructure level |
| ISO 27001 A.16 (Incident Management) | Trigger → classify → respond → recover → document lifecycle |
| SOC 2 Type II (Availability) | Recovery SLA tracking via ClickHouse, Prometheus — auditable |
| DPDP Act 2023 (India) | Minor data elevated protection enforced via Rule AG-01 |

### Immutable Audit Trail

Every NSPA action produces an immutable audit record stored in two locations:

1. **MinIO WORM bucket** (`ops-audit`) — primary, append-only, 15-year retention
2. **ClickHouse** (`audit_events` table) — secondary, query-optimized, anomaly detection enabled

Audit record schema:

```json
{
  "event_id": "uuid-v4",
  "contract_id": "uuid-v4",
  "agent": "NSPA",
  "event_type": "SHUTDOWN_PHASE_COMPLETE | TRIGGER_RECEIVED | RECOVERY_AUTHORIZED | ...",
  "trigger_id": "NIT-001 | ATT-003 | ...",
  "severity_level": 0-6,
  "phase": 1-7,
  "tenant_scope": ["tenant_id_1", "..."],
  "namespace_scope": ["core", "realtime", "..."],
  "executing_user": "NSPA_AGENT | admin_user_id",
  "human_override": false,
  "timestamp_utc": "ISO-8601",
  "data_integrity_hash": "sha256-hex",
  "outcome": "SUCCESS | FAILURE | TIMEOUT | ESCALATED"
}
```

---

## SECTION XVI — VERSION CONTROL + MUTATION POLICY

```
Version       : v1.0
Date          : SEALED AT DECLARATION
Author        : ANTIGRAVITY PLATFORM ENGINEERING COUNCIL
Mutation Rule : This document may only be extended — never mutated.
                New rules are appended with version bump.
                Existing rules cannot be modified without full council declaration.
                Any modification without version bump = UNAUTHORIZED_MUTATION
                → triggers CLT-002 (Immutable Archive tamper alert)

Version History:
  v1.0  — Initial sealed production artifact
```

---

## SECTION XVII — FINAL ENFORCEMENT DECLARATION

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│   This document is SEALED.                                      │
│                                                                 │
│   The NETWORK_SHUTDOWN_PROTOCOL_AGENT executes exactly          │
│   what is declared here — nothing more, nothing less.           │
│                                                                 │
│   Human judgment does not replace protocol.                     │
│   AI inference does not replace protocol.                       │
│   Business pressure does not replace protocol.                  │
│                                                                 │
│   The protocol IS the trust.                                    │
│   The audit IS the proof.                                       │
│   The recovery contract IS the authorization.                   │
│                                                                 │
│   ANTIGRAVITY platform users, tenants, students, parents,       │
│   recruiters, enterprises, and mentors are protected not        │
│   by human goodwill — but by enforced, logged, deterministic    │
│   infrastructure behavior.                                      │
│                                                                 │
│   EXECUTION_MODE   = LOCKED                                     │
│   MUTATION_POLICY  = ADD_ONLY                                   │
│   INTERPRETATION   = FORBIDDEN                                  │
│   STATUS           = PRODUCTION READY                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

*NETWORK_SHUTDOWN_PROTOCOL_AGENT v1.0 — ANTIGRAVITY Enterprise Optimization + Trust Infrastructure — SEALED*
