# 🔒 SEALED & LOCKED AGENT PROMPT
## `ACCOUNTING_SYNC_AGENT.md`
### Ecoskiller Enterprise SaaS — Accounting Synchronization Agent
#### Execution Target: Google Antigravity Tool — Production SaaS Generator

---

```
PROMPT_CLASS              = ACCOUNTING_SYNC_AGENT
AGENT_ID                  = ASA-v1.0
PROMPT_VERSION            = 1.0.0
MUTATION_POLICY           = ADD_ONLY
EXECUTION_MODE            = LOCKED
CREATIVE_INTERPRETATION   = FORBIDDEN
ASSUMPTION_FILLING        = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY
FAILURE_MODE              = STOP_AND_ESCALATE
OWNER                     = PLATFORM_FINANCE_OPS
COMPLIANCE_TIER           = CRITICAL
PARENT_PROMPT             = ECOSKILLER_MASTER_PROMPT_v1
```

---

> ⚠️ **ANTIGRAVITY MUST NOT** reinterpret, simplify, or creatively extend any rule in this agent.
> Every section is enforceable law. Violation of any rule = **PLATFORM-ILLEGAL STATE**.
> This agent inherits ALL base platform locks (RBAC, ABAC, MFA, Audit, Tenant Isolation) without re-declaration.

---

## 0️⃣ AGENT OVERVIEW

### Purpose

The `ACCOUNTING_SYNC_AGENT` is a bounded, policy-controlled software agent responsible for **bidirectional synchronization** between the Ecoskiller platform's internal financial subsystems (Billing Service, ERP Layer, Analytics) and the canonical General Ledger. It does **not** make business decisions. It enforces accounting governance automatically, flags violations, and stops execution on policy failure.

### Scope

```
IN SCOPE:
  ✔ Subscription billing ↔ Ledger sync
  ✔ Invoice generation ↔ GL posting
  ✔ Payout / commission ↔ Ledger credit entries
  ✔ GST / VAT ↔ Tax ledger reconciliation
  ✔ Revenue recognition per ASC 606 / IFRS 15
  ✔ Period close enforcement & lock
  ✔ Inter-entity eliminations (multi-tenant)
  ✔ Deferred revenue amortization
  ✔ Trainer / mentor revenue share sync
  ✔ Refund & chargeback reversal entries
  ✔ Scheduled reconciliation via Apache Airflow
  ✔ Event-driven sync via Kafka
  ✔ Fraud & anomaly detection signals
  ✔ Audit trail generation (immutable)
  ✔ Financial statement readiness feed

OUT OF SCOPE:
  ✗ Human approval workflows (handled by Finance Manager role)
  ✗ Tax filing submission (handled by TFC compliance module)
  ✗ UI rendering (handled by ERP_UI module)
  ✗ Customer billing communication (handled by Notification Service)
  ✗ Payroll (separate payroll service — not yet sealed)
```

---

## 1️⃣ COMPLIANCE INHERITANCE (NON-NEGOTIABLE)

This agent **INHERITS** and **ENFORCES** the following already-sealed platform sections. Do not redefine them — enforce them:

| Inherited Section | Scope Applied |
|---|---|
| `AUTHZ-MASTER-v1` | RBAC + ABAC on every accounting action |
| `SESSION_MANAGEMENT_COMPLIANCE` | All agent API calls respect session context |
| `DATA_ENCRYPTION_AT_REST` | All ledger records encrypted at rest |
| `AUDIT_TRAIL_LOCK` | Every sync action logged immutably |
| `TENANT_ISOLATION` | Tenant IDs injected into every ledger entry |
| `MFA_COMPLIANCE` | Maker-checker flows require MFA-verified sessions |
| `ASC-A through ASC-Z` | Full GAAP / IFRS accounting standards |
| `TFC-A through TFC-U` | Corporate tax filing compliance |
| `IVT-A through IVT-P` | GST / VAT / Sales Tax compliance |
| `P2 — BILLING & SUBSCRIPTION LOCK` | Billing engine rules |
| `R84 — TRAINER REVENUE & MONETIZATION` | Trainer payout accounting rules |

---

## 2️⃣ AGENT IDENTITY & EXECUTION MODEL

```
AGENT_NAME          = AccountingSyncAgent
AGENT_TYPE          = POLICY_ENFORCED_ASYNC_WORKER
RUNTIME             = Apache Airflow DAGs + Kafka Consumers
LANGUAGE            = Python 3.11 (primary), TypeScript (secondary for event validators)
EXECUTION_CONTEXT   = Internal — Never exposed to public API
AUTH_MODEL          = Service Account (non-human) + HMAC signed payloads
SECRETS_STORE       = HashiCorp Vault (injected at runtime, never hardcoded)
LOGGING             = OpenTelemetry → Loki / ELK
METRICS             = Prometheus (billing_sync_*, ledger_*, reconciliation_*)
TRACING             = Distributed trace per sync transaction
```

### Execution Triggers (All Supported, None Optional)

```
TRIGGER_1: Kafka Event Consumer       → Real-time event-driven posting
TRIGGER_2: Apache Airflow DAG         → Scheduled batch reconciliation
TRIGGER_3: REST Internal API Call     → On-demand reconciliation (human-initiated only)
TRIGGER_4: Period Close Signal        → End-of-period lock + final reconciliation
```

---

## 3️⃣ SERVICE CONNECTIVITY MAP (LOCKED)

Antigravity must generate service clients strictly using the following endpoints. No other endpoints are valid for accounting operations.

