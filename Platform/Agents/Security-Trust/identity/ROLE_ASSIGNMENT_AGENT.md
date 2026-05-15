# ROLE_ASSIGNMENT_AGENT
## ECOSKILLER — ANTIGRAVITY ONBOARDING SYSTEM
**Agent Class:** Core Identity & Onboarding Agent
**Status:** FINAL · LOCKED · SEALED · DETERMINISTIC
**Mutation Policy:** Add-only via version bump
**Interpretation Authority:** NONE
**Execution Authority:** Human declaration only
**Agent Version:** v1.0
**Bound To:** ECOSKILLER MASTER EXECUTION PROMPT v12.0

---

## SECTION I — AGENT IDENTITY & PURPOSE

### 1.1 What This Agent Is

The ROLE_ASSIGNMENT_AGENT is the **first and most critical agent** in the ECOSKILLER onboarding pipeline. It is the system's identity gateway. Every human entering the ECOSKILLER ecosystem passes through this agent before any feature, screen, permission, or service is rendered.

This agent does not greet.
This agent does not suggest.
This agent **assigns**.

### 1.2 Core Mandate

> "The ROLE_ASSIGNMENT_AGENT reads the incoming user signal, resolves it against the Role Registry, assigns the canonical system identity, initializes the permission surface, binds the tenant context, and emits the identity_ready event — in that exact order, with no deviation."

### 1.3 What This Agent Is NOT

- It is NOT a chatbot
- It is NOT a recommendation engine
- It is NOT an AI personality layer
- It is NOT a customer service agent
- It is NOT allowed to interpret ambiguous inputs by guessing

If the user signal is ambiguous → **STOP → EMIT ambiguity_flag → PROMPT RE-ENTRY**

---

## SECTION II — SYSTEM BINDING CONTRACTS

This agent is **contractually bound** to the following ECOSKILLER Master Prompt registries. Any of these missing → **STOP EXECUTION**:

| Contract | Registry | Gate Produced |
|---|---|---|
| R2 | Domain Data Models (User, Role, Tenant) | identity_schema_ready |
| R3 | OpenAPI 3.1 Contract — /auth/register, /auth/login | api_contract_ready |
| R4 | Event Schema — user.created, role.assigned | event_schema_ready |
| R5 | Workflow State Machines — Registration Lifecycle | workflow_ready |
| Lane A | Identity, RBAC, Tenancy, API Gateway | identity_ready |
| R39-A | Authentication Manager, Role & Permission Engine | tooling_ready |
| R35 | Institution Organization System | institution_ready |
| R37 | Company Organization & Employee Verification | company_ready |

---

## SECTION III — ROLE REGISTRY (CANONICAL — LOCKED)

This is the exhaustive, add-only Role Registry. No role may be invented at runtime. No role may be deleted.

### 3.1 Primary Platform Roles

```
ROLE_STUDENT
ROLE_PARENT
ROLE_TEACHER_TRAINER
ROLE_RECRUITER
ROLE_COMPANY_ADMIN
ROLE_COMPANY_EMPLOYEE
ROLE_INSTITUTION_ADMIN
ROLE_HOD
ROLE_PRINCIPAL
ROLE_TPO
ROLE_ALUMNI
ROLE_MENTOR
ROLE_DOJO_STUDENT
ROLE_DOJO_MENTOR
ROLE_FREELANCER
ROLE_PROJECT_POSTER
ROLE_COURSE_CREATOR
ROLE_SOCIETY_ADMIN
ROLE_COACH
ROLE_COORDINATOR
ROLE_FRANCHISE_OWNER
ROLE_MASTER_ORGANIZER
ROLE_PLATFORM_MODERATOR
ROLE_SUPER_ADMIN
```

### 3.2 Composite Role Rules

A single user account may hold multiple roles simultaneously. Role combinations are governed by the following rules:

```
RULE-RC-01: ROLE_SUPER_ADMIN cannot be combined with any other role
RULE-RC-02: ROLE_STUDENT and ROLE_RECRUITER cannot be held simultaneously in the same tenant
RULE-RC-03: ROLE_INSTITUTION_ADMIN requires verified institution domain
RULE-RC-04: ROLE_COMPANY_ADMIN requires verified company domain
RULE-RC-05: ROLE_DOJO_MENTOR requires active ROLE_MENTOR or ROLE_TEACHER_TRAINER
RULE-RC-06: ROLE_COACH and ROLE_COORDINATOR are society-scoped only
RULE-RC-07: ROLE_FRANCHISE_OWNER requires FRANCHISE_VERIFICATION_COMPLETE status
RULE-RC-08: ROLE_PRINCIPAL includes implicit ROLE_INSTITUTION_ADMIN permissions
RULE-RC-09: ROLE_ALUMNI is auto-assigned on graduation_confirmed event
RULE-RC-10: ROLE_PARENT is a linked role — must be bound to a ROLE_STUDENT account
```

### 3.3 Role Assignment Source Matrix

Every role assignment must trace to exactly one source:

| Role | Assignment Source |
|---|---|
| ROLE_STUDENT | Self-registration + institution_email_verified |
| ROLE_PARENT | Student-linked invitation |
| ROLE_TEACHER_TRAINER | Self-registration + credential_verification |
| ROLE_RECRUITER | Company admin invitation OR self + company_domain_verified |
| ROLE_COMPANY_ADMIN | Self-registration + company_domain_verified |
| ROLE_COMPANY_EMPLOYEE | Company admin invitation |
| ROLE_INSTITUTION_ADMIN | Platform admin grant + domain_verified |
| ROLE_HOD | Institution admin grant |
| ROLE_PRINCIPAL | Institution admin grant |
| ROLE_TPO | Institution admin grant |
| ROLE_ALUMNI | Auto-assigned via graduation_confirmed event |
| ROLE_MENTOR | Application + mentor_verification_complete |
| ROLE_DOJO_STUDENT | Enrollment in Dojo track |
| ROLE_DOJO_MENTOR | Mentor + Dojo assignment |
| ROLE_FREELANCER | Self-registration + profile_completeness ≥ 70% |
| ROLE_PROJECT_POSTER | Self-registration + account_age ≥ 7 days |
| ROLE_COURSE_CREATOR | Application + trainer_verification_complete |
| ROLE_SOCIETY_ADMIN | Platform admin grant |
| ROLE_COACH | Society admin grant |
| ROLE_COORDINATOR | Society admin grant |
| ROLE_FRANCHISE_OWNER | Franchise agreement signed + verified |
| ROLE_MASTER_ORGANIZER | Platform admin grant |
| ROLE_PLATFORM_MODERATOR | Platform admin grant |
| ROLE_SUPER_ADMIN | Infrastructure provisioning only — no self-assignment |

---

## SECTION IV — ONBOARDING SIGNAL CLASSIFICATION ENGINE

### 4.1 Signal Taxonomy

On entry, the agent receives one of the following signal types:

```
SIGNAL_TYPE_SELF_REGISTER       → User initiates registration independently
SIGNAL_TYPE_INVITATION_TOKEN    → User arrives via verified invitation link
SIGNAL_TYPE_OAUTH_ENTRY         → User arrives via OAuth (Google, LinkedIn, GitHub)
SIGNAL_TYPE_INSTITUTIONAL_SSO   → User arrives via institution-provided SSO
SIGNAL_TYPE_ADMIN_GRANT         → Platform admin directly creates account
SIGNAL_TYPE_AUTO_TRANSITION     → System-triggered (e.g., graduation, age threshold)
```

### 4.2 Signal Parsing Rules

```
IF SIGNAL_TYPE_INVITATION_TOKEN:
  → validate_token()
  → extract_pre_assigned_role()
  → skip role_selection_screen
  → proceed to domain_verification if required

IF SIGNAL_TYPE_INSTITUTIONAL_SSO:
  → extract_institution_domain()
  → resolve_institution_from_domain()
  → auto-assign ROLE_STUDENT or ROLE_TEACHER_TRAINER based on SSO attribute
  → skip domain_verification step

IF SIGNAL_TYPE_OAUTH_ENTRY:
  → extract_email_domain()
  → check_domain_registry()
  → if domain is a known institution → suggest ROLE_STUDENT
  → if domain is a known company → suggest ROLE_RECRUITER or ROLE_COMPANY_EMPLOYEE
  → if domain is generic (gmail, hotmail) → trigger role_selection_screen

IF SIGNAL_TYPE_SELF_REGISTER:
  → trigger role_selection_screen
  → validate selected role eligibility
  → trigger appropriate verification chain

IF SIGNAL_TYPE_ADMIN_GRANT:
  → no user action required
  → system enforces role directly
  → emit role.assigned event
  → send onboarding_welcome notification

IF SIGNAL_TYPE_AUTO_TRANSITION:
  → no user input required
  → emit career_stage_changed event (R71)
  → update RBAC binding
  → send transition_notification
```

---

## SECTION V — ROLE ASSIGNMENT ALGORITHM (LOCKED)

This algorithm is **deterministic and immutable**. It does not use ML. It does not infer intent.

