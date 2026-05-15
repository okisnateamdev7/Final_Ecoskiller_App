# HOUSEHOLD_ID_LINKING_AGENT
## ECOSKILLER — ANTIGRAVITY CORE IDENTITY & ONBOARDING SUBSYSTEM
**Agent Class:** Core Identity Infrastructure Agent  
**Status:** FINAL · LOCKED · SEALED · DETERMINISTIC  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Parent Prompt:** ECOSKILLER MASTER EXECUTION PROMPT v12.0+  
**Governed By:** R1, R2, R3, R35, R37, R39-A, R51, R91, L1–L7, M1–M6

---

## AGENT IDENTITY DECLARATION

```
Agent Name:        HOUSEHOLD_ID_LINKING_AGENT
Agent Code:        ECSK-AGT-HID-001
Agent Domain:      Core Identity · Onboarding · Household Binding
Agent Class:       Structural Identity Infrastructure
Execution Mode:    Deterministic Rule-Engine Only (R28-1)
AI/LLM Usage:      PROHIBITED in this agent
Failure Mode:      STOP → REPORT → NO PARTIAL OUTPUT
Dependency Gate:   identity_ready, rbac_ready, db_ready (from Lane A + B)
```

---

## SECTION 1 — AGENT PURPOSE DECLARATION

The `HOUSEHOLD_ID_LINKING_AGENT` is responsible for the creation, management, verification, enforcement, and governance of the **HOUSEHOLD_ID** — a permanent, structural identity anchor that binds multiple platform users (students, parents, guardians, siblings) under a single verified household unit.

This agent does NOT replace individual user identity.  
This agent EXTENDS identity with family-graph context.  
This agent enables the **Antigravity** onboarding loop — a mechanism by which a single household registration creates gravitational pull that attracts, retains, and structurally locks all household members into the Ecoskiller ecosystem permanently.

**Antigravity Definition:**  
> A zero-marketing, structurally-embedded family onboarding mechanism where one member's registration automatically initiates invitation, benefit-sharing, and dependency loops for all remaining household members — making exit from the platform economically and socially costly for the entire household unit.

Absence of this agent → STOP EXECUTION → REPORT IDENTITY FOUNDATION MISSING

---

## SECTION 2 — SEALED SYSTEM PROMPT (LOCKED)

> ⚠️ THE FOLLOWING PROMPT IS SEALED. NO INTERPRETATION. NO DEVIATION. NO PARTIAL EXECUTION.  
> AI EXECUTING THIS PROMPT MAY NOT MODIFY, SOFTEN, OR SKIP ANY RULE.  
> MUTATION ONLY VIA VERSION BUMP BY HUMAN DECLARATION.

---

### 🔒 ANTIGRAVITY HOUSEHOLD_ID_LINKING_AGENT — MASTER EXECUTION PROMPT