```yaml
services:
  billing_service:
    internal_url: http://billing-service:8011
    auth: service_account_jwt
    allowed_operations:
      - GET /subscriptions
      - GET /invoices
      - GET /refunds
      - GET /payouts
      - GET /charges
      - POST /reconciliation-webhook-ack

  ledger_service:
    internal_url: http://ledger-service:8015
    auth: service_account_jwt
    allowed_operations:
      - POST /journal-entries
      - GET /journal-entries
      - GET /account-balances
      - POST /period-close
      - GET /coa  # Chart of Accounts

  tax_service:
    internal_url: http://tax-service:8016
    auth: service_account_jwt
    allowed_operations:
      - GET /gst-summary
      - GET /vat-summary
      - POST /tax-entry-ack

  analytics_service:
    internal_url: http://analytics-service:8012
    auth: service_account_jwt
    allowed_operations:
      - POST /revenue-event
      - GET /revenue-metrics

  minio_storage:
    internal_url: http://minio:9000
    bucket: ecoskiller-accounting
    sub_buckets:
      - invoices/
      - journal-entries/
      - reconciliation-reports/
      - audit-archives/

  kafka:
    brokers: [kafka-broker:9092]
    consumer_group: accounting-sync-agent
    producer_acks: all
    topics_consumed:
      - invoice.generated
      - payment.settled
      - payment.failed
      - subscription.created
      - subscription.upgraded
      - subscription.cancelled
      - refund.initiated
      - refund.completed
      - payout.requested
      - payout.approved
      - payout.disbursed
      - commission.calculated
      - chargeback.raised
      - chargeback.resolved
      - period.close.initiated
      - tenant.entity.declared
    topics_produced:
      - ledger.entry.posted
      - reconciliation.completed
      - reconciliation.failed
      - anomaly.detected
      - period.close.confirmed
      - period.close.failed

  airflow:
    connection: airflow-accounting-sync
    dag_namespace: ecoskiller.accounting
```

---

## 4️⃣ CHART OF ACCOUNTS (CoA) — LOCKED SCHEMA

> **Rule:** The CoA is version-controlled and immutable per period. No ad-hoc accounts in production. All accounts must be mapped before first use.

### CoA Top-Level Structure

```
ASSETS (1xxx)
  1100 — Cash & Cash Equivalents
  1200 — Accounts Receivable (Platform)
  1210 — Accounts Receivable (Subscriptions)
  1220 — Accounts Receivable (Certification Fees)
  1230 — Accounts Receivable (Tournament Entry Fees)
  1300 — Deferred Tax Asset (DTA)
  1400 — Prepaid Expenses
  1500 — Input Tax Credit (ITC) Receivable

LIABILITIES (2xxx)
  2100 — Accounts Payable (Mentor / Trainer Payouts)
  2200 — Deferred Revenue (Subscriptions — Unearned)
  2210 — Deferred Revenue (Certifications — Unearned)
  2300 — GST / VAT Payable
  2310 — CGST Payable
  2320 — SGST Payable
  2330 — IGST Payable
  2400 — Deferred Tax Liability (DTL)
  2500 — Refund Liability
  2600 — Chargeback Reserve

EQUITY (3xxx)
  3100 — Retained Earnings
  3200 — Platform Revenue Reserve

REVENUE (4xxx)
  4100 — Subscription Revenue (SaaS)
  4110 — Monthly Subscription Revenue
  4120 — Annual Subscription Revenue
  4130 — Seat-Based Subscription Revenue
  4200 — Certification Fee Revenue
  4300 — Tournament / Match Fee Revenue
  4400 — Mentor License Revenue
  4500 — Commission Revenue (Marketplace)
  4600 — Placement Fee Revenue
  4700 — Advertisement / Sponsor Revenue (future)
  4800 — Grant Revenue (Science domain)
  4900 — Trainer Course Revenue (Commerce domain)

COST OF REVENUE (5xxx)
  5100 — Mentor / Trainer Payout Cost
  5200 — Payment Gateway Fees
  5300 — Infrastructure Cost Allocation
  5400 — Revenue Share — Third Party

OPERATING EXPENSES (6xxx)
  6100 — Platform Operations
  6200 — Compliance & Legal
  6300 — Support & Customer Success
  6400 — R&D / Engineering Allocation

TAX ACCOUNTS (9xxx)
  9100 — Corporate Tax Expense
  9200 — Deferred Tax Movement
  9300 — GST / VAT Collected
  9400 — Input Tax Credit Utilized
  9500 — TDS Payable (India)
  9600 — Withholding Tax Payable
```

### CoA Rules (HARD)

```
RULE CoA-1:  CoA version must be declared per fiscal year. Changes require approval + disclosure.
RULE CoA-2:  Domain-aware mapping is mandatory:
             Arts    → Accounts 4800, 4900 area
             Commerce → Accounts 4100–4500 area
             Science  → Accounts 4800, 4600 area
RULE CoA-3:  Cross-domain account misclassification ⇒ ENTRY REJECTED
RULE CoA-4:  New accounts require finance_manager + auditor dual approval before activation.
RULE CoA-5:  Account deactivation requires zero-balance confirmation.
```

---

## 5️⃣ JOURNAL ENTRY GOVERNANCE (LOCKED)

### Entry Schema (PostgreSQL)

```sql
CREATE TABLE journal_entries (
  entry_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id          UUID NOT NULL,                          -- Hard tenant isolation
  entity_id          UUID NOT NULL,                          -- Legal entity
  entry_date         DATE NOT NULL,
  posting_date       DATE NOT NULL,
  period_id          VARCHAR(7) NOT NULL,                    -- 'YYYY-MM' format
  source_event       VARCHAR(100) NOT NULL,                  -- e.g. 'invoice.generated'
  source_ref_id      UUID NOT NULL,                          -- FK to source record
  entry_type         VARCHAR(50) NOT NULL,                   -- AUTO | MANUAL | ADJUSTMENT | REVERSAL
  debit_account      VARCHAR(10) NOT NULL REFERENCES coa(account_code),
  credit_account     VARCHAR(10) NOT NULL REFERENCES coa(account_code),
  amount             NUMERIC(18,4) NOT NULL CHECK (amount > 0),
  currency           CHAR(3) NOT NULL DEFAULT 'INR',
  fx_rate            NUMERIC(12,6),                          -- Required if currency != functional_currency
  functional_amount  NUMERIC(18,4) NOT NULL,                 -- Always in functional currency
  domain_tag         VARCHAR(20) NOT NULL,                   -- Arts | Commerce | Science | Platform
  description        TEXT NOT NULL,
  supporting_doc_url VARCHAR(500),                           -- MinIO path
  created_by_agent   VARCHAR(100) NOT NULL,                  -- 'AccountingSyncAgent/v1.0'
  approved_by        UUID,                                   -- NULL for auto, required for MANUAL
  second_approved_by UUID,                                   -- Maker-checker: required for MANUAL
  is_reversed        BOOLEAN DEFAULT FALSE,
  reversal_entry_id  UUID REFERENCES journal_entries(entry_id),
  is_period_locked   BOOLEAN DEFAULT FALSE,
  audit_hash         VARCHAR(64) NOT NULL,                   -- SHA-256 of entry fields
  created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT debit_ne_credit CHECK (debit_account != credit_account),
  CONSTRAINT no_post_lock_edit CHECK (
    NOT (is_period_locked = TRUE AND entry_type NOT IN ('REVERSAL', 'ADJUSTMENT'))
  )
);
CREATE INDEX idx_je_tenant_period ON journal_entries(tenant_id, period_id);
CREATE INDEX idx_je_source_ref ON journal_entries(source_ref_id);
CREATE INDEX idx_je_entry_date ON journal_entries(entry_date);
```

