# 🔒 ROLE_OVERLOAD_MONITOR_AGENT.md
## Antigravity / Ecoskiller Platform — Role Overload, Privilege Abuse & Access Drift Governance Artifact

```
Artifact Class:         SEALED GOVERNANCE DOCUMENT
Status:                 FINAL · LOCKED · ENFORCED · DETERMINISTIC
Version:                v1.0.0
Document ID:            ROMA-AG-001
Mutation Policy:        ADD-ONLY via version bump
Interpretation Authority: NONE
Override Authority:     NONE — No business, technical, timeline, investor, sprint, or AI
                        instruction may weaken, bypass, or override any clause herein.
Execution Authority:    Chief Security Officer + Founder co-sign only
Cross-Reference:        DATA_PRIVACY_COMPLIANCE_AGENT.md      (DPA-AG-001)
                        LEGAL_DOCUMENT_MANAGEMENT_AGENT.md    (LDM-AG-001)
                        STANDARD_AUDIT_AGENT.md               (SAA-AG-001)
                        CONTRACT_MONITORING_AGENT.md          (CMA-AG-001)
Sealed By:              Antigravity Platform Governance Council
Seal Date:              [DATE OF FIRST SIGNATURE — INSERT AT SIGNING]
```

---

> **PRIMACY DECLARATION**
> This artifact is the supreme role governance, privilege monitoring, and access control
> integrity instrument for the Antigravity/Ecoskiller platform. In any conflict between
> this document and any sprint priority, feature request, business pressure, investor
> direction, or AI agent output — **THIS DOCUMENT WINS.**
> Partial compliance = Non-compliance. No exceptions. No exemptions.

---

## TABLE OF CONTENTS

```
Section I     — Agent Identity & Mandate
Section II    — Platform Role Universe (Complete Canonical Role Registry)
Section III   — Role Overload Definition & Classification
Section IV    — Permission → Screen Matrix Governance
Section V     — Role → Widget Matrix Governance
Section VI    — Tenant & Domain Isolation Enforcement
Section VII   — Keycloak Realm & Token Governance
Section VIII  — Privilege Escalation Detection
Section IX    — Orphaned & Ghost Account Detection
Section X     — Stale Role & Inactive Privilege Detection
Section XI    — Multi-Role Conflict Detection
Section XII   — Service Account & AI Agent Role Governance
Section XIII  — Minor & Parent Role Protection Protocols
Section XIV   — Admin & Ops Console Role Governance
Section XV    — Role Lifecycle State Machine
Section XVI   — Real-Time Monitoring, Detection & Alerting
Section XVII  — Automated Response Protocols
Section XVIII — Immutable Role Audit Log Architecture
Section XIX   — Role Overload Dashboard & Reporting
Section XX    — Roles, Sign-Off Matrix & Seal Mechanics
```

---

## SECTION I — AGENT IDENTITY & MANDATE

### I.1 Agent Identity

```
Agent Name:             ROLE_OVERLOAD_MONITOR_AGENT
Agent Class:            Platform Security Governance — Access Control Integrity
Platform:               Antigravity / Ecoskiller
Entity Type:            Enterprise Multi-Tenant · Multi-Domain · Multi-Hierarchy SaaS
Domain Tracks:          Arts | Commerce | Science | Technology | Administration
Sub-Platforms:          Ecoskiller Core · Dojo for Arts · Innovation Engine ·
                        Society / Offline Franchise · Campus Placement Portal ·
                        Voice GD · Trainer Ecosystem · Developer API
Identity Stack:         Keycloak (self-hosted) + OAuth2 + OIDC + JWT
Authorization Model:    RBAC (Role-Based Access Control) + ABAC (Attribute-Based Access Control)
API Gateway:            Kong OSS (all inbound requests; rate limiting; JWT validation)
Technology Stack:       Python 3.11 / FastAPI / Flutter / Next.js 14 / PostgreSQL 15 /
                        Redis 7 / OpenSearch 2.x / Keycloak / Kong / Vault /
                        Kafka / Temporal / k3s (GCP self-hosted)
```

### I.2 Mandate

The ROLE_OVERLOAD_MONITOR_AGENT is the **single authoritative access control integrity mechanism** for the entire Antigravity/Ecoskiller platform. Its mandate covers:

1. **Role Universe Integrity** — Every role assigned in every Keycloak realm is declared, registered, and matches the canonical Role Registry. No undeclared role may exist in any environment.
2. **Overload Detection** — Detect when any user account carries more roles, permissions, or privileges than their declared persona requires. Excess = overload = violation.
3. **Permission Drift Detection** — Continuously verify that the live Permission→Screen Matrix matches the declared registry (R23). Any drift = breach.
4. **Privilege Escalation Detection** — Detect horizontal and vertical privilege escalation attempts — role assignment above declared authority, cross-tenant access, cross-domain access.
5. **Orphan & Ghost Account Detection** — Identify accounts with active role assignments but no active user (departed employees, terminated contractors, expired franchise owners).
6. **Stale Privilege Detection** — Identify permissions, tokens, or role bindings that have not been used within their declared activity window and flag for review or revocation.
7. **Multi-Role Conflict Detection** — Identify conflicting dual-role assignments that violate the platform's role separation rules (e.g., Student + Recruiter on same account).
8. **Minor Protection Enforcement** — Enforce that accounts flagged as minor (under-18) never receive adult-tier, financial, or admin roles.
9. **Continuous Keycloak Realm Surveillance** — Monitor all Keycloak realm configurations, client scopes, and token claims for drift against declared specifications.
10. **Real-Time Automated Response** — Automatically suspend, quarantine, or alert on any detected overload condition without requiring human approval for containment.

### I.3 Non-Negotiable Role Governance Law

```
DEFAULT BEHAVIOR = DENY
  Any role not explicitly declared in this document's Role Registry = INVALID
  Any permission not in the Permission→Screen Matrix = BLOCKED
  Any token claim not in the declared scope = REJECTED at Kong gateway

IF role overload or privilege violation is detected:
  → SUSPEND the excess role/permission immediately (do not wait for approval)
  → LOG to immutable role audit log
  → ALERT Chief Security Officer + DPO within 15 minutes
  → HOLD any in-flight requests using the suspended privilege
  → REPORT finding to SAA-AG-001 audit registry

Acceptable role states: ACTIVE | ACTIVE_LIMITED | PROBATION
Unacceptable role states causing immediate action: OVERLOADED | ESCALATED |
  ORPHANED | GHOST | STALE | CONFLICTED | UNAUTHORIZED
```

---

## SECTION II — PLATFORM ROLE UNIVERSE (CANONICAL ROLE REGISTRY)

This is the **single source of truth** for every role that may exist on the Antigravity/Ecoskiller platform. Any role not in this registry that appears in any Keycloak realm, database table, JWT token, or API request is an **UNAUTHORIZED ROLE** and must be immediately invalidated.

### II.1 Core Platform Roles

#### TIER 0 — Platform Infrastructure Roles (Internal Only — No User-Facing)

```
ROLE: PLATFORM_SUPER_ADMIN
  Description:  Highest platform authority. Anthropic/Antigravity internal only.
  Scope:        All tenants, all domains, all services
  Keycloak:     platform-realm
  MFA:          MANDATORY — hardware key + TOTP required
  IP Allowlist: REQUIRED — corporate IP only
  Max Accounts: 3 (hard limit)
  Audit:        Every action logged with full request body + IP + device fingerprint
  Prohibited:   Cannot be assigned via self-service, API, or automated provisioning
  Restrictions: Dual authorization required for any destructive action

ROLE: PLATFORM_COMPLIANCE_ADMIN
  Description:  Platform-wide compliance and audit access. DPO + Legal Officer use.
  Scope:        Read all tenants; write to compliance logs, legal holds, audit trails
  Keycloak:     platform-realm
  MFA:          MANDATORY — TOTP minimum
  Max Accounts: 5
  Restrictions: No write access to user data; no ability to modify RBAC assignments

ROLE: PLATFORM_SECURITY_ADMIN
  Description:  Security monitoring, Keycloak realm management, threat response
  Scope:        All tenants; RBAC management; token revocation; session termination
  Keycloak:     platform-realm
  MFA:          MANDATORY — hardware key + TOTP
  Max Accounts: 3
  Restrictions: Cannot approve financial transactions; cannot access user PII beyond security context

ROLE: PLATFORM_OPS_ADMIN
  Description:  Internal operations console access. Ops team use.
  Scope:        Platform-wide operational monitoring; tenant management; feature flags
  Keycloak:     platform-realm
  MFA:          MANDATORY — TOTP minimum
  Max Accounts: 10
  Restrictions: No financial transaction approval; no RBAC management
```

#### TIER 1 — Tenant Administration Roles

```
ROLE: TENANT_ADMIN
  Description:  Administrator for a specific tenant (institute, enterprise, or franchise unit)
  Scope:        Own tenant only; hard tenant_id isolation enforced by RLS
  Keycloak:     tenant-realm (per tenant)
  MFA:          MANDATORY
  Max per Tenant: 3
  Restrictions: Cannot access other tenants; cannot modify platform-level settings

ROLE: TENANT_COMPLIANCE_OFFICER
  Description:  Compliance and audit access within a single tenant
  Scope:        Own tenant audit logs, compliance reports; read-only on user data
  Keycloak:     tenant-realm
  MFA:          MANDATORY
  Restrictions: Read-only; no user account modification

ROLE: TENANT_BILLING_ADMIN
  Description:  Billing and payment management for a tenant
  Scope:        Own tenant billing; invoices; subscription management
  MFA:          MANDATORY
  Restrictions: No access to user personal data; no RBAC management
```

#### TIER 2 — Domain-Specific Operational Roles

```
ROLE: INSTITUTE_ADMIN
  Description:  Administrator for a school, college, or university
  Scope:        Own institution's student cohort; own department structure; own events
  Keycloak:     tenant-realm → institute sub-realm
  Domain:       All academic domain tracks for own institution
  Restrictions: Cannot access other institutions' data; no employer/recruiter screens

ROLE: INSTITUTE_TPO
  Description:  Training and Placement Officer — manages campus placement operations
  Scope:        Own institution's placement pipeline; employer relationships; student profiles (consented)
  Domain:       Campus Placement Portal only
  Restrictions: Cannot modify student profiles directly; read-only on AI match scores;
                access revoked when employment with institution ends

ROLE: ENTERPRISE_ADMIN
  Description:  Administrator for a corporate enterprise account (L1–L7 hierarchy)
  Scope:        Own company's departments, job postings, hiring pipeline, workforce
  Keycloak:     enterprise-realm
  Hierarchy:    L1 (Board) → L2 (C-Suite) → L3 (VP) → L4 (Director) → L5 (Manager) →
                L6 (Senior) → L7 (Junior) — access gates applied per level
  Restrictions: Cannot access other enterprises; no student personal data without consent

ROLE: RECRUITER_HR
  Description:  HR and recruiting role for enterprise or SME
  Scope:        Job postings management; application review; offer issuance; consented profiles
  Domain:       Job Portal; Campus Placement
  Restrictions: AI match scores are advisory only; cannot sole-reject based on AI score;
                consented profiles only; no bulk profile export

ROLE: SME_OWNER
  Description:  Small/Medium Enterprise owner account
  Scope:        Own company profile; job postings; hiring workflow; SME reliability score
  Restrictions: Same as RECRUITER_HR + additional SME-specific screening obligations;
                no bypass for SME verification requirements (per system law)
```

#### TIER 3 — Learning & Education Roles

