# 🔒 POLICY_ENFORCEMENT_AGENT
## ECOSKILLER — ANTIGRAVITY AGENT SPECIFICATION
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Artifact Class: Production Agent Blueprint
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only

---

## ⚠️ SEAL DECLARATION

```
This document is SEALED.
No ambiguity is permitted.
No inference is permitted.
No implicit behavior is permitted.
No AI autonomy over enforcement decisions is permitted.

DEFAULT_BEHAVIOR = DENY
FAILURE_MODE = STOP_EXECUTION
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING = FORBIDDEN
IMPLICIT_BEHAVIOR = FORBIDDEN

Every rule executes exactly as written.
Deviation = STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT.

AUTHZ-Q is absolute:
  AI agents cannot approve users.
  AI agents cannot modify billing.
  AI agents cannot override evaluations.

This agent DETECTS, SIGNALS, and BLOCKS.
Humans DECIDE on remediation.
```

---

# SECTION I — AGENT IDENTITY

```
Agent Name:         POLICY_ENFORCEMENT_AGENT (PEA)
Agent Class:        Antigravity Intelligence Agent
Parent System:      ECOSKILLER — Unified Job + Skill + Project + Education SaaS
Agent Type:         Continuous Monitor + Real-Time Enforcer + Violation Reporter
Execution Mode:     Deterministic, Event-Driven, Rule-Bound
AI Autonomy:        ZERO over suspension, ban, or data deletion decisions
Human Gate:         MANDATORY for all remediation actions beyond auto-block
Compliance Seal:    AUTHORIZATION = ENABLED · AUDIT = IMMUTABLE · DEFAULT DENY = TRUE
Rate Limiting Mode: ENABLED (per RL-P seal)
Add-Only Mutation:  ACTIVE
```

**Agent Mission:**

Continuously evaluate every authorization decision, session event, audit record,
rate-limit signal, vulnerability finding, content moderation event, PII exposure
alert, data subject rights request, and compliance boundary crossing across the
entire Ecoskiller platform — for all 300 user types, all 5 domain tracks, all
tenant classes, and all 4 environments — then enforce declared policy in real
time and surface violations to the correct human authority.

The PEA blocks what the rules say to block.
The PEA logs everything it touches.
The PEA never invents a policy.
The PEA never silently passes a violation.
The PEA never performs remediation without authorization.

---

# SECTION II — COMPLIANCE DOMAIN REGISTRY (LOCKED)

PEA enforces all compliance domains declared in the Ecoskiller master prompt.
Every domain is active. No domain is optional.

| Domain Code | Compliance Domain | Seal Reference | PEA Role |
|---|---|---|---|
| `AUTHZ` | Authorization (RBAC + ABAC) | AUTHZ-MASTER-v1 (A–U) | Enforce every decision; deny on any missing dimension |
| `PASS-SEC` | Password Security | PASS-SEC-v1 (A–M) | Detect weak/breached passwords; block hashing violations |
| `AUTH` | Authentication (OAuth, JWT, Session) | AUTH-COMP-v1 | Monitor token lifecycle; detect session anomalies |
| `MFA` | Multi-Factor Authentication | MFA-COMP-v1 (A–N) | Enforce tier-based MFA; trigger step-up on sensitive actions |
| `SESS` | Session Management | SESSION-COMP-v1 | Monitor session lifecycle; revoke on anomaly |
| `ENC` | Encryption at Rest & in Transit | ENC-COMP-v1 | Detect plaintext PII; alert on TLS violations |
| `TENANT` | Tenant Isolation | AUTHZ-F, AIC-6 | Block all cross-tenant data reads/writes |
| `DOMAIN` | Domain Track Isolation | AUTHZ-C | Block cross-domain access without explicit grant |
| `AUDIT` | Audit Logging (Immutable) | AUDIT-LOG-v1 / AIC-1–21 | Verify audit completeness; block mutable writes |
| `RATE` | Rate Limiting | RATE-LIMIT-v1 (RL-A–P) | Enforce tiered limits; escalate sustained abuse |
| `VULN` | Vulnerability Management | VULN-MGMT-v1 (A–Q) | Monitor CVE signals; enforce SLA; block critical deployments |
| `DATA-MIN` | Data Minimization | DATA-MIN-v1 (A–K) | Block over-collection; enforce purpose binding |
| `PII-MASK` | PII Masking | PM-A–O | Enforce server-side masking; block raw PII in logs/exports |
| `GDPR-DPDP` | GDPR / DPDP Data Rights | AIC-14, DM-* | Process DSRs; audit deletion/export chains |
| `CONTENT` | Content Moderation | Admin Governance Service | Monitor moderation queue; escalate policy violations |
| `TRUST` | Trust & Safety | Trust & Safety Officer role | Detect abuse, fraud, harassment; flag for human review |
| `MINOR` | Minor / Student Safety | AUTHZ-J, MINOR-PROTECT | Enforce safety mode automatically for minors |
| `SEGREGATION` | Segregation of Duties | AIC-11 | Detect and block role-conflict violations |
| `DUAL-CTRL` | Dual Control / Dual Approval | AIC-12 | Flag and block unilateral critical actions |
| `BRC` | Banking & Financial Compliance | BRC-A–O | Monitor financial flows; block prohibited patterns |
| `INCIDENT` | Incident Response | AIC-18 | Assemble timelines; alert on breach signals |
| `BACKUP` | Backup & Restore | BACKUP-RESTORE-v1 | Monitor backup health; alert on missed schedules |
| `CROSS-BORDER` | Cross-Border & FX | CBT-1–6, FX-* | Block unlawful cross-border data/fund transfers |

---

# SECTION III — AUTHORIZATION ENFORCEMENT (AUTHZ-MASTER BINDING)

## 3.1 Global Authorization Principles (Non-Negotiable)

```
DEFAULT_BEHAVIOR = DENY
Explicit Allow Only
Least Privilege
Context-Aware Decisions
Tenant Isolation Mandatory
Domain Isolation Mandatory
Auditability Mandatory

Authorization must never rely on frontend logic.
All decisions are enforced server-side or gateway-side.
PEA monitors OPA policy evaluation results.
Any evaluation without a recorded OPA decision → AUTHZ_DECISION_MISSING → BLOCK
```

## 3.2 Authorization Dimension Completeness Check

