# 🔒 REVENUE_SHARE_RECONCILIATION_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED AGENT SPEC v1.0

```
ARTIFACT CLASS        : Enterprise Financial Agent Blueprint
MUTATION POLICY       : ADD-ONLY via version bump
EXECUTION MODE        : DETERMINISTIC + VALIDATED
INTERPRETATION AUTH   : NONE
CREATIVE FILL         : FORBIDDEN
ASSUMPTION FILL       : FORBIDDEN
DEFAULT BEHAVIOR      : DENY
FAILURE MODE          : HALT ON AMBIGUITY → LOG → ESCALATE → FREEZE PAYOUT
STACK ALIGNMENT       : ECOSKILLER ANTIGRAVITY v12.0
DOMAIN CLASSIFICATION : FINANCIAL — HIGHEST SENSITIVITY
SEAL STATUS           : LOCKED
COMPLIANCE TIER       : CRITICAL — FINANCIAL AUDIT REQUIRED
```

---

## ⚠️ FINANCIAL DOMAIN DECLARATION

This agent operates in the **Financial Domain**. All outputs directly affect real monetary flows: trainer earnings, platform commissions, referral rewards, creator payouts, co-teaching splits, certification fees, tournament entry distributions, and GST/VAT obligations.

```
FINANCIAL_DOMAIN      = TRUE
PAYOUT_AUTHORITY      = HUMAN-APPROVED ONLY (no autonomous disbursement)
TAX_LOGIC             = ACTIVE
FRAUD_DETECTION       = MANDATORY
DISPUTE_ENGINE        = MANDATORY
RECONCILIATION_MODE   = DOUBLE-ENTRY LEDGER
CURRENCY_HANDLING     = MULTI-CURRENCY with base currency lock
ZERO_TOLERANCE_POLICY = Ledger imbalance → IMMEDIATE HALT + FREEZE ALL PENDING PAYOUTS
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME          : REVENUE_SHARE_RECONCILIATION_AGENT
AGENT_ID            : ECSK-AGENT-REVSHR-001
SYSTEM_ROLE         : Multi-party Revenue Split Calculator, Ledger Reconciler,
                      and Payout Readiness Validator for the Ecoskiller Platform
PRIMARY_DOMAIN      : FINANCIAL (Billing + Trainer Monetization + Platform Economy)
EXECUTION_MODE      : DETERMINISTIC + VALIDATED
DATA_SCOPE          : TrainerWallet, CoursePricing, SubscriptionPlan, PayoutRequest,
                      RevenueShareRule, ReferralRewardLedger, CreatorRewardLedger,
                      PlatformCommissionLedger, TaxLedger, PaymentTransactions,
                      CoTeachingAgreement, TournamentRevenuePool, CertificationFeePool
TENANT_SCOPE        : STRICT ISOLATION — trainer earnings of Tenant A
                      never visible or accessible to Tenant B
FAILURE_POLICY      : HALT ON AMBIGUITY → LOG_FINANCIAL_INCIDENT →
                      FREEZE AFFECTED WALLET → ESCALATE_TO FINANCE_ADMIN
VERSION             : 1.0.0
LAST_SEALED         : 2025
```

**This agent NEVER executes a payout. It ONLY calculates, validates, reconciles, and emits payout-readiness signals. Actual disbursement is gated by human-approved PAYOUT_APPROVAL_AGENT.**

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves

Ecoskiller's revenue flows are complex and multi-party. A single student subscription payment can simultaneously generate: a platform commission, a trainer's course revenue share, a co-teacher's split, a referral reward for the referring user, a creator amplification reward, a GST/VAT obligation, and an XP bonus for gamification. Without a deterministic reconciliation agent, these splits will be calculated inconsistently, ledgers will drift out of balance, payouts will be wrong, and compliance will fail.

This agent is the **single source of truth** for all revenue share calculations across the entire Ecoskiller multi-tenant economy.

### Revenue Streams This Agent Reconciles

```
1. Trainer course subscription revenue share
2. Trainer direct course purchase revenue share
3. Co-teaching revenue splits (multi-trainer agreements)
4. Platform commission (Ecoskiller's retained percentage)
5. Referral reward distributions (ReferralRewardLedger)
6. Creator content amplification rewards (CreatorRewardLedger)
7. Dojo tournament entry fee distribution
8. Dojo certification fee distribution
9. Dojo mentor license fee distribution
10. Seat-based institute subscription splits
11. GST / VAT tax extraction and ledger segregation
12. Coupon / discount impact reconciliation
13. Refund reversal reconciliation
14. Usage-metered billing settlements (matches, hours, recordings)
```

### What Input It Consumes

Every billable event emitted by the platform's payment and activity layer.

### What Output It Produces

- Validated revenue split records per transaction
- Updated wallet balances per stakeholder (trainer, co-trainer, platform, referrer, creator)
- Tax ledger entries (GST/VAT segregated)
- Payout eligibility signals per wallet
- Reconciliation health report (balanced / imbalanced flag)
- Dispute flags for anomalous splits

### Downstream Agents Depending On This Agent

- `PAYOUT_APPROVAL_AGENT` — consumes payout_ready signals for human-gated disbursement
- `INVOICE_GENERATION_AGENT` — consumes split records to generate trainer invoices
- `TAX_COMPLIANCE_AGENT` — consumes tax_ledger_entries for GST/VAT filing
- `INSTITUTE_ERP_DASHBOARD_AGENT` — consumes revenue metrics for admin dashboards
- `GAMIFICATION_ENGINE` — consumes XP credit signals triggered by revenue events
- `FRAUD_DETECTION_AGENT` — receives anomaly flags from this agent
- `OBSERVABILITY_AGENT` — receives performance and drift metrics
- `FEATURE_STORE_AGENT` — receives behavioral feature vectors

### Upstream Agents Feeding This Agent

- `PAYMENT_GATEWAY_AGENT` — provides confirmed payment transaction records
- `SUBSCRIPTION_MANAGEMENT_AGENT` — provides plan activation / renewal events
- `BILLING_LEDGER_AGENT` — provides the authoritative transaction log
- `REVENUE_SHARE_RULE_ENGINE` — provides current versioned split rules per product type
- `REFUND_PROCESSING_AGENT` — provides refund confirmation events for reversal
- `COUPON_ENGINE_AGENT` — provides discount impact amounts per transaction
- `DOJO_MATCH_ENGINE` — provides tournament + certification fee events
- `TAX_RULE_ENGINE` — provides applicable GST/VAT rates per region/entity

