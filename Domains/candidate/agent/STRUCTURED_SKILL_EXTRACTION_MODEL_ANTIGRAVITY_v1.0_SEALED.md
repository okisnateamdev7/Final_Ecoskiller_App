# 🔒 STRUCTURED SKILL EXTRACTION MODEL (SSEM)
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY LAYER
### ECOSKILLER MASTER SYSTEM — SEALED PRODUCTION ARTIFACT v1.0

```
Artifact Class:         Production System Blueprint
Module Class:           Enterprise Optimization + Trust Infrastructure
Engine Name:            Structured Skill Extraction Model (SSEM)
Parent System:          Data Normalization Engine (DNE) → AntiGravity Layer
Mutation Policy:        Add-only via version bump
Execution Mode:         Deterministic
Stack Lock:             Enforced
Seal Status:            LOCKED & IMMUTABLE
Authority:              Human Declaration Only
Interpretation:         FORBIDDEN
Version:                1.0-SEALED
Generated:              2026-02-26
```

---

# ⚠️ SEAL DECLARATION

This document is a **LOCKED PRODUCTION ARTIFACT** for the **Structured Skill Extraction Model (SSEM)** operating within the **AntiGravity Enterprise Optimization + Trust Infrastructure** layer of **EcoSkiller**.

The SSEM is the intelligence core of the AntiGravity Data Normalization Engine. It defines, with zero ambiguity, **exactly how raw signals from 100+ integrated business tools are converted into verified, structured, trusted skill records** that power EcoSkiller's Dojo Belt Engine, AI Matcher, Talent Marketplace, and Trust Seal system.

No algorithm, taxonomy mapping, extraction rule, scoring formula, evidence classification, or downstream dispatch rule defined herein may be reinterpreted, simplified, skipped, or substituted without a formal **version bump** and **human-declared override**.

**Violation → STOP EXECUTION → REPORT → NO SYSTEM COMPLETION CLAIM PERMITTED**

---

# 🔒 SECTION SSEM-A — SYSTEM IDENTITY & MISSION

## Module Identity

```
Module Name:             Structured Skill Extraction Model (SSEM)
Namespace:               ecoskiller.enterprise.dne.ssem
Parent Module:           Data Normalization Engine (DNE)
Layer:                   AntiGravity — Enterprise Optimization + Trust Infrastructure
Version:                 v1.0-SEALED
Execution Class:         Deterministic AI-Assisted Rules Engine
Mutation Policy:         Add-only via version bump
```

## Core Mission

The Structured Skill Extraction Model answers one singular question for every piece of work data that enters EcoSkiller:

> **"What skill does this work event prove, at what strength, with what level of trust — and is it sufficient to advance a user's verified skill credentials?"**

The SSEM is the bridge between:

```
Raw Work Reality                         EcoSkiller Verified Credentials
────────────────────────────────────────────────────────────────────────
GitHub commit                →            Coding Skill Signal (direct)
Jira sprint closure          →            Project Management Signal (direct)
Salesforce deal closed       →            Sales Skill Signal (direct)
Slack message patterns       →            Communication Signal (inferred)
Figma design iteration       →            Design Skill Signal (direct)
HackerRank contest result    →            Algorithm Signal (direct)
Course completion (Moodle)   →            Domain Knowledge Signal (direct)
Mentor Dojo match score      →            Applied Skill Signal (corroborated)
```

Without the SSEM, EcoSkiller is just another profile platform.  
**With the SSEM, EcoSkiller is a real-work verified talent operating system.**

---

# 🔒 SECTION SSEM-B — ARCHITECTURAL POSITION

## SSEM Position in AntiGravity Stack

```
External Tool Events (100+ tools)
          ↓
[EIE — Ecoskiller Integration Engine]
          ↓
[DNE Stage 1–4: Ingest → Extract → Normalize → Enrich]
          ↓
  ┌────────────────────────────────────────────┐
  │  STRUCTURED SKILL EXTRACTION MODEL (SSEM) │  ← THIS MODULE
  │  DNE Stage 5                               │
  └────────────────────────────────────────────┘
          ↓
[UWDF Skill Signal Records]
          ↓
┌─────────────────────────────────────────────────────┐
│  Dojo Belt Engine   │  AI Matcher    │  Trust Engine │
│  Skill Registry     │  Marketplace   │  Reputation   │
│  Analytics Engine   │  Career AI     │  Parent View  │
└─────────────────────────────────────────────────────┘
```

## SSEM Is Non-Skippable

No UWDF record may carry a `skill_signal` array without passing through the SSEM.

Empty skill signals are valid UWDF output. Fabricated skill signals without SSEM processing are a **critical integrity violation**.

Bypass → **STOP EXECUTION + INTEGRITY ALERT**

---

# 🔒 SECTION SSEM-C — ECOSKILLER MASTER SKILL TAXONOMY (LOCKED)

## Taxonomy Purpose

The EcoSkiller Master Skill Taxonomy (EMST) is the **canonical, versioned list of all extractable skills** recognized by EcoSkiller. The SSEM may only emit skill signals for skills registered in the EMST.

All extraction rules, tool mappings, and AI enrichment targets reference EMST skill IDs.

## Taxonomy Governance Rules

```
Rule T-01: Skills may be added to EMST (add-only)
Rule T-02: Skill names and IDs are immutable once published
Rule T-03: Skills may be deprecated (flagged inactive) — never deleted
Rule T-04: New skill additions require Taxonomy Board approval
Rule T-05: Each skill must have: ID, name, category, domain, level_model, evidence_rules
Rule T-06: Taxonomy version must be recorded on every SSEM extraction output
```

## Master Skill Taxonomy — Domain Structure

```
EMST v1.0 — Domain Map
────────────────────────────────────────────────────────────
Domain ID    Domain Name                Sub-Domains
────────────────────────────────────────────────────────────
SKD-01       Software Engineering       Frontend, Backend, DevOps, Mobile, Data, Security, QA
SKD-02       Data & Analytics           Data Science, ML/AI, BI, Data Engineering, Statistics
SKD-03       Product & Design           Product Management, UX/UI, Design Systems, Research
SKD-04       Project Management         Agile, Scrum, PMP, Kanban, Risk, Stakeholder Mgmt
SKD-05       Sales & Business Dev       B2B Sales, B2C Sales, CRM Usage, Negotiation, Outreach
SKD-06       Marketing                  Digital Marketing, SEO, Content, Paid Ads, Analytics
SKD-07       Finance & Accounting       Bookkeeping, Reporting, Budgeting, Tax, Audit
SKD-08       Communication              Written, Verbal, Presentation, Cross-Cultural, Negotiation
SKD-09       Leadership & Management    Team Leadership, Conflict Resolution, Delegation, Coaching
SKD-10       Domain Knowledge           Industry-specific knowledge (Education, Healthcare, etc.)
SKD-11       Creativity & Design        Visual Design, Graphic Design, Motion, Illustration
SKD-12       Research & Analysis        Literature Review, Market Research, Data Gathering
SKD-13       Collaboration              Peer Collaboration, Cross-functional Work, Remote Work
SKD-14       Reliability & Execution    Deadline Adherence, Quality Consistency, Ownership
SKD-15       Problem Solving            Root Cause Analysis, Debugging, Optimization, Ideation
```

