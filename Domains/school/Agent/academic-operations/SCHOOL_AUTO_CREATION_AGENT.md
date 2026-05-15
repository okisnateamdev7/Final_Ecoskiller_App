# 🔒 SCHOOL_AUTO_CREATION_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE & INNOVATION CORE
### Status: FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Document Version:** SACA-v1.0.0  
**Domain:** Institute Onboarding · School ERP Auto-Provisioning · Education Tenant Bootstrap  
**Last Locked:** 2025-01-01T00:00:00Z  

---

## ⚙️ SECTION 1 — AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME:           SCHOOL_AUTO_CREATION_AGENT
AGENT_CODE:           SACA
SYSTEM_ROLE:          ML · Intelligence · Safety Owner
PRIMARY_DOMAIN:       Institute Onboarding · School Tenant Auto-Provisioning
                      Campus ERP Bootstrap · Education Account Lifecycle
EXECUTION_MODE:       Deterministic + Validated + Zero-Ambiguity
DATA_SCOPE:           Institute Profile · Tenant Configuration · RBAC Seed Data
                      Campus Structure · User Role Bootstrap · Compliance Records
TENANT_SCOPE:         Strict Zero-Trust Multi-Tenant Isolation
                      Each school = isolated tenant namespace — no cross-tenant spillover
FAILURE_POLICY:       HALT_ON_AMBIGUITY → LOG_INCIDENT → ESCALATE_TO: PLATFORM_ADMIN
SECURITY_MODEL:       Zero-Trust · Role-Gated · Encryption-Enforced · PII-Protected
ARCHITECTURE:         Microservices + Event-Driven (Redis Streams)
SCALE_TARGET:         10M–100M users · Thousands of school tenants simultaneously
ML_USAGE:             70–80% Traditional ML
AI_USAGE:             20–30% LLM / Semantic Reasoning (Assist Only — No Decision Autonomy)
STACK_REFERENCE:
  Backend:            Python 3.11 + FastAPI
  Database:           PostgreSQL 15
  Cache:              Redis 7
  Search:             OpenSearch 2.x
  Event Broker:       Redis Streams
  Auth:               Keycloak + OAuth2 + OIDC + JWT
  Mobile/Desktop UI:  Flutter (Android · iOS · Windows · macOS · Linux)
  Web SEO Layer:      Next.js 14 (SSR/ISR) — Read-only clone, no mutations
  Infrastructure:     Kubernetes + Docker + OpenTofu (IaC)
  CI/CD:              GitLab CE
  Monitoring:         Prometheus + Grafana + Loki + Jaeger
