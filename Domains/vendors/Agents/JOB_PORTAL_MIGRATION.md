# 🔒 SEALED & LOCKED JOB PORTAL MIGRATION STRATEGY
## ANTIGRAVITY SAAS PLATFORM - JOB PORTAL IMPLEMENTATION & TRANSITION
### EXECUTION MODE: LOCKED | MUTATION POLICY: ADD_ONLY

---

## 🔐 DOCUMENT SECURITY NOTICE
```
CLASSIFICATION: JOB PORTAL MIGRATION STRATEGY (CONFIDENTIAL)
EXECUTION_MODE = LOCKED
MUTATION_POLICY = ADD_ONLY (No modifications, only extensions with explicit approval)
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING = FORBIDDEN
DEFAULT_BEHAVIOR = DENY (Whitelist-only job portal features)
FAILURE_MODE = STOP_EXECUTION (Halt on constraint violation, escalate)
APPROVAL_REQUIRED = CTO + VP Product + Chief Compliance Officer
ROLLBACK_TRIGGER = Data integrity failure OR >25% cost overrun OR matching accuracy <85%
```

---

# PART 1: JOB PORTAL STRATEGY & ARCHITECTURE

## 1️⃣ JOB PORTAL VISION & SCOPE (LOCKED)

### Platform Definition

```
ANTIGRAVITY_JOB_PORTAL:
├─ Primary Purpose: Connect qualified candidates with physics/aerospace jobs
├─ Scope: Research positions, Commercial roles, Government contracts
├─ Target Audience:
│  ├─ Job Seekers: Physics PhDs, Engineers, Researchers (Antigravity-specialized)
│  ├─ Employers: Research institutions, Defense contractors, Aerospace companies
│  ├─ Recruiters: Specialized physics/engineering recruiters
│  └─ Agencies: Government contracting agencies
│
├─ Unique Value Proposition:
│  ├─ Physics-specific AI matching (understanding gravity field expertise)
│  ├─ Certification validation (pilot licenses, security clearances)
│  ├─ Skill verification (antigravity-specific competencies)
│  ├─ Physics-aware resume parsing (equations, technical papers)
│  └─ Safety-critical role matching (high accountability)
│
└─ Integration Points:
   ├─ Links to Skill Development module (gap analysis)
   ├─ Links to Project Execution (portfolio evidence)
   ├─ Links to Certification engine (license verification)
   ├─ Integrates with ERP (hiring workflows, offers, contracts)
   └─ Integrates with Compliance (safety clearance tracking)
```

### Job Categories & Types

```
RESEARCH_POSITIONS (Academia, National Labs, Private R&D)
├─ Gravity field physicist
├─ Propulsion systems engineer
├─ Quantum mechanics researcher
├─ Experimental physicist
├─ Materials scientist (antigravity-specific)
├─ Senior researcher / PI roles
└─ Postdoc / Research scientist

COMMERCIAL_ENGINEERING (Aerospace, Tech Companies)
├─ Systems engineer (antigravity propulsion)
├─ Flight control engineer
├─ Hardware engineer
├─ Software engineer (physics simulation)
├─ Test engineer / Field technician
├─ Manufacturing engineer
└─ Quality assurance engineer

OPERATIONAL_ROLES (Flight Operations, Facilities)
├─ Certified pilot (antigravity-capable)
├─ Flight controller
├─ Ground support technician
├─ Field operations manager
├─ Safety monitor
└─ Maintenance technician

GOVERNMENT_CONTRACTS (Defense, Intelligence, Space)
├─ Program manager (classified)
├─ System engineer (security clearance required)
├─ Safety analyst
├─ Regulatory compliance specialist
├─ Intelligence analyst
└─ Project officer

SUPPORT_ROLES (Sales, Marketing, Administration)
├─ Sales engineer (physics background)
├─ Technical writer
├─ Project administrator
├─ Business development (science background)
└─ Training specialist
```

---

## 2️⃣ JOB PORTAL DATA MODEL (LOCKED SCHEMA)

### Core Entities

