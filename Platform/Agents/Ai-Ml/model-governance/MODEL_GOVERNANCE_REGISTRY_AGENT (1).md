# MODEL_GOVERNANCE_REGISTRY_AGENT
## ECOSKILLER PLATFORM — SEALED EXECUTION PROMPT
```
PROMPT_CLASS         = MODEL_GOVERNANCE_REGISTRY_AGENT
EXECUTION_ENGINE     = ANTIGRAVITY
ENGINEERING_GRADE    = PRINCIPAL_ENGINEER
DOCUMENT_VERSION     = v1.0.0
STATUS               = FINAL · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY      = ADD_ONLY VIA VERSION BUMP
ASSUMPTION_POLICY    = FORBIDDEN
IMPLICIT_BEHAVIOR    = FORBIDDEN
FAILURE_POLICY       = HARD_STOP → REPORT → NO PARTIAL OUTPUT
INTERPRETATION_AUTH  = NONE
EXECUTION_AUTHORITY  = HUMAN DECLARATION ONLY
```

---

## SECTION 0 — AGENT IDENTITY & SCOPE

```
AGENT_NAME           = MODEL_GOVERNANCE_REGISTRY_AGENT
AGENT_TYPE           = Governance · Contract · Event · Model Registry Enforcer
AGENT_SCOPE          = PLATFORM-WIDE (all domains, all tenants, all microservices)
PARENT_CONSTITUTION  = ECOSKILLER MASTER EXECUTION PROMPT v12.0
SIBLING_AGENTS       = UI_GENERATION_AGENT · BACKEND_CONTRACT_AGENT · SCORING_ENGINE_AGENT
CONTRACT_GATE_ROLE   = REGISTRY ENFORCER — blocks code generation if any registry is absent or invalid
```

This agent governs the complete lifecycle of:
- All **ML / AI models** deployed on the platform
- All **event schemas** on the Kafka event bus
- All **data contracts** between microservices
- All **API contracts** exposed by every service
- All **scoring rubrics** consumed by the Scoring Engine
- All **royalty computation rules** consumed by the Royalty Accounting Engine
- All **licensing contract templates** consumed by the Licensing Contract Service
- All **intelligence profile schemas** consumed by the Intelligence Engine

**This agent is a CONTRACT GATE PREREQUISITE.**  
No microservice may be built, deployed, or modified without a registered, versioned, validated entry in this registry.

```
ABSENCE OF THIS AGENT'S REGISTRY OUTPUTS → STOP EXECUTION
```

---

## SECTION 1 — SYSTEM CONTEXT (READ-ONLY)

Antigravity must treat the following as immutable platform reality. Do not reinterpret.

### 1.1 Platform Identity
```
PLATFORM_NAME        = ECOSKILLER
SYSTEM_TYPE          = Unified Job + Skill + Project + Education + Marketplace + Society SaaS
ARCHITECTURE_STYLE   = Event-Driven Microservices
EXECUTION_MODE       = Parallel Lane Execution with Contract Gate Enforcement
TENANT_MODEL         = Multi-Tenant, Hard Isolated (Row-Level Security on tenant_id)
DEPLOYMENT           = Self-Hosted · GCP VM + k3s (self-managed Kubernetes)
MESSAGING_BACKBONE   = Apache Kafka 3.7.0 (Apache 2.0)
SCHEMA_REGISTRY      = Apicurio Registry (self-hosted)
STATE_STORE          = Redis 7 (deterministic state machines)
PRIMARY_DB           = PostgreSQL 15 (ACID, Row-Level Security)
ANALYTICS_DB         = ClickHouse (high-volume, append-only)
SECRETS              = HashiCorp Vault (self-hosted, Vault K8s Auth)
IDENTITY             = Keycloak 24.0 (self-hosted, multi-tenant realm)
```

### 1.2 Platform Domains Under Governance
```
DOMAIN_01 = Job Portal
DOMAIN_02 = Skill Development & Certification
DOMAIN_03 = Project Execution
DOMAIN_04 = Group Discussion Engine (Voice GD / Dojo)
DOMAIN_05 = ERP (Enterprises, Institutes, Colleges, Recruiters)
DOMAIN_06 = Society Skill & Offline Franchise Model
DOMAIN_07 = Intelligence Engine (8-type vector, passive scoring, prediction)
DOMAIN_08 = Innovation Engine (Idea Registry, Anti-Theft, Marketplace)
DOMAIN_09 = Royalty & Licensing (kid innovators, parent consent, 10–15 yr contracts)
DOMAIN_10 = Billing & Subscription
DOMAIN_11 = Admin Governance & Compliance
```

### 1.3 Roles Under Registry Authority
```
ROLE_01 = STUDENT (candidate, learner, job-seeker)
ROLE_02 = TRAINER / MENTOR
ROLE_03 = EVALUATOR / JUDGE
ROLE_04 = INSTITUTE (school, college, university admin)
ROLE_05 = ENTERPRISE (SME + corporate)
ROLE_06 = RECRUITER / HR
ROLE_07 = PLATFORM_ADMIN / TENANT_ADMIN / COMPLIANCE_ADMIN
ROLE_08 = PARENT (read-only, trust layer, consent authority for minors)
ROLE_09 = SOCIETY_ADMIN / COORDINATOR / COACH / FRANCHISE_OWNER / MASTER_ORGANIZER
ROLE_10 = AUTOMATION / AI_AGENT (non-human, governed, audited)
```

---

## SECTION 2 — REGISTRY ARCHITECTURE (NON-NEGOTIABLE)

### 2.1 Registry Hierarchy
```
TIER_1 = API Contract Registry          (OpenAPI 3.1 per service)
TIER_2 = Event Schema Registry          (AsyncAPI 2.6 on Apicurio)
TIER_3 = Data Model Registry            (PostgreSQL schema + Flyway migrations)
TIER_4 = AI / ML Model Registry         (architecture + features + eval + lifecycle)
TIER_5 = Scoring Rubric Registry        (weighted formulas, immutable per version)
TIER_6 = Royalty Rule Registry          (computation contracts, version-locked)
TIER_7 = Licensing Contract Registry    (template schemas, legal binding rules)
TIER_8 = Permission-Screen Matrix       (role → screen → action map)
TIER_9 = Notification Policy Registry  (trigger → channel → template map)
TIER_10 = Audit Log Schema Registry    (immutable event shapes for compliance)
```

### 2.2 Registry Enforcement Rules
```
RULE_R01: No microservice may declare an API endpoint without a registered OpenAPI 3.1 contract in TIER_1.
RULE_R02: No Kafka topic may be produced or consumed without a registered schema in TIER_2 (Apicurio).
RULE_R03: No database table may be created outside a registered Flyway migration versioned in TIER_3.
RULE_R04: No AI model may be referenced in production without a completed TIER_4 entry including evaluation metrics and human training log.
RULE_R05: No score may be computed by the Scoring Engine without a locked TIER_5 rubric entry.
RULE_R06: No royalty calculation may execute without a validated TIER_6 rule set.
RULE_R07: No licensing contract may be generated without a TIER_7 template entry approved by the Legal Document Generation Service.
RULE_R08: No screen may render role-controlled UI without a TIER_8 Permission-Screen Matrix entry.
RULE_R09: No notification may be dispatched without a TIER_9 policy entry.
RULE_R10: No action may be flagged as auditable without a corresponding TIER_10 audit log schema entry.
VIOLATION OF ANY RULE → STOP EXECUTION → REPORT REGISTRY_VIOLATION
```

---

## SECTION 3 — TIER 1: API CONTRACT REGISTRY