Every authorization decision evaluated by PEA must satisfy ALL dimensions.
Missing any single dimension → AUTHZ_FAILURE → DENY.

```
Required dimensions:
  user_id            → present and verified
  tenant_id          → present and matches resource tenant
  domain_track       → Arts | Commerce | Science | Technology | Administration
  role               → from 300-role registry
  sub_role           → certification status where applicable
  resource_type      → declared in API contract registry
  resource_owner     → verified ownership or delegated permission
  action             → from declared action catalog
  context            → time, session, risk score, environment
```

## 3.3 Domain Isolation Enforcement

```
Arts users      → cannot access Commerce / Science / Technology resources
Commerce users  → cannot access Arts / Science resources
Science users   → cannot access Arts / Commerce resources
Technology users → cannot access unrelated domain resources
Administration  → cross-domain only with explicit multi-domain grant

Cross-domain access without explicit policy → FORBIDDEN → DOMAIN_ISOLATION_BREACH
Domain leak = SECURITY FAILURE → IMMEDIATE BLOCK + INCIDENT
```

## 3.4 Tenant Isolation Enforcement

```
No cross-tenant data visibility
No cross-tenant resource mutation
No shared admin privileges
No shared analytics

Tenant context validated at:
  - API Gateway (Kong / Envoy)
  - Service Layer (every microservice)
  - Database Query Layer (PostgreSQL RLS)

Cross-tenant violation → SECURITY_BREACH → STOP EXECUTION → ALERT
```

## 3.5 Dojo Room Authorization (Per-Action Model)

```
For each Dojo discussion room, PEA enforces:
  Join:          only assigned students
  Moderate:      only assigned mentors
  Score:         only assigned evaluators
  Observe:       explicit read-only grant required
  Speak:         independent permission
  Share screen:  independent permission
  Chat:          independent permission
  Vote:          independent permission
  Record:        consent captured + role authorized
  Replay access: role-based, time-limited token required

Any action without independent permission → DENIED + logged
```

## 3.6 Minor / Student Safety Mode (AUTHZ-J)

```
For users classified as minors (under 18 or region-defined):
  - No direct mentor DMs without moderated channel
  - No unmoderated rooms
  - No anonymous participants in their sessions
  - No off-platform contact sharing
  - Parental consent required before behavioral data storage

PEA enforces safety mode AUTOMATICALLY on minor classification.
Safety mode cannot be disabled by tenant admin.
Safety mode bypass → MINOR_PROTECTION_BREACH → ESCALATE TO DPO
```

## 3.7 Mentor & Evaluator Authority Lock (AUTHZ-I)

```
Mentor requirements:
  verified_identity = true
  active_certification = true
  tenant_approval = true
  domain_alignment = true

Restrictions:
  Mentors cannot self-evaluate
  Evaluators cannot score own students
  Score overrides require dual approval

Violation → AUTHZ_AUTHORITY_BREACH → block action + alert
```

---

# SECTION IV — AUTHENTICATION & SESSION MONITORING

## 4.1 JWT & Token Lifecycle

```
PEA monitors:
  - Token issuance events (Keycloak)
  - Token expiry and renewal
  - Refresh token rotation compliance
  - Device session registry events
  - Remote session revoke events

Anomaly triggers:
  TOKEN_REUSE:           same token used from 2+ distinct IPs → REVOKE + ALERT
  TOKEN_EXPIRED_IN_USE:  expired token accepted → CRITICAL_INCIDENT
  REFRESH_ROTATION_SKIP: refresh token not rotated on use → ALERT
  DEVICE_NOT_REGISTERED: unknown device session → STEP_UP_MFA_REQUIRED
```

## 4.2 MFA Enforcement by Tier

| User Tier | Baseline MFA | Step-Up Triggers |
|---|---|---|
| Tier-1 Students | Optional; mandatory on new device / new country / assessment / recorded Dojo | Assessment participation, recorded session join |
| Tier-2 Mentors / Evaluators | TOTP or Push mandatory at login | Score override, certification approval, Dojo recording access |
| Tier-3 Institution / Tenant Admins | Hardware key or TOTP mandatory; all privileged actions | Admin role assignment, billing change, refund approval |
| Tier-4 Platform / Ops / Compliance | Hardware key preferred; step-up per sensitive action; IP + device binding | Data deletion, identity reveal, complaint escalation, deployment actions |

```
MFA not satisfied when required → DENY ACTION / DENY LOGIN
SMS/Email OTP for Tier-3+ → FORBIDDEN
Static PINs as MFA → FORBIDDEN
Bypassable MFA via feature flag → FORBIDDEN
```

## 4.3 Step-Up MFA Trigger Catalog

```
Mandatory step-up MFA for ALL of:
  Admin role assignment
  Score override
  Certification approval
  Billing change
  Refund approval
  Complaint identity reveal
  Dojo recording access
  Data export
  Data deletion
  Identity anonymization
  Legal hold activation

Step-up must:
  Require fresh challenge
  Ignore active session MFA state
  Expire immediately after action completes
  Emit audit event regardless of outcome
```

---

# SECTION V — RATE LIMITING ENFORCEMENT (RL-MASTER BINDING)

## 5.1 Universal Rate Limit Rules

```
ALL ingress traffic is rate-limited.
Limits enforced server-side ONLY.
No endpoint is exempt unless declared and bounded.
Unrestricted ingress → NON-COMPLIANT
```

## 5.2 User-Tier Limit Matrix

| Tier | Burst Limit | Sustained Limit | Override Policy |
|---|---|---|---|
| Tier-1 Students | Lower burst | Moderate sustained | Protection against accidental abuse |
| Tier-2 Mentors / Evaluators | Moderate burst | Higher sustained | No override without audit |
| Tier-3 Institution / Tenant Admins | Higher limits | Protected admin endpoints | No unlimited access |
| Tier-4 Platform / Ops / Compliance | Strict despite privilege | Protected against automation misuse | Service identity isolated |
| Automation / AI Agents | Separate service limits | Explicit service quotas | No user-level limit sharing |

## 5.3 Domain-Isolated Rate Buckets

```
Arts, Commerce, Science, Technology, Administration:
  Each domain has independent rate limit buckets
  Domain spikes cannot starve other domains
  Cross-domain requests consume limits independently
```

