# 🔒 IDEA_VISIBILITY_CONTROL_AGENT
## `IVCA-v1.0.0` · Ecoskiller Antigravity · Intelligence & Innovation Core

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║         ECOSKILLER ANTIGRAVITY — ENTERPRISE AGENT REGISTRY                  ║
║                  IDEA_VISIBILITY_CONTROL_AGENT                               ║
║                       IVCA-v1.0.0                                            ║
╠══════════════════════════════════════════════════════════════════════════════╣
║  STATUS         : SEALED · LOCKED · GOVERNED · DETERMINISTIC                ║
║  MUTATION       : ADD-ONLY via version bump                                  ║
║  INTERPRETATION : FORBIDDEN                                                  ║
║  ASSUMPTION     : FORBIDDEN — Agent HALTS on missing spec                   ║
║  DEFAULT        : DENY VISIBILITY — Idea not surfaced unless ALL gates pass ║
║  FAILURE MODE   : STOP → REPORT → NO PARTIAL OUTPUT                         ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 📋 TABLE OF CONTENTS

| # | Section |
|---|---------|
| 1 | Agent Identity |
| 2 | Why This Agent Exists — Platform Context |
| 3 | Purpose Declaration |
| 4 | Idea Visibility Taxonomy |
| 5 | Input Contract (Strict) |
| 6 | Output Contract (Strict) |
| 7 | Visibility Decision Engine — State Machine |
| 8 | ML / AI Logic Layer |
| 9 | Originality & Similarity Engine |
| 10 | Scalability Design |
| 11 | Security Enforcement |
| 12 | Audit & Traceability |
| 13 | Failure Policy |
| 14 | Inter-Agent Dependency Map |
| 15 | Passive Intelligence Compatibility |
| 16 | Innovation Economy Compatibility |
| 17 | Growth Engine Hook |
| 18 | Performance Monitoring |
| 19 | Versioning Policy |
| 20 | Tech Stack Binding |
| 21 | Non-Negotiable Rules |
| 22 | Agent Seal |

---

---

## 1️⃣ AGENT IDENTITY

> **MANDATORY — ZERO OMISSIONS — ZERO ASSUMPTIONS**

```
AGENT_NAME             = IDEA_VISIBILITY_CONTROL_AGENT
AGENT_ID               = IVCA-v1.0.0
SYSTEM_ROLE            = Idea Visibility Authority — Control · Score · Gate · Reveal · Protect · Audit
PRIMARY_DOMAIN         = Lane F (Intelligence — Discovery, AI Explainability)
                         + Lane D (Governance — Moderation, Reputation)
                         + Lane C (Core Services — Marketplace, Innovation Economy)
EXECUTION_MODE         = Deterministic + Validated
                         Identical Input → Identical Visibility Decision → Identical Output
DATA_SCOPE             = Tenant-Isolated READ on idea metadata, creator profiles, similarity corpus
                         WRITE to idea_visibility_store (own tenant only)
                         APPEND to audit trail (immutable)
TENANT_SCOPE           = Strict Zero-Trust Multi-Tenant Isolation
                         No cross-tenant idea exposure under any condition
FAILURE_POLICY         = HALT on Ambiguity
                         DENY on Fraud Signal
                         SUPPRESS on Confidence Below Threshold
                         LOG All Operations Without Exception
SECURITY_MODEL         = Zero-Trust · RBAC + ABAC · Content Hash Sealing · Append-Only Audit
ML_WEIGHT              = 75% Traditional ML
                         (originality scoring, similarity detection, quality classification,
                          visibility ranking, abuse pattern detection)
AI_WEIGHT              = 25% LLM Semantic Assist
                         (originality explanation, similarity narrative, quality feedback generation)
ASSUMPTION_FILLING     = FORBIDDEN
DEFAULT_BEHAVIOR       = DENY VISIBILITY
                         Idea is NOT visible to any audience unless ALL eligibility gates pass
```

---

---

## 2️⃣ WHY THIS AGENT EXISTS — PLATFORM CONTEXT

Ecoskiller Antigravity operates a full **Innovation Economy** layer across its 300 user types — from School students and Freelance AI engineers to Startup founders, Government skill officers, and Ecosystem strategists. Users across all categories submit **ideas, innovations, project concepts, course designs, research proposals, creative scenarios, business plans, and original content** to the platform.

Without a centralized, deterministic Idea Visibility Control system, the following failure modes emerge:

- **Duplicate ideas get equal exposure** as original ones, destroying incentive to innovate
- **Low-quality, spammy, or abusive ideas** pollute discovery feeds across all 300 user types
- **Plagiarized ideas** get surfaced before originals, creating royalty and legal risk
- **Ideas shared prematurely** expose creators to IP theft before protection mechanisms activate
- **Sensitive institutional ideas** (from institutes, companies, government programs) leak across tenant boundaries
- **Minors or unverified users** get idea visibility without identity gate — creating safety and compliance risk
- **Economic abuse** — bulk-submitting low-effort ideas to game leaderboards or extract rewards without merit

The `IDEA_VISIBILITY_CONTROL_AGENT` eliminates all these failure modes by acting as the **single, ML-governed, cryptographically-audited authority** that controls what ideas are visible, to whom, at what access level, and when.

---

---

## 3️⃣ PURPOSE DECLARATION

### PROBLEM THIS AGENT SOLVES

Controls the full visibility lifecycle of every idea submitted to Ecoskiller Antigravity across all domains (Arts, Commerce, Science, Technology, Administration) and all 300 user types — from first submission through scoring, gating, publishing, protecting, monetizing, and archiving.

### WHAT INPUT IT CONSUMES

| Input Category | Description |
|---|---|
| Idea Submission Payload | Raw idea content, domain, creator_id, tenant_id, submission_type |
| Creator Profile Signals | Verification status, reputation score, belt level, prior idea history |
| Originality Signals | Similarity scores from COPY_DETECTION_ENGINE, IDEA_DNA_AGENT vectors |
| Quality Signals | Content quality classification from ML quality model |
| Fraud Signals | Abuse risk score from FRAUD_DETECTION_AGENT |
| Audience Request | Who is requesting visibility (role, domain, clearance, relationship to creator) |
| Compliance Signals | Domain compliance gate, tenant boundary flags, minor protection flags |
| Market Signals | Demand alignment score, trending topic relevance from MARKET_SIGNAL_AGENT |

### WHAT OUTPUT IT PRODUCES

| Output | Description |
|---|---|
| Visibility Decision | `ENUM: PUBLIC · COMMUNITY · RESTRICTED · PROTECTED · SUPPRESSED · FLAGGED` |
| Audience Matrix | Exact list of role types permitted to see this idea at each access level |
| Originality Certificate | Signed originality score + similarity hash for ROYALTY_ENGINE |
| Quality Score | Composite quality rating (0.0–1.0) with explanation tokens |
| Visibility Expiry | Timestamp when visibility tier is re-evaluated |
| Audit Record | Immutable append-only log of every visibility decision |
| Next Event Triggers | Downstream events to NOTIFICATION_AGENT, ROYALTY_ENGINE, RANKING_AGENT |

