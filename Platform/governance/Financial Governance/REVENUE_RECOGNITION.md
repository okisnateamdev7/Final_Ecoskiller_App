# 🔒 SEALED & LOCKED MODULE
# REVENUE_RECOGNITION.md
# SERVICE: ANTIGRAVITY — BILLING, MONETIZATION & REVENUE ENGINE
# PARENT PLATFORM: ECOSKILLER (ENTERPRISE MULTI-TENANT SAAS)

---

```
🔐 EXECUTION MODE
EXECUTION_MODE       = LOCKED
MUTATION_POLICY      = ADD_ONLY
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING   = FORBIDDEN
DEFAULT_BEHAVIOR     = DENY
FAILURE_MODE         = STOP_EXECUTION
OVERRIDE_POLICY      = REQUIRES_WRITTEN_APPROVAL + VERSION_BUMP
```

---

## 1️⃣ MODULE IDENTITY (NON-NEGOTIABLE)

```
MODULE_NAME          = ANTIGRAVITY
MODULE_TYPE          = BILLING + REVENUE RECOGNITION ENGINE
PARENT_PLATFORM      = ECOSKILLER
SCOPE                = MULTI-TENANT | MULTI-CURRENCY | MULTI-MODEL
COMPLIANCE_STANDARDS = ASC 606 | IFRS 15 | GST/HST | MCA (India)
CURRENCY_PRIMARY     = INR
CURRENCY_SECONDARY   = USD | EUR | GBP | AED | SGD
TAX_REGIMES          = GST (India) | VAT (EU) | Sales Tax (US) | Custom per tenant
AUDIT_GRADE          = ENTERPRISE
```

---

## 2️⃣ REVENUE MODEL TAXONOMY (LOCKED)

All revenue streams are classified under exactly one of the following categories.  
No uncategorized revenue is permitted in any billing cycle.

### A. SUBSCRIPTION REVENUE (Recurring)

| Tier | Target User Group | Billing Cycle | Recognition Method |
|------|------------------|---------------|--------------------|
| FREE | Students, Individuals | N/A | No revenue |
| STARTER | Students, Job Seekers | Monthly / Annual | Straight-line over period |
| PRO | Students, Trainers, Mentors | Monthly / Annual | Straight-line over period |
| TEAM | SMEs, Recruiters, HR Teams | Monthly / Annual | Straight-line over period |
| INSTITUTE | Colleges, Universities, Schools | Annual / 3-Year | Straight-line over contract term |
| ENTERPRISE | Corporates (L1–L7), Large Recruiters | Annual / Custom | Milestone + Straight-line hybrid |
| PLATFORM_GOD | Antigravity Internal (Admin) | N/A | Cost center, not revenue |

**Recognition Rules:**
- Monthly subscriptions: Recognized fully in the month of service delivery.
- Annual subscriptions: Deferred to Unearned Revenue on billing date; recognized 1/12 per month.
- Multi-year contracts: Performance Obligation schedule applied per ASC 606 Step 5.
- Cancellations with refund: Reverse pro-rata on remaining unused period.
- Cancellations without refund: Revenue recognized up to cancellation date only.

---

### B. TRANSACTION-BASED REVENUE (Usage)

| Revenue Event | Actor | Unit Price Basis | Recognition Trigger |
|--------------|-------|------------------|---------------------|
| Job Posting Credit | Enterprise / SME / Recruiter | Per posting | On successful job publish |
| Application Credits | Student / Job Seeker | Per batch | On application submission batch |
| Profile Boost | All users | Per boost unit | On boost activation (immediate) |
| Dojo Match Token | Student | Per session | On Dojo room entry confirmation |
| Evaluator Session Fee | Evaluator | Per evaluation completed | On evaluation submission + approval |
| Project Submission Credit | Student | Per project submission | On project submit |
| AI Analysis Credit | All users | Per AI call unit | On API response delivery |
| Background Verification Credit | Recruiter / Enterprise | Per verification run | On report delivery |

