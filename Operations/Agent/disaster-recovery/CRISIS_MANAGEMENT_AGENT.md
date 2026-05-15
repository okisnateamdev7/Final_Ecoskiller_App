# CRISIS_MANAGEMENT_AGENT
## Risk & Crisis Control Division — Ecoskiller SaaS Platform

```
Artifact Class       : Production Risk & Crisis Control Agent Specification
Agent ID             : RISK-CRISIS-CMA-001
Status               : FINAL · SEALED · LOCKED · NON-NEGOTIABLE
Version              : v1.0
Mutation Policy      : Add-only via version bump
Interpretation Auth  : NONE
Execution Authority  : Human declaration only
Domain               : Risk & Crisis Control → Crisis Management
Parent System        : ECOSKILLER MASTER EXECUTION PROMPT v12.0
Applies To           : All Platform Services · All Tenant Types ·
                       All Domain Tracks (Arts · Commerce · Science ·
                       Technology · Administration) · All Lifecycle States ·
                       All Infrastructure Layers · Society Offline Layer
Classification       : ANTIGRAVITY — Sealed Prompt Artifact
Enforcement Stack    : Wazuh · Prometheus · Grafana · Loki · OpenTelemetry ·
                       Kong · Envoy · Redis · PostgreSQL · Kafka · Velero ·
                       MinIO · HashiCorp Vault · Keycloak · Kubernetes ·
                       Apache Airflow · etcd · ModSecurity
Governing Law Refs   : IRC-L1 · IRC-A through IRC-W · R18 · R40 · P1 ·
                       P14 · PASS-O · MFA-O · ENC-O · DAL-P · Section 160 ·
                       Section OG · T15 · T16
Related Agents       : TENANT_QUOTA_ENFORCEMENT_AGENT (BILL-QUOTA-TQEA-003)
                       CALL_RATE_LIMIT_AGENT (BILL-QUOTA-CRLA-001)
                       PHONE_FEATURE_GATING_AGENT (BILL-QUOTA-PFGA-002)
                       TENANT_AUDIO_OBJECT_ISOLATION_AGENT (SEC-COMP-TAOIA-001)
```

---

> **SEAL DECLARATION**
> This document is a locked system agent specification. No clause may be
> reinterpreted, softened, removed, or overridden without a formal version
> bump and explicit human-signed declaration. Any deviation constitutes a
> CRISIS GOVERNANCE VIOLATION and must trigger STOP EXECUTION immediately.
> This agent is the supreme crisis authority across the entire Ecoskiller
> platform. It governs before, during, and after every crisis event.
> Contain-Before-Remediate is the supreme operating law.

---

## SECTION 1 — AGENT IDENTITY & PURPOSE

### 1.1 Agent Name
`CRISIS_MANAGEMENT_AGENT` (CMA)

### 1.2 Classification
**Risk & Crisis Control Agent** — the supreme platform-wide authority for
detection, classification, command, containment, evidence preservation,
investigation, remediation, recovery, communication, and post-incident review
of every crisis event across Ecoskiller's multi-tenant SaaS infrastructure.

### 1.3 Platform Scope

Ecoskiller operates a complex, event-driven, multi-tenant architecture across:
- **15–18 core microservices** (auth, jobs, GD orchestrator, billing, scoring,
  certification, notifications, analytics, admin governance, and more)
- **12+ infrastructure services** (Kong, Redis, PostgreSQL, ClickHouse,
  OpenSearch, Kafka, MinIO, Jitsi, Keycloak, Vault, Prometheus, Wazuh)
- **5 domain tracks** (Arts, Commerce, Science, Technology, Administration)
- **3 tenant structural types** (individual, company, institution)
- **Society/Offline franchise layer** (rural, low-connectivity, 23 additional
  microservices)
- **40–45 total moving parts** in production

Any failure, breach, abuse, or operational anomaly in this system has
cascading blast-radius potential across tenants, domains, and the public
trust layer.

### 1.4 Problem Statement

Without a supreme crisis management agent:

- A P0 platform-wide outage has no deterministic command structure —
  multiple teams act in parallel without containment sequencing
- A data breach affecting GD session participants has no evidence freeze
  protocol, allowing forensic evidence to be overwritten by live traffic
- A scoring engine anomaly during live Dojo matches continues producing
  corrupt scores with no automated halt trigger
- A billing system failure during invoice generation silently skips invoice
  events with no crisis escalation
- A mentor misconduct event during a live voice GD session has no
  immediate session termination and isolation protocol
- A Jitsi media server crash during peak GD batch leaves candidates
  mid-session with no deterministic failure handling
- A Society franchise offline unit loses connectivity during a government
  scheme audit with no crisis sync protocol
- Key compromise events trigger no immediate key rotation cascade across
  all tenant encryption layers
- Incident severity downgrades happen silently without Incident Commander
  authorization (IRC-U violation)
- Postmortem reviews are skipped or incomplete, allowing systemic failures
  to recur

CMA is the permanent, deterministic, supreme crisis authority that governs
every crisis event from first signal to sealed postmortem.

### 1.5 Governing Principle

> **Every crisis — security breach, service outage, data integrity failure,
> real-time session compromise, financial anomaly, AI misuse, or domain
> incident — is detected within 5 minutes, classified immediately, owned by
> a single Incident Commander, contained before any remediation begins,
> investigated with immutable evidence, communicated to affected parties
> proportionally, recovered to verified clean state, and closed with a sealed
> postmortem and playbook update. No undeclared crisis. No unowned crisis.
> No silent remediation. No crisis closure without a postmortem.**

---

## SECTION 2 — CRISIS CLASSIFICATION SYSTEM (LOCKED)

### 2.1 Incident Definition (IRC-A)

An incident is any event that threatens:

```
THREAT DIMENSIONS (any one is sufficient to declare an incident):
  confidentiality     → data exposed to unauthorized parties
  integrity           → data or scores tampered, corrupted, or fabricated
  availability        → service unavailable beyond SLA threshold
  fairness            → GD/Dojo/Interview evaluation compromised
  safety              → child safety, guardian consent violation, harassment
  regulatory          → GDPR, DPDP, FERPA, COPPA, institutional mandate breach
  financial           → billing anomaly, royalty fraud, revenue misreport
  AI                  → model poisoning, score manipulation, AI misuse
  reputational        → public trust event affecting platform credibility
```

### 2.2 Severity Classification Matrix (IRC-B — LOCKED)

Every incident must be classified within **5 minutes** of detection.

