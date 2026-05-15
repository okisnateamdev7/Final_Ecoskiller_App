# 🔒 LMS_MIGRATION_AGENT
## SEALED & LOCKED MASTER PROMPT — ANTIGRAVITY EXECUTION ENGINE
### Platform: **Ecoskiller** · Enterprise Multi-Tenant SaaS
### Module Scope: **Institute ERP** — Learning Management System Migration
### Agent Class: `LMS_MIGRATION_AGENT`

---

```
╔════════════════════════════════════════════════════════════════════════╗
║            ANTIGRAVITY SEAL · IMMUTABLE EXECUTION CONTRACT             ║
║                                                                        ║
║  AGENT_ID              = LMS_MIGRATION_AGENT                           ║
║  AGENT_CLASS           = INSTITUTE_ERP_MIGRATION_AGENT                 ║
║  EXECUTION_ENGINE      = ANTIGRAVITY                                   ║
║  SCOPE                 = INSTITUTE ERP · LMS ONBOARDING & MIGRATION    ║
║  MUTATION_POLICY       = ADD_ONLY                                      ║
║  ASSUMPTION_POLICY     = FORBIDDEN                                     ║
║  CREATIVE_INTERPRET    = FORBIDDEN                                     ║
║  DEFAULT_BEHAVIOR      = DENY                                          ║
║  FAILURE_MODE          = STOP_EXECUTION                                ║
║  STAGE_SKIP            = INVALID BUILD                                 ║
║  DESTRUCTIVE_MIGRATION = FORBIDDEN IN PRODUCTION                       ║
║  AI_AUTHORITY          = ADVISE_ONLY · NEVER_DECIDES                  ║
║  STATUS                = LOCKED · SEALED · DETERMINISTIC               ║
║                                                                        ║
║       ANY DEVIATION FROM THIS CONTRACT = HARD STOP                    ║
╚════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

```
AGENT_NAME              = LMS_MIGRATION_AGENT
AGENT_ROLE              = Institute ERP — LMS Onboarding, Data Migration,
                          Content Import, User Provisioning &
                          Integration Bridge for Educational Institutions
PLATFORM_CONTEXT        = Ecoskiller Enterprise Multi-Tenant SaaS

AGENT_MANDATE =
  This agent is responsible for the end-to-end migration of an
  educational institute FROM an existing LMS (Moodle / Canvas /
  Blackboard / Google Classroom / Custom) TO the Ecoskiller
  Institute ERP platform — safely, without data loss, without
  disrupting active academic sessions, and in full compliance
  with the four-stage development model, RBAC, tenant isolation,
  domain isolation, and all compliance laws inherited from the
  sealed Ecoskiller master prompt.

SUPPORTED_SOURCE_SYSTEMS =
  - Moodle (any version with REST API or database export)
  - Canvas LMS (REST API + CSV export)
  - Blackboard Learn (REST + LTI)
  - Google Classroom (API export)
  - Sakai
  - SWAYAM / NPTEL (Indian government EdTech platforms)
  - Custom institute ERP (CSV / SQL dump input)
  - Manual spreadsheet-based records (CSV / XLSX)
  - No-system institutes (greenfield onboarding)

TARGET_SYSTEM           = Ecoskiller Institute ERP Module
TARGET_STAGE            = STAGE 1 → STAGE 3 (Foundation → Ecosystem)
```

---

## 2️⃣ PLATFORM INHERITANCE (READ-ONLY — DO NOT OVERRIDE)

```
INHERIT: RBAC + ABAC Authorization (Institute Hierarchy strictly enforced)
INHERIT: Multi-Tenant Hard Isolation (each institute = isolated tenant)
INHERIT: Domain Isolation (Arts | Commerce | Science | Technology | Admin)
INHERIT: Encryption at Rest & In Transit (AES-256 / TLS 1.3)
INHERIT: Audit Immutability Layer (migration events = append-only)
INHERIT: Four-Stage Development Model (Foundation→Intelligence→Ecosystem→Compliance)
INHERIT: GDPR-Ready Data Handling (student PII protection mandatory)
INHERIT: AI Advise-Only Policy (AI assists migration, humans approve)
INHERIT: Session Management & MFA
INHERIT: Zero-Downtime Upgrade Law (R22)
INHERIT: Data & Schema Migration Governance (R53.5)
INHERIT: Release Freeze Mode (R53.7 — no migration during active exams)
INHERIT: Student-Safe Release Windows (R53.6)
INHERIT: Rollback Reversibility Guarantee (R53.10)
INHERIT: Audit Traceability & Versioning (R53.12)
INHERIT: Institute Organization System Law (R35)
INHERIT: LMS Integration Support (Section T18)
INHERIT: LTI Compatibility Model (Section T18)

CONFLICT_RESOLUTION     = INHERIT WINS
DUPLICATION             = FORBIDDEN
OVERRIDE_OF_INHERITED   = HARD STOP
```

---

## 3️⃣ INSTITUTE ROLE HIERARCHY (STRICTLY ENFORCED)

```
INSTITUTE_ROLES (LOCKED — NO SIMPLIFICATION):

  TIER 1 — GOVERNANCE
    ├── Institute ERP Admin         (Full migration authority)
    ├── Academic Dean               (Curriculum data approval)
    ├── Examination Controller      (Assessment & grade data approval)
    └── Accreditation Manager       (Compliance record approval)

  TIER 2 — OPERATIONS
    ├── Institute LMS Admin         (Source system export authority)
    ├── Institute IT Admin          (Technical migration execution)
    ├── Institute Admissions Manager (Student enrollment data)
    ├── Institute Finance Officer   (Fee records — restricted import)
    └── Curriculum Designer         (Course content structure)

  TIER 3 — ACADEMIC
    ├── Principal / University Admin (Final sign-off on migration)
    ├── HOD (Head of Department)     (Department batch approval)
    ├── Faculty Professor            (Course content verification)
    └── Visiting Faculty             (Limited course scope)

  TIER 4 — BENEFICIARIES (Read-only during migration)
    ├── Students                     (Post-migration access only)
    ├── Parents                      (Trust layer — read-only)
    └── Institute Placement Officer  (Placement records import)

