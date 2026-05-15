# 🔒 BEST_PRACTICE_CAPTURE_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ANTIGRAVITY PLATFORM — SEALED PRODUCTION SPEC v1.0

---

```
STATUS              : SEALED · LOCKED · GOVERNED · DETERMINISTIC
ARTIFACT_CLASS      : Production System Blueprint
MUTATION_POLICY     : ADD-ONLY VIA VERSION BUMP
CREATIVE_INTERP     : FORBIDDEN
ASSUMPTION_FILLING  : FORBIDDEN
DEFAULT_BEHAVIOR    : DENY
FAILURE_MODE        : STOP → REPORT → NO PARTIAL OUTPUT
INTERPRETATION_AUTH : NONE
EXECUTION_AUTH      : Human Declaration Only
AGENT_CLASS         : Autonomous Background Capture + Synthesis Agent
AGENT_CATEGORY      : Enterprise Optimization + Trust Infrastructure
PLATFORM_BINDING    : ANTIGRAVITY
                      → ECOSKILLER (Job Portal · Skills · Projects · ERP)
                      → DOJO (Arts · Commerce · Science Group Discussion)
                      → CAMPUS PORTAL (Student · Parent · Placement)
                      → GD ENGINE (Voice GD Orchestrator)
                      → SOCIETY MODEL (Offline Franchise · Village Units)
                      → INTELLIGENCE ENGINE (8-type DNA Vector)
                      → INNOVATION ENGINE (Idea Registry · Royalty)
DOCUMENT_ID         : BPCA-ENT-001-v1.0
SEALED_DATE         : March 2026
NEXT_REVIEW         : September 2026 (mandatory)
CLASSIFICATION      : INTERNAL — PLATFORM ARCHITECTURE
```

---

## SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

| Field | Value |
|---|---|
| **Agent Name** | BEST_PRACTICE_CAPTURE_AGENT |
| **Agent ID** | BPCA-ENT-001 |
| **Tier** | Enterprise Optimization Layer |
| **Sub-Layer** | Trust Infrastructure |
| **Scope** | Platform-Wide (All Domains · All Participant Classes · All Service Layers) |
| **Mode** | Passive Observation + Event-Driven Capture + Structured Synthesis + Controlled Distribution |
| **Bias Policy** | Structurally Eliminated — No subjective interpretation in capture |
| **Human Judgment** | NOT PERMITTED in capture or scoring |
| **AI Interpretation** | PERMITTED ONLY for narrative generation (R28-3 class — LLM) |
| **AI Judgment** | NOT PERMITTED in validation, ranking, or trust scoring |
| **Output Type** | Immutable Practice Records + Ranked Indexes + Distribution Events + Trust Attestations |
| **Alignment** | R28, R62, R64, R68, R71, R72, R80, SECTION N |

---

## SECTION 2 — AGENT PURPOSE STATEMENT

> **"The BEST_PRACTICE_CAPTURE_AGENT converts every measurable outcome across the Antigravity platform — placement success, GD compliance excellence, skill progression, innovation approval, mentor effectiveness, society franchise performance, and scoring anomaly resolution — into a permanently indexed, trust-attested, reusable practice record. No opinion enters. No personal identity attaches. Only outcomes, patterns, and reproducible behaviors are captured, ranked, and redistributed as actionable institutional knowledge."**

**This agent serves:**
- Platform reliability (pattern-driven infrastructure decisions)
- Participant outcomes (surfacing what actually works)
- Recruiter trust (company-side best practice attestations)
- Institutional credibility (TPO, Society Coordinator, Franchise proof points)
- Regulatory compliance (audit-grade evidence of platform governance)
- SEO & public trust (Section R80 success story pipeline feeds from this agent)

---

## SECTION 3 — PRACTICE DOMAIN TAXONOMY (LOCKED)

Best practices are captured across **nine platform domains**. Each domain is isolated. Cross-domain inference requires explicit contract.

### Domain Map

| Domain ID | Domain Name | Source System(s) | Practice Class |
|---|---|---|---|
| D-01 | GD Execution Excellence | GD Orchestrator · Jitsi · Redis State Machine | Protocol Compliance · Communication Discipline |
| D-02 | Campus Placement Success | Campus Portal · AI Match Engine · Blockchain Offer Ledger | Academic → Employment Transition |
| D-03 | Skill Development Velocity | Skill Engine · Certification Engine · Belt Engine | Learning Rate · Skill Passport Growth |
| D-04 | Mentor & Trainer Effectiveness | Dojo Match Engine · Mentor Control Engine · Rating Engine | Instruction Quality · Outcome Correlation |
| D-05 | Innovation Pipeline Excellence | Idea Registry · Innovation Scoring Engine · Licensing Contract Service | Idea Quality · Commercialization Success |
| D-06 | Intelligence Growth Trajectory | Intelligence Profile Service · Passive Intelligence Engine · Prediction Engine | Cognitive Development Pattern |
| D-07 | Society Franchise Operational Excellence | Society Service · Workshop Service · Attendance Service · Commission Engine | Offline Program Delivery · Rural Impact |
| D-08 | Enterprise Recruitment Quality | Company KYC System · Trust Score Engine · Salary Truth Validation | Hire-to-Offer Integrity · Offer Rollout Rate |
| D-09 | Platform Governance & Trust | Admin Governance Service · Compliance Service · Audit Service · Dispute Service | Policy Adherence · Incident Resolution |

---

## SECTION 4 — PRACTICE CAPTURE TAXONOMY

### 4.1 Capture Class Definitions

