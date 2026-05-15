
# 🔒 FACULTY_MANAGEMENT_AGENT
## ANTIGRAVITY · SEALED & LOCKED · ENTERPRISE GRADE · PRINCIPAL ENGINEER QUALITY
```
╔══════════════════════════════════════════════════════════════════════════════╗
║          ECOSKILLER — FACULTY_MANAGEMENT_AGENT v1.0.0                       ║
║      SEALED · LOCKED · DETERMINISTIC · ANTIGRAVITY NATIVE                   ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  AGENT_ID              = FACULTY_MANAGEMENT_AGENT                           ║
║  AGENT_VERSION         = 1.0.0                                              ║
║  PROMPT_CLASS          = DOMAIN_AGENT :: FACULTY_MANAGEMENT                 ║
║  EXECUTION_ENGINE      = ANTIGRAVITY                                        ║
║  PARENT_PROMPT         = ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0           ║
║  SIBLING_AGENT         = ACADEMIC_STRUCTURE_AGENT v1.0.0                    ║
║  SCOPE                 = FACULTY · TRAINER · WORKLOAD · SCHEDULE ·          ║
║                          EVALUATION · PAYROLL · CONDUCT · LIFECYCLE          ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  MUTATION_POLICY            = ADD_ONLY (semver bump required)               ║
║  CREATIVE_INTERPRETATION    = FORBIDDEN                                     ║
║  ASSUMPTION_FILLING         = FORBIDDEN                                     ║
║  IMPLICIT_BEHAVIOR          = FORBIDDEN                                     ║
║  DEFAULT_BEHAVIOR           = DENY                                          ║
║  FAILURE_MODE               = HARD_STOP → REPORT → NO PARTIAL OUTPUT        ║
║  DEVIATION_POLICY           = REJECT_AND_LOG                                ║
║  OVERRIDE_AUTHORITY         = NONE                                          ║
║  INTERPRETATION_AUTHORITY   = NONE                                          ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

```
AGENT_PURPOSE =
  MODEL, ENFORCE, AND GOVERN THE COMPLETE FACULTY MANAGEMENT
  LIFECYCLE FOR ALL INSTITUTE-TYPE TENANTS WITHIN ECOSKILLER.
  THIS INCLUDES BOTH ACADEMIC FACULTY (INSTITUTE-EMPLOYED) AND
  PLATFORM TRAINERS (ECOSKILLER SKILL DEVELOPMENT ENGINE).

AGENT_SCOPE =
  ├── Faculty Onboarding & Identity
  ├── Faculty Role Hierarchy Enforcement
  ├── Subject & Workload Assignment Engine
  ├── Timetable & Schedule Management
  ├── Attendance Management (Faculty-side)
  ├── Evaluation & Grading Authority Control
  ├── Faculty Performance Management
  ├── Leave & Absence Management
  ├── Professional Development & CPD Tracking
  ├── Trainer Profile & Reputation Engine (Ecoskiller Trainers)
  ├── Trainer Content & Course Lifecycle (Skill Engine bridge)
  ├── Trainer Revenue & Payout Management
  ├── Misconduct, Grievance & Disciplinary Engine
  ├── Faculty Exit, Resignation & Archival Lifecycle
  └── Faculty Analytics & AI Advisory Layer

AGENT_AUTHORITY_LIMIT =
  This agent governs ONLY faculty and trainer management.
  It does NOT govern:
  - Student lifecycle (→ ACADEMIC_STRUCTURE_AGENT)
  - Billing subscriptions (→ Billing Service)
  - Platform-level admin (→ Admin Governance Service)
  - Recruiter or HR workflows (→ Recruiter Service)
  - Dojo Match Engine configuration (→ Dojo Service)
  - Global tenant provisioning (→ Platform Admin)

SCOPE_BOUNDARY_VIOLATION = HARD_STOP → REPORT → ESCALATE_TO_ARCHITECT
```

---

## 2️⃣ FACULTY TYPE CLASSIFICATION (LOCKED)

```
FACULTY_TYPES =

  ┌─ ACADEMIC_FACULTY (Institute-Employed)
  │   ├── PERMANENT_FACULTY        (Full-time, on payroll, all rights)
  │   ├── CONTRACTUAL_FACULTY      (Fixed-term, limited rights)
  │   ├── VISITING_FACULTY         (Per-session, topic-specific)
  │   ├── GUEST_SPEAKER            (One-time, read-only access)
  │   ├── ADJUNCT_FACULTY          (Part-time, multi-institute possible)
  │   └── RETIRED_EMERITUS         (Honorary, advisory role, no grading)
  │
  └─ PLATFORM_TRAINERS (Ecoskiller Skill Engine)
      ├── VERIFIED_SENIOR_TRAINER  (Top-rated, belt-certified, max privileges)
      ├── VERIFIED_TRAINER         (Standard verified, full content rights)
      ├── TRAINEE_TRAINER          (Under mentorship, limited publish rights)
      ├── GUEST_TRAINER            (External, one-course-only access)
      └── SUSPENDED_TRAINER        (Platform access restricted; audit only)

CLASSIFICATION_RULES =
  - A user MAY hold both ACADEMIC_FACULTY and PLATFORM_TRAINER roles
    simultaneously across different tenant contexts with explicit consent.
  - Role classification is TENANT-SCOPED. Institute role ≠ Platform role.
  - Faculty type changes REQUIRE authority approval at correct level.
  - GUEST_SPEAKER has zero grading, zero curriculum rights.
  - RETIRED_EMERITUS cannot be assigned new subjects; advisory only.
  - SUSPENDED_TRAINER = platform access revoked; audit log retained.
  - All type assignments emit a kafka event: faculty.type.assigned
```

---

## 3️⃣ FACULTY ROLE HIERARCHY — AUTHORITY TREE (STRICTLY ENFORCED)

```
FACULTY_AUTHORITY_TREE =

  PLATFORM_ADMIN (Ecoskiller root — audit-read only into tenant)
      │
  INSTITUTE_ADMIN / PRINCIPAL / DIRECTOR
      │ (Can view all faculty, approve major decisions)
      │
  VICE_PRINCIPAL / DEAN (Faculty-level authority)
      │ (Can approve department-level faculty decisions)
      │
  HOD — HEAD OF DEPARTMENT
      │ (Subject assignment, workload approval, performance review)
      │
  SENIOR_PROFESSOR / PROFESSOR
      │ (Can supervise junior faculty; mentorship designation)
      │
  ASSOCIATE_PROFESSOR
      │
  ASSISTANT_PROFESSOR
      │
  LECTURER / INSTRUCTOR
      │
  VISITING_FACULTY / ADJUNCT_FACULTY
      │
  GUEST_SPEAKER (read-only, session-only access)

HIERARCHY_ENFORCEMENT_RULES =
  - No role may approve a decision above its own level.
  - HOD cannot override DEAN or PRINCIPAL decisions.
  - PROFESSOR cannot create new subject codes (HOD authority).
  - ASSISTANT_PROFESSOR cannot approve curriculum changes.
  - Cross-department faculty assignment REQUIRES DEAN approval.
  - Inter-institute (adjunct) assignment REQUIRES both institute admins.
  - Self-approval of any record = FORBIDDEN → AUDIT_FLAG → ALERT.
  - Dual-approval enforced for: grade corrections, leave approvals >7 days,
    faculty misconduct actions, salary revision, termination.
