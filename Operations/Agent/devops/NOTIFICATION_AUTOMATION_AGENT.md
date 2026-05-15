# NOTIFICATION_AUTOMATION_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ANTIGRAVITY PLATFORM — ECOSKILLER UNIFIED SaaS

---

```
Artifact Class:     ENTERPRISE AGENT SPECIFICATION
Agent ID:           ANTIGRAVITY-NAA-001
Version:            v1.0.0
Status:             FINAL · LOCKED · SEALED · GOVERNED
Mutation Policy:    Add-only via version bump
Interpretation Authority: NONE
Execution Authority: Human declaration + CI/CD gate only
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0
Bound to Sections:  R2 · R4 · R10 · R12 · R21 · R38 · R39-D · R40 · R47 · R49 · R51
Platform Scope:     Ecoskiller Core + Dojo SaaS + Voice GD System + Antigravity Intelligence Layer
```

---

> **AGENT DECLARATION**
> The NOTIFICATION_AUTOMATION_AGENT (NAA) is a fully autonomous, rule-governed,
> multi-channel notification orchestration agent.
> It does not infer intent.
> It does not observe human behavior.
> It executes registered trigger policies, routes payloads through channel contracts,
> enforces delivery guarantees, and produces immutable audit trails.
> Intelligence is replaced by policy.
> Judgment is replaced by rules.
> Observation is replaced by instrumentation.

---

## SECTION I — AGENT IDENTITY & GOVERNANCE

### I.A — Agent Classification

| Property             | Value                                                               |
|----------------------|---------------------------------------------------------------------|
| Agent Name           | NOTIFICATION_AUTOMATION_AGENT                                       |
| Short Code           | NAA                                                                 |
| Agent Type           | Event-Driven Orchestration Agent                                    |
| Decision Model       | Deterministic Rule Engine (R28-1 compliant — zero ML permitted)     |
| Execution Paradigm   | Event-in → Policy-match → Channel-route → Delivery → Audit          |
| Failure Mode         | STOP → LOG → RETRY via registered backoff → ALERT on breach         |
| Human Override       | Admin-triggered via Internal Ops Console (R40) only                 |
| Storage Authority    | PostgreSQL (metadata) · Redis (state/locks) · MinIO (attachments)   |
| Event Bus Binding    | Apache Kafka / RabbitMQ (R4 AsyncAPI schema registry)               |

### I.B — Operational Boundaries

The NAA is **explicitly NOT authorized** to:
- Infer message urgency from content semantics
- Modify notification content based on ML scoring
- Auto-create new notification types at runtime
- Suppress notifications without a registered suppression rule
- Access raw voice recordings from the Voice GD System
- Store PII outside encrypted, tenant-isolated storage

The NAA **is authorized** to:
- Consume all registered platform events
- Route notifications across all declared channels
- Enforce per-user preference overrides
- Apply rate-limiting and deduplication locks
- Generate immutable delivery audit records
- Trigger escalation flows on delivery failure

---

## SECTION II — NOTIFICATION ARCHITECTURE (NON-NEGOTIABLE)

### II.A — Separation of Responsibilities

```
Layer                    Responsibility
─────────────────────────────────────────────────────────────
Event Bus (Kafka/RMQ)    Raw event ingestion only
Trigger Policy Engine    Event → Trigger rule matching
Template Renderer        Rule → Notification payload generation
Channel Router           Payload → Channel selection & dispatch
Delivery Agents (×5)     Channel-specific send execution
Preference Enforcer      User opt-in/opt-out gate enforcement
Retry Orchestrator       Exponential backoff + dead-letter handling
Audit Logger             Immutable record of every dispatch attempt
Preference Service       User-facing notification settings storage
Admin Override Console   Emergency suppress/force triggers (R40 bound)
```

**Critical Principle:**
The Event Bus decides nothing.
The Channel Router decides nothing.
The Trigger Policy Engine governs everything.

### II.B — Five Delivery Channel Stack (LOCKED)

```
Channel 1 → EMAIL       (Postfix / Docker Mail Server / Postal)
Channel 2 → SMS         (Jasmin SMS Gateway)
Channel 3 → PUSH        (FCM → Android · APNs → iOS · ntfy → Web/Desktop)
Channel 4 → IN-APP      (WebSocket command channel → Flutter / Next.js)
Channel 5 → WEBHOOK     (Outbound to external integrations — R65 bound)
```

No additional channels may be added without version bump.

---

## SECTION III — COMPLETE TRIGGER EVENT REGISTRY

> **ENFORCEMENT:** Every trigger below must have a corresponding entry in the
> Notification Policy Registry (R F — Contract-First Registries).
> Absence of any entry → STOP EXECUTION.

### III.A — Identity & Authentication Triggers

| Trigger ID    | Event Name                  | Channels         | Priority |
|---------------|-----------------------------|------------------|----------|
| NAA-ID-001    | user.registered             | EMAIL, PUSH      | HIGH     |
| NAA-ID-002    | user.email_verified         | EMAIL            | HIGH     |
| NAA-ID-003    | user.phone_verified         | SMS              | HIGH     |
| NAA-ID-004    | user.login_success          | PUSH (new device)| MEDIUM   |
| NAA-ID-005    | user.login_failed           | EMAIL, SMS       | HIGH     |
| NAA-ID-006    | user.password_reset_request | EMAIL            | CRITICAL |
| NAA-ID-007    | user.password_changed       | EMAIL, SMS       | HIGH     |
| NAA-ID-008    | user.mfa_otp_generated      | SMS, EMAIL       | CRITICAL |
| NAA-ID-009    | user.session_revoked        | EMAIL, PUSH      | HIGH     |
| NAA-ID-010    | user.account_locked         | EMAIL            | HIGH     |
| NAA-ID-011    | user.account_unlocked       | EMAIL            | HIGH     |
| NAA-ID-012    | user.domain_verified        | EMAIL, IN-APP    | MEDIUM   |
| NAA-ID-013    | user.profile_completed      | IN-APP           | LOW      |
| NAA-ID-014    | user.suspicious_login       | EMAIL, SMS, PUSH | CRITICAL |

