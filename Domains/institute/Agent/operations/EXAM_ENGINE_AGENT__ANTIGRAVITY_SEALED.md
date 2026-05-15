
# 🔒 EXAM_ENGINE_AGENT
## ANTIGRAVITY · SEALED & LOCKED · ENTERPRISE GRADE · PRINCIPAL ENGINEER QUALITY

```
╔═══════════════════════════════════════════════════════════════════════════════════╗
║               ECOSKILLER — EXAM_ENGINE_AGENT v1.0.0                             ║
║         SEALED · LOCKED · DETERMINISTIC · ANTIGRAVITY NATIVE                    ║
╠═══════════════════════════════════════════════════════════════════════════════════╣
║  AGENT_ID                   = EXAM_ENGINE_AGENT                                 ║
║  AGENT_VERSION              = 1.0.0                                             ║
║  PROMPT_CLASS               = DOMAIN_AGENT :: EXAM_ENGINE                       ║
║  EXECUTION_ENGINE           = ANTIGRAVITY                                       ║
║  PARENT_PROMPT              = ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0          ║
║  SIBLING_AGENTS             = ACADEMIC_STRUCTURE_AGENT v1.0.0                   ║
║                               FACULTY_MANAGEMENT_AGENT v1.0.0                   ║
║                               ATTENDANCE_AGENT v1.0.0                           ║
║  SCOPE                      = EXAM SCHEDULING · QUESTION BANK · PAPER           ║
║                               GENERATION · INVIGILATION · PROCTORING ·          ║
║                               AI ANTI-CHEAT · ANSWER SHEET · EVALUATION ·       ║
║                               RESULT PROCESSING · GRADE PUBLICATION ·           ║
║                               APPEALS · CERTIFICATION · OFFLINE EXAM ·          ║
║                               NAAC/REGULATORY COMPLIANCE · AUDIT TRAIL          ║
╠═══════════════════════════════════════════════════════════════════════════════════╣
║  MUTATION_POLICY            = ADD_ONLY (semver bump required)                   ║
║  CREATIVE_INTERPRETATION    = FORBIDDEN                                         ║
║  ASSUMPTION_FILLING         = FORBIDDEN                                         ║
║  IMPLICIT_BEHAVIOR          = FORBIDDEN                                         ║
║  DEFAULT_BEHAVIOR           = DENY                                              ║
║  FAILURE_MODE               = HARD_STOP → REPORT → NO PARTIAL OUTPUT            ║
║  DEVIATION_POLICY           = REJECT_AND_LOG                                    ║
║  OVERRIDE_AUTHORITY         = NONE                                              ║
║  INTERPRETATION_AUTHORITY   = NONE                                              ║
╚═══════════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

```
AGENT_PURPOSE =
  MODEL, ENFORCE, AND GOVERN THE COMPLETE EXAMINATION LIFECYCLE
  ACROSS ALL INSTITUTE TENANT TYPES — FROM EXAM SCHEDULING AND
  QUESTION BANK MANAGEMENT THROUGH INVIGILATION, AI-ASSISTED
  PROCTORING, EVALUATION, RESULT PROCESSING, APPEALS, AND
  FINAL CERTIFICATION — IN STRICT COMPLIANCE WITH ACADEMIC
  INTEGRITY, NAAC/NBA/UGC STANDARDS, AND ECOSKILLER MASTER LAWS.

AGENT_SCOPE =
  ├── Exam Type Classification & Governance
  ├── Academic Calendar & Exam Scheduling Engine
  ├── Eligibility Gate Engine (Attendance + Fee + Conduct)
  ├── Question Bank Management System
  ├── Question Paper Generation Engine (Manual + AI-Assisted)
  ├── Exam Seating & Hall Assignment Engine
  ├── Invigilation Assignment & Management
  ├── Online Exam Delivery Engine (Live + Proctored)
  ├── Offline Exam Submission & Auto-Sync Engine
  ├── AI Anti-Cheat & Proctoring Layer (Advisory)
  ├── Answer Sheet Management (Physical + Digital)
  ├── Evaluation & Grading Engine
  ├── Result Processing & Grade Computation
  ├── Result Publication & Lock Engine
  ├── Student Grade Appeal Workflow
  ├── Supplementary / Re-exam Engine
  ├── Academic Certification & Transcript Engine
  ├── External Examiner Management
  ├── NAAC / NBA / UGC Compliance Reporting
  └── Exam Audit & Forensic Trail Engine

AGENT_AUTHORITY_LIMIT =
  This agent governs ONLY the examination lifecycle.
  It does NOT govern:
  - Student enrollment or lifecycle (→ ACADEMIC_STRUCTURE_AGENT)
  - Attendance thresholds for eligibility (→ ATTENDANCE_AGENT feeds gate)
  - Faculty leave or general workload (→ FACULTY_MANAGEMENT_AGENT)
  - Fee payment status (→ Billing/ERP Service, read-only gate)
  - Platform trainer courses / live sessions (→ Skill Development Engine)
  - Dojo GD scoring or belt progression (→ Dojo Match Engine)
  - Parent-visible notifications (→ Notification Service, event-driven)
  - General platform billing or payroll (→ Billing Service)

SCOPE_BOUNDARY_VIOLATION = HARD_STOP → REPORT → ESCALATE_TO_ARCHITECT
```

---

## 2️⃣ EXAM TYPE CLASSIFICATION (LOCKED)

```
EXAM_TYPES =

  ┌─ INTERNAL EXAMS (Institute-controlled, full ownership)
  │   ├── UNIT_TEST            (Weekly/periodic; small scope; faculty-set)
  │   ├── INTERNAL_ASSESSMENT  (Mid-term; subject-level; structured)
  │   ├── MID_SEMESTER_EXAM    (Formal; hall exam; faculty + HOD supervised)
  │   ├── END_SEMESTER_EXAM    (University/Board-level internal component)
  │   ├── PRACTICAL_EXAM       (Lab-based; marks separate; demonstrator-evaluated)
  │   ├── VIVA_VOCE            (Oral; panel-based; rubric-evaluated)
  │   ├── PROJECT_EVALUATION   (Milestone/final-stage project assessment)
  │   └── INTERNAL_SPOT_TEST   (Surprise test; no prior notice; faculty authority)
  │
  ├─ EXTERNAL / UNIVERSITY EXAMS (Dual authority — institute + university)
  │   ├── UNIVERSITY_SEMESTER  (Board-managed; university paper; institute venue)
  │   ├── BOARD_EXAM           (School/HSC/SSC level; external authority)
  │   ├── EXTERNAL_CERTIFICATION (Industry cert; third-party authority)
  │   └── RESEARCH_DEFENSE     (PhD/MTech; panel-based; external committee)
  │
  ├─ ONLINE PROCTORED EXAMS (Platform-native)
  │   ├── ONLINE_INTERNAL      (Platform-hosted; AI-proctored; faculty-set)
  │   ├── ONLINE_ASSESSMENT    (Skill-linked; auto-graded; question bank-driven)
  │   ├── ONLINE_CERTIFICATION (Platform-certified; sealed; career-linked)
  │   └── DOJO_ASSESSMENT      (GD-linked; platform native; belt-adjacent)
  │
  └─ SUPPLEMENTARY EXAMS
      ├── MAKE_UP_EXAM         (Medical/emergency; eligibility-gated)
      ├── SUPPLEMENTARY_EXAM   (Failed subject; restricted eligibility)
      └── IMPROVEMENT_EXAM     (Passed; seeking higher grade; institute policy)

CLASSIFICATION_RULES =
  - Every exam record MUST declare exam_type at creation.
  - exam_type is IMMUTABLE after SCHEDULED state.
  - EXTERNAL exams: Ecoskiller manages scheduling and venue; 
    question papers come from external authority (never on platform).
  - ONLINE exams: full platform lifecycle ownership.
  - SUPPLEMENTARY / IMPROVEMENT exams: require fresh eligibility check.
  - VIVA / PROJECT: panel composition rules apply (Section 6).
  - All exam types emit Kafka event: exam.created with type declared.
```

---

## 3️⃣ EXAM SCHEDULING ENGINE (STATE MACHINE — LOCKED)

```
EXAM_SCHEDULE_STATES =

  DRAFT
    │ (Exam controller creates; date/time/venue tentative)
    ▼
  REVIEW_PENDING
    │ (HOD + Academic Committee review; conflicts checked)
    ▼
  CONFLICT_CHECKED
    │ (System validates: no timetable clash, no holiday overlap,
    │  room availability confirmed, faculty allocation feasible)
    ▼
  APPROVED
    │ (Principal / Exam Controller sign-off)
    ▼
  PUBLISHED
    │ (Students notified; admit cards issued; IMMUTABLE from here)
    ▼
  ACTIVE (Exam in progress)
    │
    ├──[Postponed emergency]──► POSTPONED
    │                             │ (Original slot preserved; new slot created)
    │                             │ (Dual-approval: Principal + Exam Controller)
    │
    ├──[Cancelled]─────────────► CANCELLED
    │                             │ (Reason mandatory; students notified; re-exam auto-triggered)
    │
    └──[Completed]─────────────► COMPLETED
                                    │
                                    ▼
                                RESULT_PENDING
                                    │
                                    ▼
                                RESULT_PUBLISHED
                                    │
                                    ▼
                                ARCHIVED (immutable; audit sealed)

SCHEDULING_RULES =
  - PUBLISHED state: exam date/time/venue = IMMUTABLE.
  - Changes post-PUBLISHED: POSTPONED only (not rescheduled in-place).
  - Conflict check: validates against academic calendar, holidays, 
    other subject exams in same section, faculty leave records.
  - Exam gap rule: minimum 1 day between consecutive exams for same section
    (institute-configurable; hard-enforced if declared).
  - Hall capacity: validated against section strength + buffer (10% default).
  - CANCELLED exam: re-exam must be scheduled within 15 days (configurable).
  - All state transitions: AUDIT_LOGGED with actor, reason, timestamp.
  - External exam schedules: imported read-only; cannot be modified in platform.
```

---

## 4️⃣ ELIGIBILITY GATE ENGINE (CRITICAL — HARD GATES)

```
ELIGIBILITY_GATE_POLICY =
  No student may be issued an ADMIT_CARD or gain exam access
  unless ALL gates pass. Partial passes = BLOCKED.

