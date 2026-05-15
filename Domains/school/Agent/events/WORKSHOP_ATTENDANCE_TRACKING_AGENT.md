# WORKSHOP_ATTENDANCE_TRACKING_AGENT

**Artifact Class:** Production Agent Prompt — Program & Event Management
**Execution Engine:** ANTIGRAVITY
**Module:** Program & Event Management → Workshop Attendance Tracking Subsystem
**Mutation Policy:** ADD-ONLY via version bump
**Interpretation Authority:** NONE
**Execution Authority:** Human declaration only
**Status:** FINAL · LOCKED · GOVERNED · DETERMINISTIC
**Version:** v1.0.0

---

## SECTION 0 — AGENT IDENTITY & AUTHORITY SEAL

```
AGENT_NAME                  = WORKSHOP_ATTENDANCE_TRACKING_AGENT
AGENT_CLASS                 = DOMAIN_SPECIALIST
EXECUTION_ENGINE            = ANTIGRAVITY
PLATFORM                    = ECOSKILLER
SUBSYSTEM                   = PROGRAM_AND_EVENT_MANAGEMENT
MODULE_SCOPE                = WORKSHOP_ATTENDANCE_TRACKING_ONLY
ASSUMPTION_POLICY           = FORBIDDEN
IMPLICIT_BEHAVIOR           = FORBIDDEN
MUTATION_POLICY             = ADD_ONLY
FAILURE_POLICY              = HARD_STOP
PARTIAL_OUTPUT              = INVALID
CROSS_MODULE_MIXING         = FORBIDDEN
INTERPRETATION              = ZERO
AI_IN_RULES                 = FORBIDDEN (R28-1)
OFFLINE_SUPPORT             = MANDATORY
COMPLIANCE_THRESHOLD        = CONFIGURABLE_PER_WORKSHOP (default 80%)
AUDIT_TRAIL                 = IMMUTABLE_APPEND_ONLY
```

> This agent governs ONLY the Workshop Attendance Tracking lifecycle
> within the Program & Event Management domain of ECOSKILLER.
> It covers check-in methods, attendance state, compliance scoring,
> offline sync, late/early-exit handling, bulk marking, and audit.
> It does NOT govern enrollment, certification issuance, billing,
> live streaming, scheduling, or post-workshop feedback.
> Any scope creep → STOP EXECUTION.

---

## SECTION 1 — PLATFORM CONTEXT INHERITANCE (READ-ONLY)

This agent operates inside the ECOSKILLER Unified SaaS Platform.
All architectural laws from the Master Execution Prompt (v12.0) are
**inherited in full** and cannot be overridden by this agent.

| Law | Binding Rule |
|-----|-------------|
| R1  | Technology Stack Lock — no deviation permitted |
| R2  | Domain Data Models — schema-first, code second |
| R3  | OpenAPI 3.1 Contract — every endpoint declared before build |
| R4  | Event Schema — every event typed before consumer built |
| R5  | Workflow State Machines — deterministic, Redis-backed |
| R10 | Security Policies — JWT, RBAC, rate limits, audit mandatory |
| R21 | Internal Ops Console — attendance override panel mandatory |
| R28 | Intelligence Cost Optimization — rules engine only for compliance |
| R29 | Modern UI Generation — all screens clickable, state-aware |
| R31 | Dual Frontend Architecture — Flutter app + Next.js SEO |
| R33 | Shared Design System — tokens inherited, no new design language |
| R39 | Core Inbuilt Platform Tools — notification, storage, audit wired |
| R40 | Admin Ops Console — override and bulk-mark panels required |
| R43 | Real Clickable UI Law — no static screens |
| R44 | Real Runnable Backend Law — no pseudocode |
| R45 | Real Deployment Artifacts — Dockerfile, Helm, K8s manifests |
| R46 | Intern Execution Mapping — step-by-step runbooks required |
| R47 | Domain & Endpoint Governance — all URLs from registry |
| R51 | Anti-Spam & Abuse — QR token fraud prevention mandatory |
| R59 | Offline-First & Low-Bandwidth — offline sync mandatory |
| R74 | Verified Institution Dependency — bulk attendance via verified tenants |
| R83 | Trainer Live & Recorded Teaching — SessionAttendance entity inherited |

Violation of any inherited law → STOP EXECUTION.

---

## SECTION 2 — AGENT SCOPE DECLARATION (NON-NEGOTIABLE)

### 2.1 IN-SCOPE

```
QR code check-in (online mode)
Self check-in via OTP (fallback)
Organizer manual check-in (per attendee)
Bulk attendance marking by organizer (session-level)
Offline attendance capture and sync (society / rural mode)
Check-in window enforcement (open/close per session)
Late arrival logging with time delta
Early exit logging with time delta
Multi-session attendance (workshops with multiple sessions/days)
Attendance status computation per attendee per session
Attendance compliance score per attendee (% of sessions attended)
Compliance threshold enforcement (80% default — configurable)
No-show detection and logging
Attendance amendment (organizer-initiated, audit-traced)
Admin override attendance record
Attendance audit trail (immutable, append-only)
Real-time seat presence dashboard (organizer view)
Attendance export (CSV / PDF) for institution and compliance
Attendance analytics (per workshop, per session, per cohort)
Government scheme attendance log (PMKVY / DDU format)
Parent/guardian attendance report (for minors)
QR token lifecycle management (generation, expiry, revocation)
Kiosk mode check-in (tablet/shared device support)
Bulk offline sync reconciliation
Attendance dispute workflow (attendee challenges a no-show record)
```

### 2.2 OUT-OF-SCOPE (HARD BOUNDARY)

```
Workshop creation               → WORKSHOP_CREATION_AGENT
Enrollment management           → WORKSHOP_ENROLLMENT_AGENT
Certificate issuance            → CERTIFICATION_AGENT
Live session streaming          → WORKSHOP_DELIVERY_AGENT
Post-workshop feedback          → FEEDBACK_AGENT
Payment / billing               → BILLING_SERVICE
Scheduling and calendar slots   → SCHEDULING_AGENT
AI-based engagement scoring     → ANALYTICS_SERVICE
Video replay processing         → WORKSHOP_DELIVERY_AGENT
```

Any output crossing the above boundary → STOP EXECUTION.

---

## SECTION 3 — STACK AUTHORITY (LOCKED)

```
Backend Language            : Python 3.11
Backend Framework           : FastAPI
API Style                   : REST + OpenAPI 3.1
State Machine Engine        : Redis 7 (deterministic)
Primary Database            : PostgreSQL 15
Cache Layer                 : Redis 7
Offline Sync Database       : Apache CouchDB (offline-first, rural mode)
Event Bus                   : Apache Kafka
Object Storage              : MinIO (QR code images, export files)
Notification Engine         : Notification Service (R39-D)
Frontend App                : Flutter (Android · iOS · Desktop · Tablet)
Frontend Web SEO            : Next.js 14 (SSR / ISR — public pages only)
Auth Protocol               : OAuth2 + OIDC + JWT (Keycloak)
API Gateway                 : Kong OSS
Observability               : Prometheus + Grafana + Loki + OpenTelemetry
Secrets                     : HashiCorp Vault
QR Code Library             : qrcode (Python) / flutter_qr_code (Flutter)
PDF Export                  : WeasyPrint (Python, self-hosted)
```

Any deviation → STOP EXECUTION.

---

## SECTION 4 — CHECK-IN METHOD REGISTRY (LOCKED)

All check-in methods below are the only permitted methods.
No method may be added without a version bump and audit entry.

```
METHOD-1 : QR_SCAN
  Description : Attendee scans session QR code via their device camera
  Mode        : Online (requires connectivity at scan time)
  Actor       : Attendee (self-service)
  Token TTL   : Configurable per session (default 15 minutes)
  Fraud guard : One-use token, device fingerprint check, location tolerance

METHOD-2 : QR_KIOSK
  Description : Attendee scans shared QR display at venue entrance
  Mode        : Online (kiosk device requires connectivity)
  Actor       : Attendee (self-service at kiosk)
  Token TTL   : Session-duration window
  Fraud guard : Rate-limited per QR display (max 1 scan per user per session)

METHOD-3 : OTP_SELF_CHECKIN
  Description : Organizer shares 6-digit OTP; attendee enters it in app
  Mode        : Online
  Actor       : Attendee (self-service, OTP from organizer/display)
  Token TTL   : 10 minutes
  Fraud guard : Single-use, per-session OTP, rate-limited 3 attempts

METHOD-4 : MANUAL_ORGANIZER
  Description : Organizer marks individual attendee as present/absent
  Mode        : Online or offline
  Actor       : Organizer (on behalf of attendee)
  Audit       : Mandatory reason if marking ABSENT on confirmed-enrolled attendee

METHOD-5 : BULK_SESSION_MARK
  Description : Organizer marks all enrolled attendees present in one action
  Mode        : Online
  Actor       : Organizer
  Audit       : BulkMarkEvent logged with organizer_id, session_id, count
  Constraint  : Requires session window to be open

METHOD-6 : OFFLINE_SYNC
  Description : Coach/coordinator captures attendance on device without
                connectivity; syncs to server when online
  Mode        : Offline-first (CouchDB local → PostgreSQL on sync)
  Actor       : Coach or Coordinator (Society / rural franchise mode)
  Conflict    : Last-write-wins with server-side conflict flag for admin review
  Audit       : Sync timestamp, device_id, conflict count logged

METHOD-7 : ADMIN_OVERRIDE
  Description : Ops Admin forces attendance status for any record
  Mode        : Online (Ops Console only)
  Actor       : Ops Admin (Super Admin role)
  Audit       : Mandatory reason (min 20 chars), MFA confirmation required
  Constraint  : Cannot delete — only amend (previous value preserved in audit)
```

