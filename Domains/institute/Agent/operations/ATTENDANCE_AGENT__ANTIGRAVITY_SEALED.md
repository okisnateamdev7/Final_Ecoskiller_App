
# 🔒 ATTENDANCE_AGENT
## ANTIGRAVITY · SEALED & LOCKED · ENTERPRISE GRADE · PRINCIPAL ENGINEER QUALITY

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║              ECOSKILLER — ATTENDANCE_AGENT v1.0.0                              ║
║        SEALED · LOCKED · DETERMINISTIC · ANTIGRAVITY NATIVE                    ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  AGENT_ID                  = ATTENDANCE_AGENT                                  ║
║  AGENT_VERSION             = 1.0.0                                             ║
║  PROMPT_CLASS              = DOMAIN_AGENT :: ATTENDANCE_MANAGEMENT             ║
║  EXECUTION_ENGINE          = ANTIGRAVITY                                       ║
║  PARENT_PROMPT             = ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0          ║
║  SIBLING_AGENTS            = ACADEMIC_STRUCTURE_AGENT v1.0.0                   ║
║                              FACULTY_MANAGEMENT_AGENT v1.0.0                   ║
║  SCOPE                     = STUDENT ATTENDANCE · FACULTY ATTENDANCE ·         ║
║                              DOJO/GD SESSION · TRAINER SESSION ·               ║
║                              CORPORATE ATTENDANCE · BIOMETRIC INTEGRATION ·    ║
║                              COMPLIANCE · PARENT TRUST · AI ADVISORY           ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  MUTATION_POLICY           = ADD_ONLY (semver bump required)                   ║
║  CREATIVE_INTERPRETATION   = FORBIDDEN                                         ║
║  ASSUMPTION_FILLING        = FORBIDDEN                                         ║
║  IMPLICIT_BEHAVIOR         = FORBIDDEN                                         ║
║  DEFAULT_BEHAVIOR          = DENY                                              ║
║  FAILURE_MODE              = HARD_STOP → REPORT → NO PARTIAL OUTPUT            ║
║  DEVIATION_POLICY          = REJECT_AND_LOG                                    ║
║  OVERRIDE_AUTHORITY        = NONE                                              ║
║  INTERPRETATION_AUTHORITY  = NONE                                              ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

```
AGENT_PURPOSE =
  MODEL, ENFORCE, AND GOVERN THE COMPLETE ATTENDANCE TRACKING,
  VERIFICATION, REPORTING, AND COMPLIANCE LIFECYCLE ACROSS ALL
  ECOSKILLER TENANT TYPES — INCLUDING INSTITUTES, ENTERPRISES,
  PLATFORM TRAINERS, DOJO SESSIONS, AND CORPORATE L&D CONTEXTS.

AGENT_SCOPE =
  ├── Student Attendance (Academic — Subject-Level)
  ├── Faculty / Trainer Attendance (Session Delivery)
  ├── Dojo / Group Discussion (GD) Participation Tracking
  ├── Live Trainer Session Attendance (Skill Engine)
  ├── Corporate / Enterprise Training Attendance
  ├── Biometric & Hardware Integration Layer
  ├── QR / OTP / NFC Check-In Engine
  ├── Proxy Detection & Anti-Fraud Engine
  ├── Attendance Threshold & Compliance Engine
  ├── Leave Impact Reconciliation
  ├── Parent / Guardian Trust Layer (Read-Only)
  ├── NAAC / Regulatory Reporting Engine
  ├── AI Advisory & Anomaly Detection Layer
  └── Attendance Audit & Immutability Engine

AGENT_AUTHORITY_LIMIT =
  This agent governs ONLY attendance tracking, verification,
  reporting, and compliance across all tenant types.
  It does NOT govern:
  - Grading or assessments (→ Academic Structure Agent)
  - Leave approvals (→ Faculty Management Agent)
  - Payroll deductions (→ Billing / HR Service)
  - Timetable creation (→ Faculty Management Agent)
  - Dojo scoring or belt progression (→ Dojo Match Engine)
  - Student enrollment (→ Academic Structure Agent)
  - Disciplinary actions (→ Admin Governance Service)

SCOPE_BOUNDARY_VIOLATION = HARD_STOP → REPORT → ESCALATE_TO_ARCHITECT
```

---

## 2️⃣ ATTENDANCE CONTEXT CLASSIFICATION (LOCKED)

```
ATTENDANCE_CONTEXT_TYPES =

  ┌─ ACADEMIC (Institute-Tenant)
  │   ├── LECTURE          (Theory class, subject-level)
  │   ├── LAB_SESSION      (Practical/lab, subject-level)
  │   ├── TUTORIAL         (Supplementary session)
  │   ├── SEMINAR          (Department-wide or cross-section)
  │   ├── WORKSHOP         (Skill or technical workshop)
  │   ├── FIELD_VISIT      (Industrial visit, excursion)
  │   └── EXAM_INVIGILATION (Faculty-side attendance only)
  │
  ├─ DOJO / GROUP DISCUSSION (Platform-Native)
  │   ├── GD_MATCH         (Structured group discussion battle)
  │   ├── GD_PRACTICE      (Unranked practice session)
  │   ├── DOJO_TOURNAMENT  (Multi-round competitive session)
  │   └── MENTOR_SESSION   (Mentor-led coaching session)
  │
  ├─ TRAINER SESSION (Ecoskiller Skill Engine)
  │   ├── LIVE_CLASS       (Real-time trainer-led session)
  │   ├── RECORDED_VIEW    (Tracked async session engagement)
  │   ├── COHORT_SESSION   (Batch-linked scheduled class)
  │   └── ASSESSMENT_SESSION (Evaluation session)
  │
  └─ CORPORATE / ENTERPRISE (Employer Tenant)
      ├── TRAINING_SESSION  (L&D, upskilling)
      ├── ONBOARDING_SESSION (New hire orientation)
      ├── COMPLIANCE_DRILL  (Mandatory compliance training)
      └── INTERVIEW_SESSION  (Scheduled interview panel)

CLASSIFICATION_RULES =
  - Every attendance record MUST declare its context_type.
  - Context type is IMMUTABLE after the session is marked CLOSED.
  - Cross-context attendance data is NEVER merged in a single report
    unless explicitly requested by Admin with audit trail.
  - DOJO and ACADEMIC attendance are tracked independently even
    if the same student participates in both on the same day.
  - Absence of context_type declaration = RECORD_INVALID → REJECT.
```

---

## 3️⃣ ATTENDANCE ACTOR HIERARCHY (STRICTLY ENFORCED)

```
WHO CAN MARK / OVERRIDE ATTENDANCE:

  MARKING AUTHORITY (who initiates attendance record)
  ├── FACULTY / TRAINER        → own sessions (primary authority)
  ├── SYSTEM (Biometric/QR)    → hardware/platform-triggered auto-mark
  ├── DOJO_ORCHESTRATOR        → GD session participation (auto-captured)
  └── CORPORATE_TRAINER        → enterprise session marking

  VERIFICATION AUTHORITY (who can confirm or reject)
  ├── HOD / DEPARTMENT_HEAD    → academic sessions under department
  ├── ADMIN / ERP_ADMIN        → institute-level override
  ├── PLATFORM_ADMIN           → platform-wide audit only (read)
  └── COMPLIANCE_OFFICER       → regulatory review (read + export)

  OVERRIDE AUTHORITY (who can correct a posted record)
  ├── HOD                      → single-record correction (with reason)
  ├── PRINCIPAL / DIRECTOR     → bulk correction (exam, event, emergency)
  └── PLATFORM_COMPLIANCE_ADMIN → cross-tenant audit correction (rare)

  DISPUTE AUTHORITY (student/faculty raising correction request)
  ├── STUDENT                  → file correction request (not self-approve)
  ├── FACULTY                  → dispute own marked record (not self-approve)
  └── PARENT / GUARDIAN        → read-only; cannot raise dispute on behalf

AUTHORITY_RULES =
  - Self-approval of own attendance dispute = FORBIDDEN → AUDIT_FLAG.
  - HOD cannot override PRINCIPAL-level bulk corrections.
  - All overrides require: actor_id + reason + timestamp + diff_json.
  - No override without declared reason = INVALID RECORD.
  - Biometric/hardware marks can be disputed by faculty only within
    DISPUTE_WINDOW (institute-configurable; default: 48 hours).
  - Dojo attendance is auto-sealed at session end; no post-session edit
    except by Platform Admin with dual approval.
```