```
ROLE: TRAINER
  Description:  Course creator and live session facilitator
  Scope:        Own courses; own students enrolled in own courses; live session hosting
  Domain:       Skill Development Engine; Project Execution (as mentor)
  Restrictions: No access to other trainers' student lists;
                minor_facing trainers require background_check_status = CLEARED before activation;
                content license is non-exclusive display-only

ROLE: MENTOR
  Description:  Project mentor and career guidance provider
  Scope:        Assigned projects; assigned mentees; mentorship sessions
  Domain:       Project Execution Engine; Career Lifecycle
  Restrictions: Access to mentee profile only for assigned mentees;
                session recording requires explicit per-session consent

ROLE: EVALUATOR
  Description:  Assessment evaluator for skills, projects, and Dojo matches
  Scope:        Assigned evaluation queues; scoring panels
  Domain:       Skill Development; Dojo for Arts; Project Execution
  Restrictions: Cannot modify evaluation criteria after assignment;
                score override requires Mentor + Ops Console dual approval;
                no access to student PII beyond evaluation context
```

#### TIER 4 — Community & Social Roles

```
ROLE: STUDENT
  Description:  Primary learner account — students from colleges, schools, self-learners
  Scope:        Own profile; enrolled courses; applied jobs; skill passport; portfolio
  Domain:       All learning domains per declared track
  Restrictions: No recruiter screens; no admin controls; no financial transaction approval;
                minor students additionally restricted per MINOR_STUDENT role extension

ROLE: STUDENT_MINOR [Role Extension — not standalone]
  Description:  Extension applied to STUDENT accounts where age < 18
  Applied On:   Detection of DOB confirming under-18 at account creation or verification
  Additional Restrictions:
                - Financial transactions BLOCKED (no purchase, no royalty payout, no wallet)
                - Adult-tier content BLOCKED
                - Voice GD: guardian co-sign required before first session
                - Dojo: guardian consent required for video capture
                - Innovation Engine: royalties in escrow; commercialization locked until 18
                - Parent/guardian access ENABLED for account
                - Age-18 transition: guardian restrictions lifted automatically on 18th birthday
                - Cannot be manually removed by any role below PLATFORM_COMPLIANCE_ADMIN

ROLE: PARENT_GUARDIAN
  Description:  Read-only trust layer for monitoring a linked minor account
  Scope:        Linked minor's progress, activity, content consumption — READ ONLY
  Keycloak:     Separate guardian account; linked via guardian_id to minor account
  Restrictions: STRICTLY read-only; cannot post, apply, purchase, or modify minor's account;
                guardian consent actions are write operations handled via dedicated consent API;
                access revoked automatically when linked minor turns 18 (age-18 protocol)

ROLE: ALUMNI
  Description:  Former student who has graduated; provides mentorship and referrals
  Scope:        Own profile; mentorship matching; referral network; success story (consented)
  Domain:       Core Platform; Career Lifecycle; Campus referral
  Restrictions: No active placement pipeline access; no recruiter screens
```

#### TIER 5 — Society / Franchise Domain Roles

```
ROLE: FRANCHISE_OWNER
  Description:  Operates a licensed franchise unit in a declared territory
  Scope:        Own franchise territory; own coaches and coordinators; own workshop batches;
                own commission and payout records
  Keycloak:     society-realm
  Domain:       Society / Offline Franchise domain only
  Restrictions: Cannot access other franchise territories (geo-boundary RLS enforced);
                access revoked within 24h of franchise termination;
                commission data visible only for own unit

ROLE: SOCIETY_ADMIN
  Description:  Society unit administrator — manages village-level operations
  Scope:        Assigned society unit(s); coordinator assignment; territory mapping
  Keycloak:     society-realm
  Restrictions: No access beyond assigned society_id(s); RLS enforced on society_id

ROLE: MASTER_ORGANIZER
  Description:  Multi-society oversight role; manages multiple franchise territories
  Scope:        Assigned set of franchise units (explicit list in role assignment attribute)
  Keycloak:     society-realm
  Restrictions: Access limited to assigned territory list only; no platform-wide access;
                territory list must be explicitly enumerated — no wildcard access

ROLE: COORDINATOR
  Description:  Field coordinator for society operations
  Scope:        Assigned society unit; batch coordination; attendance verification; coach liaison
  Keycloak:     society-realm
  Restrictions: No commission calculation access; no franchise agreement access;
                non-solicitation obligation tracked (12 months post-engagement)

ROLE: COACH
  Description:  Skill delivery agent in franchise/society units
  Scope:        Own batches; own students; demo evaluation; performance tracking
  Keycloak:     society-realm
  Restrictions: Own batch data only; no access to other coaches' batches;
                background verification required for minor-facing workshops;
                non-solicitation obligation tracked (12 months post-engagement)
```

#### TIER 6 — Developer & Partner Roles

```
ROLE: API_DEVELOPER
  Description:  External developer with API key access
  Scope:        Permitted API endpoints per signed Developer Agreement
  Keycloak:     developer-realm
  Restrictions: Identity verified before key issuance;
                rate limits enforced at Kong; bulk export blocked;
                access suspended immediately on agreement violation;
                annual competitive-use attestation obligation

ROLE: INTEGRATION_PARTNER
  Description:  White-label or deep integration partner
  Scope:        Permitted API scopes per Integration Partner Agreement
  Restrictions: DPA required; subprocessor disclosure required;
                data use limited to stated integration purpose;
                access revoked within 30 days of agreement termination

ROLE: INTERN_DEVELOPER [Restricted]
  Description:  Development intern with limited system access
  Scope:        DEV environment only; no production access; no Tier 0/1 data
  Restrictions: No production database access; no secret vault access;
                access revoked within 4h of internship end date;
                no Tier 0/1 personal data access without explicit DPO + CCO approval
```

#### TIER 7 — Non-Human / Automated Roles

```
ROLE: SERVICE_ACCOUNT
  Description:  Machine identity for inter-service communication
  Scope:        Declared inter-service API calls only; defined in Service Mesh policy
  Keycloak:     service-realm
  Token:        Short-lived (max 1h), Vault-issued; auto-rotated
  Restrictions: No human login; no interactive session; token expiry strictly enforced;
                scope limited to declared service-to-service calls;
                any SERVICE_ACCOUNT used for human login = TIER 1 SECURITY BREACH

ROLE: AI_AGENT [Automation — Non-Human]
  Description:  AI agent accounts for platform automation (matching, ranking, analytics)
  Scope:        Read access to declared data sets; no write access to user profile core fields;
                no financial transaction authority
  Restrictions: AI NEVER approves, blocks, or overrides humans (system law);
                AI_AGENT role cannot escalate to any human role;
                all AI actions logged in immutable audit trail;
                AI advisory flag required on all AI-generated scores

ROLE: CI_CD_AGENT [Automation — Non-Human]
  Description:  CI/CD pipeline service accounts (GitLab CI, Kubernetes operators)
  Scope:        Deployment pipeline only; infrastructure namespaces
  Restrictions: Production deployment requires human release authorization (CTO sign-off);
                no access to user data stores; no financial system access;
                secrets accessed via Vault dynamic credentials only — no static secrets
```

### II.2 Role Registry Schema

```sql
CREATE TABLE role_registry (
    role_id             VARCHAR(100) PRIMARY KEY,
    -- Format: ROLE_{TIER}_{NAME} e.g. ROLE_T0_PLATFORM_SUPER_ADMIN
    role_name           VARCHAR(100) NOT NULL UNIQUE,
    tier                INTEGER      NOT NULL CHECK (tier BETWEEN 0 AND 7),
    description         TEXT         NOT NULL,
    domain              TEXT[]       NOT NULL,
    keycloak_realm      VARCHAR(100) NOT NULL,
    mfa_required        BOOLEAN      NOT NULL DEFAULT TRUE,
    max_concurrent_users INTEGER,
    is_human_role       BOOLEAN      NOT NULL DEFAULT TRUE,
    is_minor_safe       BOOLEAN      NOT NULL DEFAULT FALSE,
    -- FALSE = cannot be assigned to STUDENT_MINOR accounts
    allows_financial_tx BOOLEAN      NOT NULL DEFAULT FALSE,
    allows_pii_write    BOOLEAN      NOT NULL DEFAULT FALSE,
    requires_background_check BOOLEAN NOT NULL DEFAULT FALSE,
    requires_contract   BOOLEAN      NOT NULL DEFAULT TRUE,
    ip_allowlist_required BOOLEAN    NOT NULL DEFAULT FALSE,
    max_session_duration_hours INTEGER DEFAULT 8,
    inactivity_revoke_days INTEGER   DEFAULT 90,
    -- Role is flagged STALE if not used within this window
    conflicting_roles   TEXT[],
    -- List of role_ids that cannot coexist on same account
    permitted_attributes JSONB,
    -- ABAC attributes permitted for this role
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    last_reviewed_at    DATE,
    next_review_date    DATE,
    reviewed_by         VARCHAR(200),
    is_active           BOOLEAN      NOT NULL DEFAULT TRUE
    -- Append-only: is_active = FALSE to decommission; never DELETE
);
```

---

## SECTION III — ROLE OVERLOAD DEFINITION & CLASSIFICATION

### III.1 What Constitutes Role Overload

Role overload is defined as **any condition where a user account, service account, or AI agent holds more roles, permissions, scopes, or privileges than their declared persona permits**. The following conditions constitute role overload:

```
OVERLOAD TYPE 1: ROLE COUNT EXCESS
  Definition: Account holds more simultaneous roles than the declared maximum
              for any single account type.
  Rule:       MAX_ROLES_PER_ACCOUNT = 2 for human accounts (primary role + one contextual)
              Service accounts: MAX = 1 (single declared service scope)
              AI agents: MAX = 1 (single declared agent scope)
  Exception:  TENANT_ADMIN may hold TENANT_ADMIN + TENANT_BILLING_ADMIN simultaneously
              (declared dual-role). All other combinations require CCO approval.
  Detection:  COUNT(roles) per account > allowed maximum

OVERLOAD TYPE 2: PERMISSION SCOPE EXCESS
  Definition: Account has been granted individual permissions (via Keycloak client roles
              or direct DB grants) that exceed what their assigned role is permitted to have.
  Rule:       Permissions must be derived solely from role assignment —
              no individual permission grants permitted outside the Role Registry.
  Detection:  Any permission in Keycloak or DB that has no corresponding role_registry entry

OVERLOAD TYPE 3: CROSS-TENANT PRIVILEGE
  Definition: Account in Tenant A has any access whatsoever to Tenant B data or screens.
  Rule:       Tenant isolation is HARD. Cross-tenant = zero tolerance.
  Detection:  Any JWT token with tenant_id claim accessing data outside own tenant;
              Any DB query bypassing RLS tenant_id filter.

OVERLOAD TYPE 4: CROSS-DOMAIN PRIVILEGE
  Definition: Account in Domain Track X (e.g. Arts) accessing screens, data, or services
              of Domain Track Y (e.g. Technology) without explicit cross-domain grant.
  Rule:       Domain isolation is HARD per system architecture law.
  Detection:  Request from user with domain_track = Arts hitting Science-track endpoints.

OVERLOAD TYPE 5: HIERARCHICAL OVERREACH
  Definition: Account with lower privilege tier (e.g. L4 Manager) accessing L1 Board
              screens or approving L1-gated actions in the enterprise hierarchy.
  Detection:  Corporate hierarchy level (L1–L7) mismatch between token claim and
              endpoint's declared minimum_hierarchy_level requirement.

OVERLOAD TYPE 6: UNAUTHORIZED ADMIN SURFACE
  Definition: Non-admin role (e.g. STUDENT, TRAINER) accessing any admin console
              screen, endpoint, or data source.
  Rule:       Admin UI and API are completely separate from user-facing surfaces;
              no shared endpoints between admin and user tiers.
  Detection:  Any user-tier JWT hitting /admin, /ops, or internal service endpoints.

OVERLOAD TYPE 7: MINOR FINANCIAL OVERLOAD
  Definition: Account with STUDENT_MINOR extension attempting financial transactions,
              royalty payouts, marketplace purchases, or wallet operations.
  Rule:       Zero financial authority for under-18 accounts. Absolute.
  Detection:  Any financial endpoint request with minor_flag = TRUE in user record.

OVERLOAD TYPE 8: SERVICE ACCOUNT HUMAN USE
  Definition: A SERVICE_ACCOUNT or AI_AGENT role used for an interactive human session.
  Rule:       Service accounts have no interactive session authority.
  Detection:  Service account JWT used with user-agent header indicating browser/mobile app.

OVERLOAD TYPE 9: TOKEN SCOPE INFLATION
  Definition: JWT token contains scopes or claims beyond what the issuing client and
              role combination is permitted in the Keycloak client scope configuration.
  Rule:       Token claims must exactly match declared role scopes — no additional claims.
  Detection:  Token claim set ≠ declared scope set for role/client combination.

OVERLOAD TYPE 10: DORMANT PRIVILEGE ACCUMULATION
  Definition: Account has accumulated permissions over time through role changes but
              old permissions from prior roles were never revoked.
  Rule:       Role change = immediate revocation of all permissions from previous role;
              no permission carry-over unless explicitly re-granted by new role.
  Detection:  Permission set does not match current role's declared permission set.
```

