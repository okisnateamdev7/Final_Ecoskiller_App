# 🔒 AUDIT_COMPLIANCE_AGENT.md
## ECOSKILLER — ANTIGRAVITY EXECUTION PROMPT
### Status: SEALED · LOCKED · DETERMINISTIC · ANTIGRAVITY_COMPATIBLE

---

```
PROMPT_CLASS              = AUDIT_COMPLIANCE_AGENT
EXECUTION_ENGINE          = ANTIGRAVITY
SCOPE                     = AUDIT LOGGING · EVIDENCE MANAGEMENT · INCIDENT RESPONSE ·
                            PII MASKING · CONSENT · DATA DELETION · GDPR/DPDP ·
                            LEGAL HOLD · MODERATION · DISPUTE GOVERNANCE ·
                            TRANSPARENCY REPORTING · BACKUP COMPLIANCE ·
                            VULNERABILITY GOVERNANCE · AI GOVERNANCE AUDIT
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
MUTABLE_AUDIT_LOGS        = SECURITY_BREACH
SILENT_ACTIONS            = FORBIDDEN
PII_IN_LOGS               = CRITICAL_INCIDENT
```

---

## ⚠️ ANTIGRAVITY BINDING DECLARATION

> This agent prompt is **PERMANENTLY LOCKED** to the ECOSKILLER Master Constitution v12.0.
> Antigravity MUST NOT reinterpret, simplify, merge compliance domains, auto-approve
> legal actions, skip evidence chains, or bypass any immutability rule herein.
> **Any deviation = STOP EXECUTION → REPORT VIOLATION → NO PARTIAL OUTPUT.**

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME              = AUDIT_COMPLIANCE_AGENT
AGENT_CLASS             = GOVERNANCE · EVIDENCE · LEGAL · SECURITY COMPLIANCE
PLATFORM                = ECOSKILLER (Enterprise Multi-Tenant SaaS)
EXECUTION_LANE          = D — Governance (Audit · Compliance · Moderation · Incident)
CONTRACT_GATE_INPUT     = identity_ready · db_ready
CONTRACT_GATE_OUTPUT    = governance_ready · compliance_certified
DEPENDENCY_CONSUMERS    = All service lanes (every lane must respect audit hooks)
ARCHITECTURE_POSITION   = CROSS-CUTTING CONCERN — applies to EVERY microservice
```

**The Audit Compliance Agent is not a standalone service.**
**It is a cross-cutting governance fabric wired into every ECOSKILLER microservice,**
**API gateway, event bus, data store, and UI layer. No service may claim**
**governance_ready without this agent's contracts implemented.**

---

## 2️⃣ AGENT SCOPE (LOCKED — 14 COMPLIANCE DOMAINS)

| Domain | ID | Seal |
|--------|----|------|
| Audit Event Logging | AEL | LOCKED |
| Data Access Logging | DAL | LOCKED |
| Evidence Management & Chain of Custody | CEM | LOCKED |
| PII Masking & Classification | PII | LOCKED |
| Consent Management | CON | LOCKED |
| Data Deletion & Purge | DEL | LOCKED |
| Legal Hold Management | LHL | LOCKED |
| Incident Response | IRC | LOCKED |
| Moderation & Dispute Governance | MDG | LOCKED |
| Regulatory Jurisdiction (GDPR / DPDP / FERPA / COPPA) | REG | LOCKED |
| AI Analytics Governance Audit | AIG | LOCKED |
| Backup & Restore Compliance | BAK | LOCKED |
| Vulnerability & CVE Governance | VUL | LOCKED |
| Transparency & Public Trust Reporting | TPR | LOCKED |

---

## 3️⃣ DOMAIN AEL — AUDIT EVENT LOGGING (IMMUTABLE FOUNDATION)

### 3.1 Core Immutability Rules

```
APPEND_ONLY       = TRUE (NO UPDATE, NO DELETE on audit tables — ever)
CRYPTOGRAPHIC_INTEGRITY = SHA-256 hash chain (each entry chains to previous)
TAMPER_DETECTION  = ENABLED (hash mismatch → CRITICAL ALERT + access freeze)
MUTABLE_LOGS      = FORBIDDEN → triggers SECURITY_BREACH classification
PII_IN_LOGS       = FORBIDDEN → triggers CRITICAL_INCIDENT
SILENT_ACTIONS    = FORBIDDEN → any system action without audit entry = NON-COMPLIANT
PASSWORDS_IN_LOGS = FORBIDDEN
SECRETS_IN_LOGS   = FORBIDDEN
FULL_PAYLOAD_IN_LOGS = FORBIDDEN
```

### 3.2 Mandatory Audit Event Categories

```
CATEGORY                      EVENTS TO CAPTURE
--------------------------    --------------------------------------------------
Authentication Events         Login · Logout · MFA verify · MFA fail · Token refresh
                              SSO initiation · SSO success · SSO fail · Device bind
Authorization Events          Access granted · Access denied · Role change · Permission override
                              Privilege escalation attempt · Cross-tenant attempt
Tenant Lifecycle              Registration · Verification · Activation · Suspension
                              Reinstatement · Offboarding · Termination
User Lifecycle                Account creation · Email verify · Profile update · Deletion
                              Account lock · Account unlock · Trust score change
Billing & Financial           Payment initiated · Payment confirmed · Payment failed
                              Invoice generated · Refund requested · Refund approved
                              Subscription change · Payout processed
Content & Moderation          Post created · Post deleted · Content flagged · Report filed
                              Moderation decision · Appeal submitted · Appeal resolved
                              Emergency takedown triggered
Dojo (GD/Interview)           Room created · Room deleted · Participant join · Participant leave
                              Role change in room · Recording start · Recording stop
                              Moderation action (mute/remove/block) · Scoring action
                              Mentor override · Assessment finalized · Certification issued
Job & Recruitment             Job posted · Job moderated · Application submitted
                              Offer locked · Offer accepted · Offer declined
                              SME reliability score updated
AI / Automation               AI score generated · AI model version tagged · Prompt logged
                              AI output overridden by human · Bias flag raised
                              Auto-promotion blocked · Belt awarded (mentor confirmed)
Data Operations               Export requested · Export approved · Export delivered
                              Bulk delete initiated · Deletion propagation confirmed
                              Legal hold applied · Legal hold released
                              Consent captured · Consent revoked · Consent expired
Encryption & Keys             DEK generated · DEK rotated · DEK revoked
                              KEK rotation event · Key access event
Security Events               WAF rule triggered · Rate limit hit · Brute-force detected
                              Vulnerability scan completed · CVE flagged · Patch deployed
                              Isolation violation detected · PII exposure detected
Admin & Ops                   Admin login · Admin action · Role override
                              Config change · Feature flag change · Emergency switch
```

### 3.3 Audit Log Entry Structure (MANDATORY FIELDS)

```json
{
  "event_id":           "UUID (globally unique)",
  "event_type":         "auth.login.success | tenant.activated | ...",
  "event_category":     "AUTHENTICATION | AUTHORIZATION | BILLING | DOJO | ...",
  "timestamp":          "ISO8601 UTC (microsecond precision)",
  "actor_id":           "UUID (user_id or service_id)",
  "actor_type":         "USER | SERVICE | AUTOMATION | AI_AGENT",
  "actor_role":         "STUDENT | MENTOR | ADMIN | PLATFORM_OPS | ...",
  "tenant_id":          "UUID",
  "domain_track":       "ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION | PLATFORM",
  "source_ip":          "IPv4 or IPv6 (masked for logs if PII concern)",
  "device_fingerprint": "hashed_device_id",
  "resource_type":      "USER | JOB | DOJO_ROOM | ASSESSMENT | INVOICE | ...",
  "resource_id":        "UUID",
  "action":             "CREATE | READ | UPDATE | DELETE | EXPORT | APPROVE | DENY | ...",
  "result":             "ALLOW | DENY | FAIL | ERROR",
  "session_id":         "UUID",
  "correlation_id":     "UUID (trace across distributed services)",
  "access_purpose":     "ASSESSMENT | SUPPORT | MODERATION | COMPLIANCE | ANALYTICS",
  "access_channel":     "UI | API | ADMIN_CONSOLE | AUTOMATION",
  "risk_level":         "LOW | MEDIUM | HIGH | CRITICAL",
  "previous_hash":      "SHA-256 of previous log entry (chain integrity)",
  "current_hash":       "SHA-256 of this entry (computed on write)",
  "schema_version":     "1.0"
}
```

### 3.4 Audit Log Isolation Rules

```
Tenant A CANNOT access Tenant B audit logs
Arts domain logs isolated from Commerce & Science logs
Cross-tenant log visibility = FORBIDDEN (even Super Admin requires compliance order)
Compliance Officer: read-only, MFA required, scoped per order
Security Admin: read-only, MFA required, scoped to security events
Authorized Tenant Admin: own-tenant audit only, read-only, MFA required
All log access events themselves ARE logged (access-of-access logging)
No bulk export without dual-approval + watermarking
```

### 3.5 Dojo-Specific Audit Requirements

```
For every Dojo GD / Interview / Match room:
  ✅ Room creation + deletion (actor, tenant, session_id)
  ✅ Participant join / leave (with role: STUDENT | MENTOR | EVALUATOR | OBSERVER)
  ✅ Role changes within room (speaker grant/revoke, evaluator assignment)
  ✅ Recording start / stop (with explicit consent reference: consent_id)
  ✅ Transcript generation trigger (with consent reference)
  ✅ Moderation actions: mute, unmute, remove, block (actor + reason)
  ✅ Scoring action + mentor override (immutable, with justification)
  ✅ Assessment finalization (evaluator_id + score + formula_version)
  ✅ Certification decision (mentor_confirmed=true required)

