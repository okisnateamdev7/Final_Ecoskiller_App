# 🔒 ANTIGRAVITY — ARTIFACT #89
## AI FIELD MAPPING MODEL
### Layer: ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Artifact Class: Production Execution Contract — Sealed & Locked
### Mutation Policy: Add-Only via Version Bump
### Execution Mode: Deterministic · Event-Driven · Non-Interpretable
### Interpretation Authority: NONE
### Version: v1.0 — LOCKED

---

> **SEAL DECLARATION**
> This document is a locked production artifact for the ANTIGRAVITY system — the unified intelligence layer governing ECOSKILLER + DOJO SaaS. The AI Field Mapping Model (AIFMM) is the authoritative specification for all automated field detection, schema translation, semantic alignment, data normalization, and skill extraction operations across all 100 integrated and migratable platforms. No mapping rule, confidence threshold, field resolution logic, or AI model boundary defined herein may be altered without a formal version bump and Governance Board approval.
> Absence of any declared component → STOP EXECUTION.

---

## SECTION AIFMM-0 — SYSTEM CONTEXT BINDING

| Dimension | Value |
|---|---|
| Platform | ANTIGRAVITY (ECOSKILLER + DOJO SaaS Unified) |
| Layer | Enterprise Optimization + Trust Infrastructure |
| Artifact Number | 89 |
| Component Name | AI Field Mapping Model (AIFMM) |
| Parent System | ECOSKILLER Universal Migration Engine (EUME) + Integration Engine (EIE) |
| Execution Stack | Python AI Services + Node.js Microservices + PostgreSQL 15 + Redis 7 |
| AI Runtime | UWDF (Universal Work Data Format) Normalization Layer |
| Trigger Surfaces | Migration flow · Integration sync · Resume import · Tool connector · Bulk upload |
| Auth Binding | OAuth2 + OIDC + JWT — all mapping jobs require authenticated tenant context |
| Governance Gate | All model updates require version bump + Governance Board approval |

---

## SECTION AIFMM-1 — MISSION DECLARATION

The AI Field Mapping Model solves one of the hardest enterprise data problems: **every source system uses different field names, data types, structures, and conventions to represent the same real-world entities.** The AIFMM is the intelligence layer that bridges all external systems into Antigravity's canonical data model automatically, without human intervention, with full audit trail, and with trust-grade confidence scoring on every mapping decision.

**Three Core Problems AIFMM Solves:**

**Problem 1 — Semantic Mismatch:** `employee_name` (Workday) = `full_name` (Ecoskiller) = `member_display_name` (Slack) = `contributor` (GitHub). These are the same entity expressed differently across 100 platforms.

**Problem 2 — Type Mismatch:** Source sends `"2024-03-15"` (string), Ecoskiller requires `TIMESTAMP WITH TIME ZONE`. Source sends `"85%"` (string), Ecoskiller requires `DECIMAL(5,2)`.

**Problem 3 — Structure Mismatch:** Source has `first_name + last_name` as two fields. Ecoskiller has `full_name` as one. Source has role embedded in a user object. Ecoskiller has role as a foreign-key relationship.

---

## SECTION AIFMM-2 — CANONICAL TARGET MODEL (LOCKED)

The Antigravity canonical model is the immutable target for all field mapping operations. Source fields are always mapped INTO these entities. Canonical entities cannot be renamed (Ecoskiller Data Model Freeze, Section C).

### Core Canonical Entities

```
User                    → Identity, auth, profile
Tenant                  → Organisation / institute / company
Role                    → RBAC assignment
Skill                   → Verified skill definition
UserSkill               → User ↔ Skill with level
Job                     → Job listing
Project                 → Project entity
EducationCourse         → Course / curriculum unit
Match                   → Dojo match session
Score                   → Match scoring record
Belt                    → Certification level
Certification           → Issued certificate
Analytics               → Aggregated performance record
WorkDataRecord          → UWDF normalised real work signal
IntegrationSyncLog      → Audit log of all sync operations
MigrationRecord         → Full migration audit trail
FieldMappingDecision    → Immutable log of every AI mapping decision
```

### Canonical Field Registry (Core User Entity — Sample)

| Canonical Field | Type | Nullable | Source of Truth |
|---|---|---|---|
| `id` | UUID | NO | System-generated |
| `email` | TEXT UNIQUE | NO | Verified at import |
| `full_name` | TEXT | NO | Mapped from source |
| `phone` | TEXT | YES | Mapped from source |
| `avatar_url` | TEXT | YES | Mapped from source |
| `is_verified` | BOOLEAN | NO | Default FALSE; requires verification flow |
| `tenant_id` | UUID FK | NO | Injected from migration/sync context |
| `role_id` | UUID FK | YES | Mapped from source role field |
| `created_at` | TIMESTAMP TZ | NO | Preserved from source or set at import |
| `source_system` | TEXT | NO | System identifier of origin platform |
| `source_record_id` | TEXT | NO | Original ID in source system |
| `mapping_confidence` | DECIMAL(4,3) | NO | AI mapping confidence score |

---

## SECTION AIFMM-3 — AI MAPPING ENGINE ARCHITECTURE

### 3.1 Engine Layers (All Required — No Layer Optional)