---

## 3️⃣ INPUT CONTRACT (STRICT)

### Primary Input: Billable Event

```json
INPUT_SCHEMA: {
  "required_fields": [
    "event_id",
    "event_type",
    "transaction_id",
    "tenant_id",
    "platform_id",
    "payment_status",
    "gross_amount",
    "currency_code",
    "base_currency_code",
    "fx_rate",
    "net_amount_after_gateway_fee",
    "gateway_fee_amount",
    "product_type",
    "product_id",
    "payer_user_id",
    "primary_beneficiary_id",
    "revenue_share_rule_version",
    "tax_rule_version",
    "tax_jurisdiction",
    "timestamp_utc",
    "request_id",
    "idempotency_key"
  ],
  "optional_fields": [
    "co_beneficiaries": [
      {
        "beneficiary_id",
        "split_type",
        "split_value",
        "agreement_id"
      }
    ],
    "referrer_user_id",
    "coupon_id",
    "coupon_discount_amount",
    "metered_usage_units",
    "metered_unit_type"
  ],
  "product_type_enum": [
    "COURSE_SUBSCRIPTION",
    "COURSE_DIRECT_PURCHASE",
    "DOJO_TOURNAMENT_ENTRY",
    "DOJO_CERTIFICATION_FEE",
    "DOJO_MENTOR_LICENSE",
    "INSTITUTE_SEAT_SUBSCRIPTION",
    "PLATFORM_SUBSCRIPTION",
    "USAGE_METERED_BILLING"
  ],
  "validation_rules": [
    "event_id must be globally unique",
    "idempotency_key must be unique — duplicate key returns cached result, no reprocess",
    "transaction_id must exist in BILLING_LEDGER_AGENT as CONFIRMED",
    "payment_status must be SUCCESS before processing — PENDING/FAILED events rejected",
    "gross_amount must be > 0",
    "net_amount_after_gateway_fee must be <= gross_amount",
    "revenue_share_rule_version must resolve to an ACTIVE locked rule set",
    "tax_rule_version must resolve to an ACTIVE locked tax rule set",
    "primary_beneficiary_id must resolve to an active user/trainer with a wallet",
    "co_beneficiaries split percentages + primary_beneficiary split + platform_commission must sum to exactly 100.00%",
    "currency_code must be ISO 4217 valid",
    "fx_rate must be sourced from APPROVED_FX_RATE_PROVIDER only",
    "tenant_id must match product_id ownership (no cross-tenant revenue claims)"
  ],
  "security_checks": [
    "tenant_id isolation: beneficiary_id must belong to same tenant as product_id",
    "actor calling this agent must hold ROLE: BILLING_SERVICE or SYSTEM_AGENT only",
    "no human-direct API calls to this agent in production",
    "all inbound events must carry valid HMAC signature from PAYMENT_GATEWAY_AGENT",
    "request must not originate from external network (internal service mesh only)"
  ],
  "domain_checks": [
    "DOJO product events must carry match_id or tournament_id or certification_id",
    "INSTITUTE events must carry institute_id + seat_count",
    "USAGE_METERED events must carry metered_usage_units + metered_unit_type"
  ]
}
```

### Validation Failure Behavior

```
IF payment_status ≠ SUCCESS                     → REJECT → LOG → DO NOT PROCESS
IF idempotency_key already processed            → RETURN CACHED RESULT → LOG DUPLICATE
IF split percentages ≠ 100.00%                 → REJECT → LOG SPLIT_IMBALANCE_ERROR → ESCALATE
IF revenue_share_rule_version not found         → HALT → LOG RULE_VERSION_MISSING → ESCALATE
IF beneficiary wallet not found                 → HALT → LOG WALLET_MISSING → ESCALATE
IF HMAC signature invalid                       → HALT → LOG SECURITY_VIOLATION → FREEZE TRANSACTION
IF cross-tenant beneficiary detected            → HALT → SECURITY_INCIDENT_LOG → FREEZE PERMANENTLY
IF fx_rate provider not approved                → HALT → LOG FX_INTEGRITY_FAILURE → ESCALATE
```

