---
🔒 SEALED & LOCKED DOCUMENT
DOCUMENT_CLASS     = PRICING_STRATEGY
EXECUTION_ENGINE   = ANTIGRAVITY
PLATFORM           = ECOSKILLER
VERSION            = 1.0.0
STATUS             = LOCKED · SEALED · APPROVED_DRAFT
MUTATION_POLICY    = APPEND_ONLY (human approval required)
CREATIVE_DEVIATION = FORBIDDEN
CONFLICT_POLICY    = DENY
---

# 💰 ECOSKILLER PRICING STRATEGY
## Antigravity Execution Reference — Sealed & Locked

---

## SECTION 1 — PRICING ARCHITECTURE OVERVIEW

### Platform Context (READ-ONLY)
Ecoskiller is an **Enterprise Multi-Tenant SaaS** platform spanning:
- Job Portal Engine
- Skill Development Engine
- Project Execution Engine
- Group Discussion (Dojo) Engine
- ERP for Enterprises / Institutes / Colleges / Recruiters

The pricing model MUST reflect:
- Multi-tenant isolation (Institute ≠ Company ≠ Platform)
- Role-based entitlement gating (Student, Trainer, Evaluator, Recruiter, Institute, Enterprise, Admin)
- Domain isolation (Arts | Commerce | Science | Technology | Administration)
- Four-stage development rollout (Foundation → Intelligence → Ecosystem → Compliance)

---

## SECTION 2 — PRICING MODEL STRUCTURE

### 2.1 Core Pricing Dimensions

| Dimension | Description |
|---|---|
| **Subscription Plans** | Monthly + Annual billing cycles |
| **Seat-Based Pricing** | Per-user licensing for Enterprise / Institute tenants |
| **Mentor License Pricing** | Per-trainer/evaluator seat |
| **Metered Usage** | Dojo match hours, recorded sessions, AI evaluations |
| **Transaction Fees** | Tournament entry fees, certification fees |
| **Revenue Share** | Trainer course revenue splits |

### 2.2 Pricing Pillars
```
PRICING_PILLARS =
  SUBSCRIPTION      → Recurring revenue base
  SEATS             → Enterprise & Institute scaling
  METERED_USAGE     → Dojo + AI consumption
  TRANSACTIONS      → Certification + Tournaments
  MARKETPLACE       → Trainer revenue share
  ENTERPRISE_ERP    → Custom contract floor
```

---

## SECTION 3 — PLAN TIERS (BY USER SEGMENT)

---

### 3.1 STUDENT PLANS

#### FREE TIER
```
PLAN_ID            = STUDENT_FREE
TARGET             = Individual students (onboarding, trial)
BILLING_CYCLE      = None
PRICE              = ₹0 / $0
```

**Inclusions:**
- Profile creation + skill tagging
- View job listings (no application tracking)
- 2 Dojo practice matches/month
- Limited skill gap analysis (top 3 skills)
- Public portfolio page (read-only)
- No certification access
- No project execution access
- AI match score: view only, no explanation

**Feature Gate Enforcement:**
```
certification          → DENIED
dojo_rooms_monthly     → LIMIT: 2
skill_gap_depth        → LIMIT: top_3
project_execution      → DENIED
ai_explanation         → DENIED
analytics              → DENIED
```

---

#### BASIC PLAN (STUDENT)
```
PLAN_ID            = STUDENT_BASIC
TARGET             = Active job-seeking students
BILLING_CYCLE      = Monthly | Annual
PRICE_MONTHLY      = ₹299 / $4
PRICE_ANNUAL       = ₹2,499 / $33  (30% savings)
```

**Inclusions:**
- All Free features
- Full job application tracking + eligibility explanation
- 10 Dojo matches/month (live + recorded)
- Full skill gap analysis + learning path
- 2 project executions/month
- AI match score + explanation
- Basic portfolio with evidence attachments
- Belt progression tracking (Dojo)

**Feature Gate Enforcement:**
```
dojo_rooms_monthly     → LIMIT: 10
project_executions     → LIMIT: 2/month
certification          → DENIED
ai_advanced_analytics  → DENIED
```

