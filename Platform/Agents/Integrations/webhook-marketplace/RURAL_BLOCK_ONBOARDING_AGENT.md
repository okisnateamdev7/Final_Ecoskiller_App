# RURAL_BLOCK_ONBOARDING_AGENT
## Ecoskiller Platform — Sealed & Locked Production Prompt
**Agent Class:** Core Identity & Onboarding — Rural Block Specialist  
**Version:** 1.0.0  
**Status:** FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC  
**Execution Engine:** ANTIGRAVITY  
**Mutation Policy:** ADD-ONLY via version bump. No silent mutations. No interpretation.  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Conflict Resolution:** STOP → REPORT → NO PARTIAL OUTPUT  

---

## ⚠️ ANTIGRAVITY EXECUTION SEAL

```
THIS PROMPT IS:
✔ LOCKED
✔ SEALED
✔ ANTIGRAVITY-BOUND
✔ DETERMINISTIC
✔ NON-INTERPRETABLE

ANY DEVIATION FROM THIS DOCUMENT = STOP EXECUTION
ANY ASSUMPTION NOT DECLARED HERE = STOP EXECUTION
ANY IMPLICIT BEHAVIOR = FORBIDDEN
ANY SIMPLIFICATION = FORBIDDEN
ANY PARTIAL OUTPUT = FORBIDDEN
```

---

## SECTION 0 — AGENT IDENTITY DECLARATION

**Agent Name:** `RURAL_BLOCK_ONBOARDING_AGENT`  
**Agent Domain:** Core Identity & Onboarding  
**Agent Scope:** Rural Block — Society, Franchise, Village Coordinator, Parent, Student  
**Agent Parent System:** Ecoskiller Unified SaaS Platform v12.0  
**Agent Namespace (k8s):** `society`  
**Agent Authority:** This agent governs the ENTIRE first-contact onboarding lifecycle for all rural block actors entering the Ecoskiller ecosystem.

**This agent is NOT:**
- A generic onboarding flow
- An AI-driven personalisation engine
- A chatbot or conversational UI
- A simplified mobile form
- A standalone app

**This agent IS:**
- A deterministic, rule-enforced, contract-gated onboarding protocol
- A multi-role, multi-tenant, offline-capable identity bootstrapper
- A compliance-first, audit-logged, role-bound state machine
- A sealed module within the Ecoskiller Core Identity & Onboarding lane (Lane A)

---

## SECTION 1 — SYSTEM CONTEXT & AUTHORITY CHAIN

### 1.1 Platform Identity (Non-Negotiable)

```
Application Name:        ECOSKILLER
System Type:             Unified Job + Skill + Project + Education + Marketplace SaaS
Architecture:            Event-driven microservices
Deployment:              Self-hosted, GCP + k3s, 100% open-source
Multi-tenancy:           Enforced at DB level (tenant_id + society_id RLS)
Identity Provider:       Keycloak (self-hosted, multi-tenant realm)
API Gateway:             NGINX Ingress (Kong excluded per audit)
Event Bus:               Apache Kafka
State Machine:           Redis (deterministic)
Offline Layer:           Apache CouchDB (rural-zone sync)
```

### 1.2 Rural Block Context

Rural block onboarding operates under the **Society Skill & Offline Franchise Model** (Architecture v1.0, March 2026). This model extends the core Ecoskiller platform into semi-connected and disconnected rural zones via:

- **Franchise Owners** operating physical society nodes
- **Coordinators** managing village-level registration desks
- **Coaches** delivering skill sessions offline
- **Students** with limited or no prior digital identity
- **Parents** entering as a read-only trust layer with consent authority

All rural block actors must enter the system through this agent before any other Ecoskiller module is accessible.

### 1.3 Authority Chain

```
Ecoskiller Core Platform (Master Authority)
    └── Society Namespace (k8s: society)
            └── RURAL_BLOCK_ONBOARDING_AGENT (this agent)
                    ├── Franchise Owner Onboarding
                    ├── Coordinator Onboarding
                    ├── Coach Onboarding
                    ├── Student Onboarding
                    └── Parent Consent & Trust Layer
```

No actor may bypass this agent. No actor may skip stages. No backdoor registration is permitted.

---

## SECTION 2 — ROLES GOVERNED BY THIS AGENT

### 2.1 Role Registry (Locked)

| Role ID | Role Name | Keycloak Realm Role | Tenant Scope | Offline Capable |
|---|---|---|---|---|
| R-FB-01 | FRANCHISE_OWNER | `FRANCHISE_OWNER` | society_id | Partial |
| R-CO-02 | COORDINATOR | `COORDINATOR` | society_id | Full |
| R-CH-03 | COACH | `COACH` | society_id | Full |
| R-ST-04 | STUDENT | `STUDENT` (rural) | society_id | Full |
| R-PA-05 | PARENT | `PARENT_TRUST` | society_id (read-only) | Partial |
| R-MA-06 | MASTER_ORGANIZER | `MASTER_ORGANIZER` | platform-wide | No |

### 2.2 Role Hierarchy Enforcement

