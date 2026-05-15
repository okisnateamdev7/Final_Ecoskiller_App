# 🔒 KNOWLEDGE_SHARING_PORTAL_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Ecoskiller Platform — ANTIGRAVITY LAYER
**Artifact Class:** Production Agent Blueprint — Sealed & Locked  
**Status:** FINAL · LOCKED · GOVERNED · DETERMINISTIC  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Version:** v1.0 — March 2026  
**Domain:** Enterprise Optimization + Trust Infrastructure  
**Agent Identity Namespace:** `ecoskiller.agents.knowledge_sharing_portal`

---

> **ANTIGRAVITY DEFINITION**
> Antigravity in Ecoskiller context means: a system layer that actively **reverses institutional drag** — hoarding, gatekeeping, silence, redundancy, and entropy — by making knowledge flow lighter, faster, more trusted, and structurally enforced. This agent is not a wiki. It is a living trust machine embedded in enterprise architecture.

---

## 🧱 SECTION 0 — EXECUTION CONSTRAINTS (NON-NEGOTIABLE)

```
EXECUTION_MODE            = LOCKED
MUTATION_POLICY           = ADD_ONLY
CREATIVE_INTERPRETATION   = FORBIDDEN
ASSUMPTION_FILLING        = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY
FAILURE_MODE              = STOP → REPORT → NO_PARTIAL_OUTPUT
PARTIAL_KNOWLEDGE_SAVE    = FORBIDDEN
UNAPPROVED_INFERENCE      = FORBIDDEN
AGENT_SELF_MODIFICATION   = FORBIDDEN
```

**Failure cascade rule:** Any violation of a constraint in this section → `STOP_ALL` → emit `AGENT_CONSTRAINT_VIOLATION` event to Kafka → alert Admin Governance Service → lock agent execution until human confirmation.

---

## 🎯 SECTION 1 — AGENT IDENTITY

| Field | Value |
|---|---|
| Agent Name | `KNOWLEDGE_SHARING_PORTAL_AGENT` |
| Agent Class | Enterprise Optimization + Trust Infrastructure |
| Platform Layer | Antigravity Layer (above core services) |
| Parent System | Ecoskiller Unified SaaS Platform |
| Execution Mode | Deterministic rule-based + event-driven |
| AI Permitted | Indexing, summarization, semantic tagging ONLY |
| AI Forbidden | Scoring overrides, trust decisions, moderation decisions |
| Bias Elimination | Structural — no personality inference, no ranking by author seniority |
| Storage Backend | PostgreSQL (metadata) + MinIO (artifacts) + OpenSearch (search) + ClickHouse (analytics) |
| State Machine | Redis |
| Event Bus | Kafka |
| Auth | Keycloak (RBAC-gated all access) |
| Policy Engine | Open Policy Agent (OPA) |
| Secrets | HashiCorp Vault |

---

## 🗺️ SECTION 2 — SYSTEM POSITIONING IN ECOSKILLER ARCHITECTURE

```
ECOSKILLER PLATFORM LAYERS
│
├── Foundation Lane A — Identity, RBAC, Tenancy, API Gateway, Event Registry
├── Data Lane B        — PostgreSQL, CQRS, Audit Logs, Search Index
├── Core Services Lane C — Job, Skill, Project, Education, Marketplace
├── Governance Lane D  — Notification, Billing, Reputation, Moderation
├── UI Lane E          — Flutter Mobile, Web Dashboards, Widgets
├── Intelligence Lane F — Matching, Ranking, Discovery, AI Explainability
├── DevOps Lane G      — Kubernetes, CI/CD, Observability, Secrets
│
└── ★ ANTIGRAVITY LAYER — KNOWLEDGE_SHARING_PORTAL_AGENT
      ├── Reads from: Lanes A, B, C, D, F
      ├── Writes to: Knowledge Repository, Trust Registry, Audit Ledger
      ├── Emits to: Kafka Event Bus
      └── Governed by: OPA + Admin Governance Service + Keycloak
```

**Binding rule:** The agent sits on top of the existing 75–80 service mesh. It consumes contracts — it does not bypass them. All data access goes through the canonical API Gateway (Kong → Envoy → ModSecurity stack).

---

## 👥 SECTION 3 — USER ECOSYSTEM SERVED (LOCKED)

| User Group | Knowledge Sharing Role | Access Level |
|---|---|---|
| Students | Consumers + peer contributors | Role-gated read; contribute after verification |
| Trainers / Mentors | Primary knowledge creators | Full create + review; belt and reputation weighted |
| Evaluators | Knowledge validators | Review + approve artifacts; audit write |
| Institutes | Curriculum publishers | Publish structured programs; territory-gated |
| Enterprises / SMEs | Solution publishers; consumer of talent knowledge | Read + curated publish; recruiter-gated |
| Recruiters / HR | Job knowledge consumers; GD pattern consumers | Read-only curated feed |
| Admins (Tenant / Platform / Compliance) | Governance + moderation | Full audit; moderation override |
| Parents | Trust layer; read-only progress visibility | Parent-scoped read-only portal |
| Automation / AI Agents | Event-driven ingestion and indexing | System-level; no human impersonation |

