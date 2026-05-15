# 🔒 SEALED & LOCKED · ECOSKILLER ANTIGRAVITY
# LICENSE_GENERATION_AGENT
### ML Intelligence & Safety Owner — Enterprise Autonomous Agent

> **STATUS: FINAL · GOVERNED · DETERMINISTIC · ADD-ONLY MUTATION**
>
> **MUTATION POLICY: ADD-ONLY · CREATIVE INTERPRETATION: FORBIDDEN · ASSUMPTION FILLING: FORBIDDEN · FAILURE MODE: HALT ON AMBIGUITY**

---

## SYSTEM CONTEXT

```
Platform:         Ecoskiller Antigravity
Architecture:     Microservices + Event Driven
Scale Target:     10M–100M users
ML Usage:         70–80% traditional ML
AI Usage:         20–30% LLM / semantic reasoning
Mutation Policy:  Add-only versioned
Security Model:   Zero-trust multi-tenant isolation
Data Policy:      Append-only audit trail
Execution Lane:   Lane D — Governance
```

---

## 1. AGENT IDENTITY (MANDATORY — LOCKED)

| Field | Value |
|---|---|
| **AGENT_NAME** | `LICENSE_GENERATION_AGENT` |
| **SYSTEM_ROLE** | Autonomous intelligent license lifecycle engine: generates, validates, activates, monitors, renews, revokes, and audits all platform licenses across every tenant, user type, product tier, and domain track |
| **PRIMARY_DOMAIN** | Governance · Billing · Compliance · Credential Issuance · Intelligence Certificate Engine |
| **EXECUTION_MODE** | Deterministic + Validated. Identical input → Identical output. Zero creative interpretation. |
| **DATA_SCOPE** | User subscriptions, tenant plans, skill certifications, intelligence certificates, course completion records, feature entitlements, seat allocations, billing events, credential hashes, audit trails |
| **TENANT_SCOPE** | STRICT ISOLATION — No cross-tenant license data access. Zero-trust enforcement on every operation. |
| **FAILURE_POLICY** | HALT ON AMBIGUITY — Stop, Log, Escalate. No license issued without complete validated input. No silent failures. |
| **VERSION** | `v1.0.0` — Baseline Sealed Release |

---

## 2. PURPOSE DECLARATION

### Problem This Agent Solves

