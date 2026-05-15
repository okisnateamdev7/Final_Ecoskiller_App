# 🔐 API_ECONOMY_AGENT.md
## SEALED & LOCKED EXECUTION PROMPT — ANTIGRAVITY ENGINE
### ECOSKILLER · ENTERPRISE SAAS PLATFORM · API ECONOMY LAYER
---

```
PROMPT_CLASS              = API_ECONOMY_AGENT
EXECUTION_ENGINE          = ANTIGRAVITY
AGENT_VERSION             = 1.0.0
MUTATION_POLICY           = ADD_ONLY
CREATIVE_INTERPRETATION   = FORBIDDEN
ASSUMPTION_FILLING        = FORBIDDEN
IMPLICIT_BEHAVIOR         = FORBIDDEN
DEFAULT_BEHAVIOR          = DENY
FAILURE_MODE              = STOP_EXECUTION
EXECUTION_MODE            = LOCKED
STATUS                    = SEALED
ENGINEERING_GRADE         = PRINCIPAL_ENGINEER
ANTIGRAVITY_CONFUSION     = IMPOSSIBLE
```

---

## ⚠️ PREAMBLE — READ BEFORE EXECUTION

This agent governs the complete **API Economy Layer** of the ECOSKILLER platform — encompassing API product packaging, monetization structures, consumption metering, developer onboarding, revenue distribution, marketplace economics, commission governance, rate plan management, API lifecycle governance, and external partner billing. It is a **revenue-generating commercial layer** sitting above the technical Integration Hub and Webhook Engine.

This agent inherits — and **MUST NEVER duplicate** — all decisions already sealed in:
- ECOSKILLER Master Execution Prompt v12.0
- The UI Constitution (Flutter-first, Next.js SEO-clone)
- WEBHOOK_MARKETPLACE_AGENT.md (External API Keys, webhook delivery, plugin registry)
- R19 (API Versioning & Deprecation Law)
- R28 (Intelligence Cost Optimization Law)
- R39 (Core Inbuilt Platform Tools Law — Billing & Commercial Tools)
- R65 (Open Platform Extensibility Law)
- Section §VIII (Marketplace Economic Governance — commission, revenue distribution, payout)
- Billing_Service (subscription plans, feature gating, invoices — already mandated)

**Antigravity MUST NOT:**
- Re-implement authentication, RBAC, or session management (owned by Auth_Service + Keycloak)
- Re-implement webhook delivery infrastructure (owned by WEBHOOK_MARKETPLACE_AGENT)
- Re-implement base subscription billing (owned by Billing_Service)
- Introduce new databases outside: PostgreSQL, Redis, ClickHouse, OpenSearch (sealed stack)
- Apply creative interpretation to any pricing model, commission formula, or revenue rule
- Generate UI outside Flutter (primary) and Next.js (SEO read-only clone)

**Any deviation from the above = STOP EXECUTION + REPORT DEVIATION CLASS + SECTION VIOLATED**

---

## SECTION 1 — AGENT IDENTITY & SCOPE

```
AGENT_NAME                = ApiEconomyAgent
AGENT_TYPE                = COMMERCIAL_MONETIZATION_AGENT
PARENT_PROMPT             = ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0
INHERITS_FROM             = [R19, R28, R39, R65, Billing_Service, WEBHOOK_MARKETPLACE_AGENT]
OWNED_MODULES             = [
                              API_Product_Catalog,
                              API_Rate_Plan_Engine,
                              API_Consumption_Metering,
                              Developer_Billing_Portal,
                              Revenue_Distribution_Engine,
                              Commission_Governance,
                              Partner_Payout_Service,
                              Marketplace_Economic_Governor,
                              API_Lifecycle_Manager,
                              API_Analytics_Engine
                            ]
KAFKA_CONSUMER            = YES
KAFKA_PRODUCER            = YES
PLATFORM_SCOPE            = MULTI_TENANT
```

### 1.1 Functional Mandate

This agent is responsible for:

1. **API Product Catalog** — Defining, versioning, and publishing Ecoskiller's APIs as named, sellable products with distinct capability sets
2. **Rate Plan Engine** — Governing free, pay-as-you-go, tiered, flat-rate, and enterprise rate plans across all API products
3. **Consumption Metering** — Real-time per-call, per-token, per-seat, and per-data-volume metering with immutable ledger entries
4. **Developer Billing Portal** — Self-service billing management for external developers, partners, and enterprise integrators
5. **Revenue Distribution Engine** — Calculating, splitting, and disbursing platform revenue across: Platform, Trainers, Institutes, Enterprise Partners, Plugin Vendors, Affiliate Referrers
6. **Commission Governance** — Sealed commission rules for every revenue-generating transaction type on the platform
7. **Partner Payout Service** — Processing verified, audited payouts to trainers, institutes, and plugin vendors on defined schedules
8. **Marketplace Economic Governor** — Monitoring marketplace liquidity, supply-demand equilibrium, and economic health (inherits R67 framework)
9. **API Lifecycle Manager** — Governing API version rollout, deprecation, sunset enforcement, and backward compatibility windows (inherits R19)
10. **API Analytics Engine** — Providing developer-facing and internal revenue analytics, usage dashboards, quota consumption charts

### 1.2 Out of Scope (DO NOT GENERATE)

- Core webhook delivery infrastructure → owned by WEBHOOK_MARKETPLACE_AGENT
- Plugin registration and marketplace UI → owned by WEBHOOK_MARKETPLACE_AGENT
- Base subscription plan management → owned by Billing_Service
- Authentication and token issuance → owned by Auth_Service
- Job/Skill/Project/Dojo domain business logic → owned by respective domain services
- AI/ML scoring and matching engines → owned by Intelligence Services

---

## SECTION 2 — EXECUTION INHERITANCE LOCK

All inherited contracts are SEALED. Antigravity MUST NOT redefine them:

| Inherited Contract | Source | Behavior in This Agent |
|---|---|---|
| RBAC + ABAC enforcement | Master Prompt §Auth | All billing and revenue actions are role-gated via OPA |
| Tenant isolation (RLS) | Master Prompt §III | All economy tables carry tenant_id with RLS policies |
| Kafka event bus | Master Prompt §VI | Revenue events published to shared Kafka bus |
| PostgreSQL primary store | Master Prompt §III | All ledger and plan tables in PostgreSQL |
| Redis for rate/lock/cache | Master Prompt §III | Real-time quota enforcement via Redis counters |
| ClickHouse for analytics | Master Prompt §III | Revenue and usage metrics sink |
| Kong API Gateway | Master Prompt | Quota enforcement middleware lives in Kong |
| HashiCorp Vault | Master Prompt | Payout credentials and payment processor keys |
| Prometheus + Loki + OTEL | Master Prompt §VIII | All economy metrics and traces mandatory |
| Flutter primary UI | UI Constitution §2 | Developer Billing Portal primary app = Flutter |
| Next.js SEO read-only clone | R31 | Billing portal web = Next.js (SEO read-only) |
| API versioning SemVer | R19 | All API products follow SemVer + 90-day deprecation window |
| AI-for-advisory only | R28 | AI may generate revenue insights but NEVER approve payouts |
| WCAG 2.1 AA | R20 | All Developer Billing Portal screens WCAG-compliant |
| Billing_Service plans | Billing_Service | API rate plans layer ABOVE base subscription plans |
| Webhook API Keys | WEBHOOK_MARKETPLACE_AGENT | API Economy reuses same key infrastructure, never re-implements |
| Marketplace extensibility | R65 | API Economy extends R65, does not replace it |
| Commission rules | §8 Marketplace Economic Governance | Formulas adopted from master promise, enforced here |
| GST/VAT compliance | Billing_Service | Tax calculation delegated to Billing_Service |

**Conflict resolution:** Inherited contract always wins. Conflicting generated artifact MUST be discarded. STOP + REPORT.

---

## SECTION 3 — API PRODUCT CATALOG (SEALED)

The platform exposes its capabilities to external developers, enterprise integrators, and partner systems as **named API Products**. Each product is a sellable, metered, versioned unit with defined SLAs.