### UPSTREAM AGENTS (feeds IVCA)

`IDEA_DNA_AGENT` · `COPY_DETECTION_ENGINE` · `FRAUD_DETECTION_AGENT` · `VERIFICATION_AGENT` · `SCORING_ENGINE` · `COMPLIANCE_AGENT` · `MARKET_SIGNAL_AGENT` · `RBAC_AGENT` · `LICENSE_GENERATION_AGENT`

### DOWNSTREAM AGENTS (consumes IVCA output)

`ROYALTY_ENGINE` · `NOTIFICATION_AGENT` · `RANKING_AGENT` · `XP_ENGINE` · `FEATURE_STORE_AGENT` · `OBSERVABILITY_AGENT` · `BUSINESS_MATCHING_AGENT` · `TALENT_MARKETPLACE_AGENT` · `COPY_DETECTION_ENGINE` (feedback loop)

---

---

## 4️⃣ IDEA VISIBILITY TAXONOMY

> **All 6 visibility states are locked. No agent or human may create states outside this taxonomy without a governance board approved version bump.**

### VISIBILITY STATES

```
┌─────────────────┬──────────────────────────────────────────────────────────────────┐
│ VISIBILITY STATE │ MEANING & AUDIENCE                                               │
├─────────────────┼──────────────────────────────────────────────────────────────────┤
│ PUBLIC          │ Visible to ALL platform users, SEO-indexed React web layer,       │
│                 │ employer discovery, talent marketplace, anonymous visitors.        │
│                 │ Requires: quality ≥ 0.65, originality ≥ 0.55, fraud < 0.30,      │
│                 │ creator verified, compliance gate passed.                          │
├─────────────────┼──────────────────────────────────────────────────────────────────┤
│ COMMUNITY       │ Visible only to verified platform members in same domain.         │
│                 │ Not SEO-indexed. Not externally discoverable.                     │
│                 │ Requires: quality ≥ 0.45, originality ≥ 0.40, fraud < 0.50,      │
│                 │ creator identity check passed.                                    │
├─────────────────┼──────────────────────────────────────────────────────────────────┤
│ RESTRICTED      │ Visible only within the creator's tenant (institute/company).     │
│                 │ Role-gated within tenant. ABAC grant required for cross-role view. │
│                 │ Requires: quality ≥ 0.30, fraud < 0.60.                           │
├─────────────────┼──────────────────────────────────────────────────────────────────┤
│ PROTECTED       │ Visible ONLY to creator + explicitly granted collaborators.       │
│                 │ Idea hash sealed with timestamp for IP protection.                │
│                 │ No scoring, no discovery, no indexing.                            │
│                 │ Assigned when: originality_check_pending OR creator_requested.    │
├─────────────────┼──────────────────────────────────────────────────────────────────┤
│ SUPPRESSED      │ Not visible to anyone including creator on discovery feeds.       │
│                 │ Exists in DB for audit. Assigned when: fraud ≥ 0.65 OR           │
│                 │ quality < 0.20 OR similarity_to_existing > 0.90 (near-duplicate). │
├─────────────────┼──────────────────────────────────────────────────────────────────┤
│ FLAGGED         │ Idea exists but is under governance review.                       │
│                 │ Temporarily invisible during review window.                       │
│                 │ Assigned when: abuse_report_count ≥ 3 OR                         │
│                 │ GOVERNANCE_BOARD review triggered. Human decision required        │
│                 │ to transition out of FLAGGED state.                               │
└─────────────────┴──────────────────────────────────────────────────────────────────┘
```

### VISIBILITY BY IDEA TYPE × USER TYPE MATRIX

```
┌──────────────────────────┬─────────┬───────────┬────────────┬───────────┬──────────────┐
│ IDEA TYPE                │ PUBLIC  │ COMMUNITY │ RESTRICTED │ PROTECTED │ MAX ELIGIBLE │
├──────────────────────────┼─────────┼───────────┼────────────┼───────────┼──────────────┤
│ Student Innovation       │ ✓       │ ✓         │ ✓          │ ✓         │ PUBLIC       │
│ Trainer Course Concept   │ ✓       │ ✓         │ ✓          │ ✓         │ PUBLIC       │
│ Corporate R&D Idea       │ ✗       │ ✗         │ ✓          │ ✓         │ RESTRICTED   │
│ Government Policy Draft  │ ✗       │ ✗         │ ✓          │ ✓         │ RESTRICTED   │
│ Freelancer Pitch         │ ✓       │ ✓         │ ✓          │ ✓         │ PUBLIC       │
│ Dojo Scenario Concept    │ ✓       │ ✓         │ ✓          │ ✓         │ PUBLIC       │
│ Research Proposal        │ ✗       │ ✓         │ ✓          │ ✓         │ COMMUNITY    │
│ AI Agent Blueprint       │ ✗       │ ✗         │ ✓          │ ✓         │ RESTRICTED   │
│ Abusive / Spam Idea      │ ✗       │ ✗         │ ✗          │ ✗         │ SUPPRESSED   │
└──────────────────────────┴─────────┴───────────┴────────────┴───────────┴──────────────┘
```

---

---

## 5️⃣ INPUT CONTRACT (STRICT)

> **No null tolerance · Reject malformed data · Log all validation failures · Halt on ambiguity**

### IDEA SUBMISSION INPUT SCHEMA

```json
{
  "required_fields": [
    "request_id",          // UUID v4 — idempotency anchor
    "tenant_id",           // UUID — strict isolation boundary
    "creator_id",          // UUID — verified IAM user ID
    "idea_id",             // UUID — assigned at submission, immutable
    "domain",              // ENUM: Arts | Commerce | Science | Technology | Administration
    "idea_type",           // ENUM: INNOVATION | COURSE_CONCEPT | RESEARCH | PITCH | SCENARIO | POLICY | AI_BLUEPRINT | OTHER
    "content_hash",        // SHA-256 of idea content at submission time — immutable seal
    "submission_timestamp" // ISO 8601 UTC
  ],

  "optional_fields": [
    "title",               // String max 200 chars
    "description",         // String max 5000 chars
    "tags",                // Array<String> max 10 tags
    "collaborator_ids",    // Array<UUID> — users granted PROTECTED visibility
    "visibility_requested",// ENUM — creator preference (may be overridden by ML gates)
    "ip_protection_flag",  // Boolean — if true, idea sealed in PROTECTED until similarity check completes
    "external_ref",        // String — for government program or institutional reference codes
    "rubric_domain_ref"    // UUID — links to Dojo rubric if idea is scenario-based
  ],

  "validation_rules": [
    "request_id must be UUID v4",
    "content_hash must be SHA-256 hex string (64 chars)",
    "domain must be in approved DOMAIN_REGISTRY",
    "idea_type must be in approved IDEA_TYPE_REGISTRY",
    "creator_id must exist in IAM registry (Keycloak verified)",
    "submission_timestamp must be within ±5 minutes of server UTC (replay attack prevention)",
    "tags array max 10 items, each max 50 chars, no HTML",
    "description max 5000 chars, sanitized — no executable content",
    "collaborator_ids: all UUIDs must exist in IAM and belong to same tenant"
  ],

  "security_checks": [
    "JWT token validation via Kong API Gateway",
    "tenant_id must match JWT claim tenant_id",
    "creator_id must have IDEA:SUBMIT permission in RBAC registry",
    "Rate limit: 20 idea submissions per day per creator_id",
    "Fraud risk score from FRAUD_DETECTION_AGENT must be < 0.65 at submission",
    "Request HMAC-SHA256 signature must validate against tenant secret",
    "Content sanitization: strip all HTML, scripts, executable patterns"
  ],

  "domain_checks": [
    "RESTRICTED ideas: corporate R&D and AI_BLUEPRINT types cannot be promoted to PUBLIC via visibility_requested",
    "GOVERNMENT policy ideas: require program_code validation and compliance gate",
    "Minor creators (under 18 flag in IAM): max visibility = COMMUNITY regardless of scores",
    "Unverified creators: max visibility = RESTRICTED regardless of scores"
  ]
}
```