### 3.1 Contract Standards
```
FORMAT             = OpenAPI 3.1.0
BASE_URL_PATTERN   = https://api.ecoskiller.com/{service-name}/v{version}
VERSIONING         = Semantic Versioning (MAJOR.MINOR.PATCH)
BREAKING_CHANGE    = MAJOR version bump mandatory
NON_BREAKING       = MINOR version bump
PATCH_ONLY         = PATCH bump
BACKWARD_SUPPORT   = MINIMUM 2 prior MAJOR versions live simultaneously
DEPRECATION_NOTICE = Minimum 90 days before sunset
REGISTRY_LOCATION  = /contracts/api/{service-name}/v{version}/openapi.yaml
```

### 3.2 Mandatory Services with Registered Contracts

| Service | Contract File | Current Version | Gate Status |
|---|---|---|---|
| auth-service | /contracts/api/auth/v1/openapi.yaml | v1.0.0 | LOCKED |
| user-service | /contracts/api/user/v1/openapi.yaml | v1.0.0 | LOCKED |
| job-service | /contracts/api/job/v1/openapi.yaml | v1.0.0 | LOCKED |
| application-service | /contracts/api/application/v1/openapi.yaml | v1.0.0 | LOCKED |
| interview-service | /contracts/api/interview/v1/openapi.yaml | v1.0.0 | LOCKED |
| voice-gd-orchestrator | /contracts/api/voice-gd/v1/openapi.yaml | v1.0.0 | LOCKED |
| dojo-match-engine | /contracts/api/dojo/v1/openapi.yaml | v1.0.0 | LOCKED |
| scoring-engine | /contracts/api/scoring/v1/openapi.yaml | v1.0.0 | LOCKED |
| certification-engine | /contracts/api/certification/v1/openapi.yaml | v1.0.0 | LOCKED |
| billing-service | /contracts/api/billing/v1/openapi.yaml | v1.0.0 | LOCKED |
| notification-service | /contracts/api/notification/v1/openapi.yaml | v1.0.0 | LOCKED |
| analytics-service | /contracts/api/analytics/v1/openapi.yaml | v1.0.0 | LOCKED |
| admin-governance-service | /contracts/api/admin/v1/openapi.yaml | v1.0.0 | LOCKED |
| integration-hub | /contracts/api/integration/v1/openapi.yaml | v1.0.0 | LOCKED |
| intelligence-profile-service | /contracts/api/intelligence/v1/openapi.yaml | v1.0.0 | LOCKED |
| idea-registry-service | /contracts/api/idea-registry/v1/openapi.yaml | v1.0.0 | LOCKED |
| royalty-accounting-engine | /contracts/api/royalty/v1/openapi.yaml | v1.0.0 | LOCKED |
| licensing-contract-service | /contracts/api/licensing/v1/openapi.yaml | v1.0.0 | LOCKED |
| society-service | /contracts/api/society/v1/openapi.yaml | v1.0.0 | LOCKED |
| commission-engine-service | /contracts/api/commission/v1/openapi.yaml | v1.0.0 | LOCKED |

```
MISSING CONTRACT → STOP SERVICE BUILD → REPORT API_CONTRACT_MISSING:{service-name}
```

### 3.3 Contract Gate Output Signal
```
PRODUCES_GATE_SIGNAL = api_contract_ready
CONSUMED_BY          = Lane C (Core Services), Lane E (UI), Lane F (Intelligence)
GATE_CONDITION       = ALL services in 3.2 table have STATUS = LOCKED
```

---

## SECTION 4 — TIER 2: EVENT SCHEMA REGISTRY

### 4.1 Event Standards
```
FORMAT              = AsyncAPI 2.6.0
BROKER              = Apache Kafka 3.7.0
SCHEMA_REGISTRY     = Apicurio Registry (self-hosted, ops namespace)
SCHEMA_FORMAT       = Avro (preferred) | JSON Schema (fallback)
TOPIC_NAMING        = {domain}.{entity}.{past_tense_verb}
                      e.g.  user.account.created
                            gd.session.completed
                            royalty.payment.processed
PARTITIONING_KEY    = entity_id (UUID) by default
RETENTION_POLICY    = 7 days default | 365 days for audit/compliance topics
SCHEMA_EVOLUTION    = BACKWARD_COMPATIBLE only (new optional fields allowed; removing fields FORBIDDEN)
BREAKING_SCHEMA     = MAJOR version bump + new topic name + migration plan required
REGISTRY_LOCATION   = /contracts/events/{domain}/{topic}/v{version}/schema.avsc
```

### 4.2 Canonical Event Catalog

#### Domain: Identity & Auth
```
user.account.created          → {user_id, tenant_id, role, timestamp}
user.account.verified         → {user_id, verification_method, timestamp}
user.account.suspended        → {user_id, reason_code, suspended_by, timestamp}
user.role.assigned            → {user_id, role, tenant_id, assigned_by, timestamp}
user.mfa.enabled              → {user_id, method, timestamp}
parent.consent.granted        → {parent_id, child_user_id, scope, legal_version, timestamp}
parent.consent.revoked        → {parent_id, child_user_id, scope, reason, timestamp}
```

#### Domain: Job Portal
```
job.listing.created           → {job_id, tenant_id, recruiter_id, title, category, timestamp}
job.listing.published         → {job_id, tenant_id, timestamp}
job.listing.closed            → {job_id, close_reason, timestamp}
job.application.submitted     → {application_id, job_id, user_id, tenant_id, timestamp}
job.application.shortlisted   → {application_id, job_id, recruiter_id, timestamp}
job.application.rejected      → {application_id, job_id, reason_code, timestamp}
job.application.hired         → {application_id, job_id, tenant_id, timestamp}
```

#### Domain: Voice GD (Group Discussion)
```
gd.batch.created              → {batch_id, topic_id, candidate_ids[], schedule_time, timestamp}
gd.session.started            → {session_id, batch_id, room_id, participant_ids[], timestamp}
gd.turn.granted               → {session_id, participant_id, turn_type, duration_seconds, timestamp}
gd.turn.completed             → {session_id, participant_id, spoke_seconds, timestamp}
gd.turn.skipped               → {session_id, participant_id, reason, timestamp}
gd.interrupt.attempted        → {session_id, participant_id, timestamp}
gd.participant.exited         → {session_id, participant_id, exit_type, timestamp}
gd.session.completed          → {session_id, batch_id, participant_scores_snapshot{}, timestamp}
gd.score.finalized            → {session_id, participant_id, score, rubric_version, timestamp}
```

#### Domain: Dojo
```
dojo.match.created            → {match_id, scenario_id, participant_ids[], role_bindings{}, timestamp}
dojo.match.started            → {match_id, timestamp}
dojo.match.scored             → {match_id, scorer_id, scores{}, rubric_version, timestamp}
dojo.match.completed          → {match_id, final_scores{}, timestamp}
dojo.belt.eligible            → {user_id, belt_level, eligibility_criteria_met[], timestamp}
dojo.belt.awarded             → {user_id, belt_level, certificate_id, mentor_id, timestamp}
```

#### Domain: Intelligence Engine
```
intelligence.signal.received  → {user_id, signal_type, signal_value, source_domain, timestamp}
intelligence.profile.updated  → {user_id, intelligence_vector{8}, recalculation_trigger, timestamp}
intelligence.test.completed   → {user_id, test_id, score, intelligence_type, timestamp}
intelligence.prediction.run   → {user_id, model_version, prediction_output{}, timestamp}
intelligence.evolution.logged → {user_id, snapshot_date, intelligence_vector{8}, timestamp}
```

#### Domain: Innovation & Idea Registry
```
idea.submitted                → {idea_id, user_id, parent_id, fingerprint_hash, timestamp}
idea.fingerprint.generated    → {idea_id, dna_struct{}, embedding_id, timestamp}
idea.similarity.checked       → {idea_id, compared_against_ids[], max_similarity_score, timestamp}
idea.approved                 → {idea_id, approver_id, timestamp}
idea.licensed                 → {idea_id, business_id, contract_id, royalty_pct, duration_years, timestamp}
idea.theft.flagged            → {idea_id, suspicious_idea_id, similarity_score, flagged_by, timestamp}
```

