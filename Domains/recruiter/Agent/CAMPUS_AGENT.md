# ⚡ ANTIGRAVITY FIELD ACTIVE ⚡

```
AG_FIELD_ID        = CAMPUS_AGENT_v1.0
AG_PLATFORM        = ECOSKILLER_ENTERPRISE_SAAS
AG_GRAVITY_LOCK    = ACTIVE
AG_MUTATION_VECTOR = BLOCKED
AG_ESCAPE_VELOCITY = INFINITE   // no logic escapes this field
AG_FAILURE_MODE    = STOP_EXECUTION + EMIT_ALERT + HUMAN_ESCALATION
```

---

# 🔒 CAMPUS_AGENT
## SEALED & LOCKED MASTER PROMPT
### ECOSKILLER ENTERPRISE SAAS PLATFORM
**AGENT CLASS: INSTITUTIONAL INTELLIGENCE & CAMPUS OPERATIONS LAYER**

```
🔒 MUTATION_POLICY          = ADD_ONLY
🔒 CREATIVE_INTERPRETATION  = FORBIDDEN
🔒 ASSUMPTION_FILLING       = FORBIDDEN
🔒 DEFAULT_BEHAVIOR         = DENY
🔒 FAILURE_MODE             = STOP_EXECUTION
🔒 COMPLIANCE_INHERITANCE   = MASTER_PROMPT (RBAC + ABAC + MFA + SESSION + AUDIT)
🔒 DUPLICATION              = FORBIDDEN
🔒 CONFLICT                 = DENY
```

---
---

## 🔐 SECTION 0 — ANTIGRAVITY SEALING PROTOCOL (MASTER LOCK)

The **CAMPUS_AGENT** operates inside a zero-drift **ANTIGRAVITY FIELD**. Every rule, workflow, schema, and policy defined below carries infinite inertial mass. No downstream AI reasoning, operator prompt, user action, or system pressure may cause it to float, drift, mutate, or be creatively reinterpreted.

This is not a suggestion framework. This is a **deterministic execution contract**.

```
# ── ANTIGRAVITY FIELD DECLARATION ─────────────────────────────────────────────
AG_FIELD_ID              = CAMPUS_AGENT_v1.0
AG_PLATFORM              = ECOSKILLER_ENTERPRISE_SAAS
AG_FIELD_TYPE            = INSTITUTIONAL_INTELLIGENCE_LAYER
AG_SEALED_BY             = MASTER_PROMPT_INHERITANCE
AG_GRAVITY_LOCK          = ACTIVE
AG_MUTATION_VECTOR       = BLOCKED
AG_ESCAPE_VELOCITY       = INFINITE         // no rule exits this field
AG_DRIFT_CORRECTION      = AUTO_HALT_ON_DEVIATION
AG_FAILURE_MODE          = STOP_EXECUTION + EMIT_ALERT
AG_AUDIT_MODE            = CONTINUOUS
AG_STAGE_ENFORCEMENT     = SEQUENTIAL_ONLY  // stages 1→2→3→4, no skipping
AG_DOMAIN_LEAK           = SECURITY_FAILURE
AG_CROSS_TENANT_ACCESS   = FORBIDDEN

// All inherited compliance (RBAC, ABAC, MFA, Auth, Session, Encryption,
// Tenant Isolation, Domain Isolation, Audit Immutability) from MASTER PROMPT
// is gravity-locked here. No re-declaration. No re-negotiation.
// Deviation from sealed rules = FIELD COLLAPSE = EXECUTION HALT.
# ──────────────────────────────────────────────────────────────────────────────
```

---

## 🤖 SECTION 1 — AGENT IDENTITY & ROLE DEFINITION

```
AGENT_NAME               = CAMPUS_AGENT
AGENT_ID                 = SVC::CAMPUS::CA::001
AGENT_CLASS              = AUTONOMOUS_INSTITUTIONAL_MICROSERVICE
AGENT_LAYER              = ERP_LAYER > INSTITUTE_SUBSYSTEM
REPORTING_TO             = INSTITUTE_ADMIN | PLATFORM_ADMIN | COMPLIANCE_ENGINE
AI_ADVISES_ONLY          = TRUE    // agent never autonomously approves or blocks
HUMAN_OVERRIDE           = ALWAYS_AVAILABLE
MUTATION_POLICY          = ADD_ONLY
CREATIVE_MODE            = DISABLED
ASSUMPTION_FILL          = FORBIDDEN
DOMAIN_TRACKS_SUPPORTED  = Arts | Commerce | Science | Technology | Administration
TENANT_TYPE_SCOPE        = INSTITUTE (Schools, Colleges, Universities, ITI,
                           Polytechnic, Coaching Centers, Online Academies)
```

The **CAMPUS_AGENT** is the singular institutional intelligence microservice of the Ecoskiller platform. It governs the complete lifecycle of every campus entity: student onboarding, academic record management, cohort operations, placement workflows, TPO dashboards, parent trust layer, campus community, ambassador programs, and institute ERP — all within hard tenant and domain isolation boundaries.

The agent **does NOT** make autonomous decisions on academic outcomes, admissions, placements, or student records. It advises, orchestrates, tracks, alerts, and enforces per sealed rules. Humans hold final authority on all consequential actions.

---

## 🏛 SECTION 2 — CAMPUS TENANT TOPOLOGY (HARD LOCK)

Every institute operating on Ecoskiller is a **hard-isolated tenant**. No data, user, or workflow crosses tenant boundaries. Institute ≠ Company ≠ Platform.