**No null tolerance. No partial splits. No silent processing.**

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "reconciliation_id": "UUID",
    "event_id": "string (input reference)",
    "transaction_id": "string",
    "tenant_id": "string",
    "processing_status": "RECONCILED | HELD | REJECTED | DISPUTED",
    "gross_amount": "decimal",
    "currency_code": "string",
    "base_currency_amount": "decimal",
    "fx_rate_applied": "decimal",
    "gateway_fee_deducted": "decimal",
    "tax_extracted": {
      "total_tax_amount": "decimal",
      "tax_type": "GST | VAT | NONE",
      "tax_rate_applied": "decimal",
      "tax_jurisdiction": "string",
      "tax_ledger_entry_id": "UUID"
    },
    "distributable_amount": "decimal",
    "split_ledger": [
      {
        "ledger_entry_id": "UUID",
        "beneficiary_type": "TRAINER | CO_TRAINER | PLATFORM | REFERRER | CREATOR | TOURNAMENT_POOL | CERTIFICATION_POOL",
        "beneficiary_id": "string",
        "wallet_id": "string",
        "split_rule_applied": "string (rule_id + version)",
        "split_percentage": "decimal",
        "split_amount_base_currency": "decimal",
        "wallet_credit_status": "CREDITED | HELD | PENDING",
        "payout_eligible": "boolean",
        "payout_hold_reason": "string | null"
      }
    ],
    "platform_commission_entry": {
      "ledger_entry_id": "UUID",
      "commission_rule_version": "string",
      "commission_percentage": "decimal",
      "commission_amount": "decimal",
      "platform_wallet_credited": "boolean"
    },
    "referral_reward_entry": {
      "referral_reward_ledger_id": "UUID | null",
      "referrer_user_id": "string | null",
      "reward_amount": "decimal",
      "reward_rule_version": "string | null"
    },
    "reconciliation_check": {
      "gross_equals_sum_of_parts": "boolean",
      "ledger_balanced": "boolean",
      "imbalance_amount": "decimal",
      "imbalance_flag": "boolean"
    },
    "dispute_flags": [],
    "payout_readiness_signals": [
      {
        "wallet_id": "string",
        "beneficiary_id": "string",
        "eligible_for_payout": "boolean",
        "payout_hold_active": "boolean",
        "hold_reason": "string | null"
      }
    ]
  },
  "confidence_score": "0.0 to 1.0",
  "model_version": "RSRA-CALC-v1.0.0",
  "audit_reference": "UUID",
  "next_trigger_event": [
    "REVENUE_RECONCILED_EVENT",
    "PAYOUT_APPROVAL_AGENT:PAYOUT_READY_SIGNAL",
    "INVOICE_GENERATION_AGENT:INVOICE_TRIGGER",
    "TAX_COMPLIANCE_AGENT:TAX_ENTRY_READY",
    "FRAUD_DETECTION_AGENT:ANOMALY_CHECK",
    "FEATURE_STORE_AGENT:FEATURE_VECTOR_EMIT"
  ]
}
```

### Output Rules

```
ALL split_ledger entries must sum to distributable_amount EXACTLY
IF reconciliation_check.ledger_balanced = false → HOLD ALL PAYOUTS → ESCALATE
IF confidence_score < 0.90 → PROCESSING_STATUS = HELD → require FINANCE_ADMIN review
IF dispute_flags is non-empty → PROCESSING_STATUS = DISPUTED → block payout
PLATFORM_COMMISSION entry is mandatory on every transaction — never zero unless explicitly exempted by rule
TAX entry mandatory for all taxable jurisdictions — never skipped
```

---

## 5️⃣ REVENUE SHARE RULE ENGINE (LOCKED CONTRACTS)

All revenue share rules are versioned, locked, and sourced from `REVENUE_SHARE_RULE_ENGINE`. This agent consumes rules — it NEVER defines them.

### Supported Rule Types

```yaml
RULE_TYPES:

  TRAINER_DIRECT_COURSE_PURCHASE:
    description    : One-time purchase of trainer course
    split_parties  : [TRAINER, PLATFORM]
    default_split  : TRAINER=70%, PLATFORM=30%
    tax_treatment  : Extract GST/VAT before split
    rule_reference : RSRULE-001-v1

  TRAINER_SUBSCRIPTION_REVENUE:
    description    : Monthly/annual plan — course included
    split_parties  : [TRAINER, PLATFORM]
    default_split  : TRAINER=65%, PLATFORM=35%
    tax_treatment  : Extract GST/VAT before split
    rule_reference : RSRULE-002-v1

  CO_TEACHING_AGREEMENT:
    description    : Two+ trainers on one course
    split_parties  : [PRIMARY_TRAINER, CO_TRAINER(s), PLATFORM]
    split_source   : CoTeachingAgreement.split_map (must sum to 100% less platform cut)
    tax_treatment  : Extract before split
    rule_reference : RSRULE-003-v1

  REFERRAL_REWARD:
    description    : Referrer earns reward when referred user purchases
    split_parties  : [REFERRER, PRIMARY_SPLIT_UNCHANGED]
    reward_pool    : Funded from PLATFORM share only (never from trainer share)
    anti_fraud     : Fraud check required before credit
    rule_reference : RSRULE-004-v1

  CREATOR_AMPLIFICATION_REWARD:
    description    : Creator earns reward for high-engagement content
    split_parties  : [CREATOR, PLATFORM_POOL]
    trigger        : engagement threshold event (not transaction-based)
    rule_reference : RSRULE-005-v1

  DOJO_TOURNAMENT_DISTRIBUTION:
    description    : Tournament entry fees → prize pool + platform cut
    split_parties  : [TOURNAMENT_WINNER_POOL, PLATFORM, DOJO_OPERATIONS]
    prize_pool_pct : Declared at tournament creation (immutable post-launch)
    rule_reference : RSRULE-006-v1

  DOJO_CERTIFICATION_FEE:
    description    : Certification exam fee distribution
    split_parties  : [MENTOR_POOL, PLATFORM, CONTENT_GOVERNANCE_FUND]
    rule_reference : RSRULE-007-v1

  DOJO_MENTOR_LICENSE:
    description    : Mentor license subscription
    split_parties  : [PLATFORM, MENTOR_TRAINING_FUND]
    rule_reference : RSRULE-008-v1

  INSTITUTE_SEAT_SUBSCRIPTION:
    description    : Seat-based subscription for institute students
    split_parties  : [PLATFORM, INSTITUTE_REVENUE_SHARE (if reseller)]
    rule_reference : RSRULE-009-v1

  USAGE_METERED_BILLING:
    description    : Per-match, per-hour, per-recording billing
    split_parties  : [PLATFORM, TRAINER (if applicable)]
    meter_unit     : from metered_unit_type in input
    rule_reference : RSRULE-010-v1

RULE_OVERRIDE_POLICY:
  - Rules may NOT be overridden by this agent
  - Rule change requires REVENUE_SHARE_RULE_ENGINE version bump
  - All active transactions use rule version stamped at payment time
  - Retroactive rule application = FORBIDDEN
```

---

## 6️⃣ TAX LOGIC LAYER (MANDATORY)

```yaml
TAX_ENGINE_INTEGRATION:
  source          : TAX_RULE_ENGINE (versioned)
  tax_types       : [GST, VAT, NONE]
  treatment       : Tax extracted BEFORE split calculation
  ledger          : Separate TAX_LEDGER (never mixed with revenue ledger)

TAX_CALCULATION_RULES:
  - Applicable rate resolved by (tax_jurisdiction + product_type + entity_type)
  - Tax amount = gross_amount × applicable_rate
  - distributable_amount = gross_amount - gateway_fee - tax_amount
  - Tax never distributed to trainer or referrer — goes to TAX_LEDGER only
  - Tax_ledger_entry_id required on every taxable transaction

GST_VAT_HANDLING:
  - India: GST (18% default for digital services)
  - EU: VAT (destination-based, rate varies by country)
  - Other: Per tax_rule_version lookup
  - Tax exempt transactions: explicit tax_rule exemption code required

TAX_COMPLIANCE_AGENT_HANDOFF:
  - Every tax_ledger_entry emitted to TAX_COMPLIANCE_AGENT immediately
  - Monthly aggregation for filing is TAX_COMPLIANCE_AGENT responsibility
  - This agent does NOT file taxes — it only ledgers them
```

---

## 7️⃣ DOUBLE-ENTRY LEDGER MODEL

Every revenue event generates **balanced ledger entries**. No single-entry writes permitted.

```
LEDGER STRUCTURE (Double-Entry):

For every DEBIT there must be a corresponding CREDIT of equal amount.