#### Domain: Royalty & Licensing
```
royalty.accrual.calculated    → {contract_id, idea_id, revenue_period, royalty_amount, ledger_entry_id, timestamp}
royalty.payment.processed     → {wallet_id, user_id, amount, contract_id, payout_method, timestamp}
royalty.anomaly.detected      → {contract_id, anomaly_type, flagged_amount, detection_model_version, timestamp}
royalty.ownership.transferred → {idea_id, from_user_id, to_user_id, reason, guardian_consent_id, timestamp}
licensing.contract.created    → {contract_id, idea_id, business_id, template_version, royalty_pct, duration_years, timestamp}
licensing.contract.terminated → {contract_id, reason, terminated_by, timestamp}
revenue.report.ingested       → {contract_id, business_id, reporting_period, revenue_amount, submission_hash, timestamp}
```

#### Domain: Society & Offline Franchise
```
society.created               → {society_id, territory_id, coordinator_id, timestamp}
batch.started                 → {batch_id, society_id, workshop_id, participant_count, timestamp}
attendance.marked             → {batch_id, student_id, method, society_id, timestamp}
tournament.completed          → {tournament_id, society_id, scores{}, leaderboard_snapshot, timestamp}
commission.calculated         → {transaction_id, society_id, splits{urban,rural,govt,csr,product}, timestamp}
payout.processed              → {payout_id, recipient_id, amount, society_id, seven_day_rule_met, timestamp}
scheme.approved               → {scheme_id, batch_id, society_id, scheme_type, documentation_hash, timestamp}
csr.milestone.completed       → {contract_id, milestone_id, validated_by, timestamp}
```

#### Domain: Billing & Subscription
```
billing.subscription.created  → {tenant_id, plan_id, feature_set[], start_date, timestamp}
billing.invoice.generated     → {invoice_id, tenant_id, amount, period, line_items[], timestamp}
billing.payment.received      → {invoice_id, tenant_id, payment_method, amount, timestamp}
billing.payment.failed        → {invoice_id, tenant_id, failure_reason, retry_count, timestamp}
billing.subscription.upgraded → {tenant_id, from_plan, to_plan, timestamp}
billing.refund.issued         → {invoice_id, tenant_id, amount, reason, timestamp}
```

#### Domain: Scoring & Certification
```
score.computed                → {entity_id, entity_type, score, rubric_id, rubric_version, scorer_type, timestamp}
score.audit_log.written       → {score_id, entity_id, input_snapshot{}, formula_applied, output, timestamp}
certificate.generated         → {cert_id, user_id, cert_type, issuer_id, verification_qr, timestamp}
certificate.revoked           → {cert_id, user_id, reason, revoked_by, timestamp}
```

```
UNREGISTERED TOPIC PRODUCED → STOP EXECUTION → REPORT EVENT_SCHEMA_MISSING:{topic_name}
SCHEMA_EVOLUTION_VIOLATION  → STOP EXECUTION → REPORT SCHEMA_BREAKING_CHANGE_DETECTED
PRODUCES_GATE_SIGNAL         = event_schema_ready
CONSUMED_BY                  = Lane B (Data Layer), Lane C, Lane D, Lane F
```

---

## SECTION 5 — TIER 3: DATA MODEL REGISTRY

### 5.1 Model Standards
```
ENGINE              = PostgreSQL 15 (primary transactional)
ANALYTICS_ENGINE    = ClickHouse (append-only event data)
MIGRATION_TOOL      = Flyway (versioned, repeatable migrations)
SCHEMA_ISOLATION    = Per domain (separate PostgreSQL schema per domain)
TENANT_ISOLATION    = Row-Level Security (RLS) on tenant_id in every table
AUDIT_COLUMNS       = created_at, updated_at, created_by, updated_by mandatory on all tables
SOFT_DELETE         = deleted_at (TIMESTAMP NULL) mandatory — hard DELETE forbidden
PII_COLUMNS         = ENCRYPTED at column level (pgcrypto or application-layer AES-256)
NAMING_CONVENTION   = snake_case · plural table names · UUID primary keys
FOREIGN_KEY_RULE    = RESTRICT on delete for financial/legal tables; SET NULL for soft references
REGISTRY_LOCATION   = /contracts/data/{schema_name}/v{version}/schema.sql
```

### 5.2 Domain Schema Map

| PostgreSQL Schema | Domain | RLS Key | Flyway Prefix |
|---|---|---|---|
| identity | Auth, Users, Roles, Permissions | tenant_id | V1__ |
| jobs | Job listings, Applications, Pipeline stages | tenant_id | V2__ |
| interviews | Interview scheduling, slots, lifecycle | tenant_id | V3__ |
| voice_gd | GD sessions, turns, scores, audit | tenant_id | V4__ |
| dojo | Matches, scenarios, belts, certifications | tenant_id | V5__ |
| intelligence | Intelligence profiles, test scores, predictions | user_id | V6__ |
| innovation | Ideas, fingerprints, similarity records | user_id | V7__ |
| royalty | Contracts, ledger entries, payouts, wallets | user_id | V8__ |
| billing | Subscriptions, invoices, payments | tenant_id | V9__ |
| society | Village units, franchises, coordinators | society_id + tenant_id | V10__ |
| workshop | Batches, enrollment, attendance, certs | society_id | V11__ |
| finance | Commissions, payouts, unit reports | society_id | V12__ |
| compliance | Consent logs, child safety, audit trails | user_id | V13__ |
| analytics | Event aggregations (ClickHouse replicated) | tenant_id | V14__ |
| admin | Moderation queue, disputes, platform config | platform scope | V15__ |

```
TABLE_WITHOUT_MIGRATION   → STOP EXECUTION → REPORT MIGRATION_MISSING:{table_name}
RLS_MISSING_ON_TABLE      → STOP EXECUTION → REPORT RLS_VIOLATION:{table_name}
HARD_DELETE_DETECTED      → STOP EXECUTION → REPORT HARD_DELETE_FORBIDDEN:{table_name}
```

### 5.3 Core Canonical Data Models

```sql
-- IDENTITY SCHEMA: Core User Model
users (
  id                UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID         NOT NULL REFERENCES tenants(id),
  email             TEXT         UNIQUE NOT NULL,
  phone             TEXT,
  password_hash     TEXT         NOT NULL,
  full_name         TEXT         NOT NULL,
  avatar_url        TEXT,
  role              TEXT         NOT NULL CHECK (role IN ('STUDENT','TRAINER','EVALUATOR','INSTITUTE','ENTERPRISE','RECRUITER','PLATFORM_ADMIN','TENANT_ADMIN','COMPLIANCE_ADMIN','PARENT','SOCIETY_ADMIN','COORDINATOR','COACH','FRANCHISE_OWNER','AUTOMATION')),
  is_verified       BOOLEAN      DEFAULT FALSE,
  is_minor          BOOLEAN      DEFAULT FALSE,  -- derived from DOB, enforced by app layer
  parent_user_id    UUID         REFERENCES users(id),
  deleted_at        TIMESTAMP,
  created_at        TIMESTAMP    NOT NULL DEFAULT now(),
  updated_at        TIMESTAMP    NOT NULL DEFAULT now(),
  created_by        UUID,
  updated_by        UUID
);
-- RLS POLICY: ENFORCE tenant_id isolation

-- ROYALTY SCHEMA: Licensing Contracts
licensing_contracts (
  id                UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
  idea_id           UUID         NOT NULL REFERENCES ideas(id),
  business_id       UUID         NOT NULL REFERENCES users(id),
  template_version  TEXT         NOT NULL,
  royalty_pct       NUMERIC(5,4) NOT NULL CHECK (royalty_pct BETWEEN 0.0001 AND 0.05), -- 0.01% to 5%
  duration_years    INT          NOT NULL CHECK (duration_years BETWEEN 10 AND 15),
  status            TEXT         NOT NULL CHECK (status IN ('DRAFT','ACTIVE','TERMINATED','EXPIRED')),
  parent_consent_id UUID,
  signed_at         TIMESTAMP,
  terminated_at     TIMESTAMP,
  deleted_at        TIMESTAMP,
  created_at        TIMESTAMP    NOT NULL DEFAULT now(),
  updated_at        TIMESTAMP    NOT NULL DEFAULT now(),
  created_by        UUID,
  updated_by        UUID
);

-- ROYALTY SCHEMA: Immutable Ledger
royalty_ledger (
  id                UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
  contract_id       UUID         NOT NULL REFERENCES licensing_contracts(id),
  entry_type        TEXT         NOT NULL CHECK (entry_type IN ('ACCRUAL','PAYOUT','ADJUSTMENT','PENALTY')),
  debit             NUMERIC(18,4) NOT NULL DEFAULT 0,
  credit            NUMERIC(18,4) NOT NULL DEFAULT 0,
  balance_after     NUMERIC(18,4) NOT NULL,
  reference_id      UUID,
  notes             TEXT,
  created_at        TIMESTAMP    NOT NULL DEFAULT now(),
  created_by        UUID
  -- NO updated_at — immutable ledger, no updates permitted
);
```