RULE: No Dojo moderation action without audit trace → SESSION INVALID
RULE: Scoring override without logged justification → BLOCKED
RULE: Auto-promotion without mentor confirmation → HARD FAIL
```

---

## 4️⃣ DOMAIN DAL — DATA ACCESS LOGGING

### 4.1 Principles

```
EVERY data access must be logged — READ as important as WRITE
No silent reads · No invisible queries · No anonymous background access
Service-to-service reads logged identically to human reads
All access events: purpose-bound, immutable, tenant-isolated
```

### 4.2 Access Type Classification

```
ACCESS_TYPES = READ | WRITE | UPDATE | DELETE | EXPORT | BULK_QUERY
BULK or EXPORT access = elevated scrutiny + dual-approval for admin
```

### 4.3 Data Access Log Entry

```json
{
  "dal_event_id":    "UUID",
  "timestamp":       "ISO8601 UTC microseconds",
  "actor_id":        "UUID (user or service)",
  "actor_type":      "USER | SERVICE | AUTOMATION",
  "user_type":       "STUDENT | MENTOR | ADMIN | COMPLIANCE | ...",
  "tenant_id":       "UUID",
  "domain_track":    "ARTS | COMMERCE | SCIENCE | ...",
  "role_at_access":  "role active during access",
  "data_category":   "USER_PROFILE | ACADEMIC_RECORD | ASSESSMENT | DISCUSSION |
                      MEDIA | ANALYTICS | AUDIT | SEARCH | BACKUP",
  "resource_id":     "UUID",
  "access_type":     "READ | WRITE | UPDATE | DELETE | EXPORT | BULK_QUERY",
  "access_purpose":  "ASSESSMENT | SUPPORT | MODERATION | ANALYTICS | COMPLIANCE",
  "access_channel":  "UI | API | ADMIN_CONSOLE | AUTOMATION",
  "result":          "SUCCESS | DENIED",
  "source_ip":       "masked",
  "elevated_flag":   "boolean (true for admin/ops/compliance access)",
  "previous_hash":   "SHA-256",
  "current_hash":    "SHA-256"
}
```

### 4.4 Privileged Access Controls

```
Admin / Ops / Compliance access to data:
  - Requires explicit authorized role at time of access
  - Elevated_flag = TRUE
  - Triggers real-time alert for off-hours access
  - Triggers alert for mass data read (> threshold in window)
  - Requires access_purpose justification (no purpose = DENY)

Dojo-specific access logging:
  - Chat history view: logged
  - Recording playback: logged with viewer_id + purpose
  - Transcript access: logged with viewer_id + purpose
  - Evaluation notes view: logged with evaluator_id
  - Participant metadata read: logged
```

---

## 5️⃣ DOMAIN CEM — EVIDENCE MANAGEMENT & CHAIN OF CUSTODY

### 5.1 Evidence Immutability Rules

```
Once evidence is FINALIZED:
  ALLOWED:   append annotation · append correction note · append legal hold · append appeal outcome
  FORBIDDEN: update content · delete content · overwrite metadata · reassign ownership

Any mutation attempt → HARD FAIL + CRITICAL ALERT
```

### 5.2 Integrity Sealing

```
Every evidence object must carry:
  content_hash:   SHA-256 of evidence content (computed on creation)
  metadata_hash:  SHA-256 of evidence metadata
  hash_stored:    separately from content (tamper-isolated)

On every access:
  Hash recomputed and compared
  Mismatch → integrity_state = COMPROMISED → access freeze + alert
```

### 5.3 Evidence Categories (Domain-Sensitive)

```
ARTS:
  - Discussion transcripts · Creative submissions · Peer review evidence
  - Mentor feedback evidence · Participation timestamps

COMMERCE:
  - Evaluation rubrics · Presentation scoring · Business simulation logs
  - Financial decision evidence · Conflict-of-interest disclosure evidence

SCIENCE:
  - Methodology submissions · Experiment discussion logs
  - Data interpretation evidence · Plagiarism analysis evidence

Cross-domain reuse without domain_tag = BLOCK
```

### 5.4 Assessment & Certification Evidence Chain

```
Every score, grade, belt, or certificate MUST have a complete evidence chain:
  1. Assessment criteria snapshot (version-locked rubric at time of assessment)
  2. Evaluator identity (sealed, hashed)
  3. Raw scoring data (peer scores + mentor scores + self-assessment if applicable)
  4. Normalization logic applied (algorithm version tagged)
  5. Final outcome (score, belt, grade)
  6. Mentor confirmation record (mentor_confirmed = TRUE, mentor_id, timestamp)
  7. Appeal status (NONE | PENDING | RESOLVED)

Certificates without full evidence chain = NON-ISSUABLE
AI cannot directly award belts — mentor confirmation ALWAYS required
```

### 5.5 Consent Evidence Lock

```
All consent must be independently evidentiary:
  consent_id · consent_version · user_id · domain_track
  scope (recording | evaluation | certification | data_use)
  timestamp · revocation_flag · guardian_flag (minors)

Missing consent evidence → downstream evidence INVALID
Implied or bundled consent = FORBIDDEN
```

### 5.6 Moderation & Complaint Evidence Lock

```
Every moderation decision must preserve:
  - Original reported content (hashed, sealed)
  - Reporter identity (masked if anonymous complaint)
  - Real identity stored in sealed audit vault (for legal use only)
  - Moderation decision + decision_maker_id
  - Appeal chain (if any)
  - Final resolution status + timestamp
  - Evidence sealed post-resolution (immutable)
```

---

## 6️⃣ DOMAIN PII — PII MASKING & CLASSIFICATION

### 6.1 PII Classification Registry

```
PII_FIELD               PII_TYPE          SENSITIVITY    MASKING_STRATEGY
--------------------    ---------------   -----------    ----------------------------
Full name               IDENTITY          HIGH           Partial (Jo** Do**)
Email address           CONTACT           HIGH           Partial (jo****@mail.com)
Phone number            CONTACT           HIGH           Partial (98****1234)
Government/Student ID   GOVERNMENT_ID     CRITICAL       Full redaction (***)
Exact location          LOCATION          HIGH           City-level only
Date of birth           IDENTITY          HIGH           Year only for non-owner
Biometric identifiers   BIOMETRIC         CRITICAL       Tokenization only
Bank account / UPI      FINANCIAL         CRITICAL       Last 4 only
Tax ID (PAN/GST)        FINANCIAL         CRITICAL       Full redaction
IP Address (personal)   NETWORK           MEDIUM         Truncated (x.x.x.*)
Device ID               NETWORK           MEDIUM         Hashed token

Untyped PII field = FORBIDDEN → BLOCK DEPLOYMENT
```

### 6.2 Role-Aware Masking Matrix

```
VIEWER ROLE           FULL PII   PARTIAL   REDACTED   TOKENIZED
-------------------   --------   -------   --------   ---------
Self (data subject)   ✅         —         —          —
Student (other)       —          —         ✅         —
Mentor/Evaluator      —          ✅         —          —
Tenant Admin          —          ✅         —          —
Platform Ops          —          —         ✅         —
Compliance Officer    UNMASK*    —         —          —
Automation/Service    —          —         —          ✅

* Unmask requires: explicit role + purpose justification +
  time-bound access grant + full audit trail + alert fired