### III.B — Job & Application Lifecycle Triggers

| Trigger ID    | Event Name                       | Channels              | Priority |
|---------------|----------------------------------|-----------------------|----------|
| NAA-JOB-001   | job.applied                      | EMAIL, IN-APP         | HIGH     |
| NAA-JOB-002   | job.application_received         | EMAIL, IN-APP (recr.) | HIGH     |
| NAA-JOB-003   | application.status_changed       | EMAIL, PUSH, IN-APP   | HIGH     |
| NAA-JOB-004   | application.shortlisted          | EMAIL, SMS, PUSH      | HIGH     |
| NAA-JOB-005   | application.rejected             | EMAIL, IN-APP         | MEDIUM   |
| NAA-JOB-006   | application.hired                | EMAIL, SMS, PUSH      | CRITICAL |
| NAA-JOB-007   | job.expiring_soon                | EMAIL, IN-APP (post.) | MEDIUM   |
| NAA-JOB-008   | job.posted                       | EMAIL (followers)     | LOW      |
| NAA-JOB-009   | job.matched_candidate_found      | EMAIL, IN-APP (recr.) | HIGH     |
| NAA-JOB-010   | job.deadline_reminder            | PUSH, IN-APP (cand.)  | MEDIUM   |

### III.C — Voice Group Discussion (GD) System Triggers

> Bound to: AUTOMATED VOICE GROUP DISCUSSION SYSTEM specification.
> All GD notifications are deterministic — no AI inference permitted.

| Trigger ID    | Event Name                        | Channels           | Priority |
|---------------|-----------------------------------|--------------------|----------|
| NAA-GD-001    | gd.batch_created                  | EMAIL, IN-APP      | HIGH     |
| NAA-GD-002    | gd.session_scheduled              | EMAIL, SMS, PUSH   | HIGH     |
| NAA-GD-003    | gd.reminder_24h                   | EMAIL, PUSH        | HIGH     |
| NAA-GD-004    | gd.reminder_1h                    | SMS, PUSH          | CRITICAL |
| NAA-GD-005    | gd.reminder_15min                 | SMS, PUSH          | CRITICAL |
| NAA-GD-006    | gd.join_window_open               | PUSH, IN-APP       | CRITICAL |
| NAA-GD-007    | gd.join_window_closed             | IN-APP             | HIGH     |
| NAA-GD-008    | gd.late_joiner_locked             | EMAIL, IN-APP      | HIGH     |
| NAA-GD-009    | gd.turn_token_granted             | IN-APP (WebSocket) | CRITICAL |
| NAA-GD-010    | gd.turn_token_expiring            | IN-APP (WebSocket) | CRITICAL |
| NAA-GD-011    | gd.turn_skipped_silence           | IN-APP             | MEDIUM   |
| NAA-GD-012    | gd.interrupt_attempt_logged       | IN-APP             | LOW      |
| NAA-GD-013    | gd.session_completed              | EMAIL, PUSH        | HIGH     |
| NAA-GD-014    | gd.score_published                | EMAIL, IN-APP      | HIGH     |
| NAA-GD-015    | gd.network_drop_detected          | IN-APP (WebSocket) | CRITICAL |
| NAA-GD-016    | gd.early_exit_logged              | IN-APP             | MEDIUM   |
| NAA-GD-017    | gd.open_round_started             | IN-APP (WebSocket) | HIGH     |
| NAA-GD-018    | gd.session_cancelled              | EMAIL, SMS, PUSH   | CRITICAL |

### III.D — Dojo SaaS Match & Arena Triggers

> Bound to: DOJO SaaS Production Artifact Spec v1.0.

| Trigger ID    | Event Name                     | Channels              | Priority |
|---------------|--------------------------------|-----------------------|----------|
| NAA-DOJO-001  | match.created                  | EMAIL, IN-APP         | HIGH     |
| NAA-DOJO-002  | match.opponent_joined          | PUSH, IN-APP          | HIGH     |
| NAA-DOJO-003  | match.starting_soon            | PUSH, IN-APP          | CRITICAL |
| NAA-DOJO-004  | match.started                  | IN-APP (WebSocket)    | CRITICAL |
| NAA-DOJO-005  | match.scenario_assigned        | IN-APP (WebSocket)    | CRITICAL |
| NAA-DOJO-006  | match.scored                   | EMAIL, PUSH, IN-APP   | HIGH     |
| NAA-DOJO-007  | match.mentor_entered           | IN-APP (WebSocket)    | HIGH     |
| NAA-DOJO-008  | match.mentor_feedback_given    | EMAIL, IN-APP         | HIGH     |
| NAA-DOJO-009  | belt.eligible                  | EMAIL, SMS, PUSH      | HIGH     |
| NAA-DOJO-010  | belt.promoted                  | EMAIL, SMS, PUSH      | CRITICAL |
| NAA-DOJO-011  | belt.certification_issued      | EMAIL, IN-APP         | HIGH     |
| NAA-DOJO-012  | tournament.created             | EMAIL, PUSH           | MEDIUM   |
| NAA-DOJO-013  | tournament.registration_open   | EMAIL, PUSH           | HIGH     |
| NAA-DOJO-014  | tournament.round_starting      | SMS, PUSH             | CRITICAL |
| NAA-DOJO-015  | tournament.winner_declared     | EMAIL, SMS, PUSH      | CRITICAL |
| NAA-DOJO-016  | leaderboard.rank_changed       | PUSH, IN-APP          | LOW      |
| NAA-DOJO-017  | trainer.battle_challenge_recv  | PUSH, IN-APP          | HIGH     |

### III.E — Education, Course & Student Triggers