### 3.1 Platform API Products (Sealed Catalog)

```
CATALOG_VERSION   = 1.0.0
NAMESPACE         = ecoskiller.api.v1
MUTATION_RULE     = New products ADD-ONLY. Existing products version-bumped only.
```

| Product ID | Product Name | Description | Metering Unit | Default SLA |
|---|---|---|---|---|
| `api.jobs` | Job Intelligence API | Job discovery, eligibility scoring, AI match | per-call | 99.9% |
| `api.candidates` | Candidate Intelligence API | Candidate search, skill vectors, placement probability | per-call | 99.9% |
| `api.skills` | Skill Graph API | Skill gap analysis, demand mapping, ontology access | per-call | 99.9% |
| `api.projects` | Project Intelligence API | Project matching, milestone verification, portfolio data | per-call | 99.9% |
| `api.certifications` | Certification & Belt API | Credential verification, belt status, certificate issuance | per-call | 99.99% |
| `api.assessments` | Assessment & Dojo API | GD session orchestration, scoring retrieval, replay metadata | per-call | 99.9% |
| `api.analytics` | Platform Analytics API | Cohort analytics, placement rates, recruiter behavior | per-call | 99.9% |
| `api.notifications` | Notification Delivery API | Trigger email/SMS/push via platform notification engine | per-call | 99.5% |
| `api.institutions` | Institution ERP API | Student records, cohort management, placement reports | per-seat | 99.9% |
| `api.recruiting` | Recruiting Pipeline API | Application management, pipeline stages, offer tracking | per-call | 99.9% |
| `api.resumes` | Resume Intelligence API | Resume parsing, skill extraction, AI enhancement | per-call + per-token | 99.9% |
| `api.reputation` | Reputation & Trust API | Trust scores, endorsement graphs, fraud flags (read-only) | per-call | 99.9% |
| `api.events` | Platform Event Stream API | Webhook subscription management, event catalog access | per-subscription | 99.9% |
| `api.bulk` | Bulk Data API | Batch exports, bulk imports, async data pipelines | per-GB | 99.5% |
| `api.embed` | Embeddable Widgets API | White-label widget embedding for external portals | per-render | 99.9% |

### 3.2 API Product Rules (LOCKED)

- Every API product has a unique `product_id`, `version`, `status`, and `metering_type`
- A product may be in: `draft` → `beta` → `ga` → `deprecated` → `sunset` — no stages may be skipped
- Products in `deprecated` must remain functional for **minimum 90 days** post-announcement (R19 inheritance)
- Products in `sunset` immediately reject all calls with HTTP 410 Gone and a migration URL
- Metering types: `per_call`, `per_token`, `per_seat`, `per_gb`, `per_render`, `flat_rate` — exactly one per product
- Composite metering (e.g., `per_call + per_token`) is permitted for AI products where declared explicitly
- No product may expose raw PII without tenant consent capture and GDPR-safe transformation

---

## SECTION 4 — RATE PLAN ENGINE (SEALED)

Rate plans define the commercial terms under which API products are consumed. Plans are tenant-scoped, product-specific, and enforced at Kong gateway level with Redis counters.

### 4.1 Rate Plan Types (Sealed)

```
PLAN_TYPES = [
  free_tier,
  pay_as_you_go,
  tiered_volume,
  flat_rate_monthly,
  enterprise_custom,
  trial
]
```

| Plan Type | Description | Overage Behavior | Billing Cycle |
|---|---|---|---|
| `free_tier` | Fixed monthly quota, no charge | Hard block on overage | N/A (free) |
| `pay_as_you_go` | No monthly commitment, pay per unit | No block, charged per unit | Monthly |
| `tiered_volume` | Discounted rates as volume increases across tiers | Charged at applicable tier rate | Monthly |
| `flat_rate_monthly` | Fixed monthly fee, unlimited within quota | Soft throttle, no charge | Monthly |
| `enterprise_custom` | Bespoke contract with SLA commitment | Contractual negotiation | Custom |
| `trial` | Time-limited free access to a specific product | Hard block post-trial expiry | N/A |

### 4.2 Standard Rate Tiers Per Product (Sealed Defaults)

```
FREE_TIER:
  api.jobs           = 500 calls/month
  api.candidates     = 500 calls/month
  api.skills         = 1,000 calls/month
  api.certifications = 100 calls/month
  api.resumes        = 50 calls/month + 25,000 tokens/month
  api.analytics      = 100 calls/month
  api.events         = 5 active subscriptions
  api.bulk           = 1 GB/month
  api.embed          = 10,000 renders/month
  All other products = 200 calls/month

PAY_AS_YOU_GO (unit pricing, per product):
  api.jobs           = ₹0.10 / call   (USD $0.0012)
  api.candidates     = ₹0.15 / call   (USD $0.0018)
  api.skills         = ₹0.08 / call   (USD $0.0010)
  api.certifications = ₹0.25 / call   (USD $0.0030)
  api.resumes        = ₹0.20 / call + ₹0.002 / token
  api.analytics      = ₹0.30 / call   (USD $0.0036)
  api.bulk           = ₹5.00 / GB     (USD $0.060)
  api.embed          = ₹0.005 / render

TIERED_VOLUME (api.jobs example — all products follow same pattern):
  Tier 1:  0    –  10,000 calls → ₹0.10/call
  Tier 2: 10,001 –  50,000 calls → ₹0.08/call
  Tier 3: 50,001 – 200,000 calls → ₹0.06/call
  Tier 4: 200,001+              → ₹0.04/call

FLAT_RATE_MONTHLY:
  Starter  Plan = ₹4,999/month  → 50,000 calls across all products
  Growth   Plan = ₹14,999/month → 200,000 calls across all products
  Scale    Plan = ₹39,999/month → 1,000,000 calls across all products

ENTERPRISE_CUSTOM:
  Pricing = Platform Admin + human negotiation required
  SLA     = Custom (must be documented in enterprise_contracts table)
  Discount= Maximum 40% below Scale Plan pricing
```

**Rules:**
- Prices are in INR primary, USD secondary. Currency conversion uses locked daily exchange rates from RBI reference
- All pricing changes require MAJOR version bump on the rate plan record + 30-day advance notice to affected tenants
- No rate plan may be applied retroactively
- Trial plans automatically expire; no manual extension without Platform Admin approval
- Free tier is available per developer account, not per API key (one free tier per verified developer identity)

### 4.3 Quota Enforcement Architecture (LOCKED)

```
ENFORCEMENT_LAYER   = Kong API Gateway (primary) + Redis (secondary counter)
ENFORCEMENT_MODE    = Distributed token bucket algorithm
COUNTER_TTL         = Rolling 30-day window (monthly plans) | Rolling 1-hour (burst)
BURST_ALLOWANCE     = 20% above plan quota for pay_as_you_go and tiered plans only
HARD_BLOCK_AT       = 3× plan quota (free_tier) | No hard block (pay_as_you_go)
HTTP_STATUS_BLOCKED = 429 Too Many Requests
RETRY_AFTER_HEADER  = REQUIRED on every 429 response
QUOTA_HEADERS       = X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset (MANDATORY on all responses)
REDIS_KEY_PATTERN   = quota:{tenant_id}:{product_id}:{plan_id}:{window}
REDIS_KEY_TTL       = Auto-expires at window end
```

Kong plugin configuration per product:
- `rate-limiting` plugin with Redis backend (shared counter cluster)
- `request-termination` plugin on sunset products
- `response-transformer` plugin injects quota headers on all responses
- Per-consumer rate limits configured via Kong Admin API, not hardcoded

---

## SECTION 5 — API CONSUMPTION METERING ENGINE

The metering engine records every billable API event with sub-second precision into an immutable ledger. It is the authoritative source for all invoicing.

### 5.1 Metering Architecture

```
METERING_SOURCE     = Kong access logs (primary) + Service-side instrumentation (secondary)
METERING_TRANSPORT  = Kafka topic: ecoskiller.metering.events
METERING_SINK       = PostgreSQL (api_usage_ledger — immutable) + ClickHouse (analytics)
METERING_LATENCY    = <500ms from API call to ledger entry (P99)
IDEMPOTENCY_KEY     = Kong request ID (deduplicated on ingest)
```

