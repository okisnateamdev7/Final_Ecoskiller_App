# EVENT_CALENDAR_SYNC_AGENT
## Ecoskiller · Unified Platform · Program & Event Management Layer
### Status: FINAL · LOCKED · SEALED · ANTIGRAVITY-ENFORCED
### Artifact Class: Production Agent Prompt — Deterministic Execution
### Mutation Policy: Add-only via version bump · Interpretation Authority: NONE
### Execution Engine: ANTIGRAVITY
### Version: 1.0 · March 2026 · Internal Use Only

---

## ⚠️ ANTIGRAVITY OPERATOR SEAL

```
AGENT_NAME                  = EVENT_CALENDAR_SYNC_AGENT
EXECUTION_ENGINE            = ANTIGRAVITY
PLATFORM                    = ECOSKILLER · UNIFIED PLATFORM
DOMAIN                      = Program & Event Management — Calendar Sync Layer
SUB_DOMAINS                 = Dojo SaaS · Society SaaS · Core Ecoskiller
STACK_LOCK                  = Flutter (operational) + React/Next.js (SEO/public)
NAMESPACES_SERVED           = core · realtime · society · billing · ops
MUTATION_POLICY             = ADD-ONLY VIA VERSION BUMP
INTERPRETATION              = FORBIDDEN
ARCHITECTURE_AUTHORITY      = LOCKED — HUMAN DECLARATION ONLY
ANTIGRAVITY_CONFUSION       = IMPOSSIBLE
READY_FOR                   = ANTIGRAVITY_PRODUCTION
ANTIGRAVITY_SAFE            = TRUE
TIMEZONE_AUTHORITY          = UTC CANONICAL · USER_TZ DISPLAY ONLY
OFFLINE_REQUIRED            = FALSE (calendar sync is network-dependent)
EXTERNAL_CALENDAR_WRITE     = CONSENT-GATED · NEVER FORCED
```

> Antigravity MUST NOT reinterpret calendar sync architecture, event type registry, reminder scheduling logic, timezone conversion rules, iCal feed generation, or external provider integration contracts.
> Antigravity MUST generate UI strictly within the declared screen boundaries in Section 16.
> Antigravity MUST NOT add event types, reminder tiers, or external calendar providers beyond those declared in this document without a version bump.
> Antigravity MUST treat every STOP signal as a hard execution halt with zero partial output.
> Antigravity MUST enforce that all time storage is UTC. Display conversion is the ONLY permitted use of user timezone.
> All external calendar writes (Google, Outlook, Apple) are CONSENT-GATED. The agent NEVER pushes to an external calendar without explicit user opt-in captured and logged.

---

## SECTION 1 — AGENT IDENTITY & SCOPE

### 1.1 Agent Purpose

The Event Calendar Sync Agent is the single authoritative service governing how every scheduled event across the Ecoskiller unified platform is:

- Registered into a canonical internal event calendar store
- Surfaced to users as a unified personal event calendar (in-app)
- Synced to external calendar providers (Google Calendar, Microsoft Outlook, Apple Calendar) via iCal feeds and provider APIs — consent-gated, opt-in only
- Paired with a deterministic, role-aware, classification-aware reminder schedule
- Tracked for no-show, attendance, rescheduling, and cancellation outcomes
- Reported to analytics for event attendance and engagement metrics
- Governed by timezone normalization rules (UTC canonical, user-timezone display)

This agent does NOT own event creation. Every event type is created by its authoritative source service. This agent CONSUMES event lifecycle Kafka topics and MAINTAINS the calendar layer.

### 1.2 Cross-Domain Event Coverage

The agent serves all scheduled event types across the entire unified platform:

```
DOMAIN: ECOSKILLER CORE
  interview.scheduled          — campus placement interviews
  gd.scheduled                 — Voice Group Discussion sessions
  mentor.session.scheduled     — live mentor guidance sessions
  live.session.scheduled       — live classroom/webinar sessions
  job.deadline.set             — job application deadlines
  certification.exam.scheduled — certification assessment sessions

DOMAIN: DOJO SAAS
  dojo.match.scheduled         — live skill duel match assignments
  tournament.round.scheduled   — tournament round start times
  belt.calibration.scheduled   — mentor-driven belt calibration events
  dojo.drill.scheduled         — practice drill time windows

DOMAIN: SOCIETY SAAS
  exposition.scheduled         — physical expo event dates
  workshop.batch.scheduled     — offline skill workshop batch start/end
  tournament.society.scheduled — society-level tournament events
  audit.scheduled              — surprise compliance audit events (admin only)
  scheme.deadline.set          — PMKVY/DDU document submission deadlines

DOMAIN: PLATFORM-WIDE
  platform.event.scheduled     — time-limited events (bootcamps, hackathons, bonus windows)
  billing.renewal.upcoming     — subscription renewal reminders
  consent.expiry.upcoming      — consent renewal deadlines
```

### 1.3 What This Agent Does NOT Own

```
DOES NOT OWN:
  Event creation logic         — owned by source services (Interview Service, Match Engine, etc.)
  Event cancellation logic     — owned by source services; this agent consumes cancellation events
  Scoring                      — owned by Scoring Engine
  Billing                      — owned by Billing Service
  Notification delivery        — owned by Notification Service; this agent triggers, not delivers
  User authentication          — owned by Auth Service / Keycloak
  Match room creation          — owned by Dojo Match Engine
```

### 1.4 Agent Position in System Architecture

```
Ecoskiller Unified Platform
  └── Namespaces: core · realtime · society
        └── EVENT_CALENDAR_SYNC_AGENT
              ├── calendar-service (primary owned service)
              ├── ical-feed-service (owned — generates .ics feeds per user)
              ├── reminder-engine (owned — schedules + triggers reminders)
              ├── Kafka (event bus — consumes all event.scheduled topics)
              ├── Redis (reminder state machine + deduplication)
              ├── PostgreSQL (canonical calendar store)
              ├── Notification Service (consumer — reminder delivery)
              ├── Apache Airflow (scheduled reminder jobs)
              └── Analytics Service (publisher — attendance outcome events)
```

---

## SECTION 2 — SYSTEM IDENTITY LOCK

### 2.1 Naming Conventions (Immutable)

Primary entities cannot be renamed. Fields may extend — not mutate.

```
CalendarEvent       — canonical internal representation of any scheduled event
EventType           — classification of the event (from registry in Section 3)
EventSlot           — time window (start_datetime, end_datetime, timezone_canonical = UTC)
UserCalendar        — aggregated view of all CalendarEvents for a user
CalendarSyncLink    — a user's opt-in connection to an external calendar provider
ICalFeed            — a generated per-user .ics feed URL (signed, scoped)
ReminderSchedule    — the set of reminder triggers assigned to a CalendarEvent
ReminderDelivery    — a single reminder dispatch record (channel + time + status)
AttendanceOutcome   — post-event record (ATTENDED | NO_SHOW | CANCELLED | RESCHEDULED)
CalendarPreference  — per-user settings for reminder channels, advance timing, quiet hours
```

