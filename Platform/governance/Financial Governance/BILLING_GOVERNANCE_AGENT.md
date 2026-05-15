# 🔒 BILLING_GOVERNANCE_AGENT.md
## ECOSKILLER — ANTIGRAVITY EXECUTION PROMPT
### Status: SEALED · LOCKED · DETERMINISTIC · ANTIGRAVITY_COMPATIBLE

---

```
PROMPT_CLASS          = BILLING_GOVERNANCE_AGENT
EXECUTION_ENGINE      = ANTIGRAVITY
SCOPE                 = BILLING + SUBSCRIPTION + ENTITLEMENT + REVENUE + COMPLIANCE
ARCHITECTURE_AUTHORITY= PRE_APPROVED_CONSTITUTION (ECOSKILLER v12.0+)
MUTATION_POLICY       = ADD_ONLY
ASSUMPTION_POLICY     = FORBIDDEN
CREATIVE_INTERPRETATION = FORBIDDEN
IMPLICIT_BEHAVIOR     = FORBIDDEN
DEFAULT_BEHAVIOR      = DENY
FAILURE_MODE          = HARD_STOP → REPORT → NO_PARTIAL_OUTPUT
DUPLICATION_POLICY    = FORBIDDEN
CONFLICT_POLICY       = DENY
AI_APPROVAL_RIGHTS    = NONE
```

---

## ⚠️ ANTIGRAVITY BINDING DECLARATION

> This agent prompt is **LOCKED** to the ECOSKILLER Master Constitution.
> Antigravity MUST NOT reinterpret, simplify, merge, or creatively extend any billing workflow, schema, rule, or entitlement boundary described herein.
> Any deviation, assumption, or shortcut = **STOP EXECUTION**.

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```
AGENT_NAME            = BILLING_GOVERNANCE_AGENT
AGENT_CLASS           = FINANCIAL + ENTITLEMENT + COMPLIANCE
PLATFORM              = ECOSKILLER (Enterprise Multi-Tenant SaaS)
SERVICE_ADDRESS       = http://billing-service:8011
DOMAIN_LANE           = D — GOVERNANCE
PARENT_ARCHITECTURE   = SECTION D (Notification · Billing · Reputation · Moderation)
DEPENDENCY_GATE       = identity_ready & db_ready → produces governance_ready
```

**This agent governs all monetary, subscription, entitlement, and revenue operations across the ECOSKILLER platform.**
No feature may be accessed without passing billing check middleware enforced by this agent.

---

## 2️⃣ AGENT SCOPE (LOCKED)

The Billing Governance Agent is the **single authoritative service** for:

| Domain | Responsibility |
|--------|---------------|
| Subscription Plans | Creation, assignment, upgrade, downgrade, cancellation |
| Feature Entitlement | Plan-gated feature access enforcement at API and UI level |
| Usage Metering | Dojo matches, hours, recordings, seat counts, API calls |
| Invoice Lifecycle | Generation, delivery, archival, GST/VAT compliance |
| Payment Processing | Stripe primary, provider abstraction layer |
| Refund Workflow | Request, approval, audit, disbursement |
| Commission & Revenue Split | Trainer, co-teacher, platform, referral shares |
| Coupon & Discount System | Generation, redemption, expiry, abuse detection |
| Trainer Wallet & Payout | Wallet ledger, payout requests, tax deduction |
| Economic Abuse Detection | Refund abuse, multi-account farming, billing collusion |
| Billing Ledger | Immutable append-only financial record |
| Tenant Billing Isolation | Per-tenant subscription, quota, and invoice isolation |

---

## 3️⃣ SUBSCRIPTION PLAN ENGINE (MANDATORY)

### 3.1 Plan Tiers (LOCKED)

```
FREE
  - No certification access
  - Limited Dojo room access (read-only lobby)
  - Basic job portal view
  - No analytics

BASIC
  - Limited Dojo rooms (up to 3 active/month)
  - Job portal full access
  - Basic skill tracking
  - No evaluation scoring export

PRO
  - Full Dojo rooms + evaluation + analytics
  - Certification eligible
  - Trainer market dashboard
  - Project portfolio generation
  - AI match score access

ENTERPRISE (Tenant-Level)
  - All PRO features
  - Seat-based licensing
  - Mentor license pricing
  - Custom branding
  - HRIS / LMS integrations
  - SLA-backed support
  - Custom audit export
```

### 3.2 Billing Cycles

```
MONTHLY  = calendar month, auto-renew, prorate on upgrade
ANNUAL   = 12-month commitment, upfront, discount applied
SEAT     = per-seat pricing for enterprise tenants
MENTOR_LICENSE = per-mentor-slot pricing, billed monthly
TOURNAMENT_ENTRY = per-event fee, non-refundable after match start
CERTIFICATION_FEE = one-time per certification attempt
```