---

#### PRO PLAN (STUDENT)
```
PLAN_ID            = STUDENT_PRO
TARGET             = Power users, placement-focused students
BILLING_CYCLE      = Monthly | Annual
PRICE_MONTHLY      = ₹599 / $8
PRICE_ANNUAL       = ₹4,999 / $66  (30% savings)
```

**Inclusions:**
- All Basic features
- Unlimited Dojo matches
- Certification access (pay-per-cert or bundled 2/year)
- Full project execution (unlimited)
- Advanced AI analytics + placement probability score
- Offer acceptance prediction
- Portfolio auto-generation
- Priority application visibility to recruiters
- Belt ladder + tournament entry (2 free/month)
- Mentor feedback access

**Feature Gate Enforcement:**
```
dojo_rooms_monthly     → UNLIMITED
certification          → INCLUDED (2/year)
project_executions     → UNLIMITED
ai_advanced_analytics  → ENABLED
placement_probability  → ENABLED
tournament_entry       → 2 free/month (pay-per beyond)
mentor_feedback        → ENABLED
```

---

### 3.2 TRAINER / MENTOR PLANS

#### TRAINER LITE
```
PLAN_ID            = TRAINER_LITE
TARGET             = Part-time trainers, guest mentors
BILLING_CYCLE      = Monthly | Annual
PRICE_MONTHLY      = ₹799 / $10
PRICE_ANNUAL       = ₹6,999 / $88
```

**Inclusions:**
- Trainer profile + skill listing
- Accept up to 10 mentoring sessions/month
- Host up to 5 Dojo rooms/month
- Revenue dashboard (basic earnings view)
- Course pricing editor
- Revenue share: Platform takes **20%** of trainer earnings

---

#### TRAINER PRO
```
PLAN_ID            = TRAINER_PRO
TARGET             = Full-time trainers, professional mentors
BILLING_CYCLE      = Monthly | Annual
PRICE_MONTHLY      = ₹1,999 / $25
PRICE_ANNUAL       = ₹17,999 / $225
```

**Inclusions:**
- All Trainer Lite features
- Unlimited mentoring sessions
- Unlimited Dojo room hosting
- Co-teaching collaboration + revenue split tooling
- Price experiment toggles (A/B pricing)
- Revenue forecast model
- Trainer market demand dashboard
- Payout request workflow (instant/scheduled)
- Tax deduction reports (GST/TDS)
- Revenue share: Platform takes **15%** of trainer earnings

---

### 3.3 EVALUATOR PLANS

```
PLAN_ID            = EVALUATOR_LICENSE
TARGET             = Certified evaluators / GD judges
BILLING_CYCLE      = Monthly
PRICE_MONTHLY      = ₹999 / $13
```

**Inclusions:**
- Evaluator profile + domain badge
- Access to assigned Dojo evaluation rooms
- Scoring + rubric access
- Override logging (all overrides auto-audited)
- Evaluator analytics (accuracy, consistency scores)
- Anti-cheat enforcement tools

---

### 3.4 INSTITUTE PLANS

#### INSTITUTE STARTER
```
PLAN_ID            = INSTITUTE_STARTER
TARGET             = Small colleges (< 500 students)
BILLING_CYCLE      = Annual (mandatory)
PRICE_ANNUAL       = ₹49,999 / $650
INCLUDED_SEATS     = Up to 500 students + 10 trainers + 2 admins
```

**Inclusions:**
- Multi-tenant institute ERP
- TPO dashboard (cohort performance, hiring readiness)
- Student batch import
- Dojo rooms (100 concurrent max)
- Institute branding + domain isolation
- Compliance & audit reports
- Parent trust layer (read-only)
- Basic placement analytics
- No cross-institute data access

---

#### INSTITUTE GROWTH
```
PLAN_ID            = INSTITUTE_GROWTH
TARGET             = Mid-size institutes (500–5,000 students)
BILLING_CYCLE      = Annual (mandatory)
PRICE_ANNUAL       = ₹1,49,999 / $1,900
INCLUDED_SEATS     = Up to 5,000 students + 50 trainers + 10 admins
```

