# TENANT_QUOTA_ENFORCEMENT_AGENT
## Billing & Quota Division — Ecoskiller SaaS Platform

```
Artifact Class       : Production Billing & Quota Agent Specification
Agent ID             : BILL-QUOTA-TQEA-003
Status               : FINAL · SEALED · LOCKED · NON-NEGOTIABLE
Version              : v1.0
Mutation Policy      : Add-only via version bump
Interpretation Auth  : NONE
Execution Authority  : Human declaration only
Domain               : Billing & Quota → Tenant Quota Enforcement
Parent System        : ECOSKILLER MASTER EXECUTION PROMPT v12.0
Applies To           : All Tenant Types (Individual · Company · Institution)
                       All Domain Tracks (Arts · Commerce · Science ·
                       Technology · Administration) · All Plan Tiers ·
                       All Resource Classes · All Lifecycle States
Classification       : ANTIGRAVITY — Sealed Prompt Artifact
Enforcement Stack    : Kong · Envoy · Redis · PostgreSQL (RLS) · Kafka ·
                       ClickHouse · Flyway · Apache Airflow · HashiCorp Vault ·
                       Open Policy Agent · Unleash · Prometheus · Wazuh
Related Agents       : CALL_RATE_LIMIT_AGENT (BILL-QUOTA-CRLA-001)
                       PHONE_FEATURE_GATING_AGENT (BILL-QUOTA-PFGA-002)
                       TENANT_AUDIO_OBJECT_ISOLATION_AGENT (SEC-COMP-TAOIA-001)
Config Blast Domain  : ECOSKILLER CORE CONFIG → Billing · Plans · Quotas
                       (R55.1 — Configuration Blast-Domain Isolation)
```

---

> **SEAL DECLARATION**
> This document is a locked system agent specification. No clause may be
> reinterpreted, softened, removed, or overridden without a formal version
> bump and explicit human-signed declaration. Any deviation from this
> specification is a BILLING INTEGRITY VIOLATION and must trigger
> STOP EXECUTION immediately. This agent enforces the supreme quota law
> across every resource dimension of the Ecoskiller multi-tenant SaaS.

---

## SECTION 1 — AGENT IDENTITY & PURPOSE

### 1.1 Agent Name
`TENANT_QUOTA_ENFORCEMENT_AGENT` (TQEA)

### 1.2 Classification
**Billing & Quota Agent** — enforces comprehensive, deterministic, plan-bound
quota ceilings on every resource class consumed by every tenant across the
Ecoskiller platform. TQEA is the supreme quota authority. It governs tenant
lifecycle states (ACTIVE, GRACE, SUSPENDED, CANCELLED), enforces resource
ceilings across all five domain tracks (Arts, Commerce, Science, Technology,
Administration), and coordinates with all downstream billing, metering, and
enforcement agents to guarantee no tenant consumes beyond its contracted
entitlements.

### 1.3 Platform Context

Ecoskiller is a **multi-tenant SaaS** with three tenant structural types:

| Tenant Type | Examples |
|---|---|
| `individual` | Student, Trainer, Evaluator, Parent (read-only) |
| `company` | Enterprise, SME, Corporate recruiter |
| `institution` | School, College, University, Society Franchise unit |

Each tenant operates within one or more of five domain tracks:

```
Arts · Commerce · Science · Technology · Administration
```

Domain isolation is hard. Cross-domain resource consumption without explicit
policy grant is **FORBIDDEN** (AUTHZ-C — Domain Isolation Rules). TQEA
enforces this boundary for every quota dimension.

### 1.4 Problem Statement

Without a supreme quota enforcement agent:

- A Free-tier individual tenant can exhaust shared infrastructure (jobs,
  sessions, storage, notifications) that degrades paid tenant experience
- A company tenant on Starter plan can onboard unlimited users beyond seat
  limit without billing consequences
- An institution with expired subscription continues consuming GD sessions,
  course slots, and recruiter seats in a "soft zombie" state (R58.8 forbids this)
- A tenant in GRACE state (payment overdue) can continue consuming all
  premium features instead of degraded-mode enforcement
- A downgraded tenant retains higher-tier resource allocations from a previous
  plan with no reconciliation
- Cross-domain resource consumption (Arts tenant accessing Commerce GD rooms)
  occurs silently without quota and security enforcement
- Storage quota overages for MinIO (resumes, certificates, invoices, audit
  files) go undetected and unbilled
- Orphaned, stale, or suspended tenant accounts (R58.10) continue consuming
  background job slots, event bus capacity, and analytics compute
- Society franchise units in rural deployment consume SMS and push resources
  without contracted allocation limits
- Revenue leakage occurs when resource usage is not metered against
  tenant-specific quota thresholds

TQEA is the permanent, supreme, deterministic enforcement agent for every
resource quota dimension across the entire platform lifecycle.

### 1.5 Governing Principle

> **Every resource consumed by any tenant — users, sessions, storage, jobs,
> courses, projects, API calls, notifications, search queries, marketplace
> items, domain slots, or compute minutes — is governed by a quota ceiling,
> metered in real time, enforced before consumption, and immutably audited.
> No tenant resource exists outside a plan contract. No quota ceiling exists
> without a tenant binding. No tenant billing event exists without a TQEA
> meter record. The tenant lifecycle state determines the enforcement mode.
> No soft zombie tenants are permitted.**

No exception. No override. No workaround.

---

## SECTION 2 — TENANT LIFECYCLE STATE MACHINE (LOCKED)

### 2.1 Tenant States

TQEA enforces quota rules that vary by tenant lifecycle state. The state
machine is the supreme arbiter of what any tenant can consume at any moment.

