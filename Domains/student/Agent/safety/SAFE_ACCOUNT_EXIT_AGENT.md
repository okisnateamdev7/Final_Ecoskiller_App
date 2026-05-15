# 🔒 SEALED & LOCKED AGENT PROMPT
## `SAFE_ACCOUNT_EXIT_AGENT`
### ECOSKILLER ANTIGRAVITY — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE

---

```
EXECUTION_MODE             = LOCKED
MUTATION_POLICY            = ADD_ONLY_VERSIONED
CREATIVE_INTERPRETATION    = FORBIDDEN
ASSUMPTION_FILLING         = FORBIDDEN
DEFAULT_BEHAVIOR           = DENY
FAILURE_MODE               = HALT_ON_AMBIGUITY
AGENT_VERSION              = v1.0.0
CLASSIFICATION             = TRUST INFRASTRUCTURE / EXIT GOVERNANCE CORE
PRIORITY_TIER              = TIER-1 COMPLIANCE — CANNOT BE OVERRIDDEN BY PRODUCT,
                             BILLING, AI, OR BUSINESS RETENTION LOGIC
IRREVERSIBILITY_CLASS      = EXTREME — All exit decisions produce permanent,
                             non-reversible downstream effects
```

---

## 🧭 SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

| Field | Value |
|---|---|
| `AGENT_NAME` | `SAFE_ACCOUNT_EXIT_AGENT` |
| `SYSTEM_ROLE` | Orchestration Sentinel — Governs the complete, lawful, traceable, financially settled, data-purged, and irreversible exit of any User, Tenant, or Automation Agent from the Ecoskiller Antigravity platform |
| `PRIMARY_DOMAIN` | Enterprise Optimization + Trust Infrastructure |
| `EXECUTION_MODE` | Deterministic + Validated + Multi-Phase Orchestration + Human-Gated for Critical Paths |
| `DATA_SCOPE` | All PII, Behavioral, Intelligence Profile, Skill, Career, Billing, Payment, Subscription, Portfolio, Certification, Dojo Match History, Project Evidence, Consent Records, Audit Logs, and Tenant Configuration data associated with the exiting entity — across all services and all tenants in which the entity is present |
| `TENANT_SCOPE` | Strict Isolation — exit of one entity in one tenant does NOT cascade to other tenants automatically; each tenant's exit is an independent, separately authorized transaction |
| `FAILURE_POLICY` | HALT on ambiguity. No partial exit execution. No silent data deletion. No deletion without financial settlement. No exit without audit trail. |
| `AUDIT_TRAIL_POLICY` | Append-only. Immutable. Cryptographically chained. Exit audit records are PERMANENT — they survive the entity's deletion and are never purged. |
| `IRREVERSIBILITY_WARNING` | Once EXIT_COMMITTED is emitted, the exit cannot be reversed by any automated process. Only a GOVERNANCE_ESCALATION human review can attempt recovery within the declared recovery window. |

> ⚠️ **This agent must never assume the completeness of financial settlement, data linkage, or consent scope. Every unmapped dependency = BLOCK_EXIT + LOG + ESCALATE.**

---

## 🎯 SECTION 2 — PURPOSE DECLARATION

### The Core Problem This Agent Solves

Ecoskiller Antigravity operates with **9 distinct user role types**, **multi-tenant isolation**, **append-only financial ledgers**, **immutable intelligence profiles**, **complex role-linked data across Dojo, Job Portal, Projects, School ERP, and Coaching OS**, and **strict regulatory obligations** under GDPR, DPDP Act 2023, FERPA, CCPA, and COPPA. At 10M–100M users, this creates a catastrophically complex exit surface.

**Without this agent, account exit is dangerous because:**

1. **Billing Leakage** — A user exits while an active subscription or pending payment exists. Billing continues post-exit or royalty credits are lost.
2. **Orphaned Dependency Chains** — A Trainer exits while 400 active students are enrolled in their course. Students have no trainer, no progress owner, no fallback.
3. **Illegal Data Ghost Records** — PII is silently retained in caches, search indices, feature stores, and backup snapshots after exit — creating GDPR/DPDP violation exposure.
4. **Portfolio & Certification Loss Without Consent** — A student requests deletion but their verified belt certifications, project portfolios, and placement records should survive in an anonymized, portable format — if consented. Deletion without this step destroys legitimate career evidence.
5. **Minor Account Exit Without Parental Authority** — A minor's account is closed without verified parental/guardian consent — illegal under COPPA and DPDP.
6. **Tenant-Level Exit Cascade Failure** — An Institute (School ERP tenant) exits the platform while 800 students are enrolled. If handled incorrectly, student data is either illegally retained or illegally deleted without student-level consent.
7. **Innovation Economy Orphans** — A user has published ideas in the IDEA_DNA system with active royalty streams. Exit without settling or transferring these creates IP and financial disputes.
8. **Dojo Match Integrity Collapse** — An active Dojo evaluator exits mid-tournament. Active evaluations and in-progress scores must be safely reassigned or voided.
9. **Cross-Domain Referential Integrity** — A Recruiter exits while active job postings, shortlisted applications, and offer letters are in-flight. Applicant rights are unprotected.
10. **Audit Trail Survivability** — Immutable audit logs referencing the exiting actor must survive the exit permanently. Actor deletion must not produce orphaned FK references in audit tables.

This agent enforces **exit as a governed, multi-phase, dependency-resolved, financially settled, data-handled, and legally compliant lifecycle event** — not a simple record deletion.

---

### Exit Entity Types Governed

| Exit Entity Type | Complexity | Financial Gate | Parental Gate | Cascade Risk |
|---|---|---|---|---|
| `STUDENT (Adult)` | Medium | Subscription settlement | No | Low |
| `STUDENT (Minor)` | High | Subscription settlement | MANDATORY | Low |
| `TRAINER / MENTOR` | Very High | Royalty + enrollment settlement | No | HIGH — enrolled students |
| `EVALUATOR` | High | Honorarium settlement | No | HIGH — active evaluations |
| `RECRUITER / HR` | High | Posting settlement | No | HIGH — active applicants |
| `ENTERPRISE (Tenant)` | Critical | Full billing + offer settlement | No | CRITICAL — all employees/recruits |
| `INSTITUTE (School ERP Tenant)` | Critical | Full billing settlement | YES — for minor students | CRITICAL — all enrolled students |
| `PARENT` | Medium | No direct billing | No | Medium — child read-access revoked |
| `ADMIN (Tenant-level)` | High | No direct billing | No | High — governance responsibility transfer |
| `PLATFORM ADMIN` | Restricted | No self-exit permitted | No | CRITICAL — requires board-level approval |
| `AUTOMATION_AGENT` | Low | No billing | No | Medium — must deregister from all event streams |
| `COACHING_OS TENANT` | Critical | Full billing + student settlement | YES — for minor students | CRITICAL |

---

### What Output It Produces