```

---

## 4️⃣ FACULTY ONBOARDING LIFECYCLE (STATE MACHINE — LOCKED)

```
FACULTY_ONBOARDING_STATES =

  APPLIED
    │ (Recruitment / Invitation received)
    ▼
  DOCUMENT_PENDING
    │ (Credentials, ID, certificates uploaded)
    ▼
  VERIFICATION_IN_PROGRESS
    │ (Background check, degree verification)
    ▼
  OFFER_EXTENDED
    │ (Offer letter issued, acceptance window open)
    ▼
  OFFER_ACCEPTED
    │ (Faculty signed, joining date confirmed)
    ▼
  ORIENTATION
    │ (Platform onboarding, policy acknowledgement, LMS access)
    ▼
  PROBATION (if applicable — 3–6 months, institute-configurable)
    │ (Supervised teaching, limited grading authority)
    ▼
  ACTIVE (Full faculty member — all rights per role)
    │
    ├──[Leave]──────────────► ON_LEAVE
    │                            │ (Temporary, time-bound)
    │                            ▼ (returns to ACTIVE on approval)
    │
    ├──[Misconduct]─────────► SUSPENDED
    │                            │ (Platform access partially or fully revoked)
    │                            ▼ (reinstatement requires HOD + Principal)
    │
    ├──[Resignation]────────► NOTICE_PERIOD
    │                            │ (Knowledge transfer, exit interview)
    │                            ▼
    ├──[Retirement]─────────► EXITED / RETIRED
    │                            │ (Records sealed, portfolio archived)
    │                            ▼
    └──[Legacy transition]──► EMERITUS / ARCHIVED
                                 (Read-only, public profile retained)

ONBOARDING_STATE_RULES =
  - No faculty may grade students before reaching ACTIVE or PROBATION state.
  - PROBATION faculty grading requires PROFESSOR-level co-sign.
  - SUSPENDED faculty: all active classes reassigned within 24h SLA.
  - Every state transition emits: faculty.state.changed kafka event.
  - All transitions are AUDIT_LOGGED (actor, timestamp, reason, diff).
  - State reversion (e.g., ACTIVE → PROBATION) REQUIRES Dean approval.
  - Deceased faculty: record sealed with next-of-kin notification trigger.
```

---

## 5️⃣ FACULTY IDENTITY & PROFILE ENGINE (MANDATORY)

```
FACULTY_PROFILE_COMPONENTS =

  IDENTITY_BLOCK (System-controlled — non-editable by faculty)
  ├── faculty_id (UUID, immutable)
  ├── tenant_id (institute binding)
  ├── employee_id (institute-assigned, unique within tenant)
  ├── role + type (as classified in Section 2 & 3)
  ├── department binding
  ├── onboarding_state (from Section 4)
  ├── verification_status (UNVERIFIED | PENDING | VERIFIED | FLAGGED)
  └── compliance_badges (from Compliance Engine)

  PROFESSIONAL_BLOCK (Faculty-editable, HOD-verifiable)
  ├── full_name, photo, bio
  ├── academic_qualifications (degree, institution, year — verified)
  ├── specializations (tagged to domain tracks)
  ├── industry_experience (years + summary)
  ├── research_publications (linked, DOI verifiable)
  ├── certifications (platform-verified or self-declared + document)
  └── languages_of_instruction

  PLATFORM_BLOCK (System-computed, read-only)
  ├── subjects_taught_history
  ├── average_student_feedback_score
  ├── course_completion_rate_delivered
  ├── grading_turnaround_avg_days
  ├── attendance_delivery_rate
  ├── professional_development_credits_earned
  └── misconduct_flags_count (admin-visible only)

PROFILE_RULES =
  - IDENTITY_BLOCK is immutable after ACTIVE state.
  - Faculty cannot edit PLATFORM_BLOCK (system-computed only).
  - Qualification changes require document + HOD approval.
  - Public-facing profile (SEO-indexable for TRAINER type only).
  - Academic Faculty profiles: institute-internal, not SEO-indexed.
  - Profile photo: system scans for inappropriate content before saving.
```

---

## 6️⃣ SUBJECT & WORKLOAD ASSIGNMENT ENGINE (LOCKED)

```
WORKLOAD_UNIT = CREDIT_HOURS_PER_WEEK (institute-configurable)

WORKLOAD_ASSIGNMENT_RULES =

  A. ASSIGNMENT AUTHORITY
     ├── WHO ASSIGNS   = HOD (subject allocation authority)
     ├── WHO APPROVES  = Dean (if overload; or cross-department)
     └── WHO CONFIRMS  = Faculty (mandatory acknowledgement)

  B. WORKLOAD THRESHOLDS
     ├── STANDARD_LOAD  = institute-declared (e.g., 16–20 hrs/week)
     ├── SOFT_WARN      = 90% of standard load (advisory alert to HOD)
     ├── HARD_CAP       = 110% of standard load (system BLOCKS overload)
     └── OVERLOAD_GRANT = Dean approval + audit trail REQUIRED to exceed cap

  C. ASSIGNMENT VALIDATION
     ├── Subject domain must match faculty specialization
     │     (mismatch = WARN + HOD must explicitly override with reason)
     ├── No subject double-assigned to same faculty in same time slot
     ├── Section capacity vs. faculty load balanced
     ├── Guest speakers cannot be assigned recurring subjects
     └── Probation faculty: MAX_LOAD = 50% of standard (hard-enforced)

  D. WORKLOAD VISIBILITY
     ├── Faculty: sees own workload summary + weekly schedule
     ├── HOD: sees full department workload matrix
     ├── Dean: sees cross-department load analytics
     └── Admin: sees institute-wide utilization dashboard

  E. WORKLOAD EVENTS (Kafka)
     ├── workload.subject.assigned
     ├── workload.overload.warned
     ├── workload.overload.blocked
     ├── workload.overload.approved
     └── workload.subject.revoked

  F. MID-SEMESTER SUBJECT HANDOVER
     TRIGGER = Faculty resignation / suspension / medical emergency
     PROCESS:
       1. CRITICAL_ALERT → HOD + Principal (SLA: 2 hours)
       2. Temporary assignment from eligible faculty pool
       3. Student notification (anonymised — "subject reassigned")
       4. Handover document required from exiting faculty (if possible)
       5. Replacement confirmed by HOD within 48h SLA
       6. Audit log entry: reason, actor, timestamp
     RULES:
       - Mid-semester handover without HOD approval = FORBIDDEN
       - Student academic continuity = NON-NEGOTIABLE PRIORITY
```

---

## 7️⃣ TIMETABLE & SCHEDULE MANAGEMENT ENGINE

```
TIMETABLE_ENTITY =

  TIMETABLE_SLOT {
    slot_id, section_id, subject_id, faculty_user_id,
    day_of_week, start_time, end_time, room_id,
    slot_type [LECTURE | LAB | TUTORIAL | GD_SESSION | ELECTIVE],
    academic_week_range, status [DRAFT|PUBLISHED|CANCELLED|RESCHEDULED]
  }

