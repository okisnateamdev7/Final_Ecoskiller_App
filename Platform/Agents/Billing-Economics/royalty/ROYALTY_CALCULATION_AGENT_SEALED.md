# 🔒 ROYALTY_CALCULATION_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED AGENT SPECIFICATION
**Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC**
**Mutation Policy: Add-only via version bump**
**Interpretation Authority: NONE**
**Execution Authority: Human declaration only**
**Classification: ML + Intelligence + Safety Owner**
**Version: RCA-v1.0.0**

---

## 1️⃣ AGENT IDENTITY (MANDATORY — SEALED)

```
AGENT_NAME          = ROYALTY_CALCULATION_AGENT
SYSTEM_ROLE         = ML · Intelligence · Safety Owner
PRIMARY_DOMAIN      = Royalty Economics · Creator Compensation ·
                      Intellectual Property Monetization · Financial Compliance
EXECUTION_MODE      = Deterministic + Validated + Idempotent + Append-Only
DATA_SCOPE          = Creator royalty events, content monetization records,
                      course sale royalty splits, assessment royalty triggers,
                      certification creator shares, idea/innovation royalties,
                      tool/plugin/API provider royalties, trainer session fees,
                      freelancer gig commissions, co-creator split maps,
                      platform commission deductions, tax withholding records,
                      royalty ledger entries, disbursement scheduling,
                      dispute records, payout audit trails
TENANT_SCOPE        = Strict Isolation — Zero cross-tenant royalty data access
FAILURE_POLICY      = HALT on ambiguity → LOG_INCIDENT → ESCALATE
AGENT_CLASS         = Financial Calculation · Creator Economics · Compliance Safety
SAFETY_CLASS        = CRITICAL — Royalty records are append-only, legally binding,
                      tamper-proof, and subject to financial audit
```

This agent **never assumes** royalty split percentages, commission tiers, or payout rules.
All rules must be explicitly declared in the ROYALTY_POLICY_REGISTRY before execution begins.
Absent policy = HALT + ESCALATE. No default splits permitted.

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved
The ROYALTY_CALCULATION_AGENT is the **single authoritative calculator** for all creator, trainer, freelancer, and knowledge-partner compensation across the Ecoskiller Antigravity platform. It receives royalty triggers from the REVENUE_INGESTION_AGENT, resolves the applicable royalty policy for each monetization event, calculates gross royalty, applies platform commission, deducts applicable tax withholding (TDS/GST per jurisdiction), resolves co-creator splits, schedules disbursement, and emits legally traceable, immutable royalty ledger entries.

This agent governs compensation for all content-generating user types (41–200 in the 300 user registry) across all five domain tracks (Arts, Commerce, Science, Technology, Administration) and all marketplace product types.

### Problem Boundary
This agent **calculates and records** royalties. It does **NOT** disburse funds — disbursement is owned by the PAYOUT_ORCHESTRATOR_AGENT. It does **NOT** resolve disputes — disputes are owned by the DISPUTE_RESOLUTION_AGENT. It **feeds** both.

### Input Consumed
- `ROYALTY_TRIGGER` events from REVENUE_INGESTION_AGENT
- Royalty policy declarations from ROYALTY_POLICY_REGISTRY
- Creator/trainer profile data from IDENTITY_SERVICE (tax status, jurisdiction, GST number, PAN)
- Co-creator split maps from CONTENT_REGISTRY
- Platform commission tier from TENANT_COMMISSION_CONFIG
- Tax withholding rules from TAX_COMPLIANCE_SERVICE
- Exchange rate data from CURRENCY_CONVERSION_SERVICE (for international royalties)
- Dispute flags from DISPUTE_RESOLUTION_AGENT (to hold pending disbursement)

### Output Produced
- Normalized `RoyaltyLedgerEntry` committed to append-only royalty ledger
- `RoyaltyDisbursementSchedule` record for PAYOUT_ORCHESTRATOR_AGENT
- `TaxDeductionRecord` for TAX_COMPLIANCE_SERVICE
- `CreatorEarningsSnapshot` update for TENANT_ANALYTICS_AGENT
- `FeatureVector` emitted to FEATURE_STORE_AGENT
- `GrowthHook` trigger to GROWTH_ENGINE (on creator earnings milestones)
- `IdeaRoyaltySignal` to IDEA_DNA_AGENT (for monetized innovation content)
- `AuditLog` to immutable audit trail
- Anomaly flags to OBSERVABILITY_AGENT

### Downstream Agents Dependent on This Agent
- PAYOUT_ORCHESTRATOR_AGENT
- TAX_COMPLIANCE_SERVICE
- DISPUTE_RESOLUTION_AGENT
- TENANT_ANALYTICS_AGENT
- FEATURE_STORE_AGENT
- OBSERVABILITY_AGENT
- IDEA_DNA_AGENT
- GROWTH_ENGINE
- COMPLIANCE_AGENT
- REPORTING_AGENT

### Upstream Agents Feeding This Agent
- REVENUE_INGESTION_AGENT (primary trigger source)
- ROYALTY_POLICY_REGISTRY (policy resolution)
- CONTENT_REGISTRY (split maps, content metadata)
- IDENTITY_SERVICE (creator tax profile)
- TAX_COMPLIANCE_SERVICE (withholding rules)
- TENANT_COMMISSION_CONFIG (per-tenant platform rates)
- CURRENCY_CONVERSION_SERVICE (FX rates)
- DISPUTE_RESOLUTION_AGENT (dispute hold flags)

---