### 2.2 Roles Served (Keycloak RBAC — Immutable)

```
STUDENT / CANDIDATE    — views own upcoming interviews, GDs, matches, sessions
MENTOR                 — views mentoring sessions, belt calibrations, match assignments
RECRUITER              — views interview slots, GD batch schedules
COORDINATOR            — views exposition schedules, workshop batch timelines
COACH                  — views society workshop batches, tournament schedules
SOCIETY_ADMIN          — views all society events + audit schedules (admin-only events)
FRANCHISE_OWNER        — views society exposition and workshop revenue events
MASTER_ORGANIZER       — cross-society event calendar view
JUDGE                  — views assigned judging windows (exposition, tournament)
PLATFORM_SUPER_ADMIN   — full platform calendar view for governance
```

---

## SECTION 3 — CANONICAL EVENT TYPE REGISTRY (LOCKED)

All event types are declared here. No unlisted type may enter the calendar without a version bump.

### 3.1 Event Type Registry

| event_type_key                  | Source Service               | Participant Roles                              | Visibility      | External Sync |
|---------------------------------|------------------------------|------------------------------------------------|-----------------|---------------|
| INTERVIEW                       | Interview Service            | CANDIDATE, RECRUITER                           | PRIVATE         | ALLOWED       |
| GROUP_DISCUSSION                | Voice GD Orchestrator        | CANDIDATE                                      | PRIVATE         | ALLOWED       |
| DOJO_MATCH                      | Dojo Match Engine            | STUDENT, MENTOR (observer)                     | PRIVATE         | ALLOWED       |
| TOURNAMENT_ROUND                | Tournament Management Agent  | PARTICIPANT, JUDGE                             | PRIVATE         | ALLOWED       |
| BELT_CALIBRATION                | Belt Engine                  | STUDENT, MENTOR                                | PRIVATE         | ALLOWED       |
| DOJO_DRILL                      | Dojo Match Engine            | STUDENT                                        | PRIVATE         | ALLOWED       |
| MENTOR_SESSION                  | Interview/Session Service    | STUDENT, MENTOR                                | PRIVATE         | ALLOWED       |
| LIVE_CLASSROOM                  | Session Service              | STUDENT, INSTRUCTOR                            | SEMI-PUBLIC     | ALLOWED       |
| CERTIFICATION_EXAM              | Certification & Belt Engine  | STUDENT                                        | PRIVATE         | ALLOWED       |
| EXPOSITION                      | Exposition Management Agent  | PARTICIPANT, JUDGE, COORDINATOR                | SEMI-PUBLIC     | ALLOWED       |
| WORKSHOP_BATCH                  | Workshop Service             | STUDENT, COACH                                 | SEMI-PUBLIC     | ALLOWED       |
| SOCIETY_TOURNAMENT              | Tournament Service (Society) | PARTICIPANT, JUDGE                             | SEMI-PUBLIC     | ALLOWED       |
| AUDIT_SCHEDULED                 | Audit Service                | SOCIETY_ADMIN, COORDINATOR                     | ADMIN_ONLY      | FORBIDDEN     |
| SCHEME_DEADLINE                 | Scheme Service               | PARTICIPANT, COORDINATOR                       | PRIVATE         | ALLOWED       |
| PLATFORM_EVENT                  | Admin Governance Service     | ALL (role-filtered)                            | PUBLIC          | ALLOWED       |
| BILLING_RENEWAL                 | Billing & Subscription       | Subscriber role                                | PRIVATE         | FORBIDDEN     |
| JOB_DEADLINE                    | Job Service                  | CANDIDATE                                      | PRIVATE         | ALLOWED       |
| CONSENT_EXPIRY                  | Compliance Service           | GUARDIAN, COORDINATOR                          | PRIVATE         | FORBIDDEN     |

### 3.2 Event Visibility Rules

```
PRIVATE       — visible only to directly enrolled participants
SEMI-PUBLIC   — visible to participants + coordinators in same tenant/society
ADMIN_ONLY    — visible only to SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_SUPER_ADMIN
PUBLIC        — visible to all roles in tenant; discoverable in React SEO layer
```

### 3.3 External Sync Eligibility

```
ALLOWED    — user may opt-in to sync this event type to external calendar
FORBIDDEN  — this event type MUST NEVER be written to an external calendar
             (reason: security-sensitive — audit schedules, billing details, consent expiry)
```

External sync of FORBIDDEN event types is blocked at API level. UI must not offer the toggle. No override.

---

## SECTION 4 — CALENDAR EVENT DATA MODEL (IMMUTABLE CORE)

### 4.1 CalendarEvent Schema

```
CalendarEvent
  ├── calendar_event_id       — UUID, system generated
  ├── event_type              — FK to EventType registry (Section 3.1)
  ├── source_service_id       — identifier of the originating service
  ├── source_entity_id        — FK to the source entity (match_id, interview_id, etc.)
  ├── title                   — display name (derived from source; max 150 chars)
  ├── description             — optional; max 500 chars; no PII from other users
  ├── start_datetime_utc      — UTC canonical; immutable after CONFIRMED
  ├── end_datetime_utc        — UTC canonical; immutable after CONFIRMED
  ├── duration_minutes        — computed from start/end; stored for fast read
  ├── join_url                — nullable; signed, time-limited (issued by source service)
  ├── venue_details           — nullable; for physical events (text, no PII)
  ├── participant_ids[]       — list of User IDs enrolled in this event
  ├── status                  — SCHEDULED | CONFIRMED | RESCHEDULED | CANCELLED | COMPLETED
  ├── visibility              — PRIVATE | SEMI_PUBLIC | ADMIN_ONLY | PUBLIC
  ├── external_sync_eligible  — boolean (from registry; computed, not user-settable)
  ├── reminder_schedule_id    — FK to ReminderSchedule
  ├── tenant_id               — multi-tenant isolation
  ├── society_id              — nullable; FK for society-domain events
  ├── created_at              — UTC timestamp
  └── last_updated_at         — UTC timestamp
```

### 4.2 Immutability Rules

```
IMMUTABLE AFTER CONFIRMED:
  start_datetime_utc
  end_datetime_utc
  event_type
  source_entity_id
  participant_ids[] (additions allowed; removals require CANCELLED or RESCHEDULED event)

MUTABLE UNTIL COMPLETED:
  title (with AuditLog entry)
  description
  join_url (re-issued on room recreation)
  status
  venue_details

ALWAYS IMMUTABLE:
  calendar_event_id
  source_service_id
  created_at
  tenant_id
```

---

## SECTION 5 — EVENT INGESTION ENGINE (KAFKA CONSUMER)

### 5.1 Kafka Topics Consumed

The agent subscribes to the following topics. No others. New event types require a version bump to add subscription.