### 3.3 Plan Change Rules

```
UPGRADE   → Immediate access · Prorated charge · Invoice generated
DOWNGRADE → Effective next cycle · Access maintained until period end
CANCELLATION → End of current period · No immediate access loss · Audit logged
REACTIVATION → Previous plan restored if within grace period (7 days)
TRIAL → 14-day free PRO trial · One per tenant · Credit card required
```

---

## 4️⃣ ENTITLEMENT ENFORCEMENT ENGINE (HARD LOCK)

### 4.1 Authorization Matrix

Every API endpoint MUST validate the following before execution:

```
CHECK_1: subscription_plan (is plan active?)
CHECK_2: feature_flags (is feature enabled for plan?)
CHECK_3: seat_limits (are seats available?)
CHECK_4: usage_quotas (has quota been exhausted?)
CHECK_5: entitlement_scope (does tenant/user hold rights?)
```

**Access without passing ALL 5 checks = `DENY` (403 Entitlement Denied)**

### 4.2 Plan → Feature Gate Table

| Feature | FREE | BASIC | PRO | ENTERPRISE |
|---------|------|-------|-----|------------|
| Dojo Room Access | ❌ | 3/month | Unlimited | Custom |
| Certification | ❌ | ❌ | ✅ | ✅ |
| AI Match Score | ❌ | ❌ | ✅ | ✅ |
| Analytics Export | ❌ | ❌ | ✅ | ✅ |
| Mentor Assignment | ❌ | ❌ | ✅ | ✅ |
| Portfolio Generation | ❌ | ❌ | ✅ | ✅ |
| HRIS/LMS Integration | ❌ | ❌ | ❌ | ✅ |
| Custom Branding | ❌ | ❌ | ❌ | ✅ |
| Tournament Entry | ❌ | ✅ | ✅ | ✅ |
| Trainer Revenue Tools | ❌ | ❌ | ✅ | ✅ |

### 4.3 Entitlement Middleware Rule

```
NO feature access permitted without billing check middleware.
Middleware must be applied at:
  - API Gateway layer (before service execution)
  - Service layer (secondary validation)
  - Event consumer layer (async re-validation required)

Undeclared endpoint without entitlement declaration → BLOCK DEPLOYMENT
```

---

## 5️⃣ PAYMENT PROCESSING (LOCKED)

### 5.1 Provider Architecture

```
PRIMARY_PROVIDER  = Stripe
ABSTRACTION_LAYER = REQUIRED (provider-swappable interface)
SECONDARY_READY   = Razorpay (India region fallback)
PROVIDER_INTERFACE = PaymentGatewayPort (dependency inversion)
```

### 5.2 Payment Flow (Deterministic)

```
1. User selects plan / initiates payment
2. Billing Agent creates PaymentIntent via provider abstraction
3. Idempotency key generated (UUID v4, stored before charge)
4. Payment confirmed → Webhook received from provider
5. Webhook signature verified (HMAC)
6. Event: payment.confirmed published to Kafka
7. Subscription record activated
8. Entitlement cache updated (TTL: 60s)
9. Invoice generated and stored (MinIO)
10. invoice.generated event published
11. Notification Service delivers invoice to user
```

### 5.3 Idempotency Rule

```
ALL payment operations MUST carry idempotency keys.
Duplicate charge on same key → DENY (return cached result).
Idempotency keys stored: 24 hours minimum.
```

### 5.4 Webhook Security

```
Stripe webhook → HMAC-SHA256 signature validation mandatory
Unverified webhook → REJECT + LOG security alert
Replay attack window → 300 seconds tolerance maximum
Failed webhook → Dead Letter Queue → retry 3x with exponential backoff
```

---

## 6️⃣ INVOICE ENGINE (MANDATORY)

### 6.1 Invoice Requirements

Every invoice MUST contain:

```
invoice_id         (UUID, globally unique)
tenant_id          (isolated per tenant)
user_id
plan_name
billing_period_start
billing_period_end
line_items[]       (description, quantity, unit_price, subtotal)
discount_applied
tax_type           (GST/VAT)
tax_rate
tax_amount
total_amount
currency
payment_method_last4
payment_provider
invoice_status     (DRAFT | ISSUED | PAID | VOID | DISPUTED)
generated_at
pdf_url            (MinIO storage reference)
audit_hash         (SHA-256 of invoice contents)
```

### 6.2 Tax Handling

```
INDIA       → GST (CGST + SGST for intra-state / IGST for inter-state)
EU          → VAT (VAT number validation for B2B reverse charge)
US          → Sales tax (Stripe Tax integration)
OTHERS      → Configurable per jurisdiction
TAX_RULES   → Human-approved configuration (AI may draft, NOT finalize)
```

### 6.3 Invoice Storage

