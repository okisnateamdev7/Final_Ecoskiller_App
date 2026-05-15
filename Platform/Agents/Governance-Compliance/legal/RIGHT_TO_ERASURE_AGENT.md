# 🗑️ RIGHT_TO_ERASURE_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED PRODUCTION ARTIFACT
### Document Class: Data Subject Rights · GDPR Article 17 · India DPDP · Privacy Enforcement Agent
### Mutation Policy: ADD-ONLY via signed version bump
### Execution Mode: DETERMINISTIC + VALIDATED + IRREVERSIBLE-FORWARD
### Interpretation Authority: NONE
### Architecture Authority: LOCKED
### Version: v1.0.0-SEALED
### Seal Date: 2026-02-28T00:00:00Z
### Seal Authority: PLATFORM GOVERNANCE BOARD + COMPLIANCE TEAM

---

> ⚠️ **THIS DOCUMENT IS SEALED.**
> No agent, human, or AI system may alter the erasure scope, retention exceptions, execution phases, legal basis definitions, or audit policy of this agent without a formal version bump, compliance team sign-off, legal team review, and governance board approval, followed by an append-only changelog entry.
> Silent modification = REGULATORY VIOLATION.
> Retroactive alteration of any erasure audit record = CRIMINAL LIABILITY RISK.

---

## 🔒 SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:                    RIGHT_TO_ERASURE_AGENT
AGENT_ID:                      RTEA-ANTIGRAVITY-003
SYSTEM_ROLE:                   Data Subject Rights Executor · GDPR Article 17 Enforcer · DPDP Compliance Engine
PRIMARY_DOMAIN:                User Privacy · Legal Compliance · Data Lifecycle · Tenant Data Rights
EXECUTION_MODE:                Deterministic + Validated + Irreversible-Forward (erasure is permanent)
DATA_SCOPE:                    All user PII, behavioral data, match data, scoring records, media, billing data, intelligence profiles, Dojo records, social data, session data
TENANT_SCOPE:                  Strict Isolation — erasure scoped to requesting user's tenant only
FAILURE_POLICY:                HALT ON AMBIGUITY → LOG INCIDENT → ESCALATE → NO PARTIAL ERASURE
CREATIVE_INTERPRETATION:       FORBIDDEN
ASSUMPTION_FILLING:            FORBIDDEN
DEFAULT_BEHAVIOR:              DENY (every erasure request is evaluated — not auto-approved)
LEGAL_FRAMEWORKS:              GDPR Article 17 (EU) · India DPDP Act 2023 · Platform Privacy Policy v[CURRENT]
IRREVERSIBILITY_POLICY:        Erasure is permanent and irreversible. No recovery mechanism exists post-completion.
HUMAN_GATE_REQUIRED:           YES — for exception cases and legal hold checks
AUDIT_RETENTION:               Erasure audit records retained PERMANENTLY (metadata only — no PII in audit)
STACK_LOCK:                    Flutter (Erasure Request UI) + React Next.js (Public Privacy Rights Page)
```

This agent executes the right of data subjects to request deletion of their personal data. It is the most legally consequential agent in the platform. Every execution path is deterministic, logged, verified, and irreversible. No ambiguity is tolerated.

---

## 🔒 SECTION 2 — PURPOSE DECLARATION

### Problem Solved
The RIGHT_TO_ERASURE_AGENT (RTEA) is the single, governed, legally compliant execution engine for all data deletion and erasure requests on the Ecoskiller Antigravity platform. It ensures:

- Users can exercise their legal right to erasure (GDPR Article 17, India DPDP) in a structured, verifiable manner
- All erasure operations are complete, consistent, cross-service, and auditable
- Legal retention exceptions are enforced before any data is deleted
- Immutable audit records of all erasure operations are preserved permanently
- No partial erasure leaves orphaned data across microservices
- Tenant data isolation is maintained throughout the erasure process
- Billing and legal obligations are honored before erasure proceeds
- The platform's scoring integrity, certification records, and audit trails are protected under legal retention rules
- All downstream agents are notified and synchronized on erasure completion

### What Input It Consumes
- User erasure requests from: `Flutter App → Account Settings → Delete Account`
- User erasure requests from: `React Web → Privacy Settings → Request Data Deletion`
- Operator-initiated erasure from: `Admin Operations Console → Right-to-Forget Execution Tool`
- Legal order erasure from: `GOVERNANCE_BOARD → Legal Hold Release`
- Automated DPDP/GDPR compliance triggers from: `COMPLIANCE_AGENT`
- Parental erasure requests (minor accounts) from: `PARENT_APP → Verified Parent Account`

### What Output It Produces
- `ERASURE_TICKET` — Unique, traceable erasure request record
- `LEGAL_HOLD_SCAN_RESULT` — Pre-erasure retention check result
- `ERASURE_EXECUTION_MANIFEST` — Per-service erasure instructions
- `ERASURE_COMPLETION_RECORD` — Immutable post-execution confirmation
- `DATA_DELETION_CONFIRMATION_NOTICE` — User-facing confirmation (no PII)
- `REGULATORY_DISCLOSURE_EVENT` — Feed to `PUBLIC_TRANSPARENCY_LOG_AGENT` (aggregate count only)
- `DOWNSTREAM_SYNC_EVENTS` — Notifications to all affected microservices

### Upstream Triggering Sources
```
USER (via Flutter App Settings)
USER (via React Web Privacy Page)
ADMIN (via Internal Ops Console — R40 Right-to-Forget Tool)
COMPLIANCE_AGENT (regulatory trigger)
GOVERNANCE_BOARD (legal order)
PARENT_APP (minor account erasure)
```

### Downstream Agents Notified on Erasure
```
USER_SERVICE
AUTH_SERVICE
SESSION_MANAGER
JOB_SERVICE
SKILL_SERVICE
PROJECT_SERVICE
DOJO_ENGINE
MATCH_ENGINE
SCORING_ENGINE
BELT_ENGINE
CERTIFICATION_ENGINE
REPLAY_ENGINE
ANALYTICS_SERVICE
INTELLIGENCE_ENGINE (AI Matching, Ranking)
FEATURE_STORE_AGENT
NOTIFICATION_SERVICE
BILLING_SERVICE
SOCIAL_SERVICE (Groups, Posts, Comments, Reactions)
MEDIA_SERVICE (Object Storage)
SEARCH_INDEX_SERVICE (OpenSearch)
AUDIT_TRAIL_AGENT (receives erasure event, does NOT delete audit logs)
PUBLIC_TRANSPARENCY_LOG_AGENT (aggregate count update only)
IDEA_DNA_AGENT
ROYALTY_ENGINE
```

---

## 🔒 SECTION 3 — INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "request_id",
    "requesting_user_id",
    "requesting_user_email_hash",
    "request_type",
    "request_channel",
    "request_timestamp_utc",
    "identity_verification_token",
    "erasure_scope",
    "user_acknowledgment_received",
    "tenant_id",
    "domain"
  ],
  "optional_fields": [
    "requesting_parent_id",
    "legal_order_reference",
    "operator_id",
    "operator_justification",
    "partial_erasure_scope_override",
    "regulatory_framework_tag"
  ],
  "validation_rules": [
    "request_id must be UUID v4",
    "request_timestamp_utc must be ISO 8601 UTC",
    "request_type must be one of [FULL_ERASURE, PARTIAL_ERASURE, ANONYMIZATION_ONLY, ACCOUNT_DEACTIVATION_ONLY]",
    "request_channel must be one of [USER_APP, USER_WEB, ADMIN_CONSOLE, LEGAL_ORDER, PARENT_APP, COMPLIANCE_AGENT]",
    "identity_verification_token must be a valid, unexpired (< 30 minutes) re-authentication token",
    "erasure_scope must be one of [ALL_DATA, PROFILE_ONLY, BEHAVIORAL_DATA, MEDIA_ONLY, SPECIFIED_CATEGORIES]",
    "user_acknowledgment_received must be boolean true",
    "tenant_id must reference an active tenant",
    "domain must be one of [Arts, Commerce, Science, Technology, Administration, PLATFORM_WIDE]",
    "No null values in required fields without explicit policy"
  ],
  "security_checks": [
    "Identity re-verification mandatory before erasure initiates (not just session JWT — separate re-auth)",
    "For FULL_ERASURE: MFA confirmation required",
    "For ADMIN_CONSOLE requests: Dual-operator authorization required (four-eyes principle)",
    "For LEGAL_ORDER requests: Governance board signed JWT required",
    "For PARENT_APP requests: Parent identity verification + minor account linkage confirmed",
    "Rate limiting: Maximum 1 FULL_ERASURE request per user per 30 days",
    "Anti-abuse: Check for patterns of request-cancel-request cycling",
    "Replay protection: request_id deduplication enforced"
  ],
  "domain_checks": [
    "Erasure scope must not cross tenant boundary",
    "Erasure must only affect the requesting user's own data",
    "Cross-tenant data references (e.g., match opponent records) must be anonymized, not deleted from opponent's record"
  ]
}
```

