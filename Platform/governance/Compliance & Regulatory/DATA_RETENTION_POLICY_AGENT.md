# 🔒 DATA_RETENTION_POLICY_AGENT
## ECOSKILLER ANTIGRAVITY — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Status: `SEALED · LOCKED · GOVERNED · DETERMINISTIC`
### Mutation Policy: `ADD-ONLY via Version Bump`
### Interpretation Authority: `NONE`
### Execution Authority: `Human Declaration Only`
### Document Version: `v1.0.0`
### Classification: `COMPLIANCE-CRITICAL · LEGAL-OBLIGATORY · AUDIT-MANDATORY`

---

> **LOCK DECLARATION**
> This agent specification is immutable after declaration. No field may be interpreted, assumed, filled, or generalized beyond what is explicitly stated. Every data object inside Ecoskiller Antigravity — user records, audit logs, idea chains, payment ledgers, intelligence profiles, ERP data, session records, job applications — falls under the governance of this agent. Any ambiguity in classification, retention period, or deletion authority triggers `HALT_EXECUTION`. This agent is not a cleanup utility — it is the legal data governance enforcer of a zero-trust, multi-tenant enterprise SaaS platform operating at 10M–100M user scale.

---

## 1️⃣ AGENT IDENTITY

| Field | Value |
|---|---|
| `AGENT_NAME` | `DATA_RETENTION_POLICY_AGENT` |
| `SYSTEM_ROLE` | Enterprise Data Lifecycle Governance & Compliance Enforcer |
| `PRIMARY_DOMAIN` | Data Governance · Legal Compliance · Retention Lifecycle · Secure Deletion · Regulatory Reporting |
| `EXECUTION_MODE` | `Deterministic + Validated` — Identical classification input → Identical retention policy output, always |
| `DATA_SCOPE` | All data entities across all Ecoskiller domains: User Identity, Behavioral Data, Audit Logs, Attribution Chains, Payment Records, Job Applications, ERP Records, Intelligence Profiles, Idea Submissions, Session Logs, Notification Logs, Media Assets, ML Model Artifacts, Prompt Logs |
| `TENANT_SCOPE` | `STRICT ISOLATION` — Retention policies are tenant-scoped. No cross-tenant data classification or deletion permitted |
| `FAILURE_POLICY` | `HALT ON AMBIGUITY` — If data classification is unclear, default to MAXIMUM RETENTION. Never delete under uncertainty |
| `DOMAIN_AFFILIATION` | Lane D (Governance) + Lane B (Data) + Lane G (DevOps Archival) |
| `ARCHITECTURE_LAYER` | Microservices · Event-Driven · Append-Only Audit · Scheduled Batch + Event-Triggered |
| `SECURITY_CLASS` | `CRITICAL` — Governs legal exposure, regulatory compliance, and irreversible deletion operations |
| `REGULATORY_SCOPE` | GDPR (EU) · DPDPA 2023 (India) · CCPA (US) · SOC 2 Type II · ISO 27001 data lifecycle requirements |

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

At 10M–100M user scale across multiple tenants, domains, geographies, and regulatory jurisdictions, Ecoskiller Antigravity generates massive volumes of heterogeneous data every second. Without a governed, automated, auditable retention policy engine, the platform faces:

- **Legal liability** — Retaining personal data beyond jurisdiction-mandated periods violates GDPR, DPDPA, and CCPA
- **Storage cost explosion** — Unmanaged data growth at scale becomes operationally catastrophic
- **Audit failure** — Regulators require proof of data deletion and retention compliance on demand
- **Trust erosion** — Users exercising Right to Erasure (GDPR Art. 17) or Right to Forget must receive deterministic, verifiable deletion
- **Compliance chain breaks** — ML model artifacts and prompt logs referencing deleted user data create retroactive compliance violations
- **Shadow data risk** — Backup copies, cache layers, search index replicas, and analytics snapshots retaining expired data beyond policy
- **Conflict between Append-Only Audit and Right to Erasure** — The platform must reconcile immutable audit requirements with legal deletion obligations — this agent enforces the exact resolution protocol

### What Input It Consumes

- Data entity classification requests from all agents and services
- User-submitted Right to Erasure (RTE) requests from `USER_RIGHTS_AGENT`
- Tenant termination events from `TENANT_LIFECYCLE_AGENT`
- Scheduled retention audit triggers from `SCHEDULER_AGENT`
- Data object metadata from `DATA_CATALOG_AGENT`
- Regulatory jurisdiction signals from `COMPLIANCE_AGENT`
- Storage tier transition events from `ARCHIVAL_AGENT`
- Model artifact lifecycle events from `MODEL_REGISTRY_AGENT`

### What Output It Produces

- `RETENTION_POLICY_RECORD` — Immutable classification record assigning every data entity a retention class, retention period, deletion authority, and legal hold flag
- `DELETION_DIRECTIVE` — Cryptographically signed, logged instruction authorizing deletion of a specific data entity after policy period expires
- `LEGAL_HOLD_FLAG` — Blocks deletion of data under active litigation, regulatory investigation, or dispute resolution
- `ERASURE_CONFIRMATION_CERTIFICATE` — Issued to user or tenant upon verified, complete Right to Erasure execution
- `RETENTION_AUDIT_REPORT` — Scheduled compliance report of all data objects, their retention status, and upcoming expirations
- `SHADOW_DATA_PURGE_DIRECTIVE` — Instructs backup, cache, search index, and analytics systems to purge expired data replicas
- `REGULATORY_EVIDENCE_PACKAGE` — On-demand package for regulators proving what data was held, for how long, and when deleted

### Downstream Agents That Depend on This Agent

- `ARCHIVAL_AGENT` — receives storage tier transition and cold storage directives
- `DELETION_EXECUTOR_AGENT` — receives signed DELETION_DIRECTIVE and physically executes data removal
- `SEARCH_INDEX_AGENT` — receives purge directive for expired data in OpenSearch index
- `CACHE_MANAGER_AGENT` — receives purge directive for Redis cache containing expired records
- `BACKUP_AGENT` — receives directive to purge backup snapshots containing expired data
- `COMPLIANCE_AGENT` — receives RETENTION_AUDIT_REPORT for regulatory filing
- `NOTIFICATION_AGENT` — dispatches user-facing erasure confirmation
- `OBSERVABILITY_AGENT` — receives retention health metrics and deletion execution telemetry
- `MODEL_REGISTRY_AGENT` — receives directive to retire ML artifacts referencing deleted user data

### Upstream Agents That Feed This Agent

- `DATA_CATALOG_AGENT` — provides full inventory of all data entities and their metadata
- `USER_RIGHTS_AGENT` — submits Right to Erasure and Right to Access requests
- `TENANT_LIFECYCLE_AGENT` — submits tenant termination events triggering full tenant data lifecycle close
- `SCHEDULER_AGENT` — triggers periodic retention scans and audit report generation
- `COMPLIANCE_AGENT` — provides regulatory jurisdiction classification per tenant
- `IDENTITY_AGENT` — validates actor authority for deletion directives
- `LEGAL_HOLD_AGENT` — signals active litigation holds that block deletion
- `IDEA_ATTRIBUTION_CHAIN_AGENT` — signals when attribution chain records must be preserved beyond standard retention

---

## 3️⃣ DATA CLASSIFICATION TAXONOMY (MASTER REFERENCE)

All data entities in Ecoskiller Antigravity are classified into one of the following retention classes. Classification is MANDATORY before any retention policy is applied.