### VISIBILITY QUERY INPUT SCHEMA

> When any user/agent queries whether they can see a specific idea:

```json
{
  "required_fields": [
    "request_id",       // UUID v4
    "idea_id",          // UUID — idea being queried
    "requester_id",     // UUID — who is asking
    "tenant_id",        // UUID — requester's tenant
    "requester_role",   // String — from RBAC registry
    "query_timestamp"   // ISO 8601 UTC
  ],
  "optional_fields": [
    "requester_domain", // for domain-gated visibility checks
    "purpose",          // ENUM: DISCOVERY | COLLABORATION | HIRING | EVALUATION | AUDIT
    "context_ref"       // UUID — links query to a specific matching session or job listing
  ]
}
```

---

---

## 6️⃣ OUTPUT CONTRACT (STRICT)

> **Every output must include: Visibility Decision · Confidence Score · Model Version · Audit Reference · Next Event Triggers**

```json
{
  "result_object": {
    "idea_id":              "UUID",
    "creator_id":           "UUID",
    "tenant_id":            "UUID — isolation boundary",
    "visibility_state":     "ENUM: PUBLIC | COMMUNITY | RESTRICTED | PROTECTED | SUPPRESSED | FLAGGED",
    "audience_matrix": {
      "PUBLIC":             "Array<role_type> — which roles can see at PUBLIC level",
      "COMMUNITY":          "Array<role_type> — which roles can see at COMMUNITY level",
      "RESTRICTED":         "Array<uuid> — specific user IDs (collaborators + tenant admins)",
      "can_requester_see":  "Boolean — direct answer for VISIBILITY QUERY requests"
    },
    "scores": {
      "originality_score":  "Float 0.0–1.0 — uniqueness vs. corpus",
      "similarity_score":   "Float 0.0–1.0 — closeness to most similar existing idea",
      "quality_score":      "Float 0.0–1.0 — composite content quality",
      "fraud_risk_score":   "Float 0.0–1.0 — from FRAUD_DETECTION_AGENT",
      "market_alignment":   "Float 0.0–1.0 — demand relevance score"
    },
    "originality_certificate": {
      "certificate_id":     "UUID — immutable",
      "content_hash":       "SHA-256 of idea at time of certification",
      "similarity_hash":    "LSH (Locality-Sensitive Hash) for near-duplicate detection",
      "originality_score":  "Float 0.0–1.0",
      "issued_at":          "ISO 8601 UTC",
      "signature":          "Ed25519 signature over [idea_id + content_hash + originality_score + issued_at]"
    },
    "visibility_expiry":    "ISO 8601 UTC — when visibility tier is re-evaluated",
    "explanation_tokens":   "Array<String> — human-readable reasons for the decision",
    "royalty_eligible":     "Boolean — whether idea qualifies for royalty tracking"
  },

  "confidence_score":       "Float 0.0–1.0 — ensemble model confidence in visibility decision",
  "model_version":          "String — e.g. IVCA-visibility-v1.0.0-xgb-20250101",
  "audit_reference":        "UUID — pointer to immutable audit log entry",
  "next_trigger_event":     [
    "VISIBILITY_ASSIGNED_EVENT",
    "ORIGINALITY_CERTIFIED_EVENT",
    "ROYALTY_ELIGIBILITY_EVENT",
    "RANK_UPDATE_EVENT",
    "NOTIFICATION_DISPATCH_EVENT"
  ]
}
```

---

---

## 7️⃣ VISIBILITY DECISION ENGINE — STATE MACHINE

> **All transitions are deterministic. No ambiguous transitions. Every transition logged.**

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                    VISIBILITY DECISION FLOW                                    │
└────────────────────────────────────────────────────────────────────────────────┘

[SUBMITTED] ──► Gate 1: Security & Fraud Check
                  fraud_risk ≥ 0.65 ──────────────────────────────► [SUPPRESSED]
                  fraud_risk < 0.65 ──► Gate 2: Content Quality Check
                                          quality < 0.20 ──────────► [SUPPRESSED]
                                          quality ≥ 0.20 ──► Gate 3: Similarity Check
                                                                similarity > 0.90 ──► [SUPPRESSED]
                                                                0.70–0.90 ─────────► [PROTECTED]
                                                                                       (pending review)
                                                                < 0.70 ──► Gate 4: IP Protection Flag
                                                                             ip_flag=true ──► [PROTECTED]
                                                                             ip_flag=false ──► Gate 5: Creator Verification
                                                                                                unverified ──► [RESTRICTED]
                                                                                                minor flag ──► max COMMUNITY
                                                                                                verified ──► Gate 6: Quality & Originality Scoring
                                                                                                               quality ≥ 0.65 + orig ≥ 0.55 ──► [PUBLIC]
                                                                                                               quality ≥ 0.45 + orig ≥ 0.40 ──► [COMMUNITY]
                                                                                                               quality ≥ 0.30 ──────────────► [RESTRICTED]
                                                                                                               quality < 0.30 ──────────────► [SUPPRESSED]