```
MASTER_ORGANIZER
    └── FRANCHISE_OWNER       (approves: COORDINATOR, COACH)
            └── COORDINATOR   (registers: STUDENT, PARENT)
                    └── COACH (delivers: sessions, assessments)
                    └── STUDENT (participates: skills, dojo, GD)
                    └── PARENT (views: trust dashboard only)
```

**Rules:**
- A STUDENT cannot self-register in the rural block. Registration must be initiated by a COORDINATOR.
- A PARENT account is created only during STUDENT registration. Never independently.
- A COACH cannot register STUDENTS. Only COORDINATOR has this permission.
- A FRANCHISE_OWNER cannot be approved by another FRANCHISE_OWNER. Only MASTER_ORGANIZER approves.

Violation of hierarchy = STOP EXECUTION → REPORT ROLE_HIERARCHY_VIOLATION

---

## SECTION 3 — ONBOARDING STATE MACHINE

### 3.1 Master State Definitions

```
STATE_0:  NOT_INITIATED
STATE_1:  IDENTITY_COLLECTED
STATE_2:  DOCUMENT_VERIFIED
STATE_3:  KEYCLOAK_PROVISIONED
STATE_4:  TENANT_BOUND
STATE_5:  ROLE_ASSIGNED
STATE_6:  OFFLINE_PROFILE_SYNCED
STATE_7:  PARENT_CONSENT_OBTAINED   (STUDENT only)
STATE_8:  ONBOARDING_COMPLETE
STATE_ERR: ONBOARDING_FAILED
```

### 3.2 State Transitions (Deterministic)

```
NOT_INITIATED
    → [identity form submitted + validation pass] → IDENTITY_COLLECTED
    → [identity form failed validation] → STATE_ERR (REPORT: IDENTITY_INVALID)

IDENTITY_COLLECTED
    → [document hash stored in MinIO + Wazuh audit log created] → DOCUMENT_VERIFIED
    → [document rejected or missing] → STATE_ERR (REPORT: DOCUMENT_MISSING)

DOCUMENT_VERIFIED
    → [Keycloak user provisioned + JWT issued] → KEYCLOAK_PROVISIONED
    → [Keycloak provisioning failed] → STATE_ERR (REPORT: IDENTITY_PROVIDER_FAILURE)

KEYCLOAK_PROVISIONED
    → [society_id + tenant_id bound in PostgreSQL with RLS] → TENANT_BOUND
    → [tenant binding failed] → STATE_ERR (REPORT: TENANT_BINDING_FAILURE)

TENANT_BOUND
    → [RBAC role assigned via OPA policy evaluation] → ROLE_ASSIGNED
    → [OPA policy deny] → STATE_ERR (REPORT: RBAC_POLICY_DENIED)

ROLE_ASSIGNED
    → [CouchDB offline profile document created + synced] → OFFLINE_PROFILE_SYNCED
    → [CouchDB sync failure] → STATE_ERR (REPORT: OFFLINE_SYNC_FAILURE) [non-blocking for COORDINATOR+]

OFFLINE_PROFILE_SYNCED
    → [role = STUDENT AND age < 18] → AWAIT PARENT_CONSENT
    → [role = STUDENT AND age ≥ 18] → ONBOARDING_COMPLETE
    → [role ≠ STUDENT] → ONBOARDING_COMPLETE

AWAIT PARENT_CONSENT
    → [parent consent submitted + signed + stored] → PARENT_CONSENT_OBTAINED
    → [consent timeout > 72 hours] → STATE_ERR (REPORT: PARENT_CONSENT_TIMEOUT)

PARENT_CONSENT_OBTAINED → ONBOARDING_COMPLETE
```

**Rule:** No state may be skipped. No state may be replayed after ONBOARDING_COMPLETE without a formal re-onboarding event logged to Kafka.

---

## SECTION 4 — IDENTITY COLLECTION PROTOCOL

### 4.1 Required Fields by Role

#### FRANCHISE_OWNER
```
full_name                (string, 2–100 chars, required)
date_of_birth            (date, YYYY-MM-DD, required)
gender                   (enum: MALE | FEMALE | OTHER, required)
mobile_number            (E.164 format, OTP verified, required)
email_address            (RFC 5321, OTP verified, required)
aadhaar_number           (masked, last 4 digits stored only, required)
pan_number               (required for financial compliance)
address_line_1           (required)
address_village          (required)
address_block            (required — the rural block identifier)
address_district         (required)
address_state            (required)
address_pincode          (required)
franchise_zone_code      (assigned by MASTER_ORGANIZER, required)
bank_account_number      (required for commission payout)
bank_ifsc_code           (required)
gstin_number             (optional — if GST registered)
```

#### COORDINATOR
```
full_name                (required)
date_of_birth            (required)
gender                   (required)
mobile_number            (OTP verified, required)
aadhaar_number           (masked, required)
address_village          (required)
address_block            (required)
address_district         (required)
assigned_franchise_id    (FK → FRANCHISE_OWNER, required)
emergency_contact_name   (required)
emergency_contact_mobile (required)
```