```
RETENTION_CLASS_REGISTRY:

CLASS_A — LEGAL_PERMANENT
  Definition: Data with indefinite legal retention obligation
  Examples:
    - Immutable audit logs (AuditLog table)
    - Payment ledger records (Payment table)
    - Idea attribution chain records (idea_attribution_chain table)
    - Originality certificates (idea_originality_certificate table)
    - Signed contract records
    - Regulatory filing evidence packages
    - Dispute resolution final records
  Retention Period: INDEFINITE — No expiration date assigned
  Deletion Authority: PLATFORM_ADMIN + LEGAL_COUNSEL only, with dual-approval
  Legal Hold: ALWAYS_ACTIVE
  Shadow Data Rule: Backup copies inherit PERMANENT classification

CLASS_B — REGULATORY_LONG
  Definition: Data with jurisdiction-mandated long retention (5–10 years)
  Examples:
    - Financial transaction records
    - Tax-related payment data
    - Hiring records (enterprise ERP)
    - Employment verification records
    - Institute certification records
    - Royalty distribution records
  Retention Period: 7 years from creation (default) | Override by jurisdiction
  Deletion Authority: COMPLIANCE_AGENT approval + PLATFORM_ADMIN execution
  Legal Hold: Inheritable from CLASS_A disputes

CLASS_C — OPERATIONAL_MEDIUM
  Definition: Platform operational data with medium-term retention need
  Examples:
    - Job application records (after closure)
    - Course completion records
    - Project milestone records
    - Trainer evaluation records
    - Student portfolio items
    - Intelligence profile snapshots
    - GD (Dojo) session recordings and transcripts
    - Skill assessment results
    - Badge and XP award records
  Retention Period: 3 years from last activity
  Deletion Authority: DELETION_EXECUTOR_AGENT on DELETION_DIRECTIVE
  Legal Hold: Inheritable

CLASS_D — BEHAVIORAL_SHORT
  Definition: User behavioral and session data with short retention
  Examples:
    - Session logs
    - Login event logs
    - Feature usage analytics events
    - Notification delivery logs
    - Search query logs
    - Page view events
    - Feature store behavioral vectors
    - A/B test exposure records
  Retention Period: 90 days from event creation
  Deletion Authority: DELETION_EXECUTOR_AGENT on scheduled DELETION_DIRECTIVE
  Legal Hold: NOT inheritable (behavioral data excluded from most legal holds)
  PII Handling: Pseudonymize after 30 days, delete after 90 days

CLASS_E — TRANSIENT_CACHE
  Definition: Ephemeral operational data with no long-term retention need
  Examples:
    - Redis cache entries
    - Active session tokens (beyond session expiry)
    - Temporary file uploads before processing
    - API rate limit counters
    - OTP verification tokens
    - Webhook retry queues
  Retention Period: Session lifecycle + 24 hours maximum
  Deletion Authority: CACHE_MANAGER_AGENT on TTL expiry — no explicit directive required
  Legal Hold: NOT applicable

CLASS_F — USER_CONTENT_CONTROLLED
  Definition: Data whose retention is directly controlled by user Right to Erasure (RTE)
  Examples:
    - User profile data (name, email, phone, avatar)
    - User-uploaded media and files
    - User posts, comments, reactions (social feed)
    - Resume and portfolio uploaded documents
    - Personal intelligence test responses
    - Parent-linked child data records
  Retention Period: Duration of account + 30-day grace period post-deletion request
  Deletion Authority: USER_RIGHTS_AGENT initiates → DELETION_EXECUTOR_AGENT executes
  RTE Compliance Window: 30 days from verified request (GDPR Art. 17 / DPDPA Sec. 13)
  Anonymization: Where full deletion conflicts with CLASS_A audit records, pseudonymize PII in audit entries

CLASS_G — ML_ARTIFACT
  Definition: Machine learning model artifacts, training datasets, feature vectors
  Examples:
    - Trained model snapshots (all agents)
    - Training dataset snapshots
    - Feature store vectors per user
    - Prompt version archives
    - Model evaluation reports
    - Drift detection baseline snapshots
  Retention Period: Active model lifecycle + 12 months post-retirement
  Deletion Authority: MODEL_REGISTRY_AGENT initiates → DELETION_EXECUTOR_AGENT executes
  PII Rule: Any ML artifact containing PII from deleted users must be retired and retrained within 90 days of user deletion

CLASS_H — TENANT_CONTROLLED
  Definition: Tenant-specific configuration and operational data
  Examples:
    - Tenant configuration records
    - RBAC role definitions per tenant
    - Tenant-specific notification templates
    - Tenant billing and subscription records
    - Tenant-created job templates
    - Tenant ERP configuration
  Retention Period: Tenant account duration + 5 years post-termination
  Deletion Authority: TENANT_LIFECYCLE_AGENT triggers → COMPLIANCE_AGENT approves → DELETION_EXECUTOR_AGENT executes
  Note: Tenant billing data falls under CLASS_B regulatory override
```

---

## 4️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "request_id",
    "request_type",
    "actor_id",
    "actor_role",
    "tenant_id",
    "target_entity_type",
    "target_entity_id",
    "jurisdiction_code",
    "submission_timestamp_utc",
    "request_signature_token"
  ],
  "optional_fields": [
    "legal_hold_reference_id",
    "rte_request_id",
    "regulatory_filing_ref",
    "override_classification",
    "audit_package_format",
    "dispute_reference_id",
    "model_artifact_ref"
  ],
  "request_type_enum": [
    "CLASSIFY_ENTITY",
    "ISSUE_DELETION_DIRECTIVE",
    "APPLY_LEGAL_HOLD",
    "RELEASE_LEGAL_HOLD",
    "PROCESS_RTE_REQUEST",
    "GENERATE_AUDIT_REPORT",
    "GENERATE_REGULATORY_PACKAGE",
    "INITIATE_TENANT_CLOSURE",
    "SCHEDULE_RETENTION_SCAN",
    "RETIRE_ML_ARTIFACT"
  ],
  "validation_rules": [
    "request_id MUST be UUID v4",
    "request_type MUST be one of request_type_enum — unknown types = REJECT",
    "actor_id MUST resolve against IDENTITY_AGENT before any processing",
    "actor_role MUST be authorized for request_type — see Role Authorization Matrix (Section 7)",
    "tenant_id MUST match actor_id.tenant_id — mismatch = REJECT",
    "target_entity_type MUST be registered in DATA_CATALOG_AGENT — unregistered = REJECT",
    "target_entity_id MUST exist in the declared target_entity_type table — ghost IDs = REJECT",
    "jurisdiction_code MUST be ISO 3166-1 alpha-2 or 'GLOBAL' — invalid = REJECT",
    "submission_timestamp_utc MUST be ISO-8601 with UTC offset",
    "request_signature_token MUST pass JWT verification — expired or invalid = REJECT",
    "IF request_type = PROCESS_RTE_REQUEST THEN rte_request_id is REQUIRED",
    "IF request_type = APPLY_LEGAL_HOLD THEN legal_hold_reference_id is REQUIRED",
    "IF request_type = GENERATE_REGULATORY_PACKAGE THEN regulatory_filing_ref is REQUIRED",
    "IF request_type = INITIATE_TENANT_CLOSURE THEN COMPLIANCE_AGENT dual-approval token is REQUIRED",
    "override_classification MUST NOT be used without PLATFORM_ADMIN role — unauthorized override = SECURITY_VIOLATION"
  ],
  "security_checks": [
    "JWT token validation — fail = REJECT + LOG_INCIDENT",
    "Tenant isolation — actor must belong to same tenant as target_entity — mismatch = REJECT + ESCALATE",
    "Role authorization matrix check (Section 7) — insufficient role = REJECT",
    "Legal hold conflict check — if target entity has active legal_hold, DELETION_DIRECTIVE requests = REJECT",
    "Duplicate request deduplication — same request_id = REJECT with reference to original",
    "Rate limit: max 10 DELETION_DIRECTIVE requests per tenant per hour (prevents bulk accidental deletion)",
    "IP and actor anomaly signal check from SECURITY_AGENT"
  ],
  "domain_checks": [
    "target_entity_type must belong to tenant's assigned domains",
    "Cross-domain entity targeting FORBIDDEN unless PLATFORM_ADMIN role",
    "RTE request target_entity_id must belong to requesting user or verified guardian (parent role)"
  ]
}
```

**Enforcement Rules:**

- `NULL` tolerance: ZERO on all required fields
- Malformed data: REJECT with `{ validation_failure_code, field_name, rejection_timestamp_utc, audit_reference }`
- All validation failures written to `data_retention_audit_log` before response is returned
- No partial requests — all required fields must be present or the entire request is rejected

---

## 5️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "retention_record_id": "UUID",
    "request_id": "UUID — mirrors input",
    "request_type": "string — mirrors input",
    "target_entity_type": "string",
    "target_entity_id": "UUID",
    "tenant_id": "UUID",
    "jurisdiction_code": "string",
    "retention_class": "CLASS_A | CLASS_B | CLASS_C | CLASS_D | CLASS_E | CLASS_F | CLASS_G | CLASS_H",
    "retention_period_days": "integer | null (null = PERMANENT)",
    "retention_expiry_date_utc": "ISO-8601 | null (null = PERMANENT)",
    "legal_hold_active": "boolean",
    "legal_hold_reference_id": "UUID | null",
    "deletion_authorized": "boolean",
    "deletion_directive": {
      "directive_id": "UUID | null",
      "authorized_at_utc": "ISO-8601 | null",
      "execute_after_utc": "ISO-8601 | null",
      "executor_agent": "DELETION_EXECUTOR_AGENT",
      "deletion_scope": {
        "primary_table": "string",
        "shadow_targets": ["cache", "search_index", "backup", "analytics", "ml_features"],
        "anonymize_only": "boolean — true when full deletion conflicts with audit immutability"
      },
      "directive_signature": "HMAC-SHA256 signed by DATA_RETENTION_POLICY_AGENT service key"
    },
    "erasure_certificate": {
      "certificate_id": "UUID | null",
      "issued_to_user_id": "UUID | null",
      "issued_at_utc": "ISO-8601 | null",
      "scope_description": "string",
      "certificate_hash": "SHA-256",
      "regulatory_references": ["GDPR Art.17", "DPDPA Sec.13"]
    },
    "audit_report_ref": "UUID | null — reference to generated audit report",
    "next_review_date_utc": "ISO-8601 — scheduled next retention review for this entity"
  },
  "confidence_score": "decimal 0.0–1.0",
  "model_version": "string — semver",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "DELETION_EXECUTOR_AGENT.EXECUTE_DELETION | null",
    "ARCHIVAL_AGENT.TRANSITION_TO_COLD | null",
    "NOTIFICATION_AGENT.DISPATCH_ERASURE_CONFIRMATION | null",
    "COMPLIANCE_AGENT.FILE_AUDIT_REPORT | null",
    "MODEL_REGISTRY_AGENT.RETIRE_ARTIFACT | null",
    "SEARCH_INDEX_AGENT.PURGE_ENTITY | null",
    "BACKUP_AGENT.PURGE_SNAPSHOT | null"
  ]
}
```