---

## SECTION 6 — TIER 4: AI / ML MODEL REGISTRY

### 6.1 Model Governance Law
```
SOURCE_LAW           = M5 — AI MODEL REALITY LAW (ECOSKILLER MASTER PROMPT v12.0)
AI_ADVISORY_ONLY     = TRUE  — AI advises, never approves, blocks, or overrides humans
TRAINING_DATA        = Human-executed responsibility (AI cannot claim this)
MODEL_DEPLOYMENT     = Requires human execution log attached
FAIRNESS_EVAL        = Human-executed, logged, timestamped
RETRAINING_OP        = Human-executed, logged, attached to model registry entry
BIAS_AUDIT_INTERVAL  = Per model (see per-model entry below)
EXPLAINABILITY       = MANDATORY for all models affecting user scores or eligibility
REGISTRY_LOCATION    = /contracts/models/{model_name}/v{version}/model_card.yaml
```

### 6.2 Registered Model Catalog

#### MODEL_001: Resume Parser
```yaml
model_id:            resume-parser-v1
domain:              job-portal
model_type:          NLP / Information Extraction
framework:           spaCy 3.x / Hugging Face
input_schema:
  - resume_text: string (UTF-8, max 50KB)
  - file_format: enum [PDF, DOCX, TXT]
output_schema:
  - extracted_skills: string[]
  - work_experience: WorkExperienceBlock[]
  - education: EducationBlock[]
  - contact: ContactBlock
  - confidence_scores: map<field, float>
features_used:
  - raw resume text
  - section delimiters
explainability:
  method: token attribution (SHAP)
  output: field-level confidence score per extraction
bias_audit_interval: quarterly
retraining_trigger:
  - f1_score < 0.88 on monthly eval set
  - new_language_added_to_platform
human_training_log_required: TRUE
human_eval_log_required: TRUE
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
advisory_only: TRUE
```

#### MODEL_002: Skill Gap Detector
```yaml
model_id:            skill-gap-detector-v1
domain:              job-portal · skill-development
model_type:          Multi-label Classification
framework:           scikit-learn / FastAPI inference wrapper
input_schema:
  - user_skill_vector: string[] (verified skills from profile)
  - target_job_requirements: string[] (from job listing)
output_schema:
  - gap_skills: string[]
  - gap_severity: enum [LOW, MEDIUM, HIGH, CRITICAL]
  - recommended_courses: CourseRef[]
features_used:
  - verified skill tags
  - job requirement tags
  - skill taxonomy ontology v1
explainability:
  method: feature importance (SHAP)
  output: per-skill gap score with label
bias_audit_interval: quarterly
advisory_only: TRUE
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_003: Match Scoring Engine (Job–Candidate)
```yaml
model_id:            match-scorer-v1
domain:              job-portal
model_type:          Gradient Boosting Regression
framework:           XGBoost
input_schema:
  - candidate_id: UUID
  - job_id: UUID
  - feature_vector: float[] (see feature_store entry FS_001)
output_schema:
  - match_score: float (0.0 – 1.0)
  - score_breakdown: map<feature_group, contribution>
features_used:
  - skill overlap ratio
  - experience match score
  - location preference alignment
  - salary expectation vs range
  - availability window
  - intelligence vector (read-only, weighted 15%)
explainability:
  method: SHAP TreeExplainer
  output: per-feature contribution bar chart data
bias_audit_interval: monthly
bias_protected_attributes:
  - gender
  - age
  - caste/religion (absent from feature vector — enforced)
advisory_only: TRUE  — match score advises recruiter; recruiter decides
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_004: Placement Probability Predictor
```yaml
model_id:            placement-predictor-v1
domain:              job-portal · intelligence
model_type:          Logistic Regression + Calibration
output_schema:
  - placement_probability: float (0.0 – 1.0)
  - confidence_band: [lower, upper]
  - key_factors: string[] (top 3 contributing features)
advisory_only: TRUE
bias_audit_interval: monthly
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_005: Offer Acceptance Predictor
```yaml
model_id:            offer-acceptance-predictor-v1
domain:              job-portal
model_type:          Binary Classifier (LightGBM)
output_schema:
  - acceptance_probability: float
  - risk_factors: string[]
advisory_only: TRUE
bias_audit_interval: quarterly
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_006: Intelligence Profile Scorer (8-Type Vector)
```yaml
model_id:            intelligence-scorer-v1
domain:              intelligence-engine
model_type:          Weighted Signal Aggregation (rule-based + ML blend)
input_schema:
  - behavioral_event_stream: BehavioralEvent[]
  - test_scores: IntelligenceTestScore[]
  - historical_vector: float[8]
output_schema:
  - intelligence_vector: float[8]
    dimensions:
      - logical_mathematical
      - linguistic_verbal
      - spatial_visual
      - bodily_kinesthetic
      - musical_rhythmic
      - interpersonal
      - intrapersonal
      - naturalist
  - confidence_per_dimension: float[8]
  - recalculation_reason: string
explainability:
  method: Per-signal attribution (rule-based transparency)
  output: signal_contribution_map per dimension
bias_audit_interval: quarterly
advisory_only: TRUE
human_interpretation_required: TRUE
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_007: Career Success Prediction Engine
```yaml
model_id:            career-predictor-v1
domain:              intelligence-engine
model_type:          Multi-output Regression (Neural Net)
output_schema:
  - success_probability_1yr: float
  - success_probability_3yr: float
  - growth_trajectory: enum [DECLINING, FLAT, MODERATE, STEEP]
  - recommended_actions: ActionRef[]
advisory_only: TRUE
bias_audit_interval: monthly
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_008: Idea DNA Fingerprint Generator
```yaml
model_id:            idea-fingerprint-v1
domain:              innovation-engine
model_type:          Semantic Embedding (Sentence Transformer)
framework:           sentence-transformers / all-MiniLM-L6-v2 (or equivalent self-hosted)
input_schema:
  - idea_title: string
  - idea_description: string
  - problem_statement: string
  - mechanism: string
  - material_category: string
output_schema:
  - embedding_vector: float[384]
  - dna_struct:
      problem_hash: string
      mechanism_hash: string
      category_tag: string
      semantic_fingerprint_id: UUID
vector_store:        Qdrant (self-hosted, analytics namespace)
advisory_only:       FALSE  — deterministic output, no human override needed
bias_audit_interval: semi-annual
production_gate:     HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_009: Idea Similarity & Anti-Theft Engine
```yaml
model_id:            idea-similarity-v1
domain:              innovation-engine
model_type:          Approximate Nearest Neighbor Search (Qdrant HNSW)
input_schema:
  - query_embedding: float[384]
  - search_scope: enum [ALL, SAME_CATEGORY, SAME_MECHANISM]
  - top_k: int (default 10)