### 5.2 Metering Event Schema (Kafka — SEALED)

Every API call generates one Kafka metering event:

```json
{
  "schema_version": "1.0",
  "event_type": "api.call.metered",
  "metering_id": "uuid-v4",
  "request_id": "kong-request-id",
  "tenant_id": "uuid-v4",
  "developer_id": "uuid-v4",
  "api_key_prefix": "esk_XXXXXXXX",
  "product_id": "api.jobs",
  "product_version": "v1",
  "endpoint": "/api/v1/jobs/match",
  "http_method": "POST",
  "http_status": 200,
  "plan_id": "uuid-v4",
  "plan_type": "pay_as_you_go",
  "metering_unit": "per_call",
  "units_consumed": 1,
  "token_count": null,
  "data_volume_bytes": null,
  "billable": true,
  "timestamp_ms": 1700000000000,
  "duration_ms": 145,
  "region": "ap-south-1"
}
```

**Rules:**
- `billable: false` records are logged but never invoiced (free tier overage blocked before reaching service)
- Token count is populated only for `api.resumes` and future AI products
- Every metering event is cryptographically signed with tenant-scoped key from Vault
- Metering events MUST NOT contain PII beyond identifiers

### 5.3 Database Schema — Immutable Metering Ledger (PostgreSQL)

```sql
CREATE TABLE ecoskiller.api_usage_ledger (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  developer_id        UUID NOT NULL,
  api_key_prefix      TEXT NOT NULL,
  product_id          TEXT NOT NULL,
  product_version     TEXT NOT NULL DEFAULT 'v1',
  endpoint            TEXT NOT NULL,
  http_method         TEXT NOT NULL,
  http_status         SMALLINT NOT NULL,
  plan_id             UUID NOT NULL,
  plan_type           TEXT NOT NULL,
  metering_unit       TEXT NOT NULL,
  units_consumed      NUMERIC(18,6) NOT NULL DEFAULT 1,
  token_count         BIGINT,
  data_volume_bytes   BIGINT,
  billable            BOOLEAN NOT NULL DEFAULT TRUE,
  unit_price_inr      NUMERIC(12,6),         -- Locked at call time, never retroactive
  gross_amount_inr    NUMERIC(12,4),
  idempotency_key     TEXT NOT NULL UNIQUE,  -- Kong request ID
  signed_hash         TEXT NOT NULL,         -- HMAC-SHA256 of row for tamper detection
  recorded_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  billing_cycle_id    UUID,                  -- Populated on invoice generation
  invoiced            BOOLEAN NOT NULL DEFAULT FALSE
);

-- IMMUTABILITY ENFORCEMENT
CREATE OR REPLACE FUNCTION ecoskiller.prevent_ledger_mutation()
RETURNS TRIGGER AS $$
BEGIN
  RAISE EXCEPTION 'api_usage_ledger is immutable. UPDATE and DELETE are forbidden.';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ledger_immutability
  BEFORE UPDATE OR DELETE ON ecoskiller.api_usage_ledger
  FOR EACH ROW EXECUTE FUNCTION ecoskiller.prevent_ledger_mutation();

-- PARTITION by month for performance
-- Primary index: (tenant_id, recorded_at, product_id)
-- Secondary index: (idempotency_key) UNIQUE — deduplication gate

-- ROW-LEVEL SECURITY
ALTER TABLE ecoskiller.api_usage_ledger ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.api_usage_ledger
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

---

## SECTION 6 — REVENUE DISTRIBUTION ENGINE (SEALED FORMULAS)

The Revenue Distribution Engine calculates how every billable rupee flows across all revenue participants. This is the authoritative commercial settlement layer.

### 6.1 Revenue Participants & Roles

| Participant | Role | Revenue Trigger |
|---|---|---|
| **Platform (Ecoskiller)** | Platform operator | All transactions — takes base platform commission |
| **Trainer / Mentor** | Content and session delivery | Course enrollment, GD session, live mentoring |
| **Institute** | Institutional partner | Placement-linked fees, bulk licensing, campus programs |
| **Enterprise Partner** | Corporate hiring integrator | API consumption, premium job slots, AI screening |
| **Plugin Vendor** | Marketplace extension provider | Plugin installation fees, plugin premium subscriptions |
| **Affiliate Referrer** | User/partner referral | Referral-linked first purchase, subscription activation |
| **Government Body** | Skill program operator | Government-funded batch placements (zero margin, compliance-only) |

### 6.2 Commission Structure (SEALED — Human Approval Required for Change)

```
COMMISSION_AUTHORITY     = Platform Super Admin + Finance Officer (dual approval)
COMMISSION_CHANGE_POLICY = 30-day advance notice to affected participants
COMMISSION_PRECISION     = 6 decimal places (NUMERIC(12,6)) — no rounding until payout
```

#### 6.2.1 API Product Revenue

| Product | Platform Cut | Developer/Partner Receives | Notes |
|---|---|---|---|
| `api.jobs` | 100% | 0% | Platform-owned data product |
| `api.candidates` | 100% | 0% | Platform-owned data product |
| `api.certifications` | 80% | 20% to Certification Partner | If third-party body involved |
| `api.resumes` | 100% | 0% | Platform-owned AI product |
| `api.assessments` | 70% | 30% to Assessment Partner | If partner-delivered |
| `api.embed` | 85% | 15% to Widget Publisher | If white-label publisher involved |
| All other products | 100% | 0% | Platform-owned |

#### 6.2.2 Course & Skill Marketplace Revenue

| Transaction Type | Platform Cut | Trainer Cut | Institute Cut | Notes |
|---|---|---|---|---|
| Paid course enrollment | 25% | 75% | 0% | Trainer-hosted course |
| Batch corporate licensing | 25% | 60% | 15% | Institute-affiliation bonus |
| Live mentoring session | 20% | 80% | 0% | 1:1 or group mentor session |
| Group Discussion (Dojo) paid session | 30% | 70% | 0% | Trainer-hosted GD prep |
| Campus placement program | 30% | 0% | 70% | Institute-brokered placement |
| Certification examination fee | 40% | 30% | 30% | Tripartite split |

#### 6.2.3 Recruitment & Job Board Revenue

| Transaction Type | Platform Cut | Recruiter/Employer Share | Notes |
|---|---|---|---|
| Premium job slot purchase | 100% | 0% | Pure platform revenue |
| AI candidate screening (per batch) | 100% | 0% | Platform AI product |
| Employer API consumption | 100% | 0% | Via api.recruiting |
| Recruiter profile boost | 100% | 0% | Advertising product |
| Placement success fee | 70% | 0% | 30% to originating Institute |

#### 6.2.4 Plugin Marketplace Revenue

| Transaction Type | Platform Cut | Plugin Vendor Cut | Notes |
|---|---|---|---|
| Free plugin install | 0% | 0% | No revenue event |
| Premium plugin subscription | 30% | 70% | Monthly, vendor-priced |
| Enterprise plugin license | 25% | 75% | Annual contract |
| Plugin one-time purchase | 30% | 70% | Non-recurring |

#### 6.2.5 Referral & Affiliate Revenue

| Referral Type | Referrer Reward | Trigger | Expiry |
|---|---|---|---|
| User-to-user referral | ₹250 platform credit | First paid transaction by referred user | 90 days post-signup |
| Affiliate partner (first purchase) | 15% of first invoice amount | Invoice paid | 60 days post-signup |
| Campus ambassador (bulk) | ₹100 per activated user | User completes profile + 1 activity | 30 days activation window |
| Plugin referral | 10% of first year plugin revenue | Subscription maintained 90 days | Annual |

**Rules:**
- All commission splits are calculated at transaction time, not payout time
- Platform cut is deducted first; all other splits calculated on gross amount
- Government-body revenue events carry 0% margin (cost-recovery mode, zero commission to others)
- Commission records are immutable post-calculation (stored in `revenue_distribution_ledger`)
- No commission may be manually overridden without Platform Super Admin + audit trail

### 6.3 Revenue Distribution Database Schema (PostgreSQL)

```sql
-- Revenue split ledger (immutable)
CREATE TABLE ecoskiller.revenue_distribution_ledger (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  source_transaction_id UUID NOT NULL,       -- Reference to api_usage_ledger or billing invoice
  transaction_type    TEXT NOT NULL,         -- 'api_call' | 'course_enrollment' | 'job_slot' | 'plugin_subscription' | ...
  gross_amount_inr    NUMERIC(12,4) NOT NULL,
  participant_id      UUID NOT NULL,
  participant_type    TEXT NOT NULL
                      CHECK (participant_type IN ('platform', 'trainer', 'institute', 'enterprise_partner', 'plugin_vendor', 'affiliate', 'government')),
  commission_rate     NUMERIC(6,4) NOT NULL,  -- e.g. 0.7500 = 75%
  share_amount_inr    NUMERIC(12,4) NOT NULL,
  currency            TEXT NOT NULL DEFAULT 'INR',
  calculated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  payout_batch_id     UUID,                  -- Populated when included in payout run
  payout_status       TEXT NOT NULL DEFAULT 'pending'
                      CHECK (payout_status IN ('pending', 'in_payout', 'paid', 'disputed', 'failed')),
  signed_hash         TEXT NOT NULL          -- Tamper-detection
);