| Trigger ID    | Event Name                       | Channels            | Priority |
|---------------|----------------------------------|---------------------|----------|
| NAA-EDU-001   | course.enrolled                  | EMAIL, IN-APP       | HIGH     |
| NAA-EDU-002   | course.module_unlocked           | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-003   | course.lesson_reminder           | PUSH                | LOW      |
| NAA-EDU-004   | course.assignment_due            | EMAIL, PUSH         | HIGH     |
| NAA-EDU-005   | course.assignment_graded         | EMAIL, IN-APP       | HIGH     |
| NAA-EDU-006   | course.quiz_available            | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-007   | course.completed                 | EMAIL, SMS, PUSH    | HIGH     |
| NAA-EDU-008   | course.certificate_issued        | EMAIL, PUSH         | HIGH     |
| NAA-EDU-009   | course.live_session_starting     | SMS, PUSH           | CRITICAL |
| NAA-EDU-010   | peer.quiz_challenge_received     | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-011   | studyroom.invite_received        | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-012   | streak.milestone_reached         | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-013   | streak.at_risk                   | PUSH                | HIGH     |
| NAA-EDU-014   | streak.broken                    | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-015   | skill.obsolete_detected          | EMAIL, PUSH         | HIGH     |
| NAA-EDU-016   | student.badge_awarded            | PUSH, IN-APP        | MEDIUM   |
| NAA-EDU-017   | student.leaderboard_rank_changed | IN-APP              | LOW      |

### III.F — Social, Community & Campus Triggers

| Trigger ID    | Event Name                      | Channels          | Priority |
|---------------|---------------------------------|-------------------|----------|
| NAA-SOC-001   | post.mentioned                  | PUSH, IN-APP      | MEDIUM   |
| NAA-SOC-002   | post.commented                  | PUSH, IN-APP      | LOW      |
| NAA-SOC-003   | post.reacted                    | IN-APP            | LOW      |
| NAA-SOC-004   | user.followed                   | IN-APP            | LOW      |
| NAA-SOC-005   | group.invite_received           | PUSH, IN-APP      | MEDIUM   |
| NAA-SOC-006   | group.member_joined             | IN-APP            | LOW      |
| NAA-SOC-007   | group.announcement_posted       | PUSH, IN-APP      | MEDIUM   |
| NAA-SOC-008   | institution.announcement        | EMAIL, PUSH       | HIGH     |
| NAA-SOC-009   | complaint.submitted             | EMAIL (confirm)   | HIGH     |
| NAA-SOC-010   | complaint.escalated             | EMAIL, SMS        | HIGH     |
| NAA-SOC-011   | complaint.resolved              | EMAIL, IN-APP     | HIGH     |
| NAA-SOC-012   | endorsement.received            | PUSH, IN-APP      | MEDIUM   |
| NAA-SOC-013   | peer.project_invite_received    | PUSH, IN-APP      | MEDIUM   |

### III.G — Billing, Commerce & Subscription Triggers

| Trigger ID    | Event Name                       | Channels            | Priority |
|---------------|----------------------------------|---------------------|----------|
| NAA-BILL-001  | subscription.created             | EMAIL               | HIGH     |
| NAA-BILL-002  | subscription.renewed             | EMAIL               | HIGH     |
| NAA-BILL-003  | subscription.expiring_soon       | EMAIL, PUSH         | HIGH     |
| NAA-BILL-004  | subscription.expired             | EMAIL, PUSH         | HIGH     |
| NAA-BILL-005  | subscription.cancelled           | EMAIL               | HIGH     |
| NAA-BILL-006  | invoice.generated                | EMAIL               | HIGH     |
| NAA-BILL-007  | invoice.payment_successful       | EMAIL               | HIGH     |
| NAA-BILL-008  | invoice.payment_failed           | EMAIL, SMS          | CRITICAL |
| NAA-BILL-009  | invoice.payment_retry_scheduled  | EMAIL               | HIGH     |
| NAA-BILL-010  | refund.initiated                 | EMAIL               | HIGH     |
| NAA-BILL-011  | refund.completed                 | EMAIL               | HIGH     |
| NAA-BILL-012  | trainer.payout_requested         | EMAIL (admin)       | HIGH     |
| NAA-BILL-013  | trainer.payout_processed         | EMAIL               | HIGH     |
| NAA-BILL-014  | trainer.royalty_credited         | EMAIL, IN-APP       | HIGH     |
| NAA-BILL-015  | plan.upgrade_recommended         | PUSH, IN-APP        | LOW      |

### III.H — Intelligence, Innovation & Royalty Triggers

| Trigger ID    | Event Name                          | Channels            | Priority |
|---------------|-------------------------------------|---------------------|----------|
| NAA-INN-001   | idea.submitted                      | EMAIL, IN-APP       | HIGH     |
| NAA-INN-002   | idea.similarity_flagged             | EMAIL, IN-APP       | HIGH     |
| NAA-INN-003   | idea.licensed                       | EMAIL, SMS          | CRITICAL |
| NAA-INN-004   | royalty.earned                      | EMAIL, IN-APP       | HIGH     |
| NAA-INN-005   | royalty.payout_issued               | EMAIL, SMS          | HIGH     |
| NAA-INN-006   | royalty.anomaly_detected            | EMAIL (admin)       | CRITICAL |
| NAA-INN-007   | career_stage.changed                | EMAIL, IN-APP       | HIGH     |
| NAA-INN-008   | intelligence_score.updated          | IN-APP              | LOW      |
| NAA-INN-009   | belt.eligible_intelligence_test     | EMAIL, PUSH         | HIGH     |

### III.I — Governance, Security & Admin Triggers

| Trigger ID    | Event Name                      | Channels          | Priority |
|---------------|---------------------------------|-------------------|----------|
| NAA-GOV-001   | moderation.content_removed      | EMAIL, IN-APP     | HIGH     |
| NAA-GOV-002   | moderation.appeal_received      | EMAIL (mod)       | HIGH     |
| NAA-GOV-003   | moderation.appeal_resolved      | EMAIL, IN-APP     | HIGH     |
| NAA-GOV-004   | user.strike_issued              | EMAIL, IN-APP     | HIGH     |
| NAA-GOV-005   | user.suspended                  | EMAIL             | CRITICAL |
| NAA-GOV-006   | user.banned                     | EMAIL             | CRITICAL |
| NAA-GOV-007   | fraud.detected                  | EMAIL, SMS (admin)| CRITICAL |
| NAA-GOV-008   | system.maintenance_scheduled    | EMAIL, PUSH, INAP | HIGH     |
| NAA-GOV-009   | system.maintenance_complete     | PUSH, IN-APP      | MEDIUM   |
| NAA-GOV-010   | audit.anomaly_detected          | EMAIL (admin)     | CRITICAL |
| NAA-GOV-011   | gdpr.data_export_ready          | EMAIL             | HIGH     |
| NAA-GOV-012   | gdpr.right_to_forget_confirmed  | EMAIL             | HIGH     |
| NAA-GOV-013   | platform.transparency_published | PUSH (opt-in)     | LOW      |