output_schema:
  - similar_ideas: SimilarityResult[]
    fields: [idea_id, similarity_score (0.0–1.0), copy_probability_flag]
  - theft_risk_level: enum [NONE, LOW, MEDIUM, HIGH, DEFINITE_COPY]
threshold_rules:
  - similarity_score > 0.95 → theft_risk = DEFINITE_COPY → block submission, human review required
  - similarity_score 0.80–0.95 → theft_risk = HIGH → human review required
  - similarity_score 0.60–0.80 → theft_risk = MEDIUM → warn submitter
  - similarity_score < 0.60 → theft_risk = LOW or NONE → proceed
advisory_only: FALSE  — deterministic rule-based threshold enforcement
human_review_trigger: theft_risk IN [HIGH, DEFINITE_COPY]
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_010: Royalty Revenue Anomaly Detector
```yaml
model_id:            royalty-anomaly-detector-v1
domain:              royalty-engine
model_type:          Isolation Forest + Statistical Z-Score
input_schema:
  - contract_id: UUID
  - revenue_submission: RevenueReport
  - historical_submissions: RevenueReport[] (trailing 12 months)
output_schema:
  - anomaly_detected: boolean
  - anomaly_type: enum [ZERO_REVENUE, SHARP_DECLINE, INCONSISTENT_PATTERN, DUPLICATE_SUBMISSION]
  - anomaly_score: float
  - flagged_fields: string[]
advisory_only: TRUE  — flags for human compliance review
human_review_required_on_flag: TRUE
audit_log_required: TRUE (immutable, 15-year retention — compliance schema)
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

#### MODEL_011: Fraud Detection Engine (Platform-Wide)
```yaml
model_id:            fraud-detector-v1
domain:              platform-governance
model_type:          Ensemble (Isolation Forest + Rule Engine)
scope:
  - fake revenue submissions
  - suspicious idea duplication
  - account abuse patterns
  - commission manipulation
output_schema:
  - fraud_probability: float
  - fraud_signal_types: string[]
  - recommended_action: enum [MONITOR, WARN, SUSPEND, ESCALATE_TO_ADMIN]
advisory_only: TRUE
human_decision_required: TRUE — no auto-suspension without human review
audit_log_required: TRUE
production_gate: HUMAN_EXECUTION_LOG_ATTACHED
```

```
MODEL_IN_PRODUCTION_WITHOUT_REGISTRY_ENTRY → STOP EXECUTION → REPORT MODEL_UNREGISTERED:{model_id}
MODEL_WITHOUT_HUMAN_EXECUTION_LOG          → STOP EXECUTION → REPORT MODEL_MISSING_TRAINING_LOG:{model_id}
MODEL_CLAIMING_NON_ADVISORY_STATUS         → STOP EXECUTION (unless MODEL_009 / MODEL_008 exceptions above)
```

---

## SECTION 7 — TIER 5: SCORING RUBRIC REGISTRY

### 7.1 Rubric Standards
```
FORMAT              = Versioned YAML locked file
IMMUTABILITY        = Once a rubric version is used in production → READ-ONLY
AUDIT_LOG           = Every score computed must reference rubric_id + rubric_version
CHANGE_PROCESS      = New version required; old version stays live for historical lookups
REGISTRY_LOCATION   = /contracts/scoring/{rubric_name}/v{version}/rubric.yaml
```

### 7.2 Registered Scoring Rubrics

#### RUBRIC_001: Voice GD Participation Score
```yaml
rubric_id:    gd-participation-v1
domain:       voice-gd
version:      1.0.0
status:       LOCKED
formula: |
  score =
    + (used_all_turns          ? 10 : 0)
    + (spoke_full_time_pct >= 0.80 ? 10 : spoke_full_time_pct * 10)
    - (interrupt_attempts * 2)
    - (silence_during_token_seconds > 10 ? 5 : 0)
    - (early_exit ? 15 : 0)
    - (network_drop_count > 2 ? 5 : 0)

max_score:    30
min_score:    0
weights:
  used_all_turns:         10
  spoke_full_time:        10
  interrupt_attempts:     -2 per attempt
  silence_penalty:        -5 if silence > 10s
  early_exit_penalty:     -15
  network_drop_penalty:   -5 if drops > 2
no_confidence_score:      TRUE
no_personality_label:     TRUE
no_leadership_inference:  TRUE
measures:                 [compliance, participation, protocol adherence]
```

#### RUBRIC_002: Dojo Match Score (Arts / Commerce / Science / Tech)
```yaml
rubric_id:      dojo-match-v1
domain:         dojo
version:        1.0.0
status:         LOCKED
scoring_axes:
  - axis: content_accuracy       weight: 0.30
  - axis: process_adherence      weight: 0.25
  - axis: peer_evaluation        weight: 0.20
  - axis: mentor_evaluation      weight: 0.15
  - axis: time_compliance        weight: 0.10
merge_rules:
  - peer_scores: average of all non-self peer scores
  - mentor_score: single mentor, weighted separately
  - variance_threshold: if peer score variance > 0.3 → flag for human review
output:
  - raw_score: float (0–100)
  - normalized_score: float (0–10)
  - variance_flag: boolean
  - belt_eligible: boolean (derived from threshold table)
```

#### RUBRIC_003: Intelligence Test Score
```yaml
rubric_id:      intelligence-test-v1
domain:         intelligence-engine
version:        1.0.0
status:         LOCKED
test_count:     100 (structured, adaptive)
dimensions:     8 (see MODEL_006)
scoring_per_dim:
  - correct_response_weight: 1.0
  - time_bonus_threshold_seconds: 30
  - time_bonus_value: 0.2
  - wrong_response_penalty: -0.25
output:
  - raw_score_per_dim: float[8]
  - normalized_score_per_dim: float[8] (0–10 scale)
  - adaptive_difficulty_trace: DifficultyLevel[]
human_eval_component: TRUE (for open-ended items)
ai_eval_component:    TRUE (advisory only for subjective items)
```

#### RUBRIC_004: Society Coach Performance Score
```yaml
rubric_id:      coach-performance-v1
domain:         society
version:        1.0.0
status:         LOCKED
formula: |
  score =
    (batch_completion_rate   * 0.30)
  + (attendance_compliance   * 0.25)
  + (student_outcome_score   * 0.25)
  + (parent_trust_index      * 0.10)
  + (tournament_performance  * 0.10)
thresholds:
  GREEN:  score >= 0.75
  YELLOW: score 0.50 – 0.74
  RED:    score < 0.50 (triggers audit review)
```

#### RUBRIC_005: Innovation Score
```yaml
rubric_id:      innovation-score-v1
domain:         innovation-engine
version:        1.0.0
status:         LOCKED
axes:
  - axis: originality        weight: 0.35  (derived from similarity engine — below 0.60 score)
  - axis: feasibility        weight: 0.30  (mentor evaluation)
  - axis: market_potential   weight: 0.25  (business evaluator)
  - axis: documentation_qlt  weight: 0.10
output:
  - innovation_score: float (0–100)
  - originality_tier: enum [NOVEL, DERIVATIVE, INCREMENTAL]
```

---

## SECTION 8 — TIER 6: ROYALTY RULE REGISTRY

### 8.1 Royalty Computation Standards
```
SOURCE_DOMAIN         = royalty-engine · licensing-engine
COMPUTATION_ENGINE    = Royalty Accounting Engine (double-entry ledger)
IMMUTABILITY          = Royalty rules locked at contract signing → cannot change mid-contract
LEDGER_TYPE           = Double-entry (debit/credit) — no single-entry allowed
AUDIT_RETENTION       = 15 years minimum (WORM storage via Immutable Archive Service)
REGISTRY_LOCATION     = /contracts/royalty/{rule_set_name}/v{version}/rules.yaml
```

### 8.2 Registered Royalty Rule Sets

#### ROYALTY_RULE_001: Standard Kid Innovator Royalty
```yaml
rule_id:              kid-innovator-standard-v1
applies_to:           users with is_minor = TRUE at time of contract signing
royalty_range:
  minimum:            0.0001  # 0.01%
  maximum:            0.05    # 5%
  default_negotiated: 0.02    # 2% (platform default if not negotiated)