```
FORMAT       = PDF (generated server-side)
STORAGE      = MinIO (encrypted at rest)
RETENTION    = 7 years (financial compliance)
ACCESS       = Signed URL (time-limited, audit-logged)
```

---

## 7️⃣ REFUND WORKFLOW (GOVERNED)

### 7.1 Refund Eligibility Rules

```
SUBSCRIPTION:
  - Within 7 days of charge + no feature consumption → FULL REFUND eligible
  - After 7 days OR feature consumed → PARTIAL / DENIED (human review)

CERTIFICATION_FEE:
  - Before attempt start → FULL REFUND eligible
  - After attempt started → NON-REFUNDABLE

TOURNAMENT_ENTRY:
  - Before match round begins → REFUND eligible
  - After match started → NON-REFUNDABLE

MENTOR_LICENSE:
  - Unused in first 7 days → FULL REFUND eligible
```

### 7.2 Refund Approval Workflow

```
1. User submits refund request (reason mandatory)
2. Billing Agent validates eligibility automatically
3. Auto-eligible → system approval + disbursement within 5 business days
4. Review-required → Admin Governance Service ticket created
5. Admin reviews (48-hour SLA)
6. Approved → Stripe refund issued + event: refund.approved
7. Denied → User notified + reason documented + audit logged
8. MFA required for all admin refund approvals
```

### 7.3 Refund Abuse Detection

```
Triggers:
- > 2 refunds in 30 days → FLAG for review
- Pattern: charge + refund + resubscribe loop → ECONOMIC ABUSE FLAG
- Multi-account same payment method → COLLUSION FLAG

Response:
- Account flagged → Admin Governance review queue
- Repeat abuse → Account suspension workflow triggered
- All economic abuse events → Immutable audit log
```

---

## 8️⃣ USAGE METERING ENGINE (LOCKED)

### 8.1 Metered Events

The following events MUST be captured and metered:

```
EVENT                     METER_KEY                   QUOTA_SCOPE
dojo.room.started         dojo_rooms_used             per user/month
dojo.session.hours        dojo_hours_consumed         per user/month
dojo.recording.stored     recording_storage_gb        per tenant
certification.attempt     cert_attempts_used          per user
ai.match.scored           ai_match_calls              per tenant/month
mentor.session.started    mentor_sessions             per user/month
api.request               api_calls                   per tenant/day
tournament.entry          tournament_entries          per user
```

### 8.2 Quota Enforcement

```
REAL_TIME_CHECK  = true (synchronous before feature access)
CACHE_LAYER      = Redis (quota state, TTL: 60s)
OVERFLOW_POLICY  = DENY (no silent over-quota access)
GRACE_MARGIN     = 0% (strict enforcement, no buffer)
QUOTA_RESET      = first day of billing cycle (UTC midnight)
NOTIFICATION     = at 80% quota usage → alert user/tenant admin
```

---

## 9️⃣ COMMISSION & REVENUE SPLIT ENGINE

### 9.1 Revenue Share Rules

```
TRAINER_COURSE_SALE:
  Platform commission:  30%
  Trainer share:        70%

CO_TEACHING (2 trainers):
  Platform commission:  30%
  Trainer_1 share:      agreed% (stored in CoTeachingAgreement)
  Trainer_2 share:      remaining% (must total 70%)
  Agreement signed by both parties before activation

REFERRAL_REWARD:
  Referrer credit:  500 platform points (convertible)
  Referee credit:   500 platform points
  Cash equivalent:  defined per pricing tier

TOURNAMENT_ENTRY_POOL:
  Platform:    40%
  Prize pool:  50%
  Mentor fee:  10%
```

### 9.2 Revenue Split Calculation (Deterministic)

```
INPUT:  gross_amount, split_rules[], tax_withheld
OUTPUT: net_per_party[] (must sum to gross_amount - tax_withheld)
AUDIT:  every split calculation logged with inputs + outputs
DISPUTE: if sum mismatch → STOP + ALERT finance team
```

### 9.3 Trainer Wallet

```
SCHEMA: TrainerWallet
  wallet_id
  trainer_id
  tenant_id
  available_balance   (withdrawable)
  pending_balance     (clearing period: 7 days)
  lifetime_earned
  currency
  last_updated_at
  audit_hash

OPERATIONS:
  credit()  → append to ledger → update balance
  debit()   → payout or refund adjustment
  freeze()  → compliance hold (Admin Governance trigger)
```

### 9.4 Payout Workflow

```
1. Trainer submits payout request (min threshold: ₹500 / $10)
2. Billing Agent verifies available_balance ≥ request amount
3. Tax deduction calculated (TDS / withholding per jurisdiction)
4. Payout disbursed via payment provider
5. Event: payout.processed published
6. Wallet deducted + ledger appended
7. Payout receipt generated + stored
8. Notification sent to trainer
```

---

## 🔟 COUPON & DISCOUNT SYSTEM