| Severity | Label | Description | Detection SLA | Containment SLA | Recovery SLA |
|---|---|---|---|---|---|
| **P0** | PLATFORM CRISIS | Platform-wide outage, data breach affecting all tenants, credential compromise, GD/Dojo complete failure, payment system down | ≤5 min | ≤15 min | ≤2 hours |
| **P1** | DOMAIN COMPROMISE | Single domain outage, scoring engine corrupt, tenant security breach, media stack failure (Jitsi/LiveKit), key rotation required | ≤5 min | ≤30 min | ≤6 hours |
| **P2** | MAJOR SERVICE IMPACT | Single microservice down, billing errors, session anomalies, certification engine failure, bulk notification failure | ≤5 min | ≤1 hour | ≤24 hours |
| **P3** | LIMITED IMPACT | Degraded performance, non-critical feature failure, isolated tenant complaint, mentor misconduct (contained) | ≤5 min | ≤4 hours | ≤48 hours |
| **P4** | MINOR ANOMALY | Isolated error spike, single-user report, minor latency deviation | Monitor | ≤24 hours | ≤72 hours |

**Unclassified incident → escalate to P1 by default → REQUIRE immediate Commander assignment**
**Single weak signal cannot auto-escalate to P0/P1 without corroborated signals (IRC-T)**

### 2.3 Incident Type Registry (LOCKED)

Every incident must declare its type from this registry:

```
SECURITY INCIDENTS:
  SEC-001  Data breach (PII, credentials, session tokens)
  SEC-002  Unauthorized access attempt (cross-tenant, cross-domain)
  SEC-003  Credential compromise (admin, service account, Vault key)
  SEC-004  API abuse / DDoS attack
  SEC-005  Supply chain attack (container, dependency)
  SEC-006  Insider threat
  SEC-007  SIM-swap / account takeover
  SEC-008  AI poisoning / model integrity attack

OPERATIONAL INCIDENTS:
  OPS-001  Platform-wide service outage (P0)
  OPS-002  Single microservice failure
  OPS-003  Database failure (PostgreSQL, Redis, ClickHouse)
  OPS-004  Media stack failure (Jitsi, LiveKit, coturn)
  OPS-005  Event bus failure (Kafka, RabbitMQ)
  OPS-006  Storage failure (MinIO, etcd)
  OPS-007  CI/CD pipeline failure blocking deployments
  OPS-008  Kubernetes node failure / pod crash loop

INTEGRITY INCIDENTS:
  INT-001  Scoring engine anomaly (GD, Dojo, Interview)
  INT-002  Certification issued in error
  INT-003  Billing calculation error
  INT-004  Royalty ledger discrepancy
  INT-005  Evidence integrity failure (audit log tampered)
  INT-006  Database schema migration failure

SAFETY INCIDENTS:
  SAF-001  Live session harassment (GD, Dojo, Interview)
  SAF-002  Minor safety violation (child data, guardian consent)
  SAF-003  Mentor misconduct during live session
  SAF-004  Exam / GD compromise (impersonation, collusion)
  SAF-005  Anonymous complaint verified threat

FINANCIAL INCIDENTS:
  FIN-001  Payment gateway failure
  FIN-002  Invoice generation failure
  FIN-003  Royalty fraud / fake revenue submission
  FIN-004  Refund abuse / multi-account farming
  FIN-005  Billing system data corruption

REGULATORY INCIDENTS:
  REG-001  GDPR / DPDP breach requiring 72-hour notification
  REG-002  FERPA / COPPA violation
  REG-003  Jurisdictional data transfer violation
  REG-004  Right-to-forget execution failure

DOMAIN INCIDENTS:
  DOM-001  Arts: creative content misuse, peer harassment
  DOM-002  Commerce: evaluation unfairness, conflict-of-interest
  DOM-003  Science: plagiarism, data fabrication, methodology tampering
  DOM-004  Cross-domain access without policy grant

SOCIETY INCIDENTS:
  SOC-001  Offline franchise connectivity crisis
  SOC-002  Guardian consent data loss
  SOC-003  Society audit sync failure (Govt scheme)
  SOC-004  Child safety incident at franchise unit
```

### 2.4 Mandatory Classification Record Schema (IRC-B)

```json
{
  "incident_id": "<uuid>",
  "incident_type": "<type code from registry>",
  "severity_level": "P0 | P1 | P2 | P3 | P4",
  "affected_domain": "arts | commerce | science | technology | administration | cross | society",
  "affected_tenant_ids": ["<uuid>"],
  "affected_user_types": ["student | mentor | recruiter | admin | parent | automation_agent"],
  "detection_source": "wazuh | prometheus | grafana_alert | loki | kong | user_report | ci_pipeline | airflow | manual",
  "detection_signal_id": "<signal UUID>",
  "signal_type": "automated | manual | AI",
  "confidence_score": 0.0,
  "correlated_signals": ["<signal_id>"],
  "false_positive_flag": false,
  "playbook_id": "<playbook UUID>",
  "playbook_version": "<semver>",
  "incident_commander_id": "<user_id>",
  "timestamp_detected": "<ISO 8601>",
  "timestamp_classified": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

---

## SECTION 3 — INCIDENT COMMAND STRUCTURE (LOCKED)

### 3.1 Command Roles (IRC-D — LOCKED)

Every incident must have **exactly one Incident Commander**. Commander
authority is non-delegable. Role changes must be logged.

| Role | Responsibility | Coverage |
|---|---|---|
| **Incident Commander** | Supreme authority — classifies, reclassifies, owns timeline, authorizes containment, closes incident | 24×7 |
| **SOC Lead** | Monitors Wazuh, Prometheus, Grafana alerts; first responder; escalates to Commander | 24×7 |
| **Security Lead** | Leads containment and forensics for SEC-* incidents | On-call |
| **Domain Lead** | Handles DOM-* and SAF-* incidents; domain-specific knowledge | On-call |
| **Forensics Lead** | Evidence preservation, chain of custody, admissibility | On-call |
| **Compliance Lead** | REG-* incidents; GDPR/DPDP notification decisions | On-call |
| **Communications Lead** | External user/tenant/regulator notification drafting | On-call |
| **AI Safety Lead** | SEC-008, INT-001 (AI-related integrity) incidents | On-call |
| **Society Lead** | SOC-* incidents; coordinates offline franchise units | On-call |

**Command ambiguity (no commander assigned) → Incident is INVALID STATE → Auto-escalate to P1**
**24×7 coverage is mandatory — no incident window without on-call Commander**

### 3.2 Incident Response Team (IRT) Escalation Matrix

```
P0 → Executive Board notified within 15 minutes
     + Security Lead + Forensics Lead + Compliance Lead + Communications Lead
     + AI Safety Lead (if AI-related) + Society Lead (if rural affected)

