# 🔒 DATA NORMALIZATION ENGINE (DNE)
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY LAYER
### ECOSKILLER MASTER SYSTEM — SEALED PRODUCTION ARTIFACT v1.0

```
Artifact Class:       Production System Blueprint
Module Class:         Enterprise Optimization + Trust Infrastructure
Engine Name:          Data Normalization Engine (DNE)
Mutation Policy:      Add-only via version bump
Execution Mode:       Deterministic
Stack Lock:           Enforced
Seal Status:          LOCKED & IMMUTABLE
Authority:            Human Declaration Only
Interpretation:       FORBIDDEN
```

---

# ⚠️ SEAL DECLARATION

This document is a **LOCKED PRODUCTION ARTIFACT** for the **Data Normalization Engine (DNE)** operating within the **AntiGravity Enterprise Optimization + Trust Infrastructure** layer of **EcoSkiller**.

No component, rule, schema, algorithm, or interface defined herein may be reinterpreted, simplified, skipped, or substituted without a formal **version bump** and **human-declared override**.

**Violation → STOP EXECUTION → REPORT → NO SYSTEM COMPLETION CLAIM PERMITTED**

---

# 🔒 SECTION DNE-A — SYSTEM IDENTITY & MISSION

## Engine Name
```
Data Normalization Engine (DNE)
Namespace:   ecoskiller.enterprise.dne
Layer:       AntiGravity — Enterprise Optimization + Trust Infrastructure
Version:     v1.0-SEALED
```

## Mission Statement

The Data Normalization Engine is the **universal truth layer** of EcoSkiller.

Every external tool, integration source, migration pipeline, and internal module produces data in incompatible formats, schemas, and semantics. The DNE exists to:

1. **Ingest** raw heterogeneous data from 100+ integrated business tools (HR/ATS, CRM, DevTools, EdTech, Communication, Project Management, Design, etc.)
2. **Normalize** all data into the **Universal Work Data Format (UWDF)** — EcoSkiller's canonical data model
3. **Extract** verified Skill Signals, Performance Signals, Reliability Signals, and Trust Signals from normalized data
4. **Validate** all normalized records against integrity, trust, and fraud rules
5. **Feed** downstream engines: Skill Engine (Dojo), Scoring Engine, Rating Engine, Belt Engine, AI Matching Engine, Reputation Engine, and Trust Engine
6. **Guarantee** that every skill credential, belt award, assessment score, and certification is **traceable to real verified work data**

The DNE is the **anti-gravity force** — it removes the weight of manual data entry, fake profiles, and unverified claims from EcoSkiller's talent ecosystem.

---

# 🔒 SECTION DNE-B — ARCHITECTURAL POSITION

## Position in EcoSkiller Architecture

```
External Tools (100+)
       ↓
[Integration Hub — Ecoskiller Integration Engine (EIE)]
       ↓
[DATA NORMALIZATION ENGINE (DNE)] ← THIS MODULE
       ↓
[Universal Work Data Format (UWDF) Store]
       ↓
┌──────────────────────────────────────────────┐
│  Skill Engine  │  Scoring Engine  │  Belt Engine │
│  Rating Engine │  Trust Engine    │  AI Matcher  │
│  Reputation    │  Analytics       │  Marketplace │
└──────────────────────────────────────────────┘
```

## DNE Is Mandatory Infrastructure

No skill verification, performance record, or belt promotion may be processed without the originating raw data passing through the DNE pipeline first.

Bypass → **STOP EXECUTION**

---

# 🔒 SECTION DNE-C — UNIVERSAL WORK DATA FORMAT (UWDF)

## UWDF Is the Canonical Output Schema

All DNE processing must produce records conforming to UWDF. This is the lingua franca of the EcoSkiller AntiGravity layer.

### UWDF Core Record Schema

```json
{
  "uwdf_record_id": "uuid-v4",
  "source_tool": "github | jira | salesforce | slack | ...",
  "source_tool_category": "developer | project | crm | communication | ...",
  "raw_event_ref": "external_record_id",
  "user_ecoskiller_id": "uuid-v4",
  "tenant_id": "uuid-v4",
  "institution_id": "uuid-v4 | null",
  "event_timestamp": "ISO8601",
  "normalization_timestamp": "ISO8601",
  "normalization_version": "1.0",
  "uwdf_record_type": "work_event | performance_snapshot | skill_signal | reliability_signal",
  "skill_signals": [
    {
      "skill_id": "uuid-v4",
      "skill_name": "string",
      "skill_category": "string",
      "signal_strength": 0.0–1.0,
      "evidence_type": "direct | inferred | corroborated",
      "evidence_description": "string",
      "confidence_score": 0.0–1.0
    }
  ],
  "performance_signals": {
    "productivity_score": 0.0–1.0,
    "quality_score": 0.0–1.0,
    "reliability_score": 0.0–1.0,
    "collaboration_score": 0.0–1.0,
    "initiative_score": 0.0–1.0
  },
  "trust_metadata": {
    "source_verified": true | false,
    "anti_tamper_hash": "sha256",
    "fraud_flag": true | false,
    "fraud_reason": "string | null",
    "normalization_confidence": 0.0–1.0
  },
  "dojo_linkage": {
    "applicable_skill_tracks": ["skill_track_id"],
    "belt_eligibility_signal": true | false,
    "scenario_match_hints": ["scenario_id"]
  }
}
```