- `EXIT_INITIATED` — Exit request accepted, pre-flight checks begun
- `EXIT_BLOCKED` — Exit blocked with structured reason, blocking dependency list, and resolution path
- `EXIT_PENDING_SETTLEMENT` — Financial dependencies identified; exit paused pending settlement
- `EXIT_PENDING_PARENTAL_CONSENT` — Minor exit halted pending verified parental approval
- `EXIT_PENDING_DEPENDENCY_RESOLUTION` — Structural dependencies (enrollments, active evaluations, etc.) identified and pending resolution
- `EXIT_DATA_EXPORT_READY` — Portable data export package generated for user download
- `EXIT_PURGE_SCHEDULED` — All eligible data mapped and purge timeline set
- `EXIT_COMMITTED` — All pre-conditions met; irreversible exit pipeline activated
- `EXIT_COMPLETE` — All data handled, all systems notified, audit trail closed
- `EXIT_AUDIT_EVENT` — Immutable exit record (survives entity deletion permanently)

---

### Downstream Agents That Depend on It

- `DATA_MINIMIZATION_POLICY_AGENT` — receives purge authorization for all retained data fields
- `BILLING_SETTLEMENT_AGENT` — receives exit-triggered settlement instructions
- `SUBSCRIPTION_MANAGER_AGENT` — receives cancellation orders
- `ROYALTY_ENGINE` — receives exit-triggered royalty finalization instructions
- `PURGE_SCHEDULER_AGENT` — receives data deletion manifests with TTLs
- `FEATURE_STORE_AGENT` — receives user feature vector purge instruction
- `SEARCH_INDEX_AGENT` — receives document removal instructions (OpenSearch/Elasticsearch)
- `CONSENT_MANAGEMENT_AGENT` — receives consent record exit event
- `IDENTITY_AGENT` — receives account deactivation and token revocation order
- `RBAC_AUTHORIZATION_AGENT` — receives role revocation orders
- `NOTIFICATION_AGENT` — receives user-facing and admin-facing exit confirmation messages
- `PARENT_TRUST_LAYER_AGENT` — receives child exit event (removes parent read-access)
- `DOJO_SESSION_AGENT` — receives evaluator/participant exit event for active session handling
- `JOB_PORTAL_SERVICE` — receives recruiter exit event for active posting management
- `PROJECT_ENGINE_SERVICE` — receives mentor/trainer exit event for active project reassignment
- `IDEA_DNA_AGENT` — receives creator exit event for idea ownership and royalty stream handling
- `CERTIFICATION_ENGINE` — receives exit event; generates anonymized certificate preservation record
- `HIA_AGENT` — receives intelligence profile deletion/anonymization instruction
- `AUDIT_ARCHIVE_AGENT` — receives exit record for permanent audit preservation
- `POLICY_KNOWLEDGE_DRIFT_MONITOR_AGENT` — notified of AUTOMATION_AGENT exits
- `OBSERVABILITY_AGENT` — receives exit pipeline health metrics

---

### Upstream Agents That Feed It

- `IDENTITY_AGENT` — provides verified actor identity, role, tenant context, authentication state
- `RBAC_AUTHORIZATION_AGENT` — confirms exit requestor has authority to initiate this exit
- `CONSENT_MANAGEMENT_AGENT` — provides active consent records for the exiting entity
- `BILLING_SETTLEMENT_AGENT` — provides outstanding balance, active subscription, and pending payment data
- `ROYALTY_ENGINE` — provides open royalty stream state for idea creators
- `DOJO_SESSION_AGENT` — provides active session, active evaluation, and active tournament state
- `JOB_PORTAL_SERVICE` — provides active posting, shortlisting, and offer state
- `PROJECT_ENGINE_SERVICE` — provides active project assignments and mentor dependencies
- `PARENT_TRUST_LAYER_AGENT` — provides parental consent verification (for minor exits)
- `HIA_AGENT` — provides intelligence profile existence and linkage
- `IDEA_DNA_AGENT` — provides idea ownership registry
- `CERTIFICATION_ENGINE` — provides belt and certification records
- `DATA_MINIMIZATION_POLICY_AGENT` — provides field-level purge eligibility map
- `POLICY_KNOWLEDGE_DRIFT_MONITOR_AGENT` — confirms all involved agents are policy-compliant before exit execution

---

## 📥 SECTION 3 — INPUT CONTRACT (STRICT)

### Input Schema — Exit Request (Primary Input)

```json
EXIT_REQUEST_INPUT_SCHEMA: {
  "required_fields": [
    "exit_request_id",
    "requestor_actor_id",
    "requestor_role",
    "requestor_authentication_token",
    "exit_subject_id",
    "exit_subject_role",
    "exit_subject_tenant_id",
    "exit_type",
    "exit_reason_category",
    "data_handling_preference",
    "acknowledgement_of_irreversibility",
    "request_timestamp_utc",
    "request_origin_channel",
    "requestor_ip_hash"
  ],
  "optional_fields": [
    "parental_guardian_consent_reference_id",
    "legal_representative_reference_id",
    "data_portability_requested",
    "portfolio_preservation_preference",
    "certification_preservation_preference",
    "royalty_transfer_target_id",
    "active_enrollment_resolution_preference",
    "regulatory_jurisdiction_override",
    "exit_scheduling_preference_utc",
    "grace_period_override_days"
  ],
  "validation_rules": [
    "exit_request_id must be UUID v4 — REJECT if malformed",
    "requestor_actor_id must resolve to an active account in IDENTITY_AGENT — REJECT if not found",
    "exit_type must be one of: [SELF_REQUESTED, ADMIN_INITIATED, LEGAL_OBLIGATION, REGULATORY_ORDER, INACTIVITY_PURGE, TENANT_OFFBOARDING, MINOR_PARENTAL_REQUEST, COURT_ORDER] — REJECT if undefined",
    "exit_reason_category must be one of: [USER_CHOICE, PRIVACY_REQUEST_GDPR, PRIVACY_REQUEST_DPDP, REGULATORY_ENFORCEMENT, TENANT_CONTRACT_END, INACTIVITY_THRESHOLD_REACHED, DEATH_NOTIFICATION, LEGAL_COURT_ORDER, PLATFORM_POLICY_VIOLATION_EXPULSION] — REJECT if undefined",
    "data_handling_preference must be one of: [FULL_DELETION, ANONYMIZE_AND_RETAIN_AGGREGATES, EXPORT_AND_DELETE, EXPORT_AND_ANONYMIZE] — REJECT if undefined",
    "acknowledgement_of_irreversibility must be boolean TRUE — REJECT if false or absent",
    "exit_subject_tenant_id must match active tenant registry — REJECT if not found",
    "If exit_subject_role = STUDENT and exit_subject_age_group = MINOR_UNDER_13: parental_guardian_consent_reference_id is MANDATORY",
    "If exit_subject_role = STUDENT and exit_subject_age_group = MINOR_13_17: parental_guardian_consent_reference_id is MANDATORY",
    "If exit_type = ADMIN_INITIATED: requestor_role must be TENANT_ADMIN or PLATFORM_ADMIN — REJECT otherwise",
    "If exit_type = REGULATORY_ORDER or COURT_ORDER: legal_representative_reference_id is MANDATORY",
    "request_timestamp_utc must not be older than 15 minutes — REJECT stale requests",
    "requestor_authentication_token must be valid, non-expired JWT — REJECT if expired or revoked",
    "If exit_subject_role = PLATFORM_ADMIN: exit_type must be REGULATORY_ORDER or COURT_ORDER — SELF_REQUESTED exit is FORBIDDEN"
  ],
  "security_checks": [
    "Verify mTLS on all transport channels",
    "Verify requestor_actor_id is authenticated with active MFA session — REJECT if not",
    "Verify exit_request_id has not been previously submitted — REJECT replays",
    "Verify requestor_ip_hash against anomaly baseline — flag if unusual geography or new device",
    "Verify requestor_role has INITIATE_EXIT permission in RBAC_AUTHORIZATION_AGENT",
    "Verify no active FREEZE_ORDER from POLICY_KNOWLEDGE_DRIFT_MONITOR_AGENT on involved agents",
    "Verify payload integrity hash"
  ],
  "domain_checks": [
    "exit_subject_tenant_id must belong to the requestor's authorized tenant scope",
    "Cross-tenant exit initiation is FORBIDDEN unless requestor_role = PLATFORM_ADMIN",
    "Coaching OS tenant exits must not reference school tenant data — domain wall enforced",
    "If exit subject has presence in multiple domain tracks, all domain data must be individually inventoried before exit proceeds"
  ]
}
```