### III.2 Overload Severity Classification

```
SEVERITY 1 — CRITICAL (Automatic Suspension)
  Triggers:
    - OVERLOAD TYPE 3: Cross-tenant privilege
    - OVERLOAD TYPE 4: Cross-domain privilege (unauthorized)
    - OVERLOAD TYPE 6: Non-admin user accessing admin surface
    - OVERLOAD TYPE 7: Minor with financial authority
    - OVERLOAD TYPE 8: Service account human use
  Response:   Automatic role suspension within 60 seconds; no human approval needed;
              CSO + Founder notified within 15 minutes

SEVERITY 2 — HIGH (Immediate Review)
  Triggers:
    - OVERLOAD TYPE 1: Role count excess
    - OVERLOAD TYPE 2: Permission scope excess
    - OVERLOAD TYPE 5: Hierarchical overreach
    - OVERLOAD TYPE 9: Token scope inflation
  Response:   Suspect role quarantined; CSO + DPO notified within 1h;
              human review required within 4h

SEVERITY 3 — MEDIUM (Scheduled Review)
  Triggers:
    - OVERLOAD TYPE 10: Dormant privilege accumulation
    - Stale role (not used within inactivity_revoke_days)
    - Role assignment without linked contract (for roles where requires_contract = TRUE)
  Response:   Flag for review; CSO notified within 24h; review within 72h

SEVERITY 4 — OBSERVATION (Tracked)
  Triggers:
    - Role approaching inactivity threshold (75% of inactivity_revoke_days elapsed)
    - Role review date overdue (not reviewed within declared cycle)
    - Role without MFA enforcement where mfa_required = TRUE
  Response:   Logged; dashboard flagged; notified in weekly role governance report
```

---

## SECTION IV — PERMISSION → SCREEN MATRIX GOVERNANCE

### IV.1 Permission Matrix Declaration

The Permission→Screen Matrix (R23, Section F of Master Execution Prompt) is the **canonical mapping** of every role to every screen it may access. This matrix is:

- Declared in `/registries/permission_screen_matrix.yml`
- Validated by the R49 Contract Validator CLI on every CI push
- Enforced at the API Gateway (Kong) via JWT role claim inspection
- Enforced at the database layer via PostgreSQL Row-Level Security
- Enforced at the Flutter UI layer via role-aware widget rendering
- **Any screen accessible without a corresponding matrix entry = SECURITY BREACH**

### IV.2 Matrix Governance Rules

```
RULE PM-001: COMPLETE COVERAGE
  Every platform screen (Flutter screen + Next.js SEO page) must appear in the matrix.
  Screens not in the matrix = UNDECLARED SCREEN = deployment blocked (R49 gate).

RULE PM-002: NO ESCALATION BY UI
  The Flutter UI may only show/hide screens based on the matrix.
  A screen may NEVER reveal admin controls to non-admin roles via CSS, animation,
  conditional logic, or developer mode — architectural separation is required.
  Admin UI = completely separate application boundary, not hidden tabs.

RULE PM-003: API MIRRORS UI
  Every API endpoint called by a screen must have the same role requirements as
  the screen itself. API-level role check is independent of UI — never UI-only.
  Backend must re-validate role on every request regardless of UI state.

RULE PM-004: MATRIX IS VERSION-CONTROLLED
  Every change to the matrix requires:
    - CSO review
    - Version bump in permission_screen_matrix.yml
    - R49 validator re-run with new matrix
    - CI gate pass before merge
  No matrix changes in hotfix branches without CSO + CTO dual sign-off.

RULE PM-005: MATRIX DRIFT = TIER 1 FINDING
  If R49 Contract Validator detects any screen accessible without matching matrix entry,
  or any matrix entry with no corresponding screen, the finding is classified as
  TIER 1 in SAA-AG-001. Deployment is blocked until resolved.

RULE PM-006: INHERITANCE PROHIBITION
  No role may inherit another role's screen access unless explicitly declared in the matrix.
  Implicit inheritance via Keycloak composite roles requires CSO explicit sign-off on
  every inherited permission set.
```

### IV.3 Permission Matrix Schema

```sql
CREATE TABLE permission_screen_matrix (
    matrix_id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id             VARCHAR(100) NOT NULL REFERENCES role_registry(role_id),
    screen_id           VARCHAR(200) NOT NULL,
    screen_name         VARCHAR(200) NOT NULL,
    platform            VARCHAR(20)  NOT NULL CHECK (platform IN ('FLUTTER', 'NEXTJS', 'OPS_CONSOLE', 'API')),
    domain              VARCHAR(100) NOT NULL,
    access_type         VARCHAR(20)  NOT NULL CHECK (access_type IN ('FULL', 'READ_ONLY', 'WRITE_ONLY', 'CONDITIONAL')),
    condition_attribute VARCHAR(200),
    -- ABAC attribute required for CONDITIONAL access (e.g., 'owns_resource', 'same_tenant')
    api_endpoint        VARCHAR(300),
    http_methods        TEXT[],
    -- Methods permitted (GET, POST, PUT, DELETE, PATCH)
    tenant_scoped       BOOLEAN      NOT NULL DEFAULT TRUE,
    domain_scoped       BOOLEAN      NOT NULL DEFAULT TRUE,
    minor_blocked       BOOLEAN      NOT NULL DEFAULT FALSE,
    matrix_version      VARCHAR(20)  NOT NULL,
    effective_date      DATE         NOT NULL,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    approved_by         VARCHAR(200) NOT NULL
    -- Append-only: entries never deleted; access_type updated via new entry + old marked superseded
);
```

---

## SECTION V — ROLE → WIDGET MATRIX GOVERNANCE

### V.1 Widget Matrix Rules

The Role→Widget Matrix governs which Flutter widgets and Next.js UI components are visible to each role. This matrix is separate from but complementary to the Permission→Screen Matrix.

```
WIDGET MATRIX LAW:

RULE WM-001: FIXED WIDGETS CANNOT BE ROLE-GATED
  The 40% fixed dashboard area (Identity Panel, Compliance Indicators,
  Critical Alerts, Trust Badges) is visible to ALL authenticated roles.
  These widgets contain no sensitive data — they are always rendered.

RULE WM-002: CUSTOMIZABLE WIDGETS ARE ROLE-SCOPED
  The 60% customizable dashboard area renders only widgets declared
  for the user's active role in the Role→Widget Matrix.
  No widget from another role's widget set may be rendered, even if
  a developer adds it to the customizable area via user settings.

RULE WM-003: ADMIN WIDGETS = ADMIN BOUNDARY
  Widgets that expose administrative controls (user management, billing,
  system settings, emergency controls) exist only in the Admin/Ops Console.
  These widgets must NOT be compiled into the student/trainer/user Flutter app.
  Separate build targets for user app vs admin console.

RULE WM-004: WIDGET REQUIRES_PERMISSION DECLARATION
  Every widget in the customizable area must declare requires_permission in its
  widget contract. If the authenticated user lacks that permission → widget renders
  as locked/unavailable state (not hidden — lock state is visible for upgrade prompts).

RULE WM-005: ROLE CHANGE = WIDGET STATE RESET
  On role change or role addition, the Flutter widget tree must be fully rebuilt
  from the new role's widget set. No cached widget state from prior role.
  Stale widget state showing unauthorized controls = SEVERITY 2 overload.
```

### V.2 Widget Matrix Monitoring Checks

```
MONITOR-WM-001: On every app launch — verify active widget set matches current role
MONITOR-WM-002: On role change event — verify widget state reset confirmed in audit log
MONITOR-WM-003: Weekly — scan widget contract registry for any widget without
                requires_permission declaration
MONITOR-WM-004: On every deploy — R49 Contract Validator verifies Role→Widget Matrix
                completeness; missing widget declarations block deploy
```

---

## SECTION VI — TENANT & DOMAIN ISOLATION ENFORCEMENT

### VI.1 Tenant Isolation Architecture

The platform is **Enterprise Multi-Tenant**. Tenant isolation is declared as HARD in the system constitution. No exception, no soft-tenant mode, no shared-tenant workaround.

```
TENANT ISOLATION LAYERS (ALL MUST BE ACTIVE SIMULTANEOUSLY):

LAYER 1: JWT Token Isolation
  → Every JWT issued by Keycloak contains tenant_id claim
  → Kong API Gateway validates tenant_id on every inbound request
  → Requests without tenant_id claim are REJECTED (401)
  → Requests with tenant_id mismatch to accessed resource are REJECTED (403)

LAYER 2: PostgreSQL Row-Level Security (RLS)
  → All tables with tenant-scoped data have RLS policy: tenant_id = current_setting('app.tenant_id')
  → current_setting is set from JWT claim at connection time
  → RLS bypass (SET ROW SECURITY = OFF) is PROHIBITED — monitored by database audit trigger
  → Any RLS bypass attempt = TIER 1 SECURITY BREACH

LAYER 3: Service Layer Tenant Filter
  → Every FastAPI service enforces tenant_id filter in every database query
  → Tenant_id extracted from validated JWT — never from request body (injection prevention)
  → Service-level tenant validation is independent of RLS (defense in depth)

LAYER 4: MinIO Bucket Isolation
  → Separate MinIO prefix per tenant: /{tenant_id}/{domain}/{resource_type}/
  → Presigned URL generation validates tenant_id before issuing URL
  → No cross-tenant presigned URL issuable

LAYER 5: Keycloak Realm Isolation
  → Each tenant has own Keycloak realm (or sub-realm)
  → Cross-realm token acceptance PROHIBITED
  → Realm federation only where explicitly declared with CSO sign-off

MONITORING:
  MONITOR-TENANT-001: Daily — scan JWT logs for any cross-tenant_id access attempt
  MONITOR-TENANT-002: Daily — scan PostgreSQL audit log for RLS bypass attempts
  MONITOR-TENANT-003: Weekly — verify RLS policies exist on ALL tenant-scoped tables
  MONITOR-TENANT-004: Monthly — cross-tenant data leak test (synthetic test account in
                      Tenant A attempts access to Tenant B resource; must receive 403)
```

### VI.2 Domain Track Isolation

Domain Tracks (Arts | Commerce | Science | Technology | Administration) carry hard isolation rules.