```
States:
  TRIAL        → Unverified, time-limited, restricted quota
  ACTIVE       → Verified, paid/free plan, full plan quota enforced
  GRACE        → Payment overdue (1–14 days), degraded quota enforced
  SUSPENDED    → Payment failed > 14 days OR policy violation,
                  read-only access only, no resource creation
  CANCELLED    → Tenant-initiated or admin-initiated cancellation,
                  data export window only (30 days), then ARCHIVED
  ARCHIVED     → All access terminated, data retained per retention law,
                  audit logs preserved, no API access permitted
```

### 2.2 State Transition Rules

```
TRIAL      → ACTIVE      : Verified domain + plan selected + payment method added
TRIAL      → CANCELLED   : 30-day trial expiry with no conversion
ACTIVE     → GRACE       : Payment failure on renewal date
ACTIVE     → SUSPENDED   : Admin action (policy violation, fraud, security risk)
GRACE      → ACTIVE      : Payment received within grace window
GRACE      → SUSPENDED   : Grace period (14 days) expires without payment
SUSPENDED  → ACTIVE      : Payment settled + admin review + TQEA quota reset
SUSPENDED  → CANCELLED   : Tenant request or admin forced cancellation
CANCELLED  → ARCHIVED    : 30-day data export window closes
ARCHIVED   → [TERMINAL]  : No reverse transition permitted
```

**Silent state mutation → CRITICAL VIOLATION (R58.6)**
**Every state transition → TQEA audit record + Kafka event + Admin notification**

### 2.3 Quota Enforcement by Lifecycle State

| Resource Class | TRIAL | ACTIVE | GRACE | SUSPENDED | CANCELLED |
|---|---|---|---|---|---|
| API calls | Trial quota | Plan quota | 20% of plan | 0 (read-only) | 0 |
| Voice sessions | 0 | Plan quota | 0 | 0 | 0 |
| User seats | Trial limit | Plan limit | Existing only | Existing only | 0 |
| Storage | Trial limit | Plan limit | Existing only | Read-only | Export only |
| Job postings | 0 | Plan limit | Existing only | Read-only | Read-only |
| Notifications (SMS/Push) | Trial limit | Plan limit | System only | 0 | 0 |
| Analytics queries | 0 | Plan limit | 0 | 0 | 0 |
| Background jobs | Trial limit | Plan limit | Maintenance only | 0 | 0 |
| Marketplace listings | 0 | Plan limit | Existing only | Read-only | Read-only |
| Webhook subscriptions | 0 | Plan limit | 0 | 0 | 0 |

**GRACE state enforcement: all resource creation blocked except payments**
**SUSPENDED state: login permitted to billing portal only**
**No production resource creation permitted in any state other than ACTIVE**

---

## SECTION 3 — RESOURCE QUOTA REGISTRY (LOCKED)

### 3.1 User Seat Quotas (Per Tenant Per Plan)

| Seat Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE | T5 CUSTOM |
|---|---|---|---|---|---|
| Total active users | 3 | 25 | 250 | 2,500 | Negotiated |
| Admin accounts | 1 | 3 | 10 | 50 | Negotiated |
| Recruiter accounts | 0 | 5 | 50 | 500 | Negotiated |
| Mentor / evaluator accounts | 1 | 10 | 100 | 1,000 | Negotiated |
| Automation / agent accounts | 0 | 0 | 5 | 50 | Negotiated |
| Read-only accounts (parent) | 1 | 5 | 50 | 500 | Negotiated |
| Concurrent active sessions | 1 | 10 | 100 | 1,000 | Negotiated |
| Domain tracks enabled | 1 | 2 | 3 | 5 | Negotiated |

**User seat limit reached → DENY new user creation → HTTP 402 + upgrade_prompt**
**Domain track not entitled → DENY cross-domain access → HTTP 403 + domain gate reason**

### 3.2 Content Resource Quotas (Per Tenant Per Plan — Monthly Rolling)

| Resource | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| Job postings (active) | 2 | 20 | 200 | 2,000 |
| Job applications received | 50 | 500 | 5,000 | 50,000 |
| Project listings (active) | 1 | 10 | 100 | 1,000 |
| Education courses (active) | 1 | 10 | 100 | 1,000 |
| Course enrollments (total) | 10 | 100 | 1,000 | 10,000 |
| Marketplace items (active) | 0 | 10 | 100 | 1,000 |
| GD batches (monthly) | 1 | 10 | 100 | 1,000 |
| Dojo match sessions (monthly) | 5 | 50 | 500 | 5,000 |
| Interview slots (monthly) | 0 | 20 | 200 | 2,000 |
| Referral invites sent | 10 | 100 | 1,000 | 10,000 |
| Group creations | 1 | 5 | 50 | 500 |
| Group seat limit (per group) | 10 | 50 | 500 | 5,000 |

### 3.3 Storage Quotas (Per Tenant — Cumulative)

All storage is managed on **MinIO** (self-hosted, S3-compatible). TQEA
enforces storage quotas at the MinIO bucket policy level AND at application
middleware layer (dual enforcement).

| Storage Class | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| Resume / CV files | 50 MB | 500 MB | 5 GB | 50 GB |
| Certificate files | 10 MB | 100 MB | 1 GB | 10 GB |
| Invoice / billing docs | 10 MB | 100 MB | 1 GB | 10 GB |
| Audit log files | 50 MB | 500 MB | 5 GB | Unlimited |
| Course media (video, docs) | 0 | 1 GB | 10 GB | 100 GB |
| Dojo replay files | 0 | 500 MB | 5 GB | 50 GB |
| Profile media (avatars) | 5 MB | 50 MB | 500 MB | 5 GB |
| Society / franchise media | 0 | 0 | 1 GB | 10 GB |
| **Total storage cap** | **125 MB** | **2.75 GB** | **27.5 GB** | **225+ GB** |

**Storage cap reached → DENY all new uploads → HTTP 507 → NOTIFY tenant admin**
**Storage within 85% → WARNING notification to tenant admin**
**MinIO bucket policy must mirror application-layer quota — dual enforcement required**

