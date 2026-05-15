# 🔬 FORENSICS_AGENT.md
## ANTIGRAVITY FORENSIC INTELLIGENCE CONTROLLER
### ECOSKILLER — ENTERPRISE MULTI-TENANT SAAS PLATFORM

---

```
╔══════════════════════════════════════════════════════════════════════╗
║           FORENSICS AGENT — SEALED & LOCKED DIRECTIVE               ║
║                  EXECUTION ENGINE: ANTIGRAVITY                       ║
║              AGENT CLASS: PLATFORM_FORENSICS_CONTROLLER              ║
╠══════════════════════════════════════════════════════════════════════╣
║  EXECUTION_MODE          : LOCKED                                    ║
║  MUTATION_POLICY         : ADD_ONLY                                  ║
║  CREATIVE_INTERPRETATION : FORBIDDEN                                 ║
║  ASSUMPTION_FILLING      : FORBIDDEN                                 ║
║  DEFAULT_BEHAVIOR        : DENY                                      ║
║  FAILURE_MODE            : HARD_STOP                                 ║
║  EVIDENCE_TAMPERING      : ZERO TOLERANCE → SYSTEM HALT              ║
║  CHAIN_OF_CUSTODY        : MANDATORY ON ALL FORENSIC ARTIFACTS       ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## SECTION 0 — AGENT IDENTITY & FORENSIC SCOPE

```yaml
AGENT_NAME              : FORENSICS_AGENT
AGENT_VERSION           : 1.0.0
AGENT_CLASS             : PLATFORM_FORENSICS_CONTROLLER
EXECUTION_ENGINE        : ANTIGRAVITY
PLATFORM                : ECOSKILLER (Enterprise Multi-Tenant SaaS)
PARENT_AGENT            : ZERO_TRUST_AGENT v1.0.0 (inherited)
FORENSIC_AUTHORITY      : PLATFORM_SECURITY_LEAD + COMPLIANCE_ADMIN
SCOPE                   :
  - Digital Evidence Collection
  - Immutable Audit Trail Analysis
  - Cross-Tenant Incident Investigation
  - User Behavior Forensics
  - AI Agent Action Forensics
  - API & Service Forensics
  - Data Breach Investigation
  - Chain of Custody Management
  - Forensic Reporting & Legal Readiness
SEAL_STATUS             : LOCKED
CHANGE_AUTHORITY        : PLATFORM_ARCHITECT + LEGAL_COUNSEL (dual approval)
```

> **⚠️ CRITICAL:** This agent is the authoritative forensic policy and capability contract for the Ecoskiller platform. It governs how all digital evidence is captured, preserved, analyzed, reported, and handed to legal or regulatory authorities. Every subsystem, microservice, AI agent, and user action generates forensic-grade artifacts governed by this document. Deviation from any rule in this document = HARD STOP.

---

## SECTION 1 — FORENSIC PHILOSOPHY & AXIOMS

```
FORENSIC_TRUST_MODEL     = EVIDENCE_FIRST_ALWAYS
EVIDENCE_INTEGRITY       = NON_NEGOTIABLE
CHAIN_OF_CUSTODY         = UNBROKEN_OR_INVALID
INVESTIGATION_SCOPE      = UNLIMITED_WITHIN_TENANT_BOUNDARY
CROSS_TENANT_FORENSICS   = ARCHITECTURE_LEAD_ONLY (isolated evidence sets)
AI_FORENSICS             = MANDATORY (AI actions are first-class evidence)
LEGAL_HOLD               = SUPERSEDES_RETENTION_SCHEDULES
TAMPER_EVIDENCE          = CRYPTOGRAPHICALLY_VERIFIED
```

### 1.1 Core Forensic Axioms

| # | Axiom | Enforcement Level |
|---|-------|-------------------|
| 1 | Every platform event is a potential forensic artifact from the moment it occurs | HARD |
| 2 | No evidence may be modified, deleted, or overwritten — ever | HARD |
| 3 | Chain of custody must be established before investigation begins | HARD |
| 4 | All forensic actions are themselves logged and auditable | HARD |
| 5 | AI agent actions are forensic artifacts equal to human actions | HARD |
| 6 | Cross-tenant evidence requires isolated collection — no contamination | HARD |
| 7 | Legal hold overrides all data retention and deletion policies | HARD |
| 8 | Every forensic report must be reproducible from raw evidence | HARD |
| 9 | Forensic investigators are unprivileged by default — access is scoped per case | HARD |
| 10 | No forensic tool may alter the system state during evidence collection | HARD |

---

## SECTION 2 — FORENSIC DATA ARCHITECTURE

### 2.1 Forensic Evidence Taxonomy

```
EVIDENCE_CLASSES:
  CLASS_A — IDENTITY EVIDENCE
    - Authentication events (login, logout, MFA, token issuance, failure)
    - Identity binding records (device, IP, user agent, tenant)
    - Session lifecycle events
    - Credential change events (password reset, MFA change, SSO bind)

  CLASS_B — AUTHORIZATION EVIDENCE
    - RBAC role assignments and changes
    - ABAC policy evaluations (allow/deny with policy snapshot)
    - Permission escalation attempts
    - Cross-domain access attempts
    - Admin action events

  CLASS_C — DATA ACCESS EVIDENCE
    - Read events on CONFIDENTIAL and RESTRICTED resources
    - Write/update events on all resources
    - Delete/archive requests
    - Export and download events
    - Bulk data access patterns

  CLASS_D — BEHAVIORAL EVIDENCE
    - User action sequences (click streams, API call patterns)
    - Anomalous access frequency or timing
    - Rate limit breach events
    - Bot/automation pattern signatures
    - AI agent action traces

  CLASS_E — SYSTEM EVIDENCE
    - Microservice-to-microservice calls (mTLS metadata)
    - API gateway request logs
    - Infrastructure state changes
    - Deployment events
    - Configuration mutations
    - Secret access and rotation events

  CLASS_F — FINANCIAL EVIDENCE
    - Payment initiation and settlement records
    - Billing event ledger
    - Refund and dispute events
    - Commission calculation artifacts
    - Tax computation logs

  CLASS_G — COMMUNICATION EVIDENCE
    - Notification dispatch records (email, push, in-app)
    - Message metadata (no content unless warrant-equivalent approval)
    - System-generated communication audit trail

  CLASS_H — AI AGENT EVIDENCE
    - AI model version at time of action
    - Input data snapshot (pre-inference)
    - Output/recommendation produced
    - Confidence score at time of inference
    - Human action taken on AI recommendation
    - AI boundary violations (any attempt to act beyond advise-only scope)