**All outputs MUST include:**
- `confidence_score` — classification confidence; cannot be null
- `model_version` — pinned at execution start, immutable per request
- `audit_reference` — UUID traceable to `data_retention_audit_log`
- `next_trigger_event` — list of downstream events; empty `[]` is valid, `null` is NOT

**Confidence Threshold Policy:**

| Score Range | Action |
|---|---|
| `0.90 – 1.0` | PROCEED — classification confirmed, directives issued |
| `0.75 – 0.89` | PROCEED WITH FLAG — classification logged as `REVIEW_RECOMMENDED` |
| `0.60 – 0.74` | HOLD — escalate classification to `COMPLIANCE_AGENT` + `HUMAN_REVIEW_QUEUE` |
| `< 0.60` | REJECT — classification uncertain, default to MAXIMUM RETENTION (longest applicable class) |

> **Critical Rule:** When classification confidence is below `0.60`, this agent MUST default to the longest applicable retention period — **it never defaults to deletion under uncertainty.** Deletion under uncertainty is a compliance violation. Retention under uncertainty is a safe default.

---

## 6️⃣ ML / AI LOGIC LAYER

### ML Component (Primary — 80% of decisions)

| Field | Value |
|---|---|
| `MODEL_TYPE` | Multi-class Classification + Rule-Engine Hybrid |
| `PRIMARY_TASK` | Classify every data entity into a retention class (CLASS_A through CLASS_H) |
| `SECONDARY_TASK` | Predict retention expiry risk (entities approaching policy expiry in next 30 days) |
| `TERTIARY_TASK` | Anomaly detection — identify data objects with retention class conflicts or legal hold inconsistencies |

**Features Used (Explicit — No Implicit Features Permitted):**

```
FEATURE_SET_v1:
  - entity_type_category                        [categorical, enum]
  - entity_creation_timestamp_age_days          [integer]
  - entity_last_modified_age_days               [integer]
  - entity_contains_pii_flag                    [boolean]
  - entity_contains_financial_data_flag         [boolean]
  - entity_linked_to_payment_record             [boolean]
  - entity_linked_to_audit_log                  [boolean]
  - entity_linked_to_attribution_chain          [boolean]
  - tenant_jurisdiction_code                    [categorical, ISO 3166-1]
  - tenant_regulatory_tier                      [categorical: GDPR | DPDPA | CCPA | GLOBAL]
  - entity_size_bytes                           [integer]
  - entity_access_count_90d                     [integer]
  - active_legal_hold_count                     [integer, 0–N]
  - active_dispute_ref_count                    [integer, 0–N]
  - entity_domain_id                            [categorical: Arts | Commerce | Science | Technology | Administration]
  - ml_artifact_user_pii_ref_count              [integer — for CLASS_G only]
  - user_deletion_request_pending               [boolean]
  - tenant_status                               [categorical: ACTIVE | SUSPENDED | TERMINATING | TERMINATED]
  - data_shadow_replica_count                   [integer]
  - backup_snapshot_reference_count             [integer]
```

**Training Frequency:** Monthly retraining on full data catalog classification history

**Drift Detection:**
- Monitor class distribution monthly — alert if CLASS_A proportion changes > 5% (suggests systematic misclassification)
- Monitor false deletion rate weekly — threshold: 0 tolerated false deletions of CLASS_A data
- Monitor RTE compliance window breach rate — threshold: 0% tolerated (every RTE must complete within 30 days)

**Rule Engine Overrides (Hard Rules — Cannot Be Overridden by ML Score):**
```
RULE_001: IF entity_type IN [AuditLog, PaymentLedger, AttributionChain, OriginalityCertificate]
          THEN retention_class = CLASS_A ALWAYS — ML score ignored

RULE_002: IF active_legal_hold_count > 0
          THEN deletion_authorized = FALSE ALWAYS — no directive issued regardless of expiry

RULE_003: IF entity_contains_financial_data_flag = TRUE AND entity_creation_age_days < 2555
          THEN retention_class >= CLASS_B ALWAYS (2555 days = 7 years)

RULE_004: IF user_deletion_request_pending = TRUE AND retention_class NOT IN [CLASS_A, CLASS_B]
          THEN escalate to RTE workflow within 24 hours

RULE_005: IF tenant_status = TERMINATED AND entity_creation_age_days > 1825
          THEN eligible for tenant closure deletion workflow (CLASS_H rule)

RULE_006: IF ml_artifact_user_pii_ref_count > 0 AND user_deletion_request_pending = TRUE
          THEN MODEL_REGISTRY_AGENT.RETIRE_ARTIFACT event MUST be triggered within 90 days
```

---

### AI Component (Secondary — 20% of decisions)

```
AI_USAGE_SCOPE: STRICTLY DEFINED

PERMITTED AI TASKS:
  1. Generate human-readable retention policy summaries for user-facing Erasure Certificates
  2. Generate regulatory evidence package narrative descriptions for compliance officers
  3. Generate anomaly explanation narratives for HUMAN_REVIEW_QUEUE escalations
  4. Classify ambiguous entity descriptions from DATA_CATALOG_AGENT where structured features are insufficient
  5. Generate RETENTION_AUDIT_REPORT executive summary section

FORBIDDEN AI TASKS:
  - Overriding any HARD RULE from the Rule Engine
  - Assigning retention_class to CLASS_A, CLASS_B entities (hard rules govern these)
  - Authorizing or generating DELETION_DIRECTIVE
  - Interpreting legal hold applicability
  - Making any determination with financial or legal consequence without ML + Rule Engine confirmation
  - Accessing raw user PII (AI receives pseudonymized entity references only)
```

**Prompt Governance:**
- All prompts versioned in `PROMPT_REGISTRY` at `prompt_version` tag
- Temperature: `0.0` — zero creative interpretation
- Max tokens per call: `512`
- Timeout: `3000ms` — if exceeded, fallback to ML-only path
- AI receives ONLY: `entity_type`, `entity_age_days`, `jurisdiction_code`, `retention_class_proposed` — no raw content, no PII

---

## 7️⃣ ROLE AUTHORIZATION MATRIX

All requests validated against this matrix before any processing. Unauthorized requests → `REJECT + LOG_INCIDENT`.