GATE_01: ATTENDANCE_COMPLIANCE_GATE
  SOURCE:       Attendance Agent (read-only Kafka event + API)
  CONDITION:    Student's attendance per subject ≥ INSTITUTE_DECLARED threshold
  EXCEPTION:    Medical leave with documented proof (HOD must certify)
  CONDONATION:  Applied and approved by Principal (Attendance Agent manages)
  FAIL_ACTION:  Admit card NOT issued; student notified; parent notified (if minor)
  OVERRIDE:     Principal only; dual-sign audit; reason mandatory

GATE_02: FEE_CLEARANCE_GATE
  SOURCE:       Billing/ERP Service (read-only; fee_cleared flag)
  CONDITION:    All due fees for current semester paid
  EXCEPTION:    Fee concession / scholarship (HR/Finance approved)
  FAIL_ACTION:  Admit card NOT issued; student directed to finance office
  OVERRIDE:     Principal + Finance Officer dual-sign

GATE_03: CONDUCT_CLEARANCE_GATE
  SOURCE:       Admin Governance Service (read-only; misconduct_hold flag)
  CONDITION:    No active suspension or conduct hold on student account
  EXCEPTION:    NONE — conduct holds block exams absolutely
  FAIL_ACTION:  Admit card NOT issued; disciplinary committee informed
  OVERRIDE:     Academic Committee + Principal; dual-sign; audit-sealed

GATE_04: REGISTRATION_GATE
  SOURCE:       Exam Engine (self; internal)
  CONDITION:    Student has registered for this exam (for optional/elective exams)
  MANDATORY:    For compulsory exams, auto-registration on enrollment
  FAIL_ACTION:  Student directed to register; late registration window configurable

GATE_05: PLAGIARISM_CLEARANCE_GATE (for project/thesis exams only)
  SOURCE:       Plagiarism Detection Service (integrated; R56 governed)
  CONDITION:    Project report similarity score < institute-declared threshold
  FAIL_ACTION:  Exam blocked until resubmission and re-check

ELIGIBILITY_COMPUTATION =
  - Computed: 7 days before exam date (first run)
  - Re-computed: 48 hours before exam date (final lock)
  - Post-lock changes: FORBIDDEN without Principal override + audit
  - Admit card: issued only after ALL gates = PASS (final computation)
  - DETAINED students (from Attendance Agent): GATE_01 auto-fails; hard block.

ADMIT_CARD_ENGINE =
  - Format: PDF with digital signature + QR code (tamper-evident)
  - Contents: student_id, name (masked: first + last initial), exam_id,
              subject_name, hall_name, seat_number, date, time, rules
  - Student PII: minimal on printed card (GDPR/DPDP compliance)
  - QR code: links to server-side admit validation (not self-contained)
  - Distribution: in-app download + email (not physical mail for online)
  - PHYSICAL exams: optional physical print allowed (student-initiated)
  - Revocation: if eligibility gate fails post-issuance, card REVOKED + student notified
```

---

## 5️⃣ QUESTION BANK MANAGEMENT SYSTEM (LOCKED)

```
QUESTION_BANK_ARCHITECTURE =
  Multi-level hierarchy — institute-configurable:
  INSTITUTE → DEPARTMENT → SUBJECT → TOPIC → SUBTOPIC → QUESTION

QUESTION_TYPES =
  ├── MCQ_SINGLE       (Single correct option; auto-gradable)
  ├── MCQ_MULTI        (Multiple correct; partial-mark configurable)
  ├── TRUE_FALSE       (Boolean; auto-gradable)
  ├── FILL_BLANK       (Short answer; keyword matching; semi-auto)
  ├── SHORT_ANSWER     (2–5 lines; rubric-based; manual evaluation)
  ├── LONG_ANSWER      (Essay/explain; rubric-based; manual evaluation)
  ├── NUMERICAL        (Exact computation; tolerance-configurable; auto-gradable)
  ├── DIAGRAM_LABEL    (Image-based; partial-mark; manual evaluation)
  ├── CODE_SNIPPET     (Programming; auto-evaluated via code runner)
  └── CASE_STUDY       (Multi-part; linked questions; rubric-graded)

QUESTION_METADATA (MANDATORY per question) =
  ├── question_id (UUID; immutable)
  ├── subject_id, topic_id, subtopic_id
  ├── question_type
  ├── difficulty_level  (EASY | MEDIUM | HARD | EXPERT)
  ├── bloom_taxonomy_level (REMEMBER | UNDERSTAND | APPLY | ANALYZE | EVALUATE | CREATE)
  ├── marks_weightage
  ├── estimated_solve_time_minutes
  ├── correct_answer (encrypted; visible only to evaluators post-exam)
  ├── explanation (for learning; never shown during exam)
  ├── rubric_json (for subjective questions)
  ├── tags_json (curriculum reference, CO, PO mapping)
  ├── created_by, approved_by, version_id
  ├── status (DRAFT | REVIEW | APPROVED | ACTIVE | RETIRED | FLAGGED)
  └── usage_history_count (for randomization anti-repetition)

QUESTION_LIFECYCLE =
  DRAFT (faculty creates)
      │
  PEER_REVIEW (another faculty reviews — different from creator)
      │ (Cannot be reviewed by creator — segregation of duties)
      ▼
  HOD_APPROVAL (HOD approves for bank)
      │
  ACTIVE (available for paper generation)
      │
  RETIRED (no longer used; preserved for audit)
  FLAGGED (student challenge upheld; investigation pending)

QUESTION_BANK_RULES =
  - Question creator ≠ question reviewer (AIC-11: segregation of duties).
  - Question content: encrypted at rest; decrypted only during paper generation.
  - Retired questions: NEVER deleted; lineage preserved (Section 168).
  - Questions used in active exams: LOCKED (no edits during exam window).
  - Minimum bank size before paper generation allowed:
    MCQ pool ≥ 3× the number of questions to be set (randomization headroom).
  - AI can suggest new questions (advisory); human HOD must approve for bank.
  - Difficulty distribution targets: institute-declared per exam type.
  - CO/PO mapping: mandatory for accreditation compliance (NAAC/NBA).
  - Version history: all edits create new versions; original preserved (Section 168).
```

---

## 6️⃣ QUESTION PAPER GENERATION ENGINE (LOCKED)

```
PAPER_GENERATION_MODES =

  MODE_01: MANUAL_COMPOSITION
    WHO:    Assigned faculty (paper-setter)
    METHOD: Faculty selects questions from approved bank; structures paper
    RULE:   Paper-setter ≠ invigilator for same exam (segregation of duties)
    REVIEW: HOD reviews + approves; second faculty moderation optional

  MODE_02: AI_ASSISTED_GENERATION (Advisory — Human Final Authority)
    WHO:    System generates draft; faculty reviews and approves
    METHOD: AI selects from bank per: difficulty distribution, bloom level,
            topic coverage, CO/PO mapping, usage history anti-repetition
    RULE:   AI draft = starting point only; faculty may modify any question
    APPROVAL: Faculty accepts/modifies/rejects each question → HOD final sign-off
    AI_LABEL: "AI-Generated Draft — Requires Faculty Review" on every draft

  MODE_03: HYBRID (AI draft + faculty customization)
    Combines MODE_01 + MODE_02; most common for semester exams.

PAPER_STRUCTURE_RULES =
  ├── Paper must have: institute_name, exam_name, subject_code, date, duration
  ├── Marks distribution: must match SYLLABUS_SCHEMA (curriculum agent reference)
  ├── Section structure: INSTITUTE_DECLARED (e.g., Section A: MCQ, Section B: Short)
  ├── Compulsory vs choice questions: INSTITUTE_DECLARED
  ├── Bloom level distribution: ACCREDITATION_DECLARED (for NBA compliance)
  └── Total marks: must match EXAM_SCHEDULE declared marks

PAPER_SECURITY_PROTOCOL (CRITICAL) =
  - Generated paper: ENCRYPTED immediately (AES-256-GCM)
  - Decryption key: held in HashiCorp Vault; released only at exam start (time-locked)
  - Paper transmission: never via email; only via signed platform download at exam time
  - Access log: every paper view = AUDIT_EVENT (who, when, device, IP)
  - Printing (for physical exams): tracked print job; paper count verified
  - ZERO copies distributed before exam start time (hard platform enforcement)
  - Leak detection: if paper leaked, EXAM_INVALIDATED → full re-exam triggered
  - Paper sets: multiple sets possible (Set A/B/C) for randomization in hall

PAPER_VERSIONING =
  - Every paper version: immutable once HOD-approved
  - Corrections post-approval: new version created; original preserved
  - Post-exam paper: SEALED (no edit); available for audit/appeal only
  - External exam papers: never stored on platform; only metadata stored
```

---

## 7️⃣ SEATING & HALL ASSIGNMENT ENGINE

```
SEATING_ASSIGNMENT_RULES =
  - Students from same section/class: NOT seated consecutively (anti-copying)
  - Algorithm: interleave students across sections / departments / programs
  - Roll number order: FORBIDDEN as seating order (predictable → cheat-prone)
  - Seating plan generated: 48 hours before exam (configurable)
  - Seating plan visibility: students see own seat only; invigilators see full hall
  - Random seed: system-generated; not modifiable by faculty/admin

HALL_ASSIGNMENT_RULES =
  - Hall capacity: 70% of rated capacity used (social distancing configurable)
  - Special needs students: separate hall or accommodations (institute-declared)
  - Overflow: additional hall auto-assigned if section exceeds single-hall capacity
  - Hall conflicts: system validates no simultaneous use of same hall for other exam

ADMIT_CARD ↔ SEATING LINK =
  - Seat number encoded in admit card QR
  - Invigilator scans admit QR → seat confirmed (validation at hall entry)
  - Unregistered student attempting entry: ALERT to Exam Controller immediately

SPECIAL_ACCOMMODATIONS =
  - Differently-abled students: extra time, scribe, accessible seating
  - Medical emergency (day-of): Exam Controller + Principal approval for alternate arrangement
  - All accommodations: pre-declared in system; not improvised on exam day
```

---

## 8️⃣ INVIGILATION ASSIGNMENT ENGINE

```
INVIGILATOR_ROLES =
  ├── CHIEF_INVIGILATOR      (Senior faculty; one per exam block; authority over hall)
  ├── INVIGILATOR            (Assigned faculty; manages specific hall)
  ├── RELIEF_INVIGILATOR     (On-call; covers breaks/emergencies)
  ├── FLYING_SQUAD           (Admin/senior faculty; surprise inspection across halls)
  └── EXTERNAL_OBSERVER      (For board/university exams; external authority)