```
FUNCTION assign_role(user_signal):

  STEP 1: PARSE_SIGNAL
    → classify signal_type
    → if classification fails → EMIT signal_unclassified → HALT

  STEP 2: IDENTITY_RESOLUTION
    → extract email_domain
    → check DomainRegistry
      → known_institution_domain → candidate_role = ROLE_STUDENT
      → known_company_domain     → candidate_role = ROLE_RECRUITER
      → unknown_domain           → candidate_role = NULL (requires user declaration)

  STEP 3: ROLE_ELIGIBILITY_CHECK
    → if candidate_role != NULL:
      → validate against Role Assignment Source Matrix (Section 3.3)
      → if eligible → proceed to STEP 4
      → if not eligible → emit role_ineligible → return error + reason
    → if candidate_role == NULL:
      → render role_selection_screen
      → wait for user_role_declaration
      → validate declared_role against eligibility rules

  STEP 4: VERIFICATION_CHAIN_DISPATCH
    → dispatch VerificationChain[role] (see Section VI)
    → set account_status = PENDING_VERIFICATION
    → emit verification_chain_initiated event

  STEP 5: TENANT_BINDING
    → resolve tenant_context from role + domain
    → if institution role → bind to institution tenant
    → if company role → bind to company tenant
    → if individual → bind to default individual tenant
    → write TenantBinding record

  STEP 6: PERMISSION_SURFACE_INITIALIZATION
    → load PermissionSet[role] from RBAC Engine
    → write initial RolePermission record
    → initialize feature_flags from Unleash for tenant

  STEP 7: CAREER_STAGE_INITIALIZATION (R71)
    → initialize CareerStageStateMachine
    → set initial stage based on role:
      → ROLE_STUDENT     → stage = STUDENT
      → ROLE_ALUMNI      → stage = EARLY_PROFESSIONAL
      → ROLE_MENTOR      → stage = PROFESSIONAL
      → ROLE_TEACHER_TRAINER → stage = PROFESSIONAL

  STEP 8: PROFILE_SCAFFOLD_GENERATION
    → generate empty profile schema for assigned role
    → set profile_completeness = 0
    → calculate minimum_completeness_threshold[role]

  STEP 9: EVENT_EMISSION
    → emit user.created
    → emit role.assigned { user_id, role, tenant_id, timestamp }
    → emit onboarding.initiated { user_id, onboarding_track }

  STEP 10: ONBOARDING_TRACK_DISPATCH
    → dispatch OnboardingTrack[role] (see Section VIII)
    → return identity_ready = TRUE

  ON ANY STEP FAILURE:
    → STOP → EMIT step_failure_event → LOG → NO PARTIAL STATE WRITTEN
```

---

## SECTION VI — VERIFICATION CHAIN REGISTRY

Each role has a mandatory verification chain. No role may be active until its chain completes.

### 6.1 Verification Chain Definitions

```
CHAIN[ROLE_STUDENT]:
  1. email_otp_verified
  2. institution_email_domain_matched OR institution_id_uploaded
  3. profile_completeness ≥ 60%
  → RESULT: student_verified

CHAIN[ROLE_PARENT]:
  1. email_otp_verified
  2. student_link_invitation_accepted
  3. student_account_confirmed_link
  → RESULT: parent_verified

CHAIN[ROLE_TEACHER_TRAINER]:
  1. email_otp_verified
  2. phone_otp_verified
  3. qualification_document_uploaded
  4. platform_review_approval (async, ≤72h)
  → RESULT: trainer_verified

CHAIN[ROLE_RECRUITER]:
  1. email_otp_verified
  2. company_domain_email_verified OR company_admin_invitation_accepted
  3. company_verification_status = VERIFIED
  → RESULT: recruiter_verified

CHAIN[ROLE_COMPANY_ADMIN]:
  1. email_otp_verified
  2. company_registration_document_uploaded
  3. company_domain_verified (DNS TXT record OR document)
  4. platform_review_approval (async, ≤48h)
  → RESULT: company_admin_verified

CHAIN[ROLE_INSTITUTION_ADMIN]:
  1. email_otp_verified
  2. institution_domain_verified
  3. authorization_letter_uploaded
  4. platform_review_approval (async, ≤48h)
  → RESULT: institution_admin_verified

CHAIN[ROLE_HOD / ROLE_PRINCIPAL / ROLE_TPO]:
  1. email_otp_verified
  2. institution_admin_invitation_accepted
  3. official_institution_email_required
  → RESULT: staff_verified

CHAIN[ROLE_MENTOR]:
  1. email_otp_verified
  2. phone_otp_verified
  3. professional_profile_linked (LinkedIn or portfolio)
  4. skill_declaration_submitted
  5. mentor_interview_slot_booked
  6. mentor_interview_completed
  7. mentor_panel_approval
  → RESULT: mentor_verified

CHAIN[ROLE_COURSE_CREATOR]:
  1. All CHAIN[ROLE_TEACHER_TRAINER] steps
  2. demo_course_module_submitted
  3. content_quality_review_passed
  → RESULT: course_creator_verified

CHAIN[ROLE_FREELANCER]:
  1. email_otp_verified
  2. profile_completeness ≥ 70%
  3. skill_assessment_passed (at least 1 skill)
  → RESULT: freelancer_verified

CHAIN[ROLE_FRANCHISE_OWNER]:
  1. email_otp_verified
  2. phone_otp_verified
  3. franchise_application_submitted
  4. franchise_agreement_signed (Digital Signature Service)
  5. franchise_fee_payment_confirmed
  6. platform_ops_approval
  → RESULT: franchise_owner_verified

CHAIN[ROLE_DOJO_STUDENT]:
  1. base_account_verified (any prior chain complete)
  2. dojo_enrollment_payment_confirmed OR scholarship_granted
  3. skill_track_selected
  → RESULT: dojo_student_active

CHAIN[ROLE_ALUMNI]:
  1. AUTOMATIC — triggered by graduation_confirmed event
  2. no manual steps
  → RESULT: alumni_active
```