```
JOB_POSTING
├─ job_id (UUID, immutable)
├─ employer_id (FK: Company/Institution)
├─ job_title (required, searchable)
├─ job_description (required, >200 chars)
├─ job_category (enum: Research, Commercial, Operational, Government, Support)
├─ job_type (enum: Full-time, Contract, Postdoc, Internship, Fellowship)
├─ required_skills (array of skill IDs, searchable)
├─ required_certifications (array: Physics PhD, Pilot License, Security Clearance, etc.)
├─ physics_specialization (enum: GravityFields, Propulsion, Materials, Quantum, etc.)
├─ salary_min (decimal, optional but searchable)
├─ salary_max (decimal, optional, >salary_min)
├─ location (string, geocoded for search)
├─ remote_eligible (boolean)
├─ work_authorization_required (enum: US Citizen Only, Security Clearance needed, Open)
├─ posting_status (enum: Draft, Published, Closed, Archived)
├─ published_date (timestamp, immutable)
├─ closing_date (timestamp, no extensions allowed for compliance)
├─ applications_count (integer, auto-calculated)
├─ verified_by (FK: HR/Compliance reviewer, immutable audit)
├─ verification_date (timestamp, immutable)
├─ ai_match_calibration (decimal: 0.0-1.0, stores ML model version)
└─ metadata (JSON: tags, categories, custom fields)

JOB_APPLICATION
├─ application_id (UUID, immutable)
├─ job_id (FK: Job Posting)
├─ candidate_id (FK: User)
├─ application_date (timestamp, immutable)
├─ application_status (enum: Submitted, Reviewed, Shortlisted, Rejected, Offered, Accepted)
├─ ai_match_score (decimal: 0.0-1.0, advisory only)
├─ match_explanation (JSON: reasons for match score, human-readable)
├─ eligibility_flag (boolean: meets minimum requirements?)
├─ eligibility_details (JSON: which requirements met/not met)
├─ resume_parsed_data (JSON: extracted skills, education, experience)
├─ portfolio_evidence (array: links to projects, certifications, publications)
├─ cover_letter (text, optional)
├─ custom_questions (JSON: employer-specific questions answered)
├─ status_updated_date (timestamp)
├─ status_updated_by (FK: User, who updated status)
├─ recruiter_notes (text, private to employer)
├─ candidate_notes (text, visible to candidate)
├─ reject_reason (enum: Doesn't meet requirements, Better match found, Position filled, etc.)
└─ audit_trail (JSON array: [timestamp, action, user_id], immutable)

CANDIDATE_PROFILE
├─ candidate_id (FK: User)
├─ physics_specialty (enum: GravityFields, Propulsion, Materials, Quantum, etc.)
├─ degree_level (enum: Bachelor, Master, PhD, Postdoc)
├─ degree_discipline (enum: Physics, Engineering, Materials Science, etc.)
├─ certifications (array: Pilot License, Security Clearance, Safety, etc.)
├─ years_experience (integer)
├─ willing_to_relocate (boolean)
├─ preferred_locations (array of strings, geocoded)
├─ job_preferences (JSON: job type, salary range, specialization preferences)
├─ skills (array of skill IDs with proficiency levels)
├─ publications (array: links to research papers, patents, etc.)
├─ portfolio_projects (array FK: Project IDs from execution engine)
├─ work_authorization (enum: US Citizen, Green Card, Visa eligible, etc.)
├─ security_clearance_level (enum: None, Secret, Top Secret, Classified)
├─ clearance_expiry (date, null if not applicable)
├─ background_check_status (enum: Passed, Pending, Failed, N/A)
└─ profile_completeness (percentage: 0-100%)

EMPLOYER_PROFILE
├─ employer_id (UUID, immutable)
├─ employer_type (enum: Research Institution, Company, Government Agency, Military)
├─ employer_name (required, searchable)
├─ industry (enum: Aerospace, Defense, Energy, Transportation, etc.)
├─ location (string, geocoded)
├─ website (URL)
├─ description (text)
├─ hiring_manager_id (FK: User, must be employer employee)
├─ posting_approval_required (boolean: must HR approve postings?)
├─ posting_approval_sla (integer: hours to approve)
├─ hiring_workflow_rules (JSON: customizable hiring workflow)
├─ compliance_requirements (array: ITAR, EAR, NACI, SSBI, etc.)
├─ verified (boolean, required to post jobs)
├─ verification_date (timestamp)
├─ payment_verified (boolean, credit card on file?)
└─ posting_quota (integer: max open postings allowed)

AI_MATCH_RESULT
├─ match_id (UUID)
├─ job_id (FK: Job)
├─ candidate_id (FK: Candidate)
├─ match_score (decimal: 0.0-1.0)
├─ score_reasoning (JSON: factors contributing to score)
├─ model_version (string: which AI model used, for reproducibility)
├─ calculated_date (timestamp)
├─ human_override (boolean: was score manually adjusted?)
├─ override_reason (text, if human_override=true, required)
├─ confidence_level (decimal: 0.0-1.0, estimate of score reliability)
└─ audit_trail (JSON: how score was calculated, for explainability)

JOB_OFFER
├─ offer_id (UUID, immutable)
├─ job_id (FK: Job)
├─ candidate_id (FK: Candidate)
├─ offer_date (timestamp, immutable)
├─ offer_expiry (timestamp, typically 7-14 days from offer_date)
├─ offer_status (enum: Pending, Accepted, Rejected, Expired, Withdrawn)
├─ salary (decimal, locked in offer)
├─ benefits (JSON: health insurance, 401k, etc.)
├─ start_date (date)
├─ offer_letter (document: PDF, immutable)
├─ signed_date (timestamp, null if not yet signed)
├─ signed_by (FK: Candidate, null if not yet accepted)
├─ rejection_reason (text, if rejected)
├─ status_updated_date (timestamp)
├─ status_updated_by (FK: User)
└─ audit_trail (JSON array: all state changes, immutable)

SEARCH_LOG
├─ log_id (UUID)
├─ user_id (FK: User)
├─ search_query (text, for analytics)
├─ filters_applied (JSON: which filters used)
├─ results_count (integer)
├─ clicked_jobs (array: which job IDs clicked)
├─ search_date (timestamp)
└─ device_type (enum: Mobile, Desktop, Tablet)
```

---

## 3️⃣ JOB PORTAL FEATURES (LOCKED FUNCTIONAL REQUIREMENTS)

### Feature Set (Phase-Based)