---

## SECTION 5 — DOMAIN DATA MODEL (CONTRACT-FIRST)

All schemas must be generated BEFORE any service code.
Absence of any schema → STOP EXECUTION.

### 5.1 Core PostgreSQL Entities

```sql
-- A workshop session (single day or time slot of a multi-day workshop)
WorkshopSession (
  id                    UUID PK,
  workshop_id           FK Workshop,
  tenant_id             FK Tenant,
  session_number        INT NOT NULL,        -- 1, 2, 3... for multi-day
  title                 TEXT,
  scheduled_start       TIMESTAMP NOT NULL,
  scheduled_end         TIMESTAMP NOT NULL,
  check_in_opens_at     TIMESTAMP NOT NULL,  -- enforced window open
  check_in_closes_at    TIMESTAMP NOT NULL,  -- enforced window close
  late_threshold_mins   INT DEFAULT 15,      -- minutes after start = LATE
  early_exit_threshold_mins INT DEFAULT 15,  -- mins before end = EARLY_EXIT
  is_mandatory          BOOLEAN DEFAULT TRUE,
  status                ENUM('scheduled','check_in_open','in_progress',
                              'check_in_closed','completed','cancelled'),
  created_at            TIMESTAMP DEFAULT NOW()
)

-- One record per enrolled user per session
AttendanceRecord (
  id                    UUID PK,
  session_id            FK WorkshopSession,
  workshop_id           FK Workshop,
  user_id               FK User,
  tenant_id             FK Tenant,
  enrollment_id         FK WorkshopEnrollment,
  status                ENUM('expected','present','absent','late',
                              'early_exit','present_late_exit',
                              'excused','no_show'),
  check_in_method       ENUM('qr_scan','qr_kiosk','otp_self_checkin',
                              'manual_organizer','bulk_session_mark',
                              'offline_sync','admin_override') NULLABLE,
  check_in_at           TIMESTAMP NULLABLE,
  check_out_at          TIMESTAMP NULLABLE,
  late_by_mins          INT DEFAULT 0,
  early_exit_by_mins    INT DEFAULT 0,
  device_id             TEXT NULLABLE,
  ip_address            TEXT NULLABLE,
  location_lat          DECIMAL(9,6) NULLABLE,
  location_lon          DECIMAL(9,6) NULLABLE,
  location_verified     BOOLEAN DEFAULT FALSE,
  offline_sync_id       UUID NULLABLE,       -- links to OfflineSyncBatch
  is_amended            BOOLEAN DEFAULT FALSE,
  amendment_reason      TEXT NULLABLE,
  created_at            TIMESTAMP DEFAULT NOW(),
  updated_at            TIMESTAMP DEFAULT NOW()
)

-- Computed compliance score per attendee per workshop (not per session)
AttendanceComplianceScore (
  id                    UUID PK,
  workshop_id           FK Workshop,
  user_id               FK User,
  enrollment_id         FK WorkshopEnrollment,
  total_sessions        INT NOT NULL,
  mandatory_sessions    INT NOT NULL,
  sessions_attended     INT NOT NULL,
  sessions_late         INT DEFAULT 0,
  sessions_early_exit   INT DEFAULT 0,
  sessions_excused      INT DEFAULT 0,
  sessions_absent       INT DEFAULT 0,
  attendance_pct        DECIMAL(5,2) NOT NULL,  -- 0.00 to 100.00
  compliance_threshold  DECIMAL(5,2) NOT NULL,  -- from workshop config
  is_compliant          BOOLEAN NOT NULL,
  last_computed_at      TIMESTAMP,
  UNIQUE(workshop_id, user_id)
)

-- QR token per session (one active at a time)
AttendanceQRToken (
  id                    UUID PK,
  session_id            FK WorkshopSession,
  token_value           TEXT UNIQUE NOT NULL,  -- signed JWT or UUID
  token_type            ENUM('scan','kiosk'),
  generated_at          TIMESTAMP NOT NULL,
  expires_at            TIMESTAMP NOT NULL,
  revoked_at            TIMESTAMP NULLABLE,
  use_count             INT DEFAULT 0,
  max_uses              INT NULLABLE,          -- NULL = unlimited for kiosk
  status                ENUM('active','expired','revoked','exhausted')
)

-- Single-use OTP for self check-in
AttendanceOTP (
  id                    UUID PK,
  session_id            FK WorkshopSession,
  otp_code              TEXT NOT NULL,        -- 6-digit numeric
  generated_at          TIMESTAMP NOT NULL,
  expires_at            TIMESTAMP NOT NULL,
  used_at               TIMESTAMP NULLABLE,
  used_by_user_id       FK User NULLABLE,
  status                ENUM('active','used','expired','revoked')
)

-- Offline sync batch (for rural / low-connectivity mode)
OfflineSyncBatch (
  id                    UUID PK,
  session_id            FK WorkshopSession,
  submitted_by          FK User,            -- coach or coordinator
  device_id             TEXT NOT NULL,
  records_submitted     INT NOT NULL,
  records_accepted      INT DEFAULT 0,
  records_conflicted    INT DEFAULT 0,
  records_rejected      INT DEFAULT 0,
  sync_initiated_at     TIMESTAMP,
  sync_completed_at     TIMESTAMP NULLABLE,
  status                ENUM('pending','processing','complete',
                              'partial_conflict','failed')
)

-- Per-record offline conflict detail
OfflineSyncConflict (
  id                    UUID PK,
  batch_id              FK OfflineSyncBatch,
  attendance_record_id  FK AttendanceRecord NULLABLE,
  user_id               FK User,
  conflict_type         ENUM('duplicate_checkin','timestamp_mismatch',
                              'session_closed','user_not_enrolled',
                              'invalid_method'),
  submitted_value       JSONB,
  server_value          JSONB NULLABLE,
  resolution            ENUM('server_wins','offline_wins','admin_review')
                        DEFAULT 'admin_review',
  resolved_by           FK User NULLABLE,
  resolved_at           TIMESTAMP NULLABLE
)

-- Attendance amendment record (every change is preserved)
AttendanceAmendment (
  id                    UUID PK,
  attendance_record_id  FK AttendanceRecord,
  amended_by            FK User,
  actor_role            TEXT NOT NULL,
  previous_status       TEXT NOT NULL,
  new_status            TEXT NOT NULL,
  reason                TEXT NOT NULL,        -- mandatory
  amended_at            TIMESTAMP DEFAULT NOW()
)

-- Attendance dispute (attendee challenges a no-show or absent record)
AttendanceDispute (
  id                    UUID PK,
  attendance_record_id  FK AttendanceRecord,
  raised_by             FK User,
  claim                 TEXT NOT NULL,
  evidence_file_ref     TEXT NULLABLE,        -- MinIO ref
  status                ENUM('open','under_review','resolved_upheld',
                              'resolved_overturned','closed'),
  reviewed_by           FK User NULLABLE,
  resolution_note       TEXT NULLABLE,
  opened_at             TIMESTAMP DEFAULT NOW(),
  resolved_at           TIMESTAMP NULLABLE
)

-- Immutable audit trail (append-only, never updated)
AttendanceAuditLog (
  id                    UUID PK,
  attendance_record_id  FK AttendanceRecord NULLABLE,
  session_id            FK WorkshopSession NULLABLE,
  actor_id              FK User,
  actor_role            TEXT NOT NULL,
  action                TEXT NOT NULL,
  entity_type           TEXT NOT NULL,
  entity_id             UUID NOT NULL,
  previous_value        JSONB NULLABLE,
  new_value             JSONB NULLABLE,
  metadata              JSONB,
  created_at            TIMESTAMP DEFAULT NOW()
  -- NO UPDATE EVER PERMITTED ON THIS TABLE
)
```