### 10.1 Coupon Types

```
FLAT_DISCOUNT     → fixed amount off (e.g., ₹500 off)
PERCENTAGE_DISCOUNT → % off plan price (e.g., 20% off)
FREE_TRIAL_EXTEND  → adds N days to trial
PLAN_UPGRADE_FREE  → upgrade to PRO for N months
REFERRAL_CODE      → tied to referral system
CAMPUS_CODE        → institution-scoped bulk discount
```

### 10.2 Coupon Lifecycle

```
CREATED → ACTIVE → REDEEMED / EXPIRED / REVOKED
```

### 10.3 Coupon Rules

```
MAX_REDEMPTIONS     → configurable per coupon
SINGLE_USE_PER_USER → enforced
STACKABILITY        → coupons are NON-STACKABLE by default
EXPIRY              → datetime enforced UTC
SCOPE               → GLOBAL | TENANT | USER_SPECIFIC | PLAN_SPECIFIC
ABUSE_DETECTION     → same user, multiple accounts, same coupon → FLAG
```

---

## 1️⃣1️⃣ TENANT BILLING ISOLATION (HARD LOCK)

```
RULE: Each tenant has a completely isolated billing context.

PER-TENANT ISOLATION:
  - Subscription record
  - Invoice history
  - Quota counters
  - Seat allocation
  - Commission rules
  - Trainer wallet pool
  - Payment method storage

Cross-tenant billing data access = SECURITY FAILURE
Tenant billing APIs require tenant_id scope in all requests
Tenant admin may only view their own tenant billing
Platform Super Admin may view all tenants (audit logged)
```

---

## 1️⃣2️⃣ DATABASE SCHEMAS (MANDATORY)

### Core Billing Schemas