```
PHASE_1: JOB DISCOVERY & APPLICATIONS (Months 1-6)
├─ Job search & filtering
│  ├─ Full-text search (Elasticsearch)
│  ├─ Faceted filtering (category, location, salary, specialization)
│  ├─ Saved searches (user can save & get notified)
│  ├─ Job alerts (email/push when matching jobs posted)
│  └─ Advanced search (boolean operators, date ranges)
│
├─ Job posting & viewing
│  ├─ Job detail page (description, requirements, company info)
│  ├─ Share job (social media, email)
│  ├─ Apply to job (simple form submission)
│  ├─ Track application status (real-time updates)
│  └─ Withdraw application (if before offer stage)
│
└─ Employer job posting
   ├─ Create job posting (guided form)
   ├─ Preview posting (before publish)
   ├─ Publish job (verification required)
   ├─ Edit job (before closing date)
   ├─ View applications (list, filter, search)
   └─ Download applications (export as CSV/PDF)

PHASE_2: AI MATCHING & INSIGHTS (Months 7-12)
├─ AI match scoring
│  ├─ Resume parsing (extract skills, education, experience)
│  ├─ Match score calculation (0.0-1.0)
│  ├─ Eligibility assessment (meets minimum requirements?)
│  ├─ Explanation generation (why matched, what's missing)
│  └─ Physics-specific matching (understanding antigravity specialization)
│
├─ Candidate insights
│  ├─ Skill gap analysis (what skills missing for target role?)
│  ├─ Learning recommendations (courses to improve match)
│  ├─ Similar jobs (other jobs candidate might like)
│  └─ Career path guidance (progression recommendations)
│
└─ Employer insights
   ├─ Candidate quality metrics (application quality trends)
   ├─ Time-to-hire analytics (how long to fill position?)
   ├─ Cost-per-hire analytics (total cost to hire)
   └─ Diversity metrics (if applicable, with compliance)

PHASE_3: HIRING WORKFLOW & OFFERS (Months 13-18)
├─ Candidate screening
│  ├─ Shortlist candidates (employer action)
│  ├─ Schedule interviews (integrated calendar)
│  ├─ Interview feedback (structured form)
│  ├─ Scoring rubric (custom evaluation criteria)
│  └─ Interview comparison (compare candidates side-by-side)
│
├─ Offer generation
│  ├─ Create offer (form with salary, benefits, start date)
│  ├─ Generate offer letter (PDF)
│  ├─ Send offer (email with secure link)
│  ├─ Candidate accepts/rejects (time-stamped, logged)
│  └─ Onboarding initiation (trigger ERP workflow)
│
└─ Hiring analytics
   ├─ Conversion metrics (% applications → offers → hired)
   ├─ Time metrics (days to shortlist, interview, hire)
   ├─ Quality metrics (hire success rate, retention rate)
   └─ Compliance metrics (diversity, equal opportunity)

PHASE_4: INTEGRATIONS & CERTIFICATIONS (Months 19-24)
├─ Third-party integrations
│  ├─ LinkedIn import (pull candidate profile)
│  ├─ Indeed sync (cross-post jobs)
│  ├─ ATS integration (if employer has external ATS)
│  └─ Certification verification (auto-verify licenses, clearances)
│
├─ Compliance & verification
│  ├─ Background check integration (API to provider)
│  ├─ Certification validation (verify physics degrees, pilot licenses)
│  ├─ Security clearance status (government database lookup)
│  ├─ Work authorization verification (E-Verify integration)
│  └─ Compliance reporting (audit trails, decision logs)
│
└─ Advanced analytics & reporting
   ├─ Custom dashboards (employer can configure metrics)
   ├─ Predictive analytics (offer acceptance probability)
   ├─ Candidate success prediction (will they stay >2 years?)
   └─ ROI analysis (cost per successful hire)
```

---

## 4️⃣ JOB PORTAL AI MATCHING ENGINE (LOCKED SPECIFICATIONS)

### Match Score Algorithm

```
MATCH_SCORE_CALCULATION (0.0-1.0 scale):

COMPONENT_1: SKILLS MATCHING (Weight: 40%)
├─ Required skills: Job lists 5 skills
├─ Candidate has 4 of 5 skills: 80% match
│  └─ Formula: (Skills matched / Skills required) × 100%
├─ Proficiency level: Higher proficiency = higher match
│  ├─ Expert: 1.0 point
│  ├─ Advanced: 0.8 points
│  ├─ Intermediate: 0.6 points
│  └─ Beginner: 0.4 points
├─ Skill decay: Recent skills worth more
│  └─ Current role: 1.0x
│  └─ Prior role (1-3 years): 0.8x
│  └─ Prior role (3-7 years): 0.6x
│  └─ Prior role (>7 years): 0.4x
└─ Formula: Σ(Skill present × Proficiency level × Recency) / Required skills

COMPONENT_2: EXPERIENCE MATCHING (Weight: 25%)
├─ Years of experience: Job requires 3-5 years
├─ Candidate has: 5 years
│  └─ 100% match (within range)
├─ Candidate has: 2 years
│  └─ Partial match (below minimum), reduce score by 20%
├─ Candidate has: 10 years
│  └─ 100% match (exceeds minimum, acceptable)
└─ Formula: MIN(candidate_years / job_min_years, 1.0) × component_weight

COMPONENT_3: EDUCATION MATCHING (Weight: 15%)
├─ Job requires: Physics PhD
├─ Candidate has: Physics PhD
│  └─ 100% match
├─ Candidate has: Physics Master's
│  └─ 80% match (close but not exact)
├─ Candidate has: Physics Bachelor's
│  └─ 60% match (below requirement)
├─ Candidate has: Engineering Master's
│  └─ 70% match (related field)
└─ Formula: Degree level match × field relevance

COMPONENT_4: SPECIALIZATION MATCHING (Weight: 15%)
├─ Job specialization: Gravity field physics
├─ Candidate specialization: Gravity field physics
│  └─ 100% match
├─ Candidate specialization: Quantum mechanics
│  └─ 40% match (related but different)
├─ Candidate specialization: Materials science
│  └─ 20% match (tangential)
└─ Formula: Specialization similarity score (using domain ontology)

COMPONENT_5: LOCATION/WORK PREFERENCE (Weight: 5%)
├─ Job location: San Francisco, CA
├─ Candidate preferred location: San Francisco, CA
│  └─ 100% match
├─ Candidate preferred location: Bay Area (within 50 miles)
│  └─ 80% match
├─ Candidate preferred location: East Coast
│  └─ 0% match, but remote_eligible=true
│  └─ 50% match (remote option available)
└─ Formula: Location distance-based scoring

FINAL_MATCH_SCORE:
├─ Score = (Component1 × 0.40) + (Component2 × 0.25) + (Component3 × 0.15) 
│          + (Component4 × 0.15) + (Component5 × 0.05)
├─ Result: Decimal 0.0-1.0
├─ Threshold:
│  ├─ Excellent match: 0.85-1.0 (recommend to recruiter)
│  ├─ Good match: 0.70-0.84 (suitable for interview)
│  ├─ Fair match: 0.55-0.69 (consider if other candidates thin)
│  └─ Poor match: <0.55 (not recommended)
└─ Explainability: JSON output reasons for score

MATCH_EXPLANATION_GENERATION:
├─ Input: Match score components
├─ Output: Human-readable explanation (100-200 words)
├─ Example: "Candidate is a strong match. Has all required skills at 
│   advanced proficiency, exceeds experience requirement (5 years vs 3 required), 
│   and has PhD in physics as required. Missing: specialty in gravity field physics 
│   (has quantum mechanics instead, which is related). Would benefit from 
│   learning propulsion systems engineering."
└─ Constraints: Must be accurate, non-discriminatory, constructive
```