**Domain isolation rule:** Knowledge shared in Arts domain is NOT visible to Technology domain users unless cross-domain grant exists in OPA policy. Domain leaks = SECURITY FAILURE.

---

## 🏗️ SECTION 4 — ARCHITECTURE COMPONENTS

### 4.1 Microservice Identity

```
Service Name:       knowledge-portal-service
Namespace (k8s):    knowledge
Spring Boot:        Yes (primary backend)
Port (internal):    8095
Kafka Consumer:     knowledge.events
Kafka Producer:     knowledge.published, knowledge.validated, knowledge.flagged
Redis Keyspace:     ksp:*
OPA Policy Path:    /policies/knowledge/
Helm Chart:         ecoskiller/knowledge-portal
```

### 4.2 Supporting Services (Existing — Consumed, Not Duplicated)

| Service | Role in Agent |
|---|---|
| Auth Service | JWT validation, RBAC enforcement |
| User Service | Author identity, mentor reputation vector |
| Notification Service | Knowledge event alerts (email + push + SMS) |
| Analytics Service | Knowledge usage funnels → ClickHouse |
| Admin Governance Service | Moderation, dispute, compliance pipeline |
| Scoring Engine | Weighted knowledge quality scores (locked formula) |
| Certification & Belt Engine | Knowledge milestone certs (belt-gated topics) |
| OpenSearch | Full-text + semantic search for all knowledge artifacts |
| MinIO | Binary artifact storage (PDFs, videos, slides, code zips) |
| PostgreSQL | Relational metadata: articles, tags, versions, approvals |
| ClickHouse | Knowledge analytics: views, shares, ratings, decay metrics |
| Kafka | Event bus for all knowledge lifecycle events |
| Redis | Hot cache for trending knowledge, session state |
| Keycloak | SSO, MFA, RBAC token issuance |
| OPA | Policy-as-code: who can create, validate, approve, view |
| HashiCorp Vault | API keys, storage credentials — never hardcoded |
| Wazuh | Security monitoring, anomaly flagging on knowledge access |

### 4.3 New Microservices Required (Knowledge Domain Only)

```
1. knowledge-portal-service       — Core CRUD, lifecycle, versioning
2. knowledge-trust-engine         — Trust scoring, verification, anti-plagiarism
3. knowledge-search-service       — Semantic + keyword search (wraps OpenSearch)
4. knowledge-analytics-service    — Consumption, quality, decay analytics
5. knowledge-recommendation-engine — Personalized knowledge surfacing (vector-based)
```

**Rule:** All 5 services deploy in `knowledge` Kubernetes namespace. All use Kafka event-driven communication. No synchronous chaining across domains.

---

## 🔩 SECTION 5 — DATA MODEL (FROZEN)

**Rule:** Primary entities cannot be renamed. Fields may extend — not mutate.

### 5.1 Core Entities

```
KnowledgeArtifact
KnowledgeVersion
KnowledgeTrustRecord
KnowledgeTag
KnowledgeCollection
KnowledgeContributor
KnowledgeConsumption
KnowledgeValidation
KnowledgeFlag
KnowledgeReward
```

### 5.2 KnowledgeArtifact Schema (PostgreSQL)

```sql
CREATE TABLE knowledge_artifacts (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,                        -- Multi-tenant isolation
    domain              TEXT NOT NULL CHECK (domain IN ('Arts','Commerce','Science','Technology','Administration')),
    title               TEXT NOT NULL,
    slug                TEXT UNIQUE NOT NULL,
    artifact_type       TEXT NOT NULL CHECK (artifact_type IN ('article','guide','case_study','video','code','slide','research','sop','faq','template')),
    content_body        TEXT,                                 -- For text types
    storage_path        TEXT,                                 -- MinIO path for binary types
    author_user_id      UUID NOT NULL REFERENCES users(id),
    author_role         TEXT NOT NULL,
    status              TEXT NOT NULL DEFAULT 'draft' CHECK (status IN ('draft','under_review','approved','published','deprecated','archived')),
    visibility          TEXT NOT NULL DEFAULT 'tenant' CHECK (visibility IN ('public','tenant','role_gated','belt_gated','domain_gated')),
    belt_requirement    TEXT,                                 -- e.g. 'yellow', 'green', 'black'
    trust_score         NUMERIC(5,2) DEFAULT 0,
    quality_score       NUMERIC(5,2) DEFAULT 0,
    version_number      INTEGER NOT NULL DEFAULT 1,
    parent_artifact_id  UUID REFERENCES knowledge_artifacts(id), -- For versions
    view_count          BIGINT DEFAULT 0,
    share_count         BIGINT DEFAULT 0,
    created_at          TIMESTAMPTZ DEFAULT now(),
    published_at        TIMESTAMPTZ,
    deprecated_at       TIMESTAMPTZ,
    last_validated_at   TIMESTAMPTZ,
    metadata            JSONB DEFAULT '{}'::jsonb,
    search_vector       TSVECTOR
);
```

### 5.3 KnowledgeTrustRecord Schema