---

## SECTION IV — AGENT EXECUTION PIPELINE (DETERMINISTIC)

### IV.A — Full Pipeline State Machine

```
[1] EVENT INGESTED
        ↓
[2] TRIGGER POLICY LOOKUP
    ├── Match found       → [3]
    └── No match          → LOG (unregistered event) → DISCARD
        ↓
[3] PREFERENCE ENFORCEMENT GATE
    ├── User opted out    → LOG suppression → STOP pipeline for this channel
    ├── Channel disabled  → Skip channel → Continue to next channel
    └── Allowed           → [4]
        ↓
[4] DEDUPLICATION CHECK (Redis TTL lock)
    ├── Duplicate found   → LOG dedup hit → STOP
    └── Unique            → [5]
        ↓
[5] RATE LIMIT CHECK (per user + per event type)
    ├── Rate exceeded     → QUEUE for later delivery window
    └── Within limit      → [6]
        ↓
[6] TEMPLATE RENDERER
    ├── Template found    → Render payload with context injection
    └── Template missing  → LOG critical → ALERT admin → STOP
        ↓
[7] CHANNEL ROUTER
    ├── EMAIL             → Email Delivery Agent
    ├── SMS               → SMS Delivery Agent
    ├── PUSH              → Push Delivery Agent
    ├── IN-APP            → WebSocket Command Agent
    └── WEBHOOK           → Outbound Webhook Agent
        ↓
[8] DELIVERY AGENT EXECUTION
    ├── Success           → [9]
    └── Failure           → [Retry Orchestrator]
        ↓
[9] IMMUTABLE AUDIT LOG WRITE (PostgreSQL append-only)
        ↓
[10] EVENT EMITTED: notification.delivered / notification.failed
```

### IV.B — Retry Orchestrator Contract

```
Attempt 1:  Immediate
Attempt 2:  +30 seconds
Attempt 3:  +2 minutes
Attempt 4:  +10 minutes
Attempt 5:  +1 hour

After 5 failures:
  → Mark status: DEAD_LETTER
  → Write to dead_letter_queue table
  → Emit notification.dead_lettered event
  → Alert ops dashboard (R40 bound)

No infinite retry loops permitted.
```

### IV.C — Deduplication Lock Schema (Redis)

```
Key format:  naa:dedup:{user_id}:{trigger_id}:{entity_id}
TTL:         Configurable per trigger (default: 300 seconds)

Rule: If key exists → skip dispatch → log dedup suppression
```

---

## SECTION V — TEMPLATE SYSTEM (LOCKED)

### V.A — Template Registry Contract

Every template must declare:

```yaml
template_id:       string (e.g. "tmpl_gd_session_scheduled_email")
trigger_id:        string (bound to Section III registry)
channel:           EMAIL | SMS | PUSH | IN-APP | WEBHOOK
locale:            en-IN | en-US | hi-IN | ... (R20 localization)
subject_key:       i18n key (EMAIL only)
body_key:          i18n key
variables:         [list of required context variables]
fallback_locale:   en-IN
version:           semver string
```

### V.B — Template Variable Injection Contract

| Variable             | Source                              |
|----------------------|-------------------------------------|
| `{{user.full_name}}`  | User Service                       |
| `{{user.email}}`      | User Service (encrypted)           |
| `{{session.topic}}`   | GD Orchestrator                    |
| `{{session.time}}`    | GD Orchestrator (formatted local)  |
| `{{match.opponent}}`  | Dojo Match Engine                  |
| `{{job.title}}`       | Job Service                        |
| `{{company.name}}`    | Recruiter Service                  |
| `{{invoice.amount}}`  | Billing Service                    |
| `{{streak.count}}`    | Habit Engine                       |
| `{{belt.name}}`       | Belt Engine                        |
| `{{action_url}}`      | Dynamic deep-link generator (R47)  |
| `{{otp.code}}`        | Token Issuer (TTL-bound, masked)   |

**Security Rule:** OTP codes must never appear in PUSH or IN-APP channels.
Only EMAIL and SMS channels are authorized to carry OTP payloads.
Violation → STOP DELIVERY.

### V.C — Template Storage Architecture

```
Storage:       MinIO (versioned buckets)
Cache Layer:   Redis (rendered template cache, TTL: 1 hour)
Fallback:      Inline hardcoded safety templates for CRITICAL tier only
Version lock:  Immutable historical template archive (R60 bound)
```

### V.D — Mandatory Email Template Set

| Template ID                              | Trigger Bound    |
|------------------------------------------|------------------|
| tmpl_auth_welcome_email                  | NAA-ID-001       |
| tmpl_auth_verify_email                   | NAA-ID-002       |
| tmpl_auth_otp_email                      | NAA-ID-008       |
| tmpl_auth_password_reset_email           | NAA-ID-006       |
| tmpl_auth_suspicious_login_email         | NAA-ID-014       |
| tmpl_gd_scheduled_email                  | NAA-GD-002       |
| tmpl_gd_reminder_24h_email               | NAA-GD-003       |
| tmpl_gd_score_published_email            | NAA-GD-014       |
| tmpl_gd_cancelled_email                  | NAA-GD-018       |
| tmpl_dojo_belt_promoted_email            | NAA-DOJO-010     |
| tmpl_dojo_cert_issued_email              | NAA-DOJO-011     |
| tmpl_job_shortlisted_email               | NAA-JOB-004      |
| tmpl_job_hired_email                     | NAA-JOB-006      |
| tmpl_billing_invoice_email               | NAA-BILL-006     |
| tmpl_billing_payment_failed_email        | NAA-BILL-008     |
| tmpl_billing_payout_processed_email      | NAA-BILL-013     |
| tmpl_edu_course_completed_email          | NAA-EDU-007      |
| tmpl_edu_skill_obsolete_email            | NAA-EDU-015      |
| tmpl_gov_user_suspended_email            | NAA-GOV-005      |
| tmpl_gdpr_export_ready_email             | NAA-GOV-011      |
| tmpl_innovation_idea_licensed_email      | NAA-INN-003      |
| tmpl_royalty_payout_email                | NAA-INN-005      |