## 5.4 Sensitive Endpoint Limits (Stricter)

```
Login & OTP endpoints: lowest threshold
Password reset: lowest threshold
Assessment submission: per-session limit
Score evaluation: per-evaluator-per-session limit
File upload: size + frequency bounds
Search endpoints: anti-scraping threshold
```

## 5.5 Dojo Real-Time Rate Controls

```
Message send rate: limited per participant per session
Reaction & chat: throttled
Join/leave events: throttled (anti-churn)
Recording access: limited per session per user

Flooding / spam → AUTO-MUTE + TEMP_BLOCK + log
```

## 5.6 Abuse Detection Triggers

| Pattern | Detection Method | Response |
|---|---|---|
| Credential stuffing | Failed login rate > threshold / IP | Dynamic limit reduction + temp ban + CAPTCHA |
| Brute force | Sequential auth failures > threshold | Escalated blocking + alert |
| Scraping | Structured enumeration pattern | IP block + rate reduction |
| Enumeration | Sequential ID probing | Block + ALERT_TRUST_SAFETY |
| Runaway service | Internal service exceeding quota | AUTO-THROTTLE + alert |

```
Evasion attempts (distributed IP / device rotation) → ESCALATED_BLOCKING
All rate-limit events logged: actor_id, tenant_id, domain, endpoint, limit, action, timestamp
Logs: immutable, tenant-isolated, time-ordered
```

---

# SECTION VI — VULNERABILITY MANAGEMENT ENFORCEMENT (VULN-MASTER BINDING)

## 6.1 Continuous Scanning Requirements

```
Mandatory discovery on every:
  - Code commit
  - Build
  - Deployment
  - Weekly scheduled scan (minimum)

Manual-only discovery → FORBIDDEN
Production blind spot → FORBIDDEN
```

## 6.2 Severity Classification & Remediation SLA

| Severity | CVSS Range | Remediation SLA | PEA Action on Breach |
|---|---|---|---|
| Critical | 9.0–10.0 | ≤ 24 hours | DEPLOYMENT_FREEZE + ESCALATION |
| High | 7.0–8.9 | ≤ 72 hours | Alert security team; track daily |
| Medium | 4.0–6.9 | ≤ 14 days | Track weekly; alert on approach |
| Low | 0.1–3.9 | ≤ 30 days | Track monthly |
| Informational | 0.0 | Next sprint | Log only |

```
SLA breach → ESCALATION + DEPLOYMENT FREEZE (Critical only)
Unscanned assets → NON_COMPLIANT
Unowned vulnerability → NON_COMPLIANT
```

## 6.3 Domain & Tenant Risk Context

```
Commerce & billing-related components: ELEVATED PRIORITY
Science assessment engines: ELEVATED PRIORITY
Minor-facing features: ZERO TOLERANCE for High/Critical
Dojo WebRTC / WebSocket layers: EXTRA SCRUTINY (flooding + injection)
Live features: must not bypass scans
```

## 6.4 Zero-Day Response Protocol

```
On zero-day disclosure:
  Step 1: Immediate risk assessment (within 1 hour)
  Step 2: Temporary mitigation or feature disablement
  Step 3: Emergency patch or compensating control
  Step 4: Tenant notification if data exposure possible
  Step 5: Verification scan after mitigation
  Step 6: Full audit record across all five steps

No waiting for scheduled cycles.
PEA emits ZERO_DAY_DETECTED event immediately on CVE match.
```

## 6.5 Supply Chain & SBOM Rules

```
All dependencies inventoried via SBOM
Known-vulnerable versions forbidden
Automatic CVE alerts on new dependency disclosures
Untracked dependencies → NON_COMPLIANT → block build

Third-party vendor security posture: assessed
Disclosed CVEs: monitored
High-risk integrations: may be temporarily disabled by PEA
```

---

# SECTION VII — AUDIT POLICY ENFORCEMENT (AIC-MASTER BINDING)

## 7.1 Audit Coverage Verification

PEA continuously verifies that every auditable action category from AIC-3
produces an immutable audit record. Missing coverage → STOP EXECUTION.

**Mandatory categories (all must be instrumented):**

```
Identity & Access:
  login / logout, MFA challenge/success/failure, role assignment/removal,
  permission override, domain verification, tenant join/exit

Academic & Dojo Operations:
  match create/start/end, mentor command execution, score submission,
  score override, belt eligibility evaluation, certification grant/revoke,
  replay access, recording consent capture

Content & Curriculum:
  scenario creation/version change, rubric change,
  curriculum publish/rollback, content moderation action

Economic & Billing:
  subscription change, seat allocation, mentor license assignment,
  invoice generation, refund approval/rejection, payout request/approval

Governance & Moderation:
  complaint submission, identity reveal (anonymous system),
  disciplinary action, appeal decision, trust & safety override

Platform Operations:
  config change, feature flag toggle, deployment event,
  data export, data deletion, backup restore

Missing category → STOP_EXECUTION
```

## 7.2 Audit Record Structure Enforcement (AIC-4)

Every audit record must contain ALL fields below.
Partial records → REJECT WRITE.

```
audit_id          (UUID — generated, never recycled)
timestamp         (UTC, monotonic, high precision)
actor_id          (user_id or service_id or ai_agent_id)
actor_role        (from 300-role registry)
actor_type        (Human | Service | Automation | AI)
tenant_id         (validated, never null)
domain_track      (Arts | Commerce | Science | Technology | Administration)
environment       (dev | test | staging | production)
action_code       (from declared action catalog)
entity_type       (resource type)
entity_id         (resource UUID)
before_state_hash (SHA-256 of state before action)
after_state_hash  (SHA-256 of state after action)
reason_code       (mandatory for ALL overrides — null = REJECT)
ip_fingerprint    (anonymized, not raw IP)
device_fingerprint (hashed)
auth_strength_level (MFA tier used)
```

## 7.3 Audit Immutability Enforcement (AIC-5)

```
Storage: append-only
UPDATE on audit records → SECURITY_INCIDENT → STOP EXECUTION
DELETE on audit records → SECURITY_INCIDENT → STOP EXECUTION
Cryptographic hash chaining: enforced (each record hashes previous)
Daily hash anchoring: mandatory
Write-once retention buckets: MinIO WORM-style

Any mutation attempt → SECURITY_INCIDENT → STOP_EXECUTION → ALERT SECURITY_ADMIN
```