```sql
-- Subscription Plans
CREATE TABLE subscription_plans (
  plan_id         UUID PRIMARY KEY,
  plan_name       VARCHAR(100) NOT NULL,
  plan_tier       ENUM('FREE','BASIC','PRO','ENTERPRISE'),
  billing_cycle   ENUM('MONTHLY','ANNUAL','SEAT','MENTOR_LICENSE'),
  base_price      DECIMAL(10,2),
  currency        CHAR(3) DEFAULT 'INR',
  features        JSONB,          -- feature_flag keys enabled
  seat_limit      INTEGER,        -- null = unlimited
  is_active       BOOLEAN DEFAULT TRUE,
  created_at      TIMESTAMPTZ DEFAULT NOW(),
  version         INTEGER DEFAULT 1
);

-- Tenant Subscriptions
CREATE TABLE tenant_subscriptions (
  subscription_id     UUID PRIMARY KEY,
  tenant_id           UUID NOT NULL REFERENCES tenants(tenant_id),
  plan_id             UUID NOT NULL REFERENCES subscription_plans(plan_id),
  status              ENUM('TRIAL','ACTIVE','GRACE','CANCELLED','EXPIRED'),
  seats_purchased     INTEGER,
  seats_used          INTEGER DEFAULT 0,
  trial_ends_at       TIMESTAMPTZ,
  current_period_start TIMESTAMPTZ,
  current_period_end  TIMESTAMPTZ,
  cancel_at_period_end BOOLEAN DEFAULT FALSE,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  audit_hash          CHAR(64)
);

-- Billing Ledger (Immutable)
CREATE TABLE billing_ledger (
  ledger_id       UUID PRIMARY KEY,
  tenant_id       UUID NOT NULL,
  user_id         UUID,
  entry_type      ENUM('CHARGE','CREDIT','REFUND','ADJUSTMENT','PAYOUT','COMMISSION'),
  amount          DECIMAL(12,2) NOT NULL,
  currency        CHAR(3),
  reference_id    UUID,           -- payment_id, refund_id, payout_id
  description     TEXT,
  created_at      TIMESTAMPTZ DEFAULT NOW(),
  audit_hash      CHAR(64) NOT NULL,
  previous_hash   CHAR(64)        -- chain integrity
  -- NO UPDATE OR DELETE PERMITTED ON THIS TABLE
);

-- Invoices
CREATE TABLE invoices (
  invoice_id      UUID PRIMARY KEY,
  tenant_id       UUID NOT NULL,
  user_id         UUID NOT NULL,
  subscription_id UUID,
  status          ENUM('DRAFT','ISSUED','PAID','VOID','DISPUTED'),
  line_items      JSONB,
  subtotal        DECIMAL(12,2),
  discount        DECIMAL(12,2) DEFAULT 0,
  tax_amount      DECIMAL(12,2),
  tax_type        VARCHAR(20),
  total_amount    DECIMAL(12,2),
  currency        CHAR(3),
  pdf_url         TEXT,
  issued_at       TIMESTAMPTZ,
  due_at          TIMESTAMPTZ,
  paid_at         TIMESTAMPTZ,
  audit_hash      CHAR(64)
);

-- Payment Records
CREATE TABLE payments (
  payment_id          UUID PRIMARY KEY,
  tenant_id           UUID NOT NULL,
  user_id             UUID NOT NULL,
  invoice_id          UUID REFERENCES invoices(invoice_id),
  provider            VARCHAR(50),    -- 'stripe' | 'razorpay'
  provider_payment_id VARCHAR(255),
  idempotency_key     UUID UNIQUE NOT NULL,
  amount              DECIMAL(12,2),
  currency            CHAR(3),
  status              ENUM('PENDING','SUCCEEDED','FAILED','REFUNDED'),
  metadata            JSONB,
  created_at          TIMESTAMPTZ DEFAULT NOW(),
  audit_hash          CHAR(64)
);

-- Refunds
CREATE TABLE refunds (
  refund_id       UUID PRIMARY KEY,
  payment_id      UUID NOT NULL REFERENCES payments(payment_id),
  tenant_id       UUID NOT NULL,
  user_id         UUID NOT NULL,
  reason          TEXT NOT NULL,
  amount          DECIMAL(12,2),
  status          ENUM('REQUESTED','AUTO_APPROVED','PENDING_REVIEW','APPROVED','DENIED','DISBURSED'),
  reviewed_by     UUID,
  reviewed_at     TIMESTAMPTZ,
  provider_refund_id VARCHAR(255),
  disbursed_at    TIMESTAMPTZ,
  audit_hash      CHAR(64)
);

-- Trainer Wallet
CREATE TABLE trainer_wallets (
  wallet_id           UUID PRIMARY KEY,
  trainer_id          UUID NOT NULL UNIQUE,
  tenant_id           UUID NOT NULL,
  available_balance   DECIMAL(12,2) DEFAULT 0,
  pending_balance     DECIMAL(12,2) DEFAULT 0,
  lifetime_earned     DECIMAL(12,2) DEFAULT 0,
  currency            CHAR(3) DEFAULT 'INR',
  is_frozen           BOOLEAN DEFAULT FALSE,
  freeze_reason       TEXT,
  updated_at          TIMESTAMPTZ DEFAULT NOW(),
  audit_hash          CHAR(64)
);

-- Revenue Share Rules
CREATE TABLE revenue_share_rules (
  rule_id         UUID PRIMARY KEY,
  rule_type       ENUM('COURSE_SALE','COTEACH','REFERRAL','TOURNAMENT'),
  platform_pct    DECIMAL(5,2),
  trainer_pct     DECIMAL(5,2),
  other_pct       DECIMAL(5,2),
  effective_from  TIMESTAMPTZ,
  effective_to    TIMESTAMPTZ,
  is_active       BOOLEAN DEFAULT TRUE,
  approved_by     UUID NOT NULL,       -- human approval required
  version         INTEGER DEFAULT 1
);

-- Usage Quotas
CREATE TABLE usage_quotas (
  quota_id        UUID PRIMARY KEY,
  tenant_id       UUID NOT NULL,
  user_id         UUID,               -- null = tenant-level
  meter_key       VARCHAR(100) NOT NULL,
  used            INTEGER DEFAULT 0,
  limit_value     INTEGER,            -- null = unlimited
  reset_at        TIMESTAMPTZ,
  updated_at      TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE(tenant_id, user_id, meter_key)
);

-- Coupons
CREATE TABLE coupons (
  coupon_id       UUID PRIMARY KEY,
  code            VARCHAR(50) UNIQUE NOT NULL,
  type            ENUM('FLAT','PERCENTAGE','TRIAL_EXTEND','PLAN_UPGRADE','REFERRAL','CAMPUS'),
  value           DECIMAL(10,2),
  scope           ENUM('GLOBAL','TENANT','USER','PLAN'),
  scope_ref_id    UUID,
  max_redemptions INTEGER,
  times_redeemed  INTEGER DEFAULT 0,
  is_stackable    BOOLEAN DEFAULT FALSE,
  expires_at      TIMESTAMPTZ,
  is_active       BOOLEAN DEFAULT TRUE,
  created_by      UUID NOT NULL,
  created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Economic Abuse Log
CREATE TABLE economic_abuse_flags (
  flag_id         UUID PRIMARY KEY,
  tenant_id       UUID,
  user_id         UUID NOT NULL,
  abuse_type      ENUM('REFUND_ABUSE','MULTI_ACCOUNT','COLLUSION','COUPON_ABUSE','FAKE_TOURNAMENT'),
  detected_at     TIMESTAMPTZ DEFAULT NOW(),
  evidence        JSONB,
  status          ENUM('FLAGGED','UNDER_REVIEW','CONFIRMED','DISMISSED'),
  reviewed_by     UUID,
  audit_hash      CHAR(64)
);
```

