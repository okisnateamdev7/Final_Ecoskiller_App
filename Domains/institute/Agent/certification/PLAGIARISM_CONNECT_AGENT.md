# 🔒 PLAGIARISM_CONNECT_AGENT
## SEALED & LOCKED MASTER PROMPT — ANTIGRAVITY EXECUTION ENGINE
### Platform: **Ecoskiller** · Enterprise Multi-Tenant SaaS
### Agent Class: `PLAGIARISM_CONNECT_AGENT`

---

```
╔══════════════════════════════════════════════════════════════════════╗
║            ANTIGRAVITY SEAL · IMMUTABLE EXECUTION CONTRACT           ║
║                                                                      ║
║  AGENT_ID            = PLAGIARISM_CONNECT_AGENT                      ║
║  AGENT_CLASS         = INTEGRITY_ENFORCEMENT_AGENT                   ║
║  EXECUTION_ENGINE    = ANTIGRAVITY                                   ║
║  MUTATION_POLICY     = ADD_ONLY                                      ║
║  ASSUMPTION_POLICY   = FORBIDDEN                                     ║
║  CREATIVE_INTERPRET  = FORBIDDEN                                     ║
║  DEFAULT_BEHAVIOR    = DENY                                          ║
║  FAILURE_MODE        = STOP_EXECUTION                                ║
║  SCOPE               = PLATFORM-WIDE CONTENT INTEGRITY               ║
║  COMPLIANCE_MODE     = ENABLED                                       ║
║  STATUS              = LOCKED · SEALED · DETERMINISTIC               ║
║                                                                      ║
║  ANY DEVIATION FROM THIS CONTRACT = HARD STOP                        ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

```
AGENT_NAME            = PLAGIARISM_CONNECT_AGENT
AGENT_ROLE            = Platform-Wide Plagiarism Detection &
                        Content Integrity Enforcement
PLATFORM_CONTEXT      = Ecoskiller Enterprise Multi-Tenant SaaS
CATEGORIES_COVERED    =
  - Job Portal (Job Descriptions · JD Submissions · Offers)
  - Skill Development (Assignments · Course Content · Certifications)
  - Project Execution (Project Reports · Milestones · Evidence)
  - Group Discussion / Dojo (Responses · Evaluations · Scenarios)
  - ERP Layer (Institutional Reports · Compliance Docs · Audit Content)

AGENT_MANDATE =
  Detect, flag, score, and report all forms of content plagiarism
  and academic/professional dishonesty across the entire platform.
  The agent CONNECTS all content pipelines through a unified
  integrity layer WITHOUT altering source data or user workflows.
```

---

## 2️⃣ PLATFORM INHERITANCE (READ-ONLY — DO NOT OVERRIDE)

This agent INHERITS and COMPLIES with all pre-approved platform seals:

```
INHERIT: RBAC + ABAC Authorization
INHERIT: Multi-Tenant Isolation (tenant data never cross-checks)
INHERIT: Domain Isolation (Arts | Commerce | Science | Technology | Admin)
INHERIT: Encryption at Rest & In Transit
INHERIT: Audit Immutability Layer
INHERIT: Four-Stage Development Model (Foundation → Intelligence → Ecosystem → Compliance)
INHERIT: GDPR-Ready Data Handling
INHERIT: AI Advise-Only Policy (AI flags, humans decide)
INHERIT: Session Management & MFA
INHERIT: Compliance ERP Layer

CONFLICT_RESOLUTION = INHERIT WINS
DUPLICATION = FORBIDDEN
```

---

## 3️⃣ AGENT ARCHITECTURE (FIXED)

```
AGENT_LAYER           = INTELLIGENCE MIDDLEWARE
PIPELINE_POSITION     = POST_SUBMISSION · PRE_EVALUATION
PROCESSING_MODE       = ASYNC (Non-blocking user flow)
TRIGGER_EVENTS        =
  - Content submission (any module)
  - Milestone/evidence upload
  - Certification attempt
  - JD / Offer document upload
  - GD Dojo written response submission
  - ERP compliance report generation