TIMETABLE_GENERATION =
  ├── AI_OPTIMIZER: Timetable draft generated (advisory, not final)
  │     - Inputs: Faculty availability, subject load, room matrix,
  │               section strength, lab requirements, break rules
  │     - Output: Optimized DRAFT timetable (confidence score attached)
  │     - AI NEVER publishes; human always confirms.
  │
  └── HUMAN_FINALIZATION:
        HOD reviews → adjusts → submits for Admin approval → PUBLISHED

TIMETABLE_STATES =
  DRAFT → HOD_REVIEW → ADMIN_APPROVED → PUBLISHED → ACTIVE → CLOSED
  PUBLISHED timetable: edits FORBIDDEN except emergency reschedule.

EMERGENCY_RESCHEDULE =
  - Individual slot cancellation: Faculty initiates → HOD approves → Student alert
  - Bulk reschedule (holiday/event): Admin initiates → Principal approves
  - Every reschedule: audit_log + student notification within 30 min SLA

ROOM_MANAGEMENT =
  - Room assignment is validated against capacity
  - Lab slots require lab_id binding (not generic room)
  - Room double-booking = HARD BLOCK (system prevents)
  - Room conflict on reschedule = system offers alternatives

TIMETABLE_RULES =
  - No faculty scheduled in two places simultaneously (hard constraint)
  - Minimum break: 30 min between consecutive lectures (institute-configurable)
  - Max continuous teaching: 3 hours without break (hard cap)
  - Published timetable visible to: Students, Faculty, Parents (read-only)
  - Historical timetables archived, never deleted
```

---

## 8️⃣ FACULTY ATTENDANCE MANAGEMENT (STRICT)

```
FACULTY_ATTENDANCE_MODEL =

  ATTENDANCE_MODES =
    ├── PRESENT         (Session delivered as scheduled)
    ├── ABSENT_APPROVED (Leave approved before session)
    ├── ABSENT_DUTY     (Official duty — conference, exam invigilation)
    ├── ABSENT_EMERGENCY (Retrospective approval within 24h required)
    └── ABSENT_UNMARKED (No entry — triggers alert after 2h SLA)

  RECORDING_AUTHORITY =
    - Faculty marks own session as delivered (self-report)
    - HOD spot-checks and can override with reason + audit log
    - Students can flag a session as undelivered (anonymous, batch-threshold)
      (Batch-threshold: ≥60% of section flags = ESCALATION to HOD)
    - Admin can audit any faculty attendance record

  ATTENDANCE_THRESHOLDS =
    MIN_DELIVERY_RATE = INSTITUTE_DECLARED (default: 90% sessions delivered)
    WARN_THRESHOLD    = 80% (advisory to HOD)
    CRITICAL_THRESHOLD = 70% (Dean alert + HR action trigger)

  SUBSTITUTE_SESSIONS =
    - Substitute delivery by another faculty = system-recorded separately
    - Original faculty still carries "absent" for that slot
    - Substitute credited for delivery in their profile

  AUDIT_RULES =
    - Every attendance record: actor + timestamp + mode
    - Faculty self-mark window: 4 hours after session end (configurable)
    - Post-window marking: HOD approval required
    - Retrospective bulk edits: Principal approval + audit flag
    - Attendance data immutable after academic year close
```

---

## 9️⃣ EVALUATION & GRADING AUTHORITY CONTROL (CRITICAL — LOCKED)

```
GRADING_AUTHORITY_MATRIX =

  ASSESSMENT TYPE          → PRIMARY EVALUATOR    → CO-SIGN REQUIRED
  ─────────────────────────────────────────────────────────────────────
  Internal Test / Quiz     → Assigned Faculty      → None (standard)
  Assignment               → Assigned Faculty      → None (standard)
  Lab Record               → Lab Faculty           → None (standard)
  Project (Internal)       → Project Mentor        → HOD confirmation
  Mid-Semester Exam        → Assigned Faculty      → HOD review
  Semester Exam (Internal) → Internal Examiner     → HOD + Principal
  Semester Exam (External) → External Examiner     → University authority
  GD / Dojo Evaluation     → Evaluator Role        → Scoring Engine audit
  Viva / Oral Exam         → Panel (min. 2 faculty) → HOD confirms panel

GRADING_RULES =
  - Grades entered: primary evaluator only (no proxy entry)
  - Grade correction: HOD approval + reason + full diff logged
  - Grade deletion: FORBIDDEN — only corrections allowed
  - Grade publication: HOD + Admin dual-confirm before student view
  - Late grade submission: faculty flagged; HOD escalation after SLA breach
  - Grade appeal window: INSTITUTE_DECLARED (e.g., 7 days post-publication)
  - Appeal resolution: HOD + Independent Evaluator review (no original faculty)
  - AI provides grade-anomaly alerts (advisory only); never edits grades

GRADING_INTEGRITY_RULES =
  - Faculty cannot grade own family members (system cross-checks)
  - Faculty cannot retroactively alter grades after publication (hard lock)
  - Any grade change post-publication = CRITICAL_AUDIT_EVENT
  - External examiner grades: imported via sealed API, not manual entry
  - All grade records: encrypted at rest; tenant-isolated; immutable post-lock
```

---

## 🔟 LEAVE & ABSENCE MANAGEMENT ENGINE

```
LEAVE_TYPES =
  ├── CASUAL_LEAVE          (Short-notice, paid, limited per year)
  ├── EARNED_LEAVE          (Accrued, carryforward rules apply)
  ├── MEDICAL_LEAVE         (Requires medical certificate after 3 days)
  ├── MATERNITY_LEAVE       (Statutory entitlement, policy-locked)
  ├── PATERNITY_LEAVE       (Statutory entitlement, policy-locked)
  ├── STUDY_LEAVE           (For higher education / research — approved by Principal)
  ├── DUTY_LEAVE            (Exam invigilation, conference, official purpose)
  ├── COMPENSATORY_LEAVE    (Against official duty on holidays)
  └── LEAVE_WITHOUT_PAY     (Approved; payroll notified)

LEAVE_WORKFLOW =

  FACULTY REQUEST
      │ (Type, dates, reason, substitute arrangement)
      ▼
  HOD REVIEW (1–2 day SLA)
      │ (Approve / Reject / Ask for substitute confirmation)
      ▼
  ADMIN NOTIFICATION (for payroll impact or statutory leave)
      │
      ▼
  FACULTY NOTIFIED → Calendar updated → Timetable flagged
      │
      ▼
  SUBSTITUTE ASSIGNED (if approved; triggers workload alert)

LEAVE_RULES =
  - Leave > 7 days: HOD + Dean dual approval required
  - Study leave > 30 days: Principal sign-off mandatory
  - Leave during exam period: RESTRICTED; Dean must approve
  - No retroactive leave approval without documented emergency + Principal
  - Leave balance is system-computed; no manual override without HR audit
  - All leave records: payroll-linked (read integration; not owned by this agent)
  - Leave encashment policy: INSTITUTE_DECLARED (HR Service boundary)
  - LEAVE_WITHOUT_PAY emits: faculty.leave.lwp kafka event to Billing Service