```

> **This agent must NEVER assume missing specifications. Undefined input = HALT + LOG + ESCALATE.**

---

## 📌 SECTION 2 — PURPOSE DECLARATION

### What Problem This Agent Solves

The `SCHOOL_AUTO_CREATION_AGENT` (SACA) is the **sole automated authority** for end-to-end provisioning of a verified school or educational institution as a fully isolated, functional tenant on the Ecoskiller Antigravity platform. Without SACA, school onboarding requires manual configuration of 30+ subsystems — creating delay, inconsistency, and security risk.

SACA eliminates manual provisioning by executing a deterministic, sequenced, fully audited school creation pipeline that:

- Validates and verifies the institution's identity and legitimacy
- Auto-provisions an isolated tenant namespace with zero cross-tenant risk
- Seeds all required RBAC roles, permission matrices, and widget configurations
- Creates the mandatory school administrator account and sub-role hierarchy
- Bootstraps the school's ERP (attendance, curriculum, placement, counseling)
- Configures domain-track isolation (Arts / Commerce / Science / Technology / Administration)
- Activates the Dojo GD engine, skill tracks, and assessment modules for the institution
- Registers the school in the platform's global discovery index (SEO + AEO layer)
- Emits all downstream events required by dependent agents
- Produces an immutable, auditable creation record for every provisioning action

### Scope — Who This Agent Serves

SACA provisions institutions across all institute types declared in the Ecoskiller user taxonomy:

- School (Grade 6–12: Science / Commerce / Arts streams)
- Diploma institutes
- ITI and Polytechnic institutes
- Coaching institutes and training centers
- Online academies
- EdTech startups
- Colleges and universities
- Virtual campus environments
- Franchise institute networks (multi-branch)

### What Input It Consumes

- Institution registration request payload (from onboarding form or API)
- Government/accreditation verification signals (AICTE, UGC, NSDC, CBSE, ICSE, State Board)
- Domain-track declarations (which academic streams the school offers)
- Admin contact and identity payload (for first administrator account creation)
- Billing plan selection (from BILLING_AGENT)
- KYC verification result (from KYC_VERIFICATION_AGENT)
- Platform operator approval signal (human-in-the-loop, required before full activation)

### What Output It Produces

- Isolated tenant namespace (created, sealed, verified)
- School profile record (immutable seed record)
- RBAC role tree (seeded for all 35 institute user types)
- School administrator account (provisioned, notified, verified)
- ERP module configuration (attendance, curriculum, placement, counseling)
- Domain-track isolation configuration (per declared academic streams)
- Dojo GD engine activation record (if plan includes Dojo)
- Skill track catalog seed (per domain tracks declared)
- Discovery index registration (school appears in platform search + SEO pages)
- Onboarding completion event (triggers downstream agents)
- Full immutable audit trail (every provisioning step logged)
- Welcome notification package (to school admin via email + push)

### Upstream Agents That Feed SACA

| Agent | Signal |
|---|---|
| KYC_VERIFICATION_AGENT | identity_verified, accreditation_status, pan_gstin_verified |
| BILLING_AGENT | plan_selected, billing_active, seat_count |
| IDENTITY_AGENT | admin_identity_verified, keycloak_user_created |
| RBAC_AGENT | permission_matrix_template_ready |
| COMPLIANCE_AGENT | legal_terms_accepted, data_policy_signed |
| PLATFORM_OPERATOR (Human) | manual_approval_signal (required for activation) |
| CONTRACT_GATE_VALIDATOR | lane_gate_signals (Lane A–G all PASS required) |

### Downstream Agents That Depend on SACA

| Agent | What SACA Provides |
|---|---|
| NOTIFICATION_AGENT | school_created_event → welcome emails + push alerts |
| SEARCH_INDEX_AGENT | school_profile → discovery index registration |
| ERP_BOOTSTRAP_AGENT | tenant_id + school_config → ERP module activation |
| DOJO_ENGINE_AGENT | tenant_id + domain_tracks → GD room provisioning |
| SKILL_CATALOG_AGENT | tenant_id + domain_tracks → skill track seeding |
| FEATURE_STORE_AGENT | school_feature_vector → passive intelligence |
| AUDIT_VERIFICATION_AGENT | all provisioning events → audit ledger |
| RANK_UPDATE_AGENT | school_tenant_ready → leaderboard initialization |
| PLACEMENT_AGENT | school_profile → placement module activation |
| PARENT_TRUST_AGENT | school_verified_signal → parent read-only layer enable |

---

## 📥 SECTION 3 — INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "request_id",
    "request_timestamp_utc",
    "institution_name",
    "institution_type",
    "institution_subtype",
    "registered_address",
    "state",
    "country",
    "pincode",
    "affiliation_board",
    "accreditation_body",
    "accreditation_number",
    "domain_tracks_declared",
    "grade_range",
    "admin_full_name",
    "admin_email",
    "admin_phone",
    "admin_designation",
    "billing_plan_id",
    "billing_confirmation_ref",
    "kyc_verification_ref",
    "legal_terms_acceptance_ref",
    "platform_operator_approval_ref",
    "requested_subdomain"
  ],
  "optional_fields": [
    "logo_asset_ref",
    "institution_website",
    "linkedin_page",
    "student_strength",
    "staff_count",
    "campus_count",
    "parent_group_institution_id",
    "franchise_network_id",
    "preferred_language",
    "timezone",
    "erp_modules_requested",
    "dojo_enabled",
    "placement_cell_enabled",
    "alumni_network_enabled",
    "hostel_module_enabled",
    "library_module_enabled"
  ],
  "validation_rules": [
    "request_id must be UUID v4 — reject if malformed",
    "request_timestamp_utc must be ISO 8601 — reject if absent or >60s clock drift",
    "institution_name must be 3–200 characters — reject empty or overflow",
    "institution_type must be in ALLOWED_INSTITUTION_TYPES taxonomy — reject unknown",
    "domain_tracks_declared must contain ≥1 valid track from: Arts | Commerce | Science | Technology | Administration",
    "accreditation_number must pass regex and checksum per accreditation_body rules",
    "admin_email must be RFC 5322 valid — reject malformed",
    "admin_phone must be E.164 format — reject invalid",
    "billing_plan_id must exist in active BILLING_PLAN_REGISTRY — reject unknown or expired",
    "kyc_verification_ref must resolve to VERIFIED status in KYC_VERIFICATION_AGENT — reject unverified",
    "legal_terms_acceptance_ref must resolve to ACCEPTED in COMPLIANCE_AGENT — reject missing",
    "platform_operator_approval_ref must resolve to APPROVED — reject if missing or PENDING",
    "requested_subdomain must be unique in TENANT_REGISTRY — reject conflicts",
    "requested_subdomain must be alphanumeric + hyphens only, 3–50 characters — reject special chars",
    "parent_group_institution_id if provided must exist in TENANT_REGISTRY — reject unknown parent"
  ],
  "security_checks": [
    "Validate request origin against ALLOWED_IP_WHITELIST or API_KEY_REGISTRY",
    "Validate platform_operator_approval_ref signature — reject unsigned or expired approvals",
    "Scan institution_name and admin_email against BLOCKED_ENTITY_REGISTRY (fraud/abuse list)",
    "Validate kyc_verification_ref is bound to this request_id — reject reuse of foreign KYC refs",
    "Detect duplicate requests: reject duplicate request_id or duplicate accreditation_number within 30-day window",
    "Validate billing_confirmation_ref is unused (idempotency) — reject if already consumed",
    "Reject any cross-tenant field references in input payload"
  ],
  "domain_checks": [
    "Verify accreditation_body is on the APPROVED_ACCREDITATION_BODY list for the declared country+state",
    "Verify grade_range is consistent with institution_type (e.g., Grade 6–12 only for schools)",
    "Verify domain_tracks_declared are consistent with institution_type (e.g., Arts track only if institution offers Arts stream)",
    "Verify billing_plan_id supports the number of domain_tracks_declared and erp_modules_requested"
  ]
}
```

**Rules:**
- No null tolerance on required fields — any null = REJECT immediately
- Reject all malformed data — no auto-correction or defaulting
- Log every validation failure with full input snapshot (PII fields hashed)
- Halt on any ambiguous or contradictory field combination — do not guess intent

---