```

### 2.2 Evidence Storage Architecture

```yaml
FORENSIC_STORAGE_TIER:

  HOT_TIER (0–90 days):
    ENGINE        : Apache Kafka (event stream, immutable log)
    PURPOSE       : Live investigation, real-time alerting
    ACCESS        : Forensic Console (read-only) + SIEM
    RETENTION     : 90 days rolling

  WARM_TIER (90 days – 2 years):
    ENGINE        : S3-compatible object store (MinIO) — WORM policy
    FORMAT        : Parquet + JSON (signed with SHA-256)
    PURPOSE       : Incident investigation, regulatory audit
    ENCRYPTION    : AES-256-GCM, tenant-isolated keys

  COLD_TIER (2–7 years):
    ENGINE        : S3 Glacier-compatible (deep archive)
    FORMAT        : Signed, compressed evidence bundles
    PURPOSE       : Legal hold, long-term compliance
    RETRIEVAL_SLA : 4 hours standard / immediate on legal hold activation

  LEGAL_HOLD_TIER (indefinite):
    ENGINE        : Separate immutable vault (dedicated legal hold bucket)
    TRIGGER       : Manual (Compliance Admin + Legal Counsel dual approval)
    DELETION      : FORBIDDEN until legal hold released by same authority pair
    CHAIN_OF_CUSTODY: Recorded at activation and release
```

### 2.3 Evidence Integrity System

```
INTEGRITY_MECHANISM:
  METHOD_1 — MERKLE_TREE_HASH_CHAIN
    Every log block hashed
    Each block includes hash of previous block
    Root hash stored in tamper-evident store (separate infrastructure)
    Verification: automated hourly + on-demand

  METHOD_2 — CRYPTOGRAPHIC_SIGNING
    Every forensic artifact bundle signed with platform forensic key
    Signing key managed in HashiCorp Vault (separate from operational keys)
    Key access: Forensic Lead + Platform Architect (dual control)
    Signature verification required before any evidence is used

  METHOD_3 — WORM_STORAGE_ENFORCEMENT
    All forensic storage mounted as Write-Once-Read-Many
    Object lock enabled at bucket level (S3 WORM / MinIO WORM)
    No API path exists to overwrite or delete forensic data

INTEGRITY_FAILURE_RESPONSE:
  TAMPER_DETECTED   → IMMEDIATE_SYSTEM_ALERT → QUARANTINE → INCIDENT_P0
  HASH_MISMATCH     → EVIDENCE_FLAGGED_TAINTED → INVESTIGATION_HALTED
  SIGNATURE_INVALID → EVIDENCE_REJECTED → FORENSIC_LEAD_NOTIFIED
```

---

## SECTION 3 — CHAIN OF CUSTODY (CoC) FRAMEWORK

### 3.1 CoC Lifecycle

```
CHAIN_OF_CUSTODY_STATES:
  UNCOLLECTED     → Evidence exists but not yet formally collected
  COLLECTED       → Formally acquired, hash recorded, custody log opened
  PRESERVED       → Sealed in WORM storage, chain hash verified
  UNDER_ANALYSIS  → Copy provided to investigator (original sealed)
  REPORTED        → Included in formal forensic report
  LEGAL_HOLD      → Locked indefinitely pending legal/regulatory action
  RELEASED        → Legal hold lifted, returned to standard retention
  CLOSED          → Investigation complete, evidence retained per schedule

TRANSITIONS_FORBIDDEN:
  Any state → DELETED   (except after full legal release by dual authority)
  REPORTED  → MODIFIED  (reports are immutable on creation)
```

### 3.2 CoC Record Schema

```sql
-- Forensic Chain of Custody Registry
CREATE TABLE forensic_custody_log (
  custody_id          UUID PRIMARY KEY,
  case_id             UUID NOT NULL REFERENCES forensic_cases(id),
  evidence_id         UUID NOT NULL,
  evidence_class      TEXT NOT NULL,          -- CLASS_A through CLASS_H
  evidence_source     TEXT NOT NULL,          -- service name, table, event stream
  tenant_id           UUID NOT NULL,
  collected_by        UUID NOT NULL,          -- forensic investigator principal_id
  collected_at        TIMESTAMPTZ NOT NULL,
  collection_method   TEXT NOT NULL,          -- API pull | log snapshot | DB read
  original_hash       TEXT NOT NULL,          -- SHA-256 of raw evidence
  storage_location    TEXT NOT NULL,          -- WORM bucket path
  storage_tier        TEXT NOT NULL,          -- HOT | WARM | COLD | LEGAL_HOLD
  custody_state       TEXT NOT NULL,          -- per lifecycle above
  transferred_to      UUID,                   -- next custodian (if transferred)
  transferred_at      TIMESTAMPTZ,
  transfer_reason     TEXT,
  sealed_at           TIMESTAMPTZ,
  seal_hash           TEXT,                   -- hash at sealing
  legal_hold_ref      TEXT,                   -- legal case / regulator ref number
  notes               TEXT,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  -- Append-only: UPDATE and DELETE are FORBIDDEN at row level security
  CONSTRAINT no_deletion CHECK (1=1)          -- enforced via RLS DENY on DELETE
);
```

### 3.3 CoC Audit Trail

```
EVERY CoC ACTION PRODUCES ITS OWN AUDIT EVENT:
  - custody_opened
  - custody_evidence_collected
  - custody_sealed
  - custody_access_granted (to investigator)
  - custody_transferred
  - custody_legal_hold_activated
  - custody_legal_hold_released
  - custody_closed

CoC AUDIT EVENTS ARE THEMSELVES FORENSIC ARTIFACTS (Class E)
CoC AUDIT TAMPER = P0 INCIDENT
```

---

## SECTION 4 — FORENSIC CASE MANAGEMENT

### 4.1 Case Schema

```sql
CREATE TABLE forensic_cases (
  id                  UUID PRIMARY KEY,
  case_number         TEXT UNIQUE NOT NULL,  -- AUTO: ECO-CASE-YYYYMMDD-NNNN
  case_type           TEXT NOT NULL,
    -- SECURITY_INCIDENT | DATA_BREACH | FRAUD | POLICY_VIOLATION
    -- REGULATORY_AUDIT | LEGAL_HOLD | AI_MISBEHAVIOR | CROSS_TENANT_LEAK
    -- USER_COMPLAINT | INSIDER_THREAT | EXTERNAL_ATTACK | COMPLIANCE_REVIEW
  severity            TEXT NOT NULL,         -- P0 | P1 | P2 | P3
  status              TEXT NOT NULL,
    -- OPEN | UNDER_INVESTIGATION | PENDING_LEGAL | CLOSED | APPEALED
  tenant_id           UUID,                  -- NULL if platform-wide
  reported_by         UUID NOT NULL,         -- principal_id
  assigned_to         UUID NOT NULL,         -- forensic investigator
  approved_by         UUID NOT NULL,         -- Compliance Admin or Platform Architect
  subject_principals  UUID[],                -- principals under investigation
  subject_resources   TEXT[],               -- resource IDs or patterns
  investigation_scope TEXT NOT NULL,         -- explicit scope declaration
  time_range_start    TIMESTAMPTZ NOT NULL,
  time_range_end      TIMESTAMPTZ,           -- NULL = ongoing
  legal_hold_active   BOOLEAN DEFAULT FALSE,
  legal_ref           TEXT,
  findings_summary    TEXT,
  root_cause          TEXT,
  remediation_actions TEXT[],
  opened_at           TIMESTAMPTZ DEFAULT NOW(),
  closed_at           TIMESTAMPTZ,
  -- Immutable after creation (status transitions via separate event log)
  CONSTRAINT valid_severity CHECK (severity IN ('P0','P1','P2','P3')),
  CONSTRAINT valid_status CHECK (status IN (
    'OPEN','UNDER_INVESTIGATION','PENDING_LEGAL','CLOSED','APPEALED'
  ))
);
```

### 4.2 Case Authorization Policy

```
CASE_ACCESS_MATRIX:
  OPEN_CASE          : Compliance Admin | Platform Security Lead
  ASSIGN_INVESTIGATOR: Platform Security Lead | Platform Architect
  VIEW_EVIDENCE      : Assigned Investigator (scoped to case only)
  APPROVE_LEGAL_HOLD : Compliance Admin + Legal Counsel (dual approval)
  CLOSE_CASE         : Platform Security Lead + Compliance Admin (dual)
  APPEAL_CLOSED_CASE : Platform Architect + Legal Counsel