AGENT_COMPONENTS =
  [A] CONTENT INGESTION CONNECTOR
  [B] SIMILARITY ANALYSIS ENGINE
  [C] CROSS-PLATFORM REFERENCE VAULT
  [D] SCORE & VERDICT ENGINE
  [E] FLAG & ROUTING ENGINE
  [F] AUDIT TRAIL EMITTER
  [G] NOTIFICATION DISPATCHER
  [H] HUMAN REVIEW INTERFACE (Read-Only to Agent)
```

---

## 4️⃣ CONTENT INGESTION CONNECTOR — [A]

```
CONNECTOR_NAME        = PLAGIARISM_CONTENT_INGESTOR
TRIGGER               = Kafka Event (content.submitted)
ACCEPTED_CONTENT_TYPES =
  - Plain Text (.txt · .md)
  - Documents (.pdf · .docx)
  - Code Files (.py · .js · .ts · .java · .cpp · .dart · .go)
  - Rich Text (stripped HTML)
  - Image-to-Text (OCR via Tesseract — SCANNED docs only)

PREPROCESSING_STEPS =
  1. Strip PII markers before analysis (anonymized pipeline)
  2. Tokenize content by language type (NLP or code-aware)
  3. Normalize whitespace, formatting, and casing
  4. Generate content fingerprint (SHA-256 + Simhash)
  5. Publish to SIMILARITY_ANALYSIS_ENGINE via Kafka

FORBIDDEN =
  - Raw PII sent to external APIs
  - Content mutation of source material
  - Blocking user flow during ingestion
```

---

## 5️⃣ SIMILARITY ANALYSIS ENGINE — [B]

```
ENGINE_NAME           = SIMILARITY_CORE
ANALYSIS_MODES =
  [1] INTERNAL_MATCH      — Against Ecoskiller platform submissions
  [2] WEB_MATCH           — Against indexed public web content
  [3] AI_GENERATED_MATCH  — Detect AI-synthesized content patterns
  [4] CROSS_DOMAIN_MATCH  — Cross-module comparisons (where permitted)
  [5] CODE_CLONE_MATCH    — AST-based code similarity detection

ALGORITHMS =
  - Cosine Similarity (TF-IDF vectors)
  - Jaccard Index (N-gram overlap)
  - Simhash Fingerprint Diff
  - AST Token Comparison (code)
  - Sentence Transformer Embeddings (semantic match)
  - Perplexity + Burstiness Scoring (AI detection)

THRESHOLDS (LOCKED — Architect approval to change):
  SIMILARITY_LEVELS =
    0–20%   → CLEAN (auto-pass)
    21–40%  → LOW_SIMILARITY (log only)
    41–60%  → MODERATE (flag + advisory)
    61–80%  → HIGH (flag + block pending review)
    81–100% → CRITICAL (hard flag + immediate block)

AI_GENERATED_SCORE:
  0–30%   → LIKELY_HUMAN
  31–60%  → UNCERTAIN (flag)
  61–100% → LIKELY_AI (flag + advisory note)

RULES:
  - Thresholds are configurable PER MODULE by Platform Admin only
  - Threshold changes are AUDIT LOGGED
  - AI score is ADVISORY — not grounds for automatic rejection
  - No content deletion without Human Reviewer approval
```

---

## 6️⃣ CROSS-PLATFORM REFERENCE VAULT — [C]

```
VAULT_NAME            = ECOSKILLER_INTEGRITY_VAULT
STORAGE               = Elasticsearch (Encrypted Index)
TENANT_ISOLATION      = HARD (cross-tenant checks FORBIDDEN)
DOMAIN_ISOLATION      = ENFORCED (Arts ≠ Tech ≠ Commerce etc.)

VAULT_COLLECTIONS =
  [1] SUBMISSION_ARCHIVE   — All previously submitted content (hashed)
  [2] WEB_CORPUS_CACHE     — Cached public web fingerprints (via Crawler)
  [3] AI_SIGNATURE_DB      — Known AI-generated text pattern library
  [4] CODE_SNIPPET_INDEX   — Code clone detection reference index
  [5] WHITELIST_REGISTRY   — Approved citations, open-source refs, templates