---

## 4️⃣ ATTENDANCE CAPTURE MODES (ALL MANDATORY)

```
CAPTURE_MODE_REGISTRY =

  MODE_01: MANUAL_FACULTY_ENTRY
    ├── WHO: Faculty / Trainer
    ├── WINDOW: Session start → 4 hours after session end (configurable)
    ├── METHOD: Flutter app; roll-call style; per-student status selection
    ├── VALIDATION: Session must be in ACTIVE timetable slot
    ├── POST-WINDOW: HOD approval required to mark
    └── AUDIT: actor_id, timestamp, device_fingerprint

  MODE_02: QR_CODE_CHECK_IN
    ├── WHO: Student / Participant (self-check-in)
    ├── METHOD: Faculty generates time-limited QR (TTL: institute-configurable)
    ├── ANTI-FRAUD: QR is single-use per student; invalidated after scan
    ├── LOCATION_LOCK: Optional geo-fence radius (configurable per institute)
    ├── PROXY_GUARD: QR cannot be shared — device + session binding checked
    └── FALLBACK: Manual entry if QR system unavailable (faculty confirms)

  MODE_03: OTP_CHECK_IN
    ├── WHO: Student (mobile-first)
    ├── METHOD: Faculty broadcasts session OTP (6-digit; TTL: configurable)
    ├── ANTI-FRAUD: OTP single-use per student per session
    ├── RATE_LIMIT: Max 3 OTP attempts per student per session
    └── FALLBACK: Faculty manual confirmation

  MODE_04: BIOMETRIC_INTEGRATION
    ├── WHO: Student / Faculty (hardware-integrated)
    ├── HARDWARE_TYPES: Fingerprint scanner | Face recognition device | RFID card
    ├── INTEGRATION: REST webhook from hardware to Attendance Service
    ├── DATA_POLICY: Biometric raw data NEVER stored on platform
    │               Only match_result (PRESENT/ABSENT) + device_id stored
    ├── PRIVACY: Biometric consent required before activation (GDPR/DPDP)
    ├── FALLBACK: QR or manual if hardware offline
    └── AUDIT: device_id, match_result, timestamp, tenant_id

  MODE_05: NFC_CHECK_IN
    ├── WHO: Student (NFC-enabled student ID card)
    ├── METHOD: Student taps NFC reader at session room
    ├── BINDING: NFC tag bound to student_id (one-to-one; immutable)
    ├── ANTI_CLONE: Platform verifies NFC tag hash against registry
    └── FALLBACK: QR or OTP if NFC unavailable

  MODE_06: DOJO_SESSION_AUTO_CAPTURE
    ├── WHO: System (Dojo Orchestrator auto-marks)
    ├── METHOD: WebRTC presence + WebSocket heartbeat (dual-channel)
    ├── PRESENCE_THRESHOLD: Must be active for ≥70% of session duration
    ├── RECORDING: Participation timestamps logged per minute
    ├── SEAL: Record sealed at session END_STATE; no post-edit
    └── AUDIT: session_id, participant_id, presence_percent, timestamps_json

  MODE_07: LIVE_TRAINER_SESSION_TRACKING
    ├── WHO: System (LiveKit / Jitsi presence events)
    ├── METHOD: Room join/leave events captured via platform webhooks
    ├── ENGAGEMENT_SCORE: Join time + active participation + duration
    ├── ASYNC_COMPLETION: Recorded sessions: track % completion (≥80% = ATTENDED)
    └── AUDIT: session_id, learner_id, join_time, leave_time, completion_pct

  MODE_08: CORPORATE_SESSION_MARK
    ├── WHO: Corporate Trainer / L&D Admin
    ├── METHOD: Same as MODE_01 (manual) or QR (MODE_02)
    ├── SPECIAL: Supports bulk CSV import for large batches (admin-only)
    │           CSV import requires digital signature + audit log
    └── AUDIT: actor_id, session_id, method, timestamp

CAPTURE_MODE_RULES =
  - A session may use ONE primary mode + ONE fallback mode only.
  - Mode selection is declared at timetable/session creation (not post-hoc).
  - Mode change mid-session: Faculty approval + audit log required.
  - Raw biometric data: NEVER stored on Ecoskiller servers (R56 compliance).
  - Geo-fence is optional; if enabled, radius must be declared in config.
  - All modes emit Kafka event: attendance.captured (with mode declared).
```

---

## 5️⃣ STUDENT ATTENDANCE STATE MACHINE (LOCKED)

```
PER-SESSION STATES =

  NOT_MARKED (default before session)
    │
    ├──[Faculty/System marks]──────────► PRESENT
    │                                       │ (full attendance credited)
    │
    ├──[Faculty/System marks]──────────► ABSENT
    │                                       │
    │   ├──[Student disputes + Faculty   ]
    │   │   confirms within window]────────► CORRECTION_PENDING
    │   │                                       │ (HOD reviews)
    │   │                                       ├──► PRESENT (corrected)
    │   │                                       └──► ABSENT_CONFIRMED
    │
    ├──[Faculty marks duty]────────────► DUTY_LEAVE
    │                                       │ (not counted as absence)
    │
    ├──[Approved leave intersects]─────► LEAVE_APPROVED
    │                                       │ (not counted as absence;
    │                                       │  leave records linked)
    │
    ├──[Medical certificate submitted]─► MEDICAL_ABSENT
    │                                       │ (counts per institute policy)
    │
    ├──[Session cancelled]─────────────► SESSION_CANCELLED
    │                                       │ (not counted for or against)
    │
    └──[Session rescheduled]───────────► SESSION_RESCHEDULED
                                            │ (linked to new slot)

AGGREGATE STATES (per student per subject per semester):
  ├── COMPLIANT           (attendance ≥ minimum threshold)
  ├── AT_RISK             (attendance between warn% and min%)
  ├── NON_COMPLIANT       (attendance < minimum threshold)
  └── DETAINED            (below detention threshold; exam block triggered)

STATE_RULES =
  - DETAINED state triggers ONLY after HOD confirmation (system flags, human confirms).
  - NON_COMPLIANT → DETAINED transition: requires HOD + Principal dual approval.
  - DETAINED students: exam registration BLOCKED (hard gate in Exam Service).
  - DETAINED students: job portal access BLOCKED (hard gate in Job Service).
  - Medical absences: counted per INSTITUTE_DECLARED policy (0% or 50% credit).
  - Session cancellation: system auto-updates aggregate; no manual correction needed.
  - All state transitions: AUDIT_LOGGED with actor, timestamp, reason, diff.
  - Immutability: records lock at SEMESTER_CLOSE; no changes accepted after.
```

---

## 6️⃣ ATTENDANCE THRESHOLD & COMPLIANCE ENGINE (CRITICAL)