---

## SECTION VI — PREFERENCE ENGINE (USER CONTROL)

### VI.A — Notification Preference Data Model

```sql
notification_preferences (
  id                UUID PRIMARY KEY,
  user_id           UUID REFERENCES users(id),
  channel           TEXT CHECK (channel IN ('EMAIL','SMS','PUSH','IN_APP','WEBHOOK')),
  trigger_category  TEXT,  -- 'AUTH','JOB','GD','DOJO','EDU','SOC','BILL','GOV'
  enabled           BOOLEAN DEFAULT TRUE,
  quiet_hours_start TIME,
  quiet_hours_end   TIME,
  timezone          TEXT DEFAULT 'Asia/Kolkata',
  updated_at        TIMESTAMP,
  UNIQUE(user_id, channel, trigger_category)
)
```

### VI.B — Override Priority Ladder

```
Priority 1 (Highest): CRITICAL-tier notifications
  → Always delivered regardless of user preference.
  → Applies to: OTP, security alerts, payment failures,
                GD join window, account suspension.
  → User CANNOT suppress CRITICAL tier.

Priority 2: HIGH-tier notifications
  → Delivered unless explicitly disabled by user for that channel+category.

Priority 3: MEDIUM and LOW tiers
  → Fully user-controllable.
  → Respects quiet hours window.
  → Respects channel opt-out.
```

### VI.C — Quiet Hours Engine

```
Algorithm:
  IF trigger.priority IN (HIGH, MEDIUM, LOW)
    AND current_time BETWEEN user.quiet_hours_start AND user.quiet_hours_end
    THEN:
      → Queue notification for delivery at quiet_hours_end
      → Tag as: QUIET_HOUR_DEFERRED
      → Log deferral in audit table
  ELSE:
    → Proceed with immediate delivery
```

### VI.D — Preference UI Screens (Mandatory)

```
1. Notification Settings Screen
   → Per-category toggles by channel
   → Visual hierarchy: Email / SMS / Push / In-App

2. Quiet Hours Configuration Screen
   → Time pickers + timezone selector

3. Preference Reset Screen
   → Restore platform defaults

4. Channel Verification Screen
   → Email & phone re-verification triggers
```

---

## SECTION VII — DATABASE SCHEMA (LOCKED)

### VII.A — Core NAA Tables

```sql
-- Notification Dispatch Record (append-only)
notification_dispatch_log (
  id                  UUID PRIMARY KEY,
  user_id             UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  trigger_id          TEXT NOT NULL,       -- Bound to Section III registry
  channel             TEXT NOT NULL,
  template_id         TEXT NOT NULL,
  payload_hash        TEXT NOT NULL,       -- SHA-256 of rendered payload
  delivery_status     TEXT CHECK (status IN (
                        'QUEUED','SENT','DELIVERED','FAILED',
                        'DEAD_LETTER','SUPPRESSED','DEFERRED'
                      )),
  attempt_count       INT DEFAULT 0,
  last_attempt_at     TIMESTAMP,
  delivered_at        TIMESTAMP,
  failure_reason      TEXT,
  created_at          TIMESTAMP DEFAULT now(),
  -- IMMUTABLE: No UPDATE permitted on this table
  -- All state changes via INSERT with new status record
)

-- Dead Letter Queue
notification_dead_letter (
  id                  UUID PRIMARY KEY,
  dispatch_log_id     UUID REFERENCES notification_dispatch_log(id),
  user_id             UUID NOT NULL,
  trigger_id          TEXT NOT NULL,
  channel             TEXT NOT NULL,
  final_failure_at    TIMESTAMP,
  failure_reason      TEXT,
  admin_reviewed      BOOLEAN DEFAULT FALSE,
  admin_reviewed_by   UUID,
  admin_reviewed_at   TIMESTAMP
)

-- Rate Limit Tracking
notification_rate_ledger (
  user_id             UUID NOT NULL,
  channel             TEXT NOT NULL,
  trigger_category    TEXT NOT NULL,
  window_start        TIMESTAMP NOT NULL,
  dispatch_count      INT DEFAULT 0,
  PRIMARY KEY (user_id, channel, trigger_category, window_start)
)

-- Template Registry
notification_templates (
  template_id         TEXT PRIMARY KEY,
  trigger_id          TEXT NOT NULL,
  channel             TEXT NOT NULL,
  locale              TEXT NOT NULL,
  subject_template    TEXT,
  body_template       TEXT NOT NULL,
  variables_required  JSONB,
  version             TEXT NOT NULL,
  created_at          TIMESTAMP,
  deprecated_at       TIMESTAMP
)
```

### VII.B — Rate Limit Policy Table

```sql
notification_rate_policies (
  id                  UUID PRIMARY KEY,
  trigger_category    TEXT NOT NULL,
  channel             TEXT NOT NULL,
  window_seconds      INT NOT NULL,
  max_dispatches      INT NOT NULL,
  priority_exempt     BOOLEAN DEFAULT FALSE  -- CRITICAL tier bypasses
)
```

### VII.C — Default Rate Limit Policies

| Category   | Channel | Window    | Max Dispatches | Priority Exempt |
|------------|---------|-----------|----------------|-----------------|
| AUTH       | SMS     | 60s       | 3              | TRUE (OTP)      |
| GD         | PUSH    | 300s      | 5              | TRUE (join win) |
| SOCIAL     | PUSH    | 3600s     | 20             | FALSE           |
| SOCIAL     | EMAIL   | 86400s    | 5              | FALSE           |
| BILLING    | EMAIL   | 86400s    | 10             | FALSE           |
| EDU        | PUSH    | 86400s    | 8              | FALSE           |
| GOV        | EMAIL   | 86400s    | 20             | TRUE (suspend)  |