### Physics-Specific Matching Rules

```
GRAVITY_FIELD_SPECIALIZATION_MATCHING:
├─ Identify physics papers author published
│  ├─ Extract keywords (gravity field, General Relativity, exotic physics, etc.)
│  ├─ Match against job requirements
│  └─ Calculate research alignment score
│
├─ Verify physics equations & math level
│  ├─ Parse resume for mathematical notation
│  ├─ Assess theoretical depth (classical vs quantum)
│  └─ Match against job complexity level
│
└─ Cross-reference with Skill module
   ├─ If candidate completed antigravity physics courses: +20% bonus
   ├─ If candidate completed propulsion engineering projects: +15% bonus
   └─ If candidate has published related research: +25% bonus

SAFETY_CRITICAL_MATCHING:
├─ Pilot license validation (active, not expired)
├─ Security clearance status (level and expiration)
├─ Background check (passed, no issues)
├─ Flight hours (for flight roles, >= minimum required)
└─ Formula: All safety requirements must be 100% met (no partial credit)

GOVERNMENT_CONTRACT_MATCHING:
├─ ITAR export control: Candidate must be US citizen (no exceptions)
├─ EAR compliance: Citizenship or work authorization verification required
├─ Security clearance: Match must meet minimum clearance level
├─ Background investigation: Status (Passed/Pending/Failed)
└─ Outcome: Automatic rejection if any requirement not met

CERTIFICATION_MATCHING:
├─ Required certification: Physics PhD
├─ Candidate proof:
│  ├─ Transcript on file: 100% verified
│  ├─ Degree in progress: Partial match (85%), with conditions
│  ├─ No proof: 0% match (automatic rejection for required certs)
│  └─ Related degree (Engineering PhD): 80% match
│
├─ Verification:
│  ├─ Auto-verify via registrar API (if available)
│  ├─ Manual review by HR (if API not available)
│  └─ Audit trail: Timestamp who verified, what was verified
│
└─ Storage: Cert data encrypted, with expiration tracking
```

---

# PART 2: MIGRATION FROM LEGACY JOB SYSTEMS

## 5️⃣ LEGACY JOB SYSTEM LANDSCAPE (ASSESSED SYSTEMS)

### Common Legacy Systems

```
LEGACY_SYSTEM_TYPES:
├─ Basic Job Boards (Craigslist-like)
│  ├─ Single table: jobs (id, title, description, company)
│  ├─ No structured data (everything in description text)
│  ├─ No matching (manual by humans)
│  ├─ No applications tracking (email-based)
│  └─ Data quality: Poor (inconsistent formats, missing fields)
│
├─ LinkedIn/Indeed-Style Boards
│  ├─ Job postings + Application tracking
│  ├─ Basic search & filtering
│  ├─ Resume upload (no parsing)
│  ├─ Candidate messaging
│  └─ Data quality: Medium (more structured)
│
├─ Dedicated ATS (Applicant Tracking Systems)
│  ├─ Workday Recruiting
│  ├─ SAP SuccessFactors
│  ├─ Greenhouse
│  ├─ Taleo (Oracle)
│  └─ Features: Full workflow, interviews, offers, integrations
│
├─ Internal Custom-Built Systems
│  ├─ Usually built on SAP, Oracle, or custom code
│  ├─ Integrated with ERP (Finance, HR)
│  ├─ Custom workflows per company
│  ├─ Data quality: Varies (often poor if unmaintained)
│  └─ Documentation: Often missing
│
└─ Physics-Specific Job Boards (if any)
   ├─ Nature.com jobs
   ├─ PhysicsToday.org listings
   ├─ University career websites
   └─ Aerospace company career pages
```

### Data Migration Scope

```
JOBS_TABLE_MIGRATION:
├─ Source: Legacy job postings
├─ Target: Antigravity JOB_POSTING table
├─ Mapping:
│  ├─ job_id: Generate new UUID (legacy ID archived)
│  ├─ job_title: Direct copy (validate 5-100 chars)
│  ├─ job_description: Copy (>200 chars required)
│  ├─ salary_min/max: Extract from description (regex parse)
│  ├─ location: Extract & geocode
│  ├─ required_skills: Parse from description (domain-specific)
│  └─ posting_date: Convert timestamp
│
├─ Data quality checks:
│  ├─ Required fields present: job_title, job_description, employer_id
│  ├─ Description length: >200 characters
│  ├─ Location valid: Can be geocoded
│  ├─ Skills extractable: Can identify from text
│  └─ Date valid: In reasonable range (not future-dated)
│
└─ Remediation:
   ├─ Missing fields: Flag for manual review
   ├─ Invalid data: Exclude (don't force migrate bad data)
   ├─ Duplicate jobs: Deduplicate (keep most recent)
   └─ Acceptance threshold: >90% of jobs with valid core data

APPLICATIONS_TABLE_MIGRATION:
├─ Source: Legacy applications (from ATS or email logs)
├─ Target: Antigravity JOB_APPLICATION table
├─ Mapping:
│  ├─ application_id: Generate new UUID
│  ├─ job_id: Match to migrated job posting
│  ├─ candidate_id: Match to candidate (or create if new)
│  ├─ application_date: Convert timestamp
│  ├─ application_status: Map (Submitted → Submitted, Hired → Accepted)
│  ├─ resume: Store in document system (S3, immutable)
│  └─ other_documents: Store with same immutability
│
├─ Status mapping:
│  ├─ Submitted/Received → Submitted
│  ├─ Under Review → Reviewed
│  ├─ Shortlisted → Shortlisted
│  ├─ Rejected → Rejected (with reason)
│  ├─ Offered → Offered
│  ├─ Hired/Accepted → Accepted
│  └─ Withdrawn → Withdrawn
│
└─ Data quality checks:
   ├─ application_date valid: In reasonable range
   ├─ candidate data: Can identify or create
   ├─ job mapping: Can link to migrated job posting
   ├─ resume: File exists & readable
   └─ Acceptance threshold: >95% of applications linkable

CANDIDATE_PROFILE_MIGRATION:
├─ Source: Candidate data (resumes, profiles, registration)
├─ Target: Antigravity CANDIDATE_PROFILE table
├─ Mapping:
│  ├─ candidate_id: FK to User table (via Auth service)
│  ├─ skills: Extract from resume (NLP parsing)
│  ├─ education: Parse resume (degree, institution, year)
│  ├─ experience: Parse resume (job titles, companies, durations)
│  ├─ certifications: Extract from text (PhD, Licenses, Clearances)
│  └─ physics_specialty: Infer from education & experience
│
├─ Data quality checks:
│  ├─ Skills extractable: Can identify 3+ skills
│  ├─ Education present: Has degree (at least bachelor's)
│  ├─ Experience reasonable: Years of experience makes sense
│  ├─ Profile completeness: >60% (baseline for active candidates)
│  └─ Contact info valid: Email, phone working
│
└─ Acceptance threshold: >80% of candidates with valid core data

EMPLOYER_PROFILE_MIGRATION:
├─ Source: Company data (from job postings, contracts)
├─ Target: Antigravity EMPLOYER_PROFILE table
├─ Mapping:
│  ├─ employer_id: Generate new UUID
│  ├─ employer_name: Direct copy (validate)
│  ├─ employer_type: Infer or map (Research, Company, Government, etc.)
│  ├─ location: Geocode from address
│  ├─ industry: Infer or classify
│  └─ website: Extract from data or normalize
│
├─ Data quality checks:
│  ├─ Company name present: Valid, not generic
│  ├─ Location geocodeable: Can identify city/region
│  ├─ Contact info: Valid email/phone for HR contact
│  └─ Verification: Has signed contract or agreement
│
└─ Acceptance threshold: 100% (invalid employers not migrated)
```