| Request Type | Minimum Required Role |
|---|---|
| `CLASSIFY_ENTITY` | Any authenticated service agent or `COMPLIANCE_ADMIN` |
| `ISSUE_DELETION_DIRECTIVE` | `COMPLIANCE_ADMIN` (for CLASS_C, CLASS_D, CLASS_E, CLASS_F, CLASS_G, CLASS_H) |
| `ISSUE_DELETION_DIRECTIVE` (CLASS_A) | `PLATFORM_ADMIN` + `LEGAL_COUNSEL` dual-approval only |
| `APPLY_LEGAL_HOLD` | `LEGAL_COUNSEL` or `PLATFORM_ADMIN` |
| `RELEASE_LEGAL_HOLD` | `LEGAL_COUNSEL` only — `PLATFORM_ADMIN` alone is INSUFFICIENT |
| `PROCESS_RTE_REQUEST` | `USER_RIGHTS_AGENT` (system role) — initiated by user via verified channel |
| `GENERATE_AUDIT_REPORT` | `COMPLIANCE_ADMIN` or `PLATFORM_ADMIN` |
| `GENERATE_REGULATORY_PACKAGE` | `PLATFORM_ADMIN` + `LEGAL_COUNSEL` dual-approval |
| `INITIATE_TENANT_CLOSURE` | `PLATFORM_ADMIN` + `COMPLIANCE_AGENT` dual-approval |
| `SCHEDULE_RETENTION_SCAN` | `SCHEDULER_AGENT` (system role) |
| `RETIRE_ML_ARTIFACT` | `MODEL_REGISTRY_AGENT` (system role) or `COMPLIANCE_ADMIN` |

---

## 8️⃣ RETENTION PERIOD REFERENCE TABLE

| Data Entity | Retention Class | Retention Period | Deletion Method | Legal Hold Eligible |
|---|---|---|---|---|
| AuditLog | CLASS_A | PERMANENT | Dual-approval only | ALWAYS ACTIVE |
| Payment / PaymentLedger | CLASS_A | PERMANENT (financial) | Dual-approval only | YES |
| AttributionChain | CLASS_A | PERMANENT | Dual-approval only | YES |
| OriginalityCertificate | CLASS_A | PERMANENT | Dual-approval only | YES |
| SignedContracts | CLASS_A | PERMANENT | Dual-approval only | YES |
| Tax / Financial Transaction Records | CLASS_B | 7 years | COMPLIANCE_ADMIN | YES |
| Enterprise Hiring Records (ERP) | CLASS_B | 7 years | COMPLIANCE_ADMIN | YES |
| Royalty Distribution Records | CLASS_B | 7 years | COMPLIANCE_ADMIN | YES |
| Certification / Institute Records | CLASS_B | 7 years | COMPLIANCE_ADMIN | YES |
| Job Application Records | CLASS_C | 3 years post-closure | DELETION_EXECUTOR | YES |
| Course Completion Records | CLASS_C | 3 years post-completion | DELETION_EXECUTOR | YES |
| Dojo GD Session Recordings | CLASS_C | 3 years post-session | DELETION_EXECUTOR | YES |
| Intelligence Profile Snapshots | CLASS_C | 3 years post-last-activity | DELETION_EXECUTOR | YES |
| XP / Badge Award Records | CLASS_C | 3 years post-award | DELETION_EXECUTOR | YES |
| Student Skill Assessment | CLASS_C | 3 years | DELETION_EXECUTOR | YES |
| Session Logs | CLASS_D | 90 days | DELETION_EXECUTOR (auto) | NO |
| Feature Store Behavioral Vectors | CLASS_D | 90 days | DELETION_EXECUTOR (auto) | NO |
| Notification Delivery Logs | CLASS_D | 90 days | DELETION_EXECUTOR (auto) | NO |
| Search Query Logs | CLASS_D | 90 days (pseudonymize at 30d) | DELETION_EXECUTOR (auto) | NO |
| Login Event Logs | CLASS_D | 90 days | DELETION_EXECUTOR (auto) | NO |
| Redis Cache Entries | CLASS_E | TTL + 24 hours max | CACHE_MANAGER_AGENT | NO |
| Active Session Tokens | CLASS_E | Session expiry | CACHE_MANAGER_AGENT | NO |
| OTP Tokens | CLASS_E | 10 minutes | CACHE_MANAGER_AGENT | NO |
| User Profile (PII) | CLASS_F | Account duration + 30-day RTE window | USER_RIGHTS_AGENT | NO (anonymize conflict) |
| User-Uploaded Media | CLASS_F | Account duration + 30-day RTE window | DELETION_EXECUTOR | NO |
| Social Feed Posts | CLASS_F | Account duration + 30-day RTE window | DELETION_EXECUTOR | NO |
| Intelligence Test Responses | CLASS_F | Account duration + 30-day RTE window | DELETION_EXECUTOR | NO |
| ML Model Artifacts | CLASS_G | Active lifecycle + 12 months post-retirement | MODEL_REGISTRY_AGENT | NO |
| Feature Store Vectors (named user) | CLASS_G | Linked to user lifecycle | MODEL_REGISTRY_AGENT | NO |
| Prompt Version Archives | CLASS_G | Active lifecycle + 12 months | MODEL_REGISTRY_AGENT | NO |
| Tenant Configuration | CLASS_H | Tenant duration + 5 years | TENANT_LIFECYCLE_AGENT | YES |
| Tenant RBAC Records | CLASS_H | Tenant duration + 5 years | TENANT_LIFECYCLE_AGENT | YES |
| Tenant Billing Records | CLASS_B override | 7 years (regulatory override) | COMPLIANCE_ADMIN | YES |

---

## 9️⃣ RIGHT TO ERASURE (RTE) WORKFLOW — LOCKED PROTOCOL

This is the most legally sensitive workflow in the agent. Every step is mandatory and audited.

```
RTE_WORKFLOW_v1:

Step 1 — REQUEST RECEIPT
  - Receive RTE request via USER_RIGHTS_AGENT
  - Validate: actor_id = requesting user OR verified guardian (parent role)
  - Validate: jurisdiction eligibility (GDPR Art.17 / DPDPA Sec.13 / CCPA)
  - Log receipt with timestamp_utc to data_retention_audit_log
  - Issue: RTE_ACKNOWLEDGEMENT to user via NOTIFICATION_AGENT within 24 hours

Step 2 — ENTITY SCOPE DISCOVERY
  - Query DATA_CATALOG_AGENT for all entities linked to actor_id
  - Classify each entity against retention class taxonomy
  - Identify conflicts:
    a. CLASS_A entities → CANNOT be deleted → pseudonymize PII fields only
    b. CLASS_B entities → CANNOT be deleted before 7-year period → notify user of limitation
    c. CLASS_C, CLASS_D, CLASS_F entities → eligible for deletion
    d. CLASS_G ML artifacts referencing user → schedule retirement within 90 days
  - Total scope documented as RTE_SCOPE_RECORD

Step 3 — LEGAL HOLD CHECK
  - Query LEGAL_HOLD_AGENT for any active holds on user data
  - If active hold exists: notify user, explain delay, pause RTE for held entities only
  - Non-held entities continue to RTE execution

Step 4 — DELETION DIRECTIVE ISSUANCE
  - For each eligible entity: issue DELETION_DIRECTIVE with execute_after_utc = now() + 0 (immediate)
  - For shadow data: issue SHADOW_DATA_PURGE_DIRECTIVE to:
    a. SEARCH_INDEX_AGENT — remove from OpenSearch index
    b. CACHE_MANAGER_AGENT — purge from Redis
    c. BACKUP_AGENT — mark for purge from next backup rotation (max 30-day backup retention window)
    d. ANALYTICS_AGENT — pseudonymize in analytics snapshots

Step 5 — AUDIT RECORD ANONYMIZATION (CLASS_A conflict resolution)
  - For audit log entries containing user PII that cannot be deleted:
    - Replace: actor_id → hash(actor_id + SALT)
    - Replace: email/phone → [REDACTED_GDPR]
    - Replace: name → [REDACTED_GDPR]
    - Preserve: action, entity, entity_id, timestamp_utc (non-PII fields)
    - Log: ANONYMIZATION_APPLIED = TRUE in audit entry metadata

Step 6 — ERASURE CERTIFICATE ISSUANCE
  - Verify: all non-exempt entities deleted or pseudonymized
  - Verify: shadow data purge acknowledged by all target agents
  - Issue: ERASURE_CONFIRMATION_CERTIFICATE with SHA-256 certificate hash
  - Dispatch: certificate to user via NOTIFICATION_AGENT
  - Deadline: All steps complete within 30 days of RTE request receipt

Step 7 — REGULATORY RECORD
  - File: RTE completion record to COMPLIANCE_AGENT
  - Retain: RTE_SCOPE_RECORD and ERASURE_CERTIFICATE in CLASS_A (they prove compliance)
  - RTE records themselves are PERMANENT (proving deletion is a legal obligation)

FAILURE AT ANY STEP:
  → STOP_EXECUTION
  → LOG_INCIDENT with step_number and failure_reason
  → ESCALATE_TO: COMPLIANCE_ADMIN + PLATFORM_ADMIN
  → NOTIFY_USER: delay acknowledged with expected resolution date
  → NEVER issue ERASURE_CERTIFICATE if deletion is incomplete
```