Example — Trainer Course Purchase (₹1000 gross):
  DR  Payment_Received_Account      ₹1000.00
  CR  Gateway_Fee_Payable            ₹30.00
  CR  GST_Payable                   ₹153.00   (18% of ₹850 net)
  CR  Trainer_Revenue_Share_Payable ₹573.50   (70% of distributable ₹819)
  CR  Platform_Commission_Payable   ₹245.50   (30% of distributable ₹819 less referral)
  CR  Referral_Reward_Payable        ₹0.00    (if no referral)
  ─────────────────────────────────────────
  BALANCE CHECK: DR total = CR total = ₹1000.00

BALANCE_CHECK_RULE:
  IF sum(all_credits) ≠ DR_amount → LEDGER_IMBALANCE_ERROR
  → HALT IMMEDIATELY
  → FREEZE all pending payouts across this tenant
  → LOG with full entry detail
  → ESCALATE_TO: FINANCE_ADMIN + PLATFORM_ADMIN

LEDGER_PROPERTIES:
  - Append-only (no updates, no deletes)
  - Every entry has: entry_id (UUID), transaction_id, entry_type, amount, timestamp, audit_reference
  - Corrections via REVERSAL entries only (never by editing original)
```

---

## 8️⃣ REFUND RECONCILIATION PROTOCOL

```yaml
REFUND_EVENT_TRIGGER:
  source          : REFUND_PROCESSING_AGENT
  input_required  : original_transaction_id, refund_amount, refund_reason, refund_policy_version

REFUND_RECONCILIATION_RULES:
  - Refund reverses ONLY the distributable splits — not gateway fees (non-recoverable)
  - GST/VAT reversal handled per tax_rule_version (jurisdiction-specific)
  - Trainer wallet DEBITED (reversed credit) by trainer's original share
  - Platform commission DEBITED (reversed) by platform's original share
  - Referral reward: NOT reversed if referral reward was already disbursed
    (absorb from platform commission)
  - If trainer wallet balance insufficient for reversal → HOLD status → ESCALATE

REVERSAL_LEDGER_ENTRIES:
  - All reversal entries reference original_reconciliation_id
  - Reversal entries are DEBIT entries against previously credited wallets
  - Net wallet balance after reversal must be >= 0 (can never go negative without explicit overdraft policy)

PARTIAL_REFUND_HANDLING:
  - Partial refund = pro-rata reversal across all parties
  - Pro-rata calculation must be deterministic and documented in reversal entry
  - Rounding handled by accumulating remainder to PLATFORM account

REFUND_ABUSE_FLAG:
  - User with refund_count > threshold within rolling_window → FLAG to FRAUD_DETECTION_AGENT
  - Flagged users' refund requests require FINANCE_ADMIN approval before processing
```

---

## 9️⃣ CO-TEACHING REVENUE SPLIT PROTOCOL

```yaml
CO_TEACHING_AGREEMENT_VALIDATION:
  - CoTeachingAgreement must be in ACTIVE + SIGNED status
  - All co-trainer_ids must have active wallets
  - Co-trainer split percentages in agreement must sum to (100% - platform_commission)
  - Agreement version locked at course creation — cannot change mid-subscription

CO_TEACHING_SPLIT_CALCULATION:
  distributable_amount = gross - gateway_fee - tax
  platform_share       = distributable_amount × platform_commission_rate
  trainer_pool         = distributable_amount - platform_share
  
  FOR EACH co_trainer in agreement:
    co_trainer_share = trainer_pool × co_trainer.split_percentage
    credit co_trainer.wallet_id

  VALIDATION:
    sum(all_co_trainer_shares) must equal trainer_pool exactly
    Rounding remainder credited to PRIMARY_TRAINER

DISPUTE_ON_CO_TEACHING:
  - If any co-trainer disputes their split → FLAG as DISPUTED
  - HOLD all payouts on that reconciliation_id
  - Escalate to GOVERNANCE_BOARD
  - Original signed agreement is the binding reference
```

---

## 🔟 ML / AI LOGIC LAYER

### ML Model (Primary — 80% weight)

```yaml
MODEL_TYPE          : Anomaly Detection + Classification
ALGORITHM           : Isolation Forest (anomaly) +
                      Gradient Boosted Trees (fraud classification)
MODEL_VERSION       : RSRA-ML-v1.0.0

FEATURES_USED:
  - transaction_amount_zscore (float, per trainer baseline)
  - split_deviation_from_rule (float, delta from expected split)
  - payer_transaction_frequency (integer, rolling 30d)
  - refund_rate_per_payer (float, rolling 90d)
  - co_teaching_agreement_age_days (integer)
  - fx_rate_deviation_from_market (float, % delta)
  - coupon_usage_frequency_per_user (integer, rolling 30d)
  - beneficiary_wallet_age_days (integer)
  - tournament_entry_volume_spike (float, zscore)
  - multi_account_signal_score (float, from FRAUD_DETECTION_AGENT)

DETECTION_TARGETS:
  - Split calculation anomalies
  - Refund abuse patterns
  - Fake tournament participation loops
  - Co-teaching collusion billing
  - Multi-account reward farming
  - Unusual fx_rate manipulation attempts

TRAINING_FREQUENCY  : Monthly
DRIFT_DETECTION     : Monitor fraud_flag_rate increase > 5% week-over-week
                      Monitor split_deviation_from_rule mean drift
VERSION_CONTROL     : Immutable model artifact per version
                      All reconciliation outputs stamped with model_version
```

### AI Layer (Secondary — 20% weight)

```yaml
AI_USAGE_SCOPE:
  - Natural language dispute explanation generation
  - Finance admin summary narration of reconciliation report
  - Anomaly explanation for flagged transactions

AI_STRICTLY_FORBIDDEN:
  - AI must NEVER calculate revenue splits
  - AI must NEVER approve payouts
  - AI must NEVER modify ledger entries
  - AI must NEVER interpret tax rules
  - AI must NEVER override ML anomaly flags

PROMPT_GOVERNANCE:
  - All AI prompts versioned in PROMPT_REGISTRY
  - Deterministic structured prompts only
  - AI output is supplementary explanation only — never decision input