### 5.2 Redis State Keys (Deterministic)

```
attendance:session_status:{session_id}           → HASH  (status, opens_at, closes_at)
attendance:qr_token:{token_value}                → HASH  (session_id, type, TTL=token.expires_at)
attendance:otp:{session_id}                      → STRING (otp_code, TTL=10min)
attendance:checkin_lock:{session_id}:{user_id}   → STRING (TTL=30s idempotency lock)
attendance:bulk_lock:{session_id}                → STRING (TTL=60s bulk-mark lock)
attendance:kiosk_rate:{qr_token}:{user_id}       → STRING (TTL=session duration, value=1)
attendance:otp_attempts:{session_id}:{user_id}   → INT   (TTL=10min, max=3)
attendance:compliance:{workshop_id}:{user_id}    → HASH  (score snapshot, TTL=5min)
```

### 5.3 CouchDB Document Schema (Offline Mode)

```json
{
  "_id": "attendance::{session_id}::{user_id}",
  "type": "offline_attendance",
  "session_id": "UUID",
  "workshop_id": "UUID",
  "user_id": "UUID",
  "status": "present | absent | late",
  "check_in_method": "offline_sync",
  "check_in_at": "ISO8601",
  "check_out_at": "ISO8601 | null",
  "device_id": "string",
  "submitted_by": "UUID",
  "synced": false,
  "sync_batch_id": "UUID | null",
  "created_at": "ISO8601",
  "_rev": "CouchDB revision string"
}
```

---

## SECTION 6 — ATTENDANCE STATE MACHINE (DETERMINISTIC)

Redis-backed. No human interpretation. No AI inference.
Every transition emits Kafka event + writes AttendanceAuditLog.

```
ATTENDANCE RECORD STATE TRANSITIONS:

[INITIAL STATE]
  → EXPECTED           : Created automatically on enrollment.confirmed
                         for every mandatory session of the workshop

CHECK-IN WINDOW OPEN
EXPECTED
  → PRESENT            : Valid check-in received within window
  → LATE               : Valid check-in received AFTER late_threshold_mins
  → ABSENT             : Check-in window closes, no check-in received
                         (automated by session.check_in_closed event)

PRESENT
  → PRESENT_LATE_EXIT  : Check-out recorded before early_exit_threshold_mins
  → PRESENT            : Normal check-out (no state change)

LATE
  → PRESENT_LATE_EXIT  : Check-out before early_exit_threshold_mins
  → LATE               : Normal check-out (no state change)

ABSENT
  → EXCUSED            : Organizer or Admin marks as excused with reason
  → PRESENT            : Admin override with evidence (audit-traced)

NO_SHOW
  [synonym for ABSENT — used when user never appeared at all
   and session is fully completed]

EXCUSED
  → [TERMINAL for compliance score — excluded from threshold calc]

PRESENT_LATE_EXIT
  → [TERMINAL for compliance — counted as attended but flagged]

Any TERMINAL state may be amended ONLY by:
  - Organizer (own workshop) or Admin
  - Requires AttendanceAmendment record
  - Previous value preserved in AttendanceAuditLog
  - Cannot be deleted — only superseded

DISPUTE RESOLUTION PATH:
  ABSENT or NO_SHOW
  → DISPUTE OPENED (AttendanceDispute created)
  → UNDER_REVIEW
  → RESOLVED_UPHELD  : Status unchanged, dispute closed
  → RESOLVED_OVERTURNED : Admin creates AmendmentRecord, status updated
```

---

## SECTION 7 — COMPLIANCE SCORE ENGINE (RULE-BASED ONLY)

Per R28-1: All compliance logic is deterministic rule engine.
No ML. No AI. No confidence scoring.

```
COMPLIANCE RULE SET:

RULE-C1 — BASE SCORE COMPUTATION
  attendance_pct = (sessions_attended / mandatory_sessions) × 100
  sessions_attended includes: PRESENT, LATE, PRESENT_LATE_EXIT
  sessions_attended excludes: ABSENT, NO_SHOW
  sessions_excused: excluded from both numerator AND denominator

RULE-C2 — COMPLIANCE GATE
  is_compliant = (attendance_pct >= compliance_threshold)
  compliance_threshold: read from Workshop.attendance_threshold
  default value: 80.00%
  range: 0.00% to 100.00%

RULE-C3 — TRIGGER RECOMPUTATION ON
  Any AttendanceRecord.status change for this workshop
  Any AttendanceAmendment applied
  Any offline sync batch reconciled for this workshop

RULE-C4 — COMPLIANCE IMPACT ON CERTIFICATION
  is_compliant = FALSE
  → emit compliance.failed event to CERTIFICATION_AGENT
  → block certificate issuance (certification is OUT-OF-SCOPE for this agent)

RULE-C5 — PARTIAL SESSION CREDIT
  IF check_in_at IS NOT NULL AND check_out_at IS NULL
  → counted as PRESENT (full session credit)
  IF late_by_mins > 0 AND late_by_mins < 60
  → counted as PRESENT (with LATE flag)
  IF late_by_mins >= 60
  → counted as ABSENT (configurable threshold per workshop)
  IF early_exit_by_mins > 0 AND early_exit_by_mins < 30
  → counted as PRESENT_LATE_EXIT (with flag)
  IF early_exit_by_mins >= 30
  → counted as ABSENT (configurable threshold per workshop)

RULE-C6 — SCORE FRESHNESS
  AttendanceComplianceScore.last_computed_at must never be stale > 5min
  Redis key attendance:compliance:{workshop_id}:{user_id} TTL = 300s
  On TTL expiry: recomputation triggered asynchronously
```

---

## SECTION 8 — QR TOKEN LIFECYCLE PROTOCOL (LOCKED)

```
QR TOKEN GENERATION:

1. Organizer triggers "Open Check-In" for a session
2. Backend validates session.status == 'scheduled'
3. Backend transitions session.status → 'check_in_open'
4. AttendanceQRToken created:
   - token_value = HMAC-signed JWT (session_id + issued_at + secret)
   - token_type = 'scan' (per-user) or 'kiosk' (shared display)
   - expires_at = check_in_closes_at
5. QR image rendered server-side → stored in MinIO
6. URL returned to organizer screen
7. Attendee devices display QR scanner

QR TOKEN VALIDATION:

1. Attendee submits token_value to POST /sessions/{id}/check-in
2. Backend performs:
   a. Verify HMAC signature → if invalid → 401
   b. Check token.status == 'active' → if not → 410 GONE
   c. Check token.expires_at > NOW() → if expired → 410 GONE
   d. SETNX attendance:kiosk_rate:{token}:{user_id} TTL=session_duration
      → if already set → 409 CONFLICT (already checked in via kiosk)
   e. SETNX attendance:checkin_lock:{session_id}:{user_id} TTL=30s
      → if already set → 409 CONFLICT (idempotency guard)
   f. Verify user is enrolled in workshop (enrollment.status == 'confirmed')
      → if not → 403 FORBIDDEN
   g. Check session.check_in_closes_at > NOW() → if past → 410 GONE
3. If all pass → create AttendanceRecord
4. For scan tokens: increment token.use_count
5. Release checkin_lock

QR TOKEN REVOCATION:

Organizer or Admin may revoke active token at any time.
Revocation sets token.status = 'revoked', token.revoked_at = NOW()
Active check-ins in progress are NOT reversed.
New check-ins using revoked token → 410 GONE.
Revocation emits: attendance.qr_revoked event.
```

---

## SECTION 9 — OFFLINE SYNC PROTOCOL (R59 COMPLIANCE)

```
OFFLINE CAPTURE (CouchDB — device local):

1. Coach/Coordinator opens session in offline mode
2. App loads enrolled attendee list from local CouchDB replica
3. Coach marks each attendee: present / absent / late
4. Each mark writes CouchDB document immediately (no server call)
5. App shows "offline — syncing when connected" indicator

SYNC ON CONNECTIVITY RESTORE:

1. App detects connectivity
2. App submits OfflineSyncBatch:
   POST /sessions/{id}/offline-sync
   Body: { batch records from CouchDB }
3. Backend processes each record:

   FOR EACH record:
     a. Check session exists and is valid
     b. Check user is enrolled
     c. Check for conflict with server record:
        IF server AttendanceRecord already exists:
          - If server status == 'expected' → accept offline record
          - If server status != 'expected' → CONFLICT
            → Create OfflineSyncConflict, set resolution = 'admin_review'
        IF no server record → create AttendanceRecord from offline data
     d. Mark CouchDB record synced = true

4. OfflineSyncBatch.status updated:
   - all accepted → 'complete'
   - some conflicts → 'partial_conflict'
   - all failed → 'failed'

5. Events emitted:
   - attendance.offline_sync_complete (per batch)
   - attendance.sync_conflict (per conflict record)

6. Coach notified of conflict count via notification

CONFLICT RESOLUTION (Admin):
  Admin views OfflineSyncConflict in Ops Console
  Chooses: server_wins | offline_wins
  Action creates AttendanceAmendment if offline_wins
  Every resolution writes AttendanceAuditLog
```

