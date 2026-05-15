# 🔒 TENANCY_GOVERNANCE_AGENT.md
## ECOSKILLER — ANTIGRAVITY EXECUTION PROMPT
### Status: SEALED · LOCKED · DETERMINISTIC · ANTIGRAVITY_COMPATIBLE

---

```
PROMPT_CLASS              = TENANCY_GOVERNANCE_AGENT
EXECUTION_ENGINE          = ANTIGRAVITY
SCOPE                     = MULTI-TENANT LIFECYCLE · ISOLATION · ONBOARDING ·
                            BRANDING · DOMAIN · ENCRYPTION · QUOTA · OFFBOARDING
ARCHITECTURE_AUTHORITY    = PRE_APPROVED_CONSTITUTION (ECOSKILLER v12.0+)
MUTATION_POLICY           = ADD_ONLY
ASSUMPTION_POLICY         = FORBIDDEN
CREATIVE_INTERPRETATION   = FORBIDDEN
IMPLICIT_BEHAVIOR         = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY
FAILURE_MODE              = HARD_STOP → REPORT → NO_PARTIAL_OUTPUT
DUPLICATION_POLICY        = FORBIDDEN
CONFLICT_POLICY           = DENY
AI_APPROVAL_RIGHTS        = NONE
CROSS_TENANT_ACCESS       = SECURITY_BREACH
```

---

## ⚠️ ANTIGRAVITY BINDING DECLARATION

> This agent prompt is **PERMANENTLY LOCKED** to the ECOSKILLER Master Constitution v12.0.
> Antigravity MUST NOT reinterpret, simplify, creatively extend, merge tenant types, or shortcut
> any isolation boundary, lifecycle rule, or governance workflow described herein.
> **Any deviation = STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT.**

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME              = TENANCY_GOVERNANCE_AGENT
AGENT_CLASS             = INFRASTRUCTURE · IDENTITY · ISOLATION · COMPLIANCE
PLATFORM                = ECOSKILLER (Enterprise Multi-Tenant SaaS)
EXECUTION_LANE          = A — Foundation (Identity · RBAC · Tenancy · API Gateway · Event Registry)
CONTRACT_GATE_OUTPUT    = identity_ready · rbac_ready · event_schema_ready
DEPENDENCY_CONSUMERS    = Lane B (db_ready) · Lane C (api_contract_ready) · Lane D (governance_ready)
ARCHITECTURE_POSITION   = FOUNDATION LAYER — must be operational before ALL other services
```

**The Tenancy Governance Agent is the zero-dependency foundation upon which every other**
**ECOSKILLER service is built. No service may claim identity_ready without this agent passing.**

---

## 2️⃣ PLATFORM TENANCY MODEL (LOCKED)

### 2.1 Platform Identity

```
PLATFORM_TYPE       = ENTERPRISE MULTI-TENANT SAAS
MULTI_TENANT_FROM   = DAY ZERO (no single-tenant mode exists)
TENANT_ISOLATION    = HARD (not soft, not logical-only — cryptographically enforced)
SHARED_TENANTS      = ABSOLUTELY FORBIDDEN
SHARED_ADMIN        = FORBIDDEN
SHARED_ANALYTICS    = FORBIDDEN
CROSS_TENANT_READ   = FORBIDDEN
CROSS_TENANT_WRITE  = FORBIDDEN
```

### 2.2 Tenant Type Registry (LOCKED)

```
TENANT_TYPES:

  INDIVIDUAL
    Description : Single professional or freelance user operating as a tenant unit
    Scope       : Personal job profile, skill development, Dojo participation
    Admin Roles : Self-admin only
    Billing     : Individual plan (FREE | BASIC | PRO)

  COMPANY (SME + Corporate)
    Description : Verified business entity — SME or enterprise corporate
    Scope       : Job posting, recruiter portal, employee management, hiring ERP
    Admin Roles : Company Admin → HR Manager → Recruiter
    Verification: Company domain email + manual verification (humans only)
    Billing     : ENTERPRISE plan · seat-based licensing
    Sub-Types   : SME (≤200 employees) | CORPORATE (>200 employees, L1–L7 hierarchy)

  INSTITUTION (Schools · Colleges · Universities)
    Description : Verified educational institution
    Scope       : Student enrollment, placement ERP, trainer/mentor management, Dojo
    Admin Roles : Principal → HOD → Professor → Staff → TPO
    Verification: Institution email domain + accreditation check (humans only)
    Billing     : ENTERPRISE plan · seat-based (student + faculty seats)
    Sub-Types   : SCHOOL | COLLEGE | UNIVERSITY | TRAINING_INSTITUTE

  RECRUITER_AGENCY
    Description : Third-party staffing or recruitment firm
    Scope       : Access to job marketplace, candidate pipeline, Dojo evaluation
    Admin Roles : Agency Admin → Senior Recruiter → Recruiter
    Verification: GST/Business registration + domain verification
    Billing     : PRO | ENTERPRISE plan

RULE: Tenant type is immutable once verified. Type change requires offboarding + re-onboarding.
RULE: Institute ≠ Company ≠ Platform — no role or data sharing across types.
```

### 2.3 Domain Track Binding (HARD LOCK)

```
DOMAIN_TRACKS = Arts | Commerce | Science | Technology | Administration

RULES:
  - Every tenant must declare supported domain tracks at onboarding
  - Cross-domain data access FORBIDDEN unless explicitly multi-domain tenant
  - Arts users cannot authenticate into Science tenant context
  - Domain isolation enforced at: API Gateway · Service Layer · DB Query Layer
  - Domain leaks = SECURITY FAILURE
  - Admins require explicit multi-domain grant (written into RBAC policy)
```

---

## 3️⃣ TENANT LIFECYCLE (DETERMINISTIC STATE MACHINE)

### 3.1 Lifecycle States

```
PENDING_VERIFICATION
  ↓ (human verifies domain + org type)
VERIFIED_INACTIVE
  ↓ (tenant admin accepts + completes onboarding wizard)
ACTIVE
  ↓ (admin or platform action)
SUSPENDED (read-only access, no new operations, billing paused)
  ↓ (reinstatement or escalation)
ACTIVE  ←→  SUSPENDED (reversible)
  ↓ (platform or compliance order)
OFFBOARDING
  ↓ (data purge complete, grace period elapsed)
TERMINATED (immutable final state)
```

### 3.2 State Transition Rules

```
PENDING_VERIFICATION → VERIFIED_INACTIVE
  Trigger : Human verification officer approves domain + org
  Guard   : No AI auto-approval of tenant verification
  Action  : Tenant record created · onboarding invitation sent

VERIFIED_INACTIVE → ACTIVE
  Trigger : Tenant admin completes onboarding wizard + billing setup
  Guard   : Subscription plan activated · MFA configured
  Action  : Tenant namespace provisioned · RBAC initialized · DEK generated

ACTIVE → SUSPENDED
  Trigger : Payment failure (>7 days grace) | Compliance violation | Admin action
  Guard   : User data preserved · audit trail continued
  Action  : All logins blocked · API access denied · billing paused