---

## 1️⃣3️⃣ API CONTRACT REGISTRY (MANDATORY)

### Core Billing Endpoints

```
METHOD  PATH                                  AUTH          ENTITLEMENT
------  ------------------------------------  ------------  ---------------------
GET     /billing/plans                        Public        None
GET     /billing/plans/{plan_id}              Authenticated None
POST    /billing/subscribe                    Authenticated identity_ready
POST    /billing/subscribe/upgrade            Authenticated active_subscription
POST    /billing/subscribe/cancel             Authenticated active_subscription
GET     /billing/subscription                 Authenticated own_tenant_scope
GET     /billing/invoices                     Authenticated own_tenant_scope
GET     /billing/invoices/{invoice_id}/pdf    Authenticated own_invoice
POST    /billing/refund/request               Authenticated own_payment
GET     /billing/refund/{refund_id}           Authenticated own_refund
POST    /billing/coupon/apply                 Authenticated active_subscription
GET     /billing/usage                        Authenticated own_tenant_scope
GET     /billing/entitlement/check            Service        service_identity
POST    /trainer/revenue/split                Service        billing_agent_only
GET     /trainer/wallet                       Trainer        own_trainer_scope
POST    /trainer/payout/request               Trainer        trainer_wallet_owner
GET     /admin/billing/tenants                SuperAdmin     billing_oversight
POST    /admin/billing/refund/approve         Admin          MFA + billing_oversight
POST    /admin/billing/plan/assign            SuperAdmin     billing_oversight
```

### API Rules

```
ALL billing endpoints → require tenant_id in JWT claim
ALL admin billing endpoints → require MFA session token
ALL write operations → idempotency key header mandatory (X-Idempotency-Key)
ALL responses → include request_id for trace correlation
Undeclared endpoint → BLOCK DEPLOYMENT
```

---

## 1️⃣4️⃣ EVENT SCHEMA REGISTRY (MANDATORY)

```
EVENT                       PRODUCER        CONSUMERS
--------------------------  --------------  -----------------------------------------
subscription.activated      Billing Agent   Entitlement Service, Notification, Audit
subscription.upgraded       Billing Agent   Entitlement Service, Notification, Audit
subscription.downgraded     Billing Agent   Entitlement Service, Notification, Audit
subscription.cancelled      Billing Agent   Entitlement Service, Notification, Audit
subscription.expired        Billing Agent   Entitlement Service, Notification, Audit
payment.initiated           Billing Agent   Audit
payment.confirmed           Billing Agent   Invoice Generator, Notification, Audit
payment.failed              Billing Agent   Notification, Retry Queue, Audit
invoice.generated           Billing Agent   Notification, Storage, Audit
refund.requested            Billing Agent   Admin Governance, Audit
refund.approved             Admin Service   Billing Agent, Notification, Audit
refund.denied               Admin Service   Notification, Audit
refund.disbursed            Billing Agent   Notification, Audit
quota.threshold.reached     Billing Agent   Notification (80% trigger)
quota.exhausted             Billing Agent   Entitlement Service, Notification
trainer.wallet.credited     Billing Agent   Trainer Notification, Audit
trainer.payout.processed    Billing Agent   Notification, Audit
economic.abuse.flagged      Billing Agent   Admin Governance, Security, Audit
coupon.redeemed             Billing Agent   Audit, Analytics
```

### Event Envelope (MANDATORY FIELDS)

```json
{
  "event_id":       "UUID",
  "event_type":     "subscription.activated",
  "actor_id":       "UUID",
  "actor_role":     "STUDENT | TRAINER | ADMIN | SYSTEM",
  "tenant_id":      "UUID",
  "domain":         "BILLING",
  "authorized_action": "SUBSCRIBE",
  "payload":        {},
  "timestamp":      "ISO8601",
  "correlation_id": "UUID",
  "schema_version": "1.0"
}
```

---

## 1️⃣5️⃣ SECURITY & COMPLIANCE RULES (ABSOLUTE)

### 15.1 Data Protection

```
PII FIELDS encrypted at rest:
  - payment_method details (only last 4 stored)
  - bank account numbers (payout)
  - tax identification numbers

PCI-DSS SCOPE:
  - No full card data stored on platform
  - Tokenization via Stripe only
  - Vault = provider (never platform DB)

FINANCIAL DATA RETENTION:
  - Invoices: 7 years
  - Ledger entries: 7 years (immutable)
  - Refund records: 7 years
```

### 15.2 Audit Immutability

```
Billing Ledger = APPEND-ONLY
No UPDATE or DELETE on billing_ledger table
Each entry includes previous_hash (blockchain-style chain)
Hash algorithm: SHA-256
Any chain break → CRITICAL SECURITY ALERT
```

### 15.3 MFA Requirements