### UWDF Schema Is Frozen

UWDF fields may be extended (new optional fields added). Existing field names, types, and semantics may not be mutated. Extension requires version bump.

---

# 🔒 SECTION DNE-D — DNE PROCESSING PIPELINE

## Pipeline Architecture

The DNE must implement the following **seven-stage pipeline** in exact order. No stage may be skipped.

```
Stage 1 → Raw Ingest & Schema Detection
Stage 2 → Tool-Specific Extractor
Stage 3 → Field Normalization & Mapping
Stage 4 → AI Semantic Enrichment
Stage 5 → Skill Signal Extraction
Stage 6 → Fraud & Trust Validation
Stage 7 → UWDF Emission & Downstream Dispatch
```

---

## Stage 1 — Raw Ingest & Schema Detection

### Responsibilities
- Accept raw payloads from the Ecoskiller Integration Engine (EIE) via event bus
- Detect source tool identity from metadata headers
- Validate payload structure matches registered tool schema (from Tool Schema Registry)
- Assign a unique `ingest_id` and record `ingest_timestamp`
- Route to the correct Tool-Specific Extractor (Stage 2)

### Failure Rules
- Unknown tool → **QUARANTINE** → alert ops team → no processing
- Schema mismatch → **QUARANTINE** → log schema_drift event → no processing
- Missing auth context (tenant/user) → **REJECT** → log security event

### Required Database Record
```sql
CREATE TABLE dne_raw_ingest_log (
  ingest_id         UUID PRIMARY KEY,
  tenant_id         UUID NOT NULL,
  source_tool       VARCHAR NOT NULL,
  source_tool_version VARCHAR,
  payload_hash      VARCHAR NOT NULL,
  payload_size_bytes INTEGER,
  schema_valid      BOOLEAN NOT NULL,
  quarantine_reason TEXT,
  ingest_timestamp  TIMESTAMPTZ NOT NULL
);
```

---

## Stage 2 — Tool-Specific Extractor

### Responsibilities
- Apply the registered **Extractor Module** for the detected source tool
- Extract raw fields into a **Pre-Normalized Intermediate Record (PNIR)**
- Handle tool-specific quirks (nested objects, pagination artifacts, encoding differences)
- Preserve original field names alongside extracted values for audit trail

### Extractor Module Registry

Each integrated tool must have a registered, versioned Extractor Module. No tool may be processed without a registered extractor.

```
Tool Category         Example Tools                  Extractor Type
─────────────────────────────────────────────────────────────────────
HR / ATS              Workday, BambooHR, Greenhouse   HR-Extractor-v1
Project Management    Jira, Asana, ClickUp, Monday    PM-Extractor-v1
Developer Tools       GitHub, GitLab, Bitbucket        Dev-Extractor-v1
CRM                   Salesforce, HubSpot, Zoho CRM   CRM-Extractor-v1
Communication         Slack, MS Teams, Discord         Comms-Extractor-v1
Design                Figma, Adobe XD, Canva           Design-Extractor-v1
EdTech / LMS          Moodle, Google Classroom, LTI   Edu-Extractor-v1
Interview Tools       HackerRank, Codility, HireVue   Interview-Extractor-v1
Cloud / DevOps        AWS, GCP, Azure, Docker Hub     Cloud-Extractor-v1
Accounting            QuickBooks, Xero, Zoho Books    Finance-Extractor-v1
```

### Mandatory Extractor Outputs (PNIR)
```json
{
  "pnir_id": "uuid-v4",
  "ingest_id": "ref:dne_raw_ingest_log",
  "tool_name": "string",
  "tool_version": "string",
  "user_external_id": "string",
  "user_ecoskiller_id": "uuid-v4",
  "event_type": "string",
  "event_timestamp": "ISO8601",
  "raw_fields": { "...original key/value pairs..." },
  "extracted_fields": { "...structured intermediate fields..." },
  "extraction_confidence": 0.0–1.0,
  "extraction_warnings": ["string"]
}
```

---

## Stage 3 — Field Normalization & Mapping