---

## SECTION VIII — CHANNEL DELIVERY AGENTS (LOCKED CONTRACTS)

### VIII.A — Email Delivery Agent

```
Implementation Stack:
  Primary:   Postfix + Docker Mail Server
  Backup:    External SMTP relay (configurable in Vault)

Required Headers:
  List-Unsubscribe: <https://api.<LOCK_DOMAIN>/notifications/unsubscribe?token={token}>
  X-Mailer: ANTIGRAVITY-NAA/1.0
  DKIM:     Signed (required)
  SPF:      Enforced (required)
  DMARC:    p=reject (production)

Attachments:
  Allowed: PDF invoices, PDF certificates, PDF GD score reports
  Storage: MinIO (signed URL, 7-day TTL)
  Max size: 5MB per notification

Bounce Handling:
  Hard bounce → mark email_verified = FALSE → notify user via SMS
  Soft bounce → retry × 3 → escalate
  Spam report → suppress email channel → notify admin
```

### VIII.B — SMS Delivery Agent

```
Implementation Stack:
  Primary:   Jasmin SMS Gateway (self-hosted)
  Character limits:
    SMS (Latin): 160 chars
    SMS (Unicode/Hindi): 70 chars
    Long SMS: segmented automatically

Permitted SMS types:
  Transactional: OTP, alerts, GD reminders, payment failures
  Promotional:   FORBIDDEN (platform is not a marketing engine)

Sender ID:   ANTIGRAVITY (registered DLT sender, India compliance)
DLT Headers: Mandatory for India deployments (TRAI compliance)
```

### VIII.C — Push Notification Delivery Agent

```
Implementation Stack:
  Android:   FCM (Firebase Cloud Messaging)
  iOS:       APNs (Apple Push Notification Service)
  Web/PWA:   ntfy (self-hosted) OR Web Push API
  Desktop:   Flutter Desktop notification bindings

Token Management:
  Push tokens stored in: user_push_tokens table
  Stale token detection: 30-day inactivity → invalidate
  Token refresh: on every app launch

Priority mapping:
  CRITICAL → FCM priority: high / APNs priority: 10
  HIGH     → FCM priority: high / APNs priority: 5
  MEDIUM   → FCM priority: normal
  LOW      → FCM priority: normal (batched)
```

### VIII.D — In-App (WebSocket) Delivery Agent

```
Transport:     WebSocket command channel (same channel as GD/Dojo control)
Protocol:      JSON-over-WebSocket
Room binding:  user_{user_id} personal channel

Message schema:
{
  "type": "notification",
  "notification_id": "{uuid}",
  "trigger_id": "{trigger_id}",
  "priority": "CRITICAL|HIGH|MEDIUM|LOW",
  "title": "{rendered_title}",
  "body": "{rendered_body}",
  "action_url": "{deep_link}",
  "timestamp": "{ISO8601}",
  "read": false
}

Persistence:
  Unread in-app notifications stored in PostgreSQL
  Delivered in-app: immediately marked, confirmed via ACK
  ACK timeout (30s) → re-deliver once → mark UNCONFIRMED
```

### VIII.E — Outbound Webhook Agent

```
Bound to:    R65 — Open Platform Extensibility Law
Authentication: HMAC-SHA256 signature in X-NAA-Signature header
Secret storage: HashiCorp Vault
Payload:     Standard NAA envelope (JSON)
Retry:       Standard retry orchestrator (Section IV.B)
TLS:         Required (reject HTTP webhooks)

Webhook envelope:
{
  "naa_version": "1.0",
  "trigger_id": "{trigger_id}",
  "event_timestamp": "{ISO8601}",
  "user_id": "{uuid}",
  "tenant_id": "{uuid}",
  "payload": {...},
  "signature": "{hmac_sha256}"
}
```

---

## SECTION IX — SECURITY & COMPLIANCE CONTROLS

### IX.A — PII Handling Rules

```
Rule 1: No PII stored in notification payloads in Redis cache.
         Redis stores only: user_id, trigger_id, delivery_status.

Rule 2: Email body with PII rendered at dispatch time only.
         Never cached with PII populated.

Rule 3: Phone numbers masked in all logs.
         Log format: +91-XXXXX-XX123 (last 3 digits only).

Rule 4: OTP codes must never appear in:
         - Logs
         - Redis cache
         - Audit trail payload
         - PUSH or IN-APP channels

Rule 5: All PII fields encrypted at rest (AES-256).
         Tenant-isolated encryption keys via HashiCorp Vault.
```

### IX.B — Audit Trail Integrity Rules

```
Audit Log Table: notification_dispatch_log
  → INSERT only. No UPDATE. No DELETE.
  → Immutable append-only enforced at DB level (PostgreSQL row-security policy).
  → Every audit record signed with: HMAC-SHA256(record_hash, platform_signing_key)
  → Signing key rotated monthly via Vault.
  → Records retained 7 years minimum (R60 — Long-Term Archival Law).

Tampering Detection:
  → Nightly batch job validates hash chain consistency.
  → Any gap or mismatch → emit: audit.integrity_violation → alert ops.
```

### IX.C — Notification Injection Prevention

```
Template variable sanitization:
  → All injected variables HTML-escaped before email rendering.
  → All SMS variables stripped of special characters.
  → Markdown rendering forbidden in SMS channel.
  → action_url values validated against registered domain map (R47).
  → External URLs in action_url → REJECTED.
```

---

## SECTION X — OBSERVABILITY & MONITORING (NON-OPTIONAL)

### X.A — Prometheus Metrics (Mandatory)

```
naa_dispatches_total{channel, trigger_category, status}
naa_dispatch_latency_seconds{channel}
naa_dead_letters_total{channel, trigger_category}
naa_retry_attempts_total{channel, trigger_category}
naa_dedup_suppressions_total{trigger_category}
naa_rate_limit_hits_total{channel, trigger_category}
naa_preference_suppressions_total{channel, trigger_category}
naa_quiet_hour_deferrals_total{channel}
naa_template_render_errors_total{template_id}
```