---

## 🔟 TENANT CLOSURE WORKFLOW — LOCKED PROTOCOL

```
TENANT_CLOSURE_WORKFLOW_v1:

Trigger: TENANT_LIFECYCLE_AGENT emits TENANT_TERMINATION_EVENT

Step 1 — DUAL APPROVAL GATE
  - Require: PLATFORM_ADMIN signed approval token
  - Require: COMPLIANCE_AGENT signed approval token
  - Both tokens must be present — single approval = REJECT

Step 2 — DATA INVENTORY
  - Query DATA_CATALOG_AGENT for complete tenant data inventory
  - Classify all entities by retention class

Step 3 — FINANCIAL AND LEGAL HOLD SWEEP
  - Query LEGAL_HOLD_AGENT and ROYALTY_ENGINE for any outstanding obligations
  - If unpaid royalties exist → BLOCK CLOSURE until resolved
  - If active litigation hold → BLOCK CLOSURE for held entities, proceed for non-held

Step 4 — REGULATORY DATA PRESERVATION
  - CLASS_A, CLASS_B entities → move to COLD STORAGE under ARCHIVAL_AGENT
  - Tenant loses access immediately — data preserved for regulatory period only
  - Issue TENANT_DATA_PRESERVATION_RECORD

Step 5 — CLASS_C, CLASS_D, CLASS_F, CLASS_E DELETION
  - Issue DELETION_DIRECTIVE for all non-regulatory tenant data
  - Purge shadow data (search index, cache, analytics)

Step 6 — ML ARTIFACT RETIREMENT
  - Issue RETIRE_ARTIFACT directive to MODEL_REGISTRY_AGENT for all tenant-scoped ML models
  - Tenant-specific feature vectors → purge from FEATURE_STORE_AGENT

Step 7 — CLOSURE CERTIFICATE
  - Issue TENANT_CLOSURE_CERTIFICATE with scope, deletion dates, and preserved records list
  - File with COMPLIANCE_AGENT

FAILURE AT ANY STEP:
  → HALT_CLOSURE
  → LOG_INCIDENT
  → ESCALATE_TO: PLATFORM_ADMIN + LEGAL_COUNSEL
```

---

## 1️⃣1️⃣ SCALABILITY DESIGN

| Parameter | Value |
|---|---|
| `EXPECTED_RPS` | 500 rps baseline classification · 2,000 rps peak (scheduled batch scan events) |
| `LATENCY_TARGET` | p50: 150ms · p95: 500ms · p99: 1,200ms (classification) · Batch: async, no latency SLA |
| `MAX_CONCURRENCY` | 200 concurrent retention policy operations per tenant |
| `QUEUE_STRATEGY` | Redis Streams — partitioned by `tenant_id` · Separate queue for RTE requests (priority lane) |
| `PROCESSING_MODE` | Hybrid: Real-time (RTE, legal hold, classification) + Batch (scheduled audit scans, expiry sweeps) |
| `STATELESS_EXECUTION` | YES — all state in PostgreSQL + Redis; agent instances are ephemeral |
| `IDEMPOTENCY` | YES — deduplicated by `request_id`; same request replayed = returns original result |
| `HORIZONTAL_SCALING` | Kubernetes HPA on queue depth and CPU |
| `BATCH_SCAN_FREQUENCY` | Daily at 02:00 UTC — scan all entities for upcoming expiry within 30-day window |

**Scheduled Batch Jobs:**
```yaml
daily_expiry_scan:
  schedule: "0 2 * * *"          # 02:00 UTC daily
  action: Scan all entities approaching expiry within 30 days
  output: DELETION_DIRECTIVE batch queue for DELETION_EXECUTOR_AGENT

weekly_legal_hold_review:
  schedule: "0 3 * * 1"          # 03:00 UTC every Monday
  action: Review all active legal holds for release eligibility
  output: LEGAL_HOLD_STATUS_REPORT to COMPLIANCE_AGENT

monthly_audit_report:
  schedule: "0 4 1 * *"          # 04:00 UTC on the 1st of each month
  action: Generate full RETENTION_AUDIT_REPORT across all tenants
  output: Report filed to COMPLIANCE_AGENT and stored in ARCHIVAL_AGENT

quarterly_ml_artifact_review:
  schedule: "0 5 1 1,4,7,10 *"   # 05:00 UTC quarterly
  action: Review all CLASS_G ML artifacts for retirement eligibility
  output: RETIRE_ARTIFACT directives to MODEL_REGISTRY_AGENT
```

---

## 1️⃣2️⃣ SECURITY ENFORCEMENT

**All security checks execute before any business logic. No exceptions.**

| Security Control | Enforcement |
|---|---|
| Tenant Isolation | `actor_id.tenant_id === input.tenant_id` — hard check, REJECT on mismatch |
| Domain Isolation | Data entities in one domain cannot be targeted by actors in another domain |
| Role Authorization Matrix | Enforced per Section 7 — unauthorized role = REJECT + LOG_INCIDENT |
| JWT Verification | Every request carries valid Keycloak JWT — expired or invalid = REJECT |
| Deletion Directive Signing | Every DELETION_DIRECTIVE signed with HMAC-SHA256 using agent service key |
| Deletion Signature Verification | DELETION_EXECUTOR_AGENT must verify signature before executing any deletion |
| Dual Approval Enforcement | CLASS_A deletion and tenant closure require two independent signed approval tokens |
| Audit Logging | Every request logged to append-only `data_retention_audit_log` before processing |
| Rate Limiting | Max 10 DELETION_DIRECTIVE requests per tenant per hour — enforced at Kong API Gateway |
| Shadow Data Tracking | All purge directives acknowledged by target agents — unacknowledged = ESCALATE |
| Cold Storage Access Control | CLASS_A, CLASS_B data in cold storage accessible only to PLATFORM_ADMIN + LEGAL_COUNSEL |
| Zero-Trust Inter-Agent Auth | All inter-agent calls carry signed service token from Keycloak — no implicit trust |
| Encryption In Transit | All retention directives TLS 1.3 minimum |
| Encryption At Rest | All retention records AES-256 |

---

## 1️⃣3️⃣ AUDIT & TRACEABILITY

Every execution — classification, directive, RTE step, batch scan — MUST produce an immutable audit entry:

```json
AUDIT_LOG_ENTRY: {
  "audit_id": "UUID",
  "timestamp_utc": "ISO-8601",
  "actor_id": "UUID — pseudonymized after user RTE if applicable",
  "agent_id": "DATA_RETENTION_POLICY_AGENT",
  "agent_version": "semver",
  "tenant_id": "UUID",
  "request_type": "string",
  "target_entity_type": "string",
  "target_entity_id": "UUID",
  "jurisdiction_code": "string",
  "retention_class_assigned": "CLASS_A through CLASS_H",
  "retention_period_days": "integer | null",
  "legal_hold_active": "boolean",
  "deletion_authorized": "boolean",
  "deletion_directive_id": "UUID | null",
  "rte_request_id": "UUID | null",
  "input_hash": "SHA-256 of full input payload",
  "output_hash": "SHA-256 of full output payload",
  "model_version": "string",
  "prompt_version": "string | null",
  "decision_path": "RULE_ENGINE | ML_ONLY | ML+AI | RULE_OVERRIDE | REJECTED",
  "rule_triggered": "RULE_001 through RULE_006 | null",
  "confidence_score": "decimal",
  "anomaly_flags": ["list or empty array"],
  "execution_duration_ms": "integer",
  "audit_reference": "UUID — matches output.audit_reference"
}
```

**Audit Ledger Rules:**
- Table: `data_retention_audit_log` — append-only, `UPDATE` and `DELETE` DENIED to all roles including `PLATFORM_ADMIN`
- Exception: PII anonymization in audit fields upon RTE (as described in Section 9, Step 5) — only PII fields are overwritten with `[REDACTED_GDPR]`, structural data preserved
- PostgreSQL Row Security: `INSERT` to `DATA_RETENTION_POLICY_AGENT` service role only
- Retention of this audit log: CLASS_A (PERMANENT) — the audit log itself cannot be deleted
- Exported to cold storage monthly via `ARCHIVAL_AGENT`
- Retention: 10 years minimum (exceeds all jurisdiction minimums)