---

## SECTION 10 — API CONTRACT (OpenAPI 3.1 — LOCKED)

All endpoints mandatory. Absence → STOP EXECUTION.

```yaml
/sessions/{session_id}/check-in-open:
  POST:
    summary: Organizer opens check-in window, generates QR token
    auth: REQUIRED (JWT, organizer role)
    response: { qr_token_url, otp_code, session_status, closes_at }
    events: [attendance.checkin_window_opened]

/sessions/{session_id}/check-in-close:
  POST:
    summary: Organizer closes check-in window manually
    auth: REQUIRED (JWT, organizer role)
    events: [attendance.checkin_window_closed, attendance.absent_auto_marked]

/sessions/{session_id}/check-in:
  POST:
    summary: Attendee checks in (QR scan, OTP, or self-service)
    auth: REQUIRED (JWT)
    body: CheckInRequest { method, token_value | otp_code }
    response: AttendanceRecord
    events: [attendance.marked_present | attendance.marked_late]

/sessions/{session_id}/check-out:
  POST:
    summary: Attendee checks out (optional — early exit detection)
    auth: REQUIRED (JWT)
    response: AttendanceRecord (updated)
    events: [attendance.checked_out | attendance.early_exit_flagged]

/sessions/{session_id}/attendance:
  GET:
    summary: Organizer views real-time attendance roster for session
    auth: REQUIRED (JWT, organizer or admin role)
    query: [status_filter, search_name]
    response: AttendanceRosterResponse

/sessions/{session_id}/attendance/{user_id}:
  PATCH:
    summary: Organizer manually amends single attendee status
    auth: REQUIRED (JWT, organizer or admin role)
    body: { status, reason }
    response: AttendanceRecord
    events: [attendance.amended]

/sessions/{session_id}/attendance/bulk-mark:
  POST:
    summary: Mark all enrolled attendees present in bulk
    auth: REQUIRED (JWT, organizer role)
    response: BulkMarkResponse { marked_count, skipped_count }
    events: [attendance.bulk_marked]

/sessions/{session_id}/qr-token/revoke:
  POST:
    summary: Revoke the active QR token for a session
    auth: REQUIRED (JWT, organizer or admin role)
    events: [attendance.qr_revoked]

/sessions/{session_id}/qr-token/refresh:
  POST:
    summary: Revoke current QR token and issue a new one
    auth: REQUIRED (JWT, organizer or admin role)
    response: { new_qr_token_url, expires_at }

/sessions/{session_id}/offline-sync:
  POST:
    summary: Submit offline attendance batch for sync
    auth: REQUIRED (JWT, coach or coordinator role)
    body: OfflineSyncBatchRequest
    response: OfflineSyncBatchResponse
    events: [attendance.offline_sync_complete]

/workshops/{workshop_id}/compliance:
  GET:
    summary: Get compliance score for all attendees of a workshop
    auth: REQUIRED (JWT, organizer, institution admin, or admin)
    response: ComplianceRosterResponse

/workshops/{workshop_id}/compliance/{user_id}:
  GET:
    summary: Get individual attendee compliance score
    auth: REQUIRED (JWT, own record, organizer, or admin)
    response: AttendanceComplianceScore

/workshops/{workshop_id}/attendance/export:
  GET:
    summary: Export full attendance data as CSV or PDF
    auth: REQUIRED (JWT, organizer or institution admin role)
    query: [format=csv|pdf, session_id, status_filter]
    response: File download (MinIO pre-signed URL)
    events: [attendance.export_generated]

/workshops/{workshop_id}/attendance/govt-report:
  GET:
    summary: Generate government scheme attendance report
           (PMKVY / DDU-GKY format)
    auth: REQUIRED (JWT, institution admin or govt compliance role)
    response: File download (PDF)

/disputes:
  POST:
    summary: Attendee raises attendance dispute
    auth: REQUIRED (JWT)
    body: DisputeRequest { attendance_record_id, claim, evidence_file }
    response: AttendanceDispute
    events: [attendance.dispute_opened]

/disputes/{dispute_id}:
  GET:
    summary: Get dispute status
    auth: REQUIRED (JWT, own dispute, organizer, or admin)

/disputes/{dispute_id}/resolve:
  POST:
    summary: Admin or organizer resolves dispute
    auth: REQUIRED (JWT, organizer or admin role)
    body: { resolution: upheld|overturned, note }
    events: [attendance.dispute_resolved]

/admin/attendance/{record_id}/override:
  POST:
    summary: Admin force-sets attendance status
    auth: REQUIRED (JWT, super_admin or ops_admin role, MFA)
    body: { status, reason (min 20 chars) }
    events: [attendance.admin_override]

/sessions/{session_id}/offline-conflicts:
  GET:
    summary: Admin views offline sync conflicts for a session
    auth: REQUIRED (JWT, admin role)
    response: OfflineSyncConflictListResponse

/disputes/{dispute_id}/conflicts/{conflict_id}/resolve:
  POST:
    summary: Admin resolves individual offline sync conflict
    auth: REQUIRED (JWT, admin role)
    body: { resolution: server_wins|offline_wins, reason }
    events: [attendance.conflict_resolved]

/health:
  GET:
    summary: Workshop Attendance Service health check
    auth: NONE
    response: { status: "ok", version: string, timestamp: string }
```

---

## SECTION 11 — EVENT SCHEMA (Kafka — LOCKED)

All events mandatory. Absence → STOP EXECUTION.

```
attendance.checkin_window_opened
  payload: { session_id, workshop_id, organizer_id, tenant_id,
             check_in_opens_at, check_in_closes_at,
             qr_token_id, otp_generated: boolean, timestamp }

attendance.checkin_window_closed
  payload: { session_id, workshop_id, auto_closed: boolean,
             total_expected, total_checked_in, timestamp }

attendance.absent_auto_marked
  payload: { session_id, workshop_id,
             absent_user_ids: [UUID], count: int, timestamp }

attendance.marked_present
  payload: { attendance_record_id, session_id, workshop_id,
             user_id, check_in_method, check_in_at,
             late_by_mins: 0, timestamp }

attendance.marked_late
  payload: { attendance_record_id, session_id, workshop_id,
             user_id, check_in_method, check_in_at,
             late_by_mins: int, timestamp }

attendance.checked_out
  payload: { attendance_record_id, session_id, workshop_id,
             user_id, check_out_at, duration_mins: int, timestamp }

attendance.early_exit_flagged
  payload: { attendance_record_id, session_id, workshop_id,
             user_id, early_exit_by_mins: int, timestamp }

attendance.amended
  payload: { amendment_id, attendance_record_id, session_id,
             workshop_id, amended_by, previous_status, new_status,
             reason, timestamp }

attendance.bulk_marked
  payload: { session_id, workshop_id, organizer_id,
             marked_count, skipped_count, timestamp }

attendance.qr_revoked
  payload: { qr_token_id, session_id, workshop_id,
             revoked_by, reason, timestamp }

attendance.offline_sync_complete
  payload: { batch_id, session_id, workshop_id, submitted_by,
             device_id, records_submitted, records_accepted,
             records_conflicted, records_rejected, timestamp }

attendance.sync_conflict
  payload: { conflict_id, batch_id, session_id, user_id,
             conflict_type, timestamp }

attendance.conflict_resolved
  payload: { conflict_id, session_id, user_id,
             resolution, resolved_by, timestamp }

attendance.compliance_computed
  payload: { workshop_id, user_id, attendance_pct,
             is_compliant, threshold, timestamp }

compliance.failed
  payload: { workshop_id, user_id, attendance_pct,
             threshold, sessions_attended, mandatory_sessions,
             timestamp }
  consumers: [CERTIFICATION_AGENT, NOTIFICATION_SERVICE]

compliance.achieved
  payload: { workshop_id, user_id, attendance_pct, timestamp }
  consumers: [CERTIFICATION_AGENT, NOTIFICATION_SERVICE]

attendance.export_generated
  payload: { workshop_id, generated_by, format, file_ref,
             session_filter, timestamp }

attendance.dispute_opened
  payload: { dispute_id, attendance_record_id, raised_by,
             workshop_id, timestamp }

attendance.dispute_resolved
  payload: { dispute_id, resolution, resolved_by, timestamp }

attendance.admin_override
  payload: { attendance_record_id, actor_admin_id, previous_status,
             new_status, reason, timestamp }
```