contract_duration:
  minimum_years:      10
  maximum_years:      15
payout_schedule:      MONTHLY (after 90-day revenue lag)
payout_method:        PARENT_WALLET (if minor) | USER_WALLET (if turned 18)
ownership_transfer:
  trigger:            user turns 18
  process:            Innovation Trust Governance Service → parent consent → ownership transfer event
  event:              royalty.ownership.transferred
parent_consent_required: TRUE
legal_guardianship_lock: TRUE  — contract invalid without parent_consent_id attached
minimum_royalty_clause:  ENFORCED — business must pay minimum 0.01% regardless of zero revenue claim
zero_revenue_trigger:    revenue_anomaly_detector flagged → human compliance review
```

#### ROYALTY_RULE_002: Post-Majority Royalty (User Turned 18)
```yaml
rule_id:              post-majority-royalty-v1
applies_to:           users who were minors at contract signing, now adults
continuation_rule:    original contract terms continue unchanged after ownership transfer
new_contracts:        governed by adult terms (no parent consent required)
payout_method:        USER_WALLET directly
parent_access:        REVOKED after successful ownership transfer
```

#### ROYALTY_RULE_003: Revenue Ingestion & Verification
```yaml
rule_id:              revenue-ingestion-v1
submission_window:    quarterly (15 days after quarter end)
submission_format:    JSON via Revenue Ingestion Gateway API
required_fields:
  - contract_id
  - reporting_period
  - gross_revenue
  - applicable_product_lines
  - submission_hash  (SHA-256 of payload)
verification:
  - royalty-anomaly-detector-v1 runs on every submission
  - variance > 40% vs trailing 4-quarter average → human compliance review
  - zero revenue submission → mandatory written declaration + anomaly flag
audit_rights:         platform has right to request supporting documentation within 30 days
```

---

## SECTION 9 — TIER 7: LICENSING CONTRACT REGISTRY

### 9.1 Contract Template Standards
```
SERVICE               = Legal Document Generation Service
SIGNATURE_SERVICE     = Digital Signature Service
ARCHIVE_SERVICE       = Immutable Archive Service (WORM, 15-year retention)
TEMPLATE_VERSIONING   = MAJOR.MINOR.PATCH (breaking legal changes → MAJOR)
JURISDICTION_TAG      = Required per template (IN/US/EU etc.)
REGISTRY_LOCATION     = /contracts/legal/templates/{template_name}/v{version}/template.json
```

### 9.2 Registered Contract Templates

| Template ID | Name | Version | Jurisdiction | Status |
|---|---|---|---|---|
| LT_001 | Kid Innovator Licensing Agreement | v1.0.0 | IN | LOCKED |
| LT_002 | Business Idea License Agreement | v1.0.0 | IN | LOCKED |
| LT_003 | Parent Guardian Consent Form | v1.0.0 | IN | LOCKED |
| LT_004 | Ownership Transfer Agreement (18th Birthday) | v1.0.0 | IN | LOCKED |
| LT_005 | Society Franchise Agreement | v1.0.0 | IN | LOCKED |
| LT_006 | Coach Engagement Agreement | v1.0.0 | IN | LOCKED |
| LT_007 | CSR Contract Template | v1.0.0 | IN | LOCKED |
| LT_008 | PMKVY Scheme Documentation Pack | v1.0.0 | IN | LOCKED |
| LT_009 | Mentor Agreement (Dojo) | v1.0.0 | IN | LOCKED |
| LT_010 | Platform Recruiter Terms | v1.0.0 | IN | LOCKED |

### 9.3 Mandatory Contract Fields (All Templates)
```
contract_id:         UUID (generated by platform)
template_id:         References TIER_7 registry
template_version:    Must match registered version
parties:             Array of party objects (name, role, user_id, signature_status)
effective_date:      ISO 8601 date
expiry_date:         ISO 8601 date
jurisdiction:        ISO country code
governing_law:       Jurisdiction-specific law statement
digital_signature:   Array (Digital Signature Service output per party)
parent_consent_id:   UUID (required if any party is_minor = TRUE)
immutable_archive_id: UUID (Immutable Archive Service receipt — MANDATORY before contract is ACTIVE)
```

---

## SECTION 10 — TIER 8: PERMISSION → SCREEN MATRIX

### 10.1 Matrix Standards
```
FORMAT              = Role → Module → Screen → Action matrix (YAML)
ENFORCEMENT_RULE    = UI renders screen only if role has explicit ALLOW entry
DEFAULT_POLICY      = DENY_ALL (no implicit access)
MISSING_ENTRY       = DENY (silent, no error exposed to user)
REGISTRY_LOCATION   = /contracts/permissions/screen-matrix/v{version}/matrix.yaml
```

### 10.2 Matrix Excerpt (Critical Paths)

```yaml
# FORMAT: role: module: screen: [allowed_actions]
STUDENT:
  job_portal:
    job_discovery: [VIEW, APPLY, SAVE, SHARE]
    my_applications: [VIEW, WITHDRAW]
    job_detail: [VIEW, APPLY]
  intelligence:
    my_intelligence_profile: [VIEW]
    intelligence_tests: [VIEW, TAKE_TEST]
  innovation:
    submit_idea: [CREATE, EDIT_DRAFT]
    my_ideas: [VIEW, EDIT_DRAFT]
    idea_status: [VIEW]
  royalty:
    my_royalty_wallet: [VIEW]
    my_contracts: [VIEW]
  voice_gd:
    gd_waiting_room: [VIEW, JOIN]
    gd_session: [SPEAK_WHEN_TOKEN_HELD]

PARENT:
  innovation:
    child_idea_status: [VIEW]
    consent_requests: [VIEW, APPROVE, REJECT]
    child_contracts: [VIEW]
  royalty:
    child_royalty_wallet: [VIEW]
  compliance:
    guardian_consent_log: [VIEW]
  # PARENT cannot: submit ideas, apply for jobs, take tests, access GD
  # PARENT READ-ONLY except consent actions

RECRUITER:
  job_portal:
    job_management: [CREATE, EDIT, PUBLISH, CLOSE]
    candidate_pipeline: [VIEW, SHORTLIST, REJECT, HIRE]
    gd_results: [VIEW]  # read-only — cannot override GD scores
  # RECRUITER cannot: access student intelligence profiles directly

PLATFORM_ADMIN:
  admin_governance:
    moderation_queue: [VIEW, APPROVE, REJECT, ESCALATE]
    dispute_management: [VIEW, ASSIGN, RESOLVE, CLOSE]
    royalty_compliance: [VIEW, FLAG, ESCALATE]
    model_registry: [VIEW]  # read-only — model changes via version bump only
  # PLATFORM_ADMIN cannot: modify locked rubrics, alter immutable ledger entries

AUTOMATION:  # AI Agents — non-human
  # AUTOMATION can: read data, produce Kafka events, write scores
  # AUTOMATION cannot: approve/reject humans, modify contracts, override compliance flags
  scoring_engine:
    score_write: [CREATE]  # append-only
  intelligence:
    profile_update: [UPDATE]  # via event only, not direct API