**Recognition Rules:**
- Credits are sold in bundles: Revenue from bundle = deferred; recognized per credit consumed.
- Unused credits at expiry: Revenue recognized at expiry date (lapse-based).
- Refundable credits: Remain in contract liability until consumed or expired.

---

### C. MARKETPLACE REVENUE (Platform Intermediary)

| Marketplace Event | Platform Take Rate | Recognition Trigger |
|-------------------|--------------------|---------------------|
| Trainer Course Sale (Student → Trainer) | 15% net commission | On enrollment confirmation + 48hr refund window expiry |
| Mentorship Session Booking | 20% net commission | On session completion confirmed by both parties |
| Project Outsource (Company → Student) | 10% net commission | On project milestone acceptance |
| Recruiter Verified Hire Bonus | 5% of first-month CTC (capped) | On verified joining date + 30-day retention |
| Resume Writing / Review Service | 25% net commission | On service delivery confirmation |

**Recognition Rules:**
- Platform acts as AGENT (not principal) in all marketplace transactions.
- Gross transaction value is NOT recorded as revenue — only net commission is recognized.
- Escrow release triggers revenue event; funds held in escrow are NOT revenue.
- Disputed transactions: Revenue deferred until dispute resolution finalized.

---

### D. INSTITUTIONAL LICENSING REVENUE (B2B Contract)

| Contract Type | Billing Structure | Recognition Method |
|--------------|-------------------|--------------------|
| Institute ERP License | Annual fixed fee | Straight-line over 12 months |
| Corporate Hiring ERP | Annual + per-hire variable | Fixed = straight-line; Variable = on-hire event |
| White-label Platform License | Upfront + Monthly | Upfront = amortized over contract; Monthly = in-month |
| API Integration License | Per-call or Flat | Per-call = on call delivery; Flat = straight-line |
| Compliance + Audit ERP Module | Annual + overage | Fixed = straight-line; Overage = in-period |

**Recognition Rules:**
- Multi-element arrangements: allocate transaction price using Standalone Selling Price (SSP) per ASC 606 Step 4.
- Setup/implementation fees: Treated as cost-to-fulfill; not standalone performance obligations; amortize over expected customer life.
- Contract modifications: Apply modification accounting (new contract vs. cumulative catch-up per facts and circumstances).

---

### E. PREMIUM FEATURE REVENUE (Incremental Upgrades)

| Feature | Target | Recognition |
|---------|--------|-------------|
| Advanced Analytics Unlock | All tiers | Immediate on activation; straight-line if subscription-linked |
| Anti-Cheat Premium Enforcement | Institutes / Enterprises | Per event OR monthly add-on |
| Priority Job Match AI | PRO and above | Monthly add-on, straight-line |
| Verified Badge / Trust Seal | Trainers, SMEs | Annual flat fee, straight-line |
| Custom Domain (White-label) | Institute / Enterprise | Annual flat fee, straight-line |
| Offline Mode Access | Students | Monthly add-on, straight-line |
| Parent Portal Premium | Parents | Monthly add-on, straight-line |

---

### F. ADVERTISING & SPONSORED CONTENT REVENUE

```
ADVERTISING_POLICY   = STRICTLY PROHIBITED in core platform UX
SPONSORED_CONTENT    = ALLOWED only in defined zones (Job Spotlight, Course Spotlight)
DISPLAY_ADS          = FORBIDDEN
DATA_SELLING         = PERMANENTLY FORBIDDEN (violates User Data Covenant)
```

| Sponsorship Type | Billing | Recognition |
|-----------------|---------|-------------|
| Job Spotlight (Recruiter-sponsored) | CPM / CPC | On impression/click delivery |
| Course Spotlight (Trainer-sponsored) | Flat weekly/monthly | Straight-line over campaign period |
| Event / Webinar Sponsorship | Flat per event | On event delivery date |
| Institution Featured Listing | Monthly flat | Straight-line over display period |

---

### G. COMPLIANCE & CERTIFICATION REVENUE