```

---

## 1️⃣1️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS          : 2,000 (peak: subscription renewal batch, tournament end)
LATENCY_TARGET        : P95 < 500ms per transaction reconciliation
MAX_CONCURRENCY       : 1,000 parallel reconciliation workers
QUEUE_STRATEGY        : Priority queue
                        HIGH: refund events (time-sensitive reversal)
                        MEDIUM: direct purchase events
                        LOW: metered billing batch
                        Dead letter queue for all failed events (no silent drop)
EXECUTION_MODEL       : Stateless workers, horizontal scaling via Kubernetes HPA
EVENT_TRIGGER         : Kafka topics:
                          billing.payment.confirmed
                          billing.refund.confirmed
                          dojo.tournament.concluded
                          dojo.certification.fee.collected
ASYNC_PROCESSING      : TRUE — reconciliation async, result via event
IDEMPOTENCY           : idempotency_key as exact deduplication key
                        Duplicate = return cached reconciliation_id, no reprocess
PARTITIONING          : Kafka partitioned by tenant_id for strict isolation
BATCH_SUPPORT         : Monthly payout batch reconciliation supported
                        Batch_id groups all daily reconciliation_ids for monthly summary
```

---

## 1️⃣2️⃣ SECURITY ENFORCEMENT

```
TENANT ISOLATION:
  - beneficiary_id + product_id must share same tenant_id
  - Cross-tenant wallet access = IMMEDIATE HALT + SECURITY_INCIDENT_LOG
  - Row-level security at DB layer enforced on all ledger tables

ROLE-BASED AUTHORIZATION:
  BILLING_SERVICE (system)    : submit payment events for reconciliation
  SYSTEM_AGENT (internal)     : read reconciliation status
  FINANCE_ADMIN               : review held/disputed reconciliations, approve overrides
  PLATFORM_ADMIN              : audit log access only
  TRAINER                     : view own wallet + own reconciliation history (read-only)
  CO_TRAINER                  : view own split records (read-only)
  INSTITUTE_ADMIN             : view institute-scoped revenue records
  REFERRER                    : view own referral reward ledger (read-only)
  EXTERNAL / HUMAN DIRECT     : FORBIDDEN — no direct API access to this agent

PAYMENT EVENT SECURITY:
  - All inbound events MUST carry HMAC-SHA256 signature from PAYMENT_GATEWAY_AGENT
  - Signature verification failure = REJECT + SECURITY_FLAG
  - Internal service mesh only — no public-facing endpoints

ENCRYPTION:
  - All ledger data encrypted at rest (AES-256)
  - Wallet balance fields encrypted column-level
  - All inter-service communication via mutual TLS (mTLS)
  - No plaintext financial data in logs

AUDIT LOGGING:
  - Every reconciliation execution logged (append-only)
  - Every wallet credit/debit logged with reconciliation_id reference
  - Every anomaly flag logged with model_version + feature_values
  - Every override by FINANCE_ADMIN logged with justification + actor_id
  - Logs immutable, 7-year retention minimum
```

---

## 1️⃣3️⃣ AUDIT & TRACEABILITY

Every execution MUST produce the following immutable audit record:

```json
{
  "timestamp_utc": "ISO8601",
  "actor_id": "BILLING_SERVICE or SYSTEM_AGENT",
  "tenant_id": "string",
  "event_id": "string",
  "transaction_id": "string",
  "reconciliation_id": "UUID",
  "input_hash": "SHA256",
  "output_hash": "SHA256",
  "model_version": "RSRA-ML-v1.0.0",
  "revenue_share_rule_version": "string",
  "tax_rule_version": "string",
  "decision_path": [
    "INPUT_VALIDATED",
    "IDEMPOTENCY_CHECKED",
    "RULE_RESOLVED",
    "TAX_CALCULATED",
    "SPLITS_CALCULATED",
    "LEDGER_BALANCE_VERIFIED",
    "ML_ANOMALY_SCAN",
    "WALLET_CREDITS_STAGED",
    "PAYOUT_SIGNALS_EMITTED"
  ],
  "confidence_score": "float",
  "ledger_balanced": "boolean",
  "anomaly_flags": [],
  "split_count": "integer",
  "total_distributed": "decimal",
  "tax_collected": "decimal",
  "audit_reference": "UUID",
  "kafka_offset": "integer"
}
```

**Audit logs are IMMUTABLE. Append-only store. 7-year retention. Separate from application DB.**

---

## 1️⃣4️⃣ FAILURE POLICY

```yaml
INVALID_INPUT:
  ACTION      : STOP_EXECUTION
  LOG         : VALIDATION_FAILURE with field-level reason + input_hash
  ESCALATE_TO : BILLING_SERVICE owner + FINANCE_ADMIN
  RETRY_POLICY: NO automatic retry — requires corrected input

PAYMENT_NOT_CONFIRMED:
  ACTION      : REJECT IMMEDIATELY
  LOG         : UNCONFIRMED_PAYMENT_ATTEMPT
  ESCALATE_TO : BILLING_LEDGER_AGENT for status sync
  NOTE        : Only SUCCESS payments processed — no optimistic processing

SPLIT_IMBALANCE_DETECTED:
  ACTION      : STOP_EXECUTION + FREEZE ALL PENDING PAYOUTS for this tenant
  LOG         : SPLIT_IMBALANCE_CRITICAL + full calculation detail
  ESCALATE_TO : FINANCE_ADMIN + PLATFORM_ADMIN (immediate)
  RETRY_POLICY: NO retry — requires manual investigation and rule fix

LEDGER_BALANCE_FAILURE:
  ACTION      : STOP_EXECUTION + FREEZE + INCIDENT_REPORT
  LOG         : LEDGER_IMBALANCE_CRITICAL + entry IDs + amounts
  ESCALATE_TO : FINANCE_ADMIN + CTO-level escalation
  RETRY_POLICY: NO retry

WALLET_NOT_FOUND:
  ACTION      : HALT this transaction + HOLD payout
  LOG         : WALLET_MISSING + beneficiary_id + transaction_id
  ESCALATE_TO : FINANCE_ADMIN + TRAINER_SUPPORT_TEAM
  RETRY_POLICY: System retry 3× (30s/60s/120s) then HOLD permanently

ML_MODEL_UNAVAILABLE:
  ACTION      : CONTINUE reconciliation (ML is anomaly layer, not calculation layer)
  LOG         : ML_LAYER_UNAVAILABLE_WARNING
  FLAG        : Set ml_check_bypassed = true on output
  ESCALATE_TO : OBSERVABILITY_AGENT + PLATFORM_DEVOPS
  NOTE        : Reconciliation math does not depend on ML. ML adds fraud layer only.

AI_TIMEOUT:
  ACTION      : CONTINUE with no explanation narrative
  LOG         : AI_LAYER_TIMEOUT_WARNING
  NOTE        : AI provides explanations only — not blocking

RULE_VERSION_NOT_FOUND:
  ACTION      : HALT IMMEDIATELY
  LOG         : RULE_VERSION_MISSING_CRITICAL + rule_id + version
  ESCALATE_TO : FINANCE_ADMIN + REVENUE_SHARE_RULE_ENGINE team
  RETRY_POLICY: NO retry — must resolve rule reference first

FX_RATE_INTEGRITY_FAILURE:
  ACTION      : HALT + HOLD transaction
  LOG         : FX_INTEGRITY_FAILURE + provider_response
  ESCALATE_TO : FINANCE_ADMIN
  RETRY_POLICY: Retry with approved FX provider fallback (if configured)

CONFIDENCE_BELOW_0.90:
  ACTION      : PROCESSING_STATUS = HELD (not RECONCILED)
  LOG         : LOW_CONFIDENCE_RECONCILIATION + contributing_factors
  ESCALATE_TO : FINANCE_ADMIN for manual review
  BLOCK       : Payout signal NOT emitted until FINANCE_ADMIN releases
```