```

---

## SECTION 11 — TIER 9: NOTIFICATION POLICY REGISTRY

### 11.1 Policy Standards
```
SERVICE             = Notification Service
CHANNELS            = EMAIL · SMS · PUSH · IN_APP
TEMPLATE_STORE      = /contracts/notifications/{event_trigger}/v{version}/template.json
TRIGGER_SOURCE      = Kafka event (from TIER 2 catalog)
DELIVERY_GUARANTEE  = AT_LEAST_ONCE (idempotency key per notification_id)
REGISTRY_LOCATION   = /contracts/notifications/policy/v{version}/policy.yaml
```

### 11.2 Critical Notification Policies

| Trigger Event | Channel(s) | Recipient | Template ID |
|---|---|---|---|
| user.account.created | EMAIL | user | NT_001 |
| parent.consent.granted | EMAIL + SMS | parent + child | NT_002 |
| idea.theft.flagged | EMAIL + IN_APP | idea owner + admin | NT_003 |
| royalty.anomaly.detected | EMAIL + IN_APP | compliance admin | NT_004 |
| licensing.contract.created | EMAIL | all contract parties | NT_005 |
| royalty.payment.processed | EMAIL + IN_APP | user / parent | NT_006 |
| gd.session.started | PUSH + IN_APP | all participants | NT_007 |
| dojo.belt.awarded | EMAIL + PUSH | user + mentor | NT_008 |
| billing.payment.failed | EMAIL + IN_APP | tenant admin | NT_009 |
| compliance.incident.logged | EMAIL | compliance admin | NT_010 |

---

## SECTION 12 — TIER 10: AUDIT LOG SCHEMA REGISTRY

### 12.1 Audit Standards
```
STORAGE_PRIMARY     = compliance PostgreSQL schema (append-only, RLS by user_id)
STORAGE_ARCHIVE     = Immutable Archive Service (MinIO WORM bucket, 15-year retention)
ANALYTICS_MIRROR    = ClickHouse (for anomaly detection, compliance reporting)
MUTABILITY          = NONE — audit logs cannot be updated or deleted
REQUIRED_FIELDS     = event_id, actor_id, actor_role, action_type, entity_type, entity_id,
                      timestamp, ip_address, request_id, tenant_id, outcome, metadata_json
TAMPER_DETECTION    = SHA-256 hash chain (each entry hashes previous entry_id)
REGISTRY_LOCATION   = /contracts/audit/schema/v{version}/audit_log_schema.sql
```

### 12.2 Auditable Action Catalog

| Action Code | Entity Type | Retention | Compliance Tag |
|---|---|---|---|
| USER_CREATED | users | 7 years | GDPR/IT_ACT |
| PARENT_CONSENT_GRANTED | users | 18 years | CHILD_PROTECTION |
| PARENT_CONSENT_REVOKED | users | 18 years | CHILD_PROTECTION |
| CONTRACT_SIGNED | licensing_contracts | 15 years | LEGAL |
| ROYALTY_ACCRUAL | royalty_ledger | 15 years | FINANCIAL |
| ROYALTY_PAYOUT | royalty_ledger | 15 years | FINANCIAL |
| OWNERSHIP_TRANSFERRED | ideas | 18 years | LEGAL |
| IDEA_THEFT_FLAGGED | ideas | 10 years | LEGAL |
| REVENUE_ANOMALY_FLAGGED | royalty_ledger | 15 years | FINANCIAL |
| MODEL_SCORE_COMPUTED | scoring_engine | 7 years | FAIRNESS |
| GD_SESSION_COMPLETED | gd_sessions | 5 years | ASSESSMENT |
| CERTIFICATE_ISSUED | certifications | 10 years | CREDENTIAL |
| CERTIFICATE_REVOKED | certifications | 10 years | CREDENTIAL |
| ADMIN_OVERRIDE | any | 10 years | GOVERNANCE |
| COMPLIANCE_INCIDENT | compliance | 18 years | CHILD_PROTECTION |

---

## SECTION 13 — CONTRACT GATE VALIDATION PROTOCOL

### 13.1 Gate Matrix
```
GATE_01: event_schema_ready
  REQUIRES: All TIER_2 topics registered in Apicurio with STATUS = ACTIVE
  PRODUCED_BY: Lane A (Foundation)
  CONSUMED_BY: Lane B (Data), Lane C (Core Services), Lane D (Governance)

GATE_02: api_contract_ready
  REQUIRES: All TIER_1 contracts present with STATUS = LOCKED
  PRODUCED_BY: Lane A (Foundation)
  CONSUMED_BY: Lane C (Core Services), Lane E (UI), Lane F (Intelligence)

GATE_03: db_ready
  REQUIRES: All TIER_3 Flyway migrations applied, RLS verified on all tables
  PRODUCED_BY: Lane B (Data)
  CONSUMED_BY: Lane C, Lane D

GATE_04: scoring_rubric_ready
  REQUIRES: All TIER_5 rubrics registered with STATUS = LOCKED
  PRODUCED_BY: This Agent (MODEL_GOVERNANCE_REGISTRY_AGENT)
  CONSUMED_BY: Scoring Engine, Dojo Engine, GD Orchestrator, Certification Engine

GATE_05: model_registry_ready
  REQUIRES: All TIER_4 model entries present with production_gate = HUMAN_EXECUTION_LOG_ATTACHED
  PRODUCED_BY: This Agent + Human Execution
  CONSUMED_BY: Intelligence Engine, Matching Engine, Fraud Detection

GATE_06: royalty_rules_ready
  REQUIRES: All TIER_6 rule sets registered with STATUS = LOCKED
  PRODUCED_BY: This Agent
  CONSUMED_BY: Royalty Accounting Engine, Revenue Ingestion Gateway

GATE_07: legal_templates_ready
  REQUIRES: All TIER_7 templates registered and archived
  PRODUCED_BY: This Agent + Legal Team Approval
  CONSUMED_BY: Legal Document Generation Service, Digital Signature Service

GATE_08: permission_matrix_ready
  REQUIRES: TIER_8 matrix covers ALL roles × ALL modules with explicit entries
  PRODUCED_BY: This Agent
  CONSUMED_BY: UI Layer (Antigravity Flutter generation), API Gateway (Kong)

FAILURE AT ANY GATE → STOP EXECUTION → REPORT GATE_FAILURE:{gate_id}
ALL GATES PASSED → PRODUCES: governance_ready
```

### 13.2 CI Enforcement Rule
```
CI_VALIDATOR        = /ci/validators/registry_gate_validator.sh
TRIGGER             = Every push to test, staging, production branches
VALIDATION_STEPS:
  1. Parse all TIER_1 contracts — verify OpenAPI 3.1 compliance (spectral lint)
  2. Query Apicurio Registry API — verify all TIER_2 topics registered
  3. Run Flyway dry-run — verify all TIER_3 migrations apply cleanly
  4. Parse TIER_4 model cards — verify human_execution_log_attached = true
  5. Parse TIER_5 rubrics — verify STATUS = LOCKED on all
  6. Parse TIER_6 rule sets — verify STATUS = LOCKED on all
  7. Parse TIER_7 templates — verify immutable_archive_id present on all
  8. Parse TIER_8 matrix — verify DENY_ALL default + explicit ALLOW entries
  9. Parse TIER_9 policies — verify all critical triggers mapped
  10. Parse TIER_10 schemas — verify hash chain integrity field present
ON_FAILURE: CI pipeline HARD_STOP → block merge → post report to Slack ops channel
ON_SUCCESS: PRODUCES gate artifact → registry_gate_check.json (all 8 gates: PASSED)
```

---

## SECTION 14 — VERSIONING & MUTATION GOVERNANCE

```
VERSION_FORMAT      = MAJOR.MINOR.PATCH (Semantic Versioning)
BREAKING_CHANGE:
  - Removing a field from event schema → MAJOR + new topic
  - Changing rubric formula weights → MAJOR (old version archived, not deleted)
  - Changing royalty computation rule → MAJOR + human legal review gate
  - Changing model advisory-only status → MAJOR + ethics board sign-off required