| Item | Billed To | Recognition |
|------|-----------|-------------|
| Student Certificate Issuance | Student | On certificate generation + delivery |
| Institute Accreditation Fee | Institute | On accreditation granted (point-in-time) |
| Evaluator Certification Program | Evaluator | On certification issued |
| Corporate Compliance Audit Report | Enterprise | On report delivery + sign-off |

---

## 3️⃣ FIVE-STEP ASC 606 / IFRS 15 FRAMEWORK (MANDATORY APPLIED TO ALL CONTRACTS)

```
STEP 1 = IDENTIFY THE CONTRACT
STEP 2 = IDENTIFY PERFORMANCE OBLIGATIONS
STEP 3 = DETERMINE TRANSACTION PRICE
STEP 4 = ALLOCATE TRANSACTION PRICE TO PERFORMANCE OBLIGATIONS
STEP 5 = RECOGNIZE REVENUE WHEN (OR AS) PERFORMANCE OBLIGATION SATISFIED
```

### Step 1 — Contract Identification Rules

- A valid contract must exist between Ecoskiller and customer (digital acceptance of ToS = contract).
- Collection is probable (payment method verified before plan activation).
- Free tier users: No contract for revenue purposes until first paid event.
- Trial periods: No revenue until trial conversion confirmed.

### Step 2 — Performance Obligations (POs)

Each of the following constitutes a distinct PO:

```
PO_01 = Platform Access (SaaS Subscription)
PO_02 = Job Portal Engine Access
PO_03 = Skill Development Engine Access
PO_04 = Project Execution Engine Access
PO_05 = Dojo (Group Discussion) Engine Access
PO_06 = ERP Module Access
PO_07 = AI Credits (batch)
PO_08 = Marketplace Transaction Facilitation
PO_09 = Certificate / Badge Issuance
PO_10 = Onboarding & Implementation Services
PO_11 = Dedicated Support SLA
PO_12 = Custom Development (if contracted)
```

- POs bundled in a single contract MUST be priced and tracked independently.
- If a PO cannot be priced independently, it is combined with its closest distinct PO.

### Step 3 — Transaction Price Determination

```
VARIABLE_CONSIDERATION  = ALLOWED (usage-based, commissions, overages)
CONSTRAINT_POLICY       = Include only amounts highly probable of no significant reversal
REFUND_OBLIGATION       = Reduce transaction price by expected refund amount
SIGNIFICANT_FINANCING   = Apply only if payment > 12 months before/after delivery
NONCASH_CONSIDERATION   = Measure at fair value (e.g., barter promotions)
```

### Step 4 — Allocation to POs

- Use Standalone Selling Price (SSP) as primary basis.
- SSP methods (in order of preference): Observable price → Adjusted market assessment → Expected cost plus margin → Residual (only if highly variable or uncertain).
- Discounts: Allocate proportionally unless discount applies specifically to a PO subset with observable evidence.

### Step 5 — Recognition Timing

```
OVER_TIME_CRITERIA      = Customer simultaneously receives and consumes benefit (SaaS = over time)
POINT_IN_TIME_CRITERIA  = Control transfers at a specific moment (certificates, one-time reports)
OUTPUT_METHOD           = Preferred for over-time POs (time elapsed, units delivered)
INPUT_METHOD            = Permitted only when output method not feasible
```

---

## 4️⃣ DEFERRED REVENUE & CONTRACT LIABILITY RULES

```
DEFERRED_REVENUE_ACCOUNT   = Unearned Revenue (Liability)
RELEASE_TRIGGER             = Service delivery per recognition schedule
MINIMUM_DEFERRAL_UNIT       = 1 day
MAXIMUM_DEFERRAL_WINDOW     = 36 months (3-year contracts max)
```

### Balance Sheet Classification

| Duration | Classification |
|----------|---------------|
| Recognized within 12 months | Current Liability |
| Recognized beyond 12 months | Non-Current Liability |

### Deferred Revenue Waterfall (Monthly Close)