## Skill Record Schema (EMST Entry)

```json
{
  "skill_id": "uuid-v4",
  "skill_code": "SKD-01-FE-001",
  "skill_name": "React Frontend Development",
  "domain_id": "SKD-01",
  "sub_domain": "Frontend",
  "category": "technical",
  "level_model": "beginner | practitioner | advanced | expert",
  "evidence_rules": {
    "direct_evidence_sources": ["github", "gitlab", "bitbucket", "vercel", "netlify"],
    "inferred_evidence_sources": ["jira", "asana", "slack"],
    "corroboration_threshold": 2,
    "minimum_signal_strength_for_belt": 0.75
  },
  "taxonomy_version": "1.0",
  "active": true,
  "created_at": "ISO8601",
  "deprecated_at": null
}
```

---

# 🔒 SECTION SSEM-D — SKILL SIGNAL TYPE SYSTEM

## Signal Type Definitions (Immutable)

The SSEM recognizes exactly **four signal types**. These are frozen. No new signal type may be added without a version bump.

### Type 1 — DIRECT Signal

```
Definition:    The tool output is explicit, unambiguous proof of skill exercise.
               No inference required. The artifact IS the skill demonstration.

Examples:
  · Merged GitHub Pull Request with passing CI tests → direct coding signal
  · Closed Salesforce deal with revenue recorded → direct sales signal
  · Submitted and approved design in Figma with client review → direct design signal
  · HackerRank problem solved with correct output → direct algorithm signal
  · Course completed with passing quiz score → direct domain knowledge signal

Signal Strength Range: 0.65 – 0.98
Belt Eligible: YES (if strength ≥ 0.75 AND trust score ≥ 0.80)
Weight in Composite Score: 1.00x (full weight)
```

### Type 2 — INFERRED Signal

```
Definition:    The tool output implies skill through behavioral pattern or activity.
               AI semantic analysis required to connect behavior to skill.
               Single-source — not yet corroborated.

Examples:
  · Jira tickets consistently closed on time → inferred project management signal
  · Slack messages show structured problem framing → inferred communication signal
  · Google Sheets with complex formulas → inferred data analysis signal
  · Zoom meetings created and managed → inferred facilitation signal

Signal Strength Range: 0.30 – 0.69
Belt Eligible: NO (inferred alone is insufficient for belt)
Weight in Composite Score: 0.60x (discounted)
Upgrade Path: Corroborated by 2+ independent sources → becomes CORROBORATED
```

### Type 3 — CORROBORATED Signal

```
Definition:    Two or more independent tool sources confirm the same skill signal
               for the same user, in the same skill domain, within the same time window.
               Cross-source verification. Strongest automated signal type.

Examples:
  · GitHub (coding) + Jira (project delivery) + Peer Review (quality) → corroborated engineering signal
  · Salesforce (CRM usage) + HubSpot (pipeline) + Outreach (outreach activity) → corroborated sales signal
  · Figma (design artifacts) + Notion (design docs) + Peer feedback → corroborated design signal

Signal Strength Range: 0.75 – 0.98
Belt Eligible: YES (strength ≥ 0.75 AND trust score ≥ 0.80)
Weight in Composite Score: 1.30x (boosted — cross-verified)
Corroboration Window: 90 days (configurable per skill domain)
```

### Type 4 — DOJO-VALIDATED Signal

```
Definition:    Signal generated directly from a completed, scored Dojo match
               or assessment scenario within EcoSkiller's Dojo system.
               The highest-trust signal type. Human mentor + system verified.

Sources:
  · Dojo match score (scored by peer + mentor)
  · Pressure scenario completion
  · Mentor certification assessment
  · Tournament performance

Signal Strength Range: 0.70 – 1.00
Belt Eligible: YES (always eligible — subject to scoring thresholds)
Weight in Composite Score: 1.50x (highest trust multiplier)
Special Rule: DOJO-VALIDATED signals bypass AI inference — they are human-verified
```

---

# 🔒 SECTION SSEM-E — EXTRACTION RULE ENGINE

## Rule Engine Architecture

The SSEM Extraction Rule Engine (ERE) processes each enriched PNIR record from DNE Stage 4 through a deterministic, ordered rule evaluation chain.

```
Input:  Enriched PNIR record (from DNE Stage 4 AI Semantic Enrichment)
        + Tool identity
        + EMST Taxonomy v1.0
        + User's existing skill signal history

Process: Rule evaluation chain (ordered — all rules evaluated)

Output: Array of typed SkillSignal objects → UWDF skill_signals field
```

## Rule Evaluation Chain — Mandatory Execution Order

```
Step 1 → Tool Category Router
Step 2 → Domain Classifier
Step 3 → Evidence Classifier (DIRECT / INFERRED / CORROBORATED / DOJO)
Step 4 → Signal Strength Calculator
Step 5 → EMST Skill Mapper
Step 6 → Corroboration Checker
Step 7 → Belt Eligibility Evaluator
Step 8 → Composite Skill Profile Updater
```

No step may be skipped. Steps must execute in order.

---

## Step 1 — Tool Category Router

Routes the PNIR record to the correct extraction ruleset based on `source_tool_category`.

```
Tool Category Router Table
────────────────────────────────────────────────────────────────
source_tool_category      →  Extraction Ruleset Applied
────────────────────────────────────────────────────────────────
developer                 →  DEV-RULES-v1
project_management        →  PM-RULES-v1
crm                       →  CRM-RULES-v1
communication             →  COMMS-RULES-v1
design                    →  DESIGN-RULES-v1
hr_ats                    →  HR-RULES-v1
edtech_lms                →  EDU-RULES-v1
interview_assessment      →  INTERVIEW-RULES-v1
cloud_devops              →  CLOUD-RULES-v1
accounting_finance        →  FINANCE-RULES-v1
marketing                 →  MARKETING-RULES-v1
dojo_internal             →  DOJO-RULES-v1
────────────────────────────────────────────────────────────────
Unknown category          →  QUARANTINE (no extraction)
```

