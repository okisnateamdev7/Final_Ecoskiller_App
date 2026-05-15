# 🔒 ROYALTY_DISPUTE_RESOLUTION_AGENT
## ECOSKILLER ANTIGRAVITY — INTELLIGENCE CORE
### Status: SEALED · LOCKED · GOVERNED · DETERMINISTIC
### Mutation Policy: Add-only via version bump
### Interpretation Authority: NONE
### Execution Authority: Human declaration only
### Agent Version: v1.0.0
### Platform: Ecoskiller Antigravity

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 1 — AGENT IDENTITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
AGENT_NAME          = ROYALTY_DISPUTE_RESOLUTION_AGENT
SYSTEM_ROLE         = Royalty Claim Arbitration & Resolution Intelligence Engine
PRIMARY_DOMAIN      = royalty_dispute_resolution
EXECUTION_MODE      = Deterministic + Validated + Human-Escalation-Gated
DATA_SCOPE          = Creator-scoped, content-scoped, billing-scoped — append-only
TENANT_SCOPE        = Strict Isolation — no cross-tenant case data permitted
FAILURE_POLICY      = HALT on ambiguity → LOG_INCIDENT → ESCALATE_TO_HUMAN
AGENT_VERSION       = v1.0.0
DEPLOYED_ON         = Ecoskiller Antigravity Platform
LANE_DEPENDENCY     = Lane D (Governance) + Lane F (Intelligence)
                      requires governance_ready + ai_ready
```

This agent must NEVER assume missing specifications.
This agent must NEVER execute financial reversals autonomously.
This agent must NEVER issue a final verdict without human confirmation on
disputes where financial_impact > configured threshold.
This agent must NEVER mix dispute records across tenant boundaries.

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 2 — PURPOSE DECLARATION
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 2.1 Problem Solved

The ROYALTY_DISPUTE_RESOLUTION_AGENT manages the full lifecycle of disputes
raised against royalty calculations, content ownership claims, originality
violations, revenue share disagreements, and creator earnings conflicts on the
Ecoskiller Antigravity platform.

It orchestrates a deterministic, evidence-driven arbitration pipeline that
combines ML-based similarity analysis, originality scoring, billing ledger
verification, and structured human escalation to produce fair, traceable,
and immutable dispute outcomes — protecting creators, institutions, and
platform economic integrity.

Dispute categories covered:
  1. Royalty calculation disputes (creator challenges computed royalty amount)
  2. Content originality disputes (claim that idea/content was copied or plagiarised)
  3. Revenue share disputes (disagreement on split between co-creators or institutions)
  4. Billing disputes (overcharge, undercharge, incorrect deduction)
  5. Trainer/instructor payout disputes (incorrect payout calculation or withholding)
  6. Institutional royalty share disputes (institution challenges its cohort revenue share)
  7. Referral reward disputes (referral credit not applied or incorrectly applied)

### 2.2 Inputs Consumed

- Dispute submission payload (claimant + claim type + evidence)
- Royalty ledger records from ROYALTY_ENGINE
- Content originality score from COPY_DETECTION_ENGINE
- Idea similarity hash from IDEA_DNA_AGENT
- Billing transaction records from BILLING_SERVICE
- Creator reward ledger from CREATOR_REWARD_LEDGER
- Trainer wallet and payout records from TRAINER_REVENUE_AGENT
- Referral reward ledger from REFERRAL_ENGINE
- Institutional royalty share records from SCHOOL_GROWTH_FORECAST_AGENT
- Audit trail references from AUDIT_LOG_SERVICE
- Prior dispute history for claimant and respondent

### 2.3 Outputs Produced

- Dispute case record (structured, immutable after creation)
- Evidence collection manifest
- ML similarity and originality analysis report
- Dispute recommendation (AI-assisted, ML-validated)
- Human review packet (for escalated cases)
- Dispute verdict record (UPHELD / PARTIALLY_UPHELD / DISMISSED / ESCALATED)
- Financial adjustment instruction (for BILLING_SERVICE — advisory signal only)
- Claimant and respondent notification payloads
- Transparency report contribution record
- Audit trace per every state transition

### 2.4 Downstream Agents That Depend On This Agent

| Agent | Dependency |
|---|---|
| BILLING_SERVICE | Receives financial adjustment instruction (advisory) |
| ROYALTY_ENGINE | Receives recalculation trigger on UPHELD disputes |
| NOTIFICATION_DISPATCH_AGENT | Receives verdict and status-change notifications |
| OBSERVABILITY_AGENT | Receives all metrics, SLA breach flags, anomaly events |
| TRANSPARENCY_REPORT_AGENT | Receives anonymized dispute outcome for public report |
| MODERATION_AGENT | Receives escalation flags for severe content violations |
| FEATURE_STORE_AGENT | Receives dispute outcome feature vectors |
| ANTIGRAVITY_LEADERBOARD_AGENT | Receives trust score impact signal post-verdict |

### 2.5 Upstream Agents That Feed This Agent

| Agent | Signal Provided |
|---|---|
| ROYALTY_ENGINE | Royalty ledger snapshot at time of dispute |
| COPY_DETECTION_ENGINE | Similarity analysis and plagiarism confidence score |
| IDEA_DNA_AGENT | Idea vector and similarity hash |
| BILLING_SERVICE | Transaction ledger records |
| CREATOR_REWARD_LEDGER | Creator earnings history |
| TRAINER_REVENUE_AGENT | Trainer wallet and payout history |
| REFERRAL_ENGINE | Referral reward ledger records |
| SCHOOL_GROWTH_FORECAST_AGENT | Institutional royalty share context |
| AUDIT_LOG_SERVICE | Immutable prior audit references |
| INSTITUTION_VERIFICATION_AGENT | Verified institution identity for institutional disputes |

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 3 — DISPUTE LIFECYCLE (CANONICAL STATE MACHINE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
DISPUTE STATE MACHINE (IMMUTABLE TRANSITIONS):

  OPENED
    │
    ▼
  EVIDENCE_COLLECTION
    │
    ▼
  ML_ANALYSIS_IN_PROGRESS
    │
    ▼
  AI_RECOMMENDATION_READY
    │
    ├──► AUTO_RESOLVABLE ──► VERDICT_ISSUED ──► CLOSED
    │                              │
    │                              ▼
    │                        FINANCIAL_ADJUSTMENT_QUEUED
    │                              │
    │                              ▼
    │                           CLOSED
    │
    └──► REQUIRES_HUMAN_REVIEW
              │
              ▼
          HUMAN_REVIEW_PENDING
              │
              ▼
          HUMAN_VERDICT_ISSUED
              │
              ▼
          VERDICT_ISSUED ──► FINANCIAL_ADJUSTMENT_QUEUED ──► CLOSED
              │
              ▼
          APPEAL_WINDOW_OPEN (72h)
              │
              ├──► APPEAL_FILED ──► REOPENED ──► EVIDENCE_COLLECTION
              │
              └──► APPEAL_WINDOW_EXPIRED ──► PERMANENTLY_CLOSED

TERMINAL STATES: CLOSED | PERMANENTLY_CLOSED | DISMISSED
STATE TRANSITIONS: Append-only — no state may be undone after commit
INVALID TRANSITIONS: Immediately rejected and logged as SECURITY_INCIDENT
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 4 — INPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
INPUT_SCHEMA — DISPUTE_SUBMISSION: {
  "required_fields": [
    "dispute_id",
    "tenant_id",
    "claimant_id",
    "claimant_type",
    "dispute_category",
    "disputed_entity_id",
    "disputed_entity_type",
    "dispute_description",
    "claimed_financial_impact",
    "claimed_currency",
    "submission_timestamp_utc",
    "evidence_references"
  ],
  "optional_fields": [
    "respondent_id",
    "respondent_type",
    "supporting_document_ids",
    "referenced_audit_trail_ids",
    "prior_dispute_id",
    "external_reference_number"
  ],
  "validation_rules": [
    "dispute_id must be unique UUID — REJECT if duplicate within 30 days",
    "tenant_id must match authenticated session claim — REJECT if mismatch",
    "claimant_id must be active, verified user or institution on platform",
    "claimant_type must be ENUM['creator','trainer','institution','platform_user']",
    "dispute_category must be ENUM['royalty_calculation','content_originality',
      'revenue_share','billing','payout','institutional_share','referral_reward']",
    "disputed_entity_id must resolve in declared entity registry",
    "disputed_entity_type must be ENUM['content','course','idea','transaction',
      'payout','referral','royalty_ledger_entry']",
    "claimed_financial_impact must be numeric >= 0",
    "claimed_currency must be ISO-4217 code",
    "submission_timestamp_utc must be ISO-8601 UTC",
    "evidence_references must be non-empty array of valid reference IDs"
  ],
  "security_checks": [
    "Validate tenant_id matches claimant's tenant assignment",
    "Validate claimant has not exceeded dispute rate limit: 5 open disputes per 30 days",
    "Validate disputed_entity_id belongs to claimant's accessible scope",
    "Reject cross-tenant entity references",
    "Validate JWT scope includes dispute_resolution:submit",
    "Check for duplicate submission fingerprint within 24h window"
  ],
  "domain_checks": [
    "Verify disputed royalty or billing record exists in ROYALTY_ENGINE or BILLING_SERVICE",
    "Verify claimant has a stake in the disputed_entity_id",
    "If content_originality dispute: verify content_id exists in COPY_DETECTION_ENGINE registry",
    "If institutional_share dispute: verify institution is verified in INSTITUTION_VERIFICATION_AGENT"
  ]
}
```