```
1. Opening Unearned Revenue Balance
2. + New Billings in Period
3. - Revenue Recognized in Period (earned)
4. - Refunds / Credits Issued
5. ± Contract Modifications
6. = Closing Unearned Revenue Balance
```

---

## 5️⃣ CONTRACT ASSET & RECEIVABLES

```
CONTRACT_ASSET       = Revenue earned but not yet billed (milestone-based contracts)
RECEIVABLE           = Unconditional right to consideration (billed, not yet collected)
TRANSITION           = Contract Asset → Receivable on invoice issuance
IMPAIRMENT           = Apply IFRS 9 / ASC 310 expected credit loss model
BAD_DEBT_RESERVE     = Minimum 2% of outstanding receivables; adjust quarterly
```

---

## 6️⃣ REFUND & CREDIT NOTE POLICY (LOCKED)

| Scenario | Policy |
|----------|--------|
| Annual plan cancelled within 7 days | Full refund; reverse all recognized revenue |
| Annual plan cancelled after 7 days | Pro-rata refund on unused months; partial revenue reversal |
| Monthly plan cancelled mid-month | No refund for current month; recognize full month revenue |
| Credit purchase — unused at expiry | Revenue recognized at expiry; no refund |
| Credit purchase — platform error | Full refund + credit; revenue reversed |
| Marketplace dispute resolved for buyer | Platform commission reversed proportionally |
| Institutional contract early termination | Per termination clause in MSA; revenue up to termination date only |

**Credit Note Issuance:**
- Issued within 5 business days of confirmed refund trigger.
- GST credit note issued separately per GST Act requirements (India).
- All credit notes logged in `billing.credit_notes` with original invoice reference.

---

## 7️⃣ MULTI-TENANT REVENUE ISOLATION (HARD LOCK)

```
TENANT_REVENUE_ISOLATION  = MANDATORY
CROSS_TENANT_REVENUE_MIX  = PERMANENTLY FORBIDDEN
REVENUE_LEDGER            = Separate per tenant_id
PLATFORM_REVENUE_LEDGER   = Separate from all tenant ledgers
CONSOLIDATION             = Platform-level only; never expose cross-tenant data
```

- Tenant admins see ONLY their own billing, revenue, and subscription data.
- Platform admins see consolidated + per-tenant view with explicit access grant.
- Revenue reports are tenant-scoped by default; cross-tenant requires SUPERADMIN role.

---

## 8️⃣ TAX HANDLING (INDIA PRIMARY)

```
GST_REGISTERED              = YES
GST_MODEL                   = Forward Charge (platform charges GST to customer)
REVERSE_CHARGE_APPLICABLE   = Only for specific B2B cross-border services
HSN_SAC_CODE                = SAC 998314 (Online Information and Database Access Services)
IGST_CGST_SGST_SPLIT        = Automatic based on customer GSTIN state vs. platform state
```

### Tax Event Table

| Revenue Type | Tax Treatment |
|-------------|---------------|
| Student Subscription (B2C) | 18% GST (IGST for inter-state) |
| Institute License (B2B registered) | 18% GST with ITC claimable by buyer |
| Enterprise Contract (B2B) | 18% GST with ITC claimable |
| Export of Service (Foreign customer) | Zero-rated (LUT filed) |
| Marketplace Commission (Agent) | 18% GST on commission only |
| Certificate Issuance | 18% GST |

- GST invoices generated automatically on billing event.
- E-invoicing (IRP) mandatory for B2B invoices above INR 5 Lakhs per transaction.
- GSTR-1 auto-populated; GSTR-3B reconciliation monthly.

---

## 9️⃣ REVENUE RECOGNITION TRIGGERS — EVENT TABLE (CANONICAL)

All revenue events flow through Kafka. Each event MUST map to exactly one trigger below.

