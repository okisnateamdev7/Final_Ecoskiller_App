# 🎓 CANDIDATE MODULE — ORCHESTRATOR AGENT
## EcoSkiller Antigravity Architecture | Master Orchestration Specification v1.0

```
Artifact Class:           Orchestrator Agent Blueprint — ANTIGRAVITY TIER
Module Name:              Candidate Module Orchestrator Agent (CAND-OA)
Mutation Policy:          Add-only via version bump
Execution Mode:           Deterministic · Event-Driven · Multi-Agent Coordination
Stack Lock:               Enforced (inherits EcoSkiller master stack)
Interpretation Authority: NONE
Sealed Status:            FINAL · LOCKED · GOVERNED · PRODUCTION-READY
Failure Mode:             STOP → REPORT → NO PARTIAL ORCHESTRATION
Determinism Rule:         Identical input → Identical orchestration output
MCP Category:             CAT-11 (Championship + Parent Predictive AI) +
                          CAT-12 (Institute + HR Predictive Systems)
Version:                  CAND-OA-v1.0.0
Platform:                 EcoSkiller v12.0 — Unified Job + Skill + Project +
                          Education + Marketplace SaaS
Target Users:             Students · Job Seekers · Professionals · Career Returners
Scale Target:             10M–100M Candidates | 488 Million Rural India Addressable
Issued Under:             EcoSkiller Master Execution Prompt v12.0
                          Dojo SaaS Production Artifact Spec v1.0
                          Talent Operating System Blueprint v1.0
```

---

## 📋 EXECUTIVE SUMMARY

The **Candidate Module Orchestrator Agent (CAND-OA)** is the **unified intelligence coordination layer** for the entire candidate lifecycle on EcoSkiller's Antigravity platform. It is the master conductor that receives every candidate-facing intent — from first account creation at age 5 through career placement, promotion, and beyond — and orchestrates the precise sequence of specialist AI agents, feature systems, and data services required to deliver a seamless, verified, and outcome-driven candidate experience.

The CAND-OA does **not** execute ML inference, skill scoring, or career predictions itself. It **plans, routes, sequences, and aggregates** across 25 candidate feature modules and 12 specialist AI agents — enforcing trust gates, data contracts, bias rules, and audit trails at every step.

### Platform Mission Alignment

EcoSkiller serves **488 million rural internet users in India** who currently have no access to quality skills training leading to real employment. The Candidate Module is the front-door of this mission — transforming a student from raw academic status to verified, employer-trusted career identity through an AI-driven, vernacular-first, offline-capable, gamified system.

Every orchestration decision the CAND-OA makes must honour three core principles:
1. **Evidence-first** — no skill, certification, or rank is claimed without proof
2. **India-first** — 2G connectivity, 8-language support, offline capability, rural psychology
3. **Antigravity** — remove every friction point between candidate potential and career outcome

### Candidate Module Scope — 25 Feature Modules

| # | Feature Module | Core Purpose |
|---|---|---|
| 01 | Identity & Account Creation | EUID — Lifelong Universal Career ID |
| 02 | Candidate Profile Management | Dynamic evidence-backed professional record |
| 03 | Candidate Settings | 7 control zones, 50+ preferences, DPDP compliant |
| 04 | Dojo Skill System | Belt-based, proctored, verified skill progression |
| 05 | Skill Competency System | 16-family taxonomy, ELO ranking, XP economy |
| 06 | Intelligence Discovery | 8-Gardner + RIASEC + Behavioral Trait DNA Vector |
| 07 | Resume & Portfolio System | AI-generated, EUID-linked, QR-verifiable resume |
| 08 | Project & Work Portfolio | Cryptographically signed real-work proof records |
| 09 | Job Portal | Smart, bias-free, skill-verified job matching |
| 10 | Job Application System | End-to-end placement for Tier-2/3 graduates |
| 11 | Interview System | Automated Voice GD + Technical + SME hiring |
| 12 | Career Guidance | Placement readiness, offer evaluation, career tracking |
| 13 | Training Program | Active, performance-measured, career-outcome-linked |
| 14 | Learning Management System | Self-paced + live + cohort, NAAC/NIRF compliant |
| 15 | Mentorship Module | Structured, goal-oriented, measurable mentoring |
| 16 | Workshops | 12 live+recorded workshops, mentor-guided |
| 17 | Social Media Integration | LinkedIn, GitHub, portfolio unification |
| 18 | Messaging & Communication | Governed, audited, end-to-end encrypted comms |
| 19 | Gamification | 16 mechanics: XP, belts, leaderboards, challenges |
| 20 | Awards & Recognition | 50+ badge types, championships, scholarships |
| 21 | Candidate Analytics | GD analytics, performance dashboards, score history |
| 22 | Competition | 3-level competitive structure — batch → national |
| 23 | Subscription Plans | FREE / BASIC / PRO / ELITE tier access control |
| 24 | Shark Tank Pitch | Investor pitch platform for innovative candidates |
| 25 | Project Execution | 310+ features, 20 modules, 36-sprint build system |

### Key Impact Metrics

| Dimension | Value |
|---|---|
| Identity Setup Time | < 5 minutes on 2G mobile |
| Skill Verification Standard | Proctored, mentor-scored, cryptographically signed |
| Intelligence Dimensions Measured | 8 (Howard Gardner) + Holland RIASEC + Behavioral |
| Belt Levels | 8 (White → Diamond) |
| Placement Timeline (Dojo direct) | 8 days vs. 42-day industry average |
| Resume Sections with System Verification | 100% evidence-backed (no self-declaration) |
| Language Support | 8 Indian languages + English |
| Candidate Lifecycle Coverage | Age 5 → Career Retirement |

---

## 🗺️ SECTION 1: ORCHESTRATOR ARCHITECTURE

### 1.1 The 25-Module Feature Ecosystem + 12 AI Agents

The CAND-OA coordinates **25 candidate feature modules** and **12 specialist AI/ML agents** organised across **6 orchestration domains**:

```
CANDIDATE MODULE ORCHESTRATOR AGENT (CAND-OA)
│
├── DOMAIN A: IDENTITY & TRUST FOUNDATION
│   ├── Feature 01 — Identity & Account Creation (EUID)
│   ├── Feature 03 — Candidate Settings (7 zones / 50+ controls)
│   ├── Agent 01  — FAKE_PROFILE_DETECTION_MODEL (PHANTOM SHIELD)
│   └── Agent 02  — CERTIFICATE_AUTHENTICITY_CLASSIFIER (CAC)
│
├── DOMAIN B: INTELLIGENCE & SKILL DISCOVERY
│   ├── Feature 06 — Intelligence Discovery (8-Gardner + RIASEC + Behavioral)
│   ├── Feature 04 — Dojo Skill System (8 belt levels)
│   ├── Feature 05 — Skill Competency System (16 families / ELO / XP)
│   ├── Agent 03  — STRUCTURED_SKILL_EXTRACTION_MODEL (SSEM)
│   ├── Agent 04  — HIDDEN_TALENT_DETECTION_AGENT (HTDA)
│   └── Agent 05  — CAREER_PREDICTION_AGENT (CPA)
│
├── DOMAIN C: PROFILE, PORTFOLIO & CREDENTIAL BUILDING
│   ├── Feature 02 — Candidate Profile Management
│   ├── Feature 07 — Resume & Portfolio System (EUID-linked)
│   ├── Feature 08 — Project & Work Portfolio (cryptographic proofs)
│   ├── Feature 17 — Social Media Integration (LinkedIn / GitHub)
│   └── Agent 06  — Antigravity_Candidate_Summary_Generator (CSG)
│
├── DOMAIN D: LEARNING, GROWTH & DEVELOPMENT
│   ├── Feature 13 — Training Program (10 modules)
│   ├── Feature 14 — Learning Management System (LMS)
│   ├── Feature 15 — Mentorship Module
│   ├── Feature 16 — Workshops (12 types)
│   ├── Feature 19 — Gamification (16 mechanics)
│   ├── Feature 20 — Awards & Recognition (50+ badge types)
│   └── Agent 07  — Antigravity_Promotion_Probability_Model (APPM)
│
├── DOMAIN E: CAREER PLACEMENT & JOB MARKET
│   ├── Feature 09 — Job Portal
│   ├── Feature 10 — Job Application System
│   ├── Feature 11 — Interview System (Voice GD + Technical + SME)
│   ├── Feature 12 — Career Guidance
│   ├── Feature 22 — Competition (3 levels)
│   ├── Agent 08  — CAREER_PROBABILITY_MODEL (CPM)
│   ├── Agent 09  — ANTIGRAVITY_PLACEMENT_PROBABILITY_MODEL (PPM)
│   ├── Agent 10  — INCOME_PREDICTION_MODEL (IPM)
│   └── Agent 11  — PLACEMENT_INTERVENTION_AGENT (PIA)
│
└── DOMAIN F: ANALYTICS, ENGAGEMENT & MONETISATION
    ├── Feature 18 — Messaging & Communication
    ├── Feature 21 — Candidate Analytics
    ├── Feature 23 — Subscription Plans (FREE/BASIC/PRO/ELITE)
    ├── Feature 24 — Shark Tank
    ├── Feature 25 — Project Execution
    └── Agent 12  — REPUTATION_SCORE_ENGINE (RSE)
```