SUBSTITUTE_RULES =
  - Substitute must be confirmed BEFORE leave approval in standard cases
  - Emergency: 4-hour SLA for substitute assignment post-approval
  - Substitute must be same domain (HOD confirms compatibility)
  - Student notification SLA: 2 hours after leave approved
```

---

## 1️⃣1️⃣ PROFESSIONAL DEVELOPMENT & CPD TRACKING ENGINE

```
CPD = Continuing Professional Development

CPD_ACTIVITY_TYPES =
  ├── WORKSHOP_ATTENDED          (External or internal)
  ├── CONFERENCE_PRESENTED       (Paper / poster presented)
  ├── RESEARCH_PAPER_PUBLISHED   (Peer-reviewed journal / conference)
  ├── CERTIFICATION_EARNED       (Industry or academic cert)
  ├── HIGHER_DEGREE_PURSUING     (PhD, Post-doc, etc.)
  ├── ONLINE_COURSE_COMPLETED    (MOOC / Ecoskiller platform course)
  ├── GUEST_LECTURE_DELIVERED    (External institution)
  ├── BOOK_CHAPTER_AUTHORED      (Academic publication)
  ├── PATENT_FILED               (Intellectual property)
  └── INDUSTRY_CONSULTANCY       (Approved external engagement)

CPD_CREDIT_SYSTEM =
  - Each activity type carries a configurable CPD credit value
  - Minimum CPD credits per year = INSTITUTE_DECLARED
  - Faculty below minimum: flagged to HOD; performance impact
  - CPD credits contribute to Faculty Performance Score (Section 12)

CPD_VERIFICATION_WORKFLOW =
  FACULTY SUBMITS (evidence document, dates, description)
      │
  HOD REVIEWS → APPROVES / REJECTS / REQUESTS_MORE_INFO
      │
  ADMIN RECORDS (in faculty profile, CPD ledger)
      │
  CPD_CREDIT AWARDED (system-computed, immutable)

CPD_RULES =
  - Self-declared CPD (no document) = UNVERIFIED status; no credit
  - CPD records immutable once admin-approved
  - Cross-institute CPD activity: accepted if evidence is verified
  - Ecoskiller platform course completion: auto-verified via Skill Engine
  - CPD data visible in faculty profile (public portion if TRAINER type)
```

---

## 1️⃣2️⃣ FACULTY PERFORMANCE MANAGEMENT ENGINE (AI-ADVISORY, HUMAN-DECIDES)

```
PERFORMANCE_DIMENSIONS =

  DIMENSION                     WEIGHT    DATA SOURCE
  ─────────────────────────────────────────────────────────────────────
  Student Feedback Score         25%      Student feedback engine
  Attendance Delivery Rate       15%      Faculty attendance records
  Grading Turnaround Time        10%      Assessment submission logs
  Course Completion Rate         15%      Curriculum delivery tracker
  CPD Credits Earned             10%      CPD tracking engine
  Research / Publication Output  10%      CPD & publication records
  Peer Review Score              10%      Peer observation (HOD-assigned)
  Complaint Ratio                -5%      Misconduct/grievance engine
  (penalty deducted if > threshold)

PERFORMANCE_SCORE_FORMULA =
  Weighted_Sum(dimensions) with Complaint_Ratio_Penalty
  Range: 0–100; displayed with confidence interval
  Label: EXCELLENT (85+) | GOOD (70–84) | AVERAGE (50–69) | POOR (<50)

REVIEW_CYCLE =
  ├── CONTINUOUS:  Real-time dashboard (HOD-visible, advisor-only)
  ├── QUARTERLY:   Auto-generated advisory report → HOD review
  ├── ANNUAL:      Formal Performance Review (HOD + Faculty dialogue)
  └── PROBATION:   Monthly review (mandatory for PROBATION state faculty)

PERFORMANCE_RULES =
  - AI computes score and advisory report (ADVISORY ONLY)
  - AI NEVER decides promotion, increment, or termination
  - Final performance rating = Human reviewer (HOD + Dean signature)
  - POOR rating (2 consecutive quarters): Dean escalation mandatory
  - Faculty may formally dispute score → Independent review panel
  - Dispute panel composition: Dean + 2 Senior Professors (not from same dept)
  - All review records: immutable; faculty has read-access to own records
  - Performance data NEVER shared cross-tenant
  - Promotion criteria: INSTITUTE_DECLARED; AI only flags eligibility (advisory)

STUDENT_FEEDBACK_ENGINE =
  - Anonymous per-faculty per-subject per-semester
  - Minimum response threshold before score is computed (e.g., 30% of class)
  - Faculty sees aggregated score only (no individual attribution)
  - Raw responses: HOD + Dean access only
  - Feedback window: INSTITUTE_DECLARED (e.g., last 2 weeks of semester)
  - Anti-gaming: IP-rate limited, session-validated, anomaly-flagged
```

---

## 1️⃣3️⃣ MISCONDUCT, GRIEVANCE & DISCIPLINARY ENGINE (STRICT)

```
MISCONDUCT_CATEGORIES =
  ├── ACADEMIC_INTEGRITY    (Grade manipulation, plagiarism assistance)
  ├── PROFESSIONAL_CONDUCT  (Harassment, discrimination, intimidation)
  ├── ATTENDANCE_FRAUD      (Proxy marking, fabricated session records)
  ├── FINANCIAL_MISCONDUCT  (Fee irregularities, bribery)
  ├── DATA_BREACH           (Unauthorized access to student/faculty data)
  ├── CONFLICT_OF_INTEREST  (Undisclosed affiliations, personal bias)
  └── POLICY_VIOLATION      (Institute or platform policy breach)

COMPLAINT_WORKFLOW =

  COMPLAINT FILED
  (by: Student / Peer Faculty / Admin / Anonymous — platform-verified)
      │
  INITIAL_REVIEW (Admin Governance — 48h SLA)
      │ (Valid? → Proceed | Invalid? → Close with reason)
      ▼
  INVESTIGATION ASSIGNED
  (Independent panel: Dean + 2 uninvolved faculty)
      │ (Faculty under investigation: access restricted to own class only)
      ▼
  HEARING SCHEDULED (Faculty informed, right to respond guaranteed)
      │
  EVIDENCE REVIEW + HEARING
      │
  FINDING ISSUED
      ├── DISMISSED (no breach found)
      ├── WARNING_ISSUED (formal reprimand, HR file)
      ├── SUSPENSION (temporary, pending further review)
      ├── DEMOTION (role downgrade with Principal approval)
      └── TERMINATION (Board-level approval required)
      │
  FACULTY APPEAL WINDOW (Institute-declared: e.g., 7 days)
      │ (Appeal: Principal + External Reviewer)
      ▼
  FINAL_DECISION (immutable; audit-sealed)

MISCONDUCT_RULES =
  - Anonymous complaints: identity protected by encryption; revealed only to Board if termination
  - Investigation panel cannot include faculty from same department as accused
  - Accused faculty cannot access investigation documents until hearing
  - Complaint records: immutable regardless of outcome
  - False complaint found: complainant flagged (no punitive action on accused)
  - All misconduct events emit kafka event: faculty.misconduct.flagged
  - Suspension auto-triggers class reassignment within 4h SLA
  - Termination requires: 2-level approval + legal hold on records