```
Topic: interview.events
  interview.scheduled        → CREATE CalendarEvent (type=INTERVIEW)
  interview.rescheduled      → UPDATE start/end + status=RESCHEDULED + notify
  interview.cancelled        → UPDATE status=CANCELLED + notify + cancel external sync
  interview.completed        → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: gd.events
  gd.scheduled               → CREATE CalendarEvent (type=GROUP_DISCUSSION)
  gd.cancelled               → UPDATE status=CANCELLED + notify
  gd.completed               → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: match.events
  match.scheduled            → CREATE CalendarEvent (type=DOJO_MATCH)
  match.cancelled            → UPDATE status=CANCELLED + notify
  match.completed            → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: tournament.events
  tournament.round.scheduled → CREATE CalendarEvent (type=TOURNAMENT_ROUND)
  tournament.round.cancelled → UPDATE status=CANCELLED + notify
  tournament.round.completed → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: belt.events
  belt.calibration.scheduled → CREATE CalendarEvent (type=BELT_CALIBRATION)
  belt.calibration.completed → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: session.events
  session.scheduled          → CREATE CalendarEvent (type=LIVE_CLASSROOM | MENTOR_SESSION)
  session.cancelled          → UPDATE status=CANCELLED + notify
  session.completed          → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: certification.events
  certification.exam.scheduled → CREATE CalendarEvent (type=CERTIFICATION_EXAM)
  certification.exam.cancelled → UPDATE status=CANCELLED + notify
  certification.exam.completed → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: exposition.events
  exposition.published        → CREATE CalendarEvent (type=EXPOSITION)
  exposition.cancelled        → UPDATE status=CANCELLED + notify + cancel external sync
  exposition.archived         → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: workshop.events (society)
  batch.started               → CREATE CalendarEvent (type=WORKSHOP_BATCH)
  batch.cancelled             → UPDATE status=CANCELLED + notify
  batch.completed             → UPDATE status=COMPLETED + record AttendanceOutcome

Topic: tournament.society.events
  tournament.society.scheduled → CREATE CalendarEvent (type=SOCIETY_TOURNAMENT)
  tournament.society.completed → UPDATE status=COMPLETED

Topic: audit.events (society)
  audit.scheduled             → CREATE CalendarEvent (type=AUDIT_SCHEDULED)
                                 visibility=ADMIN_ONLY, external_sync_eligible=FALSE

Topic: scheme.events
  scheme.deadline.set         → CREATE CalendarEvent (type=SCHEME_DEADLINE)

Topic: platform.events
  platform.event.scheduled    → CREATE CalendarEvent (type=PLATFORM_EVENT)
  platform.event.cancelled    → UPDATE status=CANCELLED + notify all enrolled

Topic: billing.events
  billing.renewal.upcoming    → CREATE CalendarEvent (type=BILLING_RENEWAL)
                                 external_sync_eligible=FALSE

Topic: compliance.events
  consent.expiry.upcoming     → CREATE CalendarEvent (type=CONSENT_EXPIRY)
                                 external_sync_eligible=FALSE
```

### 5.2 Ingestion Idempotency Rules

```
IDEMPOTENCY_KEY     = source_service_id + source_entity_id + event_type
DUPLICATE_ACTION    = UPDATE existing CalendarEvent; do not create duplicate
ORPHAN_DETECTION    = CalendarEvents with no matching source entity after 24h → flag for admin review
ORDERING_GUARANTEE  = Kafka partition key = source_entity_id (ensures ordered processing per event)
```

### 5.3 Event Ingestion Failure Handling

```
SCHEMA_VALIDATION_FAIL  → reject event; publish to dead-letter topic; alert ops
MISSING_REQUIRED_FIELD  → reject event; log to AuditLog; alert source service
DUPLICATE_CONFIRMED     → idempotent update; no error; log dedup count metric
DATABASE_WRITE_FAIL     → retry 3x with exponential backoff; alert on-call if all fail
```

---

## SECTION 6 — TIMEZONE ENGINE (NON-NEGOTIABLE)

### 6.1 Canonical Storage Rule

```
ALL datetime values stored in PostgreSQL as TIMESTAMP WITH TIME ZONE in UTC.
No exceptions.
No local timezone storage in the database.
Application-level timezone conversion is the ONLY permitted transformation.
Violation = DATA INTEGRITY ERROR → STOP WRITE → ALERT
```

### 6.2 Display Conversion Rule

```
CONVERSION_RULE:
  display_datetime = convert_to_timezone(stored_utc, user.preference.timezone)
  user.preference.timezone sourced from CalendarPreference.display_timezone
  Default display timezone = user.profile.country_timezone (from User Service)
  Fallback = UTC

DISPLAY FORMAT:
  Long format: "Wednesday, 5 March 2026 at 10:30 AM IST"
  Short format: "Wed 5 Mar · 10:30 AM IST"
  Countdown format: "Starts in 2 hours 15 minutes"
  All countdowns computed server-side (not client-side) to prevent clock skew
```

### 6.3 Multi-Timezone Conflict Detection

```
CONFLICT_CHECK: when a new CalendarEvent is ingested for a participant,
  check for overlap with existing events in participant's UserCalendar:
    overlap = (new.start_utc < existing.end_utc) AND (new.end_utc > existing.start_utc)
  On conflict detected:
    → Do NOT block event creation (source service owns the conflict)
    → Emit conflict.detected Kafka event to source service
    → Flag CalendarEvent with has_conflict = true
    → Display conflict warning in Flutter UI
    → Do NOT suppress reminder delivery for conflicted events
```

### 6.4 DST Handling

```
DST_RULE:
  All reminder dispatch times computed in UTC
  DST transitions do not affect UTC-stored values
  Display layer handles DST-aware conversion via tz database (IANA)
  No reminder time is adjusted on DST transition (UTC anchor prevents drift)
```

---

## SECTION 7 — REMINDER ENGINE (DETERMINISTIC)

### 7.1 Reminder Classification (Aligned with Notification Service PN1.4)

```
All reminders are classified before dispatch per Notification Service rules:

CRITICAL      — event cancellation with < 1 hour notice; system failure affecting event
OPERATIONAL   — standard pre-event reminders for scheduled events
ASSESSMENT    — reminders for certification exams, belt calibrations, scored events
GOVERNANCE    — audit schedule reminders, consent expiry reminders, compliance deadlines
INFORMATIONAL — optional reminder for PLATFORM_EVENT (user opt-in only)
```

### 7.2 Default Reminder Schedule Per Event Type (Locked)

The following reminder schedule is the SYSTEM DEFAULT. User preferences (Section 7.4) may reduce but not increase reminder frequency.