### 1.2 MCP Server Binding

```
PRIMARY MCP SERVER:   CAT-12 (mcp-12-institute-hr)
  Relevant Tools:     candidate_summary_generator
                      talent_retrieval_vector_engine
                      placement_probability_model
                      performance_forecast_model
                      retention_probability_model
                      interview_semantic_analyzer
                      benchmark_comparison_engine

SECONDARY MCP SERVER: CAT-11 (mcp-11-championship-parent-ai)
  Relevant Tools:     career_probability_model
                      income_prediction_model
                      learning_behavior_drift
                      dropout_risk_model
                      peer_comparison_engine
                      academic_intelligence_correlation
                      parent_llm_career_guidance
                      competition_forecast_engine

Transport:            stdio (JSON-RPC 2.0)
MCP Version:          2024-11-05
Architecture:         ANTIGRAVITY
Java Version:         11+ (zero external dependencies)
```

### 1.3 Candidate Lifecycle State Machine

The CAND-OA tracks every candidate across a **7-state career lifecycle**. The state governs which features and agents are accessible, which subscription tier unlocks which capabilities, and which orchestration flows are triggered:

```
STATE 0: UNREGISTERED
  → Trigger: First visit / app install
  → Orchestration: Identity creation flow (Feature 01)
  → Agents: FAKE_PROFILE_DETECTION_MODEL (background)

STATE 1: STUDENT
  → Features Active: Intelligence Discovery, Dojo (basic), Gamification,
                     Awards (participation), LMS, Training
  → Agents Active: HIDDEN_TALENT_DETECTION, CAREER_PREDICTION
  → Subscription Gate: FREE tier

STATE 2: JOB SEEKER
  → Features Active: All STATE 1 + Job Portal, Job Application,
                     Interview System, Career Guidance, Competition
  → Agents Active: All STATE 1 + PLACEMENT_PROBABILITY, CAREER_PROBABILITY,
                   INCOME_PREDICTION, PLACEMENT_INTERVENTION
  → Subscription Gate: BASIC/PRO tier

STATE 3: PLACEMENT PROCESS
  → Features Active: All STATE 2 + Interview System (active), Resume Builder
                     (export-enabled), Candidate Summary (shareable)
  → Agents Active: All STATE 2 + CANDIDATE_SUMMARY_GENERATOR,
                   INTERVIEW_SEMANTIC_ANALYZER
  → Trigger: Application submitted + GD/Interview scheduled

STATE 4: OFFER STAGE
  → Features Active: Career Guidance (offer evaluation), Messaging (offer threads),
                     Salary Comparison, Placement Tracker
  → Agents Active: INCOME_PREDICTION (offer validation), PLACEMENT_PROBABILITY
                   (acceptance modelling)
  → Trigger: Offer letter received

STATE 5: PROFESSIONAL
  → Features Active: All candidate features + Mentorship (mentor mode),
                     Portfolio (work records), Project Marketplace,
                     Shark Tank, Social Media (enriched)
  → Agents Active: PROMOTION_PROBABILITY, CAREER_PROBABILITY (growth mode),
                   REPUTATION_SCORE_ENGINE
  → Subscription Gate: PRO/ELITE tier

STATE 6: MENTOR
  → Features Active: Mentor profile, Mentorship module (host),
                     Workshop facilitation, Belt calibration
  → Agents Active: RSE (mentor trust scoring), APPM (calibration governance)
  → Trust Gate: RSE TIER-3 minimum required

STATE 7: CAREER RETURNER
  → Features Active: Return-Ready profile mode, Re-skilling LMS path,
                     Gap-fill training, Portfolio re-activation
  → Agents Active: CAREER_PREDICTION (re-entry), SKILL_GAP analysis,
                   HIDDEN_TALENT_DETECTION (dormant talent resurfacing)
  → Trigger: Account inactive >180 days + manual return signal
```

---

## ⚙️ SECTION 2: ORCHESTRATION LOGIC

### 2.1 Intent Classification Engine

The CAND-OA classifies all candidate-facing requests into **8 primary intent categories** before routing:

```
INTENT TABLE

Code      | Intent Name                | Primary Domain Activated
----------|----------------------------|--------------------------------------------
CAND-I01  | Identity & Onboarding      | Domain A — Identity & Trust Foundation
CAND-I02  | Skill Discovery & Growth   | Domain B — Intelligence & Skill Discovery
CAND-I03  | Profile & Portfolio Build  | Domain C — Profile, Portfolio & Credentials
CAND-I04  | Learning & Development     | Domain D — Learning, Growth & Development
CAND-I05  | Job Search & Placement     | Domain E — Career Placement & Job Market
CAND-I06  | Interview & Assessment     | Domain E (interview sub-flow)
CAND-I07  | Analytics & Progress       | Domain F — Analytics, Engagement & Monetisation
CAND-I08  | Trust & Safety Check       | Domain A — Trust gate enforcement cross-domain
```

### 2.2 Request Decomposition Protocol

```
DECOMPOSITION STEPS (run for every candidate request):

Step 1 — PARSE           Extract: candidate_id, lifecycle_state,
                          subscription_tier, intent_type, payload
Step 2 — TRUST CHECK     RSE reputation gate + FPDM fraud signal check
Step 3 — STATE RESOLVE   Map lifecycle state → permitted feature/agent set
Step 4 — CLASSIFY        Map to intent category (CAND-I01 to CAND-I08)
Step 5 — PLAN DAG        Generate agent + feature execution DAG (parallel where safe)
Step 6 — GATE CHECK      Verify contract gates (Section 4) — stop if any fail
Step 7 — DISPATCH        Execute DAG: parallel tasks fired, sequential respected
Step 8 — AGGREGATE       Collect outputs from all agents + feature services
Step 9 — SYNTHESIZE      Merge into unified, persona-appropriate response
Step 10 — AUDIT LOG      Write immutable execution record (TimescaleDB)
Step 11 — DELIVER        Return structured, trust-anchored response to candidate
```

### 2.3 Parallel vs. Sequential Execution Rules

```
PARALLEL EXECUTION PERMITTED:
  ✓ Intelligence Discovery + Fake Profile Detection (independent)
  ✓ Skill Extraction + Career Prediction + Income Prediction
  ✓ Hidden Talent Detection + Peer Comparison (independent signals)
  ✓ Gamification XP update + Award badge check + Leaderboard refresh
  ✓ LinkedIn import + GitHub import (independent data sources)
  ✓ LMS progress tracking + Training completion update

SEQUENTIAL EXECUTION REQUIRED:
  → EUID creation MUST complete before any other feature activates
  → FPDM (fake profile) MUST clear BEFORE profile becomes recruiter-visible
  → CAC (certificate check) MUST complete before belt is published to profile
  → Skill Extraction (SSEM) feeds → Career Prediction (CPA) → Income Prediction (IPM)
  → Intelligence Discovery feeds → Career Probability Model (CPM)
  → Placement Probability MUST complete before resume is surfaced to recruiters
  → Promotion Probability MUST complete before belt upgrade is published
  → Voice GD scoring MUST complete before Interview System unlocks next stage
  → Subscription tier MUST be verified before ELITE/PRO feature dispatch
```

---

## 🔀 SECTION 3: DOMAIN WORKFLOWS