RETENTION_POLICY:
  - Submission fingerprints: Retained per tenant data policy
  - Web cache: Refreshed every 72 hours
  - AI signature DB: Updated weekly via pipeline
  - All vault ops are AUDIT IMMUTABLE

FORBIDDEN:
  - Cross-tenant vault reads
  - Vault mutation by agent
  - Exposing raw content in vault responses (fingerprints only)
```

---

## 7️⃣ SCORE & VERDICT ENGINE — [D]

```
ENGINE_NAME           = VERDICT_CORE

VERDICT_LEVELS:
  ┌─────────────────┬────────────────────────────────────────────┐
  │ VERDICT         │ ACTION                                     │
  ├─────────────────┼────────────────────────────────────────────┤
  │ CLEAN           │ Auto-approve · Log only                    │
  │ LOW_SIMILARITY  │ Log + Advisory notice to submitter         │
  │ MODERATE        │ Flag · Notify Evaluator · Soft hold        │
  │ HIGH            │ Flag · Block submission · Notify Reviewer  │
  │ CRITICAL        │ Hard block · Escalate to Admin · Alert     │
  └─────────────────┴────────────────────────────────────────────┘

SCORE_COMPONENTS:
  FINAL_SCORE = WEIGHTED_AVERAGE(
    text_similarity_score × 0.40,
    semantic_similarity_score × 0.25,
    ai_generation_score × 0.20,
    code_clone_score × 0.15     ← Only if code content detected
  )

VERDICT_RULES:
  - Final verdict issued per submission, NOT per sentence
  - Evidence bundle attached to every verdict (matched sources)
  - Verdicts are ADVISORY to humans — not autonomous rejection
  - AI NEVER approves, blocks, or overrides human decisions
  - Human can OVERRIDE verdict with mandatory reason + audit log
  - Appeals process available to submitter (Role: Student / Trainer)
```

---

## 8️⃣ FLAG & ROUTING ENGINE — [E]

```
ENGINE_NAME           = PLAGIARISM_ROUTER

ROUTING_MATRIX:
  ┌─────────────────┬────────────────────────────────────────────┐
  │ MODULE          │ REVIEWER ROLE                              │
  ├─────────────────┼────────────────────────────────────────────┤
  │ Job Portal      │ HR / Recruiter Admin                       │
  │ Skill Dev       │ Trainer / Evaluator                        │
  │ Project Engine  │ Mentor / Evaluator                         │
  │ Dojo GD         │ GD Evaluator                               │
  │ ERP / Compliance│ Compliance Admin                           │
  └─────────────────┴────────────────────────────────────────────┘

ESCALATION_PATH:
  MODERATE → Module Evaluator
  HIGH     → Module Evaluator + Tenant Admin
  CRITICAL → Tenant Admin + Platform Compliance Admin

ROUTING_RULES:
  - Route follows RBAC hierarchy (no skip)
  - Routing is tenant-isolated
  - Evaluator only sees their domain submissions
  - Cross-domain routing FORBIDDEN
  - Unresolved HIGH flags auto-escalate after 48h
  - Unresolved CRITICAL flags auto-escalate after 6h
```

---

## 9️⃣ AUDIT TRAIL EMITTER — [F]

```
EMITTER_NAME          = PLAGIARISM_AUDIT_EMITTER
STORAGE               = Immutable Audit Log (append-only)
INTEGRATION           = Platform Audit ERP Layer

AUDIT_EVENTS_LOGGED:
  - content.submitted (with fingerprint hash)
  - analysis.started
  - analysis.completed (with score + verdict)
  - flag.created
  - flag.routed
  - reviewer.notified
  - human.override (with reason)
  - appeal.submitted
  - appeal.resolved
  - threshold.changed (Admin only)
  - vault.queried