The Ecoskiller platform operates across 300+ user types, 5 domain tracks, multi-tier subscription plans (Free → Premium → Enterprise), multi-tenant institute/company isolation, and a revolutionary **Intelligence Certificate System** (derived from the Dojo Intelligence Engine and Howard Gardner's Multiple Intelligences framework). Without a governed license engine, the platform cannot:

- Enforce feature entitlements per subscription tier
- Issue tamper-proof intelligence and skill certificates to learners
- Manage seat-based licenses for institutes and enterprises
- Auto-renew, auto-revoke, and audit license states across 100M users
- Generate verifiable credentials for government, NGO, and accreditation bodies
- Prevent cross-tenant license leakage or unauthorized feature access

This agent is the **single source of truth** for all license states, credential issuance, and entitlement enforcement across the entire Ecoskiller ecosystem.

### What This Agent Consumes

| Input Source | Description |
|---|---|
| **Billing Event** | From `BILLING_AGENT` — payment confirmed, plan selected, renewal triggered |
| **Course Completion Signal** | From `LEARNING_ENGINE` — user completed course/module with verified score |
| **Intelligence Assessment Result** | From `DOJO_INTELLIGENCE_ENGINE` — Gardner intelligence profile score (8 types) |
| **Skill Assessment Result** | From `SKILL_ASSESSMENT_AGENT` — verified skill proficiency score |
| **Tenant Provisioning Event** | From `TENANT_PROVISIONING_AGENT` — new institute/company tenant activated |
| **Seat Allocation Request** | From institute/company admin — seat count for bulk license activation |
| **Revocation Request** | From `COMPLIANCE_AGENT` or admin — suspend/revoke license for policy violation |
| **Renewal Signal** | From `BILLING_AGENT` or scheduler — renewal window triggered |
| **Government Scheme Signal** | From `GOVERNMENT_INTEGRATION_AGENT` — NSDC/AICTE/SSC scheme eligibility confirmed |

### What This Agent Produces

| Output | Description |
|---|---|
| **LICENSE_RECORD** | Cryptographically signed license object with entitlements, scope, validity, tenant binding |
| **INTELLIGENCE_CERTIFICATE** | Verifiable credential: Gardner intelligence type + level + score + issuer hash |
| **SKILL_CERTIFICATE** | Verifiable credential: skill name + proficiency level + assessment reference + hash |
| **COURSE_COMPLETION_CERTIFICATE** | Tamper-proof certificate: course, score, date, issuer, credential hash |
| **ENTITLEMENT_VECTOR** | Feature access map per user/tenant — consumed by API Gateway and UI layer |
| **LICENSE_AUDIT_RECORD** | Immutable log of every license event: issue, activate, renew, suspend, revoke |
| **CREDENTIAL_HASH** | SHA-256 fingerprint of every issued credential — stored in `CREDENTIAL_REGISTRY` |
| **REVOCATION_EVENT** | Structured event: license/credential ID + reason + timestamp → downstream agents |

### Agent Dependency Map

| Direction | Agents |
|---|---|
| **UPSTREAM** (feed this agent) | `BILLING_AGENT`, `LEARNING_ENGINE`, `DOJO_INTELLIGENCE_ENGINE`, `SKILL_ASSESSMENT_AGENT`, `TENANT_PROVISIONING_AGENT`, `GOVERNMENT_INTEGRATION_AGENT`, `COMPLIANCE_AGENT`, `PROFILE_VERIFICATION_AGENT` |
| **DOWNSTREAM** (depend on this agent) | `API_GATEWAY_AGENT`, `ENTITLEMENT_ENFORCEMENT_AGENT`, `NOTIFICATION_AGENT`, `CREDENTIAL_REGISTRY`, `OBSERVABILITY_AGENT`, `GROWTH_ENGINE`, `RANKING_AGENT`, `BILLING_AGENT` (for renewal confirmation), `AUDIT_AGENT` |

---

## 3. LICENSE TAXONOMY (LOCKED)

### 3A. Platform Subscription Licenses

| License Tier | Scope | Key Entitlements | Seat Model |
|---|---|---|---|
| **FREE** | Individual user | 3 job applications/month, 2 Dojo sessions/month, basic profile, 1 domain track | Per user |
| **STUDENT_PREMIUM** | Individual student | Unlimited applications, full Dojo access, intelligence profiling, course access, portfolio | Per user |
| **PROFESSIONAL** | Working professional / freelancer | All premium + project marketplace, skill assessments, verified badges | Per user |
| **TRAINER_PRO** | Trainer / mentor | Course creation, student analytics, certification issuance, trainer legacy | Per user |
| **INSTITUTE_BASIC** | Institute (≤ 500 students) | LMS, placement dashboard, batch management, TPO analytics | Seat-based |
| **INSTITUTE_ENTERPRISE** | Institute (> 500 students) | All Institute_Basic + ERP, API access, custom branding, accreditation tools | Seat-based |
| **COMPANY_STARTER** | SME / Startup (≤ 20 hires/year) | Job posting, candidate matching, interview scheduling | Seat-based |
| **COMPANY_ENTERPRISE** | Corporate (unlimited hiring) | Full hiring ERP, L&D module, campus connect, bulk assessment, AI matching | Seat-based |
| **GOVERNMENT_SCHEME** | Government / NGO | Scheme-linked enrollment, NSDC/AICTE credential issuance, bulk placement | Program-based |
| **PLATFORM_INTERNAL** | Internal platform agents and services | Full API access, no billing constraint | Service account |

### 3B. Credential / Certificate Licenses

| Certificate Type | Issuer Authority | Verification Method | Blockchain-Ready |
|---|---|---|---|
| **Intelligence Certificate** | Ecoskiller Dojo Intelligence Engine | Credential hash + public verification portal | Yes (future) |
| **Skill Proficiency Certificate** | Ecoskiller Skill Assessment Engine | Assessment reference + score hash | Yes |
| **Course Completion Certificate** | Trainer / Institute + Ecoskiller countersign | Course ID + completion hash | Yes |
| **Trainer Legacy Certificate** | Ecoskiller Platform Authority | Trainer ID + career hash | Yes |
| **Government Scheme Certificate** | NSDC / AICTE / SSC (via integration) | Government scheme reference + Ecoskiller hash | Yes |
| **Innovation / Patent Certificate** | Ecoskiller Innovation Economy Engine | IDEA_DNA hash + originality score | Yes |

---

## 4. INPUT CONTRACT (STRICT — NO NULL TOLERANCE)

```json
{
  "required_fields": [
    "request_id",             // UUID — unique per request
    "request_type",           // Enum: ISSUE | RENEW | REVOKE | SUSPEND | VERIFY | ENTITLEMENT_CHECK
    "license_type",           // Enum: SUBSCRIPTION | INTELLIGENCE_CERT | SKILL_CERT | COURSE_CERT | TRAINER_CERT | GOVT_CERT
    "tenant_id",              // Strict tenant isolation key
    "entity_id",              // User UUID or Institute UUID or Company UUID
    "entity_type",            // Enum: STUDENT | TRAINER | FREELANCER | INSTITUTE | COMPANY | GOVERNMENT | PLATFORM_SERVICE
    "domain_track",           // Enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION
    "request_timestamp",      // ISO 8601 UTC — anti-replay enforced within 300s
    "actor_id",               // Who triggered this request (user, admin, agent)
    "actor_role"              // RBAC role of the actor
  ],

  "conditional_fields": {
    "SUBSCRIPTION license": [
      "plan_tier",            // FREE | STUDENT_PREMIUM | PROFESSIONAL | TRAINER_PRO | INSTITUTE_BASIC | etc.
      "billing_reference",    // From BILLING_AGENT — payment confirmation UUID
      "seat_count",           // Required for INSTITUTE_* and COMPANY_* tiers
      "validity_period_days"  // License duration: 30 | 90 | 365
    ],
    "INTELLIGENCE_CERT": [
      "intelligence_type",    // LINGUISTIC | LOGICAL | SPATIAL | BODILY | MUSICAL | INTERPERSONAL | INTRAPERSONAL | NATURALISTIC
      "intelligence_score",   // 0–100 integer
      "assessment_reference", // Dojo session UUID
      "assessment_date"       // ISO 8601
    ],
    "SKILL_CERT": [
      "skill_id",             // Skill master record UUID
      "skill_name",           // Verified skill name
      "proficiency_level",    // BEGINNER | INTERMEDIATE | ADVANCED | EXPERT
      "assessment_score",     // 0–100
      "assessment_reference"  // Assessment session UUID
    ],
    "COURSE_CERT": [
      "course_id",
      "course_name",
      "completion_score",     // 0–100
      "trainer_id",           // Issuing trainer UUID
      "institute_id"          // Optional — if institute-bound
    ]
  },

  "validation_rules": [
    "request_type must be in permitted enum set",
    "entity_type must match declared license_type scope",
    "domain_track must not violate tenant domain isolation policy",
    "billing_reference must be verified with BILLING_AGENT before SUBSCRIPTION issuance",
    "assessment_reference must be verified with source agent before certificate issuance",
    "request_timestamp must be within last 300 seconds (anti-replay protection)",
    "tenant_id must match JWT claim — reject if mismatch",
    "seat_count must not exceed tenant plan maximum",
    "intelligence_score must be in range 0–100",
    "No duplicate certificate issuable for same entity + same assessment_reference"
  ],

  "security_checks": [
    "JWT validation — REQUIRED before any processing",
    "RBAC role check — actor_role must have LICENSE_GENERATE permission for request_type",
    "Cross-tenant query detection — REJECT if entity belongs to different tenant",
    "Domain isolation check — REJECT if domain_track violates tenant policy",
    "Billing verification — REQUIRED for all SUBSCRIPTION license requests",
    "Rate limit: max 50 license requests/min per entity_id",
    "Duplicate detection: reject if identical license already ACTIVE for entity_id"
  ],

  "domain_checks": [
    "ARTS domain: intelligence certificates valid for arts-declared career paths only unless overridden",
    "TECHNOLOGY domain: skill certificates require minimum INTERMEDIATE proficiency for job-linked credentials",
    "GOVERNMENT licenses: require scheme_reference from GOVERNMENT_INTEGRATION_AGENT",
    "INSTITUTE licenses: require accreditation_status = VERIFIED before enterprise tier activation"
  ]
}
```

> ❌ **REJECT POLICY:** Any missing `required_field` or failed `validation_rule` → REJECT immediately with structured error. Log validation failure to `AUDIT_AGENT`. Do NOT proceed with partial data. Do NOT issue partial licenses.

---

## 5. OUTPUT CONTRACT (STRICT — ALL FIELDS MANDATORY)

### 5A. License Record Output

```json
{
  "result_object": {
    "license_id":           "UUID — globally unique",
    "license_type":         "SUBSCRIPTION | INTELLIGENCE_CERT | SKILL_CERT | COURSE_CERT | ...",
    "license_tier":         "FREE | STUDENT_PREMIUM | INSTITUTE_ENTERPRISE | ...",
    "entity_id":            "UUID of licensed entity",
    "entity_type":          "STUDENT | INSTITUTE | COMPANY | ...",
    "tenant_id":            "UUID — tenant-bound",
    "domain_track":         "ARTS | TECHNOLOGY | ...",
    "status":               "ACTIVE | PENDING | SUSPENDED | REVOKED | EXPIRED",
    "issued_at":            "ISO 8601 UTC",
    "valid_from":           "ISO 8601 UTC",
    "valid_until":          "ISO 8601 UTC",
    "seat_count":           "integer | null",
    "entitlement_vector":   {
      "feature_flags":      ["JOB_APPLY", "DOJO_ACCESS", "COURSE_CREATE", "API_ACCESS", "..."],
      "limits":             { "job_applications_per_month": 999, "dojo_sessions_per_month": 999 },
      "domain_scope":       ["TECHNOLOGY", "COMMERCE"]
    },
    "credential_hash":      "SHA-256 of license record",
    "issuer_signature":     "HMAC-SHA256 signed by platform license authority",
    "verification_url":     "https://verify.ecoskiller.com/license/{license_id}",
    "renewal_due_date":     "ISO 8601 UTC | null",
    "billing_reference":    "UUID | null"
  },
  "confidence_score":       "0.0–1.0",
  "model_version":          "LGA_ML_v1.0.0",
  "audit_reference":        "UUID — immutable trace key",
  "processing_time_ms":     "integer",
  "next_trigger_event": [
    "LICENSE_ACTIVATED_EVENT",
    "ENTITLEMENT_UPDATE_EVENT",
    "NOTIFICATION_TRIGGER_EVENT"
  ]
}
```

### 5B. Certificate Output (Intelligence / Skill / Course)

```json
{
  "result_object": {
    "certificate_id":         "UUID — globally unique",
    "certificate_type":       "INTELLIGENCE_CERT | SKILL_CERT | COURSE_CERT | TRAINER_CERT",
    "entity_id":              "UUID of certificate holder",
    "tenant_id":              "UUID — tenant-bound",
    "domain_track":           "ARTS | TECHNOLOGY | ...",

    "certificate_data": {
      "intelligence_type":    "LINGUISTIC | LOGICAL | SPATIAL | ...",  // for INTELLIGENCE_CERT
      "intelligence_level":   "LEVEL_1 to LEVEL_10",                   // mapped from score
      "score":                "0–100 integer",
      "skill_id":             "UUID | null",                           // for SKILL_CERT
      "skill_name":           "string | null",
      "proficiency_level":    "BEGINNER | INTERMEDIATE | ADVANCED | EXPERT | null",
      "course_id":            "UUID | null",                           // for COURSE_CERT
      "course_name":          "string | null",
      "issuing_trainer_id":   "UUID | null",
      "issuing_institute_id": "UUID | null"
    },

    "assessment_reference":   "UUID of source assessment session",
    "issued_at":              "ISO 8601 UTC",
    "valid_until":            "ISO 8601 UTC | PERMANENT",
    "credential_hash":        "SHA-256 of certificate record",
    "issuer_signature":       "HMAC-SHA256 signed by platform certificate authority",
    "verification_url":       "https://verify.ecoskiller.com/cert/{certificate_id}",
    "public_display_flag":    "true | false",
    "badge_reference":        "Badge UUID | null"
  },
  "confidence_score":         "0.0–1.0",
  "model_version":            "LGA_CERT_v1.0.0",
  "audit_reference":          "UUID — immutable trace key",
  "processing_time_ms":       "integer",
  "next_trigger_event": [
    "CERTIFICATE_ISSUED_EVENT",
    "BADGE_AWARD_TRIGGER",
    "XP_UPDATE_EVENT",
    "CREDENTIAL_REGISTRY_UPDATE",
    "SHARE_TRIGGER_EVENT"
  ]
}
```

> ⚠️ **RULE:** If `confidence_score < 0.90` on any certificate issuance → DO NOT issue certificate. Emit `LOW_CONFIDENCE_CERT_ALERT`. Escalate to `QUALITY_CONTROL_AGENT`. Confidence threshold for certificates is higher than matches (0.90 vs 0.55) because certificates are legal documents.

---

## 6. ML / AI LOGIC LAYER

### 6A. PRIMARY ML ENGINE (70–80% of decisions)

| Field | Value |
|---|---|
| **MODEL_TYPE** | Multi-class Classification + Rule Engine + Anomaly Detection |
| **ALGORITHM_STACK** | Stage 1: Rule-based eligibility engine (deterministic — plan tier + billing state + assessment score thresholds). Stage 2: XGBoost fraud/anomaly classifier (detect fake assessment signals, billing anomalies). Stage 3: Renewal probability predictor (Time-Series — churn risk per license). |
| **FEATURES_USED — Subscription** | `plan_tier`, `billing_status`, `seat_utilization_ratio`, `tenant_age_days`, `payment_failure_count`, `feature_usage_rate`, `renewal_history`, `domain_track`, `entity_type` |
| **FEATURES_USED — Certificate** | `assessment_score`, `assessment_session_duration`, `attempt_count`, `score_delta_from_mean`, `behavioral_consistency_score`, `trainer_reputation_score`, `institute_accreditation_level`, `domain_track` |
| **TRAINING_FREQUENCY** | Weekly retraining on new billing + assessment interaction data. Monthly full refit. |
| **DRIFT_DETECTION** | Monitor: distribution shift on `assessment_score` inputs. Monitor: fraud detection accuracy on known abuse cases. Threshold: >3% drift triggers alert to `OBSERVABILITY_AGENT`. Certificate fraud threshold is tighter than general ML (3% vs 5%). |
| **VERSION_CONTROL** | All models tagged: `LGA_ML_v{major}.{minor}.{patch}`. Immutable reference in `MODEL_REGISTRY`. Rollback-safe. |

### 6B. FRAUD & ANOMALY DETECTION ENGINE (ML sub-module)

| Signal | Detection Method | Action on Trigger |
|---|---|---|
| Assessment completed too fast | Z-score on `session_duration` vs peer cohort | Flag → manual review before cert issuance |
| Same user, repeated attempts to boost score | Attempt count threshold + score jump pattern | Lock assessment for 24h + alert |
| Billing reference not confirmed by BILLING_AGENT | Real-time verification call | REJECT license issuance |
| Bulk certificate requests from single IP | Rate anomaly detection | HALT + `SECURITY_INCIDENT_EVENT` |
| Certificate hash already exists (duplicate) | Hash collision check in CREDENTIAL_REGISTRY | REJECT + LOG duplicate attempt |
| Score inconsistent with behavioral signals from PASSIVE_INTELLIGENCE_AGENT | Score vs passive intelligence delta > 40 points | Flag for human review — do not auto-issue |

### 6C. AI ASSISTANCE ENGINE (20–30% — Explainability & Certificate Content)

| Field | Value |
|---|---|
| **AI_USAGE_SCOPE** | 1) Generate natural-language certificate description text (e.g., "This certifies that the holder demonstrated Level 7 Interpersonal Intelligence through 13 validated behavioral assessments in the Dojo Intelligence Engine."). 2) Suggest license tier upgrade recommendations based on usage patterns. 3) Semantic validation of skill names against master taxonomy. |
| **PROMPT_GOVERNANCE** | All prompts versioned: `PROMPT_LGA_CERT_DESC_v1.0`. Deterministic structure. Temperature = 0. Max tokens = 200 per certificate description. No creative embellishment. |
| **AI_BOUNDARIES** | AI MUST NOT: make eligibility decisions, approve or deny license requests, override ML fraud scores, modify credential hashes, generate certificate data fields, or take any action outside defined scope. |
| **AI_DECISION_AUTHORITY** | **NONE.** AI generates descriptive text only. All eligibility and issuance decisions made by ML rule engine. |