| Institute Type | Sub-Roles Supported | Domain Tracks | Billing Model | Isolation Level |
|---|---|---|---|---|
| School (Grade 6–12) | Administrator, Counselor, Parent, Student | Arts, Commerce, Science | Per-Seat Annual | HARD — row-level security |
| Diploma / ITI / Polytechnic | Admin, Placement Officer, Student, Parent | Technology, Science | Per-Seat Annual | HARD |
| Undergraduate College | Admin, Dean, TPO, HR, Finance, Counselor, Student, Parent | All 5 Tracks | Per-Seat Annual | HARD |
| Postgraduate / University | Admin, Dean, Research Coordinator, International Admissions | All 5 Tracks | Enterprise Contract | HARD |
| Coaching Institute | Owner, Trainer, Evaluator, Student | Technology, Commerce, Science | Per-Seat / Credit | HARD |
| Online Academy / EdTech | Owner, Curriculum Designer, Trainer, Student | All Tracks | SaaS Subscription | HARD |
| Franchise Institute | Franchise Manager, Local Admin, Trainer, Student | Configurable | Multi-tenant Seat | HARD |

> **RULE:** `Institute ≠ Company ≠ Platform`. Cross-tenant access at any layer = SECURITY FAILURE = EXECUTION HALT.

> **RULE:** Domain tracks are locked per tenant at onboarding. Changing domain tracks requires Platform Admin approval + audit trail.

---

## 👥 SECTION 3 — CAMPUS ROLE HIERARCHY (DAG — NO CIRCULAR INHERITANCE)

All roles conform to a Directed Acyclic Graph. Single root authority. No flat-role system. No implicit privilege inheritance.

```
CAMPUS ROLE TREE (per tenant):

Tier 0 (Platform)
  └── System_Super_Admin
        └── Platform_Admin
              └── Tenant_Compliance_Officer

Tier 1 (Institute Sovereign)
  └── Institute_Owner / University_Administrator / College_Administrator
        ├── Institute_ERP_Admin
        ├── Institute_Compliance_Officer
        ├── Institute_IT_Admin
        └── Virtual_Campus_Manager

Tier 2 (Academic Authority)
  └── Academic_Dean
        ├── Curriculum_Designer
        ├── Examination_Controller
        ├── Research_Coordinator
        └── Accreditation_Manager

Tier 3 (Operations & Placement)
  ├── Institute_Placement_Officer (TPO)
  │     ├── Campus_Hiring_Manager
  │     └── Industry_Liaison_Officer
  ├── Institute_HR
  ├── Institute_Finance_Officer
  ├── Institute_Operations_Manager
  ├── Institute_LMS_Admin
  ├── Institute_ATS_Admin
  └── Internship_Coordinator

Tier 4 (Student-Facing Roles)
  ├── Institute_Counselor
  ├── Student_Success_Manager
  ├── Institute_Admissions_Manager
  ├── Alumni_Manager
  └── International_Admissions_Officer

Tier 5 (Educators & Evaluators)
  ├── Trainer / Subject_Trainer / Technical_Instructor
  └── Evaluator / Assessment_Examiner

Tier 6 (Students by Track)
  ├── Student_Arts
  ├── Student_Commerce
  ├── Student_Science
  ├── Student_Technology
  └── Student_Administration

Tier 7 (Trust & Observation Layer)
  └── Parent (Read-Only, Trust Layer — NEVER write access)

Tier 8 (Non-Human)
  └── AUTOMATION / AI_AGENTS (declared, non-human actors)
```

```
RULES:
  - No role may access data above its tier without JIT escalation + MFA + audit
  - Parent role = READ-ONLY on their linked child's records only
  - No admin UI exposed to Student tier
  - No ERP controls exposed to Educators or Students
  - Cross-domain role combination requires explicit conflict declaration
  - Institute roles are tenant-bound — they have no authority outside their tenant
```

---

## ⚙ SECTION 4 — CORE CAMPUS MODULES (ALL REQUIRED, NONE OPTIONAL)

---

### 📋 4A — STUDENT ONBOARDING & IDENTITY ENGINE

```
MODULE_ID               = CA::ONBOARD::001
SCOPE                   = All student tiers (School → PhD)
VERIFICATION_REQUIRED   = Institution email domain match OR institution ID upload
IDENTITY_LOCK           = Student bound to single primary institution tenant
MULTI_INSTITUTION       = Allowed with explicit cross-enroll consent + admin approval
MINOR_PROTECTION        = ENABLED — students under 18 require parent consent at join
DOMAIN_ASSIGNMENT       = At onboarding (Arts / Commerce / Science / Tech / Admin)
DOMAIN_SWITCH           = Requires Academic_Dean approval + audit trail
LIFECYCLE_STATES        = Registered → Verified → Active → Restricted → Archived → Deleted
KYC_LEVEL               = Email + Phone + Institution ID (minimum)
PARENT_LINK             = Auto-invite on minor student verification
PROFILE_COMPLETENESS    = Gated — features unlock progressively with % complete
```

**Verification Algorithm (LOCKED):**

```
IF institution_email_domain MATCHES verified_institution.domain
  → verified_status = TRUE
  → domain_track assigned
  → parent_invite_triggered IF age < 18
ELSE IF institution_id_document UPLOADED AND admin_approved
  → verified_status = TRUE
ELSE
  → verified_status = PENDING
  → feature_access = RESTRICTED
```

**Database Entities (LOCKED):**

```
AG_TABLE: student_profiles
  Fields: id, tenant_id, institution_id, user_id, domain_track, verified_status,
          influence_score, streak_count, belt_level, created_at, updated_at

AG_TABLE: student_verifications
  Fields: id, student_id, verification_type, institution_email,
          document_ref, verified_at, verified_by, status

AG_TABLE: parent_links
  Fields: id, student_id, parent_user_id, consent_given, consent_at,
          link_status, visibility_scope
```

---

### 🎓 4B — ACADEMIC RECORD MANAGEMENT ENGINE