### 3.4 Compute & Background Job Quotas (Per Tenant Per Day)

| Job Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| Airflow DAG runs (triggered) | 1 | 10 | 100 | 1,000 |
| Scoring engine jobs | 5 | 50 | 500 | 5,000 |
| Analytics export jobs | 0 | 5 | 50 | 500 |
| Certificate generation jobs | 2 | 20 | 200 | 2,000 |
| Invoice generation jobs | 1 | 10 | 100 | 1,000 |
| Bulk import / data jobs | 0 | 1 | 10 | 100 |
| ML inference jobs | 0 | 0 | 10 | 100 |
| Webhook delivery jobs | 0 | 10 | 100 | 1,000 |

### 3.5 Search & Analytics Quotas (Per Tenant Per Day)

| Query Type | T0 FREE | T1 STARTER | T2 PROFESSIONAL | T3 ENTERPRISE |
|---|---|---|---|---|
| OpenSearch candidate queries | 20 | 200 | 2,000 | 20,000 |
| OpenSearch job queries | 50 | 500 | 5,000 | 50,000 |
| OpenSearch recruiter filter queries | 0 | 50 | 500 | 5,000 |
| ClickHouse analytics reads | 0 | 10 | 100 | 1,000 |
| ClickHouse dashboard loads | 0 | 5 | 50 | 500 |
| Intelligence profile reads | 5 | 50 | 500 | 5,000 |
| Vector similarity queries | 0 | 0 | 50 | 500 |

### 3.6 Domain Track Quota Enforcement (LOCKED)

Each domain track is an independently gated resource dimension. A tenant
must have an explicit domain track entitlement to access resources within it.

```
Domain Track Quota Rules:
  1. Tenant has domain_tracks array in plan entitlement
     (e.g., T1 Starter → ["Science"] only until upgraded)
  2. Every resource request carries domain_track claim
  3. TQEA validates domain_track is in tenant's entitled tracks
  4. Resource created in domain A counts against domain A quota only
  5. Cross-domain resource consumption without grant → AUTHZ FAILURE
  6. Admin with multi-domain grant consumes from the domain of the target resource

Cross-domain violation response:
  HTTP 403 Forbidden
  {
    "denied": true,
    "reason": "DOMAIN_TRACK_NOT_ENTITLED",
    "requested_domain": "commerce",
    "entitled_domains": ["science"],
    "upgrade_url": "/billing/upgrade/domain"
  }
```

---

## SECTION 4 — ENFORCEMENT ARCHITECTURE

### 4.1 TQEA Enforcement Layer Stack

TQEA enforces quota at five distinct layers simultaneously:

```
LAYER 1 — Kong API Gateway (Ingress — first line)
  → Consumer-level quota plugin per tenant_id
  → Blocks requests when monthly/daily quota is exhausted
  → Returns HTTP 429 / HTTP 402 before request hits service
  → Fetches quota policy from TQEA Policy Cache (Redis DB 0, TTL 60s)

LAYER 2 — Envoy Service Mesh (Inter-service — second line)
  → Enforces per-service call budgets between microservices
  → Prevents internal quota circumvention via direct service calls
  → Circuit breaking on quota-exhausted services

LAYER 3 — TQEA Middleware (Application — third line)
  → Business-logic quota pre-check before all resource creation
  → Domain track entitlement validation (AUTHZ-C enforcement)
  → Tenant lifecycle state gate (ACTIVE/GRACE/SUSPENDED/CANCELLED)
  → Seat count validation before user creation
  → Storage quota validation before file upload
  → Meter record write before action execution

LAYER 4 — PostgreSQL RLS (Data — fourth line)
  → Row-Level Security on all resource tables enforces tenant isolation
  → Quota ceiling enforced as CHECK constraint on high-value tables
  → Prevents data-layer bypass of application quota checks

LAYER 5 — MinIO Bucket Policies (Storage — fifth line)
  → Per-tenant storage quota enforced at bucket policy level
  → Independent of application layer (defense in depth)
  → TQEA synchronizes MinIO policies with plan entitlements on plan change
```

### 4.2 Redis Quota Counter Namespace Protocol (LOCKED)

All TQEA quota counters in Redis must follow this schema:

```
tqea:{tenant_id}:{resource_class}:{dimension}:{window}

Examples:
  tqea:t_acme:user_seats:total:lifetime
  tqea:t_acme:user_seats:recruiter:lifetime
  tqea:t_acme:job_postings:active:snapshot
  tqea:t_acme:storage:total_bytes:snapshot
  tqea:t_acme:gd_batches:monthly:2024-02
  tqea:t_acme:course_enrollments:monthly:2024-02
  tqea:t_acme:search:candidate:daily:2024-02-06
  tqea:t_acme:background_jobs:scoring:daily:2024-02-06
  tqea:t_acme:domain_tracks:entitled:snapshot
  tqea:t_acme:lifecycle_state:current
```

**Counter rules:**

- All Redis keys for TQEA must be prefixed `tqea:{tenant_id}:`
- Unscoped TQEA counter key → STOP EXECUTION → ALERT
- Snapshot counters (seats, storage): refreshed on every mutation
- Monthly rolling counters: reset by Airflow DAG on 1st of month
- Daily counters: reset by Airflow DAG at midnight UTC
- Lifetime counters: never reset — append only
- `maxmemory-policy: noeviction` is MANDATORY on TQEA Redis instance

### 4.3 Pre-Creation Authorization Gate (LOCKED)

Before any resource-creating operation executes, the service responsible
MUST call the TQEA Pre-Creation Authorization API:

```
POST /tqea/v1/authorize-creation

Request:
{
  "tenant_id": "<uuid>",
  "resource_class": "user_seat | job_posting | course | project |
                     gd_batch | storage_upload | marketplace_item |
                     group | interview_slot | dojo_match | webhook |
                     background_job | marketplace_listing",
  "resource_subtype": "<specific type within class>",
  "domain_track": "arts | commerce | science | technology | administration",
  "quantity": <integer>,
  "actor_id": "<uuid>",
  "actor_type": "human | automation_agent"
}

Response (ALLOWED):
{
  "authorized": true,
  "authorization_token": "<single-use UUID, 120s TTL>",
  "quota_snapshot": {
    "resource_class": "job_postings",
    "used": 18,
    "limit": 20,
    "remaining": 2,
    "plan_tier": "T1",
    "domain_track": "science",
    "lifecycle_state": "ACTIVE"
  }
}

Response (DENIED):
{
  "authorized": false,
  "denial_code": "QUOTA_EXHAUSTED | SEAT_LIMIT_REACHED | STORAGE_CAP_REACHED |
                  LIFECYCLE_STATE_BLOCKED | DOMAIN_TRACK_NOT_ENTITLED |
                  PLAN_FEATURE_GATED | TRIAL_EXPIRED | TENANT_SUSPENDED",
  "quota_snapshot": { ... },
  "upgrade_prompt": true,
  "upgrade_url": "/billing/upgrade"
}
```

**Resource creation WITHOUT valid authorization token → STOP EXECUTION → ALERT**
**Authorization token is single-use (consumed on creation) and expires in 120 seconds**
**Token consumption is atomic (Redis GETDEL) — no double-spend possible**

### 4.4 Tenant Lifecycle State Gate (LOCKED)

TQEA checks tenant lifecycle state at every authorization request:

```
State: TRIAL
  → Allow creation up to trial quota limits
  → Inject trial_expires_at warning in response headers
  → Block voice sessions, marketplace, webhooks

State: ACTIVE
  → Allow creation up to plan quota limits
  → No restrictions beyond plan entitlements

State: GRACE (payment overdue, 1–14 days)
  → DENY all new resource creation
  → ALLOW read operations on existing resources
  → ALLOW access to billing portal only for payment
  → INJECT grace_expires_at in every API response header
  → Send daily reminder to tenant admin (email + in-app)
  → emit tenant.grace_period.active Kafka event daily

State: SUSPENDED
  → DENY all operations except billing portal read
  → All API calls return HTTP 402 with payment URL
  → No resource creation, no reads except billing
  → Background jobs for this tenant: PAUSED
  → emit tenant.suspended Kafka event

State: CANCELLED
  → DENY all operations except data export
  → Export portal accessible for 30 days only
  → After 30 days → transition to ARCHIVED automatically
  → emit tenant.data_export_window.open Kafka event

State: ARCHIVED
  → DENY all operations, no exceptions
  → HTTP 410 Gone on all API calls
  → Audit logs preserved per WORM retention policy
  → emit tenant.archived Kafka event
```

---

## SECTION 5 — PLAN CHANGE ENFORCEMENT (LOCKED)

### 5.1 Upgrade Protocol

```
Trigger: Tenant upgrades from T1 → T2

TQEA Actions:
  1. Validate payment confirmed (Billing Service webhook)
  2. Update plan tier in PostgreSQL tenant record (atomic transaction)
  3. Refresh TQEA Policy Cache in Redis (immediate TTL flush)
  4. Update MinIO bucket policies to new storage limits (async, within 60s)
  5. Extend quota counters to new plan limits (existing usage preserved)
  6. Unlock new domain tracks if applicable (update Redis domain_tracks snapshot)
  7. Provision new feature entitlements (Unleash flag update)
  8. Write upgrade audit record
  9. emit tenant.plan_upgraded Kafka event
  10. Notify tenant admin (email + push)
  11. TQEA quota counters do NOT reset on upgrade — usage carries forward
```

### 5.2 Downgrade Protocol

```
Trigger: Tenant downgrades from T2 → T1 (or plan cancellation approaching)

TQEA Actions:
  1. Validate downgrade request authenticated by tenant admin
  2. Compute overage: current usage vs new plan limits
  3. If current usage > new plan limits on any dimension:
       → BLOCK downgrade execution
       → Return HTTP 409 Conflict with over_quota_resources list
       → Require tenant to reduce usage before downgrade completes
       → Set downgrade_pending_at: 30-day window to comply
  4. If usage within new plan limits:
       → Execute downgrade at end of current billing cycle
       → Update plan tier in PostgreSQL (atomic)
       → Reduce MinIO bucket policies (within 60s)
       → Revoke domain tracks not included in new plan
       → Revoke feature entitlements not in new plan (Unleash)
       → Flush TQEA Policy Cache
       → Write downgrade audit record
       → emit tenant.plan_downgraded Kafka event
  5. Resources created under higher plan that exceed new limits:
       → Become read-only (cannot be edited or duplicated)
       → Are NOT auto-deleted — tenant must manually archive
       → Visible but locked with downgrade_locked: true flag
```

**Downgrade that silently exceeds new quotas → STOP EXECUTION**
**Resources locked on downgrade must be clearly communicated to tenant admin**

### 5.3 Mid-Cycle Quota Adjustment

When a tenant's quota usage must be manually adjusted by Platform Admin:

```
Trigger: Platform Admin issues quota adjustment via Admin Governance Service

TQEA Actions:
  1. Validate requester has PLATFORM_OPS role (T4)
  2. Dual-control approval required (two T4 admins must confirm)
  3. Apply adjustment to Redis counter (delta INCRBY or DECRBY)
  4. Update PostgreSQL quota_adjustments log table (immutable append)
  5. Write TQEA audit record with adjustment reason
  6. emit tenant.quota_adjusted Kafka event
  7. Notify tenant admin of adjustment (email + in-app)
```

---

## SECTION 6 — OVERAGE MANAGEMENT

### 6.1 Overage Classification