> 🔒 **HARD RULE:** AI ASSISTS ML. AI does NOT replace ML. No license or certificate is issued without a valid ML eligibility confirmation. AI-generated text is appended post-issuance — it does not influence the issuance decision.

---

## 7. LICENSE LIFECYCLE STATE MACHINE (LOCKED)

```
[REQUESTED]
     │
     ▼
[VALIDATING] ──── Validation Failure ──────────────► [REJECTED] → LOG + NOTIFY
     │
     ▼
[FRAUD_CHECK] ─── Anomaly Detected ────────────────► [FLAGGED] → HUMAN_REVIEW_QUEUE
     │
     ▼
[BILLING_VERIFY] ─ Billing Unconfirmed (subscriptions) ► [PENDING_PAYMENT] → NOTIFY
     │
     ▼
[ISSUING] ──────── Signing + Hash Generation
     │
     ▼
[ACTIVE] ◄──────── Normal operating state
     │         │
     │         ├──── Renewal Window Opens ──────────► [RENEWAL_PENDING] → NOTIFY
     │         │         │
     │         │         ├── Payment Success ────────► [ACTIVE] (renewed)
     │         │         └── Payment Failed ──────────► [GRACE_PERIOD] → NOTIFY
     │         │                   │
     │         │                   └── Grace Expired ► [EXPIRED] → REVOKE_ENTITLEMENTS
     │         │
     │         ├──── Admin Suspend ─────────────────► [SUSPENDED] → NOTIFY
     │         │         │
     │         │         └── Admin Reinstate ─────────► [ACTIVE]
     │         │
     │         └──── Policy Violation ──────────────► [REVOKED] → NOTIFY + AUDIT
     │
     ▼
[EXPIRED | REVOKED | REJECTED] → Immutable terminal states — no reactivation without new request
```