### Entry Rules (HARD)

```
RULE JE-1:  Every entry MUST have a source_ref_id traceable to a Billing Service record.
RULE JE-2:  Debit amount MUST equal Credit amount (double-entry integrity).
RULE JE-3:  AUTO entries require NO human approval.
RULE JE-4:  MANUAL entries require maker + checker (two distinct humans, neither is the initiator).
RULE JE-5:  ADJUSTMENT entries require finance_manager + auditor dual approval.
RULE JE-6:  Period-locked entries are IMMUTABLE. Post-lock edit ⇒ REJECTED.
RULE JE-7:  Reversal of a locked entry requires a new REVERSAL entry (never mutation).
RULE JE-8:  audit_hash = SHA-256(entry_id + tenant_id + debit + credit + amount + period_id).
RULE JE-9:  Supporting document URL is MANDATORY for MANUAL and ADJUSTMENT entries.
RULE JE-10: All entries emitted as Kafka event 'ledger.entry.posted' after commit.
```

---

## 6️⃣ EVENT-DRIVEN SYNC FLOWS (KAFKA — ALL REQUIRED)

Each Kafka event triggers a deterministic, policy-enforced accounting action. No interpretation allowed.

---

### EVENT: `invoice.generated`

```
TRIGGER: Billing Service generates invoice for subscription / certification / tournament fee
AGENT ACTION:
  1. Validate invoice fields (per IVT-G): invoice_number, date, supplier_tax_id,
     taxable_value, tax_rate, tax_amount, HSN/SAC code
  2. Determine supply classification (IVT-C):
     subscription | certification | digital_service | training
  3. Determine place of supply (IVT-E):
     intra-state → CGST + SGST | inter-state → IGST | export → zero-rated
  4. Verify GST rate from version-controlled rate table (IVT-F)
  5. POST journal entries:
       DR  1210 / 1220 / 1230  Accounts Receivable      [invoice gross amount]
           CR  4100–4900        Revenue (Deferred)        [taxable value]
           CR  2310/2320/2330   GST Payable               [tax amount]
  6. Store invoice PDF → MinIO: invoices/{tenant_id}/{invoice_id}.pdf
  7. Emit: ledger.entry.posted
  8. On ITC eligibility: queue 1500 account credit for next cycle

FAILURE:
  - Missing tax_id → BLOCK INVOICE (IVT-B)
  - Misclassified supply → ENTRY REJECTED (IVT-C)
  - Tax rate mismatch → AUDIT FLAG (IVT-F)
```

---

### EVENT: `payment.settled`

```
TRIGGER: Payment gateway confirms successful payment (Stripe / abstracted provider)
AGENT ACTION:
  1. Match payment to open invoice (reconcile)
  2. Enforce ASC 606 / IFRS 15 five-step recognition:
       Step 1: Contract identified (subscription agreement)
       Step 2: PO = platform access / certification / match slot
       Step 3: Transaction price = settled amount (net of gateway fee)
       Step 4: Allocation = single PO or proportional to SSP if bundled
       Step 5: Recognition = ratable (subscription) | point-in-time (certification, tournament)
  3. POST cash receipt entry:
       DR  1100  Cash                                    [settled amount]
           CR  1210 / 1220 / 1230  A/R                  [invoice amount]
  4. For subscription (ratable recognition):
       DR  2200 / 2210  Deferred Revenue                [full subscription amount]
           CR  4110 / 4120  Subscription Revenue        [current-period portion only]
     (Airflow DAG handles monthly amortization of remaining deferred balance)
  5. Record gateway cost:
       DR  5200  Payment Gateway Fees
           CR  1100  Cash
  6. Emit: ledger.entry.posted

FAILURE:
  - Unmatched payment → HOLD + alert finance_manager
  - Double settlement detected → BLOCK + anomaly.detected
```

---

### EVENT: `payment.failed`

```
TRIGGER: Payment gateway reports failed charge
AGENT ACTION:
  1. Reverse any provisional posting (REVERSAL entry type)
  2. Mark A/R as disputed
  3. No revenue recognized under any circumstance
  4. Notify analytics_service of failed payment metric
  5. Emit: ledger.entry.posted (reversal)

FAILURE MODE: Silent failure ⇒ ESCALATE immediately
```

---

### EVENT: `subscription.created`

```
TRIGGER: New subscription plan activated for tenant user
AGENT ACTION:
  1. Determine plan type: monthly | annual | seat-based | mentor-license
  2. Compute deferred revenue = total subscription value
  3. POST:
       DR  1210  A/R (Subscription)                    [full plan value]
           CR  2200  Deferred Revenue (Subscription)   [full plan value]
  4. Tag: domain = Commerce, entity_id = tenant entity
  5. Emit: ledger.entry.posted
```

---

### EVENT: `subscription.upgraded`

```
TRIGGER: User upgrades plan mid-cycle
AGENT ACTION:
  1. Calculate proration:
       remaining_value = (days_remaining / total_days) × old_plan_value
       incremental_value = new_plan_value_prorated - remaining_value
  2. POST incremental deferred revenue entry only
  3. Update A/R delta
  4. Emit: ledger.entry.posted

RULE: No revenue recognized at upgrade point. Recognition continues ratably.
```

---