```
DOMAIN ISOLATION RULES:

RULE DI-001: DEFAULT DENY CROSS-DOMAIN
  A user registered in Domain Track X may ONLY access content, services,
  and screens for Domain Track X unless explicitly granted cross-domain access.
  Cross-domain grant requires: CSO sign-off + specific domain pair declaration.

RULE DI-002: DOMAIN ENCODED IN JWT
  JWT token contains domain_track claim.
  Kong validates domain_track claim against requested domain service.
  Domain mismatch = 403 returned; incident logged.

RULE DI-003: CROSS-DOMAIN REQUESTS ARE SECURITY EVENTS
  Every blocked cross-domain request is logged as a SECURITY EVENT —
  not silently dropped. Repeated blocked cross-domain from same account
  (>3 in 24h) triggers SEVERITY 2 investigation.

RULE DI-004: TRAINER CROSS-DOMAIN RESTRICTION
  A TRAINER registered for Domain Arts may ONLY create and teach Arts content.
  Cross-domain teaching requires separate domain registration and agreement.
  System must prevent cross-domain content creation at API level — not just UI.

MONITORING:
  MONITOR-DOMAIN-001: Real-time — Kong logs all 403 responses with domain_mismatch reason
  MONITOR-DOMAIN-002: Daily — aggregate cross-domain 403 counts by account; alert at >3/day
  MONITOR-DOMAIN-003: Weekly — verify domain_track claim presence in 100% of active tokens
```

---

## SECTION VII — KEYCLOAK REALM & TOKEN GOVERNANCE

### VII.1 Keycloak Realm Inventory

All Keycloak realms must be declared and registered. Undeclared realms are UNAUTHORIZED.

```
DECLARED REALMS:

platform-realm
  Roles:    PLATFORM_SUPER_ADMIN, PLATFORM_COMPLIANCE_ADMIN,
            PLATFORM_SECURITY_ADMIN, PLATFORM_OPS_ADMIN
  MFA:      Hardware key mandatory for SUPER_ADMIN; TOTP for all others
  Sessions: Max 4h; no refresh token for SUPER_ADMIN

tenant-realm (instantiated per tenant)
  Naming:   {tenant_slug}-realm (e.g., ecoskiller-iit-bombay-realm)
  Roles:    TENANT_ADMIN, TENANT_COMPLIANCE_OFFICER, TENANT_BILLING_ADMIN,
            INSTITUTE_ADMIN, INSTITUTE_TPO, ENTERPRISE_ADMIN, RECRUITER_HR,
            SME_OWNER, STUDENT, STUDENT_MINOR, PARENT_GUARDIAN, ALUMNI,
            TRAINER, MENTOR, EVALUATOR
  MFA:      TOTP mandatory for all admin roles; optional for STUDENT (enforced at 
            high-risk actions via step-up authentication)

society-realm
  Roles:    FRANCHISE_OWNER, SOCIETY_ADMIN, MASTER_ORGANIZER, COORDINATOR, COACH
  MFA:      TOTP mandatory for FRANCHISE_OWNER, SOCIETY_ADMIN, MASTER_ORGANIZER
  Sessions: Max 12h (offline workers); refresh token permitted with rotation

developer-realm
  Roles:    API_DEVELOPER, INTEGRATION_PARTNER, INTERN_DEVELOPER
  MFA:      TOTP mandatory
  Sessions: Max 8h

service-realm
  Roles:    SERVICE_ACCOUNT, AI_AGENT, CI_CD_AGENT
  Sessions: Max 1h; client credentials flow only; no authorization code flow
  Tokens:   Vault-issued dynamic credentials preferred over static secrets
```

### VII.2 Token Governance Rules

```
TOKEN-001: JWT LIFETIME LIMITS
  Access token max lifetime:
    PLATFORM_SUPER_ADMIN:          15 minutes
    Platform admin roles (Tier 0): 30 minutes
    Tenant admin roles (Tier 1):   1 hour
    Operational roles (Tier 2–5):  4 hours
    User roles (Student, Trainer): 8 hours
    Service accounts:              1 hour (Vault-issued)
  Refresh token lifetime:
    Admin roles:   NOT ISSUED (re-authentication required)
    User roles:    24 hours with rotation on every use
    Service accounts: NOT ISSUED (client credentials re-auth)

TOKEN-002: MANDATORY TOKEN CLAIMS
  Every JWT must contain: sub, iss, aud, exp, iat, jti (unique token ID),
  tenant_id, role, domain_track, mfa_verified, minor_flag, session_id
  Missing any claim → token rejected at Kong gateway

TOKEN-003: TOKEN REVOCATION
  Keycloak must be configured for real-time token revocation via:
  → Revocation endpoint called on logout
  → Token blacklist checked on every request (Redis-backed, TTL = token exp)
  → Entire session revoked on: role change, account suspension, security event
  → PLATFORM_SECURITY_ADMIN can revoke any active session platform-wide

TOKEN-004: REFRESH TOKEN ROTATION
  Every refresh token use issues a new refresh token AND invalidates the old.
  Replay of a previously used refresh token = SECURITY EVENT → entire session revoked

TOKEN-005: CONCURRENT SESSION LIMITS
  PLATFORM_SUPER_ADMIN:     1 concurrent session (single device)
  Tenant admin roles:       2 concurrent sessions
  User roles (Student etc): 3 concurrent sessions (web + mobile + desktop)
  Service accounts:         Unlimited (stateless client credentials)
  Excess concurrent session attempt → oldest session terminated; new session granted

MONITORING:
  MONITOR-TOKEN-001: Real-time — Redis token blacklist hit → SECURITY EVENT logged
  MONITOR-TOKEN-002: Hourly — scan for tokens with lifetime exceeding declared maximum
  MONITOR-TOKEN-003: Daily — scan for tokens issued with missing mandatory claims
  MONITOR-TOKEN-004: Daily — verify token revocation latency ≤ 60 seconds
  MONITOR-TOKEN-005: Real-time — refresh token replay detection triggers session revocation
```

---

## SECTION VIII — PRIVILEGE ESCALATION DETECTION

### VIII.1 Horizontal Escalation Detection

Horizontal escalation = accessing resources of a peer user (same role level, different account owner).

```
DETECTION RULES:

HESC-001: RESOURCE OWNERSHIP ENFORCEMENT
  Every resource (profile, course, job posting, project, commission record) has
  an owner_id. Access by a non-owner of same role requires explicit sharing grant.
  Unauthorized peer access = horizontal escalation.

HESC-002: STUDENT-TO-STUDENT ISOLATION
  Student A cannot view Student B's full profile, portfolio, or application history
  without consent. Viewing via search returns only public-consented fields.
  Any attempt to access another student's private data = HESC event logged.

HESC-003: TRAINER-TO-TRAINER ISOLATION
  Trainer A cannot access Trainer B's student roster, revenue data, or course analytics.
  Shared platform features (e.g., search for collaboration) do not expose private data.

HESC-004: FRANCHISE-TO-FRANCHISE ISOLATION
  Franchise Owner A cannot access Franchise Owner B's territory, commission records,
  or coordinator/coach list. Geo-boundary RLS enforces this at DB layer.

DETECTION: ABAC attribute `owns_resource` checked on every resource access.
  owns_resource = (resource.owner_id = jwt.sub) OR (explicit_share_grant exists)
  Failure = HESC event; SEVERITY 2 if repeated; SEVERITY 1 if data exfiltration suspected
```

### VIII.2 Vertical Escalation Detection

Vertical escalation = accessing screens, data, or functions above declared role tier.

```
VESC-001: ROLE TIER ENFORCEMENT AT API LAYER
  Every API endpoint declares minimum_role_tier in its API contract registry (R3).
  Kong validates tier from JWT role claim against endpoint's declared minimum.
  Tier mismatch = 403; VESC event logged.

VESC-002: ADMIN ENDPOINT PROTECTION
  /admin/*, /ops/*, /internal/* endpoints require TIER 0 or declared TIER 1 admin role.
  Any non-admin JWT hitting these paths = VESC SEVERITY 1 event.

VESC-003: ROLE ASSIGNMENT PRIVILEGE
  Only PLATFORM_SECURITY_ADMIN may assign TIER 0 roles.
  Only TENANT_ADMIN may assign TIER 1–3 roles within own tenant.
  Any role assignment by an account without assignment authority = VESC SEVERITY 1.
  Self-assignment of higher role = VESC SEVERITY 1 + immediate account suspension.

VESC-004: KEYCLOAK ADMIN API PROTECTION
  Keycloak Admin REST API (/auth/admin/*) accessible only from internal network
  (k8s internal DNS) + PLATFORM_SECURITY_ADMIN JWT.
  Any external request to Keycloak Admin API = CRITICAL SECURITY EVENT.

VESC-005: DATABASE PRIVILEGE ESCALATION
  PostgreSQL superuser and schema-owner accounts are infrastructure accounts only.
  No application role may execute DDL statements (CREATE, ALTER, DROP).
  DDL execution by application account = VESC SEVERITY 1 + immediate DBA review.

MONITORING:
  MONITOR-VESC-001: Real-time — Kong logs all 403 tier-mismatch responses
  MONITOR-VESC-002: Real-time — Keycloak audit log → role self-assignment attempt → alert
  MONITOR-VESC-003: Real-time — PostgreSQL pgaudit → DDL by app account → CRITICAL alert
  MONITOR-VESC-004: Daily — verify admin API network policy blocks external access
  MONITOR-VESC-005: Weekly — enumerate all accounts with role-assignment authority;
                    verify each is an authorized person with signed agreement
```

---

## SECTION IX — ORPHANED & GHOST ACCOUNT DETECTION

### IX.1 Orphaned Account Definition

An **orphaned account** is an account that retains active role assignments despite the underlying user relationship having ended.

```
ORPHAN TYPE 1: DEPARTED EMPLOYEE
  Trigger: Team member's last working day has passed but Keycloak account remains ACTIVE
  Detection: HR system termination date < TODAY AND keycloak_account.enabled = TRUE
  SLA: Account deactivated within 2h of last working day (per CMA-AG-001 Section XIV)

ORPHAN TYPE 2: TERMINATED CONTRACTOR / INTERN
  Trigger: Contractor agreement end date has passed but system access remains
  Detection: contract_registry.expiry_date < TODAY AND keycloak_account.enabled = TRUE
  SLA: Account deactivated within 4h of contract end date

ORPHAN TYPE 3: TERMINATED FRANCHISE OWNER
  Trigger: Franchise agreement terminated but FRANCHISE_OWNER role remains active
  Detection: franchise_contract.status = TERMINATED AND keycloak_role.assigned = TRUE
  SLA: Access revoked within 24h of franchise termination (per CMA-AG-001 Section IX.1)

ORPHAN TYPE 4: TERMINATED COACH / COORDINATOR
  Trigger: Coach/Coordinator engagement ended but society-realm roles remain
  Detection: engagement_contract.status = TERMINATED AND keycloak_role.assigned = TRUE
  SLA: Access revoked within 24h of engagement termination
  Special: Non-solicitation 12-month timer starts at deactivation

ORPHAN TYPE 5: EXPIRED API DEVELOPER
  Trigger: Developer agreement expired/terminated but API key remains active
  Detection: developer_agreement.status = EXPIRED AND api_key.status = ACTIVE
  SLA: API key revoked within 60 seconds of agreement expiry (Kong plugin)

ORPHAN TYPE 6: INSTITUTION DEPARTURE
  Trigger: Institution TPO or admin whose institution's MOU has ended
  Detection: institution_mou.status = TERMINATED AND role_assignment.active = TRUE
  SLA: Access revoked within 24h

ORPHAN TYPE 7: MINOR TURNED 18 — GUARDIAN ACCESS
  Trigger: Minor account holder has turned 18 but PARENT_GUARDIAN access to their
           account has not been revoked per the age-18 protocol
  Detection: user.date_of_birth + 18 years ≤ TODAY AND guardian_link.active = TRUE
  SLA: Guardian access auto-revoked exactly on 18th birthday (Temporal workflow)
  Note: Minor's STUDENT_MINOR extension replaced by full STUDENT role on 18th birthday
```