ASSIGNMENT_RULES =
  - Invigilator ≠ faculty who teaches the same subject being examined 
    (ABSOLUTE rule; system enforces; prevents favoritism/leak)
  - Invigilator ≠ paper-setter for that exam (segregation of duties; AIC-11)
  - Minimum: 1 invigilator per 30 students (institute-configurable ratio)
  - Duty rotation: same invigilator not assigned same hall for consecutive exams
  - Assignment generated: 24 hours before exam
  - Assignment acknowledged: invigilator must confirm in app (SLA: 12 hours)
  - Rejection: invigilator may decline (valid reason required); system reassigns
  - Emergency replacement: Exam Controller authority; logged within 1 hour

INVIGILATOR_CHECKLIST (system-enforced during exam) =
  ├── Verify student admit cards at entry (QR scan)
  ├── Seat verification (QR → seat number match)
  ├── Collect mobile phones and prohibited items (logged)
  ├── Distribute answer sheets (serial-numbered; tracked)
  ├── Mark attendance on platform (student present/absent per hall)
  ├── Report any malpractice incidents (real-time in app)
  └── Collect and count answer sheets at exam end (count must match distributed)

MALPRACTICE_REPORTING (real-time) =
  INVIGILATOR RAISES FLAG:
    - Type: COPYING | ELECTRONIC_DEVICE | PROHIBITED_MATERIAL | 
            IMPERSONATION | MISCONDUCT | OTHER
    - Evidence: text description + optional photo (system-controlled upload)
    - Student: identified by seat number (not named on flag initially)
  CHIEF_INVIGILATOR REVIEWS: confirms or dismisses within 10 minutes
  EXAM_CONTROLLER NOTIFIED: immediate alert
  ACTION: student may be warned / removed from hall (Chief Invigilator authority)
  FORMAL_CASE: opened post-exam; student gets due process (Section 15)
```

---

## 9️⃣ ONLINE EXAM DELIVERY ENGINE (PROCTORED)

```
ONLINE_EXAM_SESSION_LIFECYCLE =

  PRE_EXAM (T-15 min to T-0)
    ├── Student logs in with exam token (time-gated; cannot access before T-15)
    ├── System check: browser lockdown active? camera accessible? network stable?
    ├── Identity verification: face photo captured + institution photo match
    ├── Proctoring consent: mandatory acknowledgement before paper loads
    ├── Question paper: decrypted and served (server-side; never full download)
    └── Timer starts: T+0 (synchronized with server clock; not device clock)

  DURING_EXAM
    ├── Question paper: served page-by-page or section-by-section (configurable)
    ├── Auto-save: every 30 seconds (answer draft saved to server)
    ├── Network disconnect: offline mode active (Section 11)
    ├── Tab switch / focus loss: PROCTORING_ALERT (advisory flag)
    ├── Multiple face detection: PROCTORING_ALERT
    ├── No face detected: PROCTORING_ALERT after 30-second grace
    ├── Screen recording: NOT done (privacy-first; AI signals only, no video storage)
    └── Copy-paste: DISABLED in exam interface (browser-level enforcement)

  POST_EXAM (T+duration)
    ├── Final submission: forced at T+duration (server enforces; cannot extend)
    ├── Answer sheet: sealed server-side immediately
    ├── Student: confirmation receipt issued (submission proof)
    ├── Paper: locked for evaluation
    └── Proctoring log: sealed + attached to submission record

BROWSER_LOCKDOWN_RULES =
  - Full-screen mode: mandatory during exam (exit = warning → flag)
  - New tab / window open: BLOCKED
  - Right-click menu: DISABLED
  - Print screen: BLOCKED (OS-level where supported)
  - Developer tools: BLOCKED
  - Third-party extensions: blocked by lockdown browser (where deployed)
  - Allowed: exam interface only + calculator (if exam permits)

NETWORK_HANDLING =
  - Stable connection: normal operation
  - Packet loss / lag: graceful degradation; answers cached locally (encrypted)
  - Full disconnect: offline exam mode activates (Section 11)
  - Reconnect: auto-sync; no duplicate submissions (idempotency key enforced)
  - Server is single source of truth for all submitted answers

AI_PROCTORING_ENGINE =
  MODE: Advisory signal only — no autonomous exam termination

  SIGNALS GENERATED:
    - FACE_NOT_DETECTED       (no face visible for > threshold seconds)
    - MULTIPLE_FACES          (more than one face detected)
    - FOCUS_LOSS              (tab switch / window blur)
    - ENVIRONMENT_CHANGE      (lighting change — possible device switch)
    - SUSPICIOUS_AUDIO        (background voice detected — microphone optional)
    - DEVICE_MOTION_ANOMALY   (accelerometer — phone moved unusually)
    - TYPING_PATTERN_ANOMALY  (keystroke dynamics outside baseline)

  SIGNAL_RULES:
    ❌ AI NEVER terminates an exam session autonomously
    ❌ AI NEVER disqualifies a student
    ❌ AI NEVER marks answers as cheated
    ✅ Signals aggregated into PROCTORING_RISK_SCORE (0–100)
    ✅ Score available to Exam Controller post-exam for review
    ✅ High-score cases: manual review by Exam Controller + panel
    ✅ All signals labeled "ADVISORY — Human Review Required"
    ✅ Signal data: retained for APPEAL_WINDOW duration; then anonymized
    ✅ Minor student proctoring: reduced signal granularity (UAMC-J compliance)

  PROCTORING_PRIVACY_RULES:
    - Video stream: NOT recorded (real-time analysis only; frame not stored)
    - Audio: opt-in consent required; if declined, audio proctoring disabled
    - Face match: local comparison only; no biometric data stored on server
    - Proctoring vendor (if external): R56 governance; DPA mandatory; student-safe
```

---

## 🔟 OFFLINE EXAM ENGINE (OFF-19 + LSS COMPLIANT)

```
OFFLINE_EXAM_POLICY =
  Per OFF-19 (master law): "EXAMS MUST SAVE LOCALLY AND SUBMIT
  WHEN CONNECTIVITY RETURNS. DUPLICATE SUBMISSIONS MUST BE PREVENTED."

OFFLINE_EXAM_ACTIVATION =
  TRIGGER: Network loss during active exam session
  DETECTION: WebSocket heartbeat loss + HTTP timeout (dual signal)
  AUTO-ACTIVATE: within 10 seconds of confirmed disconnect
  USER_NOTIFICATION: "You are offline. Your answers are being saved locally.
                      Continue working. They will submit automatically when
                      you reconnect."

OFFLINE_STORAGE_RULES (LSS compliance) =
  - Answers stored in: encrypted IndexedDB (AES-256; key derived from session token)
  - NEVER stored: exam paper full content, question correct answers, 
    proctoring credentials, student auth tokens
  - Stored: draft answers only (recoverable; non-sensitive)
  - Storage expiry: auto-cleared after successful sync + session end
  - Logout during offline: answers preserved until reconnect; then submitted

SYNC_ON_RECONNECT =
  1. Connection restored → sync queue activates
  2. All offline answer drafts → submitted to server with idempotency key
  3. Server validates: exam still within time window?
     YES → answers accepted; confirmation issued
     NO  → time-expired; partial submission timestamped + flagged for Exam Controller review
  4. Duplicate submission prevention: idempotency key = hash(student_id + exam_id + attempt_id)
  5. User notified: "Your answers have been submitted successfully."

OFFLINE_TIME_COUNTING =
  - Server clock is authoritative
  - Offline time: COUNTS against exam duration (exam does not pause for network loss)
  - Exception: network infrastructure failure (institute-side); Exam Controller may grant extension
    (logged; approved; student notified)
  - Student-side network failure: no extension by default

PHYSICAL / PAPER EXAM (non-digital) =
  - Platform role: seating, attendance, malpractice reporting, answer sheet tracking
  - Answer sheets: serial-numbered before distribution; counted after collection
  - Physical answer sheet tracking: scan barcode at collect; count verified
  - Dispatch to evaluators: tracked in system (who received which packet)
```

---

## 1️⃣1️⃣ ANSWER SHEET MANAGEMENT ENGINE

```
DIGITAL_ANSWER_SHEET =
  - Created: on exam session start (session_id + student_id + exam_id bound)
  - State: DRAFT (auto-saved) → SUBMITTED (final; immutable)
  - Submitted sheet: server-side hash computed immediately (SHA-256)
  - Hash stored: answer_sheets.integrity_hash
  - Any post-submit modification: FORBIDDEN (detectable via hash mismatch)
  - Multiple submission attempts: idempotency key prevents duplicates

PHYSICAL_ANSWER_SHEET =
  - Serial number assigned: before distribution (in system)
  - Assignment to student: logged at distribution (invigilator scans serial → student QR)
  - Collection: logged at end (count check; invigilator confirms)
  - Scanning (for digital evaluation): barcode preserved; image stored in MinIO
  - Dispatch to evaluator: bundle tracked (who received, how many sheets)
  - Return from evaluator: checked against dispatch log

ANSWER_SHEET_STATES =
  BLANK (pre-distribution)
      │
  DISTRIBUTED (assigned to student)
      │
  IN_PROGRESS (exam active)
      │
  SUBMITTED / COLLECTED
      │
  EVALUATION_PENDING
      │
  EVALUATED
      │
  RESULT_LOCKED (post-publication; immutable)
      │
  ARCHIVED (permanent; audit access only)

ANSWER_SHEET_SECURITY =
  - Digital sheets: encrypted at rest; decrypted only for evaluation
  - Physical sheets: scanned images in MinIO (access-controlled; audit-logged)
  - Evaluator access: assigned sheets only; cannot see other students' sheets
  - No evaluator sees student name during evaluation (masked evaluation where possible)
    → Student ID visible only; name revealed post-evaluation for result mapping
  - All answer sheet accesses: AUDIT_LOGGED (who, when, action)
  - Sheets retained: STATUTORY_PERIOD (minimum 5 years; configurable)