| Class | Name | Method | Trigger | Trust Level |
|---|---|---|---|---|
| CC-01 | Outcome-Confirmed Practice | Automated event detection post measurable outcome | Event bus (Kafka) | HIGHEST |
| CC-02 | Compliance-Pattern Practice | State machine compliance data above statistical threshold | Redis + PostgreSQL query | HIGH |
| CC-03 | Anomaly-Resolved Practice | Successful resolution of a detected system or behavior anomaly | Audit log + resolution event | HIGH |
| CC-04 | Longitudinal-Impact Practice | Multi-period tracking showing sustained positive delta | ClickHouse analytics + longitudinal-impact-service | HIGH |
| CC-05 | Peer-Validated Practice | Pattern endorsed by ≥3 independent participants in same cohort class | Rating Engine + Endorsement Graph | MEDIUM |
| CC-06 | Benchmark-Deviation Practice | Participant/unit performing >1.5 standard deviations above domain mean | Scoring Engine · ClickHouse distribution query | MEDIUM |
| CC-07 | Failure-Inversion Practice | Practice discovered from analyzing root cause of resolved failure | Failure log + audit trace | MEDIUM |
| CC-08 | Regulatory-Evidence Practice | Practice that demonstrably satisfies compliance or legal requirement | Compliance Service · Legal Document Service · Immutable Archive | CRITICAL |

---

### 4.2 Domain-Level Capture Specifications

#### D-01: GD Execution Excellence

```
CAPTURE_TRIGGERS:
  - gd.completed event with score >= domain_mean + 1σ
  - turns_completed == total_turns (100% compliance)
  - interrupt_attempts == 0 across full session
  - network_drop_count == 0 (clean session)
  - open_discussion_balance_score >= 80/100 (no dominance)

CAPTURE_DATA_POINTS:
  - session_id (anonymized)
  - cohort_size
  - topic_category
  - round_structure (intro / rebuttal / conclusion / open)
  - per_round_compliance_rates
  - dominance_penalty_applied (Y/N)
  - deterministic_score
  - network_stability_class (CLEAN | MINOR_DROP | RECOVERED)

PRACTICE_RECORD_TITLE FORMAT:
  "GD Compliance Excellence — [Topic Category] — [Cohort Size] — [Score Bracket]"

FORBIDDEN FROM CAPTURE:
  ❌ Participant names or IDs
  ❌ Accent or language pattern inference
  ❌ Voice content (audio never stored)
  ❌ Silence interpreted as skill deficit (silence = logged, not inferred)
```

#### D-02: Campus Placement Success

```
CAPTURE_TRIGGERS:
  - job.applied + interview.completed + offer.accepted (full pipeline completion)
  - AI match score presented >= 80% AND offer received (AI recommendation validated)
  - blockchain offer verified + joining_probability >= 90%
  - student CGPA < 7.0 + offer received >= ₹5 LPA (low-CGPA exception pattern)
  - bias_incident_detected + resolved + offer_issued (bias recovery pattern)

CAPTURE_DATA_POINTS:
  - student_tier_class (T1 | T2 | T3 college — anonymized)
  - branch_domain (CSE | Mech | Civil | etc.)
  - skill_profile_vector (skills present at time of offer)
  - internship_count (0 | 1 | 2+)
  - project_count
  - certifications_count
  - CGPA_band (6–6.5 | 6.5–7 | 7–8 | 8+)
  - company_type (Product | Service | Startup | Core)
  - package_band (in LPA brackets — not exact figure)
  - rounds_cleared (OA | Technical | HR | Managerial)
  - time_to_offer_days

PRACTICE_RECORD_TITLE FORMAT:
  "Placement Success — [Branch] — [CGPA Band] — [Company Type] — [Package Band]"

CORRELATION_ANALYSIS (Traditional ML — R28-2):
  - Skills most correlated with offer rate per company type
  - CGPA band threshold per company tier
  - Internship presence vs. absence offer delta
  - Mock interview completion rate vs. HR round pass rate

FEEDS INTO:
  → AI Match Engine (skill weight recalibration)
  → Student Dashboard (personalized recommendations)
  → R80 Success Story Generation (milestone detection)
  → Parent Progress Report (evidence base for realistic expectations)
```

#### D-03: Skill Development Velocity

```
CAPTURE_TRIGGERS:
  - belt_advancement_event (Skill Belt Engine)
  - skill_passport.record_written (R72 — Lifetime Skill Passport)
  - certification_issued (third-party + platform-native)
  - course_completion_velocity > cohort_mean (faster than average)
  - skill_obsolescence_risk DOWNGRADED after relearning (R-obsolescence law)

CAPTURE_DATA_POINTS:
  - skill_id + skill_domain (Arts | Commerce | Science | Technology)
  - user_career_stage (Student | Job Seeker | Professional | Mentor)
  - time_to_competency_days
  - learning_path_taken (platform course | external cert | project-based)
  - prerequisite_skills_at_start
  - assessment_score_trajectory (attempt 1 → final)
  - mentor_assisted (Y/N)
  - cohort_learning (Y/N — group vs. solo)

PRACTICE_RECORD_TITLE FORMAT:
  "Skill Velocity — [Skill Name] — [Domain] — [Career Stage] — [Days to Competency]"
```

#### D-04: Mentor & Trainer Effectiveness

```
CAPTURE_TRIGGERS:
  - mentor_session_rating >= 4.5/5 (explicit signal)
  - mentee_outcome_delta > 20% improvement post-session (implicit signal)
  - belt_advancement within 30 days of mentor engagement
  - mentor_misconduct_reports == 0 over 90-day window (negative absence = positive signal)
  - dojo_match_completion_rate >= 95% for sessions conducted by mentor

CAPTURE_DATA_POINTS:
  - mentor_domain + specialization
  - session_structure_type (1:1 | group | tournament-prep)
  - outcome_measurement_window (7 | 14 | 30 days)
  - mentee_career_stage_at_start
  - session_count_to_measurable_outcome
  - communication_medium (video | text | dojo-live)

PRACTICE_RECORD_TITLE FORMAT:
  "Mentor Effectiveness — [Domain] — [Session Type] — [Outcome Delta Bracket]"

ANTI-GAMING RULE:
  Mentor cannot self-nominate practice records.
  Capture ONLY via outcome events on mentee side.
```

#### D-05: Innovation Pipeline Excellence