---

## 6️⃣ JOB PORTAL MIGRATION TIMELINE (PHASED)

### Migration Schedule (Integrated with Platform Development)

```
PHASE_A: PREPARATION (Months 1-3, Stage 1)
├─ Month 1: Audit legacy job systems
│  ├─ Identify all job sources (indeed, linkedin, internal board, etc.)
│  ├─ Assess data quality (% valid, % duplicates)
│  ├─ Document current workflows (how jobs posted, how applications tracked)
│  ├─ Identify integrations (if any)
│  ├─ Meet with stakeholders (employers, recruiters, candidates)
│  └─ Deliverable: System landscape report
│
├─ Month 2: Design job portal architecture
│  ├─ Finalize data model (JOB_POSTING, JOB_APPLICATION, etc.)
│  ├─ Design AI matching algorithm (weights, thresholds)
│  ├─ Plan database schema & indexes
│  ├─ Design search infrastructure (Elasticsearch setup)
│  ├─ Plan integrations (LinkedIn, Indeed, API, etc.)
│  └─ Deliverable: Technical specification (this doc)
│
└─ Month 3: Build job portal MVP
   ├─ Implement core schema (jobs, applications, candidates)
   ├─ Build search functionality (basic filtering, full-text)
   ├─ Implement authentication (employer login, candidate login)
   ├─ Build job posting form (guided employer experience)
   ├─ Build application tracking (simple UI)
   └─ Deliverable: MVP (Platform Stage 1 complete)

PHASE_B: DATA MIGRATION (Months 4-6, Stage 2)
├─ Month 4: Data extraction & cleansing
│  ├─ Export job postings from legacy systems
│  ├─ Extract candidate resumes & profiles
│  ├─ Extract applications history
│  ├─ Cleanse & standardize data
│  ├─ Audit data quality (target: >90% valid)
│  └─ Deliverable: Cleaned dataset ready for migration
│
├─ Month 5: Data mapping & transformation
│  ├─ Create data mapping specification
│  ├─ Build ETL scripts (extract, transform, load)
│  ├─ Test transformation with sample data
│  ├─ Validate mapping accuracy (spot checks)
│  ├─ Create resume parsing (NLP for skills, education)
│  └─ Deliverable: Tested ETL ready for load
│
└─ Month 6: Data load & validation
   ├─ Load jobs to JOB_POSTING table (test environment)
   ├─ Load applications to JOB_APPLICATION table
   ├─ Load candidates to CANDIDATE_PROFILE table
   ├─ Validate data integrity (counts, relationships, quality)
   ├─ Create master data cleanup list (issues found)
   └─ Deliverable: Validated dataset in test environment

PHASE_C: AI MATCHING & TESTING (Months 7-9, Stage 2)
├─ Month 7: Implement AI matching engine
│  ├─ Code match score calculation algorithm
│  ├─ Implement skills matching (NLP models)
│  ├─ Implement experience & education matching
│  ├─ Implement physics specialization matching
│  ├─ Train/calibrate models with historical data
│  └─ Deliverable: Matching algorithm ready for testing
│
├─ Month 8: Testing & validation
│  ├─ Unit test matching algorithm
│  ├─ Integration test with job data
│  ├─ Validate match scores vs domain experts (get feedback)
│  ├─ Calibrate thresholds (what score = good match?)
│  ├─ Test explainability (can humans understand scores?)
│  └─ Deliverable: Validated matching engine
│
└─ Month 9: Performance & optimization
   ├─ Load test matching engine (1000+ job/candidate pairs)
   ├─ Optimize queries (response time <500ms)
   ├─ Implement caching (improve search performance)
   ├─ Test with legacy data (does it work with dirty data?)
   └─ Deliverable: Production-ready matching engine

PHASE_D: FEATURE ROLLOUT (Months 10-18, Stage 3)
├─ Month 10-11: Candidate job discovery
│  ├─ Build candidate UI (search, filter, save)
│  ├─ Implement job alerts (email notifications)
│  ├─ Build application submission (UX testing)
│  ├─ Get candidate feedback (early adopter testing)
│  └─ Deliverable: Candidate experience launched
│
├─ Month 12-13: Employer hiring tools
│  ├─ Build application review UI (for employers)
│  ├─ Implement candidate screening (shortlist, notes)
│  ├─ Build interview scheduling (calendar integration)
│  ├─ Implement offer generation (letter template)
│  └─ Deliverable: Employer experience launched
│
├─ Month 14-15: Advanced features
│  ├─ Implement AI match insights (for both sides)
│  ├─ Build analytics dashboards
│  ├─ Implement candidate skill gap analysis
│  ├─ Build salary benchmarking (if data available)
│  └─ Deliverable: Advanced features available
│
└─ Month 16-18: Integrations & scale
   ├─ LinkedIn integration (optional, if demand exists)
   ├─ Certification verification (API to credential providers)
   ├─ Background check integration
   ├─ ERP integration (offer → employee record)
   └─ Deliverable: Production system fully operational

PHASE_E: CUTOVER & DECOMMISSION (Months 19-24, Stage 4)
├─ Month 19-20: Parallel run
│  ├─ Both systems operational (legacy + Antigravity)
│  ├─ New job postings on Antigravity only
│  ├─ New applications in Antigravity, legacy read-only
│  ├─ Monitor data consistency
│  └─ Deliverable: System transition beginning
│
├─ Month 21-22: Legacy system freeze
│  ├─ No new job postings allowed in legacy
│  ├─ Legacy system read-only for archival lookups
│  ├─ All new activity on Antigravity
│  ├─ Help legacy system users transition
│  └─ Deliverable: Antigravity primary system
│
└─ Month 23-24: Legacy decommission
   ├─ Archive legacy data (backup, encrypted)
   ├─ Migrate remaining legacy data (if any)
   ├─ Decommission legacy system
   ├─ Retain read-only access for 6 months (compliance)
   └─ Deliverable: Legacy system retired

INTEGRATION_WITH_PLATFORM:
├─ Month 1-3: MVP job portal (Stage 1 foundation)
├─ Month 4-6: Data migration (Stage 2 intelligence starting)
├─ Month 7-9: AI matching (Stage 2 intelligence peak)
├─ Month 10-18: Feature rollout (Stage 3 ecosystem)
├─ Month 19-24: Cutover & decommission (Stage 4 compliance & scale)
└─ Dependency: Job portal ready before Skill module launches (Month 7)
```