| Kafka Topic | Event | Revenue Action |
|-------------|-------|---------------|
| `billing.subscription.activated` | New subscription started | Deferred revenue created |
| `billing.subscription.renewed` | Subscription auto-renewed | New deferral period starts |
| `billing.subscription.cancelled` | Cancellation confirmed | Pro-rata recognition; remaining deferred released or reversed |
| `billing.credit.purchased` | Credit bundle purchased | Deferred until consumption |
| `billing.credit.consumed` | Credit unit used | Recognize unit revenue |
| `billing.credit.expired` | Credits lapsed | Recognize remaining deferred |
| `marketplace.transaction.escrowed` | Funds placed in escrow | No revenue event |
| `marketplace.transaction.completed` | Service/job confirmed delivered | Platform commission recognized |
| `marketplace.transaction.disputed` | Dispute opened | Revenue deferred |
| `marketplace.transaction.resolved` | Dispute resolved | Recognize or reverse per resolution |
| `job.posting.published` | Job goes live | Job credit revenue recognized |
| `certificate.issued` | Certificate generated | Certificate fee recognized |
| `dojo.session.entered` | User enters Dojo room | Dojo token recognized |
| `evaluation.completed` | Evaluator submits result | Evaluator session fee recognized |
| `contract.milestone.accepted` | Enterprise milestone signed off | Milestone revenue unlocked |
| `refund.approved` | Refund approved by billing admin | Revenue reversal + credit note |

---

## 🔟 REPORTING & ANALYTICS (MANDATORY OUTPUTS)

```
REPORTING_FREQUENCY       = Daily (operational) | Monthly (financial close) | Quarterly (board)
REPORTING_CURRENCY        = INR primary; USD secondary for enterprise contracts
REPORTING_TOOL            = Grafana (operational) + ERP module (financial)
DATA_SOURCE               = PostgreSQL billing schema + Kafka event log
```

### Required Revenue Reports

| Report | Frequency | Audience |
|--------|-----------|----------|
| MRR (Monthly Recurring Revenue) | Daily refresh | CEO, CFO, Finance |
| ARR (Annual Recurring Revenue) | Weekly refresh | CEO, Board |
| Deferred Revenue Waterfall | Monthly close | CFO, Auditors |
| Revenue by Segment (User Group) | Monthly | Product, Finance |
| Revenue by Module (Job / Skill / Project / Dojo / ERP) | Monthly | Product |
| Churn Revenue Lost | Monthly | CEO, Product |
| Net Revenue Retention (NRR) | Monthly | CEO, Investors |
| Gross Revenue Retention (GRR) | Monthly | CFO |
| Contract Liability Aging | Monthly | Finance, Auditors |
| Tax Collected vs. Remitted | Monthly | Finance, Tax |
| Marketplace GMV vs. Net Revenue | Monthly | Product, Finance |
| Customer Lifetime Value (CLTV) | Quarterly | Growth, Finance |
| Payback Period by Segment | Quarterly | CFO, Investors |

---

## 1️⃣1️⃣ KEY METRICS DEFINITIONS (LOCKED)

```
MRR  = Sum of all normalized monthly subscription revenue (excludes one-time fees)
ARR  = MRR × 12
NRR  = (Opening MRR + Expansion MRR - Contraction MRR - Churned MRR) / Opening MRR × 100
GRR  = (Opening MRR - Contraction MRR - Churned MRR) / Opening MRR × 100
GMV  = Gross Merchandise Value (total marketplace transaction volume, platform + seller)
NET_REVENUE = GMV × Platform Take Rate (commission only)
CLTV = ARPU × Gross Margin % × Average Customer Lifespan (months)
CAC  = Total Sales & Marketing Spend / New Customers Acquired in Period
```

---

## 1️⃣2️⃣ DATABASE SCHEMA — ANTIGRAVITY BILLING TABLES (CANONICAL)