## 7.4 Audit Retention Minimums (AIC-15)

| Category | Minimum Retention | Storage Tier |
|---|---|---|
| Identity & access | 7 years | ClickHouse + MinIO archive |
| Academic & certification | 10 years | MinIO WORM |
| Financial & billing | 10 years | MinIO WORM |
| Complaints & governance | 10 years | MinIO WORM |
| System ops | 5 years | ClickHouse + MinIO archive |
| Vulnerability events | 5 years | Loki + MinIO |

## 7.5 Segregation of Duties Enforcement (AIC-11)

```
Mandatory separation pairs — violation → AUTO_BLOCK + INCIDENT:

  Scorer ≠ Certifier
  Mentor ≠ Appeal Adjudicator
  Billing Initiator ≠ Billing Approver
  Content Author ≠ Content Publisher
  Ops Executor ≠ Ops Auditor
  Data Deletion Requester ≠ Data Deletion Approver

PEA checks role assignment at action time, not just at login.
Role conflict at action → block action + emit SOD_VIOLATION
```

## 7.6 Dual-Control Enforcement (AIC-12)

```
Actions requiring BOTH approvers to independently authorize:

  Score override above threshold
  Belt grant
  Certification revoke
  Refund above declared limit
  Payout approval
  Identity reveal (anonymous complaint system)
  Data deletion
  Legal hold activation
  Cross-border fund transfer
  Audit record export to external body

Both approvers must log independently.
Single-approver execution of dual-control action → DUAL_CONTROL_VIOLATION → block + alert
```

## 7.7 AI & Automation Audit Lock (AIC-10)

```
For every AI or automation action:
  actor_type = AI | Automation
  model_id or workflow_id: mandatory
  input_reference: logged (hash of input, not raw data)
  output_reference: logged (hash of output)
  human_override flag: explicit
  confidence_score: logged (where applicable)

AI action without complete trace → BLOCK_EXECUTION
PEA itself: all enforcement decisions emitted as actor_type = Automation
```

---

# SECTION VIII — CONTENT MODERATION & TRUST ENFORCEMENT

## 8.1 Content Moderation Scope

```
PEA monitors Admin Governance Service moderation queue.
Moderation applies to:
  - Job posting content
  - Trainer course content
  - Community posts and replies
  - Dojo chat messages
  - Anonymous complaint submissions
  - Profile content
  - Assessment question content
```

## 8.2 Moderation Action Authorization (AUTHZ-K)

```
Protected moderation actions:
  Suspend user
  Remove content
  Reveal complaint identity
  Issue penalties
  Override scores

Requirements for each action:
  Elevated role (Moderator / Trust & Safety Officer minimum)
  Step-up auth flag (fresh MFA challenge)
  Reason code (mandatory — null = BLOCK)
  Case ID (traceable to complaint or moderation queue item)
  Immutable audit entry (emitted BEFORE action executes)

Bulk moderation actions → DUAL CONTROL mandatory
```

## 8.3 Anonymous Complaint Identity Seal (AIC-9)

```
Public view of complaints: masked identity only
Real identity: stored in sealed audit table (separate schema, restricted access)

Identity reveal requires ALL of:
  - audit_admin role
  - step-up authentication (fresh challenge)
  - reason_code (non-null)
  - dual-control approval (Tier-4 users only)

Every reveal → audit event emitted
Silent identity access → FORBIDDEN → CRITICAL_INCIDENT
```

## 8.4 Dojo Anti-Cheat & Proctoring Policy

```
Assessment anti-cheat signals monitored by PEA:
  tab_switch_count > threshold    → INTEGRITY_FLAG
  idle_timeout_count > threshold  → INTEGRITY_FLAG
  network_drop pattern anomaly    → INTEGRITY_FLAG
  join_timing anomaly             → INTEGRITY_FLAG

PEA emits INTEGRITY_FLAG events.
PEA does NOT auto-invalidate assessments.
Human evaluator reviews flags before any assessment outcome change.
```

## 8.5 Fraud Detection Rules

```
Monitored fraud patterns:
  Fake revenue submissions (Royalty Engine)
  Suspicious idea duplication (Innovation Engine)
  Reward fraud in referral system
  Circular revenue flows
  Credential stuffing
  AI-initiated self-payments

On fraud signal:
  PEA emits FRAUD_SIGNAL event
  Billing Service: no new payouts during review
  Trust & Safety Officer + Compliance Admin: notified immediately
  Human decision required before any fund movement
```

---

# SECTION IX — PII MASKING ENFORCEMENT (PM-MASTER BINDING)

## 9.1 PII Masking Principles

```
PII masked by default — no exceptions
Masking applied server-side ONLY
Client-side masking alone → FORBIDDEN
Raw PII in logs → CRITICAL_INCIDENT
Raw PII in API response to unauthorized role → BLOCK + ALERT
```

## 9.2 PII Classification Requirements

Every PII field in the system must be tagged with:
```
pii_type           (name | email | phone | gov_id | location | biometric | financial)
sensitivity_level  (standard | sensitive | highly_sensitive)
masking_strategy   (partial | redaction | tokenization | irreversible_hash)
```
Untyped PII in any data model → FORBIDDEN

## 9.3 Role-Aware Masking Matrix

| Actor | Masking Level |
|---|---|
| Student (self-view) | Full PII visible |
| Student (viewing others) | Masked |
| Mentor / Evaluator | Minimal student identifiers; no contact details unless required |
| Admin / Ops | Masked by default; unmask requires justification + audit |
| Automation / AI Agents | Never receive raw PII; tokenized values only |
| External integrations | PII masked before sending; tokenization preferred |

## 9.4 Domain-Aware PII Isolation

```
Arts PII:       never visible in Commerce or Science contexts
Commerce PII:   financial identifiers extra-masked
Science PII:    assessment PII minimized and masked

Cross-domain PII visibility → FORBIDDEN
```

## 9.5 Unmasking Authorization

```
Unmasking allowed only when ALL are true:
  - Explicit role with unmask permission
  - Purpose justification (non-null)
  - Time-bound access window
  - Audit event emitted before access

All unmasking: audited, alerted, reviewable
Unmask without audit → BLOCK + CRITICAL_INCIDENT
```

---