| event_type              | Reminder Triggers (before event start)          | Class        | Channel                |
|-------------------------|-------------------------------------------------|--------------|------------------------|
| INTERVIEW               | 24h, 2h, 15min                                  | OPERATIONAL  | Push + Email + SMS     |
| GROUP_DISCUSSION        | 24h, 1h, 15min                                  | OPERATIONAL  | Push + Email           |
| DOJO_MATCH              | 1h, 15min, 5min                                 | OPERATIONAL  | Push + In-App          |
| TOURNAMENT_ROUND        | 24h, 2h, 15min                                  | OPERATIONAL  | Push + Email           |
| BELT_CALIBRATION        | 48h, 24h, 2h, 15min                             | ASSESSMENT   | Push + Email + SMS     |
| DOJO_DRILL              | 30min                                           | OPERATIONAL  | Push + In-App          |
| MENTOR_SESSION          | 24h, 1h, 10min                                  | OPERATIONAL  | Push + Email           |
| LIVE_CLASSROOM          | 24h, 30min                                      | OPERATIONAL  | Push + In-App          |
| CERTIFICATION_EXAM      | 72h, 24h, 2h, 30min                             | ASSESSMENT   | Push + Email + SMS     |
| EXPOSITION              | 7d, 48h, 24h, 2h                                | OPERATIONAL  | Push + Email           |
| WORKSHOP_BATCH          | 7d, 24h, 2h                                     | OPERATIONAL  | Push + Email + SMS     |
| SOCIETY_TOURNAMENT      | 48h, 24h, 2h                                    | OPERATIONAL  | Push + Email           |
| AUDIT_SCHEDULED         | 48h, 24h (admin roles only)                     | GOVERNANCE   | Push + Email           |
| SCHEME_DEADLINE         | 7d, 3d, 24h                                     | GOVERNANCE   | Push + Email + SMS     |
| PLATFORM_EVENT          | 24h (opt-in only)                               | INFORMATIONAL| Push (opt-in)          |
| BILLING_RENEWAL         | 7d, 3d, 1d                                      | BILLING      | Push + Email           |
| JOB_DEADLINE            | 7d, 24h, 6h                                     | OPERATIONAL  | Push + In-App          |
| CONSENT_EXPIRY          | 14d, 7d, 3d, 1d                                 | GOVERNANCE   | Push + Email + SMS     |

### 7.3 Reminder State Machine (Redis-Backed)

```
For each CalendarEvent, a ReminderSchedule is created at ingestion time:

STATE: SCHEDULED
  → Airflow job fires at T-minus trigger time
  → Redis lock acquired (idempotency: event_id + trigger_offset)
  → Notification payload constructed
  → Notification Service called (async Kafka message)
  → STATE → DISPATCHED

STATE: DISPATCHED
  → Notification Service confirms receipt
  → ReminderDelivery record written (channel, dispatched_at, status)
  → STATE → DELIVERED (on success) or FAILED (on error)

STATE: FAILED
  → Retry once after 60 seconds
  → If second failure → alert ops; log to AuditLog; do not retry further
  → STATE → FAILED_FINAL

STATE: CANCELLED
  → Triggered when CalendarEvent.status = CANCELLED or COMPLETED
  → All future reminder triggers for this event purged from Airflow + Redis
  → Already-DISPATCHED reminders cannot be recalled

STATE: SUPPRESSED
  → Triggered when user quiethours active AND reminder class = OPERATIONAL or INFORMATIONAL
  → Reminder deferred to next available delivery window
  → CRITICAL, ASSESSMENT, GOVERNANCE, BILLING reminders NEVER suppressed
```

### 7.4 User Reminder Preferences (CalendarPreference)

```
CalendarPreference
  ├── user_id
  ├── display_timezone          — IANA timezone string (e.g., "Asia/Kolkata")
  ├── quiet_hours_enabled       — boolean
  ├── quiet_hours_start         — HH:MM in user's display_timezone
  ├── quiet_hours_end           — HH:MM in user's display_timezone
  ├── reminder_channels[]       — PUSH | EMAIL | SMS | IN_APP (user opt-in per channel)
  ├── reminder_advance_override — NONE | EARLIER (user may choose to get reminders earlier only)
                                  LATER or FEWER not settable for ASSESSMENT, GOVERNANCE, BILLING
  ├── external_sync_enabled     — boolean (master switch)
  ├── external_sync_providers[] — list of CalendarSyncLink IDs
  └── informational_opt_in      — boolean (opt-in for PLATFORM_EVENT reminders)

RULES:
  User cannot disable CRITICAL reminders
  User cannot disable ASSESSMENT, GOVERNANCE, BILLING reminders
  User cannot reduce reminder frequency below platform minimums for BELT_CALIBRATION,
  CERTIFICATION_EXAM, SCHEME_DEADLINE, CONSENT_EXPIRY
  Quiet hours never apply to CRITICAL, SAFETY, ASSESSMENT, GOVERNANCE, BILLING classes
```

---

## SECTION 8 — EXTERNAL CALENDAR SYNC ENGINE

### 8.1 Supported External Providers (Locked)

```
PROVIDER_KEY        PROVIDER_NAME             PROTOCOL          STATUS
GOOGLE_CALENDAR     Google Calendar           OAuth2 + API      SUPPORTED
MICROSOFT_OUTLOOK   Microsoft Outlook / 365   OAuth2 + API      SUPPORTED
APPLE_CALENDAR      Apple Calendar            iCal Feed (.ics)  SUPPORTED (feed only)
ICAL_GENERIC        Any iCal-compatible app   iCal Feed (.ics)  SUPPORTED (feed only)
```

No other external provider may be added without a version bump.

### 8.2 Consent Gate (Non-Negotiable)

```
CONSENT_REQUIREMENTS_BEFORE_ANY_EXTERNAL_SYNC:
  1. User explicitly enables external_sync_enabled in CalendarPreference
  2. User selects at least one provider
  3. For API-based providers (Google, Outlook):
     — OAuth2 consent screen shown (provider-hosted)
     — Scopes requested: calendar.events.write (minimum required only)
     — Consent timestamp stored in CalendarSyncLink
     — Consent purpose disclosed: "Ecoskiller will add scheduled events to your calendar"
  4. For feed-based providers (Apple, iCal Generic):
     — User copies their personal signed ICalFeed URL into their calendar app
     — No OAuth required; URL is the consent mechanism
     — URL is scoped per user; never shared

CONSENT_REVOCATION:
  User disconnects provider → CalendarSyncLink deactivated immediately
  All future sync writes stopped
  Previously synced events: left in external calendar (we cannot delete from external)
  User informed of this limitation at time of disconnect

CONSENT_LOG:
  Every consent grant, update, and revocation logged to AuditLog (immutable)
```

### 8.3 CalendarSyncLink Schema

```
CalendarSyncLink
  ├── sync_link_id
  ├── user_id
  ├── provider_key              — GOOGLE_CALENDAR | MICROSOFT_OUTLOOK | APPLE_CALENDAR | ICAL_GENERIC
  ├── oauth_access_token        — encrypted at rest (HashiCorp Vault)
  ├── oauth_refresh_token       — encrypted at rest (HashiCorp Vault)
  ├── token_expires_at          — UTC
  ├── ical_feed_url             — for feed-based providers; signed, per-user
  ├── consent_granted_at        — UTC
  ├── consent_scope             — declared scope string
  ├── last_sync_at              — UTC
  ├── last_sync_status          — SUCCESS | FAILED | PARTIAL
  ├── sync_failure_count        — integer; auto-disable after threshold
  └── status                    — ACTIVE | REVOKED | SUSPENDED | FAILED_DISABLED
```