> 🔴 **CRITICAL:** State transitions are append-only. No state record is deleted or overwritten. Every transition logged as immutable event in `LICENSE_STATE_LOG`.

---

## 8. INTELLIGENCE CERTIFICATE SYSTEM (DOJO INTEGRATION — LOCKED)

This section governs the issuance of **Intelligence Certificates** powered by the Ecoskiller Dojo Intelligence Engine, implementing Howard Gardner's Multiple Intelligences framework.

### 8A. Intelligence Types and Certificate Levels

| Intelligence Type | Dojo Assessment Source | Score Range → Level Mapping |
|---|---|---|
| **Linguistic** | 13 Dojo linguistic tests + passive writing behavior signals | 0–10: L1, 11–20: L2, 21–30: L3, 31–40: L4, 41–50: L5, 51–60: L6, 61–70: L7, 71–80: L8, 81–90: L9, 91–100: L10 |
| **Logical** | 13 logical tests + coding patterns + puzzle solving | Same L1–L10 scale |
| **Spatial** | 12 spatial tests + UI design + dashboard behavior | Same L1–L10 scale |
| **Bodily** | 12 physical tests + fitness tracker sync + motion sensors | Same L1–L10 scale |
| **Musical** | 12 musical tests + rhythm interaction + audio uploads | Same L1–L10 scale |
| **Interpersonal** | 13 interpersonal tests + social feed behavior + leadership signals | Same L1–L10 scale |
| **Intrapersonal** | 13 intrapersonal tests + journaling + goal setting behavior | Same L1–L10 scale |
| **Naturalistic** | 12 nature tests + agriculture modules + eco interactions | Same L1–L10 scale |

### 8B. Certificate Issuance Rules for Intelligence Certificates

```
Minimum score for certificate issuance:       Level 3 (score ≥ 21)
Minimum assessed tests completed:             At least 7 of available tests for that intelligence type
Behavioral signal confirmation required:      Passive Intelligence score delta ≤ 40 from assessed score
Assessment recency requirement:               Assessment must be within last 365 days
Duplicate check:                              No new certificate if current ACTIVE cert exists for same type
                                             (unless re-assessment shows score change > 15 points)
Confidence threshold for auto-issuance:       ≥ 0.90
Below 0.90 → HUMAN_REVIEW_QUEUE             Do not auto-issue
```

### 8C. Intelligence Profile → Career Path Link

The LICENSE_GENERATION_AGENT must emit a structured intelligence profile payload when issuing an Intelligence Certificate, consumed by the `BUSINESS_MATCHING_AGENT` and `CAREER_PATH_AGENT`:

```json
{
  "emit_type":          "INTELLIGENCE_PROFILE_UPDATE",
  "entity_id":          "UUID",
  "intelligence_vector": {
    "linguistic":       "0–100",
    "logical":          "0–100",
    "spatial":          "0–100",
    "bodily":           "0–100",
    "musical":          "0–100",
    "interpersonal":    "0–100",
    "intrapersonal":    "0–100",
    "naturalistic":     "0–100"
  },
  "top_intelligence_types": ["INTERPERSONAL", "LINGUISTIC", "INTRAPERSONAL"],
  "career_fit_signals":     ["TEACHER", "MANAGER", "COACH", "COUNSELOR"],
  "worst_fit_signals":      ["MECHANICAL_ENGINEERING", "PURE_MATHEMATICS"],
  "source_agent":           "LICENSE_GENERATION_AGENT",
  "timestamp":              "ISO 8601 UTC"
}
```

---

## 9. LICENSE COVERAGE BY USER TYPE (ALL 300 USER TYPES — LOCKED)