## 3️⃣ INPUT CONTRACT (STRICT — SEALED)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "royalty_trigger_id",
    "source_revenue_event_id",
    "tenant_id",
    "creator_id",
    "creator_type",
    "content_id",
    "content_type",
    "gross_transaction_amount_paise",
    "currency_code",
    "transaction_timestamp_utc",
    "platform_product_type",
    "royalty_policy_id",
    "trigger_source_agent"
  ],
  "optional_fields": [
    "co_creator_split_map",
    "coupon_discount_amount_paise",
    "tax_invoice_reference",
    "gst_number_creator",
    "pan_number_creator",
    "jurisdiction_code",
    "exchange_rate_to_inr",
    "original_currency_code",
    "dispute_hold_flag",
    "dispute_reference_id",
    "bulk_batch_reference",
    "subscription_period_from",
    "subscription_period_to",
    "content_version_id",
    "idea_dna_reference"
  ],
  "validation_rules": [
    "royalty_trigger_id must be UUID v4 — reject any other format",
    "source_revenue_event_id must resolve to a committed event in REVENUE_INGESTION_AGENT ledger — reject unresolved references",
    "gross_transaction_amount_paise must be integer > 0 — zero and negative values rejected",
    "currency_code must be ISO 4217 — reject unknown codes",
    "creator_type must belong to ENUM: [subject_trainer, technical_trainer, corporate_trainer, soft_skills_trainer, career_mentor, startup_mentor, coding_instructor, ai_instructor, data_science_instructor, cloud_instructor, cybersecurity_instructor, devops_instructor, robotics_instructor, iot_instructor, blockchain_instructor, ui_ux_instructor, digital_marketing_instructor, seo_instructor, sales_trainer, hr_trainer, finance_trainer, faculty_professor, visiting_faculty, guest_lecturer, industry_expert_mentor, freelance_trainer, remote_instructor, workshop_facilitator, bootcamp_lead, assessment_evaluator, interview_coach, resume_reviewer, portfolio_reviewer, hackathon_judge, certification_examiner, freelance_developer, freelance_designer, freelance_marketer, course_creator, micro_course_creator, certification_creator, assessment_creator, prompt_engineer, ai_workflow_designer, tool_builder, plugin_developer, api_provider, saas_creator, research_author, technical_blogger, youtube_educator, community_educator, knowledge_partner]",
    "content_type must belong to ENUM: [course, micro_course, certification, assessment, live_session, recorded_session, workshop, dojo_scenario, project_template, tool, plugin, api_product, resume_template, portfolio_template, research_paper, ebook, prompt_pack, ai_workflow, digital_badge, skill_path, idea_submission]",
    "platform_product_type must belong to ENUM: [marketplace_sale, subscription_allocation, one_time_purchase, session_fee, royalty_per_use, bounty_release, grant_allocation]",
    "royalty_policy_id must resolve to an active, non-expired policy in ROYALTY_POLICY_REGISTRY — reject unknown or expired policy IDs",
    "If co_creator_split_map present: sum of all creator percentages + platform_commission_percent must equal 100.00 — reject any imbalance",
    "idempotency check: royalty_trigger_id must be globally unique — duplicate = REJECT with idempotency log",
    "If dispute_hold_flag = true: dispute_reference_id is REQUIRED",
    "If original_currency_code != INR: exchange_rate_to_inr is REQUIRED"
  ],
  "security_checks": [
    "Verify JWT bearer token of trigger source — only REVENUE_INGESTION_AGENT system token accepted",
    "Confirm tenant_id matches creator_id's registered tenant — no cross-tenant royalty events",
    "Verify creator_id exists and is ACTIVE in IDENTITY_SERVICE — reject suspended/deleted creators",
    "Verify content_id belongs to creator_id in CONTENT_REGISTRY — reject misattributed content",
    "Rate-limit: max 5,000 royalty triggers/minute per tenant",
    "Royalty policy version must match currently active policy — reject stale policy references"
  ],
  "domain_checks": [
    "content_id must resolve to existing, published content in CONTENT_REGISTRY",
    "royalty_policy_id must be active for the content_type + platform_product_type combination",
    "If jurisdiction_code present: validate against supported jurisdiction list in TAX_COMPLIANCE_SERVICE",
    "If gst_number_creator present: cross-validate with GST registry — log mismatch as TAX_ANOMALY_FLAG",
    "If dispute_hold_flag = true: halt disbursement scheduling — only record calculation, do not schedule payout"
  ]
}
```

**Rules:**
- **No null tolerance** on required fields — zero exceptions without human-declared explicit null policy
- **Reject malformed data** immediately — no repair attempts
- **Log all validation failures** to audit stream with input_hash, rejection_reason, timestamp
- **No partial royalty processing** — fully atomic commit or full rejection
- **No rounding shortfall absorption** — if paise splits produce remainder, remainder is recorded in PLATFORM_ROUNDING_RESERVE and logged

---

## 4️⃣ OUTPUT CONTRACT (STRICT — SEALED)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "royalty_ledger_id":               "UUID",
    "royalty_trigger_id":              "UUID",
    "source_revenue_event_id":         "UUID",
    "tenant_id":                       "UUID",
    "creator_id":                      "UUID",
    "content_id":                      "UUID",
    "gross_transaction_amount_paise":  "integer",
    "platform_commission_paise":       "integer",
    "platform_commission_percent":     "decimal(5,2)",
    "gross_royalty_before_tax_paise":  "integer",
    "tax_withheld_paise":              "integer",
    "tax_type":                        "ENUM[TDS, GST, IGST, none]",
    "net_royalty_payable_paise":       "integer",
    "co_creator_splits": [
      {
        "creator_id":           "UUID",
        "split_percent":        "decimal(5,2)",
        "net_payout_paise":     "integer",
        "tax_withheld_paise":   "integer"
      }
    ],
    "rounding_reserve_paise":          "integer",
    "disbursement_schedule_id":        "UUID",
    "disbursement_hold":               "boolean",
    "royalty_policy_id":               "UUID",
    "royalty_policy_version":          "string",
    "currency_code":                   "ISO4217",
    "exchange_rate_applied":           "decimal(10,6)",
    "processing_status":               "ENUM[calculated, held_dispute, held_tax_anomaly, escalated]"
  },
  "confidence_score":   "0.0–1.0",
  "model_version":      "RCA-ML-v{major}.{minor}.{patch}",
  "audit_reference":    "UUID",
  "next_trigger_events": [
    "ROYALTY_LEDGER_COMMITTED",
    "DISBURSEMENT_SCHEDULED",
    "TAX_RECORD_COMMITTED",
    "CREATOR_SNAPSHOT_UPDATED",
    "FEATURE_VECTOR_PUSHED",
    "GROWTH_HOOK_TRIGGERED",
    "OBSERVABILITY_EVENT_EMITTED",
    "COMPLIANCE_LOG_WRITTEN"
  ]
}
```

**All outputs must include:**
- Confidence score from anomaly/policy-match model
- Immutable model version reference
- Full traceability via audit_reference UUID
- Every split paise amount is individually recorded — no aggregated splits permitted
- Rounding reserve always declared, even if zero

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Component (PRIMARY — 70–80% logic weight)