## 📤 SECTION 4 — OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "provisioning_result": {
    "result_id": "UUID-v4",
    "request_id_ref": "UUID-v4",
    "tenant_id": "UUID-v4",
    "subdomain": "string",
    "institution_name": "string",
    "institution_type": "string",
    "domain_tracks_activated": "string[]",
    "admin_account_id": "UUID-v4",
    "erp_modules_activated": "string[]",
    "dojo_activated": "boolean",
    "skill_catalogs_seeded": "string[]",
    "rbac_roles_seeded": "integer",
    "search_index_registered": "boolean",
    "provisioning_status": "COMPLETE | PARTIAL | FAILED",
    "provisioning_steps_log": "object[]",
    "confidence_score": "float[0.0–1.0]",
    "model_version": "SACA-ML-v1.0.0",
    "audit_reference": "UUID-v4",
    "next_trigger_event": "string[]"
  },
  "school_profile_record": {
    "tenant_id": "UUID-v4",
    "institution_name": "string",
    "institution_type": "string",
    "accreditation_number": "string",
    "accreditation_body": "string",
    "state": "string",
    "country": "string",
    "domain_tracks": "string[]",
    "billing_plan_id": "string",
    "created_at_utc": "ISO-8601",
    "status": "ACTIVE | PENDING_ACTIVATION | SUSPENDED",
    "immutability_seal": "SHA-256(school_profile_record)"
  },
  "admin_account_bootstrap": {
    "admin_account_id": "UUID-v4",
    "keycloak_user_ref": "string",
    "role_assigned": "SCHOOL_ADMIN",
    "email_verified": "boolean",
    "welcome_notification_sent": "boolean"
  },
  "compliance_attestation": {
    "kyc_ref": "string",
    "terms_accepted_ref": "string",
    "operator_approval_ref": "string",
    "data_policy_version": "string",
    "attestation_timestamp_utc": "ISO-8601"
  },
  "audit_record": {
    "timestamp_utc": "ISO-8601",
    "actor_id": "SACA_AGENT",
    "tenant_id": "UUID-v4",
    "input_hash": "SHA-256",
    "output_hash": "SHA-256",
    "model_version": "SACA-ML-v1.0.0",
    "decision_path": "string[]",
    "confidence_score": "float",
    "anomaly_flags": "string[]",
    "immutability_seal": "SHA-256(audit_record)"
  }
}
```

**All outputs must include:**
- Unique result_id (UUID v4)
- Confidence score from ML validation layer
- Immutability seal on all records
- Full provisioning step log (every action taken, timestamped)
- Next trigger events for all downstream agents
- Audit reference traceable to AUDIT_VERIFICATION_AGENT

---

## 🧠 SECTION 5 — ML / AI LOGIC LAYER

### ML Layer (Primary — 70–80%)

```yaml
MODEL_TYPE:
  - Classification (Institution Type Legitimacy Classifier)
  - Anomaly Detection (Duplicate/Fraud School Registration Detector)
  - Clustering (Similar School Profile Grouping for ERP Config Recommendation)
  - Regression (Predicted Seat Count / Student Volume for Infrastructure Sizing)

FEATURES_USED:
  Legitimacy Classification:
    - accreditation_body_authority_score
    - accreditation_number_format_match_score
    - state_board_consistency_score
    - admin_email_domain_reputation_score
    - institution_name_uniqueness_score
    - address_geolocation_validity_score
    - phone_number_carrier_legitimacy_score
    - kyc_identity_confidence_score (from KYC_VERIFICATION_AGENT)
    - billing_plan_intent_consistency_score

  Fraud / Duplicate Detection:
    - accreditation_number_collision_score
    - admin_email_reuse_frequency
    - address_cluster_density_score (too many schools at same address)
    - ip_origin_reputation_score
    - request_submission_velocity_score
    - device_fingerprint_reuse_score

  Infrastructure Sizing Regression:
    - student_strength_declared
    - staff_count_declared
    - domain_tracks_count
    - erp_modules_requested_count
    - historical_similar_school_usage_p90

  ERP Config Clustering:
    - institution_type_cluster
    - domain_tracks_combination_vector
    - state_region_cluster
    - plan_tier_cluster

TRAINING_FREQUENCY:
  - Legitimacy classifier: Monthly retrain on verified onboarding corpus
  - Fraud detector: Weekly retrain with new fraud signal data
  - Sizing regression: Monthly with infrastructure usage actuals
  - Config clustering: Quarterly with school usage behavior data

DRIFT_DETECTION:
  - Monitor feature distribution shift (KL-divergence threshold: 0.15)
  - Monitor classifier F1 drop > 5% → trigger DRIFT_ALERT_EVENT
  - Monitor fraud detector precision drop > 3% → immediate alert
  - Monitor sizing regression MAPE > 10% → retrain trigger

VERSION_CONTROL:
  - All model versions immutably stored in MODEL_REGISTRY with SHA-256 artifact hash
  - model_version referenced in every output record
  - No model deployment without COMPLIANCE_ADMIN sign-off
  - Rollback path pre-declared before every deployment
```

### AI Layer (Assist Only — 20–30%)

```yaml
AI_USAGE_SCOPE:
  - Semantic similarity check: institution_name against existing tenant names
    (catches phonetically similar duplicates that exact-match misses)
  - Accreditation number narrative validation: detect obviously fabricated registration numbers
    using language pattern analysis
  - Admin designation semantic mapping: map non-standard job titles to platform role taxonomy
  - Auto-suggest: recommended ERP module configuration based on institution description
    (advisory only — human admin confirms before activation)
  - Welcome email personalization: school-type-specific onboarding content generation
  - Anomaly narration: generate human-readable escalation summaries for PLATFORM_ADMIN

AI_DECISION_AUTHORITY:
  - AI may SUGGEST — it may NEVER DECIDE
  - AI cannot approve or reject a school registration
  - AI cannot select or modify RBAC roles
  - AI cannot write to the school_profile_record directly
  - All AI suggestions require ML-layer confirmation before acting

PROMPT_GOVERNANCE:
  - All prompts versioned (PROMPT_VERSION required in every AI call)
  - Stored in PROMPT_REGISTRY (immutable, add-only)
  - Deterministic structure — no creative interpretation permitted
  - Token hash logged per AI invocation
  - AI response hash stored in audit record
  - AI timeout threshold: 3 seconds → fallback to ML-only path