---

## 1️⃣5️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - PAYMENT_GATEWAY_AGENT
      provides : payment.confirmed events with HMAC signature
  - BILLING_LEDGER_AGENT
      provides : authoritative transaction log for cross-validation
  - REVENUE_SHARE_RULE_ENGINE
      provides : versioned split rules per product_type
  - TAX_RULE_ENGINE
      provides : applicable GST/VAT rates by jurisdiction
  - REFUND_PROCESSING_AGENT
      provides : refund.confirmed events for reversal
  - COUPON_ENGINE_AGENT
      provides : discount impact per transaction
  - DOJO_MATCH_ENGINE
      provides : tournament + certification fee collection events
  - SUBSCRIPTION_MANAGEMENT_AGENT
      provides : plan activation/renewal events

DOWNSTREAM_AGENTS:
  - PAYOUT_APPROVAL_AGENT
      consumes : payout_readiness_signals (human-gated disbursement)
  - INVOICE_GENERATION_AGENT
      consumes : split records for trainer invoice PDF generation
  - TAX_COMPLIANCE_AGENT
      consumes : tax_ledger_entries for GST/VAT filing
  - INSTITUTE_ERP_DASHBOARD_AGENT
      consumes : revenue metrics aggregated by institute
  - GAMIFICATION_ENGINE
      consumes : XP signals on revenue milestones
  - FRAUD_DETECTION_AGENT
      consumes : anomaly flags + feature vectors
  - OBSERVABILITY_AGENT
      consumes : performance metrics + error events + drift signals
  - FEATURE_STORE_AGENT
      consumes : behavioral feature vectors

EVENT_TRIGGERS:
  INBOUND (subscribed Kafka topics):
    - billing.payment.confirmed
    - billing.refund.confirmed
    - dojo.tournament.concluded
    - dojo.certification.fee.collected
    - dojo.mentor.license.activated
    - subscription.plan.renewed
    - subscription.plan.activated
    - usage.billing.cycle.completed

  OUTBOUND (emitted Kafka topics):
    - revenue.reconciled
    - revenue.reconciliation.held
    - revenue.reconciliation.disputed
    - payout.signal.ready
    - ledger.imbalance.critical     (→ FINANCE_ADMIN alert)
    - fraud.anomaly.flagged         (→ FRAUD_DETECTION_AGENT)
    - tax.entry.ready               (→ TAX_COMPLIANCE_AGENT)
    - feature.vector.emitted        (→ FEATURE_STORE_AGENT)
    - wallet.credit.staged
    - refund.reversal.completed
```

---

## 1️⃣6️⃣ WALLET STATE MACHINE

```
WALLET CREDIT LIFECYCLE:

  STAGED → HELD → PAYOUT_ELIGIBLE → DISBURSED

  STAGED        : Reconciliation calculated, credit staged but not confirmed
  HELD          : Anomaly flag / low confidence / dispute / imbalance
  PAYOUT_ELIGIBLE: Reconciliation confirmed, no flags, confidence >= 0.90
  DISBURSED     : PAYOUT_APPROVAL_AGENT confirmed disbursement (out of scope here)

WALLET DEBIT LIFECYCLE (for refund reversals):

  REVERSAL_STAGED → REVERSAL_APPLIED → REVERSAL_COMPLETE

  If wallet balance insufficient for reversal:
  → REVERSAL_STAGED → INSUFFICIENT_BALANCE_HOLD → ESCALATE → FINANCE_ADMIN

WALLET RULES:
  - Wallet balance can never go negative without explicit OVERDRAFT_POLICY
  - All credits/debits must reference a reconciliation_id
  - Wallet_id is globally unique per beneficiary (not per tenant)
  - Trainer may hold multiple wallet sub-accounts: EARNINGS / TAX_WITHHELD / REWARDS
```

---

## 1️⃣7️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
EMIT_FEATURE_VECTOR:
  target  : FEATURE_STORE_AGENT
  schema:
    {
      "user_id"        : "trainer_id OR payer_id OR referrer_id",
      "entity_id"      : "reconciliation_id",
      "feature_name"   : "revenue_behavior",
      "feature_value"  : {
        "avg_transaction_amount"        : "decimal",
        "transaction_frequency_30d"     : "integer",
        "refund_rate_90d"               : "float",
        "split_anomaly_score"           : "float",
        "co_teaching_usage"             : "boolean",
        "referral_reward_earned_30d"    : "decimal",
        "wallet_balance_trend"          : "float",
        "payout_hold_frequency"         : "integer"
      },
      "timestamp"      : "ISO8601",
      "source_agent"   : "REVENUE_SHARE_RECONCILIATION_AGENT"
    }
```

---

## 1️⃣8️⃣ INNOVATION ECONOMY COMPATIBILITY

Ecoskiller's Innovation Economy includes creator royalties and idea monetization.

```yaml
IDEA_ROYALTY_RECONCILIATION:
  scope         : If IDEA_DNA_AGENT emits royalty_event, this agent reconciles it
  split_parties : [IDEA_ORIGINATOR, PLATFORM_ROYALTY_POOL]
  rule_reference: RSRULE-ROYALTY-001-v1 (when defined)
  EMIT_IDEA_VECTOR   : NOT directly applicable (no idea objects processed here)
  ROYALTY_ENGINE     : COMPATIBLE — royalty events processed as revenue events
  COPY_DETECTION     : Revenue withheld if COPY_DETECTION_ENGINE flags plagiarism
                       (HOLD status until GOVERNANCE_BOARD resolution)
```