---

## Step 2 — Domain Classifier

Maps the routed record to one or more EMST domains (SKD-01 through SKD-15).

A single tool event may activate multiple domain classifiers.

```
Example Multi-Domain Classification:
  GitHub PR merged (React component for data dashboard)
  → SKD-01 (Software Engineering / Frontend) — primary
  → SKD-02 (Data & Analytics) — secondary (data visualization)
  → SKD-14 (Reliability) — tertiary (PR merged on time)
```

```sql
CREATE TABLE ssem_domain_classification (
  classification_id    UUID PRIMARY KEY,
  pnir_id              UUID NOT NULL,
  skill_domain_id      VARCHAR NOT NULL,
  classification_type  VARCHAR NOT NULL, -- primary | secondary | tertiary
  classifier_method    VARCHAR NOT NULL, -- rule_based | ai_classified
  confidence           DECIMAL(4,3) NOT NULL,
  created_at           TIMESTAMPTZ NOT NULL
);
```

---

## Step 3 — Evidence Classifier

Determines the signal type (DIRECT / INFERRED / CORROBORATED / DOJO-VALIDATED) for each classified domain.

### Evidence Classification Decision Tree

```
START
  ↓
Is source_tool_category = 'dojo_internal'?
  YES → Signal Type = DOJO-VALIDATED → EXIT
  NO  ↓

Is the event an explicit, unambiguous artifact of skill exercise?
(Reference: Direct Evidence Sources in EMST skill record)
  YES → Signal Type = DIRECT
        → check corroboration history (Step 6)
  NO  ↓

Does AI semantic enrichment produce skill tag with confidence ≥ 0.60?
  YES → Signal Type = INFERRED
        → check corroboration history (Step 6)
  NO  → Signal Type = NOISE → do NOT emit signal
```

### Direct Evidence Qualification Rules Per Tool Category

```
DEV-RULES-v1 — Direct Evidence Events
  ✅  PR merged with passing CI
  ✅  Release tag pushed
  ✅  Issue closed (assigned to user, with code diff)
  ✅  Code review approved (authored by user)
  ✅  HackerRank problem solved
  ❌  Commit pushed (alone — not direct, becomes inferred)
  ❌  Issue comment (inferred)

PM-RULES-v1 — Direct Evidence Events
  ✅  Sprint closed with ≥ 80% completion (managed by user)
  ✅  Milestone delivered on or before due date
  ✅  Epic completed
  ✅  Jira ticket moved to Done by assigned user
  ❌  Ticket created (not direct — inferred)
  ❌  Comment on ticket (inferred)

CRM-RULES-v1 — Direct Evidence Events
  ✅  Deal marked Closed-Won in Salesforce/HubSpot (owned by user)
  ✅  Pipeline stage advanced by user action
  ✅  Client meeting logged with outcome recorded
  ✅  Quote approved and sent
  ❌  Contact added (inferred)
  ❌  Email logged (inferred unless response tracked)

DESIGN-RULES-v1 — Direct Evidence Events
  ✅  Figma component published to team library
  ✅  Design approved in review (stakeholder sign-off recorded)
  ✅  Prototype shared and commented on by ≥ 2 stakeholders
  ✅  Design system contribution merged
  ❌  Frame created (inferred)
  ❌  Comment added (inferred)

EDU-RULES-v1 — Direct Evidence Events
  ✅  Course completed with quiz score ≥ passing threshold
  ✅  Assignment submitted and graded passing
  ✅  Certification issued by LMS
  ✅  Module completed (cumulative ≥ 3 modules)
  ❌  Course enrolled (inferred)
  ❌  Video watched (inferred)

INTERVIEW-RULES-v1 — Direct Evidence Events
  ✅  HackerRank/Codility test passed above threshold percentile
  ✅  TestGorilla assessment completed with score
  ✅  HireVue interview completed and scored
  ❌  Test started but not completed (noise)

COMMS-RULES-v1 — Direct Evidence Events
  ✅  Meeting facilitated (created + hosted ≥ 3 attendees, completed)
  ✅  Document authored (Google Docs — word count ≥ 500, shared externally)
  ❌  Message sent (inferred)
  ❌  Channel joined (noise)
```

---

## Step 4 — Signal Strength Calculator

Calculates a numerical signal strength (0.0–1.0) for each classified skill signal.

### Base Signal Strength Formula

```
Base Signal Strength = type_base_score
  × recency_multiplier
  × frequency_multiplier
  × quality_multiplier
  × complexity_multiplier
```

### Component Definitions

**Type Base Score:**
```
DIRECT:           0.70
INFERRED:         0.45
CORROBORATED:     0.82
DOJO-VALIDATED:   0.85
```

**Recency Multiplier:**
```
Event within last 30 days:    1.00
Event 31–90 days ago:         0.90
Event 91–180 days ago:        0.75
Event 181–365 days ago:       0.60
Event > 365 days ago:         0.40
```

**Frequency Multiplier:**
```
1 occurrence:                 1.00
2–5 occurrences (30 days):    1.10
6–15 occurrences (30 days):   1.20
16–30 occurrences (30 days):  1.25
> 30 occurrences (30 days):   1.30  (cap — prevents gaming)
```

**Quality Multiplier (from AI Semantic Enrichment):**
```
AI quality score 0.90–1.00:   1.15
AI quality score 0.70–0.89:   1.05
AI quality score 0.50–0.69:   1.00
AI quality score 0.30–0.49:   0.90
AI quality score < 0.30:      0.75
```

**Complexity Multiplier (from AI Semantic Enrichment):**
```
AI complexity = HIGH:         1.20
AI complexity = MEDIUM:       1.00
AI complexity = LOW:          0.85
AI complexity = TRIVIAL:      0.70
```

### Signal Strength Hard Caps

```
DIRECT signal:           max 0.95 (reserve 1.0 for DOJO-VALIDATED + peer review)
INFERRED signal:         max 0.70 (must corroborate to exceed)
CORROBORATED signal:     max 0.97
DOJO-VALIDATED signal:   max 1.00
```

### Signal Strength Clamping

Final signal strength is always clamped to `[0.10, max_cap_for_type]`.

No signal strength below 0.10 is emitted — classified as NOISE and discarded.

---

## Step 5 — EMST Skill Mapper