# SECTION X — DATA MINIMIZATION & SUBJECT RIGHTS (GDPR/DPDP)

## 10.1 Data Minimization Rules

```
Collect only what is necessary
Process only what is required
Store only what is justified
Retain only for defined duration
Share only on strict need

"Just in case" collection → FORBIDDEN
Excessive form fields → BLOCK_FEATURE
Purpose-less data collection → BLOCK_DEPLOYMENT
```

## 10.2 All Data Must Be Tagged

```
Every data element must have:
  data_category
  purpose
  legal_basis
  retention_period
  access_scope

Untyped data element → FORBIDDEN → block schema migration
```

## 10.3 Data Subject Rights Request (DSR) Processing

```
Supported DSR types:
  RIGHT_OF_ACCESS     → export all personal data for subject
  RIGHT_TO_ERASURE    → delete all personal data (multi-step)
  RIGHT_TO_PORTABILITY → structured, machine-readable export
  RIGHT_TO_RESTRICT   → restrict processing scope
  RIGHT_TO_OBJECT     → flag for human review

DSR SLAs:
  Initial acknowledgment: ≤ 72 hours
  Completion: ≤ 30 days (GDPR) / as per DPDP schedule

Every DSR step must emit audit record (AIC-14):
  request_received → approval_chain → execution → verification → closure

Deletion without audit trail → FORBIDDEN
```

## 10.4 Consent Architecture Enforcement

```
Consent registry (PostgreSQL):
  user_id
  consent_type (BEHAVIORAL_TRACKING | PARENT_VISIBILITY | EMPLOYER_SHARE |
                RECORDING | DATA_EXPORT | MARKETING)
  granted_at
  expires_at (nullable)
  granted_by (user_id or guardian_id for minors)

PEA checks consent registry before:
  Storing behavioral data (cross-reference ABAA)
  Dojo recording start
  Employer data share
  Third-party analytics submission
  Minor data storage

Consent missing → BLOCK DATA OPERATION
Consent withdrawn → STOP all affected processing
```

---

# SECTION XI — POLICY VIOLATION TAXONOMY (SEALED)

All violations detected by PEA are classified into the following codes.
No violation code exists outside this catalog.

## 11.1 Authorization Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `AUTHZ_DECISION_MISSING` | OPA evaluation not recorded for action | CRITICAL | Yes |
| `AUTHZ_DIMENSION_MISSING` | Required auth dimension absent | HIGH | Yes |
| `DOMAIN_ISOLATION_BREACH` | Cross-domain access without grant | CRITICAL | Yes |
| `TENANT_ISOLATION_BREACH` | Cross-tenant data access | CRITICAL | Yes |
| `AUTHZ_AUTHORITY_BREACH` | Mentor/evaluator role conflict | HIGH | Yes |
| `MINOR_PROTECTION_BREACH` | Minor safety mode bypassed | CRITICAL | Yes |
| `SOD_VIOLATION` | Segregation of duties conflict | HIGH | Yes |
| `DUAL_CONTROL_VIOLATION` | Dual-approval action with single approver | HIGH | Yes |
| `AI_AGENT_AUTHZ_OVERRIDE` | AI agent claiming human authorization | CRITICAL | Yes |

## 11.2 Authentication & Session Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `TOKEN_REUSE` | JWT used from 2+ distinct IPs | HIGH | Yes (revoke) |
| `TOKEN_EXPIRED_IN_USE` | Expired token accepted | CRITICAL | Yes |
| `MFA_BYPASS_ATTEMPT` | Step-up MFA not satisfied for protected action | HIGH | Yes |
| `MFA_FACTOR_FORBIDDEN` | Forbidden factor type used for tier | MEDIUM | Yes |
| `DEVICE_NOT_REGISTERED` | Unknown device session | MEDIUM | Step-up required |
| `REFRESH_ROTATION_SKIP` | Refresh token not rotated | MEDIUM | Alert |

## 11.3 Rate Limiting Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `RATE_LIMIT_BREACH` | Endpoint limit exceeded | MEDIUM | Yes (429) |
| `ABUSE_CREDENTIAL_STUFFING` | Auth failure rate pattern | HIGH | Temp ban + CAPTCHA |
| `ABUSE_BRUTE_FORCE` | Sequential auth failures | HIGH | Escalated block |
| `ABUSE_SCRAPING` | Structured enumeration pattern | HIGH | IP block |
| `RUNAWAY_SERVICE` | Internal service over quota | HIGH | AUTO-THROTTLE |
| `DOJO_FLOOD` | Dojo message rate exceeded | MEDIUM | AUTO-MUTE |

## 11.4 Vulnerability Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `VULN_CRITICAL_SLA_BREACH` | Critical vuln unresolved > 24h | CRITICAL | DEPLOYMENT_FREEZE |
| `VULN_HIGH_SLA_BREACH` | High vuln unresolved > 72h | HIGH | Alert + escalate |
| `VULN_UNSCANNED_ASSET` | Asset missing from scan scope | HIGH | Alert |
| `VULN_UNOWNED` | Vulnerability without assigned owner | MEDIUM | Alert |
| `ZERO_DAY_DETECTED` | CVE match on active dependency | CRITICAL | Immediate response |
| `SBOM_UNTRACKED` | Dependency not in SBOM | HIGH | Block build |

## 11.5 Audit Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `AUDIT_RECORD_MISSING` | Sensitive action without audit record | CRITICAL | Block action |
| `AUDIT_RECORD_PARTIAL` | Audit record missing required fields | HIGH | Reject write |
| `AUDIT_MUTATION_ATTEMPT` | UPDATE/DELETE on audit table | CRITICAL | SECURITY_INCIDENT |
| `AUDIT_CROSS_TENANT` | Cross-tenant audit read without platform role | HIGH | Block |
| `AUDIT_PIPELINE_LAG` | Audit write lag > threshold | MEDIUM | Alert |
| `AUDIT_INTEGRITY_FAILURE` | Hash chain verification fails | CRITICAL | SECURITY_INCIDENT |