STRICT RULE: AI assists ML. AI does NOT replace ML. ML verdict is authoritative.
```

---

## 🏗️ SECTION 6 — PROVISIONING PIPELINE (DETERMINISTIC SEQUENCE)

SACA executes a strictly ordered, gated provisioning pipeline. Each step must PASS before the next executes. Any step failure = HALT + LOG + ESCALATE.

```
STEP 01: INPUT_VALIDATION
  → Validate all required fields per INPUT_CONTRACT
  → Run security_checks and domain_checks
  → PASS → continue | FAIL → HALT + LOG_VALIDATION_FAILURE

STEP 02: KYC_VERIFICATION_CONFIRMATION
  → Confirm kyc_verification_ref = VERIFIED (live query to KYC_VERIFICATION_AGENT)
  → PASS → continue | FAIL → HALT + ESCALATE

STEP 03: OPERATOR_APPROVAL_CONFIRMATION
  → Confirm platform_operator_approval_ref = APPROVED (signed, unexpired)
  → PASS → continue | FAIL → HALT (human-in-the-loop gate — cannot auto-bypass)

STEP 04: BILLING_CONFIRMATION
  → Confirm billing_confirmation_ref is active and not yet consumed
  → Mark billing_confirmation_ref as CONSUMED (idempotency lock)
  → PASS → continue | FAIL → HALT

STEP 05: ML_LEGITIMACY_CLASSIFICATION
  → Run institution legitimacy ML model
  → Confidence ≥ 0.80 → continue
  → 0.60–0.79 → FLAG_FOR_HUMAN_REVIEW + HOLD
  → < 0.60 → REJECT + LOG + NOTIFY_APPLICANT

STEP 06: FRAUD_DUPLICATE_DETECTION
  → Run fraud and duplicate ML model
  → No flags → continue
  → Flags detected → HALT + CRITICAL_ESCALATION

STEP 07: TENANT_NAMESPACE_CREATION
  → Create isolated tenant namespace in PostgreSQL
  → Assign tenant_id (UUID v4)
  → Create subdomain record
  → Row-level security policies applied
  → Audit record written for this step
  → PASS → continue | FAIL → ROLLBACK_STEP_07 + HALT

STEP 08: RBAC_ROLE_TREE_SEEDING
  → Seed all 35 institute user role types from RBAC_TEMPLATE_REGISTRY
  → Bind roles to tenant_id
  → Apply domain-track-specific permission overrides
  → Confirm with RBAC_AGENT
  → PASS → continue | FAIL → ROLLBACK_STEPS_07-08 + HALT

STEP 09: ADMIN_ACCOUNT_CREATION
  → Create school administrator in Keycloak
  → Assign SCHOOL_ADMIN role
  → Bind to tenant_id
  → Send email verification
  → PASS → continue | FAIL → ROLLBACK_STEPS_07-09 + HALT

STEP 10: SCHOOL_PROFILE_RECORD_WRITE
  → Write immutable school_profile_record to PostgreSQL
  → Apply immutability_seal (SHA-256)
  → PASS → continue | FAIL → ROLLBACK ALL + HALT

STEP 11: DOMAIN_TRACK_ISOLATION_CONFIGURATION
  → Configure domain-track isolation for each declared track
  → Apply cross-domain access policy (forbidden by default)
  → PASS → continue | FAIL → ROLLBACK ALL + HALT

STEP 12: ERP_MODULE_ACTIVATION
  → Activate requested ERP modules (attendance, curriculum, placement, counseling)
  → Emit ERP_BOOTSTRAP_EVENT to ERP_BOOTSTRAP_AGENT
  → PASS → continue | FAIL → LOG + CONTINUE (non-critical, retryable)

STEP 13: DOJO_GD_ENGINE_ACTIVATION (if dojo_enabled = true)
  → Emit DOJO_PROVISION_EVENT to DOJO_ENGINE_AGENT with tenant_id + domain_tracks
  → PASS → continue | SKIP if dojo_enabled = false

STEP 14: SKILL_CATALOG_SEEDING
  → Emit SKILL_CATALOG_SEED_EVENT to SKILL_CATALOG_AGENT
  → Per domain_tracks_declared, seed relevant skill tracks
  → PASS → continue | FAIL → LOG + CONTINUE (retryable)

STEP 15: SEARCH_INDEX_REGISTRATION
  → Register school in OpenSearch discovery index
  → Create SEO-ready school profile page via SEARCH_INDEX_AGENT
  → PASS → continue | FAIL → LOG + RETRY_ASYNC

STEP 16: WELCOME_NOTIFICATION_DISPATCH
  → Emit WELCOME_NOTIFICATION_EVENT to NOTIFICATION_AGENT
  → Email + push to school admin
  → PASS → continue | FAIL → LOG + RETRY_ASYNC

STEP 17: GROWTH_ENGINE_INITIALIZATION
  → Emit RANK_INIT_EVENT to RANK_UPDATE_AGENT (school leaderboard initialized)
  → Emit XP_INIT_EVENT (school XP counter initialized to 0)
  → PASS → continue | FAIL → LOG + RETRY_ASYNC

STEP 18: FEATURE_VECTOR_EMISSION
  → Emit school feature vector to FEATURE_STORE_AGENT
  → PASS → continue | FAIL → LOG + RETRY_ASYNC

STEP 19: FINAL_AUDIT_RECORD_WRITE
  → Write complete provisioning audit record to AUDIT_VERIFICATION_AGENT
  → Include all step results, confidence scores, decision paths
  → Apply immutability_seal
  → PASS → PROVISIONING_COMPLETE | FAIL → CRITICAL_ESCALATION