```sql
CREATE TABLE knowledge_trust_records (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    artifact_id         UUID NOT NULL REFERENCES knowledge_artifacts(id),
    validator_id        UUID NOT NULL REFERENCES users(id),
    validator_role      TEXT NOT NULL,
    validation_type     TEXT NOT NULL CHECK (validation_type IN ('peer_review','mentor_review','evaluator_review','ai_check','plagiarism_check')),
    verdict             TEXT NOT NULL CHECK (verdict IN ('approved','rejected','needs_revision','flagged')),
    trust_delta         NUMERIC(5,2) NOT NULL,
    comment             TEXT,
    evidence_path       TEXT,                                 -- MinIO path for supporting docs
    created_at          TIMESTAMPTZ DEFAULT now(),
    immutable_hash      TEXT NOT NULL                        -- SHA-256 of (artifact_id + validator_id + verdict + timestamp)
);
```

### 5.4 Kafka Event Schema (knowledge domain)

```json
{
  "event_type": "knowledge.published",
  "schema_version": "1.0",
  "artifact_id": "uuid",
  "tenant_id": "uuid",
  "domain": "Technology",
  "author_user_id": "uuid",
  "artifact_type": "guide",
  "trust_score": 87.5,
  "visibility": "tenant",
  "belt_requirement": "yellow",
  "published_at": "ISO8601",
  "metadata": {}
}
```

**Registered event topics (Kafka — locked):**

| Topic | Trigger |
|---|---|
| `knowledge.draft.created` | Artifact enters draft state |
| `knowledge.submitted_for_review` | Author submits for validation |
| `knowledge.validated` | Evaluator/mentor approves |
| `knowledge.rejected` | Reviewer rejects with reason |
| `knowledge.published` | Artifact goes live |
| `knowledge.deprecated` | Artifact marked outdated |
| `knowledge.flagged` | User flags artifact for review |
| `knowledge.trust_updated` | Trust score recalculated |
| `knowledge.reward_issued` | Contributor reward triggered |
| `knowledge.bulk_ingested` | Batch import completed |

---

## ⚙️ SECTION 6 — KNOWLEDGE LIFECYCLE ENGINE (DETERMINISTIC)

```
DRAFT → SUBMITTED → UNDER_REVIEW → [APPROVED | REJECTED | NEEDS_REVISION]
                                            ↓
                                       PUBLISHED
                                            ↓
                              [DEPRECATED | ARCHIVED | VERSIONED]
```

### 6.1 State Machine Rules (Redis-enforced)

| Transition | Allowed Actor | Condition |
|---|---|---|
| DRAFT → SUBMITTED | Author (any role) | Draft passes minimum quality gate |
| SUBMITTED → UNDER_REVIEW | System (auto) | Validator assigned by OPA policy |
| UNDER_REVIEW → APPROVED | Evaluator / Mentor with review permission | Trust score ≥ 60 |
| UNDER_REVIEW → REJECTED | Evaluator / Mentor | Reason required (mandatory) |
| UNDER_REVIEW → NEEDS_REVISION | Evaluator / Mentor | Comment required |
| NEEDS_REVISION → SUBMITTED | Author | New version bump required |
| APPROVED → PUBLISHED | System (auto) or Admin | Scheduled or immediate |
| PUBLISHED → DEPRECATED | Admin / Mentor | Replacement artifact linked |
| PUBLISHED → VERSIONED | Author + Evaluator co-sign | New version replaces with redirect |
| Any → ARCHIVED | Admin Governance only | Permanent — requires audit log |

**Rule:** No state can be skipped. No state can be reversed without explicit Admin override + audit entry. Auto-promotion to PUBLISHED requires two validator approvals for belt-gated content.

### 6.2 Minimum Quality Gate (Pre-Submission Check)

```
✓ Title length: 10–120 characters
✓ Content body OR storage_path: exactly one must be non-null
✓ Domain: must match author's permitted domains (OPA check)
✓ At least 2 knowledge tags
✓ Plagiarism scan: similarity score < 25% (AI Check service)
✓ Banned content check: Wazuh pattern match
✗ FAIL any → STOP_SUBMISSION → return validation error list
```

---

## 🔐 SECTION 7 — TRUST INFRASTRUCTURE (ANTIGRAVITY CORE)

> The Trust Infrastructure is what makes this an antigravity agent — not a passive repository. It actively rewards sharing, penalizes hoarding, surfaces quality, and makes trust measurable and auditable.

### 7.1 Trust Score Formula (Locked — No Override Without Version Bump)

```
trust_score = (
    (peer_approval_count × 8)
  + (mentor_approval × 15)
  + (evaluator_approval × 20)
  + (consumption_score × 0.1)      ← normalized view+share weighted average
  + (citation_count × 5)
  - (flag_count × 10)
  - (plagiarism_similarity × 0.5)  ← % similarity as penalty factor
  - (staleness_penalty)            ← 0 if < 180 days; 5 per 30 days after
)

MAX trust_score = 100
MIN trust_score = 0 (floor, not negative)
```

**Rule:** Trust score is immutable once an artifact is ARCHIVED. Trust score is recomputed on every `knowledge.trust_updated` event. Audit log entry created on every recalculation.

### 7.2 Contributor Reputation Vector (Feeds into User Service)