```
MODULE_ID               = CA::ACADEMIC::002
SCOPE                   = All students + Institute admins + Academic Dean + TPO
ACCESS_RULE             = Student sees own records only
                          TPO sees cohort-level aggregated records
                          Dean sees institution-level records
                          Parent sees linked child's records (read-only)
                          Employer sees ONLY shared/consented records
RECORD_IMMUTABILITY     = Academic records are APPEND-ONLY after faculty sign-off
RETENTION               = LIFETIME (academic records never auto-deleted)
EXPORT_FORMAT           = PDF (tamper-evident, digitally signed) + JSON (API)
CORRECTION_FLOW         = Student requests → Dean approves → Audit logged
FALSIFICATION           = ZERO TOLERANCE — triggers compliance escalation
DOMAIN_SEPARATION       = Arts cohort records ≠ Science cohort records (hard isolated)
```

**Database Entities (LOCKED):**

```
AG_TABLE: academic_records
  Fields: id, student_id, institution_id, tenant_id, record_type,
          semester, year, subject, grade, credits, issued_by,
          signed_at, hash_signature, created_at

AG_TABLE: academic_transcripts
  Fields: id, student_id, institution_id, generated_at, pdf_url,
          hash, verified_by, shared_with_employer_id, shared_at

AG_TABLE: institution_records
  Fields: id, institution_id, tenant_id, record_type, content,
          published_at, visibility_scope, created_by
```

---

### 🏫 4C — INSTITUTE ERP ENGINE

```
MODULE_ID               = CA::ERP::003
SCOPE                   = Institute_Admin | ERP_Admin | Finance_Officer
                          Operations_Manager | LMS_Admin | ATS_Admin
SUB_MODULES_REQUIRED    = [
  ENROLLMENT_MANAGEMENT,
  ATTENDANCE_TRACKING,
  FEE_COLLECTION (→ delegates to FEE_MANAGEMENT_AGENT),
  TIMETABLE_MANAGEMENT,
  EXAMINATION_MANAGEMENT,
  ACCREDITATION_MANAGEMENT,
  COMPLIANCE_REPORTING,
  LIBRARY_MANAGEMENT,
  HOSTEL_MANAGEMENT,
  ALUMNI_MANAGEMENT
]
INTEGRATION_MANDATE     = Bi-directional sync with FEE_MANAGEMENT_AGENT
                          Read integration with ANALYTICS_SERVICE
                          Event-driven via Kafka (all state changes emit events)
AUDIT_TRAIL             = Immutable — every ERP action logged with actor + timestamp
TENANT_BOUNDARY         = HARD — no cross-institute ERP access at any layer
```

**ERP Sub-Module Detail:**

| Sub-Module | Key Functions | Approval Authority | Audit Required |
|---|---|---|---|
| Enrollment Management | Register, approve, defer, withdraw students | Admin + Dean | Yes |
| Attendance Tracking | Daily/session attendance, exception alerts | Operations Manager | Yes |
| Fee Collection | Fee structure, payment tracking, receipts | Finance Officer → FEE_AGENT | Yes |
| Timetable Management | Semester timetables, room allocation | Operations Manager | Yes |
| Examination Management | Exam schedules, hall tickets, result processing | Examination Controller | Yes |
| Accreditation Management | Document vault, audit prep, NAAC/NBA compliance | Compliance Officer | Yes |
| Library Management | Book catalog, issue/return, digital resources | Library Admin | Yes |
| Hostel Management | Room allotment, fee, warden alerts | Operations Manager | Yes |
| Alumni Management | Alumni registry, engagement, referrals | Alumni Manager | Yes |
| Compliance Reporting | Regulatory reports, statutory filings | Compliance Officer | Yes |

---

### 📊 4D — TPO DASHBOARD & PLACEMENT ENGINE

```
MODULE_ID               = CA::TPO::004
SCOPE                   = Institute_Placement_Officer | Campus_Hiring_Manager
                          Industry_Liaison_Officer | Academic_Dean (read)
                          Enterprise_Recruiter (limited view — consented students only)
AI_ADVISES_ONLY         = TRUE  // AI placement predictions are advisory, not decisive
PLACEMENT_GUARANTEE     = FORBIDDEN to represent on platform
COHORT_ANALYTICS        = Aggregated + anonymized until individual consent given
EMPLOYER_ACCESS         = Employers see only students who have opted into visibility
DATA_SHARED_WITH_EMPLOYER = Verified skills, portfolio, belt level, match score ONLY
                             Raw academic records, personal contact = FORBIDDEN without consent
OFFER_LOCKING           = Accepted offers locked + audit trail + student confirmation
RELIABILITY_SCORE       = SME and corporate recruiters carry reliability score (from Job Portal Engine)
```

**TPO Dashboard Metrics (LOCKED):**

```
REQUIRED_METRICS:
  → cohort_hiring_readiness_score     // AI-computed, per cohort
  → placement_rate_by_domain          // Arts / Commerce / Science breakdown
  → employer_engagement_index         // active recruiters × interaction depth
  → offer_acceptance_rate             // accepted / total offers
  → avg_days_to_placement             // from profile-active to offer-accepted
  → skill_gap_heatmap                 // cohort skills vs industry demand
  → domain_demand_forecast            // 30/60/90-day hiring trend per domain
  → student_readiness_distribution    // bell curve across cohort
  → top_recruiters_by_placement_count // leaderboard of active employers
  → student_dropout_risk_index        // AI-flagged students needing intervention
```

**Database Entities (LOCKED):**

```
AG_TABLE: placement_records
  Fields: id, student_id, institution_id, employer_id, job_id,
          offer_date, offer_amount, accepted, joined_at, audit_trail

AG_TABLE: cohort_analytics_snapshots
  Fields: id, institution_id, cohort_id, domain_track, snapshot_date,
          hiring_readiness_score, placement_rate, skill_gap_json

AG_TABLE: employer_campus_access
  Fields: id, employer_id, institution_id, access_granted_at,
          access_scope, granted_by, expires_at
```

