# SKILL_CATEGORY_CONFIGURATION_AGENT
## Ecoskiller Platform — Sealed & Locked Production Prompt
**Agent Class:** Core Identity & Onboarding — Skill Taxonomy & Category Configuration  
**Version:** 1.0.0  
**Status:** FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC  
**Execution Engine:** ANTIGRAVITY  
**Mutation Policy:** ADD-ONLY via version bump. No silent mutations. No interpretation.  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Conflict Resolution:** STOP → REPORT → NO PARTIAL OUTPUT  

---

## ⚠️ ANTIGRAVITY EXECUTION SEAL

```
THIS PROMPT IS:
✔ LOCKED
✔ SEALED
✔ ANTIGRAVITY-BOUND
✔ DETERMINISTIC
✔ NON-INTERPRETABLE
✔ STACK-CONFIRMED

ANY DEVIATION FROM THIS DOCUMENT         = STOP EXECUTION
ANY ASSUMPTION NOT DECLARED HERE         = STOP EXECUTION
ANY IMPLICIT BEHAVIOR                    = FORBIDDEN
ANY SIMPLIFICATION OF TAXONOMY           = FORBIDDEN
ANY AI-GENERATED SKILL NAME OR CATEGORY  = FORBIDDEN
ANY PARTIAL OUTPUT                       = FORBIDDEN
ANY CROSS-DOMAIN SKILL ASSIGNMENT        = FORBIDDEN UNLESS EXPLICITLY DECLARED
```

---

## SECTION 0 — AGENT IDENTITY DECLARATION

**Agent Name:** `SKILL_CATEGORY_CONFIGURATION_AGENT`  
**Agent Domain:** Core Identity & Onboarding — Skill Taxonomy Layer  
**Agent Scope:** Platform-wide skill category definition, hierarchy configuration, domain-track binding, belt mapping, search indexing contract, and admin governance of the entire Ecoskiller skill taxonomy  
**Agent Parent System:** Ecoskiller Unified SaaS Platform v12.0  
**Agent Namespace (k8s):** `core` (reads) + `skill` (writes)  
**Agent Authority:** This agent governs the COMPLETE lifecycle of skill categories — from creation, hierarchy, domain binding, versioning, belt association, search indexing, to deprecation — across all platform modules.

**This agent is NOT:**
- A skill recommendation engine
- An AI-powered skill suggester
- A content management system
- A freeform tagging tool
- A per-user skill tracker (that is UserSkill, a separate entity)

**This agent IS:**
- The master taxonomy authority for the Ecoskiller skill graph
- A deterministic, contract-gated, version-controlled category configuration system
- A domain-isolated, RBAC-enforced, audit-logged platform configuration module
- A sealed module whose output feeds: Skill Service, Dojo Match Engine, Belt Engine, Scoring Engine, Job Service (skill requirements), Search Index (OpenSearch), and Analytics (ClickHouse)

---

## SECTION 1 — SYSTEM CONTEXT & AUTHORITY CHAIN

### 1.1 Platform Identity (Non-Negotiable)

```
Application Name:        ECOSKILLER
System Type:             Unified Job + Skill + Project + Education + Marketplace SaaS
Architecture:            Event-driven microservices
Deployment:              Self-hosted, GCP + k3s, 100% open-source
Multi-tenancy:           Enforced at DB level (tenant_id RLS)
Identity Provider:       Keycloak (self-hosted, multi-tenant realm)
API Gateway:             NGINX Ingress
Event Bus:               Apache Kafka
Search Engine:           OpenSearch 2.x
Analytics:               ClickHouse
State Machine:           Redis (deterministic)
Object Storage:          MinIO
```

### 1.2 Domain Track Authority (Hard-Locked)

```
DOMAIN_TRACKS (immutable):
  ARTS
  COMMERCE
  SCIENCE
  TECHNOLOGY
  ADMINISTRATION

Rules:
  - Every skill category MUST belong to exactly one domain track
  - Cross-domain access is FORBIDDEN unless a CrossDomainLink record is explicitly created by PLATFORM_ADMIN
  - Domain leaks are SECURITY FAILURES — not warnings
  - Tenant isolation is HARD — no tenant may read another tenant's custom skill categories
  - Platform-global categories are readable by all tenants but editable only by PLATFORM_ADMIN
```

### 1.3 Authority Chain

```
Ecoskiller Core Platform (Master Authority)
    └── Skill Service (k8s: core / skill namespaces)
            └── SKILL_CATEGORY_CONFIGURATION_AGENT (this agent)
                    ├── Platform Skill Taxonomy (global, read by all)
                    ├── Tenant Custom Categories (isolated per tenant)
                    ├── Domain Track Binding Engine
                    ├── Belt & Proficiency Level Mapping
                    ├── Assessment Construct Registry
                    ├── Search Index Contract (OpenSearch)
                    ├── Analytics Dimension Registry (ClickHouse)
                    └── Category Version & Deprecation Engine
```

---

## SECTION 2 — SKILL TAXONOMY ARCHITECTURE

### 2.1 Four-Level Hierarchy (Non-Negotiable)

```
Level 1: DOMAIN TRACK
  ↳ Level 2: SKILL CATEGORY
        ↳ Level 3: SKILL
              ↳ Level 4: SKILL VARIANT / SUB-SKILL
```

**Rules:**
- Level 1 (Domain Track) is platform-immutable. It cannot be created or deleted. It can only be activated/deactivated by PLATFORM_ADMIN.
- Level 2 (Skill Category) is created by PLATFORM_ADMIN (global) or TENANT_ADMIN (tenant-scoped). It must be bound to exactly one Domain Track.
- Level 3 (Skill) is the atomic unit used in UserSkill, JobRequirement, DojoScenario, BeltMapping, and SearchIndex. It must belong to exactly one Skill Category.
- Level 4 (Skill Variant) is optional. Used for depth (e.g., Skill: Public Speaking → Variants: Debate, Presentation, Negotiation). Variants inherit parent skill's domain track and category.

**Violation of hierarchy = STOP EXECUTION → REPORT TAXONOMY_HIERARCHY_VIOLATION**

### 2.2 Skill Entity — Full Data Model