```
CAPTURE_TRIGGERS:
  - idea_approved by Innovation Scoring Engine (score >= 75/100)
  - idea_licensed (Licensing Contract Service event)
  - royalty_payment_received (first payment confirms commercial validity)
  - similarity_score < 15% (confirmed original — anti-theft engine cleared)
  - idea_improvement_credit issued (iteration to approval cycle)

CAPTURE_DATA_POINTS:
  - idea_category (problem domain)
  - idea_stage_at_first_capture (raw | developed | prototype)
  - submitter_age_band (< 18 | 18–25 | 25–35 | 35+)
  - time_from_submission_to_approval_days
  - originality_score (0–100)
  - feasibility_score
  - market_potential_score
  - iteration_count (number of improvement cycles before approval)
  - licensing_terms_class (standard | extended | custom)

PRACTICE_RECORD_TITLE FORMAT:
  "Innovation Excellence — [Category] — [Age Band] — [Score Bracket]"

CHILD SAFETY BINDING (HARD):
  All practice records involving submitters < 18 are stored with
  guardian_consent_verified = TRUE.
  Absence → REJECT CAPTURE → STOP
```

#### D-06: Intelligence Growth Trajectory

```
CAPTURE_TRIGGERS:
  - intelligence_score_delta > +10 points over 90-day window
  - intelligence_type_unlock (new type activation in 8-type DNA vector)
  - dojo_intelligence_test_completion (100 structured tests) with score >= 80%
  - career_success_probability_increase > 15% (Prediction Engine event)

CAPTURE_DATA_POINTS:
  - intelligence_type_ids (from 8-type vector — anonymized)
  - domain_alignment (Arts | Commerce | Science | Technology | Administration)
  - activity_that_triggered_growth (course | project | tournament | GD | peer-discussion)
  - starting_vector_snapshot
  - ending_vector_snapshot
  - time_window_days
  - career_stage

PRACTICE_RECORD_TITLE FORMAT:
  "Intelligence Growth — [Type Cluster] — [Domain] — [Delta Band]"

R28 COMPLIANCE:
  Passive intelligence scoring uses weighted signal processing (rule engine — R28-1).
  Career prediction uses traditional ML (R28-2).
  NO LLM permitted in intelligence scoring path.
```

#### D-07: Society Franchise Operational Excellence

```
CAPTURE_TRIGGERS:
  - unit_health_score >= 85/100 (society-analytics-service)
  - attendance_compliance_rate >= 90% across batch
  - certificate_issued_count > enrollment_target (100%+ delivery)
  - tournament.completed with participation_rate >= 80%
  - payout_processed on time (7-day rule met)
  - scheme.approved (PMKVY / DDU eligibility confirmed)
  - income_uplift_tracked > 20% (longitudinal-impact-service, 12-month window)

CAPTURE_DATA_POINTS:
  - society_geo_tier (Urban | Semi-Urban | Rural | Remote)
  - coordinator_tenure_months
  - workshop_domain (craft | digital | agri | vocational)
  - batch_size
  - BPL_participant_percentage
  - gender_participation_ratio
  - govt_scheme_type (PMKVY | DDU | CSR | Self-funded)
  - coach_rating_at_time
  - offline_sync_failure_rate

PRACTICE_RECORD_TITLE FORMAT:
  "Franchise Excellence — [Geo Tier] — [Domain] — [Health Score Band]"

CHILD SAFETY BINDING (MANDATORY):
  Any practice record from batches with participants < 18 requires
  compliance_service.child_safety_log_verified = TRUE.
  Absence → REJECT CAPTURE
```

#### D-08: Enterprise Recruitment Quality

```
CAPTURE_TRIGGERS:
  - company_trust_score >= 85/100 post-KYC cycle
  - offer_rollout_rate >= 95% (19/20+ offers honored)
  - salary_truth_dispute_count == 0 over 180-day window
  - average_employee_rating >= 4.0/5
  - time_to_offer_days below domain mean
  - bond_completion_rate >= 80% (students honored bond)

CAPTURE_DATA_POINTS:
  - company_type (Product | Service | Startup | Core | Govt)
  - company_size_band (< 50 | 50–500 | 500–5000 | > 5000)
  - hiring_domain (CSE | Mech | All-branch | etc.)
  - interview_rounds_count
  - assessment_type (OA | Technical | HR | GD | Case Study)
  - package_transparency_score (CTC vs in-hand accuracy)
  - joining_timeline_honoring_rate

PRACTICE_RECORD_TITLE FORMAT:
  "Recruitment Excellence — [Company Type] — [Hiring Domain] — [Trust Score Band]"
```

#### D-09: Platform Governance & Trust

```
CAPTURE_TRIGGERS:
  - bias_incident_resolved_within_sla (< 72h)
  - dispute_resolved_with_zero_escalation
  - audit_service.surprise_audit_passed (no findings)
  - gdpr_compliance_score == 100% per audit period
  - moderation_decision_upheld_on_appeal (governance correctness)
  - compliance_service.incident_resolved + no_recurrence (90-day window)

CAPTURE_DATA_POINTS:
  - governance_event_type (bias | dispute | audit | moderation | GDPR)
  - domain_affected
  - resolution_time_hours
  - parties_involved_count (anonymized)
  - root_cause_category
  - resolution_mechanism (automated | manual | escalated)
  - recurrence_rate (0 = best practice confirmed)

PRACTICE_RECORD_TITLE FORMAT:
  "Governance Excellence — [Event Type] — [Domain] — [Resolution Time Band]"
```

---

## SECTION 5 — PRACTICE RECORD SCHEMA (CANONICAL · LOCKED)

Every captured best practice writes to a `best_practice_record` entity. Schema is immutable once written. Only append-only extensions permitted via version bump.