```
MODEL_TYPE:
  Primary:    Multi-label Classification (XGBoost)
              → Classify royalty event into policy bucket
              → Detect anomalous royalty amounts vs. historical baseline
  Secondary:  Regression (Gradient Boosted Regressor)
              → Predict expected royalty amount for content_type + creator_type
              → Flag deviations > 3σ from predicted as ANOMALY
  Tertiary:   Time-Series (Prophet)
              → Creator earnings trend forecasting
              → Seasonal royalty velocity modeling
              → Feeds REPORTING_AGENT and TENANT_ANALYTICS_AGENT

FEATURES_USED:

  Policy Resolution Features:
    - content_type_encoded
    - platform_product_type_encoded
    - creator_type_encoded
    - tenant_commission_tier_encoded
    - royalty_policy_version
    - creator_tenure_days
    - content_age_days
    - content_total_sales_count
    - content_average_sale_value_paise

  Anomaly Detection Features:
    - gross_transaction_amount_paise (normalized)
    - amount_vs_content_historical_median_ratio
    - amount_vs_creator_historical_median_ratio
    - sales_velocity_1hr (transaction count per hour for this content)
    - sales_velocity_24hr
    - discount_ratio (coupon_discount / gross_amount)
    - is_bulk_batch_event (boolean)
    - co_creator_count
    - split_map_entropy (measure of split complexity)
    - jurisdiction_tax_rate
    - currency_conversion_flag

  Tax Compliance Features:
    - jurisdiction_code_encoded
    - creator_gst_registered_flag
    - creator_pan_verified_flag
    - tds_applicable_flag
    - creator_annual_earnings_ytd_paise (for TDS threshold detection)
    - content_type_tax_category

  Earnings Forecast Features (Time-Series):
    - creator_id
    - content_id
    - earnings_7d_rolling
    - earnings_30d_rolling
    - seasonality_index
    - platform_wide_sales_trend

TRAINING_FREQUENCY:
  Classification Model:  Monthly (1st of month 02:00 UTC)
  Anomaly Regression:    Weekly (Monday 02:00 UTC)
  Forecast Model:        Weekly (Tuesday 02:00 UTC)
  Tax Model:             Quarterly (aligned to tax calendar)

DRIFT_DETECTION:
  - Monitor royalty amount distribution per content_type (KS-test weekly)
  - Monitor classification accuracy on labeled holdout (threshold: <93% → retrain trigger)
  - Monitor anomaly flag false positive rate (threshold: >4% FPR → alert)
  - Monitor tax calculation error rate against manually audited sample (threshold: >0.1% → HALT tax engine)
  - Monitor feature null rate per field (threshold: >1% → alert)

VERSION_CONTROL:
  - model_version stored as immutable tag on every output record
  - No inference permitted from untagged or expired model versions
  - Model rollback requires human admin declaration + dual approval
  - Tax model changes require Compliance Officer sign-off before activation
```

### AI Component (SECONDARY — 20–30% logic weight)

```
AI_USAGE_SCOPE:
  1. Ambiguous royalty policy resolution
     → When royalty_policy_id is absent or content_type spans multiple policy buckets
     → AI proposes policy bucket → ML validates classification
     → If AI + ML confidence combined < 0.80 → ESCALATE_TO = ROYALTY_POLICY_ADMIN
  
  2. Co-creator split dispute pre-analysis
     → When DISPUTE_RESOLUTION_AGENT sends a split dispute flag
     → AI generates structured summary of split history for human reviewer
     → AI NEVER resolves dispute — human-only resolution
  
  3. Natural language audit log enrichment
     → AI generates human-readable decision summary for each royalty record
     → Appended to audit log as enrichment field only — non-binding
  
  4. Tax anomaly narrative
     → When TAX_ANOMALY_FLAG is raised, AI generates structured explanation
     → Forwarded to TAX_COMPLIANCE_SERVICE for human review

PROMPT_GOVERNANCE:
  - All prompts versioned: PROMPT_RCA_POLICY_v{n}, PROMPT_RCA_AUDIT_v{n}, PROMPT_RCA_TAX_v{n}
  - Deterministic structure — zero open-ended generation
  - AI response must conform to structured JSON schema before use
  - AI timeout = 3 seconds → fallback to ML-only path → log AI_TIMEOUT_FLAG
  - No AI output may modify a calculated paise amount
  - No AI output may override a committed royalty ledger entry

AI must ASSIST ML policy resolution. AI cannot commit, calculate, or disburse.
AI can only propose — ML or human must confirm.
```

---

## 6️⃣ SCALABILITY DESIGN

```
EXPECTED_RPS         = 2,000 royalty trigger events/second
                       (burst to 20,000/second during flash sales, course launch events)
LATENCY_TARGET       = p50 < 100ms · p95 < 500ms · p99 < 1,500ms
MAX_CONCURRENCY      = 300 parallel processing workers (Kubernetes HPA governed)
QUEUE_STRATEGY       = Redis Streams with consumer groups
                       Priority queue: time-sensitive session fee royalties first
                       Dead Letter Queue (DLQ) after 3 failed attempts
                       DLQ retention: 72 hours
                       Bulk batch processing: async with progress tracking

STATELESS_EXECUTION  = TRUE — no shared in-memory state between events
IDEMPOTENCY          = Enforced via royalty_trigger_id deduplication (Redis + PostgreSQL)
ASYNC_PROCESSING     = TRUE — ACK upstream immediately, process asynchronously
HORIZONTAL_SCALING   = Kubernetes HPA: min 3 pods, max 80 pods, scale at 65% CPU

BATCH_PROCESSING:
  - Subscription allocation royalties processed in nightly batch (02:00 UTC)
  - Batch size: max 10,000 events per batch worker
  - Batch idempotency enforced via bulk_batch_reference UUID
  - Batch failures: individual event-level retry — no whole-batch retry

TAX_CALCULATION_SERIALIZATION:
  - Tax calculation for a single creator serialized (no parallel tax computation
    for same creator in same calendar period — prevents concurrent TDS threshold errors)
```

---

## 7️⃣ SECURITY ENFORCEMENT