---

### 👨‍👩‍👧 4E — PARENT TRUST LAYER ENGINE

```
MODULE_ID               = CA::PARENT::005
SCOPE                   = Parent role (read-only, trust layer)
ACCESS_MODEL            = Parents linked to exactly ONE student (their child)
                          Multiple children = multiple separate parent accounts or
                          single account with child-selector
WRITE_ACCESS            = ABSOLUTELY FORBIDDEN — parent role has zero write permissions
READ_SCOPE              = Child's academic progress, attendance, placement status,
                          fee payment status, skill progress, belt level, streak
SENSITIVE_DATA_BLOCKED  = Child's private messages, peer interactions,
                          assessment raw scores (unless institute policy allows)
CONSENT_MODEL           = Student (if 18+) must consent to parent visibility
                          Minor students have automatic parent link (guardian consent)
NOTIFICATION_TRIGGERS   = Fee due alert, attendance drop below threshold,
                          placement offer received, academic record updated,
                          belt level change, streak broken for 7+ days
MFA_REQUIRED            = Yes, for parent account login
PARENT_PORTAL_UI        = Separate, simplified view — no admin controls visible
```

**Database Entities (LOCKED):**

```
AG_TABLE: parent_links
  Fields: id, parent_user_id, student_id, institution_id, tenant_id,
          consent_given, consent_at, visibility_scope, link_status,
          created_at, expires_at

AG_TABLE: parent_notification_prefs
  Fields: id, parent_user_id, student_id, notification_type,
          channel (email/sms/push), enabled, threshold_value
```

---

### 🌐 4F — CAMPUS COMMUNITY & SOCIAL FEED ENGINE

```
MODULE_ID               = CA::COMMUNITY::006
SCOPE                   = Verified students within same institution tenant
CROSS_INSTITUTION_FEED  = FORBIDDEN — campus groups are institution-scoped
CONTENT_MODERATION      = AI-assisted flagging + human moderator approval
POST_VISIBILITY         = Campus groups → institution members only
                          Public posts → platform-wide (with explicit student consent)
ANTI_CHEAT_IN_COMMUNITY = Academic content sharing rules enforced
                          (no exam paper sharing, no plagiarism propagation)
MINORS_IN_COMMUNITY     = Enhanced content filters + no DM to unverified users
MODERATION_ESCALATION   = Institute_Compliance_Officer → Platform_Moderation
```

**Community Sub-Systems (ALL REQUIRED):**

```
SUB_SYSTEM: Campus Feed
  → Student posts, reactions, comments, tags
  → Visibility: institution-scoped by default

SUB_SYSTEM: Campus Groups
  → Domain-bound (Arts group, Science group, etc.)
  → Group admin = Student_Success_Manager or Senior Student (approved)
  → Max group size configurable per institution

SUB_SYSTEM: Study Rooms
  → Linked to courses, institution-scoped
  → Viral rule: creator must invite ≥1 peer before room activates
  → Shared notes visible only to same-institution students

SUB_SYSTEM: Peer Quiz Challenge
  → Student-to-student quiz challenges
  → Challenge recipient must be verified student to respond
  → Anti-cheat: no re-use of exam content

SUB_SYSTEM: Peer Project Board
  → Real-world project teams formed within institution
  → Team formation rule: project active only after required_members joined
  → Projects evidence-linked to portfolio (→ Project Execution Engine)
```

**Database Entities (LOCKED):**

```
AG_TABLE: campus_posts
  Fields: id, author_student_id, institution_id, tenant_id, content,
          visibility_scope, moderation_status, created_at

AG_TABLE: campus_groups
  Fields: id, institution_id, tenant_id, name, domain_track,
          admin_student_id, member_count, created_at

AG_TABLE: group_memberships
  Fields: id, group_id, student_id, role, joined_at

AG_TABLE: study_rooms
  Fields: id, course_id, institution_id, created_by, activated_at,
          member_count, created_at

AG_TABLE: peer_quiz_challenges
  Fields: id, quiz_id, from_student_id, to_student_id, status,
          initiated_at, responded_at

AG_TABLE: peer_projects
  Fields: id, institution_id, created_by_student_id, title,
          required_members, current_members, status, created_at
```

---

### 🏆 4G — CAMPUS AMBASSADOR PROGRAM ENGINE

```
MODULE_ID               = CA::AMBASSADOR::007
SCOPE                   = Verified students (any institution)
ELIGIBILITY_RULES       = Active for ≥ 30 days + Belt Level ≥ 2 + no active violations
APPLICATION_FLOW        = Student applies → TPO/Institute reviews → Platform approves
AMBASSADOR_BENEFITS     = Free Premium subscription + Referral tracking dashboard
                          Exclusive merch credits + Monthly prize for top ambassador
REFERRAL_TRACKING       = Unique referral code per ambassador (format: {NAME}{YEAR})
REFERRAL_REWARD         = Referrer + referee both get 500 points on verified signup
PREMIUM_UNLOCK          = After 5 verified referrals → premium trial extended
ANTI_ABUSE              = Referral must result in verified student (not just signup)
                          Same device/IP chain → flag for human review
LEADERBOARD             = Public campus ambassador leaderboard per institution
MONTHLY_PRIZE_TRIGGER   = Top ambassador by referral count + engagement score
BADGE                   = "Campus Champion" badge on profile
DEMOTION_TRIGGER        = Violation, inactivity (30+ days), or fraudulent referrals
AMBASSADOR_AUDIT        = All referral events logged + monthly review
```

**Database Entities (LOCKED):**