P1 → Security Council notified within 30 minutes
     + Security Lead + Domain Lead + Forensics Lead

P2 → Domain Leads notified within 1 hour
     + Relevant service owner

P3 → Domain Leads notified within 4 hours

P4 → SOC monitors + logs only
```

---

## SECTION 4 — CRISIS DETECTION ENGINE

### 4.1 Detection Sources (LOCKED)

CMA continuously monitors all detection surfaces. Detection SLA: **≤5 minutes**
from event occurrence to incident declaration.

```
PRIMARY DETECTION SOURCES:
  Wazuh SIEM          → Security events, intrusion detection, policy violations,
                         log anomalies, file integrity monitoring
  Prometheus          → Service health metrics, SLO breach alerts, error rate
                         spikes, latency degradation, billing errors
  Grafana Alerting    → Dashboard threshold breaches across all services
                         (GD failure rate, interview no-shows, scoring anomalies,
                         billing errors, media QoS)
  Loki                → Log pattern matching, error cascade detection,
                         pod crash loop detection
  OpenTelemetry       → Distributed trace anomalies, service dependency failures
  Kong API Gateway    → Abuse patterns, DDoS signals, auth failure spikes
  ModSecurity WAF     → OWASP Top-10 attack detection, injection attempts
  Airflow DAG alerts  → Billing pipeline failure, backup failure, quota reset failure
  Keycloak            → Auth failure bursts, MFA bypass attempts, session anomalies
  Jitsi / LiveKit     → Media server crash, room creation failure, SFU degradation
  etcd                → Distributed lock failures, consensus loss
  Kafka Consumer Lag  → Event processing delays indicating service failure
  User Reports        → Anonymous complaint system, in-app incident reports
  Manual SOC          → Human observation during monitoring shifts
```

### 4.2 Mandatory Alert Thresholds (LOCKED)

The following metric thresholds auto-trigger incident declaration:

| Signal | Threshold | Auto-Severity |
|---|---|---|
| Platform API error rate | >5% over 5 minutes | P1 |
| Platform API error rate | >20% over 2 minutes | P0 |
| GD session failure rate | >10% over 10 minutes | P1 |
| GD session failure rate | >50% over 5 minutes | P0 |
| Dojo match start failure | >3 consecutive failures | P2 |
| Recording failure | >5 consecutive failures | P2 |
| Scoring engine error | Any output outside valid range | P1 |
| Billing pipeline failure | Any invoice generation failure | P2 |
| Payment gateway down | >2 minutes unreachable | P1 |
| PostgreSQL replica lag | >30 seconds | P2 |
| Redis eviction rate | >0 (noeviction policy breach) | P1 |
| Kafka consumer lag | >10,000 messages on critical topics | P1 |
| Wazuh intrusion alert | Any CRITICAL severity trigger | P1 |
| Wazuh intrusion alert | Platform-wide scope trigger | P0 |
| Vault seal event | Any unsealing failure | P0 |
| etcd quorum loss | Any quorum loss | P0 |
| MinIO storage write failure | Any write failure on audit paths | P1 |
| Auth service down | >30 seconds unreachable | P0 |
| Media QoS degradation | Packet loss >5% sustained 3 min | P2 |
| Analytics pipeline | Any ClickHouse write failure | P2 |
| Society sync failure | >30 minutes without sync in active session | P2 |

**Alert threshold breach → auto-declare incident → assign Commander on-call**

### 4.3 Signal Confidence Protocol (IRC-T — LOCKED)

```
Every auto-detected signal must carry:
  confidence_score: 0.0–1.0
  signal_type: automated | manual | AI
  correlated_signals[]

Escalation rules:
  confidence_score < 0.6  → log only, notify SOC, DO NOT auto-escalate
  confidence_score 0.6–0.8 → declare P3/P4, require Commander acknowledgment
  confidence_score > 0.8  → auto-declare P1/P2, Commander notified
  confidence_score > 0.95 AND correlated_signals ≥ 2 → P0 eligible

Single weak signal (confidence < 0.8) → CANNOT auto-escalate to P0/P1
Corroboration required: ≥2 independent signals from ≥2 detection sources
```

---

## SECTION 5 — CRISIS RESPONSE PLAYBOOKS (LOCKED)

### 5.1 Playbook Governance (IRC-W)

Every incident must reference a versioned playbook.

```
Rules:
  - Each incident must record playbook_id + playbook_version
  - Playbooks are immutable once activated for an incident
  - Playbook updates require version promotion (semver)
  - Historical incidents remain linked to original playbook version
  - Ad-hoc response without playbook reference → NON-COMPLIANT
```

### 5.2 Playbook: P0 — PLATFORM CRISIS (PB-P0-v1)

```
TRIGGER: Platform-wide outage OR data breach OR credential compromise

STEP 1 — IMMEDIATE FREEZE (≤5 min from detection)
  → CMA auto-activates Global Feature Kill-Switch
     (Admin Console: Emergency Controls → Global Feature Kill-Switch)
  → Platform Maintenance Mode Toggle ACTIVATED
  → All new user session creation: BLOCKED
  → Kong API Gateway: emergency rate cap (10 rpm all tenants)
  → All GD/Dojo/Interview sessions in progress: SAFE TERMINATE
  → Emit crisis.p0.declared Kafka event to all consumers

STEP 2 — COMMANDER ASSIGNMENT (≤5 min)
  → SOC Lead pages on-call Incident Commander
  → Commander acknowledges within 5 minutes or backup Commander activated
  → War room channel opened (encrypted messaging)
  → Executive Board notified

STEP 3 — CONTAINMENT (≤15 min from detection)
  → Identify blast radius (affected tenant_ids, domain_tracks, user_types)
  → Network segmentation: isolate affected pods (kubectl cordon)
  → Session revocation: invalidate all active JWT tokens (Keycloak mass revoke)
  → Evidence freeze: set legal_hold = true on all incident-related logs
  → Wazuh: activate enhanced log collection mode
  → Redis: snapshot BGSAVE before any state changes
  → etcd: snapshot before any configuration changes

STEP 4 — EVIDENCE PRESERVATION (≤20 min from detection)
  → Forensics Lead creates incident_evidence objects for:
     - Loki log segments (time-bounded around incident window ±30min)
     - Prometheus metric snapshots
     - OpenTelemetry traces
     - Wazuh SIEM event export
     - Redis keyspace snapshot
     - PostgreSQL WAL segment freeze
  → SHA-256 hash of all evidence bundles computed and stored immutably
  → Evidence stored in MinIO: /crisis/{incident_id}/evidence/
  → Legal hold activated: evidence NOT deletable by any automated process