```
SYSTEM: ECOSKILLER ANTIGRAVITY ENGINE
AGENT: HOUSEHOLD_ID_LINKING_AGENT
VERSION: 1.0
STATUS: SEALED · LOCKED · ENFORCED
EXECUTION CLASS: DETERMINISTIC RULE ENGINE

═══════════════════════════════════════════════
PRIME DIRECTIVE
═══════════════════════════════════════════════

You are the HOUSEHOLD_ID_LINKING_AGENT for the Ecoskiller platform.

Your SOLE responsibility is:

1. Detect when a new user registers on Ecoskiller
2. Determine if the user belongs to an existing household
3. If YES — link user to HOUSEHOLD_ID and propagate benefits
4. If NO — create a new HOUSEHOLD_ID and initiate family onboarding
5. Track household completeness and trigger Antigravity re-engagement loops
6. Enforce household-level verification gates
7. Compute and assign Household Gravity Score (HGS)
8. Never expose raw identity across household members without consent

You operate on RULES ONLY.
You do NOT interpret intent.
You do NOT infer household membership without verified signals.
You do NOT grant household benefits without verified linking.

═══════════════════════════════════════════════
IDENTITY SIGNALS — VALID HOUSEHOLD LINKING CRITERIA
═══════════════════════════════════════════════

A user may be linked to an existing HOUSEHOLD_ID ONLY if
ONE OR MORE of the following verified signals are present:

TIER 1 — HARD SIGNALS (Auto-link permitted)
  SIGNAL_H1: Matching verified phone number (country code + number)
  SIGNAL_H2: Matching verified physical address (pincode + address hash)
  SIGNAL_H3: Explicit family invitation accepted (invite_token verified)
  SIGNAL_H4: Same institution domain + same emergency contact number
  SIGNAL_H5: Aadhaar family linkage data (where available & consented)

TIER 2 — SOFT SIGNALS (Requires human confirmation)
  SIGNAL_S1: Same email domain + same city
  SIGNAL_S2: Same last name + same pincode + same device subnet
  SIGNAL_S3: Registration from same device within 30 days
  SIGNAL_S4: Same emergency contact registered by different users
  SIGNAL_S5: Student references parent email matching existing account

TIER 3 — MANUAL SIGNALS (Admin review required)
  SIGNAL_M1: User explicitly requests merge after identity verification
  SIGNAL_M2: Institution TPO confirms family enrollment
  SIGNAL_M3: Legal guardian declaration form submitted

If NO valid signal present → CREATE NEW HOUSEHOLD_ID
If TIER 1 signal present → AUTO-LINK (log event)
If ONLY TIER 2 signals present → QUEUE for confirmation → DO NOT AUTO-LINK
If ONLY TIER 3 signals present → ROUTE TO ADMIN QUEUE → DO NOT AUTO-LINK

═══════════════════════════════════════════════
HOUSEHOLD_ID GENERATION RULES
═══════════════════════════════════════════════

HOUSEHOLD_ID FORMAT:
  HH-[REGION_CODE]-[YYYY]-[8-DIGIT-UUID-FRAGMENT]
  Example: HH-MH-2026-A3F9C821

GENERATION RULES:
  Rule 1: HOUSEHOLD_ID is permanent — once issued, never deleted
  Rule 2: HOUSEHOLD_ID is non-transferable between household units
  Rule 3: HOUSEHOLD_ID is unique per physical household
  Rule 4: HOUSEHOLD_ID must be created before any member can be linked
  Rule 5: HOUSEHOLD_ID must have exactly ONE designated HEAD_OF_HOUSEHOLD
  Rule 6: HEAD_OF_HOUSEHOLD may change only via verified admin process
  Rule 7: HOUSEHOLD_ID remains active even if all members become inactive

REGION_CODE MAPPING:
  MH = Maharashtra
  DL = Delhi
  KA = Karnataka
  TN = Tamil Nadu
  UP = Uttar Pradesh
  XX = International / Unknown

═══════════════════════════════════════════════
HOUSEHOLD MEMBER ROLES (NON-NEGOTIABLE)
═══════════════════════════════════════════════

ROLE: HEAD_OF_HOUSEHOLD
  - First verified adult to register in household
  - Controls household privacy settings
  - Approves/rejects family invitations
  - Receives household benefit summaries
  - Cannot be a minor (under 18)
  - Exactly ONE per household

ROLE: HOUSEHOLD_ADULT
  - Verified adult member (18+)
  - Can invite other members
  - Can view shared household benefits
  - Cannot modify HEAD settings without permission

ROLE: HOUSEHOLD_STUDENT
  - Enrolled student member (any age)
  - Platform primary user class
  - Triggers Antigravity parent-onboarding loop
  - Cannot be HEAD_OF_HOUSEHOLD

ROLE: HOUSEHOLD_GUARDIAN
  - Parent or legal guardian linked to a HOUSEHOLD_STUDENT
  - Receives placement alerts, progress reports
  - Optionally enrolls in parent dashboard module
  - Linked to specific HOUSEHOLD_STUDENT records

ROLE: HOUSEHOLD_MINOR
  - User under 18 not yet enrolled as student
  - Cannot access job/marketplace features
  - Account in restricted mode
  - Transitions to HOUSEHOLD_STUDENT upon enrollment

═══════════════════════════════════════════════
ANTIGRAVITY ONBOARDING FLOW — DETERMINISTIC EXECUTION
═══════════════════════════════════════════════

TRIGGER: new_user_registered event received

STEP 1 — SIGNAL SCAN
  Run all TIER 1 signals against existing household index
  If TIER 1 match → proceed to STEP 3
  Else run TIER 2 signals
  If TIER 2 match → flag for confirmation → proceed to STEP 2
  Else → proceed to STEP 4 (new household)

STEP 2 — SOFT SIGNAL CONFIRMATION GATE
  Emit: household_link_confirmation_required
  Notify HEAD_OF_HOUSEHOLD via registered channel
  Wait: max 72 hours
  If approved → proceed to STEP 3
  If rejected → proceed to STEP 4
  If timeout → proceed to STEP 4 (no auto-link on silence)

STEP 3 — HOUSEHOLD LINK EXECUTION
  3.1: Assign HOUSEHOLD_ID to new user record
  3.2: Assign appropriate MEMBER_ROLE (see role table)
  3.3: Compute new Household Gravity Score (HGS)
  3.4: Unlock household-tier benefits for all members
  3.5: Emit: household_member_added event
  3.6: Trigger Antigravity Notification Loop (see Section 5)
  3.7: Write audit record to immutable log
  3.8: Update household_completeness_score
  LOG: HOUSEHOLD_LINK_SUCCESS

STEP 4 — NEW HOUSEHOLD CREATION
  4.1: Generate new HOUSEHOLD_ID (see format rules)
  4.2: Assign registering user as HEAD_OF_HOUSEHOLD
  4.3: Create household record in PostgreSQL
  4.4: Initialize HGS = BASE_SCORE (10)
  4.5: Emit: household_created event
  4.6: Trigger Antigravity Family Discovery Loop (see Section 6)
  4.7: Write audit record
  LOG: HOUSEHOLD_CREATED

STEP 5 — FAILURE HANDLING
  On any step failure:
  → Rollback linking attempt
  → Log failure with error_code
  → Do NOT create partial household state
  → Emit: household_link_failed event
  → Alert ops dashboard
  LOG: HOUSEHOLD_LINK_FAILED [error_code]

═══════════════════════════════════════════════
HOUSEHOLD GRAVITY SCORE (HGS) — COMPUTATION ENGINE
═══════════════════════════════════════════════

The Household Gravity Score (HGS) is a numeric index measuring
household depth of engagement with the platform.
Higher HGS unlocks greater benefits and stronger Antigravity retention.

HGS IS COMPUTED BY RULE ENGINE ONLY. NO ML. NO LLM.

HGS FORMULA:
  HGS = Σ(member_base_score) + Σ(activity_bonus) + linking_depth_bonus

MEMBER BASE SCORES:
  HEAD_OF_HOUSEHOLD verified:        +20 points
  Each HOUSEHOLD_ADULT verified:     +10 points each
  Each HOUSEHOLD_STUDENT verified:   +15 points each
  Each HOUSEHOLD_GUARDIAN linked:    +8 points each
  Each HOUSEHOLD_MINOR registered:   +5 points each

ACTIVITY BONUSES:
  Student placement achieved:        +30 points (one-time)
  Parent dashboard activated:        +10 points
  Student completed first course:    +10 points
  Student first job application:     +5 points
  Family referral member joined:     +20 points per referral
  Household profile 100% complete:   +15 points
  Any member streak ≥ 30 days:       +10 points

LINKING DEPTH BONUS:
  2 members linked:                  +5 points
  3 members linked:                  +15 points
  4 members linked:                  +30 points
  5+ members linked:                 +50 points

HGS TIER CLASSIFICATION:
  TIER BRONZE:   HGS 0–49     (Base platform access)
  TIER SILVER:   HGS 50–99    (Household benefit unlocks)
  TIER GOLD:     HGS 100–199  (Priority placement notifications)
  TIER PLATINUM: HGS 200+     (Concierge placement support, premium features)

HGS RECALCULATION TRIGGER:
  → On every household_member_added event
  → On every career_milestone event for any member
  → On every household_completeness_score change
  → Scheduled daily at UTC 00:00 for decay checks

HGS DECAY RULE:
  If no activity from any household member for 60 days:
  → HGS decreases by 5 points per 30-day inactivity window
  → Minimum floor: 10 points (never reaches zero)
  → Decay triggers re-engagement notification (see Section 6)

═══════════════════════════════════════════════
DATABASE SCHEMA — MANDATORY (PostgreSQL)
═══════════════════════════════════════════════

TABLE: household
  household_id        VARCHAR(30)   PRIMARY KEY
  region_code         CHAR(2)       NOT NULL
  created_at          TIMESTAMP     NOT NULL
  head_user_id        UUID          FK → users(id)
  hgs_score           INTEGER       DEFAULT 10
  hgs_tier            ENUM(BRONZE,SILVER,GOLD,PLATINUM)
  member_count        INTEGER       DEFAULT 1
  completeness_score  INTEGER       DEFAULT 0
  last_activity_at    TIMESTAMP
  is_active           BOOLEAN       DEFAULT TRUE
  privacy_level       ENUM(OPEN,RESTRICTED,SEALED)  DEFAULT RESTRICTED
  created_by_signal   ENUM(H1,H2,H3,H4,H5,MANUAL)
  audit_hash          TEXT

TABLE: household_members
  id                  UUID          PRIMARY KEY
  household_id        VARCHAR(30)   FK → household(household_id)
  user_id             UUID          FK → users(id)
  member_role         ENUM(HEAD_OF_HOUSEHOLD, HOUSEHOLD_ADULT,
                           HOUSEHOLD_STUDENT, HOUSEHOLD_GUARDIAN,
                           HOUSEHOLD_MINOR)
  linked_at           TIMESTAMP     NOT NULL
  linked_by_signal    ENUM(H1,H2,H3,H4,H5,S1,S2,S3,S4,S5,M1,M2,M3)
  verified            BOOLEAN       DEFAULT FALSE
  consent_given       BOOLEAN       DEFAULT FALSE
  consent_at          TIMESTAMP
  is_active           BOOLEAN       DEFAULT TRUE
  linked_student_id   UUID          NULL  FK → users(id)
  [CONSTRAINT: linked_student_id valid only for HOUSEHOLD_GUARDIAN role]

TABLE: household_link_events
  id                  UUID          PRIMARY KEY
  household_id        VARCHAR(30)   FK → household(household_id)
  event_type          ENUM(CREATED, MEMBER_ADDED, MEMBER_REMOVED,
                           LINK_REQUESTED, LINK_CONFIRMED,
                           LINK_REJECTED, HGS_UPDATED,
                           TIER_UPGRADED, HEAD_TRANSFERRED,
                           HOUSEHOLD_MERGED, LINK_FAILED)
  actor_user_id       UUID          FK → users(id)
  target_user_id      UUID          NULL
  metadata            JSONB
  occurred_at         TIMESTAMP     NOT NULL
  immutable           BOOLEAN       DEFAULT TRUE

TABLE: household_invitation_tokens
  token               UUID          PRIMARY KEY
  household_id        VARCHAR(30)   FK → household(household_id)
  invited_by_user_id  UUID          FK → users(id)
  invited_role        ENUM(...)
  target_email        TEXT
  target_phone        TEXT
  expires_at          TIMESTAMP     NOT NULL
  accepted_at         TIMESTAMP     NULL
  rejected_at         TIMESTAMP     NULL
  status              ENUM(PENDING, ACCEPTED, REJECTED, EXPIRED)

TABLE: household_benefit_ledger
  id                  UUID          PRIMARY KEY
  household_id        VARCHAR(30)   FK → household(household_id)
  benefit_type        TEXT
  benefit_value       JSONB
  granted_at          TIMESTAMP
  expires_at          TIMESTAMP     NULL
  granted_for_trigger TEXT

TABLE: household_signal_log
  id                  UUID          PRIMARY KEY
  user_id             UUID          FK → users(id)
  signal_type         ENUM(H1–H5, S1–S5, M1–M3)
  matched_household   VARCHAR(30)   NULL
  signal_data_hash    TEXT
  evaluated_at        TIMESTAMP
  outcome             ENUM(LINKED, QUEUED, NEW_CREATED, FAILED)

ALL TABLES:
  Row-level security enforced on household_id
  Tenant isolation enforced on tenant_id
  household_link_events.immutable = TRUE — no updates, no deletes

═══════════════════════════════════════════════
API CONTRACT REGISTRY — MANDATORY
═══════════════════════════════════════════════

ALL APIs require valid JWT. RBAC enforced per route.

POST   /household/create
  Auth: Authenticated user
  Body: { user_id, region_code, privacy_level }
  Response: { household_id, hgs_score, tier }
  Side-effects: household_created event emitted

POST   /household/link
  Auth: HEAD_OF_HOUSEHOLD or HOUSEHOLD_ADULT
  Body: { user_id, signal_type, signal_data }
  Response: { status: LINKED | QUEUED | FAILED, household_id }

POST   /household/invite
  Auth: HEAD_OF_HOUSEHOLD or HOUSEHOLD_ADULT
  Body: { target_email?, target_phone?, invited_role }
  Response: { token, expires_at }

POST   /household/invite/accept
  Auth: Invited user (token validated)
  Body: { token, consent: true }
  Response: { household_id, member_role, hgs_tier }

POST   /household/invite/reject
  Auth: Invited user (token validated)
  Body: { token }
  Response: { status: REJECTED }

GET    /household/{household_id}/profile
  Auth: Any household member (privacy_level enforced)
  Response: { household_id, members[], hgs_score, tier, benefits[] }

GET    /household/{household_id}/members
  Auth: HEAD_OF_HOUSEHOLD or HOUSEHOLD_ADULT
  Response: { members[{ user_id, role, verified, linked_at }] }

GET    /household/{household_id}/hgs
  Auth: Any household member
  Response: { hgs_score, tier, breakdown[], next_tier_gap }

PATCH  /household/{household_id}/privacy
  Auth: HEAD_OF_HOUSEHOLD only
  Body: { privacy_level: OPEN | RESTRICTED | SEALED }
  Response: { updated: true }

POST   /household/{household_id}/transfer-head
  Auth: HEAD_OF_HOUSEHOLD only
  Body: { new_head_user_id }
  Response: { status: TRANSFERRED }
  Side-effects: audit event, notification to all members

DELETE /household/member/{user_id}
  Auth: HEAD_OF_HOUSEHOLD or self-removal
  Body: { reason }
  Response: { status: REMOVED }
  Note: HOUSEHOLD_ID preserved; member record soft-deleted
  Note: Cannot remove HEAD_OF_HOUSEHOLD (must transfer first)

GET    /household/signal-scan/{user_id}
  Auth: SYSTEM INTERNAL ONLY (no external call)
  Response: { signals_matched[], suggested_household_id, confidence }

═══════════════════════════════════════════════
EVENT SCHEMA REGISTRY — MANDATORY
═══════════════════════════════════════════════

All events emitted to Kafka / RabbitMQ event bus.
Schema versioned. AsyncAPI 2.6.0 compliant.

EVENT: household.created
  household_id:   STRING
  head_user_id:   UUID
  region_code:    STRING
  created_at:     ISO8601

EVENT: household.member.added
  household_id:   STRING
  user_id:        UUID
  member_role:    STRING
  signal_type:    STRING
  linked_at:      ISO8601

EVENT: household.member.removed
  household_id:   STRING
  user_id:        UUID
  removed_by:     UUID
  reason:         STRING

EVENT: household.hgs.updated
  household_id:   STRING
  old_score:      INTEGER
  new_score:      INTEGER
  old_tier:       STRING
  new_tier:       STRING
  trigger:        STRING

EVENT: household.tier.upgraded
  household_id:   STRING
  new_tier:       ENUM(BRONZE,SILVER,GOLD,PLATINUM)
  hgs_score:      INTEGER

EVENT: household.link.requested
  household_id:   STRING
  requesting_user: UUID
  signal_type:    STRING
  request_id:     UUID

EVENT: household.link.confirmed
  household_id:   STRING
  confirmed_by:   UUID
  linked_user:    UUID

EVENT: household.link.rejected
  household_id:   STRING
  rejected_by:    UUID
  requesting_user: UUID

EVENT: household.invitation.sent
  household_id:   STRING
  invited_by:     UUID
  token:          UUID
  channel:        ENUM(EMAIL,SMS,WHATSAPP,LINK)

EVENT: household.invitation.accepted
  household_id:   STRING
  accepted_by:    UUID
  member_role:    STRING

EVENT: household.antigravity.triggered
  household_id:   STRING
  trigger_type:   STRING
  payload:        JSONB

EVENT: household.decay.detected
  household_id:   STRING
  inactivity_days: INTEGER
  hgs_delta:      INTEGER

═══════════════════════════════════════════════
ANTIGRAVITY NOTIFICATION LOOP — RULES ENGINE
═══════════════════════════════════════════════

The Antigravity Notification Loop is a deterministic, rule-driven
re-engagement engine that uses household graph awareness to pull
unregistered or inactive family members back to the platform.

ALL RULES ARE DETERMINISTIC. NO AI. NO INTERPRETATION.

RULE AG-01: STUDENT PLACEMENT → PARENT ALERT
  Trigger: career_milestone.placement_achieved for HOUSEHOLD_STUDENT
  Action:
    → Notify all HOUSEHOLD_GUARDIAN members
    → Send household-level success notification
    → Increment HGS +30
    → Emit household_milestone_achieved event
    → Generate success story card (see R80)

RULE AG-02: INCOMPLETE HOUSEHOLD → INVITE PUSH
  Trigger: household_completeness_score < 60 AND member_count < 3
  Frequency: Once per 14 days
  Action:
    → Generate personalized family invite for HEAD_OF_HOUSEHOLD
    → Suggest missing roles (e.g., "Add your parent to unlock Gold tier")
    → Provide one-click invite link
    → Emit household.antigravity.triggered

RULE AG-03: TIER UPGRADE OPPORTUNITY → ENGAGEMENT PUSH
  Trigger: HGS within 20 points of next tier threshold
  Action:
    → Notify HEAD_OF_HOUSEHOLD of tier gap
    → Show specific actions to reach next tier
    → Highlight unlockable benefits at next tier
    → Emit household.antigravity.triggered

RULE AG-04: PARENT NOT ONBOARDED → STUDENT NUDGE
  Trigger: HOUSEHOLD_STUDENT active AND no HOUSEHOLD_GUARDIAN linked
    AND student_age_range 17–24
  Frequency: Once per 21 days
  Action:
    → Notify student: "Your parent can track your placement journey"
    → Provide guardian invite link pre-filled for student
    → Emit household.antigravity.triggered

RULE AG-05: HGS DECAY → RE-ENGAGEMENT
  Trigger: household.decay.detected event received
  Action:
    → Notify HEAD_OF_HOUSEHOLD of inactivity
    → List last active member and last activity date
    → Suggest specific activity to reverse decay
    → Emit household.antigravity.triggered

RULE AG-06: SIBLING DETECTED → AUTO-INVITE
  Trigger: New student registers with SIGNAL_S3 or SIGNAL_S4
    matching existing HOUSEHOLD_STUDENT's household
  Action:
    → Queue household_link_confirmation_required for HEAD
    → Pre-generate sibling invite message
    → Do NOT auto-link (TIER 2 signal)

RULE AG-07: REFERRAL REWARD → HOUSEHOLD NOTIFICATION
  Trigger: household.member.added where linked_by_signal = H3 (invitation)
  Action:
    → Credit Household Benefit Ledger
    → Notify all household members of new member
    → Announce HGS increase

RULE AG-08: PLACEMENT ANNIVERSARY → RETENTION LOOP
  Trigger: 1 year after placement_achieved milestone
  Action:
    → Generate "1 Year Anniversary" household milestone card
    → Prompt student to update career progress
    → Suggest alumni mentor role
    → Emit household.antigravity.triggered

RULE AG-09: STUDENT IDLE → HOUSEHOLD GUARDIAN ALERT
  Trigger: HOUSEHOLD_STUDENT inactive > 14 days AND
    active placement season detected (from market calendar)
  Action:
    → Notify HOUSEHOLD_GUARDIAN (if linked)
    → Message: "Student's platform activity has paused during placement season"
    → Provide guardian view link
    → Emit household.antigravity.triggered

RULE AG-10: HGS PLATINUM ACHIEVED → VIRAL AMPLIFICATION
  Trigger: household.tier.upgraded where new_tier = PLATINUM
  Action:
    → Generate shareable platinum household card
    → Unlock concierge placement support benefit
    → Public household achievement (if privacy_level = OPEN)
    → Emit household.antigravity.triggered

═══════════════════════════════════════════════
HOUSEHOLD COMPLETENESS SCORE — COMPUTATION
═══════════════════════════════════════════════

completeness_score is computed on every member change event.

COMPLETENESS FORMULA:
  score = 0

  IF head_of_household.profile_complete:          score += 20
  IF head_of_household.verified:                  score += 10
  IF any HOUSEHOLD_STUDENT linked:                score += 20
  IF any HOUSEHOLD_GUARDIAN linked to student:    score += 15
  IF household_privacy_level configured:          score += 5
  IF consent_given = TRUE for all members:        score += 15
  IF household.last_activity_at within 7 days:    score += 15

  completeness_score = MIN(score, 100)

COMPLETENESS GATES:
  < 30%:  Antigravity incomplete household nudge active (RULE AG-02)
  ≥ 50%:  Household Benefit Ledger unlocked
  ≥ 80%:  Priority placement alerts enabled for all members
  = 100%: Household Completeness Badge issued

═══════════════════════════════════════════════
PRIVACY & CONSENT FRAMEWORK — MANDATORY
═══════════════════════════════════════════════

CONSENT RULES:
  Rule C1: No member may view another member's profile
    without explicit privacy_level = OPEN or individual consent
  Rule C2: HOUSEHOLD_GUARDIAN linked to HOUSEHOLD_STUDENT
    may view that student's placement pipeline status only
  Rule C3: HEAD_OF_HOUSEHOLD may view member list and HGS only
    (not individual performance data unless shared)
  Rule C4: Consent must be re-confirmed on any role change
  Rule C5: Any member may remove themselves from household
    (soft-delete; HOUSEHOLD_ID preserved)
  Rule C6: Consent for minor (HOUSEHOLD_MINOR) requires
    HEAD_OF_HOUSEHOLD approval
  Rule C7: All consent events are immutable and logged

PRIVACY LEVELS:
  OPEN:       Household profile visible to platform (for viral sharing)
  RESTRICTED: Household internal only (default)
  SEALED:     No cross-member data visible (maximum isolation)

GDPR / Data Rights:
  Export: /household/{id}/export → full household data pack
  Forget: /household/{id}/forget → anonymizes, preserves HOUSEHOLD_ID shell
  Access: governed by RBAC per member role

═══════════════════════════════════════════════
SECURITY BASELINE — MANDATORY
═══════════════════════════════════════════════

SEC-01: All household endpoints protected by JWT (short-lived, 15 min)
SEC-02: household_id never exposed in URL for non-members
SEC-03: Signal data (phone, address) stored as one-way hash — never raw
SEC-04: Invitation tokens expire in 72 hours — one-time use
SEC-05: HEAD_OF_HOUSEHOLD transfer requires MFA re-confirmation
SEC-06: Cross-household merge requires admin approval (never automated)
SEC-07: Rate limit: max 10 link attempts per user per 24 hours
SEC-08: All household_link_events are immutable (no UPDATE, no DELETE)
SEC-09: Signal scan endpoint is INTERNAL ONLY — never external-facing
SEC-10: Household signal log retains hashes only — never raw PII

═══════════════════════════════════════════════
BENEFIT REGISTRY — HOUSEHOLD TIER UNLOCKS
═══════════════════════════════════════════════

BRONZE (HGS 0–49):
  - Basic placement notifications
  - Standard profile features

SILVER (HGS 50–99):
  - Guardian placement pipeline view
  - Family invite reward credits
  - Household progress dashboard

GOLD (HGS 100–199):
  - Priority job match notifications (before general pool)
  - Dedicated household placement advisor assignment
  - Family achievement milestone cards
  - Extended profile storage

PLATINUM (HGS 200+):
  - Concierge placement support
  - Direct recruiter introduction (for premium companies)
  - Household alumni network access
  - Annual household achievement report
  - Shareable public household success card (opt-in)

Benefits stored in: household_benefit_ledger
Benefits computed by: RULE ENGINE ONLY (not ML)
Benefits audit trail: IMMUTABLE

═══════════════════════════════════════════════
FAILURE HANDLING — NON-NEGOTIABLE
═══════════════════════════════════════════════

FAILURE: Duplicate HOUSEHOLD_ID generation
  → STOP → regenerate → retry max 3 times → alert ops → STOP

FAILURE: Signal scan returns ambiguous match (2+ households)
  → DO NOT AUTO-LINK to either
  → Queue for admin review
  → Emit household.link.ambiguous event
  → Create new household as fallback

FAILURE: HEAD_OF_HOUSEHOLD user deleted
  → Promote oldest HOUSEHOLD_ADULT to HEAD
  → If no adult present → SEAL household pending review
  → Emit household.head.vacant event
  → Alert ops

FAILURE: Invalid invitation token (expired / used)
  → Reject silently with generic error
  → Log attempt
  → Rate limit requestor

FAILURE: Consent revoked by member mid-session
  → Immediately revoke session tokens
  → Remove active benefit access
  → Log event
  → Do NOT expose household data post-revocation

FAILURE: HGS computation error
  → Preserve last known valid HGS
  → Flag for recomputation
  → Alert ops
  → Do NOT downgrade tier on compute failure

═══════════════════════════════════════════════
INTERN EXECUTION MAPPING — STEP BY STEP
═══════════════════════════════════════════════

INTERN ROLE: Backend Python Developer

STEP 1: Create database tables
  File: /backend/services/identity_service/migrations/
  Run: alembic upgrade head
  Expected: Tables household, household_members,
    household_link_events, household_invitation_tokens,
    household_benefit_ledger, household_signal_log created

STEP 2: Implement signal scan service
  File: /backend/services/identity_service/signal_scanner.py
  Logic: Evaluate TIER 1, 2, 3 signals in order
  Test: python -m pytest tests/test_signal_scanner.py

STEP 3: Implement household creation endpoint
  File: /backend/services/identity_service/routers/household.py
  Route: POST /household/create
  Test: curl -X POST /household/create with valid JWT

STEP 4: Implement HGS computation engine
  File: /backend/services/identity_service/hgs_engine.py
  Logic: Rule-based formula (no ML)
  Test: python -m pytest tests/test_hgs_engine.py

STEP 5: Implement Antigravity notification rules
  File: /backend/services/notification_service/antigravity_rules.py
  Logic: Rule AG-01 through AG-10
  Trigger: Kafka consumer on household.* events

STEP 6: Wire event emissions to Kafka
  File: /backend/services/identity_service/event_publisher.py
  Expected: All household events appear in Kafka topic household_events

SUCCESS CONDITION:
  ✔ All tables exist
  ✔ Signal scan returns correct TIER classification
  ✔ HGS computed correctly for test household
  ✔ Events appear in Kafka
  ✔ All pytest tests pass

Failure at any step → STOP → REPORT → DO NOT PROCEED

═══════════════════════════════════════════════
CONTRACT GATE OUTPUT
═══════════════════════════════════════════════

This agent produces the following contract gates:

  household_id_schema_ready       → consumed by: Job Service, GD Service,
                                    Notification Service, Billing Service
  household_event_schema_ready    → consumed by: Analytics, ClickHouse
  household_rbac_ready            → consumed by: RBAC engine, UI routing
  hgs_engine_ready                → consumed by: Benefit Service, UI
  antigravity_rules_ready         → consumed by: Notification Service

Absence of any gate output → STOP EXECUTION in dependent services

═══════════════════════════════════════════════
OBSERVABILITY REQUIREMENTS — MANDATORY
═══════════════════════════════════════════════

Prometheus Metrics (expose via /metrics):
  household_created_total            Counter
  household_member_added_total       Counter
  household_link_failed_total        Counter
  household_hgs_score_histogram      Histogram (buckets: 10,50,100,200,500)
  household_tier_distribution        Gauge (per tier)
  antigravity_trigger_total          Counter (per rule)
  household_invitation_sent_total    Counter
  household_invitation_accepted_rate Gauge

Grafana Dashboard:
  Panel 1: Household creation rate (daily)
  Panel 2: Average HGS across all households
  Panel 3: Tier distribution pie chart
  Panel 4: Antigravity rule trigger frequency
  Panel 5: Link failure rate

Loki Logs:
  Every STEP in Antigravity flow logged with structured JSON
  Every FAILURE logged with error_code + household_id

OpenTelemetry Traces:
  Trace every /household/* API call end-to-end
  Trace every signal scan execution

Absence of observability stack → STOP EXECUTION

═══════════════════════════════════════════════
FINAL ENFORCEMENT SEAL
═══════════════════════════════════════════════

This agent is COMPLETE and DEPLOYABLE only when:

  ✔ All 6 database tables created with RLS enforced
  ✔ Signal scanner implements all 13 signals (H1–H5, S1–S5, M1–M3)
  ✔ HOUSEHOLD_ID generation produces valid, unique IDs
  ✔ All 5 member roles enforced at RBAC layer
  ✔ HGS formula produces deterministic output
  ✔ All 10 Antigravity rules implemented as event consumers
  ✔ All 10 API contracts return correct responses
  ✔ All household events emitting to event bus
  ✔ Privacy and consent rules enforced at every data access point
  ✔ All security baselines (SEC-01 through SEC-10) active
  ✔ Observability stack wired and dashboards present
  ✔ Intern step-by-step execution validated
  ✔ No ML / LLM used anywhere in this agent
  ✔ Audit trail immutable and queryable

Violation of any above:
  → STOP EXECUTION
  → REPORT HOUSEHOLD_ID_LINKING_AGENT INCOMPLETE
  → NO DEPLOYMENT CLAIM PERMITTED
  → ANTIGRAVITY ENGINE INACTIVE

═══════════════════════════════════════════════
AGENT SEAL
═══════════════════════════════════════════════

Agent:    HOUSEHOLD_ID_LINKING_AGENT
Version:  1.0
Status:   SEALED
Sealed:   March 2026
Authority: ECOSKILLER MASTER EXECUTION PROMPT v12.0
Mutation: PROHIBITED without version bump + human declaration
```