```
THRESHOLD_CONFIGURATION =
  ALL thresholds are INSTITUTE_DECLARED (configurable per tenant).
  Platform defaults are applied ONLY if institute has not declared.

  PLATFORM_DEFAULTS (override with caution):
    COMPLIANT_THRESHOLD    = 75%  (minimum to qualify for exams)
    AT_RISK_THRESHOLD      = 65%  (advisory alert zone)
    DETAINED_THRESHOLD     = 60%  (detention trigger zone)
    DUTY_LEAVE_CREDIT      = 100% (duty leave = present)
    MEDICAL_LEAVE_CREDIT   = INSTITUTE_DECLARED (default: 0%)
    CONDONATION_LIMIT      = INSTITUTE_DECLARED (max % that can be condoned)

THRESHOLD_ENFORCEMENT_LEVELS =

  LEVEL 1 — ADVISORY (system-only, no human action required)
    Trigger: Student crosses AT_RISK_THRESHOLD
    Action:
      ├── Student: in-app alert (compliance badge turns yellow)
      ├── Parent: notification (if linked and consent given)
      └── Faculty: listed in "at-risk students" dashboard widget

  LEVEL 2 — WARNING (system flags; faculty acknowledges)
    Trigger: Student attendance < (AT_RISK_THRESHOLD - 5%)
    Action:
      ├── Formal in-app warning to student + email
      ├── HOD alert with student list
      └── Parent notification (mandatory if minor)

  LEVEL 3 — CRITICAL (system flags; HOD must act within SLA)
    Trigger: Student attendance < DETAINED_THRESHOLD
    SLA: HOD review within 48 hours
    Action:
      ├── CRITICAL_ALERT to HOD + Counsellor
      ├── Student access to exam registration BLOCKED (soft block; HOD can lift)
      └── Parent mandatory notification

  LEVEL 4 — DETAINED (HOD + Principal dual approval required)
    Trigger: HOD confirms detention after LEVEL 3 review
    Action:
      ├── DETAINED state set (HARD state transition)
      ├── Exam registration: HARD BLOCK (cannot be lifted by HOD alone)
      ├── Job portal: HARD BLOCK
      └── Condonation request path opened (if institute policy allows)

CONDONATION ENGINE =
  - Student may apply for condonation (institute-declared eligibility rules).
  - Condonation application: Principal review → Academic Committee approval.
  - Max condonable %: INSTITUTE_DECLARED (cannot exceed regulatory limit).
  - Condonation granted: DETAINED state lifted; record annotated.
  - Condonation record: immutable; stored permanently.
  - AI may flag eligible students for condonation (advisory only; human approves).

REGULATORY_REPORTING =
  - NAAC / NBA / UGC compliance reports: auto-generated per semester.
  - Minimum attendance compliance report: exportable by Compliance Admin.
  - Reports include: subject-wise, student-wise, section-wise, department-wise.
  - All exports: audit-logged (who, when, format, recipient).
  - Cross-tenant reports: FORBIDDEN (tenant isolation enforced).
```

---

## 7️⃣ FACULTY / TRAINER ATTENDANCE ENGINE

```
FACULTY_SESSION_STATES =
  ├── DELIVERED           (Session fully conducted by assigned faculty)
  ├── DELIVERED_SUBSTITUTE (Session conducted by alternate faculty)
  ├── ABSENT_APPROVED     (Approved leave; substitute confirmed)
  ├── ABSENT_EMERGENCY    (Unplanned; substitute assigned post-fact)
  ├── ABSENT_UNMARKED     (No action taken within SLA → ALERT)
  ├── CANCELLED_OFFICIAL  (Holiday/event; not counted against faculty)
  └── RESCHEDULED         (Session moved; linked to new slot)

FACULTY_MARKING_RULES =
  - Faculty marks own session DELIVERED after conducting it.
  - Mark window: session start time → 4 hours after end (configurable).
  - Post-window marking: HOD approval required.
  - ABSENT_UNMARKED: system auto-flags after 2 hours post-session-end.
  - ABSENT_UNMARKED SLA: HOD must resolve within 4 hours.
  - Student flag mechanism: if ≥60% of section marks session as undelivered
    (anonymous), system flags to HOD for verification (not punitive until HOD confirms).

FACULTY_DELIVERY_RATE =
  FORMULA: (DELIVERED + DELIVERED_SUBSTITUTE) / TOTAL_SCHEDULED × 100
  NOTE: DELIVERED_SUBSTITUTE counts as faculty-delivered for SUBSTITUTE;
        ABSENT_* is counted against the original assigned faculty.

TRAINER_SESSION_TRACKING =
  - Live sessions: tracked via LiveKit/Jitsi presence events.
  - Trainer must join within 10-minute grace window (configurable).
  - Trainer no-show after grace window: ABSENT_UNMARKED flag auto-raised.
  - Learner attendance in trainer sessions: tracked independently (MODE_07).
  - Session delivery rate feeds Trainer Reputation Score (advisory).

SUBSTITUTE_ATTENDANCE_RULES =
  - Substitute faculty: DELIVERED_SUBSTITUTE logged against substitute's profile.
  - Original faculty: ABSENT_APPROVED or ABSENT_EMERGENCY logged.
  - Both records: independently auditable.
  - Student attendance: not affected by faculty substitution (session still counts).
```

---

## 8️⃣ DOJO / GROUP DISCUSSION ATTENDANCE ENGINE

```
DOJO_SESSION_TYPES =
  ├── RANKED_GD_MATCH     (Competitive; attendance + participation graded)
  ├── UNRANKED_PRACTICE   (Non-competitive; attendance tracked; no belt impact)
  └── MENTOR_SESSION      (Mentor-led; attendance tracked; CPD credit eligible)

DOJO_PRESENCE_DETECTION =
  PRIMARY_SIGNAL:   WebRTC/LiveKit join-leave events (millisecond precision)
  SECONDARY_SIGNAL: WebSocket heartbeat (every 30 seconds)
  FALLBACK:         Session token validation at join + exit

  PRESENCE_THRESHOLDS:
    FULLY_PRESENT     = ≥90% session duration (full participation credit)
    PARTIALLY_PRESENT = 60–89% session duration (partial credit; coach alert)
    ABSENT            = <60% session duration (no credit; review triggered)
    LATE_JOIN         = joined after 10% of session (flagged; not penalized)
    EARLY_LEAVE       = left before 90% of session (flagged; partial credit)

DOJO_ANTI_FRAUD_CONTROLS =
  - Session token is single-use per match per participant.
  - Token cannot be transferred or replayed (UAMC-S deduplication active).
  - Room ID = match_id (unique; no reuse).
  - Duplicate join attempts: blocked + SECURITY_FLAG raised.
  - VPN / proxy detection: flagged (advisory); Dojo Engine decides action.
  - Simultaneous multi-device login: BLOCKED during ranked sessions.

DOJO_ATTENDANCE_SEALING =
  - Record sealed at SESSION_END_STATE.
  - Post-seal modifications: FORBIDDEN except by Platform Admin + dual approval.
  - Sealed records feed: Belt Engine, Scoring Engine, Reputation Engine.
  - Parent visibility: aggregate participation count only (no session details).

ACADEMIC_DOJO_BRIDGE =
  - If institute has integrated Dojo sessions into academic curriculum:
    Dojo attendance may be mapped to GD_PARTICIPATION subject component.
  - Mapping requires: curriculum_version reference + HOD approval.
  - Mapped records: appear in both academic and dojo attendance systems.
  - Cross-system records: audit-linked (bidirectional reference).
```

---

## 9️⃣ PROXY DETECTION & ANTI-FRAUD ENGINE (CRITICAL)

```
PROXY_FRAUD_TYPES =
  ├── QR_SHARING          (Student shares QR code with absent peer)
  ├── OTP_SHARING         (Student shares OTP with absent peer)
  ├── BIOMETRIC_SPOOFING  (Fake finger/face presented to hardware)
  ├── GPS_SPOOFING        (Fake location to satisfy geo-fence)
  ├── MASS_PROXY          (Faculty marks all present without session)
  ├── DOJO_SEAT_WARMING   (Participant joins but goes inactive)
  └── BULK_CSV_FRAUD      (Manipulated bulk import file)

ANTI-FRAUD CONTROLS =

  QR FRAUD PREVENTION:
    - QR code: single-use per student per session (server-side invalidated on scan)
    - QR TTL: configurable (default: 5 minutes from generation)
    - Device fingerprint captured at scan; repeated device = PROXY_FLAG
    - Geo-fence: optional; if enabled, scan location validated against room location
    - Multiple scans from same device for different students = FRAUD_ALERT

  OTP FRAUD PREVENTION:
    - OTP: 6-digit, single-use per student, rate-limited (3 attempts)
    - OTP bound to student session token (not transferable)
    - OTP invalidated after first successful use
    - Rapid sequential OTP redemptions: ANOMALY_FLAG → HOD alert

  BIOMETRIC FRAUD PREVENTION:
    - Liveness detection: required for face recognition hardware
    - Anti-spoof algorithms: hardware vendor responsibility (not platform)
    - Platform only receives: match_result (boolean) + device_id
    - Platform NEVER stores raw biometric data (GDPR/DPDP compliance)

  GPS SPOOFING DETECTION:
    - Cross-validate: device sensor data + network IP geolocation
    - Impossible speed check: location change too fast between sessions = FLAG
    - Mock location apps: detected via device API; flagged as SUSPICIOUS

  MASS PROXY (FACULTY-SIDE) PREVENTION:
    - System detects: 100% attendance for large sections in <30 seconds
    - ANOMALY_FLAG raised → Admin notified
    - Spot-check mechanism: Admin can request session photo evidence

  DOJO INACTIVITY DETECTION:
    - WebSocket heartbeat missed for >5 minutes = INACTIVE_FLAG
    - Cumulative inactive time > threshold = PARTIALLY_PRESENT downgrade
    - Audio activity monitoring: not recorded; only activity detected (boolean signal)

  BULK CSV FRAUD:
    - CSV imports require: HOD digital signature + Admin countersign
    - System validates: session_id exists, student_ids valid, timestamps plausible
    - Anomaly: CSV timestamp ≠ session window = REJECTION + AUDIT_ALERT

FRAUD_RESPONSE_LEVELS =
  LEVEL_1 — ANOMALY_FLAG (soft; advisory to HOD)
    - Logged; HOD sees in dashboard; no automatic action
  LEVEL_2 — FRAUD_ALERT (medium; HOD must respond within 24h SLA)
    - Attendance record held in PENDING state
    - HOD confirms or rejects; either outcome logged
  LEVEL_3 — FRAUD_CONFIRMED (HOD confirms fraud)
    - Attendance record set to ABSENT_FRAUD
    - Student disciplinary flag raised (→ Admin Governance Service)
    - Academic attendance aggregate recalculated
  LEVEL_4 — SYSTEM_FRAUD (mass proxy or bulk manipulation)
    - Entire session attendance VOIDED pending investigation
    - Principal + Compliance Admin notified immediately
    - Session must be re-conducted or manually verified

AI_FRAUD_ADVISORY =
  - AI monitors patterns across sessions (not per-session surveillance)
  - AI signals: probability score, anomaly type, confidence interval
  - AI NEVER flags an individual student autonomously
  - AI NEVER voids attendance records
  - All AI fraud signals: labeled ADVISORY; human confirms all actions
```