```

### 6.3 Masking Enforcement Rules

```
Masking applied SERVER-SIDE before any response (API, UI, export, PDF, CSV)
Client-side masking alone = FORBIDDEN
Logs must mask PII (PII in logs = CRITICAL_INCIDENT)
Telemetry excludes PII (debug logs sanitized before emission)
Exports respect same masking rules as API responses
No raw PII to analytics vendors (tokenization mandatory)
Dojo recordings: usernames preferred over real names in overlays
Transcripts: PII redacted before storage and before any viewer access
Recording replay: masking level matches viewer's role
```

### 6.4 Controlled Unmasking Protocol

```
Unmask request → Compliance Officer submits justification
  → Dual approval required (Compliance Officer + Super Admin)
  → Time-bound access granted (max 4 hours per request)
  → All views during access window are logged individually
  → Access window expires automatically
  → Alert fires to Security Officer during access window
  → Unmask event written to immutable audit log
```

---

## 7️⃣ DOMAIN CON — CONSENT MANAGEMENT

### 7.1 Consent Principles

```
Consent MUST be: explicit · informed · purpose-specific · freely given · revocable
Implied consent = FORBIDDEN
Bundled consent = FORBIDDEN
Blanket personal data consent = FORBIDDEN
Domain-crossed consent = FORBIDDEN (Arts consent ≠ Commerce consent)
```

### 7.2 Consent Triggers (Mandatory Capture)

```
TRIGGER                         CONSENT SCOPE
------------------------------  ------------------------------------------
Recording in Dojo room          recording | transcript | replay_access
Certification attempt           performance_assessment_disclosure
Data use for AI training        ai_data_use (separate, explicit)
Third-party data sharing        third_party_sharing (per vendor)
Profile public visibility       public_profile_display
Parent/guardian visibility      parent_visibility_disclosure
Minor onboarding                guardian_consent (required, not optional)
```

### 7.3 Consent Record Structure

```sql
consent_records:
  consent_id          UUID PK
  user_id             UUID NOT NULL
  tenant_id           UUID NOT NULL
  domain_track        ENUM('ARTS','COMMERCE','SCIENCE','TECHNOLOGY','ADMINISTRATION')
  scope               VARCHAR(100)    -- 'recording' | 'certification' | 'ai_data_use' | ...
  consent_version     VARCHAR(20)     -- versioned with platform policy
  consented_at        TIMESTAMPTZ NOT NULL
  consent_method      ENUM('EXPLICIT_CLICK','SIGNED_FORM','API_CONFIRM')
  is_active           BOOLEAN DEFAULT TRUE
  revoked_at          TIMESTAMPTZ
  revoked_by          UUID
  revocation_reason   TEXT
  guardian_id         UUID            -- for minors
  audit_hash          CHAR(64) NOT NULL
  -- NO UPDATE OR DELETE — append revocation only
```

### 7.4 Consent Enforcement Rules

```
Check consent BEFORE data processing
Check consent BEFORE feature activation
Block execution if consent missing or expired
Revoked consent = immediate feature block + data use halt
Third-party sharing requires separate consent (not bundled)
Consent withdrawal → deletion workflow triggered within 30 days
Annual consent audit mandatory
Unauthorized consent scope expansion = BLOCK RELEASE
```

---

## 8️⃣ DOMAIN DEL — DATA DELETION & PURGE COMPLIANCE

### 8.1 Deletion Triggers

```
TRIGGER                       WORKFLOW
--------------------------    ----------------------------------------
User-initiated deletion       Self-service form → validation → 30-day grace → hard delete
Account closure               Admin confirms → 30-day grace → hard delete cascade
Retention expiry (auto)       Airflow DAG → automated purge pipeline
Legal requirement             Compliance Officer order → immediate or scheduled
Security incident remediation Incident Commander order → forensic-safe purge
Tenant offboarding            Full tenant data purge (Section DEL-7)
```

### 8.2 Deletion Propagation (Mandatory — All Layers)

```
Layer               Action Required
-----------------   -----------------------------------------------------------
Primary DB          Hard delete (after soft-delete grace window closes)
Search Indexes      Elasticsearch document removal + index refresh
Caches              Redis key flush (tenant:{id}:user:{id}:*)
Object Storage      MinIO object deletion (all user-associated objects)
Derived Datasets    ClickHouse records purged
Analytics Stores    Anonymized aggregates retained; linked records purged
Audit Logs          NOT deleted — user identity anonymized (pseudonymization)
Backups             Deleted data ages out; backup rehydration re-applies deletions
Kafka Events        TTL enforcement; no resurrection via event replay

Partial deletion = NON-COMPLIANT → incident raised
```

### 8.3 Deletion Verification Protocol

```
After every deletion:
  1. Verify each layer reports deletion complete
  2. Generate deletion receipt (deletion_id, layers_purged[], timestamp, actor_id)
  3. Deletion receipt stored in immutable compliance ledger
  4. Notify user/tenant on completion (no sensitive metadata in notification)
  5. Failed deletion layer → INCIDENT raised → manual remediation required
```

### 8.4 Soft Delete → Hard Delete Rules

```
Soft delete: allowed during grace period (configurable: 7–30 days)
Hard delete: mandatory after grace period (automated, idempotent, retry-safe)
Grace period: time-bound, documented, audit-logged
Backup resurrection of deleted data = FORBIDDEN
Legal hold overrides deletion (see Domain LHL)
```

---

## 9️⃣ DOMAIN LHL — LEGAL HOLD MANAGEMENT

### 9.1 Legal Hold Principles

```
Legal hold OVERRIDES all retention expiry and deletion workflows
Legal hold must be: explicit · scoped · time-bound · compliance-approved
Permanent legal hold = FORBIDDEN (must have defined duration or review date)
Deletion with active legal hold = CRITICAL VIOLATION
```

### 9.2 Legal Hold Lifecycle

```
REQUESTED (by Compliance Officer or Legal team)
  → Scope defined (user_id | tenant_id | match_id | evidence_id | date_range)
  → Dual approval: Compliance Officer + Super Admin (MFA required)
  → Hold applied: all data in scope flagged as LEGAL_HOLD
  → Deletion workflows for in-scope data: PAUSED
  → Evidence objects: SEALED (append-only enforced)

ACTIVE
  → Periodic review date set (max 180 days without re-review)
  → Hold owner must confirm continuation or release
  → Stale hold (no review within window) → escalation to Super Admin

RELEASED
  → Compliance Officer confirms release
  → Dual approval required
  → Deletion workflows resume for expired data
  → Release event logged to immutable audit

COMPLETED
  → Evidence released from hold
  → Original retention policy re-applied
  → Legal hold record retained 7 years (immutable)
```

### 9.3 Legal Hold Database Schema

```sql
legal_holds:
  hold_id             UUID PK
  hold_name           VARCHAR(255) NOT NULL
  scope_type          ENUM('USER','TENANT','MATCH','EVIDENCE','DATE_RANGE','MIXED')
  scope_definition    JSONB NOT NULL
  jurisdiction        VARCHAR(50)    -- GDPR | DPDP | FERPA | COPPA | INSTITUTIONAL
  status              ENUM('REQUESTED','ACTIVE','RELEASED','COMPLETED')
  requested_by        UUID NOT NULL   -- human compliance officer
  approved_by_1       UUID NOT NULL   -- Compliance Officer
  approved_by_2       UUID NOT NULL   -- Super Admin
  approved_at         TIMESTAMPTZ
  review_due_at       TIMESTAMPTZ NOT NULL
  released_by         UUID
  released_at         TIMESTAMPTZ
  release_reason      TEXT
  created_at          TIMESTAMPTZ DEFAULT NOW()
  audit_hash          CHAR(64) NOT NULL

legal_hold_items:
  item_id             UUID PK
  hold_id             UUID NOT NULL REFERENCES legal_holds(hold_id)
  entity_type         VARCHAR(100)
  entity_id           UUID NOT NULL
  applied_at          TIMESTAMPTZ DEFAULT NOW()
  released_at         TIMESTAMPTZ
  audit_hash          CHAR(64)
```

---

## 🔟 DOMAIN IRC — INCIDENT RESPONSE COMPLIANCE

### 10.1 Incident Severity Classification

```
SEVERITY   LABEL      SLA_CONTAIN  SLA_RESOLVE  EXAMPLES
--------   --------   -----------  -----------  ----------------------------------------
P0         CRITICAL   15 minutes   4 hours      Cross-tenant data breach · DEK exposed
                                                Live Dojo exploitation · Financial fraud
P1         HIGH       1 hour       24 hours     Auth bypass · Mass PII exposure
                                                Recording consent violation · Cert fraud
P2         MEDIUM     4 hours      72 hours     Scoring anomaly · Moderation failure
                                                Rate limit breach · Config error