### Responsibilities
- Map PNIR extracted fields to **UWDF canonical field names**
- Apply the **AI Field Mapper** for ambiguous or novel field structures
- Normalize data types: dates → ISO8601, scores → 0.0–1.0, strings → UTF-8 clean
- Resolve user identity: map `user_external_id` to `user_ecoskiller_id` via Identity Resolver
- Resolve tenant context: map external organization ID to EcoSkiller `tenant_id`

### AI Field Mapper Rules

The AI Field Mapper operates on PNIR records where field-to-UWDF mapping is ambiguous.

Rules:
- Field mapper must use a **deterministic mapping model** (frozen version per DNE release)
- All AI mapping decisions must be logged with confidence score
- Confidence < 0.75 → flag for human review queue → do NOT block pipeline (use best-guess mapping with `mapping_uncertain = true` flag)
- Mapping model version must be recorded on each UWDF record

### Required Mapping Log
```sql
CREATE TABLE dne_field_mapping_log (
  mapping_id          UUID PRIMARY KEY,
  pnir_id             UUID NOT NULL,
  source_field_name   VARCHAR NOT NULL,
  target_uwdf_field   VARCHAR NOT NULL,
  mapping_method      VARCHAR NOT NULL, -- 'direct' | 'ai_mapped' | 'rule_based'
  mapping_confidence  DECIMAL(4,3),
  mapping_uncertain   BOOLEAN DEFAULT FALSE,
  mapper_version      VARCHAR NOT NULL,
  created_at          TIMESTAMPTZ NOT NULL
);
```

---

## Stage 4 — AI Semantic Enrichment

### Responsibilities
- Apply NLP and contextual AI models to enriched semantic meaning from raw text fields (commit messages, ticket descriptions, Slack messages, CRM notes, etc.)
- Produce **semantic tags** describing work content, skills demonstrated, and behavioral signals
- Score the **quality** and **complexity** of observed work artifacts
- Detect language, domain, and technical depth

### Enrichment Outputs Per Tool Category

| Source Tool | Semantic Enrichment Targets |
|---|---|
| GitHub | Code complexity, bug rate, commit quality, PR collaboration, code review behavior |
| Jira | Task complexity, delivery reliability, sprint completion, scope management |
| Salesforce | Sales performance, pipeline management, customer relationship quality |
| Slack | Communication clarity, collaboration frequency, response reliability |
| Figma | Design iteration depth, component reuse, design system adherence |
| HackerRank | Problem-solving speed, algorithm complexity, correctness rate |
| Moodle | Course completion, quiz performance, engagement consistency |
| Google Classroom | Assignment submission reliability, grade trajectory, participation |

### Enrichment Model Governance

- AI enrichment models must be **versioned and frozen per DNE release**
- Model updates require DNE version bump and re-normalization audit
- Enrichment outputs must carry `model_version` and `enrichment_timestamp`
- Enrichment is **advisory** — it feeds skill extraction but does not override explicit evidence

---

## Stage 5 — Skill Signal Extraction

### Responsibilities
- Translate enriched PNIR + semantic tags into **typed Skill Signals**
- Map skill signals to EcoSkiller's **Skill Taxonomy** (maintained in Skill Registry)
- Assign signal strength (0.0–1.0) based on evidence type and frequency
- Assign evidence classification: `direct | inferred | corroborated`
- Feed extracted skill signals into the **Dojo Skill Engine** for belt eligibility updates

### Skill Signal Classification Rules

```
Evidence Type     Definition
──────────────────────────────────────────────────────────────────────
direct            Tool output directly proves skill (e.g., merged PR with passing CI → coding skill confirmed)
inferred          AI infers skill from behavioral pattern (e.g., Jira ticket closed repeatedly on-time → project management inferred)
corroborated      Signal confirmed by multiple independent sources (e.g., GitHub + Jira + peer review all confirm same skill)
```

### Signal Strength Rules

```
Evidence Type     Base Signal Strength     Booster Conditions
──────────────────────────────────────────────────────────────────────
direct            0.70–0.90                Multiple occurrences → up to 0.95
inferred          0.40–0.70                Corroborated by second tool → +0.15
corroborated      0.80–1.00                Three+ sources → cap at 0.98
```

Signal strength > 0.80 from verified real-work sources may contribute to Belt eligibility.
Signal strength < 0.50 is advisory only — does not feed Belt Engine directly.

### Skill Taxonomy Lock

The Skill Taxonomy is maintained in the **EcoSkiller Skill Registry**. DNE Skill Signal Extraction must map to registered skills only.

Unmapped skill signals are:
- Logged to `dne_unmapped_skill_queue` for taxonomy review
- NOT discarded — held pending taxonomy expansion
- NOT fed to Belt Engine until mapped