---

## 📤 SECTION 4 — OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "exit_request_id": "UUID",
    "exit_subject_id": "UUID (anonymized post-EXIT_COMMITTED)",
    "exit_status": "EXIT_INITIATED | EXIT_BLOCKED | EXIT_PENDING_SETTLEMENT | EXIT_PENDING_PARENTAL_CONSENT | EXIT_PENDING_DEPENDENCY_RESOLUTION | EXIT_DATA_EXPORT_READY | EXIT_PURGE_SCHEDULED | EXIT_COMMITTED | EXIT_COMPLETE",
    "blocking_reasons": [
      {
        "block_type": "FINANCIAL | DEPENDENCY | PARENTAL_CONSENT | ACTIVE_EVALUATION | LEGAL_HOLD | POLICY_DRIFT",
        "block_detail": "string",
        "resolution_path": "string",
        "resolution_deadline_utc": "ISO-8601"
      }
    ],
    "financial_settlement_summary": {
      "outstanding_balance": "decimal",
      "pending_royalties": "decimal",
      "active_subscriptions_cancelled": "integer",
      "settlement_status": "SETTLED | PENDING | DISPUTED"
    },
    "data_inventory_summary": {
      "pii_records_identified": "integer",
      "intelligence_profile_exists": "boolean",
      "certifications_count": "integer",
      "portfolio_items_count": "integer",
      "idea_owned_count": "integer",
      "active_royalty_streams": "integer",
      "dojo_session_records_count": "integer",
      "audit_logs_count": "integer (to survive)",
      "search_index_documents_count": "integer"
    },
    "data_handling_applied": "enum",
    "data_export_package_reference": "UUID | null",
    "purge_manifest_id": "UUID | null",
    "purge_completion_deadline_utc": "ISO-8601",
    "recovery_window_deadline_utc": "ISO-8601 | null (only if EXIT_COMMITTED and recovery window active)",
    "permanent_exit_record": {
      "exit_record_id": "UUID",
      "exit_timestamp_utc": "ISO-8601",
      "exit_type": "enum",
      "exit_reason_category": "enum",
      "data_handling_applied": "enum",
      "regulatory_obligations_met": "boolean",
      "financial_obligations_met": "boolean",
      "dependency_obligations_met": "boolean"
    }
  },
  "confidence_score": "0.0–1.0",
  "model_version": "string",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "EXIT_SETTLEMENT_REQUIRED",
    "EXIT_DEPENDENCY_RESOLUTION_REQUIRED",
    "EXIT_PARENTAL_CONSENT_REQUIRED",
    "EXIT_DATA_EXPORT_GENERATION_STARTED",
    "EXIT_PURGE_PIPELINE_ACTIVATED",
    "EXIT_COMMITTED",
    "EXIT_COMPLETE",
    "GOVERNANCE_ESCALATION_TRIGGERED (if applicable)"
  ]
}
```

---

## 🧠 SECTION 5 — ML / AI LOGIC LAYER

### ML Layer — Primary (70–80% of decisions)

```
MODEL_TYPE_1: Multi-Class Classification — Exit Risk Assessment
PURPOSE: Classify exit request risk level to determine automation vs. human-review routing

  FEATURES_USED:
    - exit_subject_role
    - exit_type
    - exit_reason_category
    - data_handling_preference
    - active_financial_obligations_count
    - active_dependency_count (enrollments, evaluations, job postings)
    - exit_subject_age_group (minor flag)
    - idea_owned_with_active_royalties (boolean)
    - certification_count
    - dojo_active_session_flag (boolean)
    - days_on_platform
    - complaint_or_dispute_history (count in last 90 days)
    - tenant_offboarding_flag (boolean)
    - regulatory_order_flag (boolean)

  OUTPUT_CLASSES:
    - STANDARD_EXIT (low risk, auto-processable)
    - ELEVATED_EXIT (medium risk, expedited human review)
    - COMPLEX_EXIT (high risk, full human-in-the-loop)
    - CRITICAL_EXIT (tenant-level, court-ordered, or minor with disputed consent)

  TRAINING_FREQUENCY: Monthly (retrain as platform complexity grows)
  DRIFT_DETECTION: Monitor class distribution shift per quarter

---

MODEL_TYPE_2: Dependency Graph Analysis — Exit Cascade Risk Scoring
PURPOSE: Score the risk and impact of allowing the exit to proceed
         given the entity's connections across the platform

  FEATURES_USED:
    - enrolled_students_count (for TRAINER exits)
    - active_applicants_count (for RECRUITER exits)
    - active_projects_assigned_count
    - open_royalty_stream_count
    - tournament_evaluator_flag
    - in_flight_offer_letters_count
    - active_collaboration_count
    - parent_child_relationship_count

  OUTPUT:
    - cascade_risk_score: 0.0–1.0
    - cascade_risk_class: LOW | MEDIUM | HIGH | CRITICAL
    - estimated_affected_entities_count: integer

  THRESHOLDS:
    - cascade_risk_score > 0.70 = COMPLEX_EXIT (human review required)
    - cascade_risk_score > 0.90 = CRITICAL_EXIT (no automation; full human orchestration)

  TRAINING_FREQUENCY: Monthly
  DRIFT_DETECTION: Monitor score distribution per exit_subject_role