**Rules:**
- If `user_acknowledgment_received = false` → REJECT. Never proceed without explicit acknowledgment.
- If `identity_verification_token` is invalid or expired → REJECT + log `IDENTITY_VERIFICATION_FAILED`
- If `FULL_ERASURE` requested and MFA not confirmed → HOLD + prompt MFA re-verification
- If any required field missing → REJECT + log `INCOMPLETE_REQUEST`
- No partial processing on partially valid input

---

## 🔒 SECTION 4 — OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "erasure_ticket": {
    "ticket_id": "UUID",
    "request_id": "UUID (from input)",
    "ticket_created_utc": "ISO 8601",
    "status": "PENDING | IN_LEGAL_REVIEW | EXECUTING | COMPLETED | REJECTED | ON_HOLD",
    "erasure_scope": "string",
    "legal_hold_status": "CLEAR | PARTIAL_HOLD | FULL_HOLD",
    "retained_categories": ["list of data categories under legal hold with retention reason"],
    "scheduled_completion_utc": "ISO 8601",
    "actual_completion_utc": "ISO 8601 (null until complete)"
  },
  "confidence_score": "0.0 - 1.0",
  "model_version": "RTEA-v1.0.0",
  "audit_reference": "UUID",
  "erasure_manifest": {
    "services_to_process": ["list"],
    "services_completed": ["list"],
    "services_failed": ["list"],
    "data_categories_erased": ["list"],
    "data_categories_retained": ["list with legal basis"],
    "anonymization_applied": ["list of anonymized-only records"],
    "third_party_notifications_required": ["list"]
  },
  "next_trigger_events": [
    "ERASURE_INITIATED",
    "LEGAL_HOLD_CLEARED",
    "ERASURE_EXECUTING",
    "ERASURE_COMPLETED",
    "CONFIRMATION_SENT",
    "DOWNSTREAM_SYNC_COMPLETE"
  ],
  "user_confirmation": {
    "confirmation_sent": true,
    "channel": "EMAIL + IN_APP",
    "content": "Plain language summary (no PII)",
    "ticket_reference": "Public-safe ticket reference code"
  }
}
```

---

## 🔒 SECTION 5 — DATA INVENTORY MAP (COMPLETE)

This section defines every category of data the platform holds for a user, the default erasure action, and any retention exceptions. This map is the authoritative source for the erasure manifest.

### 5A — Identity & Authentication Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Full name | PostgreSQL: users | ERASE | None |
| Email address | PostgreSQL: users | ERASE | Suppression list (hash only — to prevent re-registration abuse, 2 years) |
| Phone number | PostgreSQL: users | ERASE | None |
| Password hash | PostgreSQL: users | ERASE | None |
| Profile avatar / photo | MinIO Object Storage | ERASE | None |
| Device session records | PostgreSQL: sessions | ERASE | None |
| OAuth tokens | Redis / Token Store | ERASE (revoke + delete) | None |
| MFA configuration | Auth Service DB | ERASE | None |
| Login history | Audit Log | ANONYMIZE (actor_id → DELETED_USER) | 7 years (legal audit obligation) |

### 5B — Professional Profile Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Skills, endorsements | PostgreSQL: user_skills | ERASE | None |
| Work experience | PostgreSQL: work_experience | ERASE | None |
| Education history | PostgreSQL: education | ERASE | None |
| Portfolio items | PostgreSQL + MinIO | ERASE | None |
| Resume / CV files | MinIO Object Storage | ERASE | None |
| Intelligence profile (EIE scores) | PostgreSQL: user_intelligence_profile | ERASE | Aggregate anonymized stats retained (no user linkage) |
| Career DNA profile | PostgreSQL: career_dna | ERASE | None |

### 5C — Job Portal Activity

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Job applications | PostgreSQL: applications | ERASE (user-side) | Recruiter copy: anonymized (applicant_id → DELETED_USER) — 2 years for recruiter compliance |
| Saved jobs | PostgreSQL: saved_jobs | ERASE | None |
| Application messages | PostgreSQL: messages | ERASE (user messages) | Counterparty thread: anonymized sender — 2 years |
| Offer records | PostgreSQL: offers | ANONYMIZE | 7 years (financial/legal audit obligation) |
| Interview feedback received | PostgreSQL: interview_feedback | ERASE (user view) | Anonymized aggregate version retained for platform analytics |

### 5D — Skill Development & Learning Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Learning path progress | PostgreSQL: learning_progress | ERASE | Aggregate completion stats (no user linkage) |
| Drill attempt records | PostgreSQL: drill_attempts | ERASE | None |
| Skill gap analysis results | PostgreSQL: skill_gaps | ERASE | None |
| Course enrollment records | PostgreSQL: enrollments | ERASE | Certification-linked records: see 5F |

### 5E — Dojo / Match / GD Records

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Match participation records | PostgreSQL: matches | ANONYMIZE (user_id → DELETED_USER) | 7 years — scoring integrity + anti-fraud obligation |
| Match scores (user's own) | PostgreSQL: scores | ANONYMIZE | 7 years — scoring audit obligation |
| Peer scores given by user | PostgreSQL: peer_scores | ANONYMIZE | 7 years — integrity audit |
| Mentor feedback received | PostgreSQL: mentor_feedback | ERASE (user's personal copy) | Anonymized version retained for mentor calibration audit — 7 years |
| Match replay recordings | MinIO Object Storage | ERASE (user-identifiable version) | Anonymized transcript (voice removed, text anonymized) may be retained for scoring validation — governance board decision required |
| Replay transcripts | PostgreSQL: transcripts | ERASE (user-identifiable content) | Anonymized version — 7 years scoring audit |
| Scenario attempt history | PostgreSQL: scenario_attempts | ERASE | Aggregate difficulty calibration stats retained (no user linkage) |
| Match chat logs | PostgreSQL: match_chat | ERASE | None |
| Live video room metadata | PostgreSQL: video_rooms | ANONYMIZE | 2 years (platform integrity) |
| WebSocket session logs | Loki (Log Store) | ANONYMIZE | 2 years |

### 5F — Belt & Certification Records

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Belt progression records | PostgreSQL: belt_progression | ANONYMIZE | **7 years** — certification validity audit, talent marketplace trust |
| Issued certificates (digital) | PostgreSQL: certifications | ANONYMIZE | **7 years** — institutional recognition obligation |
| Certificate PDF files | MinIO | ERASE (user's copy) | Metadata record anonymized — 7 years |
| Mentor certification decisions | PostgreSQL: cert_decisions | ANONYMIZE | **7 years** — governance audit |
| Re-certification records | PostgreSQL: recertifications | ANONYMIZE | 7 years |

> **⚠️ CRITICAL NOTE:** Belt and certification records are retained in anonymized form because third parties (employers, institutions) may have received or referenced these certificates. Erasure of certification metadata could undermine third-party verification. Anonymization is the legally defensible action — not deletion. The user's identity linkage is removed; the certification event record is retained.

### 5G — Project Execution Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Project submissions | PostgreSQL: project_submissions | ERASE | Anonymized milestone records — 2 years (project integrity) |
| Portfolio auto-generated items | PostgreSQL: portfolio_items | ERASE | None |
| Mentor assignment records | PostgreSQL: mentor_assignments | ANONYMIZE | 2 years |
| Milestone evaluation scores | PostgreSQL: milestone_scores | ANONYMIZE | 2 years |

### 5H — Social & Organization Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Posts authored | PostgreSQL: posts | ERASE (content) + replace with [DELETED USER POST] | None — but post structure retained for thread integrity 90 days |
| Comments authored | PostgreSQL: comments | ERASE (content) + replace with [DELETED USER COMMENT] | Thread structural record: 90 days |
| Reactions given | PostgreSQL: reactions | ERASE | None |
| Group memberships | PostgreSQL: group_members | ERASE | None |
| Anonymous complaint records | PostgreSQL: anonymous_complaints | **SPECIAL HANDLING** — see Section 12 | Legal investigation hold possible |
| Follow relationships | PostgreSQL: follows | ERASE | None |
| Institution/Company affiliation | PostgreSQL: org_members | ERASE (user's side) | Org's verified record: anonymized — 2 years |

### 5I — Billing & Payment Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Subscription records | PostgreSQL: subscriptions | ANONYMIZE | **7 years** (financial/tax legal obligation) |
| Payment transactions | PostgreSQL: payments | ANONYMIZE | **7 years** (financial/tax legal obligation — GST/VAT records) |
| Invoice records | PostgreSQL: invoices | ANONYMIZE | **7 years** |
| Refund records | PostgreSQL: refunds | ANONYMIZE | **7 years** |
| Billing address | PostgreSQL: billing_details | ERASE | Anonymized transaction record retained — 7 years |
| Payment card metadata (tokenized) | Stripe / Payment Provider | DELETE via provider API | Provider 7-year financial obligation applies separately |
| Coupon / discount usage | PostgreSQL: coupon_usage | ANONYMIZE | 7 years (financial audit) |

### 5J — Intelligence & Behavioral Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Feature vectors | Feature Store (Redis/PostgreSQL) | ERASE | Aggregate ML training set (no user linkage) — retained |
| AI match scores | PostgreSQL: ai_scores | ERASE | Aggregate model evaluation stats retained |
| Behavioral analytics events | Analytics Service / Kafka | ERASE from hot store | Anonymized aggregate retained in cold analytics — 2 years |
| Recommendation history | PostgreSQL: recommendations | ERASE | None |
| Placement probability scores | PostgreSQL: placement_scores | ERASE | None |
| Intelligence profile (Gardner EIE) | PostgreSQL: user_intelligence_profile | ERASE | Aggregate anonymized population stats retained |

### 5K — Audit & System Logs

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Audit log entries (actor_id references) | PostgreSQL: audit_logs | ANONYMIZE (actor_id → DELETED_USER_{hash}) | **PERMANENT RETENTION** — audit logs are immutable and cannot be deleted |
| System access logs | Loki | ANONYMIZE | 7 years |
| API request logs | Loki | ANONYMIZE | 2 years |
| Security event logs | Loki | ANONYMIZE | 7 years |
| Scoring override logs | PostgreSQL: score_overrides | ANONYMIZE | 7 years |
| Mentor command logs | PostgreSQL: mentor_commands | ANONYMIZE | 7 years |

> **⚠️ ABSOLUTE RULE:** Audit logs are immutable. The audit trail cannot be deleted even upon erasure request. The user's identity within audit logs is ANONYMIZED — not deleted. This is the legally correct approach under GDPR Recital 65 and Article 17(3)(b) (legal obligation retention) and is consistent with append-only audit architecture.

### 5L — Search Index Data

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| User profile search index | OpenSearch | ERASE index documents | None — re-indexing confirmed after erasure |
| Job application search references | OpenSearch | ANONYMIZE references | None |
| Public portfolio search index | OpenSearch | ERASE | None |

### 5M — Media & Files

| Data Category | Storage Location | Default Action | Retention Exception |
|---|---|---|---|
| Profile photos | MinIO | ERASE | None |
| Resume/CV files | MinIO | ERASE | None |
| Portfolio project files | MinIO | ERASE | None |
| Video room recordings (identified) | MinIO | ERASE user-identifiable version | Anonymized scoring reference version — governance decision required |
| Uploaded documents | MinIO | ERASE | None |

---

## 🔒 SECTION 6 — LEGAL RETENTION EXCEPTIONS (LOCKED)

The following legal basis categories permit or require data retention despite an erasure request. All retained data must be anonymized where technically feasible.

| Exception Code | Legal Basis | Retention Period | Applies To |
|---|---|---|---|
| LR-TAX | Tax/Financial compliance obligation | 7 years | Billing, payments, invoices |
| LR-AUDIT | Platform integrity audit obligation | 7 years | Scoring, certification, mentor, match records |
| LR-CERT | Certification validity for third parties | 7 years | Belt records, certificates |
| LR-LEGAL | Legal proceedings / court order | Duration of proceedings + 3 years | Any data subject to order |
| LR-FRAUD | Anti-fraud integrity | 2 years | Anonymized match/fraud records |
| LR-MINOR | Minor protection legal obligation | Until subject turns 18 + 2 years | Minor account data |
| LR-ANON | Anonymized aggregate — no personal data | Indefinite | Statistical aggregates (no user linkage) |
| LR-THREAD | Social thread structural integrity | 90 days | Post/comment structure (content erased) |
| LR-SUPPRESS | Email suppression list (re-registration abuse prevention) | 2 years | Email hash only (no PII value) |
| LR-SAFETY | Behavioral safety investigation | Duration of investigation + 1 year | Harassment, abuse reports |
| LR-COMPLAINT | Anonymous complaint under investigation | Duration of investigation | Anonymous complaint records |

**If any retention exception applies:**
- The user MUST be informed which categories are being retained
- The user MUST be told the legal basis
- The user MUST be told the retention period
- The retained data MUST be anonymized where technically feasible
- Full erasure must still proceed for all non-retained categories

---

## 🔒 SECTION 7 — ERASURE EXECUTION PHASES

Erasure is an irreversible-forward, multi-phase operation. Each phase must complete before the next begins. No phase may be skipped.

### PHASE 0: REQUEST INTAKE & VALIDATION (< 2 minutes)
```
→ Validate input schema (Section 3)
→ Verify identity re-authentication token
→ Confirm MFA for FULL_ERASURE
→ Apply rate limiting check
→ Deduplication check (request_id)
→ Create ERASURE_TICKET with status: PENDING
→ Send acknowledgment to user: "We received your request. Ticket: [REF]"
→ Log ERASURE_REQUEST_RECEIVED to audit store
```

### PHASE 1: LEGAL HOLD SCAN (< 24 hours SLA)
```
→ Query BILLING_SERVICE: Outstanding payments? Disputed charges?
→ Query COMPLIANCE_AGENT: Active legal hold, court order, or regulatory investigation?
→ Query GOVERNANCE_BOARD_REGISTRY: Appeals pending? Governance cases open?
→ Query COLLUSION_DETECTION_AGENT: Active fraud investigation involving this user?
→ Query DOJO_ENGINE: Active match in progress? (Abort gracefully if yes)
→ Query CERTIFICATION_ENGINE: Certificate under active third-party verification?
→ Query SOCIAL_SERVICE: Anonymous complaint under active investigation?
→ Run data inventory scan across all services (Section 5)
→ Classify each data category: ERASE | ANONYMIZE | RETAIN (with legal basis)
→ Generate LEGAL_HOLD_SCAN_RESULT
→ Update ERASURE_TICKET status:
     → If FULL_HOLD: status = ON_HOLD, notify user with reason and expected hold duration
     → If PARTIAL_HOLD: status = IN_LEGAL_REVIEW, notify user of what will proceed and what is held
     → If CLEAR: status = EXECUTING → proceed to Phase 2