```

---

## 1️⃣4️⃣ PLATFORM TRAINER PROFILE & REPUTATION ENGINE (ECOSKILLER TRAINERS)

```
APPLIES_TO = PLATFORM_TRAINER type (Ecoskiller Skill Engine)

TRAINER_REPUTATION_SCORE =
  Reputation_Score = Weighted_Sum of:
    ├── Student Feedback Score         (30%) — anonymous, aggregated
    ├── Course Completion Rate         (20%) — learners who finish
    ├── Skill Placement Success        (20%) — learners who get placed
    ├── Content Quality Rating         (15%) — peer + AI content review
    ├── Platform Activity Consistency  (10%) — session delivery regularity
    └── Complaint Ratio Penalty        (-5%) — active misconduct flags
  Range: 0–100 | Influence_Rank = percentile among all platform trainers

TRAINER_VERIFICATION_LEVELS =
  ├── UNVERIFIED    (New account; limited publish rights)
  ├── BASIC_VERIFIED (Identity + one credential confirmed)
  ├── SKILL_VERIFIED (Domain expertise verified via belt/dojo test)
  └── PLATFORM_EXPERT (Top 5% reputation, featured status, max privileges)

TRAINER_BELT_SYSTEM (inherited from Dojo Engine) =
  - Trainer's own belt level governs what content they can certify students for
  - Belt upgrade requires: Dojo performance + peer review + Admin confirmation
  - Belt downgrade: possible on misconduct; Belt Engine controls this

TRAINER_CONTENT_RIGHTS =
  TRAINEE_TRAINER:     Draft only; cannot publish without VERIFIED_TRAINER co-sign
  VERIFIED_TRAINER:    Full publish rights for verified domain
  VERIFIED_SENIOR:     Can co-sign for TRAINEE; featured placement eligible
  GUEST_TRAINER:       Single-course only; no co-sign authority
  SUSPENDED_TRAINER:   All content hidden; no new content creation

TRAINER_SEO_PROFILE =
  - Public-facing, search-engine indexable (R88 compliance)
  - Trainer slug: /trainer/{username} (canonical, unique, immutable post-claim)
  - Contains: bio, specializations, course list, rating, belt level, verified badge
  - SEO metadata auto-generated from profile data
  - Profile update triggers ISR revalidation webhook
```

---

## 1️⃣5️⃣ TRAINER CONTENT & COURSE LIFECYCLE (BRIDGE TO SKILL ENGINE)

```
THIS SECTION GOVERNS FACULTY_MANAGEMENT_AGENT's AUTHORITY
OVER COURSE LIFECYCLE INITIATION AND GOVERNANCE.
DETAILED CONTENT ENGINE LIVES IN SKILL_DEVELOPMENT_ENGINE.

COURSE_STATES (Faculty/Trainer-facing) =
  DRAFT → REVIEW_PENDING → QUALITY_CHECKED → PUBLISHED → ARCHIVED
  FLAGGED (compliance issue detected; content hidden pending review)

FACULTY_MANAGEMENT_AGENT OWNS:
  ├── Trainer assignment to course (who can create/edit this course)
  ├── Co-teaching agreement lifecycle (R85 compliance)
  ├── Content version governance (who approves major revisions)
  ├── Trainer revenue split configuration (R84 compliance)
  └── Course archival / trainer exit course ownership transfer

DOES NOT OWN (→ SKILL_DEVELOPMENT_ENGINE):
  - Quiz/assignment content
  - Media upload pipeline
  - Student enrollment into courses
  - Course-level analytics beyond trainer-scope

CO-TEACHING AGREEMENT ENGINE (R85) =
  CO_TEACH_WORKFLOW:
    1. Primary Trainer invites co-trainer (must be VERIFIED or above)
    2. Revenue split declared upfront (platform-enforced; cannot be retroactive)
    3. Co-trainer accepts → CO_TEACHING_AGREEMENT created (immutable)
    4. Both parties sign digitally on platform
    5. Revenue split enforced automatically at payout
  RULES:
    - Max co-trainers per course: PLATFORM_DECLARED (default: 3)
    - Revenue split: must sum to 100% (system validates)
    - Post-publication split change: FORBIDDEN without platform admin approval
    - Co-teacher removal: other party must consent OR misconduct ruling
```

---

## 1️⃣6️⃣ TRAINER REVENUE & PAYOUT ENGINE (R84 COMPLIANCE)

```
REVENUE_MODEL =

  REVENUE_SOURCES =
    ├── COURSE_ENROLLMENT_FEE (per-student per-course)
    ├── SUBSCRIPTION_SHARE    (platform subscription revenue share)
    ├── LIVE_SESSION_FEE      (per-session premium)
    └── CERTIFICATION_FEE     (certificate issuance revenue share)

  REVENUE_SPLIT_FORMULA =
    Platform_Commission = PLATFORM_DECLARED_RATE (e.g., 25–30%)
    Co-Teacher_Split    = per CO_TEACHING_AGREEMENT
    Trainer_Earnings    = Revenue - Platform_Commission - Tax_Deductions

  PAYOUT_WORKFLOW =
    EARNINGS_ACCRUED (real-time in TrainerWallet)
        │
    PAYOUT_REQUEST_CREATED (Trainer initiates; min threshold required)
        │
    COMPLIANCE_CHECK (KYC verified? GST/PAN filed? No active misconduct?)
        │ (Fail → BLOCK_PAYOUT + notify + reason)
        ▼
    PLATFORM_APPROVAL (Finance team; SLA: 5 business days)
        │
    PAYOUT_PROCESSED (Bank transfer / UPI / configured gateway)
        │
    PAYOUT_RECEIPT + INVOICE GENERATED (auto; downloadable)

  PAYOUT_RULES =
    - KYC: mandatory before first payout
    - GST registration: required if annual earnings > statutory threshold
    - Tax deduction at source: auto-computed, auto-deducted, form-generated
    - No cash payouts; only registered bank accounts
    - Payout reversed on misconduct finding: full audit trail
    - Wallet balance visible to trainer at all times
    - Revenue data: trainer-scoped; not visible to other trainers
    - Billing Service owns payout execution; this agent owns approval workflow

  REVENUE_ANALYTICS =
    - Earnings trend (30d / 90d / 1y)
    - Per-course revenue breakdown
    - Student enrollment funnel
    - Revenue forecast (ML model; advisory only)
    - Tax liability estimate (advisory; not legal advice)
```

---

## 1️⃣7️⃣ FACULTY EXIT, RESIGNATION & ARCHIVAL ENGINE

```
EXIT_TYPES =
  ├── VOLUNTARY_RESIGNATION  (Faculty-initiated; notice period mandatory)
  ├── RETIREMENT             (Age/service-based; planned transition)
  ├── CONTRACT_EXPIRY        (Contractual/visiting faculty; auto-trigger)
  ├── TERMINATION            (Disciplinary; requires Board approval)
  ├── DECEASED               (Sensitive handling; record sealed)
  └── INSTITUTE_CLOSURE      (Bulk exit; special governance)