```
┌─────────────────────────────────────────────────────────────┐
│              AIFMM ENGINE STACK (LOCKED)                    │
│                                                             │
│  L1 ── SOURCE CONNECTOR LAYER                               │
│        OAuth / API Key / Webhook / Export File              │
│        → Produces: raw_source_payload                       │
│                                                             │
│  L2 ── SCHEMA DISCOVERY ENGINE                              │
│        Field detection · Type inference · Structure scan    │
│        → Produces: source_schema_manifest                   │
│                                                             │
│  L3 ── SEMANTIC MATCHING ENGINE (AI CORE)                   │
│        Embedding model · Similarity scoring                 │
│        Rule-based override · Confidence ranking             │
│        → Produces: field_mapping_candidates[]               │
│                                                             │
│  L4 ── TYPE RESOLUTION ENGINE                               │
│        Type coercion rules · Format normalisation           │
│        Null handling · Default injection                    │
│        → Produces: typed_field_mapping                      │
│                                                             │
│  L5 ── STRUCTURE NORMALISATION ENGINE                       │
│        Split / merge / flatten / nest operations            │
│        Relationship resolution                              │
│        → Produces: structured_record_draft                  │
│                                                             │
│  L6 ── SKILL EXTRACTION ENGINE                              │
│        NLP skill detection from text fields                 │
│        Tool-to-skill mapping · UWDF conversion              │
│        → Produces: extracted_skill_signals[]                │
│                                                             │
│  L7 ── VALIDATION & CONFIDENCE ENGINE                       │
│        Confidence threshold gates · Human review flags      │
│        Anomaly detection · Duplicate detection              │
│        → Produces: validated_mapping_package                │
│                                                             │
│  L8 ── IMPORT EXECUTOR                                      │
│        Transactional write to PostgreSQL                    │
│        Event emission to Redis Streams                      │
│        Audit log write (immutable)                          │
│        → Produces: import_result + FieldMappingDecision log │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Engine Execution Rules

- All 8 layers execute in strict sequence. No layer may be skipped.
- L3 failure → L7 must flag for human review, not auto-reject
- L6 executes in parallel with L4–L5 (non-blocking)
- L8 is transactional — partial writes are rolled back
- Every execution produces one `FieldMappingDecision` log record (immutable)
- Absence of any layer → STOP EXECUTION

---

## SECTION AIFMM-4 — LAYER 2: SCHEMA DISCOVERY ENGINE

### 4.1 Discovery Protocol

The Schema Discovery Engine runs before any mapping attempt. It constructs a `source_schema_manifest` from the raw source payload.

**Discovery Inputs:**

| Input Type | Examples | Discovery Method |
|---|---|---|
| REST API response | Workday, BambooHR, Salesforce | JSON key traversal + type inference |
| GraphQL schema | GitHub, Shopify | Introspection query |
| CSV / Excel export | Legacy HRMS, manual exports | Header row parsing + column sampling |
| SQL dump | School ERP, Moodle export | DDL parsing + sample data scan |
| Webhook payload | Slack, Jira, GitHub events | Payload key traversal |

**Schema Manifest Output (per field):**

```json
{
  "source_field_path": "employee.personal.name",
  "inferred_type": "string",
  "sample_values": ["Arjun Sharma", "Priya Mehta"],
  "null_rate": 0.02,
  "unique_rate": 0.99,
  "detected_pattern": "FULL_NAME",
  "field_depth": 2,
  "parent_entity": "employee"
}
```

### 4.2 Pattern Library (Locked Detectors)

The Schema Discovery Engine uses a locked pattern library to classify field candidates before semantic matching.

| Pattern ID | Pattern Name | Detection Signal | Example Matches |
|---|---|---|---|
| PTN-01 | FULL_NAME | String, high uniqueness, 2–4 word distribution | `name`, `full_name`, `display_name`, `employee_name` |
| PTN-02 | FIRST_NAME | String, 1-word, paired with last_name | `first_name`, `fname`, `given_name` |
| PTN-03 | LAST_NAME | String, 1-word, paired with first_name | `last_name`, `lname`, `surname`, `family_name` |
| PTN-04 | EMAIL | String, `@` present, RFC 5322 compliant | `email`, `email_address`, `work_email`, `login` |
| PTN-05 | PHONE | String/Int, E.164 format signals | `phone`, `mobile`, `contact_no`, `phone_number` |
| PTN-06 | DATE | ISO 8601, Unix timestamp, or localised date | `dob`, `hire_date`, `created_on`, `joined_at` |
| PTN-07 | ROLE_LABEL | String, low cardinality (<20 distinct values) | `role`, `position`, `designation`, `job_title` |
| PTN-08 | SKILL_NAME | String, matches skill taxonomy lookup | `skill`, `competency`, `technology`, `expertise` |
| PTN-09 | SCORE_VALUE | Numeric, 0–100 range OR percentage | `score`, `grade`, `rating`, `performance_pct` |
| PTN-10 | STATUS_FLAG | ENUM / Boolean / low-cardinality string | `status`, `is_active`, `employment_type` |
| PTN-11 | IDENTIFIER | UUID, integer ID, or system-specific key | `id`, `employee_id`, `user_key`, `record_id` |
| PTN-12 | ORGANISATION | String, low uniqueness per tenant | `company`, `department`, `team`, `division` |
| PTN-13 | CURRENCY_AMOUNT | Numeric + currency context | `salary`, `amount`, `fee`, `payment` |
| PTN-14 | URL | String, URL pattern | `avatar`, `profile_pic`, `linkedin_url`, `photo` |
| PTN-15 | FREE_TEXT | Long string, low structure | `bio`, `description`, `notes`, `comments` |
| PTN-16 | SKILL_LEVEL | Numeric or ENUM, low cardinality | `level`, `proficiency`, `rating`, `band` |
| PTN-17 | TENANT_REF | Foreign key to org/company | `org_id`, `company_id`, `institute_id` |
| PTN-18 | TIMESTAMP | Full datetime with timezone signals | `created_at`, `updated_at`, `timestamp` |
| PTN-19 | BOOLEAN_FLAG | True/false binary field | `is_verified`, `active`, `enabled` |
| PTN-20 | MULTI_SKILL_LIST | Array or comma-separated skill strings | `skills`, `technologies`, `tools_used` |

---

## SECTION AIFMM-5 — LAYER 3: SEMANTIC MATCHING ENGINE (AI CORE)

### 5.1 Model Specification

| Parameter | Value |
|---|---|
| Model Type | Sentence embedding model (semantic similarity) |
| Embedding Dimension | 384 (sentence-transformers/all-MiniLM-L6-v2 class) |
| Similarity Metric | Cosine similarity |
| Threshold: Auto-Accept | ≥ 0.88 |
| Threshold: Human Review | 0.65 – 0.87 |
| Threshold: Auto-Reject | < 0.65 |
| Rule Override Priority | Rule-based mappings always supersede model output |
| Retraining Trigger | Quarterly OR when human override rate > 10% in 30-day window |

### 5.2 Mapping Decision Flow

```
source_field_label + sample_values + pattern_class
           ↓
  [RULE ENGINE CHECK]
  Known exact mapping? ─── YES ──→ Apply rule · Confidence = 1.0 · Skip model
           ↓ NO
  [EMBEDDING ENGINE]
  Embed source field context
  Embed all canonical field contexts
  Compute cosine similarity matrix
           ↓
  Top-3 candidate matches with scores
           ↓
  Score ≥ 0.88?  ──→ AUTO-ACCEPT · Log decision · Proceed to L4
  Score 0.65–0.87? → FLAG FOR HUMAN REVIEW · Hold record
  Score < 0.65?  ──→ UNMAPPED FLAG · Human mapping required · No import