| Overage Type | Detection Method | Response |
|---|---|---|
| Soft overage (85–99% of quota) | TQEA Redis counter threshold | WARNING notification |
| Hard overage (100% of quota) | TQEA Pre-Creation Gate DENY | DENY + quota.exhausted event |
| Storage overage | MinIO + TQEA dual detection | DENY uploads + notify |
| Seat overage attempt | TQEA Pre-Creation Gate | DENY user creation + notify |
| Domain track overage | TQEA domain gate | DENY + HTTP 403 |
| Background job overage | TQEA job gate | Queue rejection + notify |

### 6.2 Overage Billing Protocol

For tenants with `overage_enabled: true` (T2, T3, T5 only):

```
1. TQEA detects quota threshold crossed (100%)
2. TQEA checks overage_enabled flag (Billing Service cache, 60s TTL)
3. If overage_enabled = true:
   → Allow resource creation to proceed
   → Meter with status: EXECUTED_OVERAGE
   → Billing Service accrues overage charge per unit
   → In-app overage banner shown to tenant admin
   → emit resource.overage.accruing Kafka event
4. If overage_enabled = false:
   → DENY resource creation
   → HTTP 402 + upgrade_prompt: true
   → emit quota.exhausted Kafka event
```

**Overage units and rates per resource class:**

| Resource Class | Overage Unit | Rate Basis |
|---|---|---|
| User seats (over plan) | Per user per month | Plan-defined rate |
| Job postings (over limit) | Per posting per month | Plan-defined rate |
| Storage (over cap) | Per GB per month | Plan-defined rate |
| GD sessions (over quota) | Per session-minute | Plan-defined rate |
| Course enrollments | Per enrollment | Plan-defined rate |
| Background jobs | Per 100 jobs | Plan-defined rate |
| Search queries | Per 1,000 queries | Plan-defined rate |

### 6.3 Quota Warning Cascade

```
Resource at 70% of quota  → In-app info banner (tenant admin)
Resource at 85% of quota  → Email to tenant admin
Resource at 95% of quota  → Email + push + urgent in-app banner
Resource at 100% (hit)    → DENY + full notification cascade
                           → emit quota.exhausted Kafka event
                           → Upgrade prompt shown on all admin screens
```

---

## SECTION 7 — DUNNING & PAYMENT FAILURE PROTOCOL

### 7.1 Dunning Sequence (LOCKED)

```
Day 0    : Payment fails on renewal
           → Tenant transitions to GRACE state
           → emit tenant.payment_failed Kafka event
           → Email: "Payment failed — 14 days to resolve"
           → TQEA enforces GRACE quota rules immediately

Day 1    : Automatic payment retry (Billing Service)
           → If success: transition back to ACTIVE
           → If fail: continue GRACE

Day 3    : Second payment retry + reminder email + in-app alert
Day 7    : Third payment retry + email + push + in-app urgent
Day 10   : Final payment retry + email + push + phone notification (if T2+)
Day 14   : GRACE expires → tenant transitions to SUSPENDED
           → emit tenant.suspended Kafka event
           → All resource creation: BLOCKED
           → Background jobs: PAUSED
           → WebSocket sessions: TERMINATED
           → Redis sessions: INVALIDATED
           → TQEA quota enforcement: SUSPENDED MODE

Day 14–44 : Tenant can only access billing portal to pay
Day 44   : Tenant transitions to CANCELLED if no payment received
           → Data export window opens (30 days)

Day 74   : Data export window closes → tenant transitions to ARCHIVED
```

**Every dunning action: audit record + Kafka event + TQEA state update**
**No "soft zombie" tenant permitted (R58.8 — No soft zombie accounts allowed)**

### 7.2 Payment Recovery Protocol

```
Trigger: Payment received during GRACE or SUSPENDED state

TQEA Actions:
  1. Billing Service confirms payment (webhook)
  2. TQEA validates payment covers full outstanding amount
  3. Transition tenant to ACTIVE state (atomic DB update)
  4. Restore full plan quota (Redis counter ceiling reset to plan limits)
  5. Resume paused background jobs (Airflow DAG unpause)
  6. Re-enable WebSocket connection permissions
  7. Refresh TQEA Policy Cache (Redis TTL flush)
  8. Write recovery audit record
  9. emit tenant.payment_recovered Kafka event
  10. Notify tenant admin (email + push)
```

---

## SECTION 8 — ORPHAN & STALE TENANT DETECTION (R58.10)

### 8.1 Detection Protocol

TQEA runs Airflow-scheduled scans for orphan, shadow, and stale tenants
per R58.10 (Orphan, Shadow & Stale Account Detection):

```
Weekly Scan (Airflow DAG: tqea_orphan_scan):
  1. Query PostgreSQL: tenants with no API activity > 90 days
  2. Query PostgreSQL: tenants with no users active > 60 days
  3. Query PostgreSQL: tenants in TRIAL state > 45 days (trial overstay)
  4. Query PostgreSQL: tenants with no billing record > 30 days (free tier)
  5. For each detected orphan:
     → Set orphan_detected: true in tenant record
     → emit tenant.orphan_detected Kafka event
     → Notify platform admin
     → Schedule auto-disable if no response in 14 days
```

### 8.2 Orphan Remediation States

```
Orphan detected + no response in 14 days:
  → Transition to SUSPENDED (no billing impact if free tier)
  → Admin Governance Service notified for review

Orphan confirmed legitimate (admin review):
  → Clear orphan_detected flag
  → Write remediation audit record

Confirmed orphan:
  → Transition to CANCELLED
  → Data export window: 30 days
  → Then ARCHIVED
```

---

## SECTION 9 — SOCIETY FRANCHISE QUOTA GOVERNANCE

### 9.1 Society Domain Quota Rules

Society franchise units are `institution` type tenants operating under
T3/T5 plan tiers with specialized resource allocations for rural/offline
deployment (Society Architecture document, March 2026):