---

## 7️⃣ DATA QUALITY GATES (LOCKED REQUIREMENTS)

### Migration Quality Standards

```
DATA_QUALITY_ACCEPTANCE_CRITERIA:

JOBS_POSTING_TABLE:
├─ Record count validation: Actual ≥ 95% of legacy
├─ Required fields: 100% have job_title, job_description, employer_id
├─ Field length validation:
│  ├─ job_title: 5-100 chars (100% compliance)
│  ├─ job_description: >200 chars (100% compliance)
│  ├─ salary_min/max: Valid numeric (if present, 100%)
│  └─ location: Geocodeable (95%+ geolocated)
├─ Data type validation: All fields match schema
├─ Relationship validation: All employer_ids exist in EMPLOYER_PROFILE
└─ GATE_DECISION: If all pass → Proceed to applications migration

JOB_APPLICATION_TABLE:
├─ Record count validation: Actual ≥ 98% of legacy
├─ Required fields: 100% have job_id, candidate_id, application_date
├─ Field validation:
│  ├─ job_id: All link to valid JOB_POSTING (100%)
│  ├─ candidate_id: All link to valid CANDIDATE_PROFILE (100%)
│  ├─ application_status: Valid enum (100%)
│  ├─ application_date: In valid range, not future-dated (100%)
│  └─ resume_file: Readable & not corrupted (95%+)
├─ Data integrity: Applications linked to valid jobs & candidates
├─ Audit trail: All state changes logged with timestamp
└─ GATE_DECISION: If all pass → Proceed to candidate profile migration

CANDIDATE_PROFILE_TABLE:
├─ Record count validation: Actual ≥ 90% of legacy unique candidates
├─ Required fields: 100% have candidate_id (FK to User)
├─ Profile completeness:
│  ├─ All candidates: 60%+ complete (some data, email, phone)
│  ├─ Active candidates: 75%+ complete (education, experience)
│  ├─ Educated candidates: 100% have degree_level, degree_discipline
│  └─ Data present: >60% have extracted skills
├─ Data accuracy:
│  ├─ Education dates: Bachelor before Master before PhD (logic check)
│  ├─ Experience: Years of experience > 0 and < 70 (sanity check)
│  ├─ Certifications: Valid (match against known certifications)
│  └─ Skills: >3 identified per candidate (on average)
├─ Relationship validation: candidate_id links to valid User
└─ GATE_DECISION: If all pass → Proceed to employer profile migration

EMPLOYER_PROFILE_TABLE:
├─ Record count validation: 100% of legacy employers (complete coverage)
├─ Required fields: 100% have employer_name, employer_id
├─ Verification status: 100% marked verified=true (no invalid employers)
├─ Data validation:
│  ├─ employer_name: Not generic, actual company name
│  ├─ location: Geocodeable (100%)
│  ├─ employer_type: Valid enum (100%)
│  └─ industry: Identified or classified (95%+)
├─ Contact info: Valid hiring manager or HR contact (95%+)
└─ GATE_DECISION: If all pass → Proceed to AI matching training

AI_MATCHING_VALIDATION:
├─ Model accuracy: Match score reliability ≥85% (validated against domain experts)
├─ Explainability: Humans understand match reasons (usability tested)
├─ Calibration: Score thresholds match hiring patterns
│  ├─ 0.85-1.0: "Excellent" - should result in interviews
│  ├─ 0.70-0.84: "Good" - suitable candidates
│  ├─ <0.70: "Fair/Poor" - requires strong justification
│  └─ Threshold validation: Against 100+ historical hires
├─ Physics specialization matching: Validated by domain experts
├─ Fairness: No bias against protected characteristics (tested)
└─ GATE_DECISION: If all pass → Proceed to production deployment

COMPLIANCE_VALIDATION:
├─ ITAR/EAR controls: Implemented & tested (government contract roles)
├─ Security clearance: Integration working (status lookup accurate)
├─ Background check: Integration functional (if needed)
├─ Certification verification: Auto-verify working (>90% accuracy)
├─ Audit trail: All actions logged, immutable, searchable
└─ GATE_DECISION: If all pass → System ready for production use
```