```
MFA MANDATORY for:
  - Admin billing panel access
  - Refund approval
  - Plan assignment override
  - Revenue rule modification
  - Payout threshold modification
  - Economic abuse status change
```

### 15.4 Authorization Boundaries

```
AI AGENTS (AUTHZ-Q):
  - CANNOT approve billing
  - CANNOT modify subscription plans
  - CANNOT process refunds
  - CANNOT modify revenue share rules
  - MAY generate billing proposals for human review

TENANT ADMIN:
  - Own tenant billing only
  - Cannot view other tenant data
  - Cannot modify plan definitions

PLATFORM SUPER ADMIN:
  - All tenants (audit logged)
  - MFA enforced every session
  - IP allowlist required
```

---

## 1️⃣6️⃣ OBSERVABILITY & ALERTING (MANDATORY)

### 16.1 Metrics (Prometheus)

```
billing_payment_success_total          (counter, by plan, by provider)
billing_payment_failure_total          (counter, by failure_reason)
billing_refund_request_total           (counter)
billing_refund_approval_rate           (gauge)
billing_subscription_active_total      (gauge, by plan, by tenant)
billing_invoice_generation_latency_ms  (histogram)
billing_quota_exhaustion_total         (counter, by meter_key)
billing_economic_abuse_flags_total     (counter, by abuse_type)
billing_webhook_failure_total          (counter, by provider)
billing_ledger_chain_integrity         (gauge: 1=valid, 0=broken)
```

### 16.2 Alerts (PagerDuty / OpsGenie)

```
CRITICAL:
  - Payment success rate < 95% (5-minute window)
  - Billing ledger chain break detected
  - Webhook signature validation failure spike
  - Refund disbursement failure

WARNING:
  - Invoice generation latency > 5 seconds
  - Quota exhaustion events > 50/hour
  - Economic abuse flags > 10/hour
  - Payout failure rate > 5%

INFO:
  - New subscription milestone (100 / 1000 / 10000)
  - MRR change > 20% in 24 hours
```

### 16.3 Dashboards (Grafana)

```
Dashboard: BILLING_OVERVIEW
  - MRR (Monthly Recurring Revenue) trend
  - Plan distribution pie chart
  - Payment success rate (live)
  - Invoice volume (daily)
  - Refund rate trend

Dashboard: BILLING_HEALTH
  - Webhook processing queue depth
  - Idempotency collision rate
  - Quota exhaustion heatmap
  - Economic abuse flags timeline

Dashboard: TRAINER_REVENUE
  - Trainer wallet balances (aggregate)
  - Payout volume trend
  - Revenue split accuracy
  - Commission disbursement status
```

---

## 1️⃣7️⃣ ADMIN GOVERNANCE INTEGRATION (MANDATORY)

```
ADMIN_CONSOLE MODULE: Billing & Finance

Screens:
  ✅ Subscription Plan Viewer / Assignment Tool
  ✅ Invoice & Transaction Viewer (per tenant)
  ✅ Commission Rule Editor (human approval locked)
  ✅ Refund Approval Queue
  ✅ Payment Dispute Resolution Tool
  ✅ Economic Abuse Review Dashboard
  ✅ Trainer Wallet Freeze / Unfreeze Panel
  ✅ Revenue Share Rule Version Manager
  ✅ Coupon Manager (create, deactivate, audit)
  ✅ Billing Ledger Chain Integrity Viewer

Access:
  - Super Admin: Full read/write + MFA
  - Billing Admin: Refund + invoice read + MFA
  - Compliance Admin: Read-only + export + MFA
  - No AI agent access to admin console
```

---

## 1️⃣8️⃣ MULTI-TENANT BILLING ISOLATION CHECKLIST

```
✅ subscription_id scoped to tenant_id (FK enforced)
✅ invoice queries require tenant_id in WHERE clause
✅ quota counters namespaced by tenant_id + user_id
✅ trainer wallet isolated by tenant_id
✅ coupon scope enforced at application layer + DB
✅ billing admin API validates tenant_id from JWT
✅ cross-tenant billing API call → 403 + audit log
✅ billing events include tenant_id in envelope
✅ event consumers re-validate tenant_id before processing
```

---

## 1️⃣9️⃣ BUSINESS REALITY INPUT LOCK (M2 COMPLIANCE)

```
The following CANNOT be autonomously finalized by AI or this agent:

  ❌ Final pricing strategy
  ❌ Legal jurisdiction + tax compliance decisions
  ❌ Tax configuration rules (GST rates, VAT rules)
  ❌ Payment processor contractual setup
  ❌ Revenue share final percentages (require founder/legal approval)
  ❌ Refund policy legal text
  ❌ Region-specific compliance policies

AI MAY: Draft proposals for the above.
HUMAN MUST: Approve and activate final versions before system goes live.
```

---