### 8.4 iCal Feed Generation (ICalFeedService)

```
FEED_FORMAT         — RFC 5545 compliant .ics
FEED_URL_STRUCTURE  — /calendar/feed/{signed_user_token}.ics
SIGNING             — HMAC-SHA256 with user-specific key (rotates on password change)
TOKEN_SCOPE         — single user only; cannot be shared to reveal other users' events
CONTENT_RULES:
  DTSTART, DTEND   — UTC (Z suffix)
  SUMMARY          — event title
  DESCRIPTION      — event description (no PII of other participants)
  LOCATION         — venue_details for physical events; join_url for virtual
  UID              — calendar_event_id@ecoskiller
  STATUS           — CONFIRMED | CANCELLED | TENTATIVE (maps to CalendarEvent.status)
  SEQUENCE         — increments on update (enables calendar app to process updates)
  ALARM            — VALARM VCALENDAR component for reminders (15min default)

FORBIDDEN IN FEED:
  Other participants' names or emails
  Scoring data
  Internal IDs (source_entity_id must not appear verbatim)
  Billing amounts or invoice references
  Audit schedule details (AUDIT_SCHEDULED events must NEVER appear in feed)
  BILLING_RENEWAL events
  CONSENT_EXPIRY events
```

### 8.5 API-Based Sync (Google / Outlook)

```
SYNC_MODE           — WRITE-ONLY (we create events in their calendar; we never read)
SYNC_TRIGGER        — CalendarEvent created/updated/cancelled → push to external provider
RETRY_POLICY        — 3 retries with exponential backoff on provider API failure
TOKEN_REFRESH       — automatic via stored refresh_token before each API call
REVOCATION_DETECT   — if provider returns 401 (token revoked) → CalendarSyncLink.status = REVOKED
                       → notify user via in-app notification → prompt to reconnect

SYNC_OPERATION_LOG:
  Every API call to external provider logged:
    provider, user_id, calendar_event_id, operation (CREATE|UPDATE|CANCEL), status, latency
  Stored in ClickHouse for analytics and failure investigation
  Not visible to user (internal ops only)

EXTERNAL_EVENT_MAPPING:
  external_event_id (provider's ID) stored in CalendarSyncLink for update/cancel operations
  Without external_event_id → CREATE
  With external_event_id → UPDATE
  On CalendarEvent.status = CANCELLED → CANCEL (delete from external calendar)
```

---

## SECTION 9 — UNIFIED PERSONAL CALENDAR (IN-APP)

### 9.1 UserCalendar View

```
The UserCalendar is a computed, role-filtered, tenant-isolated view of all CalendarEvents
where user_id is in participant_ids[].

COMPUTATION:
  Query: SELECT * FROM calendar_events
         WHERE $user_id = ANY(participant_ids)
           AND tenant_id = $user_tenant_id
           AND status != 'CANCELLED'
         ORDER BY start_datetime_utc ASC

FILTERING:
  ADMIN_ONLY events → visible only to qualifying admin roles
  PRIVATE events → visible only to enrolled participants
  PUBLIC events → visible to all users in tenant

CACHE:
  Redis cache per user_id (TTL = 5 minutes)
  Cache invalidated on CalendarEvent.status change or new event ingestion for that user
```

### 9.2 Calendar View Modes

```
AGENDA_VIEW      — chronological list of upcoming events (next 30 days default)
DAY_VIEW         — events for a single selected day
WEEK_VIEW        — 7-day grid with event blocks
MONTH_VIEW       — full month overview (event count per day; tap for details)
FILTER_VIEW      — filter by event_type, domain, status
```

### 9.3 Conflict Indicator

```
When has_conflict = true on a CalendarEvent:
  → Display yellow warning indicator on event card
  → Show "Time conflict with [other event title]" message
  → Do NOT block the user; informational only
  → Source service owns conflict resolution
```

---

## SECTION 10 — ATTENDANCE OUTCOME TRACKING

### 10.1 Attendance Outcome Rules

```
AttendanceOutcome is created when CalendarEvent.status → COMPLETED or event_type's
source service emits a no-show or completion signal.

OUTCOME_VALUES:
  ATTENDED         — participant joined; confirmed by source service
  NO_SHOW          — participant enrolled but did not join within join window
  CANCELLED_EARLY  — participant cancelled before event start
  CANCELLED_LATE   — participant cancelled after cancellation deadline
  RESCHEDULED      — event rescheduled; new CalendarEvent created
  SYSTEM_CANCELLED — event cancelled by source service or admin

OUTCOME_SOURCE:
  Source service emits completion/no-show event → Kafka → this agent records outcome
  This agent DOES NOT determine outcomes; it records what source services report
```

### 10.2 Attendance Analytics Published

```
Event: calendar.attendance.recorded → Analytics Service (Kafka)
Payload:
  calendar_event_id
  event_type
  user_id
  outcome (AttendanceOutcome value)
  source_service_id
  tenant_id
  society_id (nullable)

Analytics use cases (ClickHouse):
  no-show rate by event_type
  no-show rate by user cohort
  reminder effectiveness (outcome by reminder channel)
  external sync adoption rate
  event type engagement trend
```

---

## SECTION 11 — SECURITY & ACCESS CONTROLS

### 11.1 Auth & RBAC (Mandatory)

```
ALL calendar API calls → JWT auth enforced via Keycloak
Role-based access enforced per visibility level:
  PRIVATE        → only enrolled participant_ids
  SEMI_PUBLIC    → enrolled + coordinators/coaches in same society/tenant
  ADMIN_ONLY     → SOCIETY_ADMIN, MASTER_ORGANIZER, PLATFORM_SUPER_ADMIN only
  PUBLIC         → all authenticated users in tenant

External sync endpoints → additionally require CalendarSyncLink.status = ACTIVE
iCal feed URL → signed token is the auth mechanism; no session required for feed read
Signed iCal tokens are invalidated on: password change, account suspension, explicit revocation
```

### 11.2 Data Security

```
OAuth tokens → encrypted at rest (HashiCorp Vault; never in PostgreSQL plaintext)
iCal feed URLs → HMAC-signed; per-user; scoped
Other participants' PII → NEVER exposed in calendar events, reminders, or iCal feeds
AUDIT_SCHEDULED, BILLING_RENEWAL, CONSENT_EXPIRY events → never in external feeds
Row-level security on tenant_id enforced at PostgreSQL
Cross-tenant calendar event access → FORBIDDEN
```

### 11.3 Rate Limiting