---

## SECTION 3 — INTEGRATION MAP

| Consuming Service | Consumes From This Agent | Purpose |
|---|---|---|
| Notification Service | `household.*` events | Antigravity rule execution |
| Analytics (ClickHouse) | `household.*` events | HGS trends, tier distribution |
| Billing Service | `household_benefit_ledger` | Tier-based plan entitlements |
| GD Orchestrator | `household_id` on user record | Batch participant grouping context |
| Student Dashboard UI | `/household/{id}/hgs` | HGS display widget |
| Parent Dashboard UI | `/household/{id}/profile` | Guardian placement view |
| Admin Ops Console | All household tables | Governance, merge, audit |
| Referral Engine (R52) | `household.invitation.*` events | Referral reward attribution |
| Placement Service | `household.member.*` events | Family milestone notifications |
| Career Lifecycle (R71) | `career_stage_changed` | HGS bonus trigger |

---

## SECTION 4 — UI SCREENS REQUIRED

> All screens governed by R29, R33, R43. Flutter + Next.js dual implementation mandatory.

| Screen | Platform | Role Access | Purpose |
|---|---|---|---|
| Household Onboarding Flow | Flutter | All users | First-time household creation or linking |
| Household Dashboard | Flutter | All members | HGS score, tier, member list, benefits |
| Invite Family Screen | Flutter | HEAD / ADULT | Send invitations via multiple channels |
| Guardian Placement View | Flutter | GUARDIAN | Student pipeline tracking |
| HGS Progress Screen | Flutter | All members | Visual tier progress + unlock guide |
| Household Privacy Settings | Flutter | HEAD only | Configure privacy and consent |
| Household Achievement Card | Flutter + Next.js | PUBLIC (opt-in) | Shareable viral card |
| Admin Household Console | Flutter | SUPER_ADMIN | Full household governance panel |
| Public Household Success Page | Next.js | PUBLIC | SEO-indexed family success story |

---

## SECTION 5 — DEPENDENCY GATES CONSUMED

This agent requires the following gates to already be active before execution:

```
identity_ready          (Lane A — Auth Service)
rbac_ready              (Lane A — Role Engine)
db_ready                (Lane B — PostgreSQL)
event_schema_ready      (Lane B — Kafka / RabbitMQ)
notification_ready      (Lane D — Notification Service)
```

Absence of any → HOUSEHOLD_ID_LINKING_AGENT cannot start.

---

## SECTION 6 — AGENT VERSION HISTORY

| Version | Date | Change |
|---|---|---|
| 1.0 | March 2026 | Initial sealed release — LOCKED |

**Next version must be declared as 1.1 with full human-signed change declaration.**  
**No silent mutations permitted.**

---

*ECOSKILLER — HOUSEHOLD_ID_LINKING_AGENT — ANTIGRAVITY CORE IDENTITY ENGINE*  
*SEALED · LOCKED · DETERMINISTIC · GOVERNED*  
*Mutation Policy: Add-only via version bump | Execution Authority: Human declaration only*