P3         LOW        24 hours     7 days       Policy drift · Minor access anomaly
P4         INFO       72 hours     14 days      Near-miss · Monitoring gap identified
```

### 10.2 Incident Lifecycle (State Machine)

```
DETECTED
  → Classification required immediately (incident_id · type · severity · domain)
  → Incident Commander assigned (ONE commander, non-delegable authority)
  ↓
CONTAINED (MANDATORY BEFORE REMEDIATION)
  Allowed actions: session revocation · service isolation · feature freeze
                   tenant lockdown · evidence access freeze
  Forbidden:       data deletion · evidence mutation · silent rollback
  ↓
EVIDENCE_PRESERVED
  → All related data placed on legal hold
  → New incident_evidence objects created (hashed, sealed)
  → Hash verification enforced
  ↓
INVESTIGATED
  → Root cause analysis produced
  → Attack or failure vector documented
  → Policy breach mapped
  → Control failure mapped
  → Speculative conclusions FORBIDDEN
  ↓
NOTIFIED (jurisdiction-aware, severity-gated)
  P0/P1: Users impacted + Tenant admins + Regulator (where legally required)
  P2: Tenant admins
  P3/P4: Internal ops only
  Over-notification FORBIDDEN · Under-notification FORBIDDEN
  ↓
REMEDIATED
  → Fix mapped to root cause (no unvalidated fixes)
  → Control improvement documented
  → Validation test passed before deployment
  ↓
REVIEWED (Post-Incident — MANDATORY)
  → Timeline produced · What failed · What worked
  → Control gaps identified · Policy updates required
  → Review skipped = INCIDENT NOT CLOSED
  ↓
CLOSED & SEALED
  → Containment verified · Remediation deployed
  → Notifications complete · Evidence preserved
  → Review approved by Incident Commander
  → Incident record sealed (immutable, retained per policy)
```

### 10.3 Dojo Real-Time Incident Handling

```
TRIGGERS:
  - Live harassment during active GD/Interview room
  - Impersonation of evaluator/mentor
  - Exam compromise or answer leaking
  - Mentor abuse of moderation power

MANDATORY IMMEDIATE ACTIONS (< 60 seconds):
  1. Instant room freeze (participants cannot speak/chat)
  2. Participant isolation (suspected actor removed from room)
  3. Session termination (graceful, evidence-preserving)
  4. Real-time evidence capture (buffer sealed immediately)
  5. Incident auto-created at P1 severity
  6. Tenant Admin + Platform Ops notified instantly

Delayed response to live Dojo incident = FAILURE
```

### 10.4 AI & Automation Incidents

```
AI-related incidents must be isolated:
  - Model version frozen (no new inference from implicated model)
  - Automation disabled if AI agent is implicated
  - Decision replay blocked until investigation complete
  - AI rollback without evidence chain = FORBIDDEN

AI cannot:
  - Approve incident severity reclassification
  - Release legal holds
  - Approve remediation plans
  - Close incidents
```

### 10.5 Financial Incident Handling

```
TRIGGERS:
  Unauthorized payout · Pricing manipulation · Commission abuse
  Refund fraud · Wallet injection · Duplicate charge

MANDATORY:
  1. Transaction freeze (immediate, no new billing operations for affected account)
  2. Dual-control investigation (cannot be resolved by single actor)
  3. Billing Agent notified + Audit escalation
  4. Finance trail reconstructed from immutable billing ledger
  5. Single-actor resolution = DENY
```

### 10.6 Incident Record Retention

```
Incident records = LEGAL EVIDENCE
Retention: mandatory policy-bound duration
Legal hold overrides retention (hold can extend indefinitely with review)
Silent deletion of incident records = CRITICAL VIOLATION
Destruction requires Compliance Officer approval + audit log
```

---

## 1️⃣1️⃣ DOMAIN MDG — MODERATION & DISPUTE GOVERNANCE

### 11.1 Content Moderation Architecture

```
MODERATION_PIPELINE:
  1. Content submitted → automated pre-screening (spam, keywords, abuse signals)
  2. Flagged content → Moderation Queue
  3. Human moderator reviews (within SLA per severity)
  4. Decision: APPROVE | REMOVE | ESCALATE | HIDE_PENDING
  5. Decision logged (immutable, moderator_id + reason)
  6. User notified of outcome
  7. Appeal window opens (48 hours)
  8. Appeal reviewed by senior moderator (≠ original reviewer)
  9. Final decision logged + sealed

EMERGENCY TAKEDOWN:
  - Platform Super Admin or Compliance Officer can trigger
  - Content hidden immediately, review follows
  - MFA required · Reason mandatory · Alert fires
```

### 11.2 Dispute Governance (All dispute types)

```
DISPUTE TYPES:
  Match score dispute       → tied to match_id
  Certification dispute     → tied to cert_id + evidence chain
  Mentor misconduct         → tied to mentor_id + session evidence
  Refund dispute            → tied to payment_id (handled by Billing Agent)
  Content dispute           → tied to content_id
  Job offer dispute         → tied to offer_id + audit trail

DISPUTE WORKFLOW:
  1. Party submits dispute (reason + evidence upload)
  2. Dispute record created (dispute_id · type · severity · affected parties)
  3. Evidence sealed (both sides, immutable)
  4. Assigned to appropriate reviewer (≠ original decision-maker)
  5. Review SLA: 5 days for standard, 24 hours for P1 scoring disputes
  6. Decision: UPHELD | OVERTURNED | PARTIAL_RESOLUTION
  7. All parties notified · Decision logged (immutable)
  8. If overturned: corrective action triggered (rescore / re-certify / refund)
  9. Second appeal: governance board review (final, no further appeal)

RULE: All disputes tied to entity IDs (match_id, cert_id, etc.)
RULE: Dispute reviewer ≠ original decision-maker (conflict of interest rule)
RULE: Dispute decisions immutable once sealed
```

### 11.3 Anonymous Complaint System

```
Verified student submits anonymous complaint:
  - Student identity verified (credential check before submission)
  - Identity masked in all public-facing views
  - Real identity stored in sealed audit vault ONLY
  - Vault access: Compliance Officer + dual approval + legal basis only
  - Complaint taggable to: Department | HOD | Principal | Institution | External body
  - Anti-abuse: false report penalty system enforced
  - Escalation to government/regulatory bodies via R63 escalation router
```

### 11.4 Moderator Accountability

```
Moderator activity monitoring:
  - Decision rate tracking
  - Appeal overturn rate per moderator
  - Consistency score (cross-moderator variance)
  - Anomaly: >20% overturn rate → supervisor review
  - All moderator actions audit-logged (no moderator action is anonymous)
```

---

## 1️⃣2️⃣ DOMAIN REG — REGULATORY JURISDICTION COMPLIANCE

### 12.1 Jurisdiction Registry

```
JURISDICTION    APPLIES TO                      KEY OBLIGATIONS
-----------     ----------------------------    -------------------------------------------
GDPR            EU users / EU data residency    Right to erasure · Data portability · DPA
                                                Breach notification ≤ 72 hours · DPO required
DPDP            India users (all tenants)       Consent-first · Purpose limitation
                                                Grievance redressal · Localization
FERPA           US education institutions       Student record privacy · Guardian rights
                                                Disclosure restrictions
COPPA           Users < 13 years               Guardian consent mandatory · No data collection
                                                without guardian approval
INSTITUTIONAL   Per-institution mandates        Institution-specific compliance overlays
```

### 12.2 Data Subject Rights Engine

```
RIGHT                  TRIGGER             SLA         MECHANISM
-------------------    -----------------   ---------   ---------------------------
Right to Access        User request        30 days     Data export package (GDPR/DPDP)
Right to Erasure       User request        30 days     Deletion workflow (Domain DEL)
Right to Portability   User request        30 days     Structured JSON/CSV export
Right to Rectification User request        15 days     Profile correction workflow
Right to Object        User objection      Immediate   Processing halt for objected scope
Grievance Redressal    DPDP filing         30 days     Formal grievance response + audit

All data subject rights requests:
  - Logged in immutable compliance ledger
  - SLA tracked with automated alerting at 80% of window
  - Completion certificate issued to user
  - Record retained 7 years (immutable)
```

### 12.3 Cross-Border Data Transfer Rules

```
RULE: Cross-border transfer without legal basis = DENY
RULE: Jurisdiction inference forbidden (must be declared)
RULE: Jurisdiction mismatch in policy execution = DENY
RULE: Third-party data sharing requires compatible jurisdiction + DPA
RULE: Sub-processor list disclosed to users on request
RULE: Data residency selection immutable post-activation (see Tenancy Agent)
```

### 12.4 Breach Notification Protocol

```
P0/P1 personal data breach:
  Within 72 hours (GDPR) / as soon as practicable (DPDP):
    1. Regulator notified (automated notification draft + human review + submission)
    2. Affected users notified (with breach scope, recommended actions)
    3. Notification records retained in immutable compliance ledger
    4. Breach entry in Transparency Report