```

### STATE TRANSITION RULES

| FROM | EVENT | TO | Human Required? |
|---|---|---|---|
| `PROTECTED` | Similarity check completes, score < 0.70 | Re-evaluate → PUBLIC/COMMUNITY/RESTRICTED | No |
| `PROTECTED` | Similarity check completes, 0.70–0.90 | `FLAGGED` for governance review | No |
| `COMMUNITY` | Quality score improves ≥ 0.65 + originality ≥ 0.55 on re-evaluation | `PUBLIC` | No |
| `PUBLIC` | Abuse reports ≥ 3 from distinct verified users | `FLAGGED` | No |
| `PUBLIC` | Fraud risk rises ≥ 0.65 (re-check event) | `SUPPRESSED` | No |
| `RESTRICTED` | Creator completes verification | Re-evaluate for upgrade | No |
| `FLAGGED` | Governance board clears — no violation found | Previous state restored | **YES** |
| `FLAGGED` | Governance board confirms violation | `SUPPRESSED` (permanent) | **YES** |
| `SUPPRESSED` | Re-appeal by creator | `FLAGGED` for review | **YES — appeal workflow** |
| `SUPPRESSED` (permanent) | ANY | `SUPPRESSED` — terminal state | **NOT PERMITTED** |

> 🔒 **`SUPPRESSED (permanent)` is a terminal state. No agent, model, or admin may restore visibility without full governance board quorum vote and documented audit entry.**

---

---

## 8️⃣ ML / AI LOGIC LAYER

### 8A — PRIMARY ML LAYER (75%)

#### MODEL DEFINITIONS

```
┌─────────────────────────────┬──────────────────────────────────────────────────────────────────┐
│ MODEL NAME                  │ SPECIFICATION                                                    │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ VISIBILITY_CLASSIFIER       │ XGBoost Multi-Class (6 classes = 6 visibility states)            │
│                             │ Primary decision model. Confidence threshold ≥ 0.55 required.    │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ QUALITY_SCORER              │ Gradient Boosted Regressor → Float 0.0–1.0                       │
│                             │ Features: content length, structural coherence, tag relevance,    │
│                             │ domain alignment, novelty of framing, referenced sources.         │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ ORIGINALITY_SCORER          │ Cosine Similarity (FastText embeddings) + TF-IDF on corpus       │
│                             │ originality_score = 1 - max_cosine_similarity_to_any_corpus_idea │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ SIMILARITY_DETECTOR         │ Locality-Sensitive Hashing (LSH) + MinHash                      │
│                             │ Produces similarity_hash and nearest_neighbour_distance           │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ ABUSE_PATTERN_DETECTOR      │ Isolation Forest (unsupervised)                                  │
│                             │ Detects: bulk submission patterns, idea farming, copy-paste       │
│                             │ flooding, coordinated idea boosting rings.                        │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ MARKET_ALIGNMENT_SCORER     │ XGBoost Regressor → Float 0.0–1.0                               │
│                             │ Features: domain trending signals, skill demand index,            │
│                             │ employer interest proxy, seasonal topic relevance.                │
├─────────────────────────────┼──────────────────────────────────────────────────────────────────┤
│ VISIBILITY_UPGRADE_PREDICTOR│ Logistic Regression → P(upgrade in next 30d)                    │
│                             │ Predicts which RESTRICTED/COMMUNITY ideas will earn PUBLIC.       │
│                             │ Feeds proactive guidance to creator via NOTIFICATION_AGENT.       │
└─────────────────────────────┴──────────────────────────────────────────────────────────────────┘
```

#### EXPLICIT FEATURES USED

```
VISIBILITY_CLASSIFIER features:
  content_length_chars, domain_encoded, idea_type_encoded,
  creator_reputation_score, creator_verification_level, creator_belt_level_encoded,
  quality_score (from QUALITY_SCORER), originality_score (from ORIGINALITY_SCORER),
  similarity_score (from SIMILARITY_DETECTOR), fraud_risk_score (from FRAUD_DETECTION_AGENT),
  market_alignment_score, prior_ideas_published_count, prior_ideas_suppressed_count,
  account_age_days, ip_protection_flag, idea_type_risk_tier,
  abuse_report_count_7d, engagement_rate_prior_ideas

QUALITY_SCORER features:
  word_count, sentence_count, avg_sentence_length, vocabulary_richness,
  heading_structure_flag, domain_keyword_density, tag_relevance_cosine,
  external_reference_count, structural_completeness_score,
  flesch_kincaid_readability, domain_specific_term_ratio

MARKET_ALIGNMENT_SCORER features:
  skill_demand_index_30d (from MARKET_SIGNAL_AGENT), trending_topic_overlap,
  employer_search_term_match, domain_hiring_velocity_90d, seasonal_index,
  similar_ideas_engagement_rate_90d
```

#### TRAINING & GOVERNANCE

```
TRAINING_FREQUENCY:
  VISIBILITY_CLASSIFIER:    Monthly full retrain + weekly incremental
  QUALITY_SCORER:           Monthly full retrain
  ORIGINALITY_SCORER:       Corpus updated daily (new ideas added to reference corpus)
  ABUSE_PATTERN_DETECTOR:   Weekly full retrain
  MARKET_ALIGNMENT_SCORER:  Weekly (market signals change rapidly)
  UPGRADE_PREDICTOR:        Monthly

DRIFT_DETECTION:
  PSI (Population Stability Index) monitored on all top-10 features per model
  Alert threshold: PSI > 0.20
  Critical threshold: PSI > 0.30 → IMMEDIATE RETRAIN + ALERT to Model Governance Manager
  VISIBILITY_CLASSIFIER accuracy degradation > 4% on monthly eval set → RETRAIN_NEEDED event
  ORIGINALITY corpus staleness: if new idea count exceeds 10,000 since last corpus refresh → REFRESH

VERSION_CONTROL:
  Format: IVCA-{component}-v{major}.{minor}.{patch}-{YYYYMMDD}
  All model versions immutable in model registry
  Every output references model_version that produced it
  No overwrite permitted — new version only
```

---

### 8B — AI ASSIST LAYER (25%)

```
AI_USAGE_SCOPE:
  1. Generate explanation_tokens for visibility decision (why PUBLIC vs COMMUNITY)
  2. Generate originality certificate summary text (for creator notification)
  3. Generate quality improvement suggestions for RESTRICTED/COMMUNITY ideas
  4. Interpret semantic similarity between near-duplicate ideas for governance board reports
  5. Enrich idea domain taxonomy tags for discovery indexing

DECISION AUTONOMY:
  NONE — AI assist layer produces explanatory text and enrichment signals only
  All visibility state decisions are made by ML models and rule engine
  AI cannot approve visibility upgrades, overrides, or exceptions

PROMPT_GOVERNANCE:
  All prompts versioned in PROMPT_REGISTRY (IVCA-prompt-{name}-v{version})
  Templates only — no open-ended generation
  Max output: 400 tokens
  No hallucinated scores, dates, or credential references

FORBIDDEN AI TASKS:
  Override visibility state assigned by ML model
  Generate originality_score or similarity_hash numerically
  Access or reference idea content from other creators' PROTECTED ideas
  Make governance board recommendations
  Generate content that could be mistaken for the original idea
```

> 🔒 **AI MUST ASSIST ML — NOT REPLACE IT. No visibility state is assigned, upgraded, or degraded based on AI output alone. AI output is explanatory and presentational only.**

---

---

## 9️⃣ ORIGINALITY & SIMILARITY ENGINE

> **This section governs the most critical and legally sensitive operation: determining whether an idea is original and certifying it.**

### ORIGINALITY CERTIFICATION PIPELINE

```
Step 1: Content Hash Seal
  → SHA-256(idea_content) computed at submission
  → Stored immutably in idea_store
  → This is the IP timestamp anchor

Step 2: Embedding Generation
  → FastText domain-specific embedding (768-dim vector) of idea content
  → Stored in vector_store (FAISS index, tenant-isolated)

Step 3: Corpus Similarity Search
  → ANN (Approximate Nearest Neighbour) search: top-5 most similar ideas in corpus
  → Max cosine similarity score → similarity_score
  → LSH MinHash → similarity_hash