### EVENT: `subscription.cancelled`

```
TRIGGER: User cancels subscription (immediate or end-of-period)
AGENT ACTION:
  1. Compute unearned deferred revenue balance
  2. If refund_eligible: queue refund.initiated event
  3. If no refund:
       DR  2200  Deferred Revenue                      [unearned balance]
           CR  4110  Subscription Revenue              [accelerated recognition]
  4. Close A/R line
  5. Emit: ledger.entry.posted

RULE: Accelerated recognition without contractual basis ⇒ FLAG for auditor review
```

---

### EVENT: `refund.initiated` + `refund.completed`

```
TRIGGER: Billing Service initiates and completes refund
AGENT ACTION (on initiated):
  1. Create refund liability:
       DR  2500  Refund Liability
           CR  1100  Cash

AGENT ACTION (on completed):
  1. Reverse original revenue (proportional to refund amount):
       DR  4100–4900  Revenue (reversal)
           CR  2500    Refund Liability
  2. Reverse GST proportionally (IVT-P: credit note linked to original invoice):
       DR  2310/2320/2330  GST Payable
           CR  2500  Refund Liability   [GST portion]
  3. Generate credit note → MinIO: invoices/{tenant_id}/credit-note-{id}.pdf
  4. Emit: ledger.entry.posted

RULE: Silent refund adjustment ⇒ ILLEGAL (IVT-P). Credit note is mandatory.
```

---

### EVENT: `payout.requested` → `payout.approved` → `payout.disbursed`

```
TRIGGER: Trainer / mentor requests payout of earned revenue share
AGENT ACTION (on requested):
  1. Validate: payout_amount <= settled_and_recognized_revenue_share
  2. Validate: mentor is NOT the payout approver (R84: dual-approval mandatory)
  3. POST payable:
       DR  5100  Trainer / Mentor Payout Cost
           CR  2100  A/P (Mentor Payouts)             [payout amount]
  4. Emit: ledger.entry.posted

AGENT ACTION (on approved):
  1. Confirm second approver != requester and != first approver
  2. Log to journal_entries.approved_by + second_approved_by

AGENT ACTION (on disbursed):
  1. Settle payable:
       DR  2100  A/P (Mentor Payouts)
           CR  1100  Cash
  2. Compute TDS (India, Section 194J for professional fees):
       DR  2100  A/P (Mentor Payouts)       [TDS portion]
           CR  9500  TDS Payable
  3. Generate payout receipt → MinIO: invoices/{tenant_id}/payout-{id}.pdf
  4. Record: disbursement_id, timestamp, bank_reference, TDS_certificate_ref
  5. Emit: ledger.entry.posted

FAILURE:
  - Single-actor payout approval ⇒ DENY (ASC-G, R84)
  - Payout exceeds earned balance ⇒ BLOCK
  - Duplicate payout detected ⇒ HOLD + anomaly.detected
```

---

### EVENT: `commission.calculated`

```
TRIGGER: Marketplace commission calculated on recruiter/enterprise placement fee
AGENT ACTION:
  1. Classify: Commerce domain → Account 4500 (Commission Revenue)
  2. Split if co-mentor arrangement (revenue split rules from R84)
  3. POST:
       DR  1200  A/R (Platform)
           CR  4500  Commission Revenue               [platform commission]
  4. If cross-border: apply FX rate (ASC-K), declare FX source explicitly
  5. Emit: ledger.entry.posted

RULE: Commissions recognized NET (ASC-F, Commerce). Gross presentation forbidden unless principal.
```

---

### EVENT: `chargeback.raised` + `chargeback.resolved`

```
TRIGGER: Payment provider raises chargeback dispute
AGENT ACTION (on raised):
  1. Transfer A/R to reserve:
       DR  2600  Chargeback Reserve
           CR  1210  A/R (disputed)
  2. Re-defer recognized revenue:
       DR  4100–4900  Revenue
           CR  2200    Deferred Revenue (re-deferred)
  3. Flag: anomaly.detected (MEDIUM)

AGENT ACTION (on resolved — WON):
  1. Restore A/R:
       DR  1210  A/R
           CR  2600  Chargeback Reserve
  2. Re-recognize revenue

AGENT ACTION (on resolved — LOST):
  1. Write off:
       DR  6100  Platform Operations (bad debt)
           CR  2600  Chargeback Reserve
  2. Reverse GST (IVT-P: credit note required)
```

---

### EVENT: `period.close.initiated`

```
TRIGGER: Finance Manager initiates period close for YYYY-MM
AGENT ACTION:
  1. Run full reconciliation DAG (all 6 layers — see Section 8)
  2. Validate: all open invoices matched
  3. Validate: GST payable reconciled with return_type (IVT-N)
  4. Validate: deferred revenue amortized for period
  5. Validate: no unresolved anomalies flagged HIGH or CRITICAL
  6. If ALL validations pass:
       → SET period_id YYYY-MM as LOCKED in ledger
       → Emit: period.close.confirmed
  7. If ANY validation fails:
       → Emit: period.close.failed with validation_errors[]
       → BLOCK close until all errors resolved

RULE: Post-close edits ⇒ FORBIDDEN (ASC-H). Only REVERSAL entries permitted post-lock.
RULE: Reopening requires finance_manager + auditor + platform_finance_ops triple authorization.
```

---

## 7️⃣ APACHE AIRFLOW DAGs (SCHEDULED — ALL MANDATORY)

```
DAG_NAMESPACE: ecoskiller.accounting
```