```sql
CREATE TABLE dne_skill_signal (
  signal_id             UUID PRIMARY KEY,
  uwdf_record_id        UUID NOT NULL,
  user_ecoskiller_id    UUID NOT NULL,
  skill_id              UUID REFERENCES skill_registry(skill_id),
  skill_name_raw        VARCHAR NOT NULL,
  evidence_type         VARCHAR NOT NULL, -- direct | inferred | corroborated
  signal_strength       DECIMAL(4,3) NOT NULL,
  confidence_score      DECIMAL(4,3) NOT NULL,
  contributing_sources  JSONB, -- array of tool names
  belt_eligible         BOOLEAN DEFAULT FALSE,
  created_at            TIMESTAMPTZ NOT NULL
);
```

---

## Stage 6 — Fraud & Trust Validation

### Responsibilities
- Apply all Anti-Fraud rules to the fully enriched UWDF candidate record
- Assign a **Trust Score** to the record
- Flag suspicious records for human audit queue
- Block flagged records from feeding Belt Engine or Marketplace

### Mandatory Fraud Detection Rules

All rules below are **non-negotiable**. Violation of any rule → flag record immediately.

#### Rule F-01 — Velocity Fraud Detection
```
IF number of high-signal records from same user in same 24h window > threshold_velocity
THEN fraud_flag = TRUE, reason = 'velocity_anomaly'
```

#### Rule F-02 — Tool Authenticity Verification
```
IF tool integration OAuth token age < 24 hours AND record volume > threshold_new_account
THEN fraud_flag = TRUE, reason = 'new_token_volume_spike'
```

#### Rule F-03 — Cross-Source Contradiction Detection
```
IF skill_signal from Tool A claims mastery-level evidence
AND Tool B (same skill domain) shows zero activity for same user in same period
THEN confidence_score -= 0.30, flag for human review
```

#### Rule F-04 — Peer Collusion Detection (Dojo Integration)
```
IF two users mutually award each other high scores in Dojo matches
AND both share same external tool workspace
AND skill signals from shared workspace are anomalously high
THEN fraud_flag = TRUE for both records, reason = 'peer_collusion_signal'
```

#### Rule F-05 — Identity Spoofing Detection
```
IF user_ecoskiller_id resolves to multiple external tool accounts with conflicting identity signals
THEN quarantine record, reason = 'identity_conflict'
```

#### Rule F-06 — Schema Injection Guard
```
IF any field value contains executable code patterns, SQL injection markers, or schema escape sequences
THEN REJECT record, log security_event = 'injection_attempt'
```

#### Rule F-07 — Tenant Isolation Validation
```
IF record's resolved tenant_id does not match the authorized tenant scope of the integration token
THEN REJECT record, log security_event = 'cross_tenant_attempt'
```

### Trust Score Computation

```
Trust Score = base_score
  + (source_verified ? +0.20 : 0)
  + (corroborated_by_2_sources ? +0.15 : 0)
  + (user_identity_verified ? +0.10 : 0)
  - (fraud_flag ? -1.00 : 0)
  - (mapping_uncertain ? -0.10 : 0)
  - (cross_source_contradiction ? -0.30 : 0)

clamped to range [0.0, 1.0]
```

Trust Score thresholds:
```
≥ 0.80 → Trusted — feeds Belt Engine, Marketplace, AI Matcher
0.60–0.79 → Provisional — feeds Analytics only, not Belt Engine
0.40–0.59 → Under Review — human review queue
< 0.40 → Blocked — does not feed any downstream engine
```

```sql
CREATE TABLE dne_trust_log (
  trust_log_id          UUID PRIMARY KEY,
  uwdf_record_id        UUID NOT NULL,
  trust_score           DECIMAL(4,3) NOT NULL,
  fraud_flag            BOOLEAN NOT NULL DEFAULT FALSE,
  fraud_reason          TEXT,
  fraud_rules_triggered JSONB, -- array of rule codes
  human_review_required BOOLEAN DEFAULT FALSE,
  reviewed_by           UUID,
  reviewed_at           TIMESTAMPTZ,
  disposition           VARCHAR, -- 'approved' | 'rejected' | 'pending'
  created_at            TIMESTAMPTZ NOT NULL
);
```

---

## Stage 7 — UWDF Emission & Downstream Dispatch

### Responsibilities
- Persist the fully validated UWDF record to the **UWDF Store**
- Publish downstream events via **EcoSkiller Event Bus** to registered consumers
- Log the full pipeline trace for auditability

### UWDF Store