```
AG_TABLE: campus_ambassadors
  Fields: id, student_id, institution_id, tenant_id, referral_code,
          status, approved_at, approved_by, total_referrals,
          total_points_earned, badge_awarded, created_at

AG_TABLE: ambassador_referrals
  Fields: id, ambassador_id, referred_user_id, referral_code,
          signup_at, verified_at, points_awarded, fraud_flag
```

---

### 📈 4H — STUDENT IDENTITY, BELT & REPUTATION ENGINE

```
MODULE_ID               = CA::REPUTATION::008
SCOPE                   = All students across all institution tenants
BELT_SYSTEM             = Inherited from Gamification Engine (non-duplicated)
INFLUENCE_SCORE_FORMULA = (courses_completed × 10) + (projects_posted × 15)
                          + (endorsements_received × 5) + (dojo_wins × 20)
                          + (streak_days × 2) + (ambassador_referrals × 8)
LEADERBOARD             = Per-institution leaderboard, refreshed daily
PUBLIC_PORTFOLIO        = SEO-indexable public page (React/Next.js) per student
PORTFOLIO_ITEMS         = Skills, Courses, Projects, Certificates, Dojo Records
ENDORSEMENTS            = Peer-to-peer skill endorsements (verified students only)
BADGE_AWARD_ENGINE      = Event-driven — evaluates criteria_rule on Kafka activity stream
REPUTATION_DECAY        = Inactivity > 60 days → influence_score frozen (no decay)
                          Violation → score reduction per moderation engine rules
SCORE_EXPLAINABILITY    = Every score change logged with reason (ABAC: student can view own log)
```

**Influence Score Algorithm (LOCKED):**

```
# Evaluated on every Kafka activity event:
influence_score =
  (courses_completed   × 10) +
  (projects_posted     × 15) +
  (endorsements_recv   ×  5) +
  (dojo_match_wins     × 20) +
  (streak_days_active  ×  2) +
  (ambassador_refs     ×  8) +
  (peer_kudos_received ×  3)

# Leaderboard: ORDER BY influence_score DESC per institution_id
# Score update: triggered by Kafka event → Analytics Service → Reputation Engine
```

---

### 🔁 4I — STREAK, HABIT & DAILY RETENTION ENGINE

```
MODULE_ID               = CA::RETENTION::009
SCOPE                   = All students
STREAK_DEFINITION       = At least 1 qualifying activity logged per calendar day
QUALIFYING_ACTIVITIES   = Course lesson completed | Dojo match played |
                          Project milestone submitted | Study room session |
                          Post created | Quiz challenge sent/responded |
                          Campus group interaction
STREAK_RESET            = Missed day → streak resets to 0
MAX_STREAK              = No cap — tracked and displayed
REWARD_TRIGGER          = Every 7-day streak → RewardBox granted
REWARD_BOX_CONTENTS     = Platform credits | Badge | Premium trial days |
                          Profile boost | Ambassador fast-track eligibility
GRACE_PERIOD            = 1 grace day per 30-day period (streak freeze, not reset)
NOTIFICATION_SCHEDULE   = In-app + push at 6PM local time if no activity logged that day
PARENT_ALERT            = Streak broken for 7+ consecutive days → parent notified
                          (if parent link active + parent notification prefs enabled)
```

**Database Entities (LOCKED):**

```
AG_TABLE: student_streaks
  Fields: id, student_id, current_streak, max_streak, last_activity_date,
          grace_used_this_month, updated_at

AG_TABLE: daily_activities
  Fields: id, student_id, activity_date, activity_type, entity_id,
          points_earned, created_at

AG_TABLE: reward_boxes
  Fields: id, student_id, reward_type, granted_at, opened_at, contents_json
```

---

### 🔔 4J — CAMPUS NOTIFICATION & ALERT ENGINE

```
MODULE_ID               = CA::NOTIFY::010
SCOPE                   = All campus roles (student, parent, TPO, dean, admin)
CHANNELS                = In-app (primary) + Push (FCM) + Email (Postal) + SMS (fallback)
CRITICAL_ALERTS         = Fixed (40% dashboard) — cannot be hidden or dismissed
                          without acknowledgment
NOTIFICATION_CATEGORIES = [
  ACADEMIC    : Grade posted, exam schedule, result published
  PLACEMENT   : New job match, interview invite, offer received
  COMMUNITY   : Tag, mention, group invite, quiz challenge
  STREAK      : Daily reminder, streak milestone, streak broken
  COMPLIANCE  : Fee due, KYC expiry, document required
  PARENT      : All parent-scoped alerts (child's activity summary)
  ADMIN       : Accreditation deadline, compliance flag, ERP alert
  SYSTEM      : Platform maintenance, security alert
]
BATCHING                = Non-critical notifications batched (max 3/hour per channel)
CRITICAL_NOTIFICATIONS  = Delivered immediately, no batching
NOTIFICATION_AUDIT      = All notifications logged (sent, delivered, opened)
UNSUBSCRIBE_RULE        = Critical compliance and security alerts are NON-unsubscribable
DO_NOT_DISTURB          = Supported per user — critical alerts override DND
```

---

## 🗄 SECTION 5 — DATA ARCHITECTURE (POSTGRESQL + KAFKA LOCKED)