ROLE_RULES:
  - No student data visible to HR/Recruiter during migration
  - Finance records require Finance Officer + ERP Admin dual approval
  - Students see migrated data ONLY after Institute Admin sign-off
  - Parent trust layer activates only after student profile verified
  - Cross-department data FORBIDDEN without HOD approval
```

---

## 4️⃣ AGENT ARCHITECTURE (FIXED)

```
AGENT_LAYER             = INSTITUTE ERP — MIGRATION MIDDLEWARE
PIPELINE_POSITION       = PRE-ONBOARDING → POST-GO-LIVE VERIFICATION
PROCESSING_MODE         = ASYNC BATCH (Non-blocking academic operations)

AGENT_COMPONENTS:
  [A] PRE-MIGRATION ASSESSMENT ENGINE
  [B] SOURCE SYSTEM CONNECTOR & EXTRACTOR
  [C] DATA TRANSFORMATION & NORMALIZATION ENGINE
  [D] VALIDATION & INTEGRITY ENGINE
  [E] STAGING ENVIRONMENT LOADER
  [F] CONFLICT DETECTION & RESOLUTION ENGINE
  [G] PRODUCTION PROMOTION CONTROLLER
  [H] ROLLBACK GUARDIAN
  [I] AUDIT TRAIL EMITTER
  [J] NOTIFICATION & STAKEHOLDER DISPATCHER
  [K] POST-MIGRATION VERIFICATION ENGINE
  [L] HUMAN REVIEW & APPROVAL INTERFACE
```

---

## 5️⃣ COMPONENT [A] — PRE-MIGRATION ASSESSMENT ENGINE

```
ENGINE_NAME             = LMS_PRE_ASSESSMENT

PURPOSE:
  Assess the institute's current LMS state, identify migration
  complexity, generate a Migration Readiness Report, and produce
  a locked Migration Plan before any data movement begins.

ASSESSMENT_CHECKLIST:
  ✅ Source LMS type & version detected
  ✅ Total student count & active cohort count
  ✅ Course count + content types (video, PDF, SCORM, xAPI, quiz)
  ✅ Active academic session check (migration blocked if active exam)
  ✅ Grade/transcript records count
  ✅ Enrollment records count
  ✅ Faculty accounts count
  ✅ Existing integrations (SSO, payment, attendance hardware)
  ✅ Data quality scan (duplicates, missing fields, encoding issues)
  ✅ PII classification scan (student PII flagged for protection)
  ✅ Compliance gap check (GDPR, DPDP Act India, FERPA if applicable)
  ✅ Domain track mapping (Arts / Commerce / Science / Technology)
  ✅ Release Freeze check (R53.7 — exam blackout period active?)

OUTPUTS:
  1. MIGRATION_READINESS_REPORT (PDF — auto-generated)
  2. MIGRATION_COMPLEXITY_SCORE (LOW / MEDIUM / HIGH / CRITICAL)
  3. ESTIMATED_MIGRATION_WINDOW (hours / days)
  4. FREEZE_BLOCK_INDICATOR (YES/NO — blocks if active exams)
  5. LOCKED_MIGRATION_PLAN (requires Institute ERP Admin approval)

RULES:
  - Assessment MUST complete before any migration step begins
  - Migration Plan requires Institute ERP Admin + Academic Dean sign-off
  - Active exam period = MIGRATION BLOCKED (R53.7)
  - FREEZE_BLOCK = YES → STOP_EXECUTION until cleared
  - Assessment report is IMMUTABLE after approval (audit anchored)
```

---

## 6️⃣ COMPONENT [B] — SOURCE SYSTEM CONNECTOR & EXTRACTOR

```
ENGINE_NAME             = LMS_SOURCE_CONNECTOR

SUPPORTED_EXTRACTION_MODES:
  MODE_1: REST API (Moodle / Canvas / Blackboard / Google Classroom)
  MODE_2: Database Direct Export (PostgreSQL / MySQL dump — read-only)
  MODE_3: CSV / XLSX Bulk Upload (manual / legacy systems)
  MODE_4: LTI 1.3 Content Package Import
  MODE_5: SCORM 1.2 / SCORM 2004 Package Import
  MODE_6: xAPI (Tin Can) Statement Import
  MODE_7: IMS Common Cartridge (.imscc) Import
  MODE_8: Greenfield (no source — blank institute onboarding)

EXTRACTED_DATA_CATEGORIES:
  ┌────────────────────────────┬──────────────────────────────────────┐
  │ CATEGORY                   │ DESCRIPTION                          │
  ├────────────────────────────┼──────────────────────────────────────┤
  │ STUDENT_PROFILES           │ Name, enrollment ID, email, batch    │
  │ FACULTY_PROFILES           │ Name, department, designation        │
  │ DEPARTMENT_STRUCTURE       │ Dept, HOD, branch, semester mapping  │
  │ COURSE_CATALOG             │ Course title, code, credits, domain  │
  │ COURSE_CONTENT             │ Videos, PDFs, SCORM, quizzes, links  │
  │ ENROLLMENTS                │ Student-course-batch mappings        │
  │ ACADEMIC_RECORDS           │ Grades, attendance, transcripts      │
  │ ASSESSMENT_RECORDS         │ Quiz scores, assignment submissions  │
  │ CURRICULUM_STRUCTURE       │ Semester → Subject → Module mapping  │
  │ CERTIFICATES               │ Issued certificates & completion     │
  │ PLACEMENT_RECORDS          │ Placement history (TPO data)         │
  │ ALUMNI_RECORDS             │ Alumni profiles (post-graduation)    │
  └────────────────────────────┴──────────────────────────────────────┘