```
Calendar API read endpoints   → 60 requests/minute per user
iCal feed fetches             → 12/hour per feed URL (external calendar apps poll frequently)
External sync write operations → 30/minute per user (provider API call budget)
Reminder schedule API         → 20 requests/minute per user
```

### 11.4 Audit Immutability

```
EVERY CalendarEvent creation         → AuditLog entry
EVERY CalendarEvent status change    → AuditLog entry
EVERY external sync consent grant    → AuditLog entry (immutable)
EVERY external sync consent revoke   → AuditLog entry (immutable)
EVERY reminder dispatch              → ReminderDelivery record
EVERY sync failure                   → ClickHouse log + AuditLog if threshold crossed
```

---

## SECTION 12 — APACHE AIRFLOW INTEGRATION (REMINDER SCHEDULING)

### 12.1 Airflow DAG Architecture

```
DAG: reminder_scheduler_dag
  Schedule: @every_minute (triggered on CalendarEvent ingestion via Kafka → Airflow trigger)
  Tasks:
    T1: load_pending_reminders
        → Query PostgreSQL for ReminderSchedule records where
          next_trigger_utc <= NOW() + 5 minutes AND status = SCHEDULED
    T2: acquire_redis_lock(event_id + trigger_offset)
        → Idempotency: skip if already dispatched
    T3: build_notification_payload
        → Construct role-aware, domain-aware, class-aware payload
        → Apply CalendarPreference (quiet hours, channel prefs)
    T4: publish_to_notification_service(Kafka)
        → Topic: calendar.reminders.dispatch
    T5: update_reminder_state(DISPATCHED)
        → Release Redis lock
    T6: write_reminder_delivery_record

RETRY_POLICY:
  Task-level retry: 2 retries, 30-second delay
  DAG-level failure → alert ops

QUIET_HOURS_HANDLING:
  T3 checks quiet_hours_enabled and current user display_timezone time
  If within quiet hours AND class NOT in [CRITICAL, ASSESSMENT, GOVERNANCE, BILLING]:
    → Defer reminder to quiet_hours_end + 5 minutes
    → Update next_trigger_utc accordingly
    → Do not mark as DISPATCHED
```

### 12.2 Airflow DAG: External Sync Processor

```
DAG: external_calendar_sync_dag
  Schedule: @every_5_minutes
  Tasks:
    T1: load_pending_sync_operations
        → CalendarEvents with status changes since last sync per active CalendarSyncLink
    T2: refresh_oauth_tokens (if expires_at < NOW() + 10 minutes)
    T3: call_external_provider_api (Google/Outlook) per operation
    T4: update_CalendarSyncLink.last_sync_at and status
    T5: log_sync_operation to ClickHouse

FAILURE_HANDLING:
  Provider 401 → mark REVOKED; notify user
  Provider 429 → exponential backoff; log rate limit event
  Provider 5xx → retry 3x; mark PARTIAL; alert ops if 3 consecutive DAG failures
  sync_failure_count > 5 → CalendarSyncLink.status = FAILED_DISABLED; user notified
```

### 12.3 Airflow DAG: iCal Feed Regeneration

```
DAG: ical_feed_refresh_dag
  Schedule: @every_15_minutes
  Tasks:
    T1: identify_users_with_changed_events (since last feed generation)
    T2: regenerate_ical_feed per affected user
    T3: store_feed_in_Redis_cache (TTL = 20 minutes)
    T4: serve_from_Redis on next feed request

Feed is regenerated on demand when:
  CalendarEvent created, updated, or cancelled for that user
  CalendarSyncLink token rotated
  User CalendarPreference updated
```

---

## SECTION 13 — ASYNC EVENT CONTRACTS (KAFKA — PUBLISHED BY THIS AGENT)

```
Topic: calendar.events
  calendar.event.created        { calendar_event_id, event_type, participant_ids[], start_utc }
  calendar.event.updated        { calendar_event_id, changed_fields[], updated_at }
  calendar.event.cancelled      { calendar_event_id, reason, cancelled_at }
  calendar.conflict.detected    { calendar_event_id, conflicting_event_id, user_id }

Topic: calendar.reminders
  calendar.reminder.scheduled   { reminder_schedule_id, calendar_event_id, trigger_times[] }
  calendar.reminder.dispatched  { reminder_delivery_id, channel, dispatched_at }
  calendar.reminder.failed      { reminder_delivery_id, reason, retry_count }
  calendar.reminder.cancelled   { reminder_schedule_id, reason }

Topic: calendar.sync
  calendar.sync.succeeded       { sync_link_id, calendar_event_id, provider, external_event_id }
  calendar.sync.failed          { sync_link_id, calendar_event_id, provider, error_code }
  calendar.sync.revoked         { sync_link_id, user_id, provider }

Topic: calendar.attendance
  calendar.attendance.recorded  { calendar_event_id, user_id, outcome, recorded_at }
```

---

## SECTION 14 — OBSERVABILITY REQUIREMENTS

### 14.1 Metrics (Prometheus)

```
calendar_events_ingested_total          (by event_type, status)
calendar_events_conflict_detected_total (by event_type)
calendar_reminders_dispatched_total     (by event_type, class, channel)
calendar_reminders_failed_total         (by event_type, channel)
calendar_reminders_suppressed_total     (by reason: quiet_hours | user_mute)
calendar_external_sync_success_total    (by provider)
calendar_external_sync_failure_total    (by provider, error_type)
calendar_ical_feed_requests_total       (by provider_type)
calendar_ical_feed_generation_ms        (histogram)
calendar_attendance_outcomes_total      (by event_type, outcome)
calendar_kafka_lag_seconds              (gauge — ingestion consumer lag)
```

### 14.2 Dashboards (Grafana)

```
Calendar Sync Health Dashboard:
  → Events ingested per hour (by event_type)
  → Reminder dispatch success rate (by class, channel)
  → External sync success rate (by provider)
  → iCal feed request rate + latency
  → Kafka consumer lag (ingestion pipeline)
  → Conflict detection rate (by event_type)
  → No-show rate (by event_type, last 7 days)
  → External sync adoption rate (% of users with at least one provider linked)
```

### 14.3 Alerting

```
calendar.kafka_lag > 120 seconds              → alert ops; ingestion pipeline degraded
calendar.reminder_dispatch_failure_rate > 5%  → alert ops
calendar.external_sync_failure > 10 in 5min  → alert ops; check provider status
calendar.ical_feed_error_rate > 1%            → alert ops
calendar.audit_event_in_external_sync         → CRITICAL ALERT (security violation)
calendar.billing_event_in_ical_feed           → CRITICAL ALERT (security violation)
calendar.cross_tenant_event_access_attempt    → CRITICAL ALERT (security violation)
```

---

## SECTION 15 — FAILURE HANDLING (DETERMINISTIC)