SUSPENDED → ACTIVE
  Trigger : Payment cleared | Compliance resolved | Admin reinstatement
  Guard   : MFA re-verified by tenant admin
  Action  : Access restored · billing resumed

ACTIVE / SUSPENDED → OFFBOARDING
  Trigger : Tenant-initiated cancellation | Platform termination order
  Guard   : 30-day notice period mandatory | Data export offered
  Action  : Read-only access for 30 days · data export workflow triggered

OFFBOARDING → TERMINATED
  Trigger : Grace period elapsed | Tenant confirms data export complete
  Guard   : Hard deletion of all tenant data executed
  Action  : Encryption keys revoked · Tenant record anonymized · Audit retained 7 years

RULE: State transitions are LOGGED, IRREVERSIBLE once in TERMINATED state.
RULE: AI agents CANNOT trigger SUSPENDED, OFFBOARDING, or TERMINATED transitions.
RULE: TERMINATED tenants cannot be reactivated — new tenant registration required.
```

---

## 4️⃣ TENANT ONBOARDING WORKFLOW (MANDATORY)

### 4.1 Stage-by-Stage Onboarding

```
STAGE 1 — REGISTRATION (Self-Service)
  Input  : Org name · Org type · Admin email · Domain · Country
  Output : Pending verification record created
  System : Sends verification request to Platform Ops queue
  AI     : May pre-fill non-sensitive suggestions only (NOT submit)

STAGE 2 — DOMAIN VERIFICATION (Human-Gated)
  Trigger : DNS TXT record verification OR email verification via official domain
  Guard   : Human verification officer must approve (cannot be automated)
  Output  : Tenant status → VERIFIED_INACTIVE
  Block   : No feature access until this stage passes

STAGE 3 — ADMIN ACCOUNT SETUP
  Steps  :
    3a. Tenant Admin creates password (policy enforced)
    3b. MFA enrollment (mandatory — TOTP or hardware key)
    3c. Recovery codes generated + acknowledged
  Output : Admin identity_ready = TRUE

STAGE 4 — SUBSCRIPTION ACTIVATION
  Steps  :
    4a. Plan selected (FREE | BASIC | PRO | ENTERPRISE)
    4b. Billing method added (Stripe)
    4c. Seats configured (if ENTERPRISE)
    4d. First invoice generated
  Output : billing_ready = TRUE

STAGE 5 — TENANT NAMESPACE PROVISIONING (Automated)
  Actions:
    5a. Kubernetes namespace created (k8s: core/tenant-{id})
    5b. PostgreSQL schema isolated (Row-Level Security enabled)
    5c. Redis key namespace partitioned (tenant:{id}:*)
    5d. MinIO bucket created (tenant-{id}-data, encrypted)
    5e. DEK (Data Encryption Key) generated via KMS
    5f. Elasticsearch index created (tenant-{id}-search)
    5g. Kafka topic prefix assigned (tenant.{id}.*)
  Output : infrastructure_ready = TRUE

STAGE 6 — DOMAIN TRACKS DECLARED
  Input  : Tenant declares supported academic/business domain tracks
  Output : Domain isolation policy written to RBAC engine
  Guard  : Domain declarations locked (require Super Admin to modify post-activation)

STAGE 7 — BRANDING CONFIGURATION
  Input  :
    7a. Logo upload (PNG, SVG — validated)
    7b. Primary color (hex — contrast-checked for WCAG 2.1 AA)
    7c. Subdomain preference (if custom domain enabled on plan)
    7d. Email sender name + reply-to
  Output : brand_ready = TRUE

STAGE 8 — ROLE & PERMISSION INITIALIZATION
  Actions:
    8a. Default roles seeded for tenant type (from role template registry)
    8b. Tenant Admin assigned all tenant-scoped permissions
    8c. RBAC policy written to authorization engine
  Output : rbac_ready = TRUE

STAGE 9 — ONBOARDING WIZARD COMPLETE
  Checklist validation:
    ✅ identity_ready
    ✅ billing_ready
    ✅ infrastructure_ready
    ✅ rbac_ready
    ✅ brand_ready
    ✅ domain_declared
  Output : Tenant status → ACTIVE
  Event  : tenant.activated published to Kafka
```

### 4.2 Onboarding Rules

```
RULE: No feature access until STAGE 5 complete
RULE: Stages execute sequentially — no skipping
RULE: AI agents may assist data entry but CANNOT approve any stage
RULE: Every stage transition audit-logged with actor_id + timestamp
RULE: Failed stages DO NOT auto-retry — human intervention required for STAGE 2
```

---

## 5️⃣ TENANT ISOLATION ARCHITECTURE (HARD LOCK)

### 5.1 Isolation Layers

```
LAYER 1 — API GATEWAY ISOLATION
  Every inbound request must carry tenant_id (from JWT claim)
  API Gateway validates tenant_id before routing
  Requests without valid tenant_id → 401 UNAUTHORIZED
  Requests with mismatched tenant_id → 403 FORBIDDEN + SECURITY ALERT

LAYER 2 — SERVICE LAYER ISOLATION
  Every microservice validates tenant_id independently
  Services NEVER trust upstream tenant claims without re-validation
  Service-to-service calls include scoped service identity + tenant_id
  Services cannot impersonate another tenant's context

LAYER 3 — DATABASE ISOLATION
  PostgreSQL: Row-Level Security (RLS) enabled on ALL tenant-scoped tables
  RLS policy: WHERE tenant_id = current_tenant_id()
  Queries without tenant_id in WHERE clause → BLOCK + ALERT
  No shared sequences or auto-increment IDs across tenants

LAYER 4 — CACHE ISOLATION
  Redis keys namespaced: tenant:{tenant_id}:{resource}:{id}
  No cross-namespace Redis access
  Cache invalidation scoped to tenant_id
  Redis SCAN operations must include tenant key prefix

LAYER 5 — OBJECT STORAGE ISOLATION
  MinIO: One bucket per tenant (tenant-{id}-data)
  Bucket policy: Private + tenant-scoped presigned URLs only
  Public buckets = FORBIDDEN
  Cross-tenant object URL access = SECURITY FAILURE

LAYER 6 — SEARCH INDEX ISOLATION
  Elasticsearch: One index per tenant (tenant-{id}-{resource})
  Index alias management scoped to tenant
  No cross-index queries without explicit multi-tenant grant
  Search API validates tenant_id before query execution

LAYER 7 — ENCRYPTION KEY ISOLATION
  Each tenant has a dedicated DEK (Data Encryption Key)
  DEK wrapped by KEK (Key Encryption Key) stored in external KMS
  No shared encryption keys across tenants
  Tenant compromise → DEK rotated, NOT KEK

LAYER 8 — EVENT ISOLATION
  Kafka topics prefixed: tenant.{tenant_id}.*
  Event consumers validate tenant_id in event envelope before processing
  Cross-tenant event routing = FORBIDDEN
  Event consumers re-authorize before every state mutation
```

### 5.2 Isolation Violation Response

```
VIOLATION DETECTED → immediate actions:
  1. Request blocked (503 or 403 depending on context)
  2. Security alert published (Kafka: security.isolation.violation)
  3. Incident logged to immutable audit trail
  4. PagerDuty CRITICAL alert fired
  5. Affected tenant admin notified
  6. Platform Security Officer notified
  7. Forensic trace ID generated for investigation