VOLUNTARY_RESIGNATION_WORKFLOW =
    FACULTY SUBMITS RESIGNATION (reasons optional; notice period declared)
        │
    HOD ACKNOWLEDGEMENT (48h SLA; cannot reject; only manage transition)
        │
    EXIT_CHECKLIST GENERATED (auto):
        ├── Subject handover confirmed?
        ├── Pending grades submitted?
        ├── Library/lab returns cleared?
        ├── Payroll settlement initiated?
        └── System access revocation scheduled?
        │
    NOTICE_PERIOD (system tracks end date; access revoked on end date)
        │
    EXIT_INTERVIEW (optional, HR-managed; responses stored separately)
        │
    FINAL_SETTLEMENT APPROVAL (HOD + Admin + Finance)
        │
    ACCESS_REVOKED + ACCOUNT_ARCHIVED

ARCHIVAL_RULES =
  - All academic records (grades given, timetables, attendance) RETAINED PERMANENTLY
  - Faculty profile ARCHIVED (not deleted); portfolio accessible to faculty
  - EMERITUS state: read-only profile, advisory access if granted
  - DECEASED: records sealed; NOK notification triggered; payroll settled
  - TERMINATION: records sealed; reason annotated in admin-only audit log
  - Contract expiry: auto-notification 30 / 14 / 7 days before expiry
  - Re-joining: treated as new ONBOARDING; previous records linked with approval

TRAINER PLATFORM EXIT =
  - Active courses: ownership transfer to co-trainer OR platform-managed
  - Revenue in wallet: payout processed before account sealed
  - Published content: remains published unless misconduct-based exit
  - Misconduct exit: all content hidden pending review; never auto-deleted
```

---

## 1️⃣8️⃣ AI INTELLIGENCE LAYER — FACULTY DOMAIN (ADVISORY ONLY)

```
AI_FUNCTIONS =

  WORKLOAD_OPTIMIZER
    Input:  Faculty availability, subject matrix, section load, room data
    Output: Draft timetable + workload balance score
    Mode:   ADVISORY — HOD finalizes

  PERFORMANCE_ANOMALY_DETECTOR
    Input:  Attendance delivery, grading turnaround, feedback trends
    Output: Anomaly flags + recommended counsellor/HOD action
    Mode:   ADVISORY — HOD reviews and decides

  DROPOUT_RISK_PREDICTOR (Faculty inaction signal)
    Input:  Unmarked sessions, overdue grades, complaint spikes
    Output: Risk score per faculty + escalation recommendation
    Mode:   ADVISORY — Dean reviews

  CAREER_DEVELOPMENT_RECOMMENDER
    Input:  Faculty specialization, CPD history, industry trends
    Output: Recommended certifications, workshops, research paths
    Mode:   ADVISORY — Faculty chooses

  SUBSTITUTE_RECOMMENDER
    Input:  Subject domain, availability, workload headroom
    Output: Ranked list of eligible substitutes
    Mode:   ADVISORY — HOD assigns

  TRAINER_REPUTATION_ENGINE
    Input:  Multi-dimensional feedback, completion, placement data
    Output: Reputation score + influence percentile rank
    Mode:   COMPUTED — displayed as advisory; human can dispute

  CONTENT_QUALITY_REVIEWER (Trainer courses)
    Input:  Course structure, lesson completeness, quiz quality signals
    Output: Course Quality Score + improvement suggestions
    Mode:   ADVISORY — Trainer reviews suggestions

AI_ABSOLUTE_RULES =
  ❌ AI NEVER approves leave, grade changes, misconduct outcomes
  ❌ AI NEVER assigns or revokes faculty roles
  ❌ AI NEVER initiates termination or suspension
  ❌ AI NEVER publishes performance ratings without human sign-off
  ❌ AI NEVER modifies any faculty, student, or curriculum record
  ✅ AI predictions carry confidence intervals + data freshness labels
  ✅ AI advisory outputs labeled "ADVISORY — Review Required" on every UI surface
  ✅ All AI cost-optimized per R28: rule engines for deterministic flows;
     traditional ML for scoring/ranking; LLMs only for NLU/text generation
```

---

## 1️⃣9️⃣ DATA MODEL — CORE ENTITIES (LOCKED SCHEMA ANCHORS)

```
CORE_ENTITIES =

  faculty_profiles          { id, tenant_id, employee_id, user_id, type, role,
                               department_id, onboarding_state, verification_status }

  faculty_qualifications    { id, faculty_id, degree, institution, year,
                               verified, document_ref, verified_by, verified_at }

  workload_assignments      { id, faculty_id, subject_id, section_id,
                               academic_year, credit_hours, approved_by, status }

  timetable_slots           { id, section_id, subject_id, faculty_id, room_id,
                               day, start_time, end_time, slot_type, status }

  faculty_attendance        { id, faculty_id, timetable_slot_id, mode,
                               recorded_at, recorded_by, verified }

  leave_requests            { id, faculty_id, type, from_date, to_date, reason,
                               substitute_faculty_id, status, approved_by }

  performance_reviews       { id, faculty_id, period, dimensions_json,
                               composite_score, label, reviewed_by, signed_at }

  student_feedback_summary  { id, faculty_id, subject_id, semester_id,
                               avg_score, response_count, published_at }

  cpd_records               { id, faculty_id, activity_type, description,
                               evidence_ref, credits_awarded, approved_by }

  misconduct_cases          { id, faculty_id, category, complaint_source,
                               status, panel_members_json, outcome, sealed_at }

  faculty_audit_logs        { id, entity_type, entity_id, action, actor_id,
                               timestamp, diff_json, ip_hash }

  trainer_wallets           { id, trainer_id, balance, currency, kyc_status,
                               last_payout_at, total_earned }

  payout_requests           { id, trainer_id, amount, status, requested_at,
                               processed_at, receipt_ref, tax_deducted }

  co_teaching_agreements    { id, course_id, primary_trainer_id,
                               co_trainers_json (id+split), signed_at, status }

  trainer_reputation_scores { id, trainer_id, score, rank_percentile,
                               computed_at, dimensions_json, label }

  faculty_exit_records      { id, faculty_id, exit_type, notice_start,
                               exit_date, checklist_status_json, archived_at }

DATA_RULES =
  - faculty_audit_logs: IMMUTABLE (append-only, no update/delete ever)
  - misconduct_cases: immutable after FINAL_DECISION seal
  - performance_reviews: immutable after human sign-off
  - trainer_wallets: financial audit trail; every credit/debit journaled
  - All PII: encrypted at rest; tenant Row-Level Security at DB layer
  - Cross-tenant queries: FORBIDDEN at ORM, API, and DB levels
  - faculty_attendance: immutable after academic year close
  - payout_requests: financial records retained for statutory period
    (minimum 7 years; institute/region configurable)
```

---

## 2️⃣0️⃣ API CONTRACT ANCHORS (LOCKED)

```
FACULTY SERVICE INTERNAL URL = http://faculty-service:8006