---

## 1️⃣4️⃣ FAILURE POLICY

| Failure Scenario | Policy |
|---|---|
| Invalid input (schema fail) | `STOP_EXECUTION` → `LOG_INCIDENT` → return structured rejection with field-level detail |
| JWT invalid or expired | `STOP_EXECUTION` → `LOG_INCIDENT` → `401 UNAUTHORIZED` |
| Tenant or domain mismatch | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: SECURITY_AGENT` |
| Unauthorized role | `STOP_EXECUTION` → `LOG_INCIDENT` → `403 FORBIDDEN` |
| DATA_CATALOG_AGENT unavailable | `STOP_EXECUTION` → `LOG_INCIDENT` → `RETRY: 3 attempts, 3s exponential backoff` → `ESCALATE_TO: OBSERVABILITY_AGENT` |
| LEGAL_HOLD_AGENT unavailable | `STOP_EXECUTION` — NEVER issue deletion directive without legal hold confirmation |
| ML model unavailable | `STOP_EXECUTION` → apply RULE_ENGINE hard rules only → `LOG_INCIDENT` → `ESCALATE_TO: OBSERVABILITY_AGENT` |
| Confidence score < 0.60 | `STOP_EXECUTION` → default to MAXIMUM RETENTION → `ESCALATE_TO: HUMAN_REVIEW_QUEUE` |
| DELETION_EXECUTOR_AGENT fails to acknowledge directive | `LOG_INCIDENT` → `RETRY: 3 times, 1-hour interval` → `ESCALATE_TO: PLATFORM_ADMIN` |
| Shadow purge unacknowledged after 72 hours | `LOG_INCIDENT (CRITICAL)` → `ESCALATE_TO: COMPLIANCE_ADMIN` |
| Dual approval tokens invalid | `STOP_EXECUTION` → `LOG_INCIDENT` → `ESCALATE_TO: PLATFORM_ADMIN + LEGAL_COUNSEL` |
| RTE deadline risk (>25 days elapsed, incomplete) | `LOG_INCIDENT (CRITICAL)` → `ESCALATE_TO: COMPLIANCE_ADMIN + PLATFORM_ADMIN` |
| Data corruption in retention record | `STOP_EXECUTION` → `LOG_INCIDENT (CRITICAL)` → `ESCALATE_TO: PLATFORM_ADMIN` — all deletions for tenant SUSPENDED |

```
RETRY_POLICY:
  max_retries: 3
  backoff_strategy: EXPONENTIAL
  backoff_base_ms: 3000
  max_backoff_ms: 30000
  retry_on: [UPSTREAM_TIMEOUT, TRANSIENT_DB_ERROR]
  no_retry_on: [VALIDATION_FAILURE, SECURITY_VIOLATION, UNAUTHORIZED, DATA_CORRUPTION]

ESCALATION_TARGETS:
  OBSERVABILITY_AGENT:    infrastructure failures
  COMPLIANCE_AGENT:       regulatory and policy violations
  SECURITY_AGENT:         identity and isolation breaches
  HUMAN_REVIEW_QUEUE:     classification ambiguity (confidence < 0.60)
  PLATFORM_ADMIN:         critical failures and CLASS_A operations
  LEGAL_COUNSEL:          legal hold and regulatory conflicts
  COMPLIANCE_ADMIN:       RTE compliance deadline risks, audit failures
```

**No Silent Failures. No Deletion Under Uncertainty. No Compliance Shortcuts.**

---

## 1️⃣5️⃣ INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  - DATA_CATALOG_AGENT              → provides entity metadata and inventory
  - USER_RIGHTS_AGENT               → submits RTE and Right to Access requests
  - TENANT_LIFECYCLE_AGENT          → submits tenant termination events
  - SCHEDULER_AGENT                 → triggers scheduled batch scans and reports
  - COMPLIANCE_AGENT                → provides jurisdiction classification per tenant
  - IDENTITY_AGENT                  → validates actor identity and role
  - LEGAL_HOLD_AGENT                → signals active litigation holds
  - IDEA_ATTRIBUTION_CHAIN_AGENT    → signals attribution records requiring extended preservation
  - MODEL_REGISTRY_AGENT            → reports ML artifact lifecycle events

DOWNSTREAM_AGENTS:
  - DELETION_EXECUTOR_AGENT         → receives signed DELETION_DIRECTIVE
  - ARCHIVAL_AGENT                  → receives cold storage transition directive
  - SEARCH_INDEX_AGENT              → receives purge directive for OpenSearch
  - CACHE_MANAGER_AGENT             → receives purge directive for Redis
  - BACKUP_AGENT                    → receives purge directive for backup snapshots
  - ANALYTICS_AGENT                 → receives pseudonymization directive
  - COMPLIANCE_AGENT                → receives RETENTION_AUDIT_REPORT
  - NOTIFICATION_AGENT              → dispatches ERASURE_CONFIRMATION_CERTIFICATE to user
  - OBSERVABILITY_AGENT             → receives health metrics and incident reports
  - MODEL_REGISTRY_AGENT            → receives RETIRE_ARTIFACT directive

EVENT_TRIGGERS (Emitted by This Agent):
  - RETENTION_POLICY_CLASSIFIED     → consumed by DATA_CATALOG_AGENT
  - DELETION_DIRECTIVE_ISSUED       → consumed by DELETION_EXECUTOR_AGENT
  - LEGAL_HOLD_APPLIED              → consumed by DELETION_EXECUTOR_AGENT (blocks any pending directives)
  - LEGAL_HOLD_RELEASED             → consumed by DELETION_EXECUTOR_AGENT (resumes pending directives)
  - RTE_ACKNOWLEDGED                → consumed by NOTIFICATION_AGENT
  - ERASURE_CERTIFICATE_ISSUED      → consumed by NOTIFICATION_AGENT, COMPLIANCE_AGENT
  - SHADOW_PURGE_DIRECTIVE_ISSUED   → consumed by SEARCH_INDEX_AGENT, CACHE_MANAGER_AGENT, BACKUP_AGENT
  - TENANT_CLOSURE_INITIATED        → consumed by all downstream agents for tenant scope
  - RETENTION_AUDIT_REPORT_READY    → consumed by COMPLIANCE_AGENT
  - ML_ARTIFACT_RETIRE_TRIGGERED    → consumed by MODEL_REGISTRY_AGENT
```

**All events emitted to Redis Streams with:**
- `event_id`: UUID
- `agent_source`: `DATA_RETENTION_POLICY_AGENT`
- `tenant_id`: strict tenant scope
- `timestamp_utc`: ISO-8601
- `payload_schema_version`: semver

---

## 1️⃣6️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent touches user data lifecycle. It MUST emit feature vectors to `FEATURE_STORE_AGENT` where applicable, but with strict PII restrictions:

```json
EMIT_FEATURE_VECTOR (anonymized, no PII): {
  "user_id": "pseudonymized_user_ref",
  "feature_name": "data_retention_rte_requested",
  "feature_value": "1 (boolean event)",
  "timestamp": "ISO-8601",
  "source_agent": "DATA_RETENTION_POLICY_AGENT"
}

EMIT_FEATURE_VECTOR: {
  "user_id": "pseudonymized_user_ref",
  "feature_name": "data_entity_count_active",
  "feature_value": "integer count",
  "timestamp": "ISO-8601",
  "source_agent": "DATA_RETENTION_POLICY_AGENT"
}
```

> **Critical Rule:** Feature vectors emitted by this agent MUST use pseudonymized user references only. Raw `user_id` is FORBIDDEN in emitted feature vectors if user has an active RTE request. Feature vectors for users with completed RTE are deleted as CLASS_D data per retention policy.

---

## 1️⃣7️⃣ PERFORMANCE MONITORING

**Required Metrics — Emitted to OBSERVABILITY_AGENT every 60 seconds:**