```sql
-- Core Subscription
subscriptions (
  id UUID PK,
  tenant_id UUID FK,
  user_id UUID FK,
  plan_id UUID FK,
  status ENUM(active, cancelled, paused, trial, expired),
  billing_cycle ENUM(monthly, annual, multi_year, custom),
  start_date DATE,
  end_date DATE,
  trial_end_date DATE,
  mrr_contribution DECIMAL(15,4),
  arr_contribution DECIMAL(15,4),
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
)

-- Invoices
invoices (
  id UUID PK,
  tenant_id UUID FK,
  customer_id UUID FK,
  subscription_id UUID FK NULLABLE,
  invoice_number VARCHAR UNIQUE,
  invoice_date DATE,
  due_date DATE,
  currency CHAR(3),
  subtotal DECIMAL(15,4),
  tax_amount DECIMAL(15,4),
  total_amount DECIMAL(15,4),
  status ENUM(draft, issued, paid, overdue, void, credited),
  payment_method_id UUID FK NULLABLE,
  irp_ack_number VARCHAR NULLABLE,
  created_at TIMESTAMPTZ
)

-- Revenue Recognition Schedule
revenue_recognition_schedule (
  id UUID PK,
  invoice_id UUID FK,
  performance_obligation_id UUID FK,
  total_allocated_amount DECIMAL(15,4),
  recognition_start_date DATE,
  recognition_end_date DATE,
  recognition_method ENUM(straight_line, point_in_time, milestone, usage),
  recognized_to_date DECIMAL(15,4),
  deferred_balance DECIMAL(15,4),
  last_run_date DATE,
  created_at TIMESTAMPTZ
)

-- Deferred Revenue Ledger
deferred_revenue_ledger (
  id UUID PK,
  tenant_id UUID FK,
  source_type ENUM(subscription, credit_bundle, marketplace, license, certificate),
  source_id UUID,
  opening_balance DECIMAL(15,4),
  period_billings DECIMAL(15,4),
  period_recognized DECIMAL(15,4),
  period_refunds DECIMAL(15,4),
  closing_balance DECIMAL(15,4),
  period_start DATE,
  period_end DATE,
  created_at TIMESTAMPTZ
)

-- Credit Bundles
credit_bundles (
  id UUID PK,
  tenant_id UUID FK,
  user_id UUID FK,
  credit_type ENUM(job_posting, application, dojo_token, ai_call, profile_boost, evaluation),
  total_credits INTEGER,
  consumed_credits INTEGER,
  expired_credits INTEGER,
  unit_price DECIMAL(15,4),
  total_paid DECIMAL(15,4),
  deferred_amount DECIMAL(15,4),
  recognized_amount DECIMAL(15,4),
  expiry_date DATE,
  created_at TIMESTAMPTZ
)

-- Marketplace Transactions
marketplace_transactions (
  id UUID PK,
  tenant_id UUID FK,
  transaction_type ENUM(course_sale, mentorship, project, hire_bonus, resume_review),
  buyer_id UUID FK,
  seller_id UUID FK,
  gross_amount DECIMAL(15,4),
  platform_commission_rate DECIMAL(6,4),
  platform_commission_amount DECIMAL(15,4),
  escrow_status ENUM(held, released, refunded, disputed),
  revenue_recognized BOOLEAN DEFAULT FALSE,
  recognition_date DATE NULLABLE,
  dispute_id UUID NULLABLE,
  created_at TIMESTAMPTZ
)

-- Contract Liabilities
contract_liabilities (
  id UUID PK,
  tenant_id UUID FK,
  customer_id UUID FK,
  contract_id UUID FK,
  liability_type ENUM(unearned_subscription, unearned_credits, customer_deposit, refund_obligation),
  amount DECIMAL(15,4),
  current_portion DECIMAL(15,4),
  non_current_portion DECIMAL(15,4),
  maturity_date DATE,
  created_at TIMESTAMPTZ,
  updated_at TIMESTAMPTZ
)

-- Credit Notes
credit_notes (
  id UUID PK,
  tenant_id UUID FK,
  original_invoice_id UUID FK,
  credit_note_number VARCHAR UNIQUE,
  reason ENUM(cancellation, refund, error, goodwill, dispute_resolution),
  amount DECIMAL(15,4),
  tax_adjustment DECIMAL(15,4),
  issued_date DATE,
  status ENUM(issued, applied, void),
  created_at TIMESTAMPTZ
)

-- Tax Ledger
tax_ledger (
  id UUID PK,
  invoice_id UUID FK,
  tax_type ENUM(IGST, CGST, SGST, VAT, SALES_TAX, CUSTOM),
  hsn_sac_code VARCHAR,
  taxable_value DECIMAL(15,4),
  tax_rate DECIMAL(6,4),
  tax_amount DECIMAL(15,4),
  customer_gstin VARCHAR NULLABLE,
  place_of_supply_state_code CHAR(2) NULLABLE,
  irp_status ENUM(pending, submitted, acknowledged, error) DEFAULT pending,
  gstr1_period VARCHAR NULLABLE,
  created_at TIMESTAMPTZ
)
```