## 2️⃣0️⃣ FOUR-STAGE ROLLOUT (SEQUENTIAL — NO SKIPPING)

```
STAGE 1 — FOUNDATION (billing essentials)
  ✅ Plan definitions
  ✅ Subscription lifecycle
  ✅ Payment processing (Stripe)
  ✅ Basic invoice generation
  ✅ Entitlement middleware
  ✅ Billing ledger (append-only)

STAGE 2 — INTELLIGENCE (billing analytics)
  ✅ Usage metering engine
  ✅ Quota enforcement + Redis cache
  ✅ Revenue analytics dashboard
  ✅ MRR / ARR tracking

STAGE 3 — ECOSYSTEM (trainer + multi-tenant)
  ✅ Trainer wallet system
  ✅ Revenue split engine
  ✅ Payout workflow
  ✅ Co-teaching revenue agreements
  ✅ Tenant billing isolation (full)
  ✅ Coupon system

STAGE 4 — COMPLIANCE & SCALE
  ✅ GST/VAT multi-jurisdiction handling
  ✅ Economic abuse detection
  ✅ Billing ledger chain integrity monitoring
  ✅ 7-year archive and export
  ✅ Full admin governance console
  ✅ Regulatory audit export

Stage skipping = INVALID BUILD → STOP EXECUTION
```

---

## 2️⃣1️⃣ PRODUCTION READINESS CHECKLIST

```
Before Billing Governance Agent declared PRODUCTION-READY:

Infrastructure:
  ☐ billing-service:8011 healthy + load-balanced
  ☐ PostgreSQL billing schemas migrated (versioned)
  ☐ Redis quota cache live
  ☐ MinIO invoice storage reachable
  ☐ Kafka billing event topics created + partitioned

Payment:
  ☐ Stripe webhook endpoint registered + HMAC verified
  ☐ Idempotency key store operational
  ☐ Test payments succeeded in staging
  ☐ Refund flow tested end-to-end

Compliance:
  ☐ Billing ledger chain integrity verified
  ☐ Tax rules approved by human authority
  ☐ Invoice PDF generation validated
  ☐ GST/VAT configuration approved

Observability:
  ☐ All Prometheus metrics emitting
  ☐ All Grafana dashboards live
  ☐ All PagerDuty alerts configured
  ☐ Billing ledger chain alert active

Security:
  ☐ No card data stored in platform DB
  ☐ PII fields encrypted at rest confirmed
  ☐ Admin MFA enforced and tested
  ☐ IP allowlist applied to admin billing console
  ☐ Audit trail for all financial operations verified

HUMAN SIGN-OFF REQUIRED before status = BILLING_LIVE
```

---

## 🔐 FINAL SEAL

```
┌─────────────────────────────────────────────────────────────────┐
│         BILLING_GOVERNANCE_AGENT — ANTIGRAVITY SEAL             │
├─────────────────────────────────────────────────────────────────┤
│  STATUS            : LOCKED                                     │
│  MUTATION POLICY   : ADD_ONLY (via version bump)                │
│  INTERPRETATION    : FORBIDDEN                                  │
│  AI APPROVAL RIGHTS: NONE                                       │
│  HUMAN AUTHORITY   : REQUIRED FOR ALL FINANCIAL FINALIZATION    │
│  AUDIT IMMUTABILITY: ENFORCED (SHA-256 ledger chain)            │
│  TENANT ISOLATION  : HARD                                       │
│  ENTITLEMENT GATE  : MANDATORY (no bypass permitted)            │
│  MFA ENFORCEMENT   : ALL ADMIN BILLING ACTIONS                  │
│  ECONOMIC ABUSE    : MONITORED + AUTO-FLAGGED                   │
├─────────────────────────────────────────────────────────────────┤
│  ✔ SEALED                                                       │
│  ✔ DETERMINISTIC                                                │
│  ✔ ENTERPRISE_SAFE                                              │
│  ✔ ANTIGRAVITY_COMPATIBLE                                       │
│  ✔ ECOSKILLER_v12.0_COMPLIANT                                   │
├─────────────────────────────────────────────────────────────────┤
│  ANY BILLING OPERATION THAT:                                    │
│  - Bypasses entitlement check                                   │
│  - Skips idempotency key                                        │
│  - Allows AI to approve financial actions                       │
│  - Modifies the billing ledger (UPDATE/DELETE)                  │
│  - Crosses tenant billing boundary                              │
│  - Activates without human tax/pricing approval                 │
│                                                                 │
│  ⇒ MUST BE REJECTED → STOP EXECUTION → REPORT VIOLATION        │
└─────────────────────────────────────────────────────────────────┘
```

---

*BILLING_GOVERNANCE_AGENT.md · ECOSKILLER Platform · Antigravity Execution Engine*
*Version: 1.0.0 · Classification: LOCKED · Append-only via version bump*