| Metric | Type | Alert Threshold |
|---|---|---|
| `classification_success_rate` | Percentage | < 98% → WARNING |
| `rte_compliance_window_breach_rate` | Percentage | > 0% → CRITICAL (zero tolerance) |
| `legal_hold_conflict_rate` | Percentage | > 0% unexpected holds → WARNING |
| `deletion_directive_execution_rate` | Percentage | < 99% → WARNING |
| `shadow_purge_acknowledgement_rate` | Percentage | < 100% within 72h → CRITICAL |
| `avg_classification_latency_ms` | Gauge | p95 > 500ms → WARNING |
| `ml_model_confidence_avg` | Gauge | < 0.80 → WARNING (drift risk) |
| `rule_engine_override_rate` | Percentage | > 15% → REVIEW (possible ML drift) |
| `rte_active_count` | Gauge | Real-time count of open RTE requests |
| `rte_age_max_days` | Gauge | > 25 days → CRITICAL (approaching 30-day deadline) |
| `legal_hold_active_count` | Gauge | Informational — no threshold |
| `audit_log_write_failure_count` | Counter | > 0 → CRITICAL (compliance breach) |
| `tenant_closure_pending_count` | Gauge | Informational |
| `class_a_deletion_attempt_count` | Counter | > 0 without dual approval → CRITICAL (security alert) |

**Grafana Dashboard:** `DATA_RETENTION_POLICY_AGENT — Compliance & Lifecycle Monitor`
**Tracing:** Jaeger spans per request with `retention_record_id` as correlation ID
**Alerting:** PagerDuty integration for all CRITICAL alerts — zero tolerance on RTE breach and unauthorized deletion attempts

---

## 1️⃣8️⃣ DATABASE SCHEMA (REQUIRED ENTITIES)

```sql
-- Master retention policy record
CREATE TABLE data_retention_policy_record (
  retention_record_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  target_entity_type        VARCHAR(128) NOT NULL,
  target_entity_id          UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  jurisdiction_code         VARCHAR(8) NOT NULL,
  retention_class           VARCHAR(16) NOT NULL CHECK (retention_class IN
    ('CLASS_A','CLASS_B','CLASS_C','CLASS_D','CLASS_E','CLASS_F','CLASS_G','CLASS_H')),
  retention_period_days     INTEGER,          -- NULL = PERMANENT
  retention_expiry_date_utc TIMESTAMPTZ,      -- NULL = PERMANENT
  legal_hold_active         BOOLEAN NOT NULL DEFAULT FALSE,
  legal_hold_reference_id   UUID,
  deletion_authorized       BOOLEAN NOT NULL DEFAULT FALSE,
  deletion_directive_id     UUID,
  rule_triggered            VARCHAR(32),
  classified_at_utc         TIMESTAMPTZ NOT NULL DEFAULT now(),
  next_review_date_utc      TIMESTAMPTZ NOT NULL,
  model_version             VARCHAR(32) NOT NULL,
  confidence_score          NUMERIC(5,4) NOT NULL,
  -- Append-only: no UPDATE permitted
  CONSTRAINT valid_retention CHECK (
    (retention_period_days IS NULL AND retention_expiry_date_utc IS NULL) OR
    (retention_period_days IS NOT NULL AND retention_expiry_date_utc IS NOT NULL)
  )
);

-- Deletion directives (signed, append-only)
CREATE TABLE data_deletion_directive (
  directive_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  retention_record_id       UUID NOT NULL REFERENCES data_retention_policy_record(retention_record_id),
  tenant_id                 UUID NOT NULL,
  target_entity_type        VARCHAR(128) NOT NULL,
  target_entity_id          UUID NOT NULL,
  authorized_by_actor_id    UUID NOT NULL,
  dual_approval_token_hash  VARCHAR(128),     -- Required for CLASS_A
  execute_after_utc         TIMESTAMPTZ NOT NULL,
  execution_status          VARCHAR(32) NOT NULL DEFAULT 'PENDING'
    CHECK (execution_status IN ('PENDING','EXECUTING','COMPLETED','FAILED','BLOCKED_LEGAL_HOLD')),
  executed_at_utc           TIMESTAMPTZ,
  directive_signature       TEXT NOT NULL,   -- HMAC-SHA256
  shadow_purge_cache        BOOLEAN NOT NULL DEFAULT FALSE,
  shadow_purge_search       BOOLEAN NOT NULL DEFAULT FALSE,
  shadow_purge_backup       BOOLEAN NOT NULL DEFAULT FALSE,
  shadow_purge_analytics    BOOLEAN NOT NULL DEFAULT FALSE,
  anonymize_only            BOOLEAN NOT NULL DEFAULT FALSE,
  created_at_utc            TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Legal hold registry
CREATE TABLE data_legal_hold (
  hold_id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id                 UUID NOT NULL,
  target_entity_type        VARCHAR(128),
  target_entity_id          UUID,
  hold_scope                VARCHAR(32) NOT NULL CHECK (hold_scope IN ('ENTITY','USER','TENANT','DOMAIN')),
  legal_reference           TEXT NOT NULL,
  applied_by_actor_id       UUID NOT NULL,
  applied_at_utc            TIMESTAMPTZ NOT NULL DEFAULT now(),
  released_by_actor_id      UUID,
  released_at_utc           TIMESTAMPTZ,
  status                    VARCHAR(16) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','RELEASED','EXPIRED'))
);

-- RTE request tracking
CREATE TABLE data_rte_request (
  rte_request_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id                   UUID NOT NULL,
  tenant_id                 UUID NOT NULL,
  jurisdiction_code         VARCHAR(8) NOT NULL,
  received_at_utc           TIMESTAMPTZ NOT NULL DEFAULT now(),
  deadline_utc              TIMESTAMPTZ NOT NULL,  -- received_at + 30 days
  status                    VARCHAR(32) NOT NULL DEFAULT 'RECEIVED'
    CHECK (status IN ('RECEIVED','SCOPING','IN_PROGRESS','PARTIALLY_COMPLETE','COMPLETE','BLOCKED_LEGAL_HOLD')),
  scope_record_id           UUID,
  certificate_id            UUID,
  completed_at_utc          TIMESTAMPTZ,
  blocked_reason            TEXT
);

-- Erasure certificates
CREATE TABLE data_erasure_certificate (
  certificate_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  rte_request_id            UUID NOT NULL REFERENCES data_rte_request(rte_request_id),
  user_id_pseudonymized     VARCHAR(128) NOT NULL, -- pseudonymized after issuance
  tenant_id                 UUID NOT NULL,
  scope_description         TEXT NOT NULL,
  certificate_hash          CHAR(64) NOT NULL,
  issued_at_utc             TIMESTAMPTZ NOT NULL DEFAULT now(),
  regulatory_references     TEXT[] NOT NULL
  -- CLASS_A retention: PERMANENT (proving deletion is itself a legal obligation)
);

-- Immutable audit log
CREATE TABLE data_retention_audit_log (
  audit_id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timestamp_utc             TIMESTAMPTZ NOT NULL DEFAULT now(),
  actor_id                  VARCHAR(128) NOT NULL, -- may be pseudonymized post-RTE
  agent_id                  VARCHAR(128) NOT NULL DEFAULT 'DATA_RETENTION_POLICY_AGENT',
  agent_version             VARCHAR(32) NOT NULL,
  tenant_id                 UUID NOT NULL,
  request_type              VARCHAR(64) NOT NULL,
  target_entity_type        VARCHAR(128),
  target_entity_id          UUID,
  jurisdiction_code         VARCHAR(8),
  retention_class_assigned  VARCHAR(16),
  legal_hold_active         BOOLEAN,
  deletion_authorized       BOOLEAN,
  deletion_directive_id     UUID,
  rte_request_id            UUID,
  input_hash                CHAR(64) NOT NULL,
  output_hash               CHAR(64),
  model_version             VARCHAR(32) NOT NULL,
  prompt_version            VARCHAR(32),
  decision_path             VARCHAR(64) NOT NULL,
  rule_triggered            VARCHAR(32),
  confidence_score          NUMERIC(5,4),
  anomaly_flags             TEXT[],
  execution_duration_ms     INTEGER,
  retention_record_id       UUID,
  audit_reference           UUID NOT NULL
);

-- Row Level Security
ALTER TABLE data_retention_policy_record ENABLE ROW LEVEL SECURITY;
ALTER TABLE data_deletion_directive ENABLE ROW LEVEL SECURITY;
ALTER TABLE data_legal_hold ENABLE ROW LEVEL SECURITY;
ALTER TABLE data_rte_request ENABLE ROW LEVEL SECURITY;

CREATE POLICY tenant_isolation ON data_retention_policy_record
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);

CREATE POLICY tenant_isolation_directive ON data_deletion_directive
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);

-- Append-only enforcement on audit log
CREATE RULE no_update_audit AS ON UPDATE TO data_retention_audit_log DO INSTEAD NOTHING;
CREATE RULE no_delete_audit AS ON DELETE TO data_retention_audit_log DO INSTEAD NOTHING;
```