```

### 5.3 Rule Engine (Locked Exact Mappings)

The Rule Engine takes precedence over all model outputs. These mappings are locked. New rules added via version bump only.

#### HR / ATS Systems

| Source System | Source Field | Canonical Field | Confidence |
|---|---|---|---|
| Workday | `Worker_ID` | `source_record_id` | 1.0 |
| Workday | `Legal_Name_-_Last_Name` | derived → `full_name` merge | 1.0 |
| Workday | `Legal_Name_-_First_Name` | derived → `full_name` merge | 1.0 |
| Workday | `Email_-_Work` | `email` | 1.0 |
| Workday | `Job_Profile_Name` | `role_id` (lookup) | 1.0 |
| Workday | `Hire_Date` | `created_at` | 1.0 |
| BambooHR | `firstName` | derived → `full_name` merge | 1.0 |
| BambooHR | `lastName` | derived → `full_name` merge | 1.0 |
| BambooHR | `workEmail` | `email` | 1.0 |
| BambooHR | `department` | `tenant_id` sub-unit (lookup) | 1.0 |
| BambooHR | `jobTitle` | `role_id` (lookup) | 1.0 |
| Zoho People | `Employee_ID` | `source_record_id` | 1.0 |
| Zoho People | `Email_ID` | `email` | 1.0 |
| Darwinbox | `employee_code` | `source_record_id` | 1.0 |
| Darwinbox | `official_email` | `email` | 1.0 |
| SAP SuccessFactors | `userId` | `source_record_id` | 1.0 |
| SAP SuccessFactors | `defaultEmail` | `email` | 1.0 |
| Rippling | `personal_email` | `email` | 1.0 |
| Keka HR | `email_address` | `email` | 1.0 |
| ADP | `AssociateID` | `source_record_id` | 1.0 |

#### CRM Systems

| Source System | Source Field | Canonical Field | Confidence |
|---|---|---|---|
| Salesforce | `Id` | `source_record_id` | 1.0 |
| Salesforce | `Email` | `email` | 1.0 |
| Salesforce | `Name` | `full_name` | 1.0 |
| Salesforce | `Title` | `role_id` (lookup) | 1.0 |
| Salesforce | `Account.Name` | `tenant_id` (lookup) | 1.0 |
| HubSpot | `hs_object_id` | `source_record_id` | 1.0 |
| HubSpot | `email` | `email` | 1.0 |
| HubSpot | `firstname` + `lastname` | derived → `full_name` | 1.0 |
| HubSpot | `jobtitle` | `role_id` (lookup) | 1.0 |
| Zoho CRM | `CONTACTID` | `source_record_id` | 1.0 |
| Pipedrive | `person_id` | `source_record_id` | 1.0 |
| Pipedrive | `email[0].value` | `email` | 1.0 |

#### Developer Platforms

| Source System | Source Field | Canonical Field | Confidence |
|---|---|---|---|
| GitHub | `login` | `source_record_id` | 1.0 |
| GitHub | `email` | `email` | 1.0 |
| GitHub | `name` | `full_name` | 1.0 |
| GitHub | `avatar_url` | `avatar_url` | 1.0 |
| GitHub | `company` | `tenant_id` (lookup) | 0.95 |
| GitLab | `username` | `source_record_id` | 1.0 |
| GitLab | `email` | `email` | 1.0 |
| Jira | `accountId` | `source_record_id` | 1.0 |
| Jira | `emailAddress` | `email` | 1.0 |
| Jira | `displayName` | `full_name` | 1.0 |

#### Communication Tools

| Source System | Source Field | Canonical Field | Confidence |
|---|---|---|---|
| Slack | `id` | `source_record_id` | 1.0 |
| Slack | `profile.email` | `email` | 1.0 |
| Slack | `profile.real_name_normalized` | `full_name` | 1.0 |
| Slack | `profile.image_512` | `avatar_url` | 1.0 |
| Slack | `profile.title` | `role_id` (lookup) | 0.90 |
| Microsoft Teams | `userPrincipalName` | `email` | 1.0 |
| Microsoft Teams | `displayName` | `full_name` | 1.0 |

#### EdTech / LMS Platforms

| Source System | Source Field | Canonical Field | Confidence |
|---|---|---|---|
| Moodle | `username` | `source_record_id` | 1.0 |
| Moodle | `email` | `email` | 1.0 |
| Moodle | `firstname` + `lastname` | derived → `full_name` | 1.0 |
| Moodle | `course.fullname` | `EducationCourse.title` | 1.0 |
| Moodle | `grade.finalgrade` | `Score.value` | 1.0 |
| Google Classroom | `userId` | `source_record_id` | 1.0 |
| Google Classroom | `emailAddress` | `email` | 1.0 |
| Google Classroom | `name.fullName` | `full_name` | 1.0 |
| Canvas LMS | `login_id` | `email` | 1.0 |
| Canvas LMS | `name` | `full_name` | 1.0 |
| Canvas LMS | `enrollments[].type` | `role_id` (lookup) | 1.0 |
| Teachmint | `phone_number` | `phone` | 1.0 |
| Classplus | `student_id` | `source_record_id` | 1.0 |

---

## SECTION AIFMM-6 — LAYER 4: TYPE RESOLUTION ENGINE

### 6.1 Type Coercion Rules (Locked)

All type transformations are deterministic. No ambiguous coercion permitted.

| Source Type | Source Format | Canonical Type | Coercion Rule |
|---|---|---|---|
| String date | `"2024-03-15"` | TIMESTAMP TZ | Parse ISO 8601 → append `T00:00:00Z` |
| String date | `"15/03/2024"` | TIMESTAMP TZ | Detect locale format → convert to ISO 8601 |
| Unix timestamp | `1710460800` | TIMESTAMP TZ | Multiply ×1000 if ms → `new Date(ts).toISOString()` |
| String percentage | `"85%"` | DECIMAL(5,2) | Strip `%` → parse float → divide by 100 |
| String boolean | `"true"`, `"yes"`, `"1"`, `"active"` | BOOLEAN | Map to TRUE |
| String boolean | `"false"`, `"no"`, `"0"`, `"inactive"` | BOOLEAN | Map to FALSE |
| Integer ID | `12345` | TEXT | Cast to string (IDs are stored as TEXT) |
| Currency string | `"$5,000"` | DECIMAL(12,2) | Strip currency symbol + commas → parse float |
| Comma list | `"Python, SQL, React"` | TEXT[] | Split on `, ` → produce array |
| Null / empty | `null`, `""`, `"N/A"`, `"—"` | NULL | Normalize all empty representations to NULL |
| Phone E.164 | `"+91-9876543210"` | TEXT | Strip separators → store `+919876543210` |
| Phone local | `"9876543210"` (India) | TEXT | Detect region → prepend country code |

### 6.2 Null Handling Protocol

| Field Nullability | Behaviour on Null Input |
|---|---|
| NOT NULL canonical field | Block import → flag for human review |
| NOT NULL with default | Inject default value → log in FieldMappingDecision |
| NULLABLE canonical field | Accept NULL → store as NULL |
| Required for belt/cert gate | Block downstream operations until resolved |

### 6.3 Split / Merge Operations (Structure Normalisation — Layer 5)

| Operation | Source Pattern | Target Pattern | Rule |
|---|---|---|---|
| MERGE | `first_name` + `last_name` | `full_name` | `TRIM(first_name) + " " + TRIM(last_name)` |
| SPLIT | `full_name` (single string) | `full_name` (kept as-is) | Ecoskiller stores as single field; no split required |
| FLATTEN | `employee.contact.email` (nested JSON) | `email` | Dot-notation path traversal to extract leaf value |
| EXTRACT | `address` (street, city, state, pin) | `location_text` | Concatenate components with comma separator |
| LOOKUP | `"Manager"` (string role label) | `role_id` (UUID) | Match against Role taxonomy table → return UUID |
| ARRAY | `skills: ["Python","SQL"]` (JSON array) | `UserSkill[]` (rows) | Explode array → create one UserSkill row per item |
| ENUM-MAP | `"Full-Time"` (source) | `employment_type ENUM` | Map against locked enum dictionary (see below) |

### 6.4 Enum Mapping Dictionary (Locked)

| Canonical Enum Field | Accepted Source Values | Canonical Value |
|---|---|---|
| `employment_type` | Full-Time, FT, Permanent, Regular | `full_time` |
| `employment_type` | Part-Time, PT, Contract, Temp | `part_time` |
| `employment_type` | Intern, Internship, Trainee | `intern` |
| `employment_type` | Freelance, Consultant, Contractor | `freelance` |
| `project.status` | Open, Active, In Progress, Live | `open` |
| `project.status` | Closed, Done, Completed, Finished | `closed` |
| `skill.level` | Beginner, Basic, 1, L1, Novice | `1` |
| `skill.level` | Intermediate, Mid, 2, L2 | `2` |
| `skill.level` | Advanced, Senior, 3, L3, Expert | `3` |
| `skill.level` | Expert, Principal, 4, L4, Master | `4` |
| `user.is_verified` | true, yes, verified, active, 1 | `TRUE` |
| `user.is_verified` | false, no, unverified, inactive, 0 | `FALSE` |

---

## SECTION AIFMM-7 — LAYER 6: SKILL EXTRACTION ENGINE

### 7.1 Skill Extraction Sources

The Skill Extraction Engine processes free-text and structured fields from source systems to produce `extracted_skill_signals[]` that feed into `UserSkill` records and the `WorkDataRecord` UWDF model.

| Source System | Field(s) Processed | Extraction Method |
|---|---|---|
| GitHub | `repo.languages`, commit messages, README content | Code language taxonomy lookup + NLP |
| GitLab | `project.topics`, MR descriptions | Taxonomy lookup + keyword NLP |
| Jira | Issue titles, labels, components, epic names | NLP entity extraction + project taxonomy |
| Asana / ClickUp | Task titles, project descriptions, custom fields | NLP keyword extraction |
| Salesforce | `Opportunity.Type`, activity notes, product lines | Sales skill taxonomy mapping |
| HubSpot | Deal stages, campaign types, contact properties | Marketing / sales skill extraction |
| Figma / Adobe XD | Project type labels, component naming conventions | Design skill taxonomy |
| Slack / Teams | Channel names, pinned messages (with consent) | Soft skill signals (NLP, consent-gated) |
| LMS (Moodle, Canvas) | Course names, completed module titles, quiz topics | Educational skill taxonomy |
| Resumes (PDF/DOCX) | Full resume text | NLP named entity recognition + skill ontology |
| Google Classroom | Assignment titles, course descriptions | Educational taxonomy |
| Teachmint / Classplus | Subject names, test types | Indian curriculum taxonomy |

### 7.2 Skill Taxonomy (Locked Ontology Domains)

The Skill Extraction Engine maps extracted tokens to the Antigravity locked skill taxonomy. No skill may be created outside this taxonomy without a Governance Board version bump.

| Taxonomy Domain | Domain ID | Example Skills |
|---|---|---|
| Software Engineering | SKT-01 | Python, JavaScript, React, Node.js, SQL, Docker, Kubernetes |
| Data & AI | SKT-02 | Machine Learning, Data Analysis, TensorFlow, Pandas, SQL |
| Design | SKT-03 | UI/UX, Figma, Prototyping, Wireframing, Visual Design |
| Sales & CRM | SKT-04 | Lead Generation, Pipeline Management, Salesforce, Negotiation |
| Marketing | SKT-05 | SEO, Content Marketing, Google Ads, Analytics, Copywriting |
| Project Management | SKT-06 | Agile, Scrum, JIRA, Risk Management, Stakeholder Management |
| Finance & Accounting | SKT-07 | Bookkeeping, QuickBooks, Financial Modelling, GST, Tally |
| Communication | SKT-08 | Presentation, Written Communication, Negotiation, Facilitation |
| Leadership | SKT-09 | Team Management, Decision Making, Conflict Resolution |
| Research | SKT-10 | Literature Review, Data Collection, Statistical Analysis |
| Education & Training | SKT-11 | Curriculum Design, Assessment, Classroom Management |
| Dojo Performance Skills | SKT-12 | All Dojo-defined skill constructs (Section T1 binding) |

### 7.3 Skill Signal Confidence Model

| Signal Source | Base Confidence | Boost Conditions | Penalty Conditions |
|---|---|---|---|
| GitHub verified repo language | 0.90 | >100 commits in language: +0.05 | Forked only repo: −0.15 |
| LMS course completion | 0.85 | Score ≥ 80%: +0.10 | Score < 60%: −0.20 |
| Jira project role match | 0.80 | >20 issues closed: +0.08 | Role = viewer only: −0.30 |
| Resume text extraction | 0.65 | Corroborated by other signal: +0.15 | No corroborating signal: −0.10 |
| Self-declared (profile) | 0.40 | Verified by Dojo match: +0.45 | No verification: 0 |
| Dojo Match Score | 0.95 | Belt awarded: +0.05 | Low confidence SP-02 < 0.6: −0.20 |
| Employer confirmation | 1.00 | — | Retracted by employer: set to 0 |

**Minimum confidence for verified skill badge: 0.75**
**Minimum confidence for Dojo belt eligibility contribution: 0.85**
**Minimum confidence for hiring marketplace skill display: 0.75**

### 7.4 UWDF Output Schema (Universal Work Data Format)

Every extracted skill signal is normalised into a `WorkDataRecord` in UWDF format before writing to the Ecoskiller data store.

```json
{
  "record_id": "uuid",
  "user_id": "uuid",
  "tenant_id": "uuid",
  "source_system": "github",
  "source_record_ref": "repo/ecoskiller-api",
  "signal_type": "code_commit_language",
  "extracted_skill_id": "uuid → Python (SKT-01)",
  "skill_level_signal": 3,
  "confidence_score": 0.93,
  "evidence_ref": "https://github.com/user/repo",
  "extracted_at": "2025-06-01T10:00:00Z",
  "uwdf_version": "1.0",
  "mapping_decision_id": "uuid → FieldMappingDecision FK",
  "trust_gate_passed": true
}
```

---

## SECTION AIFMM-8 — LAYER 7: VALIDATION & CONFIDENCE ENGINE

### 8.1 Validation Gates (All Required — No Gate Optional)

| Gate ID | Gate Name | Check | Fail Action |
|---|---|---|---|
| VG-01 | Email Uniqueness | `email` not already in User table for tenant | FLAG as duplicate → human review |
| VG-02 | Required Fields Present | All NOT NULL canonical fields are populated | BLOCK import of record |
| VG-03 | Confidence Threshold | All auto-accepted mappings have score ≥ 0.88 | FLAG low-confidence → review queue |
| VG-04 | Type Validity | All coerced values conform to canonical types | BLOCK record → type error log |
| VG-05 | Enum Validity | All enum values in locked dictionary | BLOCK record → unmapped enum log |
| VG-06 | Source Record Deduplication | `source_record_id` + `source_system` not already imported | SKIP with deduplicate log entry |
| VG-07 | Tenant Context Binding | Every record has valid `tenant_id` injected | BLOCK import → context error |
| VG-08 | Skill Confidence Gate | Skill signals below 0.75 not written to UserSkill | DISCARD signal → log only |
| VG-09 | PII Encryption Check | PII fields (email, phone, full_name) passed to encryption layer | BLOCK if encryption layer unavailable |
| VG-10 | Audit Log Writability | `FieldMappingDecision` record can be written | STOP ENTIRE JOB → no partial imports |

### 8.2 Human Review Queue Schema

When any validation gate produces a FLAG (not BLOCK), the record enters the Human Review Queue.

```
HumanReviewItem (
  id                UUID PK,
  migration_job_id  UUID FK,
  source_record     JSONB,
  proposed_mapping  JSONB,
  confidence_score  DECIMAL(4,3),
  flag_reason       TEXT,
  gate_id           TEXT,
  status            ENUM('pending','approved','rejected','modified'),
  reviewed_by       UUID FK User (admin only),
  reviewed_at       TIMESTAMP TZ,
  resolution_notes  TEXT
)
```

- Human Review Queue items are non-blocking. Import job proceeds for non-flagged records.
- Flagged records are held until reviewed.
- Approved records proceed to L8.
- Rejected records produce a final `FieldMappingDecision` with outcome = `rejected_by_human`.
- Modified records proceed with human-corrected mapping.

---

## SECTION AIFMM-9 — LAYER 8: IMPORT EXECUTOR & AUDIT TRAIL

### 9.1 Import Transaction Protocol

```
BEGIN TRANSACTION
  ├── Write User record (or update if exists)
  ├── Write UserSkill records (for each extracted skill above threshold)
  ├── Write WorkDataRecord (UWDF format)
  ├── Write IntegrationSyncLog
  └── Write FieldMappingDecision (immutable — no UPDATE permitted)