Notification records:
  - Authority notification: timestamp · method · reference number
  - User notification: batch_id · delivery_status · content_hash
  - Records retained 7 years
```

---

## 1️⃣3️⃣ DOMAIN AIG — AI ANALYTICS GOVERNANCE AUDIT

### 13.1 AI Governance Principles

```
AI advises only — AI NEVER approves, blocks, or overrides humans autonomously
AI agents have NO UI access, NO human roles, NO approval authority
AI output is always human-reviewable and human-overridable
Bias review sampling is MANDATORY (not optional per feature)
Explainability notes REQUIRED on all AI-generated scores
```

### 13.2 AI Audit Trail Requirements

```
EVERY AI operation must capture:
  ai_request_id      (UUID, globally unique)
  model_name         (e.g., "skill_match_v2.1")
  model_version      (semantic version tag — mandatory)
  prompt_hash        (SHA-256 of prompt — NOT the prompt itself)
  input_feature_hash (hashed input vector)
  output_summary     (human-readable description, NOT raw output in log)
  confidence_score   (model confidence)
  bias_flag          (triggered if anomaly detected in output distribution)
  human_override     (boolean — was this AI decision overridden by human?)
  override_actor     (user_id if overridden)
  override_reason    (text if overridden)
  tenant_id          (scoped)
  domain_track       (scoped)
  timestamp          (ISO8601 UTC)
  schema_version     "1.0"
```

### 13.3 AI Governance Rules

```
RULE: Model version tagging mandatory before any AI output enters system
RULE: Prompt logging: hash only (never raw prompts in audit log)
RULE: All AI outputs subject to human override rights
RULE: Belt promotions: AI may recommend — mentor confirmation ALWAYS required
RULE: AI cannot directly award certifications
RULE: Bias review sampling: minimum 5% of AI scoring outputs per month
RULE: Explainability note: every AI match score must carry human-readable explanation
RULE: AI model retrained data must be GDPR/DPDP compliant (consent-checked)
RULE: AI incident (bias confirmed, model error): model version frozen immediately
RULE: AI rollback requires evidence chain — silent rollback = FORBIDDEN
```

---

## 1️⃣4️⃣ DOMAIN BAK — BACKUP & RESTORE COMPLIANCE

### 14.1 Backup Scope (Mandatory)

```
INCLUDED:           Primary databases · Object storage · Config metadata
                    Identity metadata (non-secret) · Audit + compliance logs
                    Encryption key metadata references (NOT keys)

EXCLUDED:           Plaintext secrets · Ephemeral caches
                    Transient queues (unless replay required)
                    Plaintext DEKs or KEKs (managed in KMS only)
```

### 14.2 Backup Rules

```
ENCRYPTION:     AES-256 at rest (separate keys per environment via KMS)
FREQUENCY:      Daily full or incremental · Hourly snapshots for critical systems
                Pre-deployment backup for high-risk changes
IMMUTABILITY:   Write-once / immutable snapshots (ransomware protection)
ISOLATION:      Tenant A backups never restore into Tenant B
ENVIRONMENT:    No prod backups restored to non-prod (synthetic data for testing)
RESTORE AUTH:   Explicit role approval + multi-person control for full restores
TESTING:        Quarterly restore drill (mandatory, intern-executable runbook)
CONSENT AWARE:  Restores re-apply deletions and anonymizations
                Consent withdrawal respected post-restore
                No resurrection of deleted or withdrawn-consent data
```

### 14.3 Restore Authorization Protocol

```
Full Restore:
  → Multi-person control (≥ 2 authorized DevOps + Compliance Officer sign-off)
  → MFA required for all approvers
  → Restore audit: initiation · completion · integrity checks · anomalies
  → Post-restore: checksum validation + schema integrity + access control verify
  → Restore record: immutable, retained per compliance policy
```

---

## 1️⃣5️⃣ DOMAIN VUL — VULNERABILITY & CVE GOVERNANCE

### 15.1 Vulnerability Management Pipeline

```
SCAN TYPES (mandatory):
  - Dependency scanning (SCA): every build
  - Container image scanning: every image push
  - SAST (Static Application Security Testing): every PR
  - DAST (Dynamic testing): staging environment weekly
  - CVE scanning (direct + transitive dependencies): every build

CLASSIFICATION:
  CRITICAL / HIGH CVE → BLOCK BUILD immediately
  MEDIUM CVE → flagged, 72-hour remediation SLA
  LOW CVE → tracked, 30-day remediation SLA
```

### 15.2 CVE Record Structure

```
cve_records:
  cve_record_id     UUID PK
  cve_id            VARCHAR(20)    -- e.g., CVE-2024-12345
  affected_service  VARCHAR(100)
  severity          ENUM('CRITICAL','HIGH','MEDIUM','LOW')
  cvss_score        DECIMAL(3,1)
  detected_at       TIMESTAMPTZ
  detection_source  ENUM('AUTO_SCAN','MANUAL','VENDOR_ADVISORY','EXTERNAL_REPORT')
  status            ENUM('DETECTED','TRIAGED','IN_REMEDIATION','PATCHED','ACCEPTED_RISK')
  patch_version     VARCHAR(50)
  patched_at        TIMESTAMPTZ
  accepted_risk_by  UUID            -- human authority if accepted without patch
  accepted_risk_reason TEXT
  tenant_impact     JSONB           -- which tenants affected
  audit_hash        CHAR(64)
```

### 15.3 SIEM Integration

```
SIEM_TOOL = Wazuh (deployed, self-hosted)
STREAMS:
  - Real-time auth failure streams
  - WAF alert streams
  - Rate limit breach events
  - Isolation violation events
  - CVE auto-alerts on new disclosures
  - Anomaly detection signals (behavioral baseline deviation)

SIEM → auto-creates P2+ incident records for confirmed threats
SIEM → fires PagerDuty CRITICAL for P0/P1 signals
SIEM → all SIEM events themselves are immutably logged
```

---

## 1️⃣6️⃣ DOMAIN TPR — TRANSPARENCY & PUBLIC TRUST REPORTING

### 16.1 Public Transparency Report

```
Published: quarterly (minimum)
Content (anonymized, statistically redacted):
  - Total moderation actions taken (by category)
  - Appeal overturn rates
  - Dispute resolution outcomes (aggregate)
  - Data deletion requests: received / completed / pending
  - Data subject rights requests: received / fulfilled / denied
  - Security incidents: count by severity (no details)
  - Uptime / SLA compliance (public status page)
  - Active legal holds: count only

No PII, no tenant-identifiable information in public report
Report hash published (verifiable integrity)
```

### 16.2 Public Verification API

```
Endpoints:
  GET /verify/certificate/{cert_id}   → returns: valid | invalid | revoked
  GET /verify/skill/{badge_id}        → returns: valid | expired | invalid
  GET /verify/credential/{hash}       → returns: match | no_match

Rules:
  - Hash-based verification only (no raw data returned)
  - Tamper-proof signature on every verification response
  - All verification requests logged (rate-limited per IP)
  - API publicly accessible (no auth required for verification)
```

### 16.3 Government & Regulatory Escalation

```
Complaint Escalation Router:
  - Priority escalation scoring (automated, human-reviewed)
  - Jurisdiction routing resolver (maps complaint to correct authority)
  - Exportable Evidence Pack Generator (for regulatory submissions)
  - All escalation records: immutable, tenant-isolated, audit-logged