### IX.2 Ghost Account Detection

A **ghost account** is a registered account that has never been used, or has been completely inactive for a period exceeding the declared threshold, yet retains active role assignments.

```
GHOST TYPE 1: NEVER-USED ACCOUNT
  Definition: Account created > 30 days ago with zero login activity
  Detection:  account.created_at < (NOW() - 30 days) AND account.last_login IS NULL
  Action:     Email verification reminder; suspend after 60 days if no activity
  Exception:  CI/CD service accounts may never login interactively — exclude SERVICE_ACCOUNT

GHOST TYPE 2: LONG-INACTIVE USER ACCOUNT
  Definition: Account with no login in excess of role's inactivity_revoke_days threshold
  Detection:  account.last_login < (NOW() - role_registry.inactivity_revoke_days)
  Action:
    STUDENT: 90 days inactive → email re-engagement; 180 days → access suspended
    TRAINER: 60 days inactive → review; 120 days → access suspended
    ADMIN roles: 30 days inactive → immediate CSO review required
    FRANCHISE_OWNER: 45 days inactive → franchise minimum activity check triggered

GHOST TYPE 3: INCOMPLETE ONBOARDING
  Definition: Account has a role assigned but has not completed mandatory onboarding steps
              (e.g., Trainer with active TRAINER role but no signed Trainer Agreement)
  Detection:  role requires_contract = TRUE AND no linked active contract in contract_registry
  Action:     Access to full role capabilities restricted until onboarding complete

GHOST TYPE 4: UNVERIFIED ROLE-SPECIFIC REQUIREMENT
  Definition: Account holds a role that requires a specific verification but verification
              is not on file (e.g., TRAINER with minor_facing = TRUE but no background check)
  Detection:  role.requires_background_check = TRUE AND user.background_check_status ≠ 'CLEARED'
  Action:     Immediate restriction of minor_facing sessions; CSO + DPO notified
```

---

## SECTION X — STALE ROLE & INACTIVE PRIVILEGE DETECTION

### X.1 Stale Role Detection Rules

```
STALE-001: UNUSED PERMISSION DETECTION
  Rule: Any permission within a role's permission set that has not been exercised
        (no API call using that permission) within the role's inactivity_revoke_days
        threshold is flagged as STALE.
  Action: CSO review; consider role right-sizing (remove unused permissions from role definition)
  Note: This drives Permission Minimization — roles should be trimmed to what's actually used.

STALE-002: KEYCLOAK CLIENT SCOPE STALENESS
  Rule: Any Keycloak client scope that has not been requested in any token in 90 days
        is flagged for review.
  Action: CSO review; decommission if confirmed unused
  Note: Dead client scopes are attack surface — they remain in token grant flow unused.

STALE-003: INACTIVE ROLE ASSIGNMENT
  Rule: A role assignment that has not resulted in any login or API call for
        > role.inactivity_revoke_days is flagged STALE.
  Action:
    STALE admin role: Immediate CSO review + dual approval to reactivate
    STALE user role: Email notification + 14-day reactivation window; then suspend

STALE-004: EXPIRED SERVICE ACCOUNT CREDENTIAL
  Rule: Service account Vault-issued credential older than 1h is expired.
  Action: Automatic credential rotation; any credential used after expiry = SECURITY EVENT

STALE-005: API KEY STALENESS
  Rule: API key not used in 90 days is flagged; not used in 180 days → auto-revoke
  Action:
    90 days: Email notification to developer + CCO flag
    180 days: API key revoked; developer agreement status reviewed

STALE-006: REFRESH TOKEN STALENESS
  Rule: Refresh token not used in 7 days is revoked (user must re-authenticate)
  Action: Silent revocation; user prompted to re-login on next access attempt
```

---

## SECTION XI — MULTI-ROLE CONFLICT DETECTION

### XI.1 Conflicting Role Pairs (Zero Tolerance)

The following role combinations may NEVER coexist on the same account. Detection = SEVERITY 1 or 2 depending on pair. The `conflicting_roles` field in the role_registry declares this for each role.

```
CONFLICT PAIR 1: STUDENT + RECRUITER_HR [SEVERITY 1]
  Reason: Student should not access recruiter screens; creates unfair advantage
          and potential data integrity risk in placement pipeline.
  Detection: Account has both STUDENT and RECRUITER_HR roles simultaneously.

CONFLICT PAIR 2: STUDENT + TRAINER [SEVERITY 2]
  Reason: Trainer has access to other students' enrollment data — conflict of interest.
  Exception: A graduated ALUMNI who also teaches may hold ALUMNI + TRAINER.
  Detection: Active STUDENT (not ALUMNI) + TRAINER simultaneously.

CONFLICT PAIR 3: STUDENT + EVALUATOR [SEVERITY 1]
  Reason: Student cannot evaluate their own peers in assessments they also take.
  Detection: STUDENT + EVALUATOR on same account.

CONFLICT PAIR 4: FRANCHISE_OWNER + COACH (same territory) [SEVERITY 2]
  Reason: Financial conflict of interest; owner grading own coaching creates
          commission manipulation risk.
  Exception: FRANCHISE_OWNER + COACH permitted if territories are different (different franchise units).
  Detection: same territory_id in both role assignment attributes.

CONFLICT PAIR 5: RECRUITER_HR + INSTITUTE_TPO [SEVERITY 2]
  Reason: Corporate recruiter should not also act as institutional placement officer
          — creates bias in campus placement pipeline.
  Detection: RECRUITER_HR + INSTITUTE_TPO simultaneously.

CONFLICT PAIR 6: API_DEVELOPER + any ADMIN role [SEVERITY 1]
  Reason: External developer accounts must not have internal administrative access.
  Detection: API_DEVELOPER + any TIER 0–1 role simultaneously.

CONFLICT PAIR 7: SERVICE_ACCOUNT + any human role [SEVERITY 1]
  Reason: Machine identities must be separate from human identities.
  Detection: SERVICE_ACCOUNT or AI_AGENT + any TIER 0–5 human role.

CONFLICT PAIR 8: PARENT_GUARDIAN + STUDENT (same tenant) [SEVERITY 2]
  Reason: Parent must not also be a student in the same tenant — creates access boundary blur.
  Exception: Different tenants (parent enrolled in adult learning platform, minor in schools) permitted.
  Detection: PARENT_GUARDIAN + STUDENT under same tenant_id.

CONFLICT PAIR 9: PLATFORM_COMPLIANCE_ADMIN + any Financial Role [SEVERITY 1]
  Reason: Compliance auditor must not also approve financial transactions — independence required.
  Detection: PLATFORM_COMPLIANCE_ADMIN + TENANT_BILLING_ADMIN simultaneously.

CONFLICT PAIR 10: INTERN_DEVELOPER + any Production Access [SEVERITY 1]
  Reason: Interns operate in DEV environment only; no production access.
  Detection: INTERN_DEVELOPER + any role scoped to production environment.
```

### XI.2 Conflict Detection Implementation

```sql
-- Trigger: Fires on every role assignment INSERT or UPDATE
CREATE OR REPLACE FUNCTION check_role_conflict()
RETURNS TRIGGER AS $$
DECLARE
    conflict_list TEXT[];
    existing_role TEXT;
BEGIN
    -- Load conflicting roles for the newly assigned role
    SELECT conflicting_roles INTO conflict_list
    FROM role_registry WHERE role_id = NEW.role_id;

    -- Check if account already holds any conflicting role
    FOR existing_role IN
        SELECT role_id FROM user_role_assignments
        WHERE account_id = NEW.account_id
        AND status = 'ACTIVE'
        AND role_id = ANY(conflict_list)
    LOOP
        -- Emit conflict event to Kafka
        PERFORM pg_notify('role_conflict_detected', json_build_object(
            'account_id', NEW.account_id,
            'new_role', NEW.role_id,
            'conflicting_role', existing_role,
            'detected_at', NOW()
        )::text);
        -- Block the assignment
        RAISE EXCEPTION 'ROLE_CONFLICT: % conflicts with existing role % on account %',
            NEW.role_id, existing_role, NEW.account_id;
    END LOOP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER enforce_role_conflict_check
    BEFORE INSERT OR UPDATE ON user_role_assignments
    FOR EACH ROW EXECUTE FUNCTION check_role_conflict();
```

---

## SECTION XII — SERVICE ACCOUNT & AI AGENT ROLE GOVERNANCE

### XII.1 Service Account Governance

Service accounts are machine identities with no human backing. They require stricter governance than human accounts because they often have broad, automated access.

```
SERVICE ACCOUNT LAW:

SA-001: ONE SERVICE = ONE ACCOUNT
  Each microservice has exactly one service account.
  No service shares a service account with another service.
  Shared service accounts = OVERLOAD TYPE 1 for machine identities.

SA-002: MINIMUM SCOPE DECLARATION
  Service account scope must be declared in the Service Mesh policy.
  Scope = exact set of API endpoints the service calls.
  Any API call outside declared scope = SERVICE ACCOUNT OVERLOAD event.

SA-003: VAULT-ISSUED CREDENTIALS ONLY
  Service accounts authenticate via Vault-issued dynamic credentials.
  Static service account passwords = PROHIBITED.
  Vault credential TTL = 1h; auto-rotated.
  Any static credential detected = TIER 1 SECURITY BREACH.

SA-004: NO INTERACTIVE SESSIONS
  Service account JWT cannot be used for browser-based or mobile app sessions.
  user-agent header from a browser or mobile SDK = SERVICE ACCOUNT MISUSE event.

SA-005: AUDIT EVERY CALL
  Every API call by a service account is logged with: service_name, endpoint,
  method, status_code, request_size, response_size, timestamp.
  No service account calls are exempt from audit logging.

SA-006: SERVICE ACCOUNT INVENTORY
  Complete inventory maintained in service_account_registry table.
  Undeclared service accounts in any realm = UNAUTHORIZED IDENTITY event.

MONITORING:
  MONITOR-SA-001: Daily — compare Keycloak service-realm clients against inventory
  MONITOR-SA-002: Real-time — service account calling undeclared endpoint → alert
  MONITOR-SA-003: Hourly — verify Vault credential rotation occurred within TTL
  MONITOR-SA-004: Real-time — browser user-agent from service account JWT → CRITICAL alert
```

### XII.2 AI Agent Role Governance

```
AI AGENT LAW (from System Constitution — Non-Negotiable):
  "AI advises only. AI never approves, blocks, or overrides humans."

AI-001: READ SCOPE ONLY FOR CORE AI FUNCTIONS
  AI matching, ranking, and recommendation agents have READ-ONLY access
  to declared data sets. They cannot write to user profiles, scores, or decisions.

AI-002: ADVISORY FLAG IS MANDATORY
  Every output from an AI agent must carry advisory_only = TRUE flag in the response.
  This flag must be surfaced in the UI before any human acts on AI output.
  AI output without advisory_only flag = BLOCKED at API response layer.

AI-003: AI CANNOT ASSIGN ROLES
  AI_AGENT role cannot call any RBAC management endpoint.
  AI cannot suggest, draft, or queue a role assignment — these are human-only actions.

AI-004: AI TRAINING DATA GOVERNANCE
  AI agents that train on platform data must verify ai_training_consent = TRUE
  for every data record used in training.
  Training job without consent verification gate = TIER 1 DATA BREACH.

AI-005: AI AGENT ACTIVITY LOG
  All AI agent API calls logged with: agent_id, model_version, input_hash,
  output_hash, advisory_flag_present, consent_verified (for training jobs), timestamp.

MONITORING:
  MONITOR-AI-001: On every AI response — verify advisory_only = TRUE flag present
  MONITOR-AI-002: Before every training run — verify consent gate completion
  MONITOR-AI-003: Daily — verify AI_AGENT role has no write permissions to user profile tables
  MONITOR-AI-004: Weekly — AI agent activity audit: scope adherence check
```