---

## 8️⃣ GOVERNANCE & APPROVALS (LOCKED STRUCTURE)

### Job Portal Governance

```
JOB_PORTAL_STEERING_COMMITTEE (Strategic):
├─ VP Product (Chair)
├─ VP Engineering
├─ Chief Compliance Officer
├─ Finance (job posting costs)
├─ HR Lead (employer feedback)
├─ Recruiter Lead (user feedback)
└─ Legal (compliance, contracts)

Responsibilities:
├─ Approve job portal features & roadmap
├─ Approve data migration plan & timeline
├─ Review quality gates & thresholds
├─ Approve AI matching algorithm & weights
├─ Monitor implementation progress
└─ Resolve cross-functional issues

Cadence:
├─ Weekly during migration (Months 1-24)
├─ Monthly post-launch (Months 25+)

PRODUCT_TEAM (Tactical Execution):
├─ Product Manager (Chair)
├─ Data Scientist (AI matching)
├─ Engineering Lead (backend)
├─ Frontend Lead (UI/UX)
├─ Quality Engineer (testing)
├─ Data Engineer (ETL, data quality)
└─ Compliance Officer (regulatory)

Responsibilities:
├─ Execute feature development
├─ Run data migration
├─ Ensure quality gates met
├─ Test all features
├─ Document decisions & learnings
└─ Report to steering committee

Cadence:
├─ Daily standups during active phases
├─ Twice-weekly detailed reviews
├─ Weekly metrics reporting

SUBJECT_MATTER_EXPERTS (Functional):
├─ Recruiters (hiring process, UX)
├─ Employers (job posting, applicant needs)
├─ Candidates (search, apply, match feedback)
├─ Physics domain expert (specialization matching)
├─ Compliance expert (regulatory, audit trails)
└─ Data expert (quality, cleansing, validation)

Responsibilities:
├─ Define requirements & workflows
├─ Provide feedback on designs
├─ Test features & functionality
├─ Validate AI matching explanations
├─ Sign off on quality gates
└─ Provide user training

Cadence:
├─ As needed during development
├─ Intensive during migration phases
├─ Monthly feedback sessions post-launch

APPROVAL_AUTHORITY:
├─ Feature changes: Product Manager
├─ Architecture changes: VP Engineering
├─ Algorithm changes: Data Scientist + Physics expert
├─ Quality gate exceptions: VP Product + Compliance
├─ Timeline/scope changes: Steering Committee
├─ Budget overruns: VP Finance
└─ Go-live decision: Steering Committee (unanimous approval required)
```

---

## 9️⃣ JOB PORTAL LAUNCH CHECKLIST (LOCKED)

### Pre-Launch Gates

```
GATE_1: DATA MIGRATION COMPLETE (Before Month 10)
- [ ] All jobs migrated (>95% success rate)
- [ ] All applications migrated (>98% success rate)
- [ ] All candidates migrated (>90% success rate)
- [ ] All employers migrated (100% success rate)
- [ ] Data quality >95% for all tables
- [ ] Audit trail complete (all changes logged)
- [ ] Data validated against legacy (spot checks, reconciliation)
- [ ] Steering Committee sign-off

GATE_2: AI MATCHING READY (Before Month 10)
- [ ] Algorithm implemented & tested
- [ ] Model trained on historical data
- [ ] Accuracy validated >85% (domain expert testing)
- [ ] Explainability tested (users understand reasons)
- [ ] Performance tested (<500ms response time)
- [ ] Physics specialization matching working
- [ ] Fairness audit completed (no bias detected)
- [ ] Steering Committee sign-off

GATE_3: CANDIDATE FEATURES READY (Before Month 12)
- [ ] Job search functionality working
- [ ] Job filtering & facets functional
- [ ] Job alerts working (email delivery tested)
- [ ] Application submission functional
- [ ] Application tracking updated in real-time
- [ ] Resume upload & parsing working
- [ ] User testing completed (10+ users, positive feedback)
- [ ] UI/UX approved by design & product
- [ ] Steering Committee sign-off

GATE_4: EMPLOYER FEATURES READY (Before Month 13)
- [ ] Employer registration & verification working
- [ ] Job posting form working (guided experience)
- [ ] Application management interface functional
- [ ] Candidate screening features working
- [ ] Hiring workflow customizable
- [ ] Reporting & analytics functional
- [ ] Integration with ERP tested (offer → employee record)
- [ ] User testing completed (10+ employers, positive feedback)
- [ ] Steering Committee sign-off

GATE_5: COMPLIANCE & SECURITY READY (Before Month 15)
- [ ] ITAR/EAR controls implemented (government contracts)
- [ ] Security clearance lookup working (if applicable)
- [ ] Background check integration working (if needed)
- [ ] Certification verification automated
- [ ] Audit trail complete & immutable
- [ ] Access controls tested & working
- [ ] Data encryption verified (in transit & at rest)
- [ ] Penetration testing completed (no critical vulnerabilities)
- [ ] Compliance sign-off

GATE_6: PRODUCTION READINESS (Before Month 19)
- [ ] All features tested (unit, integration, e2e)
- [ ] Performance tested under load (1000+ concurrent users)
- [ ] Backup & recovery tested (RTO acceptable)
- [ ] Monitoring & alerting configured (dashboards live)
- [ ] Support team trained & ready (help desk)
- [ ] Communication plan executed (stakeholders aware)
- [ ] Rollback plan documented & tested
- [ ] Steering Committee final sign-off

LAUNCH_DECISION:
├─ If all gates passed: APPROVED for production deployment
├─ If 1 gate failed: Resolve issue, re-test, continue
├─ If 2+ gates failed: Halt launch, review, extend timeline
└─ Authority: Steering Committee unanimous decision
```

---

## 🔟 RISK REGISTER & CONTINGENCIES

### Identified Risks