```

---

## 1️⃣7️⃣ DATABASE SCHEMAS (MANDATORY)

```sql
-- Master Audit Log (immutable, append-only)
CREATE TABLE audit_events (
  event_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type          VARCHAR(100) NOT NULL,
  event_category      VARCHAR(50) NOT NULL,
  timestamp           TIMESTAMPTZ(6) NOT NULL DEFAULT NOW(),
  actor_id            UUID NOT NULL,
  actor_type          ENUM('USER','SERVICE','AUTOMATION','AI_AGENT') NOT NULL,
  actor_role          VARCHAR(100) NOT NULL,
  tenant_id           UUID NOT NULL,
  domain_track        VARCHAR(50),
  source_ip           VARCHAR(64),        -- masked at storage
  device_fingerprint  VARCHAR(255),       -- hashed
  resource_type       VARCHAR(100),
  resource_id         UUID,
  action              VARCHAR(100) NOT NULL,
  result              ENUM('ALLOW','DENY','FAIL','ERROR') NOT NULL,
  session_id          UUID,
  correlation_id      UUID NOT NULL,
  access_purpose      VARCHAR(100),
  access_channel      VARCHAR(50),
  risk_level          ENUM('LOW','MEDIUM','HIGH','CRITICAL') DEFAULT 'LOW',
  previous_hash       CHAR(64),
  current_hash        CHAR(64) NOT NULL,
  schema_version      VARCHAR(10) DEFAULT '1.0'
  -- ABSOLUTELY NO UPDATE OR DELETE PERMITTED
);
CREATE INDEX idx_audit_tenant_timestamp ON audit_events(tenant_id, timestamp);
CREATE INDEX idx_audit_actor ON audit_events(actor_id, timestamp);
CREATE INDEX idx_audit_resource ON audit_events(resource_type, resource_id, timestamp);

-- Data Access Log (immutable, append-only)
CREATE TABLE data_access_logs (
  dal_event_id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timestamp           TIMESTAMPTZ(6) NOT NULL DEFAULT NOW(),
  actor_id            UUID NOT NULL,
  actor_type          ENUM('USER','SERVICE','AUTOMATION') NOT NULL,
  user_type           VARCHAR(100) NOT NULL,
  tenant_id           UUID NOT NULL,
  domain_track        VARCHAR(50),
  role_at_access      VARCHAR(100) NOT NULL,
  data_category       VARCHAR(100) NOT NULL,
  resource_id         UUID,
  access_type         ENUM('READ','WRITE','UPDATE','DELETE','EXPORT','BULK_QUERY') NOT NULL,
  access_purpose      VARCHAR(100) NOT NULL,
  access_channel      VARCHAR(50) NOT NULL,
  result              ENUM('SUCCESS','DENIED') NOT NULL,
  source_ip           VARCHAR(64),
  elevated_flag       BOOLEAN DEFAULT FALSE,
  previous_hash       CHAR(64),
  current_hash        CHAR(64) NOT NULL
  -- NO UPDATE OR DELETE PERMITTED
);

-- Evidence Objects (append-only)
CREATE TABLE compliance_evidence (
  evidence_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  entity_type         VARCHAR(100) NOT NULL,  -- 'MATCH' | 'CERT' | 'MODERATION' | ...
  entity_id           UUID NOT NULL,
  domain_track        VARCHAR(50) NOT NULL,
  evidence_type       VARCHAR(100) NOT NULL,
  content_hash        CHAR(64) NOT NULL,
  metadata_hash       CHAR(64) NOT NULL,
  integrity_state     ENUM('VALID','COMPROMISED','UNDER_REVIEW') DEFAULT 'VALID',
  legal_hold_id       UUID REFERENCES legal_holds(hold_id),
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  created_by          UUID NOT NULL,
  finalized_at        TIMESTAMPTZ,
  finalized_by        UUID,
  schema_version      VARCHAR(10) DEFAULT '1.0'
  -- finalized = append-only (annotations only, no content mutation)
);

-- Evidence Annotations (append-only additions to finalized evidence)
CREATE TABLE evidence_annotations (
  annotation_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  evidence_id         UUID NOT NULL REFERENCES compliance_evidence(evidence_id),
  annotation_type     ENUM('CORRECTION_NOTE','LEGAL_HOLD','APPEAL_OUTCOME','ADMIN_NOTE'),
  content             TEXT NOT NULL,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  created_by          UUID NOT NULL,
  audit_hash          CHAR(64) NOT NULL
);

-- Incidents (structured lifecycle)
CREATE TABLE compliance_incidents (
  incident_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  incident_type       VARCHAR(100) NOT NULL,
  severity_level      ENUM('P0','P1','P2','P3','P4') NOT NULL,
  status              ENUM('DETECTED','CONTAINED','EVIDENCE_PRESERVED',
                          'INVESTIGATED','NOTIFIED','REMEDIATED','REVIEWED','CLOSED') NOT NULL,
  affected_domain     VARCHAR(50),
  affected_tenant_ids UUID[] NOT NULL,
  affected_user_types TEXT[],
  detection_source    ENUM('MANUAL','AUTOMATED','AI','SIEM','EXTERNAL') NOT NULL,
  detection_signal_id UUID,
  confidence_score    DECIMAL(5,2),
  commander_id        UUID NOT NULL,      -- single commander, non-delegable
  related_incident_ids UUID[],
  blocking_incident_ids UUID[],
  detected_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  contained_at        TIMESTAMPTZ,
  resolved_at         TIMESTAMPTZ,
  closed_at           TIMESTAMPTZ,
  closed_by           UUID,
  root_cause          TEXT,
  post_incident_review JSONB,
  sealed_at           TIMESTAMPTZ,
  audit_hash          CHAR(64)
);

-- Moderation Records (immutable decisions)
CREATE TABLE moderation_records (
  moderation_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  content_type        VARCHAR(100) NOT NULL,
  content_id          UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  domain_track        VARCHAR(50),
  reported_by         UUID,
  report_reason       TEXT,
  original_content_hash CHAR(64) NOT NULL,  -- sealed copy of reported content
  moderator_id        UUID NOT NULL,
  decision            ENUM('APPROVED','REMOVED','ESCALATED','HIDDEN_PENDING') NOT NULL,
  decision_reason     TEXT NOT NULL,
  decided_at          TIMESTAMPTZ DEFAULT NOW(),
  appeal_submitted_at TIMESTAMPTZ,
  appeal_reviewer_id  UUID,               -- must differ from moderator_id
  appeal_decision     ENUM('UPHELD','OVERTURNED','PARTIAL'),
  appeal_decided_at   TIMESTAMPTZ,
  sealed_at           TIMESTAMPTZ,
  audit_hash          CHAR(64) NOT NULL
);

-- Disputes
CREATE TABLE dispute_records (
  dispute_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  dispute_type        ENUM('MATCH_SCORE','CERTIFICATION','MENTOR_MISCONDUCT',
                          'CONTENT','JOB_OFFER','FINANCIAL') NOT NULL,
  entity_type         VARCHAR(100) NOT NULL,
  entity_id           UUID NOT NULL,       -- match_id | cert_id | offer_id | etc.
  tenant_id           UUID NOT NULL,
  initiating_party_id UUID NOT NULL,
  responding_party_id UUID,
  severity            ENUM('P1','P2','P3') NOT NULL,
  status              ENUM('SUBMITTED','EVIDENCE_SEALED','UNDER_REVIEW',
                          'DECIDED','APPEALED','FINAL') NOT NULL,
  reviewer_id         UUID,               -- must differ from original decision-maker
  decision            ENUM('UPHELD','OVERTURNED','PARTIAL_RESOLUTION'),
  decision_reason     TEXT,
  decided_at          TIMESTAMPTZ,
  appeal_to_board     BOOLEAN DEFAULT FALSE,
  board_decision      ENUM('UPHELD','OVERTURNED'),
  board_decided_at    TIMESTAMPTZ,
  sealed_at           TIMESTAMPTZ,
  audit_hash          CHAR(64)
);

-- Data Subject Rights Requests
CREATE TABLE data_subject_requests (
  request_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id             UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  request_type        ENUM('ACCESS','ERASURE','PORTABILITY','RECTIFICATION',
                          'OBJECT','GRIEVANCE') NOT NULL,
  jurisdiction        VARCHAR(50) NOT NULL,
  status              ENUM('RECEIVED','IN_PROGRESS','COMPLETED','DENIED','ESCALATED'),
  received_at         TIMESTAMPTZ DEFAULT NOW(),
  sla_deadline        TIMESTAMPTZ NOT NULL,
  completed_at        TIMESTAMPTZ,
  completion_cert_url TEXT,           -- MinIO signed URL
  denied_reason       TEXT,
  audit_hash          CHAR(64)
);