```json
{
  "record_id"            : "uuid-v4",
  "domain_id"            : "D-01 through D-09",
  "capture_class"        : "CC-01 through CC-08",
  "title"                : "human-readable title per domain format",
  "platform_module"      : "source service name",
  "event_source_ids"     : ["kafka_event_id_1", "..."],
  "capture_timestamp"    : "ISO 8601 UTC",
  "participant_class"    : "STUDENT | MENTOR | RECRUITER | INSTITUTE | SOCIETY | ENTERPRISE | PLATFORM",
  "anonymized_actor_id"  : "SHA-256 of internal user_id",
  "domain"               : "Arts | Commerce | Science | Technology | Administration",
  "tenant_id"            : "hashed_tenant_id",
  "career_stage"         : "Student | Job Seeker | Professional | Mentor | Retired",
  "outcome_metrics"      : { "key": "value_pairs_per_domain" },
  "preconditions"        : ["list of conditions present before practice"],
  "actions_taken"        : ["list of behaviors / decisions that led to outcome"],
  "outcome_confirmed"    : true,
  "outcome_confirmation_source" : "event_id or audit_log_id",
  "reproducibility_score": "0–100 (rule-computed)",
  "trust_attestation"    : {
    "attested"           : true,
    "attestation_type"   : "blockchain_hash | audit_log_hash | scoring_engine_hash",
    "hash"               : "SHA-256 hash of full record",
    "timestamp"          : "ISO 8601 UTC"
  },
  "pii_present"          : false,
  "child_safety_verified": "N/A | TRUE (mandatory if actor < 18)",
  "consent_verified"     : true,
  "distribution_tier"    : "PUBLIC | INTERNAL | RESTRICTED | COMPLIANCE_ONLY",
  "version"              : "1.0",
  "status"               : "ACTIVE | SUPERSEDED | ARCHIVED"
}
```

**HARD RULES FOR SCHEMA:**
- `pii_present = true` → REJECT RECORD → QUARANTINE → Security Alert
- `outcome_confirmed = false` → DRAFT status only → NOT distributed
- `child_safety_verified = false` (when required) → STOP → DO NOT WRITE
- `consent_verified = false` → REJECT RECORD

---

## SECTION 6 — PRACTICE VALIDATION ENGINE (DETERMINISTIC · RULE-BASED)

### 6.1 Validation Gate Sequence

```
GATE 1 — Schema Integrity Check
  → All required fields present
  → Data types match schema
  → FAIL: REJECT + LOG

GATE 2 — PII Scanner
  → Zero PII in any field
  → Zero personally identifiable patterns
  → FAIL: QUARANTINE + Security Alert

GATE 3 — Consent & Safety Verification
  → consent_verified = TRUE
  → child_safety_verified = TRUE (where required)
  → FAIL: REJECT + Compliance Log

GATE 4 — Outcome Confirmation Linkage
  → outcome_confirmation_source references valid event_id or audit_log_id
  → Event exists in Kafka ledger or PostgreSQL audit log
  → FAIL: DRAFT status, no distribution

GATE 5 — Reproducibility Scoring
  → Compute reproducibility_score using rule engine (NOT ML)
  → Score < 40: ARCHIVED (single occurrence, not generalizable)
  → Score 40–69: INTERNAL only
  → Score 70–89: INTERNAL + PARTICIPANT distribution
  → Score >= 90: PUBLIC distribution eligible

GATE 6 — Trust Attestation
  → Generate SHA-256 hash of full record content
  → Write hash to Immutable Archive Service
  → Attach to record trust_attestation block
  → FAIL: STOP — record not published without attestation

All gates must PASS sequentially.
Any gate failure = STOP progression.
No partial publication.
```

### 6.2 Reproducibility Score Formula (LOCKED)

```
reproducibility_score =
  + (outcome_metric_count >= 3 ? 20 : outcome_metric_count * 6)
  + (precondition_count >= 3 ? 20 : precondition_count * 6)
  + (action_count >= 2 ? 20 : action_count * 9)
  + (longitudinal_confirmed ? 20 : 0)
  + (cross_cohort_replicated ? 20 : 0)

Range: 0 – 100
Interpretation:
  0–39  : Single case, anecdotal
  40–69 : Emerging pattern, monitor
  70–89 : Established practice, distribute internally
  90–100: Proven practice, distribute publicly
```

---

## SECTION 7 — PRACTICE INDEX & RANKING ENGINE

### 7.1 Index Architecture

```
INDEX_STORE: OpenSearch (full-text + faceted search)
ANALYTICS_STORE: ClickHouse (aggregation + trend queries)
STATE_STORE: PostgreSQL (canonical records + version history)

INDEX_DIMENSIONS:
  - domain_id
  - capture_class
  - participant_class
  - career_stage
  - skill_domain
  - company_type / society_geo_tier (domain-dependent)
  - reproducibility_band
  - recency (capture_timestamp)
  - distribution_tier
  - outcome_metric_keys
```

### 7.2 Practice Ranking Algorithm (DETERMINISTIC)

```
practice_rank =
  (reproducibility_score × 0.35)
+ (recency_score × 0.20)          // exponential decay over 365 days
+ (cross_domain_applicability × 0.15)
+ (replication_count × 0.20)      // times same pattern confirmed independently
+ (trust_attestation_strength × 0.10)

// trust_attestation_strength:
//   blockchain_hash = 1.0
//   audit_log_hash  = 0.85
//   scoring_engine_hash = 0.70

Ranking is deterministic.
Ties broken by capture_timestamp (earlier = higher rank).
No personalization in ranking engine (same rank for all viewers of same index).
```

### 7.3 Practice Supersession Rules

```
A practice record is SUPERSEDED when:
  - A newer record in same domain + same capture_class achieves
    reproducibility_score >= current_record + 10 points
  - Platform rule change invalidates the preconditions
  - Compliance update renders the practice non-compliant

On supersession:
  - Old record status → SUPERSEDED (never deleted)
  - Superseded_by field populated with new record_id
  - Both records retained in Immutable Archive
  - Distribution halted for SUPERSEDED records
  - Audit trail preserved indefinitely
```

---

## SECTION 8 — DISTRIBUTION FRAMEWORK

### 8.1 Distribution Tier Map

| Tier | Label | Audience | Access Method | Condition |
|---|---|---|---|---|
| T-1 | PUBLIC | All platform visitors + SEO | Static pages (Next.js SSR) + Public API | reproducibility_score >= 90 |
| T-2 | PARTICIPANT | Logged-in participants (relevant class) | Dashboard widget + Notification | reproducibility_score >= 70 |
| T-3 | INTERNAL | Platform admins, Module owners, TPOs | Admin dashboard + Internal API | reproducibility_score >= 40 |
| T-4 | COMPLIANCE_ONLY | Compliance officer, Legal, Audit | Secure compliance portal | CC-08 class only |