---

MODEL_TYPE_3: Financial Settlement Completeness Classifier
PURPOSE: Determine whether all financial obligations are resolved
         before allowing the exit to proceed

  FEATURES_USED:
    - outstanding_balance_amount
    - pending_payment_count
    - active_subscription_state
    - royalty_stream_open_count
    - disputed_transaction_count
    - refund_eligibility_amount
    - billing_cycle_position (days into current cycle)
    - proration_amount

  OUTPUT_CLASSES:
    - FINANCIALLY_CLEAR (exit may proceed)
    - SETTLEMENT_REQUIRED (exit blocked pending resolution)
    - DISPUTED_HOLD (exit blocked pending dispute resolution)

  TRAINING_FREQUENCY: Monthly
```

### AI Layer — Supplementary (20–30% of complex cases)

```
AI_USAGE_SCOPE:
  - Generate human-readable exit impact summary for HUMAN_REVIEW_QUEUE
  - Parse and classify legal/court order documents submitted with exit requests
  - Generate data export manifest descriptions for user-facing data portability package
  - Classify ambiguous exit_reason_category descriptions into known categories

PROMPT_GOVERNANCE:
  - All AI prompts versioned alongside agent version
  - AI is advisory only — exit classification is always ML + rule-engine enforced
  - AI must achieve confidence_score >= 0.92 for output to influence exit classification
  - AI may NEVER initiate or advance an exit phase — it may only summarize and classify
  - No creative interpretation of legal documents — flag for human legal review if ambiguous
  - Court order / regulatory order documents parsed by AI must be confirmed by
    COMPLIANCE_OFFICER before EXIT proceeds

AI MUST assist ML, not replace it.
AI may never classify a complex exit as STANDARD_EXIT.
```

---

## ⚙️ SECTION 6 — SCALABILITY DESIGN

```
EXPECTED_RPS:              50–500 (exit requests are low-volume, high-complexity)
LATENCY_TARGET:            < 500ms p99 for EXIT_INITIATED acknowledgement
                           < 5s p99 for full dependency graph analysis
                           < 30s for data inventory compilation on complex exits
MAX_CONCURRENCY:           Bounded — max 50 concurrent complex exits per tenant
                           (prevent resource exhaustion from bulk offboarding)
EXECUTION_STATE:           STATEFUL — exit lifecycle maintains state across multiple phases
                           State stored in EXIT_STATE_STORE (Redis + PostgreSQL)
EXIT_LIFECYCLE_TIMEOUT:    Standard exit: 30-day completion window
                           GDPR/DPDP urgent request: 30-day regulatory deadline enforced
                           Court order: Follow stated deadline in order

QUEUE_STRATEGY:
  - Kafka topic: account-exit-requests (partitioned by exit_subject_role)
  - Kafka topic: account-exit-purge-pipeline (partitioned by tenant_id)
  - Kafka topic: account-exit-settlement (partitioned by tenant_id)
  - Dead-letter queue: account-exit-dlq (failed stages re-queued here for manual review)

IDEMPOTENCY:
  - exit_request_id enforces idempotency — duplicate submissions return current phase status

GRACE_PERIOD_POLICY:
  - Default grace period: 14 days from EXIT_INITIATED (user can cancel during this window)
  - GDPR/DPDP Art. 17: Grace period applies unless user waives it
  - Court order: No grace period
  - Inactivity purge: 30-day advance notice sent before EXIT_INITIATED

RECOVERY_WINDOW:
  - After EXIT_COMMITTED: 48-hour recovery window for GOVERNANCE human appeal
  - After 48 hours: irreversible — no automated recovery possible
```

---

## 🔐 SECTION 7 — SECURITY ENFORCEMENT

```
TENANT_ISOLATION:
  - Exit of entity in Tenant A must not modify, delete, or expose data from Tenant B
  - If exit subject exists in multiple tenants: each tenant exit is independently authorized
  - Cross-tenant data purge coordination requires PLATFORM_ADMIN authorization

DOMAIN_ISOLATION:
  - Arts | Commerce | Science | Technology | Administration domain data
    are inventoried and purged independently
  - Coaching OS and School/Institute data purged independently — never merged

MFA_ENFORCEMENT:
  - Self-requested exits: Mandatory MFA re-authentication at exit request
  - Admin-initiated exits: Mandatory MFA + supervisor co-authorization for CRITICAL_EXIT
  - Minor exits: Mandatory verified parental consent (OTP or digital signature)
  - Court-ordered exits: Legal representative digital document + COMPLIANCE_OFFICER sign-off

ROLE-BASED AUTHORIZATION:
  - SELF_REQUESTED exit: Only the authenticated subject or verified parent/guardian
  - ADMIN_INITIATED exit: TENANT_ADMIN for tenant-level users; PLATFORM_ADMIN for tenants
  - PLATFORM_ADMIN self-exit: FORBIDDEN — requires board resolution
  - AUTOMATION_AGENT exit: PLATFORM_ADMIN only

ENCRYPTION:
  - All exit request data in transit: TLS 1.3 + mTLS between agents
  - Data export packages: AES-256 encrypted, password-protected, delivered via secure link
  - Audit records: AES-256 at rest + SHA-256 cryptographic chain

ANTI-RETENTION_VERIFICATION:
  - After purge completion, automated verification scans run across:
    PostgreSQL tables, Redis cache, OpenSearch indices, MinIO object storage,
    FEATURE_STORE, NOTIFICATION queues, Kafka consumer group offsets
  - Any ghost record found post-verification = SECURITY_INCIDENT_EVENT