Maps each extracted signal to one or more registered skills in the EcoSkiller Master Skill Taxonomy (EMST).

### Mapping Process

```
Input: domain classification + evidence type + tool category + AI semantic tags

Mapping Methods (tried in order):
  1. EXACT MATCH: AI semantic tag exactly matches EMST skill_name → direct mapping
  2. RULE MATCH: Predefined tool-skill mapping rule matches → rule-based mapping
  3. EMBEDDING MATCH: AI embedding similarity ≥ 0.85 → ai_mapped
  4. PARTIAL MATCH: AI embedding similarity 0.65–0.84 → ai_mapped_uncertain
  5. NO MATCH: similarity < 0.65 → emit to unmapped_skill_queue (NO signal emitted)
```

### Pre-Defined Tool-Skill Mapping Rules (Core 50 Rules — Locked)

```
Rule ID    Tool              Event                           EMST Skill Code         Signal Type
────────────────────────────────────────────────────────────────────────────────────────────────
TSM-001    GitHub            PR merged + CI pass             SKD-01-BE/FE (detected) DIRECT
TSM-002    GitHub            Code review approved            SKD-01-QA-REVIEW        DIRECT
TSM-003    GitHub            Release tag + changelog         SKD-04-RELEASE-MGMT     INFERRED
TSM-004    GitLab            Pipeline passed + deploy        SKD-01-DEVOPS           DIRECT
TSM-005    Jira              Sprint closed ≥80%              SKD-04-AGILE            DIRECT
TSM-006    Jira              Epic delivered on time          SKD-04-PM               DIRECT
TSM-007    Jira              Bug resolved (authored fix)     SKD-15-DEBUGGING        DIRECT
TSM-008    Asana             Project milestone delivered     SKD-04-PM               DIRECT
TSM-009    Salesforce        Deal Closed-Won                 SKD-05-B2B-SALES        DIRECT
TSM-010    Salesforce        Pipeline stage advance          SKD-05-PIPELINE         INFERRED
TSM-011    HubSpot           Contact converted               SKD-05-B2C-SALES        DIRECT
TSM-012    HubSpot           Campaign completed              SKD-06-DIGITAL-MKT      DIRECT
TSM-013    Figma             Component published             SKD-11-VISUAL-DESIGN    DIRECT
TSM-014    Figma             Prototype approved              SKD-03-UX-DESIGN        DIRECT
TSM-015    Slack             Message volume pattern          SKD-08-COMMUNICATION    INFERRED
TSM-016    Slack             Structured thread (long form)   SKD-08-WRITTEN-COMM     INFERRED
TSM-017    Google Docs       Document authored (500+ words)  SKD-08-WRITTEN-COMM     DIRECT
TSM-018    Google Sheets     Complex formula detected        SKD-02-DATA-ANALYSIS    INFERRED
TSM-019    HackerRank        Problem solved (scored)         SKD-01-ALGORITHMS       DIRECT
TSM-020    HackerRank        Contest top percentile          SKD-01-ALGORITHMS       DIRECT
TSM-021    Codility          Assessment passed               SKD-01-CODE-QUALITY     DIRECT
TSM-022    TestGorilla       Role test passed                SKD (domain detected)   DIRECT
TSM-023    Moodle            Course completed (passing)      SKD (domain detected)   DIRECT
TSM-024    Google Classroom  Assignment submitted + graded   SKD (domain detected)   DIRECT
TSM-025    Zoom              Meeting hosted (≥3 attendees)   SKD-08-FACILITATION     DIRECT
TSM-026    MS Teams          Meeting hosted (≥3 attendees)   SKD-08-FACILITATION     DIRECT
TSM-027    Jenkins           Pipeline authored + deployed    SKD-01-DEVOPS-CI        DIRECT
TSM-028    DockerHub         Image published (public)        SKD-01-DEVOPS-DOCKER    DIRECT
TSM-029    Sentry            Alert resolved (authored fix)   SKD-15-DEBUGGING        DIRECT
TSM-030    QuickBooks        Report generated                SKD-07-ACCOUNTING       INFERRED
TSM-031    QuickBooks        Reconciliation completed        SKD-07-BOOKKEEPING      DIRECT
TSM-032    Xero              Invoice issued                  SKD-07-ACCOUNTING       INFERRED
TSM-033    Workday           Performance review submitted    SKD-09-LEADERSHIP       INFERRED
TSM-034    Greenhouse        Candidate hired (recruiter)     SKD-09-TALENT-ACQ       DIRECT
TSM-035    BambooHR          Onboarding completed            SKD-09-HR-OPS           INFERRED
TSM-036    Pipedrive         Pipeline managed (active)       SKD-05-CRM-USAGE        INFERRED
TSM-037    Apollo            Outreach sequence completed     SKD-05-OUTREACH         DIRECT
TSM-038    Airtable          Database built + shared         SKD-04-OPERATIONS       INFERRED
TSM-039    Notion            Knowledge base authored         SKD-08-DOCUMENTATION    DIRECT
TSM-040    ClickUp           Workflow automated              SKD-04-PROCESS-MGMT     DIRECT
TSM-041    Vercel            Production deployment           SKD-01-DEVOPS-DEPLOY    DIRECT
TSM-042    Netlify           Site deployed                   SKD-01-DEVOPS-DEPLOY    DIRECT
TSM-043    Adobe XD          Prototype shared                SKD-03-UX-DESIGN        DIRECT
TSM-044    Canva             Design published                SKD-11-GRAPHIC-DESIGN   INFERRED
TSM-045    SEMrush           SEO audit completed             SKD-06-SEO              DIRECT
TSM-046    Mailchimp         Campaign launched               SKD-06-EMAIL-MARKETING  DIRECT
TSM-047    ActiveCampaign    Automation built                SKD-06-MARKETING-AUTO   DIRECT
TSM-048    Postman           API collection published        SKD-01-API-DESIGN       DIRECT
TSM-049    Bitbucket         PR merged (Jira-linked)         SKD-01-BE/FE (detected) DIRECT
TSM-050    Dojo Match        Scored + mentor-reviewed        SKD (scenario domain)   DOJO-VALIDATED
```

### Unmapped Skill Queue

```sql
CREATE TABLE ssem_unmapped_skill_queue (
  queue_id              UUID PRIMARY KEY,
  pnir_id               UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  user_ecoskiller_id    UUID NOT NULL,
  source_tool           VARCHAR NOT NULL,
  ai_semantic_tag       VARCHAR NOT NULL,
  embedding_similarity  DECIMAL(4,3),
  suggested_emst_skill  VARCHAR,
  status                VARCHAR NOT NULL DEFAULT 'pending',
  -- 'pending' | 'mapped' | 'rejected' | 'new_skill_added'
  reviewed_by           UUID,
  resolved_at           TIMESTAMPTZ,
  created_at            TIMESTAMPTZ NOT NULL
);
```