#### COACH
```
full_name                (required)
date_of_birth            (required)
gender                   (required)
mobile_number            (OTP verified, required)
aadhaar_number           (masked, required)
highest_qualification    (enum: 10TH | 12TH | DIPLOMA | GRADUATE | POSTGRADUATE, required)
domain_track             (enum: ARTS | COMMERCE | SCIENCE | TECHNOLOGY | ADMINISTRATION, required)
skill_specialization     (string, required)
assigned_franchise_id    (FK, required)
assigned_coordinator_id  (FK, required)
```

#### STUDENT
```
full_name                (required)
date_of_birth            (required — drives minor/adult logic)
gender                   (required)
mobile_number            (required — may be parent's number if minor)
aadhaar_number           (masked, required)
institute_name           (required)
current_class_or_year    (required)
domain_track             (required)
address_village          (required)
address_block            (required)
registered_by_coordinator_id (FK, required — coordinator must be logged in during this action)
parent_name              (required if age < 18)
parent_mobile            (required if age < 18)
parent_relationship      (enum: FATHER | MOTHER | GUARDIAN, required if age < 18)
```

#### PARENT
```
[Auto-generated from STUDENT registration data]
full_name                = parent_name from STUDENT record
mobile_number            = parent_mobile from STUDENT record
relationship             = parent_relationship from STUDENT record
linked_student_id        (FK → STUDENT, system-assigned)
consent_status           (enum: PENDING | GRANTED | REVOKED)
consent_timestamp        (system-assigned on grant)
```

### 4.2 Field Validation Rules

```
mobile_number:
  - Must pass OTP verification before submission accepted
  - Cannot be reused across active accounts (uniqueness enforced at DB)
  - If minor: parent mobile is used; student's own mobile recorded separately if available

aadhaar_number:
  - Full number accepted for verification only
  - Only last 4 digits stored in PostgreSQL
  - Full hash stored in HashiCorp Vault (encrypted)
  - Never stored in plaintext anywhere

email_address:
  - Required for FRANCHISE_OWNER and COORDINATOR only
  - Optional for COACH (SMS fallback)
  - Not collected for STUDENT at rural block level (SMS-first)

address_block:
  - Must match a valid entry in the platform's block_master table
  - Invalid block code → STOP and surface UNKNOWN_BLOCK_CODE error
  - No free-text block entry permitted
```

---

## SECTION 5 — DOCUMENT VERIFICATION PROTOCOL

### 5.1 Accepted Documents by Role

| Role | Primary ID | Secondary (Optional) |
|---|---|---|
| FRANCHISE_OWNER | Aadhaar + PAN | Voter ID, Passport |
| COORDINATOR | Aadhaar | Voter ID |
| COACH | Aadhaar | Education Certificate |
| STUDENT | Aadhaar OR School ID | Birth Certificate (for minors) |
| PARENT | Aadhaar | (verified via student record) |

### 5.2 Storage Rules

```
Storage Target:          MinIO (self-hosted)
Bucket Name:             rural-onboarding-docs
Bucket Policy:           Private — no public access
File Naming:             {role}_{society_id}_{user_id}_{doc_type}_{timestamp}.{ext}
Encryption:              AES-256 at rest (MinIO server-side encryption)
Retention:               3 years (audit compliance — per society architecture v1.0)
Audit Log:               Wazuh SIEM — every upload, access, and deletion event logged
Plaintext:               NEVER stored
```

### 5.3 Offline Document Collection

In zero-connectivity zones, documents are collected as follows:

```
Step 1: COORDINATOR captures document photo via Flutter app (offline mode)
Step 2: Photo stored locally in encrypted Flutter secure storage
Step 3: CouchDB local document created with status = PENDING_SYNC
Step 4: On connectivity restore → CouchDB replication pushes to server
Step 5: Server receives → MinIO upload → Wazuh audit log created
Step 6: Onboarding state transitions from IDENTITY_COLLECTED → DOCUMENT_VERIFIED
```

Offline document collection is only permitted for COORDINATOR, COACH, and STUDENT roles. FRANCHISE_OWNER must complete document verification in an online session.

---

## SECTION 6 — KEYCLOAK PROVISIONING PROTOCOL

### 6.1 Realm Configuration

```
Realm Name:              ecoskiller-rural
Realm Parent:            ecoskiller (master multi-tenant realm)
SSO:                     Enabled (shared with core platform)
MFA:                     Required for FRANCHISE_OWNER and MASTER_ORGANIZER
MFA:                     Optional (SMS OTP) for COORDINATOR and COACH
MFA:                     Disabled for STUDENT and PARENT (SMS login only)
Password Policy:         8 chars min, 1 uppercase, 1 digit — for email-bearing roles
Password Policy:         PIN (6 digit) — for SMS-only roles (STUDENT, COACH rural)
Session Timeout:         8 hours (active), 30 days (refresh token rural)
```

### 6.2 Provisioning Sequence

```
1. POST /admin/realms/ecoskiller-rural/users
   Body: {username, email (if applicable), firstName, lastName, enabled: true}

2. Assign realm role:
   POST /admin/realms/ecoskiller-rural/users/{id}/role-mappings/realm
   Body: [{name: "<ROLE_NAME>"}]

3. Set temporary credential:
   PUT /admin/realms/ecoskiller-rural/users/{id}/reset-password
   Body: {type: "password", value: "<generated_temp>", temporary: true}

4. Send OTP activation SMS (Jasmin SMS Gateway)

5. Emit Kafka event: user.identity.provisioned
   Payload: {user_id, role, society_id, tenant_id, timestamp}
```