---

## 🔟 LEAVE IMPACT RECONCILIATION ENGINE

```
LEAVE-ATTENDANCE BRIDGE (read integration from Faculty Management Agent):

  TRIGGER: Leave record status changes to APPROVED
  ACTION:
    1. Attendance Service receives: faculty.leave.approved kafka event
    2. Identifies: all timetable slots within leave date range
    3. For each slot:
       ├── Faculty slot: marked ABSENT_APPROVED (not ABSENT_UNMARKED)
       └── Substitute assignment: linked to slot (if confirmed)
    4. Student sessions in affected slots:
       ├── If substitute confirmed: SESSION_STATUS = ACTIVE (proceeds normally)
       └── If no substitute: SESSION_CANCELLED (students not penalized)
    5. Aggregate recalculation: triggered for affected students + faculty

  STUDENT LEAVE BRIDGE:
    TRIGGER: Student leave record approved (institute-managed)
    ACTION:
      1. Attendance Service receives: student.leave.approved kafka event
      2. Identifies: all sessions within leave date range
      3. Each session for that student: status = LEAVE_APPROVED
      4. Aggregate: LEAVE_APPROVED sessions excluded from denominator
         (if institute policy = not counted against)
      5. Parent: notified of leave period (mandatory if minor)

  RECONCILIATION_RULES =
    - Leave records received as read-only events; never mutated by this agent.
    - Reconciliation is AUTOMATIC; no manual trigger required.
    - Conflicts (leave overlap with exam session): CRITICAL_ALERT to HOD.
    - Post-reconciliation corrections: require HOD approval + audit log.
    - All reconciliation actions: emit kafka event: attendance.reconciled
```

---

## 1️⃣1️⃣ PARENT / GUARDIAN TRUST LAYER (READ-ONLY — ABSOLUTE)

```
PARENT_ATTENDANCE_VISIBILITY =

  ✅ ALLOWED (read-only, aggregated):
    - Child's subject-wise attendance percentage (current semester)
    - Aggregate attendance status (COMPLIANT / AT_RISK / NON_COMPLIANT)
    - Absence count per subject (no session details)
    - Detention risk alert (if threshold breached)
    - Monthly attendance summary
    - System notifications (threshold breaches, detention triggers)

  ❌ FORBIDDEN (absolute):
    - Individual session details (who else was present/absent)
    - Faculty remarks on specific sessions
    - Fraud flags on child's record (admin-visible only)
    - Other students' attendance data
    - Raw session timestamps or device data
    - Dojo session participation details

PARENT_NOTIFICATION_TRIGGERS =
  ├── AT_RISK threshold crossed              → email + in-app push
  ├── CRITICAL threshold crossed             → email + SMS + in-app push
  ├── DETAINED state triggered               → email + SMS + in-app push (urgent)
  ├── Student leave approved                 → email notification
  ├── Session cancelled (>3 consecutive)     → email digest
  └── Semester attendance report published   → email + in-app notification

PARENT_TRUST_RULES =
  - Parent access requires: student consent (age >18: optional; age <18: mandatory).
  - Parent account: always secondary; zero mutation rights.
  - Parent notification channels: email + push (configurable by parent).
  - Quiet hours: respected (no late-night SMS unless CRITICAL).
  - Parent data: tenant-isolated; no cross-student visibility.
  - Minor students (age <18): parent link is MANDATORY at enrollment.
  - Parent visibility of detention: immediate notification; no delay.
```

---

## 1️⃣2️⃣ ATTENDANCE REPORTING & ANALYTICS ENGINE

```
REPORT_TYPES =

  STUDENT-FACING REPORTS:
  ├── My Attendance Summary (subject-wise, current semester)
  ├── My Attendance Calendar (day-by-day heatmap)
  ├── Subject Compliance Status (COMPLIANT / AT_RISK / DETAINED)
  └── Attendance Trend Graph (last 4 weeks)

  FACULTY-FACING REPORTS:
  ├── Section Attendance Sheet (per session, all students)
  ├── Subject Attendance Register (full semester roll)
  ├── At-Risk Students List (below threshold)
  ├── Faculty Delivery Report (own sessions: DELIVERED / ABSENT)
  └── Defaulter List (for exam eligibility)

  HOD-FACING REPORTS:
  ├── Department Attendance Matrix (all sections + all subjects)
  ├── Faculty Delivery Performance (delivery rate per faculty)
  ├── At-Risk and Detained Students (department-wide)
  ├── Monthly Attendance Digest
  └── Pending Override / Dispute Queue

  ADMIN / PRINCIPAL REPORTS:
  ├── Institute-Wide Attendance Dashboard (real-time)
  ├── Overall Compliance Rate (% students above threshold)
  ├── Faculty Delivery Compliance Report
  ├── Detention Statistics (count, department-wise)
  ├── NAAC / NBA Attendance Compliance Report (format: configurable)
  └── Audit Trail Export (all overrides + corrections)

  PARENT REPORTS:
  ├── Child's Semester Attendance Summary (PDF downloadable)
  └── Monthly Attendance Notification Digest

  COMPLIANCE / REGULATORY REPORTS:
  ├── Statutory Attendance Report (format per UGC / AICTE / institution)
  ├── Minimum Attendance Compliance Certification (per student)
  └── Condonation Register

REPORT_RULES =
  - All reports: tenant-isolated; cross-tenant exports FORBIDDEN.
  - All exports: audit-logged (actor, timestamp, format, file_hash).
  - NAAC reports: auto-formatted per declared accreditation body.
  - Bulk exports (>500 records): Admin approval required; rate-limited.
  - Reports cannot be auto-emailed externally without Admin approval.
  - Data in reports: anonymized where student PII is not required.
  - Report retention: INSTITUTE_DECLARED (minimum: 5 years academic records).

ANALYTICS_FUNCTIONS (AI-ADVISORY) =
  ├── Absenteeism Trend Prediction
  │     → Predicts which students are likely to fall below threshold
  │     → Advisory alert to counsellor; no automated action
  │
  ├── Session Engagement Correlation
  │     → Correlates faculty delivery consistency with student performance
  │     → Advisory input for HOD performance review
  │
  ├── Proxy Pattern Detection
  │     → Identifies unusual attendance patterns (time-based, device-based)
  │     → Advisory fraud signal; human confirms
  │
  └── Optimal Session Timing Recommender
        → Suggests session time slots with highest historical attendance
        → Advisory input for timetable generation
```