```
knowledge_reputation_delta = (
  + 10 per published artifact (first approval)
  + 5  per version approved
  + 3  per citation received from another artifact
  + 2  per quality review completed (as validator)
  - 15 per plagiarism violation confirmed
  - 10 per artifact deprecated due to inaccuracy
  - 5  per flagged artifact confirmed as invalid
)
```

This delta is emitted via `user.reputation_updated` event and consumed by the User Service. It feeds the mentor reputation vector and belt eligibility check in the Belt Engine.

### 7.3 Anti-Hoarding Protocol (Antigravity Enforcement)

**Problem:** Senior users or mentors may hoard knowledge to maintain authority. This system eliminates that behavior structurally.

```
Anti-Hoarding Rules:

R1 — Mentor Knowledge Obligation
     Any mentor with belt ≥ Green who has NOT published ≥ 1 artifact
     in 90 days receives:
     → Warning notification at day 60
     → Reputation decay: -2 per 7 days after day 90
     → Flagged in Admin Governance dashboard
     → Mentor panel badge shows "Knowledge Inactive"

R2 — Evaluator Review Obligation
     Evaluators must complete ≥ 2 knowledge reviews per 30 days
     or lose review permission until next activity cycle.

R3 — Knowledge Decay Detection
     Artifacts not updated in 180+ days AND with active consumption
     trigger: auto-deprecation_candidate event → notify original author
     → if no response in 14 days → deprecate with redirect placeholder.

R4 — Domain Bottleneck Detection
     If ≥ 60% of approved artifacts in a domain come from ≤ 2 authors:
     → emit knowledge.domain_concentration_alert
     → Admin Governance is notified
     → Incentive campaign auto-triggered for that domain
```

### 7.4 Plagiarism & Anti-Theft Engine Integration

Consumes the existing **Idea Similarity & Anti-Theft Engine** (already in Ecoskiller XIV — AI & Data Infrastructure):

```
On every draft submission:
1. Extract text embedding via Embedding & Model Inference Service
2. Query Vector Database Service for top-10 similar artifacts (cosine similarity)
3. If max_similarity > 0.85 → BLOCK submission → return violation report
4. If max_similarity > 0.65 → FLAG for human review → allow draft save, block submission
5. If max_similarity < 0.65 → PASS → proceed to quality gate
6. Result stored in knowledge_trust_records (type = 'plagiarism_check')
7. Similarity score stored in artifact metadata
```

### 7.5 Immutable Audit Ledger

Every trust action generates an immutable audit record:

```sql
CREATE TABLE knowledge_audit_ledger (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    artifact_id     UUID NOT NULL,
    actor_id        UUID NOT NULL,
    action          TEXT NOT NULL,
    previous_state  TEXT,
    new_state       TEXT,
    trust_before    NUMERIC(5,2),
    trust_after     NUMERIC(5,2),
    immutable_hash  TEXT NOT NULL,  -- SHA-256(artifact_id + actor_id + action + new_state + timestamp)
    created_at      TIMESTAMPTZ DEFAULT now()
);
-- WORM policy enforced at DB level: UPDATE forbidden, DELETE forbidden
-- Row-level security: SELECT permitted; INSERT permitted; no UPDATE/DELETE policy
```

---

## 🔍 SECTION 8 — SEARCH & DISCOVERY LAYER

### 8.1 OpenSearch Index Schema

```json
{
  "index": "knowledge_artifacts",
  "mappings": {
    "properties": {
      "id":             { "type": "keyword" },
      "tenant_id":      { "type": "keyword" },
      "domain":         { "type": "keyword" },
      "title":          { "type": "text", "analyzer": "english" },
      "content_body":   { "type": "text", "analyzer": "english" },
      "artifact_type":  { "type": "keyword" },
      "tags":           { "type": "keyword" },
      "author_user_id": { "type": "keyword" },
      "author_role":    { "type": "keyword" },
      "trust_score":    { "type": "float" },
      "quality_score":  { "type": "float" },
      "belt_requirement": { "type": "keyword" },
      "status":         { "type": "keyword" },
      "visibility":     { "type": "keyword" },
      "published_at":   { "type": "date" },
      "embedding":      { "type": "knn_vector", "dimension": 768 }
    }
  }
}
```

### 8.2 Search Access Control Rules (OPA-enforced)

```
Search results are ALWAYS filtered by:
  1. tenant_id = caller's tenant_id
  2. domain IN caller's permitted_domains (from RBAC)
  3. belt_requirement ≤ caller's current_belt
  4. visibility = 'public' OR (visibility = 'tenant' AND same_tenant)
     OR (visibility = 'role_gated' AND caller_role IN allowed_roles)

FORBIDDEN:
  × Cross-tenant search results
  × Belt-gated content returned to below-belt users
  × Domain-isolated content returned to unauthorized domain
```

### 8.3 Recommendation Engine

Built on top of existing Qdrant Vector Database (from Society Architecture integration):