```
TENANT_ISOLATION:
  - tenant_id verified against JWT claims on every event
  - No query may join creator data, royalty data, or policy data across tenant boundaries
  - Tenant-scoped encryption keys (per-tenant KMS key via HashiCorp Vault)
  - Creator royalty amounts NEVER exposed to other creators within same tenant

DOMAIN_ISOLATION:
  - Royalty domain data is NEVER shared with non-financial agents directly
  - Feature vectors emitted are anonymized: no raw paise amounts — only normalized ratios,
    earnings percentile ranks, and behavioral velocity signals
  - Creator identity in feature vectors: pseudonymized creator_hash

ROLE_BASED_AUTHORIZATION:
  Read Access:
    - FINANCE_MANAGER (own tenant only)
    - COMPLIANCE_ADMIN
    - AUDIT_ADMIN
    - PLATFORM_SUPER_ADMIN
    - Creator (own records only, read-only)
  
  Write Access:
    - REVENUE_INGESTION_AGENT (system trigger only — no human write path)
    - ROYALTY_POLICY_REGISTRY (policy update only — add-only, version bumped)
  
  Correction Access:
    - Requires dual human admin approval (FINANCE_MANAGER + COMPLIANCE_ADMIN)
    - Correction creates a new corrective ledger entry — original record NEVER modified
    - Correction reference links original and corrective entries bidirectionally

ENCRYPTION:
  - All royalty data at rest: AES-256 (tenant-scoped key)
  - All royalty data in transit: TLS 1.3 minimum
  - Tax records (PAN, GST): field-level encryption with restricted key access
  - Creator payout amounts: encrypted in transit to PAYOUT_ORCHESTRATOR_AGENT

AUDIT_LOGGING:
  - Append-only audit stream (PostgreSQL with write-once policy enforced at DB level)
  - Audit log replicated to MinIO immutable bucket with object-lock enabled
  - Log retention: 7 years minimum (financial and tax compliance requirement — India GST mandate)

ACCESS_LOG_TRACKING:
  - Every read of royalty records logs: accessor_id, purpose_code, timestamp, query_hash
  - Creator self-reads logged separately: creator_id, record_type, access_timestamp
```

---

## 8️⃣ AUDIT & TRACEABILITY (EVERY EXECUTION)

```json
AUDIT_LOG_RECORD: {
  "log_id":                  "UUID",
  "timestamp_utc":           "ISO8601",
  "actor_id":                "UUID (system agent or human)",
  "actor_type":              "ENUM[system_agent, human_admin, compliance_reviewer, creator_self_read]",
  "royalty_trigger_id":      "UUID",
  "source_revenue_event_id": "UUID",
  "input_hash":              "SHA256 of raw input payload",
  "output_hash":             "SHA256 of committed royalty ledger entry",
  "model_version":           "RCA-ML-v{n}",
  "policy_version":          "string",
  "decision_path": [
    "VALIDATION_PASSED",
    "IDEMPOTENCY_CHECK_PASSED",
    "SECURITY_CHECK_PASSED",
    "CREATOR_IDENTITY_VERIFIED",
    "CONTENT_OWNERSHIP_VERIFIED",
    "POLICY_RESOLVED",
    "ML_CLASSIFICATION_COMPLETED",
    "ROYALTY_CALCULATED",
    "TAX_WITHHOLDING_APPLIED",
    "CO_CREATOR_SPLITS_RESOLVED",
    "ROUNDING_RESERVE_ALLOCATED",
    "LEDGER_COMMITTED",
    "DISBURSEMENT_SCHEDULED",
    "DOWNSTREAM_EVENTS_EMITTED"
  ],
  "confidence_score":          "0.0–1.0",
  "anomaly_flags":             ["ENUM: none | amount_spike | velocity_spike | policy_mismatch | tax_anomaly | split_imbalance | dispute_hold | currency_anomaly | creator_suspended_post_trigger"],
  "tax_calculation_method":    "string",
  "processing_duration_ms":    "integer",
  "tenant_id":                 "UUID",
  "creator_id":                "UUID"
}
```

**Logs are immutable. Zero tolerance for modification, deletion, or overwrite of audit records.**
**Tax audit records are separately replicated to TAX_COMPLIANCE_SERVICE with sovereign-level immutability.**

---

## 9️⃣ FAILURE POLICY (SEALED)

| Failure Condition | Action |
|---|---|
| Invalid input (schema violation) | REJECT → LOG_VALIDATION_FAILURE → Return 422 to upstream |
| Duplicate royalty_trigger_id | REJECT → LOG_IDEMPOTENCY_HIT → Return 409 to upstream |
| Tenant not found / mismatch | HALT → LOG_SECURITY_VIOLATION → ESCALATE_TO = SECURITY_ADMIN |
| Creator not found in IDENTITY_SERVICE | HALT → LOG_CREATOR_NOT_FOUND → ESCALATE_TO = CONTENT_ADMIN → Hold trigger in DLQ |
| Content not owned by creator | HALT → LOG_OWNERSHIP_VIOLATION → ESCALATE_TO = TRUST_SAFETY_OFFICER |
| royalty_policy_id not found | HALT → LOG_POLICY_MISSING → ESCALATE_TO = ROYALTY_POLICY_ADMIN → Hold in DLQ |
| Royalty policy expired | HALT → LOG_POLICY_EXPIRED → ESCALATE_TO = ROYALTY_POLICY_ADMIN |
| Co-creator split map imbalance | HALT → LOG_SPLIT_IMBALANCE → ESCALATE_TO = FINANCE_MANAGER |
| ML model unavailable | HALT → LOG_MODEL_UNAVAILABLE → Queue event to DLQ → ESCALATE_TO = DEVOPS_ONCALL |
| ML confidence < 0.75 | Route to AI enrichment → if combined < 0.80 → ESCALATE_TO = ROYALTY_POLICY_ADMIN_QUEUE |
| AI timeout (>3s) | Fallback to ML-only path → Log AI_TIMEOUT_FLAG → Continue if ML ≥ 0.85 |
| Tax calculation failure | HALT → LOG_TAX_FAILURE → HOLD disbursement → ESCALATE_TO = TAX_COMPLIANCE_OFFICER |
| Tax anomaly detected (GST mismatch) | CALCULATE + HOLD disbursement → Log TAX_ANOMALY_FLAG → ESCALATE_TO = TAX_COMPLIANCE_OFFICER |
| Ledger commit failure | HALT → STOP_EXECUTION → LOG_LEDGER_FAILURE → ESCALATE_TO = DBA_ONCALL → Do NOT schedule disbursement |
| dispute_hold_flag = true | CALCULATE + RECORD → Do NOT schedule disbursement → Log DISPUTE_HOLD → Await DISPUTE_RESOLUTION_AGENT signal |
| Creator suspended post-trigger | HALT disbursement scheduling → Log CREATOR_SUSPENDED_FLAG → ESCALATE_TO = COMPLIANCE_ADMIN |
| DLQ exhausted (>72hr) | LOG_DLQ_OVERFLOW → ESCALATE_TO = PLATFORM_SUPER_ADMIN |
| Currency conversion unavailable | HALT → LOG_FX_UNAVAILABLE → Queue to DLQ → ESCALATE_TO = FINANCE_MANAGER |