```sql
-- Level 2
CREATE TABLE skill_categories (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                TEXT NOT NULL,
    slug                TEXT UNIQUE NOT NULL,           -- URL-safe, lowercase, hyphenated
    description         TEXT,
    domain_track        skill_domain_enum NOT NULL,     -- ARTS|COMMERCE|SCIENCE|TECHNOLOGY|ADMINISTRATION
    scope               skill_scope_enum NOT NULL,      -- PLATFORM_GLOBAL | TENANT_CUSTOM
    tenant_id           UUID REFERENCES tenants(id),   -- NULL if PLATFORM_GLOBAL
    parent_category_id  UUID REFERENCES skill_categories(id),  -- NULL for top-level categories
    icon_key            TEXT,                           -- MinIO icon reference
    color_hex           TEXT,                           -- Display color (validated hex)
    display_order       INTEGER NOT NULL DEFAULT 0,
    is_active           BOOLEAN NOT NULL DEFAULT true,
    is_dojo_eligible    BOOLEAN NOT NULL DEFAULT false, -- Can be used in Dojo matches
    is_job_eligible     BOOLEAN NOT NULL DEFAULT true,  -- Can be used in job requirements
    is_belt_eligible    BOOLEAN NOT NULL DEFAULT false, -- Can trigger belt progression
    version             INTEGER NOT NULL DEFAULT 1,
    created_by          UUID REFERENCES users(id),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    deprecated_at       TIMESTAMPTZ,
    deprecation_reason  TEXT,
    successor_id        UUID REFERENCES skill_categories(id)  -- for migrations
);

-- Level 3
CREATE TABLE skills (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id             UUID NOT NULL REFERENCES skill_categories(id),
    name                    TEXT NOT NULL,
    slug                    TEXT UNIQUE NOT NULL,
    description             TEXT,
    domain_track            skill_domain_enum NOT NULL,   -- inherited, denormalized for query perf
    scope                   skill_scope_enum NOT NULL,
    tenant_id               UUID REFERENCES tenants(id),
    proficiency_model       proficiency_model_enum NOT NULL, -- BELT | LEVEL | SCORE_ONLY
    max_proficiency_level   INTEGER NOT NULL DEFAULT 5,
    has_assessment_construct BOOLEAN NOT NULL DEFAULT false,
    assessment_construct_id UUID REFERENCES assessment_constructs(id),
    is_active               BOOLEAN NOT NULL DEFAULT true,
    is_dojo_eligible        BOOLEAN NOT NULL DEFAULT false,
    is_job_eligible         BOOLEAN NOT NULL DEFAULT true,
    is_belt_eligible        BOOLEAN NOT NULL DEFAULT false,
    tags                    TEXT[],                        -- for search facets
    industry_codes          TEXT[],                        -- e.g. ["IT", "BANKING", "MEDIA"]
    demand_index            NUMERIC(5,2),                  -- updated by Analytics Service async
    version                 INTEGER NOT NULL DEFAULT 1,
    created_by              UUID REFERENCES users(id),
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    deprecated_at           TIMESTAMPTZ,
    deprecation_reason      TEXT,
    successor_id            UUID REFERENCES skills(id)
);

-- Level 4
CREATE TABLE skill_variants (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    skill_id    UUID NOT NULL REFERENCES skills(id),
    name        TEXT NOT NULL,
    slug        TEXT UNIQUE NOT NULL,
    description TEXT,
    is_active   BOOLEAN NOT NULL DEFAULT true,
    created_by  UUID REFERENCES users(id),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Enum types
CREATE TYPE skill_domain_enum AS ENUM (
    'ARTS', 'COMMERCE', 'SCIENCE', 'TECHNOLOGY', 'ADMINISTRATION'
);

CREATE TYPE skill_scope_enum AS ENUM (
    'PLATFORM_GLOBAL', 'TENANT_CUSTOM'
);

CREATE TYPE proficiency_model_enum AS ENUM (
    'BELT',         -- Dojo belt system (White → Black)
    'LEVEL',        -- Numeric level 1–5 (or configurable max)
    'SCORE_ONLY'    -- Raw score only, no tiered progression
);
```

### 2.3 Assessment Construct Registry (Required for Dojo-Eligible Skills)

Per SECTION T1 of the Dojo SaaS specification, every dojo-eligible skill must have a fully defined assessment construct before it can be activated in the Dojo Match Engine.

```sql
CREATE TABLE assessment_constructs (
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    skill_id                    UUID UNIQUE NOT NULL REFERENCES skills(id),
    construct_definition        TEXT NOT NULL,        -- What this skill fundamentally measures
    observable_behaviors        JSONB NOT NULL,       -- Array of observable behavior descriptors
    measurable_indicators       JSONB NOT NULL,       -- Quantifiable indicators per behavior
    rubric_justification        TEXT NOT NULL,        -- Why this rubric is valid
    metric_to_skill_mapping     JSONB NOT NULL,       -- Which scoring metrics map to this skill
    performance_level_descriptors JSONB NOT NULL,     -- Descriptions per proficiency level
    exclusion_indicators        JSONB NOT NULL,       -- What explicitly does NOT count toward this skill
    inter_rater_reliability_min NUMERIC(3,2) NOT NULL, -- Minimum acceptable IRR score (0.00–1.00)
    difficulty_normalization_factor NUMERIC(4,3),
    version                     INTEGER NOT NULL DEFAULT 1,
    approved_by                 UUID REFERENCES users(id),  -- Must be PLATFORM_ADMIN or MENTOR_BOARD
    approved_at                 TIMESTAMPTZ,
    created_by                  UUID REFERENCES users(id),
    created_at                  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);
```

**Rule:** `is_dojo_eligible = true` on a skill without an approved `assessment_construct` → STOP EXECUTION → REPORT CONSTRUCT_MISSING

### 2.4 Belt Mapping Table

```sql
CREATE TABLE belt_level_definitions (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    skill_id                UUID NOT NULL REFERENCES skills(id),
    belt_level              INTEGER NOT NULL,            -- 1 (White) to 7 (Black) or custom
    belt_name               TEXT NOT NULL,               -- e.g., "White", "Yellow", "Green"
    belt_color_hex          TEXT NOT NULL,
    min_match_count         INTEGER NOT NULL,
    min_score_threshold     NUMERIC(5,2) NOT NULL,
    pressure_scenario_pass  BOOLEAN NOT NULL DEFAULT true,
    mentor_confirmation_req BOOLEAN NOT NULL DEFAULT true,
    description             TEXT,
    UNIQUE (skill_id, belt_level)
);
```

**Rule:** Auto-promotion via code is FORBIDDEN. Belt assignment requires the full gate: match_count + score_threshold + pressure_scenario_pass + mentor_confirmation. All four must pass. Any short-circuit = STOP EXECUTION.

---

## SECTION 3 — ROLE-BASED ACCESS CONTROL FOR SKILL CONFIGURATION

### 3.1 Roles with Skill Category Authority