### DOMAIN A — IDENTITY & TRUST FOUNDATION

**Trigger:** New candidate registration OR trust event detected on existing account

**Orchestration Flow:**

```
WORKFLOW: CANDIDATE ONBOARDING (CAND-I01)

STEP 1: Basic Registration — Mobile OTP anchor
  Input:  mobile_number, full_name, date_of_birth
  Action: OTP dispatch → verify → create base account record
  Gate:   Bot detection + duplicate account check (FPDM background scan)
  SLA:    < 5 minutes on 2G — mandatory India-first design
  Output: Provisional EUID (6 components: District + Category + Year + Sequence + Check)

STEP 2: Role Selection & Profile Initialisation
  Input:  role_type (Student/Job Seeker/Professional/Mentor)
  Action: Lifecycle state set → subscription tier initialised (FREE default)
          → RBAC roles provisioned → feature flags activated
  Output: Active EUID + career lifecycle state = STATE 1 or 2

STEP 3: Progressive Verification (6 Levels — unlocks progressively)
  Level 1: Mobile + Email (browse jobs, apply SME listings)
  Level 2: Institute Email Validated (campus drives, recruiter search)
  Level 3: Academic Records (salary tools, offer tracking)
  Level 4: Aadhar/Gov ID hash (trust badge, 3–5x recruiter contact boost)
  Level 5: Institute Approval Handshake (full campus placement activation)
  Level 6: Work History + LinkedIn verified (Professional tier full access)
  Agent:   CAC validates every document upload for tampering/authenticity

STEP 4: PHANTOM SHIELD Background Scan [FPDM]
  Action: Multi-signal fraud detection on new profile
          (device fingerprint, behavioral pattern, duplicate detection,
           identity signal cross-check)
  Output: Trust flag severity: GREEN / YELLOW / RED
  Gate:   RED → account quarantined, recruiter visibility blocked
          YELLOW → trust advisory shown, limited access
          GREEN → full access proceeds
  Write:  trust_flags table (append-only)

STEP 5: EUID QR Code Generation
  Action: SHA-256 signed QR code generated
  Output: Shareable EUID QR → recruiter can verify in 10 seconds
  Cache:  Redis TTL 24hr (regenerated on profile update)

STEP 6: 8-Language AI Onboarding
  Action: Detect preferred language → serve onboarding in vernacular
          Languages: Hindi, Marathi, Tamil, Telugu, Kannada,
                     Bengali, Gujarati, Malayalam, English
  Output: Personalised onboarding journey in candidate's language

STEP 7: Settings Initialisation [Feature 03]
  Action: Default settings applied (privacy-maximum, learning-optimised)
          → 7 zones configured: Account & Security, Privacy & Visibility,
          Notifications, Learning & Dojo, Accessibility, Payment, Data
  Output: Settings profile persisted, 60-second cross-device propagation active
```

---

### DOMAIN B — INTELLIGENCE & SKILL DISCOVERY

**Trigger:** Candidate completes registration OR requests intelligence assessment OR Dojo activity event fires

**Orchestration Flow:**

```
WORKFLOW: INTELLIGENCE DNA + SKILL MAPPING (CAND-I02)

STEP 1: Intelligence Discovery Assessment [Feature 06]
  Input:  candidate_id + lifecycle_state
  Modules activated (10):
    F01: Intelligence Test (Howard Gardner 8-type framework)
    F02: Personality Analysis (Behavioral Trait Engine)
    F03: Career Aptitude Assessment (Holland RIASEC model)
    F04: Skill Discovery Questionnaire
    F05: AI Career Recommendations (ANTIGRAVITY ASRA Engine)
    F06: Strength / Weakness Profile
    F07: Learning Path Constructor (ALPA DAG engine)
    F08: Career Roadmap (4-horizon planning)
    F09: Intelligence Report (cryptographically signed PDF)
    F10: Mentor Matching (trust-gated MMA agent)
  Output: Intelligence DNA Vector (permanent, cryptographically signed)
          → feeds all AI recommendations for the candidate's lifetime

STEP 2: Structured Skill Extraction [SSEM Agent]
  Input:  All integrated tool signals (GitHub commits, Jira closures,
          Salesforce deals, Figma iterations, Moodle completions,
          HackerRank results, Mentor Dojo scores)
  Action: Raw work reality → EcoSkiller Verified Skill Record
  Logic:  Direct signals (code commit → Coding skill)
          Inferred signals (Slack patterns → Communication skill)
  Output: Verified skill inventory with evidence classification
          + trust weight per skill per evidence type
  MCP:    ecoskiller.enterprise.dne.ssem namespace

STEP 3: Dojo Skill System Activation [Feature 04]
  Input:  Skill inventory + Intelligence DNA Vector
  Action: Assign starting belt level (White by default)
          → Generate AI Skill Roadmap (personalised to career goal)
          → Activate belt progression engine
  Belt Levels: White → Yellow → Orange → Green → Blue → Purple → Red → Diamond
  Features:    Proctored assessments, mentor scoring, national ranking,
               championship eligibility, XP economy, mission system
  Output: Active Dojo profile + belt record + national rank (ELO-based)

STEP 4: Skill Competency System Calibration [Feature 05]
  Input:  Dojo scores + SSEM extracted skills
  Action: Map skills to 16-family taxonomy
          → Compute ELO ranking per skill family
          → Initialise XP account
          → Assign AI coach (7-layer feedback engine)
  16 Skill Families: Technology, Analytics, Design, Business, Communication,
                      Leadership, Finance, Legal, Healthcare, Education,
                      Engineering, Marketing, Operations, Research,
                      Creative Arts, Social Sciences
  Output: Skill Passport (immutable, cryptographically signed records)
          + AI coaching schedule + market benchmark comparison

STEP 5: Hidden Talent Detection [HTDA Agent]
  Input:  500M composite scores from Intelligence Scoring ML Agent (ISMA)
          + behavioral patterns + learning trajectories
  Action: Detect latent talents invisible to traditional assessment
          → Cross-domain polymaths, rapid improvers, late bloomers
          → Diversity & inclusion indicators (underrepresented high-potential)
          → Anomaly-based talent discovery
  Output: Hidden talent map + growth pathway recommendations
          + diversity signal (feeds recruiter DE&I tools)

STEP 6: Career Prediction [CPA Agent — 75% ML / 25% LLM]
  Input:  Skill snapshot + education profile + behavioral signals +
          job market data + peer benchmark vectors
  ML Models:
    career_classifier_v1:     XGBoost → top-N career paths with probability
    skill_gap_ranker_v1:      LambdaMART → ordered skill gaps by urgency
    market_demand_forecaster: Prophet + LSTM → 6/12/24 month demand index
  Output: career_prediction_bundle (ranked paths + probabilities)
          skill_gap_report (ordered gaps → closure actions)
          market_alignment_score
          confidence_score (minimum 0.75 for candidate-facing output)
  LLM Layer: Generates 2–3 sentence plain-language explanation only
             (NEVER modifies scores or makes decisions autonomously)
  Downstream: Feeds RECOMMENDATION_ENGINE, JOB_MATCH_AGENT,
              RANK_UPDATE_AGENT, NOTIFICATION_MANAGER_AGENT
```

---

### DOMAIN C — PROFILE, PORTFOLIO & CREDENTIAL BUILDING

**Trigger:** Candidate updates profile, completes project, imports social data, or requests resume generation

**Orchestration Flow:**