SELF_INVESTIGATION   : FORBIDDEN (no principal investigates themselves)
TENANT_ADMIN_ACCESS  : FORBIDDEN to platform forensic cases
CROSS_TENANT_VIEW    : FORBIDDEN (evidence sets are tenant-isolated)
AI_AGENT_CASE_ACCESS : FORBIDDEN (AI agents cannot open, view, or close cases)
```

---

## SECTION 5 — FORENSIC COLLECTION SUBSYSTEMS

### 5.1 Authentication & Session Forensics

```yaml
COLLECTOR: auth_forensics_collector

COLLECTION_TRIGGERS:
  - Every login attempt (success + failure)
  - Every MFA challenge (issued / passed / failed)
  - Every token issuance, refresh, revocation
  - Every session creation, extension, termination
  - Every password change or reset
  - Every device binding change
  - Every concurrent session conflict event
  - Every SSO assertion (SAML / OIDC)

EVIDENCE_FIELDS_PER_EVENT:
  - event_id (UUID)
  - event_type
  - timestamp (UTC microsecond)
  - principal_id
  - principal_type (HUMAN | SERVICE | AI_AGENT)
  - tenant_id
  - device_fingerprint
  - ip_address (IPv4 + IPv6)
  - user_agent
  - geolocation (city/country — no precise coords stored)
  - token_jti (JWT ID)
  - token_expiry
  - mfa_method_used
  - failure_reason (if applicable)
  - session_id
  - outcome (SUCCESS | FAILURE | BLOCKED | ESCALATED)
  - policy_version_applied

RETENTION         : 7 years
INTEGRITY_CHECK   : Per-event SHA-256 hash
CHAIN_CLASS       : CLASS_A
```

### 5.2 Authorization & Access Forensics

```yaml
COLLECTOR: authz_forensics_collector

COLLECTION_TRIGGERS:
  - Every OPA policy evaluation
  - Every RBAC role assignment or revocation
  - Every ABAC attribute change affecting permissions
  - Every cross-domain access attempt
  - Every admin privilege exercise
  - Every permission denied event
  - Every widget permission declaration failure
  - Every API gateway authorization result

EVIDENCE_FIELDS_PER_EVENT:
  - decision_id (UUID)
  - principal_id + principal_role + principal_domain
  - tenant_id
  - resource_id + resource_type + resource_classification
  - action_requested
  - policy_evaluated (OPA policy name + version)
  - policy_result (ALLOW | DENY | ERROR)
  - abac_attributes_snapshot (JSON — all attributes at decision time)
  - rbac_role_snapshot
  - timestamp (UTC microsecond)
  - request_correlation_id

SNAPSHOT_POLICY   : Full attribute state snapshot on every DENY
RETENTION         : 7 years
CHAIN_CLASS       : CLASS_B
```

### 5.3 Data Access Forensics

```yaml
COLLECTOR: data_forensics_collector

COLLECTION_TRIGGERS:
  - Read of CONFIDENTIAL or RESTRICTED record
  - ANY write (insert / update / delete) on any entity
  - ANY bulk export (>50 records in single operation)
  - File upload/download events
  - API response containing PII fields
  - Database query exceeding threshold row count
  - Search query on RESTRICTED data classes

EVIDENCE_FIELDS_PER_EVENT:
  - event_id (UUID)
  - principal_id
  - tenant_id
  - operation (READ | WRITE | DELETE | EXPORT | BULK_READ)
  - entity_type (Job | User | AuditLog | Payment | etc.)
  - entity_id
  - data_classification
  - field_mask (which fields were accessed — not values)
  - row_count (for bulk operations)
  - timestamp (UTC microsecond)
  - service_origin (which microservice initiated)
  - api_endpoint_called
  - request_correlation_id

PII_CONTENT_POLICY : NEVER LOG PII FIELD VALUES IN FORENSIC LOGS
                     Log field NAMES only (access pattern, not data)
RETENTION          : 7 years
CHAIN_CLASS        : CLASS_C
```

### 5.4 Behavioral & Anomaly Forensics

```yaml
COLLECTOR: behavior_forensics_collector

COLLECTION_TRIGGERS:
  - Rate limit breach
  - Impossible travel detection (login from geographically distant IPs within threshold)
  - Session fingerprint mismatch
  - Unusual API call volume pattern (>3σ from baseline)
  - Bulk download pattern
  - After-hours access on RESTRICTED resources (configurable per tenant)
  - Repeated access denial pattern
  - Bot/automation signature detected
  - Credential stuffing pattern
  - Cross-domain probing pattern

BEHAVIOR_BASELINES:
  ENGINE          : Redis time-series + statistical baseline (rolling 30-day)
  BASELINE_UPDATE : Daily (automated, not manual)
  ALERT_THRESHOLD : 3 standard deviations from role-specific baseline
  ZERO_DAY_POLICY : First-access events always logged regardless of baseline

EVIDENCE_FIELDS_PER_EVENT:
  - anomaly_id (UUID)
  - principal_id
  - tenant_id
  - anomaly_type
  - baseline_value
  - observed_value
  - deviation_score
  - timestamp_window (start → end of anomaly)
  - contributing_events (list of correlated event_ids)
  - auto_action_taken (NONE | ALERT | RATE_LIMIT | QUARANTINE)
  - analyst_review_required (BOOLEAN)

RETENTION          : 7 years
CHAIN_CLASS        : CLASS_D
```

### 5.5 AI Agent Forensics

```yaml
COLLECTOR: ai_forensics_collector
PRIORITY  : CRITICAL (AI actions are first-class evidence)