-- Transparency Report Log
CREATE TABLE transparency_reports (
  report_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  report_period_start DATE NOT NULL,
  report_period_end   DATE NOT NULL,
  published_at        TIMESTAMPTZ,
  report_hash         CHAR(64),       -- verifiable integrity on public page
  content_url         TEXT,           -- MinIO signed URL (public read)
  generated_by        UUID NOT NULL,
  approved_by         UUID NOT NULL,
  status              ENUM('DRAFT','APPROVED','PUBLISHED')
);
```

---

## 1️⃣8️⃣ API CONTRACT REGISTRY (MANDATORY)

```
METHOD  PATH                                          AUTH              ENTITLEMENT
------  -------------------------------------------   ---------------   --------------------------
GET     /audit/events                                 Compliance/Admin  audit_read + MFA + scope
GET     /audit/events/{event_id}                      Compliance/Admin  audit_read + MFA
GET     /audit/data-access-logs                       Compliance/Admin  audit_read + MFA + scope
GET     /compliance/incidents                         Compliance/Ops    incident_read + MFA
POST    /compliance/incidents                         Ops/Auto          incident_create
PATCH   /compliance/incidents/{id}                    Commander         own_incident + MFA
GET     /compliance/evidence/{evidence_id}            Compliance        evidence_read + step-up
POST    /compliance/evidence/{id}/annotation          Compliance        evidence_annotate + MFA
GET     /compliance/legal-holds                       Compliance        legal_hold_read + MFA
POST    /compliance/legal-holds                       Compliance        legal_hold_create + dual-MFA
PATCH   /compliance/legal-holds/{id}/release          Compliance        legal_hold_release + dual-MFA
GET     /compliance/moderation                        Mod/Admin         moderation_read + MFA
POST    /compliance/moderation/{id}/decide            Moderator         moderation_write + MFA
GET     /compliance/disputes                          Compliance/Admin  dispute_read + MFA
POST    /compliance/disputes/{id}/decide              Reviewer          dispute_write + MFA
GET     /compliance/data-subject-requests             Compliance        dsr_read + MFA
POST    /compliance/data-subject-requests/{id}/fulfill Compliance       dsr_write + MFA
GET     /compliance/consent/{user_id}                 User/Compliance   own_consent_scope
POST    /compliance/consent/{user_id}/revoke          User              own_consent_scope
GET     /compliance/pii-unmask-requests               Compliance        pii_unmask_authority + MFA
POST    /compliance/pii-unmask-requests               Compliance        pii_unmask_authority + dual-MFA
GET     /compliance/vulnerability/cve                 Security Admin    vuln_read + MFA
PATCH   /compliance/vulnerability/cve/{id}            Security Admin    vuln_write + MFA
GET     /transparency/reports                         Public            None
GET     /transparency/reports/{id}                    Public            None
GET     /verify/certificate/{cert_id}                 Public            None (rate-limited)
GET     /verify/skill/{badge_id}                      Public            None (rate-limited)
GET     /verify/credential/{hash}                     Public            None (rate-limited)
GET     /compliance/ai-audit-trail                    Compliance        ai_audit_read + MFA
GET     /compliance/backup/restore-log                DevOps/Compliance  backup_read + MFA
```

---

## 1️⃣9️⃣ EVENT SCHEMA REGISTRY (MANDATORY)

```
EVENT                              PRODUCER              CONSUMERS
---------------------------------  --------------------   ------------------------------------------
audit.event.written                All services          Audit Log Store, SIEM, Anomaly Detector
audit.chain.integrity.failed       Audit Store           Security Officer, PagerDuty P0, Incident Agent
data.access.logged                 All services          DAL Store, SIEM
data.access.anomaly.detected       SIEM                  Security, Incident Agent, Admin Ops
pii.exposure.detected              Any service           Security, Incident Agent (P1), Admin Ops
pii.unmask.requested               Compliance            Dual-Approval Queue, Audit, Security Alert
pii.unmask.granted                 Compliance            Access Token Issuer, Audit
consent.captured                   Any service           Consent Store, Audit
consent.revoked                    User/Service          Processing Halt Engine, Deletion Trigger, Audit
deletion.initiated                 Deletion Agent         All data services, Notification, Audit
deletion.propagation.confirmed     Deletion Agent         Compliance Ledger, Notification, Audit
deletion.failed                    Deletion Agent         Incident Agent, Admin Ops, Audit
legal.hold.applied                 Compliance Agent       Deletion Halt Engine, All data services, Audit
legal.hold.released                Compliance Agent       Deletion Resume Engine, Audit
incident.detected                  SIEM / Any service     Incident Store, Commander Queue, Audit
incident.classified                Incident Agent         Domain Leads, Notification, Audit
incident.contained                 Commander              Evidence Sealer, Legal Hold Agent, Audit
incident.evidence.compromised      Audit Verifier         PagerDuty P0, Security Officer, Audit
incident.closed                    Commander              Post-Incident Scheduler, Audit, SIEM
moderation.decision.made           Moderator              User Notification, Dispute Engine, Audit
moderation.appeal.decided          Senior Moderator        User Notification, Audit
dispute.submitted                  User/Tenant Admin       Dispute Queue, Evidence Sealer, Audit
dispute.decision.made              Reviewer                Corrective Action Engine, Notification, Audit
ai.output.generated                AI Service             AI Audit Trail, Bias Monitor, Audit
ai.bias.flag.raised                Bias Monitor            Compliance Officer, AI Governance, Audit
ai.model.frozen                    Incident Commander      AI Inference Engine (halt), Audit
vulnerability.detected             Scanner                 CVE Record Store, Security Alert, Audit
vulnerability.critical.detected    Scanner                 BUILD BLOCKER, PagerDuty P1, Audit
breach.notification.required       Incident Commander      Regulatory Notifier, User Notifier, Audit
transparency.report.published      Compliance Agent        Public CDN, Integrity Hasher, Audit
```

---

## 2️⃣0️⃣ ADMIN GOVERNANCE CONSOLE (MANDATORY MODULES)

```
COMPLIANCE CONSOLE — all screens require MFA + audit-logged access:

Security & Audit:
  ✅ Audit Event Explorer (filterable, read-only, tenant-scoped)
  ✅ Data Access Log Viewer (with elevated-access filter)
  ✅ Audit Chain Integrity Status (live — should always show GREEN)
  ✅ PII Exposure Alert Feed
  ✅ PII Unmask Request Queue (dual-approval UI)

Evidence & Legal:
  ✅ Evidence Registry Browser (hash-verified access)
  ✅ Legal Hold Manager (create · monitor · release)
  ✅ Evidence Annotation Panel

Incident Response:
  ✅ Incident Dashboard (live P0/P1 incidents highlighted)
  ✅ Incident Lifecycle Manager (state transitions)
  ✅ Post-Incident Review Tracker
  ✅ Dojo Live Incident Freeze Panel (real-time, P1 priority)

Moderation & Disputes:
  ✅ Content Moderation Queue
  ✅ Appeal Review Panel (anti-conflict-of-interest routing)
  ✅ Dispute Review Panel (tied to entity IDs)
  ✅ Moderator Activity Monitor (overturn rate, consistency)
  ✅ Emergency Content Takedown Switch (Super Admin + MFA)

Compliance & Regulatory:
  ✅ Data Subject Rights Request Queue (with SLA tracker)
  ✅ Consent Audit Viewer (per user, per scope)
  ✅ Deletion Status Tracker (per-layer propagation view)
  ✅ Breach Notification Drafting Tool (human-in-loop)
  ✅ Regulatory Escalation Tracker

AI Governance:
  ✅ AI Audit Trail Explorer
  ✅ Bias Flag Review Queue
  ✅ Model Version Freeze Panel
  ✅ Human Override History

Vulnerability:
  ✅ CVE Dashboard (by severity, by service)
  ✅ Patch Status Tracker
  ✅ SIEM Alert Feed

Transparency:
  ✅ Transparency Report Builder (draft → approve → publish)
  ✅ Public Verification API Monitor

Access Rules:
  - Super Admin: Full access (MFA + IP allowlist)
  - Compliance Officer: Evidence, Legal Hold, DSR, Breach, AI Governance (MFA)
  - Security Admin: Audit, Vulnerability, SIEM, Incident (MFA)
  - Moderator: Moderation Queue, Appeal (MFA)
  - AI agents: NO ACCESS TO COMPLIANCE CONSOLE
```

---

## 2️⃣1️⃣ OBSERVABILITY & ALERTING (MANDATORY)

### Prometheus Metrics

```
audit_events_total                        (counter, by category, by tenant)
audit_chain_integrity_valid               (gauge: 1=valid, 0=BROKEN → P0)
audit_pipeline_failure_total              (counter — CRITICAL if > 0)
data_access_logs_total                    (counter, by access_type)
pii_exposure_events_total                 (counter — CRITICAL if > 0)
pii_unmask_requests_total                 (counter)
consent_revocations_total                 (counter, by scope)
deletion_propagation_success_rate         (gauge — target: 100%)
legal_holds_active_total                  (gauge)
incident_count_by_severity                (gauge, by P0/P1/P2/P3/P4)
incident_mttc_seconds                     (histogram — Mean Time to Contain)
incident_mttr_seconds                     (histogram — Mean Time to Resolve)
incident_recurrence_rate                  (gauge)
moderation_queue_depth                    (gauge)
moderation_overturn_rate_by_moderator     (gauge — alert if > 20%)
dispute_sla_breach_total                  (counter)
dsr_sla_breach_total                      (counter)
ai_bias_flags_total                       (counter)
ai_model_frozen_total                     (counter)
vulnerability_critical_open_total         (gauge — must be 0 in prod)
transparency_report_overdue               (gauge)
```

### Critical Alerts

```
P0 IMMEDIATE:
  - Audit chain integrity broken (hash mismatch detected)
  - Cross-tenant data isolation violation
  - PII exposure detected in any log or response
  - Evidence object integrity COMPROMISED
  - Active legal hold deletion attempt
  - Critical CVE in production service (CVSS ≥ 9.0)
  - Audit pipeline silent failure