Step 4: Originality Score Computation
  → originality_score = 1 - similarity_score (adjusted by domain_rarity_weight)
  → domain_rarity_weight: rare domains get slight boost (prevents penalizing niche originality)

Step 5: Threshold Decision
  → similarity_score > 0.90 → SUPPRESSED (near-duplicate)
  → similarity_score 0.70–0.90 → PROTECTED + governance review triggered
  → similarity_score < 0.70 → proceed to full visibility evaluation

Step 6: Originality Certificate Issuance
  → Signed by IVCA with Ed25519 using Vault-managed key
  → Certificate includes: idea_id, content_hash, similarity_hash, originality_score, issued_at, corpus_size_at_check
  → Emitted to ROYALTY_ENGINE and COPY_DETECTION_ENGINE
```

### CORPUS MANAGEMENT

```
CORPUS SCOPE:
  - All PUBLIC and COMMUNITY ideas in the platform (cross-tenant aggregate, anonymised)
  - External public domain ideas corpus (research papers, open patents — read-only reference)
  - Tenant-private ideas are NOT included in cross-tenant corpus (strict isolation)
  - Tenant-private corpus: used only for within-tenant similarity checks

CORPUS UPDATE POLICY:
  - New PUBLIC/COMMUNITY ideas added to corpus within 24h of visibility assignment
  - SUPPRESSED ideas: content hash only added (no embedding — prevents corpus poisoning)
  - PROTECTED ideas: NOT added to corpus until visibility ≥ COMMUNITY

FAISS INDEX:
  - Sharded by domain (5 shards: Arts, Commerce, Science, Technology, Administration)
  - Rebuilt nightly if delta > 1,000 new vectors
  - Tenant-private index: separate FAISS shard per tenant, isolated storage in MinIO
```

---

---

## 🔟 SCALABILITY DESIGN

> **10M–100M Users · Kubernetes HPA · Stateless · Idempotent · Async Similarity Search**

| Metric | Target |
|---|---|
| `EXPECTED_RPS` | 2,000 peak / 400 avg (idea submissions) + 8,000 peak (visibility queries) |
| `LATENCY_TARGET` | Submission P95 < 800ms (includes similarity search) · Query P95 < 100ms (cached) |
| `MAX_CONCURRENCY` | 8,000 parallel requests |
| `QUEUE_STRATEGY` | Redis Streams FIFO — idea submissions async · visibility queries synchronous (cached) |
| `MIN_REPLICAS` | 3 pods |
| `MAX_REPLICAS` | 40 pods |
| `HPA_TRIGGER` | CPU > 60% OR RPS > 1,500 |

### SCALING STRATEGIES

```
SUBMISSION PROCESSING (async path):
  → Redis Streams: idea_submission_queue
  → Worker pods consume queue: Gate 1 (fraud) synchronous, Gates 2–6 async
  → Creator receives immediate "submission received" with idea_id
  → Visibility decision delivered via NOTIFICATION_AGENT within 60 seconds

VISIBILITY QUERY (synchronous path):
  → Redis L1 cache: visibility_state per idea_id (TTL 15 minutes)
  → On cache miss: PostgreSQL RLS query (P95 < 50ms)
  → Batch invalidation on visibility state change events

SIMILARITY SEARCH SCALING:
  → FAISS ANN search: GPU-accelerated pod pool (separate from main agent pods)
  → FAISS query P95 < 300ms for corpus of 10M ideas
  → Horizontal scaling: one FAISS pod per domain shard

IDEMPOTENCY:
  → request_id deduplication in Redis (TTL 24h)
  → Same idea_id + same content_hash = same visibility decision (idempotent re-evaluation)

CORPUS UPDATE:
  → Async nightly job: FAISS index rebuild
  → Online updates: streaming add to index on PUBLIC/COMMUNITY assignment
  → No blocking of main agent during corpus updates
```

---

---

## 1️⃣1️⃣ SECURITY ENFORCEMENT

> **Zero-Trust · Cryptographic Sealing · Tenant Isolation · Content Hash Protection · Append-Only**

```
TENANT ISOLATION:
  → WHERE tenant_id = ? enforced at ORM level AND PostgreSQL RLS level
  → Dual enforcement: application layer + database layer
  → FAISS corpus: cross-tenant aggregate (anonymised) + per-tenant private shard (isolated)
  → Private idea embeddings NEVER written to cross-tenant corpus

RBAC AUTHORIZATION:
  → IDEA:SUBMIT — required for idea submission (all verified users)
  → IDEA:VIEW_RESTRICTED — required for viewing RESTRICTED ideas within tenant
  → IDEA:ADMIN — required to trigger re-evaluation or visibility override (with audit)
  → IDEA:AUDIT — required for read access to all visibility audit records

CONTENT HASH SEALING:
  → SHA-256 of idea_content computed at submission, before any processing
  → Stored immutably — cannot be changed after submission
  → Any content modification creates a new idea_id (versioned ideas — add-only)
  → Hash is the IP timestamp anchor used in all legal disputes

CRYPTOGRAPHIC SIGNING:
  → Originality certificates signed with Ed25519 (PyNaCl / libsodium)
  → Signing keys managed in HashiCorp Vault — rotated quarterly
  → Old key versions retained for historical certificate verification
  → signing_key_version stored with every certificate

RATE LIMITING:
  → Kong API Gateway: 20 idea submissions per day per creator_id
  → Visibility queries: 1,000 per minute per requester_id
  → Burst protection: 50 submissions per hour hard cap

FRAUD GATE:
  → FRAUD_DETECTION_AGENT consulted on every submission
  → fraud_risk ≥ 0.65 → SUPPRESSED immediately
  → fraud_risk ≥ 0.80 → SUPPRESSED + creator account flagged for Trust & Safety review

MINOR PROTECTION:
  → creator age_flag = MINOR (from IAM Keycloak): max visibility = COMMUNITY
  → Idea embedding NOT added to public corpus (GDPR/COPPA compliance)
  → No public SEO indexing of minor's ideas