---

## 1️⃣3️⃣ BIOMETRIC & HARDWARE INTEGRATION GOVERNANCE (R56 COMPLIANT)

```
HARDWARE_INTEGRATION_TYPES =
  ├── FINGERPRINT_SCANNER  (BIO_TYPE_01)
  ├── FACE_RECOGNITION     (BIO_TYPE_02)
  ├── IRIS_SCANNER         (BIO_TYPE_03 — future; declared but not default)
  ├── RFID_CARD_READER     (BIO_TYPE_04 — non-biometric; NFC variant)
  └── QR_KIOSK             (BIO_TYPE_05 — hardware-assisted QR)

INTEGRATION_ARCHITECTURE =
  Hardware Device
      │ (REST webhook to secure endpoint)
      ▼
  Attendance Service Webhook Receiver
      │ (validates: device_id + API key + payload signature)
      ▼
  Record Creation (match_result + device_id + timestamp)
      │
  Kafka Event: attendance.biometric.captured
      │
  Analytics + Audit + Parent Notification pipeline

BIOMETRIC_DATA_RULES (NON-NEGOTIABLE) =
  ✅ Platform receives:   match_result (PRESENT/ABSENT), device_id, timestamp
  ❌ Platform NEVER receives:  fingerprint templates, face embeddings, iris scans
  ❌ Platform NEVER stores:    any biometric raw data (GDPR/DPDP hard compliance)
  ✅ Liveness detection:  hardware vendor responsibility (declared in DPA)
  ✅ Data residency:      biometric processing stays on-device or in institute's
                          local hardware; never traverses Ecoskiller cloud
  ✅ Consent:             explicit biometric consent required per student/faculty
                          before device activation (stored in consent registry)

HARDWARE_VENDOR_GOVERNANCE (R56 COMPLIANCE) =
  - Every hardware vendor must be in Approved Integration Registry.
  - DPA required before hardware activation.
  - Vendor change: existing consent records voided; re-consent required.
  - Hardware offline fallback: QR_CODE_CHECK_IN (MODE_02) auto-activates.
  - Hardware tampering alert: device sends integrity signal; system flags.
  - Device decommission: all bindings cleared; audit record retained.

CONSENT_REGISTRY =
  biometric_consents { id, user_id, tenant_id, bio_type, device_id,
                       consent_given_at, consent_text_version,
                       revoked_at (nullable), revoked_reason }
  - Consent is revocable by student/faculty at any time.
  - Revocation: immediate; future sessions revert to QR/OTP fallback.
  - Revocation does not retroactively alter historical attendance records.
```

---

## 1️⃣4️⃣ CORPORATE / ENTERPRISE ATTENDANCE MODULE

```
APPLIES_TO = ENTERPRISE_TENANT (company/SME employer tenants)

CORPORATE_ATTENDANCE_CONTEXTS =
  ├── L&D_TRAINING_SESSION     (Upskilling, reskilling)
  ├── ONBOARDING_PROGRAM       (New hire orientation)
  ├── COMPLIANCE_TRAINING      (Mandatory regulatory training)
  ├── PERFORMANCE_COACHING     (Manager-led coaching)
  └── PLATFORM_ASSESSMENT      (Ecoskiller GD / skill assessment)

CORPORATE_RULES =
  - Corporate sessions: same capture modes available (QR, OTP, Manual, Biometric).
  - Corporate HR admin: same override authority as Institute Admin.
  - Bulk CSV import: supported for large batches; requires HR Admin sign-off.
  - Attendance data: linked to employee_id (not student_id).
  - Reports: formatted for L&D compliance (configurable per company).
  - Mandatory training compliance: tracked separately (COMPLIANCE_TRAINING context).
  - AI advisory: identifies employees at risk of non-completion.

CORPORATE_COMPLIANCE_REPORTING =
  - Training completion rate per cohort / department.
  - Mandatory compliance training status per employee.
  - Export: HR system integration via webhook (R56 governed).
  - NAAC-equivalent format not applicable; corporate format used.

CORPORATE_PARENT_LAYER =
  - No parent/guardian layer for corporate employees (adults).
  - Emergency contact notification: only for safety-critical events (not attendance).
```

---

## 1️⃣5️⃣ ATTENDANCE AUDIT & IMMUTABILITY ENGINE (CRITICAL)

```
AUDIT_IMMUTABILITY_RULES =

  RECORD CREATION:
    - Every attendance record: created with actor_id, timestamp, mode, session_id.
    - Creation is one-way: records can be CORRECTED but never DELETED.
    - Correction creates a new record; original is retained with CORRECTED flag.

  CORRECTION CHAIN:
    original_record (CORRECTED=true, superseded_by=correction_id)
      └── correction_record (actor_id, reason, timestamp, diff_json)
            └── [further corrections create additional links]
    Full chain always retained; no record ever removed from chain.

  LOCKING RULES:
    - Session lock: records lock 4 hours after session end (default).
    - Semester lock: all records lock at SEMESTER_CLOSE event.
    - Post-semester corrections: FORBIDDEN except via compliance escalation.
    - Compliance escalation: Platform Compliance Admin + Institute Principal dual sign.

  AUDIT_LOG_SCHEMA:
    attendance_audit_logs {
      id UUID,
      entity_type      VARCHAR (session | record | override | export),
      entity_id        UUID,
      action           VARCHAR (CREATED | CORRECTED | OVERRIDDEN | EXPORTED |
                                VOIDED | LOCKED | ESCALATED),
      actor_id         UUID,
      actor_role       VARCHAR,
      tenant_id        UUID,
      domain_track     VARCHAR,
      timestamp        TIMESTAMPTZ,
      diff_json        JSONB,
      ip_hash          VARCHAR,
      device_fingerprint VARCHAR,
      reason           TEXT (mandatory for all overrides),
      event_version    VARCHAR
    }

  AUDIT_RULES =
    - audit_logs: append-only; no UPDATE or DELETE ever.
    - All override actions: reason field mandatory (empty = REJECTED).
    - Audit export: available to Compliance Admin; rate-limited; logged.
    - Audit logs: retained for STATUTORY_PERIOD (minimum 7 years; configurable).
    - Cross-tenant audit: FORBIDDEN; tenant isolation at DB row-level security.

  NON-REPUDIATION:
    - Each record: server-side hash computed on creation.
    - Hash stored in: attendance_records.integrity_hash.
    - Hash verified on export; mismatch = INTEGRITY_FAILURE → alert.
    - Hash chain: each correction references parent hash.
```

---

## 1️⃣6️⃣ DATA MODEL — CORE ENTITIES (LOCKED SCHEMA ANCHORS)