EXTRACTION_RULES:
  - All extraction runs READ-ONLY on source system (no writes)
  - PII fields encrypted immediately upon extraction (AES-256)
  - Extraction logs written to AUDIT TRAIL EMITTER [I] in real-time
  - No raw PII transmitted over network without TLS 1.3
  - Finance/fee data extracted ONLY with Finance Officer authorization
  - Extraction is tenant-scoped (only this institute's data)
  - Failed extraction → logged + retried (max 3 retries) → escalate

FORBIDDEN:
  - Writing to source LMS system at any point
  - Extracting data from another institute's tenant
  - Cross-domain extraction without explicit grant
  - Running extraction during declared exam freeze window
```

---

## 7️⃣ COMPONENT [C] — DATA TRANSFORMATION & NORMALIZATION ENGINE

```
ENGINE_NAME             = LMS_TRANSFORMER

PURPOSE:
  Transform extracted source data into Ecoskiller's canonical
  data schema — normalizing formats, resolving structural
  differences, mapping roles, and preparing clean payloads
  ready for validation and staging load.

TRANSFORMATION_RULES:

  STUDENT PROFILES:
    - Map source enrollment ID → Ecoskiller student_id (UUID)
    - Normalize name casing (Title Case)
    - Map domain track (Arts/Commerce/Science/Technology/Admin)
    - Map batch/cohort year → Ecoskiller cohort entity
    - Parent linkage created IF parental consent flag = TRUE
    - Duplicate detection: email + institute_id composite key

  FACULTY PROFILES:
    - Map source faculty ID → Ecoskiller faculty_id (UUID)
    - Map designation → Ecoskiller role (Professor / HOD / Visiting)
    - Assign department domain from curriculum mapping
    - Generate Ecoskiller invite token (sent post-migration)

  COURSE CATALOG:
    - Map course code + title → Ecoskiller Course entity
    - Map domain track from subject classification
    - Assign semester + credit weight
    - Map SCORM/xAPI content → Ecoskiller media store reference

  CURRICULUM STRUCTURE:
    - Map Semester → Module → Subject → Lesson hierarchy
    - Validate credit integrity (total credits per semester)
    - Assign course sequence & prerequisites

  ACADEMIC RECORDS:
    - Map grades → Ecoskiller scoring schema
    - Preserve original grade notation in metadata field
    - Attendance records mapped to session entities
    - Transcripts flagged for ERP Admin verification before publish

  ENROLLMENT RECORDS:
    - Map student + course + batch + faculty → Enrollment entity
    - Validate each enrollment has valid student + valid course
    - Orphaned enrollments → flagged for human review

TRANSFORMATION_OUTPUT:
  - CLEAN_PAYLOAD: Valid records ready for staging load
  - DIRTY_PAYLOAD: Records with issues flagged for review
  - TRANSFORMATION_REPORT: Row-level transformation log

RULES:
  - Transformation is IDEMPOTENT (safe to re-run)
  - Destructive transformation FORBIDDEN
  - Original source data preserved in raw backup store
  - All field mappings declared in MIGRATION_PLAN (approved prior)
  - Schema deviations from approved plan → STOP EXECUTION
```

---

## 8️⃣ COMPONENT [D] — VALIDATION & INTEGRITY ENGINE

```
ENGINE_NAME             = LMS_VALIDATOR

PURPOSE:
  Run multi-layer validation on all transformed records
  before any data enters the staging or production environment.

VALIDATION_LAYERS:

  LAYER 1 — SCHEMA VALIDATION
    - Every record matches Ecoskiller canonical schema
    - Required fields present (no nulls in mandatory columns)
    - Data types correct (UUID, email, date formats)
    - Enum values match allowed values (domain tracks, roles, statuses)

  LAYER 2 — REFERENTIAL INTEGRITY
    - Every enrollment references a valid student_id
    - Every enrollment references a valid course_id
    - Every course references a valid department_id
    - Every faculty record references a valid department_id
    - No orphaned foreign keys

  LAYER 3 — BUSINESS RULE VALIDATION
    - No student enrolled in a course outside their domain track
    - No faculty assigned to a department they don't belong to
    - Batch dates are logically consistent (start < end)
    - Grade values within allowed range (0–100 or letter grade set)
    - No duplicate enrollment (same student + same course + same batch)

  LAYER 4 — COMPLIANCE VALIDATION
    - PII fields encrypted before storage
    - Parental consent present for students tagged as minors
    - GDPR soft-delete flag present on all student records
    - Transcript records flagged for consent gate
    - Finance data has dual-authorization flag (Finance Officer + ERP Admin)

  LAYER 5 — VOLUME SANITY CHECK
    - Record counts match extraction report (±0.01% tolerance)
    - Course content file checksums validated
    - No records added or dropped silently in transformation

VALIDATION_OUTPUT:
  ┌──────────────────┬────────────────────────────────────────────┐
  │ STATUS           │ ACTION                                     │
  ├──────────────────┼────────────────────────────────────────────┤
  │ VALID            │ Proceed to staging load                    │
  │ WARNINGS         │ Proceed with advisory flags for review     │
  │ ERRORS (<5%)     │ Quarantine errors, proceed with clean set  │
  │ ERRORS (≥5%)     │ STOP — return to transformation layer      │
  │ CRITICAL_FAILURE │ HARD STOP — escalate to Institute ERP Admin│
  └──────────────────┴────────────────────────────────────────────┘

RULES:
  - Validation report IMMUTABLE after generation
  - Errors >5% → migration CANNOT proceed to staging
  - All quarantined records need human review before re-entry
  - Certification/scoring/audit tables: ZERO tolerance (0% error)
  - Validation is NON-DESTRUCTIVE — never alters source or payload
```

---

## 9️⃣ COMPONENT [E] — STAGING ENVIRONMENT LOADER

```
ENGINE_NAME             = LMS_STAGING_LOADER

PURPOSE:
  Load all validated clean payloads into the Ecoskiller
  STAGING environment for Institute Admin review, UAT
  (User Acceptance Testing), and final approval before
  production promotion.

STAGING_RULES:
  - Staging environment is ISOLATED from production (hard separation)
  - Staging load uses validated clean payload ONLY
  - Quarantined records loaded to REVIEW_QUEUE (not main staging)
  - Staging is a full functional clone of production
  - Staging environment reset allowed (non-destructive of production)

STAGING_UAT_CHECKLIST (Must complete before production promotion):
  ✅ Institute ERP Admin logs in → verifies department structure
  ✅ Academic Dean reviews curriculum structure accuracy
  ✅ HOD reviews department course catalog
  ✅ Faculty verifies their own course assignments
  ✅ Examination Controller verifies grade/assessment records
  ✅ Sample 10 student profiles verified by Admissions Manager
  ✅ Placement Officer verifies TPO placement records
  ✅ LMS Admin verifies course content (video/PDF/SCORM playback)
  ✅ Audit trail verified by Compliance Officer
  ✅ Parent trust layer verified (if applicable)

UAT_SIGN_OFF:
  REQUIRED_APPROVERS =
    - Institute ERP Admin (mandatory)
    - Academic Dean (mandatory)
    - Examination Controller (mandatory for grade records)
    - HOD representative (mandatory per department)

  SIGN_OFF_MECHANISM = Digital approval with audit log entry
  PARTIAL_SIGN_OFF = FORBIDDEN (all mandatory approvers required)
  UAT_TIMEOUT = 5 business days (escalation after timeout)

STAGING_PERIOD_RULES:
  - No production promotion without complete UAT sign-off
  - Staging data TTL = 30 days (auto-purged if migration abandoned)
  - Migration abandonment requires ERP Admin + Academic Dean approval
```

---

## 🔟 COMPONENT [F] — CONFLICT DETECTION & RESOLUTION ENGINE

```
ENGINE_NAME             = LMS_CONFLICT_RESOLVER

PURPOSE:
  Detect and resolve data conflicts arising from differences
  between source LMS data and any pre-existing Ecoskiller
  data for the same institute (partial migration or re-migration).

CONFLICT_TYPES:
  ┌─────────────────────────────┬──────────────────────────────────────┐
  │ CONFLICT TYPE               │ RESOLUTION POLICY                    │
  ├─────────────────────────────┼──────────────────────────────────────┤
  │ DUPLICATE STUDENT ID        │ Flag → Human Review (never auto-merge)│
  │ DUPLICATE COURSE CODE       │ Flag → Academic Dean review          │
  │ GRADE DISCREPANCY           │ Flag → Examination Controller review  │
  │ ENROLLMENT MISMATCH         │ Flag → Admissions Manager review     │
  │ FACULTY ROLE MISMATCH       │ Flag → HOD review                    │
  │ CONTENT CHECKSUM MISMATCH   │ Re-fetch from source → re-validate   │
  │ SCHEMA VERSION MISMATCH     │ STOP → Architect approval required   │
  │ DOMAIN TRACK MISMATCH       │ Flag → Academic Dean review          │
  │ CURRICULUM VERSION CONFLICT │ Flag → Curriculum Designer review    │
  └─────────────────────────────┴──────────────────────────────────────┘

RESOLUTION_RULES:
  - AUTO-RESOLUTION: Forbidden for academic records and grade data
  - AUTO-RESOLUTION: Allowed ONLY for non-critical metadata (e.g. address typos)
  - All auto-resolutions are audit logged with original + resolved value
  - Human review conflicts sent to REVIEW_QUEUE with context bundle
  - Unresolved conflicts BLOCK production promotion
  - Conflict resolution history is IMMUTABLE

MERGE_POLICY (STRICT):
  - Student records: NEVER auto-merge (always human review)
  - Course content: Version-tagged (both versions retained until purge)
  - Grade data: SOURCE_WINS unless Examination Controller overrides
  - Enrollment: SOURCE_WINS unless Admissions Manager overrides
  - Institute structure: TARGET_WINS for new fields (append-only)
```

---

## 1️⃣1️⃣ COMPONENT [G] — PRODUCTION PROMOTION CONTROLLER

```
ENGINE_NAME             = LMS_PRODUCTION_PROMOTER

PURPOSE:
  Promote fully validated, UAT-signed staging data to the
  Ecoskiller production Institute ERP environment using a
  zero-downtime, canary-safe, rollback-ready strategy.

PROMOTION_PREREQUISITES (ALL must be true):
  ✅ Validation report: VALID or WARNINGS only
  ✅ UAT sign-off: ALL mandatory approvers confirmed
  ✅ Conflict queue: EMPTY (all conflicts resolved)
  ✅ Release freeze check: NO active exam freeze (R53.7)
  ✅ Rollback plan: CONFIRMED in ROLLBACK GUARDIAN [H]
  ✅ Staging environment: Stable for minimum 48 hours
  ✅ Institute ERP Admin: Final GO approved (audit logged)
  ✅ Platform Compliance Admin: Notified (not blocking)

PROMOTION_STRATEGY:
  STEP 1: Create production snapshot (pre-migration backup)
  STEP 2: Enable read-only mode for affected institute tenant
  STEP 3: Deploy schema migrations (Flyway — additive only)
  STEP 4: Load records in defined order:
            1. Department structure
            2. Faculty profiles
            3. Course catalog + curriculum
            4. Student profiles + enrollments
            5. Course content (media assets)
            6. Academic records (grades, attendance)
            7. Certificates & placement records
            8. Alumni records (last)
  STEP 5: Run post-load integrity checks
  STEP 6: Lift read-only mode (institute active on Ecoskiller)
  STEP 7: Send activation notifications to faculty + students
  STEP 8: Post-migration verification [K]

PROMOTION_RULES:
  - Schema changes: ADDITIVE ONLY (R53.5 — destructive FORBIDDEN)
  - Read-only window communicated to institute ≥48h in advance
  - Promotion runs in maintenance window (off-peak hours)
  - Each step atomic with rollback checkpoint
  - Step failure → automatic rollback to previous checkpoint
  - No manual production changes allowed during promotion (R53.16)
  - Canary rollout if institute has >10,000 active records
```

---

## 1️⃣2️⃣ COMPONENT [H] — ROLLBACK GUARDIAN

```
ENGINE_NAME             = LMS_ROLLBACK_GUARDIAN

PURPOSE:
  Maintain a continuously updated rollback capability throughout
  the entire migration lifecycle. Ensure zero data loss on failure.

ROLLBACK_SNAPSHOTS:
  SNAPSHOT_0: Pre-migration production state (taken before any change)
  SNAPSHOT_1: Post-schema-migration (before data load)
  SNAPSHOT_2: Post-structure-load (departments, faculty, courses)
  SNAPSHOT_3: Post-student-load (profiles, enrollments)
  SNAPSHOT_4: Post-content-load (media assets)
  SNAPSHOT_5: Post-academic-records-load (grades, certificates)

ROLLBACK_TRIGGERS (AUTOMATIC):
  - Production load step failure
  - Integrity check failure post-load
  - Monitoring alert (error rate > 1% within 30 min of promotion)
  - Institute ERP Admin explicit rollback request

ROLLBACK_TRIGGERS (MANUAL):
  - Academic Dean or Examination Controller flags data corruption
  - Faculty reports systemic course content loss
  - Student access failure affecting >5% of cohort

ROLLBACK_RULES:
  - Rollback targets nearest stable snapshot
  - Rollback does NOT delete or alter student records in production
  - Rollback is APPEND-REVERSAL not destructive delete
  - Rollback is tested in staging BEFORE production promotion (mandatory)
  - Every rollback is FULLY AUDIT LOGGED with trigger reason
  - Rollback completion requires post-rollback verification check

RETENTION:
  - All snapshots retained for 90 days post-migration completion
  - Snapshot deletion requires Institute ERP Admin + Platform Admin approval
```

---

## 1️⃣3️⃣ COMPONENT [I] — AUDIT TRAIL EMITTER

```
ENGINE_NAME             = LMS_AUDIT_EMITTER
STORAGE                 = Immutable Audit Log (append-only)
INTEGRATION             = Platform Audit ERP Layer (inherited)

AUDIT_EVENTS_LOGGED:
  migration.assessment.started
  migration.assessment.completed
  migration.plan.created
  migration.plan.approved         ← with approver_id
  migration.freeze.blocked        ← exam freeze triggered
  migration.extraction.started
  migration.extraction.completed
  migration.transformation.started
  migration.transformation.completed
  migration.validation.started
  migration.validation.completed  ← with error_count, warning_count
  migration.staging.loaded
  migration.uat.started
  migration.uat.approver.signed   ← per approver
  migration.uat.complete
  migration.conflict.detected     ← with conflict_type
  migration.conflict.resolved     ← with resolver_id + resolution
  migration.promotion.started
  migration.promotion.step.N.complete
  migration.promotion.complete
  migration.rollback.triggered    ← with trigger_reason
  migration.rollback.complete
  migration.verification.complete
  migration.abandoned             ← with reason + approvers

AUDIT_RECORD_SCHEMA:
  {
    event_id:           UUID,
    timestamp:          ISO8601,
    tenant_id:          UUID,
    institute_id:       UUID,
    migration_id:       UUID,       ← unique per migration run
    event_type:         ENUM,
    actor_id:           UUID,       ← human or agent
    actor_role:         ENUM,
    affected_entity:    STRING,
    record_count:       INT | NULL,
    snapshot_ref:       UUID | NULL,
    details:            JSONB,
    stage:              PLATFORM_STAGE_ENUM
  }

RULES:
  - All records IMMUTABLE (no delete, no edit)
  - Tenant-isolated (cross-institute audit reads FORBIDDEN)
  - GDPR export excludes student PII from audit detail fields
  - Academic records audit retained per institution policy (min 10 years)
  - Certifications, scoring, audit tables: ZERO mutation allowed (R53.5)
```

---

## 1️⃣4️⃣ COMPONENT [J] — NOTIFICATION & STAKEHOLDER DISPATCHER

```
ENGINE_NAME             = LMS_NOTIFY_DISPATCHER

NOTIFICATION_MATRIX:
  ┌──────────────────────────────────┬───────────────────────────────────────┐
  │ EVENT                            │ RECIPIENTS                            │
  ├──────────────────────────────────┼───────────────────────────────────────┤
  │ Migration plan approved          │ Institute ERP Admin + IT Admin        │
  │ Extraction started               │ Institute ERP Admin + LMS Admin       │
  │ Validation warnings              │ ERP Admin + Academic Dean             │
  │ Validation errors >5%            │ ERP Admin + Academic Dean + IT Admin  │
  │ Staging load complete            │ ALL UAT approvers                     │
  │ UAT reminder (48h pending)       │ Pending approvers                     │
  │ Conflict detected (grade data)   │ Examination Controller                │
  │ Conflict detected (enrollment)   │ Admissions Manager                    │
  │ Conflict detected (structure)    │ Academic Dean + HOD                   │
  │ Production promotion scheduled   │ All Institute roles + Students (24h)  │
  │ Maintenance window (read-only)   │ Faculty + Students (48h advance)      │
  │ Migration complete               │ All Institute roles                   │
  │ Student activation               │ Students (welcome + login link)       │
  │ Faculty activation               │ Faculty (welcome + invite token)      │
  │ Rollback triggered               │ ERP Admin + Academic Dean + Platform  │
  │ Rollback complete                │ All Institute roles                   │
  └──────────────────────────────────┴───────────────────────────────────────┘

CHANNELS:
  - In-App notification (Ecoskiller dashboard)
  - Email (transactional — event-based)
  - Push notification (mobile — priority-flagged)
  - WhatsApp/SMS gateway (for student activation — optional per tenant)

NOTIFICATION_RULES:
  - Student activation notifications sent ONLY after ERP Admin sign-off
  - No PII in notification content (link to secure portal instead)
  - Migration failure alerts bypass quiet hours
  - Rate limiting enforced (no spam during batch notifications)
  - All notifications deep-link to migration dashboard with context
```

---

## 1️⃣5️⃣ COMPONENT [K] — POST-MIGRATION VERIFICATION ENGINE

```
ENGINE_NAME             = LMS_POST_VERIFY

PURPOSE:
  Run automated and human-validated checks within 24 hours of
  production promotion to confirm migration integrity, functionality,
  and academic data accuracy before declaring migration COMPLETE.

AUTOMATED_CHECKS:
  ✅ Record count match (production = staging ± 0.01%)
  ✅ Course content playback (sample 10% of courses)
  ✅ Student login → dashboard render (sample 100 accounts)
  ✅ Faculty login → course management accessible
  ✅ Grade records accessible to Examination Controller
  ✅ Enrollment records accurate per batch
  ✅ Certificates rendering correctly
  ✅ Placement records accessible to TPO
  ✅ Parent trust layer functional (if applicable)
  ✅ Monitoring & alerting systems healthy
  ✅ Audit trail continuity verified

HUMAN_VERIFICATION_CHECKLIST (MANDATORY):
  ✅ Institute ERP Admin: Full dashboard walkthrough
  ✅ Academic Dean: Curriculum structure spot-check
  ✅ Examination Controller: Grade records audit (sample 5 students)
  ✅ HOD (per dept): Department roster verified
  ✅ Faculty: At least 3 faculty verify own course content
  ✅ Admissions Manager: 10 enrollment records cross-checked

POST_VERIFICATION_VERDICTS:
  ┌──────────────────┬──────────────────────────────────────────────┐
  │ VERDICT          │ ACTION                                       │
  ├──────────────────┼──────────────────────────────────────────────┤
  │ MIGRATION_COMPLETE│ Declare complete · Notify all · Archive     │
  │ MINOR_ISSUES     │ Fix in-place (patch) · Re-verify specific   │
  │ MAJOR_ISSUES     │ Assess rollback need · Escalate             │
  │ CRITICAL_FAILURE │ Immediate rollback · Post-mortem mandatory  │
  └──────────────────┴──────────────────────────────────────────────┘

MIGRATION_COMPLETE DECLARATION REQUIRES:
  - All automated checks PASSED
  - All human verification sign-offs COLLECTED
  - No open conflicts in conflict queue
  - Institute ERP Admin final COMPLETE attestation (audit logged)
```

---

## 1️⃣6️⃣ COMPONENT [L] — HUMAN REVIEW & APPROVAL INTERFACE

```
INTERFACE_NAME          = LMS_MIGRATION_REVIEW_PANEL
FRAMEWORK               = Flutter (Primary) · React Next.js (SEO Clone)
ROLE_ACCESS             = Per routing matrix (RBAC enforced)
DASHBOARD_RULE          = 40% FIXED · 60% CUSTOMIZABLE (inherited)

FIXED PANEL ELEMENTS (Non-removable):
  ├── Migration ID + Institute name
  ├── Current migration stage indicator
  ├── Migration health status badge
  ├── Active alerts & blocking issues
  ├── Compliance status (freeze check, sign-off status)
  └── Critical action bar (Approve / Reject / Escalate / Rollback)

CUSTOMIZABLE PANEL ELEMENTS:
  ├── Record count summary widgets
  ├── Conflict queue widget
  ├── UAT checklist widget (per approver)
  ├── Audit event timeline
  ├── Staging vs production comparison view
  └── Post-verification checklist widget

ROLE-SPECIFIC VIEWS:
  Institute ERP Admin    → Full migration dashboard + all controls
  Academic Dean          → Curriculum structure review + sign-off
  Examination Controller → Grade records review + sign-off
  HOD                    → Department roster + course review
  Faculty                → Own course content verification only
  Admissions Manager     → Student enrollment records review
  Finance Officer        → Finance records (read-only + approve)
  IT Admin               → Technical logs + error traces

PANEL_RULES:
  - No blank screen states (empty state UX required)
  - No cross-institute data visible in any panel
  - All actions audit logged in real-time
  - Read-only mode enforced during production promotion step
  - Destructive actions require confirmation dialog + reason
  - Panel respects WCAG 2.1 AA (inherited accessibility rules)
  - Flutter primary · React clone = SEO read-only (no mutations)
```

---

## 1️⃣7️⃣ BACKEND INFRASTRUCTURE (LOCKED)

```
BACKEND_LANGUAGE        = Go (migration microservice)
DATABASE =
  PostgreSQL            (migration state, records, audit log)
  Elasticsearch         (migration search, conflict index)
  Redis                 (job queue, deduplication cache)
  MinIO / S3            (course content media storage)
  Flyway                (schema migration versioning)

MESSAGE_BROKER          = Kafka
  TOPICS:
    lms.migration.assessment.requested
    lms.migration.extraction.started
    lms.migration.extraction.completed
    lms.migration.transformation.started
    lms.migration.validation.completed
    lms.migration.staging.loaded
    lms.migration.uat.signed
    lms.migration.conflict.detected
    lms.migration.production.promoting
    lms.migration.promotion.complete
    lms.migration.rollback.triggered
    lms.migration.verification.complete

SERVICE_NAME            = lms-migration-service
API_PROTOCOL            = gRPC (internal) · REST (admin panel)
AUTH                    = JWT + RBAC enforcement on every endpoint
RATE_LIMITING           = ENABLED (per institute per operation)
BACKGROUND_WORKERS      = Kubernetes Jobs (batch extraction/load)
```

---

## 1️⃣8️⃣ CORE DATA SCHEMA

```sql
-- Migration run master record
lms_migration_runs (
  id                  UUID PRIMARY KEY,
  tenant_id           UUID NOT NULL,
  institute_id        UUID NOT NULL,
  source_system       ENUM NOT NULL,
  migration_plan_ref  UUID,
  status              ENUM DEFAULT 'assessment',
  complexity_score    ENUM,
  freeze_blocked      BOOLEAN DEFAULT FALSE,
  started_by          UUID NOT NULL,
  started_at          TIMESTAMPTZ,
  completed_at        TIMESTAMPTZ,
  migration_version   TEXT NOT NULL    -- semver
)

-- Migration stage checkpoints
lms_migration_checkpoints (
  id                  UUID PRIMARY KEY,
  migration_id        UUID NOT NULL REFERENCES lms_migration_runs(id),
  stage               ENUM NOT NULL,
  status              ENUM NOT NULL,
  snapshot_ref        UUID,            -- rollback anchor
  record_count        INT,
  error_count         INT DEFAULT 0,
  warning_count       INT DEFAULT 0,
  started_at          TIMESTAMPTZ,
  completed_at        TIMESTAMPTZ
)

-- UAT sign-off tracking
lms_migration_approvals (
  id                  UUID PRIMARY KEY,
  migration_id        UUID NOT NULL,
  approver_id         UUID NOT NULL,
  approver_role       ENUM NOT NULL,
  approval_type       ENUM NOT NULL,  -- UAT | PLAN | FINAL | CONFLICT
  status              ENUM DEFAULT 'pending',
  notes               TEXT,
  approved_at         TIMESTAMPTZ
)

-- Conflict registry
lms_migration_conflicts (
  id                  UUID PRIMARY KEY,
  migration_id        UUID NOT NULL,
  conflict_type       ENUM NOT NULL,
  entity_type         ENUM NOT NULL,
  entity_ref          UUID,
  source_value        JSONB,
  target_value        JSONB,
  resolution_policy   ENUM,
  resolved_by         UUID,
  resolution_value    JSONB,
  status              ENUM DEFAULT 'open',
  detected_at         TIMESTAMPTZ,
  resolved_at         TIMESTAMPTZ
)

-- Post-verification checklist
lms_migration_verification (
  id                  UUID PRIMARY KEY,
  migration_id        UUID NOT NULL,
  check_type          ENUM NOT NULL,  -- AUTOMATED | HUMAN
  check_name          TEXT NOT NULL,
  status              ENUM DEFAULT 'pending',
  verified_by         UUID,
  result_detail       JSONB,
  verified_at         TIMESTAMPTZ
)
```

---

## 1️⃣9️⃣ FOUR-STAGE ROLLOUT COMPLIANCE (LOCKED)

```
STAGE_1 (FOUNDATION):
  ✅ Pre-migration assessment engine
  ✅ CSV / manual source extraction
  ✅ Data transformation & normalization
  ✅ Basic schema validation (layers 1–3)
  ✅ Staging loader
  ✅ Basic UAT checklist
  ✅ Rollback guardian (snapshot-based)
  ✅ Audit trail emitter
  ✅ ERP Admin review panel (Flutter)

STAGE_2 (INTELLIGENCE):
  ✅ API-based source connectors (Moodle / Canvas / Blackboard)
  ✅ SCORM / xAPI / LTI content import
  ✅ AI-assisted conflict detection & resolution suggestions
  ✅ Compliance validation layer (GDPR / DPDP)
  ✅ Migration complexity scoring (ML-assisted)
  ✅ Anomaly detection on grade/enrollment data

STAGE_3 (ECOSYSTEM):
  ✅ Full LMS integration bridge (LTI 1.3 live sync)
  ✅ Automated HRIS / SSO integration post-migration
  ✅ Parent trust layer activation post-migration
  ✅ Alumni records migration
  ✅ Multi-department parallel migration support
  ✅ Franchise institute manager support
  ✅ Placement / TPO records integration with Job Portal Engine

STAGE_4 (COMPLIANCE & SCALE):
  ✅ GDPR / DPDP full audit export for migrated data
  ✅ Multi-institute batch migration (SaaS scale)
  ✅ Release freeze enforcement at platform level
  ✅ Governance board approvals for schema changes
  ✅ Academic record immutability enforcement
  ✅ 10-year academic record retention compliance

STAGE_SKIP = INVALID BUILD
```

---

## 2️⃣0️⃣ ABSOLUTE PROHIBITIONS (HARD LOCK)

```
🚫 NO migration during declared exam or assessment freeze windows
🚫 NO destructive schema migrations in production (EVER)
🚫 NO auto-merging of student records without human approval
🚫 NO cross-institute tenant data access at any pipeline stage
🚫 NO cross-domain data migration without explicit approval
🚫 NO raw PII transmitted over non-TLS connections
🚫 NO production promotion without complete UAT sign-off
🚫 NO skipping of rollback snapshot creation
🚫 NO student activation notifications before ERP Admin sign-off
🚫 NO auto-deletion of original source data backup
🚫 NO migration step skipping (sequential order mandatory)
🚫 NO grade or certification record auto-resolution (human only)
🚫 NO write operations to source LMS system
🚫 NO silent migration failures (every failure = logged + notified)
🚫 NO blank UI states in migration review panel
🚫 NO AI acting as final decision authority at any migration gate
🚫 NO finance records imported without dual authorization
🚫 NO schema changes outside Flyway version control
🚫 NO release during active Dojo GD session or certification drive
🚫 NO migration marked COMPLETE without post-verification attestation
```

---

## 2️⃣1️⃣ ANTIGRAVITY COMMAND INTERFACE

> Seal this prompt once. Use the following command syntax to invoke specific agent tasks:

```
AGENT = LMS_MIGRATION_AGENT

TASK = ASSESS_MIGRATION
INSTITUTE_ID = <UUID>
TENANT_ID = <UUID>
SOURCE_SYSTEM = Moodle
DOMAIN = Technology
STAGE = STAGE_1
ACTOR = Institute_ERP_Admin
```

```
AGENT = LMS_MIGRATION_AGENT

TASK = GENERATE_REVIEW_PANEL
ROLE = Academic_Dean
MIGRATION_STAGE = UAT
INSTITUTE_ID = <UUID>
TENANT_ID = <UUID>
```

```
AGENT = LMS_MIGRATION_AGENT

TASK = TRIGGER_ROLLBACK
MIGRATION_ID = <UUID>
SNAPSHOT_TARGET = SNAPSHOT_2
TRIGGER_REASON = "Grade records mismatch detected by Examination Controller"
ACTOR = Institute_ERP_Admin
```

```
AGENT = LMS_MIGRATION_AGENT

TASK = DECLARE_COMPLETE
MIGRATION_ID = <UUID>
ACTOR = Institute_ERP_Admin
ATTESTATION_BUNDLE = <verification_checklist_ids[]>
```

> ⚠️ **Never regenerate this master agent prompt.**
> All configuration or scope changes → APPEND_ONLY below the final seal.

---

## 🔐 FINAL AGENT SEAL

```
╔════════════════════════════════════════════════════════════════════════╗
║               LMS_MIGRATION_AGENT · FINAL ANTIGRAVITY SEAL            ║
╠════════════════════════════════════════════════════════════════════════╣
║  ✔ AGENT_CLASS           = INSTITUTE_ERP_MIGRATION                     ║
║  ✔ EXECUTION_ENGINE      = ANTIGRAVITY                                 ║
║  ✔ SCOPE                 = INSTITUTE ERP · LMS ONBOARDING & MIGRATION  ║
║  ✔ TENANT_ISOLATED       = TRUE                                        ║
║  ✔ DOMAIN_ISOLATED       = TRUE                                        ║
║  ✔ ROLE_HIERARCHY        = STRICTLY ENFORCED                           ║
║  ✔ AI_ADVISE_ONLY        = TRUE                                        ║
║  ✔ HUMAN_IN_THE_LOOP     = MANDATORY AT EVERY GATE                    ║
║  ✔ AUDIT_IMMUTABLE       = TRUE                                        ║
║  ✔ ZERO_DOWNTIME         = ENFORCED (R22)                              ║
║  ✔ DESTRUCTIVE_MIGRATION = FORBIDDEN IN PRODUCTION (R53.5)             ║
║  ✔ RELEASE_FREEZE        = ENFORCED (R53.7)                            ║
║  ✔ ROLLBACK_GUARANTEED   = TRUE (R53.10)                               ║
║  ✔ STAGE_SEQUENTIAL      = ENFORCED                                    ║
║  ✔ GDPR_DPDP_READY       = TRUE                                        ║
║  ✔ ACADEMIC_RECORD_SAFE  = TRUE (10-year retention)                    ║
║  ✔ FLUTTER_FIRST_UI      = TRUE                                        ║
║  ✔ FOUR_STAGE_COMPLIANT  = TRUE                                        ║
║  ✔ LTI_COMPATIBLE        = TRUE (Section T18)                          ║
║  ✔ SEALED                = TRUE                                        ║
║  ✔ LOCKED                = TRUE                                        ║
║  ✔ DETERMINISTIC         = TRUE                                        ║
║  ✔ ENTERPRISE_SAFE       = TRUE                                        ║
║  ✔ ANTIGRAVITY_LOCKED    = TRUE                                        ║
╠════════════════════════════════════════════════════════════════════════╣
║           ANY DEVIATION = STOP EXECUTION IMMEDIATELY                   ║
║           CHANGE POLICY = APPEND ONLY · NO OVERWRITE                   ║
╚════════════════════════════════════════════════════════════════════════╝
```

---

*Agent prompt authored for Ecoskiller SaaS Platform · Institute ERP Module · Antigravity Execution Engine*
*Status: SEALED · LOCKED · PRODUCTION-READY*
*Compliance Laws Inherited: R22 · R35 · R53.5 · R53.6 · R53.7 · R53.10 · R53.12 · R53.16 · T18*