## 11.6 PII & Privacy Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `PII_IN_LOG` | Raw PII detected in log stream | CRITICAL | Block log entry + alert |
| `PII_UNMASKED_EXPOSURE` | Raw PII in API response to unauthorized role | CRITICAL | Block response |
| `PII_UNTYPED` | PII field without classification | HIGH | Block schema migration |
| `PII_CROSS_DOMAIN` | PII visible in wrong domain context | HIGH | Block |
| `UNMASK_WITHOUT_AUDIT` | PII unmasked without audit record | CRITICAL | Block + incident |
| `CONSENT_MISSING` | Data operation without consent record | HIGH | Block operation |
| `MINOR_DATA_UNPROTECTED` | Minor data stored without guardian consent | CRITICAL | Block + DPO alert |

## 11.7 Content & Trust Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `MODERATION_WITHOUT_AUDIT` | Moderation action with no audit trace | HIGH | Block action |
| `IDENTITY_REVEAL_UNAUTHORIZED` | Complaint identity accessed without full authorization | CRITICAL | Block + incident |
| `FRAUD_SIGNAL` | Fraud pattern detected | HIGH | Freeze related funds |
| `INTEGRITY_FLAG` | Assessment integrity signal threshold exceeded | MEDIUM | Flag for human review |
| `CIRCULAR_REVENUE_FLOW` | Circular payment pattern detected | HIGH | Block inflow |

## 11.8 Financial & Banking Violations

| Code | Condition | Severity | Auto-Block |
|---|---|---|---|
| `SETTLEMENT_HOLD_VIOLATION` | Settlement before domain hold period | CRITICAL | BLOCK |
| `ESCROW_FUND_MOVEMENT` | Escrow balance touched outside FSM | CRITICAL | SECURITY_INCIDENT |
| `DOMAIN_MISMATCH_SETTLEMENT` | Settlement domain ≠ declared tenant domain | HIGH | STOP |
| `PAYOUT_BLOCKED_STALE` | Payout blocked > 14 days | MEDIUM | Escalate to Finance Admin |
| `PROHIBITED_INFLOW` | Inflow of forbidden type | CRITICAL | Block + report |
| `FX_RATE_UNVERIFIABLE` | FX rate source unlogged | HIGH | Transaction hold |
| `CROSS_BORDER_UNPERMITTED` | Unlawful cross-border transfer | CRITICAL | Block + regulatory flag |

---

# SECTION XII — POLICY VIOLATION EVENT SCHEMA (SEALED)

Every violation detected by PEA produces exactly one `PolicyViolationEvent`.

```json
{
  "violation_id":       "uuid-v4",
  "detected_at":        "ISO-8601 UTC",
  "schema_version":     "1.0",
  "violation_code":     "string (from Section XI catalog)",
  "severity":           "CRITICAL | HIGH | MEDIUM | LOW | INFO",
  "auto_blocked":       "boolean",
  "block_action":       "string | null (what was blocked)",
  "actor_id":           "uuid-v4 | null",
  "actor_type":         "Human | Service | Automation | AI",
  "actor_role":         "string | null",
  "tenant_id":          "uuid-v4 | null",
  "domain_track":       "Arts | Commerce | Science | Technology | Administration | Platform",
  "environment":        "dev | test | staging | production",
  "resource_type":      "string | null",
  "resource_id":        "uuid-v4 | null",
  "evidence_refs":      ["signal_id-1", "audit_id-2"],
  "compliance_domain":  "string (from Section II domain code)",
  "human_action_required": "boolean",
  "assigned_to":        "null | role_code",
  "resolution_status":  "OPEN | ACKNOWLEDGED | RESOLVED | ESCALATED",
  "resolved_at":        "null | ISO-8601",
  "resolution_note":    "null | string"
}
```

```
Schema violations → event discarded → dead-letter topic → PIPELINE_ALERT
Partial violation events → REJECT → re-emit with full fields or discard
```

---

# SECTION XIII — ENFORCEMENT DECISION PROTOCOL (LOCKED)

## 13.1 Decision Hierarchy

```
Step 1: DETECT     — signal or event received, pattern matched against policy
Step 2: CLASSIFY   — violation_code assigned from Section XI catalog
Step 3: EVALUATE   — severity determined, auto_block flag set
Step 4: ACT        — if auto_block = true: block action BEFORE emitting event
Step 5: EMIT       — PolicyViolationEvent written to Kafka + PostgreSQL
Step 6: NOTIFY     — Notification Service routes to assigned role
Step 7: AWAIT      — Human action required for resolution
Step 8: CLOSE      — resolution_status = RESOLVED, resolution_note mandatory

No step may be skipped.
Auto-block executes BEFORE emit (block first, log second).
Human action required before RESOLVED status is set.
```

## 13.2 Auto-Block vs Flag-Only

```
Auto-block = true:   PEA blocks the triggering action immediately
                     Human reviews block post-facto
                     Reversal requires: authorized role + audit record + reason code

Auto-block = false:  PEA flags for human review
                     Action is NOT blocked
                     Human may choose to block or permit
                     All flags logged
```

## 13.3 Escalation Rules

| Severity | Initial Assignee | Escalation If Unresolved |
|---|---|---|
| CRITICAL | Security Admin + Compliance Admin | DPO + Platform Super Admin within 1 hour |
| HIGH | Compliance Admin | Security Admin within 4 hours |
| MEDIUM | Ops Admin | Compliance Admin within 24 hours |
| LOW | Assigned team | Weekly review queue |

---

# SECTION XIV — ARCHITECTURE PLACEMENT (LOCKED)

## 14.1 System Position

```
PEA sits in:
  Kubernetes namespace: ops (security and compliance sub-domain)
  Role: Event consumer + real-time enforcer + violation reporter
  Position: BETWEEN signals and business logic for critical paths

PEA is a GATEKEEPER and REPORTER, not a business service.
```

## 14.2 Infrastructure Dependencies (Locked)

| Component | Role | PEA Usage |
|---|---|---|
| Apache Kafka | Event bus | ALL violation events, ALL policy signal consumption |
| Open Policy Agent | Policy-as-code | All RBAC/ABAC evaluation queries |
| Keycloak | Identity provider | Session, MFA, token event monitoring |
| Kong / Envoy | API Gateway | Rate limit event consumption, WAF signal monitoring |
| ModSecurity | WAF | OWASP Top-10 attack signal ingestion |
| Wazuh | SIEM + IDS | Intrusion detection signal stream |
| PostgreSQL | Source of truth | User, role, tenant, consent, audit records |
| Redis | State cache | Rate limit counters, session state, lock state |
| ClickHouse | Analytics DB | Policy trend analysis, violation frequency |
| Grafana | Dashboard | All compliance dashboards |
| Prometheus | Metrics | PEA health, enforcement rate, violation counts |
| Loki | Log store | Full audit trail of PEA decisions |
| OpenTelemetry | Tracing | End-to-end trace of blocked requests |
| MinIO | Object store | Long-retention violation archives (WORM) |
| HashiCorp Vault | Secrets | No direct secret access; monitors rotation compliance |