| DAG ID | Schedule (Cron) | Description |
|---|---|---|
| `deferred_revenue_amortization` | `0 1 1 * *` (Monthly 1st) | Recognize earned portion of deferred subscription revenue |
| `tax_ledger_reconciliation` | `0 2 * * *` (Daily) | Reconcile invoice GST ↔ GST payable ↔ ITC |
| `ar_aging_report` | `0 6 * * 1` (Weekly Mon) | Identify overdue receivables, flag write-off candidates |
| `payout_liability_reconciliation` | `0 3 * * *` (Daily) | Reconcile A/P payouts vs disbursed payouts |
| `revenue_recognition_audit` | `0 4 1 * *` (Monthly 1st) | Validate all recognized revenue follows 5-step model |
| `inter_entity_elimination` | `0 5 1 * *` (Monthly 1st) | Eliminate inter-company transactions for consolidation |
| `fx_revaluation` | `0 0 * * *` (Daily) | Revalue FX-denominated balances with declared rate source |
| `impairment_signal_scan` | `0 8 1 */3 *` (Quarterly) | Scan impairment indicators on assets |
| `fraud_anomaly_scan` | `0 */4 * * *` (Every 4 hrs) | Benford, round-number bias, late-period spikes |
| `audit_archive_snapshot` | `0 23 * * *` (Daily) | Snapshot journal_entries to MinIO audit-archives/ |
| `financial_statement_feed` | `0 6 1 * *` (Monthly 1st) | Generate BS / IS / CFS data feed for ERP_UI |
| `tds_payable_reconciliation` | `0 5 7 * *` (Monthly 7th) | Reconcile TDS payable before statutory due date |
| `going_concern_assessment` | `0 9 1 1,4,7,10 *` (Quarterly) | Flag going-concern risk if liabilities > assets 2 consecutive quarters |

### DAG: `deferred_revenue_amortization` (Logic)

```
For each active subscription per tenant per entity:
  earned_amount = (days_elapsed_in_period / total_subscription_days) × plan_value
  already_recognized = sum(journal_entries WHERE source_ref = subscription_id AND period = current)
  delta = earned_amount - already_recognized
  If delta > 0:
    POST:
      DR  2200  Deferred Revenue                 [delta]
          CR  4110/4120  Subscription Revenue    [delta]
  domain_tag must match subscription plan domain
  period lock check: if period already locked → SKIP + ALERT
  entry source_event = 'airflow.deferred_revenue_amortization'
```

### DAG: `fraud_anomaly_scan` (Signals per ASC-S)

```
SIGNAL               THRESHOLD                               SEVERITY
Benford's Law dev    first-digit chi-square > 2σ             MEDIUM
Round-number bias    >15% entries ending .00 (excl subs)    MEDIUM
Late-period spike    >40% revenue posted in last 3 days      HIGH
Rapid reversal       same account DR+CR within 24h           HIGH
Duplicate source_ref Two entries with same source_ref_id     CRITICAL — BLOCK second
Payout inflation     payout_amount > earned_balance          CRITICAL — BLOCK payout
Single-actor manual  Manual entry without dual approvers     CRITICAL — DENY

ON SIGNAL:
  Emit: anomaly.detected {signal_type, entry_ids[], severity, tenant_id}
  Alert: finance_manager + auditor via Notification Service
  NEVER auto-reverse — human decision required
  MEDIUM → alert only | HIGH → HOLD account | CRITICAL → ESCALATE to compliance
```

---

## 8️⃣ RECONCILIATION ENGINE (MANDATORY)

### Six Reconciliation Layers (All Required)

```
LAYER 1 — Invoice ↔ Ledger (IVT-N):
  Every invoice_id in billing_service must match a journal_entries.source_ref_id
  Invoice taxable_value must match ledger A/R debit (net of GST)
  Invoice tax_amount must match GST payable credit
  Mismatch tolerance: ZERO

LAYER 2 — GST / Tax Ledger ↔ Return (IVT-N, TFC-H):
  Sum of 2310+2320+2330 per period = GST return outward supply total
  ITC in 1500 account = verified ITC claims
  Mismatch ⇒ BLOCK PERIOD CLOSE + ESCALATE

LAYER 3 — Revenue ↔ Deferred Revenue Roll-Forward (ASC-E):
  Opening Deferred + Billed - Recognized = Closing Deferred
  Validated per entity, per cohort
  Unreconciled variance ⇒ DENY Financial Statement Feed

LAYER 4 — Payout A/P ↔ Disbursement:
  Sum of 2100 credits (payables created) = Sum of 2100 debits (paid) + outstanding balance
  Duplicate payouts → BLOCK
  Undisbursed payouts > 60 days → FLAG for review

LAYER 5 — Cash Reconciliation (1100 Account):
  DR Cash (payment.settled) = Stripe settlement reports
  CR Cash (payouts.disbursed) = payout_disbursement_records
  Net balance reconciled daily with bank statement feed

LAYER 6 — Inter-Entity Elimination (ASC-J):
  All transactions between Ecoskiller legal entities eliminated
  Elimination entries generated automatically
  Minority interest calculated where applicable
  Unreconciled inter-entity ⇒ MISSTATEMENT
```

### Reconciliation Output Schema (PostgreSQL)

```sql
CREATE TABLE reconciliation_runs (
  run_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id         UUID NOT NULL,
  period_id         VARCHAR(7) NOT NULL,
  run_type          VARCHAR(50) NOT NULL,   -- DAILY | PERIOD_CLOSE | ON_DEMAND
  layer             VARCHAR(50) NOT NULL,   -- INVOICE_LEDGER | GST | REVENUE | PAYOUT | CASH | INTER_ENTITY
  status            VARCHAR(20) NOT NULL,   -- PASS | FAIL | PARTIAL
  total_checked     INTEGER NOT NULL,
  total_passed      INTEGER NOT NULL,
  total_failed      INTEGER NOT NULL,
  variance_amount   NUMERIC(18,4),
  errors            JSONB,                 -- [{entry_id, expected, actual, delta}]
  started_at        TIMESTAMPTZ NOT NULL,
  completed_at      TIMESTAMPTZ,
  triggered_by      VARCHAR(100) NOT NULL, -- 'airflow.dag' | 'period_close' | 'human:{user_id}'
  report_url        VARCHAR(500)           -- MinIO: reconciliation-reports/{run_id}.pdf
);
```

---

## 9️⃣ REVENUE RECOGNITION ENGINE (ASC 606 / IFRS 15 — LOCKED)

### 5-Step Enforcement Matrix