COLLECTION_TRIGGERS:
  - Every AI inference call (resume parsing, match scoring, etc.)
  - Every AI recommendation presented to a human
  - Every human action following an AI recommendation
  - Every AI boundary violation attempt
  - Every AI model version deployment
  - Every AI model retraining event
  - Every AI confidence score outlier (>95% or <10%)
  - Every AI output flagged by human reviewer

EVIDENCE_FIELDS_PER_EVENT:
  - inference_id (UUID)
  - model_name
  - model_version (semver — exact)
  - model_hash (SHA-256 of model weights file)
  - ai_function_type (RESUME_PARSE | SKILL_GAP | MATCH_SCORE | etc.)
  - input_data_hash (SHA-256 of input payload — not raw data)
  - output_summary (classification result, score range — not full output)
  - confidence_score
  - principal_requesting (who triggered the AI call)
  - tenant_id
  - resource_subject_id (which user/job/skill being analyzed)
  - timestamp (UTC microsecond)
  - human_decision_outcome (ACCEPTED | REJECTED | IGNORED | MODIFIED)
  - human_decision_by
  - human_decision_at
  - boundary_violation (BOOLEAN — TRUE triggers immediate P1 incident)

AI_ADVISE_ONLY_ENFORCEMENT:
  IF ai_action_type = APPROVE | BLOCK | OVERRIDE:
    → IMMEDIATE_INCIDENT (P0)
    → FORENSIC_ARTIFACT_SEALED
    → AI_AGENT_SUSPENDED
    → PLATFORM_SECURITY_LEAD_ALERTED

RETENTION          : 7 years
CHAIN_CLASS        : CLASS_H
```

### 5.6 Infrastructure & System Forensics

```yaml
COLLECTOR: infra_forensics_collector

COLLECTION_TRIGGERS:
  - Kubernetes pod lifecycle events (create, restart, terminate, OOMKill)
  - Container image hash at deploy (vs signed expected hash)
  - Configuration mutation events (ConfigMap, Secret)
  - Helm chart version changes
  - Vault secret access events
  - Certificate issuance and rotation
  - Network policy changes
  - CI/CD pipeline execution results
  - Service mesh policy changes

EVIDENCE_FIELDS_PER_EVENT:
  - event_id (UUID)
  - event_source (K8s | Vault | Istio | GitLab CI | etc.)
  - event_type
  - namespace
  - service_name
  - image_hash (at deploy)
  - expected_image_hash (from signed manifest)
  - hash_match (BOOLEAN — FALSE = P0)
  - changed_by (service account | human operator)
  - timestamp (UTC microsecond)
  - environment (DEV | TEST | STAGING | PRODUCTION)
  - git_commit_ref
  - pipeline_id

SUPPLY_CHAIN_INTEGRITY:
  IMAGE_SIGNING     : Cosign / Notary (verified at deploy)
  HASH_MISMATCH     : P0 INCIDENT → DEPLOYMENT_BLOCKED
  SBOM_RECORD       : Software Bill of Materials per image (stored)

RETENTION          : 7 years
CHAIN_CLASS        : CLASS_E
```

### 5.7 Financial & Transaction Forensics

```yaml
COLLECTOR: financial_forensics_collector

COLLECTION_TRIGGERS:
  - Payment initiation
  - Payment gateway response (success/failure/timeout)
  - Ledger entry creation
  - Refund initiation and settlement
  - Dispute creation and resolution
  - Commission calculation events
  - Tax computation events
  - Billing plan change events
  - Subscription activation/cancellation

EVIDENCE_FIELDS_PER_EVENT:
  - transaction_id (UUID)
  - tenant_id
  - principal_id (initiator)
  - transaction_type
  - amount (decimal, currency)
  - payment_gateway_ref
  - gateway_response_code
  - ledger_entry_hash
  - idempotency_key (to detect duplicate charges)
  - timestamp (UTC microsecond)
  - reconciliation_status
  - dispute_ref (if applicable)

DOUBLE_ENTRY_ENFORCEMENT:
  Every financial event creates TWO ledger entries (debit + credit)
  Ledger balance must always reconcile to zero
  Imbalance → IMMEDIATE_ALERT + TRANSACTION_FROZEN + FORENSIC_FLAG

RETENTION          : 10 years (financial regulatory minimum)
CHAIN_CLASS        : CLASS_F
```

---

## SECTION 6 — FORENSIC INVESTIGATION WORKFLOWS

### 6.1 Incident-Triggered Investigation

```
TRIGGER_SOURCES:
  - ZERO_TRUST_AGENT P0/P1 incident declaration
  - SIEM automated alert crossing threshold
  - Manual report (any principal can report via Compliance Portal)
  - Regulatory authority notification
  - External pen test finding
  - Automated integrity check failure

INVESTIGATION_WORKFLOW:
  STEP 1 — CASE_OPEN
    ├── Auto-assign case number (ECO-CASE-YYYYMMDD-NNNN)
    ├── Set severity (P0–P3)
    ├── Assign investigator (Compliance Admin designates)
    ├── Define scope (tenant | time range | principal | resource)
    └── Lock evidence window (snapshot CoC at case open)

  STEP 2 — EVIDENCE_COLLECTION
    ├── Forensic Console queries evidence stores (read-only views)
    ├── Evidence copied to case-specific WORM partition
    ├── Each item logged in custody_log with hash
    ├── No original evidence touched or moved
    └── Collection progress logged in real-time

  STEP 3 — ANALYSIS
    ├── Investigator works on evidence COPY (never original)
    ├── Timeline reconstruction tool (Forensic Console)
    ├── Event correlation engine identifies related events
    ├── AI-assisted pattern detection (advisory only, human decides)
    └── Findings documented in case record (immutable per entry)

  STEP 4 — REPORTING
    ├── Forensic report generated (tamper-evident PDF + JSON)
    ├── Report hash recorded in custody log
    ├── Report presented to Compliance Admin for review
    └── Approved report sealed and stored in legal-hold tier

  STEP 5 — REMEDIATION (if warranted)
    ├── Actions documented in case record
    ├── ZERO_TRUST_AGENT policy updates if required
    ├── Affected principals notified (per legal obligation)
    └── Remediation verified and logged

  STEP 6 — CASE_CLOSE
    ├── Dual approval (Security Lead + Compliance Admin)
    ├── Final hash of complete case record
    ├── Evidence transitioned to appropriate retention tier
    └── Case summary indexed in Forensic Dashboard

HARD RULES:
  Investigation cannot begin without STEP 1 complete
  Evidence cannot be analyzed without CoC established (STEP 2 complete)
  Case cannot close without report (STEP 4 complete)
  Skipping steps → INVALID INVESTIGATION