**Rules — Non-Negotiable:**
- No null tolerance without explicit null_policy declaration
- Reject malformed submissions immediately with structured error payload
- Log ALL validation failures with full input hash to AUDIT_LOG_SERVICE
- No silent failures — every rejection produces a structured response
- False report detection: auto-flag if claimant has >3 dismissed disputes in 90 days

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 5 — OUTPUT CONTRACT (STRICT)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```json
OUTPUT_SCHEMA — DISPUTE_VERDICT: {
  "result_object": {
    "dispute_id":                  "UUID",
    "case_reference":              "string (human-readable, e.g. RDRA-2026-00142)",
    "tenant_id":                   "UUID",
    "claimant_id":                 "UUID",
    "respondent_id":               "UUID | null",
    "dispute_category":            "ENUM string",
    "disputed_entity_id":          "UUID",
    "dispute_state":               "ENUM (state machine state)",
    "verdict":                     "ENUM['UPHELD','PARTIALLY_UPHELD','DISMISSED',
                                      'ESCALATED','PENDING_HUMAN_REVIEW'] | null",
    "verdict_rationale":           "string (AI-assisted, ML-validated)",
    "verdict_issued_at_utc":       "ISO-8601 | null",
    "verdict_issued_by":           "ENUM['SYSTEM','HUMAN_REVIEWER','PANEL']",
    "financial_adjustment": {
      "action":                    "ENUM['CREDIT','DEBIT','NO_ACTION']",
      "amount":                    "numeric",
      "currency":                  "ISO-4217",
      "beneficiary_id":            "UUID",
      "adjustment_reference":      "UUID",
      "advisory_only":             true
    },
    "ml_analysis_summary": {
      "originality_score":         "float [0.0–1.0] | null",
      "similarity_hash_match":     "boolean | null",
      "billing_variance_detected": "boolean",
      "billing_variance_amount":   "numeric | null",
      "fraud_signal_score":        "float [0.0–1.0]",
      "confidence_score":          "float [0.0–1.0]"
    },
    "evidence_manifest":           "array of evidence reference IDs",
    "appeal_window_expires_utc":   "ISO-8601 | null",
    "appeal_filed":                "boolean",
    "trust_impact_signal": {
      "claimant_trust_delta":      "float",
      "respondent_trust_delta":    "float | null"
    },
    "anomaly_flags":               ["string"]
  },
  "confidence_score":              "float [0.0–1.0]",
  "model_version":                 "string (e.g. rdra-ml-v1.2.0)",
  "audit_reference":               "UUID",
  "next_trigger_event": [
    "DISPUTE_VERDICT_ISSUED",
    "FINANCIAL_ADJUSTMENT_QUEUED (if verdict = UPHELD or PARTIALLY_UPHELD)",
    "TRUST_SCORE_UPDATE_EVENT",
    "HUMAN_REVIEW_REQUIRED (if escalated)",
    "TRANSPARENCY_REPORT_CONTRIBUTION_EMITTED"
  ]
}
```