ZERO TOLERANCE: A single confirmed cross-tenant data leak = PLATFORM INCIDENT
```

---

## 6️⃣ TENANT ADMIN ROLE ARCHITECTURE (LOCKED)

### 6.1 Role Hierarchy Per Tenant Type

```
INDIVIDUAL TENANT:
  Self-Admin
    - Full control of own account
    - No sub-role delegation

COMPANY TENANT:
  Company Admin (L1 — highest)
    - Full tenant control
    - Billing management
    - Employee invitation
    - Role assignment within tenant
  HR Manager (L2)
    - Job posting management
    - Recruiter assignment
    - Candidate pipeline visibility
  Recruiter (L3)
    - Job applications
    - Interview scheduling
    - Candidate communication
  Employee (L4)
    - Internal access only
    - Group participation
    - Profile + skill tracker

INSTITUTION TENANT:
  Principal / Director (L1)
    - Full institutional control
    - TPO assignment
    - Department management
  HOD (Head of Department) (L2)
    - Department-scoped control
    - Faculty management
    - Student cohort access
  Professor / Faculty (L3)
    - Course management
    - Student assessment
    - Dojo room creation
  Staff / TPO (L3 — Placement Track)
    - Student placement tracking
    - Recruiter relationship management
    - Placement analytics
  Student (L4)
    - Own profile, skill, Dojo access
    - Domain-scoped participation
  Parent (L5 — READ ONLY)
    - Child's progress visibility only
    - No mutation rights

PLATFORM LEVEL (above all tenants):
  Super Admin
    - Cross-tenant management
    - MFA mandatory + IP allowlist
    - IP allowlisted sessions only
    - All actions audit-logged
  Platform Ops
    - Monitoring + incident response
    - No direct data access
  Compliance Officer
    - Audit export + legal hold
    - Read-only across all tenants
  Automation / AI Agent
    - Scoped permissions only
    - No tenant approval rights
    - Time-bound execution
    - Cannot modify RBAC
```

### 6.2 RBAC Enforcement Rules

```
RULE: Roles define MAXIMUM capability — never automatic access
RULE: RBAC alone is insufficient — ABAC must also return ALLOW
RULE: No hardcoded roles in application code (declared in RBAC engine only)
RULE: Cross-tenant role inheritance = FORBIDDEN
RULE: Tenant Admin cannot grant permissions they do not hold
RULE: AI agents have no UI access and no human role equivalence
RULE: Role changes require MFA + audit log entry
RULE: Role assignments expire if MFA not confirmed within 24 hours
```

---

## 7️⃣ ABAC ENFORCEMENT LAYER (MANDATORY)

Every authorization decision MUST pass ALL dimensions:

```
DIMENSION         CHECK
--------------    --------------------------------------------------
tenant_match      requested resource belongs to actor's tenant
domain_match      actor's domain track matches resource domain
group_membership  actor is member of required group (where applicable)
cert_status       certification valid (where feature requires it)
content_visibility content visibility policy allows access
resource_state    resource is in ACTIVE (not DRAFT/ARCHIVED/DELETED)
plan_entitlement  actor's subscription plan allows the feature
session_valid     actor's session is live and not expired
risk_score        risk score below threshold for sensitive operations

ALL dimensions must return ALLOW — single DENY = full block
```

---

## 8️⃣ TENANT BRANDING SYSTEM (LOCKED)

### 8.1 Branding Scope Per Plan

```
FREE / BASIC:
  - No custom branding
  - ECOSKILLER default theme applied
  - Platform logo displayed

PRO:
  - Custom logo (uploaded, validated)
  - Primary color customization (WCAG contrast enforced)
  - Custom email sender name

ENTERPRISE:
  - Full white-label capability
  - Custom subdomain (tenant.ecoskiller.com or custom domain)
  - Custom logo + color system
  - Custom email domain
  - Custom login page
  - Custom onboarding copy
  - Brand token applied to Flutter + React UI
```

### 8.2 Branding Rules

```
RULE: Branding config stored in tenant_branding table (tenant-isolated)
RULE: Logo dimensions: min 200×200px, max 2MB, PNG/SVG only
RULE: Color hex validated for WCAG 2.1 AA contrast ratio (≥4.5:1 on white/dark backgrounds)
RULE: Custom domain requires DNS verification + SSL provisioning (humans execute, AI generates config)
RULE: Branding changes → audit-logged with actor_id + previous/new values
RULE: ECOSKILLER platform attribution must be visible on FREE/BASIC plans
RULE: Branding rollback available (last 5 versions retained)
```

---

## 9️⃣ TENANT VERIFICATION SYSTEM (LOCKED)

### 9.1 Institution Verification

```
REQUIRED:
  - Registered institution name (matches government records)
  - Official .edu / institution domain
  - Principal/Director contact (official email)
  - Accreditation body (NAAC/AICTE/UGC or equivalent)
  - Physical address (verified via map lookup)

VERIFICATION STEPS:
  1. DNS TXT record placed on institution domain (auto-generated challenge)
  2. OR Admin clicks verification link sent to official email
  3. Platform Ops officer manually cross-checks accreditation
  4. Approval by human verification officer
  5. Institution badge granted: ✅ Verified Institution

RULE: AI cannot approve institution verification
RULE: Pending verifications expire after 30 days
RULE: Re-verification required if domain changes
```

### 9.2 Company Verification

```
REQUIRED:
  - Registered company name (CIN/GST/Business registration number)
  - Official company domain
  - Company Admin contact (official company email)
  - Business category (SME / Corporate / Startup)
  - Country of incorporation

VERIFICATION STEPS:
  1. DNS TXT record on company domain OR official email verification
  2. GST / CIN number validated against government database (API integration)
  3. Human Platform Ops officer confirms
  4. Company badge granted: ✅ Verified Company

RULE: SME < 200 employees · Corporate ≥ 200 employees (self-declared, admin responsibility)
RULE: Domain mismatch between registration and email = HOLD for manual review
```

### 9.3 Recruiter Agency Verification

```
REQUIRED:
  - Agency name + registration number
  - Agency domain
  - Primary recruiter contact
  - Reference client list (optional but accelerates approval)

VERIFICATION:
  - Same domain verification flow
  - Human approval mandatory
```

---

## 🔟 TENANT ENCRYPTION ISOLATION (MANDATORY)

### 10.1 Key Architecture Per Tenant

```
ARCHITECTURE: ENVELOPE ENCRYPTION MODEL

  Tenant Data
      ↓ encrypted by
  DEK (Data Encryption Key)  ← unique per tenant, stored encrypted
      ↓ encrypted by
  KEK (Key Encryption Key)   ← stored in external KMS (never in application)

ALGORITHM: AES-256-GCM (mandatory)
FORBIDDEN: AES-128, DES, 3DES, RC4, XOR, custom crypto

DEK GENERATION:
  - Generated at tenant ACTIVE transition (Stage 5 of onboarding)
  - Generated by KMS, never by application code
  - Never logged, never exported, never hardcoded