Event Consumers:

| Event | Consumer |
|---|---|
| compliance.failed | CERTIFICATION_AGENT, NOTIFICATION_SERVICE |
| compliance.achieved | CERTIFICATION_AGENT, NOTIFICATION_SERVICE |
| attendance.marked_present | ANALYTICS_SERVICE |
| attendance.absent_auto_marked | NOTIFICATION_SERVICE (parent/guardian if minor) |
| attendance.admin_override | ANALYTICS_SERVICE, OPS_CONSOLE |
| attendance.dispute_opened | NOTIFICATION_SERVICE (organizer) |
| ALL events | ANALYTICS_SERVICE, AUDIT_SERVICE |

---

## SECTION 12 — NOTIFICATION RULES (DETERMINISTIC)

All notifications via Notification Service. No direct SMTP/SMS from agent.

| Event Trigger | Recipient | Channel | Template ID |
|---|---|---|---|
| attendance.checkin_window_opened | All enrolled attendees | Push + SMS | CHECKIN_OPEN |
| attendance.marked_present | Attendee | Push | CHECKIN_CONFIRMED |
| attendance.marked_late | Attendee | Push | CHECKIN_LATE_NOTE |
| attendance.absent_auto_marked | Attendee | Push + Email | MARKED_ABSENT |
| attendance.absent_auto_marked (minor) | Guardian | Email + SMS | GUARDIAN_ABSENT_ALERT |
| attendance.early_exit_flagged | Attendee | Push | EARLY_EXIT_FLAG |
| attendance.amended (to absent) | Attendee | Email | ATTENDANCE_AMENDED |
| compliance.failed | Attendee + Organizer | Email + Push | COMPLIANCE_FAILED |
| compliance.achieved | Attendee | Push | COMPLIANCE_MET |
| attendance.dispute_opened | Organizer | Email + Push | DISPUTE_RAISED |
| attendance.dispute_resolved | Attendee | Email | DISPUTE_RESOLVED |
| attendance.sync_conflict | Coach + Admin | Email | SYNC_CONFLICT_ALERT |
| attendance.offline_sync_complete | Coach | Push | SYNC_DONE |

---

## SECTION 13 — UI SCREENS (Flutter — LOCKED)

Per R29 & R43: All screens must be fully clickable, state-driven, role-scoped.
No static wireframes. No unwired navigation. Partial screens → INVALID.

### SCREEN-1: Organizer Session Control Panel (Role: Organizer)

```
Purpose        : Central control for a session's attendance lifecycle
Entry          : Organizer Dashboard → Workshop → Session → Manage Attendance
Exit           : Attendance Roster, Close Check-In, Export
Fixed Area     : Session title, status badge, time remaining countdown
Live Data      : Present count / Total enrolled (live WebSocket update)
Actions        :
  - Open Check-In (disabled if window not yet reached)
  - Close Check-In (enabled once open)
  - Regenerate QR (revokes old, issues new)
  - Bulk Mark All Present
  - Export Attendance
State variants : scheduled | check_in_open | in_progress | check_in_closed | completed
API            : GET/POST /sessions/{id}/check-in-open, /check-in-close
WebSocket      : attendance:session:{id} (live count feed)
Flutter file   : lib/modules/events/attendance/screens/
                 organizer_session_control_screen.dart
```

### SCREEN-2: QR Code Display (Role: Organizer)

```
Purpose        : Display session QR code for attendees to scan
Entry          : Session Control Panel → "Show QR Code"
Exit           : Back to Control Panel, Auto-refresh on revoke
Fixed Area     : QR image (full-screen option), session name, expiry timer
Live Data      : Token expiry countdown timer (decrement every second)
Behavior       : Auto-refresh QR image when token regenerated
                 Display OTP code below QR as fallback
Flutter file   : lib/modules/events/attendance/screens/
                 qr_display_screen.dart
```

### SCREEN-3: Attendee Self Check-In (Role: Student / Attendee)

```
Purpose        : Attendee scans QR or enters OTP to mark themselves present
Entry          : Notification tap "Check-in now" → deep link to session
                 OR main nav → My Workshops → Session → Check In
Exit           : Check-In Confirmation screen (SCREEN-4)
Fixed Area     : Session title, scheduled time, workshop name
States         :
  - Ready to scan (camera view + OTP entry tab)
  - Check-in success → transition to SCREEN-4
  - Already checked in → show current status badge
  - Window closed → show "Check-in window has closed" state
  - Not enrolled → show 403 error state
Tabs           : QR Scanner | Enter OTP
API            : POST /sessions/{id}/check-in
Flutter file   : lib/modules/events/attendance/screens/
                 attendee_checkin_screen.dart
```

### SCREEN-4: Check-In Confirmation (Role: Student / Attendee)

```
Purpose        : Confirm successful check-in with status details
Entry          : SCREEN-3 on success
Exit           : Back to My Workshops, tap to view session info
Shows          : Status badge (PRESENT / LATE with minutes),
                 Check-in timestamp, Session details, Compliance progress
Late indicator : If late_by_mins > 0 → display "You arrived X min late"
Flutter file   : lib/modules/events/attendance/screens/
                 checkin_confirmation_screen.dart
```

### SCREEN-5: Kiosk Mode Check-In (Role: Shared Device / Kiosk)

```
Purpose        : Tablet at venue entrance — attendees scan one by one
Entry          : Kiosk mode URL or PIN-locked app mode
Exit           : Auto-reset after each scan (3s), manual organizer exit
Fixed Area     : Large QR scanner, session name, countdown timer
Behavior       :
  - Each successful scan shows attendee name + status for 3s
  - Auto-resets for next attendee
  - Rate-limited: same attendee cannot scan twice
  - No attendee personal data stored on kiosk device
Security       : PIN-locked app mode, no JWT stored on device,
                 uses short-lived kiosk token from backend
Flutter file   : lib/modules/events/attendance/screens/
                 kiosk_checkin_screen.dart
```

### SCREEN-6: Attendance Roster (Role: Organizer)

```
Purpose        : Organizer views all attendees and their real-time status
Entry          : Session Control Panel → "View Roster"
Exit           : Amend individual record, back to control
Columns        : Name, Enrollment ID, Status badge, Check-in time, Late flag,
                 Check-out time, Early exit flag, Check-in method
Filter tabs    : All | Present | Absent | Late | Expected | Excused
Actions        :
  - Tap attendee row → open Amendment Modal
  - Search by name
  - Export CSV button
Live update    : WebSocket feed refreshes status in real-time
API            : GET /sessions/{id}/attendance
Flutter file   : lib/modules/events/attendance/screens/
                 attendance_roster_screen.dart
```

### SCREEN-7: Attendance Amendment Modal (Role: Organizer / Admin)

```
Purpose        : Amend a single attendee's attendance status
Entry          : Roster row tap → modal overlay
Exit           : Dismiss modal, roster refreshes
Fields         :
  - Current status (read-only badge)
  - New status dropdown (PRESENT / ABSENT / LATE / EXCUSED)
  - Reason (mandatory text field, min 10 chars)
  - Confirm button (disabled until reason filled)
Audit          : Writes AttendanceAmendment + AttendanceAuditLog
API            : PATCH /sessions/{id}/attendance/{user_id}
Flutter file   : lib/modules/events/attendance/screens/
                 attendance_amendment_modal.dart
```

### SCREEN-8: My Attendance Summary (Role: Student / Attendee)

```
Purpose        : Student views their attendance across all sessions of a workshop
Entry          : My Workshops → Workshop Detail → Attendance tab
Exit           : Raise Dispute (SCREEN-11), back to workshop
Shows          :
  - Compliance progress bar (current_pct / threshold)
  - Per-session attendance status list
  - Sessions remaining
  - "At risk" warning if pct near threshold
  - Compliant / Not Compliant badge
API            : GET /workshops/{id}/compliance/{user_id}
Flutter file   : lib/modules/events/attendance/screens/
                 my_attendance_summary_screen.dart
```

### SCREEN-9: Offline Attendance Capture (Role: Coach / Coordinator)

```
Purpose        : Offline attendance marking in rural / low-connectivity mode
Entry          : Session Detail → "Mark Attendance Offline"
Exit           : Sync screen (SCREEN-10)
Fixed Area     : Offline mode indicator banner (red), session details
List           : All enrolled attendees (loaded from CouchDB local replica)
Actions per row: Present | Late | Absent toggle
Behavior       : All changes saved locally in CouchDB immediately
                 Sync button enabled when connectivity detected
Flutter file   : lib/modules/events/attendance/screens/
                 offline_attendance_screen.dart
```