```sql
CREATE TABLE uwdf_records (
  uwdf_record_id            UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  user_ecoskiller_id        UUID NOT NULL,
  source_tool               VARCHAR NOT NULL,
  source_tool_category      VARCHAR NOT NULL,
  raw_event_ref             VARCHAR,
  event_timestamp           TIMESTAMPTZ NOT NULL,
  normalization_timestamp   TIMESTAMPTZ NOT NULL,
  normalization_version     VARCHAR NOT NULL DEFAULT '1.0',
  uwdf_record_type          VARCHAR NOT NULL,
  skill_signals             JSONB NOT NULL DEFAULT '[]',
  performance_signals       JSONB NOT NULL DEFAULT '{}',
  trust_metadata            JSONB NOT NULL,
  dojo_linkage              JSONB,
  pipeline_trace_id         UUID NOT NULL,
  anti_tamper_hash          VARCHAR NOT NULL,
  created_at                TIMESTAMPTZ NOT NULL
);
```

### Anti-Tamper Hash

Every UWDF record must include a SHA-256 hash computed over the full record payload at emission time. Any post-emission mutation of a UWDF record invalidates the hash and must be detected by integrity checks.

```
anti_tamper_hash = SHA256(
  uwdf_record_id +
  user_ecoskiller_id +
  source_tool +
  event_timestamp +
  skill_signals_json +
  trust_score +
  normalization_version
)
```

### Downstream Event Dispatch Map

```
UWDF Record Emitted
      ↓
Event Type: uwdf.record.emitted
      ↓
Consumers:
  → Skill Engine          (if skill_signals present AND trust_score ≥ 0.80)
  → Belt Engine           (if belt_eligible = true AND trust_score ≥ 0.80)
  → Analytics Engine      (always — all trust levels)
  → AI Matcher            (if trust_score ≥ 0.60)
  → Reputation Engine     (if trust_score ≥ 0.60)
  → Talent Marketplace    (if trust_score ≥ 0.80 AND user opted-in)
  → Fraud Review Queue    (if fraud_flag = true)
  → Human Review Queue    (if trust_score < 0.60 OR mapping_uncertain = true)
```

All dispatch is **event-driven**. No synchronous cross-engine calls permitted.

---

# 🔒 SECTION DNE-E — TOOL INTEGRATION SCHEMA REGISTRY

## Purpose

The Tool Schema Registry is the **authoritative source** of truth for every tool's raw data schema supported by the DNE. No tool may be processed without a registered schema.

## Registry Requirements

```sql
CREATE TABLE dne_tool_schema_registry (
  registry_id         UUID PRIMARY KEY,
  tool_name           VARCHAR NOT NULL UNIQUE,
  tool_category       VARCHAR NOT NULL,
  schema_version      VARCHAR NOT NULL,
  schema_definition   JSONB NOT NULL,
  extractor_module    VARCHAR NOT NULL,
  extractor_version   VARCHAR NOT NULL,
  auth_protocol       VARCHAR NOT NULL, -- oauth2 | api_key | webhook
  active              BOOLEAN NOT NULL DEFAULT TRUE,
  created_at          TIMESTAMPTZ NOT NULL,
  updated_at          TIMESTAMPTZ NOT NULL
);
```

## Registered Tool Categories (Initial Set — 100 Tools)

| Category | Tools | Count |
|---|---|---|
| HR / ATS | Workday, BambooHR, Zoho People, SAP SuccessFactors, ADP, Greenhouse, Lever, iCIMS, SmartRecruiters, Freshteam, Darwinbox, Keka HR, Rippling, Gusto, Paychex | 15 |
| Communication | Slack, MS Teams, Zoom, Google Meet, Discord, Telegram, WhatsApp Business, Skype, Webex, RocketChat | 10 |
| Project Management | Jira, Asana, Trello, ClickUp, Monday.com, Notion, Wrike, Basecamp, Teamwork, Airtable | 10 |
| CRM | Salesforce, HubSpot, Zoho CRM, Pipedrive, Freshsales, Close CRM, Insightly, Agile CRM, Copper CRM, Bitrix24 | 10 |
| Developer Tools | GitHub, GitLab, Bitbucket, Jenkins, DockerHub, Kubernetes, Vercel, Netlify, Postman, Sentry | 10 |
| Design | Figma, Adobe XD, Photoshop Cloud, Canva, Sketch | 5 |
| Cloud Platforms | AWS, Google Cloud, Azure, DigitalOcean, Cloudflare | 5 |
| Accounting | QuickBooks, Xero, Zoho Books, Freshbooks, Tally, Wave, Sage, Kashoo, FreeAgent, Busy | 10 |
| Marketing | Google Analytics, Google Ads, Facebook Ads, LinkedIn Ads, Mailchimp, ActiveCampaign, SEMrush, Ahrefs, HubSpot Marketing, Hootsuite | 10 |
| Sales | Outreach, Apollo, Salesloft, Lemlist, Reply.io | 5 |
| Document | Google Docs, Google Sheets, Notion Docs, Dropbox, OneDrive | 5 |
| Interview | Zoom Interview, HireVue, HackerRank, Codility, TestGorilla | 5 |
| **Total** | | **100** |

---