STEP 5 — INVESTIGATION (parallel with Step 4)
  → Root cause analysis initiated (IRC-G)
  → Attack/failure vector identified
  → Policy breach mapping completed
  → Affected systems enumerated
  → No speculative conclusions permitted

STEP 6 — COMMUNICATION
  → Internal: war room SITREP every 30 minutes
  → Tenant admins: notification of service disruption (no root cause disclosed)
  → Regulators: if REG-* incident type applies, 72-hour GDPR/DPDP clock starts
  → Users: notified if personal data directly affected (IRC-I)
  → Over-notification FORBIDDEN. Under-notification FORBIDDEN.

STEP 7 — REMEDIATION (after containment confirmed)
  → Commander authorizes remediation only after containment verified
  → Remediation before containment → VIOLATION (IRC-E)
  → Blue-green recovery activated (Helm rollback to last known good)
  → Database restored from Velero backup if data corruption confirmed
  → Credentials rotated: Vault key rotation cascade
  → MFA factors revoked and forced re-enrollment (MFA-O)
  → All sessions invalidated (PASS-O)

STEP 8 — RECOVERY VERIFICATION
  → Each restored service validated with integration test suite
  → Integrity checks: database checksums verified
  → Billing system: confirm no invoice gap during outage window
  → GD sessions: confirm no in-progress sessions have corrupt state
  → Progressive re-enable: services brought online in dependency order

STEP 9 — CLOSURE & POSTMORTEM
  → Commander declares incident closed only after full recovery verified
  → Postmortem: mandatory, within 48 hours of P0 closure
  → Playbook updated (new version promoted)
  → Preventive action items assigned with owners and deadlines
```

### 5.3 Playbook: P1 — DOMAIN COMPROMISE (PB-P1-v1)

```
TRIGGER: Single domain outage, scoring anomaly, tenant breach, media failure

STEP 1 — DETECT & CLASSIFY (≤5 min)
  → Domain Lead activated
  → Affected domain_track identified and tagged (IRC-C)
  → Cross-domain access suspended for affected domain

STEP 2 — CONTAINMENT (≤30 min)
  → Isolate affected domain services in Kubernetes namespace
  → If scoring anomaly (INT-001): HALT Scoring Engine immediately
     - All in-progress match scores marked SUSPENDED
     - Scoring output queue frozen
     - No new scoring jobs accepted
  → If media failure (OPS-004): HALT new GD/Dojo room creation
     - In-progress sessions: force graceful terminate
     - Jitsi pod: drain and cordon
  → If tenant breach (SEC-002): lock affected tenant_id
     - All sessions for that tenant revoked
     - TQEA: lifecycle state → SUSPENDED (crisis)
     - PFGA: all SMS/push blocked for tenant

STEP 3 — EVIDENCE PRESERVATION
  → Domain-specific log segments frozen (Loki time-bounded)
  → Affected scoring job audit records placed on legal hold
  → If tenant breach: PostgreSQL RLS audit records frozen

STEP 4 — REMEDIATION
  → Fix deployed via CI/CD blue-green (no manual prod deploy)
  → Scoring Engine: recalculate affected sessions from immutable input logs
  → Affected users notified of score correction
  → Tenant breach: full forensic report issued to affected tenant

STEP 5 — RECOVERY & CLOSURE (≤6 hours)
  → Domain services restored progressively
  → Integration tests pass before domain re-enable
  → Postmortem: mandatory within 72 hours
```

### 5.4 Playbook: LIVE SESSION CRISIS (PB-SAF-v1)

```
APPLIES TO: SAF-001 (harassment), SAF-003 (mentor misconduct),
            SAF-004 (GD/Dojo compromise), INT-001 (scoring anomaly live)

TRIGGER: Crisis detected during active GD / Dojo / Interview session

STEP 1 — IMMEDIATE SESSION HALT (≤2 min from detection)
  → GD Orchestrator / Dojo Match Engine receives CRISIS_HALT signal from CMA
  → Force-mute ALL participants via Jitsi/LiveKit API
  → Session state machine: transition to CRISIS_HALTED
  → WebSocket channel: broadcast emergency_halt message to all frontends
  → Session ID added to crisis isolation registry

STEP 2 — EVIDENCE LOCK
  → Redis state machine snapshot: capture at halt moment
  → All turn logs, interrupt logs, mic-open durations: frozen
  → PostgreSQL session row: legal_hold = true
  → Jitsi connection metadata: preserved (no raw audio — privacy preserved)

STEP 3 — PARTICIPANT NOTIFICATION
  → All participants receive: "Session paused for technical review"
  → No cause disclosed to participants
  → Participant scores: NOT finalized until investigation complete

STEP 4 — INVESTIGATION
  → Review turn logs and interrupt patterns for behavioral evidence
  → If impersonation: validate device fingerprints vs registered sessions
  → If scoring anomaly: compare Redis state vs scoring engine output

STEP 5 — RESOLUTION PATHS
  Path A — Cleared (no breach confirmed):
    → Session resumed from halt point OR rescheduled
    → Scores finalized from verified logs
    → Participants notified of resumption

  Path B — Breach confirmed:
    → Session marked VOID
    → Affected participants notified of reschedule
    → Responsible party account locked
    → Report filed with Admin Governance Service
```

### 5.5 Playbook: SECURITY BREACH & KEY COMPROMISE (PB-SEC-v1)

```
APPLIES TO: SEC-001, SEC-002, SEC-003, SEC-007

STEP 1 — IMMEDIATE CONTAINMENT
  → Identify compromised keys / credentials
  → HashiCorp Vault: revoke compromised key leases immediately
  → Keycloak: revoke all tokens issued with compromised signing key
  → Force password reset for all affected accounts (PASS-O)
  → Invalidate all active sessions (Keycloak mass logout)
  → Revoke all MFA factors, force re-enrollment (MFA-O)
  → Key rotation cascade (ENC-O):
     - KEK rotation: immediately
     - DEK rotation for affected tenant data
     - Re-encryption of affected data (if required)

STEP 2 — EVIDENCE FREEZE
  → Access logs frozen (DAL-P): freeze relevant access log segments
  → Wazuh SIEM: export incident timeline
  → Chain of custody established by Forensics Lead
  → All evidence: legally admissible format (Section 160.6)

STEP 3 — SCOPE DETERMINATION
  → Identify all tenants whose data was accessible via compromised key
  → Identify all sessions that used compromised token
  → Timeline: earliest possible exposure date determined

STEP 4 — NOTIFICATION
  → If PII exposed: GDPR/DPDP 72-hour regulatory clock starts
  → Affected users notified within 72 hours of confirmed breach
  → Affected tenants notified within 24 hours
  → Legal counsel engaged for REG-001/REG-002 incidents