DEK ROTATION TRIGGERS:
  - Security incident affecting tenant
  - Annual rotation schedule
  - Tenant-initiated rotation (ENTERPRISE plan)
  - Domain migration event
```

### 10.2 Domain-Aware Key Separation

```
Within a multi-domain tenant:
  Arts data   → encrypted under DEK-arts-{tenant_id}
  Commerce data → encrypted under DEK-commerce-{tenant_id}
  Science data  → encrypted under DEK-science-{tenant_id}

Cross-domain decryption = FORBIDDEN without explicit policy grant
Re-encryption under target domain key required for cross-domain reads
```

### 10.3 Tenant Key Revocation (Offboarding)

```
On TERMINATED state:
  1. DEK revocation request sent to KMS
  2. All tenant data becomes permanently inaccessible
  3. KMS confirms revocation (logged)
  4. Backup tapes containing tenant DEK flagged for destruction cycle
  5. Revocation audit entry written to immutable compliance ledger
```

---

## 1️⃣1️⃣ TENANT QUOTA & RESOURCE GOVERNANCE (LOCKED)

### 11.1 Per-Tenant Resource Quotas

```
RESOURCE                    FREE        BASIC       PRO         ENTERPRISE
------------------------    --------    --------    --------    -----------
Active Users (seats)        1           50          500         Custom
Job Postings                0           10          Unlimited   Custom
Dojo Rooms (active)         0           3/month     Unlimited   Custom
Storage (MinIO)             100 MB      5 GB        50 GB       Custom
API Calls (daily)           100         5,000       50,000      Custom
Mentor Pool Size            0           5           50          Custom
Skill Catalogs              Platform    Platform    +Tenant     Full Custom
Kafka Event Rate            50/min      500/min     5,000/min   Custom
Elasticsearch Indexes       0           1           5           Custom
Recording Storage           0           10 GB       100 GB      Custom
```

### 11.2 Quota Enforcement Rules

```
RULE: Per-tenant CPU + memory quotas enforced at Kubernetes namespace level
RULE: Per-tenant request concurrency caps enforced at API Gateway (Redis rate limiter)
RULE: Tenant throttling occurs BEFORE global throttling
RULE: Large tenants CANNOT degrade student discussion quality for other tenants
RULE: Quota alerts sent at 80% utilization to tenant admin + Platform Ops
RULE: At 100% quota → feature blocked with upgrade prompt (NOT silent degradation)
RULE: Quota reset on billing cycle renewal date (UTC midnight)
```

### 11.3 Tenant Performance Isolation

```
Per-tenant SLO monitoring:
  API P95 latency per tenant tracked independently
  If tenant A's load causes P95 > 1s for tenant B → ISOLATION FAILURE ALERT

Degradation order (tenant in violation, never others):
  1. Analytics + reports throttled
  2. Search facets reduced
  3. Admin operations queued

PROTECTED (never shed for any tenant):
  - Student discussions (Dojo)
  - Mentor controls
  - Scoring & assessments
  - Authentication flows
```

---

## 1️⃣2️⃣ TENANT CUSTOM DOMAIN & SSO (ENTERPRISE ONLY)

### 12.1 Custom Domain

```
ELIGIBILITY: ENTERPRISE plan only