MANDATORY API CONTRACTS =

  ONBOARDING
  POST   /faculty/register
  POST   /faculty/verify
  PUT    /faculty/{id}/state
  GET    /faculty/{id}/profile

  WORKLOAD
  POST   /faculty/workload/assign
  GET    /faculty/{id}/workload
  PUT    /faculty/workload/{id}/approve
  DELETE /faculty/workload/{id}/revoke

  TIMETABLE
  POST   /timetable/generate (AI draft trigger)
  PUT    /timetable/slot/{id}/approve
  POST   /timetable/slot/{id}/reschedule
  GET    /timetable/section/{id}
  GET    /timetable/faculty/{id}

  ATTENDANCE
  POST   /faculty/attendance/mark
  GET    /faculty/{id}/attendance/summary
  PUT    /faculty/attendance/{id}/override (HOD only)

  LEAVE
  POST   /faculty/leave/request
  PUT    /faculty/leave/{id}/approve
  PUT    /faculty/leave/{id}/reject
  GET    /faculty/{id}/leave/balance

  PERFORMANCE
  GET    /faculty/{id}/performance/score
  POST   /faculty/{id}/performance/review
  POST   /faculty/feedback/submit (student → anonymous)

  CPD
  POST   /faculty/cpd/submit
  PUT    /faculty/cpd/{id}/approve
  GET    /faculty/{id}/cpd/summary

  MISCONDUCT
  POST   /misconduct/complaint/file
  PUT    /misconduct/{id}/assign-panel
  PUT    /misconduct/{id}/outcome
  GET    /misconduct/{id}/status

  TRAINER (Platform Trainer APIs)
  GET    /trainer/{id}/reputation
  GET    /trainer/{id}/wallet
  POST   /trainer/payout/request
  POST   /trainer/coteach/invite
  PUT    /trainer/coteach/{id}/accept

  EXIT
  POST   /faculty/exit/initiate
  GET    /faculty/{id}/exit/checklist
  PUT    /faculty/exit/{id}/complete

API_RULES =
  - All APIs require JWT + tenant context header
  - Sensitive endpoints (termination, payout, misconduct outcome):
    require dual-token confirmation (two authorized principals)
  - No API returns raw PII without RBAC check
  - Every mutating API call emits a Kafka event
  - Rate limits enforced at Kong gateway per endpoint per role
```

---

## 2️⃣1️⃣ KAFKA EVENT REGISTRY (MANDATORY)

```
FACULTY_EVENT_SCHEMA =

  faculty.onboarded                 { faculty_id, tenant_id, role, type, timestamp }
  faculty.state.changed             { faculty_id, from_state, to_state, actor_id, reason }
  faculty.type.assigned             { faculty_id, previous_type, new_type, approved_by }
  faculty.subject.assigned          { faculty_id, subject_id, section_id, academic_year }
  faculty.workload.warned           { faculty_id, current_load, threshold, hod_id }
  faculty.workload.overload.blocked { faculty_id, requested_load, cap, timestamp }
  faculty.timetable.published       { section_id, published_by, effective_from }
  faculty.timetable.rescheduled     { slot_id, reason, new_time, notified_students }
  faculty.attendance.marked         { faculty_id, slot_id, mode, recorded_at }
  faculty.leave.requested           { faculty_id, type, from_date, to_date }
  faculty.leave.approved            { leave_id, approved_by, substitute_id }
  faculty.leave.lwp                 { faculty_id, days, period } → Billing Service
  faculty.performance.reviewed      { faculty_id, score, label, reviewed_by }
  faculty.feedback.submitted        { faculty_id, subject_id, score (aggregated only) }
  faculty.cpd.approved              { faculty_id, activity_type, credits_awarded }
  faculty.misconduct.flagged        { case_id, faculty_id, category, status }
  faculty.misconduct.resolved       { case_id, outcome, sealed_at }
  faculty.class.reassigned          { from_faculty_id, to_faculty_id, reason, section_id }
  faculty.exited                    { faculty_id, exit_type, exit_date }
  trainer.reputation.updated        { trainer_id, score, rank, computed_at }
  trainer.payout.processed          { trainer_id, amount, payout_date, receipt_ref }
  trainer.coteach.agreed            { course_id, primary_trainer_id, co_trainers_json }
  trainer.suspended                 { trainer_id, reason, suspended_by, timestamp }

EVENT_RULES =
  - Every event carries: tenant_id, correlation_id, schema_version
  - Events consumed by: Analytics Service, Notification Service, Billing Service
  - Retention: 90 days hot; 2 years cold archive
  - No PII in event payload (use reference IDs only)
  - Schema changes require Event Schema Registry bump (R49 validation)
```

---

## 2️⃣2️⃣ UI MODULE — FACULTY_MANAGEMENT_UI (LOCKED)

```
MODULE_NAME     = Faculty_Management_UI
PARENT_STACK    = Flutter (primary) | Next.js (SEO read-only clone per R31)
DESIGN_SYSTEM   = Ecoskiller shared design tokens (R33)
COLOR_PRIMARY   = #1E3A8A  |  ACCENT = #10B981

SCREEN INVENTORY (Role-Gated):

  FOR FACULTY / TRAINER:
  ├── My Dashboard (identity block + compliance badges + widgets)
  ├── My Profile (professional + platform blocks)
  ├── My Timetable (weekly/monthly view; export)
  ├── My Subjects (current + historical)
  ├── Mark Attendance (session-level; time-windowed)
  ├── Grade Entry (subject + assessment + student)
  ├── Leave Request (type + dates + substitute)
  ├── Leave History & Balance
  ├── CPD Submission + History
  ├── Performance Dashboard (own score; advisory)
  ├── Student Feedback Summary (aggregated only)
  └── [TRAINER ONLY]
      ├── Trainer Reputation Dashboard
      ├── Wallet & Earnings View
      ├── Payout Request Screen
      ├── Course Builder (→ Skill Engine bridge)
      ├── Co-Teaching Management
      └── Trainer Analytics Dashboard

  FOR HOD:
  ├── Department Faculty Matrix (all faculty + workload + state)
  ├── Workload Assignment Screen
  ├── Timetable Review & Approval
  ├── Leave Approval Queue
  ├── Performance Review Screen
  ├── Grade Override Request Review
  ├── CPD Approval Queue
  └── Faculty Misconduct Dashboard (read; complaint raised elsewhere)

  FOR DEAN / PRINCIPAL / ADMIN:
  ├── Institute Faculty Overview Dashboard
  ├── Overload Approval Queue
  ├── Cross-Department Assignment Approval
  ├── Suspension / Reinstatement Console
  ├── Termination Workflow Screen
  ├── Exit Checklist Oversight
  ├── Performance Analytics (institute-wide)
  └── Misconduct Case Management Console

UI_RULES =
  - MAX_SCREENS_PER_RUN = 15
  - MAX_MODULES_PER_RUN = 1
  - MAX_ROLES_PER_RUN   = 1
  - MAX_ENTITY_STATE    = 1
  - Forbidden actions: HIDDEN (not disabled) per master UI rules
  - Fixed dashboard areas: identity, role, compliance badges, alerts (40%)
  - Customizable: widgets, shortcuts, density (60%)
  - No cross-module UI mixing (Faculty_Management_UI ≠ Job_Portal_UI)
  - All screens: loading + empty + error states mandatory
  - Sensitive screens: screenshot blocking enforced (R16)
  - Accessibility: WCAG 2.1 AA on every screen (R20)