| User Group | Applicable License Types | Certificate Types | Special Rules |
|---|---|---|---|
| **A. Students & Learners (1–40)** | FREE → STUDENT_PREMIUM | Intelligence Cert, Skill Cert, Course Completion Cert | School students (Grade 6–10): Parent consent required for premium activation |
| **B. Trainers & Educators (41–75)** | PROFESSIONAL → TRAINER_PRO | Trainer Legacy Cert, Course Completion Cert (as issuer) | Trainer must hold ≥1 verified Skill Cert to issue course certificates |
| **C. Institutes (76–110)** | INSTITUTE_BASIC → INSTITUTE_ENTERPRISE | Govt Scheme Cert (if accredited) | Seat-based. Accreditation verification required for Enterprise tier. |
| **D. Companies & Employers (111–160)** | COMPANY_STARTER → COMPANY_ENTERPRISE | N/A (issuers only via CSR programs) | Seat-based. CTO/CISO accounts require PLATFORM_INTERNAL scope for API access. |
| **E. Freelancers & Creators (161–200)** | PROFESSIONAL | Skill Cert, Course Cert | Course creators must hold TRAINER_PRO to issue certificates to learners |
| **F. Government & NGOs (201–230)** | GOVERNMENT_SCHEME | Govt Scheme Cert, NSDC/AICTE Cert | Requires active government scheme reference. Batch certificate issuance supported. |
| **G. Platform Ops & Tech (231–270)** | PLATFORM_INTERNAL | N/A | Service account licenses only. No credential issuance. Full API access. |
| **H. Advanced / Future Roles (271–300)** | PROFESSIONAL → COMPANY_ENTERPRISE | Verifiable Credential (VC) — W3C DID compatible | Digital twin and metaverse instructors may hold XR_TRAINING_LICENSE (future tier). |

---

## 10. SCALABILITY DESIGN

| Parameter | Value |
|---|---|
| **EXPECTED_RPS** | 5,000 license operations/sec at 10M users. 50,000 RPS at 100M users. |
| **LATENCY_TARGET** | P50 < 120ms · P95 < 500ms · P99 < 1,200ms. Certificate signing adds ~80ms overhead. |
| **MAX_CONCURRENCY** | 30,000 simultaneous license operations per tenant cluster |
| **QUEUE_STRATEGY** | Redis Streams for async certificate generation. Synchronous path for real-time entitlement checks. Priority queue: REVOCATION > RENEWAL > NEW_ISSUANCE. |
| **EXECUTION_MODEL** | Stateless — no session state in agent. All state from PostgreSQL + Redis cache. |
| **IDEMPOTENCY** | Same `request_id` → Same output. Duplicate `request_id` within 1 hour → return cached result without reprocessing. |
| **HORIZONTAL_SCALING** | Kubernetes HPA. Min 3 replicas. Max 150 replicas. CPU threshold 70%. |
| **CACHING_LAYER** | Redis 7. Active license entitlement vectors cached 5 min (short TTL — entitlements must be fresh). Certificate metadata cached 60 min. Revocation list cached 30 seconds (fast invalidation). |
| **SIGNING_INFRASTRUCTURE** | HSM (Hardware Security Module) or Vault-backed key management for HMAC-SHA256 signing. Signing keys rotated every 90 days. All rotations logged to `AUDIT_AGENT`. |
| **BULK_OPERATIONS** | Institute/Government batch certificate issuance: async job queue. Max 10,000 certificates per batch. Progress tracked via `batch_job_id`. |

---

## 11. SECURITY ENFORCEMENT (ZERO-TRUST MODEL)

> 🔴 **CRITICAL:** A license or certificate issued without tenant isolation verification is a CRITICAL SECURITY FAILURE. Any cross-tenant license operation must trigger `SECURITY_INCIDENT_EVENT` and halt execution immediately.

| Control | Enforcement |
|---|---|
| **Tenant Isolation** | Hard enforcement via `tenant_id` on every DB query and signing operation. No override permitted. Cross-tenant issuance = HALT + SECURITY_INCIDENT. |
| **Domain Isolation** | License scope enforced per `domain_track`. Cross-domain entitlements require explicit RBAC grant. |
| **Role-Based Authorization** | `LICENSE_GENERATE` permission required for issuance. `LICENSE_REVOKE` requires elevated role. `LICENSE_VIEW` is read-only. Checked via Kong API Gateway + Keycloak before execution. |
| **Encryption** | All data in transit: TLS 1.3. At rest: AES-256. Certificate signing keys stored in Vault/HSM. No plaintext credentials in logs. |
| **Credential Integrity** | Every issued license and certificate has a `credential_hash` (SHA-256) and `issuer_signature` (HMAC-SHA256). Tampering detectable on verification. |
| **Revocation List** | Active revocation list maintained in Redis + PostgreSQL. API Gateway checks revocation list on every protected request. TTL: 30 seconds max staleness. |
| **Anti-Replay** | `request_timestamp` within ±300 seconds. Duplicate `request_id` rejected within 1-hour window. |
| **Rate Limiting** | 50 license requests/min per `entity_id`. 5,000/min per tenant. Bulk batch operations use separate async queue — not rate-limited but require admin authorization. |
| **Audit Logging** | Append-only. Every license event creates immutable record. Log tampering triggers `SECURITY_ALERT`. |

---

## 12. AUDIT & TRACEABILITY (IMMUTABLE — APPEND-ONLY)

Every license operation — issuance, renewal, suspension, revocation, verification — must produce an immutable audit record.

```json
{
  "audit_reference":        "UUID — globally unique per operation",
  "timestamp_utc":          "ISO 8601",
  "operation_type":         "ISSUE | RENEW | REVOKE | SUSPEND | VERIFY | BATCH_ISSUE",
  "actor_id":               "entity/admin/agent that triggered operation",
  "actor_role":             "RBAC role",
  "tenant_id":              "isolated tenant identifier",
  "entity_id":              "licensed entity UUID",
  "license_id":             "UUID of affected license",
  "certificate_id":         "UUID | null",
  "input_hash":             "SHA-256 of full input payload",
  "output_hash":            "SHA-256 of full output payload",
  "credential_hash":        "SHA-256 of issued credential",
  "model_version":          "LGA_ML_v1.0.0",
  "decision_path":          "VALIDATE → FRAUD_CHECK → BILLING_VERIFY → SIGN → ACTIVATE",
  "confidence_score":       "0.0–1.0",
  "anomaly_flags":          ["NONE | FAST_ASSESSMENT | SCORE_INCONSISTENCY | BILLING_UNVERIFIED | DUPLICATE_ATTEMPT"],
  "processing_time_ms":     "integer",
  "signing_key_version":    "string — which signing key was active",
  "previous_state":         "ACTIVE | PENDING | null",
  "new_state":              "ACTIVE | REVOKED | SUSPENDED | EXPIRED"
}
```

> 📋 **COMPLIANCE:** License and certificate audit logs must be retained for **minimum 10 years** (longer than general audit logs due to credential legal validity). No log may be deleted, modified, or overwritten. Violation triggers `COMPLIANCE_BREACH_EVENT`.

---

## 13. FAILURE POLICY (NO SILENT FAILURES)