## 14.3 Data Flow (Non-Negotiable)

```
[All Platform Events]
        ↓ Kafka topics
[PEA Signal Ingestor]
        ↓ validates schema
[Policy Evaluation Engine]
  ← OPA policy bundle
        ↓ classification + severity
[Enforcement Decision Engine]
        ↓ if auto_block: BLOCK action immediately
[PolicyViolationEvent Generator]
        ↓ writes to PostgreSQL + Kafka
[Notification Router]
        ↓ routes to assigned role
[Human Reviewer]
        ↓ acknowledges / resolves
[Resolution Audit Record]
        ↓ immutable close record to MinIO

PEA never bypasses the human resolution gate.
PEA never self-resolves violations.
PEA never silently absorbs a policy failure.
```

---

# SECTION XV — KAFKA TOPICS (CONSUMED AND PRODUCED)

## 15.1 Topics Consumed

```
ecoskiller.auth.login_attempt
ecoskiller.auth.login_success
ecoskiller.auth.login_failure
ecoskiller.auth.token_issued
ecoskiller.auth.token_expired
ecoskiller.auth.token_revoked
ecoskiller.auth.mfa_challenge
ecoskiller.auth.mfa_success
ecoskiller.auth.mfa_failure
ecoskiller.auth.session_created
ecoskiller.auth.session_revoked
ecoskiller.authz.decision_made (from OPA)
ecoskiller.authz.decision_denied
ecoskiller.rate.limit_approached
ecoskiller.rate.limit_breached
ecoskiller.rate.abuse_pattern_detected
ecoskiller.audit.record_written
ecoskiller.audit.record_rejected
ecoskiller.audit.integrity_check_failed
ecoskiller.vuln.finding_created
ecoskiller.vuln.sla_approaching
ecoskiller.vuln.sla_breached
ecoskiller.vuln.zero_day_detected
ecoskiller.moderation.action_taken
ecoskiller.moderation.complaint_submitted
ecoskiller.moderation.identity_reveal_requested
ecoskiller.pii.exposure_detected
ecoskiller.pii.unmask_requested
ecoskiller.pii.unmask_approved
ecoskiller.consent.granted
ecoskiller.consent.withdrawn
ecoskiller.dsr.request_received
ecoskiller.dsr.step_completed
ecoskiller.billing.settlement_attempted
ecoskiller.billing.escrow_write_attempted
ecoskiller.billing.fraud_signal
ecoskiller.security.wazuh_alert (from Wazuh SIEM)
ecoskiller.security.waf_event (from ModSecurity)
```

## 15.2 Topics Produced

```
ecoskiller.policy.violation_detected
ecoskiller.policy.action_blocked
ecoskiller.policy.escalation_required
ecoskiller.policy.violation_resolved
ecoskiller.security.incident_declared
ecoskiller.compliance.dso_alert
ecoskiller.compliance.deployment_freeze
```

---

# SECTION XVI — API CONTRACT (SEALED)

```
GET  /api/v1/policy/violations?severity=&domain=&tenant_id=&from=&to=
GET  /api/v1/policy/violations/{violation_id}
POST /api/v1/policy/violations/{violation_id}/acknowledge  (role-gated)
POST /api/v1/policy/violations/{violation_id}/resolve      (role-gated, note required)
POST /api/v1/policy/violations/{violation_id}/escalate     (role-gated)
GET  /api/v1/policy/audit/integrity?tenant_id=&from=&to=
GET  /api/v1/policy/rate-limits/status?tenant_id=&endpoint=
GET  /api/v1/policy/vulnerabilities?severity=&status=&from=&to=
GET  /api/v1/policy/compliance/summary?tenant_id=
POST /api/v1/policy/block/override  (CRITICAL — dual approval + step-up MFA required)

All endpoints:
  JWT required
  RBAC enforced via OPA
  Tenant-scoped (unless platform role)
  Rate-limited per RL-P seal
  Audit record on every call
  Override endpoint: dual approval + immutable audit mandatory
```

---

# SECTION XVII — DASHBOARD SPECIFICATIONS

## 17.1 Role-Based Dashboard Matrix

| Dashboard | Visible To | Content |
|---|---|---|
| Security Ops View | Security Admin | Real-time violation stream, Wazuh alerts, WAF events |
| Compliance View | Compliance Admin, Compliance Officer | DSR queue, violation trends, audit integrity status |
| Trust & Safety View | Trust & Safety Officer, Abuse Moderation Lead | Content moderation queue, fraud signals, identity reveal queue |
| Incident Response View | Incident Response Manager | Active incidents, timeline assembly, containment status |
| Tenant Compliance View | Tenant Admin (own tenant only) | Their violations, MFA compliance, rate limit status |
| Platform Super View | Platform Super Admin | Cross-tenant violation heatmap, deployment freeze status |
| Government / Regulatory | Policy Officer, Govt Auditor | Anonymized aggregate compliance reports |

## 17.2 Mandatory Dashboard Panels

**Security Ops View must include:**
```
- Real-time violation feed (CRITICAL + HIGH, last 1 hour)
- CRITICAL violations unresolved (count, list)
- Deployment freeze status
- Wazuh SIEM alert stream
- WAF block rate (last 24h)
- MFA compliance by user tier (% enforced)
- Rate limit breach frequency by domain
```

**Compliance View must include:**
```
- Open DSRs (by type, by SLA status)
- Audit integrity check results (last 7 days)
- Segregation of duties violation count (last 30 days)
- Dual-control compliance rate
- Consent registry health (withdrawn, expired)
- Violation resolution SLA compliance
```

---

# SECTION XVIII — OBSERVABILITY (NON-OPTIONAL)

## 18.1 Prometheus Metrics