```
RETRY_POLICY:
  Transient errors (DB timeout, network, FX service unavailable):
    Retry 3× with exponential backoff (2s, 4s, 8s) → DLQ on 3rd failure
  Non-transient errors (validation, security, ownership, policy missing):
    NO RETRY — immediate rejection + ESCALATE
  Tax hold events:
    No retry — held in ROYALTY_TAX_HOLD_QUEUE until TAX_COMPLIANCE_OFFICER releases

NO SILENT FAILURES. Every failure produces a log entry and an escalation event.
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP

```
UPSTREAM_AGENTS:
  - REVENUE_INGESTION_AGENT          → primary royalty trigger events
  - ROYALTY_POLICY_REGISTRY          → active policy rules (read-only)
  - CONTENT_REGISTRY                 → content ownership + co-creator split maps
  - IDENTITY_SERVICE                 → creator identity, tax profile, status
  - TAX_COMPLIANCE_SERVICE           → withholding rules, threshold checks
  - TENANT_COMMISSION_CONFIG         → per-tenant platform commission rates
  - CURRENCY_CONVERSION_SERVICE      → FX rates for international royalties
  - DISPUTE_RESOLUTION_AGENT         → dispute hold flags

DOWNSTREAM_AGENTS:
  - PAYOUT_ORCHESTRATOR_AGENT        → disbursement schedule records
  - TAX_COMPLIANCE_SERVICE           → tax deduction records
  - DISPUTE_RESOLUTION_AGENT         → royalty calculation input for dispute review
  - TENANT_ANALYTICS_AGENT           → creator earnings snapshots
  - FEATURE_STORE_AGENT              → anonymized creator behavioral feature vectors
  - OBSERVABILITY_AGENT              → metrics, anomaly alerts
  - IDEA_DNA_AGENT                   → monetized idea royalty signals
  - GROWTH_ENGINE                    → creator earnings milestone hooks
  - COMPLIANCE_AGENT                 → compliance event log
  - REPORTING_AGENT                  → royalty reporting data

EVENT_TRIGGERS_EMITTED:
  - ROYALTY_LEDGER_COMMITTED
  - ROYALTY_LEDGER_REJECTED
  - ROYALTY_HELD_DISPUTE
  - ROYALTY_HELD_TAX_ANOMALY
  - DISBURSEMENT_SCHEDULED
  - DISBURSEMENT_HELD
  - TAX_RECORD_COMMITTED
  - TAX_ANOMALY_FLAGGED
  - CO_CREATOR_SPLIT_RESOLVED
  - CREATOR_EARNINGS_SNAPSHOT_UPDATE
  - FEATURE_VECTOR_PUSHED
  - IDEA_ROYALTY_SIGNAL_EMITTED
  - GROWTH_EARNINGS_MILESTONE_TRIGGERED
  - COMPLIANCE_LOG_WRITTEN
  - ANOMALY_FLAG_RAISED
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

Every royalty event involving creator behavior emits an anonymized feature vector:

```json
EMIT_FEATURE_VECTOR → FEATURE_STORE_AGENT: {
  "user_id":        "creator_hash (pseudonymized UUID — NOT raw creator_id)",
  "feature_name":   "ENUM[royalty_earnings_velocity_7d, royalty_earnings_velocity_30d,
                    content_monetization_rate, avg_royalty_per_sale,
                    co_creator_collaboration_count, royalty_policy_tier,
                    tax_compliance_status, earnings_percentile_rank,
                    top_content_type_by_earnings, dispute_rate_30d]",
  "feature_value":  "float | integer | string | boolean",
  "timestamp":      "ISO8601",
  "source_agent":   "ROYALTY_CALCULATION_AGENT",
  "model_version":  "RCA-ML-v{n}"
}
```

**Raw paise amounts NEVER emitted in feature vectors.**
**Only normalized ratios, percentile ranks, and behavioral velocity signals.**

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

When royalty events are linked to idea submissions or innovation marketplace content:

```json
EMIT → IDEA_DNA_AGENT: {
  "idea_id":                  "UUID",
  "creator_id":               "UUID",
  "royalty_event_id":         "UUID",
  "monetization_confirmed":   "boolean",
  "originality_score_signal": "float (0.0–1.0, from content ML layer)",
  "royalty_tier_signal":      "ENUM[micro, standard, premium, enterprise]",
  "first_monetization_flag":  "boolean",
  "co_creator_count":         "integer"
}

EMIT → ROYALTY_ENGINE (self-reference for co-creator cascade):
  → When royalty event triggers downstream co-creator secondary splits,
    emit a secondary ROYALTY_TRIGGER for each co-creator
  → Secondary triggers must reference parent royalty_trigger_id
  → Depth limit: max 2 levels of cascade (primary + secondary — no tertiary cascades)

EMIT → COPY_DETECTION_ENGINE:
  → When content_type = [course, assessment, tool, plugin, ai_workflow]
  → Emit content fingerprint for ongoing originality monitoring
  → copy_detection_signal:
    { content_id, content_hash, creator_id, monetization_timestamp }
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

When royalty events cross defined creator earnings milestones:

```json
MILESTONES (declared in GROWTH_CONFIG registry — this agent READS, NEVER WRITES):
  - First royalty earned (any amount)
  - Cumulative earnings ≥ ₹1,000
  - Cumulative earnings ≥ ₹10,000
  - Cumulative earnings ≥ ₹1,00,000
  - First co-creator collaboration royalty
  - First international royalty (non-INR transaction)
  - 10 unique content items earning royalties
  - Top 10% earner in creator_type category (monthly)