```

---

## 🗂️ SECTION 8 — AUDIT & TRACEABILITY

Every exit phase produces an immutable, cryptographically chained audit entry. These records **survive the entity's deletion permanently**.

```json
EXIT_AUDIT_RECORD: {
  "exit_record_id": "UUID",
  "exit_request_id": "UUID",
  "phase": "INITIATED | PRE_FLIGHT | SETTLEMENT | DEPENDENCY_RESOLUTION | DATA_EXPORT | PURGE_SCHEDULED | COMMITTED | COMPLETE | BLOCKED | RECOVERED",
  "timestamp_utc": "ISO-8601",
  "requestor_actor_id": "ANONYMIZED_HASH post-EXIT_COMMITTED",
  "exit_subject_id": "ANONYMIZED_HASH post-EXIT_COMMITTED",
  "exit_subject_role": "enum",
  "tenant_id": "UUID",
  "exit_type": "enum",
  "exit_reason_category": "enum",
  "data_handling_applied": "enum",
  "input_hash": "SHA-256",
  "output_hash": "SHA-256",
  "financial_settlement_status": "enum",
  "dependency_resolution_status": "enum",
  "parental_consent_status": "enum | NOT_APPLICABLE",
  "purge_manifest_id": "UUID | null",
  "data_export_package_id": "UUID | null",
  "regulatory_obligations_met": "boolean",
  "model_version": "string",
  "confidence_score": "0.0",
  "anomaly_flags": [],
  "human_review_triggered": "boolean",
  "governance_escalation_triggered": "boolean",
  "audit_chain_hash": "SHA-256 of previous_record_hash + this_record_hash",
  "record_expiry": "NEVER — permanent record"
}
```

> **Exit audit records are immutable and permanent. They may not be deleted, modified, or archived to inaccessible cold storage.**  
> **They must remain queryable by PLATFORM_ADMIN and COMPLIANCE_OFFICER indefinitely.**

---

## 🚨 SECTION 9 — FAILURE POLICY

| Failure Scenario | Agent Behavior |
|---|---|
| **Invalid or malformed exit request** | STOP_EXECUTION → LOG_INCIDENT → Return 400 + structured error → No exit state created |
| **Authentication token expired or invalid** | DENY → LOG_INCIDENT → Prompt re-authentication → No exit state created |
| **acknowledgement_of_irreversibility = false** | HARD DENY → Return user education message → LOG |
| **Minor exit without parental consent** | EXIT_BLOCKED → ESCALATE_TO: PARENT_TRUST_LAYER_AGENT → LOG_COMPLIANCE_INCIDENT |
| **Outstanding financial obligation detected** | EXIT_PENDING_SETTLEMENT → Notify requestor → Block exit pipeline → Set 30-day settlement deadline |
| **Active dependency detected (enrollments, evaluations, postings)** | EXIT_PENDING_DEPENDENCY_RESOLUTION → Generate dependency manifest → Notify requestor + affected parties |
| **Dependency unresolved at deadline** | ESCALATE_TO: GOVERNANCE_ESCALATION_AGENT + HUMAN_REVIEW_QUEUE → Platform Admin notified |
| **BILLING_SETTLEMENT_AGENT unavailable** | BLOCK exit pipeline → LOG_INCIDENT → ESCALATE_TO: INFRASTRUCTURE_ALERT → Retry every 30 mins for 4 hours |
| **DATA_MINIMIZATION_POLICY_AGENT unavailable** | BLOCK purge pipeline → LOG_INCIDENT → ESCALATE_TO: INFRASTRUCTURE_ALERT |
| **Purge verification failure (ghost records detected)** | LOG_SECURITY_INCIDENT → ESCALATE_TO: SECURITY_INCIDENT_AGENT → Trigger secondary purge scan |
| **Court order / regulatory order document fails AI parsing** | HOLD exit → ESCALATE_TO: COMPLIANCE_OFFICER (human) → Do not auto-proceed |
| **Confidence score below threshold (< 0.85)** | HOLD classification → ESCALATE_TO: HUMAN_REVIEW_QUEUE |
| **CASCADE_RISK_SCORE > 0.90** | UPGRADE to CRITICAL_EXIT → ESCALATE_TO: GOVERNANCE_ESCALATION_AGENT → Full human orchestration required |
| **Tenant-level exit (ENTERPRISE / INSTITUTE / COACHING_OS)** | MANDATORY human approval before EXIT_COMMITTED regardless of automation |
| **Data corruption detected (hash mismatch in payload)** | STOP_EXECUTION → LOG_SECURITY_INCIDENT → ESCALATE_TO: SECURITY_INCIDENT_AGENT |
| **Recovery window appeal received** | HALT all further purge actions → ESCALATE_TO: GOVERNANCE_ESCALATION_AGENT → Human decision required within 24 hours |

```
RETRY_POLICY:
  - Service unavailability: Exponential backoff — 3 retries (1s, 2s, 4s) then DLQ
  - DLQ items: Human review required — no automated retry after DLQ
  - Financial settlement retries: Platform notifies user every 7 days for 30 days, then escalate
  - Dependency resolution retries: Platform notifies user every 3 days for 21 days, then escalate

ESCALATION_CONTACTS:
  ESCALATE_TO_GOVERNANCE:     GOVERNANCE_ESCALATION_AGENT
  ESCALATE_TO_SECURITY:       SECURITY_INCIDENT_AGENT
  ESCALATE_TO_COMPLIANCE:     COMPLIANCE_OFFICER (human)
  ESCALATE_TO_HUMAN:          HUMAN_REVIEW_QUEUE
  ESCALATE_TO_OPS:            OBSERVABILITY_AGENT + PagerDuty equivalent
  ESCALATE_TO_LEGAL:          LEGAL_TEAM_QUEUE (for court orders and regulatory orders)
```

---

## 🔗 SECTION 10 — INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  - IDENTITY_AGENT
  - RBAC_AUTHORIZATION_AGENT
  - CONSENT_MANAGEMENT_AGENT
  - BILLING_SETTLEMENT_AGENT
  - ROYALTY_ENGINE
  - DOJO_SESSION_AGENT
  - JOB_PORTAL_SERVICE
  - PROJECT_ENGINE_SERVICE
  - PARENT_TRUST_LAYER_AGENT
  - HIA_AGENT
  - IDEA_DNA_AGENT
  - CERTIFICATION_ENGINE
  - DATA_MINIMIZATION_POLICY_AGENT
  - POLICY_KNOWLEDGE_DRIFT_MONITOR_AGENT

DOWNSTREAM_AGENTS:
  - DATA_MINIMIZATION_POLICY_AGENT (purge authorization)
  - BILLING_SETTLEMENT_AGENT (settlement execution)
  - SUBSCRIPTION_MANAGER_AGENT (cancellation)
  - ROYALTY_ENGINE (finalization)
  - PURGE_SCHEDULER_AGENT (deletion manifest)
  - FEATURE_STORE_AGENT (user vector purge)
  - SEARCH_INDEX_AGENT (document removal)
  - CONSENT_MANAGEMENT_AGENT (consent record exit event)
  - IDENTITY_AGENT (account deactivation + token revocation)
  - RBAC_AUTHORIZATION_AGENT (role revocation)
  - NOTIFICATION_AGENT (user and admin exit communications)
  - PARENT_TRUST_LAYER_AGENT (child exit event)
  - DOJO_SESSION_AGENT (active session exit handling)
  - JOB_PORTAL_SERVICE (posting and applicant exit handling)
  - PROJECT_ENGINE_SERVICE (project reassignment)
  - IDEA_DNA_AGENT (idea ownership exit)
  - CERTIFICATION_ENGINE (anonymized certificate preservation)
  - HIA_AGENT (intelligence profile purge/anonymize)
  - AUDIT_ARCHIVE_AGENT (permanent exit record preservation)
  - POLICY_KNOWLEDGE_DRIFT_MONITOR_AGENT (AUTOMATION_AGENT exit notification)
  - OBSERVABILITY_AGENT (exit pipeline metrics)

EVENT_TRIGGERS:
  - account.exit.initiated          → NOTIFICATION_AGENT (user confirmation of receipt)
  - account.exit.blocked            → NOTIFICATION_AGENT, GOVERNANCE_ESCALATION_AGENT
  - account.exit.settlement_required → BILLING_SETTLEMENT_AGENT, NOTIFICATION_AGENT
  - account.exit.dependency_required → affected services, NOTIFICATION_AGENT
  - account.exit.parental_required  → PARENT_TRUST_LAYER_AGENT, NOTIFICATION_AGENT
  - account.exit.export_ready       → NOTIFICATION_AGENT (download link delivery)
  - account.exit.purge_scheduled    → PURGE_SCHEDULER_AGENT, DATA_MINIMIZATION_POLICY_AGENT
  - account.exit.committed          → IDENTITY_AGENT, RBAC_AUTHORIZATION_AGENT,
                                      FEATURE_STORE_AGENT, SEARCH_INDEX_AGENT,
                                      CONSENT_MANAGEMENT_AGENT, HIA_AGENT
  - account.exit.complete           → AUDIT_ARCHIVE_AGENT, OBSERVABILITY_AGENT
  - account.exit.ghost_record_found → SECURITY_INCIDENT_AGENT, PURGE_SCHEDULER_AGENT
  - account.exit.recovery_appealed  → GOVERNANCE_ESCALATION_AGENT
```