### 8.2 Distribution Channel Contracts

```
CHANNEL: Student Dashboard (Flutter App)
  SOURCE: D-02 (Placement) + D-03 (Skill) + D-06 (Intelligence)
  FORMAT: Practice Card widget (title + 3 action bullets + outcome metric)
  TRIGGER: Career stage match + skill domain match
  PERSONALIZATION: Domain-filtered, career-stage-filtered only
                   (NOT individual-targeted — privacy protection)

CHANNEL: Mentor Control Dashboard
  SOURCE: D-04 (Mentor Effectiveness) + D-01 (GD) + D-03 (Skill Velocity)
  FORMAT: Effectiveness report + peer benchmark
  TRIGGER: Weekly digest (Apache Airflow scheduled workflow)

CHANNEL: TPO / Institute Dashboard
  SOURCE: D-02 (Placement) + D-08 (Enterprise Recruitment)
  FORMAT: Placement success pattern report
  TRIGGER: Monthly batch + on-demand

CHANNEL: Society Coordinator Dashboard
  SOURCE: D-07 (Franchise Excellence)
  FORMAT: Unit benchmark report + operational practice cards
  TRIGGER: Post-batch completion

CHANNEL: Public SEO Pages (Next.js — R80 pipeline)
  SOURCE: T-1 practices only → feeds R80 Success Story Generation
  FORMAT: LLM-generated narrative (R28-3 permitted) wrapping anonymized practice data
  GATE: success_story_records writer requires practice record_id as source reference

CHANNEL: Compliance Portal
  SOURCE: D-09 (Governance) + CC-08 class
  FORMAT: Audit-grade PDF (Legal Document Generation Service)
  RETENTION: 7 years (Immutable Archive Service — WORM storage)
```

### 8.3 Distribution Anti-Patterns (FORBIDDEN)

```
❌ Surfacing practices with pii_present = TRUE to any channel
❌ Distributing DRAFT or SUPERSEDED records
❌ Recommending practices to individual recruiters based on student identity
❌ Cross-tenant practice sharing without explicit tenant opt-in contract
❌ Using innovation domain practices (D-05) in public channels without
   royalty_payment_confirmed = TRUE (IP protection)
❌ Society child-batch practices in public channel without
   child_safety_verified = TRUE
❌ Practice records as hiring criteria (HARD FORBIDDEN — same rule as PSTA)
```

---

## SECTION 9 — TRUST INFRASTRUCTURE INTEGRATION

### 9.1 Immutable Archive Service Binding

```
ALL practice records → written to Immutable Archive Service
STORAGE CLASS: WORM (Write Once, Read Many)
RETENTION: 15 years (aligned with Royalty & Licensing contract lifecycles)
HASH CHAIN: Each record references prior record hash (chain integrity)
AUDIT ACCESS: Read-only via Compliance Portal
DELETION: FORBIDDEN (only status change to ARCHIVED permitted)

On any hash verification failure:
  → STOP distribution of affected record
  → Alert: Compliance Officer + Infrastructure Lead
  → Quarantine: record locked pending investigation
  → SLA: 24-hour resolution
```

### 9.2 Blockchain Offer Ledger Binding (D-02 Domain)

```
READS_FROM: Blockchain Offer Ledger
PURPOSE: Confirms offer authenticity before placement practice records are written

Rule:
  offer_verified_on_blockchain = FALSE
  → placement practice record BLOCKED
  → LOG: unverified_offer_practice_attempt
  → Alert: Compliance + TPO

This prevents fabricated placement success stories entering the practice index.
```

### 9.3 Scoring Engine Binding

```
READS_FROM:
  - GD Scoring Engine (D-01)
  - Dojo Scoring Engine + Belt Engine (D-03, D-04)
  - Intelligence Scoring Engine (D-06)
  - Innovation Scoring Engine (D-05)

ALL scores consumed are immutable audit log entries.
Agent reads score hashes, not raw score computation.
Score disputes → BLOCK affected practice record pending dispute resolution.
```

### 9.4 Legal & Document Services Binding

```
CC-08 (Regulatory-Evidence Practice) REQUIRES:
  - Legal Document Generation Service output attached as evidence
  - Digital Signature Service confirmation (parent consent where required)
  - Immutable Archive reference ID

Distribution of CC-08 practices without legal document linkage:
  → BLOCKED
  → Compliance Alert
```

### 9.5 Fraud Detection Engine Binding

```
READS_FROM: Fraud Detection Engine (AI & Data Infrastructure)

Monitors practice record submission patterns for:
  - Rapid pattern fabrication (abnormal velocity of capture events)
  - Cross-tenant practice injection attempts
  - Innovation idea duplication submissions designed to inflate D-05 index
  - Anomalous outcome metric clustering (statistical impossibility detection)

On fraud signal detected:
  → QUARANTINE all records from flagged source
  → ALERT: Admin Governance Service + Security
  → FREEZE: Source tenant practice write permissions pending investigation
  → SLA: 48-hour resolution
```

---

## SECTION 10 — INTELLIGENCE ENGINE INTEGRATION

```
FEEDS INTO: Intelligence Prediction Engine (career success probability model)
DATA TRANSFERRED:
  - D-06 practice records (anonymized aggregate, not individual)
  - Feature vectors: activity_type → intelligence_delta mappings
  - Cross-domain intelligence growth patterns

PURPOSE:
  Improves ML model accuracy for career success prediction (R28-2 class).

RULE:
  Individual intelligence DNA vectors NEVER written to practice records.
  Only aggregate pattern vectors (cohort-level, career-stage-level).
  Any individual vector detected → REJECT → PII violation alert.

FEEDS FROM: Passive Intelligence Engine
  Best practices that consistently precede intelligence growth events
  are flagged for CC-04 (Longitudinal-Impact) capture.
```