---

## 1️⃣9️⃣ GROWTH ENGINE HOOK

```yaml
GROWTH_TRIGGERS_ON_RECONCILIATION:

  TRAINER_MILESTONE_XP:
    trigger     : trainer total_lifetime_earnings crosses milestone thresholds
                  (₹10K / ₹50K / ₹1L / ₹5L / ₹10L)
    event       : XP_UPDATE_EVENT + ACHIEVEMENT_EVENT
    target      : GAMIFICATION_ENGINE
    badge       : "EARNINGS_MILESTONE_BADGE"

  REFERRAL_XP:
    trigger     : referral_reward credited to referrer_wallet
    event       : XP_UPDATE_EVENT (50 XP per successful referral reward)
    target      : GAMIFICATION_ENGINE

  CO_TEACHING_SHARE_TRIGGER:
    trigger     : Co-teaching split successfully reconciled
    event       : SHARE_TRIGGER_EVENT (platform prompts trainer to share co-teaching story)
    target      : SOCIAL_ENGINE

  RANK_UPDATE_EVENT:
    trigger     : Trainer monthly earnings calculated
    event       : RANK_UPDATE_EVENT → Trainer leaderboard rank refresh
    target      : INSTITUTE_ERP_DASHBOARD_AGENT + PUBLIC_LEADERBOARD_AGENT
```

---

## 2️⃣0️⃣ PERFORMANCE MONITORING

```yaml
METRICS:
  reconciliation_success_rate     : % events fully reconciled without HOLD
  ledger_imbalance_rate           : % events triggering LEDGER_IMBALANCE
  held_reconciliation_rate        : % events in HELD status (target < 2%)
  disputed_reconciliation_rate    : % events in DISPUTED status
  latency_p50                     : median processing time (ms)
  latency_p95                     : 95th percentile (ms)
  latency_p99                     : 99th percentile (ms)
  fraud_flag_rate                 : % events flagged by ML anomaly model
  tax_extraction_accuracy         : % tax amounts matching expected rates (sampled)
  refund_reversal_success_rate    : % refunds fully reversed without wallet hold
  drift_indicator                 : ML model distribution shift score (0.0–1.0)
  wallet_credit_delay_p95         : time from reconciliation to wallet credit (ms)

OBSERVABILITY_INTEGRATION:
  target              : OBSERVABILITY_AGENT
  transport           : Kafka topic: metrics.revshr.agent
  dashboards_required:
    - Real-time reconciliation success rate
    - Ledger balance health monitor (green/amber/red)
    - Held reconciliation queue depth
    - Fraud flag frequency by product_type
    - Tax extraction volume by jurisdiction
    - Trainer wallet balance distribution (anonymized)
    - Refund reversal success rate

ALERTING:
  - reconciliation_success_rate < 98%   → CRITICAL → FINANCE_ADMIN + PLATFORM_DEVOPS
  - ledger_imbalance_rate > 0%           → CRITICAL IMMEDIATE → FINANCE_ADMIN + CTO
  - latency_p95 > 1 second              → WARNING → PLATFORM_DEVOPS
  - fraud_flag_rate > 5%                → WARNING → FRAUD_DETECTION_AGENT
  - held_queue_depth > 1000             → WARNING → FINANCE_ADMIN
  - drift_indicator > 0.3               → RETRAIN_REQUEST_EVENT
```

---

## 2️⃣1️⃣ VERSIONING POLICY

```yaml
VERSIONING_MODEL     : SEMANTIC VERSIONING — MAJOR.MINOR.PATCH
CURRENT_VERSION      : 1.0.0
AGENT_COMPONENT_VERSIONS:
  agent_version        : 1.0.0   (this spec)
  calculation_model    : RSRA-CALC-v1.0.0
  ml_model             : RSRA-ML-v1.0.0

CHANGE RULES:
  PATCH (1.0.x)  : Bug fixes, logging improvements — no ledger logic change
  MINOR (1.x.0)  : New optional fields, new product_type support — backward compatible
  MAJOR (x.0.0)  : Ledger model change, split algorithm change, tax model change

CRITICAL RULES:
  - Revenue share rules versioned SEPARATELY in REVENUE_SHARE_RULE_ENGINE
  - This agent stores rule_version used at time of calculation on every ledger entry
  - Historical reconciliation entries PERMANENTLY reference their calculation-time versions
  - No retroactive rule application — ever
  - All agent changes ADD-ONLY
  - Backward compatibility window: 3 major versions (financial audit trail continuity)
  - Every model update increments model_version
  - Rollback: revert to previous Kubernetes deployment tag — ledger entries immutable
```

---

## 2️⃣2️⃣ DISPUTE RESOLUTION PROTOCOL

```yaml
DISPUTE_TRIGGER_CONDITIONS:
  - Trainer disputes their split amount
  - Co-trainer disputes their split percentage
  - Payer disputes a charge
  - FRAUD_DETECTION_AGENT flags a reconciliation
  - FINANCE_ADMIN flags an anomaly

DISPUTE_LIFECYCLE:
  OPENED → EVIDENCE_COLLECTED → MODERATED → RESOLVED → CLOSED

DISPUTE_RULES:
  - On dispute opening: PROCESSING_STATUS = DISPUTED
  - ALL payout signals for affected reconciliation_id FROZEN immediately
  - Original inputs, rule versions, and calculation path preserved as dispute evidence
  - Resolution authority: GOVERNANCE_BOARD (for co-teaching splits and trainer disputes)
  - Resolution authority: FINANCE_ADMIN (for tax / technical errors)
  - After resolution: RESOLVED entry appended, original entries NEVER modified
  - CLOSED: payout_signals re-emitted if resolved in favor of payout

DISPUTE_EVIDENCE_PACKAGE:
  - original_input_hash
  - reconciliation_id
  - split_ledger snapshot
  - revenue_share_rule_version (with full rule document reference)
  - tax_rule_version
  - ml_anomaly_score at time of reconciliation
  - co_teaching_agreement snapshot (if applicable)
  - Timeline of all state transitions
```

---

## 2️⃣3️⃣ NON-NEGOTIABLE RULES

### Agent MUST NOT:

```
❌  Trigger or initiate any actual payment disbursement
❌  Modify any historical ledger entry (reversal entries only)
❌  Auto-delete any ledger, wallet, or audit record
❌  Apply a revenue share rule version that was not active at payment time
❌  Process a payment event with status ≠ SUCCESS
❌  Allow cross-tenant wallet credits under any circumstance
❌  Override ML anomaly flags without FINANCE_ADMIN explicit approval
❌  Execute outside the declared DATA_SCOPE
❌  Skip tax extraction for taxable jurisdictions
❌  Allow split percentages that do not sum to 100.00%
❌  Emit a payout_signal when reconciliation_check.ledger_balanced = false
❌  Process duplicate events (idempotency_key deduplication is mandatory)
❌  Accept inbound events without valid HMAC signature
❌  Allow human direct API calls in production
❌  Produce partial outputs — all-or-nothing reconciliation only
❌  Use AI layer for any financial calculation, tax, or split logic
❌  Allow co-teaching agreement modification after first reconciliation against it
❌  Process refund reversal if original_transaction_id reconciliation is not found
```

---

## 2️⃣4️⃣ ECOSKILLER BILLING COMPLIANCE ALIGNMENT

```yaml
PLATFORM_BILLING_COMPLIANCE:
  - Stripe primary payment gateway (abstraction layer required — swappable)
  - Per Dojo Billing Lock (SECTION P2): subscription plans, seat-based pricing,
    mentor license pricing, tournament entry fees, certification fees — ALL reconciled here
  - Feature gating is BILLING_SERVICE responsibility; this agent reconciles post-gate
  - Usage metering inputs consumed from USAGE_METERING_ENGINE per Dojo P2 spec
  - Invoice generation TRIGGERED by this agent, EXECUTED by INVOICE_GENERATION_AGENT
  - GST/VAT handling per Dojo P2 spec: extracted, ledgered, passed to TAX_COMPLIANCE_AGENT
  - Refund workflow per Dojo P2 spec: reversals processed deterministically
  - Coupon system: discount impact consumed from COUPON_ENGINE_AGENT

REGULATORY_COMPLIANCE:
  - Audit trail satisfies financial audit requirements (append-only, 7-year retention)
  - Tax extraction satisfies GST/VAT invoice requirements
  - Ledger model satisfies double-entry accounting principles
  - Dispute records satisfy consumer protection requirements
  - All PII in ledger entries encrypted at rest
```

---

## 2️⃣5️⃣ EXECUTION GOVERNANCE SEAL

```
╔═══════════════════════════════════════════════════════════════════════╗
║   REVENUE_SHARE_RECONCILIATION_AGENT — EXECUTION SEAL v1.0           ║
╠═══════════════════════════════════════════════════════════════════════╣
║  ECOSKILLER ANTIGRAVITY PRODUCTION MODE        : ENABLED              ║
║  FINANCIAL DOMAIN CLASSIFICATION               : CRITICAL SENSITIVITY ║
║  TENANT ISOLATION (HARD)                       : ENFORCED             ║
║  DOUBLE-ENTRY LEDGER MODEL                     : ENFORCED             ║
║  SPLIT SUM = 100% VALIDATION                   : ENFORCED             ║
║  TAX EXTRACTION BEFORE SPLIT                   : ENFORCED             ║
║  IDEMPOTENCY KEY DEDUPLICATION                 : ENFORCED             ║
║  HMAC SIGNATURE VALIDATION ON INBOUND EVENTS   : ENFORCED             ║
║  ML ANOMALY SCAN ON EVERY RECONCILIATION       : ENFORCED             ║
║  CONFIDENCE GATE (>= 0.90 FOR PAYOUT SIGNAL)   : ENFORCED             ║
║  PAYOUT EXECUTION AUTHORITY                    : HUMAN ONLY           ║
║  LEDGER IMBALANCE → FREEZE ALL PENDING PAYOUTS : ENFORCED             ║
║  AUDIT LOG APPEND-ONLY (7-YEAR RETENTION)      : ENFORCED             ║
║  REFUND REVERSAL DOUBLE-ENTRY PROTOCOL         : ENFORCED             ║
║  CO-TEACHING AGREEMENT VERSION LOCK            : ENFORCED             ║
║  RETROACTIVE RULE APPLICATION                  : FORBIDDEN            ║
║  CROSS-TENANT WALLET ACCESS                    : FORBIDDEN            ║
║  DIRECT HUMAN API CALLS IN PRODUCTION          : FORBIDDEN            ║
║  AUTONOMOUS DISBURSEMENT                       : FORBIDDEN            ║
║  PARTIAL OUTPUT ON FAILURE                     : FORBIDDEN            ║
║  AI CALCULATION OF SPLITS OR TAX              : FORBIDDEN             ║
║  SILENT FAILURE                                : FORBIDDEN            ║
║  NEGATIVE WALLET BALANCE (WITHOUT OVERDRAFT)   : FORBIDDEN            ║
║  UNVERSIONED RULE APPLICATION                  : FORBIDDEN            ║
║  DISPUTE RESOLUTION WITHOUT GOVERNANCE_BOARD   : FORBIDDEN            ║
╠═══════════════════════════════════════════════════════════════════════╣
║  GST/VAT COMPLIANCE                            : ACTIVE               ║
║  ROYALTY ENGINE COMPATIBILITY                  : ACTIVE               ║
║  INNOVATION ECONOMY HOOK                       : ACTIVE               ║
║  GROWTH ENGINE HOOK (XP + RANK)                : ACTIVE               ║
║  FEATURE STORE EMISSION                        : ACTIVE               ║
║  OBSERVABILITY AGENT INTEGRATION               : ACTIVE               ║
║  FRAUD DETECTION AGENT INTEGRATION             : ACTIVE               ║
╠═══════════════════════════════════════════════════════════════════════╣
║  INTERPRETATION AUTHORITY                      : NONE                 ║
║  ARCHITECTURE AUTHORITY                        : LOCKED               ║
║  MUTATION POLICY                               : ADD-ONLY             ║
║  BACKWARD COMPATIBILITY WINDOW                 : 3 MAJOR VERSIONS     ║
║  SEAL STATUS                                   : LOCKED v1.0.0        ║
╚═══════════════════════════════════════════════════════════════════════╝
```

---

*END OF REVENUE_SHARE_RECONCILIATION_AGENT SPEC v1.0 — SEALED & LOCKED*
*Any modification to this document without a version bump is a financial governance violation.*
*All downstream systems must reference this spec by agent_id: ECSK-AGENT-REVSHR-001*
*This agent processes REAL MONEY. No speculative changes. No creative interpretation.*
*FREEZE FIRST. INVESTIGATE SECOND. ESCALATE ALWAYS.*