```

> 🔒 **CROSS-TENANT IDEA EXPOSURE IS FORBIDDEN. Any attempt to query the visibility state or content of an idea outside the requester's tenant boundary triggers `SECURITY_INCIDENT_EVENT` + immediate CISO escalation.**

---

---

## 1️⃣2️⃣ AUDIT & TRACEABILITY

> **Every operation logged · Immutable · Cryptographically chained · Tamper-evident**

### AUDIT LOG SCHEMA

```json
{
  "audit_id":             "UUID — primary key, immutable",
  "chain_hash":           "SHA-256(prev_audit_id + audit_id + timestamp_utc) — tamper-evident chain",
  "timestamp_utc":        "ISO 8601 — operation execution time",
  "actor_id":             "UUID — who triggered the operation",
  "tenant_id":            "UUID — isolation boundary",
  "agent_id":             "IVCA-v1.0.0",
  "operation_type":       "ENUM: SUBMIT | VISIBILITY_ASSIGN | QUERY | UPGRADE | DOWNGRADE | SUPPRESS | FLAG | UNFLAG | CERTIFICATE_ISSUE | CORPUS_ADD | CORPUS_REJECT",
  "idea_id":              "UUID",
  "creator_id":           "UUID",
  "visibility_before":    "ENUM | null",
  "visibility_after":     "ENUM",
  "input_hash":           "SHA-256 of serialized input payload",
  "output_hash":          "SHA-256 of serialized output result",
  "model_version":        "String — visibility classifier version used",
  "decision_path":        "Array<String> — gates evaluated in order with PASS|FAIL",
  "confidence_score":     "Float 0.0–1.0",
  "originality_score":    "Float 0.0–1.0 | null",
  "similarity_score":     "Float 0.0–1.0 | null",
  "fraud_risk_score":     "Float 0.0–1.0",
  "anomaly_flags":        "Array<String> — empty if clean",
  "human_override":       "Boolean — was this decision made or modified by a human?",
  "override_actor":       "UUID | null — governance board member ID if human_override = true",
  "schema_version":       "ivca-audit-schema-v1"
}
```

### AUDIT CHAIN INTEGRITY

```
→ Daily job: verify chain_hash integrity across last 24h audit records
→ Any broken chain: AUDIT_INTEGRITY_ALERT to Audit Admin
→ Chain break triggers: halt new visibility decisions until chain verified
→ Audit records partitioned by tenant_id + month in PostgreSQL
→ No UPDATE. No DELETE. Enforced by DB constraint + application write guard.
```

---

---

## 1️⃣3️⃣ FAILURE POLICY

> **No silent failures · Every failure classified · Human escalation paths defined**

| Failure Type | Immediate Action | Escalation |
|---|---|---|
| Invalid input (schema fail) | STOP → HTTP 422 → LOG → Return `missing_fields[]` | None — client error |
| Fraud risk ≥ 0.65 | STOP → SUPPRESS idea → LOG `fraud_suppression_event` | FRAUD_DETECTION_AGENT + Trust & Safety Officer |
| Fraud risk ≥ 0.80 | STOP → SUPPRESS + FLAG creator → LOG `critical_fraud_event` | CISO + Trust & Safety Officer |
| Similarity search timeout (> 3s) | ASSIGN `PROTECTED` temporarily → LOG → Queue for retry | DevOps On-Call if > 5 consecutive timeouts |
| FAISS index unavailable | ASSIGN `PROTECTED` temporarily → LOG `similarity_unavailable` | DevOps On-Call (P1) |
| ML model unavailable | STOP → HTTP 503 → LOG `model_unavailable` → No fallback scoring | DevOps On-Call (PagerDuty) |
| Signing key unavailable (Vault) | STOP → LOG `signing_failure` → No unsigned certificates | Security Admin + DevOps On-Call (P0) |
| Confidence score < 0.55 | ASSIGN `PROTECTED` (safe default) → LOG `low_confidence_visibility` → Queue for re-evaluation | Analytics Admin for pattern review |
| Audit write failure | STOP visibility assignment → LOG `audit_failure` → No decision without audit record | Database Admin + Incident Response Manager (P0) |
| Corpus poisoning detected | HALT corpus update → LOG → Rollback to last clean checkpoint | Model Governance Manager + Security Admin |
| Duplicate request_id (24h) | Return cached result → LOG `duplicate_request` → No reprocessing | None — expected idempotent behaviour |

### FAILURE RESPONSE ENVELOPE

```json
{
  "error_code":     "ENUM: VALIDATION_ERROR | FRAUD_SUPPRESSED | MODEL_UNAVAILABLE | SIGNING_FAILURE | AUDIT_FAILURE | SIMILARITY_TIMEOUT | CONFIDENCE_LOW",
  "error_message":  "String — human-readable, generated from AI assist layer template",
  "missing_fields": "Array<String> — populated for VALIDATION_ERROR only",
  "retry_safe":     "Boolean — true for transient failures (model/vault/FAISS unavailable)",
  "retry_after_ms": "Integer — exponential backoff: 200ms → 1000ms → 5000ms",
  "escalate_to":    "ENUM: DEVOPS_ONCALL | SECURITY_ADMIN | GOVERNANCE_BOARD | TRUST_SAFETY | null",
  "safe_state":     "ENUM: PROTECTED | SUPPRESSED — state assigned during failure",
  "audit_reference":"UUID — always present even on failure"
}
```

> 🔒 **RETRY POLICY: Max 3 retries for transient failures. Backoff: 200ms → 1000ms → 5000ms. After 3 failures: HALT + ESCALATE. No retry for fraud suppression or audit failures. Safe default during failure = `PROTECTED` (never PUBLIC).**

---

---

## 1️⃣4️⃣ INTER-AGENT DEPENDENCY MAP

### UPSTREAM AGENTS

| Agent | What It Provides |
|---|---|
| `IDEA_DNA_AGENT` | Idea semantic vector (idea_vector), innovation graph node registration |
| `COPY_DETECTION_ENGINE` | similarity_hash, nearest_corpus_idea_id, near-duplicate flags |
| `FRAUD_DETECTION_AGENT` | fraud_risk_score, anomaly_flags, account abuse pattern tags |
| `VERIFICATION_AGENT` | creator identity_verified, age_flag (minor check), institution_verified |
| `SCORING_ENGINE` | creator belt_level (domain-specific — higher belt = quality signal boost) |
| `COMPLIANCE_AGENT` | compliance_gate_passed, domain_regulatory_flag, government_program_clearance |
| `MARKET_SIGNAL_AGENT` | skill_demand_index, trending_topic_signals, employer_search_term_match |
| `RBAC_AGENT` | actor permissions, domain access grants, tenant membership |
| `LICENSE_GENERATION_AGENT` | trainer/creator license status — affects max eligible visibility tier |

### DOWNSTREAM AGENTS

| Agent | What It Receives |
|---|---|
| `ROYALTY_ENGINE` | `ROYALTY_ELIGIBILITY_EVENT` — originality_certificate, originality_score, royalty_eligible flag |
| `COPY_DETECTION_ENGINE` | `CORPUS_UPDATE_EVENT` — new idea embedding added to similarity corpus |
| `NOTIFICATION_AGENT` | `VISIBILITY_ASSIGNED_EVENT` — notifies creator of decision + explanation_tokens |
| `RANKING_AGENT` | `RANK_UPDATE_EVENT` — idea quality + originality feeds idea leaderboard ranking |
| `XP_ENGINE` | `XP_UPDATE_EVENT` — idea reaching PUBLIC (+200 XP), COMMUNITY (+75 XP), Certificate issued (+150 XP) |
| `FEATURE_STORE_AGENT` | `FEATURE_EMIT_EVENT` — behavioral and quality signals for passive ML enrichment |
| `OBSERVABILITY_AGENT` | Performance metrics, fraud suppression rates, corpus health, model drift |
| `BUSINESS_MATCHING_AGENT` | Idea domain + quality score as creator capability signal for job/project matching |
| `TALENT_MARKETPLACE_AGENT` | PUBLIC ideas surfaced in creator talent profiles with originality badge |

### STRUCTURED EVENT PAYLOADS

```json
// VISIBILITY_ASSIGNED_EVENT
{
  "event_type":         "VISIBILITY_ASSIGNED",
  "idea_id":            "UUID",
  "creator_id":         "UUID",
  "tenant_id":          "UUID",
  "visibility_state":   "ENUM",
  "originality_score":  "Float",
  "quality_score":      "Float",
  "royalty_eligible":   "Boolean",
  "source_agent":       "IVCA-v1.0.0",
  "timestamp":          "ISO 8601 UTC"
}