P1 HIGH:
  - Dojo live harassment incident (real-time response required)
  - Financial incident (unauthorized payout/manipulation)
  - Audit log access by unauthorized actor
  - Mass data access anomaly (>1000 records in <5 min, off-hours)
  - Breach notification threshold triggered
  - AI model producing biased outputs (confirmed)

WARNING:
  - Moderation queue depth > 100 items
  - DSR SLA at 80% of window without completion
  - Moderator overturn rate > 20%
  - Legal hold review date passed without renewal
  - Vulnerability (HIGH severity) unpatched > 72 hours
```

---

## 2️⃣2️⃣ FOUR-STAGE ROLLOUT (SEQUENTIAL — NO SKIPPING)

```
STAGE 1 — FOUNDATION
  ✅ Audit event logging (all categories) — immutable, append-only
  ✅ Data access logging — all data stores
  ✅ PII masking engine — server-side, role-aware
  ✅ Basic consent capture — recording, certification
  ✅ Basic incident detection + classification
  ✅ Hash chain integrity on audit log

STAGE 2 — INTELLIGENCE
  ✅ SIEM integration (Wazuh) — real-time streaming
  ✅ Anomaly detection on access patterns
  ✅ AI audit trail (model version, bias monitoring)
  ✅ Compliance dashboards (Grafana: audit + incident + PII)
  ✅ Moderation queue + automated pre-screening

STAGE 3 — ECOSYSTEM
  ✅ Legal hold management (full lifecycle)
  ✅ Evidence management + chain of custody
  ✅ Dispute governance (all dispute types)
  ✅ Data subject rights engine (GDPR/DPDP)
  ✅ Anonymous complaint system
  ✅ Government escalation router
  ✅ Backup compliance (restore authorization + drill tracking)
  ✅ Controlled PII unmasking protocol

STAGE 4 — COMPLIANCE & SCALE
  ✅ Full regulatory jurisdiction engine (GDPR/DPDP/FERPA/COPPA)
  ✅ Breach notification automation (draft + human-in-loop)
  ✅ 7-year retention + archive pipeline (cold storage)
  ✅ Public transparency reporting (quarterly publish cycle)
  ✅ Public verification API (certificate, skill, credential)
  ✅ CVE governance pipeline (scan → triage → patch → audit)
  ✅ Vulnerability SIEM integration
  ✅ Third-party compliance (DPA, sub-processor audit)
  ✅ Full compliance console (all modules operational)

Stage skipping = INVALID BUILD → STOP EXECUTION
```

---

## 2️⃣3️⃣ PRODUCTION READINESS CHECKLIST

```
Before AUDIT_COMPLIANCE_AGENT declared PRODUCTION-READY:

Audit Logging:
  ☐ Immutable audit_events table — no UPDATE/DELETE possible (tested)
  ☐ Hash chain verified on 10,000 test entries
  ☐ Cross-tenant audit isolation confirmed (penetration test passed)
  ☐ Audit pipeline failure alert firing correctly
  ☐ PII-free log verified (automated scan of log outputs)

Evidence & Legal:
  ☐ Evidence immutability tested (mutation attempt = HARD FAIL)
  ☐ Legal hold deletion block tested
  ☐ Dual-approval flow for legal hold tested end-to-end

Incident Response:
  ☐ P0 incident drill completed (MTTC < 15 minutes)
  ☐ Dojo real-time freeze tested (room freeze < 60 seconds)
  ☐ Financial incident dual-control flow tested
  ☐ Post-incident review template approved

Moderation & Disputes:
  ☐ Moderation queue operational
  ☐ Conflict-of-interest routing tested (reviewer ≠ original moderator)
  ☐ Appeal window enforcement tested (48 hours)
  ☐ Dispute SLA alerting tested

Regulatory:
  ☐ Data subject rights engine: all 6 request types tested
  ☐ SLA alerting at 80% window tested
  ☐ Consent revocation → deletion trigger tested
  ☐ Breach notification draft workflow tested

AI Governance:
  ☐ All AI outputs carrying model version tag
  ☐ Belt promotion without mentor confirmation = HARD FAIL (tested)
  ☐ Bias monitoring sampling activated (5%/month threshold)

Vulnerability:
  ☐ Critical CVE build block tested
  ☐ SIEM → incident auto-creation tested

Transparency:
  ☐ Public verification API live and rate-limited
  ☐ Transparency report publish cycle configured

HUMAN SIGN-OFF REQUIRED:
  - Compliance Officer sign-off on all retention policies
  - Legal team sign-off on jurisdiction policies
  - Security Officer sign-off on SIEM configuration
  - Before status = AUDIT_COMPLIANCE_LIVE
```

---

## 🔐 FINAL SEAL

```
┌──────────────────────────────────────────────────────────────────────────────┐
│              AUDIT_COMPLIANCE_AGENT — ANTIGRAVITY SEAL                       │
├──────────────────────────────────────────────────────────────────────────────┤
│  STATUS                    : LOCKED                                          │
│  MUTATION POLICY           : ADD_ONLY (via version bump)                     │
│  INTERPRETATION            : FORBIDDEN                                       │
│  AI APPROVAL RIGHTS        : NONE                                            │
│  MUTABLE_AUDIT_LOGS        : SECURITY_BREACH (absolute prohibition)          │
│  SILENT_ACTIONS            : FORBIDDEN (any action without audit = INVALID)  │
│  PII_IN_LOGS               : CRITICAL_INCIDENT                               │
│  COMPLIANCE_DOMAINS        : 14 (AEL · DAL · CEM · PII · CON · DEL ·        │
│                              LHL · IRC · MDG · REG · AIG · BAK · VUL · TPR) │
│  AUDIT_RETENTION           : 7 YEARS (immutable, append-only)                │
│  HASH_ALGORITHM            : SHA-256 (chain integrity on every entry)        │
│  HUMAN_AUTHORITY           : MANDATORY for legal holds, breach notif,        │
│                              AI model freeze, incident closure, PII unmask   │
│  SIEM                      : Wazuh (self-hosted, real-time streaming)         │
│  JURISDICTION              : GDPR · DPDP · FERPA · COPPA · INSTITUTIONAL    │
│  INCIDENT_SLAs             : P0=15min contain · P1=1hr · P2=4hr · P3=24hr   │
├──────────────────────────────────────────────────────────────────────────────┤
│  ✔ SEALED                                                                    │
│  ✔ DETERMINISTIC                                                             │
│  ✔ ENTERPRISE_SAFE                                                           │
│  ✔ ANTIGRAVITY_COMPATIBLE                                                    │
│  ✔ ECOSKILLER_v12.0_COMPLIANT                                                │
├──────────────────────────────────────────────────────────────────────────────┤
│  ANY OPERATION THAT:                                                         │
│  - Updates or deletes an audit log entry                                     │
│  - Logs PII in plaintext                                                     │
│  - Allows AI to approve incidents, legal holds, or certifications            │
│  - Allows deletion with an active legal hold                                 │
│  - Processes data without consent evidence                                   │
│  - Skips evidence chain for belt/certification issuance                      │
│  - Allows remediation before containment                                     │
│  - Allows cross-tenant audit log visibility                                  │
│  - Produces an AI-generated belt without mentor confirmation                 │
│  - Deploys a build with CRITICAL/HIGH CVE unresolved                         │
│  - Produces audit chain hash mismatch without P0 alert                       │
│                                                                              │
│  ⇒ MUST BE REJECTED → STOP EXECUTION → REPORT VIOLATION                     │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

*AUDIT_COMPLIANCE_AGENT.md · ECOSKILLER Platform · Antigravity Execution Engine*
*Version: 1.0.0 · Classification: LOCKED · Append-only via version bump*
*Governance Lane D — produces: governance_ready · compliance_certified*
*Cross-cutting concern: wired into ALL microservices, ALL data stores, ALL event consumers*