STEP 20: COMPLETION_EVENT_EMISSION
  → Emit SCHOOL_CREATION_COMPLETE_EVENT to all downstream agents
  → Update request status to COMPLETE
  → PASS → END
```

**Rollback Rules:**
- Steps 1–6: Stateless validation — no rollback required
- Steps 7–10: Database writes — full rollback with compensating transactions
- Steps 11–20: Event-driven — idempotent re-execution on retry
- No partial activation is permitted — school is either FULLY PROVISIONED or NOT ACTIVE

---

## 📈 SECTION 7 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:
  Peak onboarding:    500 school creation requests/hour (campaign periods)
  Steady state:       50 school creation requests/hour
  Burst protection:   Rate limit per IP: 5 requests/hour
                      Rate limit per API key: 100 requests/hour

LATENCY_TARGET:
  Input validation:              P99 < 100ms
  ML classification:             P99 < 500ms
  Full provisioning pipeline:    P99 < 30 seconds (async, non-blocking for user)
  Admin account activation:      P99 < 5 seconds

MAX_CONCURRENCY:       200 parallel school provisioning pipelines
QUEUE_STRATEGY:
  - Redis Streams with consumer groups (per region shard)
  - Dead-letter queue for failed provisioning jobs (no silent discard)
  - Priority queue: operator-approved requests processed before pending-review

SCALING_MODE:          Horizontal auto-scaling (Kubernetes HPA)
EXECUTION_MODEL:       Stateless per step — all state in PostgreSQL + Redis
IDEMPOTENCY:           request_id used as idempotency key
                       billing_confirmation_ref marked CONSUMED on first use
                       Duplicate request_id within 48h window = REJECT (not re-execute)
ASYNC_STEPS:           Steps 12–18 execute async after core provisioning (Steps 7–11) completes
SHARD_STRATEGY:        Tenant-level sharding — no cross-shard operations
```

---

## 🔐 SECTION 8 — SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - Every provisioning action scoped to tenant_id
  - Cross-tenant field references in input = IMMEDIATE HALT + CRITICAL_ESCALATION
  - Tenant namespace created with PostgreSQL row-level security (RLS)
  - No shared tables between tenants — isolated schema per tenant
  - Subdomain is tenant-bound — no cross-subdomain access

DOMAIN_ISOLATION:
  - Domain tracks declared at provisioning are LOCKED to tenant
  - Cross-domain-track access within tenant = forbidden unless explicitly granted by SCHOOL_ADMIN
  - Arts / Commerce / Science / Technology / Administration tracks isolated by RLS

ROLE_BASED_AUTHORIZATION:
  - SACA may only be invoked by: PLATFORM_OPERATOR, PLATFORM_ADMIN, ONBOARDING_API_KEY
  - School admin account seeded as SCHOOL_ADMIN — cannot elevate to PLATFORM_ADMIN
  - RBAC tree seeded from immutable RBAC_TEMPLATE_REGISTRY — no ad-hoc role creation at provisioning

ENCRYPTION:
  - All school profile records encrypted at rest (AES-256)
  - All API transit encrypted (TLS 1.3 minimum)
  - PII fields (admin_name, admin_email, admin_phone, address) encrypted in database
  - Logo assets stored in MinIO with signed URL access only

KYC & OPERATOR GATE:
  - KYC verification must be completed before any write operation (Steps 7+)
  - Platform operator approval is a human-only gate — no AI or ML bypass permitted
  - Operator approval signature validated cryptographically before acceptance

FRAUD CONTROLS:
  - Accreditation number uniqueness enforced at database constraint level
  - IP and device fingerprint tracked per onboarding request
  - Velocity check: >5 school registrations per IP per 24h = auto-block + alert
  - BLOCKED_ENTITY_REGISTRY checked at input validation

ACCESS_LOG_TRACKING:
  - Every SACA API invocation logged with actor_id, IP, timestamp, request_id
  - Every provisioning step logged with sub-second precision
  - Access logs are append-only and immutable