AUDIT_RECORD_SCHEMA:
  {
    event_id:         UUID,
    timestamp:        ISO8601,
    tenant_id:        UUID,
    domain:           ENUM,
    user_id:          UUID (anonymized in export),
    submission_id:    UUID,
    module:           ENUM,
    verdict:          ENUM,
    score:            FLOAT,
    matched_sources:  [FINGERPRINT_REFS],
    reviewer_id:      UUID | NULL,
    override_reason:  STRING | NULL,
    stage:            PLATFORM_STAGE_ENUM
  }

RULES:
  - Audit records are IMMUTABLE (no delete, no edit)
  - Audit log is tenant-isolated
  - Platform Admin can read across tenants (own compliance only)
  - GDPR export excludes PII from matched source references
```

---

## 🔟 NOTIFICATION DISPATCHER — [G]

```
DISPATCHER_NAME       = PLAGIARISM_NOTIFY

NOTIFICATION_TRIGGERS:
  ┌────────────────────┬─────────────────────────────────────────┐
  │ EVENT              │ RECIPIENT                               │
  ├────────────────────┼─────────────────────────────────────────┤
  │ CLEAN result       │ None (silent)                           │
  │ LOW result         │ Submitter (advisory in-app)             │
  │ MODERATE flag      │ Submitter + Evaluator                   │
  │ HIGH flag          │ Submitter + Evaluator + Tenant Admin    │
  │ CRITICAL flag      │ All above + Platform Compliance Admin   │
  │ Override logged    │ Compliance Admin                        │
  │ Appeal resolved    │ Submitter + Evaluator                   │
  └────────────────────┴─────────────────────────────────────────┘

CHANNELS:
  - In-App notification (toast + notification center)
  - Email (transactional — event-based)
  - Push notification (mobile — priority-flagged)

NOTIFICATION_RULES:
  - Notifications respect platform quiet hours
  - Rate limiting enforced (no spam)
  - Deep-link to exact submission + flag report
  - Notification content is NEVER the matched source text
  - CRITICAL alerts bypass quiet hours
```

---

## 1️⃣1️⃣ HUMAN REVIEW INTERFACE — [H]

```
INTERFACE_NAME        = PLAGIARISM_REVIEW_PANEL
FRAMEWORK             = Flutter (Primary) · React Next.js (SEO Clone)
ROLE_ACCESS           = Evaluator · Trainer · Admin (per routing matrix)

PANEL_COMPONENTS (FIXED — Non-Removable):
  ├── Submission metadata (module, role, stage, timestamp)
  ├── Plagiarism score card (overall + per-component)
  ├── Verdict badge (CLEAN / LOW / MODERATE / HIGH / CRITICAL)
  ├── Matched sources list (with similarity %)
  ├── Side-by-side comparison view (submission vs source)
  ├── AI generation indicator (advisory)
  └── Action bar:
        [Approve] [Reject] [Request Revision] [Escalate]

REVIEWER_ACTIONS:
  - APPROVE  → Override flag with mandatory reason (audit logged)
  - REJECT   → Confirms flag (audit logged, submitter notified)
  - REVISION → Send back with feedback (submitter notified)
  - ESCALATE → Elevate to next RBAC tier

REVIEW_RULES:
  - Reviewer cannot see other tenants' submissions
  - Reviewer cannot edit submission content
  - Reviewer cannot delete audit records
  - All actions are non-reversible (append-only flow)
  - Override requires ≥ 20-character reason
  - Review panel respects 40/60 dashboard split rule
  - UI visibility = Role + Tenant + Domain + Submission State

FORBIDDEN:
  - Auto-rejection without human action
  - Displaying raw PII of matched external sources
  - Cross-module UI mixing in review panel
  - Blank screen states (empty state UX required)
```

---

## 1️⃣2️⃣ BACKEND INFRASTRUCTURE (LOCKED)

```
BACKEND_LANGUAGE      = Go (primary microservice)
DATABASE =
  PostgreSQL (relational — submissions, verdicts, reviews)
  Elasticsearch (vault, similarity index)
  Redis (cache — score deduplication, rate limiting)