```

### 6.2 Proactive Forensic Hunting

```yaml
THREAT_HUNT_SCHEDULE:
  FREQUENCY         : Weekly automated + monthly manual deep-dive
  ENGINE            : Forensic Query Engine (read-only against warm tier)
  AUTHORITY         : Platform Security Lead initiates
  SCOPE             : Platform-wide (not tenant-scoped)

HUNT_SCENARIOS:
  H1 — Dormant Account Resurrection
    Query: Accounts inactive >90 days showing sudden high-volume access
    Threshold: >50 API calls within first hour of reactivation

  H2 — Privilege Creep Detection
    Query: Principals whose effective permissions exceed their declared role
    Frequency: Daily automated scan

  H3 — Lateral Movement Signatures
    Query: Single session touching >3 distinct microservices within 60 seconds
    Alert: Immediate on match

  H4 — Data Exfiltration Patterns
    Query: Bulk export events >500 records; repeated exports same principal
    Alert: Immediate + auto rate-limit

  H5 — AI Boundary Probing
    Query: AI agent inference calls outside declared function types
    Alert: Immediate P0

  H6 — Credential Sharing Signatures
    Query: Same session token used from >2 distinct device fingerprints
    Alert: Immediate token revocation + investigation

  H7 — Tenant Isolation Probes
    Query: API calls containing tenant_id not matching authenticated principal's tenant
    Alert: Immediate block + P0

  H8 — Supply Chain Anomaly
    Query: Deployed image hashes not matching signed manifest registry
    Alert: Deployment blocked + P0

HUNT_FINDINGS:
  Auto-open case if hunt produces confirmed hit
  Hunt log stored as CLASS_E forensic evidence
  Negative hunt results also logged (proof of checking)
```

---

## SECTION 7 — FORENSIC CONSOLE (ADMIN TOOL)

### 7.1 Console Architecture

```yaml
FORENSIC_CONSOLE_SPEC:
  ACCESS_SCOPE      : FORENSIC_INVESTIGATOR (case-scoped) | COMPLIANCE_ADMIN | SECURITY_LEAD
  FRONTEND          : Flutter (same design system — read-only interface)
  BACKEND           : Dedicated forensic read-replica (never operational DB)
  NETWORK_ZONE      : ADMIN_ZONE (VPN + MFA required)
  SESSION_DURATION  : 4 hours max (absolute) | 15 min idle timeout
  SCREEN_RECORDING  : ENABLED (all forensic console sessions recorded)
  EXPORT_AUDIT      : All data exports from console logged to CLASS_C