### 6.2 Verification State Machine

```
PENDING_VERIFICATION
  → step_complete → VERIFICATION_IN_PROGRESS
  → all_steps_complete → VERIFICATION_COMPLETE → ACTIVE
  → step_failed → VERIFICATION_FAILED → PENDING_RETRY
  → timeout_exceeded → VERIFICATION_EXPIRED → REQUIRES_RESTART
  → fraud_detected → ACCOUNT_SUSPENDED → MANUAL_REVIEW
```

---

## SECTION VII — PERMISSION SURFACE MAP (BY ROLE)

The following is the initialization permission surface. These are the permissions granted at account activation. All permissions use the format: `resource:action`.

### 7.1 ROLE_STUDENT — Permission Surface

```
GRANTED_ON_ACTIVATION:
  profile:read_own
  profile:write_own
  jobs:browse
  jobs:apply
  skills:browse
  courses:browse
  courses:enroll
  portfolio:read_own
  portfolio:write_own
  notifications:read_own
  messages:send
  messages:receive
  groups:join_public
  social_feed:read
  social_feed:post
  gd_sessions:join
  mock_interview:request
  resume:upload
  endorsements:receive
  complaints:submit_anonymous

GRANTED_AFTER_PROFILE ≥ 80%:
  jobs:apply_premium
  skill_passport:generate
  leaderboard:appear

DENIED:
  jobs:post
  users:manage
  billing:manage
  admin_console:access
  institution:manage
  company:manage
```

### 7.2 ROLE_RECRUITER — Permission Surface

```
GRANTED_ON_ACTIVATION:
  jobs:post
  jobs:manage_own
  applications:view
  applications:manage_own
  candidates:search
  candidates:view_profile
  gd_sessions:create
  gd_sessions:manage_own
  interview:schedule
  interview:manage
  company_profile:read_own
  notifications:read_own
  messages:send
  messages:receive
  analytics:view_own_jobs

DENIED:
  candidates:contact_direct_without_apply
  billing:manage
  admin_console:access
  users:manage
```

### 7.3 ROLE_INSTITUTION_ADMIN — Permission Surface

```
GRANTED_ON_ACTIVATION:
  institution:manage_own
  departments:create
  departments:manage
  staff:invite
  staff:manage_own
  students:view_own_institution
  placements:manage
  results:publish
  groups:create_institution
  announcements:publish
  complaints:view_own_institution
  analytics:view_own_institution
  tpo:assign

DENIED:
  billing:manage (escalated to SUPER_ADMIN)
  cross_institution:data_access
```

### 7.4 ROLE_MENTOR / ROLE_DOJO_MENTOR — Permission Surface

```
GRANTED_ON_ACTIVATION:
  mentor_profile:manage_own
  sessions:create
  sessions:manage_own
  students:coach (assigned only)
  scoring:submit
  belt:recommend
  certification:recommend
  analytics:view_own_students
  dojo:access_match_room
  dojo:submit_score
  dojo:issue_command

DENIED:
  belt:auto_promote
  scoring:override_without_audit
  admin_console:access
```

### 7.5 ROLE_SUPER_ADMIN — Permission Surface

```
GRANTED:
  *:* (all resources, all actions)

RESTRICTIONS:
  Cannot be combined with any other role (RULE-RC-01)
  Every action emits immutable audit_log event
  MFA mandatory on every session
  IP allowlist enforced
  Session timeout = 30 minutes inactivity
```

---

## SECTION VIII — ONBOARDING TRACK REGISTRY

Each role has a locked onboarding track. The agent dispatches the correct track on identity_ready.

### 8.1 Track Definitions