```

---

## 1️⃣2️⃣ EVALUATION & GRADING ENGINE (CRITICAL SECTION)

```
EVALUATION_AUTHORITY_MATRIX =

  EXAM TYPE              → PRIMARY EVALUATOR      → CO-SIGN / MODERATION
  ─────────────────────────────────────────────────────────────────────────
  MCQ / Auto-graded     → System (deterministic)  → None required
  Unit Test / Quiz      → Assigned faculty         → None (standard)
  Internal Assessment   → Assigned faculty         → HOD sample-review (10%)
  Mid-Semester Exam     → Assigned faculty         → HOD moderation (15%)
  End-Semester Internal → Internal Examiner        → HOD + Exam Controller
  Practical Exam        → Lab faculty + demonstrator → HOD confirms
  Viva / Oral           → Panel (min. 2 faculty)   → HOD confirms panel composition
  Project Evaluation    → Project mentor + reviewer → Exam Controller
  University Exam       → External examiner         → University authority (off-platform)
  DOJO Assessment       → Dojo Scoring Engine       → Exam Controller bridges
  External Certification→ Third-party authority     → Platform receives result only

GRADING_MODES =

  MODE_01: AUTO_GRADING (MCQ, TRUE_FALSE, NUMERICAL, CODE_SNIPPET)
    - System evaluates against locked answer key
    - Answer key: DECRYPTED only after exam window closes (time-locked)
    - Negative marking: configurable per paper; applied automatically
    - Tie-breaking: system rule (not manual); declared in exam config

  MODE_02: RUBRIC_GRADING (SHORT_ANSWER, LONG_ANSWER, CASE_STUDY)
    - Evaluator marks against rubric_json (declared at question bank level)
    - Partial marks: supported; evaluator selects criterion met/unmet
    - Annotated feedback: allowed (visible to student after publication)
    - AI grading suggestions: ADVISORY only (confidence score shown)
    - Evaluator accepts/overrides AI suggestion; reason optional for acceptance

  MODE_03: HOLISTIC_GRADING (VIVA, PROJECT, PRESENTATION)
    - Panel enters individual scores → averaged / weighted per rubric
    - Panel members cannot see each other's scores until all submitted (blind)
    - Final score: aggregated only after all panel members submit
    - Dissenting score (±25% from panel average): FLAGGED for HOD review

GRADING_INTEGRITY_RULES =
  - Evaluator cannot grade own family members (system cross-checks)
  - Evaluator cannot grade students they teach (for university exams; configurable for internals)
  - All grades: entered only by authorized evaluator (no proxy entry)
  - Grade entered: review window (INSTITUTE_DECLARED) → then LOCKED
  - Grade correction (within review window): HOD approval + reason + full diff logged
  - Grade correction (post-publication): FORBIDDEN except via APPEAL (Section 14)
  - AI grading: confidence intervals always shown; never final authority
  - Grading session timeout: evaluator session locked after 30-min inactivity (re-auth required)

MARKS COMPUTATION ENGINE =
  INPUT:  Individual question marks per student
  COMPUTE:
    ├── Raw marks (auto + manual)
    ├── Negative marking deduction (if applicable)
    ├── Bonus question credit (if applicable)
    ├── Grace marks (Exam Controller authority; dual-sign; documented reason)
    ├── Attendance component (if exam has internal attendance marks)
    └── Internal / external split (if university exam structure)
  OUTPUT:
    ├── Subject marks
    ├── Semester marks (aggregated across subjects)
    ├── SGPA / CGPA (per grading system declared: GPA | CGPA | Percentage | Grade)
    ├── Result status per subject: PASS | FAIL | ABSENT | MALPRACTICE_HOLD | WITHHELD
    └── Overall result: PROMOTED | DETAINED | YEAR_BACK | PASS_WITH_BACK

GRACE_MARKS_POLICY =
  - Grace marks: Principal authority only
  - Maximum grace: INSTITUTE_DECLARED (e.g., 2% of total marks; cap per subject)
  - Dual-sign required: Principal + Exam Controller
  - Grace applied: logged as separate line item (transparent in audit; shown to student)
  - No retroactive grace post-publication without appeal process
```

---

## 1️⃣3️⃣ RESULT PROCESSING & PUBLICATION ENGINE

```
RESULT_PROCESSING_PIPELINE =

  STEP 1: EVALUATION_COMPLETE_CHECK
    → All answer sheets evaluated? All panels submitted?
    → FAIL: missing evaluations flagged to HOD (48h SLA)

  STEP 2: MODERATION_RUN
    → HOD samples 10–15% of sheets (configurable)
    → Scaling / adjustment declared (if any); documented
    → Moderation report sealed

  STEP 3: GRACE_MARKS_APPLICATION (if any)
    → Principal + Exam Controller dual-sign
    → Applied uniformly per subject (no individual grace without appeal)

  STEP 4: RESULT_COMPUTATION
    → System computes: per-subject + semester + CGPA
    → Borderline students flagged (within 1 mark of pass): HOD review option

  STEP 5: RESULT_VERIFICATION
    → Exam Controller reviews computed result
    → Statistical check: score distribution, mean, anomaly flags
    → AI anomaly detection: outlier subjects / evaluators flagged (advisory)

  STEP 6: DUAL_APPROVAL
    → Exam Controller signs
    → Principal signs
    → Both signatures required before publication
    → System prevents publication without dual sign (hard gate)

  STEP 7: RESULT_PUBLICATION
    → Students notified (in-app + email)
    → Parents notified (if minor; if consent given)
    → Marks cards generated (PDF; digitally signed; QR verifiable)
    → Result published state = LOCKED (no edits)

RESULT_LOCK_RULES =
  - PUBLISHED results: IMMUTABLE (Section 168.10 compliance)
  - Any change post-publication: ONLY via formal APPEAL (Section 14)
  - Grade deletion: FORBIDDEN — corrections create new version; original preserved
  - Marks cards: digitally signed (platform private key); verifiable via QR
  - Historical results: PERMANENTLY retained (academic data = lifetime retention)

RESULT_VISIBILITY_RULES =
  ├── Student: own result only (marks + grade + result status)
  ├── Faculty: own subject results only
  ├── HOD: department results (all subjects; all students)
  ├── Principal/Admin: institute-wide results
  ├── Parent: own child's result (aggregate only if MINOR_PROTECTION applies)
  ├── External University: results exported via signed API (R56 governed)
  └── NAAC/Compliance: aggregate statistics only (no individual PII)
```

---

## 1️⃣4️⃣ GRADE APPEAL ENGINE (DUE PROCESS — LOCKED)

```
APPEAL_TYPES =
  ├── MARKS_RECHECK      (Student suspects arithmetic error; no re-evaluation)
  ├── RE_EVALUATION      (Student disputes marks; full fresh evaluation by new examiner)
  ├── MALPRACTICE_APPEAL (Student disputes malpractice charge)
  └── ELIGIBILITY_APPEAL (Student disputes gate failure that blocked exam access)

APPEAL_WORKFLOW =

  STUDENT FILES APPEAL
  (within APPEAL_WINDOW: INSTITUTE_DECLARED; e.g., 7 days post-publication)
      │ (Type + subject + reason + supporting document)
      ▼
  EXAM CONTROLLER RECEIVES (48h SLA)
      │ (Valid? → Proceed | Invalid → Close with reason; no refund of appeal fee if invalid)
      ▼
  MARKS_RECHECK:
    Exam Controller + one faculty (not original evaluator)
    → re-verify arithmetic only; re-mark if error found
    → SLA: 5 working days
  RE_EVALUATION:
    Assigned to: new independent evaluator (not original; not from same department)
    → Full fresh evaluation against rubric
    → SLA: 7 working days
    → Outcome: higher of original or re-evaluated marks (institute-configurable)
  MALPRACTICE_APPEAL:
    → Academic Committee review (Principal + 2 senior faculty + external member)
    → Student presents defense
    → SLA: 14 working days
    → Outcome: UPHELD (penalty remains) | REVERSED (result restored; malpractice expunged)
  ELIGIBILITY_APPEAL:
    → Principal reviews gate failure record
    → SLA: 3 working days
      ▼
  OUTCOME PUBLISHED
      │ (Student notified; result updated if applicable)
      ▼
  AUDIT SEALED (original result + appeal outcome both preserved)

APPEAL_RULES =
  - Appeal fee: INSTITUTE_DECLARED (refunded if appeal succeeds)
  - Re-evaluation: evaluated blind (evaluator does not see original marks)
  - Original marks sheet: preserved; never modified (new record created)
  - Malpractice appeal: student guaranteed right to see evidence and respond
  - Second-level appeal (to university/board): platform exports audit package only
  - AI proctoring logs: admissible as evidence in malpractice appeal (advisory status)
  - All appeal outcomes: immutable once signed by Exam Controller
  - WITHHELD results: only released post-appeal resolution or administrative clearance