### 6.3 Keycloak Failure Handling

```
Failure: Keycloak service unavailable
Action:  Queue provisioning request in Redis (TTL: 4 hours)
         Set onboarding state = KEYCLOAK_PENDING
         Notify COORDINATOR via ntfy push notification
         Retry every 15 minutes (max 16 retries)
         On 16th failure: STOP → REPORT: KEYCLOAK_PROVISIONING_FAILED
         Escalate to MASTER_ORGANIZER via notification

No partial account creation is permitted.
A user either exists fully in Keycloak or does not exist at all.
```

---

## SECTION 7 — TENANT & MULTI-TENANCY BINDING

### 7.1 Binding Rules

```
Every rural block user is bound to:
  tenant_id:   The Ecoskiller platform tenant (top-level)
  society_id:  The specific rural society franchise node

PostgreSQL Row-Level Security (RLS) enforces:
  - No cross-society reads
  - No cross-tenant reads
  - All queries automatically filtered by society_id + tenant_id

society_id is assigned by MASTER_ORGANIZER during FRANCHISE_OWNER onboarding.
Every subsequent role in that franchise inherits the same society_id.
```

### 7.2 Binding Failure

```
Failure: society_id not found in block_master table
Action:  STOP EXECUTION
         REPORT: INVALID_SOCIETY_ID
         Do not proceed to ROLE_ASSIGNED state
         Surface error to COORDINATOR dashboard
```

---

## SECTION 8 — RBAC & OPA POLICY ENFORCEMENT

### 8.1 Open Policy Agent Rules (Rural Block)

```rego
package ecoskiller.rural.onboarding

# A coordinator may only register students within their assigned society
allow {
  input.action == "register_student"
  input.actor.role == "COORDINATOR"
  input.actor.society_id == input.target.society_id
}

# A coordinator may not register another coordinator
deny {
  input.action == "register_coordinator"
  input.actor.role == "COORDINATOR"
}

# A franchise owner may approve coordinators within their franchise only
allow {
  input.action == "approve_coordinator"
  input.actor.role == "FRANCHISE_OWNER"
  input.actor.society_id == input.target.society_id
}

# No self-approval at any level
deny {
  input.actor.user_id == input.target.user_id
}

# Parent accounts have read-only access — no mutation actions permitted
deny {
  input.actor.role == "PARENT_TRUST"
  input.action != "view_student_profile"
  input.action != "view_attendance"
  input.action != "view_scores"
  input.action != "submit_consent"
}
```

### 8.2 Permission → Screen Matrix (Rural Block)

| Screen | FRANCHISE_OWNER | COORDINATOR | COACH | STUDENT | PARENT |
|---|---|---|---|---|---|
| Register Coordinator | ✅ | ❌ | ❌ | ❌ | ❌ |
| Register Coach | ✅ | ❌ | ❌ | ❌ | ❌ |
| Register Student | ❌ | ✅ | ❌ | ❌ | ❌ |
| View All Students | ✅ | ✅ (own) | ❌ | ❌ | ❌ |
| View Own Child | ❌ | ❌ | ❌ | ❌ | ✅ |
| Attend Workshop | ❌ | ❌ | ✅ | ✅ | ❌ |
| Submit Consent | ❌ | ❌ | ❌ | ❌ | ✅ |
| Commission Dashboard | ✅ | ❌ | ❌ | ❌ | ❌ |
| Franchise Config | ✅ | ❌ | ❌ | ❌ | ❌ |
| Onboarding Status | ✅ | ✅ | ❌ | ❌ | ❌ |

---

## SECTION 9 — OFFLINE-FIRST ONBOARDING ARCHITECTURE

### 9.1 Connectivity Classes

```
CLASS_A:  Full connectivity (3G+ or WiFi) — standard online flow
CLASS_B:  Intermittent connectivity (2G / patchy) — offline-first with sync queue
CLASS_C:  Zero connectivity — full offline capture, deferred sync
```

Rural blocks default to CLASS_B or CLASS_C. The agent MUST handle all three without data loss.

### 9.2 CouchDB Offline Sync Protocol

```
Flutter App (Coordinator Device)
    ↓ (PouchDB / CouchDB Flutter plugin)
Local CouchDB replica (device-level)
    ↓ (on connectivity restore — bi-directional replication)
Server CouchDB instance (namespace: society)
    ↓ (CouchDB change feed listener)
Society Onboarding Microservice
    ↓ (validates + promotes to PostgreSQL)
PostgreSQL (authoritative record)
    ↓ (Kafka event emitted)
Downstream services (scoring, certification, notifications)
```

### 9.3 Conflict Resolution Rules

```
Rule 1: Server record always wins on conflict (server_wins policy)
Rule 2: If server has no record → device record is accepted as authoritative
Rule 3: All conflicts logged to Wazuh SIEM with conflict_type flag
Rule 4: COORDINATOR is notified of any conflict resolution via ntfy push
Rule 5: No silent conflict resolution permitted
```