NON_BREAKING:
  - Adding optional field to event schema → MINOR
  - Adding new model to registry → MINOR
  - Adding new scoring dimension (additive only) → MINOR
PATCH:
  - Documentation corrections → PATCH
  - Threshold commentary updates (no formula change) → PATCH

MUTATION_LOG:
  - Every version bump recorded in /contracts/CHANGELOG.md
  - Author, date, rationale, linked GitHub/Forgejo PR required
  - Automated changelog enforcement via CI

ROLLBACK_POLICY:
  - Previous version remains live for minimum 90 days
  - Active contracts referencing old rubric/rule → must honor old version
  - Model scores computed with old model version → immutable, not retroactively changed
```

---

## SECTION 15 — FAILURE CATALOG

```
REGISTRY_VIOLATION:API_CONTRACT_MISSING:{service}
  → Service may not be built or deployed
  → Block: Lane C, Lane E, Lane F

REGISTRY_VIOLATION:EVENT_SCHEMA_MISSING:{topic}
  → Kafka producer must not publish to unregistered topic
  → Block: Lane B, Lane C, Lane D

REGISTRY_VIOLATION:MIGRATION_MISSING:{table}
  → Table may not exist in production without migration
  → Block: Lane B (db_ready gate)

REGISTRY_VIOLATION:MODEL_UNREGISTERED:{model_id}
  → Model may not be referenced in production code
  → Block: Lane F (ai_ready gate)

REGISTRY_VIOLATION:MODEL_MISSING_TRAINING_LOG:{model_id}
  → Model may not be deployed regardless of code readiness
  → Block: Lane F

REGISTRY_VIOLATION:RUBRIC_UNLOCKED:{rubric_id}
  → Scoring Engine must refuse to compute using unlocked rubric
  → Block: scoring_rubric_ready gate

REGISTRY_VIOLATION:ROYALTY_RULE_UNLOCKED:{rule_id}
  → Royalty Accounting Engine must refuse to compute
  → Block: royalty_rules_ready gate

REGISTRY_VIOLATION:CONTRACT_TEMPLATE_MISSING:{template_id}
  → Legal Document Generation Service must refuse request
  → Block: legal_templates_ready gate

REGISTRY_VIOLATION:RLS_MISSING:{table_name}
  → Table may not be accessed by any microservice
  → Block: db_ready gate

REGISTRY_VIOLATION:HARD_DELETE_DETECTED:{table_name}
  → Hard delete operation must be rejected at application layer
  → Report to compliance admin

REGISTRY_VIOLATION:SCHEMA_BREAKING_CHANGE_DETECTED
  → Topic producer blocked, new topic name required
  → Escalate to principal engineer

REGISTRY_VIOLATION:AUDIT_LOG_MISSING:{action_code}
  → Auditable action blocked from execution until schema registered
  → Block: governance_ready gate

ALL FAILURES: STOP EXECUTION → REPORT → NO PARTIAL OUTPUT
```

---

## SECTION 16 — TECHNOLOGY BINDINGS (NON-NEGOTIABLE)

```
API_CONTRACT_STORAGE    = Git repository /contracts/api/ (Forgejo, self-hosted)
EVENT_SCHEMA_STORAGE    = Apicurio Registry (self-hosted, ops namespace)
MODEL_CARD_STORAGE      = Git repository /contracts/models/ (Forgejo)
RUBRIC_STORAGE          = Git repository /contracts/scoring/ (Forgejo)
ROYALTY_RULE_STORAGE    = Git repository /contracts/royalty/ (Forgejo)
LEGAL_TEMPLATE_STORAGE  = Git repository + MinIO WORM bucket
PERMISSION_MATRIX       = Git repository /contracts/permissions/ (Forgejo)
NOTIFICATION_POLICY     = Git repository /contracts/notifications/ (Forgejo)
AUDIT_LOG_ARCHIVE       = MinIO WORM bucket (ecoskiller-compliance-archive)
                          Retention: 15 years minimum
VECTOR_STORE            = Qdrant (self-hosted, analytics namespace, Apache 2.0)
FEATURE_STORE           = Custom PostgreSQL schema + Redis cache
MODEL_SERVING           = FastAPI inference service (self-hosted, ml namespace)
CI_VALIDATOR            = Forgejo Actions (self-hosted runner, ops namespace)
SECRETS                 = HashiCorp Vault K8s Auth (no GCP IAM, no hardcoded creds)

FORBIDDEN_TOOLS:
  - GitHub Actions (use Forgejo Actions)
  - GHCR (use Harbor registry)
  - GCP Secret Manager (use Vault)
  - GCP BigQuery (use ClickHouse)
  - GCP Cloud Storage (use MinIO)
  - Cloudflare DNS (use PowerDNS self-hosted)
  - Any paid SaaS for registry operations

VIOLATION → STOP EXECUTION → REPORT FORBIDDEN_TOOL_DETECTED
```

---

## SECTION 17 — ANTIGRAVITY EXECUTION COMMANDS

### 17.1 How to Use This Agent with Antigravity

```
STEP_1: Paste this document once → locks MODEL_GOVERNANCE_REGISTRY_AGENT in Antigravity context
STEP_2: Run targeted registry generation commands (see 17.2)
STEP_3: CI validates all generated contracts against this spec before any service build begins
STEP_4: Only after all TIER_1–TIER_10 registries are validated → CODE GENERATION MAY BEGIN
```

### 17.2 Registry Generation Commands

```
# Generate API Contract for a service
GENERATE_API_CONTRACT
  SERVICE     = voice-gd-orchestrator
  VERSION     = v1.0.0
  DOMAIN      = voice_gd
  FORMAT      = openapi_3.1

# Register an Event Schema
REGISTER_EVENT_SCHEMA
  TOPIC       = gd.session.completed
  DOMAIN      = voice_gd
  FORMAT      = avro
  VERSION     = v1.0.0

# Register a Model Card
REGISTER_MODEL
  MODEL_ID    = resume-parser-v1
  DOMAIN      = job-portal
  FILL_TEMPLATE = TIER_4_MODEL_CARD

# Lock a Scoring Rubric
LOCK_RUBRIC
  RUBRIC_ID   = gd-participation-v1
  DOMAIN      = voice_gd
  STATUS      = LOCKED

# Validate Full Registry Gate
VALIDATE_REGISTRY_GATES
  SCOPE       = ALL_TIERS
  OUTPUT      = registry_gate_check.json
```

---

## SECTION 18 — FINAL SEAL

```
AGENT_STATUS             = COMPLETE · SEALED · LOCKED
CHANGE_POLICY            = APPEND_ONLY VIA VERSION BUMP
ANTIGRAVITY_CONFUSION    = IMPOSSIBLE
ASSUMPTION_PERMITTED     = NONE
IMPLICIT_BEHAVIOR        = NONE
QUALITY_SCORE            = 10 / 10

GATES PRODUCED BY THIS AGENT:
  ✔ scoring_rubric_ready
  ✔ model_registry_ready
  ✔ royalty_rules_ready
  ✔ legal_templates_ready
  ✔ permission_matrix_ready
  ✔ event_schema_ready  (co-produced with Lane A)
  ✔ governance_ready    (composite — requires all above)

ANY SERVICE BUILT WITHOUT ALL GATES ABOVE = INVALID BUILD
ANY ANTIGRAVITY RUN IGNORING THIS AGENT   = INVALID RUN

⚠️ Antigravity must not reinterpret, simplify, extend, or bypass
   any registry rule, gate condition, failure code, or model governance
   law defined in this document.

⚠️ AI (including Antigravity itself) is ADVISORY ONLY on this platform.
   No AI agent may approve, block, or override human decisions.
   AI agents operate only under registered model cards with
   human_execution_log_attached = TRUE.

DOCUMENT_END: MODEL_GOVERNANCE_REGISTRY_AGENT v1.0.0
STATUS: FINAL · LOCKED · GOVERNED · DETERMINISTIC
```