→ Log LEGAL_HOLD_SCAN_COMPLETE to audit store
```

**SLA:** Legal hold scan must complete within 24 hours of ticket creation.
**Human gate:** If active legal hold detected → Compliance team must manually review before proceeding.

### PHASE 2: PRE-ERASURE ACCOUNT FREEZE (< 5 minutes)
```
→ Revoke all active JWT tokens for the user
→ Revoke all active video room tokens (LiveKit)
→ Force-close all active WebSocket connections
→ Terminate active match rooms (graceful abort — opponent notified)
→ Deactivate user account login (account status = ERASURE_IN_PROGRESS)
→ Prevent new logins while erasure is executing
→ Cancel any pending notifications for the user
→ Emit ACCOUNT_FROZEN event to all services
→ Log ACCOUNT_FROZEN to audit store
```

### PHASE 3: ACTIVE DATA ERASURE (< 72 hours SLA)
```
Executed in this strict order to maintain referential integrity:

  3.1 — Media & File Erasure (MinIO)
        → Delete profile photos, resumes, portfolio files
        → Delete user-identifiable video recordings
        → Confirm deletion with MinIO object lifecycle proof

  3.2 — Search Index Erasure (OpenSearch)
        → Delete user profile documents from search index
        → Anonymize job application references
        → Trigger re-index validation

  3.3 — Behavioral & Intelligence Data Erasure
        → Erase feature vectors from Feature Store
        → Erase AI scores from AI service
        → Erase behavioral analytics from hot store
        → Erase intelligence profile records
        → Emit FEATURE_VECTOR_ERASED to FEATURE_STORE_AGENT

  3.4 — Social & Communication Data Erasure
        → Erase post content (replace with [DELETED USER POST])
        → Erase comment content (replace with [DELETED USER COMMENT])
        → Erase reactions, follows, group memberships
        → Notify affected group admins (no PII disclosed)

  3.5 — Dojo / Match / Scoring Data (Anonymize, not delete)
        → Replace user_id with ANON_{ticket_id_hash} in match records
        → Replace user_id in scoring records
        → Replace user_id in belt progression records
        → Erase personal scoring narratives (keep score values — anonymized)
        → Anonymize replay metadata (erase identifiable content)

  3.6 — Certification Records (Anonymize, not delete — LR-CERT)
        → Replace user_id with ANON_{ticket_id_hash}
        → Remove name from certification record
        → Retain certificate event metadata (anonymized)
        → Invalidate certificate public verification link (or replace with "User Requested Erasure" state)

  3.7 — Project & Learning Data Erasure
        → Erase learning progress records
        → Erase project submission content
        → Anonymize milestone evaluation records

  3.8 — Job Portal Activity Erasure
        → Erase job application user-side data
        → Anonymize application references on recruiter side (LR-FRAUD, LR-AUDIT)
        → Erase saved jobs, preferences
        → Anonymize offer records (LR-TAX)

  3.9 — Billing & Payment Anonymization (retain anonymized — LR-TAX)
        → Anonymize billing records (replace user_id with ANON_{ticket_id_hash})
        → Delete billing address
        → Trigger Stripe/Payment Provider token deletion via provider API
        → Confirm provider deletion receipt

  3.10 — Authentication & Profile Erasure
         → Delete user profile record (all PII fields)
         → Add email hash to suppression list (LR-SUPPRESS — 2 years, no PII stored)
         → Delete all OAuth tokens
         → Delete MFA configuration
         → Delete device session records

  3.11 — Audit Log Anonymization (NEVER DELETE — append-only rule)
          → Replace actor_id references in audit logs with ANON_{ticket_id_hash}
          → Log AUDIT_ANONYMIZATION_COMPLETE

  3.12 — Notification & Preference Erasure
          → Delete notification preferences
          → Delete notification history
          → Delete push notification tokens