---

## 1️⃣9️⃣ API CONTRACT

```yaml
POST /api/v1/retention/classify
Authorization: Bearer {JWT with RETENTION_CLASSIFY scope}
Body: INPUT_SCHEMA (request_type = CLASSIFY_ENTITY)
Response 200: OUTPUT_SCHEMA with retention_class and retention_period
Response 400: Validation failure
Response 403: Unauthorized

POST /api/v1/retention/rte/submit
Authorization: Bearer {JWT — user or USER_RIGHTS_AGENT service token}
Body: INPUT_SCHEMA (request_type = PROCESS_RTE_REQUEST)
Response 202: { "rte_request_id": "UUID", "deadline_utc": "ISO-8601", "audit_reference": "UUID" }
Response 400: Validation failure

GET /api/v1/retention/rte/{rte_request_id}/status
Authorization: Bearer {JWT — requesting user or COMPLIANCE_ADMIN}
Response 200: { "status": "...", "scope_summary": {...}, "completed_at_utc": "...", "certificate_id": "..." }

POST /api/v1/retention/legal-hold/apply
Authorization: Bearer {JWT with LEGAL_HOLD_APPLY scope — LEGAL_COUNSEL role only}
Body: INPUT_SCHEMA (request_type = APPLY_LEGAL_HOLD)
Response 200: { "hold_id": "UUID", "audit_reference": "UUID" }

POST /api/v1/retention/legal-hold/{hold_id}/release
Authorization: Bearer {JWT with LEGAL_HOLD_RELEASE scope — LEGAL_COUNSEL role only}
Response 200: { "hold_id": "UUID", "released_at_utc": "ISO-8601", "audit_reference": "UUID" }

GET /api/v1/retention/audit-report
Authorization: Bearer {JWT — COMPLIANCE_ADMIN or PLATFORM_ADMIN}
Query: ?tenant_id=&from_date=&to_date=&format=JSON|PDF
Response 200: RETENTION_AUDIT_REPORT

GET /api/v1/retention/certificate/{certificate_id}/verify
Authorization: PUBLIC — no auth required (public erasure verification)
Response 200: { "valid": true|false, "issued_at_utc": "...", "scope_description": "..." }

POST /api/v1/retention/tenant/close
Authorization: Bearer {PLATFORM_ADMIN + COMPLIANCE_AGENT dual-approval tokens}
Body: INPUT_SCHEMA (request_type = INITIATE_TENANT_CLOSURE)
Response 202: { "closure_workflow_id": "UUID", "estimated_completion_utc": "ISO-8601" }
```

---

## 2️⃣0️⃣ VERSIONING POLICY

| Rule | Enforcement |
|---|---|
| All changes ADD-ONLY | No fields removed — deprecated via `deprecated: true` flag |
| Every change requires version bump | PATCH for fixes, MINOR for new optional fields, MAJOR for retention class changes |
| Retention class changes | Any change to retention periods requires `LEGAL_COUNSEL` sign-off before deployment |
| Backward compatibility | Prior input schema versions valid for 24 months after deprecation |
| Migration documented | Every version bump requires `MIGRATION_NOTE` and `LEGAL_IMPACT_ASSESSMENT` |
| Rollback safe | Every deployment passes rollback smoke test before promotion |
| Model versions immutable | Production audit log `model_version` references cannot be deleted |

**Current Versions:**
```
AGENT_VERSION:         v1.0.0
SCHEMA_VERSION:        v1.0.0
ML_MODEL_VERSION:      v1.0.0
RULE_ENGINE_VERSION:   v1.0.0
PROMPT_VERSION:        v1.0.0
API_CONTRACT_VERSION:  v1.0.0
RETENTION_CLASS_REGISTRY_VERSION: v1.0.0
```

---

## 2️⃣1️⃣ NON-NEGOTIABLE RULES

**This agent MUST NOT:**

| Rule | Description |
|---|---|
| NO deletion under uncertainty | If classification confidence < 0.60, default to maximum retention — NEVER delete |
| NO deletion without legal hold check | LEGAL_HOLD_AGENT must be confirmed reachable before any DELETION_DIRECTIVE is issued |
| NO CLASS_A deletion without dual approval | Two independent signed tokens required — no exceptions, no emergency bypasses |
| NO unsigned deletion directives | Every directive carries HMAC-SHA256 signature — DELETION_EXECUTOR_AGENT rejects unsigned directives |
| NO RTE certificate without complete execution | ERASURE_CERTIFICATE cannot be issued if any eligible entity remains undeleted or unpseudomized |
| NO shadow data left behind | Every deletion directive must trigger shadow purge across cache, search, backup, analytics |
| NO hidden logic | All classification decision paths logged in `decision_path` audit field |
| NO historical audit modification | Audit log is append-only — only PII anonymization of specific fields permitted on RTE |
| NO cross-tenant targeting | Data entities can only be targeted by actors within the same tenant |
| NO AI autonomous retention decisions | LLM output is advisory for summaries only — retention class assignment is ML + Rule Engine |
| NO deletion of RTE records | RTE requests and erasure certificates are CLASS_A PERMANENT — they prove compliance |
| NO retention period shortening without legal review | Any reduction in retention periods requires `LEGAL_COUNSEL` sign-off |
| NO execution outside scope | This agent does not create data, modify business records, or perform any operation other than classification, retention policy, and deletion governance |

---

## 2️⃣2️⃣ ECOSKILLER ANTIGRAVITY — TRUST INFRASTRUCTURE POSITION

This agent is the **legal compliance backbone** of the entire Ecoskiller Antigravity platform. Its integrity governs:

| System | Dependency on This Agent |
|---|---|
| **Legal Compliance** | 100% — platform's GDPR, DPDPA, CCPA compliance depends entirely on this agent |
| **User Trust** | Right to Erasure execution — users can verify their data is deleted via public certificate |
| **Audit Integrity** | Retention policy records feed the immutability guarantee of the entire audit system |
| **ML Governance** | All ML model artifacts and feature stores governed by CLASS_G policy |
| **Financial Systems** | Payment and royalty records protected under CLASS_A and CLASS_B |
| **Attribution Chain** | Idea attribution records permanently preserved under CLASS_A |
| **Tenant Operations** | Tenant closure and lifecycle managed through governed protocol |
| **Storage Optimization** | Systematic deletion of CLASS_D and CLASS_E data controls platform storage costs at scale |
| **Regulatory Reporting** | Retention Audit Reports feed all regulatory filings |
| **Parent Trust Layer** | Child data governed under CLASS_F with parent-authorized RTE rights |

> **Scale Target:** At 10M–100M users, this agent processes tens of millions of entity classifications, thousands of RTE requests, and systematic deletion of billions of behavioral records annually. Every single operation is audited, signed, and legally defensible. Zero tolerance for compliance breach at any scale.

---

## 🔐 FINAL LOCK DECLARATION

```
AGENT: DATA_RETENTION_POLICY_AGENT
VERSION: v1.0.0
STATUS: SEALED · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY: ADD-ONLY via version bump
CREATIVE_INTERPRETATION: FORBIDDEN
ASSUMPTION_FILLING: FORBIDDEN
DEFAULT_BEHAVIOR: DENY
DEFAULT_ON_UNCERTAINTY: MAXIMUM RETENTION — NEVER DELETE UNDER UNCERTAINTY
FAILURE_MODE: STOP_EXECUTION → LOG → ESCALATE
EXECUTION_AUTHORITY: Human Declaration Only

REGULATORY DECLARATIONS:
  GDPR Compliance: Art. 5 (storage limitation), Art. 17 (erasure), Art. 30 (records)
  DPDPA 2023 Compliance: Sec. 8 (accuracy), Sec. 13 (erasure)
  CCPA Compliance: Sec. 1798.105 (deletion rights)
  SOC 2 Type II: CC6.5 (data disposal)
  ISO 27001: A.8.3 (media handling)

This specification is complete and self-contained.
No field may be assumed. No rule may be relaxed.
No deletion may occur under uncertainty.
No RTE may be marked complete before verified execution.
Violation of any rule in this document → STOP EXECUTION → REPORT → NO COMPLETION CLAIM PERMITTED.
```

---

*Generated for: Ecoskiller Antigravity — Enterprise Optimization + Trust Infrastructure*
*Document Classification: Internal — Governed System Specification — Legal-Obligatory*
*Effective From: v1.0.0 Declaration*
*Legal Review Required Before: Any retention period modification, any Class A deletion authorization*