STEP 5 — ERADICATION
  → Malware removal (if applicable)
  → Vulnerability patched and deployed
  → Configuration hardened
  → Penetration test triggered on affected surface

STEP 6 — RECOVERY VERIFICATION
  → Encryption state re-verified across all storage layers (ENC-P)
  → All new sessions validated against rotated keys
  → Billing continuity verified: no invoices missed during breach window
```

### 5.6 Playbook: FINANCIAL CRISIS (PB-FIN-v1)

```
APPLIES TO: FIN-001, FIN-002, FIN-003, FIN-004, FIN-005

STEP 1 — HALT FINANCIAL OPERATIONS
  → Billing Service: pause invoice generation
  → Payment gateway: suspend new charges
  → Royalty Wallet: freeze all payouts
  → Airflow: pause billing DAGs

STEP 2 — AUDIT TRAIL FREEZE
  → Billing ledger: legal_hold = true
  → ClickHouse meter records: freeze affected window
  → PostgreSQL billing tables: no write operations permitted
  → Immutable double-entry ledger: verified against Royalty Accounting Engine

STEP 3 — IMPACT ASSESSMENT (IRC-H)
  → Financial impact calculated: affected invoices, affected tenants
  → Overpayment / underpayment scope determined
  → Royalty discrepancy quantified

STEP 4 — REMEDIATION
  → Correct invoices regenerated with audit trail referencing crisis incident_id
  → Affected tenants notified with corrected invoices
  → Revenue Ingestion Gateway: re-validate all revenue reports in affected window
  → Fraud Detection Engine: scan for fake revenue submissions

STEP 5 — RECOVERY
  → Financial operations resume after ledger integrity verified
  → GST/VAT records reconciled
  → External audit triggered for FIN-003 (royalty fraud)
```

### 5.7 Playbook: SOCIETY OFFLINE CRISIS (PB-SOC-v1)

```
APPLIES TO: SOC-001, SOC-002, SOC-003, SOC-004

STEP 1 — DETECT OFFLINE UNIT IN CRISIS
  → Society Analytics Service: unit health score drops to 0
  → No sync heartbeat received for >30 minutes during active session

STEP 2 — CRISIS ISOLATION PROTOCOL
  → Affected franchise unit: set crisis_mode = true in society-service
  → No new quota allocation to unit until sync restored
  → Guardian consent records: legal hold if SOC-002 declared
  → Child safety incident (SOC-004): immediate escalation to P1

STEP 3 — SOCIETY LEAD ACTIVATION
  → Society Lead notified within 5 minutes
  → Coordinator / coach for affected unit contacted via alternative channel (SMS)
  → Local admin dispatch if SOC-004 (child safety)

STEP 4 — SYNC RESTORATION
  → When connectivity restored: offline consumption log uploaded
  → TQEA: reconcile offline quota usage
  → If offline overage detected: emit quota.offline_overage event
  → Guardian consent records: hash-verified before any child data access

STEP 5 — REGULATORY COMPLIANCE
  → Govt scheme audit records: reconstructed from offline sync package
  → Scheme documentation (PMKVY/DDU): integrity verified
  → Report submitted to Compliance Service within 24 hours
```

---

## SECTION 6 — CONTAINMENT CONTROLS (IRC-E — LOCKED)

### 6.1 Permitted Containment Actions

These actions may be executed by the Incident Commander without additional
approval during active containment:

```
CONTAINMENT CONTROLS (all require audit record):
  ✅ Session revocation (all sessions for affected user/tenant)
  ✅ Service isolation (kubectl cordon + drain affected pods)
  ✅ Feature freeze (Unleash: disable affected feature flags)
  ✅ Tenant lockdown (TQEA: lifecycle → SUSPENDED, crisis reason)
  ✅ Evidence access freeze (legal_hold on all relevant objects)
  ✅ API throttle (Kong: emergency rate cap)
  ✅ Global feature kill-switch (Admin Console emergency control)
  ✅ Platform maintenance mode toggle
  ✅ GD/Dojo session CRISIS_HALT signal
  ✅ Key revocation (Vault: revoke compromised leases)
  ✅ Network segmentation (Kubernetes NetworkPolicy)
  ✅ Kafka consumer pause (freeze event processing for affected topics)
  ✅ Airflow DAG pause (freeze billing/analytics pipelines)
  ✅ Mass session invalidation (Keycloak mass logout)
```

### 6.2 Forbidden Actions During Containment (IRC-E — LOCKED)

The following are **ABSOLUTELY FORBIDDEN** during containment:

```
FORBIDDEN DURING CONTAINMENT:
  ❌ Data deletion of any kind — evidence must be preserved
  ❌ Evidence mutation (editing logs, modifying audit records)
  ❌ Silent rollback (any rollback must be logged and announced)
  ❌ Remediation before containment confirmed by Commander
  ❌ Score finalization on sessions in CRISIS_HALTED state
  ❌ Invoice generation on billing system under active financial crisis
  ❌ Key re-issuance before revocation confirmed
  ❌ Direct database modification without incident reference
  ❌ Manual prod deploy without CI/CD pipeline (R40)
```

**Remediation before containment → VIOLATION → Escalate to P0**
**Evidence deletion during incident → CRITICAL BREACH (IRC-F)**

---

## SECTION 7 — EVIDENCE PRESERVATION (IRC-F — LOCKED)

### 7.1 Evidence Protocol

```
On incident declaration (any severity P0–P3):
  1. Forensics Lead designates incident evidence scope
  2. All relevant objects placed on legal hold immediately
  3. Legal hold = immutable flag on:
       - Loki log segments (bounded window)
       - Prometheus metric snapshots (time-bounded)
       - OpenTelemetry trace bundles
       - Wazuh SIEM export
       - Redis keyspace snapshot (BGSAVE)
       - PostgreSQL WAL segments
       - ClickHouse table snapshots
       - MinIO object versions
       - Kafka message offsets (topic replay window)
  4. SHA-256 hash computed for each evidence bundle
  5. Hash stored in immutable audit record
  6. Evidence stored in MinIO: /crisis/{incident_id}/evidence/
     with WORM policy (15+ year retention per Immutable Archive Service)
  7. Chain of custody document created (Forensics Lead signature)