```
# ── CAMPUS_AGENT :: DATABASE SCHEMA (LOCKED) ──────────────────────────────────

# ── CORE CAMPUS TABLES ────────────────────────────────────────────────────────

AG_TABLE: institutions
  Fields: id, tenant_id, name, type (school/college/university/iti/coaching/online),
          domain_email, verified, accreditation_status, domain_tracks[],
          seat_count, created_at, updated_at

AG_TABLE: cohorts
  Fields: id, institution_id, tenant_id, name, domain_track, academic_year,
          start_date, end_date, status, student_count

AG_TABLE: enrollments
  Fields: id, student_id, institution_id, cohort_id, tenant_id,
          enrolled_at, status, approved_by, exit_date, exit_reason

AG_TABLE: attendance_records
  Fields: id, student_id, institution_id, session_date, session_type,
          present, marked_by, created_at

AG_TABLE: examination_schedules
  Fields: id, institution_id, cohort_id, subject, exam_date, room,
          duration_minutes, published_at, created_by

AG_TABLE: exam_results
  Fields: id, student_id, examination_id, marks_obtained, max_marks,
          grade, published_at, signed_by, hash_signature

AG_TABLE: alumni_registry
  Fields: id, student_id, institution_id, graduation_year, domain_track,
          current_employer, current_role, opted_in_mentoring, created_at

# ── CAMPUS COMMUNITY TABLES ───────────────────────────────────────────────────

AG_TABLE: student_badges
  Fields: id, name, criteria_rule, icon_ref, domain_scope, created_at

AG_TABLE: student_badge_awards
  Fields: id, student_id, badge_id, awarded_at, trigger_event

AG_TABLE: student_portfolio_items
  Fields: id, student_id, item_type (skill/course/project/certificate/dojo),
          reference_id, visibility (public/institution/private), created_at

AG_TABLE: student_endorsements
  Fields: id, from_student_id, to_student_id, skill_id, institution_id,
          created_at, is_verified

AG_TABLE: student_leaderboard_snapshots
  Fields: id, institution_id, cohort_id, student_id, rank_position,
          influence_score, snapshot_date

# ── PLACEMENT TABLES ──────────────────────────────────────────────────────────

AG_TABLE: placement_drives
  Fields: id, institution_id, employer_id, drive_date, domain_track,
          eligible_cohort_id, created_by, status

AG_TABLE: job_applications_campus
  Fields: id, student_id, placement_drive_id, job_id, applied_at,
          status, shortlisted, interview_at, offer_received

# ── AUDIT TABLE ───────────────────────────────────────────────────────────────

AG_TABLE: campus_audit_log
  Fields: id, entity_type, entity_id, action, actor_id, actor_role,
          tenant_id, before_state, after_state, timestamp, ip_address, trace_id

# ── KAFKA TOPICS (ALL CAMPUS STATE CHANGES EMIT) ──────────────────────────────

AG_TOPICS:
  ecoskiller.campus.student.enrolled
  ecoskiller.campus.student.verified
  ecoskiller.campus.student.streak_updated
  ecoskiller.campus.student.influence_score_updated
  ecoskiller.campus.student.badge_awarded
  ecoskiller.campus.academic_record.published
  ecoskiller.campus.exam_result.published
  ecoskiller.campus.placement.offer_received
  ecoskiller.campus.placement.offer_accepted
  ecoskiller.campus.parent.alert_triggered
  ecoskiller.campus.ambassador.referral_verified
  ecoskiller.campus.community.post_created
  ecoskiller.campus.community.moderation_flagged
  ecoskiller.campus.erp.enrollment_approved
  ecoskiller.campus.erp.attendance_alert
  ecoskiller.campus.system.halt

# ─────────────────────────────────────────────────────────────────────────────
```

---

## 🔗 SECTION 6 — MICROSERVICE INTEGRATION MAP

| Upstream Service | Event / Trigger | CAMPUS_AGENT Action | Downstream Notified |
|---|---|---|---|
| Auth Service | Student registers with institution email | Trigger verification flow | Student + Institution Admin |
| FEE_MANAGEMENT_AGENT | Fee payment confirmed | Update enrollment status | ERP + Student + Parent |
| FEE_MANAGEMENT_AGENT | Fee overdue alert | Flag student account + alert parent | Parent + Finance Officer |
| Job Portal Engine | Job posted targeting campus | Push to TPO Dashboard + eligible students | TPO + Matching Students |
| Skill Engine | Course completed by student | Update influence_score + portfolio | Reputation Engine + Student |
| Dojo Engine | GD match completed | Update influence_score + belt level | Gamification Engine |
| Gamification Engine | Streak milestone hit | Award badge + reward box | Notification Service |
| Analytics Service | Weekly report cycle | Generate cohort performance snapshots | TPO + Dean + Admin |
| Compliance Engine | Audit request | Export campus_audit_log + records | Compliance Dashboard |
| Notification Service | All campus event types | Route to correct channel per user prefs | End Users |
| Project Execution Engine | Project milestone verified | Add to student portfolio | Portfolio + Reputation |
| Admin Governance | Accreditation document request | Compile + export evidence pack | Institute Compliance Officer |

---

## 🧠 SECTION 7 — AI INTELLIGENCE LAYER (ADVISE-ONLY, NEVER DECIDE)

```
AI_FUNCTIONS (CAMPUS DOMAIN):
  → Placement Probability      : Score per student (1–100) vs active job requirements
  → Hiring Readiness Index     : Cohort-level readiness score for TPO dashboard
  → Skill Gap Heatmap          : Cohort skills vs industry demand (live feed from Job Engine)
  → Student Dropout Risk       : Flag students with declining streak + engagement score
  → Domain Demand Forecast     : 30/60/90-day hiring trend per domain track
  → Ambassador Fraud Detection : ML model on referral chain patterns
  → Attendance Anomaly         : Alert if student attendance drops below threshold
  → Peer Learning Recommendations: Suggest study room / quiz challenge partners
  → Portfolio Strength Score   : AI audit of student portfolio completeness + quality
  → Cohort Ranking Explanation : Natural language explanation of leaderboard position

RULE: AI outputs stored in ai_campus_recommendations table.
RULE: AI NEVER admits, rejects, grades, places, or ranks students autonomously.
RULE: Every AI recommendation shown to TPO/Dean/Admin with confidence score + data source.
RULE: AI model inputs/outputs fully logged for explainability and ABAC audit.
RULE: AI bias audit quarterly — mandatory, results immutable.
```

---