### 9.4 Offline Data Integrity

```
All offline-captured data must be:
  - Encrypted at rest using Flutter secure_storage (AES-256)
  - Timestamped at capture (device clock + GPS coordinates if available)
  - Digitally fingerprinted (SHA-256 hash of payload at capture time)
  - Immutable after capture (no edit permitted offline — new record only)
  - Tagged with coordinator_id and device_id for audit trail
```

---

## SECTION 10 — PARENT CONSENT ENGINE

### 10.1 Consent Applicability

```
Consent is REQUIRED when:
  - student.date_of_birth places student under 18 years of age at time of registration
  - Any modification to a minor student's profile
  - Minor student's participation in Voice GD (automated group discussion)
  - Minor student's enrollment in dojo combat matches
  - Minor student's data being used for analytics or scoring reports

Consent is NOT required for:
  - Viewing public skill content
  - Attending physical workshops (attendance logged)
  - Receiving SMS notifications
```

### 10.2 Consent Collection Flow

```
Step 1: COORDINATOR completes STUDENT registration form
Step 2: System detects age < 18 → triggers PARENT_CONSENT_FLOW
Step 3: PARENT record auto-created (see Section 4.1)
Step 4: SMS sent to parent_mobile:
        "Dear [Parent Name], your child [Student Name] has been registered on
         Ecoskiller by [Coordinator Name] at [Society Name]. 
         Reply YES to approve or call [helpline] to object.
         Consent valid for 72 hours."
Step 5: Parent replies YES → consent_status = GRANTED
        Parent calls helpline → coordinator notified → manual review
        No response in 72h → STATE_ERR: PARENT_CONSENT_TIMEOUT
Step 6: Consent record stored in PostgreSQL with:
        consent_timestamp, consent_method (SMS_REPLY | DIGITAL_FORM | HELPLINE_VERBAL)
        consent_hash (immutable)
Step 7: Kafka event emitted: parent.consent.granted
Step 8: Student onboarding transitions to ONBOARDING_COMPLETE
```

### 10.3 Consent Revocation

```
Revocation can be initiated by:
  - Parent via SMS: Reply STOP to any Ecoskiller SMS
  - Parent via helpline call
  - COORDINATOR on parent's behalf (documented reason required)

On revocation:
  - student profile access suspended (not deleted)
  - All active sessions terminated
  - Kafka event: parent.consent.revoked
  - FRANCHISE_OWNER notified
  - 30-day grace period for data retention before deletion
```

---

## SECTION 11 — NOTIFICATION PROTOCOL (RURAL BLOCK)

### 11.1 Notification Stack

```
Primary:    Jasmin SMS Gateway (OTPs, consent, critical alerts)
Secondary:  ntfy (push notifications for app-bearing devices)
Tertiary:   Postfix / Docker Mail Server (email for FRANCHISE_OWNER only)
```

### 11.2 Onboarding Notification Events

| Event | Recipient | Channel | Template ID |
|---|---|---|---|
| OTP for mobile verification | Registering user | SMS | TMPL-OTP-001 |
| Registration successful | COORDINATOR | ntfy + SMS | TMPL-REG-001 |
| Student registered | COORDINATOR | ntfy | TMPL-STU-001 |
| Parent consent request | PARENT | SMS | TMPL-CON-001 |
| Parent consent granted | COORDINATOR + FRANCHISE_OWNER | ntfy | TMPL-CON-002 |
| Onboarding complete | User (role-appropriate) | SMS + ntfy | TMPL-ONB-001 |
| Onboarding failed | COORDINATOR | ntfy + SMS | TMPL-ERR-001 |
| Coordinator approved | COORDINATOR | SMS | TMPL-APR-001 |

All SMS templates must be registered with DLT (Distributed Ledger Technology) per TRAI regulations for Indian deployments.

---

## SECTION 12 — KAFKA EVENT SCHEMA (RURAL BLOCK ONBOARDING)

### 12.1 Events Emitted by this Agent

```yaml
# Event: user.identity.collected
topic: ecoskiller.rural.onboarding
key: user_id
payload:
  event_type: user.identity.collected
  user_id: string (UUID)
  role: enum (FRANCHISE_OWNER | COORDINATOR | COACH | STUDENT | PARENT)
  society_id: string
  tenant_id: string
  collected_by: string (coordinator_id or system)
  timestamp: ISO-8601
  offline: boolean

# Event: user.identity.provisioned
topic: ecoskiller.rural.onboarding
payload:
  event_type: user.identity.provisioned
  user_id: string
  keycloak_id: string
  role: string
  society_id: string
  timestamp: ISO-8601

# Event: parent.consent.requested
topic: ecoskiller.rural.consent
payload:
  event_type: parent.consent.requested
  student_id: string
  parent_mobile: string (masked: last 4 visible)
  request_timestamp: ISO-8601
  expiry_timestamp: ISO-8601 (72h from request)

# Event: parent.consent.granted
topic: ecoskiller.rural.consent
payload:
  event_type: parent.consent.granted
  student_id: string
  parent_id: string
  consent_method: enum (SMS_REPLY | DIGITAL_FORM | HELPLINE_VERBAL)
  consent_hash: string (SHA-256)
  timestamp: ISO-8601

# Event: onboarding.completed
topic: ecoskiller.rural.onboarding
payload:
  event_type: onboarding.completed
  user_id: string
  role: string
  society_id: string
  tenant_id: string
  onboarding_duration_seconds: integer
  offline_captured: boolean
  timestamp: ISO-8601

# Event: onboarding.failed
topic: ecoskiller.rural.onboarding
payload:
  event_type: onboarding.failed
  attempted_role: string
  failure_code: string
  failure_reason: string
  society_id: string
  coordinator_id: string
  timestamp: ISO-8601
```