| Failure Scenario | Action | Escalate To | Retry Policy |
|---|---|---|---|
| Invalid / Malformed Input | `STOP` + `REJECT_400` + structured error | `AUDIT_AGENT` | No retry — caller must fix |
| Billing Verification Failed | `STOP` + `REJECT_402` + `NOTIFY_USER` | `BILLING_AGENT` | No retry — billing must be resolved first |
| Assessment Reference Invalid | `STOP` + `REJECT_422` + log | `AUDIT_AGENT` | No retry — assessment must be re-verified |
| ML Model Unavailable | `STOP` + `CIRCUIT_BREAK` | `OBSERVABILITY_AGENT` + `ON_CALL` | Retry ×3, 30s backoff, then HALT |
| Fraud / Anomaly Detected | `HALT_ISSUANCE` + `FLAG_FOR_REVIEW` | `COMPLIANCE_AGENT` + `TRUST_AND_SAFETY` | No retry — human review required |
| Confidence Score < 0.90 (certificate) | `DO_NOT_ISSUE` + `HUMAN_REVIEW_QUEUE` | `QUALITY_CONTROL_AGENT` | No retry — escalate |
| Signing Key Unavailable (HSM/Vault) | `HALT_ALL_ISSUANCE` | `SECURITY_AGENT` + `L3_ENGINEER` + `CISO` | No retry — critical infrastructure alert |
| Credential Hash Collision | `HALT` + `SECURITY_INCIDENT_EVENT` | `SECURITY_AGENT` | No retry — forensic review required |
| Audit Log Write Failure | `HALT_ENTIRE_REQUEST` | `COMPLIANCE_AGENT` + `DBA` | Retry ×2, then HALT — no license issued without audit |
| Cross-Tenant Violation | `HALT` + `SECURITY_INCIDENT_EVENT` | `CISO` + `SECURITY_AGENT` | No retry — security review required |
| Revocation List Update Failure | `ALERT` + `FORCE_CACHE_INVALIDATION` | `OBSERVABILITY_AGENT` + `DBA` | Retry ×5, 5s backoff, then ESCALATE |
| Bulk Batch Partial Failure | `PAUSE_BATCH` + `LOG_PARTIAL` + `RESUME_FROM_CHECKPOINT` | `OBSERVABILITY_AGENT` | Resume from last checkpoint — never restart full batch |

---

## 14. INTER-AGENT DEPENDENCY MAP

### Upstream Agents (feed this agent)

```
BILLING_AGENT                     → payment confirmed, plan selected, renewal triggered
LEARNING_ENGINE                   → course completion with verified score
DOJO_INTELLIGENCE_ENGINE          → Gardner intelligence profile assessment result
SKILL_ASSESSMENT_AGENT            → skill proficiency assessment result
TENANT_PROVISIONING_AGENT         → new tenant activated, seat allocation confirmed
PROFILE_VERIFICATION_AGENT        → identity verification flags
GOVERNMENT_INTEGRATION_AGENT      → NSDC/AICTE/SSC scheme eligibility confirmed
COMPLIANCE_AGENT                  → revocation / suspension orders
PASSIVE_INTELLIGENCE_AGENT        → behavioral intelligence signals for fraud cross-check
```

### Downstream Agents (depend on this agent)

```
API_GATEWAY_AGENT (Kong)          → entitlement_vector for request gating
ENTITLEMENT_ENFORCEMENT_AGENT     → feature flag enforcement per user session
NOTIFICATION_AGENT                → license issued / expiry / revocation notifications
CREDENTIAL_REGISTRY               → credential_hash storage + public verification
OBSERVABILITY_AGENT               → performance metrics + anomaly alerts
GROWTH_ENGINE                     → XP_UPDATE_EVENT + BADGE_AWARD_TRIGGER
RANKING_AGENT                     → rank update on new intelligence certificate
BILLING_AGENT                     → renewal confirmation + grace period alerts
AUDIT_AGENT                       → all license audit records
BUSINESS_MATCHING_AGENT           → intelligence_profile_vector for career matching
CAREER_PATH_AGENT                 → intelligence type signals for career recommendations
```

### Events Emitted

```
LICENSE_ACTIVATED_EVENT           → ENTITLEMENT_ENFORCEMENT_AGENT, NOTIFICATION_AGENT
LICENSE_RENEWED_EVENT             → BILLING_AGENT, NOTIFICATION_AGENT
LICENSE_EXPIRED_EVENT             → ENTITLEMENT_ENFORCEMENT_AGENT, NOTIFICATION_AGENT, BILLING_AGENT
LICENSE_SUSPENDED_EVENT           → ENTITLEMENT_ENFORCEMENT_AGENT, NOTIFICATION_AGENT
LICENSE_REVOKED_EVENT             → ENTITLEMENT_ENFORCEMENT_AGENT, CREDENTIAL_REGISTRY, NOTIFICATION_AGENT
CERTIFICATE_ISSUED_EVENT          → CREDENTIAL_REGISTRY, GROWTH_ENGINE, NOTIFICATION_AGENT
CREDENTIAL_REGISTRY_UPDATE        → CREDENTIAL_REGISTRY
INTELLIGENCE_PROFILE_UPDATE       → BUSINESS_MATCHING_AGENT, CAREER_PATH_AGENT
FRAUD_ALERT_EVENT                 → COMPLIANCE_AGENT, TRUST_AND_SAFETY
SECURITY_INCIDENT_EVENT           → SECURITY_AGENT, CISO
XP_UPDATE_EVENT                   → GROWTH_ENGINE
BADGE_AWARD_TRIGGER               → GROWTH_ENGINE
SHARE_TRIGGER_EVENT               → GROWTH_ENGINE (for high-level intelligence certificates L8+)
BULK_BATCH_COMPLETE_EVENT         → NOTIFICATION_AGENT, OBSERVABILITY_AGENT
SIGNING_KEY_ROTATION_EVENT        → AUDIT_AGENT, SECURITY_AGENT
```

---

## 15. PASSIVE INTELLIGENCE COMPATIBILITY

When issuing an Intelligence Certificate, the agent must emit a feature vector to `FEATURE_STORE_AGENT` for passive learning system enrichment.

```json
{
  "user_id":        "entity_id",
  "feature_name":   "intelligence_certificate_signal",
  "feature_value": {
    "certificate_type":       "INTELLIGENCE_CERT",
    "intelligence_type":      "INTERPERSONAL",
    "intelligence_level":     "LEVEL_7",
    "score":                  72,
    "score_vs_passive_delta": 8,
    "assessment_attempt":     2,
    "session_duration_sec":   1840,
    "domain_track":           "ARTS"
  },
  "timestamp":      "ISO 8601 UTC",
  "source_agent":   "LICENSE_GENERATION_AGENT"
}
```

---

## 16. INNOVATION ECONOMY COMPATIBILITY

When issuing an **Innovation Certificate** (from the Idea DNA / Innovation Economy engine):

```json
{
  "emit_type":          "INNOVATION_LICENSE_ISSUED",
  "idea_vector":        "embedding hash of the idea",
  "similarity_hash":    "SHA-256 of idea content",
  "originality_score":  "0.0–1.0",
  "certificate_id":     "UUID",
  "royalty_eligible":   "true | false"
}
```