```
Input:  current_user_id, current_page_artifact_id (optional)
Output: [artifact_id, relevance_score, recommendation_reason] × N

Algorithm:
  1. Fetch user's knowledge consumption history (ClickHouse)
  2. Fetch user's skill vector (User Service)
  3. Build query embedding from history + skill vector
  4. Query Qdrant for nearest knowledge artifact embeddings
  5. Filter by OPA access rules
  6. Boost by: trust_score × 0.4 + recency_score × 0.3 + domain_match × 0.3
  7. Return top-10 with reason labels
  8. Cache in Redis (TTL: 30 minutes per user)
```

---

## 📊 SECTION 9 — ANALYTICS (ANTIGRAVITY MEASUREMENT)

All events → Kafka → Analytics Service → ClickHouse

### 9.1 ClickHouse Tables

```sql
-- Knowledge consumption fact table
CREATE TABLE knowledge_consumption (
    artifact_id         UUID,
    tenant_id           UUID,
    domain              String,
    consumer_user_id    UUID,
    consumer_role       String,
    action              String,  -- 'view', 'download', 'share', 'cite', 'bookmark'
    session_duration_s  UInt32,
    occurred_at         DateTime,
    date                Date MATERIALIZED toDate(occurred_at)
) ENGINE = MergeTree()
PARTITION BY date
ORDER BY (tenant_id, domain, artifact_id, occurred_at);

-- Knowledge quality distribution
CREATE TABLE knowledge_quality_snapshots (
    artifact_id     UUID,
    tenant_id       UUID,
    domain          String,
    trust_score     Float32,
    quality_score   Float32,
    view_count      UInt64,
    share_count     UInt64,
    citation_count  UInt32,
    snapshot_date   Date
) ENGINE = ReplacingMergeTree()
ORDER BY (tenant_id, artifact_id, snapshot_date);
```

### 9.2 Dashboard Metrics (Grafana)

| Metric | Description | Alert Threshold |
|---|---|---|
| Knowledge Velocity | New artifacts published per week per domain | < 3/week → warn |
| Trust Score Distribution | P25/P50/P75/P90 across tenant | P50 < 50 → warn |
| Domain Concentration Index | Herfindahl score of author distribution | > 0.60 → alert |
| Stale Artifact Rate | % artifacts > 180 days without update | > 30% → alert |
| Review Backlog | Artifacts in UNDER_REVIEW > 7 days | > 10 → alert |
| Anti-Hoarding Violations | Active mentors with 0 contributions in 90 days | > 5% → alert |
| Search Null Rate | Searches returning 0 results | > 15% → investigate |
| Plagiarism Block Rate | Submissions blocked for similarity | Spike > 5/day → alert |

---

## 🔔 SECTION 10 — NOTIFICATION POLICY

All notifications routed through existing **Notification Service** (email + SMS + push via Postfix + Jasmin + ntfy stack).

| Event | Recipient | Channel | Template |
|---|---|---|---|
| Artifact published | Tenant knowledge subscribers | Push + Email | `KSP_PUBLISHED_001` |
| Review assigned | Validator | Push + Email | `KSP_REVIEW_ASSIGNED_001` |
| Review overdue (48h) | Validator + Admin | Push | `KSP_REVIEW_OVERDUE_001` |
| Plagiarism blocked | Author | Email | `KSP_PLAGIARISM_BLOCK_001` |
| Artifact deprecated | Author + Consumers | Email | `KSP_DEPRECATED_001` |
| Anti-hoarding warning | Mentor | Push + Email | `KSP_HOARDING_WARN_001` |
| Knowledge reward issued | Contributor | Push | `KSP_REWARD_001` |
| Domain concentration alert | Admin | Email | `KSP_DOMAIN_ALERT_001` |
| Belt unlock required | Student (blocked content) | Push | `KSP_BELT_GATE_001` |

---

## 📱 SECTION 11 — UI SYSTEM LOCK

### 11.1 Flutter App Responsibilities (Operational Interface)

```
Student Portal:
  ├── Knowledge Feed (personalized, belt-gated, domain-filtered)
  ├── Artifact Viewer (article / video / code / PDF)
  ├── Search & Filter (domain, type, belt, date, trust_score)
  ├── Bookmarks & Learning Path builder
  ├── Contribution Wizard (draft → submit flow, step-by-step)
  └── My Reputation Panel (knowledge_reputation_delta history)

Mentor / Evaluator Panel:
  ├── Review Queue (artifacts pending validation)
  ├── Side-by-side Diff Viewer (version comparison)
  ├── Trust Score Breakdown Widget
  ├── Plagiarism Report Viewer
  └── Anti-Hoarding Status Badge

Admin Panel:
  ├── Domain Concentration Heatmap
  ├── Stale Artifact List (sortable by age × consumption)
  ├── Moderation Queue (flagged artifacts)
  ├── Bulk Deprecation Tool
  └── Audit Ledger Explorer
```

### 11.2 React Web (SEO Layer — Read Only)

```
Public Knowledge Pages:
  ├── /knowledge/[domain]/[slug]  — Published artifact (approved, trust ≥ 70)
  ├── /knowledge/[domain]/        — Domain index page
  ├── /knowledge/authors/[id]     — Contributor profile (public stats only)
  └── /knowledge/collections/[id] — Curated collections

Rules:
  ✓ SSR enabled, structured data (JSON-LD schema.org/Article)
  ✓ Only PUBLIC + trust ≥ 70 artifacts indexed
  ✓ No mutations on React layer
  ✗ No authentication actions on React layer
  ✗ No belt-gated content on React layer
```