**All outputs must include:**
- Confidence score (auto-escalate to human review if confidence < 0.65)
- Immutable model version reference
- Full audit traceability via audit_reference UUID
- advisory_only: true on ALL financial adjustment signals — this agent does not
  execute financial transactions; it instructs BILLING_SERVICE only

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 6 — ML / AI LOGIC LAYER
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 6.1 ML Layer (PRIMARY — 75% of analysis weight)

```
MODEL_TYPE:
  Model A:   Binary Classification (dispute legitimacy: VALID / FRAUDULENT)
  Model B:   Regression (financial variance calculation from ledger diff)
  Model C:   Similarity Scoring (content/idea overlap via embedding comparison)
  Model D:   Time-Series Anomaly Detection (billing pattern anomalies)

FEATURES_USED:
  Dispute legitimacy model (A):
    - claimant_dispute_history_count_90d
    - claimant_dismissed_rate_90d
    - claimant_trust_score
    - disputed_entity_age_days
    - time_since_disputed_event_days
    - evidence_reference_count
    - financial_impact_to_total_earnings_ratio
    - respondent_dispute_history_count_90d
    - duplicate_submission_flag

  Financial variance model (B):
    - computed_royalty_amount
    - claimed_royalty_amount
    - variance_amount
    - variance_percentage
    - ledger_entry_count
    - billing_rule_version_at_time
    - revenue_share_rule_version

  Similarity scoring model (C):
    - content_embedding_cosine_similarity
    - idea_vector_distance
    - similarity_hash_match_flag
    - originality_score_at_submission
    - submission_timestamp_delta_days
    - shared_feature_count

  Billing anomaly model (D):
    - transaction_amount_zscore
    - rolling_avg_payout_7d
    - rolling_avg_payout_30d
    - payout_deviation_from_rolling_avg
    - gateway_error_flag
    - deduction_rule_change_proximity_flag

TRAINING_FREQUENCY:
  Legitimacy model (A):  Bi-weekly
  Financial variance (B): Monthly
  Similarity model (C):  Monthly
  Anomaly model (D):     Weekly

DRIFT_DETECTION:
  - Monitor prediction accuracy degradation > 7% from baseline
  - Monitor false positive rate on FRAUDULENT classification > 3%
  - Monitor financial variance calibration error > 5%
  - Action on drift: PAUSE affected model → FLAG OBSERVABILITY_AGENT → revert

VERSION_CONTROL:
  - Format: rdra-[model-identifier]-v[MAJOR].[MINOR].[PATCH]
  - Immutable model artifacts stored per version
  - Rollback safe — no in-place model overwrites permitted
```

### 6.2 AI Layer (ASSIST ONLY — 25% of execution weight)

```
AI_USAGE_SCOPE:
  - Generate verdict_rationale natural language explanation
  - Summarize evidence manifest into human-readable dispute brief
  - Generate human reviewer briefing packet (for escalated cases)
  - Assist in identifying inconsistencies across evidence documents
  - AI must NOT issue verdict autonomously
  - AI must NOT override ML-derived confidence_score
  - AI must NOT access financial ledger records directly

PROMPT_GOVERNANCE:
  - All prompts versioned: rdra-prompt-v[VERSION]
  - Temperature: 0.1 (minimal variance — legal/financial precision required)
  - Max tokens: 800 per verdict_rationale | 1200 per human review briefing
  - Prompt inputs: ML-derived structured analysis only — no raw claimant text
  - All prompts logged with prompt_version and input_hash
  - Prompt outputs flagged as AI_ASSISTED — not autonomous decisions

AI MUST NOT:
  - Issue financial adjustment instructions independently
  - Generate verdict without ML confidence score present
  - Access PII of claimants beyond anonymized case context
  - Produce outputs that imply legal certainty
  - Reference prior AI outputs from other tenants
```

**Law: AI assists. ML decides. Humans confirm on high-stakes cases.**
**Violation → STOP EXECUTION → LOG_INCIDENT**

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 7 — AUTO-RESOLUTION vs HUMAN ESCALATION POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
AUTO-RESOLUTION ELIGIBLE (system may issue verdict without human):
  Condition ALL must be true:
    ✅ confidence_score >= 0.80
    ✅ claimed_financial_impact <= AUTO_RESOLVE_THRESHOLD
       (default: $50 USD equivalent — configurable per tenant)
    ✅ dispute_category IN ['billing','referral_reward']
    ✅ fraud_signal_score < 0.20
    ✅ billing_variance_detected = true AND variance is unambiguous ledger error
    ✅ No prior appeal filed on same disputed_entity_id within 180 days

MANDATORY HUMAN ESCALATION (system MUST escalate — no auto-verdict):
  Condition ANY triggers escalation:
    ⚠️ claimed_financial_impact > AUTO_RESOLVE_THRESHOLD
    ⚠️ dispute_category IN ['content_originality','revenue_share',
                             'institutional_share','royalty_calculation']
    ⚠️ confidence_score < 0.65
    ⚠️ fraud_signal_score >= 0.50
    ⚠️ claimant or respondent is a verified institution
    ⚠️ prior dispute on same entity within 90 days
    ⚠️ respondent has contested the evidence
    ⚠️ legal_hold_flag active on disputed entity