---

## Step 6 — Corroboration Checker

Checks whether the current signal, combined with prior signals in the user's skill history, achieves **CORROBORATED** status.

### Corroboration Logic

```
FOR each extracted skill signal (DIRECT or INFERRED):

  1. Query user's existing skill signal history for same EMST skill_id
     within the corroboration_window (default: 90 days)

  2. Count distinct source_tool values contributing prior signals

  3. IF (current_signal_type = DIRECT OR INFERRED)
     AND (prior_distinct_sources ≥ corroboration_threshold from EMST)
     AND (prior signals contain at least 1 DIRECT signal)
     THEN:
       Upgrade current signal to CORROBORATED
       Recalculate signal_strength using CORROBORATED base (0.82)
       Apply corroboration boost: signal_strength × 1.30

  4. Emit corroboration_event to Skill Signal History store
```

### Corroboration Window Rules

```
Skill Domain              Corroboration Window
────────────────────────────────────────────────
Software Engineering      90 days
Project Management        90 days
Sales / CRM               60 days
Design                    90 days
Communication             180 days (communication is longitudinal)
Data & Analytics          90 days
Leadership                180 days (leadership signals accumulate slowly)
Domain Knowledge          365 days (knowledge retention is long-term)
Dojo Validated            N/A (no corroboration needed — highest trust)
```

---

## Step 7 — Belt Eligibility Evaluator

Determines whether the extracted skill signal should generate a **belt eligibility flag** for the Dojo Belt Engine.

### Belt Eligibility Decision Matrix

```
Condition                                          Belt Eligible?
────────────────────────────────────────────────────────────────────────
Signal Type = DOJO-VALIDATED
  AND signal_strength ≥ 0.70                            YES
  AND trust_score ≥ 0.80                                YES

Signal Type = CORROBORATED
  AND signal_strength ≥ 0.75                            YES
  AND trust_score ≥ 0.80                                YES
  AND sources_count ≥ 2                                 YES

Signal Type = DIRECT
  AND signal_strength ≥ 0.80                            YES
  AND trust_score ≥ 0.80                                YES
  AND no active fraud_flag                              YES

Signal Type = INFERRED
  (any strength)                                        NO
  → must corroborate first

Signal Type = ANY
  AND fraud_flag = TRUE                                 NO (hard block)

Signal Type = ANY
  AND trust_score < 0.80                                NO (insufficient trust)
```

### Belt Level Mapping

The SSEM outputs a **belt level recommendation** alongside each eligible skill signal.

```
Skill Level Inference from Signal Data
────────────────────────────────────────────────────────────────
BEGINNER belt:
  · 1–3 DIRECT signals in domain
  · signal_strength avg: 0.65–0.74
  · evidence span < 30 days
  · no high-complexity events

PRACTITIONER belt:
  · 4–9 DIRECT or CORROBORATED signals
  · signal_strength avg: 0.75–0.84
  · evidence span 30–90 days
  · ≥1 medium-complexity event

ADVANCED belt:
  · 10–24 DIRECT/CORROBORATED signals
  · signal_strength avg: 0.85–0.92
  · evidence span 90–180 days
  · ≥3 high-complexity events
  · ≥1 DOJO-VALIDATED signal required (AntiGravity corroboration rule)

EXPERT belt:
  · 25+ signals, multi-source corroborated
  · signal_strength avg: 0.90+
  · evidence span 180+ days
  · ≥5 high-complexity events
  · ≥3 DOJO-VALIDATED signals required
  · Mentor board review required (Dojo Governance Rule T3)
```

**Expert belt without Dojo validation is FORBIDDEN. No bypass permitted.**

---

## Step 8 — Composite Skill Profile Updater

After all signals are extracted and evaluated, the SSEM updates the user's **Composite Skill Profile (CSP)** — the live, rolling skill record that powers all downstream systems.

### Composite Skill Profile Schema

```sql
CREATE TABLE ssem_composite_skill_profile (
  profile_id              UUID PRIMARY KEY,
  user_ecoskiller_id      UUID NOT NULL,
  skill_id                UUID REFERENCES emst_skill_registry(skill_id),
  skill_name              VARCHAR NOT NULL,
  skill_domain_id         VARCHAR NOT NULL,
  composite_signal_score  DECIMAL(4,3) NOT NULL,
  -- Weighted average of all signals in rolling 365-day window
  signal_count            INTEGER NOT NULL DEFAULT 0,
  direct_signal_count     INTEGER NOT NULL DEFAULT 0,
  inferred_signal_count   INTEGER NOT NULL DEFAULT 0,
  corroborated_count      INTEGER NOT NULL DEFAULT 0,
  dojo_validated_count    INTEGER NOT NULL DEFAULT 0,
  belt_level              VARCHAR,
  -- beginner | practitioner | advanced | expert | null
  belt_eligible           BOOLEAN NOT NULL DEFAULT FALSE,
  last_signal_at          TIMESTAMPTZ,
  first_signal_at         TIMESTAMPTZ,
  evidence_span_days      INTEGER,
  skill_velocity          DECIMAL(4,3),
  -- rate of signal strength improvement over last 90 days
  trust_score_composite   DECIMAL(4,3) NOT NULL,
  taxonomy_version        VARCHAR NOT NULL DEFAULT '1.0',
  last_updated            TIMESTAMPTZ NOT NULL
);
```

### Composite Score Formula

```
Composite Signal Score =
  Σ (signal_strength_i × type_weight_i × recency_decay_i)
  ──────────────────────────────────────────────────────
  Σ (type_weight_i × recency_decay_i)

Where:
  type_weight (DIRECT):         1.00
  type_weight (INFERRED):       0.60
  type_weight (CORROBORATED):   1.30
  type_weight (DOJO-VALIDATED): 1.50

  recency_decay:
    0–30 days: 1.00
    31–90 days: 0.90
    91–180 days: 0.75
    181–365 days: 0.60
    > 365 days: excluded from composite
```

### Skill Velocity Score

Measures how rapidly a user's skill is improving:

```
skill_velocity = (avg_signal_strength_last_30d - avg_signal_strength_prior_60d)
                 ──────────────────────────────────────────────────────────────
                 prior_60d_avg_signal_strength

Positive velocity → skill improving
Negative velocity → skill regressing (flag for review/coaching suggestion)
Zero velocity → skill stable
```