```
WORKFLOW: EVIDENCE-BACKED PROFESSIONAL IDENTITY (CAND-I03)

STEP 1: Dynamic Profile Management [Feature 02]
  Profile Zones (5):
    Zone 1: Personal Identity (photo, headline, goal — candidate-declared + moderated)
    Zone 2: Dojo Skill Record (system-verified, auto-updated on every belt change)
    Zone 3: Career & Education History (verified via institute handshake)
    Zone 4: Portfolio & Work Showcase (cryptographic proof links)
    Zone 5: Competitive Achievements (championship medals, national ranks)
  Visibility Controls: Granular per-section per-audience (public/recruiter/private)
  AI Auto-Update: Belt level, national rank, EIS score — never require manual input

STEP 2: Social Media Integration [Feature 17]
  Connections:
    LinkedIn:  Pull verified professional credentials, work history, certifications
    GitHub:    Import repositories, commit history, language proficiency signals
    Portfolio: External portfolio links with metadata extraction
  Action: SSEM processes imported data → adds to skill inventory as inferred signals
  Benefit: Eliminates manual re-entry; recruiter sees unified, cross-verified profile
  Privacy Gate: Candidate controls which imports are visible to whom (Settings)

STEP 3: Resume Generation System [Feature 07]
  Architecture: Next.js SSR (SEO-indexed) + Flutter (mobile)
  Key Components:
    - EUID Header (QR code + verification badge + EIS score + national rank)
    - Intelligence Profile (8-type scores with percentile context)
    - Dojo Belt Showcase (current belt + progression history)
    - Championship Medals (gold/silver/bronze + level + category)
    - Skill Inventory (SSEM-verified, belt-backed, taxonomy-mapped)
    - AI Career Probability (top 3 paths + percentages)
    - Project Portfolio (SHA-256 signed project links)
  Output: Recruiter-verifiable in 10 seconds via EUID QR scan
          Candidate-declared sections clearly labelled as unverified

STEP 4: Project & Work Portfolio [Feature 08]
  Lifecycle: ProjectOpen → ProposalSubmitted → ProposalAccepted →
             InProgress → Completed → Paid
  Verification: SHA-256 signed on milestone completion
  SEO:          canonical URL: ecoskiller.com/candidate/[name-id]
                auto-indexed after milestone completion (Next.js SSR)
  Governance:   Laws R56, R58, R64, R71, R72, R73, R76, R80, R91
  Output: Public portfolio page + Skill Passport update + Badge award

STEP 5: Candidate Summary Generation [CSG Agent — CAT-12]
  Trigger: Recruiter requests summary OR placement pipeline shortlists candidate
  Input:   Full candidate profile + Dojo record + portfolio + championship history
  Action:  Generate structured intelligence card (one-page, evidence-based)
           → Career probability highlights
           → Skill belt achievements
           → National rank context
           → Hidden talent signals (from HTDA)
           → Recommended role fit score
  Output:  Candidate intelligence card (PDF + JSON) for recruiter consumption
  MCP Tool: candidate_summary_generator (CAT-12)
```

---

### DOMAIN D — LEARNING, GROWTH & DEVELOPMENT

**Trigger:** Candidate enrolls in training, attends workshop, earns XP, completes course, or receives mentor session

**Orchestration Flow:**

```
WORKFLOW: VERIFIED SKILL DEVELOPMENT ECOSYSTEM (CAND-I04)

STEP 1: Learning Management System [Feature 14]
  Delivery Modes: Self-paced courses, Live classes, Cohort programs,
                  Skill-based assessments
  Design Principles:
    - Outcome-first: every feature traces to a measurable career outcome
    - Offline-capable: downloadable content for 2G/no-connectivity environments
    - Compliance-ready: NAAC, NIRF, AICTE, corporate L&D reporting
    - AI-assisted: adaptive assessments, intelligent recommendations
  Certification: All completions → Skill Passport entry (cryptographically signed)

STEP 2: Training Program [Feature 13]
  Architecture Layers:
    L1: Content Anchoring (live industry demand data — not static)
    L2: Skill Proof Generation (verifiable, cryptographically signed records)
    L3: Behavioral Performance Analytics (retained skill tracking)
  10 Core Training Modules: Technical Skills, Aptitude, Communication,
    Domain Knowledge, Leadership, Interview Prep, Project Management,
    Analytical Thinking, Digital Literacy, Industry Certification
  Output: Permanent Lifetime Skill Passport entries per module

STEP 3: Mentorship Module [Feature 15]
  Mentor Matching: Trust-gated MMA agent (from Intelligence Discovery F10)
                   → matches by domain expertise, teaching style, availability
  Session Types: 1-on-1 coaching, Group learning, Long-term career tracking
  Accountability: Structured check-ins, goal tracking, progress measurement
  Belt Authority: Mentors score Dojo matches (calibration-governed, anti-bias)

STEP 4: Workshops [Feature 16 — 12 Types]
  Workshop Types:
    WS-01: Resume & Profile Masterclass
    WS-02: Interview Skills & Mock GD
    WS-03: Career Planning & Goal Setting
    WS-04: Aptitude & Logical Reasoning
    WS-05: Domain Technical Skills (role-specific)
    WS-06: Soft Skills & Communication
    WS-07: Entrepreneurship & Shark Tank Prep
    WS-08: Financial Literacy for First Earners
    WS-09: LinkedIn & Social Media Branding
    WS-10: Project Management & Execution
    WS-11: AI & Tech for Non-Technical Roles
    WS-12: Mental Health & Career Resilience
  Format: Live (Jitsi self-hosted) + Recorded (asynchronous access)
  Output: Workshop completion badge + skill record + mentor feedback score

STEP 5: Gamification Engine [Feature 19 — 16 Mechanics]
  Core Mechanics:
    M01: Points System (XP for every platform action)
    M02: Belt Progression (Dojo belt milestones)
    M03: Daily Streaks (habit-building, streak XP bonuses)
    M04: National Leaderboard (ELO-ranked, real-time)
    M05: Mission System (weekly/monthly challenges)
    M06: Achievement Badges (50+ types)
    M07: Level-Up Celebrations (animated milestones)
    M08: XP Economy (redeemable for features/benefits)
    M09: Peer Challenges (1v1 skill battles)
    M10: Team Competitions (batch vs. batch)
    M11: Streak Protection (1 freeze/week, PRO tier)
    M12: Referral Rewards (XP bonus for invites)
    M13: Hidden Missions (discovery-based secret challenges)
    M14: Comeback Bonuses (re-engagement after absence)
    M15: Mentor Recognition (XP for mentoring others)
    M16: Championship Qualifier Points (feeds belt promotion)
  Design Goal: 40–60% increase in daily active usage,
               35–50% improvement in course completion rates

STEP 6: Promotion Probability [APPM Agent]
  Trigger: Candidate approaches belt promotion threshold
  Input:   Assessment scores + mentor calibration data +
           championship performance + behavioral consistency
  Action:  Sealed probability score → gates mentor board review
           → triggers enterprise reporting pipeline
           → feeds Talent Operating System skill vector model
  Gate:    Auto-promotion FORBIDDEN — human mentor board review REQUIRED
  Output:  Promotion probability score + mentor review trigger +
           candidate development feedback
  Governance: Anti-favoritism rules, calibration drift detection,
              appeals system active

STEP 7: Awards & Recognition [Feature 20]
  Recognition Layers:
    L1: Activity Awards (first login, first streak, first belt)
    L2: Consistency Awards (7-day, 30-day, 100-day streaks)
    L3: Skill Excellence (belt-based trophies, national rank badges)
    L4: Championship Medals (Gold/Silver/Bronze — school to world level)
    L5: Community Awards (most helpful, best mentor, peer vote)
    L6: Scholarship Recognition (₹∞ prize pool via championships)
    L7: Hall of Fame (permanent national champion records)
  Philosophy: 100% of candidates recognised for something — not just toppers
```

---

### DOMAIN E — CAREER PLACEMENT & JOB MARKET

**Trigger:** Candidate searches for jobs, applies, enters GD/Interview, or receives offer

**Orchestration Flow:**