---

## SECTION XIII — MINOR & PARENT ROLE PROTECTION PROTOCOLS

### XIII.1 Minor Account Hardening

All accounts with minor_flag = TRUE (under-18) receive the STUDENT_MINOR role extension. This extension is system-enforced and cannot be removed by any role below PLATFORM_COMPLIANCE_ADMIN.

```
MINOR PROTECTION RULES:

MINOR-001: FINANCIAL TRANSACTION BLOCK
  All payment, purchase, wallet top-up, and payout endpoints return 403
  for any request with minor_flag = TRUE in JWT.
  Block is enforced at Kong gateway — not just at UI level.
  Keycloak client scope 'financial_transactions' is excluded from STUDENT_MINOR tokens.

MINOR-002: ADULT CONTENT BLOCK
  Content-type = 'adult', 'explicit', or content_rating ≥ 'PG-13' is blocked
  at content delivery service for minor accounts.
  Content_rating check occurs at API layer — not just at UI.

MINOR-003: VOICE GD GUARDIAN GATE
  Voice GD session start API requires guardian_consent_verified = TRUE
  for minor accounts. Session cannot start without this flag.
  Guardian verification is a separate async flow — not inline with session start.

MINOR-004: DOJO VIDEO GUARDIAN GATE
  Replay consent for minor accounts requires guardian co-consent.
  System checks guardian_consent_for_video = TRUE before any recording.

MINOR-005: INNOVATION ENGINE COMMERCIALIZATION LOCK
  Minor accounts cannot execute royalty payouts or sign licensing agreements.
  innovation_engine.commercialization_enabled = FALSE for all minor accounts.
  Royalties auto-route to escrow_account until age-18 transfer.

MINOR-006: AGE-18 TRANSITION AUTOMATION (Temporal Workflow)
  Trigger: user.date_of_birth + 18 years = TODAY (checked daily at 00:01 IST)
  Steps:
    1. Set minor_flag = FALSE in user record
    2. Remove STUDENT_MINOR role extension from Keycloak
    3. Revoke all existing tokens (force re-authentication)
    4. Send ownership transfer notification to user (now adult)
    5. Revoke PARENT_GUARDIAN access to account
    6. Send guardian account revocation notice
    7. Release royalty escrow to user wallet (if applicable)
    8. Log in immutable audit trail: minor_to_adult_transition_completed = TRUE

MINOR-007: MINOR ACCOUNT CANNOT BE CREATED BY MINOR
  Registration flow detects DOB indicating under-18 → requires guardian account
  creation and 8-step Minor Consent Protocol before account is activated.
  Self-registration completing without guardian step = BLOCKED at registration API.

MONITORING:
  MONITOR-MINOR-001: Real-time — any financial endpoint hit by minor_flag account → CRITICAL alert
  MONITOR-MINOR-002: Daily — scan for minor accounts missing STUDENT_MINOR role extension
  MONITOR-MINOR-003: Daily — scan for minor accounts approaching 18th birthday (7-day advance)
  MONITOR-MINOR-004: Daily — verify guardian_link exists for 100% of minor accounts
  MONITOR-MINOR-005: Weekly — scan for minor accounts with active royalty payout records
                     (should be zero — all in escrow)
```

### XIII.2 Parent/Guardian Role Controls

```
PARENT-001: STRICTLY READ-ONLY ENFORCEMENT
  PARENT_GUARDIAN Keycloak client scope grants: openid, profile:read, activity:read
  No write scope is issued. No exception.
  Any attempt by PARENT_GUARDIAN to call a write endpoint = 403; security event logged.

PARENT-002: GUARDIAN LINKED TO SPECIFIC MINOR
  Guardian access is scoped to exactly one minor account (guardian_id → minor_account_id mapping).
  Guardian cannot access any other minor's account — even siblings.
  Each sibling minor requires a separate guardian account (or separate guardian_id binding).

PARENT-003: CONSENT ACTIONS ARE GUARDIAN-SPECIFIC APIs
  Consent actions (session consent, video consent, activity consent) are not
  performed via the PARENT_GUARDIAN role screens — they use a dedicated
  guardian consent API with explicit guardian_id + minor_account_id pairing.
  Consent recorded in immutable consent_records table (per DPA-AG-001).

PARENT-004: GUARDIAN ACCOUNT REQUIRES OWN VERIFICATION
  Guardian accounts must complete identity verification before being linked
  to a minor account. Unverified guardian = 8-step Minor Consent Protocol blocked.

MONITORING:
  MONITOR-PARENT-001: Real-time — any write call from PARENT_GUARDIAN JWT → CRITICAL alert
  MONITOR-PARENT-002: Daily — verify every PARENT_GUARDIAN account has exactly one
                      active guardian_link record
  MONITOR-PARENT-003: Daily — check for PARENT_GUARDIAN accounts where linked minor
                      has turned 18 but guardian access not yet revoked
```

---

## SECTION XIV — ADMIN & OPS CONSOLE ROLE GOVERNANCE

### XIV.1 Console Access Hardening

The Internal Admin & Ops Console (R21/R40) is a completely separate application with its own security boundary.

```
CONSOLE-001: PHYSICAL SEPARATION
  Admin console is deployed in a separate Kubernetes namespace (ops-console-namespace).
  Admin console has separate ingress domain (ops.{LOCK_DOMAIN}).
  Admin console code is in a separate build target — NOT compiled into user apps.
  Network policy: ops-console-namespace blocks all inbound from user-facing namespaces.

CONSOLE-002: ADMIN-ONLY ROLES REQUIRED
  Access requires one of: PLATFORM_SUPER_ADMIN, PLATFORM_OPS_ADMIN,
  PLATFORM_COMPLIANCE_ADMIN, PLATFORM_SECURITY_ADMIN, TENANT_ADMIN (for own tenant).
  No other role may access the console. No exceptions.

CONSOLE-003: MANDATORY MFA FOR ALL CONSOLE ACCESS
  MFA is enforced at Keycloak — not optional for console access.
  Console login without completed MFA = 401 from Kong.
  MFA challenge timeout = 5 minutes; failed challenge = account locked for 30 minutes.

CONSOLE-004: IP ALLOWLIST FOR TIER 0
  PLATFORM_SUPER_ADMIN console access restricted to declared corporate IP ranges.
  IP not in allowlist + SUPER_ADMIN role = rejected at Kong IP restriction plugin.
  Allowlist changes require CSO + Founder co-sign.

CONSOLE-005: EMERGENCY CONTROLS ARE DUAL-AUTHORIZED
  Global feature kill-switch: requires TWO admin accounts to activate simultaneously.
  Platform maintenance mode: requires PLATFORM_OPS_ADMIN + CTO dual-sign.
  User account lock/unlock: single admin; logged with full justification text.
  Tenant suspension: PLATFORM_OPS_ADMIN + Founder co-sign required.

CONSOLE-006: CONSOLE SESSION AUDIT
  Every console action (screen view, button click, data query, action executed)
  is logged in the immutable console_audit_log table.
  Console audit log is append-only; no session is exempt from logging.
  Console audit log retained for 7 years.

MONITORING:
  MONITOR-CONSOLE-001: Real-time — any non-admin JWT hitting ops.* domain → CRITICAL alert
  MONITOR-CONSOLE-002: Real-time — emergency control action without dual-auth → BLOCKED + alert
  MONITOR-CONSOLE-003: Daily — verify console namespace network policy blocks user-namespace inbound
  MONITOR-CONSOLE-004: Weekly — review all console sessions: actions logged, anomalies flagged
  MONITOR-CONSOLE-005: Monthly — console admin account review: verify all accounts are active staff
```

---

## SECTION XV — ROLE LIFECYCLE STATE MACHINE

Every user role assignment follows this state machine, enforced by Temporal workflow.

### XV.1 Role Assignment Lifecycle

```
States:
  PENDING_VERIFICATION  — Assignment requested; awaiting required checks
  ACTIVE                — Fully verified; all requirements met
  ACTIVE_LIMITED        — Provisionally active; missing non-critical requirements (e.g., profile incomplete)
  PROBATION             — Newly assigned; under enhanced monitoring (first 30 days for admin roles)
  SUSPENDED             — Temporarily suspended (security event, investigation, inactivity)
  REVOKED               — Permanently removed (termination, violation, conflict resolution)
  ARCHIVED              — Historically retained in immutable log; no system access

State Transitions (Temporal Workflows):

PENDING_VERIFICATION → ACTIVE
  Gate: All requires_ fields satisfied (background_check, contract, MFA, identity_verification)
  Operator: Temporal RoleActivationWorkflow
  Failure: Remains PENDING; alert to role owner + CCO

ACTIVE → SUSPENDED
  Triggers:
    - Security event detected (any SEVERITY 1 or 2 overload)
    - Inactivity threshold exceeded
    - Contract expiry (where requires_contract = TRUE)
    - CSO manual suspension
  Operator: Temporal RoleSuspensionWorkflow (automated) or CSO (manual)
  Effect: Keycloak role removed; all tokens revoked; session terminated

SUSPENDED → ACTIVE
  Gate: Suspension reason resolved + CCO sign-off (SEVERITY 2)
        CSO + Founder co-sign (SEVERITY 1 suspension)
  Operator: Manual reactivation via Internal Ops Console

ACTIVE → REVOKED
  Triggers:
    - Account holder termination (employment, contractor, franchise)
    - Permanent ban for platform violation
    - Role conflict detected and resolved against this role
    - Court order or regulatory direction
  Operator: Temporal RoleRevocationWorkflow
  Effect: Keycloak role permanently removed; account deactivated

REVOKED → ARCHIVED
  Time: Immediately on revocation
  Effect: Immutable record in user_role_history; role_id + revocation_reason + timestamp

Role Review Cycle (Temporal Scheduled Workflow — runs quarterly):
  1. Load all ACTIVE role assignments
  2. For each: check last_reviewed_at ≤ 90 days ago
  3. Flag overdue reviews to CSO + role owner
  4. Check all requires_ fields still satisfied (background check not lapsed, etc.)
  5. Generate quarterly Role Health Report
  6. Flag any STALE roles (inactivity threshold exceeded)
```

---

## SECTION XVI — REAL-TIME MONITORING, DETECTION & ALERTING

### XVI.1 Detection Event Taxonomy

All role overload and access control events are emitted to Kafka topic `role.events` and stored in the `role_security_events` table.