```

### PHASE 4: THIRD-PARTY NOTIFICATION (< 7 days SLA)
```
→ Identify any third-party processors who received user data
→ Notify each third party of erasure request per data processing agreement
→ Applicable third parties include:
     - Payment processor (Stripe) → deletion confirmation requested
     - Email delivery provider (Postal) → user's email records deletion requested
     - Push notification provider (FCM) → token deletion requested
     - Any integrated LMS/HRIS partners (tenant-specific)
→ Log third-party notification events per Article 19 GDPR obligation
→ Record notifications in ERASURE_TICKET with timestamps
```

### PHASE 5: VERIFICATION SWEEP (< 24 hours after Phase 3 completion)
```
→ Run automated cross-service scan for remaining PII fields
→ Verify all erasure targets from manifest are confirmed deleted/anonymized
→ Run PII scanner (same engine as PUBLIC_TRANSPARENCY_LOG_AGENT) across all service DBs for user reference
→ If orphaned PII found → Trigger additional erasure → Re-run sweep
→ Confirm MinIO objects are deleted (list bucket for user's namespace)
→ Confirm OpenSearch documents are removed
→ Confirm Feature Store vectors are removed
→ Confirm Stripe/payment provider deletion receipt received
→ Generate VERIFICATION_SWEEP_REPORT
→ If all clear → Proceed to Phase 6
→ If gaps found → Return to Phase 3, document gaps, re-execute
```

### PHASE 6: COMPLETION & CONFIRMATION
```
→ Update ERASURE_TICKET status = COMPLETED
→ Set actual_completion_utc
→ Generate ERASURE_COMPLETION_RECORD (immutable)
→ Send DATA_DELETION_CONFIRMATION_NOTICE to user:
     → Channel: Email (to address before erasure) + Last in-app notification
     → Content: Plain language confirmation, ticket reference, what was deleted, what was retained with legal basis
     → PII-free confirmation (no data values in confirmation)
→ Emit ERASURE_COMPLETED to all downstream agents
→ Emit aggregate count update to PUBLIC_TRANSPARENCY_LOG_AGENT (count only, no user reference)
→ Log ERASURE_COMPLETED to audit store (immutable)
→ Close ERASURE_TICKET
```

**Total SLA from request receipt to completion: Maximum 30 days** (GDPR Article 12 requirement)
**Target operational SLA: 72 hours for standard requests**

---

## 🔒 SECTION 8 — ERASURE REQUEST TYPES

### TYPE 1: FULL_ERASURE (Account Deletion)
```yaml
SCOPE: All personal data across all services
LEGAL_HOLD_CHECK: Full scan required (Phase 1)
MFA_REQUIRED: YES
ACKNOWLEDGMENT_REQUIRED: YES (multi-step confirmation — minimum 2 confirmations)
COOLING_OFF_PERIOD: 14-day window during which user can cancel (configurable per regulatory jurisdiction)
CANCELLATION_WINDOW: Cancel before Phase 3 begins
POST_COMPLETION: Account permanently closed. Re-registration permitted (suppression list prevents same-email re-registration for 2 years)
SLA: 30 days maximum
```

### TYPE 2: PARTIAL_ERASURE (Specific Data Categories)
```yaml
SCOPE: User-specified categories (e.g., erase social posts but retain profile)
LEGAL_HOLD_CHECK: Category-scoped scan
MFA_REQUIRED: NO (standard re-auth sufficient)
ACKNOWLEDGMENT_REQUIRED: YES (single confirmation per category)
COOLING_OFF_PERIOD: None
SLA: 30 days maximum
```

### TYPE 3: ANONYMIZATION_ONLY
```yaml
SCOPE: Remove personal identifiers but retain anonymized records (user choice)
USE_CASE: User wants to preserve achievement history without identity linkage
LEGAL_HOLD_CHECK: Standard scan
MFA_REQUIRED: NO
ACKNOWLEDGMENT_REQUIRED: YES
SLA: 30 days maximum
```

### TYPE 4: ACCOUNT_DEACTIVATION_ONLY
```yaml
SCOPE: Deactivate account without erasure (data retained, account inaccessible)
NOTE: This is NOT an erasure operation. Data retained per retention policy.
REACTIVATION: Permitted within 90 days
POST_90_DAYS: Escalated to FULL_ERASURE process
MFA_REQUIRED: NO
SLA: Immediate
```

---

## 🔒 SECTION 9 — MINOR ACCOUNT ERASURE PROTOCOL

Aligned with Ecoskiller's parent trust layer and minor protection obligations.

```yaml
TRIGGERING_PARTY: Verified parent via PARENT_APP
PREREQUISITE: Parent identity verified + minor account linkage confirmed
MINOR_CONSENT: Required if minor is 16+ (GDPR Article 8 standard)
LEGAL_HOLD: Additional check — minor protection obligation (LR-MINOR)
SPECIAL_HANDLING:
  - If minor is currently enrolled in institutional program: notify institution admin before erasure (no PII)
  - If minor has active mentor sessions: graceful termination required
  - If minor has active tournament: resolution required before erasure
  - Parent receives confirmation to verified parent contact
AUDIT: Minor erasure events get elevated audit retention
```

---

## 🔒 SECTION 10 — ANONYMOUS COMPLAINT SPECIAL HANDLING

From R36 (Anonymous Verified Complaint System) — anonymous complaints require special erasure handling.

```yaml
SCENARIO: User who submitted anonymous complaint requests erasure

RULE 1 — IDENTITY MASKING ALREADY APPLIED:
  The real identity behind anonymous complaints is stored in secure audit storage (not in the public complaint record).
  The public complaint content is already identity-masked.

RULE 2 — ACTIVE INVESTIGATION HOLD:
  If the complaint is under active investigation:
    → Apply LR-SAFETY + LR-COMPLAINT retention hold
    → Inform user that erasure is partially deferred
    → Identity linkage erased from non-investigation stores
    → Identity within investigation record: LEGAL HOLD until investigation closes

RULE 3 — CLOSED INVESTIGATION:
  If complaint investigation is closed:
    → Erase identity linkage from secure audit storage
    → Complaint content itself: Retain anonymized (public interest/safety record) — governance board decision required
    → Inform user of outcome

RULE 4 — FALSE REPORT RECORDS:
  If user was flagged for submitting false reports:
    → LR-FRAUD hold applies (2 years) on flag record (anonymized)
    → Identity linkage erasure proceeds
```

---

## 🔒 SECTION 11 — TENANT ADMIN & OPERATOR ERASURE CONTROLS

Operators may initiate erasure via the Internal Admin & Ops Console (R40 — Right-to-Forget Execution Tool).

```yaml
OPERATOR_ERASURE_RULES:
  - Dual-operator authorization required (four-eyes principle)
  - Both operators must have DATA_ERASURE_EXECUTE permission
  - Operator erasure only permitted for users within operator's tenant scope
  - Platform admin can execute cross-tenant erasure (PLATFORM_ADMIN role only)
  - Full audit trail of operator identity, justification, and both authorizations
  - Operator-initiated erasure follows identical phases (Section 7) as user-initiated
  - Operator CANNOT bypass legal hold scan
  - Operator CANNOT bypass cooling-off period for FULL_ERASURE
  - Governance board must be notified for bulk erasure operations (> 10 users)

BULK_ERASURE:
  - Maximum 100 users per bulk operation
  - Requires governance board pre-approval
  - Each user still gets individual ERASURE_TICKET
  - Staggered execution (max 10 concurrent) to prevent database overload
  - Full audit log per user
```

---

## 🔒 SECTION 12 — CERTIFICATION & BELT RECORD POLICY (SPECIAL)

This section addresses one of the most legally complex aspects: belt and certification records that may be referenced by third parties.

```yaml
POLICY: Belt and certification records are ANONYMIZED, not deleted.

RATIONALE:
  - Employers, institutions, and talent marketplace users may have received or referenced a user's certification
  - Deletion of the certification event could enable fraudulent re-presentation of credentials
  - The user's identity is fully removed (ANON_{ticket_id_hash} replaces all PII fields)
  - The certification event exists as an anonymized historical record only
  - No third party can re-identify the user from the anonymized record

CERTIFICATE VERIFICATION LINKS:
  - Public certificate verification URL is invalidated upon erasure
  - Verification URL returns: "This certificate was issued to a user who has exercised their right to erasure. The certificate record exists but personal details are not available."
  - Employer/institution who received the certificate is notified via registered email (if applicable) — notification states erasure occurred, no PII disclosed

USER DISCLOSURE:
  - User is explicitly informed during Phase 0 that belt/cert records will be anonymized, not deleted
  - User must acknowledge this retention (part of required acknowledgment in Section 3)
  - User may appeal this retention to the Governance Board via the appeals process

RETENTION PERIOD: 7 years from certificate issuance (LR-CERT)
```

---

## 🔒 SECTION 13 — ML / AI LOGIC LAYER

```yaml
ML_COMPONENTS (70-80%):

  MODEL_1 — Data Inventory Scanner
    PURPOSE: Automated cross-service PII detection to build complete erasure manifest
    TYPE: Named Entity Recognition + Schema Pattern Matching
    FEATURES:
      - user_id reference frequency per service
      - email pattern detection
      - PII field schema matching
      - Cross-reference graph density
    OUTPUT: data_inventory_manifest (list of all user data locations)
    TRAINING_FREQUENCY: Quarterly (updated when new services are added)

  MODEL_2 — Legal Hold Risk Classifier
    PURPOSE: Assess likelihood of retention exceptions before human review
    TYPE: Multi-class classification
    FEATURES:
      - outstanding_payment_flag
      - active_investigation_flag
      - open_legal_order_flag
      - pending_appeal_flag
      - active_match_flag
      - certification_verification_request_in_flight_flag
    OUTPUT: hold_risk_score (0.0-1.0), recommended_action (PROCEED | REVIEW | HOLD)

  MODEL_3 — Orphaned PII Detector (Verification Sweep)
    PURPOSE: Phase 5 verification — detect any remaining PII after erasure
    TYPE: Pattern matching + anomaly detection
    FEATURES:
      - user_id occurrence in service stores post-erasure
      - email_hash occurrence
      - Name pattern occurrence in text fields
    OUTPUT: orphaned_pii_list (locations of remaining PII), sweep_confidence_score

AI_COMPONENTS (20-30%):

  AI_USAGE_SCOPE:
    - Generate plain-language user-facing confirmation notices
    - Generate plain-language explanation of retained data with legal basis
    - Correlate data inventory across complex cross-service relationships
    - Flag unusual erasure patterns (potential abuse detection)

  RESTRICTIONS:
    - AI CANNOT approve erasure requests autonomously
    - AI CANNOT determine legal hold decisions — human + Compliance Agent required
    - AI CANNOT generate erasure manifests without ML data inventory scan confirmation
    - AI output on legal matters must be reviewed by compliance team before user communication

  PROMPT_GOVERNANCE:
    - Versioned templates only (especially for legal notices)
    - No dynamic prompt construction
    - Deterministic structure enforced
    - All AI-generated legal notices tagged: [AI_ASSISTED_DRAFT — Compliance Reviewed]
```

---

## 🔒 SECTION 14 — SCALABILITY DESIGN

```yaml
EXECUTION_MODEL:    Async job pipeline (Kafka events + worker pool)
                    Each ERASURE_TICKET = independent async job
                    Phase execution is serialized per ticket
LATENCY_TARGET:
  - Phase 0 (intake): < 2 minutes
  - Phase 1 (legal scan): < 24 hours
  - Phase 3 (erasure execution): < 72 hours
  - Full completion: < 30 days (GDPR maximum)
  - Target operational: < 72 hours for standard requests
MAX_CONCURRENCY:    50 concurrent ERASURE_TICKET executions
                    10 concurrent FULL_ERASURE executions (resource-intensive)
QUEUE_STRATEGY:     Priority queue:
                    - Legal order erasure: P1 (immediate)
                    - Minor account erasure: P1
                    - Standard user request: P2
                    - Operator batch: P3
IDEMPOTENCY:        Guaranteed — request_id deduplication; phase completion is idempotent
BULK_PROTECTION:    Staggered execution prevents DB overload during bulk operations
DATABASE_IMPACT:    Erasure operations use low-priority DB connections to prevent impact on live operations
KAFKA_EVENTS:
  - ERASURE_REQUEST_RECEIVED
  - LEGAL_HOLD_SCAN_STARTED
  - LEGAL_HOLD_SCAN_COMPLETE
  - ACCOUNT_FROZEN
  - ERASURE_PHASE_{N}_COMPLETE
  - ERASURE_COMPLETED
  - THIRD_PARTY_NOTIFICATION_SENT
  - VERIFICATION_SWEEP_COMPLETE
```

---

## 🔒 SECTION 15 — SECURITY ENFORCEMENT

```yaml
✅ Identity re-authentication mandatory for all erasure requests (separate from session)
✅ MFA required for FULL_ERASURE
✅ Dual-operator authorization for operator-initiated erasure (four-eyes principle)
✅ Governance board JWT required for legal order erasure
✅ Rate limiting: 1 FULL_ERASURE request per user per 30 days
✅ Tenant isolation: operator cannot erase users outside their tenant
✅ JWT scope: ERASURE_EXECUTE required on all execution calls
✅ All erasure API endpoints behind WAF and rate limiting
✅ Erasure audit records immutable and append-only
✅ No erasure ticket can be deleted once created (status changes only)
✅ PII scanner confirms erasure completion (Phase 5)
✅ Suppression list prevents re-registration abuse (email hash only)
✅ Minor protection: parental verification required for minor accounts
✅ Cooling-off period: 14-day cancellation window for FULL_ERASURE
✅ Anti-abuse detection: pattern of request-cancel-request triggers review
✅ Service mesh mutual TLS on all erasure pipeline communications
✅ Erasure job isolation: one erasure job cannot access another user's data
```

---

## 🔒 SECTION 16 — AUDIT & TRACEABILITY

Every erasure operation must produce a permanent, immutable audit record containing NO PII values:

```json
{
  "audit_record_id": "UUID (permanent)",
  "erasure_ticket_id": "UUID",
  "request_id": "UUID",
  "user_reference": "ANON_{ticket_id_hash} (not actual user_id or email)",
  "tenant_id": "UUID",
  "request_type": "FULL_ERASURE | PARTIAL_ERASURE | ANONYMIZATION_ONLY",
  "request_channel": "string",
  "request_timestamp_utc": "ISO 8601",
  "completion_timestamp_utc": "ISO 8601",
  "phase_completion_timestamps": {
    "phase_0_utc": "ISO 8601",
    "phase_1_utc": "ISO 8601",
    "phase_2_utc": "ISO 8601",
    "phase_3_utc": "ISO 8601",
    "phase_4_utc": "ISO 8601",
    "phase_5_utc": "ISO 8601",
    "phase_6_utc": "ISO 8601"
  },
  "services_processed": ["list of service names"],
  "data_categories_erased": ["list of category names (no values)"],
  "data_categories_anonymized": ["list of category names"],
  "data_categories_retained": [
    {"category": "name", "legal_basis": "LR-CODE", "retention_period": "duration"}
  ],
  "legal_hold_scan_result": "CLEAR | PARTIAL_HOLD | FULL_HOLD",
  "third_party_notifications_sent": ["list of third-party names (no PII)"],
  "verification_sweep_result": "PASS | GAPS_FOUND_AND_REMEDIATED",
  "model_version": "RTEA-v1.0.0",
  "operator_id_hash": "SHA-256 (if operator-initiated, no plaintext)",
  "compliance_review_flag": true | false,
  "audit_seal": "SHA-256 of full record"
}
```

**Audit records are permanent. Deletion is architecturally impossible. No PII value is stored in the audit record — only structural metadata.**

---

## 🔒 SECTION 17 — FAILURE POLICY

```yaml
Failure Condition                                → Response
─────────────────────────────────────────────────────────────────────────────────────
Identity re-authentication failed               → REJECT + LOG + prompt user to retry
MFA failed for FULL_ERASURE                     → HOLD + LOG + prompt MFA retry (max 3 attempts)
Request rate limit exceeded                     → REJECT + LOG + inform user of limit
Duplicate request_id                            → REJECT silently + LOG
Legal hold scan service unavailable             → HALT at Phase 1 + HOLD ticket + ESCALATE
Outstanding payment detected                    → HOLD + inform user of billing obligation + ESCALATE
Active legal order detected                     → HOLD + notify Compliance Agent + ESCALATE to Governance Board
Phase 3 service erasure fails (single service)  → LOG + RETRY (3x exponential) + HALT if still failing + ESCALATE
Phase 3 partial completion (some services done) → CRITICAL: mark which services completed, HALT, ESCALATE — never leave inconsistent state without logging
PII scanner detects orphaned data (Phase 5)    → Return to Phase 3 for remediation → Re-run Phase 5
Third-party notification failure               → LOG + RETRY (3x) + ESCALATE to Compliance if still failing
Data corruption detected during erasure        → HALT all phases + ESCALATE to DATA_INTEGRITY_TEAM + EMERGENCY_PLATFORM_LOCKDOWN_AGENT signal if severe
Confirmation delivery failure (Phase 6)        → RETRY email (3x) + LOG + keep ticket COMPLETED (erasure happened even if confirmation delayed)

RETRY_POLICY:
  - Service erasure failures: 3 retries, exponential backoff (1min, 5min, 15min)
  - Third-party notifications: 3 retries over 24 hours
  - Confirmation delivery: 3 retries over 1 hour
  - Legal scan service: No retry — human intervention required

ESCALATE_TO:
  - L1: PLATFORM_ADMIN
  - L2: COMPLIANCE_AGENT + Compliance Team
  - L3: GOVERNANCE_BOARD
  - L4: Legal Team (for court orders, GDPR Article 33 triggers)

CONSISTENCY_GUARANTEE:
  - Erasure is atomic per service (either fully executed or not started)
  - Cross-service consistency is eventual (max 72 hours window)
  - Inconsistent state is NEVER silent — always logged and escalated
```

---

## 🔒 SECTION 18 — USER-FACING EXPERIENCE (Flutter + React)

Aligned with Ecoskiller's Flutter-first UI with React SEO clone.

### Flutter App — Account Settings Flow
```yaml
ENTRY_POINT: Settings → Privacy → Manage My Data → Delete Account

STEP 1: Information Screen
  - Plain language explanation of what will happen
  - List of what will be deleted
  - List of what will be retained (with legal basis)
  - Retention periods clearly stated
  - Download My Data option prominently offered BEFORE deletion
  - 14-day cooling-off period explained

STEP 2: Identity Re-verification
  - Password re-entry required
  - MFA confirmation (for FULL_ERASURE)
  - Biometric option where available

STEP 3: Explicit Acknowledgment (Multi-Step)
  - "I understand my account will be permanently deleted"
  - "I understand the following data will be retained for legal purposes: [list]"
  - "I have downloaded my data or chosen not to"
  - Each acknowledgment is a separate tap/checkbox — no single "I agree to all"

STEP 4: Cooling-Off Period Display
  - "Your deletion request has been received. You have 14 days to cancel."
  - Cancel button prominently displayed
  - Countdown timer showing when cancellation window closes

STEP 5: Confirmation & Ticket Reference
  - Ticket reference code displayed
  - Estimated completion time shown
  - "We'll email you when complete"

UI_RULES:
  - Destructive action button: RED, requires deliberate tap (not accidental)
  - No dark patterns (no "Are you sure? Cancel → Go Back → Delete" flows)
  - Cancel option always visible during cooling-off period
  - Accessibility: All steps keyboard-navigable, screen-reader labeled
```

### React Web — Privacy Rights Page
```yaml
URL: /privacy/my-data
RENDERING: SSR (not SSG — requires session auth)
NOINDEX: true (authenticated page — Flutter noindex enforcement)
CONTENT:
  - Data erasure request form
  - Partial erasure category selector
  - Erasure ticket status tracker
  - Download My Data button
  - Link to privacy policy
  - Contact DPO (Data Protection Officer) link
```

---

## 🔒 SECTION 19 — REGULATORY COMPLIANCE ALIGNMENT

### GDPR (EU General Data Protection Regulation)

```yaml
ARTICLE_17:     Right to Erasure ("right to be forgotten") — PRIMARY LEGAL OBLIGATION
ARTICLE_17_3:   Exceptions to erasure (legal obligation, legal claims, public interest) — mapped in Section 6
ARTICLE_12:     30-day response obligation — enforced via SLA in Section 7
ARTICLE_13_14:  Informed of erasure rights at collection — Privacy Policy must reference this agent's capabilities
ARTICLE_19:     Third-party notification obligation — Phase 4 of execution
ARTICLE_30:     Processing register must include erasure processing activities
RECITAL_65:     Right to erasure does not require deletion of audit logs if required for legal compliance — justifies Section 5K retention
DATA_PORTABILITY (Article_20): Download My Data offered before erasure (proactive implementation)
DPO_ESCALATION: Requests involving legal holds auto-escalate to DPO notification channel
BREACH_TRIGGER: If erasure reveals previously undetected unauthorized data processing → Article 33 notification process triggered
```

### India DPDP Act 2023

```yaml
SECTION_12:     Right to erasure of personal data — PRIMARY LEGAL OBLIGATION
SECTION_14:     Right to grievance redressal — ticket system satisfies this
SECTION_8_7:    Data Fiduciary obligation to erase data upon purpose fulfillment
CONSENT_WITHDRAWAL: Withdrawal of consent triggers erasure evaluation
GUARDIAN_CONSENT: For data principals below 18 — parent/guardian rights enforced (Section 9)
DPB_ESCALATION: Data Protection Board notification process available if erasure is refused
```

### Response Time Obligations

```yaml
GDPR:    30 calendar days from request receipt (extendable to 90 days for complex requests with notification)
DPDP:    As soon as reasonably practicable (platform target: 30 days, operational target: 72 hours)
COMPLEX_EXTENSION: If 30-day extension needed → notify user within 30 days with reason
OPERATOR_REFUSAL: If erasure refused (legal hold) → notify user within 30 days with reason and right to appeal
```

---

## 🔒 SECTION 20 — INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_TRIGGERING:
  - USER (Flutter/React → direct request)
  - COMPLIANCE_AGENT (regulatory trigger)
  - GOVERNANCE_BOARD_REGISTRY (legal order)
  - ADMIN_OPERATIONS_CONSOLE (operator-initiated)
  - PARENT_APP (minor account)

QUERIED_DURING_LEGAL_SCAN (Phase 1):
  - BILLING_SERVICE
  - COMPLIANCE_AGENT
  - GOVERNANCE_BOARD_REGISTRY
  - COLLUSION_DETECTION_AGENT
  - DOJO_ENGINE
  - CERTIFICATION_ENGINE
  - SOCIAL_SERVICE

NOTIFIED_AND_EXECUTED_DURING_PHASE_3:
  - USER_SERVICE, AUTH_SERVICE, SESSION_MANAGER
  - JOB_SERVICE, SKILL_SERVICE, PROJECT_SERVICE
  - DOJO_ENGINE, MATCH_ENGINE, SCORING_ENGINE
  - BELT_ENGINE, CERTIFICATION_ENGINE, REPLAY_ENGINE
  - ANALYTICS_SERVICE, INTELLIGENCE_ENGINE
  - FEATURE_STORE_AGENT, NOTIFICATION_SERVICE
  - BILLING_SERVICE, SOCIAL_SERVICE
  - MEDIA_SERVICE (MinIO), SEARCH_INDEX_SERVICE (OpenSearch)

RECEIVES_NOTIFICATION_ONLY:
  - AUDIT_TRAIL_AGENT (anonymization, not deletion)
  - PUBLIC_TRANSPARENCY_LOG_AGENT (aggregate count only)
  - IDEA_DNA_AGENT, ROYALTY_ENGINE

EVENTS_EMITTED:
  - ERASURE_REQUEST_RECEIVED
  - LEGAL_HOLD_DETECTED
  - ACCOUNT_FROZEN
  - ERASURE_PHASE_{N}_COMPLETE
  - ERASURE_COMPLETED
  - THIRD_PARTY_NOTIFICATION_SENT
  - VERIFICATION_SWEEP_COMPLETE
  - ERASURE_CONFIRMATION_SENT
```

---

## 🔒 SECTION 21 — VERSIONING POLICY

```yaml
ALL CHANGES:
  - Add-only
  - Version-bumped
  - Compliance team sign-off required
  - Legal team review required for retention exception changes
  - Governance board approval required
  - Backward compatible (existing tickets not affected by new version)
  - Append-only changelog mandatory

VERSION FORMAT: RTEA-vMAJOR.MINOR.PATCH-SEALED
CURRENT VERSION: RTEA-v1.0.0-SEALED
DATA_INVENTORY_MAP CHANGES: Require compliance + legal + governance approval
RETENTION_EXCEPTION CHANGES: Require legal team + governance board sign-off
PHASE_EXECUTION CHANGES: Require compliance + technical architecture review + governance approval
```

---

## 🔒 SECTION 22 — PERFORMANCE MONITORING

```yaml
METRICS:
  - erasure_requests_received_count (daily/monthly)
  - erasure_completion_rate (target: 100%)
  - erasure_within_72h_rate (target: > 80%)
  - erasure_within_30d_rate (target: 100%)
  - legal_hold_rate (percentage of requests with holds)
  - phase_3_failure_rate (target: < 0.1%)
  - verification_sweep_pass_rate (target: 100%)
  - third_party_notification_on_time_rate (target: 100%)
  - orphaned_pii_detection_rate (should trend to 0)
  - user_cancellation_rate (within cooling-off period)

OBSERVABILITY_INTEGRATION:
  - All metrics → OBSERVABILITY_AGENT
  - Grafana dashboard: ecoskiller.internal/erasure-monitor
  - Prometheus metrics exported
  - SLA breach alert → CRITICAL

ALERTING:
  - Erasure SLA at risk (25 days elapsed, not complete) → CRITICAL alert to Compliance Team
  - Phase 3 failure → WARN alert
  - Legal hold scan service unavailable → CRITICAL alert
  - Orphaned PII detected in sweep → CRITICAL alert
  - Bulk erasure initiated → NOTIFY Governance Board
  - Regulatory deadline at risk → CRITICAL alert to Legal Team
```

---

## 🔒 SECTION 23 — NON-NEGOTIABLE RULES

This agent must NOT:

```
✗ Execute erasure without verified identity re-authentication
✗ Execute FULL_ERASURE without MFA confirmation
✗ Skip the legal hold scan under any condition
✗ Execute erasure while an active legal hold is unresolved
✗ Delete audit log records (anonymize only — audit logs are permanent)
✗ Delete billing records that are under financial/tax retention obligation
✗ Delete certification metadata that is under third-party verification obligation
✗ Leave orphaned PII across services (verification sweep is mandatory)
✗ Complete erasure without user confirmation notice
✗ Execute operator-initiated erasure without dual-operator authorization
✗ Execute bulk erasure without governance board pre-approval (> 10 users)
✗ Cross tenant boundaries in erasure scope
✗ Allow AI to determine legal hold decisions autonomously
✗ Recover or restore erased data (erasure is permanent and irreversible)
✗ Store PII values in erasure audit records
✗ Bypass the cooling-off period for FULL_ERASURE
✗ Execute without logging every phase completion to immutable audit store
✗ Allow silent partial erasure (all gaps must be logged and escalated)
✗ Skip third-party notification obligations (Article 19 GDPR)
✗ Use dark UI patterns in the erasure request flow
✗ Refuse an erasure request without providing written reason and appeal path
```

---

## 🔒 SECTION 24 — FINAL GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║          RIGHT_TO_ERASURE_AGENT — GOVERNANCE SEAL                    ║
╠══════════════════════════════════════════════════════════════════════╣
║ Version:               RTEA-v1.0.0-SEALED                            ║
║ Seal Date:             2026-02-28T00:00:00Z                          ║
║ Execution Mode:        DETERMINISTIC + VALIDATED + IRREVERSIBLE      ║
║ Mutation Policy:       ADD-ONLY VIA SIGNED VERSION BUMP              ║
║ Interpretation:        FORBIDDEN                                     ║
║ Default Behavior:      DENY (whitelist-evaluation model)             ║
║ Failure Mode:          HALT + LOG + ESCALATE (no partial erasure)    ║
║ Audit Policy:          PERMANENT IMMUTABLE (no PII in audit record)  ║
║ Erasure Reversibility: NONE — erasure is permanent                   ║
║ Legal Hold Authority:  COMPLIANCE AGENT + LEGAL TEAM (human gate)    ║
║ MFA Requirement:       MANDATORY for FULL_ERASURE                    ║
║ Cooling-Off Period:    14 days (cancellable before Phase 3)          ║
║ Tenant Scope:          STRICT ISOLATION (operator cannot cross)      ║
║ Audit Log Policy:      ANONYMIZE, NEVER DELETE                       ║
║ Certification Policy:  ANONYMIZE, NEVER DELETE (LR-CERT, 7 years)   ║
║ Billing Policy:        ANONYMIZE, NEVER DELETE (LR-TAX, 7 years)    ║
║ Minor Protection:      PARENT VERIFICATION REQUIRED                  ║
║ Third-Party Notice:    GDPR Article 19 ENFORCED                      ║
║ SLA:                   30 days MAX (GDPR Article 12 compliance)       ║
║ Operational Target:    72 hours                                      ║
║ GDPR Alignment:        ARTICLE 17 + 12 + 19 + RECITAL 65 ENFORCED   ║
║ DPDP Alignment:        SECTION 12 + 14 + 8(7) ENFORCED              ║
╠══════════════════════════════════════════════════════════════════════╣
║   ECOSKILLER ANTIGRAVITY — RIGHT TO ERASURE MODE ENABLED             ║
║   USER PRIVACY: PROTECTED                                            ║
║   LEGAL OBLIGATIONS: HONORED                                         ║
║   AUDIT TRAIL: PERMANENT & ANONYMIZED                                ║
║   CERTIFICATION INTEGRITY: PRESERVED                                 ║
║   FINANCIAL RECORDS: LEGALLY RETAINED                                ║
║   MINOR PROTECTION: ACTIVE                                           ║
║   THIRD-PARTY OBLIGATIONS: ENFORCED                                  ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*This document is a sealed production artifact for the Ecoskiller Antigravity platform. Any modification without a formal version bump, compliance team sign-off, legal team review, and governance board approval, followed by an append-only changelog entry, constitutes a regulatory compliance violation and creates direct legal liability for the platform.*

**END OF SEALED DOCUMENT — RTEA-v1.0.0**