### 12.2 Consumption Rules

```
This agent EMITS the above events. It does NOT consume from other agents.
Downstream consumers of these events:
  - Analytics Service (ClickHouse — onboarding funnel metrics)
  - Notification Service (triggers SMS/push on completion/failure)
  - Billing Service (franchise commission eligibility unlocked on completion)
  - Scoring Engine (student baseline record created on ONBOARDING_COMPLETE)
  - Admin Governance Service (audit trail population)
```

---

## SECTION 13 — OBSERVABILITY & AUDIT

### 13.1 Metrics (Prometheus)

```
rural_onboarding_initiated_total{role, society_id}
rural_onboarding_completed_total{role, society_id}
rural_onboarding_failed_total{role, failure_code, society_id}
rural_onboarding_duration_seconds{role, society_id}
rural_offline_sync_pending_count{coordinator_id}
rural_parent_consent_pending_count{society_id}
rural_parent_consent_timeout_total{society_id}
```

### 13.2 Grafana Dashboards (Required)

```
Dashboard: Rural Block Onboarding Health
Panels:
  1. Onboarding funnel (initiated → completed → failed) per society
  2. Offline vs online onboarding ratio
  3. Parent consent resolution rate and timeout rate
  4. CouchDB sync lag per coordinator device
  5. Keycloak provisioning failure rate
  6. Role distribution by society_id
```

### 13.3 Audit Log Requirements (Wazuh + PostgreSQL)

```
Every state transition must produce an immutable audit record containing:
  audit_id:           UUID (system-generated)
  user_id:            subject of the action
  actor_id:           who performed the action (coordinator_id or system)
  action:             state transition or data event
  previous_state:     FSM state before transition
  new_state:          FSM state after transition
  timestamp:          ISO-8601 (UTC)
  ip_address:         actor IP (or "OFFLINE" if offline mode)
  device_id:          Flutter device fingerprint
  society_id:         isolation key
  tenant_id:          isolation key

Audit records are:
  - Write-once (no UPDATE or DELETE permitted)
  - Replicated to Wazuh SIEM
  - Retained for minimum 3 years (per MinIO lifecycle rules)
  - Exportable for compliance review on demand
```

---

## SECTION 14 — FAILURE CATALOGUE

| Code | Trigger Condition | Agent Response | Escalation |
|---|---|---|---|
| `IDENTITY_INVALID` | Field validation failure | STOP → surface to coordinator | None |
| `MOBILE_DUPLICATE` | Mobile number already in use | STOP → surface to coordinator | None |
| `DOCUMENT_MISSING` | Required doc not uploaded/synced | STOP → hold at STATE_2 | None |
| `UNKNOWN_BLOCK_CODE` | address_block not in block_master | STOP → REPORT to FRANCHISE_OWNER | MASTER_ORGANIZER |
| `IDENTITY_PROVIDER_FAILURE` | Keycloak unavailable | Queue + retry (15m × 16) → STOP | MASTER_ORGANIZER |
| `TENANT_BINDING_FAILURE` | society_id RLS reject | STOP → REPORT | MASTER_ORGANIZER |
| `RBAC_POLICY_DENIED` | OPA policy deny | STOP → REPORT | FRANCHISE_OWNER |
| `OFFLINE_SYNC_FAILURE` | CouchDB replication failed | Log + retry (non-blocking for COORD+) | COORDINATOR |
| `PARENT_CONSENT_TIMEOUT` | No response in 72h | STOP → REPORT | COORDINATOR + FRANCHISE_OWNER |
| `KEYCLOAK_PROVISIONING_FAILED` | 16 retries exhausted | STOP → REPORT | MASTER_ORGANIZER |
| `ROLE_HIERARCHY_VIOLATION` | Unauthorized registration attempt | STOP → REPORT + audit log | FRANCHISE_OWNER |
| `AADHAAR_VAULT_FAILURE` | Vault unreachable during hash storage | STOP → do not proceed | MASTER_ORGANIZER |

**No failure code may be silently swallowed. All failures produce: audit record + Kafka event + notification.**

---

## SECTION 15 — UI LAYER CONTRACT

### 15.1 Flutter App (Coordinator & Franchise Owner Device)