# 🔒 SECTION DNE-F — DATA GOVERNANCE & AUDIT INFRASTRUCTURE

## Immutable Audit Log

All DNE pipeline events must be written to an **immutable audit log**. Retroactive modification is forbidden. Deletion is forbidden except via approved legal data deletion workflows with full audit trail.

```sql
CREATE TABLE dne_pipeline_audit_log (
  audit_id              UUID PRIMARY KEY,
  pipeline_trace_id     UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  user_ecoskiller_id    UUID,
  stage_name            VARCHAR NOT NULL,
  stage_input_hash      VARCHAR NOT NULL,
  stage_output_hash     VARCHAR NOT NULL,
  stage_status          VARCHAR NOT NULL, -- success | quarantine | rejected | flagged
  error_code            VARCHAR,
  error_detail          TEXT,
  processing_duration_ms INTEGER,
  created_at            TIMESTAMPTZ NOT NULL
);
```

## Data Retention Policy

```
UWDF Records:          7 years (enterprise compliance)
Raw Ingest Logs:       90 days (after which PII is masked, record shell retained)
Skill Signal Records:  Lifetime of user account
Trust Logs:            7 years
Fraud Flags:           7 years
Pipeline Audit Logs:   Immutable — never deleted
```

## PII Handling

- Raw payload fields containing PII (names, emails, phone numbers) must be **encrypted at rest** using AES-256
- PII fields in UWDF records are stored encrypted with per-tenant key derivation
- UWDF analytical outputs strip PII — only `user_ecoskiller_id` (pseudonymous) passes to analytics

## Row-Level Security

UWDF records are subject to **row-level security** enforced at the database layer:

```sql
-- Tenant isolation: no cross-tenant data access
ALTER TABLE uwdf_records ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON uwdf_records
  USING (tenant_id = current_setting('app.current_tenant_id')::uuid);
```

---

# 🔒 SECTION DNE-G — API CONTRACT REGISTRY

## DNE Service APIs

All APIs are versioned, authenticated via JWT, and enforced via RBAC.

### Ingest API

```
POST /api/v1/dne/ingest
  Auth: Integration Service Token (internal)
  Body: { tool_name, payload, auth_context }
  Response: { ingest_id, status: 'queued' | 'quarantined' }
```

### UWDF Query API

```
GET /api/v1/dne/uwdf/{user_ecoskiller_id}
  Auth: JWT + RBAC (skill_engine, belt_engine, analytics roles)
  Query Params: trust_min, skill_id, date_from, date_to
  Response: { uwdf_records: [...], total: int }

GET /api/v1/dne/skill-signals/{user_ecoskiller_id}
  Auth: JWT + RBAC
  Response: { skill_signals: [...] }

GET /api/v1/dne/trust-score/{user_ecoskiller_id}
  Auth: JWT + RBAC (marketplace, ai_matcher roles)
  Response: { composite_trust_score: float, signal_count: int, last_updated: ISO8601 }
```

### Admin & Audit APIs

```
GET /api/v1/dne/audit/{pipeline_trace_id}
  Auth: JWT + Admin role
  Response: { full pipeline trace }

GET /api/v1/dne/fraud-queue
  Auth: JWT + Trust Officer role
  Response: { flagged records pending review }

POST /api/v1/dne/fraud-review/{trust_log_id}
  Auth: JWT + Trust Officer role
  Body: { disposition: 'approved' | 'rejected', reviewer_notes: string }
  Response: { updated trust_log record }
```

---

# 🔒 SECTION DNE-H — DNE INTEGRATION WITH DOJO SYSTEM

## Dojo ↔ DNE Bidirectional Trust Loop

The DNE feeds the Dojo Skill Engine. The Dojo Skill Engine feeds back evidence signals to the DNE for cross-validation. This creates a **closed verification loop** that makes EcoSkiller's skill credentials uniquely verifiable.

```
Real Work Tool → DNE → Skill Signal → Dojo Belt Engine
                                           ↓
                               Belt Promotion Requires:
                               Match Count (Dojo) +
                               Score Threshold (Dojo Scoring Engine) +
                               Pressure Scenario Pass +
                               Mentor Certification +
                               ✅ UWDF Skill Signal Corroboration (DNE) ← NEW
```

### Corroboration Rule

For **Advanced Belt tiers** (Silver and above), at least one **direct or corroborated** UWDF skill signal with trust score ≥ 0.80 must exist from a verified real-work tool for the same skill domain before belt promotion is authorized.

This rule is **immutable**. No override without formal version bump.

### Dojo Anomaly → DNE Fraud Trigger

If the Dojo's **Collusion & Manipulation Detection Engine** (Section T9) flags a match pair, the DNE must automatically:
1. Re-audit the UWDF skill signals contributed by both flagged users
2. Apply fraud rules F-04 (peer collusion) retroactively
3. Place affected UWDF records in fraud review queue
4. Notify the Trust Engine and Belt Engine to hold any pending promotions