```
STEP 1 — CONTRACT IDENTIFICATION:
  Required fields: subscription_id | certification_id | tournament_id
  Missing contract reference ⇒ BLOCK revenue recognition

STEP 2 — PERFORMANCE OBLIGATIONS (PO):
  Subscription → platform access (time-based, over-time)
  Certification → assessment + certificate issuance (point-in-time)
  Tournament → match slot (point-in-time)
  Bundled PO: allocate by SSP (standalone selling price)
  Bundled PO without declared SSP ⇒ BLOCK until SSP declared in config

STEP 3 — TRANSACTION PRICE:
  = gross_amount - refund_reserve_estimate - variable_consideration
  Variable consideration (usage-based) → constrained until highly probable
  Gateway fee is NOT part of transaction price (deducted as cost to revenue)

STEP 4 — ALLOCATION:
  Single PO: 100% to that obligation
  Bundled: proportional to relative SSP
  SSP table must be version-controlled in platform config

STEP 5 — RECOGNITION TIMING:
  Over time (subscription):        Ratably over subscription period via Airflow DAG
  Point in time (certification):   On certificate issuance (event: certification.issued)
  Point in time (tournament):      On match completion (event: gd.completed)
  Point in time (belt progression): On belt award (event: belt.eligible confirmed)
```

---

## 1️⃣0️⃣ MULTI-TENANT & ENTITY ISOLATION (HARD LOCK)

```
RULE MT-1:  Every journal entry MUST carry tenant_id + entity_id. Missing ⇒ REJECT.
RULE MT-2:  Agent queries are always tenant-scoped. Cross-tenant data access = SECURITY FAILURE.
RULE MT-3:  Institute entity ≠ Corporate entity ≠ Platform entity.
            No cross-entity netting without legal consolidation basis (ASC-J).
RULE MT-4:  Each tenant must declare on event 'tenant.entity.declared':
              - primary_accounting_framework (GAAP | IFRS)
              - functional_currency
              - tax_jurisdiction + tax_registration_id (GSTIN or equivalent)
            Missing declaration → accounting functions DISABLED for that tenant.
RULE MT-5:  Tenant declaration immutable per reporting period.
RULE MT-6:  Arts / Commerce / Science revenue accounts must not cross-post.
            Cross-domain misclassification ⇒ ERROR STATE (ASC-F, IVT-D).
```

---

## 1️⃣1️⃣ CURRENCY & FX HANDLING (ASC-K — LOCKED)

```
RULE FX-1:  Functional currency declared per entity at onboarding. Cannot change mid-period.
RULE FX-2:  FX rate source declared and version-controlled.
            Accepted: RBI reference rate (India) | ECB rate (EU) | Stripe FX (payment layer)
RULE FX-3:  Translation (foreign entity → reporting) vs Remeasurement (monetary items) separated.
RULE FX-4:  FX rate locked at transaction date for revenue recognition.
RULE FX-5:  Unrealized FX gains/losses on A/R and A/P revalued daily (DAG: fx_revaluation).
RULE FX-6:  Cross-border payout FX holding > 30 days must be disclosed.
RULE FX-7:  Implicit FX assumption ⇒ ERROR. All FX must be explicit in journal entries.
```

---

## 1️⃣2️⃣ PERIOD CLOSE & LOCK PROTOCOL (ASC-H — LOCKED)

```
PHASE 1 — PRE-CLOSE CHECKLIST (automated by agent):
  □ All invoices for period have matching journal entries
  □ All payment.settled events matched to invoices
  □ deferred_revenue_amortization DAG completed for period
  □ GST reconciliation LAYER 2: PASS
  □ Payout A/P reconciliation LAYER 4: PASS
  □ No open anomaly.detected events at HIGH or CRITICAL severity
  □ FX revaluation run
  □ Inter-entity eliminations applied

PHASE 2 — CLOSE AUTHORIZATION (human, MFA-required):
  □ finance_manager reviews pre-close report
  □ auditor confirms GL trial balance
  □ Both approve via MFA-verified session
  □ Maker ≠ Checker (enforced at DB level)

PHASE 3 — LOCK EXECUTION (agent):
  □ SET period_id YYYY-MM = LOCKED
  □ Emit: period.close.confirmed
  □ Snapshot all journal_entries to MinIO audit-archives/YYYY-MM/
  □ Run financial_statement_feed DAG

PHASE 4 — POST-CLOSE RULES:
  □ No mutations to locked period entries (DB constraint enforced)
  □ Corrections only via new REVERSAL + ADJUSTMENT entry pair
  □ Reopening: requires finance_manager + auditor + platform_finance_ops (triple MFA)
```

---

## 1️⃣3️⃣ FINANCIAL STATEMENTS FEED (ASC-P — LOCKED)

Agent generates **structured data feeds only**. Rendering is ERP_UI responsibility.

```
BALANCE SHEET FEED:
  Assets:      1xxx account balances as of period end
  Liabilities: 2xxx account balances as of period end
  Equity:      3xxx account balances as of period end
  RULE: Assets must equal Liabilities + Equity before feed emitted.
  Imbalance ⇒ HALT + ESCALATE (never emit imbalanced BS)

INCOME STATEMENT FEED:
  Revenue:           4xxx account balances for period
  Cost of Revenue:   5xxx account balances for period
  Operating Expenses: 6xxx account balances for period
  Gross Profit, Operating Income auto-computed

CASH FLOW STATEMENT FEED:
  Operating: Net income ± working capital changes (A/R, A/P, Deferred Revenue movement)
  Investing: Infrastructure capex from 6xxx
  Financing: N/A for platform entity in MVP

SEGMENT REPORT FEED (ASC-Y):
  Arts | Commerce | Science | Platform segments reported separately
  Revenue and cost allocated by domain_tag
  Cross-segment allocation documented with methodology

NOTES FEED:
  Accounting policy elections (ASC-V)
  Revenue recognition policies
  GST / tax disclosures
  Related-party transactions (ASC-X): declared relationship + arm's-length assessment
  Going-concern assessment (ASC-Z): documented quarterly
  Subsequent events: captured until feed emission date

RULE FS-1: Feed not generated if any reconciliation layer = FAIL for the period.
RULE FS-2: Balance sheet imbalance ⇒ HALT + page platform_finance_ops.
RULE FS-3: Revenue figures must reconcile with contract subledger ↔ GL (ASC-Y).
```

---

## 1️⃣4️⃣ SECURITY & ACCESS CONTROL (HARD LOCK)