---

## SECTION 11 — CAREER LIFECYCLE INTEGRATION (R71)

```
ALIGNMENT: R71 — Career Lifecycle Capture System Law

Practice records are tagged to career_stage:
  STUDENT       → D-03 (Skill Velocity), D-01 (GD Compliance), D-06 (Intelligence)
  JOB_SEEKER    → D-02 (Placement Success), D-01 (GD Excellence), D-08 (Recruitment Quality)
  PROFESSIONAL  → D-04 (Mentor Effectiveness), D-05 (Innovation), D-06 (Intelligence Growth)
  MENTOR        → D-04 (own effectiveness), D-03 (teaching patterns)
  RETIRED       → D-09 (Governance, if admin roles held)

State machine integration:
  On career_stage_transition event:
    → Filter relevant practice domains for new stage
    → Push practice recommendations to participant dashboard
    → Archive irrelevant stage records from active feed (not deleted)
```

---

## SECTION 12 — R80 SUCCESS STORY PIPELINE BINDING

```
ALIGNMENT: R80 — Continuous Success Story Generation Law

TRIGGER:
  practice_record (D-02 | D-03 | D-05 | D-07) with:
    reproducibility_score >= 90
    distribution_tier = PUBLIC
    outcome_metrics showing significant positive delta

PIPELINE:
  1. BPCA emits: practice_story_eligible event (Kafka)
  2. R80 Story Engine receives event
  3. LLM generates narrative (R28-3 — permitted)
     Input: anonymized practice record (no PII)
     Output: narrative story text
  4. Human review gate (optional, admin-configurable)
  5. Publish to: /blog/[slug] (Next.js SSR — SEO indexed)
  6. Link: success_story_records.practice_record_id = record_id

CONSTRAINT:
  LLM narrative generation is DOWNSTREAM of this agent.
  This agent never calls LLM directly.
  All LLM calls are in R80 Story Engine only.
  This agent provides only structured data input to that engine.
```

---

## SECTION 13 — SOCIETY OFFLINE SYNC PROTOCOL

```
CONTEXT:
  Society franchise operates in rural / low-connectivity environments.
  Attendance records, tournament scores, and payout events may arrive
  with delay via offline sync (attendance-service offline sync protocol).

RULE FOR OFFLINE-SOURCED PRACTICES:
  practice record may not be written until:
    - offline_sync_status = CONFIRMED
    - data_integrity_check = PASSED
    - sync_timestamp + source_timestamp delta logged

  offline_sync_failure_rate > 20% for a unit:
    → Suppress D-07 practice capture for that unit
    → Log: data_quality_insufficient
    → Alert: Society Analytics Service + Coordinator

  Rationale: Practices derived from corrupt or incomplete offline data
  would contaminate the practice index.
```

---

## SECTION 14 — ALERTING & ESCALATION

| Alert ID | Trigger | Severity | Recipient | SLA |
|---|---|---|---|---|
| A-01 | PII detected in capture payload | CRITICAL | Security + Admin | Immediate |
| A-02 | Child safety verification missing | CRITICAL | Compliance + Legal | Immediate |
| A-03 | Fraud pattern in practice submissions | HIGH | Admin + Governance | 48h |
| A-04 | Hash chain integrity failure | HIGH | Infra Lead + Compliance | 24h |
| A-05 | Blockchain offer verification failure (D-02 blocked) | HIGH | TPO + Admin | 24h |
| A-06 | Practice index staleness > 7 days (no new captures) | MEDIUM | Module Owner | 72h |
| A-07 | Reproducibility score collapse (domain mean drops > 20%) | MEDIUM | Platform Admin | 72h |
| A-08 | Cross-tenant practice injection attempt | CRITICAL | Security + Legal | Immediate |
| A-09 | Offline sync corruption affecting D-07 capture | MEDIUM | Society Analytics + Coordinator | 48h |
| A-10 | Innovation practice record written without royalty confirmation | HIGH | IP Compliance + Legal | 24h |

---

## SECTION 15 — REPORTING CONTRACTS

| Report ID | Name | Frequency | Recipient | Format |
|---|---|---|---|---|
| RPT-01 | Platform Best Practice Index Digest | Weekly | All Module Owners | Dashboard + JSON |
| RPT-02 | Domain Excellence Report (per D-01 to D-09) | Monthly | Domain Leads | PDF |
| RPT-03 | Trust Attestation Health Report | Weekly | Compliance + Admin | PDF |
| RPT-04 | Society Franchise Practice Benchmark | Per batch | Society Coordinators | PDF |
| RPT-05 | Campus Placement Pattern Report | Semester | TPOs + Institutes | PDF (GDPR-safe) |
| RPT-06 | Innovation Excellence Pipeline Report | Monthly | Innovation Lead + Legal | PDF |
| RPT-07 | Practice Supersession Log | On-change | Platform Admin | JSON |
| RPT-08 | Fraud & Anti-Gaming Incident Report | Monthly | Compliance + Security | CONFIDENTIAL PDF |
| RPT-09 | Annual Institutional Knowledge Audit | Annual | Board + Legal | Full Audit Package |
| RPT-10 | R80 Story Source Traceability Report | Monthly | Content Team + Admin | JSON |

---

## SECTION 16 — FAILURE HANDLING (NON-NEGOTIABLE)

| Failure Scenario | Agent Action | Downstream Effect |
|---|---|---|
| Kafka event source unavailable | Buffer to local queue (max 6h); retry on reconnect | No practice records lost; delay only |
| PostgreSQL write failure | Retry ×3 (10s interval); on failure → STOP + ALERT | No partial records; atomicity enforced |
| OpenSearch index failure | Queue for re-index; practice record still written to PostgreSQL | Search degraded; records safe |
| Immutable Archive write failure | BLOCK record distribution until archive confirmed | Record exists but not published |
| PII detection mid-pipeline | Halt pipeline; quarantine payload; alert Security | No PII ever reaches record store |
| Fraud signal received | Freeze source; quarantine records; escalate | Index integrity protected |
| Offline sync data corruption | Reject capture; log; alert coordinator | Practice index not contaminated |
| Schema validation failure | Reject record; return error event to source | Source service must correct and re-emit |
| Hash chain break detected | Lock affected domain index; escalate | Compliance investigation triggered |