-- IMMUTABILITY (same pattern as usage ledger)
CREATE TRIGGER revenue_ledger_immutability
  BEFORE UPDATE OR DELETE ON ecoskiller.revenue_distribution_ledger
  FOR EACH ROW EXECUTE FUNCTION ecoskiller.prevent_ledger_mutation();

ALTER TABLE ecoskiller.revenue_distribution_ledger ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.revenue_distribution_ledger
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);

-- Payout batches
CREATE TABLE ecoskiller.payout_batches (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  batch_reference     TEXT NOT NULL UNIQUE,  -- PAY-2025-01-001
  participant_id      UUID NOT NULL,
  participant_type    TEXT NOT NULL,
  period_start        DATE NOT NULL,
  period_end          DATE NOT NULL,
  gross_payable_inr   NUMERIC(12,4) NOT NULL,
  tds_deduction_inr   NUMERIC(12,4) NOT NULL DEFAULT 0,  -- TDS per Indian tax law
  net_payable_inr     NUMERIC(12,4) NOT NULL,
  payment_method      TEXT NOT NULL
                      CHECK (payment_method IN ('bank_transfer_neft', 'bank_transfer_rtgs', 'upi', 'paypal', 'stripe_payout')),
  payment_status      TEXT NOT NULL DEFAULT 'pending'
                      CHECK (payment_status IN ('pending', 'processing', 'completed', 'failed', 'disputed')),
  authorized_by       UUID NOT NULL,         -- Platform Finance Officer
  authorized_at       TIMESTAMPTZ NOT NULL,
  processed_at        TIMESTAMPTZ,
  failure_reason      TEXT,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Payout rules per participant
CREATE TABLE ecoskiller.payout_rules (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  participant_id      UUID NOT NULL UNIQUE,
  participant_type    TEXT NOT NULL,
  minimum_payout_inr  NUMERIC(12,4) NOT NULL DEFAULT 500.00,
  payout_frequency    TEXT NOT NULL DEFAULT 'monthly'
                      CHECK (payout_frequency IN ('weekly', 'monthly', 'quarterly', 'on_demand')),
  payout_day          SMALLINT,              -- Day of month for monthly payouts
  bank_account_ref    TEXT,                  -- Vault reference, never stored plaintext
  upi_ref             TEXT,                  -- Vault reference
  tds_applicable      BOOLEAN NOT NULL DEFAULT TRUE,
  tds_rate            NUMERIC(5,4) NOT NULL DEFAULT 0.10,  -- 10% TDS default
  currency            TEXT NOT NULL DEFAULT 'INR',
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

## SECTION 7 — API PRODUCT DATABASE SCHEMA (FULL)

```sql
-- API Product Catalog (platform-managed)
CREATE TABLE ecoskiller.api_products (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  product_id          TEXT NOT NULL UNIQUE,  -- 'api.jobs', 'api.candidates', ...
  name                TEXT NOT NULL,
  description         TEXT NOT NULL,
  metering_type       TEXT NOT NULL
                      CHECK (metering_type IN ('per_call', 'per_token', 'per_seat', 'per_gb', 'per_render', 'flat_rate', 'composite')),
  composite_meters    TEXT[],                -- Populated if metering_type = 'composite'
  status              TEXT NOT NULL DEFAULT 'ga'
                      CHECK (status IN ('draft', 'beta', 'ga', 'deprecated', 'sunset')),
  deprecated_at       TIMESTAMPTZ,
  sunset_at           TIMESTAMPTZ,           -- Hard cutoff date
  migration_url       TEXT,                  -- Required when status = 'deprecated' or 'sunset'
  sla_availability    NUMERIC(5,4),          -- e.g. 0.9990 = 99.90%
  docs_url            TEXT NOT NULL,
  current_version     TEXT NOT NULL DEFAULT 'v1',
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Rate plans (tenant or platform-global)
CREATE TABLE ecoskiller.api_rate_plans (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  product_id          TEXT NOT NULL REFERENCES ecoskiller.api_products(product_id),
  plan_name           TEXT NOT NULL,
  plan_type           TEXT NOT NULL
                      CHECK (plan_type IN ('free_tier', 'pay_as_you_go', 'tiered_volume', 'flat_rate_monthly', 'enterprise_custom', 'trial')),
  quota_units         BIGINT,                -- NULL = unlimited (pay_as_you_go)
  quota_window        TEXT DEFAULT 'monthly' CHECK (quota_window IN ('hourly', 'daily', 'monthly')),
  overage_behavior    TEXT NOT NULL
                      CHECK (overage_behavior IN ('block', 'charge', 'throttle')),
  unit_price_inr      NUMERIC(12,6),
  flat_fee_inr        NUMERIC(12,4),
  currency            TEXT NOT NULL DEFAULT 'INR',
  trial_duration_days SMALLINT,              -- Only for trial plans
  is_global           BOOLEAN NOT NULL DEFAULT TRUE,  -- FALSE = tenant-custom plan
  tenant_id           UUID,                  -- NULL if is_global = TRUE
  tier_breaks         JSONB,                 -- Tier volume break points for tiered_volume
  status              TEXT NOT NULL DEFAULT 'active'
                      CHECK (status IN ('active', 'deprecated', 'sunset')),
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version             INT NOT NULL DEFAULT 1
);

-- Tenant plan subscriptions (which plan a tenant is on per product)
CREATE TABLE ecoskiller.tenant_api_plan_subscriptions (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  developer_id        UUID NOT NULL,
  product_id          TEXT NOT NULL,
  plan_id             UUID NOT NULL REFERENCES ecoskiller.api_rate_plans(id),
  status              TEXT NOT NULL DEFAULT 'active'
                      CHECK (status IN ('active', 'trial', 'suspended', 'cancelled')),
  activated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  trial_expires_at    TIMESTAMPTZ,
  cancelled_at        TIMESTAMPTZ,
  current_quota_used  BIGINT NOT NULL DEFAULT 0,
  quota_reset_at      TIMESTAMPTZ NOT NULL,  -- Next window reset
  created_by          UUID NOT NULL
);

ALTER TABLE ecoskiller.tenant_api_plan_subscriptions ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.tenant_api_plan_subscriptions
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);

-- API product invoices (computed monthly by Airflow)
CREATE TABLE ecoskiller.api_invoices (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  invoice_number      TEXT NOT NULL UNIQUE,  -- INV-API-2025-01-001
  tenant_id           UUID NOT NULL,
  developer_id        UUID NOT NULL,
  billing_period_start DATE NOT NULL,
  billing_period_end   DATE NOT NULL,
  line_items          JSONB NOT NULL,         -- Per-product breakdown
  subtotal_inr        NUMERIC(12,4) NOT NULL,
  gst_amount_inr      NUMERIC(12,4) NOT NULL DEFAULT 0,
  total_inr           NUMERIC(12,4) NOT NULL,
  currency            TEXT NOT NULL DEFAULT 'INR',
  status              TEXT NOT NULL DEFAULT 'draft'
                      CHECK (status IN ('draft', 'issued', 'paid', 'overdue', 'disputed', 'voided')),
  issued_at           TIMESTAMPTZ,
  due_at              TIMESTAMPTZ,
  paid_at             TIMESTAMPTZ,
  payment_method      TEXT,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Enterprise API contracts (bespoke deals)
CREATE TABLE ecoskiller.enterprise_api_contracts (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL,
  contract_reference  TEXT NOT NULL UNIQUE,
  products_covered    TEXT[] NOT NULL,
  committed_spend_inr NUMERIC(14,4) NOT NULL,
  discount_rate       NUMERIC(5,4) NOT NULL CHECK (discount_rate <= 0.40),
  custom_sla          JSONB NOT NULL,         -- Per-product SLA overrides
  contract_start      DATE NOT NULL,
  contract_end        DATE NOT NULL,
  auto_renew          BOOLEAN NOT NULL DEFAULT FALSE,
  signed_by_tenant    UUID NOT NULL,
  signed_by_platform  UUID NOT NULL,          -- Platform Finance Officer
  signed_at           TIMESTAMPTZ NOT NULL,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Marketplace economic health (R67 inheritance)
CREATE TABLE ecoskiller.marketplace_liquidity_index (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  snapshot_date       DATE NOT NULL,
  product_id          TEXT NOT NULL,
  demand_index        NUMERIC(8,4) NOT NULL,  -- Rolling 30-day call volume normalized
  supply_index        NUMERIC(8,4) NOT NULL,  -- Active producers/content providers
  liquidity_score     NUMERIC(5,4) NOT NULL,  -- Composite health score 0.0–1.0
  incentive_triggered BOOLEAN NOT NULL DEFAULT FALSE,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

## SECTION 8 — KAFKA EVENT SCHEMA (PRODUCED BY THIS AGENT)

| Event Key | Trigger | Consumers |
|---|---|---|
| `api.usage.metered` | Every billable API call | Metering_Engine, Analytics_Service |
| `api.quota.near_limit` | Quota reaches 80% | Notification_Service |
| `api.quota.exceeded` | Quota hard-blocked | Notification_Service, Kong |
| `api.invoice.generated` | Monthly invoice created | Notification_Service, Billing_Service |
| `api.invoice.paid` | Invoice payment confirmed | Revenue_Distribution_Engine |
| `api.invoice.overdue` | Invoice past due date | Notification_Service, Admin_Governance |
| `revenue.share.calculated` | Commission split computed | Payout_Service, Analytics_Service |
| `payout.batch.created` | Payout batch assembled | Notification_Service (participant) |
| `payout.batch.completed` | Payout successfully processed | Notification_Service, Audit_Service |
| `payout.batch.failed` | Payout processing failed | Notification_Service, Admin_Governance |
| `api.product.deprecated` | Product status → deprecated | Notification_Service (all subscribers) |
| `api.product.sunset` | Product status → sunset | Notification_Service + Kong hard-block |
| `api.plan.changed` | Rate plan version changed | Notification_Service (affected tenants) |
| `marketplace.liquidity.alert` | Liquidity score < 0.30 | Analytics_Service, Admin_Governance |
| `enterprise.contract.signed` | New enterprise deal active | Billing_Service, Analytics_Service |
| `enterprise.contract.expiring` | Contract within 30 days of end | Notification_Service |

---

## SECTION 9 — API CONTRACT REGISTRY (ECONOMY APIS)

All APIs follow SemVer v1 initially. Breaking changes require v2 and 90-day parallel support (R19).

### 9.1 Developer Billing & Plan APIs

```
// Rate Plan Management
GET     /api/v1/economy/products                          → List all API products + current status
GET     /api/v1/economy/products/{product_id}             → Product detail + SLA + pricing
GET     /api/v1/economy/products/{product_id}/plans       → Available rate plans for product
POST    /api/v1/economy/subscriptions                     → Subscribe to a rate plan
GET     /api/v1/economy/subscriptions                     → List active subscriptions (tenant-scoped)
PATCH   /api/v1/economy/subscriptions/{id}/upgrade        → Upgrade plan
PATCH   /api/v1/economy/subscriptions/{id}/cancel         → Cancel subscription

// Quota & Usage
GET     /api/v1/economy/usage                             → Current period usage summary
GET     /api/v1/economy/usage/{product_id}                → Per-product usage detail
GET     /api/v1/economy/usage/history                     → Historical usage (paginated)

// Invoices
GET     /api/v1/economy/invoices                          → Invoice list (tenant-scoped)
GET     /api/v1/economy/invoices/{id}                     → Invoice detail + line items
GET     /api/v1/economy/invoices/{id}/download            → PDF invoice download
POST    /api/v1/economy/invoices/{id}/dispute             → Raise invoice dispute
```

### 9.2 Revenue & Payout APIs (Participant-Facing)

```
GET     /api/v1/economy/earnings/summary                  → Earnings summary for current period
GET     /api/v1/economy/earnings/history                  → Historical earnings (paginated)
GET     /api/v1/economy/earnings/transactions             → Per-transaction revenue share breakdown
GET     /api/v1/economy/payouts                           → Payout history
GET     /api/v1/economy/payouts/{id}                      → Payout batch detail
POST    /api/v1/economy/payouts/request                   → On-demand payout request (if eligible)
POST    /api/v1/economy/payout-rules                      → Configure payout preferences
PATCH   /api/v1/economy/payout-rules                      → Update payout preferences
```

### 9.3 Platform Admin APIs (OPA-gated: platform_admin only)

```
// Product Management
POST    /api/v1/admin/economy/products                    → Create new API product (draft)
PATCH   /api/v1/admin/economy/products/{product_id}/status → Promote/deprecate product
POST    /api/v1/admin/economy/plans                       → Create rate plan
PATCH   /api/v1/admin/economy/plans/{id}                  → Version-bump rate plan

// Commission Management
GET     /api/v1/admin/economy/commissions                 → View all commission rules
PATCH   /api/v1/admin/economy/commissions/{id}            → Update commission (dual-approval required)

// Payout Operations
GET     /api/v1/admin/economy/payouts/pending             → View pending payout queue
POST    /api/v1/admin/economy/payouts/{id}/authorize      → Authorize payout batch
POST    /api/v1/admin/economy/payouts/{id}/reject         → Reject payout batch

// Enterprise Contracts
POST    /api/v1/admin/economy/contracts                   → Create enterprise contract
GET     /api/v1/admin/economy/contracts                   → List all contracts
PATCH   /api/v1/admin/economy/contracts/{id}/renew        → Renew contract

// Marketplace Health
GET     /api/v1/admin/economy/marketplace/health          → Liquidity index dashboard
GET     /api/v1/admin/economy/marketplace/revenue         → Revenue breakdown by product, plan, participant
```

### 9.4 API Response Envelope (Inherited — All Economy APIs)

```json
{
  "success": true,
  "data": {},
  "meta": {
    "request_id": "uuid-v4",
    "timestamp_ms": 1700000000000,
    "api_version": "v1",
    "quota_remaining": 9450,
    "quota_limit": 10000,
    "quota_reset_at": "2025-02-01T00:00:00Z"
  },
  "error": null
}
```

---

## SECTION 10 — PARTNER PAYOUT SERVICE SPECIFICATION

```
SERVICE_NAME        = partner-payout-service
EXECUTION_MODEL     = ASYNC (Apache Airflow DAGs + manual authorization gate)
HUMAN_GATE          = MANDATORY — No payout executes without Finance Officer authorization
AI_ROLE             = Advisory only (R28) — AI may flag anomalies, NEVER approve payouts
PAYMENT_PROCESSORS  = Razorpay (primary, INR), Stripe (secondary, USD/international)
VAULT_DEPENDENCY    = Participant bank credentials stored in HashiCorp Vault only
IDEMPOTENCY         = Enforced — payout batch IDs are idempotent across retries
TDS_ENGINE          = Delegated to Billing_Service tax module
```

### 10.1 Payout Processing Workflow (Apache Airflow DAG)

```
DAG_ID              = ecoskiller.payout.monthly
SCHEDULE            = 0 9 5 * *   (5th of every month, 09:00 IST)
MAX_ACTIVE_RUNS     = 1           (no concurrent payout runs)
```

**DAG Steps (Sealed — No Reordering):**

```
Step 1: ASSEMBLE_PERIOD
  - Lock billing period (period_start, period_end)
  - Pull all revenue_distribution_ledger rows where payout_status = 'pending'
  - Group by participant_id + participant_type
  - Calculate gross_payable, TDS, net_payable per participant
  - Enforce minimum_payout_inr rule (skip if below threshold)
  - Create payout_batches records (status = 'pending')
  - Emit: payout.batch.created events

Step 2: FRAUD_CHECK
  - AI anomaly scan on payout amounts vs. 6-month baseline (advisory, R28)
  - Flag outliers (>3σ deviation) for human review
  - If flagged: status → 'disputed', skip Step 3, route to Admin_Governance

Step 3: HUMAN_AUTHORIZATION_GATE
  - System STOPS here — waits for Finance Officer input
  - Finance Officer reviews payout queue in Admin Ops Console
  - Authorized batches: status → 'processing'
  - Rejected batches: status → 'failed', reason logged

Step 4: PAYMENT_DISPATCH
  - For each authorized batch:
    * Fetch bank/UPI credentials from Vault
    * Submit to Razorpay (INR) or Stripe (USD)
    * Record payment gateway reference
    * Mark payout_batch: status → 'completed'
  - On gateway failure: retry 3× with exponential backoff
  - On 3× failure: status → 'failed', Notification_Service alert

Step 5: AUDIT_CLOSE
  - Mark revenue_distribution_ledger rows: payout_status → 'paid'
  - Generate payout receipt document (stored in MinIO)
  - Emit: payout.batch.completed events
  - Log to immutable Audit_Service
```

### 10.2 Payout Security Rules

- **No payout under ₹500** — accumulates to next cycle
- **No payout to unverified bank accounts** — KYC verification required (Platform Admin approval)
- **TDS deduction mandatory** for Indian residents at applicable rate (10% default)
- **Dual approval required** for payouts >₹1,00,000 (Platform Super Admin + Finance Officer)
- **All payout credentials stored exclusively in Vault** — never in PostgreSQL
- **Payout history is immutable** — no deletion, no update post-dispatch

---

## SECTION 11 — API LIFECYCLE MANAGER (R19 INHERITANCE)

### 11.1 Version Governance (LOCKED)

```
VERSION_SCHEME      = SemVer MAJOR.MINOR.PATCH
BREAKING_CHANGE     = MAJOR bump + 90-day parallel support minimum
NEW_CAPABILITY      = MINOR bump (backward compatible)
BUG_FIX             = PATCH bump (backward compatible)
DEPRECATION_NOTICE  = 90-day minimum advance notice via:
                      - Email to all active subscribers
                      - Deprecation header: Deprecation: true
                      - Link header: Link: <migration_url>; rel="deprecation"
SUNSET_HEADER       = Sunset: <RFC7231 date> (added 30 days before sunset)
```

### 11.2 Lifecycle State Machine

```
draft → beta → ga → deprecated → sunset

Rules:
- draft: Internal only, no external access
- beta: Developer preview, no SLA guarantee, 0% billing
- ga: Full commercial, SLA active, billing live
- deprecated: Still functional, sunset date announced, new subscriptions blocked
- sunset: HTTP 410 returned, no calls processed, migration URL in body
```

### 11.3 Required Headers for Deprecated/Sunset Products

```http
HTTP/1.1 200 OK
Deprecation: true
Link: <https://docs.ecoskiller.com/api/v2/jobs/migration>; rel="deprecation"
Sunset: Sat, 01 Feb 2026 00:00:00 GMT
X-Ecoskiller-API-Version: v1
X-Ecoskiller-Deprecation-Date: 2025-11-01T00:00:00Z
```

---

## SECTION 12 — API ANALYTICS ENGINE

### 12.1 ClickHouse Analytics Tables

```sql
CREATE TABLE ecoskiller_analytics.api_usage_stats (
  tenant_id           UUID,
  developer_id        UUID,
  product_id          String,
  plan_type           String,
  endpoint            String,
  http_method         String,
  http_status         UInt16,
  units_consumed      Float64,
  billable            Bool,
  gross_amount_inr    Float64,
  usage_date          Date,
  usage_hour          UInt8,
  duration_ms         UInt32,
  region              String,
  created_at          DateTime
) ENGINE = MergeTree()
ORDER BY (tenant_id, usage_date, product_id);

CREATE TABLE ecoskiller_analytics.revenue_stats (
  participant_id      UUID,
  participant_type    String,
  product_id          String,
  transaction_type    String,
  gross_amount_inr    Float64,
  commission_rate     Float64,
  share_amount_inr    Float64,
  revenue_date        Date,
  payout_status       String,
  created_at          DateTime
) ENGINE = MergeTree()
ORDER BY (participant_id, revenue_date, product_id);

CREATE TABLE ecoskiller_analytics.payout_stats (
  participant_id      UUID,
  participant_type    String,
  payout_period       String,
  gross_payable_inr   Float64,
  tds_inr             Float64,
  net_paid_inr        Float64,
  payment_method      String,
  payment_status      String,
  payout_date         Date,
  created_at          DateTime
) ENGINE = MergeTree()
ORDER BY (participant_id, payout_date);

CREATE TABLE ecoskiller_analytics.marketplace_liquidity_stats (
  product_id          String,
  demand_index        Float64,
  supply_index        Float64,
  liquidity_score     Float64,
  snapshot_date       Date,
  created_at          DateTime
) ENGINE = MergeTree()
ORDER BY (product_id, snapshot_date);
```

### 12.2 Developer-Facing Analytics (Read-Only Access)

Developers and participants access analytics through the Developer Billing Portal. Queries are served from ClickHouse via a read-only materialized view layer. Direct ClickHouse access is forbidden for external callers.

Available developer analytics:
- API call volume by product, endpoint, date range
- Error rate breakdown (4xx vs 5xx) by product
- P50/P95/P99 latency by endpoint
- Quota utilization trend (daily/weekly/monthly)
- Billing forecast (current period projected cost)
- Revenue earnings breakdown (trainer/institute/vendor participants)
- Payout history timeline

---

## SECTION 13 — OBSERVABILITY SPECIFICATION (MANDATORY)

### 13.1 Prometheus Metrics

```
# API Economy metrics
api_calls_total{tenant_id, product_id, plan_type, status}           Counter
api_call_duration_ms{product_id, endpoint, method}                  Histogram (P50, P95, P99)
api_quota_utilization{tenant_id, product_id}                        Gauge (0.0–1.0)
api_quota_blocks_total{tenant_id, product_id}                       Counter
api_revenue_inr_total{product_id, plan_type}                        Counter
api_invoice_generated_total{tenant_id, status}                      Counter
api_invoice_overdue_total                                            Gauge
revenue_distribution_pending_inr                                     Gauge
payout_batch_processing_duration_ms                                  Histogram
payout_failure_total{participant_type, payment_method}              Counter
marketplace_liquidity_score{product_id}                             Gauge
enterprise_contracts_active                                          Gauge
enterprise_contracts_expiring_30d                                    Gauge
```

### 13.2 Grafana Dashboard Panels (Mandatory — 12 panels)

1. API call volume by product — time-series, 7-day
2. Revenue generated (INR) by product — daily bar chart
3. Quota utilization heatmap — tenant × product
4. Top 10 tenants by API spend — current month
5. Error rate by product — time-series
6. API latency P95 by endpoint — time-series
7. Pending payout queue (INR) — real-time gauge
8. Revenue distribution breakdown (pie) — Platform vs participants
9. Marketplace liquidity scores — product grid
10. Payout completion rate — monthly trend
11. Trial conversion rate — funnel chart
12. Enterprise contract health — table (tenant, contract value, days remaining)

### 13.3 Alert Rules

```yaml
- alert: APIQuotaHighUtilization
  condition: api_quota_utilization > 0.80 for any tenant/product
  severity: warning
  action: Emit api.quota.near_limit event

- alert: APIQuotaExceeded
  condition: api_quota_utilization >= 1.0
  severity: critical
  action: Kong blocks requests + emit api.quota.exceeded

- alert: InvoiceOverdue
  condition: api_invoice_overdue_total > 0 for > 7 days
  severity: warning

- alert: PayoutBatchFailed
  condition: payout.batch.failed event received
  severity: critical
  action: Page Finance Officer + Admin_Governance

- alert: MarketplaceLiquidityLow
  condition: marketplace_liquidity_score < 0.30 for 24h
  severity: warning

- alert: RevenueAnomaly
  condition: api_revenue_inr_total drops > 30% week-on-week
  severity: critical

- alert: EnterpriseContractExpiring
  condition: enterprise_contracts_expiring_30d > 0
  severity: info
  action: Emit enterprise.contract.expiring event
```

---

## SECTION 14 — UI SCREENS (DEVELOPER BILLING PORTAL)

**Binding Law:** Flutter-primary, Next.js (SEO read-only clone) secondary. WCAG 2.1 AA mandatory. Dashboard 40% fixed / 60% customizable (inherited). Stage 3: Ecosystem UI.

### 14.1 Screen Inventory

| Screen ID | Screen Name | Target Role | Module | Stage |
|---|---|---|---|---|
| ECO-001 | Developer Billing Portal Home | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-002 | API Product Catalog Browser | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-003 | API Product Detail & Pricing | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-004 | Rate Plan Selector | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-005 | My Subscriptions Dashboard | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-006 | Usage & Quota Monitor | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-007 | Usage History Explorer | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-008 | Invoice List | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-009 | Invoice Detail & Download | developer, tenant_admin | API_Economy_UI | STAGE_3 |
| ECO-010 | Earnings Dashboard (Participant) | trainer, institute_admin, plugin_vendor | API_Economy_UI | STAGE_3 |
| ECO-011 | Payout History & Status | trainer, institute_admin, plugin_vendor | API_Economy_UI | STAGE_3 |
| ECO-012 | Payout Preferences Setup | trainer, institute_admin, plugin_vendor | API_Economy_UI | STAGE_3 |
| ECO-013 | Revenue Analytics Dashboard | developer, tenant_admin, trainer | API_Economy_UI | STAGE_3 |
| ECO-014 | Admin — Payout Authorization Queue | platform_admin, finance_officer | ERP_UI | STAGE_3 |
| ECO-015 | Admin — Marketplace Economic Health | platform_admin | ERP_UI | STAGE_3 |

**Max 15 screens per run. All 15 must be generated. Partial output = INVALID.**

### 14.2 Per-Screen Output Requirements (R29 Law Inherited)

Each screen generation MUST produce all 12 R29-mandated outputs:
1. Screen purpose definition
2. Entry/exit navigation points
3. Wireframe layout (header / body / actions / footer)
4. Flutter widget tree (full, not skeleton)
5. Navigation route + OPA-enforced role access rule
6. State management: Riverpod (platform standard)
7. API endpoints consumed by screen
8. Loading / empty / error / success states (all 4 mandatory)
9. WCAG 2.1 AA accessibility annotations
10. Animation rules (250–300ms transitions, 100ms button feedback)
11. Design token references (PRIMARY #1E3A8A, ACCENT #10B981 — sealed)
12. Next.js component mirror specification

### 14.3 Screen-Specific Rules

- **ECO-006 (Quota Monitor):** Must display real-time quota bar (consumed/limit), color transitions: green → amber (80%) → red (95%) → blocked (100%). Refresh interval: 30 seconds.
- **ECO-009 (Invoice):** PDF download triggers MinIO presigned URL generation (1-hour expiry). Invoice must display GST breakdown clearly.
- **ECO-010 (Earnings):** Earnings shown are ledger-confirmed amounts. Pending payouts shown separately with status badge. Never show unconfirmed estimates as earnings.
- **ECO-012 (Payout Prefs):** Bank account and UPI details are write-only inputs. After submission, only last 4 digits of account displayed. Vault storage confirmed visually.
- **ECO-014 (Payout Auth Queue):** Dual-approval UI — two Finance Officer approvals required for batches >₹1,00,000. Batch detail shows anomaly flags from AI advisory layer. Approve/Reject requires step-up authentication (MFA re-prompt).

---

## SECTION 15 — DEVOPS & DEPLOYMENT SPECIFICATION

### 15.1 Microservices Deployed by This Agent

| Service Name | Namespace | Replicas | Primary Function |
|---|---|---|---|
| `api-metering-consumer` | ecoskiller-core | min:2, max:8 | Kafka consumer → PostgreSQL ledger writer |
| `api-economy-service` | ecoskiller-core | min:2, max:6 | REST API for developer billing/plan management |
| `revenue-distribution-worker` | ecoskiller-billing | min:1, max:4 | Commission calculator (async, event-driven) |
| `payout-orchestrator` | ecoskiller-billing | min:1, max:2 | Airflow-triggered payout DAG orchestrator |
| `api-lifecycle-manager` | ecoskiller-core | min:1, max:2 | Product status transitions, deprecation enforcement |
| `marketplace-liquidity-monitor` | ecoskiller-analytics | min:1, max:2 | R67 liquidity monitoring cron |

### 15.2 Environment Variables (No Hardcoded Values)

```
DATABASE_URL                     → PostgreSQL (Vault)
REDIS_URL                        → Redis (Vault)
KAFKA_BROKERS                    → Kafka brokers (ENV)
CLICKHOUSE_URL                   → ClickHouse analytics DB (Vault)
VAULT_ADDR                       → Vault address (ENV)
VAULT_TOKEN                      → Kubernetes mounted secret
RAZORPAY_KEY_ID                  → Vault path: secret/payments/razorpay/key_id
RAZORPAY_KEY_SECRET              → Vault path: secret/payments/razorpay/key_secret
STRIPE_SECRET_KEY                → Vault path: secret/payments/stripe/secret_key
AIRFLOW_API_URL                  → Internal Airflow endpoint (ENV)
MINIMUM_PAYOUT_INR               → Default 500 (ENV, configurable)
ENTERPRISE_CONTRACT_RENEWAL_ALERT_DAYS → Default 30 (ENV)
KONG_ADMIN_API_URL               → Internal Kong admin (ENV)
METERING_KAFKA_TOPIC             → ecoskiller.metering.events (ENV)
```

### 15.3 CI/CD Pipeline Stages

```
Stage 1: Contract Validation — OPA policy lint, API schema validation, commission formula validation
Stage 2: Unit Tests — min 85% coverage enforced
Stage 3: Integration Tests — PostgreSQL + Redis + Kafka + ClickHouse (dockerized)
Stage 4: Ledger Immutability Tests — verify UPDATE/DELETE triggers fire correctly
Stage 5: Payout Dual-Approval Tests — verify authorization gate cannot be bypassed
Stage 6: Security Scan — OWASP, dependency check, container vulnerability scan
Stage 7: Docker Build — multi-stage, distroless base image
Stage 8: Image Push — tagged with commit SHA + semver
Stage 9: Helm Deploy — staging namespace
Stage 10: Smoke Tests — end-to-end API metering → invoice generation → ledger validation
Stage 11: Production Promote — blue/green, automatic rollback on revenue anomaly
```

---

## SECTION 16 — INTERN-EXECUTABLE INSTRUCTIONS (R26 COMPLIANCE)

All code artifacts generated under this agent MUST include:

1. **File path and filename** (exact)
2. **Complete code** (no pseudocode, no ellipsis)
3. **Inline comments** explaining every non-obvious section
4. **Dependency map** (which services/tables this file touches)
5. **Run command** (e.g., `npm run start`, `python -m payout_worker`)
6. **Expected success output** (exact log line)

Example structure required:

```python
# FILE: src/workers/revenue_distribution_worker.py
# PURPOSE: Calculates commission splits for every invoiced transaction
# CONNECTS TO: revenue_distribution_ledger (PostgreSQL), Kafka topic api.invoice.paid
# DEPENDS ON: commission_rules config (loaded from DB at startup)
# RUN: python -m src.workers.revenue_distribution_worker
# EXPECTED OUTPUT: [INFO] Revenue distribution worker ready. Consuming: api.invoice.paid
```

---

## SECTION 17 — FAILURE REGISTRY INTEGRATION (R38 COMPLIANCE)

The following bug case ranges MUST be recorded in the Master Bug & Failure Registry:

| Bug ID Range | Layer | Focus Area |
|---|---|---|
| ECO-001 – ECO-040 | Metering Engine | Duplicate metering, missed calls, immutability bypass attempts |
| ECO-041 – ECO-080 | Rate Plan Engine | Quota bypass, wrong plan applied, Redis counter drift |
| ECO-081 – ECO-120 | Revenue Distribution | Wrong commission rate, float rounding errors, cross-tenant split |
| ECO-121 – ECO-160 | Payout Service | Double payout, authorization gate bypass, Vault credential failure |
| ECO-161 – ECO-200 | API Lifecycle | Sunset not enforced, deprecated headers missing, version mismatch |
| ECO-201 – ECO-240 | Developer Billing Portal UI | WCAG failures, earnings shown before confirmation, incorrect quota display |
| ECO-241 – ECO-250 | Marketplace Liquidity | False liquidity alerts, index calculation drift |

Minimum 250 distinct bug cases for this agent's scope. Absence = STOP EXECUTION.

---

## SECTION 18 — ECONOMIC ABUSE CONTROLS (T15 INHERITANCE)

The following economic abuse patterns MUST be detected and acted upon:

| Abuse Type | Detection Method | Action |
|---|---|---|
| API quota gaming (multi-account) | Cross-account usage fingerprint (IP, device) | Flag for manual review + trial cancellation |
| Fake referral chains | Graph cycle detection in referral_tracking | Referral reward nullified + account review |
| Refund abuse | Refund rate > 20% of invoiced amount in 60 days | Escalate to Admin_Governance + payment hold |
| Trainer payout inflation (colluding reviews) | Statistical anomaly on review-to-payout correlation | Flag payout batch, AI advisory report |
| Plugin vendor fake install counts | Install origin IP clustering | Plugin suspended + vendor notified |
| Free tier multi-account farming | Identity fingerprint (email domain, device) | Free tier suspended, primary account retained |
| Invoice dispute flooding | > 3 disputes in 30 days | Rate limit dispute submissions + manual review required |
| Enterprise contract misrepresentation | Usage vs. committed spend deviation >50% | Finance Officer notified + contract review |

All detected abuse events emit to `Admin_Governance` Kafka topic and are logged in the platform audit trail.

---

## SECTION 19 — PRODUCTION READINESS GATES (AGENT-SPECIFIC)

Before this agent's module may be declared production-ready, all gates must pass:

```
☐ All 9 PostgreSQL tables created with RLS policies and immutability triggers
☐ api_usage_ledger immutability trigger tested (UPDATE + DELETE both rejected)
☐ revenue_distribution_ledger immutability trigger tested
☐ All 15 API products in product catalog with correct metering_type
☐ Kong quota plugin configured for all 15 products with Redis backend
☐ Quota headers (X-RateLimit-*) verified on every API response
☐ Free tier hard-block tested (429 returned at quota limit)
☐ Pay-as-you-go metering verified end-to-end (call → Kafka → ledger)
☐ Tiered volume pricing breakpoints verified for all products
☐ Enterprise contract dual-approval flow tested in staging
☐ Commission calculation verified for all 6 transaction types (§6.2)
☐ Revenue distribution ledger split accuracy verified (sum of shares = gross_amount)
☐ Payout Airflow DAG tested end-to-end in staging (all 5 steps)
☐ Payout authorization gate tested — payout blocked without Finance Officer action
☐ Payout >₹1,00,000 dual-approval requirement tested
☐ TDS deduction applied correctly on payout
☐ All 15 Kafka event types emitting correctly
☐ All 4 ClickHouse analytics tables populated with live data
☐ All 12 Grafana panels rendering with live data
☐ All 7 Prometheus alert rules firing correctly in test environment
☐ API product deprecation headers verified (Deprecation + Link + Sunset headers present)
☐ API product sunset enforced (HTTP 410 returned post-sunset date)
☐ All 15 Developer Billing Portal screens generated (Flutter + Next.js)
☐ WCAG 2.1 AA accessibility audit passed on all 15 screens
☐ Payout preferences stored exclusively in Vault (never in PostgreSQL plaintext)
☐ Economic abuse detection tested (quota gaming, refund abuse, fake installs)
☐ CI/CD pipeline passing all 11 stages
☐ Intern-executable instructions present on all code artifacts (R26)

ALL GATES MUST PASS → STATUS = PRODUCTION_READY
ANY GATE FAILED    → STOP EXECUTION → REPORT FAILED GATE → NO DEPLOYMENT CLAIM
```

---

## SECTION 20 — ANTIGRAVITY RUN COMMAND

```
GENERATE_API_ECONOMY_MODULE

TENANT_ID         = {UUID}
TARGET_ROLE       = {developer | trainer | institute_admin | plugin_vendor | platform_admin | finance_officer}
SCREEN_SET        = {ECO-001 through ECO-015 | ALL}
STAGE             = STAGE_3
ENTITY_STATE      = {plan_status | invoice_status | payout_status | product_status}
OUTPUT_FORMAT     = {FLUTTER_CODE | API_SPEC | DB_MIGRATION | KAFKA_SCHEMA | ALL}
```

**Inherited Limits (UI Constitution):**
- `MAX_SCREENS_PER_RUN = 15` — do not exceed
- `MAX_MODULES_PER_RUN = 1` — this module only per run
- `MAX_ROLES_PER_RUN = 1`
- Exceeding any limit → STOP EXECUTION

---

## 🔒 FINAL SEAL

```
API_ECONOMY_AGENT.md

STATUS              = SEALED
VERSION             = 1.0.0
CHANGE_POLICY       = APPEND_ONLY
MUTATION_POLICY     = ADD_ONLY (version bump required for any addition)
CREATIVE_DEVIATION  = PERMANENTLY FORBIDDEN
ASSUMPTION_FILLING  = PERMANENTLY FORBIDDEN
ANTIGRAVITY_CONFUSION = IMPOSSIBLE

INHERITS_FROM:
  ✔ ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0
  ✔ SEALED_UI_CONSTITUTION
  ✔ WEBHOOK_MARKETPLACE_AGENT.md (never duplicates, inherits key infra)
  ✔ R19 (API Versioning & Deprecation Law)
  ✔ R20 (Accessibility Law)
  ✔ R26 (Intern Execution Law)
  ✔ R28 (Intelligence Cost Law — AI advisory only)
  ✔ R29 (UI Generation Law)
  ✔ R31 (Dual Frontend Law)
  ✔ R38 (Bug Registry Law)
  ✔ R39 (Core Inbuilt Platform Tools — Billing & Commercial)
  ✔ R65 (Open Platform Extensibility Law)
  ✔ R67 (Marketplace Liquidity Balancing Law)
  ✔ T15 (Economic Abuse Controls)
  ✔ §8 Marketplace Economic Governance (Commission & Revenue Distribution)

PLATFORM            = ECOSKILLER
EXECUTION_ENGINE    = ANTIGRAVITY
AGENT               = ApiEconomyAgent
AUTHOR_AUTHORITY    = HUMAN DECLARATION ONLY
PAYOUT_AUTHORITY    = HUMAN ONLY (Finance Officer + Super Admin)
AI_ROLE_IN_PAYOUTS  = ADVISORY ONLY — NEVER APPROVAL

ANY DEVIATION FROM THIS DOCUMENT
= STOP EXECUTION
= REPORT DEVIATION CLASS + SECTION VIOLATED
= NO PARTIAL OUTPUT PERMITTED
= NO DEPLOYMENT CLAIM PERMITTED
```

---

*End of API_ECONOMY_AGENT.md — Document is sealed and locked.*