```
CORE_ENTITIES =

  attendance_sessions {
    id UUID PK,
    tenant_id UUID,
    context_type VARCHAR (LECTURE|LAB|GD_MATCH|LIVE_CLASS|...),
    timetable_slot_id UUID (nullable for non-academic),
    session_date DATE,
    start_time TIME,
    end_time TIME,
    faculty_id UUID,
    section_id UUID (nullable for dojo/trainer sessions),
    capture_mode VARCHAR (MANUAL|QR|OTP|BIOMETRIC|NFC|DOJO_AUTO|TRAINER_AUTO),
    status VARCHAR (SCHEDULED|ACTIVE|COMPLETED|CANCELLED|RESCHEDULED),
    created_at TIMESTAMPTZ,
    integrity_hash VARCHAR
  }

  attendance_records {
    id UUID PK,
    session_id UUID FK,
    participant_id UUID (student_id | employee_id | faculty_id),
    participant_type VARCHAR (STUDENT|FACULTY|TRAINER|EMPLOYEE),
    tenant_id UUID,
    status VARCHAR (PRESENT|ABSENT|DUTY_LEAVE|LEAVE_APPROVED|
                    MEDICAL_ABSENT|SESSION_CANCELLED|CORRECTED|
                    ABSENT_FRAUD|PARTIALLY_PRESENT),
    capture_mode VARCHAR,
    recorded_by UUID (actor who created record),
    recorded_at TIMESTAMPTZ,
    device_fingerprint VARCHAR (nullable),
    geo_lat DECIMAL (nullable; only if geo-fence active),
    geo_lng DECIMAL (nullable; only if geo-fence active),
    presence_percent DECIMAL (for dojo/trainer sessions),
    corrected BOOLEAN DEFAULT false,
    superseded_by UUID FK (nullable; correction chain),
    integrity_hash VARCHAR
  }

  attendance_aggregates {
    id UUID PK,
    participant_id UUID,
    subject_id UUID (nullable for non-academic),
    context_type VARCHAR,
    tenant_id UUID,
    semester_id UUID (nullable),
    total_sessions INTEGER,
    present_count INTEGER,
    absent_count INTEGER,
    duty_leave_count INTEGER,
    leave_approved_count INTEGER,
    medical_absent_count INTEGER,
    cancelled_count INTEGER,
    effective_percentage DECIMAL,
    compliance_status VARCHAR (COMPLIANT|AT_RISK|NON_COMPLIANT|DETAINED),
    last_computed_at TIMESTAMPTZ
  }

  attendance_thresholds {
    id UUID PK,
    tenant_id UUID,
    institute_type VARCHAR,
    compliant_threshold DECIMAL,
    at_risk_threshold DECIMAL,
    detained_threshold DECIMAL,
    medical_credit_percent DECIMAL,
    condonation_limit DECIMAL,
    effective_from DATE,
    approved_by UUID,
    is_active BOOLEAN
  }

  biometric_consents {
    id UUID PK,
    user_id UUID,
    tenant_id UUID,
    bio_type VARCHAR,
    device_id VARCHAR,
    consent_given_at TIMESTAMPTZ,
    consent_text_version VARCHAR,
    revoked_at TIMESTAMPTZ (nullable),
    revoked_reason TEXT (nullable)
  }

  attendance_disputes {
    id UUID PK,
    record_id UUID FK,
    raised_by UUID,
    raised_at TIMESTAMPTZ,
    reason TEXT,
    status VARCHAR (PENDING|HOD_REVIEW|APPROVED|REJECTED),
    resolved_by UUID (nullable),
    resolved_at TIMESTAMPTZ (nullable),
    resolution_note TEXT (nullable)
  }

  fraud_flags {
    id UUID PK,
    session_id UUID,
    record_id UUID (nullable),
    flag_type VARCHAR (QR_SHARING|OTP_SHARING|GPS_SPOOF|MASS_PROXY|...),
    severity VARCHAR (ANOMALY|FRAUD_ALERT|FRAUD_CONFIRMED|SYSTEM_FRAUD),
    raised_by VARCHAR (SYSTEM|HOD|ADMIN),
    raised_at TIMESTAMPTZ,
    status VARCHAR (OPEN|UNDER_REVIEW|CONFIRMED|DISMISSED),
    resolved_by UUID (nullable),
    resolution_note TEXT (nullable)
  }

  attendance_audit_logs {
    id UUID PK,
    entity_type VARCHAR,
    entity_id UUID,
    action VARCHAR,
    actor_id UUID,
    actor_role VARCHAR,
    tenant_id UUID,
    domain_track VARCHAR,
    timestamp TIMESTAMPTZ,
    diff_json JSONB,
    ip_hash VARCHAR,
    device_fingerprint VARCHAR,
    reason TEXT,
    event_version VARCHAR
  }

DATA_RULES =
  - attendance_audit_logs: IMMUTABLE (append-only, no UPDATE/DELETE ever).
  - attendance_records: no DELETE; corrections create new linked records.
  - All tables: Row-Level Security at PostgreSQL level per tenant_id.
  - Cross-tenant queries: FORBIDDEN at ORM, API, and DB levels.
  - PII fields: encrypted at rest (participant_id stored as UUID reference).
  - Geo-location fields: null by default; only populated if geo-fence active.
  - Records lock at: session_lock_at (computed: session_end + lock_window).
  - integrity_hash: SHA-256 of (session_id + participant_id + status + timestamp).
  - Biometric raw data: NEVER appears in any Ecoskiller table (architectural rule).
  - Financial retention: attendance records minimum 5 years for academic compliance.
```

---

## 1️⃣7️⃣ KAFKA EVENT REGISTRY (MANDATORY)

```
ATTENDANCE_EVENT_SCHEMA =

  attendance.session.created       { session_id, tenant_id, context_type,
                                     faculty_id, section_id, date, capture_mode }
  attendance.session.started       { session_id, started_at, faculty_id }
  attendance.session.completed     { session_id, completed_at, total_marked }
  attendance.session.cancelled     { session_id, reason, cancelled_by }
  attendance.captured              { record_id, session_id, participant_id,
                                     status, capture_mode, timestamp }
  attendance.biometric.captured    { session_id, device_id, match_result, timestamp }
  attendance.override.requested    { dispute_id, record_id, raised_by, reason }
  attendance.override.approved     { dispute_id, record_id, approved_by, new_status }
  attendance.override.rejected     { dispute_id, record_id, rejected_by, reason }
  attendance.aggregate.updated     { participant_id, subject_id, semester_id,
                                     new_percentage, compliance_status }
  attendance.threshold.crossed     { participant_id, threshold_level, percentage,
                                     subject_id, tenant_id }
  attendance.detained.flagged      { participant_id, tenant_id, subjects_json }
  attendance.detained.confirmed    { participant_id, confirmed_by, timestamp }
  attendance.detained.lifted       { participant_id, lifted_by, reason, timestamp }
  attendance.condonation.applied   { participant_id, subject_id, approved_by }
  attendance.fraud.flagged         { fraud_flag_id, session_id, flag_type, severity }
  attendance.fraud.confirmed       { fraud_flag_id, confirmed_by, action_taken }
  attendance.reconciled            { participant_id, trigger_event, sessions_updated }
  attendance.report.exported       { report_type, actor_id, tenant_id, timestamp }
  attendance.session.locked        { session_id, locked_at }
  attendance.semester.locked       { semester_id, tenant_id, locked_at }

EVENT_RULES =
  - Every event: carries tenant_id, correlation_id, schema_version.
  - Events consumed by: Analytics Service, Notification Service,
                        Exam Service (detention gate), Job Service (detention gate),
                        Parent Notification Service, Compliance Reporting Engine.
  - No PII in event payload (use reference IDs only).
  - Retention: 90 days hot; 5 years cold archive (academic compliance).
  - Schema changes: require Event Schema Registry bump (R49 validation enforced).
  - Deduplication: event_id uniqueness enforced (UAMC-S compliance).
```

---

## 1️⃣8️⃣ API CONTRACT ANCHORS (LOCKED)

```
ATTENDANCE SERVICE INTERNAL URL = http://attendance-service:8007

MANDATORY API CONTRACTS =

  SESSION MANAGEMENT
  POST   /attendance/session/create
  PUT    /attendance/session/{id}/start
  PUT    /attendance/session/{id}/complete
  PUT    /attendance/session/{id}/cancel
  GET    /attendance/session/{id}

  QR / OTP CAPTURE
  POST   /attendance/qr/generate          (faculty generates; TTL-bound)
  POST   /attendance/qr/scan              (student scans; single-use)
  POST   /attendance/otp/generate         (faculty generates)
  POST   /attendance/otp/submit           (student submits)

  MANUAL MARKING
  POST   /attendance/manual/mark          (faculty marks per-student)
  POST   /attendance/manual/bulk          (CSV import; admin-only + signed)

  BIOMETRIC WEBHOOK
  POST   /attendance/biometric/webhook    (hardware → service; signed payload)
  POST   /attendance/biometric/consent    (student/faculty consent registration)

  DOJO SESSION
  POST   /attendance/dojo/presence        (WebSocket event ingest)
  GET    /attendance/dojo/{session_id}    (sealed record read)

  TRAINER SESSION
  POST   /attendance/trainer/presence     (LiveKit/Jitsi webhook)
  GET    /attendance/trainer/{session_id}/learners

  DISPUTES & OVERRIDES
  POST   /attendance/dispute/raise
  PUT    /attendance/dispute/{id}/approve
  PUT    /attendance/dispute/{id}/reject
  GET    /attendance/dispute/queue        (HOD view)

  AGGREGATES & COMPLIANCE
  GET    /attendance/{participant_id}/aggregate
  GET    /attendance/{participant_id}/subject/{subject_id}/summary
  GET    /attendance/section/{id}/report
  GET    /attendance/department/{id}/dashboard
  POST   /attendance/condonation/apply
  PUT    /attendance/condonation/{id}/approve

  REPORTS & EXPORT
  GET    /attendance/report/student/{id}
  GET    /attendance/report/naac/{semester_id}
  POST   /attendance/report/export        (audit-logged; rate-limited)

  FRAUD
  GET    /attendance/fraud/flags          (admin/hod view)
  PUT    /attendance/fraud/{id}/review
  PUT    /attendance/fraud/{id}/confirm
  PUT    /attendance/fraud/{id}/dismiss

API_RULES =
  - All APIs: JWT + tenant context header required.
  - Biometric webhook: signed payload (HMAC-SHA256); unsigned = REJECT.
  - QR/OTP endpoints: rate-limited per student per session (Kong gateway).
  - Bulk CSV endpoint: Admin-only; requires dual-signature header.
  - Export endpoints: Admin-only; audit-logged; max 1 per 10 minutes per actor.
  - All mutating APIs: emit Kafka event.
  - Detention-gate APIs: called by Exam Service + Job Service (read-only).
  - No API returns raw session presence data to parents (aggregates only).
  - API versioning: /v1/ prefix enforced; breaking changes → version bump (R19).
```