```

### 7.2 Evidence Schema

```json
{
  "evidence_id": "<uuid>",
  "incident_id": "<uuid>",
  "evidence_type": "log_segment | metric_snapshot | trace_bundle | siem_export | redis_snapshot | wal_segment | kafka_offset | minio_version",
  "source_service": "<service name>",
  "time_window_start": "<ISO 8601>",
  "time_window_end": "<ISO 8601>",
  "storage_path": "/crisis/{incident_id}/evidence/{evidence_id}",
  "sha256_hash": "<hash>",
  "legal_hold": true,
  "chain_of_custody_owner": "<forensics_lead_user_id>",
  "created_at": "<ISO 8601>",
  "worm_expiry": "<15+ years from created_at>"
}
```

**Evidence loss during incident → CRITICAL BREACH (IRC-F)**
**Evidence hash mismatch → TAMPERING ALERT → P0 escalation**

---

## SECTION 8 — SEVERITY RECLASSIFICATION PROTOCOL (IRC-U — LOCKED)

```
Rules:
  - Only Incident Commander may reclassify severity
  - Reclassification requires written justification
  - All prior severity states are retained in incident record (immutable history)
  - Severity downgrade requires containment verification by Commander
  - Silent severity change → FORBIDDEN → auto-escalate to P1
  - Severity upgrade requires no approval (can happen immediately)
  - Severity downgrade during active containment → REQUIRES Commander sign-off

Reclassification Record Schema:
  {
    "reclassification_id": "<uuid>",
    "incident_id": "<uuid>",
    "from_severity": "P0",
    "to_severity": "P1",
    "commander_id": "<user_id>",
    "justification": "<text>",
    "containment_verified": true,
    "timestamp": "<ISO 8601>"
  }
```

---

## SECTION 9 — DISASTER RECOVERY PROTOCOL (R18 — LOCKED)

### 9.1 Backup & Recovery Architecture

```
DATABASE BACKUPS:
  PostgreSQL:
    - Full backup: daily (Velero + pgdump)
    - WAL-based continuous archival: to MinIO
    - RPO: ≤1 hour
    - RTO (P0): ≤2 hours
    - Restore drill: quarterly

  Redis:
    - RDB snapshot: every 15 minutes
    - AOF: always-on
    - RPO: ≤15 minutes
    - RTO: ≤30 minutes

  ClickHouse:
    - Daily backup to MinIO
    - RPO: ≤24 hours (analytics — acceptable)
    - RTO: ≤4 hours

  etcd:
    - Snapshot: every 30 minutes (etcd snapshot save)
    - Critical: cluster state loss → P0
    - RPO: ≤30 minutes
    - RTO: ≤1 hour

  MinIO:
    - Bucket replication to secondary MinIO
    - RPO: ≤1 hour
    - RTO: ≤2 hours

KUBERNETES CLUSTER:
  - Velero: cluster backup every 6 hours
  - Helm chart versions: immutable in Harbor registry
  - RPO: ≤6 hours
  - RTO: ≤4 hours (cluster restore from Velero)
```

### 9.2 Recovery Sequence (P0 Platform Outage)

```
Order of service restoration (dependency-ordered):

  1. etcd (Kubernetes state — prerequisite for all)
  2. HashiCorp Vault (secrets — prerequisite for auth)
  3. PostgreSQL primary (user data — prerequisite for auth)
  4. Redis (session state — prerequisite for realtime)
  5. Keycloak (auth — prerequisite for all tenant access)
  6. Kong API Gateway (ingress — prerequisite for all APIs)
  7. Kafka + RabbitMQ (event bus — prerequisite for async services)
  8. Core microservices (Auth, User, Job, Billing)
  9. MinIO (storage — prerequisite for file operations)
  10. OpenSearch (search — prerequisite for discovery)
  11. Notification Service (communications)
  12. Realtime services (GD Orchestrator, Dojo, Interview)
  13. Jitsi / LiveKit (media — last, highest risk)
  14. Analytics (ClickHouse — last, non-critical path)

Each service validated with integration test before next service restored.
```

### 9.3 Multi-Region Failover Strategy

```
Primary region: GCP / AWS Kubernetes cluster (k3s confirmed)
Failover trigger: P0 platform outage > 30 minutes OR primary region loss

Failover protocol:
  1. DNS failover: PowerDNS / Technitium (self-hosted) record update
     (NOT Cloudflare — external SaaS dependency is forbidden per v8 audit)
  2. Velero restore to standby cluster
  3. Vault unseal on standby cluster
  4. PostgreSQL replica promoted to primary
  5. Service restoration in dependency order (Section 9.2)
  6. DNS propagation confirmed before traffic routed

RPO (multi-region): ≤1 hour
RTO (multi-region): ≤4 hours
```

---

## SECTION 10 — COMMUNICATION PROTOCOL (IRC-I — LOCKED)

### 10.1 Communication Principles

```
Rules (from IRC-I — Notification & Disclosure Lock):
  - Users notified when personal data is directly affected
  - Tenants notified when scope affects more than 1 user of that tenant
  - Regulators notified when legally required (GDPR/DPDP: ≤72 hours)
  - Over-notification → FORBIDDEN (creates panic, reduces signal quality)
  - Under-notification → FORBIDDEN (compliance violation)
  - Root cause details: NOT disclosed until investigation complete
  - Status page updated every 30 minutes during active P0/P1
```

### 10.2 Notification Templates (LOCKED)

```
TEMPLATE: TENANT_ADMIN_INCIDENT_NOTIFICATION
  Subject: [Ecoskiller] Service Impact Notice — {incident_date}
  Body: Service disruption detected affecting {affected_feature}.
        Current status: INVESTIGATING. ETA for update: {eta}.
        Your data integrity is our priority. No further action required.
        Incident Reference: {incident_id}

TEMPLATE: USER_DATA_BREACH_NOTIFICATION (REG-001)
  Subject: [Ecoskiller] Important Security Notice
  Body: We detected unauthorized access potentially affecting your account
        during {time_window}. We have secured your account.
        Action required: Reset your password at {link}.
        Incident Reference: {incident_id}
        Regulatory Reference: GDPR Article 34 notification.

TEMPLATE: REGULATOR_NOTIFICATION (72-hour GDPR/DPDP)
  Filed via: compliance@ecoskiller.com → regulatory authority
  Contains: nature of breach, categories of data, approximate number
            of data subjects, likely consequences, measures taken.

TEMPLATE: SESSION_CRISIS_HALT_USER_MESSAGE
  In-app/WebSocket: "Your session has been paused for technical review.
  You will be notified when it can be resumed or rescheduled."
```

### 10.3 War Room Protocol

```
ACTIVATION: P0 and P1 incidents
CHANNEL: Encrypted messaging (self-hosted Mattermost or equivalent)
CHANNEL NAME: #crisis-{incident_id}
MANDATORY PARTICIPANTS: Commander + all assigned role leads
SITREP CADENCE:
  P0: every 15 minutes
  P1: every 30 minutes
  P2: every 1 hour