---

# 🔒 SECTION DNE-I — DNE INTEGRATION WITH TRUST SYSTEM

## Trust Infrastructure Layer Position

The DNE is the **data source** for EcoSkiller's Trust System (Module 15). The Trust System does not generate trust — it **reads** the DNE's trust scores and signals.

### Trust Signals Exported to Trust Engine

```
From DNE                          →  Trust Engine Metric
────────────────────────────────────────────────────────────
UWDF records count (verified)     →  Verified Activity Score
Skill signal count (direct)       →  Verified Skill Count
Trust score composite             →  User Trust Score
Fraud flags (none)                →  Clean Record Indicator
Cross-tool corroboration count    →  Multi-Source Verification Depth
```

### Marketplace Trust Seal

For the **Talent Marketplace** (Section T14 of Dojo spec), a user's talent profile may display a **Trust Seal** only if:

```
Composite DNE Trust Score ≥ 0.80
AND Verified Skill Signals ≥ 3 (direct evidence type)
AND No Active Fraud Flags
AND Identity Verified (Section DNE-J)
```

The Trust Seal must reference the `normalization_version` and `seal_generated_at` timestamp so employers can verify the certification is current.

---

# 🔒 SECTION DNE-J — IDENTITY RESOLUTION ENGINE

## Purpose

The Identity Resolution Engine (IRE) is a sub-module of the DNE responsible for mapping external tool user identities to EcoSkiller Universal IDs.

## IRE Rules

```
Rule IR-01: OAuth-based integrations → resolve via email match + tenant scope
Rule IR-02: API key integrations → resolve via registered employer domain + employee ID
Rule IR-03: Webhook integrations → resolve via registered webhook secret + user mapping
Rule IR-04: If external identity cannot be resolved → record is QUARANTINED
Rule IR-05: Multi-account detection → if same person maps to 2+ EcoSkiller IDs → escalate to ops
Rule IR-06: All identity resolutions are logged with resolution method and confidence
```

```sql
CREATE TABLE dne_identity_resolution_log (
  resolution_id         UUID PRIMARY KEY,
  external_user_id      VARCHAR NOT NULL,
  external_tool         VARCHAR NOT NULL,
  tenant_id             UUID NOT NULL,
  resolved_ecoskiller_id UUID,
  resolution_method     VARCHAR NOT NULL,
  resolution_confidence DECIMAL(4,3),
  resolution_status     VARCHAR NOT NULL, -- resolved | quarantined | escalated
  created_at            TIMESTAMPTZ NOT NULL
);
```

---

# 🔒 SECTION DNE-K — OBSERVABILITY & ALERTING

## Required Telemetry

```
Metric                           Threshold      Alert Level
────────────────────────────────────────────────────────────────────
Pipeline failure rate            > 1%           WARNING
Pipeline failure rate            > 5%           CRITICAL
Quarantine queue depth           > 500 records  WARNING
Fraud flag rate                  > 10%          WARNING
Fraud flag rate                  > 25%          CRITICAL
AI mapping uncertainty rate      > 20%          WARNING
Trust score < 0.40 rate          > 15%          WARNING
Identity resolution failure rate > 5%           WARNING
Stage processing latency p99     > 5s           WARNING
```

## Required Dashboards

```
Dashboard 1 — Pipeline Health
  · Ingest volume (per tool, per hour)
  · Stage success/failure rates
  · Quarantine queue depth
  · Processing latency percentiles

Dashboard 2 — Trust & Fraud Monitor
  · Fraud flag rate over time
  · Trust score distribution
  · Active fraud review queue
  · Resolved vs pending fraud cases

Dashboard 3 — Skill Signal Quality
  · Signal volume by type (direct/inferred/corroborated)
  · Average signal strength by tool category
  · Belt eligibility conversion rate (signals → promotions)
  · Unmapped skill queue depth

Dashboard 4 — Integration Health
  · Tool connection status (all 100 tools)
  · OAuth token expiry warnings
  · Schema drift alerts
  · Integration error rates per tool
```

---

# 🔒 SECTION DNE-L — ENTERPRISE READINESS CONTROLS

## Multi-Tenant Isolation

All DNE operations must enforce **strict tenant isolation**:
- Row-level security on all DNE tables
- No cross-tenant query permitted at any layer
- Tenant-scoped encryption keys for PII fields
- Tenant admin can view only their tenant's DNE pipeline logs

## Enterprise SLA Targets

```
Pipeline Processing SLA:    p95 < 2 seconds per record
Fraud Review SLA:           < 48 hours for human review queue resolution
Data Availability:          99.9% UWDF store uptime
Backup RPO:                 < 1 hour
Backup RTO:                 < 4 hours
```