ESCALATION_TARGETS:
  Tier 1 (internal reviewer):   dispute-ops@ecoskiller.internal
  Tier 2 (senior arbiter):      senior-arbiter@ecoskiller.internal
  Tier 3 (legal + compliance):  legal@ecoskiller.internal
  Tier 4 (government authority): AUTHORITY_ESCALATION_ROUTER (R63 system)

SLA TARGETS:
  AUTO_RESOLVABLE disputes:            resolved within 24h of OPENED
  HUMAN_REVIEW_PENDING (Tier 1):       reviewed within 5 business days
  HUMAN_REVIEW_PENDING (Tier 2):       reviewed within 10 business days
  LEGAL_HOLD cases:                    no SLA — indefinite pending legal instruction
  SLA BREACH: auto-flag to OBSERVABILITY_AGENT + escalate to next tier
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 8 — SCALABILITY DESIGN
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
EXPECTED_RPS             = 200 peak (dispute submissions + status checks)
LATENCY_TARGET           = p95 < 600ms (status queries)
                           p95 < 5s (evidence collection trigger)
                           p95 < 30s (full ML analysis complete)
MAX_CONCURRENCY          = 500 simultaneous active dispute pipelines
QUEUE_STRATEGY           = Redis Streams priority queue
                           Priority 1: HIGH financial impact disputes
                           Priority 2: Content originality disputes (SLA-sensitive)
                           Priority 3: Standard billing disputes
                           Priority 4: Referral reward disputes

HORIZONTAL_SCALING:
  - Stateless execution — no in-process state
  - Kubernetes HPA: scale on CPU > 60% OR queue depth > 200
  - Min replicas: 2 | Max replicas: 20
  - Pod anti-affinity enforced across availability zones

ASYNC PROCESSING:
  - All dispute analysis jobs are async
  - Webhook + event notification on state transitions
  - Claimant polling endpoint available

IDEMPOTENCY:
  - All state transitions keyed by: dispute_id + from_state + to_state
  - Duplicate transition attempts within 60s return current state
  - No double-processing of financial adjustment instructions

SCALE PROJECTION:
  At 10M users: ~5,000 active disputes per month
  At 100M users: ~50,000 active disputes per month
  ML batch analysis: nightly re-scoring of PENDING cases
  Database: partitioned by tenant_id + year-month
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 9 — SECURITY ENFORCEMENT
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
SECURITY CHECKLIST (ALL MANDATORY — absence = STOP EXECUTION):

  ✅ Tenant isolation on every request — hard enforced at ORM + Gateway layers
  ✅ Claimant identity verified against Keycloak JWT — scope: dispute_resolution:submit
  ✅ Respondent data access — scope: dispute_resolution:respond only
  ✅ Human reviewer access — scope: dispute_resolution:review (Tier 1/2/3 roles)
  ✅ Platform admin access — scope: dispute_resolution:admin
  ✅ No cross-tenant dispute data visible under any role
  ✅ Financial adjustment signals encrypted and signed before dispatch to BILLING_SERVICE
  ✅ All evidence documents scanned for malware before acceptance
  ✅ Audit logging: append-only, immutable, every state transition
  ✅ Case data encrypted at rest: AES-256
  ✅ Case data encrypted in transit: TLS 1.3 minimum
  ✅ Rate limiting: 5 dispute submissions per claimant per 30 days
  ✅ Duplicate submission fingerprint rejection (24h window)
  ✅ False report detection: auto-flag after 3 dismissed disputes in 90 days
  ✅ Legal hold flag: blocks state transitions on legally held cases
  ✅ Kong Gateway policy: ROYALTY_DISPUTE_RESOLUTION rate-limit plugin active
  ✅ Zero cross-tenant queries: hard enforced
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 10 — AUDIT & TRACEABILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Every state transition and agent execution emits an immutable audit record:

```json
AUDIT_LOG_SCHEMA: {
  "audit_reference":         "UUID (primary key)",
  "timestamp_utc":           "ISO-8601",
  "actor_id":                "UUID (claimant, reviewer, or system agent)",
  "actor_type":              "ENUM['claimant','respondent','system','reviewer','admin']",
  "tenant_id":               "UUID",
  "dispute_id":              "UUID",
  "agent_name":              "ROYALTY_DISPUTE_RESOLUTION_AGENT",
  "agent_version":           "v1.0.0",
  "action":                  "string (e.g. DISPUTE_OPENED, EVIDENCE_COLLECTED, VERDICT_ISSUED)",
  "from_state":              "ENUM state",
  "to_state":                "ENUM state",
  "input_hash":              "SHA-256 of input payload",
  "output_hash":             "SHA-256 of output payload",
  "model_version":           "string | null",
  "prompt_version":          "string | null",
  "confidence_score":        "float | null",
  "decision_path":           "ENUM['AUTO_RESOLVED','ML+AI','HUMAN_TIER1','HUMAN_TIER2','LEGAL']",
  "financial_impact_amount": "numeric | null",
  "financial_impact_currency":"ISO-4217 | null",
  "fraud_signal_score":      "float | null",
  "anomaly_flags":           ["string"],
  "prior_audit_hash":        "SHA-256 of prior audit entry (hash chain)",
  "execution_time_ms":       "integer"
}
```

**Immutability Law:**
- Audit logs written to append-only PostgreSQL partition — no UPDATE or DELETE
- Hash chain: every entry includes SHA-256 of prior entry (tamper detection)
- Replicated to immutable cold storage within 5 minutes
- Retention: 7 years minimum (legal compliance)
- Legal hold: audit records linked to held disputes cannot be purged under any retention policy

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 11 — FAILURE POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
FAILURE_SCENARIOS AND RESPONSE:

┌──────────────────────────────────────┬─────────────────────────────────────────────────┐
│ Failure Type                         │ Response                                        │
├──────────────────────────────────────┼─────────────────────────────────────────────────┤
│ Invalid input (schema fail)          │ STOP → REJECT structured error → LOG            │
│ Unauthorized claimant                │ STOP → SECURITY_INCIDENT → ALERT OPS            │
│ Cross-tenant entity reference        │ STOP → SECURITY_INCIDENT → BLOCK IP if repeat  │
│ ML model unavailable                 │ STOP → LOG_INCIDENT → ESCALATE human review     │
│ AI layer timeout (> 8s)              │ CONTINUE with ML-only rationale → FLAG ai_down  │
│ COPY_DETECTION_ENGINE unreachable    │ PAUSE originality disputes → QUEUE for retry    │
│ Confidence < 0.65                    │ AUTO-ESCALATE to human → FLAG low_confidence    │
│ Fraud score >= 0.50                  │ FLAG dispute → ESCALATE Tier 1 → LOG           │
│ Duplicate state transition attempt   │ REJECT → return current state → LOG            │
│ Financial instruction dispatch fail  │ RETRY 3x → STOP → MANUAL_QUEUE → ALERT        │
│ Legal hold conflict                  │ STOP transitions → NOTIFY legal team → HOLD    │
│ Data corruption in evidence          │ QUARANTINE evidence → LOG → NOTIFY claimant    │
│ SLA breach (any tier)                │ AUTO-ESCALATE to next tier → ALERT OPS         │
└──────────────────────────────────────┴─────────────────────────────────────────────────┘

ESCALATION_TARGETS:
  DISPUTE_OPS_TEAM:    dispute-ops@ecoskiller.internal
  SENIOR_ARBITER:      senior-arbiter@ecoskiller.internal
  LEGAL_TEAM:          legal@ecoskiller.internal
  ML_OPS_TEAM:         ml-ops@ecoskiller.internal
  OBSERVABILITY_AGENT: emit AGENT_FAILURE_EVENT

RETRY_POLICY:
  Transient (network / DB timeout):
    Attempt 1: immediate | 2: 3s | 3: 12s | 4: 45s
    Max retries: 4 → STOP → LOG_INCIDENT → MANUAL_QUEUE
  No silent failures under any condition
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 12 — INTER-AGENT DEPENDENCY MAP
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
UPSTREAM_AGENTS:
  - ROYALTY_ENGINE                  → royalty ledger snapshot
  - COPY_DETECTION_ENGINE           → originality score + similarity hash
  - IDEA_DNA_AGENT                  → idea vector comparison
  - BILLING_SERVICE                 → transaction ledger
  - CREATOR_REWARD_LEDGER           → creator earnings history
  - TRAINER_REVENUE_AGENT           → trainer wallet and payout history
  - REFERRAL_ENGINE                 → referral reward ledger
  - SCHOOL_GROWTH_FORECAST_AGENT    → institutional royalty share context
  - AUDIT_LOG_SERVICE               → prior audit references
  - INSTITUTION_VERIFICATION_AGENT  → institution identity for institutional disputes

DOWNSTREAM_AGENTS:
  - BILLING_SERVICE                 → receives financial adjustment instruction (advisory)
  - ROYALTY_ENGINE                  → receives recalculation trigger on UPHELD
  - NOTIFICATION_DISPATCH_AGENT     → receives verdict + state-change notifications
  - OBSERVABILITY_AGENT             → receives metrics, SLA flags, anomaly events
  - TRANSPARENCY_REPORT_AGENT       → receives anonymized dispute outcome
  - MODERATION_AGENT                → receives content violation escalations
  - FEATURE_STORE_AGENT             → receives dispute outcome feature vectors
  - ANTIGRAVITY_LEADERBOARD_AGENT   → receives trust score impact signal

EVENT_TRIGGERS (emitted via Redis Streams):
  - DISPUTE_OPENED
  - EVIDENCE_COLLECTION_COMPLETE
  - ML_ANALYSIS_COMPLETE
  - DISPUTE_VERDICT_ISSUED
  - FINANCIAL_ADJUSTMENT_QUEUED     (conditional: UPHELD or PARTIALLY_UPHELD)
  - HUMAN_REVIEW_REQUIRED           (conditional: escalated)
  - SLA_BREACH_EVENT                (conditional)
  - DISPUTE_APPEAL_FILED            (conditional)
  - DISPUTE_PERMANENTLY_CLOSED
  - TRUST_SCORE_UPDATE_EVENT
  - AGENT_FAILURE_EVENT             (conditional)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 13 — PASSIVE INTELLIGENCE COMPATIBILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

After every verdict, the agent emits structured feature vectors to FEATURE_STORE_AGENT:

```json
EMIT_FEATURE_VECTOR (per verdict): {
  "entity_type":    "user | institution",
  "entity_id":      "claimant_id or institution_id (UUID)",
  "tenant_id":      "UUID",
  "feature_name":   "dispute_outcome_claimant",
  "feature_value":  "ENUM['UPHELD','PARTIALLY_UPHELD','DISMISSED','ESCALATED']",
  "timestamp_utc":  "ISO-8601",
  "source_agent":   "ROYALTY_DISPUTE_RESOLUTION_AGENT",
  "model_version":  "string"
}
```

Additional vectors emitted per execution:

```
creator_fraud_signal_score           → float
creator_dispute_legitimacy_rate      → float (rolling 90d)
content_originality_dispute_flag     → boolean
billing_variance_detected_flag       → boolean
trust_impact_delta_claimant          → float
trust_impact_delta_respondent        → float | null
```

These feed into the PASSIVE_INTELLIGENCE_AGENT and inform:
- Creator trust score computation
- Content ranking adjustments for disputed content
- Institution reputation signals
- Antigravity tier eligibility adjustments

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 14 — INNOVATION ECONOMY COMPATIBILITY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

When dispute_category = content_originality, the agent interfaces with
the full innovation economy pipeline:

```json
EMIT_ORIGINALITY_DISPUTE_SIGNAL (to IDEA_DNA_AGENT + COPY_DETECTION_ENGINE): {
  "dispute_id":             "UUID",
  "disputed_content_id":    "UUID",
  "claimant_idea_vector":   "float[] (from IDEA_DNA_AGENT)",
  "respondent_idea_vector": "float[] | null",
  "similarity_hash":        "string",
  "originality_score_at_dispute": "float [0.0–1.0]",
  "dispute_outcome":        "ENUM['UPHELD','PARTIALLY_UPHELD','DISMISSED'] | null",
  "timestamp_utc":          "ISO-8601",
  "source_agent":           "ROYALTY_DISPUTE_RESOLUTION_AGENT"
}
```

Post-verdict impact on innovation economy:
- UPHELD originality dispute → COPY_DETECTION_ENGINE updates similarity threshold training data
- UPHELD royalty dispute → ROYALTY_ENGINE recalculates affected period
- DISMISSED originality dispute → claimant originality_credibility_score decremented
- All outcomes contribute to platform-wide originality corpus quality

Compatible with:
  - IDEA_DNA_AGENT (similarity retraining signal)
  - ROYALTY_ENGINE (recalculation trigger)
  - COPY_DETECTION_ENGINE (threshold calibration)

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 15 — GROWTH ENGINE & TRUST HOOK
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Dispute outcomes affect creator and institution trust scores, which feed
Antigravity ranking directly:

```
TRUST_SCORE_UPDATE_EVENT (emitted on every verdict):
  {
    "entity_type":            "user | institution",
    "entity_id":              "UUID",
    "trust_delta":            "float (positive or negative)",
    "trust_update_reason":    "ENUM['dispute_upheld','dispute_dismissed',
                                'fraud_flag','false_report_penalty',
                                'dispute_resolved_cleanly']",
    "timestamp_utc":          "ISO-8601",
    "source_agent":           "ROYALTY_DISPUTE_RESOLUTION_AGENT"
  }

RANK_UPDATE_EVENT (emitted to INSTITUTION_RANK_AGENT where applicable):
  {
    "entity_type":            "institution",
    "entity_id":              "UUID",
    "rank_signal":            "float (trust-adjusted)",
    "dimension":              "dispute_integrity",
    "timestamp_utc":          "ISO-8601"
  }

XP_UPDATE_EVENT (where platform policy assigns XP for dispute participation):
  {
    "entity_type":            "user",
    "entity_id":              "claimant_id",
    "xp_delta":               "integer (positive for legitimate, negative for false)",
    "xp_source":              "ROYALTY_DISPUTE_RESOLUTION_AGENT",
    "timestamp_utc":          "ISO-8601"
  }
```

False report penalty rule:
- 3 dismissed disputes in 90 days → temporary dispute submission suspension (30 days)
- 5 dismissed disputes in 180 days → permanent dispute credibility flag on profile

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 16 — SCALE, COMPATIBILITY & ECONOMICS
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

### 16.1 Scale Architecture

```
TARGET_SCALE: 10M–100M users on Ecoskiller Antigravity

Dispute volume projections:
  At 10M users: ~5,000 disputes/month (~167/day)
  At 100M users: ~50,000 disputes/month (~1,667/day)

Active pipeline capacity:
  Max concurrent open disputes: 500
  ML analysis workers:          20 async pods (HPA)
  Human review queue:           SLA-managed, Tier 1/2/3

Storage design:
  Dispute records:     PostgreSQL — partitioned by tenant_id + year-month
  Evidence documents:  S3-compatible object storage — tenant-isolated buckets
  Audit logs:          Append-only PostgreSQL + cold storage replication
  Feature vectors:     OpenSearch (FEATURE_STORE_AGENT index)

Data retention:
  Active disputes:     until PERMANENTLY_CLOSED + 90 days hot storage
  Closed disputes:     2 years hot, 7 years cold
  Audit logs:          7 years minimum (legal compliance requirement)
  Evidence documents:  7 years (aligned with audit retention)
```

### 16.2 Economic Integrity

```
ROYALTY RECALCULATION PIPELINE (on UPHELD verdict):
  Step 1: Agent emits FINANCIAL_ADJUSTMENT_QUEUED event (advisory)
  Step 2: BILLING_SERVICE receives signal — human finance ops confirms
  Step 3: ROYALTY_ENGINE recalculates affected period
  Step 4: Corrected amount credited or debited via CreatorRewardLedger
  Step 5: Audit entry appended with financial_adjustment_reference
  Step 6: Claimant and respondent notified via NOTIFICATION_DISPATCH_AGENT

This agent NEVER executes financial transactions directly.
All financial signals are advisory — human confirmation required for
amounts above AUTO_RESOLVE_THRESHOLD.

ECONOMIC IMPACT SIGNALS emitted to ROYALTY_ENGINE:
  {
    "dispute_id":               "UUID",
    "disputed_ledger_entry_id": "UUID",
    "correction_action":        "ENUM['RECALCULATE','CREDIT','NO_ACTION']",
    "correction_period_start":  "ISO-8601",
    "correction_period_end":    "ISO-8601",
    "estimated_correction_amount": "numeric",
    "currency":                 "ISO-4217",
    "advisory_only":            true,
    "source_agent":             "ROYALTY_DISPUTE_RESOLUTION_AGENT"
  }

PLATFORM TRUST ECONOMICS:
  - High dispute resolution rate → increases institution Antigravity tier eligibility
  - Consistent false report filing → reduces creator visibility score
  - Clean dispute record (no upheld disputes as respondent) → positive trust multiplier
  - Dispute resolution speed (within SLA) → positive platform credibility signal
    emitted to TRANSPARENCY_REPORT_AGENT
```

### 16.3 Compatibility Matrix