```
Society-specific quotas (per franchise unit per month):
  - Workshop batch creations        : T3=50, T5=Negotiated
  - Student enrollments             : T3=500, T5=Negotiated
  - Attendance QR scans             : T3=5,000, T5=Negotiated
  - Certificate generations         : T3=200, T5=Negotiated
  - Coach accounts                  : T3=20, T5=Negotiated
  - Coordinator accounts            : T3=10, T5=Negotiated
  - Tournament events               : T3=5, T5=Negotiated
  - Offline sync events             : T3=10,000, T5=Negotiated
  - Commission calculation jobs     : T3=1,000, T5=Negotiated
  - Govt scheme documentation jobs  : T3=50, T5=Negotiated

Society storage quotas (cumulative):
  - Craft/product inventory media   : T3=5GB, T5=Negotiated
  - Student portfolio files         : T3=10GB, T5=Negotiated
  - Guardian consent records        : T3=1GB, T5=Negotiated (WORM)
  - Audit and compliance logs       : Unlimited (WORM mandatory)
```

### 9.2 Offline-First Quota Sync

Society franchise units operate in low-connectivity environments. TQEA
enforces quotas with offline-tolerant sync:

```
Offline mode (connectivity absent):
  → TQEA grants offline_quota_budget from last sync
  → Device caches quota budget locally (encrypted)
  → Operations consume from local budget
  → Budget refresh on next connectivity restore

Budget exhausted offline:
  → Device blocks new resource creation locally
  → Queues rejection events for sync
  → Does NOT silently over-consume

Sync restore:
  → Device uploads offline consumption log
  → TQEA reconciles against server quota counters
  → If over-consumed (sync gap): emit quota.offline_overage event
  → Admin Governance Service reviews offline overages
```

---

## SECTION 10 — METERING ENGINE

### 10.1 Metering Principle

Every resource creation, modification, or deletion — whether authorized or
denied — must produce a TQEA meter record.

```
METERING RULE: Meter before create. Meter the denial.
               No unmetered resource. No unmetered denial.
               Quota counter must be incremented BEFORE the resource
               is created. On creation failure: DECREMENT counter.
```

### 10.2 Meter Record Schema

```json
{
  "meter_id": "<uuid>",
  "agent": "TENANT_QUOTA_ENFORCEMENT_AGENT",
  "tenant_id": "<uuid>",
  "tenant_type": "individual | company | institution",
  "actor_id": "<uuid>",
  "actor_type": "human | automation_agent | system",
  "resource_class": "<resource type>",
  "resource_subtype": "<specific subtype>",
  "domain_track": "arts | commerce | science | technology | administration",
  "plan_tier": "T0 | T1 | T2 | T3 | T4 | T5",
  "lifecycle_state": "TRIAL | ACTIVE | GRACE | SUSPENDED | CANCELLED | ARCHIVED",
  "action": "CREATE | UPDATE | DELETE | READ | EXPORT",
  "status": "AUTHORIZED | DENIED | OVERAGE | DOWNGRADE_LOCKED",
  "denial_code": "<null or denial reason>",
  "quota_snapshot": {
    "resource_class": "job_postings",
    "used_before": 17,
    "used_after": 18,
    "limit": 20,
    "remaining": 2,
    "domain_track": "science"
  },
  "units_consumed": 1,
  "billable": true,
  "overage": false,
  "authorization_token_id": "<single-use token UUID>",
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

### 10.3 Meter Storage Architecture

```
Path 1 — Real-time (Redis):
  Atomic INCR on quota counter before creation
  Atomic DECR on creation failure (rollback)

Path 2 — Durable (ClickHouse via Kafka):
  Kafka: ecoskiller.{tenant_id}.tqea.metering.{resource_class}
  → Analytics Service → ClickHouse tqea_meters table

ClickHouse Schema:
CREATE TABLE tqea_meters (
  meter_id         UUID,
  tenant_id        UUID NOT NULL,
  tenant_type      LowCardinality(String),
  resource_class   LowCardinality(String),
  resource_subtype LowCardinality(String),
  domain_track     LowCardinality(String),
  plan_tier        LowCardinality(String),
  lifecycle_state  LowCardinality(String),
  action           LowCardinality(String),
  status           LowCardinality(String),
  denial_code      Nullable(String),
  units_consumed   UInt32,
  billable         UInt8,
  overage          UInt8,
  event_ts         DateTime
) ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(event_ts))
ORDER BY (tenant_id, resource_class, event_ts);

All ClickHouse reads MUST include WHERE tenant_id = ?
```

---

## SECTION 11 — QUOTA RESET CYCLE (LOCKED)

### 11.1 Monthly Reset (Airflow DAG: tqea_monthly_reset)

```
Trigger: 1st of each calendar month, 00:00:00 UTC

Actions:
  1. For every ACTIVE tenant:
     a. Archive all monthly counters to ClickHouse
     b. DELETE Redis monthly counter keys: tqea:{tenant_id}:*:monthly:*
     c. Write reset audit record to PostgreSQL
     d. emit tqea.quota.monthly_reset Kafka event
  2. For every GRACE tenant:
     a. Archive counters
     b. Do NOT reset — GRACE tenants receive no new quota until payment
     c. Write audit record
  3. For SUSPENDED/CANCELLED/ARCHIVED tenants:
     a. Archive counters only
     b. No quota reset performed
  4. Trigger Invoice Generation DAG for billing period
  5. Alert on any DAG failure (quota not reset = tenants incorrectly blocked)
```

### 11.2 Daily Reset (Airflow DAG: tqea_daily_reset)

```
Trigger: Every day, 00:00:00 UTC

Actions:
  1. DELETE Redis daily counter keys: tqea:{tenant_id}:*:daily:*
  2. Write daily audit summary per tenant to PostgreSQL
  3. emit tqea.quota.daily_reset Kafka event
  4. Run orphan scan (Section 8)
  5. Run dunning schedule check (Section 7)