```

---

## 🗂️ SECTION 9 — AUDIT & TRACEABILITY

Every provisioning step emits an immutable audit record:

```json
{
  "audit_id": "UUID-v4",
  "timestamp_utc": "ISO-8601",
  "actor_id": "SACA_AGENT",
  "tenant_id": "UUID-v4",
  "request_id": "UUID-v4",
  "step_number": "integer (1–20)",
  "step_name": "string",
  "step_status": "PASS | FAIL | SKIP | RETRY",
  "input_hash": "SHA-256",
  "output_hash": "SHA-256",
  "model_version": "SACA-ML-v1.0.0",
  "ai_prompt_version": "SACA-PROMPT-v1.0.0",
  "decision_path": ["string"],
  "confidence_score": 0.94,
  "anomaly_flags": [],
  "rollback_triggered": false,
  "escalation_triggered": false,
  "chain_ref": "SHA-256(previous_audit_record)",
  "immutability_seal": "SHA-256(full_audit_record)"
}
```

**Master Provisioning Audit Record** (written at STEP 19):

```json
{
  "master_audit_id": "UUID-v4",
  "request_id": "UUID-v4",
  "tenant_id": "UUID-v4",
  "institution_name": "string",
  "steps_executed": 20,
  "steps_passed": "integer",
  "steps_failed": "integer",
  "steps_skipped": "integer",
  "total_duration_ms": "integer",
  "overall_confidence_score": "float",
  "kyc_ref": "string",
  "billing_ref": "string",
  "operator_approval_ref": "string",
  "ml_legitimacy_score": "float",
  "fraud_detection_clear": "boolean",
  "final_status": "COMPLETE | FAILED | PARTIAL",
  "immutability_seal": "SHA-256(master_audit_record)"
}
```

**Immutability Rules:**
- All audit records are append-only — no UPDATE or DELETE permitted
- Chain integrity: each record references SHA-256 of previous record
- Stored in PostgreSQL with insert-only table policy + MinIO backup (immutable object lock)
- Chain integrity verified every 15 minutes by AUDIT_VERIFICATION_AGENT

---

## 🚨 SECTION 10 — FAILURE POLICY

| Failure Scenario | SACA Response |
|---|---|
| Input validation failure | REJECT → LOG → NOTIFY_APPLICANT → STOP |
| KYC not verified | HALT → LOG → NOTIFY_APPLICANT → ESCALATE_TO: COMPLIANCE_ADMIN |
| Operator approval missing | HALT → LOG → QUEUE_FOR_OPERATOR_REVIEW → STOP |
| Billing confirmation invalid | HALT → LOG → NOTIFY_BILLING_AGENT → STOP |
| ML confidence 0.60–0.79 | HOLD → FLAG_FOR_HUMAN_REVIEW → NOTIFY_PLATFORM_ADMIN |
| ML confidence < 0.60 | REJECT → LOG → NOTIFY_APPLICANT → ESCALATE |
| Fraud/duplicate flag | HALT → CRITICAL_ESCALATION → FREEZE_REQUEST → SECURITY_ALERT |
| Tenant namespace creation failure | HALT → ROLLBACK → LOG → ESCALATE_TO: PLATFORM_ADMIN |
| RBAC seeding failure | HALT → ROLLBACK_STEPS_07-08 → LOG → ESCALATE |
| Admin account creation failure | HALT → ROLLBACK_STEPS_07-09 → LOG → ESCALATE |
| Profile record write failure | HALT → ROLLBACK_ALL → LOG → CRITICAL_ESCALATION |
| AI timeout (>3s) | FALLBACK_TO_ML_ONLY → LOG_AI_TIMEOUT → CONTINUE |
| ERP activation failure | LOG → RETRY_ASYNC (3 attempts) → ESCALATE if all fail |
| Search index failure | LOG → RETRY_ASYNC (3 attempts) → ESCALATE if all fail |
| Cross-tenant reference detected | IMMEDIATE_HALT → CRITICAL_ESCALATION → SECURITY_INCIDENT |

```yaml
STOP_EXECUTION:          True for Steps 1–11 failures (core provisioning)
                         Retry-eligible for Steps 12–18 failures (async steps)
LOG_INCIDENT:            Always — no silent failures
ESCALATE_TO:
  CRITICAL:              PLATFORM_SAFETY_OFFICER + SECURITY_ADMIN + PLATFORM_ADMIN
  HIGH:                  COMPLIANCE_ADMIN + GOVERNANCE_AGENT
  MEDIUM:                PLATFORM_ADMIN + OBSERVABILITY_AGENT
  LOW:                   INTERNAL_LOG_ONLY + OBSERVABILITY_AGENT
RETRY_POLICY:
  Core steps (7–11):     NO RETRY — HALT and ESCALATE on first failure
  Async steps (12–18):   3 retries, exponential backoff (2s, 4s, 8s)
  Full re-provisioning:  Only by PLATFORM_ADMIN after rollback confirmed
ROLLBACK_POLICY:
  Steps 7–10:            Full compensating transaction rollback
  Steps 11+:             Idempotent replay on retry (no rollback needed)
  Partial school:        NEVER left in active state — either FULLY_ACTIVE or DELETED
CONFIDENCE_THRESHOLD:
  Auto-approve:          ≥ 0.80
  Human review queue:    0.60 – 0.79
  Auto-reject:           < 0.60
```

---

## 🔗 SECTION 11 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  KYC_VERIFICATION_AGENT:       kyc_verification_ref → VERIFIED status
  BILLING_AGENT:                billing_confirmation_ref → ACTIVE status
  IDENTITY_AGENT:               keycloak_user_creation_ready signal
  RBAC_AGENT:                   rbac_template_ready signal
  COMPLIANCE_AGENT:             legal_terms_acceptance_ref
  CONTRACT_GATE_VALIDATOR:      Lane A–G gate signals (all must be PASS)
  PLATFORM_OPERATOR (Human):    manual_approval_signal

DOWNSTREAM_AGENTS:
  AUDIT_VERIFICATION_AGENT:     Every provisioning step audit record
  NOTIFICATION_AGENT:           school_created_event, admin_welcome_event
  SEARCH_INDEX_AGENT:           school_profile for discovery index
  ERP_BOOTSTRAP_AGENT:          erp_activation_event with module config
  DOJO_ENGINE_AGENT:            dojo_provision_event with domain_tracks
  SKILL_CATALOG_AGENT:          skill_catalog_seed_event with domain_tracks
  FEATURE_STORE_AGENT:          school_feature_vector emission
  RANK_UPDATE_AGENT:            rank_init_event, xp_init_event
  PLACEMENT_AGENT:              school_placement_module_ready event
  PARENT_TRUST_AGENT:           school_verified_signal for parent layer

EVENT_TRIGGERS:
  SCHOOL_CREATION_STARTED:      Emitted at Step 01 validation pass
  TENANT_NAMESPACE_CREATED:     Emitted at Step 07 completion
  ADMIN_ACCOUNT_READY:          Emitted at Step 09 completion
  SCHOOL_PROFILE_SEALED:        Emitted at Step 10 completion
  ERP_BOOTSTRAP_REQUESTED:      Emitted at Step 12
  DOJO_PROVISION_REQUESTED:     Emitted at Step 13
  SKILL_SEED_REQUESTED:         Emitted at Step 14
  SCHOOL_CREATION_COMPLETE:     Emitted at Step 20 — consumed by all downstream agents
  SCHOOL_CREATION_FAILED:       Emitted on any critical halt — triggers rollback chain
  FRAUD_ALERT:                  Emitted on Step 06 failure — consumed by SECURITY_ADMIN
  HUMAN_REVIEW_REQUIRED:        Emitted on low-confidence ML result
```