---

## SECTION 17 — DEPLOYMENT SPECIFICATION

### 17.1 Infrastructure Requirements

```
RUNTIME         : Node.js 20 LTS (microservice)
STATE_STORE     : Redis (capture pipeline state; deduplication locks)
PRIMARY_DB      : PostgreSQL (canonical records; version history; audit)
ANALYTICS_DB    : ClickHouse (ranking analytics; trend queries; reproducibility distributions)
SEARCH_ENGINE   : OpenSearch (practice index; faceted search)
ARCHIVE_SERVICE : MinIO (WORM-configured bucket; 15-year retention)
MESSAGE_BUS     : Kafka (all inbound capture events; all outbound distribution events)
SCHEDULER       : Apache Airflow (weekly digest reports; monthly benchmark reports)
HOSTING         : Kubernetes namespace: antigravity-trust-infra
SCALING_POLICY  : Horizontal pod autoscaling (min 2 replicas; max 8)
ENVIRONMENT     : DEV · TEST · STAGING · PRODUCTION (isolated per R96 law)
```

### 17.2 Environment Config Files Required

```
/config/environments/
  bpca.dev.env
  bpca.test.env
  bpca.staging.env
  bpca.production.env

Each must define:
  POSTGRES_CONNECTION
  REDIS_CONNECTION
  KAFKA_BROKER_URL
  OPENSEARCH_URL
  CLICKHOUSE_URL
  MINIO_ENDPOINT
  MINIO_BUCKET_PRACTICE_RECORDS
  IMMUTABLE_ARCHIVE_ENDPOINT
  BLOCKCHAIN_NODE_URL
  FRAUD_ENGINE_WEBHOOK
  COMPLIANCE_ALERT_WEBHOOK
  AGENT_SECRET (90-day rotation policy)
  PII_SCANNER_ENDPOINT
  CHILD_SAFETY_CONSENT_SERVICE_URL
```

### 17.3 Kubernetes Namespace Binding

```
NAMESPACE: antigravity-trust-infra

CO-RESIDENT SERVICES (same namespace):
  - PARTICIPANT_SATISFACTION_TRACKING_AGENT (PSTA-ENT-001)
  - BEST_PRACTICE_CAPTURE_AGENT (BPCA-ENT-001) ← THIS AGENT
  - Trust Score Engine
  - Compliance Service
  - Audit Service

NETWORK POLICY:
  - Inbound: Kafka consumer only (no direct HTTP from external services)
  - Outbound: Kafka producer + OpenSearch + PostgreSQL + ClickHouse + MinIO
  - Cross-namespace: DENIED except explicit service mesh policies
```

### 17.4 CI/CD Gate Requirements

```
PIPELINE_STAGES:
  1. Schema Contract Validator (best_practice_record_v1.json)
  2. PII Scanner Integration Tests (must detect and reject synthetic PII)
  3. Child Safety Verification Tests
  4. Validation Gate Unit Tests (all 6 gates, pass + fail scenarios)
  5. Reproducibility Score Determinism Tests (same input → same score always)
  6. Fraud Detection Integration Tests
  7. Immutable Archive Write Tests
  8. Trust Attestation Hash Tests
  9. Distribution Tier Enforcement Tests
  10. Docker Build + Security Scan (zero HIGH/CRITICAL CVEs)
  11. Staging Deploy + Smoke Test (all 9 domains capture at least one practice)
  12. Production Gate (dual human approval for scoring formula changes)
```

---

## SECTION 18 — ANTI-PATTERNS (EXPLICITLY FORBIDDEN)

```
FORBIDDEN:
  ❌ Practice records containing real names, emails, student IDs, or any PII
  ❌ Practice records written from unverified or DRAFT outcome events
  ❌ Distributing SUPERSEDED records through any channel
  ❌ Using practice records as evaluation or hiring input
  ❌ Surfacing innovation practices (D-05) without IP clearance
  ❌ Writing intelligence DNA vectors to practice records (individual cognitive data)
  ❌ Cross-tenant practice read access without explicit tenant contract
  ❌ Generating R80 narratives directly from this agent (LLM calls belong to R80 engine only)
  ❌ Deleting any practice record from Immutable Archive
  ❌ Ranking practices by subjective popularity metrics (likes, shares)
  ❌ Allowing mentors to self-nominate D-04 practices
  ❌ Capturing society practices from offline-sync-unverified data
  ❌ Publishing child-batch practices without guardian consent verification
  ❌ Inferences about personal character, personality, or leadership from practice patterns
```

---

## SECTION 19 — AUDIT & COMPLIANCE TRAIL

```
EVERY AGENT ACTION MUST LOG:
  - agent_id            : BPCA-ENT-001
  - action_type         : [CAPTURE_RECEIVED | VALIDATION_GATE_PASSED | VALIDATION_GATE_FAILED |
                           RECORD_WRITTEN | RECORD_DISTRIBUTED | RECORD_SUPERSEDED |
                           RECORD_QUARANTINED | ALERT_EMITTED | REPORT_GENERATED |
                           ARCHIVE_WRITTEN | HASH_GENERATED | FRAUD_FLAG_RAISED]
  - timestamp           : ISO 8601 UTC
  - record_id           : (if applicable)
  - domain_id           : D-01 through D-09
  - capture_class       : CC-01 through CC-08
  - gate_id             : (if validation gate action)
  - outcome             : PASS | FAIL | QUARANTINE | ALERT
  - input_hash          : SHA-256 of input payload
  - output_hash         : SHA-256 of output payload
  - rule_applied        : section + rule ID from this document
  - environment         : DEV | TEST | STAGING | PRODUCTION
  - tenant_id           : hashed

LOG_RETENTION:
  PRODUCTION  : 15 years (aligned with innovation/royalty contract lifecycle)
  STAGING     : 90 days
  TEST        : 30 days
  DEV         : 7 days

LOG_IMMUTABILITY: APPEND-ONLY. No UPDATE or DELETE on audit log.
```