Compatible with: `IDEA_DNA_AGENT`, `ROYALTY_ENGINE`, `COPY_DETECTION_ENGINE`.

---

## 17. GROWTH ENGINE HOOK

| Event | Trigger Condition | Payload |
|---|---|---|
| **XP_UPDATE_EVENT** | New certificate issued (any type) | `+100 XP` for Skill/Course Cert, `+200 XP` for Intelligence Cert Level 5+, `+500 XP` for Level 8+ |
| **BADGE_AWARD_TRIGGER** | Intelligence Cert Level 7+ in any type | Award intelligence badge for that type |
| **RANK_UPDATE_EVENT** | Intelligence Cert issued or renewed | Trigger leaderboard re-rank at institution scope |
| **SHARE_TRIGGER_EVENT** | Intelligence Cert Level 8+ or Skill Cert EXPERT issued | Prompt user to share public achievement card |

---

## 18. PERFORMANCE MONITORING

| Metric | Target / Alert Threshold |
|---|---|
| **License Issuance Success Rate** | Target > 99.8%. Alert if < 99.5%. |
| **Certificate Issuance Success Rate** | Target > 99.9%. Alert if < 99.7%. (higher bar — legal documents) |
| **Error Rate** | Alert if > 0.2% over 5-min window. Critical if > 1%. |
| **Latency P50** | < 120ms |
| **Latency P95** | < 500ms. Alert if breaches 800ms. |
| **Latency P99** | < 1,200ms |
| **Signing Latency** | < 80ms additional per operation. Alert if > 150ms. |
| **Drift Indicator** | Weekly: compare fraud detection rate vs baseline. Alert if delta > 3%. |
| **Anomaly Frequency** | Alert if fraud flags fire > 50 times/hour for any single tenant. |
| **Revocation List Freshness** | Max staleness: 30 seconds. Alert if exceeds 60 seconds. |
| **Credential Hash Collision Rate** | Must be 0. Any collision = CRITICAL INCIDENT. |
| **Batch Job Success Rate** | > 99.5% per batch. Alert on any partial failure. |
| **Integration Stack** | Metrics → Prometheus · Dashboards → Grafana · Traces → Jaeger · Logs → Loki |

---

## 19. DATABASE SCHEMA (POSTGRESQL 15 — LOCKED)

```sql
-- Subscription Licenses
CREATE TABLE licenses (
  license_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id            UUID NOT NULL,
  entity_id            UUID NOT NULL,
  entity_type          VARCHAR(32) NOT NULL,
  license_type         VARCHAR(32) NOT NULL,
  license_tier         VARCHAR(64) NOT NULL,
  domain_track         VARCHAR(32) NOT NULL,
  status               VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  seat_count           INTEGER,
  entitlement_vector   JSONB NOT NULL,
  billing_reference    UUID,
  credential_hash      VARCHAR(64) NOT NULL,
  issuer_signature     VARCHAR(128) NOT NULL,
  signing_key_version  VARCHAR(32) NOT NULL,
  issued_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
  valid_from           TIMESTAMPTZ NOT NULL,
  valid_until          TIMESTAMPTZ,
  renewal_due_date     TIMESTAMPTZ,
  created_at           TIMESTAMPTZ DEFAULT now()
);

-- Certificates (Intelligence / Skill / Course / Trainer)
CREATE TABLE certificates (
  certificate_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id             UUID NOT NULL,
  entity_id             UUID NOT NULL,
  certificate_type      VARCHAR(32) NOT NULL,
  domain_track          VARCHAR(32) NOT NULL,
  certificate_data      JSONB NOT NULL,
  assessment_reference  UUID NOT NULL,
  status                VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  credential_hash       VARCHAR(64) NOT NULL UNIQUE,
  issuer_signature      VARCHAR(128) NOT NULL,
  signing_key_version   VARCHAR(32) NOT NULL,
  issued_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
  valid_until           TIMESTAMPTZ,
  public_display_flag   BOOLEAN DEFAULT true,
  badge_reference       UUID
);

-- License State Log (immutable — no UPDATE/DELETE)
CREATE TABLE license_state_log (
  log_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  audit_reference      UUID NOT NULL,
  license_id           UUID,
  certificate_id       UUID,
  tenant_id            UUID NOT NULL,
  actor_id             UUID NOT NULL,
  operation_type       VARCHAR(32) NOT NULL,
  previous_state       VARCHAR(32),
  new_state            VARCHAR(32) NOT NULL,
  input_hash           VARCHAR(64) NOT NULL,
  output_hash          VARCHAR(64) NOT NULL,
  model_version        VARCHAR(32) NOT NULL,
  confidence_score     DECIMAL(4,3),
  anomaly_flags        TEXT[],
  decision_path        TEXT NOT NULL,
  processing_time_ms   INTEGER NOT NULL,
  signing_key_version  VARCHAR(32) NOT NULL,
  logged_at            TIMESTAMPTZ DEFAULT now()
);
-- IMMUTABLE: No UPDATE or DELETE permitted on license_state_log

-- Revocation Registry
CREATE TABLE revocation_registry (
  revocation_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  license_id           UUID,
  certificate_id       UUID,
  tenant_id            UUID NOT NULL,
  reason               VARCHAR(255) NOT NULL,
  revoked_by           UUID NOT NULL,
  revoked_at           TIMESTAMPTZ DEFAULT now(),
  effective_at         TIMESTAMPTZ NOT NULL
);
-- Synced to Redis for fast API Gateway checks (TTL: 30s)

-- Credential Registry (public verification)
CREATE TABLE credential_registry (
  registry_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  credential_hash      VARCHAR(64) NOT NULL UNIQUE,
  certificate_id       UUID NOT NULL,
  entity_id            UUID NOT NULL,
  certificate_type     VARCHAR(32) NOT NULL,
  status               VARCHAR(32) NOT NULL DEFAULT 'VALID',
  registered_at        TIMESTAMPTZ DEFAULT now(),
  verification_url     TEXT NOT NULL
);

-- Batch Jobs
CREATE TABLE license_batch_jobs (
  batch_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id            UUID NOT NULL,
  operation_type       VARCHAR(32) NOT NULL,
  total_count          INTEGER NOT NULL,
  processed_count      INTEGER DEFAULT 0,
  failed_count         INTEGER DEFAULT 0,
  status               VARCHAR(32) DEFAULT 'PENDING',
  checkpoint_entity_id UUID,
  created_at           TIMESTAMPTZ DEFAULT now(),
  completed_at         TIMESTAMPTZ
);
```

---

## 20. API CONTRACTS (OPENAPI 3.1 — LOCKED)