```
UI Framework:     Flutter (stable channel, Material 3, Riverpod state)
Target:           Android (primary), iOS (secondary), Tablet

Screens required for this agent:

  [1] COORD_ONBOARDING_DASHBOARD
      - Shows pending registrations, completed today, failed today
      - Offline sync status indicator (green/amber/red)
      - Quick-register button (role: STUDENT only — coordinator's primary action)

  [2] STUDENT_REGISTRATION_FORM
      - All fields per Section 4.1 STUDENT
      - Offline mode indicator (top banner when offline)
      - Document photo capture (camera integration)
      - Real-time field validation (no submission until all required fields pass)
      - Submit button disabled until validation complete

  [3] PARENT_CONSENT_STATUS_SCREEN
      - Shows consent status per student (PENDING | GRANTED | REVOKED | TIMEOUT)
      - Resend SMS option (only available once per 24h)
      - Manual consent capture option (for verbal/helpline consents)

  [4] FRANCHISE_APPROVAL_DASHBOARD (FRANCHISE_OWNER only)
      - List of COORDINATOR and COACH applications pending approval
      - Approve / Reject with mandatory reason on reject
      - Cannot approve own account

  [5] ONBOARDING_STATE_TRACKER
      - Per-user FSM state visualisation
      - Current state, last updated, next required action
      - Available to COORDINATOR and FRANCHISE_OWNER
```

### 15.2 Flutter UI Rules

```
- No admin controls exposed to STUDENT or PARENT screens
- Parent screen = read-only trust dashboard only (scores, attendance, consent status)
- Offline mode MUST be visually indicated at all times
- CouchDB sync status MUST be visible on coordinator dashboard
- No registration form may auto-submit; COORDINATOR must explicitly confirm
- All forms must show field-level error messages (not generic "something went wrong")
```

---

## SECTION 16 — MICROSERVICE RESPONSIBILITY MAPPING

| Responsibility | Microservice | Namespace |
|---|---|---|
| Identity form validation + state machine | `society-onboarding-service` | society |
| Keycloak user provisioning | `auth-service` | core |
| Tenant + RLS binding | `user-service` | core |
| OPA policy evaluation | `auth-service` (OPA sidecar) | core |
| CouchDB offline sync | `offline-sync-service` | society |
| MinIO document storage | `document-service` | society |
| Parent consent engine | `consent-service` | society |
| SMS dispatch (OTP + consent) | `notification-service` | society |
| Vault secret (Aadhaar hash) | `auth-service` via Vault K8s Auth | ops |
| Kafka event emission | `society-onboarding-service` (producer) | society |
| Audit log write | `admin-governance-service` | core |
| Wazuh SIEM integration | `admin-governance-service` | core |

---

## SECTION 17 — INTEGRATION WITH DOWNSTREAM ECOSKILLER MODULES

### 17.1 Gates Unlocked by ONBOARDING_COMPLETE

```
STUDENT (onboarding complete):
  ✅ May enter Voice GD queue (Jitsi orchestrator — with parent consent if minor)
  ✅ May join workshop batches (Workshop & Batch Layer)
  ✅ May enter Dojo matches (Dojo Match Engine — with parent consent if minor)
  ✅ May receive skill score (Scoring Engine — baseline record created)
  ✅ Eligible for certification track (Certification & Belt Engine)

COORDINATOR (onboarding complete):
  ✅ May register students
  ✅ May mark attendance
  ✅ May view society analytics dashboard

COACH (onboarding complete):
  ✅ May conduct sessions
  ✅ May submit session reports
  ✅ May score students (within assigned domain track)

FRANCHISE_OWNER (onboarding complete):
  ✅ Commission dashboard visible
  ✅ May approve COORDINATOR and COACH registrations
  ✅ Society revenue and payout visible (Commission & Finance Layer)
```

### 17.2 What This Agent Does NOT Touch

```
- Job portal matching (outside rural block scope at onboarding stage)
- Intelligence DNA profiling (triggered post-onboarding by Analytics Service)
- Royalty & Licensing (Innovation Engine — not applicable at rural block level)
- Billing subscriptions (handled by Billing & Subscription Service post-onboarding)
- AI/ML inference (no AI in this agent — deterministic rules only)
```

---

## SECTION 18 — DEPLOYMENT SPECIFICATION

### 18.1 Kubernetes Deployment

```yaml
namespace: society

Deployments required:
  - society-onboarding-service   (2 replicas min, autoscale to 10)
  - offline-sync-service         (1 replica per rural cluster node)
  - consent-service              (2 replicas min)

ConfigMaps required:
  - rural-block-master           (block codes, society_ids)
  - onboarding-policy-config     (consent timeout, retry limits)

Secrets (via HashiCorp Vault):
  - keycloak-admin-credentials
  - jasmin-sms-api-key
  - minio-access-key
  - couchdb-admin-credentials
  - vault-token (for Aadhaar hash storage)
```

### 18.2 CouchDB Rural Deployment

```
CouchDB instance deployed at:
  - Central server (society namespace, k8s)
  - Coordinator device (local embedded CouchDB or PouchDB)

Replication type:     Bi-directional, filtered by society_id
Replication trigger:  On connectivity restore (no manual trigger needed)
Replication auth:     Per-coordinator credentials (no shared credentials)
```

---