```

### 11.3 Snapshot Counters Reconciliation (Airflow DAG: tqea_snapshot_reconcile)

```
Trigger: Every 4 hours

Purpose: Redis snapshot counters (seats, storage) can drift from DB truth.

Actions:
  1. For each tenant: query PostgreSQL for actual user count per seat type
  2. Compare against Redis snapshot counter
  3. If drift detected (>5% variance): update Redis to DB truth
  4. Write reconciliation record
  5. Alert if drift detected (indicates possible counter manipulation)
```

---

## SECTION 12 — AUDIT & OBSERVABILITY

### 12.1 Immutable Audit Log Schema

```json
{
  "audit_id": "<uuid>",
  "agent": "TENANT_QUOTA_ENFORCEMENT_AGENT",
  "tenant_id": "<uuid>",
  "tenant_type": "individual | company | institution",
  "actor_id": "<uuid>",
  "event_class": "quota_check | quota_exhausted | quota_warning | plan_upgraded |
                  plan_downgraded | lifecycle_state_change | overage_accrued |
                  dunning_triggered | tenant_suspended | tenant_archived |
                  orphan_detected | snapshot_drift | domain_gate_denied |
                  storage_cap_reached | seat_limit_reached | offline_overage",
  "resource_class": "<resource type>",
  "domain_track": "<domain>",
  "lifecycle_state_before": "<state>",
  "lifecycle_state_after": "<state or null>",
  "decision": "AUTHORIZED | DENIED | STATE_CHANGED | FLAGGED",
  "plan_tier": "<T0-T5>",
  "quota_context": { ... },
  "timestamp": "<ISO 8601>",
  "trace_id": "<OpenTelemetry trace ID>"
}
```

**Audit logs: append-only, WORM-compatible (R52.4)**
**Payments & refunds audit: immutable, retained per legal requirements**
**No deletion, no mutation of audit records under any condition**

### 12.2 Wazuh SIEM Integration

TQEA must emit structured security events to Wazuh for:

| Event | Severity |
|---|---|
| Unscoped Redis counter key write | CRITICAL |
| Resource creation bypassing Pre-Creation Gate | CRITICAL |
| Suspended tenant API access attempt | HIGH |
| Archived tenant API access attempt | HIGH |
| Snapshot drift > 20% (possible counter manipulation) | HIGH |
| Cross-domain resource creation without entitlement | HIGH |
| Downgrade attempt with quota overage silently applied | HIGH |
| Orphan tenant auto-disable | MEDIUM |
| Quota exhaustion cascade (3+ resource classes) | MEDIUM |
| Grace period approaching final day | LOW |

### 12.3 Prometheus Metrics (Mandatory)

```
tqea_quota_checks_total{tenant_id, resource_class, decision}
tqea_quota_exhausted_total{tenant_id, resource_class}
tqea_quota_warning_total{tenant_id, resource_class, threshold}
tqea_lifecycle_state_transitions_total{from_state, to_state}
tqea_tenants_by_state{lifecycle_state}
tqea_seat_utilization_ratio{tenant_id, seat_type}
tqea_storage_utilization_bytes{tenant_id, storage_class}
tqea_overage_events_total{tenant_id, resource_class}
tqea_dunning_events_total{tenant_id, dunning_day}
tqea_plan_upgrades_total{from_tier, to_tier}
tqea_plan_downgrades_total{from_tier, to_tier}
tqea_orphan_detections_total
tqea_snapshot_drift_detections_total{tenant_id}
tqea_domain_gate_denials_total{tenant_id, denied_domain, entitled_domains}
tqea_authorization_token_latency_seconds
```

### 12.4 Grafana Dashboards (Mandatory)

- Tenant lifecycle state distribution (real-time donut)
- Quota utilization heat map (all resource classes × all active tenants)
- Monthly quota burn-down by tenant and resource class
- Storage utilization by tenant (bar chart with cap line)
- Seat utilization by tenant and seat type
- Domain track access distribution (which tracks are active)
- Dunning pipeline (tenants in GRACE / SUSPENDED by dunning day)
- Overage accrual rate by resource class
- Plan upgrade / downgrade funnel (monthly)
- Orphan tenant detection and remediation tracker

---

## SECTION 13 — INTEGRATION CONTRACTS

### 13.1 Auth Service (Keycloak)
Must inject `tenant_id`, `tenant_type`, `plan_tier`, `lifecycle_state`,
`domain_tracks` into every JWT.
**Missing `lifecycle_state` claim → TQEA treats tenant as SUSPENDED**
**Missing `domain_tracks` claim → TQEA denies all domain-scoped operations**

### 13.2 Billing & Subscription Service
Is the **sole authority** for plan tier assignments and payment status.
TQEA fetches plan and lifecycle state from Billing Service (Redis cache,
60s TTL). Plan and lifecycle changes propagate to TQEA within 60 seconds.
**TQEA never modifies plan tier directly — only Billing Service owns this**

### 13.3 All Resource-Creating Services
(Job Service, User Service, Course Service, Project Service,
Application Service, Marketplace Service, GD Orchestrator,
Dojo Match Engine, Interview Service, Society Services)
**Must call TQEA Pre-Creation Gate before every creation operation**
**Resource creation without TQEA authorization token → STOP EXECUTION**

### 13.4 MinIO Object Storage
TQEA synchronizes MinIO bucket size policies with plan storage quotas.
On plan upgrade/downgrade: TQEA pushes updated bucket policies within 60s.
MinIO must NOT permit uploads beyond bucket size policy regardless of
application-layer state.

### 13.5 Apache Airflow
Owns quota reset DAGs (daily, monthly), orphan scan DAG, reconciliation DAG,
and dunning schedule DAG.
**DAG failure → TQEA alert → human review within 1 hour**
**Quota reset DAG failure: tenants may be incorrectly blocked or under-enforced**

### 13.6 Unleash Feature Flags
TQEA updates Unleash feature flag states when:
- Plan tier changes (upgrade/downgrade)
- Lifecycle state changes (GRACE/SUSPENDED: disable premium flags)
- Domain track entitlements change
**Feature flag state must mirror TQEA entitlement state within 60 seconds**

### 13.7 Admin Governance Service
Receives all TQEA lifecycle state change events.
Provides dual-control approval for mid-cycle quota adjustments.
Receives orphan tenant detection events for human review.
**Admin Governance is the human escalation path for all TQEA edge cases**

### 13.8 Open Policy Agent (OPA)
TQEA delegates complex multi-dimensional authorization to OPA:
- "Can this institution admin create a job posting in the science domain track?"
- "Can this T1 company tenant enable overage billing?"
- "Can this coordinator create a workshop batch under this franchise unit?"

### 13.9 CALL_RATE_LIMIT_AGENT (CRLA)
CRLA governs per-call rate limits and API quota.
TQEA governs per-resource quota ceilings and lifecycle state.
Both must be satisfied for any operation to proceed.
**TQEA and CRLA enforce independently — neither defers to the other**

### 13.10 PHONE_FEATURE_GATING_AGENT (PFGA)
PFGA governs phone channel entitlements.
TQEA lifecycle state changes must be propagated to PFGA (Kafka event).
When tenant enters SUSPENDED: PFGA immediately blocks all SMS/push dispatches.

---

## SECTION 14 — DEPLOYMENT REQUIREMENTS

### 14.1 Kubernetes Namespace

```yaml
namespace: billing
labels:
  app: tqea
  tier: billing-quota
  criticality: critical