---

## 1️⃣3️⃣ MICROSERVICE INTEGRATION MAP

```
Antigravity (Billing Engine)
  ├── Auth Service          → Subscription tier enforcement, feature gates
  ├── User Service          → Customer identity, billing address, GSTIN
  ├── Job Portal Service    → Job posting credit consumption events
  ├── Skill Engine Service  → Course enrollment, trainer payouts
  ├── Project Service       → Project submission credits, milestone events
  ├── Dojo Engine Service   → Dojo token consumption events
  ├── ERP Service           → Institutional contract lifecycle
  ├── Analytics Service     → MRR/ARR feeds, cohort billing data
  ├── Notification Service  → Invoice delivery, payment reminders, dunning
  ├── Admin Governance      → Manual revenue adjustments (audit-logged)
  └── Integration Hub       → Payment gateways, GST APIs, IRP (e-invoicing)
```

---

## 1️⃣4️⃣ PAYMENT GATEWAY INTEGRATION (LOCKED GATEWAY LIST)

```
PRIMARY_GATEWAY_IN    = Razorpay (INR, UPI, Cards, NetBanking, EMI)
PRIMARY_GATEWAY_INTL  = Stripe (USD, EUR, GBP, Cards)
BACKUP_GATEWAY        = PayU (India fallback)
CRYPTO               = PERMANENTLY FORBIDDEN
BNPL_PROVIDERS       = FORBIDDEN (regulatory risk in education sector)
AUTO_RECONCILIATION  = MANDATORY (daily bank reconciliation via webhook + bank statement match)
```

### Payment Failure & Dunning Sequence

```
Day 0   → Payment failed → Retry immediately (1x)
Day 1   → Retry + Email notification
Day 3   → Retry + Email + In-app alert
Day 7   → Retry + Email + SMS + In-app degraded mode warning
Day 14  → Final retry + Email + SMS + Subscription grace period ends
Day 15  → Subscription paused (data retained; access revoked)
Day 30  → Subscription terminated (data retained per data retention policy)
Day 90  → Write-off to bad debt; revenue reversed if previously recognized
```

---

## 1️⃣5️⃣ AUDIT & COMPLIANCE RULES (IMMUTABLE)

```
REVENUE_ADJUSTMENT_POLICY     = Requires CFO approval + audit log entry
RETROACTIVE_RECOGNITION       = FORBIDDEN without restatement procedure
REVENUE_SMOOTHING             = FORBIDDEN (channel stuffing, pull-forward: PROHIBITED)
RELATED_PARTY_TRANSACTIONS    = Disclosed separately; arm's-length pricing enforced
DATA_RETENTION_BILLING        = 7 years (India: Companies Act 2013, Income Tax Act)
AUDIT_TRAIL                   = Immutable append-only log for all billing events
SOC2_TYPE2_TARGET             = YES (within 18 months of launch)
ISO_27001_TARGET              = YES (concurrent with SOC2)
```

### Immutable Audit Log Entry (per billing event)