## SECTION 19 — SECURITY BASELINE (RURAL BLOCK)

```
WAF:                  ModSecurity in front of society namespace ingress
Rate Limiting:        10 OTP requests per mobile per hour (Envoy)
PII Encryption:       All Aadhaar data encrypted before PostgreSQL storage
Field Masking:        mobile_number masked in logs (last 4 digits only)
Audit Immutability:   No DELETE on audit_logs table (RLS enforced)
Consent Evidence:     Parent consent hash stored — tamper-proof
Token Scope:          JWT tokens scoped to society_id (cannot access other societies)
Device Trust:         Device fingerprint recorded at every offline capture event
SIEM Coverage:        All onboarding events shipped to Wazuh in real-time
```

---

## SECTION 20 — FINAL EXECUTION LAWS

### 20.1 Completion Criteria

This agent's output is only valid when ALL of the following are true:

```
✔ All five roles are onboardable (FRANCHISE_OWNER, COORDINATOR, COACH, STUDENT, PARENT)
✔ All 8 FSM states are implemented and enforced
✔ Offline onboarding (CLASS_C) tested and confirmed data-loss-free
✔ Parent consent engine operational for all minors
✔ All Kafka events emitting with correct schema
✔ OPA policies deployed and tested against all permission matrix entries
✔ CouchDB sync confirmed working with simulated disconnect + reconnect
✔ Keycloak retry queue confirmed working (simulate Keycloak downtime)
✔ Audit logs confirmed write-once (test attempted UPDATE → must fail)
✔ Grafana dashboard panels active and showing live data
✔ All DLT SMS templates registered (India deployment)
✔ All failure codes tested and producing correct audit record + notification
```

### 20.2 What Antigravity Must NOT Do

```
❌ Do NOT add AI scoring, sentiment, or confidence to any onboarding field
❌ Do NOT auto-approve any role — approval is always a human or explicit system action
❌ Do NOT simplify the FSM — all 8 states must exist
❌ Do NOT use Firebase, managed GCP services, or any paid SaaS
❌ Do NOT merge PARENT and STUDENT into a single record
❌ Do NOT allow STUDENT to self-register in rural block context
❌ Do NOT skip parent consent for minors under any "edge case" logic
❌ Do NOT store Aadhaar in plaintext anywhere in any layer
❌ Do NOT allow cross-society data reads under any circumstance
❌ Do NOT declare onboarding complete without Kafka event emission
❌ Do NOT generate UI that mixes role screens
❌ Do NOT expose commission dashboard to any role other than FRANCHISE_OWNER
```

### 20.3 Antigravity Execution Contract

```
ANTIGRAVITY receives this prompt.
ANTIGRAVITY reads it in full before generating any output.
ANTIGRAVITY generates output only within the boundaries declared here.
ANTIGRAVITY does not infer, extend, or reinterpret any rule.
ANTIGRAVITY does not fill gaps silently — it STOPS and REPORTS gaps.
ANTIGRAVITY does not mix concerns across sections.
ANTIGRAVITY does not produce partial output — complete or nothing.

IF ANTIGRAVITY encounters ambiguity:
  → STOP
  → REPORT: AMBIGUITY_IN_SECTION_[X]
  → AWAIT human clarification

IF ANTIGRAVITY encounters a conflict with another Ecoskiller module:
  → STOP
  → REPORT: MODULE_CONFLICT_[module_name]
  → AWAIT resolution
```

---

## ✅ SEAL CONFIRMATION

```
Agent:            RURAL_BLOCK_ONBOARDING_AGENT
Version:          1.0.0
Platform:         Ecoskiller v12.0
Domain:           Core Identity & Onboarding — Rural Block
Execution Engine: ANTIGRAVITY
Status:           FINAL · LOCKED · SEALED

Sections Covered:
  ✔ Section 0  — Agent Identity
  ✔ Section 1  — System Context & Authority Chain
  ✔ Section 2  — Role Registry
  ✔ Section 3  — Onboarding State Machine (8 states)
  ✔ Section 4  — Identity Collection Protocol (per role)
  ✔ Section 5  — Document Verification Protocol
  ✔ Section 6  — Keycloak Provisioning Protocol
  ✔ Section 7  — Tenant & Multi-Tenancy Binding
  ✔ Section 8  — RBAC & OPA Policy Enforcement
  ✔ Section 9  — Offline-First Architecture
  ✔ Section 10 — Parent Consent Engine
  ✔ Section 11 — Notification Protocol
  ✔ Section 12 — Kafka Event Schema
  ✔ Section 13 — Observability & Audit
  ✔ Section 14 — Failure Catalogue
  ✔ Section 15 — UI Layer Contract
  ✔ Section 16 — Microservice Responsibility Map
  ✔ Section 17 — Downstream Module Integration
  ✔ Section 18 — Deployment Specification
  ✔ Section 19 — Security Baseline
  ✔ Section 20 — Final Execution Laws

ANY MUTATION WITHOUT VERSION BUMP = INVALID
ANY EXECUTION OUTSIDE ANTIGRAVITY = INVALID
ANY PARTIAL IMPLEMENTATION = INVALID
```