```

### 14.2 Resource Limits

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "512Mi"
  limits:
    cpu: "2"
    memory: "1Gi"
```

### 14.3 Redis Dedicated Instance

```yaml
redis:
  host: redis-tqea
  maxmemory-policy: noeviction    # MANDATORY — quota counters must never evict
  database:
    0: tqea_policy_cache           # plan entitlements, lifecycle state (TTL 60s)
    1: tqea_quota_counters         # all monthly/daily/lifetime counters
    2: tqea_authorization_tokens   # single-use creation tokens (TTL 120s)
    3: tqea_snapshot_counters      # seat counts, storage bytes (snapshot)
    4: tqea_domain_entitlements    # per-tenant domain track grants
```

**`noeviction` is MANDATORY. Counter eviction = quota bypass vulnerability.**
**Shared Redis with other agents is FORBIDDEN.**

### 14.4 Health Probes

Readiness must verify:
- Redis TQEA instance reachable + `noeviction` confirmed
- PostgreSQL connection with RLS validation check
- Billing Service plan cache populated (at least 1 tenant policy loaded)
- Kafka producer connection healthy
- ClickHouse write connection healthy
- Airflow DAG health endpoint reachable
- OPA policy loaded and responsive
- MinIO connection + bucket policy write permission confirmed

---

## SECTION 15 — FINAL ENFORCEMENT DECLARATION

```
┌──────────────────────────────────────────────────────────────────┐
│  TENANT_QUOTA_ENFORCEMENT_AGENT — FINAL LAW                      │
│                                                                  │
│  1.  No resource is created without TQEA pre-authorization       │
│  2.  No tenant consumes beyond its plan quota ceiling            │
│  3.  No tenant resource exists outside a tenant_id binding       │
│  4.  No Redis quota counter exists without tqea:{tenant_id}:     │
│  5.  No feature is accessible in GRACE, SUSPENDED, or ARCHIVED   │
│      state beyond what the lifecycle state permits               │
│  6.  No soft zombie tenant is permitted (R58.8)                  │
│  7.  No cross-domain resource creation without domain track      │
│      entitlement (AUTHZ-C)                                       │
│  8.  No plan downgrade executes while usage exceeds new limits   │
│  9.  No storage upload proceeds beyond MinIO + app dual cap      │
│  10. No orphan or stale tenant continues resource consumption    │
│      (R58.10)                                                    │
│  11. No dunning state exists beyond 14 days without SUSPENDED    │
│  12. No tenant billing record exists without TQEA meter source   │
│  13. Every quota exhaustion triggers a tenant notification       │
│  14. Every lifecycle state transition produces an audit record   │
│  15. Every snapshot drift triggers a reconciliation and alert    │
│  16. Every mid-cycle quota adjustment requires dual-control      │
│  17. Every overage accrual is metered and invoiced               │
│  18. Society offline overages are reconciled on sync restore     │
│                                                                  │
│  Violation of any rule above:                                    │
│  → DENY OPERATION                                                │
│  → WRITE METER RECORD (even for denied operations)              │
│  → WRITE IMMUTABLE AUDIT LOG                                     │
│  → FIRE SIEM ALERT (on bypass, drift, or state violation)        │
│  → REPORT TO BILLING SERVICE                                     │
│  → REPORT TO ADMIN GOVERNANCE SERVICE                            │
│                                                                  │
│  No exceptions. No overrides. No workarounds.                    │
│  T5 CUSTOM exemptions require human-signed contract only.        │
│  T4 PLATFORM_ADMIN adjustments require dual-control only.        │
└──────────────────────────────────────────────────────────────────┘
```

---

## SECTION 16 — VERSION CONTROL

| Version | Date | Change | Authority |
|---|---|---|---|
| v1.0 | 2026-03-04 | Initial sealed specification | Human declaration |

**Next version: v1.1 — Add-only. No mutations to existing clauses.**

---

```
SEAL: TENANT_QUOTA_ENFORCEMENT_AGENT · BILL-QUOTA-TQEA-003 · v1.0
CLASSIFICATION: ANTIGRAVITY — SEALED PROMPT ARTIFACT
STATUS: FINAL · LOCKED · GOVERNED · NON-NEGOTIABLE
```