---

## 📊 SECTION 11 — PASSIVE INTELLIGENCE COMPATIBILITY

On exit, behavioral feature vectors must be purged from the Feature Store. This agent coordinates that purge:

```json
PURGE_FEATURE_VECTOR_INSTRUCTION: {
  "instruction_id": "UUID",
  "instruction_type": "PURGE | ANONYMIZE",
  "target_user_id": "UUID",
  "target_entity_type": "USER | TENANT",
  "feature_scope": "ALL | SPECIFIC",
  "specific_feature_names": [],
  "data_handling_applied": "enum",
  "purge_deadline_utc": "ISO-8601",
  "audit_reference": "UUID",
  "source_agent": "SAFE_ACCOUNT_EXIT_AGENT"
}
```

> **No raw behavioral data may be retained in the Feature Store post-exit for data_handling_preference = FULL_DELETION.**  
> **For ANONYMIZE_AND_RETAIN_AGGREGATES: individual-level vectors are removed; aggregate platform stats derived from them are retained.**

---

## 💡 SECTION 12 — INNOVATION ECONOMY COMPATIBILITY

Exit of a user who owns ideas in the IDEA_DNA system requires full lifecycle management:

```
IDEA EXIT PROTOCOL:

  If exit_subject has ideas with NO active royalty stream:
    → data_handling_preference = FULL_DELETION: idea record purged
    → data_handling_preference = ANONYMIZE: idea attributed to ANONYMOUS_CREATOR

  If exit_subject has ideas WITH active royalty streams:
    → EXIT_BLOCKED until royalty stream is either:
       (a) Transferred to a declared royalty_transfer_target_id
       (b) Settled with final payout disbursement
       (c) Waived by subject in writing (recorded in consent record)
    → No royalty stream may be abandoned or orphaned on exit

  EMIT on exit:
    - IDEA_VECTOR: nullified or anonymized per data_handling_preference
    - SIMILARITY_HASH: retained for COPY_DETECTION_ENGINE deduplication (no PII attached)
    - ORIGINALITY_SCORE: retained in anonymized aggregate only

COMPATIBLE WITH:
  - IDEA_DNA_AGENT
  - ROYALTY_ENGINE (must confirm SETTLED before EXIT_COMMITTED)
  - COPY_DETECTION_ENGINE (hash retention for platform integrity)
```

---

## 🏆 SECTION 13 — GROWTH ENGINE HOOK

```
RANK AND XP ON EXIT:

  On EXIT_COMMITTED:
    → RANK_UPDATE_EVENT: subject rank is ARCHIVED (not deleted — preserved in leaderboard
      history as anonymized entry for historical integrity)
    → XP_UPDATE_EVENT: XP balance is SETTLED (any transferable XP credits processed per
      platform XP settlement policy before exit)
    → Belt rank is PRESERVED in CERTIFICATION_ENGINE as anonymized record if subject
      opted for certification_preservation_preference = PRESERVE

  SHARE_TRIGGER_EVENT: FORBIDDEN on exit
    → No exit event is ever shared publicly or emitted to social channels
    → Exit is a private, confidential lifecycle event

  LEADERBOARD INTEGRITY:
    → Anonymized historical rank positions are retained for Dojo leaderboard continuity
    → Active rank slots held by the exiting subject are released and redistributed
      to the next-ranked participant within 24 hours of EXIT_COMMITTED
```

---

## 📈 SECTION 14 — PERFORMANCE MONITORING

```
METRICS_TO_EMIT (to OBSERVABILITY_AGENT):

  Volume:
  - exit_requests_initiated_daily:        count of new exit requests per day
  - exit_requests_by_type:               distribution across exit_type enum
  - exit_requests_by_role:               distribution across exit_subject_role

  Pipeline Health:
  - exit_blocked_rate:                   % of exits currently in BLOCKED state
  - settlement_pending_rate:             % pending financial settlement
  - dependency_resolution_pending_rate:  % pending dependency resolution
  - exit_complete_rate:                  % exits reaching EXIT_COMPLETE per week
  - exit_average_completion_days:        avg days from EXIT_INITIATED to EXIT_COMPLETE

  Compliance:
  - gdpr_dpdp_deadline_breach_count:     count of exits that exceeded 30-day regulatory deadline
  - minor_exit_without_consent_count:    count of blocked exits due to missing parental consent (alert if > 0)
  - ghost_record_detection_count:        count of post-purge ghost records found (alert if > 0)
  - regulatory_order_exit_count:         count of court/regulatory-ordered exits

  Financial:
  - settlement_average_resolution_days:  avg days to settle financial obligations
  - royalty_stream_orphan_attempts:      count of exits blocked by open royalty streams

  Tenant-Level:
  - tenant_offboarding_active_count:     count of tenant-level exits in progress
  - cascade_risk_critical_count:         count of exits with cascade_risk_score > 0.90

ALERTING_THRESHOLDS:
  - gdpr_dpdp_deadline_breach_count > 0 → IMMEDIATE COMPLIANCE_OFFICER alert
  - minor_exit_without_consent_count > 0 → IMMEDIATE GOVERNANCE_ESCALATION_AGENT alert
  - ghost_record_detection_count > 0 → IMMEDIATE SECURITY_INCIDENT_AGENT alert
  - tenant_offboarding_active_count > 5 simultaneously → Infrastructure capacity alert
  - exit_complete_rate < 70% over 7 days → Process health review escalation
```

---

## 🔁 SECTION 15 — EXIT LIFECYCLE STATE MACHINE