| Failure Condition                                | Agent Action                                                                       |
|--------------------------------------------------|------------------------------------------------------------------------------------|
| Kafka ingestion failure                          | Dead-letter topic; alert ops; no calendar event created                            |
| PostgreSQL write failure on event create         | Retry 3x; alert ops; no partial record written                                     |
| Reminder not dispatched within 2 min of trigger  | Retry once; if fail → FAILED_FINAL; alert ops; AuditLog entry                     |
| External provider API 401                        | Mark CalendarSyncLink REVOKED; notify user; stop future sync                       |
| External provider API 429                        | Exponential backoff; log; do not alert unless 3 consecutive DAG failures           |
| iCal feed signing key rotation failure           | Suspend feed generation; alert ops; serve cached feed (TTL extended temporarily)   |
| Quiet hours computation error (bad TZ data)      | Default to UTC; dispatch reminder; log timezone error; alert ops                   |
| FORBIDDEN event type appearing in external feed  | Block at feed generation; CRITICAL alert ops; audit log                            |
| Kafka consumer lag > 5 minutes                   | Agent enters DEGRADED mode; reminder Airflow DAGs continue from PostgreSQL         |
| Redis reminder lock unavailable                  | Skip this trigger cycle; retry next Airflow run; AuditLog                          |

No partial outputs. No silent failures. Every failure produces an AuditLog entry + observability event.

---

## SECTION 16 — UI GENERATION RULES (ANTIGRAVITY)

### 16.1 Flutter App — Calendar UI Responsibilities

Antigravity MUST generate the following screens. No extras. No omissions.

```
UNIFIED_CALENDAR_SCREEN
  → AGENDA_VIEW (default): chronological list next 30 days
  → DAY_VIEW: events on selected date
  → WEEK_VIEW: 7-day grid with event time blocks
  → MONTH_VIEW: full month; dot indicators per day
  → Toggle between views via tab/segmented control
  → Event card shows: event type icon, title, time (user timezone), status badge
  → Conflict indicator (yellow badge) on conflicted events
  → Filter by event_type (multi-select chip row)
  → Filter by domain (DOJO | SOCIETY | CORE)
  → Empty state for no upcoming events (not an error)

EVENT_DETAIL_SCREEN
  → Title, type, date/time (user timezone), duration
  → Join URL (as button; visible only within 15 min of event + during event window)
  → Venue (for physical events)
  → Reminder summary (next scheduled reminder time)
  → Conflict warning (if has_conflict = true)
  → Status badge (SCHEDULED | CONFIRMED | RESCHEDULED | CANCELLED)
  → "Add to external calendar" button (visible only if external_sync_enabled = false AND event_type external_sync_eligible = true)
  → Cancel RSVP button (where source service permits)

CALENDAR_PREFERENCES_SCREEN
  → Display timezone selector (IANA timezone list; searchable)
  → Quiet hours toggle + start/end time pickers
  → Per-channel reminder preferences: PUSH | EMAIL | SMS | IN_APP toggles
    (CRITICAL, ASSESSMENT, GOVERNANCE, BILLING channels shown as locked-ON; no toggle)
  → Informational reminders opt-in toggle (PLATFORM_EVENT only)
  → Save button (debounced; single submit)

EXTERNAL_CALENDAR_SYNC_SCREEN
  → Master toggle: "Sync events to my calendar" (external_sync_enabled)
  → Provider cards: Google Calendar | Microsoft Outlook | Apple Calendar | iCal (generic)
  → Per-provider connect/disconnect button
  → For Google/Outlook: "Connect" → OAuth2 flow (provider-hosted)
  → For Apple/iCal: "Copy feed URL" → signed ICalFeed URL copied to clipboard
  → Consent disclosure text shown before any connect action (non-dismissable)
  → Connected providers show: last sync time, sync status badge
  → Disconnected providers show: disconnect confirmation with data notice
  → Note: "Events already added to your external calendar will remain there after disconnecting"

REMINDER_HISTORY_SCREEN
  → List of delivered reminders (last 30 days)
  → Per reminder: event title, reminder type, channel, delivered at (user timezone), status
  → Filter by event_type, channel, status
  → No retry button (reminder delivery is system-managed)

UPCOMING_EVENTS_WIDGET (dashboard embed, not standalone screen)
  → Next 3 events across all types
  → Compact card: event type icon, title, countdown timer
  → Tap → opens EVENT_DETAIL_SCREEN
  → Shown on main app dashboard (not on React layer)
```

### 16.2 React / Next.js SEO Layer — Calendar Pages

```
/events                        → public platform events index (SSG; PUBLIC events only)
/events/[slug]                 → public event detail (SSR; PUBLIC events only)
```

React layer MUST NOT host:
- Personal calendar view (requires auth)
- Reminder preference settings
- External calendar sync setup
- Any PRIVATE, SEMI_PUBLIC, or ADMIN_ONLY event details
- Any event join URL or access token

### 16.3 Antigravity UI Limits

```
✅ ALLOWED:
  Generate screens listed in 16.1 and 16.2
  Display events in user's chosen display timezone (conversion in Flutter, not stored)
  Show conflict indicators (informational only; no conflict resolution UI)
  Implement consent disclosure before external sync connection
  Show locked-ON reminder toggles for CRITICAL/ASSESSMENT/GOVERNANCE/BILLING channels
  Display join URL as time-gated button (active only ≤15 min before + during event)
  Show iCal feed URL as copy-to-clipboard (no QR code; no share sheet to other users)

🚫 FORBIDDEN:
  Implementing reminder dispatch logic client-side
  Implementing timezone conversion server-side in the UI layer
  Displaying other participants' names/emails in event details
  Displaying AUDIT_SCHEDULED, BILLING_RENEWAL, CONSENT_EXPIRY events in external sync UI
  Offering a "disable all reminders" toggle for ASSESSMENT, GOVERNANCE, or BILLING events
  Allowing user to set reminder advance timing LATER than platform minimum for locked event types
  Displaying external provider OAuth tokens or iCal feed URLs other than via copy-to-clipboard
  Adding any external calendar provider not listed in Section 8.1
  Building conflict resolution UI (conflict display is informational only)
  Storing any datetime in local timezone (display conversion only; storage is UTC)
```

---

## SECTION 17 — DATA RETENTION & BACKUP

```
CalendarEvent records          → retained for lifetime of source entity + 1 year
ReminderDelivery records       → 90-day retention
CalendarSyncLink records       → retained 1 year post-revocation (legal compliance)
ICalFeed signed tokens         → invalidated on revocation; log retained 1 year
AuditLog (consent, sync)       → immutable, permanent
ClickHouse sync operation logs → 1-year retention
AttendanceOutcome records      → retained with source event record lifecycle
CalendarPreference records     → retained for user account lifetime
```

Backup per Ecoskiller infrastructure standards:
- PostgreSQL daily backup (calendar schema)
- ClickHouse analytics backup
- Redis snapshot backup (reminder state)
- Velero cluster backup includes calendar namespace state

---

## SECTION 18 — MULTI-TENANT ISOLATION