---

# 🔒 SECTION SSEM-F — SKILL PROOF REPORT (SPR)

## Purpose

The Skill Proof Report (SPR) is the **human-readable, shareable, employer-facing output** generated from the SSEM Composite Skill Profile. It is the verifiable evidence package that makes EcoSkiller's talent credentials trusted by employers.

## SPR Generation Rules

```
Rule SPR-01: SPR may only be generated for skills with composite_signal_score ≥ 0.60
Rule SPR-02: SPR must display evidence_type breakdown (% direct / inferred / corroborated / dojo)
Rule SPR-03: SPR must NOT expose raw tool names if user privacy setting is restricted
Rule SPR-04: SPR must carry normalization_version, taxonomy_version, and generated_at timestamp
Rule SPR-05: SPR anti-tamper hash must be verifiable by employer via /api/v1/spr/verify endpoint
Rule SPR-06: SPR is invalidated if fraud_flag becomes active after generation
```

## SPR Contents

```
ECOSKILLER SKILL PROOF REPORT
══════════════════════════════════════════════════════════════
Candidate:          [User Display Name]
EcoSkiller ID:      [ecoskiller_id]
Trust Seal:         ✅ VERIFIED (if conditions met) | ⚠️ PROVISIONAL
Generated:          [ISO8601 timestamp]
Taxonomy Version:   v1.0
Normalization:      DNE v1.0

SKILL: React Frontend Development (SKD-01-FE-001)
──────────────────────────────────────────────────
Belt Level:         ADVANCED
Composite Score:    0.88 / 1.00
Evidence Span:      142 days
Signal Count:       18 (6 direct · 4 inferred · 5 corroborated · 3 dojo-validated)
Skill Velocity:     +0.12 (improving)

Evidence Sources:
  ✅ Developer Tools   (direct — code contributions, deployments)
  ✅ Project Tools     (corroborated — delivery record)
  ✅ Dojo Assessment   (mentor-validated match scores)
  ℹ️ Communication     (inferred — technical discussion patterns)

Verification Hash:   sha256:[anti_tamper_hash]
Verify at:           https://ecoskiller.com/verify/spr/[spr_id]
══════════════════════════════════════════════════════════════
```

## SPR API

```
POST /api/v1/ssem/spr/generate
  Auth: JWT + user consent
  Body: { user_ecoskiller_id, skill_id, visibility_scope }
  Response: { spr_id, spr_url, anti_tamper_hash, expires_at }

GET  /api/v1/ssem/spr/verify/{spr_id}
  Auth: Public endpoint (employer verification)
  Response: { valid: bool, skill_name, belt_level, composite_score, generated_at }
```

---

# 🔒 SECTION SSEM-G — AI MODEL GOVERNANCE

## AI Models Used by SSEM

The SSEM uses AI at three points:

```
AI Usage Point              Model Type                  Governance Rule
────────────────────────────────────────────────────────────────────────────
Domain Classification       Multi-label text classifier  Versioned + frozen per release
Semantic Tag Enrichment     NLP embedding model          Versioned + frozen per release
EMST Embedding Mapper       Cosine similarity + index    Versioned + frozen per release
Quality + Complexity Score  Regression model on content  Versioned + frozen per release
```

## AI Model Governance Rules

```
Rule AI-01: All AI models used in SSEM must be version-tagged
Rule AI-02: Model updates require SSEM version bump
Rule AI-03: All AI decisions must be logged with model_version and confidence
Rule AI-04: AI decisions with confidence < threshold must fall back to rule-based
Rule AI-05: AI models may never override DIRECT evidence rules
Rule AI-06: AI may not fabricate skill signals — it may only classify existing signals
Rule AI-07: Model retraining requires human approval before activation
Rule AI-08: Adversarial pattern detection must run on all AI-classified signals
```

## AI Fallback Thresholds

```
AI Component              Confidence Threshold    Fallback Action
────────────────────────────────────────────────────────────────────
Domain Classifier         < 0.70                  route to unmapped queue
Semantic Enrichment       < 0.60                  skip enrichment, use rule-based only
EMST Mapper               < 0.65                  route to unmapped queue
Quality Scorer            < 0.50                  use quality_score = 0.50 (neutral)
Complexity Scorer         < 0.50                  use complexity = MEDIUM (neutral)
```

---

# 🔒 SECTION SSEM-H — FRAUD RESISTANCE IN SKILL EXTRACTION

## Extraction-Level Fraud Rules

The SSEM adds a second layer of fraud resistance beyond the DNE Trust layer (rules F-01 to F-07). These rules operate specifically on the **skill signal extraction** stage.

### Rule SE-01 — Signal Velocity Cap

```
IF user generates > 15 DIRECT signals in same skill domain in 7 days
THEN: cap signal count to 15, flag all excess for human review
Reason: abnormal skill signal velocity
```

### Rule SE-02 — Corroboration Source Diversity Check

```
IF corroboration sources are all from same tenant workspace
  (e.g., all from same GitHub org + same Jira project)
THEN: corroboration_boost reduced to 1.10x (from 1.30x)
  AND corroborated signal flagged as 'same_workspace_corroboration'
Reason: cross-workspace corroboration is stronger trust evidence
```

### Rule SE-03 — Complexity Inflation Detection

```
IF AI complexity score > 0.85
AND user has < 5 prior signals in this skill domain
THEN: complexity_multiplier capped at 1.05
  AND signal flagged for review
Reason: experts rarely appear from nowhere
```

### Rule SE-04 — Dojo Score vs Real-World Consistency Check

```
IF user has DOJO-VALIDATED signals in a domain at ADVANCED level
AND real-world tool signals in same domain are absent or LOW
THEN: flag inconsistency to Trust Engine
  AND reduce DOJO-VALIDATED signal_strength by 0.10 pending investigation
Reason: Dojo skill should correlate with real-world tool usage patterns
```

### Rule SE-05 — Temporal Gap Anomaly

```
IF current DIRECT signal is HIGH strength
AND user had zero signals in this domain for > 180 days
THEN: signal_strength reduced by 0.10
  AND flagged as 'skill_reactivation_review'
Reason: skill dormancy followed by sudden high-strength signal is suspicious
```

---

# 🔒 SECTION SSEM-I — DATABASE SCHEMAS (COMPLETE)

## All Required Tables