```
WORKFLOW: END-TO-END PLACEMENT INTELLIGENCE (CAND-I05 + CAND-I06)

STEP 1: Job Portal Discovery [Feature 09]
  Verification Tiers Unlocked at Each Level:
    Basic (Mobile + Email):       Browse jobs, apply SME listings
    Institute Verified:           Campus drives, recruiter search visible
    Document Verified:            Salary negotiation tools, offer tracking
    Fully KYC'd:                  All features, highest recruiter priority
  Intelligence Engines (8):
    IE-1: AI Skill Match Engine (profile vs. JD vector similarity)
    IE-2: Cultural Fit Predictor (company values alignment)
    IE-3: Salary Expectation Calibrator (market vs. candidate band)
    IE-4: Location Preference Optimizer (commute, relocation signals)
    IE-5: Career Growth Trajectory Engine (5-year projected growth)
    IE-6: Competition Index Calculator (applicant pool difficulty)
    IE-7: Application Success Predictor (probability score per role)
    IE-8: Bias-Free Match Engine (zero recruiter bias permitted)

STEP 2: Job Application System [Feature 10]
  Target: Fresh graduates from Tier-2 and Tier-3 institutes
  Key Features:
    - Smart-apply: one-click with EUID + skill match (no manual form fill)
    - SME safeguards: offer authenticity verification, employer credibility score
    - Placement drives: campus batch processing (200+ candidates per drive)
    - Application tracker: stage-by-stage pipeline visibility
    - Rejection analysis: AI feedback on why application was filtered out
  Application Filtering Logic:
    → Profile Completeness Score < 75: suppressed from recruiter search
    → Skill match score < 60%: not surfaced for that role
    → No skill assessments: cannot be ranked against assessed candidates
    → Trust flag RED (FPDM): application blocked

STEP 3: Placement Probability Prediction [PPM Agent — CAT-12]
  Input:   Student profile + target role + company profile + batch data
  Models:  Supervised Gradient Boosting + Neural Embedding Hybrid
  Predictions:
    - Individual placement probability (30/60/90/180/365 day windows)
    - Batch-level placement forecast (institute-level)
    - Recruiter match probability (bidirectional: candidate → company)
    - Time-to-placement prediction
    - Salary band prediction (linked to ASPE)
    - Offer acceptance probability
  Explainability: SHAP values per prediction (mandatory)
  Human Override: REQUIRED for all final hiring decisions
  Output:  placement_probability_score + ranked_improvement_actions

STEP 4: Career Probability + Income Prediction [CPM + IPM — CAT-11]
  CPM Multi-Dimensional Inputs:
    - Intelligence Vector (Gardner 8-type, weight: 0.25)
    - Skill Vector (Dojo + SSEM verified, weight: 0.30)
    - Championship Performance (win_rate, ranking, consistency, weight: 0.15)
    - Academic Performance (grades, learning_speed, weight: 0.10)
    - Project Portfolio (complexity, quality, innovation, weight: 0.10)
    - Behavioral Traits (work_ethic, reliability, communication, weight: 0.05)
  IPM Inputs: Career Probability Vector (0.30) + Intelligence Profile (0.15) +
              Skill Valuation (0.25) + Championship Performance (0.15)
  Output:
    CPM: Ranked career paths with probability + progression roadmap
    IPM: Lifetime earning trajectory (Age 18–65) with confidence bands
         Salary band prediction per career path + market alignment
  MCP Tools: career_probability_model + income_prediction_model (CAT-11)

STEP 5: Interview System [Feature 11]

  5A — Automated Voice Group Discussion (GD)
    Architecture: Deterministic state machine — zero human/AI judgment
    Layers:
      - Candidate Browser: renders topic, timer, audio UI (zero decision power)
      - Portal Backend: all rules, scoring, state machine (100% decision power)
      - Jitsi Server (self-hosted): voice transport ONLY — no data retention
    Protocol: Permission-based turn-taking, timed contributions,
               protocol compliance scoring, participation metadata analytics
    Privacy Guarantee: Zero audio storage — analytics from metadata only
    Output: GD participation score (reproducible: same log → same score always)

  5B — Technical Interview
    Engine: AI-driven question generation + real-time evaluation
    Anti-fraud: Proctoring layer + anomaly detection
    Output: Technical competency score + skill gap report

  5C — SME Hiring Layer
    Safeguards: Offer locking, risk prediction, SME verification
    Post-joining Tracker: 30/60/90 day accountability loop
    Output: Hiring risk score + post-placement monitoring

  5D — Interview Semantic Analysis [Antigravity_Interview_Semantic_Analyzer — CAT-12]
    Trigger: Post-interview (async processing)
    Input:   Interview transcript or structured responses
    Action:  Semantic scoring, red-flag detection, culture-fit analysis
    MCP Tool: interview_semantic_analyzer (CAT-12)

STEP 6: Career Guidance [Feature 12]
  Guide Covers:
    - Platform registration and profile optimisation
    - Skill assessment and AI-driven matching
    - Resume building and optimisation tips
    - GD preparation (rules, scoring, strategy)
    - Interview rounds: Technical, HR, Aptitude, SME-specific
    - Offer letter understanding: CTC, variable pay, compliance
    - SME vs. Corporate hiring comparison
    - Post-placement career tracking and growth roadmap

STEP 7: Competition [Feature 22 — 3 Levels]
  Level 1 — Batch Competition:  vs. classmates → same placement drive
  Level 2 — Institute Competition: Institute rank vs. other institutes
  Level 3 — Platform Competition: AI match score vs. all candidates nationally
  Championship Integration:
    School → District → State → National → Continental → World
  Outputs: Championship medals + XP + belt promotion eligibility +
           scholarship prize pool + Hall of Fame (national champions)

STEP 8: Placement Intervention [PIA Agent]
  Trigger:  Candidate stall detected (no offer after 14+ days in pipeline,
            or dropout_risk_model from CAT-11 fires)
  Input:    candidate_id + hiring_stage + stall_reason + behavioral signals
  Actions:  Timeline acceleration recommendations
            Targeted messaging suggestions
            Skill gap closure sprint (emergency training path)
            Offer strategy adjustment
  CAT-11:   learning_behavior_drift + dropout_risk_model feed this agent
  Output:   Intervention plan + urgency score + expected placement recovery time
```

---

### DOMAIN F — ANALYTICS, ENGAGEMENT & MONETISATION

**Trigger:** Candidate views dashboard, sends message, manages subscription, or pitches Shark Tank

**Orchestration Flow:**

```
WORKFLOW: ENGAGEMENT, TRUST & MONETISATION (CAND-I07 + CAND-I08)

STEP 1: Candidate Analytics [Feature 21]
  GD Analytics Dashboard — What is Measured:
    Phase 1 (Pre-GD):      Preparation time, topic review engagement
    Phase 2 (GD Active):   Turn count, compliance score, protocol adherence
    Phase 3 (GD Scoring):  Participation score, comparison vs. cohort
    Phase 4 (Post-GD):     Historical trends, improvement velocity
  Analytics Principles:
    - Zero audio storage (privacy-by-design)
    - Reproducible: identical event log → identical score always
    - Deterministic: rules-based, no AI judgment in scoring pipeline
    - All scores explainable (full event log available to candidate)

STEP 2: Messaging & Communication [Feature 18]
  Channels:  Candidate ↔ Recruiter, Candidate ↔ Mentor, Candidate ↔ Trainer
  Stack:     Flutter + Next.js + PostgreSQL + Redis + Kafka +
             WebSocket (WSS) + LiveKit (WebRTC)
  Security:  TLS 1.3 + AES-256-GCM + WSS Mandatory +
             End-to-End Encryption (sensitive threads)
  Rate Controls: RBAC-governed send limits + trust-score gating +
                 burst detection + spam filters
  Notifications: In-App + Push (FCM) + Email (Postfix) + SMS (Jasmin Gateway)
  Governance:    Laws R52, R53, R54, R55, R56, R58, R64, R65, R72, R78, R91, R92

STEP 3: Subscription Management [Feature 23]
  Tier Access Control:
    FREE:  Intelligence Discovery, basic Dojo, browse jobs, gamification
    BASIC: +Job applications, GD practice, resume builder, basic analytics
    PRO:   +Full interview system, mentorship, workshops, advanced analytics,
            streak protection, Shark Tank access
    ELITE: +Premium mentorship, championship fast-track, income prediction,
            dedicated placement support, custom career roadmap

  Career Stage State Machine: Student → Job Seeker → Professional → Mentor
  Governed by: Role-based access control (RBAC) + entitlement checks +
               feature-flag management

STEP 4: Shark Tank Platform [Feature 24]
  Target:    Innovative candidates with entrepreneurial ideas
  Purpose:   Direct investor pitch, startup funding discovery,
             entrepreneurial career path activation
  Platform Stats: ₹40 Crore pre-money valuation, ₹2 Crore seed raise target
  Integration: Links to Project Portfolio (evidence base), Career Probability
               (entrepreneurship path), Income Prediction (founder trajectory)

STEP 5: Reputation Score Engine [RSE Agent]
  Entity:     Candidate reputation across 7 dimensions
  Gates Controlled by RSE:
    - Match eligibility and matchmaking band
    - Belt and certification access
    - Marketplace visibility and hiring priority
    - Mentor authority scope (Mentor state transition)
    - Tournament entry eligibility
    - Recruiter search ranking priority
  Score Ledger: Append-only, immutable per-candidate reputation record
  Signals:      All 9 core engines + FPDM + Billing + Integration Engine
                + all behavioral event streams

STEP 6: Project Execution Tracker [Feature 25]
  Architecture: Event-Driven Microservices on Kubernetes (GCP + AWS k3s)
  Scale:        310+ features, 20 modules, 36 sprints (2-week each)
  Primary Stack: Node.js + PostgreSQL 15 + Redis 7 + Kafka 3.7 +
                 Flutter + React/Next.js
  Deployment:   GCP asia-south1 + AWS ap-south-1 (Active-Active)
  Phases:       MVP (Sprints 1–8) → Growth (9–18) → Scale (19–28) → Global (29–36)
```