### 11.3 Dashboard Composition Rule (From Master Prompt)

```
Customizable (User-controlled): 60%
  - Widget position
  - Knowledge feed ordering
  - Domain filter defaults
  - Display density (card vs list vs compact)

Fixed (System-controlled): 40%
  - Identity block with knowledge reputation badge
  - Compliance indicators (review obligations)
  - Critical alerts (hoarding warnings, plagiarism notices)
  - Trust verification badges
```

---

## 🔗 SECTION 12 — API CONTRACT REGISTRY (LOCKED)

All endpoints go through Kong → Envoy → ModSecurity → knowledge-portal-service.

```
BASE: /api/v1/knowledge

Artifacts:
  POST   /artifacts                        → create draft
  GET    /artifacts?domain=&type=&q=       → search (OPA-filtered)
  GET    /artifacts/{id}                   → get artifact (access check)
  PUT    /artifacts/{id}                   → update draft only
  POST   /artifacts/{id}/submit            → submit for review
  POST   /artifacts/{id}/publish           → admin/auto publish
  POST   /artifacts/{id}/deprecate         → deprecate with redirect
  POST   /artifacts/{id}/version           → create new version
  GET    /artifacts/{id}/versions          → version history
  GET    /artifacts/{id}/audit             → audit ledger (admin only)

Reviews:
  GET    /reviews/queue                    → validator's review queue
  POST   /reviews/{artifact_id}/verdict    → submit review verdict
  GET    /reviews/{artifact_id}/history    → all reviews for artifact

Trust:
  GET    /trust/{artifact_id}              → trust record + breakdown
  GET    /trust/contributors/{user_id}     → contributor reputation

Search:
  GET    /search?q=&domain=&type=&belt=    → full-text + semantic search
  GET    /recommendations                  → personalized feed (auth required)

Analytics:
  POST   /analytics/consume               → log consumption event
  GET    /analytics/dashboard             → admin analytics summary

Collections:
  POST   /collections                     → create curated collection
  PUT    /collections/{id}/artifacts      → add/remove artifact
  GET    /collections/{id}               → get collection
```

**Rate limits (Kong-enforced):**

| Role | Create | Read | Search |
|---|---|---|---|
| Student | 5/day | 500/day | 200/day |
| Mentor / Trainer | 20/day | unlimited | unlimited |
| Evaluator | 10/day | unlimited | unlimited |
| Admin | unlimited | unlimited | unlimited |
| Automation Agent | 100/min | 1000/min | 500/min |

---

## 🔒 SECTION 13 — SECURITY BASELINE (INHERITED + EXTENDED)

From Ecoskiller Security Baseline (Section IX of infrastructure spec):

```
Inherited Controls:
  ✓ WAF (ModSecurity) in front of ingress
  ✓ Rate limits per IP + user
  ✓ Encrypted PII
  ✓ Immutable audit logs
  ✓ Tenant isolation enforced at DB (Row-Level Security)

Knowledge-Specific Controls:
  ✓ Signed MinIO artifact URLs (TTL: 15 minutes, never permanent)
  ✓ Wazuh anomaly detection on bulk downloads (>50 artifacts in <5 min)
  ✓ Plagiarism scan on every submission (no bypass path)
  ✓ Belt-gate enforcement at API level (not UI level — UI is informational only)
  ✓ Domain isolation enforced at OPA, DB, and Search layers independently
  ✓ Author cannot approve their own artifact (enforced in state machine)
  ✓ SHA-256 content hash stored on first publication (integrity baseline)
  ✓ Trust score tampering detection: any manual DB update triggers Wazuh alert
```

---

## ⚡ SECTION 14 — DEVOPS & DEPLOYMENT (LOCKED)

### 14.1 Kubernetes Namespaces

```yaml
namespace: knowledge

Deployments:
  - knowledge-portal-service        replicas: 2 (min), 6 (max HPA)
  - knowledge-trust-engine          replicas: 2 (min), 4 (max)
  - knowledge-search-service        replicas: 2 (min), 4 (max)
  - knowledge-analytics-service     replicas: 1 (min), 3 (max)
  - knowledge-recommendation-engine replicas: 1 (min), 2 (max)
```

### 14.2 CI/CD Pipeline (Forgejo Actions — replacing GitHub Actions per Audit Report)

```yaml
# .forgejo/workflows/knowledge-portal.yml
stages:
  - contract-validate    # Validates API contract registry
  - unit-test
  - integration-test
  - security-scan        # Trivy + OPA policy lint
  - image-build          # → Harbor internal registry
  - deploy-dev
  - deploy-staging       # requires: manual approval gate
  - deploy-prod          # requires: two approvers
```