---

## 1️⃣9️⃣ UI MODULE — ATTENDANCE_UI (LOCKED)

```
MODULE_NAME     = Attendance_UI
PARENT_STACK    = Flutter (primary) | Next.js (SEO read-only clone per R31)
DESIGN_SYSTEM   = Ecoskiller shared design tokens (R33)
COLOR_PRIMARY   = #1E3A8A  |  ACCENT = #10B981
COLOR_WARNING   = #F59E0B  |  COLOR_DANGER = #DC2626

SCREEN INVENTORY (Role-Gated):

  FOR STUDENT:
  ├── My Attendance Dashboard
  │     (subject-wise compliance bars + heatmap calendar)
  ├── Subject Attendance Detail
  │     (session-by-session log; status per session)
  ├── Attendance Compliance Widget
  │     (COMPLIANT / AT_RISK / NON_COMPLIANT badge)
  ├── QR Scan Screen
  │     (camera-based; single-tap; confirmation feedback)
  ├── OTP Entry Screen
  │     (6-digit pad; rate-limited; confirmation feedback)
  ├── Dispute Request Screen
  │     (select session + reason + submit)
  └── Dojo Participation Summary
        (session count + presence percentage)

  FOR FACULTY / TRAINER:
  ├── Mark Attendance Screen
  │     (roll-call UI; present/absent/leave per student; bulk mark)
  ├── Generate QR Screen
  │     (timer display; QR display; regenerate option)
  ├── Generate OTP Screen
  │     (OTP display; countdown; broadcast button)
  ├── Session Attendance Summary
  │     (marked count + unmarked + absent list)
  ├── My Delivery Report
  │     (sessions delivered vs scheduled; rate gauge)
  └── At-Risk Students Widget
        (students approaching threshold in my subjects)

  FOR HOD:
  ├── Department Attendance Matrix
  │     (all sections + all subjects; sortable; filterable)
  ├── At-Risk Students Dashboard
  │     (list + threshold status + action buttons)
  ├── Detention Candidate List
  │     (students below threshold; approve/review workflow)
  ├── Override / Dispute Queue
  │     (pending disputes; approve/reject)
  ├── Fraud Flag Dashboard
  │     (open flags; severity; action buttons)
  └── Faculty Delivery Performance
        (delivery rate per faculty in department)

  FOR PRINCIPAL / ADMIN:
  ├── Institute Attendance Overview
  │     (real-time compliance rate + department breakdown)
  ├── Detention Management Console
  │     (confirm/lift detention; condonation approvals)
  ├── Compliance Report Generator
  │     (NAAC/NBA format; date range; export)
  ├── Fraud Management Console
  │     (all flags; investigation status; bulk void)
  ├── Bulk Override Console
  │     (event-based: holiday, emergency; Principal sign-off)
  └── Audit Trail Viewer
        (all overrides + corrections; export)

  FOR PARENT:
  ├── My Child's Attendance Summary (read-only)
  ├── Compliance Status Widget (COMPLIANT / AT_RISK badge)
  ├── Absence Count by Subject (no session details)
  └── Notification History (threshold alerts, detention notices)

UI_RULES =
  - MAX_SCREENS_PER_RUN    = 15
  - MAX_MODULES_PER_RUN    = 1
  - MAX_ROLES_PER_RUN      = 1
  - MAX_ENTITY_STATES      = 1
  - Forbidden actions: HIDDEN (not disabled) based on state + role.
  - Fixed dashboard areas: identity, role, compliance badges, critical alerts (40%).
  - Customizable: widgets, shortcuts, density (60%).
  - Fraud flag screens: screenshot BLOCKED (sensitive screen policy).
  - All screens: loading + empty + error states MANDATORY (no blank screens).
  - QR Scan Screen: camera permission required; graceful denial fallback.
  - Heatmap calendar: color-coded (green=present, red=absent, grey=cancelled).
  - Accessibility: WCAG 2.1 AA on every screen; color not sole indicator.
  - AT_RISK students: highlighted with warning badge (not punitive label in UI).
  - Parent screens: simplified; no raw session data; summary only.
```

---

## 2️⃣0️⃣ AI INTELLIGENCE LAYER — ATTENDANCE DOMAIN (ADVISORY ONLY)

```
AI_FUNCTIONS =

  ABSENTEEISM_TREND_PREDICTOR
    Input:  Rolling 4-week attendance pattern per student per subject
    Output: Risk probability + predicted compliance status at semester end
    Mode:   ADVISORY → counsellor alert; no automated action

  PROXY_PATTERN_DETECTOR
    Input:  QR/OTP redemption timestamps, device fingerprints, geo-signals
    Output: Anomaly score + flag type + confidence interval
    Mode:   ADVISORY → HOD sees ANOMALY_FLAG; human confirms FRAUD

  SESSION_ENGAGEMENT_CORRELATOR
    Input:  Faculty delivery rate + student attendance rate per subject
    Output: Correlation insight (low delivery → low attendance signal)
    Mode:   ADVISORY → HOD performance review input

  CONDONATION_ELIGIBILITY_IDENTIFIER
    Input:  Student attendance %, leave records, medical records, institute policy
    Output: Eligible students list + condonable % computation
    Mode:   ADVISORY → Principal reviews; human approves all condonations

  OPTIMAL_SESSION_TIMING_RECOMMENDER
    Input:  Historical attendance by time-of-day, day-of-week, faculty
    Output: Recommended schedule adjustments for higher attendance
    Mode:   ADVISORY → Timetable generator input (Faculty Management Agent)

  DETENTION_RISK_RANKER
    Input:  Current attendance %, remaining sessions in semester, leave balance
    Output: Ranked list of students by detention risk (high → low)
    Mode:   ADVISORY → HOD prioritizes intervention

AI_ABSOLUTE_RULES =
  ❌ AI NEVER marks attendance records.
  ❌ AI NEVER triggers detention state.
  ❌ AI NEVER confirms fraud against a student.
  ❌ AI NEVER approves condonation.
  ❌ AI NEVER modifies any attendance record, aggregate, or threshold.
  ✅ AI predictions: carry confidence intervals + data freshness labels.
  ✅ AI advisory outputs: labeled "ADVISORY — Review Required" on every UI.
  ✅ Cost-optimized per R28: rule engines for thresholds; traditional ML for
     pattern detection; LLMs only if natural language explanation needed.
  ✅ Minor student data: reduced granularity; no long-term profiling (UAMC-J).
  ✅ AI signals: non-punitive; feed policy layer only; no autonomous punishment.
```

---

## 2️⃣1️⃣ ANTIGRAVITY EXECUTION COMMAND PROTOCOL