```
EVENT TAXONOMY:

RSOM-001: ROLE_OVERLOAD_DETECTED        — Any of the 10 overload types
RSOM-002: PRIVILEGE_ESCALATION_ATTEMPT  — Vertical or horizontal escalation
RSOM-003: CROSS_TENANT_ACCESS_ATTEMPT   — Cross-tenant request blocked
RSOM-004: CROSS_DOMAIN_ACCESS_ATTEMPT   — Cross-domain request blocked
RSOM-005: ADMIN_SURFACE_BREACH_ATTEMPT  — Non-admin hitting admin endpoint
RSOM-006: MINOR_FINANCIAL_BLOCK         — Minor account financial endpoint hit
RSOM-007: SERVICE_ACCOUNT_MISUSE        — Service account used interactively
RSOM-008: TOKEN_REPLAY_DETECTED         — Stale or replayed refresh token used
RSOM-009: ORPHANED_ROLE_DETECTED        — Active role on terminated account
RSOM-010: GHOST_ACCOUNT_DETECTED        — Inactive account with active roles
RSOM-011: STALE_PERMISSION_FLAGGED      — Permission unused for > threshold
RSOM-012: ROLE_CONFLICT_DETECTED        — Conflicting role combination found
RSOM-013: UNAUTHORIZED_ROLE_DETECTED    — Role not in canonical Role Registry
RSOM-014: RLS_BYPASS_ATTEMPT            — PostgreSQL RLS bypass attempted
RSOM-015: MFA_BYPASS_ATTEMPT            — Admin endpoint hit without MFA-verified token
RSOM-016: CONSOLE_UNAUTHORIZED_ACCESS   — Non-admin hitting Ops Console domain
RSOM-017: RATE_LIMIT_BREACH             — API rate limit exceeded (repeated = abuse risk)
RSOM-018: BULK_DATA_HARVEST_ATTEMPT     — API key requesting >10,000 records/hour
RSOM-019: KEYCLOAK_ADMIN_API_EXTERNAL   — External request to Keycloak Admin API
RSOM-020: AI_ADVISORY_FLAG_MISSING      — AI response returned without advisory_only flag
```

### XVI.2 Alert Routing Matrix

```
EVENT               SEVERITY  CSO    DPO    CCO    CTO    FOUNDER  SLA
RSOM-001 (overload)   2–1     15min  1h*    —      —      4h*      *=SEVERITY 1 only
RSOM-002 (escalation) 1       15min  —      —      1h     4h
RSOM-003 (x-tenant)   1       15min  1h     —      1h     4h
RSOM-004 (x-domain)   2       1h     —      —      4h     —
RSOM-005 (admin hit)  1       15min  —      —      1h     4h
RSOM-006 (minor fin)  1       15min  1h     —      —      4h
RSOM-007 (svc acct)   1       15min  —      —      1h     4h
RSOM-008 (token rep)  1       15min  —      —      1h     —
RSOM-009 (orphan)     2       1h     —      1h     —      —
RSOM-010 (ghost)      3       24h    —      —      —      —
RSOM-011 (stale)      3       24h    —      —      —      —
RSOM-012 (conflict)   1–2     1h     —      —      —      —
RSOM-013 (unauth)     1       15min  —      —      1h     4h
RSOM-014 (RLS bypass) 1       15min  1h     —      1h     4h
RSOM-015 (MFA bypass) 1       15min  —      —      1h     4h
RSOM-016 (console)    1       15min  —      —      1h     4h
RSOM-018 (harvest)    2       1h     —      1h     1h     —
RSOM-019 (KC admin)   1       15min  —      —      1h     4h
RSOM-020 (AI flag)    2       1h     —      —      1h     —
```

### XVI.3 Monitoring Cadence Summary

```
REAL-TIME (every request):
  → Kong JWT validation: tenant_id, domain_track, role, mfa_verified, minor_flag
  → PostgreSQL RLS enforcement on every query
  → Admin endpoint access check
  → Financial endpoint minor_flag check
  → Role conflict check on every role assignment

EVERY 5 MINUTES:
  → Token blacklist integrity (Redis)
  → Active session count per account (concurrent session enforcement)
  → Service account credential TTL check

HOURLY:
  → Scan for tokens with lifetime exceeding declared maximum
  → Service account Vault credential rotation verification
  → Cross-domain 403 rate per account (threshold: >3/hour = alert)

DAILY (06:00 IST — Temporal Scheduled Workflow):
  → Orphaned account scan (all 7 orphan types)
  → Ghost account scan (all 4 ghost types)
  → Minor-to-adult transition check (approaching 18th birthday)
  → Stale permission scan (inactivity_revoke_days threshold)
  → PARENT_GUARDIAN access validity check
  → Keycloak realm inventory vs. declared realm list
  → API key activity check (90-day/180-day thresholds)
  → Background check status for all minor_facing trainers/coaches

WEEKLY:
  → Permission→Screen Matrix drift scan (compare live API endpoints vs. matrix)
  → Role→Widget Matrix completeness check
  → Admin console account census (verify all accounts are active staff)
  → Service account registry vs. Keycloak service-realm clients
  → Cross-domain 403 aggregate report by account
  → Franchise territory overlap scan
  → Corporate hierarchy level vs. endpoint access log audit

MONTHLY:
  → Privilege Minimization Audit (identify roles with unused permissions)
  → Cross-tenant penetration test (synthetic test account)
  → Token lifetime compliance (random sample of issued tokens)
  → MFA enforcement verification for all admin accounts
  → Role assignment authority census (who can assign roles and should they still be able to)

QUARTERLY:
  → Full Role Health Review (all active role assignments reviewed)
  → Keycloak client scope staleness audit
  → Permission Matrix version review
  → Minor protection protocol end-to-end test
  → Role conflict pair effectiveness review
  → Non-solicitation clause tracking (departing coaches/coordinators 12-month check)
```

---

## SECTION XVII — AUTOMATED RESPONSE PROTOCOLS

### XVII.1 Automated Response by Severity

```
SEVERITY 1 — AUTOMATIC CONTAINMENT (No Human Approval Required)

STEP 1: CONTAIN (within 60 seconds of detection)
  → Suspend the overloaded/unauthorized role in Keycloak
  → Revoke all active tokens for the affected account (Redis blacklist)
  → Terminate all active sessions for the affected account
  → Block all in-flight API requests from the account (Kong 503)
  → Quarantine the account (login prevented until CSO review)

STEP 2: LOG (within 60 seconds)
  → Write to role_security_events table (immutable)
  → Emit Kafka event: role.security_event.severity_1
  → Link to audit_findings in SAA-AG-001

STEP 3: ALERT (within 15 minutes)
  → CSO: push notification + email + Ops Console banner
  → Founder (where required): push + email within 4h
  → DPO (where data-involved): push + email within 1h

STEP 4: PRESERVE EVIDENCE
  → Snapshot of account's role assignments at time of detection
  → Snapshot of relevant audit log entries (last 24h)
  → Kong request logs for the account (last 4h)
  → PostgreSQL query audit for the account (last 4h)
  → Stored in security_evidence_archive (WORM bucket)

STEP 5: HUMAN REVIEW GATE
  → Account remains quarantined until CSO completes review
  → CSO + Founder dual-sign required for SEVERITY 1 reactivation
  → No automatic reactivation regardless of time elapsed

SEVERITY 2 — QUARANTINE + ALERT (15-minute SLA)

  → Suspect role quarantined (not full account suspension)
  → Active tokens with quarantined role revoked only
  → CSO + DPO (if relevant) notified within 1h
  → Human review SLA: 4h from detection
  → Automatic escalation to SEVERITY 1 if human review not completed in 4h

SEVERITY 3 — FLAG + SCHEDULE (24-hour SLA)

  → Account/role flagged in Role Overload Dashboard
  → CSO notified in daily digest (not urgent push)
  → Scheduled review created in Temporal (due within 72h)
  → No automatic suspension (stale/inactive — not active threat)

SEVERITY 4 — LOG + TRACK

  → Logged in role_security_events with severity = 4
  → Appears in weekly Role Governance Report
  → No immediate alert; addressed in next scheduled review
```

### XVII.2 Automated Temporal Workflows for Role Management

```
WORKFLOW: RoleOrphanCleanupWorkflow (Daily)
  1. Query all accounts where user_status = TERMINATED and keycloak_role.active = TRUE
  2. For each orphaned account:
     a. Revoke all Keycloak roles
     b. Disable Keycloak account
     c. Revoke all active tokens
     d. Emit RSOM-009 event
     e. Log to role_security_events
  3. Generate cleanup report → CSO

WORKFLOW: GhostAccountSuspensionWorkflow (Daily)
  1. Query accounts with last_login < (NOW() - inactivity_revoke_days)
  2. For each ghost account:
     a. Send re-engagement email (first detection)
     b. After 14 days with no response: suspend account
     c. Emit RSOM-010 event
     d. Log to role_security_events

WORKFLOW: MinorAgeTransitionWorkflow (Daily at 00:01 IST)
  1. Query users where date_of_birth + 18 years = TODAY
  2. For each turning-18 account:
     a. Execute 8-step age-18 transition (per Section XIII.1 MINOR-006)
     b. Log all steps in immutable audit trail

WORKFLOW: RoleReviewReminderWorkflow (Weekly Monday 09:00 IST)
  1. Load all ACTIVE role assignments where last_reviewed_at > 90 days ago
  2. Send review reminder to role owner + CSO
  3. Escalate to CSO if admin role review overdue > 30 days

WORKFLOW: StalePermissionAuditWorkflow (Monthly)
  1. Load all role→permission mappings
  2. Join with API call logs: find permissions with zero usage in 90 days
  3. Flag as STALE-001; report to CSO for role right-sizing consideration
```

---

## SECTION XVIII — IMMUTABLE ROLE AUDIT LOG ARCHITECTURE

### XVIII.1 Role Security Events Schema

```sql
CREATE TABLE role_security_events (
    event_id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type          VARCHAR(50)  NOT NULL,
    -- RSOM-001 through RSOM-020 codes
    severity            INTEGER      NOT NULL CHECK (severity BETWEEN 1 AND 4),
    account_id          UUID         NOT NULL,
    account_email_hash  VARCHAR(64),
    -- SHA-256 of email — PII-safe for security logs
    role_id             VARCHAR(100),
    tenant_id           VARCHAR(100),
    domain_track        VARCHAR(50),
    description         TEXT         NOT NULL,
    evidence_snapshot   JSONB,
    -- Role assignments, token claims, request context at detection time
    automated_action    VARCHAR(200),
    -- Action taken automatically (SUSPENDED, QUARANTINED, BLOCKED, NONE)
    automated_action_at TIMESTAMPTZ,
    cso_review_status   VARCHAR(50)  DEFAULT 'PENDING',
    cso_reviewed_by     VARCHAR(200),
    cso_reviewed_at     TIMESTAMPTZ,
    cso_resolution      TEXT,
    reactivated_by      VARCHAR(200),
    reactivated_at      TIMESTAMPTZ,
    reactivation_justification TEXT,
    linked_audit_finding_id VARCHAR(50),
    linked_contract_id  VARCHAR(50),
    ip_hash             VARCHAR(64),
    user_agent_hash     VARCHAR(64),
    request_id          UUID,
    detected_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW()
    -- APPEND-ONLY: No UPDATE/DELETE permitted on this table
);

-- Immutability triggers
CREATE RULE no_update_role_events AS ON UPDATE TO role_security_events DO INSTEAD NOTHING;
CREATE RULE no_delete_role_events AS ON DELETE TO role_security_events DO INSTEAD NOTHING;

-- Role assignment history (immutable)
CREATE TABLE user_role_history (
    history_id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id          UUID         NOT NULL,
    role_id             VARCHAR(100) NOT NULL,
    action              VARCHAR(50)  NOT NULL,
    -- ASSIGNED | SUSPENDED | REVOKED | REACTIVATED | TRANSITIONED | EXPIRED
    action_by           VARCHAR(200) NOT NULL,
    action_reason       TEXT,
    prior_status        VARCHAR(50),
    new_status          VARCHAR(50),
    effective_at        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    evidence_path       TEXT
    -- APPEND-ONLY
);
```

### XVIII.2 Retention & WORM Policy

```
role_security_events:          7 years (WORM after 90 days in PostgreSQL hot store)
user_role_history:             10 years (WORM; legal/compliance evidence)
console_audit_log:             7 years (WORM)
permission_screen_matrix:      Permanent (all versions archived; never deleted)
role_registry:                 Permanent (is_active = FALSE to decommission; never DELETE)
minor protection audit logs:   Until subject age 26 + 2 years
```