## HRIS Export Compatibility

UWDF records must be exportable in:
- JSON (canonical UWDF format)
- CSV (flattened skill signal summary)
- PDF (Skill Proof Report — human-readable, suitable for employer presentation)

## LMS / LTI Integration

UWDF normalization must extend to LTI-compatible EdTech sources to support institutional EcoSkiller deployments.

---

# 🔒 SECTION DNE-M — VERSION GOVERNANCE

## Version Lock Rules

```
Change Type                               Allowed Without Version Bump?
──────────────────────────────────────────────────────────────────────────
Add new tool extractor module             YES (add-only)
Add new UWDF optional field               YES (add-only)
Add new skill signal source               YES (add-only)
Add new fraud detection rule              YES (add-only)
Change existing fraud rule logic          NO — requires version bump
Change UWDF field name or type            NO — requires version bump
Change trust score formula                NO — requires version bump
Change belt eligibility threshold         NO — requires version bump
Change AI field mapper model              NO — requires version bump + audit
Remove any UWDF field                     FORBIDDEN
Remove any fraud rule                     FORBIDDEN
```

## Re-Normalization Policy

When a version bump changes normalization logic:
- Historical UWDF records are NOT automatically re-normalized
- New normalization version applies to all new ingest only
- If re-normalization is required (e.g., fraud rule improvement), it must be run as a **supervised batch job** with human approval before any Belt Engine reads the re-normalized data
- Re-normalization jobs must produce a **comparison diff report** before being applied

---

# 🔒 SECTION DNE-N — MASTER PROMPT INSERT BLOCK

Paste this block into the EcoSkiller Master Execution Prompt alongside the Dojo Trust Seal:

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
BEGIN LOCKED ANTIGRAVITY DNE ARTIFACT — v1.0

Module:                Data Normalization Engine (DNE)
Layer:                 Enterprise Optimization + Trust Infrastructure
Namespace:             ecoskiller.enterprise.dne
Version:               1.0-SEALED

Pipeline Stages:       7 — ALL MANDATORY — NONE SKIPPABLE
UWDF Schema:           FROZEN — EXTEND ONLY
Tool Registry:         100 Tools — REGISTERED EXTRACTORS REQUIRED
Skill Signal Types:    direct | inferred | corroborated
Trust Score Thresholds: 0.80 Belt/Marketplace | 0.60 AI/Analytics | <0.40 BLOCKED
Fraud Rules:           F-01 through F-07 — ALL ENFORCED
Anti-Tamper:           SHA-256 hash on every UWDF record
Tenant Isolation:      Row-level security ENFORCED
PII:                   AES-256 encrypted at rest
Immutable Audit:       ALL pipeline events logged — NO DELETION
Dojo Integration:      Corroboration loop ACTIVE for Advanced Belts
Trust Seal:            Requires DNE Trust Score ≥ 0.80 + 3 direct signals
Belt Bypass:           FORBIDDEN — DNE corroboration required for Silver+
Interpretation Authority: NONE
Architecture Authority:  LOCKED

END LOCKED ANTIGRAVITY DNE ARTIFACT
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

# 🔒 SECTION DNE-O — FINAL ENFORCEMENT SEAL

```
DATA NORMALIZATION ENGINE — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
ANTIGRAVITY LAYER — ECOSKILLER PRODUCTION SYSTEM

STATUS: LOCKED · SEALED · DETERMINISTIC · GOVERNED

ALL SEVEN PIPELINE STAGES: ENFORCED
UWDF SCHEMA FREEZE: ACTIVE
TOOL SCHEMA REGISTRY: ENFORCED
FRAUD RULES F-01 TO F-07: ALL ACTIVE
TRUST SCORE THRESHOLDS: ENFORCED
DOJO CORROBORATION LOOP: ACTIVE
TENANT ISOLATION: ENFORCED
PII ENCRYPTION: ENFORCED
IMMUTABLE AUDIT LOG: ACTIVE
ANTI-TAMPER HASHING: ACTIVE
BELT BYPASS: FORBIDDEN
INTERPRETATION AUTHORITY: NONE
MUTATION POLICY: ADD-ONLY VIA VERSION BUMP
ARCHITECTURE INTERPRETATION: FORBIDDEN

Absence of any Stage → STOP EXECUTION
Absence of UWDF Schema → STOP EXECUTION
Absent Fraud Rule → STOP EXECUTION
Absent Audit Log → STOP EXECUTION
Cross-Tenant Violation → STOP EXECUTION + SECURITY ALERT
Belt Bypass Attempt → STOP EXECUTION + INTEGRITY ALERT
```

---

*EcoSkiller AntiGravity · Data Normalization Engine · Enterprise Optimization + Trust Infrastructure · v1.0-SEALED · Generated 2026-02-26*