```json
{
  "event_id": "uuid",
  "event_type": "revenue_recognized | deferred | reversed | refunded | adjusted",
  "amount": 0.00,
  "currency": "INR",
  "tenant_id": "uuid",
  "user_id": "uuid",
  "invoice_id": "uuid",
  "recognition_date": "YYYY-MM-DD",
  "recognition_method": "straight_line | point_in_time | milestone | usage",
  "performed_by": "system | admin_user_id",
  "approval_by": "cfo_user_id | null",
  "reason_code": "string",
  "timestamp": "ISO8601",
  "immutable": true
}
```

---

## 1️⃣6️⃣ FORECASTING & COHORT RULES

```
FORECAST_HORIZON         = 12 months rolling
COHORT_DEFINITION        = Month of first paid activation
CHURN_DEFINITION         = No renewal within 30 days of subscription end date
EXPANSION_DEFINITION     = Upgrade to higher tier OR additional seat/credit purchase
CONTRACTION_DEFINITION   = Downgrade to lower tier
LOGO_CHURN              = Customer fully offboarded
REVENUE_CHURN           = MRR lost from churned + contracted customers
```

---

## 1️⃣7️⃣ PROHIBITED ACTIONS (PERMANENTLY LOCKED)

```
❌ Recognizing revenue before performance obligation is satisfied
❌ Recording gross marketplace revenue (must record net commission only)
❌ Omitting variable consideration constraints
❌ Mixing tenant revenue in consolidated ledger without proper elimination
❌ Issuing invoices without GST/tax compliance check
❌ Processing refunds without credit note issuance
❌ Revenue recognition without matching Kafka event trigger
❌ Manual database edits to billing tables without audit log
❌ Offering revenue-share arrangements without signed MSA
❌ Selling or monetizing user data in any form
❌ Backdating invoices or recognition dates
❌ Accepting cryptocurrency as payment
❌ Using BNPL for subscription payments
❌ Creative interpretation of ASC 606 / IFRS 15 to accelerate revenue
```

---

## 1️⃣8️⃣ COMPLIANCE INHERITANCE (DO NOT DUPLICATE)

This module INHERITS the following already finalized by the master Ecoskiller prompt:

- Authorization (RBAC + ABAC) — Billing role gates enforced by Auth Service
- Authentication + MFA — Required for all billing admin actions
- Session Management — Billing sessions inherit platform session policy
- Data Encryption — All billing PII encrypted at rest (AES-256) + in transit (TLS 1.3)
- Tenant Isolation — Billing data isolated per `tenant_id` hard partition
- Audit & Governance — Billing audit logs feed into the Compliance & Audit ERP module
- GDPR / DPDPA — Right to erasure applies to PII only; billing records retained per statutory minimum

---

## 1️⃣9️⃣ STAGE ALIGNMENT (FOUR-STAGE MODEL)

```
STAGE 1 (Foundation)   → Billing service bootstrapped; subscription + invoice engine live
STAGE 2 (Intelligence) → AI-driven churn prediction, CLTV forecasting, expansion signals
STAGE 3 (Ecosystem)    → Marketplace commission engine, trainer payout automation, ERP billing
STAGE 4 (Compliance)   → Full ASC 606 / IFRS 15 audit readiness, SOC2, e-invoicing (IRP)
```

---

## 2️⃣0️⃣ VERSION CONTROL

```
DOCUMENT_VERSION     = 1.0.0
STATUS               = SEALED & LOCKED
CREATED_BY           = Antigravity Revenue Architecture Team
APPROVED_BY          = [CFO_SIGN_OFF_REQUIRED_BEFORE_IMPLEMENTATION]
NEXT_REVIEW          = Quarterly OR on any pricing/model change (whichever is sooner)
CHANGE_POLICY        = ADD_ONLY; no deletion of revenue categories without restatement audit
PARENT_PROMPT        = Ecoskiller SEALED & LOCKED MASTER PROMPT v1.0
```

---

*🔒 END OF SEALED DOCUMENT — REVENUE_RECOGNITION.md — ANTIGRAVITY*  
*Unauthorized modification constitutes a compliance breach. All changes require CFO approval + version bump.*