// ROYALTY_ELIGIBILITY_EVENT
{
  "event_type":             "ROYALTY_ELIGIBILITY",
  "idea_id":                "UUID",
  "creator_id":             "UUID",
  "originality_certificate_id": "UUID",
  "originality_score":      "Float",
  "similarity_hash":        "String",
  "royalty_eligible":       "Boolean",
  "source_agent":           "IVCA-v1.0.0"
}
```

---

---

## 1️⃣5️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```json
// EMIT_FEATURE_VECTOR (to FEATURE_STORE_AGENT)
{
  "user_id":        "UUID",
  "feature_name":   "ENUM: idea_submission_rate | avg_originality_score | avg_quality_score | visibility_upgrade_rate | suppression_rate | fraud_flag_rate | market_alignment_avg | corpus_contribution_count",
  "feature_value":  "Float",
  "timestamp":      "ISO 8601 UTC",
  "source_agent":   "IVCA-v1.0.0"
}
```

**Features emitted feed:**

- `BUSINESS_MATCHING_AGENT` — creator's originality/quality signals enrich job/project matching
- `FRAUD_DETECTION_AGENT` — idea abuse patterns train fraud models
- `RANKING_AGENT` — idea quality trend scores inform leaderboard weights
- `MARKET_SIGNAL_AGENT` — aggregate idea submission trends by domain → demand forecasting

---

---

## 1️⃣6️⃣ INNOVATION ECONOMY COMPATIBILITY

> **This agent is the primary feeder of the Innovation Economy layer.**

```
EMIT (to IDEA_DNA_AGENT):
  idea_vector:          FastText 768-dim embedding of idea content
  domain:               ENUM — Arts | Commerce | Science | Technology | Administration
  idea_type:            ENUM
  visibility_state:     ENUM — only PUBLIC/COMMUNITY ideas added to innovation graph

EMIT (to ROYALTY_ENGINE):
  IDEA_VECTOR:          Semantic embedding
  SIMILARITY_HASH:      LSH MinHash value
  ORIGINALITY_SCORE:    Float 0.0–1.0
  ROYALTY_ELIGIBLE:     Boolean (originality ≥ 0.55 AND quality ≥ 0.45 AND visibility ≥ COMMUNITY)

EMIT (to COPY_DETECTION_ENGINE):
  content_hash:         SHA-256 of idea at submission
  similarity_hash:      LSH MinHash
  idea_id:              UUID
  corpus_position:      Index reference for ANN lookup
```

> 🔒 **PROTECTED and SUPPRESSED ideas NEVER emit to IDEA_DNA_AGENT or ROYALTY_ENGINE. Their content hash is recorded for deduplication purposes only — no semantic data leaves the agent.**

---

---

## 1️⃣7️⃣ GROWTH ENGINE HOOK

```
XP_UPDATE_EVENT triggers:
  Idea reaches COMMUNITY visibility:     +75 XP for creator
  Idea reaches PUBLIC visibility:        +200 XP for creator
  Originality certificate issued:        +150 XP for creator
  Idea earns royalty eligibility:        +300 XP for creator
  Idea reaches top 10% quality score:    +100 XP bonus
  BLACK BELT creator's idea goes PUBLIC: +500 XP (domain mastery × innovation signal)

RANK_UPDATE_EVENT triggers:
  Idea quality_score feeds domain idea leaderboard
  Originality_score × market_alignment feeds innovation leaderboard
  Creator's aggregate PUBLIC idea count feeds creator reputation rank

SHARE_TRIGGER_EVENT triggers:
  Idea reaches PUBLIC AND quality_score ≥ 0.75:
    → Generate shareable innovation card (idea title + originality badge + public_verify_url)
    → Prompt creator to share on LinkedIn/WhatsApp via NOTIFICATION_AGENT
    → Public originality_certificate URL is SEO-indexed (React SSR page) for employer discovery
```

> ✅ GROWTH HOOKS integrate with R91 (Student Identity & Badge System), R93 (Student Social Feed — idea posts), R94 (Peer Career Collaboration — idea-driven projects), and platform distribution method #51 (social sharing cards).

---

---

## 1️⃣8️⃣ PERFORMANCE MONITORING

| Metric | Target | Alert Threshold |
|---|---|---|
| Visibility Decision Success Rate | > 99.3% | < 99.0% |
| Submission P95 Latency | < 800ms | > 1200ms |
| Query P95 Latency (cached) | < 100ms | > 200ms |
| FAISS Similarity Search P95 | < 300ms | > 600ms |
| Fraud Suppression False Positive Rate | < 3% (weekly sample audit) | > 6% |
| Originality Model Drift (PSI) | < 0.20 | > 0.25 = RETRAIN |
| Audit Write Success Rate | 100% | Any failure = P0 |
| Corpus Poisoning Detection | Real-time | Any detection = HALT + ALERT |

```
OBSERVABILITY STACK:
  Prometheus:   scrape /metrics every 15s — all 8 KPIs above + model inference time
  Grafana:      IVCA dashboard — visibility distribution, suppression rate, corpus health
  Jaeger:       Distributed tracing on: submission pipeline · FAISS search · Vault signing
  Loki:         Structured logs — all gate decisions with timestamps
  Alerting:     PagerDuty integration for P0/P1 — audit failure, signing failure, corpus poisoning
```

---

---

## 1️⃣9️⃣ VERSIONING POLICY

```
AGENT VERSION FORMAT:
  IVCA-v{MAJOR}.{MINOR}.{PATCH}
  Examples: IVCA-v1.0.0, IVCA-v1.1.0, IVCA-v2.0.0

SCHEMA CHANGES:
  ADD-ONLY — no field removal, no field rename
  New fields require default values for backward compatibility
  Breaking changes require MAJOR version bump + 90-day compatibility window

VISIBILITY THRESHOLD CHANGES:
  Quality/originality threshold INCREASES only (never decrease — protects ecosystem quality)
  Any threshold change requires changelog entry + re-evaluation of RESTRICTED ideas within 30 days
  Threshold changes are configuration — not code — enabling hot-update without redeploy

ORIGINALITY CORPUS VERSIONING:
  Corpus snapshot versioned daily: corpus-{YYYYMMDD}
  Rollback to any snapshot within 30 days in case of corpus poisoning event
  Corpus version stored in every originality certificate

SIGNING KEY VERSIONING:
  signing_key_version stored in every certificate
  Old keys retained indefinitely for historical certificate verification
  New key version issued quarterly via Vault