| Role | Can Create Global Category | Can Create Tenant Category | Can Deprecate | Can Assign Belt Mapping | Can Approve Construct |
|---|---|---|---|---|---|
| `PLATFORM_ADMIN` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `TENANT_ADMIN` | ❌ | ✅ (own tenant only) | ✅ (own tenant only) | ❌ | ❌ |
| `COACH` | ❌ | ❌ | ❌ | ❌ | ❌ |
| `COORDINATOR` | ❌ | ❌ | ❌ | ❌ | ❌ |
| `STUDENT` | ❌ | ❌ | ❌ | ❌ | ❌ |
| `RECRUITER` | ❌ | ❌ | ❌ | ❌ | ❌ |
| `MENTOR_BOARD` | ❌ | ❌ | ❌ | ❌ | ✅ (approve only) |

### 3.2 OPA Policy Rules (Skill Category)

```rego
package ecoskiller.skill.category

# Platform admin may create global or tenant-scoped categories
allow {
  input.action == "create_skill_category"
  input.actor.role == "PLATFORM_ADMIN"
}

# Tenant admin may create only tenant-scoped categories for their own tenant
allow {
  input.action == "create_skill_category"
  input.actor.role == "TENANT_ADMIN"
  input.payload.scope == "TENANT_CUSTOM"
  input.actor.tenant_id == input.payload.tenant_id
}

# No role may set scope = PLATFORM_GLOBAL except PLATFORM_ADMIN
deny {
  input.action == "create_skill_category"
  input.payload.scope == "PLATFORM_GLOBAL"
  input.actor.role != "PLATFORM_ADMIN"
}

# No role may assign a skill to a domain track other than their tenant's licensed tracks
deny {
  input.action == "create_skill"
  not platform_admin(input.actor)
  not licensed_domain(input.actor.tenant_id, input.payload.domain_track)
}

# Only PLATFORM_ADMIN or MENTOR_BOARD may approve assessment constructs
allow {
  input.action == "approve_assessment_construct"
  input.actor.role == "PLATFORM_ADMIN"
}
allow {
  input.action == "approve_assessment_construct"
  input.actor.role == "MENTOR_BOARD"
}

# No user may deprecate a skill that has active UserSkill records
deny {
  input.action == "deprecate_skill"
  active_userskill_count(input.payload.skill_id) > 0
  not input.payload.force_deprecate == true
  not input.actor.role == "PLATFORM_ADMIN"
}

# Cross-domain skill assignment is forbidden unless CrossDomainLink exists
deny {
  input.action == "assign_skill_to_job"
  job_domain(input.payload.job_id) != skill_domain(input.payload.skill_id)
  not cross_domain_link_exists(input.payload.job_id, input.payload.skill_id)
}
```

---

## SECTION 4 — CONFIGURATION STATE MACHINE

### 4.1 Skill Category Lifecycle States

```
DRAFT
  → [all required fields populated + domain track assigned] → PENDING_REVIEW
  → [saved incomplete] → DRAFT (unchanged)

PENDING_REVIEW
  → [PLATFORM_ADMIN or TENANT_ADMIN approves] → ACTIVE
  → [reviewer rejects] → DRAFT (with rejection notes)
  → [48h timeout with no action] → AUTO_NOTIFY reviewer

ACTIVE
  → [deprecation initiated by authorized role] → DEPRECATION_PENDING
  → [edit to any locked field] → DRAFT (version bump required)

DEPRECATION_PENDING
  → [no active UserSkills, DojoScenarios, JobRequirements] → DEPRECATED
  → [active references exist + no successor assigned] → STOP → REPORT ACTIVE_REFERENCES_BLOCKING_DEPRECATION
  → [successor assigned + migration plan confirmed] → DEPRECATED (migration queued)

DEPRECATED
  → terminal state (no reads in new flows)
  → existing records maintain FK integrity via successor_id chain
```

### 4.2 Skill Activation Gate (Checklist — All Must Pass)

```
Gate Item                                    Required For
─────────────────────────────────────────────────────────────────
category_id populated                         All skills
domain_track populated                        All skills
slug unique platform-wide                     All skills
proficiency_model declared                    All skills
is_dojo_eligible = true + construct approved  Dojo skills
is_belt_eligible = true + belt_map exists     Belt skills
is_job_eligible = true                        Job-requirement skills
assessment_construct.approved_by != null      Dojo + belt skills
belt_level_definitions ≥ 1 row                Belt skills
```

**Any gate item missing → STOP → REPORT SKILL_ACTIVATION_GATE_FAILURE**

---

## SECTION 5 — PLATFORM-GLOBAL SEED TAXONOMY

This section defines the mandatory seed taxonomy that MUST be loaded before any skill configuration is accessible. This data is seeded via Flyway migration and is immutable post-seeding (editable only via versioned migration).

### 5.1 Domain Track Seeds (Level 1 — Immutable)

```
ID  | Domain Track    | Active
----|-----------------|-------
DT1 | ARTS            | true
DT2 | COMMERCE        | true
DT3 | SCIENCE         | true
DT4 | TECHNOLOGY      | true
DT5 | ADMINISTRATION  | true
```

### 5.2 Platform-Global Skill Categories (Level 2 — Seed)

**ARTS**
```
ART-001  | Visual Arts          | Drawing, Painting, Sculpture, Digital Art
ART-002  | Performing Arts      | Acting, Dance, Music, Theatre
ART-003  | Literary Arts        | Creative Writing, Poetry, Storytelling, Translation
ART-004  | Design & Crafts      | Graphic Design, Textile, Jewellery, Pottery
ART-005  | Media Arts           | Photography, Videography, Animation, Audio Production
ART-006  | Communication Arts   | Public Speaking, Debate, Presentation, Anchoring
```

**COMMERCE**
```
COM-001  | Accounting & Finance      | Bookkeeping, Taxation, Financial Reporting, Auditing
COM-002  | Business Management       | Operations, Strategy, Supply Chain, HR Management
COM-003  | Sales & Marketing         | Digital Marketing, Sales Techniques, Market Research, Branding
COM-004  | Entrepreneurship          | Business Planning, Ideation, Pitching, Startup Operations
COM-005  | Banking & Insurance       | Banking Operations, Insurance Products, Risk Assessment
COM-006  | Logistics & Retail        | Inventory, Procurement, Retail Operations
```

**SCIENCE**
```
SCI-001  | Life Sciences             | Biology, Microbiology, Biochemistry, Genetics
SCI-002  | Physical Sciences         | Physics, Chemistry, Materials Science
SCI-003  | Earth & Environmental     | Geography, Environmental Studies, Climate Science
SCI-004  | Health Sciences           | Nutrition, First Aid, Public Health, Medical Basics
SCI-005  | Agricultural Sciences     | Agronomy, Horticulture, Animal Husbandry, Soil Science
SCI-006  | Laboratory Techniques     | Sample Analysis, Instrument Operation, Quality Control
```