---

## 🔐 SECTION 4: CONTRACT GATES & GOVERNANCE RULES

### 4.1 Immutable Candidate Module Orchestration Gates

**Gate CG-C01 — EUID-First Rule**
```
No candidate feature, agent, or data operation may execute
unless a valid EUID exists for the requesting candidate.
Account in provisional state → read-only access only.
EUID revoked → all feature access suspended immediately.
Violation: STOP all downstream processing → escalate to Trust Board.
```

**Gate CG-C02 — Fake Profile Firewall (PHANTOM SHIELD)**
```
FPDM background scan MUST run within 60 seconds of account creation.
RED-flagged profiles: recruiter visibility BLOCKED, placement pipeline BLOCKED.
YELLOW-flagged profiles: trust advisory shown, job application limited.
No RED or YELLOW profile may appear in any recruiter shortlist.
Violation: STOP pipeline → write to phantom_audit_log → escalate.
```

**Gate CG-C03 — Certificate Authenticity Gate**
```
CAC must validate ALL certificate or credential uploads before
they are displayed on the public profile or fed into the skill record.
FRAUDULENT classification: credential suppressed + flag written.
SUSPICIOUS: candidate notified, manual review triggered.
No unverified certificate may be displayed as 'verified' on profile.
```

**Gate CG-C04 — Skill Evidence Mandatory**
```
No skill may appear as 'verified' on the candidate's profile or
resume without at least one of:
  ✓ Dojo belt assessment (proctored + mentor-scored)
  ✓ SSEM extracted real-work signal (from integrated tool)
  ✓ Certificate authenticity check PASSED (CAC)
  ✓ Championship performance at qualifying level
Self-declared skills are permissible but MUST be labelled 'self-declared'.
No verified label without evidence.
```

**Gate CG-C05 — Auto-Promotion Forbidden**
```
No belt level upgrade may be applied to a candidate's profile
without:
  ✓ Promotion Probability score ≥ 0.80 (APPM)
  ✓ Mentor Board review completion
  ✓ Calibration drift check passed
  ✓ Anti-manipulation audit passed
Auto-promotion by any system, agent, or admin is STRICTLY FORBIDDEN.
Violation: Revert belt, audit trail generated, mentor board notified.
```

**Gate CG-C06 — Prediction Confidence Gate**
```
Career Prediction output below 0.75 confidence: SUPPRESSED from candidate view.
Placement Probability output below required confidence: replaced with
"Insufficient Data — complete more assessments" advisory.
Income Prediction: never shown without Career Probability Model input.
No candidate-facing prediction is emitted below confidence threshold.
```

**Gate CG-C07 — Audio Privacy Absolute**
```
No audio from any GD session or interview may be stored, retained,
processed by AI, or transmitted off the self-hosted Jitsi server.
Analytics are derived from participation METADATA ONLY.
This rule is ABSOLUTE and cannot be overridden by any admin or corporate user.
Violation: Immediate audit, data deletion, regulatory notification.
```

**Gate CG-C08 — Subscription Feature Enforcement**
```
Every ELITE/PRO/BASIC feature must verify subscription tier before dispatch.
Expired subscription: feature gracefully downgraded to FREE access.
No feature silently breaks — degraded mode with upgrade nudge shown.
Payment failure: 7-day grace period, then tier downgrade.
```

**Gate CG-C09 — Audit Trail Mandatory**
```
Every CAND-OA orchestration cycle generates an immutable log entry:
  - timestamp, request_id, candidate_id, lifecycle_state
  - intent_category, agents_activated, gates_evaluated
  - trust_check_result, fake_profile_flag, certificate_check_result
  - output_type, delivery_status, agent_confidence_scores
Log is append-only, stored in TimescaleDB — cannot be deleted or modified.
```

**Gate CG-C10 — India-First Connectivity Rule**
```
ALL candidate-facing features must function on 2G connectivity.
Offline mode MUST support: Dojo practice, LMS content access,
                            profile viewing, GD preparation content.
No feature may require stable broadband as a prerequisite.
UI must render in < 3 seconds on low-end Android with 2G.
Violation: Feature blocked from release until 2G compliance confirmed.
```

### 4.2 Failure Mode Protocol

```
CAND-OA Failure Hierarchy:

Level 1 — Sub-Agent Timeout (> 30 seconds)
  Action: Retry once → DEGRADED mode → continue without agent output
  Exception: Trust gates (CG-C01, CG-C02) → pipeline STOPS if these fail

Level 2 — Data Contract Violation
  Action: STOP → report CONTRACT_VIOLATION → log → escalate
  No partial output emitted to candidate

Level 3 — Trust Gate Failure (FPDM / CAC / RSE)
  Action: HALT processing → alert Trust Board → candidate notified
  Resume only after trust gate resolves

Level 4 — Prediction Confidence Below Threshold
  Action: Suppress prediction → show "More data needed" advisory
  Do NOT emit low-confidence prediction to candidate

Level 5 — Audio/Privacy Violation Detected
  Action: IMMEDIATE halt → delete data → regulatory notification → audit
  No exceptions permitted under Gate CG-C07
```

---

## 👤 SECTION 5: CANDIDATE PERSONAS & THEIR ORCHESTRATION PATHS

### 5.1 Persona: Rural School Student (Age 14–18)
**Location:** Tier-3 district | **Device:** Low-end Android | **Connectivity:** 2G
```
Primary Intent: CAND-I02 (Skill Discovery) + CAND-I04 (Learning)
CAND-OA Route:  Identity (EUID) → Intelligence Discovery → Dojo (beginner)
                → Gamification (daily habit) → Awards (participation)
                → LMS (offline-capable) → Career Prediction (student mode)
Key Agents:     HIDDEN_TALENT_DETECTION + CAREER_PREDICTION
Language:       Hindi / regional vernacular (8-language engine)
Subscription:   FREE tier
Parent Link:    Parent-Linked ID enabled → parent receives progress reports
                via parent_llm_career_guidance (CAT-11)
```

### 5.2 Persona: Engineering Graduate — Tier-2 College (Age 21–23)
**Location:** Tier-2 city | **Target:** IT company placement
```
Primary Intent: CAND-I05 (Job Search) + CAND-I06 (Interview)
CAND-OA Route:  Profile complete → Job Portal → Placement Probability
                → Job Application → Voice GD → Technical Interview
                → Career Guidance → Offer evaluation
Key Agents:     PPM + CPM + IPM + INTERVIEW_SEMANTIC_ANALYZER +
                CANDIDATE_SUMMARY_GENERATOR
Subscription:   PRO tier
Dojo Belt:      Blue/Purple target for IT roles
Competition:    Platform-level (national rank visibility)
```

### 5.3 Persona: First-Generation Professional (Any Stream, Age 18–24)
**Background:** First person in family to enter formal employment
```
Primary Intent: CAND-I01 (Onboarding) + CAND-I04 (Development) + CAND-I05
CAND-OA Route:  EUID creation → 8-language onboarding → Intelligence Discovery
                (strength profile) → Career roadmap → Training → Mentorship
                → Workshop (Financial Literacy + Interview Skills)
                → Job application (SME-first for faster placement)
Key Agents:     HTDA (latent potential) + PIA (ensure no stall) + IPM
Key Features:   Mentorship (first-gen support), Scholarship recognition,
                Career Guidance handbook (offer letter education)
Subscription:   BASIC tier (subsidised rural pricing)
```