COMMIT
  └── Emit event → Redis Streams topic: `migration.record.imported`
ROLLBACK on any failure
  └── Emit event → Redis Streams topic: `migration.record.failed`
  └── Write error to MigrationRecord.error_log
```

### 9.2 FieldMappingDecision Schema (Immutable Audit Log)

```
FieldMappingDecision (
  id                    UUID PK,
  migration_job_id      UUID FK,
  source_system         TEXT,
  source_field_path     TEXT,
  source_value_sample   TEXT,
  canonical_field       TEXT,
  canonical_entity      TEXT,
  mapping_method        ENUM('rule','model','human'),
  confidence_score      DECIMAL(4,3),
  model_version         TEXT,
  rule_id               TEXT,
  outcome               ENUM('accepted','review_approved','review_rejected','blocked','discarded'),
  executed_at           TIMESTAMP TZ,
  executed_by_system    TEXT,
  tenant_id             UUID FK
)
```

**Immutability enforcement:** No `UPDATE` or `DELETE` permitted on `FieldMappingDecision`. Append-only. Row-level security enforced. Accessible only to Governance Board admin role.

---

## SECTION AIFMM-10 — PLATFORM-SPECIFIC MAPPING PACKAGES

### 10.1 Business Platform Packages (50 Platforms)

Each platform has a pre-built Mapping Package containing: Source schema manifest · Rule engine entries · Field transformation config · Skill extraction config.

| # | Platform | Category | Mapping Package ID | Status |
|---|---|---|---|---|
| 1 | Workday | HR/ATS | PKG-HR-001 | LOCKED |
| 2 | BambooHR | HR/ATS | PKG-HR-002 | LOCKED |
| 3 | Zoho People | HR/ATS | PKG-HR-003 | LOCKED |
| 4 | Darwinbox | HR/ATS | PKG-HR-004 | LOCKED |
| 5 | Keka HR | HR/ATS | PKG-HR-005 | LOCKED |
| 6 | ADP Workforce | HR/ATS | PKG-HR-006 | LOCKED |
| 7 | SAP SuccessFactors | HR/ATS | PKG-HR-007 | LOCKED |
| 8 | Freshteam | HR/ATS | PKG-HR-008 | LOCKED |
| 9 | Rippling | HR/ATS | PKG-HR-009 | LOCKED |
| 10 | Gusto | HR/ATS | PKG-HR-010 | LOCKED |
| 11 | Salesforce | CRM | PKG-CRM-001 | LOCKED |
| 12 | HubSpot | CRM | PKG-CRM-002 | LOCKED |
| 13 | Zoho CRM | CRM | PKG-CRM-003 | LOCKED |
| 14 | Pipedrive | CRM | PKG-CRM-004 | LOCKED |
| 15 | Freshsales | CRM | PKG-CRM-005 | LOCKED |
| 16 | Insightly | CRM | PKG-CRM-006 | LOCKED |
| 17 | Bitrix24 | CRM | PKG-CRM-007 | LOCKED |
| 18 | Copper CRM | CRM | PKG-CRM-008 | LOCKED |
| 19 | Agile CRM | CRM | PKG-CRM-009 | LOCKED |
| 20 | Close CRM | CRM | PKG-CRM-010 | LOCKED |
| 21 | Jira | Project | PKG-PM-001 | LOCKED |
| 22 | Asana | Project | PKG-PM-002 | LOCKED |
| 23 | Trello | Project | PKG-PM-003 | LOCKED |
| 24 | ClickUp | Project | PKG-PM-004 | LOCKED |
| 25 | Monday.com | Project | PKG-PM-005 | LOCKED |
| 26 | Notion | Project | PKG-PM-006 | LOCKED |
| 27 | Wrike | Project | PKG-PM-007 | LOCKED |
| 28 | Airtable | Project | PKG-PM-008 | LOCKED |
| 29 | Basecamp | Project | PKG-PM-009 | LOCKED |
| 30 | Teamwork | Project | PKG-PM-010 | LOCKED |
| 31 | GitHub | Developer | PKG-DEV-001 | LOCKED |
| 32 | GitLab | Developer | PKG-DEV-002 | LOCKED |
| 33 | Bitbucket | Developer | PKG-DEV-003 | LOCKED |
| 34 | Jenkins | Developer | PKG-DEV-004 | LOCKED |
| 35 | DockerHub | Developer | PKG-DEV-005 | LOCKED |
| 36 | QuickBooks | Accounting | PKG-ACC-001 | LOCKED |
| 37 | Zoho Books | Accounting | PKG-ACC-002 | LOCKED |
| 38 | Tally | Accounting | PKG-ACC-003 | LOCKED |
| 39 | Xero | Accounting | PKG-ACC-004 | LOCKED |
| 40 | Freshbooks | Accounting | PKG-ACC-005 | LOCKED |
| 41 | Slack | Communication | PKG-COM-001 | LOCKED |
| 42 | Microsoft Teams | Communication | PKG-COM-002 | LOCKED |
| 43 | Zoom | Communication | PKG-COM-003 | LOCKED |
| 44 | Discord | Communication | PKG-COM-004 | LOCKED |
| 45 | RocketChat | Communication | PKG-COM-005 | LOCKED |
| 46 | Google Drive | Documents | PKG-DOC-001 | LOCKED |
| 47 | OneDrive | Documents | PKG-DOC-002 | LOCKED |
| 48 | Dropbox | Documents | PKG-DOC-003 | LOCKED |
| 49 | Notion Docs | Documents | PKG-DOC-004 | LOCKED |
| 50 | SharePoint | Documents | PKG-DOC-005 | LOCKED |

### 10.2 EdTech Platform Packages (50 Platforms)

| # | Platform | Category | Mapping Package ID | Status |
|---|---|---|---|---|
| 51 | Moodle | LMS | PKG-LMS-001 | LOCKED |
| 52 | Canvas LMS | LMS | PKG-LMS-002 | LOCKED |
| 53 | Blackboard | LMS | PKG-LMS-003 | LOCKED |
| 54 | TalentLMS | LMS | PKG-LMS-004 | LOCKED |
| 55 | Teachable | LMS | PKG-LMS-005 | LOCKED |
| 56 | Thinkific | LMS | PKG-LMS-006 | LOCKED |
| 57 | LearnDash | LMS | PKG-LMS-007 | LOCKED |
| 58 | Kajabi | LMS | PKG-LMS-008 | LOCKED |
| 59 | Absorb LMS | LMS | PKG-LMS-009 | LOCKED |
| 60 | Docebo | LMS | PKG-LMS-010 | LOCKED |
| 61 | SAP Litmos | LMS | PKG-LMS-011 | LOCKED |
| 62 | iSpring | LMS | PKG-LMS-012 | LOCKED |
| 63 | Schoology | LMS | PKG-LMS-013 | LOCKED |
| 64 | Open edX | LMS | PKG-LMS-014 | LOCKED |
| 65 | Chamilo | LMS | PKG-LMS-015 | LOCKED |
| 66 | Fedena | School ERP | PKG-ERP-001 | LOCKED |
| 67 | Entab | School ERP | PKG-ERP-002 | LOCKED |
| 68 | MyClassCampus | School ERP | PKG-ERP-003 | LOCKED |
| 69 | Schoolknot | School ERP | PKG-ERP-004 | LOCKED |
| 70 | EduSys | School ERP | PKG-ERP-005 | LOCKED |
| 71 | Teachmint | School ERP | PKG-ERP-006 | LOCKED |
| 72 | Classplus | School ERP | PKG-ERP-007 | LOCKED |
| 73 | Entab CampusCare | School ERP | PKG-ERP-008 | LOCKED |
| 74 | Vidyalaya ERP | School ERP | PKG-ERP-009 | LOCKED |
| 75 | SchoolDiary | School ERP | PKG-ERP-010 | LOCKED |
| 76 | Google Classroom | Classroom | PKG-CLS-001 | LOCKED |
| 77 | Microsoft Classroom | Classroom | PKG-CLS-002 | LOCKED |
| 78 | Zoom Classes | Classroom | PKG-CLS-003 | LOCKED |
| 79 | Edmodo | Classroom | PKG-CLS-004 | LOCKED |
| 80 | Nearpod | Classroom | PKG-CLS-005 | LOCKED |
| 81 | Udemy Instructor | Online Course | PKG-OCP-001 | LOCKED |
| 82 | Coursera Instructor | Online Course | PKG-OCP-002 | LOCKED |
| 83 | Skillshare Instructor | Online Course | PKG-OCP-003 | LOCKED |
| 84 | Podia | Online Course | PKG-OCP-004 | LOCKED |
| 85 | LearnWorlds | Online Course | PKG-OCP-005 | LOCKED |
| 86 | Graphy | Coaching | PKG-COA-001 | LOCKED |
| 87 | Wise App | Coaching | PKG-COA-002 | LOCKED |
| 88 | Unacademy Educator | Coaching | PKG-COA-003 | LOCKED |
| 89 | TestGorilla | Assessment | PKG-ASS-001 | LOCKED |
| 90 | HackerRank | Assessment | PKG-ASS-002 | LOCKED |
| 91 | Mettl | Assessment | PKG-ASS-003 | LOCKED |
| 92 | Questionmark | Assessment | PKG-ASS-004 | LOCKED |
| 93 | ExamSoft | Assessment | PKG-ASS-005 | LOCKED |
| 94 | PowerSchool | Student Data | PKG-SDS-001 | LOCKED |
| 95 | Infinite Campus | Student Data | PKG-SDS-002 | LOCKED |
| 96 | Skyward | Student Data | PKG-SDS-003 | LOCKED |
| 97 | Alma SIS | Student Data | PKG-SDS-004 | LOCKED |
| 98 | Gradelink | Student Data | PKG-SDS-005 | LOCKED |
| 99 | Figma | Design | PKG-DSN-001 | LOCKED |
| 100 | Adobe XD | Design | PKG-DSN-002 | LOCKED |

---

## SECTION AIFMM-11 — AI MODEL SPECIFICATION (R12 BINDING)

This section fulfils the R12 AI Model Specification contract from Ecoskiller Master Prompt Section H.

| Parameter | Value |
|---|---|
| Model Name | AIFMM-Semantic-v1 |
| Model Type | Sentence embedding + cosine similarity (bi-encoder architecture) |
| Base Architecture | all-MiniLM-L6-v2 (384-dim) fine-tuned on Ecoskiller field vocabulary |
| Training Data | Labelled field mapping pairs from 100 platforms · Human-corrected review queue outputs |
| Evaluation Metrics | Mapping precision@1, precision@3, human override rate, confidence calibration |
| Retraining Schedule | Quarterly scheduled · Triggered when human override rate > 10% in 30-day window |
| Explainability Format | Top-3 candidate matches with similarity scores logged in FieldMappingDecision |
| Model Registry | Versioned artefact in MinIO object storage · Version tag on every FieldMappingDecision |
| Deployment | Python FastAPI microservice · Containerised · Kubernetes-managed |
| Inference Latency SLA | < 200ms per field mapping call |
| Batch Processing | Bulk migration: batch 1000 records per job · Redis Streams queue |
| Skill NLP Model | spaCy NER + custom skill ontology recogniser |
| Skill Model Retraining | When skill taxonomy version bumps |

---

## SECTION AIFMM-12 — ZERO-API MIGRATION MODE (EXCEL / CSV)

For organisations without API access, AIFMM supports Zero-API migration via exported files.

### Supported File Formats

| Format | Extension | Max File Size | Batch Limit |
|---|---|---|---|
| Excel | `.xlsx`, `.xls` | 50 MB | 100,000 rows |
| CSV | `.csv` | 100 MB | 500,000 rows |
| JSON export | `.json` | 100 MB | 500,000 records |
| SQL dump | `.sql` | 500 MB | No row limit |

### Zero-API Mapping Protocol

```
Step 1: File upload → secure presigned URL (MinIO)
Step 2: Schema Discovery Engine → header row + 50-row sample analysis
Step 3: AI generates field mapping preview (displayed to admin before execution)
Step 4: Admin reviews + approves/modifies in Human Review UI
Step 5: Import Executor runs on approved mapping
Step 6: Full audit trail written to FieldMappingDecision
```

- Admin confirmation required before Zero-API import executes
- No silent import — preview step is mandatory
- Confidence scores displayed in preview UI per field
- Approved mapping saved as reusable template for same organisation

---

## SECTION AIFMM-13 — DUAL-ROLE DETECTION ENGINE

### AI Dual-Role Intelligence

When source data contains a single person fulfilling multiple roles (e.g., a teacher who also coaches, or a manager who also codes), the Dual-Role Detection Engine automatically creates the correct multi-role binding.

**Detection Logic:**

```
IF source entity has:
  - Multiple job_title values  → create multiple Role assignments
  - Teaching + coaching signals → create User with [teacher, mentor] roles
  - Admin + instructor LMS flags → create User with [admin, instructor] roles
  - GitHub activity + Jira PM role → create User with [developer, project_manager] roles