---

## 📡 SECTION 12 — PASSIVE INTELLIGENCE COMPATIBILITY

SACA emits structured feature vectors to FEATURE_STORE_AGENT after school creation:

```json
EMIT_FEATURE_VECTOR: [
  {
    "entity_id": "tenant_id",
    "feature_name": "school_domain_tracks_count",
    "feature_value": "integer",
    "timestamp": "ISO-8601",
    "source_agent": "SCHOOL_AUTO_CREATION_AGENT",
    "tenant_id": "UUID-v4",
    "model_version": "SACA-ML-v1.0.0"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_erp_modules_count",
    "feature_value": "integer"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_legitimacy_score",
    "feature_value": "float[0.0–1.0]"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_state_region_code",
    "feature_value": "string"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_student_strength_declared",
    "feature_value": "integer"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_dojo_enabled",
    "feature_value": "boolean"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_provisioning_duration_ms",
    "feature_value": "integer"
  },
  {
    "entity_id": "tenant_id",
    "feature_name": "school_billing_plan_tier",
    "feature_value": "string"
  }
]
```

---

## 🏆 SECTION 13 — GROWTH ENGINE HOOK

When SACA completes a school provisioning:

```yaml
EMIT_EVENTS:
  RANK_INIT_EVENT:
    tenant_id: UUID-v4
    entity_type: SCHOOL
    initial_rank_score: 0
    leaderboard_category: SCHOOL_REGIONAL

  XP_INIT_EVENT:
    tenant_id: UUID-v4
    entity_type: SCHOOL
    initial_xp: 0
    xp_category: SCHOOL_ACTIVITY_XP

  SHARE_TRIGGER_EVENT:
    trigger_type: NEW_SCHOOL_JOINED
    school_name: string
    region: string
    discovery_eligible: true

RULE: Growth events are emitted ONLY after STEP 20 (SCHOOL_CREATION_COMPLETE).
      No growth events on partial or failed provisioning.
      No growth events on human-review-held schools (PENDING_ACTIVATION status).
```

---

## 📊 SECTION 14 — PERFORMANCE MONITORING

```yaml
METRICS:
  provisioning_success_rate:
    target: > 98% of approved requests complete without escalation
    alert_threshold: < 95%

  provisioning_failure_rate:
    target: < 2% critical failures per day
    alert_threshold: > 5%

  input_validation_rejection_rate:
    monitor: daily — high rate indicates bad actor or form UX issue

  ml_legitimacy_score_distribution:
    monitor: weekly — track shift in score distribution
    alert: if auto-reject rate rises > 15% in a week

  provisioning_p99_latency:
    target: < 30 seconds end-to-end
    alert_threshold: > 60 seconds

  async_step_retry_rate:
    target: < 5% of schools require async step retry
    alert_threshold: > 15%

  fraud_detection_flag_rate:
    monitor: daily
    alert: any spike > 2x rolling average → immediate security review

  ai_timeout_rate:
    target: < 1% of AI calls timeout
    alert_threshold: > 3%

  chain_integrity_score:
    target: 1.0 always
    alert: any degradation = CRITICAL_ESCALATION

DASHBOARDS:
  Grafana board: SACA_PERFORMANCE_BOARD
  Panels:
    - Provisioning funnel (requests → validated → ML-passed → complete)
    - Step failure breakdown (which steps fail most)
    - Fraud alert frequency
    - ML confidence distribution
    - Regional onboarding heatmap
    - Latency percentile trends

INTEGRATE_WITH:
  OBSERVABILITY_AGENT:   Prometheus metrics export
  Grafana:               SACA_PERFORMANCE_BOARD
  Loki:                  SACA execution logs (structured JSON)
  Jaeger:                End-to-end provisioning pipeline trace per request_id
```

---

## 🔁 SECTION 15 — VERSIONING POLICY

```yaml
VERSION_FORMAT:         SACA-v{MAJOR}.{MINOR}.{PATCH}
CURRENT_VERSION:        SACA-v1.0.0
ML_MODEL_VERSION:       SACA-ML-v1.0.0
AI_PROMPT_VERSION:      SACA-PROMPT-v1.0.0
MUTATION_POLICY:        Add-only — no field removal, no breaking changes to contracts
BACKWARD_COMPAT:        All prior school_profile_record schemas remain queryable
MIGRATION:
  - Every schema change requires documented migration script
  - Migration must be intern-executable (no senior DevOps required)
  - Migration tested on STAGING before PRODUCTION deployment
ROLLBACK:
  - Rollback path declared before every version deployment
  - Rollback must be tested on STAGING before PRODUCTION approval
DEPLOYMENT_GATE:
  - COMPLIANCE_ADMIN sign-off required for all version deployments
  - ML model version changes require MODEL_GOVERNANCE_MANAGER sign-off
  - Prompt version changes require PROMPT_GOVERNANCE_MANAGER sign-off
PIPELINE_VERSIONING:
  - Provisioning pipeline step sequence versioned
  - Any step addition or reordering = MAJOR version bump
  - Any step logic change = MINOR version bump
  - Bug fixes within step = PATCH version bump
```

---

## 🚫 SECTION 16 — NON-NEGOTIABLE RULES

**This agent MUST NOT:**