**Inclusions:**
- All Starter features
- AI-powered placement probability per student
- Custom skill track catalogs per institute
- Industry demand mapping
- Advanced cohort analytics + ROI dashboards
- Campus ambassador program management
- Recruiter partnership tools
- SLA: 99.5% uptime

---

#### INSTITUTE ENTERPRISE
```
PLAN_ID            = INSTITUTE_ENTERPRISE
TARGET             = Universities, large chains (5,000+ students)
BILLING_CYCLE      = Annual | Multi-year contract
PRICE              = Custom (minimum ₹4,99,999 / $6,500/year)
INCLUDED_SEATS     = Unlimited (fair use policy)
```

**Inclusions:**
- All Growth features
- Dedicated tenant instance
- Custom ERP integrations
- SLA: 99.9% uptime + dedicated support
- Compliance ERP (NAAC, UGC, audit readiness)
- Multi-campus support
- Platform-level admin access (scoped)
- White-label option available

---

### 3.5 ENTERPRISE (CORPORATE) PLANS

#### SME HIRING PLAN
```
PLAN_ID            = ENTERPRISE_SME
TARGET             = Small & Medium Enterprises (< 100 employees)
BILLING_CYCLE      = Monthly | Annual
PRICE_MONTHLY      = ₹4,999 / $65
PRICE_ANNUAL       = ₹44,999 / $580
INCLUDED_SEATS     = 5 HR/recruiter seats
```

**Inclusions:**
- Verified job posting (up to 10 active postings)
- AI match score + eligibility explanation per applicant
- Application tracking + offer locking
- SME reliability scoring (visible to candidates)
- Audit trail for all hiring actions
- Basic corporate hiring ERP

---

#### CORPORATE STANDARD
```
PLAN_ID            = ENTERPRISE_CORPORATE
TARGET             = Mid-size corporations (100–1,000 employees)
BILLING_CYCLE      = Annual
PRICE_ANNUAL       = ₹1,99,999 / $2,600
INCLUDED_SEATS     = 20 HR/recruiter seats + 5 ERP admin seats
```

**Inclusions:**
- All SME features
- Unlimited job postings
- Bulk applicant AI scoring
- Recruiter behavior analytics
- Offer acceptance prediction per candidate
- Corporate L1–L7 hierarchy enforcement
- Full corporate hiring ERP
- Integration with internal HR systems (API)

---

#### ENTERPRISE CUSTOM
```
PLAN_ID            = ENTERPRISE_CUSTOM
TARGET             = Large corporations (1,000+ employees), group companies
BILLING_CYCLE      = Annual | Multi-year
PRICE              = Custom contract (floor ₹9,99,999 / $13,000/year)
```

**Inclusions:**
- All Corporate Standard features
- Dedicated account manager
- Custom AI model tuning for domain-specific hiring
- SSO / SAML integration
- Custom compliance & audit ERP
- SLA: 99.9% uptime
- Multi-tenant sub-account management

---

### 3.6 RECRUITER / HR PORTAL PLANS

#### RECRUITER BASIC
```
PLAN_ID            = RECRUITER_BASIC
TARGET             = Independent recruiters, staffing agencies
BILLING_CYCLE      = Monthly | Annual
PRICE_MONTHLY      = ₹1,999 / $26
PRICE_ANNUAL       = ₹17,999 / $230
```

**Inclusions:**
- 5 active job postings
- Applicant match scores
- Application pipeline management
- Basic SME reliability score access

---

#### RECRUITER PRO
```
PLAN_ID            = RECRUITER_PRO
TARGET             = High-volume recruiters, RPO firms
BILLING_CYCLE      = Annual
PRICE_ANNUAL       = ₹59,999 / $775
```

**Inclusions:**
- Unlimited job postings
- Full AI match + placement analytics
- Bulk candidate scoring
- Dojo evaluation access (for pre-screening)
- Recruiter behavior analytics
- Offer acceptance prediction
- Priority listing in candidate search