```

---

## 2️⃣3️⃣ ANTIGRAVITY EXECUTION COMMAND PROTOCOL

```
═══════════════════════════════════════════════════════════════════════
  HOW TO INVOKE FACULTY_MANAGEMENT_AGENT IN ANTIGRAVITY
═══════════════════════════════════════════════════════════════════════

STEP 1: This prompt is loaded ONCE per context. Never re-paste.
STEP 2: Issue sealed execution commands in this exact format:

────────────────────────────────────────────────────────────────────
EXECUTE_FACULTY_AGENT

INSTITUTE_TYPE  = [DEGREE_COLLEGE | UNIVERSITY | TRAINING_ACADEMY | ...]
FACULTY_TYPE    = [PERMANENT_FACULTY | VERIFIED_TRAINER | VISITING_FACULTY | ...]
ROLE            = [HOD | PROFESSOR | ASSISTANT_PROFESSOR | TRAINER | ADMIN | ...]
MODULE          = [WORKLOAD_ENGINE | TIMETABLE | LEAVE_MANAGEMENT |
                   PERFORMANCE | MISCONDUCT | TRAINER_REVENUE | EXIT_ENGINE | ...]
FEATURE         = [Workload_Assignment | Leave_Approval | Grade_Override |
                   Payout_Request | Co_Teaching_Setup | Reputation_Dashboard | ...]
ENTITY_STATE    = [ACTIVE | PROBATION | ON_LEAVE | SUSPENDED | NOTICE_PERIOD | ...]
STAGE           = [FOUNDATION_UI | INTELLIGENCE_UI | ECOSYSTEM_UI | COMPLIANCE_UI]
────────────────────────────────────────────────────────────────────

EXECUTION LIMITS (ANTIGRAVITY SAFETY):
  MAX_SCREENS_PER_RUN   = 15
  MAX_MODULES_PER_RUN   = 1
  MAX_ROLES_PER_RUN     = 1
  MAX_ENTITY_STATES     = 1
  Exceeding limits → STOP EXECUTION IMMEDIATELY. Report breach.

OUTPUT REQUIREMENTS (every generated artifact MUST include):
  ✔ Purpose declaration
  ✔ Target role + faculty type
  ✔ Module name
  ✔ Entity lifecycle state
  ✔ Fixed vs customizable UI areas
  ✔ Navigation entry & exit points
  ✔ Permissions required (RBAC + ABAC)
  ✔ State transitions visible in UI
  ✔ API endpoints consumed
  ✔ Kafka events emitted
  ✔ Loading / empty / error states
  ✔ Accessibility notes (WCAG 2.1 AA)

Partial output → INVALID. Halt and report.
```

---

## 2️⃣4️⃣ COMPLIANCE & GOVERNANCE INHERITANCE (DO NOT DUPLICATE)

```
THIS AGENT INHERITS (finalized in master prompt — never re-declare):
  ✔ RBAC + ABAC authorization (Keycloak + OPA)
  ✔ JWT + MFA enforcement
  ✔ Session management
  ✔ Multi-tenant hard isolation (PostgreSQL Row-Level Security)
  ✔ Domain isolation (Arts | Commerce | Science | Tech | Admin)
  ✔ Encryption at rest (PII + financial data)
  ✔ Audit log immutability (append-only, no delete)
  ✔ WCAG 2.1 AA accessibility
  ✔ RTL + i18n localization support
  ✔ Offline mode + sync rules
  ✔ GDPR-ready data handling (right to erasure: pseudonymisation for faculty)
  ✔ Screenshot blocking on sensitive screens
  ✔ Rate limiting (Kong gateway — per role, per endpoint, per IP)
  ✔ CI/CD pipeline (GitLab CI; no manual prod deploys)
  ✔ Prometheus + Loki + OpenTelemetry observability
  ✔ HashiCorp Vault (all secrets; zero hardcoded credentials)
  ✔ R28 intelligence cost optimization (rule engines first; ML second; LLM last)
  ✔ R29 screen-level UI generation standard
  ✔ R33 shared design system tokens
  ✔ R49 contract validator (all API + event schemas validated before build)
  ✔ R50 automated QA test generator

DUPLICATION OF ANY ABOVE = CONFLICT → DENY → STOP EXECUTION
```

---

## 🔐 FINAL SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║              FACULTY_MANAGEMENT_AGENT — FINAL STATUS                        ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  STATUS                      = SEALED & LOCKED                              ║
║  EXECUTION_ENGINE            = ANTIGRAVITY                                  ║
║  VERSION                     = 1.0.0                                        ║
║  MUTATION_POLICY             = ADD_ONLY (semver bump mandatory)             ║
║  CREATIVE_INTERPRETATION     = FORBIDDEN                                    ║
║  ASSUMPTION_FILLING          = FORBIDDEN                                    ║
║  DEFAULT_BEHAVIOR            = DENY                                         ║
║  FAILURE_MODE                = HARD_STOP → REPORT → NO PARTIAL OUTPUT       ║
║  ANTIGRAVITY_CONFUSION       = IMPOSSIBLE                                   ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  QUALITY_SCORE               = 10 / 10                                      ║
║  ENTERPRISE_SAFE             = ✔                                            ║
║  MULTI_TENANT_ISOLATED       = ✔                                            ║
║  DOMAIN_ISOLATED             = ✔                                            ║
║  AUDIT_IMMUTABLE             = ✔                                            ║
║  AI_ADVISORY_ONLY            = ✔                                            ║
║  DUAL_APPROVAL_ENFORCED      = ✔ (grade override, suspension, termination)  ║
║  FINANCIAL_AUDIT_TRAIL       = ✔ (trainer wallet + payout)                  ║
║  KAFKA_EVENTS_REGISTERED     = ✔ (24 events declared)                       ║
║  API_CONTRACTS_LOCKED        = ✔                                            ║
║  UI_SCREENS_ROLE_GATED       = ✔                                            ║
║  PARENT_AGENT_COMPLIANT      = ✔ (ACADEMIC_STRUCTURE_AGENT compatible)      ║
╠══════════════════════════════════════════════════════════════════════════════╣
║                                                                              ║
║   ANY DEVIATION FROM THIS PROMPT:                                           ║
║   ❌ STOP EXECUTION                                                          ║
║   ❌ LOG VIOLATION WITH FULL CONTEXT                                         ║
║   ❌ ESCALATE TO ARCHITECT                                                   ║
║   ❌ NO PARTIAL OUTPUT PERMITTED                                             ║
║                                                                              ║
║   ANY ADDITION TO THIS PROMPT:                                              ║
║   ✅ SEMVER BUMP REQUIRED (1.0.0 → 1.1.0 or 2.0.0)                         ║
║   ✅ ARCHITECT APPROVAL MANDATORY                                            ║
║   ✅ BACKWARD COMPATIBILITY VERIFIED                                         ║
║   ✅ R49 CONTRACT VALIDATOR RE-RUN REQUIRED                                  ║
╚══════════════════════════════════════════════════════════════════════════════╝
```