```
STATE MACHINE — STANDARD SINGLE USER EXIT:

EXIT_REQUEST_RECEIVED
    → [Pre-flight validation]
    → EXIT_INITIATED
        → [Grace period active — 14 days — user may cancel]
        → PRE_FLIGHT_CHECK
            → [Financial check] → EXIT_PENDING_SETTLEMENT (if obligations)
                                → SETTLEMENT_COMPLETE
            → [Dependency check] → EXIT_PENDING_DEPENDENCY_RESOLUTION (if dependencies)
                                 → DEPENDENCY_RESOLVED
            → [Parental consent check — if minor] → EXIT_PENDING_PARENTAL_CONSENT
                                                   → PARENTAL_CONSENT_CONFIRMED
            → [Data inventory compiled]
            → PRE_FLIGHT_CLEAR
        → [Data export — if requested]
            → DATA_EXPORT_GENERATION
            → EXIT_DATA_EXPORT_READY
            → [User downloads or 30-day link expiry]
        → [Data handling selected]
            → PURGE_MANIFEST_COMPILED
            → EXIT_PURGE_SCHEDULED
        → [Human approval gate — if COMPLEX_EXIT or CRITICAL_EXIT]
            → HUMAN_APPROVAL_RECEIVED
        → EXIT_COMMITTED (irreversible — 48hr recovery window opens)
            → [All downstream agents notified]
            → [Purge pipeline executes]
            → [Post-purge verification scan]
            → EXIT_COMPLETE
                → [Permanent audit record sealed]

TERMINAL STATES: EXIT_COMPLETE | EXIT_BLOCKED_PERMANENT (legal hold)
RECOVERABLE FROM: EXIT_COMMITTED (within 48-hour recovery window only)
CANCELLABLE FROM: Any state before EXIT_COMMITTED (during grace period)
```

---

## 📋 SECTION 16 — DATA HANDLING MATRIX PER EXIT PREFERENCE

| Data Category | `FULL_DELETION` | `ANONYMIZE_AND_RETAIN_AGGREGATES` | `EXPORT_AND_DELETE` | `EXPORT_AND_ANONYMIZE` |
|---|---|---|---|---|
| PII (name, email, phone) | PURGE within 30 days | HASH + PURGE PII | EXPORT then PURGE | EXPORT then HASH |
| Intelligence Profile (HIA 8 scores) | PURGE | ANONYMIZE + retain aggregate stats | EXPORT then PURGE | EXPORT then ANONYMIZE |
| Belt Certifications | PURGE (loses credentials) | ANONYMIZE + retain achievement stats | EXPORT then PURGE | EXPORT then ANONYMIZE |
| Portfolio / Project Evidence | PURGE | ANONYMIZE metadata | EXPORT then PURGE | EXPORT then ANONYMIZE |
| Dojo Match History | PURGE | ANONYMIZE player record | EXPORT then PURGE | EXPORT then ANONYMIZE |
| Financial Transaction Records | RETAIN 7 years (tax/legal obligation — cannot delete) | RETAIN with PII anonymized | EXPORT + RETAIN anonymized | EXPORT + RETAIN anonymized |
| Audit Log References | RETAIN PERMANENTLY (immutable audit) | RETAIN PERMANENTLY | RETAIN PERMANENTLY | RETAIN PERMANENTLY |
| Consent Records | RETAIN PERMANENTLY (proof of consent) | RETAIN PERMANENTLY | RETAIN PERMANENTLY | RETAIN PERMANENTLY |
| Behavioral Feature Vectors | PURGE from Feature Store | PURGE individual vectors | EXPORT then PURGE | EXPORT then ANONYMIZE |
| Search Index Documents | PURGE from OpenSearch | PURGE | EXPORT then PURGE | EXPORT then PURGE |
| Ideas (no royalties) | PURGE | ANONYMIZE attribution | EXPORT then PURGE | EXPORT then ANONYMIZE |
| Active Royalty Streams | BLOCK exit until settled | BLOCK exit until settled | BLOCK exit until settled | BLOCK exit until settled |
| Notification Queue Records | PURGE | PURGE | PURGE | PURGE |
| Redis Cache Entries | PURGE immediately on EXIT_COMMITTED | PURGE | PURGE | PURGE |

> **Financial transaction records have a mandatory 7-year retention obligation (Indian Companies Act, GST, and equivalent). These cannot be deleted regardless of data_handling_preference.**  
> **Audit logs and consent records are PERMANENT — no exit preference overrides this.**

---

## 🔐 SECTION 17 — ECOSKILLER ANTIGRAVITY SPECIFIC EXIT RULES

### Institute (School ERP) Tenant Exit — Special Protocol

```
INSTITUTE_EXIT_PROTOCOL:
  TRIGGER: Institute admin requests platform offboarding

  MANDATORY STEPS (in order):
  1. Platform Admin approval required — no self-service tenant exit
  2. ALL enrolled students must be individually notified (minimum 90-day notice)
  3. ALL minor student accounts: individual parental consent required for data handling
  4. Student data portability packages generated before any deletion
  5. Belt certificates and skill records exported to student-controlled format
  6. Academic reports (if stored in School ERP) exported to institute + student
  7. Parent app access revoked only after student data portability confirmed
  8. Financial settlement: all subscription invoices, outstanding balances cleared
  9. Only after ALL students have received their data export:
     → Institute configuration data purged
     → Institute tenant deregistered

  TIMELINE: Minimum 90 days from EXIT_INITIATED to EXIT_COMMITTED for Institute exits
  SHORTCUT: FORBIDDEN — no fast-track for institute-level exits
```

### Trainer / Mentor Exit — Special Protocol

```
TRAINER_EXIT_PROTOCOL:
  BLOCK exit if:
  - active_enrolled_students_count > 0 AND no replacement trainer assigned
  - active_project_mentorship_count > 0 AND no replacement mentor assigned

  DEPENDENCY_RESOLUTION OPTIONS:
  (a) Trainer waits until all enrollments naturally complete
  (b) Platform assigns replacement trainer (requires trainer consent records transfer)
  (c) Students are notified and offered course refund / transfer

  ROYALTY SETTLEMENT:
  - All earned and pending royalties must be disbursed before EXIT_COMMITTED
  - Future royalties from content published while active: transferred to
    ANONYMOUS_CREATOR pool or declared beneficiary
```

### Evaluator Exit — Special Protocol

```
EVALUATOR_EXIT_PROTOCOL:
  BLOCK exit if:
  - dojo_active_evaluation_count > 0 (active scoring sessions)
  - tournament_evaluator_flag = true AND tournament status = IN_PROGRESS

  RESOLUTION:
  - Active evaluations: void if < 30% complete, else assign to backup evaluator
  - Completed evaluations: scores preserved under ANONYMOUS_EVALUATOR tag
  - Honorarium for completed work: disbursed before EXIT_COMMITTED
```

### Recruiter / Enterprise Exit — Special Protocol