---

## SECTION 4 — METERED & TRANSACTION PRICING

### 4.1 Certification Fees
```
CERTIFICATION_MODEL = Pay-per-certification (outside plan inclusions)

STANDARD_CERT      = ₹299 / $4 per certification
DOMAIN_CERT        = ₹599 / $8 per certification (specialized domain)
ENTERPRISE_CERT    = Bulk pricing available (min. 50 certs)
```

### 4.2 Dojo Tournament Entry
```
TOURNAMENT_ENTRY   = ₹99–₹499 per entry (event-based pricing)
PRIZE_POOL         = Platform funds pool from entry fees (net of 25% platform fee)
FREE_ENTRIES       = 2/month included in Student Pro plan
```

### 4.3 Dojo Usage Overage (Metered)
```
METERED_EVENTS =
  Extra Dojo match (beyond plan limit)    → ₹29 / $0.40 per match
  Extra recorded session storage (>5GB)   → ₹5 / $0.07 per GB/month
  AI evaluation overage (beyond plan)     → ₹15 / $0.20 per evaluation
```

### 4.4 Profile Boost (Job Visibility)
```
PROFILE_BOOST      = ₹49 / $0.65 per 7-day boost (student)
APPLICATION_CREDIT = ₹19 / $0.25 per priority application slot
```

### 4.5 Trainer Revenue Share
```
REVENUE_SHARE_MODEL =
  Trainer Lite plan   → Platform: 20% | Trainer: 80%
  Trainer Pro plan    → Platform: 15% | Trainer: 85%
  Enterprise trainer  → Negotiated (min. Platform: 10%)

TAX_HANDLING =
  GST applicable on platform fees (India)
  TDS deducted at source per Income Tax rules
  VAT handling for international payouts
```

---

## SECTION 5 — BILLING ENGINE REQUIREMENTS (LOCKED)

```
BILLING_ENGINE_REQUIREMENTS =

  PAYMENT_PRIMARY    = Stripe
  PAYMENT_SECONDARY  = Razorpay (India primary)
  ABSTRACTION        = REQUIRED (provider-swappable)

  INVOICE_GENERATION = MANDATORY (all plans)
  GST_VAT_HANDLING   = MANDATORY
  REFUND_WORKFLOW    = MANDATORY
  COUPON_SYSTEM      = MANDATORY
  PRORATION          = MANDATORY (plan upgrades/downgrades)
  DUNNING_MANAGEMENT = MANDATORY (failed payments)
  ANNUAL_DISCOUNT    = MINIMUM 25% vs monthly

  USAGE_METERING =
    Dojo matches
    Recorded session hours
    AI evaluation calls
    Certification issuances
    Active job postings (metered overage)

  ENTITLEMENT_CHECK  = MIDDLEWARE (every feature access)
  FEATURE_GATE_ON_EXPIRY = IMMEDIATE DOWNGRADE (grace: 3 days)
```

---

## SECTION 6 — ENTITLEMENT MATRIX (BILLING-GATED ACCESS)

```
AUTHZ-L ENFORCEMENT — ENTITLEMENT GATES

Free plan       → certification: DENIED
                  dojo_rooms:    LIMIT 2/month
                  projects:      DENIED
                  ai_analytics:  DENIED

Basic plan      → certification: DENIED
                  dojo_rooms:    LIMIT 10/month
                  projects:      LIMIT 2/month
                  ai_analytics:  basic only

Pro plan        → certification: 2/year included
                  dojo_rooms:    UNLIMITED
                  projects:      UNLIMITED
                  ai_analytics:  full (placement probability)
                  tournaments:   2 free/month

Institute plans → entitlement scoped to tenant seat count
Enterprise plans → entitlement scoped to HR seat count

Access without valid entitlement  ⇒  DENY (hard block)
Expired subscription              ⇒  IMMEDIATE feature downgrade
```

---

## SECTION 7 — DISCOUNTS & PROMOTIONS POLICY