Output:
  User record (single)
  Role[] assignment (multiple)
  UserSkill[] (combined from all roles)
  WorkDataRecord[] (one per role context)
```

- Dual-role detection requires confidence ≥ 0.80 for both roles to be auto-assigned
- Single confirmed role + second role at 0.65–0.79 → human review queue
- All dual-role assignments logged in FieldMappingDecision

---

## SECTION AIFMM-14 — API CONTRACTS (LOCKED)

### Migration Job APIs

```
POST   /migration/start              → Initiate migration job for tenant
GET    /migration/{job_id}/status    → Poll migration job status
GET    /migration/{job_id}/preview   → Get AI mapping preview before execution
POST   /migration/{job_id}/approve   → Admin approves mapping preview
POST   /migration/{job_id}/cancel    → Cancel pending migration
GET    /migration/{job_id}/report    → Full migration report with decision log
GET    /migration/{job_id}/errors    → All blocked/flagged records
POST   /migration/review/{item_id}   → Submit human review decision
```

### Integration Sync APIs

```
POST   /integration/connect          → Initiate OAuth connection for tool
POST   /integration/sync/{conn_id}   → Trigger manual sync
GET    /integration/{conn_id}/health → Sync health status
GET    /integration/{conn_id}/log    → Last N sync decisions
DELETE /integration/{conn_id}        → Disconnect tool
```

### Field Mapping APIs (Admin / Governance)

```
GET    /fieldmapping/decisions       → Query FieldMappingDecision log (read-only)
GET    /fieldmapping/rules           → List locked rule engine entries
GET    /fieldmapping/model/version   → Current AIFMM model version
GET    /fieldmapping/packages        → List all 100 platform mapping packages
```

All APIs require JWT auth. Migration APIs require `admin` or `migration_operator` role. FieldMapping admin APIs require `governance_admin` role. No field mapping rule may be modified via API — version bump required.

---

## SECTION AIFMM-15 — OBSERVABILITY & ALERTING BINDINGS

| Event | Metric ID | Alert Threshold | Severity |
|---|---|---|---|
| Human override rate spike | AIFMM-OVR-01 | > 10% in 30 days | HIGH → trigger model retraining |
| Mapping confidence avg drop | AIFMM-CONF-01 | Avg confidence < 0.80 | HIGH → rule engine review |
| Import failure rate | AIFMM-FAIL-01 | > 2% of records | HIGH → engineering alert |
| Unmapped field rate | AIFMM-UNMAP-01 | > 5% of fields | MEDIUM → taxonomy gap alert |
| Skill extraction confidence drop | AIFMM-SKILL-01 | Avg < 0.70 | MEDIUM → NLP model review |
| Duplicate detection rate | AIFMM-DUP-01 | > 8% of records | MEDIUM → source data quality alert |
| PII encryption failure | AIFMM-PII-01 | Any failure | P1 CRITICAL → stop all imports |
| FieldMappingDecision write failure | AIFMM-AUDIT-01 | Any failure | P1 CRITICAL → stop entire job |
| Migration job latency | AIFMM-LAT-01 | > SLA per org size | MEDIUM → infra scaling alert |

**Migration SLA (Locked):**

| Organisation Size | SLA Target |
|---|---|
| ≤ 50 users | 5 minutes |
| ≤ 500 users | 15 minutes |
| ≤ 5,000 users | 45 minutes |
| ≤ 50,000 users | 2 hours |
| > 50,000 users | Scheduled batch — human-supervised |

---

## SECTION AIFMM-16 — SECURITY & COMPLIANCE BINDINGS

| Requirement | Binding | Enforcement |
|---|---|---|
| PII at rest encryption | All `email`, `phone`, `full_name` fields encrypted | Dojo P1 / Ecoskiller R10 |
| Tenant isolation | Row-level security on all migration tables | Ecoskiller R10 |
| No cross-tenant data access | `tenant_id` mandatory on every record; queries filtered by RLS | Dojo P11 |
| Secret manager only | No plaintext API keys in env files | Ecoskiller R1 / Dojo P1 |
| Audit trail immutability | FieldMappingDecision: no UPDATE/DELETE | Dojo P1 |
| GDPR / data residency | Source data purged post-migration per retention policy | Ecoskiller R15 binding |
| Consent for Slack/Teams text | Consent capture before processing communication tool data | Ecoskiller R15 binding |
| Rate limiting on migration APIs | Per-tenant rate limit: 10 concurrent jobs max | Ecoskiller R10 |
| WAF in front of migration API | Enforced via Kong OSS gateway | Ecoskiller R1 stack |

---

## SECTION AIFMM-17 — FINAL LOCK SEAL

```
╔═══════════════════════════════════════════════════════════════════╗
║      ANTIGRAVITY — ARTIFACT #89                                   ║
║      AI FIELD MAPPING MODEL (AIFMM)                               ║
║      ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE               ║
║                                                                   ║
║  Status:  SEALED · LOCKED · PRODUCTION ACTIVE                     ║
║  Version: v1.0                                                    ║
║                                                                   ║
║  Engine Layers Defined:              8  (L1–L8, all required)     ║
║  Pattern Library Entries:           20  (PTN-01–20)               ║
║  Rule Engine Mappings:              50+ (locked exact mappings)   ║
║  Type Coercion Rules:               13  (all deterministic)       ║
║  Enum Mapping Dictionary Entries:   22  (locked)                  ║
║  Platform Mapping Packages:        100  (50 business + 50 edtech) ║
║  Skill Taxonomy Domains:            12  (SKT-01–12)               ║
║  Validation Gates:                  10  (VG-01–10, all required)  ║
║  API Contracts Declared:            15                            ║
║  Observability Alerts:               9                            ║
║  Migration SLA Tiers:                5                            ║
║                                                                   ║
║  AI Model: AIFMM-Semantic-v1                                      ║
║  Auto-Accept Threshold:  ≥ 0.88                                   ║
║  Human Review Band:      0.65 – 0.87                              ║
║  Auto-Reject Threshold:  < 0.65                                   ║
║                                                                   ║
║  RULE ENGINE PRIORITY:   SUPERSEDES ALL MODEL OUTPUT              ║
║  AUDIT LOG:              IMMUTABLE (APPEND-ONLY)                  ║
║  PII ENCRYPTION:         MANDATORY (P1 BLOCK IF UNAVAILABLE)      ║
║  TENANT ISOLATION:       ROW-LEVEL SECURITY ENFORCED              ║
║  ZERO-API MODE:          SUPPORTED (ADMIN CONFIRM REQUIRED)       ║
║  DUAL-ROLE DETECTION:    ACTIVE                                   ║
║                                                                   ║
║  DOJO TRUST INFRASTRUCTURE:     BOUND                            ║
║  ECOSKILLER DATA MODEL FREEZE:   BOUND                            ║
║  UWDF NORMALIZATION LAYER:       BOUND                            ║
║  EUME MIGRATION ENGINE:          BOUND                            ║
║  EIE INTEGRATION ENGINE:         BOUND                            ║
║                                                                   ║
║  Interpretation Authority: NONE                                   ║
║  Mutation Policy: Add-Only via Version Bump                       ║
║  Architecture Authority: LOCKED                                   ║
║  Governance Board Approval: Required for any model update         ║
╚═══════════════════════════════════════════════════════════════════╝
```

---

*End of ANTIGRAVITY Artifact #89 — AI Field Mapping Model v1.0 — SEALED & LOCKED*
*All migration, integration, and skill extraction pipelines must bind to this model as single source of truth.*
*No mapping rule, confidence threshold, type coercion logic, or skill taxonomy entry may be changed without Governance Board approval and version bump.*
*STOP EXECUTION if any declared engine layer, validation gate, or audit log write is absent.*