```
RISK_1: DATA QUALITY ISSUES IN MIGRATION
├─ Probability: High (common in legacy migrations)
├─ Impact: HIGH (bad data = poor matching, user confusion)
├─ Mitigation:
│  ├─ Audit legacy data pre-migration (identify issues early)
│  ├─ Cleansing campaign (fix obvious issues)
│  ├─ Validation at each step (extract, transform, load)
│  ├─ Quality gates >95% (exclude bad data)
│  └─ Manual review process (fix exceptions)
├─ Residual Risk: Low
└─ Contingency: Exclude bad data, re-migrate later with fixes

RISK_2: AI MATCHING ACCURACY LOW (<85%)
├─ Probability: Medium (depends on model training)
├─ Impact: CRITICAL (matching is core feature)
├─ Mitigation:
│  ├─ Domain expert validation (physicist reviews algorithm)
│  ├─ Historical data training (use real hiring patterns)
│  ├─ Explainability testing (humans understand scores)
│  ├─ Fairness audit (no bias against groups)
│  └─ Iterative refinement (adjust weights based on feedback)
├─ Residual Risk: Medium
└─ Contingency: Disable matching, launch with manual matching only

RISK_3: EMPLOYER ADOPTION / RESISTANCE
├─ Probability: Medium (change resistance common)
├─ Impact: HIGH (no employers = no jobs = failed product)
├─ Mitigation:
│  ├─ Early stakeholder engagement (beta testing)
│  ├─ Training program (how to use system)
│  ├─ Support team (help with issues)
│  ├─ Incentives (discount for early adopters)
│  └─ Feedback loops (listen and improve)
├─ Residual Risk: Medium
└─ Contingency: Extended support, feature customization, pricing changes

RISK_4: PERFORMANCE DEGRADATION UNDER LOAD
├─ Probability: Medium (common in scaled systems)
├─ Impact: HIGH (slow search = users leave)
├─ Mitigation:
│  ├─ Load testing (1000+ concurrent users)
│  ├─ Database optimization (indexes, queries)
│  ├─ Caching strategy (Redis, CDN)
│  ├─ Auto-scaling (add resources as needed)
│  └─ Monitoring (detect slowness, alert on issues)
├─ Residual Risk: Low
└─ Contingency: Optimize queries, add resources, roll back features

RISK_5: SECURITY / COMPLIANCE ISSUES
├─ Probability: Low (with proper controls)
├─ Impact: CRITICAL (data breach = legal liability)
├─ Mitigation:
│  ├─ Threat modeling (identify risks)
│  ├─ Penetration testing (find vulnerabilities)
│  ├─ Security review (code, architecture, operations)
│  ├─ Compliance audit (ITAR, EAR, etc.)
│  ├─ Encryption (data in transit & at rest)
│  └─ Access controls (least privilege)
├─ Residual Risk: Very Low
└─ Contingency: Incident response plan, legal counsel, notification

RISK_6: INTEGRATION FAILURES (ERP, BACKGROUND CHECK, etc.)
├─ Probability: Medium (third-party systems often break)
├─ Impact: HIGH (offer process stalled)
├─ Mitigation:
│  ├─ Integration testing (with partners pre-launch)
│  ├─ Fallback to manual (if integration fails)
│  ├─ Error monitoring (detect failures quickly)
│  ├─ Vendor SLAs (contractual commitments)
│  └─ Documentation (integration procedures)
├─ Residual Risk: Medium
└─ Contingency: Manual workarounds, escalate to vendor, timeline extension

RISK_7: CANDIDATE DATA PRIVACY BREACHES
├─ Probability: Low (proper controls in place)
├─ Impact: CRITICAL (GDPR/CCPA fines, reputation damage)
├─ Mitigation:
│  ├─ Data minimization (collect only needed data)
│  ├─ Consent management (candidates opt-in)
│  ├─ Encryption (sensitive data encrypted)
│  ├─ Access logging (who accessed what, when)
│  ├─ Retention policy (delete data after period)
│  └─ Privacy notice (clear disclosure)
├─ Residual Risk: Very Low
└─ Contingency: Legal response, notifications, credit monitoring

RISK_8: PHYSICS SPECIALIZATION MATCHING WRONG
├─ Probability: Medium (domain-specific, hard to get right)
├─ Impact: HIGH (wrong matches = poor hiring outcomes)
├─ Mitigation:
│  ├─ Domain expert involvement (physicist validates)
│  ├─ Research paper analysis (extract specialization from publications)
│  ├─ Certification verification (gravity field courses, etc.)
│  ├─ User feedback loop (collect feedback, refine)
│  └─ A/B testing (test different algorithms)
├─ Residual Risk: Medium
└─ Contingency: Manual specialization assignment, delay feature launch

CONTINGENCY_BUDGET: 15% of job portal development budget
CONTINGENCY_TIMELINE: +2-4 weeks for major issues
CONTINGENCY_STAFFING: 1-2 extra engineers on-call for critical issues
```

---

## SEALING & LOCKING CERTIFICATION

```
THIS DOCUMENT IS SEALED AND LOCKED.

Seal: 2025-02-24 | Version: 1.0 | Status: LOCKED
Classification: JOB PORTAL MIGRATION STRATEGY
Authority: VP Product + CTO + Chief Compliance Officer

CONSTRAINTS:
✓ No creative interpretation of job portal features allowed
✓ Data migration must follow documented procedures
✓ AI matching threshold of 85% accuracy (mandatory, no lower)
✓ Data quality >95% acceptance (mandatory gate)
✓ All quality gates must be met (no exceptions)
✓ Physics specialization matching requires domain expert validation
✓ Government contract roles: ITAR/EAR controls mandatory
✓ All changes to architecture require VP Product approval

APPROVAL SIGNATURES:
├─ VP Product: [SEALED]
├─ CTO: [SEALED]
└─ Chief Compliance Officer: [SEALED]

NEXT REVIEW: 2025-06-24 (6 months)
IMPLEMENTATION DEADLINE: 2025-09-24 (when Stage 3 begins)

🔐 SEALED END 🔐
```

---

**END OF JOB_PORTAL_MIGRATION.MD**

*Generated for Antigravity Control Systems (ACS) - Enterprise Multi-Tenant SaaS Platform*
*Classification: JOB PORTAL MIGRATION STRATEGY*
*Last Updated: 2025-02-24*