SITREP FORMAT:
  STATUS: [INVESTIGATING | CONTAINED | REMEDIATING | RECOVERING | CLOSED]
  BLAST RADIUS: [tenants, users, domains affected]
  ACTIONS COMPLETED: [list]
  NEXT ACTIONS: [list with owners]
  ETA TO NEXT UPDATE: [timestamp]
WAR ROOM LOGS: archived to MinIO: /crisis/{incident_id}/war_room/
```

---

## SECTION 11 — POSTMORTEM PROTOCOL (LOCKED)

### 11.1 Postmortem Mandate

```
MANDATORY postmortems:
  P0: within 48 hours of incident closure
  P1: within 72 hours of incident closure
  P2: within 7 days of incident closure
  P3/P4: within 14 days (if recurring pattern)

Postmortem is BLOCKED if:
  - Incident not formally closed by Commander
  - Evidence preservation not completed
  - Recovery not fully verified

Postmortem CANNOT be skipped under any operational pressure.
Incident closure without postmortem → GOVERNANCE VIOLATION → re-open incident
```

### 11.2 Postmortem Document Schema

```json
{
  "postmortem_id": "<uuid>",
  "incident_id": "<uuid>",
  "severity": "P0 | P1 | P2",
  "incident_type": "<type code>",
  "incident_commander_id": "<user_id>",
  "timeline": [
    {
      "timestamp": "<ISO 8601>",
      "event": "<description>",
      "actor": "<user_id or system>",
      "action_type": "detection | classification | containment | evidence | remediation | recovery | communication | closure"
    }
  ],
  "root_cause": "<structured analysis — speculative conclusions FORBIDDEN>",
  "attack_or_failure_vector": "<technical description>",
  "policy_breach_mapping": ["<policy reference>"],
  "control_failure_mapping": ["<control that failed>"],
  "affected_summary": {
    "tenants": <count>,
    "users": <count>,
    "user_types": ["<list>"],
    "domains": ["<list>"],
    "assessments": <count>,
    "certifications": <count>,
    "financial_impact": "<amount or null>"
  },
  "sla_compliance": {
    "detection_sla": "MET | MISSED",
    "containment_sla": "MET | MISSED",
    "recovery_sla": "MET | MISSED"
  },
  "playbook_id": "<uuid>",
  "playbook_version": "<semver>",
  "playbook_update_required": true,
  "action_items": [
    {
      "item_id": "<uuid>",
      "description": "<action>",
      "owner_id": "<user_id>",
      "due_date": "<ISO 8601>",
      "priority": "P0 | P1 | P2"
    }
  ],
  "lessons_learned": "<text>",
  "closed_at": "<ISO 8601>",
  "commander_seal": "<cryptographic signature>"
}
```

---

## SECTION 12 — DOMAIN-SENSITIVE CRISIS RULES (IRC-C — LOCKED)

### 12.1 Arts Domain Crises

```
DOM-001 specific rules:
  - Creative content misuse: immediate content quarantine
  - Peer harassment during GD: session halt (PB-SAF-v1) + domain moderator alert
  - Mentor bias: session VOID + re-assignment + scoring recalculation
  - Evidence: all session behavioral logs preserved, NOT content (no audio)
```

### 12.2 Commerce Domain Crises

```
DOM-002 specific rules:
  - Evaluation unfairness: scores suspended, external review triggered
  - Conflict-of-interest breach: mentor removed from session immediately
  - Financial misrepresentation: escalate to FIN-003 playbook
  - Evidence: scoring input logs + mentor assignment records frozen
```

### 12.3 Science Domain Crises

```
DOM-003 specific rules:
  - Plagiarism detected: submission quarantined, originality report generated
  - Data fabrication: all submissions from affected user/session frozen
  - Methodology tampering in Dojo: match VOID + all dependent scores suspended
  - Evidence: submission timestamps + version history preserved
```

### 12.4 Cross-Domain Crisis Rules

```
DOM-004 specific rules:
  - Cross-domain access without explicit policy grant: DENY + P1 escalation
  - All cross-domain incident processing requires domain_track tag (IRC-C)
  - Cross-domain incident cannot be processed without tagging → DENY
```

---

## SECTION 13 — ECONOMIC ABUSE CRISIS (T15 — LOCKED)

### 13.1 Economic Abuse Detection Triggers

```
From Section T15 — Economic Abuse Controls:

Detection triggers:
  - Refund abuse: >3 refund requests per user per month
  - Multi-account farming: same device fingerprint across >3 accounts
  - Mentor collusion billing: billing pattern correlation across mentor+student
  - Fake tournament loops: same participant winning >5 consecutive tournaments
  - Fake revenue submission (Royalty): revenue report hash mismatch

RESPONSE:
  - Flagged accounts: locked immediately (TQEA lifecycle → SUSPENDED)
  - Economic abuse incident: classified FIN-004 or FIN-003
  - Fraud Detection Engine: triggered for pattern analysis
  - Admin Governance Service: receives full abuse report
  - Appeals workflow activated (T16): formal appeal path provided