### 7.1 Annual Billing Discount
```
ANNUAL_DISCOUNT_MINIMUM = 25%
ANNUAL_DISCOUNT_DISPLAY = "Save 25–30% vs monthly"
```

### 7.2 Campus Ambassador Program
```
AMBASSADOR_BENEFIT = Free Student Pro (3-month rolling)
CONDITION          = Active ambassador status maintained
```

### 7.3 Referral Rewards
```
REFERRAL_MODEL =
  Referrer: 500 platform points + 1 month plan extension (on 5th referral)
  Referred: 500 platform points on first activity completion
  Premium trial: Triggered at 5 confirmed referrals
```

### 7.4 Institutional Bulk Discount
```
BULK_DISCOUNT =
  500–2,000 seats:     10% additional discount
  2,001–10,000 seats:  20% additional discount
  10,001+ seats:       Negotiated custom pricing
```

### 7.5 Coupon System
```
COUPON_TYPES =
  PERCENTAGE_OFF  → e.g., 20% off first 3 months
  FLAT_OFF        → e.g., ₹500 off annual plan
  FREE_TRIAL      → e.g., 30-day Pro trial
  FEATURE_UNLOCK  → e.g., certification credit gift

COUPON_RESTRICTIONS =
  Per-user: 1 active coupon
  No stacking (unless explicitly configured)
  Expiry enforced at billing middleware
```

---

## SECTION 8 — REFUND & DISPUTE POLICY

```
REFUND_POLICY =

  SUBSCRIPTION_REFUND =
    Monthly plan:   No refund after 7-day window
    Annual plan:    Pro-rated refund within 30 days of renewal
    Enterprise:     Per contract terms

  CERTIFICATION_REFUND =
    Pre-exam:       Full refund (up to 24 hours before)
    Post-exam:      No refund

  TOURNAMENT_ENTRY =
    Cancellation 24h+ before:   Full refund
    No-show or disqualification: No refund

  DISPUTE_WORKFLOW =
    User submits via Support Ticket system
    Linked to billing_id + plan_id
    Admin approval required for refunds > ₹999 / $13
    Audit trail mandatory for all refunds
```

---

## SECTION 9 — TAX COMPLIANCE

```
TAX_REQUIREMENTS =

  INDIA_GST =
    GST registration mandatory (post threshold)
    18% GST on SaaS subscription services
    GST invoice with GSTIN mandatory
    B2B: Reverse charge where applicable

  TDS_INDIA =
    TDS on trainer payouts: Section 194J (10%)
    Platform to deduct and deposit

  INTERNATIONAL =
    VAT handling for EU customers (OSS registration if applicable)
    Zero-rated export for non-India B2B (with LUT)
    Currency handling: INR primary, USD secondary

  BILLING_ENGINE_MUST_SUPPORT =
    Auto-tax calculation by region
    Tax-inclusive pricing display (India)
    Tax-exclusive display (International B2B)
    Downloadable tax invoices
```

---

## SECTION 10 — REVENUE MODEL SUMMARY

```
REVENUE_STREAMS =

  PRIMARY:
    Subscription fees         → All plan tiers (recurring)
    Seat licensing            → Institute + Enterprise

  SECONDARY:
    Certification fees        → Per-transaction
    Tournament entry fees     → Per-event
    Trainer revenue share     → % of marketplace transactions
    Metered overage           → Dojo, AI, storage

  GROWTH:
    Profile boosts            → Optional upsell (student)
    Application credits       → Optional upsell (student)
    White-label contracts     → Enterprise custom

  TARGET_REVENUE_MIX (projection):
    Subscriptions             → 60%
    Enterprise + Institute    → 25%
    Marketplace (trainer)     → 10%
    Transactions + Boosts     → 5%
```

---

## SECTION 11 — PRICING GOVERNANCE RULES