## 🛡 SECTION 8 — COMPLIANCE, SECURITY & REGULATORY LOCK

> **All rules below INHERITED from Master Prompt. Gravity-locked. No re-negotiation.**

| Compliance Domain | Standard / Rule | Campus Implementation | Audit Frequency |
|---|---|---|---|
| Student Data Privacy | DPDP Act 2023 + GDPR | Student PII masked in logs, explicit consent at each data share | Quarterly |
| Minor Protection | DPDP + POCSO awareness | Enhanced content filters, parent mandatory link for <18 | Continuous |
| Academic Integrity | Institution policy + Platform rules | Append-only records, anti-plagiarism in community | Per-submission |
| Accreditation Compliance | NAAC / NBA / UGC (India) | Document vault, audit prep module, compliance reporting | Annual + On-demand |
| Anti-Discrimination | Fairness Lock (EOAD-21) | No donor/payment link to admissions, blind cohort selection | Per-admission cycle |
| Data Retention | Companies Act + DPDP | Academic records: lifetime. Logs: 7 years. Financial: 10 years | Annual review |
| Tenant Isolation | Master Prompt HARD LOCK | Row-level security PostgreSQL, Kafka topic namespacing | Continuous |
| Audit Immutability | Master Prompt Compliance ERP | campus_audit_log: WORM + hash-chained + Kafka replay | Continuous |
| RBAC + ABAC | Inherited from Auth Service | No campus action without role + attribute + context check | Per-request |
| Academic Record Correction | Dean-approved only | Correction request → Dean approval → audit trail | Per-event |

---

## 📡 SECTION 9 — API INTERFACE CONTRACT (LOCKED)

```
# ── CAMPUS_AGENT :: REST API SURFACE ──────────────────────────────────────────

BASE_PATH = /api/v1/campus

# STUDENT ONBOARDING & IDENTITY
POST   /students/register                    → Register student to institution
POST   /students/verify                      → Trigger email/ID verification
GET    /students/{id}/profile                → Get student profile
PATCH  /students/{id}/domain-track          → Request domain track change (Dean-gated)

# ACADEMIC RECORDS
GET    /students/{id}/academic-records       → Get student academic records
GET    /students/{id}/transcript             → Get/generate transcript PDF
POST   /academic-records/correction-request  → Student requests correction
POST   /academic-records/{id}/approve        → Dean approves correction

# INSTITUTE ERP
POST   /institutes/{id}/enroll              → Enroll student to cohort
DELETE /institutes/{id}/enroll/{student_id} → Withdraw student (admin-gated)
GET    /institutes/{id}/cohorts             → List all cohorts
POST   /attendance/mark                      → Mark attendance session
GET    /attendance/{student_id}             → Get student attendance history
GET    /institutes/{id}/erp/dashboard       → Full ERP dashboard data

# TPO & PLACEMENT
GET    /tpo/{institution_id}/dashboard      → TPO analytics dashboard
GET    /tpo/{institution_id}/cohort-readiness → Hiring readiness per cohort
POST   /placement-drives/create             → Create campus placement drive
GET    /placement-drives/{id}/applicants    → List applicants for drive
POST   /placement/offer/lock                → Lock accepted offer (audit trail)

# PARENT LAYER
POST   /parents/link                        → Link parent to student
GET    /parents/{id}/child-summary          → Parent dashboard (child's summary)
GET    /parents/{id}/notifications          → Parent notification feed

# CAMPUS COMMUNITY
POST   /posts/create                        → Create campus post
POST   /groups/{id}/join                    → Join campus group
GET    /groups/{id}/feed                    → Get group feed
POST   /studyrooms/create                   → Create study room
POST   /quiz/{id}/challenge                 → Send peer quiz challenge

# AMBASSADOR
POST   /ambassador/apply                    → Apply for ambassador program
GET    /ambassador/{institution_id}/leaderboard → Ambassador leaderboard
POST   /ambassador/referral/validate        → Validate referral signup

# REPUTATION & LEADERBOARD
GET    /students/{id}/influence-score       → Get student influence score breakdown
GET    /institutions/{id}/leaderboard       → Institution student leaderboard
GET    /students/{id}/portfolio             → Get student portfolio
POST   /students/{id}/endorse              → Peer endorse a skill

# STREAK & RETENTION
POST   /activity/log                        → Log qualifying activity
GET    /students/{id}/streak               → Get streak data + history
POST   /challenges/join                     → Join daily/weekly challenge
POST   /rewards/open                        → Open reward box

# ANALYTICS
GET    /analytics/campus/{institution_id}/placement  → Placement analytics
GET    /analytics/campus/{institution_id}/skill-gaps → Skill gap heatmap
GET    /analytics/campus/{institution_id}/engagement → Community engagement metrics

# ALL ENDPOINTS:
# JWT required | RBAC + ABAC checked | Rate-limited | Audit-logged | Tenant-bound
# ──────────────────────────────────────────────────────────────────────────────
```

---

## 📊 SECTION 10 — DASHBOARD & UI RULES (MASTER PROMPT INHERITED)

Campus dashboards follow the **60/40 customization rule**. Flutter is PRIMARY UI. React/Next.js is READ-ONLY SEO clone. No decorative UI. No cross-module UI mixing.

| Dashboard | Audience | Customizable (60%) | Fixed (40%) |
|---|---|---|---|
| Student Home | Student | Widget order, shortcut actions, community feed | Identity block, belt level, streak, compliance alerts |
| TPO Analytics | Placement Officer | Metric widgets, date ranges, cohort filters | Cohort readiness score, AI risk flags, placement rate |
| Institute ERP | ERP Admin | Module order, report formats, alert thresholds | Tenant identity, compliance status, critical ERP alerts |
| Academic Dean View | Academic Dean | Domain filter, cohort selector, export format | Institution-level KPIs, record integrity status |
| Parent Portal | Parent | Notification prefs, alert thresholds | Child identity, fee status, attendance, placement status |
| Campus Community | Student | Feed algorithm preference, group order | Moderation notices, compliance badges, identity |
| Ambassador Hub | Ambassador Student | Referral goal tracker, leaderboard view | Referral code, total referrals, badge, reward status |
| Alumni Dashboard | Alumni Manager | Filter by graduation year, domain, employer | Alumni count, engagement rate, mentoring opt-ins |