**TECHNOLOGY**
```
TEC-001  | Software Development      | Programming Languages, Web Dev, Mobile Dev, APIs
TEC-002  | Data & Analytics          | Data Analysis, Machine Learning, Statistics, Visualization
TEC-003  | Infrastructure & DevOps   | Linux, Kubernetes, CI/CD, Networking, Cloud Basics
TEC-004  | Cybersecurity             | Ethical Hacking, Security Auditing, Compliance, OWASP
TEC-005  | Electronics & Hardware    | Circuit Design, Embedded Systems, IoT, PCB Design
TEC-006  | Emerging Technologies     | AI/ML Concepts, Blockchain Basics, AR/VR Fundamentals
TEC-007  | Digital Literacy          | Computer Basics, Office Tools, Internet Safety
```

**ADMINISTRATION**
```
ADM-001  | Governance & Policy       | Panchayat Administration, Govt Schemes, Policy Analysis
ADM-002  | Legal & Compliance        | Indian Law Basics, Consumer Rights, Labour Law
ADM-003  | Office Administration     | File Management, Correspondence, Record-Keeping
ADM-004  | Community Management      | Event Organisation, NGO Operations, CSR Management
ADM-005  | Education Administration  | Curriculum Design, Student Assessment, Institute Management
ADM-006  | Health & Welfare Admin    | Health Scheme Implementation, Social Work, Disability Services
```

### 5.3 Seed Loading Rules

```
Seed mechanism:     Flyway versioned migration
Migration file:     V002__skill_taxonomy_seed.sql
Execution order:    After V001__schema_baseline.sql
Idempotency:        All inserts use ON CONFLICT DO NOTHING
Mutation policy:    Never modify V002 — create V003 for additions
Absence of seed:    → STOP EXECUTION → REPORT SEED_TAXONOMY_MISSING
```

---

## SECTION 6 — API CONTRACT (OpenAPI 3.1)

### 6.1 Skill Category Endpoints

```yaml
openapi: 3.1.0
info:
  title: Ecoskiller Skill Category Configuration API
  version: 1.0.0

paths:

  /v1/skill-categories:
    get:
      summary: List all skill categories
      security: [bearerAuth: []]
      parameters:
        - name: domain_track
          in: query
          schema:
            type: string
            enum: [ARTS, COMMERCE, SCIENCE, TECHNOLOGY, ADMINISTRATION]
        - name: scope
          in: query
          schema:
            type: string
            enum: [PLATFORM_GLOBAL, TENANT_CUSTOM]
        - name: is_active
          in: query
          schema:
            type: boolean
        - name: is_dojo_eligible
          in: query
          schema:
            type: boolean
      responses:
        200:
          description: List of skill categories
        403:
          description: RBAC policy denied

    post:
      summary: Create a skill category
      security: [bearerAuth: []]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateSkillCategoryRequest'
      responses:
        201:
          description: Category created in DRAFT state
        400:
          description: Validation failure
        403:
          description: OPA policy denied
        409:
          description: Slug collision

  /v1/skill-categories/{id}/activate:
    post:
      summary: Activate a category (PENDING_REVIEW → ACTIVE)
      security: [bearerAuth: []]
      responses:
        200:
          description: Category activated
        403:
          description: Role not authorized
        422:
          description: Activation gate failed — missing fields

  /v1/skill-categories/{id}/deprecate:
    post:
      summary: Initiate deprecation
      security: [bearerAuth: []]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [reason, successor_id]
              properties:
                reason: {type: string}
                successor_id: {type: string, format: uuid}
                force_deprecate: {type: boolean, default: false}
      responses:
        200:
          description: Deprecation initiated
        409:
          description: Active references blocking deprecation

  /v1/skills:
    get:
      summary: List skills
      parameters:
        - name: category_id
          in: query
          schema: {type: string, format: uuid}
        - name: domain_track
          in: query
          schema: {type: string}
        - name: is_belt_eligible
          in: query
          schema: {type: boolean}
        - name: tags
          in: query
          schema: {type: array, items: {type: string}}
    post:
      summary: Create a skill
      security: [bearerAuth: []]

  /v1/skills/{id}/assessment-construct:
    put:
      summary: Create or update assessment construct for a skill
      security: [bearerAuth: []]
    get:
      summary: Get assessment construct for a skill

  /v1/skills/{id}/assessment-construct/approve:
    post:
      summary: Approve assessment construct (PLATFORM_ADMIN or MENTOR_BOARD only)
      security: [bearerAuth: []]

  /v1/skills/{id}/belt-levels:
    get:
      summary: Get belt level definitions for a skill
    post:
      summary: Create belt level definition
      security: [bearerAuth: []]

  /v1/domain-tracks:
    get:
      summary: List all domain tracks (read-only)
      responses:
        200:
          description: Fixed list of 5 domain tracks

components:
  schemas:
    CreateSkillCategoryRequest:
      type: object
      required: [name, domain_track, scope]
      properties:
        name: {type: string, minLength: 2, maxLength: 100}
        slug: {type: string, pattern: '^[a-z0-9-]+$'}
        description: {type: string}
        domain_track:
          type: string
          enum: [ARTS, COMMERCE, SCIENCE, TECHNOLOGY, ADMINISTRATION]
        scope:
          type: string
          enum: [PLATFORM_GLOBAL, TENANT_CUSTOM]
        tenant_id:
          type: string
          format: uuid
          description: Required if scope = TENANT_CUSTOM
        parent_category_id:
          type: string
          format: uuid
        is_dojo_eligible: {type: boolean, default: false}
        is_job_eligible: {type: boolean, default: true}
        is_belt_eligible: {type: boolean, default: false}
        display_order: {type: integer, default: 0}

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

### 6.2 API Rules

```
All endpoints return standard Ecoskiller error envelope:
{
  "error_code": "SKILL_ACTIVATION_GATE_FAILURE",
  "message": "Human-readable description",
  "details": {...},
  "request_id": "UUID",
  "timestamp": "ISO-8601"
}