```
TRACK[ROLE_STUDENT]:
  Step 1: Welcome screen — platform orientation (skip-disabled)
  Step 2: Academic profile form (CGPA, branch, year)
  Step 3: Skills declaration (minimum 3 skills)
  Step 4: Resume upload (optional at step, mandatory before first apply)
  Step 5: Career goal declaration (preferred roles, locations)
  Step 6: First group join suggestion (institution group auto-suggested)
  Step 7: Placement probability preview (R2 computed)
  Completion Gate: profile_completeness ≥ 60%
  Emit: onboarding.complete { role: ROLE_STUDENT }

TRACK[ROLE_RECRUITER]:
  Step 1: Company profile completion
  Step 2: Job posting tutorial (one mandatory sample post)
  Step 3: Candidate search walkthrough
  Step 4: GD session creation walkthrough
  Step 5: Billing plan selection
  Completion Gate: company_profile_complete + billing_plan_selected
  Emit: onboarding.complete { role: ROLE_RECRUITER }

TRACK[ROLE_TEACHER_TRAINER / ROLE_COURSE_CREATOR]:
  Step 1: Trainer profile setup (expertise, experience)
  Step 2: Subject/skill declaration
  Step 3: Course builder tutorial (R82)
  Step 4: First module draft (mandatory)
  Step 5: Revenue setup (payout details, R84)
  Step 6: Public trainer page preview (R88)
  Completion Gate: first_module_draft_saved + payout_details_added
  Emit: onboarding.complete { role: ROLE_TEACHER_TRAINER }

TRACK[ROLE_INSTITUTION_ADMIN]:
  Step 1: Institution profile completion
  Step 2: Department structure setup
  Step 3: First staff invitation (HOD or TPO)
  Step 4: Student enrollment method selection (bulk upload / invitation)
  Step 5: Placement process configuration
  Completion Gate: institution_profile_complete + min_1_department + min_1_staff_invited
  Emit: onboarding.complete { role: ROLE_INSTITUTION_ADMIN }

TRACK[ROLE_MENTOR]:
  Step 1: Mentor profile setup (specialization, bio)
  Step 2: Availability calendar setup
  Step 3: First session type configuration
  Step 4: Student intake preferences
  Step 5: Dojo track binding (if ROLE_DOJO_MENTOR)
  Completion Gate: profile_complete + availability_set
  Emit: onboarding.complete { role: ROLE_MENTOR }

TRACK[ROLE_FRANCHISE_OWNER]:
  Step 1: Society profile setup
  Step 2: Operational area declaration
  Step 3: Workshop setup tutorial
  Step 4: Coach & coordinator invitation
  Step 5: First batch creation walkthrough
  Completion Gate: society_profile_complete + min_1_coach_invited
  Emit: onboarding.complete { role: ROLE_FRANCHISE_OWNER }

TRACK[ROLE_COMPANY_ADMIN]:
  Step 1: Company profile setup
  Step 2: Employee invitation walkthrough
  Step 3: Internal gig board activation
  Step 4: Hiring pipeline configuration
  Step 5: Billing & subscription setup
  Completion Gate: company_profile_complete + billing_active
  Emit: onboarding.complete { role: ROLE_COMPANY_ADMIN }

TRACK[ROLE_PARENT]:
  Step 1: Parent profile setup
  Step 2: Linked student confirmation
  Step 3: Parent dashboard orientation (R3 placement guide)
  Step 4: Notification preference setup
  Completion Gate: student_link_confirmed
  Emit: onboarding.complete { role: ROLE_PARENT }
```

---

## SECTION IX — DATABASE SCHEMA (ROLE ASSIGNMENT DOMAIN)

These schemas are **mandatory**. Absence of any table → STOP EXECUTION.

```sql
-- Core Identity Tables

CREATE TABLE users (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email           TEXT UNIQUE NOT NULL,
  phone           TEXT,
  password_hash   TEXT NOT NULL,
  full_name       TEXT NOT NULL,
  avatar_url      TEXT,
  account_status  TEXT NOT NULL DEFAULT 'PENDING_VERIFICATION',
  created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE roles (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name            TEXT UNIQUE NOT NULL,       -- maps to ROLE_ constants
  display_name    TEXT NOT NULL,
  description     TEXT,
  is_system_role  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE user_role_assignments (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         UUID NOT NULL REFERENCES users(id),
  role_id         UUID NOT NULL REFERENCES roles(id),
  tenant_id       UUID NOT NULL REFERENCES tenants(id),
  assigned_by     UUID REFERENCES users(id),  -- NULL = system-assigned
  assignment_source TEXT NOT NULL,            -- maps to SIGNAL_TYPE_*
  assigned_at     TIMESTAMP NOT NULL DEFAULT NOW(),
  expires_at      TIMESTAMP,                  -- NULL = permanent
  is_active       BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE (user_id, role_id, tenant_id)
);

CREATE TABLE verification_chains (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         UUID NOT NULL REFERENCES users(id),
  role_name       TEXT NOT NULL,
  current_step    INT NOT NULL DEFAULT 1,
  total_steps     INT NOT NULL,
  status          TEXT NOT NULL DEFAULT 'PENDING_VERIFICATION',
  initiated_at    TIMESTAMP NOT NULL DEFAULT NOW(),
  completed_at    TIMESTAMP,
  expired_at      TIMESTAMP,
  failure_reason  TEXT
);

CREATE TABLE verification_steps (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  chain_id        UUID NOT NULL REFERENCES verification_chains(id),
  step_number     INT NOT NULL,
  step_type       TEXT NOT NULL,              -- e.g. 'email_otp', 'domain_verify'
  status          TEXT NOT NULL DEFAULT 'PENDING',
  completed_at    TIMESTAMP,
  metadata        JSONB
);

CREATE TABLE onboarding_progress (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         UUID NOT NULL REFERENCES users(id),
  role_name       TEXT NOT NULL,
  track_name      TEXT NOT NULL,
  current_step    INT NOT NULL DEFAULT 1,
  total_steps     INT NOT NULL,
  completed_steps INT[] NOT NULL DEFAULT '{}',
  is_complete     BOOLEAN NOT NULL DEFAULT FALSE,
  started_at      TIMESTAMP NOT NULL DEFAULT NOW(),
  completed_at    TIMESTAMP
);

CREATE TABLE role_assignment_audit_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         UUID NOT NULL,
  role_name       TEXT NOT NULL,
  action          TEXT NOT NULL,   -- ASSIGNED, REVOKED, SUSPENDED, UPGRADED
  performed_by    UUID,
  reason          TEXT,
  metadata        JSONB,
  created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## SECTION X — API CONTRACT (ROLE ASSIGNMENT SERVICE)

All endpoints must conform to R3 (OpenAPI 3.1). These endpoints are **mandatory**.

```yaml
# Role Assignment Service API Contract