```
ROLE                  PERMISSIONS
----------------------------------------------------------
AccountingSyncAgent   READ: billing_service, ledger_service, tax_service
(service account)     WRITE: journal_entries (AUTO only), reconciliation_runs
                      EMIT: Kafka producer topics listed in Section 3
                      NEVER: approve manual entries, initiate payouts, file taxes

accountant            READ: journal_entries, coa, reconciliation_runs (own tenant)
                      WRITE: journal_entries (MANUAL, after maker-checker)
                      NEVER: approve own entries, access another tenant

finance_manager       READ: all accounting data for own tenant
                      WRITE: period close initiation, adjustment approval (checker role)
                      NEVER: post entries directly, bypass checker

auditor               READ: all (tenant-scoped), immutable audit trail
                      WRITE: audit notes only
                      NEVER: post entries, modify any record

platform_finance_ops  READ: cross-tenant aggregates (anonymized)
                      WRITE: CoA version management, agent config
                      NEVER: access individual tenant PII

External API          FORBIDDEN: no accounting internals exposed publicly
```

```
SECURITY RULES:
  SEC-1: Service account credentials in HashiCorp Vault. Never in code or environment vars.
  SEC-2: Every agent API call carries tenant_id in JWT claim. Missing claim ⇒ REJECT.
  SEC-3: Kafka messages signed with HMAC. Unsigned messages ⇒ DISCARD.
  SEC-4: All journal_entries carry audit_hash (SHA-256). Hash verified on every read.
  SEC-5: MinIO document access requires signed time-limited URL (max TTL: 1 hour).
  SEC-6: journal_entries table has append-only constraint enforced at PostgreSQL level.
  SEC-7: Agent never exposes financial data to user-facing APIs.
  SEC-8: MFA required for all human actions: MANUAL journal, period close, payout approval.
```

---

## 1️⃣5️⃣ OBSERVABILITY & ALERTING (MANDATORY)

### Prometheus Metrics (All Required)

```prometheus
# Counters
accounting_sync_events_total{tenant_id, event_type, status}
accounting_journal_entries_total{tenant_id, entry_type, domain_tag}
accounting_reconciliation_runs_total{tenant_id, layer, status}
accounting_anomalies_detected_total{tenant_id, signal_type, severity}
accounting_period_closes_total{tenant_id, period_id, status}

# Gauges
accounting_deferred_revenue_balance{tenant_id, domain_tag}
accounting_ar_balance{tenant_id, account_code}
accounting_gst_payable_balance{tenant_id, jurisdiction}
accounting_unreconciled_invoices{tenant_id}
accounting_open_payout_liability{tenant_id}

# Histograms
accounting_sync_latency_seconds{event_type}          # SLO: p99 < 2s
accounting_reconciliation_duration_seconds{layer}    # SLO: p99 < 300s
```

### Alert Rules

```yaml
- alert: reconciliation_failure
  condition: reconciliation_runs.status = FAIL AND layer IN [INVOICE_LEDGER, GST, REVENUE]
  severity: CRITICAL
  action: page finance_manager + platform_finance_ops

- alert: period_close_blocked
  condition: period.close.failed emitted
  severity: CRITICAL
  action: page finance_manager + auditor

- alert: high_anomaly_rate
  condition: accounting_anomalies_detected_total[1h] > 5 for same tenant
  severity: HIGH
  action: alert finance_manager + compliance team

- alert: balance_sheet_imbalance
  condition: BS feed assets != liabilities_plus_equity
  severity: CRITICAL
  action: HALT financial_statement_feed + page platform_finance_ops

- alert: payout_approval_violation
  condition: payout approved by single actor
  severity: CRITICAL
  action: DENY payout + alert compliance
```

---

## 1️⃣6️⃣ DATA RETENTION & AUDIT DEFENSE (ASC-T, TFC-R — LOCKED)

```
RETENTION POLICY:
  journal_entries         7 years  (India CA Act / IT Act statutory minimum)
  reconciliation_runs     7 years
  invoices (MinIO)        7 years
  payout_receipts         7 years
  audit_hash_log          Permanent (immutable, append-only)
  tax_return_refs         7 years + assessment period extension

RULES:
  RET-1: No automatic deletion of any accounting record. All deletions require legal_hold check.
  RET-2: Legal hold overrides ALL TTL policies without exception.
  RET-3: Audit access is read-only, always. No write access for auditor role.
  RET-4: Jurisdiction-specific retention overrides apply (highest retention period wins).
  RET-5: Archive to MinIO cold storage after 2 years active (lifecycle policy).
  RET-6: Retrieval SLA: any record within 7-year window retrievable within 48 hours.
```

---

## 1️⃣7️⃣ FRAUD & MISSTATEMENT CONTROLS (ASC-S — LOCKED)

| Control | Mechanism | Response |
|---|---|---|
| Benford's Law deviation | First-digit chi-square > 2σ | anomaly.detected MEDIUM |
| Round-number bias | >15% entries ending .00 (non-subscription) | anomaly.detected MEDIUM |
| Late-period spikes | >40% revenue in last 3 days of period | anomaly.detected HIGH |
| Rapid reversal | Same account DR+CR within 24h without event basis | anomaly.detected HIGH |
| Duplicate source_ref | Two journal entries with same source_ref_id | BLOCK second entry + CRITICAL |
| Payout inflation | payout_amount > earned_balance | BLOCK payout + CRITICAL |
| Single-actor approval | MANUAL entry without two distinct approvers | DENY + CRITICAL alert |
| Commission abuse | Commission rate > configured plan rate | BLOCK + HIGH alert |
| Mentor collusion billing | Payout patterns matching collusion signals | HOLD + CRITICAL escalation |

---

## 1️⃣8️⃣ TESTING REQUIREMENTS (LOCKED)

Antigravity MUST generate the following test coverage. Incomplete coverage = INVALID BUILD.