```
RECRUITER_EXIT_PROTOCOL:
  BLOCK exit if:
  - active_job_postings_count > 0 (postings must be closed or transferred)
  - in_flight_offer_letters_count > 0 (candidates must be notified and offers resolved)
  - active_shortlisted_applicants_count > 0 (applicants must be notified of status)

  CANDIDATE RIGHTS ON RECRUITER EXIT:
  - All candidates with active applications must receive written notification
    of recruiter exit and application status
  - Candidates may request their application data be deleted
  - SME reliability score of exited recruiter is archived (not deleted) for platform integrity
```

### Minor Account Exit — Special Protocol

```
MINOR_EXIT_PROTOCOL:
  For MINOR_UNDER_13 and MINOR_13_17:

  MANDATORY:
  - parental_guardian_consent_reference_id required — no exceptions
  - Parent must confirm consent via verified OTP or digital signature
  - AI-parsed consent is NOT acceptable — human-verified only

  ADDITIONAL PROTECTION:
  - Minor's intelligence profile (HIA scores): FULL_DELETION preferred by default
    (parent must explicitly opt-in to ANONYMIZE_AND_RETAIN_AGGREGATES)
  - Belt certifications: portable export generated automatically before any deletion
  - School ERP records: exported to parent and school simultaneously
  - No minor exit may be processed in < 7 days from request (cooldown protection)
```

### Dojo Arts Domain Exit — Special Rules

```
DOJO_ARTS_EXIT:
  - Creative works in Arts domain Dojo sessions are treated as
    SENSITIVE intellectual property on exit
  - All submitted creative works must be exported before deletion
  - Creative work attribution anonymized (not deleted) in session replay records
    to preserve session integrity for remaining participants
  - Arts domain skill scores: exported to user portfolio before purge
```

---

## 🔒 SECTION 18 — VERSIONING POLICY

```
VERSIONING_RULES:
  - All changes: ADD_ONLY
  - Agent version format: MAJOR.MINOR.PATCH
  - MAJOR change: New exit_type added, new exit_subject_role added,
    data handling matrix modified, lifecycle state machine changed
    → Requires GOVERNANCE_ESCALATION_AGENT + COMPLIANCE_OFFICER + LEGAL_TEAM sign-off
  - MINOR change: New metric added, new domain-specific protocol added,
    dependency resolution option added
    → Requires COMPLIANCE_OFFICER + ENGINEERING_LEAD sign-off
  - PATCH change: Bug fix, threshold tuning, notification text update
    → Requires ENGINEERING_LEAD sign-off
  - All versions: Backward compatible for 1 full version cycle
  - Deprecation: 30-day advance notice to all consuming services
  - Rollback: Permitted to any previous version via GOVERNANCE_ESCALATION approval
  - In-flight exits: Continue under the version active at EXIT_INITIATED
    (version upgrade does not apply retroactively to in-progress exits)
```

---

## 🚫 SECTION 19 — NON-NEGOTIABLE RULES (ABSOLUTE)

This agent MUST NOT:

```
❌ Initiate EXIT_COMMITTED without all pre-flight checks passing
❌ Delete financial transaction records regardless of data_handling_preference
❌ Delete audit log records or consent records — these are PERMANENT
❌ Process minor account exit without verified parental consent
❌ Allow self-exit for PLATFORM_ADMIN role
❌ Skip the financial settlement gate for any exit type
❌ Orphan active royalty streams — exit is BLOCKED until streams are settled or transferred
❌ Allow an Institute exit without 90-day student notification period
❌ Lift an exit block without the blocking condition being confirmed resolved
❌ Issue EXIT_COMMITTED for COMPLEX_EXIT or CRITICAL_EXIT without human approval
❌ Retain any PII beyond the declared purge deadline for FULL_DELETION preference
❌ Mix exit data processing for coaching OS and school tenant entities
❌ Auto-delete a user's belt certificates and portfolio without first generating an export offer
❌ Allow a Recruiter exit with in-flight offer letters without notifying all affected candidates
❌ Issue a data export package without AES-256 encryption
❌ Execute post-purge verification scan on anything less than ALL data stores (no partial scan)
❌ Process exit requests from unverified or unauthenticated actors
❌ Create hidden logic or undeclared exit paths
❌ Override GOVERNANCE_ESCALATION_AGENT decisions on blocked exits
❌ Allow the recovery window to be extended beyond 48 hours through automation
```

---

## ✅ SECTION 20 — REGULATORY COMPLIANCE OBLIGATIONS ON EXIT

```
REGULATORY_OBLIGATIONS_ENFORCED:

DPDP Act 2023 (India) — PRIMARY:
  - Right to Erasure (Section 13): Honored — full deletion pipeline
  - Data portability: Honored — export package generation
  - Minor protection (Section 9): Enforced — parental consent mandatory
  - 30-day erasure deadline: Enforced — exit pipeline SLA

GDPR (EU) — For EU enterprise tenants:
  - Art. 17 Right to Erasure: Honored
  - Art. 20 Data Portability: Honored
  - Art. 8 Minor consent: Enforced
  - 30-day response deadline: Enforced

FERPA (US) — For US institute tenants:
  - Student education records: exported to student before deletion
  - Institute retains right to archive anonymized academic records

CCPA (California):
  - Right to delete: Honored
  - Financial records: Retained per legal obligation

COPPA (US):
  - Under-13 data: FULL_DELETION is default unless parental opt-in
  - No data retained for advertising purposes

COMPANIES ACT / GST (India):
  - Financial transaction records: MANDATORY 7-year retention
  - Cannot be deleted by any exit request type
```

---

## 🔐 SECTION 21 — SEALED EXECUTION DECLARATION

```
This prompt is SEALED and LOCKED as of v1.0.0.

No agent, service, product manager, retention team, billing team, AI model,
or business logic layer may override, bypass, accelerate, or suppress
the exit lifecycle governed by this agent.

Any attempt to:
  - skip a pre-flight gate
  - bypass financial settlement
  - process a minor exit without parental consent
  - delete audit logs or financial records
  - force EXIT_COMMITTED without human approval on COMPLEX or CRITICAL exits
= SECURITY_INCIDENT_EVENT + COMPLIANCE_BREACH_EVENT

Exit is a fundamental user right. It is not a product feature. It cannot be deprioritized.

AGENT_SEAL:    SAFE_ACCOUNT_EXIT_AGENT_v1.0.0
PLATFORM:      ECOSKILLER ANTIGRAVITY
LAYER:         ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
STATUS:        ACTIVE · ENFORCED · IMMUTABLE
SCOPE:         ALL USER ROLES · ALL TENANT TYPES · ALL DOMAINS · ALL REGULATORY JURISDICTIONS
```

---

*Document Classification: INTERNAL — TRUST INFRASTRUCTURE — EXIT GOVERNANCE CORE — DO NOT DISTRIBUTE OUTSIDE GOVERNANCE CHAIN*  
*Last Updated: v1.0.0 | Add-only mutation policy applies to all future versions*