PROVISIONING STEPS:
  1. Tenant Admin requests custom domain (e.g., learn.acmecorp.com)
  2. Tenant adds CNAME record pointing to ecoskiller edge
  3. Platform validates DNS propagation
  4. SSL certificate provisioned automatically (Let's Encrypt / ACM)
  5. Domain status: ACTIVE
  6. Flutter + React SEO layer updated to serve tenant domain

RULES:
  - Domain ownership verified by DNS TXT or CAA record
  - SSL auto-renewed 30 days before expiry
  - Human DevOps executes DNS + SSL configuration
  - AI generates configuration templates only
  - Domain breach → immediate suspension + incident
```

### 12.2 Enterprise SSO

```
SUPPORTED PROTOCOLS: SAML 2.0 | OIDC (OpenID Connect)
IDENTITY PROVIDERS  : Google Workspace | Microsoft Entra ID | Okta | Generic SAML

PROVISIONING STEPS:
  1. Tenant Admin provides IdP metadata URL or XML
  2. Platform generates SP metadata for tenant
  3. Test SSO flow in staging
  4. Human approval by Platform Ops
  5. SSO enabled for tenant domain

RULES:
  - SSO does NOT bypass MFA requirements
  - Just-in-time (JIT) provisioning supported
  - Local password login disabled when SSO enforced (configurable)
  - SSO failures fallback to local auth (configurable per tenant)
  - SSO config changes require MFA + audit log
  - Institution SSO supported (school Active Directory / Google EDU)
```

---

## 1️⃣3️⃣ TENANT SKILL CATALOGS & MENTOR POOLS (LOCKED)

### 13.1 Skill Catalog Hierarchy

```
PLATFORM CATALOG (global):
  - Maintained by ECOSKILLER Platform Ops
  - Available to all tenants
  - Cannot be deleted by tenants

TENANT CATALOG (custom):
  - Institution-specific courses, subjects, skill tracks
  - Company-specific internal competencies
  - Visible ONLY within tenant
  - Requires PRO or ENTERPRISE plan

CATALOG RULES:
  - Tenant catalog inherits platform catalog (additive only)
  - Tenant cannot modify platform catalog entries
  - Skill version changes in tenant catalog are version-controlled
  - Deleted skills move to ARCHIVED (not hard deleted — referenced in past assessments)
```

### 13.2 Mentor Pool Management

```
MENTOR POOL:
  - Each tenant has an isolated mentor pool
  - Mentors belong to ONE primary tenant (can guest in others with approval)
  - Mentor license = per-seat billing (ENTERPRISE)

POOL GOVERNANCE:
  - Tenant Admin invites mentors via official email
  - Mentor accepts → background check flow (institution-configurable)
  - Mentor verification badge assigned
  - Mentor suspension/removal by Tenant Admin only
  - Platform Super Admin can force-remove (compliance reason, audit-logged)

CROSS-TENANT MENTOR SHARING:
  - Only via explicit platform marketplace (future feature, not default)
  - Revenue split rules apply (Section R84 of master constitution)
```

---

## 1️⃣4️⃣ DATABASE SCHEMAS (MANDATORY)

```sql
-- Core Tenant Registry
CREATE TABLE tenants (
  tenant_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_name         VARCHAR(255) NOT NULL,
  tenant_type         ENUM('INDIVIDUAL','COMPANY','INSTITUTION','RECRUITER_AGENCY') NOT NULL,
  status              ENUM('PENDING_VERIFICATION','VERIFIED_INACTIVE','ACTIVE',
                          'SUSPENDED','OFFBOARDING','TERMINATED') NOT NULL DEFAULT 'PENDING_VERIFICATION',
  domain              VARCHAR(255) UNIQUE,
  country             CHAR(2) NOT NULL,
  subdomain           VARCHAR(100) UNIQUE,
  custom_domain       VARCHAR(255),
  custom_domain_status ENUM('NONE','PENDING','ACTIVE','FAILED') DEFAULT 'NONE',
  sso_enabled         BOOLEAN DEFAULT FALSE,
  sso_protocol        ENUM('SAML2','OIDC'),
  sso_idp_metadata    TEXT,
  plan_id             UUID REFERENCES subscription_plans(plan_id),
  verified_at         TIMESTAMPTZ,
  verified_by         UUID,          -- human officer (NOT AI)
  activated_at        TIMESTAMPTZ,
  suspended_at        TIMESTAMPTZ,
  offboarding_started_at TIMESTAMPTZ,
  terminated_at       TIMESTAMPTZ,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  audit_hash          CHAR(64) NOT NULL
);

-- Tenant Domain Tracks
CREATE TABLE tenant_domain_tracks (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  domain_track        ENUM('ARTS','COMMERCE','SCIENCE','TECHNOLOGY','ADMINISTRATION') NOT NULL,
  declared_at         TIMESTAMPTZ DEFAULT NOW(),
  declared_by         UUID NOT NULL,
  is_active           BOOLEAN DEFAULT TRUE,
  UNIQUE(tenant_id, domain_track)
);

-- Tenant Branding
CREATE TABLE tenant_branding (
  branding_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL UNIQUE REFERENCES tenants(tenant_id),
  logo_url            TEXT,
  primary_color_hex   CHAR(7),      -- #RRGGBB, WCAG-validated
  secondary_color_hex CHAR(7),
  email_sender_name   VARCHAR(100),
  email_reply_to      VARCHAR(255),
  login_page_copy     TEXT,
  onboarding_copy     TEXT,
  brand_version       INTEGER DEFAULT 1,
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  updated_by          UUID NOT NULL,
  audit_hash          CHAR(64)
);

-- Tenant Branding Versions (audit trail)
CREATE TABLE tenant_branding_versions (
  version_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  brand_snapshot      JSONB NOT NULL,
  version_number      INTEGER NOT NULL,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  created_by          UUID NOT NULL
);

-- Tenant Admin Roles
CREATE TABLE tenant_roles (
  role_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  role_name           VARCHAR(100) NOT NULL,
  role_class          VARCHAR(50),   -- TENANT_ADMIN | DOMAIN_ADMIN | OPERATIONAL
  permissions         JSONB NOT NULL,
  is_system_role      BOOLEAN DEFAULT FALSE,  -- seeded, cannot be deleted
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE(tenant_id, role_name)
);

-- Tenant User Membership
CREATE TABLE tenant_memberships (
  membership_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  user_id             UUID NOT NULL REFERENCES users(user_id),
  role_id             UUID NOT NULL REFERENCES tenant_roles(role_id),
  domain_track        ENUM('ARTS','COMMERCE','SCIENCE','TECHNOLOGY','ADMINISTRATION'),
  status              ENUM('INVITED','ACTIVE','SUSPENDED','REMOVED') NOT NULL DEFAULT 'INVITED',
  invited_at          TIMESTAMPTZ DEFAULT NOW(),
  joined_at           TIMESTAMPTZ,
  removed_at          TIMESTAMPTZ,
  removed_by          UUID,
  audit_hash          CHAR(64),
  UNIQUE(tenant_id, user_id)
);

-- Tenant Encryption Keys (reference only — actual keys in KMS)
CREATE TABLE tenant_encryption_keys (
  key_ref_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  domain_track        ENUM('ARTS','COMMERCE','SCIENCE','TECHNOLOGY','ADMINISTRATION','ALL'),
  kms_key_arn         TEXT NOT NULL,   -- reference to KMS, never the key itself
  key_status          ENUM('ACTIVE','ROTATING','REVOKED') DEFAULT 'ACTIVE',
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  rotated_at          TIMESTAMPTZ,
  revoked_at          TIMESTAMPTZ,
  rotation_reason     TEXT,
  UNIQUE(tenant_id, domain_track)
);

-- Tenant Quotas
CREATE TABLE tenant_quotas (
  quota_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  resource_type       VARCHAR(100) NOT NULL,   -- 'seats', 'job_postings', 'api_calls', etc.
  quota_limit         INTEGER,                 -- NULL = unlimited
  current_usage       INTEGER DEFAULT 0,
  alert_threshold_pct INTEGER DEFAULT 80,
  reset_period        ENUM('DAILY','MONTHLY','NEVER') DEFAULT 'MONTHLY',
  reset_at            TIMESTAMPTZ,
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE(tenant_id, resource_type)
);

-- Tenant Lifecycle Events (immutable append-only)
CREATE TABLE tenant_lifecycle_events (
  event_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  event_type          VARCHAR(100) NOT NULL,
  from_status         VARCHAR(50),
  to_status           VARCHAR(50),
  triggered_by        UUID NOT NULL,   -- actor_id (human or system)
  trigger_source      ENUM('HUMAN','SYSTEM','COMPLIANCE') NOT NULL,
  reason              TEXT,
  metadata            JSONB,
  occurred_at         TIMESTAMPTZ DEFAULT NOW(),
  audit_hash          CHAR(64) NOT NULL
  -- NO UPDATE OR DELETE PERMITTED
);

-- Tenant SSO Configuration
CREATE TABLE tenant_sso_configs (
  sso_id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL UNIQUE REFERENCES tenants(tenant_id),
  protocol            ENUM('SAML2','OIDC') NOT NULL,
  idp_entity_id       TEXT,
  idp_metadata_url    TEXT,
  idp_metadata_xml    TEXT,
  sp_entity_id        TEXT,
  acs_url             TEXT,
  is_active           BOOLEAN DEFAULT FALSE,
  test_passed         BOOLEAN DEFAULT FALSE,
  approved_by         UUID,            -- human Platform Ops officer
  activated_at        TIMESTAMPTZ,
  created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- Tenant Verification Records
CREATE TABLE tenant_verifications (
  verification_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  verification_type   ENUM('DNS_TXT','EMAIL_LINK','MANUAL_REVIEW') NOT NULL,
  challenge_token     TEXT,
  challenge_issued_at TIMESTAMPTZ DEFAULT NOW(),
  challenge_expires_at TIMESTAMPTZ,
  verified_at         TIMESTAMPTZ,
  verified_by         UUID,           -- human officer
  accreditation_body  VARCHAR(255),
  registration_number VARCHAR(100),
  status              ENUM('PENDING','VERIFIED','FAILED','EXPIRED') DEFAULT 'PENDING'
);

-- Tenant Skill Catalogs
CREATE TABLE tenant_skill_catalogs (
  catalog_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  skill_name          VARCHAR(255) NOT NULL,
  domain_track        ENUM('ARTS','COMMERCE','SCIENCE','TECHNOLOGY','ADMINISTRATION'),
  description         TEXT,
  version             INTEGER DEFAULT 1,
  status              ENUM('ACTIVE','ARCHIVED') DEFAULT 'ACTIVE',
  created_by          UUID NOT NULL,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE(tenant_id, skill_name, version)
);

-- Tenant Mentor Pool
CREATE TABLE tenant_mentor_pool (
  pool_entry_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  mentor_user_id      UUID NOT NULL REFERENCES users(user_id),
  status              ENUM('INVITED','ACTIVE','SUSPENDED','REMOVED') DEFAULT 'INVITED',
  invited_by          UUID NOT NULL,
  joined_at           TIMESTAMPTZ,
  removed_at          TIMESTAMPTZ,
  license_seat_id     UUID,           -- references billing seat
  audit_hash          CHAR(64),
  UNIQUE(tenant_id, mentor_user_id)
);

-- Tenant Offboarding Records
CREATE TABLE tenant_offboarding (
  offboarding_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  initiated_by        UUID NOT NULL,
  initiated_at        TIMESTAMPTZ DEFAULT NOW(),
  data_export_offered BOOLEAN DEFAULT TRUE,
  data_export_completed BOOLEAN DEFAULT FALSE,
  data_export_url     TEXT,           -- signed URL, expires in 30 days
  grace_period_ends   TIMESTAMPTZ,   -- 30 days from initiated_at
  hard_delete_scheduled_at TIMESTAMPTZ,
  hard_delete_completed_at TIMESTAMPTZ,
  dek_revoked_at      TIMESTAMPTZ,
  status              ENUM('GRACE_PERIOD','EXPORT_READY','PURGING','COMPLETE')
);
```

---

## 1️⃣5️⃣ API CONTRACT REGISTRY (MANDATORY)

```
METHOD  PATH                                        AUTH              ENTITLEMENT
------  ------------------------------------------  ----------------  -------------------------
POST    /tenants/register                           Public            None (rate-limited)
GET     /tenants/{id}/verification-status          Authenticated     own_tenant_scope
POST    /tenants/{id}/verify/dns                   Platform Ops      verification_officer
POST    /tenants/{id}/verify/approve               Platform Ops      verification_officer + MFA
POST    /tenants/{id}/activate                     Tenant Admin      own_tenant + billing_ready
GET     /tenants/{id}                              Authenticated     own_tenant_scope
PATCH   /tenants/{id}/branding                    Tenant Admin      own_tenant + branding_rights
GET     /tenants/{id}/branding                    Authenticated     own_tenant_scope
POST    /tenants/{id}/domain-tracks               Tenant Admin      own_tenant + Super Admin
POST    /tenants/{id}/sso                         Tenant Admin      enterprise_plan + own_tenant
GET     /tenants/{id}/sso                         Tenant Admin      own_tenant_scope
POST    /tenants/{id}/sso/test                    Tenant Admin      own_tenant + Platform Ops
POST    /tenants/{id}/custom-domain               Tenant Admin      enterprise_plan + own_tenant
GET     /tenants/{id}/quotas                      Tenant Admin      own_tenant_scope
GET     /tenants/{id}/members                     Tenant Admin      own_tenant_scope
POST    /tenants/{id}/members/invite              Tenant Admin      own_tenant + seat_available
DELETE  /tenants/{id}/members/{user_id}           Tenant Admin      own_tenant + MFA
POST    /tenants/{id}/suspend                     Super Admin       MFA + compliance_authority
POST    /tenants/{id}/reinstate                   Super Admin       MFA + compliance_authority
POST    /tenants/{id}/offboard                    Super Admin or Tenant Admin  MFA
GET     /tenants/{id}/offboarding/export          Tenant Admin      own_tenant + offboarding_state
GET     /tenants/{id}/skill-catalogs              Authenticated     own_tenant_scope
POST    /tenants/{id}/skill-catalogs              Tenant Admin      own_tenant + pro_plan
GET     /tenants/{id}/mentor-pool                 Tenant Admin      own_tenant_scope
POST    /tenants/{id}/mentor-pool/invite          Tenant Admin      own_tenant + seat_available
GET     /tenants/{id}/lifecycle-events            Compliance Officer  audit_access + MFA
GET     /admin/tenants                            Super Admin       MFA + platform_admin
POST    /admin/tenants/{id}/force-terminate       Super Admin       MFA + compliance_authority
```

**ALL tenant API endpoints:**
```
- Require tenant_id in JWT claim (validated independently at service layer)
- Require re-validation of tenant_id against database before any mutation
- Admin endpoints require MFA session token (X-MFA-Token header)
- All write operations carry idempotency key (X-Idempotency-Key header)
- Undeclared endpoints → BLOCK DEPLOYMENT
```

---

## 1️⃣6️⃣ EVENT SCHEMA REGISTRY (MANDATORY)

```
EVENT                             PRODUCER             CONSUMERS
--------------------------------  -------------------  -----------------------------------------
tenant.registered                 Tenancy Agent        Notification, Audit, Platform Ops Queue
tenant.verification.challenged    Tenancy Agent        Notification, Audit
tenant.verification.approved      Platform Ops         Tenancy Agent, Notification, Audit
tenant.verification.failed        Platform Ops         Notification, Audit
tenant.activated                  Tenancy Agent        Billing, RBAC Engine, Infrastructure,
                                                       Notification, Audit
tenant.suspended                  Tenancy Agent        Auth Service (block logins), Billing,
                                                       Notification, Audit
tenant.reinstated                 Tenancy Agent        Auth Service, Billing, Notification, Audit
tenant.offboarding.initiated      Tenancy Agent        Data Export Service, Notification, Audit
tenant.offboarding.complete       Tenancy Agent        KMS (DEK revocation), Audit
tenant.terminated                 Tenancy Agent        All services (purge tenant data), KMS, Audit
tenant.branding.updated           Tenancy Agent        CDN invalidation, UI service, Audit
tenant.quota.threshold.reached    Tenancy Agent        Notification (80% alert), Platform Ops
tenant.quota.exhausted            Tenancy Agent        Entitlement Engine (block feature), Notification
tenant.sso.activated              Tenancy Agent        Auth Service, Notification, Audit
tenant.domain.verified            Tenancy Agent        SSL provisioning, CDN, Audit
tenant.encryption.key.rotated     KMS (via agent)      Audit, Security Officer Notification
tenant.isolation.violation        API Gateway          Security Officer, PagerDuty CRITICAL, Audit
tenant.member.invited             Tenancy Agent        Notification, Audit
tenant.member.joined              Tenancy Agent        RBAC Engine, Notification, Audit
tenant.member.removed             Tenancy Agent        RBAC Engine, Session Invalidation, Audit
```

### Event Envelope (MANDATORY FIELDS)

```json
{
  "event_id":          "UUID",
  "event_type":        "tenant.activated",
  "actor_id":          "UUID",
  "actor_role":        "PLATFORM_OPS | TENANT_ADMIN | SYSTEM",
  "trigger_source":    "HUMAN | SYSTEM | COMPLIANCE",
  "tenant_id":         "UUID",
  "tenant_type":       "COMPANY | INSTITUTION | INDIVIDUAL | RECRUITER_AGENCY",
  "domain":            "TENANCY",
  "authorized_action": "ACTIVATE_TENANT",
  "payload":           {},
  "timestamp":         "ISO8601 UTC",
  "correlation_id":    "UUID",
  "schema_version":    "1.0"
}
```

---

## 1️⃣7️⃣ TENANT OFFBOARDING & DATA PURGE (HARD LOCK)

### 17.1 Offboarding Triggers

```
TENANT-INITIATED:
  - Tenant Admin submits cancellation request
  - MFA confirmation required
  - 30-day grace period begins immediately

PLATFORM-INITIATED:
  - Non-payment > 45 days (after 7-day SUSPENDED grace)
  - Repeated compliance violations
  - Legal order (compliance officer authority)
  - All require Super Admin MFA + documented reason

RULE: AI agents CANNOT initiate offboarding
```

### 17.2 Offboarding Sequence

```
DAY 0  — Offboarding initiated
          → Tenant status: OFFBOARDING
          → All new registrations blocked
          → Existing users: read-only access
          → Data export package generated (signed URL, 30-day expiry)
          → Tenant Admin notified with export link

DAY 1–30 — Grace Period
          → Tenant Admin can cancel offboarding (reinstates to ACTIVE)
          → Export link remains valid
          → Audit trail continues

DAY 30  — Grace period ends
          → If not reinstated: PURGE SEQUENCE BEGINS
          → All user sessions invalidated
          → All API access blocked
          → Offboarding status: PURGING

PURGE SEQUENCE (automated, deterministic):
  1. Object storage (MinIO bucket) → deleted
  2. Elasticsearch indexes → deleted
  3. Redis keys (tenant:{id}:*) → flushed
  4. PostgreSQL (soft-delete first, then hard delete cascade)
  5. Kafka topic messages (TTL enforcement)
  6. Backup tapes flagged for purge cycle
  7. DEK revocation request sent to KMS
  8. KMS confirms DEK revoked
  9. Tenant record anonymized (name → [TERMINATED], domain → [PURGED])
  10. Lifecycle event: tenant.terminated written (immutable, retained 7 years)

RULE: Financial records retained 7 years (anonymized, no PII linkage)
RULE: Audit trail retained 7 years (immutable, anonymized tenant reference)
RULE: Hard delete is IRREVERSIBLE — terminated tenants cannot be reactivated
```

---

## 1️⃣8️⃣ TENANT RESTORE ISOLATION ORDER (DISASTER RECOVERY)

```
On DR scenario, restore sequence is STRICTLY ORDERED:

  1. Platform system tables (global config, plan definitions)
  2. Identity & tenant registry (tenants, tenant_roles, tenant_memberships)
  3. Tenant data — ONE TENANT AT A TIME (never parallel)
  4. Cross-tenant read models (analytics aggregates) — LAST

RULES:
  - Isolation check after every tenant restore (cross-tenant leak = SYSTEM INVALIDATED)
  - No parallel tenant restores
  - CI/CD pipelines PAUSED during DR MODE
  - No schema migrations during restore
  - Human approval required before exiting DR MODE
  - Quarterly full restore drill mandatory (intern-executable runbook)
```

---

## 1️⃣9️⃣ SECURITY & COMPLIANCE RULES (ABSOLUTE)

### 19.1 Authentication Binding

```
Authentication MUST include tenant_id + domain context
Authentication without tenant + domain context = INVALID
Arts student cannot authenticate into Science tenant (HARD BLOCK)
Institution SSO and Enterprise SSO supported (Section 12.2)
Password hashes MUST be tenant-isolated (no cross-tenant hash reuse)
```

### 19.2 Session Isolation

```
Session tokens carry tenant_id claim (validated on every request)
Force tenant-wide logout available to Tenant Admin + Super Admin
Tenant suspension → all active sessions invalidated immediately
Inactivity timeout applies per tenant configuration (default: 30 min)
```

### 19.3 Data Residency

```
Tenants can select data residency region (ENTERPRISE plan):
  - India (Mumbai / Pune)
  - EU (Frankfurt)
  - US (Virginia)
  - APAC (Singapore)

Residency selection is IMMUTABLE post-activation (requires new tenant for migration)
Cross-region data transfer requires explicit tenant consent
Data residency selection logged in tenant_lifecycle_events
```

### 19.4 Audit Requirements

```
EVERY action on tenant entities must log:
  actor_id · actor_role · tenant_id · action · timestamp · ip_address · result

Audit logs:
  - Immutable (no UPDATE/DELETE on audit tables)
  - Tenant-isolated (Tenant A cannot see Tenant B audit logs)
  - Retained 7 years
  - Compliance Officer read-only access (MFA required)
  - Super Admin read access (MFA + IP allowlist)

Cross-tenant audit visibility = FORBIDDEN (even for Super Admin without compliance order)
```

---

## 2️⃣0️⃣ OBSERVABILITY & ALERTING (MANDATORY)

### 20.1 Prometheus Metrics

```
tenancy_active_tenants_total              (gauge, by tenant_type)
tenancy_pending_verification_total        (gauge)
tenancy_onboarding_stage_completion       (histogram, by stage)
tenancy_suspended_tenants_total           (gauge)
tenancy_offboarding_tenants_total         (gauge)
tenancy_isolation_violations_total        (counter — CRITICAL)
tenancy_quota_alerts_total                (counter, by resource_type)
tenancy_quota_exhaustion_total            (counter, by resource_type)
tenancy_sso_login_success_total           (counter, by protocol)
tenancy_sso_login_failure_total           (counter, by protocol)
tenancy_dek_rotation_total                (counter)
tenancy_member_join_total                 (counter, by tenant_type)
tenancy_member_remove_total               (counter)
```

### 20.2 Alerts

```
CRITICAL:
  - Cross-tenant isolation violation detected
  - DEK revocation failure during offboarding
  - Tenant namespace provisioning failure
  - SSO authentication exploited (anomaly detected)

WARNING:
  - Verification queue depth > 50 pending
  - Tenant quota > 80% for > 5 tenants simultaneously
  - Offboarding grace period expiring in < 24 hours (no export confirmation)
  - SSO config change without test pass

INFO:
  - New tenant activated
  - Tenant suspended/reinstated
  - DEK rotation completed
  - Tenant offboarding initiated
```

### 20.3 Grafana Dashboards

```
Dashboard: TENANCY_OVERVIEW
  - Active tenant count by type
  - Onboarding funnel (stages completed vs dropped)
  - Verification queue depth (live)
  - Pending offboarding list

Dashboard: TENANCY_ISOLATION
  - Isolation violation timeline (should be zero)
  - Cross-tenant API error rate
  - DEK rotation schedule compliance

Dashboard: TENANCY_HEALTH
  - Quota utilization per tenant (top 10)
  - Suspended tenant list
  - SSO success/failure rate by tenant
  - Namespace provisioning success rate
```

---

## 2️⃣1️⃣ ADMIN GOVERNANCE INTEGRATION (MANDATORY)

```
ADMIN CONSOLE MODULE: Identity & Tenancy

Screens (ALL REQUIRED):
  ✅ Tenant Registry Viewer (list + filter + status)
  ✅ Tenant Verification Approval Tool (human gate)
  ✅ Tenant Lifecycle Status Manager (suspend / reinstate / offboard)
  ✅ Domain Verification Status Viewer
  ✅ Tenant Branding Previewer + Rollback Tool
  ✅ Tenant Quota Viewer + Override Tool (Super Admin)
  ✅ Tenant SSO Configuration Validator
  ✅ Tenant Member Management Panel (cross-tenant view for Super Admin)
  ✅ Isolation Violation Incident Log
  ✅ DEK Rotation Trigger Panel (compliance use)
  ✅ Offboarding Progress Tracker
  ✅ Tenant Lifecycle Event Audit Trail (immutable view)

Access:
  - Super Admin: Full access (MFA + IP allowlist)
  - Platform Ops: Verification + health monitoring (MFA)
  - Compliance Officer: Audit + lifecycle events (read-only, MFA)
  - AI Agents: NO ACCESS
```

---

## 2️⃣2️⃣ FOUR-STAGE ROLLOUT (SEQUENTIAL — NO SKIPPING)

```
STAGE 1 — FOUNDATION (tenancy essentials)
  ✅ Tenant registration + verification workflow
  ✅ Tenant lifecycle state machine
  ✅ Basic RBAC seeding per tenant type
  ✅ Namespace provisioning (K8s + PostgreSQL RLS + Redis + MinIO)
  ✅ DEK generation via KMS
  ✅ Domain isolation enforcement
  ✅ Quota enforcement (basic)

STAGE 2 — INTELLIGENCE (tenancy analytics)
  ✅ Tenant usage analytics per resource type
  ✅ Quota alerting + enforcement (full)
  ✅ Tenant health dashboards
  ✅ Onboarding funnel analytics

STAGE 3 — ECOSYSTEM (advanced features)
  ✅ Enterprise SSO (SAML2 + OIDC)
  ✅ Custom domain + SSL provisioning
  ✅ Tenant branding system (full white-label)
  ✅ Tenant skill catalogs
  ✅ Tenant mentor pool management
  ✅ Data residency selection
  ✅ Domain-aware key separation (arts/commerce/science DEKs)

STAGE 4 — COMPLIANCE & SCALE
  ✅ Full offboarding + hard delete pipeline
  ✅ DEK revocation on termination
  ✅ 7-year audit retention + compliance export
  ✅ Tenant restore isolation order (DR)
  ✅ Performance isolation enforcement
  ✅ Cross-tenant isolation violation alerting
  ✅ Legal hold support
  ✅ Data residency audit reporting

Stage skipping = INVALID BUILD → STOP EXECUTION
```

---

## 2️⃣3️⃣ PRODUCTION READINESS CHECKLIST

```
Before TENANCY_GOVERNANCE_AGENT declared PRODUCTION-READY:

Infrastructure:
  ☐ PostgreSQL RLS policies active on all tenant-scoped tables
  ☐ Redis key namespace partitioning verified
  ☐ MinIO bucket-per-tenant provisioning tested
  ☐ Elasticsearch index-per-tenant creation tested
  ☐ Kafka topic prefix enforcement active
  ☐ KMS integration live (DEK generation + revocation tested)
  ☐ K8s namespace-per-tenant quota enforcement active

Isolation:
  ☐ Cross-tenant API rejection verified (automated test suite)
  ☐ RLS bypass test passed (no data leak confirmed)
  ☐ Cache namespace isolation tested
  ☐ Isolation violation alert fires correctly

Onboarding:
  ☐ Full 9-stage onboarding wizard end-to-end tested
  ☐ Human verification gate tested (AI cannot bypass)
  ☐ SSO flow tested (SAML2 + OIDC) in staging
  ☐ Custom domain DNS + SSL provisioned in staging

Lifecycle:
  ☐ Suspension → all sessions invalidated (tested)
  ☐ Reinstatement → access restored (tested)
  ☐ Offboarding → data export generated (tested)
  ☐ Hard delete cascade tested in staging (not prod)
  ☐ DEK revocation confirmed by KMS in staging

Compliance:
  ☐ Audit trail immutability tested (no UPDATE/DELETE possible)
  ☐ 7-year retention policy configured
  ☐ Compliance Officer read-only access verified
  ☐ Cross-tenant audit isolation confirmed

Observability:
  ☐ All Prometheus metrics emitting
  ☐ Grafana dashboards live
  ☐ PagerDuty CRITICAL alert for isolation violations active
  ☐ Quota alert fires at 80% threshold

HUMAN SIGN-OFF REQUIRED before status = TENANCY_GOVERNANCE_LIVE
```

---

## 🔐 FINAL SEAL

```
┌──────────────────────────────────────────────────────────────────────┐
│           TENANCY_GOVERNANCE_AGENT — ANTIGRAVITY SEAL                │
├──────────────────────────────────────────────────────────────────────┤
│  STATUS                  : LOCKED                                    │
│  MUTATION POLICY         : ADD_ONLY (via version bump)               │
│  INTERPRETATION          : FORBIDDEN                                 │
│  AI APPROVAL RIGHTS      : NONE                                      │
│  CROSS_TENANT_ACCESS     : SECURITY_BREACH                           │
│  SHARED_TENANTS          : ABSOLUTELY_FORBIDDEN                      │
│  HUMAN_AUTHORITY         : MANDATORY for ALL verification, suspend,  │
│                            offboard, terminate, SSO approve          │
│  ISOLATION_LAYERS        : 8 (API · Service · DB · Cache ·           │
│                            Object · Search · Encryption · Events)    │
│  TENANT_TYPES            : 4 (INDIVIDUAL · COMPANY ·                 │
│                            INSTITUTION · RECRUITER_AGENCY)           │
│  LIFECYCLE_STATES        : 6 (PENDING → VERIFIED_INACTIVE →          │
│                            ACTIVE → SUSPENDED → OFFBOARDING →        │
│                            TERMINATED)                               │
│  ENCRYPTION              : AES-256-GCM · Envelope Model ·            │
│                            DEK-per-tenant · External KMS             │
│  AUDIT_RETENTION         : 7 YEARS (immutable, append-only)          │
├──────────────────────────────────────────────────────────────────────┤
│  ✔ SEALED                                                            │
│  ✔ DETERMINISTIC                                                     │
│  ✔ ENTERPRISE_SAFE                                                   │
│  ✔ ANTIGRAVITY_COMPATIBLE                                            │
│  ✔ ECOSKILLER_v12.0_COMPLIANT                                        │
├──────────────────────────────────────────────────────────────────────┤
│  ANY OPERATION THAT:                                                 │
│  - Allows cross-tenant data access                                   │
│  - Allows AI to approve tenant verification                          │
│  - Shares encryption keys between tenants                            │
│  - Skips onboarding stages                                           │
│  - Allows tenant reactivation after TERMINATED                       │
│  - Processes auth without tenant_id + domain context                 │
│  - Allows namespace provisioning without DEK generation              │
│  - Allows offboarding without data export offer                      │
│                                                                      │
│  ⇒ MUST BE REJECTED → STOP EXECUTION → REPORT VIOLATION             │
└──────────────────────────────────────────────────────────────────────┘
```

---

*TENANCY_GOVERNANCE_AGENT.md · ECOSKILLER Platform · Antigravity Execution Engine*
*Version: 1.0.0 · Classification: LOCKED · Append-only via version bump*
*Foundation Lane A — produces: identity_ready · rbac_ready · event_schema_ready*