TRIGGER → GROWTH_ENGINE: {
  "RANK_UPDATE_EVENT":    { "creator_id": "UUID", "rank_category": "creator_earnings", "rank_delta": "integer" },
  "XP_UPDATE_EVENT":      { "creator_id": "UUID", "xp_delta": "integer", "reason": "royalty_milestone_[milestone_name]" },
  "SHARE_TRIGGER_EVENT":  { "creator_id": "UUID", "milestone_type": "string", "share_template_id": "string" }
}
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```
METRICS (exported to Prometheus → Grafana):
  - royalty_events_processed_total (counter, by content_type, creator_type, tenant_id)
  - royalty_events_rejected_total (counter, by rejection_reason)
  - royalty_calculation_latency_ms (histogram: p50, p95, p99)
  - ml_confidence_score_distribution (histogram)
  - royalty_held_dispute_total (counter)
  - royalty_held_tax_anomaly_total (counter)
  - tax_withholding_calculation_accuracy (gauge, sampled audit)
  - co_creator_split_resolution_rate (gauge)
  - anomaly_flag_rate_percent (gauge, rolling 1hr window)
  - dlq_depth (gauge)
  - model_drift_indicator (gauge: KS-test score)
  - royalty_ledger_commit_success_rate (gauge)
  - payout_schedule_creation_success_rate (gauge)
  - creator_earnings_snapshot_staleness_seconds (gauge)

ALERT THRESHOLDS:
  - error_rate > 0.5%               → PAGE DEVOPS_ONCALL
  - p99 latency > 2,000ms           → PAGE DEVOPS_ONCALL
  - tax_anomaly_rate > 1%           → PAGE TAX_COMPLIANCE_OFFICER
  - dispute_hold_queue_depth > 500  → PAGE DISPUTE_RESOLUTION_TEAM
  - model_drift_indicator > 0.12    → TRIGGER RETRAINING PIPELINE
  - royalty_ledger_commit_rate < 99.5% → PAGE DBA_ONCALL + FINANCE_MANAGER
  - dlq_depth > 5,000               → PAGE PLATFORM_SUPER_ADMIN

INTEGRATES WITH: OBSERVABILITY_AGENT (Prometheus exporter endpoint)
TRACES: Jaeger distributed tracing for end-to-end royalty calculation spans
LOGS: Loki structured log ingestion
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```
ALL CHANGES:
  - Add-only (no field removal, no type change, no schema downgrade)
  - Every schema change = version bump (semver: MAJOR.MINOR.PATCH)
  - MAJOR bump: royalty policy engine changes, tax rule changes
  - MINOR bump: new content_type, new creator_type, new jurisdiction
  - PATCH bump: bug fixes, performance improvements
  - Backward compatible for minimum 2 major versions
  - Migration documented in CHANGELOG_RCA.md
  - Rollback requires human admin declaration + dual approval
    (FINANCE_MANAGER + COMPLIANCE_ADMIN)
  - Tax model version changes: require TAX_COMPLIANCE_OFFICER sign-off
  - Royalty policy version changes: require ROYALTY_POLICY_ADMIN sign-off
  - ML model versions pinned to agent version at deployment
  - All active policy versions remain resolvable for 7 years
    (historical royalty re-audit requirement)

CURRENT VERSION:   RCA-v1.0.0
MODEL VERSION:     RCA-ML-v1.0.0
PROMPT VERSIONS:   PROMPT_RCA_POLICY_v1 | PROMPT_RCA_AUDIT_v1 | PROMPT_RCA_TAX_v1
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES (ABSOLUTE — SEALED)

This agent **MUST NOT**:

- ❌ Assume a royalty split percentage — every split must be declared in ROYALTY_POLICY_REGISTRY or co_creator_split_map
- ❌ Modify any historical royalty ledger record — append-only is inviolable and legally binding
- ❌ Auto-delete audit logs under any condition or any retention policy automation
- ❌ Override COMPLIANCE_AGENT or DISPUTE_RESOLUTION_AGENT decisions
- ❌ Disburse funds — disbursement is PAYOUT_ORCHESTRATOR_AGENT's sole responsibility
- ❌ Resolve disputes — dispute resolution is DISPUTE_RESOLUTION_AGENT's sole responsibility
- ❌ Mix royalty data across tenant boundaries
- ❌ Execute outside declared royalty domain scope
- ❌ Emit raw paise amounts in feature vectors or growth hooks
- ❌ Allow cascade royalty depth beyond 2 levels (primary + one secondary)
- ❌ Calculate royalties for suspended, deleted, or unverified creators without explicit human override
- ❌ Accept royalty trigger from any source other than REVENUE_INGESTION_AGENT
- ❌ Apply a default commission rate when TENANT_COMMISSION_CONFIG is unavailable — HALT instead
- ❌ Process international royalties without a valid FX rate from CURRENCY_CONVERSION_SERVICE
- ❌ Skip tax withholding calculation even for zero-tax events — record zero explicitly
- ❌ Allow a single royalty trigger to create multiple ledger entries (idempotency is sacred)
- ❌ Operate without an active, versioned, drift-monitored ML model
- ❌ Allow AI to override a calculated paise amount
- ❌ Create hidden commission deductions not declared in TENANT_COMMISSION_CONFIG

---

## 1️⃣7️⃣ ROYALTY POLICY TAXONOMY (ECOSKILLER-SPECIFIC — SEALED)

All policies live in ROYALTY_POLICY_REGISTRY. This agent reads and enforces them.
Policies are versioned, add-only, and human-declared.

### Standard Royalty Rate Table (v1 — Human Declared)

| Content Type | Creator Type (Representative) | Creator Share | Platform Share | TDS Applicable |
|---|---|---|---|---|
| Course (marketplace sale) | Course Creator, Technical Trainer | 80% | 20% | Yes (>₹30,000/yr threshold) |
| Micro-Course | Micro-Course Creator | 75% | 25% | Yes |
| Certification Program | Certification Creator, Faculty | 70% | 30% | Yes |
| Assessment / Test | Assessment Creator, Evaluator | 72% | 28% | Yes |
| Live Session (single) | Any Trainer/Mentor type | 85% | 15% | Yes |
| Recorded Session (archive) | Remote Instructor, Trainer | 78% | 22% | Yes |
| Workshop | Workshop Facilitator | 82% | 18% | Yes |
| Dojo Scenario Pack | Scenario Designer, Instructor | 75% | 25% | Yes |
| Project Template | Freelance Developer, Tool Builder | 80% | 20% | Yes |
| Tool / Plugin | Tool Builder, Plugin Developer | 85% | 15% | Yes |
| API Product | API Provider, SaaS Creator | 88% | 12% | Yes |
| Resume Template | Freelance Designer, HR Consultant | 80% | 20% | Yes |
| Research Paper / Ebook | Research Author, Technical Blogger | 90% | 10% | Yes |
| Prompt Pack | Prompt Engineer | 80% | 20% | Yes |
| AI Workflow | AI Workflow Designer | 82% | 18% | Yes |
| Idea Submission (monetized) | Any creator type | 70% | 30% | Yes |
| Bootcamp (full program) | Bootcamp Lead Instructor | 75% | 25% | Yes |
| Government Grant Allocation | NGO Trainer, Skill Mission Officer | 95% | 5% (admin fee) | TDS per govt rules |