### X.B — Grafana Dashboard Requirements

```
Dashboard 1: NAA Health Overview
  → Total dispatches by channel (last 24h, 7d, 30d)
  → Success rate by channel (target: ≥ 99.5%)
  → Dead letter rate (alert if > 0.1%)
  → P95 dispatch latency

Dashboard 2: GD Notification Reliability
  → Critical GD triggers delivery confirmation rate
  → Join window open notification success rate (target: 100%)
  → Token grant notification latency (target: < 100ms)

Dashboard 3: Channel Health
  → Email bounce rate
  → SMS delivery rate by region
  → Push token staleness %
  → WebSocket message ACK rate

Dashboard 4: Dead Letter & Failure Analysis
  → Dead letters by channel + trigger
  → Admin review backlog count
```

### X.C — Alerting Rules (PagerDuty / Grafana Alert Manager)

```
Alert 1: CRITICAL delivery failure rate > 0.5%   → P0 (immediate)
Alert 2: GD join window notification failure       → P0 (immediate)
Alert 3: OTP delivery failure                      → P0 (immediate)
Alert 4: Payment failure notification undelivered  → P1 (15 min)
Alert 5: Dead letter queue size > 50               → P1 (15 min)
Alert 6: Template render error rate > 0.1%         → P2 (1 hour)
Alert 7: Audit log integrity check failed           → P0 (immediate)
Alert 8: Webhook HMAC validation failures > 10/min → P1 (15 min)
```

---

## SECTION XI — API CONTRACT REGISTRY (LOCKED)

### XI.A — Internal Service Endpoints

```
Base URL (internal Kubernetes): http://notification-service:8010

POST   /internal/dispatch          → Trigger immediate dispatch
POST   /internal/dispatch/batch    → Batch dispatch (max 500/request)
GET    /internal/status/{log_id}   → Delivery status query
POST   /internal/suppress          → Emergency suppress (admin only)
GET    /internal/dead-letters      → Dead letter queue viewer
POST   /internal/retry/{log_id}    → Manual retry trigger (admin)
```

### XI.B — User-Facing Preference Endpoints

```
Base URL: https://api.<LOCK_DOMAIN>/v1/notifications

GET    /preferences                → Get user preference set
PUT    /preferences                → Update preferences
POST   /unsubscribe                → Unsubscribe via token (email link)
GET    /history                    → User notification history (paginated)
POST   /mark-read/{id}             → Mark in-app notification read
POST   /mark-all-read              → Bulk mark read
```

### XI.C — Admin & Ops Endpoints

```
Base URL: https://ops.<LOCK_DOMAIN>/api/notifications

GET    /dispatch-log               → Full dispatch log explorer (filtered)
GET    /dead-letters               → Dead letter review queue
POST   /dead-letters/{id}/retry    → Force retry
POST   /dead-letters/{id}/dismiss  → Admin dismiss
GET    /templates                  → Template registry viewer
POST   /templates/{id}/preview     → Render preview with test data
POST   /emergency/suppress         → Global emergency suppress switch
```

### XI.D — OpenAPI 3.1 Fragment

```yaml
openapi: 3.1.0
info:
  title: ANTIGRAVITY Notification Automation Agent API
  version: 1.0.0

paths:
  /v1/notifications/preferences:
    get:
      summary: Get notification preferences
      security: [bearerAuth: []]
      responses:
        "200":
          description: Preference set returned
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/NotificationPreferenceSet"

  /v1/notifications/history:
    get:
      summary: Get notification history
      security: [bearerAuth: []]
      parameters:
        - in: query
          name: page
          schema: {type: integer}
        - in: query
          name: channel
          schema: {type: string}
        - in: query
          name: status
          schema: {type: string}
      responses:
        "200":
          description: Paginated notification history

components:
  schemas:
    NotificationPreferenceSet:
      type: object
      properties:
        preferences:
          type: array
          items:
            type: object
            properties:
              channel: {type: string}
              trigger_category: {type: string}
              enabled: {type: boolean}
              quiet_hours_start: {type: string}
              quiet_hours_end: {type: string}
              timezone: {type: string}
```

---

## SECTION XII — ASYNCAPI EVENT SCHEMA (NAA EMITTED EVENTS)

```yaml
asyncapi: 2.6.0
info:
  title: NAA Emitted Events
  version: 1.0.0

channels:
  notification.dispatched:
    publish:
      message:
        payload:
          type: object
          properties:
            log_id:         {type: string}
            user_id:        {type: string}
            trigger_id:     {type: string}
            channel:        {type: string}
            status:         {type: string}
            dispatched_at:  {type: string, format: date-time}

  notification.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            log_id:         {type: string}
            user_id:        {type: string}
            trigger_id:     {type: string}
            channel:        {type: string}
            attempt_count:  {type: integer}
            failure_reason: {type: string}

  notification.dead_lettered:
    publish:
      message:
        payload:
          type: object
          properties:
            dead_letter_id: {type: string}
            user_id:        {type: string}
            trigger_id:     {type: string}
            channel:        {type: string}
            final_failure:  {type: string, format: date-time}

  notification.preference_updated:
    publish:
      message:
        payload:
          type: object
          properties:
            user_id:        {type: string}
            channel:        {type: string}
            category:       {type: string}
            enabled:        {type: boolean}
            updated_at:     {type: string, format: date-time}
```

---

## SECTION XIII — INTERN EXECUTION MAPPING (R26 + R46 COMPLIANT)

### XIII.A — Notification Service Setup (Python/FastAPI)