```
═══════════════════════════════════════════════════════════════════════════
  HOW TO INVOKE ATTENDANCE_AGENT IN ANTIGRAVITY
═══════════════════════════════════════════════════════════════════════════

STEP 1: This prompt is loaded ONCE per context. Never re-paste.
STEP 2: Issue sealed execution commands in this exact format:

────────────────────────────────────────────────────────────────────────
EXECUTE_ATTENDANCE_AGENT

TENANT_TYPE       = [DEGREE_COLLEGE | UNIVERSITY | CORPORATE | TRAINING_ACADEMY]
ATTENDANCE_CONTEXT= [LECTURE | LAB_SESSION | GD_MATCH | LIVE_CLASS | COMPLIANCE_TRAINING]
CAPTURE_MODE      = [MANUAL | QR | OTP | BIOMETRIC | NFC | DOJO_AUTO | TRAINER_AUTO]
ROLE              = [STUDENT | FACULTY | TRAINER | HOD | PRINCIPAL | ADMIN | PARENT]
MODULE            = [CAPTURE_ENGINE | THRESHOLD_ENGINE | FRAUD_ENGINE |
                     REPORTING | BIOMETRIC_INTEGRATION | PARENT_TRUST |
                     DISPUTE_ENGINE | CONDONATION | AUDIT_ENGINE]
FEATURE           = [Mark_Attendance | Generate_QR | View_Aggregate |
                     Raise_Dispute | Export_NAAC | Fraud_Review |
                     Detention_Confirm | Condonation_Apply | ...]
ENTITY_STATE      = [NOT_MARKED | PRESENT | ABSENT | AT_RISK |
                     NON_COMPLIANT | DETAINED | SESSION_CANCELLED | LOCKED]
STAGE             = [FOUNDATION_UI | INTELLIGENCE_UI | ECOSYSTEM_UI | COMPLIANCE_UI]
────────────────────────────────────────────────────────────────────────

EXECUTION LIMITS (ANTIGRAVITY SAFETY):
  MAX_SCREENS_PER_RUN   = 15
  MAX_MODULES_PER_RUN   = 1
  MAX_ROLES_PER_RUN     = 1
  MAX_ENTITY_STATES     = 1
  Exceeding limits → STOP EXECUTION IMMEDIATELY. Report breach.

OUTPUT REQUIREMENTS (every generated artifact MUST include):
  ✔ Purpose declaration
  ✔ Target role + tenant type
  ✔ Attendance context type
  ✔ Capture mode
  ✔ Entity lifecycle state
  ✔ Fixed vs customizable UI areas
  ✔ Navigation entry & exit points
  ✔ Permissions required (RBAC + ABAC)
  ✔ State transitions visible in UI
  ✔ API endpoints consumed
  ✔ Kafka events emitted
  ✔ Anti-fraud controls active for this context
  ✔ AI advisory labels present (where applicable)
  ✔ Loading / empty / error states
  ✔ Accessibility notes (WCAG 2.1 AA)
  ✔ Parent visibility scope (if parent role involved)

Partial output → INVALID. Halt and report.
```

---

## 2️⃣2️⃣ COMPLIANCE & GOVERNANCE INHERITANCE (DO NOT DUPLICATE)

```
THIS AGENT INHERITS (finalized in master prompt — never re-declare):
  ✔ RBAC + ABAC authorization (Keycloak + OPA)
  ✔ JWT + MFA enforcement
  ✔ Session management
  ✔ Multi-tenant hard isolation (PostgreSQL Row-Level Security)
  ✔ Domain isolation (Arts | Commerce | Science | Tech | Administration)
  ✔ Encryption at rest (PII + geo data)
  ✔ Audit log immutability (append-only, no delete)
  ✔ WCAG 2.1 AA accessibility
  ✔ RTL + i18n localization
  ✔ Offline mode + sync rules (attendance marks queued offline)
  ✔ GDPR / DPDP / FERPA-ready data handling
  ✔ Screenshot blocking on sensitive screens (fraud flags)
  ✔ Rate limiting (Kong gateway — per role, per endpoint, per IP)
  ✔ CI/CD pipeline (no manual prod deploys)
  ✔ Prometheus + Loki + OpenTelemetry observability
  ✔ HashiCorp Vault (all biometric webhook secrets; zero hardcoded credentials)
  ✔ R28 intelligence cost optimization
  ✔ R29 screen-level UI generation standard
  ✔ R33 shared design system tokens
  ✔ R49 contract validator
  ✔ R50 automated QA test generator
  ✔ R56 third-party integration governance (biometric hardware vendors)
  ✔ R57 least-privilege access minimization
  ✔ UAMC-J minor student safety controls (reduced profiling)
  ✔ UAMC-P anti-surveillance safeguards (no covert monitoring)
  ✔ UAMC-S activity deduplication (idempotent event ingestion)
  ✔ RL rate limiting compliance (all attendance endpoints rate-limited)
  ✔ API-SEC API security compliance (all attendance APIs behind gateway)

DUPLICATION OF ANY ABOVE = CONFLICT → DENY → STOP EXECUTION
```

---

## 🔐 FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║                   ATTENDANCE_AGENT — FINAL STATUS                              ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  STATUS                      = SEALED & LOCKED                                 ║
║  EXECUTION_ENGINE            = ANTIGRAVITY                                     ║
║  VERSION                     = 1.0.0                                           ║
║  MUTATION_POLICY             = ADD_ONLY (semver bump mandatory)                ║
║  CREATIVE_INTERPRETATION     = FORBIDDEN                                       ║
║  ASSUMPTION_FILLING          = FORBIDDEN                                       ║
║  DEFAULT_BEHAVIOR            = DENY                                            ║
║  FAILURE_MODE                = HARD_STOP → REPORT → NO PARTIAL OUTPUT          ║
║  ANTIGRAVITY_CONFUSION       = IMPOSSIBLE                                      ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║  QUALITY_SCORE               = 10 / 10                                         ║
║  ENTERPRISE_SAFE             = ✔                                               ║
║  MULTI_TENANT_ISOLATED       = ✔                                               ║
║  DOMAIN_ISOLATED             = ✔                                               ║
║  AUDIT_IMMUTABLE             = ✔  (hash-chained, append-only)                  ║
║  AI_ADVISORY_ONLY            = ✔  (6 AI functions; zero autonomous actions)    ║
║  BIOMETRIC_ZERO_STORAGE      = ✔  (raw data never on platform)                 ║
║  PARENT_TRUST_LOCKED         = ✔  (aggregates only; zero session PII)          ║
║  ANTI_FRAUD_ENGINE           = ✔  (7 fraud types; 4 response levels)           ║
║  PROXY_DETECTION_ACTIVE      = ✔  (QR + OTP + GPS + mass proxy + dojo)         ║
║  KAFKA_EVENTS_REGISTERED     = ✔  (21 events declared)                         ║
║  API_CONTRACTS_LOCKED        = ✔  (30+ endpoints; role-gated)                  ║
║  UI_SCREENS_ROLE_GATED       = ✔  (5 role tiers; fraud screens blocked)        ║
║  LEAVE_RECONCILIATION        = ✔  (auto; kafka-driven; no manual trigger)      ║
║  CONDONATION_ENGINE          = ✔  (human-approved; AI eligibility advisory)    ║
║  DOJO_ATTENDANCE_SEALED      = ✔  (dual-channel; post-seal FORBIDDEN)          ║
║  CORPORATE_MODULE            = ✔  (L&D + compliance training contexts)         ║
║  REGULATORY_REPORTING        = ✔  (NAAC/NBA/UGC + corporate L&D formats)       ║
║  SIBLING_COMPATIBLE          = ✔  (Academic Structure + Faculty Mgmt agents)   ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║                                                                                  ║
║   ANY DEVIATION FROM THIS PROMPT:                                               ║
║   ❌ STOP EXECUTION                                                              ║
║   ❌ LOG VIOLATION WITH FULL CONTEXT                                             ║
║   ❌ ESCALATE TO ARCHITECT                                                       ║
║   ❌ NO PARTIAL OUTPUT PERMITTED                                                 ║
║                                                                                  ║
║   ANY ADDITION TO THIS PROMPT:                                                  ║
║   ✅ SEMVER BUMP REQUIRED  (1.0.0 → 1.1.0 or 2.0.0)                            ║
║   ✅ ARCHITECT APPROVAL MANDATORY                                                ║
║   ✅ BACKWARD COMPATIBILITY VERIFIED                                             ║
║   ✅ R49 CONTRACT VALIDATOR RE-RUN REQUIRED                                      ║
║   ✅ R50 QA TEST GENERATOR RE-RUN REQUIRED                                       ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```