### 5.4 Persona: Career Returner (Woman returning after gap, Age 27–35)
**Gap:** 2–5 years career break | **Goal:** Re-enter workforce
```
Lifecycle State: STATE 7 (Career Returner)
CAND-OA Route:  Return-Ready profile mode → Skill re-assessment (Dojo)
                → Gap-fill LMS path → Mentorship (return-specialist mentor)
                → Resume (return narrative generation) → Job Portal
                → Workshop WS-12 (Career Resilience) + WS-09 (LinkedIn)
Key Agents:     HIDDEN_TALENT_DETECTION (dormant talent resurfacing)
                + CAREER_PREDICTION (re-entry path)
                + SSEM (re-assess current skill inventory from any work signals)
Dropout Guard:  dropout_risk_model (CAT-11) monitored continuously
```

### 5.5 Persona: Working Professional Seeking Growth (Age 25–35)
**Status:** Employed | **Goal:** Belt promotion, salary increase, mentor others
```
Lifecycle State: STATE 5 (Professional)
CAND-OA Route:  Dojo (advanced belt) → APPM (promotion gate)
                → Project Portfolio (real work evidence) → Shark Tank
                → Income Prediction (negotiation intelligence)
                → Mentorship (provide mentorship to juniors)
Key Agents:     APPM + IPM + RSE (mentor trust tier)
Subscription:   ELITE tier
Output:         Belt promotion + salary negotiation data +
                Mentor state transition eligibility
```

---

## 🏗️ SECTION 6: TECHNICAL ARCHITECTURE

### 6.1 Technology Stack

```
INHERITED ECOSKILLER STACK (LOCKED):
  Backend Primary:      Node.js (Candidate-facing API services)
  Backend Secondary:    Python 3.11 + FastAPI (ML/AI agent services)
  Database:             PostgreSQL 15 (RLS multi-tenancy)
  Cache:                Redis 7 (session, feature flags, leaderboard)
  Event Broker:         Apache Kafka 3.7 (ecoskiller.candidate.* topics)
  Search:               OpenSearch 2.x (job search, talent search)
  Auth:                 OAuth2 + OIDC + JWT (Keycloak realms — per tenant)
  Container:            Docker + Kubernetes (k3s self-managed)
  Deployment:           GCP asia-south1 + AWS ap-south-1 (Active-Active)
  IaC:                  OpenTofu
  Monitoring:           Prometheus + Grafana + Jaeger + Loki
  Frontend Mobile:      Flutter (primary — offline-capable, 2G-first)
  Frontend Web:         Next.js (SSR for SEO + recruiter-facing pages)
  Graph DB:             Neo4j 5.x (skill relationship + team compatibility)
  Time-Series DB:       TimescaleDB (audit logs, analytics, score history)
  Vector DB:            Qdrant (talent search) + Weaviate (backup)
  ML Engine:            scikit-learn + PyTorch 2.x + XGBoost + Prophet + LSTM
  Voice/WebRTC:         Jitsi (self-hosted) + LiveKit (candidate comms)

CAND-OA SPECIFIC EXTENSIONS (LOCKED):
  Orchestration Engine: Python FastAPI async + Redis Streams
  DAG Executor:         Custom async DAG (asyncio + aiohttp)
  Agent Registry:       PostgreSQL (agent metadata + health status)
  Audit Log Store:      TimescaleDB (append-only, cand_orchestration_log)
  MCP Client Bridge:    Java 11 (CAT-11, CAT-12) via stdio JSON-RPC 2.0
  Skill Passport Store: PostgreSQL + cryptographic signing (SHA-256)
  EUID Registry:        PostgreSQL + Redis cache (QR generation + TTL)
  Privacy Vault:        Encrypted store for Aadhar hash + KYC data
  Offline Sync Engine:  Background sync for Flutter offline mode
  Model Registry:       MinIO (immutable model versions + rollback)
```

### 6.2 Kafka Topic Architecture

```
CAND-OA Managed Kafka Topics:

ecoskiller.candidate.intent              — incoming candidate requests
ecoskiller.candidate.lifecycle           — state transition events
ecoskiller.candidate.orchestration       — DAG execution events
ecoskiller.candidate.agent.results       — sub-agent response aggregation
ecoskiller.candidate.trust               — FPDM / CAC / RSE events
ecoskiller.candidate.skill.events        — Dojo / SSEM / belt events
ecoskiller.candidate.gamification        — XP / badge / streak events
ecoskiller.candidate.placement           — job / GD / interview events
ecoskiller.candidate.audit               — immutable execution log feed
ecoskiller.candidate.notifications       — nudges / alerts / milestone events
ecoskiller.candidate.parent              — parent-linked updates (CAT-11)
```

### 6.3 API Contract

**Endpoint:** `POST /api/v1/candidate/orchestrate`

```json
REQUEST:
{
  "candidate_id": "EUID-MH-2026-00847",
  "session_token": "JWT",
  "intent": "CAND-I05",
  "lifecycle_state": "JOB_SEEKER",
  "subscription_tier": "PRO",
  "payload": {
    "action": "job_search",
    "role_preference": "Software Engineer",
    "location": "Pune, Maharashtra",
    "expected_salary_band": "4-6 LPA"
  },
  "options": {
    "include_placement_probability": true,
    "include_career_prediction": true,
    "include_income_forecast": true,
    "language": "hi"
  }
}

RESPONSE:
{
  "request_id": "CAND-OA-REQ-20260318-001",
  "intent": "CAND-I05",
  "status": "SUCCESS",
  "trust_gate": "PASSED (GREEN)",
  "lifecycle_state": "JOB_SEEKER",
  "agents_executed": [
    "FPDM", "PPM", "CPM", "IPM", "CANDIDATE_SUMMARY_GENERATOR"
  ],
  "placement_probability": 0.84,
  "top_career_paths": [...],
  "income_forecast_3yr": "5.2-7.8 LPA",
  "recommended_jobs": [...],
  "audit_trail_id": "AUDIT-CAND-20260318-001",
  "execution_time_ms": 1240,
  "language": "hi"
}
```

---

## 📅 SECTION 7: IMPLEMENTATION ROADMAP

### Phase 0 — Core Infrastructure (Sprint 1–2)
- [ ] CAND-OA orchestration engine deployed
- [ ] EUID registry operational + QR generation service live
- [ ] Agent registry seeded with all 12 specialist agents
- [ ] Kafka topic architecture provisioned (11 candidate topics)
- [ ] MCP client bridge (Java ↔ Python) CAT-11 + CAT-12 tested
- [ ] Audit log schema created (TimescaleDB, append-only)
- [ ] Contract gates CG-C01 to CG-C10 implemented + unit-tested

### Phase 1 — Identity & Trust (Sprint 3–4)
- [ ] EUID creation flow end-to-end (< 5 min on 2G)
- [ ] 8-language onboarding engine live
- [ ] PHANTOM SHIELD (FPDM) integrated + alert pipeline tested
- [ ] CAC certificate authenticity classifier operational
- [ ] RSE reputation gating enforced at all feature entry points
- [ ] 6-level progressive verification system implemented
- [ ] Offline sync engine functional (Flutter mobile)

### Phase 2 — Intelligence & Skill Engine (Sprint 5–8)
- [ ] Intelligence Discovery all 10 modules live
- [ ] Intelligence DNA Vector cryptographic signing operational
- [ ] SSEM real-work signal extraction from 100+ tool integrations
- [ ] Dojo skill system: 8 belt levels, proctored assessments, mentor scoring
- [ ] Skill Competency System: 16 families, ELO ranking, XP economy
- [ ] HTDA hidden talent detection pipeline operational
- [ ] Career Prediction Agent (CPA): XGBoost + Prophet + LSTM models trained

### Phase 3 — Profile, Portfolio & Credentials (Sprint 9–12)
- [ ] Dynamic profile management: 5 zones, auto-update hooks
- [ ] Social media integration: LinkedIn + GitHub import + SSEM processing
- [ ] Resume generation: EUID QR + verified sections + Next.js SSR
- [ ] Project & work portfolio: SHA-256 signing + SEO indexing
- [ ] Candidate Summary Generator (CSG): CAT-12 integration tested