```sql
-- EMST Skill Registry (Taxonomy)
CREATE TABLE emst_skill_registry (
  skill_id                UUID PRIMARY KEY,
  skill_code              VARCHAR UNIQUE NOT NULL,
  skill_name              VARCHAR NOT NULL,
  domain_id               VARCHAR NOT NULL,
  sub_domain              VARCHAR,
  category                VARCHAR NOT NULL,
  level_model             VARCHAR NOT NULL,
  evidence_rules          JSONB NOT NULL,
  taxonomy_version        VARCHAR NOT NULL DEFAULT '1.0',
  active                  BOOLEAN NOT NULL DEFAULT TRUE,
  created_at              TIMESTAMPTZ NOT NULL,
  deprecated_at           TIMESTAMPTZ
);

-- Skill Signal Records
CREATE TABLE ssem_skill_signal (
  signal_id               UUID PRIMARY KEY,
  uwdf_record_id          UUID NOT NULL,
  pnir_id                 UUID NOT NULL,
  user_ecoskiller_id      UUID NOT NULL,
  tenant_id               UUID NOT NULL,
  skill_id                UUID REFERENCES emst_skill_registry(skill_id),
  skill_name_raw          VARCHAR NOT NULL,
  signal_type             VARCHAR NOT NULL,
  -- direct | inferred | corroborated | dojo_validated | noise
  signal_strength         DECIMAL(4,3) NOT NULL,
  confidence_score        DECIMAL(4,3) NOT NULL,
  evidence_type           VARCHAR NOT NULL,
  evidence_description    TEXT,
  source_tool             VARCHAR NOT NULL,
  source_tool_category    VARCHAR NOT NULL,
  contributing_sources    JSONB,
  -- for corroborated: array of tool names
  belt_eligible           BOOLEAN NOT NULL DEFAULT FALSE,
  belt_level_recommended  VARCHAR,
  fraud_flags             JSONB,
  -- array of active SE-rules triggered
  mapping_method          VARCHAR NOT NULL,
  mapping_confidence      DECIMAL(4,3),
  mapping_uncertain       BOOLEAN DEFAULT FALSE,
  taxonomy_version        VARCHAR NOT NULL DEFAULT '1.0',
  ssem_version            VARCHAR NOT NULL DEFAULT '1.0',
  model_version           VARCHAR NOT NULL,
  event_timestamp         TIMESTAMPTZ NOT NULL,
  extracted_at            TIMESTAMPTZ NOT NULL
);

-- Composite Skill Profile (already defined in Step 8)
-- see: ssem_composite_skill_profile

-- Skill Signal History (rolling window for corroboration checks)
CREATE TABLE ssem_signal_history (
  history_id              UUID PRIMARY KEY,
  user_ecoskiller_id      UUID NOT NULL,
  skill_id                UUID NOT NULL,
  signal_id               UUID NOT NULL,
  signal_type             VARCHAR NOT NULL,
  signal_strength         DECIMAL(4,3) NOT NULL,
  source_tool             VARCHAR NOT NULL,
  source_tool_category    VARCHAR NOT NULL,
  event_timestamp         TIMESTAMPTZ NOT NULL
);
CREATE INDEX ON ssem_signal_history (user_ecoskiller_id, skill_id, event_timestamp DESC);

-- Skill Proof Reports
CREATE TABLE ssem_skill_proof_reports (
  spr_id                  UUID PRIMARY KEY,
  user_ecoskiller_id      UUID NOT NULL,
  skill_id                UUID NOT NULL,
  spr_data                JSONB NOT NULL,
  anti_tamper_hash        VARCHAR NOT NULL,
  generated_at            TIMESTAMPTZ NOT NULL,
  expires_at              TIMESTAMPTZ,
  visibility_scope        VARCHAR NOT NULL DEFAULT 'private',
  -- private | shared_link | public
  fraud_invalidated       BOOLEAN NOT NULL DEFAULT FALSE,
  fraud_invalidated_at    TIMESTAMPTZ
);

-- Domain Classification Log
-- see: ssem_domain_classification

-- Field Mapping Log
-- see: dne_field_mapping_log (parent DNE module)

-- Unmapped Skill Queue
-- see: ssem_unmapped_skill_queue
```

---

# 🔒 SECTION SSEM-J — API CONTRACT REGISTRY

```
POST /api/v1/ssem/extract
  Auth: Internal DNE service token
  Body: { pnir_id, enriched_pnir, user_ecoskiller_id, tenant_id }
  Response: { signals: [SkillSignal], extraction_summary }

GET  /api/v1/ssem/skill-profile/{user_ecoskiller_id}
  Auth: JWT + RBAC (skill_engine, belt_engine, ai_matcher roles)
  Response: { composite_skill_profile: [...], last_updated }

GET  /api/v1/ssem/skill-signals/{user_ecoskiller_id}
  Auth: JWT + RBAC
  Query: ?skill_id=&type=&date_from=&date_to=&min_strength=
  Response: { signals: [...], total }

GET  /api/v1/ssem/belt-eligible/{user_ecoskiller_id}
  Auth: JWT + Belt Engine role
  Response: { eligible_skills: [{ skill_id, belt_level, signal_count, composite_score }] }

POST /api/v1/ssem/spr/generate
  Auth: JWT + user self
  Body: { skill_id, visibility_scope }
  Response: { spr_id, spr_url, anti_tamper_hash, expires_at }

GET  /api/v1/ssem/spr/verify/{spr_id}
  Auth: Public
  Response: { valid, skill_name, belt_level, composite_score, generated_at, fraud_invalidated }

GET  /api/v1/ssem/taxonomy
  Auth: JWT (any authenticated user)
  Response: { skills: [EMST entries], taxonomy_version }

GET  /api/v1/ssem/unmapped-queue
  Auth: JWT + Taxonomy Admin role
  Response: { pending_items: [...], total }

POST /api/v1/ssem/unmapped-queue/{queue_id}/resolve
  Auth: JWT + Taxonomy Admin role
  Body: { resolution: 'mapped' | 'rejected' | 'new_skill', mapped_skill_id?, new_skill? }
  Response: { updated queue item }
```

---

# 🔒 SECTION SSEM-K — OBSERVABILITY & ALERTING

## Required SSEM Metrics

```
Metric                                    Threshold     Alert Level
────────────────────────────────────────────────────────────────────
Extraction pipeline failure rate          > 1%          WARNING
Extraction pipeline failure rate          > 5%          CRITICAL
Unmapped skill queue depth                > 200 items   WARNING
AI classification uncertainty rate        > 15%         WARNING
Fraud rule SE-01 to SE-05 trigger rate    > 8%          WARNING
Signal noise rate (below 0.10 discarded)  > 30%         WARNING
Composite profile stale (>7 days)         any           WARNING
SPR generation failure rate               > 2%          WARNING
Belt eligibility conversion rate          < 5%          INFO
```