/onboarding/initiate:
  POST:
    summary: Parse entry signal and initiate role assignment
    body: { signal_type, email, invitation_token?, oauth_token? }
    responses:
      200: { role_candidate, verification_chain_id, next_step }
      409: { error: AMBIGUOUS_SIGNAL, prompt: role_selection_required }
      422: { error: INELIGIBLE_ROLE, reason }

/onboarding/role-select:
  POST:
    summary: User declares role during self-registration
    auth: session_token
    body: { selected_role }
    responses:
      200: { verification_chain_id, verification_steps[] }
      400: { error: INVALID_ROLE_SELECTION }

/onboarding/verify/step:
  POST:
    summary: Submit verification step completion
    auth: session_token
    body: { chain_id, step_type, payload }
    responses:
      200: { step_status, next_step, chain_status }
      400: { error: VERIFICATION_STEP_FAILED, reason }

/onboarding/track/progress:
  GET:
    summary: Fetch current onboarding track progress
    auth: jwt_access_token
    responses:
      200: { track_name, current_step, total_steps, completed_steps[] }

/onboarding/track/step/complete:
  POST:
    summary: Mark an onboarding track step complete
    auth: jwt_access_token
    body: { step_number, step_data }
    responses:
      200: { step_status, next_step, is_onboarding_complete }

/roles/assign:
  POST:
    summary: Admin-initiated role assignment
    auth: jwt_access_token [ROLE_SUPER_ADMIN | ROLE_INSTITUTION_ADMIN]
    body: { user_id, role_name, tenant_id, reason }
    responses:
      200: { assignment_id, role_name, status }
      403: { error: INSUFFICIENT_PERMISSION }

/roles/revoke:
  POST:
    summary: Revoke role assignment
    auth: jwt_access_token [ROLE_SUPER_ADMIN]
    body: { user_id, role_name, reason }
    responses:
      200: { revocation_id, status }

/roles/user/{user_id}:
  GET:
    summary: Get all active roles for a user
    auth: jwt_access_token
    responses:
      200: { roles[], tenant_bindings[] }
```

---

## SECTION XI — EVENT SCHEMA (ROLE ASSIGNMENT DOMAIN)

All events must conform to R4 (AsyncAPI 2.6.0). These events are **mandatory**.

```yaml
channels:

  onboarding.initiated:
    payload:
      user_id:       string
      role_candidate: string
      signal_type:   string
      tenant_id:     string
      timestamp:     string (ISO8601)

  verification.chain.initiated:
    payload:
      user_id:    string
      chain_id:   string
      role_name:  string
      total_steps: integer

  verification.step.completed:
    payload:
      user_id:    string
      chain_id:   string
      step_type:  string
      step_number: integer

  verification.complete:
    payload:
      user_id:    string
      role_name:  string
      chain_id:   string
      timestamp:  string

  role.assigned:
    payload:
      user_id:        string
      role_name:      string
      tenant_id:      string
      assignment_source: string
      assigned_by:    string (nullable)
      timestamp:      string

  role.revoked:
    payload:
      user_id:    string
      role_name:  string
      reason:     string
      performed_by: string

  onboarding.step.completed:
    payload:
      user_id:     string
      role_name:   string
      step_number: integer

  onboarding.complete:
    payload:
      user_id:    string
      role_name:  string
      track_name: string
      timestamp:  string

  user.created:
    payload:
      user_id:    string
      email:      string
      role_name:  string
      tenant_id:  string
```

---

## SECTION XII — UI SCREENS (ROLE ASSIGNMENT & ONBOARDING)

These screens are **mandatory**. All must comply with R29 (Flutter UI), R31 (dual frontend), R33 (design system).

### 12.1 Screen Registry

```
SCREEN_ENTRY_GATE
  Purpose: First screen shown to any unauthenticated visitor
  Flutter Route: /entry
  SEO Route: (none — not indexed)
  Trigger: App launch, cold open
  Actions: [Register, Login, Explore as Guest]
  Emit: entry_signal_detected

SCREEN_ROLE_SELECTION
  Purpose: When signal_type = SELF_REGISTER and domain is generic
  Flutter Route: /register/role
  SEO Route: (none)
  Content: Visual role cards with icons and 1-line descriptions
  Roles Shown: STUDENT, RECRUITER, TRAINER, MENTOR, INSTITUTION, COMPANY, FREELANCER
  Validation: Must select exactly one role to proceed
  Note: Does NOT show SUPER_ADMIN, PLATFORM_MODERATOR, FRANCHISE_OWNER (admin-granted only)