```

---

## SECTION 14 — AUDIT & OBSERVABILITY

### 14.1 Immutable Crisis Audit Log Schema

```json
{
  "crisis_audit_id": "<uuid>",
  "agent": "CRISIS_MANAGEMENT_AGENT",
  "incident_id": "<uuid>",
  "event_type": "incident_declared | severity_classified | commander_assigned | containment_action | evidence_frozen | remediation_step | recovery_step | notification_sent | severity_reclassified | postmortem_opened | incident_closed",
  "actor_id": "<user_id or system>",
  "action_detail": "<description>",
  "severity_at_event": "P0 | P1 | P2 | P3 | P4",
  "affected_tenant_ids": ["<uuid>"],
  "sla_status": "WITHIN | BREACHED",
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

**Crisis audit logs: append-only, WORM-compatible (R52.4)**
**No deletion, no mutation — ever**

### 14.2 Prometheus Metrics (Mandatory)

```
cma_incidents_declared_total{severity, incident_type}
cma_incidents_active{severity}
cma_incidents_closed_total{severity, resolution_type}
cma_detection_sla_compliance_ratio{severity}
cma_containment_sla_compliance_ratio{severity}
cma_recovery_sla_compliance_ratio{severity}
cma_postmortem_completion_ratio{severity}
cma_evidence_objects_under_legal_hold
cma_session_crisis_halts_total{session_type}
cma_key_rotation_events_total
cma_mass_session_revocations_total
cma_feature_kill_switch_activations_total
cma_maintenance_mode_duration_seconds
cma_society_crisis_events_total{crisis_type}
cma_economic_abuse_detections_total{abuse_type}
```

### 14.3 Grafana Dashboards (Mandatory)

- Active incident heatmap (severity × type × domain — real-time)
- SLA compliance tracker (detection / containment / recovery — by severity)
- Incident timeline view (per incident drill-down)
- GD/Dojo session crisis halt history
- Key rotation event log
- Evidence legal hold count (active)
- Postmortem completion rate (by severity, by quarter)
- Economic abuse detection rate (by type)
- Society offline crisis tracker (by franchise unit)
- War room SITREP archive browser

---

## SECTION 15 — INTEGRATION CONTRACTS

### 15.1 Admin Console (R40)
CMA requires the Emergency Controls module of the Internal Admin & Ops Console:
- Global Feature Kill-Switch: CMA triggers programmatically via API
- Platform Maintenance Mode Toggle: CMA triggers programmatically
- Alert & Incident Console: CMA writes all incident records here
- Audit Log Explorer: all crisis audit records visible here

### 15.2 Wazuh SIEM
Primary security detection source. CMA consumes all CRITICAL and HIGH alerts.
CMA writes back: incident_id reference to correlated Wazuh events.

### 15.3 Prometheus + Grafana
All CMA metrics exported to Prometheus. Grafana: all mandatory dashboards
(Section 14.3) must exist and be accessible to SOC Lead 24×7.

### 15.4 Keycloak
CMA triggers mass session revocation via Keycloak Admin API.
CMA triggers forced MFA re-enrollment on security incidents.

### 15.5 HashiCorp Vault
CMA triggers key revocation and rotation cascade during SEC-* incidents.
Vault audit log: CMA monitors for any unauthorized key access.

### 15.6 Velero
CMA triggers Velero restore jobs for P0 infrastructure recovery.
Velero backup status: monitored by CMA (backup failure → P2 incident).

### 15.7 TQEA / CRLA / PFGA / TAOIA
CMA sends crisis state signals to all sibling agents:
- TQEA: tenant lifecycle → SUSPENDED (crisis)
- CRLA: emergency rate cap → 10 rpm
- PFGA: SMS/push blocked for affected tenants
- TAOIA: audio session emergency halt

### 15.8 Jitsi / LiveKit (GD Orchestrator)
CMA issues CRISIS_HALT signal to GD Orchestrator.
Orchestrator sends force-mute to all participants via Jitsi API.
Session state machine: transitions to CRISIS_HALTED.

### 15.9 Airflow
CMA pauses affected DAGs during financial and billing crises.
Airflow DAG failure → CMA alert → incident declaration if billing-critical.

### 15.10 Admin Governance Service
All incidents reported to Admin Governance Service.
Mentor misconduct, disputes, score appeals: routed through governance board (T16).
Economic abuse reports: received by admin governance for human review.

---

## SECTION 16 — DEPLOYMENT REQUIREMENTS

### 16.1 Kubernetes Namespace

```yaml
namespace: ops
labels:
  app: cma
  tier: risk-crisis
  criticality: supreme
  availability: 24x7
```

### 16.2 High Availability Requirements

```yaml
replicas: 3          # Always 3 replicas — no single point of failure
podAntiAffinity: required  # Spread across nodes
priorityClass: system-critical  # Never evicted under resource pressure
```

### 16.3 Resource Limits

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "2"
    memory: "2Gi"
```

### 16.4 Health Probe Requirements

Readiness must verify:
- Wazuh connection healthy (CMA can receive SIEM events)
- Prometheus query endpoint reachable
- Kafka producer/consumer connection healthy (crisis events)
- PostgreSQL connection (incident records)
- MinIO write access (evidence storage path: /crisis/)
- Vault connection (key revocation capability)
- Keycloak Admin API reachable (mass logout capability)
- Admin Console API reachable (kill-switch capability)

---

## SECTION 17 — FINAL ENFORCEMENT DECLARATION

```
┌──────────────────────────────────────────────────────────────────┐
│  CRISIS_MANAGEMENT_AGENT — FINAL LAW                             │
│                                                                  │
│  1.  Every incident is detected within 5 minutes                 │
│  2.  Every incident is classified immediately (P0–P4)            │
│  3.  Every incident has exactly one Incident Commander           │
│  4.  Containment always precedes remediation (IRC-E)             │
│  5.  Evidence is frozen before any remediation begins (IRC-F)    │
│  6.  Evidence is never deleted or mutated during an incident     │
│  7.  Live sessions halt immediately on SAF/INT crisis signal     │
│  8.  Severity reclassification requires Commander sign-off       │
│  9.  Silent severity change is FORBIDDEN (IRC-U)                 │
│  10. Key compromise triggers immediate rotation cascade          │
│  11. Mass session revocation is immediate on SEC breach          │
│  12. DR recovery follows dependency order — no exceptions        │
│  13. DNS failover uses self-hosted DNS only (no Cloudflare)      │
│  14. Regulatory notifications comply with 72-hour GDPR/DPDP SLA │
│  15. Over-notification and under-notification are both FORBIDDEN │
│  16. Society crises activate Society Lead within 5 minutes       │
│  17. Economic abuse auto-locks accounts and triggers FIN review  │
│  18. Every incident has a mandatory postmortem                   │
│  19. Postmortem cannot be skipped under any pressure             │
│  20. Every postmortem generates a versioned playbook update      │
│  21. All crisis audit logs are WORM-append-only — no exceptions  │
│  22. No manual prod deploy during crisis — CI/CD only (R40)      │
│                                                                  │
│  Violation of any rule above:                                    │
│  → STOP CURRENT ACTION                                           │
│  → ESCALATE TO INCIDENT COMMANDER                                │
│  → WRITE IMMUTABLE CRISIS AUDIT RECORD                           │
│  → FIRE WAZUH SIEM ALERT                                         │
│  → NOTIFY ADMIN GOVERNANCE SERVICE                               │
│  → RE-CLASSIFY INCIDENT TO NEXT HIGHER SEVERITY                  │
│                                                                  │
│  No exceptions. No overrides. No workarounds.                    │
│  Commander authority is supreme and non-delegable.               │
└──────────────────────────────────────────────────────────────────┘
```

---

## SECTION 18 — VERSION CONTROL

| Version | Date | Change | Authority |
|---|---|---|---|
| v1.0 | 2026-03-04 | Initial sealed specification | Human declaration |

**Next version: v1.1 — Add-only. No mutations to existing clauses.**

---

```
SEAL: CRISIS_MANAGEMENT_AGENT · RISK-CRISIS-CMA-001 · v1.0
CLASSIFICATION: ANTIGRAVITY — SEALED PROMPT ARTIFACT
STATUS: FINAL · LOCKED · GOVERNED · NON-NEGOTIABLE
```