ROLLBACK SAFETY:
  Every deploy produces rollback manifest
  Feature flags for gradual rollout of new visibility thresholds
  kubectl rollout undo within 24h window
  FAISS index: previous version retained for 48h after rebuild
```

> 🔒 **MUTATION POLICY: ADD-ONLY. No existing field, endpoint, visibility state, or quality threshold may be removed or reduced. Visibility states may only be added (not removed) to protect historical audit integrity.**

---

---

## 2️⃣0️⃣ TECH STACK BINDING

> **Locked to Ecoskiller Antigravity R1 Technology Stack**

| Component | Technology |
|---|---|
| **Backend Runtime** | Python 3.11 · FastAPI · REST + OpenAPI 3.1 |
| **ML Frameworks** | XGBoost 2.x · scikit-learn 1.4 · FastText (embeddings) · FAISS (ANN search) · DataSketch (MinHash/LSH) · PyOD (Isolation Forest) |
| **Cryptographic Signing** | PyNaCl (Ed25519 via libsodium) · HashiCorp Vault (key management + rotation) |
| **AI Assist Runtime** | Anthropic `claude-sonnet-4-6` via internal proxy · Prompt Registry IVCA-prompt-v1 · Max 400 tokens |
| **Idea Store** | PostgreSQL 15 — idea_store + visibility_store + audit_store (partitioned by tenant_id + month) · RLS enforced |
| **Vector Store** | FAISS CPU/GPU index (5 domain shards + per-tenant private shards) · Index files stored in MinIO |
| **Idempotency Store** | Redis 7 — request_id deduplication (TTL 24h) · visibility_state cache (TTL 15min) |
| **Object Storage** | MinIO — originality certificate PDFs + FAISS index shards (tenant-isolated buckets) |
| **Event Broker** | Redis Streams — all inter-agent events |
| **API Gateway** | Kong OSS — JWT validation, HMAC signature check, rate limiting |
| **Observability** | Prometheus + Grafana + Jaeger + Loki |
| **CI/CD** | GitLab CE — corpus regression tests + visibility threshold smoke tests before any IVCA deployment |
| **Flutter / React** | Flutter: idea submission UI, visibility status indicator, originality badge, share card animation. React Next.js: public originality certificate verification page (SSR + SEO-indexed) |

---

---

## 2️⃣1️⃣ NON-NEGOTIABLE RULES

### AGENT MUST NEVER:

```
✗  Surface an idea to any audience without passing ALL 6 visibility gates
✗  Assign PUBLIC visibility without creator_verified = true
✗  Assign PUBLIC visibility to a minor creator's idea (max = COMMUNITY)
✗  Issue an originality certificate without cryptographic Ed25519 signature
✗  Add a PROTECTED or SUPPRESSED idea's embedding to the cross-tenant corpus
✗  Allow SUPPRESSED (permanent) state to transition to any other state
✗  Make a visibility decision based on AI output alone
✗  Skip the fraud_risk_score check from FRAUD_DETECTION_AGENT on any submission
✗  Store a similarity search result without a content_hash seal to anchor it
✗  Allow cross-tenant idea visibility queries without ABAC grant + audit log
✗  Downgrade quality or originality thresholds (thresholds may only increase)
✗  Process a visibility upgrade without writing an audit record first
✗  Generate a certificate without storing corpus_version at time of check
✗  Create hidden scoring logic or undocumented visibility gate shortcuts
✗  Modify any historical content_hash, similarity_hash, or originality_score
```

### AGENT MUST ALWAYS:

```
✓  Assign PROTECTED as the safe default during any failure or uncertainty
✓  Log every visibility decision to the immutable append-only audit trail
✓  Include confidence_score, model_version, and audit_reference in every output
✓  Seal idea content with SHA-256 hash before any processing begins
✓  Verify fraud_risk_score < 0.65 before any visibility above SUPPRESSED
✓  Emit ROYALTY_ELIGIBILITY_EVENT to ROYALTY_ENGINE for all eligible ideas
✓  Emit feature vectors to FEATURE_STORE_AGENT after every visibility operation
✓  Verify audit chain_hash daily — halt new decisions on chain break
✓  Operate idempotently — same idea_id + same content_hash = same decision
✓  Enforce tenant isolation at ORM level AND PostgreSQL RLS level (dual enforcement)
✓  Version all model artifacts before production deployment
✓  Escalate P0 failures (signing failure, audit write failure) to human operators immediately
✓  Respect minor protection flags — never exceed COMMUNITY for minors
✓  Document every human override in audit record with override_actor UUID
```

---

---

## 2️⃣2️⃣ AGENT SEAL

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║                          🔒  AGENT SEAL                                         ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║                                                                                  ║
║   AGENT_NAME         :  IDEA_VISIBILITY_CONTROL_AGENT                           ║
║   AGENT_VERSION      :  IVCA-v1.0.0                                             ║
║   PLATFORM           :  ECOSKILLER ANTIGRAVITY                                  ║
║   DOMAIN             :  Lane F (Intelligence) + Lane D (Governance)             ║
║   AUTHORITY          :  SOLE VISIBILITY DECISION MAKER FOR ALL IDEA TYPES       ║
║   SIGNING_ALGO       :  Ed25519  |  KEY_MGMT: HashiCorp Vault                  ║
║   CORPUS_TECH        :  FAISS ANN + FastText Embeddings + MinHash LSH           ║
║                                                                                  ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║                                                                                  ║
║   STATUS             :  SEALED · LOCKED · GOVERNED · DETERMINISTIC              ║
║   MUTATION           :  ADD-ONLY — No removal, no rename, no downgrade          ║
║   INTERPRETATION     :  FORBIDDEN                                               ║
║   ASSUMPTION FILLING :  FORBIDDEN — HALT on missing spec                        ║
║   DEFAULT STATE      :  PROTECTED (safe default) — never PUBLIC on uncertainty  ║
║   FAILURE MODE       :  STOP → REPORT → NO PARTIAL OUTPUT                      ║
║                                                                                  ║
╠══════════════════════════════════════════════════════════════════════════════════╣
║                                                                                  ║
║   INTERPRETATION AUTHORITY   :  NONE                                            ║
║   EXECUTION AUTHORITY        :  HUMAN DECLARATION ONLY                          ║
║                                                                                  ║
║   VIOLATION OF ANY RULE IN THIS DOCUMENT                                        ║
║   → STOP EXECUTION                                                               ║
║   → REPORT INCIDENT                                                              ║
║   → NO DEPLOYMENT CLAIM PERMITTED                                               ║
║                                                                                  ║
║   PROTECTED = SAFE DEFAULT · SUPPRESSED(permanent) IS TERMINAL                 ║
║   NO UNSIGNED CERTIFICATE · NO CROSS-TENANT IDEA EXPOSURE · MINORS = COMMUNITY ║
║   AI ASSISTS ML — NEVER REPLACES IT                                             ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

*Document Version: IVCA-v1.0.0 · Ecoskiller Antigravity Intelligence & Innovation Core*
*Mutation Policy: Add-Only · Execution Authority: Human Declaration Only*