**Audit compliance:** Replaces GitHub Actions (CRITICAL issue #1 in Infrastructure Audit v8) with self-hosted Forgejo Actions. Harbor replaces GHCR. No external SaaS in CI pipeline.

### 14.3 Environment Files

```
/config/environments/
  dev.env        → localhost:8095, local PostgreSQL, local Redis
  test.env       → test namespace, isolated DB, test Kafka cluster
  staging.env    → staging namespace, staging DB, staging MinIO
  production.env → prod namespace, prod DB, Vault-injected secrets
```

**No hardcoded credentials. All secrets from HashiCorp Vault.**

---

## 📈 SECTION 15 — KAFKA EVENT CHOREOGRAPHY (FULL MAP)

```
KNOWLEDGE_SHARING_PORTAL_AGENT events feed into:

knowledge.published
  → Analytics Service     (ClickHouse ingestion)
  → Notification Service  (subscriber alerts)
  → Scoring Engine        (knowledge quality contribution)
  → Certification Engine  (milestone check)
  → User Service          (reputation delta)
  → OpenSearch            (index update)

knowledge.trust_updated
  → Analytics Service     (trust score snapshot)
  → Admin Governance      (if trust drops below 30)
  → Notification Service  (author alert if trust changes > ±15)

knowledge.flagged
  → Admin Governance      (moderation queue)
  → Notification Service  (author + admin alert)

knowledge.reward_issued
  → Billing Service       (if monetary reward configured)
  → Notification Service  (contributor push notification)
  → User Service          (reputation vector update)

knowledge.domain_concentration_alert
  → Admin Governance
  → Notification Service  (admin email)

Consumed events from other services:
  user.belt_upgraded       → recalculate content accessibility
  user.reputation_updated  → refresh contributor standing
  match.scored             → check knowledge contribution obligations
  gd.completed             → surface relevant knowledge to GD participants
  interview.completed      → push relevant guides to candidate
```

---

## 🏆 SECTION 16 — KNOWLEDGE REWARD SYSTEM

**Rule:** Reward is behavioral, not financial by default. Financial rewards require explicit Billing Service integration and tenant-level configuration.

### 16.1 Behavioral Rewards (Default — All Tenants)

| Milestone | Reward |
|---|---|
| First artifact published | `knowledge_pioneer` badge |
| 5 artifacts approved | `knowledge_contributor` badge + reputation boost |
| 10 artifacts with trust_score ≥ 80 | `knowledge_authority` badge |
| First artifact cited by another | `knowledge_cited` badge |
| Complete review of 20 artifacts | `knowledge_validator` badge |
| Zero plagiarism flags lifetime | `clean_author` badge |
| 90-day streak of contribution | `knowledge_streak` badge |

### 16.2 Belt Integration

Knowledge contribution requirements by belt level (locked in Belt Engine):

| Belt | Knowledge Obligation |
|---|---|
| White | None |
| Yellow | 1 published artifact in domain |
| Green | 3 published artifacts; 1 with trust_score ≥ 70 |
| Brown | 5 published; min 2 reviews completed |
| Black | 10 published; min 5 reviews; 0 plagiarism violations; domain_authority score ≥ 75 |

---

## 🌍 SECTION 17 — SOCIETY & FRANCHISE EXTENSION

Knowledge artifacts created in the Society/Offline Franchise layer (from ecoskiller_society_architecture.docx) are subject to additional rules:

```
Society Knowledge Rules:

R1 — Village-level coaches publishing knowledge:
     → Requires coach_service reputation_score ≥ 60
     → Territory-gated: visible within franchise boundary only (unless Admin promotes)

R2 — Offline sync:
     → Knowledge artifacts can be pre-downloaded in offline bundles
     → Bundle manifest signed with SHA-256
     → Consumption events queued locally → synced on connectivity restore

R3 — BPL-tagged student access:
     → Belt-gate waived for first 3 knowledge artifacts (policy: `knowledge_bpl_access`)
     → OPA policy path: /policies/knowledge/bpl_access_override

R4 — Scheme-compliant content tagging:
     → PMKVY / DDU-aligned artifacts tagged with scheme_metadata
     → scheme-accounting-service validates content compliance before scheme billing
```

---

## 🤖 SECTION 18 — PARENT TRUST PORTAL (READ-ONLY LAYER)

From Student-parents document integration:

```
Parent Knowledge Visibility (Read-Only — Non-Modifiable):
  ├── Child's consumed knowledge artifacts (titles + domains only)
  ├── Child's knowledge reputation score (aggregated, not detailed)
  ├── Belt milestone triggered by knowledge contribution
  ├── Review feedback received (summary, not full content)
  └── Stale engagement alerts ("No knowledge activity in 14 days")

Parent CANNOT see:
  ✗ Content body of artifacts (child's privacy)
  ✗ Other students' data
  ✗ Reviewer identities
  ✗ Trust score calculation details
  ✗ Plagiarism check internals

Parent auth: Separate Keycloak realm, parent-scoped JWT
Parent API: /api/v1/parent/knowledge/{child_id} (read-only, strict scope)
```

---

## 🧩 SECTION 19 — OBSERVABILITY (NON-OPTIONAL)

```
Metrics (Prometheus + Grafana):
  knowledge_artifacts_total{status, domain, tenant}
  knowledge_trust_score_histogram{domain}
  knowledge_review_backlog_gauge{tenant}
  knowledge_plagiarism_blocked_total{tenant}
  knowledge_search_latency_ms_histogram
  knowledge_recommendation_cache_hit_rate

Logs (Loki):
  All lifecycle state transitions
  All OPA policy decisions (allow + deny)
  All trust score changes
  All plagiarism check results

Tracing (OpenTelemetry):
  Trace: artifact submission → plagiarism check → quality gate → submit
  Trace: search query → OPA filter → OpenSearch → ranking → response
  Trace: recommendation compute → Qdrant query → OPA filter → Redis cache

Alerts:
  CRITICAL: knowledge-portal-service down > 2 min
  HIGH:     review backlog > 10 items older than 48h
  HIGH:     plagiarism spike > 10 blocks in 1 hour
  MEDIUM:   domain concentration index > 0.70
  MEDIUM:   stale artifact rate > 40%
  LOW:      search null rate > 20%
```

---

## 🔁 SECTION 20 — VERSIONING & MUTATION POLICY

```
This specification is version 1.0.

Permitted changes (Add-only via version bump):
  ✓ New event topics added to Kafka topic list
  ✓ New entity fields added (no field rename, no field delete)
  ✓ New OPA policy paths added
  ✓ New notification templates added
  ✓ New badge/reward milestones added
  ✓ New API endpoints added under /api/v2/knowledge/

FORBIDDEN without full spec reversion and new major version:
  ✗ Renaming any entity, field, topic, or endpoint
  ✗ Modifying the trust_score formula
  ✗ Removing any state from the lifecycle state machine
  ✗ Changing rate limit downward without Admin review
  ✗ Removing any security control
  ✗ Modifying the anti-hoarding rules without governance approval
  ✗ Bypassing the plagiarism gate for any role
```

---

## ✅ SECTION 21 — CONTRACT GATE INTEGRATION

```
knowledge-portal-service depends on (MANDATORY — production start blocked if missing):
  ✓ identity_ready      (Lane A — Auth Service)
  ✓ rbac_ready          (Lane A — Keycloak)
  ✓ event_schema_ready  (Lane A — Kafka Schema Registry)
  ✓ db_ready            (Lane B — PostgreSQL)
  ✓ search_ready        (Lane B — OpenSearch)
  ✓ api_contract_ready  (Lane C — Kong API Gateway)

knowledge-portal-service produces:
  → knowledge_portal_ready  (consumed by UI Lane E)
  → knowledge_trust_ready   (consumed by Intelligence Lane F)
```

**CI Gate:** Contract Validator stage in Forgejo Actions must verify all dependency contracts are green before deploying `knowledge-portal-service` to any environment.

---

## 🛑 SECTION 22 — FAILURE HANDLING MATRIX

| Failure | System Action | Recovery |
|---|---|---|
| Plagiarism service unavailable | Block submission; queue for retry; notify author | Auto-retry every 5 min × 6; manual override by Admin after 30 min |
| OpenSearch indexing failure | Artifact published; search unavailable; alert Ops | Reindex job triggered via Airflow; no data loss |
| Kafka broker unreachable | All state transitions buffered in Redis; emit on reconnect | Redis TTL: 24 hours; alert if > 5 min lag |
| Trust score service crash | Freeze trust scores; block new reviews; alert | Manual trust score recalculation job |
| MinIO unavailable | Block artifact submission; allow text-only; alert | Fallback to local temp storage; migrate on restore |
| OPA policy service down | DENY ALL access (fail closed); alert Critical | No exceptions; restart OPA pod immediately |
| PostgreSQL primary down | Read-only mode from replica; block writes; alert | Automatic failover via Patroni |

**Principle:** Always fail closed. Never degrade security to maintain availability.

---

## 🔐 SECTION 23 — AGENT SEALED DECLARATION

```
This agent specification is sealed as of v1.0 (March 2026).

Sealed by:         Ecoskiller Platform Architecture Authority
Sealed for:        KNOWLEDGE_SHARING_PORTAL_AGENT
Platform Context:  Enterprise Optimization + Trust Infrastructure — Antigravity Layer
Stack:             Self-hosted OSS on GCP/AWS Kubernetes (k3s compatible)
CI/CD:             Forgejo Actions + Harbor (per Infrastructure Audit v8 compliance)
Compliance:        Ecoskiller Master Execution Prompt v12.0 SECTION G (Production Readiness Law)
Security:          Wazuh + OPA + Vault + ModSecurity baseline enforced
Privacy:           No recording of student content; parent visibility scoped; WORM audit ledger

AGENT DEFENSE STATEMENT:
"This agent converts institutional knowledge from a hoarded, siloed,
authority-dependent resource into a deterministic, trust-scored,
structurally enforced, and measurably decaying public good —
executing antigravity on organizational entropy through protocol,
not persuasion."

EXECUTION AUTHORITY: Human declaration only.
INTERPRETATION AUTHORITY: NONE.
STATUS: FINAL · LOCKED · GOVERNED.
```

---

*End of KNOWLEDGE_SHARING_PORTAL_AGENT Specification v1.0*  
*Ecoskiller Platform — Enterprise Optimization + Trust Infrastructure — Antigravity Layer*  
*March 2026 — Internal Architecture Document — SEALED*