| Endpoint | Description |
|---|---|
| `POST /licenses/issue` | Issue new subscription license. Auth: JWT + `LICENSE_GENERATE`. |
| `POST /licenses/renew` | Renew existing license. Auth: JWT + `LICENSE_RENEW`. |
| `POST /licenses/revoke` | Revoke a license. Auth: JWT + `LICENSE_REVOKE` (elevated). |
| `POST /licenses/suspend` | Suspend a license. Auth: JWT + `LICENSE_REVOKE`. |
| `GET /licenses/{license_id}` | Get license details. Auth: JWT + entity owner or admin. |
| `GET /licenses/entity/{entity_id}` | Get all licenses for an entity. Auth: JWT + owner or admin. |
| `GET /licenses/entitlement/{entity_id}` | Get active entitlement vector (used by API Gateway). Auth: Service account. |
| `POST /certificates/issue` | Issue intelligence / skill / course certificate. Auth: JWT + `CERT_ISSUE`. |
| `GET /certificates/{certificate_id}` | Get certificate. Auth: JWT or public (if `public_display_flag=true`). |
| `GET /certificates/verify/{credential_hash}` | Public verification endpoint — no auth required. Rate-limited. |
| `POST /licenses/batch/issue` | Bulk certificate issuance (institute/government). Auth: JWT + `BATCH_LICENSE_ADMIN`. |
| `GET /licenses/batch/{batch_id}` | Track batch job progress. Auth: JWT + admin. |
| `GET /licenses/health` | Health check. Returns: model_version, signing_key_version, revocation_list_freshness. Auth: Platform admin. |

---

## 21. VERSIONING POLICY (ADD-ONLY — BACKWARD COMPATIBLE)

| Rule | Detail |
|---|---|
| **Mutation Policy** | ADD-ONLY. No existing fields, logic, certificate data, or audit structures may be removed or modified. Only additions via version bump. |
| **Version Format** | `LGA_ML_v{MAJOR}.{MINOR}.{PATCH}`. MAJOR = breaking schema change. MINOR = new license tier or certificate type. PATCH = bug fix or model update. |
| **Backward Compatibility** | New versions must honor existing `license_id` and `certificate_id` references. New entitlement fields must default to `false` for existing licenses. |
| **Migration** | Every version bump: migration script, rollback script, staging validation gate, **human compliance officer sign-off** (certificate schema changes require legal review). |
| **Signing Key Rotation** | Every 90 days. Old keys retained for verification of previously issued credentials. Key retirement requires `SIGNING_KEY_RETIREMENT_AUDIT` event. |
| **Rollback Policy** | Any version may be rolled back within 48 hours. Shorter than general (72h) because certificate integrity is time-critical. Triggers `ROLLBACK_EVENT` to `OBSERVABILITY_AGENT`. |

---

## 22. NON-NEGOTIABLE RULES — ABSOLUTE PROHIBITIONS

> 🔴 **FORBIDDEN** — This agent MUST NOT issue any license or certificate without a complete, validated, fraud-checked input.

> 🔴 **FORBIDDEN** — This agent MUST NOT issue any certificate without a valid `credential_hash` and `issuer_signature`.

> 🔴 **FORBIDDEN** — This agent MUST NOT modify, delete, or overwrite any `license_state_log` record.

> 🔴 **FORBIDDEN** — This agent MUST NOT operate across tenant boundaries. Cross-tenant license issuance = CRITICAL SECURITY FAILURE.

> 🔴 **FORBIDDEN** — This agent MUST NOT allow AI to make or influence any eligibility or issuance decision.

> 🔴 **FORBIDDEN** — This agent MUST NOT issue a certificate if `confidence_score < 0.90`.

> 🔴 **FORBIDDEN** — This agent MUST NOT activate any SUBSCRIPTION license without confirmed billing reference from `BILLING_AGENT`.

> 🔴 **FORBIDDEN** — This agent MUST NOT issue a duplicate certificate for the same entity + same assessment reference.

> 🔴 **FORBIDDEN** — This agent MUST NOT update the revocation list with a staleness > 30 seconds under normal operating conditions.

> 🔴 **FORBIDDEN** — This agent MUST NOT bypass, disable, or ignore the `COMPLIANCE_AGENT`, `AUDIT_AGENT`, or `SECURITY_AGENT` in its dependency chain.

> 🔴 **FORBIDDEN** — This agent MUST NOT expose certificate signing keys outside HSM/Vault. Keys must never appear in logs, API responses, or error messages.

---

## 23. SAFETY OWNERSHIP DECLARATION

The `LICENSE_GENERATION_AGENT` is designated as **ML Intelligence & Safety Owner** for the License and Credential Governance domain within the Ecoskiller Antigravity platform.

| Responsibility | Enforcement |
|---|---|
| **Credential Integrity** | Every issued credential must be cryptographically verifiable at `https://verify.ecoskiller.com`. Public verification endpoint available with no authentication required. |
| **Fraud Prevention** | Continuous monitoring of assessment signal consistency. Any anomaly score above threshold → automatic hold, no auto-issuance. Human review mandatory. |
| **Fairness in Certification** | Monthly audit: compare certificate issuance rates across domain tracks, user groups, and intelligence types. Alert if any group shows > 15% systematic deviation without educational basis. |
| **Consent Compliance** | Intelligence Certificates may only be issued for users who have explicitly consented to intelligence profiling (separate consent from general platform consent). |
| **Minor Protection** | School students (Grade 6–10, user types 1–2): parental consent required before intelligence certificate issuance. `PARENT_CONSENT_CHECK` mandatory. |
| **Data Minimization** | License and certificate operations access only the minimum required fields. Intelligence scores are never logged in raw form in access logs — only hashed references. |
| **Right to Revoke** | Any user may request revocation of their own certificates at any time. Revocation is processed within 24 hours. Revoked credentials immediately invalidated in public verification portal. |
| **Government Compliance** | Government Scheme Certificates must comply with the issuing authority's (NSDC/AICTE/SSC/UGC) data retention and format requirements. `GOVERNMENT_INTEGRATION_AGENT` is the authoritative source. |
| **Signing Key Safety** | Signing key compromise → immediate `SIGNING_KEY_INCIDENT` event. All licenses and certificates issued with the compromised key must be reviewed and potentially re-issued. |
| **Incident Response** | Any credential integrity failure, signing key anomaly, or cross-tenant violation must be escalated within **10 minutes** to the designated human safety owner (CISO + Compliance Officer). |

---

---

> ## 🔒 AGENT SEALED & LOCKED
>
> **LICENSE_GENERATION_AGENT · ECOSKILLER ANTIGRAVITY**
>
> Version: `v1.0.0` · Status: `FINAL` · Mutation Policy: `ADD-ONLY`
>
> Execution Lane: `Lane D — Governance`
>
> Safety Owner: `CISO + Compliance Officer`
>
> All 23 sections enforced. No creative interpretation permitted. No assumption filling. No partial outputs.
>
> **Violation → STOP EXECUTION → LOG INCIDENT → ESCALATE → NO COMPLETION CLAIM PERMITTED**