No endpoint returns raw stack traces.
No endpoint silently swallows errors.
All 4xx and 5xx responses are logged to Wazuh SIEM.
```

---

## SECTION 7 — SEARCH INDEX CONTRACT (OpenSearch)

### 7.1 Index Definition

```json
{
  "index": "ecoskiller_skills",
  "settings": {
    "number_of_shards": 2,
    "number_of_replicas": 1,
    "analysis": {
      "analyzer": {
        "skill_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": ["lowercase", "stop", "snowball"]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "skill_id":         {"type": "keyword"},
      "category_id":      {"type": "keyword"},
      "name":             {"type": "text", "analyzer": "skill_analyzer", "fields": {"keyword": {"type": "keyword"}}},
      "slug":             {"type": "keyword"},
      "description":      {"type": "text", "analyzer": "skill_analyzer"},
      "domain_track":     {"type": "keyword"},
      "scope":            {"type": "keyword"},
      "tenant_id":        {"type": "keyword"},
      "tags":             {"type": "keyword"},
      "industry_codes":   {"type": "keyword"},
      "is_dojo_eligible": {"type": "boolean"},
      "is_job_eligible":  {"type": "boolean"},
      "is_belt_eligible": {"type": "boolean"},
      "is_active":        {"type": "boolean"},
      "demand_index":     {"type": "float"},
      "proficiency_model":{"type": "keyword"},
      "version":          {"type": "integer"},
      "created_at":       {"type": "date"}
    }
  }
}
```

### 7.2 Indexing Rules

```
Indexing trigger:    Kafka event consumer (skill.created, skill.updated, skill.deprecated)
Index update mode:   Upsert (never hard delete — mark is_active=false)
Deprecated skills:   Remain in index with is_active=false (for historical queries)
Tenant isolation:    Query-time filter_by tenant_id + PLATFORM_GLOBAL (combined)
Cross-domain access: Query-time enforcement — domain_track filter mandatory
SEO scope:           React (Next.js) reads OpenSearch via API — Flutter does NOT directly query OpenSearch
Autocomplete:        Enabled on name.keyword field (prefix queries only)
Faceted filters:     domain_track, tags, industry_codes, is_dojo_eligible, is_belt_eligible
```

### 7.3 Search Ranking Policy

```
Base relevance:     BM25 (OpenSearch default)
Boost signals:
  demand_index > 80:    score boost +1.5
  is_belt_eligible:     score boost +1.2 (for dojo-context queries)
  is_job_eligible:      score boost +1.3 (for job-context queries)
  PLATFORM_GLOBAL:      score boost +1.1 over TENANT_CUSTOM (unless tenant query)

Re-ranking:         None — no ML re-ranking at this layer
Absence of ranking policy → STOP EXECUTION
```

---

## SECTION 8 — KAFKA EVENT SCHEMA

### 8.1 Events Emitted by this Agent

```yaml
# skill.category.created
topic: ecoskiller.skill.taxonomy
key: category_id
payload:
  event_type: skill.category.created
  category_id: UUID
  name: string
  domain_track: enum
  scope: enum
  tenant_id: UUID | null
  created_by: UUID
  timestamp: ISO-8601

# skill.category.activated
topic: ecoskiller.skill.taxonomy
payload:
  event_type: skill.category.activated
  category_id: UUID
  domain_track: enum
  activated_by: UUID
  timestamp: ISO-8601

# skill.created
topic: ecoskiller.skill.taxonomy
payload:
  event_type: skill.created
  skill_id: UUID
  category_id: UUID
  domain_track: enum
  is_dojo_eligible: boolean
  is_belt_eligible: boolean
  is_job_eligible: boolean
  proficiency_model: enum
  timestamp: ISO-8601

# skill.updated
topic: ecoskiller.skill.taxonomy
payload:
  event_type: skill.updated
  skill_id: UUID
  changed_fields: string[]
  updated_by: UUID
  version: integer
  timestamp: ISO-8601

# skill.deprecated
topic: ecoskiller.skill.taxonomy
payload:
  event_type: skill.deprecated
  skill_id: UUID
  successor_id: UUID | null
  reason: string
  deprecated_by: UUID
  active_userskill_migrated: boolean
  timestamp: ISO-8601

# skill.assessment_construct.approved
topic: ecoskiller.skill.taxonomy
payload:
  event_type: skill.assessment_construct.approved
  skill_id: UUID
  construct_id: UUID
  approved_by: UUID
  timestamp: ISO-8601

# skill.belt_level.defined
topic: ecoskiller.skill.taxonomy
payload:
  event_type: skill.belt_level.defined
  skill_id: UUID
  belt_level: integer
  belt_name: string
  min_match_count: integer
  min_score_threshold: float
  timestamp: ISO-8601
```

### 8.2 Downstream Consumers

```
Consumer Service           Consumed Events
─────────────────────────────────────────────────────────────────
OpenSearch Indexer         skill.created, skill.updated, skill.deprecated
Job Service                skill.created, skill.deprecated (update job requirements)
Dojo Match Engine          skill.assessment_construct.approved, skill.belt_level.defined
Belt Engine                skill.belt_level.defined, skill.deprecated
Scoring Engine             skill.assessment_construct.approved
Analytics Service          ALL events → ClickHouse dimension tables
Notification Service       skill.deprecated (notify affected users via UserSkill FK)
```

---

## SECTION 9 — ANALYTICS DIMENSION REGISTRY (ClickHouse)

### 9.1 Dimension Tables

```sql
-- ClickHouse skill dimension (materialized from Kafka)
CREATE TABLE ecoskiller.dim_skills (
    skill_id         UUID,
    category_id      UUID,
    skill_name       String,
    category_name    String,
    domain_track     LowCardinality(String),
    scope            LowCardinality(String),
    tenant_id        Nullable(UUID),
    is_dojo_eligible UInt8,
    is_belt_eligible UInt8,
    is_job_eligible  UInt8,
    is_active        UInt8,
    demand_index     Float32,
    version          UInt32,
    valid_from       DateTime,
    valid_to         Nullable(DateTime)   -- SCD Type 2
) ENGINE = MergeTree()
ORDER BY (domain_track, category_id, skill_id);

-- Skill demand metrics (updated by Analytics Service via event stream)
CREATE TABLE ecoskiller.skill_demand_stats (
    skill_id         UUID,
    domain_track     LowCardinality(String),
    period_month     Date,
    job_requirement_count     UInt32,
    userskill_count           UInt32,
    dojo_scenario_usage_count UInt32,
    demand_index_computed     Float32
) ENGINE = SummingMergeTree()
ORDER BY (domain_track, skill_id, period_month);
```

### 9.2 Required Grafana Panels

```
Dashboard: Skill Taxonomy Health
Panels:
  1. Skills by domain track (bar chart)
  2. Dojo-eligible vs total skills (donut)
  3. Deprecated skills trend (line)
  4. Pending assessment construct approvals (counter)
  5. Skill demand index heatmap by domain track
  6. Top 20 most used skills (job requirements + user skills combined)
  7. Skill creation rate by tenant (last 30 days)
  8. Belt-eligible skills without belt level definitions (alert counter)
```

---

## SECTION 10 — VERSIONING & DEPRECATION PROTOCOL

### 10.1 Version Bump Rules

```
Scenario                                  Action Required
─────────────────────────────────────────────────────────────────
Adding a new skill to existing category   No version bump on category
Renaming a skill name                     Skill version++
Changing domain_track of a skill          FORBIDDEN (create new skill + deprecate old)
Changing proficiency_model               FORBIDDEN (create new skill + deprecate old)
Changing is_dojo_eligible true→false      Category version++ + all active Dojo scenarios must be reviewed
Editing assessment_construct content      Construct version++ + re-approval required
Changing belt level thresholds            BeltLevelDefinition version++ + audit log mandatory
Adding a new Level 2 category             Category version = 1 (new record)
```

### 10.2 Deprecation Migration Protocol

```
Step 1: Identify successor_id (mandatory before deprecation trigger)
Step 2: Enumerate active references:
          - UserSkill records using this skill_id
          - JobRequirement records using this skill_id
          - DojoScenario records using this skill_id
          - BeltMapping records using this skill_id
Step 3: Notify affected users via Notification Service (SMS + ntfy)
Step 4: Set skill deprecated_at + successor_id in PostgreSQL
Step 5: Emit skill.deprecated Kafka event with active_userskill_migrated flag
Step 6: Analytics Service updates ClickHouse dim_skills SCD record (valid_to = now)
Step 7: OpenSearch updates is_active=false for deprecated skill
Step 8: Job Service auto-suggests successor to recruiters with stale requirements
Step 9: Audit log: actor, reason, successor, migration count — immutable record
```

### 10.3 Seed Data Version Control

```
No migration file may be edited after execution in any environment.
Additions to taxonomy = new Flyway migration file (V003, V004, etc.)
Each migration file must:
  - Be numbered sequentially
  - Include rollback SQL (DOWN migration)
  - Be tested in DEV and TEST before STAGING deployment
  - Never contain hardcoded UUIDs (use gen_random_uuid())
  - Never contain environment-specific values
```

---

## SECTION 11 — UI CONTRACT (Flutter + React)

### 11.1 Flutter App — Admin Skill Configuration Screens

```
Framework:  Flutter (stable, Material 3, Riverpod)
Access:     PLATFORM_ADMIN and TENANT_ADMIN only (RBAC enforced at route level)

Required Screens:

  [1] SKILL_TAXONOMY_DASHBOARD
      - Domain track overview (5 tracks, skill count per track)
      - Active / Draft / Deprecated counts
      - Pending construct approvals counter (badge)
      - Quick action: Create Category, Create Skill

  [2] SKILL_CATEGORY_LIST
      - Filterable by: domain_track, scope, is_active, is_dojo_eligible
      - Sortable by: name, display_order, created_at, demand_index
      - Each row shows: name, domain_track badge, scope badge, state badge, skill count
      - Row actions: View, Edit (if DRAFT), Activate, Deprecate

  [3] SKILL_CATEGORY_FORM
      - Fields per Section 2.2 schema
      - Slug auto-generated from name (editable before submission)
      - Domain track selector (dropdown — 5 fixed options)
      - Scope selector (PLATFORM_GLOBAL restricted to PLATFORM_ADMIN)
      - Toggle: is_dojo_eligible, is_job_eligible, is_belt_eligible
      - Save as DRAFT → explicit Activate button (2-step)
      - Validation: inline, field-level, no generic errors

  [4] SKILL_LIST (within a category)
      - All skills in selected category
      - Filter by: is_active, proficiency_model, is_belt_eligible
      - Construct status indicator per skill (Approved / Pending / Missing)

  [5] SKILL_FORM
      - Full skill creation/edit form
      - Proficiency model selector
      - Tags input (multi-select + free entry)
      - Industry codes (multi-select from registered industry code list)
      - Assessment construct status badge (Approved | Pending | Not Required)
      - If is_dojo_eligible enabled: construct entry becomes mandatory

  [6] ASSESSMENT_CONSTRUCT_FORM
      - Section per construct field (construct_definition, observable_behaviors, etc.)
      - observable_behaviors and measurable_indicators: structured array input
      - performance_level_descriptors: matrix input (level × descriptor)
      - Inter-rater reliability min: numeric slider (0.00–1.00)
      - Submit for approval → MENTOR_BOARD or PLATFORM_ADMIN notified
      - Approval status badge

  [7] BELT_LEVEL_CONFIG
      - For belt-eligible skills only
      - Table of belt levels (drag to reorder)
      - Per-row: belt_name, color picker, min_match_count, min_score_threshold
      - Toggle: pressure_scenario_pass, mentor_confirmation_req
      - Save = creates/updates belt_level_definitions rows

  [8] SKILL_DEPRECATION_FLOW
      - Multi-step: Select successor → View active references → Confirm migration plan → Trigger
      - Active reference count displayed (UserSkill, JobRequirement, DojoScenario)
      - Cannot confirm without successor_id
      - Force override only available to PLATFORM_ADMIN with explicit reason
```

### 11.2 React SEO Layer (Next.js — Read-Only Clone)

```
Purpose:    Public-facing skill directory — SEO indexed
Scope:      PLATFORM_GLOBAL, ACTIVE skills only
Mutations:  DISABLED (read-only)

Required Pages:
  /skills                            → Skill directory (all domain tracks)
  /skills/[domain-track]             → Skills by domain (e.g., /skills/technology)
  /skills/[domain-track]/[category]  → Category detail with skill list
  /skills/[skill-slug]               → Individual skill page (SEO optimized)

SEO Requirements:
  - Schema.org EducationalOccupationalCredential markup per skill
  - OpenGraph tags per skill page
  - Auto-generated sitemap including all active skill slugs
  - Canonical URLs enforced
  - SSR (server-side rendered) — no client-side rendering of skill content
  - Dynamic meta description from skill description field

React MUST NOT:
  - Display TENANT_CUSTOM skills on public pages
  - Display DRAFT or DEPRECATED skills
  - Display assessment construct details (internal data)
  - Display belt level thresholds (internal data)
```

### 11.3 UI Rules

```
- No skill form may auto-submit
- No category may be activated from the list view — must go into detail view first
- Bulk actions on skills are restricted: bulk activate is FORBIDDEN (must be individual)
- Belt level config UI is only visible when is_belt_eligible = true
- Assessment construct UI is only visible when is_dojo_eligible = true
- Deprecation flow cannot be completed in fewer than 3 explicit user confirmations
- Domain track on an existing skill is READ-ONLY after creation
- Color tokens follow the platform design system (SECTION 3 of Ecoskiller LOCKED PROMPT):
    PRIMARY_COLOR  = #1E3A8A
    ACCENT_COLOR   = #10B981
    NEUTRAL_BASE   = #F8FAFC
    ERROR_COLOR    = #DC2626
    WARNING_COLOR  = #F59E0B
    SUCCESS_COLOR  = #16A34A
```

---

## SECTION 12 — MICROSERVICE RESPONSIBILITY MAPPING

| Responsibility | Microservice | Namespace |
|---|---|---|
| Category & skill CRUD, state machine | `skill-service` | skill |
| OPA policy evaluation | `auth-service` (OPA sidecar) | core |
| Keycloak role check (JWT validation) | `auth-service` | core |
| Assessment construct management | `skill-service` | skill |
| Belt level definitions | `skill-service` | skill |
| Search index sync (OpenSearch) | `search-service` (Kafka consumer) | core |
| Analytics dimension sync (ClickHouse) | `analytics-service` (Kafka consumer) | analytics |
| Flyway migrations | `skill-service` init container | skill |
| Kafka event emission | `skill-service` (producer) | skill |
| Audit log write | `admin-governance-service` | core |
| Wazuh SIEM | `admin-governance-service` | core |
| Notification on deprecation | `notification-service` | core |
| SEO API (React consumption) | `skill-service` (public read endpoint) | skill |

---

## SECTION 13 — OBSERVABILITY & AUDIT

### 13.1 Prometheus Metrics

```
skill_categories_total{domain_track, scope, state}
skills_total{domain_track, scope, state, proficiency_model}
skill_category_create_total{domain_track, scope, actor_role}
skill_category_create_failures_total{failure_code}
skill_activation_gate_failures_total{gate_item}
assessment_construct_pending_approval_count{domain_track}
skill_deprecation_total{domain_track, had_active_references}
skill_search_queries_total{domain_track}
skill_demand_index_update_lag_seconds
```

### 13.2 Audit Log Requirements

```
Every write operation must produce an immutable audit record:

  audit_id:       UUID
  entity_type:    skill_category | skill | assessment_construct | belt_level_definition
  entity_id:      UUID
  action:         CREATED | ACTIVATED | UPDATED | DEPRECATED | CONSTRUCT_APPROVED | BELT_DEFINED
  actor_id:       UUID (user who performed action)
  actor_role:     role at time of action
  tenant_id:      UUID | null
  payload_before: JSONB (previous state)
  payload_after:  JSONB (new state)
  changed_fields: TEXT[]
  ip_address:     TEXT
  request_id:     UUID
  timestamp:      TIMESTAMPTZ

Write-once (no UPDATE or DELETE on audit_log rows — enforced by RLS policy).
Retention: 3 years minimum.
Replicated to Wazuh SIEM.
```

---

## SECTION 14 — FAILURE CATALOGUE

| Code | Trigger | Agent Response | Escalation |
|---|---|---|---|
| `TAXONOMY_HIERARCHY_VIOLATION` | Skill assigned to non-existent category | STOP → 400 Bad Request | None |
| `DOMAIN_TRACK_MISMATCH` | Category domain ≠ parent category domain | STOP → 422 | None |
| `SLUG_COLLISION` | Duplicate slug detected | STOP → 409 | None |
| `SCOPE_POLICY_DENIED` | TENANT_ADMIN tries to create PLATFORM_GLOBAL | STOP → 403 + audit | PLATFORM_ADMIN |
| `SKILL_ACTIVATION_GATE_FAILURE` | Required gate item missing | STOP → 422 with gate item detail | None |
| `CONSTRUCT_MISSING` | is_dojo_eligible=true but no construct | STOP → 422 | None |
| `CONSTRUCT_NOT_APPROVED` | Dojo activation attempted without approved construct | STOP → 422 | MENTOR_BOARD |
| `BELT_MAP_MISSING` | is_belt_eligible=true but no belt_level_definitions | STOP → 422 | None |
| `ACTIVE_REFERENCES_BLOCKING_DEPRECATION` | Active UserSkill/Job/Dojo refs on skill | STOP → 409 | PLATFORM_ADMIN |
| `SEED_TAXONOMY_MISSING` | V002 migration not executed | STOP → 500 + alert | PLATFORM_ADMIN |
| `CROSS_DOMAIN_ASSIGNMENT_DENIED` | Job requires skill from different domain | STOP → 403 | None |
| `SEARCH_INDEX_SYNC_FAILURE` | OpenSearch upsert failed | Log + retry (5× exp backoff) | Platform ops |
| `ANALYTICS_SYNC_FAILURE` | ClickHouse insert failed | Log + retry | Platform ops |
| `VERSION_CONFLICT` | Concurrent edit on same entity version | STOP → 409 (optimistic lock) | None |
| `MIGRATION_CONFLICT` | Flyway checksum mismatch | STOP DEPLOYMENT | PLATFORM_ADMIN |

**All failures produce: audit record + Kafka event (where applicable) + error response with `error_code`.**

---

## SECTION 15 — DEPLOYMENT SPECIFICATION

### 15.1 Kubernetes

```yaml
namespace: skill

Deployments:
  - skill-service         (2 replicas min, autoscale to 8)

ConfigMaps:
  - skill-taxonomy-config
      keys: DEFAULT_MAX_PROFICIENCY_LEVEL, SLUG_MAX_LENGTH,
            DEMAND_INDEX_UPDATE_INTERVAL_HOURS, DEPRECATION_NOTIFICATION_DAYS_BEFORE

Secrets (via HashiCorp Vault):
  - postgres-skill-credentials
  - opensearch-credentials
  - kafka-producer-credentials

Persistent Volume Claims: None (stateless service)

Health probes:
  readinessProbe: GET /health/ready
  livenessProbe:  GET /health/live
```

### 15.2 Flyway Migration Order

```
V001__schema_baseline.sql          (enums, extensions)
V002__skill_taxonomy_seed.sql      (5 domain tracks, seed categories)
V003__assessment_construct.sql     (assessment_constructs table)
V004__belt_level_definitions.sql   (belt_level_definitions table)
V005__skill_variants.sql           (skill_variants table)
V006__cross_domain_links.sql       (cross_domain_links table)

All must pass in all environments before skill-service accepts traffic.
Failure of any migration → pod fails readiness probe → no traffic routed.
```

---

## SECTION 16 — INTEGRATION WITH DOWNSTREAM MODULES

### 16.1 Modules Gated by Skill Configuration Readiness

```
Dojo Match Engine:
  ✅ Unlocked when: skill.assessment_construct.approved received
  ❌ Blocked when: construct missing or not approved

Belt Engine:
  ✅ Unlocked when: skill.belt_level.defined received (all levels)
  ❌ Blocked when: belt map incomplete

Job Service:
  ✅ Uses skills in job requirements (is_job_eligible = true only)
  ❌ Rejects skill_id where is_job_eligible = false or skill is DEPRECATED

Scoring Engine:
  ✅ References assessment_construct.metric_to_skill_mapping
  ❌ Cannot score a match if construct not approved

Analytics Service:
  ✅ Consumes all taxonomy events to populate ClickHouse dim_skills
  ✅ Computes demand_index async and writes back to skill.demand_index via internal API

OpenSearch Indexer:
  ✅ Indexes all ACTIVE skills
  ✅ Marks DEPRECATED skills as is_active=false (never hard deletes)
```

### 16.2 What This Agent Does NOT Touch

```
- UserSkill (per-user skill assignment) — managed by User Service
- JobRequirement (skill requirements per job) — managed by Job Service
- DojoScenario content — managed by Dojo Match Engine
- Intelligence DNA profiling — managed by Intelligence Engine
- Billing plan features — managed by Billing Service
- Certificate issuance — managed by Certification & Belt Engine
```

---

## SECTION 17 — SECURITY BASELINE

```
WAF:                  ModSecurity on skill-service ingress
Rate Limiting:        50 category-write requests / tenant / minute (Envoy)
Tenant Isolation:     RLS on tenant_id — no cross-tenant reads via ORM or raw SQL
Domain Isolation:     OPA enforces domain_track filter on all cross-domain operations
Slug Injection:       All slugs validated against regex ^[a-z0-9-]+$ before persistence
JSONB Fields:         Size-limited (max 50KB per JSONB column — enforced at API layer)
Audit Immutability:   No DELETE on audit_logs table (RLS enforced at PostgreSQL level)
SEO API:              Public endpoint rate-limited at 200 req/min per IP (Envoy)
Secret Storage:       All DB credentials in HashiCorp Vault — never in k8s Secrets plaintext
Construct Approval:   Dual-role gate (PLATFORM_ADMIN or MENTOR_BOARD) — no self-approval
```

---

## SECTION 18 — FINAL EXECUTION LAWS

### 18.1 Completion Criteria

This agent's output is only valid when ALL of the following are confirmed:

```
✔ Five domain tracks seeded and immutable (V002 migration passed)
✔ All platform-global seed categories loaded (30 categories per seed spec)
✔ skill-service API responding on all declared endpoints
✔ OPA policies deployed and tested against permission matrix
✔ OpenSearch index created + skill.created consumer active
✔ ClickHouse dim_skills table created + analytics consumer active
✔ Assessment construct approval flow tested (submit → approve → skill activates)
✔ Belt level config tested (define → skill marked belt_eligible)
✔ Deprecation flow tested with active references present (must block without force)
✔ Audit log confirmed write-once (test UPDATE → must fail)
✔ Flutter admin screens present and RBAC-enforced at route level
✔ React public skill directory rendering PLATFORM_GLOBAL ACTIVE skills only
✔ All Kafka events emitting with correct schema
✔ All 15 failure codes producing correct error responses
✔ Grafana dashboard (Section 9.2) active with live data
✔ SEO pages indexable (verified via Google Search Console simulation)
```

### 18.2 What Antigravity Must NOT Do

```
❌ Do NOT auto-generate skill names or categories from AI inference
❌ Do NOT allow any role except PLATFORM_ADMIN to create PLATFORM_GLOBAL categories
❌ Do NOT allow domain_track to be changed on an existing skill (create new + deprecate)
❌ Do NOT allow proficiency_model to be changed on an existing skill
❌ Do NOT mark a skill dojo-eligible without an approved assessment construct
❌ Do NOT allow belt promotion logic inside this agent — that is the Belt Engine's domain
❌ Do NOT expose assessment construct internals on public-facing SEO API
❌ Do NOT allow TENANT_CUSTOM skills to appear in SEO pages
❌ Do NOT allow bulk activation of skills — all activations are individual, gated
❌ Do NOT silently swallow slug collisions — surface 409 with conflicting slug path
❌ Do NOT allow a skill to be deprecated without a successor_id (unless PLATFORM_ADMIN force)
❌ Do NOT use Firebase, paid SaaS search, or any non-OpenSearch search backend
❌ Do NOT mix skill category config concerns with UserSkill (per-user) concerns
❌ Do NOT allow cross-domain skill assignment without an explicit CrossDomainLink record
❌ Do NOT allow Flyway migrations to run on production without passing DEV + TEST + STAGING
```

### 18.3 Antigravity Execution Contract

```
ANTIGRAVITY receives this prompt.
ANTIGRAVITY reads it in full before generating any output.
ANTIGRAVITY generates output only within the boundaries declared here.
ANTIGRAVITY does not infer, extend, or reinterpret any rule.
ANTIGRAVITY does not fill gaps silently — it STOPS and REPORTS gaps.
ANTIGRAVITY does not mix concerns across sections.
ANTIGRAVITY does not produce partial output — complete or nothing.

IF ANTIGRAVITY encounters ambiguity:
  → STOP
  → REPORT: AMBIGUITY_IN_SECTION_[X]
  → AWAIT human clarification

IF ANTIGRAVITY encounters a conflict with another Ecoskiller module:
  → STOP
  → REPORT: MODULE_CONFLICT_[module_name]
  → AWAIT resolution

IF ANTIGRAVITY is asked to auto-generate skill names or categories:
  → STOP
  → REPORT: AI_TAXONOMY_GENERATION_FORBIDDEN
  → REQUEST human-authored taxonomy input
```

---

## ✅ SEAL CONFIRMATION

```
Agent:            SKILL_CATEGORY_CONFIGURATION_AGENT
Version:          1.0.0
Platform:         Ecoskiller v12.0
Domain:           Core Identity & Onboarding — Skill Taxonomy Layer
Execution Engine: ANTIGRAVITY
Status:           FINAL · LOCKED · SEALED

Sections Covered:
  ✔ Section 0  — Agent Identity
  ✔ Section 1  — System Context & Authority Chain
  ✔ Section 2  — Skill Taxonomy Architecture (4-level hierarchy + full schemas)
  ✔ Section 3  — RBAC & OPA Policy Enforcement
  ✔ Section 4  — Configuration State Machine
  ✔ Section 5  — Platform-Global Seed Taxonomy (30 categories, 5 tracks)
  ✔ Section 6  — API Contract (OpenAPI 3.1)
  ✔ Section 7  — Search Index Contract (OpenSearch)
  ✔ Section 8  — Kafka Event Schema (8 events)
  ✔ Section 9  — Analytics Dimension Registry (ClickHouse)
  ✔ Section 10 — Versioning & Deprecation Protocol
  ✔ Section 11 — UI Contract (Flutter + React SEO)
  ✔ Section 12 — Microservice Responsibility Map
  ✔ Section 13 — Observability & Audit
  ✔ Section 14 — Failure Catalogue (15 codes)
  ✔ Section 15 — Deployment Specification (k8s + Flyway)
  ✔ Section 16 — Downstream Module Integration
  ✔ Section 17 — Security Baseline
  ✔ Section 18 — Final Execution Laws

ANY MUTATION WITHOUT VERSION BUMP    = INVALID
ANY EXECUTION OUTSIDE ANTIGRAVITY    = INVALID
ANY PARTIAL IMPLEMENTATION           = INVALID
ANY AI-GENERATED TAXONOMY INPUT      = INVALID
```