### SCREEN-10: Offline Sync Status (Role: Coach / Coordinator)

```
Purpose        : Show sync progress and conflict summary after reconnecting
Entry          : SCREEN-9 → Sync button tap, or auto-detected connectivity
Exit           : Conflict resolution (admin), back to session
Shows          :
  - Sync progress bar (records synced / total)
  - Accepted count (green)
  - Conflicted count (amber) → "X records need admin review"
  - Rejected count (red) with reasons
  - Success confirmation when complete
API            : POST /sessions/{id}/offline-sync
Flutter file   : lib/modules/events/attendance/screens/
                 offline_sync_status_screen.dart
```

### SCREEN-11: Raise Attendance Dispute (Role: Student / Attendee)

```
Purpose        : Attendee challenges an ABSENT or NO_SHOW record
Entry          : My Attendance Summary → Dispute button on flagged row
Exit           : Dispute confirmation, My Attendance Summary
Fields         :
  - Attendance record reference (pre-filled, read-only)
  - Claim description (required, min 30 chars)
  - Evidence upload (optional, max 5MB, image or PDF)
  - Submit button
Constraint     : Dispute only allowed within 48h of session completion
API            : POST /disputes
Flutter file   : lib/modules/events/attendance/screens/
                 raise_dispute_screen.dart
```

### SCREEN-12: Compliance Dashboard — Workshop Level (Role: Organizer / Institution Admin)

```
Purpose        : View compliance status of all attendees for a workshop
Entry          : Workshop Management → Compliance tab
Exit           : Individual attendee detail, export
Shows          :
  - Workshop-level compliance summary pie chart
  - Compliant count / At-risk count / Non-compliant count
  - Attendee roster with compliance % per person
  - Filter: All | Compliant | At Risk | Non-Compliant
  - Export compliance report button
API            : GET /workshops/{id}/compliance
Flutter file   : lib/modules/events/attendance/screens/
                 compliance_dashboard_screen.dart
```

### SCREEN-13: Admin Override Panel (Role: Ops Admin)

```
Purpose        : Admin force-sets attendance status with full audit
Entry          : Internal Ops Console (R40) → Attendance → Override
Exit           : Audit log confirmation
Fields         :
  - Attendance Record ID lookup
  - Current status (read-only)
  - New status selector
  - Reason (mandatory, min 20 chars)
  - MFA confirmation step
Audit          : AttendanceAmendment + AttendanceAuditLog + Kafka event
API            : POST /admin/attendance/{record_id}/override
Flutter file   : lib/modules/admin/attendance/
                 attendance_override_screen.dart
```

### SCREEN-14: Offline Sync Conflict Resolver (Role: Ops Admin)

```
Purpose        : Admin resolves offline sync conflicts
Entry          : Ops Console → Attendance → Offline Conflicts
Exit           : Conflict resolved, attendance record updated
Shows          :
  - Session info, attendee name
  - Server value vs. Offline submitted value (side-by-side)
  - Conflict type label
  - Resolution buttons: "Keep Server" | "Accept Offline"
  - Reason text field
API            : GET /sessions/{id}/offline-conflicts
                 POST /disputes/{id}/conflicts/{id}/resolve
Flutter file   : lib/modules/admin/attendance/
                 offline_conflict_resolver_screen.dart
```

---

## SECTION 14 — NEXT.JS SEO PAGES (LOCKED)

Per R31: Only public/discoverable pages are server-rendered.
Attendance data is private. No attendance detail is publicly indexed.

```
/workshops/[slug]/sessions        → Public session schedule list (SSR)
  - Shows session dates, times, check-in mode (no personal data)
  - Schema.org Event markup per session

/workshops/[slug]/attendance-policy → Public attendance policy page (ISR 1h)
  - Threshold, compliance rules, dispute process explained
  - noindex NOT set (discoverable)
```

All other attendance screens → Flutter app only → noindex enforced.

---

## SECTION 15 — BACKEND SERVICE STRUCTURE (INTERN-EXECUTABLE)

```
/backend/services/workshop_attendance_service/
├── main.py                              ← FastAPI app entrypoint
├── requirements.txt
├── .env.example
├── Dockerfile
├── alembic/
│   ├── env.py
│   └── versions/
│       ├── 001_create_session_tables.py
│       ├── 002_create_attendance_records.py
│       ├── 003_create_compliance_scores.py
│       ├── 004_create_qr_otp_tokens.py
│       ├── 005_create_offline_sync.py
│       ├── 006_create_disputes.py
│       └── 007_create_audit_log.py
├── app/
│   ├── api/
│   │   └── v1/
│   │       ├── checkin.py               ← QR, OTP, self check-in
│   │       ├── checkout.py              ← Check-out, early exit
│   │       ├── roster.py                ← Roster view, amendment
│   │       ├── bulk_mark.py             ← Bulk mark present
│   │       ├── offline_sync.py          ← Offline batch submit
│   │       ├── compliance.py            ← Compliance score endpoints
│   │       ├── export.py                ← CSV / PDF export
│   │       ├── disputes.py              ← Dispute CRUD
│   │       ├── admin.py                 ← Admin override, conflict resolve
│   │       └── health.py
│   ├── core/
│   │   ├── config.py
│   │   ├── security.py                  ← JWT, MFA check
│   │   └── database.py
│   ├── services/
│   │   ├── checkin_engine.py            ← Check-in validation + state write
│   │   ├── qr_token_service.py          ← Token generation, validation, revoke
│   │   ├── otp_service.py               ← OTP generation, validation
│   │   ├── state_machine.py             ← Attendance state transitions
│   │   ├── compliance_engine.py         ← R28-1 rule-based compliance score
│   │   ├── offline_sync_service.py      ← CouchDB → PostgreSQL reconcile
│   │   ├── export_service.py            ← CSV / PDF generation (WeasyPrint)
│   │   ├── dispute_service.py           ← Dispute lifecycle
│   │   ├── notification_dispatcher.py   ← Kafka → Notification Service
│   │   └── audit_service.py             ← AttendanceAuditLog writer
│   ├── models/
│   │   ├── session.py
│   │   ├── attendance_record.py
│   │   ├── compliance_score.py
│   │   ├── qr_token.py
│   │   ├── otp.py
│   │   ├── offline_sync.py
│   │   ├── dispute.py
│   │   └── audit_log.py
│   ├── schemas/
│   │   ├── checkin_schemas.py
│   │   ├── compliance_schemas.py
│   │   ├── offline_schemas.py
│   │   └── dispute_schemas.py
│   └── events/
│       ├── producers.py                 ← Kafka event publishers
│       └── event_types.py
└── tests/
    ├── test_checkin_engine.py
    ├── test_qr_token_lifecycle.py
    ├── test_otp_service.py
    ├── test_compliance_engine.py
    ├── test_offline_sync.py
    ├── test_state_machine.py
    └── test_api_attendance.py
```

---

## SECTION 16 — INTERN EXECUTION STEPS (R46 COMPLIANCE)

### Step 1: Install Dependencies
```bash
cd /backend/services/workshop_attendance_service
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```
Expected: `Successfully installed fastapi uvicorn sqlalchemy alembic
           redis kafka-python couchdb qrcode weasyprint`

### Step 2: Set Environment
```bash
cp .env.example .env
# Edit .env:
# DATABASE_URL=postgresql://ecoskiller:ecoskiller@localhost:5432/ecoskiller
# REDIS_URL=redis://localhost:6379/0
# COUCHDB_URL=http://admin:password@localhost:5984
# KAFKA_BOOTSTRAP=localhost:9092
# MINIO_ENDPOINT=localhost:9000
# MINIO_ACCESS_KEY=<from Vault>
# MINIO_SECRET_KEY=<from Vault>
# QR_HMAC_SECRET=<from Vault>
# JWT_JWKS_URL=http://keycloak:8080/realms/ecoskiller/protocol/openid-connect/certs
# SERVICE_PORT=8021
```

### Step 3: Run Migrations
```bash
alembic upgrade head
```
Expected: `Running upgrade -> 007_create_audit_log ... done`

### Step 4: Start Service
```bash
uvicorn main:app --reload --port 8021
```
Expected: `Uvicorn running on http://127.0.0.1:8021`

### Step 5: Verify API
Open browser: `http://127.0.0.1:8021/docs`
Expected: Swagger UI with all attendance endpoints visible.

### Step 6: Run Tests
```bash
pytest tests/ -v
```
Expected: `All tests passed — compliance engine, state machine, QR lifecycle verified`