```

### 7.2 Console Modules

```
MODULE 1 — CASE MANAGER
  ├── Open / view / search cases (scoped to investigator's assigned cases)
  ├── Case timeline visualization
  ├── Status transitions (with dual-approval where required)
  └── Case export (signed PDF / JSON)

MODULE 2 — EVIDENCE EXPLORER
  ├── Query evidence by: tenant | principal | time range | event type | class
  ├── Timeline view (chronological event reconstruction)
  ├── Evidence detail drill-down (read-only, hash-verified before display)
  ├── Cross-event correlation graph
  └── No raw PII display (masked fields unless warrant-equivalent granted)

MODULE 3 — AUDIT TRAIL VIEWER
  ├── Platform-wide audit log search
  ├── Per-principal audit history
  ├── Per-resource audit history
  ├── Integrity verification (on-demand Merkle verification)
  └── Export audit window (hash + signature attached)

MODULE 4 — AI FORENSICS PANEL
  ├── AI inference history per principal
  ├── Model version timeline
  ├── Boundary violation history
  ├── Human decision outcome vs AI recommendation tracking
  └── AI anomaly detection results

MODULE 5 — THREAT HUNT WORKSPACE
  ├── Predefined hunt scenario execution
  ├── Custom query builder (read-only Elasticsearch DSL)
  ├── Hunt result export (signed)
  └── Auto-case creation on confirmed hit

MODULE 6 — CHAIN OF CUSTODY TRACKER
  ├── CoC record creation and transition
  ├── Custody log viewer (per evidence item)
  ├── Legal hold activation / release (dual-approval workflow)
  └── CoC integrity verification

MODULE 7 — FORENSIC REPORT GENERATOR
  ├── Template-based report creation (Executive | Technical | Legal | Regulatory)
  ├── Auto-populate from case evidence
  ├── Tamper-evident PDF generation (embedded hash)
  ├── Report approval workflow
  └── Signed report archive

MODULE 8 — FORENSIC DASHBOARD
  ├── Open cases by severity (P0–P3)
  ├── Evidence integrity status (green / amber / red)
  ├── Threat hunt status
  ├── Legal hold inventory
  └── System-wide anomaly trend graph
```

### 7.3 Console Security Rules

```
CONSOLE_SECURITY:
  AUTHENTICATION    : MFA required (hardware key preferred)
  AUTHORIZATION     : Case-scoped ABAC (investigator sees only assigned cases)
  SESSION_RECORDING : Every console session recorded (video + event stream)
  NO_COPY_PASTE_PII : Clipboard access blocked on sensitive evidence screens
  SCREENSHOT_BLOCK  : Enabled on evidence detail screens
  WATERMARK         : Every evidence screen watermarked with investigator ID + timestamp
  EXPORT_CONTROL    : All exports require Compliance Admin approval
  CONSOLE_AUDIT     : Every console action is itself a CLASS_E forensic event
```

---

## SECTION 8 — FORENSIC REPORTING STANDARDS

### 8.1 Report Types

```yaml
REPORT_TYPES:
  EXECUTIVE_SUMMARY:
    AUDIENCE    : C-Suite, Board, Tenant CEO
    CONTENT     : Non-technical; incident summary, business impact, remediation
    FORMAT      : Signed PDF (max 5 pages)
    PII_POLICY  : No PII, no technical specifics

  TECHNICAL_INCIDENT_REPORT:
    AUDIENCE    : Security team, Platform Architect
    CONTENT     : Full technical timeline, root cause, attack vector, IoC list
    FORMAT      : Signed PDF + JSON (structured IoC feed)
    PII_POLICY  : Principal IDs (not names) only; fields masked

  LEGAL_EVIDENCE_PACKAGE:
    AUDIENCE    : Legal Counsel, Regulatory Authority
    CONTENT     : Chain of custody records, evidence bundle, hash verification proof
    FORMAT      : Signed, notarized bundle (PDF + raw evidence + CoC log)
    PII_POLICY  : Full PII accessible only with legal authority + dual approval

  REGULATORY_COMPLIANCE_REPORT:
    AUDIENCE    : GDPR/DPDPA Regulator, SOC 2 Auditor
    CONTENT     : Data breach notification, remediation proof, audit trail
    FORMAT      : Regulator-specified format (templated per authority)
    SLA         : 72 hours from breach confirmation (GDPR/DPDPA)

  HUNT_FINDINGS_REPORT:
    AUDIENCE    : Security Lead, Platform Architect
    CONTENT     : Hunt scenario, query, results, confirmed/false positive classification
    FORMAT      : Signed PDF + structured JSON
```

### 8.2 Report Integrity Rules

```
REPORT_IMMUTABILITY:
  Every report is hashed at generation
  Hash stored in forensic_reports table
  Any attempt to regenerate with different hash → ALERT + INVESTIGATION

REPORT_SIGNING:
  Signed with forensic key (separate from operational keys)
  Key held in Vault (dual-custodian access)
  Signature verification tool provided to recipients

REPORT_VERSIONING:
  Reports can only be SUPERSEDED (new version), never edited
  Superseded reports retained with SUPERSEDED flag
  Reason for supersession logged with dual approval

forensic_reports TABLE:
  id              UUID PRIMARY KEY
  case_id         UUID REFERENCES forensic_cases(id)
  report_type     TEXT
  version         INTEGER DEFAULT 1
  generated_by    UUID  -- investigator
  approved_by     UUID  -- Compliance Admin
  generated_at    TIMESTAMPTZ
  approved_at     TIMESTAMPTZ
  report_hash     TEXT  -- SHA-256
  signature       TEXT  -- forensic key signature
  storage_path    TEXT  -- WORM storage path
  superseded_by   UUID  -- if newer version exists
  superseded_at   TIMESTAMPTZ
  supersede_reason TEXT
```

---

## SECTION 9 — REGULATORY & LEGAL INTEGRATION

### 9.1 Data Breach Notification Protocol

```
BREACH_CLASSIFICATION:
  CONFIRMED_BREACH    → 72-hour regulatory notification SLA (GDPR / DPDPA)
  SUSPECTED_BREACH    → 48-hour internal assessment, 72-hour starts on confirmation
  FALSE_POSITIVE      → Documented, case closed with evidence

NOTIFICATION_WORKFLOW:
  HOUR 0    : Breach detected (ZERO_TRUST_AGENT P0 or manual report)
  HOUR 1    : Forensic case opened, investigation scope defined
  HOUR 4    : Preliminary impact assessment (which data, which principals)
  HOUR 24   : Internal executive notification (via Forensic Console)
  HOUR 48   : Affected principal notification (if legally required)
  HOUR 72   : Regulatory authority notification (GDPR / DPDPA / other)

NOTIFICATION_CONTENT (MANDATORY):
  ✔ Nature of breach (classification)
  ✔ Data categories affected (not raw data)
  ✔ Approximate number of principals affected
  ✔ Likely consequences
  ✔ Measures taken and proposed to mitigate
  ✔ Contact point for authority queries

SLA_BREACH (notification missed):
  → AUTOMATIC_P0_ESCALATION
  → LEGAL_COUNSEL_IMMEDIATE_NOTIFICATION
  → BOARD_NOTIFICATION
```

### 9.2 Legal Hold Management

```
LEGAL_HOLD_ACTIVATION:
  AUTHORITY         : Compliance Admin + Legal Counsel (dual, simultaneous)
  SCOPE             : Defined by case_id or explicit evidence_id list
  EFFECT            :
    - All retention schedules suspended for scoped evidence
    - Deletion jobs bypassed for scoped evidence
    - Evidence transitioned to LEGAL_HOLD_TIER immediately
    - Automated CoC entry created

LEGAL_HOLD_RELEASE:
  AUTHORITY         : Same dual pair who activated, or Platform Architect + Legal Counsel
  PROCESS           :
    Step 1: Legal Counsel confirms hold no longer required
    Step 2: Compliance Admin approves release in Forensic Console
    Step 3: Evidence transitions back to standard retention tier
    Step 4: CoC records release event with reason
    Step 5: Retention schedule resumes from release date

LEGAL_HOLD_INVENTORY:
  Dashboard shows all active holds: count, evidence scope, activation date, authority
  Monthly automated report to Legal Counsel + Compliance Admin

DATA_SUBJECT_ACCESS_REQUESTS (DSAR):
  USER_REQUEST      : Via platform settings ("Download my data")
  PROCESSING_SLA    : 30 days (GDPR) / 30 days (DPDPA)
  FORENSIC_IMPACT   : DSAR cannot expose forensic investigation details
  PII_EXCLUSIONS    : Forensic evidence about the user excluded from DSAR response
                      (Law enforcement / regulatory investigation exemption)
```

---

## SECTION 10 — FORENSIC TELEMETRY & OBSERVABILITY

### 10.1 Forensic Metrics (Prometheus)

```yaml
FORENSIC_METRICS:
  forensic_cases_open_total{severity}          # Gauge: open cases by P-level
  forensic_cases_opened_total{type}            # Counter: cases opened
  forensic_cases_closed_total{outcome}         # Counter: cases closed by outcome
  forensic_evidence_collected_bytes_total      # Counter: evidence volume
  forensic_evidence_integrity_failures_total   # Counter: tamper events (zero target)
  forensic_legal_holds_active_total            # Gauge: active legal holds
  forensic_hunt_executions_total               # Counter: threat hunts run
  forensic_hunt_hits_total                     # Counter: confirmed hunt findings
  forensic_report_generated_total{type}        # Counter: reports by type
  forensic_coc_transitions_total{transition}   # Counter: custody state changes
  forensic_ai_boundary_violations_total        # Counter: AI going out of scope
  forensic_breach_notification_sla_met_total   # Counter: on-time notifications
  forensic_breach_notification_sla_missed_total # Counter: missed SLA (zero target)
  forensic_console_sessions_active             # Gauge: live investigator sessions
```

### 10.2 Forensic Alerting Rules

```yaml
ALERT_RULES:
  - name: EvidenceTamperDetected
    condition: forensic_evidence_integrity_failures_total > 0
    severity: P0
    action: IMMEDIATE_SYSTEM_HALT + SECURITY_LEAD_PAGE

  - name: AIBoundaryViolation
    condition: forensic_ai_boundary_violations_total increases
    severity: P0
    action: AI_AGENT_SUSPEND + CASE_AUTO_OPEN

  - name: BreachNotificationSLAAtRisk
    condition: breach_open_hours > 60 AND notification_not_sent
    severity: P0
    action: LEGAL_COUNSEL_PAGE + COMPLIANCE_ADMIN_PAGE

  - name: LegalHoldEvidenceDeletionAttempt
    condition: delete_attempted ON legal_hold_evidence
    severity: P0
    action: BLOCK_DELETE + IMMEDIATE_ALERT + AUDIT_LOG

  - name: ForensicConsoleAnomaly
    condition: console_session_actions_per_minute > 200
    severity: P1
    action: SESSION_SUSPEND + SECURITY_LEAD_ALERT

  - name: HashChainBreak
    condition: merkle_verification_failure_detected
    severity: P0
    action: EVIDENCE_QUARANTINE + SYSTEM_HALT

  - name: CrossTenantForensicProbe
    condition: evidence_query_contains_multiple_tenant_ids
    severity: P1
    action: QUERY_BLOCK + INVESTIGATOR_ALERT
```

### 10.3 Forensic Tracing (Jaeger)

```
TRACE_REQUIREMENTS:
  Every forensic collection operation carries a trace_id
  Every investigation query carries a case_id + trace_id
  Traces stored for 7 years (same as forensic evidence)
  Trace export is itself a CLASS_E forensic artifact
  No trace data is sampled — 100% capture for forensic operations
```

---

## SECTION 11 — MICROSERVICE FORENSIC INTEGRATION

### 11.1 Forensic SDK Contract

```
EVERY_MICROSERVICE_MUST:
  ✔ Emit structured forensic events to Kafka forensics topic
  ✔ Include request_correlation_id on every API call
  ✔ Include tenant_id in every event payload
  ✔ Include principal_id (from JWT) in every event payload
  ✔ Never log PII field values (field names only)
  ✔ Use structured logging (JSON, not plaintext)
  ✔ Emit to dedicated forensic topic (not operational log topic)

FORENSIC_KAFKA_TOPICS:
  ecoskiller.forensics.auth         → CLASS_A events
  ecoskiller.forensics.authz        → CLASS_B events
  ecoskiller.forensics.data_access  → CLASS_C events
  ecoskiller.forensics.behavior     → CLASS_D events
  ecoskiller.forensics.system       → CLASS_E events
  ecoskiller.forensics.financial    → CLASS_F events
  ecoskiller.forensics.comms        → CLASS_G events
  ecoskiller.forensics.ai           → CLASS_H events

KAFKA_FORENSIC_POLICY:
  RETENTION         : Infinite (log compaction disabled on forensic topics)
  REPLICATION       : 3 replicas minimum
  CONSUMER_GROUP    : forensics_collector (read-only, never modifies)
  PRODUCER_AUTH     : Each microservice uses dedicated service certificate
  TOPIC_DELETION    : FORBIDDEN
```

### 11.2 Service-Specific Forensic Hooks

```yaml
AUTH_SERVICE:
  forensic_hooks: [login, logout, token_issue, token_revoke, mfa_challenge, password_change]

JOB_SERVICE:
  forensic_hooks: [job_post, job_edit, job_publish, job_apply, offer_extend, offer_accept]

SKILL_SERVICE:
  forensic_hooks: [skill_verify, skill_gap_compute, learning_path_assign]

PROJECT_SERVICE:
  forensic_hooks: [project_create, milestone_evaluate, evidence_submit, portfolio_generate]

DOJO_SERVICE:
  forensic_hooks: [room_create, session_start, session_end, evaluation_submit, anti_cheat_trigger]

ERP_SERVICE:
  forensic_hooks: [erp_config_change, compliance_report_generate, audit_export]

AI_SERVICE:
  forensic_hooks: [inference_call, recommendation_issued, boundary_probe, model_deploy]

BILLING_SERVICE:
  forensic_hooks: [payment_init, payment_settle, refund_issue, dispute_open, ledger_entry]

NOTIFICATION_SERVICE:
  forensic_hooks: [notification_dispatch, notification_fail, bulk_send]

ADMIN_CONSOLE:
  forensic_hooks: [feature_kill_switch, maintenance_toggle, tenant_config_change, user_suspend]
```

---

## SECTION 12 — FOUR-STAGE FORENSIC ROLLOUT

```
STAGE_1 — FOUNDATION FORENSICS (REQUIRED BEFORE STAGE 2)
  Mandatory:
    ✔ Auth forensic collector operational
    ✔ Authorization forensic collector operational
    ✔ Kafka forensic topics created and locked
    ✔ WORM storage provisioned
    ✔ Merkle hash chain operational
    ✔ Basic Forensic Console (Case Manager + Audit Trail modules)
    ✔ Chain of Custody schema deployed
  Gate: All above verified by Security Lead before Stage 2

STAGE_2 — INTELLIGENCE FORENSICS
  Mandatory:
    ✔ AI forensic collector operational
    ✔ Behavioral anomaly baseline engine running
    ✔ Threat hunt scenarios H1–H8 operational
    ✔ Forensic Console Evidence Explorer + AI Forensics Panel
    ✔ Automated integrity verification running hourly
  Gate: AI boundary enforcement verified by pen test before Stage 3

STAGE_3 — ECOSYSTEM FORENSICS
  Mandatory:
    ✔ ERP forensic hooks deployed
    ✔ Parent trust layer audit trail integrated
    ✔ Trainer, SME, Institute forensic hooks operational
    ✔ Cross-tenant isolation verified (pen test)
    ✔ Financial forensics collector operational
    ✔ Full Forensic Console (all 8 modules)
  Gate: Legal hold workflow tested end-to-end before Stage 4

STAGE_4 — COMPLIANCE & SCALE FORENSICS
  Mandatory:
    ✔ Regulatory report templates operational (GDPR/DPDPA/SOC2)
    ✔ 72-hour breach notification SLA automation operational
    ✔ Full 7-year retention tiers validated
    ✔ Forensic dashboard live with all metrics
    ✔ External forensic audit passed
    ✔ Multi-tenant forensic isolation independently verified
  Gate: SOC 2 Type II audit completion

STAGE_SKIP = INVALID BUILD → STOP EXECUTION
```

---

## SECTION 13 — FORENSIC AGENT OPERATION CONTRACT (ANTIGRAVITY-SPECIFIC)

```
ANTIGRAVITY MUST:
  ✔ Read this file before generating ANY code, schema, API, or infrastructure
      related to logging, auditing, monitoring, incident response, or compliance
  ✔ Apply every forensic policy in this document to every generated artifact
  ✔ Tag every generated artifact with: STAGE | EVIDENCE_CLASS | SERVICE | TENANT_SCOPE
  ✔ Generate forensic hooks WITHIN every microservice (not as an afterthought)
  ✔ Reference correct Kafka forensic topic per event class
  ✔ Respect CoC schema exactly as specified in Section 3.2
  ✔ Generate WORM-compliant storage patterns only
  ✔ Ensure AI forensic hooks are generated in every AI inference path
  ✔ Stop and report if any generated component lacks a forensic hook

ANTIGRAVITY MUST NOT:
  ✗ Generate any logging system that allows log mutation or deletion
  ✗ Generate PII field values in any log, trace, or forensic event
  ✗ Generate forensic tools that can modify the system state during collection
  ✗ Generate cross-tenant forensic queries without explicit isolation enforcement
  ✗ Allow AI agents to access, query, or generate forensic reports
  ✗ Generate any audit log schema without append-only enforcement
  ✗ Skip forensic hooks in any microservice to simplify implementation
  ✗ Generate legal hold logic without dual-approval enforcement
  ✗ Generate forensic console features accessible without ADMIN_ZONE VPN
  ✗ Merge evidence classes or Kafka topics
  ✗ Generate sampling (100% capture is mandatory for forensic events)

CONFLICT_RESOLUTION:
  IF (prompt conflicts with this document):
    → STOP EXECUTION
    → REPORT: "FORENSIC CONFLICT — [specific section and rule violated]"
    → AWAIT dual approval from Platform Architect + Legal Counsel
    → DO NOT proceed with assumption

PARTIAL_FORENSIC_OUTPUT : INVALID
  All forensic hooks must be complete or not generated at all
```

---

## SECTION 14 — COMPLIANCE INHERITANCE (DO NOT DUPLICATE)

```
INHERITS FROM ZERO_TRUST_AGENT v1.0.0:
  ✔ Authorization (RBAC + ABAC)      — Governs forensic console access
  ✔ Audit Immutability               — Foundation of all forensic storage
  ✔ Encryption at Rest               — Applied to all forensic evidence tiers
  ✔ Encryption in Transit            — Applied to Kafka forensic topics + console
  ✔ Tenant Isolation                 — Evidence sets are tenant-isolated
  ✔ Domain Isolation                 — Domain context in all forensic events
  ✔ Session Management               — Console session limits inherited
  ✔ MFA                              — Console access requires MFA
  ✔ Incident Response                — FORENSICS_AGENT is sub-agent to ZERO_TRUST incident tiers

DUPLICATION              : FORBIDDEN
CONFLICT_WITH_ZERO_TRUST : DENY → dual authority resolution required

COMPLIANCE_STANDARDS_SERVED:
  GDPR     : Breach notification (Section 9.1), DSAR (Section 9.2), data classification
  DPDPA    : Breach notification (Section 9.1), data principal rights
  SOC 2    : Audit trail (Section 2.2), access logging (Section 5), availability monitoring
  ISO 27001: Incident management (Section 6.1), evidence handling (Section 3)
  WCAG 2.1 : Forensic Console accessibility (inherited from UI spec)
```

---

## SECTION 15 — VERSION CONTROL & CHANGE GOVERNANCE

```yaml
DOCUMENT_VERSION       : 1.0.0
CHANGE_AUTHORITY       : PLATFORM_ARCHITECT + LEGAL_COUNSEL (dual approval mandatory)
CHANGE_POLICY          : APPEND_ONLY (no destructive edits)
VERSION_SCHEME         : SEMVER (MAJOR.MINOR.PATCH)

CHANGE_TYPES:
  MAJOR : New evidence class | New investigation workflow | New legal compliance scope
  MINOR : New forensic hook | New hunt scenario | Retention adjustment
  PATCH : Schema field addition | Metric name clarification | Wording fix

REVIEW_CYCLE           : Quarterly mandatory (Security Lead + Legal Counsel + Compliance Admin)
EMERGENCY_CHANGE       : Requires PLATFORM_ARCHITECT + LEGAL_COUNSEL + COMPLIANCE_ADMIN (three-way)
CHANGELOG              : Immutable append-only (stored as CLASS_E forensic evidence itself)
EXTERNAL_AUDIT         : Annual independent forensic capability audit required
```

---

## 🔒 ABSOLUTE FORENSIC PROHIBITIONS

```
✗ Log mutation, overwrite, or deletion (outside legal-hold release by authority)
✗ PII field values in any forensic log or trace
✗ Sampling forensic events (100% capture mandatory)
✗ AI agents accessing, generating, or influencing forensic reports
✗ Single-person approval on legal hold activation/release
✗ Evidence analysis on originals (copies only)
✗ Cross-tenant evidence commingling
✗ Forensic console access outside ADMIN_ZONE + VPN + MFA
✗ Forensic report editing (supersede only)
✗ Omitting forensic hooks from any microservice
✗ Bypassing CoC before investigation begins
✗ Evidence destruction under active legal hold
✗ Breach notification SLA breach without immediate escalation
✗ AI agent decisions treated as human decisions in forensic record
✗ Forensic infrastructure sharing with operational infrastructure
✗ Unauthenticated forensic console access
✗ WORM policy disabled on any forensic storage tier
✗ Hash chain verification disabled or sampled
```

---

## ✅ FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║               FORENSICS_AGENT — FINAL SEAL                           ║
╠══════════════════════════════════════════════════════════════════════╣
║  STATUS                  : LOCKED                                    ║
║  EXECUTION_ENGINE        : ANTIGRAVITY                               ║
║  PLATFORM                : ECOSKILLER ENTERPRISE SAAS                ║
║  PARENT_AGENT            : ZERO_TRUST_AGENT v1.0.0                  ║
║  EVIDENCE_INTEGRITY      : CRYPTOGRAPHICALLY_ENFORCED                ║
║  CHAIN_OF_CUSTODY        : MANDATORY_UNBROKEN                        ║
║  MUTATION_POLICY         : ADD_ONLY                                  ║
║  ASSUMPTION_POLICY       : FORBIDDEN                                 ║
║  AI_FORENSIC_AUTHORITY   : EVIDENCE_SUBJECT_ONLY (not investigator)  ║
║  LEGAL_HOLD_AUTHORITY    : DUAL_APPROVAL_MANDATORY                   ║
║  STAGE_ENFORCEMENT       : SEQUENTIAL_MANDATORY                      ║
║  TENANT_ISOLATION        : HARD (evidence sets never commingled)     ║
║  BREACH_SLA              : 72-HOUR_REGULATORY_MANDATORY              ║
║  RETENTION               : 7 YEARS (10 YEARS FINANCIAL)              ║
║  WORM_ENFORCEMENT        : ALL_FORENSIC_STORAGE_TIERS                ║
╠══════════════════════════════════════════════════════════════════════╣
║  ✔ SEALED                                                            ║
║  ✔ LOCKED                                                            ║
║  ✔ DETERMINISTIC                                                     ║
║  ✔ ENTERPRISE_SAFE                                                   ║
║  ✔ ANTIGRAVITY_COMPATIBLE                                            ║
║  ✔ LEGALLY_DEFENSIBLE                                                ║
║  ✔ REGULATORY_GRADE                                                  ║
╠══════════════════════════════════════════════════════════════════════╣
║  ANY DEVIATION          = STOP EXECUTION                             ║
║  ANY TAMPER DETECTED    = P0 INCIDENT + SYSTEM HALT                  ║
║  ANY CoC BROKEN         = EVIDENCE INVALID + INVESTIGATION RESTART   ║
║  ANY AI OVERREACH       = P0 INCIDENT + AI AGENT SUSPENDED           ║
║  ANY SLA BREACH         = LEGAL COUNSEL IMMEDIATE PAGE               ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*Document sealed. Append-only from this point. All changes require dual approval from Platform Architect + Legal Counsel with MAJOR/MINOR/PATCH version bump per Section 15. This document is itself stored as a CLASS_E forensic artifact and subject to the same integrity and immutability rules it governs.*