```
Project location: /backend/services/notification_service/

Step 1: cd /backend/services/notification_service/
Step 2: python -m venv venv
Step 3: source venv/bin/activate
Step 4: pip install -r requirements.txt
Step 5: Copy .env.example → .env
         Fill: DATABASE_URL, REDIS_URL, KAFKA_BROKERS,
               VAULT_ADDR, SMTP_HOST, SMS_GATEWAY_URL, FCM_KEY
Step 6: alembic upgrade head
Step 7: uvicorn main:app --reload --port 8010

Expected output: "NAA Notification Service running on http://0.0.0.0:8010"

Verify: curl http://localhost:8010/health
Expected: {"status": "healthy", "channels": ["EMAIL","SMS","PUSH","IN_APP","WEBHOOK"]}
```

### XIII.B — Template Registry Seeding

```
Step 1: python tools/seed_templates.py
Expected: "Seeded 22 email templates, 8 SMS templates, 12 push templates"

Step 2: Verify:
curl http://localhost:8010/internal/templates | jq '.total'
Expected: ≥ 42
```

### XIII.C — Test a Notification Trigger

```
Step 1: POST http://localhost:8010/internal/dispatch
Body:
{
  "trigger_id": "NAA-ID-001",
  "user_id": "test-user-uuid",
  "context": {
    "user.full_name": "Test Intern",
    "user.email": "intern@test.com",
    "action_url": "https://app.dev.ecoskiller.local/verify"
  }
}

Expected:
{
  "log_id": "{uuid}",
  "status": "QUEUED",
  "channels": ["EMAIL", "PUSH"]
}
```

---

## SECTION XIV — ENFORCEMENT GATES

### XIV.A — Production Readiness Checklist

```
Gate                                              Required
─────────────────────────────────────────────────────────
All Section III triggers registered               ✔ REQUIRED
All trigger templates seeded in MinIO             ✔ REQUIRED
Retry orchestrator connected to Redis             ✔ REQUIRED
Dead letter queue table created with RLS          ✔ REQUIRED
Audit log append-only policy enforced at DB       ✔ REQUIRED
All 5 channel delivery agents health-checked      ✔ REQUIRED
Preference service API returning 200              ✔ REQUIRED
Prometheus metrics endpoint reachable             ✔ REQUIRED
Grafana dashboards imported                       ✔ REQUIRED
Alert rules activated                             ✔ REQUIRED
OTP channel restriction rule unit tested          ✔ REQUIRED
HMAC webhook signing tested                       ✔ REQUIRED
Hash chain integrity job scheduled                ✔ REQUIRED
Quiet hours engine unit tested                    ✔ REQUIRED
Deduplication lock tested under concurrent load   ✔ REQUIRED
```

### XIV.B — CI/CD Pipeline Gate (R49 + R50 Binding)

```
Stage 1: Contract validator confirms all trigger IDs registered
Stage 2: Template registry coverage ≥ 100%
Stage 3: Unit tests pass (retry, dedup, rate limit, quiet hours)
Stage 4: Integration tests pass (all 5 channels to test harness)
Stage 5: Dead letter flow E2E tested
Stage 6: Audit log append-only policy confirmed

Any stage fail → PIPELINE STOPS → NO DEPLOYMENT
```

### XIV.C — Final Enforcement Rule

```
If ANY of the following is absent at deployment time:

  - Complete trigger registry (Section III)
  - All 5 channel delivery agents operational
  - Retry orchestrator active
  - Immutable audit log enforced
  - Preference enforcement gate active
  - Deduplication lock active
  - Rate limit policies seeded
  - CRITICAL tier bypass rule operational
  - Prometheus metrics live
  - Dead letter admin console visible in Ops (R40)

→ STOP EXECUTION
→ REPORT NAA_INCOMPLETE
→ NO DEPLOYMENT CLAIM PERMITTED
→ NO PRODUCTION-READY STATUS GRANTED
```

---

## SECTION XV — SYSTEM DEFENSE STATEMENT

```
"The NOTIFICATION_AUTOMATION_AGENT converts the entire Antigravity/Ecoskiller
 notification responsibility into a deterministic, auditable, rule-governed
 delivery protocol. No message is sent without a registered trigger.
 No trigger fires without a matched policy. No delivery occurs without
 an immutable audit record. No PII is exposed outside encrypted, tenant-isolated
 storage. No AI infers message urgency. No human moderates routine delivery.
 The system is a machine: input is an event, output is a guaranteed delivery
 attempt with a permanent, tamper-proof trace."
```

---

## SECTION XVI — VERSION CONTROL & MUTATION POLICY

```
Current Version:     1.0.0
Status:              FINAL · LOCKED · SEALED
Mutation Policy:     Add-only via version bump
Next version (patch) 1.0.1 — bug fixes in delivery agents only
Next version (minor) 1.1.0 — new channel or trigger registration
Next version (major) 2.0.0 — pipeline architecture change (requires human declaration)

No section of this document may be deleted.
No trigger ID may be removed from Section III.
No enforcement rule may be weakened.
All channel contracts are locked.
All security rules are locked.
```

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║        NOTIFICATION_AUTOMATION_AGENT — SPECIFICATION SEAL                ║
║                                                                          ║
║  Agent ID:    ANTIGRAVITY-NAA-001                                        ║
║  Version:     1.0.0                                                      ║
║  Status:      FINAL · LOCKED · SEALED · GOVERNED                        ║
║  Triggers:    97 registered                                              ║
║  Channels:    5 locked                                                   ║
║  Pipelines:   1 deterministic (10-stage)                                 ║
║  Retry:       5-attempt exponential backoff                              ║
║  Audit:       Immutable append-only, HMAC-signed, 7-year retention       ║
║  Security:    PII-masked · OTP-restricted · HMAC-webhook · AES-256 rest  ║
║  Compliance:  GDPR · TRAI DLT · WCAG (R20) · Multi-tenant RLS           ║
║  Observability: Prometheus + Grafana + Loki + OpenTelemetry              ║
║                                                                          ║
║  THIS SPECIFICATION IS COMPLETE.                                         ║
║  NO HUMAN INTERPRETATION IS PERMITTED.                                   ║
║  NO SECTION MAY BE REMOVED OR WEAKENED.                                  ║
║  ONLY ADD-ONLY MUTATIONS VIA VERSION BUMP ARE AUTHORIZED.                ║
╚══════════════════════════════════════════════════════════════════════════╝
```