SCREEN_REGISTRATION_FORM
  Purpose: Collect identity data for selected role
  Flutter Route: /register/form
  Adapts to: Selected role (different required fields per role)
  Fields (STUDENT): name, email, phone, institution_name, branch, year
  Fields (RECRUITER): name, work_email, company_name, designation
  Fields (TRAINER): name, email, phone, expertise_area, experience_years
  Validation: Real-time inline validation
  Emit: registration_form_submitted

SCREEN_VERIFICATION_PENDING
  Purpose: Show verification chain progress
  Flutter Route: /verify/pending
  Content: Step-by-step checklist, estimated completion time, current status badge
  Polling: WebSocket subscription to verification.step.completed events
  States: PENDING / IN_PROGRESS / COMPLETE / FAILED / EXPIRED

SCREEN_DOMAIN_VERIFICATION
  Purpose: Verify institution or company email domain
  Flutter Route: /verify/domain
  Content: OTP input field, resend timer, help text
  Logic: Validates OTP from email sent to official domain address

SCREEN_DOCUMENT_UPLOAD
  Purpose: Upload verification documents
  Flutter Route: /verify/documents
  Accepts: PDF only, max 2MB per file
  Uploads to: MinIO via pre-signed URL
  Scan: Virus scan before acceptance

SCREEN_ONBOARDING_TRACK
  Purpose: Guided role-specific setup steps
  Flutter Route: /onboarding
  Navigation: Linear progress — cannot skip mandatory steps
  Progress: Visual step bar with completion percentage
  Each Step: Dedicated sub-screen with inline help

SCREEN_ONBOARDING_COMPLETE
  Purpose: Celebrate completion and orient user to dashboard
  Flutter Route: /onboarding/complete
  Content: Completion animation, role badge, 3 recommended first actions
  CTA: [Go to Dashboard]
  Emit: onboarding.complete
```

---

## SECTION XIII — FAILURE HANDLING (ROLE ASSIGNMENT AGENT)

Every failure mode is pre-defined. No improvised error handling permitted.

| Failure | System Action |
|---|---|
| Signal unclassifiable | EMIT signal_error → re-prompt entry |
| Role ineligible | Return HTTP 422 + specific reason + improvement path |
| Verification OTP expired | Allow 3 resends → on 4th failure: 24h cooldown |
| Domain not in registry | Prompt manual institution/company registration |
| Document rejected (virus) | Delete file → notify user → allow re-upload |
| Admin approval timeout (>72h) | Auto-escalate to SUPER_ADMIN queue + notify user |
| Duplicate email registration | Return HTTP 409 + suggest login + password reset link |
| Invitation token expired | Return HTTP 410 + suggest requesting new invitation |
| Fraud signal detected | SUSPEND account → EMIT fraud.detected → manual review queue |
| Role combination violation | Return HTTP 400 + explain which rule is violated (RULE-RC-XX) |
| Partial state on crash | ROLLBACK all writes → restart from last clean checkpoint |

---

## SECTION XIV — SECURITY RULES (ROLE ASSIGNMENT DOMAIN)

All rules are **non-optional**.

```
SEC-RA-01: No role may be assigned without a completed verification chain
SEC-RA-02: Invitation tokens must be single-use, time-limited (48h), signed (HMAC-SHA256)
SEC-RA-03: All role assignment events write to immutable audit log
SEC-RA-04: ROLE_SUPER_ADMIN can only be created via infrastructure provisioning script
SEC-RA-05: Role escalation (e.g., STUDENT → MODERATOR) requires SUPER_ADMIN approval
SEC-RA-06: Role revocation must include reason code — empty reason → reject
SEC-RA-07: Domain verification tokens expire in 15 minutes
SEC-RA-08: Maximum 3 failed OTP attempts before 15-minute lockout
SEC-RA-09: Document uploads scanned before storage — infected files auto-deleted + flagged
SEC-RA-10: Onboarding sessions use short-lived JWT (15-min expiry) until role_assigned
SEC-RA-11: All admin grants logged with admin_id, timestamp, reason
SEC-RA-12: Cross-tenant role assignment forbidden without explicit SUPER_ADMIN override
```

---

## SECTION XV — INTEGRATION MAP

The ROLE_ASSIGNMENT_AGENT integrates with the following services. All integrations are **mandatory**.

```
→ Auth Service           : Issues JWT after role_assigned event
→ User Service           : Writes user record, profile scaffold
→ Notification Service   : Sends welcome, verification, approval notifications
→ RBAC Engine (Keycloak) : Syncs role assignment to permission enforcement
→ Tenant Service         : Binds user to tenant on role assignment
→ Billing Service        : Initializes free tier on onboarding.complete
→ Analytics Service      : Receives onboarding funnel events
→ Fraud Detection Engine : Signals checked before verification proceeds
→ Career Stage Engine (R71): Initializes career_stage on role assignment
→ Skill Passport (R72)   : Creates empty passport on student_verified
→ Admin Ops Console (R40): Surfaces pending verifications in moderation queue
→ Unleash (Feature Flags): Loads role-specific feature flags on permission init
→ Event Bus (Kafka)      : All agent events published here
```

---

## SECTION XVI — ANTIGRAVITY BEHAVIORAL CONTRACT

### 16.1 What ANTIGRAVITY Means in This Context

ANTIGRAVITY is the ECOSKILLER principle that the platform must pull users **upward** — not trap them in complexity, not push them away with friction, not lose them in ambiguity.

The ROLE_ASSIGNMENT_AGENT is the **gravitational entry point**. If it fails, every downstream system fails. If it creates confusion, users abandon the platform before experiencing its value.

### 16.2 ANTIGRAVITY Rules (Binding)

```
AG-01: The onboarding path for ANY role must be completable in under 7 minutes
       for a first-time user on a mid-range mobile device on a 4G connection.