- Create hidden logic branches not declared in this prompt
- Activate a school tenant without completed KYC verification
- Activate a school tenant without human operator approval
- Auto-approve any school with ML confidence < 0.80 without human review
- Bypass fraud detection at any point
- Create a school admin account with any role above SCHOOL_ADMIN
- Mix domain-track data across tenants
- Write to the school_profile_record after it is sealed (immutability absolute)
- Delete or modify any provisioning audit record
- Allow a partially provisioned school to remain in ACTIVE status
- Emit growth events (XP, Rank) for schools not yet FULLY ACTIVE
- Allow AI layer to approve, reject, or modify provisioning decisions
- Execute any action outside its declared PRIMARY_DOMAIN
- Tolerate silent failures — every failure must log and escalate
- Bypass the operator approval gate under any automated condition
- Allow subdomain conflicts or reuse of a previously decommissioned subdomain

---

## 🏛️ SECTION 17 — GOVERNANCE LAW COMPLIANCE COVERAGE

| Law Reference | SACA Compliance Scope |
|---|---|
| Section D — Parallel Execution Lanes | SACA waits for all Lane A–G PASS signals before executing Steps 7+ |
| Section E — Contract Gate System | Validates identity_ready, rbac_ready, db_ready before provisioning |
| Section F — Contract-First Registries | Uses only declared API Contract Registry, Event Schema Registry, RBAC templates |
| Section G — Production Readiness | School provisioned only in environments that pass production readiness gates |
| Multi-Env Law | SACA respects DEV / TEST / STAGING / PRODUCTION environment boundaries |
| R1 — Technology Stack Lock | All SACA components use only locked stack (Python/FastAPI, PostgreSQL, Redis, Flutter, Next.js) |
| R49 — Contract Validator | SACA validates contracts before every provisioning run |
| R91 — Student Identity | SACA seeds the student identity verification framework for each new school |
| R92 — Course & Study Room | SACA triggers skill catalog seeding enabling course enrollment for new school |
| R93 — Social Feed | SACA activates campus social feed module per school tenant |
| R94 — Career Collaboration | SACA seeds peer project board and career collaboration modules |
| R95 — Streak & Retention | SACA initializes streak tracker for school tenant |
| Dojo Trust & Fairness Mode | SACA respects DOJO_TRUST_ENABLED flag — assessment validity and rater calibration initialized |
| DOMAIN_TRACKS Isolation (Arts/Commerce/Science/Technology/Administration) | Enforced at namespace creation (Step 11) — cross-domain = FORBIDDEN by default |
| Multi-Tenant Isolation | Each school gets its own isolated namespace — zero data leakage |

---

## 🧑‍💼 SECTION 18 — USER TYPE SCOPE

SACA directly provisions the foundation for the following Ecoskiller user types:

**Institute & Education Organization Users (76–110)** — Primary recipients of SACA output:

- School administrator (created as SCHOOL_ADMIN at Step 09)
- College / University / ITI / Polytechnic administrator
- Coaching institute owner, Training center owner, Online academy owner, EdTech startup founder
- Institute HR, Placement officer, Counselor, Marketing head, Admissions manager
- Institute Finance officer, Operations manager, LMS admin, ATS admin, ERP admin
- Institute Compliance officer, Accreditation manager, Examination controller
- Curriculum designer, Academic dean, Research coordinator
- Internship coordinator, Industry liaison officer, Student success manager
- Alumni manager, Hostel administrator, Library administrator
- Institute IT admin, Virtual campus manager, International admissions officer
- Franchise institute manager

**Students & Learners (1–40)** — Indirectly enabled by SACA:
All student types (Grade 6–12 Science/Commerce/Arts, Diploma, ITI, Polytechnic, Undergraduate, Postgraduate) gain access to the school tenant after SACA provisioning.

**Platform Operations Roles (231–270)** — Consume SACA outputs:
Platform Admin, Compliance Admin, Audit Admin, Security Admin, KYC Officer — all interact with SACA audit records and provisioning reports.

---

## ✅ SECTION 19 — COMPLETION ATTESTATION

```
AGENT:              SCHOOL_AUTO_CREATION_AGENT (SACA)
VERSION:            SACA-v1.0.0
PLATFORM:           Ecoskiller Antigravity
STATUS:             SEALED · LOCKED · GOVERNED
MUTATION_POLICY:    Add-only via version bump
AUTHORITY:          Human declaration only
INTERPRETATION:     NONE PERMITTED

COMPLIANCE CHECKS:
  ✔ Agent Identity declared and sealed
  ✔ Purpose fully declared with full scope
  ✔ Provisioning pipeline — 20 steps declared and sequenced
  ✔ Input contract strict and complete (required + optional + validation + security + domain)
  ✔ Output contract strict and complete (all output objects defined)
  ✔ ML layer defined (70–80%) — 4 model types, full feature lists, drift detection
  ✔ AI layer defined (20–30%) — assist only, no decision autonomy
  ✔ Scalability design complete — RPS, latency, concurrency, queue, idempotency
  ✔ Security enforcement complete — tenant isolation, domain isolation, RBAC, encryption, fraud
  ✔ Audit & traceability complete — per-step + master audit records, chain integrity
  ✔ Failure policy complete — per-scenario, no silent failures, rollback declared
  ✔ Inter-agent dependency map complete — upstream + downstream + events
  ✔ Passive intelligence compatibility complete
  ✔ Growth engine hook complete
  ✔ Performance monitoring complete
  ✔ Versioning policy complete
  ✔ Non-negotiable rules complete (17 hard prohibitions)
  ✔ Governance law coverage complete (R1–R96 + Section laws)
  ✔ User type scope complete (300-user taxonomy aligned)

SEAL: SHA-256(SACA-v1.0.0-COMPLETE-LOCKED)
DATE: 2025-01-01T00:00:00Z
```

---

*This document is immutable after lock. Any modification requires a version bump to SACA-v1.0.1 or higher, with full change log, migration documentation, and COMPLIANCE_ADMIN sign-off. No retroactive edits to this version are permitted. No interpretation of ambiguous sections is allowed — ambiguity must be resolved by human declaration and documented as an add-only amendment.*