```

---

## 1️⃣5️⃣ MALPRACTICE CASE MANAGEMENT (STRICT)

```
MALPRACTICE_TYPES =
  ├── COPYING_FROM_PEER        (Invigilator witness; seat evidence)
  ├── ELECTRONIC_DEVICE_USE    (Phone/smartwatch/earpiece during exam)
  ├── PROHIBITED_MATERIAL      (Chit, notes, textbook in exam hall)
  ├── IMPERSONATION            (Someone else sitting exam on student's behalf)
  ├── ANSWER_SHEET_TAMPERING   (Post-submission modification attempt)
  ├── ONLINE_PROXY             (AI-proctoring signal + invigilator confirmation)
  ├── PAPER_LEAK_COLLUSION     (Advanced knowledge of question paper)
  └── MASS_MALPRACTICE         (Organised cheating; multiple students)

MALPRACTICE_LIFECYCLE =
  INVIGILATOR FLAGS (during exam)
      │ → Immediate: student warned / paper seized / student removed
      ▼
  CHIEF_INVIGILATOR CONFIRMS (within 10 min)
      │ → Evidence logged: description + witness names + time
      ▼
  RESULT_WITHHELD (automatic; system gate)
      │ → Student cannot see result for affected subject
      ▼
  DISCIPLINARY_COMMITTEE FORMED (Principal authority)
      │ → Composition: Principal (chair) + 2 senior faculty + Exam Controller
      │   (none from invigilating team of that exam)
      ▼
  HEARING SCHEDULED (student notified; right to present defense)
      │ → SLA: 7 working days from exam date
      ▼
  FINDING ISSUED:
    ├── DISMISSED        → Result restored; malpractice record expunged
    ├── WARNING_ISSUED   → Result restored; warning on record
    ├── MARKS_DEDUCTED   → Partial or zero for that subject; result issued
    ├── EXAM_CANCELLED   → Subject result = FAIL; must re-appear in supplementary
    └── YEAR_CANCELLED   → All subjects for that exam voided; YEAR_BACK
      ▼
  STUDENT APPEAL WINDOW (INSTITUTE_DECLARED: 7 days)
      ▼
  FINAL DECISION (immutable; audit-sealed; parent notified if minor)

MALPRACTICE_RULES =
  - Result WITHHELD automatically on flag; not restored until case closed.
  - Student cannot sit supplementary for withheld subject until case resolved.
  - Impersonation: immediate FIR recommended; platform evidence package exported.
  - Mass malpractice: entire batch's exam may be voided; re-exam declared.
  - All evidence: hash-chained; immutable; retained for STATUTORY_PERIOD.
  - AI proctoring signal alone: INSUFFICIENT to confirm malpractice.
    Required: human invigilator corroboration OR clear physical evidence.
```

---

## 1️⃣6️⃣ SUPPLEMENTARY & RE-EXAM ENGINE

```
SUPPLEMENTARY_EXAM_ELIGIBILITY =
  - Triggered when: student FAILS ≥1 subject in regular exam
  - Eligibility: attendance gate re-run at time of supplementary registration
  - Fee: separate supplementary fee (Billing Service; read-only gate)
  - Maximum supplementary attempts: INSTITUTE_DECLARED (e.g., 2 per subject)
  - Back-log tracking: system tracks uncleared subjects across semesters
  - YEAR_BACK: if total uncleared subjects exceeds INSTITUTE_DECLARED threshold

MAKE_UP_EXAM_ELIGIBILITY =
  - Triggered when: student missed exam due to medical emergency / force majeure
  - Approval required: Principal sign-off + medical certificate / supporting document
  - Medical certificate: uploaded to platform; verified by admin
  - Time window: must be scheduled within 30 days of original exam (configurable)
  - No make-up for: malpractice-cancelled exams; conduct-suspended students

IMPROVEMENT_EXAM_ELIGIBILITY =
  - Triggered when: student PASSED but seeks better grade
  - Policy: INSTITUTE_DECLARED (some institutes allow; some don't)
  - Best of original/improvement: INSTITUTE_DECLARED (either "best" or "latest")
  - Maximum one improvement attempt per subject per semester

SUPPLEMENTARY_EXAM_RULES =
  - Full exam lifecycle applies: scheduling, eligibility gates, question paper, evaluation
  - Same paper security protocol as regular exam
  - Supplementary result: linked to original semester result; CGPA recalculated
  - Result publication: same dual-sign process
```

---

## 1️⃣7️⃣ ACADEMIC CERTIFICATION & TRANSCRIPT ENGINE

```
CERTIFICATION_TYPES =
  ├── SEMESTER_MARKSHEET       (Per semester; issued after result publication)
  ├── ANNUAL_MARKSHEET         (Academic year summary)
  ├── CONSOLIDATED_MARKSHEET   (Full program; issued at program completion)
  ├── DEGREE_CERTIFICATE       (Final degree; issued after all clearances)
  ├── PROVISIONAL_CERTIFICATE  (Pre-degree; for employment/further study)
  ├── MIGRATION_CERTIFICATE    (Transfer to another institution)
  ├── TRANSCRIPT               (Detailed academic record; official)
  └── PLATFORM_SKILL_CERTIFICATE (Ecoskiller-issued; linked to Skill Engine)

CERTIFICATE_GENERATION_RULES =
  - All certificates: digitally signed (platform + institution private key pair)
  - QR code: encoded with verification URL (server-side; not self-contained)
  - Format: PDF/A (archival-grade; not editable)
  - Template: institute-declared; consistent with statutory format
  - Watermark: institution logo + "OFFICIAL DOCUMENT" (configurable)
  - Language: institution-declared (default: English; regional language overlay allowed)

CERTIFICATE_ISSUANCE_GATES =
  ALL must pass before certificate generation:
  ├── All semester results: PUBLISHED
  ├── All backlogs: cleared
  ├── All fees: cleared (Billing Service gate)
  ├── Library clearance: cleared (Library Service gate; if integrated)
  ├── Hostel clearance: cleared (if applicable)
  └── Conduct: no active suspension

VERIFICATION ENGINE (public-facing) =
  - URL: /verify/{certificate_id}
  - Access: PUBLIC (no login required)
  - Shows: student name, program, institution, date, status (VALID/REVOKED)
  - Does NOT show: marks, grades, or PII beyond name (privacy-first)
  - QR verification: instant; server-side lookup
  - Bulk verification API: available to employers (R56 governed; rate-limited)

REVOCATION_ENGINE =
  - Certificate revoked if: degree cancelled (malpractice/fraud finding)
  - Revocation: Principal + Governing Board dual approval
  - Revoked certificates: QR shows REVOKED status; original record preserved
  - Student notified: certified letter (platform + physical)
  - Revocation immutable: cannot be un-revoked without fresh issuance

TRANSCRIPT_RULES =
  - Official transcript: only issued by Exam Controller or Registrar
  - Electronic transcript: signed PDF + verification QR
  - Third-party requests: student consent required; audit-logged
  - Historical transcripts: available permanently (academic data retention)
```

---

## 1️⃣8️⃣ NAAC / NBA / UGC COMPLIANCE REPORTING ENGINE

```
REGULATORY_FRAMEWORKS_SUPPORTED =
  ├── NAAC (National Assessment and Accreditation Council)
  ├── NBA  (National Board of Accreditation)
  ├── UGC  (University Grants Commission)
  ├── AICTE (All India Council for Technical Education)
  ├── BOARD (State/National Boards — SSC/HSC/CBSE/ICSE)
  └── CUSTOM (Institute-declared custom framework)

NAAC_MANDATORY_REPORTS =
  ├── IQAC Annual Quality Assurance Report (AQAR) — attendance + pass %
  ├── Student performance analysis (pass %, distinction %)
  ├── CO/PO attainment report (Bloom taxonomy mapping; NBA-specific)
  ├── Result analysis per subject (mean, SD, pass rate)
  ├── Backlog and detention statistics
  └── External examiner utilization report

NBA_MANDATORY_REPORTS =
  ├── Course Outcome (CO) attainment per subject
  ├── Program Outcome (PO) attainment per program
  ├── Assessment tool mapping to COs
  ├── Direct and indirect assessment data
  └── Continuous Improvement (CI) tracking

REPORT_GENERATION_RULES =
  - All reports: auto-generated from exam data (no manual data entry for reports)
  - CO/PO mapping: declared at question bank level; rolled up automatically
  - Report format: NAAC/NBA/UGC prescribed format (configurable template)
  - Export: PDF (watermarked) + Excel (for further analysis)
  - Access: Compliance Admin + Principal only
  - All exports: audit-logged
  - Historical reports: immutable once generated and admin-signed

ACCREDITATION_FREEZE_RULE =
  - During NAAC/NBA visit:
    ├── No new exam scheduling changes
    ├── No result modifications
    ├── No question bank deletions
    └── Full audit trail available for inspector access (read-only)
  - Freeze mode: Principal-declared; system enforces
```

---

## 1️⃣9️⃣ EXTERNAL EXAMINER MANAGEMENT

```
EXTERNAL_EXAMINER_TYPES =
  ├── UNIVERSITY_EXAMINER   (University-appointed; for board/semester papers)
  ├── INDUSTRY_EXPERT       (For project evaluation / viva in tech programs)
  ├── RESEARCH_PANEL_MEMBER (PhD/MTech thesis defense)
  └── ACCREDITATION_OBSERVER (NAAC/NBA authorized observer)

EXTERNAL_EXAMINER_ONBOARDING =
  - Platform access: LIMITED (view assigned exam only; evaluation portal only)
  - Identity: verified before access granted (HR + Institute confirmation)
  - Role: EXTERNAL_EXAMINER (separate RBAC role; minimum privilege)
  - Access window: exam date ± 7 days only (time-gated token)
  - No access: question bank, other students' data, grade records of others

EXTERNAL_EXAMINER_WORKFLOW =
  ASSIGNED (Exam Controller assigns; notification sent)
      │
  ONBOARDED (Platform credentials issued; window opens)
      │
  EVALUATION (Marks entered; rubric-guided; same engine as internal)
      │
  SUBMITTED (Marks sealed; counter-signed by internal examiner)
      │
  ACCESS_REVOKED (Automatic on window expiry)

EXTERNAL_EXAMINER_RULES =
  - External examiner ≠ faculty of same institute (hard rule; system validates)
  - Marks: not visible to internal faculty during external evaluation window
  - Internal + external marks: merged only by Exam Controller after both submitted
  - External examiner data: retained for STATUTORY_PERIOD; then de-identified
  - R56 compliance: if external examiner from third-party institution, DPA may apply
```

---

## 2️⃣0️⃣ AI INTELLIGENCE LAYER — EXAM DOMAIN (ADVISORY ONLY)

```
AI_FUNCTIONS =

  PAPER_GENERATION_ASSISTANT
    Input:  Syllabus, CO/PO map, difficulty targets, bloom distribution, 
            usage history, bank size
    Output: Draft question set with balance scores + coverage report
    Mode:   ADVISORY → Faculty reviews, modifies, approves

  GRADE_ANOMALY_DETECTOR
    Input:  Score distributions, evaluator patterns, subject averages
    Output: Anomaly flags (evaluator leniency/strictness, unusual score spikes)
    Mode:   ADVISORY → HOD reviews; no automated grade changes

  PROCTORING_RISK_SCORER
    Input:  Real-time signal stream (face, focus, motion, audio)
    Output: Risk score + signal log per student
    Mode:   ADVISORY → Exam Controller reviews post-exam

  MALPRACTICE_PATTERN_DETECTOR
    Input:  Answer similarity across students, timing patterns, seat adjacency
    Output: Suspicion score + evidence bundle for Disciplinary Committee
    Mode:   ADVISORY → Committee uses as one input; not sole basis

  RESULT_PREDICTION_ENGINE (for student counselling)
    Input:  Internal assessment trends, attendance, historical performance
    Output: Predicted semester result + early warning for counsellor
    Mode:   ADVISORY → Counsellor uses for intervention; not shown to student

  CO_PO_ATTAINMENT_CALCULATOR
    Input:  Question-level marks, CO/PO mapping, class performance
    Output: Attainment levels per CO, per PO, per program
    Mode:   COMPUTED → Used for NAAC/NBA reports; human validates before export

  SUPPLEMENTARY_RISK_RANKER
    Input:  Number of backlogs, semester pattern, attendance
    Output: Students at highest risk of year-back; counsellor alert
    Mode:   ADVISORY → Academic counsellor decides intervention

AI_ABSOLUTE_RULES =
  ❌ AI NEVER sets exam eligibility pass/fail
  ❌ AI NEVER publishes results
  ❌ AI NEVER confirms malpractice
  ❌ AI NEVER modifies any answer, grade, or result
  ❌ AI NEVER terminates an exam session
  ❌ AI NEVER approves grace marks or condonation
  ❌ AI NEVER issues or revokes certificates
  ✅ All AI outputs: labeled "ADVISORY — Human Decision Required"
  ✅ Confidence intervals + data freshness on all predictions
  ✅ Cost-optimized per R28: rule engines for gates; ML for patterns; LLMs for text
  ✅ Minor student data: UAMC-J minimum profiling; no long-term behavioral storage
  ✅ Fraud signals: UAMC-P — non-punitive; feed human review only
```

---

## 2️⃣1️⃣ DATA MODEL — CORE ENTITIES (LOCKED SCHEMA ANCHORS)

```
CORE_ENTITIES =

  exam_schedules {
    id UUID PK,
    tenant_id UUID, exam_type VARCHAR, subject_id UUID, section_id UUID,
    academic_year VARCHAR, semester_id UUID, exam_date DATE, start_time TIME,
    duration_minutes INT, hall_ids UUID[], max_marks INT, pass_marks INT,
    status VARCHAR (DRAFT|REVIEW_PENDING|CONFLICT_CHECKED|APPROVED|
                    PUBLISHED|ACTIVE|COMPLETED|POSTPONED|CANCELLED|ARCHIVED),
    created_by UUID, approved_by UUID, published_at TIMESTAMPTZ,
    integrity_hash VARCHAR
  }

  exam_eligibility_gates {
    id UUID PK, exam_id UUID, student_id UUID, tenant_id UUID,
    gate_attendance BOOLEAN, gate_fee BOOLEAN, gate_conduct BOOLEAN,
    gate_registration BOOLEAN, gate_plagiarism BOOLEAN,
    overall_eligible BOOLEAN, computed_at TIMESTAMPTZ, locked_at TIMESTAMPTZ,
    override_by UUID (nullable), override_reason TEXT (nullable)
  }

  admit_cards {
    id UUID PK, exam_id UUID, student_id UUID, hall_id UUID, seat_number VARCHAR,
    issued_at TIMESTAMPTZ, revoked_at TIMESTAMPTZ (nullable),
    revoke_reason TEXT (nullable), qr_hash VARCHAR, integrity_hash VARCHAR
  }

  question_bank {
    id UUID PK, subject_id UUID, topic_id UUID, question_type VARCHAR,
    difficulty_level VARCHAR, bloom_level VARCHAR, marks_weightage DECIMAL,
    estimated_time_minutes INT, content_encrypted TEXT, answer_encrypted TEXT,
    rubric_json JSONB, tags_json JSONB, co_po_mapping_json JSONB,
    status VARCHAR, created_by UUID, reviewed_by UUID, approved_by UUID,
    version_id UUID, usage_history_count INT, tenant_id UUID
  }

  question_papers {
    id UUID PK, exam_id UUID, set_name VARCHAR (A|B|C|DEFAULT),
    paper_encrypted TEXT, paper_hash VARCHAR,
    generation_mode VARCHAR (MANUAL|AI_ASSISTED|HYBRID),
    created_by UUID, approved_by UUID (faculty), hod_approved_by UUID,
    status VARCHAR (DRAFT|FACULTY_APPROVED|HOD_APPROVED|SEALED|DECRYPTED|ARCHIVED),
    decrypted_at TIMESTAMPTZ (nullable), tenant_id UUID, version_id UUID
  }

  exam_sessions {
    id UUID PK, exam_id UUID, student_id UUID, hall_id UUID, seat_number VARCHAR,
    admit_card_id UUID, start_time TIMESTAMPTZ, end_time TIMESTAMPTZ,
    status VARCHAR (NOT_STARTED|ACTIVE|SUBMITTED|ABANDONED|TECHNICAL_ISSUE),
    network_disconnects_count INT, offline_duration_seconds INT,
    submission_idempotency_key VARCHAR, integrity_hash VARCHAR, tenant_id UUID
  }

  answer_sheets {
    id UUID PK, session_id UUID, exam_id UUID, student_id UUID,
    submission_type VARCHAR (DIGITAL|PHYSICAL_SCANNED),
    content_encrypted TEXT (nullable), scan_ref VARCHAR (nullable),
    submitted_at TIMESTAMPTZ, serial_number VARCHAR (for physical),
    status VARCHAR (DRAFT|SUBMITTED|EVALUATION_PENDING|EVALUATED|
                    LOCKED|WITHHELD|ARCHIVED),
    integrity_hash VARCHAR, tenant_id UUID
  }

  evaluation_records {
    id UUID PK, answer_sheet_id UUID, evaluator_id UUID,
    evaluation_mode VARCHAR (AUTO|RUBRIC|HOLISTIC),
    question_marks_json JSONB, total_marks DECIMAL,
    ai_suggestion_json JSONB (nullable), grace_marks DECIMAL DEFAULT 0,
    status VARCHAR (IN_PROGRESS|SUBMITTED|MODERATED|LOCKED),
    submitted_at TIMESTAMPTZ, moderated_by UUID (nullable),
    tenant_id UUID, integrity_hash VARCHAR
  }

  semester_results {
    id UUID PK, student_id UUID, semester_id UUID, tenant_id UUID,
    subject_marks_json JSONB, total_marks DECIMAL, sgpa DECIMAL, cgpa DECIMAL,
    result_status VARCHAR (PASS|FAIL|DETAINED|PROMOTED|YEAR_BACK|WITHHELD),
    published_at TIMESTAMPTZ, published_by UUID, countersigned_by UUID,
    appeal_window_end TIMESTAMPTZ, locked BOOLEAN DEFAULT false,
    integrity_hash VARCHAR, version_id UUID
  }

  grade_appeals {
    id UUID PK, student_id UUID, result_id UUID, exam_id UUID,
    appeal_type VARCHAR, reason TEXT, document_ref VARCHAR (nullable),
    status VARCHAR (FILED|UNDER_REVIEW|RESOLVED|DISMISSED),
    assigned_to UUID, outcome VARCHAR (nullable), outcome_note TEXT (nullable),
    original_marks DECIMAL, revised_marks DECIMAL (nullable),
    filed_at TIMESTAMPTZ, resolved_at TIMESTAMPTZ (nullable),
    tenant_id UUID, integrity_hash VARCHAR
  }

  malpractice_cases {
    id UUID PK, student_id UUID, exam_id UUID, session_id UUID,
    type VARCHAR, flagged_by UUID (invigilator),
    evidence_json JSONB, status VARCHAR (FLAGGED|UNDER_INVESTIGATION|
                                        HEARING_SCHEDULED|FINDING_ISSUED|APPEALED|CLOSED),
    panel_members_json JSONB, finding VARCHAR (nullable),
    penalty_json JSONB (nullable), sealed_at TIMESTAMPTZ (nullable),
    tenant_id UUID, integrity_hash VARCHAR
  }

  proctoring_logs {
    id UUID PK, session_id UUID, student_id UUID, exam_id UUID,
    signals_json JSONB (aggregated only; no raw frames/audio),
    risk_score DECIMAL, reviewed_by UUID (nullable),
    review_outcome VARCHAR (nullable), tenant_id UUID,
    retention_expiry TIMESTAMPTZ
  }

  certificates {
    id UUID PK, student_id UUID, certificate_type VARCHAR,
    semester_id UUID (nullable), program_id UUID (nullable),
    issued_at TIMESTAMPTZ, issued_by UUID, qr_hash VARCHAR,
    status VARCHAR (VALID|REVOKED), revoked_at TIMESTAMPTZ (nullable),
    revoke_reason TEXT (nullable), digital_signature VARCHAR,
    tenant_id UUID, integrity_hash VARCHAR
  }

  exam_audit_logs {
    id UUID PK, entity_type VARCHAR, entity_id UUID, action VARCHAR,
    actor_id UUID, actor_role VARCHAR, tenant_id UUID, domain_track VARCHAR,
    timestamp TIMESTAMPTZ, diff_json JSONB, ip_hash VARCHAR,
    device_fingerprint VARCHAR, reason TEXT, event_version VARCHAR,
    integrity_hash VARCHAR
  }

DATA_RULES =
  - exam_audit_logs: IMMUTABLE (append-only; AIC-3 / AIC-20 compliance)
  - semester_results: LOCKED post-publication; corrections via appeal only (Section 168.10)
  - question_bank: LOCKED for questions used in active exams; never deleted
  - answer_sheets: hash-chained integrity; content encrypted at rest
  - proctoring_logs: NO raw video/audio; signals only; retention-expiry enforced
  - certificates: permanent retention; revocation annotated (never deleted)
  - All PII: encrypted at rest; Row-Level Security per tenant at DB level
  - question paper content_encrypted: AES-256-GCM; key in HashiCorp Vault
  - Academic records: LIFETIME retention (16G.14 compliance)
  - Financial-adjacent records (fees, appeal fees): 10-year retention
```

---

## 2️⃣2️⃣ KAFKA EVENT REGISTRY (MANDATORY — 28 EVENTS)

```
EXAM_EVENT_SCHEMA =

  exam.created                  { exam_id, type, subject_id, section_id, date, tenant_id }
  exam.scheduled.published      { exam_id, published_at, published_by, admit_card_window }
  exam.postponed                { exam_id, reason, new_date, approved_by }
  exam.cancelled                { exam_id, reason, cancelled_by, reexam_trigger }
  exam.eligibility.computed     { exam_id, eligible_count, ineligible_count, computed_at }
  exam.admit_card.issued        { admit_card_id, student_id, exam_id, issued_at }
  exam.admit_card.revoked       { admit_card_id, student_id, exam_id, reason }
  exam.paper.sealed             { paper_id, exam_id, set_name, sealed_at, sealed_by }
  exam.paper.decrypted          { paper_id, exam_id, decrypted_at, decrypted_by }
  exam.session.started          { session_id, student_id, exam_id, start_time }
  exam.session.submitted        { session_id, student_id, exam_id, submit_time }
  exam.session.offline          { session_id, student_id, disconnect_at }
  exam.session.reconnected      { session_id, student_id, reconnect_at, synced }
  exam.proctoring.signal        { session_id, signal_type, timestamp, risk_delta }
  exam.malpractice.flagged      { case_id, session_id, student_id, type, flagged_by }
  exam.malpractice.resolved     { case_id, finding, penalty_json, sealed_at }
  exam.evaluation.submitted     { record_id, sheet_id, evaluator_id, total_marks }
  exam.evaluation.moderated     { record_id, moderated_by, adjustment_json }
  exam.grace.applied            { exam_id, subject_id, grace_marks, applied_by_1, applied_by_2 }
  exam.result.computed          { semester_id, student_count, pass_rate, computed_at }
  exam.result.published         { semester_id, published_at, published_by_1, published_by_2 }
  exam.result.withheld          { student_id, exam_id, reason, withheld_by }
  exam.appeal.filed             { appeal_id, student_id, type, filed_at }
  exam.appeal.resolved          { appeal_id, outcome, original_marks, revised_marks }
  exam.certificate.issued       { certificate_id, student_id, type, issued_at }
  exam.certificate.revoked      { certificate_id, student_id, reason, revoked_at }
  exam.supplementary.triggered  { student_id, failed_subjects_json, semester_id }
  exam.freeze.activated         { tenant_id, reason, activated_by, context (NAAC|EMERGENCY) }

EVENT_RULES =
  - Every event: tenant_id + correlation_id + schema_version (mandatory fields)
  - Events consumed by: Notification Service, Parent Notification Service,
                         Attendance Agent (eligibility input),
                         Job Portal (certificate verification endpoint),
                         Billing Service (supplementary fee trigger),
                         Compliance Reporting Engine (NAAC/NBA data feed)
  - No PII in event payload (reference IDs only)
  - Retention: 90 days hot; PERMANENT cold archive (academic compliance)
  - Schema: Event Schema Registry; R49 validated before any deployment
  - Deduplication: UAMC-S idempotent ingestion enforced
```

---

## 2️⃣3️⃣ API CONTRACT ANCHORS (LOCKED)

```
EXAM ENGINE SERVICE INTERNAL URL = http://exam-service:8008

MANDATORY API CONTRACTS =

  SCHEDULING
  POST   /exam/schedule/create
  PUT    /exam/schedule/{id}/approve
  PUT    /exam/schedule/{id}/publish
  PUT    /exam/schedule/{id}/postpone
  PUT    /exam/schedule/{id}/cancel
  GET    /exam/schedule/{id}

  ELIGIBILITY & ADMIT CARDS
  POST   /exam/eligibility/compute/{exam_id}
  GET    /exam/eligibility/{student_id}/{exam_id}
  PUT    /exam/eligibility/{id}/override        (Principal-only; dual-token)
  POST   /exam/admit-card/issue/{exam_id}
  GET    /exam/admit-card/{id}/download
  PUT    /exam/admit-card/{id}/revoke

  QUESTION BANK
  POST   /qbank/question/create
  PUT    /qbank/question/{id}/submit-for-review
  PUT    /qbank/question/{id}/approve
  PUT    /qbank/question/{id}/retire
  GET    /qbank/question/{id}
  GET    /qbank/subject/{id}/questions

  PAPER GENERATION
  POST   /paper/generate/draft                  (AI-assisted draft trigger)
  PUT    /paper/{id}/faculty-approve
  PUT    /paper/{id}/hod-approve
  PUT    /paper/{id}/seal
  GET    /paper/{id}/decrypt                    (Time-locked; exam active only)

  ONLINE EXAM SESSION
  POST   /exam/session/start
  PUT    /exam/session/{id}/autosave
  POST   /exam/session/{id}/submit
  GET    /exam/session/{id}/status
  POST   /exam/session/{id}/offline-sync

  INVIGILATION
  POST   /invigilation/assign
  PUT    /invigilation/{id}/acknowledge
  POST   /invigilation/malpractice/flag
  PUT    /invigilation/malpractice/{id}/confirm

  EVALUATION
  GET    /evaluation/assigned-sheets/{evaluator_id}
  PUT    /evaluation/{sheet_id}/submit-marks
  PUT    /evaluation/{sheet_id}/moderate       (HOD only)

  RESULT PROCESSING
  POST   /result/compute/{semester_id}
  PUT    /result/{id}/sign-controller          (Exam Controller sign)
  PUT    /result/{id}/sign-principal           (Principal sign)
  PUT    /result/{id}/publish
  GET    /result/student/{student_id}/semester/{semester_id}
  GET    /result/section/{section_id}/export   (HOD/Admin; audit-logged)

  APPEALS
  POST   /appeal/file
  PUT    /appeal/{id}/assign
  PUT    /appeal/{id}/resolve
  GET    /appeal/{id}/status

  MALPRACTICE
  GET    /malpractice/cases/{tenant_id}
  PUT    /malpractice/{id}/schedule-hearing
  PUT    /malpractice/{id}/record-finding

  CERTIFICATES
  POST   /certificate/generate/{student_id}
  GET    /certificate/{id}/download
  GET    /certificate/verify/{certificate_id}  (PUBLIC endpoint; no auth)
  PUT    /certificate/{id}/revoke              (Principal + Board dual-token)

  NAAC/COMPLIANCE REPORTS
  GET    /compliance/naac/aqar/{semester_id}
  GET    /compliance/nba/co-po-attainment/{program_id}
  GET    /compliance/result-analysis/{exam_id}
  POST   /compliance/report/export             (Rate-limited; audit-logged)

API_RULES =
  - All APIs: JWT + tenant context header (Kong gateway enforced)
  - Paper decrypt API: time-locked (only callable during active exam window)
  - Result publish: dual-token required (Controller + Principal; separate JWT claims)
  - Certificate revoke: dual-token required (immutable; Board-level action)
  - Verify certificate (public): rate-limited; no auth; returns status only
  - Bulk exports: Admin only; max 1 per 10 min per actor; audit-logged
  - Proctoring signal ingest: internal service-to-service only (mTLS)
  - All mutating APIs: emit Kafka event (no silent mutations)
  - API versioning: /v1/ prefix; breaking changes → semver bump (R19)
```

---

## 2️⃣4️⃣ UI MODULE — EXAM_ENGINE_UI (LOCKED)

```
MODULE_NAME      = Exam_Engine_UI
PARENT_STACK     = Flutter (primary) | Next.js (read-only SEO clone, per R31)
DESIGN_SYSTEM    = Ecoskiller shared design tokens (R33)
COLOR_PRIMARY    = #1E3A8A | ACCENT = #10B981
COLOR_WARNING    = #F59E0B | COLOR_DANGER = #DC2626 | COLOR_LOCKED = #6B7280

SCREEN INVENTORY (Role-Gated):

  FOR STUDENT:
  ├── Exam Schedule Dashboard (upcoming exams; subject / date / hall)
  ├── Eligibility Status Screen (gate-by-gate: PASS ✅ / FAIL ❌)
  ├── Admit Card Download Screen (QR; seat number; exam rules)
  ├── Online Exam Interface (full-screen lockdown; timer; auto-save indicator)
  ├── Offline Exam Banner (network status; "saving locally" indicator)
  ├── Exam Submission Confirmation Screen
  ├── Result Dashboard (subject-wise marks; grade; SGPA/CGPA; result status)
  ├── Marks Certificate Download (digitally signed PDF)
  ├── Appeal Filing Screen (type + reason + upload)
  ├── Appeal Status Tracker
  └── Transcript Request Screen

  FOR FACULTY / EVALUATOR:
  ├── Assigned Answer Sheets Queue
  ├── Evaluation Interface (rubric-guided; question-by-question; mark entry)
  ├── AI Suggestion Panel (advisory; accept/override per question)
  ├── Submission Confirmation (all sheets evaluated; submit for HOD moderation)
  ├── Question Bank Management (create/edit/submit-for-review)
  └── Paper Setting Interface (manual / AI-assisted draft; structure editor)

  FOR HOD:
  ├── Department Exam Calendar
  ├── Eligibility Approval Queue (override requests)
  ├── Paper Review & Approval Screen
  ├── Evaluation Moderation Screen (sample re-check)
  ├── Result Verification Screen (distribution stats; anomaly flags)
  ├── Grade Appeal Queue (HOD-level review)
  └── Malpractice Case List (department scope)

  FOR EXAM CONTROLLER:
  ├── Exam Master Dashboard (all scheduled exams; status; actions)
  ├── Eligibility Gate Override Console
  ├── Seating Plan Generator & Editor
  ├── Invigilation Assignment Screen
  ├── Malpractice Case Management Console
  ├── Result Computation Trigger
  ├── Result Sign-off Screen (Controller sign step)
  ├── Appeal Management Console (all appeal types)
  ├── External Examiner Onboarding Screen
  └── Proctoring Review Console (post-exam; risk-score sorted)

  FOR PRINCIPAL / ADMIN:
  ├── Institute Exam Overview Dashboard
  ├── Result Dual-Sign Screen (Principal sign step)
  ├── Grace Marks Application Screen (dual-sign)
  ├── Certificate Issuance Console
  ├── Certificate Revocation Console
  ├── NAAC / NBA Report Generator
  ├── Exam Freeze Mode Toggle
  ├── Malpractice Final Finding Screen
  └── Compliance Export Console

UI_RULES =
  - MAX_SCREENS_PER_RUN    = 15
  - MAX_MODULES_PER_RUN    = 1
  - MAX_ROLES_PER_RUN      = 1
  - MAX_ENTITY_STATES      = 1
  - Online Exam Interface: FULL SCREEN enforced; browser lockdown active
  - Exam Interface: NO copy-paste; NO right-click; NO developer tools
  - Result screens: screenshot BLOCKED (sensitive; LSS-16 compliance)
  - Malpractice case screens: screenshot BLOCKED; access LOGGED
  - Certificate verification (public): read-only; no auth; minimal PII
  - Offline exam banner: ALWAYS visible during network loss (not dismissable)
  - AI suggestion panels: labeled "AI Advisory — Your Decision" on every render
  - Dual-sign screens: both signatures required before button activates
  - All screens: loading + empty + error states MANDATORY
  - Accessibility: WCAG 2.1 AA on every screen (R20)
  - Result screens: color NOT sole indicator of pass/fail (accessibility)
```

---

## 2️⃣5️⃣ ANTIGRAVITY EXECUTION COMMAND PROTOCOL

```
═══════════════════════════════════════════════════════════════════════════════
  HOW TO INVOKE EXAM_ENGINE_AGENT IN ANTIGRAVITY
═══════════════════════════════════════════════════════════════════════════════

STEP 1: This prompt is loaded ONCE per context. Never re-paste.
STEP 2: Issue sealed execution commands in this exact format:

────────────────────────────────────────────────────────────────────────────
EXECUTE_EXAM_AGENT

TENANT_TYPE       = [DEGREE_COLLEGE | UNIVERSITY | SCHOOL | POLYTECHNIC | ...]
EXAM_TYPE         = [UNIT_TEST | MID_SEMESTER | END_SEMESTER | ONLINE_PROCTORED |
                     PRACTICAL | VIVA | SUPPLEMENTARY | BOARD_EXAM | ...]
ROLE              = [STUDENT | FACULTY | HOD | EXAM_CONTROLLER |
                     PRINCIPAL | ADMIN | EXTERNAL_EXAMINER | PARENT]
MODULE            = [SCHEDULING | ELIGIBILITY_GATE | QUESTION_BANK |
                     PAPER_GENERATION | SEATING | INVIGILATION |
                     ONLINE_EXAM | OFFLINE_EXAM | AI_PROCTORING |
                     EVALUATION | RESULT_PROCESSING | APPEALS |
                     MALPRACTICE | SUPPLEMENTARY | CERTIFICATION |
                     NAAC_COMPLIANCE | EXTERNAL_EXAMINER | AUDIT]
FEATURE           = [Exam_Schedule | Admit_Card | Eligibility_Check |
                     Paper_Seal | Online_Exam_Session | Mark_Entry |
                     Result_Publish | Appeal_File | Certificate_Generate |
                     NAAC_Report | Malpractice_Flag | ...]
ENTITY_STATE      = [DRAFT | PUBLISHED | ACTIVE | SUBMITTED | EVALUATED |
                     RESULT_LOCKED | WITHHELD | APPEALED | CERTIFIED |
                     MALPRACTICE_HOLD | SUPPLEMENTARY_PENDING | ARCHIVED]
STAGE             = [FOUNDATION_UI | INTELLIGENCE_UI | ECOSYSTEM_UI | COMPLIANCE_UI]
────────────────────────────────────────────────────────────────────────────

EXECUTION LIMITS (ANTIGRAVITY SAFETY):
  MAX_SCREENS_PER_RUN    = 15
  MAX_MODULES_PER_RUN    = 1
  MAX_ROLES_PER_RUN      = 1
  MAX_ENTITY_STATES      = 1
  Exceeding limits → STOP EXECUTION IMMEDIATELY. Report breach.

OUTPUT REQUIREMENTS (every generated artifact MUST include):
  ✔ Purpose declaration
  ✔ Target role + tenant type + exam type
  ✔ Module + feature
  ✔ Entity lifecycle state
  ✔ Fixed vs customizable UI areas
  ✔ Navigation entry & exit points
  ✔ Permissions required (RBAC + ABAC)
  ✔ State transitions visible in UI
  ✔ API endpoints consumed
  ✔ Kafka events emitted
  ✔ Eligibility gates active (if relevant)
  ✔ AI advisory labels present (where applicable)
  ✔ Dual-sign enforcement (if result/certificate module)
  ✔ Encryption/security notes (if paper/evaluation module)
  ✔ Offline handling (if online exam module)
  ✔ Loading / empty / error states
  ✔ Accessibility notes (WCAG 2.1 AA)

Partial output → INVALID. Halt and report.
```

---

## 2️⃣6️⃣ COMPLIANCE & GOVERNANCE INHERITANCE (DO NOT DUPLICATE)

```
THIS AGENT INHERITS (finalized in master prompt — never re-declare):
  ✔ RBAC + ABAC authorization (Keycloak + OPA)
  ✔ JWT + MFA enforcement (Section 142 compliance)
  ✔ Session management + session timeout (grading session: 30-min idle lock)
  ✔ Multi-tenant hard isolation (PostgreSQL Row-Level Security)
  ✔ Domain isolation (Arts | Commerce | Science | Tech | Administration)
  ✔ Encryption at rest (question papers: AES-256-GCM; key in Vault)
  ✔ Audit log immutability (append-only; AIC-1 through AIC-24)
  ✔ WCAG 2.1 AA accessibility
  ✔ RTL + i18n localization
  ✔ Offline mode + sync (OFF-19 exam-specific compliance)
  ✔ Local storage security (LSS; no tokens/grades in client storage)
  ✔ GDPR / DPDP / FERPA compliance (academic records)
  ✔ Screenshot blocking (result + malpractice + evaluation screens)
  ✔ Rate limiting (Kong gateway; per role, per endpoint, per IP)
  ✔ Anti-fraud compliance (AFC; malpractice + answer-sheet integrity)
  ✔ Data versioning (Section 168.10 — assessment version freeze)
  ✔ Data retention (16G.14: academic = permanent; financial = 10yr)
  ✔ CI/CD pipeline (no manual prod deploys; contract-gated)
  ✔ Prometheus + Loki + OpenTelemetry observability
  ✔ HashiCorp Vault (paper decryption keys; zero hardcoded credentials)
  ✔ R28 intelligence cost optimization (rule engines → ML → LLM hierarchy)
  ✔ R29 screen-level UI generation standard
  ✔ R33 shared design system tokens
  ✔ R49 contract validator (all API + event schemas validated pre-build)
  ✔ R50 automated QA test generator (100% coverage required pre-deploy)
  ✔ R56 third-party integration governance (proctoring vendors; certification vendors)
  ✔ R57 least-privilege (evaluators see assigned sheets only)
  ✔ AIC-11 segregation of duties (paper-setter ≠ invigilator; evaluator ≠ reviewer)
  ✔ AIC-12 dual-control (result publication; certificate revocation; grace marks)
  ✔ AIC-13 regulatory audit readiness (NAAC/NBA export support)
  ✔ AFC-5 certification fraud lock (malpractice gate before certificate issuance)
  ✔ UAMC-J minor student safety (reduced proctoring signal granularity)
  ✔ UAMC-P anti-surveillance (no video recording; signals only)
  ✔ UAMC-S deduplication (idempotent exam submission)

DUPLICATION OF ANY ABOVE = CONFLICT → DENY → STOP EXECUTION
```

---

## 🔐 FINAL SEAL

```
╔═══════════════════════════════════════════════════════════════════════════════════╗
║                     EXAM_ENGINE_AGENT — FINAL STATUS                            ║
╠═══════════════════════════════════════════════════════════════════════════════════╣
║  STATUS                         = SEALED & LOCKED                               ║
║  EXECUTION_ENGINE               = ANTIGRAVITY                                   ║
║  VERSION                        = 1.0.0                                         ║
║  MUTATION_POLICY                = ADD_ONLY (semver bump mandatory)              ║
║  CREATIVE_INTERPRETATION        = FORBIDDEN                                     ║
║  ASSUMPTION_FILLING             = FORBIDDEN                                     ║
║  DEFAULT_BEHAVIOR               = DENY                                          ║
║  FAILURE_MODE                   = HARD_STOP → REPORT → NO PARTIAL OUTPUT        ║
║  ANTIGRAVITY_CONFUSION          = IMPOSSIBLE                                    ║
╠═══════════════════════════════════════════════════════════════════════════════════╣
║  QUALITY_SCORE                  = 10 / 10                                       ║
║  ENTERPRISE_SAFE                = ✔                                             ║
║  MULTI_TENANT_ISOLATED          = ✔                                             ║
║  DOMAIN_ISOLATED                = ✔                                             ║
║  AUDIT_IMMUTABLE                = ✔  (hash-chained; AIC-1→AIC-24 compliant)     ║
║  AI_ADVISORY_ONLY               = ✔  (7 AI functions; zero autonomous decisions) ║
║  PAPER_ENCRYPTION               = ✔  (AES-256-GCM; Vault time-locked key)       ║
║  ELIGIBILITY_HARD_GATES         = ✔  (5 gates; all must pass; no partial)       ║
║  DUAL_SIGN_ENFORCED             = ✔  (result publish; grace marks; cert revoke)  ║
║  SEGREGATION_OF_DUTIES          = ✔  (AIC-11; paper-setter ≠ invigilator)       ║
║  OFFLINE_EXAM_COMPLIANT         = ✔  (OFF-19; auto-sync; no answer loss)        ║
║  PROCTORING_PRIVACY_SAFE        = ✔  (no video stored; signals only; UAMC-P)    ║
║  MALPRACTICE_DUE_PROCESS        = ✔  (hearing; defense; appeal; sealed)         ║
║  CERTIFICATE_TAMPER_PROOF       = ✔  (digital sig; QR verify; VALID/REVOKED)    ║
║  NAAC_NBA_UGC_READY             = ✔  (CO/PO mapping; auto-reports; freeze mode) ║
║  EXTERNAL_EXAMINER_GATED        = ✔  (time-window; limited access; audit)       ║
║  KAFKA_EVENTS_REGISTERED        = ✔  (28 events; all lifecycle points covered)   ║
║  API_CONTRACTS_LOCKED           = ✔  (40+ endpoints; time-locked paper decrypt) ║
║  UI_SCREENS_ROLE_GATED          = ✔  (6 role tiers; exam UI full lockdown)      ║
║  SIBLING_AGENTS_COMPATIBLE      = ✔  (Academic + Faculty + Attendance agents)   ║
╠═══════════════════════════════════════════════════════════════════════════════════╣
║                                                                                   ║
║   ANY DEVIATION FROM THIS PROMPT:                                                ║
║   ❌ STOP EXECUTION                                                               ║
║   ❌ LOG VIOLATION WITH FULL CONTEXT                                              ║
║   ❌ ESCALATE TO ARCHITECT                                                        ║
║   ❌ NO PARTIAL OUTPUT PERMITTED                                                  ║
║                                                                                   ║
║   ANY ADDITION TO THIS PROMPT:                                                   ║
║   ✅ SEMVER BUMP REQUIRED  (1.0.0 → 1.1.0 or 2.0.0)                             ║
║   ✅ ARCHITECT APPROVAL MANDATORY                                                 ║
║   ✅ BACKWARD COMPATIBILITY VERIFIED                                              ║
║   ✅ R49 CONTRACT VALIDATOR RE-RUN REQUIRED                                       ║
║   ✅ R50 QA TEST GENERATOR RE-RUN REQUIRED                                        ║
╚═══════════════════════════════════════════════════════════════════════════════════╝
```