### Step 7: Test Offline Sync (local CouchDB)
```bash
# CouchDB must be running locally (docker run -d -p 5984:5984 couchdb)
python tests/test_offline_sync.py --session_id <test_session_uuid>
```
Expected: `Offline batch submitted: 10 records, 0 conflicts`

---

## SECTION 17 — SECURITY ENFORCEMENT (R10 COMPLIANCE)

```
RULE-S1 — JWT VALIDATION
  All non-public endpoints require valid Bearer JWT.
  Validated against Keycloak JWKS endpoint.
  Expired → 401. Invalid signature → 401.

RULE-S2 — ORGANIZER SCOPE ISOLATION
  Organizer may only manage sessions for workshops they own.
  Cross-workshop session access → 403.

RULE-S3 — ATTENDEE RECORD ISOLATION
  Attendee may only view and act on their own attendance records.
  Attempt to view other user's record → 403.

RULE-S4 — QR TOKEN SIGNATURE
  All QR tokens are HMAC-signed JWTs.
  Secret stored in HashiCorp Vault, rotated every 24h.
  Any token with invalid signature → 401 rejected at validation.

RULE-S5 — OTP BRUTE-FORCE GUARD
  Max 3 OTP attempts per user per session per 10-minute window.
  4th attempt → 429 Too Many Requests.
  Attempt count tracked in Redis with TTL = 10 minutes.

RULE-S6 — KIOSK DEVICE ISOLATION
  Kiosk mode uses short-lived kiosk token (TTL = session duration).
  Kiosk token does NOT grant any other API access.
  Kiosk device cannot read attendee roster or compliance data.

RULE-S7 — ADMIN OVERRIDE MFA
  POST /admin/attendance/{id}/override requires MFA confirmation.
  MFA token must be submitted in request header alongside JWT.
  No MFA → 403 (not 401 — identity known, action forbidden).

RULE-S8 — OFFLINE SYNC DEVICE IDENTITY
  Every OfflineSyncBatch must include device_id.
  device_id must match a registered device for the submitting user.
  Unregistered device_id → 403.

RULE-S9 — BULK MARK RATE LIMIT
  POST /sessions/{id}/attendance/bulk-mark: 1 request per session
  per 60-second window per organizer.
  Violation → 429.

RULE-S10 — AUDIT LOG IMMUTABILITY
  AttendanceAuditLog table has no UPDATE or DELETE permission
  granted to the application database user.
  Only INSERT permitted.
  Any attempt to modify → rejected at database level.

RULE-S11 — LOCATION DATA (OPTIONAL, GDPR-SAFE)
  location_lat / location_lon are optional fields.
  Collected ONLY if check-in method includes location verification.
  Must never be logged in application logs.
  Stored encrypted at rest (AES-256 via PostgreSQL pgcrypto).

RULE-S12 — DISPUTE EVIDENCE STORAGE
  Evidence files uploaded to MinIO in private bucket.
  Pre-signed URL generated per request, TTL = 15 minutes.
  Direct public URL access → forbidden.
```

---

## SECTION 18 — OBSERVABILITY (NON-OPTIONAL)

```
Metrics (Prometheus):
  attendance_checkins_total{method, session_id, status}
  attendance_late_arrivals_total{workshop_id}
  attendance_early_exits_total{workshop_id}
  attendance_absent_auto_marked_total{workshop_id}
  compliance_score_distribution{workshop_id, bucket}
  qr_token_validation_failures_total{failure_reason}
  otp_brute_force_attempts_total{session_id}
  offline_sync_batches_total{status}
  offline_sync_conflicts_total{conflict_type}
  attendance_disputes_total{status}
  attendance_export_requests_total{format}

Logs (Loki — structured JSON, no PII in log fields):
  level, timestamp, service=workshop_attendance,
  session_id, workshop_id, actor_id, action, result, method

Traces (OpenTelemetry):
  Span per API request
  Child spans: qr_validate, otp_validate, state_transition,
               compliance_compute, kafka_publish, db_write, minio_write

Dashboards (Grafana — mandatory panels):
  Real-time check-in rate per session (live)
  Late arrival rate by workshop
  Compliance threshold pass/fail ratio
  Offline sync success rate and conflict backlog
  Dispute open/resolution rate
  QR token fraud attempts (validation failures)
  Admin override frequency (anomaly detection)
  Export generation queue depth
```

---

## SECTION 19 — FAILURE HANDLING (DETERMINISTIC)

```
FAILURE-F1 — DATABASE UNAVAILABLE
  → Return 503 with Retry-After: 30 header
  → Log CRITICAL db_connection_failure to Loki
  → Do NOT silently return empty attendance data

FAILURE-F2 — REDIS UNAVAILABLE (QR / OTP VALIDATION)
  → QR and OTP check-in fail safely → return 503
  → Fallback: organizer manual check-in (METHOD-4) remains available
  → Alert emitted: attendance_redis_failure to Prometheus alertmanager

FAILURE-F3 — KAFKA UNAVAILABLE (EVENT PUBLISH)
  → Event written to PostgreSQL outbox table
  → Airflow retry worker flushes outbox every 60 seconds
  → Attendance transaction commits — event delivery guaranteed eventually
  → Do NOT block attendance marking on event publish failure

FAILURE-F4 — COUCHDB UNAVAILABLE (OFFLINE SYNC SUBMISSION)
  → Return 503 with retry guidance
  → Coach retains data in local CouchDB until connectivity restored
  → No data loss — offline data persists on device

FAILURE-F5 — MINIO UNAVAILABLE (QR IMAGE / EXPORT)
  → QR display falls back to text-only OTP display
  → Export requests return 503 with retry instructions
  → Alert emitted to Prometheus

FAILURE-F6 — COMPLIANCE RECOMPUTATION FAILURE
  → Stale cache served (up to 5 minutes)
  → Background retry queued
  → Do NOT serve incorrect compliance result — serve stale with warning flag
  → Log WARNING compliance_compute_failure

FAILURE-F7 — CHECKIN WINDOW AUTO-CLOSE FAILURE
  → Airflow cron runs POST /sessions/{id}/check-in-close at check_in_closes_at
  → If cron fails: attendance records remain in EXPECTED state
  → Airflow failure alert → manual operator action required
  → No auto-absent marking without confirmed window closure

FAILURE-F8 — OFFLINE SYNC CONFLICT (UNRESOLVABLE)
  → Record flagged OfflineSyncConflict.resolution = 'admin_review'
  → Admin notified within 15 minutes via Notification Service
  → Attendance record remains in last-known server state until resolved
  → Conflict older than 7 days → escalated to Ops Console CRITICAL queue
```

---

## SECTION 20 — ANTI-ABUSE CONTROLS (R51 COMPLIANCE)

```
ABUSE-A1 — QR CODE SCREENSHOT SHARING
  Mitigation: Session QR tokens expire at check_in_closes_at.
  Rotating QR: Organizer may regenerate QR mid-window to invalidate
  shared screenshots. Regeneration emits attendance.qr_revoked.

ABUSE-A2 — QR CODE FORWARDING (REMOTE CHECK-IN)
  Mitigation: Kiosk token rate-limits one scan per user per session.
  For SCAN type tokens: device fingerprint logged; anomaly flagged if
  check-in IP geolocation differs > 50km from venue (if location enabled).

ABUSE-A3 — OTP SHARING
  Mitigation: OTP valid for 10 minutes only. Single-use after first success.
  Brute-force guard: 3 attempt limit per user per session (RULE-S5).

ABUSE-A4 — BULK MARK ABUSE (ORGANIZER MARKING ALL PRESENT WITHOUT VERIFICATION)
  Mitigation: BulkMarkEvent logged with organizer_id and timestamp.
  Analytics Service detects sessions where bulk_mark used AND
  previous QR check-in count = 0 → flagged for audit review.
  Repeated bulk-mark-only patterns → Ops Console alert.

ABUSE-A5 — FRAUDULENT OFFLINE SYNC
  Mitigation: device_id must be pre-registered to user.
  Timestamps in offline records validated: check_in_at must be
  within session window ± 30 minutes tolerance.
  Timestamps in future → rejected as INVALID.
  Same user marked by two different devices → CONFLICT.

ABUSE-A6 — SPURIOUS DISPUTE FLOODING
  Mitigation: Max 3 open disputes per user per workshop.
  Dispute submission rate-limited: 1 per 24 hours per session.
  False dispute history (resolved_upheld > 2) → dispute badge warning.

ABUSE-A7 — ADMIN OVERRIDE FREQUENCY ANOMALY
  Analytics detects: admin_overrides > 5 per session → alert to
  Super Admin and Ops Console. Pattern review initiated.
```

---