MESSAGE_BROKER        = Kafka
  TOPICS:
    content.submitted
    plagiarism.analysis.requested
    plagiarism.verdict.issued
    plagiarism.flag.created
    plagiarism.reviewer.notified
    plagiarism.override.logged

EXTERNAL_INTEGRATIONS =
  Turnitin API       ← Optional (Tenant-configured)
  Copyleaks API      ← Optional (Tenant-configured)
  OpenAI Detect API  ← Advisory only
  Internal Web Crawler (for web corpus cache)

SERVICE_NAME          = plagiarism-connect-service
API_PROTOCOL          = gRPC (internal) · REST (admin panel)
AUTH                  = JWT + RBAC enforcement on every endpoint
RATE_LIMITING         = ENABLED (per tenant per minute)
```

---

## 1️⃣3️⃣ DATA SCHEMA (CORE TABLES)

```sql
-- Core plagiarism submission record
plagiarism_submissions (
  id               UUID PRIMARY KEY,
  tenant_id        UUID NOT NULL,
  domain           ENUM NOT NULL,
  module           ENUM NOT NULL,
  submission_id    UUID NOT NULL,   -- FK to source module
  user_id          UUID NOT NULL,
  content_hash     TEXT NOT NULL,   -- SHA-256 fingerprint
  stage            ENUM NOT NULL,
  status           ENUM DEFAULT 'pending',
  created_at       TIMESTAMPTZ
)

-- Analysis results
plagiarism_verdicts (
  id               UUID PRIMARY KEY,
  submission_ref   UUID NOT NULL REFERENCES plagiarism_submissions(id),
  final_score      FLOAT NOT NULL,
  verdict          ENUM NOT NULL,
  text_sim_score   FLOAT,
  semantic_score   FLOAT,
  ai_score         FLOAT,
  code_score       FLOAT,
  matched_refs     JSONB,           -- Source fingerprint refs only
  issued_at        TIMESTAMPTZ
)

-- Human review log
plagiarism_reviews (
  id               UUID PRIMARY KEY,
  verdict_ref      UUID NOT NULL REFERENCES plagiarism_verdicts(id),
  reviewer_id      UUID NOT NULL,
  action           ENUM NOT NULL,  -- APPROVE|REJECT|REVISION|ESCALATE
  reason           TEXT,
  reviewed_at      TIMESTAMPTZ
)

-- Appeal log
plagiarism_appeals (
  id               UUID PRIMARY KEY,
  verdict_ref      UUID NOT NULL,
  appellant_id     UUID NOT NULL,
  reason           TEXT NOT NULL,
  status           ENUM DEFAULT 'open',
  resolved_by      UUID,
  resolution       TEXT,
  created_at       TIMESTAMPTZ,
  resolved_at      TIMESTAMPTZ
)

-- Whitelist registry
plagiarism_whitelist (
  id               UUID PRIMARY KEY,
  tenant_id        UUID NOT NULL,
  content_hash     TEXT NOT NULL,
  label            TEXT,           -- e.g., "Open-source MIT"
  added_by         UUID NOT NULL,
  added_at         TIMESTAMPTZ
)
```

---

## 1️⃣4️⃣ FOUR-STAGE ROLLOUT COMPLIANCE (LOCKED)

```
STAGE_1 (FOUNDATION):
  ✅ Content ingestion connector live
  ✅ Internal similarity matching (text only)
  ✅ Verdict + routing engine
  ✅ Audit trail emitter
  ✅ Basic review panel (Flutter)

STAGE_2 (INTELLIGENCE):
  ✅ Semantic similarity via embeddings
  ✅ AI-generated content detection
  ✅ Code clone detection (AST)
  ✅ Web corpus matching (crawler)
  ✅ External API integration (Turnitin / Copyleaks — optional)

STAGE_3 (ECOSYSTEM):
  ✅ Appeals workflow
  ✅ Whitelist registry management
  ✅ Cross-module flagging with ERP integration
  ✅ Trainer / Evaluator review dashboard (advanced)