## Required SSEM Dashboards

```
Dashboard 1 — Extraction Quality
  · Signals extracted per hour (by type)
  · DIRECT / INFERRED / CORROBORATED / DOJO breakdown
  · Noise discard rate
  · AI confidence distribution

Dashboard 2 — Taxonomy Coverage
  · Top 20 extracted skills (volume)
  · Unmapped skill queue (volume + age)
  · Domain coverage map
  · New skill suggestion frequency

Dashboard 3 — Belt Pipeline
  · Belt eligibility flags generated per day
  · Conversion rate: signal → belt eligibility → belt awarded
  · Belt level distribution across user base
  · DOJO-VALIDATED signal volume (required for ADVANCED+)

Dashboard 4 — Fraud & Integrity
  · SE rule trigger rates (SE-01 to SE-05)
  · Same-workspace corroboration flags
  · Skill velocity anomalies
  · Dojo vs real-world consistency gaps
```

---

# 🔒 SECTION SSEM-L — VERSION GOVERNANCE

## Version Lock Rules

```
Change Type                                   Needs Version Bump?
──────────────────────────────────────────────────────────────────────
Add new TSM tool-skill mapping rule           NO (add-only)
Add new EMST skill                            NO (add-only, taxonomy board approval)
Add new AI model for enrichment               YES (model version bump)
Add new fraud rule (SE-xx)                    YES
Change signal strength formula                YES
Change signal type definitions                YES (FORBIDDEN without major bump)
Change belt eligibility thresholds            YES
Change corroboration window                   YES
Change composite score formula                YES
Remove any TSM rule                           FORBIDDEN
Remove any EMST skill (only deprecate)        FORBIDDEN
Remove any fraud rule                         FORBIDDEN
```

---

# 🔒 SECTION SSEM-M — MASTER PROMPT INSERT BLOCK

Paste this block into the EcoSkiller Master Execution Prompt alongside the DNE Seal and Dojo Trust Seal:

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
BEGIN LOCKED ANTIGRAVITY SSEM ARTIFACT — v1.0

Module:                 Structured Skill Extraction Model (SSEM)
Layer:                  Enterprise Optimization + Trust Infrastructure
Parent:                 Data Normalization Engine (DNE) — Stage 5
Namespace:              ecoskiller.enterprise.dne.ssem
Version:                1.0-SEALED

Taxonomy:               EMST v1.0 — 15 Domains — LOCKED
Signal Types:           DIRECT | INFERRED | CORROBORATED | DOJO-VALIDATED — FROZEN
Extraction Steps:       8 — ALL MANDATORY — NONE SKIPPABLE
TSM Rules:              50 Tool-Skill Mapping Rules — LOCKED (add-only)
Fraud Rules:            SE-01 through SE-05 — ALL ACTIVE
Belt Eligibility:       DIRECT(≥0.80) | CORROBORATED(≥0.75) | DOJO(≥0.70) + trust≥0.80
Advanced Belt:          DOJO-VALIDATED signal REQUIRED — no bypass
Expert Belt:            3× DOJO-VALIDATED + Mentor Board — no bypass
Composite Score:        Weighted decay formula — LOCKED
Skill Proof Report:     Anti-tamper hash — publicly verifiable
AI Governance:          All models versioned + frozen — fallback rules active
Inferred Signals:       NOT belt-eligible alone — must corroborate first
Noise Discard:          Signals < 0.10 discarded — not emitted
Bypass:                 FORBIDDEN at all stages
Interpretation:         NONE
Mutation Policy:        ADD-ONLY via version bump
Architecture Authority: LOCKED

END LOCKED ANTIGRAVITY SSEM ARTIFACT
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

# 🔒 SECTION SSEM-N — FINAL ENFORCEMENT SEAL

```
STRUCTURED SKILL EXTRACTION MODEL
ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY LAYER
ECOSKILLER PRODUCTION SYSTEM

STATUS: LOCKED · SEALED · DETERMINISTIC · GOVERNED

EMST TAXONOMY LOCK:                   ACTIVE — v1.0
FOUR SIGNAL TYPES:                    FROZEN — DIRECT | INFERRED | CORROBORATED | DOJO-VALIDATED
EIGHT EXTRACTION STEPS:               ALL MANDATORY
50 TSM MAPPING RULES:                 LOCKED — ADD-ONLY
SIGNAL STRENGTH CALCULATOR:           FORMULA LOCKED
CORROBORATION ENGINE:                 ACTIVE
BELT ELIGIBILITY EVALUATOR:           THRESHOLDS LOCKED
COMPOSITE PROFILE UPDATER:            FORMULA LOCKED
ADVANCED BELT DOJO REQUIREMENT:       ENFORCED — NO BYPASS
EXPERT BELT DOJO + MENTOR REQUIREMENT:ENFORCED — NO BYPASS
FRAUD RULES SE-01 TO SE-05:           ALL ACTIVE
AI MODEL GOVERNANCE:                  VERSIONED + FALLBACK ACTIVE
SKILL PROOF REPORT ANTI-TAMPER:       ACTIVE
SPR FRAUD INVALIDATION:               ACTIVE
INFERRED-ONLY BELT BLOCK:             ENFORCED
NOISE DISCARD THRESHOLD:              0.10 — ENFORCED
UNMAPPED SKILL QUEUE:                 ACTIVE — NO SILENT DISCARD
TAXONOMY BYPASS:                      FORBIDDEN
PIPELINE STAGE SKIP:                  FORBIDDEN
INTERPRETATION AUTHORITY:             NONE
MUTATION POLICY:                      ADD-ONLY VIA VERSION BUMP

Absence of any Extraction Step   → STOP EXECUTION
Absent Fraud Rule SE-01 to SE-05 → STOP EXECUTION
Belt Bypass Attempt              → STOP EXECUTION + INTEGRITY ALERT
Expert Belt without Dojo         → STOP EXECUTION + GOVERNANCE ALERT
EMST Taxonomy absent             → STOP EXECUTION
AI Model unversioned             → STOP EXECUTION
SPR anti-tamper missing          → STOP EXECUTION
```

---

*EcoSkiller AntiGravity · Structured Skill Extraction Model · Enterprise Optimization + Trust Infrastructure · v1.0-SEALED · 2026-02-26*