### Phase 4 — Learning & Development Ecosystem (Sprint 13–18)
- [ ] LMS fully operational (self-paced + live + cohort)
- [ ] Training program: 10 modules with skill passport integration
- [ ] Mentorship module: Trust-gated MMA matching live
- [ ] Workshop platform: 12 workshop types (live + recorded)
- [ ] Gamification engine: all 16 mechanics operational
- [ ] Awards & Recognition: 50+ badge types, Hall of Fame system
- [ ] Promotion Probability (APPM): mentor board workflow integrated

### Phase 5 — Career Placement Engine (Sprint 19–24)
- [ ] Job Portal: 8 intelligence engines live + bias-free matching
- [ ] Job Application System: smart-apply + SME safeguards + batch drives
- [ ] Placement Probability (PPM): CAT-12 integration + SHAP explanations
- [ ] Career Probability (CPM) + Income Prediction (IPM): CAT-11 live
- [ ] Voice GD System: Jitsi self-hosted + state machine + metadata analytics
- [ ] Technical Interview: AI question engine + anti-fraud proctoring
- [ ] Interview Semantic Analyzer: CAT-12 integration tested
- [ ] Placement Intervention (PIA): stall detection + recovery plan engine

### Phase 6 — Analytics, Comms & Monetisation (Sprint 25–28)
- [ ] Candidate Analytics dashboard: GD + skill + career progress
- [ ] Messaging system: E2E encrypted + trust-gated + all notification channels
- [ ] Subscription tier management: 4-tier RBAC + entitlement system
- [ ] Shark Tank platform integration
- [ ] Reputation Score Engine (RSE): 7-dimension scoring + all gate bindings

### Phase 7 — India-Scale & Compliance (Sprint 29–32)
- [ ] 2G performance audit: all features verified on low-end Android
- [ ] DPDP (Digital Personal Data Protection) compliance audit completed
- [ ] GDPR compliance validation
- [ ] NAAC/NIRF/AICTE reporting standards for LMS
- [ ] Rural pricing and payment models (cash + UPI + carrier billing)
- [ ] Load testing: 10M concurrent candidates

### Phase 8 — Global & Growth (Sprint 33–36)
- [ ] Active-Active deployment: GCP asia-south1 + AWS ap-south-1
- [ ] International expansion: language pack additions
- [ ] Parent platform integration: parent-llm-career-guidance (CAT-11) live
- [ ] Championship world-level activation
- [ ] Full observability: Prometheus + Grafana + Jaeger + Loki dashboards

---

## 📈 SECTION 8: SUCCESS METRICS

### Operational KPIs (6 Months Post-Launch)

| Metric | Target |
|---|---|
| EUID Creation Completion Rate | > 80% (5-min flow on 2G) |
| Profile Completeness Score (avg) | > 72/100 |
| Dojo Assessment Completion Rate | > 65% of registered candidates |
| Daily Active Usage (DAU/MAU) | > 40% (gamification target) |
| GD Score Reproducibility | 100% (same log → same score) |
| Fake Profile Detection Accuracy | > 94% precision |
| Career Prediction Confidence (avg) | > 0.78 |
| Placement Rate (PRO tier, 90 days) | > 55% |
| 2G Performance Compliance | 100% of features < 3s load |
| Audit Log Completeness | 100% (every cycle logged) |

### Candidate Outcome KPIs (12 Months Post-Launch)

| Metric | Target |
|---|---|
| Time-to-First-Placement (Dojo direct) | < 30 days |
| Quality-of-Hire Score (employer-rated) | > 85/100 |
| Candidate Retention (12-month) | > 70% active after 12 months |
| Belt Promotion Rate (annual) | > 35% advancing one belt per year |
| National Championship Participation | > 500,000 candidates/year |
| Rural Candidate % of Active Base | > 60% (mission alignment) |
| First-Generation Professional % | > 40% of placed candidates |
| Parent Satisfaction (parent-linked IDs) | > 80% satisfaction score |

### Platform Financial KPIs

| Metric | Target |
|---|---|
| PRO Tier Conversion (from FREE) | > 15% within 90 days |
| ELITE Tier Conversion | > 5% of PRO users |
| Scholarship Prize Distribution | ₹∞ pool — merit-only awards |
| Shark Tank Funding Facilitated | > ₹5 Crore in Year 1 |

---

## 🔒 SECTION 9: SEALED GOVERNANCE BLOCK

```
================== BEGIN CAND-OA SEALED CONTEXT ==================

SYSTEM:           ECOSKILLER CANDIDATE MODULE ORCHESTRATOR AGENT
VERSION:          CAND-OA-v1.0.0-ANTIGRAVITY
LOCKED:           Mutation via version-bump only
STATUS:           Production-Ready · Sealed · Governed · India-First

CANDIDATE FEATURE MODULES (25 TOTAL):
  Domain A (Identity & Trust):       F01-Identity · F03-Settings
  Domain B (Intelligence & Skill):   F06-Intelligence · F04-Dojo · F05-Skill
  Domain C (Profile & Portfolio):    F02-Profile · F07-Resume · F08-Portfolio · F17-Social
  Domain D (Learning & Growth):      F13-Training · F14-LMS · F15-Mentorship ·
                                     F16-Workshops · F19-Gamification · F20-Awards
  Domain E (Career & Placement):     F09-JobPortal · F10-JobApp · F11-Interview ·
                                     F12-CareerGuidance · F22-Competition
  Domain F (Analytics & Monetise):   F18-Messaging · F21-Analytics · F23-Subscription ·
                                     F24-SharkTank · F25-ProjectExecution

SPECIALIST AI/ML AGENTS (12 TOTAL):
  Trust Agents:       FPDM (PHANTOM SHIELD) · CAC · RSE
  Intelligence:       SSEM · HTDA · CPA (Career Prediction)
  Career/Placement:   CPM · PPM · IPM · PIA
  Profile/Comms:      CSG (Candidate Summary) · APPM (Promotion Probability)

MCP BINDING:
  Primary:    CAT-12 (mcp-12-institute-hr) — 7 relevant tools
  Secondary:  CAT-11 (mcp-11-championship-parent-ai) — 8 relevant tools

CONTRACT GATES LOCKED (10):
  CG-C01: EUID-First Rule (no feature without valid EUID)
  CG-C02: Fake Profile Firewall (FPDM must clear all candidates)
  CG-C03: Certificate Authenticity Gate (CAC validates all credentials)
  CG-C04: Skill Evidence Mandatory (no verified skill without proof)
  CG-C05: Auto-Promotion Forbidden (APPM + mentor board required)
  CG-C06: Prediction Confidence Gate (min thresholds enforced)
  CG-C07: Audio Privacy Absolute (zero audio storage — ever)
  CG-C08: Subscription Feature Enforcement (RBAC + entitlement check)
  CG-C09: Audit Trail Mandatory (every cycle logged immutably)
  CG-C10: India-First Connectivity Rule (2G compliance mandatory)

CANDIDATE LIFECYCLE STATES (8):
  0-UNREGISTERED · 1-STUDENT · 2-JOB_SEEKER · 3-PLACEMENT_PROCESS
  4-OFFER_STAGE · 5-PROFESSIONAL · 6-MENTOR · 7-CAREER_RETURNER

FAILURE MODE:     STOP → REPORT → NO PARTIAL ORCHESTRATION
AUDIT MODE:       APPEND-ONLY · IMMUTABLE · TAMPER-PROOF

PLATFORM MISSION:
  Serve 488 million rural Indian candidates
  Age coverage:   5 years → Career Retirement
  Language:       8 Indian languages + English
  Connectivity:   2G-first, offline-capable
  Economic goal:  Rural economic mobility through verified skills + placement

INTERPRETATION AUTHORITY: NONE
MUTATION POLICY:          Add-only via version bump
GOVERNANCE AUTHORITY:     EcoSkiller Governance Board only
AUDIT REQUIREMENT:        Quarterly orchestration review

================== END CAND-OA SEALED CONTEXT ==================
```

---

**Document Created:** March 18, 2026
**Architecture:** ANTIGRAVITY
**Platform:** EcoSkiller Unified Talent OS v12.0
**Module:** Candidate
**Status:** Sealed · Locked · Production-Ready
**Mission:** Rural India Economic Mobility — 488 Million Addressable Candidates

---

🔒 **END OF CANDIDATE MODULE ORCHESTRATOR AGENT SPECIFICATION** 🔒