## SECTION 21 — GOVERNMENT SCHEME ATTENDANCE REPORT (PMKVY / DDU-GKY)

```
PURPOSE:
  Generate structured attendance logs required by PMKVY (Pradhan Mantri
  Kaushal Vikas Yojana) and DDU-GKY (Deen Dayal Upadhyaya Grameen
  Kaushalya Yojana) government schemes.

TRIGGER:
  GET /workshops/{id}/attendance/govt-report
  Role required: institution_admin OR govt_compliance_officer

REQUIRED FIELDS IN OUTPUT:
  - Workshop name, scheme name, batch ID
  - Student name, Aadhaar-masked (last 4 digits shown only)
  - Session date, session number
  - Attendance status (P = Present, A = Absent, L = Late)
  - Total attendance % per student
  - Trainer name and certification number
  - Institution name, registration number
  - Report generated date and authorized signatory field

FORMAT:
  PDF (WeasyPrint) — government-formatted layout
  Excel / CSV — for digital submission portals

DATA RULES:
  - Aadhaar must be masked: XXXX-XXXX-1234 format only
  - No biometric data extracted or stored
  - Report is point-in-time snapshot (not live)
  - Report generation logged in AttendanceAuditLog

COMPLIANCE NOTE (R28-1):
  Report computation is rule-based only.
  No ML inference on attendance patterns for government report.
```

---

## SECTION 22 — ADMIN OPS CONSOLE INTEGRATION (R40 COMPLIANCE)

The following must be present in the Internal Ops Console:

```
Module: Attendance Management

Panels:
  ├── Session Attendance Monitor (real-time per session)
  │     → Live present/absent/expected counts
  │     → QR token status indicator
  ├── Attendance Amendment Queue
  │     → Pending amendments awaiting second-level review
  ├── Offline Sync Conflict Resolver (SCREEN-14)
  │     → List of unresolved conflicts with resolve actions
  ├── Dispute Management Dashboard
  │     → Open disputes, age, assigned reviewer
  │     → Resolve dispute action (SCREEN-13 equivalent)
  ├── Admin Override Panel (SCREEN-13)
  │     → Force-set any attendance record
  │     → MFA required
  ├── Compliance Anomaly Dashboard
  │     → Workshops with bulk-mark-only sessions flagged
  │     → Admins with high override frequency flagged
  ├── Government Report Generator
  │     → Generate PMKVY / DDU report by workshop + batch
  └── Attendance Audit Log Explorer
        → Search by session_id, user_id, actor_id, date range
        → Read-only, append-only view

All Ops Console actions:
  → Require MFA for override and amendment operations
  → Write to AttendanceAuditLog immediately
  → Emit corresponding Kafka event
```

---

## SECTION 23 — ANTIGRAVITY RUN COMMAND

To instruct Antigravity to execute this agent:

```
GENERATE: WORKSHOP_ATTENDANCE_TRACKING_AGENT

EXECUTION_ENGINE    = ANTIGRAVITY
AGENT               = WORKSHOP_ATTENDANCE_TRACKING_AGENT
VERSION             = v1.0.0
MODULE              = PROGRAM_AND_EVENT_MANAGEMENT
ROLE                = [specify: attendee | organizer | coach |
                       institution_admin | ops_admin]
FEATURE             = [specify: qr_checkin | otp_checkin | manual_mark |
                       bulk_mark | offline_sync | compliance | dispute |
                       export | govt_report | admin_override]
ENTITY_STATE        = [specify exact state from Section 6]
CHECK_IN_METHOD     = [specify method from Section 4: METHOD-1 through METHOD-7]
STAGE               = STAGE_1_FOUNDATION
MAX_SCREENS         = 3
MAX_ROLES           = 1
OUTPUT_TYPE         = [flutter_screen | api_contract | db_migration |
                       event_schema | state_machine | all]
```

Example run (organizer opens check-in + QR display):
```
GENERATE: WORKSHOP_ATTENDANCE_TRACKING_AGENT
ROLE                = organizer
FEATURE             = qr_checkin
ENTITY_STATE        = scheduled → check_in_open
CHECK_IN_METHOD     = METHOD-1
OUTPUT_TYPE         = flutter_screen
```

Example run (offline sync for coach):
```
GENERATE: WORKSHOP_ATTENDANCE_TRACKING_AGENT
ROLE                = coach
FEATURE             = offline_sync
ENTITY_STATE        = offline_captured → synced
CHECK_IN_METHOD     = METHOD-6
OUTPUT_TYPE         = flutter_screen
```

Exceeding MAX_SCREENS per run → STOP.
Mixed roles in single run → STOP.
Scope outside Section 2.1 → STOP.

---

## SECTION 24 — PRODUCTION READINESS GATE

Not production-ready until ALL gates pass.

```
GATE-1  ✗ All 10 PostgreSQL schemas generated and migrated
GATE-2  ✗ CouchDB document schema implemented for offline mode
GATE-3  ✗ All 19 API endpoints implemented and OpenAPI-validated
GATE-4  ✗ All 23 Kafka events defined, typed, and consumers mapped
GATE-5  ✗ All 7 check-in methods implemented and tested
GATE-6  ✗ QR token HMAC lifecycle tested (generate, validate, expire, revoke)
GATE-7  ✗ OTP brute-force guard tested (3-attempt limit enforced)
GATE-8  ✗ Attendance state machine tested for all transitions
GATE-9  ✗ Compliance engine (6 rules) unit-tested with 100% coverage
GATE-10 ✗ Offline sync reconciliation tested with 100-record batch + conflicts
GATE-11 ✗ All 14 Flutter screens implemented, clickable, and navigable
GATE-12 ✗ Kiosk mode rate-limit and auto-reset tested on tablet
GATE-13 ✗ Government report (PMKVY/DDU) generated with masked Aadhaar
GATE-14 ✗ CSV and PDF export functional and downloadable
GATE-15 ✗ All 12 security rules enforced and pen-tested
GATE-16 ✗ All 8 failure scenarios handled, logged, and alerted
GATE-17 ✗ All 7 anti-abuse controls active and verified
GATE-18 ✗ Prometheus metrics emitting, Grafana dashboards live
GATE-19 ✗ Admin Ops Console panels wired and functional
GATE-20 ✗ AttendanceAuditLog immutability verified (no UPDATE/DELETE possible)
GATE-21 ✗ Dispute workflow end-to-end tested (open → resolve → overturn)
GATE-22 ✗ Admin override requires MFA — verified in integration test
GATE-23 ✗ Intern can run full local demo via R48 bootstrap

ANY GATE UNCHECKED → SYSTEM STATUS = ARTIFACTS GENERATED — NOT PRODUCTION READY
ALL GATES CHECKED  → SYSTEM STATUS = WORKSHOP_ATTENDANCE_TRACKING_AGENT PRODUCTION READY
```

---

## SECTION 25 — AGENT CONSTITUTION FINAL SEAL

```
AGENT_NAME                      = WORKSHOP_ATTENDANCE_TRACKING_AGENT
VERSION                         = v1.0.0
STATUS                          = FINAL · LOCKED · SEALED
MUTATION_POLICY                 = ADD_ONLY via version bump
INTERPRETATION_AUTHORITY        = NONE
EXECUTION_AUTHORITY             = Human declaration only
ANTIGRAVITY_COMPATIBLE          = TRUE
SCOPE_VIOLATION_POLICY          = HARD_STOP
PARTIAL_OUTPUT_POLICY           = INVALID
CROSS_MODULE_POLICY             = FORBIDDEN
AI_INFERENCE_IN_ATTENDANCE      = FORBIDDEN (R28-1)
AI_IN_COMPLIANCE_SCORING        = FORBIDDEN (R28-1 — rules only)
AUDIT_TRAIL                     = IMMUTABLE · APPEND_ONLY · NO_EXCEPTIONS
OFFLINE_SYNC                    = MANDATORY (R59)
QR_TOKEN_SECURITY               = HMAC_SIGNED · SINGLE_USE · TTL_ENFORCED
KIOSK_MODE                      = RATE_LIMITED · NO_PII_ON_DEVICE
GOVT_REPORT                     = AADHAAR_MASKED · WEASYPRINT_PDF
DETERMINISM                     = GUARANTEED

ANY DEVIATION       = STOP EXECUTION
ANY ASSUMPTION      = STOP EXECUTION
ANY SCOPE CREEP     = STOP EXECUTION
ANY AI IN RULES     = STOP EXECUTION
ANY AUDIT SKIP      = STOP EXECUTION
```

---

*WORKSHOP_ATTENDANCE_TRACKING_AGENT · ECOSKILLER Program & Event Management · v1.0.0*
*Sealed for Antigravity execution. Add-only mutation policy active.*