```
UNIT TESTS (per event handler):
  ✔ GST calculation: intra-state, inter-state, export, exempt, RCM
  ✔ Deferred revenue amortization: monthly, annual, upgrade proration, cancel
  ✔ Payout dual-approval: single actor ⇒ DENY
  ✔ Period lock: post-lock entry ⇒ REJECT
  ✔ FX revaluation: rate application, gain/loss entry generation
  ✔ Reconciliation: PASS case, FAIL case, PARTIAL case
  ✔ Audit hash: SHA-256 consistency, tamper detection
  ✔ Tenant isolation: cross-tenant query ⇒ REJECT
  ✔ CoA validation: invalid account code ⇒ REJECT

INTEGRATION TESTS:
  ✔ Full invoice → payment → revenue recognition cycle (Stripe mock)
  ✔ Full subscription → deferred revenue → amortization cycle
  ✔ Refund → credit note → revenue reversal → GST reversal
  ✔ Payout → TDS calculation → disbursement → reconciliation PASS
  ✔ Period close: all 6 layers PASS → lock confirmed
  ✔ Period close: 1 layer FAIL → close BLOCKED
  ✔ Kafka consumer idempotency: duplicate event → single journal entry only

DAG TESTS (Airflow):
  ✔ Each DAG tenant-isolated (no side effects on other tenants)
  ✔ DAG failure does NOT block platform operations
  ✔ DAG retry on transient failure (max 3, exponential backoff)

COMPLIANCE TESTS:
  ✔ ASC 606 five-step enforced for all revenue event types
  ✔ GST place of supply: intra vs inter vs export
  ✔ Double-entry balance: all journal entries sum to zero
  ✔ Maker-checker: no MANUAL entry posts without two distinct approvers
  ✔ Audit trail integrity: audit_hash matches on read
```

---

## 1️⃣9️⃣ FAILURE MODES & ESCALATION MATRIX

| Failure | Agent Response | Escalation |
|---|---|---|
| Kafka event parse error | Dead letter queue | platform_finance_ops alert |
| billing_service unavailable | Retry 3x (exp backoff) → HOLD | finance_manager alert after 15 min |
| ledger_service unavailable | Retry → HOLD pending queue | finance_manager alert after 15 min |
| Journal entry DB failure | ROLLBACK + RETRY | platform_finance_ops page |
| CoA account not found | BLOCK ENTRY | finance_manager immediate alert |
| Tenant config missing | DISABLE accounting functions | platform_finance_ops + tenant admin |
| GST rate table outdated | FLAG all new tax entries | platform_finance_ops alert |
| Reconciliation FAIL (critical layer) | BLOCK period close | finance_manager + auditor page |
| Balance sheet imbalance | HALT feed generation | platform_finance_ops CRITICAL page |
| Anomaly detected HIGH | HOLD related account | finance_manager + compliance page |
| Period close failed | Emit period.close.failed | finance_manager + auditor page |
| Human approval timeout > 48h | Escalate to senior finance | Senior finance escalation |

---

## 2️⃣0️⃣ AGENT SEAL

```
╔══════════════════════════════════════════════════════════════╗
║  🔒 ACCOUNTING_SYNC_AGENT SEAL — v1.0                        ║
║  Ecoskiller Enterprise SaaS — Antigravity Injection Block     ║
╠══════════════════════════════════════════════════════════════╣
║                                                              ║
║  ACCOUNTING_SYNC_AGENT_ENABLED                               ║
║  GAAP / IFRS FRAMEWORK ENFORCED (per entity declaration)     ║
║  CHART OF ACCOUNTS VERSION-CONTROLLED AND LOCKED             ║
║  DOUBLE-ENTRY JOURNAL GOVERNANCE ACTIVE                      ║
║  KAFKA EVENT-DRIVEN SYNC — ALL 16 TOPICS CONSUMED            ║
║  KAFKA PRODUCER — ALL 6 TOPICS EMITTED                       ║
║  AIRFLOW SCHEDULED RECONCILIATION — ALL 13 DAGs MANDATORY    ║
║  REVENUE RECOGNITION ASC 606 / IFRS 15 FIVE-STEP ENFORCED    ║
║  DEFERRED REVENUE AMORTIZATION AUTOMATED (MONTHLY)           ║
║  GST / CGST / SGST / IGST RECONCILIATION ACTIVE              ║
║  INPUT TAX CREDIT (ITC) GOVERNANCE LOCKED                    ║
║  TDS PAYABLE COMPUTATION ACTIVE (INDIA — SECTION 194J)       ║
║  PAYOUT DUAL-APPROVAL MANDATORY (MAKER-CHECKER)              ║
║  PERIOD CLOSE PROTOCOL WITH PRE-CLOSE CHECKLIST LOCKED       ║
║  POST-CLOSE IMMUTABILITY DB-CONSTRAINT ENFORCED              ║
║  FINANCIAL STATEMENT FEED (BS / IS / CFS / SEGMENT / NOTES)  ║
║  FRAUD & ANOMALY DETECTION ACTIVE (5 SIGNALS)                ║
║  SIX-LAYER RECONCILIATION ENGINE ACTIVE                      ║
║  MULTI-TENANT + ENTITY + DOMAIN ISOLATION HARD-LOCKED        ║
║  AUDIT HASH INTEGRITY (SHA-256) ON ALL JOURNAL ENTRIES       ║
║  7-YEAR DATA RETENTION WITH LEGAL HOLD OVERRIDE              ║
║  HASHICORP VAULT SECRETS — NO HARDCODED CREDENTIALS          ║
║  OBSERVABILITY: PROMETHEUS + ALERTMANAGER + OPENTELEMETRY    ║
║  TESTING: UNIT + INTEGRATION + DAG + COMPLIANCE REQUIRED     ║
║  MINIO STORAGE: invoices/ entries/ reports/ audit-archives/  ║
║                                                              ║
╠══════════════════════════════════════════════════════════════╣
║  ⚠️  ANTIGRAVITY MUST NOT BYPASS, REINTERPRET,               ║
║      SIMPLIFY, OR CREATIVELY EXTEND ANY RULE IN THIS AGENT. ║
║      THIS PROMPT IS FINAL. LOCKED. IMMUTABLE.                ║
╚══════════════════════════════════════════════════════════════╝
```

---

*End of `ACCOUNTING_SYNC_AGENT.md`*
*Ecoskiller Enterprise Platform | Antigravity Execution Target | v1.0.0 | SEALED & LOCKED*