```
COMPATIBLE WITH:
  ✅ Ecoskiller Core Platform (Billing, Royalty, Creator, Trainer domains)
  ✅ Dojo For Arts (trainer payout disputes, session revenue disputes)
  ✅ Antigravity Leaderboard Engine (trust signal input)
  ✅ Innovation Economy (Royalty + Idea DNA + Copy Detection pipeline)
  ✅ Passive Intelligence Layer (Feature Store)
  ✅ Moderation Stack (content escalation routing)
  ✅ Transparency Report Engine (R62 compliance)
  ✅ Government & Authority Escalation Router (R63 compliance)
  ✅ Long-Term Archival & Legal Hold System (R60 compliance)
  ✅ Notification & Communication Stack
  ✅ Keycloak RBAC (JWT scope enforcement, tiered reviewer roles)
  ✅ Kong API Gateway (rate limiting + auth + signature validation)
  ✅ CQRS Event Store (append-only)
  ✅ OpenSearch (evidence search + case indexing)
  ✅ Kubernetes autoscaling (stateless pods)
  ✅ CI/CD contract-gate system (Lane D + Lane F dependency)

NOT COMPATIBLE WITH (by design):
  ❌ Cross-tenant dispute data queries
  ❌ Direct financial transaction execution
  ❌ Autonomous verdict issuance on high-stakes cases
  ❌ PII exposure in AI analysis layer
  ❌ State machine rollback (transitions are append-only)
  ❌ Overriding legal hold flags
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 17 — PERFORMANCE MONITORING
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
METRICS (all exposed to OBSERVABILITY_AGENT):

  dispute_submission_rate:           submissions per hour
  auto_resolution_rate:              % of disputes resolved without human (target > 60%)
  human_escalation_rate:             % requiring human review
  false_report_rate:                 % of submissions flagged as fraudulent
  verdict_accuracy_proxy:            % of verdicts not appealed (proxy for quality)
  appeal_rate:                       % of closed disputes that receive appeal
  appeal_overturn_rate:              % of appeals that change verdict
  sla_compliance_rate:               % of cases resolved within SLA (target > 95%)
  sla_breach_count:                  rolling 24h count
  avg_resolution_time_hours:         by dispute_category
  financial_adjustment_accuracy:     % of adjustments confirmed by BILLING_SERVICE
  ml_confidence_distribution:        histogram of confidence scores
  low_confidence_escalation_rate:    % of cases with confidence < 0.65
  fraud_detection_precision:         true positive / (true positive + false positive)
  queue_depth:                       active disputes in pipeline (alert > 300)
  model_drift_indicator:             0 = stable | 1 = drift detected
  ai_layer_availability:             % uptime of AI rationale generation

DASHBOARD:
  Grafana panel: ROYALTY_DISPUTE_RESOLUTION_AGENT
  Alerts: PagerDuty → DISPUTE_OPS_TEAM, ML_OPS_TEAM

MUST INTEGRATE WITH:
  OBSERVABILITY_AGENT   (metrics push every 60s)
  AUDIT_LOG_SERVICE     (every state transition)
  TRANSPARENCY_REPORT_AGENT (monthly anonymized outcome aggregation)
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 18 — VERSIONING POLICY
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```
VERSIONING LAW:
  All changes: Add-only, versioned via semver, backward compatible,
               migration documented, rollback safe

  MAJOR version bump required for:
    - Input schema breaking change
    - Output schema breaking change
    - State machine new terminal state
    - ML model architecture change
    - Dispute category addition (new dispute type)

  MINOR version bump required for:
    - New optional input field
    - New optional output field
    - New feature vector emission
    - New escalation tier added
    - SLA threshold update

  PATCH version bump required for:
    - Bug fixes in validation logic
    - Prompt updates (minor wording)
    - Performance improvements
    - Threshold calibration (non-breaking)

  NO in-place schema mutation permitted.
  NO historical dispute record modification permitted.
  NO audit log deletion permitted under any version bump.
  NO state machine rollback permitted under any version bump.
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 19 — DATABASE SCHEMA (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```sql
-- Core dispute records (append-only after creation)
CREATE TABLE royalty_disputes (
  id                         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  case_reference             VARCHAR(30) NOT NULL UNIQUE,
  tenant_id                  UUID NOT NULL,
  claimant_id                UUID NOT NULL,
  claimant_type              VARCHAR(20) NOT NULL,
  respondent_id              UUID,
  respondent_type            VARCHAR(20),
  dispute_category           VARCHAR(30) NOT NULL,
  disputed_entity_id         UUID NOT NULL,
  disputed_entity_type       VARCHAR(30) NOT NULL,
  dispute_description        TEXT NOT NULL,
  claimed_financial_impact   NUMERIC(14,4) NOT NULL DEFAULT 0,
  claimed_currency           CHAR(3) NOT NULL,
  dispute_state              VARCHAR(30) NOT NULL DEFAULT 'OPENED',
  verdict                    VARCHAR(30),
  verdict_rationale          TEXT,
  verdict_issued_at_utc      TIMESTAMPTZ,
  verdict_issued_by          VARCHAR(20),
  confidence_score           NUMERIC(5,4),
  fraud_signal_score         NUMERIC(5,4),
  originality_score          NUMERIC(5,4),
  billing_variance_amount    NUMERIC(14,4),
  ml_model_version           VARCHAR(50),
  prompt_version             VARCHAR(50),
  input_hash                 CHAR(64) NOT NULL,
  appeal_window_expires_utc  TIMESTAMPTZ,
  appeal_filed               BOOLEAN DEFAULT FALSE,
  legal_hold_flag            BOOLEAN DEFAULT FALSE,
  prior_dispute_id           UUID,
  audit_reference            UUID NOT NULL,
  created_at                 TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at                 TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- State transition log (immutable append)
CREATE TABLE dispute_state_transitions (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  dispute_id       UUID NOT NULL REFERENCES royalty_disputes(id),
  from_state       VARCHAR(30) NOT NULL,
  to_state         VARCHAR(30) NOT NULL,
  actor_id         UUID NOT NULL,
  actor_type       VARCHAR(20) NOT NULL,
  transition_note  TEXT,
  audit_reference  UUID NOT NULL,
  prior_hash       CHAR(64),
  created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Immutability enforcement
CREATE RULE no_delete_disputes AS ON DELETE TO royalty_disputes DO INSTEAD NOTHING;
CREATE RULE no_delete_transitions AS ON DELETE TO dispute_state_transitions DO INSTEAD NOTHING;

-- Indexes
CREATE INDEX idx_rd_tenant_claimant ON royalty_disputes (tenant_id, claimant_id);
CREATE INDEX idx_rd_state ON royalty_disputes (dispute_state);
CREATE INDEX idx_rd_category ON royalty_disputes (dispute_category);
CREATE INDEX idx_rd_entity ON royalty_disputes (disputed_entity_id, disputed_entity_type);
CREATE INDEX idx_dst_dispute ON dispute_state_transitions (dispute_id, created_at DESC);
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 20 — API CONTRACT (REFERENCE)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

```yaml
# OpenAPI 3.1 reference (excerpt)

/v1/disputes:
  post:
    summary: Submit new royalty dispute
    security:
      - BearerAuth: [dispute_resolution:submit]
    responses:
      202: Accepted (async pipeline triggered)
      400: Validation failure — structured error
      403: Tenant/scope violation
      429: Rate limit exceeded (5 disputes per 30 days)

/v1/disputes/{dispute_id}:
  get:
    summary: Get dispute status and result
    security:
      - BearerAuth: [dispute_resolution:read]
    responses:
      200: Current dispute state and verdict (if issued)
      403: Not claimant or respondent — access denied
      404: Dispute not found

/v1/disputes/{dispute_id}/appeal:
  post:
    summary: File appeal on closed dispute
    security:
      - BearerAuth: [dispute_resolution:submit]
    responses:
      202: Appeal accepted — dispute reopened
      400: Appeal window expired or invalid
      409: Appeal already filed

/v1/disputes/{dispute_id}/review (internal — reviewer role only):
  post:
    summary: Submit human reviewer verdict
    security:
      - BearerAuth: [dispute_resolution:review]
    responses:
      200: Verdict recorded
      403: Insufficient reviewer tier for this case
```

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 21 — NON-NEGOTIABLE RULES (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

This agent MUST NOT under any condition:

```
❌ Issue a final verdict autonomously on cases above AUTO_RESOLVE_THRESHOLD
❌ Execute financial transactions — advisory signals only, always
❌ Modify or delete historical dispute records or state transitions
❌ Auto-delete audit logs or override legal hold flags
❌ Allow AI layer to issue verdict independently of ML confidence score
❌ Rollback a committed state machine transition
❌ Mix dispute data across tenant boundaries
❌ Execute outside declared PRIMARY_DOMAIN (royalty_dispute_resolution)
❌ Access raw claimant PII in AI analysis layer
❌ Accept dispute submissions from unverified or suspended accounts
❌ Emit financial adjustment signals without linked audit_reference UUID
❌ Silently absorb validation failures — every failure must produce a log entry
❌ Bypass Keycloak JWT / RBAC scope checks
❌ Operate without upstream agent contracts confirmed ready
❌ Produce partial outputs — STOP or COMPLETE, never partial
❌ Override MODERATION_AGENT, LEGAL escalation, or GOVERNANCE_AGENT decisions
❌ Allow a single claimant to circumvent rate limits via alternate accounts
   (cross-account detection enforced via device fingerprint + IP heuristics)
```

Violation of any rule above → STOP EXECUTION → LOG_INCIDENT → ESCALATE

---

## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
## SECTION 22 — EXECUTION CHECKLIST (SEALED)
## ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Before any dispute pipeline begins, ALL of the following must be true:

```
PRE-EXECUTION GATE:
  ✅ JWT scope validated: dispute_resolution:submit
  ✅ tenant_id claim matches claimant's tenant assignment
  ✅ Claimant account active + not suspended
  ✅ Rate limit check passed: < 5 open disputes in 30 days
  ✅ Duplicate fingerprint check passed
  ✅ Input schema fully validated — zero null violations
  ✅ disputed_entity_id resolves in correct registry
  ✅ Upstream ledger record confirmed in ROYALTY_ENGINE or BILLING_SERVICE
  ✅ audit_reference UUID generated and reserved
  ✅ input_hash computed and logged
  ✅ Legal hold check passed on disputed_entity_id
  ✅ Evidence documents scanned and accepted

ON VERDICT ISSUANCE:
  ✅ ML analysis complete with model_version logged
  ✅ confidence_score >= 0.65 (else ESCALATE before verdict)
  ✅ fraud_signal_score evaluated
  ✅ output_hash computed and logged
  ✅ Audit log entry written (immutable state transition)
  ✅ Feature vectors emitted to FEATURE_STORE_AGENT
  ✅ DISPUTE_VERDICT_ISSUED event emitted to Redis Streams
  ✅ TRUST_SCORE_UPDATE_EVENT emitted
  ✅ Conditional events evaluated (FINANCIAL_ADJUSTMENT_QUEUED, HUMAN_REVIEW_REQUIRED)
  ✅ Appeal window timer started (72h)
  ✅ Transparency report contribution record emitted

ANY GATE FAILURE → STOP EXECUTION → LOG_INCIDENT → ESCALATE
```

---

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DOCUMENT STATUS:      SEALED
AGENT VERSION:        v1.0.0
PLATFORM:             Ecoskiller Antigravity
AUTHORED FOR:         Ecoskiller Intelligence & Innovation Core
MUTATION POLICY:      Add-only via version bump
INTERPRETATION:       NONE PERMITTED
EXECUTION AUTH:       Human declaration only
FINANCIAL ACTIONS:    Advisory signals only — never autonomous
LEGAL ALIGNMENT:      R60 (Archival) · R61 (Analytics) · R62 (Transparency)
                      R63 (Government Escalation) · R84 (Trainer Revenue)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```