---

## SECTION XIX — ROLE OVERLOAD DASHBOARD & REPORTING

### XIX.1 Dashboard Panels (Internal Ops Console — R21/R40)

```
PANEL 1: ROLE HEALTH OVERVIEW
  → Total active role assignments by tier
  → Accounts with overloaded roles (count by overload type)
  → Accounts in SUSPENDED / QUARANTINED state
  → Security events in past 24h (by severity)
  → Trend: security events over last 30 days

PANEL 2: ORPHAN & GHOST TRACKER
  → Orphaned accounts (by orphan type) with days since trigger
  → Ghost accounts (inactive beyond threshold)
  → Accounts pending age-18 transition (next 30 days)
  → PARENT_GUARDIAN accounts pending revocation

PANEL 3: PRIVILEGE ESCALATION LOG
  → Cross-tenant access attempts (last 7 days)
  → Cross-domain access attempts (last 7 days)
  → Admin surface breach attempts (last 7 days)
  → Vertical escalation events (last 7 days)

PANEL 4: MINOR PROTECTION STATUS
  → Minor accounts count (by domain)
  → Accounts with STUDENT_MINOR extension (should = minor account count)
  → Minor accounts with active guardian link (should = 100%)
  → Minor accounts with financial block verified
  → Upcoming age-18 transitions (next 30 days)

PANEL 5: SERVICE ACCOUNT HEALTH
  → Service account count vs. declared inventory
  → Service accounts with credential age > 30 min (approaching 1h TTL)
  → Service account API calls to undeclared endpoints (last 24h)
  → AI_AGENT advisory flag compliance rate (last 24h)

PANEL 6: PERMISSION MATRIX COMPLIANCE
  → Matrix version (current vs. deployed)
  → Last R49 validator run result + timestamp
  → Drift alerts (undeclared endpoints detected)
  → Role review overdue count (admin roles / user roles)

PANEL 7: KEYCLOAK REALM HEALTH
  → Undeclared realms detected (should be zero)
  → Undeclared client scopes in any realm (should be zero)
  → Tokens issued vs. tokens revoked (last 24h)
  → MFA enforcement rate by role tier
```

### XIX.2 Reporting Schedule

```
DAILY (06:30 IST — automated):
  → Overnight security event digest (all events while humans were offline)
  → Orphan/ghost account actions taken
  → Minor protection status summary
  Recipients: CSO, DPO (if data events), CTO

WEEKLY (Monday 09:00 IST):
  → Role Governance Weekly Report:
    All SEVERITY 3/4 events, stale permission flags, review reminders,
    admin console session anomalies, service account health
  Recipients: CSO, CTO, CCO

MONTHLY:
  → Role Minimization Report: permissions flagged as unused; right-sizing recommendations
  → Access Control Posture Report: tenant isolation test results, cross-domain stats
  → Minor Protection Monthly Review
  Recipients: CSO, DPO, CCO, Legal Officer

QUARTERLY:
  → Comprehensive Role Health Report (sign-off required):
    Full role assignment review results, conflict pair effectiveness,
    Keycloak realm audit, permission matrix currency, ghost account actions,
    privilege escalation trend analysis, minor protection audit
  → Included in Quarterly Compliance Report (cross-reference SAA-AG-001 Section XVIII)
  Sign-off: CSO + DPO + Founder
  Recipients: CSO, DPO, CCO, CAO, Founders + Board summary
```

---

## SECTION XX — ROLES, SIGN-OFF MATRIX & SEAL MECHANICS

### XX.1 Mandatory Role Governance Roles

```
Chief Security Officer (CSO):
  Authority: Supreme role governance authority; SEVERITY 1 response lead;
             Keycloak realm changes; Role Registry updates; emergency account actions
  Cannot be: DPO or Legal Officer simultaneously
  MFA: Hardware key mandatory

Data Protection Officer (DPO):
  Authority: Minor protection oversight; data-involved access control events;
             consent chain integrity for role-based data access
  Cross-ref: DPA-AG-001 Section XII; LDM-AG-001 Section XIV

Chief Contract Officer (CCO):
  Authority: Contract-linked role validations (requires_contract = TRUE);
             orphan account detection trigger (contract expiry)
  Cross-ref: CMA-AG-001 Section XVI

Chief Audit Officer (CAO):
  Authority: Role governance findings in audit registry; quarterly sign-off
  Cross-ref: SAA-AG-001 Section XVII

CTO:
  Authority: Technical implementation of role controls; Keycloak configuration;
             Kong gateway policy; service account governance
```

### XX.2 Sign-Off Matrix

| Action | CSO | DPO | CCO | CAO | Founder |
|---|---|---|---|---|---|
| Role Registry addition | ✅ | If minor/data role | | | |
| Role Registry decommission | ✅ | If minor/data role | | | ✅ |
| Permission Matrix update | ✅ | | | | |
| SEVERITY 1 account reactivation | ✅ | | | | ✅ |
| SEVERITY 2 account reactivation | ✅ | | | | |
| New Keycloak realm creation | ✅ | | | | |
| IP allowlist update (SUPER_ADMIN) | ✅ | | | | ✅ |
| Emergency console dual-auth actions | ✅ + CTO | | | | |
| Minor role extension removal | Prohibited below PLATFORM_COMPLIANCE_ADMIN | | | | |
| Role conflict pair update | ✅ | | | | |
| Quarterly Role Health Report | ✅ | ✅ | | ✅ | ✅ (Board version) |
| This document amendment | ✅ | ✅ | | | ✅ |

### XX.3 Supremacy Declaration

This ROLE_OVERLOAD_MONITOR_AGENT.md is the **supreme role governance, privilege monitoring, and access control integrity instrument** for the Antigravity/Ecoskiller platform. No business request, feature sprint, investor timeline, or AI agent output may:

- Add a role to any Keycloak realm without corresponding Role Registry entry
- Grant a permission that is not in the Permission→Screen Matrix
- Remove monitoring checks for any overload type
- Weaken any severity classification
- Allow a minor account to hold financial authority
- Permit cross-tenant or cross-domain access outside declared grants
- Override an automated SEVERITY 1 containment action before CSO review

### XX.4 Add-Only Mutation Policy

```
This document may be AMENDED by:
  → Chief Security Officer + Founder co-sign
  → Version bump (additions = v1.1.0; structural changes = v2.0.0)
  → Previous version archived in WORM storage with SHA-256 hash
  → R49 Contract Validator re-run with updated Role Registry before merge

PROHIBITED mutations:
  → Removing any declared role from the Role Registry
  → Reducing overload type count or severity thresholds
  → Removing any role conflict pair
  → Weakening minor protection rules
  → Relaxing token lifetime limits
  → Reducing monitoring cadence for any check category
```

### XX.5 Binding Scope

This document binds every entity operating within or interfacing with the platform:
- Every developer, intern, contractor, and employee
- Every CI/CD pipeline, automated deployment system, and AI agent
- Every Keycloak realm administrator and database administrator
- Every franchise owner, coach, coordinator, and trainer with system access
- Every institution, employer, and API developer with platform integration

**No exceptions. No exemptions. No partial compliance.**

---

## APPENDIX A — QUICK REFERENCE: OVERLOAD TYPES

| Type | Name | Severity | Auto-Action |
|---|---|---|---|
| 1 | Role Count Excess | 2 | Quarantine excess role |
| 2 | Permission Scope Excess | 2 | Quarantine excess permissions |
| 3 | Cross-Tenant Privilege | 1 | Suspend account + revoke tokens |
| 4 | Cross-Domain Privilege | 1 | Suspend account + revoke tokens |
| 5 | Hierarchical Overreach | 2 | Quarantine excess level access |
| 6 | Unauthorized Admin Surface | 1 | Suspend account + revoke tokens |
| 7 | Minor Financial Overload | 1 | Block transaction + alert |
| 8 | Service Account Human Use | 1 | Revoke tokens + quarantine account |
| 9 | Token Scope Inflation | 2 | Revoke inflated token |
| 10 | Dormant Privilege Accumulation | 3 | Flag for review |

## APPENDIX B — QUICK REFERENCE: ROLE CONFLICT PAIRS

| Pair | Role A | Role B | Severity |
|---|---|---|---|
| 1 | STUDENT | RECRUITER_HR | 1 |
| 2 | STUDENT (active) | TRAINER | 2 |
| 3 | STUDENT | EVALUATOR | 1 |
| 4 | FRANCHISE_OWNER | COACH (same territory) | 2 |
| 5 | RECRUITER_HR | INSTITUTE_TPO | 2 |
| 6 | API_DEVELOPER | Any ADMIN role | 1 |
| 7 | SERVICE_ACCOUNT | Any human role | 1 |
| 8 | PARENT_GUARDIAN | STUDENT (same tenant) | 2 |
| 9 | PLATFORM_COMPLIANCE_ADMIN | TENANT_BILLING_ADMIN | 1 |
| 10 | INTERN_DEVELOPER | Any production role | 1 |

## APPENDIX C — CROSS-REFERENCE MAP

| Section | References | For |
|---|---|---|
| Section II (Role Registry) | CMA-AG-001 Class 6 | Employment agreement per role |
| Section IV (Permission Matrix) | SAA-AG-001 AUDIT-CORE-002 | R23 matrix audit checks |
| Section VI (Tenant Isolation) | DPA-AG-001 Section IV | RLS, encryption, access logging |
| Section VII (Token Governance) | DPA-AG-001 Section IV | JWT PII, session management |
| Section XIII (Minor Protection) | LDM-AG-001 Section V | Minor Consent Protocol |
| Section XIV (Admin Console) | SAA-AG-001 Section XIV | Ops Console role audit |
| Section XVI (Alerting) | SAA-AG-001 Section XVI | Finding creation, escalation |
| Section XVIII (Audit Log) | SAA-AG-001 Section XIX | Immutable log architecture |
| Section XIX (Reporting) | SAA-AG-001 Section XVIII | Quarterly compliance report |
| All minor rules | DPA-AG-001 Section VII | Minor data handling obligations |

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║       ROLE_OVERLOAD_MONITOR_AGENT.md — SEAL CONFIRMATION                ║
║                                                                          ║
║  Document ID:     ROMA-AG-001                                            ║
║  Version:         v1.0.0                                                 ║
║  Status:          SEALED · LOCKED · ENFORCED · NON-NEGOTIABLE            ║
║  Mutation Policy: ADD-ONLY via version bump                              ║
║  Override Policy: NONE — this document cannot be overridden              ║
║                                                                          ║
║  Authorized Signatories (required before production deployment):         ║
║                                                                          ║
║  Chief Security Officer: ______________________  Date: ________________  ║
║  Data Protection Officer:______________________  Date: ________________  ║
║  Chief Contract Officer: ______________________  Date: ________________  ║
║  Chief Audit Officer:    ______________________  Date: ________________  ║
║  CTO:                    ______________________  Date: ________________  ║
║  Founder:                ______________________  Date: ________________  ║
║                                                                          ║
║  WORM Archive Path: /audit-archive-worm/governance/ROMA-AG-001-v1.0.0   ║
║  SHA-256 (at seal): [COMPUTED AT TIME OF FIRST SIGNATURE]                ║
║                                                                          ║
║  This seal confirms: No clause in this document has been weakened,       ║
║  removed, or bypassed. All signatories accept personal accountability    ║
║  for enforcing this document within their domain of authority.           ║
║                                                                          ║
║  Co-Binding Documents:                                                   ║
║    DPA-AG-001 · LDM-AG-001 · SAA-AG-001 · CMA-AG-001                    ║
║  Conflict resolution: most restrictive clause prevails.                  ║
╚══════════════════════════════════════════════════════════════════════════╝
```