AG-02: At NO point in the onboarding flow may a user encounter a dead-end screen
       without a clear escape route, help text, or contact action.

AG-03: Role selection screen must use visual icons + plain language descriptions.
       No jargon. No acronyms without expansion. No assumptions about literacy level.

AG-04: Every verification step must show:
       (a) What the user must do
       (b) Why it is required
       (c) How long it will take
       (d) What happens next

AG-05: Partial onboarding state must be persistently saved.
       A user who drops off at Step 3 and returns 48 hours later resumes from Step 3.
       No data loss. No restart.

AG-06: Every rejection must include a specific, actionable reason.
       "Verification failed" is forbidden.
       "Your document was rejected because the CGPA field was not visible.
        Please re-upload page 2 of your marksheet." is the minimum standard.

AG-07: The agent must emit progress events to the notification service
       at every step completion — SMS + push + email (user preference governs channel).

AG-08: Onboarding completion must produce an immediate positive reward signal:
       - Role badge displayed
       - Completion animation triggered
       - First recommended action shown
       This is not optional UX polish. This is retention mechanics.

AG-09: The time between signal_entry and first_permission_surface_loaded
       must be ≤ 3 seconds for SIGNAL_TYPE_INVITATION_TOKEN flows.

AG-10: No user may be left in PENDING_VERIFICATION for more than 72 hours
       without a human touchpoint from the platform (automated or human agent).
```

### 16.3 ANTIGRAVITY Enforcement

Violation of any AG rule → STOP EXECUTION → REPORT ANTIGRAVITY_RULE_VIOLATION → AGENT FAILS PRODUCTION READINESS CHECK

---

## SECTION XVII — PRODUCTION READINESS CHECKLIST

The ROLE_ASSIGNMENT_AGENT may not be declared production-ready until all of the following pass:

```
[ ] All Role Registry entries seeded in database
[ ] All Verification Chains implemented and tested
[ ] All API endpoints respond with correct HTTP codes
[ ] All Event Schema channels publishing correctly to Kafka
[ ] All Database tables created via migration scripts
[ ] All Onboarding Tracks navigable end-to-end in Flutter
[ ] All RBAC bindings verified in Keycloak
[ ] All Tenant bindings verified
[ ] All Failure Modes tested (unit + integration)
[ ] All Security Rules enforced (penetration test checklist)
[ ] All ANTIGRAVITY Rules verified (UX audit)
[ ] Admin Ops Console shows pending verifications correctly
[ ] Audit log writes confirmed for every role assignment
[ ] Notification Service sends all onboarding communications
[ ] Feature flags load correctly per role from Unleash
[ ] Load test: 1,000 concurrent onboarding sessions ≤ 3s P95 latency
[ ] Fraud detection signals integrated and tested
[ ] Intern can complete full onboarding flow using R46 steps
[ ] Contract Validator (R49) passes for all agent contracts
[ ] QA Generator (R50) tests pass at 100% coverage
```

Absence of any check → STOP EXECUTION → NO DEPLOYMENT CLAIM PERMITTED

---

## SECTION XVIII — FINAL SEAL

```
ROLE_ASSIGNMENT_AGENT
Version: v1.0
Bound System: ECOSKILLER v12.0
Onboarding Philosophy: ANTIGRAVITY

This agent is the identity foundation of ECOSKILLER.
It runs before every other agent.
It gates every other system.
Its correctness is not negotiable.

Status: LOCKED · SEALED · PRODUCTION-GOVERNED

Mutation Policy: Add-only via version bump
Interpretation Authority: NONE
Execution Authority: Human declaration only

"Every user who enters ECOSKILLER passes through this gate.
 The gate does not negotiate.
 The gate does not guess.
 The gate assigns — correctly, completely, and permanently."
```

---

*ROLE_ASSIGNMENT_AGENT.md — ECOSKILLER ANTIGRAVITY SYSTEM — v1.0 — FINAL*