```
GOVERNANCE =

  PRICE_CHANGES =
    Require ARCHITECT + BUSINESS_AUTHORITY approval
    30-day advance notice to existing subscribers
    Grandfathering minimum: 90 days for annual plans
    Silent price increases: FORBIDDEN

  PLAN_DEPRECATION =
    Minimum 90-day sunset notice
    Migration path must be defined
    No forced upgrade without written consent

  ENTITLEMENT_CHANGES =
    Feature removal from paid plan: FORBIDDEN without compensation
    Feature additions: May roll out without notice
    Downgrade enforcement: 3-day grace period maximum

  AUDIT_TRAIL =
    All pricing changes logged with timestamp + approver
    Billing disputes archived for 7 years
    Revenue records: IMMUTABLE
```

---

## SECTION 12 — STAGE-BASED PRICING ACTIVATION

Aligned with the Four-Stage Development Model:

```
STAGE_1 (FOUNDATION):
  ACTIVATE =
    Student Free tier
    Student Basic tier
    Institute Starter (early access)
    Core billing middleware
    Stripe/Razorpay integration

STAGE_2 (INTELLIGENCE):
  ACTIVATE =
    Student Pro tier (AI analytics unlocked)
    Recruiter Basic + Pro
    Metered AI evaluation billing
    Placement probability monetization

STAGE_3 (ECOSYSTEM):
  ACTIVATE =
    Trainer Lite + Pro plans
    Trainer revenue share system
    Enterprise SME + Corporate plans
    Tournament entry fee system
    Certification fee system

STAGE_4 (COMPLIANCE & SCALE):
  ACTIVATE =
    Enterprise Custom + Institute Enterprise
    Multi-year contract billing
    Advanced GST/VAT compliance
    Full audit + governance ERP billing
    White-label pricing tiers
```

---

## SECTION 13 — ANTIGRAVITY EXECUTION CONSTRAINTS

```
CONSTRAINTS FOR ANTIGRAVITY =

  AI_MAY_DO =
    Generate billing schema drafts
    Generate plan tier configurations
    Generate entitlement middleware code
    Generate invoice templates
    Generate Stripe/Razorpay integration code
    Generate coupon + refund workflow code
    Generate revenue split calculation logic
    Generate tax deduction logic

  AI_MAY_NOT_DO =
    Finalize pricing (human approval required)
    Activate real payment gateway
    Set live tax configuration
    Execute real refunds
    Approve billing disputes
    Sign payment processor contracts

  HUMAN_AUTHORITY_REQUIRED_FOR =
    Final price publication
    Tax jurisdiction selection
    Payment processor contractual setup
    Enterprise custom contract negotiation
    Refund approvals above threshold

  FAILURE_MODE =
    Billing check failure → DENY feature access (hard block)
    Payment gateway error → Retry with backoff (max 3 attempts)
    Entitlement inconsistency → STOP + alert compliance admin
```

---

## 🔐 FINAL PRICING STRATEGY SEAL

```
THIS DOCUMENT IS:

  ✔ SEALED
  ✔ LOCKED
  ✔ VERSION 1.0.0
  ✔ ANTIGRAVITY COMPATIBLE
  ✔ STAGE-ALIGNED (Foundation → Compliance)
  ✔ BILLING ENGINE COMPLIANT
  ✔ TAX COMPLIANT DRAFT (India GST + International)
  ✔ ENTITLEMENT GATING MAPPED
  ✔ HUMAN APPROVAL REQUIRED BEFORE LIVE ACTIVATION

  MUTATION_POLICY     = APPEND_ONLY
  CHANGE_AUTHORITY    = ARCHITECT + BUSINESS_OWNER
  FURTHER_CHANGES     = REQUIRE_VERSION_BUMP
  SILENT_CHANGES      = FORBIDDEN

  STATUS = DRAFT_APPROVED — AWAITING HUMAN RATIFICATION
           BEFORE PRODUCTION ACTIVATION
```

---

*Document generated by Claude (Antigravity-compatible) | Ecoskiller Enterprise SaaS | v1.0.0*
*This is a DRAFT PROPOSAL. Per Section M2 of the master prompt: "Final pricing strategy requires Human authority approval before system activation."*