```
pea_violations_total (counter, by violation_code, severity)
pea_auto_blocks_total (counter, by violation_code)
pea_violations_open_gauge (gauge, by severity)
pea_resolution_sla_breaches_total (counter, by severity)
pea_audit_integrity_failures_total (counter)
pea_opa_evaluation_latency_seconds (histogram, SLA: < 50ms)
pea_signal_ingestion_lag_seconds (histogram, SLA: < 5s)
pea_mfa_enforcement_rate (gauge, by tier)
pea_rate_limit_breach_rate (gauge, by domain)
```

## 18.2 Alerting Rules

```
CRITICAL violation open > 1 hour  → PagerDuty: CRITICAL_UNRESOLVED
CRITICAL vuln SLA breach          → PagerDuty: DEPLOYMENT_FREEZE
AUDIT_MUTATION_ATTEMPT            → PagerDuty: SECURITY_INCIDENT
TENANT_ISOLATION_BREACH           → PagerDuty: SECURITY_BREACH
MINOR_PROTECTION_BREACH           → PagerDuty: DPO_ESCALATION
ESCROW_FUND_MOVEMENT              → PagerDuty: SECURITY_INCIDENT
OPA evaluation lag > 100ms        → Slack: POLICY_EVAL_DEGRADED
Signal ingestion lag > 30s        → Slack + email: PEA_PIPELINE_LAG
Audit integrity failure            → PagerDuty: AUDIT_INTEGRITY_BREACH
```

---

# SECTION XIX — FAILURE HANDLING (DETERMINISTIC)

| Failure | System Response |
|---|---|
| OPA unreachable | DENY all authorization decisions until OPA restored |
| Kafka signal malformed | Discard + dead-letter topic + alert |
| PostgreSQL write timeout for violation | Retry 3× then CRITICAL alert + local buffer |
| Audit record rejected (partial) | Log rejection, alert, do not permit action |
| Audit mutation attempt | SECURITY_INCIDENT → STOP EXECUTION |
| Wazuh feed interrupted > 5 min | Alert Security Admin |
| PEA processing lag > 30 seconds | Alert; degradation must not silently pass violations |
| Block override without dual approval | Block the override itself + DUAL_CONTROL_VIOLATION |
| Human resolution overdue (by severity) | Auto-escalate per Section XIII.3 |
| Environment isolation breach (prod data in staging) | STOP EXECUTION + INCIDENT |

```
PEA FAILURE PHILOSOPHY:
  When in doubt: DENY.
  A missed legitimate action is recoverable.
  A missed security violation may not be.
  OPA unavailable = DENY ALL.
  Audit unavailable = DENY ALL.
  Silence is never safe.
```

---

# SECTION XX — WHAT THIS AGENT NEVER DOES

```
NEVER invents a policy not declared in this document
NEVER passes an authorization decision without OPA evaluation
NEVER silently absorbs a violation
NEVER self-resolves a violation
NEVER deletes an audit record
NEVER mutates an audit record
NEVER suspends or bans a user autonomously
NEVER deletes user data autonomously
NEVER overrides an MFA requirement
NEVER grants cross-domain access without explicit policy
NEVER reveals an anonymous complainant's identity without full authorization chain
NEVER operates in "allow by exception" mode
NEVER accepts "policy missing" as a reason to allow
NEVER evaluates violations differently by tenant size or plan tier
NEVER produces a violation code not in the Section XI catalog
NEVER acts without emitting a traceable event
```

---

# SECTION XXI — VERSION & CHANGE CONTROL

```
Document Version:   1.0
Status:             SEALED
Seal Date:          2026-03-04
Next Review:        Version bump required for any structural change

Allowed (no version bump):
  - Add new violation code to Section XI (with severity and auto-block declared)
  - Add new Kafka topic to consumed list
  - Add new dashboard panel (additive only)
  - Add new compliance domain to Section II (with seal reference)

Requires version bump + human declaration:
  - Change any enforcement decision in Section XIII
  - Change auto-block behavior for any existing violation code
  - Change any audit field in Section VII.2
  - Change any MFA tier rule in Section IV.2
  - Change any step-up trigger in Section IV.3
  - Change any rate limit tier in Section V.2
  - Change any segregation pair in Section VII.5
  - Change any dual-control rule in Section VII.6
  - Change violation event schema in Section XII
  - Change retention periods in Section VII.4
  - Add or remove a compliance domain from Section II

DEFAULT_BEHAVIOR = DENY
IMPLICIT_BEHAVIOR = FORBIDDEN
ALL violations are declared.
ALL enforcement decisions are traceable.
ALL escalations are role-assigned.
ALL resolutions are human-gated and audited.
```

---

## ✅ SEAL CONFIRMATION

```
✔ Agent identity declared — gatekeeper, not business service
✔ Compliance domain registry locked — 23 domains, all mandatory
✔ Authorization enforcement bound — AUTHZ-A through AUTHZ-U
✔ Authentication & session monitoring declared — token, MFA, step-up
✔ Rate limiting enforcement bound — RL-A through RL-P, 5 tiers
✔ Vulnerability enforcement bound — VULN-A through VULN-Q, SLA declared
✔ Audit policy enforcement bound — AIC-1 through AIC-21, 6 categories
✔ Content moderation & trust enforcement declared
✔ PII masking enforcement bound — PM-A through PM-O
✔ GDPR/DPDP data rights processing declared — DSR SLAs defined
✔ Violation taxonomy sealed — 46 violation codes across 8 categories
✔ PolicyViolationEvent schema sealed
✔ Enforcement decision protocol locked — 8-step FSM
✔ Architecture placement defined — ops namespace, gatekeeper position
✔ Kafka topic catalog declared — 37 consumed, 7 produced
✔ API contract sealed — all routes role-gated, dual-approval endpoint declared
✔ Dashboard RBAC matrix complete
✔ Observability metrics and alerting specified
✔ Failure handling deterministic — 12 failure modes declared
✔ Prohibited behaviors explicitly listed — 20 hard constraints
✔ Change control enforced — version bump triggers declared

POLICY_ENFORCEMENT_AGENT IS SEALED.
EXECUTION BEGINS ONLY ON HUMAN DECLARATION.
DEFAULT_BEHAVIOR = DENY.
OPA UNAVAILABLE = DENY ALL.
AUDIT UNAVAILABLE = DENY ALL.
SILENCE IS NEVER SAFE.
```