**All rates above are v1 human declarations. Any rate change requires ROYALTY_POLICY_ADMIN sign-off + version bump.**

### Tax Withholding Rules (v1 — India Jurisdiction)

```
TDS (Section 194J — Professional/Technical Services):
  - Applicable when: creator annual earnings on platform > ₹30,000
  - Rate: 10% of royalty amount (if PAN provided)
  - Rate: 20% of royalty amount (if PAN NOT provided)
  - TDS accumulation tracked per creator per financial year (April–March)
  - TDS certificates: generated quarterly, stored in TAX_COMPLIANCE_SERVICE

GST / IGST:
  - GST-registered creators: GST collected on platform commission, not on royalty
  - Non-GST creators: no GST deducted from royalty
  - IGST applies on cross-state transactions (creator state ≠ platform state)
  - International creators: no Indian GST — local tax jurisdiction rules apply

International Royalties:
  - Withholding tax per DTAA (Double Taxation Avoidance Agreement) treaty rates
  - Non-DTAA jurisdictions: 20% withholding (per Income Tax Act Section 195)
  - FX conversion at mid-market rate from CURRENCY_CONVERSION_SERVICE at trigger timestamp
```

---

## 1️⃣8️⃣ DATABASE SCHEMA (MANDATORY — APPEND-ONLY)

```sql
-- Core Royalty Ledger (APPEND-ONLY, NO UPDATE, NO DELETE)
CREATE TABLE royalty_ledger (
  royalty_ledger_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  royalty_trigger_id              UUID UNIQUE NOT NULL,       -- idempotency key
  source_revenue_event_id         UUID NOT NULL,
  tenant_id                       UUID NOT NULL,
  creator_id                      UUID NOT NULL,
  content_id                      UUID NOT NULL,
  content_type                    TEXT NOT NULL,
  creator_type                    TEXT NOT NULL,
  gross_transaction_amount_paise  BIGINT NOT NULL CHECK (gross_transaction_amount_paise > 0),
  platform_commission_paise       BIGINT NOT NULL CHECK (platform_commission_paise >= 0),
  platform_commission_percent     NUMERIC(5,2) NOT NULL,
  gross_royalty_before_tax_paise  BIGINT NOT NULL,
  tax_withheld_paise              BIGINT NOT NULL DEFAULT 0,
  tax_type                        TEXT NOT NULL DEFAULT 'none',
  net_royalty_payable_paise       BIGINT NOT NULL,
  rounding_reserve_paise          BIGINT NOT NULL DEFAULT 0,
  disbursement_hold               BOOLEAN NOT NULL DEFAULT FALSE,
  dispute_hold                    BOOLEAN NOT NULL DEFAULT FALSE,
  dispute_reference_id            UUID,
  royalty_policy_id               UUID NOT NULL,
  royalty_policy_version          TEXT NOT NULL,
  currency_code                   CHAR(3) NOT NULL,
  exchange_rate_applied           NUMERIC(10,6) NOT NULL DEFAULT 1.000000,
  processing_status               TEXT NOT NULL DEFAULT 'calculated',
  confidence_score                NUMERIC(4,3) NOT NULL,
  model_version                   TEXT NOT NULL,
  audit_reference                 UUID NOT NULL,
  committed_at_utc                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- APPEND-ONLY enforced at DB level — no UPDATE, no DELETE grants to any role

-- Co-Creator Split Records (APPEND-ONLY, linked to royalty_ledger)
CREATE TABLE royalty_co_creator_splits (
  split_id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  royalty_ledger_id           UUID NOT NULL REFERENCES royalty_ledger(royalty_ledger_id),
  co_creator_id               UUID NOT NULL,
  split_percent               NUMERIC(5,2) NOT NULL,
  gross_split_paise           BIGINT NOT NULL,
  tax_withheld_paise          BIGINT NOT NULL DEFAULT 0,
  net_payout_paise            BIGINT NOT NULL,
  disbursement_schedule_id    UUID,
  committed_at_utc            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Disbursement Schedule Records
CREATE TABLE royalty_disbursement_schedule (
  disbursement_schedule_id    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  royalty_ledger_id           UUID NOT NULL,
  creator_id                  UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  net_payout_paise            BIGINT NOT NULL,
  currency_code               CHAR(3) NOT NULL,
  scheduled_disbursement_date DATE NOT NULL,
  disbursement_status         TEXT NOT NULL DEFAULT 'pending',
  payout_batch_id             UUID,
  created_at_utc              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Tax Deduction Records
CREATE TABLE royalty_tax_records (
  tax_record_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  royalty_ledger_id           UUID NOT NULL,
  creator_id                  UUID NOT NULL,
  tenant_id                   UUID NOT NULL,
  tax_type                    TEXT NOT NULL,
  taxable_amount_paise        BIGINT NOT NULL,
  tax_withheld_paise          BIGINT NOT NULL,
  jurisdiction_code           TEXT NOT NULL,
  financial_year              TEXT NOT NULL,
  pan_number_encrypted        TEXT,
  gst_number_encrypted        TEXT,
  tds_certificate_reference   TEXT,
  committed_at_utc            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Royalty Audit Log (SEPARATE, APPEND-ONLY)
CREATE TABLE royalty_audit_log (
  log_id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timestamp_utc               TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  actor_id                    UUID NOT NULL,
  actor_type                  TEXT NOT NULL,
  royalty_trigger_id          UUID NOT NULL,
  source_revenue_event_id     UUID NOT NULL,
  input_hash                  TEXT NOT NULL,
  output_hash                 TEXT NOT NULL,
  model_version               TEXT NOT NULL,
  policy_version              TEXT NOT NULL,
  decision_path               TEXT[] NOT NULL,
  confidence_score            NUMERIC(4,3) NOT NULL,
  anomaly_flags               TEXT[] NOT NULL DEFAULT '{}',
  tax_calculation_method      TEXT NOT NULL,
  processing_duration_ms      INTEGER NOT NULL,
  tenant_id                   UUID NOT NULL,
  creator_id                  UUID NOT NULL
);
```