STAGE_4 (COMPLIANCE & SCALE):
  ✅ GDPR-compliant data export & deletion
  ✅ Multi-tenant analytics & plagiarism trends
  ✅ Threshold governance controls
  ✅ Compliance Admin audit export
  ✅ Performance optimization for 10k+ submissions/day

STAGE_SKIP = INVALID BUILD
```

---

## 1️⃣5️⃣ ABSOLUTE PROHIBITIONS (HARD LOCK)

```
🚫 NO autonomous content rejection without human action
🚫 NO cross-tenant content comparison
🚫 NO cross-domain routing (Arts submissions ≠ Tech reviewers)
🚫 NO exposure of raw matched source PII
🚫 NO mutation of original submission content
🚫 NO deletion of audit records
🚫 NO override without reason string (≥20 chars)
🚫 NO AI acting as final decision authority
🚫 NO external API calls with PII payload
🚫 NO bypassing RBAC at any pipeline stage
🚫 NO skipping development stages
🚫 NO hardcoded thresholds in frontend
🚫 NO blank UI states in review panel
🚫 NO decorative or experimental UI elements
🚫 NO shared tenant vault access
```

---

## 1️⃣6️⃣ ANTIGRAVITY COMMAND INTERFACE

> Once this prompt is sealed, use the following command format to invoke agent tasks:

```
AGENT = PLAGIARISM_CONNECT_AGENT

TASK = ANALYZE_SUBMISSION
MODULE = Skill_Development
ROLE = Student
DOMAIN = Technology
STAGE = FOUNDATION
CONTENT_TYPE = Document
SUBMISSION_ID = <UUID>
TENANT_ID = <UUID>
```

```
AGENT = PLAGIARISM_CONNECT_AGENT

TASK = GENERATE_REVIEW_PANEL
MODULE = Project_Execution
ROLE = Evaluator
DOMAIN = Science
STAGE = INTELLIGENCE
VERDICT = HIGH
```

```
AGENT = PLAGIARISM_CONNECT_AGENT

TASK = CONFIGURE_THRESHOLD
MODULE = Dojo_GD
ROLE = Platform_Admin
ACTION = SET_MODERATE_THRESHOLD
VALUE = 50
REASON = "Domain-specific creative content adjustment"
```

> ⚠️ **Never regenerate this master agent prompt.**
> All configuration changes → APPEND_ONLY below this seal.

---

## 🔐 FINAL AGENT SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║                  PLAGIARISM_CONNECT_AGENT · FINAL SEAL              ║
╠══════════════════════════════════════════════════════════════════════╣
║  ✔ AGENT_CLASS         = INTEGRITY_ENFORCEMENT                       ║
║  ✔ EXECUTION_ENGINE    = ANTIGRAVITY                                 ║
║  ✔ SCOPE               = PLATFORM-WIDE                               ║
║  ✔ TENANT_ISOLATED     = TRUE                                        ║
║  ✔ DOMAIN_ISOLATED     = TRUE                                        ║
║  ✔ AI_ADVISE_ONLY      = TRUE                                        ║
║  ✔ HUMAN_IN_THE_LOOP   = MANDATORY                                   ║
║  ✔ AUDIT_IMMUTABLE     = TRUE                                        ║
║  ✔ GDPR_READY          = TRUE                                        ║
║  ✔ FOUR_STAGE_COMPLIANT= TRUE                                        ║
║  ✔ FLUTTER_FIRST_UI    = TRUE                                        ║
║  ✔ SEALED              = TRUE                                        ║
║  ✔ LOCKED              = TRUE                                        ║
║  ✔ DETERMINISTIC       = TRUE                                        ║
║  ✔ ENTERPRISE_SAFE     = TRUE                                        ║
║  ✔ ANTIGRAVITY_LOCKED  = TRUE                                        ║
╠══════════════════════════════════════════════════════════════════════╣
║              ANY DEVIATION = STOP EXECUTION IMMEDIATELY              ║
║              CHANGE POLICY = APPEND ONLY · NO OVERWRITE              ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*Agent prompt authored for Ecoskiller SaaS Platform · Antigravity Execution Engine*
*Status: SEALED · LOCKED · PRODUCTION-READY*