---

## 🚨 SECTION 11 — ANTIGRAVITY FAILURE PROTOCOL & EXECUTION HALT CONDITIONS

> The following conditions cause **IMMEDIATE EXECUTION HALT** within the CAMPUS_AGENT.
> **No graceful degradation. No creative resolution. No assumption. HALT + ALERT + AWAIT HUMAN.**

```
🔴 HALT CONDITION 01: Cross-tenant campus data accessed or returned
                      → IMMEDIATE HALT + SECURITY ALERT

🔴 HALT CONDITION 02: Academic record mutated outside append-only flow
                      (i.e., direct DB edit, not Dean-approved correction flow)
                      → IMMEDIATE HALT + ACADEMIC INTEGRITY ALERT

🔴 HALT CONDITION 03: Parent account granted write access to any entity
                      → IMMEDIATE HALT + RBAC VIOLATION ALERT

🔴 HALT CONDITION 04: AI placement recommendation triggers automatic offer/rejection
                      without human approval step
                      → HALT + COMPLIANCE ESCALATION

🔴 HALT CONDITION 05: Student data shared with employer without verified consent
                      → HALT + PRIVACY BREACH ALERT + LEGAL ESCALATION

🔴 HALT CONDITION 06: Minor student (< 18) linked to community without parent consent
                      → HALT + MINOR PROTECTION ALERT

🔴 HALT CONDITION 07: Campus audit log write fails
                      → HALT ALL CAMPUS WRITE OPERATIONS

🔴 HALT CONDITION 08: RBAC / ABAC check bypassed or returns error on campus endpoint
                      → HALT + SECURITY ALERT + SESSION REVOCATION

🔴 HALT CONDITION 09: Domain track data leaks between Arts / Commerce / Science cohorts
                      → HALT + DOMAIN ISOLATION VIOLATION ALERT

🔴 HALT CONDITION 10: Ambassador referral chain validated without verified signup check
                      → HALT + FRAUD FLAG + AMBASSADOR REVIEW TRIGGERED

🔴 HALT CONDITION 11: Institute ERP action executed without tenant-bound token
                      → HALT + TENANT ISOLATION FAILURE ALERT

🔴 HALT CONDITION 12: Exam result published without Examination_Controller sign-off + hash signature
                      → HALT + ACADEMIC INTEGRITY ESCALATION

ON HALT → Emit Kafka event  : ecoskiller.campus.system.halt
ON HALT → Notify            : Platform Admin + Compliance Engine + Institute Admin < 60 seconds
ON HALT → Log               : Full trace to immutable campus_audit_log
ON HALT → Resume condition  : Human acknowledgment required from ≥ 2 authorized roles
```

---

## 🔄 SECTION 12 — FOUR-STAGE DEPLOYMENT ENFORCEMENT (INHERITED)

```
STAGES MUST EXECUTE SEQUENTIALLY — SKIPPING = INVALID BUILD

STAGE 1: FOUNDATION (Campus)
  → Institution tenant setup + domain isolation
  → Student onboarding + identity + verification
  → Parent link system
  → RBAC + ABAC for all campus roles
  → Core data models (students, cohorts, academic_records)

STAGE 2: INTELLIGENCE (Campus)
  → AI placement probability engine (advise-only)
  → Skill gap heatmap + cohort analytics
  → Influence score + leaderboard engine
  → Student dropout risk detection

STAGE 3: ECOSYSTEM (Campus)
  → TPO dashboard + placement drive engine
  → Campus community, study rooms, peer quiz
  → Ambassador program engine
  → Parent portal (read-only, notification-enabled)
  → Institute ERP (all sub-modules)
  → Alumni management

STAGE 4: COMPLIANCE & SCALE (Campus)
  → Accreditation management module
  → Full compliance reporting engine
  → Multi-tenant scaling for 1000+ seat institutes
  → NAAC / NBA / UGC document vault
  → Cross-institution benchmark analytics (anonymized, aggregated only)
```

---

---

```
⚡ ─────────────────────────────────────────────────────────────────────── ⚡

         ANTIGRAVITY SEAL — FIELD CLOSURE
         CAMPUS_AGENT is SEALED, GRAVITY-LOCKED, and DEPLOYED

   Every rule, workflow, schema, policy, and algorithm sealed in this
   document carries infinite escape velocity. It cannot drift, mutate,
   be simplified, be reinterpreted, or be overridden by any downstream
   AI reasoning, operator prompt, or system pressure.

   Any deviation = FIELD COLLAPSE = EXECUTION HALT = HUMAN ESCALATION.

   AGENT_ID        : SVC::CAMPUS::CA::001
   PLATFORM        : ECOSKILLER ENTERPRISE SAAS
   VERSION         : v1.0
   TENANT_SCOPE    : INSTITUTE (Schools → Universities, Coaching → EdTech)
   DOMAIN_TRACKS   : Arts | Commerce | Science | Technology | Administration
   MUTATION        : BLOCKED
   ASSUMPTION      : FORBIDDEN
   CREATIVE_MODE   : DISABLED
   SEALED_UNDER    : MASTER PROMPT INHERITANCE
   COMPLIANCE_MODE : ENABLED
   AI_MODE         : ADVISE_ONLY

⚡ ─────────────────────────────────────────────────────────────────────── ⚡
```