```
TENANT_ISOLATION_RULE:
  All CalendarEvents scoped to tenant_id
  Row-level security on tenant_id at PostgreSQL
  Cross-tenant calendar event access FORBIDDEN
  iCal feed URL is user-scoped — tenant boundary enforced by user identity
  External sync CalendarSyncLinks are user-scoped; no tenant-wide sync
  PLATFORM_SUPER_ADMIN may view cross-tenant event metadata for governance only
    (not calendar content of other users)
  Notification payloads include tenant branding (Notification Service applies)
  No global calendar broadcast except platform outage (PLATFORM_SUPER_ADMIN only)
```

---

## SECTION 19 — APPEALS & DISPUTE (CALENDAR LAYER)

```
CALENDAR_DISPUTE_SCOPE:
  Reminder not received          → user reports via CALENDAR_PREFERENCES_SCREEN
                                   → logged to AuditLog; ops investigates ReminderDelivery
  Wrong event time displayed     → user reports; ops checks UTC storage vs display conversion
  External sync event incorrect  → user reports; ops checks external sync log in ClickHouse
  Event appeared without consent → CRITICAL escalation; Privacy Officer notified

CALENDAR_DISPUTE_AUTHORITY:
  No user-facing dispute workflow (calendar is informational infrastructure)
  Issues routed to ops via support ticket system
  Correction of CalendarEvent data → requires AuditLog entry with source service confirmation
  Calendar data cannot be corrected unilaterally by this agent
    — must be corrected at source service → re-ingested via Kafka → calendar updated
```

---

## SECTION 20 — ANTIGRAVITY RUN COMMAND

Paste this block into the Antigravity master prompt to activate this agent:

```
BEGIN LOCKED AGENT ARTIFACT — EVENT_CALENDAR_SYNC_AGENT v1.0

System Role             : Event Calendar Sync Agent · Ecoskiller Unified Platform
Execution Engine        : ANTIGRAVITY
Domain                  : Program & Event Management — Calendar Sync Layer
Sub-Domains Served      : Dojo SaaS · Society SaaS · Core Ecoskiller
Stack                   : Flutter (operational UI) + React/Next.js (SEO/public)
Services Owned          : calendar-service · ical-feed-service · reminder-engine
Services Consumed       : Kafka (all event.scheduled topics) · Notification Service ·
                          Apache Airflow · Redis · PostgreSQL · ClickHouse ·
                          HashiCorp Vault (OAuth token encryption)
Event Ingestion         : Kafka consumer — all declared event types in Section 3
                          No event created by this agent; all events consumed from source services
Timezone Authority      : UTC canonical storage ONLY · User-TZ display conversion ONLY
                          NO local timezone storage in database — EVER
Reminder Engine         : Airflow-backed · Redis state machine · Classification-aware ·
                          Quiet-hours-aware · CRITICAL/ASSESSMENT/GOVERNANCE/BILLING always delivered
External Sync           : CONSENT-GATED · WRITE-ONLY · OAuth2 (Google/Outlook) ·
                          iCal feed (Apple/Generic) · FORBIDDEN types never in external feed
Forbidden Event Types   : AUDIT_SCHEDULED · BILLING_RENEWAL · CONSENT_EXPIRY must NEVER
                          appear in external calendar feeds or sync operations
iCal Feed               : RFC 5545 compliant · HMAC-signed · Per-user scoped ·
                          No other participants' PII in feed
Conflict Detection      : Informational only — source service owns conflict resolution
Attendance Tracking     : Recorded from source service events · Published to Analytics Service
Security                : Keycloak RBAC · JWT · Row-level PostgreSQL isolation (tenant_id) ·
                          Vault-encrypted OAuth tokens · Signed iCal feed URLs
Audit                   : Immutable AuditLog on every consent action and state transition
Multi-Tenant            : Enforced on tenant_id at every layer
Observability           : Prometheus metrics · Grafana dashboard · Alerting declared
Failure Mode            : STOP → LOG → ALERT → NO PARTIAL OUTPUT
Interpretation          : FORBIDDEN
Architecture Change     : ADD-ONLY VIA VERSION BUMP
UI Boundaries           : Section 16 — Antigravity must not exceed declared screens
React Boundary          : Public events index + detail only · No auth actions · No personal calendar

ANTIGRAVITY_SAFE        = TRUE
ANTIGRAVITY_CONFUSION   = IMPOSSIBLE
READY_FOR               = ANTIGRAVITY_PRODUCTION

END LOCKED AGENT ARTIFACT — EVENT_CALENDAR_SYNC_AGENT v1.0
```

---

## SECTION 21 — COMPLIANCE SEAL

```
EVENT_CALENDAR_SYNC_AGENT COMPLIANCE SEAL

✔ UTC Canonical Storage Enforced — No Local TZ in Database
✔ User Timezone Display Conversion Only
✔ DST Handling via IANA tz Database
✔ Event Type Registry Locked (Section 3 — 18 event types)
✔ FORBIDDEN External Sync Types Blocked at API Level
✔ External Calendar Sync Consent-Gated (Opt-in Only)
✔ OAuth2 Token Encrypted at Rest (HashiCorp Vault)
✔ iCal Feed HMAC-Signed Per-User
✔ iCal Feed RFC 5545 Compliant
✔ No PII of Other Participants in Any External Output
✔ CRITICAL/ASSESSMENT/GOVERNANCE/BILLING Reminders Always Delivered
✔ Quiet Hours Never Apply to Safety-Class Reminders
✔ Reminder Classification Aligned with Notification Service PN1.4
✔ Airflow DAG Idempotency via Redis Locking
✔ Kafka Ingestion Idempotency (source_service_id + source_entity_id + event_type)
✔ Attendance Outcome Tracking Published to Analytics
✔ Conflict Detection Informational Only (no conflict resolution)
✔ Audit Log Immutable (consent + state transitions)
✔ Multi-Tenant Isolation (tenant_id row-level security)
✔ Cross-Tenant Access Forbidden
✔ Rate Limiting Declared Per Endpoint
✔ External Sync Revocation Propagates Immediately
✔ AUDIT_SCHEDULED Events Blocked from All External Output
✔ Data Retention Declared (calendar events, reminders, sync logs, consent)
✔ Antigravity UI Boundary Declared (5 Flutter screens + 1 widget)
✔ React Boundary: Public Events Only
✔ Flutter/React Split Locked
✔ Event-Driven Only (No Sync Cross-Domain Chaining)
✔ Zero Paid SaaS Dependencies (self-hosted Airflow, Redis, Kafka, Vault)
✔ GDPR / India DPDP Pattern Ready (consent log, data minimization)
✔ Observability Non-Optional

MUTATION POLICY   : ADD-ONLY VIA VERSION BUMP
INTERPRETATION    : FORBIDDEN
EXECUTION ENGINE  : ANTIGRAVITY
STATUS            : FINAL · LOCKED · SEALED
```

---

*Ecoskiller Platform · Unified Program & Event Management · Event Calendar Sync Agent · v1.0 · March 2026 · Internal Use Only · Confidential*