---

## SECTION 20 — VERSION CONTROL POLICY

```
DOCUMENT_VERSION   : 1.0
STATUS             : SEALED
CHANGE_AUTHORITY   : Platform Architect + Compliance Officer + Legal (triple sign-off)
CHANGE_POLICY      : ADD-ONLY sections permitted
                     No deletion or modification of existing rules
VERSION_FORMAT     : MAJOR.MINOR (1.0, 1.1, 2.0)

MAJOR_BUMP_TRIGGERS:
  - Addition of new Practice Domain (D-10+)
  - Change to reproducibility score formula
  - Change to Practice Record schema
  - New capture class added
  - Immutable Archive binding change
  - Child safety rule change
  - Cross-domain inference contract added

MINOR_BUMP_TRIGGERS:
  - New capture trigger within existing domain
  - Alert threshold adjustment
  - Report type added
  - New distribution channel
  - Additional Kafka event binding
```

---

## SECTION 21 — SYSTEM DEFENSE STATEMENT

> **"The BEST_PRACTICE_CAPTURE_AGENT converts every verified positive outcome across the Antigravity ecosystem — from a student's first blockchain-secured job offer to a rural society franchise achieving 90% attendance compliance — into a permanently indexed, trust-attested, reproducible knowledge record. No human judgment enters the capture process. No AI interprets the outcomes. Only event-confirmed behaviors, deterministic validation gates, SHA-256 trust attestations, and rule-based ranking remain. The result is institutional knowledge that cannot be gamed, cannot be fabricated, cannot be attributed to individuals without consent, and cannot be erased. Knowledge becomes infrastructure."**

---

## APPENDIX A — DOMAIN × CAPTURE CLASS ELIGIBILITY MATRIX

| | CC-01 | CC-02 | CC-03 | CC-04 | CC-05 | CC-06 | CC-07 | CC-08 |
|---|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| D-01 GD Excellence | ✅ | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ | ❌ |
| D-02 Placement Success | ✅ | ✅ | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ |
| D-03 Skill Velocity | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ | ❌ | ❌ |
| D-04 Mentor Effectiveness | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ❌ |
| D-05 Innovation Excellence | ✅ | ❌ | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ |
| D-06 Intelligence Growth | ✅ | ✅ | ❌ | ✅ | ❌ | ✅ | ❌ | ❌ |
| D-07 Society Franchise | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| D-08 Enterprise Recruitment | ✅ | ✅ | ✅ | ✅ | ❌ | ✅ | ✅ | ✅ |
| D-09 Governance & Trust | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ | ✅ | ✅ |

---

## APPENDIX B — KAFKA EVENT CONSUMPTION MAP

| Kafka Topic | Domain | Capture Class Triggered |
|---|---|---|
| `gd.completed` | D-01 | CC-01, CC-02 |
| `interview.completed` | D-02 | CC-01 |
| `offer.accepted` (blockchain confirmed) | D-02 | CC-01, CC-06 |
| `belt.eligible` | D-03 | CC-01, CC-04 |
| `skill_passport.record_written` | D-03 | CC-01 |
| `match.scored` (Dojo) | D-04 | CC-01, CC-05 |
| `idea_approved` | D-05 | CC-01, CC-06 |
| `royalty_payment.received` | D-05 | CC-04 |
| `intelligence_score.updated` | D-06 | CC-04, CC-06 |
| `tournament.completed` (Society) | D-07 | CC-01, CC-04 |
| `payout.processed` (7-day rule) | D-07 | CC-02 |
| `scheme.approved` | D-07 | CC-08 |
| `company_trust_score.updated` | D-08 | CC-01, CC-02 |
| `bias_incident.resolved` | D-09 | CC-03, CC-07 |
| `audit_service.audit_passed` | D-09 | CC-02, CC-08 |
| `dispute.resolved` | D-09 | CC-03 |
| `income_uplift.tracked` | D-07 | CC-04 |
| `career_milestone.detected` | D-02, D-03 | CC-01 → R80 feed |

---

## APPENDIX C — INTEGRATION SERVICE DEPENDENCY MAP

```
UPSTREAM (reads from):
  ✅ GD Orchestrator State Machine
  ✅ Blockchain Offer Ledger
  ✅ Scoring Engine (all variants)
  ✅ Belt Engine
  ✅ Intelligence Profile Service
  ✅ Innovation Scoring Engine
  ✅ Licensing Contract Service
  ✅ Royalty Accounting Engine
  ✅ Society Analytics Service
  ✅ Longitudinal Impact Service
  ✅ Company KYC System
  ✅ Admin Governance Service
  ✅ Compliance Service
  ✅ Audit Service
  ✅ Fraud Detection Engine
  ✅ Consent Management System
  ✅ Legal Document Generation Service
  ✅ Digital Signature Service

DOWNSTREAM (writes to / feeds):
  ✅ Immutable Archive Service (WORM)
  ✅ OpenSearch Practice Index
  ✅ ClickHouse Practice Analytics
  ✅ PostgreSQL Canonical Record Store
  ✅ Student Dashboard (Flutter — practice cards)
  ✅ Mentor Control Dashboard
  ✅ TPO / Institute Dashboard
  ✅ Society Coordinator Dashboard
  ✅ R80 Success Story Engine (Kafka event)
  ✅ Compliance Portal
  ✅ Admin Governance Dashboard
  ✅ Alert & Notification Engine

ABSENT CONTRACT → STOP_LANE + REPORT_MISSING_CONTRACT
```

---

*ANTIGRAVITY Platform — Trust Infrastructure Division*
*Document ID: BPCA-ENT-001-v1.0*
*Sealed: March 2026*
*Next Review: September 2026 (mandatory)*
*Classification: INTERNAL — PLATFORM ARCHITECTURE*
*Companion Document: PSTA-ENT-001-v1.0 (PARTICIPANT_SATISFACTION_TRACKING_AGENT)*