---

## 1️⃣9️⃣ API CONTRACTS (SEALED)

```
POST   /royalty/calculate
       → Ingest a royalty trigger event (internal system only)
       → Auth: REVENUE_INGESTION_AGENT system JWT only
       → Response: 202 Accepted (async) | 409 Duplicate | 422 Validation Error | 500 Error

GET    /royalty/ledger/{royalty_ledger_id}
       → Retrieve committed royalty ledger entry
       → Auth: FINANCE_MANAGER, COMPLIANCE_ADMIN, AUDIT_ADMIN, PLATFORM_SUPER_ADMIN
       → Creator self-read: GET /royalty/creator/{creator_id}/earnings (own records only)
       → Response: 200 OK | 404 Not Found | 403 Forbidden

GET    /royalty/creator/{creator_id}/earnings/summary
       → Creator earnings summary with breakdown
       → Auth: Creator (own ID only), FINANCE_MANAGER, TENANT_ADMIN
       → Response: 200 OK | 403 Forbidden

GET    /royalty/creator/{creator_id}/tax-records/{financial_year}
       → Tax deduction records for a financial year
       → Auth: Creator (own ID only), TAX_COMPLIANCE_OFFICER, AUDIT_ADMIN
       → Response: 200 OK | 404 Not Found | 403 Forbidden

GET    /royalty/disbursement/schedule/{disbursement_schedule_id}
       → Disbursement schedule record
       → Auth: PAYOUT_ORCHESTRATOR_AGENT, FINANCE_MANAGER, AUDIT_ADMIN
       → Response: 200 OK | 404 Not Found

GET    /royalty/audit/{audit_reference}
       → Audit log entry
       → Auth: AUDIT_ADMIN, PLATFORM_SUPER_ADMIN
       → Response: 200 OK | 404 Not Found

POST   /royalty/dispute/hold
       → Place disbursement hold on a royalty record (from DISPUTE_RESOLUTION_AGENT)
       → Auth: DISPUTE_RESOLUTION_AGENT system JWT only
       → Response: 200 OK | 404 Not Found | 409 Already Held

POST   /royalty/dispute/release
       → Release dispute hold (from DISPUTE_RESOLUTION_AGENT, post-resolution)
       → Auth: DISPUTE_RESOLUTION_AGENT system JWT + human COMPLIANCE_ADMIN dual approval
       → Response: 200 OK | 404 Not Found | 422 Release Without Resolution Forbidden

GET    /royalty/health
       → Agent health, model status, queue depth
       → Auth: DEVOPS_ONCALL, OBSERVABILITY_AGENT
       → Response: 200 OK with model_version, drift_score, dlq_depth, tax_hold_queue_depth

POST   /royalty/admin/reprocess/{royalty_trigger_id}
       → Re-process DLQ event (human-declared only)
       → Auth: PLATFORM_SUPER_ADMIN + dual approval (FINANCE_MANAGER + COMPLIANCE_ADMIN)
       → Response: 202 Accepted
```

---

## 2️⃣0️⃣ TECHNOLOGY STACK (LOCKED TO ECOSKILLER STACK)

```
Runtime:              Python 3.11 + FastAPI
Event Broker:         Redis Streams (consumer groups, priority queues)
Database:             PostgreSQL 15 (append-only policy enforced at DB level)
Cache:                Redis 7 (idempotency registry, policy cache)
ML Framework:         scikit-learn (classification, anomaly regression)
                      XGBoost (primary policy classification)
                      statsmodels / Prophet (time-series earnings forecast)
                      PyTorch (LSTM for deep earnings pattern anomaly)
AI Component:         Internal LLM (governed prompts, 3s timeout, JSON output only)
Tax Engine:           Custom deterministic tax calculator (rules from TAX_COMPLIANCE_SERVICE)
                      No third-party tax SaaS — all rules owned internally
Observability:        Prometheus metrics export + Jaeger distributed tracing + Loki logs
Secrets:              HashiCorp Vault (per-tenant KMS keys, PAN/GST field encryption keys)
Object Storage:       MinIO (immutable audit log + tax record backup, object-lock enabled)
Deployment:           Kubernetes (ecoskiller-prod namespace, HPA governed)
Container:            Docker (Ecoskiller standard base image)
CI/CD:                GitLab CE (all four environments: DEV · TEST · STAGING · PRODUCTION)
Auth:                 Keycloak (JWT verification), Kong API Gateway
```

---

## ✅ FINAL LOCK DECLARATION

```
AGENT:                ROYALTY_CALCULATION_AGENT
PLATFORM:             ECOSKILLER ANTIGRAVITY
VERSION:              RCA-v1.0.0
STATUS:               SEALED · LOCKED · GOVERNED · DETERMINISTIC
AUTHORITY:            Human declaration only
MODIFICATION:         Add-only via version bump with human declaration
INTERPRETATION:       NONE PERMITTED
SILENT FAILURE:       STRICTLY PROHIBITED
CROSS-TENANT:         STRICTLY PROHIBITED
LEDGER MUTATION:      STRICTLY PROHIBITED
AUDIT DELETION:       STRICTLY PROHIBITED
DISBURSEMENT:         STRICTLY PROHIBITED — owned by PAYOUT_ORCHESTRATOR_AGENT
DISPUTE RESOLUTION:   STRICTLY PROHIBITED — owned by DISPUTE_RESOLUTION_AGENT
DEFAULT SPLITS:       STRICTLY PROHIBITED — all splits must be explicitly declared
CASCADE DEPTH:        MAX 2 LEVELS — primary + one secondary co-creator

Absence of any declared section implementation → STOP EXECUTION
Violation of any NON-NEGOTIABLE RULE → HALT → ESCALATE → INCIDENT LOG
Tax rule changes without TAX_COMPLIANCE_OFFICER sign-off → STOP EXECUTION
Policy changes without ROYALTY_POLICY_ADMIN sign-off → STOP EXECUTION
```

---

*ROYALTY_CALCULATION_AGENT · Ecoskiller Antigravity · RCA-v1.0.0 · SEALED*
